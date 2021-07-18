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
public class MediaSource extends android.filterfw.core.Filter {
    /**
     * User-visible parameters
     */
    /**
     * The source URL for the media source. Can be an http: link to a remote
     * resource, or a file: link to a local media file
     */
    @android.filterfw.core.GenerateFieldPort(name = "sourceUrl", hasDefault = true)
    private java.lang.String mSourceUrl = "";

    /**
     * An open asset file descriptor to a local media source. Default is null
     */
    @android.filterfw.core.GenerateFieldPort(name = "sourceAsset", hasDefault = true)
    private android.content.res.AssetFileDescriptor mSourceAsset = null;

    /**
     * The context for the MediaPlayer to resolve the sourceUrl.
     * Make sure this is set before the sourceUrl to avoid unexpected result.
     * If the sourceUrl is not a content URI, it is OK to keep this as null.
     */
    @android.filterfw.core.GenerateFieldPort(name = "context", hasDefault = true)
    private android.content.Context mContext = null;

    /**
     * Whether the media source is a URL or an asset file descriptor. Defaults
     * to false.
     */
    @android.filterfw.core.GenerateFieldPort(name = "sourceIsUrl", hasDefault = true)
    private boolean mSelectedIsUrl = false;

    /**
     * Whether the filter will always wait for a new video frame, or whether it
     * will output an old frame again if a new frame isn't available. Defaults
     * to true.
     */
    @android.filterfw.core.GenerateFinalPort(name = "waitForNewFrame", hasDefault = true)
    private boolean mWaitForNewFrame = true;

    /**
     * Whether the media source should loop automatically or not. Defaults to
     * true.
     */
    @android.filterfw.core.GenerateFieldPort(name = "loop", hasDefault = true)
    private boolean mLooping = true;

    /**
     * Volume control. Currently sound is piped directly to the speakers, so
     * this defaults to mute.
     */
    @android.filterfw.core.GenerateFieldPort(name = "volume", hasDefault = true)
    private float mVolume = 0.0F;

    /**
     * Orientation. This controls the output orientation of the video. Valid
     * values are 0, 90, 180, 270
     */
    @android.filterfw.core.GenerateFieldPort(name = "orientation", hasDefault = true)
    private int mOrientation = 0;

    private android.media.MediaPlayer mMediaPlayer;

    private android.filterfw.core.GLFrame mMediaFrame;

    private android.graphics.SurfaceTexture mSurfaceTexture;

    private android.filterfw.core.ShaderProgram mFrameExtractor;

    private android.filterfw.core.MutableFrameFormat mOutputFormat;

    private int mWidth;

    private int mHeight;

    // Total timeouts will be PREP_TIMEOUT*PREP_TIMEOUT_REPEAT
    private static final int PREP_TIMEOUT = 100;// ms


    private static final int PREP_TIMEOUT_REPEAT = 100;

    private static final int NEWFRAME_TIMEOUT = 100;// ms


    private static final int NEWFRAME_TIMEOUT_REPEAT = 10;

    // This is an identity shader; not using the default identity
    // shader because reading from a SurfaceTexture requires the
    // GL_OES_EGL_image_external extension.
    private final java.lang.String mFrameShader = "#extension GL_OES_EGL_image_external : require\n" + ((((("precision mediump float;\n" + "uniform samplerExternalOES tex_sampler_0;\n") + "varying vec2 v_texcoord;\n") + "void main() {\n") + "  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n") + "}\n");

    // The following transforms enable rotation of the decoded source.
    // These are multiplied with the transform obtained from the
    // SurfaceTexture to get the final transform to be set on the media source.
    // Currently, given a device orientation, the MediaSource rotates in such a way
    // that the source is displayed upright. A particular use case
    // is "Background Replacement" feature in the Camera app
    // where the MediaSource rotates the source to align with the camera feed and pass it
    // on to the backdropper filter. The backdropper only does the blending
    // and does not have to do any rotation
    // (except for mirroring in case of front camera).
    // TODO: Currently the rotations are spread over a bunch of stages in the
    // pipeline. A cleaner design
    // could be to cast away all the rotation in a separate filter or attach a transform
    // to the frame so that MediaSource itself need not know about any rotation.
    private static final float[] mSourceCoords_0 = new float[]{ 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1 };

    private static final float[] mSourceCoords_270 = new float[]{ 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1 };

    private static final float[] mSourceCoords_180 = new float[]{ 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1 };

    private static final float[] mSourceCoords_90 = new float[]{ 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1 };

    private boolean mGotSize;

