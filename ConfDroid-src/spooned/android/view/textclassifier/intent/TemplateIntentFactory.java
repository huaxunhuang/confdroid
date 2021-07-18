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
 * Creates intents based on {@link RemoteActionTemplate} objects.
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class TemplateIntentFactory {
    private static final java.lang.String TAG = android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG;

    /**
     * Constructs and returns a list of {@link LabeledIntent} based on the given templates.
     */
    @android.annotation.Nullable
    public java.util.List<android.view.textclassifier.intent.LabeledIntent> create(@android.annotation.NonNull
    com.google.android.textclassifier.RemoteActionTemplate[] remoteActionTemplates) {
        if (remoteActionTemplates.length == 0) {
            return new java.util.ArrayList<>();
        }
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> labeledIntents = new java.util.ArrayList<>();
        for (com.google.android.textclassifier.RemoteActionTemplate remoteActionTemplate : remoteActionTemplates) {
            if (!android.view.textclassifier.intent.TemplateIntentFactory.isValidTemplate(remoteActionTemplate)) {
                android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate skipped.");
                continue;
            }
            labeledIntents.add(new android.view.textclassifier.intent.LabeledIntent(remoteActionTemplate.titleWithoutEntity, remoteActionTemplate.titleWithEntity, remoteActionTemplate.description, remoteActionTemplate.descriptionWithAppName, android.view.textclassifier.intent.TemplateIntentFactory.createIntent(remoteActionTemplate), remoteActionTemplate.requestCode == null ? android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE : remoteActionTemplate.requestCode));
        }
        return labeledIntents;
    }

    private static boolean isValidTemplate(@android.annotation.Nullable
    com.google.android.textclassifier.RemoteActionTemplate remoteActionTemplate) {
        if (remoteActionTemplate == null) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate: is null");
            return false;
        }
        if (android.text.TextUtils.isEmpty(remoteActionTemplate.titleWithEntity) && android.text.TextUtils.isEmpty(remoteActionTemplate.titleWithoutEntity)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate: title is null");
            return false;
        }
        if (android.text.TextUtils.isEmpty(remoteActionTemplate.description)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate: description is null");
            return false;
        }
        if (!android.text.TextUtils.isEmpty(remoteActionTemplate.packageName)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate: package name is set");
            return false;
        }
        if (android.text.TextUtils.isEmpty(remoteActionTemplate.action)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Invalid RemoteActionTemplate: intent action not set");
            return false;
        }
        return true;
    }

    private static android.content.Intent createIntent(com.google.android.textclassifier.RemoteActionTemplate remoteActionTemplate) {
        final android.content.Intent intent = new android.content.Intent(remoteActionTemplate.action);
        final android.net.Uri uri = (android.text.TextUtils.isEmpty(remoteActionTemplate.data)) ? null : android.net.Uri.parse(remoteActionTemplate.data).normalizeScheme();
        final java.lang.String type = (android.text.TextUtils.isEmpty(remoteActionTemplate.type)) ? null : android.content.Intent.normalizeMimeType(remoteActionTemplate.type);
        intent.setDataAndType(uri, type);
        intent.setFlags(remoteActionTemplate.flags == null ? 0 : remoteActionTemplate.flags);
        if (remoteActionTemplate.category != null) {
            for (java.lang.String category : remoteActionTemplate.category) {
                if (category != null) {
                    intent.addCategory(category);
                }
            }
        }
        intent.putExtras(android.view.textclassifier.intent.TemplateIntentFactory.nameVariantsToBundle(remoteActionTemplate.extras));
        return intent;
    }

    /**
     * Converts an array of {@link NamedVariant} to a Bundle and returns it.
     */
    public static android.os.Bundle nameVariantsToBundle(@android.annotation.Nullable
    com.google.android.textclassifier.NamedVariant[] namedVariants) {
        if (namedVariants == null) {
            return android.os.Bundle.EMPTY;
        }
        android.os.Bundle bundle = new android.os.Bundle();
        for (com.google.android.textclassifier.NamedVariant namedVariant : namedVariants) {
            if (namedVariant == null) {
                continue;
            }
            switch (namedVariant.getType()) {
                case com.google.android.textclassifier.NamedVariant.TYPE_INT :
                    bundle.putInt(namedVariant.getName(), namedVariant.getInt());
                    break;
                case com.google.android.textclassifier.NamedVariant.TYPE_LONG :
                    bundle.putLong(namedVariant.getName(), namedVariant.getLong());
                    break;
                case com.google.android.textclassifier.NamedVariant.TYPE_FLOAT :
                    bundle.putFloat(namedVariant.getName(), namedVariant.getFloat());
                    break;
                case com.google.android.textclassifier.NamedVariant.TYPE_DOUBLE :
                    bundle.putDouble(namedVariant.getName(), namedVariant.getDouble());
                    break;
                case com.google.android.textclassifier.NamedVariant.TYPE_BOOL :
                    bundle.putBoolean(namedVariant.getName(), namedVariant.getBool());
                    break;
                case com.google.android.textclassifier.NamedVariant.TYPE_STRING :
                    bundle.putString(namedVariant.getName(), namedVariant.getString());
                    break;
                default :
                    android.view.textclassifier.Log.w(android.view.textclassifier.intent.TemplateIntentFactory.TAG, "Unsupported type found in nameVariantsToBundle : " + namedVariant.getType());
            }
        }
        return bundle;
    }
}

