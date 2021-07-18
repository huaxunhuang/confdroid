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
 * Like AnalogClock, but digital.
 *
 * @deprecated It is recommended you use {@link TextClock} instead.
 */
@java.lang.Deprecated
public class DigitalClock extends android.widget.TextView {
    // FIXME: implement separate views for hours/minutes/seconds, so
    // proportional fonts don't shake rendering
    java.util.Calendar mCalendar;

    // We must keep a reference to this observer
    @java.lang.SuppressWarnings("FieldCanBeLocal")
    private android.widget.DigitalClock.FormatChangeObserver mFormatChangeObserver;

    private java.lang.Runnable mTicker;

    private android.os.Handler mHandler;

    private boolean mTickerStopped = false;

    java.lang.String mFormat;

    public DigitalClock(android.content.Context context) {
        super(context);
        initClock();
    }

    public DigitalClock(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        initClock();
    }

    private void initClock() {
        if (mCalendar == null) {
            mCalendar = java.util.Calendar.getInstance();
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mFormatChangeObserver = new android.widget.DigitalClock.FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();
        mHandler = new android.os.Handler();
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new java.lang.Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;

                mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
                setText(android.text.format.DateFormat.format(mFormat, mCalendar));
                invalidate();
                long now = android.os.SystemClock.uptimeMillis();
                long next = now + (1000 - (now % 1000));
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
        getContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
    }

    private void setFormat() {
        mFormat = android.text.format.DateFormat.getTimeFormatString(getContext());
    }

    private class FormatChangeObserver extends android.database.ContentObserver {
        public FormatChangeObserver() {
            super(new android.os.Handler());
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        // noinspection deprecation
        return android.widget.DigitalClock.class.getName();
    }
}

