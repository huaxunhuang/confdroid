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
package android.view.textclassifier;


/**
 * Utility class for inserting and retrieving data in TextClassifier request/response extras.
 *
 * @unknown 
 */
// TODO: Make this a TestApi for CTS testing.
public final class ExtrasUtils {
    // Keys for response objects.
    private static final java.lang.String SERIALIZED_ENTITIES_DATA = "serialized-entities-data";

    private static final java.lang.String ENTITIES_EXTRAS = "entities-extras";

    private static final java.lang.String ACTION_INTENT = "action-intent";

    private static final java.lang.String ACTIONS_INTENTS = "actions-intents";

    private static final java.lang.String FOREIGN_LANGUAGE = "foreign-language";

    private static final java.lang.String ENTITY_TYPE = "entity-type";

    private static final java.lang.String SCORE = "score";

    private static final java.lang.String MODEL_VERSION = "model-version";

    private static final java.lang.String MODEL_NAME = "model-name";

    private static final java.lang.String TEXT_LANGUAGES = "text-languages";

    private static final java.lang.String ENTITIES = "entities";

    // Keys for request objects.
    private static final java.lang.String IS_SERIALIZED_ENTITY_DATA_ENABLED = "is-serialized-entity-data-enabled";

    private ExtrasUtils() {
    }

    /**
     * Bundles and returns foreign language detection information for TextClassifier responses.
     */
    static android.os.Bundle createForeignLanguageExtra(java.lang.String language, float score, int modelVersion) {
        final android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString(android.view.textclassifier.ExtrasUtils.ENTITY_TYPE, language);
        bundle.putFloat(android.view.textclassifier.ExtrasUtils.SCORE, score);
        bundle.putInt(android.view.textclassifier.ExtrasUtils.MODEL_VERSION, modelVersion);
        bundle.putString(android.view.textclassifier.ExtrasUtils.MODEL_NAME, "langId_v" + modelVersion);
        return bundle;
    }

    /**
     * Stores {@code extra} as foreign language information in TextClassifier response object's
     * extras {@code container}.
     *
     * @see #getForeignLanguageExtra(TextClassification)
     */
    static void putForeignLanguageExtra(android.os.Bundle container, android.os.Bundle extra) {
        container.putParcelable(android.view.textclassifier.ExtrasUtils.FOREIGN_LANGUAGE, extra);
    }

    /**
     * Returns foreign language detection information contained in the TextClassification object.
     * responses.
     *
     * @see #putForeignLanguageExtra(Bundle, Bundle)
     */
    @android.annotation.Nullable
    public static android.os.Bundle getForeignLanguageExtra(@android.annotation.Nullable
    android.view.textclassifier.TextClassification classification) {
        if (classification == null) {
            return null;
        }
        return classification.getExtras().getBundle(android.view.textclassifier.ExtrasUtils.FOREIGN_LANGUAGE);
    }

    /**
     *
     *
     * @see #getTopLanguage(Intent)
     */
    static void putTopLanguageScores(android.os.Bundle container, android.view.textclassifier.EntityConfidence languageScores) {
        final int maxSize = java.lang.Math.min(3, languageScores.getEntities().size());
        final java.lang.String[] languages = languageScores.getEntities().subList(0, maxSize).toArray(new java.lang.String[0]);
        final float[] scores = new float[languages.length];
        for (int i = 0; i < languages.length; i++) {
            scores[i] = languageScores.getConfidenceScore(languages[i]);
        }
        container.putStringArray(android.view.textclassifier.ExtrasUtils.ENTITY_TYPE, languages);
        container.putFloatArray(android.view.textclassifier.ExtrasUtils.SCORE, scores);
    }

