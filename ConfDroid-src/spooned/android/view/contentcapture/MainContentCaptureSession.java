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
 * Main session associated with a context.
 *
 * <p>This session is created when the activity starts and finished when it stops; clients can use
 * it to create children activities.
 *
 * @unknown 
 */
public final class MainContentCaptureSession extends android.view.contentcapture.ContentCaptureSession {
    private static final java.lang.String TAG = android.view.contentcapture.MainContentCaptureSession.class.getSimpleName();

    // For readability purposes...
    private static final boolean FORCE_FLUSH = true;

    /**
     * Handler message used to flush the buffer.
     */
    private static final int MSG_FLUSH = 1;

    /**
     * Name of the {@link IResultReceiver} extra used to pass the binder interface to the service.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_BINDER = "binder";

    /**
     * Name of the {@link IResultReceiver} extra used to pass the content capture enabled state.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_ENABLED_STATE = "enabled";

    @android.annotation.NonNull
    private final java.util.concurrent.atomic.AtomicBoolean mDisabled = new java.util.concurrent.atomic.AtomicBoolean(false);

    @android.annotation.NonNull
    private final android.content.Context mContext;

    @android.annotation.NonNull
    private final android.view.contentcapture.ContentCaptureManager mManager;

    @android.annotation.NonNull
    private final android.os.Handler mHandler;

    /**
     * Interface to the system_server binder object - it's only used to start the session (and
     * notify when the session is finished).
     */
    @android.annotation.NonNull
    private final android.view.contentcapture.IContentCaptureManager mSystemServerInterface;

    /**
     * Direct interface to the service binder object - it's used to send the events, including the
     * last ones (when the session is finished)
     */
    @android.annotation.NonNull
    private android.view.contentcapture.IContentCaptureDirectManager mDirectServiceInterface;

    @android.annotation.Nullable
    private android.os.IBinder.DeathRecipient mDirectServiceVulture;

    private int mState = android.view.contentcapture.ContentCaptureSession.UNKNOWN_STATE;

    @android.annotation.Nullable
    private android.os.IBinder mApplicationToken;

    @android.annotation.Nullable
    private android.content.ComponentName mComponentName;

    /**
     * List of events held to be sent as a batch.
     */
    @android.annotation.Nullable
    private java.util.ArrayList<android.view.contentcapture.ContentCaptureEvent> mEvents;

    // Used just for debugging purposes (on dump)
    private long mNextFlush;

    /**
     * Whether the next buffer flush is queued by a text changed event.
     */
    private boolean mNextFlushForTextChanged = false;

    @android.annotation.Nullable
    private final android.util.LocalLog mFlushHistory;

    /**
     * Binder object used to update the session state.
     */
    @android.annotation.NonNull
    private final IResultReceiver.Stub mSessionStateReceiver;

    protected MainContentCaptureSession(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.view.contentcapture.ContentCaptureManager manager, @android.annotation.NonNull
    android.os.Handler handler, @android.annotation.NonNull
    android.view.contentcapture.IContentCaptureManager systemServerInterface) {
        mContext = context;
        mManager = manager;
        mHandler = handler;
        mSystemServerInterface = systemServerInterface;
        final int logHistorySize = mManager.mOptions.logHistorySize;
        mFlushHistory = (logHistorySize > 0) ? new android.util.LocalLog(logHistorySize) : null;
        mSessionStateReceiver = new com.android.internal.os.IResultReceiver.Stub() {
            @java.lang.Override
            public void send(int resultCode, android.os.Bundle resultData) {
                final android.os.IBinder binder;
                if (resultData != null) {
                    // Change in content capture enabled.
                    final boolean hasEnabled = resultData.getBoolean(android.view.contentcapture.MainContentCaptureSession.EXTRA_ENABLED_STATE);
                    if (hasEnabled) {
                        final boolean disabled = resultCode == android.view.contentcapture.ContentCaptureManager.RESULT_CODE_FALSE;
                        mDisabled.set(disabled);
                        return;
                    }
                    binder = resultData.getBinder(android.view.contentcapture.MainContentCaptureSession.EXTRA_BINDER);
                    if (binder == null) {
                        android.util.Log.wtf(android.view.contentcapture.MainContentCaptureSession.TAG, ("No " + android.view.contentcapture.MainContentCaptureSession.EXTRA_BINDER) + " extra result");
                        mHandler.post(() -> resetSession(android.view.contentcapture.ContentCaptureSession.STATE_DISABLED | android.view.contentcapture.ContentCaptureSession.STATE_INTERNAL_ERROR));
                        return;
                    }
                } else {
                    binder = null;
                }
                mHandler.post(() -> onSessionStarted(resultCode, binder));
            }
        };
    }

