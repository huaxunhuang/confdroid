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
 * Non-standard class for creating an inbound UNIX-domain socket
 * in the Linux abstract namespace.
 */
public class LocalServerSocket {
    private final android.net.LocalSocketImpl impl;

    private final android.net.LocalSocketAddress localAddress;

    /**
     * 50 seems a bit much, but it's what was here
     */
    private static final int LISTEN_BACKLOG = 50;

    /**
     * Creates a new server socket listening at specified name.
     * On the Android platform, the name is created in the Linux
     * abstract namespace (instead of on the filesystem).
     *
     * @param name
     * 		address for socket
     * @throws IOException
     * 		
     */
    public LocalServerSocket(java.lang.String name) throws java.io.IOException {
        impl = new android.net.LocalSocketImpl();
        impl.create(android.net.LocalSocket.SOCKET_STREAM);
        localAddress = new android.net.LocalSocketAddress(name);
        impl.bind(localAddress);
        impl.listen(android.net.LocalServerSocket.LISTEN_BACKLOG);
    }

    /**
     * Create a LocalServerSocket from a file descriptor that's already
     * been created and bound. listen() will be called immediately on it.
     * Used for cases where file descriptors are passed in via environment
     * variables
     *
     * @param fd
     * 		bound file descriptor
     * @throws IOException
     * 		
     */
    public LocalServerSocket(java.io.FileDescriptor fd) throws java.io.IOException {
        impl = new android.net.LocalSocketImpl(fd);
        impl.listen(android.net.LocalServerSocket.LISTEN_BACKLOG);
        localAddress = impl.getSockAddress();
    }

    /**
     * Obtains the socket's local address
     *
     * @return local address
     */
    public android.net.LocalSocketAddress getLocalSocketAddress() {
        return localAddress;
    }

    /**
     * Accepts a new connection to the socket. Blocks until a new
     * connection arrives.
     *
     * @return a socket representing the new connection.
     * @throws IOException
     * 		
     */
    public android.net.LocalSocket accept() throws java.io.IOException {
        android.net.LocalSocketImpl acceptedImpl = new android.net.LocalSocketImpl();
        impl.accept(acceptedImpl);
        return new android.net.LocalSocket(acceptedImpl, android.net.LocalSocket.SOCKET_UNKNOWN);
    }

    /**
     * Returns file descriptor or null if not yet open/already closed
     *
     * @return fd or null
     */
    public java.io.FileDescriptor getFileDescriptor() {
        return impl.getFileDescriptor();
    }

    /**
     * Closes server socket.
     *
     * @throws IOException
     * 		
     */
    public void close() throws java.io.IOException {
        impl.close();
    }
}

