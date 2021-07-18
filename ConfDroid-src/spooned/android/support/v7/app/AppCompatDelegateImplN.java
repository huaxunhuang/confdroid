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
 * limitations under the License
 */
package android.support.v7.app;


class AppCompatDelegateImplN extends android.support.v7.app.AppCompatDelegateImplV23 {
    AppCompatDelegateImplN(android.content.Context context, android.view.Window window, android.support.v7.app.AppCompatCallback callback) {
        super(context, window, callback);
    }

    @java.lang.Override
    android.view.Window.Callback wrapWindowCallback(android.view.Window.Callback callback) {
        return new android.support.v7.app.AppCompatDelegateImplN.AppCompatWindowCallbackN(callback);
    }

    class AppCompatWindowCallbackN extends android.support.v7.app.AppCompatDelegateImplV23.AppCompatWindowCallbackV23 {
        AppCompatWindowCallbackN(android.view.Window.Callback callback) {
            super(callback);
        }

        @java.lang.Override
        public void onProvideKeyboardShortcuts(java.util.List<android.view.KeyboardShortcutGroup> data, android.view.Menu menu, int deviceId) {
            final android.support.v7.app.AppCompatDelegateImplV9.PanelFeatureState panel = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, true);
            if ((panel != null) && (panel.menu != null)) {
                // The menu provided is one created by PhoneWindow which we don't actually use.
                // Instead we'll pass through our own...
                super.onProvideKeyboardShortcuts(data, panel.menu, deviceId);
            } else {
                // If we don't have a menu, jump pass through the original instead
                super.onProvideKeyboardShortcuts(data, menu, deviceId);
            }
        }
    }
}

