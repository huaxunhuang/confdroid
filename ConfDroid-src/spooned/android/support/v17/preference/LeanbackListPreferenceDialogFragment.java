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


public class LeanbackListPreferenceDialogFragment extends android.support.v17.preference.LeanbackPreferenceDialogFragment {
    public static android.support.v17.preference.LeanbackListPreferenceDialogFragment newInstanceSingle(java.lang.String key) {
        final android.os.Bundle args = new android.os.Bundle(5);
        args.putString(android.support.v17.preference.LeanbackPreferenceDialogFragment.ARG_KEY, key);
        final android.support.v17.preference.LeanbackListPreferenceDialogFragment fragment = new android.support.v17.preference.LeanbackListPreferenceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static android.support.v17.preference.LeanbackListPreferenceDialogFragment newInstanceMulti(java.lang.String key) {
        final android.os.Bundle args = new android.os.Bundle(5);
        args.putString(android.support.v17.preference.LeanbackPreferenceDialogFragment.ARG_KEY, key);
        final android.support.v17.preference.LeanbackListPreferenceDialogFragment fragment = new android.support.v17.preference.LeanbackListPreferenceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.preference.DialogPreference preference = getPreference();
        if ((!(preference instanceof android.support.v7.preference.ListPreference)) && (!(preference instanceof android.support.v14.preference.MultiSelectListPreference))) {
            throw new java.lang.IllegalArgumentException("Preference must be a ListPreference or " + "MultiSelectListPreference");
        }
    }

    @java.lang.Override
    @android.support.annotation.Nullable
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.leanback_list_preference_fragment, container, false);
        final android.support.v17.leanback.widget.VerticalGridView verticalGridView = ((android.support.v17.leanback.widget.VerticalGridView) (view.findViewById(android.R.id.list)));
        verticalGridView.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_BOTH_EDGE);
        verticalGridView.setFocusScrollStrategy(android.support.v17.leanback.widget.VerticalGridView.FOCUS_SCROLL_ALIGNED);
        verticalGridView.setAdapter(onCreateAdapter());
        verticalGridView.requestFocus();
        final android.support.v7.preference.DialogPreference preference = getPreference();
        final java.lang.CharSequence title = preference.getDialogTitle();
        if (!android.text.TextUtils.isEmpty(title)) {
            final android.widget.TextView titleView = ((android.widget.TextView) (view.findViewById(R.id.decor_title)));
            titleView.setText(title);
        }
        final java.lang.CharSequence message = preference.getDialogMessage();
        if (!android.text.TextUtils.isEmpty(message)) {
            final android.widget.TextView messageView = ((android.widget.TextView) (view.findViewById(android.R.id.message)));
            messageView.setVisibility(android.view.View.VISIBLE);
            messageView.setText(message);
        }
        return view;
    }

    public android.support.v7.widget.RecyclerView.Adapter onCreateAdapter() {
        final android.support.v7.preference.DialogPreference preference = getPreference();
        if (preference instanceof android.support.v14.preference.MultiSelectListPreference) {
            final android.support.v14.preference.MultiSelectListPreference pref = ((android.support.v14.preference.MultiSelectListPreference) (preference));
            final java.lang.CharSequence[] entries = pref.getEntries();
            final java.lang.CharSequence[] entryValues = pref.getEntryValues();
            final java.util.Set<java.lang.String> initialSelections = pref.getValues();
            return new android.support.v17.preference.LeanbackListPreferenceDialogFragment.AdapterMulti(entries, entryValues, initialSelections);
        } else
            if (preference instanceof android.support.v7.preference.ListPreference) {
                final android.support.v7.preference.ListPreference pref = ((android.support.v7.preference.ListPreference) (preference));
                final java.lang.CharSequence[] entries = pref.getEntries();
                final java.lang.CharSequence[] entryValues = pref.getEntryValues();
                final java.lang.String initialSelection = pref.getValue();
                return new android.support.v17.preference.LeanbackListPreferenceDialogFragment.AdapterSingle(entries, entryValues, initialSelection);
            } else {
                throw new java.lang.IllegalStateException("Unknown preference type");
            }

    }

    public class AdapterSingle extends android.support.v7.widget.RecyclerView.Adapter<android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder> implements android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder.OnItemClickListener {
        private final java.lang.CharSequence[] mEntries;

        private final java.lang.CharSequence[] mEntryValues;

        private java.lang.CharSequence mSelectedValue;

        public AdapterSingle(java.lang.CharSequence[] entries, java.lang.CharSequence[] entryValues, java.lang.CharSequence selectedValue) {
            mEntries = entries;
            mEntryValues = entryValues;
            mSelectedValue = selectedValue;
        }

        @java.lang.Override
        public android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
            final android.view.View view = inflater.inflate(R.layout.leanback_list_preference_item_single, parent, false);
            return new android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder(view, this);
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder holder, int position) {
            holder.getWidgetView().setChecked(mEntryValues[position].equals(mSelectedValue));
            holder.getTitleView().setText(mEntries[position]);
        }

        @java.lang.Override
        public int getItemCount() {
            return mEntries.length;
        }

        @java.lang.Override
        public void onItemClick(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder viewHolder) {
            final int index = viewHolder.getAdapterPosition();
            if (index == android.support.v7.widget.RecyclerView.NO_POSITION) {
                return;
            }
            final java.lang.CharSequence entry = mEntryValues[index];
            final android.support.v7.preference.ListPreference preference = ((android.support.v7.preference.ListPreference) (getPreference()));
            if (index >= 0) {
                java.lang.String value = mEntryValues[index].toString();
                if (preference.callChangeListener(value)) {
                    preference.setValue(value);
                    mSelectedValue = entry;
                }
            }
            getFragmentManager().popBackStack();
            notifyDataSetChanged();
        }
    }

    public class AdapterMulti extends android.support.v7.widget.RecyclerView.Adapter<android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder> implements android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder.OnItemClickListener {
        private final java.lang.CharSequence[] mEntries;

        private final java.lang.CharSequence[] mEntryValues;

        private final java.util.Set<java.lang.String> mSelections;

        public AdapterMulti(java.lang.CharSequence[] entries, java.lang.CharSequence[] entryValues, java.util.Set<java.lang.String> initialSelections) {
            mEntries = entries;
            mEntryValues = entryValues;
            mSelections = new java.util.HashSet<>(initialSelections);
        }

        @java.lang.Override
        public android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
            final android.view.View view = inflater.inflate(R.layout.leanback_list_preference_item_multi, parent, false);
            return new android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder(view, this);
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder holder, int position) {
            holder.getWidgetView().setChecked(mSelections.contains(mEntryValues[position].toString()));
            holder.getTitleView().setText(mEntries[position]);
        }

        @java.lang.Override
        public int getItemCount() {
            return mEntries.length;
        }

        @java.lang.Override
        public void onItemClick(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder viewHolder) {
            final int index = viewHolder.getAdapterPosition();
            if (index == android.support.v7.widget.RecyclerView.NO_POSITION) {
                return;
            }
            final java.lang.String entry = mEntryValues[index].toString();
            if (mSelections.contains(entry)) {
                mSelections.remove(entry);
            } else {
                mSelections.add(entry);
            }
            final android.support.v14.preference.MultiSelectListPreference multiSelectListPreference = ((android.support.v14.preference.MultiSelectListPreference) (getPreference()));
            // Pass copies of the set to callChangeListener and setValues to avoid mutations
            if (multiSelectListPreference.callChangeListener(new java.util.HashSet<>(mSelections))) {
                multiSelectListPreference.setValues(new java.util.HashSet<>(mSelections));
            } else {
                // Change refused, back it out
                if (mSelections.contains(entry)) {
                    mSelections.remove(entry);
                } else {
                    mSelections.add(entry);
                }
            }
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements android.view.View.OnClickListener {
        public interface OnItemClickListener {
            void onItemClick(android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder viewHolder);
        }

        private final android.widget.Checkable mWidgetView;

        private final android.widget.TextView mTitleView;

        private final android.view.ViewGroup mContainer;

        private final android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder.OnItemClickListener mListener;

        public ViewHolder(@android.support.annotation.NonNull
        android.view.View view, @android.support.annotation.NonNull
        android.support.v17.preference.LeanbackListPreferenceDialogFragment.ViewHolder.OnItemClickListener listener) {
            super(view);
            mWidgetView = ((android.widget.Checkable) (view.findViewById(R.id.button)));
            mContainer = ((android.view.ViewGroup) (view.findViewById(R.id.container)));
            mTitleView = ((android.widget.TextView) (view.findViewById(android.R.id.title)));
            mContainer.setOnClickListener(this);
            mListener = listener;
        }

        public android.widget.Checkable getWidgetView() {
            return mWidgetView;
        }

        public android.widget.TextView getTitleView() {
            return mTitleView;
        }

        public android.view.ViewGroup getContainer() {
            return mContainer;
        }

        @java.lang.Override
        public void onClick(android.view.View v) {
            mListener.onItemClick(this);
        }
    }
}

