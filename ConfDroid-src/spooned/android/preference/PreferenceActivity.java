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
 * This is the base class for an activity to show a hierarchy of preferences
 * to the user.  Prior to {@link android.os.Build.VERSION_CODES#HONEYCOMB}
 * this class only allowed the display of a single set of preference; this
 * functionality should now be found in the new {@link PreferenceFragment}
 * class.  If you are using PreferenceActivity in its old mode, the documentation
 * there applies to the deprecated APIs here.
 *
 * <p>This activity shows one or more headers of preferences, each of which
 * is associated with a {@link PreferenceFragment} to display the preferences
 * of that header.  The actual layout and display of these associations can
 * however vary; currently there are two major approaches it may take:
 *
 * <ul>
 * <li>On a small screen it may display only the headers as a single list when first launched.
 * Selecting one of the header items will only show the PreferenceFragment of that header (on
 * Android N and lower a new Activity is launched).
 * <li>On a large screen it may display both the headers and current PreferenceFragment together as
 * panes. Selecting a header item switches to showing the correct PreferenceFragment for that item.
 * </ul>
 *
 * <p>Subclasses of PreferenceActivity should implement
 * {@link #onBuildHeaders} to populate the header list with the desired
 * items.  Doing this implicitly switches the class into its new "headers
 * + fragments" mode rather than the old style of just showing a single
 * preferences list.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using {@code PreferenceActivity},
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public abstract class PreferenceActivity extends android.app.ListActivity implements android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback , android.preference.PreferenceManager.OnPreferenceTreeClickListener {
    private static final java.lang.String TAG = "PreferenceActivity";

    // Constants for state save/restore
    private static final java.lang.String HEADERS_TAG = ":android:headers";

    private static final java.lang.String CUR_HEADER_TAG = ":android:cur_header";

    private static final java.lang.String PREFERENCES_TAG = ":android:preferences";

    /**
     * When starting this activity, the invoking Intent can contain this extra
     * string to specify which fragment should be initially displayed.
     * <p/>Starting from Key Lime Pie, when this argument is passed in, the PreferenceActivity
     * will call isValidFragment() to confirm that the fragment class name is valid for this
     * activity.
     */
    public static final java.lang.String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";

    /**
     * When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
     * this extra can also be specified to supply a Bundle of arguments to pass
     * to that fragment when it is instantiated during the initial creation
     * of PreferenceActivity.
     */
    public static final java.lang.String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":android:show_fragment_args";

    /**
     * When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
     * this extra can also be specify to supply the title to be shown for
     * that fragment.
     */
    public static final java.lang.String EXTRA_SHOW_FRAGMENT_TITLE = ":android:show_fragment_title";

    /**
     * When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
     * this extra can also be specify to supply the short title to be shown for
     * that fragment.
     */
    public static final java.lang.String EXTRA_SHOW_FRAGMENT_SHORT_TITLE = ":android:show_fragment_short_title";

    /**
     * When starting this activity, the invoking Intent can contain this extra
     * boolean that the header list should not be displayed.  This is most often
     * used in conjunction with {@link #EXTRA_SHOW_FRAGMENT} to launch
     * the activity to display a specific fragment that the user has navigated
     * to.
     */
    public static final java.lang.String EXTRA_NO_HEADERS = ":android:no_headers";

    private static final java.lang.String BACK_STACK_PREFS = ":android:prefs";

    // extras that allow any preference activity to be launched as part of a wizard
    // show Back and Next buttons? takes boolean parameter
    // Back will then return RESULT_CANCELED and Next RESULT_OK
    private static final java.lang.String EXTRA_PREFS_SHOW_BUTTON_BAR = "extra_prefs_show_button_bar";

    // add a Skip button?
    private static final java.lang.String EXTRA_PREFS_SHOW_SKIP = "extra_prefs_show_skip";

    // specify custom text for the Back or Next buttons, or cause a button to not appear
    // at all by setting it to null
    private static final java.lang.String EXTRA_PREFS_SET_NEXT_TEXT = "extra_prefs_set_next_text";

    private static final java.lang.String EXTRA_PREFS_SET_BACK_TEXT = "extra_prefs_set_back_text";

    // --- State for new mode when showing a list of headers + prefs fragment
    private final java.util.ArrayList<android.preference.PreferenceActivity.Header> mHeaders = new java.util.ArrayList<android.preference.PreferenceActivity.Header>();

    private android.widget.FrameLayout mListFooter;

    @android.annotation.UnsupportedAppUsage
    private android.view.ViewGroup mPrefsContainer;

    // Backup of the original activity title. This is used when navigating back to the headers list
    // in onBackPress to restore the title.
    private java.lang.CharSequence mActivityTitle;

    // Null if in legacy mode.
    private android.view.ViewGroup mHeadersContainer;

    private android.app.FragmentBreadCrumbs mFragmentBreadCrumbs;

    private boolean mSinglePane;

    private android.preference.PreferenceActivity.Header mCurHeader;

    // --- State for old mode when showing a single preference list
    @android.annotation.UnsupportedAppUsage
    private android.preference.PreferenceManager mPreferenceManager;

    private android.os.Bundle mSavedInstanceState;

    // --- Common state
    private android.widget.Button mNextButton;

    private int mPreferenceHeaderItemResId = 0;

    private boolean mPreferenceHeaderRemoveEmptyIcon = false;

    /**
     * The starting request code given out to preference framework.
     */
    private static final int FIRST_REQUEST_CODE = 100;

    private static final int MSG_BIND_PREFERENCES = 1;

    private static final int MSG_BUILD_HEADERS = 2;

    private android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.preference.PreferenceActivity.MSG_BIND_PREFERENCES :
                    {
                        bindPreferences();
                    }
                    break;
                case android.preference.PreferenceActivity.MSG_BUILD_HEADERS :
                    {
                        java.util.ArrayList<android.preference.PreferenceActivity.Header> oldHeaders = new java.util.ArrayList<android.preference.PreferenceActivity.Header>(mHeaders);
                        mHeaders.clear();
                        onBuildHeaders(mHeaders);
                        if (mAdapter instanceof android.widget.BaseAdapter) {
                            ((android.widget.BaseAdapter) (mAdapter)).notifyDataSetChanged();
                        }
                        android.preference.PreferenceActivity.Header header = onGetNewHeader();
                        if ((header != null) && (header.fragment != null)) {
                            android.preference.PreferenceActivity.Header mappedHeader = findBestMatchingHeader(header, oldHeaders);
                            if ((mappedHeader == null) || (mCurHeader != mappedHeader)) {
                                switchToHeader(header);
                            }
                        } else
                            if (mCurHeader != null) {
                                android.preference.PreferenceActivity.Header mappedHeader = findBestMatchingHeader(mCurHeader, mHeaders);
                                if (mappedHeader != null) {
                                    setSelectedHeader(mappedHeader);
                                }
                            }

                    }
                    break;
            }
        }
    };

    private static class HeaderAdapter extends android.widget.ArrayAdapter<android.preference.PreferenceActivity.Header> {
        private static class HeaderViewHolder {
            android.widget.ImageView icon;

            android.widget.TextView title;

            android.widget.TextView summary;
        }

        private android.view.LayoutInflater mInflater;

        private int mLayoutResId;

        private boolean mRemoveIconIfEmpty;

        public HeaderAdapter(android.content.Context context, java.util.List<android.preference.PreferenceActivity.Header> objects, int layoutResId, boolean removeIconBehavior) {
            super(context, 0, objects);
            mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            mLayoutResId = layoutResId;
            mRemoveIconIfEmpty = removeIconBehavior;
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.preference.PreferenceActivity.HeaderAdapter.HeaderViewHolder holder;
            android.view.View view;
            if (convertView == null) {
                view = mInflater.inflate(mLayoutResId, parent, false);
                holder = new android.preference.PreferenceActivity.HeaderAdapter.HeaderViewHolder();
                holder.icon = ((android.widget.ImageView) (view.findViewById(com.android.internal.R.id.icon)));
                holder.title = ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.title)));
                holder.summary = ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.summary)));
                view.setTag(holder);
            } else {
                view = convertView;
                holder = ((android.preference.PreferenceActivity.HeaderAdapter.HeaderViewHolder) (view.getTag()));
            }
            // All view fields must be updated every time, because the view may be recycled
            android.preference.PreferenceActivity.Header header = getItem(position);
            if (mRemoveIconIfEmpty) {
                if (header.iconRes == 0) {
                    holder.icon.setVisibility(android.view.View.GONE);
                } else {
                    holder.icon.setVisibility(android.view.View.VISIBLE);
                    holder.icon.setImageResource(header.iconRes);
                }
            } else {
                holder.icon.setImageResource(header.iconRes);
            }
            holder.title.setText(header.getTitle(getContext().getResources()));
            java.lang.CharSequence summary = header.getSummary(getContext().getResources());
            if (!android.text.TextUtils.isEmpty(summary)) {
                holder.summary.setVisibility(android.view.View.VISIBLE);
                holder.summary.setText(summary);
            } else {
                holder.summary.setVisibility(android.view.View.GONE);
            }
            return view;
        }
    }

    /**
     * Default value for {@link Header#id Header.id} indicating that no
     * identifier value is set.  All other values (including those below -1)
     * are valid.
     */
    public static final long HEADER_ID_UNDEFINED = -1;

    /**
     * Description of a single Header item that the user can select.
     *
     * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
    <a href="{@docRoot }reference/androidx/preference/package-summary.html">
    Preference Library</a> for consistent behavior across all devices.
    For more information on using the AndroidX Preference Library see
    <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
     */
    @java.lang.Deprecated
    public static final class Header implements android.os.Parcelable {
        /**
         * Identifier for this header, to correlate with a new list when
         * it is updated.  The default value is
         * {@link PreferenceActivity#HEADER_ID_UNDEFINED}, meaning no id.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_id
         */
        public long id = android.preference.PreferenceActivity.HEADER_ID_UNDEFINED;

        /**
         * Resource ID of title of the header that is shown to the user.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_title
         */
        @android.annotation.StringRes
        public int titleRes;

        /**
         * Title of the header that is shown to the user.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_title
         */
        public java.lang.CharSequence title;

        /**
         * Resource ID of optional summary describing what this header controls.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_summary
         */
        @android.annotation.StringRes
        public int summaryRes;

        /**
         * Optional summary describing what this header controls.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_summary
         */
        public java.lang.CharSequence summary;

        /**
         * Resource ID of optional text to show as the title in the bread crumb.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_breadCrumbTitle
         */
        @android.annotation.StringRes
        public int breadCrumbTitleRes;

        /**
         * Optional text to show as the title in the bread crumb.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_breadCrumbTitle
         */
        public java.lang.CharSequence breadCrumbTitle;

        /**
         * Resource ID of optional text to show as the short title in the bread crumb.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_breadCrumbShortTitle
         */
        @android.annotation.StringRes
        public int breadCrumbShortTitleRes;

        /**
         * Optional text to show as the short title in the bread crumb.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_breadCrumbShortTitle
         */
        public java.lang.CharSequence breadCrumbShortTitle;

        /**
         * Optional icon resource to show for this header.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_icon
         */
        public int iconRes;

        /**
         * Full class name of the fragment to display when this header is
         * selected.
         *
         * @unknown ref android.R.styleable#PreferenceHeader_fragment
         */
        public java.lang.String fragment;

        /**
         * Optional arguments to supply to the fragment when it is
         * instantiated.
         */
        public android.os.Bundle fragmentArguments;

        /**
         * Intent to launch when the preference is selected.
         */
        public android.content.Intent intent;

        /**
         * Optional additional data for use by subclasses of PreferenceActivity.
         */
        public android.os.Bundle extras;

        public Header() {
            // Empty
        }

        /**
         * Return the currently set title.  If {@link #titleRes} is set,
         * this resource is loaded from <var>res</var> and returned.  Otherwise
         * {@link #title} is returned.
         */
        public java.lang.CharSequence getTitle(android.content.res.Resources res) {
            if (titleRes != 0) {
                return res.getText(titleRes);
            }
            return title;
        }

        /**
         * Return the currently set summary.  If {@link #summaryRes} is set,
         * this resource is loaded from <var>res</var> and returned.  Otherwise
         * {@link #summary} is returned.
         */
        public java.lang.CharSequence getSummary(android.content.res.Resources res) {
            if (summaryRes != 0) {
                return res.getText(summaryRes);
            }
            return summary;
        }

        /**
         * Return the currently set bread crumb title.  If {@link #breadCrumbTitleRes} is set,
         * this resource is loaded from <var>res</var> and returned.  Otherwise
         * {@link #breadCrumbTitle} is returned.
         */
        public java.lang.CharSequence getBreadCrumbTitle(android.content.res.Resources res) {
            if (breadCrumbTitleRes != 0) {
                return res.getText(breadCrumbTitleRes);
            }
            return breadCrumbTitle;
        }

        /**
         * Return the currently set bread crumb short title.  If
         * {@link #breadCrumbShortTitleRes} is set,
         * this resource is loaded from <var>res</var> and returned.  Otherwise
         * {@link #breadCrumbShortTitle} is returned.
         */
        public java.lang.CharSequence getBreadCrumbShortTitle(android.content.res.Resources res) {
            if (breadCrumbShortTitleRes != 0) {
                return res.getText(breadCrumbShortTitleRes);
            }
            return breadCrumbShortTitle;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeInt(titleRes);
            android.text.TextUtils.writeToParcel(title, dest, flags);
            dest.writeInt(summaryRes);
            android.text.TextUtils.writeToParcel(summary, dest, flags);
            dest.writeInt(breadCrumbTitleRes);
            android.text.TextUtils.writeToParcel(breadCrumbTitle, dest, flags);
            dest.writeInt(breadCrumbShortTitleRes);
            android.text.TextUtils.writeToParcel(breadCrumbShortTitle, dest, flags);
            dest.writeInt(iconRes);
            dest.writeString(fragment);
            dest.writeBundle(fragmentArguments);
            if (intent != null) {
                dest.writeInt(1);
                intent.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            dest.writeBundle(extras);
        }

        public void readFromParcel(android.os.Parcel in) {
            id = in.readLong();
            titleRes = in.readInt();
            title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            summaryRes = in.readInt();
            summary = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            breadCrumbTitleRes = in.readInt();
            breadCrumbTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            breadCrumbShortTitleRes = in.readInt();
            breadCrumbShortTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            iconRes = in.readInt();
            fragment = in.readString();
            fragmentArguments = in.readBundle();
            if (in.readInt() != 0) {
                intent = android.content.Intent.this.CREATOR.createFromParcel(in);
            }
            extras = in.readBundle();
        }

        Header(android.os.Parcel in) {
            readFromParcel(in);
        }

        @android.annotation.NonNull
        public static final android.preference.Creator<android.preference.PreferenceActivity.Header> CREATOR = new android.preference.Creator<android.preference.PreferenceActivity.Header>() {
            public android.preference.PreferenceActivity.Header createFromParcel(android.os.Parcel source) {
                return new android.preference.PreferenceActivity.Header(source);
            }

            public android.preference.PreferenceActivity.Header[] newArray(int size) {
                return new android.preference.PreferenceActivity.Header[size];
            }
        };
    }

    @java.lang.Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Override home navigation button to call onBackPressed (b/35152749).
            onBackPressed();
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @java.lang.Override
    protected void onCreate(@android.annotation.Nullable
    android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Theming for the PreferenceActivity layout and for the Preference Header(s) layout
        android.content.res.TypedArray sa = obtainStyledAttributes(null, com.android.internal.R.styleable.PreferenceActivity, com.android.internal.R.attr.preferenceActivityStyle, 0);
        final int layoutResId = sa.getResourceId(com.android.internal.R.styleable.PreferenceActivity_layout, com.android.internal.R.layout.preference_list_content);
        mPreferenceHeaderItemResId = sa.getResourceId(com.android.internal.R.styleable.PreferenceActivity_headerLayout, com.android.internal.R.layout.preference_header_item);
        mPreferenceHeaderRemoveEmptyIcon = sa.getBoolean(com.android.internal.R.styleable.PreferenceActivity_headerRemoveIconIfEmpty, false);
        sa.recycle();
        setContentView(layoutResId);
        mListFooter = ((android.widget.FrameLayout) (findViewById(com.android.internal.R.id.list_footer)));
        mPrefsContainer = ((android.view.ViewGroup) (findViewById(com.android.internal.R.id.prefs_frame)));
        mHeadersContainer = ((android.view.ViewGroup) (findViewById(com.android.internal.R.id.headers)));
        boolean hidingHeaders = onIsHidingHeaders();
        mSinglePane = hidingHeaders || (!onIsMultiPane());
        java.lang.String initialFragment = getIntent().getStringExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT);
        android.os.Bundle initialArguments = getIntent().getBundleExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        int initialTitle = getIntent().getIntExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_TITLE, 0);
        int initialShortTitle = getIntent().getIntExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_SHORT_TITLE, 0);
        mActivityTitle = getTitle();
        if (savedInstanceState != null) {
            // We are restarting from a previous saved state; used that to
            // initialize, instead of starting fresh.
            java.util.ArrayList<android.preference.PreferenceActivity.Header> headers = savedInstanceState.getParcelableArrayList(android.preference.PreferenceActivity.HEADERS_TAG);
            if (headers != null) {
                mHeaders.addAll(headers);
                int curHeader = savedInstanceState.getInt(android.preference.PreferenceActivity.CUR_HEADER_TAG, ((int) (android.preference.PreferenceActivity.HEADER_ID_UNDEFINED)));
                if ((curHeader >= 0) && (curHeader < mHeaders.size())) {
                    setSelectedHeader(mHeaders.get(curHeader));
                } else
                    if ((!mSinglePane) && (initialFragment == null)) {
                        switchToHeader(onGetInitialHeader());
                    }

            } else {
                // This will for instance hide breadcrumbs for single pane.
                showBreadCrumbs(getTitle(), null);
            }
        } else {
            if (!onIsHidingHeaders()) {
                onBuildHeaders(mHeaders);
            }
            if (initialFragment != null) {
                switchToHeader(initialFragment, initialArguments);
            } else
                if ((!mSinglePane) && (mHeaders.size() > 0)) {
                    switchToHeader(onGetInitialHeader());
                }

        }
        if (mHeaders.size() > 0) {
            setListAdapter(new android.preference.PreferenceActivity.HeaderAdapter(this, mHeaders, mPreferenceHeaderItemResId, mPreferenceHeaderRemoveEmptyIcon));
            if (!mSinglePane) {
                getListView().setChoiceMode(android.widget.AbsListView.CHOICE_MODE_SINGLE);
            }
        }
        if ((mSinglePane && (initialFragment != null)) && (initialTitle != 0)) {
            java.lang.CharSequence initialTitleStr = getText(initialTitle);
            java.lang.CharSequence initialShortTitleStr = (initialShortTitle != 0) ? getText(initialShortTitle) : null;
            showBreadCrumbs(initialTitleStr, initialShortTitleStr);
        }
        if ((mHeaders.size() == 0) && (initialFragment == null)) {
            // If there are no headers, we are in the old "just show a screen
            // of preferences" mode.
            setContentView(com.android.internal.R.layout.preference_list_content_single);
            mListFooter = ((android.widget.FrameLayout) (findViewById(com.android.internal.R.id.list_footer)));
            mPrefsContainer = ((android.view.ViewGroup) (findViewById(com.android.internal.R.id.prefs)));
            mPreferenceManager = new android.preference.PreferenceManager(this, android.preference.PreferenceActivity.FIRST_REQUEST_CODE);
            mPreferenceManager.setOnPreferenceTreeClickListener(this);
            mHeadersContainer = null;
        } else
            if (mSinglePane) {
                // Single-pane so one of the header or prefs containers must be hidden.
                if ((initialFragment != null) || (mCurHeader != null)) {
                    mHeadersContainer.setVisibility(android.view.View.GONE);
                } else {
                    mPrefsContainer.setVisibility(android.view.View.GONE);
                }
                // This animates our manual transitions between headers and prefs panel in single-pane.
                // It also comes last so we don't animate any initial layout changes done above.
                android.view.ViewGroup container = ((android.view.ViewGroup) (findViewById(com.android.internal.R.id.prefs_container)));
                container.setLayoutTransition(new android.animation.LayoutTransition());
            } else {
                // Multi-pane
                if ((mHeaders.size() > 0) && (mCurHeader != null)) {
                    setSelectedHeader(mCurHeader);
                }
            }

        // see if we should show Back/Next buttons
        android.content.Intent intent = getIntent();
        if (intent.getBooleanExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SHOW_BUTTON_BAR, false)) {
            findViewById(com.android.internal.R.id.button_bar).setVisibility(android.view.View.VISIBLE);
            android.widget.Button backButton = ((android.widget.Button) (findViewById(com.android.internal.R.id.back_button)));
            backButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    setResult(android.preference.RESULT_CANCELED);
                    finish();
                }
            });
            android.widget.Button skipButton = ((android.widget.Button) (findViewById(com.android.internal.R.id.skip_button)));
            skipButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    setResult(android.preference.RESULT_OK);
                    finish();
                }
            });
            mNextButton = ((android.widget.Button) (findViewById(com.android.internal.R.id.next_button)));
            mNextButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    setResult(android.preference.RESULT_OK);
                    finish();
                }
            });
            // set our various button parameters
            if (intent.hasExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SET_NEXT_TEXT)) {
                java.lang.String buttonText = intent.getStringExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SET_NEXT_TEXT);
                if (android.text.TextUtils.isEmpty(buttonText)) {
                    mNextButton.setVisibility(android.view.View.GONE);
                } else {
                    mNextButton.setText(buttonText);
                }
            }
            if (intent.hasExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SET_BACK_TEXT)) {
                java.lang.String buttonText = intent.getStringExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SET_BACK_TEXT);
                if (android.text.TextUtils.isEmpty(buttonText)) {
                    backButton.setVisibility(android.view.View.GONE);
                } else {
                    backButton.setText(buttonText);
                }
            }
            if (intent.getBooleanExtra(android.preference.PreferenceActivity.EXTRA_PREFS_SHOW_SKIP, false)) {
                skipButton.setVisibility(android.view.View.VISIBLE);
            }
        }
    }

    @java.lang.Override
    public void onBackPressed() {
        if ((((mCurHeader != null) && mSinglePane) && (getFragmentManager().getBackStackEntryCount() == 0)) && (getIntent().getStringExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT) == null)) {
            mCurHeader = null;
            mPrefsContainer.setVisibility(android.view.View.GONE);
            mHeadersContainer.setVisibility(android.view.View.VISIBLE);
            if (mActivityTitle != null) {
                showBreadCrumbs(mActivityTitle, null);
            }
            getListView().clearChoices();
        } else {
            onBackPressed();
        }
    }

    /**
     * Returns true if this activity is currently showing the header list.
     */
    public boolean hasHeaders() {
        return (mHeadersContainer != null) && (mHeadersContainer.getVisibility() == android.view.View.VISIBLE);
    }

    /**
     * Returns the Header list
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public java.util.List<android.preference.PreferenceActivity.Header> getHeaders() {
        return mHeaders;
    }

    /**
     * Returns true if this activity is showing multiple panes -- the headers
     * and a preference fragment.
     */
    public boolean isMultiPane() {
        return !mSinglePane;
    }

    /**
     * Called to determine if the activity should run in multi-pane mode.
     * The default implementation returns true if the screen is large
     * enough.
     */
    public boolean onIsMultiPane() {
        boolean preferMultiPane = getResources().getBoolean(com.android.internal.R.bool.preferences_prefer_dual_pane);
        return preferMultiPane;
    }

    /**
     * Called to determine whether the header list should be hidden.
     * The default implementation returns the
     * value given in {@link #EXTRA_NO_HEADERS} or false if it is not supplied.
     * This is set to false, for example, when the activity is being re-launched
     * to show a particular preference activity.
     */
    public boolean onIsHidingHeaders() {
        return getIntent().getBooleanExtra(android.preference.PreferenceActivity.EXTRA_NO_HEADERS, false);
    }

    /**
     * Called to determine the initial header to be shown.  The default
     * implementation simply returns the fragment of the first header.  Note
     * that the returned Header object does not actually need to exist in
     * your header list -- whatever its fragment is will simply be used to
     * show for the initial UI.
     */
    public android.preference.PreferenceActivity.Header onGetInitialHeader() {
        for (int i = 0; i < mHeaders.size(); i++) {
            android.preference.PreferenceActivity.Header h = mHeaders.get(i);
            if (h.fragment != null) {
                return h;
            }
        }
        throw new java.lang.IllegalStateException("Must have at least one header with a fragment");
    }

    /**
     * Called after the header list has been updated ({@link #onBuildHeaders}
     * has been called and returned due to {@link #invalidateHeaders()}) to
     * specify the header that should now be selected.  The default implementation
     * returns null to keep whatever header is currently selected.
     */
    public android.preference.PreferenceActivity.Header onGetNewHeader() {
        return null;
    }

    /**
     * Called when the activity needs its list of headers build.  By
     * implementing this and adding at least one item to the list, you
     * will cause the activity to run in its modern fragment mode.  Note
     * that this function may not always be called; for example, if the
     * activity has been asked to display a particular fragment without
     * the header list, there is no need to build the headers.
     *
     * <p>Typical implementations will use {@link #loadHeadersFromResource}
     * to fill in the list from a resource.
     *
     * @param target
     * 		The list in which to place the headers.
     */
    public void onBuildHeaders(java.util.List<android.preference.PreferenceActivity.Header> target) {
        // Should be overloaded by subclasses
    }

    /**
     * Call when you need to change the headers being displayed.  Will result
     * in onBuildHeaders() later being called to retrieve the new list.
     */
    public void invalidateHeaders() {
        if (!mHandler.hasMessages(android.preference.PreferenceActivity.MSG_BUILD_HEADERS)) {
            mHandler.sendEmptyMessage(android.preference.PreferenceActivity.MSG_BUILD_HEADERS);
        }
    }

    /**
     * Parse the given XML file as a header description, adding each
     * parsed Header into the target list.
     *
     * @param resid
     * 		The XML resource to load and parse.
     * @param target
     * 		The list in which the parsed headers should be placed.
     */
    public void loadHeadersFromResource(@android.annotation.XmlRes
    int resid, java.util.List<android.preference.PreferenceActivity.Header> target) {
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = getResources().getXml(resid);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                // Parse next until start tag is found
            } 
            java.lang.String nodeName = parser.getName();
            if (!"preference-headers".equals(nodeName)) {
                throw new java.lang.RuntimeException((("XML document must start with <preference-headers> tag; found" + nodeName) + " at ") + parser.getPositionDescription());
            }
            android.os.Bundle curBundle = null;
            final int outerDepth = parser.getDepth();
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
                if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                    continue;
                }
                nodeName = parser.getName();
                if ("header".equals(nodeName)) {
                    android.preference.PreferenceActivity.Header header = new android.preference.PreferenceActivity.Header();
                    android.content.res.TypedArray sa = obtainStyledAttributes(attrs, com.android.internal.R.styleable.PreferenceHeader);
                    header.id = sa.getResourceId(com.android.internal.R.styleable.PreferenceHeader_id, ((int) (android.preference.PreferenceActivity.HEADER_ID_UNDEFINED)));
                    android.util.TypedValue tv = sa.peekValue(com.android.internal.R.styleable.PreferenceHeader_title);
                    if ((tv != null) && (tv.type == android.util.TypedValue.TYPE_STRING)) {
                        if (tv.resourceId != 0) {
                            header.titleRes = tv.resourceId;
                        } else {
                            header.title = tv.string;
                        }
                    }
                    tv = sa.peekValue(com.android.internal.R.styleable.PreferenceHeader_summary);
                    if ((tv != null) && (tv.type == android.util.TypedValue.TYPE_STRING)) {
                        if (tv.resourceId != 0) {
                            header.summaryRes = tv.resourceId;
                        } else {
                            header.summary = tv.string;
                        }
                    }
                    tv = sa.peekValue(com.android.internal.R.styleable.PreferenceHeader_breadCrumbTitle);
                    if ((tv != null) && (tv.type == android.util.TypedValue.TYPE_STRING)) {
                        if (tv.resourceId != 0) {
                            header.breadCrumbTitleRes = tv.resourceId;
                        } else {
                            header.breadCrumbTitle = tv.string;
                        }
                    }
                    tv = sa.peekValue(com.android.internal.R.styleable.PreferenceHeader_breadCrumbShortTitle);
                    if ((tv != null) && (tv.type == android.util.TypedValue.TYPE_STRING)) {
                        if (tv.resourceId != 0) {
                            header.breadCrumbShortTitleRes = tv.resourceId;
                        } else {
                            header.breadCrumbShortTitle = tv.string;
                        }
                    }
                    header.iconRes = sa.getResourceId(com.android.internal.R.styleable.PreferenceHeader_icon, 0);
                    header.fragment = sa.getString(com.android.internal.R.styleable.PreferenceHeader_fragment);
                    sa.recycle();
                    if (curBundle == null) {
                        curBundle = new android.os.Bundle();
                    }
                    final int innerDepth = parser.getDepth();
                    while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
                        if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                            continue;
                        }
                        java.lang.String innerNodeName = parser.getName();
                        if (innerNodeName.equals("extra")) {
                            getResources().parseBundleExtra("extra", attrs, curBundle);
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        } else
                            if (innerNodeName.equals("intent")) {
                                header.intent = android.content.Intent.parseIntent(getResources(), parser, attrs);
                            } else {
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                            }

                    } 
                    if (curBundle.size() > 0) {
                        header.fragmentArguments = curBundle;
                        curBundle = null;
                    }
                    target.add(header);
                } else {
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
            } 
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new java.lang.RuntimeException("Error parsing headers", e);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Error parsing headers", e);
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    /**
     * Subclasses should override this method and verify that the given fragment is a valid type
     * to be attached to this activity. The default implementation returns <code>true</code> for
     * apps built for <code>android:targetSdkVersion</code> older than
     * {@link android.os.Build.VERSION_CODES#KITKAT}. For later versions, it will throw an exception.
     *
     * @param fragmentName
     * 		the class name of the Fragment about to be attached to this activity.
     * @return true if the fragment class name is valid for this Activity and false otherwise.
     */
    protected boolean isValidFragment(java.lang.String fragmentName) {
        if (getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.KITKAT) {
            throw new java.lang.RuntimeException((((("Subclasses of PreferenceActivity must override isValidFragment(String)" + " to verify that the Fragment class is valid! ") + this.getClass().getName()) + " has not checked if fragment ") + fragmentName) + " is valid.");
        } else {
            return true;
        }
    }

    /**
     * Set a footer that should be shown at the bottom of the header list.
     */
    public void setListFooter(android.view.View view) {
        mListFooter.removeAllViews();
        mListFooter.addView(view, new android.widget.FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));
    }

    @java.lang.Override
    protected void onStop() {
        onStop();
        if (mPreferenceManager != null) {
            mPreferenceManager.dispatchActivityStop();
        }
    }

    @java.lang.Override
    protected void onDestroy() {
        mHandler.removeMessages(android.preference.PreferenceActivity.MSG_BIND_PREFERENCES);
        mHandler.removeMessages(android.preference.PreferenceActivity.MSG_BUILD_HEADERS);
        onDestroy();
        if (mPreferenceManager != null) {
            mPreferenceManager.dispatchActivityDestroy();
        }
    }

    @java.lang.Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mHeaders.size() > 0) {
            outState.putParcelableArrayList(android.preference.PreferenceActivity.HEADERS_TAG, mHeaders);
            if (mCurHeader != null) {
                int index = mHeaders.indexOf(mCurHeader);
                if (index >= 0) {
                    outState.putInt(android.preference.PreferenceActivity.CUR_HEADER_TAG, index);
                }
            }
        }
        if (mPreferenceManager != null) {
            final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (preferenceScreen != null) {
                android.os.Bundle container = new android.os.Bundle();
                preferenceScreen.saveHierarchyState(container);
                outState.putBundle(android.preference.PreferenceActivity.PREFERENCES_TAG, container);
            }
        }
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Bundle state) {
        if (mPreferenceManager != null) {
            android.os.Bundle container = state.getBundle(android.preference.PreferenceActivity.PREFERENCES_TAG);
            if (container != null) {
                final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                    mSavedInstanceState = state;
                    return;
                }
            }
        }
        // Only call this if we didn't save the instance state for later.
        // If we did save it, it will be restored when we bind the adapter.
        super.onRestoreInstanceState(state);
        if (!mSinglePane) {
            // Multi-pane.
            if (mCurHeader != null) {
                setSelectedHeader(mCurHeader);
            }
        }
    }

    @java.lang.Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        onActivityResult(requestCode, resultCode, data);
        if (mPreferenceManager != null) {
            mPreferenceManager.dispatchActivityResult(requestCode, resultCode, data);
        }
    }

    @java.lang.Override
    public void onContentChanged() {
        onContentChanged();
        if (mPreferenceManager != null) {
            postBindPreferences();
        }
    }

    @java.lang.Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        if (!isResumed()) {
            return;
        }
        onListItemClick(l, v, position, id);
        if (mAdapter != null) {
            java.lang.Object item = mAdapter.getItem(position);
            if (item instanceof android.preference.PreferenceActivity.Header)
                onHeaderClick(((android.preference.PreferenceActivity.Header) (item)), position);

        }
    }

    /**
     * Called when the user selects an item in the header list.  The default
     * implementation will call either
     * {@link #startWithFragment(String, Bundle, Fragment, int, int, int)}
     * or {@link #switchToHeader(Header)} as appropriate.
     *
     * @param header
     * 		The header that was selected.
     * @param position
     * 		The header's position in the list.
     */
    public void onHeaderClick(android.preference.PreferenceActivity.Header header, int position) {
        if (header.fragment != null) {
            switchToHeader(header);
        } else
            if (header.intent != null) {
                startActivity(header.intent);
            }

    }

    /**
     * Called by {@link #startWithFragment(String, Bundle, Fragment, int, int, int)} when
     * in single-pane mode, to build an Intent to launch a new activity showing
     * the selected fragment.  The default implementation constructs an Intent
     * that re-launches the current activity with the appropriate arguments to
     * display the fragment.
     *
     * @param fragmentName
     * 		The name of the fragment to display.
     * @param args
     * 		Optional arguments to supply to the fragment.
     * @param titleRes
     * 		Optional resource ID of title to show for this item.
     * @param shortTitleRes
     * 		Optional resource ID of short title to show for this item.
     * @return Returns an Intent that can be launched to display the given
    fragment.
     */
    public android.content.Intent onBuildStartFragmentIntent(java.lang.String fragmentName, android.os.Bundle args, @android.annotation.StringRes
    int titleRes, int shortTitleRes) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT, fragmentName);
        intent.putExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        intent.putExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_TITLE, titleRes);
        intent.putExtra(android.preference.PreferenceActivity.EXTRA_SHOW_FRAGMENT_SHORT_TITLE, shortTitleRes);
        intent.putExtra(android.preference.PreferenceActivity.EXTRA_NO_HEADERS, true);
        return intent;
    }

    /**
     * Like {@link #startWithFragment(String, Bundle, Fragment, int, int, int)}
     * but uses a 0 titleRes.
     */
    public void startWithFragment(java.lang.String fragmentName, android.os.Bundle args, android.app.Fragment resultTo, int resultRequestCode) {
        startWithFragment(fragmentName, args, resultTo, resultRequestCode, 0, 0);
    }

    /**
     * Start a new instance of this activity, showing only the given
     * preference fragment.  When launched in this mode, the header list
     * will be hidden and the given preference fragment will be instantiated
     * and fill the entire activity.
     *
     * @param fragmentName
     * 		The name of the fragment to display.
     * @param args
     * 		Optional arguments to supply to the fragment.
     * @param resultTo
     * 		Option fragment that should receive the result of
     * 		the activity launch.
     * @param resultRequestCode
     * 		If resultTo is non-null, this is the request
     * 		code in which to report the result.
     * @param titleRes
     * 		Resource ID of string to display for the title of
     * 		this set of preferences.
     * @param shortTitleRes
     * 		Resource ID of string to display for the short title of
     * 		this set of preferences.
     */
    public void startWithFragment(java.lang.String fragmentName, android.os.Bundle args, android.app.Fragment resultTo, int resultRequestCode, @android.annotation.StringRes
    int titleRes, @android.annotation.StringRes
    int shortTitleRes) {
        android.content.Intent intent = onBuildStartFragmentIntent(fragmentName, args, titleRes, shortTitleRes);
        if (resultTo == null) {
            startActivity(intent);
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode);
        }
    }

    /**
     * Change the base title of the bread crumbs for the current preferences.
     * This will normally be called for you.  See
     * {@link android.app.FragmentBreadCrumbs} for more information.
     */
    public void showBreadCrumbs(java.lang.CharSequence title, java.lang.CharSequence shortTitle) {
        if (mFragmentBreadCrumbs == null) {
            android.view.View crumbs = findViewById(android.R.id.title);
            // For screens with a different kind of title, don't create breadcrumbs.
            try {
                mFragmentBreadCrumbs = ((android.app.FragmentBreadCrumbs) (crumbs));
            } catch (java.lang.ClassCastException e) {
                setTitle(title);
                return;
            }
            if (mFragmentBreadCrumbs == null) {
                if (title != null) {
                    setTitle(title);
                }
                return;
            }
            if (mSinglePane) {
                mFragmentBreadCrumbs.setVisibility(android.view.View.GONE);
                // Hide the breadcrumb section completely for single-pane
                android.view.View bcSection = findViewById(com.android.internal.R.id.breadcrumb_section);
                if (bcSection != null)
                    bcSection.setVisibility(android.view.View.GONE);

                setTitle(title);
            }
            mFragmentBreadCrumbs.setMaxVisible(2);
            mFragmentBreadCrumbs.setActivity(this);
        }
        if (mFragmentBreadCrumbs.getVisibility() != android.view.View.VISIBLE) {
            setTitle(title);
        } else {
            mFragmentBreadCrumbs.setTitle(title, shortTitle);
            mFragmentBreadCrumbs.setParentTitle(null, null, null);
        }
    }

    /**
     * Should be called after onCreate to ensure that the breadcrumbs, if any, were created.
     * This prepends a title to the fragment breadcrumbs and attaches a listener to any clicks
     * on the parent entry.
     *
     * @param title
     * 		the title for the breadcrumb
     * @param shortTitle
     * 		the short title for the breadcrumb
     */
    public void setParentTitle(java.lang.CharSequence title, java.lang.CharSequence shortTitle, android.view.View.OnClickListener listener) {
        if (mFragmentBreadCrumbs != null) {
            mFragmentBreadCrumbs.setParentTitle(title, shortTitle, listener);
        }
    }

    void setSelectedHeader(android.preference.PreferenceActivity.Header header) {
        mCurHeader = header;
        int index = mHeaders.indexOf(header);
        if (index >= 0) {
            getListView().setItemChecked(index, true);
        } else {
            getListView().clearChoices();
        }
        showBreadCrumbs(header);
    }

    void showBreadCrumbs(android.preference.PreferenceActivity.Header header) {
        if (header != null) {
            java.lang.CharSequence title = header.getBreadCrumbTitle(getResources());
            if (title == null)
                title = header.getTitle(getResources());

            if (title == null)
                title = getTitle();

            showBreadCrumbs(title, header.getBreadCrumbShortTitle(getResources()));
        } else {
            showBreadCrumbs(getTitle(), null);
        }
    }

    private void switchToHeaderInner(java.lang.String fragmentName, android.os.Bundle args) {
        getFragmentManager().popBackStack(android.preference.PreferenceActivity.BACK_STACK_PREFS, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (!isValidFragment(fragmentName)) {
            throw new java.lang.IllegalArgumentException("Invalid fragment for this activity: " + fragmentName);
        }
        android.app.Fragment f = android.app.Fragment.instantiate(this, fragmentName, args);
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(mSinglePane ? android.app.FragmentTransaction.TRANSIT_NONE : android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(com.android.internal.R.id.prefs, f);
        transaction.commitAllowingStateLoss();
        if (mSinglePane && (mPrefsContainer.getVisibility() == android.view.View.GONE)) {
            // We are transitioning from headers to preferences panel in single-pane so we need
            // to hide headers and show the prefs container.
            mPrefsContainer.setVisibility(android.view.View.VISIBLE);
            mHeadersContainer.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * When in two-pane mode, switch the fragment pane to show the given
     * preference fragment.
     *
     * @param fragmentName
     * 		The name of the fragment to display.
     * @param args
     * 		Optional arguments to supply to the fragment.
     */
    public void switchToHeader(java.lang.String fragmentName, android.os.Bundle args) {
        android.preference.PreferenceActivity.Header selectedHeader = null;
        for (int i = 0; i < mHeaders.size(); i++) {
            if (fragmentName.equals(mHeaders.get(i).fragment)) {
                selectedHeader = mHeaders.get(i);
                break;
            }
        }
        setSelectedHeader(selectedHeader);
        switchToHeaderInner(fragmentName, args);
    }

    /**
     * When in two-pane mode, switch to the fragment pane to show the given
     * preference fragment.
     *
     * @param header
     * 		The new header to display.
     */
    public void switchToHeader(android.preference.PreferenceActivity.Header header) {
        if (mCurHeader == header) {
            // This is the header we are currently displaying.  Just make sure
            // to pop the stack up to its root state.
            getFragmentManager().popBackStack(android.preference.PreferenceActivity.BACK_STACK_PREFS, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            if (header.fragment == null) {
                throw new java.lang.IllegalStateException("can't switch to header that has no fragment");
            }
            switchToHeaderInner(header.fragment, header.fragmentArguments);
            setSelectedHeader(header);
        }
    }

    android.preference.PreferenceActivity.Header findBestMatchingHeader(android.preference.PreferenceActivity.Header cur, java.util.ArrayList<android.preference.PreferenceActivity.Header> from) {
        java.util.ArrayList<android.preference.PreferenceActivity.Header> matches = new java.util.ArrayList<android.preference.PreferenceActivity.Header>();
        for (int j = 0; j < from.size(); j++) {
            android.preference.PreferenceActivity.Header oh = from.get(j);
            if ((cur == oh) || ((cur.id != android.preference.PreferenceActivity.HEADER_ID_UNDEFINED) && (cur.id == oh.id))) {
                // Must be this one.
                matches.clear();
                matches.add(oh);
                break;
            }
            if (cur.fragment != null) {
                if (cur.fragment.equals(oh.fragment)) {
                    matches.add(oh);
                }
            } else
                if (cur.intent != null) {
                    if (cur.intent.equals(oh.intent)) {
                        matches.add(oh);
                    }
                } else
                    if (cur.title != null) {
                        if (cur.title.equals(oh.title)) {
                            matches.add(oh);
                        }
                    }


        }
        final int NM = matches.size();
        if (NM == 1) {
            return matches.get(0);
        } else
            if (NM > 1) {
                for (int j = 0; j < NM; j++) {
                    android.preference.PreferenceActivity.Header oh = matches.get(j);
                    if ((cur.fragmentArguments != null) && cur.fragmentArguments.equals(oh.fragmentArguments)) {
                        return oh;
                    }
                    if ((cur.extras != null) && cur.extras.equals(oh.extras)) {
                        return oh;
                    }
                    if ((cur.title != null) && cur.title.equals(oh.title)) {
                        return oh;
                    }
                }
            }

        return null;
    }

    /**
     * Start a new fragment.
     *
     * @param fragment
     * 		The fragment to start
     * @param push
     * 		If true, the current fragment will be pushed onto the back stack.  If false,
     * 		the current fragment will be replaced.
     */
    public void startPreferenceFragment(android.app.Fragment fragment, boolean push) {
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(com.android.internal.R.id.prefs, fragment);
        if (push) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(android.preference.PreferenceActivity.BACK_STACK_PREFS);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * Start a new fragment containing a preference panel.  If the preferences
     * are being displayed in multi-pane mode, the given fragment class will
     * be instantiated and placed in the appropriate pane.  If running in
     * single-pane mode, a new activity will be launched in which to show the
     * fragment.
     *
     * @param fragmentClass
     * 		Full name of the class implementing the fragment.
     * @param args
     * 		Any desired arguments to supply to the fragment.
     * @param titleRes
     * 		Optional resource identifier of the title of this
     * 		fragment.
     * @param titleText
     * 		Optional text of the title of this fragment.
     * @param resultTo
     * 		Optional fragment that result data should be sent to.
     * 		If non-null, resultTo.onActivityResult() will be called when this
     * 		preference panel is done.  The launched panel must use
     * 		{@link #finishPreferencePanel(Fragment, int, Intent)} when done.
     * @param resultRequestCode
     * 		If resultTo is non-null, this is the caller's
     * 		request code to be received with the result.
     */
    public void startPreferencePanel(java.lang.String fragmentClass, android.os.Bundle args, @android.annotation.StringRes
    int titleRes, java.lang.CharSequence titleText, android.app.Fragment resultTo, int resultRequestCode) {
        android.app.Fragment f = android.app.Fragment.instantiate(this, fragmentClass, args);
        if (resultTo != null) {
            f.setTargetFragment(resultTo, resultRequestCode);
        }
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(com.android.internal.R.id.prefs, f);
        if (titleRes != 0) {
            transaction.setBreadCrumbTitle(titleRes);
        } else
            if (titleText != null) {
                transaction.setBreadCrumbTitle(titleText);
            }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(android.preference.PreferenceActivity.BACK_STACK_PREFS);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Called by a preference panel fragment to finish itself.
     *
     * @param caller
     * 		The fragment that is asking to be finished.
     * @param resultCode
     * 		Optional result code to send back to the original
     * 		launching fragment.
     * @param resultData
     * 		Optional result data to send back to the original
     * 		launching fragment.
     */
    public void finishPreferencePanel(android.app.Fragment caller, int resultCode, android.content.Intent resultData) {
        // TODO: be smarter about popping the stack.
        onBackPressed();
        if (caller != null) {
            if (caller.getTargetFragment() != null) {
                caller.getTargetFragment().onActivityResult(caller.getTargetRequestCode(), resultCode, resultData);
            }
        }
    }

    @java.lang.Override
    public boolean onPreferenceStartFragment(android.preference.PreferenceFragment caller, android.preference.Preference pref) {
        startPreferencePanel(pref.getFragment(), pref.getExtras(), pref.getTitleRes(), pref.getTitle(), null, 0);
        return true;
    }

    /**
     * Posts a message to bind the preferences to the list view.
     * <p>
     * Binding late is preferred as any custom preference types created in
     * {@link #onCreate(Bundle)} are able to have their views recycled.
     */
    @android.annotation.UnsupportedAppUsage
    private void postBindPreferences() {
        if (mHandler.hasMessages(android.preference.PreferenceActivity.MSG_BIND_PREFERENCES))
            return;

        mHandler.obtainMessage(android.preference.PreferenceActivity.MSG_BIND_PREFERENCES).sendToTarget();
    }

    private void bindPreferences() {
        final android.preference.PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
            if (mSavedInstanceState != null) {
                super.onRestoreInstanceState(mSavedInstanceState);
                mSavedInstanceState = null;
            }
        }
    }

    /**
     * Returns the {@link PreferenceManager} used by this activity.
     *
     * @return The {@link PreferenceManager}.
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public android.preference.PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    @android.annotation.UnsupportedAppUsage
    private void requirePreferenceManager() {
        if (mPreferenceManager == null) {
            if (mAdapter == null) {
                throw new java.lang.RuntimeException("This should be called after super.onCreate.");
            }
            throw new java.lang.RuntimeException("Modern two-pane PreferenceActivity requires use of a PreferenceFragment");
        }
    }

    /**
     * Sets the root of the preference hierarchy that this activity is showing.
     *
     * @param preferenceScreen
     * 		The root {@link PreferenceScreen} of the preference hierarchy.
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public void setPreferenceScreen(android.preference.PreferenceScreen preferenceScreen) {
        requirePreferenceManager();
        if (mPreferenceManager.setPreferences(preferenceScreen) && (preferenceScreen != null)) {
            postBindPreferences();
            java.lang.CharSequence title = getPreferenceScreen().getTitle();
            // Set the title of the activity
            if (title != null) {
                setTitle(title);
            }
        }
    }

    /**
     * Gets the root of the preference hierarchy that this activity is showing.
     *
     * @return The {@link PreferenceScreen} that is the root of the preference
    hierarchy.
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public android.preference.PreferenceScreen getPreferenceScreen() {
        if (mPreferenceManager != null) {
            return mPreferenceManager.getPreferenceScreen();
        }
        return null;
    }

    /**
     * Adds preferences from activities that match the given {@link Intent}.
     *
     * @param intent
     * 		The {@link Intent} to query activities.
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
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
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(mPreferenceManager.inflateFromResource(this, preferencesResId, getPreferenceScreen()));
    }

    /**
     * {@inheritDoc }
     *
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public boolean onPreferenceTreeClick(android.preference.PreferenceScreen preferenceScreen, android.preference.Preference preference) {
        return false;
    }

    /**
     * Finds a {@link Preference} based on its key.
     *
     * @param key
     * 		The key of the preference to retrieve.
     * @return The {@link Preference} with the key, or null.
     * @see PreferenceGroup#findPreference(CharSequence)
     * @deprecated This function is not relevant for a modern fragment-based
    PreferenceActivity.
     */
    @java.lang.Deprecated
    public android.preference.Preference findPreference(java.lang.CharSequence key) {
        if (mPreferenceManager == null) {
            return null;
        }
        return mPreferenceManager.findPreference(key);
    }

    @java.lang.Override
    protected void onNewIntent(android.content.Intent intent) {
        if (mPreferenceManager != null) {
            mPreferenceManager.dispatchNewIntent(intent);
        }
    }

    // give subclasses access to the Next button
    /**
     *
     *
     * @unknown 
     */
    protected boolean hasNextButton() {
        return mNextButton != null;
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.widget.Button getNextButton() {
        return mNextButton;
    }
}

