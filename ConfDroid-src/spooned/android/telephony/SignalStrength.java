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
 * Contains phone signal strength related information.
 */
public class SignalStrength implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "SignalStrength";

    private static final boolean DBG = false;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_POOR = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_MODERATE = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_GOOD = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_GREAT = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int NUM_SIGNAL_STRENGTH_BINS = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String[] SIGNAL_STRENGTH_NAMES = new java.lang.String[]{ "none", "poor", "moderate", "good", "great" };

    /**
     *
     *
     * @unknown 
     */
    // Use int max, as -1 is a valid value in signal strength
    public static final int INVALID = 0x7fffffff;

    private static final int RSRP_THRESH_TYPE_STRICT = 0;

    private static final int[] RSRP_THRESH_STRICT = new int[]{ -140, -115, -105, -95, -85, -44 };

    private static final int[] RSRP_THRESH_LENIENT = new int[]{ -140, -128, -118, -108, -98, -44 };

    private int mGsmSignalStrength;// Valid values are (0-31, 99) as defined in TS 27.007 8.5


    private int mGsmBitErrorRate;// bit error rate (0-7, 99) as defined in TS 27.007 8.5


    private int mCdmaDbm;// This value is the RSSI value


    private int mCdmaEcio;// This value is the Ec/Io


    private int mEvdoDbm;// This value is the EVDO RSSI value


    private int mEvdoEcio;// This value is the EVDO Ec/Io


    private int mEvdoSnr;// Valid values are 0-8.  8 is the highest signal to noise ratio


    private int mLteSignalStrength;

    private int mLteRsrp;

    private int mLteRsrq;

    private int mLteRssnr;

    private int mLteCqi;

    private int mTdScdmaRscp;

    private boolean isGsm;// This value is set by the ServiceStateTracker onSignalStrengthResult


    /**
     * Create a new SignalStrength from a intent notifier Bundle
     *
     * This method is used by PhoneStateIntentReceiver and maybe by
     * external applications.
     *
     * @param m
     * 		Bundle from intent notifier
     * @return newly created SignalStrength
     * @unknown 
     */
    public static android.telephony.SignalStrength newFromBundle(android.os.Bundle m) {
        android.telephony.SignalStrength ret;
        ret = new android.telephony.SignalStrength();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    /**
     * Empty constructor
     *
     * @unknown 
     */
    public SignalStrength() {
        mGsmSignalStrength = 99;
        mGsmBitErrorRate = -1;
        mCdmaDbm = -1;
        mCdmaEcio = -1;
        mEvdoDbm = -1;
        mEvdoEcio = -1;
        mEvdoSnr = -1;
        mLteSignalStrength = 99;
        mLteRsrp = android.telephony.SignalStrength.INVALID;
        mLteRsrq = android.telephony.SignalStrength.INVALID;
        mLteRssnr = android.telephony.SignalStrength.INVALID;
        mLteCqi = android.telephony.SignalStrength.INVALID;
        mTdScdmaRscp = android.telephony.SignalStrength.INVALID;
        isGsm = true;
    }

    /**
     * This constructor is used to create SignalStrength with default
     * values and set the isGsmFlag with the value passed in the input
     *
     * @param gsmFlag
     * 		true if Gsm Phone,false if Cdma phone
     * @return newly created SignalStrength
     * @unknown 
     */
    public SignalStrength(boolean gsmFlag) {
        mGsmSignalStrength = 99;
        mGsmBitErrorRate = -1;
        mCdmaDbm = -1;
        mCdmaEcio = -1;
        mEvdoDbm = -1;
        mEvdoEcio = -1;
        mEvdoSnr = -1;
        mLteSignalStrength = 99;
        mLteRsrp = android.telephony.SignalStrength.INVALID;
        mLteRsrq = android.telephony.SignalStrength.INVALID;
        mLteRssnr = android.telephony.SignalStrength.INVALID;
        mLteCqi = android.telephony.SignalStrength.INVALID;
        mTdScdmaRscp = android.telephony.SignalStrength.INVALID;
        isGsm = gsmFlag;
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
        mTdScdmaRscp = tdScdmaRscp;
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, gsmFlag);
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source SignalStrength
     * @unknown 
     */
    public SignalStrength(android.telephony.SignalStrength s) {
        copyFrom(s);
    }

    /**
     * Initialize gsm/cdma values, sets lte values to defaults.
     *
     * @param gsmSignalStrength
     * 		
     * @param gsmBitErrorRate
     * 		
     * @param cdmaDbm
     * 		
     * @param cdmaEcio
     * 		
     * @param evdoDbm
     * 		
     * @param evdoEcio
     * 		
     * @param evdoSnr
     * 		
     * @param gsm
     * 		
     * @unknown 
     */
    public void initialize(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, boolean gsm) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, android.telephony.SignalStrength.INVALID, gsm);
    }

    /**
     * Initialize all the values
     *
     * @param gsmSignalStrength
     * 		
     * @param gsmBitErrorRate
     * 		
     * @param cdmaDbm
     * 		
     * @param cdmaEcio
     * 		
     * @param evdoDbm
     * 		
     * @param evdoEcio
     * 		
     * @param evdoSnr
     * 		
     * @param lteSignalStrength
     * 		
     * @param lteRsrp
     * 		
     * @param lteRsrq
     * 		
     * @param lteRssnr
     * 		
     * @param lteCqi
     * 		
     * @param gsm
     * 		
     * @unknown 
     */
    public void initialize(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, boolean gsm) {
        mGsmSignalStrength = gsmSignalStrength;
        mGsmBitErrorRate = gsmBitErrorRate;
        mCdmaDbm = cdmaDbm;
        mCdmaEcio = cdmaEcio;
        mEvdoDbm = evdoDbm;
        mEvdoEcio = evdoEcio;
        mEvdoSnr = evdoSnr;
        mLteSignalStrength = lteSignalStrength;
        mLteRsrp = lteRsrp;
        mLteRsrq = lteRsrq;
        mLteRssnr = lteRssnr;
        mLteCqi = lteCqi;
        mTdScdmaRscp = android.telephony.SignalStrength.INVALID;
        isGsm = gsm;
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("initialize: " + toString());

    }

    /**
     *
     *
     * @unknown 
     */
    protected void copyFrom(android.telephony.SignalStrength s) {
        mGsmSignalStrength = s.mGsmSignalStrength;
        mGsmBitErrorRate = s.mGsmBitErrorRate;
        mCdmaDbm = s.mCdmaDbm;
        mCdmaEcio = s.mCdmaEcio;
        mEvdoDbm = s.mEvdoDbm;
        mEvdoEcio = s.mEvdoEcio;
        mEvdoSnr = s.mEvdoSnr;
        mLteSignalStrength = s.mLteSignalStrength;
        mLteRsrp = s.mLteRsrp;
        mLteRsrq = s.mLteRsrq;
        mLteRssnr = s.mLteRssnr;
        mLteCqi = s.mLteCqi;
        mTdScdmaRscp = s.mTdScdmaRscp;
        isGsm = s.isGsm;
    }

    /**
     * Construct a SignalStrength object from the given parcel.
     *
     * @unknown 
     */
    public SignalStrength(android.os.Parcel in) {
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("Size of signalstrength parcel:" + in.dataSize());

        mGsmSignalStrength = in.readInt();
        mGsmBitErrorRate = in.readInt();
        mCdmaDbm = in.readInt();
        mCdmaEcio = in.readInt();
        mEvdoDbm = in.readInt();
        mEvdoEcio = in.readInt();
        mEvdoSnr = in.readInt();
        mLteSignalStrength = in.readInt();
        mLteRsrp = in.readInt();
        mLteRsrq = in.readInt();
        mLteRssnr = in.readInt();
        mLteCqi = in.readInt();
        mTdScdmaRscp = in.readInt();
        isGsm = in.readInt() != 0;
    }

    /**
     * Make a SignalStrength object from the given parcel as passed up by
     * the ril which does not have isGsm. isGsm will be changed by ServiceStateTracker
     * so the default is a don't care.
     *
     * @unknown 
     */
    public static android.telephony.SignalStrength makeSignalStrengthFromRilParcel(android.os.Parcel in) {
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("Size of signalstrength parcel:" + in.dataSize());

        android.telephony.SignalStrength ss = new android.telephony.SignalStrength();
        ss.mGsmSignalStrength = in.readInt();
        ss.mGsmBitErrorRate = in.readInt();
        ss.mCdmaDbm = in.readInt();
        ss.mCdmaEcio = in.readInt();
        ss.mEvdoDbm = in.readInt();
        ss.mEvdoEcio = in.readInt();
        ss.mEvdoSnr = in.readInt();
        ss.mLteSignalStrength = in.readInt();
        ss.mLteRsrp = in.readInt();
        ss.mLteRsrq = in.readInt();
        ss.mLteRssnr = in.readInt();
        ss.mLteCqi = in.readInt();
        ss.mTdScdmaRscp = in.readInt();
        return ss;
    }

    /**
     * {@link Parcelable#writeToParcel}
     */
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mGsmSignalStrength);
        out.writeInt(mGsmBitErrorRate);
        out.writeInt(mCdmaDbm);
        out.writeInt(mCdmaEcio);
        out.writeInt(mEvdoDbm);
        out.writeInt(mEvdoEcio);
        out.writeInt(mEvdoSnr);
        out.writeInt(mLteSignalStrength);
        out.writeInt(mLteRsrp);
        out.writeInt(mLteRsrq);
        out.writeInt(mLteRssnr);
        out.writeInt(mLteCqi);
        out.writeInt(mTdScdmaRscp);
        out.writeInt(isGsm ? 1 : 0);
    }

    /**
     * {@link Parcelable#describeContents}
     */
    public int describeContents() {
        return 0;
    }

    /**
     * {@link Parcelable.Creator}
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.telephony.SignalStrength> CREATOR = new android.os.Parcelable.Creator() {
        public android.telephony.SignalStrength createFromParcel(android.os.Parcel in) {
            return new android.telephony.SignalStrength(in);
        }

        public android.telephony.SignalStrength[] newArray(int size) {
            return new android.telephony.SignalStrength[size];
        }
    };

    /**
     * Validate the individual signal strength fields as per the range
     * specified in ril.h
     * Set to invalid any field that is not in the valid range
     * Cdma, evdo, lte rsrp & rsrq values are sign converted
     * when received from ril interface
     *
     * @return Valid values for all signalstrength fields
     * @unknown 
     */
    public void validateInput() {
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("Signal before validate=" + this);

        // TS 27.007 8.5
        mGsmSignalStrength = (mGsmSignalStrength >= 0) ? mGsmSignalStrength : 99;
        // BER no change;
        mCdmaDbm = (mCdmaDbm > 0) ? -mCdmaDbm : -120;
        mCdmaEcio = (mCdmaEcio > 0) ? -mCdmaEcio : -160;
        mEvdoDbm = (mEvdoDbm > 0) ? -mEvdoDbm : -120;
        mEvdoEcio = (mEvdoEcio >= 0) ? -mEvdoEcio : -1;
        mEvdoSnr = ((mEvdoSnr > 0) && (mEvdoSnr <= 8)) ? mEvdoSnr : -1;
        // TS 36.214 Physical Layer Section 5.1.3, TS 36.331 RRC
        mLteSignalStrength = (mLteSignalStrength >= 0) ? mLteSignalStrength : 99;
        mLteRsrp = ((mLteRsrp >= 44) && (mLteRsrp <= 140)) ? -mLteRsrp : android.telephony.SignalStrength.INVALID;
        mLteRsrq = ((mLteRsrq >= 3) && (mLteRsrq <= 20)) ? -mLteRsrq : android.telephony.SignalStrength.INVALID;
        mLteRssnr = ((mLteRssnr >= (-200)) && (mLteRssnr <= 300)) ? mLteRssnr : android.telephony.SignalStrength.INVALID;
        mTdScdmaRscp = ((mTdScdmaRscp >= 25) && (mTdScdmaRscp <= 120)) ? -mTdScdmaRscp : android.telephony.SignalStrength.INVALID;
        // Cqi no change
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("Signal after validate=" + this);

    }

    /**
     *
     *
     * @param true
     * 		- Gsm, Lte phones
     * 		false - Cdma phones
     * 		
     * 		Used by voice phone to set the isGsm
     * 		flag
     * @unknown 
     */
    public void setGsm(boolean gsmFlag) {
        isGsm = gsmFlag;
    }

    /**
     * Get the GSM Signal Strength, valid values are (0-31, 99) as defined in TS
     * 27.007 8.5
     */
    public int getGsmSignalStrength() {
        return this.mGsmSignalStrength;
    }

    /**
     * Get the GSM bit error rate (0-7, 99) as defined in TS 27.007 8.5
     */
    public int getGsmBitErrorRate() {
        return this.mGsmBitErrorRate;
    }

    /**
     * Get the CDMA RSSI value in dBm
     */
    public int getCdmaDbm() {
        return this.mCdmaDbm;
    }

    /**
     * Get the CDMA Ec/Io value in dB*10
     */
    public int getCdmaEcio() {
        return this.mCdmaEcio;
    }

    /**
     * Get the EVDO RSSI value in dBm
     */
    public int getEvdoDbm() {
        return this.mEvdoDbm;
    }

    /**
     * Get the EVDO Ec/Io value in dB*10
     */
    public int getEvdoEcio() {
        return this.mEvdoEcio;
    }

    /**
     * Get the signal to noise ratio. Valid values are 0-8. 8 is the highest.
     */
    public int getEvdoSnr() {
        return this.mEvdoSnr;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLteSignalStrength() {
        return mLteSignalStrength;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLteRsrp() {
        return mLteRsrp;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLteRsrq() {
        return mLteRsrq;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLteRssnr() {
        return mLteRssnr;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLteCqi() {
        return mLteCqi;
    }

    /**
     * Retrieve an abstract level value for the overall signal strength.
     *
     * @return a single integer from 0 to 4 representing the general signal quality.
    This may take into account many different radio technology inputs.
    0 represents very poor signal strength
    while 4 represents a very strong signal strength.
     */
    public int getLevel() {
        int level = 0;
        if (isGsm) {
            level = getLteLevel();
            if (level == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                level = getTdScdmaLevel();
                if (level == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                    level = getGsmLevel();
                }
            }
        } else {
            int cdmaLevel = getCdmaLevel();
            int evdoLevel = getEvdoLevel();
            if (evdoLevel == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                /* We don't know evdo, use cdma */
                level = cdmaLevel;
            } else
                if (cdmaLevel == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                    /* We don't know cdma, use evdo */
                    level = evdoLevel;
                } else {
                    /* We know both, use the lowest level */
                    level = (cdmaLevel < evdoLevel) ? cdmaLevel : evdoLevel;
                }

        }
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getLevel=" + level);

        return level;
    }

    /**
     * Get the signal level as an asu value between 0..31, 99 is unknown
     *
     * @unknown 
     */
    public int getAsuLevel() {
        int asuLevel = 0;
        if (isGsm) {
            if (getLteLevel() == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                if (getTdScdmaLevel() == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                    asuLevel = getGsmAsuLevel();
                } else {
                    asuLevel = getTdScdmaAsuLevel();
                }
            } else {
                asuLevel = getLteAsuLevel();
            }
        } else {
            int cdmaAsuLevel = getCdmaAsuLevel();
            int evdoAsuLevel = getEvdoAsuLevel();
            if (evdoAsuLevel == 0) {
                /* We don't know evdo use, cdma */
                asuLevel = cdmaAsuLevel;
            } else
                if (cdmaAsuLevel == 0) {
                    /* We don't know cdma use, evdo */
                    asuLevel = evdoAsuLevel;
                } else {
                    /* We know both, use the lowest level */
                    asuLevel = (cdmaAsuLevel < evdoAsuLevel) ? cdmaAsuLevel : evdoAsuLevel;
                }

        }
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getAsuLevel=" + asuLevel);

        return asuLevel;
    }

    /**
     * Get the signal strength as dBm
     *
     * @unknown 
     */
    public int getDbm() {
        int dBm = android.telephony.SignalStrength.INVALID;
        if (isGsm()) {
            dBm = getLteDbm();
            if (dBm == android.telephony.SignalStrength.INVALID) {
                if (getTdScdmaLevel() == android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                    dBm = getGsmDbm();
                } else {
                    dBm = getTdScdmaDbm();
                }
            }
        } else {
            int cdmaDbm = getCdmaDbm();
            int evdoDbm = getEvdoDbm();
            return evdoDbm == (-120) ? cdmaDbm : cdmaDbm == (-120) ? evdoDbm : cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm;
        }
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getDbm=" + dBm);

        return dBm;
    }

    /**
     * Get Gsm signal strength as dBm
     *
     * @unknown 
     */
    public int getGsmDbm() {
        int dBm;
        int gsmSignalStrength = getGsmSignalStrength();
        int asu = (gsmSignalStrength == 99) ? -1 : gsmSignalStrength;
        if (asu != (-1)) {
            dBm = (-113) + (2 * asu);
        } else {
            dBm = -1;
        }
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getGsmDbm=" + dBm);

        return dBm;
    }

    /**
     * Get gsm as level 0..4
     *
     * @unknown 
     */
    public int getGsmLevel() {
        int level;
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int asu = getGsmSignalStrength();
        if ((asu <= 2) || (asu == 99))
            level = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (asu >= 12)
                level = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (asu >= 8)
                    level = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (asu >= 5)
                        level = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        level = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;




        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getGsmLevel=" + level);

        return level;
    }

    /**
     * Get the gsm signal level as an asu value between 0..31, 99 is unknown
     *
     * @unknown 
     */
    public int getGsmAsuLevel() {
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int level = getGsmSignalStrength();
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getGsmAsuLevel=" + level);

        return level;
    }

    /**
     * Get cdma as level 0..4
     *
     * @unknown 
     */
    public int getCdmaLevel() {
        final int cdmaDbm = getCdmaDbm();
        final int cdmaEcio = getCdmaEcio();
        int levelDbm;
        int levelEcio;
        if (cdmaDbm >= (-75))
            levelDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
        else
            if (cdmaDbm >= (-85))
                levelDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
            else
                if (cdmaDbm >= (-95))
                    levelDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                else
                    if (cdmaDbm >= (-100))
                        levelDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                    else
                        levelDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;




        // Ec/Io are in dB*10
        if (cdmaEcio >= (-90))
            levelEcio = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
        else
            if (cdmaEcio >= (-110))
                levelEcio = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
            else
                if (cdmaEcio >= (-130))
                    levelEcio = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                else
                    if (cdmaEcio >= (-150))
                        levelEcio = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                    else
                        levelEcio = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;




        int level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getCdmaLevel=" + level);

        return level;
    }

    /**
     * Get the cdma signal level as an asu value between 0..31, 99 is unknown
     *
     * @unknown 
     */
    public int getCdmaAsuLevel() {
        final int cdmaDbm = getCdmaDbm();
        final int cdmaEcio = getCdmaEcio();
        int cdmaAsuLevel;
        int ecioAsuLevel;
        if (cdmaDbm >= (-75))
            cdmaAsuLevel = 16;
        else
            if (cdmaDbm >= (-82))
                cdmaAsuLevel = 8;
            else
                if (cdmaDbm >= (-90))
                    cdmaAsuLevel = 4;
                else
                    if (cdmaDbm >= (-95))
                        cdmaAsuLevel = 2;
                    else
                        if (cdmaDbm >= (-100))
                            cdmaAsuLevel = 1;
                        else
                            cdmaAsuLevel = 99;





        // Ec/Io are in dB*10
        if (cdmaEcio >= (-90))
            ecioAsuLevel = 16;
        else
            if (cdmaEcio >= (-100))
                ecioAsuLevel = 8;
            else
                if (cdmaEcio >= (-115))
                    ecioAsuLevel = 4;
                else
                    if (cdmaEcio >= (-130))
                        ecioAsuLevel = 2;
                    else
                        if (cdmaEcio >= (-150))
                            ecioAsuLevel = 1;
                        else
                            ecioAsuLevel = 99;





        int level = (cdmaAsuLevel < ecioAsuLevel) ? cdmaAsuLevel : ecioAsuLevel;
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getCdmaAsuLevel=" + level);

        return level;
    }

    /**
     * Get Evdo as level 0..4
     *
     * @unknown 
     */
    public int getEvdoLevel() {
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        int levelEvdoDbm;
        int levelEvdoSnr;
        if (evdoDbm >= (-65))
            levelEvdoDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
        else
            if (evdoDbm >= (-75))
                levelEvdoDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
            else
                if (evdoDbm >= (-90))
                    levelEvdoDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                else
                    if (evdoDbm >= (-105))
                        levelEvdoDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                    else
                        levelEvdoDbm = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;




        if (evdoSnr >= 7)
            levelEvdoSnr = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
        else
            if (evdoSnr >= 5)
                levelEvdoSnr = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
            else
                if (evdoSnr >= 3)
                    levelEvdoSnr = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                else
                    if (evdoSnr >= 1)
                        levelEvdoSnr = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                    else
                        levelEvdoSnr = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;




        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getEvdoLevel=" + level);

        return level;
    }

    /**
     * Get the evdo signal level as an asu value between 0..31, 99 is unknown
     *
     * @unknown 
     */
    public int getEvdoAsuLevel() {
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        int levelEvdoDbm;
        int levelEvdoSnr;
        if (evdoDbm >= (-65))
            levelEvdoDbm = 16;
        else
            if (evdoDbm >= (-75))
                levelEvdoDbm = 8;
            else
                if (evdoDbm >= (-85))
                    levelEvdoDbm = 4;
                else
                    if (evdoDbm >= (-95))
                        levelEvdoDbm = 2;
                    else
                        if (evdoDbm >= (-105))
                            levelEvdoDbm = 1;
                        else
                            levelEvdoDbm = 99;





        if (evdoSnr >= 7)
            levelEvdoSnr = 16;
        else
            if (evdoSnr >= 6)
                levelEvdoSnr = 8;
            else
                if (evdoSnr >= 5)
                    levelEvdoSnr = 4;
                else
                    if (evdoSnr >= 3)
                        levelEvdoSnr = 2;
                    else
                        if (evdoSnr >= 1)
                            levelEvdoSnr = 1;
                        else
                            levelEvdoSnr = 99;





        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getEvdoAsuLevel=" + level);

        return level;
    }

    /**
     * Get LTE as dBm
     *
     * @unknown 
     */
    public int getLteDbm() {
        return mLteRsrp;
    }

    /**
     * Get LTE as level 0..4
     *
     * @unknown 
     */
    public int getLteLevel() {
        /* TS 36.214 Physical Layer Section 5.1.3 TS 36.331 RRC RSSI = received
        signal + noise RSRP = reference signal dBm RSRQ = quality of signal
        dB= Number of Resource blocksxRSRP/RSSI SNR = gain=signal/noise ratio
        = -10log P1/P2 dB
         */
        int rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        int rsrpIconLevel = -1;
        int snrIconLevel = -1;
        int rsrpThreshType = android.content.res.Resources.getSystem().getInteger(com.android.internal.R.integer.config_LTE_RSRP_threshold_type);
        int[] threshRsrp;
        if (rsrpThreshType == android.telephony.SignalStrength.RSRP_THRESH_TYPE_STRICT) {
            threshRsrp = android.telephony.SignalStrength.RSRP_THRESH_STRICT;
        } else {
            threshRsrp = android.telephony.SignalStrength.RSRP_THRESH_LENIENT;
        }
        if (mLteRsrp > threshRsrp[5])
            rsrpIconLevel = -1;
        else
            if (mLteRsrp >= threshRsrp[4])
                rsrpIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (mLteRsrp >= threshRsrp[3])
                    rsrpIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (mLteRsrp >= threshRsrp[2])
                        rsrpIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (mLteRsrp >= threshRsrp[1])
                            rsrpIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            if (mLteRsrp >= threshRsrp[0])
                                rsrpIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;






        /* Values are -200 dB to +300 (SNR*10dB) RS_SNR >= 13.0 dB =>4 bars 4.5
        dB <= RS_SNR < 13.0 dB => 3 bars 1.0 dB <= RS_SNR < 4.5 dB => 2 bars
        -3.0 dB <= RS_SNR < 1.0 dB 1 bar RS_SNR < -3.0 dB/No Service Antenna
        Icon Only
         */
        if (mLteRssnr > 300)
            snrIconLevel = -1;
        else
            if (mLteRssnr >= 130)
                snrIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (mLteRssnr >= 45)
                    snrIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (mLteRssnr >= 10)
                        snrIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (mLteRssnr >= (-30))
                            snrIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            if (mLteRssnr >= (-200))
                                snrIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;






        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log((((((("getLTELevel - rsrp:" + mLteRsrp) + " snr:") + mLteRssnr) + " rsrpIconLevel:") + rsrpIconLevel) + " snrIconLevel:") + snrIconLevel);

        /* Choose a measurement type to use for notification */
        if ((snrIconLevel != (-1)) && (rsrpIconLevel != (-1))) {
            /* The number of bars displayed shall be the smaller of the bars
            associated with LTE RSRP and the bars associated with the LTE
            RS_SNR
             */
            return rsrpIconLevel < snrIconLevel ? rsrpIconLevel : snrIconLevel;
        }
        if (snrIconLevel != (-1))
            return snrIconLevel;

        if (rsrpIconLevel != (-1))
            return rsrpIconLevel;

        /* Valid values are (0-63, 99) as defined in TS 36.331 */
        if (mLteSignalStrength > 63)
            rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (mLteSignalStrength >= 12)
                rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (mLteSignalStrength >= 8)
                    rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (mLteSignalStrength >= 5)
                        rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (mLteSignalStrength >= 0)
                            rssiIconLevel = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;





        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log((("getLTELevel - rssi:" + mLteSignalStrength) + " rssiIconLevel:") + rssiIconLevel);

        return rssiIconLevel;
    }

    /**
     * Get the LTE signal level as an asu value between 0..97, 99 is unknown
     * Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
     *
     * @unknown 
     */
    public int getLteAsuLevel() {
        int lteAsuLevel = 99;
        int lteDbm = getLteDbm();
        /* 3GPP 27.007 (Ver 10.3.0) Sec 8.69
        0   -140 dBm or less
        1   -139 dBm
        2...96  -138... -44 dBm
        97  -43 dBm or greater
        255 not known or not detectable
         */
        /* validateInput will always give a valid range between -140 t0 -44 as
        per ril.h. so RSRP >= -43 & <-140 will fall under asu level 255
        and not 97 or 0
         */
        if (lteDbm == android.telephony.SignalStrength.INVALID)
            lteAsuLevel = 255;
        else
            lteAsuLevel = lteDbm + 140;

        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("Lte Asu level: " + lteAsuLevel);

        return lteAsuLevel;
    }

    /**
     *
     *
     * @return true if this is for GSM
     */
    public boolean isGsm() {
        return this.isGsm;
    }

    /**
     *
     *
     * @return get TD_SCDMA dbm
     * @unknown 
     */
    public int getTdScdmaDbm() {
        return this.mTdScdmaRscp;
    }

    /**
     * Get TD-SCDMA as level 0..4
     * Range : 25 to 120
     * INT_MAX: 0x7FFFFFFF denotes invalid value
     * Reference: 3GPP TS 25.123, section 9.1.1.1
     *
     * @unknown 
     */
    public int getTdScdmaLevel() {
        final int tdScdmaDbm = getTdScdmaDbm();
        int level;
        if ((tdScdmaDbm > (-25)) || (tdScdmaDbm == android.telephony.SignalStrength.INVALID))
            level = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (tdScdmaDbm >= (-49))
                level = android.telephony.SignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (tdScdmaDbm >= (-73))
                    level = android.telephony.SignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (tdScdmaDbm >= (-97))
                        level = android.telephony.SignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (tdScdmaDbm >= (-110))
                            level = android.telephony.SignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            level = android.telephony.SignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;





        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("getTdScdmaLevel = " + level);

        return level;
    }

    /**
     * Get the TD-SCDMA signal level as an asu value.
     *
     * @unknown 
     */
    public int getTdScdmaAsuLevel() {
        final int tdScdmaDbm = getTdScdmaDbm();
        int tdScdmaAsuLevel;
        if (tdScdmaDbm == android.telephony.SignalStrength.INVALID)
            tdScdmaAsuLevel = 255;
        else
            tdScdmaAsuLevel = tdScdmaDbm + 120;

        if (android.telephony.SignalStrength.DBG)
            android.telephony.SignalStrength.log("TD-SCDMA Asu level: " + tdScdmaAsuLevel);

        return tdScdmaAsuLevel;
    }

    /**
     *
     *
     * @return hash code
     */
    @java.lang.Override
    public int hashCode() {
        int primeNum = 31;
        return (((((((((((((mGsmSignalStrength * primeNum) + (mGsmBitErrorRate * primeNum)) + (mCdmaDbm * primeNum)) + (mCdmaEcio * primeNum)) + (mEvdoDbm * primeNum)) + (mEvdoEcio * primeNum)) + (mEvdoSnr * primeNum)) + (mLteSignalStrength * primeNum)) + (mLteRsrp * primeNum)) + (mLteRsrq * primeNum)) + (mLteRssnr * primeNum)) + (mLteCqi * primeNum)) + (mTdScdmaRscp * primeNum)) + (isGsm ? 1 : 0);
    }

    /**
     *
     *
     * @return true if the signal strengths are the same
     */
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.SignalStrength s;
        try {
            s = ((android.telephony.SignalStrength) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return (((((((((((((mGsmSignalStrength == s.mGsmSignalStrength) && (mGsmBitErrorRate == s.mGsmBitErrorRate)) && (mCdmaDbm == s.mCdmaDbm)) && (mCdmaEcio == s.mCdmaEcio)) && (mEvdoDbm == s.mEvdoDbm)) && (mEvdoEcio == s.mEvdoEcio)) && (mEvdoSnr == s.mEvdoSnr)) && (mLteSignalStrength == s.mLteSignalStrength)) && (mLteRsrp == s.mLteRsrp)) && (mLteRsrq == s.mLteRsrq)) && (mLteRssnr == s.mLteRssnr)) && (mLteCqi == s.mLteCqi)) && (mTdScdmaRscp == s.mTdScdmaRscp)) && (isGsm == s.isGsm);
    }

    /**
     *
     *
     * @return string representation.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((((((((((("SignalStrength:" + " ") + mGsmSignalStrength) + " ") + mGsmBitErrorRate) + " ") + mCdmaDbm) + " ") + mCdmaEcio) + " ") + mEvdoDbm) + " ") + mEvdoEcio) + " ") + mEvdoSnr) + " ") + mLteSignalStrength) + " ") + mLteRsrp) + " ") + mLteRsrq) + " ") + mLteRssnr) + " ") + mLteCqi) + " ") + mTdScdmaRscp) + " ") + (isGsm ? "gsm|lte" : "cdma");
    }

    /**
     * Set SignalStrength based on intent notifier map
     *
     * @param m
     * 		intent notifier map
     * @unknown 
     */
    private void setFromNotifierBundle(android.os.Bundle m) {
        mGsmSignalStrength = m.getInt("GsmSignalStrength");
        mGsmBitErrorRate = m.getInt("GsmBitErrorRate");
        mCdmaDbm = m.getInt("CdmaDbm");
        mCdmaEcio = m.getInt("CdmaEcio");
        mEvdoDbm = m.getInt("EvdoDbm");
        mEvdoEcio = m.getInt("EvdoEcio");
        mEvdoSnr = m.getInt("EvdoSnr");
        mLteSignalStrength = m.getInt("LteSignalStrength");
        mLteRsrp = m.getInt("LteRsrp");
        mLteRsrq = m.getInt("LteRsrq");
        mLteRssnr = m.getInt("LteRssnr");
        mLteCqi = m.getInt("LteCqi");
        mTdScdmaRscp = m.getInt("TdScdma");
        isGsm = m.getBoolean("isGsm");
    }

    /**
     * Set intent notifier Bundle based on SignalStrength
     *
     * @param m
     * 		intent notifier Bundle
     * @unknown 
     */
    public void fillInNotifierBundle(android.os.Bundle m) {
        m.putInt("GsmSignalStrength", mGsmSignalStrength);
        m.putInt("GsmBitErrorRate", mGsmBitErrorRate);
        m.putInt("CdmaDbm", mCdmaDbm);
        m.putInt("CdmaEcio", mCdmaEcio);
        m.putInt("EvdoDbm", mEvdoDbm);
        m.putInt("EvdoEcio", mEvdoEcio);
        m.putInt("EvdoSnr", mEvdoSnr);
        m.putInt("LteSignalStrength", mLteSignalStrength);
        m.putInt("LteRsrp", mLteRsrp);
        m.putInt("LteRsrq", mLteRsrq);
        m.putInt("LteRssnr", mLteRssnr);
        m.putInt("LteCqi", mLteCqi);
        m.putInt("TdScdma", mTdScdmaRscp);
        m.putBoolean("isGsm", java.lang.Boolean.valueOf(isGsm));
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.SignalStrength.LOG_TAG, s);
    }
}

