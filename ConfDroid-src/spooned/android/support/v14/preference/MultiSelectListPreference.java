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


/**
 * A {@link android.support.v7.preference.Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a set of strings into the SharedPreferences.
 * This set will contain one or more values from the
 * {@link #setEntryValues(CharSequence[])} array.
 *
 * @unknown name android:entries
 * @unknown name android:entryValues
 */
public class MultiSelectListPreference extends android.support.v7.preference.internal.AbstractMultiSelectListPreference {
    private java.lang.CharSequence[] mEntries;

    private java.lang.CharSequence[] mEntryValues;

    private java.util.Set<java.lang.String> mValues = new java.util.HashSet<>();

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.preference.R.styleable.MultiSelectListPreference, defStyleAttr, defStyleRes);
        mEntries = android.support.v4.content.res.TypedArrayUtils.getTextArray(a, android.support.v7.preference.R.styleable.MultiSelectListPreference_entries, android.support.v7.preference.R.styleable.MultiSelectListPreference_android_entries);
        mEntryValues = android.support.v4.content.res.TypedArrayUtils.getTextArray(a, android.support.v7.preference.R.styleable.MultiSelectListPreference_entryValues, android.support.v7.preference.R.styleable.MultiSelectListPreference_android_entryValues);
        a.recycle();
    }

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiSelectListPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, android.support.v7.preference.R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
    }

    public MultiSelectListPreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Attempts to persist a set of Strings to the {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get an editor from
     * the {@link android.preference.PreferenceManager}, put in the strings, and check if we should
     * commit (and commit if so).
     *
     * @param values
     * 		The values to persist.
     * @return True if the Preference is persistent. (This is not whether the
    value was persisted, since we may not necessarily commit if there
    will be a batch commit later.)
     * @see #getPersistedString
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected boolean persistStringSet(java.util.Set<java.lang.String> values) {
        if (shouldPersist()) {
            // Shouldn't store null
            if (values.equals(getPersistedStringSet(null))) {
                // It's already there, so the same as persisting
                return true;
            }
            android.content.SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
            editor.putStringSet(getKey(), values);
            android.support.v4.content.SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
            return true;
        }
        return false;
    }

    /**
     * Attempts to get a persisted set of Strings from the
     * {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get the SharedPreferences
     * from the {@link android.preference.PreferenceManager}, and get the value.
     *
     * @param defaultReturnValue
     * 		The default value to return if either the
     * 		Preference is not persistent or the Preference is not in the
     * 		shared preferences.
     * @return The value from the SharedPreferences or the default return
    value.
     * @see #persistStringSet(Set)
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected java.util.Set<java.lang.String> getPersistedStringSet(java.util.Set<java.lang.String> defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        return getPreferenceManager().getSharedPreferences().getStringSet(getKey(), defaultReturnValue);
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
    public void setEntries(@android.support.annotation.ArrayRes
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
    public void setEntryValues(@android.support.annotation.ArrayRes
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

    protected boolean[] getSelectedItems() {
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
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        final java.lang.CharSequence[] defaultValues = a.getTextArray(index);
        final java.util.Set<java.lang.String> result = new java.util.HashSet<>();
        for (final java.lang.CharSequence defaultValue : defaultValues) {
            result.add(defaultValue.toString());
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
        final android.support.v14.preference.MultiSelectListPreference.SavedState myState = new android.support.v14.preference.MultiSelectListPreference.SavedState(superState);
        myState.values = getValues();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.support.v14.preference.MultiSelectListPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v14.preference.MultiSelectListPreference.SavedState myState = ((android.support.v14.preference.MultiSelectListPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setValues(myState.values);
    }

    private static class SavedState extends android.support.v7.preference.Preference.BaseSavedState {
        java.util.Set<java.lang.String> values;

        public SavedState(android.os.Parcel source) {
            super(source);
            final int size = source.readInt();
            values = new java.util.HashSet<>();
            java.lang.String[] strings = new java.lang.String[size];
            source.readStringArray(strings);
            java.util.Collections.addAll(values, strings);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(@android.support.annotation.NonNull
        android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(values.size());
            dest.writeStringArray(values.toArray(new java.lang.String[values.size()]));
        }

        public static final android.os.Parcelable.Creator<android.support.v14.preference.MultiSelectListPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v14.preference.MultiSelectListPreference.SavedState>() {
            public android.support.v14.preference.MultiSelectListPreference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v14.preference.MultiSelectListPreference.SavedState(in);
            }

            public android.support.v14.preference.MultiSelectListPreference.SavedState[] newArray(int size) {
                return new android.support.v14.preference.MultiSelectListPreference.SavedState[size];
            }
        };
    }
}

