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
package android.graphics;


/**
 *
 *
 * @deprecated Prefer {@link android.graphics.drawable.AnimatedImageDrawable}.
 */
@java.lang.Deprecated
public class Movie {
    @android.annotation.UnsupportedAppUsage
    private long mNativeMovie;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private Movie(long nativeMovie) {
        if (nativeMovie == 0) {
            throw new java.lang.RuntimeException("native movie creation failed");
        }
        mNativeMovie = nativeMovie;
    }

    public native int width();

    public native int height();

    public native boolean isOpaque();

    public native int duration();

    public native boolean setTime(int relativeMilliseconds);

    private native void nDraw(long nativeCanvas, float x, float y, long paintHandle);

    public void draw(android.graphics.Canvas canvas, float x, float y, android.graphics.Paint paint) {
        nDraw(canvas.getNativeCanvasWrapper(), x, y, paint != null ? paint.getNativeInstance() : 0);
    }

    public void draw(android.graphics.Canvas canvas, float x, float y) {
        nDraw(canvas.getNativeCanvasWrapper(), x, y, 0);
    }

    public static android.graphics.Movie decodeStream(java.io.InputStream is) {
        if (is == null) {
            return null;
        }
        if (is instanceof android.content.res.AssetManager.AssetInputStream) {
            final long asset = ((android.content.res.AssetManager.AssetInputStream) (is)).getNativeAsset();
            return android.graphics.Movie.nativeDecodeAsset(asset);
        }
        return android.graphics.Movie.nativeDecodeStream(is);
    }

    private static native android.graphics.Movie nativeDecodeAsset(long asset);

    private static native android.graphics.Movie nativeDecodeStream(java.io.InputStream is);

    public static native android.graphics.Movie decodeByteArray(byte[] data, int offset, int length);

    private static native void nativeDestructor(long nativeMovie);

    public static android.graphics.Movie decodeFile(java.lang.String pathName) {
        java.io.InputStream is;
        try {
            is = new java.io.FileInputStream(pathName);
        } catch (java.io.FileNotFoundException e) {
            return null;
        }
        return android.graphics.Movie.decodeTempStream(is);
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            android.graphics.Movie.nativeDestructor(mNativeMovie);
            mNativeMovie = 0;
        } finally {
            super.finalize();
        }
    }

    private static android.graphics.Movie decodeTempStream(java.io.InputStream is) {
        android.graphics.Movie moov = null;
        try {
            moov = android.graphics.Movie.decodeStream(is);
            is.close();
        } catch (java.io.IOException e) {
            /* do nothing.
            If the exception happened on open, moov will be null.
            If it happened on close, moov is still valid.
             */
        }
        return moov;
    }
}

