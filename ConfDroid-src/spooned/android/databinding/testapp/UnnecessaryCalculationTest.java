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


public class UnnecessaryCalculationTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.UnnecessaryCalculationBinding> {
    public UnnecessaryCalculationTest() {
        super(android.databinding.testapp.databinding.UnnecessaryCalculationBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder();
    }

    @android.test.UiThreadTest
    public void testDontSetUnnecessaryFlags() {
        android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter obja = new android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter();
        android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter objb = new android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter();
        android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter objc = new android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter();
        mBinder.setObja(obja);
        mBinder.setObjb(objb);
        mBinder.setObjc(objc);
        mBinder.setA(true);
        mBinder.setB(true);
        mBinder.setC(false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("true", mBinder.textView.getText().toString());
        junit.framework.TestCase.assertEquals("true", mBinder.textView2.getText().toString());
        junit.framework.TestCase.assertEquals(1, obja.counter);
        junit.framework.TestCase.assertEquals(1, objb.counter);
        junit.framework.TestCase.assertEquals(0, objc.counter);
        obja = new android.databinding.testapp.UnnecessaryCalculationTest.BasicObjWithCounter();
        mBinder.setObja(obja);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("true", mBinder.textView.getText().toString());
        junit.framework.TestCase.assertEquals("true", mBinder.textView2.getText().toString());
        junit.framework.TestCase.assertEquals(1, obja.counter);
        junit.framework.TestCase.assertEquals(1, objb.counter);
        junit.framework.TestCase.assertEquals(0, objc.counter);
    }

    private static class BasicObjWithCounter extends android.databinding.testapp.vo.BasicObject {
        int counter = 0;

        @java.lang.Override
        public java.lang.String boolMethod(boolean value) {
            counter++;
            return super.boolMethod(value);
        }
    }
}

