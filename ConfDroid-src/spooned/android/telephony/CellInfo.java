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
public abstract class CellInfo implements android.os.Parcelable {
    // Type fields for parceling
    /**
     *
     *
     * @unknown 
     */
    protected static final int TYPE_GSM = 1;

    /**
     *
     *
     * @unknown 
     */
    protected static final int TYPE_CDMA = 2;

    /**
     *
     *
     * @unknown 
     */
    protected static final int TYPE_LTE = 3;

    /**
     *
     *
     * @unknown 
     */
    protected static final int TYPE_WCDMA = 4;

    // Type to distinguish where time stamp gets recorded.
    /**
     *
     *
     * @unknown 
     */
    public static final int TIMESTAMP_TYPE_UNKNOWN = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int TIMESTAMP_TYPE_ANTENNA = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int TIMESTAMP_TYPE_MODEM = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int TIMESTAMP_TYPE_OEM_RIL = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int TIMESTAMP_TYPE_JAVA_RIL = 4;

    // True if device is mRegistered to the mobile network
    private boolean mRegistered;

    // Observation time stamped as type in nanoseconds since boot
    private long mTimeStamp;

    // Where time stamp gets recorded.
    // Value of TIMESTAMP_TYPE_XXXX
    private int mTimeStampType;

    /**
     *
     *
     * @unknown 
     */
    protected CellInfo() {
        this.mRegistered = false;
        this.mTimeStampType = android.telephony.CellInfo.TIMESTAMP_TYPE_UNKNOWN;
        this.mTimeStamp = java.lang.Long.MAX_VALUE;
    }

    /**
     *
     *
     * @unknown 
     */
    protected CellInfo(android.telephony.CellInfo ci) {
        this.mRegistered = ci.mRegistered;
        this.mTimeStampType = ci.mTimeStampType;
        this.mTimeStamp = ci.mTimeStamp;
    }

    /**
     * True if this cell is registered to the mobile network
     */
    public boolean isRegistered() {
        return mRegistered;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRegistered(boolean registered) {
        mRegistered = registered;
    }

    /**
     * Approximate time of this cell information in nanos since boot
     */
    public long getTimeStamp() {
        return mTimeStamp;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    /**
     * Where time stamp gets recorded.
     *
     * @return one of TIMESTAMP_TYPE_XXXX
     * @unknown 
     */
    public int getTimeStampType() {
        return mTimeStampType;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTimeStampType(int timeStampType) {
        if ((timeStampType < android.telephony.CellInfo.TIMESTAMP_TYPE_UNKNOWN) || (timeStampType > android.telephony.CellInfo.TIMESTAMP_TYPE_JAVA_RIL)) {
            mTimeStampType = android.telephony.CellInfo.TIMESTAMP_TYPE_UNKNOWN;
        } else {
            mTimeStampType = timeStampType;
        }
    }

    @java.lang.Override
    public int hashCode() {
        int primeNum = 31;
        return (((mRegistered ? 0 : 1) * primeNum) + (((int) (mTimeStamp / 1000)) * primeNum)) + (mTimeStampType * primeNum);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        try {
            android.telephony.CellInfo o = ((android.telephony.CellInfo) (other));
            return ((mRegistered == o.mRegistered) && (mTimeStamp == o.mTimeStamp)) && (mTimeStampType == o.mTimeStampType);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
    }

    private static java.lang.String timeStampTypeToString(int type) {
        switch (type) {
            case 1 :
                return "antenna";
            case 2 :
                return "modem";
            case 3 :
                return "oem_ril";
            case 4 :
                return "java_ril";
            default :
                return "unknown";
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        java.lang.String timeStampType;
        sb.append("mRegistered=").append(mRegistered ? "YES" : "NO");
        timeStampType = android.telephony.CellInfo.timeStampTypeToString(mTimeStampType);
        sb.append(" mTimeStampType=").append(timeStampType);
        sb.append(" mTimeStamp=").append(mTimeStamp).append("ns");
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
    public abstract void writeToParcel(android.os.Parcel dest, int flags);

    /**
     * Used by child classes for parceling.
     *
     * @unknown 
     */
    protected void writeToParcel(android.os.Parcel dest, int flags, int type) {
        dest.writeInt(type);
        dest.writeInt(mRegistered ? 1 : 0);
        dest.writeInt(mTimeStampType);
        dest.writeLong(mTimeStamp);
    }

    /**
     * Used by child classes for parceling
     *
     * @unknown 
     */
    protected CellInfo(android.os.Parcel in) {
        mRegistered = (in.readInt() == 1) ? true : false;
        mTimeStampType = in.readInt();
        mTimeStamp = in.readLong();
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.telephony.CellInfo> CREATOR = new android.os.Parcelable.Creator<android.telephony.CellInfo>() {
        @java.lang.Override
        public android.telephony.CellInfo createFromParcel(android.os.Parcel in) {
            int type = in.readInt();
            switch (type) {
                case android.telephony.CellInfo.TYPE_GSM :
                    return android.telephony.CellInfoGsm.createFromParcelBody(in);
                case android.telephony.CellInfo.TYPE_CDMA :
                    return android.telephony.CellInfoCdma.createFromParcelBody(in);
                case android.telephony.CellInfo.TYPE_LTE :
                    return android.telephony.CellInfoLte.createFromParcelBody(in);
                case android.telephony.CellInfo.TYPE_WCDMA :
                    return android.telephony.CellInfoWcdma.createFromParcelBody(in);
                default :
                    throw new java.lang.RuntimeException("Bad CellInfo Parcel");
            }
        }

        @java.lang.Override
        public android.telephony.CellInfo[] newArray(int size) {
            return new android.telephony.CellInfo[size];
        }
    };
}

