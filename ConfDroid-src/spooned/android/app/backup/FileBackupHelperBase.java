/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.app.backup;


/**
 * Base class for the {@link android.app.backup.FileBackupHelper} implementation.
 */
class FileBackupHelperBase {
    private static final java.lang.String TAG = "FileBackupHelperBase";

    long mPtr;

    android.content.Context mContext;

    boolean mExceptionLogged;

    FileBackupHelperBase(android.content.Context context) {
        mPtr = android.app.backup.FileBackupHelperBase.ctor();
        mContext = context;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            android.app.backup.FileBackupHelperBase.dtor(mPtr);
        } finally {
            super.finalize();
        }
    }

    /**
     * Check the parameters so the native code doesn't have to throw all the exceptions
     * since it's easier to do that from Java.
     */
    static void performBackup_checked(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState, java.lang.String[] files, java.lang.String[] keys) {
        if (files.length == 0) {
            return;
        }
        // files must be all absolute paths
        for (java.lang.String f : files) {
            if (f.charAt(0) != '/') {
                throw new java.lang.RuntimeException("files must have all absolute paths: " + f);
            }
        }
        // the length of files and keys must be the same
        if (files.length != keys.length) {
            throw new java.lang.RuntimeException((("files.length=" + files.length) + " keys.length=") + keys.length);
        }
        // oldStateFd can be null
        java.io.FileDescriptor oldStateFd = (oldState != null) ? oldState.getFileDescriptor() : null;
        java.io.FileDescriptor newStateFd = newState.getFileDescriptor();
        if (newStateFd == null) {
            throw new java.lang.NullPointerException();
        }
        int err = android.app.backup.FileBackupHelperBase.performBackup_native(oldStateFd, data.mBackupWriter, newStateFd, files, keys);
        if (err != 0) {
            // TODO: more here
            throw new java.lang.RuntimeException("Backup failed 0x" + java.lang.Integer.toHexString(err));
        }
    }

    boolean writeFile(java.io.File f, android.app.backup.BackupDataInputStream in) {
        int result = -1;
        // Create the enclosing directory.
        java.io.File parent = f.getParentFile();
        parent.mkdirs();
        result = android.app.backup.FileBackupHelperBase.writeFile_native(mPtr, f.getAbsolutePath(), in.mData.mBackupReader);
        if (result != 0) {
            // Bail on this entity.  Only log one failure per helper object.
            if (!mExceptionLogged) {
                android.util.Log.e(android.app.backup.FileBackupHelperBase.TAG, (((("Failed restoring file '" + f) + "' for app '") + mContext.getPackageName()) + "\' result=0x") + java.lang.Integer.toHexString(result));
                mExceptionLogged = true;
            }
        }
        return result == 0;
    }

    public void writeNewStateDescription(android.os.ParcelFileDescriptor fd) {
        int result = android.app.backup.FileBackupHelperBase.writeSnapshot_native(mPtr, fd.getFileDescriptor());
        // TODO: Do something with the error.
    }

    boolean isKeyInList(java.lang.String key, java.lang.String[] list) {
        for (java.lang.String s : list) {
            if (s.equals(key)) {
                return true;
            }
        }
        return false;
    }

    private static native long ctor();

    private static native void dtor(long ptr);

    private static native int performBackup_native(java.io.FileDescriptor oldState, long data, java.io.FileDescriptor newState, java.lang.String[] files, java.lang.String[] keys);

    private static native int writeFile_native(long ptr, java.lang.String filename, long backupReader);

    private static native int writeSnapshot_native(long ptr, java.io.FileDescriptor fd);
}

