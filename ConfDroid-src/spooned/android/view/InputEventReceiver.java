/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Provides a low-level mechanism for an application to receive input events.
 *
 * @unknown 
 */
public abstract class InputEventReceiver {
    private static final java.lang.String TAG = "InputEventReceiver";

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private long mReceiverPtr;

    // We keep references to the input channel and message queue objects here so that
    // they are not GC'd while the native peer of the receiver is using them.
    private android.view.InputChannel mInputChannel;

    private android.os.MessageQueue mMessageQueue;

    // Map from InputEvent sequence numbers to dispatcher sequence numbers.
    private final android.util.SparseIntArray mSeqMap = new android.util.SparseIntArray();

    private static native long nativeInit(java.lang.ref.WeakReference<android.view.InputEventReceiver> receiver, android.view.InputChannel inputChannel, android.os.MessageQueue messageQueue);

    private static native void nativeDispose(long receiverPtr);

    private static native void nativeFinishInputEvent(long receiverPtr, int seq, boolean handled);

    private static native boolean nativeConsumeBatchedInputEvents(long receiverPtr, long frameTimeNanos);

    /**
     * Creates an input event receiver bound to the specified input channel.
     *
     * @param inputChannel
     * 		The input channel.
     * @param looper
     * 		The looper to use when invoking callbacks.
     */
    public InputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper) {
        if (inputChannel == null) {
            throw new java.lang.IllegalArgumentException("inputChannel must not be null");
        }
        if (looper == null) {
            throw new java.lang.IllegalArgumentException("looper must not be null");
        }
        mInputChannel = inputChannel;
        mMessageQueue = looper.getQueue();
        mReceiverPtr = android.view.InputEventReceiver.nativeInit(new java.lang.ref.WeakReference<android.view.InputEventReceiver>(this), inputChannel, mMessageQueue);
        mCloseGuard.open("dispose");
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    /**
     * Disposes the receiver.
     */
    public void dispose() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        if (mCloseGuard != null) {
            if (finalized) {
                mCloseGuard.warnIfOpen();
            }
            mCloseGuard.close();
        }
        if (mReceiverPtr != 0) {
            android.view.InputEventReceiver.nativeDispose(mReceiverPtr);
            mReceiverPtr = 0;
        }
        mInputChannel = null;
        mMessageQueue = null;
    }

    /**
     * Called when an input event is received.
     * The recipient should process the input event and then call {@link #finishInputEvent}
     * to indicate whether the event was handled.  No new input events will be received
     * until {@link #finishInputEvent} is called.
     *
     * @param event
     * 		The input event that was received.
     */
    @android.annotation.UnsupportedAppUsage
    public void onInputEvent(android.view.InputEvent event) {
        finishInputEvent(event, false);
    }

    /**
     * Called when a batched input event is pending.
     *
     * The batched input event will continue to accumulate additional movement
     * samples until the recipient calls {@link #consumeBatchedInputEvents} or
     * an event is received that ends the batch and causes it to be consumed
     * immediately (such as a pointer up event).
     */
    public void onBatchedInputEventPending() {
        consumeBatchedInputEvents(-1);
    }

    /**
     * Finishes an input event and indicates whether it was handled.
     * Must be called on the same Looper thread to which the receiver is attached.
     *
     * @param event
     * 		The input event that was finished.
     * @param handled
     * 		True if the event was handled.
     */
    public final void finishInputEvent(android.view.InputEvent event, boolean handled) {
        if (event == null) {
            throw new java.lang.IllegalArgumentException("event must not be null");
        }
        if (mReceiverPtr == 0) {
            android.util.Log.w(android.view.InputEventReceiver.TAG, "Attempted to finish an input event but the input event " + "receiver has already been disposed.");
        } else {
            int index = mSeqMap.indexOfKey(event.getSequenceNumber());
            if (index < 0) {
                android.util.Log.w(android.view.InputEventReceiver.TAG, "Attempted to finish an input event that is not in progress.");
            } else {
                int seq = mSeqMap.valueAt(index);
                mSeqMap.removeAt(index);
                android.view.InputEventReceiver.nativeFinishInputEvent(mReceiverPtr, seq, handled);
            }
        }
        event.recycleIfNeededAfterDispatch();
    }

    /**
     * Consumes all pending batched input events.
     * Must be called on the same Looper thread to which the receiver is attached.
     *
     * This method forces all batched input events to be delivered immediately.
     * Should be called just before animating or drawing a new frame in the UI.
     *
     * @param frameTimeNanos
     * 		The time in the {@link System#nanoTime()} time base
     * 		when the current display frame started rendering, or -1 if unknown.
     * @return Whether a batch was consumed
     */
    public final boolean consumeBatchedInputEvents(long frameTimeNanos) {
        if (mReceiverPtr == 0) {
            android.util.Log.w(android.view.InputEventReceiver.TAG, "Attempted to consume batched input events but the input event " + "receiver has already been disposed.");
        } else {
            return android.view.InputEventReceiver.nativeConsumeBatchedInputEvents(mReceiverPtr, frameTimeNanos);
        }
        return false;
    }

    // Called from native code.
    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private void dispatchInputEvent(int seq, android.view.InputEvent event) {
        mSeqMap.put(event.getSequenceNumber(), seq);
        onInputEvent(event);
    }

    // Called from native code.
    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private void dispatchBatchedInputEventPending() {
        onBatchedInputEventPending();
    }

    public static interface Factory {
        public android.view.InputEventReceiver createInputEventReceiver(android.view.InputChannel inputChannel, android.os.Looper looper);
    }
}

