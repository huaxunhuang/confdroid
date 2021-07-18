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


/**
 * This widget is a layout that contains several specifically-named child views that
 * handle keyboard entry interpreted as standard phone dialpad digits.
 *
 * @deprecated Use a custom view or layout to handle this functionality instead
 */
@java.lang.Deprecated
public class DialerFilter extends android.widget.RelativeLayout {
    public DialerFilter(android.content.Context context) {
        super(context);
    }

    public DialerFilter(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // Setup the filter view
        mInputFilters = new android.text.InputFilter[]{ new android.text.InputFilter.AllCaps() };
        mHint = ((android.widget.EditText) (findViewById(com.android.internal.R.id.hint)));
        if (mHint == null) {
            throw new java.lang.IllegalStateException("DialerFilter must have a child EditText named hint");
        }
        mHint.setFilters(mInputFilters);
        mLetters = mHint;
        mLetters.setKeyListener(android.text.method.TextKeyListener.getInstance());
        mLetters.setMovementMethod(null);
        mLetters.setFocusable(false);
        // Setup the digits view
        mPrimary = ((android.widget.EditText) (findViewById(com.android.internal.R.id.primary)));
        if (mPrimary == null) {
            throw new java.lang.IllegalStateException("DialerFilter must have a child EditText named primary");
        }
        mPrimary.setFilters(mInputFilters);
        mDigits = mPrimary;
        mDigits.setKeyListener(android.text.method.DialerKeyListener.getInstance());
        mDigits.setMovementMethod(null);
        mDigits.setFocusable(false);
        // Look for an icon
        mIcon = ((android.widget.ImageView) (findViewById(com.android.internal.R.id.icon)));
        // Setup focus & highlight for this view
        setFocusable(true);
        // XXX Force the mode to QWERTY for now, since 12-key isn't supported
        mIsQwerty = true;
        setMode(android.widget.DialerFilter.DIGITS_AND_LETTERS);
    }

    /**
     * Only show the icon view when focused, if there is one.
     */
    @java.lang.Override
    protected void onFocusChanged(boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (mIcon != null) {
            mIcon.setVisibility(focused ? android.view.View.VISIBLE : android.view.View.GONE);
        }
    }

