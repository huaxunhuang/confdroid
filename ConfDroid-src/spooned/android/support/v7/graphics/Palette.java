/**
 * Copyright 2014 The Android Open Source Project
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
 * A helper class to extract prominent colors from an image.
 * <p>
 * A number of colors with different profiles are extracted from the image:
 * <ul>
 *     <li>Vibrant</li>
 *     <li>Vibrant Dark</li>
 *     <li>Vibrant Light</li>
 *     <li>Muted</li>
 *     <li>Muted Dark</li>
 *     <li>Muted Light</li>
 * </ul>
 * These can be retrieved from the appropriate getter method.
 *
 * <p>
 * Instances are created with a {@link Builder} which supports several options to tweak the
 * generated Palette. See that class' documentation for more information.
 * <p>
 * Generation should always be completed on a background thread, ideally the one in
 * which you load your image on. {@link Builder} supports both synchronous and asynchronous
 * generation:
 *
 * <pre>
 * // Synchronous
 * Palette p = Palette.from(bitmap).generate();
 *
 * // Asynchronous
 * Palette.from(bitmap).generate(new PaletteAsyncListener() {
 *     public void onGenerated(Palette p) {
 *         // Use generated instance
 *     }
 * });
 * </pre>
 */
public final class Palette {
    /**
     * Listener to be used with {@link #generateAsync(Bitmap, PaletteAsyncListener)} or
     * {@link #generateAsync(Bitmap, int, PaletteAsyncListener)}
     */
    public interface PaletteAsyncListener {
        /**
         * Called when the {@link Palette} has been generated.
         */
        void onGenerated(android.support.v7.graphics.Palette palette);
    }

    static final int DEFAULT_RESIZE_BITMAP_AREA = 160 * 160;

    static final int DEFAULT_CALCULATE_NUMBER_COLORS = 16;

    static final float MIN_CONTRAST_TITLE_TEXT = 3.0F;

    static final float MIN_CONTRAST_BODY_TEXT = 4.5F;

    static final java.lang.String LOG_TAG = "Palette";

    static final boolean LOG_TIMINGS = false;

    /**
     * Start generating a {@link Palette} with the returned {@link Builder} instance.
     */
    public static android.support.v7.graphics.Palette.Builder from(android.graphics.Bitmap bitmap) {
        return new android.support.v7.graphics.Palette.Builder(bitmap);
    }

    /**
     * Generate a {@link Palette} from the pre-generated list of {@link Palette.Swatch} swatches.
     * This is useful for testing, or if you want to resurrect a {@link Palette} instance from a
     * list of swatches. Will return null if the {@code swatches} is null.
     */
    public static android.support.v7.graphics.Palette from(java.util.List<android.support.v7.graphics.Palette.Swatch> swatches) {
        return new android.support.v7.graphics.Palette.Builder(swatches).generate();
    }

    /**
     *
     *
     * @deprecated Use {@link Builder} to generate the Palette.
     */
    @java.lang.Deprecated
    public static android.support.v7.graphics.Palette generate(android.graphics.Bitmap bitmap) {
        return android.support.v7.graphics.Palette.from(bitmap).generate();
    }

    /**
     *
     *
     * @deprecated Use {@link Builder} to generate the Palette.
     */
    @java.lang.Deprecated
    public static android.support.v7.graphics.Palette generate(android.graphics.Bitmap bitmap, int numColors) {
        return android.support.v7.graphics.Palette.from(bitmap).maximumColorCount(numColors).generate();
    }

    /**
     *
     *
     * @deprecated Use {@link Builder} to generate the Palette.
     */
    @java.lang.Deprecated
    public static android.os.AsyncTask<android.graphics.Bitmap, java.lang.Void, android.support.v7.graphics.Palette> generateAsync(android.graphics.Bitmap bitmap, android.support.v7.graphics.Palette.PaletteAsyncListener listener) {
        return android.support.v7.graphics.Palette.from(bitmap).generate(listener);
    }

