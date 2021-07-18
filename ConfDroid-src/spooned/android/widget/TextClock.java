/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * <p><code>TextClock</code> can display the current date and/or time as
 * a formatted string.</p>
 *
 * <p>This view honors the 24-hour format system setting. As such, it is
 * possible and recommended to provide two different formatting patterns:
 * one to display the date/time in 24-hour mode and one to display the
 * date/time in 12-hour mode. Most callers will want to use the defaults,
 * though, which will be appropriate for the user's locale.</p>
 *
 * <p>It is possible to determine whether the system is currently in
 * 24-hour mode by calling {@link #is24HourModeEnabled()}.</p>
 *
 * <p>The rules used by this widget to decide how to format the date and
 * time are the following:</p>
 * <ul>
 *     <li>In 24-hour mode:
 *         <ul>
 *             <li>Use the value returned by {@link #getFormat24Hour()} when non-null</li>
 *             <li>Otherwise, use the value returned by {@link #getFormat12Hour()} when non-null</li>
 *             <li>Otherwise, use a default value appropriate for the user's locale, such as {@code h:mm a}</li>
 *         </ul>
 *     </li>
 *     <li>In 12-hour mode:
 *         <ul>
 *             <li>Use the value returned by {@link #getFormat12Hour()} when non-null</li>
 *             <li>Otherwise, use the value returned by {@link #getFormat24Hour()} when non-null</li>
 *             <li>Otherwise, use a default value appropriate for the user's locale, such as {@code HH:mm}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <p>The {@link CharSequence} instances used as formatting patterns when calling either
 * {@link #setFormat24Hour(CharSequence)} or {@link #setFormat12Hour(CharSequence)} can
 * contain styling information. To do so, use a {@link android.text.Spanned} object.
 * Note that if you customize these strings, it is your responsibility to supply strings
 * appropriate for formatting dates and/or times in the user's locale.</p>
 *
 * @unknown ref android.R.styleable#TextClock_format12Hour
 * @unknown ref android.R.styleable#TextClock_format24Hour
 * @unknown ref android.R.styleable#TextClock_timeZone
 */
@android.widget.RemoteViews.RemoteView
public class TextClock extends android.widget.TextView {
    /**
     * The default formatting pattern in 12-hour mode. This pattern is used
     * if {@link #setFormat12Hour(CharSequence)} is called with a null pattern
     * or if no pattern was specified when creating an instance of this class.
     *
     * This default pattern shows only the time, hours and minutes, and an am/pm
     * indicator.
     *
     * @see #setFormat12Hour(CharSequence)
     * @see #getFormat12Hour()
     * @deprecated Let the system use locale-appropriate defaults instead.
     */
    @java.lang.Deprecated
    public static final java.lang.CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";

    /**
     * The default formatting pattern in 24-hour mode. This pattern is used
     * if {@link #setFormat24Hour(CharSequence)} is called with a null pattern
     * or if no pattern was specified when creating an instance of this class.
     *
     * This default pattern shows only the time, hours and minutes.
     *
     * @see #setFormat24Hour(CharSequence)
     * @see #getFormat24Hour()
     * @deprecated Let the system use locale-appropriate defaults instead.
     */
    @java.lang.Deprecated
    public static final java.lang.CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";

    private java.lang.CharSequence mFormat12;

    private java.lang.CharSequence mFormat24;

    private java.lang.CharSequence mDescFormat12;

    private java.lang.CharSequence mDescFormat24;

    @android.view.ViewDebug.ExportedProperty
    private java.lang.CharSequence mFormat;

    @android.view.ViewDebug.ExportedProperty
    private boolean mHasSeconds;

    private java.lang.CharSequence mDescFormat;

    private boolean mRegistered;

    private boolean mShouldRunTicker;

    private java.util.Calendar mTime;

    private java.lang.String mTimeZone;

    private boolean mShowCurrentUserTime;

    private android.database.ContentObserver mFormatChangeObserver;

    // Used by tests to stop time change events from triggering the text update
    private boolean mStopTicking;

    private class FormatChangeObserver extends android.database.ContentObserver {
        public FormatChangeObserver(android.os.Handler handler) {
            super(handler);
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            chooseFormat();
            onTimeChanged();
        }

        @java.lang.Override
        public void onChange(boolean selfChange, android.net.Uri uri) {
            chooseFormat();
            onTimeChanged();
        }
    }

    private final android.content.BroadcastReceiver mIntentReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (mStopTicking) {
                return;// Test disabled the clock ticks

            }
            if ((mTimeZone == null) && android.content.Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                final java.lang.String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            } else
                if ((!mShouldRunTicker) && (android.content.Intent.ACTION_TIME_TICK.equals(intent.getAction()) || android.content.Intent.ACTION_TIME_CHANGED.equals(intent.getAction()))) {
                    return;
                }

            onTimeChanged();
        }
    };

    private final java.lang.Runnable mTicker = new java.lang.Runnable() {
        public void run() {
            if (mStopTicking) {
                return;// Test disabled the clock ticks

            }
            onTimeChanged();
            long now = android.os.SystemClock.uptimeMillis();
            long next = now + (1000 - (now % 1000));
            getHandler().postAtTime(mTicker, next);
        }
    };

    /**
     * Creates a new clock using the default patterns for the current locale.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     */
    @java.lang.SuppressWarnings("UnusedDeclaration")
    public TextClock(android.content.Context context) {
        super(context);
        init();
    }

    /**
     * Creates a new clock inflated from XML. This object's properties are
     * intialized from the attributes specified in XML.
     *
     * This constructor uses a default style of 0, so the only attribute values
     * applied are those in the Context's Theme and the given AttributeSet.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view
     */
    @java.lang.SuppressWarnings("UnusedDeclaration")
    public TextClock(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Creates a new clock inflated from XML. This object's properties are
     * intialized from the attributes specified in XML.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param attrs
     * 		The attributes of the XML tag that is inflating the view
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public TextClock(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextClock(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextClock, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.TextClock, attrs, a, defStyleAttr, defStyleRes);
        try {
            mFormat12 = a.getText(R.styleable.TextClock_format12Hour);
            mFormat24 = a.getText(R.styleable.TextClock_format24Hour);
            mTimeZone = a.getString(R.styleable.TextClock_timeZone);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        if ((mFormat12 == null) || (mFormat24 == null)) {
            libcore.icu.LocaleData ld = libcore.icu.LocaleData.get(getContext().getResources().getConfiguration().locale);
            if (mFormat12 == null) {
                mFormat12 = ld.timeFormat_hm;
            }
            if (mFormat24 == null) {
                mFormat24 = ld.timeFormat_Hm;
            }
        }
        createTime(mTimeZone);
        chooseFormat();
    }

    private void createTime(java.lang.String timeZone) {
        if (timeZone != null) {
            mTime = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone));
        } else {
            mTime = java.util.Calendar.getInstance();
        }
    }

    /**
     * Returns the formatting pattern used to display the date and/or time
     * in 12-hour mode. The formatting pattern syntax is described in
     * {@link DateFormat}.
     *
     * @return A {@link CharSequence} or null.
     * @see #setFormat12Hour(CharSequence)
     * @see #is24HourModeEnabled()
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty
    public java.lang.CharSequence getFormat12Hour() {
        return mFormat12;
    }

    /**
     * <p>Specifies the formatting pattern used to display the date and/or time
     * in 12-hour mode. The formatting pattern syntax is described in
     * {@link DateFormat}.</p>
     *
     * <p>If this pattern is set to null, {@link #getFormat24Hour()} will be used
     * even in 12-hour mode. If both 24-hour and 12-hour formatting patterns
     * are set to null, the default pattern for the current locale will be used
     * instead.</p>
     *
     * <p><strong>Note:</strong> if styling is not needed, it is highly recommended
     * you supply a format string generated by
     * {@link DateFormat#getBestDateTimePattern(java.util.Locale, String)}. This method
     * takes care of generating a format string adapted to the desired locale.</p>
     *
     * @param format
     * 		A date/time formatting pattern as described in {@link DateFormat}
     * @see #getFormat12Hour()
     * @see #is24HourModeEnabled()
     * @see DateFormat#getBestDateTimePattern(java.util.Locale, String)
     * @see DateFormat
     * @unknown ref android.R.styleable#TextClock_format12Hour
     */
    @android.view.RemotableViewMethod
    public void setFormat12Hour(java.lang.CharSequence format) {
        mFormat12 = format;
        chooseFormat();
        onTimeChanged();
    }

    /**
     * Like setFormat12Hour, but for the content description.
     *
     * @unknown 
     */
    public void setContentDescriptionFormat12Hour(java.lang.CharSequence format) {
        mDescFormat12 = format;
        chooseFormat();
        onTimeChanged();
    }

    /**
     * Returns the formatting pattern used to display the date and/or time
     * in 24-hour mode. The formatting pattern syntax is described in
     * {@link DateFormat}.
     *
     * @return A {@link CharSequence} or null.
     * @see #setFormat24Hour(CharSequence)
     * @see #is24HourModeEnabled()
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty
    public java.lang.CharSequence getFormat24Hour() {
        return mFormat24;
    }

    /**
     * <p>Specifies the formatting pattern used to display the date and/or time
     * in 24-hour mode. The formatting pattern syntax is described in
     * {@link DateFormat}.</p>
     *
     * <p>If this pattern is set to null, {@link #getFormat24Hour()} will be used
     * even in 12-hour mode. If both 24-hour and 12-hour formatting patterns
     * are set to null, the default pattern for the current locale will be used
     * instead.</p>
     *
     * <p><strong>Note:</strong> if styling is not needed, it is highly recommended
     * you supply a format string generated by
     * {@link DateFormat#getBestDateTimePattern(java.util.Locale, String)}. This method
     * takes care of generating a format string adapted to the desired locale.</p>
     *
     * @param format
     * 		A date/time formatting pattern as described in {@link DateFormat}
     * @see #getFormat24Hour()
     * @see #is24HourModeEnabled()
     * @see DateFormat#getBestDateTimePattern(java.util.Locale, String)
     * @see DateFormat
     * @unknown ref android.R.styleable#TextClock_format24Hour
     */
    @android.view.RemotableViewMethod
    public void setFormat24Hour(java.lang.CharSequence format) {
        mFormat24 = format;
        chooseFormat();
        onTimeChanged();
    }

    /**
     * Like setFormat24Hour, but for the content description.
     *
     * @unknown 
     */
    public void setContentDescriptionFormat24Hour(java.lang.CharSequence format) {
        mDescFormat24 = format;
        chooseFormat();
        onTimeChanged();
    }

    /**
     * Sets whether this clock should always track the current user and not the user of the
     * current process. This is used for single instance processes like the systemUI who need
     * to display time for different users.
     *
     * @unknown 
     */
    public void setShowCurrentUserTime(boolean showCurrentUserTime) {
        mShowCurrentUserTime = showCurrentUserTime;
        chooseFormat();
        onTimeChanged();
        unregisterObserver();
        registerObserver();
    }

    /**
     * Update the displayed time if necessary and invalidate the view.
     *
     * @unknown 
     */
    public void refresh() {
        onTimeChanged();
        invalidate();
    }

    /**
     * Indicates whether the system is currently using the 24-hour mode.
     *
     * When the system is in 24-hour mode, this view will use the pattern
     * returned by {@link #getFormat24Hour()}. In 12-hour mode, the pattern
     * returned by {@link #getFormat12Hour()} is used instead.
     *
     * If either one of the formats is null, the other format is used. If
     * both formats are null, the default formats for the current locale are used.
     *
     * @return true if time should be displayed in 24-hour format, false if it
    should be displayed in 12-hour format.
     * @see #setFormat12Hour(CharSequence)
     * @see #getFormat12Hour()
     * @see #setFormat24Hour(CharSequence)
     * @see #getFormat24Hour()
     */
    @android.view.inspector.InspectableProperty(hasAttributeId = false)
    public boolean is24HourModeEnabled() {
        if (mShowCurrentUserTime) {
            return android.text.format.DateFormat.is24HourFormat(getContext(), android.app.ActivityManager.getCurrentUser());
        } else {
            return android.text.format.DateFormat.is24HourFormat(getContext());
        }
    }

    /**
     * Indicates which time zone is currently used by this view.
     *
     * @return The ID of the current time zone or null if the default time zone,
    as set by the user, must be used
     * @see TimeZone
     * @see java.util.TimeZone#getAvailableIDs()
     * @see #setTimeZone(String)
     */
    @android.view.inspector.InspectableProperty
    public java.lang.String getTimeZone() {
        return mTimeZone;
    }

    /**
     * Sets the specified time zone to use in this clock. When the time zone
     * is set through this method, system time zone changes (when the user
     * sets the time zone in settings for instance) will be ignored.
     *
     * @param timeZone
     * 		The desired time zone's ID as specified in {@link TimeZone}
     * 		or null to user the time zone specified by the user
     * 		(system time zone)
     * @see #getTimeZone()
     * @see java.util.TimeZone#getAvailableIDs()
     * @see TimeZone#getTimeZone(String)
     * @unknown ref android.R.styleable#TextClock_timeZone
     */
    @android.view.RemotableViewMethod
    public void setTimeZone(java.lang.String timeZone) {
        mTimeZone = timeZone;
        createTime(timeZone);
        onTimeChanged();
    }

    /**
     * Returns the current format string. Always valid after constructor has
     * finished, and will never be {@code null}.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public java.lang.CharSequence getFormat() {
        return mFormat;
    }

    /**
     * Selects either one of {@link #getFormat12Hour()} or {@link #getFormat24Hour()}
     * depending on whether the user has selected 24-hour format.
     */
    private void chooseFormat() {
        final boolean format24Requested = is24HourModeEnabled();
        libcore.icu.LocaleData ld = libcore.icu.LocaleData.get(getContext().getResources().getConfiguration().locale);
        if (format24Requested) {
            mFormat = android.widget.TextClock.abc(mFormat24, mFormat12, ld.timeFormat_Hm);
            mDescFormat = android.widget.TextClock.abc(mDescFormat24, mDescFormat12, mFormat);
        } else {
            mFormat = android.widget.TextClock.abc(mFormat12, mFormat24, ld.timeFormat_hm);
            mDescFormat = android.widget.TextClock.abc(mDescFormat12, mDescFormat24, mFormat);
        }
        boolean hadSeconds = mHasSeconds;
        mHasSeconds = android.text.format.DateFormat.hasSeconds(mFormat);
        if (mShouldRunTicker && (hadSeconds != mHasSeconds)) {
            if (hadSeconds)
                getHandler().removeCallbacks(mTicker);
            else
                mTicker.run();

        }
    }

    /**
     * Returns a if not null, else return b if not null, else return c.
     */
    private static java.lang.CharSequence abc(java.lang.CharSequence a, java.lang.CharSequence b, java.lang.CharSequence c) {
        return a == null ? b == null ? c : b : a;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mRegistered) {
            mRegistered = true;
            registerReceiver();
            registerObserver();
            createTime(mTimeZone);
        }
    }

    @java.lang.Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if ((!mShouldRunTicker) && isVisible) {
            mShouldRunTicker = true;
            if (mHasSeconds) {
                mTicker.run();
            } else {
                onTimeChanged();
            }
        } else
            if (mShouldRunTicker && (!isVisible)) {
                mShouldRunTicker = false;
                getHandler().removeCallbacks(mTicker);
            }

    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRegistered) {
            unregisterReceiver();
            unregisterObserver();
            mRegistered = false;
        }
    }

    /**
     * Used by tests to stop the clock tick from updating the text.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void disableClockTick() {
        mStopTicking = true;
    }

    private void registerReceiver() {
        final android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(android.content.Intent.ACTION_TIME_TICK);
        filter.addAction(android.content.Intent.ACTION_TIME_CHANGED);
        filter.addAction(android.content.Intent.ACTION_TIMEZONE_CHANGED);
        // OK, this is gross but needed. This class is supported by the
        // remote views mechanism and as a part of that the remote views
        // can be inflated by a context for another user without the app
        // having interact users permission - just for loading resources.
        // For example, when adding widgets from a managed profile to the
        // home screen. Therefore, we register the receiver as the user
        // the app is running as not the one the context is for.
        getContext().registerReceiverAsUser(mIntentReceiver, android.widget.android.os.Process.myUserHandle(), filter, null, getHandler());
    }

    private void registerObserver() {
        if (mRegistered) {
            if (mFormatChangeObserver == null) {
                mFormatChangeObserver = new android.widget.TextClock.FormatChangeObserver(getHandler());
            }
            final android.content.ContentResolver resolver = getContext().getContentResolver();
            android.net.Uri uri = Settings.System.getUriFor(Settings.System.TIME_12_24);
            if (mShowCurrentUserTime) {
                resolver.registerContentObserver(uri, true, mFormatChangeObserver, UserHandle.USER_ALL);
            } else {
                // UserHandle.myUserId() is needed. This class is supported by the
                // remote views mechanism and as a part of that the remote views
                // can be inflated by a context for another user without the app
                // having interact users permission - just for loading resources.
                // For example, when adding widgets from a managed profile to the
                // home screen. Therefore, we register the ContentObserver with the user
                // the app is running (e.g. the launcher) and not the user of the
                // context (e.g. the widget's profile).
                resolver.registerContentObserver(uri, true, mFormatChangeObserver, android.os.UserHandle.myUserId());
            }
        }
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }

    private void unregisterObserver() {
        if (mFormatChangeObserver != null) {
            final android.content.ContentResolver resolver = getContext().getContentResolver();
            resolver.unregisterContentObserver(mFormatChangeObserver);
        }
    }

    /**
     * Update the displayed time if this view and its ancestors and window is visible
     */
    @android.annotation.UnsupportedAppUsage
    private void onTimeChanged() {
        mTime.setTimeInMillis(java.lang.System.currentTimeMillis());
        setText(android.text.format.DateFormat.format(mFormat, mTime));
        setContentDescription(android.text.format.DateFormat.format(mDescFormat, mTime));
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        java.lang.CharSequence s = getFormat12Hour();
        stream.addProperty("format12Hour", s == null ? null : s.toString());
        s = getFormat24Hour();
        stream.addProperty("format24Hour", s == null ? null : s.toString());
        stream.addProperty("format", mFormat == null ? null : mFormat.toString());
        stream.addProperty("hasSeconds", mHasSeconds);
    }
}

