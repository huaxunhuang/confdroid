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
 * CellIdentity to represent a unique GSM cell
 */
public final class CellIdentityGsm implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellIdentityGsm";

    private static final boolean DBG = false;

    // 3-digit Mobile Country Code, 0..999
    private final int mMcc;

    // 2 or 3-digit Mobile Network Code, 0..999
    private final int mMnc;

    // 16-bit Location Area Code, 0..65535
    private final int mLac;

    // 16-bit GSM Cell Identity described in TS 27.007, 0..65535
    private final int mCid;

    // 16-bit GSM Absolute RF Channel Number
    private final int mArfcn;

    // 6-bit Base Station Identity Code
    private final int mBsic;

    /**
     *
     *
     * @unknown 
     */
    public CellIdentityGsm() {
        mMcc = java.lang.Integer.MAX_VALUE;
        mMnc = java.lang.Integer.MAX_VALUE;
        mLac = java.lang.Integer.MAX_VALUE;
        mCid = java.lang.Integer.MAX_VALUE;
        mArfcn = java.lang.Integer.MAX_VALUE;
        mBsic = java.lang.Integer.MAX_VALUE;
    }

    /**
     * public constructor
     *
     * @param mcc
     * 		3-digit Mobile Country Code, 0..999
     * @param mnc
     * 		2 or 3-digit Mobile Network Code, 0..999
     * @param lac
     * 		16-bit Location Area Code, 0..65535
     * @param cid
     * 		16-bit GSM Cell Identity or 28-bit UMTS Cell Identity
     * @unknown 
     */
    public CellIdentityGsm(int mcc, int mnc, int lac, int cid) {
        this(mcc, mnc, lac, cid, java.lang.Integer.MAX_VALUE, java.lang.Integer.MAX_VALUE);
    }

    /**
     * public constructor
     *
     * @param mcc
     * 		3-digit Mobile Country Code, 0..999
     * @param mnc
     * 		2 or 3-digit Mobile Network Code, 0..999
     * @param lac
     * 		16-bit Location Area Code, 0..65535
     * @param cid
     * 		16-bit GSM Cell Identity or 28-bit UMTS Cell Identity
     * @param arfcn
     * 		16-bit GSM Absolute RF Channel Number
     * @param bsic
     * 		6-bit Base Station Identity Code
     * @unknown 
     */
    public CellIdentityGsm(int mcc, int mnc, int lac, int cid, int arfcn, int bsic) {
        mMcc = mcc;
        mMnc = mnc;
        mLac = lac;
        mCid = cid;
        mArfcn = arfcn;
        mBsic = bsic;
    }

    private CellIdentityGsm(android.telephony.CellIdentityGsm cid) {
        mMcc = cid.mMcc;
        mMnc = cid.mMnc;
        mLac = cid.mLac;
        mCid = cid.mCid;
        mArfcn = cid.mArfcn;
        mBsic = cid.mBsic;
    }

    android.telephony.CellIdentityGsm copy() {
        return new android.telephony.CellIdentityGsm(this);
    }

    /**
     *
     *
     * @return 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown
     */
    public int getMcc() {
        return mMcc;
    }

    /**
     *
     *
     * @return 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown
     */
    public int getMnc() {
        return mMnc;
    }

    /**
     *
     *
     * @return 16-bit Location Area Code, 0..65535, Integer.MAX_VALUE if unknown
     */
    public int getLac() {
        return mLac;
    }

    /**
     *
     *
     * @return CID
    Either 16-bit GSM Cell Identity described
    in TS 27.007, 0..65535, Integer.MAX_VALUE if unknown
     */
    public int getCid() {
        return mCid;
    }

    /**
     *
     *
     * @return 16-bit GSM Absolute RF Channel Number, Integer.MAX_VALUE if unknown
     */
    public int getArfcn() {
        return mArfcn;
    }

    /**
     *
     *
     * @return 6-bit Base Station Identity Code, Integer.MAX_VALUE if unknown
     */
    public int getBsic() {
        return mBsic;
    }

    /**
     *
     *
     * @return Integer.MAX_VALUE, undefined for GSM
     */
    @java.lang.Deprecated
    public int getPsc() {
        return java.lang.Integer.MAX_VALUE;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mMcc, mMnc, mLac, mCid);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof android.telephony.CellIdentityGsm)) {
            return false;
        }
        android.telephony.CellIdentityGsm o = ((android.telephony.CellIdentityGsm) (other));
        return (((((mMcc == o.mMcc) && (mMnc == o.mMnc)) && (mLac == o.mLac)) && (mCid == o.mCid)) && (mArfcn == o.mArfcn)) && (mBsic == o.mBsic);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("CellIdentityGsm:{");
        sb.append(" mMcc=").append(mMcc);
        sb.append(" mMnc=").append(mMnc);
        sb.append(" mLac=").append(mLac);
        sb.append(" mCid=").append(mCid);
        sb.append(" mArfcn=").append(mArfcn);
        sb.append(" mBsic=").append("0x").append(java.lang.Integer.toHexString(mBsic));
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
        if (android.telephony.CellIdentityGsm.DBG)
            android.telephony.CellIdentityGsm.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mMcc);
        dest.writeInt(mMnc);
        dest.writeInt(mLac);
        dest.writeInt(mCid);
        dest.writeInt(mArfcn);
        dest.writeInt(mBsic);
    }

    /**
     * Construct from Parcel, type has already been processed
     */
    private CellIdentityGsm(android.os.Parcel in) {
        mMcc = in.readInt();
        mMnc = in.readInt();
        mLac = in.readInt();
        mCid = in.readInt();
        mArfcn = in.readInt();
        int bsic = in.readInt();
        // In RIL BSIC is a UINT8, so 0xFF is the 'INVALID' designator
        // for inbound parcels
        if (bsic == 0xff)
            bsic = java.lang.Integer.MAX_VALUE;

        mBsic = bsic;
        if (android.telephony.CellIdentityGsm.DBG)
            android.telephony.CellIdentityGsm.log("CellIdentityGsm(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.SuppressWarnings("hiding")
    public static final android.os.Parcelable.Creator<android.telephony.CellIdentityGsm> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellIdentityGsm>() {
        @java.lang.Override
        public android.telephony.CellIdentityGsm createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellIdentityGsm(in);
        }

        @java.lang.Override
        public android.telephony.CellIdentityGsm[] newArray(int size) {
            return new android.telephony.CellIdentityGsm[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellIdentityGsm.LOG_TAG, s);
    }
}

