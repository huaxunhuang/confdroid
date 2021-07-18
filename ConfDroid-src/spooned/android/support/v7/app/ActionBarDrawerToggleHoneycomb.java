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
package android.support.v7.app;


/**
 * This class encapsulates some awful hacks.
 *
 * Before JB-MR2 (API 18) it was not possible to change the home-as-up indicator glyph
 * in an action bar without some really gross hacks. Since the MR2 SDK is not published as of
 * this writing, the new API is accessed via reflection here if available.
 *
 * Moved from Support-v4
 */
class ActionBarDrawerToggleHoneycomb {
    private static final java.lang.String TAG = "ActionBarDrawerToggleHoneycomb";

    private static final int[] THEME_ATTRS = new int[]{ R.attr.homeAsUpIndicator };

    public static android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo setActionBarUpIndicator(android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo info, android.app.Activity activity, android.graphics.drawable.Drawable drawable, int contentDescRes) {
        if (true || (info == null)) {
            info = new android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(activity);
        }
        if (info.setHomeAsUpIndicator != null) {
            try {
                final android.app.ActionBar actionBar = activity.getActionBar();
                info.setHomeAsUpIndicator.invoke(actionBar, drawable);
                info.setHomeActionContentDescription.invoke(actionBar, contentDescRes);
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.support.v7.app.ActionBarDrawerToggleHoneycomb.TAG, "Couldn't set home-as-up indicator via JB-MR2 API", e);
            }
        } else
            if (info.upIndicatorView != null) {
                info.upIndicatorView.setImageDrawable(drawable);
            } else {
                android.util.Log.w(android.support.v7.app.ActionBarDrawerToggleHoneycomb.TAG, "Couldn't set home-as-up indicator");
            }

        return info;
    }

    public static android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo setActionBarDescription(android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo info, android.app.Activity activity, int contentDescRes) {
        if (info == null) {
            info = new android.support.v7.app.ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(activity);
        }
        if (info.setHomeAsUpIndicator != null) {
            try {
                final android.app.ActionBar actionBar = activity.getActionBar();
                info.setHomeActionContentDescription.invoke(actionBar, contentDescRes);
                if (android.os.Build.VERSION.SDK_INT <= 19) {
                    // For API 19 and earlier, we need to manually force the
                    // action bar to generate a new content description.
                    actionBar.setSubtitle(actionBar.getSubtitle());
                }
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.support.v7.app.ActionBarDrawerToggleHoneycomb.TAG, "Couldn't set content description via JB-MR2 API", e);
            }
        }
        return info;
    }

    public static android.graphics.drawable.Drawable getThemeUpIndicator(android.app.Activity activity) {
        final android.content.res.TypedArray a = activity.obtainStyledAttributes(android.support.v7.app.ActionBarDrawerToggleHoneycomb.THEME_ATTRS);
        final android.graphics.drawable.Drawable result = a.getDrawable(0);
        a.recycle();
        return result;
    }

    static class SetIndicatorInfo {
        public java.lang.reflect.Method setHomeAsUpIndicator;

        public java.lang.reflect.Method setHomeActionContentDescription;

        public android.widget.ImageView upIndicatorView;

        SetIndicatorInfo(android.app.Activity activity) {
            try {
                setHomeAsUpIndicator = android.app.ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", android.graphics.drawable.Drawable.class);
                setHomeActionContentDescription = android.app.ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", java.lang.Integer.TYPE);
                // If we got the method we won't need the stuff below.
                return;
            } catch (java.lang.NoSuchMethodException e) {
                // Oh well. We'll use the other mechanism below instead.
            }
            final android.view.View home = activity.findViewById(android.R.id.home);
            if (home == null) {
                // Action bar doesn't have a known configuration, an OEM messed with things.
                return;
            }
            final android.view.ViewGroup parent = ((android.view.ViewGroup) (home.getParent()));
            final int childCount = parent.getChildCount();
            if (childCount != 2) {
                // No idea which one will be the right one, an OEM messed with things.
                return;
            }
            final android.view.View first = parent.getChildAt(0);
            final android.view.View second = parent.getChildAt(1);
            final android.view.View up = (first.getId() == android.R.id.home) ? second : first;
            if (up instanceof android.widget.ImageView) {
                // Jackpot! (Probably...)
                upIndicatorView = ((android.widget.ImageView) (up));
            }
        }
    }
}

