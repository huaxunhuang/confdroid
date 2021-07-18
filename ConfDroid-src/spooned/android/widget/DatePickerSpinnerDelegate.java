/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * A delegate implementing the basic DatePicker
 */
class DatePickerSpinnerDelegate extends android.widget.DatePicker.AbstractDatePickerDelegate {
    private static final java.lang.String DATE_FORMAT = "MM/dd/yyyy";

    private static final int DEFAULT_START_YEAR = 1900;

    private static final int DEFAULT_END_YEAR = 2100;

    private static final boolean DEFAULT_CALENDAR_VIEW_SHOWN = true;

    private static final boolean DEFAULT_SPINNERS_SHOWN = true;

    private static final boolean DEFAULT_ENABLED_STATE = true;

    private final android.widget.LinearLayout mSpinners;

    private final android.widget.NumberPicker mDaySpinner;

    private final android.widget.NumberPicker mMonthSpinner;

    private final android.widget.NumberPicker mYearSpinner;

    private final android.widget.EditText mDaySpinnerInput;

    private final android.widget.EditText mMonthSpinnerInput;

    private final android.widget.EditText mYearSpinnerInput;

    private final android.widget.CalendarView mCalendarView;

    private java.lang.String[] mShortMonths;

    private final java.text.DateFormat mDateFormat = new java.text.SimpleDateFormat(android.widget.DatePickerSpinnerDelegate.DATE_FORMAT);

    private int mNumberOfMonths;

    private android.icu.util.Calendar mTempDate;

    private android.icu.util.Calendar mMinDate;

    private android.icu.util.Calendar mMaxDate;

    private boolean mIsEnabled = android.widget.DatePickerSpinnerDelegate.DEFAULT_ENABLED_STATE;

