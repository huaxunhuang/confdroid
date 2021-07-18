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
public class CameraSource extends android.filterfw.core.Filter {
    /**
     * User-visible parameters
     */
    /**
     * Camera ID to use for input. Defaults to 0.
     */
    @android.filterfw.core.GenerateFieldPort(name = "id", hasDefault = true)
    private int mCameraId = 0;

    /**
     * Frame width to request from camera. Actual size may not match requested.
     */
    @android.filterfw.core.GenerateFieldPort(name = "width", hasDefault = true)
    private int mWidth = 320;

    /**
     * Frame height to request from camera. Actual size may not match requested.
     */
    @android.filterfw.core.GenerateFieldPort(name = "height", hasDefault = true)
    private int mHeight = 240;

    /**
     * Stream framerate to request from camera. Actual frame rate may not match requested.
     */
    @android.filterfw.core.GenerateFieldPort(name = "framerate", hasDefault = true)
    private int mFps = 30;

    /**
     * Whether the filter should always wait for a new frame from the camera
     * before providing output.  If set to false, the filter will keep
     * outputting the last frame it received from the camera if multiple process
     * calls are received before the next update from the Camera. Defaults to true.
     */
    @android.filterfw.core.GenerateFinalPort(name = "waitForNewFrame", hasDefault = true)
    private boolean mWaitForNewFrame = true;

    private android.hardware.Camera mCamera;

    private android.filterfw.core.GLFrame mCameraFrame;

    private android.graphics.SurfaceTexture mSurfaceTexture;

    private android.filterfw.core.ShaderProgram mFrameExtractor;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private float[] mCameraTransform;

    private float[] mMappedCoords;

    // These default source coordinates perform the necessary flip
    // for converting from OpenGL origin to MFF/Bitmap origin.
    private static final float[] mSourceCoords = new float[]{ 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1 };

    private static final int NEWFRAME_TIMEOUT = 100;// ms


    private static final int NEWFRAME_TIMEOUT_REPEAT = 10;

    private boolean mNewFrameAvailable;

    private android.hardware.Camera.Parameters mCameraParameters;

