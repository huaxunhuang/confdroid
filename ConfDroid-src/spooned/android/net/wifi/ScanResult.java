/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.net.wifi;


/**
 * Describes information about a detected access point. In addition
 * to the attributes described here, the supplicant keeps track of
 * {@code quality}, {@code noise}, and {@code maxbitrate} attributes,
 * but does not currently report them to external clients.
 */
public class ScanResult implements android.os.Parcelable {
    /**
     * The network name.
     */
    public java.lang.String SSID;

    /**
     * Ascii encoded SSID. This will replace SSID when we deprecate it. @hide
     */
    public android.net.wifi.WifiSsid wifiSsid;

    /**
     * The address of the access point.
     */
    public java.lang.String BSSID;

    /**
     * The HESSID from the beacon.
     *
     * @unknown 
     */
    public long hessid;

    /**
     * The ANQP Domain ID from the Hotspot 2.0 Indication element, if present.
     *
     * @unknown 
     */
    public int anqpDomainId;

    /* This field is equivalent to the |flags|, rather than the |capabilities| field
    of the per-BSS scan results returned by WPA supplicant. See the definition of
    |struct wpa_bss| in wpa_supplicant/bss.h for more details.
     */
    /**
     * Describes the authentication, key management, and encryption schemes
     * supported by the access point.
     */
    public java.lang.String capabilities;

    /**
     * The detected signal level in dBm, also known as the RSSI.
     *
     * <p>Use {@link android.net.wifi.WifiManager#calculateSignalLevel} to convert this number into
     * an absolute signal level which can be displayed to a user.
     */
    public int level;

    /**
     * The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating
     * with the access point.
     */
    public int frequency;

    /**
     * AP Channel bandwidth is 20 MHZ
     */
    public static final int CHANNEL_WIDTH_20MHZ = 0;

    /**
     * AP Channel bandwidth is 40 MHZ
     */
    public static final int CHANNEL_WIDTH_40MHZ = 1;

    /**
     * AP Channel bandwidth is 80 MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ = 2;

    /**
     * AP Channel bandwidth is 160 MHZ
     */
    public static final int CHANNEL_WIDTH_160MHZ = 3;

    /**
     * AP Channel bandwidth is 160 MHZ, but 80MHZ + 80MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;

    /**
     * AP Channel bandwidth; one of {@link #CHANNEL_WIDTH_20MHZ}, {@link #CHANNEL_WIDTH_40MHZ},
     * {@link #CHANNEL_WIDTH_80MHZ}, {@link #CHANNEL_WIDTH_160MHZ}
     * or {@link #CHANNEL_WIDTH_80MHZ_PLUS_MHZ}.
     */
    public int channelWidth;

    /**
     * Not used if the AP bandwidth is 20 MHz
     * If the AP use 40, 80 or 160 MHz, this is the center frequency (in MHz)
     * if the AP use 80 + 80 MHz, this is the center frequency of the first segment (in MHz)
     */
    public int centerFreq0;

    /**
     * Only used if the AP bandwidth is 80 + 80 MHz
     * if the AP use 80 + 80 MHz, this is the center frequency of the second segment (in MHz)
     */
    public int centerFreq1;

    /**
     *
     *
     * @deprecated use is80211mcResponder() instead
     * @unknown 
     */
    public boolean is80211McRTTResponder;

    /**
     * timestamp in microseconds (since boot) when
     * this result was last seen.
     */
    public long timestamp;

    /**
     * Timestamp representing date when this result was last seen, in milliseconds from 1970
     * {@hide }
     */
    public long seen;

    /**
     * If the scan result is a valid autojoin candidate
     * {@hide }
     */
    public int isAutoJoinCandidate;

    /**
     *
     *
     * @unknown Update RSSI of the scan result
     * @param previousRssi
     * 		
     * @param previousSeen
     * 		
     * @param maxAge
     * 		
     */
    public void averageRssi(int previousRssi, long previousSeen, int maxAge) {
        if (seen == 0) {
            seen = java.lang.System.currentTimeMillis();
        }
        long age = seen - previousSeen;
        if (((previousSeen > 0) && (age > 0)) && (age < (maxAge / 2))) {
            // Average the RSSI with previously seen instances of this scan result
            double alpha = 0.5 - (((double) (age)) / ((double) (maxAge)));
            level = ((int) ((((double) (level)) * (1 - alpha)) + (((double) (previousRssi)) * alpha)));
        }
    }

    /**
     * num IP configuration failures
     *
     * @unknown 
     */
    public int numIpConfigFailures;

    /**
     *
     *
     * @unknown Last time we blacklisted the ScanResult
     */
    public long blackListTimestamp;

    /**
     * Status: indicating the scan result is not a result
     * that is part of user's saved configurations
     *
     * @unknown 
     */
    public boolean untrusted;

