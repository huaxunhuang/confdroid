/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Utility class for writing BackupHelpers whose underlying data is a
 * fixed set of byte-array blobs.  The helper manages diff detection
 * and compression on the wire.
 *
 * @unknown 
 */
public abstract class BlobBackupHelper implements android.app.backup.BackupHelper {
    private static final java.lang.String TAG = "BlobBackupHelper";

    private static final boolean DEBUG = false;

    private final int mCurrentBlobVersion;

    private final java.lang.String[] mKeys;

    public BlobBackupHelper(int currentBlobVersion, java.lang.String... keys) {
        mCurrentBlobVersion = currentBlobVersion;
        mKeys = keys;
    }

    // Client interface
    /**
     * Generate and return the byte array containing the backup payload describing
     * the current data state.  During a backup operation this method is called once
     * per key that was supplied to the helper's constructor.
     *
     * @return A byte array containing the data blob that the caller wishes to store,
    or {@code null} if the current state is empty or undefined.
     */
    protected abstract byte[] getBackupPayload(java.lang.String key);

    /**
     * Given a byte array that was restored from backup, do whatever is appropriate
     * to apply that described state in the live system.  This method is called once
     * per key/value payload that was delivered for restore.  Typically data is delivered
     * for restore in lexical order by key, <i>not</i> in the order in which the keys
     * were supplied in the constructor.
     *
     * @param payload
     * 		The byte array that was passed to {@link #getBackupPayload()}
     * 		on the ancestral device.
     */
    protected abstract void applyRestoredPayload(java.lang.String key, byte[] payload);

