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


public class GenericAdapterTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.GenericAdapterBinding> {
    public GenericAdapterTest() {
        super(android.databinding.testapp.databinding.GenericAdapterBinding.class);
    }

    @android.test.UiThreadTest
    public void testGenericArgs() throws java.lang.Throwable {
        initBinder();
        java.lang.String[] arr = new java.lang.String[]{ "Hello", "World" };
        java.util.List<java.lang.String> list = java.util.Arrays.asList(arr);
        getBinder().setList(list);
        getBinder().setArr(arr);
        getBinder().executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World", getBinder().textView1.getText().toString());
        junit.framework.TestCase.assertEquals("Hello World", getBinder().textView2.getText().toString());
        junit.framework.TestCase.assertEquals("Hello World", getBinder().textView3.getText().toString());
        junit.framework.TestCase.assertEquals("Hello World", getBinder().textView4.getText().toString());
        junit.framework.TestCase.assertEquals(list, getBinder().view5.getList());
        junit.framework.TestCase.assertEquals(list, getBinder().view6.getList());
        junit.framework.TestCase.assertEquals("Hello World", getBinder().textView7.getText().toString());
    }
}

