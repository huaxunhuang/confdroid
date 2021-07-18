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


public class AbsSeekBarBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.AbsSeekBarAdapterTestBinding, android.databinding.testapp.vo.AbsSeekBarBindingObject> {
    android.widget.SeekBar mView;

    public AbsSeekBarBindingAdapterTest() {
        super(android.databinding.testapp.databinding.AbsSeekBarAdapterTestBinding.class, android.databinding.testapp.vo.AbsSeekBarBindingObject.class, R.layout.abs_seek_bar_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testThumbTint() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            junit.framework.TestCase.assertEquals(mBindingObject.getThumbTint(), mView.getThumbTintList().getDefaultColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getThumbTint(), mView.getThumbTintList().getDefaultColor());
        }
    }
}

