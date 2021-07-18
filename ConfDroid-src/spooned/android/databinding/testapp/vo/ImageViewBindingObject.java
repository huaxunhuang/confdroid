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


public class ImageViewBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private int mTint;

    @android.databinding.Bindable
    private android.graphics.drawable.Drawable mSrc;

    @android.databinding.Bindable
    private android.graphics.PorterDuff.Mode mTintMode = android.graphics.PorterDuff.Mode.DARKEN;

    public int getTint() {
        return mTint;
    }

    public android.graphics.drawable.Drawable getSrc() {
        return mSrc;
    }

    public android.graphics.PorterDuff.Mode getTintMode() {
        return mTintMode;
    }

    public void changeValues() {
        mTint = 0xff111111;
        mSrc = new android.graphics.drawable.ColorDrawable(0xff00ff00);
        mTintMode = android.graphics.PorterDuff.Mode.LIGHTEN;
        notifyChange();
    }
}

