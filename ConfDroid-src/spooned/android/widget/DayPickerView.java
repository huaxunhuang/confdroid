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


class DayPickerView extends android.view.ViewGroup {
    private static final int DEFAULT_LAYOUT = R.layout.day_picker_content_material;

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    private static final int[] ATTRS_TEXT_COLOR = new int[]{ R.attr.textColor };

    private final android.icu.util.Calendar mSelectedDay = android.icu.util.Calendar.getInstance();

    private final android.icu.util.Calendar mMinDate = android.icu.util.Calendar.getInstance();

    private final android.icu.util.Calendar mMaxDate = android.icu.util.Calendar.getInstance();

    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    private final com.android.internal.widget.ViewPager mViewPager;

    private final android.widget.ImageButton mPrevButton;

    private final android.widget.ImageButton mNextButton;

    private final android.widget.DayPickerPagerAdapter mAdapter;

    /**
     * Temporary calendar used for date calculations.
     */
    private android.icu.util.Calendar mTempCalendar;

    private android.widget.DayPickerView.OnDaySelectedListener mOnDaySelectedListener;

    public DayPickerView(android.content.Context context) {
        this(context, null);
    }

    public DayPickerView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public DayPickerView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DayPickerView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mAccessibilityManager = ((android.view.accessibility.AccessibilityManager) (context.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE)));
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.CalendarView, attrs, a, defStyleAttr, defStyleRes);
        final int firstDayOfWeek = a.getInt(R.styleable.CalendarView_firstDayOfWeek, libcore.icu.LocaleData.get(java.util.Locale.getDefault()).firstDayOfWeek);
        final java.lang.String minDate = a.getString(R.styleable.CalendarView_minDate);
        final java.lang.String maxDate = a.getString(R.styleable.CalendarView_maxDate);
        final int monthTextAppearanceResId = a.getResourceId(R.styleable.CalendarView_monthTextAppearance, R.style.TextAppearance_Material_Widget_Calendar_Month);
        final int dayOfWeekTextAppearanceResId = a.getResourceId(R.styleable.CalendarView_weekDayTextAppearance, R.style.TextAppearance_Material_Widget_Calendar_DayOfWeek);
        final int dayTextAppearanceResId = a.getResourceId(R.styleable.CalendarView_dateTextAppearance, R.style.TextAppearance_Material_Widget_Calendar_Day);
        final android.content.res.ColorStateList daySelectorColor = a.getColorStateList(R.styleable.CalendarView_daySelectorColor);
        a.recycle();
        // Set up adapter.
        mAdapter = new android.widget.DayPickerPagerAdapter(context, R.layout.date_picker_month_item_material, R.id.month_view);
        mAdapter.setMonthTextAppearance(monthTextAppearanceResId);
        mAdapter.setDayOfWeekTextAppearance(dayOfWeekTextAppearanceResId);
        mAdapter.setDayTextAppearance(dayTextAppearanceResId);
        mAdapter.setDaySelectorColor(daySelectorColor);
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        final android.view.ViewGroup content = ((android.view.ViewGroup) (inflater.inflate(android.widget.DayPickerView.DEFAULT_LAYOUT, this, false)));
        // Transfer all children from content to here.
        while (content.getChildCount() > 0) {
            final android.view.View child = content.getChildAt(0);
            content.removeViewAt(0);
            addView(child);
        } 
        mPrevButton = findViewById(R.id.prev);
        mPrevButton.setOnClickListener(mOnClickListener);
        mNextButton = findViewById(R.id.next);
        mNextButton.setOnClickListener(mOnClickListener);
        mViewPager = findViewById(R.id.day_picker_view_pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(mOnPageChangedListener);
        // Proxy the month text color into the previous and next buttons.
        if (monthTextAppearanceResId != 0) {
            final android.content.res.TypedArray ta = mContext.obtainStyledAttributes(null, android.widget.DayPickerView.ATTRS_TEXT_COLOR, 0, monthTextAppearanceResId);
            final android.content.res.ColorStateList monthColor = ta.getColorStateList(0);
            if (monthColor != null) {
                mPrevButton.setImageTintList(monthColor);
                mNextButton.setImageTintList(monthColor);
            }
            ta.recycle();
        }
        // Set up min and max dates.
        final android.icu.util.Calendar tempDate = android.icu.util.Calendar.getInstance();
        if (!android.widget.CalendarView.parseDate(minDate, tempDate)) {
            tempDate.set(android.widget.DayPickerView.DEFAULT_START_YEAR, Calendar.JANUARY, 1);
        }
        final long minDateMillis = tempDate.getTimeInMillis();
        if (!android.widget.CalendarView.parseDate(maxDate, tempDate)) {
            tempDate.set(android.widget.DayPickerView.DEFAULT_END_YEAR, Calendar.DECEMBER, 31);
        }
        final long maxDateMillis = tempDate.getTimeInMillis();
        if (maxDateMillis < minDateMillis) {
            throw new java.lang.IllegalArgumentException("maxDate must be >= minDate");
        }
        final long setDateMillis = android.util.MathUtils.constrain(java.lang.System.currentTimeMillis(), minDateMillis, maxDateMillis);
        setFirstDayOfWeek(firstDayOfWeek);
        setMinDate(minDateMillis);
        setMaxDate(maxDateMillis);
        setDate(setDateMillis, false);
        // Proxy selection callbacks to our own listener.
        mAdapter.setOnDaySelectedListener(new android.widget.DayPickerPagerAdapter.OnDaySelectedListener() {
            @java.lang.Override
            public void onDaySelected(android.widget.DayPickerPagerAdapter adapter, android.icu.util.Calendar day) {
                if (mOnDaySelectedListener != null) {
                    mOnDaySelectedListener.onDaySelected(android.widget.DayPickerView.this, day);
                }
            }
        });
    }

    private void updateButtonVisibility(int position) {
        final boolean hasPrev = position > 0;
        final boolean hasNext = position < (mAdapter.getCount() - 1);
        mPrevButton.setVisibility(hasPrev ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        mNextButton.setVisibility(hasNext ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final com.android.internal.widget.ViewPager viewPager = mViewPager;
        measureChild(viewPager, widthMeasureSpec, heightMeasureSpec);
        final int measuredWidthAndState = viewPager.getMeasuredWidthAndState();
        final int measuredHeightAndState = viewPager.getMeasuredHeightAndState();
        setMeasuredDimension(measuredWidthAndState, measuredHeightAndState);
        final int pagerWidth = viewPager.getMeasuredWidth();
        final int pagerHeight = viewPager.getMeasuredHeight();
        final int buttonWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(pagerWidth, android.view.View.MeasureSpec.AT_MOST);
        final int buttonHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(pagerHeight, android.view.View.MeasureSpec.AT_MOST);
        mPrevButton.measure(buttonWidthSpec, buttonHeightSpec);
        mNextButton.measure(buttonWidthSpec, buttonHeightSpec);
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(@android.view.View.ResolvedLayoutDir
    int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        requestLayout();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final android.widget.ImageButton leftButton;
        final android.widget.ImageButton rightButton;
        if (isLayoutRtl()) {
            leftButton = mNextButton;
            rightButton = mPrevButton;
        } else {
            leftButton = mPrevButton;
            rightButton = mNextButton;
        }
        final int width = right - left;
        final int height = bottom - top;
        mViewPager.layout(0, 0, width, height);
        final android.widget.SimpleMonthView monthView = ((android.widget.SimpleMonthView) (mViewPager.getChildAt(0)));
        final int monthHeight = monthView.getMonthHeight();
        final int cellWidth = monthView.getCellWidth();
        // Vertically center the previous/next buttons within the month
        // header, horizontally center within the day cell.
        final int leftDW = leftButton.getMeasuredWidth();
        final int leftDH = leftButton.getMeasuredHeight();
        final int leftIconTop = monthView.getPaddingTop() + ((monthHeight - leftDH) / 2);
        final int leftIconLeft = monthView.getPaddingLeft() + ((cellWidth - leftDW) / 2);
        leftButton.layout(leftIconLeft, leftIconTop, leftIconLeft + leftDW, leftIconTop + leftDH);
        final int rightDW = rightButton.getMeasuredWidth();
        final int rightDH = rightButton.getMeasuredHeight();
        final int rightIconTop = monthView.getPaddingTop() + ((monthHeight - rightDH) / 2);
        final int rightIconRight = (width - monthView.getPaddingRight()) - ((cellWidth - rightDW) / 2);
        rightButton.layout(rightIconRight - rightDW, rightIconTop, rightIconRight, rightIconTop + rightDH);
    }

    public void setDayOfWeekTextAppearance(int resId) {
        mAdapter.setDayOfWeekTextAppearance(resId);
    }

    public int getDayOfWeekTextAppearance() {
        return mAdapter.getDayOfWeekTextAppearance();
    }

    public void setDayTextAppearance(int resId) {
        mAdapter.setDayTextAppearance(resId);
    }

    public int getDayTextAppearance() {
        return mAdapter.getDayTextAppearance();
    }

    /**
     * Sets the currently selected date to the specified timestamp. Jumps
     * immediately to the new date. To animate to the new date, use
     * {@link #setDate(long, boolean)}.
     *
     * @param timeInMillis
     * 		the target day in milliseconds
     */
    public void setDate(long timeInMillis) {
        setDate(timeInMillis, false);
    }

    /**
     * Sets the currently selected date to the specified timestamp. Jumps
     * immediately to the new date, optionally animating the transition.
     *
     * @param timeInMillis
     * 		the target day in milliseconds
     * @param animate
     * 		whether to smooth scroll to the new position
     */
    public void setDate(long timeInMillis, boolean animate) {
        setDate(timeInMillis, animate, true);
    }

    /**
     * Moves to the month containing the specified day, optionally setting the
     * day as selected.
     *
     * @param timeInMillis
     * 		the target day in milliseconds
     * @param animate
     * 		whether to smooth scroll to the new position
     * @param setSelected
     * 		whether to set the specified day as selected
     */
    private void setDate(long timeInMillis, boolean animate, boolean setSelected) {
        boolean dateClamped = false;
        // Clamp the target day in milliseconds to the min or max if outside the range.
        if (timeInMillis < mMinDate.getTimeInMillis()) {
            timeInMillis = mMinDate.getTimeInMillis();
            dateClamped = true;
        } else
            if (timeInMillis > mMaxDate.getTimeInMillis()) {
                timeInMillis = mMaxDate.getTimeInMillis();
                dateClamped = true;
            }

        getTempCalendarForTime(timeInMillis);
        if (setSelected || dateClamped) {
            mSelectedDay.setTimeInMillis(timeInMillis);
        }
        final int position = getPositionFromDay(timeInMillis);
        if (position != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(position, animate);
        }
        mAdapter.setSelectedDay(mTempCalendar);
    }

    public long getDate() {
        return mSelectedDay.getTimeInMillis();
    }

    public boolean getBoundsForDate(long timeInMillis, android.graphics.Rect outBounds) {
        final int position = getPositionFromDay(timeInMillis);
        if (position != mViewPager.getCurrentItem()) {
            return false;
        }
        mTempCalendar.setTimeInMillis(timeInMillis);
        return mAdapter.getBoundsForDate(mTempCalendar, outBounds);
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mAdapter.setFirstDayOfWeek(firstDayOfWeek);
    }

    public int getFirstDayOfWeek() {
        return mAdapter.getFirstDayOfWeek();
    }

    public void setMinDate(long timeInMillis) {
        mMinDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    public long getMinDate() {
        return mMinDate.getTimeInMillis();
    }

    public void setMaxDate(long timeInMillis) {
        mMaxDate.setTimeInMillis(timeInMillis);
        onRangeChanged();
    }

    public long getMaxDate() {
        return mMaxDate.getTimeInMillis();
    }

    /**
     * Handles changes to date range.
     */
    public void onRangeChanged() {
        mAdapter.setRange(mMinDate, mMaxDate);
        // Changing the min/max date changes the selection position since we
        // don't really have stable IDs. Jumps immediately to the new position.
        setDate(mSelectedDay.getTimeInMillis(), false, false);
        updateButtonVisibility(mViewPager.getCurrentItem());
    }

    /**
     * Sets the listener to call when the user selects a day.
     *
     * @param listener
     * 		The listener to call.
     */
    public void setOnDaySelectedListener(android.widget.DayPickerView.OnDaySelectedListener listener) {
        mOnDaySelectedListener = listener;
    }

    private int getDiffMonths(android.icu.util.Calendar start, android.icu.util.Calendar end) {
        final int diffYears = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        return (end.get(Calendar.MONTH) - start.get(Calendar.MONTH)) + (12 * diffYears);
    }

    private int getPositionFromDay(long timeInMillis) {
        final int diffMonthMax = getDiffMonths(mMinDate, mMaxDate);
        final int diffMonth = getDiffMonths(mMinDate, getTempCalendarForTime(timeInMillis));
        return android.util.MathUtils.constrain(diffMonth, 0, diffMonthMax);
    }

    private android.icu.util.Calendar getTempCalendarForTime(long timeInMillis) {
        if (mTempCalendar == null) {
            mTempCalendar = android.icu.util.Calendar.getInstance();
        }
        mTempCalendar.setTimeInMillis(timeInMillis);
        return mTempCalendar;
    }

    /**
     * Gets the position of the view that is most prominently displayed within the list view.
     */
    public int getMostVisiblePosition() {
        return mViewPager.getCurrentItem();
    }

    public void setPosition(int position) {
        mViewPager.setCurrentItem(position, false);
    }

    private final com.android.internal.widget.ViewPager.OnPageChangeListener mOnPageChangedListener = new com.android.internal.widget.ViewPager.OnPageChangeListener() {
        @java.lang.Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final float alpha = java.lang.Math.abs(0.5F - positionOffset) * 2.0F;
            mPrevButton.setAlpha(alpha);
            mNextButton.setAlpha(alpha);
        }

        @java.lang.Override
        public void onPageScrollStateChanged(int state) {
        }

        @java.lang.Override
        public void onPageSelected(int position) {
            updateButtonVisibility(position);
        }
    };

    private final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            final int direction;
            if (v == mPrevButton) {
                direction = -1;
            } else
                if (v == mNextButton) {
                    direction = 1;
                } else {
                    return;
                }

            // Animation is expensive for accessibility services since it sends
            // lots of scroll and content change events.
            final boolean animate = !mAccessibilityManager.isEnabled();
            // ViewPager clamps input values, so we don't need to worry
            // about passing invalid indices.
            final int nextItem = mViewPager.getCurrentItem() + direction;
            mViewPager.setCurrentItem(nextItem, animate);
        }
    };

    public interface OnDaySelectedListener {
        void onDaySelected(android.widget.DayPickerView view, android.icu.util.Calendar day);
    }
}

