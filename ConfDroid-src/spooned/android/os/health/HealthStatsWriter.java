/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.os.health;


/**
 * Class to write the health stats data into a parcel, so it can then be
 * retrieved via a {@link HealthStats} object.
 *
 * There is an attempt to keep this class as low overhead as possible, for
 * example storing an int[] and a long[] instead of a TimerStat[].
 *
 * @unknown 
 */
public class HealthStatsWriter {
    private final android.os.health.HealthKeys.Constants mConstants;

    // TimerStat fields
    private final boolean[] mTimerFields;

    private final int[] mTimerCounts;

    private final long[] mTimerTimes;

    // Measurement fields
    private final boolean[] mMeasurementFields;

    private final long[] mMeasurementValues;

    // Stats fields
    private final android.util.ArrayMap<java.lang.String, android.os.health.HealthStatsWriter>[] mStatsValues;

    // Timers fields
    private final android.util.ArrayMap<java.lang.String, android.os.health.TimerStat>[] mTimersValues;

    // Measurements fields
    private final android.util.ArrayMap<java.lang.String, java.lang.Long>[] mMeasurementsValues;

    /**
     * Construct a HealthStatsWriter object with the given constants.
     *
     * The "getDataType()" of the resulting HealthStats object will be the
     * short name of the java class that the Constants object was initalized
     * with.
     */
    public HealthStatsWriter(android.os.health.HealthKeys.Constants constants) {
        mConstants = constants;
        // TimerStat
        final int timerCount = constants.getSize(android.os.health.HealthKeys.TYPE_TIMER);
        mTimerFields = new boolean[timerCount];
        mTimerCounts = new int[timerCount];
        mTimerTimes = new long[timerCount];
        // Measurement
        final int measurementCount = constants.getSize(android.os.health.HealthKeys.TYPE_MEASUREMENT);
        mMeasurementFields = new boolean[measurementCount];
        mMeasurementValues = new long[measurementCount];
        // Stats
        final int statsCount = constants.getSize(android.os.health.HealthKeys.TYPE_STATS);
        mStatsValues = new android.util.ArrayMap[statsCount];
        // Timers
        final int timersCount = constants.getSize(android.os.health.HealthKeys.TYPE_TIMERS);
        mTimersValues = new android.util.ArrayMap[timersCount];
        // Measurements
        final int measurementsCount = constants.getSize(android.os.health.HealthKeys.TYPE_MEASUREMENTS);
        mMeasurementsValues = new android.util.ArrayMap[measurementsCount];
    }

    /**
     * Add a timer for the given key.
     */
    public void addTimer(int timerId, int count, long time) {
        final int index = mConstants.getIndex(android.os.health.HealthKeys.TYPE_TIMER, timerId);
        mTimerFields[index] = true;
        mTimerCounts[index] = count;
        mTimerTimes[index] = time;
    }

    /**
     * Add a measurement for the given key.
     */
    public void addMeasurement(int measurementId, long value) {
        final int index = mConstants.getIndex(android.os.health.HealthKeys.TYPE_MEASUREMENT, measurementId);
        mMeasurementFields[index] = true;
        mMeasurementValues[index] = value;
    }

    /**
     * Add a recursive HealthStats object for the given key and string name. The value
     * is stored as a HealthStatsWriter until this object is written to a parcel, so
     * don't attempt to reuse the HealthStatsWriter.
     *
     * The value field should not be null.
     */
    public void addStats(int key, java.lang.String name, android.os.health.HealthStatsWriter value) {
        final int index = mConstants.getIndex(android.os.health.HealthKeys.TYPE_STATS, key);
        android.util.ArrayMap<java.lang.String, android.os.health.HealthStatsWriter> map = mStatsValues[index];
        if (map == null) {
            map = mStatsValues[index] = new android.util.ArrayMap<java.lang.String, android.os.health.HealthStatsWriter>(1);
        }
        map.put(name, value);
    }

    /**
     * Add a TimerStat for the given key and string name.
     *
     * The value field should not be null.
     */
    public void addTimers(int key, java.lang.String name, android.os.health.TimerStat value) {
        final int index = mConstants.getIndex(android.os.health.HealthKeys.TYPE_TIMERS, key);
        android.util.ArrayMap<java.lang.String, android.os.health.TimerStat> map = mTimersValues[index];
        if (map == null) {
            map = mTimersValues[index] = new android.util.ArrayMap<java.lang.String, android.os.health.TimerStat>(1);
        }
        map.put(name, value);
    }

    /**
     * Add a measurement for the given key and string name.
     */
    public void addMeasurements(int key, java.lang.String name, long value) {
        final int index = mConstants.getIndex(android.os.health.HealthKeys.TYPE_MEASUREMENTS, key);
        android.util.ArrayMap<java.lang.String, java.lang.Long> map = mMeasurementsValues[index];
        if (map == null) {
            map = mMeasurementsValues[index] = new android.util.ArrayMap<java.lang.String, java.lang.Long>(1);
        }
        map.put(name, value);
    }

