/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.content.res;


/**
 * Delegate used to provide new implementation of a select few methods of {@link Resources.Theme}
 *
 * Through the layoutlib_create tool, the original  methods of Theme have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class Resources_Theme_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.content.res.Resources_Theme_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.content.res.Resources_Theme_Delegate>(android.content.res.Resources_Theme_Delegate.class);

    public static com.android.layoutlib.bridge.impl.DelegateManager<android.content.res.Resources_Theme_Delegate> getDelegateManager() {
        return android.content.res.Resources_Theme_Delegate.sManager;
    }

    // ---- delegate methods. ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainStyledAttributes(android.content.res.Resources thisResources, android.content.res.Resources.Theme thisTheme, int[] attrs) {
        boolean changed = android.content.res.Resources_Theme_Delegate.setupResources(thisTheme);
        android.content.res.BridgeTypedArray ta = com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().internalObtainStyledAttributes(0, attrs);
        ta.setTheme(thisTheme);
        android.content.res.Resources_Theme_Delegate.restoreResources(changed);
        return ta;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainStyledAttributes(android.content.res.Resources thisResources, android.content.res.Resources.Theme thisTheme, int resid, int[] attrs) throws android.content.res.Resources.NotFoundException {
        boolean changed = android.content.res.Resources_Theme_Delegate.setupResources(thisTheme);
        android.content.res.BridgeTypedArray ta = com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().internalObtainStyledAttributes(resid, attrs);
        ta.setTheme(thisTheme);
        android.content.res.Resources_Theme_Delegate.restoreResources(changed);
        return ta;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainStyledAttributes(android.content.res.Resources thisResources, android.content.res.Resources.Theme thisTheme, android.util.AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        boolean changed = android.content.res.Resources_Theme_Delegate.setupResources(thisTheme);
        android.content.res.BridgeTypedArray ta = com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().internalObtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
        ta.setTheme(thisTheme);
        android.content.res.Resources_Theme_Delegate.restoreResources(changed);
        return ta;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean resolveAttribute(android.content.res.Resources thisResources, android.content.res.Resources.Theme thisTheme, int resid, android.util.TypedValue outValue, boolean resolveRefs) {
        boolean changed = android.content.res.Resources_Theme_Delegate.setupResources(thisTheme);
        boolean found = com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().resolveThemeAttribute(resid, outValue, resolveRefs);
        android.content.res.Resources_Theme_Delegate.restoreResources(changed);
        return found;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray resolveAttributes(android.content.res.Resources thisResources, android.content.res.Resources.Theme thisTheme, int[] values, int[] attrs) {
        // FIXME
        return null;
    }

    // ---- private helper methods ----
    private static boolean setupResources(android.content.res.Resources.Theme thisTheme) {
        // Key is a space-separated list of theme ids applied that have been merged into the
        // BridgeContext's theme to make thisTheme.
        final android.content.res.Resources.ThemeKey key = thisTheme.getKey();
        final int[] resId = key.mResId;
        final boolean[] force = key.mForce;
        boolean changed = false;
        for (int i = 0, N = key.mCount; i < N; i++) {
            com.android.ide.common.rendering.api.StyleResourceValue style = android.content.res.Resources_Theme_Delegate.resolveStyle(resId[i]);
            if (style != null) {
                com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().getRenderResources().applyStyle(style, force[i]);
                changed = true;
            }
        }
        return changed;
    }

    private static void restoreResources(boolean changed) {
        if (changed) {
            com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext().getRenderResources().clearStyles();
        }
    }

    @android.annotation.Nullable
    private static com.android.ide.common.rendering.api.StyleResourceValue resolveStyle(int nativeResid) {
        if (nativeResid == 0) {
            return null;
        }
        com.android.layoutlib.bridge.android.BridgeContext context = com.android.layoutlib.bridge.impl.RenderSessionImpl.getCurrentContext();
        com.android.ide.common.rendering.api.ResourceReference theme = context.resolveId(nativeResid);
        return ((com.android.ide.common.rendering.api.StyleResourceValue) (context.getRenderResources().getResolvedResource(theme)));
    }
}

