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
package android.telephony;


/**
 * A listener class for monitoring changes in specific telephony states
 * on the device, including service state, signal strength, message
 * waiting indicator (voicemail), and others.
 * <p>
 * Override the methods for the state that you wish to receive updates for, and
 * pass your PhoneStateListener object, along with bitwise-or of the LISTEN_
 * flags to {@link TelephonyManager#listen TelephonyManager.listen()}.
 * <p>
 * Note that access to some telephony information is
 * permission-protected. Your application won't receive updates for protected
 * information unless it has the appropriate permissions declared in
 * its manifest file. Where permissions apply, they are noted in the
 * appropriate LISTEN_ flags.
 */
public class PhoneStateListener {
    private static final java.lang.String LOG_TAG = "PhoneStateListener";

    private static final boolean DBG = false;// STOPSHIP if true


    /**
     * Stop listening for updates.
     */
    public static final int LISTEN_NONE = 0;

    /**
     * Listen for changes to the network service state (cellular).
     *
     * @see #onServiceStateChanged
     * @see ServiceState
     */
    public static final int LISTEN_SERVICE_STATE = 0x1;

    /**
     * Listen for changes to the network signal strength (cellular).
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
     * <p>
     *
     * @see #onSignalStrengthChanged
     * @deprecated by {@link #LISTEN_SIGNAL_STRENGTHS}
     */
    @java.lang.Deprecated
    public static final int LISTEN_SIGNAL_STRENGTH = 0x2;

    /**
     * Listen for changes to the message-waiting indicator.
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
     * <p>
     * Example: The status bar uses this to determine when to display the
     * voicemail icon.
     *
     * @see #onMessageWaitingIndicatorChanged
     */
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 0x4;

    /**
     * Listen for changes to the call-forwarding indicator.
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE
     * READ_PHONE_STATE}
     *
     * @see #onCallForwardingIndicatorChanged
     */
    public static final int LISTEN_CALL_FORWARDING_INDICATOR = 0x8;

    /**
     * Listen for changes to the device's cell location. Note that
     * this will result in frequent callbacks to the listener.
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#ACCESS_COARSE_LOCATION
     * ACCESS_COARSE_LOCATION}
     * <p>
     * If you need regular location updates but want more control over
     * the update interval or location precision, you can set up a listener
     * through the {@link android.location.LocationManager location manager}
     * instead.
     *
     * @see #onCellLocationChanged
     */
    public static final int LISTEN_CELL_LOCATION = 0x10;

    /**
     * Listen for changes to the device call state.
     * {@more }
     *
     * @see #onCallStateChanged
     */
    public static final int LISTEN_CALL_STATE = 0x20;

    /**
     * Listen for changes to the data connection state (cellular).
     *
     * @see #onDataConnectionStateChanged
     */
    public static final int LISTEN_DATA_CONNECTION_STATE = 0x40;

    /**
     * Listen for changes to the direction of data traffic on the data
     * connection (cellular).
     * {@more }
     * Example: The status bar uses this to display the appropriate
     * data-traffic icon.
     *
     * @see #onDataActivity
     */
    public static final int LISTEN_DATA_ACTIVITY = 0x80;

    /**
     * Listen for changes to the network signal strengths (cellular).
     * <p>
     * Example: The status bar uses this to control the signal-strength
     * icon.
     *
     * @see #onSignalStrengthsChanged
     */
    public static final int LISTEN_SIGNAL_STRENGTHS = 0x100;

    /**
     * Listen for changes to OTASP mode.
     *
     * @see #onOtaspChanged
     * @unknown 
     */
    public static final int LISTEN_OTASP_CHANGED = 0x200;

    /**
     * Listen for changes to observed cell info.
     *
     * @see #onCellInfoChanged
     */
    public static final int LISTEN_CELL_INFO = 0x400;

    /**
     * Listen for precise changes and fails to the device calls (cellular).
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PRECISE_PHONE_STATE
     * READ_PRECISE_PHONE_STATE}
     *
     * @unknown 
     */
    public static final int LISTEN_PRECISE_CALL_STATE = 0x800;

