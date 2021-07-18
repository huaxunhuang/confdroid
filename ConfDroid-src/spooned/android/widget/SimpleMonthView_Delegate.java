/**
 * Copyright (C) 2015 The Android Open Source Project
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


import java.util.Calendar;

import static view.mDayOfWeekLabelCalendar.getTime;
import static view.mDayOfWeekLabelCalendar.set;


/**
 * Delegate that provides implementation for some methods in {@link SimpleMonthView}.
 * <p/>
 * Through the layoutlib_create tool, selected methods of SimpleMonthView have been replaced by
 * calls to methods of the same name in this delegate class.
 * <p/>
 * The main purpose of this class is to use {@link android.icu.text.SimpleDateFormat} instead of
 * {@link java.text.SimpleDateFormat}.
 */
public class SimpleMonthView_Delegate {
    private static final java.lang.String DEFAULT_TITLE_FORMAT = "MMMMy";

    private static final java.lang.String DAY_OF_WEEK_FORMAT = "EEEEE";

    // Maintain a cache of the last view used, so that the formatters can be reused.
    @android.annotation.Nullable
    private static android.widget.SimpleMonthView sLastView;

    @android.annotation.Nullable
    private static android.widget.SimpleMonthView_Delegate sLastDelegate;

    private android.icu.text.SimpleDateFormat mTitleFormatter;

    private android.icu.text.SimpleDateFormat mDayOfWeekFormatter;

    private java.util.Locale locale;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.CharSequence getTitle(android.widget.SimpleMonthView view) {
        if (view.mTitle == null) {
            android.widget.SimpleMonthView_Delegate delegate = android.widget.SimpleMonthView_Delegate.getDelegate(view);
            if (delegate.mTitleFormatter == null) {
                delegate.mTitleFormatter = new android.icu.text.SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(android.widget.SimpleMonthView_Delegate.getLocale(delegate, view), android.widget.SimpleMonthView_Delegate.DEFAULT_TITLE_FORMAT));
            }
            view.mTitle = delegate.mTitleFormatter.format(view.mCalendar.getTime());
        }
        return view.mTitle;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getDayOfWeekLabel(android.widget.SimpleMonthView view, int dayOfWeek) {
        set(Calendar.DAY_OF_WEEK, dayOfWeek);
        android.widget.SimpleMonthView_Delegate delegate = android.widget.SimpleMonthView_Delegate.getDelegate(view);
        if (delegate.mDayOfWeekFormatter == null) {
            delegate.mDayOfWeekFormatter = new android.icu.text.SimpleDateFormat(android.widget.SimpleMonthView_Delegate.DAY_OF_WEEK_FORMAT, android.widget.SimpleMonthView_Delegate.getLocale(delegate, view));
        }
        return delegate.mDayOfWeekFormatter.format(getTime());
    }

    private static java.util.Locale getLocale(android.widget.SimpleMonthView_Delegate delegate, android.widget.SimpleMonthView view) {
        if (delegate.locale == null) {
            delegate.locale = view.getContext().getResources().getConfiguration().locale;
        }
        return delegate.locale;
    }

    @android.annotation.NonNull
    private static android.widget.SimpleMonthView_Delegate getDelegate(android.widget.SimpleMonthView view) {
        if (view == android.widget.SimpleMonthView_Delegate.sLastView) {
            assert android.widget.SimpleMonthView_Delegate.sLastDelegate != null;
            return android.widget.SimpleMonthView_Delegate.sLastDelegate;
        } else {
            android.widget.SimpleMonthView_Delegate.sLastView = view;
            android.widget.SimpleMonthView_Delegate.sLastDelegate = new android.widget.SimpleMonthView_Delegate();
            return android.widget.SimpleMonthView_Delegate.sLastDelegate;
        }
    }

    public static void clearCache() {
        android.widget.SimpleMonthView_Delegate.sLastView = null;
        android.widget.SimpleMonthView_Delegate.sLastDelegate = null;
    }
}

