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
 * An event logged for an interface with APF capabilities when its IpManager state machine exits.
 * {@hide }
 */
@android.annotation.SystemApi
public final class ApfStats implements android.os.Parcelable {
    public final long durationMs;// time interval in milliseconds these stastistics covers


    public final int receivedRas;// number of received RAs


    public final int matchingRas;// number of received RAs matching a known RA


    public final int droppedRas;// number of received RAs ignored due to the MAX_RAS limit


    public final int zeroLifetimeRas;// number of received RAs with a minimum lifetime of 0


    public final int parseErrors;// number of received RAs that could not be parsed


    public final int programUpdates;// number of APF program updates


    public final int maxProgramSize;// maximum APF program size advertised by hardware


    /**
     * {@hide }
     */
    public ApfStats(long durationMs, int receivedRas, int matchingRas, int droppedRas, int zeroLifetimeRas, int parseErrors, int programUpdates, int maxProgramSize) {
        this.durationMs = durationMs;
        this.receivedRas = receivedRas;
        this.matchingRas = matchingRas;
        this.droppedRas = droppedRas;
        this.zeroLifetimeRas = zeroLifetimeRas;
        this.parseErrors = parseErrors;
        this.programUpdates = programUpdates;
        this.maxProgramSize = maxProgramSize;
    }

    private ApfStats(android.os.Parcel in) {
        this.durationMs = in.readLong();
        this.receivedRas = in.readInt();
        this.matchingRas = in.readInt();
        this.droppedRas = in.readInt();
        this.zeroLifetimeRas = in.readInt();
        this.parseErrors = in.readInt();
        this.programUpdates = in.readInt();
        this.maxProgramSize = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(durationMs);
        out.writeInt(receivedRas);
        out.writeInt(matchingRas);
        out.writeInt(droppedRas);
        out.writeInt(zeroLifetimeRas);
        out.writeInt(parseErrors);
        out.writeInt(programUpdates);
        out.writeInt(maxProgramSize);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder("ApfStats(").append(java.lang.String.format("%dms ", durationMs)).append(java.lang.String.format("%dB RA: {", maxProgramSize)).append(java.lang.String.format("%d received, ", receivedRas)).append(java.lang.String.format("%d matching, ", matchingRas)).append(java.lang.String.format("%d dropped, ", droppedRas)).append(java.lang.String.format("%d zero lifetime, ", zeroLifetimeRas)).append(java.lang.String.format("%d parse errors, ", parseErrors)).append(java.lang.String.format("%d program updates})", programUpdates)).toString();
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.ApfStats> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.ApfStats>() {
        public android.net.metrics.ApfStats createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.ApfStats(in);
        }

        public android.net.metrics.ApfStats[] newArray(int size) {
            return new android.net.metrics.ApfStats[size];
        }
    };
}

