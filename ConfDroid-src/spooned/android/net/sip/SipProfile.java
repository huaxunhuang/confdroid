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
package android.net.sip;


/**
 * Defines a SIP profile, including a SIP account, domain and server information.
 * <p>You can create a {@link SipProfile} using {@link SipProfile.Builder}. You can also retrieve one from a {@link SipSession}, using {@link SipSession#getLocalProfile} and {@link SipSession#getPeerProfile}.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using SIP, read the
 * <a href="{@docRoot }guide/topics/network/sip.html">Session Initiation Protocol</a>
 * developer guide.</p>
 * </div>
 */
public class SipProfile implements android.os.Parcelable , java.io.Serializable , java.lang.Cloneable {
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_PORT = 5060;

    private static final java.lang.String TCP = "TCP";

    private static final java.lang.String UDP = "UDP";

    private javax.sip.address.Address mAddress;

    private java.lang.String mProxyAddress;

    private java.lang.String mPassword;

    private java.lang.String mDomain;

    private java.lang.String mProtocol = android.net.sip.SipProfile.UDP;

    private java.lang.String mProfileName;

    private java.lang.String mAuthUserName;

    private int mPort = android.net.sip.SipProfile.DEFAULT_PORT;

    private boolean mSendKeepAlive = false;

    private boolean mAutoRegistration = true;

    private transient int mCallingUid = 0;

    public static final android.os.Parcelable.Creator<android.net.sip.SipProfile> CREATOR = new android.os.Parcelable.Creator<android.net.sip.SipProfile>() {
        public android.net.sip.SipProfile createFromParcel(android.os.Parcel in) {
            return new android.net.sip.SipProfile(in);
        }

        public android.net.sip.SipProfile[] newArray(int size) {
            return new android.net.sip.SipProfile[size];
        }
    };

    /**
     * Helper class for creating a {@link SipProfile}.
     */
    public static class Builder {
        private javax.sip.address.AddressFactory mAddressFactory;

        private android.net.sip.SipProfile mProfile = new android.net.sip.SipProfile();

        private javax.sip.address.SipURI mUri;

        private java.lang.String mDisplayName;

        private java.lang.String mProxyAddress;

