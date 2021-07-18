/**
 * Copyright (C) 2016 The Android Open Source Project
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


public class BaseCanvas_Delegate {
    // ---- delegate manager ----
    protected static com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.BaseCanvas_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.BaseCanvas_Delegate.class);

    // ---- delegate helper data ----
    private static final boolean[] sBoolOut = new boolean[1];

    // ---- delegate data ----
    protected android.graphics.Bitmap_Delegate mBitmap;

    protected com.android.layoutlib.bridge.impl.GcSnapshot mSnapshot;

    // ---- Public Helper methods ----
    protected BaseCanvas_Delegate(android.graphics.Bitmap_Delegate bitmap) {
        mSnapshot = com.android.layoutlib.bridge.impl.GcSnapshot.createDefaultSnapshot(mBitmap = bitmap);
    }

    protected BaseCanvas_Delegate() {
        mSnapshot = /* image */
        com.android.layoutlib.bridge.impl.GcSnapshot.createDefaultSnapshot(null);
    }

    /**
     * Disposes of the {@link Graphics2D} stack.
     */
    protected void dispose() {
        mSnapshot.dispose();
    }

    /**
     * Returns the current {@link Graphics2D} used to draw.
     */
    public com.android.layoutlib.bridge.impl.GcSnapshot getSnapshot() {
        return mSnapshot;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawBitmap(long nativeCanvas, long bitmapHandle, float left, float top, long nativePaintOrZero, int canvasDensity, int screenDensity, int bitmapDensity) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
        if (bitmapDelegate == null) {
            return;
        }
        java.awt.image.BufferedImage image = bitmapDelegate.getImage();
        float right = left + image.getWidth();
        float bottom = top + image.getHeight();
        android.graphics.BaseCanvas_Delegate.drawBitmap(nativeCanvas, bitmapDelegate, nativePaintOrZero, 0, 0, image.getWidth(), image.getHeight(), ((int) (left)), ((int) (top)), ((int) (right)), ((int) (bottom)));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawBitmap(long nativeCanvas, long bitmapHandle, float srcLeft, float srcTop, float srcRight, float srcBottom, float dstLeft, float dstTop, float dstRight, float dstBottom, long nativePaintOrZero, int screenDensity, int bitmapDensity) {
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
        if (bitmapDelegate == null) {
            return;
        }
        android.graphics.BaseCanvas_Delegate.drawBitmap(nativeCanvas, bitmapDelegate, nativePaintOrZero, ((int) (srcLeft)), ((int) (srcTop)), ((int) (srcRight)), ((int) (srcBottom)), ((int) (dstLeft)), ((int) (dstTop)), ((int) (dstRight)), ((int) (dstBottom)));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawBitmap(long nativeCanvas, int[] colors, int offset, int stride, final float x, final float y, int width, int height, boolean hasAlpha, long nativePaintOrZero) {
        // create a temp BufferedImage containing the content.
        final android.util.imagepool.ImagePool.Image image = android.util.imagepool.ImagePoolProvider.get().acquire(width, height, hasAlpha ? java.awt.image.BufferedImage.TYPE_INT_ARGB : java.awt.image.BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, colors, offset, stride);
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, nativePaintOrZero, true, false, ( graphics, paint) -> {
            if ((paint != null) && paint.isFilterBitmap()) {
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
            image.drawImage(graphics, ((int) (x)), ((int) (y)), null);
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawColor(long nativeCanvas, final int color, final int mode) {
        // get the delegate from the native int.
        android.graphics.BaseCanvas_Delegate canvasDelegate = android.graphics.BaseCanvas_Delegate.sManager.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        final int w = canvasDelegate.mBitmap.getImage().getWidth();
        final int h = canvasDelegate.mBitmap.getImage().getHeight();
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, ( graphics, paint) -> {
            // reset its transform just in case
            graphics.setTransform(new java.awt.geom.AffineTransform());
            // set the color
            graphics.setColor(/* alpha */
            new java.awt.Color(color, true));
            android.graphics.Composite composite = com.android.layoutlib.bridge.impl.PorterDuffUtility.getComposite(com.android.layoutlib.bridge.impl.PorterDuffUtility.getPorterDuffMode(mode), 0xff);
            if (composite != null) {
                graphics.setComposite(composite);
            }
            graphics.fillRect(0, 0, w, h);
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawColor(long nativeCanvas, long nativeColorSpace, long color, int mode) {
        android.graphics.BaseCanvas_Delegate.nDrawColor(nativeCanvas, android.graphics.Color.toArgb(color), mode);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawPaint(long nativeCanvas, long paint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Canvas.drawPaint is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawPoint(long nativeCanvas, float x, float y, long nativePaint) {
        // TODO: need to support the attribute (e.g. stroke width) of paint
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, nativePaint, false, false, ( graphics, paintDelegate) -> graphics.fillRect(((int) (x)), ((int) (y)), 1, 1));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawPoints(long nativeCanvas, float[] pts, int offset, int count, long nativePaint) {
        if (((offset < 0) || (count < 0)) || ((offset + count) > pts.length)) {
            throw new java.lang.IllegalArgumentException("Invalid argument set");
        }
        // ignore the last point if the count is odd (It means it is not paired).
        count = (count >> 1) << 1;
        for (int i = offset; i < (offset + count); i += 2) {
            android.graphics.BaseCanvas_Delegate.nDrawPoint(nativeCanvas, pts[i], pts[i + 1], nativePaint);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawLine(long nativeCanvas, final float startX, final float startY, final float stopX, final float stopY, long paint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> graphics.drawLine(((int) (startX)), ((int) (startY)), ((int) (stopX)), ((int) (stopY))));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawLines(long nativeCanvas, final float[] pts, final int offset, final int count, long nativePaint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, nativePaint, false, false, ( graphics, paintDelegate) -> {
            for (int i = 0; i < count; i += 4) {
                graphics.drawLine(((int) (pts[i + offset])), ((int) (pts[(i + offset) + 1])), ((int) (pts[(i + offset) + 2])), ((int) (pts[(i + offset) + 3])));
            }
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawRect(long nativeCanvas, final float left, final float top, final float right, final float bottom, long paint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
            int style = paintDelegate.getStyle();
            // draw
            if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.fillRect(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)));
            }
            if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.drawRect(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)));
            }
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawOval(long nativeCanvas, final float left, final float top, final float right, final float bottom, long paint) {
        if ((right > left) && (bottom > top)) {
            /* compositeOnly */
            /* forceSrcMode */
            android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
                int style = paintDelegate.getStyle();
                // draw
                if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                    graphics.fillOval(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)));
                }
                if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                    graphics.drawOval(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)));
                }
            });
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawCircle(long nativeCanvas, float cx, float cy, float radius, long paint) {
        android.graphics.BaseCanvas_Delegate.nDrawOval(nativeCanvas, cx - radius, cy - radius, cx + radius, cy + radius, paint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawArc(long nativeCanvas, final float left, final float top, final float right, final float bottom, final float startAngle, final float sweep, final boolean useCenter, long paint) {
        if ((right > left) && (bottom > top)) {
            /* compositeOnly */
            /* forceSrcMode */
            android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
                int style = paintDelegate.getStyle();
                java.awt.geom.Arc2D.Float arc = new java.awt.geom.Arc2D.Float(left, top, right - left, bottom - top, -startAngle, -sweep, useCenter ? Arc2D.PIE : Arc2D.OPEN);
                // draw
                if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                    graphics.fill(arc);
                }
                if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                    graphics.draw(arc);
                }
            });
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawRoundRect(long nativeCanvas, final float left, final float top, final float right, final float bottom, final float rx, final float ry, long paint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
            int style = paintDelegate.getStyle();
            // draw
            if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.fillRoundRect(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)), 2 * ((int) (rx)), 2 * ((int) (ry)));
            }
            if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.drawRoundRect(((int) (left)), ((int) (top)), ((int) (right - left)), ((int) (bottom - top)), 2 * ((int) (rx)), 2 * ((int) (ry)));
            }
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawDoubleRoundRect(long nativeCanvas, float outerLeft, float outerTop, float outerRight, float outerBottom, float outerRx, float outerRy, float innerLeft, float innerTop, float innerRight, float innerBottom, float innerRx, float innerRy, long nativePaint) {
        android.graphics.BaseCanvas_Delegate.nDrawDoubleRoundRect(nativeCanvas, outerLeft, outerTop, outerRight, outerBottom, new float[]{ outerRx, outerRy, outerRx, outerRy, outerRx, outerRy, outerRx, outerRy }, innerLeft, innerTop, innerRight, innerBottom, new float[]{ innerRx, innerRy, innerRx, innerRy, innerRx, innerRy, innerRx, innerRy }, nativePaint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawDoubleRoundRect(long nativeCanvas, float outerLeft, float outerTop, float outerRight, float outerBottom, float[] outerRadii, float innerLeft, float innerTop, float innerRight, float innerBottom, float[] innerRadii, long nativePaint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, nativePaint, false, false, ( graphics, paintDelegate) -> {
            android.graphics.RoundRectangle innerRect = new android.graphics.RoundRectangle(innerLeft, innerTop, innerRight - innerLeft, innerBottom - innerTop, innerRadii);
            android.graphics.RoundRectangle outerRect = new android.graphics.RoundRectangle(outerLeft, outerTop, outerRight - outerLeft, outerBottom - outerTop, outerRadii);
            int style = paintDelegate.getStyle();
            // draw
            if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.draw(innerRect);
                graphics.draw(outerRect);
            }
            if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                java.awt.geom.Area outerArea = new java.awt.geom.Area(outerRect);
                java.awt.geom.Area innerArea = new java.awt.geom.Area(innerRect);
                outerArea.subtract(innerArea);
                graphics.fill(outerArea);
            }
        });
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nDrawPath(long nativeCanvas, long path, long paint) {
        final android.graphics.Path_Delegate pathDelegate = android.graphics.Path_Delegate.getDelegate(path);
        if (pathDelegate == null) {
            return;
        }
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
            android.graphics.Shape shape = pathDelegate.getJavaShape();
            java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
            if (bounds.isEmpty()) {
                // Apple JRE 1.6 doesn't like drawing empty shapes.
                // http://b.android.com/178278
                if (pathDelegate.isEmpty()) {
                    // This means that the path doesn't have any lines or curves so
                    // nothing to draw.
                    return;
                }
                // The stroke width is not consider for the size of the bounds so,
                // for example, a horizontal line, would be considered as an empty
                // rectangle.
                // If the strokeWidth is not 0, we use it to consider the size of the
                // path as well.
                float strokeWidth = paintDelegate.getStrokeWidth();
                if (strokeWidth <= 0.0F) {
                    return;
                }
                bounds.setRect(bounds.getX(), bounds.getY(), java.lang.Math.max(strokeWidth, bounds.getWidth()), java.lang.Math.max(strokeWidth, bounds.getHeight()));
            }
            int style = paintDelegate.getStyle();
            if ((style == Paint.Style.FILL.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.fill(shape);
            }
            if ((style == Paint.Style.STROKE.nativeInt) || (style == Paint.Style.FILL_AND_STROKE.nativeInt)) {
                graphics.draw(shape);
            }
        });
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawRegion(long nativeCanvas, long nativeRegion, long nativePaint) {
        // FIXME
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Some canvas paths may not be drawn", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawNinePatch(long nativeCanvas, long nativeBitmap, long ninePatch, final float dstLeft, final float dstTop, final float dstRight, final float dstBottom, long nativePaintOrZero, final int screenDensity, final int bitmapDensity) {
        // get the delegate from the native int.
        final android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(nativeBitmap);
        if (bitmapDelegate == null) {
            return;
        }
        byte[] c = android.graphics.NinePatch_Delegate.getChunk(ninePatch);
        if (c == null) {
            // not a 9-patch?
            java.awt.image.BufferedImage image = bitmapDelegate.getImage();
            android.graphics.BaseCanvas_Delegate.drawBitmap(nativeCanvas, bitmapDelegate, nativePaintOrZero, 0, 0, image.getWidth(), image.getHeight(), ((int) (dstLeft)), ((int) (dstTop)), ((int) (dstRight)), ((int) (dstBottom)));
            return;
        }
        final com.android.ninepatch.NinePatchChunk chunkObject = android.graphics.NinePatch_Delegate.getChunk(c);
        if (chunkObject == null) {
            return;
        }
        android.graphics.Canvas_Delegate canvasDelegate = android.graphics.Canvas_Delegate.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        // this one can be null
        android.graphics.Paint_Delegate paintDelegate = android.graphics.Paint_Delegate.getDelegate(nativePaintOrZero);
        canvasDelegate.getSnapshot().draw(new com.android.layoutlib.bridge.impl.GcSnapshot.Drawable() {
            @java.lang.Override
            public void draw(java.awt.Graphics2D graphics, android.graphics.Paint_Delegate paint) {
                chunkObject.draw(bitmapDelegate.getImage(), graphics, ((int) (dstLeft)), ((int) (dstTop)), ((int) (dstRight - dstLeft)), ((int) (dstBottom - dstTop)), screenDensity, bitmapDensity);
            }
        }, paintDelegate, true, false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawBitmapMatrix(long nCanvas, long bitmapHandle, long nMatrix, long nPaint) {
        // get the delegate from the native int.
        android.graphics.BaseCanvas_Delegate canvasDelegate = android.graphics.BaseCanvas_Delegate.sManager.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return;
        }
        // get the delegate from the native int, which can be null
        android.graphics.Paint_Delegate paintDelegate = android.graphics.Paint_Delegate.getDelegate(nPaint);
        // get the delegate from the native int.
        android.graphics.Bitmap_Delegate bitmapDelegate = android.graphics.Bitmap_Delegate.getDelegate(bitmapHandle);
        if (bitmapDelegate == null) {
            return;
        }
        final java.awt.image.BufferedImage image = android.graphics.BaseCanvas_Delegate.getImageToDraw(bitmapDelegate, paintDelegate, android.graphics.BaseCanvas_Delegate.sBoolOut);
        android.graphics.Matrix_Delegate matrixDelegate = android.graphics.Matrix_Delegate.getDelegate(nMatrix);
        if (matrixDelegate == null) {
            return;
        }
        final java.awt.geom.AffineTransform mtx = matrixDelegate.getAffineTransform();
        /* compositeOnly */
        /* forceSrcMode */
        canvasDelegate.getSnapshot().draw(( graphics, paint) -> {
            if ((paint != null) && paint.isFilterBitmap()) {
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
            // FIXME add support for canvas, screen and bitmap densities.
            graphics.drawImage(image, mtx, null);
        }, paintDelegate, true, false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawBitmapMesh(long nCanvas, long bitmapHandle, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, long nPaint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Canvas.drawBitmapMesh is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawVertices(long nCanvas, int mode, int n, float[] verts, int vertOffset, float[] texs, int texOffset, int[] colors, int colorOffset, short[] indices, int indexOffset, int indexCount, long nPaint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Canvas.drawVertices is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawText(long nativeCanvas, char[] text, int index, int count, float startX, float startY, int flags, long paint) {
        android.graphics.BaseCanvas_Delegate.drawText(nativeCanvas, text, index, count, startX, startY, flags, paint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawText(long nativeCanvas, java.lang.String text, int start, int end, float x, float y, final int flags, long paint) {
        int count = end - start;
        char[] buffer = android.graphics.TemporaryBuffer.obtain(count);
        android.text.TextUtils.getChars(text, start, end, buffer, 0);
        android.graphics.BaseCanvas_Delegate.nDrawText(nativeCanvas, buffer, 0, count, x, y, flags, paint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawTextRun(long nativeCanvas, java.lang.String text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, long paint) {
        int count = end - start;
        char[] buffer = android.graphics.TemporaryBuffer.obtain(count);
        android.text.TextUtils.getChars(text, start, end, buffer, 0);
        android.graphics.BaseCanvas_Delegate.drawText(nativeCanvas, buffer, 0, count, x, y, isRtl ? android.graphics.Paint.BIDI_RTL : android.graphics.Paint.BIDI_LTR, paint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawTextRun(long nativeCanvas, char[] text, int start, int count, int contextStart, int contextCount, float x, float y, boolean isRtl, long paint, long nativeMeasuredText) {
        android.graphics.BaseCanvas_Delegate.drawText(nativeCanvas, text, start, count, x, y, isRtl ? android.graphics.Paint.BIDI_RTL : android.graphics.Paint.BIDI_LTR, paint);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawTextOnPath(long nativeCanvas, char[] text, int index, int count, long path, float hOffset, float vOffset, int bidiFlags, long paint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Canvas.drawTextOnPath is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nDrawTextOnPath(long nativeCanvas, java.lang.String text, long path, float hOffset, float vOffset, int bidiFlags, long paint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Canvas.drawTextOnPath is not supported.", null, null);
    }

    // ---- Private delegate/helper methods ----
    /**
     * Executes a {@link GcSnapshot.Drawable} with a given canvas and paint.
     * <p>Note that the drawable may actually be executed several times if there are
     * layers involved (see {@link #saveLayer(RectF, Paint_Delegate, int)}.
     */
    private static void draw(long nCanvas, long nPaint, boolean compositeOnly, boolean forceSrcMode, com.android.layoutlib.bridge.impl.GcSnapshot.Drawable drawable) {
        // get the delegate from the native int.
        android.graphics.BaseCanvas_Delegate canvasDelegate = android.graphics.BaseCanvas_Delegate.sManager.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return;
        }
        // get the paint which can be null if nPaint is 0;
        android.graphics.Paint_Delegate paintDelegate = android.graphics.Paint_Delegate.getDelegate(nPaint);
        canvasDelegate.getSnapshot().draw(drawable, paintDelegate, compositeOnly, forceSrcMode);
    }

    /**
     * Executes a {@link GcSnapshot.Drawable} with a given canvas. No paint object will be provided
     * to {@link GcSnapshot.Drawable#draw(Graphics2D, Paint_Delegate)}.
     * <p>Note that the drawable may actually be executed several times if there are
     * layers involved (see {@link #saveLayer(RectF, Paint_Delegate, int)}.
     */
    private static void draw(long nCanvas, com.android.layoutlib.bridge.impl.GcSnapshot.Drawable drawable) {
        // get the delegate from the native int.
        android.graphics.BaseCanvas_Delegate canvasDelegate = android.graphics.BaseCanvas_Delegate.sManager.getDelegate(nCanvas);
        if (canvasDelegate == null) {
            return;
        }
        canvasDelegate.mSnapshot.draw(drawable);
    }

    private static void drawText(long nativeCanvas, final char[] text, final int index, final int count, final float startX, final float startY, final int bidiFlags, long paint) {
        /* compositeOnly */
        /* forceSrcMode */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, paint, false, false, ( graphics, paintDelegate) -> {
            // WARNING: the logic in this method is similar to Paint_Delegate.measureText.
            // Any change to this method should be reflected in Paint.measureText
            // Paint.TextAlign indicates how the text is positioned relative to X.
            // LEFT is the default and there's nothing to do.
            float x = startX;
            int limit = index + count;
            if (paintDelegate.getTextAlign() != Paint.Align.LEFT.nativeInt) {
                android.graphics.RectF bounds = paintDelegate.measureText(text, index, count, null, 0, bidiFlags);
                float m = bounds.right - bounds.left;
                if (paintDelegate.getTextAlign() == Paint.Align.CENTER.nativeInt) {
                    x -= m / 2;
                } else
                    if (paintDelegate.getTextAlign() == Paint.Align.RIGHT.nativeInt) {
                        x -= m;
                    }

            }
            new android.graphics.BidiRenderer(graphics, paintDelegate, text).setRenderLocation(x, startY).renderText(index, limit, bidiFlags, null, 0, true);
        });
    }

    private static void drawBitmap(long nativeCanvas, android.graphics.Bitmap_Delegate bitmap, long nativePaintOrZero, final int sleft, final int stop, final int sright, final int sbottom, final int dleft, final int dtop, final int dright, final int dbottom) {
        // get the delegate from the native int.
        android.graphics.BaseCanvas_Delegate canvasDelegate = android.graphics.BaseCanvas_Delegate.sManager.getDelegate(nativeCanvas);
        if (canvasDelegate == null) {
            return;
        }
        // get the paint, which could be null if the int is 0
        android.graphics.Paint_Delegate paintDelegate = android.graphics.Paint_Delegate.getDelegate(nativePaintOrZero);
        final java.awt.image.BufferedImage image = android.graphics.BaseCanvas_Delegate.getImageToDraw(bitmap, paintDelegate, android.graphics.BaseCanvas_Delegate.sBoolOut);
        /* compositeOnly */
        android.graphics.BaseCanvas_Delegate.draw(nativeCanvas, nativePaintOrZero, true, android.graphics.BaseCanvas_Delegate.sBoolOut[0], ( graphics, paint) -> {
            if ((paint != null) && paint.isFilterBitmap()) {
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
            // FIXME add support for canvas, screen and bitmap densities.
            graphics.drawImage(image, dleft, dtop, dright, dbottom, sleft, stop, sright, sbottom, null);
        });
    }

    /**
     * Returns a BufferedImage ready for drawing, based on the bitmap and paint delegate.
     * The image returns, through a 1-size boolean array, whether the drawing code should
     * use a SRC composite no matter what the paint says.
     *
     * @param bitmap
     * 		the bitmap
     * @param paint
     * 		the paint that will be used to draw
     * @param forceSrcMode
     * 		whether the composite will have to be SRC
     * @return the image to draw
     */
    private static java.awt.image.BufferedImage getImageToDraw(android.graphics.Bitmap_Delegate bitmap, android.graphics.Paint_Delegate paint, boolean[] forceSrcMode) {
        java.awt.image.BufferedImage image = bitmap.getImage();
        forceSrcMode[0] = false;
        // if the bitmap config is alpha_8, then we erase all color value from it
        // before drawing it or apply the texture from the shader if present.
        if (bitmap.getConfig() == android.graphics.Bitmap.Config.ALPHA_8) {
            android.graphics.Shader_Delegate shader = paint.getShader();
            java.awt.Paint javaPaint = null;
            if (shader instanceof android.graphics.BitmapShader_Delegate) {
                javaPaint = shader.getJavaPaint();
            }
            android.graphics.BaseCanvas_Delegate.fixAlpha8Bitmap(image, javaPaint);
        } else
            if (!bitmap.hasAlpha()) {
                // hasAlpha is merely a rendering hint. There can in fact be alpha values
                // in the bitmap but it should be ignored at drawing time.
                // There is two ways to do this:
                // - override the composite to be SRC. This can only be used if the composite
                // was going to be SRC or SRC_OVER in the first place
                // - Create a different bitmap to draw in which all the alpha channel values is set
                // to 0xFF.
                if (paint != null) {
                    android.graphics.PorterDuff.Mode mode = android.graphics.PorterDuff.intToMode(paint.getPorterDuffMode());
                    forceSrcMode[0] = (mode == android.graphics.PorterDuff.Mode.SRC_OVER) || (mode == android.graphics.PorterDuff.Mode.SRC);
                }
                // if we can't force SRC mode, then create a temp bitmap of TYPE_RGB
                if (!forceSrcMode[0]) {
                    image = android.graphics.Bitmap_Delegate.createCopy(image, java.awt.image.BufferedImage.TYPE_INT_RGB, 0xff);
                }
            }

        return image;
    }

    /**
     * This method will apply the correct color to the passed "only alpha" image. Colors on the
     * passed image will be destroyed.
     * If the passed javaPaint is null, the color will be set to 0. If a paint is passed, it will
     * be used to obtain the color that will be applied.
     * <p/>
     * This will destroy the passed image color channel.
     */
    private static void fixAlpha8Bitmap(final java.awt.image.BufferedImage image, @android.annotation.Nullable
    java.awt.Paint javaPaint) {
        int w = image.getWidth();
        int h = image.getHeight();
        java.awt.image.DataBuffer texture = null;
        if (javaPaint != null) {
            java.awt.PaintContext context = javaPaint.createContext(java.awt.image.ColorModel.getRGBdefault(), null, null, new java.awt.geom.AffineTransform(), null);
            texture = context.getRaster(0, 0, w, h).getDataBuffer();
        }
        int[] argb = new int[w * h];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), argb, 0, image.getWidth());
        final int length = argb.length;
        for (int i = 0; i < length; i++) {
            argb[i] &= 0xff000000;
            if (texture != null) {
                argb[i] |= texture.getElem(i) & 0xffffff;
            }
        }
        image.setRGB(0, 0, w, h, argb, 0, w);
    }

    protected int save(int saveFlags) {
        // get the current save count
        int count = mSnapshot.size();
        mSnapshot = mSnapshot.save(saveFlags);
        // return the old save count
        return count;
    }

    protected int saveLayerAlpha(android.graphics.RectF rect, int alpha, int saveFlags) {
        android.graphics.Paint_Delegate paint = new android.graphics.Paint_Delegate();
        paint.setAlpha(alpha);
        return saveLayer(rect, paint, saveFlags);
    }

    protected int saveLayer(android.graphics.RectF rect, android.graphics.Paint_Delegate paint, int saveFlags) {
        // get the current save count
        int count = mSnapshot.size();
        mSnapshot = mSnapshot.saveLayer(rect, paint, saveFlags);
        // return the old save count
        return count;
    }

    /**
     * Restores the {@link GcSnapshot} to <var>saveCount</var>
     *
     * @param saveCount
     * 		the saveCount
     */
    protected void restoreTo(int saveCount) {
        mSnapshot = mSnapshot.restoreTo(saveCount);
    }

    /**
     * Restores the top {@link GcSnapshot}
     */
    protected void restore() {
        mSnapshot = mSnapshot.restore();
    }

    protected boolean clipRect(float left, float top, float right, float bottom, int regionOp) {
        return mSnapshot.clipRect(left, top, right, bottom, regionOp);
    }
}

