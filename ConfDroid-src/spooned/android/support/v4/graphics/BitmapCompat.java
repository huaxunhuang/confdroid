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
package android.support.v4.graphics;


/**
 * Helper for accessing features in {@link android.graphics.Bitmap}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class BitmapCompat {
    /**
     * Interface for the full API.
     */
    interface BitmapImpl {
        public boolean hasMipMap(android.graphics.Bitmap bitmap);

        public void setHasMipMap(android.graphics.Bitmap bitmap, boolean hasMipMap);

        public int getAllocationByteCount(android.graphics.Bitmap bitmap);
    }

    static class BaseBitmapImpl implements android.support.v4.graphics.BitmapCompat.BitmapImpl {
        @java.lang.Override
        public boolean hasMipMap(android.graphics.Bitmap bitmap) {
            return false;
        }

        @java.lang.Override
        public void setHasMipMap(android.graphics.Bitmap bitmap, boolean hasMipMap) {
        }

        @java.lang.Override
        public int getAllocationByteCount(android.graphics.Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    static class HcMr1BitmapCompatImpl extends android.support.v4.graphics.BitmapCompat.BaseBitmapImpl {
        @java.lang.Override
        public int getAllocationByteCount(android.graphics.Bitmap bitmap) {
            return android.support.v4.graphics.BitmapCompatHoneycombMr1.getAllocationByteCount(bitmap);
        }
    }

    static class JbMr2BitmapCompatImpl extends android.support.v4.graphics.BitmapCompat.HcMr1BitmapCompatImpl {
        @java.lang.Override
        public boolean hasMipMap(android.graphics.Bitmap bitmap) {
            return android.support.v4.graphics.BitmapCompatJellybeanMR2.hasMipMap(bitmap);
        }

        @java.lang.Override
        public void setHasMipMap(android.graphics.Bitmap bitmap, boolean hasMipMap) {
            android.support.v4.graphics.BitmapCompatJellybeanMR2.setHasMipMap(bitmap, hasMipMap);
        }
    }

    static class KitKatBitmapCompatImpl extends android.support.v4.graphics.BitmapCompat.JbMr2BitmapCompatImpl {
        @java.lang.Override
        public int getAllocationByteCount(android.graphics.Bitmap bitmap) {
            return android.support.v4.graphics.BitmapCompatKitKat.getAllocationByteCount(bitmap);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.graphics.BitmapCompat.BitmapImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 19) {
            IMPL = new android.support.v4.graphics.BitmapCompat.KitKatBitmapCompatImpl();
        } else
            if (version >= 18) {
                IMPL = new android.support.v4.graphics.BitmapCompat.JbMr2BitmapCompatImpl();
            } else
                if (version >= 12) {
                    IMPL = new android.support.v4.graphics.BitmapCompat.HcMr1BitmapCompatImpl();
                } else {
                    IMPL = new android.support.v4.graphics.BitmapCompat.BaseBitmapImpl();
                }


    }

    public static boolean hasMipMap(android.graphics.Bitmap bitmap) {
        return android.support.v4.graphics.BitmapCompat.IMPL.hasMipMap(bitmap);
    }

    public static void setHasMipMap(android.graphics.Bitmap bitmap, boolean hasMipMap) {
        android.support.v4.graphics.BitmapCompat.IMPL.setHasMipMap(bitmap, hasMipMap);
    }

    /**
     * Returns the size of the allocated memory used to store this bitmap's pixels in a backwards
     * compatible way.
     *
     * @param bitmap
     * 		the bitmap in which to return it's allocation size
     * @return the allocation size in bytes
     */
    public static int getAllocationByteCount(android.graphics.Bitmap bitmap) {
        return android.support.v4.graphics.BitmapCompat.IMPL.getAllocationByteCount(bitmap);
    }

    private BitmapCompat() {
    }
}

