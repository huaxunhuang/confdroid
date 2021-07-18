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


class BluetoothMasObexClientSession {
    private static final java.lang.String TAG = "BluetoothMasObexClientSession";

    private static final byte[] MAS_TARGET = new byte[]{ ((byte) (0xbb)), 0x58, 0x2b, 0x40, 0x42, 0xc, 0x11, ((byte) (0xdb)), ((byte) (0xb0)), ((byte) (0xde)), 0x8, 0x0, 0x20, 0xc, ((byte) (0x9a)), 0x66 };

    private boolean DBG = true;

    static final int MSG_OBEX_CONNECTED = 100;

    static final int MSG_OBEX_DISCONNECTED = 101;

    static final int MSG_REQUEST_COMPLETED = 102;

    private static final int CONNECT = 0;

    private static final int DISCONNECT = 1;

    private static final int REQUEST = 2;

    private final javax.obex.ObexTransport mTransport;

    private final android.os.Handler mSessionHandler;

    private javax.obex.ClientSession mSession;

    private android.os.HandlerThread mThread;

    private android.os.Handler mHandler;

    private boolean mConnected;

    private static class ObexClientHandler extends android.os.Handler {
        java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMasObexClientSession> mInst;

        ObexClientHandler(android.os.Looper looper, android.bluetooth.client.map.BluetoothMasObexClientSession inst) {
            super(looper);
            mInst = new java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMasObexClientSession>(inst);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.bluetooth.client.map.BluetoothMasObexClientSession inst = mInst.get();
            if ((!inst.connected()) && (msg.what != android.bluetooth.client.map.BluetoothMasObexClientSession.CONNECT)) {
                android.util.Log.w(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, ("Cannot execute " + msg) + " when not CONNECTED.");
                return;
            }
            switch (msg.what) {
                case android.bluetooth.client.map.BluetoothMasObexClientSession.CONNECT :
                    inst.connect();
                    break;
                case android.bluetooth.client.map.BluetoothMasObexClientSession.DISCONNECT :
                    inst.disconnect();
                    break;
                case android.bluetooth.client.map.BluetoothMasObexClientSession.REQUEST :
                    inst.executeRequest(((android.bluetooth.client.map.BluetoothMasRequest) (msg.obj)));
                    break;
            }
        }
    }

    public BluetoothMasObexClientSession(javax.obex.ObexTransport transport, android.os.Handler handler) {
        mTransport = transport;
        mSessionHandler = handler;
    }

    public void start() {
        if (DBG)
            android.util.Log.d(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "start called.");

        if (mConnected) {
            if (DBG)
                android.util.Log.d(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "Already connected, nothing to do.");

            return;
        }
        // Start a thread to handle messages here.
        mThread = new android.os.HandlerThread("BluetoothMasObexClientSessionThread");
        mThread.start();
        mHandler = new android.bluetooth.client.map.BluetoothMasObexClientSession.ObexClientHandler(mThread.getLooper(), this);
        // Connect it to the target device via OBEX.
        mHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasObexClientSession.CONNECT).sendToTarget();
    }

    public boolean makeRequest(android.bluetooth.client.map.BluetoothMasRequest request) {
        if (DBG)
            android.util.Log.d(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "makeRequest called with: " + request);

        boolean status = mHandler.sendMessage(mHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasObexClientSession.REQUEST, request));
        if (!status) {
            android.util.Log.e(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "Adding messages failed, state: " + mConnected);
            return false;
        }
        return true;
    }

    public void stop() {
        if (DBG)
            android.util.Log.d(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "stop called...");

        mThread.quit();
        disconnect();
    }

    private void connect() {
        try {
            mSession = new javax.obex.ClientSession(mTransport);
            javax.obex.HeaderSet headerset = new javax.obex.HeaderSet();
            headerset.setHeader(HeaderSet.TARGET, android.bluetooth.client.map.BluetoothMasObexClientSession.MAS_TARGET);
            headerset = mSession.connect(headerset);
            if (headerset.getResponseCode() == javax.obex.ResponseCodes.OBEX_HTTP_OK) {
                mConnected = true;
                mSessionHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_OBEX_CONNECTED).sendToTarget();
            } else {
                disconnect();
            }
        } catch (java.io.IOException e) {
            disconnect();
        }
    }

    private void disconnect() {
        if (mSession != null) {
            try {
                mSession.disconnect(null);
            } catch (java.io.IOException e) {
            }
            try {
                mSession.close();
            } catch (java.io.IOException e) {
            }
        }
        mConnected = false;
        mSessionHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_OBEX_DISCONNECTED).sendToTarget();
    }

    private void executeRequest(android.bluetooth.client.map.BluetoothMasRequest request) {
        try {
            request.execute(mSession);
            mSessionHandler.obtainMessage(android.bluetooth.client.map.BluetoothMasObexClientSession.MSG_REQUEST_COMPLETED, request).sendToTarget();
        } catch (java.io.IOException e) {
            if (DBG)
                android.util.Log.d(android.bluetooth.client.map.BluetoothMasObexClientSession.TAG, "Request failed: " + request);

            // Disconnect to cleanup.
            disconnect();
        }
    }

    private boolean connected() {
        return mConnected;
    }
}

