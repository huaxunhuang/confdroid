/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.media.tv;


/**
 * Simple container for information about DVB device.
 * Not for third-party developers.
 *
 * @unknown 
 */
public final class DvbDeviceInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "DvbDeviceInfo";

    public static final android.os.Parcelable.Creator<android.media.tv.DvbDeviceInfo> CREATOR = new android.os.Parcelable.Creator<android.media.tv.DvbDeviceInfo>() {
        @java.lang.Override
        public android.media.tv.DvbDeviceInfo createFromParcel(android.os.Parcel source) {
            try {
                return new android.media.tv.DvbDeviceInfo(source);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.media.tv.DvbDeviceInfo.TAG, "Exception creating DvbDeviceInfo from parcel", e);
                return null;
            }
        }

        @java.lang.Override
        public android.media.tv.DvbDeviceInfo[] newArray(int size) {
            return new android.media.tv.DvbDeviceInfo[size];
        }
    };

    private final int mAdapterId;

    private final int mDeviceId;

    private DvbDeviceInfo(android.os.Parcel source) {
        mAdapterId = source.readInt();
        mDeviceId = source.readInt();
    }

    /**
     * Constructs a new {@link DvbDeviceInfo} with the given adapter ID and device ID.
     */
    public DvbDeviceInfo(int adapterId, int deviceId) {
        mAdapterId = adapterId;
        mDeviceId = deviceId;
    }

    /**
     * Returns the adapter ID of DVB device, in terms of enumerating the DVB device adapters
     * installed in the system. The adapter ID counts from zero.
     */
    public int getAdapterId() {
        return mAdapterId;
    }

    /**
     * Returns the device ID of DVB device, in terms of enumerating the DVB devices attached to
     * the same device adapter. The device ID counts from zero.
     */
    public int getDeviceId() {
        return mDeviceId;
    }

    // Parcelable
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mAdapterId);
        dest.writeInt(mDeviceId);
    }
}

