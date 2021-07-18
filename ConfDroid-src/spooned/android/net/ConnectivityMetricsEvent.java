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
package android.net;


/**
 * {@hide }
 */
@android.annotation.SystemApi
public final class ConnectivityMetricsEvent implements android.os.Parcelable {
    /**
     * The time when this event was collected, as returned by System.currentTimeMillis().
     */
    public final long timestamp;

    /**
     * The subsystem that generated the event. One of the COMPONENT_TAG_xxx constants.
     */
    public final int componentTag;

    /**
     * The subsystem-specific event ID.
     */
    public final int eventTag;

    /**
     * Opaque event-specific data.
     */
    public final android.os.Parcelable data;

    public ConnectivityMetricsEvent(long timestamp, int componentTag, int eventTag, android.os.Parcelable data) {
        this.timestamp = timestamp;
        this.componentTag = componentTag;
        this.eventTag = eventTag;
        this.data = data;
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.ConnectivityMetricsEvent> CREATOR = new android.os.Parcelable.Creator<android.net.ConnectivityMetricsEvent>() {
        public android.net.ConnectivityMetricsEvent createFromParcel(android.os.Parcel source) {
            final long timestamp = source.readLong();
            final int componentTag = source.readInt();
            final int eventTag = source.readInt();
            final android.os.Parcelable data = source.readParcelable(null);
            return new android.net.ConnectivityMetricsEvent(timestamp, componentTag, eventTag, data);
        }

        public android.net.ConnectivityMetricsEvent[] newArray(int size) {
            return new android.net.ConnectivityMetricsEvent[size];
        }
    };

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeInt(componentTag);
        dest.writeInt(eventTag);
        dest.writeParcelable(data, 0);
    }

    public java.lang.String toString() {
        return java.lang.String.format("ConnectivityMetricsEvent(%tT.%tL, %d, %d): %s", timestamp, timestamp, componentTag, eventTag, data);
    }

    /**
     * {@hide }
     */
    @android.annotation.SystemApi
    public static final class Reference implements android.os.Parcelable {
        private long mValue;

        public Reference(long ref) {
            this.mValue = ref;
        }

        /**
         * Implement the Parcelable interface
         */
        public static final android.os.Parcelable.Creator<android.net.ConnectivityMetricsEvent.Reference> CREATOR = new android.os.Parcelable.Creator<android.net.ConnectivityMetricsEvent.Reference>() {
            public android.net.ConnectivityMetricsEvent.Reference createFromParcel(android.os.Parcel source) {
                return new android.net.ConnectivityMetricsEvent.Reference(source.readLong());
            }

            public android.net.ConnectivityMetricsEvent.Reference[] newArray(int size) {
                return new android.net.ConnectivityMetricsEvent.Reference[size];
            }
        };

        /**
         * Implement the Parcelable interface
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * Implement the Parcelable interface
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeLong(mValue);
        }

        public void readFromParcel(android.os.Parcel in) {
            mValue = in.readLong();
        }

        public long getValue() {
            return mValue;
        }

        public void setValue(long val) {
            mValue = val;
        }
    }
}

