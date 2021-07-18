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


/**
 * This class is a base class for canvases that defer drawing operations, so all
 * the draw operations can be marked @FastNative. It contains a re-implementation of
 * all the methods in {@link BaseCanvas}.
 *
 * @unknown 
 */
public class BaseRecordingCanvas extends android.graphics.Canvas {
    public BaseRecordingCanvas(long nativeCanvas) {
        super(nativeCanvas);
    }

    @java.lang.Override
    public final void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawArc(mNativeCanvasWrapper, left, top, right, bottom, startAngle, sweepAngle, useCenter, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawArc(@android.annotation.NonNull
    android.graphics.RectF oval, float startAngle, float sweepAngle, boolean useCenter, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, useCenter, paint);
    }

    @java.lang.Override
    public final void drawARGB(int a, int r, int g, int b) {
        drawColor(android.graphics.Color.argb(a, r, g, b));
    }

    @java.lang.Override
    public final void drawBitmap(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, float left, float top, @android.annotation.Nullable
    android.graphics.Paint paint) {
        throwIfCannotDraw(bitmap);
        android.graphics.BaseRecordingCanvas.nDrawBitmap(mNativeCanvasWrapper, bitmap.getNativeInstance(), left, top, paint != null ? paint.getNativeInstance() : 0, mDensity, mScreenDensity, bitmap.mDensity);
    }

    @java.lang.Override
    public final void drawBitmap(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, @android.annotation.NonNull
    android.graphics.Matrix matrix, @android.annotation.Nullable
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawBitmapMatrix(mNativeCanvasWrapper, bitmap.getNativeInstance(), matrix.ni(), paint != null ? paint.getNativeInstance() : 0);
    }

    @java.lang.Override
    public final void drawBitmap(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, @android.annotation.Nullable
    android.graphics.Rect src, @android.annotation.NonNull
    android.graphics.Rect dst, @android.annotation.Nullable
    android.graphics.Paint paint) {
        if (dst == null) {
            throw new java.lang.NullPointerException();
        }
        throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.getNativeInstance();
        int left;
        int top;
        int right;
        int bottom;
        if (src == null) {
            left = top = 0;
            right = bitmap.getWidth();
            bottom = bitmap.getHeight();
        } else {
            left = src.left;
            right = src.right;
            top = src.top;
            bottom = src.bottom;
        }
        android.graphics.BaseRecordingCanvas.nDrawBitmap(mNativeCanvasWrapper, bitmap.getNativeInstance(), left, top, right, bottom, dst.left, dst.top, dst.right, dst.bottom, nativePaint, mScreenDensity, bitmap.mDensity);
    }

    @java.lang.Override
    public final void drawBitmap(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, @android.annotation.Nullable
    android.graphics.Rect src, @android.annotation.NonNull
    android.graphics.RectF dst, @android.annotation.Nullable
    android.graphics.Paint paint) {
        if (dst == null) {
            throw new java.lang.NullPointerException();
        }
        throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.getNativeInstance();
        float left;
        float top;
        float right;
        float bottom;
        if (src == null) {
            left = top = 0;
            right = bitmap.getWidth();
            bottom = bitmap.getHeight();
        } else {
            left = src.left;
            right = src.right;
            top = src.top;
            bottom = src.bottom;
        }
        android.graphics.BaseRecordingCanvas.nDrawBitmap(mNativeCanvasWrapper, bitmap.getNativeInstance(), left, top, right, bottom, dst.left, dst.top, dst.right, dst.bottom, nativePaint, mScreenDensity, bitmap.mDensity);
    }

