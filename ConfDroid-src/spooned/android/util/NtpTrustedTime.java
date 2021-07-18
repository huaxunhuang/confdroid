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
package android.util;


/**
 * {@link TrustedTime} that connects with a remote NTP server as its trusted
 * time source.
 *
 * @unknown 
 */
public class NtpTrustedTime implements android.util.TrustedTime {
    private static final java.lang.String TAG = "NtpTrustedTime";

    private static final boolean LOGD = false;

    private static android.util.NtpTrustedTime sSingleton;

    private static android.content.Context sContext;

    private final java.lang.String mServer;

    private final long mTimeout;

    private android.net.ConnectivityManager mCM;

    private boolean mHasCache;

    private long mCachedNtpTime;

    private long mCachedNtpElapsedRealtime;

    private long mCachedNtpCertainty;

    private NtpTrustedTime(java.lang.String server, long timeout) {
        if (android.util.NtpTrustedTime.LOGD)
            android.util.Log.d(android.util.NtpTrustedTime.TAG, "creating NtpTrustedTime using " + server);

        mServer = server;
        mTimeout = timeout;
    }

    public static synchronized android.util.NtpTrustedTime getInstance(android.content.Context context) {
        if (android.util.NtpTrustedTime.sSingleton == null) {
            final android.content.res.Resources res = context.getResources();
            final android.content.ContentResolver resolver = context.getContentResolver();
            final java.lang.String defaultServer = res.getString(com.android.internal.R.string.config_ntpServer);
            final long defaultTimeout = res.getInteger(com.android.internal.R.integer.config_ntpTimeout);
            final java.lang.String secureServer = android.provider.Settings.Global.getString(resolver, android.provider.Settings.Global.NTP_SERVER);
            final long timeout = android.provider.Settings.Global.getLong(resolver, android.provider.Settings.Global.NTP_TIMEOUT, defaultTimeout);
            final java.lang.String server = (secureServer != null) ? secureServer : defaultServer;
            android.util.NtpTrustedTime.sSingleton = new android.util.NtpTrustedTime(server, timeout);
            android.util.NtpTrustedTime.sContext = context;
        }
        return android.util.NtpTrustedTime.sSingleton;
    }

    @java.lang.Override
    public boolean forceRefresh() {
        if (android.text.TextUtils.isEmpty(mServer)) {
            // missing server, so no trusted time available
            return false;
        }
        // We can't do this at initialization time: ConnectivityService might not be running yet.
        synchronized(this) {
            if (mCM == null) {
                mCM = ((android.net.ConnectivityManager) (android.util.NtpTrustedTime.sContext.getSystemService(android.content.Context.CONNECTIVITY_SERVICE)));
            }
        }
        final android.net.NetworkInfo ni = (mCM == null) ? null : mCM.getActiveNetworkInfo();
        if ((ni == null) || (!ni.isConnected())) {
            if (android.util.NtpTrustedTime.LOGD)
                android.util.Log.d(android.util.NtpTrustedTime.TAG, "forceRefresh: no connectivity");

            return false;
        }
        if (android.util.NtpTrustedTime.LOGD)
            android.util.Log.d(android.util.NtpTrustedTime.TAG, "forceRefresh() from cache miss");

        final android.net.SntpClient client = new android.net.SntpClient();
        if (client.requestTime(mServer, ((int) (mTimeout)))) {
            mHasCache = true;
            mCachedNtpTime = client.getNtpTime();
            mCachedNtpElapsedRealtime = client.getNtpTimeReference();
            mCachedNtpCertainty = client.getRoundTripTime() / 2;
            return true;
        } else {
            return false;
        }
    }

    @java.lang.Override
    public boolean hasCache() {
        return mHasCache;
    }

    @java.lang.Override
    public long getCacheAge() {
        if (mHasCache) {
            return android.os.SystemClock.elapsedRealtime() - mCachedNtpElapsedRealtime;
        } else {
            return java.lang.Long.MAX_VALUE;
        }
    }

    @java.lang.Override
    public long getCacheCertainty() {
        if (mHasCache) {
            return mCachedNtpCertainty;
        } else {
            return java.lang.Long.MAX_VALUE;
        }
    }

    @java.lang.Override
    public long currentTimeMillis() {
        if (!mHasCache) {
            throw new java.lang.IllegalStateException("Missing authoritative time source");
        }
        if (android.util.NtpTrustedTime.LOGD)
            android.util.Log.d(android.util.NtpTrustedTime.TAG, "currentTimeMillis() cache hit");

        // current time is age after the last ntp cache; callers who
        // want fresh values will hit makeAuthoritative() first.
        return mCachedNtpTime + getCacheAge();
    }

    public long getCachedNtpTime() {
        if (android.util.NtpTrustedTime.LOGD)
            android.util.Log.d(android.util.NtpTrustedTime.TAG, "getCachedNtpTime() cache hit");

        return mCachedNtpTime;
    }

    public long getCachedNtpTimeReference() {
        return mCachedNtpElapsedRealtime;
    }
}

