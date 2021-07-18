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


public class SwitchBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private int mThumb;

    @android.databinding.Bindable
    private int mTrack;

    public int getThumb() {
        return mThumb;
    }

    public int getTrack() {
        return mTrack;
    }

    public void changeValues() {
        mThumb = 0xff111111;
        mTrack = 0xff333333;
        notifyChange();
    }
}