    private static final java.lang.String mFrameShader = "#extension GL_OES_EGL_image_external : require\n" + ((((("precision mediump float;\n" + "uniform samplerExternalOES tex_sampler_0;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n") + "}\n");

    private final boolean mLogVerbose;

    private static final java.lang.String TAG = "CameraSource";

    public CameraSource(java.lang.String name) {
        super(name);
        mCameraTransform = new float[16];
        mMappedCoords = new float[16];
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.videosrc.CameraSource.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public void setupPorts() {
        // Add input port
        addOutputPort("video", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
    }

    private void createFormats() {
        mOutputFormat = android.filterfw.format.ImageFormat.create(mWidth, mHeight, android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Preparing");

        // Compile shader TODO: Move to onGLEnvSomething?
        mFrameExtractor = new android.filterfw.core.ShaderProgram(context, android.filterpacks.videosrc.CameraSource.mFrameShader);
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Opening");

        // Open camera
        mCamera = android.hardware.Camera.open(mCameraId);
        // Set parameters
        getCameraParameters();
        mCamera.setParameters(mCameraParameters);
        // Create frame formats
        createFormats();
        // Bind it to our camera frame
        mCameraFrame = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(mOutputFormat, android.filterfw.core.GLFrame.EXTERNAL_TEXTURE, 0)));
        mSurfaceTexture = new android.graphics.SurfaceTexture(mCameraFrame.getTextureId());
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(("Could not bind camera surface texture: " + e.getMessage()) + "!");
        }
        // Connect SurfaceTexture to callback
        mSurfaceTexture.setOnFrameAvailableListener(onCameraFrameAvailableListener);
        // Start the preview
        mNewFrameAvailable = false;
        mCamera.startPreview();
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Processing new frame");

        if (mWaitForNewFrame) {
            int waitCount = 0;
            while (!mNewFrameAvailable) {
                if (waitCount == android.filterpacks.videosrc.CameraSource.NEWFRAME_TIMEOUT_REPEAT) {
                    throw new java.lang.RuntimeException("Timeout waiting for new frame");
                }
                try {
                    this.wait(android.filterpacks.videosrc.CameraSource.NEWFRAME_TIMEOUT);
                } catch (java.lang.InterruptedException e) {
                    if (mLogVerbose)
                        android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Interrupted while waiting for new frame");

                }
            } 
            mNewFrameAvailable = false;
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Got new frame");

        }
        mSurfaceTexture.updateTexImage();
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Using frame extractor in thread: " + java.lang.Thread.currentThread());

        mSurfaceTexture.getTransformMatrix(mCameraTransform);
        android.opengl.Matrix.multiplyMM(mMappedCoords, 0, mCameraTransform, 0, android.filterpacks.videosrc.CameraSource.mSourceCoords, 0);
        mFrameExtractor.setSourceRegion(mMappedCoords[0], mMappedCoords[1], mMappedCoords[4], mMappedCoords[5], mMappedCoords[8], mMappedCoords[9], mMappedCoords[12], mMappedCoords[13]);
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(mOutputFormat);
        mFrameExtractor.process(mCameraFrame, output);
        long timestamp = mSurfaceTexture.getTimestamp();
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, ("Timestamp: " + (timestamp / 1.0E9)) + " s");

        output.setTimestamp(timestamp);
        pushOutput("video", output);
        // Release pushed frame
        output.release();
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Done processing new frame");

    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "Closing");

        mCamera.release();
        mCamera = null;
        mSurfaceTexture.release();
        mSurfaceTexture = null;
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mCameraFrame != null) {
            mCameraFrame.release();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (name.equals("framerate")) {
            getCameraParameters();
            int[] closestRange = findClosestFpsRange(mFps, mCameraParameters);
            mCameraParameters.setPreviewFpsRange(closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX], closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
            mCamera.setParameters(mCameraParameters);
        }
    }

    public synchronized android.hardware.Camera.Parameters getCameraParameters() {
        boolean closeCamera = false;
        if (mCameraParameters == null) {
            if (mCamera == null) {
                mCamera = android.hardware.Camera.open(mCameraId);
                closeCamera = true;
            }
            mCameraParameters = mCamera.getParameters();
            if (closeCamera) {
                mCamera.release();
                mCamera = null;
            }
        }
        int[] closestSize = findClosestSize(mWidth, mHeight, mCameraParameters);
        mWidth = closestSize[0];
        mHeight = closestSize[1];
        mCameraParameters.setPreviewSize(mWidth, mHeight);
        int[] closestRange = findClosestFpsRange(mFps, mCameraParameters);
        mCameraParameters.setPreviewFpsRange(closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX], closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
        return mCameraParameters;
    }

    /**
     * Update camera parameters. Image resolution cannot be changed.
     */
    public synchronized void setCameraParameters(android.hardware.Camera.Parameters params) {
        params.setPreviewSize(mWidth, mHeight);
        mCameraParameters = params;
        if (isOpen()) {
            mCamera.setParameters(mCameraParameters);
        }
    }

    private int[] findClosestSize(int width, int height, android.hardware.Camera.Parameters parameters) {
        java.util.List<android.hardware.Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        int closestWidth = -1;
        int closestHeight = -1;
        int smallestWidth = previewSizes.get(0).width;
        int smallestHeight = previewSizes.get(0).height;
        for (android.hardware.Camera.Size size : previewSizes) {
            // Best match defined as not being larger in either dimension than
            // the requested size, but as close as possible. The below isn't a
            // stable selection (reording the size list can give different
            // results), but since this is a fallback nicety, that's acceptable.
            if ((((size.width <= width) && (size.height <= height)) && (size.width >= closestWidth)) && (size.height >= closestHeight)) {
                closestWidth = size.width;
                closestHeight = size.height;
            }
            if ((size.width < smallestWidth) && (size.height < smallestHeight)) {
                smallestWidth = size.width;
                smallestHeight = size.height;
            }
        }
        if (closestWidth == (-1)) {
            // Requested size is smaller than any listed size; match with smallest possible
            closestWidth = smallestWidth;
            closestHeight = smallestHeight;
        }
        if (mLogVerbose) {
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, ((((((("Requested resolution: (" + width) + ", ") + height) + "). Closest match: (") + closestWidth) + ", ") + closestHeight) + ").");
        }
        int[] closestSize = new int[]{ closestWidth, closestHeight };
        return closestSize;
    }

    private int[] findClosestFpsRange(int fps, android.hardware.Camera.Parameters params) {
        java.util.List<int[]> supportedFpsRanges = params.getSupportedPreviewFpsRange();
        int[] closestRange = supportedFpsRanges.get(0);
        for (int[] range : supportedFpsRanges) {
            if ((((range[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] < (fps * 1000)) && (range[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] > (fps * 1000))) && (range[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] > closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX])) && (range[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] < closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX])) {
                closestRange = range;
            }
        }
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, ((((("Requested fps: " + fps) + ".Closest frame rate range: [") + (closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX] / 1000.0)) + ",") + (closestRange[android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX] / 1000.0)) + "]");

        return closestRange;
    }

    private android.graphics.SurfaceTexture.OnFrameAvailableListener onCameraFrameAvailableListener = new android.graphics.SurfaceTexture.OnFrameAvailableListener() {
        @java.lang.Override
        public void onFrameAvailable(android.graphics.SurfaceTexture surfaceTexture) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.CameraSource.TAG, "New frame from camera");

            synchronized(android.filterpacks.videosrc.CameraSource.this) {
                mNewFrameAvailable = true;
                android.filterpacks.videosrc.CameraSource.this.notify();
            }
        }
    };
}

