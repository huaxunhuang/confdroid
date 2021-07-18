/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Helper class to handle ItemAlignmentFacet in a grid view.
 */
class ItemAlignmentFacetHelper {
    private static android.graphics.Rect sRect = new android.graphics.Rect();

    /**
     * get alignment position relative to optical left/top of itemView.
     */
    static int getAlignmentPosition(android.view.View itemView, android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef facet, int orientation) {
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams p = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (itemView.getLayoutParams()));
        android.view.View view = itemView;
        if (facet.mViewId != 0) {
            view = itemView.findViewById(facet.mViewId);
            if (view == null) {
                view = itemView;
            }
        }
        int alignPos = facet.mOffset;
        if (orientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            if (facet.mOffset >= 0) {
                if (facet.mOffsetWithPadding) {
                    alignPos += view.getPaddingLeft();
                }
            } else {
                if (facet.mOffsetWithPadding) {
                    alignPos -= view.getPaddingRight();
                }
            }
            if (facet.mOffsetPercent != android.support.v17.leanback.widget.ItemAlignmentFacet.ITEM_ALIGN_OFFSET_PERCENT_DISABLED) {
                alignPos += ((view == itemView ? p.getOpticalWidth(view) : view.getWidth()) * facet.mOffsetPercent) / 100.0F;
            }
            if (itemView != view) {
                android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect.left = alignPos;
                ((android.view.ViewGroup) (itemView)).offsetDescendantRectToMyCoords(view, android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect);
                alignPos = android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect.left - p.getOpticalLeftInset();
            }
        } else {
            if (facet.mOffset >= 0) {
                if (facet.mOffsetWithPadding) {
                    alignPos += view.getPaddingTop();
                }
            } else {
                if (facet.mOffsetWithPadding) {
                    alignPos -= view.getPaddingBottom();
                }
            }
            if (facet.mOffsetPercent != android.support.v17.leanback.widget.ItemAlignmentFacet.ITEM_ALIGN_OFFSET_PERCENT_DISABLED) {
                alignPos += ((view == itemView ? p.getOpticalHeight(view) : view.getHeight()) * facet.mOffsetPercent) / 100.0F;
            }
            if (itemView != view) {
                android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect.top = alignPos;
                ((android.view.ViewGroup) (itemView)).offsetDescendantRectToMyCoords(view, android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect);
                alignPos = android.support.v17.leanback.widget.ItemAlignmentFacetHelper.sRect.top - p.getOpticalTopInset();
            }
            if ((view instanceof android.widget.TextView) && facet.isAlignedToTextViewBaseLine()) {
                android.graphics.Paint textPaint = ((android.widget.TextView) (view)).getPaint();
                int titleViewTextHeight = -textPaint.getFontMetricsInt().top;
                alignPos += titleViewTextHeight;
            }
        }
        return alignPos;
    }
}

