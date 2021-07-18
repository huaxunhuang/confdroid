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
package android.databinding.test.independentlibrary;


public class LibraryActivity extends android.app.Activity {
    public static final java.lang.String FIELD_VALUE = "BAR";

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.databinding.test.independentlibrary.IndependentLibraryBinding binding = android.databinding.test.independentlibrary.IndependentLibraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        android.databinding.test.independentlibrary.vo.MyBindableObject object = new android.databinding.test.independentlibrary.vo.MyBindableObject();
        object.setField(android.databinding.test.independentlibrary.LibraryActivity.FIELD_VALUE);
        binding.setFoo(object);
        binding.executePendingBindings();
    }
}

