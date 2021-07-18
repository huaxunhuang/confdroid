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
package android.net;


/**
 * Describes a proxy configuration.
 *
 * Proxy configurations are already integrated within the {@code java.net} and
 * Apache HTTP stack. So {@link URLConnection} and Apache's {@code HttpClient} will use
 * them automatically.
 *
 * Other HTTP stacks will need to obtain the proxy info from
 * {@link Proxy#PROXY_CHANGE_ACTION} broadcast as the extra {@link Proxy#EXTRA_PROXY_INFO}.
 */
public class ProxyInfo implements android.os.Parcelable {
    private java.lang.String mHost;

    private int mPort;

    private java.lang.String mExclusionList;

    private java.lang.String[] mParsedExclusionList;

    private android.net.Uri mPacFileUrl;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String LOCAL_EXCL_LIST = "";

    /**
     *
     *
     * @unknown 
     */
    public static final int LOCAL_PORT = -1;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String LOCAL_HOST = "localhost";

    /**
     * Constructs a {@link ProxyInfo} object that points at a Direct proxy
     * on the specified host and port.
     */
    public static android.net.ProxyInfo buildDirectProxy(java.lang.String host, int port) {
        return new android.net.ProxyInfo(host, port, null);
    }

    /**
     * Constructs a {@link ProxyInfo} object that points at a Direct proxy
     * on the specified host and port.
     *
     * The proxy will not be used to access any host in exclusion list, exclList.
     *
     * @param exclList
     * 		Hosts to exclude using the proxy on connections for.  These
     * 		hosts can use wildcards such as *.example.com.
     */
    public static android.net.ProxyInfo buildDirectProxy(java.lang.String host, int port, java.util.List<java.lang.String> exclList) {
        java.lang.String[] array = exclList.toArray(new java.lang.String[exclList.size()]);
        return new android.net.ProxyInfo(host, port, android.text.TextUtils.join(",", array), array);
    }

    /**
     * Construct a {@link ProxyInfo} that will download and run the PAC script
     * at the specified URL.
     */
    public static android.net.ProxyInfo buildPacProxy(android.net.Uri pacUri) {
        return new android.net.ProxyInfo(pacUri);
    }

    /**
     * Create a ProxyProperties that points at a HTTP Proxy.
     *
     * @unknown 
     */
    public ProxyInfo(java.lang.String host, int port, java.lang.String exclList) {
        mHost = host;
        mPort = port;
        setExclusionList(exclList);
        mPacFileUrl = android.net.Uri.EMPTY;
    }

    /**
     * Create a ProxyProperties that points at a PAC URL.
     *
     * @unknown 
     */
    public ProxyInfo(android.net.Uri pacFileUrl) {
        mHost = android.net.ProxyInfo.LOCAL_HOST;
        mPort = android.net.ProxyInfo.LOCAL_PORT;
        setExclusionList(android.net.ProxyInfo.LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new java.lang.NullPointerException();
        }
        mPacFileUrl = pacFileUrl;
    }

    /**
     * Create a ProxyProperties that points at a PAC URL.
     *
     * @unknown 
     */
    public ProxyInfo(java.lang.String pacFileUrl) {
        mHost = android.net.ProxyInfo.LOCAL_HOST;
        mPort = android.net.ProxyInfo.LOCAL_PORT;
        setExclusionList(android.net.ProxyInfo.LOCAL_EXCL_LIST);
        mPacFileUrl = android.net.Uri.parse(pacFileUrl);
    }

    /**
     * Only used in PacManager after Local Proxy is bound.
     *
     * @unknown 
     */
    public ProxyInfo(android.net.Uri pacFileUrl, int localProxyPort) {
        mHost = android.net.ProxyInfo.LOCAL_HOST;
        mPort = localProxyPort;
        setExclusionList(android.net.ProxyInfo.LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new java.lang.NullPointerException();
        }
        mPacFileUrl = pacFileUrl;
    }

    private ProxyInfo(java.lang.String host, int port, java.lang.String exclList, java.lang.String[] parsedExclList) {
        mHost = host;
        mPort = port;
        mExclusionList = exclList;
        mParsedExclusionList = parsedExclList;
        mPacFileUrl = android.net.Uri.EMPTY;
    }

