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
 * Immutable cell information from a point in time.
 */
public final class CellInfoCdma extends android.telephony.CellInfo implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellInfoCdma";

    private static final boolean DBG = false;

    private android.telephony.CellIdentityCdma mCellIdentityCdma;

    private android.telephony.CellSignalStrengthCdma mCellSignalStrengthCdma;

    /**
     *
     *
     * @unknown 
     */
    public CellInfoCdma() {
        super();
        mCellIdentityCdma = new android.telephony.CellIdentityCdma();
        mCellSignalStrengthCdma = new android.telephony.CellSignalStrengthCdma();
    }

    /**
     *
     *
     * @unknown 
     */
    public CellInfoCdma(android.telephony.CellInfoCdma ci) {
        super(ci);
        this.mCellIdentityCdma = ci.mCellIdentityCdma.copy();
        this.mCellSignalStrengthCdma = ci.mCellSignalStrengthCdma.copy();
    }

    public android.telephony.CellIdentityCdma getCellIdentity() {
        return mCellIdentityCdma;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellIdentity(android.telephony.CellIdentityCdma cid) {
        mCellIdentityCdma = cid;
    }

    public android.telephony.CellSignalStrengthCdma getCellSignalStrength() {
        return mCellSignalStrengthCdma;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellSignalStrength(android.telephony.CellSignalStrengthCdma css) {
        mCellSignalStrengthCdma = css;
    }

    /**
     *
     *
     * @return hash code
     */
    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + mCellIdentityCdma.hashCode()) + mCellSignalStrengthCdma.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!super.equals(other)) {
            return false;
        }
        try {
            android.telephony.CellInfoCdma o = ((android.telephony.CellInfoCdma) (other));
            return mCellIdentityCdma.equals(o.mCellIdentityCdma) && mCellSignalStrengthCdma.equals(o.mCellSignalStrengthCdma);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("CellInfoCdma:{");
        sb.append(super.toString());
        sb.append(" ").append(mCellIdentityCdma);
        sb.append(" ").append(mCellSignalStrengthCdma);
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
        super.writeToParcel(dest, flags, android.telephony.CellInfo.TYPE_CDMA);
        mCellIdentityCdma.writeToParcel(dest, flags);
        mCellSignalStrengthCdma.writeToParcel(dest, flags);
    }

    /**
     * Construct a CellInfoCdma object from the given parcel
     * where the token is already been processed.
     */
    private CellInfoCdma(android.os.Parcel in) {
        super(in);
        mCellIdentityCdma = android.telephony.CellIdentityCdma.CREATOR.createFromParcel(in);
        mCellSignalStrengthCdma = android.telephony.CellSignalStrengthCdma.CREATOR.createFromParcel(in);
        if (android.telephony.CellInfoCdma.DBG)
            android.telephony.CellInfoCdma.log("CellInfoCdma(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.telephony.CellInfoCdma> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellInfoCdma>() {
        @java.lang.Override
        public android.telephony.CellInfoCdma createFromParcel(android.os.Parcel in) {
            in.readInt();// Skip past token, we know what it is

            return android.telephony.CellInfoCdma.createFromParcelBody(in);
        }

        @java.lang.Override
        public android.telephony.CellInfoCdma[] newArray(int size) {
            return new android.telephony.CellInfoCdma[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    protected static android.telephony.CellInfoCdma createFromParcelBody(android.os.Parcel in) {
        return new android.telephony.CellInfoCdma(in);
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellInfoCdma.LOG_TAG, s);
    }
}

