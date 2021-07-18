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
public class CropFilter extends android.filterfw.core.Filter {
    private android.filterfw.core.Program mProgram;

    private android.filterfw.core.FrameFormat mLastFormat = null;

    @android.filterfw.core.GenerateFieldPort(name = "owidth")
    private int mOutputWidth = -1;

    @android.filterfw.core.GenerateFieldPort(name = "oheight")
    private int mOutputHeight = -1;

    @android.filterfw.core.GenerateFieldPort(name = "fillblack")
    private boolean mFillBlack = false;

    public CropFilter(java.lang.String name) {
        super(name);
    }

    private final java.lang.String mFragShader = "precision mediump float;\n" + (((((((((((((("uniform sampler2D tex_sampler_0;\n" + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  const vec2 lo = vec2(0.0, 0.0);\n") + "  const vec2 hi = vec2(1.0, 1.0);\n") + "  const vec4 black = vec4(0.0, 0.0, 0.0, 1.0);\n") + "  bool out_of_bounds =\n") + "    any(lessThan(v_texcoord, lo)) ||\n") + "    any(greaterThan(v_texcoord, hi));\n") + "  if (out_of_bounds) {\n") + "    gl_FragColor = black;\n") + "  } else {\n") + "    gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n") + "  }\n") + "}\n");

    @java.lang.Override
    public void setupPorts() {
        addMaskedInputPort("image", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
        addMaskedInputPort("box", android.filterfw.format.ObjectFormat.fromClass(android.filterfw.geometry.Quad.class, android.filterfw.core.FrameFormat.TARGET_SIMPLE));
        addOutputBasedOnInput("image", "image");
    }

    @java.lang.Override
    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        // Make sure output size is set to unspecified, as we do not know what we will be resizing
        // to.
        android.filterfw.core.MutableFrameFormat outputFormat = inputFormat.mutableCopy();
        outputFormat.setDimensions(android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED);
        return outputFormat;
    }

    protected void createProgram(android.filterfw.core.FilterContext context, android.filterfw.core.FrameFormat format) {
        // TODO: Add CPU version
        if ((mLastFormat != null) && (mLastFormat.getTarget() == format.getTarget()))
            return;

        mLastFormat = format;
        mProgram = null;
        switch (format.getTarget()) {
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                if (mFillBlack)
                    mProgram = new android.filterfw.core.ShaderProgram(context, mFragShader);
                else
                    mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);

                break;
        }
        if (mProgram == null) {
            throw new java.lang.RuntimeException(("Could not create a program for crop filter " + this) + "!");
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        // Get input frame
        android.filterfw.core.Frame imageFrame = pullInput("image");
        android.filterfw.core.Frame boxFrame = pullInput("box");
        createProgram(env, imageFrame.getFormat());
        // Get the box
        android.filterfw.geometry.Quad box = ((android.filterfw.geometry.Quad) (boxFrame.getObjectValue()));
        // Create output format
        android.filterfw.core.MutableFrameFormat outputFormat = imageFrame.getFormat().mutableCopy();
        outputFormat.setDimensions(mOutputWidth == (-1) ? outputFormat.getWidth() : mOutputWidth, mOutputHeight == (-1) ? outputFormat.getHeight() : mOutputHeight);
        // Create output frame
        android.filterfw.core.Frame output = env.getFrameManager().newFrame(outputFormat);
        // Set the program parameters
        if (mProgram instanceof android.filterfw.core.ShaderProgram) {
            android.filterfw.core.ShaderProgram shaderProgram = ((android.filterfw.core.ShaderProgram) (mProgram));
            shaderProgram.setSourceRegion(box);
        }
        mProgram.process(imageFrame, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

