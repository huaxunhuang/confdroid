/**
 * Copyright (C) 2018 The Android Open Source Project
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


@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
@androidx.test.filters.LargeTest
public class TextClassifierPerfTest {
    /**
     * Request contains meaning text, rather than garbled text.
     */
    private static final int ACTUAL_REQUEST = 0;

    private static final java.lang.String RANDOM_CHAR_SET = "abcdefghijklmnopqrstuvwxyz0123456789";

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.runners.Parameterized.Parameters(name = "size{0}")
    public static java.util.Collection<java.lang.Object[]> data() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ android.view.textclassifier.TextClassifierPerfTest.ACTUAL_REQUEST }, new java.lang.Object[]{ 10 }, new java.lang.Object[]{ 100 }, new java.lang.Object[]{ 1000 } });
    }

    private android.view.textclassifier.TextClassifier mTextClassifier;

    private final int mSize;

    public TextClassifierPerfTest(int size) {
        mSize = size;
    }

    @org.junit.Before
    public void setUp() {
        android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        android.view.textclassifier.TextClassificationManager textClassificationManager = context.getSystemService(android.view.textclassifier.TextClassificationManager.class);
        mTextClassifier = textClassificationManager.getTextClassifier(android.view.textclassifier.TextClassifier.LOCAL);
    }

    @org.junit.Test
    public void testSuggestConversationActions() {
        java.lang.String text = (mSize == android.view.textclassifier.TextClassifierPerfTest.ACTUAL_REQUEST) ? "Where are you?" : android.view.textclassifier.TextClassifierPerfTest.generateRandomString(mSize);
        android.view.textclassifier.ConversationActions.Request request = android.view.textclassifier.TextClassifierPerfTest.createConversationActionsRequest(text);
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            mTextClassifier.suggestConversationActions(request);
        } 
    }

    @org.junit.Test
    public void testDetectLanguage() {
        java.lang.String text = (mSize == android.view.textclassifier.TextClassifierPerfTest.ACTUAL_REQUEST) ? "これは日本語のテキストです" : android.view.textclassifier.TextClassifierPerfTest.generateRandomString(mSize);
        android.view.textclassifier.TextLanguage.Request request = android.view.textclassifier.TextClassifierPerfTest.createTextLanguageRequest(text);
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            mTextClassifier.detectLanguage(request);
        } 
    }

    private static android.view.textclassifier.ConversationActions.Request createConversationActionsRequest(java.lang.CharSequence text) {
        android.view.textclassifier.ConversationActions.Message message = new android.view.textclassifier.ConversationActions.Message.Builder(android.view.textclassifier.ConversationActions.Message.PERSON_USER_OTHERS).setText(text).build();
        return new android.view.textclassifier.ConversationActions.Request.Builder(java.util.Collections.singletonList(message)).build();
    }

    private static android.view.textclassifier.TextLanguage.Request createTextLanguageRequest(java.lang.CharSequence text) {
        return new android.view.textclassifier.TextLanguage.Request.Builder(text).build();
    }

    private static java.lang.String generateRandomString(int length) {
        java.util.Random random = new java.util.Random();
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(android.view.textclassifier.TextClassifierPerfTest.RANDOM_CHAR_SET.length());
            stringBuilder.append(android.view.textclassifier.TextClassifierPerfTest.RANDOM_CHAR_SET.charAt(index));
        }
        return stringBuilder.toString();
    }
}

