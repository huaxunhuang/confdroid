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


class RoundedRectHelperApi21 {
    private static android.util.SparseArray<android.view.ViewOutlineProvider> sRoundedRectProvider;

    private static final int MAX_CACHED_PROVIDER = 32;

    static final class RoundedRectOutlineProvider extends android.view.ViewOutlineProvider {
        private int mRadius;

        RoundedRectOutlineProvider(int radius) {
            mRadius = radius;
        }

        @java.lang.Override
        public void getOutline(android.view.View view, android.graphics.Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mRadius);
            outline.setAlpha(1.0F);
        }
    }

    public static void setClipToRoundedOutline(android.view.View view, boolean clip, int roundedCornerRadius) {
        if (clip) {
            if (android.support.v17.leanback.widget.RoundedRectHelperApi21.sRoundedRectProvider == null) {
                android.support.v17.leanback.widget.RoundedRectHelperApi21.sRoundedRectProvider = new android.util.SparseArray<android.view.ViewOutlineProvider>();
            }
            android.view.ViewOutlineProvider provider = android.support.v17.leanback.widget.RoundedRectHelperApi21.sRoundedRectProvider.get(roundedCornerRadius);
            if (provider == null) {
                provider = new android.support.v17.leanback.widget.RoundedRectHelperApi21.RoundedRectOutlineProvider(roundedCornerRadius);
                if (android.support.v17.leanback.widget.RoundedRectHelperApi21.sRoundedRectProvider.size() < android.support.v17.leanback.widget.RoundedRectHelperApi21.MAX_CACHED_PROVIDER) {
                    android.support.v17.leanback.widget.RoundedRectHelperApi21.sRoundedRectProvider.put(roundedCornerRadius, provider);
                }
            }
            view.setOutlineProvider(provider);
        } else {
            view.setOutlineProvider(android.view.ViewOutlineProvider.BACKGROUND);
        }
        view.setClipToOutline(clip);
    }
}

