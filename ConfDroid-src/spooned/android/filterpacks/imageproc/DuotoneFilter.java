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


public class DuotoneFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "first_color", hasDefault = true)
    private int mFirstColor = 0xffff0000;

    @android.filterfw.core.GenerateFieldPort(name = "second_color", hasDefault = true)
    private int mSecondColor = 0xffffff00;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mDuotoneShader = "precision mediump float;\n" + ((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform vec3 first;\n") + "uniform vec3 second;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float energy = (color.r + color.g + color.b) * 0.3333;\n") + "  vec3 new_color = (1.0 - energy) * first + energy * second;\n") + "  gl_FragColor = vec4(new_color.rgb, color.a);\n") + "}\n");

    public DuotoneFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mDuotoneShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter Duotone does not support frames of " + "target ") + target) + "!");
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
        updateParameters();
        // Process
        mProgram.process(input, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private void updateParameters() {
        float[] first = new float[]{ android.graphics.Color.red(mFirstColor) / 255.0F, android.graphics.Color.green(mFirstColor) / 255.0F, android.graphics.Color.blue(mFirstColor) / 255.0F };
        float[] second = new float[]{ android.graphics.Color.red(mSecondColor) / 255.0F, android.graphics.Color.green(mSecondColor) / 255.0F, android.graphics.Color.blue(mSecondColor) / 255.0F };
        mProgram.setHostValue("first", first);
        mProgram.setHostValue("second", second);
    }
}

