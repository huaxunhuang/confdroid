/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Collection of active network statistics. Can contain summary details across
 * all interfaces, or details with per-UID granularity. Internally stores data
 * as a large table, closely matching {@code /proc/} data format. This structure
 * optimizes for rapid in-memory comparison, but consider using
 * {@link NetworkStatsHistory} when persisting.
 *
 * @unknown 
 */
public class NetworkStats implements android.os.Parcelable {
    private static final java.lang.String TAG = "NetworkStats";

    /**
     * {@link #iface} value when interface details unavailable.
     */
    public static final java.lang.String IFACE_ALL = null;

    /**
     * {@link #uid} value when UID details unavailable.
     */
    public static final int UID_ALL = -1;

    /**
     * {@link #tag} value matching any tag.
     */
    // TODO: Rename TAG_ALL to TAG_ANY.
    public static final int TAG_ALL = -1;

    /**
     * {@link #set} value for all sets combined, not including debug sets.
     */
    public static final int SET_ALL = -1;

    /**
     * {@link #set} value where background data is accounted.
     */
    public static final int SET_DEFAULT = 0;

    /**
     * {@link #set} value where foreground data is accounted.
     */
    public static final int SET_FOREGROUND = 1;

    /**
     * All {@link #set} value greater than SET_DEBUG_START are debug {@link #set} values.
     */
    public static final int SET_DEBUG_START = 1000;

    /**
     * Debug {@link #set} value when the VPN stats are moved in.
     */
    public static final int SET_DBG_VPN_IN = 1001;

    /**
     * Debug {@link #set} value when the VPN stats are moved out of a vpn UID.
     */
    public static final int SET_DBG_VPN_OUT = 1002;

    /**
     * {@link #tag} value for total data across all tags.
     */
    // TODO: Rename TAG_NONE to TAG_ALL.
    public static final int TAG_NONE = 0;

    /**
     * {@link #set} value for all roaming values.
     */
    public static final int ROAMING_ALL = -1;

    /**
     * {@link #set} value where native, non-roaming data is accounted.
     */
    public static final int ROAMING_NO = 0;

    /**
     * {@link #set} value where roaming data is accounted.
     */
    public static final int ROAMING_YES = 1;

    // TODO: move fields to "mVariable" notation
    /**
     * {@link SystemClock#elapsedRealtime()} timestamp when this data was
     * generated.
     */
    private long elapsedRealtime;

    private int size;

    private int capacity;

    private java.lang.String[] iface;

    private int[] uid;

    private int[] set;

    private int[] tag;

    private int[] roaming;

    private long[] rxBytes;

    private long[] rxPackets;

    private long[] txBytes;

    private long[] txPackets;

    private long[] operations;

    public static class Entry {
        public java.lang.String iface;

        public int uid;

        public int set;

        public int tag;

        /**
         * Note that this is only populated w/ the default value when read from /proc or written
         * to disk. We merge in the correct value when reporting this value to clients of
         * getSummary().
         */
        public int roaming;

        public long rxBytes;

        public long rxPackets;

        public long txBytes;

        public long txPackets;

        public long operations;

        public Entry() {
            this(android.net.NetworkStats.IFACE_ALL, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, 0L, 0L, 0L, 0L, 0L);
        }

        public Entry(long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this(android.net.NetworkStats.IFACE_ALL, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, rxBytes, rxPackets, txBytes, txPackets, operations);
        }

        public Entry(java.lang.String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this(iface, uid, set, tag, android.net.NetworkStats.ROAMING_NO, rxBytes, rxPackets, txBytes, txPackets, operations);
        }

        public Entry(java.lang.String iface, int uid, int set, int tag, int roaming, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this.iface = iface;
            this.uid = uid;
            this.set = set;
            this.tag = tag;
            this.roaming = roaming;
            this.rxBytes = rxBytes;
            this.rxPackets = rxPackets;
            this.txBytes = txBytes;
            this.txPackets = txPackets;
            this.operations = operations;
        }

        public boolean isNegative() {
            return ((((rxBytes < 0) || (rxPackets < 0)) || (txBytes < 0)) || (txPackets < 0)) || (operations < 0);
        }

        public boolean isEmpty() {
            return ((((rxBytes == 0) && (rxPackets == 0)) && (txBytes == 0)) && (txPackets == 0)) && (operations == 0);
        }

        public void add(android.net.NetworkStats.Entry another) {
            this.rxBytes += another.rxBytes;
            this.rxPackets += another.rxPackets;
            this.txBytes += another.txBytes;
            this.txPackets += another.txPackets;
            this.operations += another.operations;
        }

