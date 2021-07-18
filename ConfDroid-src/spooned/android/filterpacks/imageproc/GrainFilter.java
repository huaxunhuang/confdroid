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


public class GrainFilter extends android.filterfw.core.Filter {
    private static final int RAND_THRESHOLD = 128;

    @android.filterfw.core.GenerateFieldPort(name = "strength", hasDefault = true)
    private float mScale = 0.0F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mGrainProgram;

    private android.filterfw.core.Program mNoiseProgram;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private java.util.Random mRandom;

    private final java.lang.String mNoiseShader = "precision mediump float;\n" + (((((((((((((("uniform vec2 seed;\n" + "varying vec2 v_texcoord;\n") + "float rand(vec2 loc) {\n") + "  float theta1 = dot(loc, vec2(0.9898, 0.233));\n") + "  float theta2 = dot(loc, vec2(12.0, 78.0));\n") + "  float value = cos(theta1) * sin(theta2) + sin(theta1) * cos(theta2);\n") + // keep value of part1 in range: (2^-14 to 2^14).
    "  float temp = mod(197.0 * value, 1.0) + value;\n") + "  float part1 = mod(220.0 * temp, 1.0) + temp;\n") + "  float part2 = value * 0.5453;\n") + "  float part3 = cos(theta1 + theta2) * 0.43758;\n") + "  return fract(part1 + part2 + part3);\n") + "}\n") + "void main() {\n") + "  gl_FragColor = vec4(rand(v_texcoord + seed), 0.0, 0.0, 1.0);\n") + "}\n");

    private final java.lang.String mGrainShader = "precision mediump float;\n" + (((((((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform sampler2D tex_sampler_1;\n") + "uniform float scale;\n") + "uniform float stepX;\n") + "uniform float stepY;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  float noise = texture2D(tex_sampler_1, v_texcoord + vec2(-stepX, -stepY)).r * 0.224;\n") + "  noise += texture2D(tex_sampler_1, v_texcoord + vec2(-stepX, stepY)).r * 0.224;\n") + "  noise += texture2D(tex_sampler_1, v_texcoord + vec2(stepX, -stepY)).r * 0.224;\n") + "  noise += texture2D(tex_sampler_1, v_texcoord + vec2(stepX, stepY)).r * 0.224;\n") + "  noise += 0.4448;\n") + "  noise *= scale;\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float energy = 0.33333 * color.r + 0.33333 * color.g + 0.33333 * color.b;\n") + "  float mask = (1.0 - sqrt(energy));\n") + "  float weight = 1.0 - 1.333 * mask * noise;\n") + "  gl_FragColor = vec4(color.rgb * weight, color.a);\n") + "}\n");

    public GrainFilter(java.lang.String name) {
        super(name);
        mRandom = new java.util.Random(new java.util.Date().getTime());
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mNoiseShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mNoiseProgram = shaderProgram;
                shaderProgram = new android.filterfw.core.ShaderProgram(context, mGrainShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mGrainProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Sharpen does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    private void updateParameters() {
        float[] seed = new float[]{ mRandom.nextFloat(), mRandom.nextFloat() };
        mNoiseProgram.setHostValue("seed", seed);
        mGrainProgram.setHostValue("scale", mScale);
    }

    private void updateFrameSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        if (mGrainProgram != null) {
            mGrainProgram.setHostValue("stepX", 0.5F / mWidth);
            mGrainProgram.setHostValue("stepY", 0.5F / mHeight);
            updateParameters();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if ((mGrainProgram != null) && (mNoiseProgram != null)) {
            updateParameters();
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        android.filterfw.core.FrameFormat noiseFormat = android.filterfw.format.ImageFormat.create(inputFormat.getWidth() / 2, inputFormat.getHeight() / 2, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        // Create noise frame
        android.filterfw.core.Frame noiseFrame = context.getFrameManager().newFrame(inputFormat);
        // Create output frame
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(inputFormat);
        // Create program if not created already
        if (((mNoiseProgram == null) || (mGrainProgram == null)) || (inputFormat.getTarget() != mTarget)) {
            initProgram(context, inputFormat.getTarget());
            updateParameters();
        }
        // Check if the frame size has changed
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            updateFrameSize(inputFormat.getWidth(), inputFormat.getHeight());
        }
        android.filterfw.core.Frame[] empty = new android.filterfw.core.Frame[]{  };
        mNoiseProgram.process(empty, noiseFrame);
        // Process
        android.filterfw.core.Frame[] inputs = new android.filterfw.core.Frame[]{ input, noiseFrame };
        mGrainProgram.process(inputs, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
        noiseFrame.release();
    }
}

