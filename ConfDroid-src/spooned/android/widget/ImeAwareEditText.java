/**
 * Copyright (C) 2020 The Android Open Source Project
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
 * EditText that keeps track of the IME state, specifically its input connection. This is useful
 * for clients who request the IME before the system has established a connection.
 *
 * @unknown 
 */
public class ImeAwareEditText extends android.widget.EditText {
    private boolean mHasPendingShowSoftInputRequest;

    final java.lang.Runnable mRunShowSoftInputIfNecessary = () -> showSoftInputIfNecessary();

    public ImeAwareEditText(android.content.Context context) {
        super(context, null);
    }

    public ImeAwareEditText(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public ImeAwareEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImeAwareEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * This method is called back by the system when the system is about to establish a connection
     * to the current input method.
     *
     * <p>This is a good and reliable signal to schedule a pending task to call
     * {@link InputMethodManager#showSoftInput(View, int)}.</p>
     *
     * @param editorInfo
     * 		context about the text input field.
     * @return {@link InputConnection} to be passed to the input method.
     */
    @java.lang.Override
    public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo editorInfo) {
        final android.view.inputmethod.InputConnection ic = super.onCreateInputConnection(editorInfo);
        if (mHasPendingShowSoftInputRequest) {
            removeCallbacks(mRunShowSoftInputIfNecessary);
            post(mRunShowSoftInputIfNecessary);
        }
        return ic;
    }

    private void showSoftInputIfNecessary() {
        if (mHasPendingShowSoftInputRequest) {
            final android.view.inputmethod.InputMethodManager imm = getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
            imm.showSoftInput(this, 0);
            mHasPendingShowSoftInputRequest = false;
        }
    }

    public void scheduleShowSoftInput() {
        final android.view.inputmethod.InputMethodManager imm = getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
        if (imm.isActive(this)) {
            // This means that ImeAwareEditText is already connected to the IME.
            // InputMethodManager#showSoftInput() is guaranteed to pass client-side focus check.
            mHasPendingShowSoftInputRequest = false;
            removeCallbacks(mRunShowSoftInputIfNecessary);
            imm.showSoftInput(this, 0);
            return;
        }
        // Otherwise, InputMethodManager#showSoftInput() should be deferred after
        // onCreateInputConnection().
        mHasPendingShowSoftInputRequest = true;
    }
}

