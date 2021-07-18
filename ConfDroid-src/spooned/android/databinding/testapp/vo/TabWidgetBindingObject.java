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


public class TabWidgetBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private android.graphics.drawable.ColorDrawable mDivider = new android.graphics.drawable.ColorDrawable(0xff0000ff);

    @android.databinding.Bindable
    private boolean mTabStripEnabled;

    @android.databinding.Bindable
    private android.graphics.drawable.ColorDrawable mTabStripLeft = new android.graphics.drawable.ColorDrawable(0xff00ff00);

    @android.databinding.Bindable
    private android.graphics.drawable.ColorDrawable mTabStripRight = new android.graphics.drawable.ColorDrawable(0xffff0000);

    public android.graphics.drawable.ColorDrawable getDivider() {
        return mDivider;
    }

    public android.graphics.drawable.ColorDrawable getTabStripLeft() {
        return mTabStripLeft;
    }

    public android.graphics.drawable.ColorDrawable getTabStripRight() {
        return mTabStripRight;
    }

    public boolean isTabStripEnabled() {
        return mTabStripEnabled;
    }

    public void changeValues() {
        mDivider = new android.graphics.drawable.ColorDrawable(0xff111111);
        mTabStripEnabled = true;
        mTabStripLeft = new android.graphics.drawable.ColorDrawable(0xff222222);
        mTabStripRight = new android.graphics.drawable.ColorDrawable(0xff333333);
        notifyChange();
    }
}

