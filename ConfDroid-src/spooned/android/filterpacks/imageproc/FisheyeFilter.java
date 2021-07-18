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
public class FisheyeFilter extends android.filterfw.core.Filter {
    private static final java.lang.String TAG = "FisheyeFilter";

    // This parameter has range between 0 and 1. It controls the effect of radial distortion.
    // The larger the value, the more prominent the distortion effect becomes (a straight line
    // becomes a curve).
    @android.filterfw.core.GenerateFieldPort(name = "scale", hasDefault = true)
    private float mScale = 0.0F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private static final java.lang.String mFisheyeShader = "precision mediump float;\n" + (((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform vec2 scale;\n") + "uniform float alpha;\n") + "uniform float radius2;\n") + "uniform float factor;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  const float m_pi_2 = 1.570963;\n") + "  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n") + "  float dist = length(coord * scale);\n") + "  float radian = m_pi_2 - atan(alpha * sqrt(radius2 - dist * dist), dist);\n") + "  float scalar = radian * factor / dist;\n") + "  vec2 new_coord = coord * scalar + vec2(0.5, 0.5);\n") + "  gl_FragColor = texture2D(tex_sampler_0, new_coord);\n") + "}\n");

    public FisheyeFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, android.filterpacks.imageproc.FisheyeFilter.mFisheyeShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter FisheyeFilter does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Create program if not created already
        if ((mProgram == null) || (inputFormat.getTarget() != mTarget)) {
            initProgram(context, inputFormat.getTarget());
        }
        // Check if the frame size has changed
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            updateFrameSize(inputFormat.getWidth(), inputFormat.getHeight());
        }
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mProgram != null) {
            updateProgramParams();
        }
    }

    private void updateFrameSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        updateProgramParams();
    }

    private void updateProgramParams() {
        final float pi = 3.1415927F;
        float[] scale = new float[2];
        if (mWidth > mHeight) {
            scale[0] = 1.0F;
            scale[1] = ((float) (mHeight)) / mWidth;
        } else {
            scale[0] = ((float) (mWidth)) / mHeight;
            scale[1] = 1.0F;
        }
        float alpha = (mScale * 2.0F) + 0.75F;
        float bound2 = 0.25F * ((scale[0] * scale[0]) + (scale[1] * scale[1]));
        float bound = ((float) (java.lang.Math.sqrt(bound2)));
        float radius = 1.15F * bound;
        float radius2 = radius * radius;
        float max_radian = (0.5F * pi) - ((float) (java.lang.Math.atan((alpha / bound) * ((float) (java.lang.Math.sqrt(radius2 - bound2))))));
        float factor = bound / max_radian;
        mProgram.setHostValue("scale", scale);
        mProgram.setHostValue("radius2", radius2);
        mProgram.setHostValue("factor", factor);
        mProgram.setHostValue("alpha", alpha);
    }
}

