/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.os.storage;


/**
 * StorageManager is the interface to the systems storage service. The storage
 * manager handles storage-related items such as Opaque Binary Blobs (OBBs).
 * <p>
 * OBBs contain a filesystem that maybe be encrypted on disk and mounted
 * on-demand from an application. OBBs are a good way of providing large amounts
 * of binary assets without packaging them into APKs as they may be multiple
 * gigabytes in size. However, due to their size, they're most likely stored in
 * a shared storage pool accessible from all programs. The system does not
 * guarantee the security of the OBB file itself: if any program modifies the
 * OBB, there is no guarantee that a read from that OBB will produce the
 * expected output.
 * <p>
 * Get an instance of this class by calling
 * {@link android.content.Context#getSystemService(java.lang.String)} with an
 * argument of {@link android.content.Context#STORAGE_SERVICE}.
 */
public class StorageManager {
    private static final java.lang.String TAG = "StorageManager";

    /**
     * {@hide }
     */
    public static final java.lang.String PROP_PRIMARY_PHYSICAL = "ro.vold.primary_physical";

    /**
     * {@hide }
     */
    public static final java.lang.String PROP_HAS_ADOPTABLE = "vold.has_adoptable";

    /**
     * {@hide }
     */
    public static final java.lang.String PROP_FORCE_ADOPTABLE = "persist.fw.force_adoptable";

    /**
     * {@hide }
     */
    public static final java.lang.String PROP_EMULATE_FBE = "persist.sys.emulate_fbe";

    /**
     * {@hide }
     */
    public static final java.lang.String PROP_SDCARDFS = "persist.sys.sdcardfs";

    /**
     * {@hide }
     */
    public static final java.lang.String UUID_PRIVATE_INTERNAL = null;

    /**
     * {@hide }
     */
    public static final java.lang.String UUID_PRIMARY_PHYSICAL = "primary_physical";

    /**
     * Activity Action: Allows the user to manage their storage. This activity provides the ability
     * to free up space on the device by deleting data such as apps.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_STORAGE = "android.os.storage.action.MANAGE_STORAGE";

    /**
     * {@hide }
     */
    public static final int DEBUG_FORCE_ADOPTABLE = 1 << 0;

    /**
     * {@hide }
     */
    public static final int DEBUG_EMULATE_FBE = 1 << 1;

    /**
     * {@hide }
     */
    public static final int DEBUG_SDCARDFS_FORCE_ON = 1 << 2;

    /**
     * {@hide }
     */
    public static final int DEBUG_SDCARDFS_FORCE_OFF = 1 << 3;

    // NOTE: keep in sync with installd
    /**
     * {@hide }
     */
    public static final int FLAG_STORAGE_DE = 1 << 0;

    /**
     * {@hide }
     */
    public static final int FLAG_STORAGE_CE = 1 << 1;

    /**
     * {@hide }
     */
    public static final int FLAG_FOR_WRITE = 1 << 8;

    /**
     * {@hide }
     */
    public static final int FLAG_REAL_STATE = 1 << 9;

    /**
     * {@hide }
     */
    public static final int FLAG_INCLUDE_INVISIBLE = 1 << 10;

    private static volatile android.os.storage.IMountService sMountService = null;

    // TODO: the location of the primary storage block varies from device to device, so we need to
    // try the most likely candidates - a long-term solution would be a device-specific vold
    // function that returns the calculated size.
    private static final java.lang.String[] INTERNAL_STORAGE_SIZE_PATHS = new java.lang.String[]{ "/sys/block/mmcblk0/size", "/sys/block/sda/size" };

    private static final int INTERNAL_STORAGE_SECTOR_SIZE = 512;

    private final android.content.Context mContext;

    private final android.content.ContentResolver mResolver;

    private final android.os.storage.IMountService mMountService;

    private final android.os.Looper mLooper;

    private final java.util.concurrent.atomic.AtomicInteger mNextNonce = new java.util.concurrent.atomic.AtomicInteger(0);

    private final java.util.ArrayList<android.os.storage.StorageManager.StorageEventListenerDelegate> mDelegates = new java.util.ArrayList<>();

    private static class StorageEventListenerDelegate extends android.os.storage.IMountServiceListener.Stub implements android.os.Handler.Callback {
        private static final int MSG_STORAGE_STATE_CHANGED = 1;

        private static final int MSG_VOLUME_STATE_CHANGED = 2;

        private static final int MSG_VOLUME_RECORD_CHANGED = 3;

        private static final int MSG_VOLUME_FORGOTTEN = 4;

        private static final int MSG_DISK_SCANNED = 5;

        private static final int MSG_DISK_DESTROYED = 6;

        final android.os.storage.StorageEventListener mCallback;

        final android.os.Handler mHandler;

