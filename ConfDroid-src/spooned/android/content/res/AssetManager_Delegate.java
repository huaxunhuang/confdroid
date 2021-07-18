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
package android.content.res;


/**
 * Delegate used to provide implementation of a select few native methods of {@link AssetManager}
 * <p/>
 * Through the layoutlib_create tool, the original native methods of AssetManager have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class AssetManager_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.content.res.AssetManager_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.content.res.AssetManager_Delegate.class);

    public static com.android.layoutlib.bridge.impl.DelegateManager<android.content.res.AssetManager_Delegate> getDelegateManager() {
        return android.content.res.AssetManager_Delegate.sManager;
    }

    // ---- delegate methods. ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreate() {
        android.content.res.AssetManager_Delegate delegate = new android.content.res.AssetManager_Delegate();
        return android.content.res.AssetManager_Delegate.sManager.addNewDelegate(delegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeDestroy(long ptr) {
        android.content.res.AssetManager_Delegate.sManager.removeJavaReferenceFor(ptr);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static java.io.InputStream open(android.content.res.AssetManager mgr, java.lang.String fileName) throws java.io.IOException {
        return mgr.open_Original(fileName);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static java.io.InputStream open(android.content.res.AssetManager mgr, java.lang.String fileName, int accessMode) throws java.io.IOException {
        if (!(mgr instanceof android.content.res.BridgeAssetManager)) {
            return mgr.open_Original(fileName, accessMode);
        }
        return ((android.content.res.BridgeAssetManager) (mgr)).getAssetRepository().openAsset(fileName, accessMode);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeThemeCreate(long ptr) {
        return android.content.res.Resources_Theme_Delegate.getDelegateManager().addNewDelegate(new android.content.res.Resources_Theme_Delegate());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeThemeDestroy(long theme) {
        android.content.res.Resources_Theme_Delegate.getDelegateManager().removeJavaReferenceFor(theme);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.util.SparseArray<java.lang.String> getAssignedPackageIdentifiers(android.content.res.AssetManager manager) {
        return new android.util.SparseArray();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String[] nativeCreateIdmapsForStaticOverlaysTargetingAndroid() {
        // AssetManager requires this not to be null
        return new java.lang.String[0];
    }
}