        @java.lang.Override
        public java.lang.String toString() {
            final java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("iface=").append(iface);
            builder.append(" uid=").append(uid);
            builder.append(" set=").append(android.net.NetworkStats.setToString(set));
            builder.append(" tag=").append(android.net.NetworkStats.tagToString(tag));
            builder.append(" roaming=").append(android.net.NetworkStats.roamingToString(roaming));
            builder.append(" rxBytes=").append(rxBytes);
            builder.append(" rxPackets=").append(rxPackets);
            builder.append(" txBytes=").append(txBytes);
            builder.append(" txPackets=").append(txPackets);
            builder.append(" operations=").append(operations);
            return builder.toString();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (o instanceof android.net.NetworkStats.Entry) {
                final android.net.NetworkStats.Entry e = ((android.net.NetworkStats.Entry) (o));
                return (((((((((uid == e.uid) && (set == e.set)) && (tag == e.tag)) && (roaming == e.roaming)) && (rxBytes == e.rxBytes)) && (rxPackets == e.rxPackets)) && (txBytes == e.txBytes)) && (txPackets == e.txPackets)) && (operations == e.operations)) && iface.equals(e.iface);
            }
            return false;
        }
    }

    public NetworkStats(long elapsedRealtime, int initialSize) {
        this.elapsedRealtime = elapsedRealtime;
        this.size = 0;
        if (initialSize >= 0) {
            this.capacity = initialSize;
            this.iface = new java.lang.String[initialSize];
            this.uid = new int[initialSize];
            this.set = new int[initialSize];
            this.tag = new int[initialSize];
            this.roaming = new int[initialSize];
            this.rxBytes = new long[initialSize];
            this.rxPackets = new long[initialSize];
            this.txBytes = new long[initialSize];
            this.txPackets = new long[initialSize];
            this.operations = new long[initialSize];
        } else {
            // Special case for use by NetworkStatsFactory to start out *really* empty.
            this.capacity = 0;
            this.iface = libcore.util.EmptyArray.STRING;
            this.uid = libcore.util.EmptyArray.INT;
            this.set = libcore.util.EmptyArray.INT;
            this.tag = libcore.util.EmptyArray.INT;
            this.roaming = libcore.util.EmptyArray.INT;
            this.rxBytes = libcore.util.EmptyArray.LONG;
            this.rxPackets = libcore.util.EmptyArray.LONG;
            this.txBytes = libcore.util.EmptyArray.LONG;
            this.txPackets = libcore.util.EmptyArray.LONG;
            this.operations = libcore.util.EmptyArray.LONG;
        }
    }

    public NetworkStats(android.os.Parcel parcel) {
        elapsedRealtime = parcel.readLong();
        size = parcel.readInt();
        capacity = parcel.readInt();
        iface = parcel.createStringArray();
        uid = parcel.createIntArray();
        set = parcel.createIntArray();
        tag = parcel.createIntArray();
        roaming = parcel.createIntArray();
        rxBytes = parcel.createLongArray();
        rxPackets = parcel.createLongArray();
        txBytes = parcel.createLongArray();
        txPackets = parcel.createLongArray();
        operations = parcel.createLongArray();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(elapsedRealtime);
        dest.writeInt(size);
        dest.writeInt(capacity);
        dest.writeStringArray(iface);
        dest.writeIntArray(uid);
        dest.writeIntArray(set);
        dest.writeIntArray(tag);
        dest.writeIntArray(roaming);
        dest.writeLongArray(rxBytes);
        dest.writeLongArray(rxPackets);
        dest.writeLongArray(txBytes);
        dest.writeLongArray(txPackets);
        dest.writeLongArray(operations);
    }

    @java.lang.Override
    public android.net.NetworkStats clone() {
        final android.net.NetworkStats clone = new android.net.NetworkStats(elapsedRealtime, size);
        android.net.NetworkStats.Entry entry = null;
        for (int i = 0; i < size; i++) {
            entry = getValues(i, entry);
            clone.addValues(entry);
        }
        return clone;
    }

    @com.android.internal.annotations.VisibleForTesting
    public android.net.NetworkStats addIfaceValues(java.lang.String iface, long rxBytes, long rxPackets, long txBytes, long txPackets) {
        return addValues(iface, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, rxBytes, rxPackets, txBytes, txPackets, 0L);
    }

    @com.android.internal.annotations.VisibleForTesting
    public android.net.NetworkStats addValues(java.lang.String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return addValues(new android.net.NetworkStats.Entry(iface, uid, set, tag, rxBytes, rxPackets, txBytes, txPackets, operations));
    }

    @com.android.internal.annotations.VisibleForTesting
    public android.net.NetworkStats addValues(java.lang.String iface, int uid, int set, int tag, int roaming, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return addValues(new android.net.NetworkStats.Entry(iface, uid, set, tag, roaming, rxBytes, rxPackets, txBytes, txPackets, operations));
    }

