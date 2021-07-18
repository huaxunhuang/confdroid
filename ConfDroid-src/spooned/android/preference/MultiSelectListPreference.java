/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * A {@link Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a set of strings into the SharedPreferences.
 * This set will contain one or more values from the
 * {@link #setEntryValues(CharSequence[])} array.
 *
 * @unknown ref android.R.styleable#MultiSelectListPreference_entries
 * @unknown ref android.R.styleable#MultiSelectListPreference_entryValues
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class MultiSelectListPreference extends android.preference.DialogPreference {
    private java.lang.CharSequence[] mEntries;

    private java.lang.CharSequence[] mEntryValues;

    private java.util.Set<java.lang.String> mValues = new java.util.HashSet<java.lang.String>();

    private java.util.Set<java.lang.String> mNewValues = new java.util.HashSet<java.lang.String>();

    private boolean mPreferenceChanged;

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.MultiSelectListPreference, defStyleAttr, defStyleRes);
        mEntries = a.getTextArray(com.android.internal.R.styleable.MultiSelectListPreference_entries);
        mEntryValues = a.getTextArray(com.android.internal.R.styleable.MultiSelectListPreference_entryValues);
        a.recycle();
    }

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.dialogPreferenceStyle);
    }

    public MultiSelectListPreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Sets the human-readable entries to be shown in the list. This will be
     * shown in subsequent dialogs.
     * <p>
     * Each entry must have a corresponding index in
     * {@link #setEntryValues(CharSequence[])}.
     *
     * @param entries
     * 		The entries.
     * @see #setEntryValues(CharSequence[])
     */
    public void setEntries(java.lang.CharSequence[] entries) {
        mEntries = entries;
    }

    /**
     *
     *
     * @see #setEntries(CharSequence[])
     * @param entriesResId
     * 		The entries array as a resource.
     */
    public void setEntries(@android.annotation.ArrayRes
    int entriesResId) {
        setEntries(getContext().getResources().getTextArray(entriesResId));
    }

    /**
     * The list of entries to be shown in the list in subsequent dialogs.
     *
     * @return The list as an array.
     */
    public java.lang.CharSequence[] getEntries() {
        return mEntries;
    }

    /**
     * The array to find the value to save for a preference when an entry from
     * entries is selected. If a user clicks on the second item in entries, the
     * second item in this array will be saved to the preference.
     *
     * @param entryValues
     * 		The array to be used as values to save for the preference.
     */
    public void setEntryValues(java.lang.CharSequence[] entryValues) {
        mEntryValues = entryValues;
    }

    /**
     *
     *
     * @see #setEntryValues(CharSequence[])
     * @param entryValuesResId
     * 		The entry values array as a resource.
     */
    public void setEntryValues(@android.annotation.ArrayRes
    int entryValuesResId) {
        setEntryValues(getContext().getResources().getTextArray(entryValuesResId));
    }

    /**
     * Returns the array of values to be saved for the preference.
     *
     * @return The array of values.
     */
    public java.lang.CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    /**
     * Sets the value of the key. This should contain entries in
     * {@link #getEntryValues()}.
     *
     * @param values
     * 		The values to set for the key.
     */
    public void setValues(java.util.Set<java.lang.String> values) {
        mValues.clear();
        mValues.addAll(values);
        persistStringSet(values);
    }

    /**
     * Retrieves the current value of the key.
     */
    public java.util.Set<java.lang.String> getValues() {
        return mValues;
    }

    /**
     * Returns the index of the given value (in the entry values array).
     *
     * @param value
     * 		The value whose index should be returned.
     * @return The index of the value, or -1 if not found.
     */
    public int findIndexOfValue(java.lang.String value) {
        if ((value != null) && (mEntryValues != null)) {
            for (int i = mEntryValues.length - 1; i >= 0; i--) {
                if (mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @java.lang.Override
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        if ((mEntries == null) || (mEntryValues == null)) {
            throw new java.lang.IllegalStateException("MultiSelectListPreference requires an entries array and " + "an entryValues array.");
        }
        boolean[] checkedItems = getSelectedItems();
        builder.setMultiChoiceItems(mEntries, checkedItems, new android.content.DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(android.content.DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    mPreferenceChanged |= mNewValues.add(mEntryValues[which].toString());
                } else {
                    mPreferenceChanged |= mNewValues.remove(mEntryValues[which].toString());
                }
            }
        });
        mNewValues.clear();
        mNewValues.addAll(mValues);
    }

    private boolean[] getSelectedItems() {
        final java.lang.CharSequence[] entries = mEntryValues;
        final int entryCount = entries.length;
        final java.util.Set<java.lang.String> values = mValues;
        boolean[] result = new boolean[entryCount];
        for (int i = 0; i < entryCount; i++) {
            result[i] = values.contains(entries[i].toString());
        }
        return result;
    }

    @java.lang.Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult && mPreferenceChanged) {
            final java.util.Set<java.lang.String> values = mNewValues;
            if (callChangeListener(values)) {
                setValues(values);
            }
        }
        mPreferenceChanged = false;
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        final java.lang.CharSequence[] defaultValues = a.getTextArray(index);
        final int valueCount = defaultValues.length;
        final java.util.Set<java.lang.String> result = new java.util.HashSet<java.lang.String>();
        for (int i = 0; i < valueCount; i++) {
            result.add(defaultValues[i].toString());
        }
        return result;
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
        setValues(restoreValue ? getPersistedStringSet(mValues) : ((java.util.Set<java.lang.String>) (defaultValue)));
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state
            return superState;
        }
        final android.preference.MultiSelectListPreference.SavedState myState = new android.preference.MultiSelectListPreference.SavedState(superState);
        myState.values = getValues();
        return myState;
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
        java.util.Set<java.lang.String> values;

        public SavedState(android.os.Parcel source) {
            super(source);
            values = new java.util.HashSet<java.lang.String>();
            java.lang.String[] strings = source.readStringArray();
            final int stringCount = strings.length;
            for (int i = 0; i < stringCount; i++) {
                values.add(strings[i]);
            }
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeStringArray(values.toArray(new java.lang.String[0]));
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.MultiSelectListPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.MultiSelectListPreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

