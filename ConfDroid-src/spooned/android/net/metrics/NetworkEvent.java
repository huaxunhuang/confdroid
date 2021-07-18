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
 * {@hide }
 */
@android.annotation.SystemApi
public final class NetworkEvent implements android.os.Parcelable {
    public static final int NETWORK_CONNECTED = 1;

    public static final int NETWORK_VALIDATED = 2;

    public static final int NETWORK_VALIDATION_FAILED = 3;

    public static final int NETWORK_CAPTIVE_PORTAL_FOUND = 4;

    public static final int NETWORK_LINGER = 5;

    public static final int NETWORK_UNLINGER = 6;

    public static final int NETWORK_DISCONNECTED = 7;

    /**
     * {@hide }
     */
    @android.annotation.IntDef({ android.net.metrics.NetworkEvent.NETWORK_CONNECTED, android.net.metrics.NetworkEvent.NETWORK_VALIDATED, android.net.metrics.NetworkEvent.NETWORK_VALIDATION_FAILED, android.net.metrics.NetworkEvent.NETWORK_CAPTIVE_PORTAL_FOUND, android.net.metrics.NetworkEvent.NETWORK_LINGER, android.net.metrics.NetworkEvent.NETWORK_UNLINGER, android.net.metrics.NetworkEvent.NETWORK_DISCONNECTED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface EventType {}

    public final int netId;

    @android.net.metrics.NetworkEvent.EventType
    public final int eventType;

    public final long durationMs;

    /**
     * {@hide }
     */
    public NetworkEvent(int netId, @android.net.metrics.NetworkEvent.EventType
    int eventType, long durationMs) {
        this.netId = netId;
        this.eventType = eventType;
        this.durationMs = durationMs;
    }

    /**
     * {@hide }
     */
    public NetworkEvent(int netId, @android.net.metrics.NetworkEvent.EventType
    int eventType) {
        this(netId, eventType, 0);
    }

    private NetworkEvent(android.os.Parcel in) {
        netId = in.readInt();
        eventType = in.readInt();
        durationMs = in.readLong();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(netId);
        out.writeInt(eventType);
        out.writeLong(durationMs);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.NetworkEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.NetworkEvent>() {
        public android.net.metrics.NetworkEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.NetworkEvent(in);
        }

        public android.net.metrics.NetworkEvent[] newArray(int size) {
            return new android.net.metrics.NetworkEvent[size];
        }
    };

    public static void logEvent(int netId, int eventType) {
    }

    public static void logValidated(int netId, long durationMs) {
    }

    public static void logCaptivePortalFound(int netId, long durationMs) {
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("NetworkEvent(%d, %s, %dms)", netId, android.net.metrics.NetworkEvent.Decoder.constants.get(eventType), durationMs);
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.NetworkEvent.class }, new java.lang.String[]{ "NETWORK_" });
    }
}

