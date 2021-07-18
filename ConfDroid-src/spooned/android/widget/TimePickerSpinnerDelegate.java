/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * A delegate implementing the basic spinner-based TimePicker.
 */
class TimePickerSpinnerDelegate extends android.widget.TimePicker.AbstractTimePickerDelegate {
    private static final boolean DEFAULT_ENABLED_STATE = true;

    private static final int HOURS_IN_HALF_DAY = 12;

    private final android.widget.NumberPicker mHourSpinner;

    private final android.widget.NumberPicker mMinuteSpinner;

    private final android.widget.NumberPicker mAmPmSpinner;

    private final android.widget.EditText mHourSpinnerInput;

    private final android.widget.EditText mMinuteSpinnerInput;

    private final android.widget.EditText mAmPmSpinnerInput;

    private final android.widget.TextView mDivider;

    // Note that the legacy implementation of the TimePicker is
    // using a button for toggling between AM/PM while the new
    // version uses a NumberPicker spinner. Therefore the code
    // accommodates these two cases to be backwards compatible.
    private final android.widget.Button mAmPmButton;

    private final java.lang.String[] mAmPmStrings;

    private final java.util.Calendar mTempCalendar;

    private boolean mIsEnabled = android.widget.TimePickerSpinnerDelegate.DEFAULT_ENABLED_STATE;

    private boolean mHourWithTwoDigit;

    private char mHourFormat;

    private boolean mIs24HourView;

    private boolean mIsAm;

