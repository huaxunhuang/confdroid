/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.util;


/**
 * Helper for accessing features in {@link android.util.TimeUtils}
 * introduced after API level 4 in a backwards compatible fashion.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class TimeUtils {
    /**
     *
     *
     * @unknown Field length that can hold 999 days of time
     */
    public static final int HUNDRED_DAY_FIELD_LEN = 19;

    private static final int SECONDS_PER_MINUTE = 60;

    private static final int SECONDS_PER_HOUR = 60 * 60;

    private static final int SECONDS_PER_DAY = (24 * 60) * 60;

    private static final java.lang.Object sFormatSync = new java.lang.Object();

    private static char[] sFormatStr = new char[android.support.v4.util.TimeUtils.HUNDRED_DAY_FIELD_LEN + 5];

    private static int accumField(int amt, int suffix, boolean always, int zeropad) {
        if ((amt > 99) || (always && (zeropad >= 3))) {
            return 3 + suffix;
        }
        if ((amt > 9) || (always && (zeropad >= 2))) {
            return 2 + suffix;
        }
        if (always || (amt > 0)) {
            return 1 + suffix;
        }
        return 0;
    }

    private static int printField(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
        if (always || (amt > 0)) {
            final int startPos = pos;
            if ((always && (zeropad >= 3)) || (amt > 99)) {
                int dig = amt / 100;
                formatStr[pos] = ((char) (dig + '0'));
                pos++;
                amt -= dig * 100;
            }
            if (((always && (zeropad >= 2)) || (amt > 9)) || (startPos != pos)) {
                int dig = amt / 10;
                formatStr[pos] = ((char) (dig + '0'));
                pos++;
                amt -= dig * 10;
            }
            formatStr[pos] = ((char) (amt + '0'));
            pos++;
            formatStr[pos] = suffix;
            pos++;
        }
        return pos;
    }

    private static int formatDurationLocked(long duration, int fieldLen) {
        if (android.support.v4.util.TimeUtils.sFormatStr.length < fieldLen) {
            android.support.v4.util.TimeUtils.sFormatStr = new char[fieldLen];
        }
        char[] formatStr = android.support.v4.util.TimeUtils.sFormatStr;
        if (duration == 0) {
            int pos = 0;
            fieldLen -= 1;
            while (pos < fieldLen) {
                formatStr[pos] = ' ';
            } 
            formatStr[pos] = '0';
            return pos + 1;
        }
        char prefix;
        if (duration > 0) {
            prefix = '+';
        } else {
            prefix = '-';
            duration = -duration;
        }
        int millis = ((int) (duration % 1000));
        int seconds = ((int) (java.lang.Math.floor(duration / 1000)));
        int days = 0;
        int hours = 0;
        int minutes = 0;
        if (seconds > android.support.v4.util.TimeUtils.SECONDS_PER_DAY) {
            days = seconds / android.support.v4.util.TimeUtils.SECONDS_PER_DAY;
            seconds -= days * android.support.v4.util.TimeUtils.SECONDS_PER_DAY;
        }
        if (seconds > android.support.v4.util.TimeUtils.SECONDS_PER_HOUR) {
            hours = seconds / android.support.v4.util.TimeUtils.SECONDS_PER_HOUR;
            seconds -= hours * android.support.v4.util.TimeUtils.SECONDS_PER_HOUR;
        }
        if (seconds > android.support.v4.util.TimeUtils.SECONDS_PER_MINUTE) {
            minutes = seconds / android.support.v4.util.TimeUtils.SECONDS_PER_MINUTE;
            seconds -= minutes * android.support.v4.util.TimeUtils.SECONDS_PER_MINUTE;
        }
        int pos = 0;
        if (fieldLen != 0) {
            int myLen = android.support.v4.util.TimeUtils.accumField(days, 1, false, 0);
            myLen += android.support.v4.util.TimeUtils.accumField(hours, 1, myLen > 0, 2);
            myLen += android.support.v4.util.TimeUtils.accumField(minutes, 1, myLen > 0, 2);
            myLen += android.support.v4.util.TimeUtils.accumField(seconds, 1, myLen > 0, 2);
            myLen += android.support.v4.util.TimeUtils.accumField(millis, 2, true, myLen > 0 ? 3 : 0) + 1;
            while (myLen < fieldLen) {
                formatStr[pos] = ' ';
                pos++;
                myLen++;
            } 
        }
        formatStr[pos] = prefix;
        pos++;
        int start = pos;
        boolean zeropad = fieldLen != 0;
        pos = android.support.v4.util.TimeUtils.printField(formatStr, days, 'd', pos, false, 0);
        pos = android.support.v4.util.TimeUtils.printField(formatStr, hours, 'h', pos, pos != start, zeropad ? 2 : 0);
        pos = android.support.v4.util.TimeUtils.printField(formatStr, minutes, 'm', pos, pos != start, zeropad ? 2 : 0);
        pos = android.support.v4.util.TimeUtils.printField(formatStr, seconds, 's', pos, pos != start, zeropad ? 2 : 0);
        pos = android.support.v4.util.TimeUtils.printField(formatStr, millis, 'm', pos, true, zeropad && (pos != start) ? 3 : 0);
        formatStr[pos] = 's';
        return pos + 1;
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.lang.StringBuilder builder) {
        synchronized(android.support.v4.util.TimeUtils.sFormatSync) {
            int len = android.support.v4.util.TimeUtils.formatDurationLocked(duration, 0);
            builder.append(android.support.v4.util.TimeUtils.sFormatStr, 0, len);
        }
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.io.PrintWriter pw, int fieldLen) {
        synchronized(android.support.v4.util.TimeUtils.sFormatSync) {
            int len = android.support.v4.util.TimeUtils.formatDurationLocked(duration, fieldLen);
            pw.print(new java.lang.String(android.support.v4.util.TimeUtils.sFormatStr, 0, len));
        }
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.io.PrintWriter pw) {
        android.support.v4.util.TimeUtils.formatDuration(duration, pw, 0);
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long time, long now, java.io.PrintWriter pw) {
        if (time == 0) {
            pw.print("--");
            return;
        }
        android.support.v4.util.TimeUtils.formatDuration(time - now, pw, 0);
    }

    private TimeUtils() {
    }
}

