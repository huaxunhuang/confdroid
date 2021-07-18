/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.writer;


/**
 * Used for code generation. A BitSet can be converted into a flag set,
 * which is basically a list of longs that can be divided into pieces.
 */
public class FlagSet {
    public static final int sBucketSize = 64;// long


    public final java.lang.String type;

    public final long[] buckets;

    private java.lang.String mLocalName;

    private boolean mIsDynamic = false;

    public FlagSet(java.util.BitSet bitSet, int bucketCount) {
        buckets = new long[bucketCount];
        for (int i = bitSet.nextSetBit(0); i != (-1); i = bitSet.nextSetBit(i + 1)) {
            buckets[i / android.databinding.tool.writer.FlagSet.sBucketSize] |= 1L << (i % android.databinding.tool.writer.FlagSet.sBucketSize);
        }
        type = "long";
    }

    public FlagSet(long[] buckets) {
        this.buckets = new long[buckets.length];
        java.lang.System.arraycopy(buckets, 0, this.buckets, 0, buckets.length);
        type = "long";
    }

    public FlagSet(long[] buckets, int minBucketCount) {
        this.buckets = new long[java.lang.Math.max(buckets.length, minBucketCount)];
        java.lang.System.arraycopy(buckets, 0, this.buckets, 0, buckets.length);
        type = "long";
    }

    public FlagSet(int... bits) {
        int max = 0;
        for (int i = 0; i < bits.length; i++) {
            max = java.lang.Math.max(i, bits[i]);
        }
        buckets = new long[1 + (max / android.databinding.tool.writer.FlagSet.sBucketSize)];
        for (int x = 0; x < bits.length; x++) {
            final int i = bits[x];
            buckets[i / android.databinding.tool.writer.FlagSet.sBucketSize] |= 1L << (i % android.databinding.tool.writer.FlagSet.sBucketSize);
        }
        type = "long";
    }

    public boolean intersect(android.databinding.tool.writer.FlagSet other, int bucketIndex) {
        return (buckets[bucketIndex] & other.buckets[bucketIndex]) != 0;
    }

    public java.lang.String getLocalName() {
        return mLocalName;
    }

    public void setLocalName(java.lang.String localName) {
        mLocalName = localName;
    }

    public boolean hasLocalName() {
        return mLocalName != null;
    }

    public boolean isDynamic() {
        return mIsDynamic;
    }

    public void setDynamic(boolean isDynamic) {
        mIsDynamic = isDynamic;
    }

    public android.databinding.tool.writer.FlagSet andNot(android.databinding.tool.writer.FlagSet other) {
        android.databinding.tool.writer.FlagSet result = new android.databinding.tool.writer.FlagSet(buckets);
        final int min = java.lang.Math.min(buckets.length, other.buckets.length);
        for (int i = 0; i < min; i++) {
            result.buckets[i] &= ~other.buckets[i];
        }
        return result;
    }

    public android.databinding.tool.writer.FlagSet or(android.databinding.tool.writer.FlagSet other) {
        final android.databinding.tool.writer.FlagSet result = new android.databinding.tool.writer.FlagSet(buckets, other.buckets.length);
        for (int i = 0; i < other.buckets.length; i++) {
            result.buckets[i] |= other.buckets[i];
        }
        return result;
    }

    public boolean isEmpty() {
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != 0) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < buckets.length; i++) {
            sb.append(java.lang.Long.toBinaryString(buckets[i])).append(" ");
        }
        return sb.toString();
    }

    private long getBucket(int bucketIndex) {
        if (bucketIndex >= buckets.length) {
            return 0;
        }
        return buckets[bucketIndex];
    }

    public boolean bitsEqual(android.databinding.tool.writer.FlagSet other) {
        final int max = java.lang.Math.max(buckets.length, other.buckets.length);
        for (int i = 0; i < max; i++) {
            if (getBucket(i) != other.getBucket(i)) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = 1;
        for (long bucket : buckets) {
            hash = (hash * 7) ^ ((int) (bucket >>> 32));
            hash = (hash * 13) ^ ((int) (bucket & 0xffff));
        }
        return hash;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.databinding.tool.writer.FlagSet) {
            android.databinding.tool.writer.FlagSet other = ((android.databinding.tool.writer.FlagSet) (obj));
            if (other.buckets.length != buckets.length) {
                return false;
            }
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != other.buckets[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