    public TimePickerSpinnerDelegate(android.widget.TimePicker delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        // process style attributes
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        final int layoutResourceId = a.getResourceId(R.styleable.TimePicker_legacyLayout, R.layout.time_picker_legacy);
        a.recycle();
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        final android.view.View view = inflater.inflate(layoutResourceId, mDelegator, true);
        view.setSaveFromParentEnabled(false);
        // hour
        mHourSpinner = delegator.findViewById(R.id.hour);
        mHourSpinner.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
            public void onValueChange(android.widget.NumberPicker spinner, int oldVal, int newVal) {
                updateInputState();
                if (!is24Hour()) {
                    if (((oldVal == (android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY - 1)) && (newVal == android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY)) || ((oldVal == android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY) && (newVal == (android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY - 1)))) {
                        mIsAm = !mIsAm;
                        updateAmPmControl();
                    }
                }
                onTimeChanged();
            }
        });
        mHourSpinnerInput = mHourSpinner.findViewById(R.id.numberpicker_input);
        mHourSpinnerInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NEXT);
        // divider (only for the new widget style)
        mDivider = mDelegator.findViewById(R.id.divider);
        if (mDivider != null) {
            setDividerText();
        }
        // minute
        mMinuteSpinner = mDelegator.findViewById(R.id.minute);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setOnLongPressUpdateInterval(100);
        mMinuteSpinner.setFormatter(android.widget.NumberPicker.getTwoDigitFormatter());
        mMinuteSpinner.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
            public void onValueChange(android.widget.NumberPicker spinner, int oldVal, int newVal) {
                updateInputState();
                int minValue = mMinuteSpinner.getMinValue();
                int maxValue = mMinuteSpinner.getMaxValue();
                if ((oldVal == maxValue) && (newVal == minValue)) {
                    int newHour = mHourSpinner.getValue() + 1;
                    if ((!is24Hour()) && (newHour == android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY)) {
                        mIsAm = !mIsAm;
                        updateAmPmControl();
                    }
                    mHourSpinner.setValue(newHour);
                } else
                    if ((oldVal == minValue) && (newVal == maxValue)) {
                        int newHour = mHourSpinner.getValue() - 1;
                        if ((!is24Hour()) && (newHour == (android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY - 1))) {
                            mIsAm = !mIsAm;
                            updateAmPmControl();
                        }
                        mHourSpinner.setValue(newHour);
                    }

                onTimeChanged();
            }
        });
        mMinuteSpinnerInput = mMinuteSpinner.findViewById(R.id.numberpicker_input);
        mMinuteSpinnerInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NEXT);
        // Get the localized am/pm strings and use them in the spinner.
        mAmPmStrings = android.widget.TimePickerSpinnerDelegate.getAmPmStrings(context);
        // am/pm
        final android.view.View amPmView = mDelegator.findViewById(R.id.amPm);
        if (amPmView instanceof android.widget.Button) {
            mAmPmSpinner = null;
            mAmPmSpinnerInput = null;
            mAmPmButton = ((android.widget.Button) (amPmView));
            mAmPmButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View button) {
                    button.requestFocus();
                    mIsAm = !mIsAm;
                    updateAmPmControl();
                    onTimeChanged();
                }
            });
        } else {
            mAmPmButton = null;
            mAmPmSpinner = ((android.widget.NumberPicker) (amPmView));
            mAmPmSpinner.setMinValue(0);
            mAmPmSpinner.setMaxValue(1);
            mAmPmSpinner.setDisplayedValues(mAmPmStrings);
            mAmPmSpinner.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
                public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                    updateInputState();
                    picker.requestFocus();
                    mIsAm = !mIsAm;
                    updateAmPmControl();
                    onTimeChanged();
                }
            });
            mAmPmSpinnerInput = mAmPmSpinner.findViewById(R.id.numberpicker_input);
            mAmPmSpinnerInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        }
        if (isAmPmAtStart()) {
            // Move the am/pm view to the beginning
            android.view.ViewGroup amPmParent = delegator.findViewById(R.id.timePickerLayout);
            amPmParent.removeView(amPmView);
            amPmParent.addView(amPmView, 0);
            // Swap layout margins if needed. They may be not symmetrical (Old Standard Theme
            // for example and not for Holo Theme)
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (amPmView.getLayoutParams()));
            final int startMargin = lp.getMarginStart();
            final int endMargin = lp.getMarginEnd();
            if (startMargin != endMargin) {
                lp.setMarginStart(endMargin);
                lp.setMarginEnd(startMargin);
            }
        }
        getHourFormatData();
        // update controls to initial state
        updateHourControl();
        updateMinuteControl();
        updateAmPmControl();
        // set to current time
        mTempCalendar = java.util.Calendar.getInstance(mLocale);
        setHour(mTempCalendar.get(java.util.Calendar.HOUR_OF_DAY));
        setMinute(mTempCalendar.get(java.util.Calendar.MINUTE));
        if (!isEnabled()) {
            setEnabled(false);
        }
        // set the content descriptions
        setContentDescriptions();
        // If not explicitly specified this view is important for accessibility.
        if (mDelegator.getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            mDelegator.setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    @java.lang.Override
    public boolean validateInput() {
        return true;
    }

    private void getHourFormatData() {
        final java.lang.String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(mLocale, mIs24HourView ? "Hm" : "hm");
        final int lengthPattern = bestDateTimePattern.length();
        mHourWithTwoDigit = false;
        char hourFormat = '\u0000';
        // Check if the returned pattern is single or double 'H', 'h', 'K', 'k'. We also save
        // the hour format that we found.
        for (int i = 0; i < lengthPattern; i++) {
            final char c = bestDateTimePattern.charAt(i);
            if ((((c == 'H') || (c == 'h')) || (c == 'K')) || (c == 'k')) {
                mHourFormat = c;
                if (((i + 1) < lengthPattern) && (c == bestDateTimePattern.charAt(i + 1))) {
                    mHourWithTwoDigit = true;
                }
                break;
            }
        }
    }

    private boolean isAmPmAtStart() {
        final java.lang.String bestDateTimePattern = /* skeleton */
        android.text.format.DateFormat.getBestDateTimePattern(mLocale, "hm");
        return bestDateTimePattern.startsWith("a");
    }

    /**
     * The time separator is defined in the Unicode CLDR and cannot be supposed to be ":".
     *
     * See http://unicode.org/cldr/trac/browser/trunk/common/main
     *
     * We pass the correct "skeleton" depending on 12 or 24 hours view and then extract the
     * separator as the character which is just after the hour marker in the returned pattern.
     */
    private void setDividerText() {
        final java.lang.String skeleton = (mIs24HourView) ? "Hm" : "hm";
        final java.lang.String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(mLocale, skeleton);
        final java.lang.String separatorText;
        int hourIndex = bestDateTimePattern.lastIndexOf('H');
        if (hourIndex == (-1)) {
            hourIndex = bestDateTimePattern.lastIndexOf('h');
        }
        if (hourIndex == (-1)) {
            // Default case
            separatorText = ":";
        } else {
            int minuteIndex = bestDateTimePattern.indexOf('m', hourIndex + 1);
            if (minuteIndex == (-1)) {
                separatorText = java.lang.Character.toString(bestDateTimePattern.charAt(hourIndex + 1));
            } else {
                separatorText = bestDateTimePattern.substring(hourIndex + 1, minuteIndex);
            }
        }
        mDivider.setText(separatorText);
    }

    @java.lang.Override
    public void setDate(int hour, int minute) {
        setCurrentHour(hour, false);
        setCurrentMinute(minute, false);
        onTimeChanged();
    }

    @java.lang.Override
    public void setHour(int hour) {
        setCurrentHour(hour, true);
    }

    private void setCurrentHour(int currentHour, boolean notifyTimeChanged) {
        // why was Integer used in the first place?
        if (currentHour == getHour()) {
            return;
        }
        resetAutofilledValue();
        if (!is24Hour()) {
            // convert [0,23] ordinal to wall clock display
            if (currentHour >= android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY) {
                mIsAm = false;
                if (currentHour > android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY) {
                    currentHour = currentHour - android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY;
                }
            } else {
                mIsAm = true;
                if (currentHour == 0) {
                    currentHour = android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY;
                }
            }
            updateAmPmControl();
        }
        mHourSpinner.setValue(currentHour);
        if (notifyTimeChanged) {
            onTimeChanged();
        }
    }

    @java.lang.Override
    public int getHour() {
        int currentHour = mHourSpinner.getValue();
        if (is24Hour()) {
            return currentHour;
        } else
            if (mIsAm) {
                return currentHour % android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY;
            } else {
                return (currentHour % android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY) + android.widget.TimePickerSpinnerDelegate.HOURS_IN_HALF_DAY;
            }

    }

    @java.lang.Override
    public void setMinute(int minute) {
        setCurrentMinute(minute, true);
    }

    private void setCurrentMinute(int minute, boolean notifyTimeChanged) {
        if (minute == getMinute()) {
            return;
        }
        resetAutofilledValue();
        mMinuteSpinner.setValue(minute);
        if (notifyTimeChanged) {
            onTimeChanged();
        }
    }

    @java.lang.Override
    public int getMinute() {
        return mMinuteSpinner.getValue();
    }

    public void setIs24Hour(boolean is24Hour) {
        if (mIs24HourView == is24Hour) {
            return;
        }
        // cache the current hour since spinner range changes and BEFORE changing mIs24HourView!!
        int currentHour = getHour();
        // Order is important here.
        mIs24HourView = is24Hour;
        getHourFormatData();
        updateHourControl();
        // set value after spinner range is updated
        setCurrentHour(currentHour, false);
        updateMinuteControl();
        updateAmPmControl();
    }

    @java.lang.Override
    public boolean is24Hour() {
        return mIs24HourView;
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        mMinuteSpinner.setEnabled(enabled);
        if (mDivider != null) {
            mDivider.setEnabled(enabled);
        }
        mHourSpinner.setEnabled(enabled);
        if (mAmPmSpinner != null) {
            mAmPmSpinner.setEnabled(enabled);
        } else {
            mAmPmButton.setEnabled(enabled);
        }
        mIsEnabled = enabled;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @java.lang.Override
    public int getBaseline() {
        return mHourSpinner.getBaseline();
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState) {
        return new android.widget.TimePicker.AbstractTimePickerDelegate.SavedState(superState, getHour(), getMinute(), is24Hour());
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (state instanceof android.widget.TimePicker.AbstractTimePickerDelegate.SavedState) {
            final android.widget.TimePicker.AbstractTimePickerDelegate.SavedState ss = ((android.widget.TimePicker.AbstractTimePickerDelegate.SavedState) (state));
            setHour(ss.getHour());
            setMinute(ss.getMinute());
        }
    }

    @java.lang.Override
    public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    @java.lang.Override
    public void onPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        int flags = android.text.format.DateUtils.FORMAT_SHOW_TIME;
        if (mIs24HourView) {
            flags |= android.text.format.DateUtils.FORMAT_24HOUR;
        } else {
            flags |= android.text.format.DateUtils.FORMAT_12HOUR;
        }
        mTempCalendar.set(java.util.Calendar.HOUR_OF_DAY, getHour());
        mTempCalendar.set(java.util.Calendar.MINUTE, getMinute());
        java.lang.String selectedDateUtterance = android.text.format.DateUtils.formatDateTime(mContext, mTempCalendar.getTimeInMillis(), flags);
        event.getText().add(selectedDateUtterance);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getHourView() {
        return mHourSpinnerInput;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getMinuteView() {
        return mMinuteSpinnerInput;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getAmView() {
        return mAmPmSpinnerInput;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getPmView() {
        return mAmPmSpinnerInput;
    }

    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        android.view.inputmethod.InputMethodManager inputMethodManager = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mHourSpinnerInput)) {
                mHourSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
            } else
                if (inputMethodManager.isActive(mMinuteSpinnerInput)) {
                    mMinuteSpinnerInput.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
                } else
                    if (inputMethodManager.isActive(mAmPmSpinnerInput)) {
                        mAmPmSpinnerInput.clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
                    }


        }
    }

    private void updateAmPmControl() {
        if (is24Hour()) {
            if (mAmPmSpinner != null) {
                mAmPmSpinner.setVisibility(android.view.View.GONE);
            } else {
                mAmPmButton.setVisibility(android.view.View.GONE);
            }
        } else {
            int index = (mIsAm) ? java.util.Calendar.AM : java.util.Calendar.PM;
            if (mAmPmSpinner != null) {
                mAmPmSpinner.setValue(index);
                mAmPmSpinner.setVisibility(android.view.View.VISIBLE);
            } else {
                mAmPmButton.setText(mAmPmStrings[index]);
                mAmPmButton.setVisibility(android.view.View.VISIBLE);
            }
        }
        mDelegator.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
    }

    private void onTimeChanged() {
        mDelegator.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnTimeChangedListener != null) {
            mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
        }
        if (mAutoFillChangeListener != null) {
            mAutoFillChangeListener.onTimeChanged(mDelegator, getHour(), getMinute());
        }
    }

    private void updateHourControl() {
        if (is24Hour()) {
            // 'k' means 1-24 hour
            if (mHourFormat == 'k') {
                mHourSpinner.setMinValue(1);
                mHourSpinner.setMaxValue(24);
            } else {
                mHourSpinner.setMinValue(0);
                mHourSpinner.setMaxValue(23);
            }
        } else {
            // 'K' means 0-11 hour
            if (mHourFormat == 'K') {
                mHourSpinner.setMinValue(0);
                mHourSpinner.setMaxValue(11);
            } else {
                mHourSpinner.setMinValue(1);
                mHourSpinner.setMaxValue(12);
            }
        }
        mHourSpinner.setFormatter(mHourWithTwoDigit ? android.widget.NumberPicker.getTwoDigitFormatter() : null);
    }

    private void updateMinuteControl() {
        if (is24Hour()) {
            mMinuteSpinnerInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        } else {
            mMinuteSpinnerInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NEXT);
        }
    }

    private void setContentDescriptions() {
        // Minute
        trySetContentDescription(mMinuteSpinner, R.id.increment, R.string.time_picker_increment_minute_button);
        trySetContentDescription(mMinuteSpinner, R.id.decrement, R.string.time_picker_decrement_minute_button);
        // Hour
        trySetContentDescription(mHourSpinner, R.id.increment, R.string.time_picker_increment_hour_button);
        trySetContentDescription(mHourSpinner, R.id.decrement, R.string.time_picker_decrement_hour_button);
        // AM/PM
        if (mAmPmSpinner != null) {
            trySetContentDescription(mAmPmSpinner, R.id.increment, R.string.time_picker_increment_set_pm_button);
            trySetContentDescription(mAmPmSpinner, R.id.decrement, R.string.time_picker_decrement_set_am_button);
        }
    }

    private void trySetContentDescription(android.view.View root, int viewId, int contDescResId) {
        android.view.View target = root.findViewById(viewId);
        if (target != null) {
            target.setContentDescription(mContext.getString(contDescResId));
        }
    }

    public static java.lang.String[] getAmPmStrings(android.content.Context context) {
        java.lang.String[] result = new java.lang.String[2];
        libcore.icu.LocaleData d = libcore.icu.LocaleData.get(context.getResources().getConfiguration().locale);
        result[0] = (d.amPm[0].length() > 4) ? d.narrowAm : d.amPm[0];
        result[1] = (d.amPm[1].length() > 4) ? d.narrowPm : d.amPm[1];
        return result;
    }
}

