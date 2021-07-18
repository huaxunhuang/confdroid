/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * Data connection real time information
 *
 * TODO: How to handle multiple subscriptions?
 *
 * @unknown 
 */
public class DataConnectionRealTimeInfo implements android.os.Parcelable {
    private long mTime;// Time the info was collected since boot in nanos;


    public static final int DC_POWER_STATE_LOW = 1;

    public static final int DC_POWER_STATE_MEDIUM = 2;

    public static final int DC_POWER_STATE_HIGH = 3;

    public static final int DC_POWER_STATE_UNKNOWN = java.lang.Integer.MAX_VALUE;

    private int mDcPowerState;// DC_POWER_STATE_[LOW | MEDIUM | HIGH | UNKNOWN]


    /**
     * Constructor
     *
     * @unknown 
     */
    public DataConnectionRealTimeInfo(long time, int dcPowerState) {
        mTime = time;
        mDcPowerState = dcPowerState;
    }

    /**
     * Constructor
     *
     * @unknown 
     */
    public DataConnectionRealTimeInfo() {
        mTime = java.lang.Long.MAX_VALUE;
        mDcPowerState = android.telephony.DataConnectionRealTimeInfo.DC_POWER_STATE_UNKNOWN;
    }

    /**
     * Construct a PreciseCallState object from the given parcel.
     */
    private DataConnectionRealTimeInfo(android.os.Parcel in) {
        mTime = in.readLong();
        mDcPowerState = in.readInt();
    }

    /**
     *
     *
     * @return time the information was collected or Long.MAX_VALUE if unknown
     */
    public long getTime() {
        return mTime;
    }

    /**
     *
     *
     * @return DC_POWER_STATE_[LOW | MEDIUM | HIGH | UNKNOWN]
     */
    public int getDcPowerState() {
        return mDcPowerState;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(mTime);
        out.writeInt(mDcPowerState);
    }

    public static final android.os.Parcelable.Creator<android.telephony.DataConnectionRealTimeInfo> CREATOR = new android.os.Parcelable.Creator<android.telephony.DataConnectionRealTimeInfo>() {
        @java.lang.Override
        public android.telephony.DataConnectionRealTimeInfo createFromParcel(android.os.Parcel in) {
            return new android.telephony.DataConnectionRealTimeInfo(in);
        }

        @java.lang.Override
        public android.telephony.DataConnectionRealTimeInfo[] newArray(int size) {
            return new android.telephony.DataConnectionRealTimeInfo[size];
        }
    };

    @java.lang.Override
    public int hashCode() {
        final long prime = 17;
        long result = 1;
        result = (prime * result) + mTime;
        result += (prime * result) + mDcPowerState;
        return ((int) (result));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.telephony.DataConnectionRealTimeInfo other = ((android.telephony.DataConnectionRealTimeInfo) (obj));
        return (mTime == other.mTime) && (mDcPowerState == other.mDcPowerState);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("mTime=").append(mTime);
        sb.append(" mDcPowerState=").append(mDcPowerState);
        return sb.toString();
    }
}

