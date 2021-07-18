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


public class ResourceTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ResourceTestBinding> {
    public ResourceTest() {
        super(android.databinding.testapp.databinding.ResourceTestBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setCount(0);
                mBinder.setTitle("Mrs.");
                mBinder.setLastName("Doubtfire");
                mBinder.setBase(2);
                mBinder.setPbase(3);
                mBinder.executePendingBindings();
            }
        });
    }

    @android.test.UiThreadTest
    public void testStringFormat() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textView0;
        junit.framework.TestCase.assertEquals("Mrs. Doubtfire", view.getText().toString());
        mBinder.setTitle("Mr.");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Mr. Doubtfire", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testQuantityString() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textView1;
        junit.framework.TestCase.assertEquals("oranges", view.getText().toString());
        mBinder.setCount(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("orange", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testFractionNoParameters() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.fractionNoParameters;
        junit.framework.TestCase.assertEquals("1.5", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testFractionOneParameter() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.fractionOneParameter;
        junit.framework.TestCase.assertEquals("3.0", view.getText().toString());
    }

    @android.test.UiThreadTest
    public void testFractionTwoParameters() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.fractionTwoParameters;
        junit.framework.TestCase.assertEquals("9.0", view.getText().toString());
    }
}

