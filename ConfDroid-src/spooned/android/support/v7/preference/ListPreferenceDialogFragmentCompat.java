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


public class ListPreferenceDialogFragmentCompat extends android.support.v7.preference.PreferenceDialogFragmentCompat {
    private static final java.lang.String SAVE_STATE_INDEX = "ListPreferenceDialogFragment.index";

    private static final java.lang.String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";

    private static final java.lang.String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";

    private int mClickedDialogEntryIndex;

    private java.lang.CharSequence[] mEntries;

    private java.lang.CharSequence[] mEntryValues;

    public static android.support.v7.preference.ListPreferenceDialogFragmentCompat newInstance(java.lang.String key) {
        final android.support.v7.preference.ListPreferenceDialogFragmentCompat fragment = new android.support.v7.preference.ListPreferenceDialogFragmentCompat();
        final android.os.Bundle b = new android.os.Bundle(1);
        b.putString(android.support.v7.preference.PreferenceDialogFragmentCompat.ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final android.support.v7.preference.ListPreference preference = getListPreference();
            if ((preference.getEntries() == null) || (preference.getEntryValues() == null)) {
                throw new java.lang.IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }
            mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            mEntries = preference.getEntries();
            mEntryValues = preference.getEntryValues();
        } else {
            mClickedDialogEntryIndex = savedInstanceState.getInt(android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_INDEX, 0);
            mEntries = android.support.v7.preference.ListPreferenceDialogFragmentCompat.getCharSequenceArray(savedInstanceState, android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_ENTRIES);
            mEntryValues = android.support.v7.preference.ListPreferenceDialogFragmentCompat.getCharSequenceArray(savedInstanceState, android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_ENTRY_VALUES);
        }
    }

    @java.lang.Override
    public void onSaveInstanceState(@android.support.annotation.NonNull
    android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_INDEX, mClickedDialogEntryIndex);
        android.support.v7.preference.ListPreferenceDialogFragmentCompat.putCharSequenceArray(outState, android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_ENTRIES, mEntries);
        android.support.v7.preference.ListPreferenceDialogFragmentCompat.putCharSequenceArray(outState, android.support.v7.preference.ListPreferenceDialogFragmentCompat.SAVE_STATE_ENTRY_VALUES, mEntryValues);
    }

    private static void putCharSequenceArray(android.os.Bundle out, java.lang.String key, java.lang.CharSequence[] entries) {
        final java.util.ArrayList<java.lang.String> stored = new java.util.ArrayList<>(entries.length);
        for (final java.lang.CharSequence cs : entries) {
            stored.add(cs.toString());
        }
        out.putStringArrayList(key, stored);
    }

    private static java.lang.CharSequence[] getCharSequenceArray(android.os.Bundle in, java.lang.String key) {
        final java.util.ArrayList<java.lang.String> stored = in.getStringArrayList(key);
        return stored == null ? null : stored.toArray(new java.lang.CharSequence[stored.size()]);
    }

    private android.support.v7.preference.ListPreference getListPreference() {
        return ((android.support.v7.preference.ListPreference) (getPreference()));
    }

    @java.lang.Override
    protected void onPrepareDialogBuilder(android.support.v7.app.AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex, new android.content.DialogInterface.OnClickListener() {
            @java.lang.Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                mClickedDialogEntryIndex = which;
                /* Clicking on an item simulates the positive button
                click, and dismisses the dialog.
                 */
                android.support.v7.preference.ListPreferenceDialogFragmentCompat.this.onClick(dialog, android.content.DialogInterface.BUTTON_POSITIVE);
                dialog.dismiss();
            }
        });
        /* The typical interaction for list-based dialogs is to have
        click-on-an-item dismiss the dialog instead of the user having to
        press 'Ok'.
         */
        builder.setPositiveButton(null, null);
    }

    @java.lang.Override
    public void onDialogClosed(boolean positiveResult) {
        final android.support.v7.preference.ListPreference preference = getListPreference();
        if (positiveResult && (mClickedDialogEntryIndex >= 0)) {
            java.lang.String value = mEntryValues[mClickedDialogEntryIndex].toString();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }
}