    /**
     *
     *
     * @deprecated checkstyle
     */
    @java.lang.Override
    @java.lang.Deprecated
    public final void drawBitmap(@android.annotation.NonNull
    int[] colors, int offset, int stride, float x, float y, int width, int height, boolean hasAlpha, @android.annotation.Nullable
    android.graphics.Paint paint) {
        // check for valid input
        if (width < 0) {
            throw new java.lang.IllegalArgumentException("width must be >= 0");
        }
        if (height < 0) {
            throw new java.lang.IllegalArgumentException("height must be >= 0");
        }
        if (java.lang.Math.abs(stride) < width) {
            throw new java.lang.IllegalArgumentException("abs(stride) must be >= width");
        }
        int lastScanline = offset + ((height - 1) * stride);
        int length = colors.length;
        if ((((offset < 0) || ((offset + width) > length)) || (lastScanline < 0)) || ((lastScanline + width) > length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        // quick escape if there's nothing to draw
        if ((width == 0) || (height == 0)) {
            return;
        }
        // punch down to native for the actual draw
        android.graphics.BaseRecordingCanvas.nDrawBitmap(mNativeCanvasWrapper, colors, offset, stride, x, y, width, height, hasAlpha, paint != null ? paint.getNativeInstance() : 0);
    }

    /**
     *
     *
     * @deprecated checkstyle
     */
    @java.lang.Override
    @java.lang.Deprecated
    public final void drawBitmap(@android.annotation.NonNull
    int[] colors, int offset, int stride, int x, int y, int width, int height, boolean hasAlpha, @android.annotation.Nullable
    android.graphics.Paint paint) {
        // call through to the common float version
        drawBitmap(colors, offset, stride, ((float) (x)), ((float) (y)), width, height, hasAlpha, paint);
    }

    @java.lang.Override
    public final void drawBitmapMesh(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, int meshWidth, int meshHeight, @android.annotation.NonNull
    float[] verts, int vertOffset, @android.annotation.Nullable
    int[] colors, int colorOffset, @android.annotation.Nullable
    android.graphics.Paint paint) {
        if ((((meshWidth | meshHeight) | vertOffset) | colorOffset) < 0) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        if ((meshWidth == 0) || (meshHeight == 0)) {
            return;
        }
        int count = (meshWidth + 1) * (meshHeight + 1);
        // we mul by 2 since we need two floats per vertex
        android.graphics.BaseCanvas.checkRange(verts.length, vertOffset, count * 2);
        if (colors != null) {
            // no mul by 2, since we need only 1 color per vertex
            android.graphics.BaseCanvas.checkRange(colors.length, colorOffset, count);
        }
        android.graphics.BaseRecordingCanvas.nDrawBitmapMesh(mNativeCanvasWrapper, bitmap.getNativeInstance(), meshWidth, meshHeight, verts, vertOffset, colors, colorOffset, paint != null ? paint.getNativeInstance() : 0);
    }

    @java.lang.Override
    public final void drawCircle(float cx, float cy, float radius, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawCircle(mNativeCanvasWrapper, cx, cy, radius, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawColor(@android.annotation.ColorInt
    int color) {
        android.graphics.BaseRecordingCanvas.nDrawColor(mNativeCanvasWrapper, color, android.graphics.BlendMode.SRC_OVER.getXfermode().porterDuffMode);
    }

    @java.lang.Override
    public final void drawColor(@android.annotation.ColorInt
    int color, @android.annotation.NonNull
    android.graphics.PorterDuff.Mode mode) {
        android.graphics.BaseRecordingCanvas.nDrawColor(mNativeCanvasWrapper, color, mode.nativeInt);
    }

    @java.lang.Override
    public final void drawColor(@android.annotation.ColorInt
    int color, @android.annotation.NonNull
    android.graphics.BlendMode mode) {
        android.graphics.BaseRecordingCanvas.nDrawColor(mNativeCanvasWrapper, color, mode.getXfermode().porterDuffMode);
    }

    @java.lang.Override
    public final void drawColor(@android.annotation.ColorLong
    long color, @android.annotation.NonNull
    android.graphics.BlendMode mode) {
        android.graphics.ColorSpace cs = android.graphics.Color.colorSpace(color);
        android.graphics.BaseRecordingCanvas.nDrawColor(mNativeCanvasWrapper, cs.getNativeInstance(), color, mode.getXfermode().porterDuffMode);
    }

    @java.lang.Override
    public final void drawLine(float startX, float startY, float stopX, float stopY, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawLine(mNativeCanvasWrapper, startX, startY, stopX, stopY, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawLines(@android.annotation.Size(multiple = 4)
    @android.annotation.NonNull
    float[] pts, int offset, int count, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawLines(mNativeCanvasWrapper, pts, offset, count, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawLines(@android.annotation.Size(multiple = 4)
    @android.annotation.NonNull
    float[] pts, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawLines(pts, 0, pts.length, paint);
    }

    @java.lang.Override
    public final void drawOval(float left, float top, float right, float bottom, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawOval(mNativeCanvasWrapper, left, top, right, bottom, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawOval(@android.annotation.NonNull
    android.graphics.RectF oval, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (oval == null) {
            throw new java.lang.NullPointerException();
        }
        drawOval(oval.left, oval.top, oval.right, oval.bottom, paint);
    }

    @java.lang.Override
    public final void drawPaint(@android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawPaint(mNativeCanvasWrapper, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawPatch(@android.annotation.NonNull
    android.graphics.NinePatch patch, @android.annotation.NonNull
    android.graphics.Rect dst, @android.annotation.Nullable
    android.graphics.Paint paint) {
        android.graphics.Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.getNativeInstance();
        android.graphics.BaseRecordingCanvas.nDrawNinePatch(mNativeCanvasWrapper, bitmap.getNativeInstance(), patch.mNativeChunk, dst.left, dst.top, dst.right, dst.bottom, nativePaint, mDensity, patch.getDensity());
    }

    @java.lang.Override
    public final void drawPatch(@android.annotation.NonNull
    android.graphics.NinePatch patch, @android.annotation.NonNull
    android.graphics.RectF dst, @android.annotation.Nullable
    android.graphics.Paint paint) {
        android.graphics.Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        final long nativePaint = (paint == null) ? 0 : paint.getNativeInstance();
        android.graphics.BaseRecordingCanvas.nDrawNinePatch(mNativeCanvasWrapper, bitmap.getNativeInstance(), patch.mNativeChunk, dst.left, dst.top, dst.right, dst.bottom, nativePaint, mDensity, patch.getDensity());
    }

    @java.lang.Override
    public final void drawPath(@android.annotation.NonNull
    android.graphics.Path path, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (path.isSimplePath && (path.rects != null)) {
            android.graphics.BaseRecordingCanvas.nDrawRegion(mNativeCanvasWrapper, path.rects.mNativeRegion, paint.getNativeInstance());
        } else {
            android.graphics.BaseRecordingCanvas.nDrawPath(mNativeCanvasWrapper, path.readOnlyNI(), paint.getNativeInstance());
        }
    }

    @java.lang.Override
    public final void drawPicture(@android.annotation.NonNull
    android.graphics.Picture picture) {
        picture.endRecording();
        int restoreCount = save();
        picture.draw(this);
        restoreToCount(restoreCount);
    }

    @java.lang.Override
    public final void drawPicture(@android.annotation.NonNull
    android.graphics.Picture picture, @android.annotation.NonNull
    android.graphics.Rect dst) {
        save();
        translate(dst.left, dst.top);
        if ((picture.getWidth() > 0) && (picture.getHeight() > 0)) {
            scale(((float) (dst.width())) / picture.getWidth(), ((float) (dst.height())) / picture.getHeight());
        }
        drawPicture(picture);
        restore();
    }

    @java.lang.Override
    public final void drawPicture(@android.annotation.NonNull
    android.graphics.Picture picture, @android.annotation.NonNull
    android.graphics.RectF dst) {
        save();
        translate(dst.left, dst.top);
        if ((picture.getWidth() > 0) && (picture.getHeight() > 0)) {
            scale(dst.width() / picture.getWidth(), dst.height() / picture.getHeight());
        }
        drawPicture(picture);
        restore();
    }

    @java.lang.Override
    public final void drawPoint(float x, float y, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawPoint(mNativeCanvasWrapper, x, y, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawPoints(@android.annotation.Size(multiple = 2)
    float[] pts, int offset, int count, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawPoints(mNativeCanvasWrapper, pts, offset, count, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawPoints(@android.annotation.Size(multiple = 2)
    @android.annotation.NonNull
    float[] pts, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawPoints(pts, 0, pts.length, paint);
    }

    /**
     *
     *
     * @deprecated checkstyle
     */
    @java.lang.Override
    @java.lang.Deprecated
    public final void drawPosText(@android.annotation.NonNull
    char[] text, int index, int count, @android.annotation.NonNull
    @android.annotation.Size(multiple = 2)
    float[] pos, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (((index < 0) || ((index + count) > text.length)) || ((count * 2) > pos.length)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        for (int i = 0; i < count; i++) {
            drawText(text, index + i, 1, pos[i * 2], pos[(i * 2) + 1], paint);
        }
    }

    /**
     *
     *
     * @deprecated checkstyle
     */
    @java.lang.Override
    @java.lang.Deprecated
    public final void drawPosText(@android.annotation.NonNull
    java.lang.String text, @android.annotation.NonNull
    @android.annotation.Size(multiple = 2)
    float[] pos, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawPosText(text.toCharArray(), 0, text.length(), pos, paint);
    }

    @java.lang.Override
    public final void drawRect(float left, float top, float right, float bottom, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawRect(mNativeCanvasWrapper, left, top, right, bottom, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawRect(@android.annotation.NonNull
    android.graphics.Rect r, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawRect(r.left, r.top, r.right, r.bottom, paint);
    }

    @java.lang.Override
    public final void drawRect(@android.annotation.NonNull
    android.graphics.RectF rect, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawRGB(int r, int g, int b) {
        drawColor(android.graphics.Color.rgb(r, g, b));
    }

    @java.lang.Override
    public final void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawRoundRect(mNativeCanvasWrapper, left, top, right, bottom, rx, ry, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawRoundRect(@android.annotation.NonNull
    android.graphics.RectF rect, float rx, float ry, @android.annotation.NonNull
    android.graphics.Paint paint) {
        drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, rx, ry, paint);
    }

    @java.lang.Override
    public final void drawDoubleRoundRect(@android.annotation.NonNull
    android.graphics.RectF outer, float outerRx, float outerRy, @android.annotation.NonNull
    android.graphics.RectF inner, float innerRx, float innerRy, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawDoubleRoundRect(mNativeCanvasWrapper, outer.left, outer.top, outer.right, outer.bottom, outerRx, outerRy, inner.left, inner.top, inner.right, inner.bottom, innerRx, innerRy, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawDoubleRoundRect(@android.annotation.NonNull
    android.graphics.RectF outer, float[] outerRadii, @android.annotation.NonNull
    android.graphics.RectF inner, float[] innerRadii, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawDoubleRoundRect(mNativeCanvasWrapper, outer.left, outer.top, outer.right, outer.bottom, outerRadii, inner.left, inner.top, inner.right, inner.bottom, innerRadii, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawText(@android.annotation.NonNull
    char[] text, int index, int count, float x, float y, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if ((((index | count) | (index + count)) | ((text.length - index) - count)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        android.graphics.BaseRecordingCanvas.nDrawText(mNativeCanvasWrapper, text, index, count, x, y, paint.mBidiFlags, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawText(@android.annotation.NonNull
    java.lang.CharSequence text, int start, int end, float x, float y, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if ((((start | end) | (end - start)) | (text.length() - end)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (((text instanceof java.lang.String) || (text instanceof android.text.SpannedString)) || (text instanceof android.text.SpannableString)) {
            android.graphics.BaseRecordingCanvas.nDrawText(mNativeCanvasWrapper, text.toString(), start, end, x, y, paint.mBidiFlags, paint.getNativeInstance());
        } else
            if (text instanceof android.text.GraphicsOperations) {
                drawText(this, start, end, x, y, paint);
            } else {
                char[] buf = android.graphics.TemporaryBuffer.obtain(end - start);
                android.text.TextUtils.getChars(text, start, end, buf, 0);
                android.graphics.BaseRecordingCanvas.nDrawText(mNativeCanvasWrapper, buf, 0, end - start, x, y, paint.mBidiFlags, paint.getNativeInstance());
                android.graphics.TemporaryBuffer.recycle(buf);
            }

    }

    @java.lang.Override
    public final void drawText(@android.annotation.NonNull
    java.lang.String text, float x, float y, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawText(mNativeCanvasWrapper, text, 0, text.length(), x, y, paint.mBidiFlags, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawText(@android.annotation.NonNull
    java.lang.String text, int start, int end, float x, float y, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if ((((start | end) | (end - start)) | (text.length() - end)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        android.graphics.BaseRecordingCanvas.nDrawText(mNativeCanvasWrapper, text, start, end, x, y, paint.mBidiFlags, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawTextOnPath(@android.annotation.NonNull
    char[] text, int index, int count, @android.annotation.NonNull
    android.graphics.Path path, float hOffset, float vOffset, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if ((index < 0) || ((index + count) > text.length)) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
        android.graphics.BaseRecordingCanvas.nDrawTextOnPath(mNativeCanvasWrapper, text, index, count, path.readOnlyNI(), hOffset, vOffset, paint.mBidiFlags, paint.getNativeInstance());
    }

    @java.lang.Override
    public final void drawTextOnPath(@android.annotation.NonNull
    java.lang.String text, @android.annotation.NonNull
    android.graphics.Path path, float hOffset, float vOffset, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (text.length() > 0) {
            android.graphics.BaseRecordingCanvas.nDrawTextOnPath(mNativeCanvasWrapper, text, path.readOnlyNI(), hOffset, vOffset, paint.mBidiFlags, paint.getNativeInstance());
        }
    }

    @java.lang.Override
    public final void drawTextRun(@android.annotation.NonNull
    char[] text, int index, int count, int contextIndex, int contextCount, float x, float y, boolean isRtl, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (text == null) {
            throw new java.lang.NullPointerException("text is null");
        }
        if (paint == null) {
            throw new java.lang.NullPointerException("paint is null");
        }
        if (((((((index | count) | contextIndex) | contextCount) | (index - contextIndex)) | ((contextIndex + contextCount) - (index + count))) | (text.length - (contextIndex + contextCount))) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        /* measured text */
        android.graphics.BaseRecordingCanvas.nDrawTextRun(mNativeCanvasWrapper, text, index, count, contextIndex, contextCount, x, y, isRtl, paint.getNativeInstance(), 0);
    }

    @java.lang.Override
    public final void drawTextRun(@android.annotation.NonNull
    java.lang.CharSequence text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, @android.annotation.NonNull
    android.graphics.Paint paint) {
        if (text == null) {
            throw new java.lang.NullPointerException("text is null");
        }
        if (paint == null) {
            throw new java.lang.NullPointerException("paint is null");
        }
        if ((((((((start | end) | contextStart) | contextEnd) | (start - contextStart)) | (end - start)) | (contextEnd - end)) | (text.length() - contextEnd)) < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (((text instanceof java.lang.String) || (text instanceof android.text.SpannedString)) || (text instanceof android.text.SpannableString)) {
            android.graphics.BaseRecordingCanvas.nDrawTextRun(mNativeCanvasWrapper, text.toString(), start, end, contextStart, contextEnd, x, y, isRtl, paint.getNativeInstance());
        } else
            if (text instanceof android.text.GraphicsOperations) {
                drawTextRun(this, start, end, contextStart, contextEnd, x, y, isRtl, paint);
            } else {
                if (text instanceof android.text.PrecomputedText) {
                    final android.text.PrecomputedText pt = ((android.text.PrecomputedText) (text));
                    final int paraIndex = pt.findParaIndex(start);
                    if (end <= pt.getParagraphEnd(paraIndex)) {
                        final int paraStart = pt.getParagraphStart(paraIndex);
                        final android.text.MeasuredParagraph mp = pt.getMeasuredParagraph(paraIndex);
                        // Only support if the target is in the same paragraph.
                        drawTextRun(mp.getMeasuredText(), start - paraStart, end - paraStart, contextStart - paraStart, contextEnd - paraStart, x, y, isRtl, paint);
                        return;
                    }
                }
                int contextLen = contextEnd - contextStart;
                int len = end - start;
                char[] buf = android.graphics.TemporaryBuffer.obtain(contextLen);
                android.text.TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
                /* measured paragraph pointer */
                android.graphics.BaseRecordingCanvas.nDrawTextRun(mNativeCanvasWrapper, buf, start - contextStart, len, 0, contextLen, x, y, isRtl, paint.getNativeInstance(), 0);
                android.graphics.TemporaryBuffer.recycle(buf);
            }

    }

    @java.lang.Override
    public void drawTextRun(@android.annotation.NonNull
    android.graphics.text.MeasuredText measuredText, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseRecordingCanvas.nDrawTextRun(mNativeCanvasWrapper, measuredText.getChars(), start, end - start, contextStart, contextEnd - contextStart, x, y, isRtl, paint.getNativeInstance(), measuredText.getNativePtr());
    }

    @java.lang.Override
    public final void drawVertices(@android.annotation.NonNull
    android.graphics.Canvas.VertexMode mode, int vertexCount, @android.annotation.NonNull
    float[] verts, int vertOffset, @android.annotation.Nullable
    float[] texs, int texOffset, @android.annotation.Nullable
    int[] colors, int colorOffset, @android.annotation.Nullable
    short[] indices, int indexOffset, int indexCount, @android.annotation.NonNull
    android.graphics.Paint paint) {
        android.graphics.BaseCanvas.checkRange(verts.length, vertOffset, vertexCount);
        if (texs != null) {
            android.graphics.BaseCanvas.checkRange(texs.length, texOffset, vertexCount);
        }
        if (colors != null) {
            android.graphics.BaseCanvas.checkRange(colors.length, colorOffset, vertexCount / 2);
        }
        if (indices != null) {
            android.graphics.BaseCanvas.checkRange(indices.length, indexOffset, indexCount);
        }
        android.graphics.BaseRecordingCanvas.nDrawVertices(mNativeCanvasWrapper, mode.nativeInt, vertexCount, verts, vertOffset, texs, texOffset, colors, colorOffset, indices, indexOffset, indexCount, paint.getNativeInstance());
    }

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawBitmap(long nativeCanvas, long bitmapHandle, float left, float top, long nativePaintOrZero, int canvasDensity, int screenDensity, int bitmapDensity);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawBitmap(long nativeCanvas, long bitmapHandle, float srcLeft, float srcTop, float srcRight, float srcBottom, float dstLeft, float dstTop, float dstRight, float dstBottom, long nativePaintOrZero, int screenDensity, int bitmapDensity);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawBitmap(long nativeCanvas, int[] colors, int offset, int stride, float x, float y, int width, int height, boolean hasAlpha, long nativePaintOrZero);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawColor(long nativeCanvas, int color, int mode);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawColor(long nativeCanvas, long nativeColorSpace, @android.annotation.ColorLong
    long color, int mode);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawPaint(long nativeCanvas, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawPoint(long canvasHandle, float x, float y, long paintHandle);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawPoints(long canvasHandle, float[] pts, int offset, int count, long paintHandle);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawLine(long nativeCanvas, float startX, float startY, float stopX, float stopY, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawLines(long canvasHandle, float[] pts, int offset, int count, long paintHandle);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawRect(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawOval(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawCircle(long nativeCanvas, float cx, float cy, float radius, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawArc(long nativeCanvas, float left, float top, float right, float bottom, float startAngle, float sweep, boolean useCenter, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawRoundRect(long nativeCanvas, float left, float top, float right, float bottom, float rx, float ry, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawDoubleRoundRect(long nativeCanvas, float outerLeft, float outerTop, float outerRight, float outerBottom, float outerRx, float outerRy, float innerLeft, float innerTop, float innerRight, float innerBottom, float innerRx, float innerRy, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawDoubleRoundRect(long nativeCanvas, float outerLeft, float outerTop, float outerRight, float outerBottom, float[] outerRadii, float innerLeft, float innerTop, float innerRight, float innerBottom, float[] innerRadii, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawPath(long nativeCanvas, long nativePath, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawRegion(long nativeCanvas, long nativeRegion, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawNinePatch(long nativeCanvas, long nativeBitmap, long ninePatch, float dstLeft, float dstTop, float dstRight, float dstBottom, long nativePaintOrZero, int screenDensity, int bitmapDensity);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawBitmapMatrix(long nativeCanvas, long bitmapHandle, long nativeMatrix, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawBitmapMesh(long nativeCanvas, long bitmapHandle, int meshWidth, int meshHeight, float[] verts, int vertOffset, int[] colors, int colorOffset, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawVertices(long nativeCanvas, int mode, int n, float[] verts, int vertOffset, float[] texs, int texOffset, int[] colors, int colorOffset, short[] indices, int indexOffset, int indexCount, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawText(long nativeCanvas, char[] text, int index, int count, float x, float y, int flags, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawText(long nativeCanvas, java.lang.String text, int start, int end, float x, float y, int flags, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawTextRun(long nativeCanvas, java.lang.String text, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawTextRun(long nativeCanvas, char[] text, int start, int count, int contextStart, int contextCount, float x, float y, boolean isRtl, long nativePaint, long nativePrecomputedText);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawTextOnPath(long nativeCanvas, char[] text, int index, int count, long nativePath, float hOffset, float vOffset, int bidiFlags, long nativePaint);

    @dalvik.annotation.optimization.FastNative
    private static native void nDrawTextOnPath(long nativeCanvas, java.lang.String text, long nativePath, float hOffset, float vOffset, int flags, long nativePaint);
}

