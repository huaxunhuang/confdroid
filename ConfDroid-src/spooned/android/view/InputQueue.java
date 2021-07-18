/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * An input queue provides a mechanism for an application to receive incoming
 * input events.  Currently only usable from native code.
 */
public final class InputQueue {
    private final android.util.LongSparseArray<android.view.InputQueue.ActiveInputEvent> mActiveEventArray = new android.util.LongSparseArray<android.view.InputQueue.ActiveInputEvent>(20);

    private final android.util.Pools.Pool<android.view.InputQueue.ActiveInputEvent> mActiveInputEventPool = new android.util.Pools.SimplePool<android.view.InputQueue.ActiveInputEvent>(20);

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private long mPtr;

    private static native long nativeInit(java.lang.ref.WeakReference<android.view.InputQueue> weakQueue, android.os.MessageQueue messageQueue);

    private static native long nativeSendKeyEvent(long ptr, android.view.KeyEvent e, boolean preDispatch);

    private static native long nativeSendMotionEvent(long ptr, android.view.MotionEvent e);

    private static native void nativeDispose(long ptr);

    /**
     *
     *
     * @unknown 
     */
    public InputQueue() {
        mPtr = android.view.InputQueue.nativeInit(new java.lang.ref.WeakReference<android.view.InputQueue>(this), android.os.Looper.myQueue());
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
     *
     *
     * @unknown 
     */
    public void dispose() {
        dispose(false);
    }

    /**
     *
     *
     * @unknown 
     */
    public void dispose(boolean finalized) {
        if (mCloseGuard != null) {
            if (finalized) {
                mCloseGuard.warnIfOpen();
            }
            mCloseGuard.close();
        }
        if (mPtr != 0) {
            android.view.InputQueue.nativeDispose(mPtr);
            mPtr = 0;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public long getNativePtr() {
        return mPtr;
    }

    /**
     *
     *
     * @unknown 
     */
    public void sendInputEvent(android.view.InputEvent e, java.lang.Object token, boolean predispatch, android.view.InputQueue.FinishedInputEventCallback callback) {
        android.view.InputQueue.ActiveInputEvent event = obtainActiveInputEvent(token, callback);
        long id;
        if (e instanceof android.view.KeyEvent) {
            id = android.view.InputQueue.nativeSendKeyEvent(mPtr, ((android.view.KeyEvent) (e)), predispatch);
        } else {
            id = android.view.InputQueue.nativeSendMotionEvent(mPtr, ((android.view.MotionEvent) (e)));
        }
        mActiveEventArray.put(id, event);
    }

    @android.annotation.UnsupportedAppUsage
    private void finishInputEvent(long id, boolean handled) {
        int index = mActiveEventArray.indexOfKey(id);
        if (index >= 0) {
            android.view.InputQueue.ActiveInputEvent e = mActiveEventArray.valueAt(index);
            mActiveEventArray.removeAt(index);
            e.mCallback.onFinishedInputEvent(e.mToken, handled);
            recycleActiveInputEvent(e);
        }
    }

    private android.view.InputQueue.ActiveInputEvent obtainActiveInputEvent(java.lang.Object token, android.view.InputQueue.FinishedInputEventCallback callback) {
        android.view.InputQueue.ActiveInputEvent e = mActiveInputEventPool.acquire();
        if (e == null) {
            e = new android.view.InputQueue.ActiveInputEvent();
        }
        e.mToken = token;
        e.mCallback = callback;
        return e;
    }

    private void recycleActiveInputEvent(android.view.InputQueue.ActiveInputEvent e) {
        e.recycle();
        mActiveInputEventPool.release(e);
    }

    private final class ActiveInputEvent {
        public java.lang.Object mToken;

        public android.view.InputQueue.FinishedInputEventCallback mCallback;

        public void recycle() {
            mToken = null;
            mCallback = null;
        }
    }

    /**
     * Interface to receive notification of when an InputQueue is associated
     * and dissociated with a thread.
     */
    public static interface Callback {
        /**
         * Called when the given InputQueue is now associated with the
         * thread making this call, so it can start receiving events from it.
         */
        void onInputQueueCreated(android.view.InputQueue queue);

        /**
         * Called when the given InputQueue is no longer associated with
         * the thread and thus not dispatching events.
         */
        void onInputQueueDestroyed(android.view.InputQueue queue);
    }

    /**
     *
     *
     * @unknown 
     */
    public static interface FinishedInputEventCallback {
        void onFinishedInputEvent(java.lang.Object token, boolean handled);
    }
}

