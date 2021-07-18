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
package android.net.wifi;


/**
 * This object describes a partial tree structure in the Hotspot 2.0 release 2 management object.
 * The object is used during subscription remediation to modify parts of an existing PPS MO
 * tree (Hotspot 2.0 specification section 9.1).
 *
 * @unknown 
 */
public class PasspointManagementObjectDefinition implements android.os.Parcelable {
    private final java.lang.String mBaseUri;

    private final java.lang.String mUrn;

    private final java.lang.String mMoTree;

    public PasspointManagementObjectDefinition(java.lang.String baseUri, java.lang.String urn, java.lang.String moTree) {
        mBaseUri = baseUri;
        mUrn = urn;
        mMoTree = moTree;
    }

    public java.lang.String getBaseUri() {
        return mBaseUri;
    }

    public java.lang.String getUrn() {
        return mUrn;
    }

    public java.lang.String getMoTree() {
        return mMoTree;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mBaseUri);
        dest.writeString(mUrn);
        dest.writeString(mMoTree);
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.PasspointManagementObjectDefinition> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.PasspointManagementObjectDefinition>() {
        public android.net.wifi.PasspointManagementObjectDefinition createFromParcel(android.os.Parcel in) {
            return /* base URI */
            /* URN */
            /* Tree XML */
            new android.net.wifi.PasspointManagementObjectDefinition(in.readString(), in.readString(), in.readString());
        }

        public android.net.wifi.PasspointManagementObjectDefinition[] newArray(int size) {
            return new android.net.wifi.PasspointManagementObjectDefinition[size];
        }
    };
}

