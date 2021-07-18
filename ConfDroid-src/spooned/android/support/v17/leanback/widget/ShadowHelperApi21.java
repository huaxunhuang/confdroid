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


class ShadowHelperApi21 {
    static class ShadowImpl {
        android.view.View mShadowContainer;

        float mNormalZ;

        float mFocusedZ;
    }

    static final android.view.ViewOutlineProvider sOutlineProvider = new android.view.ViewOutlineProvider() {
        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            outline.setRect(0, 0, view.getWidth(), view.getHeight());
            outline.setAlpha(1.0F);
        }
    };

    /* add shadows and return a implementation detail object */
    public static java.lang.Object addDynamicShadow(android.view.View shadowContainer, float unfocusedZ, float focusedZ, int roundedCornerRadius) {
        if (roundedCornerRadius > 0) {
            android.support.v17.leanback.widget.RoundedRectHelperApi21.setClipToRoundedOutline(shadowContainer, true, roundedCornerRadius);
        } else {
            shadowContainer.setOutlineProvider(android.support.v17.leanback.widget.ShadowHelperApi21.sOutlineProvider);
        }
        android.support.v17.leanback.widget.ShadowHelperApi21.ShadowImpl impl = new android.support.v17.leanback.widget.ShadowHelperApi21.ShadowImpl();
        impl.mShadowContainer = shadowContainer;
        impl.mNormalZ = unfocusedZ;
        impl.mFocusedZ = focusedZ;
        shadowContainer.setZ(impl.mNormalZ);
        return impl;
    }

    /* set shadow focus level 0 for unfocused 1 for fully focused */
    public static void setShadowFocusLevel(java.lang.Object object, float level) {
        android.support.v17.leanback.widget.ShadowHelperApi21.ShadowImpl impl = ((android.support.v17.leanback.widget.ShadowHelperApi21.ShadowImpl) (object));
        impl.mShadowContainer.setZ(impl.mNormalZ + (level * (impl.mFocusedZ - impl.mNormalZ)));
    }

    public static void setZ(android.view.View view, float z) {
        view.setZ(z);
    }
}

