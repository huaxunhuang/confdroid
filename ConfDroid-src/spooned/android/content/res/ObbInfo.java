/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.content.res;


/**
 * Basic information about a Opaque Binary Blob (OBB) that reflects the info
 * from the footer on the OBB file. This information may be manipulated by a
 * developer with the <code>obbtool</code> program in the Android SDK.
 */
public class ObbInfo implements android.os.Parcelable {
    /**
     * Flag noting that this OBB is an overlay patch for a base OBB.
     */
    public static final int OBB_OVERLAY = 1 << 0;

    /**
     * The canonical filename of the OBB.
     */
    public java.lang.String filename;

    /**
     * The name of the package to which the OBB file belongs.
     */
    public java.lang.String packageName;

    /**
     * The version of the package to which the OBB file belongs.
     */
    public int version;

    /**
     * The flags relating to the OBB.
     */
    public int flags;

    /**
     * The salt for the encryption algorithm.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public byte[] salt;

    // Only allow things in this package to instantiate.
    /* package */
    ObbInfo() {
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("ObbInfo{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" packageName=");
        sb.append(packageName);
        sb.append(",version=");
        sb.append(version);
        sb.append(",flags=");
        sb.append(flags);
        sb.append('}');
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        // Keep this in sync with writeToParcel() in ObbInfo.cpp
        dest.writeString(filename);
        dest.writeString(packageName);
        dest.writeInt(version);
        dest.writeInt(flags);
        dest.writeByteArray(salt);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.res.ObbInfo> CREATOR = new android.os.Parcelable.Creator<android.content.res.ObbInfo>() {
        public android.content.res.ObbInfo createFromParcel(android.os.Parcel source) {
            return new android.content.res.ObbInfo(source);
        }

        public android.content.res.ObbInfo[] newArray(int size) {
            return new android.content.res.ObbInfo[size];
        }
    };

    private ObbInfo(android.os.Parcel source) {
        filename = source.readString();
        packageName = source.readString();
        version = source.readInt();
        flags = source.readInt();
        salt = source.createByteArray();
    }
}

