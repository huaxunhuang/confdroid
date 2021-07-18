/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.drawable;


/**
 * {@link Drawable} for drawing animated images (like GIF).
 *
 * <p>The framework handles decoding subsequent frames in another thread and
 * updating when necessary. The drawable will only animate while it is being
 * displayed.</p>
 *
 * <p>Created by {@link ImageDecoder#decodeDrawable}. A user needs to call
 * {@link #start} to start the animation.</p>
 *
 * <p>It can also be defined in XML using the <code>&lt;animated-image></code>
 * element.</p>
 *
 * @unknown ref android.R.styleable#AnimatedImageDrawable_src
 * @unknown ref android.R.styleable#AnimatedImageDrawable_autoStart
 * @unknown ref android.R.styleable#AnimatedImageDrawable_repeatCount
 * @unknown ref android.R.styleable#AnimatedImageDrawable_autoMirrored
 */
public class AnimatedImageDrawable extends android.graphics.drawable.Drawable implements android.graphics.drawable.Animatable2 {
    private int mIntrinsicWidth;

    private int mIntrinsicHeight;

    private boolean mStarting;

    private android.os.Handler mHandler;

    private class State {
        State(long nativePtr, java.io.InputStream is, android.content.res.AssetFileDescriptor afd) {
            mNativePtr = nativePtr;
            mInputStream = is;
            mAssetFd = afd;
        }

        final long mNativePtr;

        // These just keep references so the native code can continue using them.
        private final java.io.InputStream mInputStream;

        private final android.content.res.AssetFileDescriptor mAssetFd;

        int[] mThemeAttrs = null;

        boolean mAutoMirrored = false;

        int mRepeatCount = android.graphics.drawable.AnimatedImageDrawable.REPEAT_UNDEFINED;
    }

    private android.graphics.drawable.AnimatedImageDrawable.State mState;

    private java.lang.Runnable mRunnable;

    private android.graphics.ColorFilter mColorFilter;

    /**
     * Pass this to {@link #setRepeatCount} to repeat infinitely.
     *
     *  <p>{@link Animatable2.AnimationCallback#onAnimationEnd} will never be
     *  called unless there is an error.</p>
     */
    public static final int REPEAT_INFINITE = -1;

    /**
     *
     *
     * @unknown 
     * @deprecated Replaced with REPEAT_INFINITE to match other APIs.
     */
    @java.lang.Deprecated
    public static final int LOOP_INFINITE = android.graphics.drawable.AnimatedImageDrawable.REPEAT_INFINITE;

    private static final int REPEAT_UNDEFINED = -2;

