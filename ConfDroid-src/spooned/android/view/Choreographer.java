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
package android.view;


/**
 * Coordinates the timing of animations, input and drawing.
 * <p>
 * The choreographer receives timing pulses (such as vertical synchronization)
 * from the display subsystem then schedules work to occur as part of rendering
 * the next display frame.
 * </p><p>
 * Applications typically interact with the choreographer indirectly using
 * higher level abstractions in the animation framework or the view hierarchy.
 * Here are some examples of things you can do using the higher-level APIs.
 * </p>
 * <ul>
 * <li>To post an animation to be processed on a regular time basis synchronized with
 * display frame rendering, use {@link android.animation.ValueAnimator#start}.</li>
 * <li>To post a {@link Runnable} to be invoked once at the beginning of the next display
 * frame, use {@link View#postOnAnimation}.</li>
 * <li>To post a {@link Runnable} to be invoked once at the beginning of the next display
 * frame after a delay, use {@link View#postOnAnimationDelayed}.</li>
 * <li>To post a call to {@link View#invalidate()} to occur once at the beginning of the
 * next display frame, use {@link View#postInvalidateOnAnimation()} or
 * {@link View#postInvalidateOnAnimation(int, int, int, int)}.</li>
 * <li>To ensure that the contents of a {@link View} scroll smoothly and are drawn in
 * sync with display frame rendering, do nothing.  This already happens automatically.
 * {@link View#onDraw} will be called at the appropriate time.</li>
 * </ul>
 * <p>
 * However, there are a few cases where you might want to use the functions of the
 * choreographer directly in your application.  Here are some examples.
 * </p>
 * <ul>
 * <li>If your application does its rendering in a different thread, possibly using GL,
 * or does not use the animation framework or view hierarchy at all
 * and you want to ensure that it is appropriately synchronized with the display, then use
 * {@link Choreographer#postFrameCallback}.</li>
 * <li>... and that's about it.</li>
 * </ul>
 * <p>
 * Each {@link Looper} thread has its own choreographer.  Other threads can
 * post callbacks to run on the choreographer but they will run on the {@link Looper}
 * to which the choreographer belongs.
 * </p>
 */
public final class Choreographer {
    private static final java.lang.String TAG = "Choreographer";

    // Prints debug messages about jank which was detected (low volume).
    private static final boolean DEBUG_JANK = false;

    // Prints debug messages about every frame and callback registered (high volume).
    private static final boolean DEBUG_FRAMES = false;

    // The default amount of time in ms between animation frames.
    // When vsync is not enabled, we want to have some idea of how long we should
    // wait before posting the next animation message.  It is important that the
    // default value be less than the true inter-frame delay on all devices to avoid
    // situations where we might skip frames by waiting too long (we must compensate
    // for jitter and hardware variations).  Regardless of this value, the animation
    // and display loop is ultimately rate-limited by how fast new graphics buffers can
    // be dequeued.
    private static final long DEFAULT_FRAME_DELAY = 10;

    // The number of milliseconds between animation frames.
    private static volatile long sFrameDelay = android.view.Choreographer.DEFAULT_FRAME_DELAY;

    // Thread local storage for the choreographer.
    private static final java.lang.ThreadLocal<android.view.Choreographer> sThreadInstance = new java.lang.ThreadLocal<android.view.Choreographer>() {
        @java.lang.Override
        protected android.view.Choreographer initialValue() {
            android.os.Looper looper = android.os.Looper.myLooper();
            if (looper == null) {
                throw new java.lang.IllegalStateException("The current thread must have a looper!");
            }
            android.view.Choreographer choreographer = new android.view.Choreographer(looper, android.view.DisplayEventReceiver.VSYNC_SOURCE_APP);
            if (looper == android.os.Looper.getMainLooper()) {
                android.view.Choreographer.mMainInstance = choreographer;
            }
            return choreographer;
        }
    };

    private static volatile android.view.Choreographer mMainInstance;

    // Thread local storage for the SF choreographer.
    private static final java.lang.ThreadLocal<android.view.Choreographer> sSfThreadInstance = new java.lang.ThreadLocal<android.view.Choreographer>() {
        @java.lang.Override
        protected android.view.Choreographer initialValue() {
            android.os.Looper looper = android.os.Looper.myLooper();
            if (looper == null) {
                throw new java.lang.IllegalStateException("The current thread must have a looper!");
            }
            return new android.view.Choreographer(looper, android.view.DisplayEventReceiver.VSYNC_SOURCE_SURFACE_FLINGER);
        }
    };

    // Enable/disable vsync for animations and drawing.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123769497)
    private static final boolean USE_VSYNC = android.os.SystemProperties.getBoolean("debug.choreographer.vsync", true);

    // Enable/disable using the frame time instead of returning now.
    private static final boolean USE_FRAME_TIME = android.os.SystemProperties.getBoolean("debug.choreographer.frametime", true);

