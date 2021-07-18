/**
 * Copyright (C) 2015 The Android Open Source Project
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
class DayPickerPagerAdapter extends com.android.internal.widget.PagerAdapter {
    private static final int MONTHS_IN_YEAR = 12;

    private final android.icu.util.Calendar mMinDate = android.icu.util.Calendar.getInstance();

    private final android.icu.util.Calendar mMaxDate = android.icu.util.Calendar.getInstance();

    private final android.util.SparseArray<android.widget.DayPickerPagerAdapter.ViewHolder> mItems = new android.util.SparseArray();

    private final android.view.LayoutInflater mInflater;

    private final int mLayoutResId;

    private final int mCalendarViewId;

    private android.icu.util.Calendar mSelectedDay = null;

    private int mMonthTextAppearance;

    private int mDayOfWeekTextAppearance;

    private int mDayTextAppearance;

    private android.content.res.ColorStateList mCalendarTextColor;

    private android.content.res.ColorStateList mDaySelectorColor;

    private android.content.res.ColorStateList mDayHighlightColor;

    private android.widget.DayPickerPagerAdapter.OnDaySelectedListener mOnDaySelectedListener;

    private int mCount;

    private int mFirstDayOfWeek;

    public DayPickerPagerAdapter(@android.annotation.NonNull
    android.content.Context context, @android.annotation.LayoutRes
    int layoutResId, @android.annotation.IdRes
    int calendarViewId) {
        mInflater = android.view.LayoutInflater.from(context);
        mLayoutResId = layoutResId;
        mCalendarViewId = calendarViewId;
        final android.content.res.TypedArray ta = context.obtainStyledAttributes(new int[]{ com.android.internal.R.attr.colorControlHighlight });
        mDayHighlightColor = ta.getColorStateList(0);
        ta.recycle();
    }

    public void setRange(@android.annotation.NonNull
    android.icu.util.Calendar min, @android.annotation.NonNull
    android.icu.util.Calendar max) {
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());
        final int diffYear = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        final int diffMonth = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        mCount = (diffMonth + (android.widget.DayPickerPagerAdapter.MONTHS_IN_YEAR * diffYear)) + 1;
        // Positions are now invalid, clear everything and start over.
        notifyDataSetChanged();
    }

    /**
     * Sets the first day of the week.
     *
     * @param weekStart
     * 		which day the week should start on, valid values are
     * 		{@link Calendar#SUNDAY} through {@link Calendar#SATURDAY}
     */
    public void setFirstDayOfWeek(int weekStart) {
        mFirstDayOfWeek = weekStart;
        // Update displayed views.
        final int count = mItems.size();
        for (int i = 0; i < count; i++) {
            final android.widget.SimpleMonthView monthView = mItems.valueAt(i).calendar;
            monthView.setFirstDayOfWeek(weekStart);
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public boolean getBoundsForDate(android.icu.util.Calendar day, android.graphics.Rect outBounds) {
        final int position = getPositionForDay(day);
        final android.widget.DayPickerPagerAdapter.ViewHolder monthView = mItems.get(position, null);
        if (monthView == null) {
            return false;
        } else {
            final int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
            return monthView.calendar.getBoundsForDay(dayOfMonth, outBounds);
        }
    }

    /**
     * Sets the selected day.
     *
     * @param day
     * 		the selected day
     */
    public void setSelectedDay(@android.annotation.Nullable
    android.icu.util.Calendar day) {
        final int oldPosition = getPositionForDay(mSelectedDay);
        final int newPosition = getPositionForDay(day);
        // Clear the old position if necessary.
        if ((oldPosition != newPosition) && (oldPosition >= 0)) {
            final android.widget.DayPickerPagerAdapter.ViewHolder oldMonthView = mItems.get(oldPosition, null);
            if (oldMonthView != null) {
                oldMonthView.calendar.setSelectedDay(-1);
            }
        }
        // Set the new position.
        if (newPosition >= 0) {
            final android.widget.DayPickerPagerAdapter.ViewHolder newMonthView = mItems.get(newPosition, null);
            if (newMonthView != null) {
                final int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
                newMonthView.calendar.setSelectedDay(dayOfMonth);
            }
        }
        mSelectedDay = day;
    }

    /**
     * Sets the listener to call when the user selects a day.
     *
     * @param listener
     * 		The listener to call.
     */
    public void setOnDaySelectedListener(android.widget.DayPickerPagerAdapter.OnDaySelectedListener listener) {
        mOnDaySelectedListener = listener;
    }

    void setCalendarTextColor(android.content.res.ColorStateList calendarTextColor) {
        mCalendarTextColor = calendarTextColor;
        notifyDataSetChanged();
    }

    void setDaySelectorColor(android.content.res.ColorStateList selectorColor) {
        mDaySelectorColor = selectorColor;
        notifyDataSetChanged();
    }

    void setMonthTextAppearance(int resId) {
        mMonthTextAppearance = resId;
        notifyDataSetChanged();
    }

    void setDayOfWeekTextAppearance(int resId) {
        mDayOfWeekTextAppearance = resId;
        notifyDataSetChanged();
    }

    int getDayOfWeekTextAppearance() {
        return mDayOfWeekTextAppearance;
    }

    void setDayTextAppearance(int resId) {
        mDayTextAppearance = resId;
        notifyDataSetChanged();
    }

    int getDayTextAppearance() {
        return mDayTextAppearance;
    }

    @java.lang.Override
    public int getCount() {
        return mCount;
    }

    @java.lang.Override
    public boolean isViewFromObject(android.view.View view, java.lang.Object object) {
        final android.widget.DayPickerPagerAdapter.ViewHolder holder = ((android.widget.DayPickerPagerAdapter.ViewHolder) (object));
        return view == holder.container;
    }

    private int getMonthForPosition(int position) {
        return (position + mMinDate.get(Calendar.MONTH)) % android.widget.DayPickerPagerAdapter.MONTHS_IN_YEAR;
    }

    private int getYearForPosition(int position) {
        final int yearOffset = (position + mMinDate.get(Calendar.MONTH)) / android.widget.DayPickerPagerAdapter.MONTHS_IN_YEAR;
        return yearOffset + mMinDate.get(Calendar.YEAR);
    }

    private int getPositionForDay(@android.annotation.Nullable
    android.icu.util.Calendar day) {
        if (day == null) {
            return -1;
        }
        final int yearOffset = day.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        final int monthOffset = day.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        final int position = (yearOffset * android.widget.DayPickerPagerAdapter.MONTHS_IN_YEAR) + monthOffset;
        return position;
    }

    @java.lang.Override
    public java.lang.Object instantiateItem(android.view.ViewGroup container, int position) {
        final android.view.View itemView = mInflater.inflate(mLayoutResId, container, false);
        final android.widget.SimpleMonthView v = itemView.findViewById(mCalendarViewId);
        v.setOnDayClickListener(mOnDayClickListener);
        v.setMonthTextAppearance(mMonthTextAppearance);
        v.setDayOfWeekTextAppearance(mDayOfWeekTextAppearance);
        v.setDayTextAppearance(mDayTextAppearance);
        if (mDaySelectorColor != null) {
            v.setDaySelectorColor(mDaySelectorColor);
        }
        if (mDayHighlightColor != null) {
            v.setDayHighlightColor(mDayHighlightColor);
        }
        if (mCalendarTextColor != null) {
            v.setMonthTextColor(mCalendarTextColor);
            v.setDayOfWeekTextColor(mCalendarTextColor);
            v.setDayTextColor(mCalendarTextColor);
        }
        final int month = getMonthForPosition(position);
        final int year = getYearForPosition(position);
        final int selectedDay;
        if (((mSelectedDay != null) && (mSelectedDay.get(Calendar.MONTH) == month)) && (mSelectedDay.get(Calendar.YEAR) == year)) {
            selectedDay = mSelectedDay.get(Calendar.DAY_OF_MONTH);
        } else {
            selectedDay = -1;
        }
        final int enabledDayRangeStart;
        if ((mMinDate.get(Calendar.MONTH) == month) && (mMinDate.get(Calendar.YEAR) == year)) {
            enabledDayRangeStart = mMinDate.get(Calendar.DAY_OF_MONTH);
        } else {
            enabledDayRangeStart = 1;
        }
        final int enabledDayRangeEnd;
        if ((mMaxDate.get(Calendar.MONTH) == month) && (mMaxDate.get(Calendar.YEAR) == year)) {
            enabledDayRangeEnd = mMaxDate.get(Calendar.DAY_OF_MONTH);
        } else {
            enabledDayRangeEnd = 31;
        }
        v.setMonthParams(selectedDay, month, year, mFirstDayOfWeek, enabledDayRangeStart, enabledDayRangeEnd);
        final android.widget.DayPickerPagerAdapter.ViewHolder holder = new android.widget.DayPickerPagerAdapter.ViewHolder(position, itemView, v);
        mItems.put(position, holder);
        container.addView(itemView);
        return holder;
    }

    @java.lang.Override
    public void destroyItem(android.view.ViewGroup container, int position, java.lang.Object object) {
        final android.widget.DayPickerPagerAdapter.ViewHolder holder = ((android.widget.DayPickerPagerAdapter.ViewHolder) (object));
        container.removeView(holder.container);
        mItems.remove(position);
    }

    @java.lang.Override
    public int getItemPosition(java.lang.Object object) {
        final android.widget.DayPickerPagerAdapter.ViewHolder holder = ((android.widget.DayPickerPagerAdapter.ViewHolder) (object));
        return holder.position;
    }

    @java.lang.Override
    public java.lang.CharSequence getPageTitle(int position) {
        final android.widget.SimpleMonthView v = mItems.get(position).calendar;
        if (v != null) {
            return v.getMonthYearLabel();
        }
        return null;
    }

    android.widget.SimpleMonthView getView(java.lang.Object object) {
        if (object == null) {
            return null;
        }
        final android.widget.DayPickerPagerAdapter.ViewHolder holder = ((android.widget.DayPickerPagerAdapter.ViewHolder) (object));
        return holder.calendar;
    }

    private final android.widget.SimpleMonthView.OnDayClickListener mOnDayClickListener = new android.widget.SimpleMonthView.OnDayClickListener() {
        @java.lang.Override
        public void onDayClick(android.widget.SimpleMonthView view, android.icu.util.Calendar day) {
            if (day != null) {
                setSelectedDay(day);
                if (mOnDaySelectedListener != null) {
                    mOnDaySelectedListener.onDaySelected(android.widget.DayPickerPagerAdapter.this, day);
                }
            }
        }
    };

    private static class ViewHolder {
        public final int position;

        public final android.view.View container;

        public final android.widget.SimpleMonthView calendar;

        public ViewHolder(int position, android.view.View container, android.widget.SimpleMonthView calendar) {
            this.position = position;
            this.container = container;
            this.calendar = calendar;
        }
    }

    public interface OnDaySelectedListener {
        public void onDaySelected(android.widget.DayPickerPagerAdapter view, android.icu.util.Calendar day);
    }
}

