/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A wrapper class working with {@link ItemBridgeAdapter} to wrap item view in a
 * {@link ShadowOverlayContainer}.  The ShadowOverlayContainer is created from conditions
 * of {@link ShadowOverlayHelper}.
 */
public class ItemBridgeAdapterShadowOverlayWrapper extends android.support.v17.leanback.widget.ItemBridgeAdapter.Wrapper {
    private final android.support.v17.leanback.widget.ShadowOverlayHelper mHelper;

    public ItemBridgeAdapterShadowOverlayWrapper(android.support.v17.leanback.widget.ShadowOverlayHelper helper) {
        mHelper = helper;
    }

    @java.lang.Override
    public android.view.View createWrapper(android.view.View root) {
        android.content.Context context = root.getContext();
        android.support.v17.leanback.widget.ShadowOverlayContainer wrapper = mHelper.createShadowOverlayContainer(context);
        return wrapper;
    }

    @java.lang.Override
    public void wrap(android.view.View wrapper, android.view.View wrapped) {
        ((android.support.v17.leanback.widget.ShadowOverlayContainer) (wrapper)).wrap(wrapped);
    }
}