    @java.lang.Override
    android.view.contentcapture.MainContentCaptureSession getMainCaptureSession() {
        return this;
    }

    @java.lang.Override
    android.view.contentcapture.ContentCaptureSession newChild(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext clientContext) {
        final android.view.contentcapture.ContentCaptureSession child = new android.view.contentcapture.ChildContentCaptureSession(this, clientContext);
        notifyChildSessionStarted(mId, child.mId, clientContext);
        return child;
    }

    /**
     * Starts this session.
     */
    @android.annotation.UiThread
    void start(@android.annotation.NonNull
    android.os.IBinder token, @android.annotation.NonNull
    android.content.ComponentName component, int flags) {
        if (!isContentCaptureEnabled())
            return;

        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (("start(): token=" + token) + ", comp=") + android.content.ComponentName.flattenToShortString(component));
        }
        if (hasStarted()) {
            // TODO(b/122959591): make sure this is expected (and when), or use Log.w
            if (android.view.contentcapture.ContentCaptureHelper.sDebug) {
                android.util.Log.d(android.view.contentcapture.MainContentCaptureSession.TAG, (((("ignoring handleStartSession(" + token) + "/") + android.content.ComponentName.flattenToShortString(component)) + " while on state ") + android.view.contentcapture.ContentCaptureSession.getStateAsString(mState));
            }
            return;
        }
        mState = android.view.contentcapture.ContentCaptureSession.STATE_WAITING_FOR_SERVER;
        mApplicationToken = token;
        mComponentName = component;
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (((("handleStartSession(): token=" + token) + ", act=") + getDebugState()) + ", id=") + mId);
        }
        try {
            mSystemServerInterface.startSession(mApplicationToken, component, mId, flags, mSessionStateReceiver);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.view.contentcapture.MainContentCaptureSession.TAG, (("Error starting session for " + component.flattenToShortString()) + ": ") + e);
        }
    }

    @java.lang.Override
    void onDestroy() {
        mHandler.removeMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH);
        mHandler.post(() -> destroySession());
    }

    /**
     * Callback from {@code system_server} after call to
     * {@link IContentCaptureManager#startSession(IBinder, ComponentName, String, int,
     * IResultReceiver)}.
     *
     * @param resultCode
     * 		session state
     * @param binder
     * 		handle to {@code IContentCaptureDirectManager}
     */
    @android.annotation.UiThread
    private void onSessionStarted(int resultCode, @android.annotation.Nullable
    android.os.IBinder binder) {
        if (binder != null) {
            mDirectServiceInterface = IContentCaptureDirectManager.Stub.asInterface(binder);
            mDirectServiceVulture = () -> {
                android.util.Log.w(TAG, ("Keeping session " + mId) + " when service died");
                mState = android.view.contentcapture.ContentCaptureSession.STATE_SERVICE_DIED;
                mDisabled.set(true);
            };
            try {
                binder.linkToDeath(mDirectServiceVulture, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.view.contentcapture.MainContentCaptureSession.TAG, (("Failed to link to death on " + binder) + ": ") + e);
            }
        }
        if ((resultCode & android.view.contentcapture.ContentCaptureSession.STATE_DISABLED) != 0) {
            resetSession(resultCode);
        } else {
            mState = resultCode;
            mDisabled.set(false);
        }
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (((((((((("handleSessionStarted() result: id=" + mId) + " resultCode=") + resultCode) + ", state=") + android.view.contentcapture.ContentCaptureSession.getStateAsString(mState)) + ", disabled=") + mDisabled.get()) + ", binder=") + binder) + ", events=") + (mEvents == null ? 0 : mEvents.size()));
        }
    }

    @android.annotation.UiThread
    private void sendEvent(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureEvent event) {
        /* forceFlush= */
        sendEvent(event, false);
    }

    @android.annotation.UiThread
    private void sendEvent(@android.annotation.NonNull
    android.view.contentcapture.ContentCaptureEvent event, boolean forceFlush) {
        final int eventType = event.getType();
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose)
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (("handleSendEvent(" + getDebugState()) + "): ") + event);

        if (((!hasStarted()) && (eventType != android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED)) && (eventType != android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED)) {
            // TODO(b/120494182): comment when this could happen (dialogs?)
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, ((("handleSendEvent(" + getDebugState()) + ", ") + android.view.contentcapture.ContentCaptureEvent.getTypeAsString(eventType)) + "): dropping because session not started yet");
            return;
        }
        if (mDisabled.get()) {
            // This happens when the event was queued in the handler before the sesison was ready,
            // then handleSessionStarted() returned and set it as disabled - we need to drop it,
            // otherwise it will keep triggering handleScheduleFlush()
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose)
                android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, "handleSendEvent(): ignoring when disabled");

            return;
        }
        final int maxBufferSize = mManager.mOptions.maxBufferSize;
        if (mEvents == null) {
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, ("handleSendEvent(): creating buffer for " + maxBufferSize) + " events");
            }
            mEvents = new java.util.ArrayList<>(maxBufferSize);
        }
        // Some type of events can be merged together
        boolean addEvent = true;
        if ((!mEvents.isEmpty()) && (eventType == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED)) {
            final android.view.contentcapture.ContentCaptureEvent lastEvent = mEvents.get(mEvents.size() - 1);
            // TODO(b/121045053): check if flags match
            if ((lastEvent.getType() == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED) && lastEvent.getId().equals(event.getId())) {
                if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                    android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, "Buffering VIEW_TEXT_CHANGED event, updated text=" + android.view.contentcapture.ContentCaptureHelper.getSanitizedString(event.getText()));
                }
                lastEvent.mergeEvent(event);
                addEvent = false;
            }
        }
        if ((!mEvents.isEmpty()) && (eventType == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED)) {
            final android.view.contentcapture.ContentCaptureEvent lastEvent = mEvents.get(mEvents.size() - 1);
            if ((lastEvent.getType() == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED) && (event.getSessionId() == lastEvent.getSessionId())) {
                if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                    android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, "Buffering TYPE_VIEW_DISAPPEARED events for session " + lastEvent.getSessionId());
                }
                lastEvent.mergeEvent(event);
                addEvent = false;
            }
        }
        if (addEvent) {
            mEvents.add(event);
        }
        final int numberEvents = mEvents.size();
        final boolean bufferEvent = numberEvents < maxBufferSize;
        if (bufferEvent && (!forceFlush)) {
            final int flushReason;
            if (eventType == android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED) {
                mNextFlushForTextChanged = true;
                flushReason = android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_TEXT_CHANGE_TIMEOUT;
            } else {
                if (mNextFlushForTextChanged) {
                    if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                        android.util.Log.i(android.view.contentcapture.MainContentCaptureSession.TAG, "Not scheduling flush because next flush is for text changed");
                    }
                    return;
                }
                flushReason = android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_IDLE_TIMEOUT;
            }
            /* checkExisting= */
            scheduleFlush(flushReason, true);
            return;
        }
        if ((mState != android.view.contentcapture.ContentCaptureSession.STATE_ACTIVE) && (numberEvents >= maxBufferSize)) {
            // Callback from startSession hasn't been called yet - typically happens on system
            // apps that are started before the system service
            // TODO(b/122959591): try to ignore session while system is not ready / boot
            // not complete instead. Similarly, the manager service should return right away
            // when the user does not have a service set
            if (android.view.contentcapture.ContentCaptureHelper.sDebug) {
                android.util.Log.d(android.view.contentcapture.MainContentCaptureSession.TAG, ((("Closing session for " + getDebugState()) + " after ") + numberEvents) + " delayed events");
            }
            resetSession(android.view.contentcapture.ContentCaptureSession.STATE_DISABLED | android.view.contentcapture.ContentCaptureSession.STATE_NO_RESPONSE);
            // TODO(b/111276913): blacklist activity / use special flag to indicate that
            // when it's launched again
            return;
        }
        final int flushReason;
        switch (eventType) {
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED :
                flushReason = android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_STARTED;
                break;
            case android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_FINISHED :
                flushReason = android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_SESSION_FINISHED;
                break;
            default :
                flushReason = android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_FULL;
        }
        flush(flushReason);
    }

    @android.annotation.UiThread
    private boolean hasStarted() {
        return mState != android.view.contentcapture.ContentCaptureSession.UNKNOWN_STATE;
    }

    @android.annotation.UiThread
    private void scheduleFlush(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason, boolean checkExisting) {
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (("handleScheduleFlush(" + getDebugState(reason)) + ", checkExisting=") + checkExisting);
        }
        if (!hasStarted()) {
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose)
                android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, "handleScheduleFlush(): session not started yet");

            return;
        }
        if (mDisabled.get()) {
            // Should not be called on this state, as handleSendEvent checks.
            // But we rather add one if check and log than re-schedule and keep the session alive...
            android.util.Log.e(android.view.contentcapture.MainContentCaptureSession.TAG, ((("handleScheduleFlush(" + getDebugState(reason)) + "): should not be called ") + "when disabled. events=") + (mEvents == null ? null : mEvents.size()));
            return;
        }
        if (checkExisting && mHandler.hasMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH)) {
            // "Renew" the flush message by removing the previous one
            mHandler.removeMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH);
        }
        final int flushFrequencyMs;
        if (reason == android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_IDLE_TIMEOUT) {
            flushFrequencyMs = mManager.mOptions.idleFlushingFrequencyMs;
        } else
            if (reason == android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_TEXT_CHANGE_TIMEOUT) {
                flushFrequencyMs = mManager.mOptions.textChangeFlushingFrequencyMs;
            } else {
                android.util.Log.e(android.view.contentcapture.MainContentCaptureSession.TAG, (("handleScheduleFlush(" + getDebugState(reason)) + "): not called with a ") + "timeout reason.");
                return;
            }

        mNextFlush = java.lang.System.currentTimeMillis() + flushFrequencyMs;
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (("handleScheduleFlush(): scheduled to flush in " + flushFrequencyMs) + "ms: ") + android.util.TimeUtils.logTimeOfDay(mNextFlush));
        }
        // Post using a Runnable directly to trim a few μs from PooledLambda.obtainMessage()
        mHandler.postDelayed(() -> flushIfNeeded(reason), android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH, flushFrequencyMs);
    }

    @android.annotation.UiThread
    private void flushIfNeeded(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason) {
        if ((mEvents == null) || mEvents.isEmpty()) {
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose)
                android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, "Nothing to flush");

            return;
        }
        flush(reason);
    }

    @java.lang.Override
    @android.annotation.UiThread
    void flush(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason) {
        if (mEvents == null)
            return;

        if (mDisabled.get()) {
            android.util.Log.e(android.view.contentcapture.MainContentCaptureSession.TAG, (("handleForceFlush(" + getDebugState(reason)) + "): should not be when ") + "disabled");
            return;
        }
        if (mDirectServiceInterface == null) {
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
                android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, ((("handleForceFlush(" + getDebugState(reason)) + "): hold your horses, ") + "client not ready: ") + mEvents);
            }
            if (!mHandler.hasMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH)) {
                /* checkExisting= */
                scheduleFlush(reason, false);
            }
            return;
        }
        final int numberEvents = mEvents.size();
        final java.lang.String reasonString = android.view.contentcapture.ContentCaptureSession.getFlushReasonAsString(reason);
        if (android.view.contentcapture.ContentCaptureHelper.sDebug) {
            android.util.Log.d(android.view.contentcapture.MainContentCaptureSession.TAG, (("Flushing " + numberEvents) + " event(s) for ") + getDebugState(reason));
        }
        if (mFlushHistory != null) {
            // Logs reason, size, max size, idle timeout
            final java.lang.String logRecord = (((((("r=" + reasonString) + " s=") + numberEvents) + " m=") + mManager.mOptions.maxBufferSize) + " i=") + mManager.mOptions.idleFlushingFrequencyMs;
            mFlushHistory.log(logRecord);
        }
        try {
            mHandler.removeMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH);
            if (reason == android.view.contentcapture.ContentCaptureSession.FLUSH_REASON_TEXT_CHANGE_TIMEOUT) {
                mNextFlushForTextChanged = false;
            }
            final android.content.pm.ParceledListSlice<android.view.contentcapture.ContentCaptureEvent> events = clearEvents();
            mDirectServiceInterface.sendEvents(events, reason, mManager.mOptions);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.view.contentcapture.MainContentCaptureSession.TAG, (((("Error sending " + numberEvents) + " for ") + getDebugState()) + ": ") + e);
        }
    }

    @java.lang.Override
    public void updateContentCaptureContext(@android.annotation.Nullable
    android.view.contentcapture.ContentCaptureContext context) {
        notifyContextUpdated(mId, context);
    }

    /**
     * Resets the buffer and return a {@link ParceledListSlice} with the previous events.
     */
    @android.annotation.NonNull
    @android.annotation.UiThread
    private android.content.pm.ParceledListSlice<android.view.contentcapture.ContentCaptureEvent> clearEvents() {
        // NOTE: we must save a reference to the current mEvents and then set it to to null,
        // otherwise clearing it would clear it in the receiving side if the service is also local.
        final java.util.List<android.view.contentcapture.ContentCaptureEvent> events = (mEvents == null) ? java.util.Collections.emptyList() : mEvents;
        mEvents = null;
        return new android.content.pm.ParceledListSlice(events);
    }

    @android.annotation.UiThread
    private void destroySession() {
        if (android.view.contentcapture.ContentCaptureHelper.sDebug) {
            android.util.Log.d(android.view.contentcapture.MainContentCaptureSession.TAG, (((((("Destroying session (ctx=" + mContext) + ", id=") + mId) + ") with ") + (mEvents == null ? 0 : mEvents.size())) + " event(s) for ") + getDebugState());
        }
        try {
            mSystemServerInterface.finishSession(mId);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.view.contentcapture.MainContentCaptureSession.TAG, (((("Error destroying system-service session " + mId) + " for ") + getDebugState()) + ": ") + e);
        }
    }

    // TODO(b/122454205): once we support multiple sessions, we might need to move some of these
    // clearings out.
    @android.annotation.UiThread
    private void resetSession(int newState) {
        if (android.view.contentcapture.ContentCaptureHelper.sVerbose) {
            android.util.Log.v(android.view.contentcapture.MainContentCaptureSession.TAG, (((("handleResetSession(" + getActivityName()) + "): from ") + android.view.contentcapture.ContentCaptureSession.getStateAsString(mState)) + " to ") + android.view.contentcapture.ContentCaptureSession.getStateAsString(newState));
        }
        mState = newState;
        mDisabled.set((newState & android.view.contentcapture.ContentCaptureSession.STATE_DISABLED) != 0);
        // TODO(b/122454205): must reset children (which currently is owned by superclass)
        mApplicationToken = null;
        mComponentName = null;
        mEvents = null;
        if (mDirectServiceInterface != null) {
            mDirectServiceInterface.asBinder().unlinkToDeath(mDirectServiceVulture, 0);
        }
        mDirectServiceInterface = null;
        mHandler.removeMessages(android.view.contentcapture.MainContentCaptureSession.MSG_FLUSH);
    }

    @java.lang.Override
    void internalNotifyViewAppeared(@android.annotation.NonNull
    android.view.contentcapture.ViewNode.ViewStructureImpl node) {
        notifyViewAppeared(mId, node);
    }

    @java.lang.Override
    void internalNotifyViewDisappeared(@android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        notifyViewDisappeared(mId, id);
    }

    @java.lang.Override
    void internalNotifyViewTextChanged(@android.annotation.NonNull
    android.view.autofill.AutofillId id, @android.annotation.Nullable
    java.lang.CharSequence text) {
        notifyViewTextChanged(mId, id, text);
    }

    @java.lang.Override
    public void internalNotifyViewTreeEvent(boolean started) {
        notifyViewTreeEvent(mId, started);
    }

    @java.lang.Override
    boolean isContentCaptureEnabled() {
        return super.isContentCaptureEnabled() && mManager.isContentCaptureEnabled();
    }

    // Called by ContentCaptureManager.isContentCaptureEnabled
    boolean isDisabled() {
        return mDisabled.get();
    }

    /**
     * Sets the disabled state of content capture.
     *
     * @return whether disabled state was changed.
     */
    boolean setDisabled(boolean disabled) {
        return mDisabled.compareAndSet(!disabled, disabled);
    }

    // TODO(b/122454205): refactor "notifyXXXX" methods below to a common "Buffer" object that is
    // shared between ActivityContentCaptureSession and ChildContentCaptureSession objects. Such
    // change should also get get rid of the "internalNotifyXXXX" methods above
    void notifyChildSessionStarted(int parentSessionId, int childSessionId, @android.annotation.NonNull
    android.view.contentcapture.ContentCaptureContext clientContext) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(childSessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_STARTED).setParentSessionId(parentSessionId).setClientContext(clientContext), android.view.contentcapture.MainContentCaptureSession.FORCE_FLUSH);
    }

    void notifyChildSessionFinished(int parentSessionId, int childSessionId) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(childSessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_FINISHED).setParentSessionId(parentSessionId), android.view.contentcapture.MainContentCaptureSession.FORCE_FLUSH);
    }

    void notifyViewAppeared(int sessionId, @android.annotation.NonNull
    android.view.contentcapture.ViewNode.ViewStructureImpl node) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(sessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_APPEARED).setViewNode(node.mNode));
    }

    /**
     * Public because is also used by ViewRootImpl
     */
    public void notifyViewDisappeared(int sessionId, @android.annotation.NonNull
    android.view.autofill.AutofillId id) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(sessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_DISAPPEARED).setAutofillId(id));
    }

    void notifyViewTextChanged(int sessionId, @android.annotation.NonNull
    android.view.autofill.AutofillId id, @android.annotation.Nullable
    java.lang.CharSequence text) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(sessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TEXT_CHANGED).setAutofillId(id).setText(text));
    }

    /**
     * Public because is also used by ViewRootImpl
     */
    public void notifyViewTreeEvent(int sessionId, boolean started) {
        final int type = (started) ? android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARING : android.view.contentcapture.ContentCaptureEvent.TYPE_VIEW_TREE_APPEARED;
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(sessionId, type), android.view.contentcapture.MainContentCaptureSession.FORCE_FLUSH);
    }

    /**
     * Public because is also used by ViewRootImpl
     */
    public void notifySessionLifecycle(boolean started) {
        final int type = (started) ? android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_RESUMED : android.view.contentcapture.ContentCaptureEvent.TYPE_SESSION_PAUSED;
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(mId, type), android.view.contentcapture.MainContentCaptureSession.FORCE_FLUSH);
    }

    void notifyContextUpdated(int sessionId, @android.annotation.Nullable
    android.view.contentcapture.ContentCaptureContext context) {
        sendEvent(new android.view.contentcapture.ContentCaptureEvent(sessionId, android.view.contentcapture.ContentCaptureEvent.TYPE_CONTEXT_UPDATED).setClientContext(context));
    }

    @java.lang.Override
    void dump(@android.annotation.NonNull
    java.lang.String prefix, @android.annotation.NonNull
    java.io.PrintWriter pw) {
        super.dump(prefix, pw);
        pw.print(prefix);
        pw.print("mContext: ");
        pw.println(mContext);
        pw.print(prefix);
        pw.print("user: ");
        pw.println(mContext.getUserId());
        if (mDirectServiceInterface != null) {
            pw.print(prefix);
            pw.print("mDirectServiceInterface: ");
            pw.println(mDirectServiceInterface);
        }
        pw.print(prefix);
        pw.print("mDisabled: ");
        pw.println(mDisabled.get());
        pw.print(prefix);
        pw.print("isEnabled(): ");
        pw.println(isContentCaptureEnabled());
        pw.print(prefix);
        pw.print("state: ");
        pw.println(android.view.contentcapture.ContentCaptureSession.getStateAsString(mState));
        if (mApplicationToken != null) {
            pw.print(prefix);
            pw.print("app token: ");
            pw.println(mApplicationToken);
        }
        if (mComponentName != null) {
            pw.print(prefix);
            pw.print("component name: ");
            pw.println(mComponentName.flattenToShortString());
        }
        if ((mEvents != null) && (!mEvents.isEmpty())) {
            final int numberEvents = mEvents.size();
            pw.print(prefix);
            pw.print("buffered events: ");
            pw.print(numberEvents);
            pw.print('/');
            pw.println(mManager.mOptions.maxBufferSize);
            if (android.view.contentcapture.ContentCaptureHelper.sVerbose && (numberEvents > 0)) {
                final java.lang.String prefix3 = prefix + "  ";
                for (int i = 0; i < numberEvents; i++) {
                    final android.view.contentcapture.ContentCaptureEvent event = mEvents.get(i);
                    pw.print(prefix3);
                    pw.print(i);
                    pw.print(": ");
                    event.dump(pw);
                    pw.println();
                }
            }
            pw.print(prefix);
            pw.print("mNextFlushForTextChanged: ");
            pw.println(mNextFlushForTextChanged);
            pw.print(prefix);
            pw.print("flush frequency: ");
            if (mNextFlushForTextChanged) {
                pw.println(mManager.mOptions.textChangeFlushingFrequencyMs);
            } else {
                pw.println(mManager.mOptions.idleFlushingFrequencyMs);
            }
            pw.print(prefix);
            pw.print("next flush: ");
            android.util.TimeUtils.formatDuration(mNextFlush - java.lang.System.currentTimeMillis(), pw);
            pw.print(" (");
            pw.print(android.util.TimeUtils.logTimeOfDay(mNextFlush));
            pw.println(")");
        }
        if (mFlushHistory != null) {
            pw.print(prefix);
            pw.println("flush history:");
            /* fd= */
            /* args= */
            mFlushHistory.reverseDump(null, pw, null);
            pw.println();
        } else {
            pw.print(prefix);
            pw.println("not logging flush history");
        }
        super.dump(prefix, pw);
    }

    /**
     * Gets a string that can be used to identify the activity on logging statements.
     */
    private java.lang.String getActivityName() {
        return mComponentName == null ? "pkg:" + mContext.getPackageName() : "act:" + mComponentName.flattenToShortString();
    }

    @android.annotation.NonNull
    private java.lang.String getDebugState() {
        return ((((getActivityName() + " [state=") + android.view.contentcapture.ContentCaptureSession.getStateAsString(mState)) + ", disabled=") + mDisabled.get()) + "]";
    }

    @android.annotation.NonNull
    private java.lang.String getDebugState(@android.view.contentcapture.ContentCaptureSession.FlushReason
    int reason) {
        return (getDebugState() + ", reason=") + android.view.contentcapture.ContentCaptureSession.getFlushReasonAsString(reason);
    }
}

