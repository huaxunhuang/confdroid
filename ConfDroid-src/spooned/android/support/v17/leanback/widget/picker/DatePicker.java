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
 * {@link DatePicker} is a directly subclass of {@link Picker}.
 * This class is a widget for selecting a date. The date can be selected by a
 * year, month, and day Columns. The "minDate" and "maxDate" from which dates to be selected
 * can be customized.  The columns can be customized by attribute "datePickerFormat" or
 * {@link #setDatePickerFormat(String)}.
 *
 * @unknown ref R.styleable#lbDatePicker_android_maxDate
 * @unknown ref R.styleable#lbDatePicker_android_minDate
 * @unknown ref R.styleable#lbDatePicker_datePickerFormat
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class DatePicker extends android.support.v17.leanback.widget.picker.Picker {
    static final java.lang.String LOG_TAG = "DatePicker";

    private java.lang.String mDatePickerFormat;

    android.support.v17.leanback.widget.picker.PickerColumn mMonthColumn;

    android.support.v17.leanback.widget.picker.PickerColumn mDayColumn;

    android.support.v17.leanback.widget.picker.PickerColumn mYearColumn;

    int mColMonthIndex;

    int mColDayIndex;

    int mColYearIndex;

    static final java.lang.String DATE_FORMAT = "MM/dd/yyyy";

    final java.text.DateFormat mDateFormat = new java.text.SimpleDateFormat(android.support.v17.leanback.widget.picker.DatePicker.DATE_FORMAT);

    android.support.v17.leanback.widget.picker.PickerConstant mConstant;

    java.util.Calendar mMinDate;

    java.util.Calendar mMaxDate;

    java.util.Calendar mCurrentDate;

    java.util.Calendar mTempDate;

    public DatePicker(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateCurrentLocale();
        setSeparator(mConstant.dateSeparator);
        final android.content.res.TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.lbDatePicker);
        java.lang.String minDate = attributesArray.getString(R.styleable.lbDatePicker_android_minDate);
        java.lang.String maxDate = attributesArray.getString(R.styleable.lbDatePicker_android_maxDate);
        mTempDate.clear();
        if (!android.text.TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate.set(1900, 0, 1);
            }
        } else {
            mTempDate.set(1900, 0, 1);
        }
        mMinDate.setTimeInMillis(mTempDate.getTimeInMillis());
        mTempDate.clear();
        if (!android.text.TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate.set(2100, 0, 1);
            }
        } else {
            mTempDate.set(2100, 0, 1);
        }
        mMaxDate.setTimeInMillis(mTempDate.getTimeInMillis());
        java.lang.String datePickerFormat = attributesArray.getString(R.styleable.lbDatePicker_datePickerFormat);
        if (android.text.TextUtils.isEmpty(datePickerFormat)) {
            datePickerFormat = new java.lang.String(android.text.format.DateFormat.getDateFormatOrder(context));
        }
        setDatePickerFormat(datePickerFormat);
    }

    private boolean parseDate(java.lang.String date, java.util.Calendar outDate) {
        try {
            outDate.setTime(mDateFormat.parse(date));
            return true;
        } catch (java.text.ParseException e) {
            android.util.Log.w(android.support.v17.leanback.widget.picker.DatePicker.LOG_TAG, (("Date: " + date) + " not in format: ") + android.support.v17.leanback.widget.picker.DatePicker.DATE_FORMAT);
            return false;
        }
    }

    /**
     * Changes format of showing dates.  For example "YMD".
     *
     * @param datePickerFormat
     * 		Format of showing dates.
     */
    public void setDatePickerFormat(java.lang.String datePickerFormat) {
        if (android.text.TextUtils.isEmpty(datePickerFormat)) {
            datePickerFormat = new java.lang.String(android.text.format.DateFormat.getDateFormatOrder(getContext()));
        }
        datePickerFormat = datePickerFormat.toUpperCase();
        if (android.text.TextUtils.equals(mDatePickerFormat, datePickerFormat)) {
            return;
        }
        mDatePickerFormat = datePickerFormat;
        mYearColumn = mMonthColumn = mDayColumn = null;
        mColYearIndex = mColDayIndex = mColMonthIndex = -1;
        java.util.ArrayList<android.support.v17.leanback.widget.picker.PickerColumn> columns = new java.util.ArrayList<android.support.v17.leanback.widget.picker.PickerColumn>(3);
        for (int i = 0; i < datePickerFormat.length(); i++) {
            switch (datePickerFormat.charAt(i)) {
                case 'Y' :
                    if (mYearColumn != null) {
                        throw new java.lang.IllegalArgumentException("datePicker format error");
                    }
                    columns.add(mYearColumn = new android.support.v17.leanback.widget.picker.PickerColumn());
                    mColYearIndex = i;
                    mYearColumn.setLabelFormat("%d");
                    break;
                case 'M' :
                    if (mMonthColumn != null) {
                        throw new java.lang.IllegalArgumentException("datePicker format error");
                    }
                    columns.add(mMonthColumn = new android.support.v17.leanback.widget.picker.PickerColumn());
                    mMonthColumn.setStaticLabels(mConstant.months);
                    mColMonthIndex = i;
                    break;
                case 'D' :
                    if (mDayColumn != null) {
                        throw new java.lang.IllegalArgumentException("datePicker format error");
                    }
                    columns.add(mDayColumn = new android.support.v17.leanback.widget.picker.PickerColumn());
                    mDayColumn.setLabelFormat("%02d");
                    mColDayIndex = i;
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("datePicker format error");
            }
        }
        setColumns(columns);
        updateSpinners(false);
    }

    /**
     * Get format of showing dates.  For example "YMD".  Default value is from
     * {@link android.text.format.DateFormat#getDateFormatOrder(Context)}.
     *
     * @return Format of showing dates.
     */
    public java.lang.String getDatePickerFormat() {
        return mDatePickerFormat;
    }

    private java.util.Calendar getCalendarForLocale(java.util.Calendar oldCalendar, java.util.Locale locale) {
        if (oldCalendar == null) {
            return java.util.Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            java.util.Calendar newCalendar = java.util.Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    private void updateCurrentLocale() {
        mConstant = new android.support.v17.leanback.widget.picker.PickerConstant(java.util.Locale.getDefault(), getContext().getResources());
        mTempDate = getCalendarForLocale(mTempDate, mConstant.locale);
        mMinDate = getCalendarForLocale(mMinDate, mConstant.locale);
        mMaxDate = getCalendarForLocale(mMaxDate, mConstant.locale);
        mCurrentDate = getCalendarForLocale(mCurrentDate, mConstant.locale);
        if (mMonthColumn != null) {
            mMonthColumn.setStaticLabels(mConstant.months);
            setColumnAt(mColMonthIndex, mMonthColumn);
        }
    }

    @java.lang.Override
    public final void onColumnValueChanged(int column, int newVal) {
        mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
        // take care of wrapping of days and months to update greater fields
        int oldVal = getColumnAt(column).getCurrentValue();
        if (column == mColDayIndex) {
            mTempDate.add(java.util.Calendar.DAY_OF_MONTH, newVal - oldVal);
        } else
            if (column == mColMonthIndex) {
                mTempDate.add(java.util.Calendar.MONTH, newVal - oldVal);
            } else
                if (column == mColYearIndex) {
                    mTempDate.add(java.util.Calendar.YEAR, newVal - oldVal);
                } else {
                    throw new java.lang.IllegalArgumentException();
                }


        setDate(mTempDate.get(java.util.Calendar.YEAR), mTempDate.get(java.util.Calendar.MONTH), mTempDate.get(java.util.Calendar.DAY_OF_MONTH));
        updateSpinners(false);
    }

    /**
     * Sets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate
     * 		The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if ((mTempDate.get(java.util.Calendar.YEAR) == mMinDate.get(java.util.Calendar.YEAR)) && (mTempDate.get(java.util.Calendar.DAY_OF_YEAR) != mMinDate.get(java.util.Calendar.DAY_OF_YEAR))) {
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        }
        updateSpinners(false);
    }

    /**
     * Gets the minimal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default minimal date is 01/01/1900.
     * <p>
     *
     * @return The minimal supported date.
     */
    public long getMinDate() {
        return mMinDate.getTimeInMillis();
    }

    /**
     * Sets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate
     * 		The maximal supported date.
     */
    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if ((mTempDate.get(java.util.Calendar.YEAR) == mMaxDate.get(java.util.Calendar.YEAR)) && (mTempDate.get(java.util.Calendar.DAY_OF_YEAR) != mMaxDate.get(java.util.Calendar.DAY_OF_YEAR))) {
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
        updateSpinners(false);
    }

    /**
     * Gets the maximal date supported by this {@link DatePicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     * <p>
     * Note: The default maximal date is 12/31/2100.
     * <p>
     *
     * @return The maximal supported date.
     */
    public long getMaxDate() {
        return mMaxDate.getTimeInMillis();
    }

    /**
     * Gets current date value in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return Current date values.
     */
    public long getDate() {
        return mCurrentDate.getTimeInMillis();
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(year, month, dayOfMonth);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else
            if (mCurrentDate.after(mMaxDate)) {
                mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
            }

    }

    /**
     * Update the current date.
     *
     * @param year
     * 		The year.
     * @param month
     * 		The month which is <strong>starting from zero</strong>.
     * @param dayOfMonth
     * 		The day of the month.
     * @param animation
     * 		True to run animation to scroll the column.
     */
    public void updateDate(int year, int month, int dayOfMonth, boolean animation) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return;
        }
        setDate(year, month, dayOfMonth);
        updateSpinners(animation);
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return ((mCurrentDate.get(java.util.Calendar.YEAR) != year) || (mCurrentDate.get(java.util.Calendar.MONTH) != dayOfMonth)) || (mCurrentDate.get(java.util.Calendar.DAY_OF_MONTH) != month);
    }

    private static boolean updateMin(android.support.v17.leanback.widget.picker.PickerColumn column, int value) {
        if (value != column.getMinValue()) {
            column.setMinValue(value);
            return true;
        }
        return false;
    }

    private static boolean updateMax(android.support.v17.leanback.widget.picker.PickerColumn column, int value) {
        if (value != column.getMaxValue()) {
            column.setMaxValue(value);
            return true;
        }
        return false;
    }

    private static int[] DATE_FIELDS = new int[]{ java.util.Calendar.DAY_OF_MONTH, java.util.Calendar.MONTH, java.util.Calendar.YEAR };

    // Following implementation always keeps up-to-date date ranges (min & max values) no matter
    // what the currently selected date is. This prevents the constant updating of date values while
    // scrolling vertically and thus fixes the animation jumps that used to happen when we reached
    // the endpoint date field values since the adapter values do not change while scrolling up
    // & down across a single field.
    void updateSpinnersImpl(boolean animation) {
        // set the spinner ranges respecting the min and max dates
        int[] dateFieldIndices = new int[]{ mColDayIndex, mColMonthIndex, mColYearIndex };
        boolean allLargerDateFieldsHaveBeenEqualToMinDate = true;
        boolean allLargerDateFieldsHaveBeenEqualToMaxDate = true;
        for (int i = android.support.v17.leanback.widget.picker.DatePicker.DATE_FIELDS.length - 1; i >= 0; i--) {
            boolean dateFieldChanged = false;
            if (dateFieldIndices[i] < 0)
                continue;

            int currField = android.support.v17.leanback.widget.picker.DatePicker.DATE_FIELDS[i];
            android.support.v17.leanback.widget.picker.PickerColumn currPickerColumn = getColumnAt(dateFieldIndices[i]);
            if (allLargerDateFieldsHaveBeenEqualToMinDate) {
                dateFieldChanged |= android.support.v17.leanback.widget.picker.DatePicker.updateMin(currPickerColumn, mMinDate.get(currField));
            } else {
                dateFieldChanged |= android.support.v17.leanback.widget.picker.DatePicker.updateMin(currPickerColumn, mCurrentDate.getActualMinimum(currField));
            }
            if (allLargerDateFieldsHaveBeenEqualToMaxDate) {
                dateFieldChanged |= android.support.v17.leanback.widget.picker.DatePicker.updateMax(currPickerColumn, mMaxDate.get(currField));
            } else {
                dateFieldChanged |= android.support.v17.leanback.widget.picker.DatePicker.updateMax(currPickerColumn, mCurrentDate.getActualMaximum(currField));
            }
            allLargerDateFieldsHaveBeenEqualToMinDate &= mCurrentDate.get(currField) == mMinDate.get(currField);
            allLargerDateFieldsHaveBeenEqualToMaxDate &= mCurrentDate.get(currField) == mMaxDate.get(currField);
            if (dateFieldChanged) {
                setColumnAt(dateFieldIndices[i], currPickerColumn);
            }
            setColumnValue(dateFieldIndices[i], mCurrentDate.get(currField), animation);
        }
    }

    private void updateSpinners(final boolean animation) {
        // update range in a post call.  The reason is that RV does not allow notifyDataSetChange()
        // in scroll pass.  UpdateSpinner can be called in a scroll pass, UpdateSpinner() may
        // notifyDataSetChange to update the range.
        post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                updateSpinnersImpl(animation);
            }
        });
    }
}

