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


public class FillLightFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    @android.filterfw.core.GenerateFieldPort(name = "strength", hasDefault = true)
    private float mBacklight = 0.0F;

    private android.filterfw.core.Program mProgram;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mFillLightShader = "precision mediump float;\n" + ((((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform float mult;\n") + "uniform float igamma;\n") + "varying vec2 v_texcoord;\n") + "void main()\n") + "{\n") + "  const vec3 color_weights = vec3(0.25, 0.5, 0.25);\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float lightmask = dot(color.rgb, color_weights);\n") + "  float backmask = (1.0 - lightmask);\n") + "  vec3 ones = vec3(1.0, 1.0, 1.0);\n") + "  vec3 diff = pow(mult * color.rgb, igamma * ones) - color.rgb;\n") + "  diff = min(diff, 1.0);\n") + "  vec3 new_color = min(color.rgb + diff * backmask, 1.0);\n") + "  gl_FragColor = vec4(new_color, color.a);\n") + "}\n");

    public FillLightFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mFillLightShader);
                android.util.Log.e("FillLight", "tile size: " + mTileSize);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter FillLight does not support frames of " + "target ") + target) + "!");
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
            updateParameters();
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
            updateParameters();
        }
    }

    private void updateParameters() {
        float fade_gamma = 0.3F;
        float amt = 1.0F - mBacklight;
        float mult = 1.0F / ((amt * 0.7F) + 0.3F);
        float faded = fade_gamma + ((1.0F - fade_gamma) * mult);
        float igamma = 1.0F / faded;
        mProgram.setHostValue("mult", mult);
        mProgram.setHostValue("igamma", igamma);
    }
}

