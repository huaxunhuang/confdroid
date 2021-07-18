/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.bluetooth;


/**
 * The Android Bluetooth API is not finalized, and *will* change. Use at your
 * own risk.
 *
 * Public API for controlling the Bluetooth Pbap Service. This includes
 * Bluetooth Phone book Access profile.
 * BluetoothPbap is a proxy object for controlling the Bluetooth Pbap
 * Service via IPC.
 *
 * Creating a BluetoothPbap object will create a binding with the
 * BluetoothPbap service. Users of this object should call close() when they
 * are finished with the BluetoothPbap, so that this proxy object can unbind
 * from the service.
 *
 * This BluetoothPbap object is not immediately bound to the
 * BluetoothPbap service. Use the ServiceListener interface to obtain a
 * notification when it is bound, this is especially important if you wish to
 * immediately call methods on BluetoothPbap after construction.
 *
 * Android only supports one connected Bluetooth Pce at a time.
 *
 * @unknown 
 */
public class BluetoothPbap {
    private static final java.lang.String TAG = "BluetoothPbap";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * int extra for PBAP_STATE_CHANGED_ACTION
     */
    public static final java.lang.String PBAP_STATE = "android.bluetooth.pbap.intent.PBAP_STATE";

    /**
     * int extra for PBAP_STATE_CHANGED_ACTION
     */
    public static final java.lang.String PBAP_PREVIOUS_STATE = "android.bluetooth.pbap.intent.PBAP_PREVIOUS_STATE";

    /**
     * Indicates the state of a pbap connection state has changed.
     *  This intent will always contain PBAP_STATE, PBAP_PREVIOUS_STATE and
     *  BluetoothIntent.ADDRESS extras.
     */
    public static final java.lang.String PBAP_STATE_CHANGED_ACTION = "android.bluetooth.pbap.intent.action.PBAP_STATE_CHANGED";

    private android.bluetooth.IBluetoothPbap mService;

    private final android.content.Context mContext;

    private android.bluetooth.BluetoothPbap.ServiceListener mServiceListener;

    private android.bluetooth.BluetoothAdapter mAdapter;

    /**
     * There was an error trying to obtain the state
     */
    public static final int STATE_ERROR = -1;

    /**
     * No client currently connected
     */
    public static final int STATE_DISCONNECTED = 0;

    /**
     * Connection attempt in progress
     */
    public static final int STATE_CONNECTING = 1;

    /**
     * Client is currently connected
     */
    public static final int STATE_CONNECTED = 2;

    public static final int RESULT_FAILURE = 0;

    public static final int RESULT_SUCCESS = 1;

    /**
     * Connection canceled before completion.
     */
    public static final int RESULT_CANCELED = 2;

    /**
     * An interface for notifying Bluetooth PCE IPC clients when they have
     * been connected to the BluetoothPbap service.
     */
    public interface ServiceListener {
        /**
         * Called to notify the client when this proxy object has been
         * connected to the BluetoothPbap service. Clients must wait for
         * this callback before making IPC calls on the BluetoothPbap
         * service.
         */
        public void onServiceConnected(android.bluetooth.BluetoothPbap proxy);

        /**
         * Called to notify the client that this proxy object has been
         * disconnected from the BluetoothPbap service. Clients must not
         * make IPC calls on the BluetoothPbap service after this callback.
         * This callback will currently only occur if the application hosting
         * the BluetoothPbap service, but may be called more often in future.
         */
        public void onServiceDisconnected();
    }

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothPbap.DBG)
                android.util.Log.d(android.bluetooth.BluetoothPbap.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothPbap.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothPbap.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothPbap.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothPbap.TAG, "Binding service...");

                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothPbap proxy object.
     */
    public BluetoothPbap(android.content.Context context, android.bluetooth.BluetoothPbap.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothPbap.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "Could not bind to Bluetooth Pbap Service with " + intent);
            return false;
        }
        return true;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Close the connection to the backing service.
     * Other public functions of BluetoothPbap will return default error
     * results once close() has been called. Multiple invocations of close()
     * are ok.
     */
    public synchronized void close() {
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    /**
     * Get the current state of the BluetoothPbap service.
     *
     * @return One of the STATE_ return codes, or STATE_ERROR if this proxy
    object is currently not connected to the Pbap service.
     */
    public int getState() {
        if (android.bluetooth.BluetoothPbap.VDBG)
            android.bluetooth.BluetoothPbap.log("getState()");

        if (mService != null) {
            try {
                return mService.getState();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothPbap.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log(android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return android.bluetooth.BluetoothPbap.STATE_ERROR;
    }

    /**
     * Get the currently connected remote Bluetooth device (PCE).
     *
     * @return The remote Bluetooth device, or null if not in connected or
    connecting state, or if this proxy object is not connected to
    the Pbap service.
     */
    public android.bluetooth.BluetoothDevice getClient() {
        if (android.bluetooth.BluetoothPbap.VDBG)
            android.bluetooth.BluetoothPbap.log("getClient()");

        if (mService != null) {
            try {
                return mService.getClient();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothPbap.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log(android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return null;
    }

    /**
     * Returns true if the specified Bluetooth device is connected (does not
     * include connecting). Returns false if not connected, or if this proxy
     * object is not currently connected to the Pbap service.
     */
    public boolean isConnected(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPbap.VDBG)
            android.bluetooth.BluetoothPbap.log(("isConnected(" + device) + ")");

        if (mService != null) {
            try {
                return mService.isConnected(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothPbap.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log(android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Disconnects the current Pbap client (PCE). Currently this call blocks,
     * it may soon be made asynchronous. Returns false if this proxy object is
     * not currently connected to the Pbap service.
     */
    public boolean disconnect() {
        if (android.bluetooth.BluetoothPbap.DBG)
            android.bluetooth.BluetoothPbap.log("disconnect()");

        if (mService != null) {
            try {
                mService.disconnect();
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbap.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothPbap.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log(android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Check class bits for possible PBAP support.
     * This is a simple heuristic that tries to guess if a device with the
     * given class bits might support PBAP. It is not accurate for all
     * devices. It tries to err on the side of false positives.
     *
     * @return True if this device might support PBAP.
     */
    public static boolean doesClassMatchSink(android.bluetooth.BluetoothClass btClass) {
        // TODO optimize the rule
        switch (btClass.getDeviceClass()) {
            case android.bluetooth.BluetoothClass.Device.COMPUTER_DESKTOP :
            case android.bluetooth.BluetoothClass.Device.COMPUTER_LAPTOP :
            case android.bluetooth.BluetoothClass.Device.COMPUTER_SERVER :
            case android.bluetooth.BluetoothClass.Device.COMPUTER_UNCATEGORIZED :
                return true;
            default :
                return false;
        }
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log("Proxy object connected");

            mService = IBluetoothPbap.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothPbap.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothPbap.DBG)
                android.bluetooth.BluetoothPbap.log("Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected();
            }
        }
    };

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothPbap.TAG, msg);
    }
}

