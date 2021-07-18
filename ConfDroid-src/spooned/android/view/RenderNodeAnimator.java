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
package android.view;


/**
 *
 *
 * @unknown 
 */
public class RenderNodeAnimator extends android.animation.Animator {
    // Keep in sync with enum RenderProperty in Animator.h
    public static final int TRANSLATION_X = 0;

    public static final int TRANSLATION_Y = 1;

    public static final int TRANSLATION_Z = 2;

    public static final int SCALE_X = 3;

    public static final int SCALE_Y = 4;

    public static final int ROTATION = 5;

    public static final int ROTATION_X = 6;

    public static final int ROTATION_Y = 7;

    public static final int X = 8;

    public static final int Y = 9;

    public static final int Z = 10;

    public static final int ALPHA = 11;

    // The last value in the enum, used for array size initialization
    public static final int LAST_VALUE = android.view.RenderNodeAnimator.ALPHA;

    // Keep in sync with enum PaintFields in Animator.h
    public static final int PAINT_STROKE_WIDTH = 0;

    /**
     * Field for the Paint alpha channel, which should be specified as a value
     * between 0 and 255.
     */
    public static final int PAINT_ALPHA = 1;

    // ViewPropertyAnimator uses a mask for its values, we need to remap them
    // to the enum values here. RenderPropertyAnimator can't use the mask values
    // directly as internally it uses a lookup table so it needs the values to
    // be sequential starting from 0
    private static final android.util.SparseIntArray sViewPropertyAnimatorMap = new android.util.SparseIntArray(15) {
        {
            put(android.view.ViewPropertyAnimator.TRANSLATION_X, android.view.RenderNodeAnimator.TRANSLATION_X);
            put(android.view.ViewPropertyAnimator.TRANSLATION_Y, android.view.RenderNodeAnimator.TRANSLATION_Y);
            put(android.view.ViewPropertyAnimator.TRANSLATION_Z, android.view.RenderNodeAnimator.TRANSLATION_Z);
            put(android.view.ViewPropertyAnimator.SCALE_X, android.view.RenderNodeAnimator.SCALE_X);
            put(android.view.ViewPropertyAnimator.SCALE_Y, android.view.RenderNodeAnimator.SCALE_Y);
            put(android.view.ViewPropertyAnimator.ROTATION, android.view.RenderNodeAnimator.ROTATION);
            put(android.view.ViewPropertyAnimator.ROTATION_X, android.view.RenderNodeAnimator.ROTATION_X);
            put(android.view.ViewPropertyAnimator.ROTATION_Y, android.view.RenderNodeAnimator.ROTATION_Y);
            put(android.view.ViewPropertyAnimator.X, android.view.RenderNodeAnimator.X);
            put(android.view.ViewPropertyAnimator.Y, android.view.RenderNodeAnimator.Y);
            put(android.view.ViewPropertyAnimator.Z, android.view.RenderNodeAnimator.Z);
            put(android.view.ViewPropertyAnimator.ALPHA, android.view.RenderNodeAnimator.ALPHA);
        }
    };

    private com.android.internal.util.VirtualRefBasePtr mNativePtr;

    private android.os.Handler mHandler;

    private android.graphics.RenderNode mTarget;

    private android.view.View mViewTarget;

    private int mRenderProperty = -1;

    private float mFinalValue;

    private android.animation.TimeInterpolator mInterpolator;

    private static final int STATE_PREPARE = 0;

    private static final int STATE_DELAYED = 1;

    private static final int STATE_RUNNING = 2;

    private static final int STATE_FINISHED = 3;

    private int mState = android.view.RenderNodeAnimator.STATE_PREPARE;

    private long mUnscaledDuration = 300;

    private long mUnscaledStartDelay = 0;

