/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */
package android.support.v7.app;


class AppCompatDelegateImplV14 extends android.support.v7.app.AppCompatDelegateImplV11 {
    private static final java.lang.String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";

    private static final boolean FLUSH_RESOURCE_CACHES_ON_NIGHT_CHANGE = true;

    @android.support.v7.app.AppCompatDelegate.NightMode
    private int mLocalNightMode = android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;

    private boolean mApplyDayNightCalled;

    private boolean mHandleNativeActionModes = true;// defaults to true


    private android.support.v7.app.AppCompatDelegateImplV14.AutoNightModeManager mAutoNightModeManager;

    AppCompatDelegateImplV14(android.content.Context context, android.view.Window window, android.support.v7.app.AppCompatCallback callback) {
        super(context, window, callback);
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && (mLocalNightMode == android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)) {
            // If we have a icicle and we haven't had a local night mode set yet, try and read
            // it from the icicle
            mLocalNightMode = savedInstanceState.getInt(android.support.v7.app.AppCompatDelegateImplV14.KEY_LOCAL_NIGHT_MODE, android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
        }
    }

    @java.lang.Override
    android.view.Window.Callback wrapWindowCallback(android.view.Window.Callback callback) {
        // Override the window callback so that we can intercept onWindowStartingActionMode()
        // calls
        return new android.support.v7.app.AppCompatDelegateImplV14.AppCompatWindowCallbackV14(callback);
    }

    @java.lang.Override
    public void setHandleNativeActionModesEnabled(boolean enabled) {
        mHandleNativeActionModes = enabled;
    }

    @java.lang.Override
    public boolean isHandleNativeActionModesEnabled() {
        return mHandleNativeActionModes;
    }

    @java.lang.Override
    public boolean applyDayNight() {
        boolean applied = false;
        @android.support.v7.app.AppCompatDelegate.NightMode
        final int nightMode = getNightMode();
        @android.support.v7.app.AppCompatDelegate.ApplyableNightMode
        final int modeToApply = mapNightMode(nightMode);
        if (modeToApply != android.support.v7.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            applied = updateForNightMode(modeToApply);
        }
        if (nightMode == android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO) {
            // If we're already been started, we may need to setup auto mode again
            ensureAutoNightModeManager();
            mAutoNightModeManager.setup();
        }
        mApplyDayNightCalled = true;
        return applied;
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        // This will apply day/night if the time has changed, it will also call through to
        // setupAutoNightModeIfNeeded()
        applyDayNight();
    }

    @java.lang.Override
    public void onStop() {
        super.onStop();
        // Make sure we clean up any receivers setup for AUTO mode
        if (mAutoNightModeManager != null) {
            mAutoNightModeManager.cleanup();
        }
    }

    @java.lang.Override
    public void setLocalNightMode(@android.support.v7.app.AppCompatDelegate.NightMode
    final int mode) {
        switch (mode) {
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO :
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO :
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES :
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM :
                if (mLocalNightMode != mode) {
                    mLocalNightMode = mode;
                    if (mApplyDayNightCalled) {
                        // If we've already applied day night, re-apply since we won't be
                        // called again
                        applyDayNight();
                    }
                }
                break;
            default :
                android.util.Log.i(android.support.v7.app.AppCompatDelegate.TAG, "setLocalNightMode() called with an unknown mode");
                break;
        }
    }

    @android.support.v7.app.AppCompatDelegate.ApplyableNightMode
    int mapNightMode(@android.support.v7.app.AppCompatDelegate.NightMode
    final int mode) {
        switch (mode) {
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO :
                ensureAutoNightModeManager();
                return mAutoNightModeManager.getApplyableNightMode();
            case android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED :
                // If we don't have a mode specified, just let the system handle it
                return android.support.v7.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            default :
                return mode;
        }
    }

