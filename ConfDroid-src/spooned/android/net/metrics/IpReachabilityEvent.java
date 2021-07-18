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
 * An event recorded when IpReachabilityMonitor sends a neighbor probe or receives
 * a neighbor probe result.
 * {@hide }
 */
@android.annotation.SystemApi
public final class IpReachabilityEvent implements android.os.Parcelable {
    // Event types.
    /**
     * A probe forced by IpReachabilityMonitor.
     */
    public static final int PROBE = 1 << 8;

    /**
     * Neighbor unreachable after a forced probe.
     */
    public static final int NUD_FAILED = 2 << 8;

    /**
     * Neighbor unreachable after a forced probe, IP provisioning is also lost.
     */
    public static final int PROVISIONING_LOST = 3 << 8;

    /**
     * {@hide } Neighbor unreachable notification from kernel.
     */
    public static final int NUD_FAILED_ORGANIC = 4 << 8;

    /**
     * {@hide } Neighbor unreachable notification from kernel, IP provisioning is also lost.
     */
    public static final int PROVISIONING_LOST_ORGANIC = 5 << 8;

    public final java.lang.String ifName;

    // eventType byte format (MSB to LSB):
    // byte 0: unused
    // byte 1: unused
    // byte 2: type of event: PROBE, NUD_FAILED, PROVISIONING_LOST
    // byte 3: when byte 2 == PROBE, errno code from RTNetlink or IpReachabilityMonitor.
    public final int eventType;

    /**
     * {@hide }
     */
    public IpReachabilityEvent(java.lang.String ifName, int eventType) {
        this.ifName = ifName;
        this.eventType = eventType;
    }

    private IpReachabilityEvent(android.os.Parcel in) {
        this.ifName = in.readString();
        this.eventType = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(ifName);
        out.writeInt(eventType);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.IpReachabilityEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.IpReachabilityEvent>() {
        public android.net.metrics.IpReachabilityEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.IpReachabilityEvent(in);
        }

        public android.net.metrics.IpReachabilityEvent[] newArray(int size) {
            return new android.net.metrics.IpReachabilityEvent[size];
        }
    };

    public static void logProbeEvent(java.lang.String ifName, int nlErrorCode) {
    }

    public static void logNudFailed(java.lang.String ifName) {
    }

    public static void logProvisioningLost(java.lang.String ifName) {
    }

    /**
     * Returns the NUD failure event type code corresponding to the given conditions.
     * {@hide }
     */
    public static int nudFailureEventType(boolean isFromProbe, boolean isProvisioningLost) {
        if (isFromProbe) {
            return isProvisioningLost ? android.net.metrics.IpReachabilityEvent.PROVISIONING_LOST : android.net.metrics.IpReachabilityEvent.NUD_FAILED;
        } else {
            return isProvisioningLost ? android.net.metrics.IpReachabilityEvent.PROVISIONING_LOST_ORGANIC : android.net.metrics.IpReachabilityEvent.NUD_FAILED_ORGANIC;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        int hi = eventType & 0xff00;
        int lo = eventType & 0xff;
        java.lang.String eventName = android.net.metrics.IpReachabilityEvent.Decoder.constants.get(hi);
        return java.lang.String.format("IpReachabilityEvent(%s, %s:%02x)", ifName, eventName, lo);
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.IpReachabilityEvent.class }, new java.lang.String[]{ "PROBE", "PROVISIONING_", "NUD_" });
    }
}

