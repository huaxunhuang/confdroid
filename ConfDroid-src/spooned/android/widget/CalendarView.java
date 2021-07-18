/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * This class is a calendar widget for displaying and selecting dates. The
 * range of dates supported by this calendar is configurable.
 * <p>
 * The exact appearance and interaction model of this widget may vary between
 * OS versions and themes (e.g. Holo versus Material), but in general a user
 * can select a date by tapping on it and can scroll or fling the calendar to a
 * desired date.
 *
 * @unknown ref android.R.styleable#CalendarView_showWeekNumber
 * @unknown ref android.R.styleable#CalendarView_firstDayOfWeek
 * @unknown ref android.R.styleable#CalendarView_minDate
 * @unknown ref android.R.styleable#CalendarView_maxDate
 * @unknown ref android.R.styleable#CalendarView_shownWeekCount
 * @unknown ref android.R.styleable#CalendarView_selectedWeekBackgroundColor
 * @unknown ref android.R.styleable#CalendarView_focusedMonthDateColor
 * @unknown ref android.R.styleable#CalendarView_unfocusedMonthDateColor
 * @unknown ref android.R.styleable#CalendarView_weekNumberColor
 * @unknown ref android.R.styleable#CalendarView_weekSeparatorLineColor
 * @unknown ref android.R.styleable#CalendarView_selectedDateVerticalBar
 * @unknown ref android.R.styleable#CalendarView_weekDayTextAppearance
 * @unknown ref android.R.styleable#CalendarView_dateTextAppearance
 */
@android.annotation.Widget
public class CalendarView extends android.widget.FrameLayout {
    private static final java.lang.String LOG_TAG = "CalendarView";

    private static final int MODE_HOLO = 0;

    private static final int MODE_MATERIAL = 1;

    @android.annotation.UnsupportedAppUsage
    private final android.widget.CalendarView.CalendarViewDelegate mDelegate;

    /**
     * The callback used to indicate the user changes the date.
     */
    public interface OnDateChangeListener {
        /**
         * Called upon change of the selected day.
         *
         * @param view
         * 		The view associated with this listener.
         * @param year
         * 		The year that was set.
         * @param month
         * 		The month that was set [0-11].
         * @param dayOfMonth
         * 		The day of the month that was set.
         */
        void onSelectedDayChange(@android.annotation.NonNull
        android.widget.CalendarView view, int year, int month, int dayOfMonth);
    }

    public CalendarView(@android.annotation.NonNull
    android.content.Context context) {
        this(context, null);
    }

