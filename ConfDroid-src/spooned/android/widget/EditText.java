/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.widget;


/* This is supposed to be a *very* thin veneer over TextView.
Do not make any changes here that do anything that a TextView
with a key listener and a movement method wouldn't do!
 */
/**
 * A user interface element for entering and modifying text.
 * When you define an edit text widget, you must specify the
 * {@link android.R.styleable#TextView_inputType}
 * attribute. For example, for plain text input set inputType to "text":
 * <p>
 * <pre>
 * &lt;EditText
 *     android:id="@+id/plain_text_input"
 *     android:layout_height="wrap_content"
 *     android:layout_width="match_parent"
 *     android:inputType="text"/&gt;</pre>
 *
 * Choosing the input type configures the keyboard type that is shown, acceptable characters,
 * and appearance of the edit text.
 * For example, if you want to accept a secret number, like a unique pin or serial number,
 * you can set inputType to "numericPassword".
 * An inputType of "numericPassword" results in an edit text that accepts numbers only,
 * shows a numeric keyboard when focused, and masks the text that is entered for privacy.
 * <p>
 * See the <a href="{@docRoot }guide/topics/ui/controls/text.html">Text Fields</a>
 * guide for examples of other
 * {@link android.R.styleable#TextView_inputType} settings.
 * </p>
 * <p>You also can receive callbacks as a user changes text by
 * adding a {@link android.text.TextWatcher} to the edit text.
 * This is useful when you want to add auto-save functionality as changes are made,
 * or validate the format of user input, for example.
 * You add a text watcher using the {@link TextView#addTextChangedListener} method.
 * </p>
 * <p>
 * This widget does not support auto-sizing text.
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See {@link android.R.styleable#EditText EditText Attributes},
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 */
public class EditText extends android.widget.TextView {
    public EditText(android.content.Context context) {
        this(context, null);
    }

    public EditText(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.editTextStyle);
    }

    public EditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @java.lang.Override
    public boolean getFreezesText() {
        return true;
    }

    @java.lang.Override
    protected boolean getDefaultEditable() {
        return true;
    }

    @java.lang.Override
    protected android.text.method.MovementMethod getDefaultMovementMethod() {
        return android.text.method.ArrowKeyMovementMethod.getInstance();
    }

    @java.lang.Override
    public android.text.Editable getText() {
        java.lang.CharSequence text = super.getText();
        // This can only happen during construction.
        if (text == null) {
            return null;
        }
        if (text instanceof android.text.Editable) {
            return ((android.text.Editable) (super.getText()));
        }
        super.setText(text, android.widget.TextView.BufferType.EDITABLE);
        return ((android.text.Editable) (super.getText()));
    }

    @java.lang.Override
    public void setText(java.lang.CharSequence text, android.widget.TextView.BufferType type) {
        super.setText(text, android.widget.TextView.BufferType.EDITABLE);
    }

    /**
     * Convenience for {@link Selection#setSelection(Spannable, int, int)}.
     */
    public void setSelection(int start, int stop) {
        android.text.Selection.setSelection(getText(), start, stop);
    }

    /**
     * Convenience for {@link Selection#setSelection(Spannable, int)}.
     */
    public void setSelection(int index) {
        android.text.Selection.setSelection(getText(), index);
    }

    /**
     * Convenience for {@link Selection#selectAll}.
     */
    public void selectAll() {
        android.text.Selection.selectAll(getText());
    }

    /**
     * Convenience for {@link Selection#extendSelection}.
     */
    public void extendSelection(int index) {
        android.text.Selection.extendSelection(getText(), index);
    }

    /**
     * Causes words in the text that are longer than the view's width to be ellipsized instead of
     * broken in the middle. {@link TextUtils.TruncateAt#MARQUEE
     * TextUtils.TruncateAt#MARQUEE} is not supported.
     *
     * @param ellipsis
     * 		Type of ellipsis to be applied.
     * @throws IllegalArgumentException
     * 		When the value of <code>ellipsis</code> parameter is
     * 		{@link TextUtils.TruncateAt#MARQUEE}.
     * @see TextView#setEllipsize(TextUtils.TruncateAt)
     */
    @java.lang.Override
    public void setEllipsize(android.text.TextUtils.TruncateAt ellipsis) {
        if (ellipsis == TextUtils.TruncateAt.MARQUEE) {
            throw new java.lang.IllegalArgumentException("EditText cannot use the ellipsize mode " + "TextUtils.TruncateAt.MARQUEE");
        }
        super.setEllipsize(ellipsis);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.EditText.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected boolean supportsAutoSizeText() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
        }
    }
}

