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


public class MultiArgAdapterTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.MultiArgAdapterTestBinding> {
    public MultiArgAdapterTest() {
        super(android.databinding.testapp.databinding.MultiArgAdapterTestBinding.class);
    }

    @android.test.UiThreadTest
    public void testMultiArgIsCalled() {
        initBinder();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 obj1 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 obj2 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 obj3 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 obj4 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2();
        obj1.setValue("a", false);
        obj2.setValue("b", false);
        obj3.setValue("c", false);
        obj4.setValue("d", false);
        mBinder.setObj1(obj1);
        mBinder.setObj2(obj2);
        mBinder.setObj3(obj3);
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(mBinder.merged.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj1, obj2));
        junit.framework.TestCase.assertEquals(mBinder.view2.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj2));
        junit.framework.TestCase.assertEquals(mBinder.view3.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj3));
        junit.framework.TestCase.assertEquals(mBinder.view4.getText().toString(), android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj4));
        java.lang.String prev2 = mBinder.view2.getText().toString();
        java.lang.String prevValue = mBinder.merged.getText().toString();
        obj1.setValue("o", false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(prevValue, mBinder.merged.getText().toString());
        obj2.setValue("p", false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(prevValue, mBinder.merged.getText().toString());
        // now invalidate obj1 only, obj2 should be evaluated as well
        obj1.notifyPropertyChanged(BR._all);
        java.lang.String prev3 = mBinder.view3.getText().toString();
        java.lang.String prev4 = mBinder.view4.getText().toString();
        obj3.setValue("q", false);
        obj4.setValue("r", false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(android.databinding.testapp.adapter.MultiArgTestAdapter.join(obj1, obj2), mBinder.merged.getText().toString());
        junit.framework.TestCase.assertEquals("obj2 should not be re-evaluated", prev2, mBinder.view2.getText().toString());
        // make sure 3 and 4 are not invalidated
        junit.framework.TestCase.assertEquals("obj3 should not be re-evaluated", prev3, mBinder.view3.getText().toString());
        junit.framework.TestCase.assertEquals("obj4 should not be re-evaluated", prev4, mBinder.view4.getText().toString());
    }

    @android.test.UiThreadTest
    public void testSetWithOldValues() throws java.lang.Throwable {
        initBinder();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 obj1 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 obj2 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 obj3 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1();
        android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 obj4 = new android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2();
        obj1.setValue("a", false);
        obj2.setValue("b", false);
        obj3.setValue("c", false);
        obj4.setValue("d", false);
        mBinder.setObj1(obj1);
        mBinder.setObj2(obj2);
        mBinder.setObj3(obj3);
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("null -> a", mBinder.view7.getText().toString());
        junit.framework.TestCase.assertEquals("null, null -> a, b", mBinder.view8.getText().toString());
        obj1.setValue("x", true);
        obj2.setValue("y", true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("a -> x", mBinder.view7.getText().toString());
        junit.framework.TestCase.assertEquals("a, b -> x, y", mBinder.view8.getText().toString());
        obj1.setValue("x", true);
        obj2.setValue("y", true);
        mBinder.executePendingBindings();
        // Calls setter
        junit.framework.TestCase.assertEquals("x -> x", mBinder.view7.getText().toString());
        junit.framework.TestCase.assertEquals("x, y -> x, y", mBinder.view8.getText().toString());
        obj2.setValue("z", true);
        mBinder.executePendingBindings();
        // only the two-arg value changed
        junit.framework.TestCase.assertEquals("x -> x", mBinder.view7.getText().toString());
        junit.framework.TestCase.assertEquals("x, y -> x, z", mBinder.view8.getText().toString());
    }
}

