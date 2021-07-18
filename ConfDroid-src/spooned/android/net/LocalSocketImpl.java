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
 * Socket implementation used for android.net.LocalSocket and
 * android.net.LocalServerSocket. Supports only AF_LOCAL sockets.
 */
class LocalSocketImpl {
    private android.net.LocalSocketImpl.SocketInputStream fis;

    private android.net.LocalSocketImpl.SocketOutputStream fos;

    private java.lang.Object readMonitor = new java.lang.Object();

    private java.lang.Object writeMonitor = new java.lang.Object();

    /**
     * null if closed or not yet created
     */
    private java.io.FileDescriptor fd;

    /**
     * whether fd is created internally
     */
    private boolean mFdCreatedInternally;

    // These fields are accessed by native code;
    /**
     * file descriptor array received during a previous read
     */
    java.io.FileDescriptor[] inboundFileDescriptors;

    /**
     * file descriptor array that should be written during next write
     */
    java.io.FileDescriptor[] outboundFileDescriptors;

    /**
     * An input stream for local sockets. Needed because we may
     * need to read ancillary data.
     */
    class SocketInputStream extends java.io.InputStream {
        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int available() throws java.io.IOException {
            java.io.FileDescriptor myFd = fd;
            if (myFd == null)
                throw new java.io.IOException("socket closed");

            android.util.MutableInt avail = new android.util.MutableInt(0);
            try {
                android.system.Os.ioctlInt(myFd, android.system.OsConstants.FIONREAD, avail);
            } catch (android.system.ErrnoException e) {
                throw e.rethrowAsIOException();
            }
            return avail.value;
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void close() throws java.io.IOException {
            android.net.LocalSocketImpl.this.close();
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int read() throws java.io.IOException {
            int ret;
            synchronized(readMonitor) {
                java.io.FileDescriptor myFd = fd;
                if (myFd == null)
                    throw new java.io.IOException("socket closed");

                ret = read_native(myFd);
                return ret;
            }
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int read(byte[] b) throws java.io.IOException {
            return read(b, 0, b.length);
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int read(byte[] b, int off, int len) throws java.io.IOException {
            synchronized(readMonitor) {
                java.io.FileDescriptor myFd = fd;
                if (myFd == null)
                    throw new java.io.IOException("socket closed");

                if (((off < 0) || (len < 0)) || ((off + len) > b.length)) {
                    throw new java.lang.ArrayIndexOutOfBoundsException();
                }
                int ret = readba_native(b, off, len, myFd);
                return ret;
            }
        }
    }

    /**
     * An output stream for local sockets. Needed because we may
     * need to read ancillary data.
     */
    class SocketOutputStream extends java.io.OutputStream {
        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void close() throws java.io.IOException {
            android.net.LocalSocketImpl.this.close();
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void write(byte[] b) throws java.io.IOException {
            write(b, 0, b.length);
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void write(byte[] b, int off, int len) throws java.io.IOException {
            synchronized(writeMonitor) {
                java.io.FileDescriptor myFd = fd;
                if (myFd == null)
                    throw new java.io.IOException("socket closed");

                if (((off < 0) || (len < 0)) || ((off + len) > b.length)) {
                    throw new java.lang.ArrayIndexOutOfBoundsException();
                }
                writeba_native(b, off, len, myFd);
            }
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void write(int b) throws java.io.IOException {
            synchronized(writeMonitor) {
                java.io.FileDescriptor myFd = fd;
                if (myFd == null)
                    throw new java.io.IOException("socket closed");

                write_native(b, myFd);
            }
        }

        /**
         * Wait until the data in sending queue is emptied. A polling version
         * for flush implementation.
         *
         * @throws IOException
         * 		if an i/o error occurs.
         */
        @java.lang.Override
        public void flush() throws java.io.IOException {
            java.io.FileDescriptor myFd = fd;
            if (myFd == null)
                throw new java.io.IOException("socket closed");

            // Loop until the output buffer is empty.
            android.util.MutableInt pending = new android.util.MutableInt(0);
            while (true) {
                try {
                    // See linux/net/unix/af_unix.c
                    android.system.Os.ioctlInt(myFd, android.system.OsConstants.TIOCOUTQ, pending);
                } catch (android.system.ErrnoException e) {
                    throw e.rethrowAsIOException();
                }
                if (pending.value <= 0) {
                    // The output buffer is empty.
                    break;
                }
                try {
                    java.lang.Thread.sleep(10);
                } catch (java.lang.InterruptedException ie) {
                    break;
                }
            } 
        }
    }

    private native int read_native(java.io.FileDescriptor fd) throws java.io.IOException;

    private native int readba_native(byte[] b, int off, int len, java.io.FileDescriptor fd) throws java.io.IOException;

    private native void writeba_native(byte[] b, int off, int len, java.io.FileDescriptor fd) throws java.io.IOException;

    private native void write_native(int b, java.io.FileDescriptor fd) throws java.io.IOException;

    private native void connectLocal(java.io.FileDescriptor fd, java.lang.String name, int namespace) throws java.io.IOException;

    private native void bindLocal(java.io.FileDescriptor fd, java.lang.String name, int namespace) throws java.io.IOException;

    private native android.net.Credentials getPeerCredentials_native(java.io.FileDescriptor fd) throws java.io.IOException;

    /**
     * Create a new instance.
     */
    /* package */
    LocalSocketImpl() {
    }

    /**
     * Create a new instance from a file descriptor representing
     * a bound socket. The state of the file descriptor is not checked here
     *  but the caller can verify socket state by calling listen().
     *
     * @param fd
     * 		non-null; bound file descriptor
     */
    /* package */
    LocalSocketImpl(java.io.FileDescriptor fd) throws java.io.IOException {
        this.fd = fd;
    }

    public java.lang.String toString() {
        return (super.toString() + " fd:") + fd;
    }

    /**
     * Creates a socket in the underlying OS.
     *
     * @param sockType
     * 		either {@link LocalSocket#SOCKET_DGRAM}, {@link LocalSocket#SOCKET_STREAM}
     * 		or {@link LocalSocket#SOCKET_SEQPACKET}
     * @throws IOException
     * 		
     */
    public void create(int sockType) throws java.io.IOException {
        // no error if socket already created
        // need this for LocalServerSocket.accept()
        if (fd == null) {
            int osType;
            switch (sockType) {
                case android.net.LocalSocket.SOCKET_DGRAM :
                    osType = android.system.OsConstants.SOCK_DGRAM;
                    break;
                case android.net.LocalSocket.SOCKET_STREAM :
                    osType = android.system.OsConstants.SOCK_STREAM;
                    break;
                case android.net.LocalSocket.SOCKET_SEQPACKET :
                    osType = android.system.OsConstants.SOCK_SEQPACKET;
                    break;
                default :
                    throw new java.lang.IllegalStateException("unknown sockType");
            }
            try {
                fd = android.system.Os.socket(android.system.OsConstants.AF_UNIX, osType, 0);
                mFdCreatedInternally = true;
            } catch (android.system.ErrnoException e) {
                e.rethrowAsIOException();
            }
        }
    }

    /**
     * Closes the socket.
     *
     * @throws IOException
     * 		
     */
    public void close() throws java.io.IOException {
        synchronized(this) {
            if ((fd == null) || (mFdCreatedInternally == false)) {
                fd = null;
                return;
            }
            try {
                android.system.Os.close(fd);
            } catch (android.system.ErrnoException e) {
                e.rethrowAsIOException();
            }
            fd = null;
        }
    }

    /**
     * note timeout presently ignored
     */
    protected void connect(android.net.LocalSocketAddress address, int timeout) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        connectLocal(fd, address.getName(), address.getNamespace().getId());
    }

    /**
     * Binds this socket to an endpoint name. May only be called on an instance
     * that has not yet been bound.
     *
     * @param endpoint
     * 		endpoint address
     * @throws IOException
     * 		
     */
    public void bind(android.net.LocalSocketAddress endpoint) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        bindLocal(fd, endpoint.getName(), endpoint.getNamespace().getId());
    }

    protected void listen(int backlog) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        try {
            android.system.Os.listen(fd, backlog);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Accepts a new connection to the socket. Blocks until a new
     * connection arrives.
     *
     * @param s
     * 		a socket that will be used to represent the new connection.
     * @throws IOException
     * 		
     */
    protected void accept(android.net.LocalSocketImpl s) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        try {
            s.fd = /* address */
            android.system.Os.accept(fd, null);
            s.mFdCreatedInternally = true;
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Retrieves the input stream for this instance.
     *
     * @return input stream
     * @throws IOException
     * 		if socket has been closed or cannot be created.
     */
    protected java.io.InputStream getInputStream() throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        synchronized(this) {
            if (fis == null) {
                fis = new android.net.LocalSocketImpl.SocketInputStream();
            }
            return fis;
        }
    }

    /**
     * Retrieves the output stream for this instance.
     *
     * @return output stream
     * @throws IOException
     * 		if socket has been closed or cannot be created.
     */
    protected java.io.OutputStream getOutputStream() throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        synchronized(this) {
            if (fos == null) {
                fos = new android.net.LocalSocketImpl.SocketOutputStream();
            }
            return fos;
        }
    }

    /**
     * Returns the number of bytes available for reading without blocking.
     *
     * @return >= 0 count bytes available
     * @throws IOException
     * 		
     */
    protected int available() throws java.io.IOException {
        return getInputStream().available();
    }

    /**
     * Shuts down the input side of the socket.
     *
     * @throws IOException
     * 		
     */
    protected void shutdownInput() throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        try {
            android.system.Os.shutdown(fd, android.system.OsConstants.SHUT_RD);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Shuts down the output side of the socket.
     *
     * @throws IOException
     * 		
     */
    protected void shutdownOutput() throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        try {
            android.system.Os.shutdown(fd, android.system.OsConstants.SHUT_WR);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    protected java.io.FileDescriptor getFileDescriptor() {
        return fd;
    }

    protected boolean supportsUrgentData() {
        return false;
    }

    protected void sendUrgentData(int data) throws java.io.IOException {
        throw new java.lang.RuntimeException("not impled");
    }

    public java.lang.Object getOption(int optID) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        try {
            java.lang.Object toReturn;
            switch (optID) {
                case java.net.SocketOptions.SO_TIMEOUT :
                    android.system.StructTimeval timeval = android.system.Os.getsockoptTimeval(fd, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_SNDTIMEO);
                    toReturn = ((int) (timeval.toMillis()));
                    break;
                case java.net.SocketOptions.SO_RCVBUF :
                case java.net.SocketOptions.SO_SNDBUF :
                case java.net.SocketOptions.SO_REUSEADDR :
                    int osOpt = android.net.LocalSocketImpl.javaSoToOsOpt(optID);
                    toReturn = android.system.Os.getsockoptInt(fd, android.system.OsConstants.SOL_SOCKET, osOpt);
                    break;
                case java.net.SocketOptions.SO_LINGER :
                    android.system.StructLinger linger = android.system.Os.getsockoptLinger(fd, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_LINGER);
                    if (!linger.isOn()) {
                        toReturn = -1;
                    } else {
                        toReturn = linger.l_linger;
                    }
                    break;
                case java.net.SocketOptions.TCP_NODELAY :
                    toReturn = android.system.Os.getsockoptInt(fd, android.system.OsConstants.IPPROTO_TCP, android.system.OsConstants.TCP_NODELAY);
                    break;
                default :
                    throw new java.io.IOException("Unknown option: " + optID);
            }
            return toReturn;
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public void setOption(int optID, java.lang.Object value) throws java.io.IOException {
        if (fd == null) {
            throw new java.io.IOException("socket not created");
        }
        /* Boolean.FALSE is used to disable some options, so it
        is important to distinguish between FALSE and unset.
        We define it here that -1 is unset, 0 is FALSE, and 1
        is TRUE.
         */
        int boolValue = -1;
        int intValue = 0;
        if (value instanceof java.lang.Integer) {
            intValue = ((java.lang.Integer) (value));
        } else
            if (value instanceof java.lang.Boolean) {
                boolValue = (((java.lang.Boolean) (value))) ? 1 : 0;
            } else {
                throw new java.io.IOException("bad value: " + value);
            }

        try {
            switch (optID) {
                case java.net.SocketOptions.SO_LINGER :
                    android.system.StructLinger linger = new android.system.StructLinger(boolValue, intValue);
                    android.system.Os.setsockoptLinger(fd, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_LINGER, linger);
                    break;
                case java.net.SocketOptions.SO_TIMEOUT :
                    // The option must set both send and receive timeouts.
                    // Note: The incoming timeout value is in milliseconds.
                    android.system.StructTimeval timeval = android.system.StructTimeval.fromMillis(intValue);
                    android.system.Os.setsockoptTimeval(fd, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_RCVTIMEO, timeval);
                    android.system.Os.setsockoptTimeval(fd, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_SNDTIMEO, timeval);
                    break;
                case java.net.SocketOptions.SO_RCVBUF :
                case java.net.SocketOptions.SO_SNDBUF :
                case java.net.SocketOptions.SO_REUSEADDR :
                    int osOpt = android.net.LocalSocketImpl.javaSoToOsOpt(optID);
                    android.system.Os.setsockoptInt(fd, android.system.OsConstants.SOL_SOCKET, osOpt, intValue);
                    break;
                case java.net.SocketOptions.TCP_NODELAY :
                    android.system.Os.setsockoptInt(fd, android.system.OsConstants.IPPROTO_TCP, android.system.OsConstants.TCP_NODELAY, intValue);
                    break;
                default :
                    throw new java.io.IOException("Unknown option: " + optID);
            }
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /**
     * Enqueues a set of file descriptors to send to the peer. The queue
     * is one deep. The file descriptors will be sent with the next write
     * of normal data, and will be delivered in a single ancillary message.
     * See "man 7 unix" SCM_RIGHTS on a desktop Linux machine.
     *
     * @param fds
     * 		non-null; file descriptors to send.
     * @throws IOException
     * 		
     */
    public void setFileDescriptorsForSend(java.io.FileDescriptor[] fds) {
        synchronized(writeMonitor) {
            outboundFileDescriptors = fds;
        }
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
        synchronized(readMonitor) {
            java.io.FileDescriptor[] result = inboundFileDescriptors;
            inboundFileDescriptors = null;
            return result;
        }
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
        return getPeerCredentials_native(fd);
    }

    /**
     * Retrieves the socket name from the OS.
     *
     * @return non-null; socket name
     * @throws IOException
     * 		on failure
     */
    public android.net.LocalSocketAddress getSockAddress() throws java.io.IOException {
        // This method has never been implemented.
        return null;
    }

    @java.lang.Override
    protected void finalize() throws java.io.IOException {
        close();
    }

    private static int javaSoToOsOpt(int optID) {
        switch (optID) {
            case java.net.SocketOptions.SO_SNDBUF :
                return android.system.OsConstants.SO_SNDBUF;
            case java.net.SocketOptions.SO_RCVBUF :
                return android.system.OsConstants.SO_RCVBUF;
            case java.net.SocketOptions.SO_REUSEADDR :
                return android.system.OsConstants.SO_REUSEADDR;
            default :
                throw new java.lang.UnsupportedOperationException("Unknown option: " + optID);
        }
    }
}

