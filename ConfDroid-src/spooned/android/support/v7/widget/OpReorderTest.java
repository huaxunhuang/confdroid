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
 * limitations under the License.
 */
package android.support.v7.widget;


@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class OpReorderTest {
    private static final java.lang.String TAG = "OpReorderTest";

    java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> mUpdateOps = new java.util.ArrayList<android.support.v7.widget.AdapterHelper.UpdateOp>();

    java.util.List<android.support.v7.widget.OpReorderTest.Item> mAddedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();

    java.util.List<android.support.v7.widget.OpReorderTest.Item> mRemovedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();

    java.util.Set<android.support.v7.widget.AdapterHelper.UpdateOp> mRecycledOps = new java.util.HashSet<android.support.v7.widget.AdapterHelper.UpdateOp>();

    static java.util.Random random = new java.util.Random(java.lang.System.nanoTime());

    android.support.v7.widget.OpReorderer mOpReorderer = new android.support.v7.widget.OpReorderer(new android.support.v7.widget.OpReorderer.Callback() {
        @java.lang.Override
        public android.support.v7.widget.AdapterHelper.UpdateOp obtainUpdateOp(int cmd, int startPosition, int itemCount, java.lang.Object payload) {
            return new android.support.v7.widget.AdapterHelper.UpdateOp(cmd, startPosition, itemCount, payload);
        }

        @java.lang.Override
        public void recycleUpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp op) {
            mRecycledOps.add(op);
        }
    });

    int itemCount = 10;

    int updatedItemCount = 0;

    public void setup(int count) {
        itemCount = count;
        updatedItemCount = itemCount;
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        cleanState();
    }

    void cleanState() {
        mUpdateOps = new java.util.ArrayList<android.support.v7.widget.AdapterHelper.UpdateOp>();
        mAddedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();
        mRemovedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();
        mRecycledOps = new java.util.HashSet<android.support.v7.widget.AdapterHelper.UpdateOp>();
        android.support.v7.widget.OpReorderTest.Item.idCounter = 0;
    }

    @org.junit.Test
    public void testMoveRemoved() throws java.lang.Exception {
        setup(10);
        mv(3, 8);
        rm(7, 3);
        process();
    }

    @org.junit.Test
    public void testMoveRemove() throws java.lang.Exception {
        setup(10);
        mv(3, 8);
        rm(3, 5);
        process();
    }

    @org.junit.Test
    public void test1() {
        setup(10);
        mv(3, 5);
        rm(3, 4);
        process();
    }

    @org.junit.Test
    public void test2() {
        setup(5);
        mv(1, 3);
        rm(1, 1);
        process();
    }

    @org.junit.Test
    public void test3() {
        setup(5);
        mv(0, 4);
        rm(2, 1);
        process();
    }

    @org.junit.Test
    public void test4() {
        setup(5);
        mv(3, 0);
        rm(3, 1);
        process();
    }

    @org.junit.Test
    public void test5() {
        setup(10);
        mv(8, 1);
        rm(6, 3);
        process();
    }

    @org.junit.Test
    public void test6() {
        setup(5);
        mv(1, 3);
        rm(0, 3);
        process();
    }

    @org.junit.Test
    public void test7() {
        setup(5);
        mv(3, 4);
        rm(3, 1);
        process();
    }

    @org.junit.Test
    public void test8() {
        setup(5);
        mv(4, 3);
        rm(3, 1);
        process();
    }

    @org.junit.Test
    public void test9() {
        setup(5);
        mv(2, 0);
        rm(2, 2);
        process();
    }

    @org.junit.Test
    public void testRandom() throws java.lang.Exception {
        for (int i = 0; i < 150; i++) {
            try {
                cleanState();
                setup(50);
                for (int j = 0; j < 50; j++) {
                    randOp(nextInt(android.support.v7.widget.OpReorderTest.random, nextInt(android.support.v7.widget.OpReorderTest.random, 4)));
                }
                android.util.Log.d(android.support.v7.widget.OpReorderTest.TAG, "running random test " + i);
                process();
            } catch (java.lang.Throwable t) {
                throw new java.lang.Exception((t.getMessage() + "\n") + opsToString(mUpdateOps));
            }
        }
    }

    @org.junit.Test
    public void testRandomMoveRemove() throws java.lang.Exception {
        for (int i = 0; i < 1000; i++) {
            try {
                cleanState();
                setup(5);
                orderedRandom(android.support.v7.widget.AdapterHelper.UpdateOp.MOVE, android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE);
                process();
            } catch (java.lang.Throwable t) {
                throw new java.lang.Exception((t.getMessage() + "\n") + opsToString(mUpdateOps));
            }
        }
    }

    @org.junit.Test
    public void testRandomMoveAdd() throws java.lang.Exception {
        for (int i = 0; i < 1000; i++) {
            try {
                cleanState();
                setup(5);
                orderedRandom(android.support.v7.widget.AdapterHelper.UpdateOp.MOVE, android.support.v7.widget.AdapterHelper.UpdateOp.ADD);
                process();
            } catch (java.lang.Throwable t) {
                throw new java.lang.Exception((t.getMessage() + "\n") + opsToString(mUpdateOps));
            }
        }
    }

    @org.junit.Test
    public void testRandomMoveUpdate() throws java.lang.Exception {
        for (int i = 0; i < 1000; i++) {
            try {
                cleanState();
                setup(5);
                orderedRandom(android.support.v7.widget.AdapterHelper.UpdateOp.MOVE, android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE);
                process();
            } catch (java.lang.Throwable t) {
                throw new java.lang.Exception((t.getMessage() + "\n") + opsToString(mUpdateOps));
            }
        }
    }

    private java.lang.String opsToString(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> updateOps) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.support.v7.widget.AdapterHelper.UpdateOp op : updateOps) {
            sb.append("\n").append(op.toString());
        }
        return sb.append("\n").toString();
    }

    public void orderedRandom(int... ops) {
        for (int op : ops) {
            randOp(op);
        }
    }

    void randOp(int cmd) {
        switch (cmd) {
            case android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE :
                if (updatedItemCount > 1) {
                    int s = nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount - 1);
                    int len = java.lang.Math.max(1, nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount - s));
                    rm(s, len);
                }
                break;
            case android.support.v7.widget.AdapterHelper.UpdateOp.ADD :
                int s = (updatedItemCount == 0) ? 0 : nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount);
                add(s, nextInt(android.support.v7.widget.OpReorderTest.random, 50));
                break;
            case android.support.v7.widget.AdapterHelper.UpdateOp.MOVE :
                if (updatedItemCount >= 2) {
                    int from = nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount);
                    int to;
                    do {
                        to = nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount);
                    } while (to == from );
                    mv(from, to);
                }
                break;
            case android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE :
                if (updatedItemCount > 1) {
                    s = nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount - 1);
                    int len = java.lang.Math.max(1, nextInt(android.support.v7.widget.OpReorderTest.random, updatedItemCount - s));
                    up(s, len);
                }
                break;
        }
    }

    int nextInt(java.util.Random random, int n) {
        if (n == 0) {
            return 0;
        }
        return random.nextInt(n);
    }

    android.support.v7.widget.AdapterHelper.UpdateOp rm(int start, int count) {
        updatedItemCount -= count;
        return record(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE, start, count, null));
    }

    android.support.v7.widget.AdapterHelper.UpdateOp mv(int from, int to) {
        return record(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.MOVE, from, to, null));
    }

    android.support.v7.widget.AdapterHelper.UpdateOp add(int start, int count) {
        updatedItemCount += count;
        return record(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.ADD, start, count, null));
    }

    android.support.v7.widget.AdapterHelper.UpdateOp up(int start, int count) {
        return record(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE, start, count, null));
    }

    android.support.v7.widget.AdapterHelper.UpdateOp record(android.support.v7.widget.AdapterHelper.UpdateOp op) {
        mUpdateOps.add(op);
        return op;
    }

    void process() {
        java.util.List<android.support.v7.widget.OpReorderTest.Item> items = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            items.add(android.support.v7.widget.OpReorderTest.Item.create());
        }
        java.util.List<android.support.v7.widget.OpReorderTest.Item> clones = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            clones.add(android.support.v7.widget.OpReorderTest.Item.clone(items.get(i)));
        }
        java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> rewritten = rewriteOps(mUpdateOps);
        assertAllMovesAtTheEnd(rewritten);
        apply(items, mUpdateOps);
        java.util.List<android.support.v7.widget.OpReorderTest.Item> originalAdded = mAddedItems;
        java.util.List<android.support.v7.widget.OpReorderTest.Item> originalRemoved = mRemovedItems;
        if (originalAdded.size() > 0) {
            android.support.v7.widget.OpReorderTest.Item.idCounter = originalAdded.get(0).id;
        }
        mAddedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();
        mRemovedItems = new java.util.ArrayList<android.support.v7.widget.OpReorderTest.Item>();
        apply(clones, rewritten);
        // now check equality
        assertListsIdentical(items, clones);
        assertHasTheSameItems(originalAdded, mAddedItems);
        assertHasTheSameItems(originalRemoved, mRemovedItems);
        assertRecycledOpsAreNotReused(items);
        assertRecycledOpsAreNotReused(clones);
    }

    private void assertRecycledOpsAreNotReused(java.util.List<android.support.v7.widget.OpReorderTest.Item> items) {
        for (android.support.v7.widget.OpReorderTest.Item item : items) {
            org.junit.Assert.assertFalse(mRecycledOps.contains(item));
        }
    }

    private void assertAllMovesAtTheEnd(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> ops) {
        boolean foundMove = false;
        for (android.support.v7.widget.AdapterHelper.UpdateOp op : ops) {
            if (op.cmd == android.support.v7.widget.AdapterHelper.UpdateOp.MOVE) {
                foundMove = true;
            } else {
                org.junit.Assert.assertFalse(foundMove);
            }
        }
    }

    private void assertHasTheSameItems(java.util.List<android.support.v7.widget.OpReorderTest.Item> items, java.util.List<android.support.v7.widget.OpReorderTest.Item> clones) {
        java.lang.String log = (("has the same items\n" + toString(items)) + "--\n") + toString(clones);
        org.junit.Assert.assertEquals(log, items.size(), clones.size());
        for (android.support.v7.widget.OpReorderTest.Item item : items) {
            for (android.support.v7.widget.OpReorderTest.Item clone : clones) {
                if ((item.id == clone.id) && (item.version == clone.version)) {
                    clones.remove(clone);
                    break;
                }
            }
        }
        org.junit.Assert.assertEquals(log, 0, clones.size());
    }

    private void assertListsIdentical(java.util.List<android.support.v7.widget.OpReorderTest.Item> items, java.util.List<android.support.v7.widget.OpReorderTest.Item> clones) {
        java.lang.String log = (("is identical\n" + toString(items)) + "--\n") + toString(clones);
        org.junit.Assert.assertEquals(items.size(), clones.size());
        for (int i = 0; i < items.size(); i++) {
            android.support.v7.widget.OpReorderTest.Item.assertIdentical(log, items.get(i), clones.get(i));
        }
    }

    private void apply(java.util.List<android.support.v7.widget.OpReorderTest.Item> items, java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> updateOps) {
        for (android.support.v7.widget.AdapterHelper.UpdateOp op : updateOps) {
            switch (op.cmd) {
                case android.support.v7.widget.AdapterHelper.UpdateOp.ADD :
                    for (int i = 0; i < op.itemCount; i++) {
                        final android.support.v7.widget.OpReorderTest.Item newItem = android.support.v7.widget.OpReorderTest.Item.create();
                        mAddedItems.add(newItem);
                        items.add(op.positionStart + i, newItem);
                    }
                    break;
                case android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE :
                    for (int i = 0; i < op.itemCount; i++) {
                        mRemovedItems.add(items.remove(op.positionStart));
                    }
                    break;
                case android.support.v7.widget.AdapterHelper.UpdateOp.MOVE :
                    items.add(op.itemCount, items.remove(op.positionStart));
                    break;
                case android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE :
                    for (int i = 0; i < op.itemCount; i++) {
                        final int index = op.positionStart + i;
                        items.get(index).version = items.get(index).version + 1;
                    }
                    break;
            }
        }
    }

    private java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> rewriteOps(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> updateOps) {
        java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> copy = new java.util.ArrayList<android.support.v7.widget.AdapterHelper.UpdateOp>();
        for (android.support.v7.widget.AdapterHelper.UpdateOp op : updateOps) {
            copy.add(new android.support.v7.widget.AdapterHelper.UpdateOp(op.cmd, op.positionStart, op.itemCount, null));
        }
        mOpReorderer.reorderOps(copy);
        return copy;
    }

    @org.junit.Test
    public void testSwapMoveRemove_1() {
        mv(10, 15);
        rm(2, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(mv(7, 12), mUpdateOps.get(1));
        org.junit.Assert.assertEquals(rm(2, 3), mUpdateOps.get(0));
    }

    @org.junit.Test
    public void testSwapMoveRemove_2() {
        mv(3, 8);
        rm(4, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(5, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(3, 6), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_3() {
        mv(3, 8);
        rm(3, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(4, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(3, 6), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_4() {
        mv(3, 8);
        rm(2, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(3, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(4, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(2, 1), mUpdateOps.get(1));
        org.junit.Assert.assertEquals(mv(2, 5), mUpdateOps.get(2));
    }

    @org.junit.Test
    public void testSwapMoveRemove_5() {
        mv(3, 0);
        rm(2, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(3, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(4, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(1, 2), mUpdateOps.get(1));
        org.junit.Assert.assertEquals(mv(1, 0), mUpdateOps.get(2));
    }

    @org.junit.Test
    public void testSwapMoveRemove_6() {
        mv(3, 10);
        rm(2, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(3, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(4, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(2, 1), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_7() {
        mv(3, 2);
        rm(6, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(6, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(3, 2), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_8() {
        mv(3, 4);
        rm(3, 1);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(1, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(4, 1), mUpdateOps.get(0));
    }

    @org.junit.Test
    public void testSwapMoveRemove_9() {
        mv(3, 4);
        rm(4, 1);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(1, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(3, 1), mUpdateOps.get(0));
    }

    @org.junit.Test
    public void testSwapMoveRemove_10() {
        mv(1, 3);
        rm(0, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(2, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(0, 1), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_11() {
        mv(3, 8);
        rm(7, 3);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(3, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(7, 2), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_12() {
        mv(1, 3);
        rm(2, 1);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(3, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(1, 2), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_13() {
        mv(1, 3);
        rm(1, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(1, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(2, 2), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_14() {
        mv(4, 2);
        rm(3, 1);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(2, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(2, 3), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveRemove_15() {
        mv(4, 2);
        rm(3, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(1, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(2, 2), mUpdateOps.get(0));
    }

    @org.junit.Test
    public void testSwapMoveRemove_16() {
        mv(2, 3);
        rm(1, 2);
        swapMoveRemove(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(rm(3, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(rm(1, 1), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveUpdate_0() {
        mv(1, 3);
        up(1, 2);
        swapMoveUpdate(mUpdateOps, 0);
        org.junit.Assert.assertEquals(2, mUpdateOps.size());
        org.junit.Assert.assertEquals(up(2, 2), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(mv(1, 3), mUpdateOps.get(1));
    }

    @org.junit.Test
    public void testSwapMoveUpdate_1() {
        mv(0, 2);
        up(0, 4);
        swapMoveUpdate(mUpdateOps, 0);
        org.junit.Assert.assertEquals(3, mUpdateOps.size());
        org.junit.Assert.assertEquals(up(0, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(up(1, 3), mUpdateOps.get(1));
        org.junit.Assert.assertEquals(mv(0, 2), mUpdateOps.get(2));
    }

    @org.junit.Test
    public void testSwapMoveUpdate_2() {
        mv(2, 0);
        up(1, 3);
        swapMoveUpdate(mUpdateOps, 0);
        org.junit.Assert.assertEquals(3, mUpdateOps.size());
        org.junit.Assert.assertEquals(up(3, 1), mUpdateOps.get(0));
        org.junit.Assert.assertEquals(up(0, 2), mUpdateOps.get(1));
        org.junit.Assert.assertEquals(mv(2, 0), mUpdateOps.get(2));
    }

    private void swapMoveUpdate(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> list, int move) {
        mOpReorderer.swapMoveUpdate(list, move, list.get(move), move + 1, list.get(move + 1));
    }

    private void swapMoveRemove(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> list, int move) {
        mOpReorderer.swapMoveRemove(list, move, list.get(move), move + 1, list.get(move + 1));
    }

    private java.lang.String toString(java.util.List<android.support.v7.widget.OpReorderTest.Item> items) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.support.v7.widget.OpReorderTest.Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }

    static class Item {
        static int idCounter = 0;

        int id;

        int version;

        Item(int id, int version) {
            this.id = id;
            this.version = version;
        }

        static android.support.v7.widget.OpReorderTest.Item create() {
            return new android.support.v7.widget.OpReorderTest.Item(android.support.v7.widget.OpReorderTest.Item.idCounter++, 1);
        }

        static android.support.v7.widget.OpReorderTest.Item clone(android.support.v7.widget.OpReorderTest.Item other) {
            return new android.support.v7.widget.OpReorderTest.Item(other.id, other.version);
        }

        public static void assertIdentical(java.lang.String logPrefix, android.support.v7.widget.OpReorderTest.Item item1, android.support.v7.widget.OpReorderTest.Item item2) {
            org.junit.Assert.assertEquals((((logPrefix + "\n") + item1) + " vs ") + item2, item1.id, item2.id);
            org.junit.Assert.assertEquals((((logPrefix + "\n") + item1) + " vs ") + item2, item1.version, item2.version);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((("Item{" + "id=") + id) + ", version=") + version) + '}';
        }
    }
}

