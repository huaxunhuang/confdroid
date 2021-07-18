/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.hardware.location;


/**
 * A class that represents an event for each change in the state of a monitoring system.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class GeofenceHardwareMonitorEvent implements android.os.Parcelable {
    private final int mMonitoringType;

    private final int mMonitoringStatus;

    private final int mSourceTechnologies;

    private final android.location.Location mLocation;

    public GeofenceHardwareMonitorEvent(int monitoringType, int monitoringStatus, int sourceTechnologies, android.location.Location location) {
        mMonitoringType = monitoringType;
        mMonitoringStatus = monitoringStatus;
        mSourceTechnologies = sourceTechnologies;
        mLocation = location;
    }

    /**
     * Returns the type of the monitoring system that has a change on its state.
     */
    public int getMonitoringType() {
        return mMonitoringType;
    }

    /**
     * Returns the new status associated with the monitoring system.
     */
    public int getMonitoringStatus() {
        return mMonitoringStatus;
    }

    /**
     * Returns the source technologies that the status is associated to.
     */
    public int getSourceTechnologies() {
        return mSourceTechnologies;
    }

    /**
     * Returns the last known location according to the monitoring system.
     */
    public android.location.Location getLocation() {
        return mLocation;
    }

    public static final android.os.Parcelable.Creator<android.hardware.location.GeofenceHardwareMonitorEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.location.GeofenceHardwareMonitorEvent>() {
        @java.lang.Override
        public android.hardware.location.GeofenceHardwareMonitorEvent createFromParcel(android.os.Parcel source) {
            java.lang.ClassLoader classLoader = android.hardware.location.GeofenceHardwareMonitorEvent.class.getClassLoader();
            int monitoringType = source.readInt();
            int monitoringStatus = source.readInt();
            int sourceTechnologies = source.readInt();
            android.location.Location location = source.readParcelable(classLoader);
            return new android.hardware.location.GeofenceHardwareMonitorEvent(monitoringType, monitoringStatus, sourceTechnologies, location);
        }

        @java.lang.Override
        public android.hardware.location.GeofenceHardwareMonitorEvent[] newArray(int size) {
            return new android.hardware.location.GeofenceHardwareMonitorEvent[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(mMonitoringType);
        parcel.writeInt(mMonitoringStatus);
        parcel.writeInt(mSourceTechnologies);
        parcel.writeParcelable(mLocation, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("GeofenceHardwareMonitorEvent: type=%d, status=%d, sources=%d, location=%s", mMonitoringType, mMonitoringStatus, mSourceTechnologies, mLocation);
    }
}

