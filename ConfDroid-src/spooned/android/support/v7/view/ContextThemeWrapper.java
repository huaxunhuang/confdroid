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
package android.support.v7.view;


/**
 * A ContextWrapper that allows you to modify the theme from what is in the
 * wrapped context.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ContextThemeWrapper extends android.content.ContextWrapper {
    private int mThemeResource;

    private android.content.res.Resources.Theme mTheme;

    private android.view.LayoutInflater mInflater;

    public ContextThemeWrapper(android.content.Context base, @android.support.annotation.StyleRes
    int themeResId) {
        super(base);
        mThemeResource = themeResId;
    }

    public ContextThemeWrapper(android.content.Context base, android.content.res.Resources.Theme theme) {
        super(base);
        mTheme = theme;
    }

    @java.lang.Override
    public void setTheme(int resid) {
        if (mThemeResource != resid) {
            mThemeResource = resid;
            initializeTheme();
        }
    }

    public int getThemeResId() {
        return mThemeResource;
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        }
        if (mThemeResource == 0) {
            mThemeResource = R.style.Theme_AppCompat_Light;
        }
        initializeTheme();
        return mTheme;
    }

    @java.lang.Override
    public java.lang.Object getSystemService(java.lang.String name) {
        if (android.content.Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = android.view.LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return mInflater;
        }
        return getBaseContext().getSystemService(name);
    }

    /**
     * Called by {@link #setTheme} and {@link #getTheme} to apply a theme
     * resource to the current Theme object.  Can override to change the
     * default (simple) behavior.  This method will not be called in multiple
     * threads simultaneously.
     *
     * @param theme
     * 		The Theme object being modified.
     * @param resid
     * 		The theme style resource being applied to <var>theme</var>.
     * @param first
     * 		Set to true if this is the first time a style is being
     * 		applied to <var>theme</var>.
     */
    protected void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
        theme.applyStyle(resid, true);
    }

    private void initializeTheme() {
        final boolean first = mTheme == null;
        if (first) {
            mTheme = getResources().newTheme();
            android.content.res.Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                mTheme.setTo(theme);
            }
        }
        onApplyThemeResource(mTheme, mThemeResource, first);
    }
}

