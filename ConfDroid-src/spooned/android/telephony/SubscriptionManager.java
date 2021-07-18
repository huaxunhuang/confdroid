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
package android.telephony;


/**
 * SubscriptionManager is the application interface to SubscriptionController
 * and provides information about the current Telephony Subscriptions.
 * * <p>
 * You do not instantiate this class directly; instead, you retrieve
 * a reference to an instance through {@link #from}.
 * <p>
 * All SDK public methods require android.Manifest.permission.READ_PHONE_STATE.
 */
public class SubscriptionManager {
    private static final java.lang.String LOG_TAG = "SubscriptionManager";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;

    /**
     * An invalid subscription identifier
     */
    public static final int INVALID_SUBSCRIPTION_ID = -1;

    /**
     * Base value for Dummy SUBSCRIPTION_ID's.
     */
    /**
     * FIXME: Remove DummySubId's, but for now have them map just below INVALID_SUBSCRIPTION_ID
     * /** @hide
     */
    public static final int DUMMY_SUBSCRIPTION_ID_BASE = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID - 1;

    /**
     * An invalid phone identifier
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int INVALID_PHONE_INDEX = -1;

    /**
     * An invalid slot identifier
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int INVALID_SIM_SLOT_INDEX = -1;

    /**
     * Indicates the caller wants the default sub id.
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int DEFAULT_SUBSCRIPTION_ID = java.lang.Integer.MAX_VALUE;

    /**
     * Indicates the caller wants the default phone id.
     * Used in SubscriptionController and Phone but do we really need it???
     *
     * @unknown 
     */
    public static final int DEFAULT_PHONE_INDEX = java.lang.Integer.MAX_VALUE;

    /**
     * Indicates the caller wants the default slot id. NOT used remove?
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int DEFAULT_SIM_SLOT_INDEX = java.lang.Integer.MAX_VALUE;

    /**
     * Minimum possible subid that represents a subscription
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int MIN_SUBSCRIPTION_ID_VALUE = 0;

    /**
     * Maximum possible subid that represents a subscription
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int MAX_SUBSCRIPTION_ID_VALUE = android.telephony.SubscriptionManager.DEFAULT_SUBSCRIPTION_ID - 1;

    /**
     *
     *
     * @unknown 
     */
    public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://telephony/siminfo");

    /**
     * TelephonyProvider unique key column name is the subscription id.
     * <P>Type: TEXT (String)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String UNIQUE_KEY_SUBSCRIPTION_ID = "_id";

    /**
     * TelephonyProvider column name for SIM ICC Identifier
     * <P>Type: TEXT (String)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String ICC_ID = "icc_id";

    /**
     * TelephonyProvider column name for user SIM_SlOT_INDEX
     * <P>Type: INTEGER (int)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String SIM_SLOT_INDEX = "sim_id";

    /**
     * SIM is not inserted
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int SIM_NOT_INSERTED = -1;

    /**
     * TelephonyProvider column name for user displayed name.
     * <P>Type: TEXT (String)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String DISPLAY_NAME = "display_name";

    /**
     * TelephonyProvider column name for the service provider name for the SIM.
     * <P>Type: TEXT (String)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CARRIER_NAME = "carrier_name";

    /**
     * Default name resource
     *
     * @unknown 
     */
    public static final int DEFAULT_NAME_RES = com.android.internal.R.string.unknownName;

    /**
     * TelephonyProvider column name for source of the user displayed name.
     * <P>Type: INT (int)</P> with one of the NAME_SOURCE_XXXX values below
     *
     * @unknown 
     */
    public static final java.lang.String NAME_SOURCE = "name_source";

    /**
     * The name_source is undefined
     *
     * @unknown 
     */
    public static final int NAME_SOURCE_UNDEFINDED = -1;

    /**
     * The name_source is the default
     *
     * @unknown 
     */
    public static final int NAME_SOURCE_DEFAULT_SOURCE = 0;

    /**
     * The name_source is from the SIM
     *
     * @unknown 
     */
    public static final int NAME_SOURCE_SIM_SOURCE = 1;

    /**
     * The name_source is from the user
     *
     * @unknown 
     */
    public static final int NAME_SOURCE_USER_INPUT = 2;

    /**
     * TelephonyProvider column name for the color of a SIM.
     * <P>Type: INTEGER (int)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String COLOR = "color";

    /**
     *
     *
     * @unknown 
     */
    public static final int COLOR_1 = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int COLOR_2 = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int COLOR_3 = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int COLOR_4 = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int COLOR_DEFAULT = android.telephony.SubscriptionManager.COLOR_1;

