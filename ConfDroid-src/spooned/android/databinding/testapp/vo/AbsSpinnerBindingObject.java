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


public class AbsSpinnerBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private java.lang.CharSequence[] mEntries = new java.lang.CharSequence[]{ "hello", "world" };

    private static final java.lang.CharSequence[] CHANGED_VALUES = new java.lang.CharSequence[]{ "goodbye", "cruel", "world" };

    private android.databinding.ObservableList<java.lang.String> mList = new android.databinding.ObservableArrayList<java.lang.String>();

    public AbsSpinnerBindingObject() {
        mList.add("Hello");
        mList.add("World");
    }

    public java.lang.CharSequence[] getEntries() {
        return mEntries;
    }

    public void changeValues() {
        mEntries = android.databinding.testapp.vo.AbsSpinnerBindingObject.CHANGED_VALUES;
        notifyChange();
    }

    public java.util.List<java.lang.String> getList() {
        return mList;
    }
}

