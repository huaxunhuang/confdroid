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
package android.support.v17.preference;


/**
 * This fragment provides a fully decorated leanback-style preference fragment, including a
 * list background and header.
 *
 * <p>The following sample code shows a simple leanback preference fragment that is
 * populated from a resource.  The resource it loads is:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/res/xml/preferences.xml preferences}
 *
 * <p>The fragment needs only to implement {@link #onCreatePreferences(Bundle, String)} to populate
 * the list of preference objects:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/src/com/example/android/supportpreference/FragmentSupportPreferencesLeanback.java
 *      support_fragment_leanback}
 */
public abstract class LeanbackPreferenceFragment extends android.support.v17.preference.BaseLeanbackPreferenceFragment {
    public LeanbackPreferenceFragment() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v17.preference.LeanbackPreferenceFragmentTransitionHelperApi21.addTransitions(this);
        }
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.leanback_preference_fragment, container, false);
        final android.view.ViewGroup innerContainer = ((android.view.ViewGroup) (view.findViewById(R.id.main_frame)));
        final android.view.View innerView = super.onCreateView(inflater, innerContainer, savedInstanceState);
        if (innerView != null) {
            innerContainer.addView(innerView);
        }
        return view;
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final android.widget.TextView decorTitle = ((android.widget.TextView) (view.findViewById(R.id.decor_title)));
        decorTitle.setText(getPreferenceScreen().getTitle());
    }
}

