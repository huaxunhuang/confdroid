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


public class BindToFinalFieldTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BindToFinalBinding> {
    public BindToFinalFieldTest() {
        super(android.databinding.testapp.databinding.BindToFinalBinding.class);
    }

    @android.test.UiThreadTest
    public void testSimple() {
        initBinder();
        final android.databinding.testapp.vo.PublicFinalTestVo vo = new android.databinding.testapp.vo.PublicFinalTestVo(R.string.app_name);
        mBinder.setObj(vo);
        mBinder.executePendingBindings();
        final android.widget.TextView textView = ((android.widget.TextView) (mBinder.getRoot().findViewById(R.id.text_view)));
        junit.framework.TestCase.assertEquals(getActivity().getResources().getString(R.string.app_name), textView.getText().toString());
    }
}

