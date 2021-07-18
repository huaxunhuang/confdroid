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
package android.filterpacks.text;


/**
 *
 *
 * @unknown 
 */
public class ToUpperCase extends android.filterfw.core.Filter {
    private android.filterfw.core.FrameFormat mOutputFormat;

    public ToUpperCase(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        mOutputFormat = android.filterfw.format.ObjectFormat.fromClass(java.lang.String.class, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        addMaskedInputPort("mixedcase", mOutputFormat);
        addOutputPort("uppercase", mOutputFormat);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        android.filterfw.core.Frame input = pullInput("mixedcase");
        java.lang.String inputString = ((java.lang.String) (input.getObjectValue()));
        android.filterfw.core.Frame output = env.getFrameManager().newFrame(mOutputFormat);
        output.setObjectValue(inputString.toUpperCase(java.util.Locale.getDefault()));
        pushOutput("uppercase", output);
    }
}

