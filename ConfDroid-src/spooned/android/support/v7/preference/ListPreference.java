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
 * A {@link Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a string into the SharedPreferences. This string will be the value
 * from the {@link #setEntryValues(CharSequence[])} array.
 *
 * @unknown name android:entries
 * @unknown name android:entryValues
 */
public class ListPreference extends android.support.v7.preference.DialogPreference {
    private java.lang.CharSequence[] mEntries;

    private java.lang.CharSequence[] mEntryValues;

    private java.lang.String mValue;

    private java.lang.String mSummary;

    private boolean mValueSet;

    public ListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListPreference, defStyleAttr, defStyleRes);
        mEntries = android.support.v4.content.res.TypedArrayUtils.getTextArray(a, R.styleable.ListPreference_entries, R.styleable.ListPreference_android_entries);
        mEntryValues = android.support.v4.content.res.TypedArrayUtils.getTextArray(a, R.styleable.ListPreference_entryValues, R.styleable.ListPreference_android_entryValues);
        a.recycle();
        /* Retrieve the Preference summary attribute since it's private
        in the Preference class.
         */
        a = context.obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes);
        mSummary = android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.Preference_summary, R.styleable.Preference_android_summary);
        a.recycle();
    }

    public ListPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
    }

    public ListPreference(android.content.Context context) {
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
     * Sets the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     *
     * @param value
     * 		The value to set for the key.
     */
    public void setValue(java.lang.String value) {
        // Always persist/notify the first time.
        final boolean changed = !android.text.TextUtils.equals(mValue, value);
        if (changed || (!mValueSet)) {
            mValue = value;
            mValueSet = true;
            persistString(value);
            if (changed) {
                notifyChanged();
            }
        }
    }

    /**
     * Returns the summary of this ListPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @java.lang.Override
    public java.lang.CharSequence getSummary() {
        final java.lang.CharSequence entry = getEntry();
        if (mSummary == null) {
            return super.getSummary();
        } else {
            return java.lang.String.format(mSummary, entry == null ? "" : entry);
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     * If the summary has a
     * {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place when it's retrieved.
     *
     * @param summary
     * 		The summary for the preference.
     */
    @java.lang.Override
    public void setSummary(java.lang.CharSequence summary) {
        super.setSummary(summary);
        if ((summary == null) && (mSummary != null)) {
            mSummary = null;
        } else
            if ((summary != null) && (!summary.equals(mSummary))) {
                mSummary = summary.toString();
            }

    }

    /**
     * Sets the value to the given index from the entry values.
     *
     * @param index
     * 		The index of the value to set.
     */
    public void setValueIndex(int index) {
        if (mEntryValues != null) {
            setValue(mEntryValues[index].toString());
        }
    }

    /**
     * Returns the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     *
     * @return The value of the key.
     */
    public java.lang.String getValue() {
        return mValue;
    }

    /**
     * Returns the entry corresponding to the current value.
     *
     * @return The entry corresponding to the current value, or null.
     */
    public java.lang.CharSequence getEntry() {
        int index = getValueIndex();
        return (index >= 0) && (mEntries != null) ? mEntries[index] : null;
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

    private int getValueIndex() {
        return findIndexOfValue(mValue);
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getString(index);
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
        setValue(restoreValue ? getPersistedString(mValue) : ((java.lang.String) (defaultValue)));
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.support.v7.preference.ListPreference.SavedState myState = new android.support.v7.preference.ListPreference.SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.support.v7.preference.ListPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v7.preference.ListPreference.SavedState myState = ((android.support.v7.preference.ListPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    private static class SavedState extends android.support.v7.preference.Preference.BaseSavedState {
        java.lang.String value;

        public SavedState(android.os.Parcel source) {
            super(source);
            value = source.readString();
        }

        @java.lang.Override
        public void writeToParcel(@android.support.annotation.NonNull
        android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public static final android.os.Parcelable.Creator<android.support.v7.preference.ListPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v7.preference.ListPreference.SavedState>() {
            @java.lang.Override
            public android.support.v7.preference.ListPreference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v7.preference.ListPreference.SavedState(in);
            }

            @java.lang.Override
            public android.support.v7.preference.ListPreference.SavedState[] newArray(int size) {
                return new android.support.v7.preference.ListPreference.SavedState[size];
            }
        };
    }
}