    @android.support.v7.app.AppCompatDelegate.NightMode
    private int getNightMode() {
        return mLocalNightMode != android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED ? mLocalNightMode : android.support.v7.app.AppCompatDelegate.getDefaultNightMode();
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLocalNightMode != android.support.v7.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            // If we have a local night mode set, save it
            outState.putInt(android.support.v7.app.AppCompatDelegateImplV14.KEY_LOCAL_NIGHT_MODE, mLocalNightMode);
        }
    }

    @java.lang.Override
    public void onDestroy() {
        super.onDestroy();
        // Make sure we clean up any receivers setup for AUTO mode
        if (mAutoNightModeManager != null) {
            mAutoNightModeManager.cleanup();
        }
    }

    /**
     * Updates the {@link Resources} configuration {@code uiMode} with the
     * chosen {@code UI_MODE_NIGHT} value.
     */
    private boolean updateForNightMode(@android.support.v7.app.AppCompatDelegate.ApplyableNightMode
    final int mode) {
        final android.content.res.Resources res = mContext.getResources();
        final android.content.res.Configuration conf = res.getConfiguration();
        final int currentNightMode = conf.uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        final int newNightMode = (mode == android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES) ? android.content.res.Configuration.UI_MODE_NIGHT_YES : android.content.res.Configuration.UI_MODE_NIGHT_NO;
        if (currentNightMode != newNightMode) {
            if (shouldRecreateOnNightModeChange()) {
                if (android.support.v7.app.AppCompatDelegateImplBase.DEBUG) {
                    android.util.Log.d(android.support.v7.app.AppCompatDelegate.TAG, "applyNightMode() | Night mode changed, recreating Activity");
                }
                // If we've already been created, we need to recreate the Activity for the
                // mode to be applied
                final android.app.Activity activity = ((android.app.Activity) (mContext));
                activity.recreate();
            } else {
                if (android.support.v7.app.AppCompatDelegateImplBase.DEBUG) {
                    android.util.Log.d(android.support.v7.app.AppCompatDelegate.TAG, "applyNightMode() | Night mode changed, updating configuration");
                }
                final android.content.res.Configuration config = new android.content.res.Configuration(conf);
                final android.util.DisplayMetrics metrics = res.getDisplayMetrics();
                final float originalFontScale = config.fontScale;
                // Update the UI Mode to reflect the new night mode
                config.uiMode = newNightMode | (config.uiMode & (~android.content.res.Configuration.UI_MODE_NIGHT_MASK));
                if (android.support.v7.app.AppCompatDelegateImplV14.FLUSH_RESOURCE_CACHES_ON_NIGHT_CHANGE) {
                    // Set a fake font scale value to flush any resource caches
                    config.fontScale = originalFontScale * 2;
                }
                // Now update the configuration
                res.updateConfiguration(config, metrics);
                if (android.support.v7.app.AppCompatDelegateImplV14.FLUSH_RESOURCE_CACHES_ON_NIGHT_CHANGE) {
                    // If we're flushing the resources cache, revert back to the original
                    // font scale value
                    config.fontScale = originalFontScale;
                    res.updateConfiguration(config, metrics);
                }
            }
            return true;
        } else {
            if (android.support.v7.app.AppCompatDelegateImplBase.DEBUG) {
                android.util.Log.d(android.support.v7.app.AppCompatDelegate.TAG, "applyNightMode() | Night mode has not changed. Skipping");
            }
        }
        return false;
    }

    private void ensureAutoNightModeManager() {
        if (mAutoNightModeManager == null) {
            mAutoNightModeManager = new android.support.v7.app.AppCompatDelegateImplV14.AutoNightModeManager(android.support.v7.app.TwilightManager.getInstance(mContext));
        }
    }

    @android.support.annotation.VisibleForTesting
    final android.support.v7.app.AppCompatDelegateImplV14.AutoNightModeManager getAutoNightModeManager() {
        ensureAutoNightModeManager();
        return mAutoNightModeManager;
    }

    private boolean shouldRecreateOnNightModeChange() {
        if (mApplyDayNightCalled && (mContext instanceof android.app.Activity)) {
            // If we've already applyDayNight() (via setTheme), we need to check if the
            // Activity has configChanges set to handle uiMode changes
            final android.content.pm.PackageManager pm = mContext.getPackageManager();
            try {
                final android.content.pm.ActivityInfo info = pm.getActivityInfo(new android.content.ComponentName(mContext, mContext.getClass()), 0);
                // We should return true (to recreate) if configChanges does not want to
                // handle uiMode
                return (info.configChanges & android.content.pm.ActivityInfo.CONFIG_UI_MODE) == 0;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                // This shouldn't happen but let's not crash because of it, we'll just log and
                // return true (since most apps will do that anyway)
                android.util.Log.d(android.support.v7.app.AppCompatDelegate.TAG, "Exception while getting ActivityInfo", e);
                return true;
            }
        }
        return false;
    }

    class AppCompatWindowCallbackV14 extends android.support.v7.app.AppCompatDelegateImplBase.AppCompatWindowCallbackBase {
        AppCompatWindowCallbackV14(android.view.Window.Callback callback) {
            super(callback);
        }

        @java.lang.Override
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
            // We wrap in a support action mode on v14+ if enabled
            if (isHandleNativeActionModesEnabled()) {
                return startAsSupportActionMode(callback);
            }
            // Else, let the call fall through to the wrapped callback
            return super.onWindowStartingActionMode(callback);
        }

        /**
         * Wrap the framework {@link ActionMode.Callback} in a support action mode and
         * let AppCompat display it.
         */
        final android.view.ActionMode startAsSupportActionMode(android.view.ActionMode.Callback callback) {
            // Wrap the callback as a v7 ActionMode.Callback
            final android.support.v7.view.SupportActionModeWrapper.CallbackWrapper callbackWrapper = new android.support.v7.view.SupportActionModeWrapper.CallbackWrapper(mContext, callback);
            // Try and start a support action mode using the wrapped callback
            final android.support.v7.view.ActionMode supportActionMode = startSupportActionMode(callbackWrapper);
            if (supportActionMode != null) {
                // If we received a support action mode, wrap and return it
                return callbackWrapper.getActionModeWrapper(supportActionMode);
            }
            return null;
        }
    }

    @android.support.annotation.VisibleForTesting
    final class AutoNightModeManager {
        private android.support.v7.app.TwilightManager mTwilightManager;

        private boolean mIsNight;

        private android.content.BroadcastReceiver mAutoTimeChangeReceiver;

        private android.content.IntentFilter mAutoTimeChangeReceiverFilter;

        AutoNightModeManager(@android.support.annotation.NonNull
        android.support.v7.app.TwilightManager twilightManager) {
            mTwilightManager = twilightManager;
            mIsNight = twilightManager.isNight();
        }

        @android.support.v7.app.AppCompatDelegate.ApplyableNightMode
        final int getApplyableNightMode() {
            return mIsNight ? android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES : android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
        }

        final void dispatchTimeChanged() {
            final boolean isNight = mTwilightManager.isNight();
            if (isNight != mIsNight) {
                mIsNight = isNight;
                applyDayNight();
            }
        }

        final void setup() {
            cleanup();
            // If we're set to AUTO, we register a receiver to be notified on time changes. The
            // system only send the tick out every minute, but that's enough fidelity for our use
            // case
            if (mAutoTimeChangeReceiver == null) {
                mAutoTimeChangeReceiver = new android.content.BroadcastReceiver() {
                    @java.lang.Override
                    public void onReceive(android.content.Context context, android.content.Intent intent) {
                        if (android.support.v7.app.AppCompatDelegateImplBase.DEBUG) {
                            android.util.Log.d("AutoTimeChangeReceiver", "onReceive | Intent: " + intent);
                        }
                        dispatchTimeChanged();
                    }
                };
            }
            if (mAutoTimeChangeReceiverFilter == null) {
                mAutoTimeChangeReceiverFilter = new android.content.IntentFilter();
                mAutoTimeChangeReceiverFilter.addAction(android.content.Intent.ACTION_TIME_CHANGED);
                mAutoTimeChangeReceiverFilter.addAction(android.content.Intent.ACTION_TIMEZONE_CHANGED);
                mAutoTimeChangeReceiverFilter.addAction(android.content.Intent.ACTION_TIME_TICK);
            }
            mContext.registerReceiver(mAutoTimeChangeReceiver, mAutoTimeChangeReceiverFilter);
        }

        final void cleanup() {
            if (mAutoTimeChangeReceiver != null) {
                mContext.unregisterReceiver(mAutoTimeChangeReceiver);
                mAutoTimeChangeReceiver = null;
            }
        }
    }
}

