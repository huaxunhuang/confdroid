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


public class EditTextPreferenceDialogFragmentCompat extends android.support.v7.preference.PreferenceDialogFragmentCompat {
    private static final java.lang.String SAVE_STATE_TEXT = "EditTextPreferenceDialogFragment.text";

    private android.widget.EditText mEditText;

    private java.lang.CharSequence mText;

    public static android.support.v7.preference.EditTextPreferenceDialogFragmentCompat newInstance(java.lang.String key) {
        final android.support.v7.preference.EditTextPreferenceDialogFragmentCompat fragment = new android.support.v7.preference.EditTextPreferenceDialogFragmentCompat();
        final android.os.Bundle b = new android.os.Bundle(1);
        b.putString(android.support.v7.preference.PreferenceDialogFragmentCompat.ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mText = getEditTextPreference().getText();
        } else {
            mText = savedInstanceState.getCharSequence(android.support.v7.preference.EditTextPreferenceDialogFragmentCompat.SAVE_STATE_TEXT);
        }
    }

    @java.lang.Override
    public void onSaveInstanceState(@android.support.annotation.NonNull
    android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(android.support.v7.preference.EditTextPreferenceDialogFragmentCompat.SAVE_STATE_TEXT, mText);
    }

    @java.lang.Override
    protected void onBindDialogView(android.view.View view) {
        super.onBindDialogView(view);
        mEditText = ((android.widget.EditText) (view.findViewById(android.R.id.edit)));
        if (mEditText == null) {
            throw new java.lang.IllegalStateException("Dialog view must contain an EditText with id" + " @android:id/edit");
        }
        mEditText.setText(mText);
    }

    private android.support.v7.preference.EditTextPreference getEditTextPreference() {
        return ((android.support.v7.preference.EditTextPreference) (getPreference()));
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    @java.lang.Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            java.lang.String value = mEditText.getText().toString();
            if (getEditTextPreference().callChangeListener(value)) {
                getEditTextPreference().setText(value);
            }
        }
    }
}

