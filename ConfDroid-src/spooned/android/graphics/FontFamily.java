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
package android.graphics;


/**
 * A family of typefaces with different styles.
 *
 * @unknown 
 * @deprecated Use {@link android.graphics.fonts.FontFamily} instead.
 */
@java.lang.Deprecated
public class FontFamily {
    private static java.lang.String TAG = "FontFamily";

    private static final libcore.util.NativeAllocationRegistry sBuilderRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.FontFamily.class.getClassLoader(), android.graphics.FontFamily.nGetBuilderReleaseFunc());

    @android.annotation.Nullable
    private java.lang.Runnable mNativeBuilderCleaner;

    private static final libcore.util.NativeAllocationRegistry sFamilyRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.FontFamily.class.getClassLoader(), android.graphics.FontFamily.nGetFamilyReleaseFunc());

    /**
     *
     *
     * @unknown This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public long mNativePtr;

    // Points native font family builder. Must be zero after freezing this family.
    private long mBuilderPtr;

    /**
     * This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public FontFamily() {
        mBuilderPtr = android.graphics.FontFamily.nInitBuilder(null, 0);
        mNativeBuilderCleaner = android.graphics.FontFamily.sBuilderRegistry.registerNativeAllocation(this, mBuilderPtr);
    }

    /**
     * This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public FontFamily(@android.annotation.Nullable
    java.lang.String[] langs, int variant) {
        final java.lang.String langsString;
        if ((langs == null) || (langs.length == 0)) {
            langsString = null;
        } else
            if (langs.length == 1) {
                langsString = langs[0];
            } else {
                langsString = android.text.TextUtils.join(",", langs);
            }

        mBuilderPtr = android.graphics.FontFamily.nInitBuilder(langsString, variant);
        mNativeBuilderCleaner = android.graphics.FontFamily.sBuilderRegistry.registerNativeAllocation(this, mBuilderPtr);
    }

    /**
     * Finalize the FontFamily creation.
     *
     * @return boolean returns false if some error happens in native code, e.g. broken font file is
    passed, etc.

    This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public boolean freeze() {
        if (mBuilderPtr == 0) {
            throw new java.lang.IllegalStateException("This FontFamily is already frozen");
        }
        mNativePtr = android.graphics.FontFamily.nCreateFamily(mBuilderPtr);
        mNativeBuilderCleaner.run();
        mBuilderPtr = 0;
        if (mNativePtr != 0) {
            android.graphics.FontFamily.sFamilyRegistry.registerNativeAllocation(this, mNativePtr);
        }
        return mNativePtr != 0;
    }

    /**
     * This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public void abortCreation() {
        if (mBuilderPtr == 0) {
            throw new java.lang.IllegalStateException("This FontFamily is already frozen or abandoned");
        }
        mNativeBuilderCleaner.run();
        mBuilderPtr = 0;
    }

    /**
     * This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFont(java.lang.String path, int ttcIndex, android.graphics.fonts.FontVariationAxis[] axes, int weight, int italic) {
        if (mBuilderPtr == 0) {
            throw new java.lang.IllegalStateException("Unable to call addFont after freezing.");
        }
        try (java.io.FileInputStream file = new java.io.FileInputStream(path)) {
            java.nio.channels.FileChannel fileChannel = file.getChannel();
            long fontSize = fileChannel.size();
            java.nio.ByteBuffer fontBuffer = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fontSize);
            if (axes != null) {
                for (android.graphics.fonts.FontVariationAxis axis : axes) {
                    android.graphics.FontFamily.nAddAxisValue(mBuilderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
                }
            }
            return android.graphics.FontFamily.nAddFont(mBuilderPtr, fontBuffer, ttcIndex, weight, italic);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.graphics.FontFamily.TAG, "Error mapping font file " + path);
            return false;
        }
    }

    /**
     * This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFontFromBuffer(java.nio.ByteBuffer font, int ttcIndex, android.graphics.fonts.FontVariationAxis[] axes, int weight, int italic) {
        if (mBuilderPtr == 0) {
            throw new java.lang.IllegalStateException("Unable to call addFontWeightStyle after freezing.");
        }
        if (axes != null) {
            for (android.graphics.fonts.FontVariationAxis axis : axes) {
                android.graphics.FontFamily.nAddAxisValue(mBuilderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
            }
        }
        return android.graphics.FontFamily.nAddFontWeightStyle(mBuilderPtr, font, ttcIndex, weight, italic);
    }

    /**
     *
     *
     * @param mgr
     * 		The AssetManager to use for this context.
     * @param path
     * 		The path to the font file to load.
     * @param cookie
     * 		If available, the resource cookie given by Resources.
     * @param isAsset
     * 		{@code true} if this is from the assets/ folder, {@code false} if from
     * 		resources
     * @param weight
     * 		The weight of the font. If 0 is given, the weight and italic will be resolved
     * 		using the OS/2 table in the font.
     * @param isItalic
     * 		Whether this font is italic. If the weight is set to 0, this will be resolved
     * 		using the OS/2 table in the font.
     * @return This cannot be deleted because it's in use by AndroidX.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    public boolean addFontFromAssetManager(android.content.res.AssetManager mgr, java.lang.String path, int cookie, boolean isAsset, int ttcIndex, int weight, int isItalic, android.graphics.fonts.FontVariationAxis[] axes) {
        if (mBuilderPtr == 0) {
            throw new java.lang.IllegalStateException("Unable to call addFontFromAsset after freezing.");
        }
        if (axes != null) {
            for (android.graphics.fonts.FontVariationAxis axis : axes) {
                android.graphics.FontFamily.nAddAxisValue(mBuilderPtr, axis.getOpenTypeTagValue(), axis.getStyleValue());
            }
        }
        return android.graphics.FontFamily.nAddFontFromAssetManager(mBuilderPtr, mgr, path, cookie, isAsset, ttcIndex, weight, isItalic);
    }

    // TODO: Remove once internal user stop using private API.
    private static boolean nAddFont(long builderPtr, java.nio.ByteBuffer font, int ttcIndex) {
        return android.graphics.FontFamily.nAddFont(builderPtr, font, ttcIndex, -1, -1);
    }

    private static native long nInitBuilder(java.lang.String langs, int variant);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nCreateFamily(long mBuilderPtr);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nGetBuilderReleaseFunc();

    @dalvik.annotation.optimization.CriticalNative
    private static native long nGetFamilyReleaseFunc();

    // By passing -1 to weigth argument, the weight value is resolved by OS/2 table in the font.
    // By passing -1 to italic argument, the italic value is resolved by OS/2 table in the font.
    private static native boolean nAddFont(long builderPtr, java.nio.ByteBuffer font, int ttcIndex, int weight, int isItalic);

    private static native boolean nAddFontWeightStyle(long builderPtr, java.nio.ByteBuffer font, int ttcIndex, int weight, int isItalic);

    private static native boolean nAddFontFromAssetManager(long builderPtr, android.content.res.AssetManager mgr, java.lang.String path, int cookie, boolean isAsset, int ttcIndex, int weight, int isItalic);

    // The added axis values are only valid for the next nAddFont* method call.
    @dalvik.annotation.optimization.CriticalNative
    private static native void nAddAxisValue(long builderPtr, int tag, float value);
}

