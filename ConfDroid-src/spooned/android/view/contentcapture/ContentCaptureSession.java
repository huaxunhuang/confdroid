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
 * Session used when notifying the Android system about events associated with views.
 */
public abstract class ContentCaptureSession implements java.lang.AutoCloseable {
    private static final java.lang.String TAG = android.view.contentcapture.ContentCaptureSession.class.getSimpleName();

    private static final java.util.Random sIdGenerator = new java.util.Random();

    /**
     *
     *
     * @unknown 
     */
    public static final int NO_SESSION_ID = 0;

    /**
     * Initial state, when there is no session.
     *
     * @unknown 
     */
    // NOTE: not prefixed by STATE_ so it's not printed on getStateAsString()
    public static final int UNKNOWN_STATE = 0x0;

    /**
     * Service's startSession() was called, but server didn't confirm it was created yet.
     *
     * @unknown 
     */
    public static final int STATE_WAITING_FOR_SERVER = 0x1;

    /**
     * Session is active.
     *
     * @unknown 
     */
    public static final int STATE_ACTIVE = 0x2;

    /**
     * Session is disabled because there is no service for this user.
     *
     * @unknown 
     */
    public static final int STATE_DISABLED = 0x4;

    /**
     * Session is disabled because its id already existed on server.
     *
     * @unknown 
     */
    public static final int STATE_DUPLICATED_ID = 0x8;

    /**
     * Session is disabled because service is not set for user.
     *
     * @unknown 
     */
    public static final int STATE_NO_SERVICE = 0x10;

    /**
     * Session is disabled by FLAG_SECURE
     *
     * @unknown 
     */
    public static final int STATE_FLAG_SECURE = 0x20;

    /**
     * Session is disabled manually by the specific app
     * (through {@link ContentCaptureManager#setContentCaptureEnabled(boolean)}).
     *
     * @unknown 
     */
    public static final int STATE_BY_APP = 0x40;

    /**
     * Session is disabled because session start was never replied.
     *
     * @unknown 
     */
    public static final int STATE_NO_RESPONSE = 0x80;

    /**
     * Session is disabled because an internal error.
     *
     * @unknown 
     */
    public static final int STATE_INTERNAL_ERROR = 0x100;

    /**
     * Session is disabled because service didn't whitelist package or activity.
     *
     * @unknown 
     */
    public static final int STATE_NOT_WHITELISTED = 0x200;

    /**
     * Session is disabled because the service died.
     *
     * @unknown 
     */
    public static final int STATE_SERVICE_DIED = 0x400;

    /**
     * Session is disabled because the service package is being udpated.
     *
     * @unknown 
     */
    public static final int STATE_SERVICE_UPDATING = 0x800;

    /**
     * Session is enabled, after the service died and came back to live.
     *
     * @unknown 
     */
    public static final int STATE_SERVICE_RESURRECTED = 0x1000;

