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
 * Represents a top-level {@link Preference} that
 * is the root of a Preference hierarchy. A {@link PreferenceActivity}
 * points to an instance of this class to show the preferences. To instantiate
 * this class, use {@link PreferenceManager#createPreferenceScreen(Context)}.
 * <ul>
 * This class can appear in two places:
 * <li> When a {@link PreferenceActivity} points to this, it is used as the root
 * and is not shown (only the contained preferences are shown).
 * <li> When it appears inside another preference hierarchy, it is shown and
 * serves as the gateway to another screen of preferences (either by showing
 * another screen of preferences as a {@link Dialog} or via a
 * {@link Context#startActivity(android.content.Intent)} from the
 * {@link Preference#getIntent()}). The children of this {@link PreferenceScreen}
 * are NOT shown in the screen that this {@link PreferenceScreen} is shown in.
 * Instead, a separate screen will be shown when this preference is clicked.
 * </ul>
 * <p>Here's an example XML layout of a PreferenceScreen:</p>
 * <pre>
 * &lt;PreferenceScreen
 * xmlns:android="http://schemas.android.com/apk/res/android"
 * android:key="first_preferencescreen"&gt;
 * &lt;CheckBoxPreference
 * android:key="wifi enabled"
 * android:title="WiFi" /&gt;
 * &lt;PreferenceScreen
 * android:key="second_preferencescreen"
 * android:title="WiFi settings"&gt;
 * &lt;CheckBoxPreference
 * android:key="prefer wifi"
 * android:title="Prefer WiFi" /&gt;
 * ... other preferences here ...
 * &lt;/PreferenceScreen&gt;
 * &lt;/PreferenceScreen&gt; </pre>
 * <p>
 * In this example, the "first_preferencescreen" will be used as the root of the
 * hierarchy and given to a {@link PreferenceActivity}. The first screen will
 * show preferences "WiFi" (which can be used to quickly enable/disable WiFi)
 * and "WiFi settings". The "WiFi settings" is the "second_preferencescreen" and when
 * clicked will show another screen of preferences such as "Prefer WiFi" (and
 * the other preferences that are children of the "second_preferencescreen" tag).
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @see PreferenceCategory
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public final class PreferenceScreen extends android.preference.PreferenceGroup implements android.content.DialogInterface.OnDismissListener , android.widget.AdapterView.OnItemClickListener {
    @android.annotation.UnsupportedAppUsage
    private android.widget.ListAdapter mRootAdapter;

    private android.app.Dialog mDialog;

    @android.annotation.UnsupportedAppUsage
    private android.widget.ListView mListView;

    private int mLayoutResId = com.android.internal.R.layout.preference_list_fragment;

    private android.graphics.drawable.Drawable mDividerDrawable;

    private boolean mDividerSpecified;

    /**
     * Do NOT use this constructor, use {@link PreferenceManager#createPreferenceScreen(Context)}.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public PreferenceScreen(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs, com.android.internal.R.attr.preferenceScreenStyle);
        android.content.res.TypedArray a = context.obtainStyledAttributes(null, android.preference.com.android.internal.R.styleable.PreferenceScreen, com.android.internal.R.attr.preferenceScreenStyle, 0);
        mLayoutResId = a.getResourceId(android.preference.com.android.internal.R.styleable.PreferenceScreen_screenLayout, mLayoutResId);
        if (a.hasValueOrEmpty(android.preference.com.android.internal.R.styleable.PreferenceScreen_divider)) {
            mDividerDrawable = a.getDrawable(android.preference.com.android.internal.R.styleable.PreferenceScreen_divider);
            mDividerSpecified = true;
        }
        a.recycle();
    }

    /**
     * Returns an adapter that can be attached to a {@link PreferenceActivity}
     * or {@link PreferenceFragment} to show the preferences contained in this
     * {@link PreferenceScreen}.
     * <p>
     * This {@link PreferenceScreen} will NOT appear in the returned adapter, instead
     * it appears in the hierarchy above this {@link PreferenceScreen}.
     * <p>
     * This adapter's {@link Adapter#getItem(int)} should always return a
     * subclass of {@link Preference}.
     *
     * @return An adapter that provides the {@link Preference} contained in this
    {@link PreferenceScreen}.
     */
    public android.widget.ListAdapter getRootAdapter() {
        if (mRootAdapter == null) {
            mRootAdapter = onCreateRootAdapter();
        }
        return mRootAdapter;
    }

    /**
     * Creates the root adapter.
     *
     * @return An adapter that contains the preferences contained in this {@link PreferenceScreen}.
     * @see #getRootAdapter()
     */
    protected android.widget.ListAdapter onCreateRootAdapter() {
        return new android.preference.PreferenceGroupAdapter(this);
    }

    /**
     * Binds a {@link ListView} to the preferences contained in this {@link PreferenceScreen} via
     * {@link #getRootAdapter()}. It also handles passing list item clicks to the corresponding
     * {@link Preference} contained by this {@link PreferenceScreen}.
     *
     * @param listView
     * 		The list view to attach to.
     */
    public void bind(android.widget.ListView listView) {
        listView.setOnItemClickListener(this);
        listView.setAdapter(getRootAdapter());
        onAttachedToActivity();
    }

    @java.lang.Override
    protected void onClick() {
        if (((getIntent() != null) || (getFragment() != null)) || (getPreferenceCount() == 0)) {
            return;
        }
        showDialog(null);
    }

    private void showDialog(android.os.Bundle state) {
        android.content.Context context = getContext();
        if (mListView != null) {
            mListView.setAdapter(null);
        }
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        android.view.View childPrefScreen = inflater.inflate(mLayoutResId, null);
        android.view.View titleView = childPrefScreen.findViewById(android.R.id.title);
        mListView = ((android.widget.ListView) (childPrefScreen.findViewById(android.R.id.list)));
        if (mDividerSpecified) {
            mListView.setDivider(mDividerDrawable);
        }
        bind(mListView);
        // Set the title bar if title is available, else no title bar
        final java.lang.CharSequence title = getTitle();
        android.app.Dialog dialog = mDialog = new android.app.Dialog(context, context.getThemeResId());
        if (android.text.TextUtils.isEmpty(title)) {
            if (titleView != null) {
                titleView.setVisibility(android.view.View.GONE);
            }
            dialog.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        } else {
            if (titleView instanceof android.widget.TextView) {
                ((android.widget.TextView) (titleView)).setText(title);
                titleView.setVisibility(android.view.View.VISIBLE);
            } else {
                dialog.setTitle(title);
            }
        }
        dialog.setContentView(childPrefScreen);
        dialog.setOnDismissListener(this);
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        // Add the screen to the list of preferences screens opened as dialogs
        getPreferenceManager().addPreferencesScreen(dialog);
        dialog.show();
    }

    public void onDismiss(android.content.DialogInterface dialog) {
        mDialog = null;
        getPreferenceManager().removePreferencesScreen(dialog);
    }

    /**
     * Used to get a handle to the dialog.
     * This is useful for cases where we want to manipulate the dialog
     * as we would with any other activity or view.
     */
    public android.app.Dialog getDialog() {
        return mDialog;
    }

    public void onItemClick(android.widget.AdapterView parent, android.view.View view, int position, long id) {
        // If the list has headers, subtract them from the index.
        if (parent instanceof android.widget.ListView) {
            position -= ((android.widget.ListView) (parent)).getHeaderViewsCount();
        }
        java.lang.Object item = getRootAdapter().getItem(position);
        if (!(item instanceof android.preference.Preference))
            return;

        final android.preference.Preference preference = ((android.preference.Preference) (item));
        preference.performClick(this);
    }

    @java.lang.Override
    protected boolean isOnSameScreenAsChildren() {
        return false;
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        final android.app.Dialog dialog = mDialog;
        if ((dialog == null) || (!dialog.isShowing())) {
            return superState;
        }
        final android.preference.PreferenceScreen.SavedState myState = new android.preference.PreferenceScreen.SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = dialog.onSaveInstanceState();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.PreferenceScreen.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.PreferenceScreen.SavedState myState = ((android.preference.PreferenceScreen.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
        boolean isDialogShowing;

        android.os.Bundle dialogBundle;

        public SavedState(android.os.Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.PreferenceScreen.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.PreferenceScreen.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

