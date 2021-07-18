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
 *
 *
 * @unknown 
 */
public class BackupHelperDispatcher {
    private static final java.lang.String TAG = "BackupHelperDispatcher";

    private static class Header {
        int chunkSize;// not including the header


        java.lang.String keyPrefix;
    }

    java.util.TreeMap<java.lang.String, android.app.backup.BackupHelper> mHelpers = new java.util.TreeMap<java.lang.String, android.app.backup.BackupHelper>();

    public BackupHelperDispatcher() {
    }

    public void addHelper(java.lang.String keyPrefix, android.app.backup.BackupHelper helper) {
        mHelpers.put(keyPrefix, helper);
    }

    public void performBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        // First, do the helpers that we've already done, since they're already in the state
        // file.
        int err;
        android.app.backup.BackupHelperDispatcher.Header header = new android.app.backup.BackupHelperDispatcher.Header();
        java.util.TreeMap<java.lang.String, android.app.backup.BackupHelper> helpers = ((java.util.TreeMap<java.lang.String, android.app.backup.BackupHelper>) (mHelpers.clone()));
        java.io.FileDescriptor oldStateFD = null;
        if (oldState != null) {
            oldStateFD = oldState.getFileDescriptor();
            while ((err = android.app.backup.BackupHelperDispatcher.readHeader_native(header, oldStateFD)) >= 0) {
                if (err == 0) {
                    android.app.backup.BackupHelper helper = helpers.get(header.keyPrefix);
                    android.util.Log.d(android.app.backup.BackupHelperDispatcher.TAG, (("handling existing helper '" + header.keyPrefix) + "' ") + helper);
                    if (helper != null) {
                        doOneBackup(oldState, data, newState, header, helper);
                        helpers.remove(header.keyPrefix);
                    } else {
                        android.app.backup.BackupHelperDispatcher.skipChunk_native(oldStateFD, header.chunkSize);
                    }
                }
            } 
        }
        // Then go through and do the rest that we haven't done.
        for (java.util.Map.Entry<java.lang.String, android.app.backup.BackupHelper> entry : helpers.entrySet()) {
            header.keyPrefix = entry.getKey();
            android.util.Log.d(android.app.backup.BackupHelperDispatcher.TAG, ("handling new helper '" + header.keyPrefix) + "'");
            android.app.backup.BackupHelper helper = entry.getValue();
            doOneBackup(oldState, data, newState, header, helper);
        }
    }

    private void doOneBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState, android.app.backup.BackupHelperDispatcher.Header header, android.app.backup.BackupHelper helper) throws java.io.IOException {
        int err;
        java.io.FileDescriptor newStateFD = newState.getFileDescriptor();
        // allocate space for the header in the file
        int pos = android.app.backup.BackupHelperDispatcher.allocateHeader_native(header, newStateFD);
        if (pos < 0) {
            throw new java.io.IOException(("allocateHeader_native failed (error " + pos) + ")");
        }
        data.setKeyPrefix(header.keyPrefix);
        // do the backup
        helper.performBackup(oldState, data, newState);
        // fill in the header (seeking back to pos).  The file pointer will be returned to
        // where it was at the end of performBackup.  Header.chunkSize will not be filled in.
        err = android.app.backup.BackupHelperDispatcher.writeHeader_native(header, newStateFD, pos);
        if (err != 0) {
            throw new java.io.IOException(("writeHeader_native failed (error " + err) + ")");
        }
    }

    public void performRestore(android.app.backup.BackupDataInput input, int appVersionCode, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        boolean alreadyComplained = false;
        android.app.backup.BackupDataInputStream stream = new android.app.backup.BackupDataInputStream(input);
        while (input.readNextHeader()) {
            java.lang.String rawKey = input.getKey();
            int pos = rawKey.indexOf(':');
            if (pos > 0) {
                java.lang.String prefix = rawKey.substring(0, pos);
                android.app.backup.BackupHelper helper = mHelpers.get(prefix);
                if (helper != null) {
                    stream.dataSize = input.getDataSize();
                    stream.key = rawKey.substring(pos + 1);
                    helper.restoreEntity(stream);
                } else {
                    if (!alreadyComplained) {
                        android.util.Log.w(android.app.backup.BackupHelperDispatcher.TAG, ("Couldn't find helper for: '" + rawKey) + "'");
                        alreadyComplained = true;
                    }
                }
            } else {
                if (!alreadyComplained) {
                    android.util.Log.w(android.app.backup.BackupHelperDispatcher.TAG, ("Entity with no prefix: '" + rawKey) + "'");
                    alreadyComplained = true;
                }
            }
            input.skipEntityData();// In case they didn't consume the data.

        } 
        // Write out the state files -- mHelpers is a TreeMap, so the order is well defined.
        for (android.app.backup.BackupHelper helper : mHelpers.values()) {
            helper.writeNewStateDescription(newState);
        }
    }

    private static native int readHeader_native(android.app.backup.BackupHelperDispatcher.Header h, java.io.FileDescriptor fd);

    private static native int skipChunk_native(java.io.FileDescriptor fd, int bytesToSkip);

    private static native int allocateHeader_native(android.app.backup.BackupHelperDispatcher.Header h, java.io.FileDescriptor fd);

    private static native int writeHeader_native(android.app.backup.BackupHelperDispatcher.Header h, java.io.FileDescriptor fd, int pos);
}

