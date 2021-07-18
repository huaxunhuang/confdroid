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
 * Class that represents useful attributes of mobile network links
 *  such as the upload/download throughput or error rate etc.
 *
 * @unknown 
 */
public class MobileLinkQualityInfo extends android.net.LinkQualityInfo {
    // Represents TelephonyManager.NetworkType
    private int mMobileNetworkType = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mRssi = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mGsmErrorRate = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mCdmaDbm = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mCdmaEcio = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mEvdoDbm = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mEvdoEcio = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mEvdoSnr = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mLteSignalStrength = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mLteRsrp = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mLteRsrq = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mLteRssnr = android.net.LinkQualityInfo.UNKNOWN_INT;

    private int mLteCqi = android.net.LinkQualityInfo.UNKNOWN_INT;

    /**
     * Implement the Parcelable interface.
     *
     * @unknown 
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags, android.net.LinkQualityInfo.OBJECT_TYPE_MOBILE_LINK_QUALITY_INFO);
        dest.writeInt(mMobileNetworkType);
        dest.writeInt(mRssi);
        dest.writeInt(mGsmErrorRate);
        dest.writeInt(mCdmaDbm);
        dest.writeInt(mCdmaEcio);
        dest.writeInt(mEvdoDbm);
        dest.writeInt(mEvdoEcio);
        dest.writeInt(mEvdoSnr);
        dest.writeInt(mLteSignalStrength);
        dest.writeInt(mLteRsrp);
        dest.writeInt(mLteRsrq);
        dest.writeInt(mLteRssnr);
        dest.writeInt(mLteCqi);
    }

    /* Un-parceling helper */
    /**
     *
     *
     * @unknown 
     */
    public static android.net.MobileLinkQualityInfo createFromParcelBody(android.os.Parcel in) {
        android.net.MobileLinkQualityInfo li = new android.net.MobileLinkQualityInfo();
        li.initializeFromParcel(in);
        li.mMobileNetworkType = in.readInt();
        li.mRssi = in.readInt();
        li.mGsmErrorRate = in.readInt();
        li.mCdmaDbm = in.readInt();
        li.mCdmaEcio = in.readInt();
        li.mEvdoDbm = in.readInt();
        li.mEvdoEcio = in.readInt();
        li.mEvdoSnr = in.readInt();
        li.mLteSignalStrength = in.readInt();
        li.mLteRsrp = in.readInt();
        li.mLteRsrq = in.readInt();
        li.mLteRssnr = in.readInt();
        li.mLteCqi = in.readInt();
        return li;
    }

    /**
     * returns mobile network type as defined by {@link android.telephony.TelephonyManager}
     *
     * @return network type or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getMobileNetworkType() {
        return mMobileNetworkType;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setMobileNetworkType(int mobileNetworkType) {
        mMobileNetworkType = mobileNetworkType;
    }

    /**
     * returns signal strength for GSM networks
     *
     * @return signal strength in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getRssi() {
        return mRssi;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setRssi(int Rssi) {
        mRssi = Rssi;
    }

    /**
     * returns error rates for GSM networks
     *
     * @return error rate or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getGsmErrorRate() {
        return mGsmErrorRate;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setGsmErrorRate(int gsmErrorRate) {
        mGsmErrorRate = gsmErrorRate;
    }

    /**
     * returns signal strength for CDMA networks
     *
     * @return signal strength in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getCdmaDbm() {
        return mCdmaDbm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaDbm(int cdmaDbm) {
        mCdmaDbm = cdmaDbm;
    }

    /**
     * returns signal to noise ratio for CDMA networks
     *
     * @return signal to noise ratio in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getCdmaEcio() {
        return mCdmaEcio;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setCdmaEcio(int cdmaEcio) {
        mCdmaEcio = cdmaEcio;
    }

    /**
     * returns signal strength for EVDO networks
     *
     * @return signal strength in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getEvdoDbm() {
        return mEvdoDbm;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoDbm(int evdoDbm) {
        mEvdoDbm = evdoDbm;
    }

    /**
     * returns signal to noise ratio for EVDO spectrum
     *
     * @return signal to noise ration in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getEvdoEcio() {
        return mEvdoEcio;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoEcio(int evdoEcio) {
        mEvdoEcio = evdoEcio;
    }

    /**
     * returns end-to-end signal to noise ratio for EVDO networks
     *
     * @return signal to noise ration in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getEvdoSnr() {
        return mEvdoSnr;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setEvdoSnr(int evdoSnr) {
        mEvdoSnr = evdoSnr;
    }

    /**
     * returns signal strength for LTE network
     *
     * @return signal strength in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getLteSignalStrength() {
        return mLteSignalStrength;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLteSignalStrength(int lteSignalStrength) {
        mLteSignalStrength = lteSignalStrength;
    }

    /**
     * returns RSRP (Reference Signal Received Power) for LTE network
     *
     * @return RSRP in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getLteRsrp() {
        return mLteRsrp;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLteRsrp(int lteRsrp) {
        mLteRsrp = lteRsrp;
    }

    /**
     * returns RSRQ (Reference Signal Received Quality) for LTE network
     *
     * @return RSRQ ??? or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getLteRsrq() {
        return mLteRsrq;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLteRsrq(int lteRsrq) {
        mLteRsrq = lteRsrq;
    }

    /**
     * returns signal to noise ratio for LTE networks
     *
     * @return signal to noise ration in db or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getLteRssnr() {
        return mLteRssnr;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLteRssnr(int lteRssnr) {
        mLteRssnr = lteRssnr;
    }

    /**
     * returns channel quality indicator for LTE networks
     *
     * @return CQI or {@link android.net.LinkQualityInfo#UNKNOWN_INT}
     */
    public int getLteCqi() {
        return mLteCqi;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setLteCqi(int lteCqi) {
        mLteCqi = lteCqi;
    }
}

