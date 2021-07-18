/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * {@hide }
 *
 * Web Address Parser
 *
 * This is called WebAddress, rather than URL or URI, because it
 * attempts to parse the stuff that a user will actually type into a
 * browser address widget.
 *
 * Unlike java.net.uri, this parser will not choke on URIs missing
 * schemes.  It will only throw a ParseException if the input is
 * really hosed.
 *
 * If given an https scheme but no port, fills in port
 */
// TODO(igsolla): remove WebAddress from the system SDK once the WebView apk does not
// longer need to be binary compatible with the API 21 version of the framework.
@android.annotation.SystemApi
public class WebAddress {
    private java.lang.String mScheme;

    private java.lang.String mHost;

    private int mPort;

    private java.lang.String mPath;

    private java.lang.String mAuthInfo;

    static final int MATCH_GROUP_SCHEME = 1;

    static final int MATCH_GROUP_AUTHORITY = 2;

    static final int MATCH_GROUP_HOST = 3;

    static final int MATCH_GROUP_PORT = 4;

    static final int MATCH_GROUP_PATH = 5;

    static java.util.regex.Pattern sAddressPattern = /* scheme */
    java.util.regex.Pattern.compile(((((((("(?:(http|https|file)\\:\\/\\/)?" + ("(?:([-A-Za-z0-9$_.+!*\'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*\'(),;?&=]+)?)@)?" + "([")) + android.util.Patterns.GOOD_IRI_CHAR) + "%_-][") + android.util.Patterns.GOOD_IRI_CHAR) + "%_\\.-]*|\\[[0-9a-fA-F:\\.]+\\])?") + "(?:\\:([0-9]*))?") + "(\\/?[^#]*)?") + ".*", java.util.regex.Pattern.CASE_INSENSITIVE);

    /**
     * parses given uriString.
     */
    public WebAddress(java.lang.String address) throws android.net.ParseException {
        if (address == null) {
            throw new java.lang.NullPointerException();
        }
        // android.util.Log.d(LOGTAG, "WebAddress: " + address);
        mScheme = "";
        mHost = "";
        mPort = -1;
        mPath = "/";
        mAuthInfo = "";
        java.util.regex.Matcher m = android.net.WebAddress.sAddressPattern.matcher(address);
        java.lang.String t;
        if (m.matches()) {
            t = m.group(android.net.WebAddress.MATCH_GROUP_SCHEME);
            if (t != null)
                mScheme = t.toLowerCase(java.util.Locale.ROOT);

            t = m.group(android.net.WebAddress.MATCH_GROUP_AUTHORITY);
            if (t != null)
                mAuthInfo = t;

            t = m.group(android.net.WebAddress.MATCH_GROUP_HOST);
            if (t != null)
                mHost = t;

            t = m.group(android.net.WebAddress.MATCH_GROUP_PORT);
            if ((t != null) && (t.length() > 0)) {
                // The ':' character is not returned by the regex.
                try {
                    mPort = java.lang.Integer.parseInt(t);
                } catch (java.lang.NumberFormatException ex) {
                    throw new android.net.ParseException("Bad port");
                }
            }
            t = m.group(android.net.WebAddress.MATCH_GROUP_PATH);
            if ((t != null) && (t.length() > 0)) {
                /* handle busted myspace frontpage redirect with
                missing initial "/"
                 */
                if (t.charAt(0) == '/') {
                    mPath = t;
                } else {
                    mPath = "/" + t;
                }
            }
        } else {
            // nothing found... outa here
            throw new android.net.ParseException("Bad address");
        }
        /* Get port from scheme or scheme from port, if necessary and
        possible
         */
        if ((mPort == 443) && mScheme.equals("")) {
            mScheme = "https";
        } else
            if (mPort == (-1)) {
                if (mScheme.equals("https"))
                    mPort = 443;
                // default
                else
                    mPort = 80;
                // default

            }

        if (mScheme.equals(""))
            mScheme = "http";

    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String port = "";
        if (((mPort != 443) && mScheme.equals("https")) || ((mPort != 80) && mScheme.equals("http"))) {
            port = ":" + java.lang.Integer.toString(mPort);
        }
        java.lang.String authInfo = "";
        if (mAuthInfo.length() > 0) {
            authInfo = mAuthInfo + "@";
        }
        return ((((mScheme + "://") + authInfo) + mHost) + port) + mPath;
    }

    /**
     * {@hide }
     */
    public void setScheme(java.lang.String scheme) {
        mScheme = scheme;
    }

    /**
     * {@hide }
     */
    public java.lang.String getScheme() {
        return mScheme;
    }

    /**
     * {@hide }
     */
    public void setHost(java.lang.String host) {
        mHost = host;
    }

    /**
     * {@hide }
     */
    public java.lang.String getHost() {
        return mHost;
    }

    /**
     * {@hide }
     */
    public void setPort(int port) {
        mPort = port;
    }

    /**
     * {@hide }
     */
    public int getPort() {
        return mPort;
    }

    /**
     * {@hide }
     */
    public void setPath(java.lang.String path) {
        mPath = path;
    }

    /**
     * {@hide }
     */
    public java.lang.String getPath() {
        return mPath;
    }

    /**
     * {@hide }
     */
    public void setAuthInfo(java.lang.String authInfo) {
        mAuthInfo = authInfo;
    }

    /**
     * {@hide }
     */
    public java.lang.String getAuthInfo() {
        return mAuthInfo;
    }
}

