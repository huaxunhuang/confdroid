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
 * Defines a rectangle with rounded corners, where the sizes of the corners
 * are potentially different.
 */
public class RoundRectangle extends java.awt.geom.RectangularShape {
    public double x;

    public double y;

    public double width;

    public double height;

    public double ulWidth;

    public double ulHeight;

    public double urWidth;

    public double urHeight;

    public double lrWidth;

    public double lrHeight;

    public double llWidth;

    public double llHeight;

    private enum Zone {

        CLOSE_OUTSIDE,
        CLOSE_INSIDE,
        MIDDLE,
        FAR_INSIDE,
        FAR_OUTSIDE;}

    private final java.util.EnumSet<android.graphics.RoundRectangle.Zone> close = java.util.EnumSet.of(android.graphics.RoundRectangle.Zone.CLOSE_OUTSIDE, android.graphics.RoundRectangle.Zone.CLOSE_INSIDE);

    private final java.util.EnumSet<android.graphics.RoundRectangle.Zone> far = java.util.EnumSet.of(android.graphics.RoundRectangle.Zone.FAR_OUTSIDE, android.graphics.RoundRectangle.Zone.FAR_INSIDE);

    /**
     *
     *
     * @param cornerDimensions
     * 		array of 8 floating-point number corresponding to the width and
     * 		the height of each corner in the following order: upper-left, upper-right, lower-right,
     * 		lower-left. It assumes for the size the same convention as {@link RoundRectangle2D}, that
     * 		is that the width and height of a corner correspond to the total width and height of the
     * 		ellipse that corner is a quarter of.
     */
    public RoundRectangle(float x, float y, float width, float height, float[] cornerDimensions) {
        assert cornerDimensions.length == 8 : "The array of corner dimensions must have eight " + "elements";
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float[] dimensions = cornerDimensions.clone();
        // If a value is negative, the corresponding corner is squared
        for (int i = 0; i < dimensions.length; i += 2) {
            if ((dimensions[i] < 0) || (dimensions[i + 1] < 0)) {
                dimensions[i] = 0;
                dimensions[i + 1] = 0;
            }
        }
        double topCornerWidth = (dimensions[0] + dimensions[2]) / 2.0;
        double bottomCornerWidth = (dimensions[4] + dimensions[6]) / 2.0;
        double leftCornerHeight = (dimensions[1] + dimensions[7]) / 2.0;
        double rightCornerHeight = (dimensions[3] + dimensions[5]) / 2.0;
        // Rescale the corner dimensions if they are bigger than the rectangle
        double scale = java.lang.Math.min(1.0, width / topCornerWidth);
        scale = java.lang.Math.min(scale, width / bottomCornerWidth);
        scale = java.lang.Math.min(scale, height / leftCornerHeight);
        scale = java.lang.Math.min(scale, height / rightCornerHeight);
        this.ulWidth = dimensions[0] * scale;
        this.ulHeight = dimensions[1] * scale;
        this.urWidth = dimensions[2] * scale;
        this.urHeight = dimensions[3] * scale;
        this.lrWidth = dimensions[4] * scale;
        this.lrHeight = dimensions[5] * scale;
        this.llWidth = dimensions[6] * scale;
        this.llHeight = dimensions[7] * scale;
    }

    @java.lang.Override
    public double getX() {
        return x;
    }

    @java.lang.Override
    public double getY() {
        return y;
    }

    @java.lang.Override
    public double getWidth() {
        return width;
    }

    @java.lang.Override
    public double getHeight() {
        return height;
    }

    @java.lang.Override
    public boolean isEmpty() {
        return (width <= 0.0) || (height <= 0.0);
    }

