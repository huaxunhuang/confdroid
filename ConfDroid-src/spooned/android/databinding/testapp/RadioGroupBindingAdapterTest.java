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


public class RadioGroupBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.RadioGroupAdapterTestBinding, android.databinding.testapp.vo.RadioGroupBindingObject> {
    android.widget.RadioGroup mView;

    public RadioGroupBindingAdapterTest() {
        super(android.databinding.testapp.databinding.RadioGroupAdapterTestBinding.class, android.databinding.testapp.vo.RadioGroupBindingObject.class, R.layout.radio_group_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testRadioGroup() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.getCheckedButton(), mView.getCheckedRadioButtonId());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getCheckedButton(), mView.getCheckedRadioButtonId());
    }
}