    // Set a limit to warn about skipped frames.
    // Skipped frames imply jank.
    private static final int SKIPPED_FRAME_WARNING_LIMIT = android.os.SystemProperties.getInt("debug.choreographer.skipwarning", 30);

    private static final int MSG_DO_FRAME = 0;

    private static final int MSG_DO_SCHEDULE_VSYNC = 1;

    private static final int MSG_DO_SCHEDULE_CALLBACK = 2;

    // All frame callbacks posted by applications have this token.
    private static final java.lang.Object FRAME_CALLBACK_TOKEN = new java.lang.Object() {
        public java.lang.String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final java.lang.Object mLock = new java.lang.Object();

    private final android.os.Looper mLooper;

    private final android.view.Choreographer.FrameHandler mHandler;

    // The display event receiver can only be accessed by the looper thread to which
    // it is attached.  We take care to ensure that we post message to the looper
    // if appropriate when interacting with the display event receiver.
    @android.annotation.UnsupportedAppUsage
    private final android.view.Choreographer.FrameDisplayEventReceiver mDisplayEventReceiver;

    private android.view.Choreographer.CallbackRecord mCallbackPool;

    @android.annotation.UnsupportedAppUsage
    private final android.view.Choreographer.CallbackQueue[] mCallbackQueues;

    private boolean mFrameScheduled;

    private boolean mCallbacksRunning;

    @android.annotation.UnsupportedAppUsage
    private long mLastFrameTimeNanos;

    @android.annotation.UnsupportedAppUsage
    private long mFrameIntervalNanos;

    private boolean mDebugPrintNextFrameTimeDelta;

    private int mFPSDivisor = 1;

    /**
     * Contains information about the current frame for jank-tracking,
     * mainly timings of key events along with a bit of metadata about
     * view tree state
     *
     * TODO: Is there a better home for this? Currently Choreographer
     * is the only one with CALLBACK_ANIMATION start time, hence why this
     * resides here.
     *
     * @unknown 
     */
    android.graphics.FrameInfo mFrameInfo = new android.graphics.FrameInfo();

    /**
     * Must be kept in sync with CALLBACK_* ints below, used to index into this array.
     *
     * @unknown 
     */
    private static final java.lang.String[] CALLBACK_TRACE_TITLES = new java.lang.String[]{ "input", "animation", "insets_animation", "traversal", "commit" };

    /**
     * Callback type: Input callback.  Runs first.
     *
     * @unknown 
     */
    public static final int CALLBACK_INPUT = 0;

    /**
     * Callback type: Animation callback.  Runs before {@link #CALLBACK_INSETS_ANIMATION}.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public static final int CALLBACK_ANIMATION = 1;

    /**
     * Callback type: Animation callback to handle inset updates. This is separate from
     * {@link #CALLBACK_ANIMATION} as we need to "gather" all inset animation updates via
     * {@link WindowInsetsAnimationController#changeInsets} for multiple ongoing animations but then
     * update the whole view system with a single callback to {@link View#dispatchWindowInsetsAnimationProgress}
     * that contains all the combined updated insets.
     * <p>
     * Both input and animation may change insets, so we need to run this after these callbacks, but
     * before traversals.
     * <p>
     * Runs before traversals.
     *
     * @unknown 
     */
    public static final int CALLBACK_INSETS_ANIMATION = 2;

    /**
     * Callback type: Traversal callback.  Handles layout and draw.  Runs
     * after all other asynchronous messages have been handled.
     *
     * @unknown 
     */
    public static final int CALLBACK_TRAVERSAL = 3;

    /**
     * Callback type: Commit callback.  Handles post-draw operations for the frame.
     * Runs after traversal completes.  The {@link #getFrameTime() frame time} reported
     * during this callback may be updated to reflect delays that occurred while
     * traversals were in progress in case heavy layout operations caused some frames
     * to be skipped.  The frame time reported during this callback provides a better
     * estimate of the start time of the frame in which animations (and other updates
     * to the view hierarchy state) actually took effect.
     *
     * @unknown 
     */
    public static final int CALLBACK_COMMIT = 4;

    private static final int CALLBACK_LAST = android.view.Choreographer.CALLBACK_COMMIT;

    private Choreographer(android.os.Looper looper, int vsyncSource) {
        mLooper = looper;
        mHandler = new android.view.Choreographer.FrameHandler(looper);
        mDisplayEventReceiver = (android.view.Choreographer.USE_VSYNC) ? new android.view.Choreographer.FrameDisplayEventReceiver(looper, vsyncSource) : null;
        mLastFrameTimeNanos = java.lang.Long.MIN_VALUE;
        mFrameIntervalNanos = ((long) (1000000000 / android.view.Choreographer.getRefreshRate()));
        mCallbackQueues = new android.view.Choreographer.CallbackQueue[android.view.Choreographer.CALLBACK_LAST + 1];
        for (int i = 0; i <= android.view.Choreographer.CALLBACK_LAST; i++) {
            mCallbackQueues[i] = new android.view.Choreographer.CallbackQueue();
        }
        // b/68769804: For low FPS experiments.
        setFPSDivisor(android.os.SystemProperties.getInt(android.view.ThreadedRenderer.DEBUG_FPS_DIVISOR, 1));
    }

    private static float getRefreshRate() {
        android.view.DisplayInfo di = android.hardware.display.DisplayManagerGlobal.getInstance().getDisplayInfo(android.view.Display.DEFAULT_DISPLAY);
        return di.getMode().getRefreshRate();
    }

    /**
     * Gets the choreographer for the calling thread.  Must be called from
     * a thread that already has a {@link android.os.Looper} associated with it.
     *
     * @return The choreographer for this thread.
     * @throws IllegalStateException
     * 		if the thread does not have a looper.
     */
    public static android.view.Choreographer getInstance() {
        return android.view.Choreographer.sThreadInstance.get();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.view.Choreographer getSfInstance() {
        return android.view.Choreographer.sSfThreadInstance.get();
    }

    /**
     *
     *
     * @return The Choreographer of the main thread, if it exists, or {@code null} otherwise.
     * @unknown 
     */
    public static android.view.Choreographer getMainThreadInstance() {
        return android.view.Choreographer.mMainInstance;
    }

    /**
     * Destroys the calling thread's choreographer
     *
     * @unknown 
     */
    public static void releaseInstance() {
        android.view.Choreographer old = android.view.Choreographer.sThreadInstance.get();
        android.view.Choreographer.sThreadInstance.remove();
        old.dispose();
    }

    private void dispose() {
        mDisplayEventReceiver.dispose();
    }

    /**
     * The amount of time, in milliseconds, between each frame of the animation.
     * <p>
     * This is a requested time that the animation will attempt to honor, but the actual delay
     * between frames may be different, depending on system load and capabilities. This is a static
     * function because the same delay will be applied to all animations, since they are all
     * run off of a single timing loop.
     * </p><p>
     * The frame delay may be ignored when the animation system uses an external timing
     * source, such as the display refresh rate (vsync), to govern animations.
     * </p>
     *
     * @return the requested time between frames, in milliseconds
     * @unknown 
     */
    @android.annotation.TestApi
    public static long getFrameDelay() {
        return android.view.Choreographer.sFrameDelay;
    }

    /**
     * The amount of time, in milliseconds, between each frame of the animation.
     * <p>
     * This is a requested time that the animation will attempt to honor, but the actual delay
     * between frames may be different, depending on system load and capabilities. This is a static
     * function because the same delay will be applied to all animations, since they are all
     * run off of a single timing loop.
     * </p><p>
     * The frame delay may be ignored when the animation system uses an external timing
     * source, such as the display refresh rate (vsync), to govern animations.
     * </p>
     *
     * @param frameDelay
     * 		the requested time between frames, in milliseconds
     * @unknown 
     */
    @android.annotation.TestApi
    public static void setFrameDelay(long frameDelay) {
        android.view.Choreographer.sFrameDelay = frameDelay;
    }

    /**
     * Subtracts typical frame delay time from a delay interval in milliseconds.
     * <p>
     * This method can be used to compensate for animation delay times that have baked
     * in assumptions about the frame delay.  For example, it's quite common for code to
     * assume a 60Hz frame time and bake in a 16ms delay.  When we call
     * {@link #postAnimationCallbackDelayed} we want to know how long to wait before
     * posting the animation callback but let the animation timer take care of the remaining
     * frame delay time.
     * </p><p>
     * This method is somewhat conservative about how much of the frame delay it
     * subtracts.  It uses the same value returned by {@link #getFrameDelay} which by
     * default is 10ms even though many parts of the system assume 16ms.  Consequently,
     * we might still wait 6ms before posting an animation callback that we want to run
     * on the next frame, but this is much better than waiting a whole 16ms and likely
     * missing the deadline.
     * </p>
     *
     * @param delayMillis
     * 		The original delay time including an assumed frame delay.
     * @return The adjusted delay time with the assumed frame delay subtracted out.
     * @unknown 
     */
    public static long subtractFrameDelay(long delayMillis) {
        final long frameDelay = android.view.Choreographer.sFrameDelay;
        return delayMillis <= frameDelay ? 0 : delayMillis - frameDelay;
    }

    /**
     *
     *
     * @return The refresh rate as the nanoseconds between frames
     * @unknown 
     */
    public long getFrameIntervalNanos() {
        return mFrameIntervalNanos;
    }

    void dump(java.lang.String prefix, java.io.PrintWriter writer) {
        java.lang.String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("Choreographer:");
        writer.print(innerPrefix);
        writer.print("mFrameScheduled=");
        writer.println(mFrameScheduled);
        writer.print(innerPrefix);
        writer.print("mLastFrameTime=");
        writer.println(android.util.TimeUtils.formatUptime(mLastFrameTimeNanos / 1000000));
    }

    /**
     * Posts a callback to run on the next frame.
     * <p>
     * The callback runs once then is automatically removed.
     * </p>
     *
     * @param callbackType
     * 		The callback type.
     * @param action
     * 		The callback action to run during the next frame.
     * @param token
     * 		The callback token, or null if none.
     * @see #removeCallbacks
     * @unknown 
     */
    @android.annotation.TestApi
    public void postCallback(int callbackType, java.lang.Runnable action, java.lang.Object token) {
        postCallbackDelayed(callbackType, action, token, 0);
    }

    /**
     * Posts a callback to run on the next frame after the specified delay.
     * <p>
     * The callback runs once then is automatically removed.
     * </p>
     *
     * @param callbackType
     * 		The callback type.
     * @param action
     * 		The callback action to run during the next frame after the specified delay.
     * @param token
     * 		The callback token, or null if none.
     * @param delayMillis
     * 		The delay time in milliseconds.
     * @see #removeCallback
     * @unknown 
     */
    @android.annotation.TestApi
    public void postCallbackDelayed(int callbackType, java.lang.Runnable action, java.lang.Object token, long delayMillis) {
        if (action == null) {
            throw new java.lang.IllegalArgumentException("action must not be null");
        }
        if ((callbackType < 0) || (callbackType > android.view.Choreographer.CALLBACK_LAST)) {
            throw new java.lang.IllegalArgumentException("callbackType is invalid");
        }
        postCallbackDelayedInternal(callbackType, action, token, delayMillis);
    }

    private void postCallbackDelayedInternal(int callbackType, java.lang.Object action, java.lang.Object token, long delayMillis) {
        if (android.view.Choreographer.DEBUG_FRAMES) {
            android.util.Log.d(android.view.Choreographer.TAG, (((((("PostCallback: type=" + callbackType) + ", action=") + action) + ", token=") + token) + ", delayMillis=") + delayMillis);
        }
        synchronized(mLock) {
            final long now = android.os.SystemClock.uptimeMillis();
            final long dueTime = now + delayMillis;
            mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
            if (dueTime <= now) {
                scheduleFrameLocked(now);
            } else {
                android.os.Message msg = mHandler.obtainMessage(android.view.Choreographer.MSG_DO_SCHEDULE_CALLBACK, action);
                msg.arg1 = callbackType;
                msg.setAsynchronous(true);
                mHandler.sendMessageAtTime(msg, dueTime);
            }
        }
    }

    /**
     * Removes callbacks that have the specified action and token.
     *
     * @param callbackType
     * 		The callback type.
     * @param action
     * 		The action property of the callbacks to remove, or null to remove
     * 		callbacks with any action.
     * @param token
     * 		The token property of the callbacks to remove, or null to remove
     * 		callbacks with any token.
     * @see #postCallback
     * @see #postCallbackDelayed
     * @unknown 
     */
    @android.annotation.TestApi
    public void removeCallbacks(int callbackType, java.lang.Runnable action, java.lang.Object token) {
        if ((callbackType < 0) || (callbackType > android.view.Choreographer.CALLBACK_LAST)) {
            throw new java.lang.IllegalArgumentException("callbackType is invalid");
        }
        removeCallbacksInternal(callbackType, action, token);
    }

    private void removeCallbacksInternal(int callbackType, java.lang.Object action, java.lang.Object token) {
        if (android.view.Choreographer.DEBUG_FRAMES) {
            android.util.Log.d(android.view.Choreographer.TAG, (((("RemoveCallbacks: type=" + callbackType) + ", action=") + action) + ", token=") + token);
        }
        synchronized(mLock) {
            mCallbackQueues[callbackType].removeCallbacksLocked(action, token);
            if ((action != null) && (token == null)) {
                mHandler.removeMessages(android.view.Choreographer.MSG_DO_SCHEDULE_CALLBACK, action);
            }
        }
    }

    /**
     * Posts a frame callback to run on the next frame.
     * <p>
     * The callback runs once then is automatically removed.
     * </p>
     *
     * @param callback
     * 		The frame callback to run during the next frame.
     * @see #postFrameCallbackDelayed
     * @see #removeFrameCallback
     */
    public void postFrameCallback(android.view.Choreographer.FrameCallback callback) {
        postFrameCallbackDelayed(callback, 0);
    }

    /**
     * Posts a frame callback to run on the next frame after the specified delay.
     * <p>
     * The callback runs once then is automatically removed.
     * </p>
     *
     * @param callback
     * 		The frame callback to run during the next frame.
     * @param delayMillis
     * 		The delay time in milliseconds.
     * @see #postFrameCallback
     * @see #removeFrameCallback
     */
    public void postFrameCallbackDelayed(android.view.Choreographer.FrameCallback callback, long delayMillis) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        postCallbackDelayedInternal(android.view.Choreographer.CALLBACK_ANIMATION, callback, android.view.Choreographer.FRAME_CALLBACK_TOKEN, delayMillis);
    }

    /**
     * Removes a previously posted frame callback.
     *
     * @param callback
     * 		The frame callback to remove.
     * @see #postFrameCallback
     * @see #postFrameCallbackDelayed
     */
    public void removeFrameCallback(android.view.Choreographer.FrameCallback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback must not be null");
        }
        removeCallbacksInternal(android.view.Choreographer.CALLBACK_ANIMATION, callback, android.view.Choreographer.FRAME_CALLBACK_TOKEN);
    }

