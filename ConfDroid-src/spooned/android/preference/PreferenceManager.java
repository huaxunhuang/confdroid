/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Used to help create {@link Preference} hierarchies
 * from activities or XML.
 * <p>
 * In most cases, clients should use
 * {@link PreferenceActivity#addPreferencesFromIntent} or
 * {@link PreferenceActivity#addPreferencesFromResource(int)}.
 *
 * @see PreferenceActivity
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class PreferenceManager {
    private static final java.lang.String TAG = "PreferenceManager";

    /**
     * The Activity meta-data key for its XML preference hierarchy.
     */
    public static final java.lang.String METADATA_KEY_PREFERENCES = "android.preference";

    public static final java.lang.String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";

    /**
     *
     *
     * @see #getActivity()
     */
    @android.annotation.Nullable
    private android.app.Activity mActivity;

    /**
     * Fragment that owns this instance.
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.preference.PreferenceFragment mFragment;

    /**
     * The context to use. This should always be set.
     *
     * @see #mActivity
     */
    private android.content.Context mContext;

    /**
     * The counter for unique IDs.
     */
    private long mNextId = 0;

    /**
     * The counter for unique request codes.
     */
    private int mNextRequestCode;

    /**
     * Cached shared preferences.
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    private android.content.SharedPreferences mSharedPreferences;

    /**
     * Data store to be used by the Preferences or {@code null} if
     * {@link android.content.SharedPreferences} should be used.
     */
    @android.annotation.Nullable
    private android.preference.PreferenceDataStore mPreferenceDataStore;

    /**
     * If in no-commit mode, the shared editor to give out (which will be
     * committed when exiting no-commit mode).
     */
    @android.annotation.Nullable
    private android.content.SharedPreferences.Editor mEditor;

    /**
     * Blocks commits from happening on the shared editor. This is used when
     * inflating the hierarchy. Do not set this directly, use {@link #setNoCommit(boolean)}
     */
    private boolean mNoCommit;

    /**
     * The SharedPreferences name that will be used for all {@link Preference}s
     * managed by this instance.
     */
    private java.lang.String mSharedPreferencesName;

    /**
     * The SharedPreferences mode that will be used for all {@link Preference}s
     * managed by this instance.
     */
    private int mSharedPreferencesMode;

    private static final int STORAGE_DEFAULT = 0;

    private static final int STORAGE_DEVICE_PROTECTED = 1;

    private static final int STORAGE_CREDENTIAL_PROTECTED = 2;

    private int mStorage = android.preference.PreferenceManager.STORAGE_DEFAULT;

    /**
     * The {@link PreferenceScreen} at the root of the preference hierarchy.
     */
    @android.annotation.Nullable
    private android.preference.PreferenceScreen mPreferenceScreen;

    /**
     * List of activity result listeners.
     */
    @android.annotation.Nullable
    private java.util.List<android.preference.PreferenceManager.OnActivityResultListener> mActivityResultListeners;

    /**
     * List of activity stop listeners.
     */
    @android.annotation.Nullable
    private java.util.List<android.preference.PreferenceManager.OnActivityStopListener> mActivityStopListeners;

    /**
     * List of activity destroy listeners.
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    private java.util.List<android.preference.PreferenceManager.OnActivityDestroyListener> mActivityDestroyListeners;

    /**
     * List of dialogs that should be dismissed when we receive onNewIntent in
     * our PreferenceActivity.
     */
    @android.annotation.Nullable
    private java.util.List<android.content.DialogInterface> mPreferencesScreens;

    @android.annotation.UnsupportedAppUsage
    private android.preference.PreferenceManager.OnPreferenceTreeClickListener mOnPreferenceTreeClickListener;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public PreferenceManager(android.app.Activity activity, int firstRequestCode) {
        mActivity = activity;
        mNextRequestCode = firstRequestCode;
        init(activity);
    }

    /**
     * This constructor should ONLY be used when getting default values from
     * an XML preference hierarchy.
     * <p>
     * The {@link PreferenceManager#PreferenceManager(Activity)}
     * should be used ANY time a preference will be displayed, since some preference
     * types need an Activity for managed queries.
     */
    /* package */
    @android.annotation.UnsupportedAppUsage
    PreferenceManager(android.content.Context context) {
        init(context);
    }

    private void init(android.content.Context context) {
        mContext = context;
        setSharedPreferencesName(android.preference.PreferenceManager.getDefaultSharedPreferencesName(context));
    }

    /**
     * Sets the owning preference fragment
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    void setFragment(android.preference.PreferenceFragment fragment) {
        mFragment = fragment;
    }

    /**
     * Returns the owning preference fragment, if any.
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    android.preference.PreferenceFragment getFragment() {
        return mFragment;
    }

    /**
     * Sets a {@link PreferenceDataStore} to be used by all Preferences associated with this manager
     * that don't have a custom {@link PreferenceDataStore} assigned via
     * {@link Preference#setPreferenceDataStore(PreferenceDataStore)}. Also if the data store is
     * set, the child preferences won't use {@link android.content.SharedPreferences} as long as
     * they are assigned to this manager.
     *
     * @param dataStore
     * 		The {@link PreferenceDataStore} to be used by this manager.
     * @see Preference#setPreferenceDataStore(PreferenceDataStore)
     */
    public void setPreferenceDataStore(android.preference.PreferenceDataStore dataStore) {
        mPreferenceDataStore = dataStore;
    }

    /**
     * Returns the {@link PreferenceDataStore} associated with this manager or {@code null} if
     * the default {@link android.content.SharedPreferences} are used instead.
     *
     * @return The {@link PreferenceDataStore} associated with this manager or {@code null} if none.
     * @see #setPreferenceDataStore(PreferenceDataStore)
     */
    @android.annotation.Nullable
    public android.preference.PreferenceDataStore getPreferenceDataStore() {
        return mPreferenceDataStore;
    }

    /**
     * Returns a list of {@link Activity} (indirectly) that match a given
     * {@link Intent}.
     *
     * @param queryIntent
     * 		The Intent to match.
     * @return The list of {@link ResolveInfo} that point to the matched
    activities.
     */
    private java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent queryIntent) {
        return mContext.getPackageManager().queryIntentActivities(queryIntent, android.content.pm.PackageManager.GET_META_DATA);
    }

    /**
     * Inflates a preference hierarchy from the preference hierarchies of
     * {@link Activity Activities} that match the given {@link Intent}. An
     * {@link Activity} defines its preference hierarchy with meta-data using
     * the {@link #METADATA_KEY_PREFERENCES} key.
     * <p>
     * If a preference hierarchy is given, the new preference hierarchies will
     * be merged in.
     *
     * @param queryIntent
     * 		The intent to match activities.
     * @param rootPreferences
     * 		Optional existing hierarchy to merge the new
     * 		hierarchies into.
     * @return The root hierarchy (if one was not provided, the new hierarchy's
    root).
     */
    @android.annotation.UnsupportedAppUsage
    android.preference.PreferenceScreen inflateFromIntent(android.content.Intent queryIntent, android.preference.PreferenceScreen rootPreferences) {
        final java.util.List<android.content.pm.ResolveInfo> activities = queryIntentActivities(queryIntent);
        final java.util.HashSet<java.lang.String> inflatedRes = new java.util.HashSet<java.lang.String>();
        for (int i = activities.size() - 1; i >= 0; i--) {
            final android.content.pm.ActivityInfo activityInfo = activities.get(i).activityInfo;
            final android.os.Bundle metaData = activityInfo.metaData;
            if ((metaData == null) || (!metaData.containsKey(android.preference.PreferenceManager.METADATA_KEY_PREFERENCES))) {
                continue;
            }
            // Need to concat the package with res ID since the same res ID
            // can be re-used across contexts
            final java.lang.String uniqueResId = (activityInfo.packageName + ":") + activityInfo.metaData.getInt(android.preference.PreferenceManager.METADATA_KEY_PREFERENCES);
            if (!inflatedRes.contains(uniqueResId)) {
                inflatedRes.add(uniqueResId);
                final android.content.Context context;
                try {
                    context = mContext.createPackageContext(activityInfo.packageName, 0);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.w(android.preference.PreferenceManager.TAG, (("Could not create context for " + activityInfo.packageName) + ": ") + android.util.Log.getStackTraceString(e));
                    continue;
                }
                final android.preference.PreferenceInflater inflater = new android.preference.PreferenceInflater(context, this);
                final android.content.res.XmlResourceParser parser = activityInfo.loadXmlMetaData(context.getPackageManager(), android.preference.PreferenceManager.METADATA_KEY_PREFERENCES);
                rootPreferences = ((android.preference.PreferenceScreen) (inflater.inflate(parser, rootPreferences, true)));
                parser.close();
            }
        }
        rootPreferences.onAttachedToHierarchy(this);
        return rootPreferences;
    }

    /**
     * Inflates a preference hierarchy from XML. If a preference hierarchy is
     * given, the new preference hierarchies will be merged in.
     *
     * @param context
     * 		The context of the resource.
     * @param resId
     * 		The resource ID of the XML to inflate.
     * @param rootPreferences
     * 		Optional existing hierarchy to merge the new
     * 		hierarchies into.
     * @return The root hierarchy (if one was not provided, the new hierarchy's
    root).
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.preference.PreferenceScreen inflateFromResource(android.content.Context context, @android.annotation.XmlRes
    int resId, android.preference.PreferenceScreen rootPreferences) {
        // Block commits
        setNoCommit(true);
        final android.preference.PreferenceInflater inflater = new android.preference.PreferenceInflater(context, this);
        rootPreferences = ((android.preference.PreferenceScreen) (inflater.inflate(resId, rootPreferences, true)));
        rootPreferences.onAttachedToHierarchy(this);
        // Unblock commits
        setNoCommit(false);
        return rootPreferences;
    }

    public android.preference.PreferenceScreen createPreferenceScreen(android.content.Context context) {
        final android.preference.PreferenceScreen preferenceScreen = new android.preference.PreferenceScreen(context, null);
        preferenceScreen.onAttachedToHierarchy(this);
        return preferenceScreen;
    }

    /**
     * Called by a preference to get a unique ID in its hierarchy.
     *
     * @return A unique ID.
     */
    long getNextId() {
        synchronized(this) {
            return mNextId++;
        }
    }

    /**
     * Returns the current name of the SharedPreferences file that preferences managed by
     * this will use.
     *
     * @return The name that can be passed to {@link Context#getSharedPreferences(String, int)}.
     * @see Context#getSharedPreferences(String, int)
     */
    public java.lang.String getSharedPreferencesName() {
        return mSharedPreferencesName;
    }

    /**
     * Sets the name of the SharedPreferences file that preferences managed by this
     * will use.
     *
     * <p>If custom {@link PreferenceDataStore} is set, this won't override its usage.
     *
     * @param sharedPreferencesName
     * 		The name of the SharedPreferences file.
     * @see Context#getSharedPreferences(String, int)
     * @see #setPreferenceDataStore(PreferenceDataStore)
     */
    public void setSharedPreferencesName(java.lang.String sharedPreferencesName) {
        mSharedPreferencesName = sharedPreferencesName;
        mSharedPreferences = null;
    }

    /**
     * Returns the current mode of the SharedPreferences file that preferences managed by
     * this will use.
     *
     * @return The mode that can be passed to {@link Context#getSharedPreferences(String, int)}.
     * @see Context#getSharedPreferences(String, int)
     */
    public int getSharedPreferencesMode() {
        return mSharedPreferencesMode;
    }

    /**
     * Sets the mode of the SharedPreferences file that preferences managed by this
     * will use.
     *
     * @param sharedPreferencesMode
     * 		The mode of the SharedPreferences file.
     * @see Context#getSharedPreferences(String, int)
     */
    public void setSharedPreferencesMode(int sharedPreferencesMode) {
        mSharedPreferencesMode = sharedPreferencesMode;
        mSharedPreferences = null;
    }

    /**
     * Sets the storage location used internally by this class to be the default
     * provided by the hosting {@link Context}.
     */
    public void setStorageDefault() {
        mStorage = android.preference.PreferenceManager.STORAGE_DEFAULT;
        mSharedPreferences = null;
    }

    /**
     * Explicitly set the storage location used internally by this class to be
     * device-protected storage.
     * <p>
     * On devices with direct boot, data stored in this location is encrypted
     * with a key tied to the physical device, and it can be accessed
     * immediately after the device has booted successfully, both
     * <em>before and after</em> the user has authenticated with their
     * credentials (such as a lock pattern or PIN).
     * <p>
     * Because device-protected data is available without user authentication,
     * you should carefully limit the data you store using this Context. For
     * example, storing sensitive authentication tokens or passwords in the
     * device-protected area is strongly discouraged.
     *
     * @see Context#createDeviceProtectedStorageContext()
     */
    public void setStorageDeviceProtected() {
        mStorage = android.preference.PreferenceManager.STORAGE_DEVICE_PROTECTED;
        mSharedPreferences = null;
    }

    /**
     * Explicitly set the storage location used internally by this class to be
     * credential-protected storage. This is the default storage area for apps
     * unless {@code forceDeviceProtectedStorage} was requested.
     * <p>
     * On devices with direct boot, data stored in this location is encrypted
     * with a key tied to user credentials, which can be accessed
     * <em>only after</em> the user has entered their credentials (such as a
     * lock pattern or PIN).
     *
     * @see Context#createCredentialProtectedStorageContext()
     * @unknown 
     */
    @android.annotation.SystemApi
    public void setStorageCredentialProtected() {
        mStorage = android.preference.PreferenceManager.STORAGE_CREDENTIAL_PROTECTED;
        mSharedPreferences = null;
    }

    /**
     * Indicates if the storage location used internally by this class is the
     * default provided by the hosting {@link Context}.
     *
     * @see #setStorageDefault()
     * @see #setStorageDeviceProtected()
     */
    public boolean isStorageDefault() {
        return mStorage == android.preference.PreferenceManager.STORAGE_DEFAULT;
    }

    /**
     * Indicates if the storage location used internally by this class is backed
     * by device-protected storage.
     *
     * @see #setStorageDefault()
     * @see #setStorageDeviceProtected()
     */
    public boolean isStorageDeviceProtected() {
        return mStorage == android.preference.PreferenceManager.STORAGE_DEVICE_PROTECTED;
    }

    /**
     * Indicates if the storage location used internally by this class is backed
     * by credential-protected storage.
     *
     * @see #setStorageDefault()
     * @see #setStorageDeviceProtected()
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isStorageCredentialProtected() {
        return mStorage == android.preference.PreferenceManager.STORAGE_CREDENTIAL_PROTECTED;
    }

    /**
     * Gets a {@link SharedPreferences} instance that preferences managed by this will use.
     *
     * @return a {@link SharedPreferences} instance pointing to the file that contains the values of
    preferences that are managed by this PreferenceManager. If a
    {@link PreferenceDataStore} has been set, this method returns {@code null}.
     */
    public android.content.SharedPreferences getSharedPreferences() {
        if (mPreferenceDataStore != null) {
            return null;
        }
        if (mSharedPreferences == null) {
            final android.content.Context storageContext;
            switch (mStorage) {
                case android.preference.PreferenceManager.STORAGE_DEVICE_PROTECTED :
                    storageContext = mContext.createDeviceProtectedStorageContext();
                    break;
                case android.preference.PreferenceManager.STORAGE_CREDENTIAL_PROTECTED :
                    storageContext = mContext.createCredentialProtectedStorageContext();
                    break;
                default :
                    storageContext = mContext;
                    break;
            }
            mSharedPreferences = storageContext.getSharedPreferences(mSharedPreferencesName, mSharedPreferencesMode);
        }
        return mSharedPreferences;
    }

    /**
     * Gets a {@link SharedPreferences} instance that points to the default file that is used by
     * the preference framework in the given context.
     *
     * @param context
     * 		The context of the preferences whose values are wanted.
     * @return A {@link SharedPreferences} instance that can be used to retrieve and listen
    to values of the preferences.
     */
    public static android.content.SharedPreferences getDefaultSharedPreferences(android.content.Context context) {
        return context.getSharedPreferences(android.preference.PreferenceManager.getDefaultSharedPreferencesName(context), android.preference.PreferenceManager.getDefaultSharedPreferencesMode());
    }

    /**
     * Returns the name used for storing default shared preferences.
     *
     * @see #getDefaultSharedPreferences(Context)
     */
    public static java.lang.String getDefaultSharedPreferencesName(android.content.Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getDefaultSharedPreferencesMode() {
        return android.content.Context.MODE_PRIVATE;
    }

    /**
     * Returns the root of the preference hierarchy managed by this class.
     *
     * @return The {@link PreferenceScreen} object that is at the root of the hierarchy.
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    android.preference.PreferenceScreen getPreferenceScreen() {
        return mPreferenceScreen;
    }

    /**
     * Sets the root of the preference hierarchy.
     *
     * @param preferenceScreen
     * 		The root {@link PreferenceScreen} of the preference hierarchy.
     * @return Whether the {@link PreferenceScreen} given is different than the previous.
     */
    @android.annotation.UnsupportedAppUsage
    boolean setPreferences(android.preference.PreferenceScreen preferenceScreen) {
        if (preferenceScreen != mPreferenceScreen) {
            mPreferenceScreen = preferenceScreen;
            return true;
        }
        return false;
    }

    /**
     * Finds a {@link Preference} based on its key.
     *
     * @param key
     * 		the key of the preference to retrieve
     * @return the {@link Preference} with the key, or {@code null}
     * @see PreferenceGroup#findPreference(CharSequence)
     */
    @android.annotation.Nullable
    public android.preference.Preference findPreference(java.lang.CharSequence key) {
        if (mPreferenceScreen == null) {
            return null;
        }
        return mPreferenceScreen.findPreference(key);
    }

    /**
     * Sets the default values from an XML preference file by reading the values defined
     * by each {@link Preference} item's {@code android:defaultValue} attribute. This should
     * be called by the application's main activity.
     * <p>
     *
     * @param context
     * 		The context of the shared preferences.
     * @param resId
     * 		The resource ID of the preference XML file.
     * @param readAgain
     * 		Whether to re-read the default values.
     * 		If false, this method sets the default values only if this
     * 		method has never been called in the past (or if the
     * 		{@link #KEY_HAS_SET_DEFAULT_VALUES} in the default value shared
     * 		preferences file is false). To attempt to set the default values again
     * 		bypassing this check, set {@code readAgain} to true.
     * 		<p class="note">
     * 		Note: this will NOT reset preferences back to their default
     * 		values. For that functionality, use
     * 		{@link PreferenceManager#getDefaultSharedPreferences(Context)}
     * 		and clear it followed by a call to this method with this
     * 		parameter set to true.
     */
    public static void setDefaultValues(android.content.Context context, @android.annotation.XmlRes
    int resId, boolean readAgain) {
        // Use the default shared preferences name and mode
        android.preference.PreferenceManager.setDefaultValues(context, android.preference.PreferenceManager.getDefaultSharedPreferencesName(context), android.preference.PreferenceManager.getDefaultSharedPreferencesMode(), resId, readAgain);
    }

    /**
     * Similar to {@link #setDefaultValues(Context, int, boolean)} but allows
     * the client to provide the filename and mode of the shared preferences
     * file.
     *
     * @param context
     * 		The context of the shared preferences.
     * @param sharedPreferencesName
     * 		A custom name for the shared preferences file.
     * @param sharedPreferencesMode
     * 		The file creation mode for the shared preferences file, such
     * 		as {@link android.content.Context#MODE_PRIVATE} or {@link android.content.Context#MODE_PRIVATE}
     * @param resId
     * 		The resource ID of the preference XML file.
     * @param readAgain
     * 		Whether to re-read the default values.
     * 		If false, this method will set the default values only if this
     * 		method has never been called in the past (or if the
     * 		{@link #KEY_HAS_SET_DEFAULT_VALUES} in the default value shared
     * 		preferences file is false). To attempt to set the default values again
     * 		bypassing this check, set {@code readAgain} to true.
     * 		<p class="note">
     * 		Note: this will NOT reset preferences back to their default
     * 		values. For that functionality, use
     * 		{@link PreferenceManager#getDefaultSharedPreferences(Context)}
     * 		and clear it followed by a call to this method with this
     * 		parameter set to true.
     * @see #setDefaultValues(Context, int, boolean)
     * @see #setSharedPreferencesName(String)
     * @see #setSharedPreferencesMode(int)
     */
    public static void setDefaultValues(android.content.Context context, java.lang.String sharedPreferencesName, int sharedPreferencesMode, int resId, boolean readAgain) {
        final android.content.SharedPreferences defaultValueSp = context.getSharedPreferences(android.preference.PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, android.content.Context.MODE_PRIVATE);
        if (readAgain || (!defaultValueSp.getBoolean(android.preference.PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false))) {
            final android.preference.PreferenceManager pm = new android.preference.PreferenceManager(context);
            pm.setSharedPreferencesName(sharedPreferencesName);
            pm.setSharedPreferencesMode(sharedPreferencesMode);
            pm.inflateFromResource(context, resId, null);
            android.content.SharedPreferences.Editor editor = defaultValueSp.edit().putBoolean(android.preference.PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, true);
            try {
                editor.apply();
            } catch (java.lang.AbstractMethodError unused) {
                // The app injected its own pre-Gingerbread
                // SharedPreferences.Editor implementation without
                // an apply method.
                editor.commit();
            }
        }
    }

    /**
     * Returns an editor to use when modifying the shared preferences.
     *
     * <p>Do NOT commit unless {@link #shouldCommit()} returns true.
     *
     * @return an editor to use to write to shared preferences. If a {@link PreferenceDataStore}
    has been set, this method returns {@code null}.
     * @see #shouldCommit()
     */
    @android.annotation.UnsupportedAppUsage
    android.content.SharedPreferences.Editor getEditor() {
        if (mPreferenceDataStore != null) {
            return null;
        }
        if (mNoCommit) {
            if (mEditor == null) {
                mEditor = getSharedPreferences().edit();
            }
            return mEditor;
        } else {
            return getSharedPreferences().edit();
        }
    }

    /**
     * Whether it is the client's responsibility to commit on the
     * {@link #getEditor()}. This will return false in cases where the writes
     * should be batched, for example when inflating preferences from XML.
     *
     * <p>If preferences are using {@link PreferenceDataStore} this value is irrelevant.
     *
     * @return Whether the client should commit.
     */
    @android.annotation.UnsupportedAppUsage
    boolean shouldCommit() {
        return !mNoCommit;
    }

    @android.annotation.UnsupportedAppUsage
    private void setNoCommit(boolean noCommit) {
        if ((!noCommit) && (mEditor != null)) {
            try {
                mEditor.apply();
            } catch (java.lang.AbstractMethodError unused) {
                // The app injected its own pre-Gingerbread
                // SharedPreferences.Editor implementation without
                // an apply method.
                mEditor.commit();
            }
        }
        mNoCommit = noCommit;
    }

    /**
     * Returns the activity that shows the preferences. This is useful for doing
     * managed queries, but in most cases the use of {@link #getContext()} is
     * preferred.
     *
     * <p>This will return {@code null} if this class was instantiated with a Context
     * instead of Activity. For example, when setting the default values.
     *
     * @return The activity that shows the preferences.
     * @see #mContext
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    android.app.Activity getActivity() {
        return mActivity;
    }

    /**
     * Returns the context. This is preferred over {@link #getActivity()} when
     * possible.
     *
     * @return The context.
     */
    android.content.Context getContext() {
        return mContext;
    }

    /**
     * Registers a listener.
     *
     * @see OnActivityResultListener
     */
    @android.annotation.UnsupportedAppUsage
    void registerOnActivityResultListener(android.preference.PreferenceManager.OnActivityResultListener listener) {
        synchronized(this) {
            if (mActivityResultListeners == null) {
                mActivityResultListeners = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityResultListener>();
            }
            if (!mActivityResultListeners.contains(listener)) {
                mActivityResultListeners.add(listener);
            }
        }
    }

    /**
     * Unregisters a listener.
     *
     * @see OnActivityResultListener
     */
    @android.annotation.UnsupportedAppUsage
    void unregisterOnActivityResultListener(android.preference.PreferenceManager.OnActivityResultListener listener) {
        synchronized(this) {
            if (mActivityResultListeners != null) {
                mActivityResultListeners.remove(listener);
            }
        }
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch a subactivity result.
     */
    @android.annotation.UnsupportedAppUsage
    void dispatchActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        java.util.List<android.preference.PreferenceManager.OnActivityResultListener> list;
        synchronized(this) {
            if (mActivityResultListeners == null)
                return;

            list = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityResultListener>(mActivityResultListeners);
        }
        final int N = list.size();
        for (int i = 0; i < N; i++) {
            if (list.get(i).onActivityResult(requestCode, resultCode, data)) {
                break;
            }
        }
    }

    /**
     * Registers a listener.
     *
     * @see OnActivityStopListener
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void registerOnActivityStopListener(android.preference.PreferenceManager.OnActivityStopListener listener) {
        synchronized(this) {
            if (mActivityStopListeners == null) {
                mActivityStopListeners = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityStopListener>();
            }
            if (!mActivityStopListeners.contains(listener)) {
                mActivityStopListeners.add(listener);
            }
        }
    }

    /**
     * Unregisters a listener.
     *
     * @see OnActivityStopListener
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void unregisterOnActivityStopListener(android.preference.PreferenceManager.OnActivityStopListener listener) {
        synchronized(this) {
            if (mActivityStopListeners != null) {
                mActivityStopListeners.remove(listener);
            }
        }
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch the activity stop
     * event.
     */
    @android.annotation.UnsupportedAppUsage
    void dispatchActivityStop() {
        java.util.List<android.preference.PreferenceManager.OnActivityStopListener> list;
        synchronized(this) {
            if (mActivityStopListeners == null)
                return;

            list = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityStopListener>(mActivityStopListeners);
        }
        final int N = list.size();
        for (int i = 0; i < N; i++) {
            list.get(i).onActivityStop();
        }
    }

    /**
     * Registers a listener.
     *
     * @see OnActivityDestroyListener
     */
    @android.annotation.UnsupportedAppUsage
    void registerOnActivityDestroyListener(android.preference.PreferenceManager.OnActivityDestroyListener listener) {
        synchronized(this) {
            if (mActivityDestroyListeners == null) {
                mActivityDestroyListeners = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityDestroyListener>();
            }
            if (!mActivityDestroyListeners.contains(listener)) {
                mActivityDestroyListeners.add(listener);
            }
        }
    }

    /**
     * Unregisters a listener.
     *
     * @see OnActivityDestroyListener
     */
    @android.annotation.UnsupportedAppUsage
    void unregisterOnActivityDestroyListener(android.preference.PreferenceManager.OnActivityDestroyListener listener) {
        synchronized(this) {
            if (mActivityDestroyListeners != null) {
                mActivityDestroyListeners.remove(listener);
            }
        }
    }

    /**
     * Called by the {@link PreferenceManager} to dispatch the activity destroy
     * event.
     */
    @android.annotation.UnsupportedAppUsage
    void dispatchActivityDestroy() {
        java.util.List<android.preference.PreferenceManager.OnActivityDestroyListener> list = null;
        synchronized(this) {
            if (mActivityDestroyListeners != null) {
                list = new java.util.ArrayList<android.preference.PreferenceManager.OnActivityDestroyListener>(mActivityDestroyListeners);
            }
        }
        if (list != null) {
            final int N = list.size();
            for (int i = 0; i < N; i++) {
                list.get(i).onActivityDestroy();
            }
        }
        // Dismiss any PreferenceScreens still showing
        dismissAllScreens();
    }

    /**
     * Returns a request code that is unique for the activity. Each subsequent
     * call to this method should return another unique request code.
     *
     * @return A unique request code that will never be used by anyone other
    than the caller of this method.
     */
    @android.annotation.UnsupportedAppUsage
    int getNextRequestCode() {
        synchronized(this) {
            return mNextRequestCode++;
        }
    }

    void addPreferencesScreen(android.content.DialogInterface screen) {
        synchronized(this) {
            if (mPreferencesScreens == null) {
                mPreferencesScreens = new java.util.ArrayList<android.content.DialogInterface>();
            }
            mPreferencesScreens.add(screen);
        }
    }

    void removePreferencesScreen(android.content.DialogInterface screen) {
        synchronized(this) {
            if (mPreferencesScreens == null) {
                return;
            }
            mPreferencesScreens.remove(screen);
        }
    }

    /**
     * Called by {@link PreferenceActivity} to dispatch the new Intent event.
     *
     * @param intent
     * 		The new Intent.
     */
    void dispatchNewIntent(android.content.Intent intent) {
        dismissAllScreens();
    }

    private void dismissAllScreens() {
        // Remove any of the previously shown preferences screens
        java.util.ArrayList<android.content.DialogInterface> screensToDismiss;
        synchronized(this) {
            if (mPreferencesScreens == null) {
                return;
            }
            screensToDismiss = new java.util.ArrayList<android.content.DialogInterface>(mPreferencesScreens);
            mPreferencesScreens.clear();
        }
        for (int i = screensToDismiss.size() - 1; i >= 0; i--) {
            screensToDismiss.get(i).dismiss();
        }
    }

    /**
     * Sets the callback to be invoked when a {@link Preference} in the
     * hierarchy rooted at this {@link PreferenceManager} is clicked.
     *
     * @param listener
     * 		The callback to be invoked.
     */
    void setOnPreferenceTreeClickListener(android.preference.PreferenceManager.OnPreferenceTreeClickListener listener) {
        mOnPreferenceTreeClickListener = listener;
    }

    @android.annotation.Nullable
    android.preference.PreferenceManager.OnPreferenceTreeClickListener getOnPreferenceTreeClickListener() {
        return mOnPreferenceTreeClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when a
     * {@link Preference} in the hierarchy rooted at this {@link PreferenceScreen} is
     * clicked.
     *
     * @unknown 
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnPreferenceTreeClickListener {
        /**
         * Called when a preference in the tree rooted at this
         * {@link PreferenceScreen} has been clicked.
         *
         * @param preferenceScreen
         * 		The {@link PreferenceScreen} that the
         * 		preference is located in.
         * @param preference
         * 		The preference that was clicked.
         * @return Whether the click was handled.
         */
        boolean onPreferenceTreeClick(android.preference.PreferenceScreen preferenceScreen, android.preference.Preference preference);
    }

    /**
     * Interface definition for a class that will be called when the container's activity
     * receives an activity result.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnActivityResultListener {
        /**
         * See Activity's onActivityResult.
         *
         * @return Whether the request code was handled (in which case
        subsequent listeners will not be called.
         */
        boolean onActivityResult(int requestCode, int resultCode, android.content.Intent data);
    }

    /**
     * Interface definition for a class that will be called when the container's activity
     * is stopped.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnActivityStopListener {
        /**
         * See Activity's onStop.
         */
        void onActivityStop();
    }

    /**
     * Interface definition for a class that will be called when the container's activity
     * is destroyed.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnActivityDestroyListener {
        /**
         * See Activity's onDestroy.
         */
        void onActivityDestroy();
    }
}

