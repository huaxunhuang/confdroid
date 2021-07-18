/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v4.net;


/**
 * Helper for accessing features in {@link ConnectivityManager} introduced after
 * API level 16 in a backwards compatible fashion.
 */
public final class ConnectivityManagerCompat {
    interface ConnectivityManagerCompatImpl {
        boolean isActiveNetworkMetered(android.net.ConnectivityManager cm);

        @android.support.v4.net.ConnectivityManagerCompat.RestrictBackgroundStatus
        int getRestrictBackgroundStatus(android.net.ConnectivityManager cm);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef(flag = false, value = { android.support.v4.net.ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_DISABLED, android.support.v4.net.ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_WHITELISTED, android.support.v4.net.ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_ENABLED })
    public @interface RestrictBackgroundStatus {}

    /**
     * Device is not restricting metered network activity while application is running on
     * background.
     */
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;

    /**
     * Device is restricting metered network activity while application is running on background,
     * but application is allowed to bypass it.
     * <p>
     * In this state, application should take action to mitigate metered network access.
     * For example, a music streaming application should switch to a low-bandwidth bitrate.
     */
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    /**
     * Device is restricting metered network activity while application is running on background.
     * <p>
     * In this state, application should not try to use the network while running on background,
     * because it would be denied.
     */
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;

    static class BaseConnectivityManagerCompatImpl implements android.support.v4.net.ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
        @java.lang.Override
        public boolean isActiveNetworkMetered(android.net.ConnectivityManager cm) {
            final android.net.NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                // err on side of caution
                return true;
            }
            final int type = info.getType();
            switch (type) {
                case android.net.ConnectivityManager.TYPE_MOBILE :
                case android.net.ConnectivityManager.TYPE_MOBILE_DUN :
                case android.net.ConnectivityManager.TYPE_MOBILE_HIPRI :
                case android.net.ConnectivityManager.TYPE_MOBILE_MMS :
                case android.net.ConnectivityManager.TYPE_MOBILE_SUPL :
                case android.net.ConnectivityManager.TYPE_WIMAX :
                    return true;
                case android.net.ConnectivityManager.TYPE_WIFI :
                    return false;
                default :
                    // err on side of caution
                    return true;
            }
        }

        @java.lang.Override
        public int getRestrictBackgroundStatus(android.net.ConnectivityManager cm) {
            return android.support.v4.net.ConnectivityManagerCompat.RESTRICT_BACKGROUND_STATUS_ENABLED;
        }
    }

    static class HoneycombMR2ConnectivityManagerCompatImpl extends android.support.v4.net.ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl {
        @java.lang.Override
        public boolean isActiveNetworkMetered(android.net.ConnectivityManager cm) {
            return android.support.v4.net.ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(cm);
        }
    }

    static class JellyBeanConnectivityManagerCompatImpl extends android.support.v4.net.ConnectivityManagerCompat.HoneycombMR2ConnectivityManagerCompatImpl {
        @java.lang.Override
        public boolean isActiveNetworkMetered(android.net.ConnectivityManager cm) {
            return android.support.v4.net.ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(cm);
        }
    }

    static class Api24ConnectivityManagerCompatImpl extends android.support.v4.net.ConnectivityManagerCompat.JellyBeanConnectivityManagerCompatImpl {
        @java.lang.Override
        public int getRestrictBackgroundStatus(android.net.ConnectivityManager cm) {
            // noinspection ResourceType
            return android.support.v4.net.ConnectivityManagerCompatApi24.getRestrictBackgroundStatus(cm);
        }
    }

    private static final android.support.v4.net.ConnectivityManagerCompat.ConnectivityManagerCompatImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            IMPL = new android.support.v4.net.ConnectivityManagerCompat.Api24ConnectivityManagerCompatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                IMPL = new android.support.v4.net.ConnectivityManagerCompat.JellyBeanConnectivityManagerCompatImpl();
            } else
                if (android.os.Build.VERSION.SDK_INT >= 13) {
                    IMPL = new android.support.v4.net.ConnectivityManagerCompat.HoneycombMR2ConnectivityManagerCompatImpl();
                } else {
                    IMPL = new android.support.v4.net.ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl();
                }


    }

    /**
     * Returns if the currently active data network is metered. A network is
     * classified as metered when the user is sensitive to heavy data usage on
     * that connection due to monetary costs, data limitations or
     * battery/performance issues. You should check this before doing large
     * data transfers, and warn the user or delay the operation until another
     * network is available.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
     *
     * @return {@code true} if large transfers should be avoided, otherwise
    {@code false}.
     */
    public static boolean isActiveNetworkMetered(android.net.ConnectivityManager cm) {
        return android.support.v4.net.ConnectivityManagerCompat.IMPL.isActiveNetworkMetered(cm);
    }

    /**
     * Return the {@link NetworkInfo} that caused the given
     * {@link ConnectivityManager#CONNECTIVITY_ACTION} broadcast. This obtains
     * the current state from {@link ConnectivityManager} instead of using the
     * potentially-stale value from
     * {@link ConnectivityManager#EXTRA_NETWORK_INFO}. May be {@code null}.
     */
    public static android.net.NetworkInfo getNetworkInfoFromBroadcast(android.net.ConnectivityManager cm, android.content.Intent intent) {
        final android.net.NetworkInfo info = intent.getParcelableExtra(android.net.ConnectivityManager.EXTRA_NETWORK_INFO);
        if (info != null) {
            return cm.getNetworkInfo(info.getType());
        } else {
            return null;
        }
    }

    /**
     * Determines if the calling application is subject to metered network restrictions while
     * running on background.
     *
     * @return {@link #RESTRICT_BACKGROUND_STATUS_DISABLED},
    {@link #RESTRICT_BACKGROUND_STATUS_ENABLED},
    or {@link #RESTRICT_BACKGROUND_STATUS_WHITELISTED}
     */
    @android.support.v4.net.ConnectivityManagerCompat.RestrictBackgroundStatus
    public static int getRestrictBackgroundStatus(android.net.ConnectivityManager cm) {
        return android.support.v4.net.ConnectivityManagerCompat.IMPL.getRestrictBackgroundStatus(cm);
    }

    private ConnectivityManagerCompat() {
    }
}

