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
package android.databinding.adapters;


public class TabHostBindingAdapter {
    @android.databinding.InverseBindingAdapter(attribute = "android:currentTab")
    public static int getCurrentTab(android.widget.TabHost view) {
        return view.getCurrentTab();
    }

    @android.databinding.InverseBindingAdapter(attribute = "android:currentTab")
    public static java.lang.String getCurrentTabTag(android.widget.TabHost view) {
        return view.getCurrentTabTag();
    }

    @android.databinding.BindingAdapter("android:currentTab")
    public static void setCurrentTab(android.widget.TabHost view, int tab) {
        if (view.getCurrentTab() != tab) {
            view.setCurrentTab(tab);
        }
    }

    @android.databinding.BindingAdapter("android:currentTab")
    public static void setCurrentTabTag(android.widget.TabHost view, java.lang.String tabTag) {
        if (view.getCurrentTabTag() != tabTag) {
            view.setCurrentTabByTag(tabTag);
        }
    }

    @android.databinding.BindingAdapter(value = { "android:onTabChanged", "android:currentTabAttrChanged" }, requireAll = false)
    public static void setListeners(android.widget.TabHost view, final android.widget.TabHost.OnTabChangeListener listener, final android.databinding.InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnTabChangedListener(listener);
        } else {
            view.setOnTabChangedListener(new android.widget.TabHost.OnTabChangeListener() {
                @java.lang.Override
                public void onTabChanged(java.lang.String tabId) {
                    if (listener != null) {
                        listener.onTabChanged(tabId);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}

