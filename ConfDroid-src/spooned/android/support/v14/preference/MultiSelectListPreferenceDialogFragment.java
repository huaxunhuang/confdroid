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


public class MultiSelectListPreferenceDialogFragment extends android.support.v14.preference.PreferenceDialogFragment {
    private static final java.lang.String SAVE_STATE_VALUES = "MultiSelectListPreferenceDialogFragment.values";

    private static final java.lang.String SAVE_STATE_CHANGED = "MultiSelectListPreferenceDialogFragment.changed";

    private static final java.lang.String SAVE_STATE_ENTRIES = "MultiSelectListPreferenceDialogFragment.entries";

    private static final java.lang.String SAVE_STATE_ENTRY_VALUES = "MultiSelectListPreferenceDialogFragment.entryValues";

    private java.util.Set<java.lang.String> mNewValues = new java.util.HashSet<>();

    private boolean mPreferenceChanged;

    private java.lang.CharSequence[] mEntries;

    private java.lang.CharSequence[] mEntryValues;

    public static android.support.v14.preference.MultiSelectListPreferenceDialogFragment newInstance(java.lang.String key) {
        final android.support.v14.preference.MultiSelectListPreferenceDialogFragment fragment = new android.support.v14.preference.MultiSelectListPreferenceDialogFragment();
        final android.os.Bundle b = new android.os.Bundle(1);
        b.putString(android.support.v14.preference.PreferenceDialogFragment.ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final android.support.v7.preference.internal.AbstractMultiSelectListPreference preference = getListPreference();
            if ((preference.getEntries() == null) || (preference.getEntryValues() == null)) {
                throw new java.lang.IllegalStateException("MultiSelectListPreference requires an entries array and " + "an entryValues array.");
            }
            mNewValues.clear();
            mNewValues.addAll(preference.getValues());
            mPreferenceChanged = false;
            mEntries = preference.getEntries();
            mEntryValues = preference.getEntryValues();
        } else {
            mNewValues.clear();
            mNewValues.addAll(savedInstanceState.getStringArrayList(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_VALUES));
            mPreferenceChanged = savedInstanceState.getBoolean(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_CHANGED, false);
            mEntries = savedInstanceState.getCharSequenceArray(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_ENTRIES);
            mEntryValues = savedInstanceState.getCharSequenceArray(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_ENTRY_VALUES);
        }
    }

    @java.lang.Override
    public void onSaveInstanceState(@android.support.annotation.NonNull
    android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_VALUES, new java.util.ArrayList<>(mNewValues));
        outState.putBoolean(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_CHANGED, mPreferenceChanged);
        outState.putCharSequenceArray(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_ENTRIES, mEntries);
        outState.putCharSequenceArray(android.support.v14.preference.MultiSelectListPreferenceDialogFragment.SAVE_STATE_ENTRY_VALUES, mEntryValues);
    }

    private android.support.v7.preference.internal.AbstractMultiSelectListPreference getListPreference() {
        return ((android.support.v7.preference.internal.AbstractMultiSelectListPreference) (getPreference()));
    }

    @java.lang.Override
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        final int entryCount = mEntryValues.length;
        final boolean[] checkedItems = new boolean[entryCount];
        for (int i = 0; i < entryCount; i++) {
            checkedItems[i] = mNewValues.contains(mEntryValues[i].toString());
        }
        builder.setMultiChoiceItems(mEntries, checkedItems, new android.content.DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(android.content.DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    mPreferenceChanged |= mNewValues.add(mEntryValues[which].toString());
                } else {
                    mPreferenceChanged |= mNewValues.remove(mEntryValues[which].toString());
                }
            }
        });
    }

    @java.lang.Override
    public void onDialogClosed(boolean positiveResult) {
        final android.support.v7.preference.internal.AbstractMultiSelectListPreference preference = getListPreference();
        if (positiveResult && mPreferenceChanged) {
            final java.util.Set<java.lang.String> values = mNewValues;
            if (preference.callChangeListener(values)) {
                preference.setValues(values);
            }
        }
        mPreferenceChanged = false;
    }
}

