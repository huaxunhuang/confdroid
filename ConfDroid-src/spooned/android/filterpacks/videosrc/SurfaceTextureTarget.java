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
package android.filterpacks.videosrc;


/**
 *
 *
 * @unknown 
 */
public class SurfaceTextureTarget extends android.filterfw.core.Filter {
    private final int RENDERMODE_STRETCH = 0;

    private final int RENDERMODE_FIT = 1;

    private final int RENDERMODE_FILL_CROP = 2;

    private final int RENDERMODE_CUSTOMIZE = 3;

    /**
     * Required. Sets the destination surfaceTexture.
     */
    @android.filterfw.core.GenerateFinalPort(name = "surfaceTexture")
    private android.graphics.SurfaceTexture mSurfaceTexture;

    /**
     * Required. Sets the width of the output surfaceTexture images
     */
    @android.filterfw.core.GenerateFinalPort(name = "width")
    private int mScreenWidth;

    /**
     * Required. Sets the height of the output surfaceTexture images
     */
    @android.filterfw.core.GenerateFinalPort(name = "height")
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

    @android.filterfw.core.GenerateFieldPort(name = "sourceQuad", hasDefault = true)
    private android.filterfw.geometry.Quad mSourceQuad = new android.filterfw.geometry.Quad(new android.filterfw.geometry.Point(0.0F, 1.0F), new android.filterfw.geometry.Point(1.0F, 1.0F), new android.filterfw.geometry.Point(0.0F, 0.0F), new android.filterfw.geometry.Point(1.0F, 0.0F));

    @android.filterfw.core.GenerateFieldPort(name = "targetQuad", hasDefault = true)
    private android.filterfw.geometry.Quad mTargetQuad = new android.filterfw.geometry.Quad(new android.filterfw.geometry.Point(0.0F, 0.0F), new android.filterfw.geometry.Point(1.0F, 0.0F), new android.filterfw.geometry.Point(0.0F, 1.0F), new android.filterfw.geometry.Point(1.0F, 1.0F));

    private int mSurfaceId;

    private android.filterfw.core.ShaderProgram mProgram;

    private android.filterfw.core.GLFrame mScreen;

    private int mRenderMode = RENDERMODE_FIT;

    private float mAspectRatio = 1.0F;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "SurfaceTextureTarget";

    public SurfaceTextureTarget(java.lang.String name) {
        super(name);
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public synchronized void setupPorts() {
        // Make sure we have a SurfaceView
        if (mSurfaceTexture == null) {
            throw new java.lang.RuntimeException("Null SurfaceTexture passed to SurfaceTextureTarget");
        }
        // Add input port - will accept anything that's 4-channel.
        addMaskedInputPort("frame", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA));
    }

    public void updateRenderMode() {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "updateRenderMode. Thread: " + java.lang.Thread.currentThread());

