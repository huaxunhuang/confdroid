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
package android.security.keymaster;


/**
 * Utility class for the Java side of keystore-generated certificate chains.
 *
 * Serialization code for this must be kept in sync with system/security/keystore
 *
 * @unknown 
 */
public class KeymasterCertificateChain implements android.os.Parcelable {
    private java.util.List<byte[]> mCertificates;

    public static final android.os.Parcelable.Creator<android.security.keymaster.KeymasterCertificateChain> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.KeymasterCertificateChain>() {
        public android.security.keymaster.KeymasterCertificateChain createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.KeymasterCertificateChain(in);
        }

        public android.security.keymaster.KeymasterCertificateChain[] newArray(int size) {
            return new android.security.keymaster.KeymasterCertificateChain[size];
        }
    };

    public KeymasterCertificateChain() {
        mCertificates = null;
    }

    public KeymasterCertificateChain(java.util.List<byte[]> mCertificates) {
        this.mCertificates = mCertificates;
    }

    private KeymasterCertificateChain(android.os.Parcel in) {
        readFromParcel(in);
    }

    public java.util.List<byte[]> getCertificates() {
        return mCertificates;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        if (mCertificates == null) {
            out.writeInt(0);
        } else {
            out.writeInt(mCertificates.size());
            for (byte[] arg : mCertificates) {
                out.writeByteArray(arg);
            }
        }
    }

    public void readFromParcel(android.os.Parcel in) {
        int length = in.readInt();
        mCertificates = new java.util.ArrayList<byte[]>(length);
        for (int i = 0; i < length; i++) {
            mCertificates.add(in.createByteArray());
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