    /**
     * Listen for precise changes and fails on the data connection (cellular).
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PRECISE_PHONE_STATE
     * READ_PRECISE_PHONE_STATE}
     *
     * @see #onPreciseDataConnectionStateChanged
     * @unknown 
     */
    public static final int LISTEN_PRECISE_DATA_CONNECTION_STATE = 0x1000;

    /**
     * Listen for real time info for all data connections (cellular)).
     * {@more }
     * Requires Permission: {@link android.Manifest.permission#READ_PRECISE_PHONE_STATE
     * READ_PRECISE_PHONE_STATE}
     *
     * @see #onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo)
     * @deprecated Use {@link TelephonyManager#getModemActivityInfo()}
     * @unknown 
     */
    @java.lang.Deprecated
    public static final int LISTEN_DATA_CONNECTION_REAL_TIME_INFO = 0x2000;

    /**
     * Listen for changes to LTE network state
     *
     * @see #onLteNetworkStateChanged
     * @unknown 
     */
    public static final int LISTEN_VOLTE_STATE = 0x4000;

    /**
     * Listen for OEM hook raw event
     *
     * @see #onOemHookRawEvent
     * @unknown 
     */
    public static final int LISTEN_OEM_HOOK_RAW_EVENT = 0x8000;

    /**
     * Listen for carrier network changes indicated by a carrier app.
     *
     * @see #onCarrierNetworkRequest
     * @see TelephonyManager#notifyCarrierNetworkChange(boolean)
     * @unknown 
     */
    public static final int LISTEN_CARRIER_NETWORK_CHANGE = 0x10000;

    /* Subscription used to listen to the phone state changes
    @hide
     */
    /**
     *
     *
     * @unknown 
     */
    protected int mSubId = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;

    private final android.os.Handler mHandler;

    /**
     * Create a PhoneStateListener for the Phone with the default subscription.
     * This class requires Looper.myLooper() not return null.
     */
    public PhoneStateListener() {
        this(android.telephony.SubscriptionManager.DEFAULT_SUBSCRIPTION_ID, android.os.Looper.myLooper());
    }

    /**
     * Create a PhoneStateListener for the Phone with the default subscription
     * using a particular non-null Looper.
     *
     * @unknown 
     */
    public PhoneStateListener(android.os.Looper looper) {
        this(android.telephony.SubscriptionManager.DEFAULT_SUBSCRIPTION_ID, looper);
    }

    /**
     * Create a PhoneStateListener for the Phone using the specified subscription.
     * This class requires Looper.myLooper() not return null. To supply your
     * own non-null Looper use PhoneStateListener(int subId, Looper looper) below.
     *
     * @unknown 
     */
    public PhoneStateListener(int subId) {
        this(subId, android.os.Looper.myLooper());
    }

