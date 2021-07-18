/**
 * Copyright 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.support.v7.graphics;


/**
 * A class which allows custom selection of colors in a {@link Palette}'s generation. Instances
 * can be created via the {@link Builder} class.
 *
 * <p>To use the target, use the {@link Palette.Builder#addTarget(Target)} API when building a
 * Palette.</p>
 */
public final class Target {
    private static final float TARGET_DARK_LUMA = 0.26F;

    private static final float MAX_DARK_LUMA = 0.45F;

    private static final float MIN_LIGHT_LUMA = 0.55F;

    private static final float TARGET_LIGHT_LUMA = 0.74F;

    private static final float MIN_NORMAL_LUMA = 0.3F;

    private static final float TARGET_NORMAL_LUMA = 0.5F;

    private static final float MAX_NORMAL_LUMA = 0.7F;

    private static final float TARGET_MUTED_SATURATION = 0.3F;

    private static final float MAX_MUTED_SATURATION = 0.4F;

    private static final float TARGET_VIBRANT_SATURATION = 1.0F;

    private static final float MIN_VIBRANT_SATURATION = 0.35F;

    private static final float WEIGHT_SATURATION = 0.24F;

    private static final float WEIGHT_LUMA = 0.52F;

    private static final float WEIGHT_POPULATION = 0.24F;

    static final int INDEX_MIN = 0;

    static final int INDEX_TARGET = 1;

    static final int INDEX_MAX = 2;

    static final int INDEX_WEIGHT_SAT = 0;

    static final int INDEX_WEIGHT_LUMA = 1;

    static final int INDEX_WEIGHT_POP = 2;

    /**
     * A target which has the characteristics of a vibrant color which is light in luminance.
     */
    public static final android.support.v7.graphics.Target LIGHT_VIBRANT;

    /**
     * A target which has the characteristics of a vibrant color which is neither light or dark.
     */
    public static final android.support.v7.graphics.Target VIBRANT;

    /**
     * A target which has the characteristics of a vibrant color which is dark in luminance.
     */
    public static final android.support.v7.graphics.Target DARK_VIBRANT;

    /**
     * A target which has the characteristics of a muted color which is light in luminance.
     */
    public static final android.support.v7.graphics.Target LIGHT_MUTED;

    /**
     * A target which has the characteristics of a muted color which is neither light or dark.
     */
    public static final android.support.v7.graphics.Target MUTED;

    /**
     * A target which has the characteristics of a muted color which is dark in luminance.
     */
    public static final android.support.v7.graphics.Target DARK_MUTED;

    static {
        LIGHT_VIBRANT = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultLightLightnessValues(android.support.v7.graphics.Target.LIGHT_VIBRANT);
        android.support.v7.graphics.Target.setDefaultVibrantSaturationValues(android.support.v7.graphics.Target.LIGHT_VIBRANT);
        VIBRANT = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultNormalLightnessValues(android.support.v7.graphics.Target.VIBRANT);
        android.support.v7.graphics.Target.setDefaultVibrantSaturationValues(android.support.v7.graphics.Target.VIBRANT);
        DARK_VIBRANT = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultDarkLightnessValues(android.support.v7.graphics.Target.DARK_VIBRANT);
        android.support.v7.graphics.Target.setDefaultVibrantSaturationValues(android.support.v7.graphics.Target.DARK_VIBRANT);
        LIGHT_MUTED = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultLightLightnessValues(android.support.v7.graphics.Target.LIGHT_MUTED);
        android.support.v7.graphics.Target.setDefaultMutedSaturationValues(android.support.v7.graphics.Target.LIGHT_MUTED);
        MUTED = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultNormalLightnessValues(android.support.v7.graphics.Target.MUTED);
        android.support.v7.graphics.Target.setDefaultMutedSaturationValues(android.support.v7.graphics.Target.MUTED);
        DARK_MUTED = new android.support.v7.graphics.Target();
        android.support.v7.graphics.Target.setDefaultDarkLightnessValues(android.support.v7.graphics.Target.DARK_MUTED);
        android.support.v7.graphics.Target.setDefaultMutedSaturationValues(android.support.v7.graphics.Target.DARK_MUTED);
    }

    final float[] mSaturationTargets = new float[3];

    final float[] mLightnessTargets = new float[3];

    final float[] mWeights = new float[3];

    boolean mIsExclusive = true;// default to true


    Target() {
        android.support.v7.graphics.Target.setTargetDefaultValues(mSaturationTargets);
        android.support.v7.graphics.Target.setTargetDefaultValues(mLightnessTargets);
        setDefaultWeights();
    }

