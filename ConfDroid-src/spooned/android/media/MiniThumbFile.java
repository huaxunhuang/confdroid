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
package android.media;


/**
 * This class handles the mini-thumb file. A mini-thumb file consists
 * of blocks, indexed by id. Each block has BYTES_PER_MINTHUMB bytes in the
 * following format:
 *
 * 1 byte status (0 = empty, 1 = mini-thumb available)
 * 8 bytes magic (a magic number to match what's in the database)
 * 4 bytes data length (LEN)
 * LEN bytes jpeg data
 * (the remaining bytes are unused)
 *
 * @unknown This file is shared between MediaStore and MediaProvider and should remained internal use
only.
 */
public class MiniThumbFile {
    private static final java.lang.String TAG = "MiniThumbFile";

    private static final int MINI_THUMB_DATA_FILE_VERSION = 3;

    public static final int BYTES_PER_MINTHUMB = 10000;

    private static final int HEADER_SIZE = (1 + 8) + 4;

    private android.net.Uri mUri;

    private java.io.RandomAccessFile mMiniThumbFile;

    private java.nio.channels.FileChannel mChannel;

    private java.nio.ByteBuffer mBuffer;

    private static final java.util.Hashtable<java.lang.String, android.media.MiniThumbFile> sThumbFiles = new java.util.Hashtable<java.lang.String, android.media.MiniThumbFile>();

    /**
     * We store different types of thumbnails in different files. To remain backward compatibility,
     * we should hashcode of content://media/external/images/media remains the same.
     */
    public static synchronized void reset() {
        for (android.media.MiniThumbFile file : android.media.MiniThumbFile.sThumbFiles.values()) {
            file.deactivate();
        }
        android.media.MiniThumbFile.sThumbFiles.clear();
    }

    public static synchronized android.media.MiniThumbFile instance(android.net.Uri uri) {
        java.lang.String type = uri.getPathSegments().get(1);
        android.media.MiniThumbFile file = android.media.MiniThumbFile.sThumbFiles.get(type);
        // Log.v(TAG, "get minithumbfile for type: "+type);
        if (file == null) {
            file = new android.media.MiniThumbFile(android.net.Uri.parse(("content://media/external/" + type) + "/media"));
            android.media.MiniThumbFile.sThumbFiles.put(type, file);
        }
        return file;
    }

    private java.lang.String randomAccessFilePath(int version) {
        java.lang.String directoryName = android.os.Environment.getExternalStorageDirectory().toString() + "/DCIM/.thumbnails";
        return (((directoryName + "/.thumbdata") + version) + "-") + mUri.hashCode();
    }

    private void removeOldFile() {
        java.lang.String oldPath = randomAccessFilePath(android.media.MiniThumbFile.MINI_THUMB_DATA_FILE_VERSION - 1);
        java.io.File oldFile = new java.io.File(oldPath);
        if (oldFile.exists()) {
            try {
                oldFile.delete();
            } catch (java.lang.SecurityException ex) {
                // ignore
            }
        }
    }

    private java.io.RandomAccessFile miniThumbDataFile() {
        if (mMiniThumbFile == null) {
            removeOldFile();
            java.lang.String path = randomAccessFilePath(android.media.MiniThumbFile.MINI_THUMB_DATA_FILE_VERSION);
            java.io.File directory = new java.io.File(path).getParentFile();
            if (!directory.isDirectory()) {
                if (!directory.mkdirs()) {
                    android.util.Log.e(android.media.MiniThumbFile.TAG, "Unable to create .thumbnails directory " + directory.toString());
                }
            }
            java.io.File f = new java.io.File(path);
            try {
                mMiniThumbFile = new java.io.RandomAccessFile(f, "rw");
            } catch (java.io.IOException ex) {
                // Open as read-only so we can at least read the existing
                // thumbnails.
                try {
                    mMiniThumbFile = new java.io.RandomAccessFile(f, "r");
                } catch (java.io.IOException ex2) {
                    // ignore exception
                }
            }
            if (mMiniThumbFile != null) {
                mChannel = mMiniThumbFile.getChannel();
            }
        }
        return mMiniThumbFile;
    }

    public MiniThumbFile(android.net.Uri uri) {
        mUri = uri;
        mBuffer = java.nio.ByteBuffer.allocateDirect(android.media.MiniThumbFile.BYTES_PER_MINTHUMB);
    }

    public synchronized void deactivate() {
        if (mMiniThumbFile != null) {
            try {
                mMiniThumbFile.close();
                mMiniThumbFile = null;
            } catch (java.io.IOException ex) {
                // ignore exception
            }
        }
    }

