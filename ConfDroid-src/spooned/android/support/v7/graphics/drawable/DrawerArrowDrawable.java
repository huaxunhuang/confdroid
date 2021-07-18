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
package android.support.v7.graphics.drawable;


/**
 * A drawable that can draw a "Drawer hamburger" menu or an arrow and animate between them.
 * <p>
 * The progress between the two states is controlled via {@link #setProgress(float)}.
 * </p>
 */
public class DrawerArrowDrawable extends android.graphics.drawable.Drawable {
    /**
     * Direction to make the arrow point towards the left.
     *
     * @see #setDirection(int)
     * @see #getDirection()
     */
    public static final int ARROW_DIRECTION_LEFT = 0;

    /**
     * Direction to make the arrow point towards the right.
     *
     * @see #setDirection(int)
     * @see #getDirection()
     */
    public static final int ARROW_DIRECTION_RIGHT = 1;

    /**
     * Direction to make the arrow point towards the start.
     *
     * <p>When used in a view with a {@link ViewCompat#LAYOUT_DIRECTION_RTL RTL} layout direction,
     * this is the same as {@link #ARROW_DIRECTION_RIGHT}, otherwise it is the same as
     * {@link #ARROW_DIRECTION_LEFT}.</p>
     *
     * @see #setDirection(int)
     * @see #getDirection()
     */
    public static final int ARROW_DIRECTION_START = 2;

