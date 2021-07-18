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
 * Delegate implementing the native methods of android.graphics.BitmapShader
 *
 * Through the layoutlib_create tool, the original native methods of BitmapShader have been
 * replaced by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original BitmapShader class.
 *
 * Because this extends {@link Shader_Delegate}, there's no need to use a {@link DelegateManager},
 * as all the Shader classes will be added to the manager owned by {@link Shader_Delegate}.
 *
 * @see Shader_Delegate
 */
public class BitmapShader_Delegate extends android.graphics.Shader_Delegate {
    // ---- delegate data ----
    private java.awt.Paint mJavaPaint;

    // ---- Public Helper methods ----
    @java.lang.Override
    public java.awt.Paint getJavaPaint() {
        return mJavaPaint;
    }

    @java.lang.Override
    public boolean isSupported() {
        return true;
    }

    @java.lang.Override
    public java.lang.String getSupportMessage() {
        // no message since isSupported returns true;
        return null;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreate(long nativeMatrix, long bitmapHandle, int shaderTileModeX, int shaderTileModeY) {
        android.graphics.Bitmap_Delegate bitmap = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
        if (bitmap == null) {
            return 0;
        }
        android.graphics.BitmapShader_Delegate newDelegate = new android.graphics.BitmapShader_Delegate(nativeMatrix, bitmap.getImage(), android.graphics.Shader_Delegate.getTileMode(shaderTileModeX), android.graphics.Shader_Delegate.getTileMode(shaderTileModeY));
        return android.graphics.Shader_Delegate.sManager.addNewDelegate(newDelegate);
    }

    // ---- Private delegate/helper methods ----
    private BitmapShader_Delegate(long matrix, java.awt.image.BufferedImage image, android.graphics.Shader.TileMode tileModeX, android.graphics.Shader.TileMode tileModeY) {
        super(matrix);
        mJavaPaint = new android.graphics.BitmapShader_Delegate.BitmapShaderPaint(image, tileModeX, tileModeY);
    }

    private class BitmapShaderPaint implements java.awt.Paint {
        private final java.awt.image.BufferedImage mImage;

        private final android.graphics.Shader.TileMode mTileModeX;

        private final android.graphics.Shader.TileMode mTileModeY;

        BitmapShaderPaint(java.awt.image.BufferedImage image, android.graphics.Shader.TileMode tileModeX, android.graphics.Shader.TileMode tileModeY) {
            mImage = image;
            mTileModeX = tileModeX;
            mTileModeY = tileModeY;
        }

        @java.lang.Override
        public java.awt.PaintContext createContext(java.awt.image.ColorModel colorModel, java.awt.Rectangle deviceBounds, java.awt.geom.Rectangle2D userBounds, java.awt.geom.AffineTransform xform, java.awt.RenderingHints hints) {
            java.awt.geom.AffineTransform canvasMatrix;
            try {
                canvasMatrix = xform.createInverse();
            } catch (java.awt.geom.NoninvertibleTransformException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MATRIX_INVERSE, "Unable to inverse matrix in BitmapShader", e, null);
                canvasMatrix = new java.awt.geom.AffineTransform();
            }
            java.awt.geom.AffineTransform localMatrix = getLocalMatrix();
            try {
                localMatrix = localMatrix.createInverse();
            } catch (java.awt.geom.NoninvertibleTransformException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MATRIX_INVERSE, "Unable to inverse matrix in BitmapShader", e, null);
                localMatrix = new java.awt.geom.AffineTransform();
            }
            if (!colorModel.isCompatibleRaster(mImage.getRaster())) {
                // Fallback to the default ARGB color model
                colorModel = java.awt.image.ColorModel.getRGBdefault();
            }
            return new android.graphics.BitmapShader_Delegate.BitmapShaderPaint.BitmapShaderContext(canvasMatrix, localMatrix, colorModel);
        }

        private class BitmapShaderContext implements java.awt.PaintContext {
            private final java.awt.geom.AffineTransform mCanvasMatrix;

            private final java.awt.geom.AffineTransform mLocalMatrix;

            private final java.awt.image.ColorModel mColorModel;

            public BitmapShaderContext(java.awt.geom.AffineTransform canvasMatrix, java.awt.geom.AffineTransform localMatrix, java.awt.image.ColorModel colorModel) {
                mCanvasMatrix = canvasMatrix;
                mLocalMatrix = localMatrix;
                mColorModel = colorModel;
            }

            @java.lang.Override
            public void dispose() {
            }

            @java.lang.Override
            public java.awt.image.ColorModel getColorModel() {
                return mColorModel;
            }

            @java.lang.Override
            public java.awt.image.Raster getRaster(int x, int y, int w, int h) {
                java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(mColorModel, mColorModel.createCompatibleWritableRaster(w, h), mColorModel.isAlphaPremultiplied(), null);
                int[] data = new int[w * h];
                int index = 0;
                float[] pt1 = new float[2];
                float[] pt2 = new float[2];
                for (int iy = 0; iy < h; iy++) {
                    for (int ix = 0; ix < w; ix++) {
                        // handle the canvas transform
                        pt1[0] = x + ix;
                        pt1[1] = y + iy;
                        mCanvasMatrix.transform(pt1, 0, pt2, 0, 1);
                        // handle the local matrix.
                        pt1[0] = pt2[0];
                        pt1[1] = pt2[1];
                        mLocalMatrix.transform(pt1, 0, pt2, 0, 1);
                        data[index++] = getColor(pt2[0], pt2[1]);
                    }
                }
                java.awt.image.DataBufferInt dataBuffer = new java.awt.image.DataBufferInt(data, data.length);
                java.awt.image.SampleModel colorModel = mColorModel.createCompatibleSampleModel(w, h);
                return java.awt.image.Raster.createWritableRaster(colorModel, dataBuffer, null);
            }
        }

        /**
         * Returns a color for an arbitrary point.
         */
        private int getColor(float fx, float fy) {
            int x = getCoordinate(java.lang.Math.round(fx), mImage.getWidth(), mTileModeX);
            int y = getCoordinate(java.lang.Math.round(fy), mImage.getHeight(), mTileModeY);
            return mImage.getRGB(x, y);
        }

        private int getCoordinate(int i, int size, android.graphics.Shader.TileMode mode) {
            if (i < 0) {
                switch (mode) {
                    case CLAMP :
                        i = 0;
                        break;
                    case REPEAT :
                        i = (size - 1) - ((-i) % size);
                        break;
                    case MIRROR :
                        // this is the same as the positive side, just make the value positive
                        // first.
                        i = -i;
                        int count = i / size;
                        i = i % size;
                        if ((count % 2) == 1) {
                            i = (size - 1) - i;
                        }
                        break;
                }
            } else
                if (i >= size) {
                    switch (mode) {
                        case CLAMP :
                            i = size - 1;
                            break;
                        case REPEAT :
                            i = i % size;
                            break;
                        case MIRROR :
                            int count = i / size;
                            i = i % size;
                            if ((count % 2) == 1) {
                                i = (size - 1) - i;
                            }
                            break;
                    }
                }

            return i;
        }

        @java.lang.Override
        public int getTransparency() {
            return java.awt.Paint.TRANSLUCENT;
        }
    }
}

