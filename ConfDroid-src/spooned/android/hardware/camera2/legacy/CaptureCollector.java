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


/**
 * Collect timestamps and state for each {@link CaptureRequest} as it passes through
 * the Legacy camera pipeline.
 */
public class CaptureCollector {
    private static final java.lang.String TAG = "CaptureCollector";

    private static final boolean DEBUG = false;

    private static final int FLAG_RECEIVED_JPEG = 1;

    private static final int FLAG_RECEIVED_JPEG_TS = 2;

    private static final int FLAG_RECEIVED_PREVIEW = 4;

    private static final int FLAG_RECEIVED_PREVIEW_TS = 8;

    private static final int FLAG_RECEIVED_ALL_JPEG = android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG | android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG_TS;

    private static final int FLAG_RECEIVED_ALL_PREVIEW = android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW | android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW_TS;

    private static final int MAX_JPEGS_IN_FLIGHT = 1;

    private class CaptureHolder implements java.lang.Comparable<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> {
        private final android.hardware.camera2.legacy.RequestHolder mRequest;

        private final android.hardware.camera2.legacy.LegacyRequest mLegacy;

        public final boolean needsJpeg;

        public final boolean needsPreview;

        private long mTimestamp = 0;

        private int mReceivedFlags = 0;

        private boolean mHasStarted = false;

        private boolean mFailedJpeg = false;

        private boolean mFailedPreview = false;

        private boolean mCompleted = false;

        private boolean mPreviewCompleted = false;

        public CaptureHolder(android.hardware.camera2.legacy.RequestHolder request, android.hardware.camera2.legacy.LegacyRequest legacyHolder) {
            mRequest = request;
            mLegacy = legacyHolder;
            needsJpeg = request.hasJpegTargets();
            needsPreview = request.hasPreviewTargets();
        }

        public boolean isPreviewCompleted() {
            return (mReceivedFlags & android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_ALL_PREVIEW) == android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_ALL_PREVIEW;
        }

        public boolean isJpegCompleted() {
            return (mReceivedFlags & android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_ALL_JPEG) == android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_ALL_JPEG;
        }

        public boolean isCompleted() {
            return (needsJpeg == isJpegCompleted()) && (needsPreview == isPreviewCompleted());
        }

        public void tryComplete() {
            if (((!mPreviewCompleted) && needsPreview) && isPreviewCompleted()) {
                android.hardware.camera2.legacy.CaptureCollector.this.onPreviewCompleted();
                mPreviewCompleted = true;
            }
            if (isCompleted() && (!mCompleted)) {
                if (mFailedPreview || mFailedJpeg) {
                    if (!mHasStarted) {
                        // Send a request error if the capture has not yet started.
                        mRequest.failRequest();
                        android.hardware.camera2.legacy.CaptureCollector.this.mDeviceState.setCaptureStart(mRequest, mTimestamp, ERROR_CAMERA_REQUEST);
                    } else {
                        // Send buffer dropped errors for each pending buffer if the request has
                        // started.
                        for (android.view.Surface targetSurface : mRequest.getRequest().getTargets()) {
                            try {
                                if (mRequest.jpegType(targetSurface)) {
                                    if (mFailedJpeg) {
                                        /* result */
                                        android.hardware.camera2.legacy.CaptureCollector.this.mDeviceState.setCaptureResult(mRequest, null, ERROR_CAMERA_BUFFER, targetSurface);
                                    }
                                } else {
                                    // preview buffer
                                    if (mFailedPreview) {
                                        /* result */
                                        android.hardware.camera2.legacy.CaptureCollector.this.mDeviceState.setCaptureResult(mRequest, null, ERROR_CAMERA_BUFFER, targetSurface);
                                    }
                                }
                            } catch (android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException e) {
                                android.util.Log.e(android.hardware.camera2.legacy.CaptureCollector.TAG, "Unexpected exception when querying Surface: " + e);
                            }
                        }
                    }
                }
                android.hardware.camera2.legacy.CaptureCollector.this.onRequestCompleted(this);
                mCompleted = true;
            }
        }

