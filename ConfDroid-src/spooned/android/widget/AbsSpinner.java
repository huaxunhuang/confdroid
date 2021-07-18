/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * An abstract base class for spinner widgets. SDK users will probably not
 * need to use this class.
 *
 * @unknown ref android.R.styleable#AbsSpinner_entries
 */
public abstract class AbsSpinner extends android.widget.AdapterView<android.widget.SpinnerAdapter> {
    private static final java.lang.String LOG_TAG = android.widget.AbsSpinner.class.getSimpleName();

    android.widget.SpinnerAdapter mAdapter;

    int mHeightMeasureSpec;

    int mWidthMeasureSpec;

    int mSelectionLeftPadding = 0;

    int mSelectionTopPadding = 0;

    int mSelectionRightPadding = 0;

    int mSelectionBottomPadding = 0;

    final android.graphics.Rect mSpinnerPadding = new android.graphics.Rect();

    final android.widget.AbsSpinner.RecycleBin mRecycler = new android.widget.AbsSpinner.RecycleBin();

    private android.database.DataSetObserver mDataSetObserver;

    /**
     * Temporary frame to hold a child View's frame rectangle
     */
    private android.graphics.Rect mTouchFrame;

    public AbsSpinner(android.content.Context context) {
        super(context);
        initAbsSpinner();
    }

    public AbsSpinner(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsSpinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsSpinner(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Spinner is important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        initAbsSpinner();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsSpinner, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.AbsSpinner, attrs, a, defStyleAttr, defStyleRes);
        final java.lang.CharSequence[] entries = a.getTextArray(R.styleable.AbsSpinner_entries);
        if (entries != null) {
            final android.widget.ArrayAdapter<java.lang.CharSequence> adapter = new android.widget.ArrayAdapter<java.lang.CharSequence>(context, R.layout.simple_spinner_item, entries);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            setAdapter(adapter);
        }
        a.recycle();
    }

