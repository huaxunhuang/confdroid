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
package android.bordeaux.services;


// import java.util.Date;
// TODO: use build in functions in
// import android.text.format.Time;
public class TimeStatsAggregator extends android.bordeaux.services.Aggregator {
    final java.lang.String TAG = "TimeStatsAggregator";

    public static final java.lang.String TIME_OF_WEEK = "Time of Week";

    public static final java.lang.String DAY_OF_WEEK = "Day of Week";

    public static final java.lang.String TIME_OF_DAY = "Time of Day";

    public static final java.lang.String PERIOD_OF_DAY = "Period of Day";

    static final java.lang.String WEEKEND = "Weekend";

    static final java.lang.String WEEKDAY = "Weekday";

    static final java.lang.String MONDAY = "Monday";

    static final java.lang.String TUESDAY = "Tuesday";

    static final java.lang.String WEDNESDAY = "Wednesday";

    static final java.lang.String THURSDAY = "Thursday";

    static final java.lang.String FRIDAY = "Friday";

    static final java.lang.String SATURDAY = "Saturday";

    static final java.lang.String SUNDAY = "Sunday";

    static final java.lang.String MORNING = "Morning";

    static final java.lang.String NOON = "Noon";

    static final java.lang.String AFTERNOON = "AfterNoon";

    static final java.lang.String EVENING = "Evening";

    static final java.lang.String NIGHT = "Night";

    static final java.lang.String LATENIGHT = "LateNight";

    static final java.lang.String DAYTIME = "Daytime";

    static final java.lang.String NIGHTTIME = "Nighttime";

    static java.lang.String mFakeTimeOfDay = null;

    static java.lang.String mFakeDayOfWeek = null;

    static final java.lang.String[] TIME_OF_DAY_VALUES = new java.lang.String[]{ android.bordeaux.services.TimeStatsAggregator.MORNING, android.bordeaux.services.TimeStatsAggregator.NOON, android.bordeaux.services.TimeStatsAggregator.AFTERNOON, android.bordeaux.services.TimeStatsAggregator.EVENING, android.bordeaux.services.TimeStatsAggregator.NIGHT, android.bordeaux.services.TimeStatsAggregator.LATENIGHT };

    static final java.lang.String[] DAY_OF_WEEK_VALUES = new java.lang.String[]{ android.bordeaux.services.TimeStatsAggregator.SUNDAY, android.bordeaux.services.TimeStatsAggregator.MONDAY, android.bordeaux.services.TimeStatsAggregator.TUESDAY, android.bordeaux.services.TimeStatsAggregator.WEDNESDAY, android.bordeaux.services.TimeStatsAggregator.THURSDAY, android.bordeaux.services.TimeStatsAggregator.FRIDAY, android.bordeaux.services.TimeStatsAggregator.SATURDAY };

    static final java.lang.String[] DAYTIME_VALUES = new java.lang.String[]{ android.bordeaux.services.TimeStatsAggregator.MORNING, android.bordeaux.services.TimeStatsAggregator.NOON, android.bordeaux.services.TimeStatsAggregator.AFTERNOON, android.bordeaux.services.TimeStatsAggregator.EVENING };

    public java.lang.String[] getListOfFeatures() {
        java.lang.String[] list = new java.lang.String[4];
        list[0] = android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK;
        list[1] = android.bordeaux.services.TimeStatsAggregator.DAY_OF_WEEK;
        list[2] = android.bordeaux.services.TimeStatsAggregator.TIME_OF_DAY;
        list[3] = android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY;
        return list;
    }

    public java.util.Map<java.lang.String, java.lang.String> getFeatureValue(java.lang.String featureName) {
        java.util.HashMap<java.lang.String, java.lang.String> feature = new java.util.HashMap<java.lang.String, java.lang.String>();
        java.util.HashMap<java.lang.String, java.lang.String> features = android.bordeaux.services.TimeStatsAggregator.getAllTimeFeatures(java.lang.System.currentTimeMillis());
        if (features.containsKey(featureName)) {
            feature.put(featureName, features.get(featureName));
        } else {
            android.util.Log.e(TAG, "There is no Time feature called " + featureName);
        }
        return ((java.util.Map) (feature));
    }

    private static java.lang.String getTimeOfDay(int hour) {
        if ((hour >= 5) && (hour < 11)) {
            return android.bordeaux.services.TimeStatsAggregator.MORNING;
        } else
            if ((hour >= 11) && (hour < 14)) {
                return android.bordeaux.services.TimeStatsAggregator.NOON;
            } else
                if ((hour >= 14) && (hour < 18)) {
                    return android.bordeaux.services.TimeStatsAggregator.AFTERNOON;
                } else
                    if ((hour >= 18) && (hour < 21)) {
                        return android.bordeaux.services.TimeStatsAggregator.EVENING;
                    } else
                        if (((hour >= 21) && (hour < 24)) || ((hour >= 0) && (hour < 1))) {
                            return android.bordeaux.services.TimeStatsAggregator.NIGHT;
                        } else {
                            return android.bordeaux.services.TimeStatsAggregator.LATENIGHT;
                        }




    }