    /**
     * Number of time we connected to it
     *
     * @unknown 
     */
    public int numConnection;

    /**
     * Number of time autojoin used it
     *
     * @unknown 
     */
    public int numUsage;

    /**
     * The approximate distance to the AP in centimeter, if available.  Else
     * {@link UNSPECIFIED}.
     * {@hide }
     */
    public int distanceCm;

    /**
     * The standard deviation of the distance to the access point, if available.
     * Else {@link UNSPECIFIED}.
     * {@hide }
     */
    public int distanceSdCm;

    /**
     * {@hide }
     */
    public static final long FLAG_PASSPOINT_NETWORK = 0x1;

    /**
     * {@hide }
     */
    public static final long FLAG_80211mc_RESPONDER = 0x2;

    /* These flags are specific to the ScanResult class, and are not related to the |flags|
    field of the per-BSS scan results from WPA supplicant.
     */
    /**
     * Defines flags; such as {@link #FLAG_PASSPOINT_NETWORK}.
     * {@hide }
     */
    public long flags;

    /**
     * sets a flag in {@link #flags} field
     *
     * @param flag
     * 		flag to set
     * @unknown 
     */
    public void setFlag(long flag) {
        flags |= flag;
    }

    /**
     * clears a flag in {@link #flags} field
     *
     * @param flag
     * 		flag to set
     * @unknown 
     */
    public void clearFlag(long flag) {
        flags &= ~flag;
    }

    public boolean is80211mcResponder() {
        return (flags & android.net.wifi.ScanResult.FLAG_80211mc_RESPONDER) != 0;
    }

    public boolean isPasspointNetwork() {
        return (flags & android.net.wifi.ScanResult.FLAG_PASSPOINT_NETWORK) != 0;
    }

    /**
     * Indicates venue name (such as 'San Francisco Airport') published by access point; only
     * available on passpoint network and if published by access point.
     */
    public java.lang.CharSequence venueName;

    /**
     * Indicates passpoint operator name published by access point.
     */
    public java.lang.CharSequence operatorFriendlyName;

    /**
     * {@hide }
     */
    public static final int UNSPECIFIED = -1;

    /**
     *
     *
     * @unknown 
     */
    public boolean is24GHz() {
        return android.net.wifi.ScanResult.is24GHz(frequency);
    }

