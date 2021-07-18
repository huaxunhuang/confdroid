/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.testapp.adapter;


public class MultiArgTestAdapter {
    public static java.lang.String join(android.databinding.testapp.adapter.MultiArgTestAdapter.BaseMultiBindingClass... classes) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.databinding.testapp.adapter.MultiArgTestAdapter.BaseMultiBindingClass instance : classes) {
            sb.append(instance == null ? "??" : instance.getValue());
        }
        return sb.toString();
    }

    public static java.lang.String join(java.lang.String... strings) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String str : strings) {
            sb.append(str == null ? "??" : str);
        }
        return sb.toString();
    }

    @android.databinding.BindingAdapter({ "android:class1", "android:class2" })
    public static void setBoth(android.widget.TextView view, android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 class1, android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 class2) {
        view.setText(android.databinding.testapp.adapter.MultiArgTestAdapter.join(class1, class2));
    }

    @android.databinding.BindingAdapter({ "android:class1str", "android:class2str" })
    public static void setBoth(android.widget.TextView view, java.lang.String str1, java.lang.String str2) {
        view.setText(android.databinding.testapp.adapter.MultiArgTestAdapter.join(str1, str2));
    }

    @android.databinding.BindingAdapter({ "android:class1" })
    public static void setClass1(android.widget.TextView view, android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass1 class1) {
        view.setText(class1.getValue());
    }

    @android.databinding.BindingAdapter({ "android:classStr" })
    public static void setClassStr(android.widget.TextView view, java.lang.String str) {
        view.setText(str);
    }

    @android.databinding.BindingAdapter("android:class2")
    public static void setClass2(android.widget.TextView view, android.databinding.testapp.adapter.MultiArgTestAdapter.MultiBindingClass2 class2) {
        view.setText(class2.getValue());
    }

    @android.databinding.BindingAdapter("android:val3")
    public static void setWithOldValue(android.widget.TextView view, java.lang.String oldValue, java.lang.String newValue) {
        view.setText(java.lang.String.format("%s -> %s", oldValue, newValue));
    }

    @android.databinding.BindingAdapter({ "android:val3", "android:val4" })
    public static void set2WithOldValues(android.widget.TextView view, java.lang.String oldValue1, java.lang.String oldValue2, java.lang.String newValue1, java.lang.String newValue2) {
        view.setText(java.lang.String.format("%s, %s -> %s, %s", oldValue1, oldValue2, newValue1, newValue2));
    }

    public static class MultiBindingClass1 extends android.databinding.testapp.adapter.MultiArgTestAdapter.BaseMultiBindingClass {}

    public static class MultiBindingClass2 extends android.databinding.testapp.adapter.MultiArgTestAdapter.BaseMultiBindingClass {}

    public static class BaseMultiBindingClass extends android.databinding.BaseObservable {
        android.view.View mSetOn;

        @android.databinding.Bindable
        java.lang.String mValue;

        public android.view.View getSetOn() {
            return mSetOn;
        }

        public java.lang.String getValue() {
            return mValue;
        }

        public void setValue(java.lang.String value, boolean notify) {
            mValue = value;
            if (notify) {
                notifyPropertyChanged(BR.value);
            }
        }

        public void clear() {
            mSetOn = null;
        }
    }
}

