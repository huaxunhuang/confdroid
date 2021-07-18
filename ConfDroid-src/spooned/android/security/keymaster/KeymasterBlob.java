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
package android.security.keymaster;


/**
 *
 *
 * @unknown 
 */
public class KeymasterBlob implements android.os.Parcelable {
    public byte[] blob;

    public KeymasterBlob(byte[] blob) {
        this.blob = blob;
    }

    public static final android.os.Parcelable.Creator<android.security.keymaster.KeymasterBlob> CREATOR = new android.os.Parcelable.Creator<android.security.keymaster.KeymasterBlob>() {
        public android.security.keymaster.KeymasterBlob createFromParcel(android.os.Parcel in) {
            return new android.security.keymaster.KeymasterBlob(in);
        }

        public android.security.keymaster.KeymasterBlob[] newArray(int length) {
            return new android.security.keymaster.KeymasterBlob[length];
        }
    };

    protected KeymasterBlob(android.os.Parcel in) {
        blob = in.createByteArray();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeByteArray(blob);
    }
}