    /**
     * Create a PhoneStateListener for the Phone using the specified subscription
     * and non-null Looper.
     *
     * @unknown 
     */
    public PhoneStateListener(int subId, android.os.Looper looper) {
        if (android.telephony.PhoneStateListener.DBG)
            log((("ctor: subId=" + subId) + " looper=") + looper);

        mSubId = subId;
        mHandler = new android.os.Handler(looper) {
            public void handleMessage(android.os.Message msg) {
                if (android.telephony.PhoneStateListener.DBG) {
                    log((((("mSubId=" + mSubId) + " what=0x") + java.lang.Integer.toHexString(msg.what)) + " msg=") + msg);
                }
                switch (msg.what) {
                    case android.telephony.PhoneStateListener.LISTEN_SERVICE_STATE :
                        android.telephony.PhoneStateListener.this.onServiceStateChanged(((android.telephony.ServiceState) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTH :
                        android.telephony.PhoneStateListener.this.onSignalStrengthChanged(msg.arg1);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR :
                        android.telephony.PhoneStateListener.this.onMessageWaitingIndicatorChanged(msg.arg1 != 0);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR :
                        android.telephony.PhoneStateListener.this.onCallForwardingIndicatorChanged(msg.arg1 != 0);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_CELL_LOCATION :
                        android.telephony.PhoneStateListener.this.onCellLocationChanged(((android.telephony.CellLocation) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_CALL_STATE :
                        android.telephony.PhoneStateListener.this.onCallStateChanged(msg.arg1, ((java.lang.String) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_STATE :
                        android.telephony.PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1, msg.arg2);
                        android.telephony.PhoneStateListener.this.onDataConnectionStateChanged(msg.arg1);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_DATA_ACTIVITY :
                        android.telephony.PhoneStateListener.this.onDataActivity(msg.arg1);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTHS :
                        android.telephony.PhoneStateListener.this.onSignalStrengthsChanged(((android.telephony.SignalStrength) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_OTASP_CHANGED :
                        android.telephony.PhoneStateListener.this.onOtaspChanged(msg.arg1);
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_CELL_INFO :
                        android.telephony.PhoneStateListener.this.onCellInfoChanged(((java.util.List<android.telephony.CellInfo>) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_PRECISE_CALL_STATE :
                        android.telephony.PhoneStateListener.this.onPreciseCallStateChanged(((android.telephony.PreciseCallState) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_PRECISE_DATA_CONNECTION_STATE :
                        android.telephony.PhoneStateListener.this.onPreciseDataConnectionStateChanged(((android.telephony.PreciseDataConnectionState) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_REAL_TIME_INFO :
                        android.telephony.PhoneStateListener.this.onDataConnectionRealTimeInfoChanged(((android.telephony.DataConnectionRealTimeInfo) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_VOLTE_STATE :
                        android.telephony.PhoneStateListener.this.onVoLteServiceStateChanged(((android.telephony.VoLteServiceState) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_OEM_HOOK_RAW_EVENT :
                        android.telephony.PhoneStateListener.this.onOemHookRawEvent(((byte[]) (msg.obj)));
                        break;
                    case android.telephony.PhoneStateListener.LISTEN_CARRIER_NETWORK_CHANGE :
                        android.telephony.PhoneStateListener.this.onCarrierNetworkChange(((boolean) (msg.obj)));
                        break;
                }
            }
        };
    }

    /**
     * Callback invoked when device service state changes.
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
     */
    public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
        // default implementation empty
    }

    /**
     * Callback invoked when network signal strength changes.
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
     * @deprecated Use {@link #onSignalStrengthsChanged(SignalStrength)}
     */
    @java.lang.Deprecated
    public void onSignalStrengthChanged(int asu) {
        // default implementation empty
    }

    /**
     * Callback invoked when the message-waiting indicator changes.
     */
    public void onMessageWaitingIndicatorChanged(boolean mwi) {
        // default implementation empty
    }

    /**
     * Callback invoked when the call-forwarding indicator changes.
     */
    public void onCallForwardingIndicatorChanged(boolean cfi) {
        // default implementation empty
    }

    /**
     * Callback invoked when device cell location changes.
     */
    public void onCellLocationChanged(android.telephony.CellLocation location) {
        // default implementation empty
    }

    /**
     * Callback invoked when device call state changes.
     *
     * @param state
     * 		call state
     * @param incomingNumber
     * 		incoming call phone number. If application does not have
     * 		{@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE} permission, an empty
     * 		string will be passed as an argument.
     * @see TelephonyManager#CALL_STATE_IDLE
     * @see TelephonyManager#CALL_STATE_RINGING
     * @see TelephonyManager#CALL_STATE_OFFHOOK
     */
    public void onCallStateChanged(int state, java.lang.String incomingNumber) {
        // default implementation empty
    }

    /**
     * Callback invoked when connection state changes.
     *
     * @see TelephonyManager#DATA_DISCONNECTED
     * @see TelephonyManager#DATA_CONNECTING
     * @see TelephonyManager#DATA_CONNECTED
     * @see TelephonyManager#DATA_SUSPENDED
     */
    public void onDataConnectionStateChanged(int state) {
        // default implementation empty
    }

    /**
     * same as above, but with the network type.  Both called.
     */
    public void onDataConnectionStateChanged(int state, int networkType) {
    }

    /**
     * Callback invoked when data activity state changes.
     *
     * @see TelephonyManager#DATA_ACTIVITY_NONE
     * @see TelephonyManager#DATA_ACTIVITY_IN
     * @see TelephonyManager#DATA_ACTIVITY_OUT
     * @see TelephonyManager#DATA_ACTIVITY_INOUT
     * @see TelephonyManager#DATA_ACTIVITY_DORMANT
     */
    public void onDataActivity(int direction) {
        // default implementation empty
    }

    /**
     * Callback invoked when network signal strengths changes.
     *
     * @see ServiceState#STATE_EMERGENCY_ONLY
     * @see ServiceState#STATE_IN_SERVICE
     * @see ServiceState#STATE_OUT_OF_SERVICE
     * @see ServiceState#STATE_POWER_OFF
     */
    public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
        // default implementation empty
    }

    /**
     * The Over The Air Service Provisioning (OTASP) has changed. Requires
     * the READ_PHONE_STATE permission.
     *
     * @param otaspMode
     * 		is integer <code>OTASP_UNKNOWN=1<code>
     * 		means the value is currently unknown and the system should wait until
     * 		<code>OTASP_NEEDED=2<code> or <code>OTASP_NOT_NEEDED=3<code> is received before
     * 		making the decision to perform OTASP or not.
     * @unknown 
     */
    public void onOtaspChanged(int otaspMode) {
        // default implementation empty
    }

    /**
     * Callback invoked when a observed cell info has changed,
     * or new cells have been added or removed.
     *
     * @param cellInfo
     * 		is the list of currently visible cells.
     */
    public void onCellInfoChanged(java.util.List<android.telephony.CellInfo> cellInfo) {
    }

    /**
     * Callback invoked when precise device call state changes.
     *
     * @unknown 
     */
    public void onPreciseCallStateChanged(android.telephony.PreciseCallState callState) {
        // default implementation empty
    }

    /**
     * Callback invoked when data connection state changes with precise information.
     *
     * @unknown 
     */
    public void onPreciseDataConnectionStateChanged(android.telephony.PreciseDataConnectionState dataConnectionState) {
        // default implementation empty
    }

    /**
     * Callback invoked when data connection state changes with precise information.
     *
     * @unknown 
     */
    public void onDataConnectionRealTimeInfoChanged(android.telephony.DataConnectionRealTimeInfo dcRtInfo) {
        // default implementation empty
    }

    /**
     * Callback invoked when the service state of LTE network
     * related to the VoLTE service has changed.
     *
     * @param stateInfo
     * 		is the current LTE network information
     * @unknown 
     */
    public void onVoLteServiceStateChanged(android.telephony.VoLteServiceState stateInfo) {
    }

    /**
     * Callback invoked when OEM hook raw event is received. Requires
     * the READ_PRIVILEGED_PHONE_STATE permission.
     *
     * @param rawData
     * 		is the byte array of the OEM hook raw data.
     * @unknown 
     */
    public void onOemHookRawEvent(byte[] rawData) {
        // default implementation empty
    }

    /**
     * Callback invoked when telephony has received notice from a carrier
     * app that a network action that could result in connectivity loss
     * has been requested by an app using
     * {@link android.telephony.TelephonyManager#notifyCarrierNetworkChange(boolean)}
     *
     * @param active
     * 		Whether the carrier network change is or shortly
     * 		will be active. This value is true to indicate
     * 		showing alternative UI and false to stop.
     * @unknown 
     */
    public void onCarrierNetworkChange(boolean active) {
        // default implementation empty
    }

    /**
     * The callback methods need to be called on the handler thread where
     * this object was created.  If the binder did that for us it'd be nice.
     *
     * Using a static class and weak reference here to avoid memory leak caused by the
     * IPhoneStateListener.Stub callback retaining references to the outside PhoneStateListeners:
     * even caller has been destroyed and "un-registered" the PhoneStateListener, it is still not
     * eligible for GC given the references coming from:
     * Native Stack --> PhoneStateListener --> Context (Activity).
     * memory of caller's context will be collected after GC from service side get triggered
     */
    private static class IPhoneStateListenerStub extends com.android.internal.telephony.IPhoneStateListener.Stub {
        private java.lang.ref.WeakReference<android.telephony.PhoneStateListener> mPhoneStateListenerWeakRef;

        public IPhoneStateListenerStub(android.telephony.PhoneStateListener phoneStateListener) {
            mPhoneStateListenerWeakRef = new java.lang.ref.WeakReference<android.telephony.PhoneStateListener>(phoneStateListener);
        }

        private void send(int what, int arg1, int arg2, java.lang.Object obj) {
            android.telephony.PhoneStateListener listener = mPhoneStateListenerWeakRef.get();
            if (listener != null) {
                android.os.Message.obtain(listener.mHandler, what, arg1, arg2, obj).sendToTarget();
            }
        }

        public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
            send(android.telephony.PhoneStateListener.LISTEN_SERVICE_STATE, 0, 0, serviceState);
        }

        public void onSignalStrengthChanged(int asu) {
            send(android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTH, asu, 0, null);
        }

        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            send(android.telephony.PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR, mwi ? 1 : 0, 0, null);
        }

        public void onCallForwardingIndicatorChanged(boolean cfi) {
            send(android.telephony.PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR, cfi ? 1 : 0, 0, null);
        }

        public void onCellLocationChanged(android.os.Bundle bundle) {
            android.telephony.CellLocation location = android.telephony.CellLocation.newFromBundle(bundle);
            send(android.telephony.PhoneStateListener.LISTEN_CELL_LOCATION, 0, 0, location);
        }

        public void onCallStateChanged(int state, java.lang.String incomingNumber) {
            send(android.telephony.PhoneStateListener.LISTEN_CALL_STATE, state, 0, incomingNumber);
        }

        public void onDataConnectionStateChanged(int state, int networkType) {
            send(android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_STATE, state, networkType, null);
        }

        public void onDataActivity(int direction) {
            send(android.telephony.PhoneStateListener.LISTEN_DATA_ACTIVITY, direction, 0, null);
        }

        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            send(android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTHS, 0, 0, signalStrength);
        }

        public void onOtaspChanged(int otaspMode) {
            send(android.telephony.PhoneStateListener.LISTEN_OTASP_CHANGED, otaspMode, 0, null);
        }

        public void onCellInfoChanged(java.util.List<android.telephony.CellInfo> cellInfo) {
            send(android.telephony.PhoneStateListener.LISTEN_CELL_INFO, 0, 0, cellInfo);
        }

        public void onPreciseCallStateChanged(android.telephony.PreciseCallState callState) {
            send(android.telephony.PhoneStateListener.LISTEN_PRECISE_CALL_STATE, 0, 0, callState);
        }

        public void onPreciseDataConnectionStateChanged(android.telephony.PreciseDataConnectionState dataConnectionState) {
            send(android.telephony.PhoneStateListener.LISTEN_PRECISE_DATA_CONNECTION_STATE, 0, 0, dataConnectionState);
        }

        public void onDataConnectionRealTimeInfoChanged(android.telephony.DataConnectionRealTimeInfo dcRtInfo) {
            send(android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_REAL_TIME_INFO, 0, 0, dcRtInfo);
        }

        public void onVoLteServiceStateChanged(android.telephony.VoLteServiceState lteState) {
            send(android.telephony.PhoneStateListener.LISTEN_VOLTE_STATE, 0, 0, lteState);
        }

        public void onOemHookRawEvent(byte[] rawData) {
            send(android.telephony.PhoneStateListener.LISTEN_OEM_HOOK_RAW_EVENT, 0, 0, rawData);
        }

        public void onCarrierNetworkChange(boolean active) {
            send(android.telephony.PhoneStateListener.LISTEN_CARRIER_NETWORK_CHANGE, 0, 0, active);
        }
    }

    com.android.internal.telephony.IPhoneStateListener callback = new android.telephony.PhoneStateListener.IPhoneStateListenerStub(this);

    private void log(java.lang.String s) {
        android.telephony.Rlog.d(android.telephony.PhoneStateListener.LOG_TAG, s);
    }
}

