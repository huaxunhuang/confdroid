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


class BluetoothMnsService {
    private static final java.lang.String TAG = "BluetoothMnsService";

    private static final android.os.ParcelUuid MAP_MNS = android.os.ParcelUuid.fromString("00001133-0000-1000-8000-00805F9B34FB");

    static final int MSG_EVENT = 1;

    /* for BluetoothMasClient */
    static final int EVENT_REPORT = 1001;

    /* these are shared across instances */
    private static android.util.SparseArray<android.os.Handler> mCallbacks = null;

    private static android.bluetooth.client.map.BluetoothMnsService.SocketAcceptThread mAcceptThread = null;

    private static android.os.Handler mSessionHandler = null;

    private static android.bluetooth.BluetoothServerSocket mServerSocket = null;

    private static class SessionHandler extends android.os.Handler {
        private final java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMnsService> mService;

        SessionHandler(android.bluetooth.client.map.BluetoothMnsService service) {
            mService = new java.lang.ref.WeakReference<android.bluetooth.client.map.BluetoothMnsService>(service);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.util.Log.d(android.bluetooth.client.map.BluetoothMnsService.TAG, "Handler: msg: " + msg.what);
            switch (msg.what) {
                case android.bluetooth.client.map.BluetoothMnsService.MSG_EVENT :
                    int instanceId = msg.arg1;
                    synchronized(android.bluetooth.client.map.BluetoothMnsService.mCallbacks) {
                        android.os.Handler cb = android.bluetooth.client.map.BluetoothMnsService.mCallbacks.get(instanceId);
                        if (cb != null) {
                            android.bluetooth.client.map.BluetoothMapEventReport ev = ((android.bluetooth.client.map.BluetoothMapEventReport) (msg.obj));
                            cb.obtainMessage(android.bluetooth.client.map.BluetoothMnsService.EVENT_REPORT, ev).sendToTarget();
                        } else {
                            android.util.Log.w(android.bluetooth.client.map.BluetoothMnsService.TAG, "Got event for instance which is not registered: " + instanceId);
                        }
                    }
                    break;
            }
        }
    }

    private static class SocketAcceptThread extends java.lang.Thread {
        private boolean mInterrupted = false;

        @java.lang.Override
        public void run() {
            if (android.bluetooth.client.map.BluetoothMnsService.mServerSocket != null) {
                android.util.Log.w(android.bluetooth.client.map.BluetoothMnsService.TAG, "Socket already created, exiting");
                return;
            }
            try {
                android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
                android.bluetooth.client.map.BluetoothMnsService.mServerSocket = adapter.listenUsingEncryptedRfcommWithServiceRecord("MAP Message Notification Service", android.bluetooth.client.map.BluetoothMnsService.MAP_MNS.getUuid());
            } catch (java.io.IOException e) {
                mInterrupted = true;
                android.util.Log.e(android.bluetooth.client.map.BluetoothMnsService.TAG, "I/O exception when trying to create server socket", e);
            }
            while (!mInterrupted) {
                try {
                    android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "waiting to accept connection...");
                    android.bluetooth.BluetoothSocket sock = android.bluetooth.client.map.BluetoothMnsService.mServerSocket.accept();
                    android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "new incoming connection from " + sock.getRemoteDevice().getName());
                    // session will live until closed by remote
                    android.bluetooth.client.map.BluetoothMnsObexServer srv = new android.bluetooth.client.map.BluetoothMnsObexServer(android.bluetooth.client.map.BluetoothMnsService.mSessionHandler);
                    android.bluetooth.client.map.BluetoothMapRfcommTransport transport = new android.bluetooth.client.map.BluetoothMapRfcommTransport(sock);
                    new javax.obex.ServerSession(transport, srv, null);
                } catch (java.io.IOException ex) {
                    android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "I/O exception when waiting to accept (aborted?)");
                    mInterrupted = true;
                }
            } 
            if (android.bluetooth.client.map.BluetoothMnsService.mServerSocket != null) {
                try {
                    android.bluetooth.client.map.BluetoothMnsService.mServerSocket.close();
                } catch (java.io.IOException e) {
                    // do nothing
                }
                android.bluetooth.client.map.BluetoothMnsService.mServerSocket = null;
            }
        }
    }

    BluetoothMnsService() {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "BluetoothMnsService()");
        if (android.bluetooth.client.map.BluetoothMnsService.mCallbacks == null) {
            android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "BluetoothMnsService(): allocating callbacks");
            android.bluetooth.client.map.BluetoothMnsService.mCallbacks = new android.util.SparseArray<android.os.Handler>();
        }
        if (android.bluetooth.client.map.BluetoothMnsService.mSessionHandler == null) {
            android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "BluetoothMnsService(): allocating session handler");
            android.bluetooth.client.map.BluetoothMnsService.mSessionHandler = new android.bluetooth.client.map.BluetoothMnsService.SessionHandler(this);
        }
    }

    public void registerCallback(int instanceId, android.os.Handler callback) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "registerCallback()");
        synchronized(android.bluetooth.client.map.BluetoothMnsService.mCallbacks) {
            android.bluetooth.client.map.BluetoothMnsService.mCallbacks.put(instanceId, callback);
            if (android.bluetooth.client.map.BluetoothMnsService.mAcceptThread == null) {
                android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "registerCallback(): starting MNS server");
                android.bluetooth.client.map.BluetoothMnsService.mAcceptThread = new android.bluetooth.client.map.BluetoothMnsService.SocketAcceptThread();
                android.bluetooth.client.map.BluetoothMnsService.mAcceptThread.setName("BluetoothMnsAcceptThread");
                android.bluetooth.client.map.BluetoothMnsService.mAcceptThread.start();
            }
        }
    }

    public void unregisterCallback(int instanceId) {
        android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "unregisterCallback()");
        synchronized(android.bluetooth.client.map.BluetoothMnsService.mCallbacks) {
            android.bluetooth.client.map.BluetoothMnsService.mCallbacks.remove(instanceId);
            if (android.bluetooth.client.map.BluetoothMnsService.mCallbacks.size() == 0) {
                android.util.Log.v(android.bluetooth.client.map.BluetoothMnsService.TAG, "unregisterCallback(): shutting down MNS server");
                if (android.bluetooth.client.map.BluetoothMnsService.mServerSocket != null) {
                    try {
                        android.bluetooth.client.map.BluetoothMnsService.mServerSocket.close();
                    } catch (java.io.IOException e) {
                    }
                    android.bluetooth.client.map.BluetoothMnsService.mServerSocket = null;
                }
                android.bluetooth.client.map.BluetoothMnsService.mAcceptThread.interrupt();
                try {
                    android.bluetooth.client.map.BluetoothMnsService.mAcceptThread.join(5000);
                } catch (java.lang.InterruptedException e) {
                }
                android.bluetooth.client.map.BluetoothMnsService.mAcceptThread = null;
            }
        }
    }
}

