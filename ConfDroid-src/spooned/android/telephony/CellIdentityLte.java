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
 * CellIdentity is to represent a unique LTE cell
 */
public final class CellIdentityLte implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellIdentityLte";

    private static final boolean DBG = false;

    // 3-digit Mobile Country Code, 0..999
    private final int mMcc;

    // 2 or 3-digit Mobile Network Code, 0..999
    private final int mMnc;

    // 28-bit cell identity
    private final int mCi;

    // physical cell id 0..503
    private final int mPci;

    // 16-bit tracking area code
    private final int mTac;

    // 18-bit Absolute RF Channel Number
    private final int mEarfcn;

    /**
     *
     *
     * @unknown 
     */
    public CellIdentityLte() {
        mMcc = java.lang.Integer.MAX_VALUE;
        mMnc = java.lang.Integer.MAX_VALUE;
        mCi = java.lang.Integer.MAX_VALUE;
        mPci = java.lang.Integer.MAX_VALUE;
        mTac = java.lang.Integer.MAX_VALUE;
        mEarfcn = java.lang.Integer.MAX_VALUE;
    }

    /**
     *
     *
     * @param mcc
     * 		3-digit Mobile Country Code, 0..999
     * @param mnc
     * 		2 or 3-digit Mobile Network Code, 0..999
     * @param ci
     * 		28-bit Cell Identity
     * @param pci
     * 		Physical Cell Id 0..503
     * @param tac
     * 		16-bit Tracking Area Code
     * @unknown 
     */
    public CellIdentityLte(int mcc, int mnc, int ci, int pci, int tac) {
        this(mcc, mnc, ci, pci, tac, java.lang.Integer.MAX_VALUE);
    }

    /**
     *
     *
     * @param mcc
     * 		3-digit Mobile Country Code, 0..999
     * @param mnc
     * 		2 or 3-digit Mobile Network Code, 0..999
     * @param ci
     * 		28-bit Cell Identity
     * @param pci
     * 		Physical Cell Id 0..503
     * @param tac
     * 		16-bit Tracking Area Code
     * @param earfcn
     * 		18-bit LTE Absolute RF Channel Number
     * @unknown 
     */
    public CellIdentityLte(int mcc, int mnc, int ci, int pci, int tac, int earfcn) {
        mMcc = mcc;
        mMnc = mnc;
        mCi = ci;
        mPci = pci;
        mTac = tac;
        mEarfcn = earfcn;
    }

    private CellIdentityLte(android.telephony.CellIdentityLte cid) {
        mMcc = cid.mMcc;
        mMnc = cid.mMnc;
        mCi = cid.mCi;
        mPci = cid.mPci;
        mTac = cid.mTac;
        mEarfcn = cid.mEarfcn;
    }

    android.telephony.CellIdentityLte copy() {
        return new android.telephony.CellIdentityLte(this);
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
     * @return 28-bit Cell Identity, Integer.MAX_VALUE if unknown
     */
    public int getCi() {
        return mCi;
    }

    /**
     *
     *
     * @return Physical Cell Id 0..503, Integer.MAX_VALUE if unknown
     */
    public int getPci() {
        return mPci;
    }

    /**
     *
     *
     * @return 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown
     */
    public int getTac() {
        return mTac;
    }

    /**
     *
     *
     * @return 18-bit Absolute RF Channel Number, Integer.MAX_VALUE if unknown
     */
    public int getEarfcn() {
        return mEarfcn;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mMcc, mMnc, mCi, mPci, mTac);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof android.telephony.CellIdentityLte)) {
            return false;
        }
        android.telephony.CellIdentityLte o = ((android.telephony.CellIdentityLte) (other));
        return (((((mMcc == o.mMcc) && (mMnc == o.mMnc)) && (mCi == o.mCi)) && (mPci == o.mPci)) && (mTac == o.mTac)) && (mEarfcn == o.mEarfcn);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("CellIdentityLte:{");
        sb.append(" mMcc=");
        sb.append(mMcc);
        sb.append(" mMnc=");
        sb.append(mMnc);
        sb.append(" mCi=");
        sb.append(mCi);
        sb.append(" mPci=");
        sb.append(mPci);
        sb.append(" mTac=");
        sb.append(mTac);
        sb.append(" mEarfcn=");
        sb.append(mEarfcn);
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
        if (android.telephony.CellIdentityLte.DBG)
            android.telephony.CellIdentityLte.log("writeToParcel(Parcel, int): " + toString());

        dest.writeInt(mMcc);
        dest.writeInt(mMnc);
        dest.writeInt(mCi);
        dest.writeInt(mPci);
        dest.writeInt(mTac);
        dest.writeInt(mEarfcn);
    }

    /**
     * Construct from Parcel, type has already been processed
     */
    private CellIdentityLte(android.os.Parcel in) {
        mMcc = in.readInt();
        mMnc = in.readInt();
        mCi = in.readInt();
        mPci = in.readInt();
        mTac = in.readInt();
        mEarfcn = in.readInt();
        if (android.telephony.CellIdentityLte.DBG)
            android.telephony.CellIdentityLte.log("CellIdentityLte(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.SuppressWarnings("hiding")
    public static final android.os.Parcelable.Creator<android.telephony.CellIdentityLte> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellIdentityLte>() {
        @java.lang.Override
        public android.telephony.CellIdentityLte createFromParcel(android.os.Parcel in) {
            return new android.telephony.CellIdentityLte(in);
        }

        @java.lang.Override
        public android.telephony.CellIdentityLte[] newArray(int size) {
            return new android.telephony.CellIdentityLte[size];
        }
    };

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellIdentityLte.LOG_TAG, s);
    }
}

