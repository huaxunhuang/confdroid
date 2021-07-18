/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * limitations under the License
 */
package android.app.backup;


/**
 * Information about current progress of full data backup
 * Used in {@link BackupObserver#onUpdate(String, BackupProgress)}
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class BackupProgress implements android.os.Parcelable {
    /**
     * Expected size of data in full backup.
     */
    public final long bytesExpected;

    /**
     * Amount of backup data that is already saved in backup.
     */
    public final long bytesTransferred;

    public BackupProgress(long _bytesExpected, long _bytesTransferred) {
        bytesExpected = _bytesExpected;
        bytesTransferred = _bytesTransferred;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(bytesExpected);
        out.writeLong(bytesTransferred);
    }

    public static final android.os.Parcelable.Creator<android.app.backup.BackupProgress> CREATOR = new android.os.Parcelable.Creator<android.app.backup.BackupProgress>() {
        public android.app.backup.BackupProgress createFromParcel(android.os.Parcel in) {
            return new android.app.backup.BackupProgress(in);
        }

        public android.app.backup.BackupProgress[] newArray(int size) {
            return new android.app.backup.BackupProgress[size];
        }
    };

    private BackupProgress(android.os.Parcel in) {
        bytesExpected = in.readLong();
        bytesTransferred = in.readLong();
    }
}

