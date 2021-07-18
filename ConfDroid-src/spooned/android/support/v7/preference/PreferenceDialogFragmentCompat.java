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
 * Abstract base class which presents a dialog associated with a
 * {@link android.support.v7.preference.DialogPreference}. Since the preference object may
 * not be available during fragment re-creation, the necessary information for displaying the dialog
 * is read once during the initial call to {@link #onCreate(Bundle)} and saved/restored in the saved
 * instance state. Custom subclasses should also follow this pattern.
 */
public abstract class PreferenceDialogFragmentCompat extends android.support.v4.app.DialogFragment implements android.content.DialogInterface.OnClickListener {
    protected static final java.lang.String ARG_KEY = "key";

    private static final java.lang.String SAVE_STATE_TITLE = "PreferenceDialogFragment.title";

    private static final java.lang.String SAVE_STATE_POSITIVE_TEXT = "PreferenceDialogFragment.positiveText";

    private static final java.lang.String SAVE_STATE_NEGATIVE_TEXT = "PreferenceDialogFragment.negativeText";

    private static final java.lang.String SAVE_STATE_MESSAGE = "PreferenceDialogFragment.message";

    private static final java.lang.String SAVE_STATE_LAYOUT = "PreferenceDialogFragment.layout";

    private static final java.lang.String SAVE_STATE_ICON = "PreferenceDialogFragment.icon";

    private android.support.v7.preference.DialogPreference mPreference;

    private java.lang.CharSequence mDialogTitle;

    private java.lang.CharSequence mPositiveButtonText;

    private java.lang.CharSequence mNegativeButtonText;

    private java.lang.CharSequence mDialogMessage;

    @android.support.annotation.LayoutRes
    private int mDialogLayoutRes;

    private android.graphics.drawable.BitmapDrawable mDialogIcon;

    /**
     * Which button was clicked.
     */
    private int mWhichButtonClicked;

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v4.app.Fragment rawFragment = getTargetFragment();
        if (!(rawFragment instanceof android.support.v7.preference.DialogPreference.TargetFragment)) {
            throw new java.lang.IllegalStateException("Target fragment must implement TargetFragment" + " interface");
        }
        final android.support.v7.preference.DialogPreference.TargetFragment fragment = ((android.support.v7.preference.DialogPreference.TargetFragment) (rawFragment));
        final java.lang.String key = getArguments().getString(android.support.v7.preference.PreferenceDialogFragmentCompat.ARG_KEY);
        if (savedInstanceState == null) {
            mPreference = ((android.support.v7.preference.DialogPreference) (fragment.findPreference(key)));
            mDialogTitle = mPreference.getDialogTitle();
            mPositiveButtonText = mPreference.getPositiveButtonText();
            mNegativeButtonText = mPreference.getNegativeButtonText();
            mDialogMessage = mPreference.getDialogMessage();
            mDialogLayoutRes = mPreference.getDialogLayoutResource();
            final android.graphics.drawable.Drawable icon = mPreference.getDialogIcon();
            if ((icon == null) || (icon instanceof android.graphics.drawable.BitmapDrawable)) {
                mDialogIcon = ((android.graphics.drawable.BitmapDrawable) (icon));
            } else {
                final android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
                final android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
                icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                icon.draw(canvas);
                mDialogIcon = new android.graphics.drawable.BitmapDrawable(getResources(), bitmap);
            }
        } else {
            mDialogTitle = savedInstanceState.getCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_TITLE);
            mPositiveButtonText = savedInstanceState.getCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_POSITIVE_TEXT);
            mNegativeButtonText = savedInstanceState.getCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_NEGATIVE_TEXT);
            mDialogMessage = savedInstanceState.getCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_MESSAGE);
            mDialogLayoutRes = savedInstanceState.getInt(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_LAYOUT, 0);
            final android.graphics.Bitmap bitmap = savedInstanceState.getParcelable(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_ICON);
            if (bitmap != null) {
                mDialogIcon = new android.graphics.drawable.BitmapDrawable(getResources(), bitmap);
            }
        }
    }

    @java.lang.Override
    public void onSaveInstanceState(@android.support.annotation.NonNull
    android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_TITLE, mDialogTitle);
        outState.putCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_POSITIVE_TEXT, mPositiveButtonText);
        outState.putCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_NEGATIVE_TEXT, mNegativeButtonText);
        outState.putCharSequence(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_MESSAGE, mDialogMessage);
        outState.putInt(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_LAYOUT, mDialogLayoutRes);
        if (mDialogIcon != null) {
            outState.putParcelable(android.support.v7.preference.PreferenceDialogFragmentCompat.SAVE_STATE_ICON, mDialogIcon.getBitmap());
        }
    }

    @java.lang.Override
    @android.support.annotation.NonNull
    public android.app.Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
        final android.content.Context context = getActivity();
        mWhichButtonClicked = android.content.DialogInterface.BUTTON_NEGATIVE;
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context).setTitle(mDialogTitle).setIcon(mDialogIcon).setPositiveButton(mPositiveButtonText, this).setNegativeButton(mNegativeButtonText, this);
        android.view.View contentView = onCreateDialogView(context);
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.setView(contentView);
        } else {
            builder.setMessage(mDialogMessage);
        }
        onPrepareDialogBuilder(builder);
        // Create the dialog
        final android.app.Dialog dialog = builder.create();
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        return dialog;
    }

    /**
     * Get the preference that requested this dialog. Available after {@link #onCreate(Bundle)} has
     * been called on the {@link PreferenceFragmentCompat} which launched this dialog.
     *
     * @return The {@link DialogPreference} associated with this
    dialog.
     */
    public android.support.v7.preference.DialogPreference getPreference() {
        if (mPreference == null) {
            final java.lang.String key = getArguments().getString(android.support.v7.preference.PreferenceDialogFragmentCompat.ARG_KEY);
            final android.support.v7.preference.DialogPreference.TargetFragment fragment = ((android.support.v7.preference.DialogPreference.TargetFragment) (getTargetFragment()));
            mPreference = ((android.support.v7.preference.DialogPreference) (fragment.findPreference(key)));
        }
        return mPreference;
    }

    /**
     * Prepares the dialog builder to be shown when the preference is clicked.
     * Use this to set custom properties on the dialog.
     * <p>
     * Do not {@link AlertDialog.Builder#create()} or
     * {@link AlertDialog.Builder#show()}.
     */
    protected void onPrepareDialogBuilder(android.support.v7.app.AlertDialog.Builder builder) {
    }

    /**
     * Returns whether the preference needs to display a soft input method when the dialog
     * is displayed. Default is false. Subclasses should override this method if they need
     * the soft input method brought up automatically.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected boolean needInputMethod() {
        return false;
    }

    /**
     * Sets the required flags on the dialog window to enable input method window to show up.
     */
    private void requestInputMethod(android.app.Dialog dialog) {
        android.view.Window window = dialog.getWindow();
        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * Creates the content view for the dialog (if a custom content view is
     * required). By default, it inflates the dialog layout resource if it is
     * set.
     *
     * @return The content View for the dialog.
     * @see DialogPreference#setLayoutResource(int)
     */
    protected android.view.View onCreateDialogView(android.content.Context context) {
        final int resId = mDialogLayoutRes;
        if (resId == 0) {
            return null;
        }
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        return inflater.inflate(resId, null);
    }

    /**
     * Binds views in the content View of the dialog to data.
     * <p>
     * Make sure to call through to the superclass implementation.
     *
     * @param view
     * 		The content View of the dialog, if it is custom.
     */
    protected void onBindDialogView(android.view.View view) {
        android.view.View dialogMessageView = view.findViewById(android.R.id.message);
        if (dialogMessageView != null) {
            final java.lang.CharSequence message = mDialogMessage;
            int newVisibility = android.view.View.GONE;
            if (!android.text.TextUtils.isEmpty(message)) {
                if (dialogMessageView instanceof android.widget.TextView) {
                    ((android.widget.TextView) (dialogMessageView)).setText(message);
                }
                newVisibility = android.view.View.VISIBLE;
            }
            if (dialogMessageView.getVisibility() != newVisibility) {
                dialogMessageView.setVisibility(newVisibility);
            }
        }
    }

    @java.lang.Override
    public void onClick(android.content.DialogInterface dialog, int which) {
        mWhichButtonClicked = which;
    }

    @java.lang.Override
    public void onDismiss(android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        onDialogClosed(mWhichButtonClicked == android.content.DialogInterface.BUTTON_POSITIVE);
    }

    public abstract void onDialogClosed(boolean positiveResult);
}