    DatePickerSpinnerDelegate(android.widget.DatePicker delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        mDelegator = delegator;
        mContext = context;
        // initialization based on locale
        setCurrentLocale(java.util.Locale.getDefault());
        final android.content.res.TypedArray attributesArray = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.DatePicker, defStyleAttr, defStyleRes);
        boolean spinnersShown = attributesArray.getBoolean(com.android.internal.R.styleable.DatePicker_spinnersShown, android.widget.DatePickerSpinnerDelegate.DEFAULT_SPINNERS_SHOWN);
        boolean calendarViewShown = attributesArray.getBoolean(com.android.internal.R.styleable.DatePicker_calendarViewShown, android.widget.DatePickerSpinnerDelegate.DEFAULT_CALENDAR_VIEW_SHOWN);
        int startYear = attributesArray.getInt(com.android.internal.R.styleable.DatePicker_startYear, android.widget.DatePickerSpinnerDelegate.DEFAULT_START_YEAR);
        int endYear = attributesArray.getInt(com.android.internal.R.styleable.DatePicker_endYear, android.widget.DatePickerSpinnerDelegate.DEFAULT_END_YEAR);
        java.lang.String minDate = attributesArray.getString(com.android.internal.R.styleable.DatePicker_minDate);
        java.lang.String maxDate = attributesArray.getString(com.android.internal.R.styleable.DatePicker_maxDate);
        int layoutResourceId = attributesArray.getResourceId(com.android.internal.R.styleable.DatePicker_legacyLayout, com.android.internal.R.layout.date_picker_legacy);
        attributesArray.recycle();
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final android.view.View view = inflater.inflate(layoutResourceId, mDelegator, true);
        view.setSaveFromParentEnabled(false);
        android.widget.NumberPicker.OnValueChangeListener onChangeListener = new android.widget.NumberPicker.OnValueChangeListener() {
            public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                updateInputState();
                mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
                // take care of wrapping of days and months to update greater fields
                if (picker == mDaySpinner) {
                    int maxDayOfMonth = mTempDate.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if ((oldVal == maxDayOfMonth) && (newVal == 1)) {
                        mTempDate.add(Calendar.DAY_OF_MONTH, 1);
                    } else
                        if ((oldVal == 1) && (newVal == maxDayOfMonth)) {
                            mTempDate.add(Calendar.DAY_OF_MONTH, -1);
                        } else {
                            mTempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
                        }

                } else
                    if (picker == mMonthSpinner) {
                        if ((oldVal == 11) && (newVal == 0)) {
                            mTempDate.add(Calendar.MONTH, 1);
                        } else
                            if ((oldVal == 0) && (newVal == 11)) {
                                mTempDate.add(Calendar.MONTH, -1);
                            } else {
                                mTempDate.add(Calendar.MONTH, newVal - oldVal);
                            }

                    } else
                        if (picker == mYearSpinner) {
                            mTempDate.set(Calendar.YEAR, newVal);
                        } else {
                            throw new java.lang.IllegalArgumentException();
                        }


                // now set the date to the adjusted one
                setDate(mTempDate.get(Calendar.YEAR), mTempDate.get(Calendar.MONTH), mTempDate.get(Calendar.DAY_OF_MONTH));
                updateSpinners();
                updateCalendarView();
                notifyDateChanged();
            }
        };
        mSpinners = ((android.widget.LinearLayout) (mDelegator.findViewById(com.android.internal.R.id.pickers)));
        // calendar view day-picker
        mCalendarView = ((android.widget.CalendarView) (mDelegator.findViewById(com.android.internal.R.id.calendar_view)));
        mCalendarView.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int monthDay) {
                setDate(year, month, monthDay);
                updateSpinners();
                notifyDateChanged();
            }
        });
        // day
        mDaySpinner = ((android.widget.NumberPicker) (mDelegator.findViewById(com.android.internal.R.id.day)));
        mDaySpinner.setFormatter(android.widget.NumberPicker.getTwoDigitFormatter());
        mDaySpinner.setOnLongPressUpdateInterval(100);
        mDaySpinner.setOnValueChangedListener(onChangeListener);
        mDaySpinnerInput = ((android.widget.EditText) (mDaySpinner.findViewById(com.android.internal.R.id.numberpicker_input)));
        // month
        mMonthSpinner = ((android.widget.NumberPicker) (mDelegator.findViewById(com.android.internal.R.id.month)));
        mMonthSpinner.setMinValue(0);
        mMonthSpinner.setMaxValue(mNumberOfMonths - 1);
        mMonthSpinner.setDisplayedValues(mShortMonths);
        mMonthSpinner.setOnLongPressUpdateInterval(200);
        mMonthSpinner.setOnValueChangedListener(onChangeListener);
        mMonthSpinnerInput = ((android.widget.EditText) (mMonthSpinner.findViewById(com.android.internal.R.id.numberpicker_input)));
        // year
        mYearSpinner = ((android.widget.NumberPicker) (mDelegator.findViewById(com.android.internal.R.id.year)));
        mYearSpinner.setOnLongPressUpdateInterval(100);
        mYearSpinner.setOnValueChangedListener(onChangeListener);
        mYearSpinnerInput = ((android.widget.EditText) (mYearSpinner.findViewById(com.android.internal.R.id.numberpicker_input)));
        // show only what the user required but make sure we
        // show something and the spinners have higher priority
        if ((!spinnersShown) && (!calendarViewShown)) {
            setSpinnersShown(true);
        } else {
            setSpinnersShown(spinnersShown);
            setCalendarViewShown(calendarViewShown);
        }
        // set the min date giving priority of the minDate over startYear
        mTempDate.clear();
        if (!android.text.TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate.set(startYear, 0, 1);
            }
        } else {
            mTempDate.set(startYear, 0, 1);
        }
        setMinDate(mTempDate.getTimeInMillis());
        // set the max date giving priority of the maxDate over endYear
        mTempDate.clear();
        if (!android.text.TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate.set(endYear, 11, 31);
            }
        } else {
            mTempDate.set(endYear, 11, 31);
        }
        setMaxDate(mTempDate.getTimeInMillis());
        // initialize to current date
        mCurrentDate.setTimeInMillis(java.lang.System.currentTimeMillis());
        init(mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH), mCurrentDate.get(Calendar.DAY_OF_MONTH), null);
        // re-order the number spinners to match the current date format
        reorderSpinners();
        // accessibility
        setContentDescriptions();
        // If not explicitly specified this view is important for accessibility.
        if (mDelegator.getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            mDelegator.setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    @java.lang.Override
    public void init(int year, int monthOfYear, int dayOfMonth, android.widget.DatePicker.OnDateChangedListener onDateChangedListener) {
        setDate(year, monthOfYear, dayOfMonth);
        updateSpinners();
        updateCalendarView();
        mOnDateChangedListener = onDateChangedListener;
    }

    @java.lang.Override
    public void updateDate(int year, int month, int dayOfMonth) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return;
        }
        setDate(year, month, dayOfMonth);
        updateSpinners();
        updateCalendarView();
        notifyDateChanged();
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
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mCalendarView.setFirstDayOfWeek(firstDayOfWeek);
    }

    @java.lang.Override
    public int getFirstDayOfWeek() {
        return mCalendarView.getFirstDayOfWeek();
    }

    @java.lang.Override
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if ((mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)) && (mTempDate.get(Calendar.DAY_OF_YEAR) == mMinDate.get(Calendar.DAY_OF_YEAR))) {
            // Same day, no-op.
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        mCalendarView.setMinDate(minDate);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
            updateCalendarView();
        }
        updateSpinners();
    }

    @java.lang.Override
    public android.icu.util.Calendar getMinDate() {
        final android.icu.util.Calendar minDate = android.icu.util.Calendar.getInstance();
        minDate.setTimeInMillis(mCalendarView.getMinDate());
        return minDate;
    }

    @java.lang.Override
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if ((mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)) && (mTempDate.get(Calendar.DAY_OF_YEAR) == mMaxDate.get(Calendar.DAY_OF_YEAR))) {
            // Same day, no-op.
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        mCalendarView.setMaxDate(maxDate);
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
            updateCalendarView();
        }
        updateSpinners();
    }

    @java.lang.Override
    public android.icu.util.Calendar getMaxDate() {
        final android.icu.util.Calendar maxDate = android.icu.util.Calendar.getInstance();
        maxDate.setTimeInMillis(mCalendarView.getMaxDate());
        return maxDate;
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        mDaySpinner.setEnabled(enabled);
        mMonthSpinner.setEnabled(enabled);
        mYearSpinner.setEnabled(enabled);
        mCalendarView.setEnabled(enabled);
        mIsEnabled = enabled;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @java.lang.Override
    public android.widget.CalendarView getCalendarView() {
        return mCalendarView;
    }

    @java.lang.Override
    public void setCalendarViewShown(boolean shown) {
        mCalendarView.setVisibility(shown ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @java.lang.Override
    public boolean getCalendarViewShown() {
        return mCalendarView.getVisibility() == android.view.View.VISIBLE;
    }

    @java.lang.Override
    public void setSpinnersShown(boolean shown) {
        mSpinners.setVisibility(shown ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @java.lang.Override
    public boolean getSpinnersShown() {
        return mSpinners.isShown();
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState) {
        return new android.widget.DatePicker.AbstractDatePickerDelegate.SavedState(superState, getYear(), getMonth(), getDayOfMonth(), getMinDate().getTimeInMillis(), getMaxDate().getTimeInMillis());
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (state instanceof android.widget.DatePicker.AbstractDatePickerDelegate.SavedState) {
            final android.widget.DatePicker.AbstractDatePickerDelegate.SavedState ss = ((android.widget.DatePicker.AbstractDatePickerDelegate.SavedState) (state));
            setDate(ss.getSelectedYear(), ss.getSelectedMonth(), ss.getSelectedDay());
            updateSpinners();
            updateCalendarView();
        }
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
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
        mTempDate = getCalendarForLocale(mTempDate, locale);
        mMinDate = getCalendarForLocale(mMinDate, locale);
        mMaxDate = getCalendarForLocale(mMaxDate, locale);
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale);
        mNumberOfMonths = mTempDate.getActualMaximum(Calendar.MONTH) + 1;
        mShortMonths = new java.text.DateFormatSymbols().getShortMonths();
        if (usingNumericMonths()) {
            // We're in a locale where a date should either be all-numeric, or all-text.
            // All-text would require custom NumberPicker formatters for day and year.
            mShortMonths = new java.lang.String[mNumberOfMonths];
            for (int i = 0; i < mNumberOfMonths; ++i) {
                mShortMonths[i] = java.lang.String.format("%d", i + 1);
            }
        }
    }

    /**
     * Tests whether the current locale is one where there are no real month names,
     * such as Chinese, Japanese, or Korean locales.
     */
    private boolean usingNumericMonths() {
        return java.lang.Character.isDigit(mShortMonths[android.icu.util.Calendar.JANUARY].charAt(0));
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given calendar.
     *
     * @param oldCalendar
     * 		The old calendar.
     * @param locale
     * 		The locale.
     */
    private android.icu.util.Calendar getCalendarForLocale(android.icu.util.Calendar oldCalendar, java.util.Locale locale) {
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
     * Reorders the spinners according to the date format that is
     * explicitly set by the user and if no such is set fall back
     * to the current locale's default format.
     */
    private void reorderSpinners() {
        mSpinners.removeAllViews();
        // We use numeric spinners for year and day, but textual months. Ask icu4c what
        // order the user's locale uses for that combination. http://b/7207103.
        java.lang.String pattern = android.text.format.DateFormat.getBestDateTimePattern(java.util.Locale.getDefault(), "yyyyMMMdd");
        char[] order = libcore.icu.ICU.getDateFormatOrder(pattern);
        final int spinnerCount = order.length;
        for (int i = 0; i < spinnerCount; i++) {
            switch (order[i]) {
                case 'd' :
                    mSpinners.addView(mDaySpinner);
                    setImeOptions(mDaySpinner, spinnerCount, i);
                    break;
                case 'M' :
                    mSpinners.addView(mMonthSpinner);
                    setImeOptions(mMonthSpinner, spinnerCount, i);
                    break;
                case 'y' :
                    mSpinners.addView(mYearSpinner);
                    setImeOptions(mYearSpinner, spinnerCount, i);
                    break;
                default :
                    throw new java.lang.IllegalArgumentException(java.util.Arrays.toString(order));
            }
        }
    }

    /**
     * Parses the given <code>date</code> and in case of success sets the result
     * to the <code>outDate</code>.
     *
     * @return True if the date was parsed.
     */
    private boolean parseDate(java.lang.String date, android.icu.util.Calendar outDate) {
        try {
            outDate.setTime(mDateFormat.parse(date));
            return true;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return ((mCurrentDate.get(Calendar.YEAR) != year) || (mCurrentDate.get(Calendar.MONTH) != month)) || (mCurrentDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth);
    }

    @android.annotation.UnsupportedAppUsage
    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(year, month, dayOfMonth);
        resetAutofilledValue();
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else
            if (mCurrentDate.after(mMaxDate)) {
                mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
            }

    }

    @android.annotation.UnsupportedAppUsage
    private void updateSpinners() {
        // set the spinner ranges respecting the min and max dates
        if (mCurrentDate.equals(mMinDate)) {
            mDaySpinner.setMinValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            mDaySpinner.setWrapSelectorWheel(false);
            mMonthSpinner.setDisplayedValues(null);
            mMonthSpinner.setMinValue(mCurrentDate.get(Calendar.MONTH));
            mMonthSpinner.setMaxValue(mCurrentDate.getActualMaximum(Calendar.MONTH));
            mMonthSpinner.setWrapSelectorWheel(false);
        } else
            if (mCurrentDate.equals(mMaxDate)) {
                mDaySpinner.setMinValue(mCurrentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
                mDaySpinner.setMaxValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
                mDaySpinner.setWrapSelectorWheel(false);
                mMonthSpinner.setDisplayedValues(null);
                mMonthSpinner.setMinValue(mCurrentDate.getActualMinimum(Calendar.MONTH));
                mMonthSpinner.setMaxValue(mCurrentDate.get(Calendar.MONTH));
                mMonthSpinner.setWrapSelectorWheel(false);
            } else {
                mDaySpinner.setMinValue(1);
                mDaySpinner.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                mDaySpinner.setWrapSelectorWheel(true);
                mMonthSpinner.setDisplayedValues(null);
                mMonthSpinner.setMinValue(0);
                mMonthSpinner.setMaxValue(11);
                mMonthSpinner.setWrapSelectorWheel(true);
            }

        // make sure the month names are a zero based array
        // with the months in the month spinner
        java.lang.String[] displayedValues = java.util.Arrays.copyOfRange(mShortMonths, mMonthSpinner.getMinValue(), mMonthSpinner.getMaxValue() + 1);
        mMonthSpinner.setDisplayedValues(displayedValues);
        // year spinner range does not change based on the current date
        mYearSpinner.setMinValue(mMinDate.get(Calendar.YEAR));
        mYearSpinner.setMaxValue(mMaxDate.get(Calendar.YEAR));
        mYearSpinner.setWrapSelectorWheel(false);
        // set the spinner values
        mYearSpinner.setValue(mCurrentDate.get(Calendar.YEAR));
        mMonthSpinner.setValue(mCurrentDate.get(Calendar.MONTH));
        mDaySpinner.setValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        if (usingNumericMonths()) {
            mMonthSpinnerInput.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    /**
     * Updates the calendar view with the current date.
     */
    @android.annotation.UnsupportedAppUsage
    private void updateCalendarView() {
        mCalendarView.setDate(mCurrentDate.getTimeInMillis(), false, false);
    }

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    @android.annotation.UnsupportedAppUsage
    private void notifyDateChanged() {
        mDelegator.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(mDelegator, getYear(), getMonth(), getDayOfMonth());
        }
        if (mAutoFillChangeListener != null) {
            mAutoFillChangeListener.onDateChanged(mDelegator, getYear(), getMonth(), getDayOfMonth());
        }
    }

    /**
     * Sets the IME options for a spinner based on its ordering.
     *
     * @param spinner
     * 		The spinner.
     * @param spinnerCount
     * 		The total spinner count.
     * @param spinnerIndex
     * 		The index of the given spinner.
     */
    private void setImeOptions(android.widget.NumberPicker spinner, int spinnerCount, int spinnerIndex) {
        final int imeOptions;
        if (spinnerIndex < (spinnerCount - 1)) {
            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
        } else {
            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
        }
        android.widget.TextView input = ((android.widget.TextView) (spinner.findViewById(com.android.internal.R.id.numberpicker_input)));
        input.setImeOptions(imeOptions);
    }

    private void setContentDescriptions() {
        // Day
        trySetContentDescription(mDaySpinner, com.android.internal.R.id.increment, com.android.internal.R.string.date_picker_increment_day_button);
        trySetContentDescription(mDaySpinner, com.android.internal.R.id.decrement, com.android.internal.R.string.date_picker_decrement_day_button);
        // Month
        trySetContentDescription(mMonthSpinner, com.android.internal.R.id.increment, com.android.internal.R.string.date_picker_increment_month_button);
        trySetContentDescription(mMonthSpinner, com.android.internal.R.id.decrement, com.android.internal.R.string.date_picker_decrement_month_button);
        // Year
        trySetContentDescription(mYearSpinner, com.android.internal.R.id.increment, com.android.internal.R.string.date_picker_increment_year_button);
        trySetContentDescription(mYearSpinner, com.android.internal.R.id.decrement, com.android.internal.R.string.date_picker_decrement_year_button);
    }

    private void trySetContentDescription(android.view.View root, int viewId, int contDescResId) {
        android.view.View target = root.findViewById(viewId);
        if (target != null) {
            target.setContentDescription(mContext.getString(contDescResId));
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        android.view.inputmethod.InputMethodManager inputMethodManager = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mYearSpinnerInput)) {
                mYearSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
            } else
                if (inputMethodManager.isActive(mMonthSpinnerInput)) {
                    mMonthSpinnerInput.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
                } else
                    if (inputMethodManager.isActive(mDaySpinnerInput)) {
                        mDaySpinnerInput.clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
                    }


        }
    }
}

