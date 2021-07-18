/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * Provides a low-level mechanism for an application to send input events.
 *
 * @unknown 
 */
public abstract class InputEventSender {
    private static final java.lang.String TAG = "InputEventSender";

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private long mSenderPtr;

    // We keep references to the input channel and message queue objects here so that
    // they are not GC'd while the native peer of the receiver is using them.
    private android.view.InputChannel mInputChannel;

    private android.os.MessageQueue mMessageQueue;

    private static native long nativeInit(java.lang.ref.WeakReference<android.view.InputEventSender> sender, android.view.InputChannel inputChannel, android.os.MessageQueue messageQueue);

    private static native void nativeDispose(long senderPtr);

    private static native boolean nativeSendKeyEvent(long senderPtr, int seq, android.view.KeyEvent event);

    private static native boolean nativeSendMotionEvent(long senderPtr, int seq, android.view.MotionEvent event);

    /**
     * Creates an input event sender bound to the specified input channel.
     *
     * @param inputChannel
     * 		The input channel.
     * @param looper
     * 		The looper to use when invoking callbacks.
     */
    public InputEventSender(android.view.InputChannel inputChannel, android.os.Looper looper) {
        if (inputChannel == null) {
            throw new java.lang.IllegalArgumentException("inputChannel must not be null");
        }
        if (looper == null) {
            throw new java.lang.IllegalArgumentException("looper must not be null");
        }
        mInputChannel = inputChannel;
        mMessageQueue = looper.getQueue();
        mSenderPtr = android.view.InputEventSender.nativeInit(new java.lang.ref.WeakReference<android.view.InputEventSender>(this), inputChannel, mMessageQueue);
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
        if (mSenderPtr != 0) {
            android.view.InputEventSender.nativeDispose(mSenderPtr);
            mSenderPtr = 0;
        }
        mInputChannel = null;
        mMessageQueue = null;
    }

    /**
     * Called when an input event is finished.
     *
     * @param seq
     * 		The input event sequence number.
     * @param handled
     * 		True if the input event was handled.
     */
    public void onInputEventFinished(int seq, boolean handled) {
    }

    /**
     * Sends an input event.
     * Must be called on the same Looper thread to which the sender is attached.
     *
     * @param seq
     * 		The input event sequence number.
     * @param event
     * 		The input event to send.
     * @return True if the entire event was sent successfully.  May return false
    if the input channel buffer filled before all samples were dispatched.
     */
    public final boolean sendInputEvent(int seq, android.view.InputEvent event) {
        if (event == null) {
            throw new java.lang.IllegalArgumentException("event must not be null");
        }
        if (mSenderPtr == 0) {
            android.util.Log.w(android.view.InputEventSender.TAG, "Attempted to send an input event but the input event " + "sender has already been disposed.");
            return false;
        }
        if (event instanceof android.view.KeyEvent) {
            return android.view.InputEventSender.nativeSendKeyEvent(mSenderPtr, seq, ((android.view.KeyEvent) (event)));
        } else {
            return android.view.InputEventSender.nativeSendMotionEvent(mSenderPtr, seq, ((android.view.MotionEvent) (event)));
        }
    }

    // Called from native code.
    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private void dispatchInputEventFinished(int seq, boolean handled) {
        onInputEventFinished(seq, handled);
    }
}

