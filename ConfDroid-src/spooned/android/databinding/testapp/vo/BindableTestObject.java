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


public class BindableTestObject extends android.databinding.BaseObservable {
    @android.databinding.Bindable
    public int bindableField1;

    @android.databinding.Bindable
    private int bindableField2;

    private int bindableField3;

    @android.databinding.Bindable
    public int m_bindableField4;

    @android.databinding.Bindable
    public int mbindableField5;

    @android.databinding.Bindable
    public int _bindableField6;

    @android.databinding.Bindable
    public int _BindableField7;

    @android.databinding.Bindable
    public int mBindableField8;

    public int getBindableField2() {
        return bindableField2;
    }

    @android.databinding.Bindable
    public int getBindableField3() {
        return bindableField3;
    }
}