        public StorageEventListenerDelegate(android.os.storage.StorageEventListener callback, android.os.Looper looper) {
            mCallback = callback;
            mHandler = new android.os.Handler(looper, this);
        }

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            final com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
            switch (msg.what) {
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_STORAGE_STATE_CHANGED :
                    mCallback.onStorageStateChanged(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)), ((java.lang.String) (args.arg3)));
                    args.recycle();
                    return true;
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_STATE_CHANGED :
                    mCallback.onVolumeStateChanged(((android.os.storage.VolumeInfo) (args.arg1)), args.argi2, args.argi3);
                    args.recycle();
                    return true;
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_RECORD_CHANGED :
                    mCallback.onVolumeRecordChanged(((android.os.storage.VolumeRecord) (args.arg1)));
                    args.recycle();
                    return true;
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_FORGOTTEN :
                    mCallback.onVolumeForgotten(((java.lang.String) (args.arg1)));
                    args.recycle();
                    return true;
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_DISK_SCANNED :
                    mCallback.onDiskScanned(((android.os.storage.DiskInfo) (args.arg1)), args.argi2);
                    args.recycle();
                    return true;
                case android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_DISK_DESTROYED :
                    mCallback.onDiskDestroyed(((android.os.storage.DiskInfo) (args.arg1)));
                    args.recycle();
                    return true;
            }
            args.recycle();
            return false;
        }

        @java.lang.Override
        public void onUsbMassStorageConnectionChanged(boolean connected) throws android.os.RemoteException {
            // Ignored
        }

        @java.lang.Override
        public void onStorageStateChanged(java.lang.String path, java.lang.String oldState, java.lang.String newState) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = path;
            args.arg2 = oldState;
            args.arg3 = newState;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_STORAGE_STATE_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void onVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = vol;
            args.argi2 = oldState;
            args.argi3 = newState;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_STATE_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void onVolumeRecordChanged(android.os.storage.VolumeRecord rec) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = rec;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_RECORD_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void onVolumeForgotten(java.lang.String fsUuid) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = fsUuid;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_VOLUME_FORGOTTEN, args).sendToTarget();
        }

        @java.lang.Override
        public void onDiskScanned(android.os.storage.DiskInfo disk, int volumeCount) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = disk;
            args.argi2 = volumeCount;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_DISK_SCANNED, args).sendToTarget();
        }

        @java.lang.Override
        public void onDiskDestroyed(android.os.storage.DiskInfo disk) throws android.os.RemoteException {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = disk;
            mHandler.obtainMessage(android.os.storage.StorageManager.StorageEventListenerDelegate.MSG_DISK_DESTROYED, args).sendToTarget();
        }
    }

    /**
     * Binder listener for OBB action results.
     */
    private final android.os.storage.StorageManager.ObbActionListener mObbActionListener = new android.os.storage.StorageManager.ObbActionListener();

    private class ObbActionListener extends android.os.storage.IObbActionListener.Stub {
        @java.lang.SuppressWarnings("hiding")
        private android.util.SparseArray<android.os.storage.StorageManager.ObbListenerDelegate> mListeners = new android.util.SparseArray<android.os.storage.StorageManager.ObbListenerDelegate>();

        @java.lang.Override
        public void onObbResult(java.lang.String filename, int nonce, int status) {
            final android.os.storage.StorageManager.ObbListenerDelegate delegate;
            synchronized(mListeners) {
                delegate = mListeners.get(nonce);
                if (delegate != null) {
                    mListeners.remove(nonce);
                }
            }
            if (delegate != null) {
                delegate.sendObbStateChanged(filename, status);
            }
        }

        public int addListener(android.os.storage.OnObbStateChangeListener listener) {
            final android.os.storage.StorageManager.ObbListenerDelegate delegate = new android.os.storage.StorageManager.ObbListenerDelegate(listener);
            synchronized(mListeners) {
                mListeners.put(delegate.nonce, delegate);
            }
            return delegate.nonce;
        }
    }

    private int getNextNonce() {
        return mNextNonce.getAndIncrement();
    }

    /**
     * Private class containing sender and receiver code for StorageEvents.
     */
    private class ObbListenerDelegate {
        private final java.lang.ref.WeakReference<android.os.storage.OnObbStateChangeListener> mObbEventListenerRef;

        private final android.os.Handler mHandler;

        private final int nonce;

        ObbListenerDelegate(android.os.storage.OnObbStateChangeListener listener) {
            nonce = getNextNonce();
            mObbEventListenerRef = new java.lang.ref.WeakReference<android.os.storage.OnObbStateChangeListener>(listener);
            mHandler = new android.os.Handler(mLooper) {
                @java.lang.Override
                public void handleMessage(android.os.Message msg) {
                    final android.os.storage.OnObbStateChangeListener changeListener = getListener();
                    if (changeListener == null) {
                        return;
                    }
                    changeListener.onObbStateChange(((java.lang.String) (msg.obj)), msg.arg1);
                }
            };
        }

        android.os.storage.OnObbStateChangeListener getListener() {
            if (mObbEventListenerRef == null) {
                return null;
            }
            return mObbEventListenerRef.get();
        }

        void sendObbStateChanged(java.lang.String path, int state) {
            mHandler.obtainMessage(0, state, 0, path).sendToTarget();
        }
    }

    /**
     * {@hide }
     */
    @java.lang.Deprecated
    public static android.os.storage.StorageManager from(android.content.Context context) {
        return context.getSystemService(android.os.storage.StorageManager.class);
    }

    /**
     * Constructs a StorageManager object through which an application can
     * can communicate with the systems mount service.
     *
     * @param tgtLooper
     * 		The {@link android.os.Looper} which events will be received on.
     * 		
     * 		<p>Applications can get instance of this class by calling
     * 		{@link android.content.Context#getSystemService(java.lang.String)} with an argument
     * 		of {@link android.content.Context#STORAGE_SERVICE}.
     * @unknown 
     */
    public StorageManager(android.content.Context context, android.os.Looper looper) {
        mContext = context;
        mResolver = context.getContentResolver();
        mLooper = looper;
        mMountService = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
        if (mMountService == null) {
            throw new java.lang.IllegalStateException("Failed to find running mount service");
        }
    }

    /**
     * Registers a {@link android.os.storage.StorageEventListener StorageEventListener}.
     *
     * @param listener
     * 		A {@link android.os.storage.StorageEventListener StorageEventListener} object.
     * @unknown 
     */
    public void registerListener(android.os.storage.StorageEventListener listener) {
        synchronized(mDelegates) {
            final android.os.storage.StorageManager.StorageEventListenerDelegate delegate = new android.os.storage.StorageManager.StorageEventListenerDelegate(listener, mLooper);
            try {
                mMountService.registerListener(delegate);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
            mDelegates.add(delegate);
        }
    }

    /**
     * Unregisters a {@link android.os.storage.StorageEventListener StorageEventListener}.
     *
     * @param listener
     * 		A {@link android.os.storage.StorageEventListener StorageEventListener} object.
     * @unknown 
     */
    public void unregisterListener(android.os.storage.StorageEventListener listener) {
        synchronized(mDelegates) {
            for (java.util.Iterator<android.os.storage.StorageManager.StorageEventListenerDelegate> i = mDelegates.iterator(); i.hasNext();) {
                final android.os.storage.StorageManager.StorageEventListenerDelegate delegate = i.next();
                if (delegate.mCallback == listener) {
                    try {
                        mMountService.unregisterListener(delegate);
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                    i.remove();
                }
            }
        }
    }

    /**
     * Enables USB Mass Storage (UMS) on the device.
     *
     * @unknown 
     */
    @java.lang.Deprecated
    public void enableUsbMassStorage() {
    }

    /**
     * Disables USB Mass Storage (UMS) on the device.
     *
     * @unknown 
     */
    @java.lang.Deprecated
    public void disableUsbMassStorage() {
    }

    /**
     * Query if a USB Mass Storage (UMS) host is connected.
     *
     * @return true if UMS host is connected.
     * @unknown 
     */
    @java.lang.Deprecated
    public boolean isUsbMassStorageConnected() {
        return false;
    }

    /**
     * Query if a USB Mass Storage (UMS) is enabled on the device.
     *
     * @return true if UMS host is enabled.
     * @unknown 
     */
    @java.lang.Deprecated
    public boolean isUsbMassStorageEnabled() {
        return false;
    }

    /**
     * Mount an Opaque Binary Blob (OBB) file. If a <code>key</code> is
     * specified, it is supplied to the mounting process to be used in any
     * encryption used in the OBB.
     * <p>
     * The OBB will remain mounted for as long as the StorageManager reference
     * is held by the application. As soon as this reference is lost, the OBBs
     * in use will be unmounted. The {@link OnObbStateChangeListener} registered
     * with this call will receive the success or failure of this operation.
     * <p>
     * <em>Note:</em> you can only mount OBB files for which the OBB tag on the
     * file matches a package ID that is owned by the calling program's UID.
     * That is, shared UID applications can attempt to mount any other
     * application's OBB that shares its UID.
     *
     * @param rawPath
     * 		the path to the OBB file
     * @param key
     * 		secret used to encrypt the OBB; may be <code>null</code> if no
     * 		encryption was used on the OBB.
     * @param listener
     * 		will receive the success or failure of the operation
     * @return whether the mount call was successfully queued or not
     */
    public boolean mountObb(java.lang.String rawPath, java.lang.String key, android.os.storage.OnObbStateChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        com.android.internal.util.Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            final java.lang.String canonicalPath = new java.io.File(rawPath).getCanonicalPath();
            final int nonce = mObbActionListener.addListener(listener);
            mMountService.mountObb(rawPath, canonicalPath, key, mObbActionListener, nonce);
            return true;
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalArgumentException("Failed to resolve path: " + rawPath, e);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Unmount an Opaque Binary Blob (OBB) file asynchronously. If the
     * <code>force</code> flag is true, it will kill any application needed to
     * unmount the given OBB (even the calling application).
     * <p>
     * The {@link OnObbStateChangeListener} registered with this call will
     * receive the success or failure of this operation.
     * <p>
     * <em>Note:</em> you can only mount OBB files for which the OBB tag on the
     * file matches a package ID that is owned by the calling program's UID.
     * That is, shared UID applications can obtain access to any other
     * application's OBB that shares its UID.
     * <p>
     *
     * @param rawPath
     * 		path to the OBB file
     * @param force
     * 		whether to kill any programs using this in order to unmount
     * 		it
     * @param listener
     * 		will receive the success or failure of the operation
     * @return whether the unmount call was successfully queued or not
     */
    public boolean unmountObb(java.lang.String rawPath, boolean force, android.os.storage.OnObbStateChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        com.android.internal.util.Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            final int nonce = mObbActionListener.addListener(listener);
            mMountService.unmountObb(rawPath, force, mObbActionListener, nonce);
            return true;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Check whether an Opaque Binary Blob (OBB) is mounted or not.
     *
     * @param rawPath
     * 		path to OBB image
     * @return true if OBB is mounted; false if not mounted or on error
     */
    public boolean isObbMounted(java.lang.String rawPath) {
        com.android.internal.util.Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return mMountService.isObbMounted(rawPath);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Check the mounted path of an Opaque Binary Blob (OBB) file. This will
     * give you the path to where you can obtain access to the internals of the
     * OBB.
     *
     * @param rawPath
     * 		path to OBB image
     * @return absolute path to mounted OBB image data or <code>null</code> if
    not mounted or exception encountered trying to read status
     */
    public java.lang.String getMountedObbPath(java.lang.String rawPath) {
        com.android.internal.util.Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return mMountService.getMountedObbPath(rawPath);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public java.util.List<android.os.storage.DiskInfo> getDisks() {
        try {
            return java.util.Arrays.asList(mMountService.getDisks());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.DiskInfo findDiskById(java.lang.String id) {
        com.android.internal.util.Preconditions.checkNotNull(id);
        // TODO; go directly to service to make this faster
        for (android.os.storage.DiskInfo disk : getDisks()) {
            if (java.util.Objects.equals(disk.id, id)) {
                return disk;
            }
        }
        return null;
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo findVolumeById(java.lang.String id) {
        com.android.internal.util.Preconditions.checkNotNull(id);
        // TODO; go directly to service to make this faster
        for (android.os.storage.VolumeInfo vol : getVolumes()) {
            if (java.util.Objects.equals(vol.id, id)) {
                return vol;
            }
        }
        return null;
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo findVolumeByUuid(java.lang.String fsUuid) {
        com.android.internal.util.Preconditions.checkNotNull(fsUuid);
        // TODO; go directly to service to make this faster
        for (android.os.storage.VolumeInfo vol : getVolumes()) {
            if (java.util.Objects.equals(vol.fsUuid, fsUuid)) {
                return vol;
            }
        }
        return null;
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeRecord findRecordByUuid(java.lang.String fsUuid) {
        com.android.internal.util.Preconditions.checkNotNull(fsUuid);
        // TODO; go directly to service to make this faster
        for (android.os.storage.VolumeRecord rec : getVolumeRecords()) {
            if (java.util.Objects.equals(rec.fsUuid, fsUuid)) {
                return rec;
            }
        }
        return null;
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo findPrivateForEmulated(android.os.storage.VolumeInfo emulatedVol) {
        if (emulatedVol != null) {
            return findVolumeById(emulatedVol.getId().replace("emulated", "private"));
        } else {
            return null;
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo findEmulatedForPrivate(android.os.storage.VolumeInfo privateVol) {
        if (privateVol != null) {
            return findVolumeById(privateVol.getId().replace("private", "emulated"));
        } else {
            return null;
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo findVolumeByQualifiedUuid(java.lang.String volumeUuid) {
        if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, volumeUuid)) {
            return findVolumeById(android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL);
        } else
            if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIMARY_PHYSICAL, volumeUuid)) {
                return getPrimaryPhysicalVolume();
            } else {
                return findVolumeByUuid(volumeUuid);
            }

    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public java.util.List<android.os.storage.VolumeInfo> getVolumes() {
        try {
            return java.util.Arrays.asList(mMountService.getVolumes(0));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public java.util.List<android.os.storage.VolumeInfo> getWritablePrivateVolumes() {
        try {
            final java.util.ArrayList<android.os.storage.VolumeInfo> res = new java.util.ArrayList<>();
            for (android.os.storage.VolumeInfo vol : mMountService.getVolumes(0)) {
                if ((vol.getType() == android.os.storage.VolumeInfo.TYPE_PRIVATE) && vol.isMountedWritable()) {
                    res.add(vol);
                }
            }
            return res;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public java.util.List<android.os.storage.VolumeRecord> getVolumeRecords() {
        try {
            return java.util.Arrays.asList(mMountService.getVolumeRecords(0));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public java.lang.String getBestVolumeDescription(android.os.storage.VolumeInfo vol) {
        if (vol == null)
            return null;

        // Nickname always takes precedence when defined
        if (!android.text.TextUtils.isEmpty(vol.fsUuid)) {
            final android.os.storage.VolumeRecord rec = findRecordByUuid(vol.fsUuid);
            if ((rec != null) && (!android.text.TextUtils.isEmpty(rec.nickname))) {
                return rec.nickname;
            }
        }
        if (!android.text.TextUtils.isEmpty(vol.getDescription())) {
            return vol.getDescription();
        }
        if (vol.disk != null) {
            return vol.disk.getDescription();
        }
        return null;
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo getPrimaryPhysicalVolume() {
        final java.util.List<android.os.storage.VolumeInfo> vols = getVolumes();
        for (android.os.storage.VolumeInfo vol : vols) {
            if (vol.isPrimaryPhysical()) {
                return vol;
            }
        }
        return null;
    }

    /**
     * {@hide }
     */
    public void mount(java.lang.String volId) {
        try {
            mMountService.mount(volId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void unmount(java.lang.String volId) {
        try {
            mMountService.unmount(volId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void format(java.lang.String volId) {
        try {
            mMountService.format(volId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public long benchmark(java.lang.String volId) {
        try {
            return mMountService.benchmark(volId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void partitionPublic(java.lang.String diskId) {
        try {
            mMountService.partitionPublic(diskId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void partitionPrivate(java.lang.String diskId) {
        try {
            mMountService.partitionPrivate(diskId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void partitionMixed(java.lang.String diskId, int ratio) {
        try {
            mMountService.partitionMixed(diskId, ratio);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void wipeAdoptableDisks() {
        // We only wipe devices in "adoptable" locations, which are in a
        // long-term stable slot/location on the device, where apps have a
        // reasonable chance of storing sensitive data. (Apps need to go through
        // SAF to write to transient volumes.)
        final java.util.List<android.os.storage.DiskInfo> disks = getDisks();
        for (android.os.storage.DiskInfo disk : disks) {
            final java.lang.String diskId = disk.getId();
            if (disk.isAdoptable()) {
                android.util.Slog.d(android.os.storage.StorageManager.TAG, ("Found adoptable " + diskId) + "; wiping");
                try {
                    // TODO: switch to explicit wipe command when we have it,
                    // for now rely on the fact that vfat format does a wipe
                    mMountService.partitionPublic(diskId);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(android.os.storage.StorageManager.TAG, ("Failed to wipe " + diskId) + ", but soldiering onward", e);
                }
            } else {
                android.util.Slog.d(android.os.storage.StorageManager.TAG, "Ignorning non-adoptable disk " + disk.getId());
            }
        }
    }

    /**
     * {@hide }
     */
    public void setVolumeNickname(java.lang.String fsUuid, java.lang.String nickname) {
        try {
            mMountService.setVolumeNickname(fsUuid, nickname);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void setVolumeInited(java.lang.String fsUuid, boolean inited) {
        try {
            mMountService.setVolumeUserFlags(fsUuid, inited ? android.os.storage.VolumeRecord.USER_FLAG_INITED : 0, android.os.storage.VolumeRecord.USER_FLAG_INITED);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void setVolumeSnoozed(java.lang.String fsUuid, boolean snoozed) {
        try {
            mMountService.setVolumeUserFlags(fsUuid, snoozed ? android.os.storage.VolumeRecord.USER_FLAG_SNOOZED : 0, android.os.storage.VolumeRecord.USER_FLAG_SNOOZED);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void forgetVolume(java.lang.String fsUuid) {
        try {
            mMountService.forgetVolume(fsUuid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * This is not the API you're looking for.
     *
     * @see PackageManager#getPrimaryStorageCurrentVolume()
     * @unknown 
     */
    public java.lang.String getPrimaryStorageUuid() {
        try {
            return mMountService.getPrimaryStorageUuid();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * This is not the API you're looking for.
     *
     * @see PackageManager#movePrimaryStorage(VolumeInfo)
     * @unknown 
     */
    public void setPrimaryStorageUuid(java.lang.String volumeUuid, android.content.pm.IPackageMoveObserver callback) {
        try {
            mMountService.setPrimaryStorageUuid(volumeUuid, callback);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return the {@link StorageVolume} that contains the given file, or {@code null} if none.
     */
    @android.annotation.Nullable
    public android.os.storage.StorageVolume getStorageVolume(java.io.File file) {
        return android.os.storage.StorageManager.getStorageVolume(getVolumeList(), file);
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    public static android.os.storage.StorageVolume getStorageVolume(java.io.File file, int userId) {
        return android.os.storage.StorageManager.getStorageVolume(android.os.storage.StorageManager.getVolumeList(userId, 0), file);
    }

    /**
     * {@hide }
     */
    @android.annotation.Nullable
    private static android.os.storage.StorageVolume getStorageVolume(android.os.storage.StorageVolume[] volumes, java.io.File file) {
        if (file == null) {
            return null;
        }
        try {
            file = file.getCanonicalFile();
        } catch (java.io.IOException ignored) {
            android.util.Slog.d(android.os.storage.StorageManager.TAG, "Could not get canonical path for " + file);
            return null;
        }
        for (android.os.storage.StorageVolume volume : volumes) {
            java.io.File volumeFile = volume.getPathFile();
            try {
                volumeFile = volumeFile.getCanonicalFile();
            } catch (java.io.IOException ignored) {
                continue;
            }
            if (android.os.FileUtils.contains(volumeFile, file)) {
                return volume;
            }
        }
        return null;
    }

    /**
     * Gets the state of a volume via its mountpoint.
     *
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public java.lang.String getVolumeState(java.lang.String mountPoint) {
        final android.os.storage.StorageVolume vol = getStorageVolume(new java.io.File(mountPoint));
        if (vol != null) {
            return vol.getState();
        } else {
            return android.os.Environment.MEDIA_UNKNOWN;
        }
    }

    /**
     * Return the list of shared/external storage volumes available to the
     * current user. This includes both the primary shared storage device and
     * any attached external volumes including SD cards and USB drives.
     *
     * @see Environment#getExternalStorageDirectory()
     * @see StorageVolume#createAccessIntent(String)
     */
    @android.annotation.NonNull
    public java.util.List<android.os.storage.StorageVolume> getStorageVolumes() {
        final java.util.ArrayList<android.os.storage.StorageVolume> res = new java.util.ArrayList<>();
        java.util.Collections.addAll(res, android.os.storage.StorageManager.getVolumeList(android.os.UserHandle.myUserId(), android.os.storage.StorageManager.FLAG_REAL_STATE | android.os.storage.StorageManager.FLAG_INCLUDE_INVISIBLE));
        return res;
    }

    /**
     * Return the primary shared/external storage volume available to the
     * current user. This volume is the same storage device returned by
     * {@link Environment#getExternalStorageDirectory()} and
     * {@link Context#getExternalFilesDir(String)}.
     */
    @android.annotation.NonNull
    public android.os.storage.StorageVolume getPrimaryStorageVolume() {
        return android.os.storage.StorageManager.getVolumeList(android.os.UserHandle.myUserId(), android.os.storage.StorageManager.FLAG_REAL_STATE | android.os.storage.StorageManager.FLAG_INCLUDE_INVISIBLE)[0];
    }

    /**
     * {@hide }
     */
    public long getPrimaryStorageSize() {
        for (java.lang.String path : android.os.storage.StorageManager.INTERNAL_STORAGE_SIZE_PATHS) {
            final long numberBlocks = readLong(path);
            if (numberBlocks > 0) {
                return numberBlocks * android.os.storage.StorageManager.INTERNAL_STORAGE_SECTOR_SIZE;
            }
        }
        return 0;
    }

    private long readLong(java.lang.String path) {
        try (final java.io.FileInputStream fis = new java.io.FileInputStream(path);final java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(fis))) {
            return java.lang.Long.parseLong(reader.readLine());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(android.os.storage.StorageManager.TAG, "Could not read " + path, e);
            return 0;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.os.storage.StorageVolume[] getVolumeList() {
        return android.os.storage.StorageManager.getVolumeList(mContext.getUserId(), 0);
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public static android.os.storage.StorageVolume[] getVolumeList(int userId, int flags) {
        final android.os.storage.IMountService mountService = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
        try {
            java.lang.String packageName = android.app.ActivityThread.currentOpPackageName();
            if (packageName == null) {
                // Package name can be null if the activity thread is running but the app
                // hasn't bound yet. In this case we fall back to the first package in the
                // current UID. This works for runtime permissions as permission state is
                // per UID and permission realted app ops are updated for all UID packages.
                java.lang.String[] packageNames = android.app.ActivityThread.getPackageManager().getPackagesForUid(android.os.Process.myUid());
                if ((packageNames == null) || (packageNames.length <= 0)) {
                    return new android.os.storage.StorageVolume[0];
                }
                packageName = packageNames[0];
            }
            final int uid = android.app.ActivityThread.getPackageManager().getPackageUid(packageName, android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING, userId);
            if (uid <= 0) {
                return new android.os.storage.StorageVolume[0];
            }
            return mountService.getVolumeList(uid, packageName, flags);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns list of paths for all mountable volumes.
     *
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public java.lang.String[] getVolumePaths() {
        android.os.storage.StorageVolume[] volumes = getVolumeList();
        int count = volumes.length;
        java.lang.String[] paths = new java.lang.String[count];
        for (int i = 0; i < count; i++) {
            paths[i] = volumes[i].getPath();
        }
        return paths;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.os.storage.StorageVolume getPrimaryVolume() {
        return android.os.storage.StorageManager.getPrimaryVolume(getVolumeList());
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public static android.os.storage.StorageVolume getPrimaryVolume(android.os.storage.StorageVolume[] volumes) {
        for (android.os.storage.StorageVolume volume : volumes) {
            if (volume.isPrimary()) {
                return volume;
            }
        }
        throw new java.lang.IllegalStateException("Missing primary storage");
    }

    /**
     * {@hide }
     */
    private static final int DEFAULT_THRESHOLD_PERCENTAGE = 10;

    private static final long DEFAULT_THRESHOLD_MAX_BYTES = 500 * android.net.TrafficStats.MB_IN_BYTES;

    private static final long DEFAULT_FULL_THRESHOLD_BYTES = android.net.TrafficStats.MB_IN_BYTES;

    /**
     * Return the number of available bytes until the given path is considered
     * running low on storage.
     *
     * @unknown 
     */
    public long getStorageBytesUntilLow(java.io.File path) {
        return path.getUsableSpace() - getStorageFullBytes(path);
    }

    /**
     * Return the number of available bytes at which the given path is
     * considered running low on storage.
     *
     * @unknown 
     */
    public long getStorageLowBytes(java.io.File path) {
        final long lowPercent = android.provider.Settings.Global.getInt(mResolver, android.provider.Settings.Global.SYS_STORAGE_THRESHOLD_PERCENTAGE, android.os.storage.StorageManager.DEFAULT_THRESHOLD_PERCENTAGE);
        final long lowBytes = (path.getTotalSpace() * lowPercent) / 100;
        final long maxLowBytes = android.provider.Settings.Global.getLong(mResolver, android.provider.Settings.Global.SYS_STORAGE_THRESHOLD_MAX_BYTES, android.os.storage.StorageManager.DEFAULT_THRESHOLD_MAX_BYTES);
        return java.lang.Math.min(lowBytes, maxLowBytes);
    }

    /**
     * Return the number of available bytes at which the given path is
     * considered full.
     *
     * @unknown 
     */
    public long getStorageFullBytes(java.io.File path) {
        return android.provider.Settings.Global.getLong(mResolver, android.provider.Settings.Global.SYS_STORAGE_FULL_THRESHOLD_BYTES, android.os.storage.StorageManager.DEFAULT_FULL_THRESHOLD_BYTES);
    }

    /**
     * {@hide }
     */
    public void createUserKey(int userId, int serialNumber, boolean ephemeral) {
        try {
            mMountService.createUserKey(userId, serialNumber, ephemeral);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void destroyUserKey(int userId) {
        try {
            mMountService.destroyUserKey(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) {
        try {
            mMountService.unlockUserKey(userId, serialNumber, token, secret);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void lockUserKey(int userId) {
        try {
            mMountService.lockUserKey(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void prepareUserStorage(java.lang.String volumeUuid, int userId, int serialNumber, int flags) {
        try {
            mMountService.prepareUserStorage(volumeUuid, userId, serialNumber, flags);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public void destroyUserStorage(java.lang.String volumeUuid, int userId, int flags) {
        try {
            mMountService.destroyUserStorage(volumeUuid, userId, flags);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public static boolean isUserKeyUnlocked(int userId) {
        if (android.os.storage.StorageManager.sMountService == null) {
            android.os.storage.StorageManager.sMountService = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
        }
        if (android.os.storage.StorageManager.sMountService == null) {
            android.util.Slog.w(android.os.storage.StorageManager.TAG, "Early during boot, assuming locked");
            return false;
        }
        final long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.os.storage.StorageManager.sMountService.isUserKeyUnlocked(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /**
     * Return if data stored at or under the given path will be encrypted while
     * at rest. This can help apps avoid the overhead of double-encrypting data.
     */
    public boolean isEncrypted(java.io.File file) {
        if (android.os.FileUtils.contains(android.os.Environment.getDataDirectory(), file)) {
            return android.os.storage.StorageManager.isEncrypted();
        } else
            if (android.os.FileUtils.contains(android.os.Environment.getExpandDirectory(), file)) {
                return true;
            }

        // TODO: extend to support shared storage
        return false;
    }

    /**
     * {@hide }
     * Is this device encryptable or already encrypted?
     *
     * @return true for encryptable or encrypted
    false not encrypted and not encryptable
     */
    public static boolean isEncryptable() {
        final java.lang.String state = android.os.SystemProperties.get("ro.crypto.state", "unsupported");
        return !"unsupported".equalsIgnoreCase(state);
    }

    /**
     * {@hide }
     * Is this device already encrypted?
     *
     * @return true for encrypted. (Implies isEncryptable() == true)
    false not encrypted
     */
    public static boolean isEncrypted() {
        final java.lang.String state = android.os.SystemProperties.get("ro.crypto.state", "");
        return "encrypted".equalsIgnoreCase(state);
    }

    /**
     * {@hide }
     * Is this device file encrypted?
     *
     * @return true for file encrypted. (Implies isEncrypted() == true)
    false not encrypted or block encrypted
     */
    public static boolean isFileEncryptedNativeOnly() {
        if (!android.os.storage.StorageManager.isEncrypted()) {
            return false;
        }
        final java.lang.String status = android.os.SystemProperties.get("ro.crypto.type", "");
        return "file".equalsIgnoreCase(status);
    }

    /**
     * {@hide }
     * Is this device block encrypted?
     *
     * @return true for block encrypted. (Implies isEncrypted() == true)
    false not encrypted or file encrypted
     */
    public static boolean isBlockEncrypted() {
        if (!android.os.storage.StorageManager.isEncrypted()) {
            return false;
        }
        final java.lang.String status = android.os.SystemProperties.get("ro.crypto.type", "");
        return "block".equalsIgnoreCase(status);
    }

    /**
     * {@hide }
     * Is this device block encrypted with credentials?
     *
     * @return true for crediential block encrypted.
    (Implies isBlockEncrypted() == true)
    false not encrypted, file encrypted or default block encrypted
     */
    public static boolean isNonDefaultBlockEncrypted() {
        if (!android.os.storage.StorageManager.isBlockEncrypted()) {
            return false;
        }
        try {
            android.os.storage.IMountService mountService = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
            return mountService.getPasswordType() != android.os.storage.StorageManager.CRYPT_TYPE_DEFAULT;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.storage.StorageManager.TAG, "Error getting encryption type");
            return false;
        }
    }

    /**
     * {@hide }
     * Is this device in the process of being block encrypted?
     *
     * @return true for encrypting.
    false otherwise
    Whether device isEncrypted at this point is undefined
    Note that only system services and CryptKeeper will ever see this return
    true - no app will ever be launched in this state.
    Also note that this state will not change without a teardown of the
    framework, so no service needs to check for changes during their lifespan
     */
    public static boolean isBlockEncrypting() {
        final java.lang.String state = android.os.SystemProperties.get("vold.encrypt_progress", "");
        return !"".equalsIgnoreCase(state);
    }

    /**
     * {@hide }
     * Is this device non default block encrypted and in the process of
     * prompting for credentials?
     *
     * @return true for prompting for credentials.
    (Implies isNonDefaultBlockEncrypted() == true)
    false otherwise
    Note that only system services and CryptKeeper will ever see this return
    true - no app will ever be launched in this state.
    Also note that this state will not change without a teardown of the
    framework, so no service needs to check for changes during their lifespan
     */
    public static boolean inCryptKeeperBounce() {
        final java.lang.String status = android.os.SystemProperties.get("vold.decrypt");
        return "trigger_restart_min_framework".equals(status);
    }

    /**
     * {@hide }
     */
    public static boolean isFileEncryptedEmulatedOnly() {
        return android.os.SystemProperties.getBoolean(android.os.storage.StorageManager.PROP_EMULATE_FBE, false);
    }

    /**
     * {@hide }
     * Is this device running in a file encrypted mode, either native or emulated?
     *
     * @return true for file encrypted, false otherwise
     */
    public static boolean isFileEncryptedNativeOrEmulated() {
        return android.os.storage.StorageManager.isFileEncryptedNativeOnly() || android.os.storage.StorageManager.isFileEncryptedEmulatedOnly();
    }

    /**
     * {@hide }
     */
    public static java.io.File maybeTranslateEmulatedPathToInternal(java.io.File path) {
        final android.os.storage.IMountService mountService = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
        try {
            final android.os.storage.VolumeInfo[] vols = mountService.getVolumes(0);
            for (android.os.storage.VolumeInfo vol : vols) {
                if (((vol.getType() == android.os.storage.VolumeInfo.TYPE_EMULATED) || (vol.getType() == android.os.storage.VolumeInfo.TYPE_PUBLIC)) && vol.isMountedReadable()) {
                    final java.io.File internalPath = android.os.FileUtils.rewriteAfterRename(vol.getPath(), vol.getInternalPath(), path);
                    if ((internalPath != null) && internalPath.exists()) {
                        return internalPath;
                    }
                }
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        return path;
    }

    /**
     * {@hide }
     */
    public android.os.ParcelFileDescriptor mountAppFuse(java.lang.String name) {
        try {
            return mMountService.mountAppFuse(name);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    // / Consts to match the password types in cryptfs.h
    /**
     *
     *
     * @unknown 
     */
    public static final int CRYPT_TYPE_PASSWORD = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int CRYPT_TYPE_DEFAULT = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int CRYPT_TYPE_PATTERN = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int CRYPT_TYPE_PIN = 3;

    // Constants for the data available via MountService.getField.
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String SYSTEM_LOCALE_KEY = "SystemLocale";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String OWNER_INFO_KEY = "OwnerInfo";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PATTERN_VISIBLE_KEY = "PatternVisible";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String PASSWORD_VISIBLE_KEY = "PasswordVisible";
}

