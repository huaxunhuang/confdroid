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
 * Defines alignment position on two directions of an item view. Typically item
 * view alignment is at the center of the view. The class allows defining
 * alignment at left/right or fixed offset/percentage position; it also allows
 * using descendant view by id match.
 */
class ItemAlignment {
    static final class Axis extends android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef {
        private int mOrientation;

        Axis(int orientation) {
            mOrientation = orientation;
        }

        /**
         * get alignment position relative to optical left/top of itemView.
         */
        public int getAlignmentPosition(android.view.View itemView) {
            return android.support.v17.leanback.widget.ItemAlignmentFacetHelper.getAlignmentPosition(itemView, this, mOrientation);
        }
    }

    private int mOrientation = android.support.v7.widget.RecyclerView.HORIZONTAL;

    public final android.support.v17.leanback.widget.ItemAlignment.Axis vertical = new android.support.v17.leanback.widget.ItemAlignment.Axis(android.support.v7.widget.RecyclerView.VERTICAL);

    public final android.support.v17.leanback.widget.ItemAlignment.Axis horizontal = new android.support.v17.leanback.widget.ItemAlignment.Axis(android.support.v7.widget.RecyclerView.HORIZONTAL);

    private android.support.v17.leanback.widget.ItemAlignment.Axis mMainAxis = horizontal;

    private android.support.v17.leanback.widget.ItemAlignment.Axis mSecondAxis = vertical;

    public final android.support.v17.leanback.widget.ItemAlignment.Axis mainAxis() {
        return mMainAxis;
    }

    public final android.support.v17.leanback.widget.ItemAlignment.Axis secondAxis() {
        return mSecondAxis;
    }

    public final void setOrientation(int orientation) {
        mOrientation = orientation;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            mMainAxis = horizontal;
            mSecondAxis = vertical;
        } else {
            mMainAxis = vertical;
            mSecondAxis = horizontal;
        }
    }

    public final int getOrientation() {
        return mOrientation;
    }
}

