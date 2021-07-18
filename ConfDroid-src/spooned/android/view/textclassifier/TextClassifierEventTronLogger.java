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


/**
 * Log {@link TextClassifierEvent} by using Tron, only support language detection and
 * conversation actions.
 *
 * @unknown 
 */
public final class TextClassifierEventTronLogger {
    private static final java.lang.String TAG = "TCEventTronLogger";

    private final com.android.internal.logging.MetricsLogger mMetricsLogger;

    public TextClassifierEventTronLogger() {
        this(new com.android.internal.logging.MetricsLogger());
    }

    @com.android.internal.annotations.VisibleForTesting
    public TextClassifierEventTronLogger(com.android.internal.logging.MetricsLogger metricsLogger) {
        mMetricsLogger = com.android.internal.util.Preconditions.checkNotNull(metricsLogger);
    }

    /**
     * Emits a text classifier event to the logs.
     */
    public void writeEvent(android.view.textclassifier.TextClassifierEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        int category = android.view.textclassifier.TextClassifierEventTronLogger.getCategory(event);
        if (category == (-1)) {
            android.view.textclassifier.Log.w(android.view.textclassifier.TextClassifierEventTronLogger.TAG, "Unknown category: " + event.getEventCategory());
            return;
        }
        final android.metrics.LogMaker log = new android.metrics.LogMaker(category).setSubtype(android.view.textclassifier.TextClassifierEventTronLogger.getLogType(event)).addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SESSION_ID, event.getResultId()).addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXTCLASSIFIER_MODEL, android.view.textclassifier.TextClassifierEventTronLogger.getModelName(event));
        if (event.getScores().length >= 1) {
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SCORE, event.getScores()[0]);
        }
        java.lang.String[] entityTypes = event.getEntityTypes();
        // The old logger does not support a field of list type, and thus workaround by store them
        // in three separate fields. This is not an issue with the new logger.
        if (entityTypes.length >= 1) {
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_FIRST_ENTITY_TYPE, entityTypes[0]);
        }
        if (entityTypes.length >= 2) {
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SECOND_ENTITY_TYPE, entityTypes[1]);
        }
        if (entityTypes.length >= 3) {
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_THIRD_ENTITY_TYPE, entityTypes[2]);
        }
        android.view.textclassifier.TextClassificationContext eventContext = event.getEventContext();
        if (eventContext != null) {
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_TYPE, eventContext.getWidgetType());
            log.addTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_VERSION, eventContext.getWidgetVersion());
            log.setPackageName(eventContext.getPackageName());
        }
        mMetricsLogger.write(log);
        debugLog(log);
    }

    private static java.lang.String getModelName(android.view.textclassifier.TextClassifierEvent event) {
        if (event.getModelName() != null) {
            return event.getModelName();
        }
        return android.view.textclassifier.SelectionSessionLogger.SignatureParser.getModelName(event.getResultId());
    }

    private static int getCategory(android.view.textclassifier.TextClassifierEvent event) {
        switch (event.getEventCategory()) {
            case android.view.textclassifier.TextClassifierEvent.CATEGORY_CONVERSATION_ACTIONS :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.CONVERSATION_ACTIONS;
            case android.view.textclassifier.TextClassifierEvent.CATEGORY_LANGUAGE_DETECTION :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.LANGUAGE_DETECTION;
        }
        return -1;
    }

    private static int getLogType(android.view.textclassifier.TextClassifierEvent event) {
        switch (event.getEventType()) {
            case android.view.textclassifier.TextClassifierEvent.TYPE_SMART_ACTION :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
            case android.view.textclassifier.TextClassifierEvent.TYPE_ACTIONS_SHOWN :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_ACTIONS_SHOWN;
            case android.view.textclassifier.TextClassifierEvent.TYPE_MANUAL_REPLY :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_MANUAL_REPLY;
            case android.view.textclassifier.TextClassifierEvent.TYPE_ACTIONS_GENERATED :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_ACTIONS_GENERATED;
            default :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.VIEW_UNKNOWN;
        }
    }

    private java.lang.String toCategoryName(int category) {
        switch (category) {
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.CONVERSATION_ACTIONS :
                return "conversation_actions";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.LANGUAGE_DETECTION :
                return "language_detection";
        }
        return "unknown";
    }

    private java.lang.String toEventName(int logType) {
        switch (logType) {
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE :
                return "smart_share";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_ACTIONS_SHOWN :
                return "actions_shown";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_MANUAL_REPLY :
                return "manual_reply";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_CLASSIFIER_ACTIONS_GENERATED :
                return "actions_generated";
        }
        return "unknown";
    }

    private void debugLog(android.metrics.LogMaker log) {
        if (!android.view.textclassifier.Log.ENABLE_FULL_LOGGING) {
            return;
        }
        final java.lang.String id = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SESSION_ID));
        final java.lang.String categoryName = toCategoryName(log.getCategory());
        final java.lang.String eventName = toEventName(log.getSubtype());
        final java.lang.String widgetType = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_TYPE));
        final java.lang.String widgetVersion = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_VERSION));
        final java.lang.String model = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXTCLASSIFIER_MODEL));
        final java.lang.String firstEntityType = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_FIRST_ENTITY_TYPE));
        final java.lang.String secondEntityType = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SECOND_ENTITY_TYPE));
        final java.lang.String thirdEntityType = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_THIRD_ENTITY_TYPE));
        final java.lang.String score = java.lang.String.valueOf(log.getTaggedData(com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SCORE));
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("writeEvent: ");
        builder.append("id=").append(id);
        builder.append(", category=").append(categoryName);
        builder.append(", eventName=").append(eventName);
        builder.append(", widgetType=").append(widgetType);
        builder.append(", widgetVersion=").append(widgetVersion);
        builder.append(", model=").append(model);
        builder.append(", firstEntityType=").append(firstEntityType);
        builder.append(", secondEntityType=").append(secondEntityType);
        builder.append(", thirdEntityType=").append(thirdEntityType);
        builder.append(", score=").append(score);
        android.view.textclassifier.Log.v(android.view.textclassifier.TextClassifierEventTronLogger.TAG, builder.toString());
    }
}

