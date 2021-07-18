/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.bluetooth.client.map.utils;


public final class ObexTime {
    private java.util.Date mDate;

    public ObexTime(java.lang.String time) {
        /* match OBEX time string: YYYYMMDDTHHMMSS with optional UTF offset
        +/-hhmm
         */
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})(([+-])(\\d{2})(\\d{2}))?");
        java.util.regex.Matcher m = p.matcher(time);
        if (m.matches()) {
            /* matched groups are numberes as follows: YYYY MM DD T HH MM SS +
            hh mm ^^^^ ^^ ^^ ^^ ^^ ^^ ^ ^^ ^^ 1 2 3 4 5 6 8 9 10 all groups
            are guaranteed to be numeric so conversion will always succeed
            (except group 8 which is either + or -)
             */
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.lang.Integer.parseInt(m.group(1)), java.lang.Integer.parseInt(m.group(2)) - 1, java.lang.Integer.parseInt(m.group(3)), java.lang.Integer.parseInt(m.group(4)), java.lang.Integer.parseInt(m.group(5)), java.lang.Integer.parseInt(m.group(6)));
            /* if 7th group is matched then we have UTC offset information
            included
             */
            if (m.group(7) != null) {
                int ohh = java.lang.Integer.parseInt(m.group(9));
                int omm = java.lang.Integer.parseInt(m.group(10));
                /* time zone offset is specified in miliseconds */
                int offset = (((ohh * 60) + omm) * 60) * 1000;
                if (m.group(8).equals("-")) {
                    offset = -offset;
                }
                java.util.TimeZone tz = java.util.TimeZone.getTimeZone("UTC");
                tz.setRawOffset(offset);
                cal.setTimeZone(tz);
            }
            mDate = cal.getTime();
        }
    }

    public ObexTime(java.util.Date date) {
        mDate = date;
    }

    public java.util.Date getTime() {
        return mDate;
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (mDate == null) {
            return null;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(mDate);
        /* note that months are numbered stating from 0 */
        return java.lang.String.format(java.util.Locale.US, "%04d%02d%02dT%02d%02d%02d", cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DATE), cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), cal.get(java.util.Calendar.SECOND));
    }
}

