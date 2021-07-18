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


public class NoIdTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.NoIdTestBinding> {
    public NoIdTest() {
        super(android.databinding.testapp.databinding.NoIdTestBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setName("hello");
                mBinder.setOrientation(android.widget.LinearLayout.VERTICAL);
                mBinder.executePendingBindings();
            }
        });
    }

    @android.test.UiThreadTest
    public void testOnRoot() {
        android.widget.LinearLayout linearLayout = ((android.widget.LinearLayout) (mBinder.getRoot()));
        junit.framework.TestCase.assertEquals(android.widget.LinearLayout.VERTICAL, linearLayout.getOrientation());
        mBinder.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(android.widget.LinearLayout.HORIZONTAL, linearLayout.getOrientation());
    }

    @android.test.UiThreadTest
    public void testNormal() {
        android.widget.LinearLayout linearLayout = ((android.widget.LinearLayout) (mBinder.getRoot()));
        android.widget.TextView view = ((android.widget.TextView) (linearLayout.getChildAt(0)));
        junit.framework.TestCase.assertEquals("hello world", view.getTag());
        junit.framework.TestCase.assertEquals("hello", view.getText().toString());
        mBinder.setName("world");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("world", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testNoTag() {
        android.widget.LinearLayout linearLayout = ((android.widget.LinearLayout) (mBinder.getRoot()));
        android.widget.TextView view = ((android.widget.TextView) (linearLayout.getChildAt(1)));
        junit.framework.TestCase.assertNull(view.getTag());
    }

    @android.test.UiThreadTest
    public void testResourceTag() {
        android.widget.LinearLayout linearLayout = ((android.widget.LinearLayout) (mBinder.getRoot()));
        android.widget.TextView view = ((android.widget.TextView) (linearLayout.getChildAt(2)));
        java.lang.String expectedValue = view.getResources().getString(R.string.app_name);
        junit.framework.TestCase.assertEquals(expectedValue, view.getTag());
    }

    @android.test.UiThreadTest
    public void testAndroidResourceTag() {
        android.widget.LinearLayout linearLayout = ((android.widget.LinearLayout) (mBinder.getRoot()));
        android.widget.TextView view = ((android.widget.TextView) (linearLayout.getChildAt(3)));
        java.lang.String expectedValue = view.getResources().getString(android.R.string.ok);
        junit.framework.TestCase.assertEquals(expectedValue, view.getTag());
    }

    @android.test.UiThreadTest
    public void testIdOnly() {
        junit.framework.TestCase.assertEquals("hello", mBinder.textView.getText().toString());
    }
}

