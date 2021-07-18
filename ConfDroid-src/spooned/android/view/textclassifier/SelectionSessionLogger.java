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
 * A helper for logging selection session events.
 *
 * @unknown 
 */
public final class SelectionSessionLogger {
    private static final java.lang.String LOG_TAG = "SelectionSessionLogger";

    static final java.lang.String CLASSIFIER_ID = "androidtc";

    private static final int START_EVENT_DELTA = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SINCE_START;

    private static final int PREV_EVENT_DELTA = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SINCE_PREVIOUS;

    private static final int INDEX = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SESSION_INDEX;

    private static final int WIDGET_TYPE = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_WIDGET_TYPE;

    private static final int WIDGET_VERSION = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_WIDGET_VERSION;

    private static final int MODEL_NAME = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_TEXTCLASSIFIER_MODEL;

    private static final int ENTITY_TYPE = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_ENTITY_TYPE;

    private static final int SMART_START = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SMART_RANGE_START;

    private static final int SMART_END = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SMART_RANGE_END;

    private static final int EVENT_START = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_RANGE_START;

    private static final int EVENT_END = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_RANGE_END;

    private static final int SESSION_ID = com.android.internal.logging.nano.MetricsProto.MetricsEvent.FIELD_SELECTION_SESSION_ID;

    private static final java.lang.String ZERO = "0";

    private static final java.lang.String UNKNOWN = "unknown";

    private final com.android.internal.logging.MetricsLogger mMetricsLogger;

    public SelectionSessionLogger() {
        mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    }

    @com.android.internal.annotations.VisibleForTesting
    public SelectionSessionLogger(@android.annotation.NonNull
    com.android.internal.logging.MetricsLogger metricsLogger) {
        mMetricsLogger = com.android.internal.util.Preconditions.checkNotNull(metricsLogger);
    }

    /**
     * Emits a selection event to the logs.
     */
    public void writeEvent(@android.annotation.NonNull
    android.view.textclassifier.SelectionEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        final android.metrics.LogMaker log = new android.metrics.LogMaker(com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_SESSION).setType(android.view.textclassifier.SelectionSessionLogger.getLogType(event)).setSubtype(android.view.textclassifier.SelectionSessionLogger.getLogSubType(event)).setPackageName(event.getPackageName()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.START_EVENT_DELTA, event.getDurationSinceSessionStart()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.PREV_EVENT_DELTA, event.getDurationSincePreviousEvent()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.INDEX, event.getEventIndex()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.WIDGET_TYPE, event.getWidgetType()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.WIDGET_VERSION, event.getWidgetVersion()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.ENTITY_TYPE, event.getEntityType()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.EVENT_START, event.getStart()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.EVENT_END, event.getEnd());
        if (android.view.textclassifier.SelectionSessionLogger.isPlatformLocalTextClassifierSmartSelection(event.getResultId())) {
            // Ensure result id and smart indices are only set for events with smart selection from
            // the platform's textclassifier.
            log.addTaggedData(android.view.textclassifier.SelectionSessionLogger.MODEL_NAME, android.view.textclassifier.SelectionSessionLogger.SignatureParser.getModelName(event.getResultId())).addTaggedData(android.view.textclassifier.SelectionSessionLogger.SMART_START, event.getSmartStart()).addTaggedData(android.view.textclassifier.SelectionSessionLogger.SMART_END, event.getSmartEnd());
        }
        if (event.getSessionId() != null) {
            log.addTaggedData(android.view.textclassifier.SelectionSessionLogger.SESSION_ID, event.getSessionId().flattenToString());
        }
        mMetricsLogger.write(log);
        android.view.textclassifier.SelectionSessionLogger.debugLog(log);
    }

