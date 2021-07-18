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
package android.databinding.testapp.adapter;


public class CustomNamespaceAdapter {
    @android.databinding.BindingAdapter({ "android:set1" })
    public static void setOne(android.widget.TextView view, java.lang.String value) {
        view.setText(value);
    }

    @android.databinding.BindingAdapter({ "bind:set2" })
    public static void setTwo(android.widget.TextView view, java.lang.String value) {
        view.setText(value);
    }
}

