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


public class ViewStubBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.ViewStubAdapterTestBinding, android.databinding.testapp.vo.ViewStubBindingObject> {
    android.view.ViewStub mView;

    public ViewStubBindingAdapterTest() {
        super(android.databinding.testapp.databinding.ViewStubAdapterTestBinding.class, android.databinding.testapp.vo.ViewStubBindingObject.class, R.layout.view_stub_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view.getViewStub();
    }

    public void testLayout() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.getLayout(), mView.getLayoutResource());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getLayout(), mView.getLayoutResource());
    }
}

