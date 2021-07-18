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
package android.support.v17.leanback.widget.picker;


/**
 * Picker is a widget showing multiple customized {@link PickerColumn}s. The PickerColumns are
 * initialized in {@link #setColumns(List)}. Call {@link #setColumnAt(int, PickerColumn)} if the
 * column value range or labels change. Call {@link #setColumnValue(int, int, boolean)} to update
 * the current value of PickerColumn.
 * <p>
 * Picker has two states and will change height:
 * <li>{@link #isActivated()} is true: Picker shows typically three items vertically (see
 * {@link #getActivatedVisibleItemCount()}}. Columns other than {@link #getSelectedColumn()} still
 * shows one item if the Picker is focused. On a touch screen device, the Picker will not get focus
 * so it always show three items on all columns. On a non-touch device (a TV), the Picker will show
 * three items only on currently activated column. If the Picker has focus, it will intercept DPAD
 * directions and select activated column.
 * <li>{@link #isActivated()} is false: Picker shows one item vertically (see
 * {@link #getVisibleItemCount()}) on all columns. The size of Picker shrinks.
 */
public class Picker extends android.widget.FrameLayout {
    public interface PickerValueListener {
        public void onValueChanged(android.support.v17.leanback.widget.picker.Picker picker, int column);
    }

    private android.view.ViewGroup mRootView;

    private android.view.ViewGroup mPickerView;

    final java.util.List<android.support.v17.leanback.widget.VerticalGridView> mColumnViews = new java.util.ArrayList<android.support.v17.leanback.widget.VerticalGridView>();

    java.util.ArrayList<android.support.v17.leanback.widget.picker.PickerColumn> mColumns;

    private float mUnfocusedAlpha;

    private float mFocusedAlpha;

    private float mVisibleColumnAlpha;

    private float mInvisibleColumnAlpha;

    private int mAlphaAnimDuration;

    private android.view.animation.Interpolator mDecelerateInterpolator;

    private android.view.animation.Interpolator mAccelerateInterpolator;

    private java.util.ArrayList<android.support.v17.leanback.widget.picker.Picker.PickerValueListener> mListeners;

    private float mVisibleItemsActivated = 3;

    private float mVisibleItems = 1;

    private int mSelectedColumn = 0;

    private java.lang.CharSequence mSeparator;

    private int mPickerItemLayoutId = R.layout.lb_picker_item;

    private int mPickerItemTextViewId = 0;

    /**
     * Gets separator string between columns.
     */
    public final java.lang.CharSequence getSeparator() {
        return mSeparator;
    }

    /**
     * Sets separator String between Picker columns.
     *
     * @param separator
     * 		Separator String between Picker columns.
     */
    public final void setSeparator(java.lang.CharSequence separator) {
        mSeparator = separator;
    }

    /**
     * Classes extending {@link Picker} can choose to override this method to
     * supply the {@link Picker}'s item's layout id
     */
    public final int getPickerItemLayoutId() {
        return mPickerItemLayoutId;
    }

    /**
     * Returns the {@link Picker}'s item's {@link TextView}'s id from within the
     * layout provided by {@link Picker#getPickerItemLayoutId()} or 0 if the
     * layout provided by {@link Picker#getPickerItemLayoutId()} is a {link
     * TextView}.
     */
    public final int getPickerItemTextViewId() {
        return mPickerItemTextViewId;
    }

    /**
     * Sets the {@link Picker}'s item's {@link TextView}'s id from within the
     * layout provided by {@link Picker#getPickerItemLayoutId()} or 0 if the
     * layout provided by {@link Picker#getPickerItemLayoutId()} is a {link
     * TextView}.
     *
     * @param textViewId
     * 		View id of TextView inside a Picker item, or 0 if the Picker item is a
     * 		TextView.
     */
    public final void setPickerItemTextViewId(int textViewId) {
        mPickerItemTextViewId = textViewId;
    }

    /**
     * Creates a Picker widget.
     *
     * @param context
     * 		
     * @param attrs
     * 		
     * @param defStyleAttr
     * 		
     */
    public Picker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Make it enabled and clickable to receive Click event.
        setEnabled(true);
        mFocusedAlpha = 1.0F;// getFloat(R.dimen.list_item_selected_title_text_alpha);

