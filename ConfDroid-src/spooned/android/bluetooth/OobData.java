/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Out Of Band Data for Bluetooth device pairing.
 *
 * <p>This object represents optional data obtained from a remote device through
 * an out-of-band channel (eg. NFC).
 *
 * @unknown 
 */
public class OobData implements android.os.Parcelable {
    private byte[] securityManagerTk;

    public byte[] getSecurityManagerTk() {
        return securityManagerTk;
    }

    /**
     * Sets the Temporary Key value to be used by the LE Security Manager during
     * LE pairing. The value shall be 16 bytes. Please see Bluetooth CSSv6,
     * Part A 1.8 for a detailed description.
     */
    public void setSecurityManagerTk(byte[] securityManagerTk) {
        this.securityManagerTk = securityManagerTk;
    }

    public OobData() {
    }

    private OobData(android.os.Parcel in) {
        securityManagerTk = in.createByteArray();
    }

    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeByteArray(securityManagerTk);
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.OobData> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.OobData>() {
        public android.bluetooth.OobData createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.OobData(in);
        }

        public android.bluetooth.OobData[] newArray(int size) {
            return new android.bluetooth.OobData[size];
        }
    };
}

