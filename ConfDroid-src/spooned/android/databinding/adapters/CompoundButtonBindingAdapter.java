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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.CompoundButton.class, attribute = "android:buttonTint", method = "setButtonTintList"), @android.databinding.BindingMethod(type = android.widget.CompoundButton.class, attribute = "android:onCheckedChanged", method = "setOnCheckedChangeListener") })
@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.CompoundButton.class, attribute = "android:checked") })
public class CompoundButtonBindingAdapter {
    @android.databinding.BindingAdapter("android:checked")
    public static void setChecked(android.widget.CompoundButton view, boolean checked) {
        if (view.isChecked() != checked) {
            view.setChecked(checked);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onCheckedChanged", "android:checkedAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.CompoundButton view, final android.widget.CompoundButton.OnCheckedChangeListener listener, final android.databinding.InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener);
        } else {
            view.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
                @java.lang.Override
                public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                    if (listener != null) {
                        listener.onCheckedChanged(buttonView, isChecked);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}

