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
public class ResizeFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "owidth")
    private int mOWidth;

    @android.filterfw.core.GenerateFieldPort(name = "oheight")
    private int mOHeight;

    @android.filterfw.core.GenerateFieldPort(name = "keepAspectRatio", hasDefault = true)
    private boolean mKeepAspectRatio = false;

    @android.filterfw.core.GenerateFieldPort(name = "generateMipMap", hasDefault = true)
    private boolean mGenerateMipMap = false;

    private android.filterfw.core.Program mProgram;

    private android.filterfw.core.FrameFormat mLastFormat = null;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private int mInputChannels;

    public ResizeFilter(java.lang.String name) {
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

    protected void createProgram(android.filterfw.core.FilterContext context, android.filterfw.core.FrameFormat format) {
        if ((mLastFormat != null) && (mLastFormat.getTarget() == format.getTarget()))
            return;

        mLastFormat = format;
        switch (format.getTarget()) {
            case android.filterfw.core.FrameFormat.TARGET_NATIVE :
                throw new java.lang.RuntimeException("Native ResizeFilter not implemented yet!");
            case android.filterfw.core.FrameFormat.TARGET_GPU :
                android.filterfw.core.ShaderProgram prog = android.filterfw.core.ShaderProgram.createIdentity(context);
                mProgram = prog;
                break;
            default :
                throw new java.lang.RuntimeException("ResizeFilter could not create suitable program!");
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext env) {
        // Get input frame
        android.filterfw.core.Frame input = pullInput("image");
        createProgram(env, input.getFormat());
        // Create output frame
        android.filterfw.core.MutableFrameFormat outputFormat = input.getFormat().mutableCopy();
        if (mKeepAspectRatio) {
            android.filterfw.core.FrameFormat inputFormat = input.getFormat();
            mOHeight = (mOWidth * inputFormat.getHeight()) / inputFormat.getWidth();
        }
        outputFormat.setDimensions(mOWidth, mOHeight);
        android.filterfw.core.Frame output = env.getFrameManager().newFrame(outputFormat);
        // Process
        if (mGenerateMipMap) {
            android.filterfw.core.GLFrame mipmapped = ((android.filterfw.core.GLFrame) (env.getFrameManager().newFrame(input.getFormat())));
            mipmapped.setTextureParameter(android.opengl.GLES20.GL_TEXTURE_MIN_FILTER, android.opengl.GLES20.GL_LINEAR_MIPMAP_NEAREST);
            mipmapped.setDataFromFrame(input);
            mipmapped.generateMipMap();
            mProgram.process(mipmapped, output);
            mipmapped.release();
        } else {
            mProgram.process(input, output);
        }
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }
}

