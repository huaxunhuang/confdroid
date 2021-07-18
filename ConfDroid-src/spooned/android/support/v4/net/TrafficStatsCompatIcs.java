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
 * Implementation of TrafficStatsCompat that can call ICS APIs.
 */
class TrafficStatsCompatIcs {
    public static void clearThreadStatsTag() {
        android.net.TrafficStats.clearThreadStatsTag();
    }

    public static int getThreadStatsTag() {
        return android.net.TrafficStats.getThreadStatsTag();
    }

    public static void incrementOperationCount(int operationCount) {
        android.net.TrafficStats.incrementOperationCount(operationCount);
    }

    public static void incrementOperationCount(int tag, int operationCount) {
        android.net.TrafficStats.incrementOperationCount(tag, operationCount);
    }

    public static void setThreadStatsTag(int tag) {
        android.net.TrafficStats.setThreadStatsTag(tag);
    }

    public static void tagSocket(java.net.Socket socket) throws java.net.SocketException {
        android.net.TrafficStats.tagSocket(socket);
    }

    public static void untagSocket(java.net.Socket socket) throws java.net.SocketException {
        android.net.TrafficStats.untagSocket(socket);
    }

    public static void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        final android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.fromDatagramSocket(socket);
        android.net.TrafficStats.tagSocket(new android.support.v4.net.DatagramSocketWrapper(socket, pfd.getFileDescriptor()));
        // The developer is still using the FD, so we need to detach it to
        // prevent the PFD finalizer from closing it in their face. We had to
        // wait until after the tagging call above, since detaching clears out
        // the getFileDescriptor() result which tagging depends on.
        pfd.detachFd();
    }

    public static void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        final android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.fromDatagramSocket(socket);
        android.net.TrafficStats.untagSocket(new android.support.v4.net.DatagramSocketWrapper(socket, pfd.getFileDescriptor()));
        // The developer is still using the FD, so we need to detach it to
        // prevent the PFD finalizer from closing it in their face. We had to
        // wait until after the tagging call above, since detaching clears out
        // the getFileDescriptor() result which tagging depends on.
        pfd.detachFd();
    }
}

