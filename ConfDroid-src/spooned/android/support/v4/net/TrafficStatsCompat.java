/**
 * Copyright (C) 2012 The Android Open Source Project
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


/**
 * Helper for accessing features in TrafficStats introduced after API level 14
 * in a backwards compatible fashion.
 */
public final class TrafficStatsCompat {
    interface TrafficStatsCompatImpl {
        void clearThreadStatsTag();

        int getThreadStatsTag();

        void incrementOperationCount(int operationCount);

        void incrementOperationCount(int tag, int operationCount);

        void setThreadStatsTag(int tag);

        void tagSocket(java.net.Socket socket) throws java.net.SocketException;

        void untagSocket(java.net.Socket socket) throws java.net.SocketException;

        void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException;

        void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException;
    }

    static class BaseTrafficStatsCompatImpl implements android.support.v4.net.TrafficStatsCompat.TrafficStatsCompatImpl {
        private static class SocketTags {
            public int statsTag = -1;

            SocketTags() {
            }
        }

        private java.lang.ThreadLocal<android.support.v4.net.TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags> mThreadSocketTags = new java.lang.ThreadLocal<android.support.v4.net.TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags>() {
            @java.lang.Override
            protected android.support.v4.net.TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags initialValue() {
                return new android.support.v4.net.TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags();
            }
        };

        @java.lang.Override
        public void clearThreadStatsTag() {
            mThreadSocketTags.get().statsTag = -1;
        }

        @java.lang.Override
        public int getThreadStatsTag() {
            return mThreadSocketTags.get().statsTag;
        }

        @java.lang.Override
        public void incrementOperationCount(int operationCount) {
        }

        @java.lang.Override
        public void incrementOperationCount(int tag, int operationCount) {
        }

        @java.lang.Override
        public void setThreadStatsTag(int tag) {
            mThreadSocketTags.get().statsTag = tag;
        }

        @java.lang.Override
        public void tagSocket(java.net.Socket socket) {
        }

        @java.lang.Override
        public void untagSocket(java.net.Socket socket) {
        }

        @java.lang.Override
        public void tagDatagramSocket(java.net.DatagramSocket socket) {
        }

        @java.lang.Override
        public void untagDatagramSocket(java.net.DatagramSocket socket) {
        }
    }

    static class IcsTrafficStatsCompatImpl implements android.support.v4.net.TrafficStatsCompat.TrafficStatsCompatImpl {
        @java.lang.Override
        public void clearThreadStatsTag() {
            android.support.v4.net.TrafficStatsCompatIcs.clearThreadStatsTag();
        }

        @java.lang.Override
        public int getThreadStatsTag() {
            return android.support.v4.net.TrafficStatsCompatIcs.getThreadStatsTag();
        }

        @java.lang.Override
        public void incrementOperationCount(int operationCount) {
            android.support.v4.net.TrafficStatsCompatIcs.incrementOperationCount(operationCount);
        }

        @java.lang.Override
        public void incrementOperationCount(int tag, int operationCount) {
            android.support.v4.net.TrafficStatsCompatIcs.incrementOperationCount(tag, operationCount);
        }

        @java.lang.Override
        public void setThreadStatsTag(int tag) {
            android.support.v4.net.TrafficStatsCompatIcs.setThreadStatsTag(tag);
        }

        @java.lang.Override
        public void tagSocket(java.net.Socket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatIcs.tagSocket(socket);
        }

        @java.lang.Override
        public void untagSocket(java.net.Socket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatIcs.untagSocket(socket);
        }

        @java.lang.Override
        public void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatIcs.tagDatagramSocket(socket);
        }

