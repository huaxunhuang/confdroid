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
package android.filterpacks.videosink;


/**
 *
 *
 * @unknown 
 */
public class MediaEncoderFilter extends android.filterfw.core.Filter {
    /**
     * User-visible parameters
     */
    /**
     * Recording state. When set to false, recording will stop, or will not
     * start if not yet running the graph. Instead, frames are simply ignored.
     * When switched back to true, recording will restart. This allows a single
     * graph to both provide preview and to record video. If this is false,
     * recording settings can be updated while the graph is running.
     */
    @android.filterfw.core.GenerateFieldPort(name = "recording", hasDefault = true)
    private boolean mRecording = true;

    /**
     * Filename to save the output.
     */
    @android.filterfw.core.GenerateFieldPort(name = "outputFile", hasDefault = true)
    private java.lang.String mOutputFile = new java.lang.String("/sdcard/MediaEncoderOut.mp4");

    /**
     * File Descriptor to save the output.
     */
    @android.filterfw.core.GenerateFieldPort(name = "outputFileDescriptor", hasDefault = true)
    private java.io.FileDescriptor mFd = null;

    /**
     * Input audio source. If not set, no audio will be recorded.
     * Select from the values in MediaRecorder.AudioSource
     */
    @android.filterfw.core.GenerateFieldPort(name = "audioSource", hasDefault = true)
    private int mAudioSource = android.filterpacks.videosink.MediaEncoderFilter.NO_AUDIO_SOURCE;

    /**
     * Media recorder info listener, which needs to implement
     * MediaRecorder.OnInfoListener. Set this to receive notifications about
     * recording events.
     */
    @android.filterfw.core.GenerateFieldPort(name = "infoListener", hasDefault = true)
    private android.media.MediaRecorder.OnInfoListener mInfoListener = null;

    /**
     * Media recorder error listener, which needs to implement
     * MediaRecorder.OnErrorListener. Set this to receive notifications about
     * recording errors.
     */
    @android.filterfw.core.GenerateFieldPort(name = "errorListener", hasDefault = true)
    private android.media.MediaRecorder.OnErrorListener mErrorListener = null;

    /**
     * Media recording done callback, which needs to implement OnRecordingDoneListener.
     * Set this to finalize media upon completion of media recording.
     */
    @android.filterfw.core.GenerateFieldPort(name = "recordingDoneListener", hasDefault = true)
    private android.filterpacks.videosink.MediaEncoderFilter.OnRecordingDoneListener mRecordingDoneListener = null;

    /**
     * Orientation hint. Used for indicating proper video playback orientation.
     * Units are in degrees of clockwise rotation, valid values are (0, 90, 180,
     * 270).
     */
    @android.filterfw.core.GenerateFieldPort(name = "orientationHint", hasDefault = true)
    private int mOrientationHint = 0;

    /**
     * Camcorder profile to use. Select from the profiles available in
     * android.media.CamcorderProfile. If this field is set, it overrides
     * settings to width, height, framerate, outputFormat, and videoEncoder.
     */
    @android.filterfw.core.GenerateFieldPort(name = "recordingProfile", hasDefault = true)
    private android.media.CamcorderProfile mProfile = null;

    /**
     * Frame width to be encoded, defaults to 320.
     * Actual received frame size has to match this
     */
    @android.filterfw.core.GenerateFieldPort(name = "width", hasDefault = true)
    private int mWidth = 0;

    /**
     * Frame height to to be encoded, defaults to 240.
     * Actual received frame size has to match
     */
    @android.filterfw.core.GenerateFieldPort(name = "height", hasDefault = true)
    private int mHeight = 0;

    /**
     * Stream framerate to encode the frames at.
     * By default, frames are encoded at 30 FPS
     */
    @android.filterfw.core.GenerateFieldPort(name = "framerate", hasDefault = true)
    private int mFps = 30;

    /**
     * The output format to encode the frames in.
     * Choose an output format from the options in
     * android.media.MediaRecorder.OutputFormat
     */
    @android.filterfw.core.GenerateFieldPort(name = "outputFormat", hasDefault = true)
    private int mOutputFormat = android.media.MediaRecorder.OutputFormat.MPEG_4;

    /**
     * The videoencoder to encode the frames with.
     * Choose a videoencoder from the options in
     * android.media.MediaRecorder.VideoEncoder
     */
    @android.filterfw.core.GenerateFieldPort(name = "videoEncoder", hasDefault = true)
    private int mVideoEncoder = android.media.MediaRecorder.VideoEncoder.H264;