    /**
     *
     *
     * @see #putTopLanguageScores(Bundle, EntityConfidence)
     */
    @android.annotation.Nullable
    public static android.icu.util.ULocale getTopLanguage(@android.annotation.Nullable
    android.content.Intent intent) {
        if (intent == null) {
            return null;
        }
        final android.os.Bundle tcBundle = intent.getBundleExtra(android.view.textclassifier.TextClassifier.EXTRA_FROM_TEXT_CLASSIFIER);
        if (tcBundle == null) {
            return null;
        }
        final android.os.Bundle textLanguagesExtra = tcBundle.getBundle(android.view.textclassifier.ExtrasUtils.TEXT_LANGUAGES);
        if (textLanguagesExtra == null) {
            return null;
        }
        final java.lang.String[] languages = textLanguagesExtra.getStringArray(android.view.textclassifier.ExtrasUtils.ENTITY_TYPE);
        final float[] scores = textLanguagesExtra.getFloatArray(android.view.textclassifier.ExtrasUtils.SCORE);
        if ((((languages == null) || (scores == null)) || (languages.length == 0)) || (languages.length != scores.length)) {
            return null;
        }
        int highestScoringIndex = 0;
        for (int i = 1; i < languages.length; i++) {
            if (scores[highestScoringIndex] < scores[i]) {
                highestScoringIndex = i;
            }
        }
        return android.icu.util.ULocale.forLanguageTag(languages[highestScoringIndex]);
    }

    public static void putTextLanguagesExtra(android.os.Bundle container, android.os.Bundle extra) {
        container.putBundle(android.view.textclassifier.ExtrasUtils.TEXT_LANGUAGES, extra);
    }

    /**
     * Stores {@code actionIntents} information in TextClassifier response object's extras
     * {@code container}.
     */
    static void putActionsIntents(android.os.Bundle container, java.util.ArrayList<android.content.Intent> actionsIntents) {
        container.putParcelableArrayList(android.view.textclassifier.ExtrasUtils.ACTIONS_INTENTS, actionsIntents);
    }

    /**
     * Stores {@code actionIntents} information in TextClassifier response object's extras
     * {@code container}.
     */
    public static void putActionIntent(android.os.Bundle container, @android.annotation.Nullable
    android.content.Intent actionIntent) {
        container.putParcelable(android.view.textclassifier.ExtrasUtils.ACTION_INTENT, actionIntent);
    }

    /**
     * Returns {@code actionIntent} information contained in a TextClassifier response object.
     */
    @android.annotation.Nullable
    public static android.content.Intent getActionIntent(android.os.Bundle container) {
        return container.getParcelable(android.view.textclassifier.ExtrasUtils.ACTION_INTENT);
    }

    /**
     * Stores serialized entity data information in TextClassifier response object's extras
     * {@code container}.
     */
    public static void putSerializedEntityData(android.os.Bundle container, @android.annotation.Nullable
    byte[] serializedEntityData) {
        container.putByteArray(android.view.textclassifier.ExtrasUtils.SERIALIZED_ENTITIES_DATA, serializedEntityData);
    }

    /**
     * Returns serialized entity data information contained in a TextClassifier response
     * object.
     */
    @android.annotation.Nullable
    public static byte[] getSerializedEntityData(android.os.Bundle container) {
        return container.getByteArray(android.view.textclassifier.ExtrasUtils.SERIALIZED_ENTITIES_DATA);
    }

    /**
     * Stores {@code entities} information in TextClassifier response object's extras
     * {@code container}.
     *
     * @see {@link #getCopyText(Bundle)}
     */
    public static void putEntitiesExtras(android.os.Bundle container, @android.annotation.Nullable
    android.os.Bundle entitiesExtras) {
        container.putParcelable(android.view.textclassifier.ExtrasUtils.ENTITIES_EXTRAS, entitiesExtras);
    }

    /**
     * Returns {@code entities} information contained in a TextClassifier response object.
     *
     * @see {@link #putEntitiesExtras(Bundle, Bundle)}
     */
    @android.annotation.Nullable
    public static java.lang.String getCopyText(android.os.Bundle container) {
        android.os.Bundle entitiesExtras = container.getParcelable(android.view.textclassifier.ExtrasUtils.ENTITIES_EXTRAS);
        if (entitiesExtras == null) {
            return null;
        }
        return entitiesExtras.getString("text");
    }

    /**
     * Returns {@code actionIntents} information contained in the TextClassification object.
     */
    @android.annotation.Nullable
    public static java.util.ArrayList<android.content.Intent> getActionsIntents(@android.annotation.Nullable
    android.view.textclassifier.TextClassification classification) {
        if (classification == null) {
            return null;
        }
        return classification.getExtras().getParcelableArrayList(android.view.textclassifier.ExtrasUtils.ACTIONS_INTENTS);
    }

