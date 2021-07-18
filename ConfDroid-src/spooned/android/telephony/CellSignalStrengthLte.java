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
 * LTE signal strength related information.
 */
public final class CellSignalStrengthLte extends android.telephony.CellSignalStrength implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellSignalStrengthLte";

    private static final boolean DBG = false;

    private int mSignalStrength;

    private int mRsrp;

    private int mRsrq;

    private int mRssnr;

    private int mCqi;

    private int mTimingAdvance;

    /**
     * Empty constructor
     *
     * @unknown 
     */
    public CellSignalStrengthLte() {
        setDefaultValues();
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public CellSignalStrengthLte(int signalStrength, int rsrp, int rsrq, int rssnr, int cqi, int timingAdvance) {
        initialize(signalStrength, rsrp, rsrq, rssnr, cqi, timingAdvance);
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source SignalStrength
     * @unknown 
     */
    public CellSignalStrengthLte(android.telephony.CellSignalStrengthLte s) {
        copyFrom(s);
    }

    /**
     * Initialize all the values
     *
     * @param lteSignalStrength
     * 		
     * @param rsrp
     * 		
     * @param rsrq
     * 		
     * @param rssnr
     * 		
     * @param cqi
     * 		
     * @unknown 
     */
    public void initialize(int lteSignalStrength, int rsrp, int rsrq, int rssnr, int cqi, int timingAdvance) {
        mSignalStrength = lteSignalStrength;
        mRsrp = rsrp;
        mRsrq = rsrq;
        mRssnr = rssnr;
        mCqi = cqi;
        mTimingAdvance = timingAdvance;
    }

    /**
     * Initialize from the SignalStrength structure.
     *
     * @param ss
     * 		
     * @unknown 
     */
    public void initialize(android.telephony.SignalStrength ss, int timingAdvance) {
        mSignalStrength = ss.getLteSignalStrength();
        mRsrp = ss.getLteRsrp();
        mRsrq = ss.getLteRsrq();
        mRssnr = ss.getLteRssnr();
        mCqi = ss.getLteCqi();
        mTimingAdvance = timingAdvance;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void copyFrom(android.telephony.CellSignalStrengthLte s) {
        mSignalStrength = s.mSignalStrength;
        mRsrp = s.mRsrp;
        mRsrq = s.mRsrq;
        mRssnr = s.mRssnr;
        mCqi = s.mCqi;
        mTimingAdvance = s.mTimingAdvance;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.telephony.CellSignalStrengthLte copy() {
        return new android.telephony.CellSignalStrengthLte(this);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setDefaultValues() {
        mSignalStrength = java.lang.Integer.MAX_VALUE;
        mRsrp = java.lang.Integer.MAX_VALUE;
        mRsrq = java.lang.Integer.MAX_VALUE;
        mRssnr = java.lang.Integer.MAX_VALUE;
        mCqi = java.lang.Integer.MAX_VALUE;
        mTimingAdvance = java.lang.Integer.MAX_VALUE;
    }

    /**
     * Get signal level as an int from 0..4
     */
    @java.lang.Override
    public int getLevel() {
        int levelRsrp = 0;
        int levelRssnr = 0;
        if (mRsrp == java.lang.Integer.MAX_VALUE)
            levelRsrp = 0;
        else
            if (mRsrp >= (-95))
                levelRsrp = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (mRsrp >= (-105))
                    levelRsrp = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (mRsrp >= (-115))
                        levelRsrp = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        levelRsrp = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;




        // See RIL_LTE_SignalStrength in ril.h
        if (mRssnr == java.lang.Integer.MAX_VALUE)
            levelRssnr = 0;
        else
            if (mRssnr >= 45)
                levelRssnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (mRssnr >= 10)
                    levelRssnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (mRssnr >= (-30))
                        levelRssnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        levelRssnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;




        int level;
        if (mRsrp == java.lang.Integer.MAX_VALUE)
            level = levelRssnr;
        else
            if (mRssnr == java.lang.Integer.MAX_VALUE)
                level = levelRsrp;
            else
                level = (levelRssnr < levelRsrp) ? levelRssnr : levelRsrp;


        if (android.telephony.CellSignalStrengthLte.DBG)
            android.telephony.CellSignalStrengthLte.log((((("Lte rsrp level: " + levelRsrp) + " snr level: ") + levelRssnr) + " level: ") + level);

        return level;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getRsrq() {
        return mRsrq;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getRssnr() {
        return mRssnr;
    }

    /**
     * Get signal strength as dBm
     */
    @java.lang.Override
    public int getDbm() {
        return mRsrp;
    }

    /**
     * Get the LTE signal level as an asu value between 0..97, 99 is unknown
     * Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
     */
    @java.lang.Override
    public int getAsuLevel() {
        int lteAsuLevel = 99;
        int lteDbm = getDbm();
        if (lteDbm == java.lang.Integer.MAX_VALUE)
            lteAsuLevel = 99;
        else
            if (lteDbm <= (-140))
                lteAsuLevel = 0;
            else
                if (lteDbm >= (-43))
                    lteAsuLevel = 97;
                else
                    lteAsuLevel = lteDbm + 140;



        if (android.telephony.CellSignalStrengthLte.DBG)
            android.telephony.CellSignalStrengthLte.log("Lte Asu level: " + lteAsuLevel);

        return lteAsuLevel;
    }

    /**
     * Get the timing advance value for LTE.
     * See 3GPP xxxx
     */
    public int getTimingAdvance() {
        return mTimingAdvance;
    }

    @java.lang.Override
    public int hashCode() {
        int primeNum = 31;
        return (((((mSignalStrength * primeNum) + (mRsrp * primeNum)) + (mRsrq * primeNum)) + (mRssnr * primeNum)) + (mCqi * primeNum)) + (mTimingAdvance * primeNum);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.CellSignalStrengthLte s;
        try {
            s = ((android.telephony.CellSignalStrengthLte) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return (((((mSignalStrength == s.mSignalStrength) && (mRsrp == s.mRsrp)) && (mRsrq == s.mRsrq)) && (mRssnr == s.mRssnr)) && (mCqi == s.mCqi)) && (mTimingAdvance == s.mTimingAdvance);
    }

    /**
     *
     *
     * @return string representation.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((("CellSignalStrengthLte:" + " ss=") + mSignalStrength) + " rsrp=") + mRsrp) + " rsrq=") + mRsrq) + " rssnr=") + mRssnr) + " cqi=") + mCqi) + " ta=") + mTimingAdvance;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (android.telephony.CellSignalStrengthLte.DBG)
            android.telephony.CellSignalStrengthLte.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mSignalStrength);
        // Need to multiply rsrp and rsrq by -1
        // to ensure consistency when reading values written here
        // unless the values are invalid
        dest.writeInt(mRsrp * (mRsrp != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mRsrq * (mRsrq != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mRssnr);
        dest.writeInt(mCqi);
        dest.writeInt(mTimingAdvance);
    }

    /**
     * Construct a SignalStrength object from the given parcel
     * where the token is already been processed.
     */
    private CellSignalStrengthLte(android.os.Parcel in) {
        mSignalStrength = in.readInt();
        // rsrp and rsrq are written into the parcel as positive values.
        // Need to convert into negative values unless the values are invalid
        mRsrp = in.readInt();
        if (mRsrp != java.lang.Integer.MAX_VALUE)
            mRsrp *= -1;

        mRsrq = in.readInt();
        if (mRsrq != java.lang.Integer.MAX_VALUE)
            mRsrq *= -1;

        mRssnr = in.readInt();
        mCqi = in.readInt();
        mTimingAdvance = in.readInt();
        if (android.telephony.CellSignalStrengthLte.DBG)
            android.telephony.CellSignalStrengthLte.log("CellSignalStrengthLte(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.SuppressWarnings("hiding")
    public static final android.os.Parcelable.Creator<android.telephony.CellSignalStrengthLte> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellSignalStrengthLte>() {
        @java.lang.Override
        public android.telephony.CellSignalStrengthLte createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellSignalStrengthLte(in);
        }

        @java.lang.Override
        public android.telephony.CellSignalStrengthLte[] newArray(int size) {
            return new android.telephony.CellSignalStrengthLte[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellSignalStrengthLte.LOG_TAG, s);
    }
}

