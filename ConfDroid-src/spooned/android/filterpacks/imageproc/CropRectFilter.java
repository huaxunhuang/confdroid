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
public class CropRectFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "xorigin")
    private int mXorigin;

    @android.filterfw.core.GenerateFieldPort(name = "yorigin")
    private int mYorigin;

    @android.filterfw.core.GenerateFieldPort(name = "width")
    private int mOutputWidth;

    @android.filterfw.core.GenerateFieldPort(name = "height")
    private int mOutputHeight;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    public CropRectFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
        addOutputBasedOnInput("image", "image");
    }

    public void initProgram(android.filterfw.core.FilterContext context, int target) {
        switch (target) {
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                android.filterfw.core.ShaderProgram shaderProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Sharpen does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mProgram != null) {
            updateSourceRect(mWidth, mHeight);
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create output frame
        android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ImageFormat.create(mOutputWidth, mOutputHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(outputFormat);
        // Create program if not created already
        if ((mProgram == null) || (inputFormat.getTarget() != mTarget)) {
            initProgram(context, inputFormat.getTarget());
        }
        // Check if the frame size has changed
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            updateSourceRect(inputFormat.getWidth(), inputFormat.getHeight());
        }
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    void updateSourceRect(int width, int height) {
        mWidth = width;
        mHeight = height;
        /* Log.e("CropFilter", mWidth + ", " + mHeight + ", " +
        (float) mXorigin / mWidth + ", " +
        (float) mYorigin / mHeight + ", " +
        (float) mOutputWidth / mWidth + ", " +
        (float) mOutputHeight / mHeight);
         */
        ((android.filterfw.core.ShaderProgram) (mProgram)).setSourceRect(((float) (mXorigin)) / mWidth, ((float) (mYorigin)) / mHeight, ((float) (mOutputWidth)) / mWidth, ((float) (mOutputHeight)) / mHeight);
    }
}

