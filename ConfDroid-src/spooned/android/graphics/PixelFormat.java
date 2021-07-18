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


public class PixelFormat {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.graphics.PixelFormat.UNKNOWN, android.graphics.PixelFormat.TRANSLUCENT, android.graphics.PixelFormat.TRANSPARENT, android.graphics.PixelFormat.OPAQUE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Opacity {}

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.graphics.PixelFormat.RGBA_8888, android.graphics.PixelFormat.RGBX_8888, android.graphics.PixelFormat.RGBA_F16, android.graphics.PixelFormat.RGBA_1010102, android.graphics.PixelFormat.RGB_888, android.graphics.PixelFormat.RGB_565 })
    public @interface Format {}

    // NOTE: these constants must match the values from graphics/common/x.x/types.hal
    public static final int UNKNOWN = 0;

    /**
     * System chooses a format that supports translucency (many alpha bits)
     */
    public static final int TRANSLUCENT = -3;

    /**
     * System chooses a format that supports transparency
     * (at least 1 alpha bit)
     */
    public static final int TRANSPARENT = -2;

    /**
     * System chooses an opaque format (no alpha bits required)
     */
    public static final int OPAQUE = -1;

    public static final int RGBA_8888 = 1;

    public static final int RGBX_8888 = 2;

    public static final int RGB_888 = 3;

    public static final int RGB_565 = 4;

    @java.lang.Deprecated
    public static final int RGBA_5551 = 6;

    @java.lang.Deprecated
    public static final int RGBA_4444 = 7;

    @java.lang.Deprecated
    public static final int A_8 = 8;

    @java.lang.Deprecated
    public static final int L_8 = 9;

    @java.lang.Deprecated
    public static final int LA_88 = 0xa;

    @java.lang.Deprecated
    public static final int RGB_332 = 0xb;

    /**
     *
     *
     * @deprecated use {@link android.graphics.ImageFormat#NV16
    ImageFormat.NV16} instead.
     */
    @java.lang.Deprecated
    public static final int YCbCr_422_SP = 0x10;

    /**
     *
     *
     * @deprecated use {@link android.graphics.ImageFormat#NV21
    ImageFormat.NV21} instead.
     */
    @java.lang.Deprecated
    public static final int YCbCr_420_SP = 0x11;

    /**
     *
     *
     * @deprecated use {@link android.graphics.ImageFormat#YUY2
    ImageFormat.YUY2} instead.
     */
    @java.lang.Deprecated
    public static final int YCbCr_422_I = 0x14;

    public static final int RGBA_F16 = 0x16;

    public static final int RGBA_1010102 = 0x2b;

    /**
     *
     *
     * @unknown 
     */
    public static final int HSV_888 = 0x37;

    /**
     *
     *
     * @deprecated use {@link android.graphics.ImageFormat#JPEG
    ImageFormat.JPEG} instead.
     */
    @java.lang.Deprecated
    public static final int JPEG = 0x100;

    public int bytesPerPixel;

    public int bitsPerPixel;

    public static void getPixelFormatInfo(@android.graphics.PixelFormat.Format
    int format, android.graphics.PixelFormat info) {
        switch (format) {
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBX_8888 :
            case android.graphics.PixelFormat.RGBA_1010102 :
                info.bitsPerPixel = 32;
                info.bytesPerPixel = 4;
                break;
            case android.graphics.PixelFormat.RGB_888 :
            case android.graphics.PixelFormat.HSV_888 :
                info.bitsPerPixel = 24;
                info.bytesPerPixel = 3;
                break;
            case android.graphics.PixelFormat.RGB_565 :
            case android.graphics.PixelFormat.RGBA_5551 :
            case android.graphics.PixelFormat.RGBA_4444 :
            case android.graphics.PixelFormat.LA_88 :
                info.bitsPerPixel = 16;
                info.bytesPerPixel = 2;
                break;
            case android.graphics.PixelFormat.A_8 :
            case android.graphics.PixelFormat.L_8 :
            case android.graphics.PixelFormat.RGB_332 :
                info.bitsPerPixel = 8;
                info.bytesPerPixel = 1;
                break;
            case android.graphics.PixelFormat.YCbCr_422_SP :
            case android.graphics.PixelFormat.YCbCr_422_I :
                info.bitsPerPixel = 16;
                info.bytesPerPixel = 1;
                break;
            case android.graphics.PixelFormat.YCbCr_420_SP :
                info.bitsPerPixel = 12;
                info.bytesPerPixel = 1;
                break;
            case android.graphics.PixelFormat.RGBA_F16 :
                info.bitsPerPixel = 64;
                info.bytesPerPixel = 8;
                break;
            default :
                throw new java.lang.IllegalArgumentException("unknown pixel format " + format);
        }
    }

    public static boolean formatHasAlpha(@android.graphics.PixelFormat.Format
    int format) {
        switch (format) {
            case android.graphics.PixelFormat.A_8 :
            case android.graphics.PixelFormat.LA_88 :
            case android.graphics.PixelFormat.RGBA_4444 :
            case android.graphics.PixelFormat.RGBA_5551 :
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBA_F16 :
            case android.graphics.PixelFormat.RGBA_1010102 :
            case android.graphics.PixelFormat.TRANSLUCENT :
            case android.graphics.PixelFormat.TRANSPARENT :
                return true;
        }
        return false;
    }

    /**
     * Determine whether or not this is a public-visible and non-deprecated {@code format}.
     *
     * <p>In particular, {@code @hide} formats will return {@code false}.</p>
     *
     * <p>Any other indirect formats (such as {@code TRANSPARENT} or {@code TRANSLUCENT})
     * will return {@code false}.</p>
     *
     * @param format
     * 		an integer format
     * @return a boolean
     * @unknown 
     */
    public static boolean isPublicFormat(@android.graphics.PixelFormat.Format
    int format) {
        switch (format) {
            case android.graphics.PixelFormat.RGBA_8888 :
            case android.graphics.PixelFormat.RGBX_8888 :
            case android.graphics.PixelFormat.RGB_888 :
            case android.graphics.PixelFormat.RGB_565 :
            case android.graphics.PixelFormat.RGBA_F16 :
            case android.graphics.PixelFormat.RGBA_1010102 :
                return true;
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String formatToString(@android.graphics.PixelFormat.Format
    int format) {
        switch (format) {
            case android.graphics.PixelFormat.UNKNOWN :
                return "UNKNOWN";
            case android.graphics.PixelFormat.TRANSLUCENT :
                return "TRANSLUCENT";
            case android.graphics.PixelFormat.TRANSPARENT :
                return "TRANSPARENT";
            case android.graphics.PixelFormat.RGBA_8888 :
                return "RGBA_8888";
            case android.graphics.PixelFormat.RGBX_8888 :
                return "RGBX_8888";
            case android.graphics.PixelFormat.RGB_888 :
                return "RGB_888";
            case android.graphics.PixelFormat.RGB_565 :
                return "RGB_565";
            case android.graphics.PixelFormat.RGBA_5551 :
                return "RGBA_5551";
            case android.graphics.PixelFormat.RGBA_4444 :
                return "RGBA_4444";
            case android.graphics.PixelFormat.A_8 :
                return "A_8";
            case android.graphics.PixelFormat.L_8 :
                return "L_8";
            case android.graphics.PixelFormat.LA_88 :
                return "LA_88";
            case android.graphics.PixelFormat.RGB_332 :
                return "RGB_332";
            case android.graphics.PixelFormat.YCbCr_422_SP :
                return "YCbCr_422_SP";
            case android.graphics.PixelFormat.YCbCr_420_SP :
                return "YCbCr_420_SP";
            case android.graphics.PixelFormat.YCbCr_422_I :
                return "YCbCr_422_I";
            case android.graphics.PixelFormat.RGBA_F16 :
                return "RGBA_F16";
            case android.graphics.PixelFormat.RGBA_1010102 :
                return "RGBA_1010102";
            case android.graphics.PixelFormat.HSV_888 :
                return "HSV_888";
            case android.graphics.PixelFormat.JPEG :
                return "JPEG";
            default :
                return java.lang.Integer.toString(format);
        }
    }
}

