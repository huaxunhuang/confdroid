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


/**
 * Delegate that provides implementation for native methods in {@link Preference}
 * <p/>
 * Through the layoutlib_create tool, selected methods of Preference have been replaced by calls to
 * methods of the same name in this delegate class.
 */
public class Preference_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.view.View getView(android.preference.Preference pref, android.view.View convertView, android.view.ViewGroup parent) {
        android.content.Context context = pref.getContext();
        com.android.layoutlib.bridge.android.BridgeContext bc = (context instanceof com.android.layoutlib.bridge.android.BridgeContext) ? ((com.android.layoutlib.bridge.android.BridgeContext) (context)) : null;
        convertView = pref.getView_Original(convertView, parent);
        if (bc != null) {
            java.lang.Object cookie = bc.getCookie(pref);
            if (cookie != null) {
                bc.addViewKey(convertView, cookie);
            }
        }
        return convertView;
    }

    /**
     * Inflates the parser and returns the ListView containing the Preferences.
     */
    public static android.view.View inflatePreference(android.content.Context context, org.xmlpull.v1.XmlPullParser parser, android.view.ViewGroup root) {
        android.preference.PreferenceManager pm = new android.preference.PreferenceManager(context);
        android.preference.PreferenceInflater inflater = new android.preference.BridgePreferenceInflater(context, pm);
        android.preference.PreferenceScreen ps = ((android.preference.PreferenceScreen) (inflater.inflate(parser, null, true)));
        pm.setPreferences(ps);
        android.widget.ListView preferenceView = android.preference.Preference_Delegate.createContainerView(context, root);
        ps.bind(preferenceView);
        return preferenceView;
    }

    private static android.widget.ListView createContainerView(android.content.Context context, android.view.ViewGroup root) {
        android.content.res.TypedArray a = context.obtainStyledAttributes(null, R.styleable.PreferenceFragment, R.attr.preferenceFragmentStyle, 0);
        int mLayoutResId = a.getResourceId(R.styleable.PreferenceFragment_layout, R.layout.preference_list_fragment);
        a.recycle();
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        inflater.inflate(mLayoutResId, root, true);
        return ((android.widget.ListView) (root.findViewById(android.R.id.list)));
    }
}

