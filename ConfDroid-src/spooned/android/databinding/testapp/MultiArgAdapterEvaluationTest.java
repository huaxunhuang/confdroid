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


public class MultiArgAdapterEvaluationTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.MultiArgAdapterEvaluationTestBinding> {
    public MultiArgAdapterEvaluationTest() {
        super(android.databinding.testapp.databinding.MultiArgAdapterEvaluationTestBinding.class);
    }

    @android.test.UiThreadTest
    public void testMultiArgIsCalled() {
        initBinder();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 obj1 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 obj2 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2();
        obj1.setValue("a", false);
        obj2.setValue("b", false);
        mBinder.setObj1(obj1);
        mBinder.setObj2(obj2);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(mBinder.merged.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj1.getValue(), obj2.getValue()));
        junit.framework.TestCase.assertEquals(mBinder.view2.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj2.getValue()));
        junit.framework.TestCase.assertEquals(mBinder.view2text.getText().toString(), obj2.getValue());
        java.lang.String prev2 = mBinder.view2.getText().toString();
        java.lang.String prevValue = mBinder.merged.getText().toString();
        obj1.setValue("o", false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(prevValue, mBinder.merged.getText().toString());
        obj2.setValue("p", false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(prevValue, mBinder.merged.getText().toString());
        // now invalidate obj1 only, obj2 should be evaluated as well
        obj1.setValue("o2", true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj1, obj2), mBinder.merged.getText().toString());
        junit.framework.TestCase.assertEquals("obj2 should not be re-evaluated", prev2, mBinder.view2.getText().toString());
        junit.framework.TestCase.assertEquals("obj2 should not be re-evaluated", prev2, mBinder.view2text.getText().toString());
    }
}

