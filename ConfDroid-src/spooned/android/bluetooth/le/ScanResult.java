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
package android.bluetooth.le;


/**
 * ScanResult for Bluetooth LE scan.
 */
public final class ScanResult implements android.os.Parcelable {
    // Remote bluetooth device.
    private android.bluetooth.BluetoothDevice mDevice;

    // Scan record, including advertising data and scan response data.
    @android.annotation.Nullable
    private android.bluetooth.le.ScanRecord mScanRecord;

    // Received signal strength.
    private int mRssi;

    // Device timestamp when the result was last seen.
    private long mTimestampNanos;

    /**
     * Constructor of scan result.
     *
     * @param device
     * 		Remote bluetooth device that is found.
     * @param scanRecord
     * 		Scan record including both advertising data and scan response data.
     * @param rssi
     * 		Received signal strength.
     * @param timestampNanos
     * 		Device timestamp when the scan result was observed.
     */
    public ScanResult(android.bluetooth.BluetoothDevice device, android.bluetooth.le.ScanRecord scanRecord, int rssi, long timestampNanos) {
        mDevice = device;
        mScanRecord = scanRecord;
        mRssi = rssi;
        mTimestampNanos = timestampNanos;
    }

    private ScanResult(android.os.Parcel in) {
        readFromParcel(in);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (mDevice != null) {
            dest.writeInt(1);
            mDevice.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (mScanRecord != null) {
            dest.writeInt(1);
            dest.writeByteArray(mScanRecord.getBytes());
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mRssi);
        dest.writeLong(mTimestampNanos);
    }

    private void readFromParcel(android.os.Parcel in) {
        if (in.readInt() == 1) {
            mDevice = android.bluetooth.BluetoothDevice.CREATOR.createFromParcel(in);
        }
        if (in.readInt() == 1) {
            mScanRecord = android.bluetooth.le.ScanRecord.parseFromBytes(in.createByteArray());
        }
        mRssi = in.readInt();
        mTimestampNanos = in.readLong();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Returns the remote bluetooth device identified by the bluetooth device address.
     */
    public android.bluetooth.BluetoothDevice getDevice() {
        return mDevice;
    }

    /**
     * Returns the scan record, which is a combination of advertisement and scan response.
     */
    @android.annotation.Nullable
    public android.bluetooth.le.ScanRecord getScanRecord() {
        return mScanRecord;
    }

    /**
     * Returns the received signal strength in dBm. The valid range is [-127, 127].
     */
    public int getRssi() {
        return mRssi;
    }

    /**
     * Returns timestamp since boot when the scan record was observed.
     */
    public long getTimestampNanos() {
        return mTimestampNanos;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mDevice, mRssi, mScanRecord, mTimestampNanos);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        android.bluetooth.le.ScanResult other = ((android.bluetooth.le.ScanResult) (obj));
        return ((java.util.Objects.equals(mDevice, other.mDevice) && (mRssi == other.mRssi)) && java.util.Objects.equals(mScanRecord, other.mScanRecord)) && (mTimestampNanos == other.mTimestampNanos);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((("ScanResult{" + "mDevice=") + mDevice) + ", mScanRecord=") + java.util.Objects.toString(mScanRecord)) + ", mRssi=") + mRssi) + ", mTimestampNanos=") + mTimestampNanos) + '}';
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.le.ScanResult> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.le.ScanResult>() {
        @java.lang.Override
        public android.bluetooth.le.ScanResult createFromParcel(android.os.Parcel source) {
            return new android.bluetooth.le.ScanResult(source);
        }

        @java.lang.Override
        public android.bluetooth.le.ScanResult[] newArray(int size) {
            return new android.bluetooth.le.ScanResult[size];
        }
    };
}

