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
 * <p>A layout that arranges its children into rows and columns.
 * A TableLayout consists of a number of {@link android.widget.TableRow} objects,
 * each defining a row (actually, you can have other children, which will be
 * explained below). TableLayout containers do not display border lines for
 * their rows, columns, or cells. Each row has zero or more cells; each cell can
 * hold one {@link android.view.View View} object. The table has as many columns
 * as the row with the most cells. A table can leave cells empty. Cells can span
 * columns, as they can in HTML.</p>
 *
 * <p>The width of a column is defined by the row with the widest cell in that
 * column. However, a TableLayout can specify certain columns as shrinkable or
 * stretchable by calling
 * {@link #setColumnShrinkable(int, boolean) setColumnShrinkable()}
 * or {@link #setColumnStretchable(int, boolean) setColumnStretchable()}. If
 * marked as shrinkable, the column width can be shrunk to fit the table into
 * its parent object. If marked as stretchable, it can expand in width to fit
 * any extra space. The total width of the table is defined by its parent
 * container. It is important to remember that a column can be both shrinkable
 * and stretchable. In such a situation, the column will change its size to
 * always use up the available space, but never more. Finally, you can hide a
 * column by calling
 * {@link #setColumnCollapsed(int,boolean) setColumnCollapsed()}.</p>
 *
 * <p>The children of a TableLayout cannot specify the <code>layout_width</code>
 * attribute. Width is always <code>MATCH_PARENT</code>. However, the
 * <code>layout_height</code> attribute can be defined by a child; default value
 * is {@link android.widget.TableLayout.LayoutParams#WRAP_CONTENT}. If the child
 * is a {@link android.widget.TableRow}, then the height is always
 * {@link android.widget.TableLayout.LayoutParams#WRAP_CONTENT}.</p>
 *
 * <p> Cells must be added to a row in increasing column order, both in code and
 * XML. Column numbers are zero-based. If you don't specify a column number for
 * a child cell, it will autoincrement to the next available column. If you skip
 * a column number, it will be considered an empty cell in that row. See the
 * TableLayout examples in ApiDemos for examples of creating tables in XML.</p>
 *
 * <p>Although the typical child of a TableLayout is a TableRow, you can
 * actually use any View subclass as a direct child of TableLayout. The View
 * will be displayed as a single row that spans all the table columns.</p>
 */
public class TableLayout extends android.widget.LinearLayout {
    private int[] mMaxWidths;

    private android.util.SparseBooleanArray mStretchableColumns;

    private android.util.SparseBooleanArray mShrinkableColumns;

    private android.util.SparseBooleanArray mCollapsedColumns;

    private boolean mShrinkAllColumns;

    private boolean mStretchAllColumns;

    private android.widget.TableLayout.PassThroughHierarchyChangeListener mPassThroughListener;

    private boolean mInitialized;

    /**
     * <p>Creates a new TableLayout for the given context.</p>
     *
     * @param context
     * 		the application environment
     */
    public TableLayout(android.content.Context context) {
        super(context);
        initTableLayout();
    }

    /**
     * <p>Creates a new TableLayout for the given context and with the
     * specified set attributes.</p>
     *
     * @param context
     * 		the application environment
     * @param attrs
     * 		a collection of attributes
     */
    public TableLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableLayout);
        java.lang.String stretchedColumns = a.getString(R.styleable.TableLayout_stretchColumns);
        if (stretchedColumns != null) {
            if (stretchedColumns.charAt(0) == '*') {
                mStretchAllColumns = true;
            } else {
                mStretchableColumns = android.widget.TableLayout.parseColumns(stretchedColumns);
            }
        }
        java.lang.String shrinkedColumns = a.getString(R.styleable.TableLayout_shrinkColumns);
        if (shrinkedColumns != null) {
            if (shrinkedColumns.charAt(0) == '*') {
                mShrinkAllColumns = true;
            } else {
                mShrinkableColumns = android.widget.TableLayout.parseColumns(shrinkedColumns);
            }
        }
        java.lang.String collapsedColumns = a.getString(R.styleable.TableLayout_collapseColumns);
        if (collapsedColumns != null) {
            mCollapsedColumns = android.widget.TableLayout.parseColumns(collapsedColumns);
        }
        a.recycle();
        initTableLayout();
    }

    /**
     * <p>Parses a sequence of columns ids defined in a CharSequence with the
     * following pattern (regex): \d+(\s*,\s*\d+)*</p>
     *
     * <p>Examples: "1" or "13, 7, 6" or "".</p>
     *
     * <p>The result of the parsing is stored in a sparse boolean array. The
     * parsed column ids are used as the keys of the sparse array. The values
     * are always true.</p>
     *
     * @param sequence
     * 		a sequence of column ids, can be empty but not null
     * @return a sparse array of boolean mapping column indexes to the columns
    collapse state
     */
    private static android.util.SparseBooleanArray parseColumns(java.lang.String sequence) {
        android.util.SparseBooleanArray columns = new android.util.SparseBooleanArray();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\s*,\\s*");
        java.lang.String[] columnDefs = pattern.split(sequence);
        for (java.lang.String columnIdentifier : columnDefs) {
            try {
                int columnIndex = java.lang.Integer.parseInt(columnIdentifier);
                // only valid, i.e. positive, columns indexes are handled
                if (columnIndex >= 0) {
                    // putting true in this sparse array indicates that the
                    // column index was defined in the XML file
                    columns.put(columnIndex, true);
                }
            } catch (java.lang.NumberFormatException e) {
                // we just ignore columns that don't exist
            }
        }
        return columns;
    }

    /**
     * <p>Performs initialization common to prorgrammatic use and XML use of
     * this widget.</p>
     */
    private void initTableLayout() {
        if (mCollapsedColumns == null) {
            mCollapsedColumns = new android.util.SparseBooleanArray();
        }
        if (mStretchableColumns == null) {
            mStretchableColumns = new android.util.SparseBooleanArray();
        }
        if (mShrinkableColumns == null) {
            mShrinkableColumns = new android.util.SparseBooleanArray();
        }
        // TableLayouts are always in vertical orientation; keep this tracked
        // for shared LinearLayout code.
        setOrientation(android.widget.LinearLayout.VERTICAL);
        mPassThroughListener = new android.widget.TableLayout.PassThroughHierarchyChangeListener();
        // make sure to call the parent class method to avoid potential
        // infinite loops
        super.setOnHierarchyChangeListener(mPassThroughListener);
        mInitialized = true;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    private void requestRowsLayout() {
        if (mInitialized) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).requestLayout();
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void requestLayout() {
        if (mInitialized) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).forceLayout();
            }
        }
        super.requestLayout();
    }

    /**
     * <p>Indicates whether all columns are shrinkable or not.</p>
     *
     * @return true if all columns are shrinkable, false otherwise
     * @unknown ref android.R.styleable#TableLayout_shrinkColumns
     */
    public boolean isShrinkAllColumns() {
        return mShrinkAllColumns;
    }

    /**
     * <p>Convenience method to mark all columns as shrinkable.</p>
     *
     * @param shrinkAllColumns
     * 		true to mark all columns shrinkable
     * @unknown ref android.R.styleable#TableLayout_shrinkColumns
     */
    public void setShrinkAllColumns(boolean shrinkAllColumns) {
        mShrinkAllColumns = shrinkAllColumns;
    }

    /**
     * <p>Indicates whether all columns are stretchable or not.</p>
     *
     * @return true if all columns are stretchable, false otherwise
     * @unknown ref android.R.styleable#TableLayout_stretchColumns
     */
    public boolean isStretchAllColumns() {
        return mStretchAllColumns;
    }

    /**
     * <p>Convenience method to mark all columns as stretchable.</p>
     *
     * @param stretchAllColumns
     * 		true to mark all columns stretchable
     * @unknown ref android.R.styleable#TableLayout_stretchColumns
     */
    public void setStretchAllColumns(boolean stretchAllColumns) {
        mStretchAllColumns = stretchAllColumns;
    }

    /**
     * <p>Collapses or restores a given column. When collapsed, a column
     * does not appear on screen and the extra space is reclaimed by the
     * other columns. A column is collapsed/restored only when it belongs to
     * a {@link android.widget.TableRow}.</p>
     *
     * <p>Calling this method requests a layout operation.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @param isCollapsed
     * 		true if the column must be collapsed, false otherwise
     * @unknown ref android.R.styleable#TableLayout_collapseColumns
     */
    public void setColumnCollapsed(int columnIndex, boolean isCollapsed) {
        // update the collapse status of the column
        mCollapsedColumns.put(columnIndex, isCollapsed);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View view = getChildAt(i);
            if (view instanceof android.widget.TableRow) {
                ((android.widget.TableRow) (view)).setColumnCollapsed(columnIndex, isCollapsed);
            }
        }
        requestRowsLayout();
    }

    /**
     * <p>Returns the collapsed state of the specified column.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @return true if the column is collapsed, false otherwise
     */
    public boolean isColumnCollapsed(int columnIndex) {
        return mCollapsedColumns.get(columnIndex);
    }

    /**
     * <p>Makes the given column stretchable or not. When stretchable, a column
     * takes up as much as available space as possible in its row.</p>
     *
     * <p>Calling this method requests a layout operation.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @param isStretchable
     * 		true if the column must be stretchable,
     * 		false otherwise. Default is false.
     * @unknown ref android.R.styleable#TableLayout_stretchColumns
     */
    public void setColumnStretchable(int columnIndex, boolean isStretchable) {
        mStretchableColumns.put(columnIndex, isStretchable);
        requestRowsLayout();
    }

    /**
     * <p>Returns whether the specified column is stretchable or not.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @return true if the column is stretchable, false otherwise
     */
    public boolean isColumnStretchable(int columnIndex) {
        return mStretchAllColumns || mStretchableColumns.get(columnIndex);
    }

    /**
     * <p>Makes the given column shrinkable or not. When a row is too wide, the
     * table can reclaim extra space from shrinkable columns.</p>
     *
     * <p>Calling this method requests a layout operation.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @param isShrinkable
     * 		true if the column must be shrinkable,
     * 		false otherwise. Default is false.
     * @unknown ref android.R.styleable#TableLayout_shrinkColumns
     */
    public void setColumnShrinkable(int columnIndex, boolean isShrinkable) {
        mShrinkableColumns.put(columnIndex, isShrinkable);
        requestRowsLayout();
    }

    /**
     * <p>Returns whether the specified column is shrinkable or not.</p>
     *
     * @param columnIndex
     * 		the index of the column
     * @return true if the column is shrinkable, false otherwise. Default is false.
     */
    public boolean isColumnShrinkable(int columnIndex) {
        return mShrinkAllColumns || mShrinkableColumns.get(columnIndex);
    }

    /**
     * <p>Applies the columns collapse status to a new row added to this
     * table. This method is invoked by PassThroughHierarchyChangeListener
     * upon child insertion.</p>
     *
     * <p>This method only applies to {@link android.widget.TableRow}
     * instances.</p>
     *
     * @param child
     * 		the newly added child
     */
    private void trackCollapsedColumns(android.view.View child) {
        if (child instanceof android.widget.TableRow) {
            final android.widget.TableRow row = ((android.widget.TableRow) (child));
            final android.util.SparseBooleanArray collapsedColumns = mCollapsedColumns;
            final int count = collapsedColumns.size();
            for (int i = 0; i < count; i++) {
                int columnIndex = collapsedColumns.keyAt(i);
                boolean isCollapsed = collapsedColumns.valueAt(i);
                // the collapse status is set only when the column should be
                // collapsed; otherwise, this might affect the default
                // visibility of the row's children
                if (isCollapsed) {
                    row.setColumnCollapsed(columnIndex, isCollapsed);
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void addView(android.view.View child) {
        super.addView(child);
        requestRowsLayout();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void addView(android.view.View child, int index) {
        super.addView(child, index);
        requestRowsLayout();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void addView(android.view.View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        requestRowsLayout();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        requestRowsLayout();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // enforce vertical layout
        measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // enforce vertical layout
        layoutVertical(l, t, r, b);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    void measureChildBeforeLayout(android.view.View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        // when the measured child is a table row, we force the width of its
        // children with the widths computed in findLargestCells()
        if (child instanceof android.widget.TableRow) {
            ((android.widget.TableRow) (child)).setColumnsWidthConstraints(mMaxWidths);
        }
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        findLargestCells(widthMeasureSpec, heightMeasureSpec);
        shrinkAndStretchColumns(widthMeasureSpec);
        super.measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * <p>Finds the largest cell in each column. For each column, the width of
     * the largest cell is applied to all the other cells.</p>
     *
     * @param widthMeasureSpec
     * 		the measure constraint imposed by our parent
     */
    private void findLargestCells(int widthMeasureSpec, int heightMeasureSpec) {
        boolean firstRow = true;
        // find the maximum width for each column
        // the total number of columns is dynamically changed if we find
        // wider rows as we go through the children
        // the array is reused for each layout operation; the array can grow
        // but never shrinks. Unused extra cells in the array are just ignored
        // this behavior avoids to unnecessary grow the array after the first
        // layout operation
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            if (child instanceof android.widget.TableRow) {
                final android.widget.TableRow row = ((android.widget.TableRow) (child));
                // forces the row's height
                final android.view.ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
                layoutParams.height = android.widget.TableLayout.LayoutParams.WRAP_CONTENT;
                final int[] widths = row.getColumnsWidths(widthMeasureSpec, heightMeasureSpec);
                final int newLength = widths.length;
                // this is the first row, we just need to copy the values
                if (firstRow) {
                    if ((mMaxWidths == null) || (mMaxWidths.length != newLength)) {
                        mMaxWidths = new int[newLength];
                    }
                    java.lang.System.arraycopy(widths, 0, mMaxWidths, 0, newLength);
                    firstRow = false;
                } else {
                    int length = mMaxWidths.length;
                    final int difference = newLength - length;
                    // the current row is wider than the previous rows, so
                    // we just grow the array and copy the values
                    if (difference > 0) {
                        final int[] oldMaxWidths = mMaxWidths;
                        mMaxWidths = new int[newLength];
                        java.lang.System.arraycopy(oldMaxWidths, 0, mMaxWidths, 0, oldMaxWidths.length);
                        java.lang.System.arraycopy(widths, oldMaxWidths.length, mMaxWidths, oldMaxWidths.length, difference);
                    }
                    // the row is narrower or of the same width as the previous
                    // rows, so we find the maximum width for each column
                    // if the row is narrower than the previous ones,
                    // difference will be negative
                    final int[] maxWidths = mMaxWidths;
                    length = java.lang.Math.min(length, newLength);
                    for (int j = 0; j < length; j++) {
                        maxWidths[j] = java.lang.Math.max(maxWidths[j], widths[j]);
                    }
                }
            }
        }
    }

    /**
     * <p>Shrinks the columns if their total width is greater than the
     * width allocated by widthMeasureSpec. When the total width is less
     * than the allocated width, this method attempts to stretch columns
     * to fill the remaining space.</p>
     *
     * @param widthMeasureSpec
     * 		the width measure specification as indicated
     * 		by this widget's parent
     */
    private void shrinkAndStretchColumns(int widthMeasureSpec) {
        // when we have no row, mMaxWidths is not initialized and the loop
        // below could cause a NPE
        if (mMaxWidths == null) {
            return;
        }
        // should we honor AT_MOST, EXACTLY and UNSPECIFIED?
        int totalWidth = 0;
        for (int width : mMaxWidths) {
            totalWidth += width;
        }
        int size = (android.view.View.MeasureSpec.getSize(widthMeasureSpec) - mPaddingLeft) - mPaddingRight;
        if ((totalWidth > size) && (mShrinkAllColumns || (mShrinkableColumns.size() > 0))) {
            // oops, the largest columns are wider than the row itself
            // fairly redistribute the row's width among the columns
            mutateColumnsWidth(mShrinkableColumns, mShrinkAllColumns, size, totalWidth);
        } else
            if ((totalWidth < size) && (mStretchAllColumns || (mStretchableColumns.size() > 0))) {
                // if we have some space left, we distribute it among the
                // expandable columns
                mutateColumnsWidth(mStretchableColumns, mStretchAllColumns, size, totalWidth);
            }

    }

    private void mutateColumnsWidth(android.util.SparseBooleanArray columns, boolean allColumns, int size, int totalWidth) {
        int skipped = 0;
        final int[] maxWidths = mMaxWidths;
        final int length = maxWidths.length;
        final int count = (allColumns) ? length : columns.size();
        final int totalExtraSpace = size - totalWidth;
        int extraSpace = totalExtraSpace / count;
        // Column's widths are changed: force child table rows to re-measure.
        // (done by super.measureVertical after shrinkAndStretchColumns.)
        final int nbChildren = getChildCount();
        for (int i = 0; i < nbChildren; i++) {
            android.view.View child = getChildAt(i);
            if (child instanceof android.widget.TableRow) {
                child.forceLayout();
            }
        }
        if (!allColumns) {
            for (int i = 0; i < count; i++) {
                int column = columns.keyAt(i);
                if (columns.valueAt(i)) {
                    if (column < length) {
                        maxWidths[column] += extraSpace;
                    } else {
                        skipped++;
                    }
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                maxWidths[i] += extraSpace;
            }
            // we don't skip any column so we can return right away
            return;
        }
        if ((skipped > 0) && (skipped < count)) {
            // reclaim any extra space we left to columns that don't exist
            extraSpace = (skipped * extraSpace) / (count - skipped);
            for (int i = 0; i < count; i++) {
                int column = columns.keyAt(i);
                if (columns.valueAt(i) && (column < length)) {
                    if (extraSpace > maxWidths[column]) {
                        maxWidths[column] = 0;
                    } else {
                        maxWidths[column] += extraSpace;
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public android.widget.TableLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.TableLayout.LayoutParams(getContext(), attrs);
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT},
     * and a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.
     */
    @java.lang.Override
    protected android.widget.LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.widget.TableLayout.LayoutParams();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.TableLayout.LayoutParams;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    protected android.widget.LinearLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new android.widget.TableLayout.LayoutParams(p);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.TableLayout.class.getName();
    }

    /**
     * <p>This set of layout parameters enforces the width of each child to be
     * {@link #MATCH_PARENT} and the height of each child to be
     * {@link #WRAP_CONTENT}, but only if the height is not specified.</p>
     */
    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int w, int h) {
            super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, h);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, h, initWeight);
        }

        /**
         * <p>Sets the child width to
         * {@link android.view.ViewGroup.LayoutParams} and the child height to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}.</p>
         */
        public LayoutParams() {
            super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
            width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        }

        /**
         * {@inheritDoc }
         */
        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
            width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            if (source instanceof android.widget.TableLayout.LayoutParams) {
                weight = ((android.widget.TableLayout.LayoutParams) (source)).weight;
            }
        }

        /**
         * <p>Fixes the row's width to
         * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}; the row's
         * height is fixed to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} if no layout
         * height is specified.</p>
         *
         * @param a
         * 		the styled attributes set
         * @param widthAttr
         * 		the width attribute to fetch
         * @param heightAttr
         * 		the height attribute to fetch
         */
        @java.lang.Override
        protected void setBaseAttributes(android.content.res.TypedArray a, int widthAttr, int heightAttr) {
            this.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            if (a.hasValue(heightAttr)) {
                this.height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                this.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements android.view.ViewGroup.OnHierarchyChangeListener {
        private android.view.ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc }
         */
        public void onChildViewAdded(android.view.View parent, android.view.View child) {
            trackCollapsedColumns(child);
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc }
         */
        public void onChildViewRemoved(android.view.View parent, android.view.View child) {
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}

