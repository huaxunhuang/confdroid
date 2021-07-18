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
 * Connection Statistics For a WiFi Network.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class WifiNetworkConnectionStatistics implements android.os.Parcelable {
    private static final java.lang.String TAG = "WifiNetworkConnnectionStatistics";

    public int numConnection;

    public int numUsage;

    public WifiNetworkConnectionStatistics(int connection, int usage) {
        numConnection = connection;
        numUsage = usage;
    }

    public WifiNetworkConnectionStatistics() {
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sbuf = new java.lang.StringBuilder();
        sbuf.append("c=").append(numConnection);
        sbuf.append(" u=").append(numUsage);
        return sbuf.toString();
    }

    /**
     * copy constructor
     */
    public WifiNetworkConnectionStatistics(android.net.wifi.WifiNetworkConnectionStatistics source) {
        numConnection = source.numConnection;
        numUsage = source.numUsage;
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
        dest.writeInt(numConnection);
        dest.writeInt(numUsage);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.WifiNetworkConnectionStatistics> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.WifiNetworkConnectionStatistics>() {
        public android.net.wifi.WifiNetworkConnectionStatistics createFromParcel(android.os.Parcel in) {
            int numConnection = in.readInt();
            int numUsage = in.readInt();
            android.net.wifi.WifiNetworkConnectionStatistics stats = new android.net.wifi.WifiNetworkConnectionStatistics(numConnection, numUsage);
            return stats;
        }

        public android.net.wifi.WifiNetworkConnectionStatistics[] newArray(int size) {
            return new android.net.wifi.WifiNetworkConnectionStatistics[size];
        }
    };
}

