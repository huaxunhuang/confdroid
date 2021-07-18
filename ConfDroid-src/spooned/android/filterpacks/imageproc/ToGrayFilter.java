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
public class ToGrayFilter extends android.filterpacks.imageproc.SimpleImageFilter {
    @android.filterfw.core.GenerateFieldPort(name = "invertSource", hasDefault = true)
    private boolean mInvertSource = false;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private static final java.lang.String mColorToGray4Shader = "precision mediump float;\n" + (((((("uniform sampler2D tex_sampler_0;\n" + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  float y = dot(color, vec4(0.299, 0.587, 0.114, 0));\n") + "  gl_FragColor = vec4(y, y, y, color.a);\n") + "}\n");

    public ToGrayFilter(java.lang.String name) {
        super(name, null);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    protected android.filterfw.core.Program getNativeProgram(android.filterfw.core.FilterContext context) {
        throw new java.lang.RuntimeException("Native toGray not implemented yet!");
    }

    @java.lang.Override
    protected android.filterfw.core.Program getShaderProgram(android.filterfw.core.FilterContext context) {
        int inputChannels = getInputFormat("image").getBytesPerSample();
        if (inputChannels != 4) {
            throw new java.lang.RuntimeException(("Unsupported GL input channels: " + inputChannels) + "! Channels must be 4!");
        }
        android.filterfw.core.ShaderProgram program = new android.filterfw.core.ShaderProgram(context, android.filterpacks.imageproc.ToGrayFilter.mColorToGray4Shader);
        program.setMaximumTileSize(mTileSize);
        if (mInvertSource)
            program.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);

        return program;
    }
}

