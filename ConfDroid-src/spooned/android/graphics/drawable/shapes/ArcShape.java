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
 * Creates an arc shape. The arc shape starts at a specified angle and sweeps
 * clockwise, drawing slices of pie.
 * <p>
 * The arc can be drawn to a {@link Canvas} with its own
 * {@link #draw(Canvas, Paint)} method, but more graphical control is available
 * if you instead pass the ArcShape to a
 * {@link android.graphics.drawable.ShapeDrawable}.
 */
public class ArcShape extends android.graphics.drawable.shapes.RectShape {
    private final float mStartAngle;

    private final float mSweepAngle;

    /**
     * ArcShape constructor.
     *
     * @param startAngle
     * 		the angle (in degrees) where the arc begins
     * @param sweepAngle
     * 		the sweep angle (in degrees). Anything equal to or
     * 		greater than 360 results in a complete circle/oval.
     */
    public ArcShape(float startAngle, float sweepAngle) {
        mStartAngle = startAngle;
        mSweepAngle = sweepAngle;
    }

    /**
     *
     *
     * @return the angle (in degrees) where the arc begins
     */
    public final float getStartAngle() {
        return mStartAngle;
    }

    /**
     *
     *
     * @return the sweep angle (in degrees)
     */
    public final float getSweepAngle() {
        return mSweepAngle;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, android.graphics.Paint paint) {
        canvas.drawArc(rect(), mStartAngle, mSweepAngle, true, paint);
    }

    @java.lang.Override
    public void getOutline(android.graphics.Outline outline) {
        // Since we don't support concave outlines, arc shape does not attempt
        // to provide an outline.
    }

    @java.lang.Override
    public android.graphics.drawable.shapes.ArcShape clone() throws java.lang.CloneNotSupportedException {
        return ((android.graphics.drawable.shapes.ArcShape) (super.clone()));
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
        android.graphics.drawable.shapes.ArcShape arcShape = ((android.graphics.drawable.shapes.ArcShape) (o));
        return (java.lang.Float.compare(arcShape.mStartAngle, mStartAngle) == 0) && (java.lang.Float.compare(arcShape.mSweepAngle, mSweepAngle) == 0);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), mStartAngle, mSweepAngle);
    }
}

