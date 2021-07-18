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
package android.text.format;


/**
 * Utility class for producing strings with formatted date/time.
 *
 * <p>Most callers should avoid supplying their own format strings to this
 * class' {@code format} methods and rely on the correctly localized ones
 * supplied by the system. This class' factory methods return
 * appropriately-localized {@link java.text.DateFormat} instances, suitable
 * for both formatting and parsing dates. For the canonical documentation
 * of format strings, see {@link java.text.SimpleDateFormat}.
 *
 * <p>In cases where the system does not provide a suitable pattern,
 * this class offers the {@link #getBestDateTimePattern} method.
 *
 * <p>The {@code format} methods in this class implement a subset of Unicode
 * <a href="http://www.unicode.org/reports/tr35/#Date_Format_Patterns">UTS #35</a> patterns.
 * The subset currently supported by this class includes the following format characters:
 * {@code acdEHhLKkLMmsyz}. Up to API level 17, only {@code adEhkMmszy} were supported.
 * Note that this class incorrectly implements {@code k} as if it were {@code H} for backwards
 * compatibility.
 *
 * <p>See {@link java.text.SimpleDateFormat} for more documentation
 * about patterns, or if you need a more complete or correct implementation.
 * Note that the non-{@code format} methods in this class are implemented by
 * {@code SimpleDateFormat}.
 */
public class DateFormat {
    /**
     *
     *
     * @deprecated Use a literal {@code '} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char QUOTE = '\'';

    /**
     *
     *
     * @deprecated Use a literal {@code 'a'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char AM_PM = 'a';

    /**
     *
     *
     * @deprecated Use a literal {@code 'a'} instead; 'A' was always equivalent to 'a'.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char CAPITAL_AM_PM = 'A';

    /**
     *
     *
     * @deprecated Use a literal {@code 'd'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char DATE = 'd';

    /**
     *
     *
     * @deprecated Use a literal {@code 'E'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char DAY = 'E';

    /**
     *
     *
     * @deprecated Use a literal {@code 'h'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char HOUR = 'h';

    /**
     *
     *
     * @deprecated Use a literal {@code 'H'} (for compatibility with {@link SimpleDateFormat}
    and Unicode) or {@code 'k'} (for compatibility with Android releases up to and including
    Jelly Bean MR-1) instead. Note that the two are incompatible.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char HOUR_OF_DAY = 'k';

    /**
     *
     *
     * @deprecated Use a literal {@code 'm'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char MINUTE = 'm';

    /**
     *
     *
     * @deprecated Use a literal {@code 'M'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char MONTH = 'M';

    /**
     *
     *
     * @deprecated Use a literal {@code 'L'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char STANDALONE_MONTH = 'L';

    /**
     *
     *
     * @deprecated Use a literal {@code 's'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char SECONDS = 's';

    /**
     *
     *
     * @deprecated Use a literal {@code 'z'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char TIME_ZONE = 'z';

    /**
     *
     *
     * @deprecated Use a literal {@code 'y'} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public static final char YEAR = 'y';

    private static final java.lang.Object sLocaleLock = new java.lang.Object();

    private static java.util.Locale sIs24HourLocale;

    private static boolean sIs24Hour;

    /**
     * Returns true if user preference is set to 24-hour format.
     *
     * @param context
     * 		the context to use for the content resolver
     * @return true if 24 hour time format is selected, false otherwise.
     */
    public static boolean is24HourFormat(android.content.Context context) {
        return android.text.format.DateFormat.is24HourFormat(context, android.os.UserHandle.myUserId());
    }

