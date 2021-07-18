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
public class RedEyeFilter extends android.filterfw.core.Filter {
    private static final float RADIUS_RATIO = 0.06F;

    private static final float MIN_RADIUS = 10.0F;

    private static final float DEFAULT_RED_INTENSITY = 1.3F;

    @android.filterfw.core.GenerateFieldPort(name = "centers")
    private float[] mCenters;

    @android.filterfw.core.GenerateFieldPort(name = "tile_size", hasDefault = true)
    private int mTileSize = 640;

    private android.filterfw.core.Frame mRedEyeFrame;

    private android.graphics.Bitmap mRedEyeBitmap;

    private final android.graphics.Canvas mCanvas = new android.graphics.Canvas();

    private final android.graphics.Paint mPaint = new android.graphics.Paint();

    private float mRadius;

    private int mWidth = 0;

    private int mHeight = 0;

    private android.filterfw.core.Program mProgram;

    private int mTarget = android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED;

    private final java.lang.String mRedEyeShader = "precision mediump float;\n" + ((((((((((((((("uniform sampler2D tex_sampler_0;\n" + "uniform sampler2D tex_sampler_1;\n") + "uniform float intensity;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n") + "  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n") + "  if (mask.a > 0.0) {\n") + "    float green_blue = color.g + color.b;\n") + "    float red_intensity = color.r / green_blue;\n") + "    if (red_intensity > intensity) {\n") + "      color.r = 0.5 * green_blue;\n") + "    }\n") + "  }\n") + "  gl_FragColor = color;\n") + "}\n");

    public RedEyeFilter(java.lang.String name) {
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
                android.filterfw.core.ShaderProgram shaderProgram = new android.filterfw.core.ShaderProgram(context, mRedEyeShader);
                shaderProgram.setMaximumTileSize(mTileSize);
                mProgram = shaderProgram;
                mProgram.setHostValue("intensity", android.filterpacks.imageproc.RedEyeFilter.DEFAULT_RED_INTENSITY);
                break;
            default :
                throw new java.lang.RuntimeException((("Filter RedEye does not support frames of " + "target ") + target) + "!");
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
        // Check if the frame size has changed
        if ((inputFormat.getWidth() != mWidth) || (inputFormat.getHeight() != mHeight)) {
            mWidth = inputFormat.getWidth();
            mHeight = inputFormat.getHeight();
        }
        createRedEyeFrame(context);
        // Process
        android.filterfw.core.Frame[] inputs = new android.filterfw.core.Frame[]{ input, mRedEyeFrame };
        mProgram.process(inputs, output);
        // Push output
        pushOutput("image", output);
        // Release pushed frame
        output.release();
        // Release unused frame
        mRedEyeFrame.release();
        mRedEyeFrame = null;
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mProgram != null) {
            updateProgramParams();
        }
    }

    private void createRedEyeFrame(android.filterfw.core.FilterContext context) {
        int bitmapWidth = mWidth / 2;
        int bitmapHeight = mHeight / 2;
        android.graphics.Bitmap redEyeBitmap = android.graphics.Bitmap.createBitmap(bitmapWidth, bitmapHeight, android.graphics.Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(redEyeBitmap);
        mPaint.setColor(android.graphics.Color.WHITE);
        mRadius = java.lang.Math.max(android.filterpacks.imageproc.RedEyeFilter.MIN_RADIUS, android.filterpacks.imageproc.RedEyeFilter.RADIUS_RATIO * java.lang.Math.min(bitmapWidth, bitmapHeight));
        for (int i = 0; i < mCenters.length; i += 2) {
            mCanvas.drawCircle(mCenters[i] * bitmapWidth, mCenters[i + 1] * bitmapHeight, mRadius, mPaint);
        }
        android.filterfw.core.FrameFormat format = android.filterfw.format.ImageFormat.create(bitmapWidth, bitmapHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        mRedEyeFrame = context.getFrameManager().newFrame(format);
        mRedEyeFrame.setBitmap(redEyeBitmap);
        redEyeBitmap.recycle();
    }

    private void updateProgramParams() {
        if ((mCenters.length % 2) == 1) {
            throw new java.lang.RuntimeException("The size of center array must be even.");
        }
    }
}

