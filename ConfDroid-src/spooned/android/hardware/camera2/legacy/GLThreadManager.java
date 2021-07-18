/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.camera2.legacy;


/**
 * GLThreadManager handles the thread used for rendering into the configured output surfaces.
 */
public class GLThreadManager {
    private final java.lang.String TAG;

    private static final boolean DEBUG = false;

    private static final int MSG_NEW_CONFIGURATION = 1;

    private static final int MSG_NEW_FRAME = 2;

    private static final int MSG_CLEANUP = 3;

    private static final int MSG_DROP_FRAMES = 4;

    private static final int MSG_ALLOW_FRAMES = 5;

    private android.hardware.camera2.legacy.CaptureCollector mCaptureCollector;

    private final android.hardware.camera2.legacy.CameraDeviceState mDeviceState;

    private final android.hardware.camera2.legacy.SurfaceTextureRenderer mTextureRenderer;

    private final android.hardware.camera2.legacy.RequestHandlerThread mGLHandlerThread;

    private final android.hardware.camera2.legacy.RequestThreadManager.FpsCounter mPrevCounter = new android.hardware.camera2.legacy.RequestThreadManager.FpsCounter("GL Preview Producer");

    /**
     * Container object for Configure messages.
     */
    private static class ConfigureHolder {
        public final android.os.ConditionVariable condition;

        public final java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces;

        public final android.hardware.camera2.legacy.CaptureCollector collector;

        public ConfigureHolder(android.os.ConditionVariable condition, java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces, android.hardware.camera2.legacy.CaptureCollector collector) {
            this.condition = condition;
            this.surfaces = surfaces;
            this.collector = collector;
        }
    }

    private final android.os.Handler.Callback mGLHandlerCb = new android.os.Handler.Callback() {
        private boolean mCleanup = false;

        private boolean mConfigured = false;

        private boolean mDroppingFrames = false;

        @java.lang.SuppressWarnings("unchecked")
        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            if (mCleanup) {
                return true;
            }
            try {
                switch (msg.what) {
                    case android.hardware.camera2.legacy.GLThreadManager.MSG_NEW_CONFIGURATION :
                        android.hardware.camera2.legacy.GLThreadManager.ConfigureHolder configure = ((android.hardware.camera2.legacy.GLThreadManager.ConfigureHolder) (msg.obj));
                        mTextureRenderer.cleanupEGLContext();
                        mTextureRenderer.configureSurfaces(configure.surfaces);
                        mCaptureCollector = checkNotNull(configure.collector);
                        configure.condition.open();
                        mConfigured = true;
                        break;
                    case android.hardware.camera2.legacy.GLThreadManager.MSG_NEW_FRAME :
                        if (mDroppingFrames) {
                            android.util.Log.w(TAG, "Ignoring frame.");
                            break;
                        }
                        if (android.hardware.camera2.legacy.GLThreadManager.DEBUG) {
                            mPrevCounter.countAndLog();
                        }
                        if (!mConfigured) {
                            android.util.Log.e(TAG, "Dropping frame, EGL context not configured!");
                        }
                        mTextureRenderer.drawIntoSurfaces(mCaptureCollector);
                        break;
                    case android.hardware.camera2.legacy.GLThreadManager.MSG_CLEANUP :
                        mTextureRenderer.cleanupEGLContext();
                        mCleanup = true;
                        mConfigured = false;
                        break;
                    case android.hardware.camera2.legacy.GLThreadManager.MSG_DROP_FRAMES :
                        mDroppingFrames = true;
                        break;
                    case android.hardware.camera2.legacy.GLThreadManager.MSG_ALLOW_FRAMES :
                        mDroppingFrames = false;
                        break;
                    case android.hardware.camera2.legacy.RequestHandlerThread.MSG_POKE_IDLE_HANDLER :
                        // OK: Ignore message.
                        break;
                    default :
                        android.util.Log.e(TAG, ("Unhandled message " + msg.what) + " on GLThread.");
                        break;
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Received exception on GL render thread: ", e);
                mDeviceState.setError(ERROR_CAMERA_DEVICE);
            }
            return true;
        }
    };

    /**
     * Create a new GL thread and renderer.
     *
     * @param cameraId
     * 		the camera id for this thread.
     * @param facing
     * 		direction the camera is facing.
     * @param state
     * 		{@link CameraDeviceState} to use for error handling.
     */
    public GLThreadManager(int cameraId, int facing, android.hardware.camera2.legacy.CameraDeviceState state) {
        mTextureRenderer = new android.hardware.camera2.legacy.SurfaceTextureRenderer(facing);
        TAG = java.lang.String.format("CameraDeviceGLThread-%d", cameraId);
        mGLHandlerThread = new android.hardware.camera2.legacy.RequestHandlerThread(TAG, mGLHandlerCb);
        mDeviceState = state;
    }

