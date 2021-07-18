/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.view;


/**
 * Paints shadow for rounded rectangles. Inspiration from CardView. Couldn't use that directly,
 * since it modifies the size of the content, that we can't do.
 */
public class RectShadowPainter {
    private static final int START_COLOR = com.android.layoutlib.bridge.impl.ResourceHelper.getColor("#37000000");

    private static final int END_COLOR = com.android.layoutlib.bridge.impl.ResourceHelper.getColor("#03000000");

    private static final float PERPENDICULAR_ANGLE = 90.0F;

    public static void paintShadow(android.graphics.Outline viewOutline, float elevation, android.graphics.Canvas canvas, float alpha) {
        android.graphics.Rect outline = new android.graphics.Rect();
        if (!viewOutline.getRect(outline)) {
            assert false : "Outline is not a rect shadow";
            return;
        }
        // TODO replacing the algorithm here to create better shadow
        float shadowSize = android.view.RectShadowPainter.elevationToShadow(elevation);
        int saved = android.view.RectShadowPainter.modifyCanvas(canvas, shadowSize);
        if (saved == (-1)) {
            return;
        }
        float radius = viewOutline.getRadius();
        if (radius <= 0) {
            // We can not paint a shadow with radius 0
            return;
        }
        try {
            android.graphics.Paint cornerPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG | android.graphics.Paint.DITHER_FLAG);
            cornerPaint.setStyle(android.graphics.Paint.Style.FILL);
            android.graphics.Paint edgePaint = new android.graphics.Paint(cornerPaint);
            edgePaint.setAntiAlias(false);
            float outerArcRadius = radius + shadowSize;
            int[] colors = new int[]{ android.view.RectShadowPainter.START_COLOR, android.view.RectShadowPainter.START_COLOR, android.view.RectShadowPainter.END_COLOR };
            if (alpha != 1.0F) {
                // Correct colors using the given component alpha
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = android.graphics.Color.argb(((int) (android.graphics.Color.alpha(colors[i]) * alpha)), android.graphics.Color.red(colors[i]), android.graphics.Color.green(colors[i]), android.graphics.Color.blue(colors[i]));
                }
            }
            cornerPaint.setShader(new android.graphics.RadialGradient(0, 0, outerArcRadius, colors, new float[]{ 0.0F, radius / outerArcRadius, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
            edgePaint.setShader(new android.graphics.LinearGradient(0, 0, -shadowSize, 0, colors[0], colors[2], android.graphics.Shader.TileMode.CLAMP));
            android.graphics.Path path = new android.graphics.Path();
            path.setFillType(android.graphics.Path.FillType.EVEN_ODD);
            // A rectangle bounding the complete shadow.
            android.graphics.RectF shadowRect = new android.graphics.RectF(outline);
            shadowRect.inset(-shadowSize, -shadowSize);
            // A rectangle with edges corresponding to the straight edges of the outline.
            android.graphics.RectF inset = new android.graphics.RectF(outline);
            inset.inset(radius, radius);
            // A rectangle used to represent the edge shadow.
            android.graphics.RectF edgeShadowRect = new android.graphics.RectF();
            // left and right sides.
            edgeShadowRect.set(-shadowSize, 0.0F, 0.0F, inset.height());
            // Left shadow
            android.view.RectShadowPainter.sideShadow(canvas, edgePaint, edgeShadowRect, outline.left, inset.top, 0);
            // Right shadow
            android.view.RectShadowPainter.sideShadow(canvas, edgePaint, edgeShadowRect, outline.right, inset.bottom, 2);
            // Top shadow
            edgeShadowRect.set(-shadowSize, 0, 0, inset.width());
            android.view.RectShadowPainter.sideShadow(canvas, edgePaint, edgeShadowRect, inset.right, outline.top, 1);
            // bottom shadow. This needs an inset so that blank doesn't appear when the content is
            // moved up.
            edgeShadowRect.set(-shadowSize, 0, shadowSize / 2.0F, inset.width());
            edgePaint.setShader(new android.graphics.LinearGradient(edgeShadowRect.right, 0, edgeShadowRect.left, 0, colors, new float[]{ 0.0F, 1 / 3.0F, 1.0F }, android.graphics.Shader.TileMode.CLAMP));
            android.view.RectShadowPainter.sideShadow(canvas, edgePaint, edgeShadowRect, inset.left, outline.bottom, 3);
            // Draw corners.
            android.view.RectShadowPainter.drawCorner(canvas, cornerPaint, path, inset.right, inset.bottom, outerArcRadius, 0);
            android.view.RectShadowPainter.drawCorner(canvas, cornerPaint, path, inset.left, inset.bottom, outerArcRadius, 1);
            android.view.RectShadowPainter.drawCorner(canvas, cornerPaint, path, inset.left, inset.top, outerArcRadius, 2);
            android.view.RectShadowPainter.drawCorner(canvas, cornerPaint, path, inset.right, inset.top, outerArcRadius, 3);
        } finally {
            canvas.restoreToCount(saved);
        }
    }

    private static float elevationToShadow(float elevation) {
        // The factor is chosen by eyeballing the shadow size on device and preview.
        return elevation * 0.5F;
    }

    /**
     * Translate canvas by half of shadow size up, so that it appears that light is coming
     * slightly from above. Also, remove clipping, so that shadow is not clipped.
     */
    private static int modifyCanvas(android.graphics.Canvas canvas, float shadowSize) {
        android.graphics.Rect clipBounds = canvas.getClipBounds();
        if (clipBounds.isEmpty()) {
            return -1;
        }
        int saved = canvas.save();
        // Usually canvas has been translated to the top left corner of the view when this is
        // called. So, setting a clip rect at 0,0 will clip the top left part of the shadow.
        // Thus, we just expand in each direction by width and height of the canvas, while staying
        // inside the original drawing region.
        com.android.layoutlib.bridge.impl.GcSnapshot snapshot = android.graphics.Canvas_Delegate.getDelegate(canvas).getSnapshot();
        java.awt.Rectangle originalClip = snapshot.getOriginalClip();
        if (originalClip != null) {
            canvas.clipRect(originalClip.x, originalClip.y, originalClip.x + originalClip.width, originalClip.y + originalClip.height, android.graphics.Region.Op.REPLACE);
            canvas.clipRect(-canvas.getWidth(), -canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), android.graphics.Region.Op.INTERSECT);
        }
        canvas.translate(0, shadowSize / 2.0F);
        return saved;
    }

