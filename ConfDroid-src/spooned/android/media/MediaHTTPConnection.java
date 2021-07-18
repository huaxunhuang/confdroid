/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.media;


/**
 *
 *
 * @unknown 
 */
public class MediaHTTPConnection extends android.media.IMediaHTTPConnection.Stub {
    private static final java.lang.String TAG = "MediaHTTPConnection";

    private static final boolean VERBOSE = false;

    // connection timeout - 30 sec
    private static final int CONNECT_TIMEOUT_MS = 30 * 1000;

    private long mCurrentOffset = -1;

    private java.net.URL mURL = null;

    private java.util.Map<java.lang.String, java.lang.String> mHeaders = null;

    private java.net.HttpURLConnection mConnection = null;

    private long mTotalSize = -1;

    private java.io.InputStream mInputStream = null;

    private boolean mAllowCrossDomainRedirect = true;

    private boolean mAllowCrossProtocolRedirect = true;

    // from com.squareup.okhttp.internal.http
    private static final int HTTP_TEMP_REDIRECT = 307;

    private static final int MAX_REDIRECTS = 20;

    public MediaHTTPConnection() {
        if (java.net.CookieHandler.getDefault() == null) {
            java.net.CookieHandler.setDefault(new java.net.CookieManager());
        }
        native_setup();
    }

    @java.lang.Override
    public android.os.IBinder connect(java.lang.String uri, java.lang.String headers) {
        if (android.media.MediaHTTPConnection.VERBOSE) {
            android.util.Log.d(android.media.MediaHTTPConnection.TAG, (("connect: uri=" + uri) + ", headers=") + headers);
        }
        try {
            disconnect();
            mAllowCrossDomainRedirect = true;
            mURL = new java.net.URL(uri);
            mHeaders = convertHeaderStringToMap(headers);
        } catch (java.net.MalformedURLException e) {
            return null;
        }
        return native_getIMemory();
    }

