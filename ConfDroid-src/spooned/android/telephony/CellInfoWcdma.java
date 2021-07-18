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
public final class CellInfoWcdma extends android.telephony.CellInfo implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellInfoWcdma";

    private static final boolean DBG = false;

    private android.telephony.CellIdentityWcdma mCellIdentityWcdma;

    private android.telephony.CellSignalStrengthWcdma mCellSignalStrengthWcdma;

    /**
     *
     *
     * @unknown 
     */
    public CellInfoWcdma() {
        super();
        mCellIdentityWcdma = new android.telephony.CellIdentityWcdma();
        mCellSignalStrengthWcdma = new android.telephony.CellSignalStrengthWcdma();
    }

    /**
     *
     *
     * @unknown 
     */
    public CellInfoWcdma(android.telephony.CellInfoWcdma ci) {
        super(ci);
        this.mCellIdentityWcdma = ci.mCellIdentityWcdma.copy();
        this.mCellSignalStrengthWcdma = ci.mCellSignalStrengthWcdma.copy();
    }

    public android.telephony.CellIdentityWcdma getCellIdentity() {
        return mCellIdentityWcdma;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellIdentity(android.telephony.CellIdentityWcdma cid) {
        mCellIdentityWcdma = cid;
    }

    public android.telephony.CellSignalStrengthWcdma getCellSignalStrength() {
        return mCellSignalStrengthWcdma;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellSignalStrength(android.telephony.CellSignalStrengthWcdma css) {
        mCellSignalStrengthWcdma = css;
    }

    /**
     *
     *
     * @return hash code
     */
    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + mCellIdentityWcdma.hashCode()) + mCellSignalStrengthWcdma.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!super.equals(other)) {
            return false;
        }
        try {
            android.telephony.CellInfoWcdma o = ((android.telephony.CellInfoWcdma) (other));
            return mCellIdentityWcdma.equals(o.mCellIdentityWcdma) && mCellSignalStrengthWcdma.equals(o.mCellSignalStrengthWcdma);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("CellInfoWcdma:{");
        sb.append(super.toString());
        sb.append(" ").append(mCellIdentityWcdma);
        sb.append(" ").append(mCellSignalStrengthWcdma);
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
        super.writeToParcel(dest, flags, android.telephony.CellInfo.TYPE_WCDMA);
        mCellIdentityWcdma.writeToParcel(dest, flags);
        mCellSignalStrengthWcdma.writeToParcel(dest, flags);
    }

    /**
     * Construct a CellInfoWcdma object from the given parcel
     * where the token is already been processed.
     */
    private CellInfoWcdma(android.os.Parcel in) {
        super(in);
        mCellIdentityWcdma = android.telephony.CellIdentityWcdma.CREATOR.createFromParcel(in);
        mCellSignalStrengthWcdma = android.telephony.CellSignalStrengthWcdma.CREATOR.createFromParcel(in);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.telephony.CellInfoWcdma> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellInfoWcdma>() {
        @java.lang.Override
        public android.telephony.CellInfoWcdma createFromParcel(android.os.Parcel in) {
            in.readInt();// Skip past token, we know what it is

            return android.telephony.CellInfoWcdma.createFromParcelBody(in);
        }

        @java.lang.Override
        public android.telephony.CellInfoWcdma[] newArray(int size) {
            return new android.telephony.CellInfoWcdma[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    protected static android.telephony.CellInfoWcdma createFromParcelBody(android.os.Parcel in) {
        return new android.telephony.CellInfoWcdma(in);
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellInfoWcdma.LOG_TAG, s);
    }
}

