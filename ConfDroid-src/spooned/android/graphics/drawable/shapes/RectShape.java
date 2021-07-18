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
 * Defines a rectangle shape.
 * <p>
 * The rectangle can be drawn to a Canvas with its own draw() method,
 * but more graphical control is available if you instead pass
 * the RectShape to a {@link android.graphics.drawable.ShapeDrawable}.
 */
public class RectShape extends android.graphics.drawable.shapes.Shape {
    private android.graphics.RectF mRect = new android.graphics.RectF();

    public RectShape() {
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        canvas.drawRect(mRect, paint);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        final android.graphics.RectF rect = rect();
        outline.setRect(((int) (java.lang.Math.ceil(rect.left))), ((int) (java.lang.Math.ceil(rect.top))), ((int) (java.lang.Math.floor(rect.right))), ((int) (java.lang.Math.floor(rect.bottom))));
    }

    @java.lang.Override
    protected void onResize(float width, float height) {
        mRect.set(0, 0, width, height);
    }

    /**
     * Returns the RectF that defines this rectangle's bounds.
     */
    protected final android.graphics.RectF rect() {
        return mRect;
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.RectShape clone() throws java.lang.CloneNotSupportedException {
        final android.graphics.drawable.shapes.RectShape shape = ((android.graphics.drawable.shapes.RectShape) (super.clone()));
        shape.mRect = new android.graphics.RectF(mRect);
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
        android.graphics.drawable.shapes.RectShape rectShape = ((android.graphics.drawable.shapes.RectShape) (o));
        return java.util.Objects.equals(mRect, rectShape.mRect);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), mRect);
    }
}

