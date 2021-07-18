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


public class NoVariableIncludeTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.IncludeNoVariablesBinding> {
    public NoVariableIncludeTest() {
        super(android.databinding.testapp.databinding.IncludeNoVariablesBinding.class);
    }

    @android.test.UiThreadTest
    public void testInclude() {
        initBinder();
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertNotNull(mBinder.included);
        junit.framework.TestCase.assertNotNull(mBinder.included.textView);
        java.lang.String expectedValue = getActivity().getResources().getString(R.string.app_name);
        junit.framework.TestCase.assertEquals(expectedValue, mBinder.included.textView.getText().toString());
        android.widget.TextView noIdInclude = ((android.widget.TextView) (((android.view.ViewGroup) (mBinder.getRoot())).getChildAt(1)));
        junit.framework.TestCase.assertEquals(expectedValue, noIdInclude.getText().toString());
    }
}

