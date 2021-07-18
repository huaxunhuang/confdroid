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
package android.net.wifi;


/**
 * Wifi Connection Statistics: gather various stats regarding WiFi connections,
 * connection requests, auto-join
 * and WiFi usage.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class WifiConnectionStatistics implements android.os.Parcelable {
    private static final java.lang.String TAG = "WifiConnnectionStatistics";

    /**
     * history of past connection to untrusted SSID
     *  Key = SSID
     *  Value = num connection
     */
    public java.util.HashMap<java.lang.String, android.net.wifi.WifiNetworkConnectionStatistics> untrustedNetworkHistory;

    // Number of time we polled the chip and were on 5GHz
    public int num5GhzConnected;

    // Number of time we polled the chip and were on 2.4GHz
    public int num24GhzConnected;

    // Number autojoin attempts
    public int numAutoJoinAttempt;

    // Number auto-roam attempts
    public int numAutoRoamAttempt;

    // Number wifimanager join attempts
    public int numWifiManagerJoinAttempt;

    public WifiConnectionStatistics() {
        untrustedNetworkHistory = new java.util.HashMap<java.lang.String, android.net.wifi.WifiNetworkConnectionStatistics>();
    }

    public void incrementOrAddUntrusted(java.lang.String SSID, int connection, int usage) {
        android.net.wifi.WifiNetworkConnectionStatistics stats;
        if (android.text.TextUtils.isEmpty(SSID))
            return;

        if (untrustedNetworkHistory.containsKey(SSID)) {
            stats = untrustedNetworkHistory.get(SSID);
            if (stats != null) {
                stats.numConnection = connection + stats.numConnection;
                stats.numUsage = usage + stats.numUsage;
            }
        } else {
            stats = new android.net.wifi.WifiNetworkConnectionStatistics(connection, usage);
        }
        if (stats != null) {
            untrustedNetworkHistory.put(SSID, stats);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sbuf = new java.lang.StringBuilder();
        sbuf.append("Connected on: 2.4Ghz=").append(num24GhzConnected);
        sbuf.append(" 5Ghz=").append(num5GhzConnected).append("\n");
        sbuf.append(" join=").append(numWifiManagerJoinAttempt);
        sbuf.append("\\").append(numAutoJoinAttempt).append("\n");
        sbuf.append(" roam=").append(numAutoRoamAttempt).append("\n");
        for (java.lang.String Key : untrustedNetworkHistory.keySet()) {
            android.net.wifi.WifiNetworkConnectionStatistics stats = untrustedNetworkHistory.get(Key);
            if (stats != null) {
                sbuf.append(Key).append(" ").append(stats.toString()).append("\n");
            }
        }
        return sbuf.toString();
    }

    /**
     * copy constructor
     */
    public WifiConnectionStatistics(android.net.wifi.WifiConnectionStatistics source) {
        untrustedNetworkHistory = new java.util.HashMap<java.lang.String, android.net.wifi.WifiNetworkConnectionStatistics>();
        if (source != null) {
            untrustedNetworkHistory.putAll(source.untrustedNetworkHistory);
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(num24GhzConnected);
        dest.writeInt(num5GhzConnected);
        dest.writeInt(numAutoJoinAttempt);
        dest.writeInt(numAutoRoamAttempt);
        dest.writeInt(numWifiManagerJoinAttempt);
        dest.writeInt(untrustedNetworkHistory.size());
        for (java.lang.String Key : untrustedNetworkHistory.keySet()) {
            android.net.wifi.WifiNetworkConnectionStatistics num = untrustedNetworkHistory.get(Key);
            dest.writeString(Key);
            dest.writeInt(num.numConnection);
            dest.writeInt(num.numUsage);
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.WifiConnectionStatistics> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WifiConnectionStatistics>() {
        public android.net.wifi.WifiConnectionStatistics createFromParcel(android.os.Parcel in) {
            android.net.wifi.WifiConnectionStatistics stats = new android.net.wifi.WifiConnectionStatistics();
            stats.num24GhzConnected = in.readInt();
            stats.num5GhzConnected = in.readInt();
            stats.numAutoJoinAttempt = in.readInt();
            stats.numAutoRoamAttempt = in.readInt();
            stats.numWifiManagerJoinAttempt = in.readInt();
            int n = in.readInt();
            while ((n--) > 0) {
                java.lang.String Key = in.readString();
                int numConnection = in.readInt();
                int numUsage = in.readInt();
                android.net.wifi.WifiNetworkConnectionStatistics st = new android.net.wifi.WifiNetworkConnectionStatistics(numConnection, numUsage);
                stats.untrustedNetworkHistory.put(Key, st);
            } 
            return stats;
        }

        public android.net.wifi.WifiConnectionStatistics[] newArray(int size) {
            return new android.net.wifi.WifiConnectionStatistics[size];
        }
    };
}

