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
 * A delegate for picking up a date (day / month / year).
 */
class DatePickerCalendarDelegate extends android.widget.DatePicker.AbstractDatePickerDelegate {
    private static final int USE_LOCALE = 0;

    private static final int UNINITIALIZED = -1;

    private static final int VIEW_MONTH_DAY = 0;

    private static final int VIEW_YEAR = 1;

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    private static final int ANIMATION_DURATION = 300;

    private static final int[] ATTRS_TEXT_COLOR = new int[]{ com.android.internal.R.attr.textColor };

    private static final int[] ATTRS_DISABLED_ALPHA = new int[]{ com.android.internal.R.attr.disabledAlpha };

    private android.icu.text.DateFormat mYearFormat;

    private android.icu.text.DateFormat mMonthDayFormat;

    // Top-level container.
    private android.view.ViewGroup mContainer;

    // Header views.
    private android.widget.TextView mHeaderYear;

    private android.widget.TextView mHeaderMonthDay;

    // Picker views.
    private android.widget.ViewAnimator mAnimator;

    private android.widget.DayPickerView mDayPickerView;

    private android.widget.YearPickerView mYearPickerView;

    // Accessibility strings.
    private java.lang.String mSelectDay;

    private java.lang.String mSelectYear;

    private int mCurrentView = android.widget.DatePickerCalendarDelegate.UNINITIALIZED;

    private final android.icu.util.Calendar mTempDate;

    private final android.icu.util.Calendar mMinDate;

    private final android.icu.util.Calendar mMaxDate;

    private int mFirstDayOfWeek = android.widget.DatePickerCalendarDelegate.USE_LOCALE;

    public DatePickerCalendarDelegate(android.widget.DatePicker delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        final java.util.Locale locale = mCurrentLocale;
        mCurrentDate = android.icu.util.Calendar.getInstance(locale);
        mTempDate = android.icu.util.Calendar.getInstance(locale);
        mMinDate = android.icu.util.Calendar.getInstance(locale);
        mMaxDate = android.icu.util.Calendar.getInstance(locale);
        mMinDate.set(android.widget.DatePickerCalendarDelegate.DEFAULT_START_YEAR, Calendar.JANUARY, 1);
        mMaxDate.set(android.widget.DatePickerCalendarDelegate.DEFAULT_END_YEAR, Calendar.DECEMBER, 31);
        final android.content.res.Resources res = mDelegator.getResources();
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        final android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final int layoutResourceId = a.getResourceId(R.styleable.DatePicker_internalLayout, R.layout.date_picker_material);
        // Set up and attach container.
        mContainer = ((android.view.ViewGroup) (inflater.inflate(layoutResourceId, mDelegator, false)));
        mContainer.setSaveFromParentEnabled(false);
        mDelegator.addView(mContainer);
        // Set up header views.
        final android.view.ViewGroup header = mContainer.findViewById(R.id.date_picker_header);
        mHeaderYear = header.findViewById(R.id.date_picker_header_year);
        mHeaderYear.setOnClickListener(mOnHeaderClickListener);
        mHeaderMonthDay = header.findViewById(R.id.date_picker_header_date);
        mHeaderMonthDay.setOnClickListener(mOnHeaderClickListener);
        // For the sake of backwards compatibility, attempt to extract the text
        // color from the header month text appearance. If it's set, we'll let
        // that override the "real" header text color.
        android.content.res.ColorStateList headerTextColor = null;
        @java.lang.SuppressWarnings("deprecation")
        final int monthHeaderTextAppearance = a.getResourceId(R.styleable.DatePicker_headerMonthTextAppearance, 0);
        if (monthHeaderTextAppearance != 0) {
            final android.content.res.TypedArray textAppearance = mContext.obtainStyledAttributes(null, android.widget.DatePickerCalendarDelegate.ATTRS_TEXT_COLOR, 0, monthHeaderTextAppearance);
            final android.content.res.ColorStateList legacyHeaderTextColor = textAppearance.getColorStateList(0);
            headerTextColor = applyLegacyColorFixes(legacyHeaderTextColor);
            textAppearance.recycle();
        }
        if (headerTextColor == null) {
            headerTextColor = a.getColorStateList(R.styleable.DatePicker_headerTextColor);
        }
        if (headerTextColor != null) {
            mHeaderYear.setTextColor(headerTextColor);
            mHeaderMonthDay.setTextColor(headerTextColor);
        }
        // Set up header background, if available.
        if (a.hasValueOrEmpty(R.styleable.DatePicker_headerBackground)) {
            header.setBackground(a.getDrawable(R.styleable.DatePicker_headerBackground));
        }
        a.recycle();
        // Set up picker container.
        mAnimator = mContainer.findViewById(R.id.animator);
        // Set up day picker view.
        mDayPickerView = mAnimator.findViewById(R.id.date_picker_day_picker);
        mDayPickerView.setFirstDayOfWeek(mFirstDayOfWeek);
        mDayPickerView.setMinDate(mMinDate.getTimeInMillis());
        mDayPickerView.setMaxDate(mMaxDate.getTimeInMillis());
        mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
        mDayPickerView.setOnDaySelectedListener(mOnDaySelectedListener);
        // Set up year picker view.
        mYearPickerView = mAnimator.findViewById(R.id.date_picker_year_picker);
        mYearPickerView.setRange(mMinDate, mMaxDate);
        mYearPickerView.setYear(mCurrentDate.get(Calendar.YEAR));
        mYearPickerView.setOnYearSelectedListener(mOnYearSelectedListener);
        // Set up content descriptions.
        mSelectDay = res.getString(R.string.select_day);
        mSelectYear = res.getString(R.string.select_year);
        // Initialize for current locale. This also initializes the date, so no
        // need to call onDateChanged.
        onLocaleChanged(mCurrentLocale);
        setCurrentView(android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY);
    }