    /**
     * Returns true if user preference with the given user handle is set to 24-hour format.
     *
     * @param context
     * 		the context to use for the content resolver
     * @param userHandle
     * 		the user handle of the user to query.
     * @return true if 24 hour time format is selected, false otherwise.
     * @unknown 
     */
    public static boolean is24HourFormat(android.content.Context context, int userHandle) {
        java.lang.String value = android.provider.Settings.System.getStringForUser(context.getContentResolver(), android.provider.Settings.System.TIME_12_24, userHandle);
        if (value == null) {
            java.util.Locale locale = context.getResources().getConfiguration().locale;
            synchronized(android.text.format.DateFormat.sLocaleLock) {
                if ((android.text.format.DateFormat.sIs24HourLocale != null) && android.text.format.DateFormat.sIs24HourLocale.equals(locale)) {
                    return android.text.format.DateFormat.sIs24Hour;
                }
            }
            java.text.DateFormat natural = java.text.DateFormat.getTimeInstance(java.text.DateFormat.LONG, locale);
            if (natural instanceof java.text.SimpleDateFormat) {
                java.text.SimpleDateFormat sdf = ((java.text.SimpleDateFormat) (natural));
                java.lang.String pattern = sdf.toPattern();
                if (pattern.indexOf('H') >= 0) {
                    value = "24";
                } else {
                    value = "12";
                }
            } else {
                value = "12";
            }
            synchronized(android.text.format.DateFormat.sLocaleLock) {
                android.text.format.DateFormat.sIs24HourLocale = locale;
                android.text.format.DateFormat.sIs24Hour = value.equals("24");
            }
            return android.text.format.DateFormat.sIs24Hour;
        }
        return value.equals("24");
    }

    /**
     * Returns the best possible localized form of the given skeleton for the given
     * locale. A skeleton is similar to, and uses the same format characters as, a Unicode
     * <a href="http://www.unicode.org/reports/tr35/#Date_Format_Patterns">UTS #35</a>
     * pattern.
     *
     * <p>One difference is that order is irrelevant. For example, "MMMMd" will return
     * "MMMM d" in the {@code en_US} locale, but "d. MMMM" in the {@code de_CH} locale.
     *
     * <p>Note also in that second example that the necessary punctuation for German was
     * added. For the same input in {@code es_ES}, we'd have even more extra text:
     * "d 'de' MMMM".
     *
     * <p>This method will automatically correct for grammatical necessity. Given the
     * same "MMMMd" input, this method will return "d LLLL" in the {@code fa_IR} locale,
     * where stand-alone months are necessary. Lengths are preserved where meaningful,
     * so "Md" would give a different result to "MMMd", say, except in a locale such as
     * {@code ja_JP} where there is only one length of month.
     *
     * <p>This method will only return patterns that are in CLDR, and is useful whenever
     * you know what elements you want in your format string but don't want to make your
     * code specific to any one locale.
     *
     * @param locale
     * 		the locale into which the skeleton should be localized
     * @param skeleton
     * 		a skeleton as described above
     * @return a string pattern suitable for use with {@link java.text.SimpleDateFormat}.
     */
    public static java.lang.String getBestDateTimePattern(java.util.Locale locale, java.lang.String skeleton) {
        return libcore.icu.ICU.getBestDateTimePattern(skeleton, locale);
    }

    /**
     * Returns a {@link java.text.DateFormat} object that can format the time according
     * to the current locale and the user's 12-/24-hour clock preference.
     *
     * @param context
     * 		the application context
     * @return the {@link java.text.DateFormat} object that properly formats the time.
     */
    public static java.text.DateFormat getTimeFormat(android.content.Context context) {
        return new java.text.SimpleDateFormat(android.text.format.DateFormat.getTimeFormatString(context));
    }

    /**
     * Returns a String pattern that can be used to format the time according
     * to the current locale and the user's 12-/24-hour clock preference.
     *
     * @param context
     * 		the application context
     * @unknown 
     */
    public static java.lang.String getTimeFormatString(android.content.Context context) {
        return android.text.format.DateFormat.getTimeFormatString(context, android.os.UserHandle.myUserId());
    }

    /**
     * Returns a String pattern that can be used to format the time according
     * to the current locale and the user's 12-/24-hour clock preference.
     *
     * @param context
     * 		the application context
     * @param userHandle
     * 		the user handle of the user to query the format for
     * @unknown 
     */
    public static java.lang.String getTimeFormatString(android.content.Context context, int userHandle) {
        libcore.icu.LocaleData d = libcore.icu.LocaleData.get(context.getResources().getConfiguration().locale);
        return android.text.format.DateFormat.is24HourFormat(context, userHandle) ? d.timeFormat_Hm : d.timeFormat_hm;
    }

