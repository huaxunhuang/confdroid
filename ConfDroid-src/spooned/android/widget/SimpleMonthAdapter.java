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
 * An adapter for a list of {@link android.widget.SimpleMonthView} items.
 */
class SimpleMonthAdapter extends android.widget.BaseAdapter {
    private final java.util.Calendar mMinDate = java.util.Calendar.getInstance();

    private final java.util.Calendar mMaxDate = java.util.Calendar.getInstance();

    private final android.content.Context mContext;

    private java.util.Calendar mSelectedDay = java.util.Calendar.getInstance();

    private android.content.res.ColorStateList mCalendarTextColors = android.content.res.ColorStateList.valueOf(android.graphics.Color.BLACK);

    private android.widget.SimpleMonthAdapter.OnDaySelectedListener mOnDaySelectedListener;

    private int mFirstDayOfWeek;

    public SimpleMonthAdapter(android.content.Context context) {
        mContext = context;
    }

    public void setRange(java.util.Calendar min, java.util.Calendar max) {
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());
        notifyDataSetInvalidated();
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        notifyDataSetInvalidated();
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * Updates the selected day and related parameters.
     *
     * @param day
     * 		The day to highlight
     */
    public void setSelectedDay(java.util.Calendar day) {
        mSelectedDay = day;
        notifyDataSetChanged();
    }

    /**
     * Sets the listener to call when the user selects a day.
     *
     * @param listener
     * 		The listener to call.
     */
    public void setOnDaySelectedListener(android.widget.SimpleMonthAdapter.OnDaySelectedListener listener) {
        mOnDaySelectedListener = listener;
    }

    void setCalendarTextColor(android.content.res.ColorStateList colors) {
        mCalendarTextColors = colors;
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color from
     * the specified TextAppearance resource. This is mostly copied from
     * {@link TextView#setTextAppearance(Context, int)}.
     */
    void setCalendarTextAppearance(int resId) {
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(resId, R.styleable.TextAppearance);
        final android.content.res.ColorStateList textColor = a.getColorStateList(R.styleable.TextAppearance_textColor);
        if (textColor != null) {
            mCalendarTextColors = textColor;
        }
        // TODO: Support font size, etc.
        a.recycle();
    }

    @java.lang.Override
    public int getCount() {
        final int diffYear = mMaxDate.get(java.util.Calendar.YEAR) - mMinDate.get(java.util.Calendar.YEAR);
        final int diffMonth = mMaxDate.get(java.util.Calendar.MONTH) - mMinDate.get(java.util.Calendar.MONTH);
        return (diffMonth + (12 * diffYear)) + 1;
    }

    @java.lang.Override
    public java.lang.Object getItem(int position) {
        return null;
    }

    @java.lang.Override
    public long getItemId(int position) {
        return position;
    }

    @java.lang.Override
    public boolean hasStableIds() {
        return true;
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        final android.widget.SimpleMonthView v;
        if (convertView != null) {
            v = ((android.widget.SimpleMonthView) (convertView));
        } else {
            v = new android.widget.SimpleMonthView(mContext);
            // Set up the new view
            final android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, android.widget.AbsListView.LayoutParams.MATCH_PARENT);
            v.setLayoutParams(params);
            v.setClickable(true);
            v.setOnDayClickListener(mOnDayClickListener);
            if (mCalendarTextColors != null) {
                v.setTextColor(mCalendarTextColors);
            }
        }
        final int minMonth = mMinDate.get(java.util.Calendar.MONTH);
        final int minYear = mMinDate.get(java.util.Calendar.YEAR);
        final int currentMonth = position + minMonth;
        final int month = currentMonth % 12;
        final int year = (currentMonth / 12) + minYear;
        final int selectedDay;
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = mSelectedDay.get(java.util.Calendar.DAY_OF_MONTH);
        } else {
            selectedDay = -1;
        }
        // Invokes requestLayout() to ensure that the recycled view is set with the appropriate
        // height/number of weeks before being displayed.
        v.reuse();
        final int enabledDayRangeStart;
        if ((minMonth == month) && (minYear == year)) {
            enabledDayRangeStart = mMinDate.get(java.util.Calendar.DAY_OF_MONTH);
        } else {
            enabledDayRangeStart = 1;
        }
        final int enabledDayRangeEnd;
        if ((mMaxDate.get(java.util.Calendar.MONTH) == month) && (mMaxDate.get(java.util.Calendar.YEAR) == year)) {
            enabledDayRangeEnd = mMaxDate.get(java.util.Calendar.DAY_OF_MONTH);
        } else {
            enabledDayRangeEnd = 31;
        }
        v.setMonthParams(selectedDay, month, year, mFirstDayOfWeek, enabledDayRangeStart, enabledDayRangeEnd);
        v.invalidate();
        return v;
    }

    private boolean isSelectedDayInMonth(int year, int month) {
        return (mSelectedDay.get(java.util.Calendar.YEAR) == year) && (mSelectedDay.get(java.util.Calendar.MONTH) == month);
    }

    private boolean isCalendarInRange(java.util.Calendar value) {
        return (value.compareTo(mMinDate) >= 0) && (value.compareTo(mMaxDate) <= 0);
    }

    private final android.widget.SimpleMonthView.OnDayClickListener mOnDayClickListener = new android.widget.SimpleMonthView.OnDayClickListener() {
        @java.lang.Override
        public void onDayClick(android.widget.SimpleMonthView view, java.util.Calendar day) {
            if ((day != null) && isCalendarInRange(day)) {
                setSelectedDay(day);
                if (mOnDaySelectedListener != null) {
                    mOnDaySelectedListener.onDaySelected(android.widget.SimpleMonthAdapter.this, day);
                }
            }
        }
    };

    public interface OnDaySelectedListener {
        public void onDaySelected(android.widget.SimpleMonthAdapter view, java.util.Calendar day);
    }
}

