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
 * This class represents the capabilities of a network.  This is used both to specify
 * needs to {@link ConnectivityManager} and when inspecting a network.
 *
 * Note that this replaces the old {@link ConnectivityManager#TYPE_MOBILE} method
 * of network selection.  Rather than indicate a need for Wi-Fi because an application
 * needs high bandwidth and risk obsolescence when a new, fast network appears (like LTE),
 * the application should specify it needs high bandwidth.  Similarly if an application
 * needs an unmetered network for a bulk transfer it can specify that rather than assuming
 * all cellular based connections are metered and all Wi-Fi based connections are not.
 */
public final class NetworkCapabilities implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public NetworkCapabilities() {
        clearAll();
        mNetworkCapabilities = android.net.NetworkCapabilities.DEFAULT_CAPABILITIES;
    }

    public NetworkCapabilities(android.net.NetworkCapabilities nc) {
        if (nc != null) {
            mNetworkCapabilities = nc.mNetworkCapabilities;
            mTransportTypes = nc.mTransportTypes;
            mLinkUpBandwidthKbps = nc.mLinkUpBandwidthKbps;
            mLinkDownBandwidthKbps = nc.mLinkDownBandwidthKbps;
            mNetworkSpecifier = nc.mNetworkSpecifier;
            mSignalStrength = nc.mSignalStrength;
        }
    }

    /**
     * Completely clears the contents of this object, removing even the capabilities that are set
     * by default when the object is constructed.
     *
     * @unknown 
     */
    public void clearAll() {
        mNetworkCapabilities = mTransportTypes = 0;
        mLinkUpBandwidthKbps = mLinkDownBandwidthKbps = 0;
        mNetworkSpecifier = null;
        mSignalStrength = android.net.NetworkCapabilities.SIGNAL_STRENGTH_UNSPECIFIED;
    }

    /**
     * Represents the network's capabilities.  If any are specified they will be satisfied
     * by any Network that matches all of them.
     */
    private long mNetworkCapabilities;

    /**
     * Indicates this is a network that has the ability to reach the
     * carrier's MMSC for sending and receiving MMS messages.
     */
    public static final int NET_CAPABILITY_MMS = 0;

    /**
     * Indicates this is a network that has the ability to reach the carrier's
     * SUPL server, used to retrieve GPS information.
     */
    public static final int NET_CAPABILITY_SUPL = 1;

    /**
     * Indicates this is a network that has the ability to reach the carrier's
     * DUN or tethering gateway.
     */
    public static final int NET_CAPABILITY_DUN = 2;

    /**
     * Indicates this is a network that has the ability to reach the carrier's
     * FOTA portal, used for over the air updates.
     */
    public static final int NET_CAPABILITY_FOTA = 3;

    /**
     * Indicates this is a network that has the ability to reach the carrier's
     * IMS servers, used for network registration and signaling.
     */
    public static final int NET_CAPABILITY_IMS = 4;

    /**
     * Indicates this is a network that has the ability to reach the carrier's
     * CBS servers, used for carrier specific services.
     */
    public static final int NET_CAPABILITY_CBS = 5;

    /**
     * Indicates this is a network that has the ability to reach a Wi-Fi direct
     * peer.
     */
    public static final int NET_CAPABILITY_WIFI_P2P = 6;

    /**
     * Indicates this is a network that has the ability to reach a carrier's
     * Initial Attach servers.
     */
    public static final int NET_CAPABILITY_IA = 7;

    /**
     * Indicates this is a network that has the ability to reach a carrier's
     * RCS servers, used for Rich Communication Services.
     */
    public static final int NET_CAPABILITY_RCS = 8;

    /**
     * Indicates this is a network that has the ability to reach a carrier's
     * XCAP servers, used for configuration and control.
     */
    public static final int NET_CAPABILITY_XCAP = 9;

    /**
     * Indicates this is a network that has the ability to reach a carrier's
     * Emergency IMS servers or other services, used for network signaling
     * during emergency calls.
     */
    public static final int NET_CAPABILITY_EIMS = 10;

    /**
     * Indicates that this network is unmetered.
     */
    public static final int NET_CAPABILITY_NOT_METERED = 11;

    /**
     * Indicates that this network should be able to reach the internet.
     */
    public static final int NET_CAPABILITY_INTERNET = 12;

    /**
     * Indicates that this network is available for general use.  If this is not set
     * applications should not attempt to communicate on this network.  Note that this
     * is simply informative and not enforcement - enforcement is handled via other means.
     * Set by default.
     */
    public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;

    /**
     * Indicates that the user has indicated implicit trust of this network.  This
     * generally means it's a sim-selected carrier, a plugged in ethernet, a paired
     * BT device or a wifi the user asked to connect to.  Untrusted networks
     * are probably limited to unknown wifi AP.  Set by default.
     */
    public static final int NET_CAPABILITY_TRUSTED = 14;

    /**
     * Indicates that this network is not a VPN.  This capability is set by default and should be
     * explicitly cleared for VPN networks.
     */
    public static final int NET_CAPABILITY_NOT_VPN = 15;

    /**
     * Indicates that connectivity on this network was successfully validated. For example, for a
     * network with NET_CAPABILITY_INTERNET, it means that Internet connectivity was successfully
     * detected.
     */
    public static final int NET_CAPABILITY_VALIDATED = 16;

    /**
     * Indicates that this network was found to have a captive portal in place last time it was
     * probed.
     */
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;

    /**
     * Indicates that this network is available for use by apps, and not a network that is being
     * kept up in the background to facilitate fast network switching.
     *
     * @unknown 
     */
    public static final int NET_CAPABILITY_FOREGROUND = 18;

    private static final int MIN_NET_CAPABILITY = android.net.NetworkCapabilities.NET_CAPABILITY_MMS;

    private static final int MAX_NET_CAPABILITY = android.net.NetworkCapabilities.NET_CAPABILITY_FOREGROUND;

    /**
     * Network capabilities that are expected to be mutable, i.e., can change while a particular
     * network is connected.
     */
    // TRUSTED can change when user explicitly connects to an untrusted network in Settings.
    // http://b/18206275
    private static final long MUTABLE_CAPABILITIES = (((1 << android.net.NetworkCapabilities.NET_CAPABILITY_TRUSTED) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_FOREGROUND);

    /**
     * Network specifier for factories which want to match any network specifier
     * (NS) in a request. Behavior:
     * <li>Empty NS in request matches any network factory NS</li>
     * <li>Empty NS in the network factory NS only matches a request with an
     * empty NS</li>
     * <li>"*" (this constant) NS in the network factory matches requests with
     * any NS</li>
     *
     * @unknown 
     */
    public static final java.lang.String MATCH_ALL_REQUESTS_NETWORK_SPECIFIER = "*";

    /**
     * Network capabilities that are not allowed in NetworkRequests. This exists because the
     * NetworkFactory / NetworkAgent model does not deal well with the situation where a
     * capability's presence cannot be known in advance. If such a capability is requested, then we
     * can get into a cycle where the NetworkFactory endlessly churns out NetworkAgents that then
     * get immediately torn down because they do not have the requested capability.
     */
    private static final long NON_REQUESTABLE_CAPABILITIES = android.net.NetworkCapabilities.MUTABLE_CAPABILITIES & (~(1 << android.net.NetworkCapabilities.NET_CAPABILITY_TRUSTED));

    /**
     * Capabilities that are set by default when the object is constructed.
     */
    private static final long DEFAULT_CAPABILITIES = ((1 << android.net.NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_TRUSTED)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_NOT_VPN);

    /**
     * Capabilities that suggest that a network is restricted.
     * {@see #maybeMarkCapabilitiesRestricted}.
     */
    private static final long RESTRICTED_CAPABILITIES = (((((((1 << android.net.NetworkCapabilities.NET_CAPABILITY_CBS) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_DUN)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_EIMS)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_FOTA)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_IA)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_IMS)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_RCS)) | (1 << android.net.NetworkCapabilities.NET_CAPABILITY_XCAP);

    /**
     * Adds the given capability to this {@code NetworkCapability} instance.
     * Multiple capabilities may be applied sequentially.  Note that when searching
     * for a network to satisfy a request, all capabilities requested must be satisfied.
     *
     * @param capability
     * 		the {@code NetworkCapabilities.NET_CAPABILITY_*} to be added.
     * @return This NetworkCapabilities instance, to facilitate chaining.
     * @unknown 
     */
    public android.net.NetworkCapabilities addCapability(int capability) {
        if ((capability < android.net.NetworkCapabilities.MIN_NET_CAPABILITY) || (capability > android.net.NetworkCapabilities.MAX_NET_CAPABILITY)) {
            throw new java.lang.IllegalArgumentException("NetworkCapability out of range");
        }
        mNetworkCapabilities |= 1 << capability;
        return this;
    }

    /**
     * Removes (if found) the given capability from this {@code NetworkCapability} instance.
     *
     * @param capability
     * 		the {@code NetworkCapabilities.NET_CAPABILTIY_*} to be removed.
     * @return This NetworkCapabilities instance, to facilitate chaining.
     * @unknown 
     */
    public android.net.NetworkCapabilities removeCapability(int capability) {
        if ((capability < android.net.NetworkCapabilities.MIN_NET_CAPABILITY) || (capability > android.net.NetworkCapabilities.MAX_NET_CAPABILITY)) {
            throw new java.lang.IllegalArgumentException("NetworkCapability out of range");
        }
        mNetworkCapabilities &= ~(1 << capability);
        return this;
    }

    /**
     * Gets all the capabilities set on this {@code NetworkCapability} instance.
     *
     * @return an array of {@code NetworkCapabilities.NET_CAPABILITY_*} values
    for this instance.
     * @unknown 
     */
    public int[] getCapabilities() {
        return enumerateBits(mNetworkCapabilities);
    }

    /**
     * Tests for the presence of a capabilitity on this instance.
     *
     * @param capability
     * 		the {@code NetworkCapabilities.NET_CAPABILITY_*} to be tested for.
     * @return {@code true} if set on this instance.
     */
    public boolean hasCapability(int capability) {
        if ((capability < android.net.NetworkCapabilities.MIN_NET_CAPABILITY) || (capability > android.net.NetworkCapabilities.MAX_NET_CAPABILITY)) {
            return false;
        }
        return (mNetworkCapabilities & (1 << capability)) != 0;
    }

    private int[] enumerateBits(long val) {
        int size = java.lang.Long.bitCount(val);
        int[] result = new int[size];
        int index = 0;
        int resource = 0;
        while (val > 0) {
            if ((val & 1) == 1)
                result[index++] = resource;

            val = val >> 1;
            resource++;
        } 
        return result;
    }

    private void combineNetCapabilities(android.net.NetworkCapabilities nc) {
        this.mNetworkCapabilities |= nc.mNetworkCapabilities;
    }

    /**
     * Convenience function that returns a human-readable description of the first mutable
     * capability we find. Used to present an error message to apps that request mutable
     * capabilities.
     *
     * @unknown 
     */
    public java.lang.String describeFirstNonRequestableCapability() {
        if (hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED))
            return "NET_CAPABILITY_VALIDATED";

        if (hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL))
            return "NET_CAPABILITY_CAPTIVE_PORTAL";

        if (hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_FOREGROUND))
            return "NET_CAPABILITY_FOREGROUND";

        // This cannot happen unless the preceding checks are incomplete.
        if ((mNetworkCapabilities & android.net.NetworkCapabilities.NON_REQUESTABLE_CAPABILITIES) != 0) {
            return "unknown non-requestable capabilities " + java.lang.Long.toHexString(mNetworkCapabilities);
        }
        if ((mLinkUpBandwidthKbps != 0) || (mLinkDownBandwidthKbps != 0))
            return "link bandwidth";

        if (hasSignalStrength())
            return "signalStrength";

        return null;
    }

    private boolean satisfiedByNetCapabilities(android.net.NetworkCapabilities nc, boolean onlyImmutable) {
        long networkCapabilities = this.mNetworkCapabilities;
        if (onlyImmutable) {
            networkCapabilities = networkCapabilities & (~android.net.NetworkCapabilities.MUTABLE_CAPABILITIES);
        }
        return (nc.mNetworkCapabilities & networkCapabilities) == networkCapabilities;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean equalsNetCapabilities(android.net.NetworkCapabilities nc) {
        return nc.mNetworkCapabilities == this.mNetworkCapabilities;
    }

    private boolean equalsNetCapabilitiesImmutable(android.net.NetworkCapabilities that) {
        return (this.mNetworkCapabilities & (~android.net.NetworkCapabilities.MUTABLE_CAPABILITIES)) == (that.mNetworkCapabilities & (~android.net.NetworkCapabilities.MUTABLE_CAPABILITIES));
    }

    private boolean equalsNetCapabilitiesRequestable(android.net.NetworkCapabilities that) {
        return (this.mNetworkCapabilities & (~android.net.NetworkCapabilities.NON_REQUESTABLE_CAPABILITIES)) == (that.mNetworkCapabilities & (~android.net.NetworkCapabilities.NON_REQUESTABLE_CAPABILITIES));
    }

    /**
     * Removes the NET_CAPABILITY_NOT_RESTRICTED capability if all the capabilities it provides are
     * typically provided by restricted networks.
     *
     * TODO: consider:
     * - Renaming it to guessRestrictedCapability and make it set the
     *   restricted capability bit in addition to clearing it.
     *
     * @unknown 
     */
    public void maybeMarkCapabilitiesRestricted() {
        // If all the capabilities are typically provided by restricted networks, conclude that this
        // network is restricted.
        if (((mNetworkCapabilities & (~(android.net.NetworkCapabilities.DEFAULT_CAPABILITIES | android.net.NetworkCapabilities.RESTRICTED_CAPABILITIES))) == 0) && // Must have at least some restricted capabilities, otherwise a request for an
        // internet-less network will get marked restricted.
        ((mNetworkCapabilities & android.net.NetworkCapabilities.RESTRICTED_CAPABILITIES) != 0)) {
            removeCapability(android.net.NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
        }
    }

    /**
     * Representing the transport type.  Apps should generally not care about transport.  A
     * request for a fast internet connection could be satisfied by a number of different
     * transports.  If any are specified here it will be satisfied a Network that matches
     * any of them.  If a caller doesn't care about the transport it should not specify any.
     */
    private long mTransportTypes;

    /**
     * Indicates this network uses a Cellular transport.
     */
    public static final int TRANSPORT_CELLULAR = 0;

    /**
     * Indicates this network uses a Wi-Fi transport.
     */
    public static final int TRANSPORT_WIFI = 1;

    /**
     * Indicates this network uses a Bluetooth transport.
     */
    public static final int TRANSPORT_BLUETOOTH = 2;

    /**
     * Indicates this network uses an Ethernet transport.
     */
    public static final int TRANSPORT_ETHERNET = 3;

    /**
     * Indicates this network uses a VPN transport.
     */
    public static final int TRANSPORT_VPN = 4;

    private static final int MIN_TRANSPORT = android.net.NetworkCapabilities.TRANSPORT_CELLULAR;

    private static final int MAX_TRANSPORT = android.net.NetworkCapabilities.TRANSPORT_VPN;

    /**
     * Adds the given transport type to this {@code NetworkCapability} instance.
     * Multiple transports may be applied sequentially.  Note that when searching
     * for a network to satisfy a request, any listed in the request will satisfy the request.
     * For example {@code TRANSPORT_WIFI} and {@code TRANSPORT_ETHERNET} added to a
     * {@code NetworkCapabilities} would cause either a Wi-Fi network or an Ethernet network
     * to be selected.  This is logically different than
     * {@code NetworkCapabilities.NET_CAPABILITY_*} listed above.
     *
     * @param transportType
     * 		the {@code NetworkCapabilities.TRANSPORT_*} to be added.
     * @return This NetworkCapabilities instance, to facilitate chaining.
     * @unknown 
     */
    public android.net.NetworkCapabilities addTransportType(int transportType) {
        if ((transportType < android.net.NetworkCapabilities.MIN_TRANSPORT) || (transportType > android.net.NetworkCapabilities.MAX_TRANSPORT)) {
            throw new java.lang.IllegalArgumentException("TransportType out of range");
        }
        mTransportTypes |= 1 << transportType;
        setNetworkSpecifier(mNetworkSpecifier);// used for exception checking

        return this;
    }

    /**
     * Removes (if found) the given transport from this {@code NetworkCapability} instance.
     *
     * @param transportType
     * 		the {@code NetworkCapabilities.TRANSPORT_*} to be removed.
     * @return This NetworkCapabilities instance, to facilitate chaining.
     * @unknown 
     */
    public android.net.NetworkCapabilities removeTransportType(int transportType) {
        if ((transportType < android.net.NetworkCapabilities.MIN_TRANSPORT) || (transportType > android.net.NetworkCapabilities.MAX_TRANSPORT)) {
            throw new java.lang.IllegalArgumentException("TransportType out of range");
        }
        mTransportTypes &= ~(1 << transportType);
        setNetworkSpecifier(mNetworkSpecifier);// used for exception checking

        return this;
    }

    /**
     * Gets all the transports set on this {@code NetworkCapability} instance.
     *
     * @return an array of {@code NetworkCapabilities.TRANSPORT_*} values
    for this instance.
     * @unknown 
     */
    public int[] getTransportTypes() {
        return enumerateBits(mTransportTypes);
    }

    /**
     * Tests for the presence of a transport on this instance.
     *
     * @param transportType
     * 		the {@code NetworkCapabilities.TRANSPORT_*} to be tested for.
     * @return {@code true} if set on this instance.
     */
    public boolean hasTransport(int transportType) {
        if ((transportType < android.net.NetworkCapabilities.MIN_TRANSPORT) || (transportType > android.net.NetworkCapabilities.MAX_TRANSPORT)) {
            return false;
        }
        return (mTransportTypes & (1 << transportType)) != 0;
    }

    private void combineTransportTypes(android.net.NetworkCapabilities nc) {
        this.mTransportTypes |= nc.mTransportTypes;
    }

    private boolean satisfiedByTransportTypes(android.net.NetworkCapabilities nc) {
        return (this.mTransportTypes == 0) || ((this.mTransportTypes & nc.mTransportTypes) != 0);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean equalsTransportTypes(android.net.NetworkCapabilities nc) {
        return nc.mTransportTypes == this.mTransportTypes;
    }

    /**
     * Passive link bandwidth.  This is a rough guide of the expected peak bandwidth
     * for the first hop on the given transport.  It is not measured, but may take into account
     * link parameters (Radio technology, allocated channels, etc).
     */
    private int mLinkUpBandwidthKbps;

    private int mLinkDownBandwidthKbps;

    /**
     * Sets the upstream bandwidth for this network in Kbps.  This always only refers to
     * the estimated first hop transport bandwidth.
     * <p>
     * Note that when used to request a network, this specifies the minimum acceptable.
     * When received as the state of an existing network this specifies the typical
     * first hop bandwidth expected.  This is never measured, but rather is inferred
     * from technology type and other link parameters.  It could be used to differentiate
     * between very slow 1xRTT cellular links and other faster networks or even between
     * 802.11b vs 802.11AC wifi technologies.  It should not be used to differentiate between
     * fast backhauls and slow backhauls.
     *
     * @param upKbps
     * 		the estimated first hop upstream (device to network) bandwidth.
     * @unknown 
     */
    public void setLinkUpstreamBandwidthKbps(int upKbps) {
        mLinkUpBandwidthKbps = upKbps;
    }

    /**
     * Retrieves the upstream bandwidth for this network in Kbps.  This always only refers to
     * the estimated first hop transport bandwidth.
     *
     * @return The estimated first hop upstream (device to network) bandwidth.
     */
    public int getLinkUpstreamBandwidthKbps() {
        return mLinkUpBandwidthKbps;
    }

    /**
     * Sets the downstream bandwidth for this network in Kbps.  This always only refers to
     * the estimated first hop transport bandwidth.
     * <p>
     * Note that when used to request a network, this specifies the minimum acceptable.
     * When received as the state of an existing network this specifies the typical
     * first hop bandwidth expected.  This is never measured, but rather is inferred
     * from technology type and other link parameters.  It could be used to differentiate
     * between very slow 1xRTT cellular links and other faster networks or even between
     * 802.11b vs 802.11AC wifi technologies.  It should not be used to differentiate between
     * fast backhauls and slow backhauls.
     *
     * @param downKbps
     * 		the estimated first hop downstream (network to device) bandwidth.
     * @unknown 
     */
    public void setLinkDownstreamBandwidthKbps(int downKbps) {
        mLinkDownBandwidthKbps = downKbps;
    }

    /**
     * Retrieves the downstream bandwidth for this network in Kbps.  This always only refers to
     * the estimated first hop transport bandwidth.
     *
     * @return The estimated first hop downstream (network to device) bandwidth.
     */
    public int getLinkDownstreamBandwidthKbps() {
        return mLinkDownBandwidthKbps;
    }

    private void combineLinkBandwidths(android.net.NetworkCapabilities nc) {
        this.mLinkUpBandwidthKbps = java.lang.Math.max(this.mLinkUpBandwidthKbps, nc.mLinkUpBandwidthKbps);
        this.mLinkDownBandwidthKbps = java.lang.Math.max(this.mLinkDownBandwidthKbps, nc.mLinkDownBandwidthKbps);
    }

    private boolean satisfiedByLinkBandwidths(android.net.NetworkCapabilities nc) {
        return !((this.mLinkUpBandwidthKbps > nc.mLinkUpBandwidthKbps) || (this.mLinkDownBandwidthKbps > nc.mLinkDownBandwidthKbps));
    }

    private boolean equalsLinkBandwidths(android.net.NetworkCapabilities nc) {
        return (this.mLinkUpBandwidthKbps == nc.mLinkUpBandwidthKbps) && (this.mLinkDownBandwidthKbps == nc.mLinkDownBandwidthKbps);
    }

    private java.lang.String mNetworkSpecifier;

    /**
     * Sets the optional bearer specific network specifier.
     * This has no meaning if a single transport is also not specified, so calling
     * this without a single transport set will generate an exception, as will
     * subsequently adding or removing transports after this is set.
     * </p>
     * The interpretation of this {@code String} is bearer specific and bearers that use
     * it should document their particulars.  For example, Bluetooth may use some sort of
     * device id while WiFi could used SSID and/or BSSID.  Cellular may use carrier SPN (name)
     * or Subscription ID.
     *
     * @param networkSpecifier
     * 		An {@code String} of opaque format used to specify the bearer
     * 		specific network specifier where the bearer has a choice of
     * 		networks.
     * @return This NetworkCapabilities instance, to facilitate chaining.
     * @unknown 
     */
    public android.net.NetworkCapabilities setNetworkSpecifier(java.lang.String networkSpecifier) {
        if ((android.text.TextUtils.isEmpty(networkSpecifier) == false) && (java.lang.Long.bitCount(mTransportTypes) != 1)) {
            throw new java.lang.IllegalStateException("Must have a single transport specified to use " + "setNetworkSpecifier");
        }
        mNetworkSpecifier = networkSpecifier;
        return this;
    }

    /**
     * Gets the optional bearer specific network specifier.
     *
     * @return The optional {@code String} specifying the bearer specific network specifier.
    See {@link #setNetworkSpecifier}.
     * @unknown 
     */
    public java.lang.String getNetworkSpecifier() {
        return mNetworkSpecifier;
    }

    private void combineSpecifiers(android.net.NetworkCapabilities nc) {
        java.lang.String otherSpecifier = nc.getNetworkSpecifier();
        if (android.text.TextUtils.isEmpty(otherSpecifier))
            return;

        if (android.text.TextUtils.isEmpty(mNetworkSpecifier) == false) {
            throw new java.lang.IllegalStateException("Can't combine two networkSpecifiers");
        }
        setNetworkSpecifier(otherSpecifier);
    }

    private boolean satisfiedBySpecifier(android.net.NetworkCapabilities nc) {
        return (android.text.TextUtils.isEmpty(mNetworkSpecifier) || mNetworkSpecifier.equals(nc.mNetworkSpecifier)) || android.net.NetworkCapabilities.MATCH_ALL_REQUESTS_NETWORK_SPECIFIER.equals(nc.mNetworkSpecifier);
    }

    private boolean equalsSpecifier(android.net.NetworkCapabilities nc) {
        if (android.text.TextUtils.isEmpty(mNetworkSpecifier)) {
            return android.text.TextUtils.isEmpty(nc.mNetworkSpecifier);
        } else {
            return mNetworkSpecifier.equals(nc.mNetworkSpecifier);
        }
    }

    /**
     * Magic value that indicates no signal strength provided. A request specifying this value is
     * always satisfied.
     *
     * @unknown 
     */
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = java.lang.Integer.MIN_VALUE;

    /**
     * Signal strength. This is a signed integer, and higher values indicate better signal.
     * The exact units are bearer-dependent. For example, Wi-Fi uses RSSI.
     */
    private int mSignalStrength;

    /**
     * Sets the signal strength. This is a signed integer, with higher values indicating a stronger
     * signal. The exact units are bearer-dependent. For example, Wi-Fi uses the same RSSI units
     * reported by WifiManager.
     * <p>
     * Note that when used to register a network callback, this specifies the minimum acceptable
     * signal strength. When received as the state of an existing network it specifies the current
     * value. A value of code SIGNAL_STRENGTH_UNSPECIFIED} means no value when received and has no
     * effect when requesting a callback.
     *
     * @param signalStrength
     * 		the bearer-specific signal strength.
     * @unknown 
     */
    public void setSignalStrength(int signalStrength) {
        mSignalStrength = signalStrength;
    }

    /**
     * Returns {@code true} if this object specifies a signal strength.
     *
     * @unknown 
     */
    public boolean hasSignalStrength() {
        return mSignalStrength > android.net.NetworkCapabilities.SIGNAL_STRENGTH_UNSPECIFIED;
    }

    /**
     * Retrieves the signal strength.
     *
     * @return The bearer-specific signal strength.
     * @unknown 
     */
    public int getSignalStrength() {
        return mSignalStrength;
    }

    private void combineSignalStrength(android.net.NetworkCapabilities nc) {
        this.mSignalStrength = java.lang.Math.max(this.mSignalStrength, nc.mSignalStrength);
    }

    private boolean satisfiedBySignalStrength(android.net.NetworkCapabilities nc) {
        return this.mSignalStrength <= nc.mSignalStrength;
    }

    private boolean equalsSignalStrength(android.net.NetworkCapabilities nc) {
        return this.mSignalStrength == nc.mSignalStrength;
    }

    /**
     * Combine a set of Capabilities to this one.  Useful for coming up with the complete set
     *
     * @unknown 
     */
    public void combineCapabilities(android.net.NetworkCapabilities nc) {
        combineNetCapabilities(nc);
        combineTransportTypes(nc);
        combineLinkBandwidths(nc);
        combineSpecifiers(nc);
        combineSignalStrength(nc);
    }

    /**
     * Check if our requirements are satisfied by the given {@code NetworkCapabilities}.
     *
     * @param nc
     * 		the {@code NetworkCapabilities} that may or may not satisfy our requirements.
     * @param onlyImmutable
     * 		if {@code true}, do not consider mutable requirements such as link
     * 		bandwidth, signal strength, or validation / captive portal status.
     * @unknown 
     */
    private boolean satisfiedByNetworkCapabilities(android.net.NetworkCapabilities nc, boolean onlyImmutable) {
        return (((((nc != null) && satisfiedByNetCapabilities(nc, onlyImmutable)) && satisfiedByTransportTypes(nc)) && (onlyImmutable || satisfiedByLinkBandwidths(nc))) && satisfiedBySpecifier(nc)) && (onlyImmutable || satisfiedBySignalStrength(nc));
    }

    /**
     * Check if our requirements are satisfied by the given {@code NetworkCapabilities}.
     *
     * @param nc
     * 		the {@code NetworkCapabilities} that may or may not satisfy our requirements.
     * @unknown 
     */
    public boolean satisfiedByNetworkCapabilities(android.net.NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, false);
    }

    /**
     * Check if our immutable requirements are satisfied by the given {@code NetworkCapabilities}.
     *
     * @param nc
     * 		the {@code NetworkCapabilities} that may or may not satisfy our requirements.
     * @unknown 
     */
    public boolean satisfiedByImmutableNetworkCapabilities(android.net.NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, true);
    }

    /**
     * Checks that our immutable capabilities are the same as those of the given
     * {@code NetworkCapabilities}.
     *
     * @unknown 
     */
    public boolean equalImmutableCapabilities(android.net.NetworkCapabilities nc) {
        if (nc == null)
            return false;

        return (equalsNetCapabilitiesImmutable(nc) && equalsTransportTypes(nc)) && equalsSpecifier(nc);
    }

    /**
     * Checks that our requestable capabilities are the same as those of the given
     * {@code NetworkCapabilities}.
     *
     * @unknown 
     */
    public boolean equalRequestableCapabilities(android.net.NetworkCapabilities nc) {
        if (nc == null)
            return false;

        return (equalsNetCapabilitiesRequestable(nc) && equalsTransportTypes(nc)) && equalsSpecifier(nc);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if ((obj == null) || ((obj instanceof android.net.NetworkCapabilities) == false))
            return false;

        android.net.NetworkCapabilities that = ((android.net.NetworkCapabilities) (obj));
        return (((equalsNetCapabilities(that) && equalsTransportTypes(that)) && equalsLinkBandwidths(that)) && equalsSignalStrength(that)) && equalsSpecifier(that);
    }

    @java.lang.Override
    public int hashCode() {
        return ((((((((int) (mNetworkCapabilities & 0xffffffff)) + (((int) (mNetworkCapabilities >> 32)) * 3)) + (((int) (mTransportTypes & 0xffffffff)) * 5)) + (((int) (mTransportTypes >> 32)) * 7)) + (mLinkUpBandwidthKbps * 11)) + (mLinkDownBandwidthKbps * 13)) + (android.text.TextUtils.isEmpty(mNetworkSpecifier) ? 0 : mNetworkSpecifier.hashCode() * 17)) + (mSignalStrength * 19);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(mNetworkCapabilities);
        dest.writeLong(mTransportTypes);
        dest.writeInt(mLinkUpBandwidthKbps);
        dest.writeInt(mLinkDownBandwidthKbps);
        dest.writeString(mNetworkSpecifier);
        dest.writeInt(mSignalStrength);
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkCapabilities> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkCapabilities>() {
        @java.lang.Override
        public android.net.NetworkCapabilities createFromParcel(android.os.Parcel in) {
            android.net.NetworkCapabilities netCap = new android.net.NetworkCapabilities();
            netCap.mNetworkCapabilities = in.readLong();
            netCap.mTransportTypes = in.readLong();
            netCap.mLinkUpBandwidthKbps = in.readInt();
            netCap.mLinkDownBandwidthKbps = in.readInt();
            netCap.mNetworkSpecifier = in.readString();
            netCap.mSignalStrength = in.readInt();
            return netCap;
        }

        @java.lang.Override
        public android.net.NetworkCapabilities[] newArray(int size) {
            return new android.net.NetworkCapabilities[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        int[] types = getTransportTypes();
        java.lang.String transports = (types.length > 0) ? " Transports: " + android.net.NetworkCapabilities.transportNamesOf(types) : "";
        types = getCapabilities();
        java.lang.String capabilities = (types.length > 0) ? " Capabilities: " : "";
        for (int i = 0; i < types.length;) {
            switch (types[i]) {
                case android.net.NetworkCapabilities.NET_CAPABILITY_MMS :
                    capabilities += "MMS";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_SUPL :
                    capabilities += "SUPL";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_DUN :
                    capabilities += "DUN";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_FOTA :
                    capabilities += "FOTA";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_IMS :
                    capabilities += "IMS";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_CBS :
                    capabilities += "CBS";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_WIFI_P2P :
                    capabilities += "WIFI_P2P";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_IA :
                    capabilities += "IA";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_RCS :
                    capabilities += "RCS";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_XCAP :
                    capabilities += "XCAP";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_EIMS :
                    capabilities += "EIMS";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED :
                    capabilities += "NOT_METERED";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET :
                    capabilities += "INTERNET";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED :
                    capabilities += "NOT_RESTRICTED";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_TRUSTED :
                    capabilities += "TRUSTED";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_NOT_VPN :
                    capabilities += "NOT_VPN";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED :
                    capabilities += "VALIDATED";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL :
                    capabilities += "CAPTIVE_PORTAL";
                    break;
                case android.net.NetworkCapabilities.NET_CAPABILITY_FOREGROUND :
                    capabilities += "FOREGROUND";
                    break;
            }
            if ((++i) < types.length)
                capabilities += "&";

        }
        java.lang.String upBand = (mLinkUpBandwidthKbps > 0) ? (" LinkUpBandwidth>=" + mLinkUpBandwidthKbps) + "Kbps" : "";
        java.lang.String dnBand = (mLinkDownBandwidthKbps > 0) ? (" LinkDnBandwidth>=" + mLinkDownBandwidthKbps) + "Kbps" : "";
        java.lang.String specifier = (mNetworkSpecifier == null) ? "" : (" Specifier: <" + mNetworkSpecifier) + ">";
        java.lang.String signalStrength = (hasSignalStrength()) ? " SignalStrength: " + mSignalStrength : "";
        return (((((("[" + transports) + capabilities) + upBand) + dnBand) + specifier) + signalStrength) + "]";
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String transportNamesOf(int[] types) {
        java.lang.String transports = "";
        for (int i = 0; i < types.length;) {
            switch (types[i]) {
                case android.net.NetworkCapabilities.TRANSPORT_CELLULAR :
                    transports += "CELLULAR";
                    break;
                case android.net.NetworkCapabilities.TRANSPORT_WIFI :
                    transports += "WIFI";
                    break;
                case android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH :
                    transports += "BLUETOOTH";
                    break;
                case android.net.NetworkCapabilities.TRANSPORT_ETHERNET :
                    transports += "ETHERNET";
                    break;
                case android.net.NetworkCapabilities.TRANSPORT_VPN :
                    transports += "VPN";
                    break;
            }
            if ((++i) < types.length)
                transports += "|";

        }
        return transports;
    }
}