    /**
     * Returns the first action found in the {@code classification} object with an intent
     * action string, {@code intentAction}.
     */
    @android.annotation.Nullable
    public static android.app.RemoteAction findAction(@android.annotation.Nullable
    android.view.textclassifier.TextClassification classification, @android.annotation.Nullable
    java.lang.String intentAction) {
        if ((classification == null) || (intentAction == null)) {
            return null;
        }
        final java.util.ArrayList<android.content.Intent> actionIntents = android.view.textclassifier.ExtrasUtils.getActionsIntents(classification);
        if (actionIntents != null) {
            final int size = actionIntents.size();
            for (int i = 0; i < size; i++) {
                final android.content.Intent intent = actionIntents.get(i);
                if ((intent != null) && intentAction.equals(intent.getAction())) {
                    return classification.getActions().get(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns the first "translate" action found in the {@code classification} object.
     */
    @android.annotation.Nullable
    public static android.app.RemoteAction findTranslateAction(@android.annotation.Nullable
    android.view.textclassifier.TextClassification classification) {
        return android.view.textclassifier.ExtrasUtils.findAction(classification, android.content.Intent.ACTION_TRANSLATE);
    }

    /**
     * Returns the entity type contained in the {@code extra}.
     */
    @android.annotation.Nullable
    public static java.lang.String getEntityType(@android.annotation.Nullable
    android.os.Bundle extra) {
        if (extra == null) {
            return null;
        }
        return extra.getString(android.view.textclassifier.ExtrasUtils.ENTITY_TYPE);
    }

    /**
     * Returns the score contained in the {@code extra}.
     */
    @android.annotation.Nullable
    public static float getScore(android.os.Bundle extra) {
        final int defaultValue = -1;
        if (extra == null) {
            return defaultValue;
        }
        return extra.getFloat(android.view.textclassifier.ExtrasUtils.SCORE, defaultValue);
    }

    /**
     * Returns the model name contained in the {@code extra}.
     */
    @android.annotation.Nullable
    public static java.lang.String getModelName(@android.annotation.Nullable
    android.os.Bundle extra) {
        if (extra == null) {
            return null;
        }
        return extra.getString(android.view.textclassifier.ExtrasUtils.MODEL_NAME);
    }

    /**
     * Stores the entities from {@link AnnotatorModel.ClassificationResult} in {@code container}.
     */
    public static void putEntities(android.os.Bundle container, @android.annotation.Nullable
    com.google.android.textclassifier.AnnotatorModel[] classifications) {
        if (com.android.internal.util.ArrayUtils.isEmpty(classifications)) {
            return;
        }
        java.util.ArrayList<android.os.Bundle> entitiesBundle = new java.util.ArrayList<>();
        for (com.google.android.textclassifier.AnnotatorModel.ClassificationResult classification : classifications) {
            if (classification == null) {
                continue;
            }
            android.os.Bundle entityBundle = new android.os.Bundle();
            entityBundle.putString(android.view.textclassifier.ExtrasUtils.ENTITY_TYPE, classification.getCollection());
            entityBundle.putByteArray(android.view.textclassifier.ExtrasUtils.SERIALIZED_ENTITIES_DATA, classification.getSerializedEntityData());
            entitiesBundle.add(entityBundle);
        }
        if (!entitiesBundle.isEmpty()) {
            container.putParcelableArrayList(android.view.textclassifier.ExtrasUtils.ENTITIES, entitiesBundle);
        }
    }

    /**
     * Returns a list of entities contained in the {@code extra}.
     */
    @android.annotation.Nullable
    public static java.util.List<android.os.Bundle> getEntities(android.os.Bundle container) {
        return container.getParcelableArrayList(android.view.textclassifier.ExtrasUtils.ENTITIES);
    }

    /**
     * Whether the annotator should populate serialized entity data into the result object.
     */
    public static boolean isSerializedEntityDataEnabled(android.view.textclassifier.TextLinks.Request request) {
        return request.getExtras().getBoolean(android.view.textclassifier.ExtrasUtils.IS_SERIALIZED_ENTITY_DATA_ENABLED);
    }

    /**
     * To indicate whether the annotator should populate serialized entity data in the result
     * object.
     */
    public static void putIsSerializedEntityDataEnabled(android.os.Bundle bundle, boolean isEnabled) {
        bundle.putBoolean(android.view.textclassifier.ExtrasUtils.IS_SERIALIZED_ENTITY_DATA_ENABLED, isEnabled);
    }
}

