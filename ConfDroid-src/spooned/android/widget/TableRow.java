/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * <p>A layout that arranges its children horizontally. A TableRow should
 * always be used as a child of a {@link android.widget.TableLayout}. If a
 * TableRow's parent is not a TableLayout, the TableRow will behave as
 * an horizontal {@link android.widget.LinearLayout}.</p>
 *
 * <p>The children of a TableRow do not need to specify the
 * <code>layout_width</code> and <code>layout_height</code> attributes in the
 * XML file. TableRow always enforces those values to be respectively
 * {@link android.widget.TableLayout.LayoutParams#MATCH_PARENT} and
 * {@link android.widget.TableLayout.LayoutParams#WRAP_CONTENT}.</p>
 *
 * <p>
 * Also see {@link TableRow.LayoutParams android.widget.TableRow.LayoutParams}
 * for layout attributes </p>
 */
public class TableRow extends android.widget.LinearLayout {
    private int mNumColumns = 0;

    private int[] mColumnWidths;

    private int[] mConstrainedColumnWidths;

    private android.util.SparseIntArray mColumnToChildIndex;

    private android.widget.TableRow.ChildrenTracker mChildrenTracker;

    /**
     * <p>Creates a new TableRow for the given context.</p>
     *
     * @param context
     * 		the application environment
     */
    public TableRow(android.content.Context context) {
        super(context);
        initTableRow();
    }

    /**
     * <p>Creates a new TableRow for the given context and with the
     * specified set attributes.</p>
     *
     * @param context
     * 		the application environment
     * @param attrs
     * 		a collection of attributes
     */
    public TableRow(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        initTableRow();
    }

    private void initTableRow() {
        android.view.ViewGroup.OnHierarchyChangeListener oldListener = mOnHierarchyChangeListener;
        mChildrenTracker = new android.widget.TableRow.ChildrenTracker();
        if (oldListener != null) {
            mChildrenTracker.setOnHierarchyChangeListener(oldListener);
        }
        super.setOnHierarchyChangeListener(mChildrenTracker);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener) {
        mChildrenTracker.setOnHierarchyChangeListener(listener);
    }

    /**
     * <p>Collapses or restores a given column.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @param collapsed
     * 		true if the column must be collapsed, false otherwise
     * 		{@hide }
     */
    void setColumnCollapsed(int columnIndex, boolean collapsed) {
        final android.view.View child = getVirtualChildAt(columnIndex);
        if (child != null) {
            child.setVisibility(collapsed ? android.view.View.GONE : android.view.View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // enforce horizontal layout
        measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // enforce horizontal layout
        layoutHorizontal(l, t, r, b);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.view.View getVirtualChildAt(int i) {
        if (mColumnToChildIndex == null) {
            mapIndexAndColumns();
        }
        final int deflectedIndex = mColumnToChildIndex.get(i, -1);
        if (deflectedIndex != (-1)) {
            return getChildAt(deflectedIndex);
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public int getVirtualChildCount() {
        if (mColumnToChildIndex == null) {
            mapIndexAndColumns();
        }
        return mNumColumns;
    }

    private void mapIndexAndColumns() {
        if (mColumnToChildIndex == null) {
            int virtualCount = 0;
            final int count = getChildCount();
            mColumnToChildIndex = new android.util.SparseIntArray();
            final android.util.SparseIntArray columnToChild = mColumnToChildIndex;
            for (int i = 0; i < count; i++) {
                final android.view.View child = getChildAt(i);
                final android.widget.TableRow.LayoutParams layoutParams = ((android.widget.TableRow.LayoutParams) (child.getLayoutParams()));
                if (layoutParams.column >= virtualCount) {
                    virtualCount = layoutParams.column;
                }
                for (int j = 0; j < layoutParams.span; j++) {
                    columnToChild.put(virtualCount++, i);
                }
            }
            mNumColumns = virtualCount;
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    int measureNullChild(int childIndex) {
        return mConstrainedColumnWidths[childIndex];
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    void measureChildBeforeLayout(android.view.View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (mConstrainedColumnWidths != null) {
            final android.widget.TableRow.LayoutParams lp = ((android.widget.TableRow.LayoutParams) (child.getLayoutParams()));
            int measureMode = android.view.View.MeasureSpec.EXACTLY;
            int columnWidth = 0;
            final int span = lp.span;
            final int[] constrainedColumnWidths = mConstrainedColumnWidths;
            for (int i = 0; i < span; i++) {
                columnWidth += constrainedColumnWidths[childIndex + i];
            }
            final int gravity = lp.gravity;
            final boolean isHorizontalGravity = android.view.Gravity.isHorizontal(gravity);
            if (isHorizontalGravity) {
                measureMode = android.view.View.MeasureSpec.AT_MOST;
            }
            // no need to care about padding here,
            // ViewGroup.getChildMeasureSpec() would get rid of it anyway
            // because of the EXACTLY measure spec we use
            int childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.max(0, (columnWidth - lp.leftMargin) - lp.rightMargin), measureMode);
            int childHeightMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(heightMeasureSpec, (((mPaddingTop + mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + totalHeight, lp.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            if (isHorizontalGravity) {
                final int childWidth = child.getMeasuredWidth();
                lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION_NEXT] = columnWidth - childWidth;
                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = android.view.Gravity.getAbsoluteGravity(gravity, layoutDirection);
                switch (absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case android.view.Gravity.LEFT :
                        // don't offset on X axis
                        break;
                    case android.view.Gravity.RIGHT :
                        lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION] = lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION_NEXT];
                        break;
                    case android.view.Gravity.CENTER_HORIZONTAL :
                        lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION] = lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION_NEXT] / 2;
                        break;
                }
            } else {
                lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION] = lp.mOffset[android.widget.TableRow.LayoutParams.LOCATION_NEXT] = 0;
            }
        } else {
            // fail silently when column widths are not available
            super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    int getChildrenSkipCount(android.view.View child, int index) {
        android.widget.TableRow.LayoutParams layoutParams = ((android.widget.TableRow.LayoutParams) (child.getLayoutParams()));
        // when the span is 1 (default), we need to skip 0 child
        return layoutParams.span - 1;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    int getLocationOffset(android.view.View child) {
        return ((android.widget.TableRow.LayoutParams) (child.getLayoutParams())).mOffset[android.widget.TableRow.LayoutParams.LOCATION];
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    int getNextLocationOffset(android.view.View child) {
        return ((android.widget.TableRow.LayoutParams) (child.getLayoutParams())).mOffset[android.widget.TableRow.LayoutParams.LOCATION_NEXT];
    }

    /**
     * <p>Measures the preferred width of each child, including its margins.</p>
     *
     * @param widthMeasureSpec
     * 		the width constraint imposed by our parent
     * @return an array of integers corresponding to the width of each cell, or
    column, in this row
    {@hide }
     */
    int[] getColumnsWidths(int widthMeasureSpec, int heightMeasureSpec) {
        final int numColumns = getVirtualChildCount();
        if ((mColumnWidths == null) || (numColumns != mColumnWidths.length)) {
            mColumnWidths = new int[numColumns];
        }
        final int[] columnWidths = mColumnWidths;
        for (int i = 0; i < numColumns; i++) {
            final android.view.View child = getVirtualChildAt(i);
            if ((child != null) && (child.getVisibility() != android.view.View.GONE)) {
                final android.widget.TableRow.LayoutParams layoutParams = ((android.widget.TableRow.LayoutParams) (child.getLayoutParams()));
                if (layoutParams.span == 1) {
                    int spec;
                    switch (layoutParams.width) {
                        case android.widget.TableRow.LayoutParams.WRAP_CONTENT :
                            spec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
                            break;
                        case android.widget.TableRow.LayoutParams.MATCH_PARENT :
                            spec = android.view.View.MeasureSpec.makeSafeMeasureSpec(android.view.View.MeasureSpec.getSize(heightMeasureSpec), android.view.View.MeasureSpec.UNSPECIFIED);
                            break;
                        default :
                            spec = android.view.View.MeasureSpec.makeMeasureSpec(layoutParams.width, android.view.View.MeasureSpec.EXACTLY);
                    }
                    child.measure(spec, spec);
                    final int width = (child.getMeasuredWidth() + layoutParams.leftMargin) + layoutParams.rightMargin;
                    columnWidths[i] = width;
                } else {
                    columnWidths[i] = 0;
                }
            } else {
                columnWidths[i] = 0;
            }
        }
        return columnWidths;
    }

    /**
     * <p>Sets the width of all of the columns in this row. At layout time,
     * this row sets a fixed width, as defined by <code>columnWidths</code>,
     * on each child (or cell, or column.)</p>
     *
     * @param columnWidths
     * 		the fixed width of each column that this row must
     * 		honor
     * @throws IllegalArgumentException
     * 		when columnWidths' length is smaller
     * 		than the number of children in this row
     * 		{@hide }
     */
    void setColumnsWidthConstraints(int[] columnWidths) {
        if ((columnWidths == null) || (columnWidths.length < getVirtualChildCount())) {
            throw new java.lang.IllegalArgumentException("columnWidths should be >= getVirtualChildCount()");
        }
        mConstrainedColumnWidths = columnWidths;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.widget.TableRow.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.TableRow.LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT},
     * a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and no spanning.
     */
    @java.lang.Override
    protected android.widget.LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.widget.TableRow.LayoutParams();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.TableRow.LayoutParams;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected android.widget.LinearLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new android.widget.TableRow.LayoutParams(p);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.TableRow.class.getName();
    }

    /**
     * <p>Set of layout parameters used in table rows.</p>
     *
     * @see android.widget.TableLayout.LayoutParams
     * @unknown ref android.R.styleable#TableRow_Cell_layout_column
     * @unknown ref android.R.styleable#TableRow_Cell_layout_span
     */
    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        /**
         * <p>The column index of the cell represented by the widget.</p>
         */
        @android.view.ViewDebug.ExportedProperty(category = "layout")
        @android.view.inspector.InspectableProperty(name = "layout_column")
        public int column;

        /**
         * <p>The number of columns the widgets spans over.</p>
         */
        @android.view.ViewDebug.ExportedProperty(category = "layout")
        @android.view.inspector.InspectableProperty(name = "layout_span")
        public int span;

        private static final int LOCATION = 0;

        private static final int LOCATION_NEXT = 1;

        private int[] mOffset = new int[2];

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TableRow_Cell);
            column = a.getInt(com.android.internal.R.styleable.TableRow_Cell_layout_column, -1);
            span = a.getInt(com.android.internal.R.styleable.TableRow_Cell_layout_span, 1);
            if (span <= 1) {
                span = 1;
            }
            a.recycle();
        }

        /**
         * <p>Sets the child width and the child height.</p>
         *
         * @param w
         * 		the desired width
         * @param h
         * 		the desired height
         */
        public LayoutParams(int w, int h) {
            super(w, h);
            column = -1;
            span = 1;
        }

        /**
         * <p>Sets the child width, height and weight.</p>
         *
         * @param w
         * 		the desired width
         * @param h
         * 		the desired height
         * @param initWeight
         * 		the desired weight
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
            column = -1;
            span = 1;
        }

        /**
         * <p>Sets the child width to {@link android.view.ViewGroup.LayoutParams}
         * and the child height to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.</p>
         */
        public LayoutParams() {
            super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            column = -1;
            span = 1;
        }

        /**
         * <p>Puts the view in the specified column.</p>
         *
         * <p>Sets the child width to {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
         * and the child height to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.</p>
         *
         * @param column
         * 		the column index for the view
         */
        public LayoutParams(int column) {
            this();
            this.column = column;
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        @java.lang.Override
        protected void setBaseAttributes(android.content.res.TypedArray a, int widthAttr, int heightAttr) {
            // We don't want to force users to specify a layout_width
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            }
            // We don't want to force users to specify a layout_height
            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        protected void encodeProperties(@android.annotation.NonNull
        android.view.ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:column", column);
            encoder.addProperty("layout:span", span);
        }
    }

    // special transparent hierarchy change listener
    private class ChildrenTracker implements android.view.ViewGroup.OnHierarchyChangeListener {
        private android.view.ViewGroup.OnHierarchyChangeListener listener;

        private void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener) {
            this.listener = listener;
        }

        public void onChildViewAdded(android.view.View parent, android.view.View child) {
            // dirties the index to column map
            mColumnToChildIndex = null;
            if (this.listener != null) {
                this.listener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(android.view.View parent, android.view.View child) {
            // dirties the index to column map
            mColumnToChildIndex = null;
            if (this.listener != null) {
                this.listener.onChildViewRemoved(parent, child);
            }
        }
    }
}

