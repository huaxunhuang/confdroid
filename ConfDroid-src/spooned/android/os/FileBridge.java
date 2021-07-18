/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.os;


/**
 * Simple bridge that allows file access across process boundaries without
 * returning the underlying {@link FileDescriptor}. This is useful when the
 * server side needs to strongly assert that a client side is completely
 * hands-off.
 *
 * @unknown 
 */
public class FileBridge extends java.lang.Thread {
    private static final java.lang.String TAG = "FileBridge";

    // TODO: consider extending to support bidirectional IO
    private static final int MSG_LENGTH = 8;

    /**
     * CMD_WRITE [len] [data]
     */
    private static final int CMD_WRITE = 1;

    /**
     * CMD_FSYNC
     */
    private static final int CMD_FSYNC = 2;

    /**
     * CMD_CLOSE
     */
    private static final int CMD_CLOSE = 3;

    private java.io.FileDescriptor mTarget;

    private final java.io.FileDescriptor mServer = new java.io.FileDescriptor();

    private final java.io.FileDescriptor mClient = new java.io.FileDescriptor();

    private volatile boolean mClosed;

    public FileBridge() {
        try {
            android.system.Os.socketpair(android.system.OsConstants.AF_UNIX, android.system.OsConstants.SOCK_STREAM, 0, mServer, mClient);
        } catch (android.system.ErrnoException e) {
            throw new java.lang.RuntimeException("Failed to create bridge");
        }
    }

    public boolean isClosed() {
        return mClosed;
    }

    public void forceClose() {
        libcore.io.IoUtils.closeQuietly(mTarget);
        libcore.io.IoUtils.closeQuietly(mServer);
        libcore.io.IoUtils.closeQuietly(mClient);
        mClosed = true;
    }

    public void setTargetFile(java.io.FileDescriptor target) {
        mTarget = target;
    }

    public java.io.FileDescriptor getClientSocket() {
        return mClient;
    }

    @java.lang.Override
    public void run() {
        final byte[] temp = new byte[8192];
        try {
            while (libcore.io.IoBridge.read(mServer, temp, 0, android.os.FileBridge.MSG_LENGTH) == android.os.FileBridge.MSG_LENGTH) {
                final int cmd = libcore.io.Memory.peekInt(temp, 0, java.nio.ByteOrder.BIG_ENDIAN);
                if (cmd == android.os.FileBridge.CMD_WRITE) {
                    // Shuttle data into local file
                    int len = libcore.io.Memory.peekInt(temp, 4, java.nio.ByteOrder.BIG_ENDIAN);
                    while (len > 0) {
                        int n = libcore.io.IoBridge.read(mServer, temp, 0, java.lang.Math.min(temp.length, len));
                        if (n == (-1)) {
                            throw new java.io.IOException(("Unexpected EOF; still expected " + len) + " bytes");
                        }
                        libcore.io.IoBridge.write(mTarget, temp, 0, n);
                        len -= n;
                    } 
                } else
                    if (cmd == android.os.FileBridge.CMD_FSYNC) {
                        // Sync and echo back to confirm
                        android.system.Os.fsync(mTarget);
                        libcore.io.IoBridge.write(mServer, temp, 0, android.os.FileBridge.MSG_LENGTH);
                    } else
                        if (cmd == android.os.FileBridge.CMD_CLOSE) {
                            // Close and echo back to confirm
                            android.system.Os.fsync(mTarget);
                            android.system.Os.close(mTarget);
                            mClosed = true;
                            libcore.io.IoBridge.write(mServer, temp, 0, android.os.FileBridge.MSG_LENGTH);
                            break;
                        }


            } 
        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Log.wtf(android.os.FileBridge.TAG, "Failed during bridge", e);
        } finally {
            forceClose();
        }
    }

    public static class FileBridgeOutputStream extends java.io.OutputStream {
        private final android.os.ParcelFileDescriptor mClientPfd;

        private final java.io.FileDescriptor mClient;

        private final byte[] mTemp = new byte[android.os.FileBridge.MSG_LENGTH];

        public FileBridgeOutputStream(android.os.ParcelFileDescriptor clientPfd) {
            mClientPfd = clientPfd;
            mClient = clientPfd.getFileDescriptor();
        }

        public FileBridgeOutputStream(java.io.FileDescriptor client) {
            mClientPfd = null;
            mClient = client;
        }

        @java.lang.Override
        public void close() throws java.io.IOException {
            try {
                writeCommandAndBlock(android.os.FileBridge.CMD_CLOSE, "close()");
            } finally {
                libcore.io.IoBridge.closeAndSignalBlockedThreads(mClient);
                libcore.io.IoUtils.closeQuietly(mClientPfd);
            }
        }

        public void fsync() throws java.io.IOException {
            writeCommandAndBlock(android.os.FileBridge.CMD_FSYNC, "fsync()");
        }

        private void writeCommandAndBlock(int cmd, java.lang.String cmdString) throws java.io.IOException {
            libcore.io.Memory.pokeInt(mTemp, 0, cmd, java.nio.ByteOrder.BIG_ENDIAN);
            libcore.io.IoBridge.write(mClient, mTemp, 0, android.os.FileBridge.MSG_LENGTH);
            // Wait for server to ack
            if (libcore.io.IoBridge.read(mClient, mTemp, 0, android.os.FileBridge.MSG_LENGTH) == android.os.FileBridge.MSG_LENGTH) {
                if (libcore.io.Memory.peekInt(mTemp, 0, java.nio.ByteOrder.BIG_ENDIAN) == cmd) {
                    return;
                }
            }
            throw new java.io.IOException(("Failed to execute " + cmdString) + " across bridge");
        }

        @java.lang.Override
        public void write(byte[] buffer, int byteOffset, int byteCount) throws java.io.IOException {
            java.util.Arrays.checkOffsetAndCount(buffer.length, byteOffset, byteCount);
            libcore.io.Memory.pokeInt(mTemp, 0, android.os.FileBridge.CMD_WRITE, java.nio.ByteOrder.BIG_ENDIAN);
            libcore.io.Memory.pokeInt(mTemp, 4, byteCount, java.nio.ByteOrder.BIG_ENDIAN);
            libcore.io.IoBridge.write(mClient, mTemp, 0, android.os.FileBridge.MSG_LENGTH);
            libcore.io.IoBridge.write(mClient, buffer, byteOffset, byteCount);
        }

        @java.lang.Override
        public void write(int oneByte) throws java.io.IOException {
            libcore.io.Streams.writeSingleByte(this, oneByte);
        }
    }
}

