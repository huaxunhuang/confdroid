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
 * This fragment provides a container for displaying a {@link LeanbackPreferenceFragment}
 *
 * <p>The following sample code shows a simple leanback preference fragment that is
 * populated from a resource.  The resource it loads is:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/res/xml/preferences.xml preferences}
 *
 * <p>The sample implements
 * {@link PreferenceFragment.OnPreferenceStartFragmentCallback#onPreferenceStartFragment(PreferenceFragment, Preference)},
 * {@link PreferenceFragment.OnPreferenceStartScreenCallback#onPreferenceStartScreen(PreferenceFragment, PreferenceScreen)},
 * and {@link #onPreferenceStartInitialScreen()}:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/src/com/example/android/supportpreference/FragmentSupportPreferencesLeanback.java
 *      support_fragment_leanback}
 */
public abstract class LeanbackSettingsFragment extends android.app.Fragment implements android.support.v14.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback , android.support.v14.preference.PreferenceFragment.OnPreferenceStartFragmentCallback , android.support.v14.preference.PreferenceFragment.OnPreferenceStartScreenCallback {
    private static final java.lang.String PREFERENCE_FRAGMENT_TAG = "android.support.v17.preference.LeanbackSettingsFragment.PREFERENCE_FRAGMENT";

    private final android.support.v17.preference.LeanbackSettingsFragment.RootViewOnKeyListener mRootViewOnKeyListener = new android.support.v17.preference.LeanbackSettingsFragment.RootViewOnKeyListener();

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        final android.view.View v = inflater.inflate(R.layout.leanback_settings_fragment, container, false);
        return v;
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            onPreferenceStartInitialScreen();
        }
    }

    @java.lang.Override
    public void onResume() {
        super.onResume();
        // Trap back button presses
        final android.support.v17.preference.LeanbackSettingsRootView rootView = ((android.support.v17.preference.LeanbackSettingsRootView) (getView()));
        if (rootView != null) {
            rootView.setOnBackKeyListener(mRootViewOnKeyListener);
        }
    }

    @java.lang.Override
    public void onPause() {
        super.onPause();
        final android.support.v17.preference.LeanbackSettingsRootView rootView = ((android.support.v17.preference.LeanbackSettingsRootView) (getView()));
        if (rootView != null) {
            rootView.setOnBackKeyListener(null);
        }
    }

    @java.lang.Override
    public boolean onPreferenceDisplayDialog(android.support.v14.preference.PreferenceFragment caller, android.support.v7.preference.Preference pref) {
        final android.app.Fragment f;
        if (pref instanceof android.support.v7.preference.ListPreference) {
            final android.support.v7.preference.ListPreference listPreference = ((android.support.v7.preference.ListPreference) (pref));
            f = android.support.v17.preference.LeanbackListPreferenceDialogFragment.newInstanceSingle(listPreference.getKey());
            f.setTargetFragment(caller, 0);
            startPreferenceFragment(f);
        } else
            if (pref instanceof android.support.v14.preference.MultiSelectListPreference) {
                android.support.v14.preference.MultiSelectListPreference listPreference = ((android.support.v14.preference.MultiSelectListPreference) (pref));
                f = android.support.v17.preference.LeanbackListPreferenceDialogFragment.newInstanceMulti(listPreference.getKey());
                f.setTargetFragment(caller, 0);
                startPreferenceFragment(f);
            } else // TODO
            // else if (pref instanceof EditTextPreference) {
            // 
            // }
            {
                return false;
            }

        return true;
    }

    /**
     * Called to instantiate the initial {@link android.support.v14.preference.PreferenceFragment}
     * to be shown in this fragment. Implementations are expected to call
     * {@link #startPreferenceFragment(android.app.Fragment)}.
     */
    public abstract void onPreferenceStartInitialScreen();

    /**
     * Displays a preference fragment to the user. This method can also be used to display
     * list-style fragments on top of the stack of preference fragments.
     *
     * @param fragment
     * 		Fragment instance to be added.
     */
    public void startPreferenceFragment(@android.support.annotation.NonNull
    android.app.Fragment fragment) {
        final android.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        final android.app.Fragment prevFragment = getChildFragmentManager().findFragmentByTag(android.support.v17.preference.LeanbackSettingsFragment.PREFERENCE_FRAGMENT_TAG);
        if (prevFragment != null) {
            transaction.addToBackStack(null).replace(R.id.settings_preference_fragment_container, fragment, android.support.v17.preference.LeanbackSettingsFragment.PREFERENCE_FRAGMENT_TAG);
        } else {
            transaction.add(R.id.settings_preference_fragment_container, fragment, android.support.v17.preference.LeanbackSettingsFragment.PREFERENCE_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    /**
     * Displays a fragment to the user, temporarily replacing the contents of this fragment.
     *
     * @param fragment
     * 		Fragment instance to be added.
     */
    public void startImmersiveFragment(@android.support.annotation.NonNull
    android.app.Fragment fragment) {
        final android.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        final android.app.Fragment preferenceFragment = getChildFragmentManager().findFragmentByTag(android.support.v17.preference.LeanbackSettingsFragment.PREFERENCE_FRAGMENT_TAG);
        if ((preferenceFragment != null) && (!preferenceFragment.isHidden())) {
            if (android.os.Build.VERSION.SDK_INT < 23) {
                // b/22631964
                transaction.add(R.id.settings_preference_fragment_container, new android.support.v17.preference.LeanbackSettingsFragment.DummyFragment());
            }
            transaction.remove(preferenceFragment);
        }
        transaction.add(R.id.settings_dialog_container, fragment).addToBackStack(null).commit();
    }

    private class RootViewOnKeyListener implements android.view.View.OnKeyListener {
        @java.lang.Override
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                return getChildFragmentManager().popBackStackImmediate();
            } else {
                return false;
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class DummyFragment extends android.app.Fragment {
        @java.lang.Override
        @android.support.annotation.Nullable
        public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
            final android.view.View v = new android.widget.Space(inflater.getContext());
            v.setVisibility(android.view.View.GONE);
            return v;
        }
    }
}

