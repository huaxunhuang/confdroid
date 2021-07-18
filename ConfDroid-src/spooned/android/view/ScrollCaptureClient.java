/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view;


/**
 * A client of the system providing Scroll Capture capability on behalf of a Window.
 * <p>
 * An instance is created to wrap the selected {@link ScrollCaptureCallback}.
 *
 * @unknown 
 */
public class ScrollCaptureClient extends android.view.IScrollCaptureClient.Stub {
    private static final java.lang.String TAG = "ScrollCaptureClient";

    private static final int DEFAULT_TIMEOUT = 1000;

    private final android.os.Handler mHandler;

    private android.view.ScrollCaptureTarget mSelectedTarget;

    private int mTimeoutMillis = android.view.ScrollCaptureClient.DEFAULT_TIMEOUT;

    protected android.view.Surface mSurface;

    private android.view.IScrollCaptureController mController;

    private final android.graphics.Rect mScrollBounds;

    private final android.graphics.Point mPositionInWindow;

    private final android.util.CloseGuard mCloseGuard;

    // The current session instance in use by the callback.
    private android.view.ScrollCaptureSession mSession;

    // Helps manage timeout callbacks registered to handler and aids testing.
    private android.view.ScrollCaptureClient.DelayedAction mTimeoutAction;

    /**
     * Constructs a ScrollCaptureClient.
     *
     * @param selectedTarget
     * 		the target the client is controlling
     * @param controller
     * 		the callbacks to reply to system requests
     * @unknown 
     */
    public ScrollCaptureClient(@android.annotation.NonNull
    android.view.ScrollCaptureTarget selectedTarget, @android.annotation.NonNull
    android.view.IScrollCaptureController controller) {
        java.util.Objects.requireNonNull(selectedTarget, "<selectedTarget> must non-null");
        java.util.Objects.requireNonNull(controller, "<controller> must non-null");
        final android.graphics.Rect scrollBounds = java.util.Objects.requireNonNull(selectedTarget.getScrollBounds(), "target.getScrollBounds() must be non-null to construct a client");
        mSelectedTarget = selectedTarget;
        mHandler = selectedTarget.getContainingView().getHandler();
        mScrollBounds = new android.graphics.Rect(scrollBounds);
        mPositionInWindow = new android.graphics.Point(selectedTarget.getPositionInWindow());
        mController = controller;
        mCloseGuard = new android.util.CloseGuard();
        mCloseGuard.open("close");
        selectedTarget.getContainingView().addOnAttachStateChangeListener(new android.view.View.OnAttachStateChangeListener() {
            @java.lang.Override
            public void onViewAttachedToWindow(android.view.View v) {
            }

            @java.lang.Override
            public void onViewDetachedFromWindow(android.view.View v) {
                selectedTarget.getContainingView().removeOnAttachStateChangeListener(this);
                endCapture();
            }
        });
    }

    @com.android.internal.annotations.VisibleForTesting
    public void setTimeoutMillis(int timeoutMillis) {
        mTimeoutMillis = timeoutMillis;
    }

    @android.annotation.Nullable
    @com.android.internal.annotations.VisibleForTesting
    public android.view.ScrollCaptureClient.DelayedAction getTimeoutAction() {
        return mTimeoutAction;
    }

    private void checkConnected() {
        if ((mSelectedTarget == null) || (mController == null)) {
            throw new java.lang.IllegalStateException("This client has been disconnected.");
        }
    }

    private void checkStarted() {
        if (mSession == null) {
            throw new java.lang.IllegalStateException("Capture session has not been started!");
        }
    }

    // IScrollCaptureClient
    @android.annotation.WorkerThread
    @java.lang.Override
    public void startCapture(android.view.Surface surface) throws android.os.RemoteException {
        checkConnected();
        mSurface = surface;
        scheduleTimeout(mTimeoutMillis, this::onStartCaptureTimeout);
        mSession = new android.view.ScrollCaptureSession(mSurface, mScrollBounds, mPositionInWindow, this);
        mHandler.post(() -> mSelectedTarget.getCallback().onScrollCaptureStart(mSession, this::onStartCaptureCompleted));
    }

    @android.annotation.UiThread
    private void onStartCaptureCompleted() {
        if (cancelTimeout()) {
            mHandler.post(() -> {
                try {
                    mController.onCaptureStarted();
                } catch ( e) {
                    doShutdown();
                }
            });
        }
    }

    @android.annotation.UiThread
    private void onStartCaptureTimeout() {
        endCapture();
    }

