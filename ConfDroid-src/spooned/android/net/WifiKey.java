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
 * limitations under the License
 */
package android.net;


/**
 * Information identifying a Wi-Fi network.
 *
 * @see NetworkKey
 * @unknown 
 */
@android.annotation.SystemApi
public class WifiKey implements android.os.Parcelable {
    // Patterns used for validation.
    private static final java.util.regex.Pattern SSID_PATTERN = java.util.regex.Pattern.compile("(\".*\")|(0x[\\p{XDigit}]+)", java.util.regex.Pattern.DOTALL);

    private static final java.util.regex.Pattern BSSID_PATTERN = java.util.regex.Pattern.compile("([\\p{XDigit}]{2}:){5}[\\p{XDigit}]{2}");

    /**
     * The service set identifier (SSID) of an 802.11 network. If the SSID can be decoded as
     * UTF-8, it will be surrounded by double quotation marks. Otherwise, it will be a string of
     * hex digits starting with 0x.
     */
    public final java.lang.String ssid;

    /**
     * The basic service set identifier (BSSID) of an access point for this network. This will
     * be in the form of a six-byte MAC address: {@code XX:XX:XX:XX:XX:XX}, where each X is a
     * hexadecimal digit.
     */
    public final java.lang.String bssid;

    /**
     * Construct a new {@link WifiKey} for the given Wi-Fi SSID/BSSID pair.
     *
     * @param ssid
     * 		the service set identifier (SSID) of an 802.11 network. If the SSID can be
     * 		decoded as UTF-8, it should be surrounded by double quotation marks. Otherwise,
     * 		it should be a string of hex digits starting with 0x.
     * @param bssid
     * 		the basic service set identifier (BSSID) of this network's access point.
     * 		This should be in the form of a six-byte MAC address: {@code XX:XX:XX:XX:XX:XX},
     * 		where each X is a hexadecimal digit.
     * @throws IllegalArgumentException
     * 		if either the SSID or BSSID is invalid.
     */
    public WifiKey(java.lang.String ssid, java.lang.String bssid) {
        if (!android.net.WifiKey.SSID_PATTERN.matcher(ssid).matches()) {
            throw new java.lang.IllegalArgumentException("Invalid ssid: " + ssid);
        }
        if (!android.net.WifiKey.BSSID_PATTERN.matcher(bssid).matches()) {
            throw new java.lang.IllegalArgumentException("Invalid bssid: " + bssid);
        }
        this.ssid = ssid;
        this.bssid = bssid;
    }

    private WifiKey(android.os.Parcel in) {
        ssid = in.readString();
        bssid = in.readString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(ssid);
        out.writeString(bssid);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.net.WifiKey wifiKey = ((android.net.WifiKey) (o));
        return java.util.Objects.equals(ssid, wifiKey.ssid) && java.util.Objects.equals(bssid, wifiKey.bssid);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(ssid, bssid);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("WifiKey[SSID=" + ssid) + ",BSSID=") + bssid) + "]";
    }

    public static final android.os.Parcelable.Creator<android.net.WifiKey> CREATOR = new android.os.Parcelable.Creator<android.net.WifiKey>() {
        @java.lang.Override
        public android.net.WifiKey createFromParcel(android.os.Parcel in) {
            return new android.net.WifiKey(in);
        }

        @java.lang.Override
        public android.net.WifiKey[] newArray(int size) {
            return new android.net.WifiKey[size];
        }
    };
}

