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


@android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
public class TestFragment extends android.app.Fragment {
    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        android.databinding.testapp.databinding.BasicBindingBinding binding = android.databinding.testapp.databinding.BasicBindingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}

