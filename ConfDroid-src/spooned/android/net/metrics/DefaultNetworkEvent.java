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
package android.net.metrics;


/**
 * An event recorded by ConnectivityService when there is a change in the default network.
 * {@hide }
 */
@android.annotation.SystemApi
public final class DefaultNetworkEvent implements android.os.Parcelable {
    // The ID of the network that has become the new default or NETID_UNSET if none.
    public final int netId;

    // The list of transport types of the new default network, for example TRANSPORT_WIFI, as
    // defined in NetworkCapabilities.java.
    public final int[] transportTypes;

    // The ID of the network that was the default before or NETID_UNSET if none.
    public final int prevNetId;

    // Whether the previous network had IPv4/IPv6 connectivity.
    public final boolean prevIPv4;

    public final boolean prevIPv6;

    /**
     * {@hide }
     */
    public DefaultNetworkEvent(int netId, int[] transportTypes, int prevNetId, boolean prevIPv4, boolean prevIPv6) {
        this.netId = netId;
        this.transportTypes = transportTypes;
        this.prevNetId = prevNetId;
        this.prevIPv4 = prevIPv4;
        this.prevIPv6 = prevIPv6;
    }

    private DefaultNetworkEvent(android.os.Parcel in) {
        this.netId = in.readInt();
        this.transportTypes = in.createIntArray();
        this.prevNetId = in.readInt();
        this.prevIPv4 = in.readByte() > 0;
        this.prevIPv6 = in.readByte() > 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(netId);
        out.writeIntArray(transportTypes);
        out.writeInt(prevNetId);
        out.writeByte(prevIPv4 ? ((byte) (1)) : ((byte) (0)));
        out.writeByte(prevIPv6 ? ((byte) (1)) : ((byte) (0)));
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String prevNetwork = java.lang.String.valueOf(prevNetId);
        java.lang.String newNetwork = java.lang.String.valueOf(netId);
        if (prevNetId != 0) {
            prevNetwork += ":" + ipSupport();
        }
        if (netId != 0) {
            newNetwork += ":" + android.net.NetworkCapabilities.transportNamesOf(transportTypes);
        }
        return java.lang.String.format("DefaultNetworkEvent(%s -> %s)", prevNetwork, newNetwork);
    }

    private java.lang.String ipSupport() {
        if (prevIPv4 && prevIPv6) {
            return "DUAL";
        }
        if (prevIPv6) {
            return "IPv6";
        }
        if (prevIPv4) {
            return "IPv4";
        }
        return "NONE";
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.DefaultNetworkEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.DefaultNetworkEvent>() {
        public android.net.metrics.DefaultNetworkEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.DefaultNetworkEvent(in);
        }

        public android.net.metrics.DefaultNetworkEvent[] newArray(int size) {
            return new android.net.metrics.DefaultNetworkEvent[size];
        }
    };

    public static void logEvent(int netId, int[] transports, int prevNetId, boolean hadIPv4, boolean hadIPv6) {
    }
}

