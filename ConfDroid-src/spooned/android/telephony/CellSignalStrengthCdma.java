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
 * Signal strength related information.
 */
public final class CellSignalStrengthCdma extends android.telephony.CellSignalStrength implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellSignalStrengthCdma";

    private static final boolean DBG = false;

    private int mCdmaDbm;// This value is the RSSI value


    private int mCdmaEcio;// This value is the Ec/Io


    private int mEvdoDbm;// This value is the EVDO RSSI value


    private int mEvdoEcio;// This value is the EVDO Ec/Io


    private int mEvdoSnr;// Valid values are 0-8.  8 is the highest signal to noise ratio


    /**
     * Empty constructor
     *
     * @unknown 
     */
    public CellSignalStrengthCdma() {
        setDefaultValues();
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public CellSignalStrengthCdma(int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr) {
        initialize(cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr);
    }

    /**
     * Copy constructors
     *
     * @param s
     * 		Source SignalStrength
     * @unknown 
     */
    public CellSignalStrengthCdma(android.telephony.CellSignalStrengthCdma s) {
        copyFrom(s);
    }

    /**
     * Initialize all the values
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
     * @unknown 
     */
    public void initialize(int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr) {
        mCdmaDbm = cdmaDbm;
        mCdmaEcio = cdmaEcio;
        mEvdoDbm = evdoDbm;
        mEvdoEcio = evdoEcio;
        mEvdoSnr = evdoSnr;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void copyFrom(android.telephony.CellSignalStrengthCdma s) {
        mCdmaDbm = s.mCdmaDbm;
        mCdmaEcio = s.mCdmaEcio;
        mEvdoDbm = s.mEvdoDbm;
        mEvdoEcio = s.mEvdoEcio;
        mEvdoSnr = s.mEvdoSnr;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.telephony.CellSignalStrengthCdma copy() {
        return new android.telephony.CellSignalStrengthCdma(this);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setDefaultValues() {
        mCdmaDbm = java.lang.Integer.MAX_VALUE;
        mCdmaEcio = java.lang.Integer.MAX_VALUE;
        mEvdoDbm = java.lang.Integer.MAX_VALUE;
        mEvdoEcio = java.lang.Integer.MAX_VALUE;
        mEvdoSnr = java.lang.Integer.MAX_VALUE;
    }

    /**
     * Get signal level as an int from 0..4
     */
    @java.lang.Override
    public int getLevel() {
        int level;
        int cdmaLevel = getCdmaLevel();
        int evdoLevel = getEvdoLevel();
        if (evdoLevel == android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
            /* We don't know evdo, use cdma */
            level = getCdmaLevel();
        } else
            if (cdmaLevel == android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                /* We don't know cdma, use evdo */
                level = getEvdoLevel();
            } else {
                /* We know both, use the lowest level */
                level = (cdmaLevel < evdoLevel) ? cdmaLevel : evdoLevel;
            }

        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("getLevel=" + level);

        return level;
    }

    /**
     * Get the signal level as an asu value between 0..97, 99 is unknown
     */
    @java.lang.Override
    public int getAsuLevel() {
        final int cdmaDbm = getCdmaDbm();
        final int cdmaEcio = getCdmaEcio();
        int cdmaAsuLevel;
        int ecioAsuLevel;
        if (cdmaDbm == java.lang.Integer.MAX_VALUE)
            cdmaAsuLevel = 99;
        else
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
        if (cdmaEcio == java.lang.Integer.MAX_VALUE)
            ecioAsuLevel = 99;
        else
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
        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("getAsuLevel=" + level);

        return level;
    }

    /**
     * Get cdma as level 0..4
     */
    public int getCdmaLevel() {
        final int cdmaDbm = getCdmaDbm();
        final int cdmaEcio = getCdmaEcio();
        int levelDbm;
        int levelEcio;
        if (cdmaDbm == java.lang.Integer.MAX_VALUE)
            levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (cdmaDbm >= (-75))
                levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (cdmaDbm >= (-85))
                    levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (cdmaDbm >= (-95))
                        levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (cdmaDbm >= (-100))
                            levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            levelDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;





        // Ec/Io are in dB*10
        if (cdmaEcio == java.lang.Integer.MAX_VALUE)
            levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (cdmaEcio >= (-90))
                levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (cdmaEcio >= (-110))
                    levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (cdmaEcio >= (-130))
                        levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (cdmaEcio >= (-150))
                            levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            levelEcio = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;





        int level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("getCdmaLevel=" + level);

        return level;
    }

    /**
     * Get Evdo as level 0..4
     */
    public int getEvdoLevel() {
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        int levelEvdoDbm;
        int levelEvdoSnr;
        if (evdoDbm == java.lang.Integer.MAX_VALUE)
            levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (evdoDbm >= (-65))
                levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (evdoDbm >= (-75))
                    levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (evdoDbm >= (-90))
                        levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (evdoDbm >= (-105))
                            levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            levelEvdoDbm = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;





        if (evdoSnr == java.lang.Integer.MAX_VALUE)
            levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else
            if (evdoSnr >= 7)
                levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
            else
                if (evdoSnr >= 5)
                    levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
                else
                    if (evdoSnr >= 3)
                        levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
                    else
                        if (evdoSnr >= 1)
                            levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;
                        else
                            levelEvdoSnr = android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;





        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("getEvdoLevel=" + level);

        return level;
    }

    /**
     * Get the signal strength as dBm
     */
    @java.lang.Override
    public int getDbm() {
        int cdmaDbm = getCdmaDbm();
        int evdoDbm = getEvdoDbm();
        // Use the lower value to be conservative
        return cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm;
    }

    /**
     * Get the CDMA RSSI value in dBm
     */
    public int getCdmaDbm() {
        return mCdmaDbm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaDbm(int cdmaDbm) {
        mCdmaDbm = cdmaDbm;
    }

    /**
     * Get the CDMA Ec/Io value in dB*10
     */
    public int getCdmaEcio() {
        return mCdmaEcio;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaEcio(int cdmaEcio) {
        mCdmaEcio = cdmaEcio;
    }

    /**
     * Get the EVDO RSSI value in dBm
     */
    public int getEvdoDbm() {
        return mEvdoDbm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoDbm(int evdoDbm) {
        mEvdoDbm = evdoDbm;
    }

    /**
     * Get the EVDO Ec/Io value in dB*10
     */
    public int getEvdoEcio() {
        return mEvdoEcio;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoEcio(int evdoEcio) {
        mEvdoEcio = evdoEcio;
    }

    /**
     * Get the signal to noise ratio. Valid values are 0-8. 8 is the highest.
     */
    public int getEvdoSnr() {
        return mEvdoSnr;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoSnr(int evdoSnr) {
        mEvdoSnr = evdoSnr;
    }

    @java.lang.Override
    public int hashCode() {
        int primeNum = 31;
        return ((((mCdmaDbm * primeNum) + (mCdmaEcio * primeNum)) + (mEvdoDbm * primeNum)) + (mEvdoEcio * primeNum)) + (mEvdoSnr * primeNum);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        android.telephony.CellSignalStrengthCdma s;
        try {
            s = ((android.telephony.CellSignalStrengthCdma) (o));
        } catch (java.lang.ClassCastException ex) {
            return false;
        }
        if (o == null) {
            return false;
        }
        return ((((mCdmaDbm == s.mCdmaDbm) && (mCdmaEcio == s.mCdmaEcio)) && (mEvdoDbm == s.mEvdoDbm)) && (mEvdoEcio == s.mEvdoEcio)) && (mEvdoSnr == s.mEvdoSnr);
    }

    /**
     *
     *
     * @return string representation.
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("CellSignalStrengthCdma:" + " cdmaDbm=") + mCdmaDbm) + " cdmaEcio=") + mCdmaEcio) + " evdoDbm=") + mEvdoDbm) + " evdoEcio=") + mEvdoEcio) + " evdoSnr=") + mEvdoSnr;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("writeToParcel(Parcel, int): " + toString());

        // Need to multiply CdmaDbm, CdmaEcio, EvdoDbm and EvdoEcio by -1
        // to ensure consistency when reading values written here
        // unless the value is invalid
        dest.writeInt(mCdmaDbm * (mCdmaDbm != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mCdmaEcio * (mCdmaEcio != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mEvdoDbm * (mEvdoDbm != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mEvdoEcio * (mEvdoEcio != java.lang.Integer.MAX_VALUE ? -1 : 1));
        dest.writeInt(mEvdoSnr);
    }

    /**
     * Construct a SignalStrength object from the given parcel
     * where the TYPE_CDMA token is already been processed.
     */
    private CellSignalStrengthCdma(android.os.Parcel in) {
        // CdmaDbm, CdmaEcio, EvdoDbm and EvdoEcio are written into
        // the parcel as positive values.
        // Need to convert into negative values unless the value is invalid
        mCdmaDbm = in.readInt();
        if (mCdmaDbm != java.lang.Integer.MAX_VALUE)
            mCdmaDbm *= -1;

        mCdmaEcio = in.readInt();
        if (mCdmaEcio != java.lang.Integer.MAX_VALUE)
            mCdmaEcio *= -1;

        mEvdoDbm = in.readInt();
        if (mEvdoDbm != java.lang.Integer.MAX_VALUE)
            mEvdoDbm *= -1;

        mEvdoEcio = in.readInt();
        if (mEvdoEcio != java.lang.Integer.MAX_VALUE)
            mEvdoEcio *= -1;

        mEvdoSnr = in.readInt();
        if (android.telephony.CellSignalStrengthCdma.DBG)
            android.telephony.CellSignalStrengthCdma.log("CellSignalStrengthCdma(Parcel): " + toString());

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
    public static final android.os.Parcelable.Creator<android.telephony.CellSignalStrengthCdma> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellSignalStrengthCdma>() {
        @java.lang.Override
        public android.telephony.CellSignalStrengthCdma createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellSignalStrengthCdma(in);
        }

        @java.lang.Override
        public android.telephony.CellSignalStrengthCdma[] newArray(int size) {
            return new android.telephony.CellSignalStrengthCdma[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellSignalStrengthCdma.LOG_TAG, s);
    }
}

