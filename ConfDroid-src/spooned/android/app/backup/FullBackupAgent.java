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
 * Simple concrete class that merely provides the default BackupAgent full backup/restore
 * implementations for applications that do not supply their own.
 *
 * {@hide }
 */
public class FullBackupAgent extends android.app.backup.BackupAgent {
    @java.lang.Override
    public void onBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        // Doesn't do incremental backup/restore
    }

    @java.lang.Override
    public void onRestore(android.app.backup.BackupDataInput data, int appVersionCode, android.os.ParcelFileDescriptor newState) throws java.io.IOException {
        // Doesn't do incremental backup/restore
    }
}

