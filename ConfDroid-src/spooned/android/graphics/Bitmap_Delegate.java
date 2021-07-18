/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Delegate implementing the native methods of android.graphics.Bitmap
 *
 * Through the layoutlib_create tool, the original native methods of Bitmap have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Bitmap class.
 *
 * @see DelegateManager
 */
public final class Bitmap_Delegate {
    public enum BitmapCreateFlags {

        NONE,
        PREMULTIPLIED,
        MUTABLE;}

    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Bitmap_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.Bitmap_Delegate.class);

    private static long sFinalizer = -1;

    // ---- delegate helper data ----
    // ---- delegate data ----
    private final android.graphics.Bitmap.Config mConfig;

    private final java.awt.image.BufferedImage mImage;

    private boolean mHasAlpha = true;

    private boolean mHasMipMap = false;// TODO: check the default.


    private boolean mIsPremultiplied = true;

    private int mGenerationId = 0;

    private boolean mIsMutable;

    // ---- Public Helper methods ----
    /**
     * Returns the native delegate associated to a given an int referencing a {@link Bitmap} object.
     */
    public static android.graphics.Bitmap_Delegate getDelegate(long native_bitmap) {
        return android.graphics.Bitmap_Delegate.sManager.getDelegate(native_bitmap);
    }

    /**
     * Creates and returns a {@link Bitmap} initialized with the given stream content.
     *
     * @param input
     * 		the stream from which to read the bitmap content
     * @param isMutable
     * 		whether the bitmap is mutable
     * @param density
     * 		the density associated with the bitmap
     * @see Bitmap#isMutable()
     * @see Bitmap#getDensity()
     */
    public static android.graphics.Bitmap createBitmap(@android.annotation.Nullable
    java.io.InputStream input, boolean isMutable, com.android.resources.Density density) throws java.io.IOException {
        return android.graphics.Bitmap_Delegate.createBitmap(input, android.graphics.Bitmap_Delegate.getPremultipliedBitmapCreateFlags(isMutable), density);
    }

    /**
     * Creates and returns a {@link Bitmap} initialized with the given file content.
     *
     * @param input
     * 		the file from which to read the bitmap content
     * @param density
     * 		the density associated with the bitmap
     * @see Bitmap#isPremultiplied()
     * @see Bitmap#isMutable()
     * @see Bitmap#getDensity()
     */
    static android.graphics.Bitmap createBitmap(@android.annotation.Nullable
    java.io.InputStream input, java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> createFlags, com.android.resources.Density density) throws java.io.IOException {
        // create a delegate with the content of the file.
        java.awt.image.BufferedImage image = (input == null) ? null : javax.imageio.ImageIO.read(input);
        if (image == null) {
            // There was a problem decoding the image, or the decoder isn't registered. Webp maybe.
            // Replace with a broken image icon.
            com.android.layoutlib.bridge.android.BridgeContext currentContext = com.android.layoutlib.bridge.impl.RenderAction.getCurrentContext();
            if (currentContext != null) {
                com.android.ide.common.rendering.api.RenderResources resources = currentContext.getRenderResources();
                com.android.ide.common.rendering.api.ResourceValue broken = resources.getResolvedResource(com.android.layoutlib.bridge.android.BridgeContext.createFrameworkResourceReference(ResourceType.DRAWABLE, "ic_menu_report_image"));
                com.android.ide.common.rendering.api.AssetRepository assetRepository = currentContext.getAssets().getAssetRepository();
                try (java.io.InputStream stream = assetRepository.openNonAsset(0, broken.getValue(), android.content.res.AssetManager.ACCESS_STREAMING)) {
                    if (stream != null) {
                        image = javax.imageio.ImageIO.read(stream);
                    }
                }
            }
        }
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, android.graphics.Bitmap.Config.ARGB_8888);
        delegate.mIsMutable = createFlags.contains(android.graphics.Bitmap_Delegate.BitmapCreateFlags.MUTABLE);
        return android.graphics.Bitmap_Delegate.createBitmap(delegate, createFlags, density.getDpiValue());
    }

    /**
     * Creates and returns a {@link Bitmap} initialized with the given {@link BufferedImage}
     *
     * @param image
     * 		the bitmap content
     * @param isMutable
     * 		whether the bitmap is mutable
     * @param density
     * 		the density associated with the bitmap
     * @see Bitmap#isMutable()
     * @see Bitmap#getDensity()
     */
    public static android.graphics.Bitmap createBitmap(java.awt.image.BufferedImage image, boolean isMutable, com.android.resources.Density density) {
        return android.graphics.Bitmap_Delegate.createBitmap(image, android.graphics.Bitmap_Delegate.getPremultipliedBitmapCreateFlags(isMutable), density);
    }

    /**
     * Creates and returns a {@link Bitmap} initialized with the given {@link BufferedImage}
     *
     * @param image
     * 		the bitmap content
     * @param density
     * 		the density associated with the bitmap
     * @see Bitmap#isPremultiplied()
     * @see Bitmap#isMutable()
     * @see Bitmap#getDensity()
     */
    public static android.graphics.Bitmap createBitmap(java.awt.image.BufferedImage image, java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> createFlags, com.android.resources.Density density) {
        // create a delegate with the given image.
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, android.graphics.Bitmap.Config.ARGB_8888);
        delegate.mIsMutable = createFlags.contains(android.graphics.Bitmap_Delegate.BitmapCreateFlags.MUTABLE);
        return android.graphics.Bitmap_Delegate.createBitmap(delegate, createFlags, density.getDpiValue());
    }

    private static int getBufferedImageType() {
        return java.awt.image.BufferedImage.TYPE_INT_ARGB;
    }

    /**
     * Returns the {@link BufferedImage} used by the delegate of the given {@link Bitmap}.
     */
    public java.awt.image.BufferedImage getImage() {
        return mImage;
    }

    /**
     * Returns the Android bitmap config. Note that this not the config of the underlying
     * Java2D bitmap.
     */
    public android.graphics.Bitmap.Config getConfig() {
        return mConfig;
    }

    /**
     * Returns the hasAlpha rendering hint
     *
     * @return true if the bitmap alpha should be used at render time
     */
    public boolean hasAlpha() {
        return mHasAlpha && (mConfig != android.graphics.Bitmap.Config.RGB_565);
    }

    /**
     * Update the generationId.
     *
     * @see Bitmap#getGenerationId()
     */
    public void change() {
        mGenerationId++;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCreate(int[] colors, int offset, int stride, int width, int height, int nativeConfig, boolean isMutable, long nativeColorSpace) {
        int imageType = android.graphics.Bitmap_Delegate.getBufferedImageType();
        // create the image
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, imageType);
        if (colors != null) {
            image.setRGB(0, 0, width, height, colors, offset, stride);
        }
        // create a delegate with the content of the stream.
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, android.graphics.Bitmap.Config.nativeToConfig(nativeConfig));
        delegate.mIsMutable = isMutable;
        return android.graphics.Bitmap_Delegate.createBitmap(delegate, android.graphics.Bitmap_Delegate.getPremultipliedBitmapCreateFlags(isMutable), android.graphics.Bitmap.getDefaultDensity());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCopy(long srcBitmap, int nativeConfig, boolean isMutable) {
        android.graphics.Bitmap_Delegate srcBmpDelegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(srcBitmap);
        if (srcBmpDelegate == null) {
            return null;
        }
        java.awt.image.BufferedImage srcImage = srcBmpDelegate.getImage();
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int imageType = android.graphics.Bitmap_Delegate.getBufferedImageType();
        // create the image
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, imageType);
        // copy the source image into the image.
        int[] argb = new int[width * height];
        srcImage.getRGB(0, 0, width, height, argb, 0, width);
        image.setRGB(0, 0, width, height, argb, 0, width);
        // create a delegate with the content of the stream.
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, android.graphics.Bitmap.Config.nativeToConfig(nativeConfig));
        delegate.mIsMutable = isMutable;
        return android.graphics.Bitmap_Delegate.createBitmap(delegate, android.graphics.Bitmap_Delegate.getPremultipliedBitmapCreateFlags(isMutable), android.graphics.Bitmap.getDefaultDensity());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCopyAshmem(long nativeSrcBitmap) {
        // Unused method; no implementation provided.
        assert false;
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCopyAshmemConfig(long nativeSrcBitmap, int nativeConfig) {
        // Unused method; no implementation provided.
        assert false;
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeGetNativeFinalizer() {
        synchronized(android.graphics.Bitmap_Delegate.class) {
            if (android.graphics.Bitmap_Delegate.sFinalizer == (-1)) {
                android.graphics.Bitmap_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.Bitmap_Delegate.sManager::removeJavaReferenceFor);
            }
            return android.graphics.Bitmap_Delegate.sFinalizer;
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeRecycle(long nativeBitmap) {
        // In our case recycle() is a no-op. We will let the finalizer to dispose the bitmap.
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeReconfigure(long nativeBitmap, int width, int height, int config, boolean isPremultiplied) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Bitmap.reconfigure() is not supported", null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeCompress(long nativeBitmap, int format, int quality, java.io.OutputStream stream, byte[] tempStorage) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Bitmap.compress() is not supported", null);
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeErase(long nativeBitmap, int color) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        java.awt.image.BufferedImage image = delegate.mImage;
        java.awt.Graphics2D g = image.createGraphics();
        try {
            g.setColor(new java.awt.Color(color, true));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        } finally {
            g.dispose();
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeErase(long nativeBitmap, long colorSpacePtr, long color) {
        android.graphics.Bitmap_Delegate.nativeErase(nativeBitmap, android.graphics.Color.toArgb(color));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeRowBytes(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return 0;
        }
        return delegate.mImage.getWidth();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeConfig(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return 0;
        }
        return delegate.mConfig.nativeInt;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeHasAlpha(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        return (delegate == null) || delegate.mHasAlpha;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeHasMipMap(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        return (delegate == null) || delegate.mHasMipMap;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeGetPixel(long nativeBitmap, int x, int y) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return 0;
        }
        return delegate.mImage.getRGB(x, y);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeGetColor(long nativeBitmap, int x, int y) {
        return android.graphics.Bitmap_Delegate.nativeGetPixel(nativeBitmap, x, y);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeGetPixels(long nativeBitmap, int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.getImage().getRGB(x, y, width, height, pixels, offset, stride);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetPixel(long nativeBitmap, int x, int y, int color) {
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.getImage().setRGB(x, y, color);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetPixels(long nativeBitmap, int[] colors, int offset, int stride, int x, int y, int width, int height) {
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.getImage().setRGB(x, y, width, height, colors, offset, stride);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeCopyPixelsToBuffer(long nativeBitmap, java.nio.Buffer dst) {
        // FIXME implement native delegate
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Bitmap.copyPixelsToBuffer is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeCopyPixelsFromBuffer(long nb, java.nio.Buffer src) {
        // FIXME implement native delegate
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Bitmap.copyPixelsFromBuffer is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeGenerationId(long nativeBitmap) {
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return 0;
        }
        return delegate.mGenerationId;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCreateFromParcel(android.os.Parcel p) {
        // This is only called by Bitmap.CREATOR (Parcelable.Creator<Bitmap>), which is only
        // used during aidl call so really this should not be called.
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "AIDL is not suppored, and therefore Bitmaps cannot be created from parcels.", null);
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeWriteToParcel(long nativeBitmap, boolean isMutable, int density, android.os.Parcel p) {
        // This is only called when sending a bitmap through aidl, so really this should not
        // be called.
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "AIDL is not suppored, and therefore Bitmaps cannot be written to parcels.", null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeExtractAlpha(long nativeBitmap, long nativePaint, int[] offsetXY) {
        android.graphics.Bitmap_Delegate bitmap = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (bitmap == null) {
            return null;
        }
        // get the paint which can be null if nativePaint is 0.
        android.graphics.Paint_Delegate paint = android.graphics.Paint_Delegate.getDelegate(nativePaint);
        if ((paint != null) && (paint.getMaskFilter() != null)) {
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MASKFILTER, "MaskFilter not supported in Bitmap.extractAlpha", null, null);
        }
        int alpha = (paint != null) ? paint.getAlpha() : 0xff;
        java.awt.image.BufferedImage image = android.graphics.Bitmap_Delegate.createCopy(bitmap.getImage(), java.awt.image.BufferedImage.TYPE_INT_ARGB, alpha);
        // create the delegate. The actual Bitmap config is only an alpha channel
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, android.graphics.Bitmap.Config.ALPHA_8);
        delegate.mIsMutable = true;
        // the density doesn't matter, it's set by the Java method.
        return /* density */
        android.graphics.Bitmap_Delegate.createBitmap(delegate, java.util.EnumSet.of(android.graphics.Bitmap_Delegate.BitmapCreateFlags.MUTABLE), Density.DEFAULT_DENSITY);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeIsPremultiplied(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        return (delegate != null) && delegate.mIsPremultiplied;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetPremultiplied(long nativeBitmap, boolean isPremul) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.mIsPremultiplied = isPremul;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetHasAlpha(long nativeBitmap, boolean hasAlpha, boolean isPremul) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.mHasAlpha = hasAlpha;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetHasMipMap(long nativeBitmap, boolean hasMipMap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return;
        }
        delegate.mHasMipMap = hasMipMap;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeSameAs(long nb0, long nb1) {
        android.graphics.Bitmap_Delegate delegate1 = android.graphics.Bitmap_Delegate.sManager.getDelegate(nb0);
        if (delegate1 == null) {
            return false;
        }
        android.graphics.Bitmap_Delegate delegate2 = android.graphics.Bitmap_Delegate.sManager.getDelegate(nb1);
        if (delegate2 == null) {
            return false;
        }
        java.awt.image.BufferedImage image1 = delegate1.getImage();
        java.awt.image.BufferedImage image2 = delegate2.getImage();
        if (((delegate1.mConfig != delegate2.mConfig) || (image1.getWidth() != image2.getWidth())) || (image1.getHeight() != image2.getHeight())) {
            return false;
        }
        // get the internal data
        int w = image1.getWidth();
        int h = image2.getHeight();
        int[] argb1 = new int[w * h];
        int[] argb2 = new int[w * h];
        image1.getRGB(0, 0, w, h, argb1, 0, w);
        image2.getRGB(0, 0, w, h, argb2, 0, w);
        // compares
        if (delegate1.mConfig == android.graphics.Bitmap.Config.ALPHA_8) {
            // in this case we have to manually compare the alpha channel as the rest is garbage.
            final int length = w * h;
            for (int i = 0; i < length; i++) {
                if ((argb1[i] & 0xff000000) != (argb2[i] & 0xff000000)) {
                    return false;
                }
            }
            return true;
        }
        return java.util.Arrays.equals(argb1, argb2);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeGetAllocationByteCount(long nativeBitmap) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate delegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (delegate == null) {
            return 0;
        }
        int size = android.graphics.Bitmap_Delegate.nativeRowBytes(nativeBitmap) * delegate.mImage.getHeight();
        return size < 0 ? java.lang.Integer.MAX_VALUE : size;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativePrepareToDraw(long nativeBitmap) {
        // do nothing as Bitmap_Delegate does not have caches
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeCopyPreserveInternalConfig(long nativeBitmap) {
        android.graphics.Bitmap_Delegate srcBmpDelegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativeBitmap);
        if (srcBmpDelegate == null) {
            return null;
        }
        java.awt.image.BufferedImage srcImage = srcBmpDelegate.getImage();
        // create the image
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(srcImage.getColorModel(), srcImage.copyData(null), srcImage.isAlphaPremultiplied(), null);
        // create a delegate with the content of the stream.
        android.graphics.Bitmap_Delegate delegate = new android.graphics.Bitmap_Delegate(image, srcBmpDelegate.getConfig());
        delegate.mIsMutable = srcBmpDelegate.mIsMutable;
        return android.graphics.Bitmap_Delegate.createBitmap(delegate, java.util.EnumSet.of(android.graphics.Bitmap_Delegate.BitmapCreateFlags.NONE), android.graphics.Bitmap.getDefaultDensity());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeWrapHardwareBufferBitmap(android.hardware.HardwareBuffer buffer, long nativeColorSpace) {
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Bitmap.nativeWrapHardwareBufferBitmap() is not supported", null, null, null);
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.GraphicBuffer nativeCreateGraphicBufferHandle(long nativeBitmap) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Bitmap.nativeCreateGraphicBufferHandle() is not supported", null);
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeIsSRGB(long nativeBitmap) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Color spaces are not supported", null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.ColorSpace nativeComputeColorSpace(long nativePtr) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Color spaces are not supported", null);
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetColorSpace(long nativePtr, long nativeColorSpace) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Color spaces are not supported", null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeIsSRGBLinear(long nativePtr) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_UNSUPPORTED, "Color spaces are not supported", null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetImmutable(long nativePtr) {
        android.graphics.Bitmap_Delegate bmpDelegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativePtr);
        if (bmpDelegate == null) {
            return;
        }
        bmpDelegate.mIsMutable = false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeIsImmutable(long nativePtr) {
        android.graphics.Bitmap_Delegate bmpDelegate = android.graphics.Bitmap_Delegate.sManager.getDelegate(nativePtr);
        if (bmpDelegate == null) {
            return false;
        }
        return !bmpDelegate.mIsMutable;
    }

    // ---- Private delegate/helper methods ----
    private Bitmap_Delegate(java.awt.image.BufferedImage image, android.graphics.Bitmap.Config config) {
        mImage = image;
        mConfig = config;
    }

    private static android.graphics.Bitmap createBitmap(android.graphics.Bitmap_Delegate delegate, java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> createFlags, int density) {
        // get its native_int
        long nativeInt = android.graphics.Bitmap_Delegate.sManager.addNewDelegate(delegate);
        int width = delegate.mImage.getWidth();
        int height = delegate.mImage.getHeight();
        boolean isPremultiplied = createFlags.contains(android.graphics.Bitmap_Delegate.BitmapCreateFlags.PREMULTIPLIED);
        // and create/return a new Bitmap with it
        return /* ninePatchChunk */
        /* layoutBounds */
        /* fromMalloc */
        new android.graphics.Bitmap(nativeInt, width, height, density, isPremultiplied, null, null, true);
    }

    private static java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> getPremultipliedBitmapCreateFlags(boolean isMutable) {
        java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> createFlags = java.util.EnumSet.of(android.graphics.Bitmap_Delegate.BitmapCreateFlags.PREMULTIPLIED);
        if (isMutable) {
            createFlags.add(android.graphics.Bitmap_Delegate.BitmapCreateFlags.MUTABLE);
        }
        return createFlags;
    }

    /**
     * Creates and returns a copy of a given BufferedImage.
     * <p/>
     * if alpha is different than 255, then it is applied to the alpha channel of each pixel.
     *
     * @param image
     * 		the image to copy
     * @param imageType
     * 		the type of the new image
     * @param alpha
     * 		an optional alpha modifier
     * @return a new BufferedImage
     */
    /* package */
    static java.awt.image.BufferedImage createCopy(java.awt.image.BufferedImage image, int imageType, int alpha) {
        int w = image.getWidth();
        int h = image.getHeight();
        java.awt.image.BufferedImage result = new java.awt.image.BufferedImage(w, h, imageType);
        int[] argb = new int[w * h];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), argb, 0, image.getWidth());
        if (alpha != 255) {
            final int length = argb.length;
            for (int i = 0; i < length; i++) {
                int a = (argb[i] >>> (24 * alpha)) / 255;
                argb[i] = (a << 24) | (argb[i] & 0xffffff);
            }
        }
        result.setRGB(0, 0, w, h, argb, 0, w);
        return result;
    }
}

