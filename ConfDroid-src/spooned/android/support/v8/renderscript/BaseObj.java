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
package android.support.v8.renderscript;


/**
 * BaseObj is the base class for all RenderScript objects owned by a RS context.
 * It is responsible for lifetime management and resource tracking. This class
 * should not be used by a user application.
 */
public class BaseObj {
    BaseObj(long id, android.support.v8.renderscript.RenderScript rs) {
        rs.validate();
        mRS = rs;
        mID = id;
        mDestroyed = false;
    }

    void setID(long id) {
        if (mID != 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Internal Error, reset of object ID.");
        }
        mID = id;
    }

    /**
     * Lookup the native object ID for this object.  Primarily used by the
     * generated reflected code.
     *
     * @param rs
     * 		Context to verify against internal context for
     * 		match.
     * @return long
     */
    long getID(android.support.v8.renderscript.RenderScript rs) {
        mRS.validate();
        if (mDestroyed) {
            throw new android.support.v8.renderscript.RSInvalidStateException("using a destroyed object.");
        }
        if (mID == 0) {
            throw new android.support.v8.renderscript.RSRuntimeException("Internal error: Object id 0.");
        }
        if ((rs != null) && (rs != mRS)) {
            throw new android.support.v8.renderscript.RSInvalidStateException("using object with mismatched context.");
        }
        return mID;
    }

    android.renderscript.BaseObj getNObj() {
        return null;
    }

    void checkValid() {
        if ((mID == 0) && (getNObj() == null)) {
            throw new android.support.v8.renderscript.RSIllegalArgumentException("Invalid object.");
        }
    }

    private long mID;

    private boolean mDestroyed;

    android.support.v8.renderscript.RenderScript mRS;

    private void helpDestroy() {
        boolean shouldDestroy = false;
        synchronized(this) {
            if (!mDestroyed) {
                shouldDestroy = true;
                mDestroyed = true;
            }
        }
        if (shouldDestroy) {
            // must include nObjDestroy in the critical section
            java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock rlock = mRS.mRWLock.readLock();
            rlock.lock();
            if (mRS.isAlive()) {
                mRS.nObjDestroy(mID);
            }
            rlock.unlock();
            mRS = null;
            mID = 0;
        }
    }

    protected void finalize() throws java.lang.Throwable {
        helpDestroy();
        super.finalize();
    }

    /**
     * Frees any native resources associated with this object.  The
     * primary use is to force immediate cleanup of resources when it is
     * believed the GC will not respond quickly enough.
     */
    public void destroy() {
        if (mDestroyed) {
            throw new android.support.v8.renderscript.RSInvalidStateException("Object already destroyed.");
        }
        helpDestroy();
    }

    /**
     * Calculates the hash code value for a BaseObj.
     *
     * @return int
     */
    @java.lang.Override
    public int hashCode() {
        return ((int) ((mID & 0xfffffff) ^ (mID >> 32)));
    }

    /**
     * Compare the current BaseObj with another BaseObj for equality.
     *
     * @param obj
     * 		The object to check equality with.
     * @return boolean
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        // Early-out check to see if both BaseObjs are actually the same
        if (this == obj)
            return true;

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.support.v8.renderscript.BaseObj b = ((android.support.v8.renderscript.BaseObj) (obj));
        return mID == b.mID;
    }
}

