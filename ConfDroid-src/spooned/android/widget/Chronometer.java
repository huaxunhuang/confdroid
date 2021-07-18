/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Class that implements a simple timer.
 * <p>
 * You can give it a start time in the {@link SystemClock#elapsedRealtime} timebase,
 * and it counts up from that, or if you don't give it a base time, it will use the
 * time at which you call {@link #start}.
 *
 * <p>The timer can also count downward towards the base time by
 * setting {@link #setCountDown(boolean)} to true.
 *
 *  <p>By default it will display the current
 * timer value in the form "MM:SS" or "H:MM:SS", or you can use {@link #setFormat}
 * to format the timer value into an arbitrary string.
 *
 * @unknown ref android.R.styleable#Chronometer_format
 * @unknown ref android.R.styleable#Chronometer_countDown
 */
@android.widget.RemoteViews.RemoteView
public class Chronometer extends android.widget.TextView {
    private static final java.lang.String TAG = "Chronometer";

    /**
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnChronometerTickListener {
        /**
         * Notification that the chronometer has changed.
         */
        void onChronometerTick(android.widget.Chronometer chronometer);
    }

    private long mBase;

    private long mNow;// the currently displayed time


    private boolean mVisible;

    private boolean mStarted;

    private boolean mRunning;

    private boolean mLogged;

    private java.lang.String mFormat;

    private java.util.Formatter mFormatter;

    private java.util.Locale mFormatterLocale;

    private java.lang.Object[] mFormatterArgs = new java.lang.Object[1];

    private java.lang.StringBuilder mFormatBuilder;

    private android.widget.Chronometer.OnChronometerTickListener mOnChronometerTickListener;

    private java.lang.StringBuilder mRecycle = new java.lang.StringBuilder(8);

    private boolean mCountDown;

    /**
     * Initialize this Chronometer object.
     * Sets the base to the current time.
     */
    public Chronometer(android.content.Context context) {
        this(context, null, 0);
    }

    /**
     * Initialize with standard view layout information.
     * Sets the base to the current time.
     */
    public Chronometer(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Initialize with standard view layout information and style.
     * Sets the base to the current time.
     */
    public Chronometer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Chronometer(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Chronometer, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.Chronometer, attrs, a, defStyleAttr, defStyleRes);
        setFormat(a.getString(R.styleable.Chronometer_format));
        setCountDown(a.getBoolean(R.styleable.Chronometer_countDown, false));
        a.recycle();
        init();
    }

    private void init() {
        mBase = android.os.SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    /**
     * Set this view to count down to the base instead of counting up from it.
     *
     * @param countDown
     * 		whether this view should count down
     * @see #setBase(long)
     */
    @android.view.RemotableViewMethod
    public void setCountDown(boolean countDown) {
        mCountDown = countDown;
        updateText(android.os.SystemClock.elapsedRealtime());
    }

    /**
     *
     *
     * @return whether this view counts down
     * @see #setCountDown(boolean)
     */
    @android.view.inspector.InspectableProperty
    public boolean isCountDown() {
        return mCountDown;
    }

    /**
     *
     *
     * @return whether this is the final countdown
     */
    public boolean isTheFinalCountDown() {
        try {
            getContext().startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://youtu.be/9jK-NcRmVcw")).addCategory(android.content.Intent.CATEGORY_BROWSABLE).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT | android.content.Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT));
            return true;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    /**
     * Set the time that the count-up timer is in reference to.
     *
     * @param base
     * 		Use the {@link SystemClock#elapsedRealtime} time base.
     */
    @android.view.RemotableViewMethod
    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(android.os.SystemClock.elapsedRealtime());
    }

    /**
     * Return the base time as set through {@link #setBase}.
     */
    public long getBase() {
        return mBase;
    }

    /**
     * Sets the format string used for display.  The Chronometer will display
     * this string, with the first "%s" replaced by the current timer value in
     * "MM:SS" or "H:MM:SS" form.
     *
     * If the format string is null, or if you never call setFormat(), the
     * Chronometer will simply display the timer value in "MM:SS" or "H:MM:SS"
     * form.
     *
     * @param format
     * 		the format string.
     */
    @android.view.RemotableViewMethod
    public void setFormat(java.lang.String format) {
        mFormat = format;
        if ((format != null) && (mFormatBuilder == null)) {
            mFormatBuilder = new java.lang.StringBuilder(format.length() * 2);
        }
    }

    /**
     * Returns the current format string as set through {@link #setFormat}.
     */
    @android.view.inspector.InspectableProperty
    public java.lang.String getFormat() {
        return mFormat;
    }

    /**
     * Sets the listener to be called when the chronometer changes.
     *
     * @param listener
     * 		The listener.
     */
    public void setOnChronometerTickListener(android.widget.Chronometer.OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    /**
     *
     *
     * @return The listener (may be null) that is listening for chronometer change
    events.
     */
    public android.widget.Chronometer.OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    /**
     * Start counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     *
     * Chronometer works by regularly scheduling messages to the handler, even when the
     * Widget is not visible.  To make sure resource leaks do not occur, the user should
     * make sure that each start() call has a reciprocal call to {@link #stop}.
     */
    public void start() {
        mStarted = true;
        updateRunning();
    }

    /**
     * Stop counting up.  This does not affect the base as set from {@link #setBase}, just
     * the view display.
     *
     * This stops the messages to the handler, effectively releasing resources that would
     * be held as the chronometer is running, via {@link #start}.
     */
    public void stop() {
        mStarted = false;
        updateRunning();
    }

    /**
     * The same as calling {@link #start} or {@link #stop}.
     *
     * @unknown pending API council approval
     */
    @android.view.RemotableViewMethod
    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @java.lang.Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == android.view.View.VISIBLE;
        updateRunning();
    }

    @java.lang.Override
    protected void onVisibilityChanged(android.view.View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        updateRunning();
    }

    private synchronized void updateText(long now) {
        mNow = now;
        long seconds = (mCountDown) ? mBase - now : now - mBase;
        seconds /= 1000;
        boolean negative = false;
        if (seconds < 0) {
            seconds = -seconds;
            negative = true;
        }
        java.lang.String text = android.text.format.DateUtils.formatElapsedTime(mRecycle, seconds);
        if (negative) {
            text = getResources().getString(R.string.negative_duration, text);
        }
        if (mFormat != null) {
            java.util.Locale loc = java.util.Locale.getDefault();
            if ((mFormatter == null) || (!loc.equals(mFormatterLocale))) {
                mFormatterLocale = loc;
                mFormatter = new java.util.Formatter(mFormatBuilder, loc);
            }
            mFormatBuilder.setLength(0);
            mFormatterArgs[0] = text;
            try {
                mFormatter.format(mFormat, mFormatterArgs);
                text = mFormatBuilder.toString();
            } catch (java.util.IllegalFormatException ex) {
                if (!mLogged) {
                    android.util.Log.w(android.widget.Chronometer.TAG, "Illegal format string: " + mFormat);
                    mLogged = true;
                }
            }
        }
        setText(text);
    }

    private void updateRunning() {
        boolean running = (mVisible && mStarted) && isShown();
        if (running != mRunning) {
            if (running) {
                updateText(android.os.SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                postDelayed(mTickRunnable, 1000);
            } else {
                removeCallbacks(mTickRunnable);
            }
            mRunning = running;
        }
    }

    private final java.lang.Runnable mTickRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (mRunning) {
                updateText(android.os.SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                postDelayed(mTickRunnable, 1000);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    private static final int MIN_IN_SEC = 60;

    private static final int HOUR_IN_SEC = android.widget.Chronometer.MIN_IN_SEC * 60;

    private static java.lang.String formatDuration(long ms) {
        int duration = ((int) (ms / android.text.format.DateUtils.SECOND_IN_MILLIS));
        if (duration < 0) {
            duration = -duration;
        }
        int h = 0;
        int m = 0;
        if (duration >= android.widget.Chronometer.HOUR_IN_SEC) {
            h = duration / android.widget.Chronometer.HOUR_IN_SEC;
            duration -= h * android.widget.Chronometer.HOUR_IN_SEC;
        }
        if (duration >= android.widget.Chronometer.MIN_IN_SEC) {
            m = duration / android.widget.Chronometer.MIN_IN_SEC;
            duration -= m * android.widget.Chronometer.MIN_IN_SEC;
        }
        final int s = duration;
        final java.util.ArrayList<android.icu.util.Measure> measures = new java.util.ArrayList<android.icu.util.Measure>();
        if (h > 0) {
            measures.add(new android.icu.util.Measure(h, android.icu.util.MeasureUnit.HOUR));
        }
        if (m > 0) {
            measures.add(new android.icu.util.Measure(m, android.icu.util.MeasureUnit.MINUTE));
        }
        measures.add(new android.icu.util.Measure(s, android.icu.util.MeasureUnit.SECOND));
        return android.icu.text.MeasureFormat.getInstance(java.util.Locale.getDefault(), FormatWidth.WIDE).formatMeasures(measures.toArray(new android.icu.util.Measure[measures.size()]));
    }

    @java.lang.Override
    public java.lang.CharSequence getContentDescription() {
        return android.widget.Chronometer.formatDuration(mNow - mBase);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.Chronometer.class.getName();
    }
}

