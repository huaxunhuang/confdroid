/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.widget;


/**
 * A utility class for creating and animating the Smart Select animation.
 */
final class SmartSelectSprite {
    private static final int EXPAND_DURATION = 300;

    private static final int CORNER_DURATION = 50;

    private final android.view.animation.Interpolator mExpandInterpolator;

    private final android.view.animation.Interpolator mCornerInterpolator;

    private android.animation.Animator mActiveAnimator = null;

    private final java.lang.Runnable mInvalidator;

    @android.annotation.ColorInt
    private final int mFillColor;

    static final java.util.Comparator<android.graphics.RectF> RECTANGLE_COMPARATOR = java.util.Comparator.<android.graphics.RectF>comparingDouble(( e) -> e.bottom).thenComparingDouble(( e) -> e.left);

    private android.graphics.drawable.Drawable mExistingDrawable = null;

    private android.widget.SmartSelectSprite.RectangleList mExistingRectangleList = null;

    static final class RectangleWithTextSelectionLayout {
        private final android.graphics.RectF mRectangle;

        @android.text.Layout.TextSelectionLayout
        private final int mTextSelectionLayout;

        RectangleWithTextSelectionLayout(android.graphics.RectF rectangle, int textSelectionLayout) {
            mRectangle = com.android.internal.util.Preconditions.checkNotNull(rectangle);
            mTextSelectionLayout = textSelectionLayout;
        }

        public android.graphics.RectF getRectangle() {
            return mRectangle;
        }

        @android.text.Layout.TextSelectionLayout
        public int getTextSelectionLayout() {
            return mTextSelectionLayout;
        }
    }

    /**
     * A rounded rectangle with a configurable corner radius and the ability to expand outside of
     * its bounding rectangle and clip against it.
     */
    private static final class RoundedRectangleShape extends android.graphics.drawable.shapes.Shape {
        private static final java.lang.String PROPERTY_ROUND_RATIO = "roundRatio";

        /**
         * The direction in which the rectangle will perform its expansion. A rectangle can expand
         * from its left edge, its right edge or from the center (or, more precisely, the user's
         * touch point). For example, in left-to-right text, a selection spanning two lines with the
         * user's action being on the first line will have the top rectangle and expansion direction
         * of CENTER, while the bottom one will have an expansion direction of RIGHT.
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.LEFT, android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.CENTER, android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.RIGHT })
        private @interface ExpansionDirection {
            int LEFT = -1;

            int CENTER = 0;

            int RIGHT = 1;
        }

        @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        private static int invert(@android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        int expansionDirection) {
            return expansionDirection * (-1);
        }

        private final android.graphics.RectF mBoundingRectangle;

        private float mRoundRatio = 1.0F;

        @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        private final int mExpansionDirection;

        private final android.graphics.RectF mDrawRect = new android.graphics.RectF();

        private final android.graphics.Path mClipPath = new android.graphics.Path();

        /**
         * How offset the left edge of the rectangle is from the left side of the bounding box.
         */
        private float mLeftBoundary = 0;

        /**
         * How offset the right edge of the rectangle is from the left side of the bounding box.
         */
        private float mRightBoundary = 0;

        /**
         * Whether the horizontal bounds are inverted (for RTL scenarios).
         */
        private final boolean mInverted;

        private final float mBoundingWidth;

        private RoundedRectangleShape(final android.graphics.RectF boundingRectangle, @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        final int expansionDirection, final boolean inverted) {
            mBoundingRectangle = new android.graphics.RectF(boundingRectangle);
            mBoundingWidth = boundingRectangle.width();
            mInverted = inverted && (expansionDirection != android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.CENTER);
            if (inverted) {
                mExpansionDirection = android.widget.SmartSelectSprite.RoundedRectangleShape.invert(expansionDirection);
            } else {
                mExpansionDirection = expansionDirection;
            }
            if (boundingRectangle.height() > boundingRectangle.width()) {
                setRoundRatio(0.0F);
            } else {
                setRoundRatio(1.0F);
            }
        }

