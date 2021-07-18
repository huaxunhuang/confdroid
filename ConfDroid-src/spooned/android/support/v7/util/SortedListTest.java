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
package android.support.v7.util;


@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class SortedListTest extends junit.framework.TestCase {
    android.support.v7.util.SortedList<android.support.v7.util.SortedListTest.Item> mList;

    java.util.List<android.support.v7.util.SortedListTest.Pair> mAdditions = new java.util.ArrayList<android.support.v7.util.SortedListTest.Pair>();

    java.util.List<android.support.v7.util.SortedListTest.Pair> mRemovals = new java.util.ArrayList<android.support.v7.util.SortedListTest.Pair>();

    java.util.List<android.support.v7.util.SortedListTest.Pair> mMoves = new java.util.ArrayList<android.support.v7.util.SortedListTest.Pair>();

    java.util.List<android.support.v7.util.SortedListTest.Pair> mUpdates = new java.util.ArrayList<android.support.v7.util.SortedListTest.Pair>();

    private android.support.v7.util.SortedList.Callback<android.support.v7.util.SortedListTest.Item> mCallback;

    android.support.v7.util.SortedListTest.InsertedCallback<android.support.v7.util.SortedListTest.Item> mInsertedCallback;

    android.support.v7.util.SortedListTest.ChangedCallback<android.support.v7.util.SortedListTest.Item> mChangedCallback;

    private java.util.Comparator<? super android.support.v7.util.SortedListTest.Item> sItemComparator = new java.util.Comparator<android.support.v7.util.SortedListTest.Item>() {
        @java.lang.Override
        public int compare(android.support.v7.util.SortedListTest.Item o1, android.support.v7.util.SortedListTest.Item o2) {
            return mCallback.compare(o1, o2);
        }
    };

    private abstract class InsertedCallback<T> {
        public abstract void onInserted(int position, int count);
    }

    private abstract class ChangedCallback<T> {
        public abstract void onChanged(int position, int count);
    }

    @java.lang.Override
    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        super.setUp();
        mCallback = new android.support.v7.util.SortedList.Callback<android.support.v7.util.SortedListTest.Item>() {
            @java.lang.Override
            public int compare(android.support.v7.util.SortedListTest.Item o1, android.support.v7.util.SortedListTest.Item o2) {
                return o1.cmpField < o2.cmpField ? -1 : o1.cmpField == o2.cmpField ? 0 : 1;
            }

            @java.lang.Override
            public void onInserted(int position, int count) {
                mAdditions.add(new android.support.v7.util.SortedListTest.Pair(position, count));
                if (mInsertedCallback != null) {
                    mInsertedCallback.onInserted(position, count);
                }
            }

            @java.lang.Override
            public void onRemoved(int position, int count) {
                mRemovals.add(new android.support.v7.util.SortedListTest.Pair(position, count));
            }

            @java.lang.Override
            public void onMoved(int fromPosition, int toPosition) {
                mMoves.add(new android.support.v7.util.SortedListTest.Pair(fromPosition, toPosition));
            }

            @java.lang.Override
            public void onChanged(int position, int count) {
                mUpdates.add(new android.support.v7.util.SortedListTest.Pair(position, count));
                if (mChangedCallback != null) {
                    mChangedCallback.onChanged(position, count);
                }
            }

            @java.lang.Override
            public boolean areContentsTheSame(android.support.v7.util.SortedListTest.Item oldItem, android.support.v7.util.SortedListTest.Item newItem) {
                return (oldItem.cmpField == newItem.cmpField) && (oldItem.data == newItem.data);
            }

            @java.lang.Override
            public boolean areItemsTheSame(android.support.v7.util.SortedListTest.Item item1, android.support.v7.util.SortedListTest.Item item2) {
                return item1.id == item2.id;
            }
        };
        mInsertedCallback = null;
        mChangedCallback = null;
        mList = new android.support.v7.util.SortedList<android.support.v7.util.SortedListTest.Item>(android.support.v7.util.SortedListTest.Item.class, mCallback);
    }

    @org.junit.Test
    public void testEmpty() {
        junit.framework.TestCase.assertEquals("empty", mList.size(), 0);
    }

    @org.junit.Test
    public void testAdd() {
        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item();
        junit.framework.TestCase.assertEquals(insert(item), 0);
        junit.framework.TestCase.assertEquals(size(), 1);
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 1)));
        android.support.v7.util.SortedListTest.Item item2 = new android.support.v7.util.SortedListTest.Item();
        item2.cmpField = item.cmpField + 1;
        junit.framework.TestCase.assertEquals(insert(item2), 1);
        junit.framework.TestCase.assertEquals(size(), 2);
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(1, 1)));
        android.support.v7.util.SortedListTest.Item item3 = new android.support.v7.util.SortedListTest.Item();
        item3.cmpField = item.cmpField - 1;
        mAdditions.clear();
        junit.framework.TestCase.assertEquals(insert(item3), 0);
        junit.framework.TestCase.assertEquals(size(), 3);
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 1)));
    }

    @org.junit.Test
    public void testAddDuplicate() {
        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item();
        android.support.v7.util.SortedListTest.Item item2 = new android.support.v7.util.SortedListTest.Item(item.id, item.cmpField);
        item2.data = item.data;
        insert(item);
        junit.framework.TestCase.assertEquals(0, insert(item2));
        junit.framework.TestCase.assertEquals(1, size());
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertEquals(0, mUpdates.size());
    }

    @org.junit.Test
    public void testRemove() {
        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item();
        junit.framework.TestCase.assertFalse(remove(item));
        junit.framework.TestCase.assertEquals(0, mRemovals.size());
        insert(item);
        junit.framework.TestCase.assertTrue(remove(item));
        junit.framework.TestCase.assertEquals(1, mRemovals.size());
        junit.framework.TestCase.assertTrue(mRemovals.contains(new android.support.v7.util.SortedListTest.Pair(0, 1)));
        junit.framework.TestCase.assertEquals(0, size());
        junit.framework.TestCase.assertFalse(remove(item));
        junit.framework.TestCase.assertEquals(1, mRemovals.size());
    }

    @org.junit.Test
    public void testRemove2() {
        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item();
        android.support.v7.util.SortedListTest.Item item2 = new android.support.v7.util.SortedListTest.Item(item.cmpField);
        insert(item);
        junit.framework.TestCase.assertFalse(remove(item2));
        junit.framework.TestCase.assertEquals(0, mRemovals.size());
    }

    @org.junit.Test
    public void clearTest() {
        insert(new android.support.v7.util.SortedListTest.Item(1));
        insert(new android.support.v7.util.SortedListTest.Item(2));
        junit.framework.TestCase.assertEquals(2, mList.size());
        mList.clear();
        junit.framework.TestCase.assertEquals(0, mList.size());
        insert(new android.support.v7.util.SortedListTest.Item(3));
        junit.framework.TestCase.assertEquals(1, mList.size());
    }

    @org.junit.Test
    public void testBatch() {
        mList.beginBatchedUpdates();
        for (int i = 0; i < 5; i++) {
            mList.add(new android.support.v7.util.SortedListTest.Item(i));
        }
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.endBatchedUpdates();
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 5)));
    }

    @org.junit.Test
    public void testRandom() throws java.lang.Throwable {
        java.util.Random random = new java.util.Random(java.lang.System.nanoTime());
        java.util.List<android.support.v7.util.SortedListTest.Item> copy = new java.util.ArrayList<android.support.v7.util.SortedListTest.Item>();
        java.lang.StringBuilder log = new java.lang.StringBuilder();
        try {
            for (int i = 0; i < 10000; i++) {
                switch (random.nextInt(3)) {
                    case 0 :
                        // ADD
                        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item();
                        copy.add(item);
                        insert(item);
                        log.append("add ").append(item).append("\n");
                        break;
                    case 1 :
                        // REMOVE
                        if (copy.size() > 0) {
                            int index = random.nextInt(mList.size());
                            item = mList.get(index);
                            log.append("remove ").append(item).append("\n");
                            junit.framework.TestCase.assertTrue(copy.remove(item));
                            junit.framework.TestCase.assertTrue(mList.remove(item));
                        }
                        break;
                    case 2 :
                        // UPDATE
                        if (copy.size() > 0) {
                            int index = random.nextInt(mList.size());
                            item = mList.get(index);
                            // TODO this cannot work
                            android.support.v7.util.SortedListTest.Item newItem = new android.support.v7.util.SortedListTest.Item(item.id, item.cmpField);
                            log.append("update ").append(item).append(" to ").append(newItem).append("\n");
                            while (newItem.data == item.data) {
                                newItem.data = random.nextInt(1000);
                            } 
                            int itemIndex = mList.add(newItem);
                            copy.remove(item);
                            copy.add(newItem);
                            junit.framework.TestCase.assertSame(mList.get(itemIndex), newItem);
                            junit.framework.TestCase.assertNotSame(mList.get(index), item);
                        }
                        break;
                    case 3 :
                        // UPDATE AT
                        if (copy.size() > 0) {
                            int index = random.nextInt(mList.size());
                            item = mList.get(index);
                            android.support.v7.util.SortedListTest.Item newItem = new android.support.v7.util.SortedListTest.Item(item.id, random.nextInt());
                            mList.updateItemAt(index, newItem);
                            copy.remove(item);
                            copy.add(newItem);
                        }
                }
                int lastCmp = java.lang.Integer.MIN_VALUE;
                for (int index = 0; index < copy.size(); index++) {
                    junit.framework.TestCase.assertFalse(mList.indexOf(copy.get(index)) == android.support.v7.util.SortedList.INVALID_POSITION);
                    junit.framework.TestCase.assertTrue(mList.get(index).cmpField >= lastCmp);
                    lastCmp = mList.get(index).cmpField;
                    junit.framework.TestCase.assertTrue(copy.contains(mList.get(index)));
                }
                for (int index = 0; index < mList.size(); index++) {
                    junit.framework.TestCase.assertNotNull(mList.mData[index]);
                }
                for (int index = mList.size(); index < mList.mData.length; index++) {
                    junit.framework.TestCase.assertNull(mList.mData[index]);
                }
            }
        } catch (java.lang.Throwable t) {
            java.util.Collections.sort(copy, sItemComparator);
            log.append("Items:\n");
            for (android.support.v7.util.SortedListTest.Item item : copy) {
                log.append(item).append("\n");
            }
            log.append("SortedList:\n");
            for (int i = 0; i < mList.size(); i++) {
                log.append(mList.get(i)).append("\n");
            }
            throw new java.lang.Throwable(" \nlog:\n" + log.toString(), t);
        }
    }

    private static android.support.v7.util.SortedListTest.Item[] createItems(int idFrom, int idTo, int idStep) {
        final int count = ((idTo - idFrom) / idStep) + 1;
        android.support.v7.util.SortedListTest.Item[] items = new android.support.v7.util.SortedListTest.Item[count];
        int id = idFrom;
        for (int i = 0; i < count; i++) {
            android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item(id, id);
            item.data = id;
            items[i] = item;
            id += idStep;
        }
        return items;
    }

    private static android.support.v7.util.SortedListTest.Item[] shuffle(android.support.v7.util.SortedListTest.Item[] items) {
        java.util.Random random = new java.util.Random(java.lang.System.nanoTime());
        final int count = items.length;
        for (int i = 0; i < count; i++) {
            int pos1 = random.nextInt(count);
            int pos2 = random.nextInt(count);
            if (pos1 != pos2) {
                android.support.v7.util.SortedListTest.Item temp = items[pos1];
                items[pos1] = items[pos2];
                items[pos2] = temp;
            }
        }
        return items;
    }

    private void assertIntegrity(int size, java.lang.String context) {
        junit.framework.TestCase.assertEquals(context + ": incorrect size", size, size());
        int rangeStart = 0;
        for (int i = 0; i < size(); i++) {
            android.support.v7.util.SortedListTest.Item item = mList.get(i);
            junit.framework.TestCase.assertNotNull((context + ": get returned null @") + i, item);
            junit.framework.TestCase.assertEquals((context + ": incorrect indexOf result @") + i, i, mList.indexOf(item));
            if (i == 0) {
                continue;
            }
            final int compare = mCallback.compare(mList.get(i - 1), item);
            junit.framework.TestCase.assertTrue((context + ": incorrect sorting order @") + i, compare <= 0);
            if (compare == 0) {
                for (int j = rangeStart; j < i; j++) {
                    junit.framework.TestCase.assertFalse((((context + ": duplicates found @") + j) + " and ") + i, mCallback.areItemsTheSame(mList.get(j), item));
                }
            } else {
                rangeStart = i;
            }
        }
    }

    private void assertSequentialOrder() {
        for (int i = 0; i < size(); i++) {
            junit.framework.TestCase.assertEquals(i, mList.get(i).cmpField);
        }
    }

    @org.junit.Test
    public void testAddAllMerge() throws java.lang.Throwable {
        mList.addAll(new android.support.v7.util.SortedListTest.Item[0]);
        assertIntegrity(0, "addAll, empty list, empty input");
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        // Add first 5 even numbers. Test adding to an empty list.
        mList.addAll(android.support.v7.util.SortedListTest.createItems(0, 8, 2));
        assertIntegrity(5, "addAll, empty list, non-empty input");
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 5)));
        mList.addAll(new android.support.v7.util.SortedListTest.Item[0]);
        assertIntegrity(5, "addAll, non-empty list, empty input");
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        // Add 5 more even numbers, shuffled (test pre-sorting).
        mList.addAll(android.support.v7.util.SortedListTest.shuffle(android.support.v7.util.SortedListTest.createItems(10, 18, 2)));
        assertIntegrity(10, "addAll, shuffled input");
        junit.framework.TestCase.assertEquals(2, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(5, 5)));
        // Add 5 more even numbers, reversed (test pre-sorting).
        mList.addAll(android.support.v7.util.SortedListTest.shuffle(android.support.v7.util.SortedListTest.createItems(28, 20, -2)));
        assertIntegrity(15, "addAll, reversed input");
        junit.framework.TestCase.assertEquals(3, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(10, 5)));
        // Add first 10 odd numbers.
        // Test the merge when the new items run out first.
        mList.addAll(android.support.v7.util.SortedListTest.createItems(1, 19, 2));
        assertIntegrity(25, "addAll, merging in the middle");
        junit.framework.TestCase.assertEquals(13, mAdditions.size());
        for (int i = 1; i <= 19; i += 2) {
            junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(i, 1)));
        }
        // Add 10 more odd numbers.
        // Test the merge when the old items run out first.
        mList.addAll(android.support.v7.util.SortedListTest.createItems(21, 39, 2));
        assertIntegrity(35, "addAll, merging at the end");
        junit.framework.TestCase.assertEquals(18, mAdditions.size());
        for (int i = 21; i <= 27; i += 2) {
            junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(i, 1)));
        }
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(29, 6)));
        // Add 5 more even numbers.
        mList.addAll(android.support.v7.util.SortedListTest.createItems(30, 38, 2));
        assertIntegrity(40, "addAll, merging more");
        junit.framework.TestCase.assertEquals(23, mAdditions.size());
        for (int i = 30; i <= 38; i += 2) {
            junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(i, 1)));
        }
        junit.framework.TestCase.assertEquals(0, mMoves.size());
        junit.framework.TestCase.assertEquals(0, mUpdates.size());
        junit.framework.TestCase.assertEquals(0, mRemovals.size());
        assertSequentialOrder();
    }

    @org.junit.Test
    public void testAddAllUpdates() throws java.lang.Throwable {
        // Add first 5 even numbers.
        android.support.v7.util.SortedListTest.Item[] evenItems = android.support.v7.util.SortedListTest.createItems(0, 8, 2);
        for (android.support.v7.util.SortedListTest.Item item : evenItems) {
            item.data = 1;
        }
        mList.addAll(evenItems);
        junit.framework.TestCase.assertEquals(5, size());
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 5)));
        junit.framework.TestCase.assertEquals(0, mUpdates.size());
        android.support.v7.util.SortedListTest.Item[] sameEvenItems = android.support.v7.util.SortedListTest.createItems(0, 8, 2);
        for (android.support.v7.util.SortedListTest.Item item : sameEvenItems) {
            item.data = 1;
        }
        mList.addAll(sameEvenItems);
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertEquals(0, mUpdates.size());
        android.support.v7.util.SortedListTest.Item[] newEvenItems = android.support.v7.util.SortedListTest.createItems(0, 8, 2);
        for (android.support.v7.util.SortedListTest.Item item : newEvenItems) {
            item.data = 2;
        }
        mList.addAll(newEvenItems);
        junit.framework.TestCase.assertEquals(5, size());
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertEquals(1, mUpdates.size());
        junit.framework.TestCase.assertTrue(mUpdates.contains(new android.support.v7.util.SortedListTest.Pair(0, 5)));
        for (int i = 0; i < 5; i++) {
            junit.framework.TestCase.assertEquals(2, mList.get(i).data);
        }
        // Add all numbers from 0 to 9
        android.support.v7.util.SortedListTest.Item[] sequentialItems = android.support.v7.util.SortedListTest.createItems(0, 9, 1);
        for (android.support.v7.util.SortedListTest.Item item : sequentialItems) {
            item.data = 3;
        }
        mList.addAll(sequentialItems);
        // Odd numbers should have been added.
        junit.framework.TestCase.assertEquals(6, mAdditions.size());
        for (int i = 0; i < 5; i++) {
            junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair((i * 2) + 1, 1)));
        }
        // All even items should have been updated.
        junit.framework.TestCase.assertEquals(6, mUpdates.size());
        for (int i = 0; i < 5; i++) {
            junit.framework.TestCase.assertTrue(mUpdates.contains(new android.support.v7.util.SortedListTest.Pair(i * 2, 1)));
        }
        junit.framework.TestCase.assertEquals(10, size());
        // All items should have the latest data value.
        for (int i = 0; i < 10; i++) {
            junit.framework.TestCase.assertEquals(3, mList.get(i).data);
        }
        junit.framework.TestCase.assertEquals(0, mMoves.size());
        junit.framework.TestCase.assertEquals(0, mRemovals.size());
        assertSequentialOrder();
    }

    @org.junit.Test
    public void testAddAllWithDuplicates() throws java.lang.Throwable {
        final int maxCmpField = 5;
        final int idsPerCmpField = 10;
        final int maxUniqueId = maxCmpField * idsPerCmpField;
        final int maxGeneration = 5;
        android.support.v7.util.SortedListTest.Item[] items = new android.support.v7.util.SortedListTest.Item[maxUniqueId * maxGeneration];
        int index = 0;
        for (int generation = 0; generation < maxGeneration; generation++) {
            int uniqueId = 0;
            for (int cmpField = 0; cmpField < maxCmpField; cmpField++) {
                for (int id = 0; id < idsPerCmpField; id++) {
                    android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item(uniqueId++, cmpField);
                    item.data = generation;
                    items[index++] = item;
                }
            }
        }
        mList.addAll(items);
        assertIntegrity(maxUniqueId, "addAll with duplicates");
        // Check that the most recent items have made it to the list.
        for (int i = 0; i != size(); i++) {
            android.support.v7.util.SortedListTest.Item item = mList.get(i);
            junit.framework.TestCase.assertEquals(maxGeneration - 1, item.data);
        }
    }

    @org.junit.Test
    public void testAddAllFast() throws java.lang.Throwable {
        mList.addAll(new android.support.v7.util.SortedListTest.Item[0], true);
        assertIntegrity(0, "addAll(T[],boolean), empty list, with empty input");
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.addAll(android.support.v7.util.SortedListTest.createItems(0, 9, 1), true);
        assertIntegrity(10, "addAll(T[],boolean), empty list, non-empty input");
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 10)));
        mList.addAll(new android.support.v7.util.SortedListTest.Item[0], true);
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        assertIntegrity(10, "addAll(T[],boolean), non-empty list, empty input");
        mList.addAll(android.support.v7.util.SortedListTest.createItems(10, 19, 1), true);
        junit.framework.TestCase.assertEquals(2, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(10, 10)));
        assertIntegrity(20, "addAll(T[],boolean), non-empty list, non-empty input");
    }

    @org.junit.Test
    public void testAddAllCollection() throws java.lang.Throwable {
        java.util.Collection<android.support.v7.util.SortedListTest.Item> itemList = new java.util.ArrayList<android.support.v7.util.SortedListTest.Item>();
        for (int i = 0; i < 5; i++) {
            itemList.add(new android.support.v7.util.SortedListTest.Item(i));
        }
        mList.addAll(itemList);
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, itemList.size())));
        assertIntegrity(itemList.size(), "addAll on collection");
    }

    @org.junit.Test
    public void testAddAllStableSort() {
        int id = 0;
        android.support.v7.util.SortedListTest.Item item = new android.support.v7.util.SortedListTest.Item(id++, 0);
        mList.add(item);
        // Create a few items with the same sort order.
        android.support.v7.util.SortedListTest.Item[] items = new android.support.v7.util.SortedListTest.Item[3];
        for (int i = 0; i < 3; i++) {
            items[i] = new android.support.v7.util.SortedListTest.Item(id++, item.cmpField);
            junit.framework.TestCase.assertEquals(0, mCallback.compare(item, items[i]));
        }
        mList.addAll(items);
        junit.framework.TestCase.assertEquals(1 + items.length, size());
        // Check that the order has been preserved.
        for (int i = 0; i < size(); i++) {
            junit.framework.TestCase.assertEquals(i, mList.get(i).id);
        }
    }

    @org.junit.Test
    public void testAddAllAccessFromCallbacks() {
        // Add first 5 even numbers.
        android.support.v7.util.SortedListTest.Item[] evenItems = android.support.v7.util.SortedListTest.createItems(0, 8, 2);
        for (android.support.v7.util.SortedListTest.Item item : evenItems) {
            item.data = 1;
        }
        mInsertedCallback = new android.support.v7.util.SortedListTest.InsertedCallback<android.support.v7.util.SortedListTest.Item>() {
            @java.lang.Override
            public void onInserted(int position, int count) {
                junit.framework.TestCase.assertEquals(0, position);
                junit.framework.TestCase.assertEquals(5, count);
                for (int i = 0; i < count; i++) {
                    junit.framework.TestCase.assertEquals(i * 2, mList.get(i).id);
                }
                assertIntegrity(5, ((("onInserted(" + position) + ", ") + count) + ")");
            }
        };
        mList.addAll(evenItems);
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertEquals(0, mUpdates.size());
        // Add all numbers from 0 to 9. This should trigger 5 change and 5 insert notifications.
        android.support.v7.util.SortedListTest.Item[] sequentialItems = android.support.v7.util.SortedListTest.createItems(0, 9, 1);
        for (android.support.v7.util.SortedListTest.Item item : sequentialItems) {
            item.data = 2;
        }
        mChangedCallback = new android.support.v7.util.SortedListTest.ChangedCallback<android.support.v7.util.SortedListTest.Item>() {
            int expectedSize = 5;

            @java.lang.Override
            public void onChanged(int position, int count) {
                junit.framework.TestCase.assertEquals(1, count);
                junit.framework.TestCase.assertEquals(position, mList.get(position).id);
                assertIntegrity(++expectedSize, ("onChanged(" + position) + ")");
            }
        };
        mInsertedCallback = new android.support.v7.util.SortedListTest.InsertedCallback<android.support.v7.util.SortedListTest.Item>() {
            int expectedSize = 5;

            @java.lang.Override
            public void onInserted(int position, int count) {
                junit.framework.TestCase.assertEquals(1, count);
                junit.framework.TestCase.assertEquals(position, mList.get(position).id);
                assertIntegrity(++expectedSize, ("onInserted(" + position) + ")");
            }
        };
        mList.addAll(sequentialItems);
        junit.framework.TestCase.assertEquals(6, mAdditions.size());
        junit.framework.TestCase.assertEquals(5, mUpdates.size());
    }

    @org.junit.Test
    public void testModificationFromCallbackThrows() {
        final android.support.v7.util.SortedListTest.Item extraItem = new android.support.v7.util.SortedListTest.Item(0);
        android.support.v7.util.SortedListTest.Item[] items = android.support.v7.util.SortedListTest.createItems(1, 5, 2);
        for (android.support.v7.util.SortedListTest.Item item : items) {
            item.data = 1;
        }
        mList.addAll(items);
        mInsertedCallback = new android.support.v7.util.SortedListTest.InsertedCallback<android.support.v7.util.SortedListTest.Item>() {
            @java.lang.Override
            public void onInserted(int position, int count) {
                try {
                    mList.add(new android.support.v7.util.SortedListTest.Item());
                    junit.framework.TestCase.fail("add must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.addAll(android.support.v7.util.SortedListTest.createItems(0, 0, 1));
                    junit.framework.TestCase.fail("addAll must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.addAll(android.support.v7.util.SortedListTest.createItems(0, 0, 1), true);
                    junit.framework.TestCase.fail("addAll(T[],boolean) must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.remove(extraItem);
                    junit.framework.TestCase.fail("remove must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.removeItemAt(0);
                    junit.framework.TestCase.fail("removeItemAt must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.updateItemAt(0, extraItem);
                    junit.framework.TestCase.fail("updateItemAt must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.recalculatePositionOfItemAt(0);
                    junit.framework.TestCase.fail("recalculatePositionOfItemAt must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
                try {
                    mList.clear();
                    junit.framework.TestCase.fail("recalculatePositionOfItemAt must throw from within a callback");
                } catch (java.lang.IllegalStateException e) {
                }
            }
        };
        // Make sure that the last one notification is change, so that the above callback is
        // not called from endBatchUpdates when the nested alls are actually OK.
        items = android.support.v7.util.SortedListTest.createItems(1, 5, 1);
        for (android.support.v7.util.SortedListTest.Item item : items) {
            item.data = 2;
        }
        mList.addAll(items);
        assertIntegrity(5, "Modification from callback");
    }

    @org.junit.Test
    public void testAddAllOutsideBatchedUpdates() {
        mList.add(new android.support.v7.util.SortedListTest.Item(1));
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(2));
        junit.framework.TestCase.assertEquals(2, mAdditions.size());
        mList.addAll(new android.support.v7.util.SortedListTest.Item(3), new android.support.v7.util.SortedListTest.Item(4));
        junit.framework.TestCase.assertEquals(3, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(5));
        junit.framework.TestCase.assertEquals(4, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(6));
        junit.framework.TestCase.assertEquals(5, mAdditions.size());
    }

    @org.junit.Test
    public void testAddAllInsideBatchedUpdates() {
        mList.beginBatchedUpdates();
        mList.add(new android.support.v7.util.SortedListTest.Item(1));
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(2));
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.addAll(new android.support.v7.util.SortedListTest.Item(3), new android.support.v7.util.SortedListTest.Item(4));
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(5));
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.add(new android.support.v7.util.SortedListTest.Item(6));
        junit.framework.TestCase.assertEquals(0, mAdditions.size());
        mList.endBatchedUpdates();
        junit.framework.TestCase.assertEquals(1, mAdditions.size());
        junit.framework.TestCase.assertTrue(mAdditions.contains(new android.support.v7.util.SortedListTest.Pair(0, 6)));
    }

    private int size() {
        return mList.size();
    }

    private int insert(android.support.v7.util.SortedListTest.Item item) {
        return mList.add(item);
    }

    private boolean remove(android.support.v7.util.SortedListTest.Item item) {
        return mList.remove(item);
    }

    static class Item {
        static int idCounter = 0;

        final int id;

        int cmpField;

        int data = ((int) (java.lang.Math.random() * 1000));// used for comparison


        public Item() {
            id = android.support.v7.util.SortedListTest.Item.idCounter++;
            cmpField = ((int) (java.lang.Math.random() * 1000));
        }

        public Item(int cmpField) {
            id = android.support.v7.util.SortedListTest.Item.idCounter++;
            this.cmpField = cmpField;
        }

        public Item(int id, int cmpField) {
            this.id = id;
            this.cmpField = cmpField;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.support.v7.util.SortedListTest.Item item = ((android.support.v7.util.SortedListTest.Item) (o));
            if (cmpField != item.cmpField) {
                return false;
            }
            if (id != item.id) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = id;
            result = (31 * result) + cmpField;
            return result;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((("Item{" + "id=") + id) + ", cmpField=") + cmpField) + ", data=") + data) + '}';
        }
    }

    private static final class Pair {
        final int first;

        final int second;

        public Pair(int first) {
            this.first = first;
            this.second = java.lang.Integer.MIN_VALUE;
        }

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.support.v7.util.SortedListTest.Pair pair = ((android.support.v7.util.SortedListTest.Pair) (o));
            if (first != pair.first) {
                return false;
            }
            if (second != pair.second) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public int hashCode() {
            int result = first;
            result = (31 * result) + second;
            return result;
        }
    }
}

