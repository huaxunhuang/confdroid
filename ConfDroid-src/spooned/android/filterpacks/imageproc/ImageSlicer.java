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


public class ImageSlicer extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "xSlices")
    private int mXSlices;

    @android.filterfw.core.GenerateFieldPort(name = "ySlices")
    private int mYSlices;

    @android.filterfw.core.GenerateFieldPort(name = "padSize")
    private int mPadSize;

    // The current slice index from 0 to xSlices * ySlices
    private int mSliceIndex;

    private android.filterfw.core.Frame mOriginalFrame;

    private android.filterfw.core.Program mProgram;

    private int mInputWidth;

    private int mInputHeight;

    private int mSliceWidth;

    private int mSliceHeight;

    private int mOutputWidth;

    private int mOutputHeight;

    public ImageSlicer(java.lang.String name) {
        super(name);
        mSliceIndex = 0;
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    private void calcOutputFormatForInput(android.filterfw.core.Frame frame) {
        // calculate the output size based on input size, xSlices, and ySlices
        mInputWidth = frame.getFormat().getWidth();
        mInputHeight = frame.getFormat().getHeight();
        mSliceWidth = ((mInputWidth + mXSlices) - 1) / mXSlices;
        mSliceHeight = ((mInputHeight + mYSlices) - 1) / mYSlices;
        mOutputWidth = mSliceWidth + (mPadSize * 2);
        mOutputHeight = mSliceHeight + (mPadSize * 2);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        if (mSliceIndex == 0) {
            mOriginalFrame = pullInput("image");
            calcOutputFormatForInput(mOriginalFrame);
        }
        android.filterfw.core.FrameFormat inputFormat = mOriginalFrame.getFormat();
        android.filterfw.core.MutableFrameFormat outputFormat = inputFormat.mutableCopy();
        outputFormat.setDimensions(mOutputWidth, mOutputHeight);
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(outputFormat);
        // Create the program if not created already
        if (mProgram == null) {
            mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        }
        // Calculate the four corner of the source region
        int xSliceIndex = mSliceIndex % mXSlices;
        int ySliceIndex = mSliceIndex / mXSlices;
        // TODO(rslin) : not sure shifting by 0.5 is needed.
        float x0 = ((xSliceIndex * mSliceWidth) - mPadSize) / ((float) (mInputWidth));
        float y0 = ((ySliceIndex * mSliceHeight) - mPadSize) / ((float) (mInputHeight));
        ((android.filterfw.core.ShaderProgram) (mProgram)).setSourceRect(x0, y0, ((float) (mOutputWidth)) / mInputWidth, ((float) (mOutputHeight)) / mInputHeight);
        // Process
        mProgram.process(mOriginalFrame, output);
        mSliceIndex++;
        if (mSliceIndex == (mXSlices * mYSlices)) {
            mSliceIndex = 0;
            mOriginalFrame.release();
            setWaitsOnInputPort("image", true);
        } else {
            // Retain the original frame so it can be used next time.
            mOriginalFrame.retain();
            setWaitsOnInputPort("image", false);
        }
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

