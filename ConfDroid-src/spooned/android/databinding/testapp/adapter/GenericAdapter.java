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


public class GenericAdapter {
    @android.databinding.BindingAdapter("textList1")
    public static <T> void setListText(android.widget.TextView view, java.util.List<T> list) {
        android.databinding.testapp.adapter.GenericAdapter.setText(view, list);
    }

    @android.databinding.BindingAdapter("textList2")
    public static <T> void setCollectionText(android.widget.TextView view, java.util.Collection<T> list) {
        android.databinding.testapp.adapter.GenericAdapter.setText(view, list);
    }

    @android.databinding.BindingAdapter("textArray")
    public static <T> void setArrayText(android.widget.TextView view, T[] values) {
        android.databinding.testapp.adapter.GenericAdapter.setText(view, java.util.Arrays.asList(values));
    }

    @android.databinding.BindingAdapter({ "textList1", "textArray" })
    public static <T> void setListAndArray(android.widget.TextView view, java.util.List<T> list, T[] values) {
        android.databinding.testapp.adapter.GenericAdapter.setText(view, list);
    }

    @android.databinding.BindingAdapter("list")
    public static <T> void setGenericViewValue(android.databinding.testapp.GenericView<T> view, java.util.List<T> value) {
        view.setList(value);
    }

    @android.databinding.BindingAdapter({ "list", "array" })
    public static <T> void setGenericListAndArray(android.databinding.testapp.GenericView<T> view, java.util.List<T> list, T[] values) {
        view.setList(list);
    }

    @android.databinding.BindingAdapter("textList3")
    public static void setGenericList(android.widget.TextView view, java.util.List<java.lang.String> list) {
        android.databinding.testapp.adapter.GenericAdapter.setText(view, list);
    }

    @android.databinding.BindingAdapter("textList3")
    public static void setGenericIntegerList(android.widget.TextView view, java.util.List<java.lang.Integer> list) {
    }

    private static <T> void setText(android.widget.TextView view, java.util.Collection<T> collection) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        boolean isFirst = true;
        for (T val : collection) {
            if (isFirst) {
                isFirst = false;
            } else {
                stringBuilder.append(' ');
            }
            stringBuilder.append(val.toString());
        }
        view.setText(stringBuilder.toString());
    }
}

