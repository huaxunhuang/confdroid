/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.textclassifier.intent;


/**
 *
 *
 * @unknown 
 */
public interface ClassificationIntentFactory {
    /**
     * Return a list of LabeledIntent from the classification result.
     */
    java.util.List<android.view.textclassifier.intent.LabeledIntent> create(android.content.Context context, java.lang.String text, boolean foreignText, @android.annotation.Nullable
    java.time.Instant referenceTime, @android.annotation.Nullable
    com.google.android.textclassifier.AnnotatorModel.ClassificationResult classification);

    /**
     * Inserts translate action to the list if it is a foreign text.
     */
    static void insertTranslateAction(java.util.List<android.view.textclassifier.intent.LabeledIntent> actions, android.content.Context context, java.lang.String text) {
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.translate), null, context.getString(com.android.internal.R.string.translate_desc), null, // TODO: Probably better to introduce a "translate" scheme instead of
        // using EXTRA_TEXT.
        new android.content.Intent(android.content.Intent.ACTION_TRANSLATE).putExtra(android.content.Intent.EXTRA_TEXT, text), text.hashCode()));
    }
}

