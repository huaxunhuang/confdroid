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
package android.support.v14.preference;


/**
 * Shows a hierarchy of {@link Preference} objects as
 * lists. These preferences will
 * automatically save to {@link android.content.SharedPreferences} as the user interacts with
 * them. To retrieve an instance of {@link android.content.SharedPreferences} that the
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
 * framework handles this by calling {@link #onNavigateToScreen(PreferenceScreen)}.
 * <p>
 * The preference hierarchy can be formed in multiple ways:
 * <li> From an XML file specifying the hierarchy
 * <li> From different {@link android.app.Activity Activities} that each specify its own
 * preferences in an XML file via {@link android.app.Activity} meta-data
 * <li> From an object hierarchy rooted with {@link PreferenceScreen}
 * <p>
 * To inflate from XML, use the {@link #addPreferencesFromResource(int)}. The
 * root element should be a {@link PreferenceScreen}. Subsequent elements can point
 * to actual {@link Preference} subclasses. As mentioned above, subsequent
 * {@link PreferenceScreen} in the hierarchy will result in the screen break.
 * <p>
 * To specify an object hierarchy rooted with {@link PreferenceScreen}, use
 * {@link #setPreferenceScreen(PreferenceScreen)}.
 * <p>
 * As a convenience, this fragment implements a click listener for any
 * preference in the current hierarchy, see
 * {@link #onPreferenceTreeClick(Preference)}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using {@code PreferenceFragment},
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * <a name="SampleCode"></a>
 * <h3>Sample Code</h3>
 *
 * <p>The following sample code shows a simple preference fragment that is
 * populated from a resource.  The resource it loads is:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/res/xml/preferences.xml preferences}
 *
 * <p>The fragment implementation itself simply populates the preferences
 * when created.  Note that the preferences framework takes care of loading
 * the current values out of the app preferences and writing them when changed:</p>
 *
 * {@sample frameworks/support/samples/SupportPreferenceDemos/src/com/example/android/supportpreference/FragmentSupportPreferences.java
 *      support_fragment}
 *
 * @see Preference
 * @see PreferenceScreen
 */
public abstract class PreferenceFragment extends android.app.Fragment implements android.support.v7.preference.DialogPreference.TargetFragment , android.support.v7.preference.PreferenceManager.OnDisplayPreferenceDialogListener , android.support.v7.preference.PreferenceManager.OnNavigateToScreenListener , android.support.v7.preference.PreferenceManager.OnPreferenceTreeClickListener {
    /**
     * Fragment argument used to specify the tag of the desired root
     * {@link android.support.v7.preference.PreferenceScreen} object.
     */
    public static final java.lang.String ARG_PREFERENCE_ROOT = "android.support.v7.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";

    private static final java.lang.String PREFERENCES_TAG = "android:preferences";

    private static final java.lang.String DIALOG_FRAGMENT_TAG = "android.support.v14.preference.PreferenceFragment.DIALOG";

    private android.support.v7.preference.PreferenceManager mPreferenceManager;

    private android.support.v7.widget.RecyclerView mList;

    private boolean mHavePrefs;

    private boolean mInitDone;

    private android.content.Context mStyledContext;

    private int mLayoutResId = android.support.v7.preference.R.layout.preference_list_fragment;

    private final android.support.v14.preference.PreferenceFragment.DividerDecoration mDividerDecoration = new android.support.v14.preference.PreferenceFragment.DividerDecoration();

    private static final int MSG_BIND_PREFERENCES = 1;

    private android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.support.v14.preference.PreferenceFragment.MSG_BIND_PREFERENCES :
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

    private java.lang.Runnable mSelectPreferenceRunnable;

    /**
     * Interface that PreferenceFragment's containing activity should
     * implement to be able to process preference items that wish to
     * switch to a specified fragment.
     */
    public interface OnPreferenceStartFragmentCallback {
        /**
         * Called when the user has clicked on a Preference that has
         * a fragment class name associated with it.  The implementation
         * should instantiate and switch to an instance of the given
         * fragment.
         *
         * @param caller
         * 		The fragment requesting navigation.
         * @param pref
         * 		The preference requesting the fragment.
         * @return true if the fragment creation has been handled
         */
        boolean onPreferenceStartFragment(android.support.v14.preference.PreferenceFragment caller, android.support.v7.preference.Preference pref);
    }

