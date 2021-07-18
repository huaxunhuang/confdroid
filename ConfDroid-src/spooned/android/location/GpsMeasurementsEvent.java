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
 * Events are delivered to registered instances of {@link Listener}.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class GpsMeasurementsEvent implements android.os.Parcelable {
    /**
     * The system does not support tracking of GPS Measurements. This status will not change in the
     * future.
     */
    public static final int STATUS_NOT_SUPPORTED = 0;

    /**
     * GPS Measurements are successfully being tracked, it will receive updates once they are
     * available.
     */
    public static final int STATUS_READY = 1;

    /**
     * GPS provider or Location is disabled, updates will not be received until they are enabled.
     */
    public static final int STATUS_GPS_LOCATION_DISABLED = 2;

    private final android.location.GpsClock mClock;

    private final java.util.Collection<android.location.GpsMeasurement> mReadOnlyMeasurements;

    /**
     * Used for receiving GPS satellite measurements from the GPS engine.
     * Each measurement contains raw and computed data identifying a satellite.
     * You can implement this interface and call {@link LocationManager#addGpsMeasurementListener}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public interface Listener {
        /**
         * Returns the latest collected GPS Measurements.
         */
        void onGpsMeasurementsReceived(android.location.GpsMeasurementsEvent eventArgs);

        /**
         * Returns the latest status of the GPS Measurements sub-system.
         */
        void onStatusChanged(int status);
    }

    public GpsMeasurementsEvent(android.location.GpsClock clock, android.location.GpsMeasurement[] measurements) {
        if (clock == null) {
            throw new java.security.InvalidParameterException("Parameter 'clock' must not be null.");
        }
        if ((measurements == null) || (measurements.length == 0)) {
            throw new java.security.InvalidParameterException("Parameter 'measurements' must not be null or empty.");
        }
        mClock = clock;
        java.util.Collection<android.location.GpsMeasurement> measurementCollection = java.util.Arrays.asList(measurements);
        mReadOnlyMeasurements = java.util.Collections.unmodifiableCollection(measurementCollection);
    }

    @android.annotation.NonNull
    public android.location.GpsClock getClock() {
        return mClock;
    }

    /**
     * Gets a read-only collection of measurements associated with the current event.
     */
    @android.annotation.NonNull
    public java.util.Collection<android.location.GpsMeasurement> getMeasurements() {
        return mReadOnlyMeasurements;
    }

    public static final android.os.Parcelable.Creator<android.location.GpsMeasurementsEvent> CREATOR = new android.os.Parcelable.Creator<android.location.GpsMeasurementsEvent>() {
        @java.lang.Override
        public android.location.GpsMeasurementsEvent createFromParcel(android.os.Parcel in) {
            java.lang.ClassLoader classLoader = getClass().getClassLoader();
            android.location.GpsClock clock = in.readParcelable(classLoader);
            int measurementsLength = in.readInt();
            android.location.GpsMeasurement[] measurementsArray = new android.location.GpsMeasurement[measurementsLength];
            in.readTypedArray(measurementsArray, android.location.GpsMeasurement.CREATOR);
            return new android.location.GpsMeasurementsEvent(clock, measurementsArray);
        }

        @java.lang.Override
        public android.location.GpsMeasurementsEvent[] newArray(int size) {
            return new android.location.GpsMeasurementsEvent[size];
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
        android.location.GpsMeasurement[] measurementsArray = mReadOnlyMeasurements.toArray(new android.location.GpsMeasurement[measurementsCount]);
        parcel.writeInt(measurementsArray.length);
        parcel.writeTypedArray(measurementsArray, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("[ GpsMeasurementsEvent:\n\n");
        builder.append(mClock.toString());
        builder.append("\n");
        for (android.location.GpsMeasurement measurement : mReadOnlyMeasurements) {
            builder.append(measurement.toString());
            builder.append("\n");
        }
        builder.append("]");
        return builder.toString();
    }
}

