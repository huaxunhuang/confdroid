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


public class DocumentaryFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private java.util.Random mRandom;

    private int mWidth = 0;

    private int mHeight = 0;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mDocumentaryShader = "precision mediump float;\n" + (((((((((((((((((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform vec2 seed;\n") + "uniform float stepsize;\n") + "uniform float inv_max_dist;\n") + "uniform vec2 scale;\n") + "varying vec2 v_texcoord;\n") + "float rand(vec2 loc) {\n") + "  float theta1 = dot(loc, vec2(0.9898, 0.233));\n") + "  float theta2 = dot(loc, vec2(12.0, 78.0));\n") + "  float value = cos(theta1) * sin(theta2) + sin(theta1) * cos(theta2);\n") + // keep value of part1 in range: (2^-14 to 2^14).
    "  float temp = mod(197.0 * value, 1.0) + value;\n") + "  float part1 = mod(220.0 * temp, 1.0) + temp;\n") + "  float part2 = value * 0.5453;\n") + "  float part3 = cos(theta1 + theta2) * 0.43758;\n") + "  return fract(part1 + part2 + part3);\n") + "}\n") + "void main() {\n") + // black white
    "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float dither = rand(v_texcoord + seed);\n") + "  vec3 xform = clamp(2.0 * color.rgb, 0.0, 1.0);\n") + "  vec3 temp = clamp(2.0 * (color.rgb + stepsize), 0.0, 1.0);\n") + "  vec3 new_color = clamp(xform + (temp - xform) * (dither - 0.5), 0.0, 1.0);\n") + // grayscale
    "  float gray = dot(new_color, vec3(0.299, 0.587, 0.114));\n") + "  new_color = vec3(gray, gray, gray);\n") + // vignette
    "  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n") + "  float dist = length(coord * scale);\n") + "  float lumen = 0.85 / (1.0 + exp((dist * inv_max_dist - 0.83) * 20.0)) + 0.15;\n") + "  gl_FragColor = vec4(new_color * lumen, color.a);\n") + "}\n");

    public DocumentaryFilter(java.lang.String name) {
        super(name);
        java.util.Date date = new java.util.Date();
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mDocumentaryShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Sharpen does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
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
            mProgram.setHostValue("stepsize", 1.0F / 255.0F);
            float[] seed = new float[]{ mRandom.nextFloat(), mRandom.nextFloat() };
            mProgram.setHostValue("seed", seed);
        }
    }
}

