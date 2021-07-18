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
 * Utility class to aid in formatting common values that are not covered
 * by the {@link java.util.Formatter} class in {@link java.util}
 */
public final class Formatter {
    /**
     * {@hide }
     */
    public static final int FLAG_SHORTER = 1 << 0;

    /**
     * {@hide }
     */
    public static final int FLAG_CALCULATE_ROUNDED = 1 << 1;

    /**
     * {@hide }
     */
    public static class BytesResult {
        public final java.lang.String value;

        public final java.lang.String units;

        public final long roundedBytes;

        public BytesResult(java.lang.String value, java.lang.String units, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.roundedBytes = roundedBytes;
        }
    }

    /* Wraps the source string in bidi formatting characters in RTL locales */
    private static java.lang.String bidiWrap(@android.annotation.NonNull
    android.content.Context context, java.lang.String source) {
        final java.util.Locale locale = context.getResources().getConfiguration().locale;
        if (android.text.TextUtils.getLayoutDirectionFromLocale(locale) == android.view.View.LAYOUT_DIRECTION_RTL) {
            return /* RTL */
            android.text.BidiFormatter.getInstance(true).unicodeWrap(source);
        } else {
            return source;
        }
    }

    /**
     * Formats a content size to be in the form of bytes, kilobytes, megabytes, etc.
     *
     * If the context has a right-to-left locale, the returned string is wrapped in bidi formatting
     * characters to make sure it's displayed correctly if inserted inside a right-to-left string.
     * (This is useful in cases where the unit strings, like "MB", are left-to-right, but the
     * locale is right-to-left.)
     *
     * @param context
     * 		Context to use to load the localized units
     * @param sizeBytes
     * 		size value to be formatted, in bytes
     * @return formatted string with the number
     */
    public static java.lang.String formatFileSize(@android.annotation.Nullable
    android.content.Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        final android.text.format.Formatter.BytesResult res = android.text.format.Formatter.formatBytes(context.getResources(), sizeBytes, 0);
        return android.text.format.Formatter.bidiWrap(context, context.getString(com.android.internal.R.string.fileSizeSuffix, res.value, res.units));
    }

    /**
     * Like {@link #formatFileSize}, but trying to generate shorter numbers
     * (showing fewer digits of precision).
     */
    public static java.lang.String formatShortFileSize(@android.annotation.Nullable
    android.content.Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        final android.text.format.Formatter.BytesResult res = android.text.format.Formatter.formatBytes(context.getResources(), sizeBytes, android.text.format.Formatter.FLAG_SHORTER);
        return android.text.format.Formatter.bidiWrap(context, context.getString(com.android.internal.R.string.fileSizeSuffix, res.value, res.units));
    }

    /**
     * {@hide }
     */
    public static android.text.format.Formatter.BytesResult formatBytes(android.content.res.Resources res, long sizeBytes, int flags) {
        final boolean isNegative = sizeBytes < 0;
        float result = (isNegative) ? -sizeBytes : sizeBytes;
        int suffix = com.android.internal.R.string.byteShort;
        long mult = 1;
        if (result > 900) {
            suffix = com.android.internal.R.string.kilobyteShort;
            mult = android.net.TrafficStats.KB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.megabyteShort;
            mult = android.net.TrafficStats.MB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.gigabyteShort;
            mult = android.net.TrafficStats.GB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.terabyteShort;
            mult = android.net.TrafficStats.TB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.petabyteShort;
            mult = android.net.TrafficStats.PB_IN_BYTES;
            result = result / 1024;
        }
        // Note we calculate the rounded long by ourselves, but still let String.format()
        // compute the rounded value. String.format("%f", 0.1) might not return "0.1" due to
        // floating point errors.
        final int roundFactor;
        final java.lang.String roundFormat;
        if ((mult == 1) || (result >= 100)) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else
            if (result < 1) {
                roundFactor = 100;
                roundFormat = "%.2f";
            } else
                if (result < 10) {
                    if ((flags & android.text.format.Formatter.FLAG_SHORTER) != 0) {
                        roundFactor = 10;
                        roundFormat = "%.1f";
                    } else {
                        roundFactor = 100;
                        roundFormat = "%.2f";
                    }
                } else {
                    // 10 <= result < 100
                    if ((flags & android.text.format.Formatter.FLAG_SHORTER) != 0) {
                        roundFactor = 1;
                        roundFormat = "%.0f";
                    } else {
                        roundFactor = 100;
                        roundFormat = "%.2f";
                    }
                }


        if (isNegative) {
            result = -result;
        }
        final java.lang.String roundedString = java.lang.String.format(roundFormat, result);
        // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like 80PB so
        // it's okay (for now)...
        final long roundedBytes = ((flags & android.text.format.Formatter.FLAG_CALCULATE_ROUNDED) == 0) ? 0 : (((long) (java.lang.Math.round(result * roundFactor))) * mult) / roundFactor;
        final java.lang.String units = res.getString(suffix);
        return new android.text.format.Formatter.BytesResult(roundedString, units, roundedBytes);
    }