    /**
     * Interface that PreferenceFragment's containing activity should
     * implement to be able to process preference items that wish to
     * switch to a new screen of preferences.
     */
    public interface OnPreferenceStartScreenCallback {
        /**
         * Called when the user has clicked on a PreferenceScreen item in order to navigate to a new
         * screen of preferences.
         *
         * @param caller
         * 		The fragment requesting navigation.
         * @param pref
         * 		The preference screen to navigate to.
         * @return true if the screen navigation has been handled
         */
        boolean onPreferenceStartScreen(android.support.v14.preference.PreferenceFragment caller, android.support.v7.preference.PreferenceScreen pref);
    }

    public interface OnPreferenceDisplayDialogCallback {
        /**
         *
         *
         * @param caller
         * 		The fragment containing the preference requesting the dialog.
         * @param pref
         * 		The preference requesting the dialog.
         * @return true if the dialog creation has been handled.
         */
        boolean onPreferenceDisplayDialog(android.support.v14.preference.PreferenceFragment caller, android.support.v7.preference.Preference pref);
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.util.TypedValue tv = new android.util.TypedValue();
        getActivity().getTheme().resolveAttribute(android.support.v7.preference.R.attr.preferenceTheme, tv, true);
        final int theme = tv.resourceId;
        if (theme <= 0) {
            throw new java.lang.IllegalStateException("Must specify preferenceTheme in theme");
        }
        mStyledContext = new android.view.ContextThemeWrapper(getActivity(), theme);
        mPreferenceManager = new android.support.v7.preference.PreferenceManager(mStyledContext);
        mPreferenceManager.setOnNavigateToScreenListener(this);
        final android.os.Bundle args = getArguments();
        final java.lang.String rootKey;
        if (args != null) {
            rootKey = getArguments().getString(android.support.v14.preference.PreferenceFragment.ARG_PREFERENCE_ROOT);
        } else {
            rootKey = null;
        }
        onCreatePreferences(savedInstanceState, rootKey);
    }

    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState
     * 		If the fragment is being re-created from
     * 		a previous saved state, this is the state.
     * @param rootKey
     * 		If non-null, this preference fragment should be rooted at the
     * 		{@link android.support.v7.preference.PreferenceScreen} with this key.
     */
    public abstract void onCreatePreferences(android.os.Bundle savedInstanceState, java.lang.String rootKey);

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.content.res.TypedArray a = mStyledContext.obtainStyledAttributes(null, R.styleable.PreferenceFragment, android.support.v4.content.res.TypedArrayUtils.getAttr(mStyledContext, android.support.v7.preference.R.attr.preferenceFragmentStyle, android.support.v7.preference.AndroidResources.ANDROID_R_PREFERENCE_FRAGMENT_STYLE), 0);
        mLayoutResId = a.getResourceId(R.styleable.PreferenceFragment_android_layout, mLayoutResId);
        final android.graphics.drawable.Drawable divider = a.getDrawable(R.styleable.PreferenceFragment_android_divider);
        final int dividerHeight = a.getDimensionPixelSize(R.styleable.PreferenceFragment_android_dividerHeight, -1);
        a.recycle();
        // Need to theme the inflater to pick up the preferenceFragmentListStyle
        final android.util.TypedValue tv = new android.util.TypedValue();
        getActivity().getTheme().resolveAttribute(android.support.v7.preference.R.attr.preferenceTheme, tv, true);
        final int theme = tv.resourceId;
        final android.content.Context themedContext = new android.view.ContextThemeWrapper(inflater.getContext(), theme);
        final android.view.LayoutInflater themedInflater = inflater.cloneInContext(themedContext);
        final android.view.View view = themedInflater.inflate(mLayoutResId, container, false);
        final android.view.View rawListContainer = view.findViewById(android.support.v7.preference.AndroidResources.ANDROID_R_LIST_CONTAINER);
        if (!(rawListContainer instanceof android.view.ViewGroup)) {
            throw new java.lang.RuntimeException("Content has view with id attribute " + "'android.R.id.list_container' that is not a ViewGroup class");
        }
        final android.view.ViewGroup listContainer = ((android.view.ViewGroup) (rawListContainer));
        final android.support.v7.widget.RecyclerView listView = onCreateRecyclerView(themedInflater, listContainer, savedInstanceState);
        if (listView == null) {
            throw new java.lang.RuntimeException("Could not create RecyclerView");
        }
        mList = listView;
        listView.addItemDecoration(mDividerDecoration);
        setDivider(divider);
        if (dividerHeight != (-1)) {
            setDividerHeight(dividerHeight);
        }
        listContainer.addView(mList);
        mHandler.post(mRequestFocus);
        return view;
    }

    /**
     * Sets the drawable that will be drawn between each item in the list.
     * <p>
     * <strong>Note:</strong> If the drawable does not have an intrinsic
     * height, you should also call {@link #setDividerHeight(int)}.
     *
     * @param divider
     * 		the drawable to use
     * @unknown ref R.styleable#PreferenceFragment_android_divider
     */
    public void setDivider(android.graphics.drawable.Drawable divider) {
        mDividerDecoration.setDivider(divider);
    }

    /**
     * Sets the height of the divider that will be drawn between each item in the list. Calling
     * this will override the intrinsic height as set by {@link #setDivider(Drawable)}
     *
     * @param height
     * 		The new height of the divider in pixels.
     * @unknown ref R.styleable#PreferenceFragment_android_dividerHeight
     */
    public void setDividerHeight(int height) {
        mDividerDecoration.setDividerHeight(height);
    }

    @java.lang.Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mHavePrefs) {
            bindPreferences();
            if (mSelectPreferenceRunnable != null) {
                mSelectPreferenceRunnable.run();
                mSelectPreferenceRunnable = null;
            }
        }
        mInitDone = true;
    }

    @java.lang.Override
    public void onActivityCreated(android.os.Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            android.os.Bundle container = savedInstanceState.getBundle(android.support.v14.preference.PreferenceFragment.PREFERENCES_TAG);
            if (container != null) {
                final android.support.v7.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        mPreferenceManager.setOnPreferenceTreeClickListener(this);
        mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    @java.lang.Override
    public void onStop() {
        super.onStop();
        mPreferenceManager.setOnPreferenceTreeClickListener(null);
        mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }

    @java.lang.Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRequestFocus);
        mHandler.removeMessages(android.support.v14.preference.PreferenceFragment.MSG_BIND_PREFERENCES);
        if (mHavePrefs) {
            unbindPreferences();
        }
        mList = null;
        super.onDestroyView();
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        final android.support.v7.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            android.os.Bundle container = new android.os.Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(android.support.v14.preference.PreferenceFragment.PREFERENCES_TAG, container);
        }
    }

    /**
     * Returns the {@link PreferenceManager} used by this fragment.
     *
     * @return The {@link PreferenceManager}.
     */
    public android.support.v7.preference.PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    /**
     * Sets the root of the preference hierarchy that this fragment is showing.
     *
     * @param preferenceScreen
     * 		The root {@link PreferenceScreen} of the preference hierarchy.
     */
    public void setPreferenceScreen(android.support.v7.preference.PreferenceScreen preferenceScreen) {
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
    public android.support.v7.preference.PreferenceScreen getPreferenceScreen() {
        return mPreferenceManager.getPreferenceScreen();
    }

    /**
     * Inflates the given XML resource and adds the preference hierarchy to the current
     * preference hierarchy.
     *
     * @param preferencesResId
     * 		The XML resource ID to inflate.
     */
    public void addPreferencesFromResource(@android.support.annotation.XmlRes
    int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(mPreferenceManager.inflateFromResource(mStyledContext, preferencesResId, getPreferenceScreen()));
    }

    /**
     * Inflates the given XML resource and replaces the current preference hierarchy (if any) with
     * the preference hierarchy rooted at {@code key}.
     *
     * @param preferencesResId
     * 		The XML resource ID to inflate.
     * @param key
     * 		The preference key of the {@link android.support.v7.preference.PreferenceScreen}
     * 		to use as the root of the preference hierarchy, or null to use the root
     * 		{@link android.support.v7.preference.PreferenceScreen}.
     */
    public void setPreferencesFromResource(@android.support.annotation.XmlRes
    int preferencesResId, @android.support.annotation.Nullable
    java.lang.String key) {
        requirePreferenceManager();
        final android.support.v7.preference.PreferenceScreen xmlRoot = mPreferenceManager.inflateFromResource(mStyledContext, preferencesResId, null);
        final android.support.v7.preference.Preference root;
        if (key != null) {
            root = xmlRoot.findPreference(key);
            if (!(root instanceof android.support.v7.preference.PreferenceScreen)) {
                throw new java.lang.IllegalArgumentException(("Preference object with key " + key) + " is not a PreferenceScreen");
            }
        } else {
            root = xmlRoot;
        }
        setPreferenceScreen(((android.support.v7.preference.PreferenceScreen) (root)));
    }

    /**
     * {@inheritDoc }
     */
    public boolean onPreferenceTreeClick(android.support.v7.preference.Preference preference) {
        if (preference.getFragment() != null) {
            boolean handled = false;
            if (getCallbackFragment() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceStartFragmentCallback) {
                handled = ((android.support.v14.preference.PreferenceFragment.OnPreferenceStartFragmentCallback) (getCallbackFragment())).onPreferenceStartFragment(this, preference);
            }
            if ((!handled) && (getActivity() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceStartFragmentCallback)) {
                handled = ((android.support.v14.preference.PreferenceFragment.OnPreferenceStartFragmentCallback) (getActivity())).onPreferenceStartFragment(this, preference);
            }
            return handled;
        }
        return false;
    }

    /**
     * Called by
     * {@link android.support.v7.preference.PreferenceScreen#onClick()} in order to navigate to a
     * new screen of preferences. Calls
     * {@link PreferenceFragment.OnPreferenceStartScreenCallback#onPreferenceStartScreen}
     * if the target fragment or containing activity implements
     * {@link PreferenceFragment.OnPreferenceStartScreenCallback}.
     *
     * @param preferenceScreen
     * 		The {@link android.support.v7.preference.PreferenceScreen} to
     * 		navigate to.
     */
    @java.lang.Override
    public void onNavigateToScreen(android.support.v7.preference.PreferenceScreen preferenceScreen) {
        boolean handled = false;
        if (getCallbackFragment() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceStartScreenCallback) {
            handled = ((android.support.v14.preference.PreferenceFragment.OnPreferenceStartScreenCallback) (getCallbackFragment())).onPreferenceStartScreen(this, preferenceScreen);
        }
        if ((!handled) && (getActivity() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceStartScreenCallback)) {
            ((android.support.v14.preference.PreferenceFragment.OnPreferenceStartScreenCallback) (getActivity())).onPreferenceStartScreen(this, preferenceScreen);
        }
    }

    /**
     * Finds a {@link Preference} based on its key.
     *
     * @param key
     * 		The key of the preference to retrieve.
     * @return The {@link Preference} with the key, or null.
     * @see android.support.v7.preference.PreferenceGroup#findPreference(CharSequence)
     */
    public android.support.v7.preference.Preference findPreference(java.lang.CharSequence key) {
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
        if (mHandler.hasMessages(android.support.v14.preference.PreferenceFragment.MSG_BIND_PREFERENCES))
            return;

        mHandler.obtainMessage(android.support.v14.preference.PreferenceFragment.MSG_BIND_PREFERENCES).sendToTarget();
    }

    private void bindPreferences() {
        final android.support.v7.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            getListView().setAdapter(onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        onBindPreferences();
    }

    private void unbindPreferences() {
        final android.support.v7.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        onUnbindPreferences();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected void onBindPreferences() {
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected void onUnbindPreferences() {
    }

    public final android.support.v7.widget.RecyclerView getListView() {
        return mList;
    }

    /**
     * Creates the {@link android.support.v7.widget.RecyclerView} used to display the preferences.
     * Subclasses may override this to return a customized
     * {@link android.support.v7.widget.RecyclerView}.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate the
     * 		{@link android.support.v7.widget.RecyclerView}.
     * @param parent
     * 		The parent {@link android.view.View} that the RecyclerView will be attached to.
     * 		This method should not add the view itself, but this can be used to generate
     * 		the LayoutParams of the view.
     * @param savedInstanceState
     * 		If non-null, this view is being re-constructed from a previous
     * 		saved state as given here
     * @return A new RecyclerView object to be placed into the view hierarchy
     */
    public android.support.v7.widget.RecyclerView onCreateRecyclerView(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.os.Bundle savedInstanceState) {
        android.support.v7.widget.RecyclerView recyclerView = ((android.support.v7.widget.RecyclerView) (inflater.inflate(android.support.v7.preference.R.layout.preference_recyclerview, parent, false)));
        recyclerView.setLayoutManager(onCreateLayoutManager());
        recyclerView.setAccessibilityDelegateCompat(new android.support.v7.preference.PreferenceRecyclerViewAccessibilityDelegate(recyclerView));
        return recyclerView;
    }

    /**
     * Called from {@link #onCreateRecyclerView} to create the
     * {@link android.support.v7.widget.RecyclerView.LayoutManager} for the created
     * {@link android.support.v7.widget.RecyclerView}.
     *
     * @return A new {@link android.support.v7.widget.RecyclerView.LayoutManager} instance.
     */
    public android.support.v7.widget.RecyclerView.LayoutManager onCreateLayoutManager() {
        return new android.support.v7.widget.LinearLayoutManager(getActivity());
    }

    /**
     * Creates the root adapter.
     *
     * @param preferenceScreen
     * 		Preference screen object to create the adapter for.
     * @return An adapter that contains the preferences contained in this {@link PreferenceScreen}.
     */
    protected android.support.v7.widget.RecyclerView.Adapter onCreateAdapter(android.support.v7.preference.PreferenceScreen preferenceScreen) {
        return new android.support.v7.preference.PreferenceGroupAdapter(preferenceScreen);
    }

    /**
     * Called when a preference in the tree requests to display a dialog. Subclasses should
     * override this method to display custom dialogs or to handle dialogs for custom preference
     * classes.
     *
     * @param preference
     * 		The Preference object requesting the dialog.
     */
    @java.lang.Override
    public void onDisplayPreferenceDialog(android.support.v7.preference.Preference preference) {
        boolean handled = false;
        if (getCallbackFragment() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback) {
            handled = ((android.support.v14.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback) (getCallbackFragment())).onPreferenceDisplayDialog(this, preference);
        }
        if ((!handled) && (getActivity() instanceof android.support.v14.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback)) {
            handled = ((android.support.v14.preference.PreferenceFragment.OnPreferenceDisplayDialogCallback) (getActivity())).onPreferenceDisplayDialog(this, preference);
        }
        if (handled) {
            return;
        }
        // check if dialog is already showing
        if (getFragmentManager().findFragmentByTag(android.support.v14.preference.PreferenceFragment.DIALOG_FRAGMENT_TAG) != null) {
            return;
        }
        final android.app.DialogFragment f;
        if (preference instanceof android.support.v7.preference.EditTextPreference) {
            f = android.support.v14.preference.EditTextPreferenceDialogFragment.newInstance(preference.getKey());
        } else
            if (preference instanceof android.support.v7.preference.ListPreference) {
                f = android.support.v14.preference.ListPreferenceDialogFragment.newInstance(preference.getKey());
            } else
                if (preference instanceof android.support.v14.preference.MultiSelectListPreference) {
                    f = android.support.v14.preference.MultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
                } else {
                    throw new java.lang.IllegalArgumentException("Tried to display dialog for unknown " + "preference type. Did you forget to override onDisplayPreferenceDialog()?");
                }


        f.setTargetFragment(this, 0);
        f.show(getFragmentManager(), android.support.v14.preference.PreferenceFragment.DIALOG_FRAGMENT_TAG);
    }

    /**
     * Basically a wrapper for getParentFragment which is v17+. Used by the leanback preference lib.
     *
     * @return Fragment to possibly use as a callback
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public android.app.Fragment getCallbackFragment() {
        return null;
    }

    public void scrollToPreference(final java.lang.String key) {
        scrollToPreferenceInternal(null, key);
    }

    public void scrollToPreference(final android.support.v7.preference.Preference preference) {
        scrollToPreferenceInternal(preference, null);
    }

    private void scrollToPreferenceInternal(final android.support.v7.preference.Preference preference, final java.lang.String key) {
        final java.lang.Runnable r = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                final android.support.v7.widget.RecyclerView.Adapter adapter = mList.getAdapter();
                if (!(adapter instanceof android.support.v7.preference.PreferenceGroup.PreferencePositionCallback)) {
                    if (adapter != null) {
                        throw new java.lang.IllegalStateException("Adapter must implement " + "PreferencePositionCallback");
                    } else {
                        // Adapter was set to null, so don't scroll I guess?
                        return;
                    }
                }
                final int position;
                if (preference != null) {
                    position = ((android.support.v7.preference.PreferenceGroup.PreferencePositionCallback) (adapter)).getPreferenceAdapterPosition(preference);
                } else {
                    position = ((android.support.v7.preference.PreferenceGroup.PreferencePositionCallback) (adapter)).getPreferenceAdapterPosition(key);
                }
                if (position != android.support.v7.widget.RecyclerView.NO_POSITION) {
                    mList.scrollToPosition(position);
                } else {
                    // Item not found, wait for an update and try again
                    adapter.registerAdapterDataObserver(new android.support.v14.preference.PreferenceFragment.ScrollToPreferenceObserver(adapter, mList, preference, key));
                }
            }
        };
        if (mList == null) {
            mSelectPreferenceRunnable = r;
        } else {
            r.run();
        }
    }

    private static class ScrollToPreferenceObserver extends android.support.v7.widget.RecyclerView.AdapterDataObserver {
        private final android.support.v7.widget.RecyclerView.Adapter mAdapter;

        private final android.support.v7.widget.RecyclerView mList;

        private final android.support.v7.preference.Preference mPreference;

        private final java.lang.String mKey;

        public ScrollToPreferenceObserver(android.support.v7.widget.RecyclerView.Adapter adapter, android.support.v7.widget.RecyclerView list, android.support.v7.preference.Preference preference, java.lang.String key) {
            mAdapter = adapter;
            mList = list;
            mPreference = preference;
            mKey = key;
        }

        private void scrollToPreference() {
            mAdapter.unregisterAdapterDataObserver(this);
            final int position;
            if (mPreference != null) {
                position = ((android.support.v7.preference.PreferenceGroup.PreferencePositionCallback) (mAdapter)).getPreferenceAdapterPosition(mPreference);
            } else {
                position = ((android.support.v7.preference.PreferenceGroup.PreferencePositionCallback) (mAdapter)).getPreferenceAdapterPosition(mKey);
            }
            if (position != android.support.v7.widget.RecyclerView.NO_POSITION) {
                mList.scrollToPosition(position);
            }
        }

        @java.lang.Override
        public void onChanged() {
            scrollToPreference();
        }

        @java.lang.Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @java.lang.Override
        public void onItemRangeChanged(int positionStart, int itemCount, java.lang.Object payload) {
            scrollToPreference();
        }

        @java.lang.Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @java.lang.Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            scrollToPreference();
        }

        @java.lang.Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            scrollToPreference();
        }
    }

    private class DividerDecoration extends android.support.v7.widget.RecyclerView.ItemDecoration {
        private android.graphics.drawable.Drawable mDivider;

        private int mDividerHeight;

        @java.lang.Override
        public void onDrawOver(android.graphics.Canvas c, android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.State state) {
            if (mDivider == null) {
                return;
            }
            final int childCount = parent.getChildCount();
            final int width = parent.getWidth();
            for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
                final android.view.View view = parent.getChildAt(childViewIndex);
                if (shouldDrawDividerBelow(view, parent)) {
                    int top = ((int) (android.support.v4.view.ViewCompat.getY(view))) + view.getHeight();
                    mDivider.setBounds(0, top, width, top + mDividerHeight);
                    mDivider.draw(c);
                }
            }
        }

        @java.lang.Override
        public void getItemOffsets(android.graphics.Rect outRect, android.view.View view, android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.State state) {
            if (shouldDrawDividerBelow(view, parent)) {
                outRect.bottom = mDividerHeight;
            }
        }

        private boolean shouldDrawDividerBelow(android.view.View view, android.support.v7.widget.RecyclerView parent) {
            final android.support.v7.widget.RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
            final boolean dividerAllowedBelow = (holder instanceof android.support.v7.preference.PreferenceViewHolder) && ((android.support.v7.preference.PreferenceViewHolder) (holder)).isDividerAllowedBelow();
            if (!dividerAllowedBelow) {
                return false;
            }
            boolean nextAllowed = true;
            int index = parent.indexOfChild(view);
            if (index < (parent.getChildCount() - 1)) {
                final android.view.View nextView = parent.getChildAt(index + 1);
                final android.support.v7.widget.RecyclerView.ViewHolder nextHolder = parent.getChildViewHolder(nextView);
                nextAllowed = (nextHolder instanceof android.support.v7.preference.PreferenceViewHolder) && ((android.support.v7.preference.PreferenceViewHolder) (nextHolder)).isDividerAllowedAbove();
            }
            return nextAllowed;
        }

        public void setDivider(android.graphics.drawable.Drawable divider) {
            if (divider != null) {
                mDividerHeight = divider.getIntrinsicHeight();
            } else {
                mDividerHeight = 0;
            }
            mDivider = divider;
            mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int dividerHeight) {
            mDividerHeight = dividerHeight;
            mList.invalidateItemDecorations();
        }
    }
}

