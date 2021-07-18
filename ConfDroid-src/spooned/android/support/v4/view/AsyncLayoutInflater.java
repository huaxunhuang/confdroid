/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v4.view;


/**
 * <p>Helper class for inflating layouts asynchronously. To use, construct
 * an instance of {@link AsyncLayoutInflater} on the UI thread and call
 * {@link #inflate(int, ViewGroup, OnInflateFinishedListener)}. The
 * {@link OnInflateFinishedListener} will be invoked on the UI thread
 * when the inflate request has completed.
 *
 * <p>This is intended for parts of the UI that are created lazily or in
 * response to user interactions. This allows the UI thread to continue
 * to be responsive & animate while the relatively heavy inflate
 * is being performed.
 *
 * <p>For a layout to be inflated asynchronously it needs to have a parent
 * whose {@link ViewGroup#generateLayoutParams(AttributeSet)} is thread-safe
 * and all the Views being constructed as part of inflation must not create
 * any {@link Handler}s or otherwise call {@link Looper#myLooper()}. If the
 * layout that is trying to be inflated cannot be constructed
 * asynchronously for whatever reason, {@link AsyncLayoutInflater} will
 * automatically fall back to inflating on the UI thread.
 *
 * <p>NOTE that the inflated View hierarchy is NOT added to the parent. It is
 * equivalent to calling {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
 * with attachToRoot set to false. Callers will likely want to call
 * {@link ViewGroup#addView(View)} in the {@link OnInflateFinishedListener}
 * callback at a minimum.
 *
 * <p>This inflater does not support setting a {@link LayoutInflater.Factory}
 * nor {@link LayoutInflater.Factory2}. Similarly it does not support inflating
 * layouts that contain fragments.
 */
public final class AsyncLayoutInflater {
    private static final java.lang.String TAG = "AsyncLayoutInflater";

    android.view.LayoutInflater mInflater;

    android.os.Handler mHandler;

    android.support.v4.view.AsyncLayoutInflater.InflateThread mInflateThread;

    public AsyncLayoutInflater(@android.support.annotation.NonNull
    android.content.Context context) {
        mInflater = new android.support.v4.view.AsyncLayoutInflater.BasicInflater(context);
        mHandler = new android.os.Handler(mHandlerCallback);
        mInflateThread = android.support.v4.view.AsyncLayoutInflater.InflateThread.getInstance();
    }

    @android.support.annotation.UiThread
    public void inflate(@android.support.annotation.LayoutRes
    int resid, @android.support.annotation.Nullable
    android.view.ViewGroup parent, @android.support.annotation.NonNull
    android.support.v4.view.AsyncLayoutInflater.OnInflateFinishedListener callback) {
        if (callback == null) {
            throw new java.lang.NullPointerException("callback argument may not be null!");
        }
        android.support.v4.view.AsyncLayoutInflater.InflateRequest request = mInflateThread.obtainRequest();
        request.inflater = this;
        request.resid = resid;
        request.parent = parent;
        request.callback = callback;
        mInflateThread.enqueue(request);
    }

    private android.os.Handler.Callback mHandlerCallback = new android.os.Handler.Callback() {
        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            android.support.v4.view.AsyncLayoutInflater.InflateRequest request = ((android.support.v4.view.AsyncLayoutInflater.InflateRequest) (msg.obj));
            if (request.view == null) {
                request.view = mInflater.inflate(request.resid, request.parent, false);
            }
            request.callback.onInflateFinished(request.view, request.resid, request.parent);
            mInflateThread.releaseRequest(request);
            return true;
        }
    };

    public interface OnInflateFinishedListener {
        void onInflateFinished(android.view.View view, int resid, android.view.ViewGroup parent);
    }

    private static class InflateRequest {
        android.support.v4.view.AsyncLayoutInflater inflater;

        android.view.ViewGroup parent;

        int resid;

        android.view.View view;

        android.support.v4.view.AsyncLayoutInflater.OnInflateFinishedListener callback;

        InflateRequest() {
        }
    }

    private static class BasicInflater extends android.view.LayoutInflater {
        private static final java.lang.String[] sClassPrefixList = new java.lang.String[]{ "android.widget.", "android.webkit.", "android.app." };

        BasicInflater(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        public android.view.LayoutInflater cloneInContext(android.content.Context newContext) {
            return new android.support.v4.view.AsyncLayoutInflater.BasicInflater(newContext);
        }

        @java.lang.Override
        protected android.view.View onCreateView(java.lang.String name, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
            for (java.lang.String prefix : android.support.v4.view.AsyncLayoutInflater.BasicInflater.sClassPrefixList) {
                try {
                    android.view.View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (java.lang.ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }
            return super.onCreateView(name, attrs);
        }
    }

    private static class InflateThread extends java.lang.Thread {
        private static final android.support.v4.view.AsyncLayoutInflater.InflateThread sInstance;

        static {
            sInstance = new android.support.v4.view.AsyncLayoutInflater.InflateThread();
            android.support.v4.view.AsyncLayoutInflater.InflateThread.sInstance.start();
        }

        public static android.support.v4.view.AsyncLayoutInflater.InflateThread getInstance() {
            return android.support.v4.view.AsyncLayoutInflater.InflateThread.sInstance;
        }

        private java.util.concurrent.ArrayBlockingQueue<android.support.v4.view.AsyncLayoutInflater.InflateRequest> mQueue = new java.util.concurrent.ArrayBlockingQueue<>(10);

        private android.support.v4.util.Pools.SynchronizedPool<android.support.v4.view.AsyncLayoutInflater.InflateRequest> mRequestPool = new android.support.v4.util.Pools.SynchronizedPool<>(10);

        @java.lang.Override
        public void run() {
            while (true) {
                android.support.v4.view.AsyncLayoutInflater.InflateRequest request;
                try {
                    request = mQueue.take();
                } catch (java.lang.InterruptedException ex) {
                    // Odd, just continue
                    android.util.Log.w(android.support.v4.view.AsyncLayoutInflater.TAG, ex);
                    continue;
                }
                try {
                    request.view = request.inflater.mInflater.inflate(request.resid, request.parent, false);
                } catch (java.lang.RuntimeException ex) {
                    // Probably a Looper failure, retry on the UI thread
                    android.util.Log.w(android.support.v4.view.AsyncLayoutInflater.TAG, "Failed to inflate resource in the background! Retrying on the UI" + " thread", ex);
                }
                android.os.Message.obtain(request.inflater.mHandler, 0, request).sendToTarget();
            } 
        }

        public android.support.v4.view.AsyncLayoutInflater.InflateRequest obtainRequest() {
            android.support.v4.view.AsyncLayoutInflater.InflateRequest obj = mRequestPool.acquire();
            if (obj == null) {
                obj = new android.support.v4.view.AsyncLayoutInflater.InflateRequest();
            }
            return obj;
        }

        public void releaseRequest(android.support.v4.view.AsyncLayoutInflater.InflateRequest obj) {
            obj.callback = null;
            obj.inflater = null;
            obj.parent = null;
            obj.resid = 0;
            obj.view = null;
            mRequestPool.release(obj);
        }

        public void enqueue(android.support.v4.view.AsyncLayoutInflater.InflateRequest request) {
            try {
                mQueue.put(request);
            } catch (java.lang.InterruptedException e) {
                throw new java.lang.RuntimeException("Failed to enqueue async inflate request", e);
            }
        }
    }
}

