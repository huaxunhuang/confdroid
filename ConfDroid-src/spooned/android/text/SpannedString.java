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
 * This is the class for text whose content and markup are immutable.
 * For mutable markup, see {@link SpannableString}; for mutable text,
 * see {@link SpannableStringBuilder}.
 */
public final class SpannedString extends android.text.SpannableStringInternal implements android.text.GetChars , android.text.Spanned , java.lang.CharSequence {
    public SpannedString(java.lang.CharSequence source) {
        super(source, 0, source.length());
    }

    private SpannedString(java.lang.CharSequence source, int start, int end) {
        super(source, start, end);
    }

    public java.lang.CharSequence subSequence(int start, int end) {
        return new android.text.SpannedString(this, start, end);
    }

    public static android.text.SpannedString valueOf(java.lang.CharSequence source) {
        if (source instanceof android.text.SpannedString) {
            return ((android.text.SpannedString) (source));
        } else {
            return new android.text.SpannedString(source);
        }
    }
}

