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


public class ListenerTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ListenersBinding> {
    private android.databinding.testapp.vo.ListenerBindingObject mBindingObject;

    public ListenerTest() {
        super(android.databinding.testapp.databinding.ListenersBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        mBindingObject = new android.databinding.testapp.vo.ListenerBindingObject(getActivity());
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setObj(mBindingObject);
            }
        });
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 0;
    }

    @android.test.UiThreadTest
    public void testInstanceClick() throws java.lang.Throwable {
        android.view.View view = mBinder.click1;
        junit.framework.TestCase.assertEquals(0, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(1, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
    }

    @android.test.UiThreadTest
    public void testStaticClick() throws java.lang.Throwable {
        android.view.View view = mBinder.click2;
        junit.framework.TestCase.assertEquals(0, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(2, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
    }

    @android.test.UiThreadTest
    public void testInstanceClickTwoArgs() throws java.lang.Throwable {
        android.view.View view = mBinder.click3;
        junit.framework.TestCase.assertEquals(0, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(3, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        junit.framework.TestCase.assertTrue(view.isClickable());
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 0;
        mBindingObject.clickable.set(false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(view.isClickable());
        mBindingObject.useOne.set(true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(view.isClickable());
        mBindingObject.clickable.set(true);
        mBinder.executePendingBindings();
        view.callOnClick();
        junit.framework.TestCase.assertEquals(1, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
    }

    @android.test.UiThreadTest
    public void testStaticClickTwoArgs() throws java.lang.Throwable {
        android.view.View view = mBinder.click4;
        junit.framework.TestCase.assertEquals(0, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(4, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        junit.framework.TestCase.assertTrue(view.isClickable());
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 0;
        mBindingObject.clickable.set(false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(view.isClickable());
        view.setClickable(true);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(4, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
    }

    @android.test.UiThreadTest
    public void testClickExpression() throws java.lang.Throwable {
        android.view.View view = mBinder.click5;
        junit.framework.TestCase.assertEquals(0, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        view.callOnClick();
        junit.framework.TestCase.assertEquals(2, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
        android.databinding.testapp.vo.ListenerBindingObject.lastClick = 0;
        mBindingObject.useOne.set(true);
        mBinder.executePendingBindings();
        view.callOnClick();
        junit.framework.TestCase.assertEquals(1, android.databinding.testapp.vo.ListenerBindingObject.lastClick);
    }

    @android.test.UiThreadTest
    public void testInflateListener() throws java.lang.Throwable {
        android.databinding.ViewStubProxy viewStubProxy = mBinder.viewStub;
        junit.framework.TestCase.assertFalse(viewStubProxy.isInflated());
        junit.framework.TestCase.assertFalse(mBindingObject.inflateCalled);
        viewStubProxy.getViewStub().inflate();
        junit.framework.TestCase.assertTrue(mBindingObject.inflateCalled);
        junit.framework.TestCase.assertTrue(viewStubProxy.isInflated());
    }

    @android.test.UiThreadTest
    public void testBaseObservableClick() throws java.lang.Throwable {
        android.view.View view = mBinder.click6;
        android.databinding.testapp.vo.ListenerBindingObject.Inner inner = new android.databinding.testapp.vo.ListenerBindingObject.Inner();
        mBinder.setObj2(inner);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(inner.clicked);
        view.callOnClick();
        junit.framework.TestCase.assertTrue(inner.clicked);
    }
}