    /**
     * Start the thread.
     *
     * <p>
     * This must be called before queueing new frames.
     * </p>
     */
    public void start() {
        mGLHandlerThread.start();
    }

    /**
     * Wait until the thread has started.
     */
    public void waitUntilStarted() {
        mGLHandlerThread.waitUntilStarted();
    }

    /**
     * Quit the thread.
     *
     * <p>
     * No further methods can be called after this.
     * </p>
     */
    public void quit() {
        android.os.Handler handler = mGLHandlerThread.getHandler();
        handler.sendMessageAtFrontOfQueue(handler.obtainMessage(android.hardware.camera2.legacy.GLThreadManager.MSG_CLEANUP));
        mGLHandlerThread.quitSafely();
        try {
            mGLHandlerThread.join();
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(TAG, java.lang.String.format("Thread %s (%d) interrupted while quitting.", mGLHandlerThread.getName(), mGLHandlerThread.getId()));
        }
    }

    /**
     * Queue a new call to draw into the surfaces specified in the next available preview
     * request from the {@link CaptureCollector} passed to
     * {@link #setConfigurationAndWait(java.util.Collection, CaptureCollector)};
     */
    public void queueNewFrame() {
        android.os.Handler handler = mGLHandlerThread.getHandler();
        /**
         * Avoid queuing more than one new frame.  If we are not consuming faster than frames
         * are produced, drop frames rather than allowing the queue to back up.
         */
        if (!handler.hasMessages(android.hardware.camera2.legacy.GLThreadManager.MSG_NEW_FRAME)) {
            handler.sendMessage(handler.obtainMessage(android.hardware.camera2.legacy.GLThreadManager.MSG_NEW_FRAME));
        } else {
            android.util.Log.e(TAG, "GLThread dropping frame.  Not consuming frames quickly enough!");
        }
    }

    /**
     * Configure the GL renderer for the given set of output surfaces, and block until
     * this configuration has been applied.
     *
     * @param surfaces
     * 		a collection of pairs of {@link android.view.Surface}s and their
     * 		corresponding sizes to configure.
     * @param collector
     * 		a {@link CaptureCollector} to retrieve requests from.
     */
    public void setConfigurationAndWait(java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> surfaces, android.hardware.camera2.legacy.CaptureCollector collector) {
        checkNotNull(collector, "collector must not be null");
        android.os.Handler handler = mGLHandlerThread.getHandler();
        final android.os.ConditionVariable condition = /* closed */
        new android.os.ConditionVariable(false);
        android.hardware.camera2.legacy.GLThreadManager.ConfigureHolder configure = new android.hardware.camera2.legacy.GLThreadManager.ConfigureHolder(condition, surfaces, collector);
        android.os.Message m = /* arg1 */
        /* arg2 */
        handler.obtainMessage(android.hardware.camera2.legacy.GLThreadManager.MSG_NEW_CONFIGURATION, 0, 0, configure);
        handler.sendMessage(m);
        // Block until configuration applied.
        condition.block();
    }

    /**
     * Get the underlying surface to produce frames from.
     *
     * <p>
     * This returns the surface that is drawn into the set of surfaces passed in for each frame.
     * This method should only be called after a call to
     * {@link #setConfigurationAndWait(java.util.Collection)}.  Calling this before the first call
     * to {@link #setConfigurationAndWait(java.util.Collection)}, after {@link #quit()}, or
     * concurrently to one of these calls may result in an invalid
     * {@link android.graphics.SurfaceTexture} being returned.
     * </p>
     *
     * @return an {@link android.graphics.SurfaceTexture} to draw to.
     */
    public android.graphics.SurfaceTexture getCurrentSurfaceTexture() {
        return mTextureRenderer.getSurfaceTexture();
    }

    /**
     * Ignore any subsequent calls to {@link #queueNewFrame(java.util.Collection)}.
     */
    public void ignoreNewFrames() {
        mGLHandlerThread.getHandler().sendEmptyMessage(android.hardware.camera2.legacy.GLThreadManager.MSG_DROP_FRAMES);
    }

    /**
     * Wait until no messages are queued.
     */
    public void waitUntilIdle() {
        mGLHandlerThread.waitUntilIdle();
    }

    /**
     * Re-enable drawing new frames after a call to {@link #ignoreNewFrames()}.
     */
    public void allowNewFrames() {
        mGLHandlerThread.getHandler().sendEmptyMessage(android.hardware.camera2.legacy.GLThreadManager.MSG_ALLOW_FRAMES);
    }
}

