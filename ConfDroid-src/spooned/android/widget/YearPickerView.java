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
package android.widget;


/**
 * Displays a selectable list of years.
 */
class YearPickerView extends android.widget.ListView {
    private final android.widget.YearPickerView.YearAdapter mAdapter;

    private final int mViewSize;

    private final int mChildSize;

    private android.widget.YearPickerView.OnYearSelectedListener mOnYearSelectedListener;

    public YearPickerView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.listViewStyle);
    }

    public YearPickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public YearPickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.widget.AbsListView.LayoutParams frame = new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, android.widget.AbsListView.LayoutParams.WRAP_CONTENT);
        setLayoutParams(frame);
        final android.content.res.Resources res = context.getResources();
        mViewSize = res.getDimensionPixelOffset(R.dimen.datepicker_view_animator_height);
        mChildSize = res.getDimensionPixelOffset(R.dimen.datepicker_year_label_height);
        setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @java.lang.Override
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                final int year = mAdapter.getYearForPosition(position);
                mAdapter.setSelection(year);
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearChanged(android.widget.YearPickerView.this, year);
                }
            }
        });
        mAdapter = new android.widget.YearPickerView.YearAdapter(getContext());
        setAdapter(mAdapter);
    }

    public void setOnYearSelectedListener(android.widget.YearPickerView.OnYearSelectedListener listener) {
        mOnYearSelectedListener = listener;
    }

    /**
     * Sets the currently selected year. Jumps immediately to the new year.
     *
     * @param year
     * 		the target year
     */
    public void setYear(final int year) {
        mAdapter.setSelection(year);
        post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                final int position = mAdapter.getPositionForYear(year);
                if ((position >= 0) && (position < getCount())) {
                    setSelectionCentered(position);
                }
            }
        });
    }

    public void setSelectionCentered(int position) {
        final int offset = (mViewSize / 2) - (mChildSize / 2);
        setSelectionFromTop(position, offset);
    }

    public void setRange(android.icu.util.Calendar min, android.icu.util.Calendar max) {
        mAdapter.setRange(min, max);
    }

    private static class YearAdapter extends android.widget.BaseAdapter {
        private static final int ITEM_LAYOUT = R.layout.year_label_text_view;

        private static final int ITEM_TEXT_APPEARANCE = R.style.TextAppearance_Material_DatePicker_List_YearLabel;

        private static final int ITEM_TEXT_ACTIVATED_APPEARANCE = R.style.TextAppearance_Material_DatePicker_List_YearLabel_Activated;

        private final android.view.LayoutInflater mInflater;

        private int mActivatedYear;

        private int mMinYear;

        private int mCount;

        public YearAdapter(android.content.Context context) {
            mInflater = android.view.LayoutInflater.from(context);
        }

        public void setRange(android.icu.util.Calendar minDate, android.icu.util.Calendar maxDate) {
            final int minYear = minDate.get(Calendar.YEAR);
            final int count = (maxDate.get(Calendar.YEAR) - minYear) + 1;
            if ((mMinYear != minYear) || (mCount != count)) {
                mMinYear = minYear;
                mCount = count;
                notifyDataSetInvalidated();
            }
        }

        public boolean setSelection(int year) {
            if (mActivatedYear != year) {
                mActivatedYear = year;
                notifyDataSetChanged();
                return true;
            }
            return false;
        }

        @java.lang.Override
        public int getCount() {
            return mCount;
        }

        @java.lang.Override
        public java.lang.Integer getItem(int position) {
            return getYearForPosition(position);
        }

        @java.lang.Override
        public long getItemId(int position) {
            return getYearForPosition(position);
        }

        public int getPositionForYear(int year) {
            return year - mMinYear;
        }

        public int getYearForPosition(int position) {
            return mMinYear + position;
        }

        @java.lang.Override
        public boolean hasStableIds() {
            return true;
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            final android.widget.TextView v;
            final boolean hasNewView = convertView == null;
            if (hasNewView) {
                v = ((android.widget.TextView) (mInflater.inflate(android.widget.YearPickerView.YearAdapter.ITEM_LAYOUT, parent, false)));
            } else {
                v = ((android.widget.TextView) (convertView));
            }
            final int year = getYearForPosition(position);
            final boolean activated = mActivatedYear == year;
            if (hasNewView || (v.isActivated() != activated)) {
                final int textAppearanceResId;
                if (activated && (android.widget.YearPickerView.YearAdapter.ITEM_TEXT_ACTIVATED_APPEARANCE != 0)) {
                    textAppearanceResId = android.widget.YearPickerView.YearAdapter.ITEM_TEXT_ACTIVATED_APPEARANCE;
                } else {
                    textAppearanceResId = android.widget.YearPickerView.YearAdapter.ITEM_TEXT_APPEARANCE;
                }
                v.setTextAppearance(textAppearanceResId);
                v.setActivated(activated);
            }
            v.setText(java.lang.Integer.toString(year));
            return v;
        }

        @java.lang.Override
        public int getItemViewType(int position) {
            return 0;
        }

        @java.lang.Override
        public int getViewTypeCount() {
            return 1;
        }

        @java.lang.Override
        public boolean isEmpty() {
            return false;
        }

        @java.lang.Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @java.lang.Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

    public int getFirstPositionOffset() {
        final android.view.View firstChild = getChildAt(0);
        if (firstChild == null) {
            return 0;
        }
        return firstChild.getTop();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        // There are a bunch of years, so don't bother.
        if (event.getEventType() == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            event.setFromIndex(0);
            event.setToIndex(0);
        }
    }

    /**
     * The callback used to indicate the user changed the year.
     */
    public interface OnYearSelectedListener {
        /**
         * Called upon a year change.
         *
         * @param view
         * 		The view associated with this listener.
         * @param year
         * 		The year that was set.
         */
        void onYearChanged(android.widget.YearPickerView view, int year);
    }
}

