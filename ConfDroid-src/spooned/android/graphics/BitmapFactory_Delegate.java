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
package android.graphics;


/**
 * Delegate implementing the native methods of android.graphics.BitmapFactory
 *
 * Through the layoutlib_create tool, the original native methods of BitmapFactory have been
 * replaced by calls to methods of the same name in this delegate class.
 *
 * Because it's a stateless class to start with, there's no need to keep a {@link DelegateManager}
 * around to map int to instance of the delegate.
 */
/* package */
class BitmapFactory_Delegate {
    // ------ Native Delegates ------
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeDecodeStream(java.io.InputStream is, byte[] storage, @android.annotation.Nullable
    android.graphics.Rect padding, @android.annotation.Nullable
    android.graphics.BitmapFactory.Options opts, long inBitmapHandle, long colorSpaceHandle) {
        android.graphics.Bitmap bm = null;
        com.android.resources.Density density = com.android.resources.Density.MEDIUM;
        java.util.Set<android.graphics.Bitmap_Delegate.BitmapCreateFlags> bitmapCreateFlags = java.util.EnumSet.of(android.graphics.Bitmap_Delegate.BitmapCreateFlags.MUTABLE);
        if (opts != null) {
            density = com.android.resources.Density.getEnum(opts.inDensity);
            if (opts.inPremultiplied) {
                bitmapCreateFlags.add(android.graphics.Bitmap_Delegate.BitmapCreateFlags.PREMULTIPLIED);
            }
            opts.inScaled = false;
        }
        try {
            if (is instanceof com.android.layoutlib.bridge.util.NinePatchInputStream) {
                com.android.layoutlib.bridge.util.NinePatchInputStream npis = ((com.android.layoutlib.bridge.util.NinePatchInputStream) (is));
                npis.disableFakeMarkSupport();
                // load the bitmap as a nine patch
                com.android.ninepatch.NinePatch ninePatch = /* is9Patch */
                /* convert */
                android.graphics.com.android.ninepatch.NinePatch.load(npis, true, false);
                // get the bitmap and chunk objects.
                bm = android.graphics.Bitmap_Delegate.createBitmap(ninePatch.getImage(), bitmapCreateFlags, density);
                com.android.ninepatch.NinePatchChunk chunk = ninePatch.getChunk();
                // put the chunk in the bitmap
                bm.setNinePatchChunk(android.graphics.NinePatch_Delegate.serialize(chunk));
                if (padding != null) {
                    // read the padding
                    int[] paddingArray = chunk.getPadding();
                    padding.left = paddingArray[0];
                    padding.top = paddingArray[1];
                    padding.right = paddingArray[2];
                    padding.bottom = paddingArray[3];
                }
            } else {
                // load the bitmap directly.
                bm = android.graphics.Bitmap_Delegate.createBitmap(is, bitmapCreateFlags, density);
            }
        } catch (java.io.IOException e) {
            com.android.layoutlib.bridge.Bridge.getLog().error(null, "Failed to load image", e, null);
        }
        return bm;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeDecodeFileDescriptor(java.io.FileDescriptor fd, android.graphics.Rect padding, android.graphics.BitmapFactory.Options opts, long inBitmapHandle, long colorSpaceHandle) {
        if (opts != null) {
            opts.inBitmap = null;
        }
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeDecodeAsset(long asset, android.graphics.Rect padding, android.graphics.BitmapFactory.Options opts, long inBitmapHandle, long colorSpaceHandle) {
        if (opts != null) {
            opts.inBitmap = null;
        }
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Bitmap nativeDecodeByteArray(byte[] data, int offset, int length, android.graphics.BitmapFactory.Options opts, long inBitmapHandle, long colorSpaceHandle) {
        if (opts != null) {
            opts.inBitmap = null;
        }
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nativeIsSeekable(java.io.FileDescriptor fd) {
        return true;
    }

    /**
     * Set the newly decoded bitmap's density based on the Options.
     *
     * Copied from {@link BitmapFactory#setDensityFromOptions(Bitmap, Options)}.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void setDensityFromOptions(android.graphics.Bitmap outputBitmap, android.graphics.BitmapFactory.Options opts) {
        if ((outputBitmap == null) || (opts == null))
            return;

        final int density = opts.inDensity;
        if (density != 0) {
            outputBitmap.setDensity(density);
            final int targetDensity = opts.inTargetDensity;
            if (((targetDensity == 0) || (density == targetDensity)) || (density == opts.inScreenDensity)) {
                return;
            }
            // --- Change from original implementation begins ---
            // LayoutLib doesn't scale the nine patch when decoding it. Hence, don't change the
            // density of the source bitmap in case of ninepatch.
            if (opts.inScaled) {
                // --- Change from original implementation ends. ---
                outputBitmap.setDensity(targetDensity);
            }
        } else
            if (opts.inBitmap != null) {
                // bitmap was reused, ensure density is reset
                outputBitmap.setDensity(android.graphics.Bitmap.getDefaultDensity());
            }

    }
}

