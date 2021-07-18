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
 * A delegate implementing the legacy CalendarView
 */
class CalendarViewLegacyDelegate extends android.widget.CalendarView.AbstractCalendarViewDelegate {
    /**
     * Default value whether to show week number.
     */
    private static final boolean DEFAULT_SHOW_WEEK_NUMBER = true;

    /**
     * The number of milliseconds in a day.e
     */
    private static final long MILLIS_IN_DAY = 86400000L;

    /**
     * The number of day in a week.
     */
    private static final int DAYS_PER_WEEK = 7;

    /**
     * The number of milliseconds in a week.
     */
    private static final long MILLIS_IN_WEEK = android.widget.CalendarViewLegacyDelegate.DAYS_PER_WEEK * android.widget.CalendarViewLegacyDelegate.MILLIS_IN_DAY;

    /**
     * Affects when the month selection will change while scrolling upe
     */
    private static final int SCROLL_HYST_WEEKS = 2;

    /**
     * How long the GoTo fling animation should last.
     */
    private static final int GOTO_SCROLL_DURATION = 1000;

    /**
     * The duration of the adjustment upon a user scroll in milliseconds.
     */
    private static final int ADJUSTMENT_SCROLL_DURATION = 500;

    /**
     * How long to wait after receiving an onScrollStateChanged notification
     * before acting on it.
     */
    private static final int SCROLL_CHANGE_DELAY = 40;

    private static final int DEFAULT_SHOWN_WEEK_COUNT = 6;

    private static final int DEFAULT_DATE_TEXT_SIZE = 14;

    private static final int UNSCALED_SELECTED_DATE_VERTICAL_BAR_WIDTH = 6;

    private static final int UNSCALED_WEEK_MIN_VISIBLE_HEIGHT = 12;

    private static final int UNSCALED_LIST_SCROLL_TOP_OFFSET = 2;

    private static final int UNSCALED_BOTTOM_BUFFER = 20;

    private static final int UNSCALED_WEEK_SEPARATOR_LINE_WIDTH = 1;

    private static final int DEFAULT_WEEK_DAY_TEXT_APPEARANCE_RES_ID = -1;

    private final int mWeekSeparatorLineWidth;

    private int mDateTextSize;

    private android.graphics.drawable.Drawable mSelectedDateVerticalBar;

    private final int mSelectedDateVerticalBarWidth;

    private int mSelectedWeekBackgroundColor;

    private int mFocusedMonthDateColor;

    private int mUnfocusedMonthDateColor;

    private int mWeekSeparatorLineColor;

    private int mWeekNumberColor;

    private int mWeekDayTextAppearanceResId;

    private int mDateTextAppearanceResId;

    /**
     * The top offset of the weeks list.
     */
    private int mListScrollTopOffset = 2;

    /**
     * The visible height of a week view.
     */
    private int mWeekMinVisibleHeight = 12;

    /**
     * The visible height of a week view.
     */
    private int mBottomBuffer = 20;

    /**
     * The number of shown weeks.
     */
    private int mShownWeekCount;

    /**
     * Flag whether to show the week number.
     */
    private boolean mShowWeekNumber;

    /**
     * The number of day per week to be shown.
     */
    private int mDaysPerWeek = 7;

    /**
     * The friction of the week list while flinging.
     */
    private float mFriction = 0.05F;

    /**
     * Scale for adjusting velocity of the week list while flinging.
     */
    private float mVelocityScale = 0.333F;

    /**
     * The adapter for the weeks list.
     */
    private android.widget.CalendarViewLegacyDelegate.WeeksAdapter mAdapter;

    /**
     * The weeks list.
     */
    private android.widget.ListView mListView;

    /**
     * The name of the month to display.
     */
    private android.widget.TextView mMonthName;

    /**
     * The header with week day names.
     */
    private android.view.ViewGroup mDayNamesHeader;

    /**
     * Cached abbreviations for day of week names.
     */
    private java.lang.String[] mDayNamesShort;

    /**
     * Cached full-length day of week names.
     */
    private java.lang.String[] mDayNamesLong;

    /**
     * The first day of the week.
     */
    private int mFirstDayOfWeek;

    /**
     * Which month should be displayed/highlighted [0-11].
     */
    private int mCurrentMonthDisplayed = -1;

    /**
     * Used for tracking during a scroll.
     */
    private long mPreviousScrollPosition;

    /**
     * Used for tracking which direction the view is scrolling.
     */
    private boolean mIsScrollingUp = false;

    /**
     * The previous scroll state of the weeks ListView.
     */
    private int mPreviousScrollState = android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    /**
     * The current scroll state of the weeks ListView.
     */
    private int mCurrentScrollState = android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    /**
     * Listener for changes in the selected day.
     */
    private android.widget.CalendarView.OnDateChangeListener mOnDateChangeListener;

    /**
     * Command for adjusting the position after a scroll/fling.
     */
    private android.widget.CalendarViewLegacyDelegate.ScrollStateRunnable mScrollStateChangedRunnable = new android.widget.CalendarViewLegacyDelegate.ScrollStateRunnable();

    /**
     * Temporary instance to avoid multiple instantiations.
     */
    private android.icu.util.Calendar mTempDate;

    /**
     * The first day of the focused month.
     */
    private android.icu.util.Calendar mFirstDayOfMonth;

    /**
     * The start date of the range supported by this picker.
     */
    private android.icu.util.Calendar mMinDate;

    /**
     * The end date of the range supported by this picker.
     */
    private android.icu.util.Calendar mMaxDate;

