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


public class ExpressionTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ExpressionTestBinding> {
    public ExpressionTest() {
        super(android.databinding.testapp.databinding.ExpressionTestBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder();
    }

    @android.test.UiThreadTest
    public void testOr() throws java.lang.Throwable {
        // var1 == 0 || var2 == 0 ? "hello" : "world"
        mBinder.setVar1(0);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("hello", mBinder.textView0.getText().toString());
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("hello", mBinder.textView0.getText().toString());
        mBinder.setVar1(1);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("hello", mBinder.textView0.getText().toString());
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("world", mBinder.textView0.getText().toString());
    }

    @android.test.UiThreadTest
    public void testAnd() throws java.lang.Throwable {
        // var1 == 0 && var2 == 0 ? "hello" : "world"
        mBinder.setVar1(0);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("hello", mBinder.textView1.getText().toString());
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("world", mBinder.textView1.getText().toString());
        mBinder.setVar1(1);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("world", mBinder.textView1.getText().toString());
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("world", mBinder.textView1.getText().toString());
    }

    @android.test.UiThreadTest
    public void testBinary() throws java.lang.Throwable {
        mBinder.setVar1(0);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("0", mBinder.textView2.getText().toString());// var1 & var2

        junit.framework.TestCase.assertEquals("0", mBinder.textView3.getText().toString());// var1 | var2

        junit.framework.TestCase.assertEquals("0", mBinder.textView4.getText().toString());// var1 ^ var2

        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("0", mBinder.textView2.getText().toString());// var1 & var2

        junit.framework.TestCase.assertEquals("1", mBinder.textView3.getText().toString());// var1 | var2

        junit.framework.TestCase.assertEquals("1", mBinder.textView4.getText().toString());// var1 ^ var2

        mBinder.setVar1(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1", mBinder.textView2.getText().toString());// var1 & var2

        junit.framework.TestCase.assertEquals("1", mBinder.textView3.getText().toString());// var1 | var2

        junit.framework.TestCase.assertEquals("0", mBinder.textView4.getText().toString());// var1 ^ var2

    }

    @android.test.UiThreadTest
    public void testComparison() throws java.lang.Throwable {
        mBinder.setVar1(0);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("false", mBinder.textView5.getText().toString());// <

        junit.framework.TestCase.assertEquals("false", mBinder.textView6.getText().toString());// >

        junit.framework.TestCase.assertEquals("true", mBinder.textView7.getText().toString());// <=

        junit.framework.TestCase.assertEquals("true", mBinder.textView8.getText().toString());// >=

        junit.framework.TestCase.assertEquals("true", mBinder.textView9.getText().toString());// ==

        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("true", mBinder.textView5.getText().toString());// <

        junit.framework.TestCase.assertEquals("false", mBinder.textView6.getText().toString());// >

        junit.framework.TestCase.assertEquals("true", mBinder.textView7.getText().toString());// <=

        junit.framework.TestCase.assertEquals("false", mBinder.textView8.getText().toString());// >=

        junit.framework.TestCase.assertEquals("false", mBinder.textView9.getText().toString());// ==

        mBinder.setVar1(1);
        mBinder.setVar2(0);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("false", mBinder.textView5.getText().toString());// <

        junit.framework.TestCase.assertEquals("true", mBinder.textView6.getText().toString());// >

        junit.framework.TestCase.assertEquals("false", mBinder.textView7.getText().toString());// <=

        junit.framework.TestCase.assertEquals("true", mBinder.textView8.getText().toString());// >=

        junit.framework.TestCase.assertEquals("false", mBinder.textView9.getText().toString());// ==

    }

    @android.test.UiThreadTest
    public void testShift() throws java.lang.Throwable {
        mBinder.setVar1(-2);
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        int var1 = -2;
        int var2 = 1;
        junit.framework.TestCase.assertEquals(java.lang.String.valueOf(var1 << var2), mBinder.textView10.getText().toString());
        junit.framework.TestCase.assertEquals(java.lang.String.valueOf(var1 >> var2), mBinder.textView11.getText().toString());
        junit.framework.TestCase.assertEquals(java.lang.String.valueOf(var1 >>> var2), mBinder.textView12.getText().toString());
    }

    @android.test.UiThreadTest
    public void testUnary() throws java.lang.Throwable {
        mBinder.setVar1(2);
        mBinder.setVar2(1);
        mBinder.executePendingBindings();
        int var1 = 2;
        int var2 = 1;
        junit.framework.TestCase.assertEquals("1", mBinder.textView13.getText().toString());// 2 + -1

        junit.framework.TestCase.assertEquals(java.lang.String.valueOf(var1 + (~var2)), mBinder.textView14.getText().toString());// 2 + ~1

    }

    @android.test.UiThreadTest
    public void testInstanceOf() throws java.lang.Throwable {
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("true", mBinder.textView15.getText().toString());
        junit.framework.TestCase.assertEquals("true", mBinder.textView16.getText().toString());
        junit.framework.TestCase.assertEquals("false", mBinder.textView17.getText().toString());
    }

    @android.test.UiThreadTest
    public void testTernaryChain() throws java.lang.Throwable {
        mBinder.setBool1(true);
        mBinder.setBool2(false);
        mBinder.executePendingBindings();
        java.lang.String appName = getActivity().getResources().getString(R.string.app_name);
        java.lang.String rain = getActivity().getResources().getString(R.string.rain);
        junit.framework.TestCase.assertEquals(mBinder.getBool1() ? appName : mBinder.getBool2() ? rain : "", mBinder.textView18.getText().toString());
    }

    @android.test.UiThreadTest
    public void testBoundTag() throws java.lang.Throwable {
        mBinder.setBool1(false);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("bar", mBinder.textView19.getTag());
        mBinder.setBool1(true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("foo", mBinder.textView19.getTag());
    }

    @android.test.UiThreadTest
    public void testConstantExpression() throws java.lang.Throwable {
        mBinder.setVar1(1000);
        mBinder.setVar2(2000);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1000", mBinder.textView20.getText().toString());
        junit.framework.TestCase.assertEquals("2000", mBinder.textView21.getText().toString());
    }
}

