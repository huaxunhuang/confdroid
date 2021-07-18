/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * EditText widget that monitors keyboard changes.
 */
public class SearchEditText extends android.support.v17.leanback.widget.StreamingTextView {
    private static final java.lang.String TAG = android.support.v17.leanback.widget.SearchEditText.class.getSimpleName();

    private static final boolean DEBUG = false;

    /**
     * Interface for receiving notification when the keyboard is dismissed.
     */
    public interface OnKeyboardDismissListener {
        /**
         * Method invoked when the keyboard is dismissed.
         */
        public void onKeyboardDismiss();
    }

    private android.support.v17.leanback.widget.SearchEditText.OnKeyboardDismissListener mKeyboardDismissListener;

    public SearchEditText(android.content.Context context) {
        this(context, null);
    }

    public SearchEditText(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.style.TextAppearance_Leanback_SearchTextEdit);
    }

    public SearchEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @java.lang.Override
    public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_BACK) {
            if (android.support.v17.leanback.widget.SearchEditText.DEBUG)
                android.util.Log.v(android.support.v17.leanback.widget.SearchEditText.TAG, "Keyboard being dismissed");

            if (mKeyboardDismissListener != null) {
                mKeyboardDismissListener.onKeyboardDismiss();
            }
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * Sets a keyboard dismissed listener.
     *
     * @param listener
     * 		The listener.
     */
    public void setOnKeyboardDismissListener(android.support.v17.leanback.widget.SearchEditText.OnKeyboardDismissListener listener) {
        mKeyboardDismissListener = listener;
    }
}

