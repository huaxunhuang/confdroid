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
 * A class that represents an Activity Recognition Event.
 *
 * @unknown 
 */
public class ActivityRecognitionEvent implements android.os.Parcelable {
    private final java.lang.String mActivity;

    private final int mEventType;

    private final long mTimestampNs;

    public ActivityRecognitionEvent(java.lang.String activity, int eventType, long timestampNs) {
        mActivity = activity;
        mEventType = eventType;
        mTimestampNs = timestampNs;
    }

    public java.lang.String getActivity() {
        return mActivity;
    }

    public int getEventType() {
        return mEventType;
    }

    public long getTimestampNs() {
        return mTimestampNs;
    }

    public static final android.os.Parcelable.Creator<android.hardware.location.ActivityRecognitionEvent> CREATOR = new android.os.Parcelable.Creator<android.hardware.location.ActivityRecognitionEvent>() {
        @java.lang.Override
        public android.hardware.location.ActivityRecognitionEvent createFromParcel(android.os.Parcel source) {
            java.lang.String activity = source.readString();
            int eventType = source.readInt();
            long timestampNs = source.readLong();
            return new android.hardware.location.ActivityRecognitionEvent(activity, eventType, timestampNs);
        }

        @java.lang.Override
        public android.hardware.location.ActivityRecognitionEvent[] newArray(int size) {
            return new android.hardware.location.ActivityRecognitionEvent[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mActivity);
        parcel.writeInt(mEventType);
        parcel.writeLong(mTimestampNs);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("Activity='%s', EventType=%s, TimestampNs=%s", mActivity, mEventType, mTimestampNs);
    }
}

