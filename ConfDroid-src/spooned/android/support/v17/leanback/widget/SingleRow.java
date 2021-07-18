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
 * A Grid with restriction to single row.
 */
class SingleRow extends android.support.v17.leanback.widget.Grid {
    private final android.support.v17.leanback.widget.Grid.Location mTmpLocation = new android.support.v17.leanback.widget.Grid.Location(0);

    private java.lang.Object[] mTmpItem = new java.lang.Object[1];

    SingleRow() {
        setNumRows(1);
    }

    @java.lang.Override
    public final android.support.v17.leanback.widget.Grid.Location getLocation(int index) {
        // all items are on row 0, share the same Location object.
        return mTmpLocation;
    }

    @java.lang.Override
    public final void debugPrint(java.io.PrintWriter pw) {
        pw.print("SingleRow<");
        pw.print(mFirstVisibleIndex);
        pw.print(",");
        pw.print(mLastVisibleIndex);
        pw.print(">");
        pw.println();
    }

    int getStartIndexForAppend() {
        if (mLastVisibleIndex >= 0) {
            return mLastVisibleIndex + 1;
        } else
            if (mStartIndex != android.support.v17.leanback.widget.Grid.START_DEFAULT) {
                return java.lang.Math.min(mStartIndex, mProvider.getCount() - 1);
            } else {
                return 0;
            }

    }

    int getStartIndexForPrepend() {
        if (mFirstVisibleIndex >= 0) {
            return mFirstVisibleIndex - 1;
        } else
            if (mStartIndex != android.support.v17.leanback.widget.Grid.START_DEFAULT) {
                return java.lang.Math.min(mStartIndex, mProvider.getCount() - 1);
            } else {
                return mProvider.getCount() - 1;
            }

    }

    @java.lang.Override
    protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode) {
        if (mProvider.getCount() == 0) {
            return false;
        }
        if ((!oneColumnMode) && checkPrependOverLimit(toLimit)) {
            return false;
        }
        boolean filledOne = false;
        for (int index = getStartIndexForPrepend(); index >= 0; index--) {
            int size = mProvider.createItem(index, false, mTmpItem);
            int edge;
            if ((mFirstVisibleIndex < 0) || (mLastVisibleIndex < 0)) {
                edge = (mReversedFlow) ? java.lang.Integer.MIN_VALUE : java.lang.Integer.MAX_VALUE;
                mLastVisibleIndex = mFirstVisibleIndex = index;
            } else {
                if (mReversedFlow) {
                    edge = (mProvider.getEdge(index + 1) + mMargin) + size;
                } else {
                    edge = (mProvider.getEdge(index + 1) - mMargin) - size;
                }
                mFirstVisibleIndex = index;
            }
            mProvider.addItem(mTmpItem[0], index, size, 0, edge);
            filledOne = true;
            if (oneColumnMode || checkPrependOverLimit(toLimit)) {
                break;
            }
        }
        return filledOne;
    }

    @java.lang.Override
    protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode) {
        if (mProvider.getCount() == 0) {
            return false;
        }
        if ((!oneColumnMode) && checkAppendOverLimit(toLimit)) {
            // not in one column mode, return immediately if over limit
            return false;
        }
        boolean filledOne = false;
        for (int index = getStartIndexForAppend(); index < mProvider.getCount(); index++) {
            int size = mProvider.createItem(index, true, mTmpItem);
            int edge;
            if ((mFirstVisibleIndex < 0) || (mLastVisibleIndex < 0)) {
                edge = (mReversedFlow) ? java.lang.Integer.MAX_VALUE : java.lang.Integer.MIN_VALUE;
                mLastVisibleIndex = mFirstVisibleIndex = index;
            } else {
                if (mReversedFlow) {
                    edge = (mProvider.getEdge(index - 1) - mProvider.getSize(index - 1)) - mMargin;
                } else {
                    edge = (mProvider.getEdge(index - 1) + mProvider.getSize(index - 1)) + mMargin;
                }
                mLastVisibleIndex = index;
            }
            mProvider.addItem(mTmpItem[0], index, size, 0, edge);
            filledOne = true;
            if (oneColumnMode || checkAppendOverLimit(toLimit)) {
                break;
            }
        }
        return filledOne;
    }

    @java.lang.Override
    public final android.support.v4.util.CircularIntArray[] getItemPositionsInRows(int startPos, int endPos) {
        // all items are on the same row:
        mTmpItemPositionsInRows[0].clear();
        mTmpItemPositionsInRows[0].addLast(startPos);
        mTmpItemPositionsInRows[0].addLast(endPos);
        return mTmpItemPositionsInRows;
    }

    @java.lang.Override
    protected final int findRowMin(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }
        return mReversedFlow ? mProvider.getEdge(indexLimit) - mProvider.getSize(indexLimit) : mProvider.getEdge(indexLimit);
    }

    @java.lang.Override
    protected final int findRowMax(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }
        return mReversedFlow ? mProvider.getEdge(indexLimit) : mProvider.getEdge(indexLimit) + mProvider.getSize(indexLimit);
    }
}

