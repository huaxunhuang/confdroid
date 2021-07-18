/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * Contains phone state and service related information.
 *
 * The following phone information is included in returned ServiceState:
 *
 * <ul>
 *   <li>Service state: IN_SERVICE, OUT_OF_SERVICE, EMERGENCY_ONLY, POWER_OFF
 *   <li>Roaming indicator
 *   <li>Operator name, short name and numeric id
 *   <li>Network selection mode
 * </ul>
 */
public class ServiceState implements android.os.Parcelable {
    static final java.lang.String LOG_TAG = "PHONE";

    static final boolean DBG = false;

    static final boolean VDBG = false;// STOPSHIP if true


    /**
     * Normal operation condition, the phone is registered
     * with an operator either in home network or in roaming.
     */
    public static final int STATE_IN_SERVICE = 0;

    /**
     * Phone is not registered with any operator, the phone
     * can be currently searching a new operator to register to, or not
     * searching to registration at all, or registration is denied, or radio
     * signal is not available.
     */
    public static final int STATE_OUT_OF_SERVICE = 1;

    /**
     * The phone is registered and locked.  Only emergency numbers are allowed. {@more }
     */
    public static final int STATE_EMERGENCY_ONLY = 2;

    /**
     * Radio of telephony is explicitly powered off.
     */
    public static final int STATE_POWER_OFF = 3;

    /**
     * RIL level registration state values from ril.h
     * ((const char **)response)[0] is registration state 0-6,
     *              0 - Not registered, MT is not currently searching
     *                  a new operator to register
     *              1 - Registered, home network
     *              2 - Not registered, but MT is currently searching
     *                  a new operator to register
     *              3 - Registration denied
     *              4 - Unknown
     *              5 - Registered, roaming
     *             10 - Same as 0, but indicates that emergency calls
     *                  are enabled.
     *             12 - Same as 2, but indicates that emergency calls
     *                  are enabled.
     *             13 - Same as 3, but indicates that emergency calls
     *                  are enabled.
     *             14 - Same as 4, but indicates that emergency calls
     *                  are enabled.
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_NOT_REG = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_HOME = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_SEARCHING = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_DENIED = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_UNKNOWN = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_ROAMING = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_NOT_REG_EMERGENCY_CALL_ENABLED = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_SEARCHING_EMERGENCY_CALL_ENABLED = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_DENIED_EMERGENCY_CALL_ENABLED = 13;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_REG_STATE_UNKNOWN_EMERGENCY_CALL_ENABLED = 14;

    /**
     * Available radio technologies for GSM, UMTS and CDMA.
     * Duplicates the constants from hardware/radio/include/ril.h
     * This should only be used by agents working with the ril.  Others
     * should use the equivalent TelephonyManager.NETWORK_TYPE_*
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;

    /**
     * GSM radio technology only supports voice. It does not support data.
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;

    /**
     * IWLAN
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;

    /**
     * LTE_CA
     *
     * @unknown 
     */
    public static final int RIL_RADIO_TECHNOLOGY_LTE_CA = 19;

