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
public class ToPackedGrayFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "owidth", hasDefault = true)
    private int mOWidth = android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED;

    @android.filterfw.core.GenerateFieldPort(name = "oheight", hasDefault = true)
    private int mOHeight = android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED;

    @android.filterfw.core.GenerateFieldPort(name = "keepAspectRatio", hasDefault = true)
    private boolean mKeepAspectRatio = false;

    private android.filterfw.core.Program mProgram;

    private final java.lang.String mColorToPackedGrayShader = "precision mediump float;\n" + (((((((((("const vec4 coeff_y = vec4(0.299, 0.587, 0.114, 0);\n" + "uniform sampler2D tex_sampler_0;\n") + "uniform float pix_stride;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  for (int i = 0; i < 4; ++i) {\n") + "    vec4 p = texture2D(tex_sampler_0,\n") + "                       v_texcoord + vec2(pix_stride * float(i), 0.0));\n") + "    gl_FragColor[i] = dot(p, coeff_y);\n") + "  }\n") + "}\n");

    public ToPackedGrayFilter(java.lang.String name) {
        super(name);
    }

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return convertInputFormat(inputFormat);
    }

    private void checkOutputDimensions(int outputWidth, int outputHeight) {
        if ((outputWidth <= 0) || (outputHeight <= 0)) {
            throw new java.lang.RuntimeException((("Invalid output dimensions: " + outputWidth) + " ") + outputHeight);
        }
    }

    private android.filterfw.core.FrameFormat convertInputFormat(android.filterfw.core.FrameFormat inputFormat) {
        int ow = mOWidth;
        int oh = mOHeight;
        int w = inputFormat.getWidth();
        int h = inputFormat.getHeight();
        if (mOWidth == android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) {
            ow = w;
        }
        if (mOHeight == android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED) {
            oh = h;
        }
        if (mKeepAspectRatio) {
            // if keep aspect ratio, use the bigger dimension to determine the
            // final output size
            if (w > h) {
                ow = java.lang.Math.max(ow, oh);
                oh = (ow * h) / w;
            } else {
                oh = java.lang.Math.max(ow, oh);
                ow = (oh * w) / h;
            }
        }
        ow = ((ow > 0) && (ow < 4)) ? 4 : (ow / 4) * 4;// ensure width is multiple of 4

        return android.filterfw.format.ImageFormat.create(ow, oh, android.filterfw.format.ImageFormat.COLORSPACE_GRAY, android.filterfw.core.FrameFormat.TARGET_NATIVE);
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        mProgram = new android.filterfw.core.ShaderProgram(context, mColorToPackedGrayShader);
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        android.filterfw.core.Frame input = pullInput("image");
        android.filterfw.core.FrameFormat inputFormat = input.getFormat();
        android.filterfw.core.FrameFormat outputFormat = convertInputFormat(inputFormat);
        int ow = outputFormat.getWidth();
        int oh = outputFormat.getHeight();
        checkOutputDimensions(ow, oh);
        mProgram.setHostValue("pix_stride", 1.0F / ow);
        // Do the RGBA to luminance conversion.
        android.filterfw.core.MutableFrameFormat tempFrameFormat = inputFormat.mutableCopy();
        tempFrameFormat.setDimensions(ow / 4, oh);
        android.filterfw.core.Frame temp = context.getFrameManager().newFrame(tempFrameFormat);
        mProgram.process(input, temp);
        // Read frame from GPU to CPU.
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(outputFormat);
        output.setDataFromFrame(temp);
        temp.release();
        // Push output and yield ownership.
        pushOutput("image", output);
        output.release();
    }
}

