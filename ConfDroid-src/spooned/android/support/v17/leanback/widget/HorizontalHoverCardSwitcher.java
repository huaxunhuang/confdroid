/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A helper class for showing a hover card view below a {@link HorizontalGridView}.  The hover card
 * is aligned to the starting edge of the selected child view.  If there is no space when scrolling
 * to the end, the ending edge of the hover card will be aligned to the ending edge of the parent
 * view, excluding padding.
 */
public final class HorizontalHoverCardSwitcher extends android.support.v17.leanback.widget.PresenterSwitcher {
    int mCardLeft;

    // left and right of selected card view
    int mCardRight;

    private int[] mTmpOffsets = new int[2];

    private android.graphics.Rect mTmpRect = new android.graphics.Rect();

    @java.lang.Override
    protected void insertView(android.view.View view) {
        // append hovercard to the end of container
        getParentViewGroup().addView(view);
    }

    @java.lang.Override
    protected void onViewSelected(android.view.View view) {
        int rightLimit = getParentViewGroup().getWidth() - getParentViewGroup().getPaddingRight();
        int leftLimit = getParentViewGroup().getPaddingLeft();
        // measure the hover card width; if it's too large, align hover card
        // end edge with row view's end edge, otherwise align start edges.
        view.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED);
        android.view.ViewGroup.MarginLayoutParams params = ((android.view.ViewGroup.MarginLayoutParams) (view.getLayoutParams()));
        boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(view) == android.view.View.LAYOUT_DIRECTION_RTL;
        if ((!isRtl) && ((mCardLeft + view.getMeasuredWidth()) > rightLimit)) {
            params.leftMargin = rightLimit - view.getMeasuredWidth();
        } else
            if (isRtl && (mCardLeft < leftLimit)) {
                params.leftMargin = leftLimit;
            } else
                if (isRtl) {
                    params.leftMargin = mCardRight - view.getMeasuredWidth();
                } else {
                    params.leftMargin = mCardLeft;
                }


        view.requestLayout();
    }

    /**
     * Select a childView inside a grid view and create/bind a corresponding hover card view
     * for the object.
     */
    public void select(android.support.v17.leanback.widget.HorizontalGridView gridView, android.view.View childView, java.lang.Object object) {
        android.view.ViewGroup parent = getParentViewGroup();
        gridView.getViewSelectedOffsets(childView, mTmpOffsets);
        mTmpRect.set(0, 0, childView.getWidth(), childView.getHeight());
        parent.offsetDescendantRectToMyCoords(childView, mTmpRect);
        mCardLeft = mTmpRect.left - mTmpOffsets[0];
        mCardRight = mTmpRect.right - mTmpOffsets[0];
        select(object);
    }
}

