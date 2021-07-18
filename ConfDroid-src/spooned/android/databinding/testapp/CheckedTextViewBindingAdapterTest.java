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


public class CheckedTextViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.CheckedTextViewAdapterTestBinding, android.databinding.testapp.vo.CheckedTextViewBindingObject> {
    public CheckedTextViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.CheckedTextViewAdapterTestBinding.class, android.databinding.testapp.vo.CheckedTextViewBindingObject.class, R.layout.checked_text_view_adapter_test);
    }

    public void testView() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            junit.framework.TestCase.assertEquals(mBindingObject.getCheckMark().getColor(), ((android.graphics.drawable.ColorDrawable) (mBinder.view.getCheckMarkDrawable())).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getCheckMarkTint(), mBinder.view.getCheckMarkTintList().getDefaultColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getCheckMark().getColor(), ((android.graphics.drawable.ColorDrawable) (mBinder.view.getCheckMarkDrawable())).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getCheckMarkTint(), mBinder.view.getCheckMarkTintList().getDefaultColor());
        }
    }
}

