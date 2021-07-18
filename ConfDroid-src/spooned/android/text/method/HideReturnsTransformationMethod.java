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
package android.text.method;


/**
 * This transformation method causes any carriage return characters (\r)
 * to be hidden by displaying them as zero-width non-breaking space
 * characters (\uFEFF).
 */
public class HideReturnsTransformationMethod extends android.text.method.ReplacementTransformationMethod {
    private static char[] ORIGINAL = new char[]{ '\r' };

    private static char[] REPLACEMENT = new char[]{ '\ufeff' };

    /**
     * The character to be replaced is \r.
     */
    protected char[] getOriginal() {
        return android.text.method.HideReturnsTransformationMethod.ORIGINAL;
    }

    /**
     * The character that \r is replaced with is \uFEFF.
     */
    protected char[] getReplacement() {
        return android.text.method.HideReturnsTransformationMethod.REPLACEMENT;
    }

    public static android.text.method.HideReturnsTransformationMethod getInstance() {
        if (android.text.method.HideReturnsTransformationMethod.sInstance != null)
            return android.text.method.HideReturnsTransformationMethod.sInstance;

        android.text.method.HideReturnsTransformationMethod.sInstance = new android.text.method.HideReturnsTransformationMethod();
        return android.text.method.HideReturnsTransformationMethod.sInstance;
    }

    private static android.text.method.HideReturnsTransformationMethod sInstance;
}

