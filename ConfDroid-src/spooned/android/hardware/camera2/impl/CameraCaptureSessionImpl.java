/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.impl;


public class CameraCaptureSessionImpl extends android.hardware.camera2.CameraCaptureSession implements android.hardware.camera2.impl.CameraCaptureSessionCore {
    private static final java.lang.String TAG = "CameraCaptureSession";

    private static final boolean DEBUG = false;

    /**
     * Simple integer ID for session for debugging
     */
    private final int mId;

    private final java.lang.String mIdString;

    /**
     * Input surface configured by native camera framework based on user-specified configuration
     */
    private final android.view.Surface mInput;

    /**
     * User-specified set of surfaces used as the configuration outputs
     */
    private final java.util.List<android.view.Surface> mOutputs;

    /**
     * User-specified state callback, used for outgoing events; calls to this object will be
     * automatically {@link Handler#post(Runnable) posted} to {@code mStateHandler}.
     */
    private final android.hardware.camera2.CameraCaptureSession.StateCallback mStateCallback;

    /**
     * User-specified state handler used for outgoing state callback events
     */
    private final android.os.Handler mStateHandler;

    /**
     * Internal camera device; used to translate calls into existing deprecated API
     */
    private final android.hardware.camera2.impl.CameraDeviceImpl mDeviceImpl;

    /**
     * Internal handler; used for all incoming events to preserve total order
     */
    private final android.os.Handler mDeviceHandler;

    /**
     * Drain Sequence IDs which have been queued but not yet finished with aborted/completed
     */
    private final android.hardware.camera2.utils.TaskDrainer<java.lang.Integer> mSequenceDrainer;

    /**
     * Drain state transitions from ACTIVE -> IDLE
     */
    private final android.hardware.camera2.utils.TaskSingleDrainer mIdleDrainer;

    /**
     * Drain state transitions from BUSY -> IDLE
     */
    private final android.hardware.camera2.utils.TaskSingleDrainer mAbortDrainer;

    /**
     * This session is closed; all further calls will throw ISE
     */
    private boolean mClosed = false;

    /**
     * This session failed to be configured successfully
     */
    private final boolean mConfigureSuccess;

    /**
     * Do not unconfigure if this is set; another session will overwrite configuration
     */
    private boolean mSkipUnconfigure = false;

    /**
     * Is the session in the process of aborting? Pay attention to BUSY->IDLE transitions.
     */
    private volatile boolean mAborting;

    /**
     * Create a new CameraCaptureSession.
     *
     * <p>The camera device must already be in the {@code IDLE} state when this is invoked.
     * There must be no pending actions
     * (e.g. no pending captures, no repeating requests, no flush).</p>
     */
    CameraCaptureSessionImpl(int id, android.view.Surface input, java.util.List<android.view.Surface> outputs, android.hardware.camera2.CameraCaptureSession.StateCallback callback, android.os.Handler stateHandler, android.hardware.camera2.impl.CameraDeviceImpl deviceImpl, android.os.Handler deviceStateHandler, boolean configureSuccess) {
        if ((outputs == null) || outputs.isEmpty()) {
            throw new java.lang.IllegalArgumentException("outputs must be a non-null, non-empty list");
        } else
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("callback must not be null");
            }

        mId = id;
        mIdString = java.lang.String.format("Session %d: ", mId);
        // TODO: extra verification of outputs
        mOutputs = outputs;
        mInput = input;
        mStateHandler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(stateHandler);
        mStateCallback = createUserStateCallbackProxy(mStateHandler, callback);
        mDeviceHandler = checkNotNull(deviceStateHandler, "deviceStateHandler must not be null");
        mDeviceImpl = checkNotNull(deviceImpl, "deviceImpl must not be null");
        /* Use the same handler as the device's StateCallback for all the internal coming events

        This ensures total ordering between CameraDevice.StateCallback and
        CameraDeviceImpl.CaptureCallback events.
         */
        mSequenceDrainer = /* name */
        new android.hardware.camera2.utils.TaskDrainer<>(mDeviceHandler, new android.hardware.camera2.impl.CameraCaptureSessionImpl.SequenceDrainListener(), "seq");
        mIdleDrainer = /* name */
        new android.hardware.camera2.utils.TaskSingleDrainer(mDeviceHandler, new android.hardware.camera2.impl.CameraCaptureSessionImpl.IdleDrainListener(), "idle");
        mAbortDrainer = /* name */
        new android.hardware.camera2.utils.TaskSingleDrainer(mDeviceHandler, new android.hardware.camera2.impl.CameraCaptureSessionImpl.AbortDrainListener(), "abort");
        // CameraDevice should call configureOutputs and have it finish before constructing us
        if (configureSuccess) {
            mStateCallback.onConfigured(this);
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "Created session successfully");

