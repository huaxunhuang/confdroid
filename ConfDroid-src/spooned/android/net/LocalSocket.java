/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.net;


/**
 * Creates a (non-server) socket in the UNIX-domain namespace. The interface
 * here is not entirely unlike that of java.net.Socket. This class and the streams
 * returned from it may be used from multiple threads.
 */
public class LocalSocket implements java.io.Closeable {
    private final android.net.LocalSocketImpl impl;

    private volatile boolean implCreated;

    private android.net.LocalSocketAddress localAddress;

    private boolean isBound;

    private boolean isConnected;

    private final int sockType;

    /**
     * unknown socket type (used for constructor with existing file descriptor)
     */
    /* package */
    static final int SOCKET_UNKNOWN = 0;

    /**
     * Datagram socket type
     */
    public static final int SOCKET_DGRAM = 1;

    /**
     * Stream socket type
     */
    public static final int SOCKET_STREAM = 2;

    /**
     * Sequential packet socket type
     */
    public static final int SOCKET_SEQPACKET = 3;

    /**
     * Creates a AF_LOCAL/UNIX domain stream socket.
     */
    public LocalSocket() {
        this(android.net.LocalSocket.SOCKET_STREAM);
    }

    /**
     * Creates a AF_LOCAL/UNIX domain stream socket with given socket type
     *
     * @param sockType
     * 		either {@link #SOCKET_DGRAM}, {@link #SOCKET_STREAM}
     * 		or {@link #SOCKET_SEQPACKET}
     */
    public LocalSocket(int sockType) {
        this(new android.net.LocalSocketImpl(), sockType);
        isBound = false;
        isConnected = false;
    }

    /**
     * Creates a AF_LOCAL/UNIX domain stream socket with FileDescriptor.
     *
     * @unknown 
     */
    public LocalSocket(java.io.FileDescriptor fd) throws java.io.IOException {
        this(new android.net.LocalSocketImpl(fd), android.net.LocalSocket.SOCKET_UNKNOWN);
        isBound = true;
        isConnected = true;
    }

