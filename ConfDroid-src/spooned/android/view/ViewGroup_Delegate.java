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
package android.view;


/**
 * Delegate used to provide new implementation of a select few methods of {@link ViewGroup}
 * <p/>
 * Through the layoutlib_create tool, the original  methods of ViewGroup have been replaced by calls
 * to methods of the same name in this delegate class.
 */
public class ViewGroup_Delegate {
    /**
     * Overrides the original drawChild call in ViewGroup to draw the shadow.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean drawChild(android.view.ViewGroup thisVG, android.graphics.Canvas canvas, android.view.View child, long drawingTime) {
        if (child.getZ() > thisVG.getZ()) {
            // The background's bounds are set lazily. Make sure they are set correctly so that
            // the outline obtained is correct.
            child.setBackgroundBounds();
            android.view.ViewOutlineProvider outlineProvider = child.getOutlineProvider();
            if (outlineProvider != null) {
                android.graphics.Outline outline = child.mAttachInfo.mTmpOutline;
                outlineProvider.getOutline(child, outline);
                if ((outline.mPath != null) || ((outline.mRect != null) && (!outline.mRect.isEmpty()))) {
                    int restoreTo = android.view.ViewGroup_Delegate.transformCanvas(thisVG, canvas, child);
                    android.view.ViewGroup_Delegate.drawShadow(thisVG, canvas, child, outline);
                    canvas.restoreToCount(restoreTo);
                }
            }
        }
        return thisVG.drawChild_Original(canvas, child, drawingTime);
    }

    private static void drawShadow(android.view.ViewGroup parent, android.graphics.Canvas canvas, android.view.View child, android.graphics.Outline outline) {
        boolean highQualityShadow = false;
        boolean enableShadow = true;
        float elevation = android.view.ViewGroup_Delegate.getElevation(child, parent);
        android.content.Context bridgeContext = parent.getContext();
        if (bridgeContext instanceof com.android.layoutlib.bridge.android.BridgeContext) {
            highQualityShadow = ((com.android.layoutlib.bridge.android.BridgeContext) (bridgeContext)).getLayoutlibCallback().getFlag(RenderParamsFlags.FLAG_RENDER_HIGH_QUALITY_SHADOW);
            enableShadow = ((com.android.layoutlib.bridge.android.BridgeContext) (bridgeContext)).getLayoutlibCallback().getFlag(RenderParamsFlags.FLAG_ENABLE_SHADOW);
        }
        if (!enableShadow) {
            return;
        }
        if ((outline.mMode == android.graphics.Outline.MODE_ROUND_RECT) && (outline.mRect != null)) {
            if (highQualityShadow) {
                float densityDpi = bridgeContext.getResources().getDisplayMetrics().densityDpi;
                android.view.shadow.HighQualityShadowPainter.paintRectShadow(parent, outline, elevation, canvas, child.getAlpha(), densityDpi);
            } else {
                android.view.RectShadowPainter.paintShadow(outline, elevation, canvas, child.getAlpha());
            }
            return;
        }
        java.awt.image.BufferedImage shadow = null;
        if (outline.mPath != null) {
            shadow = android.view.ViewGroup_Delegate.getPathShadow(outline, canvas, elevation, child.getAlpha());
        }
        if (shadow == null) {
            return;
        }
        android.graphics.Bitmap bitmap = android.graphics.Bitmap_Delegate.createBitmap(shadow, false, com.android.resources.Density.getEnum(canvas.getDensity()));
        canvas.save();
        android.graphics.Rect clipBounds = canvas.getClipBounds();
        android.graphics.Rect newBounds = new android.graphics.Rect(clipBounds);
        newBounds.inset(((int) (-elevation)), ((int) (-elevation)));
        canvas.clipRectUnion(newBounds);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }

    private static float getElevation(android.view.View child, android.view.ViewGroup parent) {
        return child.getZ() - parent.getZ();
    }

    private static java.awt.image.BufferedImage getPathShadow(android.graphics.Outline outline, android.graphics.Canvas canvas, float elevation, float alpha) {
        android.graphics.Rect clipBounds = canvas.getClipBounds();
        if (clipBounds.isEmpty()) {
            return null;
        }
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(clipBounds.width(), clipBounds.height(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics = image.createGraphics();
        graphics.draw(android.graphics.Path_Delegate.getDelegate(outline.mPath.mNativePath).getJavaShape());
        graphics.dispose();
        return android.view.ShadowPainter.createDropShadow(image, ((int) (elevation)), alpha);
    }

    // Copied from android.view.View#draw(Canvas, ViewGroup, long) and removed code paths
    // which were never taken. Ideally, we should hook up the shadow code in the same method so
    // that we don't have to transform the canvas twice.
    private static int transformCanvas(android.view.ViewGroup thisVG, android.graphics.Canvas canvas, android.view.View child) {
        final int restoreTo = canvas.save();
        final boolean childHasIdentityMatrix = child.hasIdentityMatrix();
        int flags = thisVG.mGroupFlags;
        android.view.animation.Transformation transformToApply = null;
        boolean concatMatrix = false;
        if ((flags & android.view.ViewGroup.FLAG_SUPPORT_STATIC_TRANSFORMATIONS) != 0) {
            final android.view.animation.Transformation t = thisVG.getChildTransformation();
            final boolean hasTransform = thisVG.getChildStaticTransformation(child, t);
            if (hasTransform) {
                final int transformType = t.getTransformationType();
                transformToApply = (transformType != android.view.animation.Transformation.TYPE_IDENTITY) ? t : null;
                concatMatrix = (transformType & android.view.animation.Transformation.TYPE_MATRIX) != 0;
            }
        }
        concatMatrix |= childHasIdentityMatrix;
        child.computeScroll();
        int sx = child.mScrollX;
        int sy = child.mScrollY;
        canvas.translate(child.mLeft - sx, child.mTop - sy);
        float alpha = child.getAlpha() * child.getTransitionAlpha();
        if (((transformToApply != null) || (alpha < 1)) || (!childHasIdentityMatrix)) {
            if ((transformToApply != null) || (!childHasIdentityMatrix)) {
                int transX = -sx;
                int transY = -sy;
                if (transformToApply != null) {
                    if (concatMatrix) {
                        // Undo the scroll translation, apply the transformation matrix,
                        // then redo the scroll translate to get the correct result.
                        canvas.translate(-transX, -transY);
                        canvas.concat(transformToApply.getMatrix());
                        canvas.translate(transX, transY);
                    }
                }
                if (!childHasIdentityMatrix) {
                    canvas.translate(-transX, -transY);
                    canvas.concat(child.getMatrix());
                    canvas.translate(transX, transY);
                }
            }
        }
        return restoreTo;
    }
}