    /**
     * Gets the time when the current frame started.
     * <p>
     * This method provides the time in milliseconds when the frame started being rendered.
     * The frame time provides a stable time base for synchronizing animations
     * and drawing.  It should be used instead of {@link SystemClock#uptimeMillis()}
     * or {@link System#nanoTime()} for animations and drawing in the UI.  Using the frame
     * time helps to reduce inter-frame jitter because the frame time is fixed at the time
     * the frame was scheduled to start, regardless of when the animations or drawing
     * callback actually runs.  All callbacks that run as part of rendering a frame will
     * observe the same frame time so using the frame time also helps to synchronize effects
     * that are performed by different callbacks.
     * </p><p>
     * Please note that the framework already takes care to process animations and
     * drawing using the frame time as a stable time base.  Most applications should
     * not need to use the frame time information directly.
     * </p><p>
     * This method should only be called from within a callback.
     * </p>
     *
     * @return The frame start time, in the {@link SystemClock#uptimeMillis()} time base.
     * @throws IllegalStateException
     * 		if no frame is in progress.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long getFrameTime() {
        return getFrameTimeNanos() / android.util.TimeUtils.NANOS_PER_MS;
    }

    /**
     * Same as {@link #getFrameTime()} but with nanosecond precision.
     *
     * @return The frame start time, in the {@link System#nanoTime()} time base.
     * @throws IllegalStateException
     * 		if no frame is in progress.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long getFrameTimeNanos() {
        synchronized(mLock) {
            if (!mCallbacksRunning) {
                throw new java.lang.IllegalStateException("This method must only be called as " + "part of a callback while a frame is in progress.");
            }
            return android.view.Choreographer.USE_FRAME_TIME ? mLastFrameTimeNanos : java.lang.System.nanoTime();
        }
    }

    /**
     * Like {@link #getLastFrameTimeNanos}, but always returns the last frame time, not matter
     * whether callbacks are currently running.
     *
     * @return The frame start time of the last frame, in the {@link System#nanoTime()} time base.
     * @unknown 
     */
    public long getLastFrameTimeNanos() {
        synchronized(mLock) {
            return android.view.Choreographer.USE_FRAME_TIME ? mLastFrameTimeNanos : java.lang.System.nanoTime();
        }
    }