        if (mRenderModeString != null) {
            if (mRenderModeString.equals("stretch")) {
                mRenderMode = RENDERMODE_STRETCH;
            } else
                if (mRenderModeString.equals("fit")) {
                    mRenderMode = RENDERMODE_FIT;
                } else
                    if (mRenderModeString.equals("fill_crop")) {
                        mRenderMode = RENDERMODE_FILL_CROP;
                    } else
                        if (mRenderModeString.equals("customize")) {
                            mRenderMode = RENDERMODE_CUSTOMIZE;
                        } else {
                            throw new java.lang.RuntimeException(("Unknown render mode '" + mRenderModeString) + "'!");
                        }



        }
        updateTargetRect();
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "Prepare. Thread: " + java.lang.Thread.currentThread());

        // Create identity shader to render, and make sure to render upside-down, as textures
        // are stored internally bottom-to-top.
        mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        mProgram.setSourceRect(0, 1, 1, -1);
        mProgram.setClearColor(0.0F, 0.0F, 0.0F);
        updateRenderMode();
        // Create a frame representing the screen
        android.filterfw.core.MutableFrameFormat screenFormat = new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_BYTE, android.filterfw.core.FrameFormat.TARGET_GPU);
        screenFormat.setBytesPerSample(4);
        screenFormat.setDimensions(mScreenWidth, mScreenHeight);
        mScreen = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(screenFormat, android.filterfw.core.GLFrame.EXISTING_FBO_BINDING, 0)));
    }

    @java.lang.Override
    public synchronized void open(android.filterfw.core.FilterContext context) {
        // Set up SurfaceTexture internals
        if (mSurfaceTexture == null) {
            android.util.Log.e(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "SurfaceTexture is null!!");
            throw new java.lang.RuntimeException("Could not register SurfaceTexture: " + mSurfaceTexture);
        }
        mSurfaceId = context.getGLEnvironment().registerSurfaceTexture(mSurfaceTexture, mScreenWidth, mScreenHeight);
        if (mSurfaceId <= 0) {
            throw new java.lang.RuntimeException("Could not register SurfaceTexture: " + mSurfaceTexture);
        }
    }

    // Once the surface is unregistered, we still need the surfacetexture reference.
    // That is because when the the filter graph stops and starts again, the app
    // may not set the mSurfaceTexture again on the filter. In some cases, the app
    // may not even know that the graph has re-started. So it is difficult to enforce
    // that condition on an app using this filter. The only case where we need
    // to let go of the mSurfaceTexure reference is when the app wants to shut
    // down the graph on purpose, such as in the disconnect call.
    @java.lang.Override
    public synchronized void close(android.filterfw.core.FilterContext context) {
        if (mSurfaceId > 0) {
            context.getGLEnvironment().unregisterSurfaceId(mSurfaceId);
            mSurfaceId = -1;
        }
    }

    // This should be called from the client side when the surfacetexture is no longer
    // valid. e.g. from onPause() in the application using the filter graph.
    // In this case, we need to let go of our surfacetexture reference.
    public synchronized void disconnect(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "disconnect");

        if (mSurfaceTexture == null) {
            android.util.Log.d(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "SurfaceTexture is already null. Nothing to disconnect.");
            return;
        }
        mSurfaceTexture = null;
        // Make sure we unregister the surface as well if a surface was registered.
        // There can be a situation where the surface was not registered but the
        // surfacetexture was valid. For example, the disconnect can be called before
        // the filter was opened. Hence, the surfaceId may not be a valid one here,
        // and need to check for its validity.
        if (mSurfaceId > 0) {
            context.getGLEnvironment().unregisterSurfaceId(mSurfaceId);
            mSurfaceId = -1;
        }
    }

    @java.lang.Override
    public synchronized void process(android.filterfw.core.FilterContext context) {
        // Surface is not registered. Nothing to render into.
        if (mSurfaceId <= 0) {
            return;
        }
        android.filterfw.core.GLEnvironment glEnv = context.getGLEnvironment();
        // Get input frame
        android.filterfw.core.Frame input = pullInput("frame");
        boolean createdFrame = false;
        float currentAspectRatio = ((float) (input.getFormat().getWidth())) / input.getFormat().getHeight();
        if (currentAspectRatio != mAspectRatio) {
            if (mLogVerbose) {
                android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, (((("Process. New aspect ratio: " + currentAspectRatio) + ", previously: ") + mAspectRatio) + ". Thread: ") + java.lang.Thread.currentThread());
            }
            mAspectRatio = currentAspectRatio;
            updateTargetRect();
        }
        // See if we need to copy to GPU
        android.filterfw.core.Frame gpuFrame = null;
        int target = input.getFormat().getTarget();
        if (target != android.filterfw.core.FrameFormat.TARGET_GPU) {
            gpuFrame = context.getFrameManager().duplicateFrameToTarget(input, android.filterfw.core.FrameFormat.TARGET_GPU);
            createdFrame = true;
        } else {
            gpuFrame = input;
        }
        // Activate our surface
        glEnv.activateSurfaceWithId(mSurfaceId);
        // Process
        mProgram.process(gpuFrame, mScreen);
        glEnv.setSurfaceTimestamp(input.getTimestamp());
        // And swap buffers
        glEnv.swapBuffers();
        if (createdFrame) {
            gpuFrame.release();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "FPVU. Thread: " + java.lang.Thread.currentThread());

        updateRenderMode();
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mScreen != null) {
            mScreen.release();
        }
    }

    private void updateTargetRect() {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "updateTargetRect. Thread: " + java.lang.Thread.currentThread());

        if (((mScreenWidth > 0) && (mScreenHeight > 0)) && (mProgram != null)) {
            float screenAspectRatio = ((float) (mScreenWidth)) / mScreenHeight;
            float relativeAspectRatio = screenAspectRatio / mAspectRatio;
            if (mLogVerbose) {
                android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, (((((((("UTR. screen w = " + ((float) (mScreenWidth))) + " x screen h = ") + ((float) (mScreenHeight))) + " Screen AR: ") + screenAspectRatio) + ", frame AR: ") + mAspectRatio) + ", relative AR: ") + relativeAspectRatio);
            }
            if ((relativeAspectRatio == 1.0F) && (mRenderMode != RENDERMODE_CUSTOMIZE)) {
                mProgram.setTargetRect(0, 0, 1, 1);
                mProgram.setClearsOutput(false);
            } else {
                switch (mRenderMode) {
                    case RENDERMODE_STRETCH :
                        mTargetQuad.p0.set(0.0F, 0.0F);
                        mTargetQuad.p1.set(1.0F, 0.0F);
                        mTargetQuad.p2.set(0.0F, 1.0F);
                        mTargetQuad.p3.set(1.0F, 1.0F);
                        mProgram.setClearsOutput(false);
                        break;
                    case RENDERMODE_FIT :
                        if (relativeAspectRatio > 1.0F) {
                            // Screen is wider than the camera, scale down X
                            mTargetQuad.p0.set(0.5F - (0.5F / relativeAspectRatio), 0.0F);
                            mTargetQuad.p1.set(0.5F + (0.5F / relativeAspectRatio), 0.0F);
                            mTargetQuad.p2.set(0.5F - (0.5F / relativeAspectRatio), 1.0F);
                            mTargetQuad.p3.set(0.5F + (0.5F / relativeAspectRatio), 1.0F);
                        } else {
                            // Screen is taller than the camera, scale down Y
                            mTargetQuad.p0.set(0.0F, 0.5F - (0.5F * relativeAspectRatio));
                            mTargetQuad.p1.set(1.0F, 0.5F - (0.5F * relativeAspectRatio));
                            mTargetQuad.p2.set(0.0F, 0.5F + (0.5F * relativeAspectRatio));
                            mTargetQuad.p3.set(1.0F, 0.5F + (0.5F * relativeAspectRatio));
                        }
                        mProgram.setClearsOutput(true);
                        break;
                    case RENDERMODE_FILL_CROP :
                        if (relativeAspectRatio > 1) {
                            // Screen is wider than the camera, crop in Y
                            mTargetQuad.p0.set(0.0F, 0.5F - (0.5F * relativeAspectRatio));
                            mTargetQuad.p1.set(1.0F, 0.5F - (0.5F * relativeAspectRatio));
                            mTargetQuad.p2.set(0.0F, 0.5F + (0.5F * relativeAspectRatio));
                            mTargetQuad.p3.set(1.0F, 0.5F + (0.5F * relativeAspectRatio));
                        } else {
                            // Screen is taller than the camera, crop in X
                            mTargetQuad.p0.set(0.5F - (0.5F / relativeAspectRatio), 0.0F);
                            mTargetQuad.p1.set(0.5F + (0.5F / relativeAspectRatio), 0.0F);
                            mTargetQuad.p2.set(0.5F - (0.5F / relativeAspectRatio), 1.0F);
                            mTargetQuad.p3.set(0.5F + (0.5F / relativeAspectRatio), 1.0F);
                        }
                        mProgram.setClearsOutput(true);
                        break;
                    case RENDERMODE_CUSTOMIZE :
                        ((android.filterfw.core.ShaderProgram) (mProgram)).setSourceRegion(mSourceQuad);
                        break;
                }
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureTarget.TAG, "UTR. quad: " + mTargetQuad);

                ((android.filterfw.core.ShaderProgram) (mProgram)).setTargetRegion(mTargetQuad);
            }
        }
    }
}

