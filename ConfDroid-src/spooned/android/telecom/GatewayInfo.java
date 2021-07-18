/**
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.telecom;


/**
 * Encapsulated gateway address information for outgoing call. When calls are made, the system
 * provides a facility to specify two addresses for the call: one to display as the address being
 * dialed and a separate (gateway) address to actually dial. Telecom provides this information to
 * {@link ConnectionService}s when placing the call as an instance of {@code GatewayInfo}.
 * <p>
 * The data consists of an address to call, an address to display and the package name of the
 * service. This data is used in two ways:
 * <ol>
 * <li> Call the appropriate gateway address.
 * <li> Display information about how the call is being routed to the user.
 * </ol>
 */
public class GatewayInfo implements android.os.Parcelable {
    private final java.lang.String mGatewayProviderPackageName;

    private final android.net.Uri mGatewayAddress;

    private final android.net.Uri mOriginalAddress;

    public GatewayInfo(java.lang.String packageName, android.net.Uri gatewayUri, android.net.Uri originalAddress) {
        mGatewayProviderPackageName = packageName;
        mGatewayAddress = gatewayUri;
        mOriginalAddress = originalAddress;
    }

    /**
     * Package name of the gateway provider service that provided the gateway information.
     * This can be used to identify the gateway address source and to load an appropriate icon when
     * displaying gateway information in the in-call UI.
     */
    public java.lang.String getGatewayProviderPackageName() {
        return mGatewayProviderPackageName;
    }

    /**
     * Returns the gateway address to dial when placing the call.
     */
    public android.net.Uri getGatewayAddress() {
        return mGatewayAddress;
    }

    /**
     * Returns the address that the user is trying to connect to via the gateway.
     */
    public android.net.Uri getOriginalAddress() {
        return mOriginalAddress;
    }

    /**
     * Indicates whether this {@code GatewayInfo} instance contains any data. A returned value of
     * false indicates that no gateway number is being used for the call.
     */
    public boolean isEmpty() {
        return android.text.TextUtils.isEmpty(mGatewayProviderPackageName) || (mGatewayAddress == null);
    }

    /**
     * The Parcelable interface.
     */
    public static final android.os.Parcelable.Creator<android.telecom.GatewayInfo> CREATOR = new android.os.Parcelable.Creator<android.telecom.GatewayInfo>() {
        @java.lang.Override
        public android.telecom.GatewayInfo createFromParcel(android.os.Parcel source) {
            java.lang.String gatewayPackageName = source.readString();
            android.net.Uri gatewayUri = android.net.Uri.CREATOR.createFromParcel(source);
            android.net.Uri originalAddress = android.net.Uri.CREATOR.createFromParcel(source);
            return new android.telecom.GatewayInfo(gatewayPackageName, gatewayUri, originalAddress);
        }

        @java.lang.Override
        public android.telecom.GatewayInfo[] newArray(int size) {
            return new android.telecom.GatewayInfo[size];
        }
    };

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeString(mGatewayProviderPackageName);
        mGatewayAddress.writeToParcel(destination, 0);
        mOriginalAddress.writeToParcel(destination, 0);
    }
}

