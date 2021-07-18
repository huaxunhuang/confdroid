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
 * Immutable container for a burst of capture results.
 */
public class BurstHolder {
    private static final java.lang.String TAG = "BurstHolder";

    private final java.util.ArrayList<android.hardware.camera2.legacy.RequestHolder.Builder> mRequestBuilders;

    private final boolean mRepeating;

    private final int mRequestId;

    /**
     * Immutable container for a burst of capture results.
     *
     * @param requestId
     * 		id of the burst request.
     * @param repeating
     * 		true if this burst is repeating.
     * @param requests
     * 		the array of {@link CaptureRequest}s for this burst.
     * @param jpegSurfaceIds
     * 		a {@link Collection} of IDs for the surfaces that have jpeg outputs.
     */
    public BurstHolder(int requestId, boolean repeating, android.hardware.camera2.CaptureRequest[] requests, java.util.Collection<java.lang.Long> jpegSurfaceIds) {
        mRequestBuilders = new java.util.ArrayList<>();
        int i = 0;
        for (android.hardware.camera2.CaptureRequest r : requests) {
            mRequestBuilders.add(/* subsequenceId */
            /* request */
            new android.hardware.camera2.legacy.RequestHolder.Builder(requestId, i, r, repeating, jpegSurfaceIds));
            ++i;
        }
        mRepeating = repeating;
        mRequestId = requestId;
    }

    /**
     * Get the id of this request.
     */
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * Return true if this repeating.
     */
    public boolean isRepeating() {
        return mRepeating;
    }

    /**
     * Return the number of requests in this burst sequence.
     */
    public int getNumberOfRequests() {
        return mRequestBuilders.size();
    }

    /**
     * Create a list of {@link RequestHolder} objects encapsulating the requests in this burst.
     *
     * @param frameNumber
     * 		the starting framenumber for this burst.
     * @return the list of {@link RequestHolder} objects.
     */
    public java.util.List<android.hardware.camera2.legacy.RequestHolder> produceRequestHolders(long frameNumber) {
        java.util.ArrayList<android.hardware.camera2.legacy.RequestHolder> holders = new java.util.ArrayList<android.hardware.camera2.legacy.RequestHolder>();
        int i = 0;
        for (android.hardware.camera2.legacy.RequestHolder.Builder b : mRequestBuilders) {
            holders.add(b.build(frameNumber + i));
            ++i;
        }
        return holders;
    }
}

