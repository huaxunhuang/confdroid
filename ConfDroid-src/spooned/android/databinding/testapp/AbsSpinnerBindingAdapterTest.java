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


public class AbsSpinnerBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.AbsSpinnerAdapterTestBinding, android.databinding.testapp.vo.AbsSpinnerBindingObject> {
    android.widget.Spinner mView;

    public AbsSpinnerBindingAdapterTest() {
        super(android.databinding.testapp.databinding.AbsSpinnerAdapterTestBinding.class, android.databinding.testapp.vo.AbsSpinnerBindingObject.class, R.layout.abs_spinner_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    @android.test.UiThreadTest
    public void testEntries() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            validateEntries();
            changeValues();
            validateEntries();
        }
    }

    @android.test.UiThreadTest
    public void testList() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            validateList();
            mBindingObject.getList().add(1, "Cruel");
            mBinder.executePendingBindings();
            validateList();
        }
    }

    private void validateEntries() {
        junit.framework.TestCase.assertEquals(mBindingObject.getEntries().length, mView.getAdapter().getCount());
        java.lang.CharSequence[] entries = mBindingObject.getEntries();
        android.widget.SpinnerAdapter adapter = mView.getAdapter();
        for (int i = 0; i < entries.length; i++) {
            junit.framework.TestCase.assertEquals(adapter.getItem(i), entries[i]);
        }
    }

    private void validateList() {
        java.util.List<java.lang.String> entries = mBindingObject.getList();
        android.widget.SpinnerAdapter adapter = mBinder.view2.getAdapter();
        junit.framework.TestCase.assertEquals(entries.size(), adapter.getCount());
        for (int i = 0; i < entries.size(); i++) {
            junit.framework.TestCase.assertEquals(adapter.getItem(i), entries.get(i));
        }
    }
}

