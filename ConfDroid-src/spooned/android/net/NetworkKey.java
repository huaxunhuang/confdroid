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
 * limitations under the License
 */
package android.net;


/**
 * Information which identifies a specific network.
 *
 * @unknown 
 */
// NOTE: Ideally, we would abstract away the details of what identifies a network of a specific
// type, so that all networks appear the same and can be scored without concern to the network type
// itself. However, because no such cross-type identifier currently exists in the Android framework,
// and because systems might obtain information about networks from sources other than Android
// devices, we need to provide identifying details about each specific network type (wifi, cell,
// etc.) so that clients can pull out these details depending on the type of network.
@android.annotation.SystemApi
public class NetworkKey implements android.os.Parcelable {
    /**
     * A wifi network, for which {@link #wifiKey} will be populated.
     */
    public static final int TYPE_WIFI = 1;

    /**
     * The type of this network.
     *
     * @see #TYPE_WIFI
     */
    public final int type;

    /**
     * Information identifying a Wi-Fi network. Only set when {@link #type} equals
     * {@link #TYPE_WIFI}.
     */
    public final android.net.WifiKey wifiKey;

    /**
     * Construct a new {@link NetworkKey} for a Wi-Fi network.
     *
     * @param wifiKey
     * 		the {@link WifiKey} identifying this Wi-Fi network.
     */
    public NetworkKey(android.net.WifiKey wifiKey) {
        this.type = android.net.NetworkKey.TYPE_WIFI;
        this.wifiKey = wifiKey;
    }

    private NetworkKey(android.os.Parcel in) {
        type = in.readInt();
        switch (type) {
            case android.net.NetworkKey.TYPE_WIFI :
                wifiKey = android.net.WifiKey.CREATOR.createFromParcel(in);
                break;
            default :
                throw new java.lang.IllegalArgumentException("Parcel has unknown type: " + type);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(type);
        switch (type) {
            case android.net.NetworkKey.TYPE_WIFI :
                wifiKey.writeToParcel(out, flags);
                break;
            default :
                throw new java.lang.IllegalStateException("NetworkKey has unknown type " + type);
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.net.NetworkKey that = ((android.net.NetworkKey) (o));
        return (type == that.type) && java.util.Objects.equals(wifiKey, that.wifiKey);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(type, wifiKey);
    }

    @java.lang.Override
    public java.lang.String toString() {
        switch (type) {
            case android.net.NetworkKey.TYPE_WIFI :
                return wifiKey.toString();
            default :
                // Don't throw an exception here in case someone is logging this object in a catch
                // block for debugging purposes.
                return "InvalidKey";
        }
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkKey> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkKey>() {
        @java.lang.Override
        public android.net.NetworkKey createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkKey(in);
        }

        @java.lang.Override
        public android.net.NetworkKey[] newArray(int size) {
            return new android.net.NetworkKey[size];
        }
    };
}

