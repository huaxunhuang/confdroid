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
package android.webkit;


/**
 * Sorts dates into the following groups:
 *   Today
 *   Yesterday
 *   seven days ago
 *   one month ago
 *   older than a month ago
 */
public class DateSorter {
    private static final java.lang.String LOGTAG = "webkit";

    /**
     * must be >= 3
     */
    public static final int DAY_COUNT = 5;

    private long[] mBins = new long[android.webkit.DateSorter.DAY_COUNT - 1];

    private java.lang.String[] mLabels = new java.lang.String[android.webkit.DateSorter.DAY_COUNT];

    private static final int NUM_DAYS_AGO = 7;

    /**
     *
     *
     * @param context
     * 		Application context
     */
    public DateSorter(android.content.Context context) {
        android.content.res.Resources resources = context.getResources();
        java.util.Calendar c = java.util.Calendar.getInstance();
        beginningOfDay(c);
        // Create the bins
        mBins[0] = c.getTimeInMillis();// Today

        c.add(java.util.Calendar.DAY_OF_YEAR, -1);
        mBins[1] = c.getTimeInMillis();// Yesterday

        c.add(java.util.Calendar.DAY_OF_YEAR, -(android.webkit.DateSorter.NUM_DAYS_AGO - 1));
        mBins[2] = c.getTimeInMillis();// Five days ago

        c.add(java.util.Calendar.DAY_OF_YEAR, android.webkit.DateSorter.NUM_DAYS_AGO);// move back to today

        c.add(java.util.Calendar.MONTH, -1);
        mBins[3] = c.getTimeInMillis();// One month ago

        // build labels
        java.util.Locale locale = resources.getConfiguration().locale;
        if (locale == null) {
            locale = java.util.Locale.getDefault();
        }
        libcore.icu.LocaleData localeData = libcore.icu.LocaleData.get(locale);
        mLabels[0] = localeData.today;
        mLabels[1] = localeData.yesterday;
        int resId = com.android.internal.R.plurals.last_num_days;
        java.lang.String format = resources.getQuantityString(resId, android.webkit.DateSorter.NUM_DAYS_AGO);
        mLabels[2] = java.lang.String.format(format, android.webkit.DateSorter.NUM_DAYS_AGO);
        mLabels[3] = context.getString(com.android.internal.R.string.last_month);
        mLabels[4] = context.getString(com.android.internal.R.string.older);
    }

    /**
     *
     *
     * @param time
     * 		time since the Epoch in milliseconds, such as that
     * 		returned by Calendar.getTimeInMillis()
     * @return an index from 0 to (DAY_COUNT - 1) that identifies which
    date bin this date belongs to
     */
    public int getIndex(long time) {
        int lastDay = android.webkit.DateSorter.DAY_COUNT - 1;
        for (int i = 0; i < lastDay; i++) {
            if (time > mBins[i])
                return i;

        }
        return lastDay;
    }

    /**
     *
     *
     * @param index
     * 		date bin index as returned by getIndex()
     * @return string label suitable for display to user
     */
    public java.lang.String getLabel(int index) {
        if ((index < 0) || (index >= android.webkit.DateSorter.DAY_COUNT))
            return "";

        return mLabels[index];
    }

    /**
     *
     *
     * @param index
     * 		date bin index as returned by getIndex()
     * @return date boundary at given index
     */
    public long getBoundary(int index) {
        int lastDay = android.webkit.DateSorter.DAY_COUNT - 1;
        // Error case
        if ((index < 0) || (index > lastDay))
            index = 0;

        // Since this provides a lower boundary on dates that will be included
        // in the given bin, provide the smallest value
        if (index == lastDay)
            return java.lang.Long.MIN_VALUE;

        return mBins[index];
    }

    /**
     * Calcuate 12:00am by zeroing out hour, minute, second, millisecond
     */
    private void beginningOfDay(java.util.Calendar c) {
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
    }
}

