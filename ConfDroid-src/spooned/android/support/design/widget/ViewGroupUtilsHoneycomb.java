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
package android.support.design.widget;


class ViewGroupUtilsHoneycomb {
    private static final java.lang.ThreadLocal<android.graphics.Matrix> sMatrix = new java.lang.ThreadLocal<>();

    private static final java.lang.ThreadLocal<android.graphics.RectF> sRectF = new java.lang.ThreadLocal<>();

    public static void offsetDescendantRect(android.view.ViewGroup group, android.view.View child, android.graphics.Rect rect) {
        android.graphics.Matrix m = android.support.design.widget.ViewGroupUtilsHoneycomb.sMatrix.get();
        if (m == null) {
            m = new android.graphics.Matrix();
            android.support.design.widget.ViewGroupUtilsHoneycomb.sMatrix.set(m);
        } else {
            m.reset();
        }
        android.support.design.widget.ViewGroupUtilsHoneycomb.offsetDescendantMatrix(group, child, m);
        android.graphics.RectF rectF = android.support.design.widget.ViewGroupUtilsHoneycomb.sRectF.get();
        if (rectF == null) {
            rectF = new android.graphics.RectF();
            android.support.design.widget.ViewGroupUtilsHoneycomb.sRectF.set(rectF);
        }
        rectF.set(rect);
        m.mapRect(rectF);
        rect.set(((int) (rectF.left + 0.5F)), ((int) (rectF.top + 0.5F)), ((int) (rectF.right + 0.5F)), ((int) (rectF.bottom + 0.5F)));
    }

    static void offsetDescendantMatrix(android.view.ViewParent target, android.view.View view, android.graphics.Matrix m) {
        final android.view.ViewParent parent = view.getParent();
        if ((parent instanceof android.view.View) && (parent != target)) {
            final android.view.View vp = ((android.view.View) (parent));
            android.support.design.widget.ViewGroupUtilsHoneycomb.offsetDescendantMatrix(target, vp, m);
            m.preTranslate(-vp.getScrollX(), -vp.getScrollY());
        }
        m.preTranslate(view.getLeft(), view.getTop());
        if (!view.getMatrix().isIdentity()) {
            m.preConcat(view.getMatrix());
        }
    }
}

