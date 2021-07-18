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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
class StopWatch {
    private int STOP_WATCH_LOGGING_PERIOD = 200;

    private java.lang.String TAG = "MFF";

    private java.lang.String mName;

    private long mStartTime;

    private long mTotalTime;

    private int mNumCalls;

    public StopWatch(java.lang.String name) {
        mName = name;
        mStartTime = -1;
        mTotalTime = 0;
        mNumCalls = 0;
    }

    public void start() {
        if (mStartTime != (-1)) {
            throw new java.lang.RuntimeException("Calling start with StopWatch already running");
        }
        mStartTime = android.os.SystemClock.elapsedRealtime();
    }

    public void stop() {
        if (mStartTime == (-1)) {
            throw new java.lang.RuntimeException("Calling stop with StopWatch already stopped");
        }
        long stopTime = android.os.SystemClock.elapsedRealtime();
        mTotalTime += stopTime - mStartTime;
        ++mNumCalls;
        mStartTime = -1;
        if ((mNumCalls % STOP_WATCH_LOGGING_PERIOD) == 0) {
            android.util.Log.i(TAG, (("AVG ms/call " + mName) + ": ") + java.lang.String.format("%.1f", (mTotalTime * 1.0F) / mNumCalls));
            mTotalTime = 0;
            mNumCalls = 0;
        }
    }
}

