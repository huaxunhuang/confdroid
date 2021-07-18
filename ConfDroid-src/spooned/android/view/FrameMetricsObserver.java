/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * Provides streaming access to frame stats information from the rendering
 * subsystem to apps.
 *
 * @unknown 
 */
public class FrameMetricsObserver {
    @android.annotation.UnsupportedAppUsage
    private android.os.MessageQueue mMessageQueue;

    private java.lang.ref.WeakReference<android.view.Window> mWindow;

    @android.annotation.UnsupportedAppUsage
    private android.view.FrameMetrics mFrameMetrics;

    /* pacage */
    android.view.Window.OnFrameMetricsAvailableListener mListener;

    /**
     *
     *
     * @unknown 
     */
    public com.android.internal.util.VirtualRefBasePtr mNative;

    /**
     * Creates a FrameMetricsObserver
     *
     * @param looper
     * 		the looper to use when invoking callbacks
     */
    FrameMetricsObserver(@android.annotation.NonNull
    android.view.Window window, @android.annotation.NonNull
    android.os.Looper looper, @android.annotation.NonNull
    android.view.Window.OnFrameMetricsAvailableListener listener) {
        if (looper == null) {
            throw new java.lang.NullPointerException("looper cannot be null");
        }
        mMessageQueue = looper.getQueue();
        if (mMessageQueue == null) {
            throw new java.lang.IllegalStateException("invalid looper, null message queue\n");
        }
        mFrameMetrics = new android.view.FrameMetrics();
        mWindow = new java.lang.ref.WeakReference<>(window);
        mListener = listener;
    }

    // Called by native on the provided Handler
    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private void notifyDataAvailable(int dropCount) {
        final android.view.Window window = mWindow.get();
        if (window != null) {
            mListener.onFrameMetricsAvailable(window, mFrameMetrics, dropCount);
        }
    }
}

