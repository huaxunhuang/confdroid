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
 * Common base class for preferences that have two selectable states, persist a
 * boolean value in SharedPreferences, and may have dependent preferences that are
 * enabled/disabled based on the current state.
 *
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public abstract class TwoStatePreference extends android.preference.Preference {
    private java.lang.CharSequence mSummaryOn;

    private java.lang.CharSequence mSummaryOff;

    boolean mChecked;

    private boolean mCheckedSet;

    private boolean mDisableDependentsState;

    public TwoStatePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TwoStatePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TwoStatePreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoStatePreference(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected void onClick() {
        super.onClick();
        final boolean newValue = !isChecked();
        if (callChangeListener(newValue)) {
            setChecked(newValue);
        }
    }

    /**
     * Sets the checked state and saves it to the {@link SharedPreferences}.
     *
     * @param checked
     * 		The checked state.
     */
    public void setChecked(boolean checked) {
        // Always persist/notify the first time; don't assume the field's default of false.
        final boolean changed = mChecked != checked;
        if (changed || (!mCheckedSet)) {
            mChecked = checked;
            mCheckedSet = true;
            persistBoolean(checked);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    /**
     * Returns the checked state.
     *
     * @return The checked state.
     */
    public boolean isChecked() {
        return mChecked;
    }

    @java.lang.Override
    public boolean shouldDisableDependents() {
        boolean shouldDisable = (mDisableDependentsState) ? mChecked : !mChecked;
        return shouldDisable || super.shouldDisableDependents();
    }

    /**
     * Sets the summary to be shown when checked.
     *
     * @param summary
     * 		The summary to be shown when checked.
     */
    public void setSummaryOn(java.lang.CharSequence summary) {
        mSummaryOn = summary;
        if (isChecked()) {
            notifyChanged();
        }
    }

    /**
     *
     *
     * @see #setSummaryOn(CharSequence)
     * @param summaryResId
     * 		The summary as a resource.
     */
    public void setSummaryOn(@android.annotation.StringRes
    int summaryResId) {
        setSummaryOn(getContext().getString(summaryResId));
    }

    /**
     * Returns the summary to be shown when checked.
     *
     * @return The summary.
     */
    public java.lang.CharSequence getSummaryOn() {
        return mSummaryOn;
    }

    /**
     * Sets the summary to be shown when unchecked.
     *
     * @param summary
     * 		The summary to be shown when unchecked.
     */
    public void setSummaryOff(java.lang.CharSequence summary) {
        mSummaryOff = summary;
        if (!isChecked()) {
            notifyChanged();
        }
    }

    /**
     *
     *
     * @see #setSummaryOff(CharSequence)
     * @param summaryResId
     * 		The summary as a resource.
     */
    public void setSummaryOff(@android.annotation.StringRes
    int summaryResId) {
        setSummaryOff(getContext().getString(summaryResId));
    }

    /**
     * Returns the summary to be shown when unchecked.
     *
     * @return The summary.
     */
    public java.lang.CharSequence getSummaryOff() {
        return mSummaryOff;
    }

    /**
     * Returns whether dependents are disabled when this preference is on ({@code true})
     * or when this preference is off ({@code false}).
     *
     * @return Whether dependents are disabled when this preference is on ({@code true})
    or when this preference is off ({@code false}).
     */
    public boolean getDisableDependentsState() {
        return mDisableDependentsState;
    }

    /**
     * Sets whether dependents are disabled when this preference is on ({@code true})
     * or when this preference is off ({@code false}).
     *
     * @param disableDependentsState
     * 		The preference state that should disable dependents.
     */
    public void setDisableDependentsState(boolean disableDependentsState) {
        mDisableDependentsState = disableDependentsState;
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getBoolean(index, false);
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
        setChecked(restoreValue ? getPersistedBoolean(mChecked) : ((java.lang.Boolean) (defaultValue)));
    }

    /**
     * Sync a summary view contained within view's subhierarchy with the correct summary text.
     *
     * @param view
     * 		View where a summary should be located
     */
    @android.annotation.UnsupportedAppUsage
    void syncSummaryView(android.view.View view) {
        // Sync the summary view
        android.widget.TextView summaryView = ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.summary)));
        if (summaryView != null) {
            boolean useDefaultSummary = true;
            if (mChecked && (!android.text.TextUtils.isEmpty(mSummaryOn))) {
                summaryView.setText(mSummaryOn);
                useDefaultSummary = false;
            } else
                if ((!mChecked) && (!android.text.TextUtils.isEmpty(mSummaryOff))) {
                    summaryView.setText(mSummaryOff);
                    useDefaultSummary = false;
                }

            if (useDefaultSummary) {
                final java.lang.CharSequence summary = getSummary();
                if (!android.text.TextUtils.isEmpty(summary)) {
                    summaryView.setText(summary);
                    useDefaultSummary = false;
                }
            }
            int newVisibility = android.view.View.GONE;
            if (!useDefaultSummary) {
                // Someone has written to it
                newVisibility = android.view.View.VISIBLE;
            }
            if (newVisibility != summaryView.getVisibility()) {
                summaryView.setVisibility(newVisibility);
            }
        }
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.preference.TwoStatePreference.SavedState myState = new android.preference.TwoStatePreference.SavedState(superState);
        myState.checked = isChecked();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.TwoStatePreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.TwoStatePreference.SavedState myState = ((android.preference.TwoStatePreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setChecked(myState.checked);
    }

    static class SavedState extends android.preference.Preference.BaseSavedState {
        boolean checked;

        public SavedState(android.os.Parcel source) {
            super(source);
            checked = source.readInt() == 1;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(checked ? 1 : 0);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.TwoStatePreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.TwoStatePreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

