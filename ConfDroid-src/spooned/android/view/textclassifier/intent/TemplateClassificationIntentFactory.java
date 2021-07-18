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
 * Creates intents based on {@link RemoteActionTemplate} objects for a ClassificationResult.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class TemplateClassificationIntentFactory implements android.view.textclassifier.intent.ClassificationIntentFactory {
    private static final java.lang.String TAG = android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG;

    private final android.view.textclassifier.intent.TemplateIntentFactory mTemplateIntentFactory;

    private final android.view.textclassifier.intent.ClassificationIntentFactory mFallback;

    public TemplateClassificationIntentFactory(android.view.textclassifier.intent.TemplateIntentFactory templateIntentFactory, android.view.textclassifier.intent.ClassificationIntentFactory fallback) {
        mTemplateIntentFactory = com.android.internal.util.Preconditions.checkNotNull(templateIntentFactory);
        mFallback = com.android.internal.util.Preconditions.checkNotNull(fallback);
    }

    /**
     * Returns a list of {@link LabeledIntent}
     * that are constructed from the classification result.
     */
    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.view.textclassifier.intent.LabeledIntent> create(android.content.Context context, java.lang.String text, boolean foreignText, @android.annotation.Nullable
    java.time.Instant referenceTime, @android.annotation.Nullable
    com.google.android.textclassifier.AnnotatorModel.ClassificationResult classification) {
        if (classification == null) {
            return java.util.Collections.emptyList();
        }
        com.google.android.textclassifier.RemoteActionTemplate[] remoteActionTemplates = classification.getRemoteActionTemplates();
        if (remoteActionTemplates == null) {
            // RemoteActionTemplate is missing, fallback.
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateClassificationIntentFactory.TAG, "RemoteActionTemplate is missing, fallback to" + " LegacyClassificationIntentFactory.");
            return mFallback.create(context, text, foreignText, referenceTime, classification);
        }
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> labeledIntents = mTemplateIntentFactory.create(remoteActionTemplates);
        if (foreignText) {
            android.view.textclassifier.intent.ClassificationIntentFactory.insertTranslateAction(labeledIntents, context, text.trim());
        }
        return labeledIntents;
    }
}

