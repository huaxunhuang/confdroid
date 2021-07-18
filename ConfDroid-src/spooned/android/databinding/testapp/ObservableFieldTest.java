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


public class ObservableFieldTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ObservableFieldTestBinding> {
    private android.databinding.testapp.vo.ObservableFieldBindingObject mObj;

    public ObservableFieldTest() {
        super(android.databinding.testapp.databinding.ObservableFieldTestBinding.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mObj = new android.databinding.testapp.vo.ObservableFieldBindingObject();
                mBinder.setObj(mObj);
                mBinder.executePendingBindings();
            }
        });
    }

    @android.test.UiThreadTest
    public void testBoolean() {
        android.widget.TextView view = mBinder.bField;
        junit.framework.TestCase.assertEquals("false", view.getText());
        mObj.bField.set(true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("true", view.getText());
    }

    @android.test.UiThreadTest
    public void testByte() {
        android.widget.TextView view = mBinder.tField;
        junit.framework.TestCase.assertEquals("0", view.getText());
        mObj.tField.set(((byte) (1)));
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1", view.getText());
    }

    @android.test.UiThreadTest
    public void testShort() {
        android.widget.TextView view = mBinder.sField;
        junit.framework.TestCase.assertEquals("0", view.getText());
        mObj.sField.set(((short) (1)));
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1", view.getText());
    }

    @android.test.UiThreadTest
    public void testChar() {
        android.widget.TextView view = mBinder.cField;
        junit.framework.TestCase.assertEquals("\u0000", view.getText());
        mObj.cField.set('A');
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("A", view.getText());
    }

    @android.test.UiThreadTest
    public void testInt() {
        android.widget.TextView view = mBinder.iField;
        junit.framework.TestCase.assertEquals("0", view.getText());
        mObj.iField.set(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1", view.getText());
    }

    @android.test.UiThreadTest
    public void testLong() {
        android.widget.TextView view = mBinder.lField;
        junit.framework.TestCase.assertEquals("0", view.getText());
        mObj.lField.set(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1", view.getText());
    }

    @android.test.UiThreadTest
    public void testFloat() {
        android.widget.TextView view = mBinder.fField;
        junit.framework.TestCase.assertEquals("0.0", view.getText());
        mObj.fField.set(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1.0", view.getText());
    }

    @android.test.UiThreadTest
    public void testDouble() {
        android.widget.TextView view = mBinder.dField;
        junit.framework.TestCase.assertEquals("0.0", view.getText());
        mObj.dField.set(1);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("1.0", view.getText());
    }

    @android.test.UiThreadTest
    public void testObject() {
        android.widget.TextView view = mBinder.oField;
        junit.framework.TestCase.assertEquals("Hello", view.getText());
        mObj.oField.set("World");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("World", view.getText());
    }

    @android.test.UiThreadTest
    public void testParcelable() {
        android.widget.TextView x = mBinder.pFieldx;
        android.widget.TextView y = mBinder.pFieldy;
        junit.framework.TestCase.assertEquals(x.getText().toString(), java.lang.String.valueOf(mObj.pField.get().getX()));
        junit.framework.TestCase.assertEquals(y.getText().toString(), mObj.pField.get().getY());
        android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable p2 = new android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable(7, "updated");
        mObj.pField.set(p2);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals(x.getText().toString(), java.lang.String.valueOf(mObj.pField.get().getX()));
        junit.framework.TestCase.assertEquals(y.getText().toString(), mObj.pField.get().getY());
    }
}

