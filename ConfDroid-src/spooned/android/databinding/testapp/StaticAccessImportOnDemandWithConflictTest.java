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


public class StaticAccessImportOnDemandWithConflictTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.StaticAccessImportOnDemandWithConflictBinding> {
    public StaticAccessImportOnDemandWithConflictTest() {
        super(android.databinding.testapp.databinding.StaticAccessImportOnDemandWithConflictBinding.class);
    }

    @android.test.UiThreadTest
    public void testAccessStatics() {
        initBinder();
        android.databinding.testapp.vo.StaticTestsVo vo = new android.databinding.testapp.vo.StaticTestsVo();
        mBinder.setVo(vo);
        assertStaticContents();
    }

    private void assertStaticContents() {
        mBinder.executePendingBindings();
        assertText(android.databinding.testapp.vo.StaticTestsVo.ourStaticField, mBinder.staticFieldOverVo);
        assertText(android.databinding.testapp.vo.StaticTestsVo.ourStaticMethod(), mBinder.staticMethodOverVo);
        assertText(android.databinding.testapp.vo.StaticTestsVo.ourStaticObservable.get(), mBinder.obsStaticOverVo);
        java.lang.String newValue = java.util.UUID.randomUUID().toString();
        android.databinding.testapp.vo.StaticTestsVo.ourStaticObservable.set(newValue);
        mBinder.executePendingBindings();
        assertText(android.databinding.testapp.vo.StaticTestsVo.ourStaticObservable.get(), mBinder.obsStaticOverVo);
    }

    @android.test.UiThreadTest
    public void testAccessStaticsVoInstance() {
        initBinder();
        mBinder.setVo(null);
        assertStaticContents();
    }

    private void assertText(java.lang.String contents, android.widget.TextView textView) {
        junit.framework.TestCase.assertEquals(contents, textView.getText().toString());
    }
}

