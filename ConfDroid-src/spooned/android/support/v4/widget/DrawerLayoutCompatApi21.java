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
package android.support.v4.widget;


/**
 * Provides functionality for DrawerLayout unique to API 21
 */
class DrawerLayoutCompatApi21 {
    private static final int[] THEME_ATTRS = new int[]{ android.R.attr.colorPrimaryDark };

    public static void configureApplyInsets(android.view.View drawerLayout) {
        if (drawerLayout instanceof android.support.v4.widget.DrawerLayoutImpl) {
            drawerLayout.setOnApplyWindowInsetsListener(new android.support.v4.widget.DrawerLayoutCompatApi21.InsetsListener());
            drawerLayout.setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static void dispatchChildInsets(android.view.View child, java.lang.Object insets, int gravity) {
        android.view.WindowInsets wi = ((android.view.WindowInsets) (insets));
        if (gravity == android.view.Gravity.LEFT) {
            wi = wi.replaceSystemWindowInsets(wi.getSystemWindowInsetLeft(), wi.getSystemWindowInsetTop(), 0, wi.getSystemWindowInsetBottom());
        } else
            if (gravity == android.view.Gravity.RIGHT) {
                wi = wi.replaceSystemWindowInsets(0, wi.getSystemWindowInsetTop(), wi.getSystemWindowInsetRight(), wi.getSystemWindowInsetBottom());
            }

        child.dispatchApplyWindowInsets(wi);
    }

    public static void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams lp, java.lang.Object insets, int gravity) {
        android.view.WindowInsets wi = ((android.view.WindowInsets) (insets));
        if (gravity == android.view.Gravity.LEFT) {
            wi = wi.replaceSystemWindowInsets(wi.getSystemWindowInsetLeft(), wi.getSystemWindowInsetTop(), 0, wi.getSystemWindowInsetBottom());
        } else
            if (gravity == android.view.Gravity.RIGHT) {
                wi = wi.replaceSystemWindowInsets(0, wi.getSystemWindowInsetTop(), wi.getSystemWindowInsetRight(), wi.getSystemWindowInsetBottom());
            }

        lp.leftMargin = wi.getSystemWindowInsetLeft();
        lp.topMargin = wi.getSystemWindowInsetTop();
        lp.rightMargin = wi.getSystemWindowInsetRight();
        lp.bottomMargin = wi.getSystemWindowInsetBottom();
    }

    public static int getTopInset(java.lang.Object insets) {
        return insets != null ? ((android.view.WindowInsets) (insets)).getSystemWindowInsetTop() : 0;
    }

    public static android.graphics.drawable.Drawable getDefaultStatusBarBackground(android.content.Context context) {
        final android.content.res.TypedArray a = context.obtainStyledAttributes(android.support.v4.widget.DrawerLayoutCompatApi21.THEME_ATTRS);
        try {
            return a.getDrawable(0);
        } finally {
            a.recycle();
        }
    }

    static class InsetsListener implements android.view.View.OnApplyWindowInsetsListener {
        @java.lang.Override
        public android.view.WindowInsets onApplyWindowInsets(android.view.View v, android.view.WindowInsets insets) {
            final android.support.v4.widget.DrawerLayoutImpl drawerLayout = ((android.support.v4.widget.DrawerLayoutImpl) (v));
            drawerLayout.setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
            return insets.consumeSystemWindowInsets();
        }
    }
}