    // copy constructor instead of clone
    /**
     *
     *
     * @unknown 
     */
    public ProxyInfo(android.net.ProxyInfo source) {
        if (source != null) {
            mHost = source.getHost();
            mPort = source.getPort();
            mPacFileUrl = source.mPacFileUrl;
            mExclusionList = source.getExclusionListAsString();
            mParsedExclusionList = source.mParsedExclusionList;
        } else {
            mPacFileUrl = android.net.Uri.EMPTY;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public java.net.InetSocketAddress getSocketAddress() {
        java.net.InetSocketAddress inetSocketAddress = null;
        try {
            inetSocketAddress = new java.net.InetSocketAddress(mHost, mPort);
        } catch (java.lang.IllegalArgumentException e) {
        }
        return inetSocketAddress;
    }

    /**
     * Returns the URL of the current PAC script or null if there is
     * no PAC script.
     */
    public android.net.Uri getPacFileUrl() {
        return mPacFileUrl;
    }

    /**
     * When configured to use a Direct Proxy this returns the host
     * of the proxy.
     */
    public java.lang.String getHost() {
        return mHost;
    }

    /**
     * When configured to use a Direct Proxy this returns the port
     * of the proxy
     */
    public int getPort() {
        return mPort;
    }

    /**
     * When configured to use a Direct Proxy this returns the list
     * of hosts for which the proxy is ignored.
     */
    public java.lang.String[] getExclusionList() {
        return mParsedExclusionList;
    }

    /**
     * comma separated
     *
     * @unknown 
     */
    public java.lang.String getExclusionListAsString() {
        return mExclusionList;
    }

    // comma separated
    private void setExclusionList(java.lang.String exclusionList) {
        mExclusionList = exclusionList;
        if (mExclusionList == null) {
            mParsedExclusionList = new java.lang.String[0];
        } else {
            mParsedExclusionList = exclusionList.toLowerCase(java.util.Locale.ROOT).split(",");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isValid() {
        if (!android.net.Uri.EMPTY.equals(mPacFileUrl))
            return true;

        return android.net.Proxy.PROXY_VALID == android.net.Proxy.validate(mHost == null ? "" : mHost, mPort == 0 ? "" : java.lang.Integer.toString(mPort), mExclusionList == null ? "" : mExclusionList);
    }

    /**
     *
     *
     * @unknown 
     */
    public java.net.Proxy makeProxy() {
        java.net.Proxy proxy = java.net.Proxy.NO_PROXY;
        if (mHost != null) {
            try {
                java.net.InetSocketAddress inetSocketAddress = new java.net.InetSocketAddress(mHost, mPort);
                proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, inetSocketAddress);
            } catch (java.lang.IllegalArgumentException e) {
            }
        }
        return proxy;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (!android.net.Uri.EMPTY.equals(mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(mPacFileUrl);
        }
        if (mHost != null) {
            sb.append("[");
            sb.append(mHost);
            sb.append("] ");
            sb.append(java.lang.Integer.toString(mPort));
            if (mExclusionList != null) {
                sb.append(" xl=").append(mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.net.ProxyInfo))
            return false;

        android.net.ProxyInfo p = ((android.net.ProxyInfo) (o));
        // If PAC URL is present in either then they must be equal.
        // Other parameters will only be for fall back.
        if (!android.net.Uri.EMPTY.equals(mPacFileUrl)) {
            return mPacFileUrl.equals(p.getPacFileUrl()) && (mPort == p.mPort);
        }
        if (!android.net.Uri.EMPTY.equals(p.mPacFileUrl)) {
            return false;
        }
        if ((mExclusionList != null) && (!mExclusionList.equals(p.getExclusionListAsString()))) {
            return false;
        }
        if (((mHost != null) && (p.getHost() != null)) && (mHost.equals(p.getHost()) == false)) {
            return false;
        }
        if ((mHost != null) && (p.mHost == null))
            return false;

        if ((mHost == null) && (p.mHost != null))
            return false;

        if (mPort != p.mPort)
            return false;

        return true;
    }

    /**
     * Implement the Parcelable interface
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    /* generate hashcode based on significant fields */
    @java.lang.Override
    public int hashCode() {
        return ((null == mHost ? 0 : mHost.hashCode()) + (null == mExclusionList ? 0 : mExclusionList.hashCode())) + mPort;
    }

    /**
     * Implement the Parcelable interface.
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (!android.net.Uri.EMPTY.equals(mPacFileUrl)) {
            dest.writeByte(((byte) (1)));
            mPacFileUrl.writeToParcel(dest, 0);
            dest.writeInt(mPort);
            return;
        } else {
            dest.writeByte(((byte) (0)));
        }
        if (mHost != null) {
            dest.writeByte(((byte) (1)));
            dest.writeString(mHost);
            dest.writeInt(mPort);
        } else {
            dest.writeByte(((byte) (0)));
        }
        dest.writeString(mExclusionList);
        dest.writeStringArray(mParsedExclusionList);
    }

    public static final android.os.Parcelable.Creator<android.net.ProxyInfo> CREATOR = new android.os.Parcelable.Creator<android.net.ProxyInfo>() {
        public android.net.ProxyInfo createFromParcel(android.os.Parcel in) {
            java.lang.String host = null;
            int port = 0;
            if (in.readByte() != 0) {
                android.net.Uri url = android.net.Uri.CREATOR.createFromParcel(in);
                int localPort = in.readInt();
                return new android.net.ProxyInfo(url, localPort);
            }
            if (in.readByte() != 0) {
                host = in.readString();
                port = in.readInt();
            }
            java.lang.String exclList = in.readString();
            java.lang.String[] parsedExclList = in.readStringArray();
            android.net.ProxyInfo proxyProperties = new android.net.ProxyInfo(host, port, exclList, parsedExclList);
            return proxyProperties;
        }

        public android.net.ProxyInfo[] newArray(int size) {
            return new android.net.ProxyInfo[size];
        }
    };
}