    /**
     * Returns a {@link java.text.DateFormat} object that can format the date
     * in short form according to the current locale.
     *
     * @param context
     * 		the application context
     * @return the {@link java.text.DateFormat} object that properly formats the date.
     */
    public static java.text.DateFormat getDateFormat(android.content.Context context) {
        return java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
    }

    /**
     * Returns a {@link java.text.DateFormat} object that can format the date
     * in long form (such as {@code Monday, January 3, 2000}) for the current locale.
     *
     * @param context
     * 		the application context
     * @return the {@link java.text.DateFormat} object that formats the date in long form.
     */
    public static java.text.DateFormat getLongDateFormat(android.content.Context context) {
        return java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
    }

    /**
     * Returns a {@link java.text.DateFormat} object that can format the date
     * in medium form (such as {@code Jan 3, 2000}) for the current locale.
     *
     * @param context
     * 		the application context
     * @return the {@link java.text.DateFormat} object that formats the date in long form.
     */
    public static java.text.DateFormat getMediumDateFormat(android.content.Context context) {
        return java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
    }

    /**
     * Gets the current date format stored as a char array. Returns a 3 element
     * array containing the day ({@code 'd'}), month ({@code 'M'}), and year ({@code 'y'}))
     * in the order specified by the user's format preference.  Note that this order is
     * <i>only</i> appropriate for all-numeric dates; spelled-out (MEDIUM and LONG)
     * dates will generally contain other punctuation, spaces, or words,
     * not just the day, month, and year, and not necessarily in the same
     * order returned here.
     */
    public static char[] getDateFormatOrder(android.content.Context context) {
        return libcore.icu.ICU.getDateFormatOrder(android.text.format.DateFormat.getDateFormatString());
    }

