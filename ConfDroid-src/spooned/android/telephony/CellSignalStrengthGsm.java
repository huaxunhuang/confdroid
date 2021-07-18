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
 * GSM signal strength related information.
 */
public final class CellSignalStrengthGsm extends android.telephony.CellSignalStrength implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellSignalStrengthGsm";

    private static final boolean DBG = false;

    private static final int GSM_SIGNAL_STRENGTH_GREAT = 12;

    private static final int GSM_SIGNAL_STRENGTH_GOOD = 8;

    private static final int GSM_SIGNAL_STRENGTH_MODERATE = 5;

    private int mSignalStrength;// Valid values are (0-31, 99) as defined in TS 27.007 8.5


    private int mBitErrorRate;// bit error rate (0-7, 99) as defined in TS 27.007 8.5


    private int mTimingAdvance;

    /**
     * Empty constructor
     *
     * @unknown 
     */
    public CellSignalStrengthGsm() {
        setDefaultValues();
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public CellSignalStrengthGsm(int ss, int ber) {
        initialize(ss, ber);
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source SignalStrength
     * @unknown 
     */
    public CellSignalStrengthGsm(android.telephony.CellSignalStrengthGsm s) {
        copyFrom(s);
    }

    /**
     * Initialize all the values
     *
     * @param ss
     * 		SignalStrength as ASU value
     * @param ber
     * 		is Bit Error Rate
     * @unknown 
     */
    public void initialize(int ss, int ber) {
        mSignalStrength = ss;
        mBitErrorRate = ber;
        mTimingAdvance = java.lang.Integer.MAX_VALUE;
    }

    /**
     * Initialize all the values
     *
     * @param ss
     * 		SignalStrength as ASU value
     * @param ber
     * 		is Bit Error Rate
     * @param ta
     * 		timing advance
     * @unknown 
     */
    public void initialize(int ss, int ber, int ta) {
        mSignalStrength = ss;
        mBitErrorRate = ber;
        mTimingAdvance = ta;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void copyFrom(android.telephony.CellSignalStrengthGsm s) {
        mSignalStrength = s.mSignalStrength;
        mBitErrorRate = s.mBitErrorRate;
        mTimingAdvance = s.mTimingAdvance;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.telephony.CellSignalStrengthGsm copy() {
        return new android.telephony.CellSignalStrengthGsm(this);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setDefaultValues() {
        mSignalStrength = java.lang.Integer.MAX_VALUE;
        mBitErrorRate = java.lang.Integer.MAX_VALUE;
        mTimingAdvance = java.lang.Integer.MAX_VALUE;
    }

    /**
     * Get signal level as an int from 0..4
     */
    @java.lang.Override
    public int getLevel() {
        int level;
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int asu = mSignalStrength;
        if ((asu <= 2) || (asu == 99))
            level = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (asu >= android.telephony.CellSignalStrengthGsm.GSM_SIGNAL_STRENGTH_GREAT)
                level = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (asu >= android.telephony.CellSignalStrengthGsm.GSM_SIGNAL_STRENGTH_GOOD)
                    level = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (asu >= android.telephony.CellSignalStrengthGsm.GSM_SIGNAL_STRENGTH_MODERATE)
                        level = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        level = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;




        if (android.telephony.CellSignalStrengthGsm.DBG)
            android.telephony.CellSignalStrengthGsm.log("getLevel=" + level);

        return level;
    }

    /**
     * Get the signal strength as dBm
     */
    @java.lang.Override
    public int getDbm() {
        int dBm;
        int level = mSignalStrength;
        int asu = (level == 99) ? java.lang.Integer.MAX_VALUE : level;
        if (asu != java.lang.Integer.MAX_VALUE) {
            dBm = (-113) + (2 * asu);
        } else {
            dBm = java.lang.Integer.MAX_VALUE;
        }
        if (android.telephony.CellSignalStrengthGsm.DBG)
            android.telephony.CellSignalStrengthGsm.log("getDbm=" + dBm);

        return dBm;
    }

    /**
     * Get the signal level as an asu value between 0..31, 99 is unknown
     * Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
     */
    @java.lang.Override
    public int getAsuLevel() {
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int level = mSignalStrength;
        if (android.telephony.CellSignalStrengthGsm.DBG)
            android.telephony.CellSignalStrengthGsm.log("getAsuLevel=" + level);

        return level;
    }

    @java.lang.Override
    public int hashCode() {
        int primeNum = 31;
        return (mSignalStrength * primeNum) + (mBitErrorRate * primeNum);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.CellSignalStrengthGsm s;
        try {
            s = ((android.telephony.CellSignalStrengthGsm) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return ((mSignalStrength == s.mSignalStrength) && (mBitErrorRate == s.mBitErrorRate)) && (s.mTimingAdvance == mTimingAdvance);
    }

    /**
     *
     *
     * @return string representation.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((((("CellSignalStrengthGsm:" + " ss=") + mSignalStrength) + " ber=") + mBitErrorRate) + " mTa=") + mTimingAdvance;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (android.telephony.CellSignalStrengthGsm.DBG)
            android.telephony.CellSignalStrengthGsm.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mSignalStrength);
        dest.writeInt(mBitErrorRate);
        dest.writeInt(mTimingAdvance);
    }

    /**
     * Construct a SignalStrength object from the given parcel
     * where the token is already been processed.
     */
    private CellSignalStrengthGsm(android.os.Parcel in) {
        mSignalStrength = in.readInt();
        mBitErrorRate = in.readInt();
        mTimingAdvance = in.readInt();
        if (android.telephony.CellSignalStrengthGsm.DBG)
            android.telephony.CellSignalStrengthGsm.log("CellSignalStrengthGsm(Parcel): " + toString());

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
    public static final android.os.Parcelable.Creator<android.telephony.CellSignalStrengthGsm> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellSignalStrengthGsm>() {
        @java.lang.Override
        public android.telephony.CellSignalStrengthGsm createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellSignalStrengthGsm(in);
        }

        @java.lang.Override
        public android.telephony.CellSignalStrengthGsm[] newArray(int size) {
            return new android.telephony.CellSignalStrengthGsm[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellSignalStrengthGsm.LOG_TAG, s);
    }
}

