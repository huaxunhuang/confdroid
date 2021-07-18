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
package android.databinding.adapters;


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.support.v7.widget.SwitchCompat.class, attribute = "android:thumb", method = "setThumbDrawable"), @android.databinding.BindingMethod(type = android.support.v7.widget.SwitchCompat.class, attribute = "android:track", method = "setTrackDrawable") })
public class SwitchCompatBindingAdapter {
    @android.databinding.BindingAdapter({ "android:switchTextAppearance" })
    public static void setSwitchTextAppearance(android.support.v7.widget.SwitchCompat view, int value) {
        view.setSwitchTextAppearance(null, value);
    }
}