        /* In order to achieve the "rounded rectangle hits the wall" effect, we draw an expanding
        rounded rectangle that is clipped by the bounding box of the selected text.
         */
        @java.lang.Override
        public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
            if (mLeftBoundary == mRightBoundary) {
                return;
            }
            final float cornerRadius = getCornerRadius();
            final float adjustedCornerRadius = getAdjustedCornerRadius();
            mDrawRect.set(mBoundingRectangle);
            mDrawRect.left = (mBoundingRectangle.left + mLeftBoundary) - (cornerRadius / 2);
            mDrawRect.right = (mBoundingRectangle.left + mRightBoundary) + (cornerRadius / 2);
            canvas.save();
            mClipPath.reset();
            mClipPath.addRoundRect(mDrawRect, adjustedCornerRadius, adjustedCornerRadius, android.graphics.Path.Direction.CW);
            canvas.clipPath(mClipPath);
            canvas.drawRect(mBoundingRectangle, paint);
            canvas.restore();
        }

        void setRoundRatio(@android.annotation.FloatRange(from = 0.0, to = 1.0)
        final float roundRatio) {
            mRoundRatio = roundRatio;
        }

        float getRoundRatio() {
            return mRoundRatio;
        }

        private void setStartBoundary(final float startBoundary) {
            if (mInverted) {
                mRightBoundary = mBoundingWidth - startBoundary;
            } else {
                mLeftBoundary = startBoundary;
            }
        }

        private void setEndBoundary(final float endBoundary) {
            if (mInverted) {
                mLeftBoundary = mBoundingWidth - endBoundary;
            } else {
                mRightBoundary = endBoundary;
            }
        }

        private float getCornerRadius() {
            return java.lang.Math.min(mBoundingRectangle.width(), mBoundingRectangle.height());
        }

        private float getAdjustedCornerRadius() {
            return getCornerRadius() * mRoundRatio;
        }