    public CalendarView(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public CalendarView(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CalendarView(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.CalendarView, attrs, a, defStyleAttr, defStyleRes);
        final int mode = a.getInt(R.styleable.CalendarView_calendarViewMode, android.widget.CalendarView.MODE_HOLO);
        a.recycle();
        switch (mode) {
            case android.widget.CalendarView.MODE_HOLO :
                mDelegate = new android.widget.CalendarViewLegacyDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                break;
            case android.widget.CalendarView.MODE_MATERIAL :
                mDelegate = new android.widget.CalendarViewMaterialDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                break;
            default :
                throw new java.lang.IllegalArgumentException("invalid calendarViewMode attribute");
        }
    }

    /**
     * Sets the number of weeks to be shown.
     *
     * @param count
     * 		The shown week count.
     * @unknown ref android.R.styleable#CalendarView_shownWeekCount
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setShownWeekCount(int count) {
        mDelegate.setShownWeekCount(count);
    }

    /**
     * Gets the number of weeks to be shown.
     *
     * @return The shown week count.
     * @unknown ref android.R.styleable#CalendarView_shownWeekCount
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @java.lang.Deprecated
    public int getShownWeekCount() {
        return mDelegate.getShownWeekCount();
    }

    /**
     * Sets the background color for the selected week.
     *
     * @param color
     * 		The week background color.
     * @unknown ref android.R.styleable#CalendarView_selectedWeekBackgroundColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setSelectedWeekBackgroundColor(@android.annotation.ColorInt
    int color) {
        mDelegate.setSelectedWeekBackgroundColor(color);
    }

    /**
     * Gets the background color for the selected week.
     *
     * @return The week background color.
     * @unknown ref android.R.styleable#CalendarView_selectedWeekBackgroundColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.ColorInt
    @java.lang.Deprecated
    public int getSelectedWeekBackgroundColor() {
        return mDelegate.getSelectedWeekBackgroundColor();
    }

    /**
     * Sets the color for the dates of the focused month.
     *
     * @param color
     * 		The focused month date color.
     * @unknown ref android.R.styleable#CalendarView_focusedMonthDateColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setFocusedMonthDateColor(@android.annotation.ColorInt
    int color) {
        mDelegate.setFocusedMonthDateColor(color);
    }

    /**
     * Gets the color for the dates in the focused month.
     *
     * @return The focused month date color.
     * @unknown ref android.R.styleable#CalendarView_focusedMonthDateColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.ColorInt
    @java.lang.Deprecated
    public int getFocusedMonthDateColor() {
        return mDelegate.getFocusedMonthDateColor();
    }

    /**
     * Sets the color for the dates of a not focused month.
     *
     * @param color
     * 		A not focused month date color.
     * @unknown ref android.R.styleable#CalendarView_unfocusedMonthDateColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setUnfocusedMonthDateColor(@android.annotation.ColorInt
    int color) {
        mDelegate.setUnfocusedMonthDateColor(color);
    }

    /**
     * Gets the color for the dates in a not focused month.
     *
     * @return A not focused month date color.
     * @unknown ref android.R.styleable#CalendarView_unfocusedMonthDateColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.ColorInt
    @java.lang.Deprecated
    public int getUnfocusedMonthDateColor() {
        return mDelegate.getUnfocusedMonthDateColor();
    }

    /**
     * Sets the color for the week numbers.
     *
     * @param color
     * 		The week number color.
     * @unknown ref android.R.styleable#CalendarView_weekNumberColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setWeekNumberColor(@android.annotation.ColorInt
    int color) {
        mDelegate.setWeekNumberColor(color);
    }

    /**
     * Gets the color for the week numbers.
     *
     * @return The week number color.
     * @unknown ref android.R.styleable#CalendarView_weekNumberColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.ColorInt
    @java.lang.Deprecated
    public int getWeekNumberColor() {
        return mDelegate.getWeekNumberColor();
    }

    /**
     * Sets the color for the separator line between weeks.
     *
     * @param color
     * 		The week separator color.
     * @unknown ref android.R.styleable#CalendarView_weekSeparatorLineColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setWeekSeparatorLineColor(@android.annotation.ColorInt
    int color) {
        mDelegate.setWeekSeparatorLineColor(color);
    }

    /**
     * Gets the color for the separator line between weeks.
     *
     * @return The week separator color.
     * @unknown ref android.R.styleable#CalendarView_weekSeparatorLineColor
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.annotation.ColorInt
    @java.lang.Deprecated
    @android.view.inspector.InspectableProperty
    public int getWeekSeparatorLineColor() {
        return mDelegate.getWeekSeparatorLineColor();
    }

    /**
     * Sets the drawable for the vertical bar shown at the beginning and at
     * the end of the selected date.
     *
     * @param resourceId
     * 		The vertical bar drawable resource id.
     * @unknown ref android.R.styleable#CalendarView_selectedDateVerticalBar
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setSelectedDateVerticalBar(@android.annotation.DrawableRes
    int resourceId) {
        mDelegate.setSelectedDateVerticalBar(resourceId);
    }

    /**
     * Sets the drawable for the vertical bar shown at the beginning and at
     * the end of the selected date.
     *
     * @param drawable
     * 		The vertical bar drawable.
     * @unknown ref android.R.styleable#CalendarView_selectedDateVerticalBar
     * @deprecated No longer used by Material-style CalendarView.
     */
    @java.lang.Deprecated
    public void setSelectedDateVerticalBar(android.graphics.drawable.Drawable drawable) {
        mDelegate.setSelectedDateVerticalBar(drawable);
    }

    /**
     * Gets the drawable for the vertical bar shown at the beginning and at
     * the end of the selected date.
     *
     * @return The vertical bar drawable.
     * @deprecated No longer used by Material-style CalendarView.
     */
    @android.view.inspector.InspectableProperty
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getSelectedDateVerticalBar() {
        return mDelegate.getSelectedDateVerticalBar();
    }

    /**
     * Sets the text appearance for the week day abbreviation of the calendar header.
     *
     * @param resourceId
     * 		The text appearance resource id.
     * @unknown ref android.R.styleable#CalendarView_weekDayTextAppearance
     */
    public void setWeekDayTextAppearance(@android.annotation.StyleRes
    int resourceId) {
        mDelegate.setWeekDayTextAppearance(resourceId);
    }

    /**
     * Gets the text appearance for the week day abbreviation of the calendar header.
     *
     * @return The text appearance resource id.
     * @unknown ref android.R.styleable#CalendarView_weekDayTextAppearance
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.StyleRes
    public int getWeekDayTextAppearance() {
        return mDelegate.getWeekDayTextAppearance();
    }

    /**
     * Sets the text appearance for the calendar dates.
     *
     * @param resourceId
     * 		The text appearance resource id.
     * @unknown ref android.R.styleable#CalendarView_dateTextAppearance
     */
    public void setDateTextAppearance(@android.annotation.StyleRes
    int resourceId) {
        mDelegate.setDateTextAppearance(resourceId);
    }

    /**
     * Gets the text appearance for the calendar dates.
     *
     * @return The text appearance resource id.
     * @unknown ref android.R.styleable#CalendarView_dateTextAppearance
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.StyleRes
    public int getDateTextAppearance() {
        return mDelegate.getDateTextAppearance();
    }

    /**
     * Gets the minimal date supported by this {@link CalendarView} in milliseconds
     * since January 1, 1970 00:00:00 in {@link TimeZone#getDefault()} time
     * zone.
     * <p>
     * Note: The default minimal date is 01/01/1900.
     * <p>
     *
     * @return The minimal supported date.
     * @unknown ref android.R.styleable#CalendarView_minDate
     */
    @android.view.inspector.InspectableProperty
    public long getMinDate() {
        return mDelegate.getMinDate();
    }

    /**
     * Sets the minimal date supported by this {@link CalendarView} in milliseconds
     * since January 1, 1970 00:00:00 in {@link TimeZone#getDefault()} time
     * zone.
     *
     * @param minDate
     * 		The minimal supported date.
     * @unknown ref android.R.styleable#CalendarView_minDate
     */
    public void setMinDate(long minDate) {
        mDelegate.setMinDate(minDate);
    }

    /**
     * Gets the maximal date supported by this {@link CalendarView} in milliseconds
     * since January 1, 1970 00:00:00 in {@link TimeZone#getDefault()} time
     * zone.
     * <p>
     * Note: The default maximal date is 01/01/2100.
     * <p>
     *
     * @return The maximal supported date.
     * @unknown ref android.R.styleable#CalendarView_maxDate
     */
    @android.view.inspector.InspectableProperty
    public long getMaxDate() {
        return mDelegate.getMaxDate();
    }

    /**
     * Sets the maximal date supported by this {@link CalendarView} in milliseconds
     * since January 1, 1970 00:00:00 in {@link TimeZone#getDefault()} time
     * zone.
     *
     * @param maxDate
     * 		The maximal supported date.
     * @unknown ref android.R.styleable#CalendarView_maxDate
     */
    public void setMaxDate(long maxDate) {
        mDelegate.setMaxDate(maxDate);
    }

    /**
     * Sets whether to show the week number.
     *
     * @param showWeekNumber
     * 		True to show the week number.
     * @deprecated No longer used by Material-style CalendarView.
     * @unknown ref android.R.styleable#CalendarView_showWeekNumber
     */
    @java.lang.Deprecated
    public void setShowWeekNumber(boolean showWeekNumber) {
        mDelegate.setShowWeekNumber(showWeekNumber);
    }

    /**
     * Gets whether to show the week number.
     *
     * @return True if showing the week number.
     * @deprecated No longer used by Material-style CalendarView.
     * @unknown ref android.R.styleable#CalendarView_showWeekNumber
     */
    @android.view.inspector.InspectableProperty
    @java.lang.Deprecated
    public boolean getShowWeekNumber() {
        return mDelegate.getShowWeekNumber();
    }

    /**
     * Gets the first day of week.
     *
     * @return The first day of the week conforming to the {@link CalendarView}
    APIs.
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     * @see Calendar#SUNDAY
     * @unknown ref android.R.styleable#CalendarView_firstDayOfWeek
     */
    @android.view.inspector.InspectableProperty
    public int getFirstDayOfWeek() {
        return mDelegate.getFirstDayOfWeek();
    }

    /**
     * Sets the first day of week.
     *
     * @param firstDayOfWeek
     * 		The first day of the week conforming to the
     * 		{@link CalendarView} APIs.
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     * @see Calendar#SUNDAY
     * @unknown ref android.R.styleable#CalendarView_firstDayOfWeek
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    /**
     * Sets the listener to be notified upon selected date change.
     *
     * @param listener
     * 		The listener to be notified.
     */
    public void setOnDateChangeListener(android.widget.CalendarView.OnDateChangeListener listener) {
        mDelegate.setOnDateChangeListener(listener);
    }

    /**
     * Gets the selected date in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return The selected date.
     */
    public long getDate() {
        return mDelegate.getDate();
    }

    /**
     * Sets the selected date in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param date
     * 		The selected date.
     * @throws IllegalArgumentException
     * 		of the provided date is before the
     * 		minimal or after the maximal date.
     * @see #setDate(long, boolean, boolean)
     * @see #setMinDate(long)
     * @see #setMaxDate(long)
     */
    public void setDate(long date) {
        mDelegate.setDate(date);
    }

    /**
     * Sets the selected date in milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @param date
     * 		The date.
     * @param animate
     * 		Whether to animate the scroll to the current date.
     * @param center
     * 		Whether to center the current date even if it is already visible.
     * @throws IllegalArgumentException
     * 		of the provided date is before the
     * 		minimal or after the maximal date.
     * @see #setMinDate(long)
     * @see #setMaxDate(long)
     */
    public void setDate(long date, boolean animate, boolean center) {
        mDelegate.setDate(date, animate, center);
    }

    /**
     * Retrieves the screen bounds for the specific date in the coordinate system of this
     * view. If the passed date is being currently displayed, this method returns true and
     * the caller can query the fields of the passed {@link Rect} object. Otherwise the
     * method returns false and does not touch the passed {@link Rect} object.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public boolean getBoundsForDate(long date, android.graphics.Rect outBounds) {
        return mDelegate.getBoundsForDate(date, outBounds);
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.onConfigurationChanged(newConfig);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.CalendarView.class.getName();
    }

    /**
     * A delegate interface that defined the public API of the CalendarView. Allows different
     * CalendarView implementations. This would need to be implemented by the CalendarView delegates
     * for the real behavior.
     */
    private interface CalendarViewDelegate {
        void setShownWeekCount(int count);

        int getShownWeekCount();

        void setSelectedWeekBackgroundColor(@android.annotation.ColorInt
        int color);

        @android.annotation.ColorInt
        int getSelectedWeekBackgroundColor();

        void setFocusedMonthDateColor(@android.annotation.ColorInt
        int color);

        @android.annotation.ColorInt
        int getFocusedMonthDateColor();

        void setUnfocusedMonthDateColor(@android.annotation.ColorInt
        int color);

        @android.annotation.ColorInt
        int getUnfocusedMonthDateColor();

        void setWeekNumberColor(@android.annotation.ColorInt
        int color);

        @android.annotation.ColorInt
        int getWeekNumberColor();

        void setWeekSeparatorLineColor(@android.annotation.ColorInt
        int color);

        @android.annotation.ColorInt
        int getWeekSeparatorLineColor();

        void setSelectedDateVerticalBar(@android.annotation.DrawableRes
        int resourceId);

        void setSelectedDateVerticalBar(android.graphics.drawable.Drawable drawable);

        android.graphics.drawable.Drawable getSelectedDateVerticalBar();

        void setWeekDayTextAppearance(@android.annotation.StyleRes
        int resourceId);

        @android.annotation.StyleRes
        int getWeekDayTextAppearance();

        void setDateTextAppearance(@android.annotation.StyleRes
        int resourceId);

        @android.annotation.StyleRes
        int getDateTextAppearance();

        void setMinDate(long minDate);

        long getMinDate();

        void setMaxDate(long maxDate);

        long getMaxDate();

        void setShowWeekNumber(boolean showWeekNumber);

        boolean getShowWeekNumber();

        void setFirstDayOfWeek(int firstDayOfWeek);

        int getFirstDayOfWeek();

        void setDate(long date);

        void setDate(long date, boolean animate, boolean center);

        long getDate();

        boolean getBoundsForDate(long date, android.graphics.Rect outBounds);

        void setOnDateChangeListener(android.widget.CalendarView.OnDateChangeListener listener);

        void onConfigurationChanged(android.content.res.Configuration newConfig);
    }

    /**
     * An abstract class which can be used as a start for CalendarView implementations
     */
    static abstract class AbstractCalendarViewDelegate implements android.widget.CalendarView.CalendarViewDelegate {
        /**
         * The default minimal date.
         */
        protected static final java.lang.String DEFAULT_MIN_DATE = "01/01/1900";

        /**
         * The default maximal date.
         */
        protected static final java.lang.String DEFAULT_MAX_DATE = "01/01/2100";

        protected android.widget.CalendarView mDelegator;

        protected android.content.Context mContext;

        protected java.util.Locale mCurrentLocale;

        AbstractCalendarViewDelegate(android.widget.CalendarView delegator, android.content.Context context) {
            mDelegator = delegator;
            mContext = context;
            // Initialization based on locale
            setCurrentLocale(java.util.Locale.getDefault());
        }

        protected void setCurrentLocale(java.util.Locale locale) {
            if (locale.equals(mCurrentLocale)) {
                return;
            }
            mCurrentLocale = locale;
        }

        @java.lang.Override
        public void setShownWeekCount(int count) {
            // Deprecated.
        }

        @java.lang.Override
        public int getShownWeekCount() {
            // Deprecated.
            return 0;
        }

        @java.lang.Override
        public void setSelectedWeekBackgroundColor(@android.annotation.ColorInt
        int color) {
            // Deprecated.
        }

        @android.annotation.ColorInt
        @java.lang.Override
        public int getSelectedWeekBackgroundColor() {
            return 0;
        }

        @java.lang.Override
        public void setFocusedMonthDateColor(@android.annotation.ColorInt
        int color) {
            // Deprecated.
        }

        @android.annotation.ColorInt
        @java.lang.Override
        public int getFocusedMonthDateColor() {
            return 0;
        }

        @java.lang.Override
        public void setUnfocusedMonthDateColor(@android.annotation.ColorInt
        int color) {
            // Deprecated.
        }

        @android.annotation.ColorInt
        @java.lang.Override
        public int getUnfocusedMonthDateColor() {
            return 0;
        }

        @java.lang.Override
        public void setWeekNumberColor(@android.annotation.ColorInt
        int color) {
            // Deprecated.
        }

        @android.annotation.ColorInt
        @java.lang.Override
        public int getWeekNumberColor() {
            // Deprecated.
            return 0;
        }

        @java.lang.Override
        public void setWeekSeparatorLineColor(@android.annotation.ColorInt
        int color) {
            // Deprecated.
        }

        @android.annotation.ColorInt
        @java.lang.Override
        public int getWeekSeparatorLineColor() {
            // Deprecated.
            return 0;
        }

        @java.lang.Override
        public void setSelectedDateVerticalBar(@android.annotation.DrawableRes
        int resId) {
            // Deprecated.
        }

        @java.lang.Override
        public void setSelectedDateVerticalBar(android.graphics.drawable.Drawable drawable) {
            // Deprecated.
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getSelectedDateVerticalBar() {
            // Deprecated.
            return null;
        }

        @java.lang.Override
        public void setShowWeekNumber(boolean showWeekNumber) {
            // Deprecated.
        }

        @java.lang.Override
        public boolean getShowWeekNumber() {
            // Deprecated.
            return false;
        }

        @java.lang.Override
        public void onConfigurationChanged(android.content.res.Configuration newConfig) {
            // Nothing to do here, configuration changes are already propagated
            // by ViewGroup.
        }
    }

    /**
     * String for parsing dates.
     */
    private static final java.lang.String DATE_FORMAT = "MM/dd/yyyy";

    /**
     * Date format for parsing dates.
     */
    private static final java.text.DateFormat DATE_FORMATTER = new java.text.SimpleDateFormat(android.widget.CalendarView.DATE_FORMAT);

    /**
     * Utility method for the date format used by CalendarView's min/max date.
     *
     * @unknown Use only as directed. For internal use only.
     */
    public static boolean parseDate(java.lang.String date, android.icu.util.Calendar outDate) {
        if ((date == null) || date.isEmpty()) {
            return false;
        }
        try {
            final java.util.Date parsedDate = android.widget.CalendarView.DATE_FORMATTER.parse(date);
            outDate.setTime(parsedDate);
            return true;
        } catch (java.text.ParseException e) {
            android.util.Log.w(android.widget.CalendarView.LOG_TAG, (("Date: " + date) + " not in format: ") + android.widget.CalendarView.DATE_FORMAT);
            return false;
        }
    }
}