    private void scheduleFrameLocked(long now) {
        if (!mFrameScheduled) {
            mFrameScheduled = true;
            if (android.view.Choreographer.USE_VSYNC) {
                if (android.view.Choreographer.DEBUG_FRAMES) {
                    android.util.Log.d(android.view.Choreographer.TAG, "Scheduling next frame on vsync.");
                }
                // If running on the Looper thread, then schedule the vsync immediately,
                // otherwise post a message to schedule the vsync from the UI thread
                // as soon as possible.
                if (isRunningOnLooperThreadLocked()) {
                    scheduleVsyncLocked();
                } else {
                    android.os.Message msg = mHandler.obtainMessage(android.view.Choreographer.MSG_DO_SCHEDULE_VSYNC);
                    msg.setAsynchronous(true);
                    mHandler.sendMessageAtFrontOfQueue(msg);
                }
            } else {
                final long nextFrameTime = java.lang.Math.max((mLastFrameTimeNanos / android.util.TimeUtils.NANOS_PER_MS) + android.view.Choreographer.sFrameDelay, now);
                if (android.view.Choreographer.DEBUG_FRAMES) {
                    android.util.Log.d(android.view.Choreographer.TAG, ("Scheduling next frame in " + (nextFrameTime - now)) + " ms.");
                }
                android.os.Message msg = mHandler.obtainMessage(android.view.Choreographer.MSG_DO_FRAME);
                msg.setAsynchronous(true);
                mHandler.sendMessageAtTime(msg, nextFrameTime);
            }
        }
    }

