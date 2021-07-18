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
package android.support.v7.widget;


/**
 * A helper class to do scroll offset calculations.
 */
class ScrollbarHelper {
    /**
     *
     *
     * @param startChild
     * 		View closest to start of the list. (top or left)
     * @param endChild
     * 		View closest to end of the list (bottom or right)
     */
    static int computeScrollOffset(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.OrientationHelper orientation, android.view.View startChild, android.view.View endChild, android.support.v7.widget.RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled, boolean reverseLayout) {
        if ((((lm.getChildCount() == 0) || (state.getItemCount() == 0)) || (startChild == null)) || (endChild == null)) {
            return 0;
        }
        final int minPosition = java.lang.Math.min(lm.getPosition(startChild), lm.getPosition(endChild));
        final int maxPosition = java.lang.Math.max(lm.getPosition(startChild), lm.getPosition(endChild));
        final int itemsBefore = (reverseLayout) ? java.lang.Math.max(0, (state.getItemCount() - maxPosition) - 1) : java.lang.Math.max(0, minPosition);
        if (!smoothScrollbarEnabled) {
            return itemsBefore;
        }
        final int laidOutArea = java.lang.Math.abs(orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild));
        final int itemRange = java.lang.Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        final float avgSizePerRow = ((float) (laidOutArea)) / itemRange;
        return java.lang.Math.round((itemsBefore * avgSizePerRow) + (orientation.getStartAfterPadding() - orientation.getDecoratedStart(startChild)));
    }

    /**
     *
     *
     * @param startChild
     * 		View closest to start of the list. (top or left)
     * @param endChild
     * 		View closest to end of the list (bottom or right)
     */
    static int computeScrollExtent(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.OrientationHelper orientation, android.view.View startChild, android.view.View endChild, android.support.v7.widget.RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if ((((lm.getChildCount() == 0) || (state.getItemCount() == 0)) || (startChild == null)) || (endChild == null)) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return java.lang.Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        }
        final int extend = orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild);
        return java.lang.Math.min(orientation.getTotalSpace(), extend);
    }

    /**
     *
     *
     * @param startChild
     * 		View closest to start of the list. (top or left)
     * @param endChild
     * 		View closest to end of the list (bottom or right)
     */
    static int computeScrollRange(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.OrientationHelper orientation, android.view.View startChild, android.view.View endChild, android.support.v7.widget.RecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if ((((lm.getChildCount() == 0) || (state.getItemCount() == 0)) || (startChild == null)) || (endChild == null)) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return state.getItemCount();
        }
        // smooth scrollbar enabled. try to estimate better.
        final int laidOutArea = orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild);
        final int laidOutRange = java.lang.Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        // estimate a size for full list.
        return ((int) ((((float) (laidOutArea)) / laidOutRange) * state.getItemCount()));
    }
}

