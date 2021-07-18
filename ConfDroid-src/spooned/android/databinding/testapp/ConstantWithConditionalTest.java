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


public class ConstantWithConditionalTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ConstantBindingWithConditionalBinding> {
    public ConstantWithConditionalTest() {
        super(android.databinding.testapp.databinding.ConstantBindingWithConditionalBinding.class);
    }

    @android.test.UiThreadTest
    public void testValues() {
        initBinder();
        mBinder.executePendingBindings();
        android.databinding.testapp.vo.BasicObject basicObject = new android.databinding.testapp.vo.BasicObject();
        basicObject.setField1("tt");
        basicObject.setField2("blah");
        android.databinding.testapp.vo.ConstantBindingTestObject obj = new android.databinding.testapp.vo.ConstantBindingTestObject();
        mBinder.setVm(obj);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertTrue(mBinder.myTextView.hasFixedSize());
        junit.framework.TestCase.assertTrue(mBinder.progressBar.isIndeterminate());
        obj.setErrorMessage("blah");
        mBinder.invalidateAll();
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(mBinder.progressBar.isIndeterminate());
        obj.setErrorMessage(null);
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        obj.setCountryModels(list);
        mBinder.invalidateAll();
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertTrue(mBinder.progressBar.isIndeterminate());
        list.add("abc");
        mBinder.invalidateAll();
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertFalse(mBinder.progressBar.isIndeterminate());
    }
}

