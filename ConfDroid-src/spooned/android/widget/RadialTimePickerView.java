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
 * View to show a clock circle picker (with one or two picking circles)
 *
 * @unknown 
 */
public class RadialTimePickerView extends android.view.View {
    private static final java.lang.String TAG = "RadialTimePickerView";

    public static final int HOURS = 0;

    public static final int MINUTES = 1;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.widget.RadialTimePickerView.HOURS, android.widget.RadialTimePickerView.MINUTES })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PickerType {}

    private static final int HOURS_INNER = 2;

    private static final int SELECTOR_CIRCLE = 0;

    private static final int SELECTOR_DOT = 1;

    private static final int SELECTOR_LINE = 2;

    private static final int AM = 0;

    private static final int PM = 1;

    private static final int HOURS_IN_CIRCLE = 12;

    private static final int MINUTES_IN_CIRCLE = 60;

    private static final int DEGREES_FOR_ONE_HOUR = 360 / android.widget.RadialTimePickerView.HOURS_IN_CIRCLE;

    private static final int DEGREES_FOR_ONE_MINUTE = 360 / android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE;

    private static final int[] HOURS_NUMBERS = new int[]{ 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

    private static final int[] HOURS_NUMBERS_24 = new int[]{ 0, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };

    private static final int[] MINUTES_NUMBERS = new int[]{ 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 };

    private static final int ANIM_DURATION_NORMAL = 500;

    private static final int ANIM_DURATION_TOUCH = 60;

    private static final int[] SNAP_PREFER_30S_MAP = new int[361];

    private static final int NUM_POSITIONS = 12;

    private static final float[] COS_30 = new float[android.widget.RadialTimePickerView.NUM_POSITIONS];

    private static final float[] SIN_30 = new float[android.widget.RadialTimePickerView.NUM_POSITIONS];

    /**
     * "Something is wrong" color used when a color attribute is missing.
     */
    private static final int MISSING_COLOR = android.graphics.Color.MAGENTA;

    static {
        // Prepare mapping to snap touchable degrees to selectable degrees.
        android.widget.RadialTimePickerView.preparePrefer30sMap();
        final double increment = (2.0 * java.lang.Math.PI) / android.widget.RadialTimePickerView.NUM_POSITIONS;
        double angle = java.lang.Math.PI / 2.0;
        for (int i = 0; i < android.widget.RadialTimePickerView.NUM_POSITIONS; i++) {
            android.widget.RadialTimePickerView.COS_30[i] = ((float) (java.lang.Math.cos(angle)));
            android.widget.RadialTimePickerView.SIN_30[i] = ((float) (java.lang.Math.sin(angle)));
            angle += increment;
        }
    }

    private final android.util.FloatProperty<android.widget.RadialTimePickerView> HOURS_TO_MINUTES = new android.util.FloatProperty<android.widget.RadialTimePickerView>("hoursToMinutes") {
        @java.lang.Override
        public java.lang.Float get(android.widget.RadialTimePickerView radialTimePickerView) {
            return radialTimePickerView.mHoursToMinutes;
        }

        @java.lang.Override
        public void setValue(android.widget.RadialTimePickerView object, float value) {
            object.mHoursToMinutes = value;
            object.invalidate();
        }
    };

    private final java.lang.String[] mHours12Texts = new java.lang.String[12];

    private final java.lang.String[] mOuterHours24Texts = new java.lang.String[12];

    private final java.lang.String[] mInnerHours24Texts = new java.lang.String[12];

    private final java.lang.String[] mMinutesTexts = new java.lang.String[12];

    private final android.graphics.Paint[] mPaint = new android.graphics.Paint[2];

    private final android.graphics.Paint mPaintCenter = new android.graphics.Paint();

    private final android.graphics.Paint[] mPaintSelector = new android.graphics.Paint[3];

    private final android.graphics.Paint mPaintBackground = new android.graphics.Paint();

    private final android.graphics.Typeface mTypeface;

    private final android.content.res.ColorStateList[] mTextColor = new android.content.res.ColorStateList[3];

    private final int[] mTextSize = new int[3];

    private final int[] mTextInset = new int[3];

    private final float[][] mOuterTextX = new float[2][12];

    private final float[][] mOuterTextY = new float[2][12];

    private final float[] mInnerTextX = new float[12];

    private final float[] mInnerTextY = new float[12];

    private final int[] mSelectionDegrees = new int[2];

    private final android.widget.RadialTimePickerView.RadialPickerTouchHelper mTouchHelper;

    private final android.graphics.Path mSelectorPath = new android.graphics.Path();

    private boolean mIs24HourMode;

    private boolean mShowHours;

    private android.animation.ObjectAnimator mHoursToMinutesAnimator;

    private float mHoursToMinutes;

    /**
     * When in 24-hour mode, indicates that the current hour is between
     * 1 and 12 (inclusive).
     */
    private boolean mIsOnInnerCircle;

    private int mSelectorRadius;

    private int mSelectorStroke;

    private int mSelectorDotRadius;

    private int mCenterDotRadius;

    private int mSelectorColor;

    private int mSelectorDotColor;

    private int mXCenter;

    private int mYCenter;

    private int mCircleRadius;

    private int mMinDistForInnerNumber;

    private int mMaxDistForOuterNumber;

    private int mHalfwayDist;

    private java.lang.String[] mOuterTextHours;

    private java.lang.String[] mInnerTextHours;

    private java.lang.String[] mMinutesText;

    private int mAmOrPm;

    private float mDisabledAlpha;

    private android.widget.RadialTimePickerView.OnValueSelectedListener mListener;

    private boolean mInputEnabled = true;

    interface OnValueSelectedListener {
        /**
         * Called when the selected value at a given picker index has changed.
         *
         * @param pickerType
         * 		the type of value that has changed, one of:
         * 		<ul>
         * 		<li>{@link #MINUTES}
         * 		<li>{@link #HOURS}
         * 		</ul>
         * @param newValue
         * 		the new value as minute in hour (0-59) or hour in
         * 		day (0-23)
         * @param autoAdvance
         * 		when the picker type is {@link #HOURS},
         * 		{@code true} to switch to the {@link #MINUTES}
         * 		picker or {@code false} to stay on the current
         * 		picker. No effect when picker type is
         * 		{@link #MINUTES}.
         */
        void onValueSelected(@android.widget.RadialTimePickerView.PickerType
        int pickerType, int newValue, boolean autoAdvance);
    }

    /**
     * Split up the 360 degrees of the circle among the 60 selectable values. Assigns a larger
     * selectable area to each of the 12 visible values, such that the ratio of space apportioned
     * to a visible value : space apportioned to a non-visible value will be 14 : 4.
     * E.g. the output of 30 degrees should have a higher range of input associated with it than
     * the output of 24 degrees, because 30 degrees corresponds to a visible number on the clock
     * circle (5 on the minutes, 1 or 13 on the hours).
     */
    private static void preparePrefer30sMap() {
        // We'll split up the visible output and the non-visible output such that each visible
        // output will correspond to a range of 14 associated input degrees, and each non-visible
        // output will correspond to a range of 4 associate input degrees, so visible numbers
        // are more than 3 times easier to get than non-visible numbers:
        // {354-359,0-7}:0, {8-11}:6, {12-15}:12, {16-19}:18, {20-23}:24, {24-37}:30, etc.
        // 
        // If an output of 30 degrees should correspond to a range of 14 associated degrees, then
        // we'll need any input between 24 - 37 to snap to 30. Working out from there, 20-23 should
        // snap to 24, while 38-41 should snap to 36. This is somewhat counter-intuitive, that you
        // can be touching 36 degrees but have the selection snapped to 30 degrees; however, this
        // inconsistency isn't noticeable at such fine-grained degrees, and it affords us the
        // ability to aggressively prefer the visible values by a factor of more than 3:1, which
        // greatly contributes to the selectability of these values.
        // The first output is 0, and each following output will increment by 6 {0, 6, 12, ...}.
        int snappedOutputDegrees = 0;
        // Count of how many inputs we've designated to the specified output.
        int count = 1;
        // How many input we expect for a specified output. This will be 14 for output divisible
        // by 30, and 4 for the remaining output. We'll special case the outputs of 0 and 360, so
        // the caller can decide which they need.
        int expectedCount = 8;
        // Iterate through the input.
        for (int degrees = 0; degrees < 361; degrees++) {
            // Save the input-output mapping.
            android.widget.RadialTimePickerView.SNAP_PREFER_30S_MAP[degrees] = snappedOutputDegrees;
            // If this is the last input for the specified output, calculate the next output and
            // the next expected count.
            if (count == expectedCount) {
                snappedOutputDegrees += 6;
                if (snappedOutputDegrees == 360) {
                    expectedCount = 7;
                } else
                    if ((snappedOutputDegrees % 30) == 0) {
                        expectedCount = 14;
                    } else {
                        expectedCount = 4;
                    }

                count = 1;
            } else {
                count++;
            }
        }
    }

    /**
     * Returns mapping of any input degrees (0 to 360) to one of 60 selectable output degrees,
     * where the degrees corresponding to visible numbers (i.e. those divisible by 30) will be
     * weighted heavier than the degrees corresponding to non-visible numbers.
     * See {@link #preparePrefer30sMap()} documentation for the rationale and generation of the
     * mapping.
     */
    private static int snapPrefer30s(int degrees) {
        if (android.widget.RadialTimePickerView.SNAP_PREFER_30S_MAP == null) {
            return -1;
        }
        return android.widget.RadialTimePickerView.SNAP_PREFER_30S_MAP[degrees];
    }

    /**
     * Returns mapping of any input degrees (0 to 360) to one of 12 visible output degrees (all
     * multiples of 30), where the input will be "snapped" to the closest visible degrees.
     *
     * @param degrees
     * 		The input degrees
     * @param forceHigherOrLower
     * 		The output may be forced to either the higher or lower step, or may
     * 		be allowed to snap to whichever is closer. Use 1 to force strictly higher, -1 to force
     * 		strictly lower, and 0 to snap to the closer one.
     * @return output degrees, will be a multiple of 30
     */
    private static int snapOnly30s(int degrees, int forceHigherOrLower) {
        final int stepSize = android.widget.RadialTimePickerView.DEGREES_FOR_ONE_HOUR;
        int floor = (degrees / stepSize) * stepSize;
        final int ceiling = floor + stepSize;
        if (forceHigherOrLower == 1) {
            degrees = ceiling;
        } else
            if (forceHigherOrLower == (-1)) {
                if (degrees == floor) {
                    floor -= stepSize;
                }
                degrees = floor;
            } else {
                if ((degrees - floor) < (ceiling - degrees)) {
                    degrees = floor;
                } else {
                    degrees = ceiling;
                }
            }

        return degrees;
    }

    @java.lang.SuppressWarnings("unused")
    public RadialTimePickerView(android.content.Context context) {
        this(context, null);
    }

    public RadialTimePickerView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.timePickerStyle);
    }

    public RadialTimePickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadialTimePickerView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        applyAttributes(attrs, defStyleAttr, defStyleRes);
        // Pull disabled alpha from theme.
        final android.util.TypedValue outValue = new android.util.TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, outValue, true);
        mDisabledAlpha = outValue.getFloat();
        mTypeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL);
        mPaint[android.widget.RadialTimePickerView.HOURS] = new android.graphics.Paint();
        mPaint[android.widget.RadialTimePickerView.HOURS].setAntiAlias(true);
        mPaint[android.widget.RadialTimePickerView.HOURS].setTextAlign(android.graphics.Paint.Align.CENTER);
        mPaint[android.widget.RadialTimePickerView.MINUTES] = new android.graphics.Paint();
        mPaint[android.widget.RadialTimePickerView.MINUTES].setAntiAlias(true);
        mPaint[android.widget.RadialTimePickerView.MINUTES].setTextAlign(android.graphics.Paint.Align.CENTER);
        mPaintCenter.setAntiAlias(true);
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_CIRCLE] = new android.graphics.Paint();
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_CIRCLE].setAntiAlias(true);
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_DOT] = new android.graphics.Paint();
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_DOT].setAntiAlias(true);
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_LINE] = new android.graphics.Paint();
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_LINE].setAntiAlias(true);
        mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_LINE].setStrokeWidth(2);
        mPaintBackground.setAntiAlias(true);
        final android.content.res.Resources res = getResources();
        mSelectorRadius = res.getDimensionPixelSize(R.dimen.timepicker_selector_radius);
        mSelectorStroke = res.getDimensionPixelSize(R.dimen.timepicker_selector_stroke);
        mSelectorDotRadius = res.getDimensionPixelSize(R.dimen.timepicker_selector_dot_radius);
        mCenterDotRadius = res.getDimensionPixelSize(R.dimen.timepicker_center_dot_radius);
        mTextSize[android.widget.RadialTimePickerView.HOURS] = res.getDimensionPixelSize(R.dimen.timepicker_text_size_normal);
        mTextSize[android.widget.RadialTimePickerView.MINUTES] = res.getDimensionPixelSize(R.dimen.timepicker_text_size_normal);
        mTextSize[android.widget.RadialTimePickerView.HOURS_INNER] = res.getDimensionPixelSize(R.dimen.timepicker_text_size_inner);
        mTextInset[android.widget.RadialTimePickerView.HOURS] = res.getDimensionPixelSize(R.dimen.timepicker_text_inset_normal);
        mTextInset[android.widget.RadialTimePickerView.MINUTES] = res.getDimensionPixelSize(R.dimen.timepicker_text_inset_normal);
        mTextInset[android.widget.RadialTimePickerView.HOURS_INNER] = res.getDimensionPixelSize(R.dimen.timepicker_text_inset_inner);
        mShowHours = true;
        mHoursToMinutes = android.widget.RadialTimePickerView.HOURS;
        mIs24HourMode = false;
        mAmOrPm = android.widget.RadialTimePickerView.AM;
        // Set up accessibility components.
        mTouchHelper = new android.widget.RadialTimePickerView.RadialPickerTouchHelper();
        setAccessibilityDelegate(mTouchHelper);
        if (getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        initHoursAndMinutesText();
        initData();
        // Initial values
        final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.Locale.getDefault());
        final int currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(java.util.Calendar.MINUTE);
        setCurrentHourInternal(currentHour, false, false);
        setCurrentMinuteInternal(currentMinute, false);
        setHapticFeedbackEnabled(true);
    }

    void applyAttributes(android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final android.content.Context context = getContext();
        final android.content.res.TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.TimePicker, attrs, a, defStyleAttr, defStyleRes);
        final android.content.res.ColorStateList numbersTextColor = a.getColorStateList(R.styleable.TimePicker_numbersTextColor);
        final android.content.res.ColorStateList numbersInnerTextColor = a.getColorStateList(R.styleable.TimePicker_numbersInnerTextColor);
        mTextColor[android.widget.RadialTimePickerView.HOURS] = (numbersTextColor == null) ? android.content.res.ColorStateList.valueOf(android.widget.RadialTimePickerView.MISSING_COLOR) : numbersTextColor;
        mTextColor[android.widget.RadialTimePickerView.HOURS_INNER] = (numbersInnerTextColor == null) ? android.content.res.ColorStateList.valueOf(android.widget.RadialTimePickerView.MISSING_COLOR) : numbersInnerTextColor;
        mTextColor[android.widget.RadialTimePickerView.MINUTES] = mTextColor[android.widget.RadialTimePickerView.HOURS];
        // Set up various colors derived from the selector "activated" state.
        final android.content.res.ColorStateList selectorColors = a.getColorStateList(R.styleable.TimePicker_numbersSelectorColor);
        final int selectorActivatedColor;
        if (selectorColors != null) {
            final int[] stateSetEnabledActivated = android.util.StateSet.get(android.util.StateSet.VIEW_STATE_ENABLED | android.util.StateSet.VIEW_STATE_ACTIVATED);
            selectorActivatedColor = selectorColors.getColorForState(stateSetEnabledActivated, 0);
        } else {
            selectorActivatedColor = android.widget.RadialTimePickerView.MISSING_COLOR;
        }
        mPaintCenter.setColor(selectorActivatedColor);
        final int[] stateSetActivated = android.util.StateSet.get(android.util.StateSet.VIEW_STATE_ENABLED | android.util.StateSet.VIEW_STATE_ACTIVATED);
        mSelectorColor = selectorActivatedColor;
        mSelectorDotColor = mTextColor[android.widget.RadialTimePickerView.HOURS].getColorForState(stateSetActivated, 0);
        mPaintBackground.setColor(a.getColor(R.styleable.TimePicker_numbersBackgroundColor, context.getColor(R.color.timepicker_default_numbers_background_color_material)));
        a.recycle();
    }

    public void initialize(int hour, int minute, boolean is24HourMode) {
        if (mIs24HourMode != is24HourMode) {
            mIs24HourMode = is24HourMode;
            initData();
        }
        setCurrentHourInternal(hour, false, false);
        setCurrentMinuteInternal(minute, false);
    }

    public void setCurrentItemShowing(int item, boolean animate) {
        switch (item) {
            case android.widget.RadialTimePickerView.HOURS :
                showHours(animate);
                break;
            case android.widget.RadialTimePickerView.MINUTES :
                showMinutes(animate);
                break;
            default :
                android.util.Log.e(android.widget.RadialTimePickerView.TAG, "ClockView does not support showing item " + item);
        }
    }

    public int getCurrentItemShowing() {
        return mShowHours ? android.widget.RadialTimePickerView.HOURS : android.widget.RadialTimePickerView.MINUTES;
    }

    public void setOnValueSelectedListener(android.widget.RadialTimePickerView.OnValueSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Sets the current hour in 24-hour time.
     *
     * @param hour
     * 		the current hour between 0 and 23 (inclusive)
     */
    public void setCurrentHour(int hour) {
        setCurrentHourInternal(hour, true, false);
    }

    /**
     * Sets the current hour.
     *
     * @param hour
     * 		The current hour
     * @param callback
     * 		Whether the value listener should be invoked
     * @param autoAdvance
     * 		Whether the listener should auto-advance to the next
     * 		selection mode, e.g. hour to minutes
     */
    private void setCurrentHourInternal(int hour, boolean callback, boolean autoAdvance) {
        final int degrees = (hour % 12) * android.widget.RadialTimePickerView.DEGREES_FOR_ONE_HOUR;
        mSelectionDegrees[android.widget.RadialTimePickerView.HOURS] = degrees;
        // 0 is 12 AM (midnight) and 12 is 12 PM (noon).
        final int amOrPm = ((hour == 0) || ((hour % 24) < 12)) ? android.widget.RadialTimePickerView.AM : android.widget.RadialTimePickerView.PM;
        final boolean isOnInnerCircle = getInnerCircleForHour(hour);
        if ((mAmOrPm != amOrPm) || (mIsOnInnerCircle != isOnInnerCircle)) {
            mAmOrPm = amOrPm;
            mIsOnInnerCircle = isOnInnerCircle;
            initData();
            mTouchHelper.invalidateRoot();
        }
        invalidate();
        if (callback && (mListener != null)) {
            mListener.onValueSelected(android.widget.RadialTimePickerView.HOURS, hour, autoAdvance);
        }
    }

    /**
     * Returns the current hour in 24-hour time.
     *
     * @return the current hour between 0 and 23 (inclusive)
     */
    public int getCurrentHour() {
        return getHourForDegrees(mSelectionDegrees[android.widget.RadialTimePickerView.HOURS], mIsOnInnerCircle);
    }

    private int getHourForDegrees(int degrees, boolean innerCircle) {
        int hour = (degrees / android.widget.RadialTimePickerView.DEGREES_FOR_ONE_HOUR) % 12;
        if (mIs24HourMode) {
            // Convert the 12-hour value into 24-hour time based on where the
            // selector is positioned.
            if ((!innerCircle) && (hour == 0)) {
                // Outer circle is 1 through 12.
                hour = 12;
            } else
                if (innerCircle && (hour != 0)) {
                    // Inner circle is 13 through 23 and 0.
                    hour += 12;
                }

        } else
            if (mAmOrPm == android.widget.RadialTimePickerView.PM) {
                hour += 12;
            }

        return hour;
    }

    /**
     *
     *
     * @param hour
     * 		the hour in 24-hour time or 12-hour time
     */
    private int getDegreesForHour(int hour) {
        // Convert to be 0-11.
        if (mIs24HourMode) {
            if (hour >= 12) {
                hour -= 12;
            }
        } else
            if (hour == 12) {
                hour = 0;
            }

        return hour * android.widget.RadialTimePickerView.DEGREES_FOR_ONE_HOUR;
    }

    /**
     *
     *
     * @param hour
     * 		the hour in 24-hour time or 12-hour time
     */
    private boolean getInnerCircleForHour(int hour) {
        return mIs24HourMode && ((hour == 0) || (hour > 12));
    }

    public void setCurrentMinute(int minute) {
        setCurrentMinuteInternal(minute, true);
    }

    private void setCurrentMinuteInternal(int minute, boolean callback) {
        mSelectionDegrees[android.widget.RadialTimePickerView.MINUTES] = (minute % android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE) * android.widget.RadialTimePickerView.DEGREES_FOR_ONE_MINUTE;
        invalidate();
        if (callback && (mListener != null)) {
            mListener.onValueSelected(android.widget.RadialTimePickerView.MINUTES, minute, false);
        }
    }

    // Returns minutes in 0-59 range
    public int getCurrentMinute() {
        return getMinuteForDegrees(mSelectionDegrees[android.widget.RadialTimePickerView.MINUTES]);
    }

    private int getMinuteForDegrees(int degrees) {
        return degrees / android.widget.RadialTimePickerView.DEGREES_FOR_ONE_MINUTE;
    }

    private int getDegreesForMinute(int minute) {
        return minute * android.widget.RadialTimePickerView.DEGREES_FOR_ONE_MINUTE;
    }

    /**
     * Sets whether the picker is showing AM or PM hours. Has no effect when
     * in 24-hour mode.
     *
     * @param amOrPm
     * 		{@link #AM} or {@link #PM}
     * @return {@code true} if the value changed from what was previously set,
    or {@code false} otherwise
     */
    public boolean setAmOrPm(int amOrPm) {
        if ((mAmOrPm == amOrPm) || mIs24HourMode) {
            return false;
        }
        mAmOrPm = amOrPm;
        invalidate();
        mTouchHelper.invalidateRoot();
        return true;
    }

    public int getAmOrPm() {
        return mAmOrPm;
    }

    public void showHours(boolean animate) {
        showPicker(true, animate);
    }

    public void showMinutes(boolean animate) {
        showPicker(false, animate);
    }

    private void initHoursAndMinutesText() {
        // Initialize the hours and minutes numbers.
        for (int i = 0; i < 12; i++) {
            mHours12Texts[i] = java.lang.String.format("%d", android.widget.RadialTimePickerView.HOURS_NUMBERS[i]);
            mInnerHours24Texts[i] = java.lang.String.format("%02d", android.widget.RadialTimePickerView.HOURS_NUMBERS_24[i]);
            mOuterHours24Texts[i] = java.lang.String.format("%d", android.widget.RadialTimePickerView.HOURS_NUMBERS[i]);
            mMinutesTexts[i] = java.lang.String.format("%02d", android.widget.RadialTimePickerView.MINUTES_NUMBERS[i]);
        }
    }

    private void initData() {
        if (mIs24HourMode) {
            mOuterTextHours = mOuterHours24Texts;
            mInnerTextHours = mInnerHours24Texts;
        } else {
            mOuterTextHours = mHours12Texts;
            mInnerTextHours = mHours12Texts;
        }
        mMinutesText = mMinutesTexts;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        mCircleRadius = java.lang.Math.min(mXCenter, mYCenter);
        mMinDistForInnerNumber = (mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS_INNER]) - mSelectorRadius;
        mMaxDistForOuterNumber = (mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS]) + mSelectorRadius;
        mHalfwayDist = mCircleRadius - ((mTextInset[android.widget.RadialTimePickerView.HOURS] + mTextInset[android.widget.RadialTimePickerView.HOURS_INNER]) / 2);
        calculatePositionsHours();
        calculatePositionsMinutes();
        mTouchHelper.invalidateRoot();
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas canvas) {
        final float alphaMod = (mInputEnabled) ? 1 : mDisabledAlpha;
        drawCircleBackground(canvas);
        final android.graphics.Path selectorPath = mSelectorPath;
        drawSelector(canvas, selectorPath);
        drawHours(canvas, selectorPath, alphaMod);
        drawMinutes(canvas, selectorPath, alphaMod);
        drawCenter(canvas, alphaMod);
    }

    private void showPicker(boolean hours, boolean animate) {
        if (mShowHours == hours) {
            return;
        }
        mShowHours = hours;
        if (animate) {
            animatePicker(hours, android.widget.RadialTimePickerView.ANIM_DURATION_NORMAL);
        } else {
            // If we have a pending or running animator, cancel it.
            if ((mHoursToMinutesAnimator != null) && mHoursToMinutesAnimator.isStarted()) {
                mHoursToMinutesAnimator.cancel();
                mHoursToMinutesAnimator = null;
            }
            mHoursToMinutes = (hours) ? 0.0F : 1.0F;
        }
        initData();
        invalidate();
        mTouchHelper.invalidateRoot();
    }

    private void animatePicker(boolean hoursToMinutes, long duration) {
        final float target = (hoursToMinutes) ? android.widget.RadialTimePickerView.HOURS : android.widget.RadialTimePickerView.MINUTES;
        if (mHoursToMinutes == target) {
            // If we have a pending or running animator, cancel it.
            if ((mHoursToMinutesAnimator != null) && mHoursToMinutesAnimator.isStarted()) {
                mHoursToMinutesAnimator.cancel();
                mHoursToMinutesAnimator = null;
            }
            // We're already showing the correct picker.
            return;
        }
        mHoursToMinutesAnimator = android.animation.ObjectAnimator.ofFloat(this, HOURS_TO_MINUTES, target);
        mHoursToMinutesAnimator.setAutoCancel(true);
        mHoursToMinutesAnimator.setDuration(duration);
        mHoursToMinutesAnimator.start();
    }

    private void drawCircleBackground(android.graphics.Canvas canvas) {
        canvas.drawCircle(mXCenter, mYCenter, mCircleRadius, mPaintBackground);
    }

    private void drawHours(android.graphics.Canvas canvas, android.graphics.Path selectorPath, float alphaMod) {
        final int hoursAlpha = ((int) (((255.0F * (1.0F - mHoursToMinutes)) * alphaMod) + 0.5F));
        if (hoursAlpha > 0) {
            // Exclude the selector region, then draw inner/outer hours with no
            // activated states.
            canvas.save(android.graphics.Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(selectorPath, android.graphics.Region.Op.DIFFERENCE);
            drawHoursClipped(canvas, hoursAlpha, false);
            canvas.restore();
            // Intersect the selector region, then draw minutes with only
            // activated states.
            canvas.save(android.graphics.Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(selectorPath, android.graphics.Region.Op.INTERSECT);
            drawHoursClipped(canvas, hoursAlpha, true);
            canvas.restore();
        }
    }

    private void drawHoursClipped(android.graphics.Canvas canvas, int hoursAlpha, boolean showActivated) {
        // Draw outer hours.
        drawTextElements(canvas, mTextSize[android.widget.RadialTimePickerView.HOURS], mTypeface, mTextColor[android.widget.RadialTimePickerView.HOURS], mOuterTextHours, mOuterTextX[android.widget.RadialTimePickerView.HOURS], mOuterTextY[android.widget.RadialTimePickerView.HOURS], mPaint[android.widget.RadialTimePickerView.HOURS], hoursAlpha, showActivated && (!mIsOnInnerCircle), mSelectionDegrees[android.widget.RadialTimePickerView.HOURS], showActivated);
        // Draw inner hours (13-00) for 24-hour time.
        if (mIs24HourMode && (mInnerTextHours != null)) {
            drawTextElements(canvas, mTextSize[android.widget.RadialTimePickerView.HOURS_INNER], mTypeface, mTextColor[android.widget.RadialTimePickerView.HOURS_INNER], mInnerTextHours, mInnerTextX, mInnerTextY, mPaint[android.widget.RadialTimePickerView.HOURS], hoursAlpha, showActivated && mIsOnInnerCircle, mSelectionDegrees[android.widget.RadialTimePickerView.HOURS], showActivated);
        }
    }

    private void drawMinutes(android.graphics.Canvas canvas, android.graphics.Path selectorPath, float alphaMod) {
        final int minutesAlpha = ((int) (((255.0F * mHoursToMinutes) * alphaMod) + 0.5F));
        if (minutesAlpha > 0) {
            // Exclude the selector region, then draw minutes with no
            // activated states.
            canvas.save(android.graphics.Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(selectorPath, android.graphics.Region.Op.DIFFERENCE);
            drawMinutesClipped(canvas, minutesAlpha, false);
            canvas.restore();
            // Intersect the selector region, then draw minutes with only
            // activated states.
            canvas.save(android.graphics.Canvas.CLIP_SAVE_FLAG);
            canvas.clipPath(selectorPath, android.graphics.Region.Op.INTERSECT);
            drawMinutesClipped(canvas, minutesAlpha, true);
            canvas.restore();
        }
    }

    private void drawMinutesClipped(android.graphics.Canvas canvas, int minutesAlpha, boolean showActivated) {
        drawTextElements(canvas, mTextSize[android.widget.RadialTimePickerView.MINUTES], mTypeface, mTextColor[android.widget.RadialTimePickerView.MINUTES], mMinutesText, mOuterTextX[android.widget.RadialTimePickerView.MINUTES], mOuterTextY[android.widget.RadialTimePickerView.MINUTES], mPaint[android.widget.RadialTimePickerView.MINUTES], minutesAlpha, showActivated, mSelectionDegrees[android.widget.RadialTimePickerView.MINUTES], showActivated);
    }

    private void drawCenter(android.graphics.Canvas canvas, float alphaMod) {
        mPaintCenter.setAlpha(((int) ((255 * alphaMod) + 0.5F)));
        canvas.drawCircle(mXCenter, mYCenter, mCenterDotRadius, mPaintCenter);
    }

    private int getMultipliedAlpha(int argb, int alpha) {
        return ((int) ((android.graphics.Color.alpha(argb) * (alpha / 255.0)) + 0.5));
    }

    private void drawSelector(android.graphics.Canvas canvas, android.graphics.Path selectorPath) {
        // Determine the current length, angle, and dot scaling factor.
        final int hoursIndex = (mIsOnInnerCircle) ? android.widget.RadialTimePickerView.HOURS_INNER : android.widget.RadialTimePickerView.HOURS;
        final int hoursInset = mTextInset[hoursIndex];
        final int hoursAngleDeg = mSelectionDegrees[hoursIndex % 2];
        final float hoursDotScale = ((mSelectionDegrees[hoursIndex % 2] % 30) != 0) ? 1 : 0;
        final int minutesIndex = android.widget.RadialTimePickerView.MINUTES;
        final int minutesInset = mTextInset[minutesIndex];
        final int minutesAngleDeg = mSelectionDegrees[minutesIndex];
        final float minutesDotScale = ((mSelectionDegrees[minutesIndex] % 30) != 0) ? 1 : 0;
        // Calculate the current radius at which to place the selection circle.
        final int selRadius = mSelectorRadius;
        final float selLength = mCircleRadius - android.util.MathUtils.lerp(hoursInset, minutesInset, mHoursToMinutes);
        final double selAngleRad = java.lang.Math.toRadians(android.util.MathUtils.lerpDeg(hoursAngleDeg, minutesAngleDeg, mHoursToMinutes));
        final float selCenterX = mXCenter + (selLength * ((float) (java.lang.Math.sin(selAngleRad))));
        final float selCenterY = mYCenter - (selLength * ((float) (java.lang.Math.cos(selAngleRad))));
        // Draw the selection circle.
        final android.graphics.Paint paint = mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_CIRCLE];
        paint.setColor(mSelectorColor);
        canvas.drawCircle(selCenterX, selCenterY, selRadius, paint);
        // If needed, set up the clip path for later.
        if (selectorPath != null) {
            selectorPath.reset();
            selectorPath.addCircle(selCenterX, selCenterY, selRadius, android.graphics.Path.Direction.CCW);
        }
        // Draw the dot if we're between two items.
        final float dotScale = android.util.MathUtils.lerp(hoursDotScale, minutesDotScale, mHoursToMinutes);
        if (dotScale > 0) {
            final android.graphics.Paint dotPaint = mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_DOT];
            dotPaint.setColor(mSelectorDotColor);
            canvas.drawCircle(selCenterX, selCenterY, mSelectorDotRadius * dotScale, dotPaint);
        }
        // Shorten the line to only go from the edge of the center dot to the
        // edge of the selection circle.
        final double sin = java.lang.Math.sin(selAngleRad);
        final double cos = java.lang.Math.cos(selAngleRad);
        final float lineLength = selLength - selRadius;
        final int centerX = mXCenter + ((int) (mCenterDotRadius * sin));
        final int centerY = mYCenter - ((int) (mCenterDotRadius * cos));
        final float linePointX = centerX + ((int) (lineLength * sin));
        final float linePointY = centerY - ((int) (lineLength * cos));
        // Draw the line.
        final android.graphics.Paint linePaint = mPaintSelector[android.widget.RadialTimePickerView.SELECTOR_LINE];
        linePaint.setColor(mSelectorColor);
        linePaint.setStrokeWidth(mSelectorStroke);
        canvas.drawLine(mXCenter, mYCenter, linePointX, linePointY, linePaint);
    }

    private void calculatePositionsHours() {
        // Calculate the text positions
        final float numbersRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS];
        // Calculate the positions for the 12 numbers in the main circle.
        android.widget.RadialTimePickerView.calculatePositions(mPaint[android.widget.RadialTimePickerView.HOURS], numbersRadius, mXCenter, mYCenter, mTextSize[android.widget.RadialTimePickerView.HOURS], mOuterTextX[android.widget.RadialTimePickerView.HOURS], mOuterTextY[android.widget.RadialTimePickerView.HOURS]);
        // If we have an inner circle, calculate those positions too.
        if (mIs24HourMode) {
            final int innerNumbersRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS_INNER];
            android.widget.RadialTimePickerView.calculatePositions(mPaint[android.widget.RadialTimePickerView.HOURS], innerNumbersRadius, mXCenter, mYCenter, mTextSize[android.widget.RadialTimePickerView.HOURS_INNER], mInnerTextX, mInnerTextY);
        }
    }

    private void calculatePositionsMinutes() {
        // Calculate the text positions
        final float numbersRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.MINUTES];
        // Calculate the positions for the 12 numbers in the main circle.
        android.widget.RadialTimePickerView.calculatePositions(mPaint[android.widget.RadialTimePickerView.MINUTES], numbersRadius, mXCenter, mYCenter, mTextSize[android.widget.RadialTimePickerView.MINUTES], mOuterTextX[android.widget.RadialTimePickerView.MINUTES], mOuterTextY[android.widget.RadialTimePickerView.MINUTES]);
    }

    /**
     * Using the trigonometric Unit Circle, calculate the positions that the text will need to be
     * drawn at based on the specified circle radius. Place the values in the textGridHeights and
     * textGridWidths parameters.
     */
    private static void calculatePositions(android.graphics.Paint paint, float radius, float xCenter, float yCenter, float textSize, float[] x, float[] y) {
        // Adjust yCenter to account for the text's baseline.
        paint.setTextSize(textSize);
        yCenter -= (paint.descent() + paint.ascent()) / 2;
        for (int i = 0; i < android.widget.RadialTimePickerView.NUM_POSITIONS; i++) {
            x[i] = xCenter - (radius * android.widget.RadialTimePickerView.COS_30[i]);
            y[i] = yCenter - (radius * android.widget.RadialTimePickerView.SIN_30[i]);
        }
    }

    /**
     * Draw the 12 text values at the positions specified by the textGrid parameters.
     */
    private void drawTextElements(android.graphics.Canvas canvas, float textSize, android.graphics.Typeface typeface, android.content.res.ColorStateList textColor, java.lang.String[] texts, float[] textX, float[] textY, android.graphics.Paint paint, int alpha, boolean showActivated, int activatedDegrees, boolean activatedOnly) {
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        // The activated index can touch a range of elements.
        final float activatedIndex = activatedDegrees / (360.0F / android.widget.RadialTimePickerView.NUM_POSITIONS);
        final int activatedFloor = ((int) (activatedIndex));
        final int activatedCeil = ((int) (java.lang.Math.ceil(activatedIndex))) % android.widget.RadialTimePickerView.NUM_POSITIONS;
        for (int i = 0; i < 12; i++) {
            final boolean activated = (activatedFloor == i) || (activatedCeil == i);
            if (activatedOnly && (!activated)) {
                continue;
            }
            final int stateMask = android.util.StateSet.VIEW_STATE_ENABLED | (showActivated && activated ? android.util.StateSet.VIEW_STATE_ACTIVATED : 0);
            final int color = textColor.getColorForState(android.util.StateSet.get(stateMask), 0);
            paint.setColor(color);
            paint.setAlpha(getMultipliedAlpha(color, alpha));
            canvas.drawText(texts[i], textX[i], textY[i], paint);
        }
    }

    private int getDegreesFromXY(float x, float y, boolean constrainOutside) {
        // Ensure the point is inside the touchable area.
        final int innerBound;
        final int outerBound;
        if (mIs24HourMode && mShowHours) {
            innerBound = mMinDistForInnerNumber;
            outerBound = mMaxDistForOuterNumber;
        } else {
            final int index = (mShowHours) ? android.widget.RadialTimePickerView.HOURS : android.widget.RadialTimePickerView.MINUTES;
            final int center = mCircleRadius - mTextInset[index];
            innerBound = center - mSelectorRadius;
            outerBound = center + mSelectorRadius;
        }
        final double dX = x - mXCenter;
        final double dY = y - mYCenter;
        final double distFromCenter = java.lang.Math.sqrt((dX * dX) + (dY * dY));
        if ((distFromCenter < innerBound) || (constrainOutside && (distFromCenter > outerBound))) {
            return -1;
        }
        // Convert to degrees.
        final int degrees = ((int) (java.lang.Math.toDegrees(java.lang.Math.atan2(dY, dX) + (java.lang.Math.PI / 2)) + 0.5));
        if (degrees < 0) {
            return degrees + 360;
        } else {
            return degrees;
        }
    }

    private boolean getInnerCircleFromXY(float x, float y) {
        if (mIs24HourMode && mShowHours) {
            final double dX = x - mXCenter;
            final double dY = y - mYCenter;
            final double distFromCenter = java.lang.Math.sqrt((dX * dX) + (dY * dY));
            return distFromCenter <= mHalfwayDist;
        }
        return false;
    }

    boolean mChangedDuringTouch = false;

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if (!mInputEnabled) {
            return true;
        }
        final int action = event.getActionMasked();
        if (((action == android.view.MotionEvent.ACTION_MOVE) || (action == android.view.MotionEvent.ACTION_UP)) || (action == android.view.MotionEvent.ACTION_DOWN)) {
            boolean forceSelection = false;
            boolean autoAdvance = false;
            if (action == android.view.MotionEvent.ACTION_DOWN) {
                // This is a new event stream, reset whether the value changed.
                mChangedDuringTouch = false;
            } else
                if (action == android.view.MotionEvent.ACTION_UP) {
                    autoAdvance = true;
                    // If we saw a down/up pair without the value changing, assume
                    // this is a single-tap selection and force a change.
                    if (!mChangedDuringTouch) {
                        forceSelection = true;
                    }
                }

            mChangedDuringTouch |= handleTouchInput(event.getX(), event.getY(), forceSelection, autoAdvance);
        }
        return true;
    }

    private boolean handleTouchInput(float x, float y, boolean forceSelection, boolean autoAdvance) {
        final boolean isOnInnerCircle = getInnerCircleFromXY(x, y);
        final int degrees = getDegreesFromXY(x, y, false);
        if (degrees == (-1)) {
            return false;
        }
        // Ensure we're showing the correct picker.
        animatePicker(mShowHours, android.widget.RadialTimePickerView.ANIM_DURATION_TOUCH);
        @android.widget.RadialTimePickerView.PickerType
        final int type;
        final int newValue;
        final boolean valueChanged;
        if (mShowHours) {
            final int snapDegrees = android.widget.RadialTimePickerView.snapOnly30s(degrees, 0) % 360;
            valueChanged = (mIsOnInnerCircle != isOnInnerCircle) || (mSelectionDegrees[android.widget.RadialTimePickerView.HOURS] != snapDegrees);
            mIsOnInnerCircle = isOnInnerCircle;
            mSelectionDegrees[android.widget.RadialTimePickerView.HOURS] = snapDegrees;
            type = android.widget.RadialTimePickerView.HOURS;
            newValue = getCurrentHour();
        } else {
            final int snapDegrees = android.widget.RadialTimePickerView.snapPrefer30s(degrees) % 360;
            valueChanged = mSelectionDegrees[android.widget.RadialTimePickerView.MINUTES] != snapDegrees;
            mSelectionDegrees[android.widget.RadialTimePickerView.MINUTES] = snapDegrees;
            type = android.widget.RadialTimePickerView.MINUTES;
            newValue = getCurrentMinute();
        }
        if ((valueChanged || forceSelection) || autoAdvance) {
            // Fire the listener even if we just need to auto-advance.
            if (mListener != null) {
                mListener.onValueSelected(type, newValue, autoAdvance);
            }
            // Only provide feedback if the value actually changed.
            if (valueChanged || forceSelection) {
                performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK);
                invalidate();
            }
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean dispatchHoverEvent(android.view.MotionEvent event) {
        // First right-of-refusal goes the touch exploration helper.
        if (dispatchHoverEvent(event)) {
            return true;
        }
        return super.dispatchHoverEvent(event);
    }

    public void setInputEnabled(boolean inputEnabled) {
        mInputEnabled = inputEnabled;
        invalidate();
    }

    @java.lang.Override
    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent event, int pointerIndex) {
        if (!isEnabled()) {
            return null;
        }
        final int degrees = getDegreesFromXY(event.getX(), event.getY(), false);
        if (degrees != (-1)) {
            return android.view.PointerIcon.getSystemIcon(getContext(), android.view.PointerIcon.TYPE_HAND);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    private class RadialPickerTouchHelper extends com.android.internal.widget.ExploreByTouchHelper {
        private final android.graphics.Rect mTempRect = new android.graphics.Rect();

        private final int TYPE_HOUR = 1;

        private final int TYPE_MINUTE = 2;

        private final int SHIFT_TYPE = 0;

        private final int MASK_TYPE = 0xf;

        private final int SHIFT_VALUE = 8;

        private final int MASK_VALUE = 0xff;

        /**
         * Increment in which virtual views are exposed for minutes.
         */
        private final int MINUTE_INCREMENT = 5;

        public RadialPickerTouchHelper() {
            super(android.widget.RadialTimePickerView.this);
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.view.accessibility.AccessibilityNodeInfo info) {
            onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
        }

        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle arguments) {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true;
            }
            switch (action) {
                case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD :
                    adjustPicker(1);
                    return true;
                case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD :
                    adjustPicker(-1);
                    return true;
            }
            return false;
        }

        private void adjustPicker(int step) {
            final int stepSize;
            final int initialStep;
            final int maxValue;
            final int minValue;
            if (mShowHours) {
                stepSize = 1;
                final int currentHour24 = getCurrentHour();
                if (mIs24HourMode) {
                    initialStep = currentHour24;
                    minValue = 0;
                    maxValue = 23;
                } else {
                    initialStep = hour24To12(currentHour24);
                    minValue = 1;
                    maxValue = 12;
                }
            } else {
                stepSize = 5;
                initialStep = getCurrentMinute() / stepSize;
                minValue = 0;
                maxValue = 55;
            }
            final int nextValue = (initialStep + step) * stepSize;
            final int clampedValue = android.util.MathUtils.constrain(nextValue, minValue, maxValue);
            if (mShowHours) {
                setCurrentHour(clampedValue);
            } else {
                setCurrentMinute(clampedValue);
            }
        }

        @java.lang.Override
        protected int getVirtualViewAt(float x, float y) {
            final int id;
            final int degrees = getDegreesFromXY(x, y, true);
            if (degrees != (-1)) {
                final int snapDegrees = android.widget.RadialTimePickerView.snapOnly30s(degrees, 0) % 360;
                if (mShowHours) {
                    final boolean isOnInnerCircle = getInnerCircleFromXY(x, y);
                    final int hour24 = getHourForDegrees(snapDegrees, isOnInnerCircle);
                    final int hour = (mIs24HourMode) ? hour24 : hour24To12(hour24);
                    id = makeId(TYPE_HOUR, hour);
                } else {
                    final int current = getCurrentMinute();
                    final int touched = getMinuteForDegrees(degrees);
                    final int snapped = getMinuteForDegrees(snapDegrees);
                    // If the touched minute is closer to the current minute
                    // than it is to the snapped minute, return current.
                    final int currentOffset = getCircularDiff(current, touched, android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE);
                    final int snappedOffset = getCircularDiff(snapped, touched, android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE);
                    final int minute;
                    if (currentOffset < snappedOffset) {
                        minute = current;
                    } else {
                        minute = snapped;
                    }
                    id = makeId(TYPE_MINUTE, minute);
                }
            } else {
                id = INVALID_ID;
            }
            return id;
        }

        /**
         * Returns the difference in degrees between two values along a circle.
         *
         * @param first
         * 		value in the range [0,max]
         * @param second
         * 		value in the range [0,max]
         * @param max
         * 		the maximum value along the circle
         * @return the difference in between the two values
         */
        private int getCircularDiff(int first, int second, int max) {
            final int diff = java.lang.Math.abs(first - second);
            final int midpoint = max / 2;
            return diff > midpoint ? max - diff : diff;
        }

        @java.lang.Override
        protected void getVisibleVirtualViews(android.util.IntArray virtualViewIds) {
            if (mShowHours) {
                final int min = (mIs24HourMode) ? 0 : 1;
                final int max = (mIs24HourMode) ? 23 : 12;
                for (int i = min; i <= max; i++) {
                    virtualViewIds.add(makeId(TYPE_HOUR, i));
                }
            } else {
                final int current = getCurrentMinute();
                for (int i = 0; i < android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE; i += MINUTE_INCREMENT) {
                    virtualViewIds.add(makeId(TYPE_MINUTE, i));
                    // If the current minute falls between two increments,
                    // insert an extra node for it.
                    if ((current > i) && (current < (i + MINUTE_INCREMENT))) {
                        virtualViewIds.add(makeId(TYPE_MINUTE, current));
                    }
                }
            }
        }

        @java.lang.Override
        protected void onPopulateEventForVirtualView(int virtualViewId, android.view.accessibility.AccessibilityEvent event) {
            event.setClassName(getClass().getName());
            final int type = getTypeFromId(virtualViewId);
            final int value = getValueFromId(virtualViewId);
            final java.lang.CharSequence description = getVirtualViewDescription(type, value);
            event.setContentDescription(description);
        }

        @java.lang.Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, android.view.accessibility.AccessibilityNodeInfo node) {
            node.setClassName(getClass().getName());
            node.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
            final int type = getTypeFromId(virtualViewId);
            final int value = getValueFromId(virtualViewId);
            final java.lang.CharSequence description = getVirtualViewDescription(type, value);
            node.setContentDescription(description);
            getBoundsForVirtualView(virtualViewId, mTempRect);
            node.setBoundsInParent(mTempRect);
            final boolean selected = isVirtualViewSelected(type, value);
            node.setSelected(selected);
            final int nextId = getVirtualViewIdAfter(type, value);
            if (nextId != INVALID_ID) {
                node.setTraversalBefore(android.widget.RadialTimePickerView.this, nextId);
            }
        }

        private int getVirtualViewIdAfter(int type, int value) {
            if (type == TYPE_HOUR) {
                final int nextValue = value + 1;
                final int max = (mIs24HourMode) ? 23 : 12;
                if (nextValue <= max) {
                    return makeId(type, nextValue);
                }
            } else
                if (type == TYPE_MINUTE) {
                    final int current = getCurrentMinute();
                    final int snapValue = value - (value % MINUTE_INCREMENT);
                    final int nextValue = snapValue + MINUTE_INCREMENT;
                    if ((value < current) && (nextValue > current)) {
                        // The current value is between two snap values.
                        return makeId(type, current);
                    } else
                        if (nextValue < android.widget.RadialTimePickerView.MINUTES_IN_CIRCLE) {
                            return makeId(type, nextValue);
                        }

                }

            return INVALID_ID;
        }

        @java.lang.Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, android.os.Bundle arguments) {
            if (action == android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK) {
                final int type = getTypeFromId(virtualViewId);
                final int value = getValueFromId(virtualViewId);
                if (type == TYPE_HOUR) {
                    final int hour = (mIs24HourMode) ? value : hour12To24(value, mAmOrPm);
                    setCurrentHour(hour);
                    return true;
                } else
                    if (type == TYPE_MINUTE) {
                        setCurrentMinute(value);
                        return true;
                    }

            }
            return false;
        }

        private int hour12To24(int hour12, int amOrPm) {
            int hour24 = hour12;
            if (hour12 == 12) {
                if (amOrPm == android.widget.RadialTimePickerView.AM) {
                    hour24 = 0;
                }
            } else
                if (amOrPm == android.widget.RadialTimePickerView.PM) {
                    hour24 += 12;
                }

            return hour24;
        }

        private int hour24To12(int hour24) {
            if (hour24 == 0) {
                return 12;
            } else
                if (hour24 > 12) {
                    return hour24 - 12;
                } else {
                    return hour24;
                }

        }

        private void getBoundsForVirtualView(int virtualViewId, android.graphics.Rect bounds) {
            final float radius;
            final int type = getTypeFromId(virtualViewId);
            final int value = getValueFromId(virtualViewId);
            final float centerRadius;
            final float degrees;
            if (type == TYPE_HOUR) {
                final boolean innerCircle = getInnerCircleForHour(value);
                if (innerCircle) {
                    centerRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS_INNER];
                    radius = mSelectorRadius;
                } else {
                    centerRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.HOURS];
                    radius = mSelectorRadius;
                }
                degrees = getDegreesForHour(value);
            } else
                if (type == TYPE_MINUTE) {
                    centerRadius = mCircleRadius - mTextInset[android.widget.RadialTimePickerView.MINUTES];
                    degrees = getDegreesForMinute(value);
                    radius = mSelectorRadius;
                } else {
                    // This should never happen.
                    centerRadius = 0;
                    degrees = 0;
                    radius = 0;
                }

            final double radians = java.lang.Math.toRadians(degrees);
            final float xCenter = mXCenter + (centerRadius * ((float) (java.lang.Math.sin(radians))));
            final float yCenter = mYCenter - (centerRadius * ((float) (java.lang.Math.cos(radians))));
            bounds.set(((int) (xCenter - radius)), ((int) (yCenter - radius)), ((int) (xCenter + radius)), ((int) (yCenter + radius)));
        }

        private java.lang.CharSequence getVirtualViewDescription(int type, int value) {
            final java.lang.CharSequence description;
            if ((type == TYPE_HOUR) || (type == TYPE_MINUTE)) {
                description = java.lang.Integer.toString(value);
            } else {
                description = null;
            }
            return description;
        }

        private boolean isVirtualViewSelected(int type, int value) {
            final boolean selected;
            if (type == TYPE_HOUR) {
                selected = getCurrentHour() == value;
            } else
                if (type == TYPE_MINUTE) {
                    selected = getCurrentMinute() == value;
                } else {
                    selected = false;
                }

            return selected;
        }

        private int makeId(int type, int value) {
            return (type << SHIFT_TYPE) | (value << SHIFT_VALUE);
        }

        private int getTypeFromId(int id) {
            return (id >>> SHIFT_TYPE) & MASK_TYPE;
        }

        private int getValueFromId(int id) {
            return (id >>> SHIFT_VALUE) & MASK_VALUE;
        }
    }
}

