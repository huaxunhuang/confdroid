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
 * Session-aware TextClassifier.
 */
@android.annotation.WorkerThread
final class TextClassificationSession implements android.view.textclassifier.TextClassifier {
    private static final java.lang.String LOG_TAG = "TextClassificationSession";

    private final android.view.textclassifier.TextClassifier mDelegate;

    private final android.view.textclassifier.TextClassificationSession.SelectionEventHelper mEventHelper;

    private final android.view.textclassifier.TextClassificationSessionId mSessionId;

    private final android.view.textclassifier.TextClassificationContext mClassificationContext;

    private boolean mDestroyed;

    TextClassificationSession(android.view.textclassifier.TextClassificationContext context, android.view.textclassifier.TextClassifier delegate) {
        mClassificationContext = com.android.internal.util.Preconditions.checkNotNull(context);
        mDelegate = com.android.internal.util.Preconditions.checkNotNull(delegate);
        mSessionId = new android.view.textclassifier.TextClassificationSessionId();
        mEventHelper = new android.view.textclassifier.TextClassificationSession.SelectionEventHelper(mSessionId, mClassificationContext);
        initializeRemoteSession();
    }

    @java.lang.Override
    public android.view.textclassifier.TextSelection suggestSelection(android.view.textclassifier.TextSelection.Request request) {
        checkDestroyed();
        return mDelegate.suggestSelection(request);
    }

    private void initializeRemoteSession() {
        if (mDelegate instanceof android.view.textclassifier.SystemTextClassifier) {
            ((android.view.textclassifier.SystemTextClassifier) (mDelegate)).initializeRemoteSession(mClassificationContext, mSessionId);
        }
    }

    @java.lang.Override
    public android.view.textclassifier.TextClassification classifyText(android.view.textclassifier.TextClassification.Request request) {
        checkDestroyed();
        return mDelegate.classifyText(request);
    }

    @java.lang.Override
    public android.view.textclassifier.TextLinks generateLinks(android.view.textclassifier.TextLinks.Request request) {
        checkDestroyed();
        return mDelegate.generateLinks(request);
    }

    @java.lang.Override
    public void onSelectionEvent(android.view.textclassifier.SelectionEvent event) {
        try {
            if (mEventHelper.sanitizeEvent(event)) {
                mDelegate.onSelectionEvent(event);
            }
        } catch (java.lang.Exception e) {
            // Avoid crashing for event reporting.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassificationSession.LOG_TAG, "Error reporting text classifier selection event", e);
        }
    }

    @java.lang.Override
    public void onTextClassifierEvent(android.view.textclassifier.TextClassifierEvent event) {
        try {
            event.mHiddenTempSessionId = mSessionId;
            mDelegate.onTextClassifierEvent(event);
        } catch (java.lang.Exception e) {
            // Avoid crashing for event reporting.
            android.view.textclassifier.Log.e(android.view.textclassifier.TextClassificationSession.LOG_TAG, "Error reporting text classifier event", e);
        }
    }

    @java.lang.Override
    public void destroy() {
        mEventHelper.endSession();
        mDelegate.destroy();
        mDestroyed = true;
    }

    @java.lang.Override
    public boolean isDestroyed() {
        return mDestroyed;
    }

    /**
     *
     *
     * @throws IllegalStateException
     * 		if this TextClassification session has been destroyed.
     * @see #isDestroyed()
     * @see #destroy()
     */
    private void checkDestroyed() {
        if (mDestroyed) {
            throw new java.lang.IllegalStateException("This TextClassification session has been destroyed");
        }
    }

    /**
     * Helper class for updating SelectionEvent fields.
     */
    private static final class SelectionEventHelper {
        private final android.view.textclassifier.TextClassificationSessionId mSessionId;

        private final android.view.textclassifier.TextClassificationContext mContext;

        @android.view.textclassifier.SelectionEvent.InvocationMethod
        private int mInvocationMethod = android.view.textclassifier.SelectionEvent.INVOCATION_UNKNOWN;

        private android.view.textclassifier.SelectionEvent mPrevEvent;

        private android.view.textclassifier.SelectionEvent mSmartEvent;

        private android.view.textclassifier.SelectionEvent mStartEvent;

