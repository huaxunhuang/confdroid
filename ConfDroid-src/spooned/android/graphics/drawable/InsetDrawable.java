/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * A Drawable that insets another Drawable by a specified distance or fraction of the content bounds.
 * This is used when a View needs a background that is smaller than
 * the View's actual bounds.
 *
 * <p>It can be defined in an XML file with the <code>&lt;inset></code> element. For more
 * information, see the guide to <a
 * href="{@docRoot }guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @unknown ref android.R.styleable#InsetDrawable_visible
 * @unknown ref android.R.styleable#InsetDrawable_drawable
 * @unknown ref android.R.styleable#InsetDrawable_insetLeft
 * @unknown ref android.R.styleable#InsetDrawable_insetRight
 * @unknown ref android.R.styleable#InsetDrawable_insetTop
 * @unknown ref android.R.styleable#InsetDrawable_insetBottom
 */
public class InsetDrawable extends android.graphics.drawable.DrawableWrapper {
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    private final android.graphics.Rect mTmpInsetRect = new android.graphics.Rect();

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.InsetDrawable.InsetState mState;

    /**
     * No-arg constructor used by drawable inflation.
     */
    InsetDrawable() {
        this(new android.graphics.drawable.InsetDrawable.InsetState(null, null), null);
    }

    /**
     * Creates a new inset drawable with the specified inset.
     *
     * @param drawable
     * 		The drawable to inset.
     * @param inset
     * 		Inset in pixels around the drawable.
     */
    public InsetDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable, int inset) {
        this(drawable, inset, inset, inset, inset);
    }

    /**
     * Creates a new inset drawable with the specified inset.
     *
     * @param drawable
     * 		The drawable to inset.
     * @param inset
     * 		Inset in fraction (range: [0, 1)) of the inset content bounds.
     */
    public InsetDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable, float inset) {
        this(drawable, inset, inset, inset, inset);
    }

    /**
     * Creates a new inset drawable with the specified insets in pixels.
     *
     * @param drawable
     * 		The drawable to inset.
     * @param insetLeft
     * 		Left inset in pixels.
     * @param insetTop
     * 		Top inset in pixels.
     * @param insetRight
     * 		Right inset in pixels.
     * @param insetBottom
     * 		Bottom inset in pixels.
     */
    public InsetDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable, int insetLeft, int insetTop, int insetRight, int insetBottom) {
        this(new android.graphics.drawable.InsetDrawable.InsetState(null, null), null);
        mState.mInsetLeft = new android.graphics.drawable.InsetDrawable.InsetValue(0.0F, insetLeft);
        mState.mInsetTop = new android.graphics.drawable.InsetDrawable.InsetValue(0.0F, insetTop);
        mState.mInsetRight = new android.graphics.drawable.InsetDrawable.InsetValue(0.0F, insetRight);
        mState.mInsetBottom = new android.graphics.drawable.InsetDrawable.InsetValue(0.0F, insetBottom);
        setDrawable(drawable);
    }

    /**
     * Creates a new inset drawable with the specified insets in fraction of the view bounds.
     *
     * @param drawable
     * 		The drawable to inset.
     * @param insetLeftFraction
     * 		Left inset in fraction (range: [0, 1)) of the inset content bounds.
     * @param insetTopFraction
     * 		Top inset in fraction (range: [0, 1)) of the inset content bounds.
     * @param insetRightFraction
     * 		Right inset in fraction (range: [0, 1)) of the inset content bounds.
     * @param insetBottomFraction
     * 		Bottom inset in fraction (range: [0, 1)) of the inset content bounds.
     */
    public InsetDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable drawable, float insetLeftFraction, float insetTopFraction, float insetRightFraction, float insetBottomFraction) {
        this(new android.graphics.drawable.InsetDrawable.InsetState(null, null), null);
        mState.mInsetLeft = new android.graphics.drawable.InsetDrawable.InsetValue(insetLeftFraction, 0);
        mState.mInsetTop = new android.graphics.drawable.InsetDrawable.InsetValue(insetTopFraction, 0);
        mState.mInsetRight = new android.graphics.drawable.InsetDrawable.InsetValue(insetRightFraction, 0);
        mState.mInsetBottom = new android.graphics.drawable.InsetDrawable.InsetValue(insetBottomFraction, 0);
        setDrawable(drawable);
    }

    @java.lang.Override
    public void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.graphics.drawable.Drawable.obtainAttributes(r, theme, attrs, R.styleable.InsetDrawable);
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
        final android.graphics.drawable.InsetDrawable.InsetState state = mState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            final android.content.res.TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.InsetDrawable);
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
        if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[R.styleable.InsetDrawable_drawable] == 0))) {
            throw new org.xmlpull.v1.XmlPullParserException((a.getPositionDescription() + ": <inset> tag requires a 'drawable' attribute or ") + "child tag defining a drawable");
        }
    }

    private void updateStateFromTypedArray(@android.annotation.NonNull
    android.content.res.TypedArray a) {
        final android.graphics.drawable.InsetDrawable.InsetState state = mState;
        if (state == null) {
            return;
        }
        // Account for any configuration changes.
        state.mChangingConfigurations |= a.getChangingConfigurations();
        // Extract the theme attributes, if any.
        state.mThemeAttrs = a.extractThemeAttrs();
        // Inset attribute may be overridden by more specific attributes.
        if (a.hasValue(R.styleable.InsetDrawable_inset)) {
            final android.graphics.drawable.InsetDrawable.InsetValue inset = getInset(a, R.styleable.InsetDrawable_inset, new android.graphics.drawable.InsetDrawable.InsetValue());
            state.mInsetLeft = inset;
            state.mInsetTop = inset;
            state.mInsetRight = inset;
            state.mInsetBottom = inset;
        }
        state.mInsetLeft = getInset(a, R.styleable.InsetDrawable_insetLeft, state.mInsetLeft);
        state.mInsetTop = getInset(a, R.styleable.InsetDrawable_insetTop, state.mInsetTop);
        state.mInsetRight = getInset(a, R.styleable.InsetDrawable_insetRight, state.mInsetRight);
        state.mInsetBottom = getInset(a, R.styleable.InsetDrawable_insetBottom, state.mInsetBottom);
    }

    private android.graphics.drawable.InsetDrawable.InsetValue getInset(@android.annotation.NonNull
    android.content.res.TypedArray a, int index, android.graphics.drawable.InsetDrawable.InsetValue defaultValue) {
        if (a.hasValue(index)) {
            android.util.TypedValue tv = a.peekValue(index);
            if (tv.type == android.util.TypedValue.TYPE_FRACTION) {
                float f = tv.getFraction(1.0F, 1.0F);
                if (f >= 1.0F) {
                    throw new java.lang.IllegalStateException("Fraction cannot be larger than 1");
                }
                return new android.graphics.drawable.InsetDrawable.InsetValue(f, 0);
            } else {
                int dimension = a.getDimensionPixelOffset(index, 0);
                if (dimension != 0) {
                    return new android.graphics.drawable.InsetDrawable.InsetValue(0, dimension);
                }
            }
        }
        return defaultValue;
    }

    private void getInsets(android.graphics.Rect out) {
        final android.graphics.Rect b = getBounds();
        out.left = mState.mInsetLeft.getDimension(b.width());
        out.right = mState.mInsetRight.getDimension(b.width());
        out.top = mState.mInsetTop.getDimension(b.height());
        out.bottom = mState.mInsetBottom.getDimension(b.height());
    }

    @java.lang.Override
    public boolean getPadding(android.graphics.Rect padding) {
        final boolean pad = super.getPadding(padding);
        getInsets(mTmpInsetRect);
        padding.left += mTmpInsetRect.left;
        padding.right += mTmpInsetRect.right;
        padding.top += mTmpInsetRect.top;
        padding.bottom += mTmpInsetRect.bottom;
        return pad || ((((mTmpInsetRect.left | mTmpInsetRect.right) | mTmpInsetRect.top) | mTmpInsetRect.bottom) != 0);
    }

    @java.lang.Override
    public android.graphics.Insets getOpticalInsets() {
        final android.graphics.Insets contentInsets = super.getOpticalInsets();
        getInsets(mTmpInsetRect);
        return android.graphics.Insets.of(contentInsets.left + mTmpInsetRect.left, contentInsets.top + mTmpInsetRect.top, contentInsets.right + mTmpInsetRect.right, contentInsets.bottom + mTmpInsetRect.bottom);
    }

    @java.lang.Override
    public int getOpacity() {
        final android.graphics.drawable.InsetDrawable.InsetState state = mState;
        final int opacity = getDrawable().getOpacity();
        getInsets(mTmpInsetRect);
        if ((opacity == android.graphics.PixelFormat.OPAQUE) && ((((mTmpInsetRect.left > 0) || (mTmpInsetRect.top > 0)) || (mTmpInsetRect.right > 0)) || (mTmpInsetRect.bottom > 0))) {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }
        return opacity;
    }

    @java.lang.Override
    protected void onBoundsChange(android.graphics.Rect bounds) {
        final android.graphics.Rect r = mTmpRect;
        r.set(bounds);
        r.left += mState.mInsetLeft.getDimension(bounds.width());
        r.top += mState.mInsetTop.getDimension(bounds.height());
        r.right -= mState.mInsetRight.getDimension(bounds.width());
        r.bottom -= mState.mInsetBottom.getDimension(bounds.height());
        // Apply inset bounds to the wrapped drawable.
        super.onBoundsChange(r);
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        final int childWidth = getDrawable().getIntrinsicWidth();
        final float fraction = mState.mInsetLeft.mFraction + mState.mInsetRight.mFraction;
        if ((childWidth < 0) || (fraction >= 1)) {
            return -1;
        }
        return (((int) (childWidth / (1 - fraction))) + mState.mInsetLeft.mDimension) + mState.mInsetRight.mDimension;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        final int childHeight = getDrawable().getIntrinsicHeight();
        final float fraction = mState.mInsetTop.mFraction + mState.mInsetBottom.mFraction;
        if ((childHeight < 0) || (fraction >= 1)) {
            return -1;
        }
        return (((int) (childHeight / (1 - fraction))) + mState.mInsetTop.mDimension) + mState.mInsetBottom.mDimension;
    }

    @java.lang.Override
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
        getDrawable().getOutline(outline);
    }

    @java.lang.Override
    android.graphics.drawable.DrawableWrapper.DrawableWrapperState mutateConstantState() {
        mState = new android.graphics.drawable.InsetDrawable.InsetState(mState, null);
        return mState;
    }

    static final class InsetState extends android.graphics.drawable.DrawableWrapper.DrawableWrapperState {
        private int[] mThemeAttrs;

        android.graphics.drawable.InsetDrawable.InsetValue mInsetLeft;

        android.graphics.drawable.InsetDrawable.InsetValue mInsetTop;

        android.graphics.drawable.InsetDrawable.InsetValue mInsetRight;

        android.graphics.drawable.InsetDrawable.InsetValue mInsetBottom;

        InsetState(@android.annotation.Nullable
        android.graphics.drawable.InsetDrawable.InsetState orig, @android.annotation.Nullable
        android.content.res.Resources res) {
            super(orig, res);
            if (orig != null) {
                mInsetLeft = orig.mInsetLeft.clone();
                mInsetTop = orig.mInsetTop.clone();
                mInsetRight = orig.mInsetRight.clone();
                mInsetBottom = orig.mInsetBottom.clone();
                if (orig.mDensity != mDensity) {
                    applyDensityScaling(orig.mDensity, mDensity);
                }
            } else {
                mInsetLeft = new android.graphics.drawable.InsetDrawable.InsetValue();
                mInsetTop = new android.graphics.drawable.InsetDrawable.InsetValue();
                mInsetRight = new android.graphics.drawable.InsetDrawable.InsetValue();
                mInsetBottom = new android.graphics.drawable.InsetDrawable.InsetValue();
            }
        }

        @java.lang.Override
        void onDensityChanged(int sourceDensity, int targetDensity) {
            super.onDensityChanged(sourceDensity, targetDensity);
            applyDensityScaling(sourceDensity, targetDensity);
        }

        /**
         * Called when the constant state density changes to scale
         * density-dependent properties specific to insets.
         *
         * @param sourceDensity
         * 		the previous constant state density
         * @param targetDensity
         * 		the new constant state density
         */
        private void applyDensityScaling(int sourceDensity, int targetDensity) {
            mInsetLeft.scaleFromDensity(sourceDensity, targetDensity);
            mInsetTop.scaleFromDensity(sourceDensity, targetDensity);
            mInsetRight.scaleFromDensity(sourceDensity, targetDensity);
            mInsetBottom.scaleFromDensity(sourceDensity, targetDensity);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable newDrawable(@android.annotation.Nullable
        android.content.res.Resources res) {
            // If this drawable is being created for a different density,
            // just create a new constant state and call it a day.
            final android.graphics.drawable.InsetDrawable.InsetState state;
            if (res != null) {
                final int densityDpi = res.getDisplayMetrics().densityDpi;
                final int density = (densityDpi == 0) ? android.util.DisplayMetrics.DENSITY_DEFAULT : densityDpi;
                if (density != mDensity) {
                    state = new android.graphics.drawable.InsetDrawable.InsetState(this, res);
                } else {
                    state = this;
                }
            } else {
                state = this;
            }
            return new android.graphics.drawable.InsetDrawable(state, res);
        }
    }

    static final class InsetValue implements java.lang.Cloneable {
        final float mFraction;

        int mDimension;

        public InsetValue() {
            this(0.0F, 0);
        }

        public InsetValue(float fraction, int dimension) {
            mFraction = fraction;
            mDimension = dimension;
        }

        int getDimension(int boundSize) {
            return ((int) (boundSize * mFraction)) + mDimension;
        }

        void scaleFromDensity(int sourceDensity, int targetDensity) {
            if (mDimension != 0) {
                mDimension = android.graphics.Bitmap.scaleFromDensity(mDimension, sourceDensity, targetDensity);
            }
        }

        @java.lang.Override
        public android.graphics.drawable.InsetDrawable.InsetValue clone() {
            return new android.graphics.drawable.InsetDrawable.InsetValue(mFraction, mDimension);
        }
    }

    /**
     * The one constructor to rule them all. This is called by all public
     * constructors to set the state and initialize local properties.
     */
    private InsetDrawable(@android.annotation.NonNull
    android.graphics.drawable.InsetDrawable.InsetState state, @android.annotation.Nullable
    android.content.res.Resources res) {
        super(state, res);
        mState = state;
    }
}

