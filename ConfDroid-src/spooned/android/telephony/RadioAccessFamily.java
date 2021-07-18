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
 * Object to indicate the phone radio type and access technology.
 *
 * @unknown 
 */
public class RadioAccessFamily implements android.os.Parcelable {
    // Radio Access Family
    // 2G
    public static final int RAF_UNKNOWN = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UNKNOWN;

    public static final int RAF_GSM = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GSM;

    public static final int RAF_GPRS = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_GPRS;

    public static final int RAF_EDGE = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EDGE;

    public static final int RAF_IS95A = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95A;

    public static final int RAF_IS95B = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_IS95B;

    public static final int RAF_1xRTT = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT;

    // 3G
    public static final int RAF_EVDO_0 = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0;

    public static final int RAF_EVDO_A = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A;

    public static final int RAF_EVDO_B = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B;

    public static final int RAF_EHRPD = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD;

    public static final int RAF_HSUPA = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSUPA;

    public static final int RAF_HSDPA = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSDPA;

    public static final int RAF_HSPA = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPA;

    public static final int RAF_HSPAP = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP;

    public static final int RAF_UMTS = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_UMTS;

    public static final int RAF_TD_SCDMA = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_TD_SCDMA;

    // 4G
    public static final int RAF_LTE = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE;

    public static final int RAF_LTE_CA = 1 << android.telephony.ServiceState.RIL_RADIO_TECHNOLOGY_LTE_CA;

    // Grouping of RAFs
    // 2G
    private static final int GSM = (android.telephony.RadioAccessFamily.RAF_GSM | android.telephony.RadioAccessFamily.RAF_GPRS) | android.telephony.RadioAccessFamily.RAF_EDGE;

    private static final int CDMA = (android.telephony.RadioAccessFamily.RAF_IS95A | android.telephony.RadioAccessFamily.RAF_IS95B) | android.telephony.RadioAccessFamily.RAF_1xRTT;

    // 3G
    private static final int EVDO = ((android.telephony.RadioAccessFamily.RAF_EVDO_0 | android.telephony.RadioAccessFamily.RAF_EVDO_A) | android.telephony.RadioAccessFamily.RAF_EVDO_B) | android.telephony.RadioAccessFamily.RAF_EHRPD;

    private static final int HS = ((android.telephony.RadioAccessFamily.RAF_HSUPA | android.telephony.RadioAccessFamily.RAF_HSDPA) | android.telephony.RadioAccessFamily.RAF_HSPA) | android.telephony.RadioAccessFamily.RAF_HSPAP;

    private static final int WCDMA = android.telephony.RadioAccessFamily.HS | android.telephony.RadioAccessFamily.RAF_UMTS;

    // 4G
    private static final int LTE = android.telephony.RadioAccessFamily.RAF_LTE | android.telephony.RadioAccessFamily.RAF_LTE_CA;

    /* Phone ID of phone */
    private int mPhoneId;

    /* Radio Access Family */
    private int mRadioAccessFamily;

    /**
     * Constructor.
     *
     * @param phoneId
     * 		the phone ID
     * @param radioAccessFamily
     * 		the phone radio access family defined
     * 		in RadioAccessFamily. It's a bit mask value to represent
     * 		the support type.
     */
    public RadioAccessFamily(int phoneId, int radioAccessFamily) {
        mPhoneId = phoneId;
        mRadioAccessFamily = radioAccessFamily;
    }

    /**
     * Get phone ID.
     *
     * @return phone ID
     */
    public int getPhoneId() {
        return mPhoneId;
    }

    /**
     * get radio access family.
     *
     * @return radio access family
     */
    public int getRadioAccessFamily() {
        return mRadioAccessFamily;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String ret = ((("{ mPhoneId = " + mPhoneId) + ", mRadioAccessFamily = ") + mRadioAccessFamily) + "}";
        return ret;
    }

    /**
     * Implement the Parcelable interface.
     *
     * @return describe content
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface.
     *
     * @param outParcel
     * 		The Parcel in which the object should be written.
     * @param flags
     * 		Additional flags about how the object should be written.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel outParcel, int flags) {
        outParcel.writeInt(mPhoneId);
        outParcel.writeInt(mRadioAccessFamily);
    }

    /**
     * Implement the Parcelable interface.
     */
    public static final android.os.Parcelable.Creator<android.telephony.RadioAccessFamily> CREATOR = new android.os.Parcelable.Creator<android.telephony.RadioAccessFamily>() {
        @java.lang.Override
        public android.telephony.RadioAccessFamily createFromParcel(android.os.Parcel in) {
            int phoneId = in.readInt();
            int radioAccessFamily = in.readInt();
            return new android.telephony.RadioAccessFamily(phoneId, radioAccessFamily);
        }

        @java.lang.Override
        public android.telephony.RadioAccessFamily[] newArray(int size) {
            return new android.telephony.RadioAccessFamily[size];
        }
    };

