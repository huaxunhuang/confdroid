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
package android.net.netlink;


/**
 * NetlinkSocket
 *
 * A small wrapper class to assist with AF_NETLINK socket operations.
 *
 * @unknown 
 */
public class NetlinkSocket implements java.io.Closeable {
    private static final java.lang.String TAG = "NetlinkSocket";

    private static final int SOCKET_RECV_BUFSIZE = 64 * 1024;

    private static final int DEFAULT_RECV_BUFSIZE = 8 * 1024;

    private final java.io.FileDescriptor mDescriptor;

    private android.system.NetlinkSocketAddress mAddr;

    private long mLastRecvTimeoutMs;

    private long mLastSendTimeoutMs;

    public NetlinkSocket(int nlProto) throws android.system.ErrnoException {
        mDescriptor = android.system.Os.socket(android.system.OsConstants.AF_NETLINK, android.system.OsConstants.SOCK_DGRAM, nlProto);
        Libcore.os.setsockoptInt(mDescriptor, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_RCVBUF, android.net.netlink.NetlinkSocket.SOCKET_RECV_BUFSIZE);
    }

    public android.system.NetlinkSocketAddress getLocalAddress() throws android.system.ErrnoException {
        return ((android.system.NetlinkSocketAddress) (android.system.Os.getsockname(mDescriptor)));
    }

    public void bind(android.system.NetlinkSocketAddress localAddr) throws android.system.ErrnoException, java.net.SocketException {
        android.system.Os.bind(mDescriptor, ((java.net.SocketAddress) (localAddr)));
    }

    public void connectTo(android.system.NetlinkSocketAddress peerAddr) throws android.system.ErrnoException, java.net.SocketException {
        android.system.Os.connect(mDescriptor, ((java.net.SocketAddress) (peerAddr)));
    }

    public void connectToKernel() throws android.system.ErrnoException, java.net.SocketException {
        connectTo(new android.system.NetlinkSocketAddress(0, 0));
    }

    /**
     * Wait indefinitely (or until underlying socket error) for a
     * netlink message of at most DEFAULT_RECV_BUFSIZE size.
     */
    public java.nio.ByteBuffer recvMessage() throws android.system.ErrnoException, java.io.InterruptedIOException {
        return recvMessage(android.net.netlink.NetlinkSocket.DEFAULT_RECV_BUFSIZE, 0);
    }

    /**
     * Wait up to |timeoutMs| (or until underlying socket error) for a
     * netlink message of at most DEFAULT_RECV_BUFSIZE size.
     */
    public java.nio.ByteBuffer recvMessage(long timeoutMs) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return recvMessage(android.net.netlink.NetlinkSocket.DEFAULT_RECV_BUFSIZE, timeoutMs);
    }

    private void checkTimeout(long timeoutMs) {
        if (timeoutMs < 0) {
            throw new java.lang.IllegalArgumentException("Negative timeouts not permitted");
        }
    }

    /**
     * Wait up to |timeoutMs| (or until underlying socket error) for a
     * netlink message of at most |bufsize| size.
     *
     * Multi-threaded calls with different timeouts will cause unexpected results.
     */
    public java.nio.ByteBuffer recvMessage(int bufsize, long timeoutMs) throws android.system.ErrnoException, java.io.InterruptedIOException, java.lang.IllegalArgumentException {
        checkTimeout(timeoutMs);
        synchronized(mDescriptor) {
            if (mLastRecvTimeoutMs != timeoutMs) {
                android.system.Os.setsockoptTimeval(mDescriptor, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_RCVTIMEO, android.system.StructTimeval.fromMillis(timeoutMs));
                mLastRecvTimeoutMs = timeoutMs;
            }
        }
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(bufsize);
        int length = android.system.Os.read(mDescriptor, byteBuffer);
        if (length == bufsize) {
            android.util.Log.w(android.net.netlink.NetlinkSocket.TAG, "maximum read");
        }
        byteBuffer.position(0);
        byteBuffer.limit(length);
        byteBuffer.order(java.nio.ByteOrder.nativeOrder());
        return byteBuffer;
    }

    /**
     * Send a message to a peer to which this socket has previously connected.
     *
     * This blocks until completion or an error occurs.
     */
    public boolean sendMessage(byte[] bytes, int offset, int count) throws android.system.ErrnoException, java.io.InterruptedIOException {
        return sendMessage(bytes, offset, count, 0);
    }

    /**
     * Send a message to a peer to which this socket has previously connected,
     * waiting at most |timeoutMs| milliseconds for the send to complete.
     *
     * Multi-threaded calls with different timeouts will cause unexpected results.
     */
    public boolean sendMessage(byte[] bytes, int offset, int count, long timeoutMs) throws android.system.ErrnoException, java.io.InterruptedIOException, java.lang.IllegalArgumentException {
        checkTimeout(timeoutMs);
        synchronized(mDescriptor) {
            if (mLastSendTimeoutMs != timeoutMs) {
                android.system.Os.setsockoptTimeval(mDescriptor, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_SNDTIMEO, android.system.StructTimeval.fromMillis(timeoutMs));
                mLastSendTimeoutMs = timeoutMs;
            }
        }
        return count == android.system.Os.write(mDescriptor, bytes, offset, count);
    }

    @java.lang.Override
    public void close() {
        libcore.io.IoUtils.closeQuietly(mDescriptor);
    }
}

