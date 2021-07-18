/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * View to show text input based time picker with hour and minute fields and an optional AM/PM
 * spinner.
 *
 * @unknown 
 */
public class TextInputTimePickerView extends android.widget.RelativeLayout {
    public static final int HOURS = 0;

    public static final int MINUTES = 1;

    public static final int AMPM = 2;

    private static final int AM = 0;

    private static final int PM = 1;

    private final android.widget.EditText mHourEditText;

    private final android.widget.EditText mMinuteEditText;

    private final android.widget.TextView mInputSeparatorView;

    private final android.widget.Spinner mAmPmSpinner;

    private final android.widget.TextView mErrorLabel;

    private final android.widget.TextView mHourLabel;

    private final android.widget.TextView mMinuteLabel;

    private boolean mIs24Hour;

    private boolean mHourFormatStartsAtZero;

    private android.widget.TextInputTimePickerView.OnValueTypedListener mListener;

    private boolean mErrorShowing;

    private boolean mTimeSet;

    interface OnValueTypedListener {
        void onValueChanged(int inputType, int newValue);
    }

    public TextInputTimePickerView(android.content.Context context) {
        this(context, null);
    }

    public TextInputTimePickerView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputTimePickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public TextInputTimePickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        android.view.View.inflate(context, R.layout.time_picker_text_input_material, this);
        mHourEditText = findViewById(R.id.input_hour);
        mMinuteEditText = findViewById(R.id.input_minute);
        mInputSeparatorView = findViewById(R.id.input_separator);
        mErrorLabel = findViewById(R.id.label_error);
        mHourLabel = findViewById(R.id.label_hour);
        mMinuteLabel = findViewById(R.id.label_minute);
        mHourEditText.addTextChangedListener(new android.text.TextWatcher() {
            @java.lang.Override
            public void beforeTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2) {
            }

            @java.lang.Override
            public void onTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2) {
            }

