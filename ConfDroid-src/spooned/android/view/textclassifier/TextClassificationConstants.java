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
 * TextClassifier specific settings.
 * This is encoded as a key=value list, separated by commas.
 * <p>
 * Example of setting the values for testing.
 * <p>
 * <pre>
 * adb shell settings put global text_classifier_constants \
 *      model_dark_launch_enabled=true,smart_selection_enabled=true, \
 *      entity_list_default=phone:address, \
 *      lang_id_context_settings=20:1.0:0.4
 * </pre>
 * <p>
 * Settings are also available in device config. These take precedence over those in settings
 * global.
 * <p>
 * <pre>
 * adb shell cmd device_config put textclassifier system_textclassifier_enabled true
 * </pre>
 *
 * @see android.provider.Settings.Global.TEXT_CLASSIFIER_CONSTANTS
 * @see android.provider.DeviceConfig.NAMESPACE_TEXTCLASSIFIER
 * @unknown 
 */
// TODO: Rename to TextClassifierSettings.
public final class TextClassificationConstants {
    /**
     * Whether the smart linkify feature is enabled.
     */
    private static final java.lang.String SMART_LINKIFY_ENABLED = "smart_linkify_enabled";

    /**
     * Whether SystemTextClassifier is enabled.
     */
    private static final java.lang.String SYSTEM_TEXT_CLASSIFIER_ENABLED = "system_textclassifier_enabled";

    /**
     * Whether TextClassifierImpl is enabled.
     */
    private static final java.lang.String LOCAL_TEXT_CLASSIFIER_ENABLED = "local_textclassifier_enabled";

    /**
     * Enable smart selection without a visible UI changes.
     */
    private static final java.lang.String MODEL_DARK_LAUNCH_ENABLED = "model_dark_launch_enabled";

    /**
     * Whether the smart selection feature is enabled.
     */
    private static final java.lang.String SMART_SELECTION_ENABLED = "smart_selection_enabled";

    /**
     * Whether the smart text share feature is enabled.
     */
    private static final java.lang.String SMART_TEXT_SHARE_ENABLED = "smart_text_share_enabled";

    /**
     * Whether animation for smart selection is enabled.
     */
    private static final java.lang.String SMART_SELECT_ANIMATION_ENABLED = "smart_select_animation_enabled";

    /**
     * Max length of text that suggestSelection can accept.
     */
    private static final java.lang.String SUGGEST_SELECTION_MAX_RANGE_LENGTH = "suggest_selection_max_range_length";

    /**
     * Max length of text that classifyText can accept.
     */
    private static final java.lang.String CLASSIFY_TEXT_MAX_RANGE_LENGTH = "classify_text_max_range_length";

    /**
     * Max length of text that generateLinks can accept.
     */
    private static final java.lang.String GENERATE_LINKS_MAX_TEXT_LENGTH = "generate_links_max_text_length";

    /**
     * Sampling rate for generateLinks logging.
     */
    private static final java.lang.String GENERATE_LINKS_LOG_SAMPLE_RATE = "generate_links_log_sample_rate";

    /**
     * A colon(:) separated string that specifies the default entities types for
     * generateLinks when hint is not given.
     */
    private static final java.lang.String ENTITY_LIST_DEFAULT = "entity_list_default";

    /**
     * A colon(:) separated string that specifies the default entities types for
     * generateLinks when the text is in a not editable UI widget.
     */
    private static final java.lang.String ENTITY_LIST_NOT_EDITABLE = "entity_list_not_editable";

    /**
     * A colon(:) separated string that specifies the default entities types for
     * generateLinks when the text is in an editable UI widget.
     */
    private static final java.lang.String ENTITY_LIST_EDITABLE = "entity_list_editable";

    /**
     * A colon(:) separated string that specifies the default action types for
     * suggestConversationActions when the suggestions are used in an app.
     */
    private static final java.lang.String IN_APP_CONVERSATION_ACTION_TYPES_DEFAULT = "in_app_conversation_action_types_default";

    /**
     * A colon(:) separated string that specifies the default action types for
     * suggestConversationActions when the suggestions are used in a notification.
     */
    private static final java.lang.String NOTIFICATION_CONVERSATION_ACTION_TYPES_DEFAULT = "notification_conversation_action_types_default";

    /**
     * Threshold to accept a suggested language from LangID model.
     */
    private static final java.lang.String LANG_ID_THRESHOLD_OVERRIDE = "lang_id_threshold_override";