            mConfigureSuccess = true;
        } else {
            mStateCallback.onConfigureFailed(this);
            mClosed = true;// do not fire any other callbacks, do not allow any other work

            android.util.Log.e(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "Failed to create capture session; configuration failed");
            mConfigureSuccess = false;
        }
    }

    @java.lang.Override
    public android.hardware.camera2.CameraDevice getDevice() {
        return mDeviceImpl;
    }

    @java.lang.Override
    public void prepare(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mDeviceImpl.prepare(surface);
    }

    @java.lang.Override
    public void prepare(int maxCount, android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mDeviceImpl.prepare(maxCount, surface);
    }

    @java.lang.Override
    public void tearDown(android.view.Surface surface) throws android.hardware.camera2.CameraAccessException {
        mDeviceImpl.tearDown(surface);
    }

    @java.lang.Override
    public void finishDeferredConfiguration(java.util.List<android.hardware.camera2.params.OutputConfiguration> deferredOutputConfigs) throws android.hardware.camera2.CameraAccessException {
        mDeviceImpl.finishDeferredConfig(deferredOutputConfigs);
    }

    @java.lang.Override
    public synchronized int capture(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CameraCaptureSession.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (request == null) {
            throw new java.lang.IllegalArgumentException("request must not be null");
        } else
            if (request.isReprocess() && (!isReprocessable())) {
                throw new java.lang.IllegalArgumentException("this capture session cannot handle reprocess " + "requests");
            } else
                if (request.isReprocess() && (request.getReprocessableSessionId() != mId)) {
                    throw new java.lang.IllegalArgumentException("capture request was created for another session");
                }


        checkNotClosed();
        handler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler, callback);
        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, (((((mIdString + "capture - request ") + request) + ", callback ") + callback) + " handler ") + handler);
        }
        return addPendingSequence(mDeviceImpl.capture(request, createCaptureCallbackProxy(handler, callback), mDeviceHandler));
    }

    @java.lang.Override
    public synchronized int captureBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.CameraCaptureSession.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (requests == null) {
            throw new java.lang.IllegalArgumentException("Requests must not be null");
        } else
            if (requests.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Requests must have at least one element");
            }

        for (android.hardware.camera2.CaptureRequest request : requests) {
            if (request.isReprocess()) {
                if (!isReprocessable()) {
                    throw new java.lang.IllegalArgumentException("This capture session cannot handle " + "reprocess requests");
                } else
                    if (request.getReprocessableSessionId() != mId) {
                        throw new java.lang.IllegalArgumentException("Capture request was created for another " + "session");
                    }

            }
        }
        checkNotClosed();
        handler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler, callback);
        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
            android.hardware.camera2.CaptureRequest[] requestArray = requests.toArray(new android.hardware.camera2.CaptureRequest[0]);
            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, (((((mIdString + "captureBurst - requests ") + java.util.Arrays.toString(requestArray)) + ", callback ") + callback) + " handler ") + handler);
        }
        return addPendingSequence(mDeviceImpl.captureBurst(requests, createCaptureCallbackProxy(handler, callback), mDeviceHandler));
    }

    @java.lang.Override
    public synchronized int setRepeatingRequest(android.hardware.camera2.CaptureRequest request, android.hardware.camera2.CameraCaptureSession.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (request == null) {
            throw new java.lang.IllegalArgumentException("request must not be null");
        } else
            if (request.isReprocess()) {
                throw new java.lang.IllegalArgumentException("repeating reprocess requests are not supported");
            }

        checkNotClosed();
        handler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler, callback);
        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, ((((((mIdString + "setRepeatingRequest - request ") + request) + ", callback ") + callback) + " handler") + " ") + handler);
        }
        return addPendingSequence(mDeviceImpl.setRepeatingRequest(request, createCaptureCallbackProxy(handler, callback), mDeviceHandler));
    }

    @java.lang.Override
    public synchronized int setRepeatingBurst(java.util.List<android.hardware.camera2.CaptureRequest> requests, android.hardware.camera2.CameraCaptureSession.CaptureCallback callback, android.os.Handler handler) throws android.hardware.camera2.CameraAccessException {
        if (requests == null) {
            throw new java.lang.IllegalArgumentException("requests must not be null");
        } else
            if (requests.isEmpty()) {
                throw new java.lang.IllegalArgumentException("requests must have at least one element");
            }

        for (android.hardware.camera2.CaptureRequest r : requests) {
            if (r.isReprocess()) {
                throw new java.lang.IllegalArgumentException("repeating reprocess burst requests are not " + "supported");
            }
        }
        checkNotClosed();
        handler = android.hardware.camera2.impl.CameraDeviceImpl.checkHandler(handler, callback);
        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
            android.hardware.camera2.CaptureRequest[] requestArray = requests.toArray(new android.hardware.camera2.CaptureRequest[0]);
            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, ((((((mIdString + "setRepeatingBurst - requests ") + java.util.Arrays.toString(requestArray)) + ", callback ") + callback) + " handler") + "") + handler);
        }
        return addPendingSequence(mDeviceImpl.setRepeatingBurst(requests, createCaptureCallbackProxy(handler, callback), mDeviceHandler));
    }

    @java.lang.Override
    public synchronized void stopRepeating() throws android.hardware.camera2.CameraAccessException {
        checkNotClosed();
        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "stopRepeating");
        }
        mDeviceImpl.stopRepeating();
    }

    @java.lang.Override
    public void abortCaptures() throws android.hardware.camera2.CameraAccessException {
        synchronized(this) {
            checkNotClosed();
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG) {
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "abortCaptures");
            }
            if (mAborting) {
                android.util.Log.w(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "abortCaptures - Session is already aborting; doing nothing");
                return;
            }
            mAborting = true;
            mAbortDrainer.taskStarted();
        }
        synchronized(mDeviceImpl.mInterfaceLock) {
            synchronized(this) {
                mDeviceImpl.flush();
                // The next BUSY -> IDLE set of transitions will mark the end of the abort.
            }
        }
    }

    @java.lang.Override
    public boolean isReprocessable() {
        return mInput != null;
    }

    @java.lang.Override
    public android.view.Surface getInputSurface() {
        return mInput;
    }

    /**
     * Replace this session with another session.
     *
     * <p>This is an optimization to avoid unconfiguring and then immediately having to
     * reconfigure again.</p>
     *
     * <p>The semantics are identical to {@link #close}, except that unconfiguring will be skipped.
     * <p>
     *
     * <p>After this call completes, the session will not call any further methods on the camera
     * device.</p>
     *
     * @see CameraCaptureSession#close
     */
    @java.lang.Override
    public void replaceSessionClose() {
        synchronized(this) {
            /* In order for creating new sessions to be fast, the new session should be created
            before the old session is closed.

            Otherwise the old session will always unconfigure if there is no new session to
            replace it.

            Unconfiguring could add hundreds of milliseconds of delay. We could race and attempt
            to skip unconfigure if a new session is created before the captures are all drained,
            but this would introduce nondeterministic behavior.
             */
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "replaceSessionClose");

            // Set up fast shutdown. Possible alternative paths:
            // - This session is active, so close() below starts the shutdown drain
            // - This session is mid-shutdown drain, and hasn't yet reached the idle drain listener.
            // - This session is already closed and has executed the idle drain listener, and
            // configureOutputsChecked(null) has already been called.
            // 
            // Do not call configureOutputsChecked(null) going forward, since it would race with the
            // configuration for the new session. If it was already called, then we don't care,
            // since it won't get called again.
            mSkipUnconfigure = true;
        }
        close();
    }

    @java.lang.Override
    public void close() {
        synchronized(this) {
            if (mClosed) {
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "close - reentering");

                return;
            }
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "close - first time");

            mClosed = true;
        }
        synchronized(mDeviceImpl.mInterfaceLock) {
            synchronized(this) {
                /* Flush out any repeating request. Since camera is closed, no new requests
                can be queued, and eventually the entire request queue will be drained.

                If the camera device was already closed, short circuit and do nothing; since
                no more internal device callbacks will fire anyway.

                Otherwise, once stopRepeating is done, wait for camera to idle, then unconfigure
                the camera. Once that's done, fire #onClosed.
                 */
                try {
                    mDeviceImpl.stopRepeating();
                } catch (java.lang.IllegalStateException e) {
                    // OK: Camera device may already be closed, nothing else to do
                    // TODO: Fire onClosed anytime we get the device onClosed or the ISE?
                    // or just suppress the ISE only and rely onClosed.
                    // Also skip any of the draining work if this is already closed.
                    // Short-circuit; queue callback immediately and return
                    mStateCallback.onClosed(this);
                    return;
                } catch (android.hardware.camera2.CameraAccessException e) {
                    // OK: close does not throw checked exceptions.
                    android.util.Log.e(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "Exception while stopping repeating: ", e);
                    // TODO: call onError instead of onClosed if this happens
                }
            }
        }
        synchronized(this) {
            // If no sequences are pending, fire #onClosed immediately
            mSequenceDrainer.beginDrain();
        }
    }

    /**
     * Whether currently in mid-abort.
     *
     * <p>This is used by the implementation to set the capture failure
     * reason, in lieu of more accurate error codes from the camera service.
     * Unsynchronized to avoid deadlocks between simultaneous session->device,
     * device->session calls.</p>
     */
    @java.lang.Override
    public boolean isAborting() {
        return mAborting;
    }

    /**
     * Post calls into a CameraCaptureSession.StateCallback to the user-specified {@code handler}.
     */
    private android.hardware.camera2.CameraCaptureSession.StateCallback createUserStateCallbackProxy(android.os.Handler handler, android.hardware.camera2.CameraCaptureSession.StateCallback callback) {
        android.hardware.camera2.dispatch.InvokeDispatcher<android.hardware.camera2.CameraCaptureSession.StateCallback> userCallbackSink = new android.hardware.camera2.dispatch.InvokeDispatcher<>(callback);
        android.hardware.camera2.dispatch.HandlerDispatcher<android.hardware.camera2.CameraCaptureSession.StateCallback> handlerPassthrough = new android.hardware.camera2.dispatch.HandlerDispatcher<>(userCallbackSink, handler);
        return new android.hardware.camera2.impl.CallbackProxies.SessionStateCallbackProxy(handlerPassthrough);
    }

    /**
     * Forward callbacks from
     * CameraDeviceImpl.CaptureCallback to the CameraCaptureSession.CaptureCallback.
     *
     * <p>In particular, all calls are automatically split to go both to our own
     * internal callback, and to the user-specified callback (by transparently posting
     * to the user-specified handler).</p>
     *
     * <p>When a capture sequence finishes, update the pending checked sequences set.</p>
     */
    @java.lang.SuppressWarnings("deprecation")
    private android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback createCaptureCallbackProxy(android.os.Handler handler, android.hardware.camera2.CameraCaptureSession.CaptureCallback callback) {
        android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback localCallback = new android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback() {
            @java.lang.Override
            public void onCaptureSequenceCompleted(android.hardware.camera2.CameraDevice camera, int sequenceId, long frameNumber) {
                finishPendingSequence(sequenceId);
            }

            @java.lang.Override
            public void onCaptureSequenceAborted(android.hardware.camera2.CameraDevice camera, int sequenceId) {
                finishPendingSequence(sequenceId);
            }
        };
        /* Split the calls from the device callback into local callback and the following chain:
        - replace the first CameraDevice arg with a CameraCaptureSession
        - duck type from device callback to session callback
        - then forward the call to a handler
        - then finally invoke the destination method on the session callback object
         */
        if (callback == null) {
            // OK: API allows the user to not specify a callback, and the handler may
            // also be null in that case. Collapse whole dispatch chain to only call the local
            // callback
            return localCallback;
        }
        android.hardware.camera2.dispatch.InvokeDispatcher<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback> localSink = new android.hardware.camera2.dispatch.InvokeDispatcher<>(localCallback);
        android.hardware.camera2.dispatch.InvokeDispatcher<android.hardware.camera2.CameraCaptureSession.CaptureCallback> userCallbackSink = new android.hardware.camera2.dispatch.InvokeDispatcher<>(callback);
        android.hardware.camera2.dispatch.HandlerDispatcher<android.hardware.camera2.CameraCaptureSession.CaptureCallback> handlerPassthrough = new android.hardware.camera2.dispatch.HandlerDispatcher<>(userCallbackSink, handler);
        android.hardware.camera2.dispatch.DuckTypingDispatcher<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback, android.hardware.camera2.CameraCaptureSession.CaptureCallback> duckToSession = new android.hardware.camera2.dispatch.DuckTypingDispatcher<>(handlerPassthrough, android.hardware.camera2.CameraCaptureSession.CaptureCallback.class);
        android.hardware.camera2.dispatch.ArgumentReplacingDispatcher<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback, android.hardware.camera2.impl.CameraCaptureSessionImpl> replaceDeviceWithSession = /* argumentIndex */
        new android.hardware.camera2.dispatch.ArgumentReplacingDispatcher<>(duckToSession, 0, this);
        android.hardware.camera2.dispatch.BroadcastDispatcher<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback> broadcaster = new android.hardware.camera2.dispatch.BroadcastDispatcher<android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallback>(replaceDeviceWithSession, localSink);
        return new android.hardware.camera2.impl.CallbackProxies.DeviceCaptureCallbackProxy(broadcaster);
    }

    /**
     * Create an internal state callback, to be invoked on the mDeviceHandler
     *
     * <p>It has a few behaviors:
     * <ul>
     * <li>Convert device state changes into session state changes.
     * <li>Keep track of async tasks that the session began (idle, abort).
     * </ul>
     * </p>
     */
    @java.lang.Override
    public android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK getDeviceStateCallback() {
        final android.hardware.camera2.CameraCaptureSession session = this;
        return new android.hardware.camera2.impl.CameraDeviceImpl.StateCallbackKK() {
            private boolean mBusy = false;

            private boolean mActive = false;

            @java.lang.Override
            public void onOpened(android.hardware.camera2.CameraDevice camera) {
                throw new java.lang.AssertionError("Camera must already be open before creating a session");
            }

            @java.lang.Override
            public void onDisconnected(android.hardware.camera2.CameraDevice camera) {
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onDisconnected");

                close();
            }

            @java.lang.Override
            public void onError(android.hardware.camera2.CameraDevice camera, int error) {
                // Should not be reached, handled by device code
                android.util.Log.wtf(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, (mIdString + "Got device error ") + error);
            }

            @java.lang.Override
            public void onActive(android.hardware.camera2.CameraDevice camera) {
                mIdleDrainer.taskStarted();
                mActive = true;
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onActive");

                mStateCallback.onActive(session);
            }

            @java.lang.Override
            public void onIdle(android.hardware.camera2.CameraDevice camera) {
                boolean isAborting;
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onIdle");

                synchronized(session) {
                    isAborting = mAborting;
                }
                /* Check which states we transitioned through:

                (ACTIVE -> IDLE)
                (BUSY -> IDLE)

                Note that this is also legal:
                (ACTIVE -> BUSY -> IDLE)

                and mark those tasks as finished
                 */
                if (mBusy && isAborting) {
                    mAbortDrainer.taskFinished();
                    synchronized(session) {
                        mAborting = false;
                    }
                }
                if (mActive) {
                    mIdleDrainer.taskFinished();
                }
                mBusy = false;
                mActive = false;
                mStateCallback.onReady(session);
            }

            @java.lang.Override
            public void onBusy(android.hardware.camera2.CameraDevice camera) {
                mBusy = true;
                // TODO: Queue captures during abort instead of failing them
                // since the app won't be able to distinguish the two actives
                // Don't signal the application since there's no clean mapping here
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onBusy");

            }

            @java.lang.Override
            public void onUnconfigured(android.hardware.camera2.CameraDevice camera) {
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onUnconfigured");

            }

            @java.lang.Override
            public void onSurfacePrepared(android.view.Surface surface) {
                if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                    android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onPrepared");

                mStateCallback.onSurfacePrepared(session, surface);
            }
        };
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private void checkNotClosed() {
        if (mClosed) {
            throw new java.lang.IllegalStateException("Session has been closed; further changes are illegal.");
        }
    }

    /**
     * Notify the session that a pending capture sequence has just been queued.
     *
     * <p>During a shutdown/close, the session waits until all pending sessions are finished
     * before taking any further steps to shut down itself.</p>
     *
     * @see #finishPendingSequence
     */
    private int addPendingSequence(int sequenceId) {
        mSequenceDrainer.taskStarted(sequenceId);
        return sequenceId;
    }

    /**
     * Notify the session that a pending capture sequence is now finished.
     *
     * <p>During a shutdown/close, once all pending sequences finish, it is safe to
     * close the camera further by unconfiguring and then firing {@code onClosed}.</p>
     */
    private void finishPendingSequence(int sequenceId) {
        try {
            mSequenceDrainer.taskFinished(sequenceId);
        } catch (java.lang.IllegalStateException e) {
            // Workaround for b/27870771
            android.util.Log.w(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, e.getMessage());
        }
    }

    private class SequenceDrainListener implements android.hardware.camera2.utils.TaskDrainer.DrainListener {
        @java.lang.Override
        public void onDrained() {
            /* No repeating request is set; and the capture queue has fully drained.

            If no captures were queued to begin with, and an abort was queued,
            it's still possible to get another BUSY before the last IDLE.

            If the camera is already "IDLE" and no aborts are pending,
            then the drain immediately finishes.
             */
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onSequenceDrained");

            // Fire session close as soon as all sequences are complete.
            // We may still need to unconfigure the device, but a new session might be created
            // past this point, and notifications would then stop to this instance.
            mStateCallback.onClosed(android.hardware.camera2.impl.CameraCaptureSessionImpl.this);
            // Fast path: A new capture session has replaced this one; don't wait for abort/idle
            // as we won't get state updates any more anyway.
            if (mSkipUnconfigure) {
                return;
            }
            mAbortDrainer.beginDrain();
        }
    }

    private class AbortDrainListener implements android.hardware.camera2.utils.TaskDrainer.DrainListener {
        @java.lang.Override
        public void onDrained() {
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onAbortDrained");

            synchronized(android.hardware.camera2.impl.CameraCaptureSessionImpl.this) {
                /* Any queued aborts have now completed.

                It's now safe to wait to receive the final "IDLE" event, as the camera device
                will no longer again transition to "ACTIVE" by itself.

                If the camera is already "IDLE", then the drain immediately finishes.
                 */
                // Fast path: A new capture session has replaced this one; don't wait for idle
                // as we won't get state updates any more anyway.
                if (mSkipUnconfigure) {
                    return;
                }
                mIdleDrainer.beginDrain();
            }
        }
    }

    private class IdleDrainListener implements android.hardware.camera2.utils.TaskDrainer.DrainListener {
        @java.lang.Override
        public void onDrained() {
            if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "onIdleDrained");

            // Take device lock before session lock so that we can call back into device
            // without causing a deadlock
            synchronized(mDeviceImpl.mInterfaceLock) {
                synchronized(android.hardware.camera2.impl.CameraCaptureSessionImpl.this) {
                    /* The device is now IDLE, and has settled. It will not transition to
                    ACTIVE or BUSY again by itself.

                    It's now safe to unconfigure the outputs.

                    This operation is idempotent; a session will not be closed twice.
                     */
                    if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                        android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, (mIdString + "Session drain complete, skip unconfigure: ") + mSkipUnconfigure);

                    // Fast path: A new capture session has replaced this one; don't wait for idle
                    // as we won't get state updates any more anyway.
                    if (mSkipUnconfigure) {
                        return;
                    }
                    // Final slow path: unconfigure the camera, no session has replaced us and
                    // everything is idle.
                    try {
                        // begin transition to unconfigured
                        /* inputConfig */
                        /* outputs */
                        /* isConstrainedHighSpeed */
                        mDeviceImpl.configureStreamsChecked(null, null, false);
                    } catch (android.hardware.camera2.CameraAccessException e) {
                        // OK: do not throw checked exceptions.
                        android.util.Log.e(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "Exception while unconfiguring outputs: ", e);
                        // TODO: call onError instead of onClosed if this happens
                    } catch (java.lang.IllegalStateException e) {
                        // Camera is already closed, so nothing left to do
                        if (android.hardware.camera2.impl.CameraCaptureSessionImpl.DEBUG)
                            android.util.Log.v(android.hardware.camera2.impl.CameraCaptureSessionImpl.TAG, mIdString + "Camera was already closed or busy, skipping unconfigure");

                    }
                }
            }
        }
    }
}

