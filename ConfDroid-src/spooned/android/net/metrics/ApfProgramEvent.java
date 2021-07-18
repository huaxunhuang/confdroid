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
 * An event logged when there is a change or event that requires updating the
 * the APF program in place with a new APF program.
 * {@hide }
 */
@android.annotation.SystemApi
public final class ApfProgramEvent implements android.os.Parcelable {
    // Bitflag constants describing what an Apf program filters.
    // Bits are indexeds from LSB to MSB, starting at index 0.
    public static final int FLAG_MULTICAST_FILTER_ON = 0;

    public static final int FLAG_HAS_IPV4_ADDRESS = 1;

    /**
     * {@hide }
     */
    @android.annotation.IntDef(flag = true, value = { android.net.metrics.ApfProgramEvent.FLAG_MULTICAST_FILTER_ON, android.net.metrics.ApfProgramEvent.FLAG_HAS_IPV4_ADDRESS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Flags {}

    public final long lifetime;// Lifetime of the program in seconds


    public final int filteredRas;// Number of RAs filtered by the APF program


    public final int currentRas;// Total number of current RAs at generation time


    public final int programLength;// Length of the APF program in bytes


    public final int flags;// Bitfield compound of FLAG_* constants


    /**
     * {@hide }
     */
    public ApfProgramEvent(long lifetime, int filteredRas, int currentRas, int programLength, @android.net.metrics.ApfProgramEvent.Flags
    int flags) {
        this.lifetime = lifetime;
        this.filteredRas = filteredRas;
        this.currentRas = currentRas;
        this.programLength = programLength;
        this.flags = flags;
    }

    private ApfProgramEvent(android.os.Parcel in) {
        this.lifetime = in.readLong();
        this.filteredRas = in.readInt();
        this.currentRas = in.readInt();
        this.programLength = in.readInt();
        this.flags = in.readInt();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(lifetime);
        out.writeInt(filteredRas);
        out.writeInt(currentRas);
        out.writeInt(programLength);
        out.writeInt(flags);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String lifetimeString = (lifetime < java.lang.Long.MAX_VALUE) ? lifetime + "s" : "forever";
        return java.lang.String.format("ApfProgramEvent(%d/%d RAs %dB %s %s)", filteredRas, currentRas, programLength, lifetimeString, android.net.metrics.ApfProgramEvent.namesOf(flags));
    }

    public static final android.os.Parcelable.Creator<android.net.metrics.ApfProgramEvent> CREATOR = new android.os.Parcelable.Creator<android.net.metrics.ApfProgramEvent>() {
        public android.net.metrics.ApfProgramEvent createFromParcel(android.os.Parcel in) {
            return new android.net.metrics.ApfProgramEvent(in);
        }

        public android.net.metrics.ApfProgramEvent[] newArray(int size) {
            return new android.net.metrics.ApfProgramEvent[size];
        }
    };

    /**
     * {@hide }
     */
    @android.net.metrics.ApfProgramEvent.Flags
    public static int flagsFor(boolean hasIPv4, boolean multicastFilterOn) {
        int bitfield = 0;
        if (hasIPv4) {
            bitfield |= 1 << android.net.metrics.ApfProgramEvent.FLAG_HAS_IPV4_ADDRESS;
        }
        if (multicastFilterOn) {
            bitfield |= 1 << android.net.metrics.ApfProgramEvent.FLAG_MULTICAST_FILTER_ON;
        }
        return bitfield;
    }

    private static java.lang.String namesOf(@android.net.metrics.ApfProgramEvent.Flags
    int bitfield) {
        java.util.List<java.lang.String> names = new java.util.ArrayList<>(java.lang.Integer.bitCount(bitfield));
        java.util.BitSet set = java.util.BitSet.valueOf(new long[]{ bitfield & java.lang.Integer.MAX_VALUE });
        // Only iterate over flag bits which are set.
        for (int bit = set.nextSetBit(0); bit >= 0; bit = set.nextSetBit(bit + 1)) {
            names.add(android.net.metrics.ApfProgramEvent.Decoder.constants.get(bit));
        }
        return android.text.TextUtils.join("|", names);
    }

    static final class Decoder {
        static final android.util.SparseArray<java.lang.String> constants = com.android.internal.util.MessageUtils.findMessageNames(new java.lang.Class[]{ android.net.metrics.ApfProgramEvent.class }, new java.lang.String[]{ "FLAG_" });
    }
}

