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
 * A widget for selecting the time of day, in either 24-hour or AM/PM mode.
 * <p>
 * For a dialog using this view, see {@link android.app.TimePickerDialog}. See
 * the <a href="{@docRoot }guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide for more information.
 *
 * @unknown ref android.R.styleable#TimePicker_timePickerMode
 */
@android.annotation.Widget
public class TimePicker extends android.widget.FrameLayout {
    private static final java.lang.String LOG_TAG = android.widget.TimePicker.class.getSimpleName();

    /**
     * Presentation mode for the Holo-style time picker that uses a set of
     * {@link android.widget.NumberPicker}s.
     *
     * @see #getMode()
     * @unknown Visible for testing only.
     */
    @android.annotation.TestApi
    public static final int MODE_SPINNER = 1;

    /**
     * Presentation mode for the Material-style time picker that uses a clock
     * face.
     *
     * @see #getMode()
     * @unknown Visible for testing only.
     */
    @android.annotation.TestApi
    public static final int MODE_CLOCK = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "MODE_" }, value = { android.widget.TimePicker.MODE_SPINNER, android.widget.TimePicker.MODE_CLOCK })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TimePickerMode {}

    @android.annotation.UnsupportedAppUsage
    private final android.widget.TimePicker.TimePickerDelegate mDelegate;

    @android.widget.TimePicker.TimePickerMode
    private final int mMode;

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {
        /**
         *
         *
         * @param view
         * 		The view associated with this listener.
         * @param hourOfDay
         * 		The current hour.
         * @param minute
         * 		The current minute.
         */
        void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute);
    }

    public TimePicker(android.content.Context context) {
        this(context, null);
    }

    public TimePicker(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.timePickerStyle);
    }

    public TimePicker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePicker(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // DatePicker is important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.TimePicker, attrs, a, defStyleAttr, defStyleRes);
        final boolean isDialogMode = a.getBoolean(R.styleable.TimePicker_dialogMode, false);
        final int requestedMode = a.getInt(R.styleable.TimePicker_timePickerMode, android.widget.TimePicker.MODE_SPINNER);
        a.recycle();
        if ((requestedMode == android.widget.TimePicker.MODE_CLOCK) && isDialogMode) {
            // You want MODE_CLOCK? YOU CAN'T HANDLE MODE_CLOCK! Well, maybe
            // you can depending on your screen size. Let's check...
            mMode = context.getResources().getInteger(R.integer.time_picker_mode);
        } else {
            mMode = requestedMode;
        }
        switch (mMode) {
            case android.widget.TimePicker.MODE_CLOCK :
                mDelegate = new android.widget.TimePickerClockDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                break;
            case android.widget.TimePicker.MODE_SPINNER :
            default :
                mDelegate = new android.widget.TimePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                break;
        }
        mDelegate.setAutoFillChangeListener(( v, h, m) -> {
            final android.view.autofill.AutofillManager afm = context.getSystemService(android.view.autofill.AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
        });
    }

    /**
     *
     *
     * @return the picker's presentation mode, one of {@link #MODE_CLOCK} or
    {@link #MODE_SPINNER}
     * @unknown ref android.R.styleable#TimePicker_timePickerMode
     * @unknown Visible for testing only.
     */
    @android.widget.TimePicker.TimePickerMode
    @android.annotation.TestApi
    @android.view.inspector.InspectableProperty(name = "timePickerMode", enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(name = "clock", value = android.widget.TimePicker.MODE_CLOCK), @android.view.inspector.InspectableProperty.EnumEntry(name = "spinner", value = android.widget.TimePicker.MODE_SPINNER) })
    public int getMode() {
        return mMode;
    }

    /**
     * Sets the currently selected hour using 24-hour time.
     *
     * @param hour
     * 		the hour to set, in the range (0-23)
     * @see #getHour()
     */
    public void setHour(@android.annotation.IntRange(from = 0, to = 23)
    int hour) {
        mDelegate.setHour(android.util.MathUtils.constrain(hour, 0, 23));
    }

    /**
     * Returns the currently selected hour using 24-hour time.
     *
     * @return the currently selected hour, in the range (0-23)
     * @see #setHour(int)
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public int getHour() {
        return mDelegate.getHour();
    }

    /**
     * Sets the currently selected minute.
     *
     * @param minute
     * 		the minute to set, in the range (0-59)
     * @see #getMinute()
     */
    public void setMinute(@android.annotation.IntRange(from = 0, to = 59)
    int minute) {
        mDelegate.setMinute(android.util.MathUtils.constrain(minute, 0, 59));
    }

    /**
     * Returns the currently selected minute.
     *
     * @return the currently selected minute, in the range (0-59)
     * @see #setMinute(int)
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public int getMinute() {
        return mDelegate.getMinute();
    }

    /**
     * Sets the currently selected hour using 24-hour time.
     *
     * @param currentHour
     * 		the hour to set, in the range (0-23)
     * @deprecated Use {@link #setHour(int)}
     */
    @java.lang.Deprecated
    public void setCurrentHour(@android.annotation.NonNull
    java.lang.Integer currentHour) {
        setHour(currentHour);
    }

    /**
     *
     *
     * @return the currently selected hour, in the range (0-23)
     * @deprecated Use {@link #getHour()}
     */
    @android.annotation.NonNull
    @java.lang.Deprecated
    public java.lang.Integer getCurrentHour() {
        return getHour();
    }

    /**
     * Sets the currently selected minute.
     *
     * @param currentMinute
     * 		the minute to set, in the range (0-59)
     * @deprecated Use {@link #setMinute(int)}
     */
    @java.lang.Deprecated
    public void setCurrentMinute(@android.annotation.NonNull
    java.lang.Integer currentMinute) {
        setMinute(currentMinute);
    }

    /**
     *
     *
     * @return the currently selected minute, in the range (0-59)
     * @deprecated Use {@link #getMinute()}
     */
    @android.annotation.NonNull
    @java.lang.Deprecated
    public java.lang.Integer getCurrentMinute() {
        return getMinute();
    }

    /**
     * Sets whether this widget displays time in 24-hour mode or 12-hour mode
     * with an AM/PM picker.
     *
     * @param is24HourView
     * 		{@code true} to display in 24-hour mode,
     * 		{@code false} for 12-hour mode with AM/PM
     * @see #is24HourView()
     */
    public void setIs24HourView(@android.annotation.NonNull
    java.lang.Boolean is24HourView) {
        if (is24HourView == null) {
            return;
        }
        mDelegate.setIs24Hour(is24HourView);
    }

    /**
     *
     *
     * @return {@code true} if this widget displays time in 24-hour mode,
    {@code false} otherwise}
     * @see #setIs24HourView(Boolean)
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false, name = "24Hour")
    public boolean is24HourView() {
        return mDelegate.is24Hour();
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     *
     * @param onTimeChangedListener
     * 		the callback, should not be null.
     */
    public void setOnTimeChangedListener(android.widget.TimePicker.OnTimeChangedListener onTimeChangedListener) {
        mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDelegate.setEnabled(enabled);
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mDelegate.isEnabled();
    }

    @java.lang.Override
    public int getBaseline() {
        return mDelegate.getBaseline();
    }

    /**
     * Validates whether current input by the user is a valid time based on the locale. TimePicker
     * will show an error message to the user if the time is not valid.
     *
     * @return {@code true} if the input is valid, {@code false} otherwise
     */
    public boolean validateInput() {
        return mDelegate.validateInput();
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

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.TimePicker.class.getName();
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
    @android.annotation.TestApi
    public android.view.View getHourView() {
        return mDelegate.getHourView();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public android.view.View getMinuteView() {
        return mDelegate.getMinuteView();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public android.view.View getAmView() {
        return mDelegate.getAmView();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public android.view.View getPmView() {
        return mDelegate.getPmView();
    }

    /**
     * A delegate interface that defined the public API of the TimePicker. Allows different
     * TimePicker implementations. This would need to be implemented by the TimePicker delegates
     * for the real behavior.
     */
    interface TimePickerDelegate {
        void setHour(@android.annotation.IntRange(from = 0, to = 23)
        int hour);

        int getHour();

        void setMinute(@android.annotation.IntRange(from = 0, to = 59)
        int minute);

        int getMinute();

        void setDate(@android.annotation.IntRange(from = 0, to = 23)
        int hour, @android.annotation.IntRange(from = 0, to = 59)
        int minute);

        void autofill(android.view.autofill.AutofillValue value);

        android.view.autofill.AutofillValue getAutofillValue();

        void setIs24Hour(boolean is24Hour);

        boolean is24Hour();

        boolean validateInput();

        void setOnTimeChangedListener(android.widget.TimePicker.OnTimeChangedListener onTimeChangedListener);

        void setAutoFillChangeListener(android.widget.TimePicker.OnTimeChangedListener autoFillChangeListener);

        void setEnabled(boolean enabled);

        boolean isEnabled();

        int getBaseline();

        android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState);

        void onRestoreInstanceState(android.os.Parcelable state);

        boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event);

        void onPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event);

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        android.view.View getHourView();

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        android.view.View getMinuteView();

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        android.view.View getAmView();

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.TestApi
        android.view.View getPmView();
    }

    static java.lang.String[] getAmPmStrings(android.content.Context context) {
        final java.util.Locale locale = context.getResources().getConfiguration().locale;
        final libcore.icu.LocaleData d = libcore.icu.LocaleData.get(locale);
        final java.lang.String[] result = new java.lang.String[2];
        result[0] = (d.amPm[0].length() > 4) ? d.narrowAm : d.amPm[0];
        result[1] = (d.amPm[1].length() > 4) ? d.narrowPm : d.amPm[1];
        return result;
    }

    /**
     * An abstract class which can be used as a start for TimePicker implementations
     */
    static abstract class AbstractTimePickerDelegate implements android.widget.TimePicker.TimePickerDelegate {
        protected final android.widget.TimePicker mDelegator;

        protected final android.content.Context mContext;

        protected final java.util.Locale mLocale;

        protected android.widget.TimePicker.OnTimeChangedListener mOnTimeChangedListener;

        protected android.widget.TimePicker.OnTimeChangedListener mAutoFillChangeListener;

        // The value that was passed to autofill() - it must be stored because it getAutofillValue()
        // must return the exact same value that was autofilled, otherwise the widget will not be
        // properly highlighted after autofill().
        private long mAutofilledValue;

        public AbstractTimePickerDelegate(@android.annotation.NonNull
        android.widget.TimePicker delegator, @android.annotation.NonNull
        android.content.Context context) {
            mDelegator = delegator;
            mContext = context;
            mLocale = context.getResources().getConfiguration().locale;
        }

        @java.lang.Override
        public void setOnTimeChangedListener(android.widget.TimePicker.OnTimeChangedListener callback) {
            mOnTimeChangedListener = callback;
        }

        @java.lang.Override
        public void setAutoFillChangeListener(android.widget.TimePicker.OnTimeChangedListener callback) {
            mAutoFillChangeListener = callback;
        }

        @java.lang.Override
        public final void autofill(android.view.autofill.AutofillValue value) {
            if ((value == null) || (!value.isDate())) {
                android.util.Log.w(android.widget.TimePicker.LOG_TAG, (value + " could not be autofilled into ") + this);
                return;
            }
            final long time = value.getDateValue();
            final android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance(mLocale);
            cal.setTimeInMillis(time);
            setDate(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            // Must set mAutofilledValue *after* calling subclass method to make sure the value
            // returned by getAutofillValue() matches it.
            mAutofilledValue = time;
        }

        @java.lang.Override
        public final android.view.autofill.AutofillValue getAutofillValue() {
            if (mAutofilledValue != 0) {
                return android.view.autofill.AutofillValue.forDate(mAutofilledValue);
            }
            final android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance(mLocale);
            cal.set(Calendar.HOUR_OF_DAY, getHour());
            cal.set(Calendar.MINUTE, getMinute());
            return android.view.autofill.AutofillValue.forDate(cal.getTimeInMillis());
        }

        /**
         * This method must be called every time the value of the hour and/or minute is changed by
         * a subclass method.
         */
        protected void resetAutofilledValue() {
            mAutofilledValue = 0;
        }

        protected static class SavedState extends android.view.View.BaseSavedState {
            private final int mHour;

            private final int mMinute;

            private final boolean mIs24HourMode;

            private final int mCurrentItemShowing;

            public SavedState(android.os.Parcelable superState, int hour, int minute, boolean is24HourMode) {
                this(superState, hour, minute, is24HourMode, 0);
            }

            public SavedState(android.os.Parcelable superState, int hour, int minute, boolean is24HourMode, int currentItemShowing) {
                super(superState);
                mHour = hour;
                mMinute = minute;
                mIs24HourMode = is24HourMode;
                mCurrentItemShowing = currentItemShowing;
            }

            private SavedState(android.os.Parcel in) {
                super(in);
                mHour = in.readInt();
                mMinute = in.readInt();
                mIs24HourMode = in.readInt() == 1;
                mCurrentItemShowing = in.readInt();
            }

            public int getHour() {
                return mHour;
            }

            public int getMinute() {
                return mMinute;
            }

            public boolean is24HourMode() {
                return mIs24HourMode;
            }

            public int getCurrentItemShowing() {
                return mCurrentItemShowing;
            }

            @java.lang.Override
            public void writeToParcel(android.os.Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(mHour);
                dest.writeInt(mMinute);
                dest.writeInt(mIs24HourMode ? 1 : 0);
                dest.writeInt(mCurrentItemShowing);
            }

            @java.lang.SuppressWarnings({ "unused", "hiding" })
            @android.annotation.NonNull
            public static final android.widget.Creator<android.widget.TimePicker.AbstractTimePickerDelegate.SavedState> CREATOR = new android.widget.Creator<android.widget.TimePicker.AbstractTimePickerDelegate.SavedState>() {
                public android.widget.TimePicker.AbstractTimePickerDelegate.SavedState createFromParcel(android.os.Parcel in) {
                    return new android.widget.TimePicker.AbstractTimePickerDelegate.SavedState(in);
                }

                public android.widget.TimePicker.AbstractTimePickerDelegate.SavedState[] newArray(int size) {
                    return new android.widget.TimePicker.AbstractTimePickerDelegate.SavedState[size];
                }
            };
        }
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

