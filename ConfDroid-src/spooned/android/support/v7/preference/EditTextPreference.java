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
 * A {@link Preference} that allows for string
 * input.
 * <p>
 * It is a subclass of {@link DialogPreference} and shows the {@link EditText}
 * in a dialog.
 * <p>
 * This preference will store a string into the SharedPreferences.
 */
public class EditTextPreference extends android.support.v7.preference.DialogPreference {
    private java.lang.String mText;

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, R.attr.editTextPreferenceStyle, android.support.v7.preference.AndroidResources.ANDROID_R_EDITTEXT_PREFERENCE_STYLE));
    }

    public EditTextPreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Saves the text to the {@link android.content.SharedPreferences}.
     *
     * @param text
     * 		The text to save
     */
    public void setText(java.lang.String text) {
        final boolean wasBlocking = shouldDisableDependents();
        mText = text;
        persistString(text);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    /**
     * Gets the text from the {@link android.content.SharedPreferences}.
     *
     * @return The current preference value.
     */
    public java.lang.String getText() {
        return mText;
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getString(index);
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
        setText(restoreValue ? getPersistedString(mText) : ((java.lang.String) (defaultValue)));
    }

    @java.lang.Override
    public boolean shouldDisableDependents() {
        return android.text.TextUtils.isEmpty(mText) || super.shouldDisableDependents();
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.support.v7.preference.EditTextPreference.SavedState myState = new android.support.v7.preference.EditTextPreference.SavedState(superState);
        myState.text = getText();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.support.v7.preference.EditTextPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v7.preference.EditTextPreference.SavedState myState = ((android.support.v7.preference.EditTextPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }

    private static class SavedState extends android.support.v7.preference.Preference.BaseSavedState {
        java.lang.String text;

        public SavedState(android.os.Parcel source) {
            super(source);
            text = source.readString();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(text);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public static final android.os.Parcelable.Creator<android.support.v7.preference.EditTextPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v7.preference.EditTextPreference.SavedState>() {
            @java.lang.Override
            public android.support.v7.preference.EditTextPreference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v7.preference.EditTextPreference.SavedState(in);
            }

            @java.lang.Override
            public android.support.v7.preference.EditTextPreference.SavedState[] newArray(int size) {
                return new android.support.v7.preference.EditTextPreference.SavedState[size];
            }
        };
    }
}