    private static void sideShadow(android.graphics.Canvas canvas, android.graphics.Paint edgePaint, android.graphics.RectF edgeShadowRect, float dx, float dy, int rotations) {
        if (android.view.RectShadowPainter.isRectEmpty(edgeShadowRect)) {
            return;
        }
        int saved = canvas.save();
        canvas.translate(dx, dy);
        canvas.rotate(rotations * android.view.RectShadowPainter.PERPENDICULAR_ANGLE);
        canvas.drawRect(edgeShadowRect, edgePaint);
        canvas.restoreToCount(saved);
    }

    /**
     *
     *
     * @param canvas
     * 		Canvas to draw the rectangle on.
     * @param paint
     * 		Paint to use when drawing the corner.
     * @param path
     * 		A path to reuse. Prevents allocating memory for each path.
     * @param x
     * 		Center of circle, which this corner is a part of.
     * @param y
     * 		Center of circle, which this corner is a part of.
     * @param radius
     * 		radius of the arc
     * @param rotations
     * 		number of quarter rotations before starting to paint the arc.
     */
    private static void drawCorner(android.graphics.Canvas canvas, android.graphics.Paint paint, android.graphics.Path path, float x, float y, float radius, int rotations) {
        int saved = canvas.save();
        canvas.translate(x, y);
        path.reset();
        path.arcTo(-radius, -radius, radius, radius, rotations * android.view.RectShadowPainter.PERPENDICULAR_ANGLE, android.view.RectShadowPainter.PERPENDICULAR_ANGLE, false);
        path.lineTo(0, 0);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restoreToCount(saved);
    }

    /**
     * Differs from {@link RectF#isEmpty()} as this first converts the rect to int and then checks.
     * <p/>
     * This is required because {@link BaseCanvas_Delegate#native_drawRect(long, float, float,
     * float,
     * float, long)} casts the co-ordinates to int and we want to ensure that it doesn't end up
     * drawing empty rectangles, which results in IllegalArgumentException.
     */
    private static boolean isRectEmpty(android.graphics.RectF rect) {
        return (((int) (rect.left)) >= ((int) (rect.right))) || (((int) (rect.top)) >= ((int) (rect.bottom)));
    }
}

