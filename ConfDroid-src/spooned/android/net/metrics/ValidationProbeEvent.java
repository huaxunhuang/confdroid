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
 * An event recorded by NetworkMonitor when sending a probe for finding captive portals.
 * {@hide }
 */
@android.annotation.SystemApi
public final class ValidationProbeEvent implements android.os.Parcelable {
    public static final int PROBE_DNS = 0;

    public static final int PROBE_HTTP = 1;

    public static final int PROBE_HTTPS = 2;

    public static final int PROBE_PAC = 3;

    /**
     * {@hide }
     */
    public static final int PROBE_FALLBACK = 4;

    public static final int DNS_FAILURE = 0;

    public static final int DNS_SUCCESS = 1;

    /**
     * {@hide }
     */
    @android.annotation.IntDef({ android.net.metrics.ValidationProbeEvent.PROBE_DNS, android.net.metrics.ValidationProbeEvent.PROBE_HTTP, android.net.metrics.ValidationProbeEvent.PROBE_HTTPS, android.net.metrics.ValidationProbeEvent.PROBE_PAC })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ProbeType {}

    /**
     * {@hide }
     */
    @android.annotation.IntDef({ android.net.metrics.ValidationProbeEvent.DNS_FAILURE, android.net.metrics.ValidationProbeEvent.DNS_SUCCESS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ReturnCode {}

    public final int netId;

    public final long durationMs;

    @android.net.metrics.ValidationProbeEvent.ProbeType
    public final int probeType;

    @android.net.metrics.ValidationProbeEvent.ReturnCode
    public final int returnCode;

    /**
     * {@hide }
     */
    public ValidationProbeEvent(int netId, long durationMs, @android.net.metrics.ValidationProbeEvent.ProbeType
    int probeType, @android.net.metrics.ValidationProbeEvent.ReturnCode
    int returnCode) {
        this.netId = netId;
        this.durationMs = durationMs;
        this.probeType = probeType;
        this.returnCode = returnCode;
    }

    private ValidationProbeEvent(android.os.Parcel in) {
        netId = in.readInt();
        durationMs = in.readLong();
        probeType = in.readInt();
        returnCode = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(netId);
        out.writeLong(durationMs);
        out.writeInt(probeType);
        out.writeInt(returnCode);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.ValidationProbeEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.ValidationProbeEvent>() {
        public android.net.metrics.ValidationProbeEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.ValidationProbeEvent(in);
        }

        public android.net.metrics.ValidationProbeEvent[] newArray(int size) {
            return new android.net.metrics.ValidationProbeEvent[size];
        }
    };

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String getProbeName(int probeType) {
        return android.net.metrics.ValidationProbeEvent.Decoder.constants.get(probeType, "PROBE_???");
    }

    public static void logEvent(int netId, long durationMs, int probeType, int returnCode) {
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("ValidationProbeEvent(%d, %s:%d, %dms)", netId, android.net.metrics.ValidationProbeEvent.getProbeName(probeType), returnCode, durationMs);
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.ValidationProbeEvent.class }, new java.lang.String[]{ "PROBE_" });
    }
}

