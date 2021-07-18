/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.app;


class ActionBarDrawerToggleJellybeanMR2 {
    private static final java.lang.String TAG = "ActionBarDrawerToggleImplJellybeanMR2";

    private static final int[] THEME_ATTRS = new int[]{ R.attr.homeAsUpIndicator };

    public static java.lang.Object setActionBarUpIndicator(java.lang.Object info, android.app.Activity activity, android.graphics.drawable.Drawable drawable, int contentDescRes) {
        final android.app.ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(drawable);
            actionBar.setHomeActionContentDescription(contentDescRes);
        }
        return info;
    }

    public static java.lang.Object setActionBarDescription(java.lang.Object info, android.app.Activity activity, int contentDescRes) {
        final android.app.ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.setHomeActionContentDescription(contentDescRes);
        }
        return info;
    }

    public static android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity) {
        final android.app.ActionBar actionBar = activity.getActionBar();
        final android.content.Context context;
        if (actionBar != null) {
            context = actionBar.getThemedContext();
        } else {
            context = activity;
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(null, android.support.v4.app.ActionBarDrawerToggleJellybeanMR2.THEME_ATTRS, R.attr.actionBarStyle, 0);
        final android.graphics.drawable.Drawable result = a.getDrawable(0);
        a.recycle();
        return result;
    }
}

