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


public class VignetteFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "scale", hasDefault = true)
    private float mScale = 0.0F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final float mSlope = 20.0F;

    private final float mShade = 0.85F;

    private final java.lang.String mVignetteShader = "precision mediump float;\n" + ((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform float range;\n") + "uniform float inv_max_dist;\n") + "uniform float shade;\n") + "uniform vec2 scale;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  const float slope = 20.0;\n") + "  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n") + "  float dist = length(coord * scale);\n") + "  float lumen = shade / (1.0 + exp((dist * inv_max_dist - range) * slope)) + (1.0 - shade);\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  gl_FragColor = vec4(color.rgb * lumen, color.a);\n") + "}\n");

    public VignetteFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return inputFormat;
    }

    public void initProgram(android.filterfw.core.FilterContext context, int target) {
        switch (target) {
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mVignetteShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Sharpen does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    private void initParameters() {
        if (mProgram != null) {
            float[] scale = new float[2];
            if (mWidth > mHeight) {
                scale[0] = 1.0F;
                scale[1] = ((float) (mHeight)) / mWidth;
            } else {
                scale[0] = ((float) (mWidth)) / mHeight;
                scale[1] = 1.0F;
            }
            float max_dist = ((float) (java.lang.Math.sqrt((scale[0] * scale[0]) + (scale[1] * scale[1])))) * 0.5F;
            mProgram.setHostValue("scale", scale);
            mProgram.setHostValue("inv_max_dist", 1.0F / max_dist);
            mProgram.setHostValue("shade", mShade);
            updateParameters();
        }
    }

    private void updateParameters() {
        // The 'range' is between 1.3 to 0.6. When scale is zero then range is 1.3
        // which means no vignette at all because the luminousity difference is
        // less than 1/256 and will cause nothing.
        mProgram.setHostValue("range", 1.3F - (((float) (java.lang.Math.sqrt(mScale))) * 0.7F));
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
        // Check if the frame size has changed
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            mWidth = inputFormat.getWidth();
            mHeight = inputFormat.getHeight();
            initParameters();
        }
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

