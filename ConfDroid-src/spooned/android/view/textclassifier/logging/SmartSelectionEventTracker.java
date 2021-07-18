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
package android.view.textclassifier.logging;


/**
 * A selection event tracker.
 *
 * @unknown 
 */
// TODO: Do not allow any crashes from this class.
public final class SmartSelectionEventTracker {
    private static final java.lang.String LOG_TAG = "SmartSelectEventTracker";

    private static final boolean DEBUG_LOG_ENABLED = true;

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

    private static final java.lang.String TEXTVIEW = "textview";

    private static final java.lang.String EDITTEXT = "edittext";

    private static final java.lang.String UNSELECTABLE_TEXTVIEW = "nosel-textview";

    private static final java.lang.String WEBVIEW = "webview";

    private static final java.lang.String EDIT_WEBVIEW = "edit-webview";

    private static final java.lang.String CUSTOM_TEXTVIEW = "customview";

    private static final java.lang.String CUSTOM_EDITTEXT = "customedit";

    private static final java.lang.String CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";

    private static final java.lang.String UNKNOWN = "unknown";

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.UNSPECIFIED, android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.TEXTVIEW, android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.WEBVIEW, android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.EDITTEXT, android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.EDIT_WEBVIEW })
    public @interface WidgetType {
        int UNSPECIFIED = 0;

        int TEXTVIEW = 1;

        int WEBVIEW = 2;

        int EDITTEXT = 3;

        int EDIT_WEBVIEW = 4;

        int UNSELECTABLE_TEXTVIEW = 5;

        int CUSTOM_TEXTVIEW = 6;

        int CUSTOM_EDITTEXT = 7;

        int CUSTOM_UNSELECTABLE_TEXTVIEW = 8;
    }

    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();

    private final int mWidgetType;

    @android.annotation.Nullable
    private final java.lang.String mWidgetVersion;

    private final android.content.Context mContext;

    @android.annotation.Nullable
    private java.lang.String mSessionId;

    private final int[] mSmartIndices = new int[2];

    private final int[] mPrevIndices = new int[2];

    private int mOrigStart;

    private int mIndex;

    private long mSessionStartTime;

    private long mLastEventTime;

    private boolean mSmartSelectionTriggered;

    private java.lang.String mModelName;

    @android.annotation.UnsupportedAppUsage
    public SmartSelectionEventTracker(@android.annotation.NonNull
    android.content.Context context, @android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType
    int widgetType) {
        mWidgetType = widgetType;
        mWidgetVersion = null;
        mContext = com.android.internal.util.Preconditions.checkNotNull(context);
    }

    public SmartSelectionEventTracker(@android.annotation.NonNull
    android.content.Context context, @android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType
    int widgetType, @android.annotation.Nullable
    java.lang.String widgetVersion) {
        mWidgetType = widgetType;
        mWidgetVersion = widgetVersion;
        mContext = com.android.internal.util.Preconditions.checkNotNull(context);
    }

    /**
     * Logs a selection event.
     *
     * @param event
     * 		the selection event
     */
    @android.annotation.UnsupportedAppUsage
    public void logEvent(@android.annotation.NonNull
    android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        if (((event.mEventType != android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_STARTED) && (mSessionId == null)) && android.view.textclassifier.logging.SmartSelectionEventTracker.DEBUG_LOG_ENABLED) {
            android.util.Log.d(android.view.textclassifier.logging.SmartSelectionEventTracker.LOG_TAG, "Selection session not yet started. Ignoring event");
            return;
        }
        final long now = java.lang.System.currentTimeMillis();
        switch (event.mEventType) {
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_STARTED :
                mSessionId = startNewSession();
                com.android.internal.util.Preconditions.checkArgument(event.mEnd == (event.mStart + 1));
                mOrigStart = event.mStart;
                mSessionStartTime = now;
                break;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_SINGLE :
                // fall through
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_MULTI :
                mSmartSelectionTriggered = true;
                mModelName = getModelName(event);
                mSmartIndices[0] = event.mStart;
                mSmartIndices[1] = event.mEnd;
                break;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_MODIFIED :
                // fall through
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.AUTO_SELECTION :
                if ((mPrevIndices[0] == event.mStart) && (mPrevIndices[1] == event.mEnd)) {
                    // Selection did not change. Ignore event.
                    return;
                }
        }
        writeEvent(event, now);
        if (event.isTerminal()) {
            endSession();
        }
    }

    private void writeEvent(android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent event, long now) {
        final long prevEventDelta = (mLastEventTime == 0) ? 0 : now - mLastEventTime;
        final android.metrics.LogMaker log = new android.metrics.LogMaker(com.android.internal.logging.nano.MetricsProto.MetricsEvent.TEXT_SELECTION_SESSION).setType(android.view.textclassifier.logging.SmartSelectionEventTracker.getLogType(event)).setSubtype(MetricsEvent.TEXT_SELECTION_INVOCATION_MANUAL).setPackageName(mContext.getPackageName()).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.START_EVENT_DELTA, now - mSessionStartTime).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.PREV_EVENT_DELTA, prevEventDelta).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.INDEX, mIndex).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.WIDGET_TYPE, getWidgetTypeName()).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.WIDGET_VERSION, mWidgetVersion).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.MODEL_NAME, mModelName).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.ENTITY_TYPE, event.mEntityType).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SMART_START, getSmartRangeDelta(mSmartIndices[0])).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SMART_END, getSmartRangeDelta(mSmartIndices[1])).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.EVENT_START, getRangeDelta(event.mStart)).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.EVENT_END, getRangeDelta(event.mEnd)).addTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SESSION_ID, mSessionId);
        mMetricsLogger.write(log);
        android.view.textclassifier.logging.SmartSelectionEventTracker.debugLog(log);
        mLastEventTime = now;
        mPrevIndices[0] = event.mStart;
        mPrevIndices[1] = event.mEnd;
        mIndex++;
    }

    private java.lang.String startNewSession() {
        endSession();
        mSessionId = android.view.textclassifier.logging.SmartSelectionEventTracker.createSessionId();
        return mSessionId;
    }

    private void endSession() {
        // Reset fields.
        mOrigStart = 0;
        mSmartIndices[0] = mSmartIndices[1] = 0;
        mPrevIndices[0] = mPrevIndices[1] = 0;
        mIndex = 0;
        mSessionStartTime = 0;
        mLastEventTime = 0;
        mSmartSelectionTriggered = false;
        mModelName = getModelName(null);
        mSessionId = null;
    }

    private static int getLogType(android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent event) {
        switch (event.mEventType) {
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OVERTYPE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.COPY :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_COPY;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.PASTE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_PASTE;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.CUT :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_CUT;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SHARE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SHARE;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SMART_SHARE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.DRAG :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.ABANDON :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_ABANDON;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OTHER :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OTHER;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SELECT_ALL :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.RESET :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_STARTED :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_START;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_MODIFIED :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_MODIFY;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_SINGLE :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_MULTI :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.AUTO_SELECTION :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
            default :
                return com.android.internal.logging.nano.MetricsProto.MetricsEvent.VIEW_UNKNOWN;
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
                return android.view.textclassifier.logging.SmartSelectionEventTracker.UNKNOWN;
        }
    }

    private int getRangeDelta(int offset) {
        return offset - mOrigStart;
    }

    private int getSmartRangeDelta(int offset) {
        return mSmartSelectionTriggered ? getRangeDelta(offset) : 0;
    }

    private java.lang.String getWidgetTypeName() {
        switch (mWidgetType) {
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.TEXTVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.TEXTVIEW;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.WEBVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.WEBVIEW;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.EDITTEXT :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.EDITTEXT;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.EDIT_WEBVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.EDIT_WEBVIEW;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.UNSELECTABLE_TEXTVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.UNSELECTABLE_TEXTVIEW;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.CUSTOM_TEXTVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.CUSTOM_TEXTVIEW;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.CUSTOM_EDITTEXT :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.CUSTOM_EDITTEXT;
            case android.view.textclassifier.logging.SmartSelectionEventTracker.WidgetType.CUSTOM_UNSELECTABLE_TEXTVIEW :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.CUSTOM_UNSELECTABLE_TEXTVIEW;
            default :
                return android.view.textclassifier.logging.SmartSelectionEventTracker.UNKNOWN;
        }
    }

    private java.lang.String getModelName(@android.annotation.Nullable
    android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent event) {
        return event == null ? android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.NO_VERSION_TAG : java.util.Objects.toString(event.mVersionTag, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.NO_VERSION_TAG);
    }

    private static java.lang.String createSessionId() {
        return java.util.UUID.randomUUID().toString();
    }

    private static void debugLog(android.metrics.LogMaker log) {
        if (!android.view.textclassifier.logging.SmartSelectionEventTracker.DEBUG_LOG_ENABLED)
            return;

        final java.lang.String widgetType = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.WIDGET_TYPE), android.view.textclassifier.logging.SmartSelectionEventTracker.UNKNOWN);
        final java.lang.String widgetVersion = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.WIDGET_VERSION), "");
        final java.lang.String widget = (widgetVersion.isEmpty()) ? widgetType : (widgetType + "-") + widgetVersion;
        final int index = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.INDEX), android.view.textclassifier.logging.SmartSelectionEventTracker.ZERO));
        if (log.getType() == com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_START) {
            java.lang.String sessionId = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SESSION_ID), "");
            sessionId = sessionId.substring(sessionId.lastIndexOf("-") + 1);
            android.util.Log.d(android.view.textclassifier.logging.SmartSelectionEventTracker.LOG_TAG, java.lang.String.format("New selection session: %s (%s)", widget, sessionId));
        }
        final java.lang.String model = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.MODEL_NAME), android.view.textclassifier.logging.SmartSelectionEventTracker.UNKNOWN);
        final java.lang.String entity = java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.ENTITY_TYPE), android.view.textclassifier.logging.SmartSelectionEventTracker.UNKNOWN);
        final java.lang.String type = android.view.textclassifier.logging.SmartSelectionEventTracker.getLogTypeString(log.getType());
        final int smartStart = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SMART_START), android.view.textclassifier.logging.SmartSelectionEventTracker.ZERO));
        final int smartEnd = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.SMART_END), android.view.textclassifier.logging.SmartSelectionEventTracker.ZERO));
        final int eventStart = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.EVENT_START), android.view.textclassifier.logging.SmartSelectionEventTracker.ZERO));
        final int eventEnd = java.lang.Integer.parseInt(java.util.Objects.toString(log.getTaggedData(android.view.textclassifier.logging.SmartSelectionEventTracker.EVENT_END), android.view.textclassifier.logging.SmartSelectionEventTracker.ZERO));
        android.util.Log.d(android.view.textclassifier.logging.SmartSelectionEventTracker.LOG_TAG, java.lang.String.format("%2d: %s/%s, range=%d,%d - smart_range=%d,%d (%s/%s)", index, type, entity, eventStart, eventEnd, smartStart, smartEnd, widget, model));
    }

    /**
     * A selection event.
     * Specify index parameters as word token indices.
     */
    public static final class SelectionEvent {
        /**
         * Use this to specify an indeterminate positive index.
         */
        public static final int OUT_OF_BOUNDS = java.lang.Integer.MAX_VALUE;

        /**
         * Use this to specify an indeterminate negative index.
         */
        public static final int OUT_OF_BOUNDS_NEGATIVE = java.lang.Integer.MIN_VALUE;

        private static final java.lang.String NO_VERSION_TAG = "";

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OVERTYPE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.COPY, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.PASTE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.CUT, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SHARE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SMART_SHARE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.DRAG, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.ABANDON, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OTHER, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SELECT_ALL, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.RESET })
        public @interface ActionType {
            /**
             * User typed over the selection.
             */
            int OVERTYPE = 100;

            /**
             * User copied the selection.
             */
            int COPY = 101;

            /**
             * User pasted over the selection.
             */
            int PASTE = 102;

            /**
             * User cut the selection.
             */
            int CUT = 103;

            /**
             * User shared the selection.
             */
            int SHARE = 104;

            /**
             * User clicked the textAssist menu item.
             */
            int SMART_SHARE = 105;

            /**
             * User dragged+dropped the selection.
             */
            int DRAG = 106;

            /**
             * User abandoned the selection.
             */
            int ABANDON = 107;

            /**
             * User performed an action on the selection.
             */
            int OTHER = 108;

            /* Non-terminal actions. */
            /**
             * User activated Select All
             */
            int SELECT_ALL = 200;

            /**
             * User reset the smart selection.
             */
            int RESET = 201;
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OVERTYPE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.COPY, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.PASTE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.CUT, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SHARE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SMART_SHARE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.DRAG, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.ABANDON, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OTHER, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SELECT_ALL, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.RESET, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_STARTED, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_MODIFIED, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_SINGLE, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_MULTI, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.AUTO_SELECTION })
        private @interface EventType {
            /**
             * User started a new selection.
             */
            int SELECTION_STARTED = 1;

            /**
             * User modified an existing selection.
             */
            int SELECTION_MODIFIED = 2;

            /**
             * Smart selection triggered for a single token (word).
             */
            int SMART_SELECTION_SINGLE = 3;

            /**
             * Smart selection triggered spanning multiple tokens (words).
             */
            int SMART_SELECTION_MULTI = 4;

            /**
             * Something else other than User or the default TextClassifier triggered a selection.
             */
            int AUTO_SELECTION = 5;
        }

        private final int mStart;

        private final int mEnd;

        @android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType
        private int mEventType;

        @android.view.textclassifier.TextClassifier.EntityType
        private final java.lang.String mEntityType;

        private final java.lang.String mVersionTag;

        private SelectionEvent(int start, int end, int eventType, @android.view.textclassifier.TextClassifier.EntityType
        java.lang.String entityType, java.lang.String versionTag) {
            com.android.internal.util.Preconditions.checkArgument(end >= start, "end cannot be less than start");
            mStart = start;
            mEnd = end;
            mEventType = eventType;
            mEntityType = com.android.internal.util.Preconditions.checkNotNull(entityType);
            mVersionTag = com.android.internal.util.Preconditions.checkNotNull(versionTag);
        }

        /**
         * Creates a "selection started" event.
         *
         * @param start
         * 		the word index of the selected word
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionStarted(int start) {
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, start + 1, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_STARTED, android.view.textclassifier.TextClassifier.TYPE_UNKNOWN, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.NO_VERSION_TAG);
        }

        /**
         * Creates a "selection modified" event.
         * Use when the user modifies the selection.
         *
         * @param start
         * 		the start word (inclusive) index of the selection
         * @param end
         * 		the end word (exclusive) index of the selection
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionModified(int start, int end) {
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, end, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_MODIFIED, android.view.textclassifier.TextClassifier.TYPE_UNKNOWN, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.NO_VERSION_TAG);
        }

        /**
         * Creates a "selection modified" event.
         * Use when the user modifies the selection and the selection's entity type is known.
         *
         * @param start
         * 		the start word (inclusive) index of the selection
         * @param end
         * 		the end word (exclusive) index of the selection
         * @param classification
         * 		the TextClassification object returned by the TextClassifier that
         * 		classified the selected text
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionModified(int start, int end, @android.annotation.NonNull
        android.view.textclassifier.TextClassification classification) {
            final java.lang.String entityType = (classification.getEntityCount() > 0) ? classification.getEntity(0) : android.view.textclassifier.TextClassifier.TYPE_UNKNOWN;
            final java.lang.String versionTag = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.getVersionInfo(classification.getId());
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, end, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SELECTION_MODIFIED, entityType, versionTag);
        }

        /**
         * Creates a "selection modified" event.
         * Use when a TextClassifier modifies the selection.
         *
         * @param start
         * 		the start word (inclusive) index of the selection
         * @param end
         * 		the end word (exclusive) index of the selection
         * @param selection
         * 		the TextSelection object returned by the TextClassifier for the
         * 		specified selection
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionModified(int start, int end, @android.annotation.NonNull
        android.view.textclassifier.TextSelection selection) {
            final boolean smartSelection = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.getSourceClassifier(selection.getId()).equals(android.view.textclassifier.TextClassifier.DEFAULT_LOG_TAG);
            final int eventType;
            if (smartSelection) {
                eventType = ((end - start) > 1) ? android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_MULTI : android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.SMART_SELECTION_SINGLE;
            } else {
                eventType = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.EventType.AUTO_SELECTION;
            }
            final java.lang.String entityType = (selection.getEntityCount() > 0) ? selection.getEntity(0) : android.view.textclassifier.TextClassifier.TYPE_UNKNOWN;
            final java.lang.String versionTag = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.getVersionInfo(selection.getId());
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, end, eventType, entityType, versionTag);
        }

        /**
         * Creates an event specifying an action taken on a selection.
         * Use when the user clicks on an action to act on the selected text.
         *
         * @param start
         * 		the start word (inclusive) index of the selection
         * @param end
         * 		the end word (exclusive) index of the selection
         * @param actionType
         * 		the action that was performed on the selection
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionAction(int start, int end, @android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType
        int actionType) {
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, end, actionType, android.view.textclassifier.TextClassifier.TYPE_UNKNOWN, android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.NO_VERSION_TAG);
        }

        /**
         * Creates an event specifying an action taken on a selection.
         * Use when the user clicks on an action to act on the selected text and the selection's
         * entity type is known.
         *
         * @param start
         * 		the start word (inclusive) index of the selection
         * @param end
         * 		the end word (exclusive) index of the selection
         * @param actionType
         * 		the action that was performed on the selection
         * @param classification
         * 		the TextClassification object returned by the TextClassifier that
         * 		classified the selected text
         */
        @android.annotation.UnsupportedAppUsage
        public static android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent selectionAction(int start, int end, @android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType
        int actionType, @android.annotation.NonNull
        android.view.textclassifier.TextClassification classification) {
            final java.lang.String entityType = (classification.getEntityCount() > 0) ? classification.getEntity(0) : android.view.textclassifier.TextClassifier.TYPE_UNKNOWN;
            final java.lang.String versionTag = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.getVersionInfo(classification.getId());
            return new android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent(start, end, actionType, entityType, versionTag);
        }

        private static java.lang.String getVersionInfo(java.lang.String signature) {
            final int start = signature.indexOf("|");
            final int end = signature.indexOf("|", start);
            if ((start >= 0) && (end >= start)) {
                return signature.substring(start, end);
            }
            return "";
        }

        private static java.lang.String getSourceClassifier(java.lang.String signature) {
            final int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return "";
        }

        private boolean isTerminal() {
            switch (mEventType) {
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OVERTYPE :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.COPY :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.PASTE :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.CUT :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SHARE :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.SMART_SHARE :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.DRAG :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.ABANDON :
                    // fall through
                case android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.ActionType.OTHER :
                    // fall through
                    return true;
                default :
                    return false;
            }
        }
    }
}