    public boolean isQwertyKeyboard() {
        return mIsQwerty;
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        boolean handled = false;
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
            case android.view.KeyEvent.KEYCODE_ENTER :
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                break;
            case android.view.KeyEvent.KEYCODE_DEL :
                switch (mMode) {
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS :
                        handled = mDigits.onKeyDown(keyCode, event);
                        handled &= mLetters.onKeyDown(keyCode, event);
                        break;
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_DIGITS :
                        handled = mLetters.onKeyDown(keyCode, event);
                        if (mLetters.getText().length() == mDigits.getText().length()) {
                            setMode(android.widget.DialerFilter.DIGITS_AND_LETTERS);
                        }
                        break;
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_LETTERS :
                        if (mDigits.getText().length() == mLetters.getText().length()) {
                            mLetters.onKeyDown(keyCode, event);
                            setMode(android.widget.DialerFilter.DIGITS_AND_LETTERS);
                        }
                        handled = mDigits.onKeyDown(keyCode, event);
                        break;
                    case android.widget.DialerFilter.DIGITS_ONLY :
                        handled = mDigits.onKeyDown(keyCode, event);
                        break;
                    case android.widget.DialerFilter.LETTERS_ONLY :
                        handled = mLetters.onKeyDown(keyCode, event);
                        break;
                }
                break;
            default :
                // mIsQwerty = msg.getKeyIsQwertyKeyboard();
                switch (mMode) {
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS :
                        handled = mLetters.onKeyDown(keyCode, event);
                        // pass this throw so the shift state is correct (for example,
                        // on a standard QWERTY keyboard, * and 8 are on the same key)
                        if (android.view.KeyEvent.isModifierKey(keyCode)) {
                            mDigits.onKeyDown(keyCode, event);
                            handled = true;
                            break;
                        }
                        // Only check to see if the digit is valid if the key is a printing key
                        // in the TextKeyListener. This prevents us from hiding the digits
                        // line when keys like UP and DOWN are hit.
                        // XXX note that KEYCODE_TAB is special-cased here for
                        // devices that share tab and 0 on a single key.
                        boolean isPrint = event.isPrintingKey();
                        if ((isPrint || (keyCode == android.view.KeyEvent.KEYCODE_SPACE)) || (keyCode == android.view.KeyEvent.KEYCODE_TAB)) {
                            char c = event.getMatch(DialerKeyListener.CHARACTERS);
                            if (c != 0) {
                                handled &= mDigits.onKeyDown(keyCode, event);
                            } else {
                                setMode(android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_DIGITS);
                            }
                        }
                        break;
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_LETTERS :
                    case android.widget.DialerFilter.DIGITS_ONLY :
                        handled = mDigits.onKeyDown(keyCode, event);
                        break;
                    case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_DIGITS :
                    case android.widget.DialerFilter.LETTERS_ONLY :
                        handled = mLetters.onKeyDown(keyCode, event);
                        break;
                }
        }
        if (!handled) {
            return super.onKeyDown(keyCode, event);
        } else {
            return true;
        }
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        boolean a = mLetters.onKeyUp(keyCode, event);
        boolean b = mDigits.onKeyUp(keyCode, event);
        return a || b;
    }

    public int getMode() {
        return mMode;
    }

    /**
     * Change the mode of the widget.
     *
     * @param newMode
     * 		The mode to switch to.
     */
    public void setMode(int newMode) {
        switch (newMode) {
            case android.widget.DialerFilter.DIGITS_AND_LETTERS :
                makeDigitsPrimary();
                mLetters.setVisibility(android.view.View.VISIBLE);
                mDigits.setVisibility(android.view.View.VISIBLE);
                break;
            case android.widget.DialerFilter.DIGITS_ONLY :
                makeDigitsPrimary();
                mLetters.setVisibility(android.view.View.GONE);
                mDigits.setVisibility(android.view.View.VISIBLE);
                break;
            case android.widget.DialerFilter.LETTERS_ONLY :
                makeLettersPrimary();
                mLetters.setVisibility(android.view.View.VISIBLE);
                mDigits.setVisibility(android.view.View.GONE);
                break;
            case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_LETTERS :
                makeDigitsPrimary();
                mLetters.setVisibility(android.view.View.INVISIBLE);
                mDigits.setVisibility(android.view.View.VISIBLE);
                break;
            case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_DIGITS :
                makeLettersPrimary();
                mLetters.setVisibility(android.view.View.VISIBLE);
                mDigits.setVisibility(android.view.View.INVISIBLE);
                break;
        }
        int oldMode = mMode;
        mMode = newMode;
        onModeChange(oldMode, newMode);
    }

    private void makeLettersPrimary() {
        if (mPrimary == mDigits) {
            swapPrimaryAndHint(true);
        }
    }

    private void makeDigitsPrimary() {
        if (mPrimary == mLetters) {
            swapPrimaryAndHint(false);
        }
    }

    private void swapPrimaryAndHint(boolean makeLettersPrimary) {
        android.text.Editable lettersText = mLetters.getText();
        android.text.Editable digitsText = mDigits.getText();
        android.text.method.KeyListener lettersInput = mLetters.getKeyListener();
        android.text.method.KeyListener digitsInput = mDigits.getKeyListener();
        if (makeLettersPrimary) {
            mLetters = mPrimary;
            mDigits = mHint;
        } else {
            mLetters = mHint;
            mDigits = mPrimary;
        }
        mLetters.setKeyListener(lettersInput);
        mLetters.setText(lettersText);
        lettersText = mLetters.getText();
        android.text.Selection.setSelection(lettersText, lettersText.length());
        mDigits.setKeyListener(digitsInput);
        mDigits.setText(digitsText);
        digitsText = mDigits.getText();
        android.text.Selection.setSelection(digitsText, digitsText.length());
        // Reset the filters
        mPrimary.setFilters(mInputFilters);
        mHint.setFilters(mInputFilters);
    }

    public java.lang.CharSequence getLetters() {
        if (mLetters.getVisibility() == android.view.View.VISIBLE) {
            return mLetters.getText();
        } else {
            return "";
        }
    }

    public java.lang.CharSequence getDigits() {
        if (mDigits.getVisibility() == android.view.View.VISIBLE) {
            return mDigits.getText();
        } else {
            return "";
        }
    }

    public java.lang.CharSequence getFilterText() {
        if (mMode != android.widget.DialerFilter.DIGITS_ONLY) {
            return getLetters();
        } else {
            return getDigits();
        }
    }

    public void append(java.lang.String text) {
        switch (mMode) {
            case android.widget.DialerFilter.DIGITS_AND_LETTERS :
                mDigits.getText().append(text);
                mLetters.getText().append(text);
                break;
            case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_LETTERS :
            case android.widget.DialerFilter.DIGITS_ONLY :
                mDigits.getText().append(text);
                break;
            case android.widget.DialerFilter.DIGITS_AND_LETTERS_NO_DIGITS :
            case android.widget.DialerFilter.LETTERS_ONLY :
                mLetters.getText().append(text);
                break;
        }
    }

    /**
     * Clears both the digits and the filter text.
     */
    public void clearText() {
        android.text.Editable text;
        text = mLetters.getText();
        text.clear();
        text = mDigits.getText();
        text.clear();
        // Reset the mode based on the hardware type
        if (mIsQwerty) {
            setMode(android.widget.DialerFilter.DIGITS_AND_LETTERS);
        } else {
            setMode(android.widget.DialerFilter.DIGITS_ONLY);
        }
    }

    public void setLettersWatcher(android.text.TextWatcher watcher) {
        java.lang.CharSequence text = mLetters.getText();
        android.text.Spannable span = ((android.text.Spannable) (text));
        span.setSpan(watcher, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    public void setDigitsWatcher(android.text.TextWatcher watcher) {
        java.lang.CharSequence text = mDigits.getText();
        android.text.Spannable span = ((android.text.Spannable) (text));
        span.setSpan(watcher, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    public void setFilterWatcher(android.text.TextWatcher watcher) {
        if (mMode != android.widget.DialerFilter.DIGITS_ONLY) {
            setLettersWatcher(watcher);
        } else {
            setDigitsWatcher(watcher);
        }
    }

    public void removeFilterWatcher(android.text.TextWatcher watcher) {
        android.text.Spannable text;
        if (mMode != android.widget.DialerFilter.DIGITS_ONLY) {
            text = mLetters.getText();
        } else {
            text = mDigits.getText();
        }
        text.removeSpan(watcher);
    }

    /**
     * Called right after the mode changes to give subclasses the option to
     * restyle, etc.
     */
    protected void onModeChange(int oldMode, int newMode) {
    }

    /**
     * This mode has both lines
     */
    public static final int DIGITS_AND_LETTERS = 1;

    /**
     * This mode is when after starting in {@link #DIGITS_AND_LETTERS} mode the filter
     *  has removed all possibility of the digits matching, leaving only the letters line
     */
    public static final int DIGITS_AND_LETTERS_NO_DIGITS = 2;

    /**
     * This mode is when after starting in {@link #DIGITS_AND_LETTERS} mode the filter
     *  has removed all possibility of the letters matching, leaving only the digits line
     */
    public static final int DIGITS_AND_LETTERS_NO_LETTERS = 3;

    /**
     * This mode has only the digits line
     */
    public static final int DIGITS_ONLY = 4;

    /**
     * This mode has only the letters line
     */
    public static final int LETTERS_ONLY = 5;

    android.widget.EditText mLetters;

    android.widget.EditText mDigits;

    android.widget.EditText mPrimary;

    android.widget.EditText mHint;

    android.text.InputFilter[] mInputFilters;

    android.widget.ImageView mIcon;

    int mMode;

    private boolean mIsQwerty;
}

