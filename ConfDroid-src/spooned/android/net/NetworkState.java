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
 * Snapshot of network state.
 *
 * @unknown 
 */
public class NetworkState implements android.os.Parcelable {
    public static final android.net.NetworkState EMPTY = new android.net.NetworkState(null, null, null, null, null, null);

    public final android.net.NetworkInfo networkInfo;

    public final android.net.LinkProperties linkProperties;

    public final android.net.NetworkCapabilities networkCapabilities;

    public final android.net.Network network;

    public final java.lang.String subscriberId;

    public final java.lang.String networkId;

    public NetworkState(android.net.NetworkInfo networkInfo, android.net.LinkProperties linkProperties, android.net.NetworkCapabilities networkCapabilities, android.net.Network network, java.lang.String subscriberId, java.lang.String networkId) {
        this.networkInfo = networkInfo;
        this.linkProperties = linkProperties;
        this.networkCapabilities = networkCapabilities;
        this.network = network;
        this.subscriberId = subscriberId;
        this.networkId = networkId;
    }

    public NetworkState(android.os.Parcel in) {
        networkInfo = in.readParcelable(null);
        linkProperties = in.readParcelable(null);
        networkCapabilities = in.readParcelable(null);
        network = in.readParcelable(null);
        subscriberId = in.readString();
        networkId = in.readString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(networkInfo, flags);
        out.writeParcelable(linkProperties, flags);
        out.writeParcelable(networkCapabilities, flags);
        out.writeParcelable(network, flags);
        out.writeString(subscriberId);
        out.writeString(networkId);
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkState> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkState>() {
        @java.lang.Override
        public android.net.NetworkState createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkState(in);
        }

        @java.lang.Override
        public android.net.NetworkState[] newArray(int size) {
            return new android.net.NetworkState[size];
        }
    };
}

