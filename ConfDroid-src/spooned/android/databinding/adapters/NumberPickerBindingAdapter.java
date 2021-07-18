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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.NumberPicker.class, attribute = "android:format", method = "setFormatter"), @android.databinding.BindingMethod(type = android.widget.NumberPicker.class, attribute = "android:onScrollStateChange", method = "setOnScrollListener") })
@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.NumberPicker.class, attribute = "android:value") })
public class NumberPickerBindingAdapter {
    @android.databinding.BindingAdapter("android:value")
    public static void setValue(android.widget.NumberPicker view, int value) {
        if (view.getValue() != value) {
            view.setValue(value);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onValueChange", "android:valueAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.NumberPicker view, final android.widget.NumberPicker.OnValueChangeListener listener, final android.databinding.InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnValueChangedListener(listener);
        } else {
            view.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
                @java.lang.Override
                public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                    if (listener != null) {
                        listener.onValueChange(picker, oldVal, newVal);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}