    /**
     * The legacy text color might have been poorly defined. Ensures that it
     * has an appropriate activated state, using the selected state if one
     * exists or modifying the default text color otherwise.
     *
     * @param color
     * 		a legacy text color, or {@code null}
     * @return a color state list with an appropriate activated state, or
    {@code null} if a valid activated state could not be generated
     */
    @android.annotation.Nullable
    private android.content.res.ColorStateList applyLegacyColorFixes(@android.annotation.Nullable
    android.content.res.ColorStateList color) {
        if ((color == null) || color.hasState(R.attr.state_activated)) {
            return color;
        }
        final int activatedColor;
        final int defaultColor;
        if (color.hasState(R.attr.state_selected)) {
            activatedColor = color.getColorForState(android.util.StateSet.get(android.util.StateSet.VIEW_STATE_ENABLED | android.util.StateSet.VIEW_STATE_SELECTED), 0);
            defaultColor = color.getColorForState(android.util.StateSet.get(StateSet.VIEW_STATE_ENABLED), 0);
        } else {
            activatedColor = color.getDefaultColor();
            // Generate a non-activated color using the disabled alpha.
            final android.content.res.TypedArray ta = mContext.obtainStyledAttributes(android.widget.DatePickerCalendarDelegate.ATTRS_DISABLED_ALPHA);
            final float disabledAlpha = ta.getFloat(0, 0.3F);
            defaultColor = multiplyAlphaComponent(activatedColor, disabledAlpha);
        }
        if ((activatedColor == 0) || (defaultColor == 0)) {
            // We somehow failed to obtain the colors.
            return null;
        }
        final int[][] stateSet = new int[][]{ new int[]{ R.attr.state_activated }, new int[]{  } };
        final int[] colors = new int[]{ activatedColor, defaultColor };
        return new android.content.res.ColorStateList(stateSet, colors);
    }

    private int multiplyAlphaComponent(int color, float alphaMod) {
        final int srcRgb = color & 0xffffff;
        final int srcAlpha = (color >> 24) & 0xff;
        final int dstAlpha = ((int) ((srcAlpha * alphaMod) + 0.5F));
        return srcRgb | (dstAlpha << 24);
    }

