/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.net;


/**
 * Information about quota status on a specific network.
 *
 * @unknown 
 */
public class NetworkQuotaInfo implements android.os.Parcelable {
    private final long mEstimatedBytes;

    private final long mSoftLimitBytes;

    private final long mHardLimitBytes;

    public static final long NO_LIMIT = -1;

    /**
     * {@hide }
     */
    public NetworkQuotaInfo(long estimatedBytes, long softLimitBytes, long hardLimitBytes) {
        mEstimatedBytes = estimatedBytes;
        mSoftLimitBytes = softLimitBytes;
        mHardLimitBytes = hardLimitBytes;
    }

    /**
     * {@hide }
     */
    public NetworkQuotaInfo(android.os.Parcel in) {
        mEstimatedBytes = in.readLong();
        mSoftLimitBytes = in.readLong();
        mHardLimitBytes = in.readLong();
    }

    public long getEstimatedBytes() {
        return mEstimatedBytes;
    }

    public long getSoftLimitBytes() {
        return mSoftLimitBytes;
    }

    public long getHardLimitBytes() {
        return mHardLimitBytes;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(mEstimatedBytes);
        out.writeLong(mSoftLimitBytes);
        out.writeLong(mHardLimitBytes);
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkQuotaInfo> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkQuotaInfo>() {
        @java.lang.Override
        public android.net.NetworkQuotaInfo createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkQuotaInfo(in);
        }

        @java.lang.Override
        public android.net.NetworkQuotaInfo[] newArray(int size) {
            return new android.net.NetworkQuotaInfo[size];
        }
    };
}