    /**
     * Direction to make the arrow point to the end.
     *
     * <p>When used in a view with a {@link ViewCompat#LAYOUT_DIRECTION_RTL RTL} layout direction,
     * this is the same as {@link #ARROW_DIRECTION_LEFT}, otherwise it is the same as
     * {@link #ARROW_DIRECTION_RIGHT}.</p>
     *
     * @see #setDirection(int)
     * @see #getDirection()
     */
    public static final int ARROW_DIRECTION_END = 3;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_LEFT, android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_RIGHT, android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_START, android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_END })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ArrowDirection {}

    private final android.graphics.Paint mPaint = new android.graphics.Paint();

    // The angle in degrees that the arrow head is inclined at.
    private static final float ARROW_HEAD_ANGLE = ((float) (java.lang.Math.toRadians(45)));

    // The length of top and bottom bars when they merge into an arrow
    private float mArrowHeadLength;

    // The length of middle bar
    private float mBarLength;

    // The length of the middle bar when arrow is shaped
    private float mArrowShaftLength;

    // The space between bars when they are parallel
    private float mBarGap;

    // Whether bars should spin or not during progress
    private boolean mSpin;

    // Use Path instead of canvas operations so that if color has transparency, overlapping sections
    // wont look different
    private final android.graphics.Path mPath = new android.graphics.Path();

    // The reported intrinsic size of the drawable.
    private final int mSize;

    // Whether we should mirror animation when animation is reversed.
    private boolean mVerticalMirror = false;

    // The interpolated version of the original progress
    private float mProgress;

    // the amount that overlaps w/ bar size when rotation is max
    private float mMaxCutForBarSize;

    // The arrow direction
    private int mDirection = android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_START;

    /**
     *
     *
     * @param context
     * 		used to get the configuration for the drawable from
     */
    public DrawerArrowDrawable(android.content.Context context) {
        mPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mPaint.setStrokeJoin(android.graphics.Paint.Join.MITER);
        mPaint.setStrokeCap(android.graphics.Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        final android.content.res.TypedArray a = context.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        setColor(a.getColor(R.styleable.DrawerArrowToggle_color, 0));
        setBarThickness(a.getDimension(R.styleable.DrawerArrowToggle_thickness, 0));
        setSpinEnabled(a.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
        // round this because having this floating may cause bad measurements
        setGapSize(java.lang.Math.round(a.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0)));
        mSize = a.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
        // round this because having this floating may cause bad measurements
        mBarLength = java.lang.Math.round(a.getDimension(R.styleable.DrawerArrowToggle_barLength, 0));
        // round this because having this floating may cause bad measurements
        mArrowHeadLength = java.lang.Math.round(a.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0));
        mArrowShaftLength = a.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0);
        a.recycle();
    }

    /**
     * Sets the length of the arrow head (from tip to edge, perpendicular to the shaft).
     *
     * @param length
     * 		the length in pixels
     */
    public void setArrowHeadLength(float length) {
        if (mArrowHeadLength != length) {
            mArrowHeadLength = length;
            invalidateSelf();
        }
    }

    /**
     * Returns the length of the arrow head (from tip to edge, perpendicular to the shaft),
     * in pixels.
     */
    public float getArrowHeadLength() {
        return mArrowHeadLength;
    }

    /**
     * Sets the arrow shaft length.
     *
     * @param length
     * 		the length in pixels
     */
    public void setArrowShaftLength(float length) {
        if (mArrowShaftLength != length) {
            mArrowShaftLength = length;
            invalidateSelf();
        }
    }

    /**
     * Returns the arrow shaft length in pixels.
     */
    public float getArrowShaftLength() {
        return mArrowShaftLength;
    }

    /**
     * The length of the bars when they are parallel to each other.
     */
    public float getBarLength() {
        return mBarLength;
    }

    /**
     * Sets the length of the bars when they are parallel to each other.
     *
     * @param length
     * 		the length in pixels
     */
    public void setBarLength(float length) {
        if (mBarLength != length) {
            mBarLength = length;
            invalidateSelf();
        }
    }

    /**
     * Sets the color of the drawable.
     */
    public void setColor(@android.support.annotation.ColorInt
    int color) {
        if (color != mPaint.getColor()) {
            mPaint.setColor(color);
            invalidateSelf();
        }
    }

    /**
     * Returns the color of the drawable.
     */
    @android.support.annotation.ColorInt
    public int getColor() {
        return mPaint.getColor();
    }

    /**
     * Sets the thickness (stroke size) for the bars.
     *
     * @param width
     * 		stroke width in pixels
     */
    public void setBarThickness(float width) {
        if (mPaint.getStrokeWidth() != width) {
            mPaint.setStrokeWidth(width);
            mMaxCutForBarSize = ((float) ((width / 2) * java.lang.Math.cos(android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_HEAD_ANGLE)));
            invalidateSelf();
        }
    }

    /**
     * Returns the thickness (stroke width) of the bars.
     */
    public float getBarThickness() {
        return mPaint.getStrokeWidth();
    }

    /**
     * Returns the max gap between the bars when they are parallel to each other.
     *
     * @see #getGapSize()
     */
    public float getGapSize() {
        return mBarGap;
    }

    /**
     * Sets the max gap between the bars when they are parallel to each other.
     *
     * @param gap
     * 		the gap in pixels
     * @see #getGapSize()
     */
    public void setGapSize(float gap) {
        if (gap != mBarGap) {
            mBarGap = gap;
            invalidateSelf();
        }
    }

    /**
     * Set the arrow direction.
     */
    public void setDirection(@android.support.v7.graphics.drawable.DrawerArrowDrawable.ArrowDirection
    int direction) {
        if (direction != mDirection) {
            mDirection = direction;
            invalidateSelf();
        }
    }

    /**
     * Returns whether the bars should rotate or not during the transition.
     *
     * @see #setSpinEnabled(boolean)
     */
    public boolean isSpinEnabled() {
        return mSpin;
    }

    /**
     * Returns whether the bars should rotate or not during the transition.
     *
     * @param enabled
     * 		true if the bars should rotate.
     * @see #isSpinEnabled()
     */
    public void setSpinEnabled(boolean enabled) {
        if (mSpin != enabled) {
            mSpin = enabled;
            invalidateSelf();
        }
    }

    /**
     * Returns the arrow direction.
     */
    @android.support.v7.graphics.drawable.DrawerArrowDrawable.ArrowDirection
    public int getDirection() {
        return mDirection;
    }

    /**
     * If set, canvas is flipped when progress reached to end and going back to start.
     */
    public void setVerticalMirror(boolean verticalMirror) {
        if (mVerticalMirror != verticalMirror) {
            mVerticalMirror = verticalMirror;
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        android.graphics.Rect bounds = getBounds();
        final boolean flipToPointRight;
        switch (mDirection) {
            case android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_LEFT :
                flipToPointRight = false;
                break;
            case android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_RIGHT :
                flipToPointRight = true;
                break;
            case android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_END :
                flipToPointRight = android.support.v4.graphics.drawable.DrawableCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
                break;
            case android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_DIRECTION_START :
            default :
                flipToPointRight = android.support.v4.graphics.drawable.DrawableCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
                break;
        }
        // Interpolated widths of arrow bars
        float arrowHeadBarLength = ((float) (java.lang.Math.sqrt((mArrowHeadLength * mArrowHeadLength) * 2)));
        arrowHeadBarLength = android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(mBarLength, arrowHeadBarLength, mProgress);
        final float arrowShaftLength = android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(mBarLength, mArrowShaftLength, mProgress);
        // Interpolated size of middle bar
        final float arrowShaftCut = java.lang.Math.round(android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(0, mMaxCutForBarSize, mProgress));
        // The rotation of the top and bottom bars (that make the arrow head)
        final float rotation = android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(0, android.support.v7.graphics.drawable.DrawerArrowDrawable.ARROW_HEAD_ANGLE, mProgress);
        // The whole canvas rotates as the transition happens
        final float canvasRotate = android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(flipToPointRight ? 0 : -180, flipToPointRight ? 180 : 0, mProgress);
        final float arrowWidth = java.lang.Math.round(arrowHeadBarLength * java.lang.Math.cos(rotation));
        final float arrowHeight = java.lang.Math.round(arrowHeadBarLength * java.lang.Math.sin(rotation));
        mPath.rewind();
        final float topBottomBarOffset = android.support.v7.graphics.drawable.DrawerArrowDrawable.lerp(mBarGap + mPaint.getStrokeWidth(), -mMaxCutForBarSize, mProgress);
        final float arrowEdge = (-arrowShaftLength) / 2;
        // draw middle bar
        mPath.moveTo(arrowEdge + arrowShaftCut, 0);
        mPath.rLineTo(arrowShaftLength - (arrowShaftCut * 2), 0);
        // bottom bar
        mPath.moveTo(arrowEdge, topBottomBarOffset);
        mPath.rLineTo(arrowWidth, arrowHeight);
        // top bar
        mPath.moveTo(arrowEdge, -topBottomBarOffset);
        mPath.rLineTo(arrowWidth, -arrowHeight);
        mPath.close();
        canvas.save();
        // Rotate the whole canvas if spinning, if not, rotate it 180 to get
        // the arrow pointing the other way for RTL.
        final float barThickness = mPaint.getStrokeWidth();
        final int remainingSpace = ((int) ((bounds.height() - (barThickness * 3)) - (mBarGap * 2)));
        float yOffset = (remainingSpace / 4) * 2;// making sure it is a multiple of 2.

        yOffset += (barThickness * 1.5) + mBarGap;
        canvas.translate(bounds.centerX(), yOffset);
        if (mSpin) {
            canvas.rotate(canvasRotate * (mVerticalMirror ^ flipToPointRight ? -1 : 1));
        } else
            if (flipToPointRight) {
                canvas.rotate(180);
            }

        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
        if (alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @java.lang.Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    /**
     * Returns the current progress of the arrow.
     */
    @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    public float getProgress() {
        return mProgress;
    }

    /**
     * Set the progress of the arrow.
     *
     * <p>A value of {@code 0.0} indicates that the arrow should be drawn in it's starting
     * position. A value of {@code 1.0} indicates that the arrow should be drawn in it's ending
     * position.</p>
     */
    public void setProgress(@android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    float progress) {
        if (mProgress != progress) {
            mProgress = progress;
            invalidateSelf();
        }
    }

    /**
     * Returns the paint instance used for all drawing.
     */
    public final android.graphics.Paint getPaint() {
        return mPaint;
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private static float lerp(float a, float b, float t) {
        return a + ((b - a) * t);
    }
}

