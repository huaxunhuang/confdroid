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
package android.hardware.location;


/**
 * A class representing an event for Activity changes.
 *
 * @unknown 
 */
public class ActivityChangedEvent implements android.os.Parcelable {
    private final java.util.List<android.hardware.location.ActivityRecognitionEvent> mActivityRecognitionEvents;

    public ActivityChangedEvent(android.hardware.location.ActivityRecognitionEvent[] activityRecognitionEvents) {
        if (activityRecognitionEvents == null) {
            throw new java.security.InvalidParameterException("Parameter 'activityRecognitionEvents' must not be null.");
        }
        mActivityRecognitionEvents = java.util.Arrays.asList(activityRecognitionEvents);
    }

    @android.annotation.NonNull
    public java.lang.Iterable<android.hardware.location.ActivityRecognitionEvent> getActivityRecognitionEvents() {
        return mActivityRecognitionEvents;
    }

    public static final android.os.Parcelable.Creator<android.hardware.location.ActivityChangedEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.location.ActivityChangedEvent>() {
        @java.lang.Override
        public android.hardware.location.ActivityChangedEvent createFromParcel(android.os.Parcel source) {
            int activityRecognitionEventsLength = source.readInt();
            android.hardware.location.ActivityRecognitionEvent[] activityRecognitionEvents = new android.hardware.location.ActivityRecognitionEvent[activityRecognitionEventsLength];
            source.readTypedArray(activityRecognitionEvents, android.hardware.location.ActivityRecognitionEvent.CREATOR);
            return new android.hardware.location.ActivityChangedEvent(activityRecognitionEvents);
        }

        @java.lang.Override
        public android.hardware.location.ActivityChangedEvent[] newArray(int size) {
            return new android.hardware.location.ActivityChangedEvent[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        android.hardware.location.ActivityRecognitionEvent[] activityRecognitionEventArray = mActivityRecognitionEvents.toArray(new android.hardware.location.ActivityRecognitionEvent[0]);
        parcel.writeInt(activityRecognitionEventArray.length);
        parcel.writeTypedArray(activityRecognitionEventArray, flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("[ ActivityChangedEvent:");
        for (android.hardware.location.ActivityRecognitionEvent event : mActivityRecognitionEvents) {
            builder.append("\n    ");
            builder.append(event.toString());
        }
        builder.append("\n]");
        return builder.toString();
    }
}