    private static final int INITIAL_CHILDREN_CAPACITY = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_FULL = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_VIEW_ROOT_ENTERED = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_SESSION_STARTED = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_SESSION_FINISHED = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_IDLE_TIMEOUT = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int FLUSH_REASON_TEXT_CHANGE_TIMEOUT = 6;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "FLUSH_REASON_" }, value = { android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_FULL, android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_VIEW_ROOT_ENTERED, android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_STARTED, android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_FINISHED, android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_IDLE_TIMEOUT, android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_TEXT_CHANGE_TIMEOUT })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FlushReason {}

    private final java.lang.Object mLock = new java.lang.Object();

    /**
     * Guard use to ignore events after it's destroyed.
     */
    @android.annotation.NonNull
    @com.android.internal.annotations.GuardedBy("mLock")
    private boolean mDestroyed;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.Nullable
    protected final int mId;

    private int mState = android.view.contentcapture.ContentCaptureSession.UNKNOWN_STATE;

    // Lazily created on demand.
    private android.view.contentcapture.ContentCaptureSessionId mContentCaptureSessionId;

    /**
     * {@link ContentCaptureContext} set by client, or {@code null} when it's the
     * {@link ContentCaptureManager#getMainContentCaptureSession() default session} for the
     * context.
     */
    @android.annotation.Nullable
    private android.view.contentcapture.ContentCaptureContext mClientContext;

    /**
     * List of children session.
     */
    @android.annotation.Nullable
    @com.android.internal.annotations.GuardedBy("mLock")
    private java.util.ArrayList<android.view.contentcapture.ContentCaptureSession> mChildren;

    /**
     *
     *
     * @unknown 
     */
    protected ContentCaptureSession() {
        this(android.view.contentcapture.ContentCaptureSession.getRandomSessionId());
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public ContentCaptureSession(int id) {
        com.android.internal.util.Preconditions.checkArgument(id != android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID);
        mId = id;
    }

    // Used by ChildCOntentCaptureSession
    ContentCaptureSession(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext initialContext) {
        this();
        mClientContext = com.android.internal.util.Preconditions.checkNotNull(initialContext);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    abstract android.view.contentcapture.MainContentCaptureSession getMainCaptureSession();

    /**
     * Gets the id used to identify this session.
     */
    @android.annotation.NonNull
    public final android.view.contentcapture.ContentCaptureSessionId getContentCaptureSessionId() {
        if (mContentCaptureSessionId == null) {
            mContentCaptureSessionId = new android.view.contentcapture.ContentCaptureSessionId(mId);
        }
        return mContentCaptureSessionId;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public int getId() {
        return mId;
    }

    /**
     * Creates a new {@link ContentCaptureSession}.
     *
     * <p>See {@link View#setContentCaptureSession(ContentCaptureSession)} for more info.
     */
    @android.annotation.NonNull
    public final android.view.contentcapture.ContentCaptureSession createContentCaptureSession(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext context) {
        final android.view.contentcapture.ContentCaptureSession child = newChild(context);
        if (android.view.contentcapture.ContentCaptureHelper.sDebug) {
            android.util.Log.d(android.view.contentcapture.ContentCaptureSession.TAG, (((("createContentCaptureSession(" + context) + ": parent=") + mId) + ", child=") + child.mId);
        }
        synchronized(mLock) {
            if (mChildren == null) {
                mChildren = new java.util.ArrayList<>(android.view.contentcapture.ContentCaptureSession.INITIAL_CHILDREN_CAPACITY);
            }
            mChildren.add(child);
        }
        return child;
    }

    abstract android.view.contentcapture.ContentCaptureSession newChild(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext context);

    /**
     * Flushes the buffered events to the service.
     */
    abstract void flush(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason);

    /**
     * Sets the {@link ContentCaptureContext} associated with the session.
     *
     * <p>Typically used to change the context associated with the default session from an activity.
     */
    public final void setContentCaptureContext(@android.annotation.Nullable
    android.view.contentcapture.ContentCaptureContext context) {
        mClientContext = context;
        updateContentCaptureContext(context);
    }

    abstract void updateContentCaptureContext(@android.annotation.Nullable
    android.view.contentcapture.ContentCaptureContext context);

    /**
     * Gets the {@link ContentCaptureContext} associated with the session.
     *
     * @return context set on constructor or by
    {@link #setContentCaptureContext(ContentCaptureContext)}, or {@code null} if never
    explicitly set.
     */
    @android.annotation.Nullable
    public final android.view.contentcapture.ContentCaptureContext getContentCaptureContext() {
        return mClientContext;
    }

    /**
     * Destroys this session, flushing out all pending notifications.
     *
     * <p>Once destroyed, any new notification will be dropped.
     */
    public final void destroy() {
        synchronized(mLock) {
            if (mDestroyed) {
                if (android.view.contentcapture.ContentCaptureHelper.sDebug)
                    android.util.Log.d(android.view.contentcapture.ContentCaptureSession.TAG, ("destroy(" + mId) + "): already destroyed");

                return;
            }
            mDestroyed = true;
            // TODO(b/111276913): check state (for example, how to handle if it's waiting for remote
            // id) and send it to the cache of batched commands
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                android.util.Log.v(android.view.contentcapture.ContentCaptureSession.TAG, (("destroy(): state=" + android.view.contentcapture.ContentCaptureSession.getStateAsString(mState)) + ", mId=") + mId);
            }
            // Finish children first
            if (mChildren != null) {
                final int numberChildren = mChildren.size();
                if (android.view.contentcapture.ContentCaptureHelper.sVerbose)
                    android.util.Log.v(android.view.contentcapture.ContentCaptureSession.TAG, ("Destroying " + numberChildren) + " children first");

                for (int i = 0; i < numberChildren; i++) {
                    final android.view.contentcapture.ContentCaptureSession child = mChildren.get(i);
                    try {
                        child.destroy();
                    } catch (java.lang.Exception e) {
                        android.util.Log.w(android.view.contentcapture.ContentCaptureSession.TAG, (("exception destroying child session #" + i) + ": ") + e);
                    }
                }
            }
        }
        try {
            flush(android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_FINISHED);
        } finally {
            onDestroy();
        }
    }

    abstract void onDestroy();

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void close() {
        destroy();
    }

    /**
     * Notifies the Android system that a node has been added to the view structure.
     *
     * @param node
     * 		node that has been added.
     */
    public final void notifyViewAppeared(@android.annotation.NonNull
    android.view.ViewStructure node) {
        com.android.internal.util.Preconditions.checkNotNull(node);
        if (!isContentCaptureEnabled())
            return;

        if (!(node instanceof android.view.contentcapture.ViewNode.ViewStructureImpl)) {
            throw new java.lang.IllegalArgumentException("Invalid node class: " + node.getClass());
        }
        internalNotifyViewAppeared(((android.view.contentcapture.ViewNode.ViewStructureImpl) (node)));
    }

    abstract void internalNotifyViewAppeared(@android.annotation.NonNull
    android.view.contentcapture.ViewNode.ViewStructureImpl node);

    /**
     * Notifies the Android system that a node has been removed from the view structure.
     *
     * @param id
     * 		id of the node that has been removed.
     */
    public final void notifyViewDisappeared(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        com.android.internal.util.Preconditions.checkNotNull(id);
        if (!isContentCaptureEnabled())
            return;

        internalNotifyViewDisappeared(id);
    }

    abstract void internalNotifyViewDisappeared(@android.annotation.NonNull
    android.view.autofill.AutofillId id);

    /**
     * Notifies the Android system that many nodes has been removed from a virtual view
     * structure.
     *
     * <p>Should only be called by views that handle their own virtual view hierarchy.
     *
     * @param hostId
     * 		id of the non-virtual view hosting the virtual view hierarchy (it can be
     * 		obtained by calling {@link ViewStructure#getAutofillId()}).
     * @param virtualIds
     * 		ids of the virtual children.
     * @throws IllegalArgumentException
     * 		if the {@code hostId} is an autofill id for a virtual view.
     * @throws IllegalArgumentException
     * 		if {@code virtualIds} is empty
     */
    public final void notifyViewsDisappeared(@android.annotation.NonNull
    android.view.autofill.AutofillId hostId, @android.annotation.NonNull
    long[] virtualIds) {
        com.android.internal.util.Preconditions.checkArgument(hostId.isNonVirtual(), "hostId cannot be virtual: %s", hostId);
        com.android.internal.util.Preconditions.checkArgument(!com.android.internal.util.ArrayUtils.isEmpty(virtualIds), "virtual ids cannot be empty");
        if (!isContentCaptureEnabled())
            return;

        // TODO(b/123036895): use a internalNotifyViewsDisappeared that optimizes how the event is
        // parcelized
        for (long id : virtualIds) {
            internalNotifyViewDisappeared(new android.view.autofill.AutofillId(hostId, id, mId));
        }
    }

    /**
     * Notifies the Android system that the value of a text node has been changed.
     *
     * @param id
     * 		of the node.
     * @param text
     * 		new text.
     */
    public final void notifyViewTextChanged(@android.annotation.NonNull
    android.view.autofill.AutofillId id, @android.annotation.Nullable
    java.lang.CharSequence text) {
        com.android.internal.util.Preconditions.checkNotNull(id);
        if (!isContentCaptureEnabled())
            return;

        internalNotifyViewTextChanged(id, text);
    }

    abstract void internalNotifyViewTextChanged(@android.annotation.NonNull
    android.view.autofill.AutofillId id, @android.annotation.Nullable
    java.lang.CharSequence text);

    /**
     *
     *
     * @unknown 
     */
    public abstract void internalNotifyViewTreeEvent(boolean started);

    /**
     * Creates a {@link ViewStructure} for a "standard" view.
     *
     * <p>This method should be called after a visible view is laid out; the view then must populate
     * the structure and pass it to {@link #notifyViewAppeared(ViewStructure)}.
     *
     * <b>Note: </b>views that manage a virtual structure under this view must populate just the
     * node representing this view and return right away, then asynchronously report (not
     * necessarily in the UI thread) when the children nodes appear, disappear or have their text
     * changed by calling {@link ContentCaptureSession#notifyViewAppeared(ViewStructure)},
     * {@link ContentCaptureSession#notifyViewDisappeared(AutofillId)}, and
     * {@link ContentCaptureSession#notifyViewTextChanged(AutofillId, CharSequence)} respectively.
     * The structure for the a child must be created using
     * {@link ContentCaptureSession#newVirtualViewStructure(AutofillId, long)}, and the
     * {@code autofillId} for a child can be obtained either through
     * {@code childStructure.getAutofillId()} or
     * {@link ContentCaptureSession#newAutofillId(AutofillId, long)}.
     *
     * <p>When the virtual view hierarchy represents a web page, you should also:
     *
     * <ul>
     * <li>Call {@link ContentCaptureManager#getContentCaptureConditions()} to infer content capture
     * events should be generate for that URL.
     * <li>Create a new {@link ContentCaptureSession} child for every HTML element that renders a
     * new URL (like an {@code IFRAME}) and use that session to notify events from that subtree.
     * </ul>
     *
     * <p><b>Note: </b>the following methods of the {@code structure} will be ignored:
     * <ul>
     * <li>{@link ViewStructure#setChildCount(int)}
     * <li>{@link ViewStructure#addChildCount(int)}
     * <li>{@link ViewStructure#getChildCount()}
     * <li>{@link ViewStructure#newChild(int)}
     * <li>{@link ViewStructure#asyncNewChild(int)}
     * <li>{@link ViewStructure#asyncCommit()}
     * <li>{@link ViewStructure#setWebDomain(String)}
     * <li>{@link ViewStructure#newHtmlInfoBuilder(String)}
     * <li>{@link ViewStructure#setHtmlInfo(android.view.ViewStructure.HtmlInfo)}
     * <li>{@link ViewStructure#setDataIsSensitive(boolean)}
     * <li>{@link ViewStructure#setAlpha(float)}
     * <li>{@link ViewStructure#setElevation(float)}
     * <li>{@link ViewStructure#setTransformation(android.graphics.Matrix)}
     * </ul>
     */
    @android.annotation.NonNull
    public final android.view.ViewStructure newViewStructure(@android.annotation.NonNull
    android.view.View view) {
        return new android.view.contentcapture.ViewNode.ViewStructureImpl(view);
    }

    /**
     * Creates a new {@link AutofillId} for a virtual child, so it can be used to uniquely identify
     * the children in the session.
     *
     * @param hostId
     * 		id of the non-virtual view hosting the virtual view hierarchy (it can be
     * 		obtained by calling {@link ViewStructure#getAutofillId()}).
     * @param virtualChildId
     * 		id of the virtual child, relative to the parent.
     * @return if for the virtual child
     * @throws IllegalArgumentException
     * 		if the {@code parentId} is a virtual child id.
     */
    @android.annotation.NonNull
    public android.view.autofill.AutofillId newAutofillId(@android.annotation.NonNull
    android.view.autofill.AutofillId hostId, long virtualChildId) {
        com.android.internal.util.Preconditions.checkNotNull(hostId);
        com.android.internal.util.Preconditions.checkArgument(hostId.isNonVirtual(), "hostId cannot be virtual: %s", hostId);
        return new android.view.autofill.AutofillId(hostId, virtualChildId, mId);
    }

    /**
     * Creates a {@link ViewStructure} for a "virtual" view, so it can be passed to
     * {@link #notifyViewAppeared(ViewStructure)} by the view managing the virtual view hierarchy.
     *
     * @param parentId
     * 		id of the virtual view parent (it can be obtained by calling
     * 		{@link ViewStructure#getAutofillId()} on the parent).
     * @param virtualId
     * 		id of the virtual child, relative to the parent.
     * @return a new {@link ViewStructure} that can be used for Content Capture purposes.
     */
    @android.annotation.NonNull
    public final android.view.ViewStructure newVirtualViewStructure(@android.annotation.NonNull
    android.view.autofill.AutofillId parentId, long virtualId) {
        return new android.view.contentcapture.ViewNode.ViewStructureImpl(parentId, virtualId, mId);
    }

    boolean isContentCaptureEnabled() {
        synchronized(mLock) {
            return !mDestroyed;
        }
    }

    @android.annotation.CallSuper
    void dump(@android.annotation.NonNull
    java.lang.String prefix, @android.annotation.NonNull
    java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("id: ");
        pw.println(mId);
        if (mClientContext != null) {
            pw.print(prefix);
            mClientContext.dump(pw);
            pw.println();
        }
        synchronized(mLock) {
            pw.print(prefix);
            pw.print("destroyed: ");
            pw.println(mDestroyed);
            if ((mChildren != null) && (!mChildren.isEmpty())) {
                final java.lang.String prefix2 = prefix + "  ";
                final int numberChildren = mChildren.size();
                pw.print(prefix);
                pw.print("number children: ");
                pw.println(numberChildren);
                for (int i = 0; i < numberChildren; i++) {
                    final android.view.contentcapture.ContentCaptureSession child = mChildren.get(i);
                    pw.print(prefix);
                    pw.print(i);
                    pw.println(": ");
                    child.dump(prefix2, pw);
                }
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.Integer.toString(mId);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    protected static java.lang.String getStateAsString(int state) {
        return ((state + " (") + (state == android.view.contentcapture.ContentCaptureSession.UNKNOWN_STATE ? "UNKNOWN" : android.util.DebugUtils.flagsToString(android.view.contentcapture.ContentCaptureSession.class, "STATE_", state))) + ")";
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public static java.lang.String getFlushReasonAsString(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason) {
        switch (reason) {
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_FULL :
                return "FULL";
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_VIEW_ROOT_ENTERED :
                return "VIEW_ROOT";
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_STARTED :
                return "STARTED";
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_FINISHED :
                return "FINISHED";
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_IDLE_TIMEOUT :
                return "IDLE";
            case android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_TEXT_CHANGE_TIMEOUT :
                return "TEXT_CHANGE";
            default :
                return "UNKOWN-" + reason;
        }
    }

    private static int getRandomSessionId() {
        int id;
        do {
            id = android.view.contentcapture.ContentCaptureSession.sIdGenerator.nextInt();
        } while (id == android.view.contentcapture.ContentCaptureSession.NO_SESSION_ID );
        return id;
    }
}

