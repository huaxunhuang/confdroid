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
package android.renderscript;


/**
 * BaseObj is the base class for all RenderScript objects owned by a RS context.
 * It is responsible for lifetime management and resource tracking. This class
 * should not be used by a user application.
 */
public class BaseObj {
    BaseObj(long id, android.renderscript.RenderScript rs) {
        rs.validate();
        mRS = rs;
        mID = id;
        mDestroyed = false;
    }

    void setID(long id) {
        if (mID != 0) {
            throw new android.renderscript.RSRuntimeException("Internal Error, reset of object ID.");
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
    long getID(android.renderscript.RenderScript rs) {
        mRS.validate();
        if (mDestroyed) {
            throw new android.renderscript.RSInvalidStateException("using a destroyed object.");
        }
        if (mID == 0) {
            throw new android.renderscript.RSRuntimeException("Internal error: Object id 0.");
        }
        if ((rs != null) && (rs != mRS)) {
            throw new android.renderscript.RSInvalidStateException("using object with mismatched context.");
        }
        return mID;
    }

    void checkValid() {
        if (mID == 0) {
            throw new android.renderscript.RSIllegalArgumentException("Invalid object.");
        }
    }

    private long mID;

    final dalvik.system.CloseGuard guard = dalvik.system.CloseGuard.get();

    private boolean mDestroyed;

    private java.lang.String mName;

    android.renderscript.RenderScript mRS;

    /**
     * setName assigns a name to an object.  This object can later be looked up
     * by this name.
     *
     * @param name
     * 		The name to assign to the object.
     */
    public void setName(java.lang.String name) {
        if (name == null) {
            throw new android.renderscript.RSIllegalArgumentException("setName requires a string of non-zero length.");
        }
        if (name.length() < 1) {
            throw new android.renderscript.RSIllegalArgumentException("setName does not accept a zero length string.");
        }
        if (mName != null) {
            throw new android.renderscript.RSIllegalArgumentException("setName object already has a name.");
        }
        try {
            byte[] bytes = name.getBytes("UTF-8");
            mRS.nAssignName(mID, bytes);
            mName = name;
        } catch (java.io.UnsupportedEncodingException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    /**
     *
     *
     * @return name of the renderscript object
     */
    public java.lang.String getName() {
        return mName;
    }

    private void helpDestroy() {
        boolean shouldDestroy = false;
        synchronized(this) {
            if (!mDestroyed) {
                shouldDestroy = true;
                mDestroyed = true;
            }
        }
        if (shouldDestroy) {
            guard.close();
            // must include nObjDestroy in the critical section
            java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock rlock = mRS.mRWLock.readLock();
            rlock.lock();
            // AllocationAdapters are BaseObjs with an ID of 0 but should not be passed to nObjDestroy
            if (mRS.isAlive() && (mID != 0)) {
                mRS.nObjDestroy(mID);
            }
            rlock.unlock();
            mRS = null;
            mID = 0;
        }
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            if (guard != null) {
                guard.warnIfOpen();
            }
            helpDestroy();
        } finally {
            super.finalize();
        }
    }

    /**
     * Frees any native resources associated with this object.  The
     * primary use is to force immediate cleanup of resources when it is
     * believed the GC will not respond quickly enough.
     */
    public void destroy() {
        if (mDestroyed) {
            throw new android.renderscript.RSInvalidStateException("Object already destroyed.");
        }
        helpDestroy();
    }

    /**
     * If an object came from an a3d file, java fields need to be
     * created with objects from the native layer
     */
    void updateFromNative() {
        mRS.validate();
        mName = mRS.nGetName(getID(mRS));
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
        android.renderscript.BaseObj b = ((android.renderscript.BaseObj) (obj));
        return mID == b.mID;
    }
}

