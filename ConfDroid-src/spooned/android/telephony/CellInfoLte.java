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
public final class CellInfoLte extends android.telephony.CellInfo implements android.os.Parcelable {
    private static final java.lang.String LOG_TAG = "CellInfoLte";

    private static final boolean DBG = false;

    private android.telephony.CellIdentityLte mCellIdentityLte;

    private android.telephony.CellSignalStrengthLte mCellSignalStrengthLte;

    /**
     *
     *
     * @unknown 
     */
    public CellInfoLte() {
        super();
        mCellIdentityLte = new android.telephony.CellIdentityLte();
        mCellSignalStrengthLte = new android.telephony.CellSignalStrengthLte();
    }

    /**
     *
     *
     * @unknown 
     */
    public CellInfoLte(android.telephony.CellInfoLte ci) {
        super(ci);
        this.mCellIdentityLte = ci.mCellIdentityLte.copy();
        this.mCellSignalStrengthLte = ci.mCellSignalStrengthLte.copy();
    }

    public android.telephony.CellIdentityLte getCellIdentity() {
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("getCellIdentity: " + mCellIdentityLte);

        return mCellIdentityLte;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellIdentity(android.telephony.CellIdentityLte cid) {
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("setCellIdentity: " + cid);

        mCellIdentityLte = cid;
    }

    public android.telephony.CellSignalStrengthLte getCellSignalStrength() {
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("getCellSignalStrength: " + mCellSignalStrengthLte);

        return mCellSignalStrengthLte;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCellSignalStrength(android.telephony.CellSignalStrengthLte css) {
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("setCellSignalStrength: " + css);

        mCellSignalStrengthLte = css;
    }

    /**
     *
     *
     * @return hash code
     */
    @java.lang.Override
    public int hashCode() {
        return (super.hashCode() + mCellIdentityLte.hashCode()) + mCellSignalStrengthLte.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!super.equals(other)) {
            return false;
        }
        try {
            android.telephony.CellInfoLte o = ((android.telephony.CellInfoLte) (other));
            return mCellIdentityLte.equals(o.mCellIdentityLte) && mCellSignalStrengthLte.equals(o.mCellSignalStrengthLte);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("CellInfoLte:{");
        sb.append(super.toString());
        sb.append(" ").append(mCellIdentityLte);
        sb.append(" ").append(mCellSignalStrengthLte);
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
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("writeToParcel(Parcel, int): " + toString());

        super.writeToParcel(dest, flags, android.telephony.CellInfo.TYPE_LTE);
        mCellIdentityLte.writeToParcel(dest, flags);
        mCellSignalStrengthLte.writeToParcel(dest, flags);
    }

    /**
     * Construct a CellInfoLte object from the given parcel
     * where the TYPE_LTE token is already been processed.
     */
    private CellInfoLte(android.os.Parcel in) {
        super(in);
        mCellIdentityLte = android.telephony.CellIdentityLte.CREATOR.createFromParcel(in);
        mCellSignalStrengthLte = android.telephony.CellSignalStrengthLte.CREATOR.createFromParcel(in);
        if (android.telephony.CellInfoLte.DBG)
            android.telephony.CellInfoLte.log("CellInfoLte(Parcel): " + toString());

    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.telephony.CellInfoLte> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellInfoLte>() {
        @java.lang.Override
        public android.telephony.CellInfoLte createFromParcel(android.os.Parcel in) {
            in.readInt();// Skip past token, we know what it is

            return android.telephony.CellInfoLte.createFromParcelBody(in);
        }

        @java.lang.Override
        public android.telephony.CellInfoLte[] newArray(int size) {
            return new android.telephony.CellInfoLte[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    protected static android.telephony.CellInfoLte createFromParcelBody(android.os.Parcel in) {
        return new android.telephony.CellInfoLte(in);
    }

    /**
     * log
     */
    private static void log(java.lang.String s) {
        android.telephony.Rlog.w(android.telephony.CellInfoLte.LOG_TAG, s);
    }
}

