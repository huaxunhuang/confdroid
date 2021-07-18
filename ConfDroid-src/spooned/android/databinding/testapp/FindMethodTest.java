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


public class FindMethodTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.FindMethodTestBinding, android.databinding.testapp.vo.FindMethodBindingObject> {
    public FindMethodTest() {
        super(android.databinding.testapp.databinding.FindMethodTestBinding.class, android.databinding.testapp.vo.FindMethodBindingObject.class, R.layout.find_method_test);
    }

    public void testNoArg() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView6;
        junit.framework.TestCase.assertEquals("no arg", textView.getText().toString());
    }

    public void testIntArg() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView0;
        junit.framework.TestCase.assertEquals("1", textView.getText().toString());
    }

    public void testFloatArg() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView1;
        junit.framework.TestCase.assertEquals("1.25", textView.getText().toString());
    }

    public void testStringArg() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView2;
        junit.framework.TestCase.assertEquals("hello", textView.getText().toString());
    }

    public void testBoxedArg() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView3;
        junit.framework.TestCase.assertEquals("1", textView.getText().toString());
    }

    public void testInheritedMethod() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView4;
        junit.framework.TestCase.assertEquals("base", textView.getText().toString());
    }

    public void testInheritedMethodInt() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView5;
        junit.framework.TestCase.assertEquals("base 2", textView.getText().toString());
    }

    public void testStaticMethod() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView7;
        junit.framework.TestCase.assertEquals("world", textView.getText().toString());
    }

    public void testStaticField() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView8;
        junit.framework.TestCase.assertEquals("hello world", textView.getText().toString());
    }

    public void testImportStaticMethod() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView9;
        junit.framework.TestCase.assertEquals("world", textView.getText().toString());
    }

    public void testImportStaticField() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView10;
        junit.framework.TestCase.assertEquals("hello world", textView.getText().toString());
    }

    public void testAliasStaticMethod() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView11;
        junit.framework.TestCase.assertEquals("world", textView.getText().toString());
    }

    public void testAliasStaticField() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textView12;
        junit.framework.TestCase.assertEquals("hello world", textView.getText().toString());
    }

    @android.test.UiThreadTest
    public void testObservableField() throws java.lang.Throwable {
        // tests an ObservableField inside an Observable object
        junit.framework.TestCase.assertEquals("", mBinder.textView25.getText().toString());
        mBinder.getObj().myField.set("Hello World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World", mBinder.textView25.getText().toString());
        mBinder.getObj().myField.set("World Hello");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("World Hello", mBinder.textView25.getText().toString());
    }

    @android.test.UiThreadTest
    public void testObservableInstanceField() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals("", mBinder.textView26.getText().toString());
        mBinder.getObj().observableClass.setX("foobar");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("foobar", mBinder.textView26.getText().toString());
        mBinder.getObj().observableClass.setX("barfoo");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("barfoo", mBinder.textView26.getText().toString());
    }

    @android.test.UiThreadTest
    public void testPrimitiveToObject() throws java.lang.Throwable {
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertTrue(mBinder.textView27.getTag() instanceof java.lang.Integer);
        junit.framework.TestCase.assertEquals(((java.lang.Integer) (1)), mBinder.textView27.getTag());
    }
}

