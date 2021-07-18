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
 * Maintains Window Alignment information of two axis.
 */
class WindowAlignment {
    /**
     * Maintains alignment information in one direction.
     */
    public static class Axis {
        /**
         * mScrollCenter is used to calculate dynamic transformation based on how far a view
         * is from the mScrollCenter. For example, the views with center close to mScrollCenter
         * will be scaled up.
         */
        private float mScrollCenter;

        /**
         * Right or bottom edge of last child.
         */
        private int mMaxEdge;

        /**
         * Left or top edge of first child, typically should be zero.
         */
        private int mMinEdge;

        /**
         * Max Scroll value
         */
        private int mMaxScroll;

        /**
         * Min Scroll value
         */
        private int mMinScroll;

        private int mWindowAlignment = android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_BOTH_EDGE;

        private int mWindowAlignmentOffset = 0;

        private float mWindowAlignmentOffsetPercent = 50.0F;

        private int mSize;

        private int mPaddingLow;

        private int mPaddingHigh;

        private boolean mReversedFlow;

        private java.lang.String mName;// for debugging


        public Axis(java.lang.String name) {
            reset();
            mName = name;
        }

        public final int getWindowAlignment() {
            return mWindowAlignment;
        }

        public final void setWindowAlignment(int windowAlignment) {
            mWindowAlignment = windowAlignment;
        }

        public final int getWindowAlignmentOffset() {
            return mWindowAlignmentOffset;
        }

        public final void setWindowAlignmentOffset(int offset) {
            mWindowAlignmentOffset = offset;
        }

        public final void setWindowAlignmentOffsetPercent(float percent) {
            if (((percent < 0) || (percent > 100)) && (percent != android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED)) {
                throw new java.lang.IllegalArgumentException();
            }
            mWindowAlignmentOffsetPercent = percent;
        }

        public final float getWindowAlignmentOffsetPercent() {
            return mWindowAlignmentOffsetPercent;
        }

        public final int getScrollCenter() {
            return ((int) (mScrollCenter));
        }

        /**
         * set minEdge,  Integer.MIN_VALUE means unknown
         */
        public final void setMinEdge(int minEdge) {
            mMinEdge = minEdge;
        }

        public final int getMinEdge() {
            return mMinEdge;
        }

        /**
         * set minScroll,  Integer.MIN_VALUE means unknown
         */
        public final void setMinScroll(int minScroll) {
            mMinScroll = minScroll;
        }

        public final int getMinScroll() {
            return mMinScroll;
        }

        public final void invalidateScrollMin() {
            mMinEdge = java.lang.Integer.MIN_VALUE;
            mMinScroll = java.lang.Integer.MIN_VALUE;
        }

        /**
         * update max edge,  Integer.MAX_VALUE means unknown
         */
        public final void setMaxEdge(int maxEdge) {
            mMaxEdge = maxEdge;
        }

        public final int getMaxEdge() {
            return mMaxEdge;
        }

        /**
         * update max scroll,  Integer.MAX_VALUE means unknown
         */
        public final void setMaxScroll(int maxScroll) {
            mMaxScroll = maxScroll;
        }

        public final int getMaxScroll() {
            return mMaxScroll;
        }

        public final void invalidateScrollMax() {
            mMaxEdge = java.lang.Integer.MAX_VALUE;
            mMaxScroll = java.lang.Integer.MAX_VALUE;
        }

        public final float updateScrollCenter(float scrollTarget) {
            mScrollCenter = scrollTarget;
            return scrollTarget;
        }

        void reset() {
            mScrollCenter = java.lang.Integer.MIN_VALUE;
            mMinEdge = java.lang.Integer.MIN_VALUE;
            mMaxEdge = java.lang.Integer.MAX_VALUE;
        }

        public final boolean isMinUnknown() {
            return mMinEdge == java.lang.Integer.MIN_VALUE;
        }

        public final boolean isMaxUnknown() {
            return mMaxEdge == java.lang.Integer.MAX_VALUE;
        }

        public final void setSize(int size) {
            mSize = size;
        }

        public final int getSize() {
            return mSize;
        }

        public final void setPadding(int paddingLow, int paddingHigh) {
            mPaddingLow = paddingLow;
            mPaddingHigh = paddingHigh;
        }

        public final int getPaddingLow() {
            return mPaddingLow;
        }

        public final int getPaddingHigh() {
            return mPaddingHigh;
        }