    /**
     * Listener called when the user selects a day in the day picker view.
     */
    private final android.widget.DayPickerView.OnDaySelectedListener mOnDaySelectedListener = new android.widget.DayPickerView.OnDaySelectedListener() {
        @java.lang.Override
        public void onDaySelected(android.widget.DayPickerView view, android.icu.util.Calendar day) {
            mCurrentDate.setTimeInMillis(day.getTimeInMillis());
            onDateChanged(true, true);
        }
    };

    /**
     * Listener called when the user selects a year in the year picker view.
     */
    private final android.widget.YearPickerView.OnYearSelectedListener mOnYearSelectedListener = new android.widget.YearPickerView.OnYearSelectedListener() {
        @java.lang.Override
        public void onYearChanged(android.widget.YearPickerView view, int year) {
            // If the newly selected month / year does not contain the
            // currently selected day number, change the selected day number
            // to the last day of the selected month or year.
            // e.g. Switching from Mar to Apr when Mar 31 is selected -> Apr 30
            // e.g. Switching from 2012 to 2013 when Feb 29, 2012 is selected -> Feb 28, 2013
            final int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
            final int month = mCurrentDate.get(Calendar.MONTH);
            final int daysInMonth = android.widget.DatePickerCalendarDelegate.getDaysInMonth(month, year);
            if (day > daysInMonth) {
                mCurrentDate.set(Calendar.DAY_OF_MONTH, daysInMonth);
            }
            mCurrentDate.set(Calendar.YEAR, year);
            onDateChanged(true, true);
            // Automatically switch to day picker.
            setCurrentView(android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY);
            // Switch focus back to the year text.
            mHeaderYear.requestFocus();
        }
    };

    /**
     * Listener called when the user clicks on a header item.
     */
    private final android.view.View.OnClickListener mOnHeaderClickListener = ( v) -> {
        tryVibrate();
        switch (v.getId()) {
            case R.id.date_picker_header_year :
                setCurrentView(android.widget.DatePickerCalendarDelegate.VIEW_YEAR);
                break;
            case R.id.date_picker_header_date :
                setCurrentView(android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY);
                break;
        }
    };

    @java.lang.Override
    protected void onLocaleChanged(java.util.Locale locale) {
        final android.widget.TextView headerYear = mHeaderYear;
        if (headerYear == null) {
            // Abort, we haven't initialized yet. This method will get called
            // again later after everything has been set up.
            return;
        }
        // Update the date formatter.
        mMonthDayFormat = android.icu.text.DateFormat.getInstanceForSkeleton("EMMMd", locale);
        mMonthDayFormat.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        mYearFormat = android.icu.text.DateFormat.getInstanceForSkeleton("y", locale);
        // Update the header text.
        onCurrentDateChanged(false);
    }

    private void onCurrentDateChanged(boolean announce) {
        if (mHeaderYear == null) {
            // Abort, we haven't initialized yet. This method will get called
            // again later after everything has been set up.
            return;
        }
        final java.lang.String year = mYearFormat.format(mCurrentDate.getTime());
        mHeaderYear.setText(year);
        final java.lang.String monthDay = mMonthDayFormat.format(mCurrentDate.getTime());
        mHeaderMonthDay.setText(monthDay);
        // TODO: This should use live regions.
        if (announce) {
            mAnimator.announceForAccessibility(getFormattedCurrentDate());
        }
    }

    private void setCurrentView(final int viewIndex) {
        switch (viewIndex) {
            case android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY :
                mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
                if (mCurrentView != viewIndex) {
                    mHeaderMonthDay.setActivated(true);
                    mHeaderYear.setActivated(false);
                    mAnimator.setDisplayedChild(android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY);
                    mCurrentView = viewIndex;
                }
                mAnimator.announceForAccessibility(mSelectDay);
                break;
            case android.widget.DatePickerCalendarDelegate.VIEW_YEAR :
                final int year = mCurrentDate.get(Calendar.YEAR);
                mYearPickerView.setYear(year);
                mYearPickerView.post(() -> {
                    mYearPickerView.requestFocus();
                    final android.view.View selected = mYearPickerView.getSelectedView();
                    if (selected != null) {
                        selected.requestFocus();
                    }
                });
                if (mCurrentView != viewIndex) {
                    mHeaderMonthDay.setActivated(false);
                    mHeaderYear.setActivated(true);
                    mAnimator.setDisplayedChild(android.widget.DatePickerCalendarDelegate.VIEW_YEAR);
                    mCurrentView = viewIndex;
                }
                mAnimator.announceForAccessibility(mSelectYear);
                break;
        }
    }

