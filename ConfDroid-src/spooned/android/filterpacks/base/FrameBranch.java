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
public class FrameBranch extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFinalPort(name = "outputs", hasDefault = true)
    private int mNumberOfOutputs = 2;

    public FrameBranch(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addInputPort("in");
        for (int i = 0; i < mNumberOfOutputs; ++i) {
            addOutputBasedOnInput("out" + i, "in");
        }
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("in");
        // Push output
        for (int i = 0; i < mNumberOfOutputs; ++i) {
            pushOutput("out" + i, input);
        }
    }
}

