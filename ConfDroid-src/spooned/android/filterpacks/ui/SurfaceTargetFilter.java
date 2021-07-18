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
public class SurfaceTargetFilter extends android.filterfw.core.Filter {
    private final int RENDERMODE_STRETCH = 0;

    private final int RENDERMODE_FIT = 1;

    private final int RENDERMODE_FILL_CROP = 2;

    /**
     * Required. Sets the destination surface for this node. This assumes that
     * higher-level code is ensuring that the surface is valid, and properly
     * updates Surface parameters if they change.
     */
    @android.filterfw.core.GenerateFinalPort(name = "surface")
    private android.view.Surface mSurface;

    /**
     * Required. Width of the output surface
     */
    @android.filterfw.core.GenerateFieldPort(name = "owidth")
    private int mScreenWidth;

    /**
     * Required. Height of the output surface
     */
    @android.filterfw.core.GenerateFieldPort(name = "oheight")
    private int mScreenHeight;

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

    private android.filterfw.core.ShaderProgram mProgram;

    private android.filterfw.core.GLEnvironment mGlEnv;

    private android.filterfw.core.GLFrame mScreen;

    private int mRenderMode = RENDERMODE_FIT;

    private float mAspectRatio = 1.0F;

    private int mSurfaceId = -1;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "SurfaceRenderFilter";

    public SurfaceTargetFilter(java.lang.String name) {
        super(name);
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.ui.SurfaceTargetFilter.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public void setupPorts() {
        // Make sure we have a Surface
        if (mSurface == null) {
            throw new java.lang.RuntimeException("NULL Surface passed to SurfaceTargetFilter");
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
        mGlEnv = context.getGLEnvironment();
        // Create identity shader to render, and make sure to render upside-down, as textures
        // are stored internally bottom-to-top.
        mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        mProgram.setSourceRect(0, 1, 1, -1);
        mProgram.setClearsOutput(true);
        mProgram.setClearColor(0.0F, 0.0F, 0.0F);
        android.filterfw.core.MutableFrameFormat screenFormat = android.filterfw.format.ImageFormat.create(mScreenWidth, mScreenHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
        mScreen = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(screenFormat, android.filterfw.core.GLFrame.EXISTING_FBO_BINDING, 0)));
        // Set up cropping
        updateRenderMode();
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        registerSurface();
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.ui.SurfaceTargetFilter.TAG, "Starting frame processing");

        // Get input frame
        android.filterfw.core.Frame input = pullInput("frame");
        boolean createdFrame = false;
        float currentAspectRatio = ((float) (input.getFormat().getWidth())) / input.getFormat().getHeight();
        if (currentAspectRatio != mAspectRatio) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.ui.SurfaceTargetFilter.TAG, (("New aspect ratio: " + currentAspectRatio) + ", previously: ") + mAspectRatio);

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
        mGlEnv.activateSurfaceWithId(mSurfaceId);
        // Process
        mProgram.process(gpuFrame, mScreen);
        // And swap buffers
        mGlEnv.swapBuffers();
        if (createdFrame) {
            gpuFrame.release();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        mScreen.setViewport(0, 0, mScreenWidth, mScreenHeight);
        updateTargetRect();
    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        unregisterSurface();
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mScreen != null) {
            mScreen.release();
        }
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

    private void registerSurface() {
        mSurfaceId = mGlEnv.registerSurface(mSurface);
        if (mSurfaceId < 0) {
            throw new java.lang.RuntimeException("Could not register Surface: " + mSurface);
        }
    }

    private void unregisterSurface() {
        if (mSurfaceId > 0) {
            mGlEnv.unregisterSurfaceId(mSurfaceId);
        }
    }
}

