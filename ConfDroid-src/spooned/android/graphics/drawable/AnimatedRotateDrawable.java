/**
 * Copyright (C) 2009 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public class AnimatedRotateDrawable extends android.graphics.drawable.DrawableWrapper implements android.graphics.drawable.Animatable {
    private android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState mState;

    private float mCurrentDegrees;

    private float mIncrement;

    /**
     * Whether this drawable is currently animating.
     */
    private boolean mRunning;

    /**
     * Creates a new animated rotating drawable with no wrapped drawable.
     */
    public AnimatedRotateDrawable() {
        this(new android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState(null, null), null);
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.drawable.Drawable drawable = getDrawable();
        final android.graphics.Rect bounds = drawable.getBounds();
        final int w = bounds.right - bounds.left;
        final int h = bounds.bottom - bounds.top;
        final android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState st = mState;
        final float px = (st.mPivotXRel) ? w * st.mPivotX : st.mPivotX;
        final float py = (st.mPivotYRel) ? h * st.mPivotY : st.mPivotY;
        final int saveCount = canvas.save();
        canvas.rotate(mCurrentDegrees, px + bounds.left, py + bounds.top);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    /**
     * Starts the rotation animation.
     * <p>
     * The animation will run until {@link #stop()} is called. Calling this
     * method while the animation is already running has no effect.
     *
     * @see #stop()
     */
    @java.lang.Override
    public void start() {
        if (!mRunning) {
            mRunning = true;
            nextFrame();
        }
    }

    /**
     * Stops the rotation animation.
     *
     * @see #start()
     */
    @java.lang.Override
    public void stop() {
        mRunning = false;
        unscheduleSelf(mNextFrame);
    }

    @java.lang.Override
    public boolean isRunning() {
        return mRunning;
    }

    private void nextFrame() {
        unscheduleSelf(mNextFrame);
        scheduleSelf(mNextFrame, android.os.SystemClock.uptimeMillis() + mState.mFrameDuration);
    }

    @java.lang.Override
    public boolean setVisible(boolean visible, boolean restart) {
        final boolean changed = super.setVisible(visible, restart);
        if (visible) {
            if (changed || restart) {
                mCurrentDegrees = 0.0F;
                nextFrame();
            }
        } else {
            unscheduleSelf(mNextFrame);
        }
        return changed;
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedRotateDrawable);
        // Inflation will advance the XmlPullParser and AttributeSet.
        super.inflate(r, parser, attrs, theme);
        updateStateFromTypedArray(a);
        verifyRequiredAttributes(a);
        a.recycle();
        updateLocalState();
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState state = mState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.AnimatedRotateDrawable);
            try {
                updateStateFromTypedArray(a);
                verifyRequiredAttributes(a);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.graphics.drawable.Drawable.rethrowAsRuntimeException(e);
            } finally {
                a.recycle();
            }
        }
        updateLocalState();
    }

    private void verifyRequiredAttributes(@android.annotation.NonNull
    android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        // If we're not waiting on a theme, verify required attributes.
        if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[R.styleable.AnimatedRotateDrawable_drawable] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException((a.getPositionDescription() + ": <animated-rotate> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
        }
    }

    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState state = mState;
        if (state == null) {
            return;
        }
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        if (a.hasValue(R.styleable.AnimatedRotateDrawable_pivotX)) {
            final android.util.TypedValue tv = a.peekValue(R.styleable.AnimatedRotateDrawable_pivotX);
            state.mPivotXRel = tv.type == android.util.TypedValue.TYPE_FRACTION;
            state.mPivotX = (state.mPivotXRel) ? tv.getFraction(1.0F, 1.0F) : tv.getFloat();
        }
        if (a.hasValue(R.styleable.AnimatedRotateDrawable_pivotY)) {
            final android.util.TypedValue tv = a.peekValue(R.styleable.AnimatedRotateDrawable_pivotY);
            state.mPivotYRel = tv.type == android.util.TypedValue.TYPE_FRACTION;
            state.mPivotY = (state.mPivotYRel) ? tv.getFraction(1.0F, 1.0F) : tv.getFloat();
        }
        setFramesCount(a.getInt(R.styleable.AnimatedRotateDrawable_framesCount, state.mFramesCount));
        setFramesDuration(a.getInt(R.styleable.AnimatedRotateDrawable_frameDuration, state.mFrameDuration));
    }

    @android.annotation.UnsupportedAppUsage
    public void setFramesCount(int framesCount) {
        mState.mFramesCount = framesCount;
        mIncrement = 360.0F / mState.mFramesCount;
    }

    @android.annotation.UnsupportedAppUsage
    public void setFramesDuration(int framesDuration) {
        mState.mFrameDuration = framesDuration;
    }

    @java.lang.Override
    android.graphics.drawable.DrawableWrapper.DrawableWrapperState mutateConstantState() {
        mState = new android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState(mState, null);
        return mState;
    }

    static final class AnimatedRotateState extends android.graphics.drawable.DrawableWrapper.DrawableWrapperState {
        private int[] mThemeAttrs;

        boolean mPivotXRel = false;

        float mPivotX = 0;

        boolean mPivotYRel = false;

        float mPivotY = 0;

        int mFrameDuration = 150;

        int mFramesCount = 12;

        public AnimatedRotateState(android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState orig, android.content.res.Resources res) {
            super(orig, res);
            if (orig != null) {
                mPivotXRel = orig.mPivotXRel;
                mPivotX = orig.mPivotX;
                mPivotYRel = orig.mPivotYRel;
                mPivotY = orig.mPivotY;
                mFramesCount = orig.mFramesCount;
                mFrameDuration = orig.mFrameDuration;
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.AnimatedRotateDrawable(this, res);
        }
    }

    private AnimatedRotateDrawable(android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState state, android.content.res.Resources res) {
        super(state, res);
        mState = state;
        updateLocalState();
    }

    private void updateLocalState() {
        final android.graphics.drawable.AnimatedRotateDrawable.AnimatedRotateState state = mState;
        mIncrement = 360.0F / state.mFramesCount;
        // Force the wrapped drawable to use filtering and AA, if applicable,
        // so that it looks smooth when rotated.
        final android.graphics.drawable.Drawable drawable = getDrawable();
        if (drawable != null) {
            drawable.setFilterBitmap(true);
            if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
                ((android.graphics.drawable.BitmapDrawable) (drawable)).setAntiAlias(true);
            }
        }
    }

    private final java.lang.Runnable mNextFrame = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            // TODO: This should be computed in draw(Canvas), based on the amount
            // of time since the last frame drawn
            mCurrentDegrees += mIncrement;
            if (mCurrentDegrees > (360.0F - mIncrement)) {
                mCurrentDegrees = 0.0F;
            }
            invalidateSelf();
            nextFrame();
        }
    };
}

