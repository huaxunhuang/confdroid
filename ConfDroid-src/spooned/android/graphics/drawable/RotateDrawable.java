/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * <p>
 * A Drawable that can rotate another Drawable based on the current level value.
 * The start and end angles of rotation can be controlled to map any circular
 * arc to the level values range.
 * <p>
 * It can be defined in an XML file with the <code>&lt;rotate&gt;</code> element.
 * For more information, see the guide to
 * <a href="{@docRoot }guide/topics/resources/animation-resource.html">Animation Resources</a>.
 *
 * @unknown ref android.R.styleable#RotateDrawable_visible
 * @unknown ref android.R.styleable#RotateDrawable_fromDegrees
 * @unknown ref android.R.styleable#RotateDrawable_toDegrees
 * @unknown ref android.R.styleable#RotateDrawable_pivotX
 * @unknown ref android.R.styleable#RotateDrawable_pivotY
 * @unknown ref android.R.styleable#RotateDrawable_drawable
 */
public class RotateDrawable extends android.graphics.drawable.DrawableWrapper {
    private static final int MAX_LEVEL = 10000;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.RotateDrawable.RotateState mState;

    /**
     * Creates a new rotating drawable with no wrapped drawable.
     */
    public RotateDrawable() {
        this(new android.graphics.drawable.RotateDrawable.RotateState(null, null), null);
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.RotateDrawable);
        // Inflation will advance the XmlPullParser and AttributeSet.
        super.inflate(r, parser, attrs, theme);
        updateStateFromTypedArray(a);
        verifyRequiredAttributes(a);
        a.recycle();
    }

    @java.lang.Override
    public void applyTheme(@android.annotation.NonNull
    android.content.res.Resources.Theme t) {
        super.applyTheme(t);
        final android.graphics.drawable.RotateDrawable.RotateState state = mState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.RotateDrawable);
            try {
                updateStateFromTypedArray(a);
                verifyRequiredAttributes(a);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.graphics.drawable.Drawable.rethrowAsRuntimeException(e);
            } finally {
                a.recycle();
            }
        }
    }

    private void verifyRequiredAttributes(@android.annotation.NonNull
    android.content.res.TypedArray a) throws org.xmlpull.v1.XmlPullParserException {
        // If we're not waiting on a theme, verify required attributes.
        if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[R.styleable.RotateDrawable_drawable] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException((a.getPositionDescription() + ": <rotate> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
        }
    }

    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.RotateDrawable.RotateState state = mState;
        if (state == null) {
            return;
        }
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        if (a.hasValue(R.styleable.RotateDrawable_pivotX)) {
            final android.util.TypedValue tv = a.peekValue(R.styleable.RotateDrawable_pivotX);
            state.mPivotXRel = tv.type == android.util.TypedValue.TYPE_FRACTION;
            state.mPivotX = (state.mPivotXRel) ? tv.getFraction(1.0F, 1.0F) : tv.getFloat();
        }
        if (a.hasValue(R.styleable.RotateDrawable_pivotY)) {
            final android.util.TypedValue tv = a.peekValue(R.styleable.RotateDrawable_pivotY);
            state.mPivotYRel = tv.type == android.util.TypedValue.TYPE_FRACTION;
            state.mPivotY = (state.mPivotYRel) ? tv.getFraction(1.0F, 1.0F) : tv.getFloat();
        }
        state.mFromDegrees = a.getFloat(R.styleable.RotateDrawable_fromDegrees, state.mFromDegrees);
        state.mToDegrees = a.getFloat(R.styleable.RotateDrawable_toDegrees, state.mToDegrees);
        state.mCurrentDegrees = state.mFromDegrees;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        final android.graphics.drawable.Drawable d = getDrawable();
        final android.graphics.Rect bounds = d.getBounds();
        final int w = bounds.right - bounds.left;
        final int h = bounds.bottom - bounds.top;
        final android.graphics.drawable.RotateDrawable.RotateState st = mState;
        final float px = (st.mPivotXRel) ? w * st.mPivotX : st.mPivotX;
        final float py = (st.mPivotYRel) ? h * st.mPivotY : st.mPivotY;
        final int saveCount = canvas.save();
        canvas.rotate(st.mCurrentDegrees, px + bounds.left, py + bounds.top);
        d.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    /**
     * Sets the start angle for rotation.
     *
     * @param fromDegrees
     * 		starting angle in degrees
     * @see #getFromDegrees()
     * @unknown ref android.R.styleable#RotateDrawable_fromDegrees
     */
    public void setFromDegrees(float fromDegrees) {
        if (mState.mFromDegrees != fromDegrees) {
            mState.mFromDegrees = fromDegrees;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return starting angle for rotation in degrees
     * @see #setFromDegrees(float)
     * @unknown ref android.R.styleable#RotateDrawable_fromDegrees
     */
    public float getFromDegrees() {
        return mState.mFromDegrees;
    }

    /**
     * Sets the end angle for rotation.
     *
     * @param toDegrees
     * 		ending angle in degrees
     * @see #getToDegrees()
     * @unknown ref android.R.styleable#RotateDrawable_toDegrees
     */
    public void setToDegrees(float toDegrees) {
        if (mState.mToDegrees != toDegrees) {
            mState.mToDegrees = toDegrees;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return ending angle for rotation in degrees
     * @see #setToDegrees(float)
     * @unknown ref android.R.styleable#RotateDrawable_toDegrees
     */
    public float getToDegrees() {
        return mState.mToDegrees;
    }

    /**
     * Sets the X position around which the drawable is rotated.
     * <p>
     * If the X pivot is relative (as specified by
     * {@link #setPivotXRelative(boolean)}), then the position represents a
     * fraction of the drawable width. Otherwise, the position represents an
     * absolute value in pixels.
     *
     * @param pivotX
     * 		X position around which to rotate
     * @see #setPivotXRelative(boolean)
     * @unknown ref android.R.styleable#RotateDrawable_pivotX
     */
    public void setPivotX(float pivotX) {
        if (mState.mPivotX != pivotX) {
            mState.mPivotX = pivotX;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return X position around which to rotate
     * @see #setPivotX(float)
     * @unknown ref android.R.styleable#RotateDrawable_pivotX
     */
    public float getPivotX() {
        return mState.mPivotX;
    }

    /**
     * Sets whether the X pivot value represents a fraction of the drawable
     * width or an absolute value in pixels.
     *
     * @param relative
     * 		true if the X pivot represents a fraction of the drawable
     * 		width, or false if it represents an absolute value in pixels
     * @see #isPivotXRelative()
     */
    public void setPivotXRelative(boolean relative) {
        if (mState.mPivotXRel != relative) {
            mState.mPivotXRel = relative;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return true if the X pivot represents a fraction of the drawable width,
    or false if it represents an absolute value in pixels
     * @see #setPivotXRelative(boolean)
     */
    public boolean isPivotXRelative() {
        return mState.mPivotXRel;
    }

    /**
     * Sets the Y position around which the drawable is rotated.
     * <p>
     * If the Y pivot is relative (as specified by
     * {@link #setPivotYRelative(boolean)}), then the position represents a
     * fraction of the drawable height. Otherwise, the position represents an
     * absolute value in pixels.
     *
     * @param pivotY
     * 		Y position around which to rotate
     * @see #getPivotY()
     * @unknown ref android.R.styleable#RotateDrawable_pivotY
     */
    public void setPivotY(float pivotY) {
        if (mState.mPivotY != pivotY) {
            mState.mPivotY = pivotY;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return Y position around which to rotate
     * @see #setPivotY(float)
     * @unknown ref android.R.styleable#RotateDrawable_pivotY
     */
    public float getPivotY() {
        return mState.mPivotY;
    }

    /**
     * Sets whether the Y pivot value represents a fraction of the drawable
     * height or an absolute value in pixels.
     *
     * @param relative
     * 		True if the Y pivot represents a fraction of the drawable
     * 		height, or false if it represents an absolute value in pixels
     * @see #isPivotYRelative()
     */
    public void setPivotYRelative(boolean relative) {
        if (mState.mPivotYRel != relative) {
            mState.mPivotYRel = relative;
            invalidateSelf();
        }
    }

    /**
     *
     *
     * @return true if the Y pivot represents a fraction of the drawable height,
    or false if it represents an absolute value in pixels
     * @see #setPivotYRelative(boolean)
     */
    public boolean isPivotYRelative() {
        return mState.mPivotYRel;
    }

    @java.lang.Override
    protected boolean onLevelChange(int level) {
        super.onLevelChange(level);
        final float value = level / ((float) (android.graphics.drawable.RotateDrawable.MAX_LEVEL));
        final float degrees = android.util.MathUtils.lerp(mState.mFromDegrees, mState.mToDegrees, value);
        mState.mCurrentDegrees = degrees;
        invalidateSelf();
        return true;
    }

    @java.lang.Override
    android.graphics.drawable.DrawableWrapper.DrawableWrapperState mutateConstantState() {
        mState = new android.graphics.drawable.RotateDrawable.RotateState(mState, null);
        return mState;
    }

    static final class RotateState extends android.graphics.drawable.DrawableWrapper.DrawableWrapperState {
        private int[] mThemeAttrs;

        boolean mPivotXRel = true;

        float mPivotX = 0.5F;

        boolean mPivotYRel = true;

        float mPivotY = 0.5F;

        float mFromDegrees = 0.0F;

        float mToDegrees = 360.0F;

        float mCurrentDegrees = 0.0F;

        RotateState(android.graphics.drawable.RotateDrawable.RotateState orig, android.content.res.Resources res) {
            super(orig, res);
            if (orig != null) {
                mPivotXRel = orig.mPivotXRel;
                mPivotX = orig.mPivotX;
                mPivotYRel = orig.mPivotYRel;
                mPivotY = orig.mPivotY;
                mFromDegrees = orig.mFromDegrees;
                mToDegrees = orig.mToDegrees;
                mCurrentDegrees = orig.mCurrentDegrees;
            }
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) {
            return new android.graphics.drawable.RotateDrawable(this, res);
        }
    }

    private RotateDrawable(android.graphics.drawable.RotateDrawable.RotateState state, android.content.res.Resources res) {
        super(state, res);
        mState = state;
    }
}

