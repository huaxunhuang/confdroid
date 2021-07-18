/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view.shadow;


public class HighQualityShadowPainter {
    private HighQualityShadowPainter() {
    }

    /**
     * Draws simple Rect shadow
     */
    public static void paintRectShadow(android.view.ViewGroup parent, android.graphics.Outline outline, float elevation, android.graphics.Canvas canvas, float alpha, float densityDpi) {
        if (!android.view.shadow.HighQualityShadowPainter.validate(elevation, densityDpi)) {
            return;
        }
        int width = parent.getWidth() / android.view.shadow.ShadowConstants.SCALE_DOWN;
        int height = parent.getHeight() / android.view.shadow.ShadowConstants.SCALE_DOWN;
        android.graphics.Rect rectOriginal = new android.graphics.Rect();
        android.graphics.Rect rectScaled = new android.graphics.Rect();
        if ((!outline.getRect(rectScaled)) || (alpha < android.view.shadow.ShadowConstants.MIN_ALPHA)) {
            // If alpha below MIN_ALPHA it's invisible (based on manual test). Save some perf.
            return;
        }
        outline.getRect(rectOriginal);
        rectScaled.left /= android.view.shadow.ShadowConstants.SCALE_DOWN;
        rectScaled.right /= android.view.shadow.ShadowConstants.SCALE_DOWN;
        rectScaled.top /= android.view.shadow.ShadowConstants.SCALE_DOWN;
        rectScaled.bottom /= android.view.shadow.ShadowConstants.SCALE_DOWN;
        float radius = outline.getRadius() / android.view.shadow.ShadowConstants.SCALE_DOWN;
        if ((radius > rectScaled.width()) || (radius > rectScaled.height())) {
            // Rounded edge generation fails if radius is bigger than drawing box.
            return;
        }
        // ensure alpha doesn't go over 1
        alpha = (alpha > 1.0F) ? 1.0F : alpha;
        float[] poly = android.view.shadow.HighQualityShadowPainter.getPoly(rectScaled, elevation / android.view.shadow.ShadowConstants.SCALE_DOWN, radius);
        android.view.shadow.HighQualityShadowPainter.paintAmbientShadow(poly, canvas, width, height, alpha, rectOriginal, radius);
        android.view.shadow.HighQualityShadowPainter.paintSpotShadow(poly, rectScaled, elevation / android.view.shadow.ShadowConstants.SCALE_DOWN, canvas, densityDpi, width, height, alpha, rectOriginal, radius);
    }

