/**
 * Copyright (C) 2014 The Android Open Source Project
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


class ShadowHelperJbmr2 {
    static class ShadowImpl {
        android.view.View mNormalShadow;

        android.view.View mFocusShadow;
    }

    /* prepare parent for allowing shadows of a child */
    public static void prepareParent(android.view.ViewGroup parent) {
        parent.setLayoutMode(android.view.ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS);
    }

    /* add shadows and return a implementation detail object */
    public static java.lang.Object addShadow(android.view.ViewGroup shadowContainer) {
        shadowContainer.setLayoutMode(android.view.ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(shadowContainer.getContext());
        inflater.inflate(R.layout.lb_shadow, shadowContainer, true);
        android.support.v17.leanback.widget.ShadowHelperJbmr2.ShadowImpl impl = new android.support.v17.leanback.widget.ShadowHelperJbmr2.ShadowImpl();
        impl.mNormalShadow = shadowContainer.findViewById(R.id.lb_shadow_normal);
        impl.mFocusShadow = shadowContainer.findViewById(R.id.lb_shadow_focused);
        return impl;
    }

    /* set shadow focus level 0 for unfocused 1 for fully focused */
    public static void setShadowFocusLevel(java.lang.Object impl, float level) {
        android.support.v17.leanback.widget.ShadowHelperJbmr2.ShadowImpl shadowImpl = ((android.support.v17.leanback.widget.ShadowHelperJbmr2.ShadowImpl) (impl));
        shadowImpl.mNormalShadow.setAlpha(1 - level);
        shadowImpl.mFocusShadow.setAlpha(level);
    }
}

