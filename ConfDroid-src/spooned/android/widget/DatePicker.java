/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Provides a widget for selecting a date.
 * <p>
 * When the {@link android.R.styleable#DatePicker_datePickerMode} attribute is
 * set to {@code spinner}, the date can be selected using year, month, and day
 * spinners or a {@link CalendarView}. The set of spinners and the calendar
 * view are automatically synchronized. The client can customize whether only
 * the spinners, or only the calendar view, or both to be displayed.
 * </p>
 * <p>
 * When the {@link android.R.styleable#DatePicker_datePickerMode} attribute is
 * set to {@code calendar}, the month and day can be selected using a
 * calendar-style view while the year can be selected separately using a list.
 * </p>
 * <p>
 * See the <a href="{@docRoot }guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 * <p>
 * For a dialog using this view, see {@link android.app.DatePickerDialog}.
 * </p>
 *
 * @unknown ref android.R.styleable#DatePicker_startYear
 * @unknown ref android.R.styleable#DatePicker_endYear
 * @unknown ref android.R.styleable#DatePicker_maxDate
 * @unknown ref android.R.styleable#DatePicker_minDate
 * @unknown ref android.R.styleable#DatePicker_spinnersShown
 * @unknown ref android.R.styleable#DatePicker_calendarViewShown
 * @unknown ref android.R.styleable#DatePicker_dayOfWeekBackground
 * @unknown ref android.R.styleable#DatePicker_dayOfWeekTextAppearance
 * @unknown ref android.R.styleable#DatePicker_headerBackground
 * @unknown ref android.R.styleable#DatePicker_headerMonthTextAppearance
 * @unknown ref android.R.styleable#DatePicker_headerDayOfMonthTextAppearance
 * @unknown ref android.R.styleable#DatePicker_headerYearTextAppearance
 * @unknown ref android.R.styleable#DatePicker_yearListItemTextAppearance
 * @unknown ref android.R.styleable#DatePicker_yearListSelectorColor
 * @unknown ref android.R.styleable#DatePicker_calendarTextColor
 * @unknown ref android.R.styleable#DatePicker_datePickerMode
 */
@android.annotation.Widget
public class DatePicker extends android.widget.FrameLayout {
    private static final java.lang.String LOG_TAG = android.widget.DatePicker.class.getSimpleName();

    /**
     * Presentation mode for the Holo-style date picker that uses a set of
     * {@link android.widget.NumberPicker}s.
     *
     * @see #getMode()
     * @unknown Visible for testing only.
     */
    @android.annotation.TestApi
    public static final int MODE_SPINNER = 1;

    /**
     * Presentation mode for the Material-style date picker that uses a
     * calendar.
     *
     * @see #getMode()
     * @unknown Visible for testing only.
     */
    @android.annotation.TestApi
    public static final int MODE_CALENDAR = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "MODE_" }, value = { android.widget.DatePicker.MODE_SPINNER, android.widget.DatePicker.MODE_CALENDAR })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DatePickerMode {}

    @android.annotation.UnsupportedAppUsage
    private final android.widget.DatePicker.DatePickerDelegate mDelegate;

    @android.widget.DatePicker.DatePickerMode
    private final int mMode;

    /**
     * The callback used to indicate the user changed the date.
     */
    public interface OnDateChangedListener {
        /**
         * Called upon a date change.
         *
         * @param view
         * 		The view associated with this listener.
         * @param year
         * 		The year that was set.
         * @param monthOfYear
         * 		The month that was set (0-11) for compatibility
         * 		with {@link java.util.Calendar}.
         * @param dayOfMonth
         * 		The day of the month that was set.
         */
        void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    public DatePicker(android.content.Context context) {
        this(context, null);
    }

    public DatePicker(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public DatePicker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DatePicker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // DatePicker is important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.DatePicker, attrs, a, defStyleAttr, defStyleRes);
        final boolean isDialogMode = a.getBoolean(R.styleable.DatePicker_dialogMode, false);
        final int requestedMode = a.getInt(R.styleable.DatePicker_datePickerMode, android.widget.DatePicker.MODE_SPINNER);
        final int firstDayOfWeek = a.getInt(R.styleable.DatePicker_firstDayOfWeek, 0);
        a.recycle();
        if ((requestedMode == android.widget.DatePicker.MODE_CALENDAR) && isDialogMode) {
            // You want MODE_CALENDAR? YOU CAN'T HANDLE MODE_CALENDAR! Well,
            // maybe you can depending on your screen size. Let's check...
            mMode = context.getResources().getInteger(R.integer.date_picker_mode);
        } else {
            mMode = requestedMode;
        }
        switch (mMode) {
            case android.widget.DatePicker.MODE_CALENDAR :
                mDelegate = createCalendarUIDelegate(context, attrs, defStyleAttr, defStyleRes);
                break;
            case android.widget.DatePicker.MODE_SPINNER :
            default :
                mDelegate = createSpinnerUIDelegate(context, attrs, defStyleAttr, defStyleRes);
                break;
        }
        if (firstDayOfWeek != 0) {
            setFirstDayOfWeek(firstDayOfWeek);
        }
        mDelegate.setAutoFillChangeListener(( v, y, m, d) -> {
            final android.view.autofill.AutofillManager afm = context.getSystemService(android.view.autofill.AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
        });
    }

    private android.widget.DatePicker.DatePickerDelegate createSpinnerUIDelegate(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new android.widget.DatePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    private android.widget.DatePicker.DatePickerDelegate createCalendarUIDelegate(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new android.widget.DatePickerCalendarDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     *
     *
     * @return the picker's presentation mode, one of {@link #MODE_CALENDAR} or
    {@link #MODE_SPINNER}
     * @unknown ref android.R.styleable#DatePicker_datePickerMode
     * @unknown Visible for testing only.
     */
    @android.view.inspector.InspectableProperty(name = "datePickerMode", enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(value = android.widget.DatePicker.MODE_SPINNER, name = "spinner"), @android.view.inspector.InspectableProperty.EnumEntry(value = android.widget.DatePicker.MODE_CALENDAR, name = "calendar") })
    @android.widget.DatePicker.DatePickerMode
    @android.annotation.TestApi
    public int getMode() {
        return mMode;
    }

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the spinners.
     *
     * @param year
     * 		The initial year.
     * @param monthOfYear
     * 		The initial month <strong>starting from zero</strong>.
     * @param dayOfMonth
     * 		The initial day of the month.
     * @param onDateChangedListener
     * 		How user is notified date is changed by
     * 		user, can be null.
     */
    public void init(int year, int monthOfYear, int dayOfMonth, android.widget.DatePicker.OnDateChangedListener onDateChangedListener) {
        mDelegate.init(year, monthOfYear, dayOfMonth, onDateChangedListener);
    }

    /**
     * Set the callback that indicates the date has been adjusted by the user.
     *
     * @param onDateChangedListener
     * 		How user is notified date is changed by
     * 		user, can be null.
     */
    public void setOnDateChangedListener(android.widget.DatePicker.OnDateChangedListener onDateChangedListener) {
        mDelegate.setOnDateChangedListener(onDateChangedListener);
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
     */
    public void updateDate(int year, int month, int dayOfMonth) {
        mDelegate.updateDate(year, month, dayOfMonth);
    }

    /**
     *
     *
     * @return The selected year.
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public int getYear() {
        return mDelegate.getYear();
    }

    /**
     *
     *
     * @return The selected month.
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public int getMonth() {
        return mDelegate.getMonth();
    }

    /**
     *
     *
     * @return The selected day of month.
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public int getDayOfMonth() {
        return mDelegate.getDayOfMonth();
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
    @android.view.inspector.InspectableProperty
    public long getMinDate() {
        return mDelegate.getMinDate().getTimeInMillis();
    }

    /**
     * Sets the minimal date supported by this {@link NumberPicker} in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate
     * 		The minimal supported date.
     */
    public void setMinDate(long minDate) {
        mDelegate.setMinDate(minDate);
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
    @android.view.inspector.InspectableProperty
    public long getMaxDate() {
        return mDelegate.getMaxDate().getTimeInMillis();
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
        mDelegate.setMaxDate(maxDate);
    }

    /**
     * Sets the callback that indicates the current date is valid.
     *
     * @param callback
     * 		the callback, may be null
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setValidationCallback(@android.annotation.Nullable
    android.widget.DatePicker.ValidationCallback callback) {
        mDelegate.setValidationCallback(callback);
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        if (mDelegate.isEnabled() == enabled) {
            return;
        }
        super.setEnabled(enabled);
        mDelegate.setEnabled(enabled);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mDelegate.isEnabled();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        return mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        mDelegate.onPopulateAccessibilityEvent(event);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.DatePicker.class.getName();
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.onConfigurationChanged(newConfig);
    }

    /**
     * Sets the first day of week.
     *
     * @param firstDayOfWeek
     * 		The first day of the week conforming to the
     * 		{@link CalendarView} APIs.
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     * @unknown ref android.R.styleable#DatePicker_firstDayOfWeek
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if ((firstDayOfWeek < android.icu.util.Calendar.SUNDAY) || (firstDayOfWeek > android.icu.util.Calendar.SATURDAY)) {
            throw new java.lang.IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    /**
     * Gets the first day of week.
     *
     * @return The first day of the week conforming to the {@link CalendarView}
    APIs.
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     * @unknown ref android.R.styleable#DatePicker_firstDayOfWeek
     */
    @android.view.inspector.InspectableProperty
    public int getFirstDayOfWeek() {
        return mDelegate.getFirstDayOfWeek();
    }

    /**
     * Returns whether the {@link CalendarView} is shown.
     * <p>
     * <strong>Note:</strong> This method returns {@code false} when the
     * {@link android.R.styleable#DatePicker_datePickerMode} attribute is set
     * to {@code calendar}.
     *
     * @return {@code true} if the calendar view is shown
     * @see #getCalendarView()
     * @deprecated Not supported by Material-style {@code calendar} mode
     */
    @android.view.inspector.InspectableProperty
    @java.lang.Deprecated
    public boolean getCalendarViewShown() {
        return mDelegate.getCalendarViewShown();
    }

    /**
     * Returns the {@link CalendarView} used by this picker.
     * <p>
     * <strong>Note:</strong> This method throws an
     * {@link UnsupportedOperationException} when the
     * {@link android.R.styleable#DatePicker_datePickerMode} attribute is set
     * to {@code calendar}.
     *
     * @return the calendar view
     * @see #getCalendarViewShown()
     * @deprecated Not supported by Material-style {@code calendar} mode
     * @throws UnsupportedOperationException
     * 		if called when the picker is
     * 		displayed in {@code calendar} mode
     */
    @java.lang.Deprecated
    public android.widget.CalendarView getCalendarView() {
        return mDelegate.getCalendarView();
    }

    /**
     * Sets whether the {@link CalendarView} is shown.
     * <p>
     * <strong>Note:</strong> Calling this method has no effect when the
     * {@link android.R.styleable#DatePicker_datePickerMode} attribute is set
     * to {@code calendar}.
     *
     * @param shown
     * 		{@code true} to show the calendar view, {@code false} to
     * 		hide it
     * @deprecated Not supported by Material-style {@code calendar} mode
     */
    @java.lang.Deprecated
    public void setCalendarViewShown(boolean shown) {
        mDelegate.setCalendarViewShown(shown);
    }

    /**
     * Returns whether the spinners are shown.
     * <p>
     * <strong>Note:</strong> his method returns {@code false} when the
     * {@link android.R.styleable#DatePicker_datePickerMode} attribute is set
     * to {@code calendar}.
     *
     * @return {@code true} if the spinners are shown
     * @deprecated Not supported by Material-style {@code calendar} mode
     */
    @android.view.inspector.InspectableProperty
    @java.lang.Deprecated
    public boolean getSpinnersShown() {
        return mDelegate.getSpinnersShown();
    }

    /**
     * Sets whether the spinners are shown.
     * <p>
     * Calling this method has no effect when the
     * {@link android.R.styleable#DatePicker_datePickerMode} attribute is set
     * to {@code calendar}.
     *
     * @param shown
     * 		{@code true} to show the spinners, {@code false} to hide
     * 		them
     * @deprecated Not supported by Material-style {@code calendar} mode
     */
    @java.lang.Deprecated
    public void setSpinnersShown(boolean shown) {
        mDelegate.setSpinnersShown(shown);
    }

    @java.lang.Override
    protected void dispatchRestoreInstanceState(android.util.SparseArray<android.os.Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        return mDelegate.onSaveInstanceState(superState);
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        android.view.View.BaseSavedState ss = ((android.view.View.BaseSavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        mDelegate.onRestoreInstanceState(ss);
    }

    /**
     * A delegate interface that defined the public API of the DatePicker. Allows different
     * DatePicker implementations. This would need to be implemented by the DatePicker delegates
     * for the real behavior.
     *
     * @unknown 
     */
    interface DatePickerDelegate {
        void init(int year, int monthOfYear, int dayOfMonth, android.widget.DatePicker.OnDateChangedListener onDateChangedListener);

        void setOnDateChangedListener(android.widget.DatePicker.OnDateChangedListener onDateChangedListener);

        void setAutoFillChangeListener(android.widget.DatePicker.OnDateChangedListener onDateChangedListener);

        void updateDate(int year, int month, int dayOfMonth);

        int getYear();

        int getMonth();

        int getDayOfMonth();

        void autofill(android.view.autofill.AutofillValue value);

        android.view.autofill.AutofillValue getAutofillValue();

        void setFirstDayOfWeek(int firstDayOfWeek);

        int getFirstDayOfWeek();

        void setMinDate(long minDate);

        android.icu.util.Calendar getMinDate();

        void setMaxDate(long maxDate);

        android.icu.util.Calendar getMaxDate();

        void setEnabled(boolean enabled);

        boolean isEnabled();

        android.widget.CalendarView getCalendarView();

        void setCalendarViewShown(boolean shown);

        boolean getCalendarViewShown();

        void setSpinnersShown(boolean shown);

        boolean getSpinnersShown();

        void setValidationCallback(android.widget.DatePicker.ValidationCallback callback);

        void onConfigurationChanged(android.content.res.Configuration newConfig);

        android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState);

        void onRestoreInstanceState(android.os.Parcelable state);

        boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event);

        void onPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event);
    }

    /**
     * An abstract class which can be used as a start for DatePicker implementations
     */
    static abstract class AbstractDatePickerDelegate implements android.widget.DatePicker.DatePickerDelegate {
        // The delegator
        protected android.widget.DatePicker mDelegator;

        // The context
        protected android.content.Context mContext;

        // NOTE: when subclasses change this variable, they must call resetAutofilledValue().
        protected android.icu.util.Calendar mCurrentDate;

        // The current locale
        protected java.util.Locale mCurrentLocale;

        // Callbacks
        protected android.widget.DatePicker.OnDateChangedListener mOnDateChangedListener;

        protected android.widget.DatePicker.OnDateChangedListener mAutoFillChangeListener;

        protected android.widget.DatePicker.ValidationCallback mValidationCallback;

        // The value that was passed to autofill() - it must be stored because it getAutofillValue()
        // must return the exact same value that was autofilled, otherwise the widget will not be
        // properly highlighted after autofill().
        private long mAutofilledValue;

        public AbstractDatePickerDelegate(android.widget.DatePicker delegator, android.content.Context context) {
            mDelegator = delegator;
            mContext = context;
            setCurrentLocale(java.util.Locale.getDefault());
        }

        protected void setCurrentLocale(java.util.Locale locale) {
            if (!locale.equals(mCurrentLocale)) {
                mCurrentLocale = locale;
                onLocaleChanged(locale);
            }
        }

        @java.lang.Override
        public void setOnDateChangedListener(android.widget.DatePicker.OnDateChangedListener callback) {
            mOnDateChangedListener = callback;
        }

        @java.lang.Override
        public void setAutoFillChangeListener(android.widget.DatePicker.OnDateChangedListener callback) {
            mAutoFillChangeListener = callback;
        }

        @java.lang.Override
        public void setValidationCallback(android.widget.DatePicker.ValidationCallback callback) {
            mValidationCallback = callback;
        }

        @java.lang.Override
        public final void autofill(android.view.autofill.AutofillValue value) {
            if ((value == null) || (!value.isDate())) {
                android.util.Log.w(android.widget.DatePicker.LOG_TAG, (value + " could not be autofilled into ") + this);
                return;
            }
            final long time = value.getDateValue();
            final android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance(mCurrentLocale);
            cal.setTimeInMillis(time);
            updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            // Must set mAutofilledValue *after* calling subclass method to make sure the value
            // returned by getAutofillValue() matches it.
            mAutofilledValue = time;
        }

        @java.lang.Override
        public final android.view.autofill.AutofillValue getAutofillValue() {
            final long time = (mAutofilledValue != 0) ? mAutofilledValue : mCurrentDate.getTimeInMillis();
            return android.view.autofill.AutofillValue.forDate(time);
        }

        /**
         * This method must be called every time the value of the year, month, and/or day is
         * changed by a subclass method.
         */
        protected void resetAutofilledValue() {
            mAutofilledValue = 0;
        }

        protected void onValidationChanged(boolean valid) {
            if (mValidationCallback != null) {
                mValidationCallback.onValidationChanged(valid);
            }
        }

        protected void onLocaleChanged(java.util.Locale locale) {
            // Stub.
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
            event.getText().add(getFormattedCurrentDate());
        }

        protected java.lang.String getFormattedCurrentDate() {
            return android.text.format.DateUtils.formatDateTime(mContext, mCurrentDate.getTimeInMillis(), (android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_SHOW_YEAR) | android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY);
        }

        /**
         * Class for managing state storing/restoring.
         */
        static class SavedState extends android.view.View.BaseSavedState {
            private final int mSelectedYear;

            private final int mSelectedMonth;

            private final int mSelectedDay;

            private final long mMinDate;

            private final long mMaxDate;

            private final int mCurrentView;

            private final int mListPosition;

            private final int mListPositionOffset;

            public SavedState(android.os.Parcelable superState, int year, int month, int day, long minDate, long maxDate) {
                this(superState, year, month, day, minDate, maxDate, 0, 0, 0);
            }

            /**
             * Constructor called from {@link DatePicker#onSaveInstanceState()}
             */
            public SavedState(android.os.Parcelable superState, int year, int month, int day, long minDate, long maxDate, int currentView, int listPosition, int listPositionOffset) {
                super(superState);
                mSelectedYear = year;
                mSelectedMonth = month;
                mSelectedDay = day;
                mMinDate = minDate;
                mMaxDate = maxDate;
                mCurrentView = currentView;
                mListPosition = listPosition;
                mListPositionOffset = listPositionOffset;
            }

            /**
             * Constructor called from {@link #CREATOR}
             */
            private SavedState(android.os.Parcel in) {
                super(in);
                mSelectedYear = in.readInt();
                mSelectedMonth = in.readInt();
                mSelectedDay = in.readInt();
                mMinDate = in.readLong();
                mMaxDate = in.readLong();
                mCurrentView = in.readInt();
                mListPosition = in.readInt();
                mListPositionOffset = in.readInt();
            }

            @java.lang.Override
            public void writeToParcel(android.os.Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(mSelectedYear);
                dest.writeInt(mSelectedMonth);
                dest.writeInt(mSelectedDay);
                dest.writeLong(mMinDate);
                dest.writeLong(mMaxDate);
                dest.writeInt(mCurrentView);
                dest.writeInt(mListPosition);
                dest.writeInt(mListPositionOffset);
            }

            public int getSelectedDay() {
                return mSelectedDay;
            }

            public int getSelectedMonth() {
                return mSelectedMonth;
            }

            public int getSelectedYear() {
                return mSelectedYear;
            }

            public long getMinDate() {
                return mMinDate;
            }

            public long getMaxDate() {
                return mMaxDate;
            }

            public int getCurrentView() {
                return mCurrentView;
            }

            public int getListPosition() {
                return mListPosition;
            }

            public int getListPositionOffset() {
                return mListPositionOffset;
            }

            // suppress unused and hiding
            @java.lang.SuppressWarnings("all")
            @android.annotation.NonNull
            public static final android.os.Parcelable.Creator<android.widget.DatePicker.AbstractDatePickerDelegate.SavedState> CREATOR = new android.widget.Creator<android.widget.DatePicker.AbstractDatePickerDelegate.SavedState>() {
                public android.widget.DatePicker.AbstractDatePickerDelegate.SavedState createFromParcel(android.os.Parcel in) {
                    return new android.widget.DatePicker.AbstractDatePickerDelegate.SavedState(in);
                }

                public android.widget.DatePicker.AbstractDatePickerDelegate.SavedState[] newArray(int size) {
                    return new android.widget.DatePicker.AbstractDatePickerDelegate.SavedState[size];
                }
            };
        }
    }

    /**
     * A callback interface for updating input validity when the date picker
     * when included into a dialog.
     *
     * @unknown 
     */
    public interface ValidationCallback {
        void onValidationChanged(boolean valid);
    }

    @java.lang.Override
    public void dispatchProvideAutofillStructure(android.view.ViewStructure structure, int flags) {
        // This view is self-sufficient for autofill, so it needs to call
        // onProvideAutoFillStructure() to fill itself, but it does not need to call
        // dispatchProvideAutoFillStructure() to fill its children.
        structure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(structure, flags);
    }

    @java.lang.Override
    public void autofill(android.view.autofill.AutofillValue value) {
        if (!isEnabled())
            return;

        mDelegate.autofill(value);
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return isEnabled() ? android.view.View.AUTOFILL_TYPE_DATE : android.view.View.AUTOFILL_TYPE_NONE;
    }

    @java.lang.Override
    public android.view.autofill.AutofillValue getAutofillValue() {
        return isEnabled() ? mDelegate.getAutofillValue() : null;
    }
}