    /**
     * Specify the number of times to repeat the animation.
     *
     *  <p>By default, the repeat count in the encoded data is respected. If set
     *  to {@link #REPEAT_INFINITE}, the animation will repeat as long as it is
     *  displayed. If the value is {@code 0}, the animation will play once.</p>
     *
     *  <p>This call replaces the current repeat count. If the encoded data
     *  specified a repeat count of {@code 2} (meaning that
     *  {@link #getRepeatCount()} returns {@code 2}, the animation will play
     *  three times. Calling {@code setRepeatCount(1)} will result in playing only
     *  twice and {@link #getRepeatCount()} returning {@code 1}.</p>
     *
     *  <p>If the animation is already playing, the iterations that have already
     *  occurred count towards the new count. If the animation has already
     *  repeated the appropriate number of times (or more), it will finish its
     *  current iteration and then stop.</p>
     */
    public void setRepeatCount(@android.annotation.IntRange(from = android.graphics.drawable.AnimatedImageDrawable.REPEAT_INFINITE)
    int repeatCount) {
        if (repeatCount < android.graphics.drawable.AnimatedImageDrawable.REPEAT_INFINITE) {
            throw new java.lang.IllegalArgumentException("invalid value passed to setRepeatCount" + repeatCount);
        }
        if (mState.mRepeatCount != repeatCount) {
            mState.mRepeatCount = repeatCount;
            if (mState.mNativePtr != 0) {
                android.graphics.drawable.AnimatedImageDrawable.nSetRepeatCount(mState.mNativePtr, repeatCount);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     * @deprecated Replaced with setRepeatCount to match other APIs.
     */
    @java.lang.Deprecated
    public void setLoopCount(int loopCount) {
        setRepeatCount(loopCount);
    }

    /**
     * Retrieve the number of times the animation will repeat.
     *
     *  <p>By default, the repeat count in the encoded data is respected. If the
     *  value is {@link #REPEAT_INFINITE}, the animation will repeat as long as
     *  it is displayed. If the value is {@code 0}, it will play once.</p>
     *
     *  <p>Calling {@link #setRepeatCount} will make future calls to this method
     *  return the value passed to {@link #setRepeatCount}.</p>
     */
    public int getRepeatCount() {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called getRepeatCount on empty AnimatedImageDrawable");
        }
        if (mState.mRepeatCount == android.graphics.drawable.AnimatedImageDrawable.REPEAT_UNDEFINED) {
            mState.mRepeatCount = android.graphics.drawable.AnimatedImageDrawable.nGetRepeatCount(mState.mNativePtr);
        }
        return mState.mRepeatCount;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated Replaced with getRepeatCount to match other APIs.
     */
    @java.lang.Deprecated
    public int getLoopCount(int loopCount) {
        return getRepeatCount();
    }

    /**
     * Create an empty AnimatedImageDrawable.
     */
    public AnimatedImageDrawable() {
        mState = new android.graphics.drawable.AnimatedImageDrawable.State(0, null, null);
    }

    @java.lang.Override
    public void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedImageDrawable);
        updateStateFromTypedArray(a, mSrcDensityOverride);
    }

    private void updateStateFromTypedArray(android.content.res.TypedArray a, int srcDensityOverride) throws org.xmlpull.v1.XmlPullParserException {
        android.graphics.drawable.AnimatedImageDrawable.State oldState = mState;
        final android.content.res.Resources r = a.getResources();
        final int srcResId = a.getResourceId(R.styleable.AnimatedImageDrawable_src, 0);
        if (srcResId != 0) {
            // Follow the density handling in BitmapDrawable.
            final android.util.TypedValue value = new android.util.TypedValue();
            r.getValueForDensity(srcResId, srcDensityOverride, value, true);
            if (((srcDensityOverride > 0) && (value.density > 0)) && (value.density != android.util.TypedValue.DENSITY_NONE)) {
                if (value.density == srcDensityOverride) {
                    value.density = r.getDisplayMetrics().densityDpi;
                } else {
                    value.density = (value.density * r.getDisplayMetrics().densityDpi) / srcDensityOverride;
                }
            }
            int density = android.graphics.Bitmap.DENSITY_NONE;
            if (value.density == android.util.TypedValue.DENSITY_DEFAULT) {
                density = android.util.DisplayMetrics.DENSITY_DEFAULT;
            } else
                if (value.density != android.util.TypedValue.DENSITY_NONE) {
                    density = value.density;
                }

            android.graphics.drawable.Drawable drawable = null;
            try {
                java.io.InputStream is = r.openRawResource(srcResId, value);
                android.graphics.ImageDecoder.Source source = android.graphics.ImageDecoder.createSource(r, is, density);
                drawable = android.graphics.ImageDecoder.decodeDrawable(source, ( decoder, info, src) -> {
                    if (!info.isAnimated()) {
                        throw new java.lang.IllegalArgumentException("image is not animated");
                    }
                });
            } catch (java.io.IOException e) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <animated-image> requires a valid 'src' attribute", null, e);
            }
            if (!(drawable instanceof android.graphics.drawable.AnimatedImageDrawable)) {
                throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <animated-image> did not decode animated");
            }
            // This may have previously been set without a src if we were waiting for a
            // theme.
            final int repeatCount = mState.mRepeatCount;
            // Transfer the state of other to this one. other will be discarded.
            android.graphics.drawable.AnimatedImageDrawable other = ((android.graphics.drawable.AnimatedImageDrawable) (drawable));
            mState = other.mState;
            other.mState = null;
            mIntrinsicWidth = other.mIntrinsicWidth;
            mIntrinsicHeight = other.mIntrinsicHeight;
            if (repeatCount != android.graphics.drawable.AnimatedImageDrawable.REPEAT_UNDEFINED) {
                this.setRepeatCount(repeatCount);
            }
        }
        mState.mThemeAttrs = a.extractThemeAttrs();
        if ((mState.mNativePtr == 0) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[R.styleable.AnimatedImageDrawable_src] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException(a.getPositionDescription() + ": <animated-image> requires a valid 'src' attribute");
        }
        mState.mAutoMirrored = a.getBoolean(R.styleable.AnimatedImageDrawable_autoMirrored, oldState.mAutoMirrored);
        int repeatCount = a.getInt(R.styleable.AnimatedImageDrawable_repeatCount, android.graphics.drawable.AnimatedImageDrawable.REPEAT_UNDEFINED);
        if (repeatCount != android.graphics.drawable.AnimatedImageDrawable.REPEAT_UNDEFINED) {
            this.setRepeatCount(repeatCount);
        }
        boolean autoStart = a.getBoolean(R.styleable.AnimatedImageDrawable_autoStart, false);
        if (autoStart && (mState.mNativePtr != 0)) {
            this.start();
        }
    }