    /**
     *
     *
     * @deprecated Use {@link Builder} to generate the Palette.
     */
    @java.lang.Deprecated
    public static android.os.AsyncTask<android.graphics.Bitmap, java.lang.Void, android.support.v7.graphics.Palette> generateAsync(final android.graphics.Bitmap bitmap, final int numColors, final android.support.v7.graphics.Palette.PaletteAsyncListener listener) {
        return android.support.v7.graphics.Palette.from(bitmap).maximumColorCount(numColors).generate(listener);
    }

    private final java.util.List<android.support.v7.graphics.Palette.Swatch> mSwatches;

    private final java.util.List<android.support.v7.graphics.Target> mTargets;

    private final java.util.Map<android.support.v7.graphics.Target, android.support.v7.graphics.Palette.Swatch> mSelectedSwatches;

    private final android.util.SparseBooleanArray mUsedColors;

    private final android.support.v7.graphics.Palette.Swatch mDominantSwatch;

    Palette(java.util.List<android.support.v7.graphics.Palette.Swatch> swatches, java.util.List<android.support.v7.graphics.Target> targets) {
        mSwatches = swatches;
        mTargets = targets;
        mUsedColors = new android.util.SparseBooleanArray();
        mSelectedSwatches = new android.support.v4.util.ArrayMap<>();
        mDominantSwatch = findDominantSwatch();
    }

    /**
     * Returns all of the swatches which make up the palette.
     */
    @android.support.annotation.NonNull
    public java.util.List<android.support.v7.graphics.Palette.Swatch> getSwatches() {
        return java.util.Collections.unmodifiableList(mSwatches);
    }

    /**
     * Returns the targets used to generate this palette.
     */
    @android.support.annotation.NonNull
    public java.util.List<android.support.v7.graphics.Target> getTargets() {
        return java.util.Collections.unmodifiableList(mTargets);
    }

