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


/* (non-Javadoc)
The class is adapted from a demo tool for Blending Modes written by
Romain Guy (romainguy@android.com). The tool is available at
http://www.curious-creature.org/2006/09/20/new-blendings-modes-for-java2d/

This class has been adapted for applying color filters. When applying color filters, the src
image should not extend beyond the dest image, but in our implementation of the filters, it does.
To compensate for the effect, we recompute the alpha value of the src image before applying
the color filter as it should have been applied.
 */
public final class BlendComposite implements java.awt.Composite {
    public enum BlendingMode {

        MULTIPLY(),
        SCREEN(),
        DARKEN(),
        LIGHTEN(),
        OVERLAY(),
        ADD();
        private final android.graphics.BlendComposite mComposite;

        BlendingMode() {
            mComposite = new android.graphics.BlendComposite(this);
        }

        android.graphics.BlendComposite getBlendComposite() {
            return mComposite;
        }
    }

    private float alpha;

    private android.graphics.BlendComposite.BlendingMode mode;

    private BlendComposite(android.graphics.BlendComposite.BlendingMode mode) {
        this(mode, 1.0F);
    }

    private BlendComposite(android.graphics.BlendComposite.BlendingMode mode, float alpha) {
        this.mode = mode;
        setAlpha(alpha);
    }

    public static android.graphics.BlendComposite getInstance(android.graphics.BlendComposite.BlendingMode mode) {
        return mode.getBlendComposite();
    }

    public static android.graphics.BlendComposite getInstance(android.graphics.BlendComposite.BlendingMode mode, float alpha) {
        if (alpha > 0.9999F) {
            return android.graphics.BlendComposite.getInstance(mode);
        }
        return new android.graphics.BlendComposite(mode, alpha);
    }

    public float getAlpha() {
        return alpha;
    }

    public android.graphics.BlendComposite.BlendingMode getMode() {
        return mode;
    }

    private void setAlpha(float alpha) {
        if ((alpha < 0.0F) || (alpha > 1.0F)) {
            assert false : "alpha must be comprised between 0.0f and 1.0f";
            alpha = java.lang.Math.min(alpha, 1.0F);
            alpha = java.lang.Math.max(alpha, 0.0F);
        }
        this.alpha = alpha;
    }

