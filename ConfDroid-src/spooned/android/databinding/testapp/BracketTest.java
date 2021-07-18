/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.testapp;


public class BracketTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BracketTestBinding> {
    private java.lang.String[] mArray = new java.lang.String[]{ "Hello World" };

    private android.util.SparseArray<java.lang.String> mSparseArray = new android.util.SparseArray<>();

    private android.util.SparseIntArray mSparseIntArray = new android.util.SparseIntArray();

    private android.util.SparseBooleanArray mSparseBooleanArray = new android.util.SparseBooleanArray();

    private android.util.SparseLongArray mSparseLongArray = new android.util.SparseLongArray();

    private android.util.LongSparseArray<java.lang.String> mLongSparseArray = new android.util.LongSparseArray<>();

    public BracketTest() {
        super(android.databinding.testapp.databinding.BracketTestBinding.class);
        mSparseArray.put(0, "Hello");
        mLongSparseArray.put(0, "World");
        mSparseIntArray.put(0, 100);
        mSparseBooleanArray.put(0, true);
        mSparseLongArray.put(0, 5);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setArray(mArray);
                mBinder.setSparseArray(mSparseArray);
                mBinder.setSparseIntArray(mSparseIntArray);
                mBinder.setSparseBooleanArray(mSparseBooleanArray);
                mBinder.setSparseLongArray(mSparseLongArray);
                mBinder.setLongSparseArray(mLongSparseArray);
                mBinder.setIndexObj(((java.lang.Integer) (0)));
                mBinder.executePendingBindings();
            }
        });
    }

    @android.test.UiThreadTest
    public void testBrackets() {
        junit.framework.TestCase.assertEquals("Hello World", mBinder.arrayText.getText().toString());
        junit.framework.TestCase.assertEquals("Hello", mBinder.sparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("World", mBinder.longSparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("100", mBinder.sparseIntArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("true", mBinder.sparseBooleanArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("5", mBinder.sparseLongArrayText.getText().toString());
    }

    @android.test.UiThreadTest
    public void testBracketOutOfBounds() {
        mBinder.setIndex(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("", mBinder.arrayText.getText().toString());
        junit.framework.TestCase.assertEquals("", mBinder.sparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("", mBinder.longSparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("0", mBinder.sparseIntArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("false", mBinder.sparseBooleanArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("0", mBinder.sparseLongArrayText.getText().toString());
        mBinder.setIndex(-1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("", mBinder.arrayText.getText().toString());
        junit.framework.TestCase.assertEquals("", mBinder.sparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("", mBinder.longSparseArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("0", mBinder.sparseIntArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("false", mBinder.sparseBooleanArrayText.getText().toString());
        junit.framework.TestCase.assertEquals("0", mBinder.sparseLongArrayText.getText().toString());
    }

    @android.test.UiThreadTest
    public void testBracketObj() {
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World", mBinder.indexObj.getText().toString());
        junit.framework.TestCase.assertEquals("Hello", mBinder.sparseArrayTextObj.getText().toString());
    }

    @android.test.UiThreadTest
    public void testBracketMap() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals("", mBinder.bracketMap.getText().toString());
    }
}