    private static int getLogType(android.view.textclassifier.SelectionEvent event) {
        switch (event.getEventType()) {
            case android.view.textclassifier.SelectionEvent.ACTION_OVERTYPE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE;
            case android.view.textclassifier.SelectionEvent.ACTION_COPY :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_COPY;
            case android.view.textclassifier.SelectionEvent.ACTION_PASTE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_PASTE;
            case android.view.textclassifier.SelectionEvent.ACTION_CUT :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_CUT;
            case android.view.textclassifier.SelectionEvent.ACTION_SHARE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SHARE;
            case android.view.textclassifier.SelectionEvent.ACTION_SMART_SHARE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
            case android.view.textclassifier.SelectionEvent.ACTION_DRAG :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG;
            case android.view.textclassifier.SelectionEvent.ACTION_ABANDON :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_ABANDON;
            case android.view.textclassifier.SelectionEvent.ACTION_OTHER :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OTHER;
            case android.view.textclassifier.SelectionEvent.ACTION_SELECT_ALL :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
            case android.view.textclassifier.SelectionEvent.ACTION_RESET :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET;
            case android.view.textclassifier.SelectionEvent.EVENT_SELECTION_STARTED :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_START;
            case android.view.textclassifier.SelectionEvent.EVENT_SELECTION_MODIFIED :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_MODIFY;
            case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_SINGLE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
            case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_MULTI :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
            case android.view.textclassifier.SelectionEvent.EVENT_AUTO_SELECTION :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
            default :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.VIEW_UNKNOWN;
        }
    }

    private static int getLogSubType(android.view.textclassifier.SelectionEvent event) {
        switch (event.getInvocationMethod()) {
            case android.view.textclassifier.SelectionEvent.INVOCATION_MANUAL :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_INVOCATION_MANUAL;
            case android.view.textclassifier.SelectionEvent.INVOCATION_LINK :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_INVOCATION_LINK;
            default :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_INVOCATION_UNKNOWN;
        }
    }

    private static java.lang.String getLogTypeString(int logType) {
        switch (logType) {
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE :
                return "OVERTYPE";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_COPY :
                return "COPY";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_PASTE :
                return "PASTE";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_CUT :
                return "CUT";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SHARE :
                return "SHARE";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE :
                return "SMART_SHARE";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG :
                return "DRAG";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_ABANDON :
                return "ABANDON";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OTHER :
                return "OTHER";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL :
                return "SELECT_ALL";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET :
                return "RESET";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_START :
                return "SELECTION_STARTED";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_MODIFY :
                return "SELECTION_MODIFIED";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE :
                return "SMART_SELECTION_SINGLE";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI :
                return "SMART_SELECTION_MULTI";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO :
                return "AUTO_SELECTION";
            default :
                return android.view.textclassifier.SelectionSessionLogger.UNKNOWN;
        }
    }

    private static java.lang.String getLogSubTypeString(int logSubType) {
        switch (logSubType) {
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_INVOCATION_MANUAL :
                return "MANUAL";
            case com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_INVOCATION_LINK :
                return "LINK";
            default :
                return android.view.textclassifier.SelectionSessionLogger.UNKNOWN;
        }
    }

    static boolean isPlatformLocalTextClassifierSmartSelection(java.lang.String signature) {
        return android.view.textclassifier.SelectionSessionLogger.CLASSIFIER_ID.equals(android.view.textclassifier.SelectionSessionLogger.SignatureParser.getClassifierId(signature));
    }

