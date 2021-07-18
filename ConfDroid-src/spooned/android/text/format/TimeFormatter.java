/**
 * Based on the UCB version of strftime.c with the copyright notice appearing below.
 */
/**
 * * Copyright (c) 1989 The Regents of the University of California.
 * * All rights reserved.
 * *
 * * Redistribution and use in source and binary forms are permitted
 * * provided that the above copyright notice and this paragraph are
 * * duplicated in all such forms and that any documentation,
 * * advertising materials, and other materials related to such
 * * distribution and use acknowledge that the software was developed
 * * by the University of California, Berkeley. The name of the
 * * University may not be used to endorse or promote products derived
 * * from this software without specific prior written permission.
 * * THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package android.text.format;


/**
 * Formatting logic for {@link Time}. Contains a port of Bionic's broken strftime_tz to Java.
 *
 * <p>This class is not thread safe.
 */
class TimeFormatter {
    // An arbitrary value outside the range representable by a char.
    private static final int FORCE_LOWER_CASE = -1;

    private static final int SECSPERMIN = 60;

    private static final int MINSPERHOUR = 60;

    private static final int DAYSPERWEEK = 7;

    private static final int MONSPERYEAR = 12;

    private static final int HOURSPERDAY = 24;

    private static final int DAYSPERLYEAR = 366;

    private static final int DAYSPERNYEAR = 365;

    /**
     * The Locale for which the cached LocaleData and formats have been loaded.
     */
    private static java.util.Locale sLocale;

    private static libcore.icu.LocaleData sLocaleData;

    private static java.lang.String sTimeOnlyFormat;

    private static java.lang.String sDateOnlyFormat;

    private static java.lang.String sDateTimeFormat;

    private final libcore.icu.LocaleData localeData;

    private final java.lang.String dateTimeFormat;

    private final java.lang.String timeOnlyFormat;

    private final java.lang.String dateOnlyFormat;

    private java.lang.StringBuilder outputBuilder;

    private java.util.Formatter numberFormatter;

    public TimeFormatter() {
        synchronized(android.text.format.TimeFormatter.class) {
            java.util.Locale locale = java.util.Locale.getDefault();
            if ((android.text.format.TimeFormatter.sLocale == null) || (!locale.equals(android.text.format.TimeFormatter.sLocale))) {
                android.text.format.TimeFormatter.sLocale = locale;
                android.text.format.TimeFormatter.sLocaleData = libcore.icu.LocaleData.get(locale);
                android.content.res.Resources r = android.content.res.Resources.getSystem();
                android.text.format.TimeFormatter.sTimeOnlyFormat = r.getString(com.android.internal.R.string.time_of_day);
                android.text.format.TimeFormatter.sDateOnlyFormat = r.getString(com.android.internal.R.string.month_day_year);
                android.text.format.TimeFormatter.sDateTimeFormat = r.getString(com.android.internal.R.string.date_and_time);
            }
            this.dateTimeFormat = android.text.format.TimeFormatter.sDateTimeFormat;
            this.timeOnlyFormat = android.text.format.TimeFormatter.sTimeOnlyFormat;
            this.dateOnlyFormat = android.text.format.TimeFormatter.sDateOnlyFormat;
            localeData = android.text.format.TimeFormatter.sLocaleData;
        }
    }

    /**
     * Format the specified {@code wallTime} using {@code pattern}. The output is returned.
     */
    public java.lang.String format(java.lang.String pattern, libcore.util.ZoneInfo.WallTime wallTime, libcore.util.ZoneInfo zoneInfo) {
        try {
            java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
            outputBuilder = stringBuilder;
            // This uses the US locale because number localization is handled separately (see below)
            // and locale sensitive strings are output directly using outputBuilder.
            numberFormatter = new java.util.Formatter(stringBuilder, java.util.Locale.US);
            formatInternal(pattern, wallTime, zoneInfo);
            java.lang.String result = stringBuilder.toString();
            // This behavior is the source of a bug since some formats are defined as being
            // in ASCII and not localized.
            if (localeData.zeroDigit != '0') {
                result = localizeDigits(result);
            }
            return result;
        } finally {
            outputBuilder = null;
            numberFormatter = null;
        }
    }

