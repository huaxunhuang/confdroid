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
 * Defines a generic graphical "shape."
 * <p>
 * Any Shape can be drawn to a Canvas with its own draw() method, but more
 * graphical control is available if you instead pass it to a
 * {@link android.graphics.drawable.ShapeDrawable}.
 * <p>
 * Custom Shape classes must implement {@link #clone()} and return an instance
 * of the custom Shape class.
 */
public abstract class Shape implements java.lang.Cloneable {
    private float mWidth;

    private float mHeight;

    /**
     * Returns the width of the Shape.
     */
    public final float getWidth() {
        return mWidth;
    }

    /**
     * Returns the height of the Shape.
     */
    public final float getHeight() {
        return mHeight;
    }

    /**
     * Draws this shape into the provided Canvas, with the provided Paint.
     * <p>
     * Before calling this, you must call {@link #resize(float,float)}.
     *
     * @param canvas
     * 		the Canvas within which this shape should be drawn
     * @param paint
     * 		the Paint object that defines this shape's characteristics
     */
    public abstract void draw(android.graphics.Canvas canvas, android.graphics.Paint paint);

    /**
     * Resizes the dimensions of this shape.
     * <p>
     * Must be called before {@link #draw(Canvas,Paint)}.
     *
     * @param width
     * 		the width of the shape (in pixels)
     * @param height
     * 		the height of the shape (in pixels)
     */
    public final void resize(float width, float height) {
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        if ((mWidth != width) || (mHeight != height)) {
            mWidth = width;
            mHeight = height;
            onResize(width, height);
        }
    }

    /**
     * Checks whether the Shape is opaque.
     * <p>
     * Default impl returns {@code true}. Override if your subclass can be
     * opaque.
     *
     * @return true if any part of the drawable is <em>not</em> opaque.
     */
    public boolean hasAlpha() {
        return true;
    }

    /**
     * Callback method called when {@link #resize(float,float)} is executed.
     *
     * @param width
     * 		the new width of the Shape
     * @param height
     * 		the new height of the Shape
     */
    protected void onResize(float width, float height) {
    }

    /**
     * Computes the Outline of the shape and return it in the supplied Outline
     * parameter. The default implementation does nothing and {@code outline}
     * is not changed.
     *
     * @param outline
     * 		the Outline to be populated with the result. Must be
     * 		non-{@code null}.
     */
    public void getOutline(@android.annotation.NonNull
    android.graphics.Outline outline) {
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.Shape clone() throws java.lang.CloneNotSupportedException {
        return ((android.graphics.drawable.shapes.Shape) (super.clone()));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.graphics.drawable.shapes.Shape shape = ((android.graphics.drawable.shapes.Shape) (o));
        return (java.lang.Float.compare(shape.mWidth, mWidth) == 0) && (java.lang.Float.compare(shape.mHeight, mHeight) == 0);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mWidth, mHeight);
    }
}

