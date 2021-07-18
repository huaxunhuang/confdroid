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
 * limitations under the License.
 */
package android.support.v7.app;


/**
 * A subclass of Dialog that can display one, two or three buttons. If you only want to
 * display a String in this dialog box, use the setMessage() method.  If you
 * want to display a more complex view, look up the FrameLayout called "custom"
 * and add your view to it:
 *
 * <pre>
 * FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
 * fl.addView(myView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
 * </pre>
 *
 * <p>The AlertDialog class takes care of automatically setting
 * {@link WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM
 * WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM} for you based on whether
 * any views in the dialog return true from {@link View#onCheckIsTextEditor()
 * View.onCheckIsTextEditor()}.  Generally you want this set for a Dialog
 * without text editors, so that it will be placed on top of the current
 * input method UI.  You can modify this behavior by forcing the flag to your
 * desired mode after calling {@link #onCreate}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating dialogs, read the
 * <a href="{@docRoot }guide/topics/ui/dialogs.html">Dialogs</a> developer guide.</p>
 * </div>
 */
public class AlertDialog extends android.support.v7.app.AppCompatDialog implements android.content.DialogInterface {
    final android.support.v7.app.AlertController mAlert;

    /**
     * No layout hint.
     */
    static final int LAYOUT_HINT_NONE = 0;

    /**
     * Hint layout to the side.
     */
    static final int LAYOUT_HINT_SIDE = 1;

    protected AlertDialog(@android.support.annotation.NonNull
    android.content.Context context) {
        this(context, 0);
    }

    /**
     * Construct an AlertDialog that uses an explicit theme.  The actual style
     * that an AlertDialog uses is a private implementation, however you can
     * here supply either the name of an attribute in the theme from which
     * to get the dialog's style (such as {@link R.attr#alertDialogTheme}.
     */
    protected AlertDialog(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.StyleRes
    int themeResId) {
        super(context, android.support.v7.app.AlertDialog.resolveDialogTheme(context, themeResId));
        mAlert = new android.support.v7.app.AlertController(getContext(), this, getWindow());
    }

