/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.net.metrics;


/**
 * Event class used to record error events when parsing DHCP response packets.
 * {@hide }
 */
@android.annotation.SystemApi
public final class DhcpErrorEvent implements android.os.Parcelable {
    public static final int L2_ERROR = 1;

    public static final int L3_ERROR = 2;

    public static final int L4_ERROR = 3;

    public static final int DHCP_ERROR = 4;

    public static final int MISC_ERROR = 5;

    public static final int L2_TOO_SHORT = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L2_ERROR, 1);

    public static final int L2_WRONG_ETH_TYPE = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L2_ERROR, 2);

    public static final int L3_TOO_SHORT = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L3_ERROR, 1);

    public static final int L3_NOT_IPV4 = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L3_ERROR, 2);

    public static final int L3_INVALID_IP = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L3_ERROR, 3);

    public static final int L4_NOT_UDP = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L4_ERROR, 1);

    public static final int L4_WRONG_PORT = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.L4_ERROR, 2);

    public static final int BOOTP_TOO_SHORT = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 1);

    public static final int DHCP_BAD_MAGIC_COOKIE = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 2);

    public static final int DHCP_INVALID_OPTION_LENGTH = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 3);

    public static final int DHCP_NO_MSG_TYPE = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 4);

    public static final int DHCP_UNKNOWN_MSG_TYPE = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 5);

    /**
     * {@hide }
     */
    public static final int DHCP_NO_COOKIE = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.DHCP_ERROR, 6);

    public static final int BUFFER_UNDERFLOW = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.MISC_ERROR, 1);

    public static final int RECEIVE_ERROR = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.MISC_ERROR, 2);

    /**
     * {@hide }
     */
    public static final int PARSING_ERROR = android.net.metrics.DhcpErrorEvent.makeErrorCode(android.net.metrics.DhcpErrorEvent.MISC_ERROR, 3);

    public final java.lang.String ifName;

    // error code byte format (MSB to LSB):
    // byte 0: error type
    // byte 1: error subtype
    // byte 2: unused
    // byte 3: optional code
    public final int errorCode;

    /**
     * {@hide }
     */
    public DhcpErrorEvent(java.lang.String ifName, int errorCode) {
        this.ifName = ifName;
        this.errorCode = errorCode;
    }

    private DhcpErrorEvent(android.os.Parcel in) {
        this.ifName = in.readString();
        this.errorCode = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(ifName);
        out.writeInt(errorCode);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.DhcpErrorEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.DhcpErrorEvent>() {
        public android.net.metrics.DhcpErrorEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.DhcpErrorEvent(in);
        }

        public android.net.metrics.DhcpErrorEvent[] newArray(int size) {
            return new android.net.metrics.DhcpErrorEvent[size];
        }
    };

    public static void logParseError(java.lang.String ifName, int errorCode) {
    }

    public static void logReceiveError(java.lang.String ifName) {
    }

    public static int errorCodeWithOption(int errorCode, int option) {
        return (0xffff0000 & errorCode) | (0xff & option);
    }

    private static int makeErrorCode(int type, int subtype) {
        return (type << 24) | ((0xff & subtype) << 16);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("DhcpErrorEvent(%s, %s)", ifName, android.net.metrics.DhcpErrorEvent.Decoder.constants.get(errorCode));
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.DhcpErrorEvent.class }, new java.lang.String[]{ "L2_", "L3_", "L4_", "BOOTP_", "DHCP_", "BUFFER_", "RECEIVE_", "PARSING_" });
    }
}

