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
 * CellIdentity is to represent a unique CDMA cell
 */
public final class CellIdentityCdma implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellSignalStrengthCdma";

    private static final boolean DBG = false;

    // Network Id 0..65535
    private final int mNetworkId;

    // CDMA System Id 0..32767
    private final int mSystemId;

    // Base Station Id 0..65535
    private final int mBasestationId;

    /**
     * Longitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * It is represented in units of 0.25 seconds and ranges from -2592000
     * to 2592000, both values inclusive (corresponding to a range of -180
     * to +180 degrees).
     */
    private final int mLongitude;

    /**
     * Latitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * It is represented in units of 0.25 seconds and ranges from -1296000
     * to 1296000, both values inclusive (corresponding to a range of -90
     * to +90 degrees).
     */
    private final int mLatitude;

    /**
     *
     *
     * @unknown 
     */
    public CellIdentityCdma() {
        mNetworkId = java.lang.Integer.MAX_VALUE;
        mSystemId = java.lang.Integer.MAX_VALUE;
        mBasestationId = java.lang.Integer.MAX_VALUE;
        mLongitude = java.lang.Integer.MAX_VALUE;
        mLatitude = java.lang.Integer.MAX_VALUE;
    }

    /**
     * public constructor
     *
     * @param nid
     * 		Network Id 0..65535
     * @param sid
     * 		CDMA System Id 0..32767
     * @param bid
     * 		Base Station Id 0..65535
     * @param lon
     * 		Longitude is a decimal number ranges from -2592000
     * 		to 2592000
     * @param lat
     * 		Latitude is a decimal number ranges from -1296000
     * 		to 1296000
     * @unknown 
     */
    public CellIdentityCdma(int nid, int sid, int bid, int lon, int lat) {
        mNetworkId = nid;
        mSystemId = sid;
        mBasestationId = bid;
        mLongitude = lon;
        mLatitude = lat;
    }

    private CellIdentityCdma(android.telephony.CellIdentityCdma cid) {
        mNetworkId = cid.mNetworkId;
        mSystemId = cid.mSystemId;
        mBasestationId = cid.mBasestationId;
        mLongitude = cid.mLongitude;
        mLatitude = cid.mLatitude;
    }

    android.telephony.CellIdentityCdma copy() {
        return new android.telephony.CellIdentityCdma(this);
    }

    /**
     *
     *
     * @return Network Id 0..65535, Integer.MAX_VALUE if unknown
     */
    public int getNetworkId() {
        return mNetworkId;
    }

    /**
     *
     *
     * @return System Id 0..32767, Integer.MAX_VALUE if unknown
     */
    public int getSystemId() {
        return mSystemId;
    }

    /**
     *
     *
     * @return Base Station Id 0..65535, Integer.MAX_VALUE if unknown
     */
    public int getBasestationId() {
        return mBasestationId;
    }

    /**
     *
     *
     * @return Base station longitude, which is a decimal number as
    specified in 3GPP2 C.S0005-A v6.0. It is represented in units
    of 0.25 seconds and ranges from -2592000 to 2592000, both
    values inclusive (corresponding to a range of -180
    to +180 degrees). Integer.MAX_VALUE if unknown.
     */
    public int getLongitude() {
        return mLongitude;
    }

    /**
     *
     *
     * @return Base station latitude, which is a decimal number as
    specified in 3GPP2 C.S0005-A v6.0. It is represented in units
    of 0.25 seconds and ranges from -1296000 to 1296000, both
    values inclusive (corresponding to a range of -90
    to +90 degrees). Integer.MAX_VALUE if unknown.
     */
    public int getLatitude() {
        return mLatitude;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mNetworkId, mSystemId, mBasestationId, mLatitude, mLongitude);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof android.telephony.CellIdentityCdma)) {
            return false;
        }
        android.telephony.CellIdentityCdma o = ((android.telephony.CellIdentityCdma) (other));
        return ((((mNetworkId == o.mNetworkId) && (mSystemId == o.mSystemId)) && (mBasestationId == o.mBasestationId)) && (mLatitude == o.mLatitude)) && (mLongitude == o.mLongitude);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("CellIdentityCdma:{");
        sb.append(" mNetworkId=");
        sb.append(mNetworkId);
        sb.append(" mSystemId=");
        sb.append(mSystemId);
        sb.append(" mBasestationId=");
        sb.append(mBasestationId);
        sb.append(" mLongitude=");
        sb.append(mLongitude);
        sb.append(" mLatitude=");
        sb.append(mLatitude);
        sb.append("}");
        return sb.toString();
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
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (android.telephony.CellIdentityCdma.DBG)
            android.telephony.CellIdentityCdma.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mNetworkId);
        dest.writeInt(mSystemId);
        dest.writeInt(mBasestationId);
        dest.writeInt(mLongitude);
        dest.writeInt(mLatitude);
    }

    /**
     * Construct from Parcel, type has already been processed
     */
    private CellIdentityCdma(android.os.Parcel in) {
        mNetworkId = in.readInt();
        mSystemId = in.readInt();
        mBasestationId = in.readInt();
        mLongitude = in.readInt();
        mLatitude = in.readInt();
        if (android.telephony.CellIdentityCdma.DBG)
            android.telephony.CellIdentityCdma.log("CellIdentityCdma(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.SuppressWarnings("hiding")
    public static final android.os.Parcelable.Creator<android.telephony.CellIdentityCdma> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellIdentityCdma>() {
        @java.lang.Override
        public android.telephony.CellIdentityCdma createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellIdentityCdma(in);
        }

        @java.lang.Override
        public android.telephony.CellIdentityCdma[] newArray(int size) {
            return new android.telephony.CellIdentityCdma[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellIdentityCdma.LOG_TAG, s);
    }
}

