/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 *
 * @unknown ref android.R.styleable#AnalogClock_dial
 * @unknown ref android.R.styleable#AnalogClock_hand_hour
 * @unknown ref android.R.styleable#AnalogClock_hand_minute
 * @deprecated This widget is no longer supported.
 */
@android.widget.RemoteViews.RemoteView
@java.lang.Deprecated
public class AnalogClock extends android.view.View {
    private android.text.format.Time mCalendar;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mHourHand;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mMinuteHand;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mDial;

    private int mDialWidth;

    private int mDialHeight;

    private boolean mAttached;

    private float mMinutes;

    private float mHour;

    private boolean mChanged;

    public AnalogClock(android.content.Context context) {
        this(context, null);
    }

    public AnalogClock(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AnalogClock(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.Resources r = context.getResources();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.AnalogClock, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.AnalogClock, attrs, a, defStyleAttr, defStyleRes);
        mDial = a.getDrawable(com.android.internal.R.styleable.AnalogClock_dial);
        if (mDial == null) {
            mDial = context.getDrawable(com.android.internal.R.drawable.clock_dial);
        }
        mHourHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_hour);
        if (mHourHand == null) {
            mHourHand = context.getDrawable(com.android.internal.R.drawable.clock_hand_hour);
        }
        mMinuteHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_minute);
        if (mMinuteHand == null) {
            mMinuteHand = context.getDrawable(com.android.internal.R.drawable.clock_hand_minute);
        }
        mCalendar = new android.text.format.Time();
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction(android.content.Intent.ACTION_TIME_TICK);
            filter.addAction(android.content.Intent.ACTION_TIME_CHANGED);
            filter.addAction(android.content.Intent.ACTION_TIMEZONE_CHANGED);
            // OK, this is gross but needed. This class is supported by the
            // remote views machanism and as a part of that the remote views
            // can be inflated by a context for another user without the app
            // having interact users permission - just for loading resources.
            // For exmaple, when adding widgets from a user profile to the
            // home screen. Therefore, we register the receiver as the current
            // user not the one the context is for.
            getContext().registerReceiverAsUser(mIntentReceiver, android.widget.android.os.Process.myUserHandle(), filter, null, getHandler());
        }
        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.
        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new android.text.format.Time();
        // Make sure we update to the current time
        onTimeChanged();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.0F;
        float vScale = 1.0F;
        if ((widthMode != android.view.View.MeasureSpec.UNSPECIFIED) && (widthSize < mDialWidth)) {
            hScale = ((float) (widthSize)) / ((float) (mDialWidth));
        }
        if ((heightMode != android.view.View.MeasureSpec.UNSPECIFIED) && (heightSize < mDialHeight)) {
            vScale = ((float) (heightSize)) / ((float) (mDialHeight));
        }
        float scale = java.lang.Math.min(hScale, vScale);
        setMeasuredDimension(android.view.View.resolveSizeAndState(((int) (mDialWidth * scale)), widthMeasureSpec, 0), android.view.View.resolveSizeAndState(((int) (mDialHeight * scale)), heightMeasureSpec, 0));
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        int availableWidth = mRight - mLeft;
        int availableHeight = mBottom - mTop;
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final android.graphics.drawable.Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;
        if ((availableWidth < w) || (availableHeight < h)) {
            scaled = true;
            float scale = java.lang.Math.min(((float) (availableWidth)) / ((float) (w)), ((float) (availableHeight)) / ((float) (h)));
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }
        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate((mHour / 12.0F) * 360.0F, x, y);
        final android.graphics.drawable.Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate((mMinutes / 60.0F) * 360.0F, x, y);
        final android.graphics.drawable.Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() {
        mCalendar.setToNow();
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        mMinutes = minute + (second / 60.0F);
        mHour = hour + (mMinutes / 60.0F);
        mChanged = true;
        updateContentDescription(mCalendar);
    }

    private final android.content.BroadcastReceiver mIntentReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals(android.content.Intent.ACTION_TIMEZONE_CHANGED)) {
                java.lang.String tz = intent.getStringExtra("time-zone");
                mCalendar = new android.text.format.Time(java.util.TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
            invalidate();
        }
    };

    private void updateContentDescription(android.text.format.Time time) {
        final int flags = android.text.format.DateUtils.FORMAT_SHOW_TIME | android.text.format.DateUtils.FORMAT_24HOUR;
        java.lang.String contentDescription = android.text.format.DateUtils.formatDateTime(mContext, time.toMillis(false), flags);
        setContentDescription(contentDescription);
    }
}

