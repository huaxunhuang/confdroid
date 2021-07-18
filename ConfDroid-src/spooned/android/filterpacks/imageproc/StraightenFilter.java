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
public class StraightenFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "angle", hasDefault = true)
    private float mAngle = 0.0F;

    @android.filterfw.core.GenerateFieldPort(name = "maxAngle", hasDefault = true)
    private float mMaxAngle = 45.0F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private static final float DEGREE_TO_RADIAN = ((float) (java.lang.Math.PI)) / 180.0F;

    public StraightenFilter(java.lang.String name) {
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
        // Create output frame
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            mWidth = inputFormat.getWidth();
            mHeight = inputFormat.getHeight();
            updateParameters();
        }
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private void updateParameters() {
        float cosTheta = ((float) (java.lang.Math.cos(mAngle * android.filterpacks.imageproc.StraightenFilter.DEGREE_TO_RADIAN)));
        float sinTheta = ((float) (java.lang.Math.sin(mAngle * android.filterpacks.imageproc.StraightenFilter.DEGREE_TO_RADIAN)));
        if (mMaxAngle <= 0)
            throw new java.lang.RuntimeException("Max angle is out of range (0-180).");

        mMaxAngle = (mMaxAngle > 90) ? 90 : mMaxAngle;
        android.filterfw.geometry.Point p0 = new android.filterfw.geometry.Point(((-cosTheta) * mWidth) + (sinTheta * mHeight), ((-sinTheta) * mWidth) - (cosTheta * mHeight));
        android.filterfw.geometry.Point p1 = new android.filterfw.geometry.Point((cosTheta * mWidth) + (sinTheta * mHeight), (sinTheta * mWidth) - (cosTheta * mHeight));
        android.filterfw.geometry.Point p2 = new android.filterfw.geometry.Point(((-cosTheta) * mWidth) - (sinTheta * mHeight), ((-sinTheta) * mWidth) + (cosTheta * mHeight));
        android.filterfw.geometry.Point p3 = new android.filterfw.geometry.Point((cosTheta * mWidth) - (sinTheta * mHeight), (sinTheta * mWidth) + (cosTheta * mHeight));
        float maxWidth = ((float) (java.lang.Math.max(java.lang.Math.abs(p0.x), java.lang.Math.abs(p1.x))));
        float maxHeight = ((float) (java.lang.Math.max(java.lang.Math.abs(p0.y), java.lang.Math.abs(p1.y))));
        float scale = 0.5F * java.lang.Math.min(mWidth / maxWidth, mHeight / maxHeight);
        p0.set(((scale * p0.x) / mWidth) + 0.5F, ((scale * p0.y) / mHeight) + 0.5F);
        p1.set(((scale * p1.x) / mWidth) + 0.5F, ((scale * p1.y) / mHeight) + 0.5F);
        p2.set(((scale * p2.x) / mWidth) + 0.5F, ((scale * p2.y) / mHeight) + 0.5F);
        p3.set(((scale * p3.x) / mWidth) + 0.5F, ((scale * p3.y) / mHeight) + 0.5F);
        android.filterfw.geometry.Quad quad = new android.filterfw.geometry.Quad(p0, p1, p2, p3);
        ((android.filterfw.core.ShaderProgram) (mProgram)).setSourceRegion(quad);
    }
}

