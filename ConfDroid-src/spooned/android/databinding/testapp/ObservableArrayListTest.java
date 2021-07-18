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
package android.databinding.testapp;


public class ObservableArrayListTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private static final int ALL = 0;

    private static final int CHANGE = 1;

    private static final int INSERT = 2;

    private static final int MOVE = 3;

    private static final int REMOVE = 4;

    private android.databinding.ObservableList<java.lang.String> mObservable;

    private java.util.ArrayList<android.databinding.testapp.ObservableArrayListTest.ListChange> mNotifications = new java.util.ArrayList<>();

    private android.databinding.ObservableList.OnListChangedCallback mListener = new android.databinding.ObservableList.OnListChangedCallback() {
        @java.lang.Override
        public void onChanged(android.databinding.ObservableList sender) {
            mNotifications.add(new android.databinding.testapp.ObservableArrayListTest.ListChange(android.databinding.testapp.ObservableArrayListTest.ALL, 0, 0));
        }

        @java.lang.Override
        public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
            mNotifications.add(new android.databinding.testapp.ObservableArrayListTest.ListChange(android.databinding.testapp.ObservableArrayListTest.CHANGE, start, count));
        }

        @java.lang.Override
        public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
            mNotifications.add(new android.databinding.testapp.ObservableArrayListTest.ListChange(android.databinding.testapp.ObservableArrayListTest.INSERT, start, count));
        }

        @java.lang.Override
        public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
            mNotifications.add(new android.databinding.testapp.ObservableArrayListTest.ListChange(android.databinding.testapp.ObservableArrayListTest.MOVE, from, to, count));
        }

        @java.lang.Override
        public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
            mNotifications.add(new android.databinding.testapp.ObservableArrayListTest.ListChange(android.databinding.testapp.ObservableArrayListTest.REMOVE, start, count));
        }
    };

    private static class ListChange {
        public ListChange(int change, int start, int count) {
            this.start = start;
            this.count = count;
            this.from = 0;
            this.to = 0;
            this.change = change;
        }

        public ListChange(int change, int from, int to, int count) {
            this.from = from;
            this.to = to;
            this.count = count;
            this.start = 0;
            this.change = change;
        }

        public final int start;

        public final int count;

        public final int from;

        public final int to;

        public final int change;
    }

    public ObservableArrayListTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        mNotifications.clear();
        mObservable = new android.databinding.ObservableArrayList<>();
    }

    public void testAddListener() {
        mObservable.add("Hello");
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        mObservable.addOnListChangedCallback(mListener);
        mObservable.add("World");
        junit.framework.TestCase.assertFalse(mNotifications.isEmpty());
    }

    public void testRemoveListener() {
        // test there is no exception when the listener isn't there
        mObservable.removeOnListChangedCallback(mListener);
        mObservable.addOnListChangedCallback(mListener);
        mObservable.add("Hello");
        mNotifications.clear();
        mObservable.removeOnListChangedCallback(mListener);
        mObservable.add("World");
        junit.framework.TestCase.assertTrue(mNotifications.isEmpty());
        // test there is no exception when the listener isn't there
        mObservable.removeOnListChangedCallback(mListener);
    }

    public void testAdd() {
        android.databinding.ObservableList.OnListChangedCallback listChangedListener = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int i, int i1) {
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int i, int i1) {
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int i, int i1, int i2) {
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int i, int i1) {
            }
        };
        mObservable.addOnListChangedCallback(mListener);
        mObservable.addOnListChangedCallback(listChangedListener);
        mObservable.add("Hello");
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.INSERT, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(1, change.count);
        junit.framework.TestCase.assertEquals("Hello", mObservable.get(0));
    }

    public void testInsert() {
        mObservable.addOnListChangedCallback(mListener);
        mObservable.add("Hello");
        mObservable.add(0, "World");
        mObservable.add(1, "Dang");
        mObservable.add(3, "End");
        junit.framework.TestCase.assertEquals(4, mObservable.size());
        junit.framework.TestCase.assertEquals("World", mObservable.get(0));
        junit.framework.TestCase.assertEquals("Dang", mObservable.get(1));
        junit.framework.TestCase.assertEquals("Hello", mObservable.get(2));
        junit.framework.TestCase.assertEquals("End", mObservable.get(3));
        junit.framework.TestCase.assertEquals(4, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(1);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.INSERT, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(1, change.count);
    }

    public void testAddAll() {
        java.util.ArrayList<java.lang.String> toAdd = new java.util.ArrayList<>();
        toAdd.add("Hello");
        toAdd.add("World");
        mObservable.add("First");
        mObservable.addOnListChangedCallback(mListener);
        mObservable.addAll(toAdd);
        junit.framework.TestCase.assertEquals(3, mObservable.size());
        junit.framework.TestCase.assertEquals("Hello", mObservable.get(1));
        junit.framework.TestCase.assertEquals("World", mObservable.get(2));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.INSERT, change.change);
        junit.framework.TestCase.assertEquals(1, change.start);
        junit.framework.TestCase.assertEquals(2, change.count);
    }

    public void testInsertAll() {
        java.util.ArrayList<java.lang.String> toAdd = new java.util.ArrayList<>();
        toAdd.add("Hello");
        toAdd.add("World");
        mObservable.add("First");
        mObservable.addOnListChangedCallback(mListener);
        mObservable.addAll(0, toAdd);
        junit.framework.TestCase.assertEquals(3, mObservable.size());
        junit.framework.TestCase.assertEquals("Hello", mObservable.get(0));
        junit.framework.TestCase.assertEquals("World", mObservable.get(1));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.INSERT, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(2, change.count);
    }

    public void testClear() {
        mObservable.add("Hello");
        mObservable.add("World");
        mObservable.addOnListChangedCallback(mListener);
        mObservable.clear();
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.REMOVE, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(2, change.count);
        mObservable.clear();
        // No notification when nothing is cleared.
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
    }

    public void testRemoveIndex() {
        mObservable.add("Hello");
        mObservable.add("World");
        mObservable.addOnListChangedCallback(mListener);
        junit.framework.TestCase.assertEquals("Hello", mObservable.remove(0));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.REMOVE, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(1, change.count);
    }

    public void testRemoveObject() {
        mObservable.add("Hello");
        mObservable.add("World");
        mObservable.addOnListChangedCallback(mListener);
        junit.framework.TestCase.assertTrue(mObservable.remove("Hello"));
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.REMOVE, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(1, change.count);
        junit.framework.TestCase.assertFalse(mObservable.remove("Hello"));
        // nothing removed, don't notify
        junit.framework.TestCase.assertEquals(1, mNotifications.size());
    }

    public void testSet() {
        mObservable.add("Hello");
        mObservable.add("World");
        mObservable.addOnListChangedCallback(mListener);
        junit.framework.TestCase.assertEquals("Hello", mObservable.set(0, "Goodbye"));
        junit.framework.TestCase.assertEquals("Goodbye", mObservable.get(0));
        junit.framework.TestCase.assertEquals(2, mObservable.size());
        android.databinding.testapp.ObservableArrayListTest.ListChange change = mNotifications.get(0);
        junit.framework.TestCase.assertEquals(android.databinding.testapp.ObservableArrayListTest.CHANGE, change.change);
        junit.framework.TestCase.assertEquals(0, change.start);
        junit.framework.TestCase.assertEquals(1, change.count);
    }
}

