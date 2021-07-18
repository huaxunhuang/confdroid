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
 * A target collects the set of contextual information for a ScrollCaptureHandler discovered during
 * a {@link View#dispatchScrollCaptureSearch scroll capture search}.
 *
 * @unknown 
 */
public final class ScrollCaptureTarget {
    private final android.view.View mContainingView;

    private final android.view.ScrollCaptureCallback mCallback;

    private final android.graphics.Rect mLocalVisibleRect;

    private final android.graphics.Point mPositionInWindow;

    private final int mHint;

    private android.graphics.Rect mScrollBounds;

    private final float[] mTmpFloatArr = new float[2];

    private final android.graphics.Matrix mMatrixViewLocalToWindow = new android.graphics.Matrix();

    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    public ScrollCaptureTarget(@android.annotation.NonNull
    android.view.View scrollTarget, @android.annotation.NonNull
    android.graphics.Rect localVisibleRect, @android.annotation.NonNull
    android.graphics.Point positionInWindow, @android.annotation.NonNull
    android.view.ScrollCaptureCallback callback) {
        mContainingView = scrollTarget;
        mHint = mContainingView.getScrollCaptureHint();
        mCallback = callback;
        mLocalVisibleRect = localVisibleRect;
        mPositionInWindow = positionInWindow;
    }

    /**
     *
     *
     * @return the hint that the {@code containing view} had during the scroll capture search
     */
    @android.view.View.ScrollCaptureHint
    public int getHint() {
        return mHint;
    }

    /**
     *
     *
     * @return the {@link ScrollCaptureCallback} for this target
     */
    @android.annotation.NonNull
    public android.view.ScrollCaptureCallback getCallback() {
        return mCallback;
    }

    /**
     *
     *
     * @return the {@code containing view} for this {@link ScrollCaptureCallback callback}
     */
    @android.annotation.NonNull
    public android.view.View getContainingView() {
        return mContainingView;
    }

    /**
     * Returns the un-clipped, visible bounds of the containing view during the scroll capture
     * search. This is used to determine on-screen area to assist in selecting the primary target.
     *
     * @return the visible bounds of the {@code containing view} in view-local coordinates
     */
    @android.annotation.NonNull
    public android.graphics.Rect getLocalVisibleRect() {
        return mLocalVisibleRect;
    }

    /**
     *
     *
     * @return the position of the {@code containing view} within the window
     */
    @android.annotation.NonNull
    public android.graphics.Point getPositionInWindow() {
        return mPositionInWindow;
    }

    /**
     *
     *
     * @return the {@code scroll bounds} for this {@link ScrollCaptureCallback callback}
     */
    @android.annotation.Nullable
    public android.graphics.Rect getScrollBounds() {
        return mScrollBounds;
    }

    /**
     * Sets the scroll bounds rect to the intersection of provided rect and the current bounds of
     * the {@code containing view}.
     */
    public void setScrollBounds(@android.annotation.Nullable
    android.graphics.Rect scrollBounds) {
        mScrollBounds = android.graphics.Rect.copyOrNull(scrollBounds);
        if (mScrollBounds == null) {
            return;
        }
        if (!mScrollBounds.intersect(0, 0, mContainingView.getWidth(), mContainingView.getHeight())) {
            mScrollBounds.setEmpty();
        }
    }

    private static void zero(float[] pointArray) {
        pointArray[0] = 0;
        pointArray[1] = 0;
    }

    private static void roundIntoPoint(android.graphics.Point pointObj, float[] pointArray) {
        pointObj.x = com.android.internal.util.FastMath.round(pointArray[0]);
        pointObj.y = com.android.internal.util.FastMath.round(pointArray[1]);
    }

    /**
     * Refresh the value of {@link #mLocalVisibleRect} and {@link #mPositionInWindow} based on the
     * current state of the {@code containing view}.
     */
    @android.annotation.UiThread
    public void updatePositionInWindow() {
        mMatrixViewLocalToWindow.reset();
        mContainingView.transformMatrixToGlobal(mMatrixViewLocalToWindow);
        android.view.ScrollCaptureTarget.zero(mTmpFloatArr);
        mMatrixViewLocalToWindow.mapPoints(mTmpFloatArr);
        android.view.ScrollCaptureTarget.roundIntoPoint(mPositionInWindow, mTmpFloatArr);
    }
}

