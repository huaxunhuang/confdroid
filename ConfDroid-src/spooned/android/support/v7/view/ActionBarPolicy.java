/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v7.view;


/**
 * Allows components to query for various configuration policy decisions about how the action bar
 * should lay out and behave on the current device.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ActionBarPolicy {
    private android.content.Context mContext;

    public static android.support.v7.view.ActionBarPolicy get(android.content.Context context) {
        return new android.support.v7.view.ActionBarPolicy(context);
    }

    private ActionBarPolicy(android.content.Context context) {
        mContext = context;
    }

    /**
     * Returns the maximum number of action buttons that should be permitted within an action
     * bar/action mode. This will be used to determine how many showAsAction="ifRoom" items can fit.
     * "always" items can override this.
     */
    public int getMaxActionButtons() {
        final android.content.res.Resources res = mContext.getResources();
        final int widthDp = android.support.v4.content.res.ConfigurationHelper.getScreenWidthDp(res);
        final int heightDp = android.support.v4.content.res.ConfigurationHelper.getScreenHeightDp(res);
        final int smallest = android.support.v4.content.res.ConfigurationHelper.getSmallestScreenWidthDp(res);
        if ((((smallest > 600) || (widthDp > 600)) || ((widthDp > 960) && (heightDp > 720))) || ((widthDp > 720) && (heightDp > 960))) {
            // For values-w600dp, values-sw600dp and values-xlarge.
            return 5;
        } else
            if (((widthDp >= 500) || ((widthDp > 640) && (heightDp > 480))) || ((widthDp > 480) && (heightDp > 640))) {
                // For values-w500dp and values-large.
                return 4;
            } else
                if (widthDp >= 360) {
                    // For values-w360dp.
                    return 3;
                } else {
                    return 2;
                }


    }

    public boolean showsOverflowMenuButton() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return !android.support.v4.view.ViewConfigurationCompat.hasPermanentMenuKey(android.view.ViewConfiguration.get(mContext));
        }
    }

    public int getEmbeddedMenuWidthLimit() {
        return mContext.getResources().getDisplayMetrics().widthPixels / 2;
    }

    public boolean hasEmbeddedTabs() {
        return mContext.getResources().getBoolean(R.bool.abc_action_bar_embed_tabs);
    }

    public int getTabContainerHeight() {
        android.content.res.TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        int height = a.getLayoutDimension(R.styleable.ActionBar_height, 0);
        android.content.res.Resources r = mContext.getResources();
        if (!hasEmbeddedTabs()) {
            // Stacked tabs; limit the height
            height = java.lang.Math.min(height, r.getDimensionPixelSize(R.dimen.abc_action_bar_stacked_max_height));
        }
        a.recycle();
        return height;
    }

    public boolean enableHomeButtonByDefault() {
        // Older apps get the home button interaction enabled by default.
        // Newer apps need to enable it explicitly.
        return mContext.getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public int getStackedTabMaxWidth() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_stacked_tab_max_width);
    }
}

