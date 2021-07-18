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
 * <p>A filter that converts textures from a SurfaceTexture object into frames for
 * processing in the filter framework.</p>
 *
 * <p>To use, connect up the sourceListener callback, and then when executing
 * the graph, use the SurfaceTexture object passed to the callback to feed
 * frames into the filter graph. For example, pass the SurfaceTexture into
 * {#link
 * android.hardware.Camera.setPreviewTexture(android.graphics.SurfaceTexture)}.
 * This filter is intended for applications that need for flexibility than the
 * CameraSource and MediaSource provide. Note that the application needs to
 * provide width and height information for the SurfaceTextureSource, which it
 * should obtain from wherever the SurfaceTexture data is coming from to avoid
 * unnecessary resampling.</p>
 *
 * @unknown 
 */
public class SurfaceTextureSource extends android.filterfw.core.Filter {
    /**
     * User-visible parameters
     */
    /**
     * The callback interface for the sourceListener parameter
     */
    public interface SurfaceTextureSourceListener {
        public void onSurfaceTextureSourceReady(android.graphics.SurfaceTexture source);
    }

    /**
     * A callback to send the internal SurfaceTexture object to, once it is
     * created. This callback will be called when the the filter graph is
     * preparing to execute, but before any processing has actually taken
     * place. The SurfaceTexture object passed to this callback is the only way
     * to feed this filter. When the filter graph is shutting down, this
     * callback will be called again with null as the source.
     *
     * This callback may be called from an arbitrary thread, so it should not
     * assume it is running in the UI thread in particular.
     */
    @android.filterfw.core.GenerateFinalPort(name = "sourceListener")
    private android.filterpacks.videosrc.SurfaceTextureSource.SurfaceTextureSourceListener mSourceListener;

    /**
     * The width of the output image frame. If the texture width for the
     * SurfaceTexture source is known, use it here to minimize resampling.
     */
    @android.filterfw.core.GenerateFieldPort(name = "width")
    private int mWidth;

    /**
     * The height of the output image frame. If the texture height for the
     * SurfaceTexture source is known, use it here to minimize resampling.
     */
    @android.filterfw.core.GenerateFieldPort(name = "height")
    private int mHeight;

    /**
     * Whether the filter will always wait for a new frame from its
     * SurfaceTexture, or whether it will output an old frame again if a new
     * frame isn't available. The filter will always wait for the first frame,
     * to avoid outputting a blank frame. Defaults to true.
     */
    @android.filterfw.core.GenerateFieldPort(name = "waitForNewFrame", hasDefault = true)
    private boolean mWaitForNewFrame = true;

    /**
     * Maximum timeout before signaling error when waiting for a new frame. Set
     * this to zero to disable the timeout and wait indefinitely. In milliseconds.
     */
    @android.filterfw.core.GenerateFieldPort(name = "waitTimeout", hasDefault = true)
    private int mWaitTimeout = 1000;

    /**
     * Whether a timeout is an exception-causing failure, or just causes the
     * filter to close.
     */
    @android.filterfw.core.GenerateFieldPort(name = "closeOnTimeout", hasDefault = true)
    private boolean mCloseOnTimeout = false;

    // Variables for input->output conversion
    private android.filterfw.core.GLFrame mMediaFrame;

    private android.filterfw.core.ShaderProgram mFrameExtractor;

    private android.graphics.SurfaceTexture mSurfaceTexture;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private android.os.ConditionVariable mNewFrameAvailable;

    private boolean mFirstFrame;

    private float[] mFrameTransform;

    private float[] mMappedCoords;

    // These default source coordinates perform the necessary flip
    // for converting from MFF/Bitmap origin to OpenGL origin.
    private static final float[] mSourceCoords = new float[]{ 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1 };

    // Shader for output
    private final java.lang.String mRenderShader = "#extension GL_OES_EGL_image_external : require\n" + ((((("precision mediump float;\n" + "uniform samplerExternalOES tex_sampler_0;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n") + "}\n");

    // Variables for logging
    private static final java.lang.String TAG = "SurfaceTextureSource";

    private static final boolean mLogVerbose = android.util.Log.isLoggable(android.filterpacks.videosrc.SurfaceTextureSource.TAG, android.util.Log.VERBOSE);

    public SurfaceTextureSource(java.lang.String name) {
        super(name);
        mNewFrameAvailable = new android.os.ConditionVariable();
        mFrameTransform = new float[16];
        mMappedCoords = new float[16];
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
    protected void prepare(android.filterfw.core.FilterContext context) {
        if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "Preparing SurfaceTextureSource");

        createFormats();
        // Prepare input
        mMediaFrame = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(mOutputFormat, android.filterfw.core.GLFrame.EXTERNAL_TEXTURE, 0)));
        // Prepare output
        mFrameExtractor = new android.filterfw.core.ShaderProgram(context, mRenderShader);
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "Opening SurfaceTextureSource");

        // Create SurfaceTexture anew each time - it can use substantial memory.
        mSurfaceTexture = new android.graphics.SurfaceTexture(mMediaFrame.getTextureId());
        // Connect SurfaceTexture to callback
        mSurfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
        // Connect SurfaceTexture to source
        mSourceListener.onSurfaceTextureSourceReady(mSurfaceTexture);
        mFirstFrame = true;
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "Processing new frame");

        // First, get new frame if available
        if (mWaitForNewFrame || mFirstFrame) {
            boolean gotNewFrame;
            if (mWaitTimeout != 0) {
                gotNewFrame = mNewFrameAvailable.block(mWaitTimeout);
                if (!gotNewFrame) {
                    if (!mCloseOnTimeout) {
                        throw new java.lang.RuntimeException("Timeout waiting for new frame");
                    } else {
                        if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
                            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "Timeout waiting for a new frame. Closing.");

                        closeOutputPort("video");
                        return;
                    }
                }
            } else {
                mNewFrameAvailable.block();
            }
            mNewFrameAvailable.close();
            mFirstFrame = false;
        }
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mFrameTransform);
        android.opengl.Matrix.multiplyMM(mMappedCoords, 0, mFrameTransform, 0, android.filterpacks.videosrc.SurfaceTextureSource.mSourceCoords, 0);
        mFrameExtractor.setSourceRegion(mMappedCoords[0], mMappedCoords[1], mMappedCoords[4], mMappedCoords[5], mMappedCoords[8], mMappedCoords[9], mMappedCoords[12], mMappedCoords[13]);
        // Next, render to output
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(mOutputFormat);
        mFrameExtractor.process(mMediaFrame, output);
        output.setTimestamp(mSurfaceTexture.getTimestamp());
        pushOutput("video", output);
        output.release();
    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "SurfaceTextureSource closed");

        mSourceListener.onSurfaceTextureSourceReady(null);
        mSurfaceTexture.release();
        mSurfaceTexture = null;
    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mMediaFrame != null) {
            mMediaFrame.release();
        }
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (name.equals("width") || name.equals("height")) {
            mOutputFormat.setDimensions(mWidth, mHeight);
        }
    }

    private android.graphics.SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener = new android.graphics.SurfaceTexture.OnFrameAvailableListener() {
        public void onFrameAvailable(android.graphics.SurfaceTexture surfaceTexture) {
            if (android.filterpacks.videosrc.SurfaceTextureSource.mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.SurfaceTextureSource.TAG, "New frame from SurfaceTexture");

            mNewFrameAvailable.open();
        }
    };
}

