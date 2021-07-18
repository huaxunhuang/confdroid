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
 * Contains precise data connection state.
 *
 * The following data connection information is included in returned PreciseDataConnectionState:
 *
 * <ul>
 *   <li>Data connection state.
 *   <li>Network type of the connection.
 *   <li>APN type.
 *   <li>APN.
 *   <li>Data connection change reason.
 *   <li>The properties of the network link.
 *   <li>Data connection fail cause.
 * </ul>
 *
 * @unknown 
 */
public class PreciseDataConnectionState implements android.os.Parcelable {
    private int mState = android.telephony.TelephonyManager.DATA_UNKNOWN;

    private int mNetworkType = android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN;

    private java.lang.String mAPNType = "";

    private java.lang.String mAPN = "";

    private java.lang.String mReason = "";

    private android.net.LinkProperties mLinkProperties = null;

    private java.lang.String mFailCause = "";

    /**
     * Constructor
     *
     * @unknown 
     */
    public PreciseDataConnectionState(int state, int networkType, java.lang.String apnType, java.lang.String apn, java.lang.String reason, android.net.LinkProperties linkProperties, java.lang.String failCause) {
        mState = state;
        mNetworkType = networkType;
        mAPNType = apnType;
        mAPN = apn;
        mReason = reason;
        mLinkProperties = linkProperties;
        mFailCause = failCause;
    }

    /**
     * Empty Constructor
     *
     * @unknown 
     */
    public PreciseDataConnectionState() {
    }

    /**
     * Construct a PreciseDataConnectionState object from the given parcel.
     */
    private PreciseDataConnectionState(android.os.Parcel in) {
        mState = in.readInt();
        mNetworkType = in.readInt();
        mAPNType = in.readString();
        mAPN = in.readString();
        mReason = in.readString();
        mLinkProperties = ((android.net.LinkProperties) (in.readParcelable(null)));
        mFailCause = in.readString();
    }

    /**
     * Get data connection state
     *
     * @see TelephonyManager#DATA_UNKNOWN
     * @see TelephonyManager#DATA_DISCONNECTED
     * @see TelephonyManager#DATA_CONNECTING
     * @see TelephonyManager#DATA_CONNECTED
     * @see TelephonyManager#DATA_SUSPENDED
     */
    public int getDataConnectionState() {
        return mState;
    }

    /**
     * Get data connection network type
     *
     * @see TelephonyManager#NETWORK_TYPE_UNKNOWN
     * @see TelephonyManager#NETWORK_TYPE_GPRS
     * @see TelephonyManager#NETWORK_TYPE_EDGE
     * @see TelephonyManager#NETWORK_TYPE_UMTS
     * @see TelephonyManager#NETWORK_TYPE_CDMA
     * @see TelephonyManager#NETWORK_TYPE_EVDO_0
     * @see TelephonyManager#NETWORK_TYPE_EVDO_A
     * @see TelephonyManager#NETWORK_TYPE_1xRTT
     * @see TelephonyManager#NETWORK_TYPE_HSDPA
     * @see TelephonyManager#NETWORK_TYPE_HSUPA
     * @see TelephonyManager#NETWORK_TYPE_HSPA
     * @see TelephonyManager#NETWORK_TYPE_IDEN
     * @see TelephonyManager#NETWORK_TYPE_EVDO_B
     * @see TelephonyManager#NETWORK_TYPE_LTE
     * @see TelephonyManager#NETWORK_TYPE_EHRPD
     * @see TelephonyManager#NETWORK_TYPE_HSPAP
     */
    public int getDataConnectionNetworkType() {
        return mNetworkType;
    }

    /**
     * Get data connection APN type
     */
    public java.lang.String getDataConnectionAPNType() {
        return mAPNType;
    }

    /**
     * Get data connection APN.
     */
    public java.lang.String getDataConnectionAPN() {
        return mAPN;
    }

    /**
     * Get data connection change reason.
     */
    public java.lang.String getDataConnectionChangeReason() {
        return mReason;
    }

    /**
     * Get the properties of the network link.
     */
    public android.net.LinkProperties getDataConnectionLinkProperties() {
        return mLinkProperties;
    }

    /**
     * Get data connection fail cause, in case there was a failure.
     */
    public java.lang.String getDataConnectionFailCause() {
        return mFailCause;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mState);
        out.writeInt(mNetworkType);
        out.writeString(mAPNType);
        out.writeString(mAPN);
        out.writeString(mReason);
        out.writeParcelable(mLinkProperties, flags);
        out.writeString(mFailCause);
    }

    public static final android.os.Parcelable.Creator<android.telephony.PreciseDataConnectionState> CREATOR = new android.os.Parcelable.Creator<android.telephony.PreciseDataConnectionState>() {
        public android.telephony.PreciseDataConnectionState createFromParcel(android.os.Parcel in) {
            return new android.telephony.PreciseDataConnectionState(in);
        }

        public android.telephony.PreciseDataConnectionState[] newArray(int size) {
            return new android.telephony.PreciseDataConnectionState[size];
        }
    };

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mState;
        result = (prime * result) + mNetworkType;
        result = (prime * result) + (mAPNType == null ? 0 : mAPNType.hashCode());
        result = (prime * result) + (mAPN == null ? 0 : mAPN.hashCode());
        result = (prime * result) + (mReason == null ? 0 : mReason.hashCode());
        result = (prime * result) + (mLinkProperties == null ? 0 : mLinkProperties.hashCode());
        result = (prime * result) + (mFailCause == null ? 0 : mFailCause.hashCode());
        return result;
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
        android.telephony.PreciseDataConnectionState other = ((android.telephony.PreciseDataConnectionState) (obj));
        if (mAPN == null) {
            if (other.mAPN != null) {
                return false;
            }
        } else
            if (!mAPN.equals(other.mAPN)) {
                return false;
            }

        if (mAPNType == null) {
            if (other.mAPNType != null) {
                return false;
            }
        } else
            if (!mAPNType.equals(other.mAPNType)) {
                return false;
            }

        if (mFailCause == null) {
            if (other.mFailCause != null) {
                return false;
            }
        } else
            if (!mFailCause.equals(other.mFailCause)) {
                return false;
            }

        if (mLinkProperties == null) {
            if (other.mLinkProperties != null) {
                return false;
            }
        } else
            if (!mLinkProperties.equals(other.mLinkProperties)) {
                return false;
            }

        if (mNetworkType != other.mNetworkType) {
            return false;
        }
        if (mReason == null) {
            if (other.mReason != null) {
                return false;
            }
        } else
            if (!mReason.equals(other.mReason)) {
                return false;
            }

        if (mState != other.mState) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Data Connection state: " + mState);
        sb.append(", Network type: " + mNetworkType);
        sb.append(", APN type: " + mAPNType);
        sb.append(", APN: " + mAPN);
        sb.append(", Change reason: " + mReason);
        sb.append(", Link properties: " + mLinkProperties);
        sb.append(", Fail cause: " + mFailCause);
        return sb.toString();
    }
}

