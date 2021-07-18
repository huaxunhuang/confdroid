/**
 * Copyright (C) 2020 The Android Open Source Project
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
 * In order to accept the cutout specification for all of edges in devices, the specification
 * parsing method is extracted from
 * {@link android.view.DisplayCutout#fromResourcesRectApproximation(Resources, int, int)} to be
 * the specified class for parsing the specification.
 * BNF definition:
 * <ul>
 *      <li>Cutouts Specification = ([Cutout Delimiter],Cutout Specification) {...}, [Dp] ; </li>
 *      <li>Cutout Specification  = [Vertical Position], (SVG Path Element), [Horizontal Position]
 *                                  [Bind Cutout] ;</li>
 *      <li>Vertical Position     = "@bottom" | "@center_vertical" ;</li>
 *      <li>Horizontal Position   = "@left" | "@right" ;</li>
 *      <li>Bind Cutout           = "@bind_left_cutout" | "@bind_right_cutout" ;</li>
 *      <li>Cutout Delimiter      = "@cutout" ;</li>
 *      <li>Dp                    = "@dp"</li>
 * </ul>
 *
 * <ul>
 *     <li>Vertical position is top by default if there is neither "@bottom" nor "@center_vertical"
 *     </li>
 *     <li>Horizontal position is center horizontal by default if there is neither "@left" nor
 *     "@right".</li>
 *     <li>@bottom make the cutout piece bind to bottom edge.</li>
 *     <li>both of @bind_left_cutout and @bind_right_cutout are use to claim the cutout belong to
 *     left or right edge cutout.</li>
 * </ul>
 *
 * @unknown 
 */
@com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
public class CutoutSpecification {
    private static final java.lang.String TAG = "CutoutSpecification";

    private static final boolean DEBUG = false;

    private static final int MINIMAL_ACCEPTABLE_PATH_LENGTH = "H1V1Z".length();

    private static final char MARKER_START_CHAR = '@';

    private static final java.lang.String DP_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "dp";

    private static final java.lang.String BOTTOM_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "bottom";

    private static final java.lang.String RIGHT_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "right";

    private static final java.lang.String LEFT_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "left";

    private static final java.lang.String CUTOUT_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "cutout";

    private static final java.lang.String CENTER_VERTICAL_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "center_vertical";

    /* By default, it's top bound cutout. That's why TOP_BOUND_CUTOUT_MARKER is not defined */
    private static final java.lang.String BIND_RIGHT_CUTOUT_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "bind_right_cutout";

    private static final java.lang.String BIND_LEFT_CUTOUT_MARKER = android.view.CutoutSpecification.MARKER_START_CHAR + "bind_left_cutout";

    private final android.graphics.Path mPath;

    private final android.graphics.Rect mLeftBound;

    private final android.graphics.Rect mTopBound;

    private final android.graphics.Rect mRightBound;

    private final android.graphics.Rect mBottomBound;

    private final android.graphics.Insets mInsets;

    private CutoutSpecification(@android.annotation.NonNull
    android.view.CutoutSpecification.Parser parser) {
        mPath = parser.mPath;
        mLeftBound = parser.mLeftBound;
        mTopBound = parser.mTopBound;
        mRightBound = parser.mRightBound;
        mBottomBound = parser.mBottomBound;
        mInsets = parser.mInsets;
        if (android.view.CutoutSpecification.DEBUG) {
            android.util.Log.d(android.view.CutoutSpecification.TAG, java.lang.String.format(java.util.Locale.ENGLISH, "left cutout = %s, top cutout = %s, right cutout = %s, bottom cutout = %s", mLeftBound != null ? mLeftBound.toString() : "", mTopBound != null ? mTopBound.toString() : "", mRightBound != null ? mRightBound.toString() : "", mBottomBound != null ? mBottomBound.toString() : ""));
        }
    }

    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.graphics.Path getPath() {
        return mPath;
    }

    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.graphics.Rect getLeftBound() {
        return mLeftBound;
    }

    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.graphics.Rect getTopBound() {
        return mTopBound;
    }

    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.graphics.Rect getRightBound() {
        return mRightBound;
    }

    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.Nullable
    public android.graphics.Rect getBottomBound() {
        return mBottomBound;
    }

    /**
     * To count the safe inset according to the cutout bounds and waterfall inset.
     *
     * @return the safe inset.
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    @android.annotation.NonNull
    public android.graphics.Rect getSafeInset() {
        return mInsets.toRect();
    }

    private static int decideWhichEdge(boolean isTopEdgeShortEdge, boolean isShortEdge, boolean isStart) {
        return isTopEdgeShortEdge ? isShortEdge ? isStart ? android.view.Gravity.TOP : android.view.Gravity.BOTTOM : isStart ? android.view.Gravity.LEFT : android.view.Gravity.RIGHT : isShortEdge ? isStart ? android.view.Gravity.LEFT : android.view.Gravity.RIGHT : isStart ? android.view.Gravity.TOP : android.view.Gravity.BOTTOM;
    }

    /**
     * The CutoutSpecification Parser.
     */
    @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
    public static class Parser {
        private final boolean mIsShortEdgeOnTop;

