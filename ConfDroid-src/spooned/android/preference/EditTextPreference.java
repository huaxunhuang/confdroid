/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A {@link Preference} that allows for string
 * input.
 * <p>
 * It is a subclass of {@link DialogPreference} and shows the {@link EditText}
 * in a dialog. This {@link EditText} can be modified either programmatically
 * via {@link #getEditText()}, or through XML by setting any EditText
 * attributes on the EditTextPreference.
 * <p>
 * This preference will store a string into the SharedPreferences.
 * <p>
 * See {@link android.R.styleable#EditText EditText Attributes}.
 *
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class EditTextPreference extends android.preference.DialogPreference {
    /**
     * The edit text shown in the dialog.
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.EditText mEditText;

    private java.lang.String mText;

    private boolean mTextSet;

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mEditText = new android.widget.EditText(context, attrs);
        // Give it an ID so it can be saved/restored
        mEditText.setId(com.android.internal.R.id.edit);
        /* The preference framework and view framework both have an 'enabled'
        attribute. Most likely, the 'enabled' specified in this XML is for
        the preference framework, but it was also given to the view framework.
        We reset the enabled state.
         */
        mEditText.setEnabled(true);
    }

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditTextPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.editTextPreferenceStyle);
    }

    public EditTextPreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Saves the text to the {@link SharedPreferences}.
     *
     * @param text
     * 		The text to save
     */
    public void setText(java.lang.String text) {
        // Always persist/notify the first time.
        final boolean changed = !android.text.TextUtils.equals(mText, text);
        if (changed || (!mTextSet)) {
            mText = text;
            mTextSet = true;
            persistString(text);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    /**
     * Gets the text from the {@link SharedPreferences}.
     *
     * @return The current preference value.
     */
    public java.lang.String getText() {
        return mText;
    }

    @java.lang.Override
    protected void onBindDialogView(android.view.View view) {
        super.onBindDialogView(view);
        android.widget.EditText editText = mEditText;
        editText.setText(getText());
        android.view.ViewParent oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((android.view.ViewGroup) (oldParent)).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
    }

    /**
     * Adds the EditText widget of this preference to the dialog's view.
     *
     * @param dialogView
     * 		The dialog view.
     */
    protected void onAddEditTextToDialogView(android.view.View dialogView, android.widget.EditText editText) {
        android.view.ViewGroup container = ((android.view.ViewGroup) (dialogView.findViewById(com.android.internal.R.id.edittext_container)));
        if (container != null) {
            container.addView(editText, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @java.lang.Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            java.lang.String value = mEditText.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
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

    /**
     * Returns the {@link EditText} widget that will be shown in the dialog.
     *
     * @return The {@link EditText} widget that will be shown in the dialog.
     */
    public android.widget.EditText getEditText() {
        return mEditText;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.preference.EditTextPreference.SavedState myState = new android.preference.EditTextPreference.SavedState(superState);
        myState.text = getText();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.EditTextPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.EditTextPreference.SavedState myState = ((android.preference.EditTextPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        setText(myState.text);
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
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

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.EditTextPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.EditTextPreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