    Target(android.support.v7.graphics.Target from) {
        java.lang.System.arraycopy(from.mSaturationTargets, 0, mSaturationTargets, 0, mSaturationTargets.length);
        java.lang.System.arraycopy(from.mLightnessTargets, 0, mLightnessTargets, 0, mLightnessTargets.length);
        java.lang.System.arraycopy(from.mWeights, 0, mWeights, 0, mWeights.length);
    }

    /**
     * The minimum saturation value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getMinimumSaturation() {
        return mSaturationTargets[android.support.v7.graphics.Target.INDEX_MIN];
    }

    /**
     * The target saturation value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getTargetSaturation() {
        return mSaturationTargets[android.support.v7.graphics.Target.INDEX_TARGET];
    }

    /**
     * The maximum saturation value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getMaximumSaturation() {
        return mSaturationTargets[android.support.v7.graphics.Target.INDEX_MAX];
    }

    /**
     * The minimum lightness value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getMinimumLightness() {
        return mLightnessTargets[android.support.v7.graphics.Target.INDEX_MIN];
    }

    /**
     * The target lightness value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getTargetLightness() {
        return mLightnessTargets[android.support.v7.graphics.Target.INDEX_TARGET];
    }

    /**
     * The maximum lightness value for this target.
     */
    @android.support.annotation.FloatRange(from = 0, to = 1)
    public float getMaximumLightness() {
        return mLightnessTargets[android.support.v7.graphics.Target.INDEX_MAX];
    }

