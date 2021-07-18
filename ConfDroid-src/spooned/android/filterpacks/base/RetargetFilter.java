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
package android.filterpacks.base;


/**
 *
 *
 * @unknown 
 */
public class RetargetFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFinalPort(name = "target", hasDefault = false)
    private java.lang.String mTargetString;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private int mTarget = -1;

    public RetargetFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        // Setup target
        mTarget = android.filterfw.core.FrameFormat.readTargetString(mTargetString);
        // Add ports
        addInputPort("frame");
        addOutputBasedOnInput("frame", "frame");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        android.filterfw.core.MutableFrameFormat retargeted = inputFormat.mutableCopy();
        retargeted.setTarget(mTarget);
        return retargeted;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("frame");
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().duplicateFrameToTarget(input, mTarget);
        // Push output
        pushOutput("frame", output);
        // Release pushed frame
        output.release();
    }
}

