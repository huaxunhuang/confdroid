/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.graphics;


/**
 * This service's job is to collect aggregate rendering profile data. It
 * does this by allowing rendering processes to request an ashmem buffer
 * to place their stats into.
 *
 * Buffers are rotated on a daily (in UTC) basis and only the 3 most-recent days
 * are kept.
 *
 * The primary consumer of this is incident reports and automated metric checking. It is not
 * intended for end-developer consumption, for that we have gfxinfo.
 *
 * Buffer rotation process:
 * 1) Alarm fires
 * 2) onRotateGraphicsStatsBuffer() is sent to all active processes
 * 3) Upon receiving the callback, the process will stop using the previous ashmem buffer and
 *    request a new one.
 * 4) When that request is received we now know that the ashmem region is no longer in use so
 *    it gets queued up for saving to disk and a new ashmem region is created and returned
 *    for the process to use.
 *
 * @unknown 
 */
public class GraphicsStatsService extends android.view.IGraphicsStats.Stub {
    public static final java.lang.String GRAPHICS_STATS_SERVICE = "graphicsstats";

    private static final java.lang.String TAG = "GraphicsStatsService";

    private static final int SAVE_BUFFER = 1;

    private static final int DELETE_OLD = 2;

    private static final int AID_STATSD = 1066;// Statsd uid is set to 1066 forever.


    // This isn't static because we need this to happen after registerNativeMethods, however
    // the class is loaded (and thus static ctor happens) before that occurs.
    private final int mAshmemSize = android.graphics.GraphicsStatsService.nGetAshmemSize();

    private final byte[] mZeroData = new byte[mAshmemSize];

    private final android.content.Context mContext;

    private final android.app.AppOpsManager mAppOps;

    private final android.app.AlarmManager mAlarmManager;

    private final java.lang.Object mLock = new java.lang.Object();

    private java.util.ArrayList<android.graphics.GraphicsStatsService.ActiveBuffer> mActive = new java.util.ArrayList<>();

    private java.io.File mGraphicsStatsDir;

    private final java.lang.Object mFileAccessLock = new java.lang.Object();

    private android.os.Handler mWriteOutHandler;

    private boolean mRotateIsScheduled = false;

