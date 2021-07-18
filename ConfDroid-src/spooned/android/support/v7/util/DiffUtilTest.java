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
package android.support.v7.util;


@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class DiffUtilTest {
    private static java.util.Random sRand = new java.util.Random(java.lang.System.nanoTime());

    private java.util.List<android.support.v7.util.DiffUtilTest.Item> mBefore = new java.util.ArrayList<>();

    private java.util.List<android.support.v7.util.DiffUtilTest.Item> mAfter = new java.util.ArrayList<>();

    private java.lang.StringBuilder mLog = new java.lang.StringBuilder();

    private android.support.v7.util.DiffUtil.Callback mCallback = new android.support.v7.util.DiffUtil.Callback() {
        @java.lang.Override
        public int getOldListSize() {
            return mBefore.size();
        }

        @java.lang.Override
        public int getNewListSize() {
            return mAfter.size();
        }

        @java.lang.Override
        public boolean areItemsTheSame(int oldItemIndex, int newItemIndex) {
            return mBefore.get(oldItemIndex).id == mAfter.get(newItemIndex).id;
        }

        @java.lang.Override
        public boolean areContentsTheSame(int oldItemIndex, int newItemIndex) {
            org.hamcrest.MatcherAssert.assertThat(mBefore.get(oldItemIndex).id, org.hamcrest.CoreMatchers.equalTo(mAfter.get(newItemIndex).id));
            return mBefore.get(oldItemIndex).data.equals(mAfter.get(newItemIndex).data);
        }

        @android.support.annotation.Nullable
        @java.lang.Override
        public java.lang.Object getChangePayload(int oldItemIndex, int newItemIndex) {
            org.hamcrest.MatcherAssert.assertThat(mBefore.get(oldItemIndex).id, org.hamcrest.CoreMatchers.equalTo(mAfter.get(newItemIndex).id));
            org.hamcrest.MatcherAssert.assertThat(mBefore.get(oldItemIndex).data, org.hamcrest.CoreMatchers.not(org.hamcrest.CoreMatchers.equalTo(mAfter.get(newItemIndex).data)));
            return mAfter.get(newItemIndex).payload;
        }
    };

    @org.junit.Rule
    public org.junit.rules.TestWatcher mLogOnExceptionWatcher = new org.junit.rules.TestWatcher() {
        @java.lang.Override
        protected void failed(java.lang.Throwable e, org.junit.runner.Description description) {
            java.lang.System.err.println(mLog.toString());
        }
    };

    @org.junit.Test
    public void testNoChange() {
        initWithSize(5);
        check();
    }

    @org.junit.Test
    public void testAddItems() {
        initWithSize(2);
        add(1);
        check();
    }

    @org.junit.Test
    @android.test.suitebuilder.annotation.LargeTest
    public void testRandom() {
        for (int x = 0; x < 100; x++) {
            for (int i = 0; i < 100; i++) {
                for (int j = 2; j < 40; j++) {
                    testRandom(i, j);
                }
            }
        }
    }

    @org.junit.Test
    public void testGen2() {
        initWithSize(5);
        add(5);
        delete(3);
        delete(1);
        check();
    }

    @org.junit.Test
    public void testGen3() {
        initWithSize(5);
        add(0);
        delete(1);
        delete(3);
        check();
    }

    @org.junit.Test
    public void testGen4() {
        initWithSize(5);
        add(5);
        add(1);
        add(4);
        add(4);
        check();
    }

    @org.junit.Test
    public void testGen5() {
        initWithSize(5);
        delete(0);
        delete(2);
        add(0);
        add(2);
        check();
    }

    @org.junit.Test
    public void testGen6() {
        initWithSize(2);
        delete(0);
        delete(0);
        check();
    }

    @org.junit.Test
    public void testGen7() {
        initWithSize(3);
        move(2, 0);
        delete(2);
        add(2);
        check();
    }

    @org.junit.Test
    public void testGen8() {
        initWithSize(3);
        delete(1);
        add(0);
        move(2, 0);
        check();
    }

    @org.junit.Test
    public void testGen9() {
        initWithSize(2);
        add(2);
        move(0, 2);
        check();
    }

    @org.junit.Test
    public void testGen10() {
        initWithSize(3);
        move(0, 1);
        move(1, 2);
        add(0);
        check();
    }

    @org.junit.Test
    public void testGen11() {
        initWithSize(4);
        move(2, 0);
        move(2, 3);
        check();
    }

    @org.junit.Test
    public void testGen12() {
        initWithSize(4);
        move(3, 0);
        move(2, 1);
        check();
    }

    @org.junit.Test
    public void testGen13() {
        initWithSize(4);
        move(3, 2);
        move(0, 3);
        check();
    }

    @org.junit.Test
    public void testGen14() {
        initWithSize(4);
        move(3, 2);
        add(4);
        move(0, 4);
        check();
    }

    @org.junit.Test
    public void testAdd1() {
        initWithSize(1);
        add(1);
        check();
    }

    @org.junit.Test
    public void testMove1() {
        initWithSize(3);
        move(0, 2);
        check();
    }

    @org.junit.Test
    public void tmp() {
        initWithSize(4);
        move(0, 2);
        check();
    }

    @org.junit.Test
    public void testUpdate1() {
        initWithSize(3);
        update(2);
        check();
    }

    @org.junit.Test
    public void testUpdate2() {
        initWithSize(2);
        add(1);
        update(1);
        update(2);
        check();
    }

    @org.junit.Test
    public void testDisableMoveDetection() {
        initWithSize(5);
        move(0, 4);
        java.util.List<android.support.v7.util.DiffUtilTest.Item> applied = applyUpdates(mBefore, android.support.v7.util.DiffUtil.calculateDiff(mCallback, false));
        org.hamcrest.MatcherAssert.assertThat(applied.size(), org.hamcrest.CoreMatchers.is(5));
        org.hamcrest.MatcherAssert.assertThat(applied.get(4).newItem, org.hamcrest.CoreMatchers.is(true));
        org.hamcrest.MatcherAssert.assertThat(applied.contains(mBefore.get(0)), org.hamcrest.CoreMatchers.is(false));
    }

    private void testRandom(int initialSize, int operationCount) {
        mLog.setLength(0);
        initWithSize(initialSize);
        for (int i = 0; i < operationCount; i++) {
            int op = android.support.v7.util.DiffUtilTest.sRand.nextInt(5);
            switch (op) {
                case 0 :
                    add(android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size() + 1));
                    break;
                case 1 :
                    if (!mAfter.isEmpty()) {
                        delete(android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size()));
                    }
                    break;
                case 2 :
                    // move
                    if (mAfter.size() > 0) {
                        move(android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size()), android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size()));
                    }
                    break;
                case 3 :
                    // update
                    if (mAfter.size() > 0) {
                        update(android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size()));
                    }
                    break;
                case 4 :
                    // update with payload
                    if (mAfter.size() > 0) {
                        updateWithPayload(android.support.v7.util.DiffUtilTest.sRand.nextInt(mAfter.size()));
                    }
                    break;
            }
        }
        check();
    }

    private void check() {
        android.support.v7.util.DiffUtil.DiffResult result = android.support.v7.util.DiffUtil.calculateDiff(mCallback);
        log("before", mBefore);
        log("after", mAfter);
        log("snakes", result.getSnakes());
        java.util.List<android.support.v7.util.DiffUtilTest.Item> applied = applyUpdates(mBefore, result);
        assertEquals(applied, mAfter);
    }

    private void initWithSize(int size) {
        mBefore.clear();
        mAfter.clear();
        for (int i = 0; i < size; i++) {
            mBefore.add(new android.support.v7.util.DiffUtilTest.Item(false));
        }
        mAfter.addAll(mBefore);
        mLog.append(("initWithSize(" + size) + ");\n");
    }

    private void log(java.lang.String title, java.util.List<?> items) {
        mLog.append(title).append(":").append(items.size()).append("\n");
        for (java.lang.Object item : items) {
            mLog.append("  ").append(item).append("\n");
        }
    }

    private void assertEquals(java.util.List<android.support.v7.util.DiffUtilTest.Item> applied, java.util.List<android.support.v7.util.DiffUtilTest.Item> after) {
        log("applied", applied);
        java.lang.String report = mLog.toString();
        org.hamcrest.MatcherAssert.assertThat(report, applied.size(), org.hamcrest.CoreMatchers.is(after.size()));
        for (int i = 0; i < after.size(); i++) {
            android.support.v7.util.DiffUtilTest.Item item = applied.get(i);
            if (after.get(i).newItem) {
                org.hamcrest.MatcherAssert.assertThat(report, item.newItem, org.hamcrest.CoreMatchers.is(true));
            } else
                if (after.get(i).changed) {
                    org.hamcrest.MatcherAssert.assertThat(report, item.newItem, org.hamcrest.CoreMatchers.is(false));
                    org.hamcrest.MatcherAssert.assertThat(report, item.changed, org.hamcrest.CoreMatchers.is(true));
                    org.hamcrest.MatcherAssert.assertThat(report, item.id, org.hamcrest.CoreMatchers.is(after.get(i).id));
                    org.hamcrest.MatcherAssert.assertThat(report, item.payload, org.hamcrest.CoreMatchers.is(after.get(i).payload));
                } else {
                    org.hamcrest.MatcherAssert.assertThat(report, item, org.hamcrest.CoreMatchers.equalTo(after.get(i)));
                }

        }
    }

    private java.util.List<android.support.v7.util.DiffUtilTest.Item> applyUpdates(java.util.List<android.support.v7.util.DiffUtilTest.Item> before, android.support.v7.util.DiffUtil.DiffResult result) {
        final java.util.List<android.support.v7.util.DiffUtilTest.Item> target = new java.util.ArrayList<>();
        target.addAll(before);
        result.dispatchUpdatesTo(new android.support.v7.util.ListUpdateCallback() {
            @java.lang.Override
            public void onInserted(int position, int count) {
                for (int i = 0; i < count; i++) {
                    target.add(i + position, new android.support.v7.util.DiffUtilTest.Item(true));
                }
            }

            @java.lang.Override
            public void onRemoved(int position, int count) {
                for (int i = 0; i < count; i++) {
                    target.remove(position);
                }
            }

            @java.lang.Override
            public void onMoved(int fromPosition, int toPosition) {
                android.support.v7.util.DiffUtilTest.Item item = target.remove(fromPosition);
                target.add(toPosition, item);
            }

            @java.lang.Override
            public void onChanged(int position, int count, java.lang.Object payload) {
                for (int i = 0; i < count; i++) {
                    int positionInList = position + i;
                    android.support.v7.util.DiffUtilTest.Item existing = target.get(positionInList);
                    // make sure we don't update same item twice in callbacks
                    org.hamcrest.MatcherAssert.assertThat(existing.changed, org.hamcrest.CoreMatchers.is(false));
                    org.hamcrest.MatcherAssert.assertThat(existing.newItem, org.hamcrest.CoreMatchers.is(false));
                    org.hamcrest.MatcherAssert.assertThat(existing.payload, org.hamcrest.CoreMatchers.is(org.hamcrest.CoreMatchers.nullValue()));
                    android.support.v7.util.DiffUtilTest.Item replica = new android.support.v7.util.DiffUtilTest.Item(existing);
                    replica.payload = ((java.lang.String) (payload));
                    replica.changed = true;
                    target.remove(positionInList);
                    target.add(positionInList, replica);
                }
            }
        });
        return target;
    }

    private void add(int index) {
        mAfter.add(index, new android.support.v7.util.DiffUtilTest.Item(true));
        mLog.append("add(").append(index).append(");\n");
    }

    private void delete(int index) {
        mAfter.remove(index);
        mLog.append("delete(").append(index).append(");\n");
    }

    private void update(int index) {
        android.support.v7.util.DiffUtilTest.Item existing = mAfter.get(index);
        if (existing.newItem) {
            return;// new item cannot be changed

        }
        android.support.v7.util.DiffUtilTest.Item replica = new android.support.v7.util.DiffUtilTest.Item(existing);
        replica.changed = true;
        // clean the payload since this might be after an updateWithPayload call
        replica.payload = null;
        replica.data = java.util.UUID.randomUUID().toString();
        mAfter.remove(index);
        mAfter.add(index, replica);
        mLog.append("update(").append(index).append(");\n");
    }

    private void updateWithPayload(int index) {
        android.support.v7.util.DiffUtilTest.Item existing = mAfter.get(index);
        if (existing.newItem) {
            return;// new item cannot be changed

        }
        android.support.v7.util.DiffUtilTest.Item replica = new android.support.v7.util.DiffUtilTest.Item(existing);
        replica.changed = true;
        replica.data = java.util.UUID.randomUUID().toString();
        replica.payload = java.util.UUID.randomUUID().toString();
        mAfter.remove(index);
        mAfter.add(index, replica);
        mLog.append("update(").append(index).append(");\n");
    }

    private void move(int from, int to) {
        android.support.v7.util.DiffUtilTest.Item removed = mAfter.remove(from);
        mAfter.add(to, removed);
        mLog.append("move(").append(from).append(",").append(to).append(");\n");
    }

    static class Item {
        static long idCounter = 0;

        final long id;

        final boolean newItem;

        boolean changed = false;

        java.lang.String payload;

        java.lang.String data = java.util.UUID.randomUUID().toString();

        public Item(boolean newItem) {
            id = android.support.v7.util.DiffUtilTest.Item.idCounter++;
            this.newItem = newItem;
        }

        public Item(android.support.v7.util.DiffUtilTest.Item other) {
            id = other.id;
            newItem = other.newItem;
            changed = other.changed;
            payload = other.payload;
            data = other.data;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            android.support.v7.util.DiffUtilTest.Item item = ((android.support.v7.util.DiffUtilTest.Item) (o));
            if (id != item.id)
                return false;

            if (newItem != item.newItem)
                return false;

            if (changed != item.changed)
                return false;

            if (payload != null ? !payload.equals(item.payload) : item.payload != null) {
                return false;
            }
            return data.equals(item.data);
        }

        @java.lang.Override
        public int hashCode() {
            int result = ((int) (id ^ (id >>> 32)));
            result = (31 * result) + (newItem ? 1 : 0);
            result = (31 * result) + (changed ? 1 : 0);
            result = (31 * result) + (payload != null ? payload.hashCode() : 0);
            result = (31 * result) + data.hashCode();
            return result;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((("Item{" + "id=") + id) + ", newItem=") + newItem) + ", changed=") + changed) + ", payload='") + payload) + '\'') + ", data='") + data) + '\'') + '}';
        }
    }
}