    /**
     *
     *
     * @unknown This should only be called by ImageDecoder.

    decoder is only non-null if it has a PostProcess
     */
    public AnimatedImageDrawable(long nativeImageDecoder, @android.annotation.Nullable
    android.graphics.ImageDecoder decoder, int width, int height, long colorSpaceHandle, boolean extended, int srcDensity, int dstDensity, android.graphics.Rect cropRect, java.io.InputStream inputStream, android.content.res.AssetFileDescriptor afd) throws java.io.IOException {
        width = android.graphics.Bitmap.scaleFromDensity(width, srcDensity, dstDensity);
        height = android.graphics.Bitmap.scaleFromDensity(height, srcDensity, dstDensity);
        if (cropRect == null) {
            mIntrinsicWidth = width;
            mIntrinsicHeight = height;
        } else {
            cropRect.set(android.graphics.Bitmap.scaleFromDensity(cropRect.left, srcDensity, dstDensity), android.graphics.Bitmap.scaleFromDensity(cropRect.top, srcDensity, dstDensity), android.graphics.Bitmap.scaleFromDensity(cropRect.right, srcDensity, dstDensity), android.graphics.Bitmap.scaleFromDensity(cropRect.bottom, srcDensity, dstDensity));
            mIntrinsicWidth = cropRect.width();
            mIntrinsicHeight = cropRect.height();
        }
        mState = new android.graphics.drawable.AnimatedImageDrawable.State(android.graphics.drawable.AnimatedImageDrawable.nCreate(nativeImageDecoder, decoder, width, height, colorSpaceHandle, extended, cropRect), inputStream, afd);
        final long nativeSize = android.graphics.drawable.AnimatedImageDrawable.nNativeByteSize(mState.mNativePtr);
        libcore.util.NativeAllocationRegistry registry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.drawable.AnimatedImageDrawable.class.getClassLoader(), android.graphics.drawable.AnimatedImageDrawable.nGetNativeFinalizer(), nativeSize);
        registry.registerNativeAllocation(mState, mState.mNativePtr);
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    // nDraw returns -1 if the animation has finished.
    private static final int FINISHED = -1;

