/**
 * Copyright (C) 2012 The Android Open Source Project
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
 *
 *
 * @unknown A {@link Preference} that displays a list of entries as
a dialog which allow the user to toggle each individually on and off.
 * @unknown ref android.R.styleable#ListPreference_entries
 * @unknown ref android.R.styleable#ListPreference_entryValues
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class MultiCheckPreference extends android.preference.DialogPreference {
    private java.lang.CharSequence[] mEntries;

    private java.lang.String[] mEntryValues;

    private boolean[] mSetValues;

    private boolean[] mOrigValues;

    private java.lang.String mSummary;

    public MultiCheckPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ListPreference, defStyleAttr, defStyleRes);
        mEntries = a.getTextArray(com.android.internal.R.styleable.ListPreference_entries);
        if (mEntries != null) {
            setEntries(mEntries);
        }
        setEntryValuesCS(a.getTextArray(com.android.internal.R.styleable.ListPreference_entryValues));
        a.recycle();
        /* Retrieve the Preference summary attribute since it's private
        in the Preference class.
         */
        a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Preference, 0, 0);
        mSummary = a.getString(com.android.internal.R.styleable.Preference_summary);
        a.recycle();
    }

    public MultiCheckPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiCheckPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.dialogPreferenceStyle);
    }

    public MultiCheckPreference(android.content.Context context) {
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
        mSetValues = new boolean[entries.length];
        mOrigValues = new boolean[entries.length];
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
    public void setEntryValues(java.lang.String[] entryValues) {
        mEntryValues = entryValues;
        java.util.Arrays.fill(mSetValues, false);
        java.util.Arrays.fill(mOrigValues, false);
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
        setEntryValuesCS(getContext().getResources().getTextArray(entryValuesResId));
    }

    private void setEntryValuesCS(java.lang.CharSequence[] values) {
        setValues(null);
        if (values != null) {
            mEntryValues = new java.lang.String[values.length];
            for (int i = 0; i < values.length; i++) {
                mEntryValues[i] = values[i].toString();
            }
        }
    }

    /**
     * Returns the array of values to be saved for the preference.
     *
     * @return The array of values.
     */
    public java.lang.String[] getEntryValues() {
        return mEntryValues;
    }

    /**
     * Get the boolean state of a given value.
     */
    public boolean getValue(int index) {
        return mSetValues[index];
    }

    /**
     * Set the boolean state of a given value.
     */
    public void setValue(int index, boolean state) {
        mSetValues[index] = state;
    }

    /**
     * Sets the current values.
     */
    public void setValues(boolean[] values) {
        if (mSetValues != null) {
            java.util.Arrays.fill(mSetValues, false);
            java.util.Arrays.fill(mOrigValues, false);
            if (values != null) {
                java.lang.System.arraycopy(values, 0, mSetValues, 0, values.length < mSetValues.length ? values.length : mSetValues.length);
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
        if (mSummary == null) {
            return super.getSummary();
        } else {
            return mSummary;
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
     * Returns the currently selected values.
     */
    public boolean[] getValues() {
        return mSetValues;
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
            throw new java.lang.IllegalStateException("ListPreference requires an entries array and an entryValues array.");
        }
        mOrigValues = java.util.Arrays.copyOf(mSetValues, mSetValues.length);
        builder.setMultiChoiceItems(mEntries, mSetValues, new android.content.DialogInterface.OnMultiChoiceClickListener() {
            @java.lang.Override
            public void onClick(android.content.DialogInterface dialog, int which, boolean isChecked) {
                mSetValues[which] = isChecked;
            }
        });
    }

    @java.lang.Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            if (callChangeListener(getValues())) {
                return;
            }
        }
        java.lang.System.arraycopy(mOrigValues, 0, mSetValues, 0, mSetValues.length);
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getString(index);
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.preference.MultiCheckPreference.SavedState myState = new android.preference.MultiCheckPreference.SavedState(superState);
        myState.values = getValues();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.MultiCheckPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.MultiCheckPreference.SavedState myState = ((android.preference.MultiCheckPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setValues(myState.values);
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
        boolean[] values;

        public SavedState(android.os.Parcel source) {
            super(source);
            values = source.createBooleanArray();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBooleanArray(values);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.MultiCheckPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.MultiCheckPreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

