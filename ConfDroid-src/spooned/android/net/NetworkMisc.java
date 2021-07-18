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
package android.net;


/**
 * A grab-bag of information (metadata, policies, properties, etc) about a
 * {@link Network}. Since this contains PII, it should not be sent outside the
 * system.
 *
 * @unknown 
 */
public class NetworkMisc implements android.os.Parcelable {
    /**
     * If the {@link Network} is a VPN, whether apps are allowed to bypass the
     * VPN. This is set by a {@link VpnService} and used by
     * {@link ConnectivityManager} when creating a VPN.
     */
    public boolean allowBypass;

    /**
     * Set if the network was manually/explicitly connected to by the user either from settings
     * or a 3rd party app.  For example, turning on cell data is not explicit but tapping on a wifi
     * ap in the wifi settings to trigger a connection is explicit.  A 3rd party app asking to
     * connect to a particular access point is also explicit, though this may change in the future
     * as we want apps to use the multinetwork apis.
     */
    public boolean explicitlySelected;

    /**
     * Set if the user desires to use this network even if it is unvalidated. This field has meaning
     * only if {#link explicitlySelected} is true. If it is, this field must also be set to the
     * appropriate value based on previous user choice.
     */
    public boolean acceptUnvalidated;

    /**
     * Set to avoid surfacing the "Sign in to network" notification.
     * if carrier receivers/apps are registered to handle the carrier-specific provisioning
     * procedure, a carrier specific provisioning notification will be placed.
     * only one notification should be displayed. This field is set based on
     * which notification should be used for provisioning.
     */
    public boolean provisioningNotificationDisabled;

    /**
     * For mobile networks, this is the subscriber ID (such as IMSI).
     */
    public java.lang.String subscriberId;

    public NetworkMisc() {
    }

    public NetworkMisc(android.net.NetworkMisc nm) {
        if (nm != null) {
            allowBypass = nm.allowBypass;
            explicitlySelected = nm.explicitlySelected;
            acceptUnvalidated = nm.acceptUnvalidated;
            subscriberId = nm.subscriberId;
            provisioningNotificationDisabled = nm.provisioningNotificationDisabled;
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(allowBypass ? 1 : 0);
        out.writeInt(explicitlySelected ? 1 : 0);
        out.writeInt(acceptUnvalidated ? 1 : 0);
        out.writeString(subscriberId);
        out.writeInt(provisioningNotificationDisabled ? 1 : 0);
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkMisc> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkMisc>() {
        @java.lang.Override
        public android.net.NetworkMisc createFromParcel(android.os.Parcel in) {
            android.net.NetworkMisc networkMisc = new android.net.NetworkMisc();
            networkMisc.allowBypass = in.readInt() != 0;
            networkMisc.explicitlySelected = in.readInt() != 0;
            networkMisc.acceptUnvalidated = in.readInt() != 0;
            networkMisc.subscriberId = in.readString();
            networkMisc.provisioningNotificationDisabled = in.readInt() != 0;
            return networkMisc;
        }

        @java.lang.Override
        public android.net.NetworkMisc[] newArray(int size) {
            return new android.net.NetworkMisc[size];
        }
    };
}

