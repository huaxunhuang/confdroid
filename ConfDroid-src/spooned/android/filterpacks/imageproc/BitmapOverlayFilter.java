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
public class BitmapOverlayFilter extends android.filterfw.core.Filter {
    @android.filterfw.core.GenerateFieldPort(name = "bitmap")
    private android.graphics.Bitmap mBitmap;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Program mProgram;

    private android.filterfw.core.Frame mFrame;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mOverlayShader = "precision mediump float;\n" + ((((((("uniform sampler2D tex_sampler_0;\n" + "uniform sampler2D tex_sampler_1;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 original = texture2D(tex_sampler_0, v_texcoord);\n") + "  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n") + "  gl_FragColor = vec4(original.rgb * (1.0 - mask.a) + mask.rgb, 1.0);\n") + "}\n");

    public BitmapOverlayFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mOverlayShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                break;
            default :
                throw new java.lang.RuntimeException((("Filter FisheyeFilter does not support frames of " + "target ") + target) + "!");
        }
        mTarget = target;
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mFrame != null) {
            mFrame.release();
            mFrame = null;
        }
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
        if (mBitmap != null) {
            android.filterfw.core.Frame frame = createBitmapFrame(context);
            // Process
            android.filterfw.core.Frame[] inputs = new android.filterfw.core.Frame[]{ input, frame };
            mProgram.process(inputs, output);
            frame.release();
        } else {
            output.setDataFromFrame(input);
        }
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
    }

    private android.filterfw.core.Frame createBitmapFrame(android.filterfw.core.FilterContext context) {
        android.filterfw.core.FrameFormat format = android.filterfw.format.ImageFormat.create(mBitmap.getWidth(), mBitmap.getHeight(), android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        android.filterfw.core.Frame frame = context.getFrameManager().newFrame(format);
        frame.setBitmap(mBitmap);
        mBitmap.recycle();
        mBitmap = null;
        return frame;
    }
}

