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
package android.filterpacks.ui;


/**
 *
 *
 * @unknown 
 */
public class SurfaceRenderFilter extends android.filterfw.core.Filter implements android.view.SurfaceHolder.Callback {
    private final int RENDERMODE_STRETCH = 0;

    private final int RENDERMODE_FIT = 1;

    private final int RENDERMODE_FILL_CROP = 2;

    /**
     * Required. Sets the destination filter surface view for this
     * node.
     */
    @android.filterfw.core.GenerateFinalPort(name = "surfaceView")
    private android.filterfw.core.FilterSurfaceView mSurfaceView;

    /**
     * Optional. Control how the incoming frames are rendered onto the
     * output. Default is FIT.
     * RENDERMODE_STRETCH: Just fill the output surfaceView.
     * RENDERMODE_FIT: Keep aspect ratio and fit without cropping. May
     * have black bars.
     * RENDERMODE_FILL_CROP: Keep aspect ratio and fit without black
     * bars. May crop.
     */
    @android.filterfw.core.GenerateFieldPort(name = "renderMode", hasDefault = true)
    private java.lang.String mRenderModeString;

    private boolean mIsBound = false;

    private android.filterfw.core.ShaderProgram mProgram;

    private android.filterfw.core.GLFrame mScreen;

    private int mRenderMode = RENDERMODE_FIT;

    private float mAspectRatio = 1.0F;

    private int mScreenWidth;

    private int mScreenHeight;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "SurfaceRenderFilter";

    public SurfaceRenderFilter(java.lang.String name) {
        super(name);
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.ui.SurfaceRenderFilter.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public void setupPorts() {
        // Make sure we have a SurfaceView
        if (mSurfaceView == null) {
            throw new java.lang.RuntimeException("NULL SurfaceView passed to SurfaceRenderFilter");
        }
        // Add input port
        addMaskedInputPort("frame", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
    }

    public void updateRenderMode() {
        if (mRenderModeString != null) {
            if (mRenderModeString.equals("stretch")) {
                mRenderMode = RENDERMODE_STRETCH;
            } else
                if (mRenderModeString.equals("fit")) {
                    mRenderMode = RENDERMODE_FIT;
                } else
                    if (mRenderModeString.equals("fill_crop")) {
                        mRenderMode = RENDERMODE_FILL_CROP;
                    } else {
                        throw new java.lang.RuntimeException(("Unknown render mode '" + mRenderModeString) + "'!");
                    }


        }
        updateTargetRect();
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        // Create identity shader to render, and make sure to render upside-down, as textures
        // are stored internally bottom-to-top.
        mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        mProgram.setSourceRect(0, 1, 1, -1);
        mProgram.setClearsOutput(true);
        mProgram.setClearColor(0.0F, 0.0F, 0.0F);
        updateRenderMode();
        // Create a frame representing the screen
        android.filterfw.core.MutableFrameFormat screenFormat = android.filterfw.format.ImageFormat.create(mSurfaceView.getWidth(), mSurfaceView.getHeight(), android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        mScreen = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(screenFormat, android.filterfw.core.GLFrame.EXISTING_FBO_BINDING, 0)));
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        // Bind surface view to us. This will emit a surfaceCreated and surfaceChanged call that
        // will update our screen width and height.
        mSurfaceView.unbind();
        mSurfaceView.bindToListener(this, context.getGLEnvironment());
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Make sure we are bound to a surface before rendering
        if (!mIsBound) {
            android.util.Log.w("SurfaceRenderFilter", this + ": Ignoring frame as there is no surface to render to!");
            return;
        }
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.ui.SurfaceRenderFilter.TAG, "Starting frame processing");

        android.filterfw.core.GLEnvironment glEnv = mSurfaceView.getGLEnv();
        if (glEnv != context.getGLEnvironment()) {
            throw new java.lang.RuntimeException("Surface created under different GLEnvironment!");
        }
        // Get input frame
        android.filterfw.core.Frame input = pullInput("frame");
        boolean createdFrame = false;
        float currentAspectRatio = ((float) (input.getFormat().getWidth())) / input.getFormat().getHeight();
        if (currentAspectRatio != mAspectRatio) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.ui.SurfaceRenderFilter.TAG, (("New aspect ratio: " + currentAspectRatio) + ", previously: ") + mAspectRatio);

            mAspectRatio = currentAspectRatio;
            updateTargetRect();
        }
        // See if we need to copy to GPU
        android.filterfw.core.Frame gpuFrame = null;
        if (mLogVerbose)
            android.util.Log.v("SurfaceRenderFilter", "Got input format: " + input.getFormat());

        int target = input.getFormat().getTarget();
        if (target != android.filterfw.core.FrameFormat.TARGET_GPU) {
            gpuFrame = context.getFrameManager().duplicateFrameToTarget(input, android.filterfw.core.FrameFormat.TARGET_GPU);
            createdFrame = true;
        } else {
            gpuFrame = input;
        }
        // Activate our surface
        glEnv.activateSurfaceWithId(mSurfaceView.getSurfaceId());
        // Process
        mProgram.process(gpuFrame, mScreen);
        // And swap buffers
        glEnv.swapBuffers();
        if (createdFrame) {
            gpuFrame.release();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        updateTargetRect();
    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        mSurfaceView.unbind();
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mScreen != null) {
            mScreen.release();
        }
    }

    @java.lang.Override
    public synchronized void surfaceCreated(android.view.SurfaceHolder holder) {
        mIsBound = true;
    }

    @java.lang.Override
    public synchronized void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
        // If the screen is null, we do not care about surface changes (yet). Once we have a
        // screen object, we need to keep track of these changes.
        if (mScreen != null) {
            mScreenWidth = width;
            mScreenHeight = height;
            mScreen.setViewport(0, 0, mScreenWidth, mScreenHeight);
            updateTargetRect();
        }
    }

    @java.lang.Override
    public synchronized void surfaceDestroyed(android.view.SurfaceHolder holder) {
        mIsBound = false;
    }

    private void updateTargetRect() {
        if (((mScreenWidth > 0) && (mScreenHeight > 0)) && (mProgram != null)) {
            float screenAspectRatio = ((float) (mScreenWidth)) / mScreenHeight;
            float relativeAspectRatio = screenAspectRatio / mAspectRatio;
            switch (mRenderMode) {
                case RENDERMODE_STRETCH :
                    mProgram.setTargetRect(0, 0, 1, 1);
                    break;
                case RENDERMODE_FIT :
                    if (relativeAspectRatio > 1.0F) {
                        // Screen is wider than the camera, scale down X
                        mProgram.setTargetRect(0.5F - (0.5F / relativeAspectRatio), 0.0F, 1.0F / relativeAspectRatio, 1.0F);
                    } else {
                        // Screen is taller than the camera, scale down Y
                        mProgram.setTargetRect(0.0F, 0.5F - (0.5F * relativeAspectRatio), 1.0F, relativeAspectRatio);
                    }
                    break;
                case RENDERMODE_FILL_CROP :
                    if (relativeAspectRatio > 1) {
                        // Screen is wider than the camera, crop in Y
                        mProgram.setTargetRect(0.0F, 0.5F - (0.5F * relativeAspectRatio), 1.0F, relativeAspectRatio);
                    } else {
                        // Screen is taller than the camera, crop in X
                        mProgram.setTargetRect(0.5F - (0.5F / relativeAspectRatio), 0.0F, 1.0F / relativeAspectRatio, 1.0F);
                    }
                    break;
            }
        }
    }
}