    /**
     * TelephonyProvider column name for the phone number of a SIM.
     * <P>Type: TEXT (String)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String NUMBER = "number";

    /**
     * TelephonyProvider column name for the number display format of a SIM.
     * <P>Type: INTEGER (int)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String DISPLAY_NUMBER_FORMAT = "display_number_format";

    /**
     *
     *
     * @unknown 
     */
    public static final int DISPLAY_NUMBER_NONE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISPLAY_NUMBER_FIRST = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISPLAY_NUMBER_LAST = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int DISPLAY_NUMBER_DEFAULT = android.telephony.SubscriptionManager.DISPLAY_NUMBER_FIRST;

    /**
     * TelephonyProvider column name for permission for data roaming of a SIM.
     * <P>Type: INTEGER (int)</P>
     */
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String DATA_ROAMING = "data_roaming";

    /**
     * Indicates that data roaming is enabled for a subscription
     */
    public static final int DATA_ROAMING_ENABLE = 1;

    /**
     * Indicates that data roaming is disabled for a subscription
     */
    public static final int DATA_ROAMING_DISABLE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int DATA_ROAMING_DEFAULT = android.telephony.SubscriptionManager.DATA_ROAMING_DISABLE;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIM_PROVISIONED = 0;

    /**
     * TelephonyProvider column name for the MCC associated with a SIM.
     * <P>Type: INTEGER (int)</P>
     *
     * @unknown 
     */
    public static final java.lang.String MCC = "mcc";

    /**
     * TelephonyProvider column name for the MNC associated with a SIM.
     * <P>Type: INTEGER (int)</P>
     *
     * @unknown 
     */
    public static final java.lang.String MNC = "mnc";

    /**
     * TelephonyProvider column name for the sim provisioning status associated with a SIM.
     * <P>Type: INTEGER (int)</P>
     *
     * @unknown 
     */
    public static final java.lang.String SIM_PROVISIONING_STATUS = "sim_provisioning_status";

    /**
     * TelephonyProvider column name for extreme threat in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_EXTREME_THREAT_ALERT = "enable_cmas_extreme_threat_alerts";

    /**
     * TelephonyProvider column name for severe threat in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_SEVERE_THREAT_ALERT = "enable_cmas_severe_threat_alerts";

    /**
     * TelephonyProvider column name for amber alert in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_AMBER_ALERT = "enable_cmas_amber_alerts";

    /**
     * TelephonyProvider column name for emergency alert in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_EMERGENCY_ALERT = "enable_emergency_alerts";

    /**
     * TelephonyProvider column name for alert sound duration in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_ALERT_SOUND_DURATION = "alert_sound_duration";

    /**
     * TelephonyProvider column name for alert reminder interval in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_ALERT_REMINDER_INTERVAL = "alert_reminder_interval";

    /**
     * TelephonyProvider column name for enabling vibrate in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_ALERT_VIBRATE = "enable_alert_vibrate";

    /**
     * TelephonyProvider column name for enabling alert speech in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_ALERT_SPEECH = "enable_alert_speech";

    /**
     * TelephonyProvider column name for ETWS test alert in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_ETWS_TEST_ALERT = "enable_etws_test_alerts";

    /**
     * TelephonyProvider column name for enable channel50 alert in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_CHANNEL_50_ALERT = "enable_channel_50_alerts";

    /**
     * TelephonyProvider column name for CMAS test alert in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_CMAS_TEST_ALERT = "enable_cmas_test_alerts";

    /**
     * TelephonyProvider column name for Opt out dialog in CB settings
     *
     * @unknown 
     */
    public static final java.lang.String CB_OPT_OUT_DIALOG = "show_cmas_opt_out_dialog";

    /**
     * Broadcast Action: The user has changed one of the default subs related to
     * data, phone calls, or sms</p>
     *
     * TODO: Change to a listener
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String SUB_DEFAULT_CHANGED_ACTION = "android.intent.action.SUB_DEFAULT_CHANGED";

    private final android.content.Context mContext;

    /**
     * A listener class for monitoring changes to {@link SubscriptionInfo} records.
     * <p>
     * Override the onSubscriptionsChanged method in the object that extends this
     * class and pass it to {@link #addOnSubscriptionsChangedListener(OnSubscriptionsChangedListener)}
     * to register your listener and to unregister invoke
     * {@link #removeOnSubscriptionsChangedListener(OnSubscriptionsChangedListener)}
     * <p>
     * Permissions android.Manifest.permission.READ_PHONE_STATE is required
     * for #onSubscriptionsChanged to be invoked.
     */
    public static class OnSubscriptionsChangedListener {
        private final android.os.Handler mHandler = new android.os.Handler() {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (android.telephony.SubscriptionManager.DBG) {
                    log("handleMessage: invoke the overriden onSubscriptionsChanged()");
                }
                android.telephony.SubscriptionManager.OnSubscriptionsChangedListener.this.onSubscriptionsChanged();
            }
        };

