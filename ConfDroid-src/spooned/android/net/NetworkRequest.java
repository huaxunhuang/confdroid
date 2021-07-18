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
 * Defines a request for a network, made through {@link NetworkRequest.Builder} and used
 * to request a network via {@link ConnectivityManager#requestNetwork} or listen for changes
 * via {@link ConnectivityManager#registerNetworkCallback}.
 */
public class NetworkRequest implements android.os.Parcelable {
    /**
     * The {@link NetworkCapabilities} that define this request.
     *
     * @unknown 
     */
    public final android.net.NetworkCapabilities networkCapabilities;

    /**
     * Identifies the request.  NetworkRequests should only be constructed by
     * the Framework and given out to applications as tokens to be used to identify
     * the request.
     *
     * @unknown 
     */
    public final int requestId;

    /**
     * Set for legacy requests and the default.  Set to TYPE_NONE for none.
     * Causes CONNECTIVITY_ACTION broadcasts to be sent.
     *
     * @unknown 
     */
    public final int legacyType;

    /**
     * A NetworkRequest as used by the system can be one of the following types:
     *
     *     - LISTEN, for which the framework will issue callbacks about any
     *       and all networks that match the specified NetworkCapabilities,
     *
     *     - REQUEST, capable of causing a specific network to be created
     *       first (e.g. a telephony DUN request), the framework will issue
     *       callbacks about the single, highest scoring current network
     *       (if any) that matches the specified NetworkCapabilities, or
     *
     *     - TRACK_DEFAULT, a hybrid of the two designed such that the
     *       framework will issue callbacks for the single, highest scoring
     *       current network (if any) that matches the capabilities of the
     *       default Internet request (mDefaultRequest), but which cannot cause
     *       the framework to either create or retain the existence of any
     *       specific network. Note that from the point of view of the request
     *       matching code, TRACK_DEFAULT is identical to REQUEST: its special
     *       behaviour is not due to different semantics, but to the fact that
     *       the system will only ever create a TRACK_DEFAULT with capabilities
     *       that are identical to the default request's capabilities, thus
     *       causing it to share fate in every way with the default request.
     *
     *     - BACKGROUND_REQUEST, like REQUEST but does not cause any networks
     *       to retain the NET_CAPABILITY_FOREGROUND capability. A network with
     *       no foreground requests is in the background. A network that has
     *       one or more background requests and loses its last foreground
     *       request to a higher-scoring network will not go into the
     *       background immediately, but will linger and go into the background
     *       after the linger timeout.
     *
     *     - The value NONE is used only by applications. When an application
     *       creates a NetworkRequest, it does not have a type; the type is set
     *       by the system depending on the method used to file the request
     *       (requestNetwork, registerNetworkCallback, etc.).
     *
     * @unknown 
     */
    public static enum Type {

        NONE,
        LISTEN,
        TRACK_DEFAULT,
        REQUEST,
        BACKGROUND_REQUEST;}

    /**
     * The type of the request. This is only used by the system and is always NONE elsewhere.
     *
     * @unknown 
     */
    public final android.net.NetworkRequest.Type type;

    /**
     *
     *
     * @unknown 
     */
    public NetworkRequest(android.net.NetworkCapabilities nc, int legacyType, int rId, android.net.NetworkRequest.Type type) {
        if (nc == null) {
            throw new java.lang.NullPointerException();
        }
        requestId = rId;
        networkCapabilities = nc;
        this.legacyType = legacyType;
        this.type = type;
    }

    /**
     *
     *
     * @unknown 
     */
    public NetworkRequest(android.net.NetworkRequest that) {
        networkCapabilities = new android.net.NetworkCapabilities(that.networkCapabilities);
        requestId = that.requestId;
        this.legacyType = that.legacyType;
        this.type = that.type;
    }

    /**
     * Builder used to create {@link NetworkRequest} objects.  Specify the Network features
     * needed in terms of {@link NetworkCapabilities} features
     */
    public static class Builder {
        private final android.net.NetworkCapabilities mNetworkCapabilities = new android.net.NetworkCapabilities();

        /**
         * Default constructor for Builder.
         */
        public Builder() {
        }

        /**
         * Build {@link NetworkRequest} give the current set of capabilities.
         */
        public android.net.NetworkRequest build() {
            // Make a copy of mNetworkCapabilities so we don't inadvertently remove NOT_RESTRICTED
            // when later an unrestricted capability could be added to mNetworkCapabilities, in
            // which case NOT_RESTRICTED should be returned to mNetworkCapabilities, which
            // maybeMarkCapabilitiesRestricted() doesn't add back.
            final android.net.NetworkCapabilities nc = new android.net.NetworkCapabilities(mNetworkCapabilities);
            nc.maybeMarkCapabilitiesRestricted();
            return new android.net.NetworkRequest(nc, android.net.ConnectivityManager.TYPE_NONE, android.net.ConnectivityManager.REQUEST_ID_UNSET, android.net.NetworkRequest.Type.NONE);
        }

        /**
         * Add the given capability requirement to this builder.  These represent
         * the requested network's required capabilities.  Note that when searching
         * for a network to satisfy a request, all capabilities requested must be
         * satisfied.  See {@link NetworkCapabilities} for {@code NET_CAPABILITY_*}
         * definitions.
         *
         * @param capability
         * 		The {@code NetworkCapabilities.NET_CAPABILITY_*} to add.
         * @return The builder to facilitate chaining
        {@code builder.addCapability(...).addCapability();}.
         */
        public android.net.NetworkRequest.Builder addCapability(int capability) {
            mNetworkCapabilities.addCapability(capability);
            return this;
        }

        /**
         * Removes (if found) the given capability from this builder instance.
         *
         * @param capability
         * 		The {@code NetworkCapabilities.NET_CAPABILITY_*} to remove.
         * @return The builder to facilitate chaining.
         */
        public android.net.NetworkRequest.Builder removeCapability(int capability) {
            mNetworkCapabilities.removeCapability(capability);
            return this;
        }

        /**
         * Completely clears all the {@code NetworkCapabilities} from this builder instance,
         * removing even the capabilities that are set by default when the object is constructed.
         *
         * @return The builder to facilitate chaining.
         * @unknown 
         */
        public android.net.NetworkRequest.Builder clearCapabilities() {
            mNetworkCapabilities.clearAll();
            return this;
        }

        /**
         * Adds the given transport requirement to this builder.  These represent
         * the set of allowed transports for the request.  Only networks using one
         * of these transports will satisfy the request.  If no particular transports
         * are required, none should be specified here.  See {@link NetworkCapabilities}
         * for {@code TRANSPORT_*} definitions.
         *
         * @param transportType
         * 		The {@code NetworkCapabilities.TRANSPORT_*} to add.
         * @return The builder to facilitate chaining.
         */
        public android.net.NetworkRequest.Builder addTransportType(int transportType) {
            mNetworkCapabilities.addTransportType(transportType);
            return this;
        }

        /**
         * Removes (if found) the given transport from this builder instance.
         *
         * @param transportType
         * 		The {@code NetworkCapabilities.TRANSPORT_*} to remove.
         * @return The builder to facilitate chaining.
         */
        public android.net.NetworkRequest.Builder removeTransportType(int transportType) {
            mNetworkCapabilities.removeTransportType(transportType);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.net.NetworkRequest.Builder setLinkUpstreamBandwidthKbps(int upKbps) {
            mNetworkCapabilities.setLinkUpstreamBandwidthKbps(upKbps);
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.net.NetworkRequest.Builder setLinkDownstreamBandwidthKbps(int downKbps) {
            mNetworkCapabilities.setLinkDownstreamBandwidthKbps(downKbps);
            return this;
        }

        /**
         * Sets the optional bearer specific network specifier.
         * This has no meaning if a single transport is also not specified, so calling
         * this without a single transport set will generate an exception, as will
         * subsequently adding or removing transports after this is set.
         * </p>
         * The interpretation of this {@code String} is bearer specific and bearers that use
         * it should document their particulars.  For example, Bluetooth may use some sort of
         * device id while WiFi could used ssid and/or bssid.  Cellular may use carrier spn.
         *
         * @param networkSpecifier
         * 		An {@code String} of opaque format used to specify the bearer
         * 		specific network specifier where the bearer has a choice of
         * 		networks.
         */
        public android.net.NetworkRequest.Builder setNetworkSpecifier(java.lang.String networkSpecifier) {
            if (android.net.NetworkCapabilities.MATCH_ALL_REQUESTS_NETWORK_SPECIFIER.equals(networkSpecifier)) {
                throw new java.lang.IllegalArgumentException(("Invalid network specifier - must not be '" + android.net.NetworkCapabilities.MATCH_ALL_REQUESTS_NETWORK_SPECIFIER) + "'");
            }
            mNetworkCapabilities.setNetworkSpecifier(networkSpecifier);
            return this;
        }

        /**
         * Sets the signal strength. This is a signed integer, with higher values indicating a
         * stronger signal. The exact units are bearer-dependent. For example, Wi-Fi uses the same
         * RSSI units reported by WifiManager.
         * <p>
         * Note that when used to register a network callback, this specifies the minimum acceptable
         * signal strength. When received as the state of an existing network it specifies the
         * current value. A value of {@code SIGNAL_STRENGTH_UNSPECIFIED} means no value when
         * received and has no effect when requesting a callback.
         *
         * @param signalStrength
         * 		the bearer-specific signal strength.
         * @unknown 
         */
        public android.net.NetworkRequest.Builder setSignalStrength(int signalStrength) {
            mNetworkCapabilities.setSignalStrength(signalStrength);
            return this;
        }
    }

    // implement the Parcelable interface
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(networkCapabilities, flags);
        dest.writeInt(legacyType);
        dest.writeInt(requestId);
        dest.writeString(type.name());
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkRequest> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkRequest>() {
        public android.net.NetworkRequest createFromParcel(android.os.Parcel in) {
            android.net.NetworkCapabilities nc = ((android.net.NetworkCapabilities) (in.readParcelable(null)));
            int legacyType = in.readInt();
            int requestId = in.readInt();
            android.net.NetworkRequest.Type type = android.net.NetworkRequest.Type.valueOf(in.readString());// IllegalArgumentException if invalid.

            android.net.NetworkRequest result = new android.net.NetworkRequest(nc, legacyType, requestId, type);
            return result;
        }

        public android.net.NetworkRequest[] newArray(int size) {
            return new android.net.NetworkRequest[size];
        }
    };

    /**
     * Returns true iff. this NetworkRequest is of type LISTEN.
     *
     * @unknown 
     */
    public boolean isListen() {
        return type == android.net.NetworkRequest.Type.LISTEN;
    }

    /**
     * Returns true iff. the contained NetworkRequest is one that:
     *
     *     - should be associated with at most one satisfying network
     *       at a time;
     *
     *     - should cause a network to be kept up, but not necessarily in
     *       the foreground, if it is the best network which can satisfy the
     *       NetworkRequest.
     *
     * For full detail of how isRequest() is used for pairing Networks with
     * NetworkRequests read rematchNetworkAndRequests().
     *
     * @unknown 
     */
    public boolean isRequest() {
        return isForegroundRequest() || isBackgroundRequest();
    }

    /**
     * Returns true iff. the contained NetworkRequest is one that:
     *
     *     - should be associated with at most one satisfying network
     *       at a time;
     *
     *     - should cause a network to be kept up and in the foreground if
     *       it is the best network which can satisfy the NetworkRequest.
     *
     * For full detail of how isRequest() is used for pairing Networks with
     * NetworkRequests read rematchNetworkAndRequests().
     *
     * @unknown 
     */
    public boolean isForegroundRequest() {
        return (type == android.net.NetworkRequest.Type.TRACK_DEFAULT) || (type == android.net.NetworkRequest.Type.REQUEST);
    }

    /**
     * Returns true iff. this NetworkRequest is of type BACKGROUND_REQUEST.
     *
     * @unknown 
     */
    public boolean isBackgroundRequest() {
        return type == android.net.NetworkRequest.Type.BACKGROUND_REQUEST;
    }

    public java.lang.String toString() {
        return (((((("NetworkRequest [ " + type) + " id=") + requestId) + (legacyType != android.net.ConnectivityManager.TYPE_NONE ? ", legacyType=" + legacyType : "")) + ", ") + networkCapabilities.toString()) + " ]";
    }

    public boolean equals(java.lang.Object obj) {
        if ((obj instanceof android.net.NetworkRequest) == false)
            return false;

        android.net.NetworkRequest that = ((android.net.NetworkRequest) (obj));
        return (((that.legacyType == this.legacyType) && (that.requestId == this.requestId)) && (that.type == this.type)) && java.util.Objects.equals(that.networkCapabilities, this.networkCapabilities);
    }

    public int hashCode() {
        return java.util.Objects.hash(requestId, legacyType, networkCapabilities, type);
    }
}

