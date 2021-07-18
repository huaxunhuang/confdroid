/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.graphics.drawable.shapes;


/**
 * Creates a rounded-corner rectangle. Optionally, an inset (rounded) rectangle
 * can be included (to make a sort of "O" shape).
 * <p>
 * The rounded rectangle can be drawn to a Canvas with its own draw() method,
 * but more graphical control is available if you instead pass
 * the RoundRectShape to a {@link android.graphics.drawable.ShapeDrawable}.
 */
public class RoundRectShape extends android.graphics.drawable.shapes.RectShape {
    private float[] mOuterRadii;

    private android.graphics.RectF mInset;

    private float[] mInnerRadii;

    private android.graphics.RectF mInnerRect;

    private android.graphics.Path mPath;// this is what we actually draw


    /**
     * RoundRectShape constructor.
     * <p>
     * Specifies an outer (round)rect and an optional inner (round)rect.
     *
     * @param outerRadii
     * 		An array of 8 radius values, for the outer roundrect.
     * 		The first two floats are for the top-left corner
     * 		(remaining pairs correspond clockwise). For no rounded
     * 		corners on the outer rectangle, pass {@code null}.
     * @param inset
     * 		A RectF that specifies the distance from the inner
     * 		rect to each side of the outer rect. For no inner, pass
     * 		{@code null}.
     * @param innerRadii
     * 		An array of 8 radius values, for the inner roundrect.
     * 		The first two floats are for the top-left corner
     * 		(remaining pairs correspond clockwise). For no rounded
     * 		corners on the inner rectangle, pass {@code null}. If
     * 		inset parameter is {@code null}, this parameter is
     * 		ignored.
     */
    public RoundRectShape(@android.annotation.Nullable
    float[] outerRadii, @android.annotation.Nullable
    android.graphics.RectF inset, @android.annotation.Nullable
    float[] innerRadii) {
        if ((outerRadii != null) && (outerRadii.length < 8)) {
            throw new java.lang.ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
        }
        if ((innerRadii != null) && (innerRadii.length < 8)) {
            throw new java.lang.ArrayIndexOutOfBoundsException("inner radii must have >= 8 values");
        }
        mOuterRadii = outerRadii;
        mInset = inset;
        mInnerRadii = innerRadii;
        if (inset != null) {
            mInnerRect = new android.graphics.RectF();
        }
        mPath = new android.graphics.Path();
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        canvas.drawPath(mPath, paint);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        if (mInnerRect != null)
            return;
        // have a hole, can't produce valid outline

        float radius = 0;
        if (mOuterRadii != null) {
            radius = mOuterRadii[0];
            for (int i = 1; i < 8; i++) {
                if (mOuterRadii[i] != radius) {
                    // can't call simple constructors, use path
                    outline.setConvexPath(mPath);
                    return;
                }
            }
        }
        final android.graphics.RectF rect = rect();
        outline.setRoundRect(((int) (java.lang.Math.ceil(rect.left))), ((int) (java.lang.Math.ceil(rect.top))), ((int) (java.lang.Math.floor(rect.right))), ((int) (java.lang.Math.floor(rect.bottom))), radius);
    }

    @java.lang.Override
    protected void onResize(float w, float h) {
        super.onResize(w, h);
        android.graphics.RectF r = rect();
        mPath.reset();
        if (mOuterRadii != null) {
            mPath.addRoundRect(r, mOuterRadii, android.graphics.Path.Direction.CW);
        } else {
            mPath.addRect(r, android.graphics.Path.Direction.CW);
        }
        if (mInnerRect != null) {
            mInnerRect.set(r.left + mInset.left, r.top + mInset.top, r.right - mInset.right, r.bottom - mInset.bottom);
            if ((mInnerRect.width() < w) && (mInnerRect.height() < h)) {
                if (mInnerRadii != null) {
                    mPath.addRoundRect(mInnerRect, mInnerRadii, android.graphics.Path.Direction.CCW);
                } else {
                    mPath.addRect(mInnerRect, android.graphics.Path.Direction.CCW);
                }
            }
        }
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.RoundRectShape clone() throws java.lang.CloneNotSupportedException {
        final android.graphics.drawable.shapes.RoundRectShape shape = ((android.graphics.drawable.shapes.RoundRectShape) (super.clone()));
        shape.mOuterRadii = (mOuterRadii != null) ? mOuterRadii.clone() : null;
        shape.mInnerRadii = (mInnerRadii != null) ? mInnerRadii.clone() : null;
        shape.mInset = new android.graphics.RectF(mInset);
        shape.mInnerRect = new android.graphics.RectF(mInnerRect);
        shape.mPath = new android.graphics.Path(mPath);
        return shape;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        android.graphics.drawable.shapes.RoundRectShape that = ((android.graphics.drawable.shapes.RoundRectShape) (o));
        return (((java.util.Arrays.equals(mOuterRadii, that.mOuterRadii) && java.util.Objects.equals(mInset, that.mInset)) && java.util.Arrays.equals(mInnerRadii, that.mInnerRadii)) && java.util.Objects.equals(mInnerRect, that.mInnerRect)) && java.util.Objects.equals(mPath, that.mPath);
    }

    @java.lang.Override
    public int hashCode() {
        int result = java.util.Objects.hash(super.hashCode(), mInset, mInnerRect, mPath);
        result = (31 * result) + java.util.Arrays.hashCode(mOuterRadii);
        result = (31 * result) + java.util.Arrays.hashCode(mInnerRadii);
        return result;
    }
}