    // If this is true, we will run any start delays on the UI thread. This is
    // the safe default, and is necessary to ensure start listeners fire at
    // the correct time. Animators created by RippleDrawable (the
    // CanvasProperty<> ones) do not have this expectation, and as such will
    // set this to false so that the renderthread handles the startdelay instead
    private final boolean mUiThreadHandlesDelay;

    private long mStartDelay = 0;

    private long mStartTime;

    @android.annotation.UnsupportedAppUsage
    public static int mapViewPropertyToRenderProperty(int viewProperty) {
        return android.view.RenderNodeAnimator.sViewPropertyAnimatorMap.get(viewProperty);
    }

    @android.annotation.UnsupportedAppUsage
    public RenderNodeAnimator(int property, float finalValue) {
        mRenderProperty = property;
        mFinalValue = finalValue;
        mUiThreadHandlesDelay = true;
        init(android.view.RenderNodeAnimator.nCreateAnimator(property, finalValue));
    }

    @android.annotation.UnsupportedAppUsage
    public RenderNodeAnimator(android.graphics.CanvasProperty<java.lang.Float> property, float finalValue) {
        init(android.view.RenderNodeAnimator.nCreateCanvasPropertyFloatAnimator(property.getNativeContainer(), finalValue));
        mUiThreadHandlesDelay = false;
    }

    /**
     * Creates a new render node animator for a field on a Paint property.
     *
     * @param property
     * 		The paint property to target
     * @param paintField
     * 		Paint field to animate, one of {@link #PAINT_ALPHA} or
     * 		{@link #PAINT_STROKE_WIDTH}
     * @param finalValue
     * 		The target value for the property
     */
    @android.annotation.UnsupportedAppUsage
    public RenderNodeAnimator(android.graphics.CanvasProperty<android.graphics.Paint> property, int paintField, float finalValue) {
        init(android.view.RenderNodeAnimator.nCreateCanvasPropertyPaintAnimator(property.getNativeContainer(), paintField, finalValue));
        mUiThreadHandlesDelay = false;
    }

    public RenderNodeAnimator(int x, int y, float startRadius, float endRadius) {
        init(android.view.RenderNodeAnimator.nCreateRevealAnimator(x, y, startRadius, endRadius));
        mUiThreadHandlesDelay = true;
    }

    private void init(long ptr) {
        mNativePtr = new com.android.internal.util.VirtualRefBasePtr(ptr);
    }

    private void checkMutable() {
        if (mState != android.view.RenderNodeAnimator.STATE_PREPARE) {
            throw new java.lang.IllegalStateException("Animator has already started, cannot change it now!");
        }
        if (mNativePtr == null) {
            throw new java.lang.IllegalStateException("Animator's target has been destroyed " + "(trying to modify an animation after activity destroy?)");
        }
    }

    static boolean isNativeInterpolator(android.animation.TimeInterpolator interpolator) {
        return interpolator.getClass().isAnnotationPresent(com.android.internal.view.animation.HasNativeInterpolator.class);
    }

    private void applyInterpolator() {
        if ((mInterpolator == null) || (mNativePtr == null))
            return;

        long ni;
        if (android.view.RenderNodeAnimator.isNativeInterpolator(mInterpolator)) {
            ni = ((com.android.internal.view.animation.NativeInterpolatorFactory) (mInterpolator)).createNativeInterpolator();
        } else {
            long duration = android.view.RenderNodeAnimator.nGetDuration(mNativePtr.get());
            ni = com.android.internal.view.animation.FallbackLUTInterpolator.createNativeInterpolator(mInterpolator, duration);
        }
        android.view.RenderNodeAnimator.nSetInterpolator(mNativePtr.get(), ni);
    }