    /**
     * Returns the weight of importance that this target places on a color's saturation within
     * the image.
     *
     * <p>The larger the weight, relative to the other weights, the more important that a color
     * being close to the target value has on selection.</p>
     *
     * @see #getTargetSaturation()
     */
    public float getSaturationWeight() {
        return mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_SAT];
    }

    /**
     * Returns the weight of importance that this target places on a color's lightness within
     * the image.
     *
     * <p>The larger the weight, relative to the other weights, the more important that a color
     * being close to the target value has on selection.</p>
     *
     * @see #getTargetLightness()
     */
    public float getLightnessWeight() {
        return mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_LUMA];
    }

    /**
     * Returns the weight of importance that this target places on a color's population within
     * the image.
     *
     * <p>The larger the weight, relative to the other weights, the more important that a
     * color's population being close to the most populous has on selection.</p>
     */
    public float getPopulationWeight() {
        return mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_POP];
    }

    /**
     * Returns whether any color selected for this target is exclusive for this target only.
     *
     * <p>If false, then the color can be selected for other targets.</p>
     */
    public boolean isExclusive() {
        return mIsExclusive;
    }

    private static void setTargetDefaultValues(final float[] values) {
        values[android.support.v7.graphics.Target.INDEX_MIN] = 0.0F;
        values[android.support.v7.graphics.Target.INDEX_TARGET] = 0.5F;
        values[android.support.v7.graphics.Target.INDEX_MAX] = 1.0F;
    }

    private void setDefaultWeights() {
        mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_SAT] = android.support.v7.graphics.Target.WEIGHT_SATURATION;
        mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_LUMA] = android.support.v7.graphics.Target.WEIGHT_LUMA;
        mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_POP] = android.support.v7.graphics.Target.WEIGHT_POPULATION;
    }

    void normalizeWeights() {
        float sum = 0;
        for (int i = 0, z = mWeights.length; i < z; i++) {
            float weight = mWeights[i];
            if (weight > 0) {
                sum += weight;
            }
        }
        if (sum != 0) {
            for (int i = 0, z = mWeights.length; i < z; i++) {
                if (mWeights[i] > 0) {
                    mWeights[i] /= sum;
                }
            }
        }
    }

    private static void setDefaultDarkLightnessValues(android.support.v7.graphics.Target target) {
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_TARGET] = android.support.v7.graphics.Target.TARGET_DARK_LUMA;
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MAX] = android.support.v7.graphics.Target.MAX_DARK_LUMA;
    }

    private static void setDefaultNormalLightnessValues(android.support.v7.graphics.Target target) {
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MIN] = android.support.v7.graphics.Target.MIN_NORMAL_LUMA;
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_TARGET] = android.support.v7.graphics.Target.TARGET_NORMAL_LUMA;
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MAX] = android.support.v7.graphics.Target.MAX_NORMAL_LUMA;
    }

    private static void setDefaultLightLightnessValues(android.support.v7.graphics.Target target) {
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MIN] = android.support.v7.graphics.Target.MIN_LIGHT_LUMA;
        target.mLightnessTargets[android.support.v7.graphics.Target.INDEX_TARGET] = android.support.v7.graphics.Target.TARGET_LIGHT_LUMA;
    }

    private static void setDefaultVibrantSaturationValues(android.support.v7.graphics.Target target) {
        target.mSaturationTargets[android.support.v7.graphics.Target.INDEX_MIN] = android.support.v7.graphics.Target.MIN_VIBRANT_SATURATION;
        target.mSaturationTargets[android.support.v7.graphics.Target.INDEX_TARGET] = android.support.v7.graphics.Target.TARGET_VIBRANT_SATURATION;
    }

    private static void setDefaultMutedSaturationValues(android.support.v7.graphics.Target target) {
        target.mSaturationTargets[android.support.v7.graphics.Target.INDEX_TARGET] = android.support.v7.graphics.Target.TARGET_MUTED_SATURATION;
        target.mSaturationTargets[android.support.v7.graphics.Target.INDEX_MAX] = android.support.v7.graphics.Target.MAX_MUTED_SATURATION;
    }

    /**
     * Builder class for generating custom {@link Target} instances.
     */
    public static final class Builder {
        private final android.support.v7.graphics.Target mTarget;

        /**
         * Create a new {@link Target} builder from scratch.
         */
        public Builder() {
            mTarget = new android.support.v7.graphics.Target();
        }

        /**
         * Create a new builder based on an existing {@link Target}.
         */
        public Builder(android.support.v7.graphics.Target target) {
            mTarget = new android.support.v7.graphics.Target(target);
        }

        /**
         * Set the minimum saturation value for this target.
         */
        public android.support.v7.graphics.Target.Builder setMinimumSaturation(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mSaturationTargets[android.support.v7.graphics.Target.INDEX_MIN] = value;
            return this;
        }

        /**
         * Set the target/ideal saturation value for this target.
         */
        public android.support.v7.graphics.Target.Builder setTargetSaturation(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mSaturationTargets[android.support.v7.graphics.Target.INDEX_TARGET] = value;
            return this;
        }

        /**
         * Set the maximum saturation value for this target.
         */
        public android.support.v7.graphics.Target.Builder setMaximumSaturation(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mSaturationTargets[android.support.v7.graphics.Target.INDEX_MAX] = value;
            return this;
        }

        /**
         * Set the minimum lightness value for this target.
         */
        public android.support.v7.graphics.Target.Builder setMinimumLightness(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MIN] = value;
            return this;
        }

        /**
         * Set the target/ideal lightness value for this target.
         */
        public android.support.v7.graphics.Target.Builder setTargetLightness(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mLightnessTargets[android.support.v7.graphics.Target.INDEX_TARGET] = value;
            return this;
        }

        /**
         * Set the maximum lightness value for this target.
         */
        public android.support.v7.graphics.Target.Builder setMaximumLightness(@android.support.annotation.FloatRange(from = 0, to = 1)
        float value) {
            mTarget.mLightnessTargets[android.support.v7.graphics.Target.INDEX_MAX] = value;
            return this;
        }

        /**
         * Set the weight of importance that this target will place on saturation values.
         *
         * <p>The larger the weight, relative to the other weights, the more important that a color
         * being close to the target value has on selection.</p>
         *
         * <p>A weight of 0 means that it has no weight, and thus has no
         * bearing on the selection.</p>
         *
         * @see #setTargetSaturation(float)
         */
        public android.support.v7.graphics.Target.Builder setSaturationWeight(@android.support.annotation.FloatRange(from = 0)
        float weight) {
            mTarget.mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_SAT] = weight;
            return this;
        }

        /**
         * Set the weight of importance that this target will place on lightness values.
         *
         * <p>The larger the weight, relative to the other weights, the more important that a color
         * being close to the target value has on selection.</p>
         *
         * <p>A weight of 0 means that it has no weight, and thus has no
         * bearing on the selection.</p>
         *
         * @see #setTargetLightness(float)
         */
        public android.support.v7.graphics.Target.Builder setLightnessWeight(@android.support.annotation.FloatRange(from = 0)
        float weight) {
            mTarget.mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_LUMA] = weight;
            return this;
        }

        /**
         * Set the weight of importance that this target will place on a color's population within
         * the image.
         *
         * <p>The larger the weight, relative to the other weights, the more important that a
         * color's population being close to the most populous has on selection.</p>
         *
         * <p>A weight of 0 means that it has no weight, and thus has no
         * bearing on the selection.</p>
         */
        public android.support.v7.graphics.Target.Builder setPopulationWeight(@android.support.annotation.FloatRange(from = 0)
        float weight) {
            mTarget.mWeights[android.support.v7.graphics.Target.INDEX_WEIGHT_POP] = weight;
            return this;
        }

        /**
         * Set whether any color selected for this target is exclusive to this target only.
         * Defaults to true.
         *
         * @param exclusive
         * 		true if any the color is exclusive to this target, or false is the
         * 		color can be selected for other targets.
         */
        public android.support.v7.graphics.Target.Builder setExclusive(boolean exclusive) {
            mTarget.mIsExclusive = exclusive;
            return this;
        }

        /**
         * Builds and returns the resulting {@link Target}.
         */
        public android.support.v7.graphics.Target build() {
            return mTarget;
        }
    }
}

