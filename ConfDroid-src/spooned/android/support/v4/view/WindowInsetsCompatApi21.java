/**
 * Copyright (C) 2014 The Android Open Source Project
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


class WindowInsetsCompatApi21 {
    public static java.lang.Object consumeStableInsets(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).consumeStableInsets();
    }

    public static int getStableInsetBottom(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getStableInsetBottom();
    }

    public static int getStableInsetLeft(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getStableInsetLeft();
    }

    public static int getStableInsetRight(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getStableInsetRight();
    }

    public static int getStableInsetTop(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).getStableInsetTop();
    }

    public static boolean hasStableInsets(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).hasStableInsets();
    }

    public static boolean isConsumed(java.lang.Object insets) {
        return ((android.view.WindowInsets) (insets)).isConsumed();
    }

    public static java.lang.Object replaceSystemWindowInsets(java.lang.Object insets, android.graphics.Rect systemWindowInsets) {
        return ((android.view.WindowInsets) (insets)).replaceSystemWindowInsets(systemWindowInsets);
    }
}

