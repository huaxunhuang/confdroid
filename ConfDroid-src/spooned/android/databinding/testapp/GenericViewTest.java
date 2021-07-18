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


public class GenericViewTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.GenericViewBinding> {
    public GenericViewTest() {
        super(android.databinding.testapp.databinding.GenericViewBinding.class);
    }

    @android.test.UiThreadTest
    public void testCast() throws java.lang.Throwable {
        initBinder();
        // There used to be a compile error when the View was generic
        junit.framework.TestCase.assertNotNull(getBinder().view);
    }
}