    @java.lang.Override
    public void draw(@android.annotation.NonNull
    android.graphics.Canvas canvas) {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called draw on empty AnimatedImageDrawable");
        }
        if (mStarting) {
            mStarting = false;
            postOnAnimationStart();
        }
        long nextUpdate = android.graphics.drawable.AnimatedImageDrawable.nDraw(mState.mNativePtr, canvas.getNativeCanvasWrapper());
        // a value <= 0 indicates that the drawable is stopped or that renderThread
        // will manage the animation
        if (nextUpdate > 0) {
            if (mRunnable == null) {
                mRunnable = this::invalidateSelf;
            }
            scheduleSelf(mRunnable, nextUpdate + android.os.SystemClock.uptimeMillis());
        } else
            if (nextUpdate == android.graphics.drawable.AnimatedImageDrawable.FINISHED) {
                // This means the animation was drawn in software mode and ended.
                postOnAnimationEnd();
            }

    }

    @java.lang.Override
    public void setAlpha(@android.annotation.IntRange(from = 0, to = 255)
    int alpha) {
        if ((alpha < 0) || (alpha > 255)) {
            throw new java.lang.IllegalArgumentException(("Alpha must be between 0 and" + " 255! provided ") + alpha);
        }
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called setAlpha on empty AnimatedImageDrawable");
        }
        android.graphics.drawable.AnimatedImageDrawable.nSetAlpha(mState.mNativePtr, alpha);
        invalidateSelf();
    }

    @java.lang.Override
    public int getAlpha() {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called getAlpha on empty AnimatedImageDrawable");
        }
        return android.graphics.drawable.AnimatedImageDrawable.nGetAlpha(mState.mNativePtr);
    }

    @java.lang.Override
    public void setColorFilter(@android.annotation.Nullable
    android.graphics.ColorFilter colorFilter) {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called setColorFilter on empty AnimatedImageDrawable");
        }
        if (colorFilter != mColorFilter) {
            mColorFilter = colorFilter;
            long nativeFilter = (colorFilter == null) ? 0 : colorFilter.getNativeInstance();
            android.graphics.drawable.AnimatedImageDrawable.nSetColorFilter(mState.mNativePtr, nativeFilter);
            invalidateSelf();
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.ColorFilter getColorFilter() {
        return mColorFilter;
    }

    @java.lang.Override
    @android.graphics.PixelFormat.Opacity
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void setAutoMirrored(boolean mirrored) {
        if (mState.mAutoMirrored != mirrored) {
            mState.mAutoMirrored = mirrored;
            if ((getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) && (mState.mNativePtr != 0)) {
                android.graphics.drawable.AnimatedImageDrawable.nSetMirrored(mState.mNativePtr, mirrored);
                invalidateSelf();
            }
        }
    }

    @java.lang.Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        if ((!mState.mAutoMirrored) || (mState.mNativePtr == 0)) {
            return false;
        }
        final boolean mirror = layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL;
        android.graphics.drawable.AnimatedImageDrawable.nSetMirrored(mState.mNativePtr, mirror);
        return true;
    }

    @java.lang.Override
    public final boolean isAutoMirrored() {
        return mState.mAutoMirrored;
    }

    // Animatable overrides
    /**
     * Return whether the animation is currently running.
     *
     *  <p>When this drawable is created, this will return {@code false}. A client
     *  needs to call {@link #start} to start the animation.</p>
     */
    @java.lang.Override
    public boolean isRunning() {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called isRunning on empty AnimatedImageDrawable");
        }
        return android.graphics.drawable.AnimatedImageDrawable.nIsRunning(mState.mNativePtr);
    }

    /**
     * Start the animation.
     *
     *  <p>Does nothing if the animation is already running. If the animation is stopped,
     *  this will reset it.</p>
     *
     *  <p>When the drawable is drawn, starting the animation,
     *  {@link Animatable2.AnimationCallback#onAnimationStart} will be called.</p>
     */
    @java.lang.Override
    public void start() {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called start on empty AnimatedImageDrawable");
        }
        if (android.graphics.drawable.AnimatedImageDrawable.nStart(mState.mNativePtr)) {
            mStarting = true;
            invalidateSelf();
        }
    }

    /**
     * Stop the animation.
     *
     *  <p>If the animation is stopped, it will continue to display the frame
     *  it was displaying when stopped.</p>
     */
    @java.lang.Override
    public void stop() {
        if (mState.mNativePtr == 0) {
            throw new java.lang.IllegalStateException("called stop on empty AnimatedImageDrawable");
        }
        if (android.graphics.drawable.AnimatedImageDrawable.nStop(mState.mNativePtr)) {
            postOnAnimationEnd();
        }
    }

    // Animatable2 overrides
    private java.util.ArrayList<android.graphics.drawable.Animatable2.AnimationCallback> mAnimationCallbacks = null;

    @java.lang.Override
    public void registerAnimationCallback(@android.annotation.NonNull
    android.graphics.drawable.Animatable2.AnimationCallback callback) {
        if (callback == null) {
            return;
        }
        if (mAnimationCallbacks == null) {
            mAnimationCallbacks = new java.util.ArrayList<android.graphics.drawable.Animatable2.AnimationCallback>();
            android.graphics.drawable.AnimatedImageDrawable.nSetOnAnimationEndListener(mState.mNativePtr, this);
        }
        if (!mAnimationCallbacks.contains(callback)) {
            mAnimationCallbacks.add(callback);
        }
    }

    @java.lang.Override
    public boolean unregisterAnimationCallback(@android.annotation.NonNull
    android.graphics.drawable.Animatable2.AnimationCallback callback) {
        if (((callback == null) || (mAnimationCallbacks == null)) || (!mAnimationCallbacks.remove(callback))) {
            return false;
        }
        if (mAnimationCallbacks.isEmpty()) {
            clearAnimationCallbacks();
        }
        return true;
    }

    @java.lang.Override
    public void clearAnimationCallbacks() {
        if (mAnimationCallbacks != null) {
            mAnimationCallbacks = null;
            android.graphics.drawable.AnimatedImageDrawable.nSetOnAnimationEndListener(mState.mNativePtr, null);
        }
    }

    private void postOnAnimationStart() {
        if (mAnimationCallbacks == null) {
            return;
        }
        getHandler().post(() -> {
            for (android.graphics.drawable.Animatable2.AnimationCallback callback : mAnimationCallbacks) {
                callback.onAnimationStart(this);
            }
        });
    }

    private void postOnAnimationEnd() {
        if (mAnimationCallbacks == null) {
            return;
        }
        getHandler().post(() -> {
            for (android.graphics.drawable.Animatable2.AnimationCallback callback : mAnimationCallbacks) {
                callback.onAnimationEnd(this);
            }
        });
    }

    private android.os.Handler getHandler() {
        if (mHandler == null) {
            mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        }
        return mHandler;
    }

    /**
     * Called by JNI.
     *
     *  The JNI code has already posted this to the thread that created the
     *  callback, so no need to post.
     */
    @java.lang.SuppressWarnings("unused")
    @android.annotation.UnsupportedAppUsage
    private void onAnimationEnd() {
        if (mAnimationCallbacks != null) {
            for (android.graphics.drawable.Animatable2.AnimationCallback callback : mAnimationCallbacks) {
                callback.onAnimationEnd(this);
            }
        }
    }

    private static native long nCreate(long nativeImageDecoder, @android.annotation.Nullable
    android.graphics.ImageDecoder decoder, int width, int height, long colorSpaceHandle, boolean extended, android.graphics.Rect cropRect) throws java.io.IOException;

    @dalvik.annotation.optimization.FastNative
    private static native long nGetNativeFinalizer();

    private static native long nDraw(long nativePtr, long canvasNativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetAlpha(long nativePtr, int alpha);

    @dalvik.annotation.optimization.FastNative
    private static native int nGetAlpha(long nativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetColorFilter(long nativePtr, long nativeFilter);

    @dalvik.annotation.optimization.FastNative
    private static native boolean nIsRunning(long nativePtr);

    // Return whether the animation started.
    @dalvik.annotation.optimization.FastNative
    private static native boolean nStart(long nativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native boolean nStop(long nativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native int nGetRepeatCount(long nativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetRepeatCount(long nativePtr, int repeatCount);

    // Pass the drawable down to native so it can call onAnimationEnd.
    private static native void nSetOnAnimationEndListener(long nativePtr, @android.annotation.Nullable
    android.graphics.drawable.AnimatedImageDrawable drawable);

    @dalvik.annotation.optimization.FastNative
    private static native long nNativeByteSize(long nativePtr);

    @dalvik.annotation.optimization.FastNative
    private static native void nSetMirrored(long nativePtr, boolean mirror);
}

