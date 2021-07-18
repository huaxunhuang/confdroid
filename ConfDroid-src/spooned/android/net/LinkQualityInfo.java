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
package android.net;


/**
 * Class that represents useful attributes of generic network links
 *  such as the upload/download throughput or packet error rate.
 *  Generally speaking, you should be dealing with instances of
 *  LinkQualityInfo subclasses, such as {@link android.net.#WifiLinkQualityInfo}
 *  or {@link android.net.#MobileLinkQualityInfo} which provide additional
 *  information.
 *
 * @unknown 
 */
public class LinkQualityInfo implements android.os.Parcelable {
    /**
     * Represents a value that you can use to test if an integer field is set to a good value
     */
    public static final int UNKNOWN_INT = java.lang.Integer.MAX_VALUE;

    /**
     * Represents a value that you can use to test if a long field is set to a good value
     */
    public static final long UNKNOWN_LONG = java.lang.Long.MAX_VALUE;

    public static final int NORMALIZED_MIN_SIGNAL_STRENGTH = 0;

    public static final int NORMALIZED_MAX_SIGNAL_STRENGTH = 99;

    public static final int NORMALIZED_SIGNAL_STRENGTH_RANGE = (android.net.LinkQualityInfo.NORMALIZED_MAX_SIGNAL_STRENGTH - android.net.LinkQualityInfo.NORMALIZED_MIN_SIGNAL_STRENGTH) + 1;

    /* Network type as defined by ConnectivityManager */
    private int mNetworkType = android.net.ConnectivityManager.TYPE_NONE;

    private int mNormalizedSignalStrength = android.net.LinkQualityInfo.UNKNOWN_INT;

    private long mPacketCount = android.net.LinkQualityInfo.UNKNOWN_LONG;

    private long mPacketErrorCount = android.net.LinkQualityInfo.UNKNOWN_LONG;

    private int mTheoreticalTxBandwidth = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mTheoreticalRxBandwidth = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mTheoreticalLatency = android.net.LinkQualityInfo.UNKNOWN_INT;

    /* Timestamp when last sample was made available */
    private long mLastDataSampleTime = android.net.LinkQualityInfo.UNKNOWN_LONG;

    /* Sample duration in millisecond */
    private int mDataSampleDuration = android.net.LinkQualityInfo.UNKNOWN_INT;

    public LinkQualityInfo() {
    }

    /**
     * Implement the Parcelable interface
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface.
     */
    protected static final int OBJECT_TYPE_LINK_QUALITY_INFO = 1;

    protected static final int OBJECT_TYPE_WIFI_LINK_QUALITY_INFO = 2;

