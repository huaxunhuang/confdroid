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
 * An adapter that returns the {@link Preference} contained in this group.
 * In most cases, this adapter should be the base class for any custom
 * adapters from {@link Preference#getAdapter()}.
 * <p>
 * This adapter obeys the
 * {@link Preference}'s adapter rule (the
 * {@link Adapter#getView(int, View, ViewGroup)} should be used instead of
 * {@link Preference#getView(ViewGroup)} if a {@link Preference} has an
 * adapter via {@link Preference#getAdapter()}).
 * <p>
 * This adapter also propagates data change/invalidated notifications upward.
 * <p>
 * This adapter does not include this {@link PreferenceGroup} in the returned
 * adapter, use {@link PreferenceCategoryAdapter} instead.
 *
 * @see PreferenceCategoryAdapter
 * @unknown 
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class PreferenceGroupAdapter extends android.widget.BaseAdapter implements android.preference.Preference.OnPreferenceChangeInternalListener {
    private static final java.lang.String TAG = "PreferenceGroupAdapter";

    /**
     * The group that we are providing data from.
     */
    private android.preference.PreferenceGroup mPreferenceGroup;

    /**
     * Maps a position into this adapter -> {@link Preference}. These
     * {@link Preference}s don't have to be direct children of this
     * {@link PreferenceGroup}, they can be grand children or younger)
     */
    private java.util.List<android.preference.Preference> mPreferenceList;

    /**
     * List of unique Preference and its subclasses' names. This is used to find
     * out how many types of views this adapter can return. Once the count is
     * returned, this cannot be modified (since the ListView only checks the
     * count once--when the adapter is being set). We will not recycle views for
     * Preference subclasses seen after the count has been returned.
     */
    private java.util.ArrayList<android.preference.PreferenceGroupAdapter.PreferenceLayout> mPreferenceLayouts;

    private android.preference.PreferenceGroupAdapter.PreferenceLayout mTempPreferenceLayout = new android.preference.PreferenceGroupAdapter.PreferenceLayout();

    /**
     * Blocks the mPreferenceClassNames from being changed anymore.
     */
    private boolean mHasReturnedViewTypeCount = false;

    private volatile boolean mIsSyncing = false;

    private android.os.Handler mHandler = new android.os.Handler();

    private java.lang.Runnable mSyncRunnable = new java.lang.Runnable() {
        public void run() {
            syncMyPreferences();
        }
    };

    private int mHighlightedPosition = -1;

    private android.graphics.drawable.Drawable mHighlightedDrawable;

    private static android.view.ViewGroup.LayoutParams sWrapperLayoutParams = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

    private static class PreferenceLayout implements java.lang.Comparable<android.preference.PreferenceGroupAdapter.PreferenceLayout> {
        private int resId;

        private int widgetResId;

        private java.lang.String name;

        public int compareTo(android.preference.PreferenceGroupAdapter.PreferenceLayout other) {
            int compareNames = name.compareTo(other.name);
            if (compareNames == 0) {
                if (resId == other.resId) {
                    if (widgetResId == other.widgetResId) {
                        return 0;
                    } else {
                        return widgetResId - other.widgetResId;
                    }
                } else {
                    return resId - other.resId;
                }
            } else {
                return compareNames;
            }
        }
    }

    public PreferenceGroupAdapter(android.preference.PreferenceGroup preferenceGroup) {
        mPreferenceGroup = preferenceGroup;
        // If this group gets or loses any children, let us know
        mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        mPreferenceList = new java.util.ArrayList<android.preference.Preference>();
        mPreferenceLayouts = new java.util.ArrayList<android.preference.PreferenceGroupAdapter.PreferenceLayout>();
        syncMyPreferences();
    }

    private void syncMyPreferences() {
        synchronized(this) {
            if (mIsSyncing) {
                return;
            }
            mIsSyncing = true;
        }
        java.util.List<android.preference.Preference> newPreferenceList = new java.util.ArrayList<android.preference.Preference>(mPreferenceList.size());
        flattenPreferenceGroup(newPreferenceList, mPreferenceGroup);
        mPreferenceList = newPreferenceList;
        notifyDataSetChanged();
        synchronized(this) {
            mIsSyncing = false;
            notifyAll();
        }
    }

    private void flattenPreferenceGroup(java.util.List<android.preference.Preference> preferences, android.preference.PreferenceGroup group) {
        // TODO: shouldn't always?
        group.sortPreferences();
        final int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            final android.preference.Preference preference = group.getPreference(i);
            preferences.add(preference);
            if ((!mHasReturnedViewTypeCount) && preference.isRecycleEnabled()) {
                addPreferenceClassName(preference);
            }
            if (preference instanceof android.preference.PreferenceGroup) {
                final android.preference.PreferenceGroup preferenceAsGroup = ((android.preference.PreferenceGroup) (preference));
                if (preferenceAsGroup.isOnSameScreenAsChildren()) {
                    flattenPreferenceGroup(preferences, preferenceAsGroup);
                }
            }
            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    /**
     * Creates a string that includes the preference name, layout id and widget layout id.
     * If a particular preference type uses 2 different resources, they will be treated as
     * different view types.
     */
    private android.preference.PreferenceGroupAdapter.PreferenceLayout createPreferenceLayout(android.preference.Preference preference, android.preference.PreferenceGroupAdapter.PreferenceLayout in) {
        android.preference.PreferenceGroupAdapter.PreferenceLayout pl = (in != null) ? in : new android.preference.PreferenceGroupAdapter.PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        return pl;
    }

    private void addPreferenceClassName(android.preference.Preference preference) {
        final android.preference.PreferenceGroupAdapter.PreferenceLayout pl = createPreferenceLayout(preference, null);
        int insertPos = java.util.Collections.binarySearch(mPreferenceLayouts, pl);
        // Only insert if it doesn't exist (when it is negative).
        if (insertPos < 0) {
            // Convert to insert index
            insertPos = (insertPos * (-1)) - 1;
            mPreferenceLayouts.add(insertPos, pl);
        }
    }

    public int getCount() {
        return mPreferenceList.size();
    }

    @android.annotation.UnsupportedAppUsage
    public android.preference.Preference getItem(int position) {
        if ((position < 0) || (position >= getCount()))
            return null;

        return mPreferenceList.get(position);
    }

    public long getItemId(int position) {
        if ((position < 0) || (position >= getCount()))
            return android.widget.ListView.INVALID_ROW_ID;

        return this.getItem(position).getId();
    }

    /**
     *
     *
     * @unknown 
     */
    public void setHighlighted(int position) {
        mHighlightedPosition = position;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setHighlightedDrawable(android.graphics.drawable.Drawable drawable) {
        mHighlightedDrawable = drawable;
    }

    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        final android.preference.Preference preference = this.getItem(position);
        // Build a PreferenceLayout to compare with known ones that are cacheable.
        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);
        // If it's not one of the cached ones, set the convertView to null so that
        // the layout gets re-created by the Preference.
        if ((java.util.Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout) < 0) || (getItemViewType(position) == getHighlightItemViewType())) {
            convertView = null;
        }
        android.view.View result = preference.getView(convertView, parent);
        if ((position == mHighlightedPosition) && (mHighlightedDrawable != null)) {
            android.view.ViewGroup wrapper = new android.widget.FrameLayout(parent.getContext());
            wrapper.setLayoutParams(android.preference.PreferenceGroupAdapter.sWrapperLayoutParams);
            wrapper.setBackgroundDrawable(mHighlightedDrawable);
            wrapper.addView(result);
            result = wrapper;
        }
        return result;
    }

    @java.lang.Override
    public boolean isEnabled(int position) {
        if ((position < 0) || (position >= getCount()))
            return true;

        return this.getItem(position).isSelectable();
    }

    @java.lang.Override
    public boolean areAllItemsEnabled() {
        // There should always be a preference group, and these groups are always
        // disabled
        return false;
    }

    public void onPreferenceChange(android.preference.Preference preference) {
        notifyDataSetChanged();
    }

    public void onPreferenceHierarchyChange(android.preference.Preference preference) {
        mHandler.removeCallbacks(mSyncRunnable);
        mHandler.post(mSyncRunnable);
    }

    @java.lang.Override
    public boolean hasStableIds() {
        return true;
    }

    private int getHighlightItemViewType() {
        return getViewTypeCount() - 1;
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        if (position == mHighlightedPosition) {
            return getHighlightItemViewType();
        }
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }
        final android.preference.Preference preference = this.getItem(position);
        if (!preference.isRecycleEnabled()) {
            return android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE;
        }
        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);
        int viewType = java.util.Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout);
        if (viewType < 0) {
            // This is a class that was seen after we returned the count, so
            // don't recycle it.
            return android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE;
        } else {
            return viewType;
        }
    }

    @java.lang.Override
    public int getViewTypeCount() {
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }
        return java.lang.Math.max(1, mPreferenceLayouts.size()) + 1;
    }
}