    @java.lang.Override
    public void start() {
        if (mTarget == null) {
            throw new java.lang.IllegalStateException("Missing target!");
        }
        if (mState != android.view.RenderNodeAnimator.STATE_PREPARE) {
            throw new java.lang.IllegalStateException("Already started!");
        }
        mState = android.view.RenderNodeAnimator.STATE_DELAYED;
        if (mHandler == null) {
            mHandler = new android.os.Handler(true);
        }
        applyInterpolator();
        if (mNativePtr == null) {
            // It's dead, immediately cancel
            cancel();
        } else
            if ((mStartDelay <= 0) || (!mUiThreadHandlesDelay)) {
                android.view.RenderNodeAnimator.nSetStartDelay(mNativePtr.get(), mStartDelay);
                doStart();
            } else {
                android.view.RenderNodeAnimator.getHelper().addDelayedAnimation(this);
            }

    }

    private void doStart() {
        // Alpha is a special snowflake that has the canonical value stored
        // in mTransformationInfo instead of in RenderNode, so we need to update
        // it with the final value here.
        if (mRenderProperty == android.view.RenderNodeAnimator.ALPHA) {
            mViewTarget.ensureTransformationInfo();
            mViewTarget.setAlphaInternal(mFinalValue);
        }
        moveToRunningState();
        if (mViewTarget != null) {
            // Kick off a frame to start the process
            mViewTarget.invalidateViewProperty(true, false);
        }
    }

    private void moveToRunningState() {
        mState = android.view.RenderNodeAnimator.STATE_RUNNING;
        if (mNativePtr != null) {
            android.view.RenderNodeAnimator.nStart(mNativePtr.get());
        }
        notifyStartListeners();
    }

    private void notifyStartListeners() {
        final java.util.ArrayList<android.view.AnimatorListener> listeners = cloneListeners();
        final int numListeners = (listeners == null) ? 0 : listeners.size();
        for (int i = 0; i < numListeners; i++) {
            listeners.get(i).onAnimationStart(this);
        }
    }

    @java.lang.Override
    public void cancel() {
        if ((mState != android.view.RenderNodeAnimator.STATE_PREPARE) && (mState != android.view.RenderNodeAnimator.STATE_FINISHED)) {
            if (mState == android.view.RenderNodeAnimator.STATE_DELAYED) {
                android.view.RenderNodeAnimator.getHelper().removeDelayedAnimation(this);
                moveToRunningState();
            }
            final java.util.ArrayList<android.view.AnimatorListener> listeners = cloneListeners();
            final int numListeners = (listeners == null) ? 0 : listeners.size();
            for (int i = 0; i < numListeners; i++) {
                listeners.get(i).onAnimationCancel(this);
            }
            end();
        }
    }

    @java.lang.Override
    public void end() {
        if (mState != android.view.RenderNodeAnimator.STATE_FINISHED) {
            if (mState < android.view.RenderNodeAnimator.STATE_RUNNING) {
                android.view.RenderNodeAnimator.getHelper().removeDelayedAnimation(this);
                doStart();
            }
            if (mNativePtr != null) {
                android.view.RenderNodeAnimator.nEnd(mNativePtr.get());
                if (mViewTarget != null) {
                    // Kick off a frame to flush the state change
                    mViewTarget.invalidateViewProperty(true, false);
                }
            } else {
                // It's already dead, jump to onFinish
                onFinished();
            }
        }
    }

    @java.lang.Override
    public void pause() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void resume() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setTarget(android.view.View view) {
        mViewTarget = view;
        setTarget(mViewTarget.mRenderNode);
    }

