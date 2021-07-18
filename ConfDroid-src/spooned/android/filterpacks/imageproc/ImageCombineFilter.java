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
public abstract class ImageCombineFilter extends android.filterfw.core.Filter {
    protected android.filterfw.core.Program mProgram;

    protected java.lang.String[] mInputNames;

    protected java.lang.String mOutputName;

    protected java.lang.String mParameterName;

    protected int mCurrentTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    public ImageCombineFilter(java.lang.String name, java.lang.String[] inputNames, java.lang.String outputName, java.lang.String parameterName) {
        super(name);
        mInputNames = inputNames;
        mOutputName = outputName;
        mParameterName = parameterName;
    }

    @java.lang.Override
    public void setupPorts() {
        if (mParameterName != null) {
            try {
                java.lang.reflect.Field programField = android.filterpacks.imageproc.ImageCombineFilter.class.getDeclaredField("mProgram");
                addProgramPort(mParameterName, mParameterName, programField, float.class, false);
            } catch (java.lang.NoSuchFieldException e) {
                throw new java.lang.RuntimeException("Internal Error: mProgram field not found!");
            }
        }
        for (java.lang.String inputName : mInputNames) {
            addMaskedInputPort(inputName, android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
        }
        addOutputBasedOnInput(mOutputName, mInputNames[0]);
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    private void assertAllInputTargetsMatch() {
        int target = getInputFormat(mInputNames[0]).getTarget();
        for (java.lang.String inputName : mInputNames) {
            if (target != getInputFormat(inputName).getTarget()) {
                throw new java.lang.RuntimeException(("Type mismatch of input formats in filter " + this) + ". All input frames must have the same target!");
            }
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Pull input frames
        int i = 0;
        android.filterfw.core.Frame[] inputs = new android.filterfw.core.Frame[mInputNames.length];
        for (java.lang.String inputName : mInputNames) {
            inputs[i++] = pullInput(inputName);
        }
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputs[0].getFormat());
        // Make sure we have a program
        updateProgramWithTarget(inputs[0].getFormat().getTarget(), context);
        // Process
        mProgram.process(inputs, output);
        // Push output
        pushOutput(mOutputName, output);
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

