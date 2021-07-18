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


public class NameMappingTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.NameMappingTestBinding> {
    public NameMappingTest() {
        super(android.databinding.testapp.databinding.NameMappingTestBinding.class);
    }

    @android.test.UiThreadTest
    public void testChanges() {
        initBinder();
        final java.util.concurrent.atomic.AtomicBoolean f1 = new java.util.concurrent.atomic.AtomicBoolean(false);
        final java.util.concurrent.atomic.AtomicBoolean f2 = new java.util.concurrent.atomic.AtomicBoolean(false);
        android.databinding.testapp.vo.BasicObject object = new android.databinding.testapp.vo.BasicObject() {
            @java.lang.Override
            public boolean isThisNameDoesNotMatchAnythingElse1() {
                return f1.get();
            }

            @java.lang.Override
            public boolean getThisNameDoesNotMatchAnythingElse2() {
                return f2.get();
            }
        };
        mBinder.setObj(object);
        mBinder.executePendingBindings();
        for (int i = 0; i < 5; i++) {
            boolean f1New = (i & 1) != 0;
            boolean f2New = (i & (1 << 1)) != 0;
            if (f1New != f1.get()) {
                f1.set(f1New);
                object.notifyPropertyChanged(BR.thisNameDoesNotMatchAnythingElse1);
            }
            if (f2New != f2.get()) {
                f1.set(f2New);
                object.notifyPropertyChanged(BR.thisNameDoesNotMatchAnythingElse2);
            }
            mBinder.executePendingBindings();
            junit.framework.TestCase.assertEquals(f2.get(), mBinder.textView.isEnabled());
            junit.framework.TestCase.assertEquals(f2.get(), mBinder.textView2.isEnabled());
            junit.framework.TestCase.assertEquals(false, mBinder.textView3.isEnabled());
            junit.framework.TestCase.assertEquals(f1.get(), mBinder.textView.isFocusable());
            junit.framework.TestCase.assertEquals(f1.get(), mBinder.textView2.isFocusable());
            junit.framework.TestCase.assertEquals(false, mBinder.textView3.isFocusable());
        }
    }
}

