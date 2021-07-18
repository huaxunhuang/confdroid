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
package android.view.contentcapture;


/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
public final class ContentCaptureEvent implements android.os.Parcelable {
    private static final java.lang.String TAG = android.view.contentcapture.ContentCaptureEvent.class.getSimpleName();

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_SESSION_FINISHED = -2;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPE_SESSION_STARTED = -1;

    /**
     * Called when a node has been added to the screen and is visible to the user.
     *
     * <p>The metadata of the node is available through {@link #getViewNode()}.
     */
    public static final int TYPE_VIEW_APPEARED = 1;

    /**
     * Called when one or more nodes have been removed from the screen and is not visible to the
     * user anymore.
     *
     * <p>To get the id(s), first call {@link #getIds()} - if it returns {@code null}, then call
     * {@link #getId()}.
     */
    public static final int TYPE_VIEW_DISAPPEARED = 2;

    /**
     * Called when the text of a node has been changed.
     *
     * <p>The id of the node is available through {@link #getId()}, and the new text is
     * available through {@link #getText()}.
     */
    public static final int TYPE_VIEW_TEXT_CHANGED = 3;

    /**
     * Called before events (such as {@link #TYPE_VIEW_APPEARED} and/or
     * {@link #TYPE_VIEW_DISAPPEARED}) representing a view hierarchy are sent.
     *
     * <p><b>NOTE</b>: there is no guarantee this event will be sent. For example, it's not sent
     * if the initial view hierarchy doesn't initially have any view that's important for content
     * capture.
     */
    public static final int TYPE_VIEW_TREE_APPEARING = 4;

    /**
     * Called after events (such as {@link #TYPE_VIEW_APPEARED} and/or
     * {@link #TYPE_VIEW_DISAPPEARED}) representing a view hierarchy were sent.
     *
     * <p><b>NOTE</b>: there is no guarantee this event will be sent. For example, it's not sent
     * if the initial view hierarchy doesn't initially have any view that's important for content
     * capture.
     */
    public static final int TYPE_VIEW_TREE_APPEARED = 5;

    /**
     * Called after a call to
     * {@link ContentCaptureSession#setContentCaptureContext(ContentCaptureContext)}.
     *
     * <p>The passed context is available through {@link #getContentCaptureContext()}.
     */
    public static final int TYPE_CONTEXT_UPDATED = 6;

    /**
     * Called after the session is ready, typically after the activity resumed and the
     * initial views appeared
     */
    public static final int TYPE_SESSION_RESUMED = 7;

