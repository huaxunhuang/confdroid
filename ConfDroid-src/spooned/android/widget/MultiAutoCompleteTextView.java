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
package android.widget;


/**
 * An editable text view, extending {@link AutoCompleteTextView}, that
 * can show completion suggestions for the substring of the text where
 * the user is typing instead of necessarily for the entire thing.
 * <p>
 * You must provide a {@link Tokenizer} to distinguish the
 * various substrings.
 *
 * <p>The following code snippet shows how to create a text view which suggests
 * various countries names while the user is typing:</p>
 *
 * <pre class="prettyprint">
 * public class CountriesActivity extends Activity {
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.autocomplete_7);
 *
 *         ArrayAdapter&lt;String&gt; adapter = new ArrayAdapter&lt;String&gt;(this,
 *                 android.R.layout.simple_dropdown_item_1line, COUNTRIES);
 *         MultiAutoCompleteTextView textView = findViewById(R.id.edit);
 *         textView.setAdapter(adapter);
 *         textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
 *     }
 *
 *     private static final String[] COUNTRIES = new String[] {
 *         "Belgium", "France", "Italy", "Germany", "Spain"
 *     };
 * }</pre>
 */
public class MultiAutoCompleteTextView extends android.widget.AutoCompleteTextView {
    private android.widget.MultiAutoCompleteTextView.Tokenizer mTokenizer;

    public MultiAutoCompleteTextView(android.content.Context context) {
        this(context, null);
    }

    public MultiAutoCompleteTextView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.autoCompleteTextViewStyle);
    }

    public MultiAutoCompleteTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiAutoCompleteTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* package */
    void finishInit() {
    }

    /**
     * Sets the Tokenizer that will be used to determine the relevant
     * range of the text where the user is typing.
     */
    public void setTokenizer(android.widget.MultiAutoCompleteTextView.Tokenizer t) {
        mTokenizer = t;
    }

    /**
     * Instead of filtering on the entire contents of the edit box,
     * this subclass method filters on the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd}
     * if the length of that range meets or exceeds {@link #getThreshold}.
     */
    @java.lang.Override
    protected void performFiltering(java.lang.CharSequence text, int keyCode) {
        if (enoughToFilter()) {
            int end = getSelectionEnd();
            int start = mTokenizer.findTokenStart(text, end);
            performFiltering(text, start, end, keyCode);
        } else {
            dismissDropDown();
            android.widget.Filter f = getFilter();
            if (f != null) {
                f.filter(null);
            }
        }
    }

    /**
     * Instead of filtering whenever the total length of the text
     * exceeds the threshhold, this subclass filters only when the
     * length of the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd}
     * meets or exceeds {@link #getThreshold}.
     */
    @java.lang.Override
    public boolean enoughToFilter() {
        android.text.Editable text = getText();
        int end = getSelectionEnd();
        if ((end < 0) || (mTokenizer == null)) {
            return false;
        }
        int start = mTokenizer.findTokenStart(text, end);
        if ((end - start) >= getThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Instead of validating the entire text, this subclass method validates
     * each token of the text individually.  Empty tokens are removed.
     */
    @java.lang.Override
    public void performValidation() {
        android.widget.AutoCompleteTextView.Validator v = getValidator();
        if ((v == null) || (mTokenizer == null)) {
            return;
        }
        android.text.Editable e = getText();
        int i = getText().length();
        while (i > 0) {
            int start = mTokenizer.findTokenStart(e, i);
            int end = mTokenizer.findTokenEnd(e, start);
            java.lang.CharSequence sub = e.subSequence(start, end);
            if (android.text.TextUtils.isEmpty(sub)) {
                e.replace(start, i, "");
            } else
                if (!v.isValid(sub)) {
                    e.replace(start, i, mTokenizer.terminateToken(v.fixText(sub)));
                }

            i = start;
        } 
    }

    /**
     * <p>Starts filtering the content of the drop down list. The filtering
     * pattern is the specified range of text from the edit box. Subclasses may
     * override this method to filter with a different pattern, for
     * instance a smaller substring of <code>text</code>.</p>
     */
    protected void performFiltering(java.lang.CharSequence text, int start, int end, int keyCode) {
        getFilter().filter(text.subSequence(start, end), this);
    }

    /**
     * <p>Performs the text completion by replacing the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd} by the
     * the result of passing <code>text</code> through
     * {@link Tokenizer#terminateToken}.
     * In addition, the replaced region will be marked as an AutoText
     * substition so that if the user immediately presses DEL, the
     * completion will be undone.
     * Subclasses may override this method to do some different
     * insertion of the content into the edit box.</p>
     *
     * @param text
     * 		the selected suggestion in the drop down list
     */
    @java.lang.Override
    protected void replaceText(java.lang.CharSequence text) {
        clearComposingText();
        int end = getSelectionEnd();
        int start = mTokenizer.findTokenStart(getText(), end);
        android.text.Editable editable = getText();
        java.lang.String original = android.text.TextUtils.substring(editable, start, end);
        android.text.method.QwertyKeyListener.markAsReplaced(editable, start, end, original);
        editable.replace(start, end, mTokenizer.terminateToken(text));
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.MultiAutoCompleteTextView.class.getName();
    }

    public static interface Tokenizer {
        /**
         * Returns the start of the token that ends at offset
         * <code>cursor</code> within <code>text</code>.
         */
        public int findTokenStart(java.lang.CharSequence text, int cursor);

        /**
         * Returns the end of the token (minus trailing punctuation)
         * that begins at offset <code>cursor</code> within <code>text</code>.
         */
        public int findTokenEnd(java.lang.CharSequence text, int cursor);

        /**
         * Returns <code>text</code>, modified, if necessary, to ensure that
         * it ends with a token terminator (for example a space or comma).
         */
        public java.lang.CharSequence terminateToken(java.lang.CharSequence text);
    }

    /**
     * This simple Tokenizer can be used for lists where the items are
     * separated by a comma and one or more spaces.
     */
    public static class CommaTokenizer implements android.widget.MultiAutoCompleteTextView.Tokenizer {
        public int findTokenStart(java.lang.CharSequence text, int cursor) {
            int i = cursor;
            while ((i > 0) && (text.charAt(i - 1) != ',')) {
                i--;
            } 
            while ((i < cursor) && (text.charAt(i) == ' ')) {
                i++;
            } 
            return i;
        }

        public int findTokenEnd(java.lang.CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len) {
                if (text.charAt(i) == ',') {
                    return i;
                } else {
                    i++;
                }
            } 
            return len;
        }

        public java.lang.CharSequence terminateToken(java.lang.CharSequence text) {
            int i = text.length();
            while ((i > 0) && (text.charAt(i - 1) == ' ')) {
                i--;
            } 
            if ((i > 0) && (text.charAt(i - 1) == ',')) {
                return text;
            } else {
                if (text instanceof android.text.Spanned) {
                    android.text.SpannableString sp = new android.text.SpannableString(text + ", ");
                    android.text.TextUtils.copySpansFrom(((android.text.Spanned) (text)), 0, text.length(), java.lang.Object.class, sp, 0);
                    return sp;
                } else {
                    return text + ", ";
                }
            }
        }
    }
}

