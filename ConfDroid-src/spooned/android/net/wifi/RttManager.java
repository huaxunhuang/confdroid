package android.net.wifi;


/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class RttManager {
    private static final boolean DBG = false;

    private static final java.lang.String TAG = "RttManager";

    /**
     *
     *
     * @deprecated It is Not supported anymore.
     */
    @java.lang.Deprecated
    public static final int RTT_TYPE_UNSPECIFIED = 0;

    public static final int RTT_TYPE_ONE_SIDED = 1;

    public static final int RTT_TYPE_TWO_SIDED = 2;

    /**
     *
     *
     * @deprecated It is not supported anymore.
     */
    @java.lang.Deprecated
    public static final int RTT_TYPE_11_V = 2;

    /**
     *
     *
     * @deprecated It is not supported anymore.
     */
    @java.lang.Deprecated
    public static final int RTT_TYPE_11_MC = 4;

    /**
     *
     *
     * @deprecated It is not supported anymore.
     */
    @java.lang.Deprecated
    public static final int RTT_PEER_TYPE_UNSPECIFIED = 0;

    public static final int RTT_PEER_TYPE_AP = 1;

    public static final int RTT_PEER_TYPE_STA = 2;/* requires NAN */


    public static final int RTT_PEER_P2P_GO = 3;

    public static final int RTT_PEER_P2P_CLIENT = 4;

    public static final int RTT_PEER_NAN = 5;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_20_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_20 = 0;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_40_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_40 = 1;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_80_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_80 = 2;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_160_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_160 = 3;

    /**
     *
     *
     * @deprecated not supported anymore
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_80P80 = 4;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_5_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_5 = 5;

    /**
     *
     *
     * @deprecated It is not supported anymore.
    Use {@link android.net.wifi.RttManager#RTT_BW_10_SUPPORT} API.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_10 = 6;

    /**
     *
     *
     * @deprecated channel info must be specified.
     */
    @java.lang.Deprecated
    public static final int RTT_CHANNEL_WIDTH_UNSPECIFIED = -1;

    public static final int RTT_STATUS_SUCCESS = 0;

    /**
     * General failure
     */
    public static final int RTT_STATUS_FAILURE = 1;

    /**
     * Destination does not respond to RTT request
     */
    public static final int RTT_STATUS_FAIL_NO_RSP = 2;

    /**
     * RTT request is rejected by the destination. Double side RTT only
     */
    public static final int RTT_STATUS_FAIL_REJECTED = 3;

    /**
     *
     */
    public static final int RTT_STATUS_FAIL_NOT_SCHEDULED_YET = 4;

    /**
     * Timing measurement timeout
     */
    public static final int RTT_STATUS_FAIL_TM_TIMEOUT = 5;

    /**
     * Destination is on a different channel from the RTT Request
     */
    public static final int RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL = 6;

    /**
     * This type of Ranging is not support by Hardware
     */
    public static final int RTT_STATUS_FAIL_NO_CAPABILITY = 7;

    /**
     * Request abort fro uncertain reason
     */
    public static final int RTT_STATUS_ABORTED = 8;

    /**
     * The T1-T4 or TOD/TOA Timestamp is illegal
     */
    public static final int RTT_STATUS_FAIL_INVALID_TS = 9;

    /**
     * 11mc protocol level failed, eg, unrecognized FTMR/FTM frame
     */
    public static final int RTT_STATUS_FAIL_PROTOCOL = 10;

    /**
     * Request can not be scheduled by hardware
     */
    public static final int RTT_STATUS_FAIL_SCHEDULE = 11;

    /**
     * destination is busy now, you can try after a specified time from destination
     */
    public static final int RTT_STATUS_FAIL_BUSY_TRY_LATER = 12;

    /**
     * Bad Request argument
     */
    public static final int RTT_STATUS_INVALID_REQ = 13;

    /**
     * Wifi is not enabled
     */
    public static final int RTT_STATUS_NO_WIFI = 14;

    /**
     * Responder overrides param info, cannot range with new params 2-side RTT only
     */
    public static final int RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE = 15;

    public static final int REASON_UNSPECIFIED = -1;

    public static final int REASON_NOT_AVAILABLE = -2;

    public static final int REASON_INVALID_LISTENER = -3;

    public static final int REASON_INVALID_REQUEST = -4;

    /**
     * Do not have required permission
     */
    public static final int REASON_PERMISSION_DENIED = -5;

    /**
     * Ranging failed because responder role is enabled in STA mode.
     */
    public static final int REASON_INITIATOR_NOT_ALLOWED_WHEN_RESPONDER_ON = -6;

    public static final java.lang.String DESCRIPTION_KEY = "android.net.wifi.RttManager.Description";

    /**
     * RTT BW supported bit mask, used as RTT param bandWidth too
     */
    public static final int RTT_BW_5_SUPPORT = 0x1;

    public static final int RTT_BW_10_SUPPORT = 0x2;

    public static final int RTT_BW_20_SUPPORT = 0x4;

    public static final int RTT_BW_40_SUPPORT = 0x8;

    public static final int RTT_BW_80_SUPPORT = 0x10;

    public static final int RTT_BW_160_SUPPORT = 0x20;

    /**
     * RTT Preamble Support bit mask
     */
    public static final int PREAMBLE_LEGACY = 0x1;

    public static final int PREAMBLE_HT = 0x2;

    public static final int PREAMBLE_VHT = 0x4;

    /**
     *
     *
     * @deprecated Use the new {@link android.net.wifi.RttManager.RttCapabilities} API
     */
    @java.lang.Deprecated
    public class Capabilities {
        public int supportedType;

        public int supportedPeerType;
    }

    /**
     *
     *
     * @deprecated Use the new {@link android.net.wifi.RttManager#getRttCapabilities()} API.
     */
    @java.lang.Deprecated
    public android.net.wifi.RttManager.Capabilities getCapabilities() {
        return new android.net.wifi.RttManager.Capabilities();
    }

    /**
     * This class describe the RTT capability of the Hardware
     */
    public static class RttCapabilities implements android.os.Parcelable {
        /**
         *
         *
         * @deprecated It is not supported
         */
        @java.lang.Deprecated
        public boolean supportedType;

        /**
         *
         *
         * @deprecated It is not supported
         */
        @java.lang.Deprecated
        public boolean supportedPeerType;

        // 1-sided rtt measurement is supported
        public boolean oneSidedRttSupported;

        // 11mc 2-sided rtt measurement is supported
        public boolean twoSided11McRttSupported;

        // location configuration information supported
        public boolean lciSupported;

        // location civic records supported
        public boolean lcrSupported;

        // preamble supported, see bit mask definition above
        public int preambleSupported;

        // RTT bandwidth supported
        public int bwSupported;

        // Whether STA responder role is supported.
        public boolean responderSupported;

        /**
         * Whether the secure RTT protocol is supported.
         */
        public boolean secureRttSupported;

        /**
         * Draft 11mc version supported, including major and minor version. e.g, draft 4.3 is 43
         */
        public int mcVersion;

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuffer sb = new java.lang.StringBuffer();
            sb.append("oneSidedRtt ").append(oneSidedRttSupported ? "is Supported. " : "is not supported. ").append("twoSided11McRtt ").append(twoSided11McRttSupported ? "is Supported. " : "is not supported. ").append("lci ").append(lciSupported ? "is Supported. " : "is not supported. ").append("lcr ").append(lcrSupported ? "is Supported. " : "is not supported. ");
            if ((preambleSupported & android.net.wifi.RttManager.PREAMBLE_LEGACY) != 0) {
                sb.append("Legacy ");
            }
            if ((preambleSupported & android.net.wifi.RttManager.PREAMBLE_HT) != 0) {
                sb.append("HT ");
            }
            if ((preambleSupported & android.net.wifi.RttManager.PREAMBLE_VHT) != 0) {
                sb.append("VHT ");
            }
            sb.append("is supported. ");
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_5_SUPPORT) != 0) {
                sb.append("5 MHz ");
            }
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_10_SUPPORT) != 0) {
                sb.append("10 MHz ");
            }
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_20_SUPPORT) != 0) {
                sb.append("20 MHz ");
            }
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_40_SUPPORT) != 0) {
                sb.append("40 MHz ");
            }
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_80_SUPPORT) != 0) {
                sb.append("80 MHz ");
            }
            if ((bwSupported & android.net.wifi.RttManager.RTT_BW_160_SUPPORT) != 0) {
                sb.append("160 MHz ");
            }
            sb.append("is supported.");
            sb.append(" STA responder role is ").append(responderSupported ? "supported" : "not supported");
            sb.append(" Secure RTT protocol is ").append(secureRttSupported ? "supported" : "not supported");
            sb.append(" 11mc version is " + mcVersion);
            return sb.toString();
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(oneSidedRttSupported ? 1 : 0);
            dest.writeInt(twoSided11McRttSupported ? 1 : 0);
            dest.writeInt(lciSupported ? 1 : 0);
            dest.writeInt(lcrSupported ? 1 : 0);
            dest.writeInt(preambleSupported);
            dest.writeInt(bwSupported);
            dest.writeInt(responderSupported ? 1 : 0);
            dest.writeInt(secureRttSupported ? 1 : 0);
            dest.writeInt(mcVersion);
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        public static final android.os.Parcelable.Creator<android.net.wifi.RttManager.RttCapabilities> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.RttManager.RttCapabilities>() {
            @java.lang.Override
            public android.net.wifi.RttManager.RttCapabilities createFromParcel(android.os.Parcel in) {
                android.net.wifi.RttManager.RttCapabilities capabilities = new android.net.wifi.RttManager.RttCapabilities();
                capabilities.oneSidedRttSupported = in.readInt() == 1;
                capabilities.twoSided11McRttSupported = in.readInt() == 1;
                capabilities.lciSupported = in.readInt() == 1;
                capabilities.lcrSupported = in.readInt() == 1;
                capabilities.preambleSupported = in.readInt();
                capabilities.bwSupported = in.readInt();
                capabilities.responderSupported = in.readInt() == 1;
                capabilities.secureRttSupported = in.readInt() == 1;
                capabilities.mcVersion = in.readInt();
                return capabilities;
            }

            /**
             * Implement the Parcelable interface {@hide }
             */
            @java.lang.Override
            public android.net.wifi.RttManager.RttCapabilities[] newArray(int size) {
                return new android.net.wifi.RttManager.RttCapabilities[size];
            }
        };
    }

    public android.net.wifi.RttManager.RttCapabilities getRttCapabilities() {
        synchronized(mCapabilitiesLock) {
            if (mRttCapabilities == null) {
                try {
                    mRttCapabilities = mService.getRttCapabilities();
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return mRttCapabilities;
        }
    }

    /**
     * specifies parameters for RTT request
     */
    public static class RttParams {
        /**
         * type of destination device being ranged
         * currently only support RTT_PEER_TYPE_AP
         * Range:RTT_PEER_TYPE_xxxx Default value:RTT_PEER_TYPE_AP
         */
        public int deviceType;

        /**
         * type of RTT measurement method. Need check scan result and RttCapabilities first
         * Range: RTT_TYPE_ONE_SIDED or RTT_TYPE_TWO_SIDED
         * Default value: RTT_TYPE_ONE_SIDED
         */
        public int requestType;

        /**
         * Whether the secure RTT protocol needs to be used for ranging this peer device.
         */
        public boolean secure;

        /**
         * mac address of the device being ranged
         * Default value: null
         */
        public java.lang.String bssid;

        /**
         * The primary control channel over which the client is
         * communicating with the AP.Same as ScanResult.frequency
         * Default value: 0
         */
        public int frequency;

        /**
         * channel width of the destination AP. Same as ScanResult.channelWidth
         * Default value: 0
         */
        public int channelWidth;

        /**
         * Not used if the AP bandwidth is 20 MHz
         * If the AP use 40, 80 or 160 MHz, this is the center frequency
         * if the AP use 80 + 80 MHz, this is the center frequency of the first segment
         * same as ScanResult.centerFreq0
         * Default value: 0
         */
        public int centerFreq0;

        /**
         * Only used if the AP bandwidth is 80 + 80 MHz
         * if the AP use 80 + 80 MHz, this is the center frequency of the second segment
         * same as ScanResult.centerFreq1
         * Default value: 0
         */
        public int centerFreq1;

        /**
         * number of samples to be taken
         *
         * @deprecated Use the new {@link android.net.wifi.RttManager.RttParams#numSamplesPerBurst}
         */
        @java.lang.Deprecated
        public int num_samples;

        /**
         * number of retries if a sample fails
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttParams#numRetriesPerMeasurementFrame} API.
         */
        @java.lang.Deprecated
        public int num_retries;

        /**
         * Number of burst in exp , 2^x. 0 means single shot measurement, range 0-15
         * Currently only single shot is supported
         * Default value: 0
         */
        public int numberBurst;

        /**
         * valid only if numberBurst > 1, interval between burst(100ms).
         * Range : 0-31, 0--means no specific
         * Default value: 0
         */
        public int interval;

        /**
         * number of samples to be taken in one burst
         * Range: 1-31
         * Default value: 8
         */
        public int numSamplesPerBurst;

        /**
         * number of retries for each measurement frame if a sample fails
         *  Only used by single side RTT,
         *  Range 0 - 3 Default value: 0
         */
        public int numRetriesPerMeasurementFrame;

        /**
         * number of retries for FTMR frame (control frame) if it fails.
         * Only used by 80211MC double side RTT
         * Range: 0-3  Default Value : 0
         */
        public int numRetriesPerFTMR;

        /**
         * Request LCI information, only available when choose double side RTT measurement
         * need check RttCapabilties first.
         * Default value: false
         */
        public boolean LCIRequest;

        /**
         * Request LCR information, only available when choose double side RTT measurement
         * need check RttCapabilties first.
         * Default value: false
         */
        public boolean LCRRequest;

        /**
         * Timeout for each burst, (250 * 2^x) us,
         * Range 1-11 and 15. 15 means no control Default value: 15
         */
        public int burstTimeout;

        /**
         * preamble used for RTT measurement
         *  Range: PREAMBLE_LEGACY, PREAMBLE_HT, PREAMBLE_VHT
         *  Default value: PREAMBLE_HT
         */
        public int preamble;

        /**
         * bandWidth used for RTT measurement.User need verify the highest BW the destination
         * support (from scan result etc) before set this value. Wider channels result usually give
         * better accuracy. However, the frame loss can increase too.
         * should be one of RTT_BW_5_SUPPORT to RTT_BW_160_SUPPORT. However, need check
         * RttCapabilities firstto verify HW support this bandwidth.
         * Default value:RTT_BW_20_SUPPORT
         */
        public int bandwidth;

        public RttParams() {
            // provide initial value for RttParams
            deviceType = android.net.wifi.RttManager.RTT_PEER_TYPE_AP;
            requestType = android.net.wifi.RttManager.RTT_TYPE_ONE_SIDED;
            numberBurst = 0;
            numSamplesPerBurst = 8;
            numRetriesPerMeasurementFrame = 0;
            numRetriesPerFTMR = 0;
            burstTimeout = 15;
            preamble = android.net.wifi.RttManager.PREAMBLE_HT;
            bandwidth = android.net.wifi.RttManager.RTT_BW_20_SUPPORT;
        }
    }

    /**
     * pseudo-private class used to parcel arguments
     */
    public static class ParcelableRttParams implements android.os.Parcelable {
        @android.annotation.NonNull
        public android.net.wifi.RttManager.RttParams[] mParams;

        /**
         *
         *
         * @unknown 
         */
        @com.android.internal.annotations.VisibleForTesting
        public ParcelableRttParams(android.net.wifi.RttManager.RttParams[] params) {
            mParams = (params == null) ? new android.net.wifi.RttManager.RttParams[0] : params;
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mParams.length);
            for (android.net.wifi.RttManager.RttParams params : mParams) {
                dest.writeInt(params.deviceType);
                dest.writeInt(params.requestType);
                dest.writeByte(params.secure ? ((byte) (1)) : 0);
                dest.writeString(params.bssid);
                dest.writeInt(params.channelWidth);
                dest.writeInt(params.frequency);
                dest.writeInt(params.centerFreq0);
                dest.writeInt(params.centerFreq1);
                dest.writeInt(params.numberBurst);
                dest.writeInt(params.interval);
                dest.writeInt(params.numSamplesPerBurst);
                dest.writeInt(params.numRetriesPerMeasurementFrame);
                dest.writeInt(params.numRetriesPerFTMR);
                dest.writeInt(params.LCIRequest ? 1 : 0);
                dest.writeInt(params.LCRRequest ? 1 : 0);
                dest.writeInt(params.burstTimeout);
                dest.writeInt(params.preamble);
                dest.writeInt(params.bandwidth);
            }
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        public static final android.os.Parcelable.Creator<android.net.wifi.RttManager.ParcelableRttParams> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.RttManager.ParcelableRttParams>() {
            @java.lang.Override
            public android.net.wifi.RttManager.ParcelableRttParams createFromParcel(android.os.Parcel in) {
                int num = in.readInt();
                android.net.wifi.RttManager.RttParams[] params = new android.net.wifi.RttManager.RttParams[num];
                for (int i = 0; i < num; i++) {
                    params[i] = new android.net.wifi.RttManager.RttParams();
                    params[i].deviceType = in.readInt();
                    params[i].requestType = in.readInt();
                    params[i].secure = in.readByte() != 0;
                    params[i].bssid = in.readString();
                    params[i].channelWidth = in.readInt();
                    params[i].frequency = in.readInt();
                    params[i].centerFreq0 = in.readInt();
                    params[i].centerFreq1 = in.readInt();
                    params[i].numberBurst = in.readInt();
                    params[i].interval = in.readInt();
                    params[i].numSamplesPerBurst = in.readInt();
                    params[i].numRetriesPerMeasurementFrame = in.readInt();
                    params[i].numRetriesPerFTMR = in.readInt();
                    params[i].LCIRequest = in.readInt() == 1;
                    params[i].LCRRequest = in.readInt() == 1;
                    params[i].burstTimeout = in.readInt();
                    params[i].preamble = in.readInt();
                    params[i].bandwidth = in.readInt();
                }
                android.net.wifi.RttManager.ParcelableRttParams parcelableParams = new android.net.wifi.RttManager.ParcelableRttParams(params);
                return parcelableParams;
            }

            @java.lang.Override
            public android.net.wifi.RttManager.ParcelableRttParams[] newArray(int size) {
                return new android.net.wifi.RttManager.ParcelableRttParams[size];
            }
        };
    }

    public static class WifiInformationElement {
        /**
         * Information Element ID 0xFF means element is invalid.
         */
        public byte id;

        public byte[] data;
    }

    /**
     * specifies RTT results
     */
    public static class RttResult {
        /**
         * mac address of the device being ranged.
         */
        public java.lang.String bssid;

        /**
         * # of burst for this measurement.
         */
        public int burstNumber;

        /**
         * total number of measurement frames attempted in this measurement.
         */
        public int measurementFrameNumber;

        /**
         * total successful number of measurement frames in this measurement.
         */
        public int successMeasurementFrameNumber;

        /**
         * Maximum number of frames per burst supported by peer. Two side RTT only
         * Valid only if less than request
         */
        public int frameNumberPerBurstPeer;

        /**
         * status of the request
         */
        public int status;

        /**
         * type of the request used
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#measurementType}
         */
        @java.lang.Deprecated
        public int requestType;

        /**
         * RTT measurement method type used, should be one of RTT_TYPE_ONE_SIDED or
         *  RTT_TYPE_TWO_SIDED.
         */
        public int measurementType;

        /**
         * only valid when status ==  RTT_STATUS_FAIL_BUSY_TRY_LATER
         * please retry RTT measurement after this duration since peer indicate busy at ths moment
         *  Unit S  Range:1-31
         */
        public int retryAfterDuration;

        /**
         * timestamp of completion, in microsecond since boot.
         */
        public long ts;

        /**
         * average RSSI observed, unit of 0.5 dB.
         */
        public int rssi;

        /**
         * RSSI spread (i.e. max - min)
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#rssiSpread} API.
         */
        @java.lang.Deprecated
        public int rssi_spread;

        /**
         * RSSI spread (i.e. max - min), unit of 0.5 dB.
         */
        public int rssiSpread;

        /**
         * average transmit rate
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#txRate} API.
         */
        @java.lang.Deprecated
        public int tx_rate;

        /**
         * average transmit rate. Unit (100kbps).
         */
        public int txRate;

        /**
         * average receiving rate Unit (100kbps).
         */
        public int rxRate;

        /**
         * average round trip time in nano second
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#rtt} API.
         */
        @java.lang.Deprecated
        public long rtt_ns;

        /**
         * average round trip time in 0.1 nano second.
         */
        public long rtt;

        /**
         * standard deviation observed in round trip time
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#rttStandardDeviation} API.
         */
        @java.lang.Deprecated
        public long rtt_sd_ns;

        /**
         * standard deviation of RTT in 0.1 ns.
         */
        public long rttStandardDeviation;

        /**
         * spread (i.e. max - min) round trip time
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#rttSpread} API.
         */
        @java.lang.Deprecated
        public long rtt_spread_ns;

        /**
         * spread (i.e. max - min) RTT in 0.1 ns.
         */
        public long rttSpread;

        /**
         * average distance in centimeter, computed based on rtt_ns
         *
         * @deprecated use {@link android.net.wifi.RttManager.RttResult#distance} API.
         */
        @java.lang.Deprecated
        public int distance_cm;

        /**
         * average distance in cm, computed based on rtt.
         */
        public int distance;

        /**
         * standard deviation observed in distance
         *
         * @deprecated Use {@link .android.net.wifi.RttManager.RttResult#distanceStandardDeviation} API.
         */
        @java.lang.Deprecated
        public int distance_sd_cm;

        /**
         * standard deviation observed in distance in cm.
         */
        public int distanceStandardDeviation;

        /**
         * spread (i.e. max - min) distance
         *
         * @deprecated Use {@link android.net.wifi.RttManager.RttResult#distanceSpread} API.
         */
        @java.lang.Deprecated
        public int distance_spread_cm;

        /**
         * spread (i.e. max - min) distance in cm.
         */
        public int distanceSpread;

        /**
         * the duration of this measurement burst, unit ms.
         */
        public int burstDuration;

        /**
         * Burst number supported by peer after negotiation, 2side RTT only
         */
        public int negotiatedBurstNum;

        /**
         * LCI information Element, only available for double side RTT.
         */
        public android.net.wifi.RttManager.WifiInformationElement LCI;

        /**
         * LCR information Element, only available to double side RTT.
         */
        public android.net.wifi.RttManager.WifiInformationElement LCR;

        /**
         * Whether the secure RTT protocol was used for ranging.
         */
        public boolean secure;
    }

    /**
     * pseudo-private class used to parcel results.
     */
    public static class ParcelableRttResults implements android.os.Parcelable {
        public android.net.wifi.RttManager.RttResult[] mResults;

        public ParcelableRttResults(android.net.wifi.RttManager.RttResult[] results) {
            mResults = results;
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            if (mResults != null) {
                dest.writeInt(mResults.length);
                for (android.net.wifi.RttManager.RttResult result : mResults) {
                    dest.writeString(result.bssid);
                    dest.writeInt(result.burstNumber);
                    dest.writeInt(result.measurementFrameNumber);
                    dest.writeInt(result.successMeasurementFrameNumber);
                    dest.writeInt(result.frameNumberPerBurstPeer);
                    dest.writeInt(result.status);
                    dest.writeInt(result.measurementType);
                    dest.writeInt(result.retryAfterDuration);
                    dest.writeLong(result.ts);
                    dest.writeInt(result.rssi);
                    dest.writeInt(result.rssiSpread);
                    dest.writeInt(result.txRate);
                    dest.writeLong(result.rtt);
                    dest.writeLong(result.rttStandardDeviation);
                    dest.writeLong(result.rttSpread);
                    dest.writeInt(result.distance);
                    dest.writeInt(result.distanceStandardDeviation);
                    dest.writeInt(result.distanceSpread);
                    dest.writeInt(result.burstDuration);
                    dest.writeInt(result.negotiatedBurstNum);
                    dest.writeByte(result.LCI.id);
                    if (result.LCI.id != ((byte) (0xff))) {
                        dest.writeByte(((byte) (result.LCI.data.length)));
                        dest.writeByteArray(result.LCI.data);
                    }
                    dest.writeByte(result.LCR.id);
                    if (result.LCR.id != ((byte) (0xff))) {
                        dest.writeInt(((byte) (result.LCR.data.length)));
                        dest.writeByte(result.LCR.id);
                    }
                    dest.writeByte(result.secure ? ((byte) (1)) : 0);
                }
            } else {
                dest.writeInt(0);
            }
        }

        /**
         * Implement the Parcelable interface {@hide }
         */
        public static final android.os.Parcelable.Creator<android.net.wifi.RttManager.ParcelableRttResults> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.RttManager.ParcelableRttResults>() {
            @java.lang.Override
            public android.net.wifi.RttManager.ParcelableRttResults createFromParcel(android.os.Parcel in) {
                int num = in.readInt();
                if (num == 0) {
                    return new android.net.wifi.RttManager.ParcelableRttResults(null);
                }
                android.net.wifi.RttManager.RttResult[] results = new android.net.wifi.RttManager.RttResult[num];
                for (int i = 0; i < num; i++) {
                    results[i] = new android.net.wifi.RttManager.RttResult();
                    results[i].bssid = in.readString();
                    results[i].burstNumber = in.readInt();
                    results[i].measurementFrameNumber = in.readInt();
                    results[i].successMeasurementFrameNumber = in.readInt();
                    results[i].frameNumberPerBurstPeer = in.readInt();
                    results[i].status = in.readInt();
                    results[i].measurementType = in.readInt();
                    results[i].retryAfterDuration = in.readInt();
                    results[i].ts = in.readLong();
                    results[i].rssi = in.readInt();
                    results[i].rssiSpread = in.readInt();
                    results[i].txRate = in.readInt();
                    results[i].rtt = in.readLong();
                    results[i].rttStandardDeviation = in.readLong();
                    results[i].rttSpread = in.readLong();
                    results[i].distance = in.readInt();
                    results[i].distanceStandardDeviation = in.readInt();
                    results[i].distanceSpread = in.readInt();
                    results[i].burstDuration = in.readInt();
                    results[i].negotiatedBurstNum = in.readInt();
                    results[i].LCI = new android.net.wifi.RttManager.WifiInformationElement();
                    results[i].LCI.id = in.readByte();
                    if (results[i].LCI.id != ((byte) (0xff))) {
                        byte length = in.readByte();
                        results[i].LCI.data = new byte[length];
                        in.readByteArray(results[i].LCI.data);
                    }
                    results[i].LCR = new android.net.wifi.RttManager.WifiInformationElement();
                    results[i].LCR.id = in.readByte();
                    if (results[i].LCR.id != ((byte) (0xff))) {
                        byte length = in.readByte();
                        results[i].LCR.data = new byte[length];
                        in.readByteArray(results[i].LCR.data);
                    }
                    results[i].secure = in.readByte() != 0;
                }
                android.net.wifi.RttManager.ParcelableRttResults parcelableResults = new android.net.wifi.RttManager.ParcelableRttResults(results);
                return parcelableResults;
            }

            @java.lang.Override
            public android.net.wifi.RttManager.ParcelableRttResults[] newArray(int size) {
                return new android.net.wifi.RttManager.ParcelableRttResults[size];
            }
        };
    }

    public static interface RttListener {
        public void onSuccess(android.net.wifi.RttManager.RttResult[] results);

        public void onFailure(int reason, java.lang.String description);

        public void onAborted();
    }

    private boolean rttParamSanity(android.net.wifi.RttManager.RttParams params, int index) {
        if (mRttCapabilities == null) {
            if (getRttCapabilities() == null) {
                android.util.Log.e(android.net.wifi.RttManager.TAG, "Can not get RTT capabilities");
                throw new java.lang.IllegalStateException("RTT chip is not working");
            }
        }
        if (params.deviceType != android.net.wifi.RttManager.RTT_PEER_TYPE_AP) {
            return false;
        } else
            if ((params.requestType != android.net.wifi.RttManager.RTT_TYPE_ONE_SIDED) && (params.requestType != android.net.wifi.RttManager.RTT_TYPE_TWO_SIDED)) {
                android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal Request Type: ") + params.requestType);
                return false;
            } else
                if ((params.requestType == android.net.wifi.RttManager.RTT_TYPE_ONE_SIDED) && (!mRttCapabilities.oneSidedRttSupported)) {
                    android.util.Log.e(android.net.wifi.RttManager.TAG, ("Request " + index) + ": One side RTT is not supported");
                    return false;
                } else
                    if ((params.requestType == android.net.wifi.RttManager.RTT_TYPE_TWO_SIDED) && (!mRttCapabilities.twoSided11McRttSupported)) {
                        android.util.Log.e(android.net.wifi.RttManager.TAG, ("Request " + index) + ": two side RTT is not supported");
                        return false;
                    } else
                        if ((params.bssid == null) || params.bssid.isEmpty()) {
                            android.util.Log.e(android.net.wifi.RttManager.TAG, "No BSSID in params");
                            return false;
                        } else
                            if (params.numberBurst != 0) {
                                android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal number of burst: ") + params.numberBurst);
                                return false;
                            } else
                                if ((params.numSamplesPerBurst <= 0) || (params.numSamplesPerBurst > 31)) {
                                    android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal sample number per burst: ") + params.numSamplesPerBurst);
                                    return false;
                                } else
                                    if ((params.numRetriesPerMeasurementFrame < 0) || (params.numRetriesPerMeasurementFrame > 3)) {
                                        android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal measurement frame retry number:") + params.numRetriesPerMeasurementFrame);
                                        return false;
                                    } else
                                        if ((params.numRetriesPerFTMR < 0) || (params.numRetriesPerFTMR > 3)) {
                                            android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal FTMR frame retry number:") + params.numRetriesPerFTMR);
                                            return false;
                                        } else
                                            if (params.LCIRequest && (!mRttCapabilities.lciSupported)) {
                                                android.util.Log.e(android.net.wifi.RttManager.TAG, ("Request " + index) + ": LCI is not supported");
                                                return false;
                                            } else
                                                if (params.LCRRequest && (!mRttCapabilities.lcrSupported)) {
                                                    android.util.Log.e(android.net.wifi.RttManager.TAG, ("Request " + index) + ": LCR is not supported");
                                                    return false;
                                                } else
                                                    if ((params.burstTimeout < 1) || ((params.burstTimeout > 11) && (params.burstTimeout != 15))) {
                                                        android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Illegal burst timeout: ") + params.burstTimeout);
                                                        return false;
                                                    } else
                                                        if ((params.preamble & mRttCapabilities.preambleSupported) == 0) {
                                                            android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Do not support this preamble: ") + params.preamble);
                                                            return false;
                                                        } else
                                                            if ((params.bandwidth & mRttCapabilities.bwSupported) == 0) {
                                                                android.util.Log.e(android.net.wifi.RttManager.TAG, (("Request " + index) + ": Do not support this bandwidth: ") + params.bandwidth);
                                                                return false;
                                                            }













        return true;
    }

    /**
     * Request to start an RTT ranging
     *
     * @param params
     * 		-- RTT request Parameters
     * @param listener
     * 		-- Call back to inform RTT result
     * @exception throw
     * 		IllegalArgumentException when params are illegal
     * 		throw IllegalStateException when RttCapabilities do not exist
     */
    public void startRanging(android.net.wifi.RttManager.RttParams[] params, android.net.wifi.RttManager.RttListener listener) {
        int index = 0;
        for (android.net.wifi.RttManager.RttParams rttParam : params) {
            if (!rttParamSanity(rttParam, index)) {
                throw new java.lang.IllegalArgumentException("RTT Request Parameter Illegal");
            }
            index++;
        }
        validateChannel();
        android.net.wifi.RttManager.ParcelableRttParams parcelableParams = new android.net.wifi.RttManager.ParcelableRttParams(params);
        android.util.Log.i(android.net.wifi.RttManager.TAG, "Send RTT request to RTT Service");
        mAsyncChannel.sendMessage(android.net.wifi.RttManager.CMD_OP_START_RANGING, 0, putListener(listener), parcelableParams);
    }

    public void stopRanging(android.net.wifi.RttManager.RttListener listener) {
        validateChannel();
        mAsyncChannel.sendMessage(android.net.wifi.RttManager.CMD_OP_STOP_RANGING, 0, removeListener(listener));
    }

    /**
     * Callbacks for responder operations.
     * <p>
     * A {@link ResponderCallback} is the handle to the calling client. {@link RttManager} will keep
     * a reference to the callback for the entire period when responder is enabled. The same
     * callback as used in enabling responder needs to be passed for disabling responder.
     * The client can freely destroy or reuse the callback after {@link RttManager#disableResponder}
     * is called.
     */
    // TODO: consider adding onResponderAborted once it's supported.
    public static abstract class ResponderCallback {
        /**
         * Callback when responder is enabled.
         */
        public abstract void onResponderEnabled(android.net.wifi.RttManager.ResponderConfig config);

        /**
         * Callback when enabling responder failed.
         */
        public abstract void onResponderEnableFailure(int reason);
    }

    /**
     * Enable Wi-Fi RTT responder mode on the device. The enabling result will be delivered via
     * {@code callback}.
     * <p>
     * Note calling this method with the same callback when the responder is already enabled won't
     * change the responder state, a cached {@link ResponderConfig} from the last enabling will be
     * returned through the callback.
     *
     * @param callback
     * 		Callback for responder enabling/disabling result.
     * @throws IllegalArgumentException
     * 		If {@code callback} is null.
     */
    public void enableResponder(android.net.wifi.RttManager.ResponderCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null");
        }
        validateChannel();
        int key = putListenerIfAbsent(callback);
        mAsyncChannel.sendMessage(android.net.wifi.RttManager.CMD_OP_ENABLE_RESPONDER, 0, key);
    }

    /**
     * Disable Wi-Fi RTT responder mode on the device. The {@code callback} needs to be the
     * same one used in {@link #enableResponder(ResponderCallback)}.
     * <p>
     * Calling this method when responder isn't enabled won't have any effect. The callback can be
     * reused for enabling responder after this method is called.
     *
     * @param callback
     * 		The same callback used for enabling responder.
     * @throws IllegalArgumentException
     * 		If {@code callback} is null.
     */
    public void disableResponder(android.net.wifi.RttManager.ResponderCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null");
        }
        validateChannel();
        int key = removeListener(callback);
        if (key == android.net.wifi.RttManager.INVALID_KEY) {
            android.util.Log.e(android.net.wifi.RttManager.TAG, "responder not enabled yet");
            return;
        }
        mAsyncChannel.sendMessage(android.net.wifi.RttManager.CMD_OP_DISABLE_RESPONDER, 0, key);
    }

    /**
     * Configuration used for RTT responder mode. The configuration information can be used by a
     * peer device to range the responder.
     *
     * @see ScanResult
     */
    public static class ResponderConfig implements android.os.Parcelable {
        // TODO: make all fields final once we can get mac address from responder HAL APIs.
        /**
         * Wi-Fi mac address used for responder mode.
         */
        public java.lang.String macAddress = "";

        /**
         * The primary 20 MHz frequency (in MHz) of the channel where responder is enabled.
         *
         * @see ScanResult#frequency
         */
        public int frequency;

        /**
         * Center frequency of the channel where responder is enabled on. Only in use when channel
         * width is at least 40MHz.
         *
         * @see ScanResult#centerFreq0
         */
        public int centerFreq0;

        /**
         * Center frequency of the second segment when channel width is 80 + 80 MHz.
         *
         * @see ScanResult#centerFreq1
         */
        public int centerFreq1;

        /**
         * Width of the channel where responder is enabled on.
         *
         * @see ScanResult#channelWidth
         */
        public int channelWidth;

        /**
         * Preamble supported by responder.
         */
        public int preamble;

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("macAddress = ").append(macAddress).append(" frequency = ").append(frequency).append(" centerFreq0 = ").append(centerFreq0).append(" centerFreq1 = ").append(centerFreq1).append(" channelWidth = ").append(channelWidth).append(" preamble = ").append(preamble);
            return builder.toString();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(macAddress);
            dest.writeInt(frequency);
            dest.writeInt(centerFreq0);
            dest.writeInt(centerFreq1);
            dest.writeInt(channelWidth);
            dest.writeInt(preamble);
        }

        /**
         * Implement {@link Parcelable} interface
         */
        public static final android.os.Parcelable.Creator<android.net.wifi.RttManager.ResponderConfig> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.RttManager.ResponderConfig>() {
            @java.lang.Override
            public android.net.wifi.RttManager.ResponderConfig createFromParcel(android.os.Parcel in) {
                android.net.wifi.RttManager.ResponderConfig config = new android.net.wifi.RttManager.ResponderConfig();
                config.macAddress = in.readString();
                config.frequency = in.readInt();
                config.centerFreq0 = in.readInt();
                config.centerFreq1 = in.readInt();
                config.channelWidth = in.readInt();
                config.preamble = in.readInt();
                return config;
            }

            @java.lang.Override
            public android.net.wifi.RttManager.ResponderConfig[] newArray(int size) {
                return new android.net.wifi.RttManager.ResponderConfig[size];
            }
        };
    }

    /* private methods */
    public static final int BASE = com.android.internal.util.Protocol.BASE_WIFI_RTT_MANAGER;

    public static final int CMD_OP_START_RANGING = android.net.wifi.RttManager.BASE + 0;

    public static final int CMD_OP_STOP_RANGING = android.net.wifi.RttManager.BASE + 1;

    public static final int CMD_OP_FAILED = android.net.wifi.RttManager.BASE + 2;

    public static final int CMD_OP_SUCCEEDED = android.net.wifi.RttManager.BASE + 3;

    public static final int CMD_OP_ABORTED = android.net.wifi.RttManager.BASE + 4;

    public static final int CMD_OP_ENABLE_RESPONDER = android.net.wifi.RttManager.BASE + 5;

    public static final int CMD_OP_DISABLE_RESPONDER = android.net.wifi.RttManager.BASE + 6;

    public static final int CMD_OP_ENALBE_RESPONDER_SUCCEEDED = android.net.wifi.RttManager.BASE + 7;

    public static final int CMD_OP_ENALBE_RESPONDER_FAILED = android.net.wifi.RttManager.BASE + 8;

    private static final int INVALID_KEY = 0;

    private final android.content.Context mContext;

    private final android.net.wifi.IRttManager mService;

    private final android.util.SparseArray mListenerMap = new android.util.SparseArray();

    private final java.lang.Object mListenerMapLock = new java.lang.Object();

    private final java.lang.Object mCapabilitiesLock = new java.lang.Object();

    private android.net.wifi.RttManager.RttCapabilities mRttCapabilities;

    private int mListenerKey = 1;

    private com.android.internal.util.AsyncChannel mAsyncChannel;

    /**
     * Create a new WifiScanner instance.
     * Applications will almost always want to use
     * {@link android.content.Context#getSystemService Context.getSystemService()} to retrieve
     * the standard {@link android.content.Context#WIFI_RTT_SERVICE Context.WIFI_RTT_SERVICE}.
     *
     * @param context
     * 		the application context
     * @param service
     * 		the Binder interface
     * @param looper
     * 		Looper for running the callbacks.
     * @unknown 
     */
    public RttManager(android.content.Context context, android.net.wifi.IRttManager service, android.os.Looper looper) {
        mContext = context;
        mService = service;
        android.os.Messenger messenger = null;
        try {
            android.util.Log.d(android.net.wifi.RttManager.TAG, "Get the messenger from " + mService);
            messenger = mService.getMessenger();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        if (messenger == null) {
            throw new java.lang.IllegalStateException("getMessenger() returned null!  This is invalid.");
        }
        mAsyncChannel = new com.android.internal.util.AsyncChannel();
        android.os.Handler handler = new android.net.wifi.RttManager.ServiceHandler(looper);
        mAsyncChannel.connectSync(mContext, handler, messenger);
        // We cannot use fullyConnectSync because it sends the FULL_CONNECTION message
        // synchronously, which causes RttService to receive the wrong replyTo value.
        mAsyncChannel.sendMessage(AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
    }

    private void validateChannel() {
        if (mAsyncChannel == null)
            throw new java.lang.IllegalStateException("No permission to access and change wifi or a bad initialization");

    }

    private int putListener(java.lang.Object listener) {
        if (listener == null)
            return android.net.wifi.RttManager.INVALID_KEY;

        int key;
        synchronized(mListenerMapLock) {
            do {
                key = mListenerKey++;
            } while (key == android.net.wifi.RttManager.INVALID_KEY );
            mListenerMap.put(key, listener);
        }
        return key;
    }

    // Insert a listener if it doesn't exist in mListenerMap. Returns the key of the listener.
    private int putListenerIfAbsent(java.lang.Object listener) {
        if (listener == null)
            return android.net.wifi.RttManager.INVALID_KEY;

        synchronized(mListenerMapLock) {
            int key = getListenerKey(listener);
            if (key != android.net.wifi.RttManager.INVALID_KEY) {
                return key;
            }
            do {
                key = mListenerKey++;
            } while (key == android.net.wifi.RttManager.INVALID_KEY );
            mListenerMap.put(key, listener);
            return key;
        }
    }

    private java.lang.Object getListener(int key) {
        if (key == android.net.wifi.RttManager.INVALID_KEY)
            return null;

        synchronized(mListenerMapLock) {
            java.lang.Object listener = mListenerMap.get(key);
            return listener;
        }
    }

    private int getListenerKey(java.lang.Object listener) {
        if (listener == null)
            return android.net.wifi.RttManager.INVALID_KEY;

        synchronized(mListenerMapLock) {
            int index = mListenerMap.indexOfValue(listener);
            if (index == (-1)) {
                return android.net.wifi.RttManager.INVALID_KEY;
            } else {
                return mListenerMap.keyAt(index);
            }
        }
    }

    private java.lang.Object removeListener(int key) {
        if (key == android.net.wifi.RttManager.INVALID_KEY)
            return null;

        synchronized(mListenerMapLock) {
            java.lang.Object listener = mListenerMap.get(key);
            mListenerMap.remove(key);
            return listener;
        }
    }

    private int removeListener(java.lang.Object listener) {
        int key = getListenerKey(listener);
        if (key == android.net.wifi.RttManager.INVALID_KEY)
            return key;

        synchronized(mListenerMapLock) {
            mListenerMap.remove(key);
            return key;
        }
    }

    private class ServiceHandler extends android.os.Handler {
        ServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.util.Log.i(android.net.wifi.RttManager.TAG, "RTT manager get message: " + msg.what);
            switch (msg.what) {
                case com.android.internal.util.AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED :
                    return;
                case com.android.internal.util.AsyncChannel.CMD_CHANNEL_DISCONNECTED :
                    android.util.Log.e(android.net.wifi.RttManager.TAG, "Channel connection lost");
                    // This will cause all further async API calls on the WifiManager
                    // to fail and throw an exception
                    mAsyncChannel = null;
                    getLooper().quit();
                    return;
            }
            java.lang.Object listener = getListener(msg.arg2);
            if (listener == null) {
                android.util.Log.e(android.net.wifi.RttManager.TAG, "invalid listener key = " + msg.arg2);
                return;
            } else {
                android.util.Log.i(android.net.wifi.RttManager.TAG, "listener key = " + msg.arg2);
            }
            switch (msg.what) {
                /* ActionListeners grouped together */
                case android.net.wifi.RttManager.CMD_OP_SUCCEEDED :
                    reportSuccess(listener, msg);
                    removeListener(msg.arg2);
                    break;
                case android.net.wifi.RttManager.CMD_OP_FAILED :
                    reportFailure(listener, msg);
                    removeListener(msg.arg2);
                    break;
                case android.net.wifi.RttManager.CMD_OP_ABORTED :
                    ((android.net.wifi.RttManager.RttListener) (listener)).onAborted();
                    removeListener(msg.arg2);
                    break;
                case android.net.wifi.RttManager.CMD_OP_ENALBE_RESPONDER_SUCCEEDED :
                    android.net.wifi.RttManager.ResponderConfig config = ((android.net.wifi.RttManager.ResponderConfig) (msg.obj));
                    ((android.net.wifi.RttManager.ResponderCallback) (listener)).onResponderEnabled(config);
                    break;
                case android.net.wifi.RttManager.CMD_OP_ENALBE_RESPONDER_FAILED :
                    ((android.net.wifi.RttManager.ResponderCallback) (listener)).onResponderEnableFailure(msg.arg1);
                    removeListener(msg.arg2);
                    break;
                default :
                    if (android.net.wifi.RttManager.DBG)
                        android.util.Log.d(android.net.wifi.RttManager.TAG, "Ignoring message " + msg.what);

                    return;
            }
        }

        void reportSuccess(java.lang.Object listener, android.os.Message msg) {
            android.net.wifi.RttManager.RttListener rttListener = ((android.net.wifi.RttManager.RttListener) (listener));
            android.net.wifi.RttManager.ParcelableRttResults parcelableResults = ((android.net.wifi.RttManager.ParcelableRttResults) (msg.obj));
            ((android.net.wifi.RttManager.RttListener) (listener)).onSuccess(parcelableResults.mResults);
        }

        void reportFailure(java.lang.Object listener, android.os.Message msg) {
            android.net.wifi.RttManager.RttListener rttListener = ((android.net.wifi.RttManager.RttListener) (listener));
            android.os.Bundle bundle = ((android.os.Bundle) (msg.obj));
            ((android.net.wifi.RttManager.RttListener) (listener)).onFailure(msg.arg1, bundle.getString(android.net.wifi.RttManager.DESCRIPTION_KEY));
        }
    }
}

