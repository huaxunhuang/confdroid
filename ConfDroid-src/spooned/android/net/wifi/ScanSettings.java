/**
 * Copyright (c) 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.net.wifi;


/**
 * Bundle of customized scan settings
 *
 * @see WifiManager#startCustomizedScan
 * @unknown 
 */
public class ScanSettings implements android.os.Parcelable {
    /**
     * channel set to scan. this can be null or empty, indicating a full scan
     */
    public java.util.Collection<android.net.wifi.WifiChannel> channelSet;

    /**
     * public constructor
     */
    public ScanSettings() {
    }

    /**
     * copy constructor
     */
    public ScanSettings(android.net.wifi.ScanSettings source) {
        if (source.channelSet != null)
            channelSet = new java.util.ArrayList<android.net.wifi.WifiChannel>(source.channelSet);

    }

    /**
     * check for validity
     */
    public boolean isValid() {
        for (android.net.wifi.WifiChannel channel : channelSet)
            if (!channel.isValid())
                return false;


        return true;
    }

    /**
     * implement Parcelable interface
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * implement Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(channelSet == null ? 0 : channelSet.size());
        if (channelSet != null)
            for (android.net.wifi.WifiChannel channel : channelSet)
                channel.writeToParcel(out, flags);


    }

    /**
     * implement Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.ScanSettings> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.ScanSettings>() {
        @java.lang.Override
        public android.net.wifi.ScanSettings createFromParcel(android.os.Parcel in) {
            android.net.wifi.ScanSettings settings = new android.net.wifi.ScanSettings();
            int size = in.readInt();
            if (size > 0) {
                settings.channelSet = new java.util.ArrayList<android.net.wifi.WifiChannel>(size);
                while ((size--) > 0)
                    settings.channelSet.add(android.net.wifi.WifiChannel.CREATOR.createFromParcel(in));

            }
            return settings;
        }

        @java.lang.Override
        public android.net.wifi.ScanSettings[] newArray(int size) {
            return new android.net.wifi.ScanSettings[size];
        }
    };
}