    /**
     * Returns a string in the canonical IPv4 format ###.###.###.### from a packed integer
     * containing the IP address. The IPv4 address is expected to be in little-endian
     * format (LSB first). That is, 0x01020304 will return "4.3.2.1".
     *
     * @deprecated Use {@link java.net.InetAddress#getHostAddress()}, which supports both IPv4 and
    IPv6 addresses. This method does not support IPv6 addresses.
     */
    @java.lang.Deprecated
    public static java.lang.String formatIpAddress(int ipv4Address) {
        return android.net.NetworkUtils.intToInetAddress(ipv4Address).getHostAddress();
    }

    private static final int SECONDS_PER_MINUTE = 60;

    private static final int SECONDS_PER_HOUR = 60 * 60;

    private static final int SECONDS_PER_DAY = (24 * 60) * 60;

    private static final int MILLIS_PER_MINUTE = 1000 * 60;

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 1 day 5 hrs; will include at most two units, can go down to seconds precision.
     *
     * @param context
     * 		the application context
     * @param millis
     * 		the elapsed time in milli seconds
     * @return the formatted elapsed time
     * @unknown 
     */
    public static java.lang.String formatShortElapsedTime(android.content.Context context, long millis) {
        long secondsLong = millis / 1000;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        if (secondsLong >= android.text.format.Formatter.SECONDS_PER_DAY) {
            days = ((int) (secondsLong / android.text.format.Formatter.SECONDS_PER_DAY));
            secondsLong -= days * android.text.format.Formatter.SECONDS_PER_DAY;
        }
        if (secondsLong >= android.text.format.Formatter.SECONDS_PER_HOUR) {
            hours = ((int) (secondsLong / android.text.format.Formatter.SECONDS_PER_HOUR));
            secondsLong -= hours * android.text.format.Formatter.SECONDS_PER_HOUR;
        }
        if (secondsLong >= android.text.format.Formatter.SECONDS_PER_MINUTE) {
            minutes = ((int) (secondsLong / android.text.format.Formatter.SECONDS_PER_MINUTE));
            secondsLong -= minutes * android.text.format.Formatter.SECONDS_PER_MINUTE;
        }
        int seconds = ((int) (secondsLong));
        if (days >= 2) {
            days += (hours + 12) / 24;
            return context.getString(com.android.internal.R.string.durationDays, days);
        } else
            if (days > 0) {
                if (hours == 1) {
                    return context.getString(com.android.internal.R.string.durationDayHour, days, hours);
                }
                return context.getString(com.android.internal.R.string.durationDayHours, days, hours);
            } else
                if (hours >= 2) {
                    hours += (minutes + 30) / 60;
                    return context.getString(com.android.internal.R.string.durationHours, hours);
                } else
                    if (hours > 0) {
                        if (minutes == 1) {
                            return context.getString(com.android.internal.R.string.durationHourMinute, hours, minutes);
                        }
                        return context.getString(com.android.internal.R.string.durationHourMinutes, hours, minutes);
                    } else
                        if (minutes >= 2) {
                            minutes += (seconds + 30) / 60;
                            return context.getString(com.android.internal.R.string.durationMinutes, minutes);
                        } else
                            if (minutes > 0) {
                                if (seconds == 1) {
                                    return context.getString(com.android.internal.R.string.durationMinuteSecond, minutes, seconds);
                                }
                                return context.getString(com.android.internal.R.string.durationMinuteSeconds, minutes, seconds);
                            } else
                                if (seconds == 1) {
                                    return context.getString(com.android.internal.R.string.durationSecond, seconds);
                                } else {
                                    return context.getString(com.android.internal.R.string.durationSeconds, seconds);
                                }






    }

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 1 day 5 hrs; will include at most two units, can go down to minutes precision.
     *
     * @param context
     * 		the application context
     * @param millis
     * 		the elapsed time in milli seconds
     * @return the formatted elapsed time
     * @unknown 
     */
    public static java.lang.String formatShortElapsedTimeRoundingUpToMinutes(android.content.Context context, long millis) {
        long minutesRoundedUp = ((millis + android.text.format.Formatter.MILLIS_PER_MINUTE) - 1) / android.text.format.Formatter.MILLIS_PER_MINUTE;
        if (minutesRoundedUp == 0) {
            return context.getString(com.android.internal.R.string.durationMinutes, 0);
        } else
            if (minutesRoundedUp == 1) {
                return context.getString(com.android.internal.R.string.durationMinute, 1);
            }

        return android.text.format.Formatter.formatShortElapsedTime(context, minutesRoundedUp * android.text.format.Formatter.MILLIS_PER_MINUTE);
    }
}

