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
package android.net;


/**
 * A network identifier along with a score for the quality of that network.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class ScoredNetwork implements android.os.Parcelable {
    /**
     * A {@link NetworkKey} uniquely identifying this network.
     */
    public final android.net.NetworkKey networkKey;

    /**
     * The {@link RssiCurve} representing the scores for this network based on the RSSI.
     *
     * <p>This field is optional and may be set to null to indicate that no score is available for
     * this network at this time. Such networks, along with networks for which the scorer has not
     * responded, are always prioritized below scored networks, regardless of the score.
     */
    public final android.net.RssiCurve rssiCurve;

    /**
     * A boolean value that indicates whether or not the network is believed to be metered.
     *
     * <p>A network can be classified as metered if the user would be
     * sensitive to heavy data usage on that connection due to monetary costs,
     * data limitations or battery/performance issues. A typical example would
     * be a wifi connection where the user would be charged for usage.
     */
    public final boolean meteredHint;

    /**
     * Construct a new {@link ScoredNetwork}.
     *
     * @param networkKey
     * 		the {@link NetworkKey} uniquely identifying this network.
     * @param rssiCurve
     * 		the {@link RssiCurve} representing the scores for this network based on the
     * 		RSSI. This field is optional, and may be skipped to represent a network which the scorer
     * 		has opted not to score at this time. Passing a null value here is strongly preferred to
     * 		not returning any {@link ScoredNetwork} for a given {@link NetworkKey} because it
     * 		indicates to the system not to request scores for this network in the future, although
     * 		the scorer may choose to issue an out-of-band update at any time.
     */
    public ScoredNetwork(android.net.NetworkKey networkKey, android.net.RssiCurve rssiCurve) {
        /* meteredHint */
        this(networkKey, rssiCurve, false);
    }

    /**
     * Construct a new {@link ScoredNetwork}.
     *
     * @param networkKey
     * 		the {@link NetworkKey} uniquely identifying this network.
     * @param rssiCurve
     * 		the {@link RssiCurve} representing the scores for this network based on the
     * 		RSSI. This field is optional, and may be skipped to represent a network which the scorer
     * 		has opted not to score at this time. Passing a null value here is strongly preferred to
     * 		not returning any {@link ScoredNetwork} for a given {@link NetworkKey} because it
     * 		indicates to the system not to request scores for this network in the future, although
     * 		the scorer may choose to issue an out-of-band update at any time.
     * @param meteredHint
     * 		A boolean value indicating whether or not the network is believed to be
     * 		metered.
     */
    public ScoredNetwork(android.net.NetworkKey networkKey, android.net.RssiCurve rssiCurve, boolean meteredHint) {
        this.networkKey = networkKey;
        this.rssiCurve = rssiCurve;
        this.meteredHint = meteredHint;
    }

    private ScoredNetwork(android.os.Parcel in) {
        networkKey = android.net.NetworkKey.CREATOR.createFromParcel(in);
        if (in.readByte() == 1) {
            rssiCurve = android.net.RssiCurve.CREATOR.createFromParcel(in);
        } else {
            rssiCurve = null;
        }
        meteredHint = in.readByte() != 0;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        networkKey.writeToParcel(out, flags);
        if (rssiCurve != null) {
            out.writeByte(((byte) (1)));
            rssiCurve.writeToParcel(out, flags);
        } else {
            out.writeByte(((byte) (0)));
        }
        out.writeByte(((byte) (meteredHint ? 1 : 0)));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.net.ScoredNetwork that = ((android.net.ScoredNetwork) (o));
        return (java.util.Objects.equals(networkKey, that.networkKey) && java.util.Objects.equals(rssiCurve, that.rssiCurve)) && java.util.Objects.equals(meteredHint, that.meteredHint);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(networkKey, rssiCurve, meteredHint);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("ScoredNetwork[key=" + networkKey) + ",score=") + rssiCurve) + ",meteredHint=") + meteredHint) + "]";
    }

    public static final android.os.Parcelable.Creator<android.net.ScoredNetwork> CREATOR = new android.os.Parcelable.Creator<android.net.ScoredNetwork>() {
        @java.lang.Override
        public android.net.ScoredNetwork createFromParcel(android.os.Parcel in) {
            return new android.net.ScoredNetwork(in);
        }

        @java.lang.Override
        public android.net.ScoredNetwork[] newArray(int size) {
            return new android.net.ScoredNetwork[size];
        }
    };
}

