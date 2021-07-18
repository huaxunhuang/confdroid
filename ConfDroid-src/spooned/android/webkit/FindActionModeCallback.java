/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.webkit;


/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class FindActionModeCallback implements android.text.TextWatcher , android.view.ActionMode.Callback , android.view.View.OnClickListener , android.webkit.WebView.FindListener {
    private android.view.View mCustomView;

    private android.widget.EditText mEditText;

    private android.widget.TextView mMatches;

    private android.webkit.WebView mWebView;

    private android.view.inputmethod.InputMethodManager mInput;

    private android.content.res.Resources mResources;

    private boolean mMatchesFound;

    private int mNumberOfMatches;

    private int mActiveMatchIndex;

    private android.view.ActionMode mActionMode;

    public FindActionModeCallback(android.content.Context context) {
        mCustomView = android.view.LayoutInflater.from(context).inflate(com.android.internal.R.layout.webview_find, null);
        mEditText = ((android.widget.EditText) (mCustomView.findViewById(com.android.internal.R.id.edit)));
        mEditText.setCustomSelectionActionModeCallback(new android.webkit.FindActionModeCallback.NoAction());
        mEditText.setOnClickListener(this);
        setText("");
        mMatches = ((android.widget.TextView) (mCustomView.findViewById(com.android.internal.R.id.matches)));
        mInput = context.getSystemService(android.view.inputmethod.InputMethodManager.class);
        mResources = context.getResources();
    }

    public void finish() {
        mActionMode.finish();
    }

    /* Place text in the text field so it can be searched for.  Need to press
    the find next or find previous button to find all of the matches.
     */
    public void setText(java.lang.String text) {
        mEditText.setText(text);
        android.text.Spannable span = ((android.text.Spannable) (mEditText.getText()));
        int length = span.length();
        // Ideally, we would like to set the selection to the whole field,
        // but this brings up the Text selection CAB, which dismisses this
        // one.
        android.text.Selection.setSelection(span, length, length);
        // Necessary each time we set the text, so that this will watch
        // changes to it.
        span.setSpan(this, 0, length, android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mMatchesFound = false;
    }

    /* Set the WebView to search.  Must be non null. */
    public void setWebView(android.webkit.WebView webView) {
        if (null == webView) {
            throw new java.lang.AssertionError("WebView supplied to " + "FindActionModeCallback cannot be null");
        }
        mWebView = webView;
        mWebView.setFindDialogFindListener(this);
    }

    @java.lang.Override
    public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
        if (isDoneCounting) {
            updateMatchCount(activeMatchOrdinal, numberOfMatches, numberOfMatches == 0);
        }
    }

    /* Move the highlight to the next match.
    @param next If true, find the next match further down in the document.
                If false, find the previous match, up in the document.
     */
    private void findNext(boolean next) {
        if (mWebView == null) {
            throw new java.lang.AssertionError("No WebView for FindActionModeCallback::findNext");
        }
        if (!mMatchesFound) {
            findAll();
            return;
        }
        if (0 == mNumberOfMatches) {
            // There are no matches, so moving to the next match will not do
            // anything.
            return;
        }
        mWebView.findNext(next);
        updateMatchesString();
    }

    /* Highlight all the instances of the string from mEditText in mWebView. */
    public void findAll() {
        if (mWebView == null) {
            throw new java.lang.AssertionError("No WebView for FindActionModeCallback::findAll");
        }
        java.lang.CharSequence find = mEditText.getText();
        if (0 == find.length()) {
            mWebView.clearMatches();
            mMatches.setVisibility(android.view.View.GONE);
            mMatchesFound = false;
            mWebView.findAll(null);
        } else {
            mMatchesFound = true;
            mMatches.setVisibility(android.view.View.INVISIBLE);
            mNumberOfMatches = 0;
            mWebView.findAllAsync(find.toString());
        }
    }

    public void showSoftInput() {
        if (mEditText.requestFocus()) {
            mInput.showSoftInput(mEditText, 0);
        }
    }

    public void updateMatchCount(int matchIndex, int matchCount, boolean isEmptyFind) {
        if (!isEmptyFind) {
            mNumberOfMatches = matchCount;
            mActiveMatchIndex = matchIndex;
            updateMatchesString();
        } else {
            mMatches.setVisibility(android.view.View.GONE);
            mNumberOfMatches = 0;
        }
    }

    /* Update the string which tells the user how many matches were found, and
    which match is currently highlighted.
     */
    private void updateMatchesString() {
        if (mNumberOfMatches == 0) {
            mMatches.setText(com.android.internal.R.string.no_matches);
        } else {
            mMatches.setText(mResources.getQuantityString(com.android.internal.R.plurals.matches_found, mNumberOfMatches, mActiveMatchIndex + 1, mNumberOfMatches));
        }
        mMatches.setVisibility(android.view.View.VISIBLE);
    }

    // OnClickListener implementation
    @java.lang.Override
    public void onClick(android.view.View v) {
        findNext(true);
    }

    // ActionMode.Callback implementation
    @java.lang.Override
    public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu) {
        if (!mode.isUiFocusable()) {
            // If the action mode we're running in is not focusable the user
            // will not be able to type into the find on page field. This
            // should only come up when we're running in a dialog which is
            // already less than ideal; disable the option for now.
            return false;
        }
        mode.setCustomView(mCustomView);
        mode.getMenuInflater().inflate(com.android.internal.R.menu.webview_find, menu);
        mActionMode = mode;
        android.text.Editable edit = mEditText.getText();
        android.text.Selection.setSelection(edit, edit.length());
        mMatches.setVisibility(android.view.View.GONE);
        mMatchesFound = false;
        mMatches.setText("0");
        mEditText.requestFocus();
        return true;
    }

    @java.lang.Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        mActionMode = null;
        mWebView.notifyFindDialogDismissed();
        mWebView.setFindDialogFindListener(null);
        mInput.hideSoftInputFromWindow(mWebView.getWindowToken(), 0);
    }

    @java.lang.Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu) {
        return false;
    }

    @java.lang.Override
    public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item) {
        if (mWebView == null) {
            throw new java.lang.AssertionError("No WebView for FindActionModeCallback::onActionItemClicked");
        }
        mInput.hideSoftInputFromWindow(mWebView.getWindowToken(), 0);
        switch (item.getItemId()) {
            case com.android.internal.R.id.find_prev :
                findNext(false);
                break;
            case com.android.internal.R.id.find_next :
                findNext(true);
                break;
            default :
                return false;
        }
        return true;
    }

    // TextWatcher implementation
    @java.lang.Override
    public void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after) {
        // Does nothing.  Needed to implement TextWatcher.
    }

    @java.lang.Override
    public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
        findAll();
    }

    @java.lang.Override
    public void afterTextChanged(android.text.Editable s) {
        // Does nothing.  Needed to implement TextWatcher.
    }

    private android.graphics.Rect mGlobalVisibleRect = new android.graphics.Rect();

    private android.graphics.Point mGlobalVisibleOffset = new android.graphics.Point();

    public int getActionModeGlobalBottom() {
        if (mActionMode == null) {
            return 0;
        }
        android.view.View view = ((android.view.View) (mCustomView.getParent()));
        if (view == null) {
            view = mCustomView;
        }
        view.getGlobalVisibleRect(mGlobalVisibleRect, mGlobalVisibleOffset);
        return mGlobalVisibleRect.bottom;
    }

    public static class NoAction implements android.view.ActionMode.Callback {
        @java.lang.Override
        public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            return false;
        }

        @java.lang.Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            return false;
        }

        @java.lang.Override
        public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
        }
    }
}