    /**
     *
     *
     * @unknown 
     */
    public static final int RIL_RADIO_CDMA_TECHNOLOGY_BITMASK = ((((((1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95A - 1)) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95B - 1))) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT - 1))) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0 - 1))) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A - 1))) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B - 1))) | (1 << (android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD - 1));

    /**
     * Available registration states for GSM, UMTS and CDMA.
     */
    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_NOT_REGISTERED_AND_NOT_SEARCHING = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_HOME_NETWORK = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_NOT_REGISTERED_AND_SEARCHING = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_REGISTRATION_DENIED = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_UNKNOWN = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int REGISTRATION_STATE_ROAMING = 5;

    private int mVoiceRegState = android.telephony.ServiceState.STATE_OUT_OF_SERVICE;

    private int mDataRegState = android.telephony.ServiceState.STATE_OUT_OF_SERVICE;

    /**
     * Roaming type
     * HOME : in home network
     *
     * @unknown 
     */
    public static final int ROAMING_TYPE_NOT_ROAMING = 0;

    /**
     * Roaming type
     * UNKNOWN : in a roaming network, but we can not tell if it's domestic or international
     *
     * @unknown 
     */
    public static final int ROAMING_TYPE_UNKNOWN = 1;

    /**
     * Roaming type
     * DOMESTIC : in domestic roaming network
     *
     * @unknown 
     */
    public static final int ROAMING_TYPE_DOMESTIC = 2;

    /**
     * Roaming type
     * INTERNATIONAL : in international roaming network
     *
     * @unknown 
     */
    public static final int ROAMING_TYPE_INTERNATIONAL = 3;

    private int mVoiceRoamingType;

    private int mDataRoamingType;

    private java.lang.String mVoiceOperatorAlphaLong;

    private java.lang.String mVoiceOperatorAlphaShort;

    private java.lang.String mVoiceOperatorNumeric;

    private java.lang.String mDataOperatorAlphaLong;

    private java.lang.String mDataOperatorAlphaShort;

    private java.lang.String mDataOperatorNumeric;

    private boolean mIsManualNetworkSelection;

    private boolean mIsEmergencyOnly;

    private int mRilVoiceRadioTechnology;

    private int mRilDataRadioTechnology;

    private boolean mCssIndicator;

    private int mNetworkId;

    private int mSystemId;

    private int mCdmaRoamingIndicator;

    private int mCdmaDefaultRoamingIndicator;

    private int mCdmaEriIconIndex;

    private int mCdmaEriIconMode;

    private boolean mIsDataRoamingFromRegistration;

    private boolean mIsUsingCarrierAggregation;

    /**
     * get String description of roaming type
     *
     * @unknown 
     */
    public static final java.lang.String getRoamingLogString(int roamingType) {
        switch (roamingType) {
            case android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING :
                return "home";
            case android.telephony.ServiceState.ROAMING_TYPE_UNKNOWN :
                return "roaming";
            case android.telephony.ServiceState.ROAMING_TYPE_DOMESTIC :
                return "Domestic Roaming";
            case android.telephony.ServiceState.ROAMING_TYPE_INTERNATIONAL :
                return "International Roaming";
            default :
                return "UNKNOWN";
        }
    }

    /**
     * Create a new ServiceState from a intent notifier Bundle
     *
     * This method is used by PhoneStateIntentReceiver and maybe by
     * external applications.
     *
     * @param m
     * 		Bundle from intent notifier
     * @return newly created ServiceState
     * @unknown 
     */
    public static android.telephony.ServiceState newFromBundle(android.os.Bundle m) {
        android.telephony.ServiceState ret;
        ret = new android.telephony.ServiceState();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    /**
     * Empty constructor
     */
    public ServiceState() {
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source service state
     */
    public ServiceState(android.telephony.ServiceState s) {
        copyFrom(s);
    }

    protected void copyFrom(android.telephony.ServiceState s) {
        mVoiceRegState = s.mVoiceRegState;
        mDataRegState = s.mDataRegState;
        mVoiceRoamingType = s.mVoiceRoamingType;
        mDataRoamingType = s.mDataRoamingType;
        mVoiceOperatorAlphaLong = s.mVoiceOperatorAlphaLong;
        mVoiceOperatorAlphaShort = s.mVoiceOperatorAlphaShort;
        mVoiceOperatorNumeric = s.mVoiceOperatorNumeric;
        mDataOperatorAlphaLong = s.mDataOperatorAlphaLong;
        mDataOperatorAlphaShort = s.mDataOperatorAlphaShort;
        mDataOperatorNumeric = s.mDataOperatorNumeric;
        mIsManualNetworkSelection = s.mIsManualNetworkSelection;
        mRilVoiceRadioTechnology = s.mRilVoiceRadioTechnology;
        mRilDataRadioTechnology = s.mRilDataRadioTechnology;
        mCssIndicator = s.mCssIndicator;
        mNetworkId = s.mNetworkId;
        mSystemId = s.mSystemId;
        mCdmaRoamingIndicator = s.mCdmaRoamingIndicator;
        mCdmaDefaultRoamingIndicator = s.mCdmaDefaultRoamingIndicator;
        mCdmaEriIconIndex = s.mCdmaEriIconIndex;
        mCdmaEriIconMode = s.mCdmaEriIconMode;
        mIsEmergencyOnly = s.mIsEmergencyOnly;
        mIsDataRoamingFromRegistration = s.mIsDataRoamingFromRegistration;
        mIsUsingCarrierAggregation = s.mIsUsingCarrierAggregation;
    }

    /**
     * Construct a ServiceState object from the given parcel.
     */
    public ServiceState(android.os.Parcel in) {
        mVoiceRegState = in.readInt();
        mDataRegState = in.readInt();
        mVoiceRoamingType = in.readInt();
        mDataRoamingType = in.readInt();
        mVoiceOperatorAlphaLong = in.readString();
        mVoiceOperatorAlphaShort = in.readString();
        mVoiceOperatorNumeric = in.readString();
        mDataOperatorAlphaLong = in.readString();
        mDataOperatorAlphaShort = in.readString();
        mDataOperatorNumeric = in.readString();
        mIsManualNetworkSelection = in.readInt() != 0;
        mRilVoiceRadioTechnology = in.readInt();
        mRilDataRadioTechnology = in.readInt();
        mCssIndicator = in.readInt() != 0;
        mNetworkId = in.readInt();
        mSystemId = in.readInt();
        mCdmaRoamingIndicator = in.readInt();
        mCdmaDefaultRoamingIndicator = in.readInt();
        mCdmaEriIconIndex = in.readInt();
        mCdmaEriIconMode = in.readInt();
        mIsEmergencyOnly = in.readInt() != 0;
        mIsDataRoamingFromRegistration = in.readInt() != 0;
        mIsUsingCarrierAggregation = in.readInt() != 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mVoiceRegState);
        out.writeInt(mDataRegState);
        out.writeInt(mVoiceRoamingType);
        out.writeInt(mDataRoamingType);
        out.writeString(mVoiceOperatorAlphaLong);
        out.writeString(mVoiceOperatorAlphaShort);
        out.writeString(mVoiceOperatorNumeric);
        out.writeString(mDataOperatorAlphaLong);
        out.writeString(mDataOperatorAlphaShort);
        out.writeString(mDataOperatorNumeric);
        out.writeInt(mIsManualNetworkSelection ? 1 : 0);
        out.writeInt(mRilVoiceRadioTechnology);
        out.writeInt(mRilDataRadioTechnology);
        out.writeInt(mCssIndicator ? 1 : 0);
        out.writeInt(mNetworkId);
        out.writeInt(mSystemId);
        out.writeInt(mCdmaRoamingIndicator);
        out.writeInt(mCdmaDefaultRoamingIndicator);
        out.writeInt(mCdmaEriIconIndex);
        out.writeInt(mCdmaEriIconMode);
        out.writeInt(mIsEmergencyOnly ? 1 : 0);
        out.writeInt(mIsDataRoamingFromRegistration ? 1 : 0);
        out.writeInt(mIsUsingCarrierAggregation ? 1 : 0);
    }

    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.telephony.ServiceState> CREATOR = new android.os.Parcelable.Creator<android.telephony.ServiceState>() {
        public android.telephony.ServiceState createFromParcel(android.os.Parcel in) {
            return new android.telephony.ServiceState(in);
        }

        public android.telephony.ServiceState[] newArray(int size) {
            return new android.telephony.ServiceState[size];
        }
    };

    /**
     * Get current voice service state
     */
    public int getState() {
        return getVoiceRegState();
    }

    /**
     * Get current voice service state
     *
     * @see #STATE_IN_SERVICE
     * @see #STATE_OUT_OF_SERVICE
     * @see #STATE_EMERGENCY_ONLY
     * @see #STATE_POWER_OFF
     * @unknown 
     */
    public int getVoiceRegState() {
        return mVoiceRegState;
    }

    /**
     * Get current data service state
     *
     * @see #STATE_IN_SERVICE
     * @see #STATE_OUT_OF_SERVICE
     * @see #STATE_EMERGENCY_ONLY
     * @see #STATE_POWER_OFF
     * @unknown 
     */
    public int getDataRegState() {
        return mDataRegState;
    }

    /**
     * Get current roaming indicator of phone
     * (note: not just decoding from TS 27.007 7.2)
     *
     * @return true if TS 27.007 7.2 roaming is true
    and ONS is different from SPN
     */
    public boolean getRoaming() {
        return getVoiceRoaming() || getDataRoaming();
    }

    /**
     * Get current voice network roaming status
     *
     * @return roaming status
     * @unknown 
     */
    public boolean getVoiceRoaming() {
        return mVoiceRoamingType != android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
    }

    /**
     * Get current voice network roaming type
     *
     * @return roaming type
     * @unknown 
     */
    public int getVoiceRoamingType() {
        return mVoiceRoamingType;
    }

    /**
     * Get current data network roaming type
     *
     * @return roaming type
     * @unknown 
     */
    public boolean getDataRoaming() {
        return mDataRoamingType != android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
    }

    /**
     * Set whether data network registration state is roaming
     *
     * This should only be set to the roaming value received
     * once the data registration phase has completed.
     *
     * @unknown 
     */
    public void setDataRoamingFromRegistration(boolean dataRoaming) {
        mIsDataRoamingFromRegistration = dataRoaming;
    }

    /**
     * Get whether data network registration state is roaming
     *
     * @return true if registration indicates roaming, false otherwise
     * @unknown 
     */
    public boolean getDataRoamingFromRegistration() {
        return mIsDataRoamingFromRegistration;
    }

    /**
     * Get current data network roaming type
     *
     * @return roaming type
     * @unknown 
     */
    public int getDataRoamingType() {
        return mDataRoamingType;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isEmergencyOnly() {
        return mIsEmergencyOnly;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCdmaRoamingIndicator() {
        return this.mCdmaRoamingIndicator;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCdmaDefaultRoamingIndicator() {
        return this.mCdmaDefaultRoamingIndicator;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCdmaEriIconIndex() {
        return this.mCdmaEriIconIndex;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCdmaEriIconMode() {
        return this.mCdmaEriIconMode;
    }

    /**
     * Get current registered operator name in long alphanumeric format.
     *
     * In GSM/UMTS, long format can be up to 16 characters long.
     * In CDMA, returns the ERI text, if set. Otherwise, returns the ONS.
     *
     * @return long name of operator, null if unregistered or unknown
     */
    public java.lang.String getOperatorAlphaLong() {
        return mVoiceOperatorAlphaLong;
    }

    /**
     * Get current registered voice network operator name in long alphanumeric format.
     *
     * @return long name of operator
     * @unknown 
     */
    public java.lang.String getVoiceOperatorAlphaLong() {
        return mVoiceOperatorAlphaLong;
    }

    /**
     * Get current registered data network operator name in long alphanumeric format.
     *
     * @return long name of voice operator
     * @unknown 
     */
    public java.lang.String getDataOperatorAlphaLong() {
        return mDataOperatorAlphaLong;
    }

    /**
     * Get current registered operator name in short alphanumeric format.
     *
     * In GSM/UMTS, short format can be up to 8 characters long.
     *
     * @return short name of operator, null if unregistered or unknown
     */
    public java.lang.String getOperatorAlphaShort() {
        return mVoiceOperatorAlphaShort;
    }

    /**
     * Get current registered voice network operator name in short alphanumeric format.
     *
     * @return short name of operator, null if unregistered or unknown
     * @unknown 
     */
    public java.lang.String getVoiceOperatorAlphaShort() {
        return mVoiceOperatorAlphaShort;
    }

    /**
     * Get current registered data network operator name in short alphanumeric format.
     *
     * @return short name of operator, null if unregistered or unknown
     * @unknown 
     */
    public java.lang.String getDataOperatorAlphaShort() {
        return mDataOperatorAlphaShort;
    }

    /**
     * Get current registered operator numeric id.
     *
     * In GSM/UMTS, numeric format is 3 digit country code plus 2 or 3 digit
     * network code.
     *
     * @return numeric format of operator, null if unregistered or unknown
     */
    /* The country code can be decoded using
    {@link com.android.internal.telephony.MccTable#countryCodeForMcc(int)}.
     */
    public java.lang.String getOperatorNumeric() {
        return mVoiceOperatorNumeric;
    }

    /**
     * Get current registered voice network operator numeric id.
     *
     * @return numeric format of operator, null if unregistered or unknown
     * @unknown 
     */
    public java.lang.String getVoiceOperatorNumeric() {
        return mVoiceOperatorNumeric;
    }

    /**
     * Get current registered data network operator numeric id.
     *
     * @return numeric format of operator, null if unregistered or unknown
     * @unknown 
     */
    public java.lang.String getDataOperatorNumeric() {
        return mDataOperatorNumeric;
    }

    /**
     * Get current network selection mode.
     *
     * @return true if manual mode, false if automatic mode
     */
    public boolean getIsManualSelection() {
        return mIsManualNetworkSelection;
    }

    @java.lang.Override
    public int hashCode() {
        return ((((((((((((((mVoiceRegState * 31) + (mDataRegState * 37)) + mVoiceRoamingType) + mDataRoamingType) + (mIsManualNetworkSelection ? 1 : 0)) + (null == mVoiceOperatorAlphaLong ? 0 : mVoiceOperatorAlphaLong.hashCode())) + (null == mVoiceOperatorAlphaShort ? 0 : mVoiceOperatorAlphaShort.hashCode())) + (null == mVoiceOperatorNumeric ? 0 : mVoiceOperatorNumeric.hashCode())) + (null == mDataOperatorAlphaLong ? 0 : mDataOperatorAlphaLong.hashCode())) + (null == mDataOperatorAlphaShort ? 0 : mDataOperatorAlphaShort.hashCode())) + (null == mDataOperatorNumeric ? 0 : mDataOperatorNumeric.hashCode())) + mCdmaRoamingIndicator) + mCdmaDefaultRoamingIndicator) + (mIsEmergencyOnly ? 1 : 0)) + (mIsDataRoamingFromRegistration ? 1 : 0);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.ServiceState s;
        try {
            s = ((android.telephony.ServiceState) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return ((((((((((((((((((((mVoiceRegState == s.mVoiceRegState) && (mDataRegState == s.mDataRegState)) && (mIsManualNetworkSelection == s.mIsManualNetworkSelection)) && (mVoiceRoamingType == s.mVoiceRoamingType)) && (mDataRoamingType == s.mDataRoamingType)) && android.telephony.ServiceState.equalsHandlesNulls(mVoiceOperatorAlphaLong, s.mVoiceOperatorAlphaLong)) && android.telephony.ServiceState.equalsHandlesNulls(mVoiceOperatorAlphaShort, s.mVoiceOperatorAlphaShort)) && android.telephony.ServiceState.equalsHandlesNulls(mVoiceOperatorNumeric, s.mVoiceOperatorNumeric)) && android.telephony.ServiceState.equalsHandlesNulls(mDataOperatorAlphaLong, s.mDataOperatorAlphaLong)) && android.telephony.ServiceState.equalsHandlesNulls(mDataOperatorAlphaShort, s.mDataOperatorAlphaShort)) && android.telephony.ServiceState.equalsHandlesNulls(mDataOperatorNumeric, s.mDataOperatorNumeric)) && android.telephony.ServiceState.equalsHandlesNulls(mRilVoiceRadioTechnology, s.mRilVoiceRadioTechnology)) && android.telephony.ServiceState.equalsHandlesNulls(mRilDataRadioTechnology, s.mRilDataRadioTechnology)) && android.telephony.ServiceState.equalsHandlesNulls(mCssIndicator, s.mCssIndicator)) && android.telephony.ServiceState.equalsHandlesNulls(mNetworkId, s.mNetworkId)) && android.telephony.ServiceState.equalsHandlesNulls(mSystemId, s.mSystemId)) && android.telephony.ServiceState.equalsHandlesNulls(mCdmaRoamingIndicator, s.mCdmaRoamingIndicator)) && android.telephony.ServiceState.equalsHandlesNulls(mCdmaDefaultRoamingIndicator, s.mCdmaDefaultRoamingIndicator)) && (mIsEmergencyOnly == s.mIsEmergencyOnly)) && (mIsDataRoamingFromRegistration == s.mIsDataRoamingFromRegistration)) && (mIsUsingCarrierAggregation == s.mIsUsingCarrierAggregation);
    }

    /**
     * Convert radio technology to String
     *
     * @param radioTechnology
     * 		
     * @return String representation of the RAT
     * @unknown 
     */
    public static java.lang.String rilRadioTechnologyToString(int rt) {
        java.lang.String rtString;
        switch (rt) {
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UNKNOWN :
                rtString = "Unknown";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GPRS :
                rtString = "GPRS";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EDGE :
                rtString = "EDGE";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UMTS :
                rtString = "UMTS";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95A :
                rtString = "CDMA-IS95A";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95B :
                rtString = "CDMA-IS95B";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT :
                rtString = "1xRTT";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0 :
                rtString = "EvDo-rev.0";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A :
                rtString = "EvDo-rev.A";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSDPA :
                rtString = "HSDPA";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSUPA :
                rtString = "HSUPA";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPA :
                rtString = "HSPA";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B :
                rtString = "EvDo-rev.B";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD :
                rtString = "eHRPD";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE :
                rtString = "LTE";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP :
                rtString = "HSPAP";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GSM :
                rtString = "GSM";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IWLAN :
                rtString = "IWLAN";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_TD_SCDMA :
                rtString = "TD-SCDMA";
                break;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA :
                rtString = "LTE_CA";
                break;
            default :
                rtString = "Unexpected";
                android.telephony.Rlog.w(android.telephony.ServiceState.LOG_TAG, "Unexpected radioTechnology=" + rt);
                break;
        }
        return rtString;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String radioTechnology = android.telephony.ServiceState.rilRadioTechnologyToString(mRilVoiceRadioTechnology);
        java.lang.String dataRadioTechnology = android.telephony.ServiceState.rilRadioTechnologyToString(mRilDataRadioTechnology);
        return (((((((((((((((((((((((((((((((((((((((((mVoiceRegState + " ") + mDataRegState) + " ") + "voice ") + android.telephony.ServiceState.getRoamingLogString(mVoiceRoamingType)) + " ") + "data ") + android.telephony.ServiceState.getRoamingLogString(mDataRoamingType)) + " ") + mVoiceOperatorAlphaLong) + " ") + mVoiceOperatorAlphaShort) + " ") + mVoiceOperatorNumeric) + " ") + mDataOperatorAlphaLong) + " ") + mDataOperatorAlphaShort) + " ") + mDataOperatorNumeric) + " ") + (mIsManualNetworkSelection ? "(manual)" : "")) + " ") + radioTechnology) + " ") + dataRadioTechnology) + " ") + (mCssIndicator ? "CSS supported" : "CSS not supported")) + " ") + mNetworkId) + " ") + mSystemId) + " RoamInd=") + mCdmaRoamingIndicator) + " DefRoamInd=") + mCdmaDefaultRoamingIndicator) + " EmergOnly=") + mIsEmergencyOnly) + " IsDataRoamingFromRegistration=") + mIsDataRoamingFromRegistration) + " IsUsingCarrierAggregation=") + mIsUsingCarrierAggregation;
    }

    private void setNullState(int state) {
        if (android.telephony.ServiceState.DBG)
            android.telephony.Rlog.d(android.telephony.ServiceState.LOG_TAG, "[ServiceState] setNullState=" + state);

        mVoiceRegState = state;
        mDataRegState = state;
        mVoiceRoamingType = android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
        mDataRoamingType = android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
        mVoiceOperatorAlphaLong = null;
        mVoiceOperatorAlphaShort = null;
        mVoiceOperatorNumeric = null;
        mDataOperatorAlphaLong = null;
        mDataOperatorAlphaShort = null;
        mDataOperatorNumeric = null;
        mIsManualNetworkSelection = false;
        mRilVoiceRadioTechnology = 0;
        mRilDataRadioTechnology = 0;
        mCssIndicator = false;
        mNetworkId = -1;
        mSystemId = -1;
        mCdmaRoamingIndicator = -1;
        mCdmaDefaultRoamingIndicator = -1;
        mCdmaEriIconIndex = -1;
        mCdmaEriIconMode = -1;
        mIsEmergencyOnly = false;
        mIsDataRoamingFromRegistration = false;
        mIsUsingCarrierAggregation = false;
    }

    public void setStateOutOfService() {
        setNullState(android.telephony.ServiceState.STATE_OUT_OF_SERVICE);
    }

    public void setStateOff() {
        setNullState(android.telephony.ServiceState.STATE_POWER_OFF);
    }

    public void setState(int state) {
        setVoiceRegState(state);
        if (android.telephony.ServiceState.DBG)
            android.telephony.Rlog.e(android.telephony.ServiceState.LOG_TAG, "[ServiceState] setState deprecated use setVoiceRegState()");

    }

    /**
     *
     *
     * @unknown 
     */
    public void setVoiceRegState(int state) {
        mVoiceRegState = state;
        if (android.telephony.ServiceState.DBG)
            android.telephony.Rlog.d(android.telephony.ServiceState.LOG_TAG, "[ServiceState] setVoiceRegState=" + mVoiceRegState);

    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataRegState(int state) {
        mDataRegState = state;
        if (android.telephony.ServiceState.VDBG)
            android.telephony.Rlog.d(android.telephony.ServiceState.LOG_TAG, "[ServiceState] setDataRegState=" + mDataRegState);

    }

    public void setRoaming(boolean roaming) {
        mVoiceRoamingType = (roaming) ? android.telephony.ServiceState.ROAMING_TYPE_UNKNOWN : android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
        mDataRoamingType = mVoiceRoamingType;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setVoiceRoaming(boolean roaming) {
        mVoiceRoamingType = (roaming) ? android.telephony.ServiceState.ROAMING_TYPE_UNKNOWN : android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setVoiceRoamingType(int type) {
        mVoiceRoamingType = type;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataRoaming(boolean dataRoaming) {
        mDataRoamingType = (dataRoaming) ? android.telephony.ServiceState.ROAMING_TYPE_UNKNOWN : android.telephony.ServiceState.ROAMING_TYPE_NOT_ROAMING;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataRoamingType(int type) {
        mDataRoamingType = type;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEmergencyOnly(boolean emergencyOnly) {
        mIsEmergencyOnly = emergencyOnly;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaRoamingIndicator(int roaming) {
        this.mCdmaRoamingIndicator = roaming;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaDefaultRoamingIndicator(int roaming) {
        this.mCdmaDefaultRoamingIndicator = roaming;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaEriIconIndex(int index) {
        this.mCdmaEriIconIndex = index;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaEriIconMode(int mode) {
        this.mCdmaEriIconMode = mode;
    }

    public void setOperatorName(java.lang.String longName, java.lang.String shortName, java.lang.String numeric) {
        mVoiceOperatorAlphaLong = longName;
        mVoiceOperatorAlphaShort = shortName;
        mVoiceOperatorNumeric = numeric;
        mDataOperatorAlphaLong = longName;
        mDataOperatorAlphaShort = shortName;
        mDataOperatorNumeric = numeric;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setVoiceOperatorName(java.lang.String longName, java.lang.String shortName, java.lang.String numeric) {
        mVoiceOperatorAlphaLong = longName;
        mVoiceOperatorAlphaShort = shortName;
        mVoiceOperatorNumeric = numeric;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataOperatorName(java.lang.String longName, java.lang.String shortName, java.lang.String numeric) {
        mDataOperatorAlphaLong = longName;
        mDataOperatorAlphaShort = shortName;
        mDataOperatorNumeric = numeric;
    }

    /**
     * In CDMA, mOperatorAlphaLong can be set from the ERI text.
     * This is done from the GsmCdmaPhone and not from the ServiceStateTracker.
     *
     * @unknown 
     */
    public void setOperatorAlphaLong(java.lang.String longName) {
        mVoiceOperatorAlphaLong = longName;
        mDataOperatorAlphaLong = longName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setVoiceOperatorAlphaLong(java.lang.String longName) {
        mVoiceOperatorAlphaLong = longName;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataOperatorAlphaLong(java.lang.String longName) {
        mDataOperatorAlphaLong = longName;
    }

    public void setIsManualSelection(boolean isManual) {
        mIsManualNetworkSelection = isManual;
    }

    /**
     * Test whether two objects hold the same data values or both are null.
     *
     * @param a
     * 		first obj
     * @param b
     * 		second obj
     * @return true if two objects equal or both are null
     */
    private static boolean equalsHandlesNulls(java.lang.Object a, java.lang.Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Set ServiceState based on intent notifier map.
     *
     * @param m
     * 		intent notifier map
     * @unknown 
     */
    private void setFromNotifierBundle(android.os.Bundle m) {
        mVoiceRegState = m.getInt("voiceRegState");
        mDataRegState = m.getInt("dataRegState");
        mVoiceRoamingType = m.getInt("voiceRoamingType");
        mDataRoamingType = m.getInt("dataRoamingType");
        mVoiceOperatorAlphaLong = m.getString("operator-alpha-long");
        mVoiceOperatorAlphaShort = m.getString("operator-alpha-short");
        mVoiceOperatorNumeric = m.getString("operator-numeric");
        mDataOperatorAlphaLong = m.getString("data-operator-alpha-long");
        mDataOperatorAlphaShort = m.getString("data-operator-alpha-short");
        mDataOperatorNumeric = m.getString("data-operator-numeric");
        mIsManualNetworkSelection = m.getBoolean("manual");
        mRilVoiceRadioTechnology = m.getInt("radioTechnology");
        mRilDataRadioTechnology = m.getInt("dataRadioTechnology");
        mCssIndicator = m.getBoolean("cssIndicator");
        mNetworkId = m.getInt("networkId");
        mSystemId = m.getInt("systemId");
        mCdmaRoamingIndicator = m.getInt("cdmaRoamingIndicator");
        mCdmaDefaultRoamingIndicator = m.getInt("cdmaDefaultRoamingIndicator");
        mIsEmergencyOnly = m.getBoolean("emergencyOnly");
        mIsDataRoamingFromRegistration = m.getBoolean("isDataRoamingFromRegistration");
        mIsUsingCarrierAggregation = m.getBoolean("isUsingCarrierAggregation");
    }

    /**
     * Set intent notifier Bundle based on service state.
     *
     * @param m
     * 		intent notifier Bundle
     * @unknown 
     */
    public void fillInNotifierBundle(android.os.Bundle m) {
        m.putInt("voiceRegState", mVoiceRegState);
        m.putInt("dataRegState", mDataRegState);
        m.putInt("voiceRoamingType", mVoiceRoamingType);
        m.putInt("dataRoamingType", mDataRoamingType);
        m.putString("operator-alpha-long", mVoiceOperatorAlphaLong);
        m.putString("operator-alpha-short", mVoiceOperatorAlphaShort);
        m.putString("operator-numeric", mVoiceOperatorNumeric);
        m.putString("data-operator-alpha-long", mDataOperatorAlphaLong);
        m.putString("data-operator-alpha-short", mDataOperatorAlphaShort);
        m.putString("data-operator-numeric", mDataOperatorNumeric);
        m.putBoolean("manual", java.lang.Boolean.valueOf(mIsManualNetworkSelection));
        m.putInt("radioTechnology", mRilVoiceRadioTechnology);
        m.putInt("dataRadioTechnology", mRilDataRadioTechnology);
        m.putBoolean("cssIndicator", mCssIndicator);
        m.putInt("networkId", mNetworkId);
        m.putInt("systemId", mSystemId);
        m.putInt("cdmaRoamingIndicator", mCdmaRoamingIndicator);
        m.putInt("cdmaDefaultRoamingIndicator", mCdmaDefaultRoamingIndicator);
        m.putBoolean("emergencyOnly", java.lang.Boolean.valueOf(mIsEmergencyOnly));
        m.putBoolean("isDataRoamingFromRegistration", java.lang.Boolean.valueOf(mIsDataRoamingFromRegistration));
        m.putBoolean("isUsingCarrierAggregation", java.lang.Boolean.valueOf(mIsUsingCarrierAggregation));
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRilVoiceRadioTechnology(int rt) {
        if (rt == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA) {
            rt = android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE;
        }
        this.mRilVoiceRadioTechnology = rt;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRilDataRadioTechnology(int rt) {
        if (rt == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA) {
            rt = android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE;
            this.mIsUsingCarrierAggregation = true;
        } else {
            this.mIsUsingCarrierAggregation = false;
        }
        this.mRilDataRadioTechnology = rt;
        if (android.telephony.ServiceState.VDBG)
            android.telephony.Rlog.d(android.telephony.ServiceState.LOG_TAG, "[ServiceState] setRilDataRadioTechnology=" + mRilDataRadioTechnology);

    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isUsingCarrierAggregation() {
        return mIsUsingCarrierAggregation;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setIsUsingCarrierAggregation(boolean ca) {
        mIsUsingCarrierAggregation = ca;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCssIndicator(int css) {
        this.mCssIndicator = css != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setSystemAndNetworkId(int systemId, int networkId) {
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getRilVoiceRadioTechnology() {
        return this.mRilVoiceRadioTechnology;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getRilDataRadioTechnology() {
        return this.mRilDataRadioTechnology;
    }

    /**
     *
     *
     * @unknown 
     * @unknown to be removed Q3 2013 use {@link #getRilDataRadioTechnology} or
    {@link #getRilVoiceRadioTechnology}
     */
    public int getRadioTechnology() {
        android.telephony.Rlog.e(android.telephony.ServiceState.LOG_TAG, "ServiceState.getRadioTechnology() DEPRECATED will be removed *******");
        return getRilDataRadioTechnology();
    }

    private int rilRadioTechnologyToNetworkType(int rt) {
        switch (rt) {
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GPRS :
                return android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EDGE :
                return android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UMTS :
                return android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSDPA :
                return android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSUPA :
                return android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPA :
                return android.telephony.TelephonyManager.NETWORK_TYPE_HSPA;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95A :
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95B :
                return android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT :
                return android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0 :
                return android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A :
                return android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B :
                return android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD :
                return android.telephony.TelephonyManager.NETWORK_TYPE_EHRPD;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE :
                return android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP :
                return android.telephony.TelephonyManager.NETWORK_TYPE_HSPAP;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GSM :
                return android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_TD_SCDMA :
                return android.telephony.TelephonyManager.NETWORK_TYPE_TD_SCDMA;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IWLAN :
                return android.telephony.TelephonyManager.NETWORK_TYPE_IWLAN;
            case android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA :
                return android.telephony.TelephonyManager.NETWORK_TYPE_LTE_CA;
            default :
                return android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;
        }
    }

    /**
     *
     *
     * @unknown to be removed Q3 2013 use {@link #getVoiceNetworkType}
     * @unknown 
     */
    public int getNetworkType() {
        android.telephony.Rlog.e(android.telephony.ServiceState.LOG_TAG, "ServiceState.getNetworkType() DEPRECATED will be removed *******");
        return rilRadioTechnologyToNetworkType(mRilVoiceRadioTechnology);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getDataNetworkType() {
        return rilRadioTechnologyToNetworkType(mRilDataRadioTechnology);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getVoiceNetworkType() {
        return rilRadioTechnologyToNetworkType(mRilVoiceRadioTechnology);
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCssIndicator() {
        return this.mCssIndicator ? 1 : 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getNetworkId() {
        return this.mNetworkId;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSystemId() {
        return this.mSystemId;
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isGsm(int radioTechnology) {
        return (((((((((((radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GPRS) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EDGE)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UMTS)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSDPA)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSUPA)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPA)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GSM)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_TD_SCDMA)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IWLAN)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isCdma(int radioTechnology) {
        return ((((((radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95A) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95B)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B)) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isLte(int radioTechnology) {
        return (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE) || (radioTechnology == android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA);
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean bearerBitmapHasCdma(int radioTechnologyBitmap) {
        return (android.telephony.ServiceState.RIL_RADIO_CDMA_TECHNOLOGY_BITMASK & radioTechnologyBitmap) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean bitmaskHasTech(int bearerBitmask, int radioTech) {
        if (bearerBitmask == 0) {
            return true;
        } else
            if (radioTech >= 1) {
                return (bearerBitmask & (1 << (radioTech - 1))) != 0;
            }

        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getBitmaskForTech(int radioTech) {
        if (radioTech >= 1) {
            return 1 << (radioTech - 1);
        }
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getBitmaskFromString(java.lang.String bearerList) {
        java.lang.String[] bearers = bearerList.split("\\|");
        int bearerBitmask = 0;
        for (java.lang.String bearer : bearers) {
            int bearerInt = 0;
            try {
                bearerInt = java.lang.Integer.parseInt(bearer.trim());
            } catch (java.lang.NumberFormatException nfe) {
                return 0;
            }
            if (bearerInt == 0) {
                return 0;
            }
            bearerBitmask |= android.telephony.ServiceState.getBitmaskForTech(bearerInt);
        }
        return bearerBitmask;
    }

    /**
     * Returns a merged ServiceState consisting of the base SS with voice settings from the
     * voice SS. The voice SS is only used if it is IN_SERVICE (otherwise the base SS is returned).
     *
     * @unknown 
     */
    public static android.telephony.ServiceState mergeServiceStates(android.telephony.ServiceState baseSs, android.telephony.ServiceState voiceSs) {
        if (voiceSs.mVoiceRegState != android.telephony.ServiceState.STATE_IN_SERVICE) {
            return baseSs;
        }
        android.telephony.ServiceState newSs = new android.telephony.ServiceState(baseSs);
        // voice overrides
        newSs.mVoiceRegState = voiceSs.mVoiceRegState;
        newSs.mIsEmergencyOnly = false;// only get here if voice is IN_SERVICE

        return newSs;
    }
}