    /**
     * High quality shadow does not work well with object that is too high in elevation. Check if
     * the object elevation is reasonable and returns true if shadow will work well. False other
     * wise.
     */
    private static boolean validate(float elevation, float densityDpi) {
        float scaledElevationPx = elevation / android.view.shadow.ShadowConstants.SCALE_DOWN;
        float scaledSpotLightHeightPx = android.view.shadow.ShadowConstants.SPOT_SHADOW_LIGHT_Z_HEIGHT_DP * (densityDpi / android.util.DisplayMetrics.DENSITY_DEFAULT);
        if (scaledElevationPx > scaledSpotLightHeightPx) {
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @param polygon
     * 		- polygon of the shadow caster
     * @param canvas
     * 		- canvas to draw
     * @param width
     * 		- scaled canvas (parent) width
     * @param height
     * 		- scaled canvas (parent) height
     * @param alpha
     * 		- 0-1 scale
     * @param shadowCasterOutline
     * 		- unscaled original shadow caster outline.
     * @param radius
     * 		
     */
    private static void paintAmbientShadow(float[] polygon, android.graphics.Canvas canvas, int width, int height, float alpha, android.graphics.Rect shadowCasterOutline, float radius) {
        // TODO: Consider re-using the triangle buffer here since the world stays consistent.
        // TODO: Reduce the buffer size based on shadow bounds.
        android.view.shadow.AmbientShadowConfig config = new android.view.shadow.AmbientShadowConfig.Builder().setSize(width, height).setPolygon(polygon).setEdgeScale(android.view.shadow.ShadowConstants.AMBIENT_SHADOW_EDGE_SCALE).setShadowBoundRatio(android.view.shadow.ShadowConstants.AMBIENT_SHADOW_SHADOW_BOUND).setShadowStrength(android.view.shadow.ShadowConstants.AMBIENT_SHADOW_STRENGTH * alpha).setRays(android.view.shadow.ShadowConstants.AMBIENT_SHADOW_RAYS).setLayers(android.view.shadow.ShadowConstants.AMBIENT_SHADOW_LAYERS).build();
        android.view.shadow.AmbientShadowBitmapGenerator generator = new android.view.shadow.AmbientShadowBitmapGenerator(config);
        generator.populateShadow();
        if (!generator.isValid()) {
            return;
        }
        android.view.shadow.HighQualityShadowPainter.drawScaled(canvas, generator.getBitmap(), ((int) (generator.getTranslateX())), ((int) (generator.getTranslateY())), width, height, shadowCasterOutline, radius);
    }

    /**
     *
     *
     * @param poly
     * 		- polygon of the shadow caster
     * @param rectBound
     * 		- scaled bounds of shadow caster.
     * @param canvas
     * 		- canvas to draw
     * @param width
     * 		- scaled canvas (parent) width
     * @param height
     * 		- scaled canvas (parent) height
     * @param alpha
     * 		- 0-1 scale
     * @param shadowCasterOutline
     * 		- unscaled original shadow caster outline.
     * @param radius
     * 		
     */
    private static void paintSpotShadow(float[] poly, android.graphics.Rect rectBound, float elevation, android.graphics.Canvas canvas, float densityDpi, int width, int height, float alpha, android.graphics.Rect shadowCasterOutline, float radius) {
        // TODO: Use alpha later
        float lightZHeightPx = android.view.shadow.ShadowConstants.SPOT_SHADOW_LIGHT_Z_HEIGHT_DP * (densityDpi / android.util.DisplayMetrics.DENSITY_DEFAULT);
        if ((lightZHeightPx - elevation) < android.view.shadow.ShadowConstants.SPOT_SHADOW_LIGHT_Z_EPSILON) {
            // If the view is above or too close to the light source then return.
            // This is done to somewhat simulate android behaviour.
            return;
        }
        float lightX = (rectBound.left + rectBound.right) / 2;
        float lightY = rectBound.top;
        // Light shouldn't be bigger than the object by too much.
        int dynamicLightRadius = java.lang.Math.min(rectBound.width(), rectBound.height());
        android.view.shadow.SpotShadowConfig config = new android.view.shadow.SpotShadowConfig.Builder().setSize(width, height).setLayers(android.view.shadow.ShadowConstants.SPOT_SHADOW_LAYERS).setRays(android.view.shadow.ShadowConstants.SPOT_SHADOW_RAYS).setLightCoord(lightX, lightY, lightZHeightPx).setLightRadius(dynamicLightRadius).setLightSourcePoints(android.view.shadow.ShadowConstants.SPOT_SHADOW_LIGHT_SOURCE_POINTS).setShadowStrength(android.view.shadow.ShadowConstants.SPOT_SHADOW_STRENGTH * alpha).setPolygon(poly, poly.length / android.view.shadow.ShadowConstants.COORDINATE_SIZE).build();
        android.view.shadow.SpotShadowBitmapGenerator generator = new android.view.shadow.SpotShadowBitmapGenerator(config);
        generator.populateShadow();
        if (!generator.validate()) {
            return;
        }
        android.view.shadow.HighQualityShadowPainter.drawScaled(canvas, generator.getBitmap(), ((int) (generator.getTranslateX())), ((int) (generator.getTranslateY())), width, height, shadowCasterOutline, radius);
    }

    /**
     * Draw the bitmap scaled up.
     *
     * @param translateX
     * 		- offset in x axis by which the bitmap is shifted.
     * @param translateY
     * 		- offset in y axis by which the bitmap is shifted.
     * @param width
     * 		- scaled width of canvas (parent)
     * @param height
     * 		- scaled height of canvas (parent)
     * @param shadowCaster
     * 		- unscaled outline of shadow caster
     * @param radius
     * 		
     */
    private static void drawScaled(android.graphics.Canvas canvas, android.graphics.Bitmap bitmap, int translateX, int translateY, int width, int height, android.graphics.Rect shadowCaster, float radius) {
        int unscaledTranslateX = translateX * android.view.shadow.ShadowConstants.SCALE_DOWN;
        int unscaledTranslateY = translateY * android.view.shadow.ShadowConstants.SCALE_DOWN;
        // To the canvas
        android.graphics.Rect dest = new android.graphics.Rect(-unscaledTranslateX, -unscaledTranslateY, (width * android.view.shadow.ShadowConstants.SCALE_DOWN) - unscaledTranslateX, (height * android.view.shadow.ShadowConstants.SCALE_DOWN) - unscaledTranslateY);
        android.graphics.Rect destSrc = new android.graphics.Rect(0, 0, width, height);
        if (radius > 0) {
            // Rounded edge.
            int save = canvas.save();
            canvas.drawBitmap(bitmap, destSrc, dest, null);
            canvas.restoreToCount(save);
            return;
        }
        /**
         * ----------------------------------
         * |                                |
         * |              top               |
         * |                                |
         * ----------------------------------
         * |      |                 |       |
         * | left |  shadow caster  | right |
         * |      |                 |       |
         * ----------------------------------
         * |                                |
         * |            bottom              |
         * |                                |
         * ----------------------------------
         *
         * dest == top + left + shadow caster + right + bottom
         * Visually, canvas.drawBitmap(bitmap, destSrc, dest, paint) would achieve the same result.
         */
        android.graphics.Rect left = new android.graphics.Rect(dest.left, shadowCaster.top, shadowCaster.left, shadowCaster.bottom);
        int leftScaled = (left.width() / android.view.shadow.ShadowConstants.SCALE_DOWN) + destSrc.left;
        android.graphics.Rect top = new android.graphics.Rect(dest.left, dest.top, dest.right, shadowCaster.top);
        int topScaled = (top.height() / android.view.shadow.ShadowConstants.SCALE_DOWN) + destSrc.top;
        android.graphics.Rect right = new android.graphics.Rect(shadowCaster.right, shadowCaster.top, dest.right, shadowCaster.bottom);
        int rightScaled = ((shadowCaster.right + unscaledTranslateX) / android.view.shadow.ShadowConstants.SCALE_DOWN) + destSrc.left;
        android.graphics.Rect bottom = new android.graphics.Rect(dest.left, shadowCaster.bottom, dest.right, dest.bottom);
        int bottomScaled = ((bottom.bottom - bottom.height()) / android.view.shadow.ShadowConstants.SCALE_DOWN) + destSrc.top;
        // calculate parts of the middle ground that can be ignored.
        android.graphics.Rect leftSrc = new android.graphics.Rect(destSrc.left, topScaled, leftScaled, bottomScaled);
        android.graphics.Rect topSrc = new android.graphics.Rect(destSrc.left, destSrc.top, destSrc.right, topScaled);
        android.graphics.Rect rightSrc = new android.graphics.Rect(rightScaled, topScaled, destSrc.right, bottomScaled);
        android.graphics.Rect bottomSrc = new android.graphics.Rect(destSrc.left, bottomScaled, destSrc.right, destSrc.bottom);
        int save = canvas.save();
        android.graphics.Paint paint = new android.graphics.Paint();
        canvas.drawBitmap(bitmap, leftSrc, left, paint);
        canvas.drawBitmap(bitmap, topSrc, top, paint);
        canvas.drawBitmap(bitmap, rightSrc, right, paint);
        canvas.drawBitmap(bitmap, bottomSrc, bottom, paint);
        canvas.restoreToCount(save);
    }

    private static float[] getPoly(android.graphics.Rect rect, float elevation, float radius) {
        if (radius <= 0) {
            float[] poly = new float[android.view.shadow.ShadowConstants.RECT_VERTICES_SIZE * android.view.shadow.ShadowConstants.COORDINATE_SIZE];
            poly[0] = poly[9] = rect.left;
            poly[1] = poly[4] = rect.top;
            poly[3] = poly[6] = rect.right;
            poly[7] = poly[10] = rect.bottom;
            poly[2] = poly[5] = poly[8] = poly[11] = elevation;
            return poly;
        }
        return android.view.shadow.HighQualityShadowPainter.buildRoundedEdges(rect, elevation, radius);
    }

    private static float[] buildRoundedEdges(android.graphics.Rect rect, float elevation, float radius) {
        float[] roundedEdgeVertices = new float[((android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE + 1) * 4) * 3];
        int index = 0;
        // 1.0 LT. From theta 0 to pi/2 in K division.
        for (int i = 0; i <= android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE; i++) {
            double theta = (java.lang.Math.PI / 2.0) * (((double) (i)) / android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE);
            float x = ((float) (rect.left + (radius - (radius * java.lang.Math.cos(theta)))));
            float y = ((float) (rect.top + (radius - (radius * java.lang.Math.sin(theta)))));
            roundedEdgeVertices[index++] = x;
            roundedEdgeVertices[index++] = y;
            roundedEdgeVertices[index++] = elevation;
        }
        // 2.0 RT
        for (int i = android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE; i >= 0; i--) {
            double theta = (java.lang.Math.PI / 2.0) * (((double) (i)) / android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE);
            float x = ((float) (rect.right - (radius - (radius * java.lang.Math.cos(theta)))));
            float y = ((float) (rect.top + (radius - (radius * java.lang.Math.sin(theta)))));
            roundedEdgeVertices[index++] = x;
            roundedEdgeVertices[index++] = y;
            roundedEdgeVertices[index++] = elevation;
        }
        // 3.0 RB
        for (int i = 0; i <= android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE; i++) {
            double theta = (java.lang.Math.PI / 2.0) * (((double) (i)) / android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE);
            float x = ((float) (rect.right - (radius - (radius * java.lang.Math.cos(theta)))));
            float y = ((float) (rect.bottom - (radius - (radius * java.lang.Math.sin(theta)))));
            roundedEdgeVertices[index++] = x;
            roundedEdgeVertices[index++] = y;
            roundedEdgeVertices[index++] = elevation;
        }
        // 4.0 LB
        for (int i = android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE; i >= 0; i--) {
            double theta = (java.lang.Math.PI / 2.0) * (((double) (i)) / android.view.shadow.ShadowConstants.SPLICE_ROUNDED_EDGE);
            float x = ((float) (rect.left + (radius - (radius * java.lang.Math.cos(theta)))));
            float y = ((float) (rect.bottom - (radius - (radius * java.lang.Math.sin(theta)))));
            roundedEdgeVertices[index++] = x;
            roundedEdgeVertices[index++] = y;
            roundedEdgeVertices[index++] = elevation;
        }
        return roundedEdgeVertices;
    }
}

