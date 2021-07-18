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


public class ColorTemperatureFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "scale", hasDefault = true)
    private float mScale = 0.5F;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mColorTemperatureShader = "precision mediump float;\n" + (((((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform float scale;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  vec3 new_color = color.rgb;\n") + "  new_color.r = color.r + color.r * ( 1.0 - color.r) * scale;\n") + "  new_color.b = color.b - color.b * ( 1.0 - color.b) * scale;\n") + "  if (scale > 0.0) { \n") + "    new_color.g = color.g + color.g * ( 1.0 - color.g) * scale * 0.25;\n") + "  }\n") + "  float max_value = max(new_color.r, max(new_color.g, new_color.b));\n") + "  if (max_value > 1.0) { \n") + "     new_color /= max_value;\n") + "  } \n") + "  gl_FragColor = vec4(new_color, color.a);\n") + "}\n");

    public ColorTemperatureFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mColorTemperatureShader);
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
            updateParameters();
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

    private void updateParameters() {
        mProgram.setHostValue("scale", (2.0F * mScale) - 1.0F);
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mProgram != null) {
            updateParameters();
        }
    }
}

