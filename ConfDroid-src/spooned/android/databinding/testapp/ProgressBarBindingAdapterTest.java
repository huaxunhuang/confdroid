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


public class ProgressBarBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.ProgressBarAdapterTestBinding, android.databinding.testapp.vo.ProgressBarBindingObject> {
    android.widget.ProgressBar mView;

    public ProgressBarBindingAdapterTest() {
        super(android.databinding.testapp.databinding.ProgressBarAdapterTestBinding.class, android.databinding.testapp.vo.ProgressBarBindingObject.class, R.layout.progress_bar_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testTint() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            junit.framework.TestCase.assertEquals(mBindingObject.getIndeterminateTint(), mView.getIndeterminateTintList().getDefaultColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getProgressTint(), mView.getProgressTintList().getDefaultColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getSecondaryProgressTint(), mView.getSecondaryProgressTintList().getDefaultColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getIndeterminateTint(), mView.getIndeterminateTintList().getDefaultColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getProgressTint(), mView.getProgressTintList().getDefaultColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getSecondaryProgressTint(), mView.getSecondaryProgressTintList().getDefaultColor());
        }
    }
}

