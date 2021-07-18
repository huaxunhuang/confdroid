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
 * Creates geometric paths, utilizing the {@link android.graphics.Path} class.
 * <p>
 * The path can be drawn to a Canvas with its own draw() method,
 * but more graphical control is available if you instead pass
 * the PathShape to a {@link android.graphics.drawable.ShapeDrawable}.
 */
public class PathShape extends android.graphics.drawable.shapes.Shape {
    private final float mStdWidth;

    private final float mStdHeight;

    private android.graphics.Path mPath;

    private float mScaleX;// cached from onResize


    private float mScaleY;// cached from onResize


    /**
     * PathShape constructor.
     *
     * @param path
     * 		a Path that defines the geometric paths for this shape
     * @param stdWidth
     * 		the standard width for the shape. Any changes to the
     * 		width with resize() will result in a width scaled based
     * 		on the new width divided by this width.
     * @param stdHeight
     * 		the standard height for the shape. Any changes to the
     * 		height with resize() will result in a height scaled based
     * 		on the new height divided by this height.
     */
    public PathShape(@android.annotation.NonNull
    android.graphics.Path path, float stdWidth, float stdHeight) {
        mPath = path;
        mStdWidth = stdWidth;
        mStdHeight = stdHeight;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        canvas.save();
        canvas.scale(mScaleX, mScaleY);
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }

    @java.lang.Override
    protected void onResize(float width, float height) {
        mScaleX = width / mStdWidth;
        mScaleY = height / mStdHeight;
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.PathShape clone() throws java.lang.CloneNotSupportedException {
        final android.graphics.drawable.shapes.PathShape shape = ((android.graphics.drawable.shapes.PathShape) (super.clone()));
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
        android.graphics.drawable.shapes.PathShape pathShape = ((android.graphics.drawable.shapes.PathShape) (o));
        return ((((java.lang.Float.compare(pathShape.mStdWidth, mStdWidth) == 0) && (java.lang.Float.compare(pathShape.mStdHeight, mStdHeight) == 0)) && (java.lang.Float.compare(pathShape.mScaleX, mScaleX) == 0)) && (java.lang.Float.compare(pathShape.mScaleY, mScaleY) == 0)) && // Path does not have equals implementation but incase it gains one, use it here
        java.util.Objects.equals(mPath, pathShape.mPath);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), mStdWidth, mStdHeight, mPath, mScaleX, mScaleY);
    }
}

