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


@java.lang.SuppressWarnings("unchecked")
@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class SortedListBatchedCallbackTest {
    android.support.v7.util.SortedList.BatchedCallback mBatchedCallback;

    android.support.v7.util.SortedList.Callback mMockCallback;

    @org.junit.Before
    public void init() {
        mMockCallback = org.mockito.Mockito.mock(android.support.v7.util.SortedList.Callback.class);
        mBatchedCallback = new android.support.v7.util.SortedList.BatchedCallback(mMockCallback);
    }

    @org.junit.Test
    public void onChange() {
        mBatchedCallback.onChanged(1, 2);
        verifyZeroInteractions(mMockCallback);
        mBatchedCallback.dispatchLastEvent();
        verify(mMockCallback).onChanged(1, 2, null);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void onRemoved() {
        mBatchedCallback.onRemoved(2, 3);
        verifyZeroInteractions(mMockCallback);
        mBatchedCallback.dispatchLastEvent();
        onRemoved(2, 3);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void onInserted() {
        mBatchedCallback.onInserted(3, 4);
        verifyNoMoreInteractions(mMockCallback);
        mBatchedCallback.dispatchLastEvent();
        onInserted(3, 4);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void onMoved() {
        mBatchedCallback.onMoved(5, 6);
        // moves are not merged
        onMoved(5, 6);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void compare() {
        java.lang.Object o1 = new java.lang.Object();
        java.lang.Object o2 = new java.lang.Object();
        mBatchedCallback.compare(o1, o2);
        compare(o1, o2);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void areContentsTheSame() {
        java.lang.Object o1 = new java.lang.Object();
        java.lang.Object o2 = new java.lang.Object();
        mBatchedCallback.areContentsTheSame(o1, o2);
        areContentsTheSame(o1, o2);
        verifyNoMoreInteractions(mMockCallback);
    }

    @org.junit.Test
    public void areItemsTheSame() {
        java.lang.Object o1 = new java.lang.Object();
        java.lang.Object o2 = new java.lang.Object();
        mBatchedCallback.areItemsTheSame(o1, o2);
        areItemsTheSame(o1, o2);
        verifyNoMoreInteractions(mMockCallback);
    }
}