        /**
         * Callback invoked when there is any change to any SubscriptionInfo. Typically
         * this method would invoke {@link #getActiveSubscriptionInfoList}
         */
        public void onSubscriptionsChanged() {
            if (android.telephony.SubscriptionManager.DBG)
                log("onSubscriptionsChanged: NOT OVERRIDDEN");

        }

        /**
         * The callback methods need to be called on the handler thread where
         * this object was created.  If the binder did that for us it'd be nice.
         */
        com.android.internal.telephony.IOnSubscriptionsChangedListener callback = new com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub() {
            @java.lang.Override
            public void onSubscriptionsChanged() {
                if (android.telephony.SubscriptionManager.DBG)
                    log("callback: received, sendEmptyMessage(0) to handler");

                mHandler.sendEmptyMessage(0);
            }
        };

        private void log(java.lang.String s) {
            android.telephony.Rlog.d(android.telephony.SubscriptionManager.LOG_TAG, s);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public SubscriptionManager(android.content.Context context) {
        if (android.telephony.SubscriptionManager.DBG)
            android.telephony.SubscriptionManager.logd("SubscriptionManager created");

        mContext = context;
    }

    /**
     * Get an instance of the SubscriptionManager from the Context.
     * This invokes {@link android.content.Context#getSystemService
     * Context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE)}.
     *
     * @param context
     * 		to use.
     * @return SubscriptionManager instance
     */
    public static android.telephony.SubscriptionManager from(android.content.Context context) {
        return ((android.telephony.SubscriptionManager) (context.getSystemService(android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE)));
    }

    /**
     * Register for changes to the list of active {@link SubscriptionInfo} records or to the
     * individual records themselves. When a change occurs the onSubscriptionsChanged method of
     * the listener will be invoked immediately if there has been a notification.
     *
     * @param listener
     * 		an instance of {@link OnSubscriptionsChangedListener} with
     * 		onSubscriptionsChanged overridden.
     */
    public void addOnSubscriptionsChangedListener(android.telephony.SubscriptionManager.OnSubscriptionsChangedListener listener) {
        java.lang.String pkgForDebug = (mContext != null) ? mContext.getOpPackageName() : "<unknown>";
        if (android.telephony.SubscriptionManager.DBG) {
            android.telephony.SubscriptionManager.logd((("register OnSubscriptionsChangedListener pkgForDebug=" + pkgForDebug) + " listener=") + listener);
        }
        try {
            // We use the TelephonyRegistry as it runs in the system and thus is always
            // available. Where as SubscriptionController could crash and not be available
            com.android.internal.telephony.ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(android.os.ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.addOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (android.os.RemoteException ex) {
            // Should not happen
        }
    }

    /**
     * Unregister the {@link OnSubscriptionsChangedListener}. This is not strictly necessary
     * as the listener will automatically be unregistered if an attempt to invoke the listener
     * fails.
     *
     * @param listener
     * 		that is to be unregistered.
     */
    public void removeOnSubscriptionsChangedListener(android.telephony.SubscriptionManager.OnSubscriptionsChangedListener listener) {
        java.lang.String pkgForDebug = (mContext != null) ? mContext.getOpPackageName() : "<unknown>";
        if (android.telephony.SubscriptionManager.DBG) {
            android.telephony.SubscriptionManager.logd((("unregister OnSubscriptionsChangedListener pkgForDebug=" + pkgForDebug) + " listener=") + listener);
        }
        try {
            // We use the TelephonyRegistry as its runs in the system and thus is always
            // available where as SubscriptionController could crash and not be available
            com.android.internal.telephony.ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(android.os.ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (android.os.RemoteException ex) {
            // Should not happen
        }
    }

    /**
     * Get the active SubscriptionInfo with the input subId.
     *
     * @param subId
     * 		The unique SubscriptionInfo key in database.
     * @return SubscriptionInfo, maybe null if its not active.
     */
    public android.telephony.SubscriptionInfo getActiveSubscriptionInfo(int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfo]+ subId=" + subId);

        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            if (android.telephony.SubscriptionManager.DBG) {
                android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfo]- invalid subId");
            }
            return null;
        }
        android.telephony.SubscriptionInfo subInfo = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subInfo = iSub.getActiveSubscriptionInfo(subId, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return subInfo;
    }

    /**
     * Get the active SubscriptionInfo associated with the iccId
     *
     * @param iccId
     * 		the IccId of SIM card
     * @return SubscriptionInfo, maybe null if its not active
     * @unknown 
     */
    public android.telephony.SubscriptionInfo getActiveSubscriptionInfoForIccIndex(java.lang.String iccId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfoForIccIndex]+ iccId=" + iccId);

        if (iccId == null) {
            android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfoForIccIndex]- null iccid");
            return null;
        }
        android.telephony.SubscriptionInfo result = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubscriptionInfoForIccId(iccId, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Get the active SubscriptionInfo associated with the slotIdx
     *
     * @param slotIdx
     * 		the slot which the subscription is inserted
     * @return SubscriptionInfo, maybe null if its not active
     */
    public android.telephony.SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIdx) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfoForSimSlotIndex]+ slotIdx=" + slotIdx);

        if (!android.telephony.SubscriptionManager.isValidSlotId(slotIdx)) {
            android.telephony.SubscriptionManager.logd("[getActiveSubscriptionInfoForSimSlotIndex]- invalid slotIdx");
            return null;
        }
        android.telephony.SubscriptionInfo result = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubscriptionInfoForSimSlotIndex(slotIdx, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     *
     *
     * @return List of all SubscriptionInfo records in database,
    include those that were inserted before, maybe empty but not null.
     * @unknown 
     */
    public java.util.List<android.telephony.SubscriptionInfo> getAllSubscriptionInfoList() {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getAllSubscriptionInfoList]+");

        java.util.List<android.telephony.SubscriptionInfo> result = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoList(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (result == null) {
            result = new java.util.ArrayList<android.telephony.SubscriptionInfo>();
        }
        return result;
    }

    /**
     * Get the SubscriptionInfo(s) of the currently inserted SIM(s). The records will be sorted
     * by {@link SubscriptionInfo#getSimSlotIndex} then by {@link SubscriptionInfo#getSubscriptionId}.
     *
     * @return Sorted list of the currently {@link SubscriptionInfo} records available on the device.
    <ul>
    <li>
    If null is returned the current state is unknown but if a {@link OnSubscriptionsChangedListener}
    has been registered {@link OnSubscriptionsChangedListener#onSubscriptionsChanged} will be
    invoked in the future.
    </li>
    <li>
    If the list is empty then there are no {@link SubscriptionInfo} records currently available.
    </li>
    <li>
    if the list is non-empty the list is sorted by {@link SubscriptionInfo#getSimSlotIndex}
    then by {@link SubscriptionInfo#getSubscriptionId}.
    </li>
    </ul>
     */
    public java.util.List<android.telephony.SubscriptionInfo> getActiveSubscriptionInfoList() {
        java.util.List<android.telephony.SubscriptionInfo> result = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubscriptionInfoList(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     *
     *
     * @return the count of all subscriptions in the database, this includes
    all subscriptions that have been seen.
     * @unknown 
     */
    public int getAllSubscriptionInfoCount() {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getAllSubscriptionInfoCount]+");

        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoCount(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     *
     *
     * @return the current number of active subscriptions. There is no guarantee the value
    returned by this method will be the same as the length of the list returned by
    {@link #getActiveSubscriptionInfoList}.
     */
    public int getActiveSubscriptionInfoCount() {
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubInfoCount(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     *
     *
     * @return the maximum number of active subscriptions that will be returned by
    {@link #getActiveSubscriptionInfoList} and the value returned by
    {@link #getActiveSubscriptionInfoCount}.
     */
    public int getActiveSubscriptionInfoCountMax() {
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubInfoCountMax();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Add a new SubscriptionInfo to SubscriptionInfo database if needed
     *
     * @param iccId
     * 		the IccId of the SIM card
     * @param slotId
     * 		the slot which the SIM is inserted
     * @return the URL of the newly created row or the updated row
     * @unknown 
     */
    public android.net.Uri addSubscriptionInfoRecord(java.lang.String iccId, int slotId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd((("[addSubscriptionInfoRecord]+ iccId:" + iccId) + " slotId:") + slotId);

        if (iccId == null) {
            android.telephony.SubscriptionManager.logd("[addSubscriptionInfoRecord]- null iccId");
        }
        if (!android.telephony.SubscriptionManager.isValidSlotId(slotId)) {
            android.telephony.SubscriptionManager.logd("[addSubscriptionInfoRecord]- invalid slotId");
        }
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                // FIXME: This returns 1 on success, 0 on error should should we return it?
                iSub.addSubInfoRecord(iccId, slotId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        // FIXME: Always returns null?
        return null;
    }

    /**
     * Set SIM icon tint color by simInfo index
     *
     * @param tint
     * 		the RGB value of icon tint color of the SIM
     * @param subId
     * 		the unique SubInfoRecord index in database
     * @return the number of records updated
     * @unknown 
     */
    public int setIconTint(int tint, int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd((("[setIconTint]+ tint:" + tint) + " subId:") + subId);

        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            android.telephony.SubscriptionManager.logd("[setIconTint]- fail");
            return -1;
        }
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.setIconTint(tint, subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Set display name by simInfo index
     *
     * @param displayName
     * 		the display name of SIM card
     * @param subId
     * 		the unique SubscriptionInfo index in database
     * @return the number of records updated
     * @unknown 
     */
    public int setDisplayName(java.lang.String displayName, int subId) {
        return setDisplayName(displayName, subId, android.telephony.SubscriptionManager.NAME_SOURCE_UNDEFINDED);
    }

    /**
     * Set display name by simInfo index with name source
     *
     * @param displayName
     * 		the display name of SIM card
     * @param subId
     * 		the unique SubscriptionInfo index in database
     * @param nameSource
     * 		0: NAME_SOURCE_DEFAULT_SOURCE, 1: NAME_SOURCE_SIM_SOURCE,
     * 		2: NAME_SOURCE_USER_INPUT, -1 NAME_SOURCE_UNDEFINED
     * @return the number of records updated or < 0 if invalid subId
     * @unknown 
     */
    public int setDisplayName(java.lang.String displayName, int subId, long nameSource) {
        if (android.telephony.SubscriptionManager.VDBG) {
            android.telephony.SubscriptionManager.logd((((("[setDisplayName]+  displayName:" + displayName) + " subId:") + subId) + " nameSource:") + nameSource);
        }
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            android.telephony.SubscriptionManager.logd("[setDisplayName]- fail");
            return -1;
        }
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.setDisplayNameUsingSrc(displayName, subId, nameSource);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Set phone number by subId
     *
     * @param number
     * 		the phone number of the SIM
     * @param subId
     * 		the unique SubscriptionInfo index in database
     * @return the number of records updated
     * @unknown 
     */
    public int setDisplayNumber(java.lang.String number, int subId) {
        if ((number == null) || (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId))) {
            android.telephony.SubscriptionManager.logd("[setDisplayNumber]- fail");
            return -1;
        }
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.setDisplayNumber(number, subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Set data roaming by simInfo index
     *
     * @param roaming
     * 		0:Don't allow data when roaming, 1:Allow data when roaming
     * @param subId
     * 		the unique SubscriptionInfo index in database
     * @return the number of records updated
     * @unknown 
     */
    public int setDataRoaming(int roaming, int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd((("[setDataRoaming]+ roaming:" + roaming) + " subId:") + subId);

        if ((roaming < 0) || (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId))) {
            android.telephony.SubscriptionManager.logd("[setDataRoaming]- fail");
            return -1;
        }
        int result = 0;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.setDataRoaming(roaming, subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     * Get slotId associated with the subscription.
     *
     * @return slotId as a positive integer or a negative value if an error either
    SIM_NOT_INSERTED or < 0 if an invalid slot index
     * @unknown 
     */
    public static int getSlotId(int subId) {
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            if (android.telephony.SubscriptionManager.DBG) {
                android.telephony.SubscriptionManager.logd("[getSlotId]- fail");
            }
        }
        int result = android.telephony.SubscriptionManager.INVALID_SIM_SLOT_INDEX;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getSlotId(subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int[] getSubId(int slotId) {
        if (!android.telephony.SubscriptionManager.isValidSlotId(slotId)) {
            android.telephony.SubscriptionManager.logd("[getSubId]- fail");
            return null;
        }
        int[] subId = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getSubId(slotId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return subId;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getPhoneId(int subId) {
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            if (android.telephony.SubscriptionManager.DBG) {
                android.telephony.SubscriptionManager.logd("[getPhoneId]- fail");
            }
            return android.telephony.SubscriptionManager.INVALID_PHONE_INDEX;
        }
        int result = android.telephony.SubscriptionManager.INVALID_PHONE_INDEX;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getPhoneId(subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("[getPhoneId]- phoneId=" + result);

        return result;
    }

    private static void logd(java.lang.String msg) {
        android.telephony.Rlog.d(android.telephony.SubscriptionManager.LOG_TAG, msg);
    }

    /**
     * Returns the system's default subscription id.
     *
     * For a voice capable device, it will return getDefaultVoiceSubscriptionId.
     * For a data only device, it will return the getDefaultDataSubscriptionId.
     * May return an INVALID_SUBSCRIPTION_ID on error.
     *
     * @return the "system" default subscription id.
     */
    public static int getDefaultSubscriptionId() {
        int subId = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultSubId();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("getDefaultSubId=" + subId);

        return subId;
    }

    /**
     * Returns the system's default voice subscription id.
     *
     * On a data only device or on error, will return INVALID_SUBSCRIPTION_ID.
     *
     * @return the default voice subscription Id.
     */
    public static int getDefaultVoiceSubscriptionId() {
        int subId = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultVoiceSubId();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("getDefaultVoiceSubscriptionId, sub id = " + subId);

        return subId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDefaultVoiceSubId(int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("setDefaultVoiceSubId sub id = " + subId);

        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultVoiceSubId(subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     * Return the SubscriptionInfo for default voice subscription.
     *
     * Will return null on data only devices, or on error.
     *
     * @return the SubscriptionInfo for the default voice subscription.
     * @unknown 
     */
    public android.telephony.SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return getActiveSubscriptionInfo(android.telephony.SubscriptionManager.getDefaultVoiceSubscriptionId());
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getDefaultVoicePhoneId() {
        return android.telephony.SubscriptionManager.getPhoneId(android.telephony.SubscriptionManager.getDefaultVoiceSubscriptionId());
    }

    /**
     * Returns the system's default SMS subscription id.
     *
     * On a data only device or on error, will return INVALID_SUBSCRIPTION_ID.
     *
     * @return the default SMS subscription Id.
     */
    public static int getDefaultSmsSubscriptionId() {
        int subId = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultSmsSubId();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("getDefaultSmsSubscriptionId, sub id = " + subId);

        return subId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDefaultSmsSubId(int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("setDefaultSmsSubId sub id = " + subId);

        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultSmsSubId(subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     * Return the SubscriptionInfo for default voice subscription.
     *
     * Will return null on data only devices, or on error.
     *
     * @return the SubscriptionInfo for the default SMS subscription.
     * @unknown 
     */
    public android.telephony.SubscriptionInfo getDefaultSmsSubscriptionInfo() {
        return getActiveSubscriptionInfo(android.telephony.SubscriptionManager.getDefaultSmsSubscriptionId());
    }

    /**
     *
     *
     * @unknown 
     */
    public int getDefaultSmsPhoneId() {
        return android.telephony.SubscriptionManager.getPhoneId(android.telephony.SubscriptionManager.getDefaultSmsSubscriptionId());
    }

    /**
     * Returns the system's default data subscription id.
     *
     * On a voice only device or on error, will return INVALID_SUBSCRIPTION_ID.
     *
     * @return the default data subscription Id.
     */
    public static int getDefaultDataSubscriptionId() {
        int subId = android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getDefaultDataSubId();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("getDefaultDataSubscriptionId, sub id = " + subId);

        return subId;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDefaultDataSubId(int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("setDataSubscription sub id = " + subId);

        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultDataSubId(subId);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     * Return the SubscriptionInfo for default data subscription.
     *
     * Will return null on voice only devices, or on error.
     *
     * @return the SubscriptionInfo for the default data subscription.
     * @unknown 
     */
    public android.telephony.SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return getActiveSubscriptionInfo(android.telephony.SubscriptionManager.getDefaultDataSubscriptionId());
    }

    /**
     *
     *
     * @unknown 
     */
    public int getDefaultDataPhoneId() {
        return android.telephony.SubscriptionManager.getPhoneId(android.telephony.SubscriptionManager.getDefaultDataSubscriptionId());
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearSubscriptionInfo() {
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearSubInfo();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return;
    }

    // FIXME this is vulnerable to race conditions
    /**
     *
     *
     * @unknown 
     */
    public boolean allDefaultsSelected() {
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(android.telephony.SubscriptionManager.getDefaultDataSubscriptionId())) {
            return false;
        }
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(android.telephony.SubscriptionManager.getDefaultSmsSubscriptionId())) {
            return false;
        }
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(android.telephony.SubscriptionManager.getDefaultVoiceSubscriptionId())) {
            return false;
        }
        return true;
    }

    /**
     * If a default is set to subscription which is not active, this will reset that default back to
     * an invalid subscription id, i.e. < 0.
     *
     * @unknown 
     */
    public void clearDefaultsForInactiveSubIds() {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd("clearDefaultsForInactiveSubIds");

        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearDefaultsForInactiveSubIds();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     *
     *
     * @return true if a valid subId else false
     * @unknown 
     */
    public static boolean isValidSubscriptionId(int subId) {
        return subId > android.telephony.SubscriptionManager.INVALID_SUBSCRIPTION_ID;
    }

    /**
     *
     *
     * @return true if subId is an usable subId value else false. A
    usable subId means its neither a INVALID_SUBSCRIPTION_ID nor a DEFAULT_SUB_ID.
     * @unknown 
     */
    public static boolean isUsableSubIdValue(int subId) {
        return (subId >= android.telephony.SubscriptionManager.MIN_SUBSCRIPTION_ID_VALUE) && (subId <= android.telephony.SubscriptionManager.MAX_SUBSCRIPTION_ID_VALUE);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isValidSlotId(int slotId) {
        return (slotId >= 0) && (slotId < android.telephony.TelephonyManager.getDefault().getSimCount());
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isValidPhoneId(int phoneId) {
        return (phoneId >= 0) && (phoneId < android.telephony.TelephonyManager.getDefault().getPhoneCount());
    }

    /**
     *
     *
     * @unknown 
     */
    public static void putPhoneIdAndSubIdExtra(android.content.Intent intent, int phoneId) {
        int[] subIds = android.telephony.SubscriptionManager.getSubId(phoneId);
        if ((subIds != null) && (subIds.length > 0)) {
            android.telephony.SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phoneId, subIds[0]);
        } else {
            android.telephony.SubscriptionManager.logd("putPhoneIdAndSubIdExtra: no valid subs");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void putPhoneIdAndSubIdExtra(android.content.Intent intent, int phoneId, int subId) {
        if (android.telephony.SubscriptionManager.VDBG)
            android.telephony.SubscriptionManager.logd((("putPhoneIdAndSubIdExtra: phoneId=" + phoneId) + " subId=") + subId);

        intent.putExtra(PhoneConstants.SUBSCRIPTION_KEY, subId);
        intent.putExtra(PhoneConstants.PHONE_KEY, phoneId);
        // FIXME this is using phoneId and slotId interchangeably
        // Eventually, this should be removed as it is not the slot id
        intent.putExtra(PhoneConstants.SLOT_KEY, phoneId);
    }

    /**
     *
     *
     * @return the list of subId's that are active,
    is never null but the length maybe 0.
     * @unknown 
     */
    @android.annotation.NonNull
    public int[] getActiveSubscriptionIdList() {
        int[] subId = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getActiveSubIdList();
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        if (subId == null) {
            subId = new int[0];
        }
        return subId;
    }

    /**
     * Returns true if the device is considered roaming on the current
     * network for a subscription.
     * <p>
     * Availability: Only when user registered to a network.
     *
     * @param subId
     * 		The subscription ID
     * @return true if the network for the subscription is roaming, false otherwise
     */
    public boolean isNetworkRoaming(int subId) {
        final int phoneId = android.telephony.SubscriptionManager.getPhoneId(subId);
        if (phoneId < 0) {
            // What else can we do?
            return false;
        }
        return android.telephony.TelephonyManager.getDefault().isNetworkRoaming(subId);
    }

    /**
     * Returns a constant indicating the state of sim for the slot idx.
     *
     * @param slotIdx
     * 		{@See TelephonyManager#SIM_STATE_UNKNOWN}
     * 		{@See TelephonyManager#SIM_STATE_ABSENT}
     * 		{@See TelephonyManager#SIM_STATE_PIN_REQUIRED}
     * 		{@See TelephonyManager#SIM_STATE_PUK_REQUIRED}
     * 		{@See TelephonyManager#SIM_STATE_NETWORK_LOCKED}
     * 		{@See TelephonyManager#SIM_STATE_READY}
     * 		{@See TelephonyManager#SIM_STATE_NOT_READY}
     * 		{@See TelephonyManager#SIM_STATE_PERM_DISABLED}
     * 		{@See TelephonyManager#SIM_STATE_CARD_IO_ERROR}
     * 		
     * 		{@hide }
     */
    public static int getSimStateForSlotIdx(int slotIdx) {
        int simState = android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                simState = iSub.getSimStateForSlotIdx(slotIdx);
            }
        } catch (android.os.RemoteException ex) {
        }
        return simState;
    }

    /**
     * Store properties associated with SubscriptionInfo in database
     *
     * @param subId
     * 		Subscription Id of Subscription
     * @param propKey
     * 		Column name in database associated with SubscriptionInfo
     * @param propValue
     * 		Value to store in DB for particular subId & column name
     * @unknown 
     */
    public static void setSubscriptionProperty(int subId, java.lang.String propKey, java.lang.String propValue) {
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setSubscriptionProperty(subId, propKey, propValue);
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
    }

    /**
     * Store properties associated with SubscriptionInfo in database
     *
     * @param subId
     * 		Subscription Id of Subscription
     * @param propKey
     * 		Column name in SubscriptionInfo database
     * @return Value associated with subId and propKey column in database
     * @unknown 
     */
    private static java.lang.String getSubscriptionProperty(int subId, java.lang.String propKey, android.content.Context context) {
        java.lang.String resultValue = null;
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                resultValue = iSub.getSubscriptionProperty(subId, propKey, context.getOpPackageName());
            }
        } catch (android.os.RemoteException ex) {
            // ignore it
        }
        return resultValue;
    }

    /**
     * Returns boolean value corresponding to query result.
     *
     * @param subId
     * 		Subscription Id of Subscription
     * @param propKey
     * 		Column name in SubscriptionInfo database
     * @param defValue
     * 		Default boolean value to be returned
     * @return boolean result value to be returned
     * @unknown 
     */
    public static boolean getBooleanSubscriptionProperty(int subId, java.lang.String propKey, boolean defValue, android.content.Context context) {
        java.lang.String result = android.telephony.SubscriptionManager.getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                return java.lang.Integer.parseInt(result) == 1;
            } catch (java.lang.NumberFormatException err) {
                android.telephony.SubscriptionManager.logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    /**
     * Returns integer value corresponding to query result.
     *
     * @param subId
     * 		Subscription Id of Subscription
     * @param propKey
     * 		Column name in SubscriptionInfo database
     * @param defValue
     * 		Default integer value to be returned
     * @return integer result value to be returned
     * @unknown 
     */
    public static int getIntegerSubscriptionProperty(int subId, java.lang.String propKey, int defValue, android.content.Context context) {
        java.lang.String result = android.telephony.SubscriptionManager.getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                return java.lang.Integer.parseInt(result);
            } catch (java.lang.NumberFormatException err) {
                android.telephony.SubscriptionManager.logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    /**
     * Returns the resources associated with Subscription.
     *
     * @param context
     * 		Context object
     * @param subId
     * 		Subscription Id of Subscription who's resources are required
     * @return Resources associated with Subscription.
     * @unknown 
     */
    public static android.content.res.Resources getResourcesForSubId(android.content.Context context, int subId) {
        final android.telephony.SubscriptionInfo subInfo = android.telephony.SubscriptionManager.from(context).getActiveSubscriptionInfo(subId);
        android.content.res.Configuration config = context.getResources().getConfiguration();
        android.content.res.Configuration newConfig = new android.content.res.Configuration();
        newConfig.setTo(config);
        if (subInfo != null) {
            newConfig.mcc = subInfo.getMcc();
            newConfig.mnc = subInfo.getMnc();
            if (newConfig.mnc == 0)
                newConfig.mnc = android.content.res.Configuration.MNC_ZERO;

        }
        android.util.DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        android.util.DisplayMetrics newMetrics = new android.util.DisplayMetrics();
        newMetrics.setTo(metrics);
        return new android.content.res.Resources(context.getResources().getAssets(), newMetrics, newConfig);
    }

    /**
     *
     *
     * @return true if the sub ID is active. i.e. The sub ID corresponds to a known subscription
    and the SIM providing the subscription is present in a slot and in "LOADED" state.
     * @unknown 
     */
    public boolean isActiveSubId(int subId) {
        try {
            com.android.internal.telephony.ISub iSub = ISub.Stub.asInterface(android.os.ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isActiveSubId(subId);
            }
        } catch (android.os.RemoteException ex) {
        }
        return false;
    }
}

