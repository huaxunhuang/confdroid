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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.AutoCompleteTextView.class, attribute = "android:completionThreshold", method = "setThreshold"), @android.databinding.BindingMethod(type = android.widget.AutoCompleteTextView.class, attribute = "android:popupBackground", method = "setDropDownBackgroundDrawable"), @android.databinding.BindingMethod(type = android.widget.AutoCompleteTextView.class, attribute = "android:onDismiss", method = "setOnDismissListener"), @android.databinding.BindingMethod(type = android.widget.AutoCompleteTextView.class, attribute = "android:onItemClick", method = "setOnItemClickListener") })
public class AutoCompleteTextViewBindingAdapter {
    @android.databinding.BindingAdapter(value = { "android:fixText", "android:isValid" }, requireAll = false)
    public static void setValidator(android.widget.AutoCompleteTextView view, final android.databinding.adapters.AutoCompleteTextViewBindingAdapter.FixText fixText, final android.databinding.adapters.AutoCompleteTextViewBindingAdapter.IsValid isValid) {
        if ((fixText == null) && (isValid == null)) {
            view.setValidator(null);
        } else {
            view.setValidator(new android.widget.AutoCompleteTextView.Validator() {
                @java.lang.Override
                public boolean isValid(java.lang.CharSequence text) {
                    if (isValid != null) {
                        return isValid.isValid(text);
                    } else {
                        return true;
                    }
                }

                @java.lang.Override
                public java.lang.CharSequence fixText(java.lang.CharSequence invalidText) {
                    if (fixText != null) {
                        return fixText.fixText(invalidText);
                    } else {
                        return invalidText;
                    }
                }
            });
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onItemSelected", "android:onNothingSelected" }, requireAll = false)
    public static void setOnItemSelectedListener(android.widget.AutoCompleteTextView view, final android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected selected, final android.databinding.adapters.AdapterViewBindingAdapter.OnNothingSelected nothingSelected) {
        if ((selected == null) && (nothingSelected == null)) {
            view.setOnItemSelectedListener(null);
        } else {
            view.setOnItemSelectedListener(new android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelectedComponentListener(selected, nothingSelected, null));
        }
    }

    public interface IsValid {
        boolean isValid(java.lang.CharSequence text);
    }

    public interface FixText {
        java.lang.CharSequence fixText(java.lang.CharSequence invalidText);
    }
}