    @java.lang.Override
    public void setFrame(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    @java.lang.Override
    public java.awt.geom.Rectangle2D getBounds2D() {
        return new java.awt.geom.Rectangle2D.Double(x, y, width, height);
    }

    @java.lang.Override
    public boolean contains(double x, double y) {
        if (isEmpty()) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        double x1 = x0 + getWidth();
        double y1 = y0 + getHeight();
        // Check for trivial rejection - point is outside bounding rectangle
        if ((((x < x0) || (y < y0)) || (x >= x1)) || (y >= y1)) {
            return false;
        }
        double insideTopX0 = x0 + (ulWidth / 2.0);
        double insideLeftY0 = y0 + (ulHeight / 2.0);
        if ((x < insideTopX0) && (y < insideLeftY0)) {
            // In the upper-left corner
            return isInsideCorner(x - insideTopX0, y - insideLeftY0, ulWidth / 2.0, ulHeight / 2.0);
        }
        double insideTopX1 = x1 - (urWidth / 2.0);
        double insideRightY0 = y0 + (urHeight / 2.0);
        if ((x > insideTopX1) && (y < insideRightY0)) {
            // In the upper-right corner
            return isInsideCorner(x - insideTopX1, y - insideRightY0, urWidth / 2.0, urHeight / 2.0);
        }
        double insideBottomX1 = x1 - (lrWidth / 2.0);
        double insideRightY1 = y1 - (lrHeight / 2.0);
        if ((x > insideBottomX1) && (y > insideRightY1)) {
            // In the lower-right corner
            return isInsideCorner(x - insideBottomX1, y - insideRightY1, lrWidth / 2.0, lrHeight / 2.0);
        }
        double insideBottomX0 = x0 + (llWidth / 2.0);
        double insideLeftY1 = y1 - (llHeight / 2.0);
        if ((x < insideBottomX0) && (y > insideLeftY1)) {
            // In the lower-left corner
            return isInsideCorner(x - insideBottomX0, y - insideLeftY1, llWidth / 2.0, llHeight / 2.0);
        }
        // In the central part of the rectangle
        return true;
    }

    private boolean isInsideCorner(double x, double y, double width, double height) {
        double squareDist = (((height * height) * x) * x) + (((width * width) * y) * y);
        return squareDist <= (((width * width) * height) * height);
    }

    private android.graphics.RoundRectangle.Zone classify(double coord, double side1, double arcSize1, double side2, double arcSize2) {
        if (coord < side1) {
            return android.graphics.RoundRectangle.Zone.CLOSE_OUTSIDE;
        } else
            if (coord < (side1 + arcSize1)) {
                return android.graphics.RoundRectangle.Zone.CLOSE_INSIDE;
            } else
                if (coord < (side2 - arcSize2)) {
                    return android.graphics.RoundRectangle.Zone.MIDDLE;
                } else
                    if (coord < side2) {
                        return android.graphics.RoundRectangle.Zone.FAR_INSIDE;
                    } else {
                        return android.graphics.RoundRectangle.Zone.FAR_OUTSIDE;
                    }



    }

    public boolean intersects(double x, double y, double w, double h) {
        if ((isEmpty() || (w <= 0)) || (h <= 0)) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        double x1 = x0 + getWidth();
        double y1 = y0 + getHeight();
        // Check for trivial rejection - bounding rectangles do not intersect
        if (((((x + w) <= x0) || (x >= x1)) || ((y + h) <= y0)) || (y >= y1)) {
            return false;
        }
        double maxLeftCornerWidth = java.lang.Math.max(ulWidth, llWidth) / 2.0;
        double maxRightCornerWidth = java.lang.Math.max(urWidth, lrWidth) / 2.0;
        double maxUpperCornerHeight = java.lang.Math.max(ulHeight, urHeight) / 2.0;
        double maxLowerCornerHeight = java.lang.Math.max(llHeight, lrHeight) / 2.0;
        android.graphics.RoundRectangle.Zone x0class = classify(x, x0, maxLeftCornerWidth, x1, maxRightCornerWidth);
        android.graphics.RoundRectangle.Zone x1class = classify(x + w, x0, maxLeftCornerWidth, x1, maxRightCornerWidth);
        android.graphics.RoundRectangle.Zone y0class = classify(y, y0, maxUpperCornerHeight, y1, maxLowerCornerHeight);
        android.graphics.RoundRectangle.Zone y1class = classify(y + h, y0, maxUpperCornerHeight, y1, maxLowerCornerHeight);
        // Trivially accept if any point is inside inner rectangle
        if ((((x0class == android.graphics.RoundRectangle.Zone.MIDDLE) || (x1class == android.graphics.RoundRectangle.Zone.MIDDLE)) || (y0class == android.graphics.RoundRectangle.Zone.MIDDLE)) || (y1class == android.graphics.RoundRectangle.Zone.MIDDLE)) {
            return true;
        }
        // Trivially accept if either edge spans inner rectangle
        if ((close.contains(x0class) && far.contains(x1class)) || (close.contains(y0class) && far.contains(y1class))) {
            return true;
        }
        // Since neither edge spans the center, then one of the corners
        // must be in one of the rounded edges.  We detect this case if
        // a [xy]0class is 3 or a [xy]1class is 1.  One of those two cases
        // must be true for each direction.
        // We now find a "nearest point" to test for being inside a rounded
        // corner.
        if ((x1class == android.graphics.RoundRectangle.Zone.CLOSE_INSIDE) && (y1class == android.graphics.RoundRectangle.Zone.CLOSE_INSIDE)) {
            // Potentially in upper-left corner
            x = ((x + w) - x0) - (ulWidth / 2.0);
            y = ((y + h) - y0) - (ulHeight / 2.0);
            return ((x > 0) || (y > 0)) || isInsideCorner(x, y, ulWidth / 2.0, ulHeight / 2.0);
        }
        if (x1class == android.graphics.RoundRectangle.Zone.CLOSE_INSIDE) {
            // Potentially in lower-left corner
            x = ((x + w) - x0) - (llWidth / 2.0);
            y = (y - y1) + (llHeight / 2.0);
            return ((x > 0) || (y < 0)) || isInsideCorner(x, y, llWidth / 2.0, llHeight / 2.0);
        }
        if (y1class == android.graphics.RoundRectangle.Zone.CLOSE_INSIDE) {
            // Potentially in the upper-right corner
            x = (x - x1) + (urWidth / 2.0);
            y = ((y + h) - y0) - (urHeight / 2.0);
            return ((x < 0) || (y > 0)) || isInsideCorner(x, y, urWidth / 2.0, urHeight / 2.0);
        }
        // Potentially in the lower-right corner
        x = (x - x1) + (lrWidth / 2.0);
        y = (y - y1) + (lrHeight / 2.0);
        return ((x < 0) || (y < 0)) || isInsideCorner(x, y, lrWidth / 2.0, lrHeight / 2.0);
    }

    @java.lang.Override
    public boolean contains(double x, double y, double w, double h) {
        if ((isEmpty() || (w <= 0)) || (h <= 0)) {
            return false;
        }
        return ((contains(x, y) && contains(x + w, y)) && contains(x, y + h)) && contains(x + w, y + h);
    }

    @java.lang.Override
    public java.awt.geom.PathIterator getPathIterator(final java.awt.geom.AffineTransform at) {
        return new java.awt.geom.PathIterator() {
            int index;

            // ArcIterator.btan(Math.PI/2)
            public static final double CtrlVal = 0.5522847498307933;

            private final double ncv = 1.0 - CtrlVal;

            // Coordinates of control points for Bezier curves approximating the straight lines
            // and corners of the rounded rectangle.
            private final double[][] ctrlpts = new double[][]{ new double[]{ 0.0, 0.0, 0.0, ulHeight }, new double[]{ 0.0, 0.0, 1.0, -llHeight }, new double[]{ 0.0, 0.0, 1.0, (-llHeight) * ncv, 0.0, ncv * llWidth, 1.0, 0.0, 0.0, llWidth, 1.0, 0.0 }, new double[]{ 1.0, -lrWidth, 1.0, 0.0 }, new double[]{ 1.0, (-lrWidth) * ncv, 1.0, 0.0, 1.0, 0.0, 1.0, (-lrHeight) * ncv, 1.0, 0.0, 1.0, -lrHeight }, new double[]{ 1.0, 0.0, 0.0, urHeight }, new double[]{ 1.0, 0.0, 0.0, ncv * urHeight, 1.0, (-urWidth) * ncv, 0.0, 0.0, 1.0, -urWidth, 0.0, 0.0 }, new double[]{ 0.0, ulWidth, 0.0, 0.0 }, new double[]{ 0.0, ncv * ulWidth, 0.0, 0.0, 0.0, 0.0, 0.0, ncv * ulHeight, 0.0, 0.0, 0.0, ulHeight }, new double[]{  } };

            private final int[] types = new int[]{ java.awt.geom.PathIterator.SEG_MOVETO, java.awt.geom.PathIterator.SEG_LINETO, java.awt.geom.PathIterator.SEG_CUBICTO, java.awt.geom.PathIterator.SEG_LINETO, java.awt.geom.PathIterator.SEG_CUBICTO, java.awt.geom.PathIterator.SEG_LINETO, java.awt.geom.PathIterator.SEG_CUBICTO, java.awt.geom.PathIterator.SEG_LINETO, java.awt.geom.PathIterator.SEG_CUBICTO, java.awt.geom.PathIterator.SEG_CLOSE };

            @java.lang.Override
            public int getWindingRule() {
                return java.awt.geom.PathIterator.WIND_NON_ZERO;
            }

            @java.lang.Override
            public boolean isDone() {
                return index >= ctrlpts.length;
            }

            @java.lang.Override
            public void next() {
                index++;
            }

            @java.lang.Override
            public int currentSegment(float[] coords) {
                if (isDone()) {
                    throw new java.util.NoSuchElementException("roundrect iterator out of bounds");
                }
                int nc = 0;
                double[] ctrls = ctrlpts[index];
                for (int i = 0; i < ctrls.length; i += 4) {
                    coords[nc++] = ((float) ((x + (ctrls[i] * width)) + (ctrls[i + 1] / 2.0)));
                    coords[nc++] = ((float) ((y + (ctrls[i + 2] * height)) + (ctrls[i + 3] / 2.0)));
                }
                if (at != null) {
                    at.transform(coords, 0, coords, 0, nc / 2);
                }
                return types[index];
            }

            @java.lang.Override
            public int currentSegment(double[] coords) {
                if (isDone()) {
                    throw new java.util.NoSuchElementException("roundrect iterator out of bounds");
                }
                int nc = 0;
                double[] ctrls = ctrlpts[index];
                for (int i = 0; i < ctrls.length; i += 4) {
                    coords[nc++] = (x + (ctrls[i] * width)) + (ctrls[i + 1] / 2.0);
                    coords[nc++] = (y + (ctrls[i + 2] * height)) + (ctrls[i + 3] / 2.0);
                }
                if (at != null) {
                    at.transform(coords, 0, coords, 0, nc / 2);
                }
                return types[index];
            }
        };
    }
}