    @android.annotation.SystemApi
    public GraphicsStatsService(android.content.Context context) {
        mContext = context;
        mAppOps = context.getSystemService(android.app.AppOpsManager.class);
        mAlarmManager = context.getSystemService(android.app.AlarmManager.class);
        java.io.File systemDataDir = new java.io.File(android.os.Environment.getDataDirectory(), "system");
        mGraphicsStatsDir = new java.io.File(systemDataDir, "graphicsstats");
        mGraphicsStatsDir.mkdirs();
        if (!mGraphicsStatsDir.exists()) {
            throw new java.lang.IllegalStateException("Graphics stats directory does not exist: " + mGraphicsStatsDir.getAbsolutePath());
        }
        android.os.HandlerThread bgthread = new android.os.HandlerThread("GraphicsStats-disk", THREAD_PRIORITY_BACKGROUND);
        bgthread.start();
        mWriteOutHandler = new android.os.Handler(bgthread.getLooper(), new android.os.Handler.Callback() {
            @java.lang.Override
            public boolean handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case android.graphics.GraphicsStatsService.SAVE_BUFFER :
                        saveBuffer(((android.graphics.GraphicsStatsService.HistoricalBuffer) (msg.obj)));
                        break;
                    case android.graphics.GraphicsStatsService.DELETE_OLD :
                        deleteOldBuffers();
                        break;
                }
                return true;
            }
        });
        nativeInit();
    }

    /**
     * Current rotation policy is to rotate at midnight UTC. We don't specify RTC_WAKEUP because
     * rotation can be delayed if there's otherwise no activity. However exact is used because
     * we don't want the system to delay it by TOO much.
     */
    private void scheduleRotateLocked() {
        if (mRotateIsScheduled) {
            return;
        }
        mRotateIsScheduled = true;
        java.util.Calendar calendar = normalizeDate(java.lang.System.currentTimeMillis());
        calendar.add(java.util.Calendar.DATE, 1);
        mAlarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), android.graphics.GraphicsStatsService.TAG, this::onAlarm, mWriteOutHandler);
    }

    private void onAlarm() {
        // We need to make a copy since some of the callbacks won't be proxy and thus
        // can result in a re-entrant acquisition of mLock that would result in a modification
        // of mActive during iteration.
        android.graphics.GraphicsStatsService.ActiveBuffer[] activeCopy;
        synchronized(mLock) {
            mRotateIsScheduled = false;
            scheduleRotateLocked();
            activeCopy = mActive.toArray(new android.graphics.GraphicsStatsService.ActiveBuffer[0]);
        }
        for (android.graphics.GraphicsStatsService.ActiveBuffer active : activeCopy) {
            try {
                active.mCallback.onRotateGraphicsStatsBuffer();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.graphics.GraphicsStatsService.TAG, java.lang.String.format("Failed to notify '%s' (pid=%d) to rotate buffers", active.mInfo.mPackageName, active.mPid), e);
            }
        }
        // Give a few seconds for everyone to rotate before doing the cleanup
        mWriteOutHandler.sendEmptyMessageDelayed(android.graphics.GraphicsStatsService.DELETE_OLD, 10000);
    }

    @java.lang.Override
    public android.os.ParcelFileDescriptor requestBufferForProcess(java.lang.String packageName, android.view.IGraphicsStatsCallback token) throws android.os.RemoteException {
        int uid = android.os.Binder.getCallingUid();
        int pid = android.os.Binder.getCallingPid();
        android.os.ParcelFileDescriptor pfd = null;
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            mAppOps.checkPackage(uid, packageName);
            android.content.pm.PackageInfo info = mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, android.os.UserHandle.getUserId(uid));
            synchronized(mLock) {
                pfd = requestBufferForProcessLocked(token, uid, pid, packageName, info.getLongVersionCode());
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
            throw new android.os.RemoteException(("Unable to find package: '" + packageName) + "'");
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
        return pfd;
    }

    // If lastFullDay is true, pullGraphicsStats returns stats for the last complete day/24h period
    // that does not include today. If lastFullDay is false, pullGraphicsStats returns stats for the
    // current day.
    // This method is invoked from native code only.
    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    private void pullGraphicsStats(boolean lastFullDay, long pulledData) throws android.os.RemoteException {
        int uid = android.os.Binder.getCallingUid();
        // DUMP and PACKAGE_USAGE_STATS permissions are required to invoke this method.
        // TODO: remove exception for statsd daemon after required permissions are granted. statsd
        // TODO: should have these permissions granted by data/etc/platform.xml, but it does not.
        if (uid != android.graphics.GraphicsStatsService.AID_STATSD) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(sw);
            if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(mContext, android.graphics.GraphicsStatsService.TAG, pw)) {
                pw.flush();
                throw new android.os.RemoteException(sw.toString());
            }
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            pullGraphicsStatsImpl(lastFullDay, pulledData);
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private void pullGraphicsStatsImpl(boolean lastFullDay, long pulledData) {
        long targetDay;
        if (lastFullDay) {
            // Get stats from yesterday. Stats stay constant, because the day is over.
            targetDay = normalizeDate(java.lang.System.currentTimeMillis() - 86400000).getTimeInMillis();
        } else {
            // Get stats from today. Stats may change as more apps are run today.
            targetDay = normalizeDate(java.lang.System.currentTimeMillis()).getTimeInMillis();
        }
        // Find active buffers for targetDay.
        java.util.ArrayList<android.graphics.GraphicsStatsService.HistoricalBuffer> buffers;
        synchronized(mLock) {
            buffers = new java.util.ArrayList<>(mActive.size());
            for (int i = 0; i < mActive.size(); i++) {
                android.graphics.GraphicsStatsService.ActiveBuffer buffer = mActive.get(i);
                if (buffer.mInfo.mStartTime == targetDay) {
                    try {
                        buffers.add(new android.graphics.GraphicsStatsService.HistoricalBuffer(buffer));
                    } catch (java.io.IOException ex) {
                        // Ignore
                    }
                }
            }
        }
        // Dump active and historic buffers for targetDay in a serialized
        // GraphicsStatsServiceDumpProto proto.
        long dump = android.graphics.GraphicsStatsService.nCreateDump(-1, true);
        try {
            synchronized(mFileAccessLock) {
                java.util.HashSet<java.io.File> skipList = dumpActiveLocked(dump, buffers);
                buffers.clear();
                java.lang.String subPath = java.lang.String.format("%d", targetDay);
                java.io.File dateDir = new java.io.File(mGraphicsStatsDir, subPath);
                if (dateDir.exists()) {
                    for (java.io.File pkg : dateDir.listFiles()) {
                        for (java.io.File version : pkg.listFiles()) {
                            java.io.File data = new java.io.File(version, "total");
                            if (skipList.contains(data)) {
                                continue;
                            }
                            android.graphics.GraphicsStatsService.nAddToDump(dump, data.getAbsolutePath());
                        }
                    }
                }
            }
        } finally {
            android.graphics.GraphicsStatsService.nFinishDumpInMemory(dump, pulledData, lastFullDay);
        }
    }

    private android.os.ParcelFileDescriptor requestBufferForProcessLocked(android.view.IGraphicsStatsCallback token, int uid, int pid, java.lang.String packageName, long versionCode) throws android.os.RemoteException {
        android.graphics.GraphicsStatsService.ActiveBuffer buffer = fetchActiveBuffersLocked(token, uid, pid, packageName, versionCode);
        scheduleRotateLocked();
        return buffer.getPfd();
    }

    private java.util.Calendar normalizeDate(long timestamp) {
        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(timestamp);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar;
    }

    private java.io.File pathForApp(android.graphics.GraphicsStatsService.BufferInfo info) {
        java.lang.String subPath = java.lang.String.format("%d/%s/%d/total", normalizeDate(info.mStartTime).getTimeInMillis(), info.mPackageName, info.mVersionCode);
        return new java.io.File(mGraphicsStatsDir, subPath);
    }

    private void saveBuffer(android.graphics.GraphicsStatsService.HistoricalBuffer buffer) {
        if (android.os.Trace.isTagEnabled(Trace.TRACE_TAG_SYSTEM_SERVER)) {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_SYSTEM_SERVER, "saving graphicsstats for " + buffer.mInfo.mPackageName);
        }
        synchronized(mFileAccessLock) {
            java.io.File path = pathForApp(buffer.mInfo);
            java.io.File parent = path.getParentFile();
            parent.mkdirs();
            if (!parent.exists()) {
                android.util.Log.w(android.graphics.GraphicsStatsService.TAG, ("Unable to create path: '" + parent.getAbsolutePath()) + "'");
                return;
            }
            android.graphics.GraphicsStatsService.nSaveBuffer(path.getAbsolutePath(), buffer.mInfo.mPackageName, buffer.mInfo.mVersionCode, buffer.mInfo.mStartTime, buffer.mInfo.mEndTime, buffer.mData);
        }
        android.os.Trace.traceEnd(Trace.TRACE_TAG_SYSTEM_SERVER);
    }

    private void deleteRecursiveLocked(java.io.File file) {
        if (file.isDirectory()) {
            for (java.io.File child : file.listFiles()) {
                deleteRecursiveLocked(child);
            }
        }
        if (!file.delete()) {
            android.util.Log.w(android.graphics.GraphicsStatsService.TAG, ("Failed to delete '" + file.getAbsolutePath()) + "'!");
        }
    }

    private void deleteOldBuffers() {
        android.os.Trace.traceBegin(Trace.TRACE_TAG_SYSTEM_SERVER, "deleting old graphicsstats buffers");
        synchronized(mFileAccessLock) {
            java.io.File[] files = mGraphicsStatsDir.listFiles();
            if ((files == null) || (files.length <= 3)) {
                return;
            }
            long[] sortedDates = new long[files.length];
            for (int i = 0; i < files.length; i++) {
                try {
                    sortedDates[i] = java.lang.Long.parseLong(files[i].getName());
                } catch (java.lang.NumberFormatException ex) {
                    // Skip unrecognized folders
                }
            }
            if (sortedDates.length <= 3) {
                return;
            }
            java.util.Arrays.sort(sortedDates);
            for (int i = 0; i < (sortedDates.length - 3); i++) {
                deleteRecursiveLocked(new java.io.File(mGraphicsStatsDir, java.lang.Long.toString(sortedDates[i])));
            }
        }
        android.os.Trace.traceEnd(Trace.TRACE_TAG_SYSTEM_SERVER);
    }

    private void addToSaveQueue(android.graphics.GraphicsStatsService.ActiveBuffer buffer) {
        try {
            android.graphics.GraphicsStatsService.HistoricalBuffer data = new android.graphics.GraphicsStatsService.HistoricalBuffer(buffer);
            android.os.Message.obtain(mWriteOutHandler, android.graphics.GraphicsStatsService.SAVE_BUFFER, data).sendToTarget();
        } catch (java.io.IOException e) {
            android.util.Log.w(android.graphics.GraphicsStatsService.TAG, "Failed to copy graphicsstats from " + buffer.mInfo.mPackageName, e);
        }
        buffer.closeAllBuffers();
    }

    private void processDied(android.graphics.GraphicsStatsService.ActiveBuffer buffer) {
        synchronized(mLock) {
            mActive.remove(buffer);
        }
        addToSaveQueue(buffer);
    }

    private android.graphics.GraphicsStatsService.ActiveBuffer fetchActiveBuffersLocked(android.view.IGraphicsStatsCallback token, int uid, int pid, java.lang.String packageName, long versionCode) throws android.os.RemoteException {
        int size = mActive.size();
        long today = normalizeDate(java.lang.System.currentTimeMillis()).getTimeInMillis();
        for (int i = 0; i < size; i++) {
            android.graphics.GraphicsStatsService.ActiveBuffer buffer = mActive.get(i);
            if ((buffer.mPid == pid) && (buffer.mUid == uid)) {
                // If the buffer is too old we remove it and return a new one
                if (buffer.mInfo.mStartTime < today) {
                    buffer.binderDied();
                    break;
                } else {
                    return buffer;
                }
            }
        }
        // Didn't find one, need to create it
        try {
            android.graphics.GraphicsStatsService.ActiveBuffer buffers = new android.graphics.GraphicsStatsService.ActiveBuffer(token, uid, pid, packageName, versionCode);
            mActive.add(buffers);
            return buffers;
        } catch (java.io.IOException ex) {
            throw new android.os.RemoteException("Failed to allocate space");
        }
    }

    private java.util.HashSet<java.io.File> dumpActiveLocked(long dump, java.util.ArrayList<android.graphics.GraphicsStatsService.HistoricalBuffer> buffers) {
        java.util.HashSet<java.io.File> skipFiles = new java.util.HashSet<>(buffers.size());
        for (int i = 0; i < buffers.size(); i++) {
            android.graphics.GraphicsStatsService.HistoricalBuffer buffer = buffers.get(i);
            java.io.File path = pathForApp(buffer.mInfo);
            skipFiles.add(path);
            android.graphics.GraphicsStatsService.nAddToDump(dump, path.getAbsolutePath(), buffer.mInfo.mPackageName, buffer.mInfo.mVersionCode, buffer.mInfo.mStartTime, buffer.mInfo.mEndTime, buffer.mData);
        }
        return skipFiles;
    }

    private void dumpHistoricalLocked(long dump, java.util.HashSet<java.io.File> skipFiles) {
        for (java.io.File date : mGraphicsStatsDir.listFiles()) {
            for (java.io.File pkg : date.listFiles()) {
                for (java.io.File version : pkg.listFiles()) {
                    java.io.File data = new java.io.File(version, "total");
                    if (skipFiles.contains(data)) {
                        continue;
                    }
                    android.graphics.GraphicsStatsService.nAddToDump(dump, data.getAbsolutePath());
                }
            }
        }
    }

    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(mContext, android.graphics.GraphicsStatsService.TAG, fout))
            return;

        boolean dumpProto = false;
        for (java.lang.String str : args) {
            if ("--proto".equals(str)) {
                dumpProto = true;
                break;
            }
        }
        java.util.ArrayList<android.graphics.GraphicsStatsService.HistoricalBuffer> buffers;
        synchronized(mLock) {
            buffers = new java.util.ArrayList<>(mActive.size());
            for (int i = 0; i < mActive.size(); i++) {
                try {
                    buffers.add(new android.graphics.GraphicsStatsService.HistoricalBuffer(mActive.get(i)));
                } catch (java.io.IOException ex) {
                    // Ignore
                }
            }
        }
        long dump = android.graphics.GraphicsStatsService.nCreateDump(fd.getInt$(), dumpProto);
        try {
            synchronized(mFileAccessLock) {
                java.util.HashSet<java.io.File> skipList = dumpActiveLocked(dump, buffers);
                buffers.clear();
                dumpHistoricalLocked(dump, skipList);
            }
        } finally {
            android.graphics.GraphicsStatsService.nFinishDump(dump);
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        android.graphics.GraphicsStatsService.nativeDestructor();
    }

    private native void nativeInit();

    private static native void nativeDestructor();

    private static native int nGetAshmemSize();

    private static native long nCreateDump(int outFd, boolean isProto);

    private static native void nAddToDump(long dump, java.lang.String path, java.lang.String packageName, long versionCode, long startTime, long endTime, byte[] data);

    private static native void nAddToDump(long dump, java.lang.String path);

    private static native void nFinishDump(long dump);

    private static native void nFinishDumpInMemory(long dump, long pulledData, boolean lastFullDay);

    private static native void nSaveBuffer(java.lang.String path, java.lang.String packageName, long versionCode, long startTime, long endTime, byte[] data);

    private final class BufferInfo {
        final java.lang.String mPackageName;

        final long mVersionCode;

        long mStartTime;

        long mEndTime;

        BufferInfo(java.lang.String packageName, long versionCode, long startTime) {
            this.mPackageName = packageName;
            this.mVersionCode = versionCode;
            this.mStartTime = startTime;
        }
    }

    private final class ActiveBuffer implements android.graphics.DeathRecipient {
        final android.graphics.GraphicsStatsService.BufferInfo mInfo;

        final int mUid;

        final int mPid;

        final android.view.IGraphicsStatsCallback mCallback;

        final android.os.IBinder mToken;

        android.os.SharedMemory mProcessBuffer;

        java.nio.ByteBuffer mMapping;

        ActiveBuffer(android.view.IGraphicsStatsCallback token, int uid, int pid, java.lang.String packageName, long versionCode) throws android.os.RemoteException, java.io.IOException {
            mInfo = new android.graphics.GraphicsStatsService.BufferInfo(packageName, versionCode, java.lang.System.currentTimeMillis());
            mUid = uid;
            mPid = pid;
            mCallback = token;
            mToken = mCallback.asBinder();
            mToken.linkToDeath(this, 0);
            try {
                mProcessBuffer = android.os.SharedMemory.create("GFXStats-" + pid, mAshmemSize);
                mMapping = mProcessBuffer.mapReadWrite();
            } catch (android.system.ErrnoException ex) {
                ex.rethrowAsIOException();
            }
            mMapping.position(0);
            mMapping.put(mZeroData, 0, mAshmemSize);
        }

        @java.lang.Override
        public void binderDied() {
            mToken.unlinkToDeath(this, 0);
            processDied(this);
        }

        void closeAllBuffers() {
            if (mMapping != null) {
                android.os.SharedMemory.unmap(mMapping);
                mMapping = null;
            }
            if (mProcessBuffer != null) {
                mProcessBuffer.close();
                mProcessBuffer = null;
            }
        }

        android.os.ParcelFileDescriptor getPfd() {
            try {
                return mProcessBuffer.getFdDup();
            } catch (java.io.IOException ex) {
                throw new java.lang.IllegalStateException("Failed to get PFD from memory file", ex);
            }
        }

        void readBytes(byte[] buffer, int count) throws java.io.IOException {
            if (mMapping == null) {
                throw new java.io.IOException("SharedMemory has been deactivated");
            }
            mMapping.position(0);
            mMapping.get(buffer, 0, count);
        }
    }

    private final class HistoricalBuffer {
        final android.graphics.GraphicsStatsService.BufferInfo mInfo;

        final byte[] mData = new byte[mAshmemSize];

        HistoricalBuffer(android.graphics.GraphicsStatsService.ActiveBuffer active) throws java.io.IOException {
            mInfo = active.mInfo;
            mInfo.mEndTime = java.lang.System.currentTimeMillis();
            active.readBytes(mData, mAshmemSize);
        }
    }
}

