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


public class SaturateFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "scale", hasDefault = true)
    private float mScale = 0.0F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mBenProgram;

    private android.filterfw.core.Program mHerfProgram;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mBenSaturateShader = "precision mediump float;\n" + (((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform float scale;\n") + "uniform float shift;\n") + "uniform vec3 weights;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float kv = dot(color.rgb, weights) + shift;\n") + "  vec3 new_color = scale * color.rgb + (1.0 - scale) * kv;\n") + "  gl_FragColor = vec4(new_color, color.a);\n") + "}\n");

    private final java.lang.String mHerfSaturateShader = "precision mediump float;\n" + ((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform vec3 weights;\n") + "uniform vec3 exponents;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float de = dot(color.rgb, weights);\n") + "  float inv_de = 1.0 / de;\n") + "  vec3 new_color = de * pow(color.rgb * inv_de, exponents);\n") + "  float max_color = max(max(max(new_color.r, new_color.g), new_color.b), 1.0);\n") + "  gl_FragColor = vec4(new_color / max_color, color.a);\n") + "}\n");

    public SaturateFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mBenSaturateShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mBenProgram = shaderProgram;
                shaderProgram = new android.filterfw.core.ShaderProgram(context, mHerfSaturateShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mHerfProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Sharpen does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if ((mBenProgram != null) && (mHerfProgram != null)) {
            updateParameters();
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        // Create program if not created already
        if ((mBenProgram == null) || (inputFormat.getTarget() != mTarget)) {
            initProgram(context, inputFormat.getTarget());
            initParameters();
        }
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Process
        if (mScale > 0.0F) {
            mHerfProgram.process(input, output);
        } else {
            mBenProgram.process(input, output);
        }
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private void initParameters() {
        float shift = 1.0F / 255.0F;
        float[] weights = new float[]{ 2.0F / 8.0F, 5.0F / 8.0F, 1.0F / 8.0F };
        mBenProgram.setHostValue("weights", weights);
        mBenProgram.setHostValue("shift", shift);
        mHerfProgram.setHostValue("weights", weights);
        updateParameters();
    }

    private void updateParameters() {
        if (mScale > 0.0F) {
            float[] exponents = new float[3];
            exponents[0] = (0.9F * mScale) + 1.0F;
            exponents[1] = (2.1F * mScale) + 1.0F;
            exponents[2] = (2.7F * mScale) + 1.0F;
            mHerfProgram.setHostValue("exponents", exponents);
        } else {
            mBenProgram.setHostValue("scale", 1.0F + mScale);
        }
    }
}