    @java.lang.Override
    public void init(int year, int month, int dayOfMonth, android.widget.DatePicker.OnDateChangedListener callBack) {
        setDate(year, month, dayOfMonth);
        onDateChanged(false, false);
        mOnDateChangedListener = callBack;
    }

    @java.lang.Override
    public void updateDate(int year, int month, int dayOfMonth) {
        setDate(year, month, dayOfMonth);
        onDateChanged(false, true);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(Calendar.YEAR, year);
        mCurrentDate.set(Calendar.MONTH, month);
        mCurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        resetAutofilledValue();
    }

    private void onDateChanged(boolean fromUser, boolean callbackToClient) {
        final int year = mCurrentDate.get(Calendar.YEAR);
        if (callbackToClient && ((mOnDateChangedListener != null) || (mAutoFillChangeListener != null))) {
            final int monthOfYear = mCurrentDate.get(Calendar.MONTH);
            final int dayOfMonth = mCurrentDate.get(Calendar.DAY_OF_MONTH);
            if (mOnDateChangedListener != null) {
                mOnDateChangedListener.onDateChanged(mDelegator, year, monthOfYear, dayOfMonth);
            }
            if (mAutoFillChangeListener != null) {
                mAutoFillChangeListener.onDateChanged(mDelegator, year, monthOfYear, dayOfMonth);
            }
        }
        mDayPickerView.setDate(mCurrentDate.getTimeInMillis());
        mYearPickerView.setYear(year);
        onCurrentDateChanged(fromUser);
        if (fromUser) {
            tryVibrate();
        }
    }

    @java.lang.Override
    public int getYear() {
        return mCurrentDate.get(Calendar.YEAR);
    }

    @java.lang.Override
    public int getMonth() {
        return mCurrentDate.get(Calendar.MONTH);
    }

