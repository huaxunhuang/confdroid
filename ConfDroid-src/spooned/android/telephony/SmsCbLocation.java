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
 * Represents the location and geographical scope of a cell broadcast message.
 * For GSM/UMTS, the Location Area and Cell ID are set when the broadcast
 * geographical scope is cell wide or Location Area wide. For CDMA, the
 * broadcast geographical scope is always PLMN wide.
 *
 * @unknown 
 */
public class SmsCbLocation implements android.os.Parcelable {
    /**
     * The PLMN. Note that this field may be an empty string, but isn't allowed to be null.
     */
    private final java.lang.String mPlmn;

    private final int mLac;

    private final int mCid;

    /**
     * Construct an empty location object. This is used for some test cases, and for
     * cell broadcasts saved in older versions of the database without location info.
     */
    public SmsCbLocation() {
        mPlmn = "";
        mLac = -1;
        mCid = -1;
    }

    /**
     * Construct a location object for the PLMN. This class is immutable, so
     * the same object can be reused for multiple broadcasts.
     */
    public SmsCbLocation(java.lang.String plmn) {
        mPlmn = plmn;
        mLac = -1;
        mCid = -1;
    }

    /**
     * Construct a location object for the PLMN, LAC, and Cell ID. This class is immutable, so
     * the same object can be reused for multiple broadcasts.
     */
    public SmsCbLocation(java.lang.String plmn, int lac, int cid) {
        mPlmn = plmn;
        mLac = lac;
        mCid = cid;
    }

    /**
     * Initialize the object from a Parcel.
     */
    public SmsCbLocation(android.os.Parcel in) {
        mPlmn = in.readString();
        mLac = in.readInt();
        mCid = in.readInt();
    }

    /**
     * Returns the MCC/MNC of the network as a String.
     *
     * @return the PLMN identifier (MCC+MNC) as a String
     */
    public java.lang.String getPlmn() {
        return mPlmn;
    }

    /**
     * Returns the GSM location area code, or UMTS service area code.
     *
     * @return location area code, -1 if unknown, 0xffff max legal value
     */
    public int getLac() {
        return mLac;
    }

    /**
     * Returns the GSM or UMTS cell ID.
     *
     * @return gsm cell id, -1 if unknown, 0xffff max legal value
     */
    public int getCid() {
        return mCid;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = mPlmn.hashCode();
        hash = (hash * 31) + mLac;
        hash = (hash * 31) + mCid;
        return hash;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (!(o instanceof android.telephony.SmsCbLocation))) {
            return false;
        }
        android.telephony.SmsCbLocation other = ((android.telephony.SmsCbLocation) (o));
        return (mPlmn.equals(other.mPlmn) && (mLac == other.mLac)) && (mCid == other.mCid);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((('[' + mPlmn) + ',') + mLac) + ',') + mCid) + ']';
    }

    /**
     * Test whether this location is within the location area of the specified object.
     *
     * @param area
     * 		the location area to compare with this location
     * @return true if this location is contained within the specified location area
     */
    public boolean isInLocationArea(android.telephony.SmsCbLocation area) {
        if ((mCid != (-1)) && (mCid != area.mCid)) {
            return false;
        }
        if ((mLac != (-1)) && (mLac != area.mLac)) {
            return false;
        }
        return mPlmn.equals(area.mPlmn);
    }

    /**
     * Test whether this location is within the location area of the CellLocation.
     *
     * @param plmn
     * 		the PLMN to use for comparison
     * @param lac
     * 		the Location Area (GSM) or Service Area (UMTS) to compare with
     * @param cid
     * 		the Cell ID to compare with
     * @return true if this location is contained within the specified PLMN, LAC, and Cell ID
     */
    public boolean isInLocationArea(java.lang.String plmn, int lac, int cid) {
        if (!mPlmn.equals(plmn)) {
            return false;
        }
        if ((mLac != (-1)) && (mLac != lac)) {
            return false;
        }
        if ((mCid != (-1)) && (mCid != cid)) {
            return false;
        }
        return true;
    }

    /**
     * Flatten this object into a Parcel.
     *
     * @param dest
     * 		The Parcel in which the object should be written.
     * @param flags
     * 		Additional flags about how the object should be written (ignored).
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mPlmn);
        dest.writeInt(mLac);
        dest.writeInt(mCid);
    }

    public static final android.os.Parcelable.Creator<android.telephony.SmsCbLocation> CREATOR = new android.os.Parcelable.Creator<android.telephony.SmsCbLocation>() {
        @java.lang.Override
        public android.telephony.SmsCbLocation createFromParcel(android.os.Parcel in) {
            return new android.telephony.SmsCbLocation(in);
        }

        @java.lang.Override
        public android.telephony.SmsCbLocation[] newArray(int size) {
            return new android.telephony.SmsCbLocation[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in the marshalled representation.
     *
     * @return a bitmask indicating this Parcelable contains no special objects
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