    void setFPSDivisor(int divisor) {
        if (divisor <= 0)
            divisor = 1;

        mFPSDivisor = divisor;
        android.view.ThreadedRenderer.setFPSDivisor(divisor);
    }

    @android.annotation.UnsupportedAppUsage
    void doFrame(long frameTimeNanos, int frame) {
        final long startNanos;
        synchronized(mLock) {
            if (!mFrameScheduled) {
                return;// no work to do

            }
            if (android.view.Choreographer.DEBUG_JANK && mDebugPrintNextFrameTimeDelta) {
                mDebugPrintNextFrameTimeDelta = false;
                android.util.Log.d(android.view.Choreographer.TAG, ("Frame time delta: " + ((frameTimeNanos - mLastFrameTimeNanos) * 1.0E-6F)) + " ms");
            }
            long intendedFrameTimeNanos = frameTimeNanos;
            startNanos = java.lang.System.nanoTime();
            final long jitterNanos = startNanos - frameTimeNanos;
            if (jitterNanos >= mFrameIntervalNanos) {
                final long skippedFrames = jitterNanos / mFrameIntervalNanos;
                if (skippedFrames >= android.view.Choreographer.SKIPPED_FRAME_WARNING_LIMIT) {
                    android.util.Log.i(android.view.Choreographer.TAG, (("Skipped " + skippedFrames) + " frames!  ") + "The application may be doing too much work on its main thread.");
                }
                final long lastFrameOffset = jitterNanos % mFrameIntervalNanos;
                if (android.view.Choreographer.DEBUG_JANK) {
                    android.util.Log.d(android.view.Choreographer.TAG, (((((((((("Missed vsync by " + (jitterNanos * 1.0E-6F)) + " ms ") + "which is more than the frame interval of ") + (mFrameIntervalNanos * 1.0E-6F)) + " ms!  ") + "Skipping ") + skippedFrames) + " frames and setting frame ") + "time to ") + (lastFrameOffset * 1.0E-6F)) + " ms in the past.");
                }
                frameTimeNanos = startNanos - lastFrameOffset;
            }
            if (frameTimeNanos < mLastFrameTimeNanos) {
                if (android.view.Choreographer.DEBUG_JANK) {
                    android.util.Log.d(android.view.Choreographer.TAG, "Frame time appears to be going backwards.  May be due to a " + "previously skipped frame.  Waiting for next vsync.");
                }
                scheduleVsyncLocked();
                return;
            }
            if (mFPSDivisor > 1) {
                long timeSinceVsync = frameTimeNanos - mLastFrameTimeNanos;
                if ((timeSinceVsync < (mFrameIntervalNanos * mFPSDivisor)) && (timeSinceVsync > 0)) {
                    scheduleVsyncLocked();
                    return;
                }
            }
            mFrameInfo.setVsync(intendedFrameTimeNanos, frameTimeNanos);
            mFrameScheduled = false;
            mLastFrameTimeNanos = frameTimeNanos;
        }
        try {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "Choreographer#doFrame");
            android.view.animation.AnimationUtils.lockAnimationClock(frameTimeNanos / android.util.TimeUtils.NANOS_PER_MS);
            mFrameInfo.markInputHandlingStart();
            doCallbacks(android.view.Choreographer.CALLBACK_INPUT, frameTimeNanos);
            mFrameInfo.markAnimationsStart();
            doCallbacks(android.view.Choreographer.CALLBACK_ANIMATION, frameTimeNanos);
            doCallbacks(android.view.Choreographer.CALLBACK_INSETS_ANIMATION, frameTimeNanos);
            mFrameInfo.markPerformTraversalsStart();
            doCallbacks(android.view.Choreographer.CALLBACK_TRAVERSAL, frameTimeNanos);
            doCallbacks(android.view.Choreographer.CALLBACK_COMMIT, frameTimeNanos);
        } finally {
            android.view.animation.AnimationUtils.unlockAnimationClock();
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
        if (android.view.Choreographer.DEBUG_FRAMES) {
            final long endNanos = java.lang.System.nanoTime();
            android.util.Log.d(android.view.Choreographer.TAG, ((((("Frame " + frame) + ": Finished, took ") + ((endNanos - startNanos) * 1.0E-6F)) + " ms, latency ") + ((startNanos - frameTimeNanos) * 1.0E-6F)) + " ms.");
        }
    }

