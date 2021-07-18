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
 * Represents the basic Preference UI building
 * block displayed by a {@link PreferenceActivity} in the form of a
 * {@link ListView}. This class provides the {@link View} to be displayed in
 * the activity and associates with a {@link SharedPreferences} to
 * store/retrieve the preference data.
 * <p>
 * When specifying a preference hierarchy in XML, each element can point to a
 * subclass of {@link Preference}, similar to the view hierarchy and layouts.
 * <p>
 * This class contains a {@code key} that will be used as the key into the
 * {@link SharedPreferences}. It is up to the subclass to decide how to store
 * the value.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @unknown ref android.R.styleable#Preference_icon
 * @unknown ref android.R.styleable#Preference_key
 * @unknown ref android.R.styleable#Preference_title
 * @unknown ref android.R.styleable#Preference_summary
 * @unknown ref android.R.styleable#Preference_order
 * @unknown ref android.R.styleable#Preference_fragment
 * @unknown ref android.R.styleable#Preference_layout
 * @unknown ref android.R.styleable#Preference_widgetLayout
 * @unknown ref android.R.styleable#Preference_enabled
 * @unknown ref android.R.styleable#Preference_selectable
 * @unknown ref android.R.styleable#Preference_dependency
 * @unknown ref android.R.styleable#Preference_persistent
 * @unknown ref android.R.styleable#Preference_defaultValue
 * @unknown ref android.R.styleable#Preference_shouldDisableView
 * @unknown ref android.R.styleable#Preference_recycleEnabled
 * @unknown ref android.R.styleable#Preference_singleLineTitle
 * @unknown ref android.R.styleable#Preference_iconSpaceReserved
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class Preference implements java.lang.Comparable<android.preference.Preference> {
    /**
     * Specify for {@link #setOrder(int)} if a specific order is not required.
     */
    public static final int DEFAULT_ORDER = java.lang.Integer.MAX_VALUE;

    private android.content.Context mContext;

    @android.annotation.Nullable
    private android.preference.PreferenceManager mPreferenceManager;

    /**
     * The data store that should be used by this Preference to store / retrieve data. If null then
     * {@link PreferenceManager#getPreferenceDataStore()} needs to be checked. If that one is null
     * too it means that we are using {@link android.content.SharedPreferences} to store the data.
     */
    @android.annotation.Nullable
    private android.preference.PreferenceDataStore mPreferenceDataStore;

    /**
     * Set when added to hierarchy since we need a unique ID within that
     * hierarchy.
     */
    private long mId;

    private android.preference.Preference.OnPreferenceChangeListener mOnChangeListener;

    private android.preference.Preference.OnPreferenceClickListener mOnClickListener;

    private int mOrder = android.preference.Preference.DEFAULT_ORDER;

    private java.lang.CharSequence mTitle;

    private int mTitleRes;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mSummary;

    /**
     * mIconResId is overridden by mIcon, if mIcon is specified.
     */
    private int mIconResId;

    private android.graphics.drawable.Drawable mIcon;

    private java.lang.String mKey;

    private android.content.Intent mIntent;

    private java.lang.String mFragment;

    private android.os.Bundle mExtras;

    private boolean mEnabled = true;

    private boolean mSelectable = true;

    private boolean mRequiresKey;

    private boolean mPersistent = true;

    private java.lang.String mDependencyKey;

    private java.lang.Object mDefaultValue;

    private boolean mDependencyMet = true;

    private boolean mParentDependencyMet = true;

    private boolean mRecycleEnabled = true;

    private boolean mHasSingleLineTitleAttr;

    private boolean mSingleLineTitle = true;

    private boolean mIconSpaceReserved;

    /**
     *
     *
     * @see #setShouldDisableView(boolean)
     */
    private boolean mShouldDisableView = true;

    @android.annotation.UnsupportedAppUsage
    private int mLayoutResId = com.android.internal.R.layout.preference;

    @android.annotation.UnsupportedAppUsage
    private int mWidgetLayoutResId;

    private android.preference.Preference.OnPreferenceChangeInternalListener mListener;

    private java.util.List<android.preference.Preference> mDependents;

    private android.preference.PreferenceGroup mParentGroup;

    private boolean mBaseMethodCalled;

    /**
     * Interface definition for a callback to be invoked when the value of this
     * {@link Preference} has been changed by the user and is
     * about to be set and/or persisted.  This gives the client a chance
     * to prevent setting and/or persisting the value.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnPreferenceChangeListener {
        /**
         * Called when a Preference has been changed by the user. This is
         * called before the state of the Preference is about to be updated and
         * before the state is persisted.
         *
         * @param preference
         * 		The changed Preference.
         * @param newValue
         * 		The new value of the Preference.
         * @return True to update the state of the Preference with the new value.
         */
        boolean onPreferenceChange(android.preference.Preference preference, java.lang.Object newValue);
    }

    /**
     * Interface definition for a callback to be invoked when a {@link Preference} is
     * clicked.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public interface OnPreferenceClickListener {
        /**
         * Called when a Preference has been clicked.
         *
         * @param preference
         * 		The Preference that was clicked.
         * @return True if the click was handled.
         */
        boolean onPreferenceClick(android.preference.Preference preference);
    }

    /**
     * Interface definition for a callback to be invoked when this
     * {@link Preference} is changed or, if this is a group, there is an
     * addition/removal of {@link Preference}(s). This is used internally.
     */
    interface OnPreferenceChangeInternalListener {
        /**
         * Called when this Preference has changed.
         *
         * @param preference
         * 		This preference.
         */
        void onPreferenceChange(android.preference.Preference preference);

        /**
         * Called when this group has added/removed {@link Preference}(s).
         *
         * @param preference
         * 		This Preference.
         */
        void onPreferenceHierarchyChange(android.preference.Preference preference);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of Preference allows subclasses to use their own base style
     * when they are inflating. For example, a {@link CheckBoxPreference}
     * constructor calls this version of the super class constructor and
     * supplies {@code android.R.attr.checkBoxPreferenceStyle} for
     * <var>defStyleAttr</var>. This allows the theme's checkbox preference
     * style to modify all of the base preference attributes as well as the
     * {@link CheckBoxPreference} class's attributes.
     *
     * @param context
     * 		The Context this is associated with, through which it can
     * 		access the current theme, resources,
     * 		{@link SharedPreferences}, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the
     * 		preference.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     * @see #Preference(Context, AttributeSet)
     */
    public Preference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Preference, defStyleAttr, defStyleRes);
        for (int i = a.getIndexCount() - 1; i >= 0; i--) {
            int attr = a.getIndex(i);
            switch (attr) {
                case com.android.internal.R.styleable.Preference_icon :
                    mIconResId = a.getResourceId(attr, 0);
                    break;
                case com.android.internal.R.styleable.Preference_key :
                    mKey = a.getString(attr);
                    break;
                case com.android.internal.R.styleable.Preference_title :
                    mTitleRes = a.getResourceId(attr, 0);
                    mTitle = a.getText(attr);
                    break;
                case com.android.internal.R.styleable.Preference_summary :
                    mSummary = a.getText(attr);
                    break;
                case com.android.internal.R.styleable.Preference_order :
                    mOrder = a.getInt(attr, mOrder);
                    break;
                case com.android.internal.R.styleable.Preference_fragment :
                    mFragment = a.getString(attr);
                    break;
                case com.android.internal.R.styleable.Preference_layout :
                    mLayoutResId = a.getResourceId(attr, mLayoutResId);
                    break;
                case com.android.internal.R.styleable.Preference_widgetLayout :
                    mWidgetLayoutResId = a.getResourceId(attr, mWidgetLayoutResId);
                    break;
                case com.android.internal.R.styleable.Preference_enabled :
                    mEnabled = a.getBoolean(attr, true);
                    break;
                case com.android.internal.R.styleable.Preference_selectable :
                    mSelectable = a.getBoolean(attr, true);
                    break;
                case com.android.internal.R.styleable.Preference_persistent :
                    mPersistent = a.getBoolean(attr, mPersistent);
                    break;
                case com.android.internal.R.styleable.Preference_dependency :
                    mDependencyKey = a.getString(attr);
                    break;
                case com.android.internal.R.styleable.Preference_defaultValue :
                    mDefaultValue = onGetDefaultValue(a, attr);
                    break;
                case com.android.internal.R.styleable.Preference_shouldDisableView :
                    mShouldDisableView = a.getBoolean(attr, mShouldDisableView);
                    break;
                case com.android.internal.R.styleable.Preference_recycleEnabled :
                    mRecycleEnabled = a.getBoolean(attr, mRecycleEnabled);
                    break;
                case com.android.internal.R.styleable.Preference_singleLineTitle :
                    mSingleLineTitle = a.getBoolean(attr, mSingleLineTitle);
                    mHasSingleLineTitleAttr = true;
                    break;
                case com.android.internal.R.styleable.Preference_iconSpaceReserved :
                    mIconSpaceReserved = a.getBoolean(attr, mIconSpaceReserved);
                    break;
            }
        }
        a.recycle();
    }

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of Preference allows subclasses to use their own base style
     * when they are inflating. For example, a {@link CheckBoxPreference}
     * constructor calls this version of the super class constructor and
     * supplies {@code android.R.attr.checkBoxPreferenceStyle} for
     * <var>defStyleAttr</var>. This allows the theme's checkbox preference
     * style to modify all of the base preference attributes as well as the
     * {@link CheckBoxPreference} class's attributes.
     *
     * @param context
     * 		The Context this is associated with, through which it can
     * 		access the current theme, resources,
     * 		{@link SharedPreferences}, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the
     * 		preference.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @see #Preference(Context, AttributeSet)
     */
    public Preference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Constructor that is called when inflating a Preference from XML. This is
     * called when a Preference is being constructed from an XML file, supplying
     * attributes that were specified in the XML file. This version uses a
     * default style of 0, so the only attribute values applied are those in the
     * Context's Theme and the given AttributeSet.
     *
     * @param context
     * 		The Context this is associated with, through which it can
     * 		access the current theme, resources, {@link SharedPreferences},
     * 		etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the
     * 		preference.
     * @see #Preference(Context, AttributeSet, int)
     */
    public Preference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.preferenceStyle);
    }

    /**
     * Constructor to create a Preference.
     *
     * @param context
     * 		The Context in which to store Preference values.
     */
    public Preference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Called when a Preference is being inflated and the default value
     * attribute needs to be read. Since different Preference types have
     * different value types, the subclass should get and return the default
     * value which will be its value type.
     * <p>
     * For example, if the value type is String, the body of the method would
     * proxy to {@link TypedArray#getString(int)}.
     *
     * @param a
     * 		The set of attributes.
     * @param index
     * 		The index of the default value attribute.
     * @return The default value of this preference type.
     */
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return null;
    }

    /**
     * Sets an {@link Intent} to be used for
     * {@link Context#startActivity(Intent)} when this Preference is clicked.
     *
     * @param intent
     * 		The intent associated with this Preference.
     */
    public void setIntent(android.content.Intent intent) {
        mIntent = intent;
    }

    /**
     * Return the {@link Intent} associated with this Preference.
     *
     * @return The {@link Intent} last set via {@link #setIntent(Intent)} or XML.
     */
    public android.content.Intent getIntent() {
        return mIntent;
    }

    /**
     * Sets the class name of a fragment to be shown when this Preference is clicked.
     *
     * @param fragment
     * 		The class name of the fragment associated with this Preference.
     */
    public void setFragment(java.lang.String fragment) {
        mFragment = fragment;
    }

    /**
     * Return the fragment class name associated with this Preference.
     *
     * @return The fragment class name last set via {@link #setFragment} or XML.
     */
    public java.lang.String getFragment() {
        return mFragment;
    }

    /**
     * Sets a {@link PreferenceDataStore} to be used by this Preference instead of using
     * {@link android.content.SharedPreferences}.
     *
     * <p>The data store will remain assigned even if the Preference is moved around the preference
     * hierarchy. It will also override a data store propagated from the {@link PreferenceManager}
     * that owns this Preference.
     *
     * @param dataStore
     * 		The {@link PreferenceDataStore} to be used by this Preference.
     * @see PreferenceManager#setPreferenceDataStore(PreferenceDataStore)
     */
    public void setPreferenceDataStore(android.preference.PreferenceDataStore dataStore) {
        mPreferenceDataStore = dataStore;
    }

    /**
     * Returns {@link PreferenceDataStore} used by this Preference. Returns {@code null} if
     * {@link android.content.SharedPreferences} is used instead.
     *
     * <p>By default preferences always use {@link android.content.SharedPreferences}. To make this
     * preference to use the {@link PreferenceDataStore} you need to assign your implementation
     * to the Preference itself via {@link #setPreferenceDataStore(PreferenceDataStore)} or to its
     * {@link PreferenceManager} via
     * {@link PreferenceManager#setPreferenceDataStore(PreferenceDataStore)}.
     *
     * @return The {@link PreferenceDataStore} used by this Preference or {@code null} if none.
     */
    @android.annotation.Nullable
    public android.preference.PreferenceDataStore getPreferenceDataStore() {
        if (mPreferenceDataStore != null) {
            return mPreferenceDataStore;
        } else
            if (mPreferenceManager != null) {
                return mPreferenceManager.getPreferenceDataStore();
            }

        return null;
    }

    /**
     * Return the extras Bundle object associated with this preference, creating
     * a new Bundle if there currently isn't one.  You can use this to get and
     * set individual extra key/value pairs.
     */
    public android.os.Bundle getExtras() {
        if (mExtras == null) {
            mExtras = new android.os.Bundle();
        }
        return mExtras;
    }

    /**
     * Return the extras Bundle object associated with this preference, returning {@code null} if
     * there is not currently one.
     */
    public android.os.Bundle peekExtras() {
        return mExtras;
    }

    /**
     * Sets the layout resource that is inflated as the {@link View} to be shown
     * for this Preference. In most cases, the default layout is sufficient for
     * custom Preference objects and only the widget layout needs to be changed.
     * <p>
     * This layout should contain a {@link ViewGroup} with ID
     * {@link android.R.id#widget_frame} to be the parent of the specific widget
     * for this Preference. It should similarly contain
     * {@link android.R.id#title} and {@link android.R.id#summary}.
     *
     * @param layoutResId
     * 		The layout resource ID to be inflated and returned as
     * 		a {@link View}.
     * @see #setWidgetLayoutResource(int)
     */
    public void setLayoutResource(@android.annotation.LayoutRes
    int layoutResId) {
        if (layoutResId != mLayoutResId) {
            // Layout changed
            mRecycleEnabled = false;
        }
        mLayoutResId = layoutResId;
    }

    /**
     * Gets the layout resource that will be shown as the {@link View} for this Preference.
     *
     * @return The layout resource ID.
     */
    @android.annotation.LayoutRes
    public int getLayoutResource() {
        return mLayoutResId;
    }

    /**
     * Sets the layout for the controllable widget portion of this Preference. This
     * is inflated into the main layout. For example, a {@link CheckBoxPreference}
     * would specify a custom layout (consisting of just the CheckBox) here,
     * instead of creating its own main layout.
     *
     * @param widgetLayoutResId
     * 		The layout resource ID to be inflated into the
     * 		main layout.
     * @see #setLayoutResource(int)
     */
    public void setWidgetLayoutResource(@android.annotation.LayoutRes
    int widgetLayoutResId) {
        if (widgetLayoutResId != mWidgetLayoutResId) {
            // Layout changed
            mRecycleEnabled = false;
        }
        mWidgetLayoutResId = widgetLayoutResId;
    }

    /**
     * Gets the layout resource for the controllable widget portion of this Preference.
     *
     * @return The layout resource ID.
     */
    @android.annotation.LayoutRes
    public int getWidgetLayoutResource() {
        return mWidgetLayoutResId;
    }

    /**
     * Gets the View that will be shown in the {@link PreferenceActivity}.
     *
     * @param convertView
     * 		The old View to reuse, if possible. Note: You should
     * 		check that this View is non-null and of an appropriate type
     * 		before using. If it is not possible to convert this View to
     * 		display the correct data, this method can create a new View.
     * @param parent
     * 		The parent that this View will eventually be attached to.
     * @return Returns the same Preference object, for chaining multiple calls
    into a single statement.
     * @see #onCreateView(ViewGroup)
     * @see #onBindView(View)
     */
    public android.view.View getView(android.view.View convertView, android.view.ViewGroup parent) {
        if (convertView == null) {
            convertView = onCreateView(parent);
        }
        onBindView(convertView);
        return convertView;
    }

    /**
     * Creates the View to be shown for this Preference in the
     * {@link PreferenceActivity}. The default behavior is to inflate the main
     * layout of this Preference (see {@link #setLayoutResource(int)}. If
     * changing this behavior, please specify a {@link ViewGroup} with ID
     * {@link android.R.id#widget_frame}.
     * <p>
     * Make sure to call through to the superclass's implementation.
     *
     * @param parent
     * 		The parent that this View will eventually be attached to.
     * @return The View that displays this Preference.
     * @see #onBindView(View)
     */
    @android.annotation.CallSuper
    protected android.view.View onCreateView(android.view.ViewGroup parent) {
        final android.view.LayoutInflater layoutInflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final android.view.View layout = layoutInflater.inflate(mLayoutResId, parent, false);
        final android.view.ViewGroup widgetFrame = ((android.view.ViewGroup) (layout.findViewById(com.android.internal.R.id.widget_frame)));
        if (widgetFrame != null) {
            if (mWidgetLayoutResId != 0) {
                layoutInflater.inflate(mWidgetLayoutResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(android.view.View.GONE);
            }
        }
        return layout;
    }

    /**
     * Binds the created View to the data for this Preference.
     * <p>
     * This is a good place to grab references to custom Views in the layout and
     * set properties on them.
     * <p>
     * Make sure to call through to the superclass's implementation.
     *
     * @param view
     * 		The View that shows this Preference.
     * @see #onCreateView(ViewGroup)
     */
    @android.annotation.CallSuper
    protected void onBindView(android.view.View view) {
        final android.widget.TextView titleView = ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.title)));
        if (titleView != null) {
            final java.lang.CharSequence title = getTitle();
            if (!android.text.TextUtils.isEmpty(title)) {
                titleView.setText(title);
                titleView.setVisibility(android.view.View.VISIBLE);
                if (mHasSingleLineTitleAttr) {
                    titleView.setSingleLine(mSingleLineTitle);
                }
            } else {
                titleView.setVisibility(android.view.View.GONE);
            }
        }
        final android.widget.TextView summaryView = ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.summary)));
        if (summaryView != null) {
            final java.lang.CharSequence summary = getSummary();
            if (!android.text.TextUtils.isEmpty(summary)) {
                summaryView.setText(summary);
                summaryView.setVisibility(android.view.View.VISIBLE);
            } else {
                summaryView.setVisibility(android.view.View.GONE);
            }
        }
        final android.widget.ImageView imageView = ((android.widget.ImageView) (view.findViewById(com.android.internal.R.id.icon)));
        if (imageView != null) {
            if ((mIconResId != 0) || (mIcon != null)) {
                if (mIcon == null) {
                    mIcon = getContext().getDrawable(mIconResId);
                }
                if (mIcon != null) {
                    imageView.setImageDrawable(mIcon);
                }
            }
            if (mIcon != null) {
                imageView.setVisibility(android.view.View.VISIBLE);
            } else {
                imageView.setVisibility(mIconSpaceReserved ? android.view.View.INVISIBLE : android.view.View.GONE);
            }
        }
        final android.view.View imageFrame = view.findViewById(com.android.internal.R.id.icon_frame);
        if (imageFrame != null) {
            if (mIcon != null) {
                imageFrame.setVisibility(android.view.View.VISIBLE);
            } else {
                imageFrame.setVisibility(mIconSpaceReserved ? android.view.View.INVISIBLE : android.view.View.GONE);
            }
        }
        if (mShouldDisableView) {
            setEnabledStateOnViews(view, isEnabled());
        }
    }

    /**
     * Makes sure the view (and any children) get the enabled state changed.
     */
    private void setEnabledStateOnViews(android.view.View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof android.view.ViewGroup) {
            final android.view.ViewGroup vg = ((android.view.ViewGroup) (v));
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(vg.getChildAt(i), enabled);
            }
        }
    }

    /**
     * Sets the order of this Preference with respect to other Preference objects on the same level.
     * If this is not specified, the default behavior is to sort alphabetically. The
     * {@link PreferenceGroup#setOrderingAsAdded(boolean)} can be used to order Preference objects
     * based on the order they appear in the XML.
     *
     * @param order
     * 		the order for this Preference. A lower value will be shown first. Use
     * 		{@link #DEFAULT_ORDER} to sort alphabetically or allow ordering from XML
     * @see PreferenceGroup#setOrderingAsAdded(boolean)
     * @see #DEFAULT_ORDER
     */
    public void setOrder(int order) {
        if (order != mOrder) {
            mOrder = order;
            // Reorder the list
            notifyHierarchyChanged();
        }
    }

    /**
     * Gets the order of this Preference with respect to other Preference objects on the same level.
     *
     * @return the order of this Preference
     * @see #setOrder(int)
     */
    public int getOrder() {
        return mOrder;
    }

    /**
     * Sets the title for this Preference with a CharSequence. This title will be placed into the ID
     * {@link android.R.id#title} within the View created by {@link #onCreateView(ViewGroup)}.
     *
     * @param title
     * 		the title for this Preference
     */
    public void setTitle(java.lang.CharSequence title) {
        if (((title == null) && (mTitle != null)) || ((title != null) && (!title.equals(mTitle)))) {
            mTitleRes = 0;
            mTitle = title;
            notifyChanged();
        }
    }

    /**
     * Sets the title for this Preference with a resource ID.
     *
     * @see #setTitle(CharSequence)
     * @param titleResId
     * 		the title as a resource ID
     */
    public void setTitle(@android.annotation.StringRes
    int titleResId) {
        setTitle(mContext.getString(titleResId));
        mTitleRes = titleResId;
    }

    /**
     * Returns the title resource ID of this Preference. If the title did not come from a resource,
     * {@code 0} is returned.
     *
     * @return the title resource
     * @see #setTitle(int)
     */
    @android.annotation.StringRes
    public int getTitleRes() {
        return mTitleRes;
    }

    /**
     * Returns the title of this Preference.
     *
     * @return the title
     * @see #setTitle(CharSequence)
     */
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Sets the icon for this Preference with a Drawable. This icon will be placed into the ID
     * {@link android.R.id#icon} within the View created by {@link #onCreateView(ViewGroup)}.
     *
     * @param icon
     * 		the optional icon for this Preference
     */
    public void setIcon(android.graphics.drawable.Drawable icon) {
        if (((icon == null) && (mIcon != null)) || ((icon != null) && (mIcon != icon))) {
            mIcon = icon;
            notifyChanged();
        }
    }

    /**
     * Sets the icon for this Preference with a resource ID.
     *
     * @see #setIcon(Drawable)
     * @param iconResId
     * 		the icon as a resource ID
     */
    public void setIcon(@android.annotation.DrawableRes
    int iconResId) {
        if (mIconResId != iconResId) {
            mIconResId = iconResId;
            setIcon(mContext.getDrawable(iconResId));
        }
    }

    /**
     * Returns the icon of this Preference.
     *
     * @return the icon
     * @see #setIcon(Drawable)
     */
    public android.graphics.drawable.Drawable getIcon() {
        if ((mIcon == null) && (mIconResId != 0)) {
            mIcon = getContext().getDrawable(mIconResId);
        }
        return mIcon;
    }

    /**
     * Returns the summary of this Preference.
     *
     * @return the summary
     * @see #setSummary(CharSequence)
     */
    public java.lang.CharSequence getSummary() {
        return mSummary;
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     *
     * @param summary
     * 		the summary for the preference
     */
    public void setSummary(java.lang.CharSequence summary) {
        if (((summary == null) && (mSummary != null)) || ((summary != null) && (!summary.equals(mSummary)))) {
            mSummary = summary;
            notifyChanged();
        }
    }

    /**
     * Sets the summary for this Preference with a resource ID.
     *
     * @see #setSummary(CharSequence)
     * @param summaryResId
     * 		the summary as a resource
     */
    public void setSummary(@android.annotation.StringRes
    int summaryResId) {
        setSummary(mContext.getString(summaryResId));
    }

    /**
     * Sets whether this Preference is enabled. If disabled, it will
     * not handle clicks.
     *
     * @param enabled
     * 		set {@code true} to enable it
     */
    public void setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;
            // Enabled state can change dependent preferences' states, so notify
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    /**
     * Checks whether this Preference should be enabled in the list.
     *
     * @return {@code true} if this Preference is enabled, false otherwise
     */
    public boolean isEnabled() {
        return (mEnabled && mDependencyMet) && mParentDependencyMet;
    }

    /**
     * Sets whether this Preference is selectable.
     *
     * @param selectable
     * 		set {@code true} to make it selectable
     */
    public void setSelectable(boolean selectable) {
        if (mSelectable != selectable) {
            mSelectable = selectable;
            notifyChanged();
        }
    }

    /**
     * Checks whether this Preference should be selectable in the list.
     *
     * @return {@code true} if it is selectable, {@code false} otherwise
     */
    public boolean isSelectable() {
        return mSelectable;
    }

    /**
     * Sets whether this Preference should disable its view when it gets disabled.
     *
     * <p>For example, set this and {@link #setEnabled(boolean)} to false for preferences that are
     * only displaying information and 1) should not be clickable 2) should not have the view set to
     * the disabled state.
     *
     * @param shouldDisableView
     * 		set {@code true} if this preference should disable its view when
     * 		the preference is disabled
     */
    public void setShouldDisableView(boolean shouldDisableView) {
        mShouldDisableView = shouldDisableView;
        notifyChanged();
    }

    /**
     * Checks whether this Preference should disable its view when it's action is disabled.
     *
     * @see #setShouldDisableView(boolean)
     * @return {@code true} if it should disable the view
     */
    public boolean getShouldDisableView() {
        return mShouldDisableView;
    }

    /**
     * Sets whether this Preference has enabled to have its view recycled when used in the list
     * view. By default the recycling is enabled.
     *
     * <p>The value can be changed only before this preference is added to the preference hierarchy.
     *
     * <p>If view recycling is not allowed then each time the list view populates this preference
     * the {@link #getView(View, ViewGroup)} method receives a {@code null} convert view and needs
     * to recreate the view. Otherwise view gets recycled and only {@link #onBindView(View)} gets
     * called.
     *
     * @param enabled
     * 		set {@code true} if this preference view should be recycled
     */
    @android.annotation.CallSuper
    public void setRecycleEnabled(boolean enabled) {
        mRecycleEnabled = enabled;
        notifyChanged();
    }

    /**
     * Checks whether this Preference has enabled to have its view recycled when used in the list
     * view.
     *
     * @see #setRecycleEnabled(boolean)
     * @return {@code true} if this preference view should be recycled
     */
    public boolean isRecycleEnabled() {
        return mRecycleEnabled;
    }

    /**
     * Sets whether to constrain the title of this Preference to a single line instead of
     * letting it wrap onto multiple lines.
     *
     * @param singleLineTitle
     * 		set {@code true} if the title should be constrained to one line
     */
    public void setSingleLineTitle(boolean singleLineTitle) {
        mHasSingleLineTitleAttr = true;
        mSingleLineTitle = singleLineTitle;
        notifyChanged();
    }

    /**
     * Gets whether the title of this preference is constrained to a single line.
     *
     * @see #setSingleLineTitle(boolean)
     * @return {@code true} if the title of this preference is constrained to a single line
     */
    public boolean isSingleLineTitle() {
        return mSingleLineTitle;
    }

    /**
     * Sets whether to reserve the space of this Preference icon view when no icon is provided.
     *
     * @param iconSpaceReserved
     * 		set {@code true} if the space for the icon view should be reserved
     */
    public void setIconSpaceReserved(boolean iconSpaceReserved) {
        mIconSpaceReserved = iconSpaceReserved;
        notifyChanged();
    }

    /**
     * Gets whether the space this preference icon view is reserved.
     *
     * @see #setIconSpaceReserved(boolean)
     * @return {@code true} if the space of this preference icon view is reserved
     */
    public boolean isIconSpaceReserved() {
        return mIconSpaceReserved;
    }

    /**
     * Returns a unique ID for this Preference.  This ID should be unique across all
     * Preference objects in a hierarchy.
     *
     * @return A unique ID for this Preference.
     */
    @android.annotation.UnsupportedAppUsage
    long getId() {
        return mId;
    }

    /**
     * Processes a click on the preference. This includes saving the value to
     * the {@link SharedPreferences}. However, the overridden method should
     * call {@link #callChangeListener(Object)} to make sure the client wants to
     * update the preference's state with the new value.
     */
    protected void onClick() {
    }

    /**
     * Sets the key for this Preference, which is used as a key to the {@link SharedPreferences} or
     * {@link PreferenceDataStore}. This should be unique for the package.
     *
     * @param key
     * 		The key for the preference.
     */
    public void setKey(java.lang.String key) {
        mKey = key;
        if (mRequiresKey && (!hasKey())) {
            requireKey();
        }
    }

    /**
     * Gets the key for this Preference, which is also the key used for storing values into
     * {@link SharedPreferences} or {@link PreferenceDataStore}.
     *
     * @return The key.
     */
    public java.lang.String getKey() {
        return mKey;
    }

    /**
     * Checks whether the key is present, and if it isn't throws an
     * exception. This should be called by subclasses that persist their preferences.
     *
     * @throws IllegalStateException
     * 		If there is no key assigned.
     */
    void requireKey() {
        if (mKey == null) {
            throw new java.lang.IllegalStateException("Preference does not have a key assigned.");
        }
        mRequiresKey = true;
    }

    /**
     * Checks whether this Preference has a valid key.
     *
     * @return True if the key exists and is not a blank string, false otherwise.
     */
    public boolean hasKey() {
        return !android.text.TextUtils.isEmpty(mKey);
    }

    /**
     * Checks whether this Preference is persistent. If it is, it stores its value(s) into
     * the persistent {@link SharedPreferences} storage by default or into
     * {@link PreferenceDataStore} if assigned.
     *
     * @return True if it is persistent.
     */
    public boolean isPersistent() {
        return mPersistent;
    }

    /**
     * Checks whether, at the given time this method is called, this Preference should store/restore
     * its value(s) into the {@link SharedPreferences} or into {@link PreferenceDataStore} if
     * assigned. This, at minimum, checks whether this Preference is persistent and it currently has
     * a key. Before you save/restore from the storage, check this first.
     *
     * @return True if it should persist the value.
     */
    protected boolean shouldPersist() {
        return ((mPreferenceManager != null) && isPersistent()) && hasKey();
    }

    /**
     * Sets whether this Preference is persistent. When persistent, it stores its value(s) into
     * the persistent {@link SharedPreferences} storage by default or into
     * {@link PreferenceDataStore} if assigned.
     *
     * @param persistent
     * 		set {@code true} if it should store its value(s) into the storage.
     */
    public void setPersistent(boolean persistent) {
        mPersistent = persistent;
    }

    /**
     * Call this method after the user changes the preference, but before the
     * internal state is set. This allows the client to ignore the user value.
     *
     * @param newValue
     * 		The new value of this Preference.
     * @return True if the user value should be set as the preference
    value (and persisted).
     */
    protected boolean callChangeListener(java.lang.Object newValue) {
        return (mOnChangeListener == null) || mOnChangeListener.onPreferenceChange(this, newValue);
    }

    /**
     * Sets the callback to be invoked when this Preference is changed by the
     * user (but before the internal state has been updated).
     *
     * @param onPreferenceChangeListener
     * 		The callback to be invoked.
     */
    public void setOnPreferenceChangeListener(android.preference.Preference.OnPreferenceChangeListener onPreferenceChangeListener) {
        mOnChangeListener = onPreferenceChangeListener;
    }

    /**
     * Returns the callback to be invoked when this Preference is changed by the
     * user (but before the internal state has been updated).
     *
     * @return The callback to be invoked.
     */
    public android.preference.Preference.OnPreferenceChangeListener getOnPreferenceChangeListener() {
        return mOnChangeListener;
    }

    /**
     * Sets the callback to be invoked when this Preference is clicked.
     *
     * @param onPreferenceClickListener
     * 		The callback to be invoked.
     */
    public void setOnPreferenceClickListener(android.preference.Preference.OnPreferenceClickListener onPreferenceClickListener) {
        mOnClickListener = onPreferenceClickListener;
    }

    /**
     * Returns the callback to be invoked when this Preference is clicked.
     *
     * @return The callback to be invoked.
     */
    public android.preference.Preference.OnPreferenceClickListener getOnPreferenceClickListener() {
        return mOnClickListener;
    }

    /**
     * Called when a click should be performed.
     *
     * @param preferenceScreen
     * 		A {@link PreferenceScreen} whose hierarchy click
     * 		listener should be called in the proper order (between other
     * 		processing). May be {@code null}.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void performClick(android.preference.PreferenceScreen preferenceScreen) {
        if (!isEnabled()) {
            return;
        }
        onClick();
        if ((mOnClickListener != null) && mOnClickListener.onPreferenceClick(this)) {
            return;
        }
        android.preference.PreferenceManager preferenceManager = getPreferenceManager();
        if (preferenceManager != null) {
            android.preference.PreferenceManager.OnPreferenceTreeClickListener listener = preferenceManager.getOnPreferenceTreeClickListener();
            if (((preferenceScreen != null) && (listener != null)) && listener.onPreferenceTreeClick(preferenceScreen, this)) {
                return;
            }
        }
        if (mIntent != null) {
            android.content.Context context = getContext();
            context.startActivity(mIntent);
        }
    }

    /**
     * Allows a Preference to intercept key events without having focus.
     * For example, SeekBarPreference uses this to intercept +/- to adjust
     * the progress.
     *
     * @return True if the Preference handled the key. Returns false by default.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        return false;
    }

    /**
     * Returns the {@link android.content.Context} of this Preference.
     * Each Preference in a Preference hierarchy can be
     * from different Context (for example, if multiple activities provide preferences into a single
     * {@link PreferenceActivity}). This Context will be used to save the Preference values.
     *
     * @return The Context of this Preference.
     */
    public android.content.Context getContext() {
        return mContext;
    }

    /**
     * Returns the {@link SharedPreferences} where this Preference can read its
     * value(s). Usually, it's easier to use one of the helper read methods:
     * {@link #getPersistedBoolean(boolean)}, {@link #getPersistedFloat(float)},
     * {@link #getPersistedInt(int)}, {@link #getPersistedLong(long)},
     * {@link #getPersistedString(String)}. To save values, see
     * {@link #getEditor()}.
     * <p>
     * In some cases, writes to the {@link #getEditor()} will not be committed
     * right away and hence not show up in the returned
     * {@link SharedPreferences}, this is intended behavior to improve
     * performance.
     *
     * @return the {@link SharedPreferences} where this Preference reads its value(s). If
    this preference isn't attached to a Preference hierarchy or if
    a {@link PreferenceDataStore} has been set, this method returns {@code null}.
     * @see #getEditor()
     * @see #setPreferenceDataStore(PreferenceDataStore)
     */
    public android.content.SharedPreferences getSharedPreferences() {
        if ((mPreferenceManager == null) || (getPreferenceDataStore() != null)) {
            return null;
        }
        return mPreferenceManager.getSharedPreferences();
    }

    /**
     * Returns an {@link SharedPreferences.Editor} where this Preference can
     * save its value(s). Usually it's easier to use one of the helper save
     * methods: {@link #persistBoolean(boolean)}, {@link #persistFloat(float)},
     * {@link #persistInt(int)}, {@link #persistLong(long)},
     * {@link #persistString(String)}. To read values, see
     * {@link #getSharedPreferences()}. If {@link #shouldCommit()} returns
     * true, it is this Preference's responsibility to commit.
     * <p>
     * In some cases, writes to this will not be committed right away and hence
     * not show up in the SharedPreferences, this is intended behavior to
     * improve performance.
     *
     * @return a {@link SharedPreferences.Editor} where this preference saves its value(s). If
    this preference isn't attached to a Preference hierarchy or if
    a {@link PreferenceDataStore} has been set, this method returns {@code null}.
     * @see #shouldCommit()
     * @see #getSharedPreferences()
     * @see #setPreferenceDataStore(PreferenceDataStore)
     */
    public android.content.SharedPreferences.Editor getEditor() {
        if ((mPreferenceManager == null) || (getPreferenceDataStore() != null)) {
            return null;
        }
        return mPreferenceManager.getEditor();
    }

    /**
     * Returns whether the {@link Preference} should commit its saved value(s) in
     * {@link #getEditor()}. This may return false in situations where batch
     * committing is being done (by the manager) to improve performance.
     *
     * <p>If this preference is using {@link PreferenceDataStore} this value is irrelevant.
     *
     * @return Whether the Preference should commit its saved value(s).
     * @see #getEditor()
     */
    public boolean shouldCommit() {
        if (mPreferenceManager == null) {
            return false;
        }
        return mPreferenceManager.shouldCommit();
    }

    /**
     * Compares Preference objects based on order (if set), otherwise alphabetically on the titles.
     *
     * @param another
     * 		The Preference to compare to this one.
     * @return 0 if the same; less than 0 if this Preference sorts ahead of <var>another</var>;
    greater than 0 if this Preference sorts after <var>another</var>.
     */
    @java.lang.Override
    public int compareTo(android.preference.Preference another) {
        if (mOrder != another.mOrder) {
            // Do order comparison
            return mOrder - another.mOrder;
        } else
            if (mTitle == another.mTitle) {
                // If titles are null or share same object comparison
                return 0;
            } else
                if (mTitle == null) {
                    return 1;
                } else
                    if (another.mTitle == null) {
                        return -1;
                    } else {
                        // Do name comparison
                        return com.android.internal.util.CharSequences.compareToIgnoreCase(mTitle, another.mTitle);
                    }



    }

    /**
     * Sets the internal change listener.
     *
     * @param listener
     * 		The listener.
     * @see #notifyChanged()
     */
    @android.annotation.UnsupportedAppUsage
    final void setOnPreferenceChangeInternalListener(android.preference.Preference.OnPreferenceChangeInternalListener listener) {
        mListener = listener;
    }

    /**
     * Should be called when the data of this {@link Preference} has changed.
     */
    protected void notifyChanged() {
        if (mListener != null) {
            mListener.onPreferenceChange(this);
        }
    }

    /**
     * Should be called when a Preference has been
     * added/removed from this group, or the ordering should be
     * re-evaluated.
     */
    protected void notifyHierarchyChanged() {
        if (mListener != null) {
            mListener.onPreferenceHierarchyChange(this);
        }
    }

    /**
     * Gets the {@link PreferenceManager} that manages this Preference object's tree.
     *
     * @return The {@link PreferenceManager}.
     */
    public android.preference.PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    /**
     * Called when this Preference has been attached to a Preference hierarchy.
     * Make sure to call the super implementation.
     *
     * @param preferenceManager
     * 		The PreferenceManager of the hierarchy.
     */
    protected void onAttachedToHierarchy(android.preference.PreferenceManager preferenceManager) {
        mPreferenceManager = preferenceManager;
        mId = preferenceManager.getNextId();
        dispatchSetInitialValue();
    }

    /**
     * Called when the Preference hierarchy has been attached to the
     * {@link PreferenceActivity}. This can also be called when this
     * Preference has been attached to a group that was already attached
     * to the {@link PreferenceActivity}.
     */
    protected void onAttachedToActivity() {
        // At this point, the hierarchy that this preference is in is connected
        // with all other preferences.
        registerDependency();
    }

    /**
     * Assigns a {@link PreferenceGroup} as the parent of this Preference. Set {@code null} to
     * remove the current parent.
     *
     * @param parentGroup
     * 		Parent preference group of this Preference or {@code null} if none.
     */
    void assignParent(@android.annotation.Nullable
    android.preference.PreferenceGroup parentGroup) {
        mParentGroup = parentGroup;
    }

    private void registerDependency() {
        if (android.text.TextUtils.isEmpty(mDependencyKey))
            return;

        android.preference.Preference preference = findPreferenceInHierarchy(mDependencyKey);
        if (preference != null) {
            preference.registerDependent(this);
        } else {
            throw new java.lang.IllegalStateException(((((("Dependency \"" + mDependencyKey) + "\" not found for preference \"") + mKey) + "\" (title: \"") + mTitle) + "\"");
        }
    }

    private void unregisterDependency() {
        if (mDependencyKey != null) {
            final android.preference.Preference oldDependency = findPreferenceInHierarchy(mDependencyKey);
            if (oldDependency != null) {
                oldDependency.unregisterDependent(this);
            }
        }
    }

    /**
     * Finds a Preference in this hierarchy (the whole thing,
     * even above/below your {@link PreferenceScreen} screen break) with the given
     * key.
     * <p>
     * This only functions after we have been attached to a hierarchy.
     *
     * @param key
     * 		The key of the Preference to find.
     * @return The Preference that uses the given key.
     */
    protected android.preference.Preference findPreferenceInHierarchy(java.lang.String key) {
        if (android.text.TextUtils.isEmpty(key) || (mPreferenceManager == null)) {
            return null;
        }
        return mPreferenceManager.findPreference(key);
    }

    /**
     * Adds a dependent Preference on this Preference so we can notify it.
     * Usually, the dependent Preference registers itself (it's good for it to
     * know it depends on something), so please use
     * {@link Preference#setDependency(String)} on the dependent Preference.
     *
     * @param dependent
     * 		The dependent Preference that will be enabled/disabled
     * 		according to the state of this Preference.
     */
    @android.annotation.UnsupportedAppUsage
    private void registerDependent(android.preference.Preference dependent) {
        if (mDependents == null) {
            mDependents = new java.util.ArrayList<android.preference.Preference>();
        }
        mDependents.add(dependent);
        dependent.onDependencyChanged(this, shouldDisableDependents());
    }

    /**
     * Removes a dependent Preference on this Preference.
     *
     * @param dependent
     * 		The dependent Preference that will be enabled/disabled
     * 		according to the state of this Preference.
     * @return Returns the same Preference object, for chaining multiple calls
    into a single statement.
     */
    private void unregisterDependent(android.preference.Preference dependent) {
        if (mDependents != null) {
            mDependents.remove(dependent);
        }
    }

    /**
     * Notifies any listening dependents of a change that affects the
     * dependency.
     *
     * @param disableDependents
     * 		Whether this Preference should disable
     * 		its dependents.
     */
    public void notifyDependencyChange(boolean disableDependents) {
        final java.util.List<android.preference.Preference> dependents = mDependents;
        if (dependents == null) {
            return;
        }
        final int dependentsCount = dependents.size();
        for (int i = 0; i < dependentsCount; i++) {
            dependents.get(i).onDependencyChanged(this, disableDependents);
        }
    }

    /**
     * Called when the dependency changes.
     *
     * @param dependency
     * 		The Preference that this Preference depends on.
     * @param disableDependent
     * 		Set true to disable this Preference.
     */
    public void onDependencyChanged(android.preference.Preference dependency, boolean disableDependent) {
        if (mDependencyMet == disableDependent) {
            mDependencyMet = !disableDependent;
            // Enabled state can change dependent preferences' states, so notify
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    /**
     * Called when the implicit parent dependency changes.
     *
     * @param parent
     * 		The Preference that this Preference depends on.
     * @param disableChild
     * 		Set true to disable this Preference.
     */
    public void onParentChanged(android.preference.Preference parent, boolean disableChild) {
        if (mParentDependencyMet == disableChild) {
            mParentDependencyMet = !disableChild;
            // Enabled state can change dependent preferences' states, so notify
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    /**
     * Checks whether this preference's dependents should currently be
     * disabled.
     *
     * @return True if the dependents should be disabled, otherwise false.
     */
    public boolean shouldDisableDependents() {
        return !isEnabled();
    }

    /**
     * Sets the key of a Preference that this Preference will depend on. If that
     * Preference is not set or is off, this Preference will be disabled.
     *
     * @param dependencyKey
     * 		The key of the Preference that this depends on.
     */
    public void setDependency(java.lang.String dependencyKey) {
        // Unregister the old dependency, if we had one
        unregisterDependency();
        // Register the new
        mDependencyKey = dependencyKey;
        registerDependency();
    }

    /**
     * Returns the key of the dependency on this Preference.
     *
     * @return The key of the dependency.
     * @see #setDependency(String)
     */
    public java.lang.String getDependency() {
        return mDependencyKey;
    }

    /**
     * Returns the {@link PreferenceGroup} which is this Preference assigned to or {@code null} if
     * this preference is not assigned to any group or is a root Preference.
     *
     * @return the parent PreferenceGroup or {@code null} if not attached to any
     */
    @android.annotation.Nullable
    public android.preference.PreferenceGroup getParent() {
        return mParentGroup;
    }

    /**
     * Called when this Preference is being removed from the hierarchy. You
     * should remove any references to this Preference that you know about. Make
     * sure to call through to the superclass implementation.
     */
    @android.annotation.CallSuper
    protected void onPrepareForRemoval() {
        unregisterDependency();
    }

    /**
     * Sets the default value for this Preference, which will be set either if
     * persistence is off or persistence is on and the preference is not found
     * in the persistent storage.
     *
     * @param defaultValue
     * 		The default value.
     */
    public void setDefaultValue(java.lang.Object defaultValue) {
        mDefaultValue = defaultValue;
    }

    private void dispatchSetInitialValue() {
        if (getPreferenceDataStore() != null) {
            onSetInitialValue(true, mDefaultValue);
            return;
        }
        // By now, we know if we are persistent.
        final boolean shouldPersist = shouldPersist();
        if ((!shouldPersist) || (!getSharedPreferences().contains(mKey))) {
            if (mDefaultValue != null) {
                onSetInitialValue(false, mDefaultValue);
            }
        } else {
            onSetInitialValue(true, null);
        }
    }

    /**
     * Implement this to set the initial value of the Preference.
     *
     * <p>If <var>restorePersistedValue</var> is true, you should restore the
     * Preference value from the {@link android.content.SharedPreferences}. If
     * <var>restorePersistedValue</var> is false, you should set the Preference
     * value to defaultValue that is given (and possibly store to SharedPreferences
     * if {@link #shouldPersist()} is true).
     *
     * <p>In case of using {@link PreferenceDataStore}, the <var>restorePersistedValue</var> is
     * always {@code true}. But the default value (if provided) is set.
     *
     * <p>This may not always be called. One example is if it should not persist
     * but there is no default value given.
     *
     * @param restorePersistedValue
     * 		True to restore the persisted value;
     * 		false to use the given <var>defaultValue</var>.
     * @param defaultValue
     * 		The default value for this Preference. Only use this
     * 		if <var>restorePersistedValue</var> is false.
     */
    protected void onSetInitialValue(boolean restorePersistedValue, java.lang.Object defaultValue) {
    }

    private void tryCommit(android.content.SharedPreferences.Editor editor) {
        if (mPreferenceManager.shouldCommit()) {
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
     * Attempts to persist a String if this Preference is persistent.
     *
     * @param value
     * 		The value to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #getPersistedString(String)
     */
    protected boolean persistString(java.lang.String value) {
        if (!shouldPersist()) {
            return false;
        }
        // Shouldn't store null
        if (android.text.TextUtils.equals(value, getPersistedString(null))) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putString(mKey, value);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putString(mKey, value);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted String if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #persistString(String)
     */
    protected java.lang.String getPersistedString(java.lang.String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getString(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getString(mKey, defaultReturnValue);
    }

    /**
     * Attempts to persist a set of Strings if this Preference is persistent.
     *
     * @param values
     * 		The values to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #getPersistedStringSet(Set)
     */
    public boolean persistStringSet(java.util.Set<java.lang.String> values) {
        if (!shouldPersist()) {
            return false;
        }
        // Shouldn't store null
        if (values.equals(getPersistedStringSet(null))) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putStringSet(mKey, values);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putStringSet(mKey, values);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted set of Strings if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #persistStringSet(Set)
     */
    public java.util.Set<java.lang.String> getPersistedStringSet(java.util.Set<java.lang.String> defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getStringSet(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getStringSet(mKey, defaultReturnValue);
    }

    /**
     * Attempts to persist an int if this Preference is persistent.
     *
     * @param value
     * 		The value to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #persistString(String)
     * @see #getPersistedInt(int)
     */
    protected boolean persistInt(int value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedInt(~value)) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putInt(mKey, value);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putInt(mKey, value);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted int if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #getPersistedString(String)
     * @see #persistInt(int)
     */
    protected int getPersistedInt(int defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getInt(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getInt(mKey, defaultReturnValue);
    }

    /**
     * Attempts to persist a long if this Preference is persistent.
     *
     * @param value
     * 		The value to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #persistString(String)
     * @see #getPersistedFloat(float)
     */
    protected boolean persistFloat(float value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedFloat(java.lang.Float.NaN)) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putFloat(mKey, value);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putFloat(mKey, value);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted float if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #getPersistedString(String)
     * @see #persistFloat(float)
     */
    protected float getPersistedFloat(float defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getFloat(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getFloat(mKey, defaultReturnValue);
    }

    /**
     * Attempts to persist a long if this Preference is persistent.
     *
     * @param value
     * 		The value to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #persistString(String)
     * @see #getPersistedLong(long)
     */
    protected boolean persistLong(long value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedLong(~value)) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putLong(mKey, value);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putLong(mKey, value);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted long if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #getPersistedString(String)
     * @see #persistLong(long)
     */
    protected long getPersistedLong(long defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getLong(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getLong(mKey, defaultReturnValue);
    }

    /**
     * Attempts to persist a boolean if this Preference is persistent.
     *
     * @param value
     * 		The value to persist.
     * @return True if this Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #persistString(String)
     * @see #getPersistedBoolean(boolean)
     */
    protected boolean persistBoolean(boolean value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedBoolean(!value)) {
            // It's already there, so the same as persisting
            return true;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putBoolean(mKey, value);
        } else {
            android.content.SharedPreferences.Editor editor = mPreferenceManager.getEditor();
            editor.putBoolean(mKey, value);
            tryCommit(editor);
        }
        return true;
    }

    /**
     * Attempts to get a persisted boolean if this Preference is persistent.
     *
     * @param defaultReturnValue
     * 		The default value to return if either this
     * 		Preference is not persistent or this Preference is not present.
     * @return The value from the data store or the default return
    value.
     * @see #getPersistedString(String)
     * @see #persistBoolean(boolean)
     */
    protected boolean getPersistedBoolean(boolean defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        android.preference.PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getBoolean(mKey, defaultReturnValue);
        }
        return mPreferenceManager.getSharedPreferences().getBoolean(mKey, defaultReturnValue);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getFilterableStringBuilder().toString();
    }

    /**
     * Returns the text that will be used to filter this Preference depending on
     * user input.
     * <p>
     * If overridding and calling through to the superclass, make sure to prepend
     * your additions with a space.
     *
     * @return Text as a {@link StringBuilder} that will be used to filter this
    preference. By default, this is the title and summary
    (concatenated with a space).
     */
    java.lang.StringBuilder getFilterableStringBuilder() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.CharSequence title = getTitle();
        if (!android.text.TextUtils.isEmpty(title)) {
            sb.append(title).append(' ');
        }
        java.lang.CharSequence summary = getSummary();
        if (!android.text.TextUtils.isEmpty(summary)) {
            sb.append(summary).append(' ');
        }
        if (sb.length() > 0) {
            // Drop the last space
            sb.setLength(sb.length() - 1);
        }
        return sb;
    }

    /**
     * Store this Preference hierarchy's frozen state into the given container.
     *
     * @param container
     * 		The Bundle in which to save the instance of this Preference.
     * @see #restoreHierarchyState
     * @see #onSaveInstanceState
     */
    public void saveHierarchyState(android.os.Bundle container) {
        dispatchSaveInstanceState(container);
    }

    /**
     * Called by {@link #saveHierarchyState} to store the instance for this Preference and its
     * children. May be overridden to modify how the save happens for children. For example, some
     * Preference objects may want to not store an instance for their children.
     *
     * @param container
     * 		The Bundle in which to save the instance of this Preference.
     * @see #saveHierarchyState
     * @see #onSaveInstanceState
     */
    void dispatchSaveInstanceState(android.os.Bundle container) {
        if (hasKey()) {
            mBaseMethodCalled = false;
            android.os.Parcelable state = onSaveInstanceState();
            if (!mBaseMethodCalled) {
                throw new java.lang.IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            }
            if (state != null) {
                container.putParcelable(mKey, state);
            }
        }
    }

    /**
     * Hook allowing a Preference to generate a representation of its internal
     * state that can later be used to create a new instance with that same
     * state. This state should only contain information that is not persistent
     * or can be reconstructed later.
     *
     * @return A Parcelable object containing the current dynamic state of this Preference, or
    {@code null} if there is nothing interesting to save. The default implementation
    returns {@code null}.
     * @see #onRestoreInstanceState
     * @see #saveHierarchyState
     */
    protected android.os.Parcelable onSaveInstanceState() {
        mBaseMethodCalled = true;
        return android.preference.Preference.BaseSavedState.EMPTY_STATE;
    }

    /**
     * Restore this Preference hierarchy's previously saved state from the given container.
     *
     * @param container
     * 		The Bundle that holds the previously saved state.
     * @see #saveHierarchyState
     * @see #onRestoreInstanceState
     */
    public void restoreHierarchyState(android.os.Bundle container) {
        dispatchRestoreInstanceState(container);
    }

    /**
     * Called by {@link #restoreHierarchyState} to retrieve the saved state for this
     * Preference and its children. May be overridden to modify how restoring
     * happens to the children of a Preference. For example, some Preference objects may
     * not want to save state for their children.
     *
     * @param container
     * 		The Bundle that holds the previously saved state.
     * @see #restoreHierarchyState
     * @see #onRestoreInstanceState
     */
    void dispatchRestoreInstanceState(android.os.Bundle container) {
        if (hasKey()) {
            android.os.Parcelable state = container.getParcelable(mKey);
            if (state != null) {
                mBaseMethodCalled = false;
                onRestoreInstanceState(state);
                if (!mBaseMethodCalled) {
                    throw new java.lang.IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }
    }

    /**
     * Hook allowing a Preference to re-apply a representation of its internal state that had
     * previously been generated by {@link #onSaveInstanceState}. This function will never be called
     * with a {@code null} state.
     *
     * @param state
     * 		The saved state that had previously been returned by
     * 		{@link #onSaveInstanceState}.
     * @see #onSaveInstanceState
     * @see #restoreHierarchyState
     */
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        mBaseMethodCalled = true;
        if ((state != android.preference.Preference.BaseSavedState.EMPTY_STATE) && (state != null)) {
            throw new java.lang.IllegalArgumentException("Wrong state class -- expecting Preference State");
        }
    }

    /**
     * A base class for managing the instance state of a {@link Preference}.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public static class BaseSavedState extends android.view.AbsSavedState {
        public BaseSavedState(android.os.Parcel source) {
            super(source);
        }

        public BaseSavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.Preference.BaseSavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.Preference.BaseSavedState>() {
            public android.preference.BaseSavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.BaseSavedState(in);
            }

            public android.preference.BaseSavedState[] newArray(int size) {
                return new android.preference.BaseSavedState[size];
            }
        };
    }
}

