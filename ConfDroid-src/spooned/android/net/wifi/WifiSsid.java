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
package android.net.wifi;


/**
 * Stores SSID octets and handles conversion.
 *
 * For Ascii encoded string, any octet < 32 or > 127 is encoded as
 * a "\x" followed by the hex representation of the octet.
 * Exception chars are ", \, \e, \n, \r, \t which are escaped by a \
 * See src/utils/common.c for the implementation in the supplicant.
 *
 * @unknown 
 */
public class WifiSsid implements android.os.Parcelable {
    private static final java.lang.String TAG = "WifiSsid";

    public final java.io.ByteArrayOutputStream octets = new java.io.ByteArrayOutputStream(32);

    private static final int HEX_RADIX = 16;

    public static final java.lang.String NONE = "<unknown ssid>";

    private WifiSsid() {
    }

    public static android.net.wifi.WifiSsid createFromAsciiEncoded(java.lang.String asciiEncoded) {
        android.net.wifi.WifiSsid a = new android.net.wifi.WifiSsid();
        a.convertToBytes(asciiEncoded);
        return a;
    }

    public static android.net.wifi.WifiSsid createFromHex(java.lang.String hexStr) {
        android.net.wifi.WifiSsid a = new android.net.wifi.WifiSsid();
        if (hexStr == null)
            return a;

        if (hexStr.startsWith("0x") || hexStr.startsWith("0X")) {
            hexStr = hexStr.substring(2);
        }
        for (int i = 0; i < (hexStr.length() - 1); i += 2) {
            int val;
            try {
                val = java.lang.Integer.parseInt(hexStr.substring(i, i + 2), android.net.wifi.WifiSsid.HEX_RADIX);
            } catch (java.lang.NumberFormatException e) {
                val = 0;
            }
            a.octets.write(val);
        }
        return a;
    }

    /* This function is equivalent to printf_decode() at src/utils/common.c in
    the supplicant
     */
    private void convertToBytes(java.lang.String asciiEncoded) {
        int i = 0;
        int val = 0;
        while (i < asciiEncoded.length()) {
            char c = asciiEncoded.charAt(i);
            switch (c) {
                case '\\' :
                    i++;
                    switch (asciiEncoded.charAt(i)) {
                        case '\\' :
                            octets.write('\\');
                            i++;
                            break;
                        case '"' :
                            octets.write('"');
                            i++;
                            break;
                        case 'n' :
                            octets.write('\n');
                            i++;
                            break;
                        case 'r' :
                            octets.write('\r');
                            i++;
                            break;
                        case 't' :
                            octets.write('\t');
                            i++;
                            break;
                        case 'e' :
                            octets.write(27);// escape char

                            i++;
                            break;
                        case 'x' :
                            i++;
                            try {
                                val = java.lang.Integer.parseInt(asciiEncoded.substring(i, i + 2), android.net.wifi.WifiSsid.HEX_RADIX);
                            } catch (java.lang.NumberFormatException e) {
                                val = -1;
                            }
                            if (val < 0) {
                                val = java.lang.Character.digit(asciiEncoded.charAt(i), android.net.wifi.WifiSsid.HEX_RADIX);
                                if (val < 0)
                                    break;

                                octets.write(val);
                                i++;
                            } else {
                                octets.write(val);
                                i += 2;
                            }
                            break;
                        case '0' :
                        case '1' :
                        case '2' :
                        case '3' :
                        case '4' :
                        case '5' :
                        case '6' :
                        case '7' :
                            val = asciiEncoded.charAt(i) - '0';
                            i++;
                            if ((asciiEncoded.charAt(i) >= '0') && (asciiEncoded.charAt(i) <= '7')) {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - '0';
                                i++;
                            }
                            if ((asciiEncoded.charAt(i) >= '0') && (asciiEncoded.charAt(i) <= '7')) {
                                val = ((val * 8) + asciiEncoded.charAt(i)) - '0';
                                i++;
                            }
                            octets.write(val);
                            break;
                        default :
                            break;
                    }
                    break;
                default :
                    octets.write(c);
                    i++;
                    break;
            }
        } 
    }

    @java.lang.Override
    public java.lang.String toString() {
        byte[] ssidBytes = octets.toByteArray();
        // Supplicant returns \x00\x00\x00\x00\x00\x00\x00\x00 hex string
        // for a hidden access point. Make sure we maintain the previous
        // behavior of returning empty string for this case.
        if ((octets.size() <= 0) || isArrayAllZeroes(ssidBytes))
            return "";

        // TODO: Handle conversion to other charsets upon failure
        java.nio.charset.Charset charset = java.nio.charset.Charset.forName("UTF-8");
        java.nio.charset.CharsetDecoder decoder = charset.newDecoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE);
        java.nio.CharBuffer out = java.nio.CharBuffer.allocate(32);
        java.nio.charset.CoderResult result = decoder.decode(java.nio.ByteBuffer.wrap(ssidBytes), out, true);
        out.flip();
        if (result.isError()) {
            return android.net.wifi.WifiSsid.NONE;
        }
        return out.toString();
    }

    private boolean isArrayAllZeroes(byte[] ssidBytes) {
        for (int i = 0; i < ssidBytes.length; i++) {
            if (ssidBytes[i] != 0)
                return false;

        }
        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isHidden() {
        return isArrayAllZeroes(octets.toByteArray());
    }

    /**
     *
     *
     * @unknown 
     */
    public byte[] getOctets() {
        return octets.toByteArray();
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getHexString() {
        java.lang.String out = "0x";
        byte[] ssidbytes = getOctets();
        for (int i = 0; i < octets.size(); i++) {
            out += java.lang.String.format(java.util.Locale.US, "%02x", ssidbytes[i]);
        }
        return octets.size() > 0 ? out : null;
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
        dest.writeInt(octets.size());
        dest.writeByteArray(octets.toByteArray());
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.WifiSsid> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WifiSsid>() {
        public android.net.wifi.WifiSsid createFromParcel(android.os.Parcel in) {
            android.net.wifi.WifiSsid ssid = new android.net.wifi.WifiSsid();
            int length = in.readInt();
            byte[] b = new byte[length];
            in.readByteArray(b);
            ssid.octets.write(b, 0, length);
            return ssid;
        }

        public android.net.wifi.WifiSsid[] newArray(int size) {
            return new android.net.wifi.WifiSsid[size];
        }
    };
}