    /**
     * for use with AndroidServerSocket
     *
     * @param impl
     * 		a SocketImpl
     */
    /* package */
    LocalSocket(android.net.LocalSocketImpl impl, int sockType) {
        this.impl = impl;
        this.sockType = sockType;
        this.isConnected = false;
        this.isBound = false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public java.lang.String toString() {
        return (super.toString() + " impl:") + impl;
    }

    /**
     * It's difficult to discern from the spec when impl.create() should be
     * called, but it seems like a reasonable rule is "as soon as possible,
     * but not in a context where IOException cannot be thrown"
     *
     * @throws IOException
     * 		from SocketImpl.create()
     */
    private void implCreateIfNeeded() throws java.io.IOException {
        if (!implCreated) {
            synchronized(this) {
                if (!implCreated) {
                    try {
                        impl.create(sockType);
                    } finally {
                        implCreated = true;
                    }
                }
            }
        }
    }

    /**
     * Connects this socket to an endpoint. May only be called on an instance
     * that has not yet been connected.
     *
     * @param endpoint
     * 		endpoint address
     * @throws IOException
     * 		if socket is in invalid state or the address does
     * 		not exist.
     */
    public void connect(android.net.LocalSocketAddress endpoint) throws java.io.IOException {
        synchronized(this) {
            if (isConnected) {
                throw new java.io.IOException("already connected");
            }
            implCreateIfNeeded();
            impl.connect(endpoint, 0);
            isConnected = true;
            isBound = true;
        }
    }

    /**
     * Binds this socket to an endpoint name. May only be called on an instance
     * that has not yet been bound.
     *
     * @param bindpoint
     * 		endpoint address
     * @throws IOException
     * 		
     */
    public void bind(android.net.LocalSocketAddress bindpoint) throws java.io.IOException {
        implCreateIfNeeded();
        synchronized(this) {
            if (isBound) {
                throw new java.io.IOException("already bound");
            }
            localAddress = bindpoint;
            impl.bind(localAddress);
            isBound = true;
        }
    }

    /**
     * Retrieves the name that this socket is bound to, if any.
     *
     * @return Local address or null if anonymous
     */
    public android.net.LocalSocketAddress getLocalSocketAddress() {
        return localAddress;
    }

    /**
     * Retrieves the input stream for this instance.
     *
     * @return input stream
     * @throws IOException
     * 		if socket has been closed or cannot be created.
     */
    public java.io.InputStream getInputStream() throws java.io.IOException {
        implCreateIfNeeded();
        return impl.getInputStream();
    }

    /**
     * Retrieves the output stream for this instance.
     *
     * @return output stream
     * @throws IOException
     * 		if socket has been closed or cannot be created.
     */
    public java.io.OutputStream getOutputStream() throws java.io.IOException {
        implCreateIfNeeded();
        return impl.getOutputStream();
    }

    /**
     * Closes the socket.
     *
     * @throws IOException
     * 		
     */
    @java.lang.Override
    public void close() throws java.io.IOException {
        implCreateIfNeeded();
        impl.close();
    }

    /**
     * Shuts down the input side of the socket.
     *
     * @throws IOException
     * 		
     */
    public void shutdownInput() throws java.io.IOException {
        implCreateIfNeeded();
        impl.shutdownInput();
    }

    /**
     * Shuts down the output side of the socket.
     *
     * @throws IOException
     * 		
     */
    public void shutdownOutput() throws java.io.IOException {
        implCreateIfNeeded();
        impl.shutdownOutput();
    }

    public void setReceiveBufferSize(int size) throws java.io.IOException {
        impl.setOption(java.net.SocketOptions.SO_RCVBUF, java.lang.Integer.valueOf(size));
    }

    public int getReceiveBufferSize() throws java.io.IOException {
        return ((java.lang.Integer) (impl.getOption(java.net.SocketOptions.SO_RCVBUF))).intValue();
    }

    public void setSoTimeout(int n) throws java.io.IOException {
        impl.setOption(java.net.SocketOptions.SO_TIMEOUT, java.lang.Integer.valueOf(n));
    }

    public int getSoTimeout() throws java.io.IOException {
        return ((java.lang.Integer) (impl.getOption(java.net.SocketOptions.SO_TIMEOUT))).intValue();
    }

    public void setSendBufferSize(int n) throws java.io.IOException {
        impl.setOption(java.net.SocketOptions.SO_SNDBUF, java.lang.Integer.valueOf(n));
    }

    public int getSendBufferSize() throws java.io.IOException {
        return ((java.lang.Integer) (impl.getOption(java.net.SocketOptions.SO_SNDBUF))).intValue();
    }

    // ???SEC
    public android.net.LocalSocketAddress getRemoteSocketAddress() {
        throw new java.lang.UnsupportedOperationException();
    }

    // ???SEC
    public synchronized boolean isConnected() {
        return isConnected;
    }

    // ???SEC
    public boolean isClosed() {
        throw new java.lang.UnsupportedOperationException();
    }

    // ???SEC
    public synchronized boolean isBound() {
        return isBound;
    }

    // ???SEC
    public boolean isOutputShutdown() {
        throw new java.lang.UnsupportedOperationException();
    }

    // ???SEC
    public boolean isInputShutdown() {
        throw new java.lang.UnsupportedOperationException();
    }

    // ???SEC
    public void connect(android.net.LocalSocketAddress endpoint, int timeout) throws java.io.IOException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Enqueues a set of file descriptors to send to the peer. The queue
     * is one deep. The file descriptors will be sent with the next write
     * of normal data, and will be delivered in a single ancillary message.
     * See "man 7 unix" SCM_RIGHTS on a desktop Linux machine.
     *
     * @param fds
     * 		non-null; file descriptors to send.
     */
    public void setFileDescriptorsForSend(java.io.FileDescriptor[] fds) {
        impl.setFileDescriptorsForSend(fds);
    }

    /**
     * Retrieves a set of file descriptors that a peer has sent through
     * an ancillary message. This method retrieves the most recent set sent,
     * and then returns null until a new set arrives.
     * File descriptors may only be passed along with regular data, so this
     * method can only return a non-null after a read operation.
     *
     * @return null or file descriptor array
     * @throws IOException
     * 		
     */
    public java.io.FileDescriptor[] getAncillaryFileDescriptors() throws java.io.IOException {
        return impl.getAncillaryFileDescriptors();
    }

    /**
     * Retrieves the credentials of this socket's peer. Only valid on
     * connected sockets.
     *
     * @return non-null; peer credentials
     * @throws IOException
     * 		
     */
    public android.net.Credentials getPeerCredentials() throws java.io.IOException {
        return impl.getPeerCredentials();
    }

    /**
     * Returns file descriptor or null if not yet open/already closed
     *
     * @return fd or null
     */
    public java.io.FileDescriptor getFileDescriptor() {
        return impl.getFileDescriptor();
    }
}

