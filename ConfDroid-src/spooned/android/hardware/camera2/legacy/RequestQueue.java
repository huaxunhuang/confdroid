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
 * A queue of bursts of requests.
 *
 * <p>This queue maintains the count of frames that have been produced, and is thread safe.</p>
 */
public class RequestQueue {
    private static final java.lang.String TAG = "RequestQueue";

    private static final long INVALID_FRAME = -1;

    private android.hardware.camera2.legacy.BurstHolder mRepeatingRequest = null;

    private final java.util.ArrayDeque<android.hardware.camera2.legacy.BurstHolder> mRequestQueue = new java.util.ArrayDeque<android.hardware.camera2.legacy.BurstHolder>();

    private long mCurrentFrameNumber = 0;

    private long mCurrentRepeatingFrameNumber = android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;

    private int mCurrentRequestId = 0;

    private final java.util.List<java.lang.Long> mJpegSurfaceIds;

    public RequestQueue(java.util.List<java.lang.Long> jpegSurfaceIds) {
        mJpegSurfaceIds = jpegSurfaceIds;
    }

    /**
     * Return and remove the next burst on the queue.
     *
     * <p>If a repeating burst is returned, it will not be removed.</p>
     *
     * @return a pair containing the next burst and the current frame number, or null if none exist.
     */
    public synchronized android.util.Pair<android.hardware.camera2.legacy.BurstHolder, java.lang.Long> getNext() {
        android.hardware.camera2.legacy.BurstHolder next = mRequestQueue.poll();
        if ((next == null) && (mRepeatingRequest != null)) {
            next = mRepeatingRequest;
            mCurrentRepeatingFrameNumber = mCurrentFrameNumber + next.getNumberOfRequests();
        }
        if (next == null) {
            return null;
        }
        android.util.Pair<android.hardware.camera2.legacy.BurstHolder, java.lang.Long> ret = new android.util.Pair<android.hardware.camera2.legacy.BurstHolder, java.lang.Long>(next, mCurrentFrameNumber);
        mCurrentFrameNumber += next.getNumberOfRequests();
        return ret;
    }

    /**
     * Cancel a repeating request.
     *
     * @param requestId
     * 		the id of the repeating request to cancel.
     * @return the last frame to be returned from the HAL for the given repeating request, or
    {@code INVALID_FRAME} if none exists.
     */
    public synchronized long stopRepeating(int requestId) {
        long ret = android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;
        if ((mRepeatingRequest != null) && (mRepeatingRequest.getRequestId() == requestId)) {
            mRepeatingRequest = null;
            ret = (mCurrentRepeatingFrameNumber == android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME) ? android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME : mCurrentRepeatingFrameNumber - 1;
            mCurrentRepeatingFrameNumber = android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;
            android.util.Log.i(android.hardware.camera2.legacy.RequestQueue.TAG, "Repeating capture request cancelled.");
        } else {
            android.util.Log.e(android.hardware.camera2.legacy.RequestQueue.TAG, "cancel failed: no repeating request exists for request id: " + requestId);
        }
        return ret;
    }

    /**
     * Cancel a repeating request.
     *
     * @return the last frame to be returned from the HAL for the given repeating request, or
    {@code INVALID_FRAME} if none exists.
     */
    public synchronized long stopRepeating() {
        if (mRepeatingRequest == null) {
            android.util.Log.e(android.hardware.camera2.legacy.RequestQueue.TAG, "cancel failed: no repeating request exists.");
            return android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;
        }
        return stopRepeating(mRepeatingRequest.getRequestId());
    }

    /**
     * Add a the given burst to the queue.
     *
     * <p>If the burst is repeating, replace the current repeating burst.</p>
     *
     * @param requests
     * 		the burst of requests to add to the queue.
     * @param repeating
     * 		true if the burst is repeating.
     * @return the submission info, including the new request id, and the last frame number, which
    contains either the frame number of the last frame that will be returned for this request,
    or the frame number of the last frame that will be returned for the current repeating
    request if this burst is set to be repeating.
     */
    public synchronized android.hardware.camera2.utils.SubmitInfo submit(android.hardware.camera2.CaptureRequest[] requests, boolean repeating) {
        int requestId = mCurrentRequestId++;
        android.hardware.camera2.legacy.BurstHolder burst = new android.hardware.camera2.legacy.BurstHolder(requestId, repeating, requests, mJpegSurfaceIds);
        long lastFrame = android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;
        if (burst.isRepeating()) {
            android.util.Log.i(android.hardware.camera2.legacy.RequestQueue.TAG, "Repeating capture request set.");
            if (mRepeatingRequest != null) {
                lastFrame = (mCurrentRepeatingFrameNumber == android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME) ? android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME : mCurrentRepeatingFrameNumber - 1;
            }
            mCurrentRepeatingFrameNumber = android.hardware.camera2.legacy.RequestQueue.INVALID_FRAME;
            mRepeatingRequest = burst;
        } else {
            mRequestQueue.offer(burst);
            lastFrame = calculateLastFrame(burst.getRequestId());
        }
        android.hardware.camera2.utils.SubmitInfo info = new android.hardware.camera2.utils.SubmitInfo(requestId, lastFrame);
        return info;
    }

    private long calculateLastFrame(int requestId) {
        long total = mCurrentFrameNumber;
        for (android.hardware.camera2.legacy.BurstHolder b : mRequestQueue) {
            total += b.getNumberOfRequests();
            if (b.getRequestId() == requestId) {
                return total - 1;
            }
        }
        throw new java.lang.IllegalStateException("At least one request must be in the queue to calculate frame number");
    }
}

