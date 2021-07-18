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


@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.RadioGroup.class, attribute = "android:checkedButton", method = "getCheckedRadioButtonId") })
public class RadioGroupBindingAdapter {
    @android.databinding.BindingAdapter("android:checkedButton")
    public static void setCheckedButton(android.widget.RadioGroup view, int id) {
        if (id != view.getCheckedRadioButtonId()) {
            view.check(id);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onCheckedChanged", "android:checkedButtonAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.RadioGroup view, final android.widget.RadioGroup.OnCheckedChangeListener listener, final android.databinding.InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener);
        } else {
            view.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
                @java.lang.Override
                public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                    if (listener != null) {
                        listener.onCheckedChanged(group, checkedId);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}

