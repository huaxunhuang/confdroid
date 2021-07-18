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
package android.filterpacks.imageproc;


/**
 *
 *
 * @unknown 
 */
public abstract class SimpleImageFilter extends android.filterfw.core.Filter {
    protected int mCurrentTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    protected android.filterfw.core.Program mProgram;

    protected java.lang.String mParameterName;

    public SimpleImageFilter(java.lang.String name, java.lang.String parameterName) {
        super(name);
        mParameterName = parameterName;
    }

    @java.lang.Override
    public void setupPorts() {
        if (mParameterName != null) {
            try {
                java.lang.reflect.Field programField = android.filterpacks.imageproc.SimpleImageFilter.class.getDeclaredField("mProgram");
                addProgramPort(mParameterName, mParameterName, programField, float.class, false);
            } catch (java.lang.NoSuchFieldException e) {
                throw new java.lang.RuntimeException("Internal Error: mProgram field not found!");
            }
        }
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Create program if not created already
        updateProgramWithTarget(inputFormat.getTarget(), context);
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    protected void updateProgramWithTarget(int target, android.filterfw.core.FilterContext context) {
        if (target != mCurrentTarget) {
            switch (target) {
                case android.filterfw.core.FrameFormat.TARGET_NATIVE :
                    mProgram = getNativeProgram(context);
                    break;
                case android.filterfw.core.FrameFormat.TARGET_GPU :
                    mProgram = getShaderProgram(context);
                    break;
                default :
                    mProgram = null;
                    break;
            }
            if (mProgram == null) {
                throw new java.lang.RuntimeException(("Could not create a program for image filter " + this) + "!");
            }
            initProgramInputs(mProgram, context);
            mCurrentTarget = target;
        }
    }

    protected abstract android.filterfw.core.Program getNativeProgram(android.filterfw.core.FilterContext context);

    protected abstract android.filterfw.core.Program getShaderProgram(android.filterfw.core.FilterContext context);
}

