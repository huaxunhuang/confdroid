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


public class LeanbackPreferenceDialogFragment extends android.app.Fragment {
    public static final java.lang.String ARG_KEY = "key";

    private android.support.v7.preference.DialogPreference mPreference;

    public LeanbackPreferenceDialogFragment() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v17.preference.LeanbackPreferenceFragmentTransitionHelperApi21.addTransitions(this);
        }
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.app.Fragment rawFragment = getTargetFragment();
        if (!(rawFragment instanceof android.support.v7.preference.DialogPreference.TargetFragment)) {
            throw new java.lang.IllegalStateException("Target fragment must implement TargetFragment" + " interface");
        }
        final android.support.v7.preference.DialogPreference.TargetFragment fragment = ((android.support.v7.preference.DialogPreference.TargetFragment) (rawFragment));
        final java.lang.String key = getArguments().getString(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ARG_KEY);
        mPreference = ((android.support.v7.preference.DialogPreference) (fragment.findPreference(key)));
    }

    public android.support.v7.preference.DialogPreference getPreference() {
        return mPreference;
    }
}