    // IScrollCaptureClient
    @android.annotation.WorkerThread
    @java.lang.Override
    public void requestImage(android.graphics.Rect requestRect) {
        checkConnected();
        checkStarted();
        scheduleTimeout(mTimeoutMillis, this::onRequestImageTimeout);
        // Response is dispatched via ScrollCaptureSession, to onRequestImageCompleted
        mHandler.post(() -> mSelectedTarget.getCallback().onScrollCaptureImageRequest(mSession, new android.graphics.Rect(requestRect)));
    }

    @android.annotation.UiThread
    void onRequestImageCompleted(long frameNumber, android.graphics.Rect capturedArea) {
        final android.graphics.Rect finalCapturedArea = new android.graphics.Rect(capturedArea);
        if (cancelTimeout()) {
            mHandler.post(() -> {
                try {
                    mController.onCaptureBufferSent(frameNumber, finalCapturedArea);
                } catch ( e) {
                    doShutdown();
                }
            });
        }
    }

    @android.annotation.UiThread
    private void onRequestImageTimeout() {
        endCapture();
    }

    // IScrollCaptureClient
    @android.annotation.WorkerThread
    @java.lang.Override
    public void endCapture() {
        if (isStarted()) {
            scheduleTimeout(mTimeoutMillis, this::onEndCaptureTimeout);
            mHandler.post(() -> mSelectedTarget.getCallback().onScrollCaptureEnd(this::onEndCaptureCompleted));
        } else {
            disconnect();
        }
    }

    private boolean isStarted() {
        return (mController != null) && (mSelectedTarget != null);
    }

    @android.annotation.UiThread
    private void onEndCaptureCompleted() {
        // onEndCaptureCompleted
        if (cancelTimeout()) {
            doShutdown();
        }
    }

    @android.annotation.UiThread
    private void onEndCaptureTimeout() {
        doShutdown();
    }

    private void doShutdown() {
        try {
            if (mController != null) {
                mController.onConnectionClosed();
            }
        } catch (android.os.RemoteException e) {
            // Ignore
        } finally {
            disconnect();
        }
    }

    /**
     * Shuts down this client and releases references to dependent objects. No attempt is made
     * to notify the controller, use with caution!
     */
    public void disconnect() {
        if (mSession != null) {
            mSession.disconnect();
            mSession = null;
        }
        mSelectedTarget = null;
        mController = null;
    }

    /**
     *
     *
     * @return a string representation of the state of this client
     */
    public java.lang.String toString() {
        return (((((("ScrollCaptureClient{" + ", session=") + mSession) + ", selectedTarget=") + mSelectedTarget) + ", clientCallbacks=") + mController) + "}";
    }

    private boolean cancelTimeout() {
        if (mTimeoutAction != null) {
            return mTimeoutAction.cancel();
        }
        return false;
    }

    private void scheduleTimeout(long timeoutMillis, java.lang.Runnable action) {
        if (mTimeoutAction != null) {
            mTimeoutAction.cancel();
        }
        mTimeoutAction = new android.view.ScrollCaptureClient.DelayedAction(mHandler, timeoutMillis, action);
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static class DelayedAction {
        private final java.util.concurrent.atomic.AtomicBoolean mCompleted = new java.util.concurrent.atomic.AtomicBoolean();

        private final java.lang.Object mToken = new java.lang.Object();

        private final android.os.Handler mHandler;

        private final java.lang.Runnable mAction;

        @com.android.internal.annotations.VisibleForTesting
        public DelayedAction(android.os.Handler handler, long timeoutMillis, java.lang.Runnable action) {
            mHandler = handler;
            mAction = action;
            mHandler.postDelayed(this::onTimeout, mToken, timeoutMillis);
        }

        private boolean onTimeout() {
            if (mCompleted.compareAndSet(false, true)) {
                mAction.run();
                return true;
            }
            return false;
        }

        /**
         * Cause the timeout action to run immediately and mark as timed out.
         *
         * @return true if the timeout was run, false if the timeout had already been canceled
         */
        @com.android.internal.annotations.VisibleForTesting
        public boolean timeoutNow() {
            return onTimeout();
        }

        /**
         * Attempt to cancel the timeout action (such as after a callback is made)
         *
         * @return true if the timeout was canceled and will not run, false if time has expired and
        the timeout action has or will run momentarily
         */
        public boolean cancel() {
            if (!mCompleted.compareAndSet(false, true)) {
                // Whoops, too late!
                return false;
            }
            mHandler.removeCallbacksAndMessages(mToken);
            return true;
        }
    }
}