    private static java.lang.String getDateFormatString() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);
        if (df instanceof java.text.SimpleDateFormat) {
            return ((java.text.SimpleDateFormat) (df)).toPattern();
        }
        throw new java.lang.AssertionError("!(df instanceof SimpleDateFormat)");
    }

    /**
     * Given a format string and a time in milliseconds since Jan 1, 1970 GMT, returns a
     * CharSequence containing the requested date.
     *
     * @param inFormat
     * 		the format string, as described in {@link android.text.format.DateFormat}
     * @param inTimeInMillis
     * 		in milliseconds since Jan 1, 1970 GMT
     * @return a {@link CharSequence} containing the requested text
     */
    public static java.lang.CharSequence format(java.lang.CharSequence inFormat, long inTimeInMillis) {
        return android.text.format.DateFormat.format(inFormat, new java.util.Date(inTimeInMillis));
    }

    /**
     * Given a format string and a {@link java.util.Date} object, returns a CharSequence containing
     * the requested date.
     *
     * @param inFormat
     * 		the format string, as described in {@link android.text.format.DateFormat}
     * @param inDate
     * 		the date to format
     * @return a {@link CharSequence} containing the requested text
     */
    public static java.lang.CharSequence format(java.lang.CharSequence inFormat, java.util.Date inDate) {
        java.util.Calendar c = new java.util.GregorianCalendar();
        c.setTime(inDate);
        return android.text.format.DateFormat.format(inFormat, c);
    }

    /**
     * Indicates whether the specified format string contains seconds.
     *
     * Always returns false if the input format is null.
     *
     * @param inFormat
     * 		the format string, as described in {@link android.text.format.DateFormat}
     * @return true if the format string contains {@link #SECONDS}, false otherwise
     * @unknown 
     */
    public static boolean hasSeconds(java.lang.CharSequence inFormat) {
        return android.text.format.DateFormat.hasDesignator(inFormat, android.text.format.DateFormat.SECONDS);
    }

    /**
     * Test if a format string contains the given designator. Always returns
     * {@code false} if the input format is {@code null}.
     *
     * @unknown 
     */
    public static boolean hasDesignator(java.lang.CharSequence inFormat, char designator) {
        if (inFormat == null)
            return false;

        final int length = inFormat.length();
        int c;
        int count;
        for (int i = 0; i < length; i += count) {
            count = 1;
            c = inFormat.charAt(i);
            if (c == android.text.format.DateFormat.QUOTE) {
                count = android.text.format.DateFormat.skipQuotedText(inFormat, i, length);
            } else
                if (c == designator) {
                    return true;
                }

        }
        return false;
    }

    private static int skipQuotedText(java.lang.CharSequence s, int i, int len) {
        if (((i + 1) < len) && (s.charAt(i + 1) == android.text.format.DateFormat.QUOTE)) {
            return 2;
        }
        int count = 1;
        // skip leading quote
        i++;
        while (i < len) {
            char c = s.charAt(i);
            if (c == android.text.format.DateFormat.QUOTE) {
                count++;
                // QUOTEQUOTE -> QUOTE
                if (((i + 1) < len) && (s.charAt(i + 1) == android.text.format.DateFormat.QUOTE)) {
                    i++;
                } else {
                    break;
                }
            } else {
                i++;
                count++;
            }
        } 
        return count;
    }

    /**
     * Given a format string and a {@link java.util.Calendar} object, returns a CharSequence
     * containing the requested date.
     *
     * @param inFormat
     * 		the format string, as described in {@link android.text.format.DateFormat}
     * @param inDate
     * 		the date to format
     * @return a {@link CharSequence} containing the requested text
     */
    public static java.lang.CharSequence format(java.lang.CharSequence inFormat, java.util.Calendar inDate) {
        android.text.SpannableStringBuilder s = new android.text.SpannableStringBuilder(inFormat);
        int count;
        libcore.icu.LocaleData localeData = libcore.icu.LocaleData.get(java.util.Locale.getDefault());
        int len = inFormat.length();
        for (int i = 0; i < len; i += count) {
            count = 1;
            int c = s.charAt(i);
            if (c == android.text.format.DateFormat.QUOTE) {
                count = android.text.format.DateFormat.appendQuotedText(s, i, len);
                len = s.length();
                continue;
            }
            while (((i + count) < len) && (s.charAt(i + count) == c)) {
                count++;
            } 
            java.lang.String replacement;
            switch (c) {
                case 'A' :
                case 'a' :
                    replacement = localeData.amPm[inDate.get(java.util.Calendar.AM_PM) - java.util.Calendar.AM];
                    break;
                case 'd' :
                    replacement = android.text.format.DateFormat.zeroPad(inDate.get(java.util.Calendar.DATE), count);
                    break;
                case 'c' :
                case 'E' :
                    replacement = android.text.format.DateFormat.getDayOfWeekString(localeData, inDate.get(java.util.Calendar.DAY_OF_WEEK), count, c);
                    break;
                case 'K' :
                    // hour in am/pm (0-11)
                case 'h' :
                    // hour in am/pm (1-12)
                    {
                        int hour = inDate.get(java.util.Calendar.HOUR);
                        if ((c == 'h') && (hour == 0)) {
                            hour = 12;
                        }
                        replacement = android.text.format.DateFormat.zeroPad(hour, count);
                    }
                    break;
                case 'H' :
                    // hour in day (0-23)
                case 'k' :
                    // hour in day (1-24) [but see note below]
                    {
                        int hour = inDate.get(java.util.Calendar.HOUR_OF_DAY);
                        // Historically on Android 'k' was interpreted as 'H', which wasn't
                        // implemented, so pretty much all callers that want to format 24-hour
                        // times are abusing 'k'. http://b/8359981.
                        if ((false && (c == 'k')) && (hour == 0)) {
                            hour = 24;
                        }
                        replacement = android.text.format.DateFormat.zeroPad(hour, count);
                    }
                    break;
                case 'L' :
                case 'M' :
                    replacement = android.text.format.DateFormat.getMonthString(localeData, inDate.get(java.util.Calendar.MONTH), count, c);
                    break;
                case 'm' :
                    replacement = android.text.format.DateFormat.zeroPad(inDate.get(java.util.Calendar.MINUTE), count);
                    break;
                case 's' :
                    replacement = android.text.format.DateFormat.zeroPad(inDate.get(java.util.Calendar.SECOND), count);
                    break;
                case 'y' :
                    replacement = android.text.format.DateFormat.getYearString(inDate.get(java.util.Calendar.YEAR), count);
                    break;
                case 'z' :
                    replacement = android.text.format.DateFormat.getTimeZoneString(inDate, count);
                    break;
                default :
                    replacement = null;
                    break;
            }
            if (replacement != null) {
                s.replace(i, i + count, replacement);
                count = replacement.length();// CARE: count is used in the for loop above

                len = s.length();
            }
        }
        if (inFormat instanceof android.text.Spanned) {
            return new android.text.SpannedString(s);
        } else {
            return s.toString();
        }
    }

    private static java.lang.String getDayOfWeekString(libcore.icu.LocaleData ld, int day, int count, int kind) {
        boolean standalone = kind == 'c';
        if (count == 5) {
            return standalone ? ld.tinyStandAloneWeekdayNames[day] : ld.tinyWeekdayNames[day];
        } else
            if (count == 4) {
                return standalone ? ld.longStandAloneWeekdayNames[day] : ld.longWeekdayNames[day];
            } else {
                return standalone ? ld.shortStandAloneWeekdayNames[day] : ld.shortWeekdayNames[day];
            }

    }

    private static java.lang.String getMonthString(libcore.icu.LocaleData ld, int month, int count, int kind) {
        boolean standalone = kind == 'L';
        if (count == 5) {
            return standalone ? ld.tinyStandAloneMonthNames[month] : ld.tinyMonthNames[month];
        } else
            if (count == 4) {
                return standalone ? ld.longStandAloneMonthNames[month] : ld.longMonthNames[month];
            } else
                if (count == 3) {
                    return standalone ? ld.shortStandAloneMonthNames[month] : ld.shortMonthNames[month];
                } else {
                    // Calendar.JANUARY == 0, so add 1 to month.
                    return android.text.format.DateFormat.zeroPad(month + 1, count);
                }


    }

    private static java.lang.String getTimeZoneString(java.util.Calendar inDate, int count) {
        java.util.TimeZone tz = inDate.getTimeZone();
        if (count < 2) {
            // FIXME: shouldn't this be <= 2 ?
            return android.text.format.DateFormat.formatZoneOffset(inDate.get(java.util.Calendar.DST_OFFSET) + inDate.get(java.util.Calendar.ZONE_OFFSET), count);
        } else {
            boolean dst = inDate.get(java.util.Calendar.DST_OFFSET) != 0;
            return tz.getDisplayName(dst, java.util.TimeZone.SHORT);
        }
    }

    private static java.lang.String formatZoneOffset(int offset, int count) {
        offset /= 1000;// milliseconds to seconds

        java.lang.StringBuilder tb = new java.lang.StringBuilder();
        if (offset < 0) {
            tb.insert(0, "-");
            offset = -offset;
        } else {
            tb.insert(0, "+");
        }
        int hours = offset / 3600;
        int minutes = (offset % 3600) / 60;
        tb.append(android.text.format.DateFormat.zeroPad(hours, 2));
        tb.append(android.text.format.DateFormat.zeroPad(minutes, 2));
        return tb.toString();
    }

    private static java.lang.String getYearString(int year, int count) {
        return count <= 2 ? android.text.format.DateFormat.zeroPad(year % 100, 2) : java.lang.String.format(java.util.Locale.getDefault(), "%d", year);
    }

    private static int appendQuotedText(android.text.SpannableStringBuilder s, int i, int len) {
        if (((i + 1) < len) && (s.charAt(i + 1) == android.text.format.DateFormat.QUOTE)) {
            s.delete(i, i + 1);
            return 1;
        }
        int count = 0;
        // delete leading quote
        s.delete(i, i + 1);
        len--;
        while (i < len) {
            char c = s.charAt(i);
            if (c == android.text.format.DateFormat.QUOTE) {
                // QUOTEQUOTE -> QUOTE
                if (((i + 1) < len) && (s.charAt(i + 1) == android.text.format.DateFormat.QUOTE)) {
                    s.delete(i, i + 1);
                    len--;
                    count++;
                    i++;
                } else {
                    // Closing QUOTE ends quoted text copying
                    s.delete(i, i + 1);
                    break;
                }
            } else {
                i++;
                count++;
            }
        } 
        return count;
    }

    private static java.lang.String zeroPad(int inValue, int inMinDigits) {
        return java.lang.String.format(java.util.Locale.getDefault(), ("%0" + inMinDigits) + "d", inValue);
    }
}