    /**
     * Whether to enable {@link android.view.textclassifier.TemplateIntentFactory}.
     */
    private static final java.lang.String TEMPLATE_INTENT_FACTORY_ENABLED = "template_intent_factory_enabled";

    /**
     * Whether to enable "translate" action in classifyText.
     */
    private static final java.lang.String TRANSLATE_IN_CLASSIFICATION_ENABLED = "translate_in_classification_enabled";

    /**
     * Whether to detect the languages of the text in request by using langId for the native
     * model.
     */
    private static final java.lang.String DETECT_LANGUAGES_FROM_TEXT_ENABLED = "detect_languages_from_text_enabled";

    /**
     * A colon(:) separated string that specifies the configuration to use when including
     * surrounding context text in language detection queries.
     * <p>
     * Format= minimumTextSize<int>:penalizeRatio<float>:textScoreRatio<float>
     * <p>
     * e.g. 20:1.0:0.4
     * <p>
     * Accept all text lengths with minimumTextSize=0
     * <p>
     * Reject all text less than minimumTextSize with penalizeRatio=0
     *
     * @see {@code TextClassifierImpl#detectLanguages(String, int, int)} for reference.
     */
    private static final java.lang.String LANG_ID_CONTEXT_SETTINGS = "lang_id_context_settings";

    private static final boolean LOCAL_TEXT_CLASSIFIER_ENABLED_DEFAULT = true;

    private static final boolean SYSTEM_TEXT_CLASSIFIER_ENABLED_DEFAULT = true;

    private static final boolean MODEL_DARK_LAUNCH_ENABLED_DEFAULT = false;

    private static final boolean SMART_SELECTION_ENABLED_DEFAULT = true;

    private static final boolean SMART_TEXT_SHARE_ENABLED_DEFAULT = true;

    private static final boolean SMART_LINKIFY_ENABLED_DEFAULT = true;

    private static final boolean SMART_SELECT_ANIMATION_ENABLED_DEFAULT = true;

    private static final int SUGGEST_SELECTION_MAX_RANGE_LENGTH_DEFAULT = 10 * 1000;

    private static final int CLASSIFY_TEXT_MAX_RANGE_LENGTH_DEFAULT = 10 * 1000;

    private static final int GENERATE_LINKS_MAX_TEXT_LENGTH_DEFAULT = 100 * 1000;

    private static final int GENERATE_LINKS_LOG_SAMPLE_RATE_DEFAULT = 100;

    private static final java.util.List<java.lang.String> ENTITY_LIST_DEFAULT_VALUE = java.util.Arrays.asList(android.view.textclassifier.TextClassifier.TYPE_ADDRESS, android.view.textclassifier.TextClassifier.TYPE_EMAIL, android.view.textclassifier.TextClassifier.TYPE_PHONE, android.view.textclassifier.TextClassifier.TYPE_URL, android.view.textclassifier.TextClassifier.TYPE_DATE, android.view.textclassifier.TextClassifier.TYPE_DATE_TIME, android.view.textclassifier.TextClassifier.TYPE_FLIGHT_NUMBER);

    private static final java.util.List<java.lang.String> CONVERSATION_ACTIONS_TYPES_DEFAULT_VALUES = java.util.Arrays.asList(android.view.textclassifier.ConversationAction.TYPE_TEXT_REPLY, android.view.textclassifier.ConversationAction.TYPE_CREATE_REMINDER, android.view.textclassifier.ConversationAction.TYPE_CALL_PHONE, android.view.textclassifier.ConversationAction.TYPE_OPEN_URL, android.view.textclassifier.ConversationAction.TYPE_SEND_EMAIL, android.view.textclassifier.ConversationAction.TYPE_SEND_SMS, android.view.textclassifier.ConversationAction.TYPE_TRACK_FLIGHT, android.view.textclassifier.ConversationAction.TYPE_VIEW_CALENDAR, android.view.textclassifier.ConversationAction.TYPE_VIEW_MAP, android.view.textclassifier.ConversationAction.TYPE_ADD_CONTACT, android.view.textclassifier.ConversationAction.TYPE_COPY);

    /**
     * < 0  : Not set. Use value from LangId model.
     * 0 - 1: Override value in LangId model.
     *
     * @see EntityConfidence
     */
    private static final float LANG_ID_THRESHOLD_OVERRIDE_DEFAULT = -1.0F;

