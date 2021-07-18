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
package android.animation;


/**
 * PathKeyframes relies on approximating the Path as a series of line segments.
 * The line segments are recursively divided until there is less than 1/2 pixel error
 * between the lines and the curve. Each point of the line segment is converted
 * to a Keyframe and a linear interpolation between Keyframes creates a good approximation
 * of the curve.
 * <p>
 * PathKeyframes is optimized to reduce the number of objects created when there are
 * many keyframes for a curve.
 * </p>
 * <p>
 * Typically, the returned type is a PointF, but the individual components can be extracted
 * as either an IntKeyframes or FloatKeyframes.
 * </p>
 *
 * @unknown 
 */
public class PathKeyframes implements android.animation.Keyframes {
    private static final int FRACTION_OFFSET = 0;

    private static final int X_OFFSET = 1;

    private static final int Y_OFFSET = 2;

    private static final int NUM_COMPONENTS = 3;

    private static final java.util.ArrayList<android.animation.Keyframe> EMPTY_KEYFRAMES = new java.util.ArrayList<android.animation.Keyframe>();

    private android.graphics.PointF mTempPointF = new android.graphics.PointF();

    private float[] mKeyframeData;

    public PathKeyframes(android.graphics.Path path) {
        this(path, 0.5F);
    }

    public PathKeyframes(android.graphics.Path path, float error) {
        if ((path == null) || path.isEmpty()) {
            throw new java.lang.IllegalArgumentException("The path must not be null or empty");
        }
        mKeyframeData = path.approximate(error);
    }

    @java.lang.Override
    public java.util.ArrayList<android.animation.Keyframe> getKeyframes() {
        return android.animation.PathKeyframes.EMPTY_KEYFRAMES;
    }

    @java.lang.Override
    public java.lang.Object getValue(float fraction) {
        int numPoints = mKeyframeData.length / 3;
        if (fraction < 0) {
            return interpolateInRange(fraction, 0, 1);
        } else
            if (fraction > 1) {
                return interpolateInRange(fraction, numPoints - 2, numPoints - 1);
            } else
                if (fraction == 0) {
                    return pointForIndex(0);
                } else
                    if (fraction == 1) {
                        return pointForIndex(numPoints - 1);
                    } else {
                        // Binary search for the correct section
                        int low = 0;
                        int high = numPoints - 1;
                        while (low <= high) {
                            int mid = (low + high) / 2;
                            float midFraction = mKeyframeData[(mid * android.animation.PathKeyframes.NUM_COMPONENTS) + android.animation.PathKeyframes.FRACTION_OFFSET];
                            if (fraction < midFraction) {
                                high = mid - 1;
                            } else
                                if (fraction > midFraction) {
                                    low = mid + 1;
                                } else {
                                    return pointForIndex(mid);
                                }

                        } 
                        // now high is below the fraction and low is above the fraction
                        return interpolateInRange(fraction, high, low);
                    }



    }

    private android.graphics.PointF interpolateInRange(float fraction, int startIndex, int endIndex) {
        int startBase = startIndex * android.animation.PathKeyframes.NUM_COMPONENTS;
        int endBase = endIndex * android.animation.PathKeyframes.NUM_COMPONENTS;
        float startFraction = mKeyframeData[startBase + android.animation.PathKeyframes.FRACTION_OFFSET];
        float endFraction = mKeyframeData[endBase + android.animation.PathKeyframes.FRACTION_OFFSET];
        float intervalFraction = (fraction - startFraction) / (endFraction - startFraction);
        float startX = mKeyframeData[startBase + android.animation.PathKeyframes.X_OFFSET];
        float endX = mKeyframeData[endBase + android.animation.PathKeyframes.X_OFFSET];
        float startY = mKeyframeData[startBase + android.animation.PathKeyframes.Y_OFFSET];
        float endY = mKeyframeData[endBase + android.animation.PathKeyframes.Y_OFFSET];
        float x = android.animation.PathKeyframes.interpolate(intervalFraction, startX, endX);
        float y = android.animation.PathKeyframes.interpolate(intervalFraction, startY, endY);
        mTempPointF.set(x, y);
        return mTempPointF;
    }

    @java.lang.Override
    public void invalidateCache() {
    }

    @java.lang.Override
    public void setEvaluator(android.animation.TypeEvaluator evaluator) {
    }

    @java.lang.Override
    public java.lang.Class getType() {
        return android.graphics.PointF.class;
    }