    /**
     * Returns the most vibrant swatch in the palette. Might be null.
     *
     * @see Target#VIBRANT
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getVibrantSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.VIBRANT);
    }

    /**
     * Returns a light and vibrant swatch from the palette. Might be null.
     *
     * @see Target#LIGHT_VIBRANT
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getLightVibrantSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.LIGHT_VIBRANT);
    }

    /**
     * Returns a dark and vibrant swatch from the palette. Might be null.
     *
     * @see Target#DARK_VIBRANT
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getDarkVibrantSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.DARK_VIBRANT);
    }

    /**
     * Returns a muted swatch from the palette. Might be null.
     *
     * @see Target#MUTED
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getMutedSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.MUTED);
    }

    /**
     * Returns a muted and light swatch from the palette. Might be null.
     *
     * @see Target#LIGHT_MUTED
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getLightMutedSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.LIGHT_MUTED);
    }

    /**
     * Returns a muted and dark swatch from the palette. Might be null.
     *
     * @see Target#DARK_MUTED
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getDarkMutedSwatch() {
        return getSwatchForTarget(android.support.v7.graphics.Target.DARK_MUTED);
    }

    /**
     * Returns the most vibrant color in the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getVibrantSwatch()
     */
    @android.support.annotation.ColorInt
    public int getVibrantColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.VIBRANT, defaultColor);
    }

    /**
     * Returns a light and vibrant color from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getLightVibrantSwatch()
     */
    @android.support.annotation.ColorInt
    public int getLightVibrantColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.LIGHT_VIBRANT, defaultColor);
    }

    /**
     * Returns a dark and vibrant color from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getDarkVibrantSwatch()
     */
    @android.support.annotation.ColorInt
    public int getDarkVibrantColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.DARK_VIBRANT, defaultColor);
    }

    /**
     * Returns a muted color from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getMutedSwatch()
     */
    @android.support.annotation.ColorInt
    public int getMutedColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.MUTED, defaultColor);
    }

    /**
     * Returns a muted and light color from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getLightMutedSwatch()
     */
    @android.support.annotation.ColorInt
    public int getLightMutedColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.LIGHT_MUTED, defaultColor);
    }

    /**
     * Returns a muted and dark color from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getDarkMutedSwatch()
     */
    @android.support.annotation.ColorInt
    public int getDarkMutedColor(@android.support.annotation.ColorInt
    final int defaultColor) {
        return getColorForTarget(android.support.v7.graphics.Target.DARK_MUTED, defaultColor);
    }

    /**
     * Returns the selected swatch for the given target from the palette, or {@code null} if one
     * could not be found.
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getSwatchForTarget(@android.support.annotation.NonNull
    final android.support.v7.graphics.Target target) {
        return mSelectedSwatches.get(target);
    }

    /**
     * Returns the selected color for the given target from the palette as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     */
    @android.support.annotation.ColorInt
    public int getColorForTarget(@android.support.annotation.NonNull
    final android.support.v7.graphics.Target target, @android.support.annotation.ColorInt
    final int defaultColor) {
        android.support.v7.graphics.Palette.Swatch swatch = getSwatchForTarget(target);
        return swatch != null ? swatch.getRgb() : defaultColor;
    }

    /**
     * Returns the dominant swatch from the palette.
     *
     * <p>The dominant swatch is defined as the swatch with the greatest population (frequency)
     * within the palette.</p>
     */
    @android.support.annotation.Nullable
    public android.support.v7.graphics.Palette.Swatch getDominantSwatch() {
        return mDominantSwatch;
    }

    /**
     * Returns the color of the dominant swatch from the palette, as an RGB packed int.
     *
     * @param defaultColor
     * 		value to return if the swatch isn't available
     * @see #getDominantSwatch()
     */
    @android.support.annotation.ColorInt
    public int getDominantColor(@android.support.annotation.ColorInt
    int defaultColor) {
        return mDominantSwatch != null ? mDominantSwatch.getRgb() : defaultColor;
    }

    void generate() {
        // We need to make sure that the scored targets are generated first. This is so that
        // inherited targets have something to inherit from
        for (int i = 0, count = mTargets.size(); i < count; i++) {
            final android.support.v7.graphics.Target target = mTargets.get(i);
            target.normalizeWeights();
            mSelectedSwatches.put(target, generateScoredTarget(target));
        }
        // We now clear out the used colors
        mUsedColors.clear();
    }

    private android.support.v7.graphics.Palette.Swatch generateScoredTarget(final android.support.v7.graphics.Target target) {
        final android.support.v7.graphics.Palette.Swatch maxScoreSwatch = getMaxScoredSwatchForTarget(target);
        if ((maxScoreSwatch != null) && target.isExclusive()) {
            // If we have a swatch, and the target is exclusive, add the color to the used list
            mUsedColors.append(maxScoreSwatch.getRgb(), true);
        }
        return maxScoreSwatch;
    }

    private android.support.v7.graphics.Palette.Swatch getMaxScoredSwatchForTarget(final android.support.v7.graphics.Target target) {
        float maxScore = 0;
        android.support.v7.graphics.Palette.Swatch maxScoreSwatch = null;
        for (int i = 0, count = mSwatches.size(); i < count; i++) {
            final android.support.v7.graphics.Palette.Swatch swatch = mSwatches.get(i);
            if (shouldBeScoredForTarget(swatch, target)) {
                final float score = generateScore(swatch, target);
                if ((maxScoreSwatch == null) || (score > maxScore)) {
                    maxScoreSwatch = swatch;
                    maxScore = score;
                }
            }
        }
        return maxScoreSwatch;
    }

    private boolean shouldBeScoredForTarget(final android.support.v7.graphics.Palette.Swatch swatch, final android.support.v7.graphics.Target target) {
        // Check whether the HSL values are within the correct ranges, and this color hasn't
        // been used yet.
        final float[] hsl = swatch.getHsl();
        return ((((hsl[1] >= target.getMinimumSaturation()) && (hsl[1] <= target.getMaximumSaturation())) && (hsl[2] >= target.getMinimumLightness())) && (hsl[2] <= target.getMaximumLightness())) && (!mUsedColors.get(swatch.getRgb()));
    }

    private float generateScore(android.support.v7.graphics.Palette.Swatch swatch, android.support.v7.graphics.Target target) {
        final float[] hsl = swatch.getHsl();
        float saturationScore = 0;
        float luminanceScore = 0;
        float populationScore = 0;
        final int maxPopulation = (mDominantSwatch != null) ? mDominantSwatch.getPopulation() : 1;
        if (target.getSaturationWeight() > 0) {
            saturationScore = target.getSaturationWeight() * (1.0F - java.lang.Math.abs(hsl[1] - target.getTargetSaturation()));
        }
        if (target.getLightnessWeight() > 0) {
            luminanceScore = target.getLightnessWeight() * (1.0F - java.lang.Math.abs(hsl[2] - target.getTargetLightness()));
        }
        if (target.getPopulationWeight() > 0) {
            populationScore = target.getPopulationWeight() * (swatch.getPopulation() / ((float) (maxPopulation)));
        }
        return (saturationScore + luminanceScore) + populationScore;
    }

    private android.support.v7.graphics.Palette.Swatch findDominantSwatch() {
        int maxPop = java.lang.Integer.MIN_VALUE;
        android.support.v7.graphics.Palette.Swatch maxSwatch = null;
        for (int i = 0, count = mSwatches.size(); i < count; i++) {
            android.support.v7.graphics.Palette.Swatch swatch = mSwatches.get(i);
            if (swatch.getPopulation() > maxPop) {
                maxSwatch = swatch;
                maxPop = swatch.getPopulation();
            }
        }
        return maxSwatch;
    }

    private static float[] copyHslValues(android.support.v7.graphics.Palette.Swatch color) {
        final float[] newHsl = new float[3];
        java.lang.System.arraycopy(color.getHsl(), 0, newHsl, 0, 3);
        return newHsl;
    }

    /**
     * Represents a color swatch generated from an image's palette. The RGB color can be retrieved
     * by calling {@link #getRgb()}.
     */
    public static final class Swatch {
        private final int mRed;

        private final int mGreen;

        private final int mBlue;

        private final int mRgb;

        private final int mPopulation;

        private boolean mGeneratedTextColors;

        private int mTitleTextColor;

        private int mBodyTextColor;

        private float[] mHsl;

        public Swatch(@android.support.annotation.ColorInt
        int color, int population) {
            mRed = android.graphics.Color.red(color);
            mGreen = android.graphics.Color.green(color);
            mBlue = android.graphics.Color.blue(color);
            mRgb = color;
            mPopulation = population;
        }

        Swatch(int red, int green, int blue, int population) {
            mRed = red;
            mGreen = green;
            mBlue = blue;
            mRgb = android.graphics.Color.rgb(red, green, blue);
            mPopulation = population;
        }

        Swatch(float[] hsl, int population) {
            this(android.support.v4.graphics.ColorUtils.HSLToColor(hsl), population);
            mHsl = hsl;
        }

        /**
         *
         *
         * @return this swatch's RGB color value
         */
        @android.support.annotation.ColorInt
        public int getRgb() {
            return mRgb;
        }

        /**
         * Return this swatch's HSL values.
         *     hsv[0] is Hue [0 .. 360)
         *     hsv[1] is Saturation [0...1]
         *     hsv[2] is Lightness [0...1]
         */
        public float[] getHsl() {
            if (mHsl == null) {
                mHsl = new float[3];
            }
            android.support.v4.graphics.ColorUtils.RGBToHSL(mRed, mGreen, mBlue, mHsl);
            return mHsl;
        }

        /**
         *
         *
         * @return the number of pixels represented by this swatch
         */
        public int getPopulation() {
            return mPopulation;
        }

        /**
         * Returns an appropriate color to use for any 'title' text which is displayed over this
         * {@link Swatch}'s color. This color is guaranteed to have sufficient contrast.
         */
        @android.support.annotation.ColorInt
        public int getTitleTextColor() {
            ensureTextColorsGenerated();
            return mTitleTextColor;
        }

        /**
         * Returns an appropriate color to use for any 'body' text which is displayed over this
         * {@link Swatch}'s color. This color is guaranteed to have sufficient contrast.
         */
        @android.support.annotation.ColorInt
        public int getBodyTextColor() {
            ensureTextColorsGenerated();
            return mBodyTextColor;
        }

        private void ensureTextColorsGenerated() {
            if (!mGeneratedTextColors) {
                // First check white, as most colors will be dark
                final int lightBodyAlpha = android.support.v4.graphics.ColorUtils.calculateMinimumAlpha(android.graphics.Color.WHITE, mRgb, android.support.v7.graphics.Palette.MIN_CONTRAST_BODY_TEXT);
                final int lightTitleAlpha = android.support.v4.graphics.ColorUtils.calculateMinimumAlpha(android.graphics.Color.WHITE, mRgb, android.support.v7.graphics.Palette.MIN_CONTRAST_TITLE_TEXT);
                if ((lightBodyAlpha != (-1)) && (lightTitleAlpha != (-1))) {
                    // If we found valid light values, use them and return
                    mBodyTextColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.WHITE, lightBodyAlpha);
                    mTitleTextColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.WHITE, lightTitleAlpha);
                    mGeneratedTextColors = true;
                    return;
                }
                final int darkBodyAlpha = android.support.v4.graphics.ColorUtils.calculateMinimumAlpha(android.graphics.Color.BLACK, mRgb, android.support.v7.graphics.Palette.MIN_CONTRAST_BODY_TEXT);
                final int darkTitleAlpha = android.support.v4.graphics.ColorUtils.calculateMinimumAlpha(android.graphics.Color.BLACK, mRgb, android.support.v7.graphics.Palette.MIN_CONTRAST_TITLE_TEXT);
                if ((darkBodyAlpha != (-1)) && (darkBodyAlpha != (-1))) {
                    // If we found valid dark values, use them and return
                    mBodyTextColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.BLACK, darkBodyAlpha);
                    mTitleTextColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.BLACK, darkTitleAlpha);
                    mGeneratedTextColors = true;
                    return;
                }
                // If we reach here then we can not find title and body values which use the same
                // lightness, we need to use mismatched values
                mBodyTextColor = (lightBodyAlpha != (-1)) ? android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.WHITE, lightBodyAlpha) : android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.BLACK, darkBodyAlpha);
                mTitleTextColor = (lightTitleAlpha != (-1)) ? android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.WHITE, lightTitleAlpha) : android.support.v4.graphics.ColorUtils.setAlphaComponent(android.graphics.Color.BLACK, darkTitleAlpha);
                mGeneratedTextColors = true;
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.lang.StringBuilder(getClass().getSimpleName()).append(" [RGB: #").append(java.lang.Integer.toHexString(getRgb())).append(']').append(" [HSL: ").append(java.util.Arrays.toString(getHsl())).append(']').append(" [Population: ").append(mPopulation).append(']').append(" [Title Text: #").append(java.lang.Integer.toHexString(getTitleTextColor())).append(']').append(" [Body Text: #").append(java.lang.Integer.toHexString(getBodyTextColor())).append(']').toString();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.support.v7.graphics.Palette.Swatch swatch = ((android.support.v7.graphics.Palette.Swatch) (o));
            return (mPopulation == swatch.mPopulation) && (mRgb == swatch.mRgb);
        }

        @java.lang.Override
        public int hashCode() {
            return (31 * mRgb) + mPopulation;
        }
    }

    /**
     * Builder class for generating {@link Palette} instances.
     */
    public static final class Builder {
        private final java.util.List<android.support.v7.graphics.Palette.Swatch> mSwatches;

        private final android.graphics.Bitmap mBitmap;

        private final java.util.List<android.support.v7.graphics.Target> mTargets = new java.util.ArrayList<>();

        private int mMaxColors = android.support.v7.graphics.Palette.DEFAULT_CALCULATE_NUMBER_COLORS;

        private int mResizeArea = android.support.v7.graphics.Palette.DEFAULT_RESIZE_BITMAP_AREA;

        private int mResizeMaxDimension = -1;

        private final java.util.List<android.support.v7.graphics.Palette.Filter> mFilters = new java.util.ArrayList<>();

        private android.graphics.Rect mRegion;

        /**
         * Construct a new {@link Builder} using a source {@link Bitmap}
         */
        public Builder(android.graphics.Bitmap bitmap) {
            if ((bitmap == null) || bitmap.isRecycled()) {
                throw new java.lang.IllegalArgumentException("Bitmap is not valid");
            }
            mFilters.add(android.support.v7.graphics.Palette.DEFAULT_FILTER);
            mBitmap = bitmap;
            mSwatches = null;
            // Add the default targets
            mTargets.add(android.support.v7.graphics.Target.LIGHT_VIBRANT);
            mTargets.add(android.support.v7.graphics.Target.VIBRANT);
            mTargets.add(android.support.v7.graphics.Target.DARK_VIBRANT);
            mTargets.add(android.support.v7.graphics.Target.LIGHT_MUTED);
            mTargets.add(android.support.v7.graphics.Target.MUTED);
            mTargets.add(android.support.v7.graphics.Target.DARK_MUTED);
        }

        /**
         * Construct a new {@link Builder} using a list of {@link Swatch} instances.
         * Typically only used for testing.
         */
        public Builder(java.util.List<android.support.v7.graphics.Palette.Swatch> swatches) {
            if ((swatches == null) || swatches.isEmpty()) {
                throw new java.lang.IllegalArgumentException("List of Swatches is not valid");
            }
            mFilters.add(android.support.v7.graphics.Palette.DEFAULT_FILTER);
            mSwatches = swatches;
            mBitmap = null;
        }

        /**
         * Set the maximum number of colors to use in the quantization step when using a
         * {@link android.graphics.Bitmap} as the source.
         * <p>
         * Good values for depend on the source image type. For landscapes, good values are in
         * the range 10-16. For images which are largely made up of people's faces then this
         * value should be increased to ~24.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder maximumColorCount(int colors) {
            mMaxColors = colors;
            return this;
        }

        /**
         * Set the resize value when using a {@link android.graphics.Bitmap} as the source.
         * If the bitmap's largest dimension is greater than the value specified, then the bitmap
         * will be resized so that it's largest dimension matches {@code maxDimension}. If the
         * bitmap is smaller or equal, the original is used as-is.
         *
         * @deprecated Using {@link #resizeBitmapArea(int)} is preferred since it can handle
        abnormal aspect ratios more gracefully.
         * @param maxDimension
         * 		the number of pixels that the max dimension should be scaled down to,
         * 		or any value <= 0 to disable resizing.
         */
        @android.support.annotation.NonNull
        @java.lang.Deprecated
        public android.support.v7.graphics.Palette.Builder resizeBitmapSize(final int maxDimension) {
            mResizeMaxDimension = maxDimension;
            mResizeArea = -1;
            return this;
        }

        /**
         * Set the resize value when using a {@link android.graphics.Bitmap} as the source.
         * If the bitmap's area is greater than the value specified, then the bitmap
         * will be resized so that it's area matches {@code area}. If the
         * bitmap is smaller or equal, the original is used as-is.
         * <p>
         * This value has a large effect on the processing time. The larger the resized image is,
         * the greater time it will take to generate the palette. The smaller the image is, the
         * more detail is lost in the resulting image and thus less precision for color selection.
         *
         * @param area
         * 		the number of pixels that the intermediary scaled down Bitmap should cover,
         * 		or any value <= 0 to disable resizing.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder resizeBitmapArea(final int area) {
            mResizeArea = area;
            mResizeMaxDimension = -1;
            return this;
        }

        /**
         * Clear all added filters. This includes any default filters added automatically by
         * {@link Palette}.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder clearFilters() {
            mFilters.clear();
            return this;
        }

        /**
         * Add a filter to be able to have fine grained control over which colors are
         * allowed in the resulting palette.
         *
         * @param filter
         * 		filter to add.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder addFilter(android.support.v7.graphics.Palette.Filter filter) {
            if (filter != null) {
                mFilters.add(filter);
            }
            return this;
        }

        /**
         * Set a region of the bitmap to be used exclusively when calculating the palette.
         * <p>This only works when the original input is a {@link Bitmap}.</p>
         *
         * @param left
         * 		The left side of the rectangle used for the region.
         * @param top
         * 		The top of the rectangle used for the region.
         * @param right
         * 		The right side of the rectangle used for the region.
         * @param bottom
         * 		The bottom of the rectangle used for the region.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder setRegion(int left, int top, int right, int bottom) {
            if (mBitmap != null) {
                if (mRegion == null)
                    mRegion = new android.graphics.Rect();

                // Set the Rect to be initially the whole Bitmap
                mRegion.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
                // Now just get the intersection with the region
                if (!mRegion.intersect(left, top, right, bottom)) {
                    throw new java.lang.IllegalArgumentException("The given region must intersect with " + "the Bitmap's dimensions.");
                }
            }
            return this;
        }

        /**
         * Clear any previously region set via {@link #setRegion(int, int, int, int)}.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder clearRegion() {
            mRegion = null;
            return this;
        }

        /**
         * Add a target profile to be generated in the palette.
         *
         * <p>You can retrieve the result via {@link Palette#getSwatchForTarget(Target)}.</p>
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder addTarget(@android.support.annotation.NonNull
        final android.support.v7.graphics.Target target) {
            if (!mTargets.contains(target)) {
                mTargets.add(target);
            }
            return this;
        }

        /**
         * Clear all added targets. This includes any default targets added automatically by
         * {@link Palette}.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette.Builder clearTargets() {
            if (mTargets != null) {
                mTargets.clear();
            }
            return this;
        }

        /**
         * Generate and return the {@link Palette} synchronously.
         */
        @android.support.annotation.NonNull
        public android.support.v7.graphics.Palette generate() {
            final android.util.TimingLogger logger = (android.support.v7.graphics.Palette.LOG_TIMINGS) ? new android.util.TimingLogger(android.support.v7.graphics.Palette.LOG_TAG, "Generation") : null;
            java.util.List<android.support.v7.graphics.Palette.Swatch> swatches;
            if (mBitmap != null) {
                // We have a Bitmap so we need to use quantization to reduce the number of colors
                // First we'll scale down the bitmap if needed
                final android.graphics.Bitmap bitmap = scaleBitmapDown(mBitmap);
                if (logger != null) {
                    logger.addSplit("Processed Bitmap");
                }
                final android.graphics.Rect region = mRegion;
                if ((bitmap != mBitmap) && (region != null)) {
                    // If we have a scaled bitmap and a selected region, we need to scale down the
                    // region to match the new scale
                    final double scale = bitmap.getWidth() / ((double) (mBitmap.getWidth()));
                    region.left = ((int) (java.lang.Math.floor(region.left * scale)));
                    region.top = ((int) (java.lang.Math.floor(region.top * scale)));
                    region.right = java.lang.Math.min(((int) (java.lang.Math.ceil(region.right * scale))), bitmap.getWidth());
                    region.bottom = java.lang.Math.min(((int) (java.lang.Math.ceil(region.bottom * scale))), bitmap.getHeight());
                }
                // Now generate a quantizer from the Bitmap
                final android.support.v7.graphics.ColorCutQuantizer quantizer = new android.support.v7.graphics.ColorCutQuantizer(getPixelsFromBitmap(bitmap), mMaxColors, mFilters.isEmpty() ? null : mFilters.toArray(new android.support.v7.graphics.Palette.Filter[mFilters.size()]));
                // If created a new bitmap, recycle it
                if (bitmap != mBitmap) {
                    bitmap.recycle();
                }
                swatches = quantizer.getQuantizedColors();
                if (logger != null) {
                    logger.addSplit("Color quantization completed");
                }
            } else {
                // Else we're using the provided swatches
                swatches = mSwatches;
            }
            // Now create a Palette instance
            final android.support.v7.graphics.Palette p = new android.support.v7.graphics.Palette(swatches, mTargets);
            // And make it generate itself
            p.generate();
            if (logger != null) {
                logger.addSplit("Created Palette");
                logger.dumpToLog();
            }
            return p;
        }

        /**
         * Generate the {@link Palette} asynchronously. The provided listener's
         * {@link PaletteAsyncListener#onGenerated} method will be called with the palette when
         * generated.
         */
        @android.support.annotation.NonNull
        public android.os.AsyncTask<android.graphics.Bitmap, java.lang.Void, android.support.v7.graphics.Palette> generate(final android.support.v7.graphics.Palette.PaletteAsyncListener listener) {
            if (listener == null) {
                throw new java.lang.IllegalArgumentException("listener can not be null");
            }
            return android.support.v4.os.AsyncTaskCompat.executeParallel(new android.os.AsyncTask<android.graphics.Bitmap, java.lang.Void, android.support.v7.graphics.Palette>() {
                @java.lang.Override
                protected android.support.v7.graphics.Palette doInBackground(android.graphics.Bitmap... params) {
                    try {
                        return generate();
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(android.support.v7.graphics.Palette.LOG_TAG, "Exception thrown during async generate", e);
                        return null;
                    }
                }

                @java.lang.Override
                protected void onPostExecute(android.support.v7.graphics.Palette colorExtractor) {
                    listener.onGenerated(colorExtractor);
                }
            }, mBitmap);
        }

        private int[] getPixelsFromBitmap(android.graphics.Bitmap bitmap) {
            final int bitmapWidth = bitmap.getWidth();
            final int bitmapHeight = bitmap.getHeight();
            final int[] pixels = new int[bitmapWidth * bitmapHeight];
            bitmap.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
            if (mRegion == null) {
                // If we don't have a region, return all of the pixels
                return pixels;
            } else {
                // If we do have a region, lets create a subset array containing only the region's
                // pixels
                final int regionWidth = mRegion.width();
                final int regionHeight = mRegion.height();
                // pixels contains all of the pixels, so we need to iterate through each row and
                // copy the regions pixels into a new smaller array
                final int[] subsetPixels = new int[regionWidth * regionHeight];
                for (int row = 0; row < regionHeight; row++) {
                    java.lang.System.arraycopy(pixels, ((row + mRegion.top) * bitmapWidth) + mRegion.left, subsetPixels, row * regionWidth, regionWidth);
                }
                return subsetPixels;
            }
        }

        /**
         * Scale the bitmap down as needed.
         */
        private android.graphics.Bitmap scaleBitmapDown(final android.graphics.Bitmap bitmap) {
            double scaleRatio = -1;
            if (mResizeArea > 0) {
                final int bitmapArea = bitmap.getWidth() * bitmap.getHeight();
                if (bitmapArea > mResizeArea) {
                    scaleRatio = mResizeArea / ((double) (bitmapArea));
                }
            } else
                if (mResizeMaxDimension > 0) {
                    final int maxDimension = java.lang.Math.max(bitmap.getWidth(), bitmap.getHeight());
                    if (maxDimension > mResizeMaxDimension) {
                        scaleRatio = mResizeMaxDimension / ((double) (maxDimension));
                    }
                }

            if (scaleRatio <= 0) {
                // Scaling has been disabled or not needed so just return the Bitmap
                return bitmap;
            }
            return android.graphics.Bitmap.createScaledBitmap(bitmap, ((int) (java.lang.Math.ceil(bitmap.getWidth() * scaleRatio))), ((int) (java.lang.Math.ceil(bitmap.getHeight() * scaleRatio))), false);
        }
    }

    /**
     * A Filter provides a mechanism for exercising fine-grained control over which colors
     * are valid within a resulting {@link Palette}.
     */
    public interface Filter {
        /**
         * Hook to allow clients to be able filter colors from resulting palette.
         *
         * @param rgb
         * 		the color in RGB888.
         * @param hsl
         * 		HSL representation of the color.
         * @return true if the color is allowed, false if not.
         * @see Builder#addFilter(Filter)
         */
        boolean isAllowed(int rgb, float[] hsl);
    }

    /**
     * The default filter.
     */
    static final android.support.v7.graphics.Palette.Filter DEFAULT_FILTER = new android.support.v7.graphics.Palette.Filter() {
        private static final float BLACK_MAX_LIGHTNESS = 0.05F;

        private static final float WHITE_MIN_LIGHTNESS = 0.95F;

        @java.lang.Override
        public boolean isAllowed(int rgb, float[] hsl) {
            return ((!isWhite(hsl)) && (!isBlack(hsl))) && (!isNearRedILine(hsl));
        }

        /**
         *
         *
         * @return true if the color represents a color which is close to black.
         */
        private boolean isBlack(float[] hslColor) {
            return hslColor[2] <= BLACK_MAX_LIGHTNESS;
        }

        /**
         *
         *
         * @return true if the color represents a color which is close to white.
         */
        private boolean isWhite(float[] hslColor) {
            return hslColor[2] >= WHITE_MIN_LIGHTNESS;
        }

        /**
         *
         *
         * @return true if the color lies close to the red side of the I line.
         */
        private boolean isNearRedILine(float[] hslColor) {
            return ((hslColor[0] >= 10.0F) && (hslColor[0] <= 37.0F)) && (hslColor[1] <= 0.82F);
        }
    };
}