    /**
     * Flattens the data in this HealthStatsWriter to the Parcel format
     * that can be unparceled into a HealthStat.
     *
     * @unknown (Called flattenToParcel because this HealthStatsWriter itself is
    not parcelable and we don't flatten all the business about the
    HealthKeys.Constants, only the values that were actually supplied)
     */
    public void flattenToParcel(android.os.Parcel out) {
        int[] keys;
        // Header fields
        out.writeString(mConstants.getDataType());
        // TimerStat fields
        out.writeInt(android.os.health.HealthStatsWriter.countBooleanArray(mTimerFields));
        keys = mConstants.getKeys(android.os.health.HealthKeys.TYPE_TIMER);
        for (int i = 0; i < keys.length; i++) {
            if (mTimerFields[i]) {
                out.writeInt(keys[i]);
                out.writeInt(mTimerCounts[i]);
                out.writeLong(mTimerTimes[i]);
            }
        }
        // Measurement fields
        out.writeInt(android.os.health.HealthStatsWriter.countBooleanArray(mMeasurementFields));
        keys = mConstants.getKeys(android.os.health.HealthKeys.TYPE_MEASUREMENT);
        for (int i = 0; i < keys.length; i++) {
            if (mMeasurementFields[i]) {
                out.writeInt(keys[i]);
                out.writeLong(mMeasurementValues[i]);
            }
        }
        // Stats
        out.writeInt(android.os.health.HealthStatsWriter.countObjectArray(mStatsValues));
        keys = mConstants.getKeys(android.os.health.HealthKeys.TYPE_STATS);
        for (int i = 0; i < keys.length; i++) {
            if (mStatsValues[i] != null) {
                out.writeInt(keys[i]);
                android.os.health.HealthStatsWriter.writeHealthStatsWriterMap(out, mStatsValues[i]);
            }
        }
        // Timers
        out.writeInt(android.os.health.HealthStatsWriter.countObjectArray(mTimersValues));
        keys = mConstants.getKeys(android.os.health.HealthKeys.TYPE_TIMERS);
        for (int i = 0; i < keys.length; i++) {
            if (mTimersValues[i] != null) {
                out.writeInt(keys[i]);
                android.os.health.HealthStatsWriter.writeParcelableMap(out, mTimersValues[i]);
            }
        }
        // Measurements
        out.writeInt(android.os.health.HealthStatsWriter.countObjectArray(mMeasurementsValues));
        keys = mConstants.getKeys(android.os.health.HealthKeys.TYPE_MEASUREMENTS);
        for (int i = 0; i < keys.length; i++) {
            if (mMeasurementsValues[i] != null) {
                out.writeInt(keys[i]);
                android.os.health.HealthStatsWriter.writeLongsMap(out, mMeasurementsValues[i]);
            }
        }
    }

    /**
     * Count how many of the fields have been set.
     */
    private static int countBooleanArray(boolean[] fields) {
        int count = 0;
        final int N = fields.length;
        for (int i = 0; i < N; i++) {
            if (fields[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count how many of the fields have been set.
     */
    private static <T extends java.lang.Object> int countObjectArray(T[] fields) {
        int count = 0;
        final int N = fields.length;
        for (int i = 0; i < N; i++) {
            if (fields[i] != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Write a map of String to HealthStatsWriter to the Parcel.
     */
    private static void writeHealthStatsWriterMap(android.os.Parcel out, android.util.ArrayMap<java.lang.String, android.os.health.HealthStatsWriter> map) {
        final int N = map.size();
        out.writeInt(N);
        for (int i = 0; i < N; i++) {
            out.writeString(map.keyAt(i));
            map.valueAt(i).flattenToParcel(out);
        }
    }

    /**
     * Write a map of String to Parcelables to the Parcel.
     */
    private static <T extends android.os.Parcelable> void writeParcelableMap(android.os.Parcel out, android.util.ArrayMap<java.lang.String, T> map) {
        final int N = map.size();
        out.writeInt(N);
        for (int i = 0; i < N; i++) {
            out.writeString(map.keyAt(i));
            map.valueAt(i).writeToParcel(out, 0);
        }
    }

    /**
     * Write a map of String to Longs to the Parcel.
     */
    private static void writeLongsMap(android.os.Parcel out, android.util.ArrayMap<java.lang.String, java.lang.Long> map) {
        final int N = map.size();
        out.writeInt(N);
        for (int i = 0; i < N; i++) {
            out.writeString(map.keyAt(i));
            out.writeLong(map.valueAt(i));
        }
    }
}

