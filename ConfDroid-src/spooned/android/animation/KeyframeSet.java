/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * This class holds a collection of Keyframe objects and is called by ValueAnimator to calculate
 * values between those keyframes for a given animation. The class internal to the animation
 * package because it is an implementation detail of how Keyframes are stored and used.
 *
 * @unknown 
 */
public class KeyframeSet implements android.animation.Keyframes {
    int mNumKeyframes;

    android.animation.Keyframe mFirstKeyframe;

    android.animation.Keyframe mLastKeyframe;

    android.animation.TimeInterpolator mInterpolator;// only used in the 2-keyframe case


    java.util.List<android.animation.Keyframe> mKeyframes;// only used when there are not 2 keyframes


    android.animation.TypeEvaluator mEvaluator;

    public KeyframeSet(android.animation.Keyframe... keyframes) {
        mNumKeyframes = keyframes.length;
        // immutable list
        mKeyframes = java.util.Arrays.asList(keyframes);
        mFirstKeyframe = keyframes[0];
        mLastKeyframe = keyframes[mNumKeyframes - 1];
        mInterpolator = mLastKeyframe.getInterpolator();
    }

    /**
     * If subclass has variables that it calculates based on the Keyframes, it should reset them
     * when this method is called because Keyframe contents might have changed.
     */
    @java.lang.Override
    public void invalidateCache() {
    }

    public java.util.List<android.animation.Keyframe> getKeyframes() {
        return mKeyframes;
    }

    public static android.animation.KeyframeSet ofInt(int... values) {
        int numKeyframes = values.length;
        android.animation.Keyframe.IntKeyframe[] keyframes = new android.animation.Keyframe.IntKeyframe[java.lang.Math.max(numKeyframes, 2)];
        if (numKeyframes == 1) {
            keyframes[0] = ((android.animation.Keyframe.IntKeyframe) (android.animation.Keyframe.ofInt(0.0F)));
            keyframes[1] = ((android.animation.Keyframe.IntKeyframe) (android.animation.Keyframe.ofInt(1.0F, values[0])));
        } else {
            keyframes[0] = ((android.animation.Keyframe.IntKeyframe) (android.animation.Keyframe.ofInt(0.0F, values[0])));
            for (int i = 1; i < numKeyframes; ++i) {
                keyframes[i] = ((android.animation.Keyframe.IntKeyframe) (android.animation.Keyframe.ofInt(((float) (i)) / (numKeyframes - 1), values[i])));
            }
        }
        return new android.animation.IntKeyframeSet(keyframes);
    }

    public static android.animation.KeyframeSet ofFloat(float... values) {
        boolean badValue = false;
        int numKeyframes = values.length;
        android.animation.Keyframe.FloatKeyframe[] keyframes = new android.animation.Keyframe.FloatKeyframe[java.lang.Math.max(numKeyframes, 2)];
        if (numKeyframes == 1) {
            keyframes[0] = ((android.animation.Keyframe.FloatKeyframe) (android.animation.Keyframe.ofFloat(0.0F)));
            keyframes[1] = ((android.animation.Keyframe.FloatKeyframe) (android.animation.Keyframe.ofFloat(1.0F, values[0])));
            if (java.lang.Float.isNaN(values[0])) {
                badValue = true;
            }
        } else {
            keyframes[0] = ((android.animation.Keyframe.FloatKeyframe) (android.animation.Keyframe.ofFloat(0.0F, values[0])));
            for (int i = 1; i < numKeyframes; ++i) {
                keyframes[i] = ((android.animation.Keyframe.FloatKeyframe) (android.animation.Keyframe.ofFloat(((float) (i)) / (numKeyframes - 1), values[i])));
                if (java.lang.Float.isNaN(values[i])) {
                    badValue = true;
                }
            }
        }
        if (badValue) {
            android.util.Log.w("Animator", "Bad value (NaN) in float animator");
        }
        return new android.animation.FloatKeyframeSet(keyframes);
    }

    public static android.animation.KeyframeSet ofKeyframe(android.animation.Keyframe... keyframes) {
        // if all keyframes of same primitive type, create the appropriate KeyframeSet
        int numKeyframes = keyframes.length;
        boolean hasFloat = false;
        boolean hasInt = false;
        boolean hasOther = false;
        for (int i = 0; i < numKeyframes; ++i) {
            if (keyframes[i] instanceof android.animation.Keyframe.FloatKeyframe) {
                hasFloat = true;
            } else
                if (keyframes[i] instanceof android.animation.Keyframe.IntKeyframe) {
                    hasInt = true;
                } else {
                    hasOther = true;
                }

        }
        if ((hasFloat && (!hasInt)) && (!hasOther)) {
            android.animation.Keyframe.FloatKeyframe[] floatKeyframes = new android.animation.Keyframe.FloatKeyframe[numKeyframes];
            for (int i = 0; i < numKeyframes; ++i) {
                floatKeyframes[i] = ((android.animation.Keyframe.FloatKeyframe) (keyframes[i]));
            }
            return new android.animation.FloatKeyframeSet(floatKeyframes);
        } else
            if ((hasInt && (!hasFloat)) && (!hasOther)) {
                android.animation.Keyframe.IntKeyframe[] intKeyframes = new android.animation.Keyframe.IntKeyframe[numKeyframes];
                for (int i = 0; i < numKeyframes; ++i) {
                    intKeyframes[i] = ((android.animation.Keyframe.IntKeyframe) (keyframes[i]));
                }
                return new android.animation.IntKeyframeSet(intKeyframes);
            } else {
                return new android.animation.KeyframeSet(keyframes);
            }

    }