        @java.lang.Override
        public void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatIcs.untagDatagramSocket(socket);
        }
    }

    static class Api24TrafficStatsCompatImpl extends android.support.v4.net.TrafficStatsCompat.IcsTrafficStatsCompatImpl {
        @java.lang.Override
        public void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatApi24.tagDatagramSocket(socket);
        }

        @java.lang.Override
        public void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
            android.support.v4.net.TrafficStatsCompatApi24.untagDatagramSocket(socket);
        }
    }

    private static final android.support.v4.net.TrafficStatsCompat.TrafficStatsCompatImpl IMPL;

    static {
        if ("N".equals(android.os.Build.VERSION.CODENAME)) {
            IMPL = new android.support.v4.net.TrafficStatsCompat.Api24TrafficStatsCompatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                IMPL = new android.support.v4.net.TrafficStatsCompat.IcsTrafficStatsCompatImpl();
            } else {
                IMPL = new android.support.v4.net.TrafficStatsCompat.BaseTrafficStatsCompatImpl();
            }

    }

    /**
     * Clear active tag used when accounting {@link Socket} traffic originating
     * from the current thread.
     */
    public static void clearThreadStatsTag() {
        android.support.v4.net.TrafficStatsCompat.IMPL.clearThreadStatsTag();
    }

    /**
     * Get the active tag used when accounting {@link Socket} traffic originating
     * from the current thread. Only one active tag per thread is supported.
     * {@link #tagSocket(Socket)}.
     */
    public static int getThreadStatsTag() {
        return android.support.v4.net.TrafficStatsCompat.IMPL.getThreadStatsTag();
    }

    /**
     * Increment count of network operations performed under the accounting tag
     * currently active on the calling thread. This can be used to derive
     * bytes-per-operation.
     *
     * @param operationCount
     * 		Number of operations to increment count by.
     */
    public static void incrementOperationCount(int operationCount) {
        android.support.v4.net.TrafficStatsCompat.IMPL.incrementOperationCount(operationCount);
    }

    /**
     * Increment count of network operations performed under the given
     * accounting tag. This can be used to derive bytes-per-operation.
     *
     * @param tag
     * 		Accounting tag used in {@link #setThreadStatsTag(int)}.
     * @param operationCount
     * 		Number of operations to increment count by.
     */
    public static void incrementOperationCount(int tag, int operationCount) {
        android.support.v4.net.TrafficStatsCompat.IMPL.incrementOperationCount(tag, operationCount);
    }

    /**
     * Set active tag to use when accounting {@link Socket} traffic originating
     * from the current thread. Only one active tag per thread is supported.
     * <p>
     * Changes only take effect during subsequent calls to
     * {@link #tagSocket(Socket)}.
     * <p>
     * Tags between {@code 0xFFFFFF00} and {@code 0xFFFFFFFF} are reserved and
     * used internally by system services like DownloadManager when performing
     * traffic on behalf of an application.
     */
    public static void setThreadStatsTag(int tag) {
        android.support.v4.net.TrafficStatsCompat.IMPL.setThreadStatsTag(tag);
    }

    /**
     * Tag the given {@link Socket} with any statistics parameters active for
     * the current thread. Subsequent calls always replace any existing
     * parameters. When finished, call {@link #untagSocket(Socket)} to remove
     * statistics parameters.
     *
     * @see #setThreadStatsTag(int)
     */
    public static void tagSocket(java.net.Socket socket) throws java.net.SocketException {
        android.support.v4.net.TrafficStatsCompat.IMPL.tagSocket(socket);
    }

    /**
     * Remove any statistics parameters from the given {@link Socket}.
     */
    public static void untagSocket(java.net.Socket socket) throws java.net.SocketException {
        android.support.v4.net.TrafficStatsCompat.IMPL.untagSocket(socket);
    }

    /**
     * Tag the given {@link DatagramSocket} with any statistics parameters
     * active for the current thread. Subsequent calls always replace any
     * existing parameters. When finished, call
     * {@link #untagDatagramSocket(DatagramSocket)} to remove statistics
     * parameters.
     *
     * @see #setThreadStatsTag(int)
     */
    public static void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        android.support.v4.net.TrafficStatsCompat.IMPL.tagDatagramSocket(socket);
    }

    /**
     * Remove any statistics parameters from the given {@link DatagramSocket}.
     */
    public static void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        android.support.v4.net.TrafficStatsCompat.IMPL.untagDatagramSocket(socket);
    }

    private TrafficStatsCompat() {
    }
}