    public static int getRafFromNetworkType(int type) {
        int raf;
        switch (type) {
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_WCDMA_PREF :
                raf = android.telephony.RadioAccessFamily.GSM | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_GSM_ONLY :
                raf = android.telephony.RadioAccessFamily.GSM;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_WCDMA_ONLY :
                raf = android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_GSM_UMTS :
                raf = android.telephony.RadioAccessFamily.GSM | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_CDMA :
                raf = android.telephony.RadioAccessFamily.CDMA | android.telephony.RadioAccessFamily.EVDO;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_CDMA_EVDO :
                raf = (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_GSM_WCDMA :
                raf = (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA :
                raf = (((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_ONLY :
                raf = android.telephony.RadioAccessFamily.LTE;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_WCDMA :
                raf = android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_CDMA_NO_EVDO :
                raf = android.telephony.RadioAccessFamily.CDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_EVDO_NO_CDMA :
                raf = android.telephony.RadioAccessFamily.EVDO;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_GLOBAL :
                raf = ((android.telephony.RadioAccessFamily.GSM | android.telephony.RadioAccessFamily.WCDMA) | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_ONLY :
                raf = android.telephony.RadioAccessFamily.RAF_TD_SCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_WCDMA :
                raf = android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA :
                raf = android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_GSM :
                raf = android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.GSM;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_GSM :
                raf = (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.GSM;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_GSM_WCDMA :
                raf = (android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_WCDMA :
                raf = (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_GSM_WCDMA :
                raf = ((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA :
                raf = (((android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            case com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA :
                raf = ((((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA;
                break;
            default :
                raf = android.telephony.RadioAccessFamily.RAF_UNKNOWN;
                break;
        }
        return raf;
    }

    /**
     * if the raf includes ANY bit set for a group
     * adjust it to contain ALL the bits for that group
     */
    private static int getAdjustedRaf(int raf) {
        raf = ((android.telephony.RadioAccessFamily.GSM & raf) > 0) ? android.telephony.RadioAccessFamily.GSM | raf : raf;
        raf = ((android.telephony.RadioAccessFamily.WCDMA & raf) > 0) ? android.telephony.RadioAccessFamily.WCDMA | raf : raf;
        raf = ((android.telephony.RadioAccessFamily.CDMA & raf) > 0) ? android.telephony.RadioAccessFamily.CDMA | raf : raf;
        raf = ((android.telephony.RadioAccessFamily.EVDO & raf) > 0) ? android.telephony.RadioAccessFamily.EVDO | raf : raf;
        raf = ((android.telephony.RadioAccessFamily.LTE & raf) > 0) ? android.telephony.RadioAccessFamily.LTE | raf : raf;
        return raf;
    }

    /**
     * Returns the highest capability of the RadioAccessFamily (4G > 3G > 2G).
     *
     * @param raf
     * 		The RadioAccessFamily that we wish to filter
     * @return The highest radio capability
     */
    public static int getHighestRafCapability(int raf) {
        if ((android.telephony.RadioAccessFamily.LTE & raf) > 0) {
            return android.telephony.TelephonyManager.NETWORK_CLASS_4_G;
        }
        if (((android.telephony.RadioAccessFamily.EVDO | android.telephony.RadioAccessFamily.HS) | (android.telephony.RadioAccessFamily.WCDMA & raf)) > 0) {
            return android.telephony.TelephonyManager.NETWORK_CLASS_3_G;
        }
        if ((android.telephony.RadioAccessFamily.GSM | (android.telephony.RadioAccessFamily.CDMA & raf)) > 0) {
            return android.telephony.TelephonyManager.NETWORK_CLASS_2_G;
        }
        return android.telephony.TelephonyManager.NETWORK_CLASS_UNKNOWN;
    }

    public static int getNetworkTypeFromRaf(int raf) {
        int type;
        raf = android.telephony.RadioAccessFamily.getAdjustedRaf(raf);
        switch (raf) {
            case android.telephony.RadioAccessFamily.GSM | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_WCDMA_PREF;
                break;
            case android.telephony.RadioAccessFamily.GSM :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_GSM_ONLY;
                break;
            case android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_WCDMA_ONLY;
                break;
            case android.telephony.RadioAccessFamily.CDMA | android.telephony.RadioAccessFamily.EVDO :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_CDMA;
                break;
            case (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_CDMA_EVDO;
                break;
            case (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_GSM_WCDMA;
                break;
            case (((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA;
                break;
            case android.telephony.RadioAccessFamily.LTE :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_ONLY;
                break;
            case android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_WCDMA;
                break;
            case android.telephony.RadioAccessFamily.CDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_CDMA_NO_EVDO;
                break;
            case android.telephony.RadioAccessFamily.EVDO :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_EVDO_NO_CDMA;
                break;
            case ((android.telephony.RadioAccessFamily.GSM | android.telephony.RadioAccessFamily.WCDMA) | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_GLOBAL;
                break;
            case android.telephony.RadioAccessFamily.RAF_TD_SCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_ONLY;
                break;
            case android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_WCDMA;
                break;
            case android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA;
                break;
            case android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.GSM :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_GSM;
                break;
            case (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.GSM :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_GSM;
                break;
            case (android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_GSM_WCDMA;
                break;
            case (android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_WCDMA;
                break;
            case ((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_GSM_WCDMA;
                break;
            case (((android.telephony.RadioAccessFamily.RAF_TD_SCDMA | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA;
                break;
            case ((((android.telephony.RadioAccessFamily.LTE | android.telephony.RadioAccessFamily.RAF_TD_SCDMA) | android.telephony.RadioAccessFamily.CDMA) | android.telephony.RadioAccessFamily.EVDO) | android.telephony.RadioAccessFamily.GSM) | android.telephony.RadioAccessFamily.WCDMA :
                type = com.android.internal.telephony.RILConstants.NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA;
                break;
            default :
                type = com.android.internal.telephony.RILConstants.PREFERRED_NETWORK_MODE;
                break;
        }
        return type;
    }

    public static int singleRafTypeFromString(java.lang.String rafString) {
        switch (rafString) {
            case "GPRS" :
                return android.telephony.RadioAccessFamily.RAF_GPRS;
            case "EDGE" :
                return android.telephony.RadioAccessFamily.RAF_EDGE;
            case "UMTS" :
                return android.telephony.RadioAccessFamily.RAF_UMTS;
            case "IS95A" :
                return android.telephony.RadioAccessFamily.RAF_IS95A;
            case "IS95B" :
                return android.telephony.RadioAccessFamily.RAF_IS95B;
            case "1XRTT" :
                return android.telephony.RadioAccessFamily.RAF_1xRTT;
            case "EVDO_0" :
                return android.telephony.RadioAccessFamily.RAF_EVDO_0;
            case "EVDO_A" :
                return android.telephony.RadioAccessFamily.RAF_EVDO_A;
            case "HSDPA" :
                return android.telephony.RadioAccessFamily.RAF_HSDPA;
            case "HSUPA" :
                return android.telephony.RadioAccessFamily.RAF_HSUPA;
            case "HSPA" :
                return android.telephony.RadioAccessFamily.RAF_HSPA;
            case "EVDO_B" :
                return android.telephony.RadioAccessFamily.RAF_EVDO_B;
            case "EHRPD" :
                return android.telephony.RadioAccessFamily.RAF_EHRPD;
            case "LTE" :
                return android.telephony.RadioAccessFamily.RAF_LTE;
            case "HSPAP" :
                return android.telephony.RadioAccessFamily.RAF_HSPAP;
            case "GSM" :
                return android.telephony.RadioAccessFamily.RAF_GSM;
            case "TD_SCDMA" :
                return android.telephony.RadioAccessFamily.RAF_TD_SCDMA;
            case "HS" :
                return android.telephony.RadioAccessFamily.HS;
            case "CDMA" :
                return android.telephony.RadioAccessFamily.CDMA;
            case "EVDO" :
                return android.telephony.RadioAccessFamily.EVDO;
            case "WCDMA" :
                return android.telephony.RadioAccessFamily.WCDMA;
            case "LTE_CA" :
                return android.telephony.RadioAccessFamily.RAF_LTE_CA;
            default :
                return android.telephony.RadioAccessFamily.RAF_UNKNOWN;
        }
    }

    public static int rafTypeFromString(java.lang.String rafList) {
        rafList = rafList.toUpperCase();
        java.lang.String[] rafs = rafList.split("\\|");
        int result = 0;
        for (java.lang.String raf : rafs) {
            int rafType = android.telephony.RadioAccessFamily.singleRafTypeFromString(raf.trim());
            if (rafType == android.telephony.RadioAccessFamily.RAF_UNKNOWN)
                return rafType;

            result |= rafType;
        }
        return result;
    }
}

