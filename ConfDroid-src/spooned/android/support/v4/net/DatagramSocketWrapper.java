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
package android.support.v4.net;


class DatagramSocketWrapper extends java.net.Socket {
    public DatagramSocketWrapper(java.net.DatagramSocket socket, java.io.FileDescriptor fd) throws java.net.SocketException {
        super(new android.support.v4.net.DatagramSocketWrapper.DatagramSocketImplWrapper(socket, fd));
    }

    /**
     * Empty implementation which wires in the given {@link FileDescriptor}.
     */
    private static class DatagramSocketImplWrapper extends java.net.SocketImpl {
        public DatagramSocketImplWrapper(java.net.DatagramSocket socket, java.io.FileDescriptor fd) {
            super();
            this.localport = socket.getLocalPort();
            this.fd = fd;
        }

        @java.lang.Override
        protected void accept(java.net.SocketImpl newSocket) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected int available() throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void bind(java.net.InetAddress address, int port) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void close() throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void connect(java.lang.String host, int port) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void connect(java.net.InetAddress address, int port) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void create(boolean isStreaming) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected java.io.InputStream getInputStream() throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected java.io.OutputStream getOutputStream() throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void listen(int backlog) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void connect(java.net.SocketAddress remoteAddr, int timeout) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        protected void sendUrgentData(int value) throws java.io.IOException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public java.lang.Object getOption(int optID) throws java.net.SocketException {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public void setOption(int optID, java.lang.Object val) throws java.net.SocketException {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

