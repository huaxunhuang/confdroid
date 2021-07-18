/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.textclassifier;


/**
 * Java wrapper for SmartSelection native library interface.
 *  This library is used for detecting entities in text.
 */
final class SmartSelection {
    static {
        java.lang.System.loadLibrary("textclassifier");
    }

    /**
     * Hints the classifier that this may be a url.
     */
    static final int HINT_FLAG_URL = 0x1;

    /**
     * Hints the classifier that this may be an email.
     */
    static final int HINT_FLAG_EMAIL = 0x2;

    private final long mCtx;

    /**
     * Creates a new instance of SmartSelect predictor, using the provided model image,
     * given as a file descriptor.
     */
    SmartSelection(int fd) {
        mCtx = android.view.textclassifier.SmartSelection.nativeNew(fd);
    }

    /**
     * Given a string context and current selection, computes the SmartSelection suggestion.
     *
     * The begin and end are character indices into the context UTF8 string. selectionBegin is the
     * character index where the selection begins, and selectionEnd is the index of one character
     * past the selection span.
     *
     * The return value is an array of two ints: suggested selection beginning and end, with the
     * same semantics as the input selectionBeginning and selectionEnd.
     */
    public int[] suggest(java.lang.String context, int selectionBegin, int selectionEnd) {
        return android.view.textclassifier.SmartSelection.nativeSuggest(mCtx, context, selectionBegin, selectionEnd);
    }

    /**
     * Given a string context and current selection, classifies the type of the selected text.
     *
     * The begin and end params are character indices in the context string.
     *
     * Returns an array of ClassificationResult objects with the probability
     * scores for different collections.
     */
    public android.view.textclassifier.SmartSelection.ClassificationResult[] classifyText(java.lang.String context, int selectionBegin, int selectionEnd, int hintFlags) {
        return android.view.textclassifier.SmartSelection.nativeClassifyText(mCtx, context, selectionBegin, selectionEnd, hintFlags);
    }

    /**
     * Frees up the allocated memory.
     */
    public void close() {
        android.view.textclassifier.SmartSelection.nativeClose(mCtx);
    }

    /**
     * Returns the language of the model.
     */
    public static java.lang.String getLanguage(int fd) {
        return android.view.textclassifier.SmartSelection.nativeGetLanguage(fd);
    }

    /**
     * Returns the version of the model.
     */
    public static int getVersion(int fd) {
        return android.view.textclassifier.SmartSelection.nativeGetVersion(fd);
    }

    private static native long nativeNew(int fd);

    private static native int[] nativeSuggest(long context, java.lang.String text, int selectionBegin, int selectionEnd);

    private static native android.view.textclassifier.SmartSelection.ClassificationResult[] nativeClassifyText(long context, java.lang.String text, int selectionBegin, int selectionEnd, int hintFlags);

    private static native void nativeClose(long context);

    private static native java.lang.String nativeGetLanguage(int fd);

    private static native int nativeGetVersion(int fd);

    /**
     * Classification result for classifyText method.
     */
    static final class ClassificationResult {
        final java.lang.String mCollection;

        /**
         * float range: 0 - 1
         */
        final float mScore;

        ClassificationResult(java.lang.String collection, float score) {
            mCollection = collection;
            mScore = score;
        }
    }
}

