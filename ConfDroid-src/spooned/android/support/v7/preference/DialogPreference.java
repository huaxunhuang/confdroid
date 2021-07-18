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
 * A base class for {@link Preference} objects that are
 * dialog-based. These preferences will, when clicked, open a dialog showing the
 * actual preference controls.
 *
 * @unknown name android:dialogTitle
 * @unknown name android:dialogMessage
 * @unknown name android:dialogIcon
 * @unknown name android:dialogLayout
 * @unknown name android:positiveButtonText
 * @unknown name android:negativeButtonText
 */
public abstract class DialogPreference extends android.support.v7.preference.Preference {
    public interface TargetFragment {
        android.support.v7.preference.Preference findPreference(java.lang.CharSequence key);
    }

    private java.lang.CharSequence mDialogTitle;

    private java.lang.CharSequence mDialogMessage;

    private android.graphics.drawable.Drawable mDialogIcon;

    private java.lang.CharSequence mPositiveButtonText;

    private java.lang.CharSequence mNegativeButtonText;

    private int mDialogLayoutResId;

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialogPreference, defStyleAttr, defStyleRes);
        mDialogTitle = android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.DialogPreference_dialogTitle, R.styleable.DialogPreference_android_dialogTitle);
        if (mDialogTitle == null) {
            // Fall back on the regular title of the preference
            // (the one that is seen in the list)
            mDialogTitle = getTitle();
        }
        mDialogMessage = android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.DialogPreference_dialogMessage, R.styleable.DialogPreference_android_dialogMessage);
        mDialogIcon = android.support.v4.content.res.TypedArrayUtils.getDrawable(a, R.styleable.DialogPreference_dialogIcon, R.styleable.DialogPreference_android_dialogIcon);
        mPositiveButtonText = android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.DialogPreference_positiveButtonText, R.styleable.DialogPreference_android_positiveButtonText);
        mNegativeButtonText = android.support.v4.content.res.TypedArrayUtils.getString(a, R.styleable.DialogPreference_negativeButtonText, R.styleable.DialogPreference_android_negativeButtonText);
        mDialogLayoutResId = android.support.v4.content.res.TypedArrayUtils.getResourceId(a, R.styleable.DialogPreference_dialogLayout, R.styleable.DialogPreference_android_dialogLayout, 0);
        a.recycle();
    }

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DialogPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, android.support.v4.content.res.TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
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
    public void setDialogIcon(int dialogIconRes) {
        mDialogIcon = android.support.v4.content.ContextCompat.getDrawable(getContext(), dialogIconRes);
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
    public void setPositiveButtonText(int positiveButtonTextResId) {
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
    public void setNegativeButtonText(int negativeButtonTextResId) {
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

    @java.lang.Override
    protected void onClick() {
        getPreferenceManager().showDialog(this);
    }
}