    /**
     * Called after the session is paused, typically after the activity paused and the
     * views disappeared.
     */
    public static final int TYPE_SESSION_PAUSED = 8;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "TYPE_" }, value = { android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_APPEARED, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARING, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARED, android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED, android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_PAUSED, android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_RESUMED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface EventType {}

    private final int mSessionId;

    private final int mType;

    private final long mEventTime;

    @android.annotation.Nullable
    private android.view.autofill.AutofillId mId;

    @android.annotation.Nullable
    private java.util.ArrayList<android.view.autofill.AutofillId> mIds;

    @android.annotation.Nullable
    private android.view.contentcapture.ViewNode mNode;

    @android.annotation.Nullable
    private java.lang.CharSequence mText;

    private int mParentSessionId = android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID;

    @android.annotation.Nullable
    private android.view.contentcapture.ContentCaptureContext mClientContext;

    /**
     *
     *
     * @unknown 
     */
    public ContentCaptureEvent(int sessionId, int type, long eventTime) {
        mSessionId = sessionId;
        mType = type;
        mEventTime = eventTime;
    }

    /**
     *
     *
     * @unknown 
     */
    public ContentCaptureEvent(int sessionId, int type) {
        this(sessionId, type, java.lang.System.currentTimeMillis());
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.contentcapture.ContentCaptureEvent setAutofillId(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        mId = com.android.internal.util.Preconditions.checkNotNull(id);
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.contentcapture.ContentCaptureEvent setAutofillIds(@android.annotation.NonNull
    java.util.ArrayList<android.view.autofill.AutofillId> ids) {
        mIds = com.android.internal.util.Preconditions.checkNotNull(ids);
        return this;
    }

    /**
     * Adds an autofill id to the this event, merging the single id into a list if necessary.
     *
     * @unknown 
     */
    public android.view.contentcapture.ContentCaptureEvent addAutofillId(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        com.android.internal.util.Preconditions.checkNotNull(id);
        if (mIds == null) {
            mIds = new java.util.ArrayList<>();
            if (mId == null) {
                android.util.Log.w(android.view.contentcapture.ContentCaptureEvent.TAG, ("addAutofillId(" + id) + ") called without an initial id");
            } else {
                mIds.add(mId);
                mId = null;
            }
        }
        mIds.add(id);
        return this;
    }

    /**
     * Used by {@link #TYPE_SESSION_STARTED} and {@link #TYPE_SESSION_FINISHED}.
     *
     * @unknown 
     */
    public android.view.contentcapture.ContentCaptureEvent setParentSessionId(int parentSessionId) {
        mParentSessionId = parentSessionId;
        return this;
    }

    /**
     * Used by {@link #TYPE_SESSION_STARTED} and {@link #TYPE_SESSION_FINISHED}.
     *
     * @unknown 
     */
    public android.view.contentcapture.ContentCaptureEvent setClientContext(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext clientContext) {
        mClientContext = clientContext;
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public int getSessionId() {
        return mSessionId;
    }

    /**
     * Used by {@link #TYPE_SESSION_STARTED} and {@link #TYPE_SESSION_FINISHED}.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public int getParentSessionId() {
        return mParentSessionId;
    }

    /**
     * Gets the {@link ContentCaptureContext} set calls to
     * {@link ContentCaptureSession#setContentCaptureContext(ContentCaptureContext)}.
     *
     * <p>Only set on {@link #TYPE_CONTEXT_UPDATED} events.
     */
    @android.annotation.Nullable
    public android.view.contentcapture.ContentCaptureContext getContentCaptureContext() {
        return mClientContext;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.view.contentcapture.ContentCaptureEvent setViewNode(@android.annotation.NonNull
    android.view.contentcapture.ViewNode node) {
        mNode = com.android.internal.util.Preconditions.checkNotNull(node);
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.view.contentcapture.ContentCaptureEvent setText(@android.annotation.Nullable
    java.lang.CharSequence text) {
        mText = text;
        return this;
    }

    /**
     * Gets the type of the event.
     *
     * @return one of {@link #TYPE_VIEW_APPEARED}, {@link #TYPE_VIEW_DISAPPEARED},
    {@link #TYPE_VIEW_TEXT_CHANGED}, {@link #TYPE_VIEW_TREE_APPEARING},
    {@link #TYPE_VIEW_TREE_APPEARED}, {@link #TYPE_CONTEXT_UPDATED},
    {@link #TYPE_SESSION_RESUMED}, or {@link #TYPE_SESSION_PAUSED}.
     */
    @android.view.contentcapture.ContentCaptureEvent.EventType
    public int getType() {
        return mType;
    }

    /**
     * Gets when the event was generated, in millis since epoch.
     */
    public long getEventTime() {
        return mEventTime;
    }

    /**
     * Gets the whole metadata of the node associated with the event.
     *
     * <p>Only set on {@link #TYPE_VIEW_APPEARED} events.
     */
    @android.annotation.Nullable
    public android.view.contentcapture.ViewNode getViewNode() {
        return mNode;
    }

    /**
     * Gets the {@link AutofillId} of the node associated with the event.
     *
     * <p>Only set on {@link #TYPE_VIEW_DISAPPEARED} (when the event contains just one node - if
     * it contains more than one, this method returns {@code null} and the actual ids should be
     * retrived by {@link #getIds()}) and {@link #TYPE_VIEW_TEXT_CHANGED} events.
     */
    @android.annotation.Nullable
    public android.view.autofill.AutofillId getId() {
        return mId;
    }

    /**
     * Gets the {@link AutofillId AutofillIds} of the nodes associated with the event.
     *
     * <p>Only set on {@link #TYPE_VIEW_DISAPPEARED}, when the event contains more than one node
     * (if it contains just one node, it's returned by {@link #getId()} instead.
     */
    @android.annotation.Nullable
    public java.util.List<android.view.autofill.AutofillId> getIds() {
        return mIds;
    }

    /**
     * Gets the current text of the node associated with the event.
     *
     * <p>Only set on {@link #TYPE_VIEW_TEXT_CHANGED} events.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getText() {
        return mText;
    }

    /**
     * Merges event of the same type, either {@link #TYPE_VIEW_TEXT_CHANGED}
     * or {@link #TYPE_VIEW_DISAPPEARED}.
     *
     * @unknown 
     */
    public void mergeEvent(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureEvent event) {
        com.android.internal.util.Preconditions.checkNotNull(event);
        final int eventType = event.getType();
        if (mType != eventType) {
            android.util.Log.e(android.view.contentcapture.ContentCaptureEvent.TAG, ((("mergeEvent(" + android.view.contentcapture.ContentCaptureEvent.getTypeAsString(eventType)) + ") cannot be merged ") + "with different eventType=") + android.view.contentcapture.ContentCaptureEvent.getTypeAsString(mType));
            return;
        }
        if (eventType == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED) {
            final java.util.List<android.view.autofill.AutofillId> ids = event.getIds();
            final android.view.autofill.AutofillId id = event.getId();
            if (ids != null) {
                if (id != null) {
                    android.util.Log.w(android.view.contentcapture.ContentCaptureEvent.TAG, "got TYPE_VIEW_DISAPPEARED event with both id and ids: " + event);
                }
                for (int i = 0; i < ids.size(); i++) {
                    addAutofillId(ids.get(i));
                }
                return;
            }
            if (id != null) {
                addAutofillId(id);
                return;
            }
            throw new java.lang.IllegalArgumentException(("mergeEvent(): got " + "TYPE_VIEW_DISAPPEARED event with neither id or ids: ") + event);
        } else
            if (eventType == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED) {
                setText(event.getText());
            } else {
                android.util.Log.e(android.view.contentcapture.ContentCaptureEvent.TAG, ("mergeEvent(" + android.view.contentcapture.ContentCaptureEvent.getTypeAsString(eventType)) + ") does not support this event type.");
            }

    }

    /**
     *
     *
     * @unknown 
     */
    public void dump(@android.annotation.NonNull
    java.io.PrintWriter pw) {
        pw.print("type=");
        pw.print(android.view.contentcapture.ContentCaptureEvent.getTypeAsString(mType));
        pw.print(", time=");
        pw.print(mEventTime);
        if (mId != null) {
            pw.print(", id=");
            pw.print(mId);
        }
        if (mIds != null) {
            pw.print(", ids=");
            pw.print(mIds);
        }
        if (mNode != null) {
            pw.print(", mNode.id=");
            pw.print(mNode.getAutofillId());
        }
        if (mSessionId != android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID) {
            pw.print(", sessionId=");
            pw.print(mSessionId);
        }
        if (mParentSessionId != android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID) {
            pw.print(", parentSessionId=");
            pw.print(mParentSessionId);
        }
        if (mText != null) {
            pw.print(", text=");
            pw.println(android.view.contentcapture.ContentCaptureHelper.getSanitizedString(mText));
        }
        if (mClientContext != null) {
            pw.print(", context=");
            mClientContext.dump(pw);
            pw.println();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder string = new java.lang.StringBuilder("ContentCaptureEvent[type=").append(android.view.contentcapture.ContentCaptureEvent.getTypeAsString(mType));
        string.append(", session=").append(mSessionId);
        if ((mType == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED) && (mParentSessionId != android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID)) {
            string.append(", parent=").append(mParentSessionId);
        }
        if (mId != null) {
            string.append(", id=").append(mId);
        }
        if (mIds != null) {
            string.append(", ids=").append(mIds);
        }
        if (mNode != null) {
            final java.lang.String className = mNode.getClassName();
            if (mNode != null) {
                string.append(", class=").append(className);
            }
            string.append(", id=").append(mNode.getAutofillId());
        }
        if (mText != null) {
            string.append(", text=").append(android.view.contentcapture.ContentCaptureHelper.getSanitizedString(mText));
        }
        if (mClientContext != null) {
            string.append(", context=").append(mClientContext);
        }
        return string.append(']').toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mSessionId);
        parcel.writeInt(mType);
        parcel.writeLong(mEventTime);
        parcel.writeParcelable(mId, flags);
        parcel.writeTypedList(mIds);
        android.view.contentcapture.ViewNode.writeToParcel(parcel, mNode, flags);
        parcel.writeCharSequence(mText);
        if ((mType == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED) || (mType == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_FINISHED)) {
            parcel.writeInt(mParentSessionId);
        }
        if ((mType == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED) || (mType == android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED)) {
            parcel.writeParcelable(mClientContext, flags);
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureEvent> CREATOR = new android.os.Parcelable.Creator<android.view.contentcapture.ContentCaptureEvent>() {
        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.ContentCaptureEvent createFromParcel(android.os.Parcel parcel) {
            final int sessionId = parcel.readInt();
            final int type = parcel.readInt();
            final long eventTime = parcel.readLong();
            final android.view.contentcapture.ContentCaptureEvent event = new android.view.contentcapture.ContentCaptureEvent(sessionId, type, eventTime);
            final android.view.autofill.AutofillId id = parcel.readParcelable(null);
            if (id != null) {
                event.setAutofillId(id);
            }
            final ArrayList<android.view.autofill.AutofillId> ids = parcel.createTypedArrayList(AutofillId.CREATOR);
            if (ids != null) {
                event.setAutofillIds(ids);
            }
            final android.view.contentcapture.ViewNode node = android.view.contentcapture.ViewNode.readFromParcel(parcel);
            if (node != null) {
                event.setViewNode(node);
            }
            event.setText(parcel.readCharSequence());
            if ((type == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED) || (type == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_FINISHED)) {
                event.setParentSessionId(parcel.readInt());
            }
            if ((type == android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED) || (type == android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED)) {
                event.setClientContext(parcel.readParcelable(null));
            }
            return event;
        }

        @java.lang.Override
        @android.annotation.NonNull
        public android.view.contentcapture.ContentCaptureEvent[] newArray(int size) {
            return new android.view.contentcapture.ContentCaptureEvent[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getTypeAsString(@android.view.contentcapture.ContentCaptureEvent.EventType
    int type) {
        switch (type) {
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED :
                return "SESSION_STARTED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_FINISHED :
                return "SESSION_FINISHED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_RESUMED :
                return "SESSION_RESUMED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_PAUSED :
                return "SESSION_PAUSED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_APPEARED :
                return "VIEW_APPEARED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED :
                return "VIEW_DISAPPEARED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED :
                return "VIEW_TEXT_CHANGED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARING :
                return "VIEW_TREE_APPEARING";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARED :
                return "VIEW_TREE_APPEARED";
            case android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED :
                return "CONTEXT_UPDATED";
            default :
                return "UKNOWN_TYPE: " + type;
        }
    }
}