        private float getBoundingWidth() {
            return ((int) (mBoundingRectangle.width() + getCornerRadius()));
        }
    }

    /**
     * A collection of {@link RoundedRectangleShape}s that abstracts them to a single shape whose
     * collective left and right boundary can be manipulated.
     */
    private static final class RectangleList extends android.graphics.drawable.shapes.Shape {
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.widget.SmartSelectSprite.RectangleList.DisplayType.RECTANGLES, android.widget.SmartSelectSprite.RectangleList.DisplayType.POLYGON })
        private @interface DisplayType {
            int RECTANGLES = 0;

            int POLYGON = 1;
        }

        private static final java.lang.String PROPERTY_RIGHT_BOUNDARY = "rightBoundary";

        private static final java.lang.String PROPERTY_LEFT_BOUNDARY = "leftBoundary";

        private final java.util.List<android.widget.SmartSelectSprite.RoundedRectangleShape> mRectangles;

        private final java.util.List<android.widget.SmartSelectSprite.RoundedRectangleShape> mReversedRectangles;

        private final android.graphics.Path mOutlinePolygonPath;

        @android.widget.SmartSelectSprite.RectangleList.DisplayType
        private int mDisplayType = android.widget.SmartSelectSprite.RectangleList.DisplayType.RECTANGLES;

        private RectangleList(final java.util.List<android.widget.SmartSelectSprite.RoundedRectangleShape> rectangles) {
            mRectangles = new java.util.ArrayList<>(rectangles);
            mReversedRectangles = new java.util.ArrayList<>(rectangles);
            java.util.Collections.reverse(mReversedRectangles);
            mOutlinePolygonPath = android.widget.SmartSelectSprite.RectangleList.generateOutlinePolygonPath(rectangles);
        }

        private void setLeftBoundary(final float leftBoundary) {
            float boundarySoFar = getTotalWidth();
            for (android.widget.SmartSelectSprite.RoundedRectangleShape rectangle : mReversedRectangles) {
                final float rectangleLeftBoundary = boundarySoFar - rectangle.getBoundingWidth();
                if (leftBoundary < rectangleLeftBoundary) {
                    rectangle.setStartBoundary(0);
                } else
                    if (leftBoundary > boundarySoFar) {
                        rectangle.setStartBoundary(rectangle.getBoundingWidth());
                    } else {
                        rectangle.setStartBoundary((rectangle.getBoundingWidth() - boundarySoFar) + leftBoundary);
                    }

                boundarySoFar = rectangleLeftBoundary;
            }
        }

        private void setRightBoundary(final float rightBoundary) {
            float boundarySoFar = 0;
            for (android.widget.SmartSelectSprite.RoundedRectangleShape rectangle : mRectangles) {
                final float rectangleRightBoundary = rectangle.getBoundingWidth() + boundarySoFar;
                if (rectangleRightBoundary < rightBoundary) {
                    rectangle.setEndBoundary(rectangle.getBoundingWidth());
                } else
                    if (boundarySoFar > rightBoundary) {
                        rectangle.setEndBoundary(0);
                    } else {
                        rectangle.setEndBoundary(rightBoundary - boundarySoFar);
                    }

                boundarySoFar = rectangleRightBoundary;
            }
        }

        void setDisplayType(@android.widget.SmartSelectSprite.RectangleList.DisplayType
        int displayType) {
            mDisplayType = displayType;
        }

        private int getTotalWidth() {
            int sum = 0;
            for (android.widget.SmartSelectSprite.RoundedRectangleShape rectangle : mRectangles) {
                sum += rectangle.getBoundingWidth();
            }
            return sum;
        }

        @java.lang.Override
        public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
            if (mDisplayType == android.widget.SmartSelectSprite.RectangleList.DisplayType.POLYGON) {
                drawPolygon(canvas, paint);
            } else {
                drawRectangles(canvas, paint);
            }
        }

        private void drawRectangles(final android.graphics.Canvas canvas, final android.graphics.Paint paint) {
            for (android.widget.SmartSelectSprite.RoundedRectangleShape rectangle : mRectangles) {
                rectangle.draw(canvas, paint);
            }
        }

        private void drawPolygon(final android.graphics.Canvas canvas, final android.graphics.Paint paint) {
            canvas.drawPath(mOutlinePolygonPath, paint);
        }

        private static android.graphics.Path generateOutlinePolygonPath(final java.util.List<android.widget.SmartSelectSprite.RoundedRectangleShape> rectangles) {
            final android.graphics.Path path = new android.graphics.Path();
            for (final android.widget.SmartSelectSprite.RoundedRectangleShape shape : rectangles) {
                final android.graphics.Path rectanglePath = new android.graphics.Path();
                rectanglePath.addRect(shape.mBoundingRectangle, android.graphics.Path.Direction.CW);
                path.op(rectanglePath, android.graphics.Path.Op.UNION);
            }
            return path;
        }
    }

    /**
     *
     *
     * @param context
     * 		the {@link Context} in which the animation will run
     * @param highlightColor
     * 		the highlight color of the underlying {@link TextView}
     * @param invalidator
     * 		a {@link Runnable} which will be called every time the animation updates,
     * 		indicating that the view drawing the animation should invalidate itself
     */
    SmartSelectSprite(final android.content.Context context, @android.annotation.ColorInt
    int highlightColor, final java.lang.Runnable invalidator) {
        mExpandInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
        mCornerInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_linear_in);
        mFillColor = highlightColor;
        mInvalidator = com.android.internal.util.Preconditions.checkNotNull(invalidator);
    }

    /**
     * Performs the Smart Select animation on the view bound to this SmartSelectSprite.
     *
     * @param start
     * 		The point from which the animation will start. Must be inside
     * 		destinationRectangles.
     * @param destinationRectangles
     * 		The rectangles which the animation will fill out by its
     * 		"selection" and finally join them into a single polygon. In
     * 		order to get the correct visual behavior, these rectangles
     * 		should be sorted according to {@link #RECTANGLE_COMPARATOR}.
     * @param onAnimationEnd
     * 		the callback which will be invoked once the whole animation
     * 		completes
     * @throws IllegalArgumentException
     * 		if the given start point is not in any of the
     * 		destinationRectangles
     * @see #cancelAnimation()
     */
    // TODO nullability checks on parameters
    public void startAnimation(final android.graphics.PointF start, final java.util.List<android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout> destinationRectangles, final java.lang.Runnable onAnimationEnd) {
        cancelAnimation();
        final android.animation.ValueAnimator.AnimatorUpdateListener updateListener = ( valueAnimator) -> mInvalidator.run();
        final int rectangleCount = destinationRectangles.size();
        final java.util.List<android.widget.SmartSelectSprite.RoundedRectangleShape> shapes = new java.util.ArrayList<>(rectangleCount);
        final java.util.List<android.animation.Animator> cornerAnimators = new java.util.ArrayList<>(rectangleCount);
        android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout centerRectangle = null;
        int startingOffset = 0;
        for (android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout rectangleWithTextSelectionLayout : destinationRectangles) {
            final android.graphics.RectF rectangle = rectangleWithTextSelectionLayout.getRectangle();
            if (android.widget.SmartSelectSprite.contains(rectangle, start)) {
                centerRectangle = rectangleWithTextSelectionLayout;
                break;
            }
            startingOffset += rectangle.width();
        }
        if (centerRectangle == null) {
            throw new java.lang.IllegalArgumentException("Center point is not inside any of the rectangles!");
        }
        startingOffset += start.x - centerRectangle.getRectangle().left;
        @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        final int[] expansionDirections = android.widget.SmartSelectSprite.generateDirections(centerRectangle, destinationRectangles);
        for (int index = 0; index < rectangleCount; ++index) {
            final android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout rectangleWithTextSelectionLayout = destinationRectangles.get(index);
            final android.graphics.RectF rectangle = rectangleWithTextSelectionLayout.getRectangle();
            final android.widget.SmartSelectSprite.RoundedRectangleShape shape = new android.widget.SmartSelectSprite.RoundedRectangleShape(rectangle, expansionDirections[index], rectangleWithTextSelectionLayout.getTextSelectionLayout() == android.text.Layout.TEXT_SELECTION_LAYOUT_RIGHT_TO_LEFT);
            cornerAnimators.add(createCornerAnimator(shape, updateListener));
            shapes.add(shape);
        }
        final android.widget.SmartSelectSprite.RectangleList rectangleList = new android.widget.SmartSelectSprite.RectangleList(shapes);
        final android.graphics.drawable.ShapeDrawable shapeDrawable = new android.graphics.drawable.ShapeDrawable(rectangleList);
        final android.graphics.Paint paint = shapeDrawable.getPaint();
        paint.setColor(mFillColor);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        mExistingRectangleList = rectangleList;
        mExistingDrawable = shapeDrawable;
        mActiveAnimator = createAnimator(rectangleList, startingOffset, startingOffset, cornerAnimators, updateListener, onAnimationEnd);
        mActiveAnimator.start();
    }

    /**
     * Returns whether the sprite is currently animating.
     */
    public boolean isAnimationActive() {
        return (mActiveAnimator != null) && mActiveAnimator.isRunning();
    }

    private android.animation.Animator createAnimator(final android.widget.SmartSelectSprite.RectangleList rectangleList, final float startingOffsetLeft, final float startingOffsetRight, final java.util.List<android.animation.Animator> cornerAnimators, final android.animation.ValueAnimator.AnimatorUpdateListener updateListener, final java.lang.Runnable onAnimationEnd) {
        final android.animation.ObjectAnimator rightBoundaryAnimator = android.animation.ObjectAnimator.ofFloat(rectangleList, android.widget.SmartSelectSprite.RectangleList.PROPERTY_RIGHT_BOUNDARY, startingOffsetRight, rectangleList.getTotalWidth());
        final android.animation.ObjectAnimator leftBoundaryAnimator = android.animation.ObjectAnimator.ofFloat(rectangleList, android.widget.SmartSelectSprite.RectangleList.PROPERTY_LEFT_BOUNDARY, startingOffsetLeft, 0);
        rightBoundaryAnimator.setDuration(android.widget.SmartSelectSprite.EXPAND_DURATION);
        leftBoundaryAnimator.setDuration(android.widget.SmartSelectSprite.EXPAND_DURATION);
        rightBoundaryAnimator.addUpdateListener(updateListener);
        leftBoundaryAnimator.addUpdateListener(updateListener);
        rightBoundaryAnimator.setInterpolator(mExpandInterpolator);
        leftBoundaryAnimator.setInterpolator(mExpandInterpolator);
        final android.animation.AnimatorSet cornerAnimator = new android.animation.AnimatorSet();
        cornerAnimator.playTogether(cornerAnimators);
        final android.animation.AnimatorSet boundaryAnimator = new android.animation.AnimatorSet();
        boundaryAnimator.playTogether(leftBoundaryAnimator, rightBoundaryAnimator);
        final android.animation.AnimatorSet animatorSet = new android.animation.AnimatorSet();
        animatorSet.playSequentially(boundaryAnimator, cornerAnimator);
        setUpAnimatorListener(animatorSet, onAnimationEnd);
        return animatorSet;
    }

    private void setUpAnimatorListener(final android.animation.Animator animator, final java.lang.Runnable onAnimationEnd) {
        animator.addListener(new android.animation.Animator.AnimatorListener() {
            @java.lang.Override
            public void onAnimationStart(android.animation.Animator animator) {
            }

            @java.lang.Override
            public void onAnimationEnd(android.animation.Animator animator) {
                mExistingRectangleList.setDisplayType(android.widget.SmartSelectSprite.RectangleList.DisplayType.POLYGON);
                mInvalidator.run();
                onAnimationEnd.run();
            }

            @java.lang.Override
            public void onAnimationCancel(android.animation.Animator animator) {
            }

            @java.lang.Override
            public void onAnimationRepeat(android.animation.Animator animator) {
            }
        });
    }

    private android.animation.ObjectAnimator createCornerAnimator(final android.widget.SmartSelectSprite.RoundedRectangleShape shape, final android.animation.ValueAnimator.AnimatorUpdateListener listener) {
        final android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(shape, android.widget.SmartSelectSprite.RoundedRectangleShape.PROPERTY_ROUND_RATIO, shape.getRoundRatio(), 0.0F);
        animator.setDuration(android.widget.SmartSelectSprite.CORNER_DURATION);
        animator.addUpdateListener(listener);
        animator.setInterpolator(mCornerInterpolator);
        return animator;
    }

    @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
    private static int[] generateDirections(final android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout centerRectangle, final java.util.List<android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout> rectangles) {
        @android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection
        final int[] result = new int[rectangles.size()];
        final int centerRectangleIndex = rectangles.indexOf(centerRectangle);
        for (int i = 0; i < (centerRectangleIndex - 1); ++i) {
            result[i] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.LEFT;
        }
        if (rectangles.size() == 1) {
            result[centerRectangleIndex] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.CENTER;
        } else
            if (centerRectangleIndex == 0) {
                result[centerRectangleIndex] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.LEFT;
            } else
                if (centerRectangleIndex == (rectangles.size() - 1)) {
                    result[centerRectangleIndex] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.RIGHT;
                } else {
                    result[centerRectangleIndex] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.CENTER;
                }


        for (int i = centerRectangleIndex + 1; i < result.length; ++i) {
            result[i] = android.widget.SmartSelectSprite.RoundedRectangleShape.ExpansionDirection.RIGHT;
        }
        return result;
    }

    /**
     * A variant of {@link RectF#contains(float, float)} that also allows the point to reside on
     * the right boundary of the rectangle.
     *
     * @param rectangle
     * 		the rectangle inside which the point should be to be considered "contained"
     * @param point
     * 		the point which will be tested
     * @return whether the point is inside the rectangle (or on it's right boundary)
     */
    private static boolean contains(final android.graphics.RectF rectangle, final android.graphics.PointF point) {
        final float x = point.x;
        final float y = point.y;
        return (((x >= rectangle.left) && (x <= rectangle.right)) && (y >= rectangle.top)) && (y <= rectangle.bottom);
    }

    private void removeExistingDrawables() {
        mExistingDrawable = null;
        mExistingRectangleList = null;
        mInvalidator.run();
    }

    /**
     * Cancels any active Smart Select animation that might be in progress.
     */
    public void cancelAnimation() {
        if (mActiveAnimator != null) {
            mActiveAnimator.cancel();
            mActiveAnimator = null;
            removeExistingDrawables();
        }
    }

    public void draw(android.graphics.Canvas canvas) {
        if (mExistingDrawable != null) {
            mExistingDrawable.draw(canvas);
        }
    }
}

