/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.os;


/**
 * Advisory wakelock-like mechanism by which processes that should not be interrupted for
 * OTA/update purposes can so advise the OS.  This is particularly relevant for headless
 * or kiosk-like operation.
 *
 * @unknown 
 */
public class UpdateLock {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "UpdateLock";

    private static android.os.IUpdateLock sService;

    private static void checkService() {
        if (android.os.UpdateLock.sService == null) {
            android.os.UpdateLock.sService = IUpdateLock.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.UPDATE_LOCK_SERVICE));
        }
    }

    android.os.IBinder mToken;

    int mCount = 0;

    boolean mRefCounted = true;

    boolean mHeld = false;

    final java.lang.String mTag;

    /**
     * Broadcast Intent action sent when the global update lock state changes,
     * i.e. when the first locker acquires an update lock, or when the last
     * locker releases theirs.  The broadcast is sticky but is sent only to
     * registered receivers.
     */
    public static final java.lang.String UPDATE_LOCK_CHANGED = "android.os.UpdateLock.UPDATE_LOCK_CHANGED";

    /**
     * Boolean Intent extra on the UPDATE_LOCK_CHANGED sticky broadcast, indicating
     * whether now is an appropriate time to interrupt device activity with an
     * update operation.  True means that updates are okay right now; false indicates
     * that perhaps later would be a better time.
     */
    public static final java.lang.String NOW_IS_CONVENIENT = "nowisconvenient";

    /**
     * Long Intent extra on the UPDATE_LOCK_CHANGED sticky broadcast, marking the
     * wall-clock time [in UTC] at which the broadcast was sent.  Note that this is
     * in the System.currentTimeMillis() time base, which may be non-monotonic especially
     * around reboots.
     */
    public static final java.lang.String TIMESTAMP = "timestamp";

    /**
     * Construct an UpdateLock instance.
     *
     * @param tag
     * 		An arbitrary string used to identify this lock instance in dump output.
     */
    public UpdateLock(java.lang.String tag) {
        mTag = tag;
        mToken = new android.os.Binder();
    }

    /**
     * Change the refcount behavior of this update lock.
     */
    public void setReferenceCounted(boolean isRefCounted) {
        if (android.os.UpdateLock.DEBUG) {
            android.util.Log.v(android.os.UpdateLock.TAG, (("setting refcounted=" + isRefCounted) + " : ") + this);
        }
        mRefCounted = isRefCounted;
    }

    /**
     * Is this lock currently held?
     */
    public boolean isHeld() {
        synchronized(mToken) {
            return mHeld;
        }
    }

    /**
     * Acquire an update lock.
     */
    public void acquire() {
        if (android.os.UpdateLock.DEBUG) {
            android.util.Log.v(android.os.UpdateLock.TAG, "acquire() : " + this, new java.lang.RuntimeException("here"));
        }
        android.os.UpdateLock.checkService();
        synchronized(mToken) {
            acquireLocked();
        }
    }

    private void acquireLocked() {
        if ((!mRefCounted) || ((mCount++) == 0)) {
            if (android.os.UpdateLock.sService != null) {
                try {
                    android.os.UpdateLock.sService.acquireUpdateLock(mToken, mTag);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.os.UpdateLock.TAG, "Unable to contact service to acquire");
                }
            }
            mHeld = true;
        }
    }

    /**
     * Release this update lock.
     */
    public void release() {
        if (android.os.UpdateLock.DEBUG)
            android.util.Log.v(android.os.UpdateLock.TAG, "release() : " + this, new java.lang.RuntimeException("here"));

        android.os.UpdateLock.checkService();
        synchronized(mToken) {
            releaseLocked();
        }
    }

    private void releaseLocked() {
        if ((!mRefCounted) || ((--mCount) == 0)) {
            if (android.os.UpdateLock.sService != null) {
                try {
                    android.os.UpdateLock.sService.releaseUpdateLock(mToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.os.UpdateLock.TAG, "Unable to contact service to release");
                }
            }
            mHeld = false;
        }
        if (mCount < 0) {
            throw new java.lang.RuntimeException("UpdateLock under-locked");
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        synchronized(mToken) {
            // if mHeld is true, sService must be non-null
            if (mHeld) {
                android.util.Log.wtf(android.os.UpdateLock.TAG, "UpdateLock finalized while still held");
                try {
                    android.os.UpdateLock.sService.releaseUpdateLock(mToken);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.os.UpdateLock.TAG, "Unable to contact service to release");
                }
            }
        }
    }
}