        public final int getClientSize() {
            return (mSize - mPaddingLow) - mPaddingHigh;
        }

        public final int getSystemScrollPos(boolean isAtMin, boolean isAtMax) {
            return getSystemScrollPos(((int) (mScrollCenter)), isAtMin, isAtMax);
        }

        public final int getSystemScrollPos(int scrollCenter, boolean isAtMin, boolean isAtMax) {
            int middlePosition;
            if (!mReversedFlow) {
                if (mWindowAlignmentOffset >= 0) {
                    middlePosition = mWindowAlignmentOffset - mPaddingLow;
                } else {
                    middlePosition = (mSize + mWindowAlignmentOffset) - mPaddingLow;
                }
                if (mWindowAlignmentOffsetPercent != android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED) {
                    middlePosition += ((int) ((mSize * mWindowAlignmentOffsetPercent) / 100));
                }
            } else {
                if (mWindowAlignmentOffset >= 0) {
                    middlePosition = (mSize - mWindowAlignmentOffset) - mPaddingLow;
                } else {
                    middlePosition = (-mWindowAlignmentOffset) - mPaddingLow;
                }
                if (mWindowAlignmentOffsetPercent != android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED) {
                    middlePosition -= ((int) ((mSize * mWindowAlignmentOffsetPercent) / 100));
                }
            }
            int clientSize = getClientSize();
            int afterMiddlePosition = clientSize - middlePosition;
            boolean isMinUnknown = isMinUnknown();
            boolean isMaxUnknown = isMaxUnknown();
            if (((!isMinUnknown) && (!isMaxUnknown)) && ((mWindowAlignment & android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_BOTH_EDGE) == android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_BOTH_EDGE)) {
                if ((mMaxEdge - mMinEdge) <= clientSize) {
                    // total children size is less than view port and we want to align
                    // both edge:  align first child to start edge of view port
                    return mReversedFlow ? (mMaxEdge - mPaddingLow) - clientSize : mMinEdge - mPaddingLow;
                }
            }
            if (!isMinUnknown) {
                if ((!mReversedFlow ? (mWindowAlignment & android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_LOW_EDGE) != 0 : (mWindowAlignment & android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_HIGH_EDGE) != 0) && (isAtMin || ((scrollCenter - mMinEdge) <= middlePosition))) {
                    // scroll center is within half of view port size: align the start edge
                    // of first child to the start edge of view port
                    return mMinEdge - mPaddingLow;
                }
            }
            if (!isMaxUnknown) {
                if ((!mReversedFlow ? (mWindowAlignment & android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_HIGH_EDGE) != 0 : (mWindowAlignment & android.support.v17.leanback.widget.BaseGridView.WINDOW_ALIGN_LOW_EDGE) != 0) && (isAtMax || ((mMaxEdge - scrollCenter) <= afterMiddlePosition))) {
                    // scroll center is very close to the end edge of view port : align the
                    // end edge of last children (plus expanded size) to view port's end
                    return (mMaxEdge - mPaddingLow) - clientSize;
                }
            }
            // else put scroll center in middle of view port
            return (scrollCenter - middlePosition) - mPaddingLow;
        }

        public final void setReversedFlow(boolean reversedFlow) {
            mReversedFlow = reversedFlow;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((("center: " + mScrollCenter) + " min:") + mMinEdge) + " max:") + mMaxEdge;
        }
    }

    private int mOrientation = android.support.v7.widget.RecyclerView.HORIZONTAL;

    public final android.support.v17.leanback.widget.WindowAlignment.Axis vertical = new android.support.v17.leanback.widget.WindowAlignment.Axis("vertical");

    public final android.support.v17.leanback.widget.WindowAlignment.Axis horizontal = new android.support.v17.leanback.widget.WindowAlignment.Axis("horizontal");

    private android.support.v17.leanback.widget.WindowAlignment.Axis mMainAxis = horizontal;

    private android.support.v17.leanback.widget.WindowAlignment.Axis mSecondAxis = vertical;

    public final android.support.v17.leanback.widget.WindowAlignment.Axis mainAxis() {
        return mMainAxis;
    }

    public final android.support.v17.leanback.widget.WindowAlignment.Axis secondAxis() {
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

    public final void reset() {
        mainAxis().reset();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuffer().append("horizontal=").append(horizontal.toString()).append("; vertical=").append(vertical.toString()).toString();
    }
}

