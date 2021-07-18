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
public class FrameFetch extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFinalPort(name = "format", hasDefault = true)
    private android.filterfw.core.FrameFormat mFormat;

    @android.filterfw.core.GenerateFieldPort(name = "key")
    private java.lang.String mKey;

    @android.filterfw.core.GenerateFieldPort(name = "repeatFrame", hasDefault = true)
    private boolean mRepeatFrame = false;

    public FrameFetch(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addOutputPort("frame", mFormat == null ? android.filterfw.core.FrameFormat.unspecified() : mFormat);
    }

    public void process(android.filterfw.core.FilterContext context) {
        android.filterfw.core.Frame output = context.fetchFrame(mKey);
        if (output != null) {
            pushOutput("frame", output);
            if (!mRepeatFrame) {
                closeOutputPort("frame");
            }
        } else {
            delayNextProcess(250);
        }
    }
}