    /**
     * Add new stats entry, copying from given {@link Entry}. The {@link Entry}
     * object can be recycled across multiple calls.
     */
    public android.net.NetworkStats addValues(android.net.NetworkStats.Entry entry) {
        if (size >= capacity) {
            final int newLength = (java.lang.Math.max(size, 10) * 3) / 2;
            iface = java.util.Arrays.copyOf(iface, newLength);
            uid = java.util.Arrays.copyOf(uid, newLength);
            set = java.util.Arrays.copyOf(set, newLength);
            tag = java.util.Arrays.copyOf(tag, newLength);
            roaming = java.util.Arrays.copyOf(roaming, newLength);
            rxBytes = java.util.Arrays.copyOf(rxBytes, newLength);
            rxPackets = java.util.Arrays.copyOf(rxPackets, newLength);
            txBytes = java.util.Arrays.copyOf(txBytes, newLength);
            txPackets = java.util.Arrays.copyOf(txPackets, newLength);
            operations = java.util.Arrays.copyOf(operations, newLength);
            capacity = newLength;
        }
        iface[size] = entry.iface;
        uid[size] = entry.uid;
        set[size] = entry.set;
        tag[size] = entry.tag;
        roaming[size] = entry.roaming;
        rxBytes[size] = entry.rxBytes;
        rxPackets[size] = entry.rxPackets;
        txBytes[size] = entry.txBytes;
        txPackets[size] = entry.txPackets;
        operations[size] = entry.operations;
        size++;
        return this;
    }

    /**
     * Return specific stats entry.
     */
    public android.net.NetworkStats.Entry getValues(int i, android.net.NetworkStats.Entry recycle) {
        final android.net.NetworkStats.Entry entry = (recycle != null) ? recycle : new android.net.NetworkStats.Entry();
        entry.iface = iface[i];
        entry.uid = uid[i];
        entry.set = set[i];
        entry.tag = tag[i];
        entry.roaming = roaming[i];
        entry.rxBytes = rxBytes[i];
        entry.rxPackets = rxPackets[i];
        entry.txBytes = txBytes[i];
        entry.txPackets = txPackets[i];
        entry.operations = operations[i];
        return entry;
    }

    public long getElapsedRealtime() {
        return elapsedRealtime;
    }

    public void setElapsedRealtime(long time) {
        elapsedRealtime = time;
    }

    /**
     * Return age of this {@link NetworkStats} object with respect to
     * {@link SystemClock#elapsedRealtime()}.
     */
    public long getElapsedRealtimeAge() {
        return android.os.SystemClock.elapsedRealtime() - elapsedRealtime;
    }

    public int size() {
        return size;
    }

    @com.android.internal.annotations.VisibleForTesting
    public int internalSize() {
        return capacity;
    }

    @java.lang.Deprecated
    public android.net.NetworkStats combineValues(java.lang.String iface, int uid, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return combineValues(iface, uid, android.net.NetworkStats.SET_DEFAULT, tag, rxBytes, rxPackets, txBytes, txPackets, operations);
    }

    public android.net.NetworkStats combineValues(java.lang.String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return combineValues(new android.net.NetworkStats.Entry(iface, uid, set, tag, rxBytes, rxPackets, txBytes, txPackets, operations));
    }

    /**
     * Combine given values with an existing row, or create a new row if
     * {@link #findIndex(String, int, int, int, int)} is unable to find match. Can
     * also be used to subtract values from existing rows.
     */
    public android.net.NetworkStats combineValues(android.net.NetworkStats.Entry entry) {
        final int i = findIndex(entry.iface, entry.uid, entry.set, entry.tag, entry.roaming);
        if (i == (-1)) {
            // only create new entry when positive contribution
            addValues(entry);
        } else {
            rxBytes[i] += entry.rxBytes;
            rxPackets[i] += entry.rxPackets;
            txBytes[i] += entry.txBytes;
            txPackets[i] += entry.txPackets;
            operations[i] += entry.operations;
        }
        return this;
    }

    /**
     * Combine all values from another {@link NetworkStats} into this object.
     */
    public void combineAllValues(android.net.NetworkStats another) {
        android.net.NetworkStats.Entry entry = null;
        for (int i = 0; i < another.size; i++) {
            entry = another.getValues(i, entry);
            combineValues(entry);
        }
    }

