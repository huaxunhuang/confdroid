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
package android.util;


/**
 * A class containing utility methods related to time zones.
 */
public class TimeUtils {
    /**
     *
     *
     * @unknown 
     */
    public TimeUtils() {
    }

    private static final boolean DBG = false;

    private static final java.lang.String TAG = "TimeUtils";

    /**
     * Cached results of getTineZones
     */
    private static final java.lang.Object sLastLockObj = new java.lang.Object();

    private static java.util.ArrayList<java.util.TimeZone> sLastZones = null;

    private static java.lang.String sLastCountry = null;

    /**
     * Cached results of getTimeZonesWithUniqueOffsets
     */
    private static final java.lang.Object sLastUniqueLockObj = new java.lang.Object();

    private static java.util.ArrayList<java.util.TimeZone> sLastUniqueZoneOffsets = null;

    private static java.lang.String sLastUniqueCountry = null;

    /**
     * {@hide }
     */
    private static java.text.SimpleDateFormat sLoggingFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Tries to return a time zone that would have had the specified offset
     * and DST value at the specified moment in the specified country.
     * Returns null if no suitable zone could be found.
     */
    public static java.util.TimeZone getTimeZone(int offset, boolean dst, long when, java.lang.String country) {
        java.util.TimeZone best = null;
        final java.util.Date d = new java.util.Date(when);
        java.util.TimeZone current = java.util.TimeZone.getDefault();
        java.lang.String currentName = current.getID();
        int currentOffset = current.getOffset(when);
        boolean currentDst = current.inDaylightTime(d);
        for (java.util.TimeZone tz : android.util.TimeUtils.getTimeZones(country)) {
            // If the current time zone is from the right country
            // and meets the other known properties, keep it
            // instead of changing to another one.
            if (tz.getID().equals(currentName)) {
                if ((currentOffset == offset) && (currentDst == dst)) {
                    return current;
                }
            }
            // Otherwise, take the first zone from the right
            // country that has the correct current offset and DST.
            // (Keep iterating instead of returning in case we
            // haven't encountered the current time zone yet.)
            if (best == null) {
                if ((tz.getOffset(when) == offset) && (tz.inDaylightTime(d) == dst)) {
                    best = tz;
                }
            }
        }
        return best;
    }

    /**
     * Return list of unique time zones for the country. Do not modify
     *
     * @param country
     * 		to find
     * @return list of unique time zones, maybe empty but never null. Do not modify.
     * @unknown 
     */
    public static java.util.ArrayList<java.util.TimeZone> getTimeZonesWithUniqueOffsets(java.lang.String country) {
        synchronized(android.util.TimeUtils.sLastUniqueLockObj) {
            if ((country != null) && country.equals(android.util.TimeUtils.sLastUniqueCountry)) {
                if (android.util.TimeUtils.DBG) {
                    android.util.Log.d(android.util.TimeUtils.TAG, ("getTimeZonesWithUniqueOffsets(" + country) + "): return cached version");
                }
                return android.util.TimeUtils.sLastUniqueZoneOffsets;
            }
        }
        java.util.Collection<java.util.TimeZone> zones = android.util.TimeUtils.getTimeZones(country);
        java.util.ArrayList<java.util.TimeZone> uniqueTimeZones = new java.util.ArrayList<java.util.TimeZone>();
        for (java.util.TimeZone zone : zones) {
            // See if we already have this offset,
            // Using slow but space efficient and these are small.
            boolean found = false;
            for (int i = 0; i < uniqueTimeZones.size(); i++) {
                if (uniqueTimeZones.get(i).getRawOffset() == zone.getRawOffset()) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                if (android.util.TimeUtils.DBG) {
                    android.util.Log.d(android.util.TimeUtils.TAG, (("getTimeZonesWithUniqueOffsets: add unique offset=" + zone.getRawOffset()) + " zone.getID=") + zone.getID());
                }
                uniqueTimeZones.add(zone);
            }
        }
        synchronized(android.util.TimeUtils.sLastUniqueLockObj) {
            // Cache the last result
            android.util.TimeUtils.sLastUniqueZoneOffsets = uniqueTimeZones;
            android.util.TimeUtils.sLastUniqueCountry = country;
            return android.util.TimeUtils.sLastUniqueZoneOffsets;
        }
    }

