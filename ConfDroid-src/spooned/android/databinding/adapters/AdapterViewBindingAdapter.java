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


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.AdapterView.class, attribute = "android:onItemClick", method = "setOnItemClickListener"), @android.databinding.BindingMethod(type = android.widget.AdapterView.class, attribute = "android:onItemLongClick", method = "setOnItemLongClickListener") })
@android.databinding.InverseBindingMethods({ @android.databinding.InverseBindingMethod(type = android.widget.AbsListView.class, attribute = "android:selectedItemPosition") })
public class AdapterViewBindingAdapter {
    @android.databinding.BindingAdapter("android:selectedItemPosition")
    public static void setSelectedItemPosition(android.widget.AdapterView view, int position) {
        if (view.getSelectedItemPosition() != position) {
            view.setSelection(position);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onItemSelected", "android:onNothingSelected", "android:selectedItemPositionAttrChanged" }, requireAll = false)
    public static void setOnItemSelectedListener(android.widget.AdapterView view, final android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected selected, final android.databinding.adapters.AdapterViewBindingAdapter.OnNothingSelected nothingSelected, final android.databinding.InverseBindingListener attrChanged) {
        if (((selected == null) && (nothingSelected == null)) && (attrChanged == null)) {
            view.setOnItemSelectedListener(null);
        } else {
            view.setOnItemSelectedListener(new android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelectedComponentListener(selected, nothingSelected, attrChanged));
        }
    }

    public static class OnItemSelectedComponentListener implements android.widget.AdapterView.OnItemSelectedListener {
        private final android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected mSelected;

        private final android.databinding.adapters.AdapterViewBindingAdapter.OnNothingSelected mNothingSelected;

        private final android.databinding.InverseBindingListener mAttrChanged;

        public OnItemSelectedComponentListener(android.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected selected, android.databinding.adapters.AdapterViewBindingAdapter.OnNothingSelected nothingSelected, android.databinding.InverseBindingListener attrChanged) {
            this.mSelected = selected;
            this.mNothingSelected = nothingSelected;
            this.mAttrChanged = attrChanged;
        }

        @java.lang.Override
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (mSelected != null) {
                mSelected.onItemSelected(parent, view, position, id);
            }
            if (mAttrChanged != null) {
                mAttrChanged.onChange();
            }
        }

        @java.lang.Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            if (mNothingSelected != null) {
                mNothingSelected.onNothingSelected(parent);
            }
            if (mAttrChanged != null) {
                mAttrChanged.onChange();
            }
        }
    }

    public interface OnItemSelected {
        void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id);
    }

    public interface OnNothingSelected {
        void onNothingSelected(android.widget.AdapterView<?> parent);
    }
}