    /**
     * Find first stats index that matches the requested parameters.
     */
    public int findIndex(java.lang.String iface, int uid, int set, int tag, int roaming) {
        for (int i = 0; i < size; i++) {
            if (((((uid == this.uid[i]) && (set == this.set[i])) && (tag == this.tag[i])) && (roaming == this.roaming[i])) && java.util.Objects.equals(iface, this.iface[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find first stats index that matches the requested parameters, starting
     * search around the hinted index as an optimization.
     */
    @com.android.internal.annotations.VisibleForTesting
    public int findIndexHinted(java.lang.String iface, int uid, int set, int tag, int roaming, int hintIndex) {
        for (int offset = 0; offset < size; offset++) {
            final int halfOffset = offset / 2;
            // search outwards from hint index, alternating forward and backward
            final int i;
            if ((offset % 2) == 0) {
                i = (hintIndex + halfOffset) % size;
            } else {
                i = (((size + hintIndex) - halfOffset) - 1) % size;
            }
            if (((((uid == this.uid[i]) && (set == this.set[i])) && (tag == this.tag[i])) && (roaming == this.roaming[i])) && java.util.Objects.equals(iface, this.iface[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Splice in {@link #operations} from the given {@link NetworkStats} based
     * on matching {@link #uid} and {@link #tag} rows. Ignores {@link #iface},
     * since operation counts are at data layer.
     */
    public void spliceOperationsFrom(android.net.NetworkStats stats) {
        for (int i = 0; i < size; i++) {
            final int j = stats.findIndex(iface[i], uid[i], set[i], tag[i], roaming[i]);
            if (j == (-1)) {
                operations[i] = 0;
            } else {
                operations[i] = stats.operations[j];
            }
        }
    }

    /**
     * Return list of unique interfaces known by this data structure.
     */
    public java.lang.String[] getUniqueIfaces() {
        final java.util.HashSet<java.lang.String> ifaces = new java.util.HashSet<java.lang.String>();
        for (java.lang.String iface : this.iface) {
            if (iface != android.net.NetworkStats.IFACE_ALL) {
                ifaces.add(iface);
            }
        }
        return ifaces.toArray(new java.lang.String[ifaces.size()]);
    }

    /**
     * Return list of unique UIDs known by this data structure.
     */
    public int[] getUniqueUids() {
        final android.util.SparseBooleanArray uids = new android.util.SparseBooleanArray();
        for (int uid : this.uid) {
            uids.put(uid, true);
        }
        final int size = uids.size();
        final int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = uids.keyAt(i);
        }
        return result;
    }

    /**
     * Return total bytes represented by this snapshot object, usually used when
     * checking if a {@link #subtract(NetworkStats)} delta passes a threshold.
     */
    public long getTotalBytes() {
        final android.net.NetworkStats.Entry entry = getTotal(null);
        return entry.rxBytes + entry.txBytes;
    }

    /**
     * Return total of all fields represented by this snapshot object.
     */
    public android.net.NetworkStats.Entry getTotal(android.net.NetworkStats.Entry recycle) {
        return getTotal(recycle, null, android.net.NetworkStats.UID_ALL, false);
    }

    /**
     * Return total of all fields represented by this snapshot object matching
     * the requested {@link #uid}.
     */
    public android.net.NetworkStats.Entry getTotal(android.net.NetworkStats.Entry recycle, int limitUid) {
        return getTotal(recycle, null, limitUid, false);
    }

    /**
     * Return total of all fields represented by this snapshot object matching
     * the requested {@link #iface}.
     */
    public android.net.NetworkStats.Entry getTotal(android.net.NetworkStats.Entry recycle, java.util.HashSet<java.lang.String> limitIface) {
        return getTotal(recycle, limitIface, android.net.NetworkStats.UID_ALL, false);
    }

    public android.net.NetworkStats.Entry getTotalIncludingTags(android.net.NetworkStats.Entry recycle) {
        return getTotal(recycle, null, android.net.NetworkStats.UID_ALL, true);
    }

    /**
     * Return total of all fields represented by this snapshot object matching
     * the requested {@link #iface} and {@link #uid}.
     *
     * @param limitIface
     * 		Set of {@link #iface} to include in total; or {@code null} to include all ifaces.
     */
    private android.net.NetworkStats.Entry getTotal(android.net.NetworkStats.Entry recycle, java.util.HashSet<java.lang.String> limitIface, int limitUid, boolean includeTags) {
        final android.net.NetworkStats.Entry entry = (recycle != null) ? recycle : new android.net.NetworkStats.Entry();
        entry.iface = android.net.NetworkStats.IFACE_ALL;
        entry.uid = limitUid;
        entry.set = android.net.NetworkStats.SET_ALL;
        entry.tag = android.net.NetworkStats.TAG_NONE;
        entry.roaming = android.net.NetworkStats.ROAMING_ALL;
        entry.rxBytes = 0;
        entry.rxPackets = 0;
        entry.txBytes = 0;
        entry.txPackets = 0;
        entry.operations = 0;
        for (int i = 0; i < size; i++) {
            final boolean matchesUid = (limitUid == android.net.NetworkStats.UID_ALL) || (limitUid == uid[i]);
            final boolean matchesIface = (limitIface == null) || limitIface.contains(iface[i]);
            if (matchesUid && matchesIface) {
                // skip specific tags, since already counted in TAG_NONE
                if ((tag[i] != android.net.NetworkStats.TAG_NONE) && (!includeTags))
                    continue;

                entry.rxBytes += rxBytes[i];
                entry.rxPackets += rxPackets[i];
                entry.txBytes += txBytes[i];
                entry.txPackets += txPackets[i];
                entry.operations += operations[i];
            }
        }
        return entry;
    }

    /**
     * Fast path for battery stats.
     */
    public long getTotalPackets() {
        long total = 0;
        for (int i = size - 1; i >= 0; i--) {
            total += rxPackets[i] + txPackets[i];
        }
        return total;
    }

    /**
     * Subtract the given {@link NetworkStats}, effectively leaving the delta
     * between two snapshots in time. Assumes that statistics rows collect over
     * time, and that none of them have disappeared.
     */
    public android.net.NetworkStats subtract(android.net.NetworkStats right) {
        return android.net.NetworkStats.subtract(this, right, null, null);
    }

    /**
     * Subtract the two given {@link NetworkStats} objects, returning the delta
     * between two snapshots in time. Assumes that statistics rows collect over
     * time, and that none of them have disappeared.
     * <p>
     * If counters have rolled backwards, they are clamped to {@code 0} and
     * reported to the given {@link NonMonotonicObserver}.
     */
    public static <C> android.net.NetworkStats subtract(android.net.NetworkStats left, android.net.NetworkStats right, android.net.NetworkStats.NonMonotonicObserver<C> observer, C cookie) {
        return android.net.NetworkStats.subtract(left, right, observer, cookie, null);
    }

    /**
     * Subtract the two given {@link NetworkStats} objects, returning the delta
     * between two snapshots in time. Assumes that statistics rows collect over
     * time, and that none of them have disappeared.
     * <p>
     * If counters have rolled backwards, they are clamped to {@code 0} and
     * reported to the given {@link NonMonotonicObserver}.
     * <p>
     * If <var>recycle</var> is supplied, this NetworkStats object will be
     * reused (and returned) as the result if it is large enough to contain
     * the data.
     */
    public static <C> android.net.NetworkStats subtract(android.net.NetworkStats left, android.net.NetworkStats right, android.net.NetworkStats.NonMonotonicObserver<C> observer, C cookie, android.net.NetworkStats recycle) {
        long deltaRealtime = left.elapsedRealtime - right.elapsedRealtime;
        if (deltaRealtime < 0) {
            if (observer != null) {
                observer.foundNonMonotonic(left, -1, right, -1, cookie);
            }
            deltaRealtime = 0;
        }
        // result will have our rows, and elapsed time between snapshots
        final android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry();
        final android.net.NetworkStats result;
        if ((recycle != null) && (recycle.capacity >= left.size)) {
            result = recycle;
            result.size = 0;
            result.elapsedRealtime = deltaRealtime;
        } else {
            result = new android.net.NetworkStats(deltaRealtime, left.size);
        }
        for (int i = 0; i < left.size; i++) {
            entry.iface = left.iface[i];
            entry.uid = left.uid[i];
            entry.set = left.set[i];
            entry.tag = left.tag[i];
            entry.roaming = left.roaming[i];
            // find remote row that matches, and subtract
            final int j = right.findIndexHinted(entry.iface, entry.uid, entry.set, entry.tag, entry.roaming, i);
            if (j == (-1)) {
                // newly appearing row, return entire value
                entry.rxBytes = left.rxBytes[i];
                entry.rxPackets = left.rxPackets[i];
                entry.txBytes = left.txBytes[i];
                entry.txPackets = left.txPackets[i];
                entry.operations = left.operations[i];
            } else {
                // existing row, subtract remote value
                entry.rxBytes = left.rxBytes[i] - right.rxBytes[j];
                entry.rxPackets = left.rxPackets[i] - right.rxPackets[j];
                entry.txBytes = left.txBytes[i] - right.txBytes[j];
                entry.txPackets = left.txPackets[i] - right.txPackets[j];
                entry.operations = left.operations[i] - right.operations[j];
                if (((((entry.rxBytes < 0) || (entry.rxPackets < 0)) || (entry.txBytes < 0)) || (entry.txPackets < 0)) || (entry.operations < 0)) {
                    if (observer != null) {
                        observer.foundNonMonotonic(left, i, right, j, cookie);
                    }
                    entry.rxBytes = java.lang.Math.max(entry.rxBytes, 0);
                    entry.rxPackets = java.lang.Math.max(entry.rxPackets, 0);
                    entry.txBytes = java.lang.Math.max(entry.txBytes, 0);
                    entry.txPackets = java.lang.Math.max(entry.txPackets, 0);
                    entry.operations = java.lang.Math.max(entry.operations, 0);
                }
            }
            result.addValues(entry);
        }
        return result;
    }

    /**
     * Return total statistics grouped by {@link #iface}; doesn't mutate the
     * original structure.
     */
    public android.net.NetworkStats groupedByIface() {
        final android.net.NetworkStats stats = new android.net.NetworkStats(elapsedRealtime, 10);
        final android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry();
        entry.uid = android.net.NetworkStats.UID_ALL;
        entry.set = android.net.NetworkStats.SET_ALL;
        entry.tag = android.net.NetworkStats.TAG_NONE;
        entry.roaming = android.net.NetworkStats.ROAMING_ALL;
        entry.operations = 0L;
        for (int i = 0; i < size; i++) {
            // skip specific tags, since already counted in TAG_NONE
            if (tag[i] != android.net.NetworkStats.TAG_NONE)
                continue;

            entry.iface = iface[i];
            entry.rxBytes = rxBytes[i];
            entry.rxPackets = rxPackets[i];
            entry.txBytes = txBytes[i];
            entry.txPackets = txPackets[i];
            stats.combineValues(entry);
        }
        return stats;
    }

    /**
     * Return total statistics grouped by {@link #uid}; doesn't mutate the
     * original structure.
     */
    public android.net.NetworkStats groupedByUid() {
        final android.net.NetworkStats stats = new android.net.NetworkStats(elapsedRealtime, 10);
        final android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry();
        entry.iface = android.net.NetworkStats.IFACE_ALL;
        entry.set = android.net.NetworkStats.SET_ALL;
        entry.tag = android.net.NetworkStats.TAG_NONE;
        entry.roaming = android.net.NetworkStats.ROAMING_ALL;
        for (int i = 0; i < size; i++) {
            // skip specific tags, since already counted in TAG_NONE
            if (tag[i] != android.net.NetworkStats.TAG_NONE)
                continue;

            entry.uid = uid[i];
            entry.rxBytes = rxBytes[i];
            entry.rxPackets = rxPackets[i];
            entry.txBytes = txBytes[i];
            entry.txPackets = txPackets[i];
            entry.operations = operations[i];
            stats.combineValues(entry);
        }
        return stats;
    }

    /**
     * Return all rows except those attributed to the requested UID; doesn't
     * mutate the original structure.
     */
    public android.net.NetworkStats withoutUids(int[] uids) {
        final android.net.NetworkStats stats = new android.net.NetworkStats(elapsedRealtime, 10);
        android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry();
        for (int i = 0; i < size; i++) {
            entry = getValues(i, entry);
            if (!com.android.internal.util.ArrayUtils.contains(uids, entry.uid)) {
                stats.addValues(entry);
            }
        }
        return stats;
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("NetworkStats: elapsedRealtime=");
        pw.println(elapsedRealtime);
        for (int i = 0; i < size; i++) {
            pw.print(prefix);
            pw.print("  [");
            pw.print(i);
            pw.print("]");
            pw.print(" iface=");
            pw.print(iface[i]);
            pw.print(" uid=");
            pw.print(uid[i]);
            pw.print(" set=");
            pw.print(android.net.NetworkStats.setToString(set[i]));
            pw.print(" tag=");
            pw.print(android.net.NetworkStats.tagToString(tag[i]));
            pw.print(" roaming=");
            pw.print(android.net.NetworkStats.roamingToString(roaming[i]));
            pw.print(" rxBytes=");
            pw.print(rxBytes[i]);
            pw.print(" rxPackets=");
            pw.print(rxPackets[i]);
            pw.print(" txBytes=");
            pw.print(txBytes[i]);
            pw.print(" txPackets=");
            pw.print(txPackets[i]);
            pw.print(" operations=");
            pw.println(operations[i]);
        }
    }

    /**
     * Return text description of {@link #set} value.
     */
    public static java.lang.String setToString(int set) {
        switch (set) {
            case android.net.NetworkStats.SET_ALL :
                return "ALL";
            case android.net.NetworkStats.SET_DEFAULT :
                return "DEFAULT";
            case android.net.NetworkStats.SET_FOREGROUND :
                return "FOREGROUND";
            case android.net.NetworkStats.SET_DBG_VPN_IN :
                return "DBG_VPN_IN";
            case android.net.NetworkStats.SET_DBG_VPN_OUT :
                return "DBG_VPN_OUT";
            default :
                return "UNKNOWN";
        }
    }

    /**
     * Return text description of {@link #set} value.
     */
    public static java.lang.String setToCheckinString(int set) {
        switch (set) {
            case android.net.NetworkStats.SET_ALL :
                return "all";
            case android.net.NetworkStats.SET_DEFAULT :
                return "def";
            case android.net.NetworkStats.SET_FOREGROUND :
                return "fg";
            case android.net.NetworkStats.SET_DBG_VPN_IN :
                return "vpnin";
            case android.net.NetworkStats.SET_DBG_VPN_OUT :
                return "vpnout";
            default :
                return "unk";
        }
    }

    /**
     *
     *
     * @return true if the querySet matches the dataSet.
     */
    public static boolean setMatches(int querySet, int dataSet) {
        if (querySet == dataSet) {
            return true;
        }
        // SET_ALL matches all non-debugging sets.
        return (querySet == android.net.NetworkStats.SET_ALL) && (dataSet < android.net.NetworkStats.SET_DEBUG_START);
    }

    /**
     * Return text description of {@link #tag} value.
     */
    public static java.lang.String tagToString(int tag) {
        return "0x" + java.lang.Integer.toHexString(tag);
    }

    /**
     * Return text description of {@link #roaming} value.
     */
    public static java.lang.String roamingToString(int roaming) {
        switch (roaming) {
            case android.net.NetworkStats.ROAMING_ALL :
                return "ALL";
            case android.net.NetworkStats.ROAMING_NO :
                return "NO";
            case android.net.NetworkStats.ROAMING_YES :
                return "YES";
            default :
                return "UNKNOWN";
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
        dump("", new java.io.PrintWriter(writer));
        return writer.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkStats> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkStats>() {
        @java.lang.Override
        public android.net.NetworkStats createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkStats(in);
        }

        @java.lang.Override
        public android.net.NetworkStats[] newArray(int size) {
            return new android.net.NetworkStats[size];
        }
    };

    public interface NonMonotonicObserver<C> {
        public void foundNonMonotonic(android.net.NetworkStats left, int leftIndex, android.net.NetworkStats right, int rightIndex, C cookie);
    }

    /**
     * VPN accounting. Move some VPN's underlying traffic to other UIDs that use tun0 iface.
     *
     * This method should only be called on delta NetworkStats. Do not call this method on a
     * snapshot {@link NetworkStats} object because the tunUid and/or the underlyingIface may
     * change over time.
     *
     * This method performs adjustments for one active VPN package and one VPN iface at a time.
     *
     * It is possible for the VPN software to use multiple underlying networks. This method
     * only migrates traffic for the primary underlying network.
     *
     * @param tunUid
     * 		uid of the VPN application
     * @param tunIface
     * 		iface of the vpn tunnel
     * @param underlyingIface
     * 		the primary underlying network iface used by the VPN application
     * @return true if it successfully adjusts the accounting for VPN, false otherwise
     */
    public boolean migrateTun(int tunUid, java.lang.String tunIface, java.lang.String underlyingIface) {
        android.net.NetworkStats.Entry tunIfaceTotal = new android.net.NetworkStats.Entry();
        android.net.NetworkStats.Entry underlyingIfaceTotal = new android.net.NetworkStats.Entry();
        tunAdjustmentInit(tunUid, tunIface, underlyingIface, tunIfaceTotal, underlyingIfaceTotal);
        // If tunIface < underlyingIface, it leaves the overhead traffic in the VPN app.
        // If tunIface > underlyingIface, the VPN app doesn't get credit for data compression.
        // Negative stats should be avoided.
        android.net.NetworkStats.Entry pool = android.net.NetworkStats.tunGetPool(tunIfaceTotal, underlyingIfaceTotal);
        if (pool.isEmpty()) {
            return true;
        }
        android.net.NetworkStats.Entry moved = addTrafficToApplications(tunUid, tunIface, underlyingIface, tunIfaceTotal, pool);
        deductTrafficFromVpnApp(tunUid, underlyingIface, moved);
        if (!moved.isEmpty()) {
            android.util.Slog.wtf(android.net.NetworkStats.TAG, "Failed to deduct underlying network traffic from VPN package. Moved=" + moved);
            return false;
        }
        return true;
    }

    /**
     * Initializes the data used by the migrateTun() method.
     *
     * This is the first pass iteration which does the following work:
     * (1) Adds up all the traffic through the tunUid's underlyingIface
     *     (both foreground and background).
     * (2) Adds up all the traffic through tun0 excluding traffic from the vpn app itself.
     */
    private void tunAdjustmentInit(int tunUid, java.lang.String tunIface, java.lang.String underlyingIface, android.net.NetworkStats.Entry tunIfaceTotal, android.net.NetworkStats.Entry underlyingIfaceTotal) {
        android.net.NetworkStats.Entry recycle = new android.net.NetworkStats.Entry();
        for (int i = 0; i < size; i++) {
            getValues(i, recycle);
            if (recycle.uid == android.net.NetworkStats.UID_ALL) {
                throw new java.lang.IllegalStateException("Cannot adjust VPN accounting on an iface aggregated NetworkStats.");
            }
            if ((recycle.set == android.net.NetworkStats.SET_DBG_VPN_IN) || (recycle.set == android.net.NetworkStats.SET_DBG_VPN_OUT)) {
                throw new java.lang.IllegalStateException("Cannot adjust VPN accounting on a NetworkStats containing SET_DBG_VPN_*");
            }
            if (((recycle.uid == tunUid) && (recycle.tag == android.net.NetworkStats.TAG_NONE)) && java.util.Objects.equals(underlyingIface, recycle.iface)) {
                underlyingIfaceTotal.add(recycle);
            }
            if (((recycle.uid != tunUid) && (recycle.tag == android.net.NetworkStats.TAG_NONE)) && java.util.Objects.equals(tunIface, recycle.iface)) {
                // Add up all tunIface traffic excluding traffic from the vpn app itself.
                tunIfaceTotal.add(recycle);
            }
        }
    }

    private static android.net.NetworkStats.Entry tunGetPool(android.net.NetworkStats.Entry tunIfaceTotal, android.net.NetworkStats.Entry underlyingIfaceTotal) {
        android.net.NetworkStats.Entry pool = new android.net.NetworkStats.Entry();
        pool.rxBytes = java.lang.Math.min(tunIfaceTotal.rxBytes, underlyingIfaceTotal.rxBytes);
        pool.rxPackets = java.lang.Math.min(tunIfaceTotal.rxPackets, underlyingIfaceTotal.rxPackets);
        pool.txBytes = java.lang.Math.min(tunIfaceTotal.txBytes, underlyingIfaceTotal.txBytes);
        pool.txPackets = java.lang.Math.min(tunIfaceTotal.txPackets, underlyingIfaceTotal.txPackets);
        pool.operations = java.lang.Math.min(tunIfaceTotal.operations, underlyingIfaceTotal.operations);
        return pool;
    }

    private android.net.NetworkStats.Entry addTrafficToApplications(int tunUid, java.lang.String tunIface, java.lang.String underlyingIface, android.net.NetworkStats.Entry tunIfaceTotal, android.net.NetworkStats.Entry pool) {
        android.net.NetworkStats.Entry moved = new android.net.NetworkStats.Entry();
        android.net.NetworkStats.Entry tmpEntry = new android.net.NetworkStats.Entry();
        tmpEntry.iface = underlyingIface;
        for (int i = 0; i < size; i++) {
            // the vpn app is excluded from the redistribution but all moved traffic will be
            // deducted from the vpn app (see deductTrafficFromVpnApp below).
            if (java.util.Objects.equals(iface[i], tunIface) && (uid[i] != tunUid)) {
                if (tunIfaceTotal.rxBytes > 0) {
                    tmpEntry.rxBytes = (pool.rxBytes * rxBytes[i]) / tunIfaceTotal.rxBytes;
                } else {
                    tmpEntry.rxBytes = 0;
                }
                if (tunIfaceTotal.rxPackets > 0) {
                    tmpEntry.rxPackets = (pool.rxPackets * rxPackets[i]) / tunIfaceTotal.rxPackets;
                } else {
                    tmpEntry.rxPackets = 0;
                }
                if (tunIfaceTotal.txBytes > 0) {
                    tmpEntry.txBytes = (pool.txBytes * txBytes[i]) / tunIfaceTotal.txBytes;
                } else {
                    tmpEntry.txBytes = 0;
                }
                if (tunIfaceTotal.txPackets > 0) {
                    tmpEntry.txPackets = (pool.txPackets * txPackets[i]) / tunIfaceTotal.txPackets;
                } else {
                    tmpEntry.txPackets = 0;
                }
                if (tunIfaceTotal.operations > 0) {
                    tmpEntry.operations = (pool.operations * operations[i]) / tunIfaceTotal.operations;
                } else {
                    tmpEntry.operations = 0;
                }
                tmpEntry.uid = uid[i];
                tmpEntry.tag = tag[i];
                tmpEntry.set = set[i];
                tmpEntry.roaming = roaming[i];
                combineValues(tmpEntry);
                if (tag[i] == android.net.NetworkStats.TAG_NONE) {
                    moved.add(tmpEntry);
                    // Add debug info
                    tmpEntry.set = android.net.NetworkStats.SET_DBG_VPN_IN;
                    combineValues(tmpEntry);
                }
            }
        }
        return moved;
    }

    private void deductTrafficFromVpnApp(int tunUid, java.lang.String underlyingIface, android.net.NetworkStats.Entry moved) {
        // Add debug info
        moved.uid = tunUid;
        moved.set = android.net.NetworkStats.SET_DBG_VPN_OUT;
        moved.tag = android.net.NetworkStats.TAG_NONE;
        moved.iface = underlyingIface;
        moved.roaming = android.net.NetworkStats.ROAMING_ALL;
        combineValues(moved);
        // Caveat: if the vpn software uses tag, the total tagged traffic may be greater than
        // the TAG_NONE traffic.
        // 
        // Relies on the fact that the underlying traffic only has state ROAMING_NO, which
        // should be the case as it comes directly from the /proc file. We only blend in the
        // roaming data after applying these adjustments, by checking the NetworkIdentity of the
        // underlying iface.
        int idxVpnBackground = findIndex(underlyingIface, tunUid, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, android.net.NetworkStats.ROAMING_NO);
        if (idxVpnBackground != (-1)) {
            android.net.NetworkStats.tunSubtract(idxVpnBackground, this, moved);
        }
        int idxVpnForeground = findIndex(underlyingIface, tunUid, android.net.NetworkStats.SET_FOREGROUND, android.net.NetworkStats.TAG_NONE, android.net.NetworkStats.ROAMING_NO);
        if (idxVpnForeground != (-1)) {
            android.net.NetworkStats.tunSubtract(idxVpnForeground, this, moved);
        }
    }

    private static void tunSubtract(int i, android.net.NetworkStats left, android.net.NetworkStats.Entry right) {
        long rxBytes = java.lang.Math.min(left.rxBytes[i], right.rxBytes);
        left.rxBytes[i] -= rxBytes;
        right.rxBytes -= rxBytes;
        long rxPackets = java.lang.Math.min(left.rxPackets[i], right.rxPackets);
        left.rxPackets[i] -= rxPackets;
        right.rxPackets -= rxPackets;
        long txBytes = java.lang.Math.min(left.txBytes[i], right.txBytes);
        left.txBytes[i] -= txBytes;
        right.txBytes -= txBytes;
        long txPackets = java.lang.Math.min(left.txPackets[i], right.txPackets);
        left.txPackets[i] -= txPackets;
        right.txPackets -= txPackets;
    }
}

