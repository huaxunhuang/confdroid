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


public class ImageStitcher extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "xSlices")
    private int mXSlices;

    @android.filterfw.core.GenerateFieldPort(name = "ySlices")
    private int mYSlices;

    @android.filterfw.core.GenerateFieldPort(name = "padSize")
    private int mPadSize;

    private android.filterfw.core.Program mProgram;

    private android.filterfw.core.Frame mOutputFrame;

    private int mInputWidth;

    private int mInputHeight;

    private int mImageWidth;

    private int mImageHeight;

    private int mSliceWidth;

    private int mSliceHeight;

    private int mSliceIndex;

    public ImageStitcher(java.lang.String name) {
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

    private android.filterfw.core.FrameFormat calcOutputFormatForInput(android.filterfw.core.FrameFormat format) {
        android.filterfw.core.MutableFrameFormat outputFormat = format.mutableCopy();
        mInputWidth = format.getWidth();
        mInputHeight = format.getHeight();
        mSliceWidth = mInputWidth - (2 * mPadSize);
        mSliceHeight = mInputHeight - (2 * mPadSize);
        mImageWidth = mSliceWidth * mXSlices;
        mImageHeight = mSliceHeight * mYSlices;
        outputFormat.setDimensions(mImageWidth, mImageHeight);
        return outputFormat;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat format = input.getFormat();
        // Create output frame
        if (mSliceIndex == 0) {
            mOutputFrame = context.getFrameManager().newFrame(calcOutputFormatForInput(format));
        } else {
            if ((format.getWidth() != mInputWidth) || (format.getHeight() != mInputHeight)) {
                // CHECK input format here
                throw new java.lang.RuntimeException("Image size should not change.");
            }
        }
        // Create the program if not created already
        if (mProgram == null) {
            mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        }
        // TODO(rslin) : not sure shifting by 0.5 is needed.
        float x0 = ((float) (mPadSize)) / mInputWidth;
        float y0 = ((float) (mPadSize)) / mInputHeight;
        int outputOffsetX = (mSliceIndex % mXSlices) * mSliceWidth;
        int outputOffsetY = (mSliceIndex / mXSlices) * mSliceHeight;
        float outputWidth = ((float) (java.lang.Math.min(mSliceWidth, mImageWidth - outputOffsetX)));
        float outputHeight = ((float) (java.lang.Math.min(mSliceHeight, mImageHeight - outputOffsetY)));
        // We need to set the source rect as well because the input are padded images.
        ((android.filterfw.core.ShaderProgram) (mProgram)).setSourceRect(x0, y0, outputWidth / mInputWidth, outputHeight / mInputHeight);
        ((android.filterfw.core.ShaderProgram) (mProgram)).setTargetRect(((float) (outputOffsetX)) / mImageWidth, ((float) (outputOffsetY)) / mImageHeight, outputWidth / mImageWidth, outputHeight / mImageHeight);
        // Process this tile
        mProgram.process(input, mOutputFrame);
        mSliceIndex++;
        // Push output
        if (mSliceIndex == (mXSlices * mYSlices)) {
            pushOutput("image", mOutputFrame);
            mOutputFrame.release();
            mSliceIndex = 0;
        }
    }
}