    CalendarViewLegacyDelegate(android.widget.CalendarView delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
        mShowWeekNumber = a.getBoolean(R.styleable.CalendarView_showWeekNumber, android.widget.CalendarViewLegacyDelegate.DEFAULT_SHOW_WEEK_NUMBER);
        mFirstDayOfWeek = a.getInt(R.styleable.CalendarView_firstDayOfWeek, libcore.icu.LocaleData.get(java.util.Locale.getDefault()).firstDayOfWeek);
        final java.lang.String minDate = a.getString(R.styleable.CalendarView_minDate);
        if (!android.widget.CalendarView.parseDate(minDate, mMinDate)) {
            android.widget.CalendarView.parseDate(android.widget.CalendarView.AbstractCalendarViewDelegate.DEFAULT_MIN_DATE, mMinDate);
        }
        final java.lang.String maxDate = a.getString(R.styleable.CalendarView_maxDate);
        if (!android.widget.CalendarView.parseDate(maxDate, mMaxDate)) {
            android.widget.CalendarView.parseDate(android.widget.CalendarView.AbstractCalendarViewDelegate.DEFAULT_MAX_DATE, mMaxDate);
        }
        if (mMaxDate.before(mMinDate)) {
            throw new java.lang.IllegalArgumentException("Max date cannot be before min date.");
        }
        mShownWeekCount = a.getInt(R.styleable.CalendarView_shownWeekCount, android.widget.CalendarViewLegacyDelegate.DEFAULT_SHOWN_WEEK_COUNT);
        mSelectedWeekBackgroundColor = a.getColor(R.styleable.CalendarView_selectedWeekBackgroundColor, 0);
        mFocusedMonthDateColor = a.getColor(R.styleable.CalendarView_focusedMonthDateColor, 0);
        mUnfocusedMonthDateColor = a.getColor(R.styleable.CalendarView_unfocusedMonthDateColor, 0);
        mWeekSeparatorLineColor = a.getColor(R.styleable.CalendarView_weekSeparatorLineColor, 0);
        mWeekNumberColor = a.getColor(R.styleable.CalendarView_weekNumberColor, 0);
        mSelectedDateVerticalBar = a.getDrawable(R.styleable.CalendarView_selectedDateVerticalBar);
        mDateTextAppearanceResId = a.getResourceId(R.styleable.CalendarView_dateTextAppearance, R.style.TextAppearance_Small);
        updateDateTextSize();
        mWeekDayTextAppearanceResId = a.getResourceId(R.styleable.CalendarView_weekDayTextAppearance, android.widget.CalendarViewLegacyDelegate.DEFAULT_WEEK_DAY_TEXT_APPEARANCE_RES_ID);
        a.recycle();
        android.util.DisplayMetrics displayMetrics = mDelegator.getResources().getDisplayMetrics();
        mWeekMinVisibleHeight = ((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, android.widget.CalendarViewLegacyDelegate.UNSCALED_WEEK_MIN_VISIBLE_HEIGHT, displayMetrics)));
        mListScrollTopOffset = ((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, android.widget.CalendarViewLegacyDelegate.UNSCALED_LIST_SCROLL_TOP_OFFSET, displayMetrics)));
        mBottomBuffer = ((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, android.widget.CalendarViewLegacyDelegate.UNSCALED_BOTTOM_BUFFER, displayMetrics)));
        mSelectedDateVerticalBarWidth = ((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, android.widget.CalendarViewLegacyDelegate.UNSCALED_SELECTED_DATE_VERTICAL_BAR_WIDTH, displayMetrics)));
        mWeekSeparatorLineWidth = ((int) (android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, android.widget.CalendarViewLegacyDelegate.UNSCALED_WEEK_SEPARATOR_LINE_WIDTH, displayMetrics)));
        android.view.LayoutInflater layoutInflater = ((android.view.LayoutInflater) (mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE)));
        android.view.View content = layoutInflater.inflate(R.layout.calendar_view, null, false);
        mDelegator.addView(content);
        mListView = mDelegator.findViewById(R.id.list);
        mDayNamesHeader = content.findViewById(R.id.day_names);
        mMonthName = content.findViewById(R.id.month_name);
        setUpHeader();
        setUpListView();
        setUpAdapter();
        // go to today or whichever is close to today min or max date
        mTempDate.setTimeInMillis(java.lang.System.currentTimeMillis());
        if (mTempDate.before(mMinDate)) {
            goTo(mMinDate, false, true, true);
        } else
            if (mMaxDate.before(mTempDate)) {
                goTo(mMaxDate, false, true, true);
            } else {
                goTo(mTempDate, false, true, true);
            }

        mDelegator.invalidate();
    }

    @java.lang.Override
    public void setShownWeekCount(int count) {
        if (mShownWeekCount != count) {
            mShownWeekCount = count;
            mDelegator.invalidate();
        }
    }

    @java.lang.Override
    public int getShownWeekCount() {
        return mShownWeekCount;
    }

    @java.lang.Override
    public void setSelectedWeekBackgroundColor(int color) {
        if (mSelectedWeekBackgroundColor != color) {
            mSelectedWeekBackgroundColor = color;
            final int childCount = mListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.widget.CalendarViewLegacyDelegate.WeekView weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (mListView.getChildAt(i)));
                if (weekView.mHasSelectedDay) {
                    weekView.invalidate();
                }
            }
        }
    }

    @java.lang.Override
    public int getSelectedWeekBackgroundColor() {
        return mSelectedWeekBackgroundColor;
    }

    @java.lang.Override
    public void setFocusedMonthDateColor(int color) {
        if (mFocusedMonthDateColor != color) {
            mFocusedMonthDateColor = color;
            final int childCount = mListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.widget.CalendarViewLegacyDelegate.WeekView weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (mListView.getChildAt(i)));
                if (weekView.mHasFocusedDay) {
                    weekView.invalidate();
                }
            }
        }
    }

    @java.lang.Override
    public int getFocusedMonthDateColor() {
        return mFocusedMonthDateColor;
    }

    @java.lang.Override
    public void setUnfocusedMonthDateColor(int color) {
        if (mUnfocusedMonthDateColor != color) {
            mUnfocusedMonthDateColor = color;
            final int childCount = mListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.widget.CalendarViewLegacyDelegate.WeekView weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (mListView.getChildAt(i)));
                if (weekView.mHasUnfocusedDay) {
                    weekView.invalidate();
                }
            }
        }
    }

    @java.lang.Override
    public int getUnfocusedMonthDateColor() {
        return mUnfocusedMonthDateColor;
    }

    @java.lang.Override
    public void setWeekNumberColor(int color) {
        if (mWeekNumberColor != color) {
            mWeekNumberColor = color;
            if (mShowWeekNumber) {
                invalidateAllWeekViews();
            }
        }
    }

    @java.lang.Override
    public int getWeekNumberColor() {
        return mWeekNumberColor;
    }

    @java.lang.Override
    public void setWeekSeparatorLineColor(int color) {
        if (mWeekSeparatorLineColor != color) {
            mWeekSeparatorLineColor = color;
            invalidateAllWeekViews();
        }
    }

    @java.lang.Override
    public int getWeekSeparatorLineColor() {
        return mWeekSeparatorLineColor;
    }

    @java.lang.Override
    public void setSelectedDateVerticalBar(int resourceId) {
        android.graphics.drawable.Drawable drawable = mDelegator.getContext().getDrawable(resourceId);
        setSelectedDateVerticalBar(drawable);
    }

    @java.lang.Override
    public void setSelectedDateVerticalBar(android.graphics.drawable.Drawable drawable) {
        if (mSelectedDateVerticalBar != drawable) {
            mSelectedDateVerticalBar = drawable;
            final int childCount = mListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.widget.CalendarViewLegacyDelegate.WeekView weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (mListView.getChildAt(i)));
                if (weekView.mHasSelectedDay) {
                    weekView.invalidate();
                }
            }
        }
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getSelectedDateVerticalBar() {
        return mSelectedDateVerticalBar;
    }

    @java.lang.Override
    public void setWeekDayTextAppearance(int resourceId) {
        if (mWeekDayTextAppearanceResId != resourceId) {
            mWeekDayTextAppearanceResId = resourceId;
            setUpHeader();
        }
    }

    @java.lang.Override
    public int getWeekDayTextAppearance() {
        return mWeekDayTextAppearanceResId;
    }

    @java.lang.Override
    public void setDateTextAppearance(int resourceId) {
        if (mDateTextAppearanceResId != resourceId) {
            mDateTextAppearanceResId = resourceId;
            updateDateTextSize();
            invalidateAllWeekViews();
        }
    }

    @java.lang.Override
    public int getDateTextAppearance() {
        return mDateTextAppearanceResId;
    }

    @java.lang.Override
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if (android.widget.CalendarViewLegacyDelegate.isSameDate(mTempDate, mMinDate)) {
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        // make sure the current date is not earlier than
        // the new min date since the latter is used for
        // calculating the indices in the adapter thus
        // avoiding out of bounds error
        android.icu.util.Calendar date = mAdapter.mSelectedDate;
        if (date.before(mMinDate)) {
            mAdapter.setSelectedDay(mMinDate);
        }
        // reinitialize the adapter since its range depends on min date
        mAdapter.init();
        if (date.before(mMinDate)) {
            setDate(mTempDate.getTimeInMillis());
        } else {
            // we go to the current date to force the ListView to query its
            // adapter for the shown views since we have changed the adapter
            // range and the base from which the later calculates item indices
            // note that calling setDate will not work since the date is the same
            goTo(date, false, true, false);
        }
    }

    @java.lang.Override
    public long getMinDate() {
        return mMinDate.getTimeInMillis();
    }

    @java.lang.Override
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if (android.widget.CalendarViewLegacyDelegate.isSameDate(mTempDate, mMaxDate)) {
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        // reinitialize the adapter since its range depends on max date
        mAdapter.init();
        android.icu.util.Calendar date = mAdapter.mSelectedDate;
        if (date.after(mMaxDate)) {
            setDate(mMaxDate.getTimeInMillis());
        } else {
            // we go to the current date to force the ListView to query its
            // adapter for the shown views since we have changed the adapter
            // range and the base from which the later calculates item indices
            // note that calling setDate will not work since the date is the same
            goTo(date, false, true, false);
        }
    }

    @java.lang.Override
    public long getMaxDate() {
        return mMaxDate.getTimeInMillis();
    }

    @java.lang.Override
    public void setShowWeekNumber(boolean showWeekNumber) {
        if (mShowWeekNumber == showWeekNumber) {
            return;
        }
        mShowWeekNumber = showWeekNumber;
        mAdapter.notifyDataSetChanged();
        setUpHeader();
    }

    @java.lang.Override
    public boolean getShowWeekNumber() {
        return mShowWeekNumber;
    }

    @java.lang.Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (mFirstDayOfWeek == firstDayOfWeek) {
            return;
        }
        mFirstDayOfWeek = firstDayOfWeek;
        mAdapter.init();
        mAdapter.notifyDataSetChanged();
        setUpHeader();
    }

    @java.lang.Override
    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    @java.lang.Override
    public void setDate(long date) {
        setDate(date, false, false);
    }

    @java.lang.Override
    public void setDate(long date, boolean animate, boolean center) {
        mTempDate.setTimeInMillis(date);
        if (android.widget.CalendarViewLegacyDelegate.isSameDate(mTempDate, mAdapter.mSelectedDate)) {
            return;
        }
        goTo(mTempDate, animate, true, center);
    }

    @java.lang.Override
    public long getDate() {
        return mAdapter.mSelectedDate.getTimeInMillis();
    }

    @java.lang.Override
    public void setOnDateChangeListener(android.widget.CalendarView.OnDateChangeListener listener) {
        mOnDateChangeListener = listener;
    }

    @java.lang.Override
    public boolean getBoundsForDate(long date, android.graphics.Rect outBounds) {
        android.icu.util.Calendar calendarDate = android.icu.util.Calendar.getInstance();
        calendarDate.setTimeInMillis(date);
        int listViewEntryCount = mListView.getCount();
        for (int i = 0; i < listViewEntryCount; i++) {
            android.widget.CalendarViewLegacyDelegate.WeekView currWeekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (mListView.getChildAt(i)));
            if (currWeekView.getBoundsForDate(calendarDate, outBounds)) {
                // Found the date in this week. Now need to offset vertically to return correct
                // bounds in the coordinate system of the entire layout
                final int[] weekViewPositionOnScreen = new int[2];
                final int[] delegatorPositionOnScreen = new int[2];
                currWeekView.getLocationOnScreen(weekViewPositionOnScreen);
                mDelegator.getLocationOnScreen(delegatorPositionOnScreen);
                final int extraVerticalOffset = weekViewPositionOnScreen[1] - delegatorPositionOnScreen[1];
                outBounds.top += extraVerticalOffset;
                outBounds.bottom += extraVerticalOffset;
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    /**
     * Sets the current locale.
     *
     * @param locale
     * 		The current locale.
     */
    @java.lang.Override
    protected void setCurrentLocale(java.util.Locale locale) {
        super.setCurrentLocale(locale);
        mTempDate = android.widget.CalendarViewLegacyDelegate.getCalendarForLocale(mTempDate, locale);
        mFirstDayOfMonth = android.widget.CalendarViewLegacyDelegate.getCalendarForLocale(mFirstDayOfMonth, locale);
        mMinDate = android.widget.CalendarViewLegacyDelegate.getCalendarForLocale(mMinDate, locale);
        mMaxDate = android.widget.CalendarViewLegacyDelegate.getCalendarForLocale(mMaxDate, locale);
    }

    private void updateDateTextSize() {
        android.content.res.TypedArray dateTextAppearance = mDelegator.getContext().obtainStyledAttributes(mDateTextAppearanceResId, R.styleable.TextAppearance);
        mDateTextSize = dateTextAppearance.getDimensionPixelSize(R.styleable.TextAppearance_textSize, android.widget.CalendarViewLegacyDelegate.DEFAULT_DATE_TEXT_SIZE);
        dateTextAppearance.recycle();
    }

    /**
     * Invalidates all week views.
     */
    private void invalidateAllWeekViews() {
        final int childCount = mListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            android.view.View view = mListView.getChildAt(i);
            view.invalidate();
        }
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given calendar.
     *
     * @param oldCalendar
     * 		The old calendar.
     * @param locale
     * 		The locale.
     */
    private static android.icu.util.Calendar getCalendarForLocale(android.icu.util.Calendar oldCalendar, java.util.Locale locale) {
        if (oldCalendar == null) {
            return android.icu.util.Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            android.icu.util.Calendar newCalendar = android.icu.util.Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    /**
     *
     *
     * @return True if the <code>firstDate</code> is the same as the <code>
    secondDate</code>.
     */
    private static boolean isSameDate(android.icu.util.Calendar firstDate, android.icu.util.Calendar secondDate) {
        return (firstDate.get(Calendar.DAY_OF_YEAR) == secondDate.get(Calendar.DAY_OF_YEAR)) && (firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR));
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters.
     */
    private void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new android.widget.CalendarViewLegacyDelegate.WeeksAdapter(mContext);
            mAdapter.registerDataSetObserver(new android.database.DataSetObserver() {
                @java.lang.Override
                public void onChanged() {
                    if (mOnDateChangeListener != null) {
                        android.icu.util.Calendar selectedDay = mAdapter.getSelectedDay();
                        mOnDateChangeListener.onSelectedDayChange(mDelegator, selectedDay.get(Calendar.YEAR), selectedDay.get(Calendar.MONTH), selectedDay.get(Calendar.DAY_OF_MONTH));
                    }
                }
            });
            mListView.setAdapter(mAdapter);
        }
        // refresh the view with the new parameters
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Sets up the strings to be used by the header.
     */
    private void setUpHeader() {
        mDayNamesShort = new java.lang.String[mDaysPerWeek];
        mDayNamesLong = new java.lang.String[mDaysPerWeek];
        for (int i = mFirstDayOfWeek, count = mFirstDayOfWeek + mDaysPerWeek; i < count; i++) {
            int calendarDay = (i > android.icu.util.Calendar.SATURDAY) ? i - android.icu.util.Calendar.SATURDAY : i;
            mDayNamesShort[i - mFirstDayOfWeek] = android.text.format.DateUtils.getDayOfWeekString(calendarDay, DateUtils.LENGTH_SHORTEST);
            mDayNamesLong[i - mFirstDayOfWeek] = android.text.format.DateUtils.getDayOfWeekString(calendarDay, DateUtils.LENGTH_LONG);
        }
        android.widget.TextView label = ((android.widget.TextView) (mDayNamesHeader.getChildAt(0)));
        if (mShowWeekNumber) {
            label.setVisibility(android.view.View.VISIBLE);
        } else {
            label.setVisibility(android.view.View.GONE);
        }
        for (int i = 1, count = mDayNamesHeader.getChildCount(); i < count; i++) {
            label = ((android.widget.TextView) (mDayNamesHeader.getChildAt(i)));
            if (mWeekDayTextAppearanceResId > (-1)) {
                label.setTextAppearance(mWeekDayTextAppearanceResId);
            }
            if (i < (mDaysPerWeek + 1)) {
                label.setText(mDayNamesShort[i - 1]);
                label.setContentDescription(mDayNamesLong[i - 1]);
                label.setVisibility(android.view.View.VISIBLE);
            } else {
                label.setVisibility(android.view.View.GONE);
            }
        }
        mDayNamesHeader.invalidate();
    }

    /**
     * Sets all the required fields for the list view.
     */
    private void setUpListView() {
        // Configure the listview
        mListView.setDivider(null);
        mListView.setItemsCanFocus(true);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnScrollListener(new android.widget.AbsListView.OnScrollListener() {
            public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
                android.widget.CalendarViewLegacyDelegate.this.onScrollStateChanged(view, scrollState);
            }

            public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                android.widget.CalendarViewLegacyDelegate.this.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        // Make the scrolling behavior nicer
        mListView.setFriction(mFriction);
        mListView.setVelocityScale(mVelocityScale);
    }

    /**
     * This moves to the specified time in the view. If the time is not already
     * in range it will move the list so that the first of the month containing
     * the time is at the top of the view. If the new time is already in view
     * the list will not be scrolled unless forceScroll is true. This time may
     * optionally be highlighted as selected as well.
     *
     * @param date
     * 		The time to move to.
     * @param animate
     * 		Whether to scroll to the given time or just redraw at the
     * 		new location.
     * @param setSelected
     * 		Whether to set the given time as selected.
     * @param forceScroll
     * 		Whether to recenter even if the time is already
     * 		visible.
     * @throws IllegalArgumentException
     * 		if the provided date is before the
     * 		range start or after the range end.
     */
    private void goTo(android.icu.util.Calendar date, boolean animate, boolean setSelected, boolean forceScroll) {
        if (date.before(mMinDate) || date.after(mMaxDate)) {
            throw new java.lang.IllegalArgumentException("timeInMillis must be between the values of " + "getMinDate() and getMaxDate()");
        }
        // Find the first and last entirely visible weeks
        int firstFullyVisiblePosition = mListView.getFirstVisiblePosition();
        android.view.View firstChild = mListView.getChildAt(0);
        if ((firstChild != null) && (firstChild.getTop() < 0)) {
            firstFullyVisiblePosition++;
        }
        int lastFullyVisiblePosition = (firstFullyVisiblePosition + mShownWeekCount) - 1;
        if ((firstChild != null) && (firstChild.getTop() > mBottomBuffer)) {
            lastFullyVisiblePosition--;
        }
        if (setSelected) {
            mAdapter.setSelectedDay(date);
        }
        // Get the week we're going to
        int position = getWeeksSinceMinDate(date);
        // Check if the selected day is now outside of our visible range
        // and if so scroll to the month that contains it
        if (((position < firstFullyVisiblePosition) || (position > lastFullyVisiblePosition)) || forceScroll) {
            mFirstDayOfMonth.setTimeInMillis(date.getTimeInMillis());
            mFirstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
            setMonthDisplayed(mFirstDayOfMonth);
            // the earliest time we can scroll to is the min date
            if (mFirstDayOfMonth.before(mMinDate)) {
                position = 0;
            } else {
                position = getWeeksSinceMinDate(mFirstDayOfMonth);
            }
            mPreviousScrollState = android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
            if (animate) {
                mListView.smoothScrollToPositionFromTop(position, mListScrollTopOffset, android.widget.CalendarViewLegacyDelegate.GOTO_SCROLL_DURATION);
            } else {
                mListView.setSelectionFromTop(position, mListScrollTopOffset);
                // Perform any after scroll operations that are needed
                onScrollStateChanged(mListView, android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }
        } else
            if (setSelected) {
                // Otherwise just set the selection
                setMonthDisplayed(date);
            }

    }

    /**
     * Called when a <code>view</code> transitions to a new <code>scrollState
     * </code>.
     */
    private void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
        mScrollStateChangedRunnable.doScrollStateChange(view, scrollState);
    }

    /**
     * Updates the title and selected month if the <code>view</code> has moved to a new
     * month.
     */
    private void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        android.widget.CalendarViewLegacyDelegate.WeekView child = ((android.widget.CalendarViewLegacyDelegate.WeekView) (view.getChildAt(0)));
        if (child == null) {
            return;
        }
        // Figure out where we are
        long currScroll = (view.getFirstVisiblePosition() * child.getHeight()) - child.getBottom();
        // If we have moved since our last call update the direction
        if (currScroll < mPreviousScrollPosition) {
            mIsScrollingUp = true;
        } else
            if (currScroll > mPreviousScrollPosition) {
                mIsScrollingUp = false;
            } else {
                return;
            }

        // Use some hysteresis for checking which month to highlight. This
        // causes the month to transition when two full weeks of a month are
        // visible when scrolling up, and when the first day in a month reaches
        // the top of the screen when scrolling down.
        int offset = (child.getBottom() < mWeekMinVisibleHeight) ? 1 : 0;
        if (mIsScrollingUp) {
            child = ((android.widget.CalendarViewLegacyDelegate.WeekView) (view.getChildAt(android.widget.CalendarViewLegacyDelegate.SCROLL_HYST_WEEKS + offset)));
        } else
            if (offset != 0) {
                child = ((android.widget.CalendarViewLegacyDelegate.WeekView) (view.getChildAt(offset)));
            }

        if (child != null) {
            // Find out which month we're moving into
            int month;
            if (mIsScrollingUp) {
                month = child.getMonthOfFirstWeekDay();
            } else {
                month = child.getMonthOfLastWeekDay();
            }
            // And how it relates to our current highlighted month
            int monthDiff;
            if ((mCurrentMonthDisplayed == 11) && (month == 0)) {
                monthDiff = 1;
            } else
                if ((mCurrentMonthDisplayed == 0) && (month == 11)) {
                    monthDiff = -1;
                } else {
                    monthDiff = month - mCurrentMonthDisplayed;
                }

            // Only switch months if we're scrolling away from the currently
            // selected month
            if (((!mIsScrollingUp) && (monthDiff > 0)) || (mIsScrollingUp && (monthDiff < 0))) {
                android.icu.util.Calendar firstDay = child.getFirstDay();
                if (mIsScrollingUp) {
                    firstDay.add(Calendar.DAY_OF_MONTH, -android.widget.CalendarViewLegacyDelegate.DAYS_PER_WEEK);
                } else {
                    firstDay.add(Calendar.DAY_OF_MONTH, android.widget.CalendarViewLegacyDelegate.DAYS_PER_WEEK);
                }
                setMonthDisplayed(firstDay);
            }
        }
        mPreviousScrollPosition = currScroll;
        mPreviousScrollState = mCurrentScrollState;
    }

    /**
     * Sets the month displayed at the top of this view based on time. Override
     * to add custom events when the title is changed.
     *
     * @param calendar
     * 		A day in the new focus month.
     */
    private void setMonthDisplayed(android.icu.util.Calendar calendar) {
        mCurrentMonthDisplayed = calendar.get(Calendar.MONTH);
        mAdapter.setFocusMonth(mCurrentMonthDisplayed);
        final int flags = (android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_NO_MONTH_DAY) | android.text.format.DateUtils.FORMAT_SHOW_YEAR;
        final long millis = calendar.getTimeInMillis();
        java.lang.String newMonthName = android.text.format.DateUtils.formatDateRange(mContext, millis, millis, flags);
        mMonthName.setText(newMonthName);
        mMonthName.invalidate();
    }

    /**
     *
     *
     * @return Returns the number of weeks between the current <code>date</code>
    and the <code>mMinDate</code>.
     */
    private int getWeeksSinceMinDate(android.icu.util.Calendar date) {
        if (date.before(mMinDate)) {
            throw new java.lang.IllegalArgumentException((("fromDate: " + mMinDate.getTime()) + " does not precede toDate: ") + date.getTime());
        }
        long endTimeMillis = date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis());
        long startTimeMillis = mMinDate.getTimeInMillis() + mMinDate.getTimeZone().getOffset(mMinDate.getTimeInMillis());
        long dayOffsetMillis = (mMinDate.get(Calendar.DAY_OF_WEEK) - mFirstDayOfWeek) * android.widget.CalendarViewLegacyDelegate.MILLIS_IN_DAY;
        return ((int) (((endTimeMillis - startTimeMillis) + dayOffsetMillis) / android.widget.CalendarViewLegacyDelegate.MILLIS_IN_WEEK));
    }

    /**
     * Command responsible for acting upon scroll state changes.
     */
    private class ScrollStateRunnable implements java.lang.Runnable {
        private android.widget.AbsListView mView;

        private int mNewState;

        /**
         * Sets up the runnable with a short delay in case the scroll state
         * immediately changes again.
         *
         * @param view
         * 		The list view that changed state
         * @param scrollState
         * 		The new state it changed to
         */
        public void doScrollStateChange(android.widget.AbsListView view, int scrollState) {
            mView = view;
            mNewState = scrollState;
            mDelegator.removeCallbacks(this);
            mDelegator.postDelayed(this, android.widget.CalendarViewLegacyDelegate.SCROLL_CHANGE_DELAY);
        }

        public void run() {
            mCurrentScrollState = mNewState;
            // Fix the position after a scroll or a fling ends
            if ((mNewState == android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE) && (mPreviousScrollState != android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
                android.view.View child = mView.getChildAt(0);
                if (child == null) {
                    // The view is no longer visible, just return
                    return;
                }
                int dist = child.getBottom() - mListScrollTopOffset;
                if (dist > mListScrollTopOffset) {
                    if (mIsScrollingUp) {
                        mView.smoothScrollBy(dist - child.getHeight(), android.widget.CalendarViewLegacyDelegate.ADJUSTMENT_SCROLL_DURATION);
                    } else {
                        mView.smoothScrollBy(dist, android.widget.CalendarViewLegacyDelegate.ADJUSTMENT_SCROLL_DURATION);
                    }
                }
            }
            mPreviousScrollState = mNewState;
        }
    }

    /**
     * <p>
     * This is a specialized adapter for creating a list of weeks with
     * selectable days. It can be configured to display the week number, start
     * the week on a given day, show a reduced number of days, or display an
     * arbitrary number of weeks at a time.
     * </p>
     */
    private class WeeksAdapter extends android.widget.BaseAdapter implements android.view.View.OnTouchListener {
        private int mSelectedWeek;

        private android.view.GestureDetector mGestureDetector;

        private int mFocusedMonth;

        private final android.icu.util.Calendar mSelectedDate = android.icu.util.Calendar.getInstance();

        private int mTotalWeekCount;

        public WeeksAdapter(android.content.Context context) {
            mContext = context;
            mGestureDetector = new android.view.GestureDetector(mContext, new android.widget.CalendarViewLegacyDelegate.WeeksAdapter.CalendarGestureListener());
            init();
        }

        /**
         * Set up the gesture detector and selected time
         */
        private void init() {
            mSelectedWeek = getWeeksSinceMinDate(mSelectedDate);
            mTotalWeekCount = getWeeksSinceMinDate(mMaxDate);
            if ((mMinDate.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek) || (mMaxDate.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek)) {
                mTotalWeekCount++;
            }
            notifyDataSetChanged();
        }

        /**
         * Updates the selected day and related parameters.
         *
         * @param selectedDay
         * 		The time to highlight
         */
        public void setSelectedDay(android.icu.util.Calendar selectedDay) {
            if ((selectedDay.get(Calendar.DAY_OF_YEAR) == mSelectedDate.get(Calendar.DAY_OF_YEAR)) && (selectedDay.get(Calendar.YEAR) == mSelectedDate.get(Calendar.YEAR))) {
                return;
            }
            mSelectedDate.setTimeInMillis(selectedDay.getTimeInMillis());
            mSelectedWeek = getWeeksSinceMinDate(mSelectedDate);
            mFocusedMonth = mSelectedDate.get(Calendar.MONTH);
            notifyDataSetChanged();
        }

        /**
         *
         *
         * @return The selected day of month.
         */
        public android.icu.util.Calendar getSelectedDay() {
            return mSelectedDate;
        }

        @java.lang.Override
        public int getCount() {
            return mTotalWeekCount;
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
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.widget.CalendarViewLegacyDelegate.WeekView weekView = null;
            if (convertView != null) {
                weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (convertView));
            } else {
                weekView = new android.widget.CalendarViewLegacyDelegate.WeekView(mContext);
                android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
                weekView.setLayoutParams(params);
                weekView.setClickable(true);
                weekView.setOnTouchListener(this);
            }
            int selectedWeekDay = (mSelectedWeek == position) ? mSelectedDate.get(Calendar.DAY_OF_WEEK) : -1;
            weekView.init(position, selectedWeekDay, mFocusedMonth);
            return weekView;
        }

        /**
         * Changes which month is in focus and updates the view.
         *
         * @param month
         * 		The month to show as in focus [0-11]
         */
        public void setFocusMonth(int month) {
            if (mFocusedMonth == month) {
                return;
            }
            mFocusedMonth = month;
            notifyDataSetChanged();
        }

        @java.lang.Override
        public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
            if (mListView.isEnabled() && mGestureDetector.onTouchEvent(event)) {
                android.widget.CalendarViewLegacyDelegate.WeekView weekView = ((android.widget.CalendarViewLegacyDelegate.WeekView) (v));
                // if we cannot find a day for the given location we are done
                if (!weekView.getDayFromLocation(event.getX(), mTempDate)) {
                    return true;
                }
                // it is possible that the touched day is outside the valid range
                // we draw whole weeks but range end can fall not on the week end
                if (mTempDate.before(mMinDate) || mTempDate.after(mMaxDate)) {
                    return true;
                }
                onDateTapped(mTempDate);
                return true;
            }
            return false;
        }

        /**
         * Maintains the same hour/min/sec but moves the day to the tapped day.
         *
         * @param day
         * 		The day that was tapped
         */
        private void onDateTapped(android.icu.util.Calendar day) {
            setSelectedDay(day);
            setMonthDisplayed(day);
        }

        /**
         * This is here so we can identify single tap events and set the
         * selected day correctly
         */
        class CalendarGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
            @java.lang.Override
            public boolean onSingleTapUp(android.view.MotionEvent e) {
                return true;
            }
        }
    }

    /**
     * <p>
     * This is a dynamic view for drawing a single week. It can be configured to
     * display the week number, start the week on a given day, or show a reduced
     * number of days. It is intended for use as a single view within a
     * ListView. See {@link WeeksAdapter} for usage.
     * </p>
     */
    private class WeekView extends android.view.View {
        private final android.graphics.Rect mTempRect = new android.graphics.Rect();

        private final android.graphics.Paint mDrawPaint = new android.graphics.Paint();

        private final android.graphics.Paint mMonthNumDrawPaint = new android.graphics.Paint();

        // Cache the number strings so we don't have to recompute them each time
        private java.lang.String[] mDayNumbers;

        // Quick lookup for checking which days are in the focus month
        private boolean[] mFocusDay;

        // Whether this view has a focused day.
        private boolean mHasFocusedDay;

        // Whether this view has only focused days.
        private boolean mHasUnfocusedDay;

        // The first day displayed by this item
        private android.icu.util.Calendar mFirstDay;

        // The month of the first day in this week
        private int mMonthOfFirstWeekDay = -1;

        // The month of the last day in this week
        private int mLastWeekDayMonth = -1;

        // The position of this week, equivalent to weeks since the week of Jan
        // 1st, 1900
        private int mWeek = -1;

        // Quick reference to the width of this view, matches parent
        private int mWidth;

        // The height this view should draw at in pixels, set by height param
        private int mHeight;

        // If this view contains the selected day
        private boolean mHasSelectedDay = false;

        // Which day is selected [0-6] or -1 if no day is selected
        private int mSelectedDay = -1;

        // The number of days + a spot for week number if it is displayed
        private int mNumCells;

        // The left edge of the selected day
        private int mSelectedLeft = -1;

        // The right edge of the selected day
        private int mSelectedRight = -1;

        public WeekView(android.content.Context context) {
            super(context);
            // Sets up any standard paints that will be used
            initializePaints();
        }

        /**
         * Initializes this week view.
         *
         * @param weekNumber
         * 		The number of the week this view represents. The
         * 		week number is a zero based index of the weeks since
         * 		{@link android.widget.CalendarView#getMinDate()}.
         * @param selectedWeekDay
         * 		The selected day of the week from 0 to 6, -1 if no
         * 		selected day.
         * @param focusedMonth
         * 		The month that is currently in focus i.e.
         * 		highlighted.
         */
        public void init(int weekNumber, int selectedWeekDay, int focusedMonth) {
            mSelectedDay = selectedWeekDay;
            mHasSelectedDay = mSelectedDay != (-1);
            mNumCells = (mShowWeekNumber) ? mDaysPerWeek + 1 : mDaysPerWeek;
            mWeek = weekNumber;
            mTempDate.setTimeInMillis(mMinDate.getTimeInMillis());
            mTempDate.add(Calendar.WEEK_OF_YEAR, mWeek);
            mTempDate.setFirstDayOfWeek(mFirstDayOfWeek);
            // Allocate space for caching the day numbers and focus values
            mDayNumbers = new java.lang.String[mNumCells];
            mFocusDay = new boolean[mNumCells];
            // If we're showing the week number calculate it based on Monday
            int i = 0;
            if (mShowWeekNumber) {
                mDayNumbers[0] = java.lang.String.format(java.util.Locale.getDefault(), "%d", mTempDate.get(Calendar.WEEK_OF_YEAR));
                i++;
            }
            // Now adjust our starting day based on the start day of the week
            int diff = mFirstDayOfWeek - mTempDate.get(Calendar.DAY_OF_WEEK);
            mTempDate.add(Calendar.DAY_OF_MONTH, diff);
            mFirstDay = ((android.icu.util.Calendar) (mTempDate.clone()));
            mMonthOfFirstWeekDay = mTempDate.get(Calendar.MONTH);
            mHasUnfocusedDay = true;
            for (; i < mNumCells; i++) {
                final boolean isFocusedDay = mTempDate.get(Calendar.MONTH) == focusedMonth;
                mFocusDay[i] = isFocusedDay;
                mHasFocusedDay |= isFocusedDay;
                mHasUnfocusedDay &= !isFocusedDay;
                // do not draw dates outside the valid range to avoid user confusion
                if (mTempDate.before(mMinDate) || mTempDate.after(mMaxDate)) {
                    mDayNumbers[i] = "";
                } else {
                    mDayNumbers[i] = java.lang.String.format(java.util.Locale.getDefault(), "%d", mTempDate.get(Calendar.DAY_OF_MONTH));
                }
                mTempDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            // We do one extra add at the end of the loop, if that pushed us to
            // new month undo it
            if (mTempDate.get(Calendar.DAY_OF_MONTH) == 1) {
                mTempDate.add(Calendar.DAY_OF_MONTH, -1);
            }
            mLastWeekDayMonth = mTempDate.get(Calendar.MONTH);
            updateSelectionPositions();
        }

        /**
         * Initialize the paint instances.
         */
        private void initializePaints() {
            mDrawPaint.setFakeBoldText(false);
            mDrawPaint.setAntiAlias(true);
            mDrawPaint.setStyle(android.graphics.Paint.Style.FILL);
            mMonthNumDrawPaint.setFakeBoldText(true);
            mMonthNumDrawPaint.setAntiAlias(true);
            mMonthNumDrawPaint.setStyle(android.graphics.Paint.Style.FILL);
            mMonthNumDrawPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
            mMonthNumDrawPaint.setTextSize(mDateTextSize);
        }

        /**
         * Returns the month of the first day in this week.
         *
         * @return The month the first day of this view is in.
         */
        public int getMonthOfFirstWeekDay() {
            return mMonthOfFirstWeekDay;
        }

        /**
         * Returns the month of the last day in this week
         *
         * @return The month the last day of this view is in
         */
        public int getMonthOfLastWeekDay() {
            return mLastWeekDayMonth;
        }

        /**
         * Returns the first day in this view.
         *
         * @return The first day in the view.
         */
        public android.icu.util.Calendar getFirstDay() {
            return mFirstDay;
        }

        /**
         * Calculates the day that the given x position is in, accounting for
         * week number.
         *
         * @param x
         * 		The x position of the touch event.
         * @return True if a day was found for the given location.
         */
        public boolean getDayFromLocation(float x, android.icu.util.Calendar outCalendar) {
            final boolean isLayoutRtl = isLayoutRtl();
            int start;
            int end;
            if (isLayoutRtl) {
                start = 0;
                end = (mShowWeekNumber) ? mWidth - (mWidth / mNumCells) : mWidth;
            } else {
                start = (mShowWeekNumber) ? mWidth / mNumCells : 0;
                end = mWidth;
            }
            if ((x < start) || (x > end)) {
                outCalendar.clear();
                return false;
            }
            // Selection is (x - start) / (pixels/day) which is (x - start) * day / pixels
            int dayPosition = ((int) (((x - start) * mDaysPerWeek) / (end - start)));
            if (isLayoutRtl) {
                dayPosition = (mDaysPerWeek - 1) - dayPosition;
            }
            outCalendar.setTimeInMillis(mFirstDay.getTimeInMillis());
            outCalendar.add(Calendar.DAY_OF_MONTH, dayPosition);
            return true;
        }

        public boolean getBoundsForDate(android.icu.util.Calendar date, android.graphics.Rect outBounds) {
            android.icu.util.Calendar currDay = android.icu.util.Calendar.getInstance();
            currDay.setTime(mFirstDay.getTime());
            for (int i = 0; i < mDaysPerWeek; i++) {
                if (((date.get(Calendar.YEAR) == currDay.get(Calendar.YEAR)) && (date.get(Calendar.MONTH) == currDay.get(Calendar.MONTH))) && (date.get(Calendar.DAY_OF_MONTH) == currDay.get(Calendar.DAY_OF_MONTH))) {
                    // We found the matching date. Follow the logic in the draw pass that divides
                    // the available horizontal space equally between all the entries in this week.
                    // Note that if we're showing week number, the start entry will be that number.
                    int cellSize = mWidth / mNumCells;
                    if (isLayoutRtl()) {
                        outBounds.left = cellSize * (mShowWeekNumber ? (mNumCells - i) - 2 : (mNumCells - i) - 1);
                    } else {
                        outBounds.left = cellSize * (mShowWeekNumber ? i + 1 : i);
                    }
                    outBounds.top = 0;
                    outBounds.right = outBounds.left + cellSize;
                    outBounds.bottom = getHeight();
                    return true;
                }
                // Add one day
                currDay.add(Calendar.DAY_OF_MONTH, 1);
            }
            return false;
        }

        @java.lang.Override
        protected void onDraw(android.graphics.Canvas canvas) {
            drawBackground(canvas);
            drawWeekNumbersAndDates(canvas);
            drawWeekSeparators(canvas);
            drawSelectedDateVerticalBars(canvas);
        }

        /**
         * This draws the selection highlight if a day is selected in this week.
         *
         * @param canvas
         * 		The canvas to draw on
         */
        private void drawBackground(android.graphics.Canvas canvas) {
            if (!mHasSelectedDay) {
                return;
            }
            mDrawPaint.setColor(mSelectedWeekBackgroundColor);
            mTempRect.top = mWeekSeparatorLineWidth;
            mTempRect.bottom = mHeight;
            final boolean isLayoutRtl = isLayoutRtl();
            if (isLayoutRtl) {
                mTempRect.left = 0;
                mTempRect.right = mSelectedLeft - 2;
            } else {
                mTempRect.left = (mShowWeekNumber) ? mWidth / mNumCells : 0;
                mTempRect.right = mSelectedLeft - 2;
            }
            canvas.drawRect(mTempRect, mDrawPaint);
            if (isLayoutRtl) {
                mTempRect.left = mSelectedRight + 3;
                mTempRect.right = (mShowWeekNumber) ? mWidth - (mWidth / mNumCells) : mWidth;
            } else {
                mTempRect.left = mSelectedRight + 3;
                mTempRect.right = mWidth;
            }
            canvas.drawRect(mTempRect, mDrawPaint);
        }

        /**
         * Draws the week and month day numbers for this week.
         *
         * @param canvas
         * 		The canvas to draw on
         */
        private void drawWeekNumbersAndDates(android.graphics.Canvas canvas) {
            final float textHeight = mDrawPaint.getTextSize();
            final int y = ((int) ((mHeight + textHeight) / 2)) - mWeekSeparatorLineWidth;
            final int nDays = mNumCells;
            final int divisor = 2 * nDays;
            mDrawPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
            mDrawPaint.setTextSize(mDateTextSize);
            int i = 0;
            if (isLayoutRtl()) {
                for (; i < (nDays - 1); i++) {
                    mMonthNumDrawPaint.setColor(mFocusDay[i] ? mFocusedMonthDateColor : mUnfocusedMonthDateColor);
                    int x = (((2 * i) + 1) * mWidth) / divisor;
                    canvas.drawText(mDayNumbers[(nDays - 1) - i], x, y, mMonthNumDrawPaint);
                }
                if (mShowWeekNumber) {
                    mDrawPaint.setColor(mWeekNumberColor);
                    int x = mWidth - (mWidth / divisor);
                    canvas.drawText(mDayNumbers[0], x, y, mDrawPaint);
                }
            } else {
                if (mShowWeekNumber) {
                    mDrawPaint.setColor(mWeekNumberColor);
                    int x = mWidth / divisor;
                    canvas.drawText(mDayNumbers[0], x, y, mDrawPaint);
                    i++;
                }
                for (; i < nDays; i++) {
                    mMonthNumDrawPaint.setColor(mFocusDay[i] ? mFocusedMonthDateColor : mUnfocusedMonthDateColor);
                    int x = (((2 * i) + 1) * mWidth) / divisor;
                    canvas.drawText(mDayNumbers[i], x, y, mMonthNumDrawPaint);
                }
            }
        }

        /**
         * Draws a horizontal line for separating the weeks.
         *
         * @param canvas
         * 		The canvas to draw on.
         */
        private void drawWeekSeparators(android.graphics.Canvas canvas) {
            // If it is the topmost fully visible child do not draw separator line
            int firstFullyVisiblePosition = mListView.getFirstVisiblePosition();
            if (mListView.getChildAt(0).getTop() < 0) {
                firstFullyVisiblePosition++;
            }
            if (firstFullyVisiblePosition == mWeek) {
                return;
            }
            mDrawPaint.setColor(mWeekSeparatorLineColor);
            mDrawPaint.setStrokeWidth(mWeekSeparatorLineWidth);
            float startX;
            float stopX;
            if (isLayoutRtl()) {
                startX = 0;
                stopX = (mShowWeekNumber) ? mWidth - (mWidth / mNumCells) : mWidth;
            } else {
                startX = (mShowWeekNumber) ? mWidth / mNumCells : 0;
                stopX = mWidth;
            }
            canvas.drawLine(startX, 0, stopX, 0, mDrawPaint);
        }

        /**
         * Draws the selected date bars if this week has a selected day.
         *
         * @param canvas
         * 		The canvas to draw on
         */
        private void drawSelectedDateVerticalBars(android.graphics.Canvas canvas) {
            if (!mHasSelectedDay) {
                return;
            }
            mSelectedDateVerticalBar.setBounds(mSelectedLeft - (mSelectedDateVerticalBarWidth / 2), mWeekSeparatorLineWidth, mSelectedLeft + (mSelectedDateVerticalBarWidth / 2), mHeight);
            mSelectedDateVerticalBar.draw(canvas);
            mSelectedDateVerticalBar.setBounds(mSelectedRight - (mSelectedDateVerticalBarWidth / 2), mWeekSeparatorLineWidth, mSelectedRight + (mSelectedDateVerticalBarWidth / 2), mHeight);
            mSelectedDateVerticalBar.draw(canvas);
        }

        @java.lang.Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mWidth = w;
            updateSelectionPositions();
        }

        /**
         * This calculates the positions for the selected day lines.
         */
        private void updateSelectionPositions() {
            if (mHasSelectedDay) {
                final boolean isLayoutRtl = isLayoutRtl();
                int selectedPosition = mSelectedDay - mFirstDayOfWeek;
                if (selectedPosition < 0) {
                    selectedPosition += 7;
                }
                if (mShowWeekNumber && (!isLayoutRtl)) {
                    selectedPosition++;
                }
                if (isLayoutRtl) {
                    mSelectedLeft = (((mDaysPerWeek - 1) - selectedPosition) * mWidth) / mNumCells;
                } else {
                    mSelectedLeft = (selectedPosition * mWidth) / mNumCells;
                }
                mSelectedRight = mSelectedLeft + (mWidth / mNumCells);
            }
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            mHeight = ((mListView.getHeight() - mListView.getPaddingTop()) - mListView.getPaddingBottom()) / mShownWeekCount;
            setMeasuredDimension(android.view.View.MeasureSpec.getSize(widthMeasureSpec), mHeight);
        }
    }
}

