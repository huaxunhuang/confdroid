/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Shows a hierarchy of {@link Preference} objects as
 * lists. These preferences will
 * automatically save to {@link SharedPreferences} as the user interacts with
 * them. To retrieve an instance of {@link SharedPreferences} that the
 * preference hierarchy in this fragment will use, call
 * {@link PreferenceManager#getDefaultSharedPreferences(android.content.Context)}
 * with a context in the same package as this fragment.
 * <p>
 * Furthermore, the preferences shown will follow the visual style of system
 * preferences. It is easy to create a hierarchy of preferences (that can be
 * shown on multiple screens) via XML. For these reasons, it is recommended to
 * use this fragment (as a superclass) to deal with preferences in applications.
 * <p>
 * A {@link PreferenceScreen} object should be at the top of the preference
 * hierarchy. Furthermore, subsequent {@link PreferenceScreen} in the hierarchy
 * denote a screen break--that is the preferences contained within subsequent
 * {@link PreferenceScreen} should be shown on another screen. The preference
 * framework handles showing these other screens from the preference hierarchy.
 * <p>
 * The preference hierarchy can be formed in multiple ways:
 * <li> From an XML file specifying the hierarchy
 * <li> From different {@link Activity Activities} that each specify its own
 * preferences in an XML file via {@link Activity} meta-data
 * <li> From an object hierarchy rooted with {@link PreferenceScreen}
 * <p>
 * To inflate from XML, use the {@link #addPreferencesFromResource(int)}. The
 * root element should be a {@link PreferenceScreen}. Subsequent elements can point
 * to actual {@link Preference} subclasses. As mentioned above, subsequent
 * {@link PreferenceScreen} in the hierarchy will result in the screen break.
 * <p>
 * To specify an {@link Intent} to query {@link Activity Activities} that each
 * have preferences, use {@link #addPreferencesFromIntent}. Each
 * {@link Activity} can specify meta-data in the manifest (via the key
 * {@link PreferenceManager#METADATA_KEY_PREFERENCES}) that points to an XML
 * resource. These XML resources will be inflated into a single preference
 * hierarchy and shown by this fragment.
 * <p>
 * To specify an object hierarchy rooted with {@link PreferenceScreen}, use
 * {@link #setPreferenceScreen(PreferenceScreen)}.
 * <p>
 * As a convenience, this fragment implements a click listener for any
 * preference in the current hierarchy, see
 * {@link #onPreferenceTreeClick(PreferenceScreen, Preference)}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using {@code PreferenceFragment},
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @see Preference
 * @see PreferenceScreen
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public abstract class PreferenceFragment extends android.app.Fragment implements android.preference.PreferenceManager.OnPreferenceTreeClickListener {
    private static final java.lang.String PREFERENCES_TAG = "android:preferences";

    @android.annotation.UnsupportedAppUsage
    private android.preference.PreferenceManager mPreferenceManager;

    private android.widget.ListView mList;

    private boolean mHavePrefs;

    private boolean mInitDone;

    private int mLayoutResId = com.android.internal.R.layout.preference_list_fragment;

    /**
     * The starting request code given out to preference framework.
     */
    private static final int FIRST_REQUEST_CODE = 100;

    private static final int MSG_BIND_PREFERENCES = 1;

    private android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.preference.PreferenceFragment.MSG_BIND_PREFERENCES :
                    bindPreferences();
                    break;
            }
        }
    };

    private final java.lang.Runnable mRequestFocus = new java.lang.Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * Interface that PreferenceFragment's containing activity should
     * implement to be able to process preference items that wish to
     * switch to a new fragment.
     *
     * @deprecated Use {@link android.support.v7.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback}
     */
    @java.lang.Deprecated
    public interface OnPreferenceStartFragmentCallback {
        /**
         * Called when the user has clicked on a Preference that has
         * a fragment class name associated with it.  The implementation
         * to should instantiate and switch to an instance of the given
         * fragment.
         */
        boolean onPreferenceStartFragment(android.preference.PreferenceFragment caller, android.preference.Preference pref);
    }

    @java.lang.Override
    public void onCreate(@android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceManager = new android.preference.PreferenceManager(getActivity(), android.preference.PreferenceFragment.FIRST_REQUEST_CODE);
        mPreferenceManager.setFragment(this);
    }

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, @android.annotation.Nullable
    android.view.ViewGroup container, @android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        android.content.res.TypedArray a = getActivity().obtainStyledAttributes(null, com.android.internal.R.styleable.PreferenceFragment, com.android.internal.R.attr.preferenceFragmentStyle, 0);
        mLayoutResId = a.getResourceId(com.android.internal.R.styleable.PreferenceFragment_layout, mLayoutResId);
        a.recycle();
        return inflater.inflate(mLayoutResId, container, false);
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, @android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.content.res.TypedArray a = getActivity().obtainStyledAttributes(null, com.android.internal.R.styleable.PreferenceFragment, com.android.internal.R.attr.preferenceFragmentStyle, 0);
        android.widget.ListView lv = ((android.widget.ListView) (view.findViewById(android.R.id.list)));
        if ((lv != null) && a.hasValueOrEmpty(com.android.internal.R.styleable.PreferenceFragment_divider)) {
            lv.setDivider(a.getDrawable(com.android.internal.R.styleable.PreferenceFragment_divider));
        }
        a.recycle();
    }

    @java.lang.Override
    public void onActivityCreated(@android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHavePrefs) {
            bindPreferences();
        }
        mInitDone = true;
        if (savedInstanceState != null) {
            android.os.Bundle container = savedInstanceState.getBundle(android.preference.PreferenceFragment.PREFERENCES_TAG);
            if (container != null) {
                final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }
    }

    @java.lang.Override
    public void onStart() {
        onStart();
        mPreferenceManager.setOnPreferenceTreeClickListener(this);
    }

    @java.lang.Override
    public void onStop() {
        onStop();
        mPreferenceManager.dispatchActivityStop();
        mPreferenceManager.setOnPreferenceTreeClickListener(null);
    }

    @java.lang.Override
    public void onDestroyView() {
        if (mList != null) {
            mList.setOnKeyListener(null);
        }
        mList = null;
        mHandler.removeCallbacks(mRequestFocus);
        mHandler.removeMessages(android.preference.PreferenceFragment.MSG_BIND_PREFERENCES);
        onDestroyView();
    }

    @java.lang.Override
    public void onDestroy() {
        onDestroy();
        mPreferenceManager.dispatchActivityDestroy();
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            android.os.Bundle container = new android.os.Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(android.preference.PreferenceFragment.PREFERENCES_TAG, container);
        }
    }

    @java.lang.Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        onActivityResult(requestCode, resultCode, data);
        mPreferenceManager.dispatchActivityResult(requestCode, resultCode, data);
    }

    /**
     * Returns the {@link PreferenceManager} used by this fragment.
     *
     * @return The {@link PreferenceManager}.
     */
    public android.preference.PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    /**
     * Sets the root of the preference hierarchy that this fragment is showing.
     *
     * @param preferenceScreen
     * 		The root {@link PreferenceScreen} of the preference hierarchy.
     */
    public void setPreferenceScreen(android.preference.PreferenceScreen preferenceScreen) {
        if (mPreferenceManager.setPreferences(preferenceScreen) && (preferenceScreen != null)) {
            onUnbindPreferences();
            mHavePrefs = true;
            if (mInitDone) {
                postBindPreferences();
            }
        }
    }

    /**
     * Gets the root of the preference hierarchy that this fragment is showing.
     *
     * @return The {@link PreferenceScreen} that is the root of the preference
    hierarchy.
     */
    public android.preference.PreferenceScreen getPreferenceScreen() {
        return mPreferenceManager.getPreferenceScreen();
    }

    /**
     * Adds preferences from activities that match the given {@link Intent}.
     *
     * @param intent
     * 		The {@link Intent} to query activities.
     */
    public void addPreferencesFromIntent(android.content.Intent intent) {
        requirePreferenceManager();
        setPreferenceScreen(mPreferenceManager.inflateFromIntent(intent, getPreferenceScreen()));
    }

    /**
     * Inflates the given XML resource and adds the preference hierarchy to the current
     * preference hierarchy.
     *
     * @param preferencesResId
     * 		The XML resource ID to inflate.
     */
    public void addPreferencesFromResource(@android.annotation.XmlRes
    int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(mPreferenceManager.inflateFromResource(getActivity(), preferencesResId, getPreferenceScreen()));
    }

    /**
     * {@inheritDoc }
     */
    public boolean onPreferenceTreeClick(android.preference.PreferenceScreen preferenceScreen, android.preference.Preference preference) {
        if ((preference.getFragment() != null) && (getActivity() instanceof android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback)) {
            return ((android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback) (getActivity())).onPreferenceStartFragment(this, preference);
        }
        return false;
    }

    /**
     * Finds a {@link Preference} based on its key.
     *
     * @param key
     * 		The key of the preference to retrieve.
     * @return The {@link Preference} with the key, or null.
     * @see PreferenceGroup#findPreference(CharSequence)
     */
    public android.preference.Preference findPreference(java.lang.CharSequence key) {
        if (mPreferenceManager == null) {
            return null;
        }
        return mPreferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (mPreferenceManager == null) {
            throw new java.lang.RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (mHandler.hasMessages(android.preference.PreferenceFragment.MSG_BIND_PREFERENCES))
            return;

        mHandler.obtainMessage(android.preference.PreferenceFragment.MSG_BIND_PREFERENCES).sendToTarget();
    }

    private void bindPreferences() {
        final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            android.view.View root = getView();
            if (root != null) {
                android.view.View titleView = root.findViewById(android.R.id.title);
                if (titleView instanceof android.widget.TextView) {
                    java.lang.CharSequence title = preferenceScreen.getTitle();
                    if (android.text.TextUtils.isEmpty(title)) {
                        titleView.setVisibility(android.view.View.GONE);
                    } else {
                        ((android.widget.TextView) (titleView)).setText(title);
                        titleView.setVisibility(android.view.View.VISIBLE);
                    }
                }
            }
            preferenceScreen.bind(getListView());
        }
        onBindPreferences();
    }

    /**
     *
     *
     * @unknown 
     */
    protected void onBindPreferences() {
    }

    /**
     *
     *
     * @unknown 
     */
    protected void onUnbindPreferences() {
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.widget.ListView getListView() {
        ensureList();
        return mList;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasListView() {
        if (mList != null) {
            return true;
        }
        android.view.View root = getView();
        if (root == null) {
            return false;
        }
        android.view.View rawListView = root.findViewById(android.R.id.list);
        if (!(rawListView instanceof android.widget.ListView)) {
            return false;
        }
        mList = ((android.widget.ListView) (rawListView));
        if (mList == null) {
            return false;
        }
        return true;
    }

    private void ensureList() {
        if (mList != null) {
            return;
        }
        android.view.View root = getView();
        if (root == null) {
            throw new java.lang.IllegalStateException("Content view not yet created");
        }
        android.view.View rawListView = root.findViewById(android.R.id.list);
        if (!(rawListView instanceof android.widget.ListView)) {
            throw new java.lang.RuntimeException("Content has view with id attribute 'android.R.id.list' " + "that is not a ListView class");
        }
        mList = ((android.widget.ListView) (rawListView));
        if (mList == null) {
            throw new java.lang.RuntimeException("Your content must have a ListView whose id attribute is " + "'android.R.id.list'");
        }
        mList.setOnKeyListener(mListOnKeyListener);
        mHandler.post(mRequestFocus);
    }

    private android.view.View.OnKeyListener mListOnKeyListener = new android.view.View.OnKeyListener() {
        @java.lang.Override
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            java.lang.Object selectedItem = mList.getSelectedItem();
            if (selectedItem instanceof android.preference.Preference) {
                android.view.View selectedView = mList.getSelectedView();
                return ((android.preference.Preference) (selectedItem)).onKey(selectedView, keyCode, event);
            }
            return false;
        }
    };
}

