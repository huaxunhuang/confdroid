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


public class AutoCompleteTextViewBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private int mPopupBackground;

    @android.databinding.Bindable
    private int mCompletionThreshold = 1;

    public int getCompletionThreshold() {
        return mCompletionThreshold;
    }

    public int getPopupBackground() {
        return mPopupBackground;
    }

    public void changeValues() {
        mPopupBackground = 0xff23456;
        mCompletionThreshold = 5;
        notifyChange();
    }
}

