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


public class FrameLayoutBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.FrameLayoutAdapterTestBinding, android.databinding.testapp.vo.FrameLayoutBindingObject> {
    android.widget.FrameLayout mView;

    public FrameLayoutBindingAdapterTest() {
        super(android.databinding.testapp.databinding.FrameLayoutAdapterTestBinding.class, android.databinding.testapp.vo.FrameLayoutBindingObject.class, R.layout.frame_layout_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    @android.test.UiThreadTest
    public void testTint() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            junit.framework.TestCase.assertEquals(mBindingObject.getForegroundTint(), mView.getForegroundTintList().getDefaultColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getForegroundTint(), mView.getForegroundTintList().getDefaultColor());
        }
    }
}

