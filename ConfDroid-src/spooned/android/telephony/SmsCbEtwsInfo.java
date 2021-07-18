/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Contains information elements for a GSM or UMTS ETWS warning notification.
 * Supported values for each element are defined in 3GPP TS 23.041.
 *
 * {@hide }
 */
public class SmsCbEtwsInfo implements android.os.Parcelable {
    /**
     * ETWS warning type for earthquake.
     */
    public static final int ETWS_WARNING_TYPE_EARTHQUAKE = 0x0;

    /**
     * ETWS warning type for tsunami.
     */
    public static final int ETWS_WARNING_TYPE_TSUNAMI = 0x1;

    /**
     * ETWS warning type for earthquake and tsunami.
     */
    public static final int ETWS_WARNING_TYPE_EARTHQUAKE_AND_TSUNAMI = 0x2;

    /**
     * ETWS warning type for test messages.
     */
    public static final int ETWS_WARNING_TYPE_TEST_MESSAGE = 0x3;

    /**
     * ETWS warning type for other emergency types.
     */
    public static final int ETWS_WARNING_TYPE_OTHER_EMERGENCY = 0x4;

    /**
     * Unknown ETWS warning type.
     */
    public static final int ETWS_WARNING_TYPE_UNKNOWN = -1;

    /**
     * One of the ETWS warning type constants defined in this class.
     */
    private final int mWarningType;

    /**
     * Whether or not to activate the emergency user alert tone and vibration.
     */
    private final boolean mEmergencyUserAlert;

    /**
     * Whether or not to activate a popup alert.
     */
    private final boolean mActivatePopup;

    /**
     * Whether ETWS primary message or not/
     */
    private final boolean mPrimary;

    /**
     * 50-byte security information (ETWS primary notification for GSM only). As of Release 10,
     * 3GPP TS 23.041 states that the UE shall ignore the ETWS primary notification timestamp
     * and digital signature if received. Therefore it is treated as a raw byte array and
     * parceled with the broadcast intent if present, but the timestamp is only computed if an
     * application asks for the individual components.
     */
    private final byte[] mWarningSecurityInformation;

    /**
     * Create a new SmsCbEtwsInfo object with the specified values.
     */
    public SmsCbEtwsInfo(int warningType, boolean emergencyUserAlert, boolean activatePopup, boolean primary, byte[] warningSecurityInformation) {
        mWarningType = warningType;
        mEmergencyUserAlert = emergencyUserAlert;
        mActivatePopup = activatePopup;
        mPrimary = primary;
        mWarningSecurityInformation = warningSecurityInformation;
    }

    /**
     * Create a new SmsCbEtwsInfo object from a Parcel.
     */
    SmsCbEtwsInfo(android.os.Parcel in) {
        mWarningType = in.readInt();
        mEmergencyUserAlert = in.readInt() != 0;
        mActivatePopup = in.readInt() != 0;
        mPrimary = in.readInt() != 0;
        mWarningSecurityInformation = in.createByteArray();
    }

    /**
     * Flatten this object into a Parcel.
     *
     * @param dest
     * 		The Parcel in which the object should be written.
     * @param flags
     * 		Additional flags about how the object should be written (ignored).
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mWarningType);
        dest.writeInt(mEmergencyUserAlert ? 1 : 0);
        dest.writeInt(mActivatePopup ? 1 : 0);
        dest.writeInt(mPrimary ? 1 : 0);
        dest.writeByteArray(mWarningSecurityInformation);
    }

    /**
     * Returns the ETWS warning type.
     *
     * @return a warning type such as {@link #ETWS_WARNING_TYPE_EARTHQUAKE}
     */
    public int getWarningType() {
        return mWarningType;
    }

    /**
     * Returns the ETWS emergency user alert flag.
     *
     * @return true to notify terminal to activate emergency user alert; false otherwise
     */
    public boolean isEmergencyUserAlert() {
        return mEmergencyUserAlert;
    }

    /**
     * Returns the ETWS activate popup flag.
     *
     * @return true to notify terminal to activate display popup; false otherwise
     */
    public boolean isPopupAlert() {
        return mActivatePopup;
    }

    /**
     * Returns the ETWS format flag.
     *
     * @return true if the message is primary message, otherwise secondary message
     */
    public boolean isPrimary() {
        return mPrimary;
    }

    /**
     * Returns the Warning-Security-Information timestamp (GSM primary notifications only).
     * As of Release 10, 3GPP TS 23.041 states that the UE shall ignore this value if received.
     *
     * @return a UTC timestamp in System.currentTimeMillis() format, or 0 if not present
     */
    public long getPrimaryNotificationTimestamp() {
        if ((mWarningSecurityInformation == null) || (mWarningSecurityInformation.length < 7)) {
            return 0;
        }
        int year = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[0]);
        int month = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[1]);
        int day = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[2]);
        int hour = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[3]);
        int minute = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[4]);
        int second = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[5]);
        // For the timezone, the most significant bit of the
        // least significant nibble is the sign byte
        // (meaning the max range of this field is 79 quarter-hours,
        // which is more than enough)
        byte tzByte = mWarningSecurityInformation[6];
        // Mask out sign bit.
        int timezoneOffset = com.android.internal.telephony.uicc.IccUtils.gsmBcdByteToInt(((byte) (tzByte & (~0x8))));
        timezoneOffset = ((tzByte & 0x8) == 0) ? timezoneOffset : -timezoneOffset;
        android.text.format.Time time = new android.text.format.Time(android.text.format.Time.TIMEZONE_UTC);
        // We only need to support years above 2000.
        time.year = year + 2000;
        time.month = month - 1;
        time.monthDay = day;
        time.hour = hour;
        time.minute = minute;
        time.second = second;
        // Timezone offset is in quarter hours.
        return time.toMillis(true) - (((timezoneOffset * 15) * 60) * 1000);
    }

    /**
     * Returns the digital signature (GSM primary notifications only). As of Release 10,
     * 3GPP TS 23.041 states that the UE shall ignore this value if received.
     *
     * @return a byte array containing a copy of the primary notification digital signature
     */
    public byte[] getPrimaryNotificationSignature() {
        if ((mWarningSecurityInformation == null) || (mWarningSecurityInformation.length < 50)) {
            return null;
        }
        return java.util.Arrays.copyOfRange(mWarningSecurityInformation, 7, 50);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("SmsCbEtwsInfo{warningType=" + mWarningType) + ", emergencyUserAlert=") + mEmergencyUserAlert) + ", activatePopup=") + mActivatePopup) + '}';
    }

    /**
     * Describe the kinds of special objects contained in the marshalled representation.
     *
     * @return a bitmask indicating this Parcelable contains no special objects
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Creator for unparcelling objects.
     */
    public static final android.os.Parcelable.Creator<android.telephony.SmsCbEtwsInfo> CREATOR = new android.os.Parcelable.Creator<android.telephony.SmsCbEtwsInfo>() {
        @java.lang.Override
        public android.telephony.SmsCbEtwsInfo createFromParcel(android.os.Parcel in) {
            return new android.telephony.SmsCbEtwsInfo(in);
        }

        @java.lang.Override
        public android.telephony.SmsCbEtwsInfo[] newArray(int size) {
            return new android.telephony.SmsCbEtwsInfo[size];
        }
    };
}

