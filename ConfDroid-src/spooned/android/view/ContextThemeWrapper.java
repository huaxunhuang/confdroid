/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.view;


/**
 * A context wrapper that allows you to modify or replace the theme of the
 * wrapped context.
 */
public class ContextThemeWrapper extends android.content.ContextWrapper {
    @android.annotation.UnsupportedAppUsage
    private int mThemeResource;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123768723)
    private android.content.res.Resources.Theme mTheme;

    @android.annotation.UnsupportedAppUsage
    private android.view.LayoutInflater mInflater;

    private android.content.res.Configuration mOverrideConfiguration;

    @android.annotation.UnsupportedAppUsage
    private android.content.res.Resources mResources;

    /**
     * Creates a new context wrapper with no theme and no base context.
     * <p class="note">
     * <strong>Note:</strong> A base context <strong>must</strong> be attached
     * using {@link #attachBaseContext(Context)} before calling any other
     * method on the newly constructed context wrapper.
     */
    public ContextThemeWrapper() {
        super(null);
    }

    /**
     * Creates a new context wrapper with the specified theme.
     * <p>
     * The specified theme will be applied on top of the base context's theme.
     * Any attributes not explicitly defined in the theme identified by
     * <var>themeResId</var> will retain their original values.
     *
     * @param base
     * 		the base context
     * @param themeResId
     * 		the resource ID of the theme to be applied on top of
     * 		the base context's theme
     */
    public ContextThemeWrapper(android.content.Context base, @android.annotation.StyleRes
    int themeResId) {
        super(base);
        mThemeResource = themeResId;
    }

    /**
     * Creates a new context wrapper with the specified theme.
     * <p>
     * Unlike {@link #ContextThemeWrapper(Context, int)}, the theme passed to
     * this constructor will completely replace the base context's theme.
     *
     * @param base
     * 		the base context
     * @param theme
     * 		the theme against which resources should be inflated
     */
    public ContextThemeWrapper(android.content.Context base, android.content.res.Resources.Theme theme) {
        super(base);
        mTheme = theme;
    }

    @java.lang.Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(newBase);
    }

    /**
     * Call to set an "override configuration" on this context -- this is
     * a configuration that replies one or more values of the standard
     * configuration that is applied to the context.  See
     * {@link Context#createConfigurationContext(Configuration)} for more
     * information.
     *
     * <p>This method can only be called once, and must be called before any
     * calls to {@link #getResources()} or {@link #getAssets()} are made.
     */
    public void applyOverrideConfiguration(android.content.res.Configuration overrideConfiguration) {
        if (mResources != null) {
            throw new java.lang.IllegalStateException("getResources() or getAssets() has already been called");
        }
        if (mOverrideConfiguration != null) {
            throw new java.lang.IllegalStateException("Override configuration has already been set");
        }
        mOverrideConfiguration = new android.content.res.Configuration(overrideConfiguration);
    }

    /**
     * Used by ActivityThread to apply the overridden configuration to onConfigurationChange
     * callbacks.
     *
     * @unknown 
     */
    public android.content.res.Configuration getOverrideConfiguration() {
        return mOverrideConfiguration;
    }

    @java.lang.Override
    public android.content.res.AssetManager getAssets() {
        // Ensure we're returning assets with the correct configuration.
        return getResourcesInternal().getAssets();
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        return getResourcesInternal();
    }

    private android.content.res.Resources getResourcesInternal() {
        if (mResources == null) {
            if (mOverrideConfiguration == null) {
                mResources = super.getResources();
            } else {
                final android.content.Context resContext = createConfigurationContext(mOverrideConfiguration);
                mResources = resContext.getResources();
            }
        }
        return mResources;
    }

    @java.lang.Override
    public void setTheme(int resid) {
        if (mThemeResource != resid) {
            mThemeResource = resid;
            initializeTheme();
        }
    }

    /**
     * Set the configure the current theme. If null is provided then the default Theme is returned
     * on the next call to {@link #getTheme()}
     *
     * @param theme
     * 		Theme to consume in the wrapper, a value of null resets the theme to the default
     */
    public void setTheme(@android.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        mTheme = theme;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public int getThemeResId() {
        return mThemeResource;
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        }
        mThemeResource = android.content.res.Resources.selectDefaultTheme(mThemeResource, getApplicationInfo().targetSdkVersion);
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
     * resource to the current Theme object. May be overridden to change the
     * default (simple) behavior. This method will not be called in multiple
     * threads simultaneously.
     *
     * @param theme
     * 		the theme being modified
     * @param resId
     * 		the style resource being applied to <var>theme</var>
     * @param first
     * 		{@code true} if this is the first time a style is being
     * 		applied to <var>theme</var>
     */
    protected void onApplyThemeResource(android.content.res.Resources.Theme theme, int resId, boolean first) {
        theme.applyStyle(resId, true);
    }

    @android.annotation.UnsupportedAppUsage
    private void initializeTheme() {
        final boolean first = mTheme == null;
        if (first) {
            mTheme = getResources().newTheme();
            final android.content.res.Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                mTheme.setTo(theme);
            }
        }
        onApplyThemeResource(mTheme, mThemeResource, first);
    }
}

