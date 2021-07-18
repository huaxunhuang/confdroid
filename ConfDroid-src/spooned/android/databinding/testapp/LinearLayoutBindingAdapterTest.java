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


public class LinearLayoutBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.LinearLayoutAdapterTestBinding, android.databinding.testapp.vo.LinearLayoutBindingObject> {
    android.widget.LinearLayout mView;

    public LinearLayoutBindingAdapterTest() {
        super(android.databinding.testapp.databinding.LinearLayoutAdapterTestBinding.class, android.databinding.testapp.vo.LinearLayoutBindingObject.class, R.layout.linear_layout_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testMeasureWithLargestChild() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.isMeasureWithLargestChild(), mView.isMeasureWithLargestChildEnabled());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.isMeasureWithLargestChild(), mView.isMeasureWithLargestChildEnabled());
        }
    }
}

