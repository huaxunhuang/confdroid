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
 * A calendar-like view displaying a specified month and the appropriate selectable day numbers
 * within the specified month.
 */
class SimpleMonthView extends android.view.View {
    private static final int DAYS_IN_WEEK = 7;

    private static final int MAX_WEEKS_IN_MONTH = 6;

    private static final int DEFAULT_SELECTED_DAY = -1;

    private static final int DEFAULT_WEEK_START = android.icu.util.Calendar.SUNDAY;

    private static final java.lang.String MONTH_YEAR_FORMAT = "MMMMy";

    private static final int SELECTED_HIGHLIGHT_ALPHA = 0xb0;

    private final android.text.TextPaint mMonthPaint = new android.text.TextPaint();

    private final android.text.TextPaint mDayOfWeekPaint = new android.text.TextPaint();

    private final android.text.TextPaint mDayPaint = new android.text.TextPaint();

    private final android.graphics.Paint mDaySelectorPaint = new android.graphics.Paint();

    private final android.graphics.Paint mDayHighlightPaint = new android.graphics.Paint();

    private final android.graphics.Paint mDayHighlightSelectorPaint = new android.graphics.Paint();

    /**
     * Array of single-character weekday labels ordered by column index.
     */
    private final java.lang.String[] mDayOfWeekLabels = new java.lang.String[7];

    private final android.icu.util.Calendar mCalendar;

    private final java.util.Locale mLocale;

    private final android.widget.SimpleMonthView.MonthViewTouchHelper mTouchHelper;

    private final java.text.NumberFormat mDayFormatter;

    // Desired dimensions.
    private final int mDesiredMonthHeight;

    private final int mDesiredDayOfWeekHeight;

    private final int mDesiredDayHeight;

    private final int mDesiredCellWidth;

    private final int mDesiredDaySelectorRadius;

    private java.lang.String mMonthYearLabel;

    private int mMonth;

    private int mYear;

    // Dimensions as laid out.
    private int mMonthHeight;

    private int mDayOfWeekHeight;

    private int mDayHeight;

    private int mCellWidth;

    private int mDaySelectorRadius;

    private int mPaddedWidth;

    private int mPaddedHeight;

    /**
     * The day of month for the selected day, or -1 if no day is selected.
     */
    private int mActivatedDay = -1;

    /**
     * The day of month for today, or -1 if the today is not in the current
     * month.
     */
    private int mToday = android.widget.SimpleMonthView.DEFAULT_SELECTED_DAY;

    /**
     * The first day of the week (ex. Calendar.SUNDAY) indexed from one.
     */
    private int mWeekStart = android.widget.SimpleMonthView.DEFAULT_WEEK_START;

    /**
     * The number of days (ex. 28) in the current month.
     */
    private int mDaysInMonth;

    /**
     * The day of week (ex. Calendar.SUNDAY) for the first day of the current
     * month.
     */
    private int mDayOfWeekStart;

    /**
     * The day of month for the first (inclusive) enabled day.
     */
    private int mEnabledDayStart = 1;

    /**
     * The day of month for the last (inclusive) enabled day.
     */
    private int mEnabledDayEnd = 31;

    /**
     * Optional listener for handling day click actions.
     */
    private android.widget.SimpleMonthView.OnDayClickListener mOnDayClickListener;

    private android.content.res.ColorStateList mDayTextColor;

    private int mHighlightedDay = -1;

    private int mPreviouslyHighlightedDay = -1;

    private boolean mIsTouchHighlighted = false;

    public SimpleMonthView(android.content.Context context) {
        this(context, null);
    }

