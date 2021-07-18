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


class AppCompatDelegateImplV23 extends android.support.v7.app.AppCompatDelegateImplV14 {
    private final android.app.UiModeManager mUiModeManager;

    AppCompatDelegateImplV23(android.content.Context context, android.view.Window window, android.support.v7.app.AppCompatCallback callback) {
        super(context, window, callback);
        mUiModeManager = ((android.app.UiModeManager) (context.getSystemService(android.content.Context.UI_MODE_SERVICE)));
    }

    @java.lang.Override
    android.view.Window.Callback wrapWindowCallback(android.view.Window.Callback callback) {
        // Override the window callback so that we can intercept onWindowStartingActionMode(type)
        // calls
        return new android.support.v7.app.AppCompatDelegateImplV23.AppCompatWindowCallbackV23(callback);
    }

    @android.support.v7.app.AppCompatDelegate.ApplyableNightMode
    @java.lang.Override
    int mapNightMode(@android.support.v7.app.AppCompatDelegate.NightMode
    final int mode) {
        if ((mode == android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO) && (mUiModeManager.getNightMode() == android.app.UiModeManager.MODE_NIGHT_AUTO)) {
            // If we're set to AUTO and the system's auto night mode is already enabled,
            // we'll just let the system handle it by returning FOLLOW_SYSTEM
            return android.support.v7.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        return super.mapNightMode(mode);
    }

    class AppCompatWindowCallbackV23 extends android.support.v7.app.AppCompatDelegateImplV14.AppCompatWindowCallbackV14 {
        AppCompatWindowCallbackV23(android.view.Window.Callback callback) {
            super(callback);
        }

        @java.lang.Override
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int type) {
            if (isHandleNativeActionModesEnabled()) {
                switch (type) {
                    case android.view.ActionMode.TYPE_PRIMARY :
                        // We only take over if the type is TYPE_PRIMARY
                        return startAsSupportActionMode(callback);
                }
            }
            // Else, let the call fall through to the wrapped callback
            return super.onWindowStartingActionMode(callback, type);
        }

        @java.lang.Override
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
            // No-op on API 23+
            return null;
        }
    }
}