    protected static final int OBJECT_TYPE_MOBILE_LINK_QUALITY_INFO = 3;

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcel(dest, flags, android.net.LinkQualityInfo.OBJECT_TYPE_LINK_QUALITY_INFO);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel dest, int flags, int objectType) {
        dest.writeInt(objectType);
        dest.writeInt(mNetworkType);
        dest.writeInt(mNormalizedSignalStrength);
        dest.writeLong(mPacketCount);
        dest.writeLong(mPacketErrorCount);
        dest.writeInt(mTheoreticalTxBandwidth);
        dest.writeInt(mTheoreticalRxBandwidth);
        dest.writeInt(mTheoreticalLatency);
        dest.writeLong(mLastDataSampleTime);
        dest.writeInt(mDataSampleDuration);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final android.os.Parcelable.Creator<android.net.LinkQualityInfo> CREATOR = new android.os.Parcelable.Creator<android.net.LinkQualityInfo>() {
        public android.net.LinkQualityInfo createFromParcel(android.os.Parcel in) {
            int objectType = in.readInt();
            if (objectType == android.net.LinkQualityInfo.OBJECT_TYPE_LINK_QUALITY_INFO) {
                android.net.LinkQualityInfo li = new android.net.LinkQualityInfo();
                li.initializeFromParcel(in);
                return li;
            } else
                if (objectType == android.net.LinkQualityInfo.OBJECT_TYPE_WIFI_LINK_QUALITY_INFO) {
                    return android.net.WifiLinkQualityInfo.createFromParcelBody(in);
                } else
                    if (objectType == android.net.LinkQualityInfo.OBJECT_TYPE_MOBILE_LINK_QUALITY_INFO) {
                        return android.net.MobileLinkQualityInfo.createFromParcelBody(in);
                    } else {
                        return null;
                    }


        }

        public android.net.LinkQualityInfo[] newArray(int size) {
            return new android.net.LinkQualityInfo[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    protected void initializeFromParcel(android.os.Parcel in) {
        mNetworkType = in.readInt();
        mNormalizedSignalStrength = in.readInt();
        mPacketCount = in.readLong();
        mPacketErrorCount = in.readLong();
        mTheoreticalTxBandwidth = in.readInt();
        mTheoreticalRxBandwidth = in.readInt();
        mTheoreticalLatency = in.readInt();
        mLastDataSampleTime = in.readLong();
        mDataSampleDuration = in.readInt();
    }

    /**
     * returns the type of network this link is connected to
     *
     * @return network type as defined by {@link android.net.ConnectivityManager} or
    {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getNetworkType() {
        return mNetworkType;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setNetworkType(int networkType) {
        mNetworkType = networkType;
    }

    /**
     * returns the signal strength normalized across multiple types of networks
     *
     * @return an integer value from 0 - 99 or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getNormalizedSignalStrength() {
        return mNormalizedSignalStrength;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setNormalizedSignalStrength(int normalizedSignalStrength) {
        mNormalizedSignalStrength = normalizedSignalStrength;
    }

    /**
     * returns the total number of packets sent or received in sample duration
     *
     * @return number of packets or {@link android.net.LinkQualityInfo#UNKNOWN_LONG}
     */
    public long getPacketCount() {
        return mPacketCount;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setPacketCount(long packetCount) {
        mPacketCount = packetCount;
    }

    /**
     * returns the total number of packets errors encountered in sample duration
     *
     * @return number of errors or {@link android.net.LinkQualityInfo#UNKNOWN_LONG}
     */
    public long getPacketErrorCount() {
        return mPacketErrorCount;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setPacketErrorCount(long packetErrorCount) {
        mPacketErrorCount = packetErrorCount;
    }

    /**
     * returns the theoretical upload bandwidth of this network
     *
     * @return bandwidth in Kbps or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getTheoreticalTxBandwidth() {
        return mTheoreticalTxBandwidth;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTheoreticalTxBandwidth(int theoreticalTxBandwidth) {
        mTheoreticalTxBandwidth = theoreticalTxBandwidth;
    }

    /**
     * returns the theoretical download bandwidth of this network
     *
     * @return bandwidth in Kbps or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getTheoreticalRxBandwidth() {
        return mTheoreticalRxBandwidth;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTheoreticalRxBandwidth(int theoreticalRxBandwidth) {
        mTheoreticalRxBandwidth = theoreticalRxBandwidth;
    }

    /**
     * returns the theoretical latency of this network
     *
     * @return latency in milliseconds or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getTheoreticalLatency() {
        return mTheoreticalLatency;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setTheoreticalLatency(int theoreticalLatency) {
        mTheoreticalLatency = theoreticalLatency;
    }

    /**
     * returns the time stamp of the last sample
     *
     * @return milliseconds elapsed since start and sample time or
    {@link android.net.LinkQualityInfo#UNKNOWN_LONG}
     */
    public long getLastDataSampleTime() {
        return mLastDataSampleTime;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLastDataSampleTime(long lastDataSampleTime) {
        mLastDataSampleTime = lastDataSampleTime;
    }

    /**
     * returns the sample duration used
     *
     * @return duration in milliseconds or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getDataSampleDuration() {
        return mDataSampleDuration;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDataSampleDuration(int dataSampleDuration) {
        mDataSampleDuration = dataSampleDuration;
    }
}

