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
package android.text;


/**
 * This is the class for text whose content is immutable but to which
 * markup objects can be attached and detached.
 * For mutable text, see {@link SpannableStringBuilder}.
 */
public class SpannableString extends android.text.SpannableStringInternal implements android.text.GetChars , android.text.Spannable , java.lang.CharSequence {
    public SpannableString(java.lang.CharSequence source) {
        super(source, 0, source.length());
    }

    private SpannableString(java.lang.CharSequence source, int start, int end) {
        super(source, start, end);
    }

    public static android.text.SpannableString valueOf(java.lang.CharSequence source) {
        if (source instanceof android.text.SpannableString) {
            return ((android.text.SpannableString) (source));
        } else {
            return new android.text.SpannableString(source);
        }
    }

    public void setSpan(java.lang.Object what, int start, int end, int flags) {
        super.setSpan(what, start, end, flags);
    }

    public void removeSpan(java.lang.Object what) {
        super.removeSpan(what);
    }

    public final java.lang.CharSequence subSequence(int start, int end) {
        return new android.text.SpannableString(this, start, end);
    }
}

