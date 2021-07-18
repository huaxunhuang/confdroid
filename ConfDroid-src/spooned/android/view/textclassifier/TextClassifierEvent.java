/**
 * Copyright 2018 The Android Open Source Project
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
 * This class represents events that are sent by components to the {@link TextClassifier} to report
 * something of note that relates to a feature powered by the TextClassifier. The TextClassifier may
 * log these events or use them to improve future responses to queries.
 * <p>
 * Each category of events has its their own subclass. Events of each type have an associated
 * set of related properties. You can find their specification in the subclasses.
 */
public abstract class TextClassifierEvent implements android.os.Parcelable {
    private static final int PARCEL_TOKEN_TEXT_SELECTION_EVENT = 1;

    private static final int PARCEL_TOKEN_TEXT_LINKIFY_EVENT = 2;

    private static final int PARCEL_TOKEN_CONVERSATION_ACTION_EVENT = 3;

    private static final int PARCEL_TOKEN_LANGUAGE_DETECTION_EVENT = 4;

    /**
     *
     *
     * @unknown *
     */
    // For custom event categories, use range 1000+.
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextClassifierEvent.CATEGORY_SELECTION, android.view.textclassifier.TextClassifierEvent.CATEGORY_LINKIFY, android.view.textclassifier.TextClassifierEvent.CATEGORY_CONVERSATION_ACTIONS, android.view.textclassifier.TextClassifierEvent.CATEGORY_LANGUAGE_DETECTION })
    public @interface Category {}

    /**
     * Smart selection
     *
     * @see TextSelectionEvent
     */
    public static final int CATEGORY_SELECTION = 1;

    /**
     * Linkify
     *
     * @see TextLinkifyEvent
     */
    public static final int CATEGORY_LINKIFY = 2;

    /**
     * Conversation actions
     *
     * @see ConversationActionsEvent
     */
    public static final int CATEGORY_CONVERSATION_ACTIONS = 3;

    /**
     * Language detection
     *
     * @see LanguageDetectionEvent
     */
    public static final int CATEGORY_LANGUAGE_DETECTION = 4;

    /**
     *
     *
     * @unknown 
     */
    // For custom event types, use range 1,000,000+.
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_STARTED, android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_MODIFIED, android.view.textclassifier.TextClassifierEvent.TYPE_SMART_SELECTION_SINGLE, android.view.textclassifier.TextClassifierEvent.TYPE_SMART_SELECTION_MULTI, android.view.textclassifier.TextClassifierEvent.TYPE_AUTO_SELECTION, android.view.textclassifier.TextClassifierEvent.TYPE_ACTIONS_SHOWN, android.view.textclassifier.TextClassifierEvent.TYPE_LINK_CLICKED, android.view.textclassifier.TextClassifierEvent.TYPE_OVERTYPE, android.view.textclassifier.TextClassifierEvent.TYPE_COPY_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_PASTE_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_CUT_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_SHARE_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_SMART_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_DRAG, android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_DESTROYED, android.view.textclassifier.TextClassifierEvent.TYPE_OTHER_ACTION, android.view.textclassifier.TextClassifierEvent.TYPE_SELECT_ALL, android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_RESET, android.view.textclassifier.TextClassifierEvent.TYPE_MANUAL_REPLY, android.view.textclassifier.TextClassifierEvent.TYPE_ACTIONS_GENERATED })
    public @interface Type {}

    /**
     * User started a new selection.
     */
    public static final int TYPE_SELECTION_STARTED = 1;

    /**
     * User modified an existing selection.
     */
    public static final int TYPE_SELECTION_MODIFIED = 2;

    /**
     * Smart selection triggered for a single token (word).
     */
    public static final int TYPE_SMART_SELECTION_SINGLE = 3;

    /**
     * Smart selection triggered spanning multiple tokens (words).
     */
    public static final int TYPE_SMART_SELECTION_MULTI = 4;

    /**
     * Something else other than user or the default TextClassifier triggered a selection.
     */
    public static final int TYPE_AUTO_SELECTION = 5;

    /**
     * Smart actions shown to the user.
     */
    public static final int TYPE_ACTIONS_SHOWN = 6;

    /**
     * User clicked a link.
     */
    public static final int TYPE_LINK_CLICKED = 7;

    /**
     * User typed over the selection.
     */
    public static final int TYPE_OVERTYPE = 8;

    /**
     * User clicked on Copy action.
     */
    public static final int TYPE_COPY_ACTION = 9;

    /**
     * User clicked on Paste action.
     */
    public static final int TYPE_PASTE_ACTION = 10;

    /**
     * User clicked on Cut action.
     */
    public static final int TYPE_CUT_ACTION = 11;

    /**
     * User clicked on Share action.
     */
    public static final int TYPE_SHARE_ACTION = 12;

    /**
     * User clicked on a Smart action.
     */
    public static final int TYPE_SMART_ACTION = 13;

    /**
     * User dragged+dropped the selection.
     */
    public static final int TYPE_SELECTION_DRAG = 14;

    /**
     * Selection is destroyed.
     */
    public static final int TYPE_SELECTION_DESTROYED = 15;

    /**
     * User clicked on a custom action.
     */
    public static final int TYPE_OTHER_ACTION = 16;

    /**
     * User clicked on Select All action
     */
    public static final int TYPE_SELECT_ALL = 17;

    /**
     * User reset the smart selection.
     */
    public static final int TYPE_SELECTION_RESET = 18;

    /**
     * User composed a reply.
     */
    public static final int TYPE_MANUAL_REPLY = 19;

    /**
     * TextClassifier generated some actions
     */
    public static final int TYPE_ACTIONS_GENERATED = 20;

    @android.view.textclassifier.TextClassifierEvent.Category
    private final int mEventCategory;

    @android.view.textclassifier.TextClassifierEvent.Type
    private final int mEventType;

    @android.annotation.Nullable
    private final java.lang.String[] mEntityTypes;

    @android.annotation.Nullable
    private final android.view.textclassifier.TextClassificationContext mEventContext;

    @android.annotation.Nullable
    private final java.lang.String mResultId;

    private final int mEventIndex;

    private final float[] mScores;

    @android.annotation.Nullable
    private final java.lang.String mModelName;

    private final int[] mActionIndices;

    @android.annotation.Nullable
    private final android.icu.util.ULocale mLocale;

    private final android.os.Bundle mExtras;

    /**
     * Session id holder to help with converting this event to the legacy SelectionEvent.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.view.textclassifier.TextClassificationSessionId mHiddenTempSessionId;

    private TextClassifierEvent(android.view.textclassifier.TextClassifierEvent.Builder builder) {
        mEventCategory = builder.mEventCategory;
        mEventType = builder.mEventType;
        mEntityTypes = builder.mEntityTypes;
        mEventContext = builder.mEventContext;
        mResultId = builder.mResultId;
        mEventIndex = builder.mEventIndex;
        mScores = builder.mScores;
        mModelName = builder.mModelName;
        mActionIndices = builder.mActionIndices;
        mLocale = builder.mLocale;
        mExtras = (builder.mExtras == null) ? android.os.Bundle.EMPTY : builder.mExtras;
    }

    private TextClassifierEvent(android.os.Parcel in) {
        mEventCategory = in.readInt();
        mEventType = in.readInt();
        mEntityTypes = in.readStringArray();
        mEventContext = in.readParcelable(null);
        mResultId = in.readString();
        mEventIndex = in.readInt();
        int scoresLength = in.readInt();
        mScores = new float[scoresLength];
        in.readFloatArray(mScores);
        mModelName = in.readString();
        mActionIndices = in.createIntArray();
        final java.lang.String languageTag = in.readString();
        mLocale = (languageTag == null) ? null : android.icu.util.ULocale.forLanguageTag(languageTag);
        mExtras = in.readBundle();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @android.annotation.NonNull
    public static final android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent>() {
        @java.lang.Override
        public android.view.textclassifier.TextClassifierEvent createFromParcel(android.os.Parcel in) {
            int token = in.readInt();
            if (token == android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_TEXT_SELECTION_EVENT) {
                return new android.view.textclassifier.TextClassifierEvent.TextSelectionEvent(in);
            }
            if (token == android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_TEXT_LINKIFY_EVENT) {
                return new android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent(in);
            }
            if (token == android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_LANGUAGE_DETECTION_EVENT) {
                return new android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent(in);
            }
            if (token == android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_CONVERSATION_ACTION_EVENT) {
                return new android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent(in);
            }
            throw new java.lang.IllegalStateException("Unexpected input event type token in parcel.");
        }

        @java.lang.Override
        public android.view.textclassifier.TextClassifierEvent[] newArray(int size) {
            return new android.view.textclassifier.TextClassifierEvent[size];
        }
    };

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(getParcelToken());
        dest.writeInt(mEventCategory);
        dest.writeInt(mEventType);
        dest.writeStringArray(mEntityTypes);
        dest.writeParcelable(mEventContext, flags);
        dest.writeString(mResultId);
        dest.writeInt(mEventIndex);
        dest.writeInt(mScores.length);
        dest.writeFloatArray(mScores);
        dest.writeString(mModelName);
        dest.writeIntArray(mActionIndices);
        dest.writeString(mLocale == null ? null : mLocale.toLanguageTag());
        dest.writeBundle(mExtras);
    }

    private int getParcelToken() {
        if (this instanceof android.view.textclassifier.TextClassifierEvent.TextSelectionEvent) {
            return android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_TEXT_SELECTION_EVENT;
        }
        if (this instanceof android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent) {
            return android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_TEXT_LINKIFY_EVENT;
        }
        if (this instanceof android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent) {
            return android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_LANGUAGE_DETECTION_EVENT;
        }
        if (this instanceof android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent) {
            return android.view.textclassifier.TextClassifierEvent.PARCEL_TOKEN_CONVERSATION_ACTION_EVENT;
        }
        throw new java.lang.IllegalArgumentException("Unexpected type: " + this.getClass().getSimpleName());
    }

    /**
     * Returns the event category. e.g. {@link #CATEGORY_SELECTION}.
     */
    @android.view.textclassifier.TextClassifierEvent.Category
    public int getEventCategory() {
        return mEventCategory;
    }

    /**
     * Returns the event type. e.g. {@link #TYPE_SELECTION_STARTED}.
     */
    @android.view.textclassifier.TextClassifierEvent.Type
    public int getEventType() {
        return mEventType;
    }

    /**
     * Returns an array of entity types. e.g. {@link TextClassifier#TYPE_ADDRESS}.
     *
     * @see Builder#setEntityTypes(String...) for supported types.
     */
    @android.annotation.NonNull
    public java.lang.String[] getEntityTypes() {
        return mEntityTypes;
    }

    /**
     * Returns the event context.
     */
    @android.annotation.Nullable
    public android.view.textclassifier.TextClassificationContext getEventContext() {
        return mEventContext;
    }

    /**
     * Returns the id of the text classifier result related to this event.
     */
    @android.annotation.Nullable
    public java.lang.String getResultId() {
        return mResultId;
    }

    /**
     * Returns the index of this event in the series of event it belongs to.
     */
    public int getEventIndex() {
        return mEventIndex;
    }

    /**
     * Returns the scores of the suggestions.
     */
    @android.annotation.NonNull
    public float[] getScores() {
        return mScores;
    }

    /**
     * Returns the model name.
     */
    @android.annotation.Nullable
    public java.lang.String getModelName() {
        return mModelName;
    }

    /**
     * Returns the indices of the actions relating to this event.
     * Actions are usually returned by the text classifier in priority order with the most
     * preferred action at index 0. This list gives an indication of the position of the actions
     * that are being reported.
     *
     * @see Builder#setActionIndices(int...)
     */
    @android.annotation.NonNull
    public int[] getActionIndices() {
        return mActionIndices;
    }

    /**
     * Returns the detected locale.
     */
    @android.annotation.Nullable
    public android.icu.util.ULocale getLocale() {
        return mLocale;
    }

    /**
     * Returns a bundle containing non-structured extra information about this event.
     *
     * <p><b>NOTE: </b>Do not modify this bundle.
     */
    @android.annotation.NonNull
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder(128);
        out.append(this.getClass().getSimpleName());
        out.append("{");
        out.append("mEventCategory=").append(mEventCategory);
        out.append(", mEventTypes=").append(java.util.Arrays.toString(mEntityTypes));
        out.append(", mEventContext=").append(mEventContext);
        out.append(", mResultId=").append(mResultId);
        out.append(", mEventIndex=").append(mEventIndex);
        out.append(", mExtras=").append(mExtras);
        out.append(", mScores=").append(java.util.Arrays.toString(mScores));
        out.append(", mModelName=").append(mModelName);
        out.append(", mActionIndices=").append(java.util.Arrays.toString(mActionIndices));
        out.append("}");
        return out.toString();
    }

    /**
     * Returns a {@link SelectionEvent} equivalent of this event; or {@code null} if it can not be
     * converted to a {@link SelectionEvent}.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public final android.view.textclassifier.SelectionEvent toSelectionEvent() {
        final int invocationMethod;
        switch (getEventCategory()) {
            case android.view.textclassifier.TextClassifierEvent.CATEGORY_SELECTION :
                invocationMethod = android.view.textclassifier.SelectionEvent.INVOCATION_MANUAL;
                break;
            case android.view.textclassifier.TextClassifierEvent.CATEGORY_LINKIFY :
                invocationMethod = android.view.textclassifier.SelectionEvent.INVOCATION_LINK;
                break;
            default :
                // Cannot be converted to a SelectionEvent.
                return null;
        }
        final java.lang.String entityType = (getEntityTypes().length > 0) ? getEntityTypes()[0] : android.view.textclassifier.TextClassifier.TYPE_UNKNOWN;
        final android.view.textclassifier.SelectionEvent out = /* absoluteStart= */
        /* absoluteEnd= */
        /* eventType= */
        new android.view.textclassifier.SelectionEvent(0, 0, 0, entityType, android.view.textclassifier.SelectionEvent.INVOCATION_UNKNOWN, android.view.textclassifier.SelectionEvent.NO_SIGNATURE);
        out.setInvocationMethod(invocationMethod);
        final android.view.textclassifier.TextClassificationContext eventContext = getEventContext();
        if (eventContext != null) {
            out.setTextClassificationSessionContext(getEventContext());
        }
        out.setSessionId(mHiddenTempSessionId);
        final java.lang.String resultId = getResultId();
        out.setResultId(resultId == null ? android.view.textclassifier.SelectionEvent.NO_SIGNATURE : resultId);
        out.setEventIndex(getEventIndex());
        final int eventType;
        switch (getEventType()) {
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_STARTED :
                eventType = android.view.textclassifier.SelectionEvent.EVENT_SELECTION_STARTED;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_MODIFIED :
                eventType = android.view.textclassifier.SelectionEvent.EVENT_SELECTION_MODIFIED;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SMART_SELECTION_SINGLE :
                eventType = android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_SINGLE;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SMART_SELECTION_MULTI :
                eventType = android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_MULTI;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_AUTO_SELECTION :
                eventType = android.view.textclassifier.SelectionEvent.EVENT_AUTO_SELECTION;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_OVERTYPE :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_OVERTYPE;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_COPY_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_COPY;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_PASTE_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_PASTE;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_CUT_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_CUT;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SHARE_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_SHARE;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SMART_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_SMART_SHARE;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_DRAG :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_DRAG;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_DESTROYED :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_ABANDON;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_OTHER_ACTION :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_OTHER;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECT_ALL :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_SELECT_ALL;
                break;
            case android.view.textclassifier.TextClassifierEvent.TYPE_SELECTION_RESET :
                eventType = android.view.textclassifier.SelectionEvent.ACTION_RESET;
                break;
            default :
                eventType = 0;
                break;
        }
        out.setEventType(eventType);
        if (this instanceof android.view.textclassifier.TextClassifierEvent.TextSelectionEvent) {
            final android.view.textclassifier.TextClassifierEvent.TextSelectionEvent selEvent = ((android.view.textclassifier.TextClassifierEvent.TextSelectionEvent) (this));
            // TODO: Ideally, we should have these fields in events of type
            // TextClassifierEvent.TextLinkifyEvent events too but we're now past the API deadline
            // and will have to do with these fields being set only in TextSelectionEvent events.
            // Fix this at the next API bump.
            out.setStart(selEvent.getRelativeWordStartIndex());
            out.setEnd(selEvent.getRelativeWordEndIndex());
            out.setSmartStart(selEvent.getRelativeSuggestedWordStartIndex());
            out.setSmartEnd(selEvent.getRelativeSuggestedWordEndIndex());
        }
        return out;
    }

    /**
     * Builder to build a text classifier event.
     *
     * @param <T>
     * 		The subclass to be built.
     */
    public static abstract class Builder<T extends android.view.textclassifier.TextClassifierEvent.Builder<T>> {
        private final int mEventCategory;

        private final int mEventType;

        private java.lang.String[] mEntityTypes = new java.lang.String[0];

        @android.annotation.Nullable
        private android.view.textclassifier.TextClassificationContext mEventContext;

        @android.annotation.Nullable
        private java.lang.String mResultId;

        private int mEventIndex;

        private float[] mScores = new float[0];

        @android.annotation.Nullable
        private java.lang.String mModelName;

        private int[] mActionIndices = new int[0];

        @android.annotation.Nullable
        private android.icu.util.ULocale mLocale;

        @android.annotation.Nullable
        private android.os.Bundle mExtras;

        /**
         * Creates a builder for building {@link TextClassifierEvent}s.
         *
         * @param eventCategory
         * 		The event category. e.g. {@link #CATEGORY_SELECTION}
         * @param eventType
         * 		The event type. e.g. {@link #TYPE_SELECTION_STARTED}
         */
        private Builder(@android.view.textclassifier.TextClassifierEvent.Category
        int eventCategory, @android.view.textclassifier.TextClassifierEvent.Type
        int eventType) {
            mEventCategory = eventCategory;
            mEventType = eventType;
        }

        /**
         * Sets the entity types. e.g. {@link TextClassifier#TYPE_ADDRESS}.
         * <p>
         * Supported types:
         * <p>See {@link TextClassifier.EntityType}
         * <p>See {@link ConversationAction.ActionType}
         * <p>See {@link ULocale#toLanguageTag()}
         */
        @android.annotation.NonNull
        public T setEntityTypes(@android.annotation.NonNull
        java.lang.String... entityTypes) {
            com.android.internal.util.Preconditions.checkNotNull(entityTypes);
            mEntityTypes = new java.lang.String[entityTypes.length];
            java.lang.System.arraycopy(entityTypes, 0, mEntityTypes, 0, entityTypes.length);
            return self();
        }

        /**
         * Sets the event context.
         */
        @android.annotation.NonNull
        public T setEventContext(@android.annotation.Nullable
        android.view.textclassifier.TextClassificationContext eventContext) {
            mEventContext = eventContext;
            return self();
        }

        /**
         * Sets the id of the text classifier result related to this event.
         */
        @android.annotation.NonNull
        public T setResultId(@android.annotation.Nullable
        java.lang.String resultId) {
            mResultId = resultId;
            return self();
        }

        /**
         * Sets the index of this event in the series of events it belongs to.
         */
        @android.annotation.NonNull
        public T setEventIndex(int eventIndex) {
            mEventIndex = eventIndex;
            return self();
        }

        /**
         * Sets the scores of the suggestions.
         */
        @android.annotation.NonNull
        public T setScores(@android.annotation.NonNull
        float... scores) {
            com.android.internal.util.Preconditions.checkNotNull(scores);
            mScores = new float[scores.length];
            java.lang.System.arraycopy(scores, 0, mScores, 0, scores.length);
            return self();
        }

        /**
         * Sets the model name string.
         */
        @android.annotation.NonNull
        public T setModelName(@android.annotation.Nullable
        java.lang.String modelVersion) {
            mModelName = modelVersion;
            return self();
        }

        /**
         * Sets the indices of the actions involved in this event. Actions are usually returned by
         * the text classifier in priority order with the most preferred action at index 0.
         * These indices give an indication of the position of the actions that are being reported.
         * <p>
         * E.g.
         * <pre>
         *   // 3 smart actions are shown at index 0, 1, 2 respectively in response to a link click.
         *   new TextClassifierEvent.Builder(CATEGORY_LINKIFY, TYPE_ACTIONS_SHOWN)
         *       .setEventIndex(0, 1, 2)
         *       ...
         *       .build();
         *
         *   ...
         *
         *   // Smart action at index 1 is activated.
         *   new TextClassifierEvent.Builder(CATEGORY_LINKIFY, TYPE_SMART_ACTION)
         *       .setEventIndex(1)
         *       ...
         *       .build();
         * </pre>
         *
         * @see TextClassification#getActions()
         */
        @android.annotation.NonNull
        public T setActionIndices(@android.annotation.NonNull
        int... actionIndices) {
            mActionIndices = new int[actionIndices.length];
            java.lang.System.arraycopy(actionIndices, 0, mActionIndices, 0, actionIndices.length);
            return self();
        }

        /**
         * Sets the detected locale.
         */
        @android.annotation.NonNull
        public T setLocale(@android.annotation.Nullable
        android.icu.util.ULocale locale) {
            mLocale = locale;
            return self();
        }

        /**
         * Sets a bundle containing non-structured extra information about the event.
         *
         * <p><b>NOTE: </b>Prefer to set only immutable values on the bundle otherwise, avoid
         * updating the internals of this bundle as it may have unexpected consequences on the
         * clients of the built event object. For similar reasons, avoid depending on mutable
         * objects in this bundle.
         */
        @android.annotation.NonNull
        public T setExtras(@android.annotation.NonNull
        android.os.Bundle extras) {
            mExtras = com.android.internal.util.Preconditions.checkNotNull(extras);
            return self();
        }

        abstract T self();
    }

    /**
     * This class represents events that are related to the smart text selection feature.
     * <p>
     * <pre>
     *     // User started a selection. e.g. "York" in text "New York City, NY".
     *     new TextSelectionEvent.Builder(TYPE_SELECTION_STARTED)
     *         .setEventContext(classificationContext)
     *         .setEventIndex(0)
     *         .build();
     *
     *     // System smart-selects a recognized entity. e.g. "New York City".
     *     new TextSelectionEvent.Builder(TYPE_SMART_SELECTION_MULTI)
     *         .setEventContext(classificationContext)
     *         .setResultId(textSelection.getId())
     *         .setRelativeWordStartIndex(-1) // Goes back one word to "New" from "York".
     *         .setRelativeWordEndIndex(2)    // Goes forward 2 words from "York" to start of ",".
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(1)
     *         .build();
     *
     *     // User resets the selection to the original selection. i.e. "York".
     *     new TextSelectionEvent.Builder(TYPE_SELECTION_RESET)
     *         .setEventContext(classificationContext)
     *         .setResultId(textSelection.getId())
     *         .setRelativeSuggestedWordStartIndex(-1) // Repeated from above.
     *         .setRelativeSuggestedWordEndIndex(2)    // Repeated from above.
     *         .setRelativeWordStartIndex(0)           // Original selection is always at (0, 1].
     *         .setRelativeWordEndIndex(1)
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(2)
     *         .build();
     *
     *     // User modified the selection. e.g. "New".
     *     new TextSelectionEvent.Builder(TYPE_SELECTION_MODIFIED)
     *         .setEventContext(classificationContext)
     *         .setResultId(textSelection.getId())
     *         .setRelativeSuggestedWordStartIndex(-1) // Repeated from above.
     *         .setRelativeSuggestedWordEndIndex(2)    // Repeated from above.
     *         .setRelativeWordStartIndex(-1)          // Goes backward one word from "York" to
     *         "New".
     *         .setRelativeWordEndIndex(0)             // Goes backward one word to exclude "York".
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(3)
     *         .build();
     *
     *     // Smart (contextual) actions (at indices, 0, 1, 2) presented to the user.
     *     // e.g. "Map", "Ride share", "Explore".
     *     new TextSelectionEvent.Builder(TYPE_ACTIONS_SHOWN)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setActionIndices(0, 1, 2)
     *         .setEventIndex(4)
     *         .build();
     *
     *     // User chooses the "Copy" action.
     *     new TextSelectionEvent.Builder(TYPE_COPY_ACTION)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(5)
     *         .build();
     *
     *     // User chooses smart action at index 1. i.e. "Ride share".
     *     new TextSelectionEvent.Builder(TYPE_SMART_ACTION)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setActionIndices(1)
     *         .setEventIndex(5)
     *         .build();
     *
     *     // Selection dismissed.
     *     new TextSelectionEvent.Builder(TYPE_SELECTION_DESTROYED)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(6)
     *         .build();
     * </pre>
     * <p>
     */
    public static final class TextSelectionEvent extends android.view.textclassifier.TextClassifierEvent implements android.os.Parcelable {
        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.TextSelectionEvent> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.TextSelectionEvent>() {
            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent createFromParcel(android.os.Parcel in) {
                in.readInt();// skip token, we already know this is a TextSelectionEvent

                return new android.view.textclassifier.TextClassifierEvent.TextSelectionEvent(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent[] newArray(int size) {
                return new android.view.textclassifier.TextClassifierEvent.TextSelectionEvent[size];
            }
        };

        final int mRelativeWordStartIndex;

        final int mRelativeWordEndIndex;

        final int mRelativeSuggestedWordStartIndex;

        final int mRelativeSuggestedWordEndIndex;

        private TextSelectionEvent(android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder builder) {
            super(builder);
            mRelativeWordStartIndex = builder.mRelativeWordStartIndex;
            mRelativeWordEndIndex = builder.mRelativeWordEndIndex;
            mRelativeSuggestedWordStartIndex = builder.mRelativeSuggestedWordStartIndex;
            mRelativeSuggestedWordEndIndex = builder.mRelativeSuggestedWordEndIndex;
        }

        private TextSelectionEvent(android.os.Parcel in) {
            super(in);
            mRelativeWordStartIndex = in.readInt();
            mRelativeWordEndIndex = in.readInt();
            mRelativeSuggestedWordStartIndex = in.readInt();
            mRelativeSuggestedWordEndIndex = in.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mRelativeWordStartIndex);
            dest.writeInt(mRelativeWordEndIndex);
            dest.writeInt(mRelativeSuggestedWordStartIndex);
            dest.writeInt(mRelativeSuggestedWordEndIndex);
        }

        /**
         * Returns the relative word index of the start of the selection.
         */
        public int getRelativeWordStartIndex() {
            return mRelativeWordStartIndex;
        }

        /**
         * Returns the relative word (exclusive) index of the end of the selection.
         */
        public int getRelativeWordEndIndex() {
            return mRelativeWordEndIndex;
        }

        /**
         * Returns the relative word index of the start of the smart selection.
         */
        public int getRelativeSuggestedWordStartIndex() {
            return mRelativeSuggestedWordStartIndex;
        }

        /**
         * Returns the relative word (exclusive) index of the end of the
         * smart selection.
         */
        public int getRelativeSuggestedWordEndIndex() {
            return mRelativeSuggestedWordEndIndex;
        }

        /**
         * Builder class for {@link TextSelectionEvent}.
         */
        public static final class Builder extends android.view.textclassifier.TextClassifierEvent.Builder<android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder> {
            int mRelativeWordStartIndex;

            int mRelativeWordEndIndex;

            int mRelativeSuggestedWordStartIndex;

            int mRelativeSuggestedWordEndIndex;

            /**
             * Creates a builder for building {@link TextSelectionEvent}s.
             *
             * @param eventType
             * 		The event type. e.g. {@link #TYPE_SELECTION_STARTED}
             */
            public Builder(@android.view.textclassifier.TextClassifierEvent.Type
            int eventType) {
                super(android.view.textclassifier.TextClassifierEvent.CATEGORY_SELECTION, eventType);
            }

            /**
             * Sets the relative word index of the start of the selection.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder setRelativeWordStartIndex(int relativeWordStartIndex) {
                mRelativeWordStartIndex = relativeWordStartIndex;
                return this;
            }

            /**
             * Sets the relative word (exclusive) index of the end of the
             * selection.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder setRelativeWordEndIndex(int relativeWordEndIndex) {
                mRelativeWordEndIndex = relativeWordEndIndex;
                return this;
            }

            /**
             * Sets the relative word index of the start of the smart
             * selection.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder setRelativeSuggestedWordStartIndex(int relativeSuggestedWordStartIndex) {
                mRelativeSuggestedWordStartIndex = relativeSuggestedWordStartIndex;
                return this;
            }

            /**
             * Sets the relative word (exclusive) index of the end of the
             * smart selection.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder setRelativeSuggestedWordEndIndex(int relativeSuggestedWordEndIndex) {
                mRelativeSuggestedWordEndIndex = relativeSuggestedWordEndIndex;
                return this;
            }

            @java.lang.Override
            android.view.textclassifier.TextClassifierEvent.TextSelectionEvent.Builder self() {
                return this;
            }

            /**
             * Builds and returns a {@link TextSelectionEvent}.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextSelectionEvent build() {
                return new android.view.textclassifier.TextClassifierEvent.TextSelectionEvent(this);
            }
        }
    }

    /**
     * This class represents events that are related to the smart linkify feature.
     * <p>
     * <pre>
     *     // User clicked on a link.
     *     new TextLinkifyEvent.Builder(TYPE_LINK_CLICKED)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setEventIndex(0)
     *         .build();
     *
     *     // Smart (contextual) actions presented to the user in response to a link click.
     *     new TextLinkifyEvent.Builder(TYPE_ACTIONS_SHOWN)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setActionIndices(range(textClassification.getActions().size()))
     *         .setEventIndex(1)
     *         .build();
     *
     *     // User chooses smart action at index 0.
     *     new TextLinkifyEvent.Builder(TYPE_SMART_ACTION)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(textClassification.getEntity(0))
     *         .setScore(textClassification.getConfidenceScore(entityType))
     *         .setActionIndices(0)
     *         .setEventIndex(2)
     *         .build();
     * </pre>
     */
    public static final class TextLinkifyEvent extends android.view.textclassifier.TextClassifierEvent implements android.os.Parcelable {
        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent>() {
            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent createFromParcel(android.os.Parcel in) {
                in.readInt();// skip token, we already know this is a TextLinkifyEvent

                return new android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent[] newArray(int size) {
                return new android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent[size];
            }
        };

        private TextLinkifyEvent(android.os.Parcel in) {
            super(in);
        }

        private TextLinkifyEvent(android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder builder) {
            super(builder);
        }

        /**
         * Builder class for {@link TextLinkifyEvent}.
         */
        public static final class Builder extends android.view.textclassifier.TextClassifierEvent.Builder<android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder> {
            /**
             * Creates a builder for building {@link TextLinkifyEvent}s.
             *
             * @param eventType
             * 		The event type. e.g. {@link #TYPE_SMART_ACTION}
             */
            public Builder(@android.view.textclassifier.TextClassifierEvent.Type
            int eventType) {
                super(android.view.textclassifier.TextClassifierEvent.CATEGORY_LINKIFY, eventType);
            }

            @java.lang.Override
            android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder self() {
                return this;
            }

            /**
             * Builds and returns a {@link TextLinkifyEvent}.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent build() {
                return new android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent(this);
            }
        }
    }

    /**
     * This class represents events that are related to the language detection feature.
     * <p>
     * <pre>
     *     // Translate action shown for foreign text.
     *     new LanguageDetectionEvent.Builder(TYPE_ACTIONS_SHOWN)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(language)
     *         .setScore(score)
     *         .setActionIndices(textClassification.getActions().indexOf(translateAction))
     *         .setEventIndex(0)
     *         .build();
     *
     *     // Translate action selected.
     *     new LanguageDetectionEvent.Builder(TYPE_SMART_ACTION)
     *         .setEventContext(classificationContext)
     *         .setResultId(textClassification.getId())
     *         .setEntityTypes(language)
     *         .setScore(score)
     *         .setActionIndices(textClassification.getActions().indexOf(translateAction))
     *         .setEventIndex(1)
     *         .build();
     */
    public static final class LanguageDetectionEvent extends android.view.textclassifier.TextClassifierEvent implements android.os.Parcelable {
        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent>() {
            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent createFromParcel(android.os.Parcel in) {
                // skip token, we already know this is a LanguageDetectionEvent.
                in.readInt();
                return new android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent[] newArray(int size) {
                return new android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent[size];
            }
        };

        private LanguageDetectionEvent(android.os.Parcel in) {
            super(in);
        }

        private LanguageDetectionEvent(android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent.Builder builder) {
            super(builder);
        }

        /**
         * Builder class for {@link LanguageDetectionEvent}.
         */
        public static final class Builder extends android.view.textclassifier.TextClassifierEvent.Builder<android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent.Builder> {
            /**
             * Creates a builder for building {@link TextSelectionEvent}s.
             *
             * @param eventType
             * 		The event type. e.g. {@link #TYPE_SMART_ACTION}
             */
            public Builder(@android.view.textclassifier.TextClassifierEvent.Type
            int eventType) {
                super(android.view.textclassifier.TextClassifierEvent.CATEGORY_LANGUAGE_DETECTION, eventType);
            }

            @java.lang.Override
            android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent.Builder self() {
                return this;
            }

            /**
             * Builds and returns a {@link LanguageDetectionEvent}.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent build() {
                return new android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent(this);
            }
        }
    }

    /**
     * This class represents events that are related to the conversation actions feature.
     * <p>
     * <pre>
     *     // Conversation (contextual) actions/replies generated.
     *     new ConversationActionsEvent.Builder(TYPE_ACTIONS_GENERATED)
     *         .setEventContext(classificationContext)
     *         .setResultId(conversationActions.getId())
     *         .setEntityTypes(getTypes(conversationActions))
     *         .setActionIndices(range(conversationActions.getActions().size()))
     *         .setEventIndex(0)
     *         .build();
     *
     *     // Conversation actions/replies presented to user.
     *     new ConversationActionsEvent.Builder(TYPE_ACTIONS_SHOWN)
     *         .setEventContext(classificationContext)
     *         .setResultId(conversationActions.getId())
     *         .setEntityTypes(getTypes(conversationActions))
     *         .setActionIndices(range(conversationActions.getActions().size()))
     *         .setEventIndex(1)
     *         .build();
     *
     *     // User clicked the "Reply" button to compose their custom reply.
     *     new ConversationActionsEvent.Builder(TYPE_MANUAL_REPLY)
     *         .setEventContext(classificationContext)
     *         .setResultId(conversationActions.getId())
     *         .setEventIndex(2)
     *         .build();
     *
     *     // User selected a smart (contextual) action/reply.
     *     new ConversationActionsEvent.Builder(TYPE_SMART_ACTION)
     *         .setEventContext(classificationContext)
     *         .setResultId(conversationActions.getId())
     *         .setEntityTypes(conversationActions.get(1).getType())
     *         .setScore(conversationAction.get(1).getConfidenceScore())
     *         .setActionIndices(1)
     *         .setEventIndex(2)
     *         .build();
     * </pre>
     */
    public static final class ConversationActionsEvent extends android.view.textclassifier.TextClassifierEvent implements android.os.Parcelable {
        @android.annotation.NonNull
        public static final android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent> CREATOR = new android.view.textclassifier.Creator<android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent>() {
            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent createFromParcel(android.os.Parcel in) {
                // skip token, we already know this is a ConversationActionsEvent.
                in.readInt();
                return new android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent(in);
            }

            @java.lang.Override
            public android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent[] newArray(int size) {
                return new android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent[size];
            }
        };

        private ConversationActionsEvent(android.os.Parcel in) {
            super(in);
        }

        private ConversationActionsEvent(android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent.Builder builder) {
            super(builder);
        }

        /**
         * Builder class for {@link ConversationActionsEvent}.
         */
        public static final class Builder extends android.view.textclassifier.TextClassifierEvent.Builder<android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent.Builder> {
            /**
             * Creates a builder for building {@link TextSelectionEvent}s.
             *
             * @param eventType
             * 		The event type. e.g. {@link #TYPE_SMART_ACTION}
             */
            public Builder(@android.view.textclassifier.TextClassifierEvent.Type
            int eventType) {
                super(android.view.textclassifier.TextClassifierEvent.CATEGORY_CONVERSATION_ACTIONS, eventType);
            }

            @java.lang.Override
            android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent.Builder self() {
                return this;
            }

            /**
             * Builds and returns a {@link ConversationActionsEvent}.
             */
            @android.annotation.NonNull
            public android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent build() {
                return new android.view.textclassifier.TextClassifierEvent.ConversationActionsEvent(this);
            }
        }
    }
}