    private java.lang.String localizeDigits(java.lang.String s) {
        int length = s.length();
        int offsetToLocalizedDigits = localeData.zeroDigit - '0';
        java.lang.StringBuilder result = new java.lang.StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char ch = s.charAt(i);
            if ((ch >= '0') && (ch <= '9')) {
                ch += offsetToLocalizedDigits;
            }
            result.append(ch);
        }
        return result.toString();
    }

    /**
     * Format the specified {@code wallTime} using {@code pattern}. The output is written to
     * {@link #outputBuilder}.
     */
    private void formatInternal(java.lang.String pattern, libcore.util.ZoneInfo.WallTime wallTime, libcore.util.ZoneInfo zoneInfo) {
        java.nio.CharBuffer formatBuffer = java.nio.CharBuffer.wrap(pattern);
        while (formatBuffer.remaining() > 0) {
            boolean outputCurrentChar = true;
            char currentChar = formatBuffer.get(formatBuffer.position());
            if (currentChar == '%') {
                outputCurrentChar = handleToken(formatBuffer, wallTime, zoneInfo);
            }
            if (outputCurrentChar) {
                outputBuilder.append(formatBuffer.get(formatBuffer.position()));
            }
            formatBuffer.position(formatBuffer.position() + 1);
        } 
    }

    private boolean handleToken(java.nio.CharBuffer formatBuffer, libcore.util.ZoneInfo.WallTime wallTime, libcore.util.ZoneInfo zoneInfo) {
        // The char at formatBuffer.position() is expected to be '%' at this point.
        int modifier = 0;
        while (formatBuffer.remaining() > 1) {
            // Increment the position then get the new current char.
            formatBuffer.position(formatBuffer.position() + 1);
            char currentChar = formatBuffer.get(formatBuffer.position());
            switch (currentChar) {
                case 'A' :
                    modifyAndAppend((wallTime.getWeekDay() < 0) || (wallTime.getWeekDay() >= android.text.format.TimeFormatter.DAYSPERWEEK) ? "?" : localeData.longWeekdayNames[wallTime.getWeekDay() + 1], modifier);
                    return false;
                case 'a' :
                    modifyAndAppend((wallTime.getWeekDay() < 0) || (wallTime.getWeekDay() >= android.text.format.TimeFormatter.DAYSPERWEEK) ? "?" : localeData.shortWeekdayNames[wallTime.getWeekDay() + 1], modifier);
                    return false;
                case 'B' :
                    if (modifier == '-') {
                        modifyAndAppend((wallTime.getMonth() < 0) || (wallTime.getMonth() >= android.text.format.TimeFormatter.MONSPERYEAR) ? "?" : localeData.longStandAloneMonthNames[wallTime.getMonth()], modifier);
                    } else {
                        modifyAndAppend((wallTime.getMonth() < 0) || (wallTime.getMonth() >= android.text.format.TimeFormatter.MONSPERYEAR) ? "?" : localeData.longMonthNames[wallTime.getMonth()], modifier);
                    }
                    return false;
                case 'b' :
                case 'h' :
                    modifyAndAppend((wallTime.getMonth() < 0) || (wallTime.getMonth() >= android.text.format.TimeFormatter.MONSPERYEAR) ? "?" : localeData.shortMonthNames[wallTime.getMonth()], modifier);
                    return false;
                case 'C' :
                    outputYear(wallTime.getYear(), true, false, modifier);
                    return false;
                case 'c' :
                    formatInternal(dateTimeFormat, wallTime, zoneInfo);
                    return false;
                case 'D' :
                    formatInternal("%m/%d/%y", wallTime, zoneInfo);
                    return false;
                case 'd' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), wallTime.getMonthDay());
                    return false;
                case 'E' :
                case 'O' :
                    // C99 locale modifiers are not supported.
                    continue;
                case '_' :
                case '-' :
                case '0' :
                case '^' :
                case '#' :
                    modifier = currentChar;
                    continue;
                case 'e' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%2d", "%2d", "%d", "%02d"), wallTime.getMonthDay());
                    return false;
                case 'F' :
                    formatInternal("%Y-%m-%d", wallTime, zoneInfo);
                    return false;
                case 'H' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), wallTime.getHour());
                    return false;
                case 'I' :
                    int hour = ((wallTime.getHour() % 12) != 0) ? wallTime.getHour() % 12 : 12;
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), hour);
                    return false;
                case 'j' :
                    int yearDay = wallTime.getYearDay() + 1;
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%03d", "%3d", "%d", "%03d"), yearDay);
                    return false;
                case 'k' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%2d", "%2d", "%d", "%02d"), wallTime.getHour());
                    return false;
                case 'l' :
                    int n2 = ((wallTime.getHour() % 12) != 0) ? wallTime.getHour() % 12 : 12;
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%2d", "%2d", "%d", "%02d"), n2);
                    return false;
                case 'M' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), wallTime.getMinute());
                    return false;
                case 'm' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), wallTime.getMonth() + 1);
                    return false;
                case 'n' :
                    outputBuilder.append('\n');
                    return false;
                case 'p' :
                    modifyAndAppend(wallTime.getHour() >= (android.text.format.TimeFormatter.HOURSPERDAY / 2) ? localeData.amPm[1] : localeData.amPm[0], modifier);
                    return false;
                case 'P' :
                    modifyAndAppend(wallTime.getHour() >= (android.text.format.TimeFormatter.HOURSPERDAY / 2) ? localeData.amPm[1] : localeData.amPm[0], android.text.format.TimeFormatter.FORCE_LOWER_CASE);
                    return false;
                case 'R' :
                    formatInternal("%H:%M", wallTime, zoneInfo);
                    return false;
                case 'r' :
                    formatInternal("%I:%M:%S %p", wallTime, zoneInfo);
                    return false;
                case 'S' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), wallTime.getSecond());
                    return false;
                case 's' :
                    int timeInSeconds = wallTime.mktime(zoneInfo);
                    outputBuilder.append(java.lang.Integer.toString(timeInSeconds));
                    return false;
                case 'T' :
                    formatInternal("%H:%M:%S", wallTime, zoneInfo);
                    return false;
                case 't' :
                    outputBuilder.append('\t');
                    return false;
                case 'U' :
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), ((wallTime.getYearDay() + android.text.format.TimeFormatter.DAYSPERWEEK) - wallTime.getWeekDay()) / android.text.format.TimeFormatter.DAYSPERWEEK);
                    return false;
                case 'u' :
                    int day = (wallTime.getWeekDay() == 0) ? android.text.format.TimeFormatter.DAYSPERWEEK : wallTime.getWeekDay();
                    numberFormatter.format("%d", day);
                    return false;
                case 'V' :
                    /* ISO 8601 week number */
                case 'G' :
                    /* ISO 8601 year (four digits) */
                case 'g' :
                    /* ISO 8601 year (two digits) */
                    {
                        int year = wallTime.getYear();
                        int yday = wallTime.getYearDay();
                        int wday = wallTime.getWeekDay();
                        int w;
                        while (true) {
                            int len = (android.text.format.TimeFormatter.isLeap(year)) ? android.text.format.TimeFormatter.DAYSPERLYEAR : android.text.format.TimeFormatter.DAYSPERNYEAR;
                            // What yday (-3 ... 3) does the ISO year begin on?
                            int bot = (((yday + 11) - wday) % android.text.format.TimeFormatter.DAYSPERWEEK) - 3;
                            // What yday does the NEXT ISO year begin on?
                            int top = bot - (len % android.text.format.TimeFormatter.DAYSPERWEEK);
                            if (top < (-3)) {
                                top += android.text.format.TimeFormatter.DAYSPERWEEK;
                            }
                            top += len;
                            if (yday >= top) {
                                ++year;
                                w = 1;
                                break;
                            }
                            if (yday >= bot) {
                                w = 1 + ((yday - bot) / android.text.format.TimeFormatter.DAYSPERWEEK);
                                break;
                            }
                            --year;
                            yday += (android.text.format.TimeFormatter.isLeap(year)) ? android.text.format.TimeFormatter.DAYSPERLYEAR : android.text.format.TimeFormatter.DAYSPERNYEAR;
                        } 
                        if (currentChar == 'V') {
                            numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), w);
                        } else
                            if (currentChar == 'g') {
                                outputYear(year, false, true, modifier);
                            } else {
                                outputYear(year, true, true, modifier);
                            }

                        return false;
                    }
                case 'v' :
                    formatInternal("%e-%b-%Y", wallTime, zoneInfo);
                    return false;
                case 'W' :
                    int n = ((wallTime.getYearDay() + android.text.format.TimeFormatter.DAYSPERWEEK) - (wallTime.getWeekDay() != 0 ? wallTime.getWeekDay() - 1 : android.text.format.TimeFormatter.DAYSPERWEEK - 1)) / android.text.format.TimeFormatter.DAYSPERWEEK;
                    numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), n);
                    return false;
                case 'w' :
                    numberFormatter.format("%d", wallTime.getWeekDay());
                    return false;
                case 'X' :
                    formatInternal(timeOnlyFormat, wallTime, zoneInfo);
                    return false;
                case 'x' :
                    formatInternal(dateOnlyFormat, wallTime, zoneInfo);
                    return false;
                case 'y' :
                    outputYear(wallTime.getYear(), false, true, modifier);
                    return false;
                case 'Y' :
                    outputYear(wallTime.getYear(), true, true, modifier);
                    return false;
                case 'Z' :
                    if (wallTime.getIsDst() < 0) {
                        return false;
                    }
                    boolean isDst = wallTime.getIsDst() != 0;
                    modifyAndAppend(zoneInfo.getDisplayName(isDst, java.util.TimeZone.SHORT), modifier);
                    return false;
                case 'z' :
                    {
                        if (wallTime.getIsDst() < 0) {
                            return false;
                        }
                        int diff = wallTime.getGmtOffset();
                        char sign;
                        if (diff < 0) {
                            sign = '-';
                            diff = -diff;
                        } else {
                            sign = '+';
                        }
                        outputBuilder.append(sign);
                        diff /= android.text.format.TimeFormatter.SECSPERMIN;
                        diff = ((diff / android.text.format.TimeFormatter.MINSPERHOUR) * 100) + (diff % android.text.format.TimeFormatter.MINSPERHOUR);
                        numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%04d", "%4d", "%d", "%04d"), diff);
                        return false;
                    }
                case '+' :
                    formatInternal("%a %b %e %H:%M:%S %Z %Y", wallTime, zoneInfo);
                    return false;
                case '%' :
                    // If conversion char is undefined, behavior is undefined. Print out the
                    // character itself.
                default :
                    return true;
            }
        } 
        return true;
    }

    private void modifyAndAppend(java.lang.CharSequence str, int modifier) {
        switch (modifier) {
            case android.text.format.TimeFormatter.FORCE_LOWER_CASE :
                for (int i = 0; i < str.length(); i++) {
                    outputBuilder.append(android.text.format.TimeFormatter.brokenToLower(str.charAt(i)));
                }
                break;
            case '^' :
                for (int i = 0; i < str.length(); i++) {
                    outputBuilder.append(android.text.format.TimeFormatter.brokenToUpper(str.charAt(i)));
                }
                break;
            case '#' :
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (android.text.format.TimeFormatter.brokenIsUpper(c)) {
                        c = android.text.format.TimeFormatter.brokenToLower(c);
                    } else
                        if (android.text.format.TimeFormatter.brokenIsLower(c)) {
                            c = android.text.format.TimeFormatter.brokenToUpper(c);
                        }

                    outputBuilder.append(c);
                }
                break;
            default :
                outputBuilder.append(str);
        }
    }

    private void outputYear(int value, boolean outputTop, boolean outputBottom, int modifier) {
        int lead;
        int trail;
        final int DIVISOR = 100;
        trail = value % DIVISOR;
        lead = (value / DIVISOR) + (trail / DIVISOR);
        trail %= DIVISOR;
        if ((trail < 0) && (lead > 0)) {
            trail += DIVISOR;
            --lead;
        } else
            if ((lead < 0) && (trail > 0)) {
                trail -= DIVISOR;
                ++lead;
            }

        if (outputTop) {
            if ((lead == 0) && (trail < 0)) {
                outputBuilder.append("-0");
            } else {
                numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), lead);
            }
        }
        if (outputBottom) {
            int n = (trail < 0) ? -trail : trail;
            numberFormatter.format(android.text.format.TimeFormatter.getFormat(modifier, "%02d", "%2d", "%d", "%02d"), n);
        }
    }

    private static java.lang.String getFormat(int modifier, java.lang.String normal, java.lang.String underscore, java.lang.String dash, java.lang.String zero) {
        switch (modifier) {
            case '_' :
                return underscore;
            case '-' :
                return dash;
            case '0' :
                return zero;
        }
        return normal;
    }

    private static boolean isLeap(int year) {
        return ((year % 4) == 0) && (((year % 100) != 0) || ((year % 400) == 0));
    }

    /**
     * A broken implementation of {@link Character#isUpperCase(char)} that assumes ASCII codes in
     * order to be compatible with the old native implementation.
     */
    private static boolean brokenIsUpper(char toCheck) {
        return (toCheck >= 'A') && (toCheck <= 'Z');
    }

    /**
     * A broken implementation of {@link Character#isLowerCase(char)} that assumes ASCII codes in
     * order to be compatible with the old native implementation.
     */
    private static boolean brokenIsLower(char toCheck) {
        return (toCheck >= 'a') && (toCheck <= 'z');
    }

    /**
     * A broken implementation of {@link Character#toLowerCase(char)} that assumes ASCII codes in
     * order to be compatible with the old native implementation.
     */
    private static char brokenToLower(char input) {
        if ((input >= 'A') && (input <= 'Z')) {
            return ((char) ((input - 'A') + 'a'));
        }
        return input;
    }

    /**
     * A broken implementation of {@link Character#toUpperCase(char)} that assumes ASCII codes in
     * order to be compatible with the old native implementation.
     */
    private static char brokenToUpper(char input) {
        if ((input >= 'a') && (input <= 'z')) {
            return ((char) ((input - 'a') + 'A'));
        }
        return input;
    }
}

