/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.fonts;


/**
 * Delegate implementing the native methods of android.graphics.fonts.Font$Builder
 * <p>
 * Through the layoutlib_create tool, the original native methods of Font$Builder have been
 * replaced by calls to methods of the same name in this delegate class.
 * <p>
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between it
 * and the original Font$Builder class.
 *
 * @see DelegateManager
 */
public class Font_Builder_Delegate {
    protected static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.fonts.Font_Builder_Delegate> sBuilderManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.fonts.Font_Builder_Delegate.class);

    private static final com.android.layoutlib.bridge.impl.DelegateManager<java.lang.String> sAssetManager = new com.android.layoutlib.bridge.impl.DelegateManager(java.lang.String.class);

    private static long sFontFinalizer = -1;

    private static long sAssetFinalizer = -1;

    protected java.nio.ByteBuffer mBuffer;

    protected int mWeight;

    protected boolean mItalic;

    protected int mTtcIndex;

    protected java.lang.String filePath;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitBuilder() {
        return android.graphics.fonts.Font_Builder_Delegate.sBuilderManager.addNewDelegate(new android.graphics.fonts.Font_Builder_Delegate());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetNativeAsset(@android.annotation.NonNull
    android.content.res.AssetManager am, @android.annotation.NonNull
    java.lang.String path, boolean isAsset, int cookie) {
        return android.graphics.fonts.Font_Builder_Delegate.sAssetManager.addNewDelegate(path);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.nio.ByteBuffer nGetAssetBuffer(long nativeAsset) {
        java.lang.String fullPath = android.graphics.fonts.Font_Builder_Delegate.sAssetManager.getDelegate(nativeAsset);
        if (fullPath == null) {
            return null;
        }
        try {
            byte[] byteArray = java.nio.file.Files.readAllBytes(new java.io.File(fullPath).toPath());
            return java.nio.ByteBuffer.wrap(byteArray);
        } catch (java.io.IOException e) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_MISSING_ASSET, "Error mapping font file " + fullPath, null, null, null);
            return null;
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseNativeAssetFunc() {
        synchronized(android.graphics.fonts.Font_Builder_Delegate.class) {
            if (android.graphics.fonts.Font_Builder_Delegate.sAssetFinalizer == (-1)) {
                android.graphics.fonts.Font_Builder_Delegate.sAssetFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.fonts.Font_Builder_Delegate.sAssetManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.fonts.Font_Builder_Delegate.sAssetFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddAxis(long builderPtr, int tag, float value) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Font$Builder.nAddAxis is not supported.", null, null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nBuild(long builderPtr, java.nio.ByteBuffer buffer, java.lang.String filePath, int weight, boolean italic, int ttcIndex) {
        android.graphics.fonts.Font_Builder_Delegate font = android.graphics.fonts.Font_Builder_Delegate.sBuilderManager.getDelegate(builderPtr);
        if (font != null) {
            font.mBuffer = buffer;
            font.mWeight = weight;
            font.mItalic = italic;
            font.mTtcIndex = ttcIndex;
            font.filePath = filePath;
        }
        return builderPtr;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseNativeFont() {
        synchronized(android.graphics.fonts.Font_Builder_Delegate.class) {
            if (android.graphics.fonts.Font_Builder_Delegate.sFontFinalizer == (-1)) {
                android.graphics.fonts.Font_Builder_Delegate.sFontFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.fonts.Font_Builder_Delegate.sBuilderManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.fonts.Font_Builder_Delegate.sFontFinalizer;
    }
}