    // Get the magic number for the specified id in the mini-thumb file.
    // Returns 0 if the magic is not available.
    public synchronized long getMagic(long id) {
        // check the mini thumb file for the right data.  Right is
        // defined as having the right magic number at the offset
        // reserved for this "id".
        java.io.RandomAccessFile r = miniThumbDataFile();
        if (r != null) {
            long pos = id * android.media.MiniThumbFile.BYTES_PER_MINTHUMB;
            java.nio.channels.FileLock lock = null;
            try {
                mBuffer.clear();
                mBuffer.limit(1 + 8);
                lock = mChannel.lock(pos, 1 + 8, true);
                // check that we can read the following 9 bytes
                // (1 for the "status" and 8 for the long)
                if (mChannel.read(mBuffer, pos) == 9) {
                    mBuffer.position(0);
                    if (mBuffer.get() == 1) {
                        return mBuffer.getLong();
                    }
                }
            } catch (java.io.IOException ex) {
                android.util.Log.v(android.media.MiniThumbFile.TAG, "Got exception checking file magic: ", ex);
            } catch (java.lang.RuntimeException ex) {
                // Other NIO related exception like disk full, read only channel..etc
                android.util.Log.e(android.media.MiniThumbFile.TAG, (("Got exception when reading magic, id = " + id) + ", disk full or mount read-only? ") + ex.getClass());
            } finally {
                try {
                    if (lock != null)
                        lock.release();

                } catch (java.io.IOException ex) {
                    // ignore it.
                }
            }
        }
        return 0;
    }

    public synchronized void saveMiniThumbToFile(byte[] data, long id, long magic) throws java.io.IOException {
        java.io.RandomAccessFile r = miniThumbDataFile();
        if (r == null)
            return;

        long pos = id * android.media.MiniThumbFile.BYTES_PER_MINTHUMB;
        java.nio.channels.FileLock lock = null;
        try {
            if (data != null) {
                if (data.length > (android.media.MiniThumbFile.BYTES_PER_MINTHUMB - android.media.MiniThumbFile.HEADER_SIZE)) {
                    // not enough space to store it.
                    return;
                }
                mBuffer.clear();
                mBuffer.put(((byte) (1)));
                mBuffer.putLong(magic);
                mBuffer.putInt(data.length);
                mBuffer.put(data);
                mBuffer.flip();
                lock = mChannel.lock(pos, android.media.MiniThumbFile.BYTES_PER_MINTHUMB, false);
                mChannel.write(mBuffer, pos);
            }
        } catch (java.io.IOException ex) {
            android.util.Log.e(android.media.MiniThumbFile.TAG, ("couldn't save mini thumbnail data for " + id) + "; ", ex);
            throw ex;
        } catch (java.lang.RuntimeException ex) {
            // Other NIO related exception like disk full, read only channel..etc
            android.util.Log.e(android.media.MiniThumbFile.TAG, (("couldn't save mini thumbnail data for " + id) + "; disk full or mount read-only? ") + ex.getClass());
        } finally {
            try {
                if (lock != null)
                    lock.release();

            } catch (java.io.IOException ex) {
                // ignore it.
            }
        }
    }

    /**
     * Gallery app can use this method to retrieve mini-thumbnail. Full size
     * images share the same IDs with their corresponding thumbnails.
     *
     * @param id
     * 		the ID of the image (same of full size image).
     * @param data
     * 		the buffer to store mini-thumbnail.
     */
    public synchronized byte[] getMiniThumbFromFile(long id, byte[] data) {
        java.io.RandomAccessFile r = miniThumbDataFile();
        if (r == null)
            return null;

        long pos = id * android.media.MiniThumbFile.BYTES_PER_MINTHUMB;
        java.nio.channels.FileLock lock = null;
        try {
            mBuffer.clear();
            lock = mChannel.lock(pos, android.media.MiniThumbFile.BYTES_PER_MINTHUMB, true);
            int size = mChannel.read(mBuffer, pos);
            if (size > ((1 + 8) + 4)) {
                // flag, magic, length
                mBuffer.position(0);
                byte flag = mBuffer.get();
                long magic = mBuffer.getLong();
                int length = mBuffer.getInt();
                if (((((size >= (((1 + 8) + 4) + length)) && (length != 0)) && (magic != 0)) && (flag == 1)) && (data.length >= length)) {
                    mBuffer.get(data, 0, length);
                    return data;
                }
            }
        } catch (java.io.IOException ex) {
            android.util.Log.w(android.media.MiniThumbFile.TAG, (("got exception when reading thumbnail id=" + id) + ", exception: ") + ex);
        } catch (java.lang.RuntimeException ex) {
            // Other NIO related exception like disk full, read only channel..etc
            android.util.Log.e(android.media.MiniThumbFile.TAG, (("Got exception when reading thumbnail, id = " + id) + ", disk full or mount read-only? ") + ex.getClass());
        } finally {
            try {
                if (lock != null)
                    lock.release();

            } catch (java.io.IOException ex) {
                // ignore it.
            }
        }
        return null;
    }
}

