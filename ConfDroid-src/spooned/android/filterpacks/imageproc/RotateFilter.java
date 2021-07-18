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
public class RotateFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "angle")
    private int mAngle;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private int mOutputWidth;

    private int mOutputHeight;

    public RotateFilter(java.lang.String name) {
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
                shaderProgram.setClearsOutput(true);
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
            updateParameters();
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create program if not created already
        if ((mProgram == null) || (inputFormat.getTarget() != mTarget)) {
            initProgram(context, inputFormat.getTarget());
        }
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            mWidth = inputFormat.getWidth();
            mHeight = inputFormat.getHeight();
            mOutputWidth = mWidth;
            mOutputHeight = mHeight;
            updateParameters();
        }
        // Create output frame
        android.filterfw.core.FrameFormat outputFormat = android.filterfw.format.ImageFormat.create(mOutputWidth, mOutputHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(outputFormat);
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private void updateParameters() {
        float sinTheta;
        float cosTheta;
        if ((mAngle % 90) == 0) {
            if ((mAngle % 180) == 0) {
                sinTheta = 0.0F;
                cosTheta = ((mAngle % 360) == 0) ? 1.0F : -1.0F;
            } else {
                cosTheta = 0.0F;
                sinTheta = (((mAngle + 90) % 360) == 0) ? -1.0F : 1.0F;
                mOutputWidth = mHeight;
                mOutputHeight = mWidth;
            }
        } else {
            throw new java.lang.RuntimeException("degree has to be multiply of 90.");
        }
        android.filterfw.geometry.Point x0 = new android.filterfw.geometry.Point(0.5F * (((-cosTheta) + sinTheta) + 1.0F), 0.5F * (((-sinTheta) - cosTheta) + 1.0F));
        android.filterfw.geometry.Point x1 = new android.filterfw.geometry.Point(0.5F * ((cosTheta + sinTheta) + 1.0F), 0.5F * ((sinTheta - cosTheta) + 1.0F));
        android.filterfw.geometry.Point x2 = new android.filterfw.geometry.Point(0.5F * (((-cosTheta) - sinTheta) + 1.0F), 0.5F * (((-sinTheta) + cosTheta) + 1.0F));
        android.filterfw.geometry.Point x3 = new android.filterfw.geometry.Point(0.5F * ((cosTheta - sinTheta) + 1.0F), 0.5F * ((sinTheta + cosTheta) + 1.0F));
        android.filterfw.geometry.Quad quad = new android.filterfw.geometry.Quad(x0, x1, x2, x3);
        ((android.filterfw.core.ShaderProgram) (mProgram)).setTargetRegion(quad);
    }
}

