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
package android.bluetooth.client.map;


class BluetoothMapRfcommTransport implements javax.obex.ObexTransport {
    private final android.bluetooth.BluetoothSocket mSocket;

    public BluetoothMapRfcommTransport(android.bluetooth.BluetoothSocket socket) {
        super();
        mSocket = socket;
    }

    @java.lang.Override
    public void create() throws java.io.IOException {
    }

    @java.lang.Override
    public void listen() throws java.io.IOException {
    }

    @java.lang.Override
    public void close() throws java.io.IOException {
        mSocket.close();
    }

    @java.lang.Override
    public void connect() throws java.io.IOException {
    }

    @java.lang.Override
    public void disconnect() throws java.io.IOException {
    }

    @java.lang.Override
    public java.io.InputStream openInputStream() throws java.io.IOException {
        return mSocket.getInputStream();
    }

    @java.lang.Override
    public java.io.OutputStream openOutputStream() throws java.io.IOException {
        return mSocket.getOutputStream();
    }

    @java.lang.Override
    public java.io.DataInputStream openDataInputStream() throws java.io.IOException {
        return new java.io.DataInputStream(openInputStream());
    }

    @java.lang.Override
    public java.io.DataOutputStream openDataOutputStream() throws java.io.IOException {
        return new java.io.DataOutputStream(openOutputStream());
    }

    @java.lang.Override
    public int getMaxTransmitPacketSize() {
        return -1;
    }

    @java.lang.Override
    public int getMaxReceivePacketSize() {
        return -1;
    }

    @java.lang.Override
    public boolean isSrmSupported() {
        return false;
    }
}