    void doCallbacks(int callbackType, long frameTimeNanos) {
        android.view.Choreographer.CallbackRecord callbacks;
        synchronized(mLock) {
            // We use "now" to determine when callbacks become due because it's possible
            // for earlier processing phases in a frame to post callbacks that should run
            // in a following phase, such as an input event that causes an animation to start.
            final long now = java.lang.System.nanoTime();
            callbacks = mCallbackQueues[callbackType].extractDueCallbacksLocked(now / android.util.TimeUtils.NANOS_PER_MS);
            if (callbacks == null) {
                return;
            }
            mCallbacksRunning = true;
            // Update the frame time if necessary when committing the frame.
            // We only update the frame time if we are more than 2 frames late reaching
            // the commit phase.  This ensures that the frame time which is observed by the
            // callbacks will always increase from one frame to the next and never repeat.
            // We never want the next frame's starting frame time to end up being less than
            // or equal to the previous frame's commit frame time.  Keep in mind that the
            // next frame has most likely already been scheduled by now so we play it
            // safe by ensuring the commit time is always at least one frame behind.
            if (callbackType == android.view.Choreographer.CALLBACK_COMMIT) {
                final long jitterNanos = now - frameTimeNanos;
                android.os.Trace.traceCounter(Trace.TRACE_TAG_VIEW, "jitterNanos", ((int) (jitterNanos)));
                if (jitterNanos >= (2 * mFrameIntervalNanos)) {
                    final long lastFrameOffset = (jitterNanos % mFrameIntervalNanos) + mFrameIntervalNanos;
                    if (android.view.Choreographer.DEBUG_JANK) {
                        android.util.Log.d(android.view.Choreographer.TAG, (((((("Commit callback delayed by " + (jitterNanos * 1.0E-6F)) + " ms which is more than twice the frame interval of ") + (mFrameIntervalNanos * 1.0E-6F)) + " ms!  ") + "Setting frame time to ") + (lastFrameOffset * 1.0E-6F)) + " ms in the past.");
                        mDebugPrintNextFrameTimeDelta = true;
                    }
                    frameTimeNanos = now - lastFrameOffset;
                    mLastFrameTimeNanos = frameTimeNanos;
                }
            }
        }
        try {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, android.view.Choreographer.CALLBACK_TRACE_TITLES[callbackType]);
            for (android.view.Choreographer.CallbackRecord c = callbacks; c != null; c = c.next) {
                if (android.view.Choreographer.DEBUG_FRAMES) {
                    android.util.Log.d(android.view.Choreographer.TAG, (((((("RunCallback: type=" + callbackType) + ", action=") + c.action) + ", token=") + c.token) + ", latencyMillis=") + (android.os.SystemClock.uptimeMillis() - c.dueTime));
                }
                c.run(frameTimeNanos);
            }
        } finally {
            synchronized(mLock) {
                mCallbacksRunning = false;
                do {
                    final android.view.Choreographer.CallbackRecord next = callbacks.next;
                    recycleCallbackLocked(callbacks);
                    callbacks = next;
                } while (callbacks != null );
            }
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
    }