    /**
     * The input region to read from the frame. The corners of this quad are
     * mapped to the output rectangle. The input frame ranges from (0,0)-(1,1),
     * top-left to bottom-right. The corners of the quad are specified in the
     * order bottom-left, bottom-right, top-left, top-right.
     */
    @android.filterfw.core.GenerateFieldPort(name = "inputRegion", hasDefault = true)
    private android.filterfw.geometry.Quad mSourceRegion;

    /**
     * The maximum filesize (in bytes) of the recording session.
     * By default, it will be 0 and will be passed on to the MediaRecorder.
     * If the limit is zero or negative, MediaRecorder will disable the limit
     */
    @android.filterfw.core.GenerateFieldPort(name = "maxFileSize", hasDefault = true)
    private long mMaxFileSize = 0;

    /**
     * The maximum duration (in milliseconds) of the recording session.
     * By default, it will be 0 and will be passed on to the MediaRecorder.
     * If the limit is zero or negative, MediaRecorder will record indefinitely
     */
    @android.filterfw.core.GenerateFieldPort(name = "maxDurationMs", hasDefault = true)
    private int mMaxDurationMs = 0;

    /**
     * TimeLapse Interval between frames.
     * By default, it will be 0. Whether the recording is timelapsed
     * is inferred based on its value being greater than 0
     */
    @android.filterfw.core.GenerateFieldPort(name = "timelapseRecordingIntervalUs", hasDefault = true)
    private long mTimeBetweenTimeLapseFrameCaptureUs = 0;

    // End of user visible parameters
    private static final int NO_AUDIO_SOURCE = -1;

    private int mSurfaceId;

    private android.filterfw.core.ShaderProgram mProgram;

    private android.filterfw.core.GLFrame mScreen;

    private boolean mRecordingActive = false;

    private long mTimestampNs = 0;

    private long mLastTimeLapseFrameRealTimestampNs = 0;

    private int mNumFramesEncoded = 0;

    // Used to indicate whether recording is timelapsed.
    // Inferred based on (mTimeBetweenTimeLapseFrameCaptureUs > 0)
    private boolean mCaptureTimeLapse = false;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "MediaEncoderFilter";

    // Our hook to the encoder
    private android.media.MediaRecorder mMediaRecorder;

    /**
     * Callback to be called when media recording completes.
     */
    public interface OnRecordingDoneListener {
        public void onRecordingDone();
    }

    public MediaEncoderFilter(java.lang.String name) {
        super(name);
        android.filterfw.geometry.Point bl = new android.filterfw.geometry.Point(0, 0);
        android.filterfw.geometry.Point br = new android.filterfw.geometry.Point(1, 0);
        android.filterfw.geometry.Point tl = new android.filterfw.geometry.Point(0, 1);
        android.filterfw.geometry.Point tr = new android.filterfw.geometry.Point(1, 1);
        mSourceRegion = new android.filterfw.geometry.Quad(bl, br, tl, tr);
        mLogVerbose = android.util.Log.isLoggable(android.filterpacks.videosink.MediaEncoderFilter.TAG, android.util.Log.VERBOSE);
    }

    @java.lang.Override
    public void setupPorts() {
        // Add input port- will accept RGBA GLFrames
        addMaskedInputPort("videoframe", android.filterfw.format.ImageFormat.create(android.filterfw.format.ImageFormat.COLORSPACE_RGBA, android.filterfw.core.FrameFormat.TARGET_GPU));
    }