    /**
     * Sets the animation target to the owning view of the RecordingCanvas
     */
    public void setTarget(android.graphics.RecordingCanvas canvas) {
        setTarget(canvas.mNode);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setTarget(android.view.DisplayListCanvas canvas) {
        setTarget(((android.graphics.RecordingCanvas) (canvas)));
    }

    private void setTarget(android.graphics.RenderNode node) {
        checkMutable();
        if (mTarget != null) {
            throw new java.lang.IllegalStateException("Target already set!");
        }
        android.view.RenderNodeAnimator.nSetListener(mNativePtr.get(), this);
        mTarget = node;
        mTarget.addAnimator(this);
    }

    @android.annotation.UnsupportedAppUsage
    public void setStartValue(float startValue) {
        checkMutable();
        android.view.RenderNodeAnimator.nSetStartValue(mNativePtr.get(), startValue);
    }

    @java.lang.Override
    public void setStartDelay(long startDelay) {
        checkMutable();
        if (startDelay < 0) {
            throw new java.lang.IllegalArgumentException("startDelay must be positive; " + startDelay);
        }
        mUnscaledStartDelay = startDelay;
        mStartDelay = ((long) (android.animation.ValueAnimator.getDurationScale() * startDelay));
    }

    @java.lang.Override
    public long getStartDelay() {
        return mUnscaledStartDelay;
    }

    @android.annotation.UnsupportedAppUsage
    @java.lang.Override
    public android.view.RenderNodeAnimator setDuration(long duration) {
        checkMutable();
        if (duration < 0) {
            throw new java.lang.IllegalArgumentException("duration must be positive; " + duration);
        }
        mUnscaledDuration = duration;
        android.view.RenderNodeAnimator.nSetDuration(mNativePtr.get(), ((long) (duration * android.animation.ValueAnimator.getDurationScale())));
        return this;
    }

    @java.lang.Override
    public long getDuration() {
        return mUnscaledDuration;
    }

    @java.lang.Override
    public long getTotalDuration() {
        return mUnscaledDuration + mUnscaledStartDelay;
    }

    @java.lang.Override
    public boolean isRunning() {
        return (mState == android.view.RenderNodeAnimator.STATE_DELAYED) || (mState == android.view.RenderNodeAnimator.STATE_RUNNING);
    }

    @java.lang.Override
    public boolean isStarted() {
        return mState != android.view.RenderNodeAnimator.STATE_PREPARE;
    }

    @java.lang.Override
    public void setInterpolator(android.animation.TimeInterpolator interpolator) {
        checkMutable();
        mInterpolator = interpolator;
    }

    @java.lang.Override
    public android.animation.TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    protected void onFinished() {
        if (mState == android.view.RenderNodeAnimator.STATE_PREPARE) {
            // Unlikely but possible, the native side has been destroyed
            // before we have started.
            releaseNativePtr();
            return;
        }
        if (mState == android.view.RenderNodeAnimator.STATE_DELAYED) {
            android.view.RenderNodeAnimator.getHelper().removeDelayedAnimation(this);
            notifyStartListeners();
        }
        mState = android.view.RenderNodeAnimator.STATE_FINISHED;
        final java.util.ArrayList<android.view.AnimatorListener> listeners = cloneListeners();
        final int numListeners = (listeners == null) ? 0 : listeners.size();
        for (int i = 0; i < numListeners; i++) {
            listeners.get(i).onAnimationEnd(this);
        }
        // Release the native object, as it has a global reference to us. This
        // breaks the cyclic reference chain, and allows this object to be
        // GC'd
        releaseNativePtr();
    }

    private void releaseNativePtr() {
        if (mNativePtr != null) {
            mNativePtr.release();
            mNativePtr = null;
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.ArrayList<android.view.AnimatorListener> cloneListeners() {
        java.util.ArrayList<android.view.AnimatorListener> listeners = getListeners();
        if (listeners != null) {
            listeners = ((java.util.ArrayList<android.view.AnimatorListener>) (listeners.clone()));
        }
        return listeners;
    }

    public long getNativeAnimator() {
        return mNativePtr.get();
    }

    /**
     *
     *
     * @return true if the animator was started, false if still delayed
     */
    private boolean processDelayed(long frameTimeMs) {
        if (mStartTime == 0) {
            mStartTime = frameTimeMs;
        } else
            if ((frameTimeMs - mStartTime) >= mStartDelay) {
                doStart();
                return true;
            }

        return false;
    }

    private static android.view.RenderNodeAnimator.DelayedAnimationHelper getHelper() {
        android.view.RenderNodeAnimator.DelayedAnimationHelper helper = android.view.RenderNodeAnimator.sAnimationHelper.get();
        if (helper == null) {
            helper = new android.view.RenderNodeAnimator.DelayedAnimationHelper();
            android.view.RenderNodeAnimator.sAnimationHelper.set(helper);
        }
        return helper;
    }

    private static java.lang.ThreadLocal<android.view.RenderNodeAnimator.DelayedAnimationHelper> sAnimationHelper = new java.lang.ThreadLocal<android.view.RenderNodeAnimator.DelayedAnimationHelper>();

    private static class DelayedAnimationHelper implements java.lang.Runnable {
        private java.util.ArrayList<android.view.RenderNodeAnimator> mDelayedAnims = new java.util.ArrayList<android.view.RenderNodeAnimator>();

        private final android.view.Choreographer mChoreographer;

        private boolean mCallbackScheduled;

        public DelayedAnimationHelper() {
            mChoreographer = android.view.Choreographer.getInstance();
        }

        public void addDelayedAnimation(android.view.RenderNodeAnimator animator) {
            mDelayedAnims.add(animator);
            scheduleCallback();
        }

        public void removeDelayedAnimation(android.view.RenderNodeAnimator animator) {
            mDelayedAnims.remove(animator);
        }

        private void scheduleCallback() {
            if (!mCallbackScheduled) {
                mCallbackScheduled = true;
                mChoreographer.postCallback(android.view.Choreographer.CALLBACK_ANIMATION, this, null);
            }
        }

        @java.lang.Override
        public void run() {
            long frameTimeMs = mChoreographer.getFrameTime();
            mCallbackScheduled = false;
            int end = 0;
            for (int i = 0; i < mDelayedAnims.size(); i++) {
                android.view.RenderNodeAnimator animator = mDelayedAnims.get(i);
                if (!animator.processDelayed(frameTimeMs)) {
                    if (end != i) {
                        mDelayedAnims.set(end, animator);
                    }
                    end++;
                }
            }
            while (mDelayedAnims.size() > end) {
                mDelayedAnims.remove(mDelayedAnims.size() - 1);
            } 
            if (mDelayedAnims.size() > 0) {
                scheduleCallback();
            }
        }
    }

    // Called by native
    @android.annotation.UnsupportedAppUsage
    private static void callOnFinished(android.view.RenderNodeAnimator animator) {
        if (animator.mHandler != null) {
            animator.mHandler.post(animator::onFinished);
        } else {
            new android.os.Handler(android.os.Looper.getMainLooper(), null, true).post(animator::onFinished);
        }
    }

    @java.lang.Override
    public android.animation.Animator clone() {
        throw new java.lang.IllegalStateException("Cannot clone this animator");
    }

    @java.lang.Override
    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
        checkMutable();
        android.view.RenderNodeAnimator.nSetAllowRunningAsync(mNativePtr.get(), mayRunAsync);
    }

    private static native long nCreateAnimator(int property, float finalValue);

    private static native long nCreateCanvasPropertyFloatAnimator(long canvasProperty, float finalValue);

    private static native long nCreateCanvasPropertyPaintAnimator(long canvasProperty, int paintField, float finalValue);

    private static native long nCreateRevealAnimator(int x, int y, float startRadius, float endRadius);

    private static native void nSetStartValue(long nativePtr, float startValue);

    private static native void nSetDuration(long nativePtr, long duration);

    private static native long nGetDuration(long nativePtr);

    private static native void nSetStartDelay(long nativePtr, long startDelay);

    private static native void nSetInterpolator(long animPtr, long interpolatorPtr);

    private static native void nSetAllowRunningAsync(long animPtr, boolean mayRunAsync);

    private static native void nSetListener(long animPtr, android.view.RenderNodeAnimator listener);

    private static native void nStart(long animPtr);

    private static native void nEnd(long animPtr);
}

