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
 * A delegate implementing the radial clock-based TimePicker.
 */
class TimePickerClockDelegate extends android.widget.TimePicker.AbstractTimePickerDelegate {
    /**
     * Delay in milliseconds before valid but potentially incomplete, for
     * example "1" but not "12", keyboard edits are propagated from the
     * hour / minute fields to the radial picker.
     */
    private static final long DELAY_COMMIT_MILLIS = 2000;

    @android.annotation.IntDef({ android.widget.TimePickerClockDelegate.FROM_EXTERNAL_API, android.widget.TimePickerClockDelegate.FROM_RADIAL_PICKER, android.widget.TimePickerClockDelegate.FROM_INPUT_PICKER })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ChangeSource {}

    private static final int FROM_EXTERNAL_API = 0;

    private static final int FROM_RADIAL_PICKER = 1;

    private static final int FROM_INPUT_PICKER = 2;

    // Index used by RadialPickerLayout
    private static final int HOUR_INDEX = android.widget.RadialTimePickerView.HOURS;

    private static final int MINUTE_INDEX = android.widget.RadialTimePickerView.MINUTES;

    private static final int[] ATTRS_TEXT_COLOR = new int[]{ R.attr.textColor };

    private static final int[] ATTRS_DISABLED_ALPHA = new int[]{ R.attr.disabledAlpha };

    private static final int AM = 0;

    private static final int PM = 1;

    private static final int HOURS_IN_HALF_DAY = 12;

    private final com.android.internal.widget.NumericTextView mHourView;

    private final com.android.internal.widget.NumericTextView mMinuteView;

    private final android.view.View mAmPmLayout;

    private final android.widget.RadioButton mAmLabel;

    private final android.widget.RadioButton mPmLabel;

    private final android.widget.RadialTimePickerView mRadialTimePickerView;

    private final android.widget.TextView mSeparatorView;

    private boolean mRadialPickerModeEnabled = true;

    private final android.widget.ImageButton mRadialTimePickerModeButton;

    private final java.lang.String mRadialTimePickerModeEnabledDescription;

    private final java.lang.String mTextInputPickerModeEnabledDescription;

    private final android.view.View mRadialTimePickerHeader;

    private final android.view.View mTextInputPickerHeader;

    private final android.widget.TextInputTimePickerView mTextInputPickerView;

    private final java.util.Calendar mTempCalendar;

    // Accessibility strings.
    private final java.lang.String mSelectHours;

    private final java.lang.String mSelectMinutes;

    private boolean mIsEnabled = true;

    private boolean mAllowAutoAdvance;

    private int mCurrentHour;

    private int mCurrentMinute;

    private boolean mIs24Hour;

    // The portrait layout puts AM/PM at the right by default.
    private boolean mIsAmPmAtLeft = false;

    // The landscape layouts put AM/PM at the bottom by default.
    private boolean mIsAmPmAtTop = false;

    // Localization data.
    private boolean mHourFormatShowLeadingZero;

    private boolean mHourFormatStartsAtZero;

    // Most recent time announcement values for accessibility.
    private java.lang.CharSequence mLastAnnouncedText;

    private boolean mLastAnnouncedIsHour;