    void doScheduleVsync() {
        synchronized(mLock) {
            if (mFrameScheduled) {
                scheduleVsyncLocked();
            }
        }
    }

    void doScheduleCallback(int callbackType) {
        synchronized(mLock) {
            if (!mFrameScheduled) {
                final long now = android.os.SystemClock.uptimeMillis();
                if (mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    scheduleFrameLocked(now);
                }
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void scheduleVsyncLocked() {
        mDisplayEventReceiver.scheduleVsync();
    }

    private boolean isRunningOnLooperThreadLocked() {
        return android.os.Looper.myLooper() == mLooper;
    }

    private android.view.Choreographer.CallbackRecord obtainCallbackLocked(long dueTime, java.lang.Object action, java.lang.Object token) {
        android.view.Choreographer.CallbackRecord callback = mCallbackPool;
        if (callback == null) {
            callback = new android.view.Choreographer.CallbackRecord();
        } else {
            mCallbackPool = callback.next;
            callback.next = null;
        }
        callback.dueTime = dueTime;
        callback.action = action;
        callback.token = token;
        return callback;
    }

    private void recycleCallbackLocked(android.view.Choreographer.CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = mCallbackPool;
        mCallbackPool = callback;
    }

    /**
     * Implement this interface to receive a callback when a new display frame is
     * being rendered.  The callback is invoked on the {@link Looper} thread to
     * which the {@link Choreographer} is attached.
     */
    public interface FrameCallback {
        /**
         * Called when a new display frame is being rendered.
         * <p>
         * This method provides the time in nanoseconds when the frame started being rendered.
         * The frame time provides a stable time base for synchronizing animations
         * and drawing.  It should be used instead of {@link SystemClock#uptimeMillis()}
         * or {@link System#nanoTime()} for animations and drawing in the UI.  Using the frame
         * time helps to reduce inter-frame jitter because the frame time is fixed at the time
         * the frame was scheduled to start, regardless of when the animations or drawing
         * callback actually runs.  All callbacks that run as part of rendering a frame will
         * observe the same frame time so using the frame time also helps to synchronize effects
         * that are performed by different callbacks.
         * </p><p>
         * Please note that the framework already takes care to process animations and
         * drawing using the frame time as a stable time base.  Most applications should
         * not need to use the frame time information directly.
         * </p>
         *
         * @param frameTimeNanos
         * 		The time in nanoseconds when the frame started being rendered,
         * 		in the {@link System#nanoTime()} timebase.  Divide this value by {@code 1000000}
         * 		to convert it to the {@link SystemClock#uptimeMillis()} time base.
         */
        public void doFrame(long frameTimeNanos);
    }

    private final class FrameHandler extends android.os.Handler {
        public FrameHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.view.Choreographer.MSG_DO_FRAME :
                    doFrame(java.lang.System.nanoTime(), 0);
                    break;
                case android.view.Choreographer.MSG_DO_SCHEDULE_VSYNC :
                    doScheduleVsync();
                    break;
                case android.view.Choreographer.MSG_DO_SCHEDULE_CALLBACK :
                    doScheduleCallback(msg.arg1);
                    break;
            }
        }
    }

    private final class FrameDisplayEventReceiver extends android.view.DisplayEventReceiver implements java.lang.Runnable {
        private boolean mHavePendingVsync;

        private long mTimestampNanos;

        private int mFrame;

        public FrameDisplayEventReceiver(android.os.Looper looper, int vsyncSource) {
            super(looper, vsyncSource);
        }

        // TODO(b/116025192): physicalDisplayId is ignored because SF only emits VSYNC events for
        // the internal display and DisplayEventReceiver#scheduleVsync only allows requesting VSYNC
        // for the internal display implicitly.
        @java.lang.Override
        public void onVsync(long timestampNanos, long physicalDisplayId, int frame) {
            // Post the vsync event to the Handler.
            // The idea is to prevent incoming vsync events from completely starving
            // the message queue.  If there are no messages in the queue with timestamps
            // earlier than the frame time, then the vsync event will be processed immediately.
            // Otherwise, messages that predate the vsync event will be handled first.
            long now = java.lang.System.nanoTime();
            if (timestampNanos > now) {
                android.util.Log.w(android.view.Choreographer.TAG, (("Frame time is " + ((timestampNanos - now) * 1.0E-6F)) + " ms in the future!  Check that graphics HAL is generating vsync ") + "timestamps using the correct timebase.");
                timestampNanos = now;
            }
            if (mHavePendingVsync) {
                android.util.Log.w(android.view.Choreographer.TAG, "Already have a pending vsync event.  There should only be " + "one at a time.");
            } else {
                mHavePendingVsync = true;
            }
            mTimestampNanos = timestampNanos;
            mFrame = frame;
            android.os.Message msg = android.os.Message.obtain(mHandler, this);
            msg.setAsynchronous(true);
            mHandler.sendMessageAtTime(msg, timestampNanos / android.util.TimeUtils.NANOS_PER_MS);
        }

        @java.lang.Override
        public void run() {
            mHavePendingVsync = false;
            doFrame(mTimestampNanos, mFrame);
        }
    }

    private static final class CallbackRecord {
        public android.view.Choreographer.CallbackRecord next;

        public long dueTime;

        public java.lang.Object action;// Runnable or FrameCallback


        public java.lang.Object token;

        @android.annotation.UnsupportedAppUsage
        public void run(long frameTimeNanos) {
            if (token == android.view.Choreographer.FRAME_CALLBACK_TOKEN) {
                ((android.view.Choreographer.FrameCallback) (action)).doFrame(frameTimeNanos);
            } else {
                ((java.lang.Runnable) (action)).run();
            }
        }
    }

    private final class CallbackQueue {
        private android.view.Choreographer.CallbackRecord mHead;

        public boolean hasDueCallbacksLocked(long now) {
            return (mHead != null) && (mHead.dueTime <= now);
        }

        public android.view.Choreographer.CallbackRecord extractDueCallbacksLocked(long now) {
            android.view.Choreographer.CallbackRecord callbacks = mHead;
            if ((callbacks == null) || (callbacks.dueTime > now)) {
                return null;
            }
            android.view.Choreographer.CallbackRecord last = callbacks;
            android.view.Choreographer.CallbackRecord next = last.next;
            while (next != null) {
                if (next.dueTime > now) {
                    last.next = null;
                    break;
                }
                last = next;
                next = next.next;
            } 
            mHead = next;
            return callbacks;
        }

        @android.annotation.UnsupportedAppUsage
        public void addCallbackLocked(long dueTime, java.lang.Object action, java.lang.Object token) {
            android.view.Choreographer.CallbackRecord callback = obtainCallbackLocked(dueTime, action, token);
            android.view.Choreographer.CallbackRecord entry = mHead;
            if (entry == null) {
                mHead = callback;
                return;
            }
            if (dueTime < entry.dueTime) {
                callback.next = entry;
                mHead = callback;
                return;
            }
            while (entry.next != null) {
                if (dueTime < entry.next.dueTime) {
                    callback.next = entry.next;
                    break;
                }
                entry = entry.next;
            } 
            entry.next = callback;
        }

        public void removeCallbacksLocked(java.lang.Object action, java.lang.Object token) {
            android.view.Choreographer.CallbackRecord predecessor = null;
            for (android.view.Choreographer.CallbackRecord callback = mHead; callback != null;) {
                final android.view.Choreographer.CallbackRecord next = callback.next;
                if (((action == null) || (callback.action == action)) && ((token == null) || (callback.token == token))) {
                    if (predecessor != null) {
                        predecessor.next = next;
                    } else {
                        mHead = next;
                    }
                    recycleCallbackLocked(callback);
                } else {
                    predecessor = callback;
                }
                callback = next;
            }
        }
    }
}

