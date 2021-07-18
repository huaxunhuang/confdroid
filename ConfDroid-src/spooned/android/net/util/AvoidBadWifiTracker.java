/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.net.util;


/**
 * A class to encapsulate management of the "Smart Networking" capability of
 * avoiding bad Wi-Fi when, for example upstream connectivity is lost or
 * certain critical link failures occur.
 *
 * This enables the device to switch to another form of connectivity, like
 * mobile, if it's available and working.
 *
 * The Runnable |cb|, if given, is called on the supplied Handler's thread
 * whether the computed "avoid bad wifi" value changes.
 *
 * Disabling this reverts the device to a level of networking sophistication
 * circa 2012-13 by disabling disparate code paths each of which contribute to
 * maintaining continuous, working Internet connectivity.
 *
 * @unknown 
 */
public class AvoidBadWifiTracker {
    private static java.lang.String TAG = android.net.util.AvoidBadWifiTracker.class.getSimpleName();

    private final android.content.Context mContext;

    private final android.os.Handler mHandler;

    private final java.lang.Runnable mReevaluateRunnable;

    private final android.net.util.AvoidBadWifiTracker.SettingObserver mSettingObserver;

    private volatile boolean mAvoidBadWifi = true;

    public AvoidBadWifiTracker(android.content.Context ctx, android.os.Handler handler) {
        this(ctx, handler, null);
    }

    public AvoidBadWifiTracker(android.content.Context ctx, android.os.Handler handler, java.lang.Runnable cb) {
        mContext = ctx;
        mHandler = handler;
        mReevaluateRunnable = () -> {
            if (update() && (cb != null))
                cb.run();

        };
        mSettingObserver = new android.net.util.AvoidBadWifiTracker.SettingObserver();
        final android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction(android.content.Intent.ACTION_CONFIGURATION_CHANGED);
        mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() {
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                reevaluate();
            }
        }, android.os.UserHandle.ALL, intentFilter, null, null);
        update();
    }

    public boolean currentValue() {
        return mAvoidBadWifi;
    }

    /**
     * Whether the device or carrier configuration disables avoiding bad wifi by default.
     */
    public boolean configRestrictsAvoidBadWifi() {
        return mContext.getResources().getInteger(R.integer.config_networkAvoidBadWifi) == 0;
    }

    /**
     * Whether we should display a notification when wifi becomes unvalidated.
     */
    public boolean shouldNotifyWifiUnvalidated() {
        return configRestrictsAvoidBadWifi() && (getSettingsValue() == null);
    }

    public java.lang.String getSettingsValue() {
        final android.content.ContentResolver resolver = mContext.getContentResolver();
        return android.provider.Settings.Global.getString(resolver, android.provider.Settings.Global.NETWORK_AVOID_BAD_WIFI);
    }

    @com.android.internal.annotations.VisibleForTesting
    public void reevaluate() {
        mHandler.post(mReevaluateRunnable);
    }

    public boolean update() {
        final boolean settingAvoidBadWifi = "1".equals(getSettingsValue());
        final boolean prev = mAvoidBadWifi;
        mAvoidBadWifi = settingAvoidBadWifi || (!configRestrictsAvoidBadWifi());
        return mAvoidBadWifi != prev;
    }

    private class SettingObserver extends android.database.ContentObserver {
        private final android.net.Uri mUri = android.provider.Settings.Global.getUriFor(android.provider.Settings.Global.NETWORK_AVOID_BAD_WIFI);

        public SettingObserver() {
            super(null);
            final android.content.ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(mUri, false, this);
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            android.util.Slog.wtf(android.net.util.AvoidBadWifiTracker.TAG, "Should never be reached.");
        }

        @java.lang.Override
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (!mUri.equals(uri))
                return;

            reevaluate();
        }
    }
}