        SelectionEventHelper(android.view.textclassifier.TextClassificationSessionId sessionId, android.view.textclassifier.TextClassificationContext context) {
            mSessionId = com.android.internal.util.Preconditions.checkNotNull(sessionId);
            mContext = com.android.internal.util.Preconditions.checkNotNull(context);
        }

        /**
         * Updates the necessary fields in the event for the current session.
         *
         * @return true if the event should be reported. false if the event should be ignored
         */
        boolean sanitizeEvent(android.view.textclassifier.SelectionEvent event) {
            updateInvocationMethod(event);
            modifyAutoSelectionEventType(event);
            if ((event.getEventType() != android.view.textclassifier.SelectionEvent.EVENT_SELECTION_STARTED) && (mStartEvent == null)) {
                android.view.textclassifier.Log.d(android.view.textclassifier.TextClassificationSession.LOG_TAG, "Selection session not yet started. Ignoring event");
                return false;
            }
            final long now = java.lang.System.currentTimeMillis();
            switch (event.getEventType()) {
                case android.view.textclassifier.SelectionEvent.EVENT_SELECTION_STARTED :
                    com.android.internal.util.Preconditions.checkArgument(event.getAbsoluteEnd() == (event.getAbsoluteStart() + 1));
                    event.setSessionId(mSessionId);
                    mStartEvent = event;
                    break;
                case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_SINGLE :
                    // fall through
                case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_MULTI :
                    // fall through
                case android.view.textclassifier.SelectionEvent.EVENT_AUTO_SELECTION :
                    mSmartEvent = event;
                    break;
                case android.view.textclassifier.SelectionEvent.EVENT_SELECTION_MODIFIED :
                    if (((mPrevEvent != null) && (mPrevEvent.getAbsoluteStart() == event.getAbsoluteStart())) && (mPrevEvent.getAbsoluteEnd() == event.getAbsoluteEnd())) {
                        // Selection did not change. Ignore event.
                        return false;
                    }
                    break;
                default :
                    // do nothing.
            }
            event.setEventTime(now);
            if (mStartEvent != null) {
                event.setSessionId(mStartEvent.getSessionId()).setDurationSinceSessionStart(now - mStartEvent.getEventTime()).setStart(event.getAbsoluteStart() - mStartEvent.getAbsoluteStart()).setEnd(event.getAbsoluteEnd() - mStartEvent.getAbsoluteStart());
            }
            if (mSmartEvent != null) {
                event.setResultId(mSmartEvent.getResultId()).setSmartStart(mSmartEvent.getAbsoluteStart() - mStartEvent.getAbsoluteStart()).setSmartEnd(mSmartEvent.getAbsoluteEnd() - mStartEvent.getAbsoluteStart());
            }
            if (mPrevEvent != null) {
                event.setDurationSincePreviousEvent(now - mPrevEvent.getEventTime()).setEventIndex(mPrevEvent.getEventIndex() + 1);
            }
            mPrevEvent = event;
            return true;
        }

        void endSession() {
            mPrevEvent = null;
            mSmartEvent = null;
            mStartEvent = null;
        }

        private void updateInvocationMethod(android.view.textclassifier.SelectionEvent event) {
            event.setTextClassificationSessionContext(mContext);
            if (event.getInvocationMethod() == android.view.textclassifier.SelectionEvent.INVOCATION_UNKNOWN) {
                event.setInvocationMethod(mInvocationMethod);
            } else {
                mInvocationMethod = event.getInvocationMethod();
            }
        }

        private void modifyAutoSelectionEventType(android.view.textclassifier.SelectionEvent event) {
            switch (event.getEventType()) {
                case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_SINGLE :
                    // fall through
                case android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_MULTI :
                    // fall through
                case android.view.textclassifier.SelectionEvent.EVENT_AUTO_SELECTION :
                    if (android.view.textclassifier.SelectionSessionLogger.isPlatformLocalTextClassifierSmartSelection(event.getResultId())) {
                        if ((event.getAbsoluteEnd() - event.getAbsoluteStart()) > 1) {
                            event.setEventType(android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_MULTI);
                        } else {
                            event.setEventType(android.view.textclassifier.SelectionEvent.EVENT_SMART_SELECTION_SINGLE);
                        }
                    } else {
                        event.setEventType(android.view.textclassifier.SelectionEvent.EVENT_AUTO_SELECTION);
                    }
                    return;
                default :
                    return;
            }
        }
    }
}