    /**
     *
     *
     * @unknown TODO: makes real freq boundaries
     */
    public static boolean is24GHz(int freq) {
        return (freq > 2400) && (freq < 2500);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean is5GHz() {
        return android.net.wifi.ScanResult.is5GHz(frequency);
    }

    /**
     *
     *
     * @unknown TODO: makes real freq boundaries
     */
    public static boolean is5GHz(int freq) {
        return (freq > 4900) && (freq < 5900);
    }

    /**
     *
     *
     * @unknown anqp lines from supplicant BSS response
     */
    public java.util.List<java.lang.String> anqpLines;

    /**
     *
     *
     * @unknown storing the raw bytes of full result IEs
     */
    public byte[] bytes;

    /**
     * information elements from beacon
     *
     * @unknown 
     */
    public static class InformationElement {
        public static final int EID_SSID = 0;

        public static final int EID_SUPPORTED_RATES = 1;

        public static final int EID_TIM = 5;

        public static final int EID_BSS_LOAD = 11;

        public static final int EID_ERP = 42;

        public static final int EID_RSN = 48;

        public static final int EID_EXTENDED_SUPPORTED_RATES = 50;

        public static final int EID_HT_OPERATION = 61;

        public static final int EID_INTERWORKING = 107;

        public static final int EID_ROAMING_CONSORTIUM = 111;

        public static final int EID_EXTENDED_CAPS = 127;

        public static final int EID_VHT_OPERATION = 192;

        public static final int EID_VSA = 221;

        public int id;

        public byte[] bytes;

        public InformationElement() {
        }

        public InformationElement(android.net.wifi.ScanResult.InformationElement rhs) {
            this.id = rhs.id;
            this.bytes = rhs.bytes.clone();
        }
    }

    /**
     * information elements found in the beacon
     *
     * @unknown 
     */
    public android.net.wifi.ScanResult.InformationElement[] informationElements;

    /**
     * ANQP response elements.
     *
     * @unknown 
     */
    public android.net.wifi.AnqpInformationElement[] anqpElements;

    /**
     * {@hide }
     */
    public ScanResult(android.net.wifi.WifiSsid wifiSsid, java.lang.String BSSID, long hessid, int anqpDomainId, byte[] osuProviders, java.lang.String caps, int level, int frequency, long tsf) {
        this.wifiSsid = wifiSsid;
        this.SSID = (wifiSsid != null) ? wifiSsid.toString() : android.net.wifi.WifiSsid.NONE;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        if (osuProviders != null) {
            this.anqpElements = new android.net.wifi.AnqpInformationElement[1];
            this.anqpElements[0] = new android.net.wifi.AnqpInformationElement(android.net.wifi.AnqpInformationElement.HOTSPOT20_VENDOR_ID, android.net.wifi.AnqpInformationElement.HS_OSU_PROVIDERS, osuProviders);
        }
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = android.net.wifi.ScanResult.UNSPECIFIED;
        this.distanceSdCm = android.net.wifi.ScanResult.UNSPECIFIED;
        this.channelWidth = android.net.wifi.ScanResult.UNSPECIFIED;
        this.centerFreq0 = android.net.wifi.ScanResult.UNSPECIFIED;
        this.centerFreq1 = android.net.wifi.ScanResult.UNSPECIFIED;
        this.flags = 0;
    }

    /**
     * {@hide }
     */
    public ScanResult(android.net.wifi.WifiSsid wifiSsid, java.lang.String BSSID, java.lang.String caps, int level, int frequency, long tsf, int distCm, int distSdCm) {
        this.wifiSsid = wifiSsid;
        this.SSID = (wifiSsid != null) ? wifiSsid.toString() : android.net.wifi.WifiSsid.NONE;
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = android.net.wifi.ScanResult.UNSPECIFIED;
        this.centerFreq0 = android.net.wifi.ScanResult.UNSPECIFIED;
        this.centerFreq1 = android.net.wifi.ScanResult.UNSPECIFIED;
        this.flags = 0;
    }

    /**
     * {@hide }
     */
    public ScanResult(java.lang.String Ssid, java.lang.String BSSID, long hessid, int anqpDomainId, java.lang.String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this.SSID = Ssid;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        if (is80211McRTTResponder) {
            this.flags = android.net.wifi.ScanResult.FLAG_80211mc_RESPONDER;
        } else {
            this.flags = 0;
        }
    }

    /**
     * {@hide }
     */
    public ScanResult(android.net.wifi.WifiSsid wifiSsid, java.lang.String Ssid, java.lang.String BSSID, long hessid, int anqpDomainId, java.lang.String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this(Ssid, BSSID, hessid, anqpDomainId, caps, level, frequency, tsf, distCm, distSdCm, channelWidth, centerFreq0, centerFreq1, is80211McRTTResponder);
        this.wifiSsid = wifiSsid;
    }

    /**
     * copy constructor {@hide }
     */
    public ScanResult(android.net.wifi.ScanResult source) {
        if (source != null) {
            wifiSsid = source.wifiSsid;
            SSID = source.SSID;
            BSSID = source.BSSID;
            hessid = source.hessid;
            anqpDomainId = source.anqpDomainId;
            informationElements = source.informationElements;
            anqpElements = source.anqpElements;
            capabilities = source.capabilities;
            level = source.level;
            frequency = source.frequency;
            channelWidth = source.channelWidth;
            centerFreq0 = source.centerFreq0;
            centerFreq1 = source.centerFreq1;
            timestamp = source.timestamp;
            distanceCm = source.distanceCm;
            distanceSdCm = source.distanceSdCm;
            seen = source.seen;
            untrusted = source.untrusted;
            numConnection = source.numConnection;
            numUsage = source.numUsage;
            numIpConfigFailures = source.numIpConfigFailures;
            isAutoJoinCandidate = source.isAutoJoinCandidate;
            venueName = source.venueName;
            operatorFriendlyName = source.operatorFriendlyName;
            flags = source.flags;
        }
    }

    /**
     * empty scan result
     *
     * {@hide }
     */
    public ScanResult() {
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        java.lang.String none = "<none>";
        sb.append("SSID: ").append(wifiSsid == null ? android.net.wifi.WifiSsid.NONE : wifiSsid).append(", BSSID: ").append(BSSID == null ? none : BSSID).append(", capabilities: ").append(capabilities == null ? none : capabilities).append(", level: ").append(level).append(", frequency: ").append(frequency).append(", timestamp: ").append(timestamp);
        sb.append(", distance: ").append(distanceCm != android.net.wifi.ScanResult.UNSPECIFIED ? distanceCm : "?").append("(cm)");
        sb.append(", distanceSd: ").append(distanceSdCm != android.net.wifi.ScanResult.UNSPECIFIED ? distanceSdCm : "?").append("(cm)");
        sb.append(", passpoint: ");
        sb.append((flags & android.net.wifi.ScanResult.FLAG_PASSPOINT_NETWORK) != 0 ? "yes" : "no");
        sb.append(", ChannelBandwidth: ").append(channelWidth);
        sb.append(", centerFreq0: ").append(centerFreq0);
        sb.append(", centerFreq1: ").append(centerFreq1);
        sb.append(", 80211mcResponder: ");
        sb.append((flags & android.net.wifi.ScanResult.FLAG_80211mc_RESPONDER) != 0 ? "is supported" : "is not supported");
        return sb.toString();
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (wifiSsid != null) {
            dest.writeInt(1);
            wifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(SSID);
        dest.writeString(BSSID);
        dest.writeLong(hessid);
        dest.writeInt(anqpDomainId);
        dest.writeString(capabilities);
        dest.writeInt(level);
        dest.writeInt(frequency);
        dest.writeLong(timestamp);
        dest.writeInt(distanceCm);
        dest.writeInt(distanceSdCm);
        dest.writeInt(channelWidth);
        dest.writeInt(centerFreq0);
        dest.writeInt(centerFreq1);
        dest.writeLong(seen);
        dest.writeInt(untrusted ? 1 : 0);
        dest.writeInt(numConnection);
        dest.writeInt(numUsage);
        dest.writeInt(numIpConfigFailures);
        dest.writeInt(isAutoJoinCandidate);
        dest.writeString(venueName != null ? venueName.toString() : "");
        dest.writeString(operatorFriendlyName != null ? operatorFriendlyName.toString() : "");
        dest.writeLong(this.flags);
        if (informationElements != null) {
            dest.writeInt(informationElements.length);
            for (int i = 0; i < informationElements.length; i++) {
                dest.writeInt(informationElements[i].id);
                dest.writeInt(informationElements[i].bytes.length);
                dest.writeByteArray(informationElements[i].bytes);
            }
        } else {
            dest.writeInt(0);
        }
        if (anqpLines != null) {
            dest.writeInt(anqpLines.size());
            for (int i = 0; i < anqpLines.size(); i++) {
                dest.writeString(anqpLines.get(i));
            }
        } else {
            dest.writeInt(0);
        }
        if (anqpElements != null) {
            dest.writeInt(anqpElements.length);
            for (android.net.wifi.AnqpInformationElement element : anqpElements) {
                dest.writeInt(element.getVendorId());
                dest.writeInt(element.getElementId());
                dest.writeInt(element.getPayload().length);
                dest.writeByteArray(element.getPayload());
            }
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.ScanResult> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.ScanResult>() {
        public android.net.wifi.ScanResult createFromParcel(android.os.Parcel in) {
            android.net.wifi.WifiSsid wifiSsid = null;
            if (in.readInt() == 1) {
                wifiSsid = android.net.wifi.WifiSsid.CREATOR.createFromParcel(in);
            }
            android.net.wifi.ScanResult sr = /* SSID */
            /* BSSID */
            /* HESSID */
            /* ANQP Domain ID */
            /* capabilities */
            /* level */
            /* frequency */
            /* timestamp */
            /* distanceCm */
            /* distanceSdCm */
            /* channelWidth */
            /* centerFreq0 */
            /* centerFreq1 */
            /* rtt responder,
            fixed with flags below
             */
            new android.net.wifi.ScanResult(wifiSsid, in.readString(), in.readString(), in.readLong(), in.readInt(), in.readString(), in.readInt(), in.readInt(), in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);
            sr.seen = in.readLong();
            sr.untrusted = in.readInt() != 0;
            sr.numConnection = in.readInt();
            sr.numUsage = in.readInt();
            sr.numIpConfigFailures = in.readInt();
            sr.isAutoJoinCandidate = in.readInt();
            sr.venueName = in.readString();
            sr.operatorFriendlyName = in.readString();
            sr.flags = in.readLong();
            int n = in.readInt();
            if (n != 0) {
                sr.informationElements = new android.net.wifi.ScanResult.InformationElement[n];
                for (int i = 0; i < n; i++) {
                    sr.informationElements[i] = new android.net.wifi.ScanResult.InformationElement();
                    sr.informationElements[i].id = in.readInt();
                    int len = in.readInt();
                    sr.informationElements[i].bytes = new byte[len];
                    in.readByteArray(sr.informationElements[i].bytes);
                }
            }
            n = in.readInt();
            if (n != 0) {
                sr.anqpLines = new java.util.ArrayList<java.lang.String>();
                for (int i = 0; i < n; i++) {
                    sr.anqpLines.add(in.readString());
                }
            }
            n = in.readInt();
            if (n != 0) {
                sr.anqpElements = new android.net.wifi.AnqpInformationElement[n];
                for (int i = 0; i < n; i++) {
                    int vendorId = in.readInt();
                    int elementId = in.readInt();
                    int len = in.readInt();
                    byte[] payload = new byte[len];
                    in.readByteArray(payload);
                    sr.anqpElements[i] = new android.net.wifi.AnqpInformationElement(vendorId, elementId, payload);
                }
            }
            return sr;
        }

        public android.net.wifi.ScanResult[] newArray(int size) {
            return new android.net.wifi.ScanResult[size];
        }
    };
}

