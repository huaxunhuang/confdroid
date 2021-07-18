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
 * Bundle of RSSI and packet count information, for WiFi watchdog
 *
 * @see WifiWatchdogStateMachine
 * @unknown 
 */
public class RssiPacketCountInfo implements android.os.Parcelable {
    public int rssi;

    public int txgood;

    public int txbad;

    public int rxgood;

    public RssiPacketCountInfo() {
        rssi = txgood = txbad = rxgood = 0;
    }

    private RssiPacketCountInfo(android.os.Parcel in) {
        rssi = in.readInt();
        txgood = in.readInt();
        txbad = in.readInt();
        rxgood = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(rssi);
        out.writeInt(txgood);
        out.writeInt(txbad);
        out.writeInt(rxgood);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.wifi.RssiPacketCountInfo> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.RssiPacketCountInfo>() {
        @java.lang.Override
        public android.net.wifi.RssiPacketCountInfo createFromParcel(android.os.Parcel in) {
            return new android.net.wifi.RssiPacketCountInfo(in);
        }

        @java.lang.Override
        public android.net.wifi.RssiPacketCountInfo[] newArray(int size) {
            return new android.net.wifi.RssiPacketCountInfo[size];
        }
    };
}

