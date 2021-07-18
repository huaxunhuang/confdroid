/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.v17.leanback.widget;


/**
 * A page indicator with dots.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class PagingIndicator extends android.view.View {
    private static final long DURATION_ALPHA = 167;

    private static final long DURATION_DIAMETER = 417;

    private static final long DURATION_TRANSLATION_X = android.support.v17.leanback.widget.PagingIndicator.DURATION_DIAMETER;

    private static final android.animation.TimeInterpolator DECELERATE_INTERPOLATOR = new android.view.animation.DecelerateInterpolator();

    private static final android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float> DOT_ALPHA = new android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float>(java.lang.Float.class, "alpha") {
        @java.lang.Override
        public java.lang.Float get(android.support.v17.leanback.widget.PagingIndicator.Dot dot) {
            return dot.getAlpha();
        }

        @java.lang.Override
        public void set(android.support.v17.leanback.widget.PagingIndicator.Dot dot, java.lang.Float value) {
            dot.setAlpha(value);
        }
    };

    private static final android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float> DOT_DIAMETER = new android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float>(java.lang.Float.class, "diameter") {
        @java.lang.Override
        public java.lang.Float get(android.support.v17.leanback.widget.PagingIndicator.Dot dot) {
            return dot.getDiameter();
        }

        @java.lang.Override
        public void set(android.support.v17.leanback.widget.PagingIndicator.Dot dot, java.lang.Float value) {
            dot.setDiameter(value);
        }
    };

    private static final android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float> DOT_TRANSLATION_X = new android.util.Property<android.support.v17.leanback.widget.PagingIndicator.Dot, java.lang.Float>(java.lang.Float.class, "translation_x") {
        @java.lang.Override
        public java.lang.Float get(android.support.v17.leanback.widget.PagingIndicator.Dot dot) {
            return dot.getTranslationX();
        }

        @java.lang.Override
        public void set(android.support.v17.leanback.widget.PagingIndicator.Dot dot, java.lang.Float value) {
            dot.setTranslationX(value);
        }
    };

    // attribute
    boolean mIsLtr;

    final int mDotDiameter;

    final int mDotRadius;

    private final int mDotGap;

    final int mArrowDiameter;

    final int mArrowRadius;

    private final int mArrowGap;

    private final int mShadowRadius;

    private android.support.v17.leanback.widget.PagingIndicator.Dot[] mDots;

    // X position when the dot is selected.
    private int[] mDotSelectedX;

    // X position when the dot is located to the left of the selected dot.
    private int[] mDotSelectedPrevX;

    // X position when the dot is located to the right of the selected dot.
    private int[] mDotSelectedNextX;

    int mDotCenterY;

    // state
    private int mPageCount;

    private int mCurrentPage;

    private int mPreviousPage;

    // drawing
    @android.support.annotation.ColorInt
    final int mDotFgSelectColor;

    final android.graphics.Paint mBgPaint;

    final android.graphics.Paint mFgPaint;

    private final android.animation.AnimatorSet mShowAnimator;

    private final android.animation.AnimatorSet mHideAnimator;

    private final android.animation.AnimatorSet mAnimator = new android.animation.AnimatorSet();

    android.graphics.Bitmap mArrow;

    final android.graphics.Rect mArrowRect;

    final float mArrowToBgRatio;

    public PagingIndicator(android.content.Context context) {
        this(context, null, 0);
    }

    public PagingIndicator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagingIndicator(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.content.res.Resources res = getResources();
        android.content.res.TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagingIndicator, defStyle, 0);
        mDotRadius = getDimensionFromTypedArray(typedArray, R.styleable.PagingIndicator_lbDotRadius, R.dimen.lb_page_indicator_dot_radius);
        mDotDiameter = mDotRadius * 2;
        mArrowRadius = getDimensionFromTypedArray(typedArray, R.styleable.PagingIndicator_arrowRadius, R.dimen.lb_page_indicator_arrow_radius);
        mArrowDiameter = mArrowRadius * 2;
        mDotGap = getDimensionFromTypedArray(typedArray, R.styleable.PagingIndicator_dotToDotGap, R.dimen.lb_page_indicator_dot_gap);
        mArrowGap = getDimensionFromTypedArray(typedArray, R.styleable.PagingIndicator_dotToArrowGap, R.dimen.lb_page_indicator_arrow_gap);
        int bgColor = getColorFromTypedArray(typedArray, R.styleable.PagingIndicator_dotBgColor, R.color.lb_page_indicator_dot);
        mBgPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(bgColor);
        mDotFgSelectColor = getColorFromTypedArray(typedArray, R.styleable.PagingIndicator_arrowBgColor, R.color.lb_page_indicator_arrow_background);
        typedArray.recycle();
        mIsLtr = res.getConfiguration().getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_LTR;
        int shadowColor = res.getColor(R.color.lb_page_indicator_arrow_shadow);
        mShadowRadius = res.getDimensionPixelSize(R.dimen.lb_page_indicator_arrow_shadow_radius);
        mFgPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        int shadowOffset = res.getDimensionPixelSize(R.dimen.lb_page_indicator_arrow_shadow_offset);
        mFgPaint.setShadowLayer(mShadowRadius, shadowOffset, shadowOffset, shadowColor);
        mArrow = loadArrow();
        mArrowRect = new android.graphics.Rect(0, 0, mArrow.getWidth(), mArrow.getHeight());
        mArrowToBgRatio = ((float) (mArrow.getWidth())) / ((float) (mArrowDiameter));
        // Initialize animations.
        mShowAnimator = new android.animation.AnimatorSet();
        mShowAnimator.playTogether(createDotAlphaAnimator(0.0F, 1.0F), createDotDiameterAnimator(mDotRadius * 2, mArrowRadius * 2), createDotTranslationXAnimator());
        mHideAnimator = new android.animation.AnimatorSet();
        mHideAnimator.playTogether(createDotAlphaAnimator(1.0F, 0.0F), createDotDiameterAnimator(mArrowRadius * 2, mDotRadius * 2), createDotTranslationXAnimator());
        mAnimator.playTogether(mShowAnimator, mHideAnimator);
        // Use software layer to show shadows.
        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null);
    }

    private int getDimensionFromTypedArray(android.content.res.TypedArray typedArray, int attr, int defaultId) {
        return typedArray.getDimensionPixelOffset(attr, getResources().getDimensionPixelOffset(defaultId));
    }

    private int getColorFromTypedArray(android.content.res.TypedArray typedArray, int attr, int defaultId) {
        return typedArray.getColor(attr, getResources().getColor(defaultId));
    }

    private android.graphics.Bitmap loadArrow() {
        android.graphics.Bitmap arrow = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.lb_ic_nav_arrow);
        if (mIsLtr) {
            return arrow;
        } else {
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(-1, 1);
            return android.graphics.Bitmap.createBitmap(arrow, 0, 0, arrow.getWidth(), arrow.getHeight(), matrix, false);
        }
    }

    private android.animation.Animator createDotAlphaAnimator(float from, float to) {
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(null, android.support.v17.leanback.widget.PagingIndicator.DOT_ALPHA, from, to);
        animator.setDuration(android.support.v17.leanback.widget.PagingIndicator.DURATION_ALPHA);
        animator.setInterpolator(android.support.v17.leanback.widget.PagingIndicator.DECELERATE_INTERPOLATOR);
        return animator;
    }

    private android.animation.Animator createDotDiameterAnimator(float from, float to) {
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(null, android.support.v17.leanback.widget.PagingIndicator.DOT_DIAMETER, from, to);
        animator.setDuration(android.support.v17.leanback.widget.PagingIndicator.DURATION_DIAMETER);
        animator.setInterpolator(android.support.v17.leanback.widget.PagingIndicator.DECELERATE_INTERPOLATOR);
        return animator;
    }

    private android.animation.Animator createDotTranslationXAnimator() {
        // The direction is determined in the Dot.
        android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(null, android.support.v17.leanback.widget.PagingIndicator.DOT_TRANSLATION_X, (-mArrowGap) + mDotGap, 0.0F);
        animator.setDuration(android.support.v17.leanback.widget.PagingIndicator.DURATION_TRANSLATION_X);
        animator.setInterpolator(android.support.v17.leanback.widget.PagingIndicator.DECELERATE_INTERPOLATOR);
        return animator;
    }

    /**
     * Sets the page count.
     */
    public void setPageCount(int pages) {
        if (pages <= 0) {
            throw new java.lang.IllegalArgumentException("The page count should be a positive integer");
        }
        mPageCount = pages;
        mDots = new android.support.v17.leanback.widget.PagingIndicator.Dot[mPageCount];
        for (int i = 0; i < mPageCount; ++i) {
            mDots[i] = new android.support.v17.leanback.widget.PagingIndicator.Dot();
        }
        calculateDotPositions();
        setSelectedPage(0);
    }

    /**
     * Called when the page has been selected.
     */
    public void onPageSelected(int pageIndex, boolean withAnimation) {
        if (mCurrentPage == pageIndex) {
            return;
        }
        if (mAnimator.isStarted()) {
            mAnimator.end();
        }
        mPreviousPage = mCurrentPage;
        if (withAnimation) {
            mHideAnimator.setTarget(mDots[mPreviousPage]);
            mShowAnimator.setTarget(mDots[pageIndex]);
            mAnimator.start();
        }
        setSelectedPage(pageIndex);
    }

    private void calculateDotPositions() {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int requiredWidth = getRequiredWidth();
        int mid = (left + right) / 2;
        mDotSelectedX = new int[mPageCount];
        mDotSelectedPrevX = new int[mPageCount];
        mDotSelectedNextX = new int[mPageCount];
        if (mIsLtr) {
            int startLeft = mid - (requiredWidth / 2);
            // mDotSelectedX[0] should be mDotSelectedPrevX[-1] + mArrowGap
            mDotSelectedX[0] = ((startLeft + mDotRadius) - mDotGap) + mArrowGap;
            mDotSelectedPrevX[0] = startLeft + mDotRadius;
            mDotSelectedNextX[0] = ((startLeft + mDotRadius) - (2 * mDotGap)) + (2 * mArrowGap);
            for (int i = 1; i < mPageCount; i++) {
                mDotSelectedX[i] = mDotSelectedPrevX[i - 1] + mArrowGap;
                mDotSelectedPrevX[i] = mDotSelectedPrevX[i - 1] + mDotGap;
                mDotSelectedNextX[i] = mDotSelectedX[i - 1] + mArrowGap;
            }
        } else {
            int startRight = mid + (requiredWidth / 2);
            // mDotSelectedX[0] should be mDotSelectedPrevX[-1] - mArrowGap
            mDotSelectedX[0] = ((startRight - mDotRadius) + mDotGap) - mArrowGap;
            mDotSelectedPrevX[0] = startRight - mDotRadius;
            mDotSelectedNextX[0] = ((startRight - mDotRadius) + (2 * mDotGap)) - (2 * mArrowGap);
            for (int i = 1; i < mPageCount; i++) {
                mDotSelectedX[i] = mDotSelectedPrevX[i - 1] - mArrowGap;
                mDotSelectedPrevX[i] = mDotSelectedPrevX[i - 1] - mDotGap;
                mDotSelectedNextX[i] = mDotSelectedX[i - 1] - mArrowGap;
            }
        }
        mDotCenterY = top + mArrowRadius;
        adjustDotPosition();
    }

    @android.support.annotation.VisibleForTesting
    int getPageCount() {
        return mPageCount;
    }

    @android.support.annotation.VisibleForTesting
    int[] getDotSelectedX() {
        return mDotSelectedX;
    }

    @android.support.annotation.VisibleForTesting
    int[] getDotSelectedLeftX() {
        return mDotSelectedPrevX;
    }

    @android.support.annotation.VisibleForTesting
    int[] getDotSelectedRightX() {
        return mDotSelectedNextX;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getDesiredHeight();
        int height;
        switch (android.view.View.MeasureSpec.getMode(heightMeasureSpec)) {
            case android.view.View.MeasureSpec.EXACTLY :
                height = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                height = java.lang.Math.min(desiredHeight, android.view.View.MeasureSpec.getSize(heightMeasureSpec));
                break;
            case android.view.View.MeasureSpec.UNSPECIFIED :
            default :
                height = desiredHeight;
                break;
        }
        int desiredWidth = getDesiredWidth();
        int width;
        switch (android.view.View.MeasureSpec.getMode(widthMeasureSpec)) {
            case android.view.View.MeasureSpec.EXACTLY :
                width = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                width = java.lang.Math.min(desiredWidth, android.view.View.MeasureSpec.getSize(widthMeasureSpec));
                break;
            case android.view.View.MeasureSpec.UNSPECIFIED :
            default :
                width = desiredWidth;
                break;
        }
        setMeasuredDimension(width, height);
    }

    @java.lang.Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        setMeasuredDimension(width, height);
        calculateDotPositions();
    }

    private int getDesiredHeight() {
        return ((getPaddingTop() + mArrowDiameter) + getPaddingBottom()) + mShadowRadius;
    }

    private int getRequiredWidth() {
        return ((2 * mDotRadius) + (2 * mArrowGap)) + ((mPageCount - 3) * mDotGap);
    }

    private int getDesiredWidth() {
        return (getPaddingLeft() + getRequiredWidth()) + getPaddingRight();
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        for (int i = 0; i < mPageCount; ++i) {
            mDots[i].draw(canvas);
        }
    }

    private void setSelectedPage(int now) {
        if (now == mCurrentPage) {
            return;
        }
        mCurrentPage = now;
        adjustDotPosition();
    }

    private void adjustDotPosition() {
        for (int i = 0; i < mCurrentPage; ++i) {
            mDots[i].deselect();
            mDots[i].mDirection = (i == mPreviousPage) ? android.support.v17.leanback.widget.PagingIndicator.Dot.LEFT : android.support.v17.leanback.widget.PagingIndicator.Dot.RIGHT;
            mDots[i].mCenterX = mDotSelectedPrevX[i];
        }
        mDots[mCurrentPage].select();
        mDots[mCurrentPage].mDirection = (mPreviousPage < mCurrentPage) ? android.support.v17.leanback.widget.PagingIndicator.Dot.LEFT : android.support.v17.leanback.widget.PagingIndicator.Dot.RIGHT;
        mDots[mCurrentPage].mCenterX = mDotSelectedX[mCurrentPage];
        for (int i = mCurrentPage + 1; i < mPageCount; ++i) {
            mDots[i].deselect();
            mDots[i].mDirection = android.support.v17.leanback.widget.PagingIndicator.Dot.RIGHT;
            mDots[i].mCenterX = mDotSelectedNextX[i];
        }
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        boolean isLtr = layoutDirection == android.view.View.LAYOUT_DIRECTION_LTR;
        if (mIsLtr != isLtr) {
            mIsLtr = isLtr;
            mArrow = loadArrow();
            if (mDots != null) {
                for (android.support.v17.leanback.widget.PagingIndicator.Dot dot : mDots) {
                    dot.onRtlPropertiesChanged();
                }
            }
            calculateDotPositions();
            invalidate();
        }
    }

    public class Dot {
        static final float LEFT = -1;

        static final float RIGHT = 1;

        static final float LTR = 1;

        static final float RTL = -1;

        float mAlpha;

        @android.support.annotation.ColorInt
        int mFgColor;

        float mTranslationX;

        float mCenterX;

        float mDiameter;

        float mRadius;

        float mArrowImageRadius;

        float mDirection = android.support.v17.leanback.widget.PagingIndicator.Dot.RIGHT;

        float mLayoutDirection = (mIsLtr) ? android.support.v17.leanback.widget.PagingIndicator.Dot.LTR : android.support.v17.leanback.widget.PagingIndicator.Dot.RTL;

        void select() {
            mTranslationX = 0.0F;
            mCenterX = 0.0F;
            mDiameter = mArrowDiameter;
            mRadius = mArrowRadius;
            mArrowImageRadius = mRadius * mArrowToBgRatio;
            mAlpha = 1.0F;
            adjustAlpha();
        }

        void deselect() {
            mTranslationX = 0.0F;
            mCenterX = 0.0F;
            mDiameter = mDotDiameter;
            mRadius = mDotRadius;
            mArrowImageRadius = mRadius * mArrowToBgRatio;
            mAlpha = 0.0F;
            adjustAlpha();
        }

        public void adjustAlpha() {
            int alpha = java.lang.Math.round(0xff * mAlpha);
            int red = android.graphics.Color.red(mDotFgSelectColor);
            int green = android.graphics.Color.green(mDotFgSelectColor);
            int blue = android.graphics.Color.blue(mDotFgSelectColor);
            mFgColor = android.graphics.Color.argb(alpha, red, green, blue);
        }

        public float getAlpha() {
            return mAlpha;
        }

        public void setAlpha(float alpha) {
            this.mAlpha = alpha;
            adjustAlpha();
            invalidate();
        }

        public float getTranslationX() {
            return mTranslationX;
        }

        public void setTranslationX(float translationX) {
            this.mTranslationX = (translationX * mDirection) * mLayoutDirection;
            invalidate();
        }

        public float getDiameter() {
            return mDiameter;
        }

        public void setDiameter(float diameter) {
            this.mDiameter = diameter;
            this.mRadius = diameter / 2;
            this.mArrowImageRadius = (diameter / 2) * mArrowToBgRatio;
            invalidate();
        }

        void draw(android.graphics.Canvas canvas) {
            float centerX = mCenterX + mTranslationX;
            canvas.drawCircle(centerX, mDotCenterY, mRadius, mBgPaint);
            if (mAlpha > 0) {
                mFgPaint.setColor(mFgColor);
                canvas.drawCircle(centerX, mDotCenterY, mRadius, mFgPaint);
                canvas.drawBitmap(mArrow, mArrowRect, new android.graphics.Rect(((int) (centerX - mArrowImageRadius)), ((int) (mDotCenterY - mArrowImageRadius)), ((int) (centerX + mArrowImageRadius)), ((int) (mDotCenterY + mArrowImageRadius))), null);
            }
        }

        void onRtlPropertiesChanged() {
            mLayoutDirection = (mIsLtr) ? android.support.v17.leanback.widget.PagingIndicator.Dot.LTR : android.support.v17.leanback.widget.PagingIndicator.Dot.RTL;
        }
    }
}

