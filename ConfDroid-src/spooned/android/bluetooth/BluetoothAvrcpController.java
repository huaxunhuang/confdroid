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
package android.bluetooth;


/**
 * This class provides the public APIs to control the Bluetooth AVRCP Controller. It currently
 * supports player information, playback support and track metadata.
 *
 * <p>BluetoothAvrcpController is a proxy object for controlling the Bluetooth AVRCP
 * Service via IPC. Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothAvrcpController proxy object.
 *
 * {@hide }
 */
public final class BluetoothAvrcpController implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothAvrcpController";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;

    /**
     * Intent used to broadcast the change in connection state of the AVRCP Controller
     * profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile.</li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     *
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_DISCONNECTED}, {@link #STATE_CONNECTING},
     * {@link #STATE_CONNECTED}, {@link #STATE_DISCONNECTING}.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.avrcp-controller.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Intent used to broadcast the change in metadata state of playing track on the AVRCP
     * AG.
     *
     * <p>This intent will have the two extras:
     * <ul>
     *    <li> {@link #EXTRA_METADATA} - {@link MediaMetadata} containing the current metadata.</li>
     *    <li> {@link #EXTRA_PLAYBACK} - {@link PlaybackState} containing the current playback
     *    state. </li>
     * </ul>
     */
    public static final java.lang.String ACTION_TRACK_EVENT = "android.bluetooth.avrcp-controller.profile.action.TRACK_EVENT";

    /**
     * Intent used to broadcast the change in player application setting state on AVRCP AG.
     *
     * <p>This intent will have the following extras:
     * <ul>
     *    <li> {@link #EXTRA_PLAYER_SETTING} - {@link BluetoothAvrcpPlayerSettings} containing the
     *    most recent player setting. </li>
     * </ul>
     */
    public static final java.lang.String ACTION_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.action.PLAYER_SETTING";

    public static final java.lang.String EXTRA_METADATA = "android.bluetooth.avrcp-controller.profile.extra.METADATA";

    public static final java.lang.String EXTRA_PLAYBACK = "android.bluetooth.avrcp-controller.profile.extra.PLAYBACK";

    public static final java.lang.String EXTRA_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.extra.PLAYER_SETTING";

    /* KeyCoded for Pass Through Commands */
    public static final int PASS_THRU_CMD_ID_PLAY = 0x44;

    public static final int PASS_THRU_CMD_ID_PAUSE = 0x46;

    public static final int PASS_THRU_CMD_ID_VOL_UP = 0x41;

    public static final int PASS_THRU_CMD_ID_VOL_DOWN = 0x42;

    public static final int PASS_THRU_CMD_ID_STOP = 0x45;

    public static final int PASS_THRU_CMD_ID_FF = 0x49;

    public static final int PASS_THRU_CMD_ID_REWIND = 0x48;

    public static final int PASS_THRU_CMD_ID_FORWARD = 0x4b;

    public static final int PASS_THRU_CMD_ID_BACKWARD = 0x4c;

    /* Key State Variables */
    public static final int KEY_STATE_PRESSED = 0;

    public static final int KEY_STATE_RELEASED = 1;

    /* Group Navigation Key Codes */
    public static final int PASS_THRU_CMD_ID_NEXT_GRP = 0x0;

    public static final int PASS_THRU_CMD_ID_PREV_GRP = 0x1;

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.IBluetoothAvrcpController mService;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothAvrcpController.DBG)
                android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothAvrcpController.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothAvrcpController.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "Binding service...");

                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothAvrcpController proxy object for interacting with the local
     * Bluetooth AVRCP service.
     */
    /* package */
    BluetoothAvrcpController(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothAvrcpController.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Could not bind to Bluetooth AVRCP Controller Service with " + intent);
            return false;
        }
        return true;
    }

    /* package */
    void close() {
        mServiceListener = null;
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "", re);
                }
            }
        }
    }

    public void finalize() {
        close();
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothAvrcpController.VDBG)
            android.bluetooth.BluetoothAvrcpController.log("getConnectedDevices()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothAvrcpController.VDBG)
            android.bluetooth.BluetoothAvrcpController.log("getDevicesMatchingStates()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothAvrcpController.VDBG)
            android.bluetooth.BluetoothAvrcpController.log(("getState(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    public void sendPassThroughCmd(android.bluetooth.BluetoothDevice device, int keyCode, int keyState) {
        if (android.bluetooth.BluetoothAvrcpController.DBG)
            android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "sendPassThroughCmd");

        if ((mService != null) && isEnabled()) {
            try {
                mService.sendPassThroughCmd(device, keyCode, keyState);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in sendPassThroughCmd()", e);
                return;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

    }

    /**
     * Gets the player application settings.
     *
     * @return the {@link BluetoothAvrcpPlayerSettings} or {@link null} if there is an error.
     */
    public android.bluetooth.BluetoothAvrcpPlayerSettings getPlayerSettings(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothAvrcpController.DBG)
            android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "getPlayerSettings");

        android.bluetooth.BluetoothAvrcpPlayerSettings settings = null;
        if ((mService != null) && isEnabled()) {
            try {
                settings = mService.getPlayerSettings(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in getMetadata() " + e);
                return null;
            }
        }
        return settings;
    }

    /**
     * Gets the metadata for the current track.
     *
     * This should be usually called when application UI needs to be updated, eg. when the track
     * changes or immediately after connecting and getting the current state.
     *
     * @return the {@link MediaMetadata} or {@link null} if there is an error.
     */
    public android.media.MediaMetadata getMetadata(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothAvrcpController.DBG)
            android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "getMetadata");

        android.media.MediaMetadata metadata = null;
        if ((mService != null) && isEnabled()) {
            try {
                metadata = mService.getMetadata(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in getMetadata() " + e);
                return null;
            }
        }
        return metadata;
    }

    /**
     * Gets the playback state for current track.
     *
     * When the application is first connecting it can use current track state to get playback info.
     * For all further updates it should listen to notifications.
     *
     * @return the {@link PlaybackState} or {@link null} if there is an error.
     */
    public android.media.session.PlaybackState getPlaybackState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothAvrcpController.DBG)
            android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "getPlaybackState");

        android.media.session.PlaybackState playbackState = null;
        if ((mService != null) && isEnabled()) {
            try {
                playbackState = mService.getPlaybackState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in getPlaybackState() " + e);
                return null;
            }
        }
        return playbackState;
    }

    /**
     * Sets the player app setting for current player.
     * returns true in case setting is supported by remote, false otherwise
     */
    public boolean setPlayerApplicationSetting(android.bluetooth.BluetoothAvrcpPlayerSettings plAppSetting) {
        if (android.bluetooth.BluetoothAvrcpController.DBG)
            android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "setPlayerApplicationSetting");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.setPlayerApplicationSetting(plAppSetting);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in setPlayerApplicationSetting() " + e);
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

        return false;
    }

    /* Send Group Navigation Command to Remote.
    possible keycode values: next_grp, previous_grp defined above
     */
    public void sendGroupNavigationCmd(android.bluetooth.BluetoothDevice device, int keyCode, int keyState) {
        android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, (((("sendGroupNavigationCmd dev = " + device) + " key ") + keyCode) + " State = ") + keyState);
        if ((mService != null) && isEnabled()) {
            try {
                mService.sendGroupNavigationCmd(device, keyCode, keyState);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothAvrcpController.TAG, "Error talking to BT service in sendGroupNavigationCmd()", e);
                return;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy not attached to service");

    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothAvrcpController.DBG)
                android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy object connected");

            mService = IBluetoothAvrcpController.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.AVRCP_CONTROLLER, android.bluetooth.BluetoothAvrcpController.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothAvrcpController.DBG)
                android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, "Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.AVRCP_CONTROLLER);
            }
        }
    };

    private boolean isEnabled() {
        if (mAdapter.getState() == android.bluetooth.BluetoothAdapter.STATE_ON)
            return true;

        return false;
    }

    private boolean isValidDevice(android.bluetooth.BluetoothDevice device) {
        if (device == null)
            return false;

        if (android.bluetooth.BluetoothAdapter.checkBluetoothAddress(device.getAddress()))
            return true;

        return false;
    }

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothAvrcpController.TAG, msg);
    }
}