    /**
     * Returns the time zones for the country, which is the code
     * attribute of the timezone element in time_zones_by_country.xml. Do not modify.
     *
     * @param country
     * 		is a two character country code.
     * @return TimeZone list, maybe empty but never null. Do not modify.
     * @unknown 
     */
    public static java.util.ArrayList<java.util.TimeZone> getTimeZones(java.lang.String country) {
        synchronized(android.util.TimeUtils.sLastLockObj) {
            if ((country != null) && country.equals(android.util.TimeUtils.sLastCountry)) {
                if (android.util.TimeUtils.DBG)
                    android.util.Log.d(android.util.TimeUtils.TAG, ("getTimeZones(" + country) + "): return cached version");

                return android.util.TimeUtils.sLastZones;
            }
        }
        java.util.ArrayList<java.util.TimeZone> tzs = new java.util.ArrayList<java.util.TimeZone>();
        if (country == null) {
            if (android.util.TimeUtils.DBG)
                android.util.Log.d(android.util.TimeUtils.TAG, "getTimeZones(null): return empty list");

            return tzs;
        }
        android.content.res.Resources r = android.content.res.Resources.getSystem();
        android.content.res.XmlResourceParser parser = r.getXml(com.android.internal.R.xml.time_zones_by_country);
        try {
            com.android.internal.util.XmlUtils.beginDocument(parser, "timezones");
            while (true) {
                com.android.internal.util.XmlUtils.nextElement(parser);
                java.lang.String element = parser.getName();
                if ((element == null) || (!element.equals("timezone"))) {
                    break;
                }
                java.lang.String code = parser.getAttributeValue(null, "code");
                if (country.equals(code)) {
                    if (parser.next() == org.xmlpull.v1.XmlPullParser.TEXT) {
                        java.lang.String zoneIdString = parser.getText();
                        java.util.TimeZone tz = java.util.TimeZone.getTimeZone(zoneIdString);
                        if (tz.getID().startsWith("GMT") == false) {
                            // tz.getID doesn't start not "GMT" so its valid
                            tzs.add(tz);
                            if (android.util.TimeUtils.DBG) {
                                android.util.Log.d(android.util.TimeUtils.TAG, (("getTimeZone('" + country) + "'): found tz.getID==") + (tz != null ? tz.getID() : "<no tz>"));
                            }
                        }
                    }
                }
            } 
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.util.TimeUtils.TAG, ("Got xml parser exception getTimeZone('" + country) + "'): e=", e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.util.TimeUtils.TAG, ("Got IO exception getTimeZone('" + country) + "'): e=", e);
        } finally {
            parser.close();
        }
        synchronized(android.util.TimeUtils.sLastLockObj) {
            // Cache the last result;
            android.util.TimeUtils.sLastZones = tzs;
            android.util.TimeUtils.sLastCountry = country;
            return android.util.TimeUtils.sLastZones;
        }
    }

    /**
     * Returns a String indicating the version of the time zone database currently
     * in use.  The format of the string is dependent on the underlying time zone
     * database implementation, but will typically contain the year in which the database
     * was updated plus a letter from a to z indicating changes made within that year.
     *
     * <p>Time zone database updates should be expected to occur periodically due to
     * political and legal changes that cannot be anticipated in advance.  Therefore,
     * when computing the UTC time for a future event, applications should be aware that
     * the results may differ following a time zone database update.  This method allows
     * applications to detect that a database change has occurred, and to recalculate any
     * cached times accordingly.
     *
     * <p>The time zone database may be assumed to change only when the device runtime
     * is restarted.  Therefore, it is not necessary to re-query the database version
     * during the lifetime of an activity.
     */
    public static java.lang.String getTimeZoneDatabaseVersion() {
        return libcore.util.ZoneInfoDB.getInstance().getVersion();
    }

    /**
     *
     *
     * @unknown Field length that can hold 999 days of time
     */
    public static final int HUNDRED_DAY_FIELD_LEN = 19;

    private static final int SECONDS_PER_MINUTE = 60;

    private static final int SECONDS_PER_HOUR = 60 * 60;

    private static final int SECONDS_PER_DAY = (24 * 60) * 60;

    /**
     *
     *
     * @unknown 
     */
    public static final long NANOS_PER_MS = 1000000;

    private static final java.lang.Object sFormatSync = new java.lang.Object();

    private static char[] sFormatStr = new char[android.util.TimeUtils.HUNDRED_DAY_FIELD_LEN + 10];

    private static char[] sTmpFormatStr = new char[android.util.TimeUtils.HUNDRED_DAY_FIELD_LEN + 10];

    private static int accumField(int amt, int suffix, boolean always, int zeropad) {
        if (amt > 999) {
            int num = 0;
            while (amt != 0) {
                num++;
                amt /= 10;
            } 
            return num + suffix;
        } else {
            if ((amt > 99) || (always && (zeropad >= 3))) {
                return 3 + suffix;
            }
            if ((amt > 9) || (always && (zeropad >= 2))) {
                return 2 + suffix;
            }
            if (always || (amt > 0)) {
                return 1 + suffix;
            }
        }
        return 0;
    }

    private static int printFieldLocked(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
        if (always || (amt > 0)) {
            final int startPos = pos;
            if (amt > 999) {
                int tmp = 0;
                while ((amt != 0) && (tmp < android.util.TimeUtils.sTmpFormatStr.length)) {
                    int dig = amt % 10;
                    android.util.TimeUtils.sTmpFormatStr[tmp] = ((char) (dig + '0'));
                    tmp++;
                    amt /= 10;
                } 
                tmp--;
                while (tmp >= 0) {
                    formatStr[pos] = android.util.TimeUtils.sTmpFormatStr[tmp];
                    pos++;
                    tmp--;
                } 
            } else {
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
            }
            formatStr[pos] = suffix;
            pos++;
        }
        return pos;
    }

    private static int formatDurationLocked(long duration, int fieldLen) {
        if (android.util.TimeUtils.sFormatStr.length < fieldLen) {
            android.util.TimeUtils.sFormatStr = new char[fieldLen];
        }
        char[] formatStr = android.util.TimeUtils.sFormatStr;
        if (duration == 0) {
            int pos = 0;
            fieldLen -= 1;
            while (pos < fieldLen) {
                formatStr[pos++] = ' ';
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
        if (seconds >= android.util.TimeUtils.SECONDS_PER_DAY) {
            days = seconds / android.util.TimeUtils.SECONDS_PER_DAY;
            seconds -= days * android.util.TimeUtils.SECONDS_PER_DAY;
        }
        if (seconds >= android.util.TimeUtils.SECONDS_PER_HOUR) {
            hours = seconds / android.util.TimeUtils.SECONDS_PER_HOUR;
            seconds -= hours * android.util.TimeUtils.SECONDS_PER_HOUR;
        }
        if (seconds >= android.util.TimeUtils.SECONDS_PER_MINUTE) {
            minutes = seconds / android.util.TimeUtils.SECONDS_PER_MINUTE;
            seconds -= minutes * android.util.TimeUtils.SECONDS_PER_MINUTE;
        }
        int pos = 0;
        if (fieldLen != 0) {
            int myLen = android.util.TimeUtils.accumField(days, 1, false, 0);
            myLen += android.util.TimeUtils.accumField(hours, 1, myLen > 0, 2);
            myLen += android.util.TimeUtils.accumField(minutes, 1, myLen > 0, 2);
            myLen += android.util.TimeUtils.accumField(seconds, 1, myLen > 0, 2);
            myLen += android.util.TimeUtils.accumField(millis, 2, true, myLen > 0 ? 3 : 0) + 1;
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
        pos = android.util.TimeUtils.printFieldLocked(formatStr, days, 'd', pos, false, 0);
        pos = android.util.TimeUtils.printFieldLocked(formatStr, hours, 'h', pos, pos != start, zeropad ? 2 : 0);
        pos = android.util.TimeUtils.printFieldLocked(formatStr, minutes, 'm', pos, pos != start, zeropad ? 2 : 0);
        pos = android.util.TimeUtils.printFieldLocked(formatStr, seconds, 's', pos, pos != start, zeropad ? 2 : 0);
        pos = android.util.TimeUtils.printFieldLocked(formatStr, millis, 'm', pos, true, zeropad && (pos != start) ? 3 : 0);
        formatStr[pos] = 's';
        return pos + 1;
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.lang.StringBuilder builder) {
        synchronized(android.util.TimeUtils.sFormatSync) {
            int len = android.util.TimeUtils.formatDurationLocked(duration, 0);
            builder.append(android.util.TimeUtils.sFormatStr, 0, len);
        }
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.io.PrintWriter pw, int fieldLen) {
        synchronized(android.util.TimeUtils.sFormatSync) {
            int len = android.util.TimeUtils.formatDurationLocked(duration, fieldLen);
            pw.print(new java.lang.String(android.util.TimeUtils.sFormatStr, 0, len));
        }
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static void formatDuration(long duration, java.io.PrintWriter pw) {
        android.util.TimeUtils.formatDuration(duration, pw, 0);
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
        android.util.TimeUtils.formatDuration(time - now, pw, 0);
    }

    /**
     *
     *
     * @unknown Just for debugging; not internationalized.
     */
    public static java.lang.String formatUptime(long time) {
        final long diff = time - android.os.SystemClock.uptimeMillis();
        if (diff > 0) {
            return ((time + " (in ") + diff) + " ms)";
        }
        if (diff < 0) {
            return ((time + " (") + (-diff)) + " ms ago)";
        }
        return time + " (now)";
    }

    /**
     * Convert a System.currentTimeMillis() value to a time of day value like
     * that printed in logs. MM-DD HH:MM:SS.MMM
     *
     * @param millis
     * 		since the epoch (1/1/1970)
     * @return String representation of the time.
     * @unknown 
     */
    public static java.lang.String logTimeOfDay(long millis) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        if (millis >= 0) {
            c.setTimeInMillis(millis);
            return java.lang.String.format("%tm-%td %tH:%tM:%tS.%tL", c, c, c, c, c, c);
        } else {
            return java.lang.Long.toString(millis);
        }
    }

    /**
     * {@hide }
     */
    public static java.lang.String formatForLogging(long millis) {
        if (millis <= 0) {
            return "unknown";
        } else {
            return android.util.TimeUtils.sLoggingFormat.format(new java.util.Date(millis));
        }
    }
}