        private final float mDensity;

        private final int mDisplayWidth;

        private final int mDisplayHeight;

        private final android.graphics.Matrix mMatrix;

        private android.graphics.Insets mInsets;

        private int mSafeInsetLeft;

        private int mSafeInsetTop;

        private int mSafeInsetRight;

        private int mSafeInsetBottom;

        private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

        private final android.graphics.RectF mTmpRectF = new android.graphics.RectF();

        private boolean mInDp;

        private android.graphics.Path mPath;

        private android.graphics.Rect mLeftBound;

        private android.graphics.Rect mTopBound;

        private android.graphics.Rect mRightBound;

        private android.graphics.Rect mBottomBound;

        private boolean mPositionFromLeft = false;

        private boolean mPositionFromRight = false;

        private boolean mPositionFromBottom = false;

        private boolean mPositionFromCenterVertical = false;

        private boolean mBindLeftCutout = false;

        private boolean mBindRightCutout = false;

        private boolean mBindBottomCutout = false;

        private boolean mIsTouchShortEdgeStart;

        private boolean mIsTouchShortEdgeEnd;

        private boolean mIsCloserToStartSide;

        /**
         * The constructor of the CutoutSpecification parser to parse the specification of cutout.
         *
         * @param density
         * 		the display density.
         * @param displayWidth
         * 		the display width.
         * @param displayHeight
         * 		the display height.
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
        public Parser(float density, int displayWidth, int displayHeight) {
            mDensity = density;
            mDisplayWidth = displayWidth;
            mDisplayHeight = displayHeight;
            mMatrix = new android.graphics.Matrix();
            mIsShortEdgeOnTop = mDisplayWidth < mDisplayHeight;
        }

        private void computeBoundsRectAndAddToRegion(android.graphics.Path p, android.graphics.Region inoutRegion, android.graphics.Rect inoutRect) {
            mTmpRectF.setEmpty();
            /* unused */
            p.computeBounds(mTmpRectF, false);
            mTmpRectF.round(inoutRect);
            inoutRegion.op(inoutRect, android.graphics.Region.Op.UNION);
        }

        private void resetStatus(java.lang.StringBuilder sb) {
            sb.setLength(0);
            mPositionFromBottom = false;
            mPositionFromLeft = false;
            mPositionFromRight = false;
            mPositionFromCenterVertical = false;
            mBindLeftCutout = false;
            mBindRightCutout = false;
            mBindBottomCutout = false;
        }

        private void translateMatrix() {
            final float offsetX;
            if (mPositionFromRight) {
                offsetX = mDisplayWidth;
            } else
                if (mPositionFromLeft) {
                    offsetX = 0;
                } else {
                    offsetX = mDisplayWidth / 2.0F;
                }

            final float offsetY;
            if (mPositionFromBottom) {
                offsetY = mDisplayHeight;
            } else
                if (mPositionFromCenterVertical) {
                    offsetY = mDisplayHeight / 2.0F;
                } else {
                    offsetY = 0;
                }

            mMatrix.reset();
            if (mInDp) {
                mMatrix.postScale(mDensity, mDensity);
            }
            mMatrix.postTranslate(offsetX, offsetY);
        }

        private int computeSafeInsets(int gravity, android.graphics.Rect rect) {
            if (((gravity == android.view.Gravity.LEFT) && (rect.right > 0)) && (rect.right < mDisplayWidth)) {
                return rect.right;
            } else
                if (((gravity == android.view.Gravity.TOP) && (rect.bottom > 0)) && (rect.bottom < mDisplayHeight)) {
                    return rect.bottom;
                } else
                    if (((gravity == android.view.Gravity.RIGHT) && (rect.left > 0)) && (rect.left < mDisplayWidth)) {
                        return mDisplayWidth - rect.left;
                    } else
                        if (((gravity == android.view.Gravity.BOTTOM) && (rect.top > 0)) && (rect.top < mDisplayHeight)) {
                            return mDisplayHeight - rect.top;
                        }



            return 0;
        }

        private void setSafeInset(int gravity, int inset) {
            if (gravity == android.view.Gravity.LEFT) {
                mSafeInsetLeft = inset;
            } else
                if (gravity == android.view.Gravity.TOP) {
                    mSafeInsetTop = inset;
                } else
                    if (gravity == android.view.Gravity.RIGHT) {
                        mSafeInsetRight = inset;
                    } else
                        if (gravity == android.view.Gravity.BOTTOM) {
                            mSafeInsetBottom = inset;
                        }



        }