        public void setJpegTimestamp(long timestamp) {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setJpegTimestamp - called for request " + mRequest.getRequestId());
            }
            if (!needsJpeg) {
                throw new java.lang.IllegalStateException("setJpegTimestamp called for capture with no jpeg targets.");
            }
            if (isCompleted()) {
                throw new java.lang.IllegalStateException("setJpegTimestamp called on already completed request.");
            }
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG_TS;
            if (mTimestamp == 0) {
                mTimestamp = timestamp;
            }
            if (!mHasStarted) {
                mHasStarted = true;
                android.hardware.camera2.legacy.CaptureCollector.this.mDeviceState.setCaptureStart(mRequest, mTimestamp, android.hardware.camera2.legacy.CameraDeviceState.NO_CAPTURE_ERROR);
            }
            tryComplete();
        }

        public void setJpegProduced() {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setJpegProduced - called for request " + mRequest.getRequestId());
            }
            if (!needsJpeg) {
                throw new java.lang.IllegalStateException("setJpegProduced called for capture with no jpeg targets.");
            }
            if (isCompleted()) {
                throw new java.lang.IllegalStateException("setJpegProduced called on already completed request.");
            }
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG;
            tryComplete();
        }

        public void setJpegFailed() {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setJpegFailed - called for request " + mRequest.getRequestId());
            }
            if ((!needsJpeg) || isJpegCompleted()) {
                return;
            }
            mFailedJpeg = true;
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG;
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_JPEG_TS;
            tryComplete();
        }

        public void setPreviewTimestamp(long timestamp) {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setPreviewTimestamp - called for request " + mRequest.getRequestId());
            }
            if (!needsPreview) {
                throw new java.lang.IllegalStateException("setPreviewTimestamp called for capture with no preview targets.");
            }
            if (isCompleted()) {
                throw new java.lang.IllegalStateException("setPreviewTimestamp called on already completed request.");
            }
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW_TS;
            if (mTimestamp == 0) {
                mTimestamp = timestamp;
            }
            if (!needsJpeg) {
                if (!mHasStarted) {
                    mHasStarted = true;
                    android.hardware.camera2.legacy.CaptureCollector.this.mDeviceState.setCaptureStart(mRequest, mTimestamp, android.hardware.camera2.legacy.CameraDeviceState.NO_CAPTURE_ERROR);
                }
            }
            tryComplete();
        }

        public void setPreviewProduced() {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setPreviewProduced - called for request " + mRequest.getRequestId());
            }
            if (!needsPreview) {
                throw new java.lang.IllegalStateException("setPreviewProduced called for capture with no preview targets.");
            }
            if (isCompleted()) {
                throw new java.lang.IllegalStateException("setPreviewProduced called on already completed request.");
            }
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW;
            tryComplete();
        }

        public void setPreviewFailed() {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "setPreviewFailed - called for request " + mRequest.getRequestId());
            }
            if ((!needsPreview) || isPreviewCompleted()) {
                return;
            }
            mFailedPreview = true;
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW;
            mReceivedFlags |= android.hardware.camera2.legacy.CaptureCollector.FLAG_RECEIVED_PREVIEW_TS;
            tryComplete();
        }

        // Comparison and equals based on frame number.
        @java.lang.Override
        public int compareTo(android.hardware.camera2.legacy.CaptureCollector.CaptureHolder captureHolder) {
            return mRequest.getFrameNumber() > captureHolder.mRequest.getFrameNumber() ? 1 : mRequest.getFrameNumber() == captureHolder.mRequest.getFrameNumber() ? 0 : -1;
        }

        // Comparison and equals based on frame number.
        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            return (o instanceof android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) && (compareTo(((android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) (o))) == 0);
        }
    }

    private final java.util.TreeSet<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mActiveRequests;

    private final java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mJpegCaptureQueue;

    private final java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mJpegProduceQueue;

    private final java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mPreviewCaptureQueue;

    private final java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mPreviewProduceQueue;

    private final java.util.ArrayList<android.hardware.camera2.legacy.CaptureCollector.CaptureHolder> mCompletedRequests = new java.util.ArrayList<>();

    private final java.util.concurrent.locks.ReentrantLock mLock = new java.util.concurrent.locks.ReentrantLock();

    private final java.util.concurrent.locks.Condition mIsEmpty;

    private final java.util.concurrent.locks.Condition mPreviewsEmpty;

    private final java.util.concurrent.locks.Condition mNotFull;

    private final android.hardware.camera2.legacy.CameraDeviceState mDeviceState;

    private int mInFlight = 0;

    private int mInFlightPreviews = 0;

    private final int mMaxInFlight;

    /**
     * Create a new {@link CaptureCollector} that can modify the given {@link CameraDeviceState}.
     *
     * @param maxInFlight
     * 		max allowed in-flight requests.
     * @param deviceState
     * 		the {@link CameraDeviceState} to update as requests are processed.
     */
    public CaptureCollector(int maxInFlight, android.hardware.camera2.legacy.CameraDeviceState deviceState) {
        mMaxInFlight = maxInFlight;
        mJpegCaptureQueue = new java.util.ArrayDeque<>(android.hardware.camera2.legacy.CaptureCollector.MAX_JPEGS_IN_FLIGHT);
        mJpegProduceQueue = new java.util.ArrayDeque<>(android.hardware.camera2.legacy.CaptureCollector.MAX_JPEGS_IN_FLIGHT);
        mPreviewCaptureQueue = new java.util.ArrayDeque<>(mMaxInFlight);
        mPreviewProduceQueue = new java.util.ArrayDeque<>(mMaxInFlight);
        mActiveRequests = new java.util.TreeSet<>();
        mIsEmpty = mLock.newCondition();
        mNotFull = mLock.newCondition();
        mPreviewsEmpty = mLock.newCondition();
        mDeviceState = deviceState;
    }

    /**
     * Queue a new request.
     *
     * <p>
     * For requests that use the Camera1 API preview output stream, this will block if there are
     * already {@code maxInFlight} requests in progress (until at least one prior request has
     * completed). For requests that use the Camera1 API jpeg callbacks, this will block until
     * all prior requests have been completed to avoid stopping preview for
     * {@link android.hardware.Camera#takePicture} before prior preview requests have been
     * completed.
     * </p>
     *
     * @param holder
     * 		the {@link RequestHolder} for this request.
     * @param legacy
     * 		the {@link LegacyRequest} for this request; this will not be mutated.
     * @param timeout
     * 		a timeout to use for this call.
     * @param unit
     * 		the units to use for the timeout.
     * @return {@code false} if this method timed out.
     * @throws InterruptedException
     * 		if this thread is interrupted.
     */
    public boolean queueRequest(android.hardware.camera2.legacy.RequestHolder holder, android.hardware.camera2.legacy.LegacyRequest legacy, long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = new android.hardware.camera2.legacy.CaptureCollector.CaptureHolder(holder, legacy);
        long nanos = unit.toNanos(timeout);
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, ((("queueRequest  for request " + holder.getRequestId()) + " - ") + mInFlight) + " requests remain in flight.");
            }
            if (!(h.needsJpeg || h.needsPreview)) {
                throw new java.lang.IllegalStateException("Request must target at least one output surface!");
            }
            if (h.needsJpeg) {
                // Wait for all current requests to finish before queueing jpeg.
                while (mInFlight > 0) {
                    if (nanos <= 0) {
                        return false;
                    }
                    nanos = mIsEmpty.awaitNanos(nanos);
                } 
                mJpegCaptureQueue.add(h);
                mJpegProduceQueue.add(h);
            }
            if (h.needsPreview) {
                while (mInFlight >= mMaxInFlight) {
                    if (nanos <= 0) {
                        return false;
                    }
                    nanos = mNotFull.awaitNanos(nanos);
                } 
                mPreviewCaptureQueue.add(h);
                mPreviewProduceQueue.add(h);
                mInFlightPreviews++;
            }
            mActiveRequests.add(h);
            mInFlight++;
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait all queued requests to complete.
     *
     * @param timeout
     * 		a timeout to use for this call.
     * @param unit
     * 		the units to use for the timeout.
     * @return {@code false} if this method timed out.
     * @throws InterruptedException
     * 		if this thread is interrupted.
     */
    public boolean waitForEmpty(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        long nanos = unit.toNanos(timeout);
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            while (mInFlight > 0) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = mIsEmpty.awaitNanos(nanos);
            } 
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait all queued requests that use the Camera1 API preview output to complete.
     *
     * @param timeout
     * 		a timeout to use for this call.
     * @param unit
     * 		the units to use for the timeout.
     * @return {@code false} if this method timed out.
     * @throws InterruptedException
     * 		if this thread is interrupted.
     */
    public boolean waitForPreviewsEmpty(long timeout, java.util.concurrent.TimeUnit unit) throws java.lang.InterruptedException {
        long nanos = unit.toNanos(timeout);
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            while (mInFlightPreviews > 0) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = mPreviewsEmpty.awaitNanos(nanos);
            } 
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait for the specified request to be completed (all buffers available).
     *
     * <p>May not wait for the same request more than once, since a successful wait
     * will erase the history of that request.</p>
     *
     * @param holder
     * 		the {@link RequestHolder} for this request.
     * @param timeout
     * 		a timeout to use for this call.
     * @param unit
     * 		the units to use for the timeout.
     * @param timestamp
     * 		the timestamp of the request will be written out to here, in ns
     * @return {@code false} if this method timed out.
     * @throws InterruptedException
     * 		if this thread is interrupted.
     */
    public boolean waitForRequestCompleted(android.hardware.camera2.legacy.RequestHolder holder, long timeout, java.util.concurrent.TimeUnit unit, android.util.MutableLong timestamp) throws java.lang.InterruptedException {
        long nanos = unit.toNanos(timeout);
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            while (!/* out */
            removeRequestIfCompleted(holder, timestamp)) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = mNotFull.awaitNanos(nanos);
            } 
            return true;
        } finally {
            lock.unlock();
        }
    }

    private boolean removeRequestIfCompleted(android.hardware.camera2.legacy.RequestHolder holder, android.util.MutableLong timestamp) {
        int i = 0;
        for (android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h : mCompletedRequests) {
            if (h.mRequest.equals(holder)) {
                timestamp.value = h.mTimestamp;
                mCompletedRequests.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Called to alert the {@link CaptureCollector} that the jpeg capture has begun.
     *
     * @param timestamp
     * 		the time of the jpeg capture.
     * @return the {@link RequestHolder} for the request associated with this capture.
     */
    public android.hardware.camera2.legacy.RequestHolder jpegCaptured(long timestamp) {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = mJpegCaptureQueue.poll();
            if (h == null) {
                android.util.Log.w(android.hardware.camera2.legacy.CaptureCollector.TAG, "jpegCaptured called with no jpeg request on queue!");
                return null;
            }
            h.setJpegTimestamp(timestamp);
            return h.mRequest;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} that the jpeg capture has completed.
     *
     * @return a pair containing the {@link RequestHolder} and the timestamp of the capture.
     */
    public android.util.Pair<android.hardware.camera2.legacy.RequestHolder, java.lang.Long> jpegProduced() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = mJpegProduceQueue.poll();
            if (h == null) {
                android.util.Log.w(android.hardware.camera2.legacy.CaptureCollector.TAG, "jpegProduced called with no jpeg request on queue!");
                return null;
            }
            h.setJpegProduced();
            return new android.util.Pair<>(h.mRequest, h.mTimestamp);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Check if there are any pending capture requests that use the Camera1 API preview output.
     *
     * @return {@code true} if there are pending preview requests.
     */
    public boolean hasPendingPreviewCaptures() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            return !mPreviewCaptureQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} that the preview capture has begun.
     *
     * @param timestamp
     * 		the time of the preview capture.
     * @return a pair containing the {@link RequestHolder} and the timestamp of the capture.
     */
    public android.util.Pair<android.hardware.camera2.legacy.RequestHolder, java.lang.Long> previewCaptured(long timestamp) {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = mPreviewCaptureQueue.poll();
            if (h == null) {
                if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
                    android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, "previewCaptured called with no preview request on queue!");
                }
                return null;
            }
            h.setPreviewTimestamp(timestamp);
            return new android.util.Pair<>(h.mRequest, h.mTimestamp);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} that the preview capture has completed.
     *
     * @return the {@link RequestHolder} for the request associated with this capture.
     */
    public android.hardware.camera2.legacy.RequestHolder previewProduced() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = mPreviewProduceQueue.poll();
            if (h == null) {
                android.util.Log.w(android.hardware.camera2.legacy.CaptureCollector.TAG, "previewProduced called with no preview request on queue!");
                return null;
            }
            h.setPreviewProduced();
            return h.mRequest;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} that the next pending preview capture has failed.
     */
    public void failNextPreview() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h1 = mPreviewCaptureQueue.peek();
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h2 = mPreviewProduceQueue.peek();
            // Find the request with the lowest frame number.
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = (h1 == null) ? h2 : h2 == null ? h1 : h1.compareTo(h2) <= 0 ? h1 : h2;
            if (h != null) {
                mPreviewCaptureQueue.remove(h);
                mPreviewProduceQueue.remove(h);
                mActiveRequests.remove(h);
                h.setPreviewFailed();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} that the next pending jpeg capture has failed.
     */
    public void failNextJpeg() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h1 = mJpegCaptureQueue.peek();
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h2 = mJpegProduceQueue.peek();
            // Find the request with the lowest frame number.
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h = (h1 == null) ? h2 : h2 == null ? h1 : h1.compareTo(h2) <= 0 ? h1 : h2;
            if (h != null) {
                mJpegCaptureQueue.remove(h);
                mJpegProduceQueue.remove(h);
                mActiveRequests.remove(h);
                h.setJpegFailed();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Called to alert the {@link CaptureCollector} all pending captures have failed.
     */
    public void failAll() {
        final java.util.concurrent.locks.ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            android.hardware.camera2.legacy.CaptureCollector.CaptureHolder h;
            while ((h = mActiveRequests.pollFirst()) != null) {
                h.setPreviewFailed();
                h.setJpegFailed();
            } 
            mPreviewCaptureQueue.clear();
            mPreviewProduceQueue.clear();
            mJpegCaptureQueue.clear();
            mJpegProduceQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    private void onPreviewCompleted() {
        mInFlightPreviews--;
        if (mInFlightPreviews < 0) {
            throw new java.lang.IllegalStateException("More preview captures completed than requests queued.");
        }
        if (mInFlightPreviews == 0) {
            mPreviewsEmpty.signalAll();
        }
    }

    private void onRequestCompleted(android.hardware.camera2.legacy.CaptureCollector.CaptureHolder capture) {
        android.hardware.camera2.legacy.RequestHolder request = capture.mRequest;
        mInFlight--;
        if (android.hardware.camera2.legacy.CaptureCollector.DEBUG) {
            android.util.Log.d(android.hardware.camera2.legacy.CaptureCollector.TAG, ((("Completed request " + request.getRequestId()) + ", ") + mInFlight) + " requests remain in flight.");
        }
        if (mInFlight < 0) {
            throw new java.lang.IllegalStateException("More captures completed than requests queued.");
        }
        mCompletedRequests.add(capture);
        mActiveRequests.remove(capture);
        mNotFull.signalAll();
        if (mInFlight == 0) {
            mIsEmpty.signalAll();
        }
    }
}