    protected AlertDialog(@android.support.annotation.NonNull
    android.content.Context context, boolean cancelable, @android.support.annotation.Nullable
    android.content.DialogInterface.OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    static int resolveDialogTheme(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.StyleRes
    int resid) {
        if (resid >= 0x1000000) {
            // start of real resource IDs.
            return resid;
        } else {
            android.util.TypedValue outValue = new android.util.TypedValue();
            context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    /**
     * Gets one of the buttons used in the dialog. Returns null if the specified
     * button does not exist or the dialog has not yet been fully created (for
     * example, via {@link #show()} or {@link #create()}).
     *
     * @param whichButton
     * 		The identifier of the button that should be returned.
     * 		For example, this can be
     * 		{@link DialogInterface#BUTTON_POSITIVE}.
     * @return The button from the dialog, or null if a button does not exist.
     */
    public android.widget.Button getButton(int whichButton) {
        return mAlert.getButton(whichButton);
    }

    /**
     * Gets the list view used in the dialog.
     *
     * @return The {@link ListView} from the dialog.
     */
    public android.widget.ListView getListView() {
        return mAlert.getListView();
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        super.setTitle(title);
        mAlert.setTitle(title);
    }

    /**
     *
     *
     * @see Builder#setCustomTitle(View)

    This method has no effect if called after {@link #show()}.
     */
    public void setCustomTitle(android.view.View customTitleView) {
        mAlert.setCustomTitle(customTitleView);
    }

    /**
     * Sets the message to display.
     *
     * @param message
     * 		The message to display in the dialog.
     */
    public void setMessage(java.lang.CharSequence message) {
        mAlert.setMessage(message);
    }

    /**
     * Set the view to display in the dialog. This method has no effect if called
     * after {@link #show()}.
     */
    public void setView(android.view.View view) {
        mAlert.setView(view);
    }

    /**
     * Set the view to display in the dialog, specifying the spacing to appear around that
     * view.  This method has no effect if called after {@link #show()}.
     *
     * @param view
     * 		The view to show in the content area of the dialog
     * @param viewSpacingLeft
     * 		Extra space to appear to the left of {@code view}
     * @param viewSpacingTop
     * 		Extra space to appear above {@code view}
     * @param viewSpacingRight
     * 		Extra space to appear to the right of {@code view}
     * @param viewSpacingBottom
     * 		Extra space to appear below {@code view}
     */
    public void setView(android.view.View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /**
     * Internal api to allow hinting for the best button panel layout.
     */
    void setButtonPanelLayoutHint(int layoutHint) {
        mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    /**
     * Sets a message to be sent when a button is pressed. This method has no effect if called
     * after {@link #show()}.
     *
     * @param whichButton
     * 		Which button to set the message for, can be one of
     * 		{@link DialogInterface#BUTTON_POSITIVE},
     * 		{@link DialogInterface#BUTTON_NEGATIVE}, or
     * 		{@link DialogInterface#BUTTON_NEUTRAL}
     * @param text
     * 		The text to display in positive button.
     * @param msg
     * 		The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, java.lang.CharSequence text, android.os.Message msg) {
        mAlert.setButton(whichButton, text, null, msg);
    }

    /**
     * Sets a listener to be invoked when the positive button of the dialog is pressed. This method
     * has no effect if called after {@link #show()}.
     *
     * @param whichButton
     * 		Which button to set the listener on, can be one of
     * 		{@link DialogInterface#BUTTON_POSITIVE},
     * 		{@link DialogInterface#BUTTON_NEGATIVE}, or
     * 		{@link DialogInterface#BUTTON_NEUTRAL}
     * @param text
     * 		The text to display in positive button.
     * @param listener
     * 		The {@link DialogInterface.OnClickListener} to use.
     */
    public void setButton(int whichButton, java.lang.CharSequence text, android.content.DialogInterface.OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null);
    }

    /**
     * Set resId to 0 if you don't want an icon.
     *
     * @param resId
     * 		the resourceId of the drawable to use as the icon or 0
     * 		if you don't want an icon.
     */
    public void setIcon(int resId) {
        mAlert.setIcon(resId);
    }

    /**
     * Set the {@link Drawable} to be used in the title.
     *
     * @param icon
     * 		Drawable to use as the icon or null if you don't want an icon.
     */
    public void setIcon(android.graphics.drawable.Drawable icon) {
        mAlert.setIcon(icon);
    }

    /**
     * Sets an icon as supplied by a theme attribute. e.g. android.R.attr.alertDialogIcon
     *
     * @param attrId
     * 		ID of a theme attribute that points to a drawable resource.
     */
    public void setIconAttribute(int attrId) {
        android.util.TypedValue out = new android.util.TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        mAlert.setIcon(out.resourceId);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlert.installContent();
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private final android.support.v7.app.AlertController.AlertParams P;

        private final int mTheme;

        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p>
         * The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context
         * 		the parent context
         */
        public Builder(@android.support.annotation.NonNull
        android.content.Context context) {
            this(context, android.support.v7.app.AlertDialog.resolveDialogTheme(context, 0));
        }

        /**
         * Creates a builder for an alert dialog that uses an explicit theme
         * resource.
         * <p>
         * The specified theme resource ({@code themeResId}) is applied on top
         * of the parent {@code context}'s theme. It may be specified as a
         * style resource containing a fully-populated theme, such as
         * {@link R.style#Theme_AppCompat_Dialog}, to replace all
         * attributes in the parent {@code context}'s theme including primary
         * and accent colors.
         * <p>
         * To preserve attributes such as primary and accent colors, the
         * {@code themeResId} may instead be specified as an overlay theme such
         * as {@link R.style#ThemeOverlay_AppCompat_Dialog}. This will
         * override only the window attributes necessary to style the alert
         * window as a dialog.
         * <p>
         * Alternatively, the {@code themeResId} may be specified as {@code 0}
         * to use the parent {@code context}'s resolved value for
         * {@link android.R.attr#alertDialogTheme}.
         *
         * @param context
         * 		the parent context
         * @param themeResId
         * 		the resource ID of the theme against which to inflate
         * 		this dialog, or {@code 0} to use the parent
         * 		{@code context}'s default alert dialog theme
         */
        public Builder(@android.support.annotation.NonNull
        android.content.Context context, @android.support.annotation.StyleRes
        int themeResId) {
            P = new android.support.v7.app.AlertController.AlertParams(new android.view.ContextThemeWrapper(context, android.support.v7.app.AlertDialog.resolveDialogTheme(context, themeResId)));
            mTheme = themeResId;
        }

        /**
         * Returns a {@link Context} with the appropriate theme for dialogs created by this Builder.
         * Applications should use this Context for obtaining LayoutInflaters for inflating views
         * that will be used in the resulting dialogs, as it will cause views to be inflated with
         * the correct theme.
         *
         * @return A Context for built Dialogs.
         */
        @android.support.annotation.NonNull
        public android.content.Context getContext() {
            return P.mContext;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setTitle(@android.support.annotation.StringRes
        int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setTitle(java.lang.CharSequence title) {
            P.mTitle = title;
            return this;
        }

        /**
         * Set the title using the custom view {@code customTitleView}.
         * <p>
         * The methods {@link #setTitle(int)} and {@link #setIcon(int)} should
         * be sufficient for most titles, but this is provided if the title
         * needs more customization. Using this will replace the title and icon
         * set via the other methods.
         * <p>
         * <strong>Note:</strong> To ensure consistent styling, the custom view
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param customTitleView
         * 		the custom view to use as the title
         * @return this Builder object to allow for chaining of calls to set
        methods
         */
        public android.support.v7.app.AlertDialog.Builder setCustomTitle(android.view.View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setMessage(@android.support.annotation.StringRes
        int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setMessage(java.lang.CharSequence message) {
            P.mMessage = message;
            return this;
        }

        /**
         * Set the resource id of the {@link Drawable} to be used in the title.
         * <p>
         * Takes precedence over values set using {@link #setIcon(Drawable)}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setIcon(@android.support.annotation.DrawableRes
        int iconId) {
            P.mIconId = iconId;
            return this;
        }

        /**
         * Set the {@link Drawable} to be used in the title.
         * <p>
         * <strong>Note:</strong> To ensure consistent styling, the drawable
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @return this Builder object to allow for chaining of calls to set
        methods
         */
        public android.support.v7.app.AlertDialog.Builder setIcon(android.graphics.drawable.Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        /**
         * Set an icon as supplied by a theme attribute. e.g.
         * {@link android.R.attr#alertDialogIcon}.
         * <p>
         * Takes precedence over values set using {@link #setIcon(int)} or
         * {@link #setIcon(Drawable)}.
         *
         * @param attrId
         * 		ID of a theme attribute that points to a drawable resource.
         */
        public android.support.v7.app.AlertDialog.Builder setIconAttribute(@android.support.annotation.AttrRes
        int attrId) {
            android.util.TypedValue out = new android.util.TypedValue();
            P.mContext.getTheme().resolveAttribute(attrId, out, true);
            P.mIconId = out.resourceId;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId
         * 		The resource id of the text to display in the positive button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setPositiveButton(@android.support.annotation.StringRes
        int textId, final android.content.DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text
         * 		The text to display in the positive button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setPositiveButton(java.lang.CharSequence text, final android.content.DialogInterface.OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId
         * 		The resource id of the text to display in the negative button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setNegativeButton(@android.support.annotation.StringRes
        int textId, final android.content.DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text
         * 		The text to display in the negative button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setNegativeButton(java.lang.CharSequence text, final android.content.DialogInterface.OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId
         * 		The resource id of the text to display in the neutral button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setNeutralButton(@android.support.annotation.StringRes
        int textId, final android.content.DialogInterface.OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text
         * 		The text to display in the neutral button
         * @param listener
         * 		The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setNeutralButton(java.lang.CharSequence text, final android.content.DialogInterface.OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setOnCancelListener(android.content.DialogInterface.OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setOnDismissListener(android.content.DialogInterface.OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setOnKeyListener(android.content.DialogInterface.OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener. This should be an array type i.e. R.array.foo
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setItems(@android.support.annotation.ArrayRes
        int itemsId, final android.content.DialogInterface.OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setItems(java.lang.CharSequence[] items, final android.content.DialogInterface.OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link ListAdapter}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param adapter
         * 		The {@link ListAdapter} to supply the list of items
         * @param listener
         * 		The listener that will be called when an item is clicked.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setAdapter(final android.widget.ListAdapter adapter, final android.content.DialogInterface.OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items, which are supplied by the given {@link Cursor}, to be
         * displayed in the dialog as the content, you will be notified of the
         * selected item via the supplied listener.
         *
         * @param cursor
         * 		The {@link Cursor} to supply the list of items
         * @param listener
         * 		The listener that will be called when an item is clicked.
         * @param labelColumn
         * 		The column name on the cursor containing the string to display
         * 		in the label.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setCursor(final android.database.Cursor cursor, final android.content.DialogInterface.OnClickListener listener, java.lang.String labelColumn) {
            P.mCursor = cursor;
            P.mLabelColumn = labelColumn;
            P.mOnClickListener = listener;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * This should be an array type, e.g. R.array.foo. The list will have
         * a check mark displayed to the right of the text for each checked
         * item. Clicking on an item in the list will not dismiss the dialog.
         * Clicking on a button will dismiss the dialog.
         *
         * @param itemsId
         * 		the resource id of an array i.e. R.array.foo
         * @param checkedItems
         * 		specifies which items are checked. It should be null in which case no
         * 		items are checked. If non null it must be exactly the same length as the array of
         * 		items.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setMultiChoiceItems(@android.support.annotation.ArrayRes
        int itemsId, boolean[] checkedItems, final android.content.DialogInterface.OnMultiChoiceClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items
         * 		the text of the items to be displayed in the list.
         * @param checkedItems
         * 		specifies which items are checked. It should be null in which case no
         * 		items are checked. If non null it must be exactly the same length as the array of
         * 		items.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setMultiChoiceItems(java.lang.CharSequence[] items, boolean[] checkedItems, final android.content.DialogInterface.OnMultiChoiceClickListener listener) {
            P.mItems = items;
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content,
         * you will be notified of the selected item via the supplied listener.
         * The list will have a check mark displayed to the right of the text
         * for each checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor
         * 		the cursor used to provide the items.
         * @param isCheckedColumn
         * 		specifies the column name on the cursor to use to determine
         * 		whether a checkbox is checked or not. It must return an integer value where 1
         * 		means checked and 0 means unchecked.
         * @param labelColumn
         * 		The column name on the cursor containing the string to display in the
         * 		label.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setMultiChoiceItems(android.database.Cursor cursor, java.lang.String isCheckedColumn, java.lang.String labelColumn, final android.content.DialogInterface.OnMultiChoiceClickListener listener) {
            P.mCursor = cursor;
            P.mOnCheckboxClickListener = listener;
            P.mIsCheckedColumn = isCheckedColumn;
            P.mLabelColumn = labelColumn;
            P.mIsMultiChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. This should be an array type i.e.
         * R.array.foo The list will have a check mark displayed to the right of the text for the
         * checked item. Clicking on an item in the list will not dismiss the dialog. Clicking on a
         * button will dismiss the dialog.
         *
         * @param itemsId
         * 		the resource id of an array i.e. R.array.foo
         * @param checkedItem
         * 		specifies which item is checked. If -1 no items are checked.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setSingleChoiceItems(@android.support.annotation.ArrayRes
        int itemsId, int checkedItem, final android.content.DialogInterface.OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param cursor
         * 		the cursor to retrieve the items from.
         * @param checkedItem
         * 		specifies which item is checked. If -1 no items are checked.
         * @param labelColumn
         * 		The column name on the cursor containing the string to display in the
         * 		label.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setSingleChoiceItems(android.database.Cursor cursor, int checkedItem, java.lang.String labelColumn, final android.content.DialogInterface.OnClickListener listener) {
            P.mCursor = cursor;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mLabelColumn = labelColumn;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param items
         * 		the items to be displayed.
         * @param checkedItem
         * 		specifies which item is checked. If -1 no items are checked.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setSingleChoiceItems(java.lang.CharSequence[] items, int checkedItem, final android.content.DialogInterface.OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of
         * the selected item via the supplied listener. The list will have a check mark displayed to
         * the right of the text for the checked item. Clicking on an item in the list will not
         * dismiss the dialog. Clicking on a button will dismiss the dialog.
         *
         * @param adapter
         * 		The {@link ListAdapter} to supply the list of items
         * @param checkedItem
         * 		specifies which item is checked. If -1 no items are checked.
         * @param listener
         * 		notified when an item on the list is clicked. The dialog will not be
         * 		dismissed when an item is clicked. It will only be dismissed if clicked on a
         * 		button, if no buttons are supplied it's up to the user to dismiss the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public android.support.v7.app.AlertDialog.Builder setSingleChoiceItems(android.widget.ListAdapter adapter, int checkedItem, final android.content.DialogInterface.OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        /**
         * Sets a listener to be invoked when an item in the list is selected.
         *
         * @param listener
         * 		the listener to be invoked
         * @return this Builder object to allow for chaining of calls to set methods
         * @see AdapterView#setOnItemSelectedListener(android.widget.AdapterView.OnItemSelectedListener)
         */
        public android.support.v7.app.AlertDialog.Builder setOnItemSelectedListener(final android.widget.AdapterView.OnItemSelectedListener listener) {
            P.mOnItemSelectedListener = listener;
            return this;
        }

        /**
         * Set a custom view resource to be the contents of the Dialog. The
         * resource will be inflated, adding all top-level views to the screen.
         *
         * @param layoutResId
         * 		Resource ID to be inflated.
         * @return this Builder object to allow for chaining of calls to set
        methods
         */
        public android.support.v7.app.AlertDialog.Builder setView(int layoutResId) {
            P.mView = null;
            P.mViewLayoutResId = layoutResId;
            P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * Sets a custom view to be the contents of the alert dialog.
         * <p>
         * When using a pre-Holo theme, if the supplied view is an instance of
         * a {@link ListView} then the light background will be used.
         * <p>
         * <strong>Note:</strong> To ensure consistent styling, the custom view
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @param view
         * 		the view to use as the contents of the alert dialog
         * @return this Builder object to allow for chaining of calls to set
        methods
         */
        public android.support.v7.app.AlertDialog.Builder setView(android.view.View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = false;
            return this;
        }

        /**
         * Set a custom view to be the contents of the Dialog, specifying the
         * spacing to appear around that view. If the supplied view is an
         * instance of a {@link ListView} the light background will be used.
         *
         * @param view
         * 		The view to use as the contents of the Dialog.
         * @param viewSpacingLeft
         * 		Spacing between the left edge of the view and
         * 		the dialog frame
         * @param viewSpacingTop
         * 		Spacing between the top edge of the view and
         * 		the dialog frame
         * @param viewSpacingRight
         * 		Spacing between the right edge of the view
         * 		and the dialog frame
         * @param viewSpacingBottom
         * 		Spacing between the bottom edge of the view
         * 		and the dialog frame
         * @return This Builder object to allow for chaining of calls to set
        methods


        This is currently hidden because it seems like people should just
        be able to put padding around the view.
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.Deprecated
        public android.support.v7.app.AlertDialog.Builder setView(android.view.View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        /**
         * Sets the Dialog to use the inverse background, regardless of what the
         * contents is.
         *
         * @param useInverseBackground
         * 		Whether to use the inverse background
         * @return This Builder object to allow for chaining of calls to set methods
         * @deprecated This flag is only used for pre-Material themes. Instead,
        specify the window background using on the alert dialog
        theme.
         */
        @java.lang.Deprecated
        public android.support.v7.app.AlertDialog.Builder setInverseBackgroundForced(boolean useInverseBackground) {
            P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        public android.support.v7.app.AlertDialog.Builder setRecycleOnMeasureEnabled(boolean enabled) {
            P.mRecycleOnMeasure = enabled;
            return this;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public android.support.v7.app.AlertDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog(P.mContext, mTheme);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public android.support.v7.app.AlertDialog show() {
            final android.support.v7.app.AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}

