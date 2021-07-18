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


import android.databinding.testapp.databinding.ViewStubContentsBinding;

import static contentsBinding.firstNameContents.getText;


public class ViewStubTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ViewStubBinding> {
    public ViewStubTest() {
        super(android.databinding.testapp.databinding.ViewStubBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setViewStubVisibility(android.view.View.GONE);
                mBinder.setFirstName("Hello");
                mBinder.setLastName("World");
                mBinder.executePendingBindings();
            }
        });
    }

    @android.test.UiThreadTest
    public void testInflation() throws java.lang.Throwable {
        android.databinding.ViewStubProxy viewStubProxy = mBinder.viewStub;
        junit.framework.TestCase.assertFalse(viewStubProxy.isInflated());
        junit.framework.TestCase.assertNull(viewStubProxy.getBinding());
        junit.framework.TestCase.assertNotNull(viewStubProxy.getViewStub());
        junit.framework.TestCase.assertNull(mBinder.getRoot().findViewById(R.id.firstNameContents));
        junit.framework.TestCase.assertNull(mBinder.getRoot().findViewById(R.id.lastNameContents));
        mBinder.setViewStubVisibility(android.view.View.VISIBLE);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertTrue(viewStubProxy.isInflated());
        junit.framework.TestCase.assertNotNull(viewStubProxy.getBinding());
        junit.framework.TestCase.assertNull(viewStubProxy.getViewStub());
        ViewStubContentsBinding contentsBinding = ((ViewStubContentsBinding) (viewStubProxy.getBinding()));
        junit.framework.TestCase.assertNotNull(contentsBinding.firstNameContents);
        junit.framework.TestCase.assertNotNull(contentsBinding.lastNameContents);
        junit.framework.TestCase.assertEquals("Hello", getText().toString());
        junit.framework.TestCase.assertEquals("World", contentsBinding.lastNameContents.getText().toString());
    }

    @android.test.UiThreadTest
    public void testChangeValues() throws java.lang.Throwable {
        android.databinding.ViewStubProxy viewStubProxy = mBinder.viewStub;
        mBinder.setViewStubVisibility(android.view.View.VISIBLE);
        mBinder.executePendingBindings();
        ViewStubContentsBinding contentsBinding = ((ViewStubContentsBinding) (viewStubProxy.getBinding()));
        junit.framework.TestCase.assertEquals("Hello", getText().toString());
        mBinder.setFirstName("Goodbye");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Goodbye", getText().toString());
    }
}

