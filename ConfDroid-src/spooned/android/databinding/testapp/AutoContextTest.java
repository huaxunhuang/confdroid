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


import static binding.textView1.getText;


public class AutoContextTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.AutoContextBinding> {
    public AutoContextTest() {
        super(android.databinding.testapp.databinding.AutoContextBinding.class);
    }

    @android.test.UiThreadTest
    public void testContext() throws java.lang.Throwable {
        android.databinding.testapp.databinding.AutoContextBinding binding = initBinder();
        binding.executePendingBindings();
        junit.framework.TestCase.assertNotSame("", getText().toString());
    }
}

