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
package android.net.metrics;


/**
 * An event recorded by IpManager when IP provisioning completes for a network or
 * when a network disconnects.
 * {@hide }
 */
@android.annotation.SystemApi
public final class IpManagerEvent implements android.os.Parcelable {
    public static final int PROVISIONING_OK = 1;

    public static final int PROVISIONING_FAIL = 2;

    public static final int COMPLETE_LIFECYCLE = 3;

    /**
     * {@hide }
     */
    @android.annotation.IntDef({ android.net.metrics.IpManagerEvent.PROVISIONING_OK, android.net.metrics.IpManagerEvent.PROVISIONING_FAIL, android.net.metrics.IpManagerEvent.COMPLETE_LIFECYCLE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface EventType {}

    public final java.lang.String ifName;

    @android.net.metrics.IpManagerEvent.EventType
    public final int eventType;

    public final long durationMs;

    /**
     * {@hide }
     */
    public IpManagerEvent(java.lang.String ifName, @android.net.metrics.IpManagerEvent.EventType
    int eventType, long duration) {
        this.ifName = ifName;
        this.eventType = eventType;
        this.durationMs = duration;
    }

    private IpManagerEvent(android.os.Parcel in) {
        this.ifName = in.readString();
        this.eventType = in.readInt();
        this.durationMs = in.readLong();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(ifName);
        out.writeInt(eventType);
        out.writeLong(durationMs);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.IpManagerEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.IpManagerEvent>() {
        public android.net.metrics.IpManagerEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.IpManagerEvent(in);
        }

        public android.net.metrics.IpManagerEvent[] newArray(int size) {
            return new android.net.metrics.IpManagerEvent[size];
        }
    };

    public static void logEvent(int eventType, java.lang.String ifName, long durationMs) {
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("IpManagerEvent(%s, %s, %dms)", ifName, android.net.metrics.IpManagerEvent.Decoder.constants.get(eventType), durationMs);
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.IpManagerEvent.class }, new java.lang.String[]{ "PROVISIONING_", "COMPLETE_" });
    }
}

