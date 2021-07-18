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
 * Represents the neighboring cell information, including
 * Received Signal Strength and Cell ID location.
 */
public class NeighboringCellInfo implements android.os.Parcelable {
    /**
     * Signal strength is not available
     */
    public static final int UNKNOWN_RSSI = 99;

    /**
     * Cell location is not available
     */
    public static final int UNKNOWN_CID = -1;

    /**
     * In GSM, mRssi is the Received RSSI;
     * In UMTS, mRssi is the Level index of CPICH Received Signal Code Power
     */
    private int mRssi;

    /**
     * CID in 16 bits format in GSM. Return UNKNOWN_CID in UMTS and CMDA.
     */
    private int mCid;

    /**
     * LAC in 16 bits format in GSM. Return UNKNOWN_CID in UMTS and CMDA.
     */
    private int mLac;

    /**
     * Primary Scrambling Code in 9 bits format in UMTS
     * Return UNKNOWN_CID in GSM and CMDA.
     */
    private int mPsc;

    /**
     * Radio network type, value is one of following
     * TelephonyManager.NETWORK_TYPE_XXXXXX.
     */
    private int mNetworkType;

    /**
     * Empty constructor.  Initializes the RSSI and CID.
     *
     * NeighboringCellInfo is one time shot for the neighboring cells based on
     * the radio network type at that moment. Its constructor needs radio network
     * type.
     *
     * @deprecated by {@link #NeighboringCellInfo(int, String, int)}
     */
    @java.lang.Deprecated
    public NeighboringCellInfo() {
        mRssi = android.telephony.NeighboringCellInfo.UNKNOWN_RSSI;
        mLac = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        mCid = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        mPsc = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        mNetworkType = android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;
    }

    /**
     * Initialize the object from rssi and cid.
     *
     * NeighboringCellInfo is one time shot for the neighboring cells based on
     * the radio network type at that moment. Its constructor needs radio network
     * type.
     *
     * @deprecated by {@link #NeighboringCellInfo(int, String, int)}
     */
    @java.lang.Deprecated
    public NeighboringCellInfo(int rssi, int cid) {
        mRssi = rssi;
        mCid = cid;
    }

    /**
     * Initialize the object from rssi, location string, and radioType
     * radioType is one of following
     * {@link TelephonyManager#NETWORK_TYPE_GPRS TelephonyManager.NETWORK_TYPE_GPRS},
     * {@link TelephonyManager#NETWORK_TYPE_EDGE TelephonyManager.NETWORK_TYPE_EDGE},
     * {@link TelephonyManager#NETWORK_TYPE_UMTS TelephonyManager.NETWORK_TYPE_UMTS},
     * {@link TelephonyManager#NETWORK_TYPE_HSDPA TelephonyManager.NETWORK_TYPE_HSDPA},
     * {@link TelephonyManager#NETWORK_TYPE_HSUPA TelephonyManager.NETWORK_TYPE_HSUPA},
     * and {@link TelephonyManager#NETWORK_TYPE_HSPA TelephonyManager.NETWORK_TYPE_HSPA}.
     */
    public NeighboringCellInfo(int rssi, java.lang.String location, int radioType) {
        // set default value
        mRssi = rssi;
        mNetworkType = android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;
        mPsc = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        mLac = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        mCid = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
        // pad location string with leading "0"
        int l = location.length();
        if (l > 8)
            return;

        if (l < 8) {
            for (int i = 0; i < (8 - l); i++) {
                location = "0" + location;
            }
        }
        // TODO - handle LTE and eHRPD (or find they can't be supported)
        try {
            // set LAC/CID or PSC based on radioType
            switch (radioType) {
                case android.telephony.TelephonyManager.NETWORK_TYPE_GPRS :
                case android.telephony.TelephonyManager.NETWORK_TYPE_EDGE :
                    mNetworkType = radioType;
                    // check if 0xFFFFFFFF for UNKNOWN_CID
                    if (!location.equalsIgnoreCase("FFFFFFFF")) {
                        mCid = java.lang.Integer.parseInt(location.substring(4), 16);
                        mLac = java.lang.Integer.parseInt(location.substring(0, 4), 16);
                    }
                    break;
                case android.telephony.TelephonyManager.NETWORK_TYPE_UMTS :
                case android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA :
                case android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA :
                case android.telephony.TelephonyManager.NETWORK_TYPE_HSPA :
                    mNetworkType = radioType;
                    mPsc = java.lang.Integer.parseInt(location, 16);
                    break;
            }
        } catch (java.lang.NumberFormatException e) {
            // parsing location error
            mPsc = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
            mLac = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
            mCid = android.telephony.NeighboringCellInfo.UNKNOWN_CID;
            mNetworkType = android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;
        }
    }

    /**
     * Initialize the object from a parcel.
     */
    public NeighboringCellInfo(android.os.Parcel in) {
        mRssi = in.readInt();
        mLac = in.readInt();
        mCid = in.readInt();
        mPsc = in.readInt();
        mNetworkType = in.readInt();
    }

