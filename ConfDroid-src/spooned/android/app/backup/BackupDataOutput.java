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
 * Provides the structured interface through which a {@link BackupAgent} commits
 * information to the backup data set, via its {@link BackupAgent#onBackup(ParcelFileDescriptor,BackupDataOutput,ParcelFileDescriptor)
 * onBackup()} method.  Data written for backup is presented
 * as a set of "entities," key/value pairs in which each binary data record "value" is
 * named with a string "key."
 * <p>
 * To commit a data record to the backup transport, the agent's
 * {@link BackupAgent#onBackup(ParcelFileDescriptor,BackupDataOutput,ParcelFileDescriptor)
 * onBackup()} method first writes an "entity header" that supplies the key string for the record
 * and the total size of the binary value for the record.  After the header has been
 * written, the agent then writes the binary entity value itself.  The entity value can
 * be written in multiple chunks if desired, as long as the total count of bytes written
 * matches what was supplied to {@link #writeEntityHeader(String, int) writeEntityHeader()}.
 * <p>
 * Entity key strings are considered to be unique within a given application's backup
 * data set. If a backup agent writes a new entity under an existing key string, its value will
 * replace any previous value in the transport's remote data store.  You can remove a record
 * entirely from the remote data set by writing a new entity header using the
 * existing record's key, but supplying a negative <code>dataSize</code> parameter.
 * When you do so, the agent does not need to call {@link #writeEntityData(byte[], int)}.
 * <h3>Example</h3>
 * <p>
 * Here is an example illustrating a way to back up the value of a String variable
 * called <code>mStringToBackUp</code>:
 * <pre>
 * static final String MY_STRING_KEY = "storedstring";
 *
 * public void {@link BackupAgent#onBackup(ParcelFileDescriptor, BackupDataOutput, ParcelFileDescriptor) onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState)}
 *         throws IOException {
 *     ...
 *     byte[] stringBytes = mStringToBackUp.getBytes();
 *     data.writeEntityHeader(MY_STRING_KEY, stringBytes.length);
 *     data.writeEntityData(stringBytes, stringBytes.length);
 *     ...
 * }</pre>
 *
 * @see BackupAgent
 */
public class BackupDataOutput {
    long mBackupWriter;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public BackupDataOutput(java.io.FileDescriptor fd) {
        if (fd == null)
            throw new java.lang.NullPointerException();

        mBackupWriter = android.app.backup.BackupDataOutput.ctor(fd);
        if (mBackupWriter == 0) {
            throw new java.lang.RuntimeException("Native initialization failed with fd=" + fd);
        }
    }

    /**
     * Mark the beginning of one record in the backup data stream. This must be called before
     * {@link #writeEntityData}.
     *
     * @param key
     * 		A string key that uniquely identifies the data record within the application.
     * 		Keys whose first character is \uFF00 or higher are not valid.
     * @param dataSize
     * 		The size in bytes of this record's data.  Passing a dataSize
     * 		of -1 indicates that the record under this key should be deleted.
     * @return The number of bytes written to the backup stream
     * @throws IOException
     * 		if the write failed
     */
    public int writeEntityHeader(java.lang.String key, int dataSize) throws java.io.IOException {
        int result = android.app.backup.BackupDataOutput.writeEntityHeader_native(mBackupWriter, key, dataSize);
        if (result >= 0) {
            return result;
        } else {
            throw new java.io.IOException("result=0x" + java.lang.Integer.toHexString(result));
        }
    }

    /**
     * Write a chunk of data under the current entity to the backup transport.
     *
     * @param data
     * 		A raw data buffer to send
     * @param size
     * 		The number of bytes to be sent in this chunk
     * @return the number of bytes written
     * @throws IOException
     * 		if the write failed
     */
    public int writeEntityData(byte[] data, int size) throws java.io.IOException {
        int result = android.app.backup.BackupDataOutput.writeEntityData_native(mBackupWriter, data, size);
        if (result >= 0) {
            return result;
        } else {
            throw new java.io.IOException("result=0x" + java.lang.Integer.toHexString(result));
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setKeyPrefix(java.lang.String keyPrefix) {
        android.app.backup.BackupDataOutput.setKeyPrefix_native(mBackupWriter, keyPrefix);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            android.app.backup.BackupDataOutput.dtor(mBackupWriter);
        } finally {
            super.finalize();
        }
    }

    private static native long ctor(java.io.FileDescriptor fd);

    private static native void dtor(long mBackupWriter);

    private static native int writeEntityHeader_native(long mBackupWriter, java.lang.String key, int dataSize);

    private static native int writeEntityData_native(long mBackupWriter, byte[] data, int size);

    private static native void setKeyPrefix_native(long mBackupWriter, java.lang.String keyPrefix);
}

