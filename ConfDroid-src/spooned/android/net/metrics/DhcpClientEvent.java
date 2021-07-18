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
 * An event recorded when a DhcpClient state machine transitions to a new state.
 * {@hide }
 */
@android.annotation.SystemApi
public final class DhcpClientEvent implements android.os.Parcelable {
    // Names for recording DhcpClient pseudo-state transitions.
    /**
     * {@hide } Represents transitions from DhcpInitState to DhcpBoundState
     */
    public static final java.lang.String INITIAL_BOUND = "InitialBoundState";

    /**
     * {@hide } Represents transitions from and to DhcpBoundState via DhcpRenewingState
     */
    public static final java.lang.String RENEWING_BOUND = "RenewingBoundState";

    public final java.lang.String ifName;

    public final java.lang.String msg;

    public final int durationMs;

    /**
     * {@hide }
     */
    public DhcpClientEvent(java.lang.String ifName, java.lang.String msg, int durationMs) {
        this.ifName = ifName;
        this.msg = msg;
        this.durationMs = durationMs;
    }

    private DhcpClientEvent(android.os.Parcel in) {
        this.ifName = in.readString();
        this.msg = in.readString();
        this.durationMs = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(ifName);
        out.writeString(msg);
        out.writeInt(durationMs);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("DhcpClientEvent(%s, %s, %dms)", ifName, msg, durationMs);
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.DhcpClientEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.DhcpClientEvent>() {
        public android.net.metrics.DhcpClientEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.DhcpClientEvent(in);
        }

        public android.net.metrics.DhcpClientEvent[] newArray(int size) {
            return new android.net.metrics.DhcpClientEvent[size];
        }
    };

    public static void logStateEvent(java.lang.String ifName, java.lang.String state) {
    }
}

