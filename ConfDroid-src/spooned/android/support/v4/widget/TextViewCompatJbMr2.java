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


class TextViewCompatJbMr2 {
    public static void setCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        textView.setCompoundDrawablesRelative(start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.support.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.support.annotation.NonNull
    android.widget.TextView textView, @android.support.annotation.DrawableRes
    int start, @android.support.annotation.DrawableRes
    int top, @android.support.annotation.DrawableRes
    int end, @android.support.annotation.DrawableRes
    int bottom) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }

    public static android.graphics.drawable.Drawable[] getCompoundDrawablesRelative(@android.support.annotation.NonNull
    android.widget.TextView textView) {
        return textView.getCompoundDrawablesRelative();
    }
}

