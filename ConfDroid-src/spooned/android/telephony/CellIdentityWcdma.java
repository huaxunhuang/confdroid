/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * CellIdentity to represent a unique UMTS cell
 */
public final class CellIdentityWcdma implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellIdentityWcdma";

    private static final boolean DBG = false;

    // 3-digit Mobile Country Code, 0..999
    private final int mMcc;

    // 2 or 3-digit Mobile Network Code, 0..999
    private final int mMnc;

    // 16-bit Location Area Code, 0..65535
    private final int mLac;

    // 28-bit UMTS Cell Identity described in TS 25.331, 0..268435455
    private final int mCid;

    // 9-bit UMTS Primary Scrambling Code described in TS 25.331, 0..511
    private final int mPsc;

    // 16-bit UMTS Absolute RF Channel Number
    private final int mUarfcn;

    /**
     *
     *
     * @unknown 
     */
    public CellIdentityWcdma() {
        mMcc = java.lang.Integer.MAX_VALUE;
        mMnc = java.lang.Integer.MAX_VALUE;
        mLac = java.lang.Integer.MAX_VALUE;
        mCid = java.lang.Integer.MAX_VALUE;
        mPsc = java.lang.Integer.MAX_VALUE;
        mUarfcn = java.lang.Integer.MAX_VALUE;
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
     * 		28-bit UMTS Cell Identity
     * @param psc
     * 		9-bit UMTS Primary Scrambling Code
     * @unknown 
     */
    public CellIdentityWcdma(int mcc, int mnc, int lac, int cid, int psc) {
        this(mcc, mnc, lac, cid, psc, java.lang.Integer.MAX_VALUE);
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
     * 		28-bit UMTS Cell Identity
     * @param psc
     * 		9-bit UMTS Primary Scrambling Code
     * @param uarfcn
     * 		16-bit UMTS Absolute RF Channel Number
     * @unknown 
     */
    public CellIdentityWcdma(int mcc, int mnc, int lac, int cid, int psc, int uarfcn) {
        mMcc = mcc;
        mMnc = mnc;
        mLac = lac;
        mCid = cid;
        mPsc = psc;
        mUarfcn = uarfcn;
    }

    private CellIdentityWcdma(android.telephony.CellIdentityWcdma cid) {
        mMcc = cid.mMcc;
        mMnc = cid.mMnc;
        mLac = cid.mLac;
        mCid = cid.mCid;
        mPsc = cid.mPsc;
        mUarfcn = cid.mUarfcn;
    }

    android.telephony.CellIdentityWcdma copy() {
        return new android.telephony.CellIdentityWcdma(this);
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
    28-bit UMTS Cell Identity described in TS 25.331, 0..268435455, Integer.MAX_VALUE if unknown
     */
    public int getCid() {
        return mCid;
    }

    /**
     *
     *
     * @return 9-bit UMTS Primary Scrambling Code described in TS 25.331, 0..511, Integer.MAX_VALUE
    if unknown
     */
    public int getPsc() {
        return mPsc;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mMcc, mMnc, mLac, mCid, mPsc);
    }

    /**
     *
     *
     * @return 16-bit UMTS Absolute RF Channel Number, Integer.MAX_VALUE if unknown
     */
    public int getUarfcn() {
        return mUarfcn;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof android.telephony.CellIdentityWcdma)) {
            return false;
        }
        android.telephony.CellIdentityWcdma o = ((android.telephony.CellIdentityWcdma) (other));
        return (((((mMcc == o.mMcc) && (mMnc == o.mMnc)) && (mLac == o.mLac)) && (mCid == o.mCid)) && (mPsc == o.mPsc)) && (mUarfcn == o.mUarfcn);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("CellIdentityWcdma:{");
        sb.append(" mMcc=").append(mMcc);
        sb.append(" mMnc=").append(mMnc);
        sb.append(" mLac=").append(mLac);
        sb.append(" mCid=").append(mCid);
        sb.append(" mPsc=").append(mPsc);
        sb.append(" mUarfcn=").append(mUarfcn);
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
        if (android.telephony.CellIdentityWcdma.DBG)
            android.telephony.CellIdentityWcdma.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mMcc);
        dest.writeInt(mMnc);
        dest.writeInt(mLac);
        dest.writeInt(mCid);
        dest.writeInt(mPsc);
        dest.writeInt(mUarfcn);
    }

    /**
     * Construct from Parcel, type has already been processed
     */
    private CellIdentityWcdma(android.os.Parcel in) {
        mMcc = in.readInt();
        mMnc = in.readInt();
        mLac = in.readInt();
        mCid = in.readInt();
        mPsc = in.readInt();
        mUarfcn = in.readInt();
        if (android.telephony.CellIdentityWcdma.DBG)
            android.telephony.CellIdentityWcdma.log("CellIdentityWcdma(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.SuppressWarnings("hiding")
    public static final android.os.Parcelable.Creator<android.telephony.CellIdentityWcdma> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellIdentityWcdma>() {
        @java.lang.Override
        public android.telephony.CellIdentityWcdma createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellIdentityWcdma(in);
        }

        @java.lang.Override
        public android.telephony.CellIdentityWcdma[] newArray(int size) {
            return new android.telephony.CellIdentityWcdma[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellIdentityWcdma.LOG_TAG, s);
    }
}