    /**
     *
     *
     * @return received signal strength or UNKNOWN_RSSI if unknown

    For GSM, it is in "asu" ranging from 0 to 31 (dBm = -113 + 2*asu)
    0 means "-113 dBm or less" and 31 means "-51 dBm or greater"
    For UMTS, it is the Level index of CPICH RSCP defined in TS 25.125
     */
    public int getRssi() {
        return mRssi;
    }

    /**
     *
     *
     * @return LAC in GSM, 0xffff max legal value
    UNKNOWN_CID if in UMTS or CMDA or unknown
     */
    public int getLac() {
        return mLac;
    }

    /**
     *
     *
     * @return cell id in GSM, 0xffff max legal value
    UNKNOWN_CID if in UMTS or CDMA or unknown
     */
    public int getCid() {
        return mCid;
    }

    /**
     *
     *
     * @return Primary Scrambling Code in 9 bits format in UMTS, 0x1ff max value
    UNKNOWN_CID if in GSM or CMDA or unknown
     */
    public int getPsc() {
        return mPsc;
    }

    /**
     *
     *
     * @return Radio network type while neighboring cell location is stored.

    Return {@link TelephonyManager#NETWORK_TYPE_UNKNOWN TelephonyManager.NETWORK_TYPE_UNKNOWN}
    means that the location information is unavailable.

    Return {@link TelephonyManager#NETWORK_TYPE_GPRS TelephonyManager.NETWORK_TYPE_GPRS} or
    {@link TelephonyManager#NETWORK_TYPE_EDGE TelephonyManager.NETWORK_TYPE_EDGE}
    means that Neighboring Cell information is stored for GSM network, in
    which {@link NeighboringCellInfo#getLac NeighboringCellInfo.getLac} and
    {@link NeighboringCellInfo#getCid NeighboringCellInfo.getCid} should be
    called to access location.

    Return {@link TelephonyManager#NETWORK_TYPE_UMTS TelephonyManager.NETWORK_TYPE_UMTS},
    {@link TelephonyManager#NETWORK_TYPE_HSDPA TelephonyManager.NETWORK_TYPE_HSDPA},
    {@link TelephonyManager#NETWORK_TYPE_HSUPA TelephonyManager.NETWORK_TYPE_HSUPA},
    or {@link TelephonyManager#NETWORK_TYPE_HSPA TelephonyManager.NETWORK_TYPE_HSPA}
    means that Neighboring Cell information is stored for UMTS network, in
    which {@link NeighboringCellInfo#getPsc NeighboringCellInfo.getPsc}
    should be called to access location.
     */
    public int getNetworkType() {
        return mNetworkType;
    }

    /**
     * Set the cell id.
     *
     * NeighboringCellInfo is a one time shot for the neighboring cells based on
     * the radio network type at that moment. It shouldn't be changed after
     * creation.
     *
     * @deprecated cid value passed as in location parameter passed to constructor
    {@link #NeighboringCellInfo(int, String, int)}
     */
    @java.lang.Deprecated
    public void setCid(int cid) {
        mCid = cid;
    }

    /**
     * Set the signal strength of the cell.
     *
     * NeighboringCellInfo is a one time shot for the neighboring cells based on
     * the radio network type at that moment. It shouldn't be changed after
     * creation.
     *
     * @deprecated initial rssi value passed as parameter to constructor
    {@link #NeighboringCellInfo(int, String, int)}
     */
    @java.lang.Deprecated
    public void setRssi(int rssi) {
        mRssi = rssi;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[");
        if (mPsc != android.telephony.NeighboringCellInfo.UNKNOWN_CID) {
            sb.append(java.lang.Integer.toHexString(mPsc)).append("@").append(mRssi == android.telephony.NeighboringCellInfo.UNKNOWN_RSSI ? "-" : mRssi);
        } else
            if ((mLac != android.telephony.NeighboringCellInfo.UNKNOWN_CID) && (mCid != android.telephony.NeighboringCellInfo.UNKNOWN_CID)) {
                sb.append(java.lang.Integer.toHexString(mLac)).append(java.lang.Integer.toHexString(mCid)).append("@").append(mRssi == android.telephony.NeighboringCellInfo.UNKNOWN_RSSI ? "-" : mRssi);
            }

        sb.append("]");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mRssi);
        dest.writeInt(mLac);
        dest.writeInt(mCid);
        dest.writeInt(mPsc);
        dest.writeInt(mNetworkType);
    }

    public static final android.os.Parcelable.Creator<android.telephony.NeighboringCellInfo> CREATOR = new android.os.Parcelable.Creator<android.telephony.NeighboringCellInfo>() {
        public android.telephony.NeighboringCellInfo createFromParcel(android.os.Parcel in) {
            return new android.telephony.NeighboringCellInfo(in);
        }

        public android.telephony.NeighboringCellInfo[] newArray(int size) {
            return new android.telephony.NeighboringCellInfo[size];
        }
    };
}

