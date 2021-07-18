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
public class SortedListAdapterCallbackWrapperTest extends junit.framework.TestCase {
    private int lastReceivedType = android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE;

    private int lastReceivedPosition = -1;

    private int lastReceivedCount = -1;

    private android.support.v7.util.SortedList.Callback<java.lang.Object> mCallback = new android.support.v7.util.SortedList.Callback<java.lang.Object>() {
        @java.lang.Override
        public int compare(java.lang.Object o1, java.lang.Object o2) {
            return 0;
        }

        @java.lang.Override
        public void onInserted(int position, int count) {
            lastReceivedType = android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD;
            lastReceivedPosition = position;
            lastReceivedCount = count;
        }

        @java.lang.Override
        public void onRemoved(int position, int count) {
            lastReceivedType = android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE;
            lastReceivedPosition = position;
            lastReceivedCount = count;
        }

        @java.lang.Override
        public void onMoved(int fromPosition, int toPosition) {
            lastReceivedType = android.support.v7.util.SortedList.BatchedCallback.TYPE_MOVE;
            lastReceivedPosition = fromPosition;
            lastReceivedCount = toPosition;
        }

        @java.lang.Override
        public void onChanged(int position, int count) {
            lastReceivedType = android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE;
            lastReceivedPosition = position;
            lastReceivedCount = count;
        }

        @java.lang.Override
        public boolean areContentsTheSame(java.lang.Object oldItem, java.lang.Object newItem) {
            return false;
        }

        @java.lang.Override
        public boolean areItemsTheSame(java.lang.Object item1, java.lang.Object item2) {
            return false;
        }
    };

    private android.support.v7.util.SortedList.BatchedCallback<java.lang.Object> mBatched = new android.support.v7.util.SortedList.BatchedCallback<java.lang.Object>(mCallback);

    @org.junit.Test
    public void testAdd() throws java.lang.Throwable {
        mBatched.onInserted(0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 0, 3);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testRemove() throws java.lang.Throwable {
        mBatched.onRemoved(0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 0, 3);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testChange() throws java.lang.Throwable {
        mBatched.onChanged(0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 0, 3);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testMove() throws java.lang.Throwable {
        mBatched.onMoved(0, 3);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_MOVE, 0, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd1() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(3, 2);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 7);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd2() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(1, 2);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 5);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 1, 2);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd3() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(8, 2);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 7);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd4() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(9, 2);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 5);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 9, 2);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd5() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(4, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 6);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAdd6() throws java.lang.Throwable {
        mBatched.onInserted(3, 5);
        mBatched.onInserted(4, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.onInserted(4, 1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 3, 7);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchAddLoop() throws java.lang.Throwable {
        for (int i = 0; i < 10; i++) {
            mBatched.onInserted(4 + i, 1);
            assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
            assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 4, i + 1);
        }
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 4, 10);
    }

    @org.junit.Test
    public void testBatchAddReverseLoop() throws java.lang.Throwable {
        for (int i = 10; i >= 0; i--) {
            mBatched.onInserted(4, 1);
            assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
            assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 4, (10 - i) + 1);
        }
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 4, 11);
    }

    @org.junit.Test
    public void testBadBatchAddReverseLoop() throws java.lang.Throwable {
        for (int i = 10; i >= 0; i--) {
            mBatched.onInserted(4 + i, 1);
            if (i < 10) {
                assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, (4 + i) + 1, 1);
            }
        }
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_ADD, 4, 1);
    }

    @org.junit.Test
    public void testBatchRemove1() throws java.lang.Throwable {
        mBatched.onRemoved(3, 5);
        mBatched.onRemoved(3, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 3, 6);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchRemove2() throws java.lang.Throwable {
        mBatched.onRemoved(3, 5);
        mBatched.onRemoved(4, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 3, 5);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 4, 1);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchRemove3() throws java.lang.Throwable {
        mBatched.onRemoved(3, 5);
        mBatched.onRemoved(2, 3);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 3, 5);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_REMOVE, 2, 3);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchChange1() throws java.lang.Throwable {
        mBatched.onChanged(3, 5);
        mBatched.onChanged(3, 1);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 3, 5);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 3, 5);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchChange2() throws java.lang.Throwable {
        mBatched.onChanged(3, 5);
        mBatched.onChanged(2, 7);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 2, 7);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.dispatchLastEvent();
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 2, 7);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
    }

    @org.junit.Test
    public void testBatchChange3() throws java.lang.Throwable {
        mBatched.onChanged(3, 5);
        mBatched.onChanged(2, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        mBatched.onChanged(8, 2);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_NONE, -1, -1);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 2, 8);
    }

    @org.junit.Test
    public void testBatchChange4() throws java.lang.Throwable {
        mBatched.onChanged(3, 5);
        mBatched.onChanged(1, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 3, 5);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 1, 1);
    }

    @org.junit.Test
    public void testBatchChange5() throws java.lang.Throwable {
        mBatched.onChanged(3, 5);
        mBatched.onChanged(9, 1);
        assertLast(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 3, 5);
        assertPending(android.support.v7.util.SortedList.BatchedCallback.TYPE_CHANGE, 9, 1);
    }

    private void assertLast(int type, int position, int count) throws java.lang.Throwable {
        try {
            junit.framework.TestCase.assertEquals(lastReceivedType, type);
            if (position >= 0) {
                junit.framework.TestCase.assertEquals(lastReceivedPosition, position);
            }
            if (count >= 0) {
                junit.framework.TestCase.assertEquals(lastReceivedCount, count);
            }
        } catch (java.lang.Throwable t) {
            throw new java.lang.Throwable((("last event: expected=" + log(type, position, count)) + " found=") + log(lastReceivedType, lastReceivedPosition, lastReceivedCount), t);
        }
    }

    private void assertPending(int type, int position, int count) throws java.lang.Throwable {
        try {
            junit.framework.TestCase.assertEquals(mBatched.mLastEventType, type);
            if (position >= 0) {
                junit.framework.TestCase.assertEquals(mBatched.mLastEventPosition, position);
            }
            if (count >= 0) {
                junit.framework.TestCase.assertEquals(mBatched.mLastEventCount, count);
            }
        } catch (java.lang.Throwable t) {
            throw new java.lang.Throwable((("pending event: expected=" + log(type, position, count)) + " found=") + log(mBatched.mLastEventType, mBatched.mLastEventPosition, mBatched.mLastEventCount), t);
        }
    }

    private java.lang.String log(int type, int position, int count) {
        return (((android.support.v7.util.SortedListAdapterCallbackWrapperTest.TYPES_NAMES[type] + ", p:") + position) + ", c:") + count;
    }

    private static final java.lang.String[] TYPES_NAMES = new java.lang.String[]{ "none", "add", "remove", "change", "move" };
}

