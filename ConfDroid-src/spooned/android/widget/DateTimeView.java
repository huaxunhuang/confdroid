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


// 
// TODO
// - listen for the next threshold time to update the view.
// - listen for date format pref changed
// - put the AM/PM in a smaller font
// 
/**
 * Displays a given time in a convenient human-readable foramt.
 *
 * @unknown 
 */
@android.widget.RemoteViews.RemoteView
public class DateTimeView extends android.widget.TextView {
    private static final int SHOW_TIME = 0;

    private static final int SHOW_MONTH_DAY_YEAR = 1;

    java.util.Date mTime;

    long mTimeMillis;

    int mLastDisplay = -1;

    java.text.DateFormat mLastFormat;

    private long mUpdateTimeMillis;

    private static final java.lang.ThreadLocal<android.widget.DateTimeView.ReceiverInfo> sReceiverInfo = new java.lang.ThreadLocal<android.widget.DateTimeView.ReceiverInfo>();

    private java.lang.String mNowText;

    private boolean mShowRelativeTime;

    public DateTimeView(android.content.Context context) {
        this(context, null);
    }

    @android.annotation.UnsupportedAppUsage
    public DateTimeView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.DateTimeView, 0, 0);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DateTimeView_showRelative :
                    boolean relative = a.getBoolean(i, false);
                    setShowRelativeTime(relative);
                    break;
            }
        }
        a.recycle();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        android.widget.DateTimeView.ReceiverInfo ri = android.widget.DateTimeView.sReceiverInfo.get();
        if (ri == null) {
            ri = new android.widget.DateTimeView.ReceiverInfo();
            android.widget.DateTimeView.sReceiverInfo.set(ri);
        }
        ri.addView(this);
        // The view may not be added to the view hierarchy immediately right after setTime()
        // is called which means it won't get any update from intents before being added.
        // In such case, the view might show the incorrect relative time after being added to the
        // view hierarchy until the next update intent comes.
        // So we update the time here if mShowRelativeTime is enabled to prevent this case.
        if (mShowRelativeTime) {
            update();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final android.widget.DateTimeView.ReceiverInfo ri = android.widget.DateTimeView.sReceiverInfo.get();
        if (ri != null) {
            ri.removeView(this);
        }
    }

    @android.view.RemotableViewMethod
    @android.annotation.UnsupportedAppUsage
    public void setTime(long time) {
        android.text.format.Time t = new android.text.format.Time();
        t.set(time);
        mTimeMillis = t.toMillis(false);
        mTime = new java.util.Date(t.year - 1900, t.month, t.monthDay, t.hour, t.minute, 0);
        update();
    }

    @android.view.RemotableViewMethod
    public void setShowRelativeTime(boolean showRelativeTime) {
        mShowRelativeTime = showRelativeTime;
        updateNowText();
        update();
    }

    /**
     * Returns whether this view shows relative time
     *
     * @return True if it shows relative time, false otherwise
     */
    @android.view.inspector.InspectableProperty(name = "showReleative", hasAttributeId = false)
    public boolean isShowRelativeTime() {
        return mShowRelativeTime;
    }

    @java.lang.Override
    @android.view.RemotableViewMethod
    public void setVisibility(@android.view.View.Visibility
    int visibility) {
        boolean gotVisible = (visibility != android.view.View.GONE) && (getVisibility() == android.view.View.GONE);
        super.setVisibility(visibility);
        if (gotVisible) {
            update();
        }
    }

    @android.annotation.UnsupportedAppUsage
    void update() {
        if ((mTime == null) || (getVisibility() == android.view.View.GONE)) {
            return;
        }
        if (mShowRelativeTime) {
            updateRelativeTime();
            return;
        }
        int display;
        java.util.Date time = mTime;
        android.text.format.Time t = new android.text.format.Time();
        t.set(mTimeMillis);
        t.second = 0;
        t.hour -= 12;
        long twelveHoursBefore = t.toMillis(false);
        t.hour += 12;
        long twelveHoursAfter = t.toMillis(false);
        t.hour = 0;
        t.minute = 0;
        long midnightBefore = t.toMillis(false);
        t.monthDay++;
        long midnightAfter = t.toMillis(false);
        long nowMillis = java.lang.System.currentTimeMillis();
        t.set(nowMillis);
        t.second = 0;
        nowMillis = t.normalize(false);
        // Choose the display mode
        choose_display : {
            if (((nowMillis >= midnightBefore) && (nowMillis < midnightAfter)) || ((nowMillis >= twelveHoursBefore) && (nowMillis < twelveHoursAfter))) {
                display = android.widget.DateTimeView.SHOW_TIME;
                break choose_display;
            }
            // Else, show month day and year.
            display = android.widget.DateTimeView.SHOW_MONTH_DAY_YEAR;
            break choose_display;
        }
        // Choose the format
        java.text.DateFormat format;
        if ((display == mLastDisplay) && (mLastFormat != null)) {
            // use cached format
            format = mLastFormat;
        } else {
            switch (display) {
                case android.widget.DateTimeView.SHOW_TIME :
                    format = getTimeFormat();
                    break;
                case android.widget.DateTimeView.SHOW_MONTH_DAY_YEAR :
                    format = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
                    break;
                default :
                    throw new java.lang.RuntimeException("unknown display value: " + display);
            }
            mLastFormat = format;
        }
        // Set the text
        java.lang.String text = format.format(mTime);
        setText(text);
        // Schedule the next update
        if (display == android.widget.DateTimeView.SHOW_TIME) {
            // Currently showing the time, update at the later of twelve hours after or midnight.
            mUpdateTimeMillis = (twelveHoursAfter > midnightAfter) ? twelveHoursAfter : midnightAfter;
        } else {
            // Currently showing the date
            if (mTimeMillis < nowMillis) {
                // If the time is in the past, don't schedule an update
                mUpdateTimeMillis = 0;
            } else {
                // If hte time is in the future, schedule one at the earlier of twelve hours
                // before or midnight before.
                mUpdateTimeMillis = (twelveHoursBefore < midnightBefore) ? twelveHoursBefore : midnightBefore;
            }
        }
    }

    private void updateRelativeTime() {
        long now = java.lang.System.currentTimeMillis();
        long duration = java.lang.Math.abs(now - mTimeMillis);
        int count;
        long millisIncrease;
        boolean past = now >= mTimeMillis;
        java.lang.String result;
        if (duration < MINUTE_IN_MILLIS) {
            setText(mNowText);
            mUpdateTimeMillis = (mTimeMillis + MINUTE_IN_MILLIS) + 1;
            return;
        } else
            if (duration < HOUR_IN_MILLIS) {
                count = ((int) (duration / MINUTE_IN_MILLIS));
                result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_minutes_shortest : com.android.internal.R.plurals.duration_minutes_shortest_future, count), count);
                millisIncrease = MINUTE_IN_MILLIS;
            } else
                if (duration < DAY_IN_MILLIS) {
                    count = ((int) (duration / HOUR_IN_MILLIS));
                    result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_hours_shortest : com.android.internal.R.plurals.duration_hours_shortest_future, count), count);
                    millisIncrease = HOUR_IN_MILLIS;
                } else
                    if (duration < YEAR_IN_MILLIS) {
                        // In weird cases it can become 0 because of daylight savings
                        java.util.TimeZone timeZone = java.util.TimeZone.getDefault();
                        count = java.lang.Math.max(java.lang.Math.abs(android.widget.DateTimeView.dayDistance(timeZone, mTimeMillis, now)), 1);
                        result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_days_shortest : com.android.internal.R.plurals.duration_days_shortest_future, count), count);
                        if (past || (count != 1)) {
                            mUpdateTimeMillis = computeNextMidnight(timeZone);
                            millisIncrease = -1;
                        } else {
                            millisIncrease = DAY_IN_MILLIS;
                        }
                    } else {
                        count = ((int) (duration / YEAR_IN_MILLIS));
                        result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_years_shortest : com.android.internal.R.plurals.duration_years_shortest_future, count), count);
                        millisIncrease = YEAR_IN_MILLIS;
                    }



        if (millisIncrease != (-1)) {
            if (past) {
                mUpdateTimeMillis = (mTimeMillis + (millisIncrease * (count + 1))) + 1;
            } else {
                mUpdateTimeMillis = (mTimeMillis - (millisIncrease * count)) + 1;
            }
        }
        setText(result);
    }

    /**
     *
     *
     * @param timeZone
     * 		the timezone we are in
     * @return the timepoint in millis at UTC at midnight in the current timezone
     */
    private long computeNextMidnight(java.util.TimeZone timeZone) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeZone(timeZone);
        c.add(java.util.Calendar.DAY_OF_MONTH, 1);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateNowText();
        update();
    }

    private void updateNowText() {
        if (!mShowRelativeTime) {
            return;
        }
        mNowText = getContext().getResources().getString(com.android.internal.R.string.now_string_shortest);
    }

    // Return the date difference for the two times in a given timezone.
    private static int dayDistance(java.util.TimeZone timeZone, long startTime, long endTime) {
        return getJulianDay(endTime, timeZone.getOffset(endTime) / 1000) - getJulianDay(startTime, timeZone.getOffset(startTime) / 1000);
    }

    private java.text.DateFormat getTimeFormat() {
        return android.text.format.DateFormat.getTimeFormat(getContext());
    }

    void clearFormatAndUpdate() {
        mLastFormat = null;
        update();
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (mShowRelativeTime) {
            // The short version of the time might not be completely understandable and for
            // accessibility we rather have a longer version.
            long now = java.lang.System.currentTimeMillis();
            long duration = java.lang.Math.abs(now - mTimeMillis);
            int count;
            boolean past = now >= mTimeMillis;
            java.lang.String result;
            if (duration < MINUTE_IN_MILLIS) {
                result = mNowText;
            } else
                if (duration < HOUR_IN_MILLIS) {
                    count = ((int) (duration / MINUTE_IN_MILLIS));
                    result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_minutes_relative : com.android.internal.R.plurals.duration_minutes_relative_future, count), count);
                } else
                    if (duration < DAY_IN_MILLIS) {
                        count = ((int) (duration / HOUR_IN_MILLIS));
                        result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_hours_relative : com.android.internal.R.plurals.duration_hours_relative_future, count), count);
                    } else
                        if (duration < YEAR_IN_MILLIS) {
                            // In weird cases it can become 0 because of daylight savings
                            java.util.TimeZone timeZone = java.util.TimeZone.getDefault();
                            count = java.lang.Math.max(java.lang.Math.abs(android.widget.DateTimeView.dayDistance(timeZone, mTimeMillis, now)), 1);
                            result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_days_relative : com.android.internal.R.plurals.duration_days_relative_future, count), count);
                        } else {
                            count = ((int) (duration / YEAR_IN_MILLIS));
                            result = java.lang.String.format(getContext().getResources().getQuantityString(past ? com.android.internal.R.plurals.duration_years_relative : com.android.internal.R.plurals.duration_years_relative_future, count), count);
                        }



            info.setText(result);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setReceiverHandler(android.os.Handler handler) {
        android.widget.DateTimeView.ReceiverInfo ri = android.widget.DateTimeView.sReceiverInfo.get();
        if (ri == null) {
            ri = new android.widget.DateTimeView.ReceiverInfo();
            android.widget.DateTimeView.sReceiverInfo.set(ri);
        }
        ri.setHandler(handler);
    }

    private static class ReceiverInfo {
        private final java.util.ArrayList<android.widget.DateTimeView> mAttachedViews = new java.util.ArrayList<android.widget.DateTimeView>();

        private final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() {
            @java.lang.Override
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if (android.content.Intent.ACTION_TIME_TICK.equals(action)) {
                    if (java.lang.System.currentTimeMillis() < getSoonestUpdateTime()) {
                        // The update() function takes a few milliseconds to run because of
                        // all of the time conversions it needs to do, so we can't do that
                        // every minute.
                        return;
                    }
                }
                // ACTION_TIME_CHANGED can also signal a change of 12/24 hr. format.
                updateAll();
            }
        };

        private final android.database.ContentObserver mObserver = new android.database.ContentObserver(new android.os.Handler()) {
            @java.lang.Override
            public void onChange(boolean selfChange) {
                updateAll();
            }
        };

        private android.os.Handler mHandler = new android.os.Handler();

        public void addView(android.widget.DateTimeView v) {
            synchronized(mAttachedViews) {
                final boolean register = mAttachedViews.isEmpty();
                mAttachedViews.add(v);
                if (register) {
                    register(android.widget.DateTimeView.ReceiverInfo.getApplicationContextIfAvailable(v.getContext()));
                }
            }
        }

        public void removeView(android.widget.DateTimeView v) {
            synchronized(mAttachedViews) {
                final boolean removed = mAttachedViews.remove(v);
                // Only unregister once when we remove the last view in the list otherwise we risk
                // trying to unregister a receiver that is no longer registered.
                if (removed && mAttachedViews.isEmpty()) {
                    unregister(android.widget.DateTimeView.ReceiverInfo.getApplicationContextIfAvailable(v.getContext()));
                }
            }
        }

        void updateAll() {
            synchronized(mAttachedViews) {
                final int count = mAttachedViews.size();
                for (int i = 0; i < count; i++) {
                    android.widget.DateTimeView view = mAttachedViews.get(i);
                    view.post(() -> view.clearFormatAndUpdate());
                }
            }
        }

        long getSoonestUpdateTime() {
            long result = java.lang.Long.MAX_VALUE;
            synchronized(mAttachedViews) {
                final int count = mAttachedViews.size();
                for (int i = 0; i < count; i++) {
                    final long time = mAttachedViews.get(i).mUpdateTimeMillis;
                    if (time < result) {
                        result = time;
                    }
                }
            }
            return result;
        }

        static final android.content.Context getApplicationContextIfAvailable(android.content.Context context) {
            final android.content.Context ac = context.getApplicationContext();
            return ac != null ? ac : android.app.ActivityThread.currentApplication().getApplicationContext();
        }

        void register(android.content.Context context) {
            final android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction(android.content.Intent.ACTION_TIME_TICK);
            filter.addAction(android.content.Intent.ACTION_TIME_CHANGED);
            filter.addAction(android.content.Intent.ACTION_CONFIGURATION_CHANGED);
            filter.addAction(android.content.Intent.ACTION_TIMEZONE_CHANGED);
            context.registerReceiver(mReceiver, filter, null, mHandler);
        }

        void unregister(android.content.Context context) {
            context.unregisterReceiver(mReceiver);
        }

        public void setHandler(android.os.Handler handler) {
            mHandler = handler;
            synchronized(mAttachedViews) {
                if (!mAttachedViews.isEmpty()) {
                    unregister(mAttachedViews.get(0).getContext());
                    register(mAttachedViews.get(0).getContext());
                }
            }
        }
    }
}

