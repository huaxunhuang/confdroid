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
package android.support.v7.preference;


/**
 * An adapter that connects a RecyclerView to the {@link Preference} objects contained in the
 * associated {@link PreferenceGroup}.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class PreferenceGroupAdapter extends android.support.v7.widget.RecyclerView.Adapter<android.support.v7.preference.PreferenceViewHolder> implements android.support.v7.preference.Preference.OnPreferenceChangeInternalListener , android.support.v7.preference.PreferenceGroup.PreferencePositionCallback {
    private static final java.lang.String TAG = "PreferenceGroupAdapter";

    /**
     * The group that we are providing data from.
     */
    private android.support.v7.preference.PreferenceGroup mPreferenceGroup;

    /**
     * Maps a position into this adapter -> {@link Preference}. These
     * {@link Preference}s don't have to be direct children of this
     * {@link PreferenceGroup}, they can be grand children or younger)
     */
    private java.util.List<android.support.v7.preference.Preference> mPreferenceList;

    /**
     * Contains a sorted list of all preferences in this adapter regardless of visibility. This is
     * used to construct {@link #mPreferenceList}
     */
    private java.util.List<android.support.v7.preference.Preference> mPreferenceListInternal;

    /**
     * List of unique Preference and its subclasses' names and layouts.
     */
    private java.util.List<android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout> mPreferenceLayouts;

    private android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout mTempPreferenceLayout = new android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout();

    private android.os.Handler mHandler = new android.os.Handler();

    private java.lang.Runnable mSyncRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            syncMyPreferences();
        }
    };

    private static class PreferenceLayout {
        private int resId;

        private int widgetResId;

        private java.lang.String name;

        public PreferenceLayout() {
        }

        public PreferenceLayout(android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout other) {
            resId = other.resId;
            widgetResId = other.widgetResId;
            name = other.name;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout)) {
                return false;
            }
            final android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout other = ((android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout) (o));
            return ((resId == other.resId) && (widgetResId == other.widgetResId)) && android.text.TextUtils.equals(name, other.name);
        }

        @java.lang.Override
        public int hashCode() {
            int result = 17;
            result = (31 * result) + resId;
            result = (31 * result) + widgetResId;
            result = (31 * result) + name.hashCode();
            return result;
        }
    }

    public PreferenceGroupAdapter(android.support.v7.preference.PreferenceGroup preferenceGroup) {
        mPreferenceGroup = preferenceGroup;
        // If this group gets or loses any children, let us know
        mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        mPreferenceList = new java.util.ArrayList<>();
        mPreferenceListInternal = new java.util.ArrayList<>();
        mPreferenceLayouts = new java.util.ArrayList<>();
        if (mPreferenceGroup instanceof android.support.v7.preference.PreferenceScreen) {
            setHasStableIds(((android.support.v7.preference.PreferenceScreen) (mPreferenceGroup)).shouldUseGeneratedIds());
        } else {
            setHasStableIds(true);
        }
        syncMyPreferences();
    }

    private void syncMyPreferences() {
        for (final android.support.v7.preference.Preference preference : mPreferenceListInternal) {
            // Clear out the listeners in anticipation of some items being removed. This listener
            // will be (re-)added to the remaining prefs when we flatten.
            preference.setOnPreferenceChangeInternalListener(null);
        }
        final java.util.List<android.support.v7.preference.Preference> fullPreferenceList = new java.util.ArrayList<>(mPreferenceListInternal.size());
        flattenPreferenceGroup(fullPreferenceList, mPreferenceGroup);
        final java.util.List<android.support.v7.preference.Preference> visiblePreferenceList = new java.util.ArrayList<>(fullPreferenceList.size());
        // Copy only the visible preferences to the active list
        for (final android.support.v7.preference.Preference preference : fullPreferenceList) {
            if (preference.isVisible()) {
                visiblePreferenceList.add(preference);
            }
        }
        mPreferenceList = visiblePreferenceList;
        mPreferenceListInternal = fullPreferenceList;
        notifyDataSetChanged();
    }

    private void flattenPreferenceGroup(java.util.List<android.support.v7.preference.Preference> preferences, android.support.v7.preference.PreferenceGroup group) {
        group.sortPreferences();
        final int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            final android.support.v7.preference.Preference preference = group.getPreference(i);
            preferences.add(preference);
            addPreferenceClassName(preference);
            if (preference instanceof android.support.v7.preference.PreferenceGroup) {
                final android.support.v7.preference.PreferenceGroup preferenceAsGroup = ((android.support.v7.preference.PreferenceGroup) (preference));
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
    private android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout createPreferenceLayout(android.support.v7.preference.Preference preference, android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout in) {
        android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout pl = (in != null) ? in : new android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        return pl;
    }

    private void addPreferenceClassName(android.support.v7.preference.Preference preference) {
        final android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout pl = createPreferenceLayout(preference, null);
        if (!mPreferenceLayouts.contains(pl)) {
            mPreferenceLayouts.add(pl);
        }
    }

    @java.lang.Override
    public int getItemCount() {
        return mPreferenceList.size();
    }

    public android.support.v7.preference.Preference getItem(int position) {
        if ((position < 0) || (position >= getItemCount()))
            return null;

        return mPreferenceList.get(position);
    }

    @java.lang.Override
    public long getItemId(int position) {
        if (!hasStableIds()) {
            return android.support.v7.widget.RecyclerView.NO_ID;
        }
        return this.getItem(position).getId();
    }

    @java.lang.Override
    public void onPreferenceChange(android.support.v7.preference.Preference preference) {
        final int index = mPreferenceList.indexOf(preference);
        // If we don't find the preference, we don't need to notify anyone
        if (index != (-1)) {
            // Send the pref object as a placeholder to ensure the view holder is recycled in place
            notifyItemChanged(index, preference);
        }
    }

    @java.lang.Override
    public void onPreferenceHierarchyChange(android.support.v7.preference.Preference preference) {
        mHandler.removeCallbacks(mSyncRunnable);
        mHandler.post(mSyncRunnable);
    }

    @java.lang.Override
    public void onPreferenceVisibilityChange(android.support.v7.preference.Preference preference) {
        if (!mPreferenceListInternal.contains(preference)) {
            return;
        }
        if (preference.isVisible()) {
            // The preference has become visible, we need to add it in the correct location.
            // Index (inferred) in mPreferenceList of the item preceding the newly visible pref
            int previousVisibleIndex = -1;
            for (final android.support.v7.preference.Preference pref : mPreferenceListInternal) {
                if (preference.equals(pref)) {
                    break;
                }
                if (pref.isVisible()) {
                    previousVisibleIndex++;
                }
            }
            // Insert this preference into the active list just after the previous visible entry
            mPreferenceList.add(previousVisibleIndex + 1, preference);
            notifyItemInserted(previousVisibleIndex + 1);
        } else {
            // The preference has become invisible. Find it in the list and remove it.
            int removalIndex;
            final int listSize = mPreferenceList.size();
            for (removalIndex = 0; removalIndex < listSize; removalIndex++) {
                if (preference.equals(mPreferenceList.get(removalIndex))) {
                    break;
                }
            }
            mPreferenceList.remove(removalIndex);
            notifyItemRemoved(removalIndex);
        }
    }

    @java.lang.Override
    public int getItemViewType(int position) {
        final android.support.v7.preference.Preference preference = this.getItem(position);
        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);
        int viewType = mPreferenceLayouts.indexOf(mTempPreferenceLayout);
        if (viewType != (-1)) {
            return viewType;
        } else {
            viewType = mPreferenceLayouts.size();
            mPreferenceLayouts.add(new android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout(mTempPreferenceLayout));
            return viewType;
        }
    }

    @java.lang.Override
    public android.support.v7.preference.PreferenceViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        final android.support.v7.preference.PreferenceGroupAdapter.PreferenceLayout pl = mPreferenceLayouts.get(viewType);
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
        android.content.res.TypedArray a = parent.getContext().obtainStyledAttributes(null, R.styleable.BackgroundStyle);
        android.graphics.drawable.Drawable background = a.getDrawable(R.styleable.BackgroundStyle_android_selectableItemBackground);
        if (background == null) {
            background = parent.getContext().getResources().getDrawable(android.R.drawable.list_selector_background);
        }
        a.recycle();
        final android.view.View view = inflater.inflate(pl.resId, parent, false);
        if (view.getBackground() == null) {
            android.support.v4.view.ViewCompat.setBackground(view, background);
        }
        final android.view.ViewGroup widgetFrame = ((android.view.ViewGroup) (view.findViewById(android.R.id.widget_frame)));
        if (widgetFrame != null) {
            if (pl.widgetResId != 0) {
                inflater.inflate(pl.widgetResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(android.view.View.GONE);
            }
        }
        return new android.support.v7.preference.PreferenceViewHolder(view);
    }

    @java.lang.Override
    public void onBindViewHolder(android.support.v7.preference.PreferenceViewHolder holder, int position) {
        final android.support.v7.preference.Preference preference = getItem(position);
        preference.onBindViewHolder(holder);
    }

    @java.lang.Override
    public int getPreferenceAdapterPosition(java.lang.String key) {
        final int size = mPreferenceList.size();
        for (int i = 0; i < size; i++) {
            final android.support.v7.preference.Preference candidate = mPreferenceList.get(i);
            if (android.text.TextUtils.equals(key, candidate.getKey())) {
                return i;
            }
        }
        return android.support.v7.widget.RecyclerView.NO_POSITION;
    }

    @java.lang.Override
    public int getPreferenceAdapterPosition(android.support.v7.preference.Preference preference) {
        final int size = mPreferenceList.size();
        for (int i = 0; i < size; i++) {
            final android.support.v7.preference.Preference candidate = mPreferenceList.get(i);
            if ((candidate != null) && candidate.equals(preference)) {
                return i;
            }
        }
        return android.support.v7.widget.RecyclerView.NO_POSITION;
    }
}