    public TimePickerClockDelegate(android.widget.TimePicker delegator, android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        // process style attributes
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        final android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        final android.content.res.Resources res = mContext.getResources();
        mSelectHours = res.getString(R.string.select_hours);
        mSelectMinutes = res.getString(R.string.select_minutes);
        final int layoutResourceId = a.getResourceId(R.styleable.TimePicker_internalLayout, R.layout.time_picker_material);
        final android.view.View mainView = inflater.inflate(layoutResourceId, delegator);
        mainView.setSaveFromParentEnabled(false);
        mRadialTimePickerHeader = mainView.findViewById(R.id.time_header);
        mRadialTimePickerHeader.setOnTouchListener(new android.widget.TimePickerClockDelegate.NearestTouchDelegate());
        // Set up hour/minute labels.
        mHourView = ((com.android.internal.widget.NumericTextView) (mainView.findViewById(R.id.hours)));
        mHourView.setOnClickListener(mClickListener);
        mHourView.setOnFocusChangeListener(mFocusListener);
        mHourView.setOnDigitEnteredListener(mDigitEnteredListener);
        mHourView.setAccessibilityDelegate(new android.widget.TimePickerClockDelegate.ClickActionDelegate(context, R.string.select_hours));
        mSeparatorView = ((android.widget.TextView) (mainView.findViewById(R.id.separator)));
        mMinuteView = ((com.android.internal.widget.NumericTextView) (mainView.findViewById(R.id.minutes)));
        mMinuteView.setOnClickListener(mClickListener);
        mMinuteView.setOnFocusChangeListener(mFocusListener);
        mMinuteView.setOnDigitEnteredListener(mDigitEnteredListener);
        mMinuteView.setAccessibilityDelegate(new android.widget.TimePickerClockDelegate.ClickActionDelegate(context, R.string.select_minutes));
        mMinuteView.setRange(0, 59);
        // Set up AM/PM labels.
        mAmPmLayout = mainView.findViewById(R.id.ampm_layout);
        mAmPmLayout.setOnTouchListener(new android.widget.TimePickerClockDelegate.NearestTouchDelegate());
        final java.lang.String[] amPmStrings = android.widget.TimePicker.getAmPmStrings(context);
        mAmLabel = ((android.widget.RadioButton) (mAmPmLayout.findViewById(R.id.am_label)));
        mAmLabel.setText(android.widget.TimePickerClockDelegate.obtainVerbatim(amPmStrings[0]));
        mAmLabel.setOnClickListener(mClickListener);
        android.widget.TimePickerClockDelegate.ensureMinimumTextWidth(mAmLabel);
        mPmLabel = ((android.widget.RadioButton) (mAmPmLayout.findViewById(R.id.pm_label)));
        mPmLabel.setText(android.widget.TimePickerClockDelegate.obtainVerbatim(amPmStrings[1]));
        mPmLabel.setOnClickListener(mClickListener);
        android.widget.TimePickerClockDelegate.ensureMinimumTextWidth(mPmLabel);
        // For the sake of backwards compatibility, attempt to extract the text
        // color from the header time text appearance. If it's set, we'll let
        // that override the "real" header text color.
        android.content.res.ColorStateList headerTextColor = null;
        @java.lang.SuppressWarnings("deprecation")
        final int timeHeaderTextAppearance = a.getResourceId(R.styleable.TimePicker_headerTimeTextAppearance, 0);
        if (timeHeaderTextAppearance != 0) {
            final android.content.res.TypedArray textAppearance = mContext.obtainStyledAttributes(null, android.widget.TimePickerClockDelegate.ATTRS_TEXT_COLOR, 0, timeHeaderTextAppearance);
            final android.content.res.ColorStateList legacyHeaderTextColor = textAppearance.getColorStateList(0);
            headerTextColor = applyLegacyColorFixes(legacyHeaderTextColor);
            textAppearance.recycle();
        }
        if (headerTextColor == null) {
            headerTextColor = a.getColorStateList(R.styleable.TimePicker_headerTextColor);
        }
        mTextInputPickerHeader = mainView.findViewById(R.id.input_header);
        if (headerTextColor != null) {
            mHourView.setTextColor(headerTextColor);
            mSeparatorView.setTextColor(headerTextColor);
            mMinuteView.setTextColor(headerTextColor);
            mAmLabel.setTextColor(headerTextColor);
            mPmLabel.setTextColor(headerTextColor);
        }
        // Set up header background, if available.
        if (a.hasValueOrEmpty(R.styleable.TimePicker_headerBackground)) {
            mRadialTimePickerHeader.setBackground(a.getDrawable(R.styleable.TimePicker_headerBackground));
            mTextInputPickerHeader.setBackground(a.getDrawable(R.styleable.TimePicker_headerBackground));
        }
        a.recycle();
        mRadialTimePickerView = ((android.widget.RadialTimePickerView) (mainView.findViewById(R.id.radial_picker)));
        mRadialTimePickerView.applyAttributes(attrs, defStyleAttr, defStyleRes);
        mRadialTimePickerView.setOnValueSelectedListener(mOnValueSelectedListener);
        mTextInputPickerView = ((android.widget.TextInputTimePickerView) (mainView.findViewById(R.id.input_mode)));
        mTextInputPickerView.setListener(mOnValueTypedListener);
        mRadialTimePickerModeButton = ((android.widget.ImageButton) (mainView.findViewById(R.id.toggle_mode)));
        mRadialTimePickerModeButton.setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                toggleRadialPickerMode();
            }
        });
        mRadialTimePickerModeEnabledDescription = context.getResources().getString(R.string.time_picker_radial_mode_description);
        mTextInputPickerModeEnabledDescription = context.getResources().getString(R.string.time_picker_text_input_mode_description);
        mAllowAutoAdvance = true;
        updateHourFormat();
        // Initialize with current time.
        mTempCalendar = java.util.Calendar.getInstance(mLocale);
        final int currentHour = mTempCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int currentMinute = mTempCalendar.get(java.util.Calendar.MINUTE);
        initialize(currentHour, currentMinute, mIs24Hour, android.widget.TimePickerClockDelegate.HOUR_INDEX);
    }

    private void toggleRadialPickerMode() {
        if (mRadialPickerModeEnabled) {
            mRadialTimePickerView.setVisibility(android.view.View.GONE);
            mRadialTimePickerHeader.setVisibility(android.view.View.GONE);
            mTextInputPickerHeader.setVisibility(android.view.View.VISIBLE);
            mTextInputPickerView.setVisibility(android.view.View.VISIBLE);
            mRadialTimePickerModeButton.setImageResource(R.drawable.btn_clock_material);
            mRadialTimePickerModeButton.setContentDescription(mRadialTimePickerModeEnabledDescription);
            mRadialPickerModeEnabled = false;
        } else {
            mRadialTimePickerView.setVisibility(android.view.View.VISIBLE);
            mRadialTimePickerHeader.setVisibility(android.view.View.VISIBLE);
            mTextInputPickerHeader.setVisibility(android.view.View.GONE);
            mTextInputPickerView.setVisibility(android.view.View.GONE);
            mRadialTimePickerModeButton.setImageResource(R.drawable.btn_keyboard_key_material);
            mRadialTimePickerModeButton.setContentDescription(mTextInputPickerModeEnabledDescription);
            updateTextInputPicker();
            android.view.inputmethod.InputMethodManager imm = mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mDelegator.getWindowToken(), 0);
            }
            mRadialPickerModeEnabled = true;
        }
    }

    @java.lang.Override
    public boolean validateInput() {
        return mTextInputPickerView.validateInput();
    }

    /**
     * Ensures that a TextView is wide enough to contain its text without
     * wrapping or clipping. Measures the specified view and sets the minimum
     * width to the view's desired width.
     *
     * @param v
     * 		the text view to measure
     */
    private static void ensureMinimumTextWidth(android.widget.TextView v) {
        v.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED);
        // Set both the TextView and the View version of minimum
        // width because they are subtly different.
        final int minWidth = v.getMeasuredWidth();
        v.setMinWidth(minWidth);
        v.setMinimumWidth(minWidth);
    }

    /**
     * Updates hour formatting based on the current locale and 24-hour mode.
     * <p>
     * Determines how the hour should be formatted, sets member variables for
     * leading zero and starting hour, and sets the hour view's presentation.
     */
    private void updateHourFormat() {
        final java.lang.String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(mLocale, mIs24Hour ? "Hm" : "hm");
        final int lengthPattern = bestDateTimePattern.length();
        boolean showLeadingZero = false;
        char hourFormat = '\u0000';
        for (int i = 0; i < lengthPattern; i++) {
            final char c = bestDateTimePattern.charAt(i);
            if ((((c == 'H') || (c == 'h')) || (c == 'K')) || (c == 'k')) {
                hourFormat = c;
                if (((i + 1) < lengthPattern) && (c == bestDateTimePattern.charAt(i + 1))) {
                    showLeadingZero = true;
                }
                break;
            }
        }
        mHourFormatShowLeadingZero = showLeadingZero;
        mHourFormatStartsAtZero = (hourFormat == 'K') || (hourFormat == 'H');
        // Update hour text field.
        final int minHour = (mHourFormatStartsAtZero) ? 0 : 1;
        final int maxHour = (mIs24Hour ? 23 : 11) + minHour;
        mHourView.setRange(minHour, maxHour);
        mHourView.setShowLeadingZeroes(mHourFormatShowLeadingZero);
        final java.lang.String[] digits = android.icu.text.DecimalFormatSymbols.getInstance(mLocale).getDigitStrings();
        int maxCharLength = 0;
        for (int i = 0; i < 10; i++) {
            maxCharLength = java.lang.Math.max(maxCharLength, digits[i].length());
        }
        mTextInputPickerView.setHourFormat(maxCharLength * 2);
    }

    static final java.lang.CharSequence obtainVerbatim(java.lang.String text) {
        return new android.text.SpannableStringBuilder().append(text, new android.text.style.TtsSpan.VerbatimBuilder(text).build(), 0);
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
            final android.content.res.TypedArray ta = mContext.obtainStyledAttributes(android.widget.TimePickerClockDelegate.ATTRS_DISABLED_ALPHA);
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

    private static class ClickActionDelegate extends android.view.View.AccessibilityDelegate {
        private final android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction mClickAction;

        public ClickActionDelegate(android.content.Context context, int resId) {
            mClickAction = new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK, context.getString(resId));
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.view.accessibility.AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(mClickAction);
        }
    }

    private void initialize(int hourOfDay, int minute, boolean is24HourView, int index) {
        mCurrentHour = hourOfDay;
        mCurrentMinute = minute;
        mIs24Hour = is24HourView;
        updateUI(index);
    }

    private void updateUI(int index) {
        updateHeaderAmPm();
        updateHeaderHour(mCurrentHour, false);
        updateHeaderSeparator();
        updateHeaderMinute(mCurrentMinute, false);
        updateRadialPicker(index);
        updateTextInputPicker();
        mDelegator.invalidate();
    }

    private void updateTextInputPicker() {
        mTextInputPickerView.updateTextInputValues(getLocalizedHour(mCurrentHour), mCurrentMinute, mCurrentHour < 12 ? android.widget.TimePickerClockDelegate.AM : android.widget.TimePickerClockDelegate.PM, mIs24Hour, mHourFormatStartsAtZero);
    }

    private void updateRadialPicker(int index) {
        mRadialTimePickerView.initialize(mCurrentHour, mCurrentMinute, mIs24Hour);
        setCurrentItemShowing(index, false, true);
    }

    private void updateHeaderAmPm() {
        if (mIs24Hour) {
            mAmPmLayout.setVisibility(android.view.View.GONE);
        } else {
            // Find the location of AM/PM based on locale information.
            final java.lang.String dateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(mLocale, "hm");
            final boolean isAmPmAtStart = dateTimePattern.startsWith("a");
            setAmPmStart(isAmPmAtStart);
            updateAmPmLabelStates(mCurrentHour < 12 ? android.widget.TimePickerClockDelegate.AM : android.widget.TimePickerClockDelegate.PM);
        }
    }

    private void setAmPmStart(boolean isAmPmAtStart) {
        final android.widget.RelativeLayout.LayoutParams params = ((android.widget.RelativeLayout.LayoutParams) (mAmPmLayout.getLayoutParams()));
        if ((params.getRule(android.widget.RelativeLayout.RIGHT_OF) != 0) || (params.getRule(android.widget.RelativeLayout.LEFT_OF) != 0)) {
            final int margin = ((int) (mContext.getResources().getDisplayMetrics().density * 8));
            // Horizontal mode, with AM/PM appearing to left/right of hours and minutes.
            final boolean isAmPmAtLeft;
            if (android.text.TextUtils.getLayoutDirectionFromLocale(mLocale) == android.view.View.LAYOUT_DIRECTION_LTR) {
                isAmPmAtLeft = isAmPmAtStart;
            } else {
                isAmPmAtLeft = !isAmPmAtStart;
            }
            if (isAmPmAtLeft) {
                params.removeRule(android.widget.RelativeLayout.RIGHT_OF);
                params.addRule(android.widget.RelativeLayout.LEFT_OF, mHourView.getId());
            } else {
                params.removeRule(android.widget.RelativeLayout.LEFT_OF);
                params.addRule(android.widget.RelativeLayout.RIGHT_OF, mMinuteView.getId());
            }
            if (isAmPmAtStart) {
                params.setMarginStart(0);
                params.setMarginEnd(margin);
            } else {
                params.setMarginStart(margin);
                params.setMarginEnd(0);
            }
            mIsAmPmAtLeft = isAmPmAtLeft;
        } else
            if ((params.getRule(android.widget.RelativeLayout.BELOW) != 0) || (params.getRule(android.widget.RelativeLayout.ABOVE) != 0)) {
                // Vertical mode, with AM/PM appearing to top/bottom of hours and minutes.
                if (mIsAmPmAtTop == isAmPmAtStart) {
                    // AM/PM is already at the correct location. No change needed.
                    return;
                }
                final int otherViewId;
                if (isAmPmAtStart) {
                    otherViewId = params.getRule(android.widget.RelativeLayout.BELOW);
                    params.removeRule(android.widget.RelativeLayout.BELOW);
                    params.addRule(android.widget.RelativeLayout.ABOVE, otherViewId);
                } else {
                    otherViewId = params.getRule(android.widget.RelativeLayout.ABOVE);
                    params.removeRule(android.widget.RelativeLayout.ABOVE);
                    params.addRule(android.widget.RelativeLayout.BELOW, otherViewId);
                }
                // Switch the top and bottom paddings on the other view.
                final android.view.View otherView = mRadialTimePickerHeader.findViewById(otherViewId);
                final int top = otherView.getPaddingTop();
                final int bottom = otherView.getPaddingBottom();
                final int left = otherView.getPaddingLeft();
                final int right = otherView.getPaddingRight();
                otherView.setPadding(left, bottom, right, top);
                mIsAmPmAtTop = isAmPmAtStart;
            }

        mAmPmLayout.setLayoutParams(params);
    }

    @java.lang.Override
    public void setDate(int hour, int minute) {
        setHourInternal(hour, android.widget.TimePickerClockDelegate.FROM_EXTERNAL_API, true, false);
        setMinuteInternal(minute, android.widget.TimePickerClockDelegate.FROM_EXTERNAL_API, false);
        onTimeChanged();
    }

    /**
     * Set the current hour.
     */
    @java.lang.Override
    public void setHour(int hour) {
        setHourInternal(hour, android.widget.TimePickerClockDelegate.FROM_EXTERNAL_API, true, true);
    }

    private void setHourInternal(int hour, @android.widget.TimePickerClockDelegate.ChangeSource
    int source, boolean announce, boolean notify) {
        if (mCurrentHour == hour) {
            return;
        }
        resetAutofilledValue();
        mCurrentHour = hour;
        updateHeaderHour(hour, announce);
        updateHeaderAmPm();
        if (source != android.widget.TimePickerClockDelegate.FROM_RADIAL_PICKER) {
            mRadialTimePickerView.setCurrentHour(hour);
            mRadialTimePickerView.setAmOrPm(hour < 12 ? android.widget.TimePickerClockDelegate.AM : android.widget.TimePickerClockDelegate.PM);
        }
        if (source != android.widget.TimePickerClockDelegate.FROM_INPUT_PICKER) {
            updateTextInputPicker();
        }
        mDelegator.invalidate();
        if (notify) {
            onTimeChanged();
        }
    }

    /**
     *
     *
     * @return the current hour in the range (0-23)
     */
    @java.lang.Override
    public int getHour() {
        final int currentHour = mRadialTimePickerView.getCurrentHour();
        if (mIs24Hour) {
            return currentHour;
        }
        if (mRadialTimePickerView.getAmOrPm() == android.widget.TimePickerClockDelegate.PM) {
            return (currentHour % android.widget.TimePickerClockDelegate.HOURS_IN_HALF_DAY) + android.widget.TimePickerClockDelegate.HOURS_IN_HALF_DAY;
        } else {
            return currentHour % android.widget.TimePickerClockDelegate.HOURS_IN_HALF_DAY;
        }
    }

    /**
     * Set the current minute (0-59).
     */
    @java.lang.Override
    public void setMinute(int minute) {
        setMinuteInternal(minute, android.widget.TimePickerClockDelegate.FROM_EXTERNAL_API, true);
    }

    private void setMinuteInternal(int minute, @android.widget.TimePickerClockDelegate.ChangeSource
    int source, boolean notify) {
        if (mCurrentMinute == minute) {
            return;
        }
        resetAutofilledValue();
        mCurrentMinute = minute;
        updateHeaderMinute(minute, true);
        if (source != android.widget.TimePickerClockDelegate.FROM_RADIAL_PICKER) {
            mRadialTimePickerView.setCurrentMinute(minute);
        }
        if (source != android.widget.TimePickerClockDelegate.FROM_INPUT_PICKER) {
            updateTextInputPicker();
        }
        mDelegator.invalidate();
        if (notify) {
            onTimeChanged();
        }
    }

    /**
     *
     *
     * @return The current minute.
     */
    @java.lang.Override
    public int getMinute() {
        return mRadialTimePickerView.getCurrentMinute();
    }

    /**
     * Sets whether time is displayed in 24-hour mode or 12-hour mode with
     * AM/PM indicators.
     *
     * @param is24Hour
     * 		{@code true} to display time in 24-hour mode or
     * 		{@code false} for 12-hour mode with AM/PM
     */
    public void setIs24Hour(boolean is24Hour) {
        if (mIs24Hour != is24Hour) {
            mIs24Hour = is24Hour;
            mCurrentHour = getHour();
            updateHourFormat();
            updateUI(mRadialTimePickerView.getCurrentItemShowing());
        }
    }

    /**
     *
     *
     * @return {@code true} if time is displayed in 24-hour mode, or
    {@code false} if time is displayed in 12-hour mode with AM/PM
    indicators
     */
    @java.lang.Override
    public boolean is24Hour() {
        return mIs24Hour;
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        mHourView.setEnabled(enabled);
        mMinuteView.setEnabled(enabled);
        mAmLabel.setEnabled(enabled);
        mPmLabel.setEnabled(enabled);
        mRadialTimePickerView.setEnabled(enabled);
        mIsEnabled = enabled;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @java.lang.Override
    public int getBaseline() {
        // does not support baseline alignment
        return -1;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState(android.os.Parcelable superState) {
        return new android.widget.TimePicker.AbstractTimePickerDelegate.SavedState(superState, getHour(), getMinute(), is24Hour(), getCurrentItemShowing());
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (state instanceof android.widget.TimePicker.AbstractTimePickerDelegate.SavedState) {
            final android.widget.TimePicker.AbstractTimePickerDelegate.SavedState ss = ((android.widget.TimePicker.AbstractTimePickerDelegate.SavedState) (state));
            initialize(ss.getHour(), ss.getMinute(), ss.is24HourMode(), ss.getCurrentItemShowing());
            mRadialTimePickerView.invalidate();
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
        if (mIs24Hour) {
            flags |= android.text.format.DateUtils.FORMAT_24HOUR;
        } else {
            flags |= android.text.format.DateUtils.FORMAT_12HOUR;
        }
        mTempCalendar.set(java.util.Calendar.HOUR_OF_DAY, getHour());
        mTempCalendar.set(java.util.Calendar.MINUTE, getMinute());
        final java.lang.String selectedTime = android.text.format.DateUtils.formatDateTime(mContext, mTempCalendar.getTimeInMillis(), flags);
        final java.lang.String selectionMode = (mRadialTimePickerView.getCurrentItemShowing() == android.widget.TimePickerClockDelegate.HOUR_INDEX) ? mSelectHours : mSelectMinutes;
        event.getText().add((selectedTime + " ") + selectionMode);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getHourView() {
        return mHourView;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getMinuteView() {
        return mMinuteView;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getAmView() {
        return mAmLabel;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.TestApi
    public android.view.View getPmView() {
        return mPmLabel;
    }

    /**
     *
     *
     * @return the index of the current item showing
     */
    private int getCurrentItemShowing() {
        return mRadialTimePickerView.getCurrentItemShowing();
    }

    /**
     * Propagate the time change
     */
    private void onTimeChanged() {
        mDelegator.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnTimeChangedListener != null) {
            mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
        }
        if (mAutoFillChangeListener != null) {
            mAutoFillChangeListener.onTimeChanged(mDelegator, getHour(), getMinute());
        }
    }

    private void tryVibrate() {
        mDelegator.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK);
    }

    private void updateAmPmLabelStates(int amOrPm) {
        final boolean isAm = amOrPm == android.widget.TimePickerClockDelegate.AM;
        mAmLabel.setActivated(isAm);
        mAmLabel.setChecked(isAm);
        final boolean isPm = amOrPm == android.widget.TimePickerClockDelegate.PM;
        mPmLabel.setActivated(isPm);
        mPmLabel.setChecked(isPm);
    }

    /**
     * Converts hour-of-day (0-23) time into a localized hour number.
     * <p>
     * The localized value may be in the range (0-23), (1-24), (0-11), or
     * (1-12) depending on the locale. This method does not handle leading
     * zeroes.
     *
     * @param hourOfDay
     * 		the hour-of-day (0-23)
     * @return a localized hour number
     */
    private int getLocalizedHour(int hourOfDay) {
        if (!mIs24Hour) {
            // Convert to hour-of-am-pm.
            hourOfDay %= 12;
        }
        if ((!mHourFormatStartsAtZero) && (hourOfDay == 0)) {
            // Convert to clock-hour (either of-day or of-am-pm).
            hourOfDay = (mIs24Hour) ? 24 : 12;
        }
        return hourOfDay;
    }

    private void updateHeaderHour(int hourOfDay, boolean announce) {
        final int localizedHour = getLocalizedHour(hourOfDay);
        mHourView.setValue(localizedHour);
        if (announce) {
            tryAnnounceForAccessibility(mHourView.getText(), true);
        }
    }

    private void updateHeaderMinute(int minuteOfHour, boolean announce) {
        mMinuteView.setValue(minuteOfHour);
        if (announce) {
            tryAnnounceForAccessibility(mMinuteView.getText(), false);
        }
    }

    /**
     * The time separator is defined in the Unicode CLDR and cannot be supposed to be ":".
     *
     * See http://unicode.org/cldr/trac/browser/trunk/common/main
     *
     * We pass the correct "skeleton" depending on 12 or 24 hours view and then extract the
     * separator as the character which is just after the hour marker in the returned pattern.
     */
    private void updateHeaderSeparator() {
        final java.lang.String bestDateTimePattern = android.text.format.DateFormat.getBestDateTimePattern(mLocale, mIs24Hour ? "Hm" : "hm");
        final java.lang.String separatorText = android.widget.TimePickerClockDelegate.getHourMinSeparatorFromPattern(bestDateTimePattern);
        mSeparatorView.setText(separatorText);
        mTextInputPickerView.updateSeparator(separatorText);
    }

    /**
     * This helper method extracts the time separator from the {@code datetimePattern}.
     *
     * The time separator is defined in the Unicode CLDR and cannot be supposed to be ":".
     *
     * See http://unicode.org/cldr/trac/browser/trunk/common/main
     *
     * @return Separator string. This is the character or set of quoted characters just after the
    hour marker in {@code dateTimePattern}. Returns a colon (:) if it can't locate the
    separator.
     * @unknown 
     */
    private static java.lang.String getHourMinSeparatorFromPattern(java.lang.String dateTimePattern) {
        final java.lang.String defaultSeparator = ":";
        boolean foundHourPattern = false;
        for (int i = 0; i < dateTimePattern.length(); i++) {
            switch (dateTimePattern.charAt(i)) {
                // See http://www.unicode.org/reports/tr35/tr35-dates.html for hour formats.
                case 'H' :
                case 'h' :
                case 'K' :
                case 'k' :
                    foundHourPattern = true;
                    continue;
                case ' ' :
                    // skip spaces
                    continue;
                case '\'' :
                    if (!foundHourPattern) {
                        continue;
                    }
                    android.text.SpannableStringBuilder quotedSubstring = new android.text.SpannableStringBuilder(dateTimePattern.substring(i));
                    int quotedTextLength = android.text.format.DateFormat.appendQuotedText(quotedSubstring, 0);
                    return quotedSubstring.subSequence(0, quotedTextLength).toString();
                default :
                    if (!foundHourPattern) {
                        continue;
                    }
                    return java.lang.Character.toString(dateTimePattern.charAt(i));
            }
        }
        return defaultSeparator;
    }

    private static int lastIndexOfAny(java.lang.String str, char[] any) {
        final int lengthAny = any.length;
        if (lengthAny > 0) {
            for (int i = str.length() - 1; i >= 0; i--) {
                char c = str.charAt(i);
                for (int j = 0; j < lengthAny; j++) {
                    if (c == any[j]) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void tryAnnounceForAccessibility(java.lang.CharSequence text, boolean isHour) {
        if ((mLastAnnouncedIsHour != isHour) || (!text.equals(mLastAnnouncedText))) {
            // TODO: Find a better solution, potentially live regions?
            mDelegator.announceForAccessibility(text);
            mLastAnnouncedText = text;
            mLastAnnouncedIsHour = isHour;
        }
    }

    /**
     * Show either Hours or Minutes.
     */
    private void setCurrentItemShowing(int index, boolean animateCircle, boolean announce) {
        mRadialTimePickerView.setCurrentItemShowing(index, animateCircle);
        if (index == android.widget.TimePickerClockDelegate.HOUR_INDEX) {
            if (announce) {
                mDelegator.announceForAccessibility(mSelectHours);
            }
        } else {
            if (announce) {
                mDelegator.announceForAccessibility(mSelectMinutes);
            }
        }
        mHourView.setActivated(index == android.widget.TimePickerClockDelegate.HOUR_INDEX);
        mMinuteView.setActivated(index == android.widget.TimePickerClockDelegate.MINUTE_INDEX);
    }

    private void setAmOrPm(int amOrPm) {
        updateAmPmLabelStates(amOrPm);
        if (mRadialTimePickerView.setAmOrPm(amOrPm)) {
            mCurrentHour = getHour();
            updateTextInputPicker();
            if (mOnTimeChangedListener != null) {
                mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
            }
        }
    }

    /**
     * Listener for RadialTimePickerView interaction.
     */
    private final android.widget.RadialTimePickerView.OnValueSelectedListener mOnValueSelectedListener = new android.widget.RadialTimePickerView.OnValueSelectedListener() {
        @java.lang.Override
        public void onValueSelected(int pickerType, int newValue, boolean autoAdvance) {
            boolean valueChanged = false;
            switch (pickerType) {
                case android.widget.RadialTimePickerView.HOURS :
                    if (getHour() != newValue) {
                        valueChanged = true;
                    }
                    final boolean isTransition = mAllowAutoAdvance && autoAdvance;
                    setHourInternal(newValue, android.widget.TimePickerClockDelegate.FROM_RADIAL_PICKER, !isTransition, true);
                    if (isTransition) {
                        setCurrentItemShowing(android.widget.TimePickerClockDelegate.MINUTE_INDEX, true, false);
                        final int localizedHour = getLocalizedHour(newValue);
                        mDelegator.announceForAccessibility((localizedHour + ". ") + mSelectMinutes);
                    }
                    break;
                case android.widget.RadialTimePickerView.MINUTES :
                    if (getMinute() != newValue) {
                        valueChanged = true;
                    }
                    setMinuteInternal(newValue, android.widget.TimePickerClockDelegate.FROM_RADIAL_PICKER, true);
                    break;
            }
            if ((mOnTimeChangedListener != null) && valueChanged) {
                mOnTimeChangedListener.onTimeChanged(mDelegator, getHour(), getMinute());
            }
        }
    };

    private final android.widget.TextInputTimePickerView.OnValueTypedListener mOnValueTypedListener = new android.widget.TextInputTimePickerView.OnValueTypedListener() {
        @java.lang.Override
        public void onValueChanged(int pickerType, int newValue) {
            switch (pickerType) {
                case android.widget.TextInputTimePickerView.HOURS :
                    setHourInternal(newValue, android.widget.TimePickerClockDelegate.FROM_INPUT_PICKER, false, true);
                    break;
                case android.widget.TextInputTimePickerView.MINUTES :
                    setMinuteInternal(newValue, android.widget.TimePickerClockDelegate.FROM_INPUT_PICKER, true);
                    break;
                case android.widget.TextInputTimePickerView.AMPM :
                    setAmOrPm(newValue);
                    break;
            }
        }
    };

    /**
     * Listener for keyboard interaction.
     */
    private final com.android.internal.widget.NumericTextView.OnValueChangedListener mDigitEnteredListener = new com.android.internal.widget.NumericTextView.OnValueChangedListener() {
        @java.lang.Override
        public void onValueChanged(com.android.internal.widget.NumericTextView view, int value, boolean isValid, boolean isFinished) {
            final java.lang.Runnable commitCallback;
            final android.view.View nextFocusTarget;
            if (view == mHourView) {
                commitCallback = mCommitHour;
                nextFocusTarget = (view.isFocused()) ? mMinuteView : null;
            } else
                if (view == mMinuteView) {
                    commitCallback = mCommitMinute;
                    nextFocusTarget = null;
                } else {
                    return;
                }

            view.removeCallbacks(commitCallback);
            if (isValid) {
                if (isFinished) {
                    // Done with hours entry, make visual updates
                    // immediately and move to next focus if needed.
                    commitCallback.run();
                    if (nextFocusTarget != null) {
                        nextFocusTarget.requestFocus();
                    }
                } else {
                    // May still be making changes. Postpone visual
                    // updates to prevent distracting the user.
                    view.postDelayed(commitCallback, android.widget.TimePickerClockDelegate.DELAY_COMMIT_MILLIS);
                }
            }
        }
    };

    private final java.lang.Runnable mCommitHour = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            setHour(mHourView.getValue());
        }
    };

    private final java.lang.Runnable mCommitMinute = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            setMinute(mMinuteView.getValue());
        }
    };

    private final android.view.View.OnFocusChangeListener mFocusListener = new android.view.View.OnFocusChangeListener() {
        @java.lang.Override
        public void onFocusChange(android.view.View v, boolean focused) {
            if (focused) {
                switch (v.getId()) {
                    case R.id.am_label :
                        setAmOrPm(android.widget.TimePickerClockDelegate.AM);
                        break;
                    case R.id.pm_label :
                        setAmOrPm(android.widget.TimePickerClockDelegate.PM);
                        break;
                    case R.id.hours :
                        setCurrentItemShowing(android.widget.TimePickerClockDelegate.HOUR_INDEX, true, true);
                        break;
                    case R.id.minutes :
                        setCurrentItemShowing(android.widget.TimePickerClockDelegate.MINUTE_INDEX, true, true);
                        break;
                    default :
                        // Failed to handle this click, don't vibrate.
                        return;
                }
                tryVibrate();
            }
        }
    };

    private final android.view.View.OnClickListener mClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            final int amOrPm;
            switch (v.getId()) {
                case R.id.am_label :
                    setAmOrPm(android.widget.TimePickerClockDelegate.AM);
                    break;
                case R.id.pm_label :
                    setAmOrPm(android.widget.TimePickerClockDelegate.PM);
                    break;
                case R.id.hours :
                    setCurrentItemShowing(android.widget.TimePickerClockDelegate.HOUR_INDEX, true, true);
                    break;
                case R.id.minutes :
                    setCurrentItemShowing(android.widget.TimePickerClockDelegate.MINUTE_INDEX, true, true);
                    break;
                default :
                    // Failed to handle this click, don't vibrate.
                    return;
            }
            tryVibrate();
        }
    };

    /**
     * Delegates unhandled touches in a view group to the nearest child view.
     */
    private static class NearestTouchDelegate implements android.view.View.OnTouchListener {
        private android.view.View mInitialTouchTarget;

        @java.lang.Override
        public boolean onTouch(android.view.View view, android.view.MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == android.view.MotionEvent.ACTION_DOWN) {
                if (view instanceof android.view.ViewGroup) {
                    mInitialTouchTarget = findNearestChild(((android.view.ViewGroup) (view)), ((int) (motionEvent.getX())), ((int) (motionEvent.getY())));
                } else {
                    mInitialTouchTarget = null;
                }
            }
            final android.view.View child = mInitialTouchTarget;
            if (child == null) {
                return false;
            }
            final float offsetX = view.getScrollX() - child.getLeft();
            final float offsetY = view.getScrollY() - child.getTop();
            motionEvent.offsetLocation(offsetX, offsetY);
            final boolean handled = child.dispatchTouchEvent(motionEvent);
            motionEvent.offsetLocation(-offsetX, -offsetY);
            if ((actionMasked == android.view.MotionEvent.ACTION_UP) || (actionMasked == android.view.MotionEvent.ACTION_CANCEL)) {
                mInitialTouchTarget = null;
            }
            return handled;
        }

        private android.view.View findNearestChild(android.view.ViewGroup v, int x, int y) {
            android.view.View bestChild = null;
            int bestDist = java.lang.Integer.MAX_VALUE;
            for (int i = 0, count = v.getChildCount(); i < count; i++) {
                final android.view.View child = v.getChildAt(i);
                final int dX = x - (child.getLeft() + (child.getWidth() / 2));
                final int dY = y - (child.getTop() + (child.getHeight() / 2));
                final int dist = (dX * dX) + (dY * dY);
                if (bestDist > dist) {
                    bestChild = child;
                    bestDist = dist;
                }
            }
            return bestChild;
        }
    }
}

