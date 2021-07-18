/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.support.v4.view.animation;


/**
 * A path interpolator implementation compatible with API 4+.
 */
class PathInterpolatorDonut implements android.view.animation.Interpolator {
    /**
     * Governs the accuracy of the approximation of the {@link Path}.
     */
    private static final float PRECISION = 0.002F;

    private final float[] mX;

    private final float[] mY;

    public PathInterpolatorDonut(android.graphics.Path path) {
        final android.graphics.PathMeasure pathMeasure = /* forceClosed */
        new android.graphics.PathMeasure(path, false);
        final float pathLength = pathMeasure.getLength();
        final int numPoints = ((int) (pathLength / android.support.v4.view.animation.PathInterpolatorDonut.PRECISION)) + 1;
        mX = new float[numPoints];
        mY = new float[numPoints];
        final float[] position = new float[2];
        for (int i = 0; i < numPoints; ++i) {
            final float distance = (i * pathLength) / (numPoints - 1);
            /* tangent */
            pathMeasure.getPosTan(distance, position, null);
            mX[i] = position[0];
            mY[i] = position[1];
        }
    }

    public PathInterpolatorDonut(float controlX, float controlY) {
        this(android.support.v4.view.animation.PathInterpolatorDonut.createQuad(controlX, controlY));
    }

    public PathInterpolatorDonut(float controlX1, float controlY1, float controlX2, float controlY2) {
        this(android.support.v4.view.animation.PathInterpolatorDonut.createCubic(controlX1, controlY1, controlX2, controlY2));
    }

    @java.lang.Override
    public float getInterpolation(float t) {
        if (t <= 0.0F) {
            return 0.0F;
        } else
            if (t >= 1.0F) {
                return 1.0F;
            }

        // Do a binary search for the correct x to interpolate between.
        int startIndex = 0;
        int endIndex = mX.length - 1;
        while ((endIndex - startIndex) > 1) {
            int midIndex = (startIndex + endIndex) / 2;
            if (t < mX[midIndex]) {
                endIndex = midIndex;
            } else {
                startIndex = midIndex;
            }
        } 
        final float xRange = mX[endIndex] - mX[startIndex];
        if (xRange == 0) {
            return mY[startIndex];
        }
        final float tInRange = t - mX[startIndex];
        final float fraction = tInRange / xRange;
        final float startY = mY[startIndex];
        final float endY = mY[endIndex];
        return startY + (fraction * (endY - startY));
    }

    private static android.graphics.Path createQuad(float controlX, float controlY) {
        final android.graphics.Path path = new android.graphics.Path();
        path.moveTo(0.0F, 0.0F);
        path.quadTo(controlX, controlY, 1.0F, 1.0F);
        return path;
    }

    private static android.graphics.Path createCubic(float controlX1, float controlY1, float controlX2, float controlY2) {
        final android.graphics.Path path = new android.graphics.Path();
        path.moveTo(0.0F, 0.0F);
        path.cubicTo(controlX1, controlY1, controlX2, controlY2, 1.0F, 1.0F);
        return path;
    }
}

