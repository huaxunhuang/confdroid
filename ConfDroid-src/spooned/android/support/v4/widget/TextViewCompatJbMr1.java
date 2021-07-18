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
package android.support.v4.widget;


class TextViewCompatJbMr1 {
    public static void setCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
        textView.setCompoundDrawables(rtl ? end : start, top, rtl ? start : end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
        textView.setCompoundDrawablesWithIntrinsicBounds(rtl ? end : start, top, rtl ? start : end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, int start, int top, int end, int bottom) {
        boolean rtl = textView.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
        textView.setCompoundDrawablesWithIntrinsicBounds(rtl ? end : start, top, rtl ? start : end, bottom);
    }

    public static android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        final boolean rtl = textView.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
        final android.graphics.drawable.Drawable[] compounds = textView.getCompoundDrawables();
        if (rtl) {
            // If we're on RTL, we need to invert the horizontal result like above
            final android.graphics.drawable.Drawable start = compounds[2];
            final android.graphics.drawable.Drawable end = compounds[0];
            compounds[0] = start;
            compounds[2] = end;
        }
        return compounds;
    }
}

