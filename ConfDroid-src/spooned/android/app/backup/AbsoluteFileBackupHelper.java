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
 * Like FileBackupHelper, but takes absolute paths for the files instead of
 * subpaths of getFilesDir()
 *
 * @unknown 
 */
public class AbsoluteFileBackupHelper extends android.app.backup.FileBackupHelperBase implements android.app.backup.BackupHelper {
    private static final java.lang.String TAG = "AbsoluteFileBackupHelper";

    private static final boolean DEBUG = false;

    android.content.Context mContext;

    java.lang.String[] mFiles;

    /**
     * Construct a helper for backing up / restoring the files at the given absolute locations
     * within the file system.
     *
     * @param context
     * 		
     * @param files
     * 		
     */
    public AbsoluteFileBackupHelper(android.content.Context context, java.lang.String... files) {
        super(context);
        mContext = context;
        mFiles = files;
    }

    /**
     * Based on oldState, determine which of the files from the application's data directory
     * need to be backed up, write them to the data stream, and fill in newState with the
     * state as it exists now.
     */
    public void performBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) {
        // use the file paths as the keys, too
        android.app.backup.FileBackupHelperBase.performBackup_checked(oldState, data, newState, mFiles, mFiles);
    }

    /**
     * Restore one absolute file entity from the restore stream
     */
    public void restoreEntity(android.app.backup.BackupDataInputStream data) {
        if (android.app.backup.AbsoluteFileBackupHelper.DEBUG)
            android.util.Log.d(android.app.backup.AbsoluteFileBackupHelper.TAG, (("got entity '" + data.getKey()) + "' size=") + data.size());

        java.lang.String key = data.getKey();
        if (isKeyInList(key, mFiles)) {
            java.io.File f = new java.io.File(key);
            writeFile(f, data);
        }
    }
}