    /**
     * Common code for different constructor flavors
     */
    private void initAbsSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }

    /**
     * The Adapter is used to provide the data which backs this Spinner.
     * It also provides methods to transform spinner items based on their position
     * relative to the selected item.
     *
     * @param adapter
     * 		The SpinnerAdapter to use for this Spinner
     */
    @java.lang.Override
    public void setAdapter(android.widget.SpinnerAdapter adapter) {
        if (null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            resetList();
        }
        mAdapter = adapter;
        mOldSelectedPosition = android.widget.AdapterView.INVALID_POSITION;
        mOldSelectedRowId = android.widget.AdapterView.INVALID_ROW_ID;
        if (mAdapter != null) {
            mOldItemCount = mItemCount;
            mItemCount = mAdapter.getCount();
            checkFocus();
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
            int position = (mItemCount > 0) ? 0 : android.widget.AdapterView.INVALID_POSITION;
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            if (mItemCount == 0) {
                // Nothing selected
                checkSelectionChanged();
            }
        } else {
            checkFocus();
            resetList();
            // Nothing selected
            checkSelectionChanged();
        }
        requestLayout();
    }

    /**
     * Clear out all children from the list
     */
    void resetList() {
        mDataChanged = false;
        mNeedSync = false;
        removeAllViewsInLayout();
        mOldSelectedPosition = android.widget.AdapterView.INVALID_POSITION;
        mOldSelectedRowId = android.widget.AdapterView.INVALID_ROW_ID;
        setSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
        setNextSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
        invalidate();
    }

    /**
     *
     *
     * @see android.view.View#measure(int, int)

    Figure out the dimensions of this Spinner. The width comes from
    the widthMeasureSpec as Spinnners can't have their width set to
    UNSPECIFIED. The height is based on the height of the selected item
    plus padding.
     */
    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        int heightSize;
        mSpinnerPadding.left = (mPaddingLeft > mSelectionLeftPadding) ? mPaddingLeft : mSelectionLeftPadding;
        mSpinnerPadding.top = (mPaddingTop > mSelectionTopPadding) ? mPaddingTop : mSelectionTopPadding;
        mSpinnerPadding.right = (mPaddingRight > mSelectionRightPadding) ? mPaddingRight : mSelectionRightPadding;
        mSpinnerPadding.bottom = (mPaddingBottom > mSelectionBottomPadding) ? mPaddingBottom : mSelectionBottomPadding;
        if (mDataChanged) {
            handleDataChanged();
        }
        int preferredHeight = 0;
        int preferredWidth = 0;
        boolean needsMeasuring = true;
        int selectedPosition = getSelectedItemPosition();
        if (((selectedPosition >= 0) && (mAdapter != null)) && (selectedPosition < mAdapter.getCount())) {
            // Try looking in the recycler. (Maybe we were measured once already)
            android.view.View view = mRecycler.get(selectedPosition);
            if (view == null) {
                // Make a new one
                view = mAdapter.getView(selectedPosition, null, this);
                if (view.getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
                    view.setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                }
            }
            if (view != null) {
                // Put in recycler for re-measuring and/or layout
                mRecycler.put(selectedPosition, view);
                if (view.getLayoutParams() == null) {
                    mBlockLayoutRequests = true;
                    view.setLayoutParams(generateDefaultLayoutParams());
                    mBlockLayoutRequests = false;
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                preferredHeight = (getChildHeight(view) + mSpinnerPadding.top) + mSpinnerPadding.bottom;
                preferredWidth = (getChildWidth(view) + mSpinnerPadding.left) + mSpinnerPadding.right;
                needsMeasuring = false;
            }
        }
        if (needsMeasuring) {
            // No views -- just use padding
            preferredHeight = mSpinnerPadding.top + mSpinnerPadding.bottom;
            if (widthMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                preferredWidth = mSpinnerPadding.left + mSpinnerPadding.right;
            }
        }
        preferredHeight = java.lang.Math.max(preferredHeight, getSuggestedMinimumHeight());
        preferredWidth = java.lang.Math.max(preferredWidth, getSuggestedMinimumWidth());
        heightSize = android.view.View.resolveSizeAndState(preferredHeight, heightMeasureSpec, 0);
        widthSize = android.view.View.resolveSizeAndState(preferredWidth, widthMeasureSpec, 0);
        setMeasuredDimension(widthSize, heightSize);
        mHeightMeasureSpec = heightMeasureSpec;
        mWidthMeasureSpec = widthMeasureSpec;
    }

    int getChildHeight(android.view.View child) {
        return child.getMeasuredHeight();
    }

    int getChildWidth(android.view.View child) {
        return child.getMeasuredWidth();
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    void recycleAllViews() {
        final int childCount = getChildCount();
        final android.widget.AbsSpinner.RecycleBin recycleBin = mRecycler;
        final int position = mFirstPosition;
        // All views go in recycler
        for (int i = 0; i < childCount; i++) {
            android.view.View v = getChildAt(i);
            int index = position + i;
            recycleBin.put(index, v);
        }
    }

    /**
     * Jump directly to a specific item in the adapter data.
     */
    public void setSelection(int position, boolean animate) {
        // Animate only if requested position is already on screen somewhere
        boolean shouldAnimate = (animate && (mFirstPosition <= position)) && (position <= ((mFirstPosition + getChildCount()) - 1));
        setSelectionInt(position, shouldAnimate);
    }

    @java.lang.Override
    public void setSelection(int position) {
        setNextSelectedPositionInt(position);
        requestLayout();
        invalidate();
    }

    /**
     * Makes the item at the supplied position selected.
     *
     * @param position
     * 		Position to select
     * @param animate
     * 		Should the transition be animated
     */
    void setSelectionInt(int position, boolean animate) {
        if (position != mOldSelectedPosition) {
            mBlockLayoutRequests = true;
            int delta = position - mSelectedPosition;
            setNextSelectedPositionInt(position);
            layout(delta, animate);
            mBlockLayoutRequests = false;
        }
    }

    abstract void layout(int delta, boolean animate);

    @java.lang.Override
    public android.view.View getSelectedView() {
        if ((mItemCount > 0) && (mSelectedPosition >= 0)) {
            return getChildAt(mSelectedPosition - mFirstPosition);
        } else {
            return null;
        }
    }

    /**
     * Override to prevent spamming ourselves with layout requests
     * as we place views
     *
     * @see android.view.View#requestLayout()
     */
    @java.lang.Override
    public void requestLayout() {
        if (!mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    @java.lang.Override
    public android.widget.SpinnerAdapter getAdapter() {
        return mAdapter;
    }

    @java.lang.Override
    public int getCount() {
        return mItemCount;
    }

    /**
     * Maps a point to a position in the list.
     *
     * @param x
     * 		X in local coordinate
     * @param y
     * 		Y in local coordinate
     * @return The position of the item which contains the specified point, or
    {@link #INVALID_POSITION} if the point does not intersect an item.
     */
    public int pointToPosition(int x, int y) {
        android.graphics.Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new android.graphics.Rect();
            frame = mTouchFrame;
        }
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return mFirstPosition + i;
                }
            }
        }
        return android.widget.AdapterView.INVALID_POSITION;
    }

    @java.lang.Override
    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        // Restores the selected position when Spinner gets restored,
        // rather than wait until the next measure/layout pass to do it.
        handleDataChanged();
    }

    static class SavedState extends android.view.View.BaseSavedState {
        long selectedId;

        int position;

        /**
         * Constructor called from {@link AbsSpinner#onSaveInstanceState()}
         */
        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        SavedState(android.os.Parcel in) {
            super(in);
            selectedId = in.readLong();
            position = in.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(selectedId);
            out.writeInt(position);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((("AbsSpinner.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " selectedId=") + selectedId) + " position=") + position) + "}";
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.AbsSpinner.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.AbsSpinner.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.AbsSpinner.SavedState ss = new android.widget.AbsSpinner.SavedState(superState);
        ss.selectedId = getSelectedItemId();
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition();
        } else {
            ss.position = android.widget.AdapterView.INVALID_POSITION;
        }
        return ss;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.AbsSpinner.SavedState ss = ((android.widget.AbsSpinner.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.selectedId >= 0) {
            mDataChanged = true;
            mNeedSync = true;
            mSyncRowId = ss.selectedId;
            mSyncPosition = ss.position;
            mSyncMode = android.widget.AdapterView.SYNC_SELECTED_POSITION;
            requestLayout();
        }
    }

    class RecycleBin {
        private final android.util.SparseArray<android.view.View> mScrapHeap = new android.util.SparseArray<android.view.View>();

        public void put(int position, android.view.View v) {
            mScrapHeap.put(position, v);
        }

        android.view.View get(int position) {
            // System.out.print("Looking for " + position);
            android.view.View result = mScrapHeap.get(position);
            if (result != null) {
                // System.out.println(" HIT");
                mScrapHeap.delete(position);
            } else {
                // System.out.println(" MISS");
            }
            return result;
        }

        void clear() {
            final android.util.SparseArray<android.view.View> scrapHeap = mScrapHeap;
            final int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                final android.view.View view = scrapHeap.valueAt(i);
                if (view != null) {
                    removeDetachedView(view, true);
                }
            }
            scrapHeap.clear();
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.AbsSpinner.class.getName();
    }

    @java.lang.Override
    public void autofill(android.view.autofill.AutofillValue value) {
        if (!isEnabled())
            return;

        if (!value.isList()) {
            android.util.Log.w(android.widget.AbsSpinner.LOG_TAG, (value + " could not be autofilled into ") + this);
            return;
        }
        setSelection(value.getListValue());
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return isEnabled() ? android.view.View.AUTOFILL_TYPE_LIST : android.view.View.AUTOFILL_TYPE_NONE;
    }

    @java.lang.Override
    public android.view.autofill.AutofillValue getAutofillValue() {
        return isEnabled() ? android.view.autofill.AutofillValue.forList(getSelectedItemPosition()) : null;
    }
}

