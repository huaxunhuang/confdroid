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
package android.support.v4.graphics.drawable;


/**
 * Constructs {@link RoundedBitmapDrawable RoundedBitmapDrawable} objects,
 * either from Bitmaps directly, or from streams and files.
 */
public final class RoundedBitmapDrawableFactory {
    private static final java.lang.String TAG = "RoundedBitmapDrawableFactory";

    private static class DefaultRoundedBitmapDrawable extends android.support.v4.graphics.drawable.RoundedBitmapDrawable {
        DefaultRoundedBitmapDrawable(android.content.res.Resources res, android.graphics.Bitmap bitmap) {
            super(res, bitmap);
        }

        @java.lang.Override
        public void setMipMap(boolean mipMap) {
            if (mBitmap != null) {
                android.support.v4.graphics.BitmapCompat.setHasMipMap(mBitmap, mipMap);
                invalidateSelf();
            }
        }

        @java.lang.Override
        public boolean hasMipMap() {
            return (mBitmap != null) && android.support.v4.graphics.BitmapCompat.hasMipMap(mBitmap);
        }

        @java.lang.Override
        void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, android.graphics.Rect bounds, android.graphics.Rect outRect) {
            android.support.v4.view.GravityCompat.apply(gravity, bitmapWidth, bitmapHeight, bounds, outRect, android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR);
        }
    }

    /**
     * Returns a new drawable by creating it from a bitmap, setting initial target density based on
     * the display metrics of the resources.
     */
    public static android.support.v4.graphics.drawable.RoundedBitmapDrawable create(android.content.res.Resources res, android.graphics.Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            return new android.support.v4.graphics.drawable.RoundedBitmapDrawable21(res, bitmap);
        }
        return new android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.DefaultRoundedBitmapDrawable(res, bitmap);
    }

    /**
     * Returns a new drawable, creating it by opening a given file path and decoding the bitmap.
     */
    public static android.support.v4.graphics.drawable.RoundedBitmapDrawable create(android.content.res.Resources res, java.lang.String filepath) {
        final android.support.v4.graphics.drawable.RoundedBitmapDrawable drawable = android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.create(res, android.graphics.BitmapFactory.decodeFile(filepath));
        if (drawable.getBitmap() == null) {
            android.util.Log.w(android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.TAG, "RoundedBitmapDrawable cannot decode " + filepath);
        }
        return drawable;
    }

    /**
     * Returns a new drawable, creating it by decoding a bitmap from the given input stream.
     */
    public static android.support.v4.graphics.drawable.RoundedBitmapDrawable create(android.content.res.Resources res, java.io.InputStream is) {
        final android.support.v4.graphics.drawable.RoundedBitmapDrawable drawable = android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.create(res, android.graphics.BitmapFactory.decodeStream(is));
        if (drawable.getBitmap() == null) {
            android.util.Log.w(android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.TAG, "RoundedBitmapDrawable cannot decode " + is);
        }
        return drawable;
    }

    private RoundedBitmapDrawableFactory() {
    }
}