            @java.lang.Override
            public void afterTextChanged(android.text.Editable editable) {
                if (parseAndSetHourInternal(editable.toString()) && (editable.length() > 1)) {
                    android.view.accessibility.AccessibilityManager am = ((android.view.accessibility.AccessibilityManager) (context.getSystemService(context.ACCESSIBILITY_SERVICE)));
                    if (!am.isEnabled()) {
                        mMinuteEditText.requestFocus();
                    }
                }
            }
        });
        mMinuteEditText.addTextChangedListener(new android.text.TextWatcher() {
            @java.lang.Override
            public void beforeTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2) {
            }

            @java.lang.Override
            public void onTextChanged(java.lang.CharSequence charSequence, int i, int i1, int i2) {
            }

            @java.lang.Override
            public void afterTextChanged(android.text.Editable editable) {
                parseAndSetMinuteInternal(editable.toString());
            }
        });
        mAmPmSpinner = findViewById(R.id.am_pm_spinner);
        final java.lang.String[] amPmStrings = android.widget.TimePicker.getAmPmStrings(context);
        android.widget.ArrayAdapter<java.lang.CharSequence> adapter = new android.widget.ArrayAdapter<java.lang.CharSequence>(context, R.layout.simple_spinner_dropdown_item);
        adapter.add(android.widget.TimePickerClockDelegate.obtainVerbatim(amPmStrings[0]));
        adapter.add(android.widget.TimePickerClockDelegate.obtainVerbatim(amPmStrings[1]));
        mAmPmSpinner.setAdapter(adapter);
        mAmPmSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @java.lang.Override
            public void onItemSelected(android.widget.AdapterView<?> adapterView, android.view.View view, int position, long id) {
                if (position == 0) {
                    mListener.onValueChanged(android.widget.TextInputTimePickerView.AMPM, android.widget.TextInputTimePickerView.AM);
                } else {
                    mListener.onValueChanged(android.widget.TextInputTimePickerView.AMPM, android.widget.TextInputTimePickerView.PM);
                }
            }

            @java.lang.Override
            public void onNothingSelected(android.widget.AdapterView<?> adapterView) {
            }
        });
    }

    void setListener(android.widget.TextInputTimePickerView.OnValueTypedListener listener) {
        mListener = listener;
    }

    void setHourFormat(int maxCharLength) {
        mHourEditText.setFilters(new android.text.InputFilter[]{ new android.text.InputFilter.LengthFilter(maxCharLength) });
        mMinuteEditText.setFilters(new android.text.InputFilter[]{ new android.text.InputFilter.LengthFilter(maxCharLength) });
        final android.os.LocaleList locales = mContext.getResources().getConfiguration().getLocales();
        mHourEditText.setImeHintLocales(locales);
        mMinuteEditText.setImeHintLocales(locales);
    }

    boolean validateInput() {
        final java.lang.String hourText = (android.text.TextUtils.isEmpty(mHourEditText.getText())) ? mHourEditText.getHint().toString() : mHourEditText.getText().toString();
        final java.lang.String minuteText = (android.text.TextUtils.isEmpty(mMinuteEditText.getText())) ? mMinuteEditText.getHint().toString() : mMinuteEditText.getText().toString();
        final boolean inputValid = parseAndSetHourInternal(hourText) && parseAndSetMinuteInternal(minuteText);
        setError(!inputValid);
        return inputValid;
    }

    void updateSeparator(java.lang.String separatorText) {
        mInputSeparatorView.setText(separatorText);
    }

    private void setError(boolean enabled) {
        mErrorShowing = enabled;
        mErrorLabel.setVisibility(enabled ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        mHourLabel.setVisibility(enabled ? android.view.View.INVISIBLE : android.view.View.VISIBLE);
        mMinuteLabel.setVisibility(enabled ? android.view.View.INVISIBLE : android.view.View.VISIBLE);
    }

    private void setTimeSet(boolean timeSet) {
        mTimeSet = mTimeSet || timeSet;
    }

    private boolean isTimeSet() {
        return mTimeSet;
    }

    /**
     * Computes the display value and updates the text of the view.
     * <p>
     * This method should be called whenever the current value or display
     * properties (leading zeroes, max digits) change.
     */
    void updateTextInputValues(int localizedHour, int minute, int amOrPm, boolean is24Hour, boolean hourFormatStartsAtZero) {
        final java.lang.String hourFormat = "%d";
        final java.lang.String minuteFormat = "%02d";
        mIs24Hour = is24Hour;
        mHourFormatStartsAtZero = hourFormatStartsAtZero;
        mAmPmSpinner.setVisibility(is24Hour ? android.view.View.INVISIBLE : android.view.View.VISIBLE);
        if (amOrPm == android.widget.TextInputTimePickerView.AM) {
            mAmPmSpinner.setSelection(0);
        } else {
            mAmPmSpinner.setSelection(1);
        }
        if (isTimeSet()) {
            mHourEditText.setText(java.lang.String.format(hourFormat, localizedHour));
            mMinuteEditText.setText(java.lang.String.format(minuteFormat, minute));
        } else {
            mHourEditText.setHint(java.lang.String.format(hourFormat, localizedHour));
            mMinuteEditText.setHint(java.lang.String.format(minuteFormat, minute));
        }
        if (mErrorShowing) {
            validateInput();
        }
    }

    private boolean parseAndSetHourInternal(java.lang.String input) {
        try {
            final int hour = java.lang.Integer.parseInt(input);
            if (!isValidLocalizedHour(hour)) {
                final int minHour = (mHourFormatStartsAtZero) ? 0 : 1;
                final int maxHour = (mIs24Hour) ? 23 : 11 + minHour;
                mListener.onValueChanged(android.widget.TextInputTimePickerView.HOURS, getHourOfDayFromLocalizedHour(android.util.MathUtils.constrain(hour, minHour, maxHour)));
                return false;
            }
            mListener.onValueChanged(android.widget.TextInputTimePickerView.HOURS, getHourOfDayFromLocalizedHour(hour));
            setTimeSet(true);
            return true;
        } catch (java.lang.NumberFormatException e) {
            // Do nothing since we cannot parse the input.
            return false;
        }
    }

    private boolean parseAndSetMinuteInternal(java.lang.String input) {
        try {
            final int minutes = java.lang.Integer.parseInt(input);
            if ((minutes < 0) || (minutes > 59)) {
                mListener.onValueChanged(android.widget.TextInputTimePickerView.MINUTES, android.util.MathUtils.constrain(minutes, 0, 59));
                return false;
            }
            mListener.onValueChanged(android.widget.TextInputTimePickerView.MINUTES, minutes);
            setTimeSet(true);
            return true;
        } catch (java.lang.NumberFormatException e) {
            // Do nothing since we cannot parse the input.
            return false;
        }
    }

    private boolean isValidLocalizedHour(int localizedHour) {
        final int minHour = (mHourFormatStartsAtZero) ? 0 : 1;
        final int maxHour = (mIs24Hour ? 23 : 11) + minHour;
        return (localizedHour >= minHour) && (localizedHour <= maxHour);
    }

    private int getHourOfDayFromLocalizedHour(int localizedHour) {
        int hourOfDay = localizedHour;
        if (mIs24Hour) {
            if ((!mHourFormatStartsAtZero) && (localizedHour == 24)) {
                hourOfDay = 0;
            }
        } else {
            if ((!mHourFormatStartsAtZero) && (localizedHour == 12)) {
                hourOfDay = 0;
            }
            if (mAmPmSpinner.getSelectedItemPosition() == 1) {
                hourOfDay += 12;
            }
        }
        return hourOfDay;
    }
}

