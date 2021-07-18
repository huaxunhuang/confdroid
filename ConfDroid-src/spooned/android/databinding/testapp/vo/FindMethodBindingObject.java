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
package android.databinding.testapp.vo;


public class FindMethodBindingObject extends android.databinding.testapp.vo.FindMethodBindingObjectBase {
    public java.lang.String method() {
        return "no arg";
    }

    public java.lang.String method(int i) {
        return java.lang.String.valueOf(i);
    }

    public java.lang.String method(float f) {
        return java.lang.String.valueOf(f);
    }

    public java.lang.String method(java.lang.String value) {
        return value;
    }

    public static java.lang.String staticMethod() {
        return "world";
    }

    public static android.databinding.testapp.vo.FindMethodBindingObject.Foo foo = new android.databinding.testapp.vo.FindMethodBindingObject.Foo();

    public android.databinding.testapp.vo.FindMethodBindingObject.ObservableClass observableClass = new android.databinding.testapp.vo.FindMethodBindingObject.ObservableClass();

    public static android.databinding.testapp.vo.FindMethodBindingObject.Bar<java.lang.String> bar = new android.databinding.testapp.vo.FindMethodBindingObject.Bar<>();

    public float confusingParam(int i) {
        return i;
    }

    public java.lang.String confusingParam(java.lang.Object o) {
        return o.toString();
    }

    public int confusingPrimitive(int i) {
        return i;
    }

    public java.lang.String confusingPrimitive(java.lang.Integer i) {
        return i.toString();
    }

    public float confusingInheritance(java.lang.Object o) {
        return 0;
    }

    public java.lang.String confusingInheritance(java.lang.String s) {
        return s;
    }

    public int confusingInheritance(java.lang.Integer i) {
        return i;
    }

    public int confusingTypeArgs(java.util.List<java.lang.String> s) {
        return 0;
    }

    public java.lang.String confusingTypeArgs(java.util.Map<java.lang.String, java.lang.String> s) {
        return "yay";
    }

    public android.util.ArrayMap<java.lang.String, java.lang.String> getMap() {
        return null;
    }

    public int[] getArray() {
        return new int[5];
    }

    public final android.databinding.ObservableField<java.lang.String> myField = new android.databinding.ObservableField<java.lang.String>();

    public java.util.List getList() {
        java.util.ArrayList<java.lang.String> vals = new java.util.ArrayList<>();
        vals.add("hello");
        return vals;
    }

    public static class Foo {
        public final java.lang.String bar = "hello world";

        public static final java.lang.String baz = "hello world";
    }

    public static class Bar<T> {
        public T method(T value) {
            return value;
        }
    }

    public static final class ObservableClass extends android.databinding.BaseObservable {
        public java.lang.String x;

        public java.lang.String getX() {
            return x;
        }

        public void setX(java.lang.String x) {
            this.x = x;
            notifyPropertyChanged(BR._all);
        }
    }
}

