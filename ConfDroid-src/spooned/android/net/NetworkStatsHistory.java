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
 * Collection of historical network statistics, recorded into equally-sized
 * "buckets" in time. Internally it stores data in {@code long} series for more
 * efficient persistence.
 * <p>
 * Each bucket is defined by a {@link #bucketStart} timestamp, and lasts for
 * {@link #bucketDuration}. Internally assumes that {@link #bucketStart} is
 * sorted at all times.
 *
 * @unknown 
 */
public class NetworkStatsHistory implements android.os.Parcelable {
    private static final int VERSION_INIT = 1;

    private static final int VERSION_ADD_PACKETS = 2;

    private static final int VERSION_ADD_ACTIVE = 3;

    public static final int FIELD_ACTIVE_TIME = 0x1;

    public static final int FIELD_RX_BYTES = 0x2;

    public static final int FIELD_RX_PACKETS = 0x4;

    public static final int FIELD_TX_BYTES = 0x8;

    public static final int FIELD_TX_PACKETS = 0x10;

    public static final int FIELD_OPERATIONS = 0x20;

    public static final int FIELD_ALL = 0xffffffff;

    private long bucketDuration;

    private int bucketCount;

    private long[] bucketStart;

    private long[] activeTime;

    private long[] rxBytes;

    private long[] rxPackets;

    private long[] txBytes;

    private long[] txPackets;

    private long[] operations;

    private long totalBytes;

    public static class Entry {
        public static final long UNKNOWN = -1;

        public long bucketDuration;

        public long bucketStart;

        public long activeTime;

        public long rxBytes;

        public long rxPackets;

        public long txBytes;

        public long txPackets;

        public long operations;
    }

    public NetworkStatsHistory(long bucketDuration) {
        this(bucketDuration, 10, android.net.NetworkStatsHistory.FIELD_ALL);
    }

    public NetworkStatsHistory(long bucketDuration, int initialSize) {
        this(bucketDuration, initialSize, android.net.NetworkStatsHistory.FIELD_ALL);
    }

    public NetworkStatsHistory(long bucketDuration, int initialSize, int fields) {
        this.bucketDuration = bucketDuration;
        bucketStart = new long[initialSize];
        if ((fields & android.net.NetworkStatsHistory.FIELD_ACTIVE_TIME) != 0)
            activeTime = new long[initialSize];

        if ((fields & android.net.NetworkStatsHistory.FIELD_RX_BYTES) != 0)
            rxBytes = new long[initialSize];

        if ((fields & android.net.NetworkStatsHistory.FIELD_RX_PACKETS) != 0)
            rxPackets = new long[initialSize];

        if ((fields & android.net.NetworkStatsHistory.FIELD_TX_BYTES) != 0)
            txBytes = new long[initialSize];

        if ((fields & android.net.NetworkStatsHistory.FIELD_TX_PACKETS) != 0)
            txPackets = new long[initialSize];

        if ((fields & android.net.NetworkStatsHistory.FIELD_OPERATIONS) != 0)
            operations = new long[initialSize];

        bucketCount = 0;
        totalBytes = 0;
    }

    public NetworkStatsHistory(android.net.NetworkStatsHistory existing, long bucketDuration) {
        this(bucketDuration, existing.estimateResizeBuckets(bucketDuration));
        recordEntireHistory(existing);
    }

    public NetworkStatsHistory(android.os.Parcel in) {
        bucketDuration = in.readLong();
        bucketStart = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        activeTime = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        rxBytes = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        rxPackets = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        txBytes = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        txPackets = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        operations = android.net.NetworkStatsHistory.ParcelUtils.readLongArray(in);
        bucketCount = bucketStart.length;
        totalBytes = in.readLong();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeLong(bucketDuration);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, bucketStart, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, activeTime, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, rxBytes, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, rxPackets, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, txBytes, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, txPackets, bucketCount);
        android.net.NetworkStatsHistory.ParcelUtils.writeLongArray(out, operations, bucketCount);
        out.writeLong(totalBytes);
    }

    public NetworkStatsHistory(java.io.DataInputStream in) throws java.io.IOException {
        final int version = in.readInt();
        switch (version) {
            case android.net.NetworkStatsHistory.VERSION_INIT :
                {
                    bucketDuration = in.readLong();
                    bucketStart = android.net.NetworkStatsHistory.DataStreamUtils.readFullLongArray(in);
                    rxBytes = android.net.NetworkStatsHistory.DataStreamUtils.readFullLongArray(in);
                    rxPackets = new long[bucketStart.length];
                    txBytes = android.net.NetworkStatsHistory.DataStreamUtils.readFullLongArray(in);
                    txPackets = new long[bucketStart.length];
                    operations = new long[bucketStart.length];
                    bucketCount = bucketStart.length;
                    totalBytes = com.android.internal.util.ArrayUtils.total(rxBytes) + com.android.internal.util.ArrayUtils.total(txBytes);
                    break;
                }
            case android.net.NetworkStatsHistory.VERSION_ADD_PACKETS :
            case android.net.NetworkStatsHistory.VERSION_ADD_ACTIVE :
                {
                    bucketDuration = in.readLong();
                    bucketStart = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    activeTime = (version >= android.net.NetworkStatsHistory.VERSION_ADD_ACTIVE) ? android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in) : new long[bucketStart.length];
                    rxBytes = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    rxPackets = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    txBytes = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    txPackets = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    operations = android.net.NetworkStatsHistory.DataStreamUtils.readVarLongArray(in);
                    bucketCount = bucketStart.length;
                    totalBytes = com.android.internal.util.ArrayUtils.total(rxBytes) + com.android.internal.util.ArrayUtils.total(txBytes);
                    break;
                }
            default :
                {
                    throw new java.net.ProtocolException("unexpected version: " + version);
                }
        }
        if ((((((bucketStart.length != bucketCount) || (rxBytes.length != bucketCount)) || (rxPackets.length != bucketCount)) || (txBytes.length != bucketCount)) || (txPackets.length != bucketCount)) || (operations.length != bucketCount)) {
            throw new java.net.ProtocolException("Mismatched history lengths");
        }
    }

    public void writeToStream(java.io.DataOutputStream out) throws java.io.IOException {
        out.writeInt(android.net.NetworkStatsHistory.VERSION_ADD_ACTIVE);
        out.writeLong(bucketDuration);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, bucketStart, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, activeTime, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, rxBytes, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, rxPackets, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, txBytes, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, txPackets, bucketCount);
        android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray(out, operations, bucketCount);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public int size() {
        return bucketCount;
    }

    public long getBucketDuration() {
        return bucketDuration;
    }

    public long getStart() {
        if (bucketCount > 0) {
            return bucketStart[0];
        } else {
            return java.lang.Long.MAX_VALUE;
        }
    }

    public long getEnd() {
        if (bucketCount > 0) {
            return bucketStart[bucketCount - 1] + bucketDuration;
        } else {
            return java.lang.Long.MIN_VALUE;
        }
    }

    /**
     * Return total bytes represented by this history.
     */
    public long getTotalBytes() {
        return totalBytes;
    }

    /**
     * Return index of bucket that contains or is immediately before the
     * requested time.
     */
    public int getIndexBefore(long time) {
        int index = java.util.Arrays.binarySearch(bucketStart, 0, bucketCount, time);
        if (index < 0) {
            index = (~index) - 1;
        } else {
            index -= 1;
        }
        return android.util.MathUtils.constrain(index, 0, bucketCount - 1);
    }

    /**
     * Return index of bucket that contains or is immediately after the
     * requested time.
     */
    public int getIndexAfter(long time) {
        int index = java.util.Arrays.binarySearch(bucketStart, 0, bucketCount, time);
        if (index < 0) {
            index = ~index;
        } else {
            index += 1;
        }
        return android.util.MathUtils.constrain(index, 0, bucketCount - 1);
    }

    /**
     * Return specific stats entry.
     */
    public android.net.NetworkStatsHistory.Entry getValues(int i, android.net.NetworkStatsHistory.Entry recycle) {
        final android.net.NetworkStatsHistory.Entry entry = (recycle != null) ? recycle : new android.net.NetworkStatsHistory.Entry();
        entry.bucketStart = bucketStart[i];
        entry.bucketDuration = bucketDuration;
        entry.activeTime = android.net.NetworkStatsHistory.getLong(activeTime, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        entry.rxBytes = android.net.NetworkStatsHistory.getLong(rxBytes, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        entry.rxPackets = android.net.NetworkStatsHistory.getLong(rxPackets, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        entry.txBytes = android.net.NetworkStatsHistory.getLong(txBytes, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        entry.txPackets = android.net.NetworkStatsHistory.getLong(txPackets, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        entry.operations = android.net.NetworkStatsHistory.getLong(operations, i, android.net.NetworkStatsHistory.Entry.UNKNOWN);
        return entry;
    }

    /**
     * Record that data traffic occurred in the given time range. Will
     * distribute across internal buckets, creating new buckets as needed.
     */
    @java.lang.Deprecated
    public void recordData(long start, long end, long rxBytes, long txBytes) {
        recordData(start, end, new android.net.NetworkStats.Entry(android.net.NetworkStats.IFACE_ALL, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, rxBytes, 0L, txBytes, 0L, 0L));
    }

    /**
     * Record that data traffic occurred in the given time range. Will
     * distribute across internal buckets, creating new buckets as needed.
     */
    public void recordData(long start, long end, android.net.NetworkStats.Entry entry) {
        long rxBytes = entry.rxBytes;
        long rxPackets = entry.rxPackets;
        long txBytes = entry.txBytes;
        long txPackets = entry.txPackets;
        long operations = entry.operations;
        if (entry.isNegative()) {
            throw new java.lang.IllegalArgumentException("tried recording negative data");
        }
        if (entry.isEmpty()) {
            return;
        }
        // create any buckets needed by this range
        ensureBuckets(start, end);
        // distribute data usage into buckets
        long duration = end - start;
        final int startIndex = getIndexAfter(end);
        for (int i = startIndex; i >= 0; i--) {
            final long curStart = bucketStart[i];
            final long curEnd = curStart + bucketDuration;
            // bucket is older than record; we're finished
            if (curEnd < start)
                break;

            // bucket is newer than record; keep looking
            if (curStart > end)
                continue;

            final long overlap = java.lang.Math.min(curEnd, end) - java.lang.Math.max(curStart, start);
            if (overlap <= 0)
                continue;

            // integer math each time is faster than floating point
            final long fracRxBytes = (rxBytes * overlap) / duration;
            final long fracRxPackets = (rxPackets * overlap) / duration;
            final long fracTxBytes = (txBytes * overlap) / duration;
            final long fracTxPackets = (txPackets * overlap) / duration;
            final long fracOperations = (operations * overlap) / duration;
            android.net.NetworkStatsHistory.addLong(activeTime, i, overlap);
            android.net.NetworkStatsHistory.addLong(this.rxBytes, i, fracRxBytes);
            rxBytes -= fracRxBytes;
            android.net.NetworkStatsHistory.addLong(this.rxPackets, i, fracRxPackets);
            rxPackets -= fracRxPackets;
            android.net.NetworkStatsHistory.addLong(this.txBytes, i, fracTxBytes);
            txBytes -= fracTxBytes;
            android.net.NetworkStatsHistory.addLong(this.txPackets, i, fracTxPackets);
            txPackets -= fracTxPackets;
            android.net.NetworkStatsHistory.addLong(this.operations, i, fracOperations);
            operations -= fracOperations;
            duration -= overlap;
        }
        totalBytes += entry.rxBytes + entry.txBytes;
    }

    /**
     * Record an entire {@link NetworkStatsHistory} into this history. Usually
     * for combining together stats for external reporting.
     */
    public void recordEntireHistory(android.net.NetworkStatsHistory input) {
        recordHistory(input, java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE);
    }

    /**
     * Record given {@link NetworkStatsHistory} into this history, copying only
     * buckets that atomically occur in the inclusive time range. Doesn't
     * interpolate across partial buckets.
     */
    public void recordHistory(android.net.NetworkStatsHistory input, long start, long end) {
        final android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry(android.net.NetworkStats.IFACE_ALL, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, 0L, 0L, 0L, 0L, 0L);
        for (int i = 0; i < input.bucketCount; i++) {
            final long bucketStart = input.bucketStart[i];
            final long bucketEnd = bucketStart + input.bucketDuration;
            // skip when bucket is outside requested range
            if ((bucketStart < start) || (bucketEnd > end))
                continue;

            entry.rxBytes = android.net.NetworkStatsHistory.getLong(input.rxBytes, i, 0L);
            entry.rxPackets = android.net.NetworkStatsHistory.getLong(input.rxPackets, i, 0L);
            entry.txBytes = android.net.NetworkStatsHistory.getLong(input.txBytes, i, 0L);
            entry.txPackets = android.net.NetworkStatsHistory.getLong(input.txPackets, i, 0L);
            entry.operations = android.net.NetworkStatsHistory.getLong(input.operations, i, 0L);
            recordData(bucketStart, bucketEnd, entry);
        }
    }

    /**
     * Ensure that buckets exist for given time range, creating as needed.
     */
    private void ensureBuckets(long start, long end) {
        // normalize incoming range to bucket boundaries
        start -= start % bucketDuration;
        end += (bucketDuration - (end % bucketDuration)) % bucketDuration;
        for (long now = start; now < end; now += bucketDuration) {
            // try finding existing bucket
            final int index = java.util.Arrays.binarySearch(bucketStart, 0, bucketCount, now);
            if (index < 0) {
                // bucket missing, create and insert
                insertBucket(~index, now);
            }
        }
    }

    /**
     * Insert new bucket at requested index and starting time.
     */
    private void insertBucket(int index, long start) {
        // create more buckets when needed
        if (bucketCount >= bucketStart.length) {
            final int newLength = (java.lang.Math.max(bucketStart.length, 10) * 3) / 2;
            bucketStart = java.util.Arrays.copyOf(bucketStart, newLength);
            if (activeTime != null)
                activeTime = java.util.Arrays.copyOf(activeTime, newLength);

            if (rxBytes != null)
                rxBytes = java.util.Arrays.copyOf(rxBytes, newLength);

            if (rxPackets != null)
                rxPackets = java.util.Arrays.copyOf(rxPackets, newLength);

            if (txBytes != null)
                txBytes = java.util.Arrays.copyOf(txBytes, newLength);

            if (txPackets != null)
                txPackets = java.util.Arrays.copyOf(txPackets, newLength);

            if (operations != null)
                operations = java.util.Arrays.copyOf(operations, newLength);

        }
        // create gap when inserting bucket in middle
        if (index < bucketCount) {
            final int dstPos = index + 1;
            final int length = bucketCount - index;
            java.lang.System.arraycopy(bucketStart, index, bucketStart, dstPos, length);
            if (activeTime != null)
                java.lang.System.arraycopy(activeTime, index, activeTime, dstPos, length);

            if (rxBytes != null)
                java.lang.System.arraycopy(rxBytes, index, rxBytes, dstPos, length);

            if (rxPackets != null)
                java.lang.System.arraycopy(rxPackets, index, rxPackets, dstPos, length);

            if (txBytes != null)
                java.lang.System.arraycopy(txBytes, index, txBytes, dstPos, length);

            if (txPackets != null)
                java.lang.System.arraycopy(txPackets, index, txPackets, dstPos, length);

            if (operations != null)
                java.lang.System.arraycopy(operations, index, operations, dstPos, length);

        }
        bucketStart[index] = start;
        android.net.NetworkStatsHistory.setLong(activeTime, index, 0L);
        android.net.NetworkStatsHistory.setLong(rxBytes, index, 0L);
        android.net.NetworkStatsHistory.setLong(rxPackets, index, 0L);
        android.net.NetworkStatsHistory.setLong(txBytes, index, 0L);
        android.net.NetworkStatsHistory.setLong(txPackets, index, 0L);
        android.net.NetworkStatsHistory.setLong(operations, index, 0L);
        bucketCount++;
    }

    /**
     * Remove buckets older than requested cutoff.
     */
    @java.lang.Deprecated
    public void removeBucketsBefore(long cutoff) {
        int i;
        for (i = 0; i < bucketCount; i++) {
            final long curStart = bucketStart[i];
            final long curEnd = curStart + bucketDuration;
            // cutoff happens before or during this bucket; everything before
            // this bucket should be removed.
            if (curEnd > cutoff)
                break;

        }
        if (i > 0) {
            final int length = bucketStart.length;
            bucketStart = java.util.Arrays.copyOfRange(bucketStart, i, length);
            if (activeTime != null)
                activeTime = java.util.Arrays.copyOfRange(activeTime, i, length);

            if (rxBytes != null)
                rxBytes = java.util.Arrays.copyOfRange(rxBytes, i, length);

            if (rxPackets != null)
                rxPackets = java.util.Arrays.copyOfRange(rxPackets, i, length);

            if (txBytes != null)
                txBytes = java.util.Arrays.copyOfRange(txBytes, i, length);

            if (txPackets != null)
                txPackets = java.util.Arrays.copyOfRange(txPackets, i, length);

            if (operations != null)
                operations = java.util.Arrays.copyOfRange(operations, i, length);

            bucketCount -= i;
            // TODO: subtract removed values from totalBytes
        }
    }

    /**
     * Return interpolated data usage across the requested range. Interpolates
     * across buckets, so values may be rounded slightly.
     */
    public android.net.NetworkStatsHistory.Entry getValues(long start, long end, android.net.NetworkStatsHistory.Entry recycle) {
        return getValues(start, end, java.lang.Long.MAX_VALUE, recycle);
    }

    /**
     * Return interpolated data usage across the requested range. Interpolates
     * across buckets, so values may be rounded slightly.
     */
    public android.net.NetworkStatsHistory.Entry getValues(long start, long end, long now, android.net.NetworkStatsHistory.Entry recycle) {
        final android.net.NetworkStatsHistory.Entry entry = (recycle != null) ? recycle : new android.net.NetworkStatsHistory.Entry();
        entry.bucketDuration = end - start;
        entry.bucketStart = start;
        entry.activeTime = (activeTime != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        entry.rxBytes = (rxBytes != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        entry.rxPackets = (rxPackets != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        entry.txBytes = (txBytes != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        entry.txPackets = (txPackets != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        entry.operations = (operations != null) ? 0 : android.net.NetworkStatsHistory.Entry.UNKNOWN;
        final int startIndex = getIndexAfter(end);
        for (int i = startIndex; i >= 0; i--) {
            final long curStart = bucketStart[i];
            final long curEnd = curStart + bucketDuration;
            // bucket is older than request; we're finished
            if (curEnd <= start)
                break;

            // bucket is newer than request; keep looking
            if (curStart >= end)
                continue;

            // include full value for active buckets, otherwise only fractional
            final boolean activeBucket = (curStart < now) && (curEnd > now);
            final long overlap;
            if (activeBucket) {
                overlap = bucketDuration;
            } else {
                final long overlapEnd = (curEnd < end) ? curEnd : end;
                final long overlapStart = (curStart > start) ? curStart : start;
                overlap = overlapEnd - overlapStart;
            }
            if (overlap <= 0)
                continue;

            // integer math each time is faster than floating point
            if (activeTime != null)
                entry.activeTime += (activeTime[i] * overlap) / bucketDuration;

            if (rxBytes != null)
                entry.rxBytes += (rxBytes[i] * overlap) / bucketDuration;

            if (rxPackets != null)
                entry.rxPackets += (rxPackets[i] * overlap) / bucketDuration;

            if (txBytes != null)
                entry.txBytes += (txBytes[i] * overlap) / bucketDuration;

            if (txPackets != null)
                entry.txPackets += (txPackets[i] * overlap) / bucketDuration;

            if (operations != null)
                entry.operations += (operations[i] * overlap) / bucketDuration;

        }
        return entry;
    }

    /**
     *
     *
     * @deprecated only for temporary testing
     */
    @java.lang.Deprecated
    public void generateRandom(long start, long end, long bytes) {
        final java.util.Random r = new java.util.Random();
        final float fractionRx = r.nextFloat();
        final long rxBytes = ((long) (bytes * fractionRx));
        final long txBytes = ((long) (bytes * (1 - fractionRx)));
        final long rxPackets = rxBytes / 1024;
        final long txPackets = txBytes / 1024;
        final long operations = rxBytes / 2048;
        generateRandom(start, end, rxBytes, rxPackets, txBytes, txPackets, operations, r);
    }

    /**
     *
     *
     * @deprecated only for temporary testing
     */
    @java.lang.Deprecated
    public void generateRandom(long start, long end, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations, java.util.Random r) {
        ensureBuckets(start, end);
        final android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry(android.net.NetworkStats.IFACE_ALL, android.net.NetworkStats.UID_ALL, android.net.NetworkStats.SET_DEFAULT, android.net.NetworkStats.TAG_NONE, 0L, 0L, 0L, 0L, 0L);
        while (((((rxBytes > 1024) || (rxPackets > 128)) || (txBytes > 1024)) || (txPackets > 128)) || (operations > 32)) {
            final long curStart = android.net.NetworkStatsHistory.randomLong(r, start, end);
            final long curEnd = curStart + android.net.NetworkStatsHistory.randomLong(r, 0, (end - curStart) / 2);
            entry.rxBytes = android.net.NetworkStatsHistory.randomLong(r, 0, rxBytes);
            entry.rxPackets = android.net.NetworkStatsHistory.randomLong(r, 0, rxPackets);
            entry.txBytes = android.net.NetworkStatsHistory.randomLong(r, 0, txBytes);
            entry.txPackets = android.net.NetworkStatsHistory.randomLong(r, 0, txPackets);
            entry.operations = android.net.NetworkStatsHistory.randomLong(r, 0, operations);
            rxBytes -= entry.rxBytes;
            rxPackets -= entry.rxPackets;
            txBytes -= entry.txBytes;
            txPackets -= entry.txPackets;
            operations -= entry.operations;
            recordData(curStart, curEnd, entry);
        } 
    }

    public static long randomLong(java.util.Random r, long start, long end) {
        return ((long) (start + (r.nextFloat() * (end - start))));
    }

    /**
     * Quickly determine if this history intersects with given window.
     */
    public boolean intersects(long start, long end) {
        final long dataStart = getStart();
        final long dataEnd = getEnd();
        if ((start >= dataStart) && (start <= dataEnd))
            return true;

        if ((end >= dataStart) && (end <= dataEnd))
            return true;

        if ((dataStart >= start) && (dataStart <= end))
            return true;

        if ((dataEnd >= start) && (dataEnd <= end))
            return true;

        return false;
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw, boolean fullHistory) {
        pw.print("NetworkStatsHistory: bucketDuration=");
        pw.println(bucketDuration / android.text.format.DateUtils.SECOND_IN_MILLIS);
        pw.increaseIndent();
        final int start = (fullHistory) ? 0 : java.lang.Math.max(0, bucketCount - 32);
        if (start > 0) {
            pw.print("(omitting ");
            pw.print(start);
            pw.println(" buckets)");
        }
        for (int i = start; i < bucketCount; i++) {
            pw.print("st=");
            pw.print(bucketStart[i] / android.text.format.DateUtils.SECOND_IN_MILLIS);
            if (rxBytes != null) {
                pw.print(" rb=");
                pw.print(rxBytes[i]);
            }
            if (rxPackets != null) {
                pw.print(" rp=");
                pw.print(rxPackets[i]);
            }
            if (txBytes != null) {
                pw.print(" tb=");
                pw.print(txBytes[i]);
            }
            if (txPackets != null) {
                pw.print(" tp=");
                pw.print(txPackets[i]);
            }
            if (operations != null) {
                pw.print(" op=");
                pw.print(operations[i]);
            }
            pw.println();
        }
        pw.decreaseIndent();
    }

    public void dumpCheckin(java.io.PrintWriter pw) {
        pw.print("d,");
        pw.print(bucketDuration / android.text.format.DateUtils.SECOND_IN_MILLIS);
        pw.println();
        for (int i = 0; i < bucketCount; i++) {
            pw.print("b,");
            pw.print(bucketStart[i] / android.text.format.DateUtils.SECOND_IN_MILLIS);
            pw.print(',');
            if (rxBytes != null) {
                pw.print(rxBytes[i]);
            } else {
                pw.print("*");
            }
            pw.print(',');
            if (rxPackets != null) {
                pw.print(rxPackets[i]);
            } else {
                pw.print("*");
            }
            pw.print(',');
            if (txBytes != null) {
                pw.print(txBytes[i]);
            } else {
                pw.print("*");
            }
            pw.print(',');
            if (txPackets != null) {
                pw.print(txPackets[i]);
            } else {
                pw.print("*");
            }
            pw.print(',');
            if (operations != null) {
                pw.print(operations[i]);
            } else {
                pw.print("*");
            }
            pw.println();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
        dump(new com.android.internal.util.IndentingPrintWriter(writer, "  "), false);
        return writer.toString();
    }

    public static final android.os.Parcelable.Creator<android.net.NetworkStatsHistory> CREATOR = new android.os.Parcelable.Creator<android.net.NetworkStatsHistory>() {
        @java.lang.Override
        public android.net.NetworkStatsHistory createFromParcel(android.os.Parcel in) {
            return new android.net.NetworkStatsHistory(in);
        }

        @java.lang.Override
        public android.net.NetworkStatsHistory[] newArray(int size) {
            return new android.net.NetworkStatsHistory[size];
        }
    };

    private static long getLong(long[] array, int i, long value) {
        return array != null ? array[i] : value;
    }

    private static void setLong(long[] array, int i, long value) {
        if (array != null)
            array[i] = value;

    }

    private static void addLong(long[] array, int i, long value) {
        if (array != null)
            array[i] += value;

    }

    public int estimateResizeBuckets(long newBucketDuration) {
        return ((int) ((size() * getBucketDuration()) / newBucketDuration));
    }

    /**
     * Utility methods for interacting with {@link DataInputStream} and
     * {@link DataOutputStream}, mostly dealing with writing partial arrays.
     */
    public static class DataStreamUtils {
        @java.lang.Deprecated
        public static long[] readFullLongArray(java.io.DataInputStream in) throws java.io.IOException {
            final int size = in.readInt();
            if (size < 0)
                throw new java.net.ProtocolException("negative array size");

            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        /**
         * Read variable-length {@link Long} using protobuf-style approach.
         */
        public static long readVarLong(java.io.DataInputStream in) throws java.io.IOException {
            int shift = 0;
            long result = 0;
            while (shift < 64) {
                byte b = in.readByte();
                result |= ((long) (b & 0x7f)) << shift;
                if ((b & 0x80) == 0)
                    return result;

                shift += 7;
            } 
            throw new java.net.ProtocolException("malformed long");
        }

        /**
         * Write variable-length {@link Long} using protobuf-style approach.
         */
        public static void writeVarLong(java.io.DataOutputStream out, long value) throws java.io.IOException {
            while (true) {
                if ((value & (~0x7fL)) == 0) {
                    out.writeByte(((int) (value)));
                    return;
                } else {
                    out.writeByte((((int) (value)) & 0x7f) | 0x80);
                    value >>>= 7;
                }
            } 
        }

        public static long[] readVarLongArray(java.io.DataInputStream in) throws java.io.IOException {
            final int size = in.readInt();
            if (size == (-1))
                return null;

            if (size < 0)
                throw new java.net.ProtocolException("negative array size");

            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = android.net.NetworkStatsHistory.DataStreamUtils.readVarLong(in);
            }
            return values;
        }

        public static void writeVarLongArray(java.io.DataOutputStream out, long[] values, int size) throws java.io.IOException {
            if (values == null) {
                out.writeInt(-1);
                return;
            }
            if (size > values.length) {
                throw new java.lang.IllegalArgumentException("size larger than length");
            }
            out.writeInt(size);
            for (int i = 0; i < size; i++) {
                android.net.NetworkStatsHistory.DataStreamUtils.writeVarLong(out, values[i]);
            }
        }
    }

    /**
     * Utility methods for interacting with {@link Parcel} structures, mostly
     * dealing with writing partial arrays.
     */
    public static class ParcelUtils {
        public static long[] readLongArray(android.os.Parcel in) {
            final int size = in.readInt();
            if (size == (-1))
                return null;

            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        public static void writeLongArray(android.os.Parcel out, long[] values, int size) {
            if (values == null) {
                out.writeInt(-1);
                return;
            }
            if (size > values.length) {
                throw new java.lang.IllegalArgumentException("size larger than length");
            }
            out.writeInt(size);
            for (int i = 0; i < size; i++) {
                out.writeLong(values[i]);
            }
        }
    }
}