    private boolean parseBoolean(java.lang.String val) {
        try {
            return java.lang.Long.parseLong(val) != 0;
        } catch (java.lang.NumberFormatException e) {
            return "true".equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val);
        }
    }

    /* returns true iff header is internal */
    private boolean filterOutInternalHeaders(java.lang.String key, java.lang.String val) {
        if ("android-allow-cross-domain-redirect".equalsIgnoreCase(key)) {
            mAllowCrossDomainRedirect = parseBoolean(val);
            // cross-protocol redirects are also controlled by this flag
            mAllowCrossProtocolRedirect = mAllowCrossDomainRedirect;
        } else {
            return false;
        }
        return true;
    }

    private java.util.Map<java.lang.String, java.lang.String> convertHeaderStringToMap(java.lang.String headers) {
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<java.lang.String, java.lang.String>();
        java.lang.String[] pairs = headers.split("\r\n");
        for (java.lang.String pair : pairs) {
            int colonPos = pair.indexOf(":");
            if (colonPos >= 0) {
                java.lang.String key = pair.substring(0, colonPos);
                java.lang.String val = pair.substring(colonPos + 1);
                if (!filterOutInternalHeaders(key, val)) {
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    @java.lang.Override
    public void disconnect() {
        teardownConnection();
        mHeaders = null;
        mURL = null;
    }

    private void teardownConnection() {
        if (mConnection != null) {
            mInputStream = null;
            mConnection.disconnect();
            mConnection = null;
            mCurrentOffset = -1;
        }
    }

    private static final boolean isLocalHost(java.net.URL url) {
        if (url == null) {
            return false;
        }
        java.lang.String host = url.getHost();
        if (host == null) {
            return false;
        }
        try {
            if (host.equalsIgnoreCase("localhost")) {
                return true;
            }
            if (android.net.NetworkUtils.numericToInetAddress(host).isLoopbackAddress()) {
                return true;
            }
        } catch (java.lang.IllegalArgumentException iex) {
        }
        return false;
    }

    private void seekTo(long offset) throws java.io.IOException {
        teardownConnection();
        try {
            int response;
            int redirectCount = 0;
            java.net.URL url = mURL;
            // do not use any proxy for localhost (127.0.0.1)
            boolean noProxy = android.media.MediaHTTPConnection.isLocalHost(url);
            while (true) {
                if (noProxy) {
                    mConnection = ((java.net.HttpURLConnection) (url.openConnection(java.net.Proxy.NO_PROXY)));
                } else {
                    mConnection = ((java.net.HttpURLConnection) (url.openConnection()));
                }
                mConnection.setConnectTimeout(android.media.MediaHTTPConnection.CONNECT_TIMEOUT_MS);
                // handle redirects ourselves if we do not allow cross-domain redirect
                mConnection.setInstanceFollowRedirects(mAllowCrossDomainRedirect);
                if (mHeaders != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : mHeaders.entrySet()) {
                        mConnection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                if (offset > 0) {
                    mConnection.setRequestProperty("Range", ("bytes=" + offset) + "-");
                }
                response = mConnection.getResponseCode();
                if (((((response != java.net.HttpURLConnection.HTTP_MULT_CHOICE) && (response != java.net.HttpURLConnection.HTTP_MOVED_PERM)) && (response != java.net.HttpURLConnection.HTTP_MOVED_TEMP)) && (response != java.net.HttpURLConnection.HTTP_SEE_OTHER)) && (response != android.media.MediaHTTPConnection.HTTP_TEMP_REDIRECT)) {
                    // not a redirect, or redirect handled by HttpURLConnection
                    break;
                }
                if ((++redirectCount) > android.media.MediaHTTPConnection.MAX_REDIRECTS) {
                    throw new java.net.NoRouteToHostException("Too many redirects: " + redirectCount);
                }
                java.lang.String method = mConnection.getRequestMethod();
                if (((response == android.media.MediaHTTPConnection.HTTP_TEMP_REDIRECT) && (!method.equals("GET"))) && (!method.equals("HEAD"))) {
                    // "If the 307 status code is received in response to a
                    // request other than GET or HEAD, the user agent MUST NOT
                    // automatically redirect the request"
                    throw new java.net.NoRouteToHostException("Invalid redirect");
                }
                java.lang.String location = mConnection.getHeaderField("Location");
                if (location == null) {
                    throw new java.net.NoRouteToHostException("Invalid redirect");
                }
                url = /* TRICKY: don't use url! */
                new java.net.URL(mURL, location);
                if ((!url.getProtocol().equals("https")) && (!url.getProtocol().equals("http"))) {
                    throw new java.net.NoRouteToHostException("Unsupported protocol redirect");
                }
                boolean sameProtocol = mURL.getProtocol().equals(url.getProtocol());
                if ((!mAllowCrossProtocolRedirect) && (!sameProtocol)) {
                    throw new java.net.NoRouteToHostException("Cross-protocol redirects are disallowed");
                }
                boolean sameHost = mURL.getHost().equals(url.getHost());
                if ((!mAllowCrossDomainRedirect) && (!sameHost)) {
                    throw new java.net.NoRouteToHostException("Cross-domain redirects are disallowed");
                }
                if (response != android.media.MediaHTTPConnection.HTTP_TEMP_REDIRECT) {
                    // update effective URL, unless it is a Temporary Redirect
                    mURL = url;
                }
            } 
            if (mAllowCrossDomainRedirect) {
                // remember the current, potentially redirected URL if redirects
                // were handled by HttpURLConnection
                mURL = mConnection.getURL();
            }
            if (response == java.net.HttpURLConnection.HTTP_PARTIAL) {
                // Partial content, we cannot just use getContentLength
                // because what we want is not just the length of the range
                // returned but the size of the full content if available.
                java.lang.String contentRange = mConnection.getHeaderField("Content-Range");
                mTotalSize = -1;
                if (contentRange != null) {
                    // format is "bytes xxx-yyy/zzz
                    // where "zzz" is the total number of bytes of the
                    // content or '*' if unknown.
                    int lastSlashPos = contentRange.lastIndexOf('/');
                    if (lastSlashPos >= 0) {
                        java.lang.String total = contentRange.substring(lastSlashPos + 1);
                        try {
                            mTotalSize = java.lang.Long.parseLong(total);
                        } catch (java.lang.NumberFormatException e) {
                        }
                    }
                }
            } else
                if (response != java.net.HttpURLConnection.HTTP_OK) {
                    throw new java.io.IOException();
                } else {
                    mTotalSize = mConnection.getContentLength();
                }

            if ((offset > 0) && (response != java.net.HttpURLConnection.HTTP_PARTIAL)) {
                // Some servers simply ignore "Range" requests and serve
                // data from the start of the content.
                throw new java.net.ProtocolException();
            }
            mInputStream = new java.io.BufferedInputStream(mConnection.getInputStream());
            mCurrentOffset = offset;
        } catch (java.io.IOException e) {
            mTotalSize = -1;
            mInputStream = null;
            mConnection = null;
            mCurrentOffset = -1;
            throw e;
        }
    }

    @java.lang.Override
    public int readAt(long offset, int size) {
        return native_readAt(offset, size);
    }

    private int readAt(long offset, byte[] data, int size) {
        android.os.StrictMode.ThreadPolicy policy = new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);
        try {
            if (offset != mCurrentOffset) {
                seekTo(offset);
            }
            int n = mInputStream.read(data, 0, size);
            if (n == (-1)) {
                // InputStream signals EOS using a -1 result, our semantics
                // are to return a 0-length read.
                n = 0;
            }
            mCurrentOffset += n;
            if (android.media.MediaHTTPConnection.VERBOSE) {
                android.util.Log.d(android.media.MediaHTTPConnection.TAG, (((("readAt " + offset) + " / ") + size) + " => ") + n);
            }
            return n;
        } catch (java.net.ProtocolException e) {
            android.util.Log.w(android.media.MediaHTTPConnection.TAG, (((("readAt " + offset) + " / ") + size) + " => ") + e);
            return android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
        } catch (java.net.NoRouteToHostException e) {
            android.util.Log.w(android.media.MediaHTTPConnection.TAG, (((("readAt " + offset) + " / ") + size) + " => ") + e);
            return android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
        } catch (java.net.UnknownServiceException e) {
            android.util.Log.w(android.media.MediaHTTPConnection.TAG, (((("readAt " + offset) + " / ") + size) + " => ") + e);
            return android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
        } catch (java.io.IOException e) {
            if (android.media.MediaHTTPConnection.VERBOSE) {
                android.util.Log.d(android.media.MediaHTTPConnection.TAG, ((("readAt " + offset) + " / ") + size) + " => -1");
            }
            return -1;
        } catch (java.lang.Exception e) {
            if (android.media.MediaHTTPConnection.VERBOSE) {
                android.util.Log.d(android.media.MediaHTTPConnection.TAG, "unknown exception " + e);
                android.util.Log.d(android.media.MediaHTTPConnection.TAG, ((("readAt " + offset) + " / ") + size) + " => -1");
            }
            return -1;
        }
    }

    @java.lang.Override
    public long getSize() {
        if (mConnection == null) {
            try {
                seekTo(0);
            } catch (java.io.IOException e) {
                return -1;
            }
        }
        return mTotalSize;
    }

    @java.lang.Override
    public java.lang.String getMIMEType() {
        if (mConnection == null) {
            try {
                seekTo(0);
            } catch (java.io.IOException e) {
                return "application/octet-stream";
            }
        }
        return mConnection.getContentType();
    }

    @java.lang.Override
    public java.lang.String getUri() {
        return mURL.toString();
    }

    @java.lang.Override
    protected void finalize() {
        native_finalize();
    }

    private static final native void native_init();

    private final native void native_setup();

    private final native void native_finalize();

    private final native android.os.IBinder native_getIMemory();

    private final native int native_readAt(long offset, int size);

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaHTTPConnection.native_init();
    }

    private long mNativeContext;
}

