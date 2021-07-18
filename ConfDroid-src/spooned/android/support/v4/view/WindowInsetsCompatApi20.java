/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.view;


class WindowInsetsCompatApi20 {
    public static java.lang.Object consumeSystemWindowInsets(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).consumeSystemWindowInsets();
    }

    public static int getSystemWindowInsetBottom(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getSystemWindowInsetBottom();
    }

    public static int getSystemWindowInsetLeft(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getSystemWindowInsetLeft();
    }

    public static int getSystemWindowInsetRight(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getSystemWindowInsetRight();
    }

    public static int getSystemWindowInsetTop(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getSystemWindowInsetTop();
    }

    public static boolean hasInsets(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).hasInsets();
    }

    public static boolean hasSystemWindowInsets(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).hasSystemWindowInsets();
    }

    public static boolean isRound(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).isRound();
    }

    public static java.lang.Object replaceSystemWindowInsets(java.lang.Object insets, int left, int top, int right, int bottom) {
        return ((android.view.WindowInsets) (insets)).replaceSystemWindowInsets(left, top, right, bottom);
    }

    public static java.lang.Object getSourceWindowInsets(java.lang.Object src) {
        return new android.view.WindowInsets(((android.view.WindowInsets) (src)));
    }
}

