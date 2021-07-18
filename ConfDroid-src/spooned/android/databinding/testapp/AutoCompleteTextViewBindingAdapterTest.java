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


public class AutoCompleteTextViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.AutoCompleteTextViewAdapterTestBinding, android.databinding.testapp.vo.AutoCompleteTextViewBindingObject> {
    android.widget.AutoCompleteTextView mView;

    public AutoCompleteTextViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.AutoCompleteTextViewAdapterTestBinding.class, android.databinding.testapp.vo.AutoCompleteTextViewBindingObject.class, R.layout.auto_complete_text_view_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testCompletionThreshold() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.getCompletionThreshold(), mView.getThreshold());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getCompletionThreshold(), mView.getThreshold());
    }

    public void testPopupBackground() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            junit.framework.TestCase.assertEquals(mBindingObject.getPopupBackground(), ((android.graphics.drawable.ColorDrawable) (mView.getDropDownBackground())).getColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getPopupBackground(), ((android.graphics.drawable.ColorDrawable) (mView.getDropDownBackground())).getColor());
        }
    }
}

