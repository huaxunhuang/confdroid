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
 * A base class for {@link Preference} objects that are
 * dialog-based. These preferences will, when clicked, open a dialog showing the
 * actual preference controls.
 *
 * @unknown ref android.R.styleable#DialogPreference_dialogTitle
 * @unknown ref android.R.styleable#DialogPreference_dialogMessage
 * @unknown ref android.R.styleable#DialogPreference_dialogIcon
 * @unknown ref android.R.styleable#DialogPreference_dialogLayout
 * @unknown ref android.R.styleable#DialogPreference_positiveButtonText
 * @unknown ref android.R.styleable#DialogPreference_negativeButtonText
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public abstract class DialogPreference extends android.preference.Preference implements android.content.DialogInterface.OnClickListener , android.content.DialogInterface.OnDismissListener , android.preference.PreferenceManager.OnActivityDestroyListener {
    @android.annotation.UnsupportedAppUsage
    private AlertDialog.Builder mBuilder;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mDialogTitle;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mDialogMessage;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mDialogIcon;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mPositiveButtonText;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mNegativeButtonText;

    private int mDialogLayoutResId;

    /**
     * The dialog, if it is showing.
     */
    @android.annotation.UnsupportedAppUsage
    private android.app.Dialog mDialog;

    /**
     * Which button was clicked.
     */
    @android.annotation.UnsupportedAppUsage
    private int mWhichButtonClicked;

    /**
     * Dismiss the dialog on the UI thread, but not inline with handlers
     */
    private final java.lang.Runnable mDismissRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            mDialog.dismiss();
        }
    };

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.DialogPreference, defStyleAttr, defStyleRes);
        mDialogTitle = a.getString(com.android.internal.R.styleable.DialogPreference_dialogTitle);
        if (mDialogTitle == null) {
            // Fallback on the regular title of the preference
            // (the one that is seen in the list)
            mDialogTitle = getTitle();
        }
        mDialogMessage = a.getString(com.android.internal.R.styleable.DialogPreference_dialogMessage);
        mDialogIcon = a.getDrawable(com.android.internal.R.styleable.DialogPreference_dialogIcon);
        mPositiveButtonText = a.getString(com.android.internal.R.styleable.DialogPreference_positiveButtonText);
        mNegativeButtonText = a.getString(com.android.internal.R.styleable.DialogPreference_negativeButtonText);
        mDialogLayoutResId = a.getResourceId(com.android.internal.R.styleable.DialogPreference_dialogLayout, mDialogLayoutResId);
        a.recycle();
    }

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.dialogPreferenceStyle);
    }

    public DialogPreference(android.content.Context context) {
        this(context, null);
    }

    /**
     * Sets the title of the dialog. This will be shown on subsequent dialogs.
     *
     * @param dialogTitle
     * 		The title.
     */
    public void setDialogTitle(java.lang.CharSequence dialogTitle) {
        mDialogTitle = dialogTitle;
    }

    /**
     *
     *
     * @see #setDialogTitle(CharSequence)
     * @param dialogTitleResId
     * 		The dialog title as a resource.
     */
    public void setDialogTitle(int dialogTitleResId) {
        setDialogTitle(getContext().getString(dialogTitleResId));
    }

    /**
     * Returns the title to be shown on subsequent dialogs.
     *
     * @return The title.
     */
    public java.lang.CharSequence getDialogTitle() {
        return mDialogTitle;
    }

    /**
     * Sets the message of the dialog. This will be shown on subsequent dialogs.
     * <p>
     * This message forms the content View of the dialog and conflicts with
     * list-based dialogs, for example. If setting a custom View on a dialog via
     * {@link #setDialogLayoutResource(int)}, include a text View with ID
     * {@link android.R.id#message} and it will be populated with this message.
     *
     * @param dialogMessage
     * 		The message.
     */
    public void setDialogMessage(java.lang.CharSequence dialogMessage) {
        mDialogMessage = dialogMessage;
    }

    /**
     *
     *
     * @see #setDialogMessage(CharSequence)
     * @param dialogMessageResId
     * 		The dialog message as a resource.
     */
    public void setDialogMessage(int dialogMessageResId) {
        setDialogMessage(getContext().getString(dialogMessageResId));
    }

    /**
     * Returns the message to be shown on subsequent dialogs.
     *
     * @return The message.
     */
    public java.lang.CharSequence getDialogMessage() {
        return mDialogMessage;
    }

    /**
     * Sets the icon of the dialog. This will be shown on subsequent dialogs.
     *
     * @param dialogIcon
     * 		The icon, as a {@link Drawable}.
     */
    public void setDialogIcon(android.graphics.drawable.Drawable dialogIcon) {
        mDialogIcon = dialogIcon;
    }

    /**
     * Sets the icon (resource ID) of the dialog. This will be shown on
     * subsequent dialogs.
     *
     * @param dialogIconRes
     * 		The icon, as a resource ID.
     */
    public void setDialogIcon(@android.annotation.DrawableRes
    int dialogIconRes) {
        mDialogIcon = getContext().getDrawable(dialogIconRes);
    }

    /**
     * Returns the icon to be shown on subsequent dialogs.
     *
     * @return The icon, as a {@link Drawable}.
     */
    public android.graphics.drawable.Drawable getDialogIcon() {
        return mDialogIcon;
    }

    /**
     * Sets the text of the positive button of the dialog. This will be shown on
     * subsequent dialogs.
     *
     * @param positiveButtonText
     * 		The text of the positive button.
     */
    public void setPositiveButtonText(java.lang.CharSequence positiveButtonText) {
        mPositiveButtonText = positiveButtonText;
    }

    /**
     *
     *
     * @see #setPositiveButtonText(CharSequence)
     * @param positiveButtonTextResId
     * 		The positive button text as a resource.
     */
    public void setPositiveButtonText(@android.annotation.StringRes
    int positiveButtonTextResId) {
        setPositiveButtonText(getContext().getString(positiveButtonTextResId));
    }

    /**
     * Returns the text of the positive button to be shown on subsequent
     * dialogs.
     *
     * @return The text of the positive button.
     */
    public java.lang.CharSequence getPositiveButtonText() {
        return mPositiveButtonText;
    }

    /**
     * Sets the text of the negative button of the dialog. This will be shown on
     * subsequent dialogs.
     *
     * @param negativeButtonText
     * 		The text of the negative button.
     */
    public void setNegativeButtonText(java.lang.CharSequence negativeButtonText) {
        mNegativeButtonText = negativeButtonText;
    }

    /**
     *
     *
     * @see #setNegativeButtonText(CharSequence)
     * @param negativeButtonTextResId
     * 		The negative button text as a resource.
     */
    public void setNegativeButtonText(@android.annotation.StringRes
    int negativeButtonTextResId) {
        setNegativeButtonText(getContext().getString(negativeButtonTextResId));
    }

    /**
     * Returns the text of the negative button to be shown on subsequent
     * dialogs.
     *
     * @return The text of the negative button.
     */
    public java.lang.CharSequence getNegativeButtonText() {
        return mNegativeButtonText;
    }

    /**
     * Sets the layout resource that is inflated as the {@link View} to be shown
     * as the content View of subsequent dialogs.
     *
     * @param dialogLayoutResId
     * 		The layout resource ID to be inflated.
     * @see #setDialogMessage(CharSequence)
     */
    public void setDialogLayoutResource(int dialogLayoutResId) {
        mDialogLayoutResId = dialogLayoutResId;
    }

    /**
     * Returns the layout resource that is used as the content View for
     * subsequent dialogs.
     *
     * @return The layout resource.
     */
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    /**
     * Prepares the dialog builder to be shown when the preference is clicked.
     * Use this to set custom properties on the dialog.
     * <p>
     * Do not {@link AlertDialog.Builder#create()} or
     * {@link AlertDialog.Builder#show()}.
     */
    protected void onPrepareDialogBuilder(android.app.AlertDialog.Builder builder) {
    }

    @java.lang.Override
    protected void onClick() {
        if ((mDialog != null) && mDialog.isShowing())
            return;

        showDialog(null);
    }

    /**
     * Shows the dialog associated with this Preference. This is normally initiated
     * automatically on clicking on the preference. Call this method if you need to
     * show the dialog on some other event.
     *
     * @param state
     * 		Optional instance state to restore on the dialog
     */
    protected void showDialog(android.os.Bundle state) {
        android.content.Context context = getContext();
        mWhichButtonClicked = android.content.DialogInterface.BUTTON_NEGATIVE;
        mBuilder = new android.app.AlertDialog.Builder(context).setTitle(mDialogTitle).setIcon(mDialogIcon).setPositiveButton(mPositiveButtonText, this).setNegativeButton(mNegativeButtonText, this);
        android.view.View contentView = onCreateDialogView();
        if (contentView != null) {
            onBindDialogView(contentView);
            mBuilder.setView(contentView);
        } else {
            mBuilder.setMessage(mDialogMessage);
        }
        onPrepareDialogBuilder(mBuilder);
        getPreferenceManager().registerOnActivityDestroyListener(this);
        // Create the dialog
        final android.app.Dialog dialog = mDialog = mBuilder.create();
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @java.lang.Override
            public void onShow(android.content.DialogInterface dialog) {
                removeDismissCallbacks();
            }
        });
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    /**
     * Get the DecorView.
     *
     * @return the DecorView for the current dialog window, if it exists.
    If the window does not exist, null is returned.
     */
    @android.annotation.Nullable
    private android.view.View getDecorView() {
        if ((mDialog != null) && (mDialog.getWindow() != null)) {
            return getDecorView();
        }
        return null;
    }

    void postDismiss() {
        removeDismissCallbacks();
        android.view.View decorView = getDecorView();
        if (decorView != null) {
            // If decorView is null, dialog was already dismissed
            decorView.post(mDismissRunnable);
        }
    }

    private void removeDismissCallbacks() {
        android.view.View decorView = getDecorView();
        if (decorView != null) {
            decorView.removeCallbacks(mDismissRunnable);
        }
    }

    /**
     * Returns whether the preference needs to display a soft input method when the dialog
     * is displayed. Default is false. Subclasses should override this method if they need
     * the soft input method brought up automatically.
     *
     * @unknown 
     */
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
     * @see #setLayoutResource(int)
     */
    protected android.view.View onCreateDialogView() {
        if (mDialogLayoutResId == 0) {
            return null;
        }
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mBuilder.getContext());
        return inflater.inflate(mDialogLayoutResId, null);
    }

    /**
     * Binds views in the content View of the dialog to data.
     * <p>
     * Make sure to call through to the superclass implementation.
     *
     * @param view
     * 		The content View of the dialog, if it is custom.
     */
    @android.annotation.CallSuper
    protected void onBindDialogView(android.view.View view) {
        android.view.View dialogMessageView = view.findViewById(com.android.internal.R.id.message);
        if (dialogMessageView != null) {
            final java.lang.CharSequence message = getDialogMessage();
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

    public void onClick(android.content.DialogInterface dialog, int which) {
        mWhichButtonClicked = which;
    }

    @java.lang.Override
    public void onDismiss(android.content.DialogInterface dialog) {
        removeDismissCallbacks();
        getPreferenceManager().unregisterOnActivityDestroyListener(this);
        mDialog = null;
        onDialogClosed(mWhichButtonClicked == android.content.DialogInterface.BUTTON_POSITIVE);
    }

    /**
     * Called when the dialog is dismissed and should be used to save data to
     * the {@link SharedPreferences}.
     *
     * @param positiveResult
     * 		Whether the positive button was clicked (true), or
     * 		the negative button was clicked or the dialog was canceled (false).
     */
    protected void onDialogClosed(boolean positiveResult) {
    }

    /**
     * Gets the dialog that is shown by this preference.
     *
     * @return The dialog, or null if a dialog is not being shown.
     */
    public android.app.Dialog getDialog() {
        return mDialog;
    }

    /**
     * {@inheritDoc }
     */
    public void onActivityDestroy() {
        if ((mDialog == null) || (!mDialog.isShowing())) {
            return;
        }
        mDialog.dismiss();
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if ((mDialog == null) || (!mDialog.isShowing())) {
            return superState;
        }
        final android.preference.DialogPreference.SavedState myState = new android.preference.DialogPreference.SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.DialogPreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.DialogPreference.SavedState myState = ((android.preference.DialogPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
        boolean isDialogShowing;

        android.os.Bundle dialogBundle;

        public SavedState(android.os.Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.DialogPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.DialogPreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

