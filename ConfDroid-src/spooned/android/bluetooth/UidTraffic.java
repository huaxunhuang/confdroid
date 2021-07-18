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
package android.bluetooth;


/**
 * Record of data traffic (in bytes) by an application identified by its UID.
 *
 * @unknown 
 */
public class UidTraffic implements android.os.Parcelable , java.lang.Cloneable {
    private final int mAppUid;

    private long mRxBytes;

    private long mTxBytes;

    public UidTraffic(int appUid) {
        mAppUid = appUid;
    }

    public UidTraffic(int appUid, long rx, long tx) {
        mAppUid = appUid;
        mRxBytes = rx;
        mTxBytes = tx;
    }

    UidTraffic(android.os.Parcel in) {
        mAppUid = in.readInt();
        mRxBytes = in.readLong();
        mTxBytes = in.readLong();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mAppUid);
        dest.writeLong(mRxBytes);
        dest.writeLong(mTxBytes);
    }

    public void setRxBytes(long bytes) {
        mRxBytes = bytes;
    }

    public void setTxBytes(long bytes) {
        mTxBytes = bytes;
    }

    public void addRxBytes(long bytes) {
        mRxBytes += bytes;
    }

    public void addTxBytes(long bytes) {
        mTxBytes += bytes;
    }

    public int getUid() {
        return mAppUid;
    }

    public long getRxBytes() {
        return mRxBytes;
    }

    public long getTxBytes() {
        return mTxBytes;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public android.bluetooth.UidTraffic clone() {
        return new android.bluetooth.UidTraffic(mAppUid, mRxBytes, mTxBytes);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("UidTraffic{" + "mAppUid=") + mAppUid) + ", mRxBytes=") + mRxBytes) + ", mTxBytes=") + mTxBytes) + '}';
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.UidTraffic> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.UidTraffic>() {
        @java.lang.Override
        public android.bluetooth.UidTraffic createFromParcel(android.os.Parcel source) {
            return new android.bluetooth.UidTraffic(source);
        }

        @java.lang.Override
        public android.bluetooth.UidTraffic[] newArray(int size) {
            return new android.bluetooth.UidTraffic[size];
        }
    };
}