    @java.lang.Override
    public int hashCode() {
        return (java.lang.Float.floatToIntBits(alpha) * 31) + mode.ordinal();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.graphics.BlendComposite)) {
            return false;
        }
        android.graphics.BlendComposite bc = ((android.graphics.BlendComposite) (obj));
        return (mode == bc.mode) && (alpha == bc.alpha);
    }

    public java.awt.CompositeContext createContext(java.awt.image.ColorModel srcColorModel, java.awt.image.ColorModel dstColorModel, java.awt.RenderingHints hints) {
        return new android.graphics.BlendComposite.BlendingContext(this);
    }

    private static final class BlendingContext implements java.awt.CompositeContext {
        private final android.graphics.BlendComposite.Blender blender;

        private final android.graphics.BlendComposite composite;

        private BlendingContext(android.graphics.BlendComposite composite) {
            this.composite = composite;
            this.blender = android.graphics.BlendComposite.Blender.getBlenderFor(composite);
        }

        public void dispose() {
        }

        public void compose(java.awt.image.Raster src, java.awt.image.Raster dstIn, java.awt.image.WritableRaster dstOut) {
            if (((src.getSampleModel().getDataType() != java.awt.image.DataBuffer.TYPE_INT) || (dstIn.getSampleModel().getDataType() != java.awt.image.DataBuffer.TYPE_INT)) || (dstOut.getSampleModel().getDataType() != java.awt.image.DataBuffer.TYPE_INT)) {
                throw new java.lang.IllegalStateException("Source and destination must store pixels as INT.");
            }
            int width = java.lang.Math.min(src.getWidth(), dstIn.getWidth());
            int height = java.lang.Math.min(src.getHeight(), dstIn.getHeight());
            float alpha = composite.getAlpha();
            int[] srcPixel = new int[4];
            int[] dstPixel = new int[4];
            int[] result = new int[4];
            int[] srcPixels = new int[width];
            int[] dstPixels = new int[width];
            for (int y = 0; y < height; y++) {
                dstIn.getDataElements(0, y, width, 1, dstPixels);
                if (alpha != 0) {
                    src.getDataElements(0, y, width, 1, srcPixels);
                    for (int x = 0; x < width; x++) {
                        // pixels are stored as INT_ARGB
                        // our arrays are [R, G, B, A]
                        int pixel = srcPixels[x];
                        srcPixel[0] = (pixel >> 16) & 0xff;
                        srcPixel[1] = (pixel >> 8) & 0xff;
                        srcPixel[2] = pixel & 0xff;
                        srcPixel[3] = (pixel >> 24) & 0xff;
                        pixel = dstPixels[x];
                        dstPixel[0] = (pixel >> 16) & 0xff;
                        dstPixel[1] = (pixel >> 8) & 0xff;
                        dstPixel[2] = pixel & 0xff;
                        dstPixel[3] = (pixel >> 24) & 0xff;
                        // ---- Modified from original ----
                        // recompute src pixel for transparency.
                        srcPixel[3] *= dstPixel[3] / 0xff;
                        // ---- Modification ends ----
                        result = blender.blend(srcPixel, dstPixel, result);
                        // mixes the result with the opacity
                        if (alpha == 1) {
                            dstPixels[x] = ((((result[3] & 0xff) << 24) | ((result[0] & 0xff) << 16)) | ((result[1] & 0xff) << 8)) | (result[2] & 0xff);
                        } else {
                            dstPixels[x] = ((((((int) (dstPixel[3] + ((result[3] - dstPixel[3]) * alpha))) & 0xff) << 24) | ((((int) (dstPixel[0] + ((result[0] - dstPixel[0]) * alpha))) & 0xff) << 16)) | ((((int) (dstPixel[1] + ((result[1] - dstPixel[1]) * alpha))) & 0xff) << 8)) | (((int) (dstPixel[2] + ((result[2] - dstPixel[2]) * alpha))) & 0xff);
                        }
                    }
                }
                dstOut.setDataElements(0, y, width, 1, dstPixels);
            }
        }
    }

    private static abstract class Blender {
        public abstract int[] blend(int[] src, int[] dst, int[] result);

        public static android.graphics.BlendComposite.Blender getBlenderFor(android.graphics.BlendComposite composite) {
            switch (composite.getMode()) {
                case ADD :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            for (int i = 0; i < 4; i++) {
                                result[i] = java.lang.Math.min(255, src[i] + dst[i]);
                            }
                            return result;
                        }
                    };
                case DARKEN :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            for (int i = 0; i < 3; i++) {
                                result[i] = java.lang.Math.min(src[i], dst[i]);
                            }
                            result[3] = java.lang.Math.min(255, src[3] + dst[3]);
                            return result;
                        }
                    };
                case LIGHTEN :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            for (int i = 0; i < 3; i++) {
                                result[i] = java.lang.Math.max(src[i], dst[i]);
                            }
                            result[3] = java.lang.Math.min(255, src[3] + dst[3]);
                            return result;
                        }
                    };
                case MULTIPLY :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            for (int i = 0; i < 3; i++) {
                                result[i] = (src[i] * dst[i]) >> 8;
                            }
                            result[3] = java.lang.Math.min(255, (src[3] + dst[3]) - ((src[3] * dst[3]) / 255));
                            return result;
                        }
                    };
                case OVERLAY :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            for (int i = 0; i < 3; i++) {
                                result[i] = (dst[i] < 128) ? (dst[i] * src[i]) >> 7 : 255 - (((255 - dst[i]) * (255 - src[i])) >> 7);
                            }
                            result[3] = java.lang.Math.min(255, src[3] + dst[3]);
                            return result;
                        }
                    };
                case SCREEN :
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            result[0] = 255 - (((255 - src[0]) * (255 - dst[0])) >> 8);
                            result[1] = 255 - (((255 - src[1]) * (255 - dst[1])) >> 8);
                            result[2] = 255 - (((255 - src[2]) * (255 - dst[2])) >> 8);
                            result[3] = java.lang.Math.min(255, src[3] + dst[3]);
                            return result;
                        }
                    };
                default :
                    assert false : "Blender not implement for " + composite.getMode().name();
                    // Ignore the blend
                    return new android.graphics.BlendComposite.Blender() {
                        @java.lang.Override
                        public int[] blend(int[] src, int[] dst, int[] result) {
                            result[0] = dst[0];
                            result[1] = dst[1];
                            result[2] = dst[2];
                            result[3] = dst[3];
                            return result;
                        }
                    };
            }
        }
    }
}