        private int getSafeInset(int gravity) {
            if (gravity == android.view.Gravity.LEFT) {
                return mSafeInsetLeft;
            } else
                if (gravity == android.view.Gravity.TOP) {
                    return mSafeInsetTop;
                } else
                    if (gravity == android.view.Gravity.RIGHT) {
                        return mSafeInsetRight;
                    } else
                        if (gravity == android.view.Gravity.BOTTOM) {
                            return mSafeInsetBottom;
                        }



            return 0;
        }

        @android.annotation.NonNull
        private android.graphics.Rect onSetEdgeCutout(boolean isStart, boolean isShortEdge, @android.annotation.NonNull
        android.graphics.Rect rect) {
            final int gravity;
            if (isShortEdge) {
                gravity = android.view.CutoutSpecification.decideWhichEdge(mIsShortEdgeOnTop, true, isStart);
            } else {
                if (mIsTouchShortEdgeStart && mIsTouchShortEdgeEnd) {
                    gravity = android.view.CutoutSpecification.decideWhichEdge(mIsShortEdgeOnTop, false, isStart);
                } else
                    if (mIsTouchShortEdgeStart || mIsTouchShortEdgeEnd) {
                        gravity = android.view.CutoutSpecification.decideWhichEdge(mIsShortEdgeOnTop, true, mIsCloserToStartSide);
                    } else {
                        gravity = android.view.CutoutSpecification.decideWhichEdge(mIsShortEdgeOnTop, isShortEdge, isStart);
                    }

            }
            int oldSafeInset = getSafeInset(gravity);
            int newSafeInset = computeSafeInsets(gravity, rect);
            if (oldSafeInset < newSafeInset) {
                setSafeInset(gravity, newSafeInset);
            }
            return new android.graphics.Rect(rect);
        }

        private void setEdgeCutout(@android.annotation.NonNull
        android.graphics.Path newPath) {
            if (mBindRightCutout && (mRightBound == null)) {
                mRightBound = onSetEdgeCutout(false, !mIsShortEdgeOnTop, mTmpRect);
            } else
                if (mBindLeftCutout && (mLeftBound == null)) {
                    mLeftBound = onSetEdgeCutout(true, !mIsShortEdgeOnTop, mTmpRect);
                } else
                    if (mBindBottomCutout && (mBottomBound == null)) {
                        mBottomBound = onSetEdgeCutout(false, mIsShortEdgeOnTop, mTmpRect);
                    } else
                        if ((!((mBindBottomCutout || mBindLeftCutout) || mBindRightCutout)) && (mTopBound == null)) {
                            mTopBound = onSetEdgeCutout(true, mIsShortEdgeOnTop, mTmpRect);
                        } else {
                            return;
                        }



            if (mPath != null) {
                mPath.addPath(newPath);
            } else {
                mPath = newPath;
            }
        }

        private void parseSvgPathSpec(android.graphics.Region region, java.lang.String spec) {
            if (android.text.TextUtils.length(spec) < android.view.CutoutSpecification.MINIMAL_ACCEPTABLE_PATH_LENGTH) {
                android.util.Log.e(android.view.CutoutSpecification.TAG, "According to SVG definition, it shouldn't happen");
                return;
            }
            spec.trim();
            translateMatrix();
            final android.graphics.Path newPath = android.util.PathParser.createPathFromPathData(spec);
            newPath.transform(mMatrix);
            computeBoundsRectAndAddToRegion(newPath, region, mTmpRect);
            if (android.view.CutoutSpecification.DEBUG) {
                android.util.Log.d(android.view.CutoutSpecification.TAG, java.lang.String.format(java.util.Locale.ENGLISH, "hasLeft = %b, hasRight = %b, hasBottom = %b, hasCenterVertical = %b", mPositionFromLeft, mPositionFromRight, mPositionFromBottom, mPositionFromCenterVertical));
                android.util.Log.d(android.view.CutoutSpecification.TAG, "region = " + region);
                android.util.Log.d(android.view.CutoutSpecification.TAG, (((("spec = \"" + spec) + "\" rect = ") + mTmpRect) + " newPath = ") + newPath);
            }
            if (mTmpRect.isEmpty()) {
                return;
            }
            if (mIsShortEdgeOnTop) {
                mIsTouchShortEdgeStart = mTmpRect.top <= 0;
                mIsTouchShortEdgeEnd = mTmpRect.bottom >= mDisplayHeight;
                mIsCloserToStartSide = mTmpRect.centerY() < (mDisplayHeight / 2);
            } else {
                mIsTouchShortEdgeStart = mTmpRect.left <= 0;
                mIsTouchShortEdgeEnd = mTmpRect.right >= mDisplayWidth;
                mIsCloserToStartSide = mTmpRect.centerX() < (mDisplayWidth / 2);
            }
            setEdgeCutout(newPath);
        }

