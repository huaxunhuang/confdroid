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
 * Public API to control Hands Free Profile (HFP role only).
 * <p>
 * This class defines methods that shall be used by application to manage profile
 * connection, calls states and calls actions.
 * <p>
 *
 * @unknown 
 */
public final class BluetoothHeadsetClient implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothHeadsetClient";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Intent sent whenever connection to remote changes.
     *
     * <p>It includes two extras:
     * <code>BluetoothProfile.EXTRA_PREVIOUS_STATE</code>
     * and <code>BluetoothProfile.EXTRA_STATE</code>, which
     * are mandatory.
     * <p>There are also non mandatory feature extras:
     * {@link #EXTRA_AG_FEATURE_3WAY_CALLING},
     * {@link #EXTRA_AG_FEATURE_VOICE_RECOGNITION},
     * {@link #EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT},
     * {@link #EXTRA_AG_FEATURE_REJECT_CALL},
     * {@link #EXTRA_AG_FEATURE_ECC},
     * {@link #EXTRA_AG_FEATURE_RESPONSE_AND_HOLD},
     * {@link #EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL},
     * {@link #EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL},
     * {@link #EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT},
     * {@link #EXTRA_AG_FEATURE_MERGE},
     * {@link #EXTRA_AG_FEATURE_MERGE_AND_DETACH},
     * sent as boolean values only when <code>EXTRA_STATE</code>
     * is set to <code>STATE_CONNECTED</code>.</p>
     *
     * <p>Note that features supported by AG are being sent as
     * booleans with value <code>true</code>,
     * and not supported ones are <strong>not</strong> being sent at all.</p>
     */
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Intent sent whenever audio state changes.
     *
     * <p>It includes two mandatory extras:
     * {@link BluetoothProfile.EXTRA_STATE},
     * {@link BluetoothProfile.EXTRA_PREVIOUS_STATE},
     * with possible values:
     * {@link #STATE_AUDIO_CONNECTING},
     * {@link #STATE_AUDIO_CONNECTED},
     * {@link #STATE_AUDIO_DISCONNECTED}</p>
     * <p>When <code>EXTRA_STATE</code> is set
     * to </code>STATE_AUDIO_CONNECTED</code>,
     * it also includes {@link #EXTRA_AUDIO_WBS}
     * indicating wide band speech support.</p>
     */
    public static final java.lang.String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.AUDIO_STATE_CHANGED";

    /**
     * Intent sending updates of the Audio Gateway state.
     * Each extra is being sent only when value it
     * represents has been changed recently on AG.
     * <p>It can contain one or more of the following extras:
     * {@link #EXTRA_NETWORK_STATUS},
     * {@link #EXTRA_NETWORK_SIGNAL_STRENGTH},
     * {@link #EXTRA_NETWORK_ROAMING},
     * {@link #EXTRA_BATTERY_LEVEL},
     * {@link #EXTRA_OPERATOR_NAME},
     * {@link #EXTRA_VOICE_RECOGNITION},
     * {@link #EXTRA_IN_BAND_RING}</p>
     */
    public static final java.lang.String ACTION_AG_EVENT = "android.bluetooth.headsetclient.profile.action.AG_EVENT";

    /**
     * Intent sent whenever state of a call changes.
     *
     * <p>It includes:
     * {@link #EXTRA_CALL},
     * with value of {@link BluetoothHeadsetClientCall} instance,
     * representing actual call state.</p>
     */
    public static final java.lang.String ACTION_CALL_CHANGED = "android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED";

    /**
     * Intent that notifies about the result of the last issued action.
     * Please note that not every action results in explicit action result code being sent.
     * Instead other notifications about new Audio Gateway state might be sent,
     * like <code>ACTION_AG_EVENT</code> with <code>EXTRA_VOICE_RECOGNITION</code> value
     * when for example user started voice recognition from HF unit.
     */
    public static final java.lang.String ACTION_RESULT = "android.bluetooth.headsetclient.profile.action.RESULT";

    /**
     * Intent that notifies about the number attached to the last voice tag
     * recorded on AG.
     *
     * <p>It contains:
     * {@link #EXTRA_NUMBER},
     * with a <code>String</code> value representing phone number.</p>
     */
    public static final java.lang.String ACTION_LAST_VTAG = "android.bluetooth.headsetclient.profile.action.LAST_VTAG";

    public static final int STATE_AUDIO_DISCONNECTED = 0;

    public static final int STATE_AUDIO_CONNECTING = 1;

    public static final int STATE_AUDIO_CONNECTED = 2;

    /**
     * Extra with information if connected audio is WBS.
     * <p>Possible values: <code>true</code>,
     *                     <code>false</code>.</p>
     */
    public static final java.lang.String EXTRA_AUDIO_WBS = "android.bluetooth.headsetclient.extra.AUDIO_WBS";

    /**
     * Extra for AG_EVENT indicates network status.
     * <p>Value: 0 - network unavailable,
     *           1 - network available </p>
     */
    public static final java.lang.String EXTRA_NETWORK_STATUS = "android.bluetooth.headsetclient.extra.NETWORK_STATUS";

    /**
     * Extra for AG_EVENT intent indicates network signal strength.
     * <p>Value: <code>Integer</code> representing signal strength.</p>
     */
    public static final java.lang.String EXTRA_NETWORK_SIGNAL_STRENGTH = "android.bluetooth.headsetclient.extra.NETWORK_SIGNAL_STRENGTH";

    /**
     * Extra for AG_EVENT intent indicates roaming state.
     * <p>Value: 0 - no roaming
     *           1 - active roaming</p>
     */
    public static final java.lang.String EXTRA_NETWORK_ROAMING = "android.bluetooth.headsetclient.extra.NETWORK_ROAMING";

    /**
     * Extra for AG_EVENT intent indicates the battery level.
     * <p>Value: <code>Integer</code> representing signal strength.</p>
     */
    public static final java.lang.String EXTRA_BATTERY_LEVEL = "android.bluetooth.headsetclient.extra.BATTERY_LEVEL";

    /**
     * Extra for AG_EVENT intent indicates operator name.
     * <p>Value: <code>String</code> representing operator name.</p>
     */
    public static final java.lang.String EXTRA_OPERATOR_NAME = "android.bluetooth.headsetclient.extra.OPERATOR_NAME";

    /**
     * Extra for AG_EVENT intent indicates voice recognition state.
     * <p>Value:
     *          0 - voice recognition stopped,
     *          1 - voice recognition started.</p>
     */
    public static final java.lang.String EXTRA_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.VOICE_RECOGNITION";

    /**
     * Extra for AG_EVENT intent indicates in band ring state.
     * <p>Value:
     *          0 - in band ring tone not supported, or
     *          1 - in band ring tone supported.</p>
     */
    public static final java.lang.String EXTRA_IN_BAND_RING = "android.bluetooth.headsetclient.extra.IN_BAND_RING";

    /**
     * Extra for AG_EVENT intent indicates subscriber info.
     * <p>Value: <code>String</code> containing subscriber information.</p>
     */
    public static final java.lang.String EXTRA_SUBSCRIBER_INFO = "android.bluetooth.headsetclient.extra.SUBSCRIBER_INFO";

    /**
     * Extra for AG_CALL_CHANGED intent indicates the
     *  {@link BluetoothHeadsetClientCall} object that has changed.
     */
    public static final java.lang.String EXTRA_CALL = "android.bluetooth.headsetclient.extra.CALL";

    /**
     * Extra for ACTION_LAST_VTAG intent.
     * <p>Value: <code>String</code> representing phone number
     * corresponding to last voice tag recorded on AG</p>
     */
    public static final java.lang.String EXTRA_NUMBER = "android.bluetooth.headsetclient.extra.NUMBER";

    /**
     * Extra for ACTION_RESULT intent that shows the result code of
     * last issued action.
     * <p>Possible results:
     * {@link #ACTION_RESULT_OK},
     * {@link #ACTION_RESULT_ERROR},
     * {@link #ACTION_RESULT_ERROR_NO_CARRIER},
     * {@link #ACTION_RESULT_ERROR_BUSY},
     * {@link #ACTION_RESULT_ERROR_NO_ANSWER},
     * {@link #ACTION_RESULT_ERROR_DELAYED},
     * {@link #ACTION_RESULT_ERROR_BLACKLISTED},
     * {@link #ACTION_RESULT_ERROR_CME}</p>
     */
    public static final java.lang.String EXTRA_RESULT_CODE = "android.bluetooth.headsetclient.extra.RESULT_CODE";

    /**
     * Extra for ACTION_RESULT intent that shows the extended result code of
     * last issued action.
     * <p>Value: <code>Integer</code> - error code.</p>
     */
    public static final java.lang.String EXTRA_CME_CODE = "android.bluetooth.headsetclient.extra.CME_CODE";

    /* Extras for AG_FEATURES, extras type is boolean */
    // TODO verify if all of those are actually useful
    /**
     * AG feature: three way calling.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_3WAY_CALLING = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_3WAY_CALLING";

    /**
     * AG feature: voice recognition.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_VOICE_RECOGNITION";

    /**
     * AG feature: fetching phone number for voice tagging procedure.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT";

    /**
     * AG feature: ability to reject incoming call.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_REJECT_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_REJECT_CALL";

    /**
     * AG feature: enhanced call handling (terminate specific call, private consultation).
     */
    public static final java.lang.String EXTRA_AG_FEATURE_ECC = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ECC";

    /**
     * AG feature: response and hold.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_RESPONSE_AND_HOLD = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RESPONSE_AND_HOLD";

    /**
     * AG call handling feature: accept held or waiting call in three way calling scenarios.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL";

    /**
     * AG call handling feature: release held or waiting call in three way calling scenarios.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL";

    /**
     * AG call handling feature: release active call and accept held or waiting call in three way
     * calling scenarios.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT";

    /**
     * AG call handling feature: merge two calls, held and active - multi party conference mode.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_MERGE = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE";

    /**
     * AG call handling feature: merge calls and disconnect from multi party
     * conversation leaving peers connected to each other.
     * Note that this feature needs to be supported by mobile network operator
     * as it requires connection and billing transfer.
     */
    public static final java.lang.String EXTRA_AG_FEATURE_MERGE_AND_DETACH = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE_AND_DETACH";

    /* Action result codes */
    public static final int ACTION_RESULT_OK = 0;

    public static final int ACTION_RESULT_ERROR = 1;

    public static final int ACTION_RESULT_ERROR_NO_CARRIER = 2;

    public static final int ACTION_RESULT_ERROR_BUSY = 3;

    public static final int ACTION_RESULT_ERROR_NO_ANSWER = 4;

    public static final int ACTION_RESULT_ERROR_DELAYED = 5;

    public static final int ACTION_RESULT_ERROR_BLACKLISTED = 6;

    public static final int ACTION_RESULT_ERROR_CME = 7;

    /* Detailed CME error codes */
    public static final int CME_PHONE_FAILURE = 0;

    public static final int CME_NO_CONNECTION_TO_PHONE = 1;

    public static final int CME_OPERATION_NOT_ALLOWED = 3;

    public static final int CME_OPERATION_NOT_SUPPORTED = 4;

    public static final int CME_PHSIM_PIN_REQUIRED = 5;

    public static final int CME_PHFSIM_PIN_REQUIRED = 6;

    public static final int CME_PHFSIM_PUK_REQUIRED = 7;

    public static final int CME_SIM_NOT_INSERTED = 10;

    public static final int CME_SIM_PIN_REQUIRED = 11;

    public static final int CME_SIM_PUK_REQUIRED = 12;

    public static final int CME_SIM_FAILURE = 13;

    public static final int CME_SIM_BUSY = 14;

    public static final int CME_SIM_WRONG = 15;

    public static final int CME_INCORRECT_PASSWORD = 16;

    public static final int CME_SIM_PIN2_REQUIRED = 17;

    public static final int CME_SIM_PUK2_REQUIRED = 18;

    public static final int CME_MEMORY_FULL = 20;

    public static final int CME_INVALID_INDEX = 21;

    public static final int CME_NOT_FOUND = 22;

    public static final int CME_MEMORY_FAILURE = 23;

    public static final int CME_TEXT_STRING_TOO_LONG = 24;

    public static final int CME_INVALID_CHARACTER_IN_TEXT_STRING = 25;

    public static final int CME_DIAL_STRING_TOO_LONG = 26;

    public static final int CME_INVALID_CHARACTER_IN_DIAL_STRING = 27;

    public static final int CME_NO_NETWORK_SERVICE = 30;

    public static final int CME_NETWORK_TIMEOUT = 31;

    public static final int CME_EMERGENCY_SERVICE_ONLY = 32;

    public static final int CME_NO_SIMULTANOUS_VOIP_CS_CALLS = 33;

    public static final int CME_NOT_SUPPORTED_FOR_VOIP = 34;

    public static final int CME_SIP_RESPONSE_CODE = 35;

    public static final int CME_NETWORK_PERSONALIZATION_PIN_REQUIRED = 40;

    public static final int CME_NETWORK_PERSONALIZATION_PUK_REQUIRED = 41;

    public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PIN_REQUIRED = 42;

    public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PUK_REQUIRED = 43;

    public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PIN_REQUIRED = 44;

    public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PUK_REQUIRED = 45;

    public static final int CME_CORPORATE_PERSONALIZATION_PIN_REQUIRED = 46;

    public static final int CME_CORPORATE_PERSONALIZATION_PUK_REQUIRED = 47;

    public static final int CME_HIDDEN_KEY_REQUIRED = 48;

    public static final int CME_EAP_NOT_SUPPORTED = 49;

    public static final int CME_INCORRECT_PARAMETERS = 50;

    /* Action policy for other calls when accepting call */
    public static final int CALL_ACCEPT_NONE = 0;

    public static final int CALL_ACCEPT_HOLD = 1;

    public static final int CALL_ACCEPT_TERMINATE = 2;

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.IBluetoothHeadsetClient mService;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        @java.lang.Override
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothHeadsetClient.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothHeadsetClient.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, "Binding service...");

                            android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothHeadsetClient.class.getName());
                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothHeadsetClient proxy object.
     */
    /* package */
    BluetoothHeadsetClient(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothHeadsetClient.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "Could not bind to Bluetooth Headset Client Service with " + intent);
            return false;
        }
        return true;
    }

    /**
     * Close the connection to the backing service.
     * Other public functions of BluetoothHeadsetClient will return default error
     * results once close() has been called. Multiple invocations of close()
     * are ok.
     */
    /* package */
    void close() {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("close()");

        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    /**
     * Connects to remote device.
     *
     * Currently, the system supports only 1 connection. So, in case of the
     * second connection, this implementation will disconnect already connected
     * device automatically and will process the new one.
     *
     * @param device
     * 		a remote device we want connect to
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CONNECTION_STATE_CHANGED}
    intent.
     */
    public boolean connect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log(("connect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.connect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Disconnects remote device
     *
     * @param device
     * 		a remote device we want disconnect
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CONNECTION_STATE_CHANGED}
    intent.
     */
    public boolean disconnect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log(("disconnect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.disconnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Return the list of connected remote devices
     *
     * @return list of connected devices; empty list if nothing is connected.
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("getConnectedDevices()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * Returns list of remote devices in a particular state
     *
     * @param states
     * 		collection of states
     * @return list of devices that state matches the states listed in
    <code>states</code>; empty list if nothing matches the
    <code>states</code>
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("getDevicesMatchingStates()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * Returns state of the <code>device</code>
     *
     * @param device
     * 		a remote device
     * @return the state of connection of the device
     */
    @java.lang.Override
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log(("getConnectionState(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Set priority of the profile
     *
     * The device should already be paired.
     */
    public boolean setPriority(android.bluetooth.BluetoothDevice device, int priority) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log(((("setPriority(" + device) + ", ") + priority) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            if ((priority != android.bluetooth.BluetoothProfile.PRIORITY_OFF) && (priority != android.bluetooth.BluetoothProfile.PRIORITY_ON)) {
                return false;
            }
            try {
                return mService.setPriority(device, priority);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Get the priority of the profile.
     */
    public int getPriority(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log(("getPriority(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getPriority(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
    }

    /**
     * Starts voice recognition.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_AG_EVENT}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_VOICE_RECOGNITION}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean startVoiceRecognition(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("startVoiceRecognition()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.startVoiceRecognition(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Stops voice recognition.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_AG_EVENT}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_VOICE_RECOGNITION}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean stopVoiceRecognition(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("stopVoiceRecognition()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.stopVoiceRecognition(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Returns list of all calls in any state.
     *
     * @param device
     * 		remote device
     * @return list of calls; empty list if none call exists
     */
    public java.util.List<android.bluetooth.BluetoothHeadsetClientCall> getCurrentCalls(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("getCurrentCalls()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getCurrentCalls(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return null;
    }

    /**
     * Returns list of current values of AG indicators.
     *
     * @param device
     * 		remote device
     * @return bundle of AG  indicators; null if device is not in
    CONNECTED state
     */
    public android.os.Bundle getCurrentAgEvents(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("getCurrentCalls()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getCurrentAgEvents(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return null;
    }

    /**
     * Accepts a call
     *
     * @param device
     * 		remote device
     * @param flag
     * 		action policy while accepting a call. Possible values
     * 		{@link #CALL_ACCEPT_NONE}, {@link #CALL_ACCEPT_HOLD},
     * 		{@link #CALL_ACCEPT_TERMINATE}
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.
     */
    public boolean acceptCall(android.bluetooth.BluetoothDevice device, int flag) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("acceptCall()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.acceptCall(device, flag);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Holds a call.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.
     */
    public boolean holdCall(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("holdCall()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.holdCall(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Rejects a call.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_REJECT_CALL}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean rejectCall(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("rejectCall()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.rejectCall(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Terminates a specified call.
     *
     * Works only when Extended Call Control is supported by Audio Gateway.
     *
     * @param device
     * 		remote device
     * @param index
     * 		index of the call to be terminated
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_ECC}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean terminateCall(android.bluetooth.BluetoothDevice device, int index) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("terminateCall()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.terminateCall(device, index);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Enters private mode with a specified call.
     *
     * Works only when Extended Call Control is supported by Audio Gateway.
     *
     * @param device
     * 		remote device
     * @param index
     * 		index of the call to connect in private mode
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_ECC}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean enterPrivateMode(android.bluetooth.BluetoothDevice device, int index) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("enterPrivateMode()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.enterPrivateMode(device, index);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Performs explicit call transfer.
     *
     * That means connect other calls and disconnect.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent.

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_MERGE_AND_DETACH}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean explicitCallTransfer(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("explicitCallTransfer()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.explicitCallTransfer(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Redials last number from Audio Gateway.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent in case of success; {@link #ACTION_RESULT} is sent
    otherwise;
     */
    public boolean redial(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("redial()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.redial(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Places a call with specified number.
     *
     * @param device
     * 		remote device
     * @param number
     * 		valid phone number
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent in case of success; {@link #ACTION_RESULT} is sent
    otherwise;
     */
    public boolean dial(android.bluetooth.BluetoothDevice device, java.lang.String number) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("dial()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.dial(device, number);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Places a call to the number under specified memory location.
     *
     * @param device
     * 		remote device
     * @param location
     * 		valid memory location
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_CALL_CHANGED}
    intent in case of success; {@link #ACTION_RESULT} is sent
    otherwise;
     */
    public boolean dialMemory(android.bluetooth.BluetoothDevice device, int location) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("dialMemory()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.dialMemory(device, location);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Sends DTMF code.
     *
     * Possible code values : 0,1,2,3,4,5,6,7,8,9,A,B,C,D,*,#
     *
     * @param device
     * 		remote device
     * @param code
     * 		ASCII code
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_RESULT} intent;
     */
    public boolean sendDTMF(android.bluetooth.BluetoothDevice device, byte code) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("sendDTMF()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.sendDTMF(device, code);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Get a number corresponding to last voice tag recorded on AG.
     *
     * @param device
     * 		remote device
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_LAST_VTAG}
    or {@link #ACTION_RESULT} intent;

    <p>Feature required for successful execution is being reported by:
    {@link #EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT}.
    This method invocation will fail silently when feature is not supported.</p>
     */
    public boolean getLastVoiceTagNumber(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("getLastVoiceTagNumber()");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getLastVoiceTagNumber(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Accept the incoming connection.
     */
    public boolean acceptIncomingConnect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("acceptIncomingConnect");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.acceptIncomingConnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Reject the incoming connection.
     */
    public boolean rejectIncomingConnect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.DBG)
            android.bluetooth.BluetoothHeadsetClient.log("rejectIncomingConnect");

        if (mService != null) {
            try {
                return mService.rejectIncomingConnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Returns current audio state of Audio Gateway.
     *
     * Note: This is an internal function and shouldn't be exposed
     */
    public int getAudioState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("getAudioState");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getAudioState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return android.bluetooth.BluetoothHeadsetClient.STATE_AUDIO_DISCONNECTED;
    }

    /**
     * Sets whether audio routing is allowed.
     *
     * Note: This is an internal function and shouldn't be exposed
     */
    public void setAudioRouteAllowed(boolean allowed) {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("setAudioRouteAllowed");

        if ((mService != null) && isEnabled()) {
            try {
                mService.setAudioRouteAllowed(allowed);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
    }

    /**
     * Returns whether audio routing is allowed.
     *
     * Note: This is an internal function and shouldn't be exposed
     */
    public boolean getAudioRouteAllowed() {
        if (android.bluetooth.BluetoothHeadsetClient.VDBG)
            android.bluetooth.BluetoothHeadsetClient.log("getAudioRouteAllowed");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getAudioRouteAllowed();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Initiates a connection of audio channel.
     *
     * It setup SCO channel with remote connected Handsfree AG device.
     *
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_AUDIO_STATE_CHANGED}
    intent;
     */
    public boolean connectAudio() {
        if ((mService != null) && isEnabled()) {
            try {
                return mService.connectAudio();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Disconnects audio channel.
     *
     * It tears down the SCO channel from remote AG device.
     *
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
    upon completion HFP sends {@link #ACTION_AUDIO_STATE_CHANGED}
    intent;
     */
    public boolean disconnectAudio() {
        if ((mService != null) && isEnabled()) {
            try {
                return mService.disconnectAudio();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Get Audio Gateway features
     *
     * @param device
     * 		remote device
     * @return bundle of AG features; null if no service or
    AG not connected
     */
    public android.os.Bundle getCurrentAgFeatures(android.bluetooth.BluetoothDevice device) {
        if ((mService != null) && isEnabled()) {
            try {
                return mService.getCurrentAgFeatures(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHeadsetClient.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return null;
    }

    private android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy object connected");

            mService = IBluetoothHeadsetClient.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.HEADSET_CLIENT, android.bluetooth.BluetoothHeadsetClient.this);
            }
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothHeadsetClient.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, "Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.HEADSET_CLIENT);
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
        android.util.Log.d(android.bluetooth.BluetoothHeadsetClient.TAG, msg);
    }
}

