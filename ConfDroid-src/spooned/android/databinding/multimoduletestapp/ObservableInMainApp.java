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
package android.databinding.multimoduletestapp;


public class ObservableInMainApp extends android.databinding.BaseObservable {
    @android.databinding.Bindable
    private java.lang.String mAppField1;

    @android.databinding.Bindable
    private int mAppField2;

    @android.databinding.Bindable
    private int mSharedField;

    public java.lang.String getAppField1() {
        return mAppField1;
    }

    public void setAppField1(java.lang.String appField1) {
        mAppField1 = appField1;
        notifyPropertyChanged(BR.appField1);
    }

    public int getAppField2() {
        return mAppField2;
    }

    public void setAppField2(int appField2) {
        mAppField2 = appField2;
        notifyPropertyChanged(BR.appField2);
    }

    public int getSharedField() {
        return mSharedField;
    }

    public void setSharedField(int sharedField) {
        mSharedField = sharedField;
        notifyPropertyChanged(BR.sharedField);
    }
}