    @java.lang.Override
    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, ("Port " + name) + " has been updated");

        if (name.equals("recording"))
            return;

        if (name.equals("inputRegion")) {
            if (isOpen())
                updateSourceRegion();

            return;
        }
        // TODO: Not sure if it is possible to update the maxFileSize
        // when the recording is going on. For now, not doing that.
        if (isOpen() && mRecordingActive) {
            throw new java.lang.RuntimeException("Cannot change recording parameters" + " when the filter is recording!");
        }
    }

    private void updateSourceRegion() {
        // Flip source quad to map to OpenGL origin
        android.filterfw.geometry.Quad flippedRegion = new android.filterfw.geometry.Quad();
        flippedRegion.p0 = mSourceRegion.p2;
        flippedRegion.p1 = mSourceRegion.p3;
        flippedRegion.p2 = mSourceRegion.p0;
        flippedRegion.p3 = mSourceRegion.p1;
        mProgram.setSourceRegion(flippedRegion);
    }

    // update the MediaRecorderParams based on the variables.
    // These have to be in certain order as per the MediaRecorder
    // documentation
    private void updateMediaRecorderParams() {
        mCaptureTimeLapse = mTimeBetweenTimeLapseFrameCaptureUs > 0;
        final int GRALLOC_BUFFER = 2;
        mMediaRecorder.setVideoSource(GRALLOC_BUFFER);
        if ((!mCaptureTimeLapse) && (mAudioSource != android.filterpacks.videosink.MediaEncoderFilter.NO_AUDIO_SOURCE)) {
            mMediaRecorder.setAudioSource(mAudioSource);
        }
        if (mProfile != null) {
            mMediaRecorder.setProfile(mProfile);
            mFps = mProfile.videoFrameRate;
            // If width and height are set larger than 0, then those
            // overwrite the ones in the profile.
            if ((mWidth > 0) && (mHeight > 0)) {
                mMediaRecorder.setVideoSize(mWidth, mHeight);
            }
        } else {
            mMediaRecorder.setOutputFormat(mOutputFormat);
            mMediaRecorder.setVideoEncoder(mVideoEncoder);
            mMediaRecorder.setVideoSize(mWidth, mHeight);
            mMediaRecorder.setVideoFrameRate(mFps);
        }
        mMediaRecorder.setOrientationHint(mOrientationHint);
        mMediaRecorder.setOnInfoListener(mInfoListener);
        mMediaRecorder.setOnErrorListener(mErrorListener);
        if (mFd != null) {
            mMediaRecorder.setOutputFile(mFd);
        } else {
            mMediaRecorder.setOutputFile(mOutputFile);
        }
        try {
            mMediaRecorder.setMaxFileSize(mMaxFileSize);
        } catch (java.lang.Exception e) {
            // Following the logic in  VideoCamera.java (in Camera app)
            // We are going to ignore failure of setMaxFileSize here, as
            // a) The composer selected may simply not support it, or
            // b) The underlying media framework may not handle 64-bit range
            // on the size restriction.
            android.util.Log.w(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Setting maxFileSize on MediaRecorder unsuccessful! " + e.getMessage());
        }
        mMediaRecorder.setMaxDuration(mMaxDurationMs);
    }

    @java.lang.Override
    public void prepare(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Preparing");

        mProgram = android.filterfw.core.ShaderProgram.createIdentity(context);
        mRecordingActive = false;
    }

    @java.lang.Override
    public void open(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Opening");

        updateSourceRegion();
        if (mRecording)
            startRecording(context);

    }

    private void startRecording(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Starting recording");

        // Create a frame representing the screen
        android.filterfw.core.MutableFrameFormat screenFormat = new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_BYTE, android.filterfw.core.FrameFormat.TARGET_GPU);
        screenFormat.setBytesPerSample(4);
        int width;
        int height;
        boolean widthHeightSpecified = (mWidth > 0) && (mHeight > 0);
        // If width and height are specified, then use those instead
        // of that in the profile.
        if ((mProfile != null) && (!widthHeightSpecified)) {
            width = mProfile.videoFrameWidth;
            height = mProfile.videoFrameHeight;
        } else {
            width = mWidth;
            height = mHeight;
        }
        screenFormat.setDimensions(width, height);
        mScreen = ((android.filterfw.core.GLFrame) (context.getFrameManager().newBoundFrame(screenFormat, android.filterfw.core.GLFrame.EXISTING_FBO_BINDING, 0)));
        // Initialize the media recorder
        mMediaRecorder = new android.media.MediaRecorder();
        updateMediaRecorderParams();
        try {
            mMediaRecorder.prepare();
        } catch (java.lang.IllegalStateException e) {
            throw e;
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("IOException in" + "MediaRecorder.prepare()!", e);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Unknown Exception in" + "MediaRecorder.prepare()!", e);
        }
        // Make sure start() is called before trying to
        // register the surface. The native window handle needed to create
        // the surface is initiated in start()
        mMediaRecorder.start();
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Open: registering surface from Mediarecorder");

        mSurfaceId = context.getGLEnvironment().registerSurfaceFromMediaRecorder(mMediaRecorder);
        mNumFramesEncoded = 0;
        mRecordingActive = true;
    }

    public boolean skipFrameAndModifyTimestamp(long timestampNs) {
        // first frame- encode. Don't skip
        if (mNumFramesEncoded == 0) {
            mLastTimeLapseFrameRealTimestampNs = timestampNs;
            mTimestampNs = timestampNs;
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, (("timelapse: FIRST frame, last real t= " + mLastTimeLapseFrameRealTimestampNs) + ", setting t = ") + mTimestampNs);

            return false;
        }
        // Workaround to bypass the first 2 input frames for skipping.
        // The first 2 output frames from the encoder are: decoder specific info and
        // the compressed video frame data for the first input video frame.
        if ((mNumFramesEncoded >= 2) && (timestampNs < (mLastTimeLapseFrameRealTimestampNs + (1000L * mTimeBetweenTimeLapseFrameCaptureUs)))) {
            // If 2 frames have been already encoded,
            // Skip all frames from last encoded frame until
            // sufficient time (mTimeBetweenTimeLapseFrameCaptureUs) has passed.
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "timelapse: skipping intermediate frame");

            return true;
        } else {
            // Desired frame has arrived after mTimeBetweenTimeLapseFrameCaptureUs time:
            // - Reset mLastTimeLapseFrameRealTimestampNs to current time.
            // - Artificially modify timestampNs to be one frame time (1/framerate) ahead
            // of the last encoded frame's time stamp.
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, (((("timelapse: encoding frame, Timestamp t = " + timestampNs) + ", last real t= ") + mLastTimeLapseFrameRealTimestampNs) + ", interval = ") + mTimeBetweenTimeLapseFrameCaptureUs);

            mLastTimeLapseFrameRealTimestampNs = timestampNs;
            mTimestampNs = mTimestampNs + (1000000000L / ((long) (mFps)));
            if (mLogVerbose)
                android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, (((("timelapse: encoding frame, setting t = " + mTimestampNs) + ", delta t = ") + (1000000000L / ((long) (mFps)))) + ", fps = ") + mFps);

            return false;
        }
    }

    @java.lang.Override
    public void process(android.filterfw.core.FilterContext context) {
        android.filterfw.core.GLEnvironment glEnv = context.getGLEnvironment();
        // Get input frame
        android.filterfw.core.Frame input = pullInput("videoframe");
        // Check if recording needs to start
        if ((!mRecordingActive) && mRecording) {
            startRecording(context);
        }
        // Check if recording needs to stop
        if (mRecordingActive && (!mRecording)) {
            stopRecording(context);
        }
        if (!mRecordingActive)
            return;

        if (mCaptureTimeLapse) {
            if (skipFrameAndModifyTimestamp(input.getTimestamp())) {
                return;
            }
        } else {
            mTimestampNs = input.getTimestamp();
        }
        // Activate our surface
        glEnv.activateSurfaceWithId(mSurfaceId);
        // Process
        mProgram.process(input, mScreen);
        // Set timestamp from input
        glEnv.setSurfaceTimestamp(mTimestampNs);
        // And swap buffers
        glEnv.swapBuffers();
        mNumFramesEncoded++;
    }

    private void stopRecording(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Stopping recording");

        mRecordingActive = false;
        mNumFramesEncoded = 0;
        android.filterfw.core.GLEnvironment glEnv = context.getGLEnvironment();
        // The following call will switch the surface_id to 0
        // (thus, calling eglMakeCurrent on surface with id 0) and
        // then call eglDestroy on the surface. Hence, this will
        // call disconnect the SurfaceMediaSource, which is needed to
        // be called before calling Stop on the mediarecorder
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, java.lang.String.format("Unregistering surface %d", mSurfaceId));

        glEnv.unregisterSurfaceId(mSurfaceId);
        try {
            mMediaRecorder.stop();
        } catch (java.lang.RuntimeException e) {
            throw new android.filterpacks.videosink.MediaRecorderStopException("MediaRecorder.stop() failed!", e);
        }
        mMediaRecorder.release();
        mMediaRecorder = null;
        mScreen.release();
        mScreen = null;
        // Use an EffectsRecorder callback to forward a media finalization
        // call so that it creates the video thumbnail, and whatever else needs
        // to be done to finalize media.
        if (mRecordingDoneListener != null) {
            mRecordingDoneListener.onRecordingDone();
        }
    }

    @java.lang.Override
    public void close(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(android.filterpacks.videosink.MediaEncoderFilter.TAG, "Closing");

        if (mRecordingActive)
            stopRecording(context);

    }

    @java.lang.Override
    public void tearDown(android.filterfw.core.FilterContext context) {
        // Release all the resources associated with the MediaRecorder
        // and GLFrame members
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
        }
        if (mScreen != null) {
            mScreen.release();
        }
    }
}

