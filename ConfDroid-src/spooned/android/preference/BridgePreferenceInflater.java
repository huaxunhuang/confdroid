/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.preference;


public class BridgePreferenceInflater extends android.preference.PreferenceInflater {
    public BridgePreferenceInflater(android.content.Context context, android.preference.PreferenceManager preferenceManager) {
        super(context, preferenceManager);
    }

    @java.lang.Override
    public android.preference.Preference createItem(java.lang.String name, java.lang.String prefix, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        java.lang.Object viewKey = null;
        com.android.layoutlib.bridge.android.BridgeContext bc = null;
        android.content.Context context = getContext();
        if (context instanceof com.android.layoutlib.bridge.android.BridgeContext) {
            bc = ((com.android.layoutlib.bridge.android.BridgeContext) (context));
        }
        if (attrs instanceof com.android.layoutlib.bridge.android.BridgeXmlBlockParser) {
            viewKey = ((com.android.layoutlib.bridge.android.BridgeXmlBlockParser) (attrs)).getViewCookie();
        }
        android.preference.Preference preference = null;
        try {
            preference = super.createItem(name, prefix, attrs);
        } catch (java.lang.ClassNotFoundException | android.view.InflateException exception) {
            // name is probably not a valid preference type
            if (("android.support.v7.preference".equals(prefix) || "androidx.preference".equals(prefix)) && "SwitchPreferenceCompat".equals(name)) {
                preference = super.createItem("SwitchPreference", prefix, attrs);
            }
        }
        if ((viewKey != null) && (bc != null)) {
            bc.addCookie(preference, viewKey);
        }
        return preference;
    }
}