    private static void debugLog(android.metrics.LogMaker log) {
        if (!android.view.textclassifier.Log.ENABLE_FULL_LOGGING) {
            return;
        }
        final java.lang.String widgetType = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.WIDGET_TYPE), android.view.textclassifier.SelectionSessionLogger.UNKNOWN);
        final java.lang.String widgetVersion = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.WIDGET_VERSION), "");
        final java.lang.String widget = (widgetVersion.isEmpty()) ? widgetType : (widgetType + "-") + widgetVersion;
        final int index = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.INDEX), android.view.textclassifier.SelectionSessionLogger.ZERO));
        if (log.getType() == com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_START) {
            java.lang.String sessionId = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.SESSION_ID), "");
            sessionId = sessionId.substring(sessionId.lastIndexOf("-") + 1);
            android.view.textclassifier.Log.d(android.view.textclassifier.SelectionSessionLogger.LOG_TAG, java.lang.String.format("New selection session: %s (%s)", widget, sessionId));
        }
        final java.lang.String model = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.MODEL_NAME), android.view.textclassifier.SelectionSessionLogger.UNKNOWN);
        final java.lang.String entity = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.ENTITY_TYPE), android.view.textclassifier.SelectionSessionLogger.UNKNOWN);
        final java.lang.String type = android.view.textclassifier.SelectionSessionLogger.getLogTypeString(log.getType());
        final java.lang.String subType = android.view.textclassifier.SelectionSessionLogger.getLogSubTypeString(log.getSubtype());
        final int smartStart = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.SMART_START), android.view.textclassifier.SelectionSessionLogger.ZERO));
        final int smartEnd = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.SMART_END), android.view.textclassifier.SelectionSessionLogger.ZERO));
        final int eventStart = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.EVENT_START), android.view.textclassifier.SelectionSessionLogger.ZERO));
        final int eventEnd = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.SelectionSessionLogger.EVENT_END), android.view.textclassifier.SelectionSessionLogger.ZERO));
        android.view.textclassifier.Log.v(android.view.textclassifier.SelectionSessionLogger.LOG_TAG, java.lang.String.format(java.util.Locale.US, "%2d: %s/%s/%s, range=%d,%d - smart_range=%d,%d (%s/%s)", index, type, subType, entity, eventStart, eventEnd, smartStart, smartEnd, widget, model));
    }

    /**
     * Returns a token iterator for tokenizing text for logging purposes.
     */
    public static java.text.BreakIterator getTokenIterator(@android.annotation.NonNull
    java.util.Locale locale) {
        return java.text.BreakIterator.getWordInstance(com.android.internal.util.Preconditions.checkNotNull(locale));
    }

    /**
     * Creates a string id that may be used to identify a TextClassifier result.
     */
    public static java.lang.String createId(java.lang.String text, int start, int end, android.content.Context context, int modelVersion, java.util.List<java.util.Locale> locales) {
        com.android.internal.util.Preconditions.checkNotNull(text);
        com.android.internal.util.Preconditions.checkNotNull(context);
        com.android.internal.util.Preconditions.checkNotNull(locales);
        final java.util.StringJoiner localesJoiner = new java.util.StringJoiner(",");
        for (java.util.Locale locale : locales) {
            localesJoiner.add(locale.toLanguageTag());
        }
        final java.lang.String modelName = java.lang.String.format(java.util.Locale.US, "%s_v%d", localesJoiner.toString(), modelVersion);
        final int hash = java.util.Objects.hash(text, start, end, context.getPackageName());
        return android.view.textclassifier.SelectionSessionLogger.SignatureParser.createSignature(android.view.textclassifier.SelectionSessionLogger.CLASSIFIER_ID, modelName, hash);
    }

    /**
     * Helper for creating and parsing string ids for
     * {@link android.view.textclassifier.TextClassifierImpl}.
     */
    @com.android.internal.annotations.VisibleForTesting
    public static final class SignatureParser {
        static java.lang.String createSignature(java.lang.String classifierId, java.lang.String modelName, int hash) {
            return java.lang.String.format(java.util.Locale.US, "%s|%s|%d", classifierId, modelName, hash);
        }

        static java.lang.String getClassifierId(@android.annotation.Nullable
        java.lang.String signature) {
            if (signature == null) {
                return "";
            }
            final int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return "";
        }

        static java.lang.String getModelName(@android.annotation.Nullable
        java.lang.String signature) {
            if (signature == null) {
                return "";
            }
            final int start = signature.indexOf("|") + 1;
            final int end = signature.indexOf("|", start);
            if ((start >= 1) && (end >= start)) {
                return signature.substring(start, end);
            }
            return "";
        }

        static int getHash(@android.annotation.Nullable
        java.lang.String signature) {
            if (signature == null) {
                return 0;
            }
            final int index1 = signature.indexOf("|");
            final int index2 = signature.indexOf("|", index1);
            if (index2 > 0) {
                return java.lang.Integer.parseInt(signature.substring(index2));
            }
            return 0;
        }
    }
}