    private static java.lang.String getDayOfWeek(int day) {
        switch (day) {
            case android.text.format.Time.SATURDAY :
                return android.bordeaux.services.TimeStatsAggregator.SATURDAY;
            case android.text.format.Time.SUNDAY :
                return android.bordeaux.services.TimeStatsAggregator.SUNDAY;
            case android.text.format.Time.MONDAY :
                return android.bordeaux.services.TimeStatsAggregator.MONDAY;
            case android.text.format.Time.TUESDAY :
                return android.bordeaux.services.TimeStatsAggregator.TUESDAY;
            case android.text.format.Time.WEDNESDAY :
                return android.bordeaux.services.TimeStatsAggregator.WEDNESDAY;
            case android.text.format.Time.THURSDAY :
                return android.bordeaux.services.TimeStatsAggregator.THURSDAY;
            default :
                return android.bordeaux.services.TimeStatsAggregator.FRIDAY;
        }
    }

    private static java.lang.String getPeriodOfDay(int hour) {
        if ((hour > 6) && (hour < 19)) {
            return android.bordeaux.services.TimeStatsAggregator.DAYTIME;
        } else {
            return android.bordeaux.services.TimeStatsAggregator.NIGHTTIME;
        }
    }

    static java.util.HashMap<java.lang.String, java.lang.String> getAllTimeFeatures(long utcTime) {
        java.util.HashMap<java.lang.String, java.lang.String> features = new java.util.HashMap<java.lang.String, java.lang.String>();
        android.text.format.Time time = new android.text.format.Time();
        time.set(utcTime);
        if ((android.bordeaux.services.TimeStatsAggregator.mFakeTimeOfDay != null) && (android.bordeaux.services.TimeStatsAggregator.mFakeTimeOfDay.length() != 0)) {
            java.util.List<java.lang.String> day_list = java.util.Arrays.asList(android.bordeaux.services.TimeStatsAggregator.DAYTIME_VALUES);
            if (day_list.contains(android.bordeaux.services.TimeStatsAggregator.mFakeTimeOfDay)) {
                features.put(android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY, android.bordeaux.services.TimeStatsAggregator.DAYTIME);
            } else {
                features.put(android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY, android.bordeaux.services.TimeStatsAggregator.NIGHTTIME);
            }
            features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_DAY, android.bordeaux.services.TimeStatsAggregator.mFakeTimeOfDay);
        } else {
            features.put(android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY, android.bordeaux.services.TimeStatsAggregator.getPeriodOfDay(time.hour));
            features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_DAY, android.bordeaux.services.TimeStatsAggregator.getTimeOfDay(time.hour));
        }
        if ((android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek != null) && (android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek.length() != 0)) {
            features.put(android.bordeaux.services.TimeStatsAggregator.DAY_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek);
            if ((android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek.equals(android.bordeaux.services.TimeStatsAggregator.SUNDAY) || android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek.equals(android.bordeaux.services.TimeStatsAggregator.SATURDAY)) || (android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek.equals(android.bordeaux.services.TimeStatsAggregator.FRIDAY) && features.get(android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY).equals(android.bordeaux.services.TimeStatsAggregator.NIGHTTIME))) {
                features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.WEEKEND);
            } else {
                features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.WEEKDAY);
            }
        } else {
            features.put(android.bordeaux.services.TimeStatsAggregator.DAY_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.getDayOfWeek(time.weekDay));
            if (((time.weekDay == android.text.format.Time.SUNDAY) || (time.weekDay == android.text.format.Time.SATURDAY)) || ((time.weekDay == android.text.format.Time.FRIDAY) && features.get(android.bordeaux.services.TimeStatsAggregator.PERIOD_OF_DAY).equals(android.bordeaux.services.TimeStatsAggregator.NIGHTTIME))) {
                features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.WEEKEND);
            } else {
                features.put(android.bordeaux.services.TimeStatsAggregator.TIME_OF_WEEK, android.bordeaux.services.TimeStatsAggregator.WEEKDAY);
            }
        }
        return features;
    }

    // get all possible time_of_day values
    public static java.util.List<java.lang.String> getTimeOfDayValues() {
        return java.util.Arrays.asList(android.bordeaux.services.TimeStatsAggregator.TIME_OF_DAY_VALUES);
    }

    // get all possible day values
    public static java.util.List<java.lang.String> getDayOfWeekValues() {
        return java.util.Arrays.asList(android.bordeaux.services.TimeStatsAggregator.DAY_OF_WEEK_VALUES);
    }

    // set the fake time of day
    // set to "" to disable the fake time
    public static void setFakeTimeOfDay(java.lang.String time_of_day) {
        android.bordeaux.services.TimeStatsAggregator.mFakeTimeOfDay = time_of_day;
    }

    // set the fake day of week
    // set to "" to disable the fake day
    public static void setFakeDayOfWeek(java.lang.String day_of_week) {
        android.bordeaux.services.TimeStatsAggregator.mFakeDayOfWeek = day_of_week;
    }
}

