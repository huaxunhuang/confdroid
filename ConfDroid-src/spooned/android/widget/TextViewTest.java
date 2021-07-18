/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * TextViewTest tests {@link TextView}.
 */
public class TextViewTest extends android.test.AndroidTestCase {
    @android.test.suitebuilder.annotation.SmallTest
    public void testArray() throws java.lang.Exception {
        android.widget.TextView tv = new android.widget.TextView(mContext);
        char[] c = new char[]{ 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!' };
        tv.setText(c, 1, 4);
        java.lang.CharSequence oldText = tv.getText();
        tv.setText(c, 4, 5);
        java.lang.CharSequence newText = tv.getText();
        assertTrue(newText == oldText);
        assertEquals(5, newText.length());
        assertEquals('o', newText.charAt(0));
        assertEquals("o Wor", newText.toString());
        assertEquals(" Wo", newText.subSequence(1, 4));
        char[] c2 = new char[7];
        ((android.text.GetChars) (newText)).getChars(1, 4, c2, 2);
        assertEquals('\u0000', c2[1]);
        assertEquals(' ', c2[2]);
        assertEquals('W', c2[3]);
        assertEquals('o', c2[4]);
        assertEquals('\u0000', c2[5]);
    }

    public void testProcessTextActivityResultNonEditable() {
        android.widget.TextView tv = new android.widget.TextView(mContext);
        java.lang.CharSequence originalText = "This is some text.";
        tv.setText(originalText, android.widget.TextView.BufferType.SPANNABLE);
        assertEquals(originalText, tv.getText().toString());
        tv.setTextIsSelectable(true);
        android.text.Selection.setSelection(((android.text.Spannable) (tv.getText())), 0, tv.getText().length());
        java.lang.CharSequence newText = "Text is replaced.";
        android.content.Intent data = new android.content.Intent();
        data.putExtra(android.content.Intent.EXTRA_PROCESS_TEXT, newText);
        tv.onActivityResult(android.widget.TextView.PROCESS_TEXT_REQUEST_CODE, Activity.RESULT_OK, data);
        // This is a TextView, which can't be modified. Hence no change should have been made.
        assertEquals(originalText, tv.getText().toString());
    }

    public void testProcessTextActivityResultEditable() {
        android.widget.EditText tv = new android.widget.EditText(mContext);
        java.lang.CharSequence originalText = "This is some text.";
        tv.setText(originalText, android.widget.TextView.BufferType.SPANNABLE);
        assertEquals(originalText, tv.getText().toString());
        tv.setTextIsSelectable(true);
        android.text.Selection.setSelection(tv.getText(), 0, tv.getText().length());
        java.lang.CharSequence newText = "Text is replaced.";
        android.content.Intent data = new android.content.Intent();
        data.putExtra(android.content.Intent.EXTRA_PROCESS_TEXT, newText);
        tv.onActivityResult(android.widget.TextView.PROCESS_TEXT_REQUEST_CODE, Activity.RESULT_OK, data);
        assertEquals(newText, tv.getText().toString());
    }

    public void testProcessTextActivityResultCancel() {
        android.widget.EditText tv = new android.widget.EditText(mContext);
        java.lang.CharSequence originalText = "This is some text.";
        tv.setText(originalText, android.widget.TextView.BufferType.SPANNABLE);
        assertEquals(originalText, tv.getText().toString());
        tv.setTextIsSelectable(true);
        android.text.Selection.setSelection(tv.getText(), 0, tv.getText().length());
        java.lang.CharSequence newText = "Text is replaced.";
        android.content.Intent data = new android.content.Intent();
        data.putExtra(android.content.Intent.EXTRA_PROCESS_TEXT, newText);
        tv.onActivityResult(android.widget.TextView.PROCESS_TEXT_REQUEST_CODE, Activity.RESULT_CANCELED, data);
        assertEquals(originalText, tv.getText().toString());
    }

    public void testProcessTextActivityNoData() {
        android.widget.EditText tv = new android.widget.EditText(mContext);
        java.lang.CharSequence originalText = "This is some text.";
        tv.setText(originalText, android.widget.TextView.BufferType.SPANNABLE);
        assertEquals(originalText, tv.getText().toString());
        tv.setTextIsSelectable(true);
        android.text.Selection.setSelection(tv.getText(), 0, tv.getText().length());
        tv.onActivityResult(android.widget.TextView.PROCESS_TEXT_REQUEST_CODE, Activity.RESULT_OK, null);
        assertEquals(originalText, tv.getText().toString());
    }
}