    private boolean mPrepared;

    private boolean mPlaying;

    private boolean mNewFrameAvailable;

    private boolean mOrientationUpdated;

    private boolean mPaused;

    private boolean mCompleted;

    private final boolean mLogVerbose;

    private static final java.lang.String TAG = "MediaSource";

    public MediaSource(java.lang.String name) {
        super(name);
        mNewFrameAvailable = false;
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.videosrc.MediaSource.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public void setupPorts() {
        // Add input port
        addOutputPort("video", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
    }

    private void createFormats() {
        mOutputFormat = android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU);
    }

    @java.lang.Override
    protected void prepare(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Preparing MediaSource");

        mFrameExtractor = new android.filterfw.core.ShaderProgram(context, mFrameShader);
        // SurfaceTexture defines (0,0) to be bottom-left. The filter framework
        // defines (0,0) as top-left, so do the flip here.
        mFrameExtractor.setSourceRect(0, 1, 1, -1);
        createFormats();
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        if (mLogVerbose) {
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Opening MediaSource");
            if (mSelectedIsUrl) {
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Current URL is " + mSourceUrl);
            } else {
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Current source is Asset!");
            }
        }
        mMediaFrame = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(mOutputFormat, android.filterfw.core.GLFrame.EXTERNAL_TEXTURE, 0)));
        mSurfaceTexture = new android.graphics.SurfaceTexture(mMediaFrame.getTextureId());
        if (!setupMediaPlayer(mSelectedIsUrl)) {
            throw new java.lang.RuntimeException("Error setting up MediaPlayer!");
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        // Note: process is synchronized by its caller in the Filter base class
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Processing new frame");

        if (mMediaPlayer == null) {
            // Something went wrong in initialization or parameter updates
            throw new java.lang.NullPointerException("Unexpected null media player!");
        }
        if (mCompleted) {
            // Video playback is done, so close us down
            closeOutputPort("video");
            return;
        }
        if (!mPlaying) {
            int waitCount = 0;
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Waiting for preparation to complete");

            while ((!mGotSize) || (!mPrepared)) {
                try {
                    this.wait(android.filterpacks.videosrc.MediaSource.PREP_TIMEOUT);
                } catch (java.lang.InterruptedException e) {
                    // ignoring
                }
                if (mCompleted) {
                    // Video playback is done, so close us down
                    closeOutputPort("video");
                    return;
                }
                waitCount++;
                if (waitCount == android.filterpacks.videosrc.MediaSource.PREP_TIMEOUT_REPEAT) {
                    mMediaPlayer.release();
                    throw new java.lang.RuntimeException("MediaPlayer timed out while preparing!");
                }
            } 
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Starting playback");

            mMediaPlayer.start();
        }
        // Use last frame if paused, unless just starting playback, in which case
        // we want at least one valid frame before pausing
        if ((!mPaused) || (!mPlaying)) {
            if (mWaitForNewFrame) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Waiting for new frame");

                int waitCount = 0;
                while (!mNewFrameAvailable) {
                    if (waitCount == android.filterpacks.videosrc.MediaSource.NEWFRAME_TIMEOUT_REPEAT) {
                        if (mCompleted) {
                            // Video playback is done, so close us down
                            closeOutputPort("video");
                            return;
                        } else {
                            throw new java.lang.RuntimeException("Timeout waiting for new frame!");
                        }
                    }
                    try {
                        this.wait(android.filterpacks.videosrc.MediaSource.NEWFRAME_TIMEOUT);
                    } catch (java.lang.InterruptedException e) {
                        if (mLogVerbose)
                            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "interrupted");

                        // ignoring
                    }
                    waitCount++;
                } 
                mNewFrameAvailable = false;
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Got new frame");

            }
            mSurfaceTexture.updateTexImage();
            mOrientationUpdated = true;
        }
        if (mOrientationUpdated) {
            float[] surfaceTransform = new float[16];
            mSurfaceTexture.getTransformMatrix(surfaceTransform);
            float[] sourceCoords = new float[16];
            switch (mOrientation) {
                default :
                case 0 :
                    android.opengl.Matrix.multiplyMM(sourceCoords, 0, surfaceTransform, 0, android.filterpacks.videosrc.MediaSource.mSourceCoords_0, 0);
                    break;
                case 90 :
                    android.opengl.Matrix.multiplyMM(sourceCoords, 0, surfaceTransform, 0, android.filterpacks.videosrc.MediaSource.mSourceCoords_90, 0);
                    break;
                case 180 :
                    android.opengl.Matrix.multiplyMM(sourceCoords, 0, surfaceTransform, 0, android.filterpacks.videosrc.MediaSource.mSourceCoords_180, 0);
                    break;
                case 270 :
                    android.opengl.Matrix.multiplyMM(sourceCoords, 0, surfaceTransform, 0, android.filterpacks.videosrc.MediaSource.mSourceCoords_270, 0);
                    break;
            }
            if (mLogVerbose) {
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "OrientationHint = " + mOrientation);
                java.lang.String temp = java.lang.String.format("SetSourceRegion: %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f", sourceCoords[4], sourceCoords[5], sourceCoords[0], sourceCoords[1], sourceCoords[12], sourceCoords[13], sourceCoords[8], sourceCoords[9]);
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, temp);
            }
            mFrameExtractor.setSourceRegion(sourceCoords[4], sourceCoords[5], sourceCoords[0], sourceCoords[1], sourceCoords[12], sourceCoords[13], sourceCoords[8], sourceCoords[9]);
            mOrientationUpdated = false;
        }
        android.filterfw.core.Frame output = context.getFrameManager().newFrame(mOutputFormat);
        mFrameExtractor.process(mMediaFrame, output);
        long timestamp = mSurfaceTexture.getTimestamp();
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, ("Timestamp: " + (timestamp / 1.0E9)) + " s");

        output.setTimestamp(timestamp);
        pushOutput("video", output);
        output.release();
        mPlaying = true;
    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mPrepared = false;
        mGotSize = false;
        mPlaying = false;
        mPaused = false;
        mCompleted = false;
        mNewFrameAvailable = false;
        mMediaPlayer.release();
        mMediaPlayer = null;
        mSurfaceTexture.release();
        mSurfaceTexture = null;
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "MediaSource closed");

    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        if (mMediaFrame != null) {
            mMediaFrame.release();
        }
    }

    // When updating the port values of the filter, users can update sourceIsUrl to switch
    // between using URL objects or Assets.
    // If updating only sourceUrl/sourceAsset, MediaPlayer gets reset if the current player
    // uses Url objects/Asset.
    // Otherwise the new sourceUrl/sourceAsset is stored and will be used when users switch
    // sourceIsUrl next time.
    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Parameter update");

        if (name.equals("sourceUrl")) {
            if (isOpen()) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Opening new source URL");

                if (mSelectedIsUrl) {
                    setupMediaPlayer(mSelectedIsUrl);
                }
            }
        } else
            if (name.equals("sourceAsset")) {
                if (isOpen()) {
                    if (mLogVerbose)
                        android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Opening new source FD");

                    if (!mSelectedIsUrl) {
                        setupMediaPlayer(mSelectedIsUrl);
                    }
                }
            } else
                if (name.equals("loop")) {
                    if (isOpen()) {
                        mMediaPlayer.setLooping(mLooping);
                    }
                } else
                    if (name.equals("sourceIsUrl")) {
                        if (isOpen()) {
                            if (mSelectedIsUrl) {
                                if (mLogVerbose)
                                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Opening new source URL");

                            } else {
                                if (mLogVerbose)
                                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Opening new source Asset");

                            }
                            setupMediaPlayer(mSelectedIsUrl);
                        }
                    } else
                        if (name.equals("volume")) {
                            if (isOpen()) {
                                mMediaPlayer.setVolume(mVolume, mVolume);
                            }
                        } else
                            if (name.equals("orientation") && mGotSize) {
                                if ((mOrientation == 0) || (mOrientation == 180)) {
                                    mOutputFormat.setDimensions(mWidth, mHeight);
                                } else {
                                    mOutputFormat.setDimensions(mHeight, mWidth);
                                }
                                mOrientationUpdated = true;
                            }





    }

    public synchronized void pauseVideo(boolean pauseState) {
        if (isOpen()) {
            if (pauseState && (!mPaused)) {
                mMediaPlayer.pause();
            } else
                if ((!pauseState) && mPaused) {
                    mMediaPlayer.start();
                }

        }
        mPaused = pauseState;
    }

    /**
     * Creates a media player, sets it up, and calls prepare
     */
    private synchronized boolean setupMediaPlayer(boolean useUrl) {
        mPrepared = false;
        mGotSize = false;
        mPlaying = false;
        mPaused = false;
        mCompleted = false;
        mNewFrameAvailable = false;
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Setting up playback.");

        if (mMediaPlayer != null) {
            // Clean up existing media players
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Resetting existing MediaPlayer.");

            mMediaPlayer.reset();
        } else {
            // Create new media player
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Creating new MediaPlayer.");

            mMediaPlayer = new android.media.MediaPlayer();
        }
        if (mMediaPlayer == null) {
            throw new java.lang.RuntimeException("Unable to create a MediaPlayer!");
        }
        // Set up data sources, etc
        try {
            if (useUrl) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Setting MediaPlayer source to URI " + mSourceUrl);

                if (mContext == null) {
                    mMediaPlayer.setDataSource(mSourceUrl);
                } else {
                    mMediaPlayer.setDataSource(mContext, android.net.Uri.parse(mSourceUrl.toString()));
                }
            } else {
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Setting MediaPlayer source to asset " + mSourceAsset);

                mMediaPlayer.setDataSource(mSourceAsset.getFileDescriptor(), mSourceAsset.getStartOffset(), mSourceAsset.getLength());
            }
        } catch (java.io.IOException e) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            if (useUrl) {
                throw new java.lang.RuntimeException(java.lang.String.format("Unable to set MediaPlayer to URL %s!", mSourceUrl), e);
            } else {
                throw new java.lang.RuntimeException(java.lang.String.format("Unable to set MediaPlayer to asset %s!", mSourceAsset), e);
            }
        } catch (java.lang.IllegalArgumentException e) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            if (useUrl) {
                throw new java.lang.RuntimeException(java.lang.String.format("Unable to set MediaPlayer to URL %s!", mSourceUrl), e);
            } else {
                throw new java.lang.RuntimeException(java.lang.String.format("Unable to set MediaPlayer to asset %s!", mSourceAsset), e);
            }
        }
        mMediaPlayer.setLooping(mLooping);
        mMediaPlayer.setVolume(mVolume, mVolume);
        // Bind it to our media frame
        android.view.Surface surface = new android.view.Surface(mSurfaceTexture);
        mMediaPlayer.setSurface(surface);
        surface.release();
        // Connect Media Player to callbacks
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        // Connect SurfaceTexture to callback
        mSurfaceTexture.setOnFrameAvailableListener(onMediaFrameAvailableListener);
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "Preparing MediaPlayer.");

        mMediaPlayer.prepareAsync();
        return true;
    }

    private android.media.MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new android.media.MediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(android.media.MediaPlayer mp, int width, int height) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, (("MediaPlayer sent dimensions: " + width) + " x ") + height);

            if (!mGotSize) {
                if ((mOrientation == 0) || (mOrientation == 180)) {
                    mOutputFormat.setDimensions(width, height);
                } else {
                    mOutputFormat.setDimensions(height, width);
                }
                mWidth = width;
                mHeight = height;
            } else {
                if ((mOutputFormat.getWidth() != width) || (mOutputFormat.getHeight() != height)) {
                    android.util.Log.e(android.filterpacks.videosrc.MediaSource.TAG, "Multiple video size change events received!");
                }
            }
            synchronized(android.filterpacks.videosrc.MediaSource.this) {
                mGotSize = true;
                android.filterpacks.videosrc.MediaSource.this.notify();
            }
        }
    };

    private android.media.MediaPlayer.OnPreparedListener onPreparedListener = new android.media.MediaPlayer.OnPreparedListener() {
        public void onPrepared(android.media.MediaPlayer mp) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "MediaPlayer is prepared");

            synchronized(android.filterpacks.videosrc.MediaSource.this) {
                mPrepared = true;
                android.filterpacks.videosrc.MediaSource.this.notify();
            }
        }
    };

    private android.media.MediaPlayer.OnCompletionListener onCompletionListener = new android.media.MediaPlayer.OnCompletionListener() {
        public void onCompletion(android.media.MediaPlayer mp) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "MediaPlayer has completed playback");

            synchronized(android.filterpacks.videosrc.MediaSource.this) {
                mCompleted = true;
            }
        }
    };

    private android.graphics.SurfaceTexture.OnFrameAvailableListener onMediaFrameAvailableListener = new android.graphics.SurfaceTexture.OnFrameAvailableListener() {
        public void onFrameAvailable(android.graphics.SurfaceTexture surfaceTexture) {
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "New frame from media player");

            synchronized(android.filterpacks.videosrc.MediaSource.this) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "New frame: notify");

                mNewFrameAvailable = true;
                android.filterpacks.videosrc.MediaSource.this.notify();
                if (mLogVerbose)
                    android.util.Log.v(android.filterpacks.videosrc.MediaSource.TAG, "New frame: notify done");

            }
        }
    };
}