    public SimpleMonthView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.datePickerStyle);
    }

    public SimpleMonthView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleMonthView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.Resources res = context.getResources();
        mDesiredMonthHeight = res.getDimensionPixelSize(R.dimen.date_picker_month_height);
        mDesiredDayOfWeekHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_height);
        mDesiredDayHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_height);
        mDesiredCellWidth = res.getDimensionPixelSize(R.dimen.date_picker_day_width);
        mDesiredDaySelectorRadius = res.getDimensionPixelSize(R.dimen.date_picker_day_selector_radius);
        // Set up accessibility components.
        mTouchHelper = new android.widget.SimpleMonthView.MonthViewTouchHelper(this);
        setAccessibilityDelegate(mTouchHelper);
        setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        mLocale = res.getConfiguration().locale;
        mCalendar = android.icu.util.Calendar.getInstance(mLocale);
        mDayFormatter = java.text.NumberFormat.getIntegerInstance(mLocale);
        updateMonthYearLabel();
        updateDayOfWeekLabels();
        initPaints(res);
    }

    private void updateMonthYearLabel() {
        final java.lang.String format = android.text.format.DateFormat.getBestDateTimePattern(mLocale, android.widget.SimpleMonthView.MONTH_YEAR_FORMAT);
        final android.icu.text.SimpleDateFormat formatter = new android.icu.text.SimpleDateFormat(format, mLocale);
        formatter.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        mMonthYearLabel = formatter.format(mCalendar.getTime());
    }

    private void updateDayOfWeekLabels() {
        // Use tiny (e.g. single-character) weekday names from ICU. The indices
        // for this list correspond to Calendar days, e.g. SUNDAY is index 1.
        final java.lang.String[] tinyWeekdayNames = libcore.icu.LocaleData.get(mLocale).tinyWeekdayNames;
        for (int i = 0; i < android.widget.SimpleMonthView.DAYS_IN_WEEK; i++) {
            mDayOfWeekLabels[i] = tinyWeekdayNames[(((mWeekStart + i) - 1) % android.widget.SimpleMonthView.DAYS_IN_WEEK) + 1];
        }
    }

    /**
     * Applies the specified text appearance resource to a paint, returning the
     * text color if one is set in the text appearance.
     *
     * @param p
     * 		the paint to modify
     * @param resId
     * 		the resource ID of the text appearance
     * @return the text color, if available
     */
    private android.content.res.ColorStateList applyTextAppearance(android.graphics.Paint p, int resId) {
        final android.content.res.TypedArray ta = mContext.obtainStyledAttributes(null, R.styleable.TextAppearance, 0, resId);
        final java.lang.String fontFamily = ta.getString(R.styleable.TextAppearance_fontFamily);
        if (fontFamily != null) {
            p.setTypeface(android.graphics.Typeface.create(fontFamily, 0));
        }
        p.setTextSize(ta.getDimensionPixelSize(R.styleable.TextAppearance_textSize, ((int) (p.getTextSize()))));
        final android.content.res.ColorStateList textColor = ta.getColorStateList(R.styleable.TextAppearance_textColor);
        if (textColor != null) {
            final int enabledColor = textColor.getColorForState(android.view.View.ENABLED_STATE_SET, 0);
            p.setColor(enabledColor);
        }
        ta.recycle();
        return textColor;
    }

    public int getMonthHeight() {
        return mMonthHeight;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public void setMonthTextAppearance(int resId) {
        applyTextAppearance(mMonthPaint, resId);
        invalidate();
    }

    public void setDayOfWeekTextAppearance(int resId) {
        applyTextAppearance(mDayOfWeekPaint, resId);
        invalidate();
    }

    public void setDayTextAppearance(int resId) {
        final android.content.res.ColorStateList textColor = applyTextAppearance(mDayPaint, resId);
        if (textColor != null) {
            mDayTextColor = textColor;
        }
        invalidate();
    }

    /**
     * Sets up the text and style properties for painting.
     */
    private void initPaints(android.content.res.Resources res) {
        final java.lang.String monthTypeface = res.getString(R.string.date_picker_month_typeface);
        final java.lang.String dayOfWeekTypeface = res.getString(R.string.date_picker_day_of_week_typeface);
        final java.lang.String dayTypeface = res.getString(R.string.date_picker_day_typeface);
        final int monthTextSize = res.getDimensionPixelSize(R.dimen.date_picker_month_text_size);
        final int dayOfWeekTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_text_size);
        final int dayTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_text_size);
        mMonthPaint.setAntiAlias(true);
        mMonthPaint.setTextSize(monthTextSize);
        mMonthPaint.setTypeface(android.graphics.Typeface.create(monthTypeface, 0));
        mMonthPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        mMonthPaint.setStyle(android.graphics.Paint.Style.FILL);
        mDayOfWeekPaint.setAntiAlias(true);
        mDayOfWeekPaint.setTextSize(dayOfWeekTextSize);
        mDayOfWeekPaint.setTypeface(android.graphics.Typeface.create(dayOfWeekTypeface, 0));
        mDayOfWeekPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        mDayOfWeekPaint.setStyle(android.graphics.Paint.Style.FILL);
        mDaySelectorPaint.setAntiAlias(true);
        mDaySelectorPaint.setStyle(android.graphics.Paint.Style.FILL);
        mDayHighlightPaint.setAntiAlias(true);
        mDayHighlightPaint.setStyle(android.graphics.Paint.Style.FILL);
        mDayHighlightSelectorPaint.setAntiAlias(true);
        mDayHighlightSelectorPaint.setStyle(android.graphics.Paint.Style.FILL);
        mDayPaint.setAntiAlias(true);
        mDayPaint.setTextSize(dayTextSize);
        mDayPaint.setTypeface(android.graphics.Typeface.create(dayTypeface, 0));
        mDayPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        mDayPaint.setStyle(android.graphics.Paint.Style.FILL);
    }

    void setMonthTextColor(android.content.res.ColorStateList monthTextColor) {
        final int enabledColor = monthTextColor.getColorForState(android.view.View.ENABLED_STATE_SET, 0);
        mMonthPaint.setColor(enabledColor);
        invalidate();
    }

    void setDayOfWeekTextColor(android.content.res.ColorStateList dayOfWeekTextColor) {
        final int enabledColor = dayOfWeekTextColor.getColorForState(android.view.View.ENABLED_STATE_SET, 0);
        mDayOfWeekPaint.setColor(enabledColor);
        invalidate();
    }

    void setDayTextColor(android.content.res.ColorStateList dayTextColor) {
        mDayTextColor = dayTextColor;
        invalidate();
    }

    void setDaySelectorColor(android.content.res.ColorStateList dayBackgroundColor) {
        final int activatedColor = dayBackgroundColor.getColorForState(android.util.StateSet.get(android.util.StateSet.VIEW_STATE_ENABLED | android.util.StateSet.VIEW_STATE_ACTIVATED), 0);
        mDaySelectorPaint.setColor(activatedColor);
        mDayHighlightSelectorPaint.setColor(activatedColor);
        mDayHighlightSelectorPaint.setAlpha(android.widget.SimpleMonthView.SELECTED_HIGHLIGHT_ALPHA);
        invalidate();
    }

    void setDayHighlightColor(android.content.res.ColorStateList dayHighlightColor) {
        final int pressedColor = dayHighlightColor.getColorForState(android.util.StateSet.get(android.util.StateSet.VIEW_STATE_ENABLED | android.util.StateSet.VIEW_STATE_PRESSED), 0);
        mDayHighlightPaint.setColor(pressedColor);
        invalidate();
    }

    public void setOnDayClickListener(android.widget.SimpleMonthView.OnDayClickListener listener) {
        mOnDayClickListener = listener;
    }

    @java.lang.Override
    public boolean dispatchHoverEvent(android.view.MotionEvent event) {
        // First right-of-refusal goes the touch exploration helper.
        return dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        final int x = ((int) (event.getX() + 0.5F));
        final int y = ((int) (event.getY() + 0.5F));
        final int action = event.getAction();
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
            case android.view.MotionEvent.ACTION_MOVE :
                final int touchedItem = getDayAtLocation(x, y);
                mIsTouchHighlighted = true;
                if (mHighlightedDay != touchedItem) {
                    mHighlightedDay = touchedItem;
                    mPreviouslyHighlightedDay = touchedItem;
                    invalidate();
                }
                if ((action == android.view.MotionEvent.ACTION_DOWN) && (touchedItem < 0)) {
                    // Touch something that's not an item, reject event.
                    return false;
                }
                break;
            case android.view.MotionEvent.ACTION_UP :
                final int clickedDay = getDayAtLocation(x, y);
                onDayClicked(clickedDay);
                // Fall through.
            case android.view.MotionEvent.ACTION_CANCEL :
                // Reset touched day on stream end.
                mHighlightedDay = -1;
                mIsTouchHighlighted = false;
                invalidate();
                break;
        }
        return true;
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        // We need to handle focus change within the SimpleMonthView because we are simulating
        // multiple Views. The arrow keys will move between days until there is no space (no
        // day to the left, top, right, or bottom). Focus forward and back jumps out of the
        // SimpleMonthView, skipping over other SimpleMonthViews in the parent ViewPager
        // to the next focusable View in the hierarchy.
        boolean focusChanged = false;
        switch (event.getKeyCode()) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                if (event.hasNoModifiers()) {
                    focusChanged = moveOneDay(isLayoutRtl());
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                if (event.hasNoModifiers()) {
                    focusChanged = moveOneDay(!isLayoutRtl());
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
                if (event.hasNoModifiers()) {
                    ensureFocusedDay();
                    if (mHighlightedDay > 7) {
                        mHighlightedDay -= 7;
                        focusChanged = true;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                if (event.hasNoModifiers()) {
                    ensureFocusedDay();
                    if (mHighlightedDay <= (mDaysInMonth - 7)) {
                        mHighlightedDay += 7;
                        focusChanged = true;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_ENTER :
                if (mHighlightedDay != (-1)) {
                    onDayClicked(mHighlightedDay);
                    return true;
                }
                break;
            case android.view.KeyEvent.KEYCODE_TAB :
                {
                    int focusChangeDirection = 0;
                    if (event.hasNoModifiers()) {
                        focusChangeDirection = android.view.View.FOCUS_FORWARD;
                    } else
                        if (event.hasModifiers(android.view.KeyEvent.META_SHIFT_ON)) {
                            focusChangeDirection = android.view.View.FOCUS_BACKWARD;
                        }

                    if (focusChangeDirection != 0) {
                        final android.view.ViewParent parent = getParent();
                        // move out of the ViewPager next/previous
                        android.view.View nextFocus = this;
                        do {
                            nextFocus = nextFocus.focusSearch(focusChangeDirection);
                        } while (((nextFocus != null) && (nextFocus != this)) && (nextFocus.getParent() == parent) );
                        if (nextFocus != null) {
                            nextFocus.requestFocus();
                            return true;
                        }
                    }
                    break;
                }
        }
        if (focusChanged) {
            invalidate();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private boolean moveOneDay(boolean positive) {
        ensureFocusedDay();
        boolean focusChanged = false;
        if (positive) {
            if ((!isLastDayOfWeek(mHighlightedDay)) && (mHighlightedDay < mDaysInMonth)) {
                mHighlightedDay++;
                focusChanged = true;
            }
        } else {
            if ((!isFirstDayOfWeek(mHighlightedDay)) && (mHighlightedDay > 1)) {
                mHighlightedDay--;
                focusChanged = true;
            }
        }
        return focusChanged;
    }

    @java.lang.Override
    protected void onFocusChanged(boolean gainFocus, @android.view.View.FocusDirection
    int direction, @android.annotation.Nullable
    android.graphics.Rect previouslyFocusedRect) {
        if (gainFocus) {
            // If we've gained focus through arrow keys, we should find the day closest
            // to the focus rect. If we've gained focus through forward/back, we should
            // focus on the selected day if there is one.
            final int offset = findDayOffset();
            switch (direction) {
                case android.view.View.FOCUS_RIGHT :
                    {
                        int row = findClosestRow(previouslyFocusedRect);
                        mHighlightedDay = (row == 0) ? 1 : ((row * android.widget.SimpleMonthView.DAYS_IN_WEEK) - offset) + 1;
                        break;
                    }
                case android.view.View.FOCUS_LEFT :
                    {
                        int row = findClosestRow(previouslyFocusedRect) + 1;
                        mHighlightedDay = java.lang.Math.min(mDaysInMonth, (row * android.widget.SimpleMonthView.DAYS_IN_WEEK) - offset);
                        break;
                    }
                case android.view.View.FOCUS_DOWN :
                    {
                        final int col = findClosestColumn(previouslyFocusedRect);
                        final int day = (col - offset) + 1;
                        mHighlightedDay = (day < 1) ? day + android.widget.SimpleMonthView.DAYS_IN_WEEK : day;
                        break;
                    }
                case android.view.View.FOCUS_UP :
                    {
                        final int col = findClosestColumn(previouslyFocusedRect);
                        final int maxWeeks = (offset + mDaysInMonth) / android.widget.SimpleMonthView.DAYS_IN_WEEK;
                        final int day = ((col - offset) + (android.widget.SimpleMonthView.DAYS_IN_WEEK * maxWeeks)) + 1;
                        mHighlightedDay = (day > mDaysInMonth) ? day - android.widget.SimpleMonthView.DAYS_IN_WEEK : day;
                        break;
                    }
            }
            ensureFocusedDay();
            invalidate();
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    /**
     * Returns the row (0 indexed) closest to previouslyFocusedRect or center if null.
     */
    private int findClosestRow(@android.annotation.Nullable
    android.graphics.Rect previouslyFocusedRect) {
        if (previouslyFocusedRect == null) {
            return 3;
        } else
            if (mDayHeight == 0) {
                return 0;// There hasn't been a layout, so just choose the first row

            } else {
                int centerY = previouslyFocusedRect.centerY();
                final android.text.TextPaint p = mDayPaint;
                final int headerHeight = mMonthHeight + mDayOfWeekHeight;
                final int rowHeight = mDayHeight;
                // Text is vertically centered within the row height.
                final float halfLineHeight = (p.ascent() + p.descent()) / 2.0F;
                final int rowCenter = headerHeight + (rowHeight / 2);
                centerY -= rowCenter - halfLineHeight;
                int row = java.lang.Math.round(centerY / ((float) (rowHeight)));
                final int maxDay = findDayOffset() + mDaysInMonth;
                final int maxRows = (maxDay / android.widget.SimpleMonthView.DAYS_IN_WEEK) - ((maxDay % android.widget.SimpleMonthView.DAYS_IN_WEEK) == 0 ? 1 : 0);
                row = android.util.MathUtils.constrain(row, 0, maxRows);
                return row;
            }

    }

    /**
     * Returns the column (0 indexed) closest to the previouslyFocusedRect or center if null.
     * The 0 index is related to the first day of the week.
     */
    private int findClosestColumn(@android.annotation.Nullable
    android.graphics.Rect previouslyFocusedRect) {
        if (previouslyFocusedRect == null) {
            return android.widget.SimpleMonthView.DAYS_IN_WEEK / 2;
        } else
            if (mCellWidth == 0) {
                return 0;// There hasn't been a layout, so we can just choose the first column

            } else {
                int centerX = previouslyFocusedRect.centerX() - mPaddingLeft;
                final int columnFromLeft = android.util.MathUtils.constrain(centerX / mCellWidth, 0, android.widget.SimpleMonthView.DAYS_IN_WEEK - 1);
                return isLayoutRtl() ? (android.widget.SimpleMonthView.DAYS_IN_WEEK - columnFromLeft) - 1 : columnFromLeft;
            }

    }

    @java.lang.Override
    public void getFocusedRect(android.graphics.Rect r) {
        if (mHighlightedDay > 0) {
            getBoundsForDay(mHighlightedDay, r);
        } else {
            super.getFocusedRect(r);
        }
    }

    @java.lang.Override
    protected void onFocusLost() {
        if (!mIsTouchHighlighted) {
            // Unhighlight a day.
            mPreviouslyHighlightedDay = mHighlightedDay;
            mHighlightedDay = -1;
            invalidate();
        }
        super.onFocusLost();
    }

    /**
     * Ensure some day is highlighted. If a day isn't highlighted, it chooses the selected day,
     * if possible, or the first day of the month if not.
     */
    private void ensureFocusedDay() {
        if (mHighlightedDay != (-1)) {
            return;
        }
        if (mPreviouslyHighlightedDay != (-1)) {
            mHighlightedDay = mPreviouslyHighlightedDay;
            return;
        }
        if (mActivatedDay != (-1)) {
            mHighlightedDay = mActivatedDay;
            return;
        }
        mHighlightedDay = 1;
    }

    private boolean isFirstDayOfWeek(int day) {
        final int offset = findDayOffset();
        return (((offset + day) - 1) % android.widget.SimpleMonthView.DAYS_IN_WEEK) == 0;
    }

    private boolean isLastDayOfWeek(int day) {
        final int offset = findDayOffset();
        return ((offset + day) % android.widget.SimpleMonthView.DAYS_IN_WEEK) == 0;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        canvas.translate(paddingLeft, paddingTop);
        drawMonth(canvas);
        drawDaysOfWeek(canvas);
        drawDays(canvas);
        canvas.translate(-paddingLeft, -paddingTop);
    }

    private void drawMonth(android.graphics.Canvas canvas) {
        final float x = mPaddedWidth / 2.0F;
        // Vertically centered within the month header height.
        final float lineHeight = mMonthPaint.ascent() + mMonthPaint.descent();
        final float y = (mMonthHeight - lineHeight) / 2.0F;
        canvas.drawText(mMonthYearLabel, x, y, mMonthPaint);
    }

    public java.lang.String getMonthYearLabel() {
        return mMonthYearLabel;
    }

    private void drawDaysOfWeek(android.graphics.Canvas canvas) {
        final android.text.TextPaint p = mDayOfWeekPaint;
        final int headerHeight = mMonthHeight;
        final int rowHeight = mDayOfWeekHeight;
        final int colWidth = mCellWidth;
        // Text is vertically centered within the day of week height.
        final float halfLineHeight = (p.ascent() + p.descent()) / 2.0F;
        final int rowCenter = headerHeight + (rowHeight / 2);
        for (int col = 0; col < android.widget.SimpleMonthView.DAYS_IN_WEEK; col++) {
            final int colCenter = (colWidth * col) + (colWidth / 2);
            final int colCenterRtl;
            if (isLayoutRtl()) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            final java.lang.String label = mDayOfWeekLabels[col];
            canvas.drawText(label, colCenterRtl, rowCenter - halfLineHeight, p);
        }
    }

    /**
     * Draws the month days.
     */
    private void drawDays(android.graphics.Canvas canvas) {
        final android.text.TextPaint p = mDayPaint;
        final int headerHeight = mMonthHeight + mDayOfWeekHeight;
        final int rowHeight = mDayHeight;
        final int colWidth = mCellWidth;
        // Text is vertically centered within the row height.
        final float halfLineHeight = (p.ascent() + p.descent()) / 2.0F;
        int rowCenter = headerHeight + (rowHeight / 2);
        for (int day = 1, col = findDayOffset(); day <= mDaysInMonth; day++) {
            final int colCenter = (colWidth * col) + (colWidth / 2);
            final int colCenterRtl;
            if (isLayoutRtl()) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            int stateMask = 0;
            final boolean isDayEnabled = isDayEnabled(day);
            if (isDayEnabled) {
                stateMask |= android.util.StateSet.VIEW_STATE_ENABLED;
            }
            final boolean isDayActivated = mActivatedDay == day;
            final boolean isDayHighlighted = mHighlightedDay == day;
            if (isDayActivated) {
                stateMask |= android.util.StateSet.VIEW_STATE_ACTIVATED;
                // Adjust the circle to be centered on the row.
                final android.graphics.Paint paint = (isDayHighlighted) ? mDayHighlightSelectorPaint : mDaySelectorPaint;
                canvas.drawCircle(colCenterRtl, rowCenter, mDaySelectorRadius, paint);
            } else
                if (isDayHighlighted) {
                    stateMask |= android.util.StateSet.VIEW_STATE_PRESSED;
                    if (isDayEnabled) {
                        // Adjust the circle to be centered on the row.
                        canvas.drawCircle(colCenterRtl, rowCenter, mDaySelectorRadius, mDayHighlightPaint);
                    }
                }

            final boolean isDayToday = mToday == day;
            final int dayTextColor;
            if (isDayToday && (!isDayActivated)) {
                dayTextColor = mDaySelectorPaint.getColor();
            } else {
                final int[] stateSet = android.util.StateSet.get(stateMask);
                dayTextColor = mDayTextColor.getColorForState(stateSet, 0);
            }
            p.setColor(dayTextColor);
            canvas.drawText(mDayFormatter.format(day), colCenterRtl, rowCenter - halfLineHeight, p);
            col++;
            if (col == android.widget.SimpleMonthView.DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    private boolean isDayEnabled(int day) {
        return (day >= mEnabledDayStart) && (day <= mEnabledDayEnd);
    }

    private boolean isValidDayOfMonth(int day) {
        return (day >= 1) && (day <= mDaysInMonth);
    }

    private static boolean isValidDayOfWeek(int day) {
        return (day >= android.icu.util.Calendar.SUNDAY) && (day <= android.icu.util.Calendar.SATURDAY);
    }

    private static boolean isValidMonth(int month) {
        return (month >= android.icu.util.Calendar.JANUARY) && (month <= android.icu.util.Calendar.DECEMBER);
    }

    /**
     * Sets the selected day.
     *
     * @param dayOfMonth
     * 		the selected day of the month, or {@code -1} to clear
     * 		the selection
     */
    public void setSelectedDay(int dayOfMonth) {
        mActivatedDay = dayOfMonth;
        // Invalidate cached accessibility information.
        mTouchHelper.invalidateRoot();
        invalidate();
    }

    /**
     * Sets the first day of the week.
     *
     * @param weekStart
     * 		which day the week should start on, valid values are
     * 		{@link Calendar#SUNDAY} through {@link Calendar#SATURDAY}
     */
    public void setFirstDayOfWeek(int weekStart) {
        if (android.widget.SimpleMonthView.isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
        updateDayOfWeekLabels();
        // Invalidate cached accessibility information.
        mTouchHelper.invalidateRoot();
        invalidate();
    }

    /**
     * Sets all the parameters for displaying this week.
     * <p>
     * Parameters have a default value and will only update if a new value is
     * included, except for focus month, which will always default to no focus
     * month if no value is passed in. The only required parameter is the week
     * start.
     *
     * @param selectedDay
     * 		the selected day of the month, or -1 for no selection
     * @param month
     * 		the month
     * @param year
     * 		the year
     * @param weekStart
     * 		which day the week should start on, valid values are
     * 		{@link Calendar#SUNDAY} through {@link Calendar#SATURDAY}
     * @param enabledDayStart
     * 		the first enabled day
     * @param enabledDayEnd
     * 		the last enabled day
     */
    void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd) {
        mActivatedDay = selectedDay;
        if (android.widget.SimpleMonthView.isValidMonth(month)) {
            mMonth = month;
        }
        mYear = year;
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);
        if (android.widget.SimpleMonthView.isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
        // Figure out what day today is.
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        mToday = -1;
        mDaysInMonth = android.widget.SimpleMonthView.getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mDaysInMonth; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mToday = day;
            }
        }
        mEnabledDayStart = android.util.MathUtils.constrain(enabledDayStart, 1, mDaysInMonth);
        mEnabledDayEnd = android.util.MathUtils.constrain(enabledDayEnd, mEnabledDayStart, mDaysInMonth);
        updateMonthYearLabel();
        updateDayOfWeekLabels();
        // Invalidate cached accessibility information.
        mTouchHelper.invalidateRoot();
        invalidate();
    }

    private static int getDaysInMonth(int month, int year) {
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

    private boolean sameDay(int day, android.icu.util.Calendar today) {
        return ((mYear == today.get(Calendar.YEAR)) && (mMonth == today.get(Calendar.MONTH))) && (day == today.get(Calendar.DAY_OF_MONTH));
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int preferredHeight = ((((mDesiredDayHeight * android.widget.SimpleMonthView.MAX_WEEKS_IN_MONTH) + mDesiredDayOfWeekHeight) + mDesiredMonthHeight) + getPaddingTop()) + getPaddingBottom();
        final int preferredWidth = ((mDesiredCellWidth * android.widget.SimpleMonthView.DAYS_IN_WEEK) + getPaddingStart()) + getPaddingEnd();
        final int resolvedWidth = android.view.View.resolveSize(preferredWidth, widthMeasureSpec);
        final int resolvedHeight = android.view.View.resolveSize(preferredHeight, heightMeasureSpec);
        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(@android.view.View.ResolvedLayoutDir
    int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        requestLayout();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }
        // Let's initialize a completely reasonable number of variables.
        final int w = right - left;
        final int h = bottom - top;
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int paddedRight = w - paddingRight;
        final int paddedBottom = h - paddingBottom;
        final int paddedWidth = paddedRight - paddingLeft;
        final int paddedHeight = paddedBottom - paddingTop;
        if ((paddedWidth == mPaddedWidth) || (paddedHeight == mPaddedHeight)) {
            return;
        }
        mPaddedWidth = paddedWidth;
        mPaddedHeight = paddedHeight;
        // We may have been laid out smaller than our preferred size. If so,
        // scale all dimensions to fit.
        final int measuredPaddedHeight = (getMeasuredHeight() - paddingTop) - paddingBottom;
        final float scaleH = paddedHeight / ((float) (measuredPaddedHeight));
        final int monthHeight = ((int) (mDesiredMonthHeight * scaleH));
        final int cellWidth = mPaddedWidth / android.widget.SimpleMonthView.DAYS_IN_WEEK;
        mMonthHeight = monthHeight;
        mDayOfWeekHeight = ((int) (mDesiredDayOfWeekHeight * scaleH));
        mDayHeight = ((int) (mDesiredDayHeight * scaleH));
        mCellWidth = cellWidth;
        // Compute the largest day selector radius that's still within the clip
        // bounds and desired selector radius.
        final int maxSelectorWidth = (cellWidth / 2) + java.lang.Math.min(paddingLeft, paddingRight);
        final int maxSelectorHeight = (mDayHeight / 2) + paddingBottom;
        mDaySelectorRadius = java.lang.Math.min(mDesiredDaySelectorRadius, java.lang.Math.min(maxSelectorWidth, maxSelectorHeight));
        // Invalidate cached accessibility information.
        mTouchHelper.invalidateRoot();
    }

    private int findDayOffset() {
        final int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + android.widget.SimpleMonthView.DAYS_IN_WEEK;
        }
        return offset;
    }

    /**
     * Calculates the day of the month at the specified touch position. Returns
     * the day of the month or -1 if the position wasn't in a valid day.
     *
     * @param x
     * 		the x position of the touch event
     * @param y
     * 		the y position of the touch event
     * @return the day of the month at (x, y), or -1 if the position wasn't in
    a valid day
     */
    private int getDayAtLocation(int x, int y) {
        final int paddedX = x - getPaddingLeft();
        if ((paddedX < 0) || (paddedX >= mPaddedWidth)) {
            return -1;
        }
        final int headerHeight = mMonthHeight + mDayOfWeekHeight;
        final int paddedY = y - getPaddingTop();
        if ((paddedY < headerHeight) || (paddedY >= mPaddedHeight)) {
            return -1;
        }
        // Adjust for RTL after applying padding.
        final int paddedXRtl;
        if (isLayoutRtl()) {
            paddedXRtl = mPaddedWidth - paddedX;
        } else {
            paddedXRtl = paddedX;
        }
        final int row = (paddedY - headerHeight) / mDayHeight;
        final int col = (paddedXRtl * android.widget.SimpleMonthView.DAYS_IN_WEEK) / mPaddedWidth;
        final int index = col + (row * android.widget.SimpleMonthView.DAYS_IN_WEEK);
        final int day = (index + 1) - findDayOffset();
        if (!isValidDayOfMonth(day)) {
            return -1;
        }
        return day;
    }

    /**
     * Calculates the bounds of the specified day.
     *
     * @param id
     * 		the day of the month
     * @param outBounds
     * 		the rect to populate with bounds
     */
    public boolean getBoundsForDay(int id, android.graphics.Rect outBounds) {
        if (!isValidDayOfMonth(id)) {
            return false;
        }
        final int index = (id - 1) + findDayOffset();
        // Compute left edge, taking into account RTL.
        final int col = index % android.widget.SimpleMonthView.DAYS_IN_WEEK;
        final int colWidth = mCellWidth;
        final int left;
        if (isLayoutRtl()) {
            left = (getWidth() - getPaddingRight()) - ((col + 1) * colWidth);
        } else {
            left = getPaddingLeft() + (col * colWidth);
        }
        // Compute top edge.
        final int row = index / android.widget.SimpleMonthView.DAYS_IN_WEEK;
        final int rowHeight = mDayHeight;
        final int headerHeight = mMonthHeight + mDayOfWeekHeight;
        final int top = (getPaddingTop() + headerHeight) + (row * rowHeight);
        outBounds.set(left, top, left + colWidth, top + rowHeight);
        return true;
    }

    /**
     * Called when the user clicks on a day. Handles callbacks to the
     * {@link OnDayClickListener} if one is set.
     *
     * @param day
     * 		the day that was clicked
     */
    private boolean onDayClicked(int day) {
        if ((!isValidDayOfMonth(day)) || (!isDayEnabled(day))) {
            return false;
        }
        if (mOnDayClickListener != null) {
            final android.icu.util.Calendar date = android.icu.util.Calendar.getInstance();
            date.set(mYear, mMonth, day);
            mOnDayClickListener.onDayClick(this, date);
        }
        // This is a no-op if accessibility is turned off.
        mTouchHelper.sendEventForVirtualView(day, android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED);
        return true;
    }

    @java.lang.Override
    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent event, int pointerIndex) {
        if (!isEnabled()) {
            return null;
        }
        // Add 0.5f to event coordinates to match the logic in onTouchEvent.
        final int x = ((int) (event.getX() + 0.5F));
        final int y = ((int) (event.getY() + 0.5F));
        final int dayUnderPointer = getDayAtLocation(x, y);
        if (dayUnderPointer >= 0) {
            return android.view.PointerIcon.getSystemIcon(getContext(), android.view.PointerIcon.TYPE_HAND);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    /**
     * Provides a virtual view hierarchy for interfacing with an accessibility
     * service.
     */
    private class MonthViewTouchHelper extends com.android.internal.widget.ExploreByTouchHelper {
        private static final java.lang.String DATE_FORMAT = "dd MMMM yyyy";

        private final android.graphics.Rect mTempRect = new android.graphics.Rect();

        private final android.icu.util.Calendar mTempCalendar = android.icu.util.Calendar.getInstance();

        public MonthViewTouchHelper(android.view.View host) {
            super(host);
        }

        @java.lang.Override
        protected int getVirtualViewAt(float x, float y) {
            final int day = getDayAtLocation(((int) (x + 0.5F)), ((int) (y + 0.5F)));
            if (day != (-1)) {
                return day;
            }
            return com.android.internal.widget.ExploreByTouchHelper.INVALID_ID;
        }

        @java.lang.Override
        protected void getVisibleVirtualViews(android.util.IntArray virtualViewIds) {
            for (int day = 1; day <= mDaysInMonth; day++) {
                virtualViewIds.add(day);
            }
        }

        @java.lang.Override
        protected void onPopulateEventForVirtualView(int virtualViewId, android.view.accessibility.AccessibilityEvent event) {
            event.setContentDescription(getDayDescription(virtualViewId));
        }

        @java.lang.Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, android.view.accessibility.AccessibilityNodeInfo node) {
            final boolean hasBounds = getBoundsForDay(virtualViewId, mTempRect);
            if (!hasBounds) {
                // The day is invalid, kill the node.
                mTempRect.setEmpty();
                node.setContentDescription("");
                node.setBoundsInParent(mTempRect);
                node.setVisibleToUser(false);
                return;
            }
            node.setText(getDayText(virtualViewId));
            node.setContentDescription(getDayDescription(virtualViewId));
            node.setBoundsInParent(mTempRect);
            final boolean isDayEnabled = isDayEnabled(virtualViewId);
            if (isDayEnabled) {
                node.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
            }
            node.setEnabled(isDayEnabled);
            if (virtualViewId == mActivatedDay) {
                // TODO: This should use activated once that's supported.
                node.setChecked(true);
            }
        }

        @java.lang.Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, android.os.Bundle arguments) {
            switch (action) {
                case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK :
                    return onDayClicked(virtualViewId);
            }
            return false;
        }

        /**
         * Generates a description for a given virtual view.
         *
         * @param id
         * 		the day to generate a description for
         * @return a description of the virtual view
         */
        private java.lang.CharSequence getDayDescription(int id) {
            if (isValidDayOfMonth(id)) {
                mTempCalendar.set(mYear, mMonth, id);
                return android.text.format.DateFormat.format(android.widget.SimpleMonthView.MonthViewTouchHelper.DATE_FORMAT, mTempCalendar.getTimeInMillis());
            }
            return "";
        }

        /**
         * Generates displayed text for a given virtual view.
         *
         * @param id
         * 		the day to generate text for
         * @return the visible text of the virtual view
         */
        private java.lang.CharSequence getDayText(int id) {
            if (isValidDayOfMonth(id)) {
                return mDayFormatter.format(id);
            }
            return null;
        }
    }

    /**
     * Handles callbacks when the user clicks on a time object.
     */
    public interface OnDayClickListener {
        void onDayClick(android.widget.SimpleMonthView view, android.icu.util.Calendar day);
    }
}

