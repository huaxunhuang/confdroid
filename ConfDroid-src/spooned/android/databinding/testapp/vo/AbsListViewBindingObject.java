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


public class AbsListViewBindingObject extends android.databinding.testapp.vo.BindingAdapterBindingObject {
    @android.databinding.Bindable
    private android.graphics.drawable.ColorDrawable mListSelector = new android.graphics.drawable.ColorDrawable(0xffff0000);

    @android.databinding.Bindable
    private boolean mScrollingCache;

    @android.databinding.Bindable
    private boolean mSmoothScrollbar;

    public android.graphics.drawable.ColorDrawable getListSelector() {
        return mListSelector;
    }

    public boolean isScrollingCache() {
        return mScrollingCache;
    }

    public boolean isSmoothScrollbar() {
        return mSmoothScrollbar;
    }

    public void changeValues() {
        mListSelector = new android.graphics.drawable.ColorDrawable(0xffffffff);
        mScrollingCache = true;
        mSmoothScrollbar = true;
        notifyChange();
    }
}

