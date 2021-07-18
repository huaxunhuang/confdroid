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


public class FlagSetTest {
    @org.junit.Test
    public void testSimple1Level() {
        java.util.BitSet bs = new java.util.BitSet();
        bs.set(7);
        android.databinding.tool.writer.FlagSet flagSet = new android.databinding.tool.writer.FlagSet(bs, 3);
        org.junit.Assert.assertEquals(3, flagSet.buckets.length);
        org.junit.Assert.assertEquals(1 << 7, flagSet.buckets[0]);
        org.junit.Assert.assertEquals(0, flagSet.buckets[1]);
        org.junit.Assert.assertEquals(0, flagSet.buckets[2]);
    }

    @org.junit.Test
    public void testSimple2Level() {
        java.util.BitSet bs = new java.util.BitSet();
        bs.set(android.databinding.tool.writer.FlagSet.sBucketSize + 2);
        android.databinding.tool.writer.FlagSet flagSet = new android.databinding.tool.writer.FlagSet(bs, 3);
        org.junit.Assert.assertEquals(3, flagSet.buckets.length);
        org.junit.Assert.assertEquals(0, flagSet.buckets[0]);
        org.junit.Assert.assertEquals(1 << 2, flagSet.buckets[1]);
        org.junit.Assert.assertEquals(0, flagSet.buckets[2]);
    }

    @org.junit.Test
    public void testSimple3Level() {
        java.util.BitSet bs = new java.util.BitSet();
        bs.set(5);
        bs.set(android.databinding.tool.writer.FlagSet.sBucketSize + 2);
        bs.set((android.databinding.tool.writer.FlagSet.sBucketSize * 2) + 10);
        android.databinding.tool.writer.FlagSet flagSet = new android.databinding.tool.writer.FlagSet(bs, 3);
        org.junit.Assert.assertEquals(3, flagSet.buckets.length);
        org.junit.Assert.assertEquals(1 << 5, flagSet.buckets[0]);
        org.junit.Assert.assertEquals(1 << 2, flagSet.buckets[1]);
        org.junit.Assert.assertEquals(1 << 10, flagSet.buckets[2]);
    }

    @org.junit.Test
    public void testLargeValue() {
        java.util.BitSet bs = new java.util.BitSet();
        bs.set(43);
        android.databinding.tool.writer.FlagSet flagSet = new android.databinding.tool.writer.FlagSet(bs, 1);
        org.junit.Assert.assertEquals(1, flagSet.buckets.length);
        org.junit.Assert.assertEquals(1L << 43, flagSet.buckets[0]);
    }
}