    public static android.animation.KeyframeSet ofObject(java.lang.Object... values) {
        int numKeyframes = values.length;
        android.animation.Keyframe.ObjectKeyframe[] keyframes = new android.animation.Keyframe.ObjectKeyframe[java.lang.Math.max(numKeyframes, 2)];
        if (numKeyframes == 1) {
            keyframes[0] = ((android.animation.Keyframe.ObjectKeyframe) (android.animation.Keyframe.ofObject(0.0F)));
            keyframes[1] = ((android.animation.Keyframe.ObjectKeyframe) (android.animation.Keyframe.ofObject(1.0F, values[0])));
        } else {
            keyframes[0] = ((android.animation.Keyframe.ObjectKeyframe) (android.animation.Keyframe.ofObject(0.0F, values[0])));
            for (int i = 1; i < numKeyframes; ++i) {
                keyframes[i] = ((android.animation.Keyframe.ObjectKeyframe) (android.animation.Keyframe.ofObject(((float) (i)) / (numKeyframes - 1), values[i])));
            }
        }
        return new android.animation.KeyframeSet(keyframes);
    }

    public static android.animation.PathKeyframes ofPath(android.graphics.Path path) {
        return new android.animation.PathKeyframes(path);
    }

    public static android.animation.PathKeyframes ofPath(android.graphics.Path path, float error) {
        return new android.animation.PathKeyframes(path, error);
    }

    /**
     * Sets the TypeEvaluator to be used when calculating animated values. This object
     * is required only for KeyframeSets that are not either IntKeyframeSet or FloatKeyframeSet,
     * both of which assume their own evaluator to speed up calculations with those primitive
     * types.
     *
     * @param evaluator
     * 		The TypeEvaluator to be used to calculate animated values.
     */
    public void setEvaluator(android.animation.TypeEvaluator evaluator) {
        mEvaluator = evaluator;
    }

    @java.lang.Override
    public java.lang.Class getType() {
        return mFirstKeyframe.getType();
    }

    @java.lang.Override
    public android.animation.KeyframeSet clone() {
        java.util.List<android.animation.Keyframe> keyframes = mKeyframes;
        int numKeyframes = mKeyframes.size();
        final android.animation.Keyframe[] newKeyframes = new android.animation.Keyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; ++i) {
            newKeyframes[i] = keyframes.get(i).clone();
        }
        android.animation.KeyframeSet newSet = new android.animation.KeyframeSet(newKeyframes);
        return newSet;
    }

    /**
     * Gets the animated value, given the elapsed fraction of the animation (interpolated by the
     * animation's interpolator) and the evaluator used to calculate in-between values. This
     * function maps the input fraction to the appropriate keyframe interval and a fraction
     * between them and returns the interpolated value. Note that the input fraction may fall
     * outside the [0-1] bounds, if the animation's interpolator made that happen (e.g., a
     * spring interpolation that might send the fraction past 1.0). We handle this situation by
     * just using the two keyframes at the appropriate end when the value is outside those bounds.
     *
     * @param fraction
     * 		The elapsed fraction of the animation
     * @return The animated value.
     */
    public java.lang.Object getValue(float fraction) {
        // Special-case optimization for the common case of only two keyframes
        if (mNumKeyframes == 2) {
            if (mInterpolator != null) {
                fraction = mInterpolator.getInterpolation(fraction);
            }
            return mEvaluator.evaluate(fraction, mFirstKeyframe.getValue(), mLastKeyframe.getValue());
        }
        if (fraction <= 0.0F) {
            final android.animation.Keyframe nextKeyframe = mKeyframes.get(1);
            final android.animation.TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            final float prevFraction = mFirstKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return mEvaluator.evaluate(intervalFraction, mFirstKeyframe.getValue(), nextKeyframe.getValue());
        } else
            if (fraction >= 1.0F) {
                final android.animation.Keyframe prevKeyframe = mKeyframes.get(mNumKeyframes - 2);
                final android.animation.TimeInterpolator interpolator = mLastKeyframe.getInterpolator();
                if (interpolator != null) {
                    fraction = interpolator.getInterpolation(fraction);
                }
                final float prevFraction = prevKeyframe.getFraction();
                float intervalFraction = (fraction - prevFraction) / (mLastKeyframe.getFraction() - prevFraction);
                return mEvaluator.evaluate(intervalFraction, prevKeyframe.getValue(), mLastKeyframe.getValue());
            }

        android.animation.Keyframe prevKeyframe = mFirstKeyframe;
        for (int i = 1; i < mNumKeyframes; ++i) {
            android.animation.Keyframe nextKeyframe = mKeyframes.get(i);
            if (fraction < nextKeyframe.getFraction()) {
                final android.animation.TimeInterpolator interpolator = nextKeyframe.getInterpolator();
                final float prevFraction = prevKeyframe.getFraction();
                float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
                // Apply interpolator on the proportional duration.
                if (interpolator != null) {
                    intervalFraction = interpolator.getInterpolation(intervalFraction);
                }
                return mEvaluator.evaluate(intervalFraction, prevKeyframe.getValue(), nextKeyframe.getValue());
            }
            prevKeyframe = nextKeyframe;
        }
        // shouldn't reach here
        return mLastKeyframe.getValue();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String returnVal = " ";
        for (int i = 0; i < mNumKeyframes; ++i) {
            returnVal += mKeyframes.get(i).getValue() + "  ";
        }
        return returnVal;
    }
}

