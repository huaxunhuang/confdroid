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
package android.filterfw.format;


/**
 *
 *
 * @unknown 
 */
public class ImageFormat {
    public static final java.lang.String COLORSPACE_KEY = "colorspace";

    public static final int COLORSPACE_GRAY = 1;

    public static final int COLORSPACE_RGB = 2;

    public static final int COLORSPACE_RGBA = 3;

    public static final int COLORSPACE_YUV = 4;

    public static android.filterfw.core.MutableFrameFormat create(int width, int height, int colorspace, int bytesPerSample, int target) {
        android.filterfw.core.MutableFrameFormat result = new android.filterfw.core.MutableFrameFormat(android.filterfw.core.FrameFormat.TYPE_BYTE, target);
        result.setDimensions(width, height);
        result.setBytesPerSample(bytesPerSample);
        result.setMetaValue(android.filterfw.format.ImageFormat.COLORSPACE_KEY, colorspace);
        if (target == android.filterfw.core.FrameFormat.TARGET_SIMPLE) {
            result.setObjectClass(android.graphics.Bitmap.class);
        }
        return result;
    }

    public static android.filterfw.core.MutableFrameFormat create(int width, int height, int colorspace, int target) {
        return android.filterfw.format.ImageFormat.create(width, height, colorspace, android.filterfw.format.ImageFormat.bytesPerSampleForColorspace(colorspace), target);
    }

    public static android.filterfw.core.MutableFrameFormat create(int colorspace, int target) {
        return android.filterfw.format.ImageFormat.create(android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, colorspace, android.filterfw.format.ImageFormat.bytesPerSampleForColorspace(colorspace), target);
    }

    public static android.filterfw.core.MutableFrameFormat create(int colorspace) {
        return android.filterfw.format.ImageFormat.create(android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, android.filterfw.core.FrameFormat.SIZE_UNSPECIFIED, colorspace, android.filterfw.format.ImageFormat.bytesPerSampleForColorspace(colorspace), android.filterfw.core.FrameFormat.TARGET_UNSPECIFIED);
    }

    public static int bytesPerSampleForColorspace(int colorspace) {
        switch (colorspace) {
            case android.filterfw.format.ImageFormat.COLORSPACE_GRAY :
                return 1;
            case android.filterfw.format.ImageFormat.COLORSPACE_RGB :
                return 3;
            case android.filterfw.format.ImageFormat.COLORSPACE_RGBA :
                return 4;
            case android.filterfw.format.ImageFormat.COLORSPACE_YUV :
                return 3;
            default :
                throw new java.lang.RuntimeException(("Unknown colorspace id " + colorspace) + "!");
        }
    }
}