        {
            try {
                mAddressFactory = javax.sip.SipFactory.getInstance().createAddressFactory();
            } catch (javax.sip.PeerUnavailableException e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        /**
         * Creates a builder based on the given profile.
         */
        public Builder(android.net.sip.SipProfile profile) {
            if (profile == null)
                throw new java.lang.NullPointerException();

            try {
                mProfile = ((android.net.sip.SipProfile) (profile.clone()));
            } catch (java.lang.CloneNotSupportedException e) {
                throw new java.lang.RuntimeException("should not occur", e);
            }
            mProfile.mAddress = null;
            mUri = profile.getUri();
            mUri.setUserPassword(profile.getPassword());
            mDisplayName = profile.getDisplayName();
            mProxyAddress = profile.getProxyAddress();
            mProfile.mPort = profile.getPort();
        }

        /**
         * Constructor.
         *
         * @param uriString
         * 		the URI string as "sip:<user_name>@<domain>"
         * @throws ParseException
         * 		if the string is not a valid URI
         */
        public Builder(java.lang.String uriString) throws java.text.ParseException {
            if (uriString == null) {
                throw new java.lang.NullPointerException("uriString cannot be null");
            }
            javax.sip.address.URI uri = mAddressFactory.createURI(fix(uriString));
            if (uri instanceof javax.sip.address.SipURI) {
                mUri = ((javax.sip.address.SipURI) (uri));
            } else {
                throw new java.text.ParseException(uriString + " is not a SIP URI", 0);
            }
            mProfile.mDomain = mUri.getHost();
        }

        /**
         * Constructor.
         *
         * @param username
         * 		username of the SIP account
         * @param serverDomain
         * 		the SIP server domain; if the network address
         * 		is different from the domain, use {@link #setOutboundProxy} to
         * 		set server address
         * @throws ParseException
         * 		if the parameters are not valid
         */
        public Builder(java.lang.String username, java.lang.String serverDomain) throws java.text.ParseException {
            if ((username == null) || (serverDomain == null)) {
                throw new java.lang.NullPointerException("username and serverDomain cannot be null");
            }
            mUri = mAddressFactory.createSipURI(username, serverDomain);
            mProfile.mDomain = serverDomain;
        }

        private java.lang.String fix(java.lang.String uriString) {
            return uriString.trim().toLowerCase().startsWith("sip:") ? uriString : "sip:" + uriString;
        }

        /**
         * Sets the username used for authentication.
         *
         * @param name
         * 		authentication username of the profile
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setAuthUserName(java.lang.String name) {
            mProfile.mAuthUserName = name;
            return this;
        }

        /**
         * Sets the name of the profile. This name is given by user.
         *
         * @param name
         * 		name of the profile
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setProfileName(java.lang.String name) {
            mProfile.mProfileName = name;
            return this;
        }

        /**
         * Sets the password of the SIP account
         *
         * @param password
         * 		password of the SIP account
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setPassword(java.lang.String password) {
            mUri.setUserPassword(password);
            return this;
        }

        /**
         * Sets the port number of the server. By default, it is 5060.
         *
         * @param port
         * 		port number of the server
         * @return this builder object
         * @throws IllegalArgumentException
         * 		if the port number is out of range
         */
        public android.net.sip.SipProfile.Builder setPort(int port) throws java.lang.IllegalArgumentException {
            if ((port > 65535) || (port < 1000)) {
                throw new java.lang.IllegalArgumentException("incorrect port arugment: " + port);
            }
            mProfile.mPort = port;
            return this;
        }

        /**
         * Sets the protocol used to connect to the SIP server. Currently,
         * only "UDP" and "TCP" are supported.
         *
         * @param protocol
         * 		the protocol string
         * @return this builder object
         * @throws IllegalArgumentException
         * 		if the protocol is not recognized
         */
        public android.net.sip.SipProfile.Builder setProtocol(java.lang.String protocol) throws java.lang.IllegalArgumentException {
            if (protocol == null) {
                throw new java.lang.NullPointerException("protocol cannot be null");
            }
            protocol = protocol.toUpperCase();
            if ((!protocol.equals(android.net.sip.SipProfile.UDP)) && (!protocol.equals(android.net.sip.SipProfile.TCP))) {
                throw new java.lang.IllegalArgumentException("unsupported protocol: " + protocol);
            }
            mProfile.mProtocol = protocol;
            return this;
        }

        /**
         * Sets the outbound proxy of the SIP server.
         *
         * @param outboundProxy
         * 		the network address of the outbound proxy
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setOutboundProxy(java.lang.String outboundProxy) {
            mProxyAddress = outboundProxy;
            return this;
        }

        /**
         * Sets the display name of the user.
         *
         * @param displayName
         * 		display name of the user
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setDisplayName(java.lang.String displayName) {
            mDisplayName = displayName;
            return this;
        }

        /**
         * Sets the send keep-alive flag.
         *
         * @param flag
         * 		true if sending keep-alive message is required,
         * 		false otherwise
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setSendKeepAlive(boolean flag) {
            mProfile.mSendKeepAlive = flag;
            return this;
        }

        /**
         * Sets the auto. registration flag.
         *
         * @param flag
         * 		true if the profile will be registered automatically,
         * 		false otherwise
         * @return this builder object
         */
        public android.net.sip.SipProfile.Builder setAutoRegistration(boolean flag) {
            mProfile.mAutoRegistration = flag;
            return this;
        }

        /**
         * Builds and returns the SIP profile object.
         *
         * @return the profile object created
         */
        public android.net.sip.SipProfile build() {
            // remove password from URI
            mProfile.mPassword = mUri.getUserPassword();
            mUri.setUserPassword(null);
            try {
                if (!android.text.TextUtils.isEmpty(mProxyAddress)) {
                    javax.sip.address.SipURI uri = ((javax.sip.address.SipURI) (mAddressFactory.createURI(fix(mProxyAddress))));
                    mProfile.mProxyAddress = uri.getHost();
                } else {
                    if (!mProfile.mProtocol.equals(android.net.sip.SipProfile.UDP)) {
                        mUri.setTransportParam(mProfile.mProtocol);
                    }
                    if (mProfile.mPort != android.net.sip.SipProfile.DEFAULT_PORT) {
                        mUri.setPort(mProfile.mPort);
                    }
                }
                mProfile.mAddress = mAddressFactory.createAddress(mDisplayName, mUri);
            } catch (javax.sip.InvalidArgumentException e) {
                throw new java.lang.RuntimeException(e);
            } catch (java.text.ParseException e) {
                // must not occur
                throw new java.lang.RuntimeException(e);
            }
            return mProfile;
        }
    }

    private SipProfile() {
    }

    private SipProfile(android.os.Parcel in) {
        mAddress = ((javax.sip.address.Address) (in.readSerializable()));
        mProxyAddress = in.readString();
        mPassword = in.readString();
        mDomain = in.readString();
        mProtocol = in.readString();
        mProfileName = in.readString();
        mSendKeepAlive = (in.readInt() == 0) ? false : true;
        mAutoRegistration = (in.readInt() == 0) ? false : true;
        mCallingUid = in.readInt();
        mPort = in.readInt();
        mAuthUserName = in.readString();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeSerializable(mAddress);
        out.writeString(mProxyAddress);
        out.writeString(mPassword);
        out.writeString(mDomain);
        out.writeString(mProtocol);
        out.writeString(mProfileName);
        out.writeInt(mSendKeepAlive ? 1 : 0);
        out.writeInt(mAutoRegistration ? 1 : 0);
        out.writeInt(mCallingUid);
        out.writeInt(mPort);
        out.writeString(mAuthUserName);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the SIP URI of this profile.
     *
     * @return the SIP URI of this profile
     * @unknown 
     */
    public javax.sip.address.SipURI getUri() {
        return ((javax.sip.address.SipURI) (mAddress.getURI()));
    }

    /**
     * Gets the SIP URI string of this profile.
     *
     * @return the SIP URI string of this profile
     */
    public java.lang.String getUriString() {
        // We need to return the sip uri domain instead of
        // the SIP URI with transport, port information if
        // the outbound proxy address exists.
        if (!android.text.TextUtils.isEmpty(mProxyAddress)) {
            return (("sip:" + getUserName()) + "@") + mDomain;
        }
        return getUri().toString();
    }

    /**
     * Gets the SIP address of this profile.
     *
     * @return the SIP address of this profile
     * @unknown 
     */
    public javax.sip.address.Address getSipAddress() {
        return mAddress;
    }

    /**
     * Gets the display name of the user.
     *
     * @return the display name of the user
     */
    public java.lang.String getDisplayName() {
        return mAddress.getDisplayName();
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public java.lang.String getUserName() {
        return getUri().getUser();
    }

    /**
     * Gets the username for authentication. If it is null, then the username
     * is used in authentication instead.
     *
     * @return the authentication username
     * @see #getUserName
     */
    public java.lang.String getAuthUserName() {
        return mAuthUserName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public java.lang.String getPassword() {
        return mPassword;
    }

    /**
     * Gets the SIP domain.
     *
     * @return the SIP domain
     */
    public java.lang.String getSipDomain() {
        return mDomain;
    }

    /**
     * Gets the port number of the SIP server.
     *
     * @return the port number of the SIP server
     */
    public int getPort() {
        return mPort;
    }

    /**
     * Gets the protocol used to connect to the server.
     *
     * @return the protocol
     */
    public java.lang.String getProtocol() {
        return mProtocol;
    }

    /**
     * Gets the network address of the server outbound proxy.
     *
     * @return the network address of the server outbound proxy
     */
    public java.lang.String getProxyAddress() {
        return mProxyAddress;
    }

    /**
     * Gets the (user-defined) name of the profile.
     *
     * @return name of the profile
     */
    public java.lang.String getProfileName() {
        return mProfileName;
    }

    /**
     * Gets the flag of 'Sending keep-alive'.
     *
     * @return the flag of sending SIP keep-alive messages.
     */
    public boolean getSendKeepAlive() {
        return mSendKeepAlive;
    }

    /**
     * Gets the flag of 'Auto Registration'.
     *
     * @return the flag of registering the profile automatically.
     */
    public boolean getAutoRegistration() {
        return mAutoRegistration;
    }

    /**
     * Sets the calling process's Uid in the sip service.
     *
     * @unknown 
     */
    public void setCallingUid(int uid) {
        mCallingUid = uid;
    }

    /**
     * Gets the calling process's Uid in the sip settings.
     *
     * @unknown 
     */
    public int getCallingUid() {
        return mCallingUid;
    }

    private java.lang.Object readResolve() throws java.io.ObjectStreamException {
        // For compatibility.
        if (mPort == 0)
            mPort = android.net.sip.SipProfile.DEFAULT_PORT;

        return this;
    }
}

