/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.graphics;


/**
 * Provides streaming access to frame stats information from HardwareRenderer to apps.
 *
 * @unknown 
 */
public class HardwareRendererObserver {
    private final long[] mFrameMetrics;

    private final android.os.Handler mHandler;

    private final android.graphics.HardwareRendererObserver.OnFrameMetricsAvailableListener mListener;

    private com.android.internal.util.VirtualRefBasePtr mNativePtr;

    /**
     * Interface for clients that want frame timing information for each frame rendered.
     *
     * @unknown 
     */
    public interface OnFrameMetricsAvailableListener {
        /**
         * Called when information is available for the previously rendered frame.
         *
         * Reports can be dropped if this callback takes too long to execute, as the report producer
         * cannot wait for the consumer to complete.
         *
         * It is highly recommended that clients copy the metrics array within this method
         * and defer additional computation or storage to another thread to avoid unnecessarily
         * dropping reports.
         *
         * @param dropCountSinceLastInvocation
         * 		the number of reports dropped since the last time
         * 		this callback was invoked.
         */
        void onFrameMetricsAvailable(int dropCountSinceLastInvocation);
    }

    /**
     * Creates a FrameMetricsObserver
     *
     * @param frameMetrics
     * 		the available metrics. This array is reused on every call to the listener
     * 		and thus <strong>this reference should only be used within the scope of the listener callback
     * 		as data is not guaranteed to be valid outside the scope of that method</strong>.
     * @param handler
     * 		the Handler to use when invoking callbacks
     */
    public HardwareRendererObserver(@android.annotation.NonNull
    android.graphics.HardwareRendererObserver.OnFrameMetricsAvailableListener listener, @android.annotation.NonNull
    long[] frameMetrics, @android.annotation.NonNull
    android.os.Handler handler) {
        if ((handler == null) || (handler.getLooper() == null)) {
            throw new java.lang.NullPointerException("handler and its looper cannot be null");
        }
        if (handler.getLooper().getQueue() == null) {
            throw new java.lang.IllegalStateException("invalid looper, null message queue\n");
        }
        mFrameMetrics = frameMetrics;
        mHandler = handler;
        mListener = listener;
        mNativePtr = new com.android.internal.util.VirtualRefBasePtr(nCreateObserver());
    }

    /* package */
    long getNativeInstance() {
        return mNativePtr.get();
    }

    // Called by native on the provided Handler
    @java.lang.SuppressWarnings("unused")
    private void notifyDataAvailable() {
        mHandler.post(() -> {
            boolean hasMoreData = true;
            while (hasMoreData) {
                // a drop count of -1 is a sentinel that no more buffers are available
                int dropCount = nGetNextBuffer(mNativePtr.get(), mFrameMetrics);
                if (dropCount >= 0) {
                    mListener.onFrameMetricsAvailable(dropCount);
                } else {
                    hasMoreData = false;
                }
            } 
        });
    }

    private native long nCreateObserver();

    private static native int nGetNextBuffer(long nativePtr, long[] data);
}

