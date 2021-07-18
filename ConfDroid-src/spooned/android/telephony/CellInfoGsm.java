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
public final class CellInfoGsm extends android.telephony.CellInfo implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellInfoGsm";

    private static final boolean DBG = false;

    private android.telephony.CellIdentityGsm mCellIdentityGsm;

    private android.telephony.CellSignalStrengthGsm mCellSignalStrengthGsm;

    /**
     *
     *
     * @unknown 
     */
    public CellInfoGsm() {
        super();
        mCellIdentityGsm = new android.telephony.CellIdentityGsm();
        mCellSignalStrengthGsm = new android.telephony.CellSignalStrengthGsm();
    }

    /**
     *
     *
     * @unknown 
     */
    public CellInfoGsm(android.telephony.CellInfoGsm ci) {
        super(ci);
        this.mCellIdentityGsm = ci.mCellIdentityGsm.copy();
        this.mCellSignalStrengthGsm = ci.mCellSignalStrengthGsm.copy();
    }

    public android.telephony.CellIdentityGsm getCellIdentity() {
        return mCellIdentityGsm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellIdentity(android.telephony.CellIdentityGsm cid) {
        mCellIdentityGsm = cid;
    }

    public android.telephony.CellSignalStrengthGsm getCellSignalStrength() {
        return mCellSignalStrengthGsm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellSignalStrength(android.telephony.CellSignalStrengthGsm css) {
        mCellSignalStrengthGsm = css;
    }

    /**
     *
     *
     * @return hash code
     */
    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + mCellIdentityGsm.hashCode()) + mCellSignalStrengthGsm.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!super.equals(other)) {
            return false;
        }
        try {
            android.telephony.CellInfoGsm o = ((android.telephony.CellInfoGsm) (other));
            return mCellIdentityGsm.equals(o.mCellIdentityGsm) && mCellSignalStrengthGsm.equals(o.mCellSignalStrengthGsm);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("CellInfoGsm:{");
        sb.append(super.toString());
        sb.append(" ").append(mCellIdentityGsm);
        sb.append(" ").append(mCellSignalStrengthGsm);
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
        super.writeToParcel(dest, flags, android.telephony.CellInfo.TYPE_GSM);
        mCellIdentityGsm.writeToParcel(dest, flags);
        mCellSignalStrengthGsm.writeToParcel(dest, flags);
    }

    /**
     * Construct a CellInfoGsm object from the given parcel
     * where the token is already been processed.
     */
    private CellInfoGsm(android.os.Parcel in) {
        super(in);
        mCellIdentityGsm = android.telephony.CellIdentityGsm.CREATOR.createFromParcel(in);
        mCellSignalStrengthGsm = android.telephony.CellSignalStrengthGsm.CREATOR.createFromParcel(in);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.telephony.CellInfoGsm> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellInfoGsm>() {
        @java.lang.Override
        public android.telephony.CellInfoGsm createFromParcel(android.os.Parcel in) {
            in.readInt();// Skip past token, we know what it is

            return android.telephony.CellInfoGsm.createFromParcelBody(in);
        }

        @java.lang.Override
        public android.telephony.CellInfoGsm[] newArray(int size) {
            return new android.telephony.CellInfoGsm[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    protected static android.telephony.CellInfoGsm createFromParcelBody(android.os.Parcel in) {
        return new android.telephony.CellInfoGsm(in);
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellInfoGsm.LOG_TAG, s);
    }
}