    // Internal implementation
    /* State on-disk format:
    [Int]    : overall blob version number
    [Int=N] : number of keys represented in the state blob
    N* :
        [String] key
        [Long]   blob checksum, calculated after compression
     */
    @java.lang.SuppressWarnings("resource")
    private android.util.ArrayMap<java.lang.String, java.lang.Long> readOldState(android.os.ParcelFileDescriptor oldStateFd) {
        final android.util.ArrayMap<java.lang.String, java.lang.Long> state = new android.util.ArrayMap<java.lang.String, java.lang.Long>();
        java.io.FileInputStream fis = new java.io.FileInputStream(oldStateFd.getFileDescriptor());
        java.io.DataInputStream in = new java.io.DataInputStream(fis);
        try {
            int version = in.readInt();
            if (version <= mCurrentBlobVersion) {
                final int numKeys = in.readInt();
                if (android.app.backup.BlobBackupHelper.DEBUG) {
                    android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, ("  " + numKeys) + " keys in state record");
                }
                for (int i = 0; i < numKeys; i++) {
                    java.lang.String key = in.readUTF();
                    long checksum = in.readLong();
                    if (android.app.backup.BlobBackupHelper.DEBUG) {
                        android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, (("  key '" + key) + "' checksum is ") + checksum);
                    }
                    state.put(key, checksum);
                }
            } else {
                android.util.Log.w(android.app.backup.BlobBackupHelper.TAG, "Prior state from unrecognized version " + version);
            }
        } catch (java.io.EOFException e) {
            // Empty file is expected on first backup,  so carry on. If the state
            // is truncated we just treat it the same way.
            if (android.app.backup.BlobBackupHelper.DEBUG) {
                android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, "Hit EOF reading prior state");
            }
            state.clear();
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.app.backup.BlobBackupHelper.TAG, "Error examining prior backup state " + e.getMessage());
            state.clear();
        }
        return state;
    }

    /**
     * New overall state record
     */
    private void writeBackupState(android.util.ArrayMap<java.lang.String, java.lang.Long> state, android.os.ParcelFileDescriptor stateFile) {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(stateFile.getFileDescriptor());
            // We explicitly don't close 'out' because we must not close the backing fd.
            // The FileOutputStream will not close it implicitly.
            @java.lang.SuppressWarnings("resource")
            java.io.DataOutputStream out = new java.io.DataOutputStream(fos);
            out.writeInt(mCurrentBlobVersion);
            final int N = (state != null) ? state.size() : 0;
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                final java.lang.String key = state.keyAt(i);
                final long checksum = state.valueAt(i).longValue();
                if (android.app.backup.BlobBackupHelper.DEBUG) {
                    android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, (("  writing key " + key) + " checksum = ") + checksum);
                }
                out.writeUTF(key);
                out.writeLong(checksum);
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(android.app.backup.BlobBackupHelper.TAG, "Unable to write updated state", e);
        }
    }

    // Also versions the deflated blob internally in case we need to revise it
    private byte[] deflate(byte[] data) {
        byte[] result = null;
        if (data != null) {
            try {
                java.io.ByteArrayOutputStream sink = new java.io.ByteArrayOutputStream();
                java.io.DataOutputStream headerOut = new java.io.DataOutputStream(sink);
                // write the header directly to the sink ahead of the deflated payload
                headerOut.writeInt(mCurrentBlobVersion);
                java.util.zip.DeflaterOutputStream out = new java.util.zip.DeflaterOutputStream(sink);
                out.write(data);
                out.close();// finishes and commits the compression run

                result = sink.toByteArray();
                if (android.app.backup.BlobBackupHelper.DEBUG) {
                    android.util.Log.v(android.app.backup.BlobBackupHelper.TAG, (("Deflated " + data.length) + " bytes to ") + result.length);
                }
            } catch (java.io.IOException e) {
                android.util.Log.w(android.app.backup.BlobBackupHelper.TAG, "Unable to process payload: " + e.getMessage());
            }
        }
        return result;
    }

    // Returns null if inflation failed
    private byte[] inflate(byte[] compressedData) {
        byte[] result = null;
        if (compressedData != null) {
            try {
                java.io.ByteArrayInputStream source = new java.io.ByteArrayInputStream(compressedData);
                java.io.DataInputStream headerIn = new java.io.DataInputStream(source);
                int version = headerIn.readInt();
                if (version > mCurrentBlobVersion) {
                    android.util.Log.w(android.app.backup.BlobBackupHelper.TAG, "Saved payload from unrecognized version " + version);
                    return null;
                }
                java.util.zip.InflaterInputStream in = new java.util.zip.InflaterInputStream(source);
                java.io.ByteArrayOutputStream inflated = new java.io.ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int nRead;
                while ((nRead = in.read(buffer)) > 0) {
                    inflated.write(buffer, 0, nRead);
                } 
                in.close();
                inflated.flush();
                result = inflated.toByteArray();
                if (android.app.backup.BlobBackupHelper.DEBUG) {
                    android.util.Log.v(android.app.backup.BlobBackupHelper.TAG, (("Inflated " + compressedData.length) + " bytes to ") + result.length);
                }
            } catch (java.io.IOException e) {
                // result is still null here
                android.util.Log.w(android.app.backup.BlobBackupHelper.TAG, "Unable to process restored payload: " + e.getMessage());
            }
        }
        return result;
    }

    private long checksum(byte[] buffer) {
        if (buffer != null) {
            try {
                java.util.zip.CRC32 crc = new java.util.zip.CRC32();
                java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(buffer);
                byte[] buf = new byte[4096];
                int nRead = 0;
                while ((nRead = bis.read(buf)) >= 0) {
                    crc.update(buf, 0, nRead);
                } 
                return crc.getValue();
            } catch (java.lang.Exception e) {
                // whoops; fall through with an explicitly bogus checksum
            }
        }
        return -1;
    }

    // BackupHelper interface
    @java.lang.Override
    public void performBackup(android.os.ParcelFileDescriptor oldStateFd, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newStateFd) {
        if (android.app.backup.BlobBackupHelper.DEBUG) {
            android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, "Performing backup for " + this.getClass().getName());
        }
        final android.util.ArrayMap<java.lang.String, java.lang.Long> oldState = readOldState(oldStateFd);
        final android.util.ArrayMap<java.lang.String, java.lang.Long> newState = new android.util.ArrayMap<java.lang.String, java.lang.Long>();
        try {
            for (java.lang.String key : mKeys) {
                final byte[] payload = deflate(getBackupPayload(key));
                final long checksum = checksum(payload);
                if (android.app.backup.BlobBackupHelper.DEBUG) {
                    android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, (("Key " + key) + " backup checksum is ") + checksum);
                }
                newState.put(key, checksum);
                java.lang.Long oldChecksum = oldState.get(key);
                if ((oldChecksum == null) || (checksum != oldChecksum.longValue())) {
                    if (android.app.backup.BlobBackupHelper.DEBUG) {
                        android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, ((((("Checksum has changed from " + oldChecksum) + " to ") + checksum) + " for key ") + key) + ", writing");
                    }
                    if (payload != null) {
                        data.writeEntityHeader(key, payload.length);
                        data.writeEntityData(payload, payload.length);
                    } else {
                        // state's changed but there's no current payload => delete
                        data.writeEntityHeader(key, -1);
                    }
                } else {
                    if (android.app.backup.BlobBackupHelper.DEBUG) {
                        android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, ("No change under key " + key) + " => not writing");
                    }
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.app.backup.BlobBackupHelper.TAG, "Unable to record notification state: " + e.getMessage());
            newState.clear();
        } finally {
            // Always rewrite the state even if nothing changed
            writeBackupState(newState, newStateFd);
        }
    }

    @java.lang.Override
    public void restoreEntity(android.app.backup.BackupDataInputStream data) {
        final java.lang.String key = data.getKey();
        try {
            // known key?
            int which;
            for (which = 0; which < mKeys.length; which++) {
                if (key.equals(mKeys[which])) {
                    break;
                }
            }
            if (which >= mKeys.length) {
                android.util.Log.e(android.app.backup.BlobBackupHelper.TAG, ("Unrecognized key " + key) + ", ignoring");
                return;
            }
            byte[] compressed = new byte[data.size()];
            data.read(compressed);
            byte[] payload = inflate(compressed);
            applyRestoredPayload(key, payload);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.app.backup.BlobBackupHelper.TAG, (("Exception restoring entity " + key) + " : ") + e.getMessage());
        }
    }

    @java.lang.Override
    public void writeNewStateDescription(android.os.ParcelFileDescriptor newState) {
        // Just ensure that we do a full backup the first time after a restore
        if (android.app.backup.BlobBackupHelper.DEBUG) {
            android.util.Log.i(android.app.backup.BlobBackupHelper.TAG, "Writing state description after restore");
        }
        writeBackupState(null, newState);
    }
}

