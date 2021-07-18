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


class CalendarViewMaterialDelegate extends android.widget.CalendarView.AbstractCalendarViewDelegate {
    private final android.widget.DayPickerView mDayPickerView;

    private android.widget.CalendarView.OnDateChangeListener mOnDateChangeListener;

    public CalendarViewMaterialDelegate(android.widget.CalendarView delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        mDayPickerView = new android.widget.DayPickerView(context, attrs, defStyleAttr, defStyleRes);
        mDayPickerView.setOnDaySelectedListener(mOnDaySelectedListener);
        delegator.addView(mDayPickerView);
    }

    @java.lang.Override
    public void setWeekDayTextAppearance(@android.annotation.StyleRes
    int resId) {
        mDayPickerView.setDayOfWeekTextAppearance(resId);
    }

    @android.annotation.StyleRes
    @java.lang.Override
    public int getWeekDayTextAppearance() {
        return mDayPickerView.getDayOfWeekTextAppearance();
    }

    @java.lang.Override
    public void setDateTextAppearance(@android.annotation.StyleRes
    int resId) {
        mDayPickerView.setDayTextAppearance(resId);
    }

    @android.annotation.StyleRes
    @java.lang.Override
    public int getDateTextAppearance() {
        return mDayPickerView.getDayTextAppearance();
    }

    @java.lang.Override
    public void setMinDate(long minDate) {
        mDayPickerView.setMinDate(minDate);
    }

    @java.lang.Override
    public long getMinDate() {
        return mDayPickerView.getMinDate();
    }

    @java.lang.Override
    public void setMaxDate(long maxDate) {
        mDayPickerView.setMaxDate(maxDate);
    }

    @java.lang.Override
    public long getMaxDate() {
        return mDayPickerView.getMaxDate();
    }

    @java.lang.Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mDayPickerView.setFirstDayOfWeek(firstDayOfWeek);
    }

    @java.lang.Override
    public int getFirstDayOfWeek() {
        return mDayPickerView.getFirstDayOfWeek();
    }

    @java.lang.Override
    public void setDate(long date) {
        mDayPickerView.setDate(date, true);
    }

    @java.lang.Override
    public void setDate(long date, boolean animate, boolean center) {
        mDayPickerView.setDate(date, animate);
    }

    @java.lang.Override
    public long getDate() {
        return mDayPickerView.getDate();
    }

    @java.lang.Override
    public void setOnDateChangeListener(android.widget.CalendarView.OnDateChangeListener listener) {
        mOnDateChangeListener = listener;
    }

    @java.lang.Override
    public boolean getBoundsForDate(long date, android.graphics.Rect outBounds) {
        boolean result = mDayPickerView.getBoundsForDate(date, outBounds);
        if (result) {
            // Found the date in the current picker. Now need to offset vertically to return correct
            // bounds in the coordinate system of the entire layout
            final int[] dayPickerPositionOnScreen = new int[2];
            final int[] delegatorPositionOnScreen = new int[2];
            mDayPickerView.getLocationOnScreen(dayPickerPositionOnScreen);
            mDelegator.getLocationOnScreen(delegatorPositionOnScreen);
            final int extraVerticalOffset = dayPickerPositionOnScreen[1] - delegatorPositionOnScreen[1];
            outBounds.top += extraVerticalOffset;
            outBounds.bottom += extraVerticalOffset;
            return true;
        }
        return false;
    }

    private final android.widget.DayPickerView.OnDaySelectedListener mOnDaySelectedListener = new android.widget.DayPickerView.OnDaySelectedListener() {
        @java.lang.Override
        public void onDaySelected(android.widget.DayPickerView view, android.icu.util.Calendar day) {
            if (mOnDateChangeListener != null) {
                final int year = day.get(Calendar.YEAR);
                final int month = day.get(Calendar.MONTH);
                final int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
                mOnDateChangeListener.onSelectedDayChange(mDelegator, year, month, dayOfMonth);
            }
        }
    };
}