        private void parseSpecWithoutDp(@android.annotation.NonNull
        java.lang.String specWithoutDp) {
            android.graphics.Region region = android.graphics.Region.obtain();
            java.lang.StringBuilder sb = null;
            int currentIndex = 0;
            int lastIndex = 0;
            while ((currentIndex = specWithoutDp.indexOf(android.view.CutoutSpecification.MARKER_START_CHAR, lastIndex)) != (-1)) {
                if (sb == null) {
                    sb = new java.lang.StringBuilder(specWithoutDp.length());
                }
                sb.append(specWithoutDp, lastIndex, currentIndex);
                if (specWithoutDp.startsWith(android.view.CutoutSpecification.LEFT_MARKER, currentIndex)) {
                    if (!mPositionFromRight) {
                        mPositionFromLeft = true;
                    }
                    currentIndex += android.view.CutoutSpecification.LEFT_MARKER.length();
                } else
                    if (specWithoutDp.startsWith(android.view.CutoutSpecification.RIGHT_MARKER, currentIndex)) {
                        if (!mPositionFromLeft) {
                            mPositionFromRight = true;
                        }
                        currentIndex += android.view.CutoutSpecification.RIGHT_MARKER.length();
                    } else
                        if (specWithoutDp.startsWith(android.view.CutoutSpecification.BOTTOM_MARKER, currentIndex)) {
                            parseSvgPathSpec(region, sb.toString());
                            currentIndex += android.view.CutoutSpecification.BOTTOM_MARKER.length();
                            /* prepare to parse the rest path */
                            resetStatus(sb);
                            mBindBottomCutout = true;
                            mPositionFromBottom = true;
                        } else
                            if (specWithoutDp.startsWith(android.view.CutoutSpecification.CENTER_VERTICAL_MARKER, currentIndex)) {
                                parseSvgPathSpec(region, sb.toString());
                                currentIndex += android.view.CutoutSpecification.CENTER_VERTICAL_MARKER.length();
                                /* prepare to parse the rest path */
                                resetStatus(sb);
                                mPositionFromCenterVertical = true;
                            } else
                                if (specWithoutDp.startsWith(android.view.CutoutSpecification.CUTOUT_MARKER, currentIndex)) {
                                    parseSvgPathSpec(region, sb.toString());
                                    currentIndex += android.view.CutoutSpecification.CUTOUT_MARKER.length();
                                    /* prepare to parse the rest path */
                                    resetStatus(sb);
                                } else
                                    if (specWithoutDp.startsWith(android.view.CutoutSpecification.BIND_LEFT_CUTOUT_MARKER, currentIndex)) {
                                        mBindBottomCutout = false;
                                        mBindRightCutout = false;
                                        mBindLeftCutout = true;
                                        currentIndex += android.view.CutoutSpecification.BIND_LEFT_CUTOUT_MARKER.length();
                                    } else
                                        if (specWithoutDp.startsWith(android.view.CutoutSpecification.BIND_RIGHT_CUTOUT_MARKER, currentIndex)) {
                                            mBindBottomCutout = false;
                                            mBindLeftCutout = false;
                                            mBindRightCutout = true;
                                            currentIndex += android.view.CutoutSpecification.BIND_RIGHT_CUTOUT_MARKER.length();
                                        } else {
                                            currentIndex += 1;
                                        }






                lastIndex = currentIndex;
            } 
            if (sb == null) {
                parseSvgPathSpec(region, specWithoutDp);
            } else {
                sb.append(specWithoutDp, lastIndex, specWithoutDp.length());
                parseSvgPathSpec(region, sb.toString());
            }
            region.recycle();
        }

        /**
         * To parse specification string as the CutoutSpecification.
         *
         * @param originalSpec
         * 		the specification string
         * @return the CutoutSpecification instance
         */
        @com.android.internal.annotations.VisibleForTesting(visibility = com.android.internal.annotations.VisibleForTesting.Visibility.PACKAGE)
        public android.view.CutoutSpecification parse(@android.annotation.NonNull
        java.lang.String originalSpec) {
            java.util.Objects.requireNonNull(originalSpec);
            int dpIndex = originalSpec.lastIndexOf(android.view.CutoutSpecification.DP_MARKER);
            mInDp = dpIndex != (-1);
            final java.lang.String spec;
            if (dpIndex != (-1)) {
                spec = originalSpec.substring(0, dpIndex) + originalSpec.substring(dpIndex + android.view.CutoutSpecification.DP_MARKER.length());
            } else {
                spec = originalSpec;
            }
            parseSpecWithoutDp(spec);
            mInsets = android.graphics.Insets.of(mSafeInsetLeft, mSafeInsetTop, mSafeInsetRight, mSafeInsetBottom);
            return new android.view.CutoutSpecification(this);
        }
    }
}