        mUnfocusedAlpha = 1.0F;// getFloat(R.dimen.list_item_unselected_text_alpha);

        mVisibleColumnAlpha = 0.5F;// getFloat(R.dimen.picker_item_visible_column_item_alpha);

        mInvisibleColumnAlpha = 0.0F;// getFloat(R.dimen.picker_item_invisible_column_item_alpha);

        mAlphaAnimDuration = 200;// mContext.getResources().getInteger(R.integer.dialog_animation_duration);

        mDecelerateInterpolator = new android.view.animation.DecelerateInterpolator(2.5F);
        mAccelerateInterpolator = new android.view.animation.AccelerateInterpolator(2.5F);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
        mRootView = ((android.view.ViewGroup) (inflater.inflate(R.layout.lb_picker, this, true)));
        mPickerView = ((android.view.ViewGroup) (mRootView.findViewById(R.id.picker)));
    }

    /**
     * Get nth PickerColumn.
     *
     * @param colIndex
     * 		Index of PickerColumn.
     * @return PickerColumn at colIndex or null if {@link #setColumns(List)} is not called yet.
     */
    public android.support.v17.leanback.widget.picker.PickerColumn getColumnAt(int colIndex) {
        if (mColumns == null) {
            return null;
        }
        return mColumns.get(colIndex);
    }

    /**
     * Get number of PickerColumns.
     *
     * @return Number of PickerColumns or 0 if {@link #setColumns(List)} is not called yet.
     */
    public int getColumnsCount() {
        if (mColumns == null) {
            return 0;
        }
        return mColumns.size();
    }

    /**
     * Set columns and create Views.
     *
     * @param columns
     * 		PickerColumns to be shown in the Picker.
     */
    public void setColumns(java.util.List<android.support.v17.leanback.widget.picker.PickerColumn> columns) {
        mColumnViews.clear();
        mPickerView.removeAllViews();
        mColumns = new java.util.ArrayList<android.support.v17.leanback.widget.picker.PickerColumn>(columns);
        if (mSelectedColumn > (mColumns.size() - 1)) {
            mSelectedColumn = mColumns.size() - 1;
        }
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
        int totalCol = getColumnsCount();
        for (int i = 0; i < totalCol; i++) {
            final int colIndex = i;
            final android.support.v17.leanback.widget.VerticalGridView columnView = ((android.support.v17.leanback.widget.VerticalGridView) (inflater.inflate(R.layout.lb_picker_column, mPickerView, false)));
            // we don't want VerticalGridView to receive focus.
            updateColumnSize(columnView);
            // always center aligned, not aligning selected item on top/bottom edge.
            columnView.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_NO_EDGE);
            // Width is dynamic, so has fixed size is false.
            columnView.setHasFixedSize(false);
            mColumnViews.add(columnView);
            // add view to root
            mPickerView.addView(columnView);
            // add a separator if not the last element
            if ((i != (totalCol - 1)) && (getSeparator() != null)) {
                android.widget.TextView separator = ((android.widget.TextView) (inflater.inflate(R.layout.lb_picker_separator, mPickerView, false)));
                separator.setText(getSeparator());
                mPickerView.addView(separator);
            }
            columnView.setAdapter(new android.support.v17.leanback.widget.picker.Picker.PickerScrollArrayAdapter(getContext(), getPickerItemLayoutId(), getPickerItemTextViewId(), colIndex));
            columnView.setOnChildViewHolderSelectedListener(mColumnChangeListener);
        }
    }

    /**
     * When column labels change or column range changes, call this function to re-populate the
     * selection list.  Note this function cannot be called from RecyclerView layout/scroll pass.
     *
     * @param columnIndex
     * 		Index of column to update.
     * @param column
     * 		New column to update.
     */
    public void setColumnAt(int columnIndex, android.support.v17.leanback.widget.picker.PickerColumn column) {
        mColumns.set(columnIndex, column);
        android.support.v17.leanback.widget.VerticalGridView columnView = mColumnViews.get(columnIndex);
        android.support.v17.leanback.widget.picker.Picker.PickerScrollArrayAdapter adapter = ((android.support.v17.leanback.widget.picker.Picker.PickerScrollArrayAdapter) (columnView.getAdapter()));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        columnView.setSelectedPosition(column.getCurrentValue() - column.getMinValue());
    }

    /**
     * Manually set current value of a column.  The function will update UI and notify listeners.
     *
     * @param columnIndex
     * 		Index of column to update.
     * @param value
     * 		New value of the column.
     * @param runAnimation
     * 		True to scroll to the value or false otherwise.
     */
    public void setColumnValue(int columnIndex, int value, boolean runAnimation) {
        android.support.v17.leanback.widget.picker.PickerColumn column = mColumns.get(columnIndex);
        if (column.getCurrentValue() != value) {
            column.setCurrentValue(value);
            notifyValueChanged(columnIndex);
            android.support.v17.leanback.widget.VerticalGridView columnView = mColumnViews.get(columnIndex);
            if (columnView != null) {
                int position = value - mColumns.get(columnIndex).getMinValue();
                if (runAnimation) {
                    columnView.setSelectedPositionSmooth(position);
                } else {
                    columnView.setSelectedPosition(position);
                }
            }
        }
    }

    private void notifyValueChanged(int columnIndex) {
        if (mListeners != null) {
            for (int i = mListeners.size() - 1; i >= 0; i--) {
                mListeners.get(i).onValueChanged(this, columnIndex);
            }
        }
    }

    /**
     * Register a callback to be invoked when the picker's value has changed.
     *
     * @param listener
     * 		The callback to ad
     */
    public void addOnValueChangedListener(android.support.v17.leanback.widget.picker.Picker.PickerValueListener listener) {
        if (mListeners == null) {
            mListeners = new java.util.ArrayList<android.support.v17.leanback.widget.picker.Picker.PickerValueListener>();
        }
        mListeners.add(listener);
    }

    /**
     * Remove a previously installed value changed callback
     *
     * @param listener
     * 		The callback to remove.
     */
    public void removeOnValueChangedListener(android.support.v17.leanback.widget.picker.Picker.PickerValueListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    void updateColumnAlpha(int colIndex, boolean animate) {
        android.support.v17.leanback.widget.VerticalGridView column = mColumnViews.get(colIndex);
        int selected = column.getSelectedPosition();
        android.view.View item;
        for (int i = 0; i < column.getAdapter().getItemCount(); i++) {
            item = column.getLayoutManager().findViewByPosition(i);
            if (item != null) {
                setOrAnimateAlpha(item, selected == i, colIndex, animate);
            }
        }
    }

    void setOrAnimateAlpha(android.view.View view, boolean selected, int colIndex, boolean animate) {
        boolean columnShownAsActivated = (colIndex == mSelectedColumn) || (!hasFocus());
        if (selected) {
            // set alpha for main item (selected) in the column
            if (columnShownAsActivated) {
                setOrAnimateAlpha(view, animate, mFocusedAlpha, -1, mDecelerateInterpolator);
            } else {
                setOrAnimateAlpha(view, animate, mUnfocusedAlpha, -1, mDecelerateInterpolator);
            }
        } else {
            // set alpha for remaining items in the column
            if (columnShownAsActivated) {
                setOrAnimateAlpha(view, animate, mVisibleColumnAlpha, -1, mDecelerateInterpolator);
            } else {
                setOrAnimateAlpha(view, animate, mInvisibleColumnAlpha, -1, mDecelerateInterpolator);
            }
        }
    }

    private void setOrAnimateAlpha(android.view.View view, boolean animate, float destAlpha, float startAlpha, android.view.animation.Interpolator interpolator) {
        view.animate().cancel();
        if (!animate) {
            view.setAlpha(destAlpha);
        } else {
            if (startAlpha >= 0.0F) {
                // set a start alpha
                view.setAlpha(startAlpha);
            }
            view.animate().alpha(destAlpha).setDuration(mAlphaAnimDuration).setInterpolator(interpolator).start();
        }
    }

    /**
     * Classes extending {@link Picker} can override this function to supply the
     * behavior when a list has been scrolled.  Subclass may call {@link #setColumnValue(int, int,
     * boolean)} and or {@link #setColumnAt(int,PickerColumn)}.  Subclass should not directly call
     * {@link PickerColumn#setCurrentValue(int)} which does not update internal state or notify
     * listeners.
     *
     * @param columnIndex
     * 		index of which column was changed.
     * @param newValue
     * 		A new value desired to be set on the column.
     */
    public void onColumnValueChanged(int columnIndex, int newValue) {
        android.support.v17.leanback.widget.picker.PickerColumn column = mColumns.get(columnIndex);
        if (column.getCurrentValue() != newValue) {
            column.setCurrentValue(newValue);
            notifyValueChanged(columnIndex);
        }
    }

    private float getFloat(int resourceId) {
        android.util.TypedValue buffer = new android.util.TypedValue();
        getContext().getResources().getValue(resourceId, buffer, true);
        return buffer.getFloat();
    }

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        final android.widget.TextView textView;

        ViewHolder(android.view.View v, android.widget.TextView textView) {
            super(v);
            this.textView = textView;
        }
    }

    class PickerScrollArrayAdapter extends android.support.v7.widget.RecyclerView.Adapter<android.support.v17.leanback.widget.picker.Picker.ViewHolder> {
        private final int mResource;

        private final int mColIndex;

        private final int mTextViewResourceId;

        private android.support.v17.leanback.widget.picker.PickerColumn mData;

        PickerScrollArrayAdapter(android.content.Context context, int resource, int textViewResourceId, int colIndex) {
            mResource = resource;
            mColIndex = colIndex;
            mTextViewResourceId = textViewResourceId;
            mData = mColumns.get(mColIndex);
        }

        @java.lang.Override
        public android.support.v17.leanback.widget.picker.Picker.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
            android.view.View v = inflater.inflate(mResource, parent, false);
            android.widget.TextView textView;
            if (mTextViewResourceId != 0) {
                textView = ((android.widget.TextView) (v.findViewById(mTextViewResourceId)));
            } else {
                textView = ((android.widget.TextView) (v));
            }
            android.support.v17.leanback.widget.picker.Picker.ViewHolder vh = new android.support.v17.leanback.widget.picker.Picker.ViewHolder(v, textView);
            return vh;
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.v17.leanback.widget.picker.Picker.ViewHolder holder, int position) {
            if ((holder.textView != null) && (mData != null)) {
                holder.textView.setText(mData.getLabelFor(mData.getMinValue() + position));
            }
            setOrAnimateAlpha(holder.itemView, mColumnViews.get(mColIndex).getSelectedPosition() == position, mColIndex, false);
        }

        @java.lang.Override
        public void onViewAttachedToWindow(android.support.v17.leanback.widget.picker.Picker.ViewHolder holder) {
            holder.itemView.setFocusable(isActivated());
        }

        @java.lang.Override
        public int getItemCount() {
            return mData == null ? 0 : mData.getCount();
        }
    }

    private final android.support.v17.leanback.widget.OnChildViewHolderSelectedListener mColumnChangeListener = new android.support.v17.leanback.widget.OnChildViewHolderSelectedListener() {
        @java.lang.Override
        public void onChildViewHolderSelected(android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.ViewHolder child, int position, int subposition) {
            android.support.v17.leanback.widget.picker.Picker.PickerScrollArrayAdapter pickerScrollArrayAdapter = ((android.support.v17.leanback.widget.picker.Picker.PickerScrollArrayAdapter) (parent.getAdapter()));
            int colIndex = mColumnViews.indexOf(parent);
            updateColumnAlpha(colIndex, true);
            if (child != null) {
                int newValue = mColumns.get(colIndex).getMinValue() + position;
                onColumnValueChanged(colIndex, newValue);
            }
        }
    };

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (isActivated()) {
            final int keyCode = event.getKeyCode();
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                case android.view.KeyEvent.KEYCODE_ENTER :
                    if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                        performClick();
                    }
                    break;
                default :
                    return super.dispatchKeyEvent(event);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @java.lang.Override
    protected boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
        int column = getSelectedColumn();
        if (column < mColumnViews.size()) {
            return mColumnViews.get(column).requestFocus(direction, previouslyFocusedRect);
        }
        return false;
    }

    /**
     * Classes extending {@link Picker} can choose to override this method to
     * supply the {@link Picker}'s column's single item height in pixels.
     */
    protected int getPickerItemHeightPixels() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.picker_item_height);
    }

    private void updateColumnSize() {
        for (int i = 0; i < getColumnsCount(); i++) {
            updateColumnSize(mColumnViews.get(i));
        }
    }

    private void updateColumnSize(android.support.v17.leanback.widget.VerticalGridView columnView) {
        android.view.ViewGroup.LayoutParams lp = columnView.getLayoutParams();
        lp.height = ((int) (getPickerItemHeightPixels() * (isActivated() ? getActivatedVisibleItemCount() : getVisibleItemCount())));
        columnView.setLayoutParams(lp);
    }

    private void updateItemFocusable() {
        final boolean activated = isActivated();
        for (int i = 0; i < getColumnsCount(); i++) {
            android.support.v17.leanback.widget.VerticalGridView grid = mColumnViews.get(i);
            for (int j = 0; j < grid.getChildCount(); j++) {
                android.view.View view = grid.getChildAt(j);
                view.setFocusable(activated);
            }
        }
    }

    /**
     * Returns number of visible items showing in a column when it's activated.  The default value
     * is 3.
     *
     * @return Number of visible items showing in a column when it's activated.
     */
    public float getActivatedVisibleItemCount() {
        return mVisibleItemsActivated;
    }

    /**
     * Changes number of visible items showing in a column when it's activated.  The default value
     * is 3.
     *
     * @param visiblePickerItems
     * 		Number of visible items showing in a column when it's activated.
     */
    public void setActivatedVisibleItemCount(float visiblePickerItems) {
        if (visiblePickerItems <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        if (mVisibleItemsActivated != visiblePickerItems) {
            mVisibleItemsActivated = visiblePickerItems;
            if (isActivated()) {
                updateColumnSize();
            }
        }
    }

    /**
     * Returns number of visible items showing in a column when it's not activated.  The default
     * value is 1.
     *
     * @return Number of visible items showing in a column when it's not activated.
     */
    public float getVisibleItemCount() {
        return 1;
    }

    /**
     * Changes number of visible items showing in a column when it's not activated.  The default
     * value is 1.
     *
     * @param pickerItems
     * 		Number of visible items showing in a column when it's not activated.
     */
    public void setVisibleItemCount(float pickerItems) {
        if (pickerItems <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        if (mVisibleItems != pickerItems) {
            mVisibleItems = pickerItems;
            if (!isActivated()) {
                updateColumnSize();
            }
        }
    }

    @java.lang.Override
    public void setActivated(boolean activated) {
        if (activated != isActivated()) {
            super.setActivated(activated);
            updateColumnSize();
            updateItemFocusable();
        } else {
            super.setActivated(activated);
        }
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        super.requestChildFocus(child, focused);
        for (int i = 0; i < mColumnViews.size(); i++) {
            if (mColumnViews.get(i).hasFocus()) {
                setSelectedColumn(i);
            }
        }
    }

    /**
     * Change current selected column.  Picker shows multiple items on selected column if Picker has
     * focus.  Picker shows multiple items on all column if Picker has no focus (e.g. a Touchscreen
     * screen).
     *
     * @param columnIndex
     * 		Index of column to activate.
     */
    public void setSelectedColumn(int columnIndex) {
        if (mSelectedColumn != columnIndex) {
            mSelectedColumn = columnIndex;
            for (int i = 0; i < mColumnViews.size(); i++) {
                updateColumnAlpha(i, true);
            }
        }
    }

    /**
     * Get current activated column index.
     *
     * @return Current activated column index.
     */
    public int getSelectedColumn() {
        return mSelectedColumn;
    }
}