    private static final boolean TEMPLATE_INTENT_FACTORY_ENABLED_DEFAULT = true;

    private static final boolean TRANSLATE_IN_CLASSIFICATION_ENABLED_DEFAULT = true;

    private static final boolean DETECT_LANGUAGES_FROM_TEXT_ENABLED_DEFAULT = true;

    private static final float[] LANG_ID_CONTEXT_SETTINGS_DEFAULT = new float[]{ 20.0F, 1.0F, 0.4F };

    private final android.view.textclassifier.ConfigParser mConfigParser;

    public TextClassificationConstants(java.util.function.Supplier<java.lang.String> legacySettingsSupplier) {
        mConfigParser = new android.view.textclassifier.ConfigParser(legacySettingsSupplier);
    }

    public boolean isLocalTextClassifierEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.LOCAL_TEXT_CLASSIFIER_ENABLED, android.view.textclassifier.TextClassificationConstants.LOCAL_TEXT_CLASSIFIER_ENABLED_DEFAULT);
    }

    public boolean isSystemTextClassifierEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.SYSTEM_TEXT_CLASSIFIER_ENABLED, android.view.textclassifier.TextClassificationConstants.SYSTEM_TEXT_CLASSIFIER_ENABLED_DEFAULT);
    }

    public boolean isModelDarkLaunchEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.MODEL_DARK_LAUNCH_ENABLED, android.view.textclassifier.TextClassificationConstants.MODEL_DARK_LAUNCH_ENABLED_DEFAULT);
    }

    public boolean isSmartSelectionEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.SMART_SELECTION_ENABLED, android.view.textclassifier.TextClassificationConstants.SMART_SELECTION_ENABLED_DEFAULT);
    }

    public boolean isSmartTextShareEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.SMART_TEXT_SHARE_ENABLED, android.view.textclassifier.TextClassificationConstants.SMART_TEXT_SHARE_ENABLED_DEFAULT);
    }

    public boolean isSmartLinkifyEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.SMART_LINKIFY_ENABLED, android.view.textclassifier.TextClassificationConstants.SMART_LINKIFY_ENABLED_DEFAULT);
    }

    public boolean isSmartSelectionAnimationEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.SMART_SELECT_ANIMATION_ENABLED, android.view.textclassifier.TextClassificationConstants.SMART_SELECT_ANIMATION_ENABLED_DEFAULT);
    }

    public int getSuggestSelectionMaxRangeLength() {
        return mConfigParser.getInt(android.view.textclassifier.TextClassificationConstants.SUGGEST_SELECTION_MAX_RANGE_LENGTH, android.view.textclassifier.TextClassificationConstants.SUGGEST_SELECTION_MAX_RANGE_LENGTH_DEFAULT);
    }

    public int getClassifyTextMaxRangeLength() {
        return mConfigParser.getInt(android.view.textclassifier.TextClassificationConstants.CLASSIFY_TEXT_MAX_RANGE_LENGTH, android.view.textclassifier.TextClassificationConstants.CLASSIFY_TEXT_MAX_RANGE_LENGTH_DEFAULT);
    }

    public int getGenerateLinksMaxTextLength() {
        return mConfigParser.getInt(android.view.textclassifier.TextClassificationConstants.GENERATE_LINKS_MAX_TEXT_LENGTH, android.view.textclassifier.TextClassificationConstants.GENERATE_LINKS_MAX_TEXT_LENGTH_DEFAULT);
    }

    public int getGenerateLinksLogSampleRate() {
        return mConfigParser.getInt(android.view.textclassifier.TextClassificationConstants.GENERATE_LINKS_LOG_SAMPLE_RATE, android.view.textclassifier.TextClassificationConstants.GENERATE_LINKS_LOG_SAMPLE_RATE_DEFAULT);
    }

    public java.util.List<java.lang.String> getEntityListDefault() {
        return mConfigParser.getStringList(android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_DEFAULT, android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_DEFAULT_VALUE);
    }

    public java.util.List<java.lang.String> getEntityListNotEditable() {
        return mConfigParser.getStringList(android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_NOT_EDITABLE, android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_DEFAULT_VALUE);
    }

    public java.util.List<java.lang.String> getEntityListEditable() {
        return mConfigParser.getStringList(android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_EDITABLE, android.view.textclassifier.TextClassificationConstants.ENTITY_LIST_DEFAULT_VALUE);
    }

    public java.util.List<java.lang.String> getInAppConversationActionTypes() {
        return mConfigParser.getStringList(android.view.textclassifier.TextClassificationConstants.IN_APP_CONVERSATION_ACTION_TYPES_DEFAULT, android.view.textclassifier.TextClassificationConstants.CONVERSATION_ACTIONS_TYPES_DEFAULT_VALUES);
    }

    public java.util.List<java.lang.String> getNotificationConversationActionTypes() {
        return mConfigParser.getStringList(android.view.textclassifier.TextClassificationConstants.NOTIFICATION_CONVERSATION_ACTION_TYPES_DEFAULT, android.view.textclassifier.TextClassificationConstants.CONVERSATION_ACTIONS_TYPES_DEFAULT_VALUES);
    }

    public float getLangIdThresholdOverride() {
        return mConfigParser.getFloat(android.view.textclassifier.TextClassificationConstants.LANG_ID_THRESHOLD_OVERRIDE, android.view.textclassifier.TextClassificationConstants.LANG_ID_THRESHOLD_OVERRIDE_DEFAULT);
    }

    public boolean isTemplateIntentFactoryEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.TEMPLATE_INTENT_FACTORY_ENABLED, android.view.textclassifier.TextClassificationConstants.TEMPLATE_INTENT_FACTORY_ENABLED_DEFAULT);
    }

    public boolean isTranslateInClassificationEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.TRANSLATE_IN_CLASSIFICATION_ENABLED, android.view.textclassifier.TextClassificationConstants.TRANSLATE_IN_CLASSIFICATION_ENABLED_DEFAULT);
    }

    public boolean isDetectLanguagesFromTextEnabled() {
        return mConfigParser.getBoolean(android.view.textclassifier.TextClassificationConstants.DETECT_LANGUAGES_FROM_TEXT_ENABLED, android.view.textclassifier.TextClassificationConstants.DETECT_LANGUAGES_FROM_TEXT_ENABLED_DEFAULT);
    }

    public float[] getLangIdContextSettings() {
        return mConfigParser.getFloatArray(android.view.textclassifier.TextClassificationConstants.LANG_ID_CONTEXT_SETTINGS, android.view.textclassifier.TextClassificationConstants.LANG_ID_CONTEXT_SETTINGS_DEFAULT);
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("TextClassificationConstants:");
        pw.increaseIndent();
        pw.printPair("classify_text_max_range_length", getClassifyTextMaxRangeLength()).println();
        pw.printPair("detect_language_from_text_enabled", isDetectLanguagesFromTextEnabled()).println();
        pw.printPair("entity_list_default", getEntityListDefault()).println();
        pw.printPair("entity_list_editable", getEntityListEditable()).println();
        pw.printPair("entity_list_not_editable", getEntityListNotEditable()).println();
        pw.printPair("generate_links_log_sample_rate", getGenerateLinksLogSampleRate()).println();
        pw.printPair("generate_links_max_text_length", getGenerateLinksMaxTextLength()).println();
        pw.printPair("in_app_conversation_action_types_default", getInAppConversationActionTypes()).println();
        pw.printPair("lang_id_context_settings", java.util.Arrays.toString(getLangIdContextSettings())).println();
        pw.printPair("lang_id_threshold_override", getLangIdThresholdOverride()).println();
        pw.printPair("local_textclassifier_enabled", isLocalTextClassifierEnabled()).println();
        pw.printPair("model_dark_launch_enabled", isModelDarkLaunchEnabled()).println();
        pw.printPair("notification_conversation_action_types_default", getNotificationConversationActionTypes()).println();
        pw.printPair("smart_linkify_enabled", isSmartLinkifyEnabled()).println();
        pw.printPair("smart_select_animation_enabled", isSmartSelectionAnimationEnabled()).println();
        pw.printPair("smart_selection_enabled", isSmartSelectionEnabled()).println();
        pw.printPair("smart_text_share_enabled", isSmartTextShareEnabled()).println();
        pw.printPair("suggest_selection_max_range_length", getSuggestSelectionMaxRangeLength()).println();
        pw.printPair("system_textclassifier_enabled", isSystemTextClassifierEnabled()).println();
        pw.printPair("template_intent_factory_enabled", isTemplateIntentFactoryEnabled()).println();
        pw.printPair("translate_in_classification_enabled", isTranslateInClassificationEnabled()).println();
        pw.decreaseIndent();
    }
}

