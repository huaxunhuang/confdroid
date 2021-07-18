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


public class ListChangeRegistryTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private android.databinding.ListChangeRegistry mListChangeRegistry;

    private int mCallCount;

    public ListChangeRegistryTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListChangeRegistry = new android.databinding.ListChangeRegistry();
        mCallCount = 0;
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
        mListChangeRegistry = null;
    }

    public void testNotifyChangedAll() {
        android.databinding.ObservableList.OnListChangedCallback listChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
                mCallCount++;
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeInserted should not be called");
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
                junit.framework.TestCase.fail("onItemRangeMoved should not be called");
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeRemoved should not be called");
            }
        };
        mListChangeRegistry.add(listChangedCallback);
        junit.framework.TestCase.assertEquals(0, mCallCount);
        mListChangeRegistry.notifyChanged(null);
        junit.framework.TestCase.assertEquals(1, mCallCount);
    }

    public void testNotifyChanged() {
        final int expectedStart = 10;
        final int expectedCount = 3;
        android.databinding.ObservableList.OnListChangedCallback listChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
                junit.framework.TestCase.fail("onChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.assertEquals(expectedStart, start);
                junit.framework.TestCase.assertEquals(expectedCount, count);
                mCallCount++;
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeInserted should not be called");
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
                junit.framework.TestCase.fail("onItemRangeMoved should not be called");
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeRemoved should not be called");
            }
        };
        mListChangeRegistry.add(listChangedCallback);
        junit.framework.TestCase.assertEquals(0, mCallCount);
        mListChangeRegistry.notifyChanged(null, expectedStart, expectedCount);
        junit.framework.TestCase.assertEquals(1, mCallCount);
    }

    public void testNotifyInserted() {
        final int expectedStart = 10;
        final int expectedCount = 3;
        android.databinding.ObservableList.OnListChangedCallback listChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
                junit.framework.TestCase.fail("onChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.assertEquals(expectedStart, start);
                junit.framework.TestCase.assertEquals(expectedCount, count);
                mCallCount++;
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
                junit.framework.TestCase.fail("onItemRangeMoved should not be called");
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeRemoved should not be called");
            }
        };
        mListChangeRegistry.add(listChangedCallback);
        junit.framework.TestCase.assertEquals(0, mCallCount);
        mListChangeRegistry.notifyInserted(null, expectedStart, expectedCount);
        junit.framework.TestCase.assertEquals(1, mCallCount);
    }

    public void testNotifyMoved() {
        final int expectedFrom = 10;
        final int expectedTo = 100;
        final int expectedCount = 3;
        android.databinding.ObservableList.OnListChangedCallback listChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
                junit.framework.TestCase.fail("onChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeInserted should not be called");
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
                junit.framework.TestCase.assertEquals(expectedFrom, from);
                junit.framework.TestCase.assertEquals(expectedTo, to);
                junit.framework.TestCase.assertEquals(expectedCount, count);
                mCallCount++;
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeRemoved should not be called");
            }
        };
        mListChangeRegistry.add(listChangedCallback);
        junit.framework.TestCase.assertEquals(0, mCallCount);
        mListChangeRegistry.notifyMoved(null, expectedFrom, expectedTo, expectedCount);
        junit.framework.TestCase.assertEquals(1, mCallCount);
    }

    public void testNotifyRemoved() {
        final int expectedStart = 10;
        final int expectedCount = 3;
        android.databinding.ObservableList.OnListChangedCallback listChangedCallback = new android.databinding.ObservableList.OnListChangedCallback() {
            @java.lang.Override
            public void onChanged(android.databinding.ObservableList sender) {
                junit.framework.TestCase.fail("onChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeChanged(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeChanged should not be called");
            }

            @java.lang.Override
            public void onItemRangeInserted(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.fail("onItemRangeInserted should not be called");
            }

            @java.lang.Override
            public void onItemRangeMoved(android.databinding.ObservableList sender, int from, int to, int count) {
                junit.framework.TestCase.fail("onItemRangeMoved should not be called");
            }

            @java.lang.Override
            public void onItemRangeRemoved(android.databinding.ObservableList sender, int start, int count) {
                junit.framework.TestCase.assertEquals(expectedStart, start);
                junit.framework.TestCase.assertEquals(expectedCount, count);
                mCallCount++;
            }
        };
        mListChangeRegistry.add(listChangedCallback);
        junit.framework.TestCase.assertEquals(0, mCallCount);
        mListChangeRegistry.notifyRemoved(null, expectedStart, expectedCount);
        junit.framework.TestCase.assertEquals(1, mCallCount);
    }
}

