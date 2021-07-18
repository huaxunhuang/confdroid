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
package android.hardware.camera2.legacy;


public class RequestHandlerThread extends android.os.HandlerThread {
    /**
     * Ensure that the MessageQueue's idle handler gets run by poking the message queue;
     * normally if the message queue is already idle, the idle handler won't get invoked.
     *
     * <p>Users of this handler thread should ignore this message.</p>
     */
    public static final int MSG_POKE_IDLE_HANDLER = -1;

    private final android.os.ConditionVariable mStarted = new android.os.ConditionVariable(false);

    private final android.os.ConditionVariable mIdle = new android.os.ConditionVariable(true);

    private android.os.Handler.Callback mCallback;

    private volatile android.os.Handler mHandler;

    public RequestHandlerThread(java.lang.String name, android.os.Handler.Callback callback) {
        super(name, java.lang.Thread.MAX_PRIORITY);
        mCallback = callback;
    }

    @java.lang.Override
    protected void onLooperPrepared() {
        mHandler = new android.os.Handler(getLooper(), mCallback);
        mStarted.open();
    }

    // Blocks until thread has started
    public void waitUntilStarted() {
        mStarted.block();
    }

    // May return null if the handler is not set up yet.
    public android.os.Handler getHandler() {
        return mHandler;
    }

    // Blocks until thread has started
    public android.os.Handler waitAndGetHandler() {
        waitUntilStarted();
        return getHandler();
    }

    // Atomic multi-type message existence check
    public boolean hasAnyMessages(int[] what) {
        synchronized(mHandler.getLooper().getQueue()) {
            for (int i : what) {
                if (mHandler.hasMessages(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Atomic multi-type message remove
    public void removeMessages(int[] what) {
        synchronized(mHandler.getLooper().getQueue()) {
            for (int i : what) {
                mHandler.removeMessages(i);
            }
        }
    }

    private final android.os.MessageQueue.IdleHandler mIdleHandler = new android.os.MessageQueue.IdleHandler() {
        @java.lang.Override
        public boolean queueIdle() {
            mIdle.open();
            return false;
        }
    };

    // Blocks until thread is idling
    public void waitUntilIdle() {
        android.os.Handler handler = waitAndGetHandler();
        android.os.MessageQueue queue = handler.getLooper().getQueue();
        if (queue.isIdle()) {
            return;
        }
        mIdle.close();
        queue.addIdleHandler(mIdleHandler);
        // Ensure that the idle handler gets run even if the looper already went idle
        handler.sendEmptyMessage(android.hardware.camera2.legacy.RequestHandlerThread.MSG_POKE_IDLE_HANDLER);
        if (queue.isIdle()) {
            return;
        }
        mIdle.block();
    }
}

