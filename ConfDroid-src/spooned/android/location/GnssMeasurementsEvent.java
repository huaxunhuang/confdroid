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
 * limitations under the License
 */
package android.location;


/**
 * A class implementing a container for data associated with a measurement event.
 * Events are delivered to registered instances of {@link Callback}.
 */
public final class GnssMeasurementsEvent implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public static final int STATUS_NOT_SUPPORTED = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int STATUS_READY = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int STATUS_GNSS_LOCATION_DISABLED = 2;

    private final android.location.GnssClock mClock;

    private final java.util.Collection<android.location.GnssMeasurement> mReadOnlyMeasurements;

    /**
     * Used for receiving GNSS satellite measurements from the GNSS engine.
     * Each measurement contains raw and computed data identifying a satellite.
     * You can implement this interface and call
     * {@link LocationManager#registerGnssMeasurementsCallback}.
     */
    public static abstract class Callback {
        /**
         * The status of the GNSS measurements event.
         *
         * @unknown 
         */
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.location.GnssMeasurementsEvent.Callback.STATUS_NOT_SUPPORTED, android.location.GnssMeasurementsEvent.Callback.STATUS_READY, android.location.GnssMeasurementsEvent.Callback.STATUS_LOCATION_DISABLED })
        public @interface GnssMeasurementsStatus {}

        /**
         * The system does not support tracking of GNSS Measurements.
         *
         * <p>This status will not change in the future.
         */
        public static final int STATUS_NOT_SUPPORTED = 0;

        /**
         * GNSS Measurements are successfully being tracked, it will receive updates once they are
         * available.
         */
        public static final int STATUS_READY = 1;

        /**
         * GPS provider or Location is disabled, updates will not be received until they are
         * enabled.
         */
        public static final int STATUS_LOCATION_DISABLED = 2;

        /**
         * Reports the latest collected GNSS Measurements.
         */
        public void onGnssMeasurementsReceived(android.location.GnssMeasurementsEvent eventArgs) {
        }

        /**
         * Reports the latest status of the GNSS Measurements sub-system.
         */
        public void onStatusChanged(@android.location.GnssMeasurementsEvent.Callback.GnssMeasurementsStatus
        int status) {
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public GnssMeasurementsEvent(android.location.GnssClock clock, android.location.GnssMeasurement[] measurements) {
        if (clock == null) {
            throw new java.security.InvalidParameterException("Parameter 'clock' must not be null.");
        }
        if ((measurements == null) || (measurements.length == 0)) {
            mReadOnlyMeasurements = java.util.Collections.emptyList();
        } else {
            java.util.Collection<android.location.GnssMeasurement> measurementCollection = java.util.Arrays.asList(measurements);
            mReadOnlyMeasurements = java.util.Collections.unmodifiableCollection(measurementCollection);
        }
        mClock = clock;
    }

    /**
     * Gets the GNSS receiver clock information associated with the measurements for the current
     * event.
     */
    @android.annotation.NonNull
    public android.location.GnssClock getClock() {
        return mClock;
    }

    /**
     * Gets a read-only collection of measurements associated with the current event.
     */
    @android.annotation.NonNull
    public java.util.Collection<android.location.GnssMeasurement> getMeasurements() {
        return mReadOnlyMeasurements;
    }

    public static final android.os.Parcelable.Creator<android.location.GnssMeasurementsEvent> CREATOR = new android.os.Parcelable.Creator<android.location.GnssMeasurementsEvent>() {
        @java.lang.Override
        public android.location.GnssMeasurementsEvent createFromParcel(android.os.Parcel in) {
            java.lang.ClassLoader classLoader = getClass().getClassLoader();
            android.location.GnssClock clock = in.readParcelable(classLoader);
            int measurementsLength = in.readInt();
            android.location.GnssMeasurement[] measurementsArray = new android.location.GnssMeasurement[measurementsLength];
            in.readTypedArray(measurementsArray, android.location.GnssMeasurement.CREATOR);
            return new android.location.GnssMeasurementsEvent(clock, measurementsArray);
        }

        @java.lang.Override
        public android.location.GnssMeasurementsEvent[] newArray(int size) {
            return new android.location.GnssMeasurementsEvent[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mClock, flags);
        int measurementsCount = mReadOnlyMeasurements.size();
        android.location.GnssMeasurement[] measurementsArray = mReadOnlyMeasurements.toArray(new android.location.GnssMeasurement[measurementsCount]);
        parcel.writeInt(measurementsArray.length);
        parcel.writeTypedArray(measurementsArray, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("[ GnssMeasurementsEvent:\n\n");
        builder.append(mClock.toString());
        builder.append("\n");
        for (android.location.GnssMeasurement measurement : mReadOnlyMeasurements) {
            builder.append(measurement.toString());
            builder.append("\n");
        }
        builder.append("]");
        return builder.toString();
    }
}

