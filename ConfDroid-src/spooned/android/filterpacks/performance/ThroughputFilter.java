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
package android.filterpacks.performance;


/**
 *
 *
 * @unknown 
 */
public class ThroughputFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "period", hasDefault = true)
    private int mPeriod = 5;

    private long mLastTime = 0;

    private int mTotalFrameCount = 0;

    private int mPeriodFrameCount = 0;

    private android.filterfw.core.FrameFormat mOutputFormat;

    public ThroughputFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        // Add input ports
        addInputPort("frame");
        // Add output ports
        mOutputFormat = android.filterfw.format.ObjectFormat.fromClass(android.filterpacks.performance.Throughput.class, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        addOutputBasedOnInput("frame", "frame");
        addOutputPort("throughput", mOutputFormat);
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext env) {
        mTotalFrameCount = 0;
        mPeriodFrameCount = 0;
        mLastTime = 0;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Pass through input frame
        android.filterfw.core.Frame input = pullInput("frame");
        pushOutput("frame", input);
        // Update stats
        ++mTotalFrameCount;
        ++mPeriodFrameCount;
        // Check clock
        if (mLastTime == 0) {
            mLastTime = android.os.SystemClock.elapsedRealtime();
        }
        long curTime = android.os.SystemClock.elapsedRealtime();
        // Output throughput info if time period is up
        if ((curTime - mLastTime) >= (mPeriod * 1000)) {
            android.filterfw.core.FrameFormat inputFormat = input.getFormat();
            int pixelCount = inputFormat.getWidth() * inputFormat.getHeight();
            android.filterpacks.performance.Throughput throughput = new android.filterpacks.performance.Throughput(mTotalFrameCount, mPeriodFrameCount, mPeriod, pixelCount);
            android.filterfw.core.Frame throughputFrame = context.getFrameManager().newFrame(mOutputFormat);
            throughputFrame.setObjectValue(throughput);
            pushOutput("throughput", throughputFrame);
            mLastTime = curTime;
            mPeriodFrameCount = 0;
        }
    }
}