    @java.lang.Override
    public android.animation.Keyframes clone() {
        android.animation.Keyframes clone = null;
        try {
            clone = ((android.animation.Keyframes) (super.clone()));
        } catch (java.lang.CloneNotSupportedException e) {
        }
        return clone;
    }

    private android.graphics.PointF pointForIndex(int index) {
        int base = index * android.animation.PathKeyframes.NUM_COMPONENTS;
        int xOffset = base + android.animation.PathKeyframes.X_OFFSET;
        int yOffset = base + android.animation.PathKeyframes.Y_OFFSET;
        mTempPointF.set(mKeyframeData[xOffset], mKeyframeData[yOffset]);
        return mTempPointF;
    }

    private static float interpolate(float fraction, float startValue, float endValue) {
        float diff = endValue - startValue;
        return startValue + (diff * fraction);
    }

    /**
     * Returns a FloatKeyframes for the X component of the Path.
     *
     * @return a FloatKeyframes for the X component of the Path.
     */
    public android.animation.Keyframes.FloatKeyframes createXFloatKeyframes() {
        return new android.animation.PathKeyframes.FloatKeyframesBase() {
            @java.lang.Override
            public float getFloatValue(float fraction) {
                android.graphics.PointF pointF = ((android.graphics.PointF) (android.animation.PathKeyframes.this.getValue(fraction)));
                return pointF.x;
            }
        };
    }

    /**
     * Returns a FloatKeyframes for the Y component of the Path.
     *
     * @return a FloatKeyframes for the Y component of the Path.
     */
    public android.animation.Keyframes.FloatKeyframes createYFloatKeyframes() {
        return new android.animation.PathKeyframes.FloatKeyframesBase() {
            @java.lang.Override
            public float getFloatValue(float fraction) {
                android.graphics.PointF pointF = ((android.graphics.PointF) (android.animation.PathKeyframes.this.getValue(fraction)));
                return pointF.y;
            }
        };
    }

    /**
     * Returns an IntKeyframes for the X component of the Path.
     *
     * @return an IntKeyframes for the X component of the Path.
     */
    public android.animation.Keyframes.IntKeyframes createXIntKeyframes() {
        return new android.animation.PathKeyframes.IntKeyframesBase() {
            @java.lang.Override
            public int getIntValue(float fraction) {
                android.graphics.PointF pointF = ((android.graphics.PointF) (android.animation.PathKeyframes.this.getValue(fraction)));
                return java.lang.Math.round(pointF.x);
            }
        };
    }

    /**
     * Returns an IntKeyframeSet for the Y component of the Path.
     *
     * @return an IntKeyframeSet for the Y component of the Path.
     */
    public android.animation.Keyframes.IntKeyframes createYIntKeyframes() {
        return new android.animation.PathKeyframes.IntKeyframesBase() {
            @java.lang.Override
            public int getIntValue(float fraction) {
                android.graphics.PointF pointF = ((android.graphics.PointF) (android.animation.PathKeyframes.this.getValue(fraction)));
                return java.lang.Math.round(pointF.y);
            }
        };
    }

    private static abstract class SimpleKeyframes implements android.animation.Keyframes {
        @java.lang.Override
        public void setEvaluator(android.animation.TypeEvaluator evaluator) {
        }

        @java.lang.Override
        public void invalidateCache() {
        }

        @java.lang.Override
        public java.util.ArrayList<android.animation.Keyframe> getKeyframes() {
            return android.animation.PathKeyframes.EMPTY_KEYFRAMES;
        }

        @java.lang.Override
        public android.animation.Keyframes clone() {
            android.animation.Keyframes clone = null;
            try {
                clone = ((android.animation.Keyframes) (super.clone()));
            } catch (java.lang.CloneNotSupportedException e) {
            }
            return clone;
        }
    }

    static abstract class IntKeyframesBase extends android.animation.PathKeyframes.SimpleKeyframes implements android.animation.Keyframes.IntKeyframes {
        @java.lang.Override
        public java.lang.Class getType() {
            return java.lang.Integer.class;
        }

        @java.lang.Override
        public java.lang.Object getValue(float fraction) {
            return getIntValue(fraction);
        }
    }

    static abstract class FloatKeyframesBase extends android.animation.PathKeyframes.SimpleKeyframes implements android.animation.Keyframes.FloatKeyframes {
        @java.lang.Override
        public java.lang.Class getType() {
            return java.lang.Float.class;
        }

        @java.lang.Override
        public java.lang.Object getValue(float fraction) {
            return getFloatValue(fraction);
        }
    }
}