    @java.lang.Override
    public int getDayOfMonth() {
        return mCurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    @java.lang.Override
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if ((mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)) && (mTempDate.get(Calendar.DAY_OF_YEAR) == mMinDate.get(Calendar.DAY_OF_YEAR))) {
            // Same day, no-op.
            return;
        }
        if (mCurrentDate.before(mTempDate)) {
            mCurrentDate.setTimeInMillis(minDate);
            onDateChanged(false, true);
        }
        mMinDate.setTimeInMillis(minDate);
        mDayPickerView.setMinDate(minDate);
        mYearPickerView.setRange(mMinDate, mMaxDate);
    }

    @java.lang.Override
    public android.icu.util.Calendar getMinDate() {
        return mMinDate;
    }

    @java.lang.Override
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if ((mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)) && (mTempDate.get(Calendar.DAY_OF_YEAR) == mMaxDate.get(Calendar.DAY_OF_YEAR))) {
            // Same day, no-op.
            return;
        }
        if (mCurrentDate.after(mTempDate)) {
            mCurrentDate.setTimeInMillis(maxDate);
            onDateChanged(false, true);
        }
        mMaxDate.setTimeInMillis(maxDate);
        mDayPickerView.setMaxDate(maxDate);
        mYearPickerView.setRange(mMinDate, mMaxDate);
    }

    @java.lang.Override
    public android.icu.util.Calendar getMaxDate() {
        return mMaxDate;
    }

    @java.lang.Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        mDayPickerView.setFirstDayOfWeek(firstDayOfWeek);
    }

    @java.lang.Override
    public int getFirstDayOfWeek() {
        if (mFirstDayOfWeek != android.widget.DatePickerCalendarDelegate.USE_LOCALE) {
            return mFirstDayOfWeek;
        }
        return mCurrentDate.getFirstDayOfWeek();
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        mContainer.setEnabled(enabled);
        mDayPickerView.setEnabled(enabled);
        mYearPickerView.setEnabled(enabled);
        mHeaderYear.setEnabled(enabled);
        mHeaderMonthDay.setEnabled(enabled);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mContainer.isEnabled();
    }

    @java.lang.Override
    public android.widget.CalendarView getCalendarView() {
        throw new java.lang.UnsupportedOperationException("Not supported by calendar-mode DatePicker");
    }

    @java.lang.Override
    public void setCalendarViewShown(boolean shown) {
        // No-op for compatibility with the old DatePicker.
    }

    @java.lang.Override
    public boolean getCalendarViewShown() {
        return false;
    }

    @java.lang.Override
    public void setSpinnersShown(boolean shown) {
        // No-op for compatibility with the old DatePicker.
    }

    @java.lang.Override
    public boolean getSpinnersShown() {
        return false;
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState) {
        final int year = mCurrentDate.get(Calendar.YEAR);
        final int month = mCurrentDate.get(Calendar.MONTH);
        final int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int listPosition = -1;
        int listPositionOffset = -1;
        if (mCurrentView == android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY) {
            listPosition = mDayPickerView.getMostVisiblePosition();
        } else
            if (mCurrentView == android.widget.DatePickerCalendarDelegate.VIEW_YEAR) {
                listPosition = mYearPickerView.getFirstVisiblePosition();
                listPositionOffset = mYearPickerView.getFirstPositionOffset();
            }

        return new android.widget.DatePicker.AbstractDatePickerDelegate.SavedState(superState, year, month, day, mMinDate.getTimeInMillis(), mMaxDate.getTimeInMillis(), mCurrentView, listPosition, listPositionOffset);
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (state instanceof android.widget.DatePicker.AbstractDatePickerDelegate.SavedState) {
            final android.widget.DatePicker.AbstractDatePickerDelegate.SavedState ss = ((android.widget.DatePicker.AbstractDatePickerDelegate.SavedState) (state));
            // TODO: Move instance state into DayPickerView, YearPickerView.
            mCurrentDate.set(ss.getSelectedYear(), ss.getSelectedMonth(), ss.getSelectedDay());
            mMinDate.setTimeInMillis(ss.getMinDate());
            mMaxDate.setTimeInMillis(ss.getMaxDate());
            onCurrentDateChanged(false);
            final int currentView = ss.getCurrentView();
            setCurrentView(currentView);
            final int listPosition = ss.getListPosition();
            if (listPosition != (-1)) {
                if (currentView == android.widget.DatePickerCalendarDelegate.VIEW_MONTH_DAY) {
                    mDayPickerView.setPosition(listPosition);
                } else
                    if (currentView == android.widget.DatePickerCalendarDelegate.VIEW_YEAR) {
                        final int listPositionOffset = ss.getListPositionOffset();
                        mYearPickerView.setSelectionFromTop(listPosition, listPositionOffset);
                    }

            }
        }
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.DatePicker.class.getName();
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case android.icu.util.Calendar.JANUARY :
            case android.icu.util.Calendar.MARCH :
            case android.icu.util.Calendar.MAY :
            case android.icu.util.Calendar.JULY :
            case android.icu.util.Calendar.AUGUST :
            case android.icu.util.Calendar.OCTOBER :
            case android.icu.util.Calendar.DECEMBER :
                return 31;
            case android.icu.util.Calendar.APRIL :
            case android.icu.util.Calendar.JUNE :
            case android.icu.util.Calendar.SEPTEMBER :
            case android.icu.util.Calendar.NOVEMBER :
                return 30;
            case android.icu.util.Calendar.FEBRUARY :
                return (year % 4) == 0 ? 29 : 28;
            default :
                throw new java.lang.IllegalArgumentException("Invalid Month");
        }
    }

    private void tryVibrate() {
        mDelegator.performHapticFeedback(android.view.HapticFeedbackConstants.CALENDAR_DATE);
    }
}

