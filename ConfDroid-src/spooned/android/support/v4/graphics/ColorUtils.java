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
package android.support.v4.graphics;


/**
 * A set of color-related utility methods, building upon those available in {@code Color}.
 */
public final class ColorUtils {
    private static final double XYZ_WHITE_REFERENCE_X = 95.047;

    private static final double XYZ_WHITE_REFERENCE_Y = 100;

    private static final double XYZ_WHITE_REFERENCE_Z = 108.883;

    private static final double XYZ_EPSILON = 0.008856;

    private static final double XYZ_KAPPA = 903.3;

    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;

    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;

    private static final java.lang.ThreadLocal<double[]> TEMP_ARRAY = new java.lang.ThreadLocal<>();

    private ColorUtils() {
    }

    /**
     * Composite two potentially translucent colors over each other and returns the result.
     */
    public static int compositeColors(@android.support.annotation.ColorInt
    int foreground, @android.support.annotation.ColorInt
    int background) {
        int bgAlpha = android.graphics.Color.alpha(background);
        int fgAlpha = android.graphics.Color.alpha(foreground);
        int a = android.support.v4.graphics.ColorUtils.compositeAlpha(fgAlpha, bgAlpha);
        int r = android.support.v4.graphics.ColorUtils.compositeComponent(android.graphics.Color.red(foreground), fgAlpha, android.graphics.Color.red(background), bgAlpha, a);
        int g = android.support.v4.graphics.ColorUtils.compositeComponent(android.graphics.Color.green(foreground), fgAlpha, android.graphics.Color.green(background), bgAlpha, a);
        int b = android.support.v4.graphics.ColorUtils.compositeComponent(android.graphics.Color.blue(foreground), fgAlpha, android.graphics.Color.blue(background), bgAlpha, a);
        return android.graphics.Color.argb(a, r, g, b);
    }

    private static int compositeAlpha(int foregroundAlpha, int backgroundAlpha) {
        return 0xff - (((0xff - backgroundAlpha) * (0xff - foregroundAlpha)) / 0xff);
    }

    private static int compositeComponent(int fgC, int fgA, int bgC, int bgA, int a) {
        if (a == 0)
            return 0;

        return (((0xff * fgC) * fgA) + ((bgC * bgA) * (0xff - fgA))) / (a * 0xff);
    }

    /**
     * Returns the luminance of a color as a float between {@code 0.0} and {@code 1.0}.
     * <p>Defined as the Y component in the XYZ representation of {@code color}.</p>
     */
    @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    public static double calculateLuminance(@android.support.annotation.ColorInt
    int color) {
        final double[] result = android.support.v4.graphics.ColorUtils.getTempDouble3Array();
        android.support.v4.graphics.ColorUtils.colorToXYZ(color, result);
        // Luminance is the Y component
        return result[1] / 100;
    }

    /**
     * Returns the contrast ratio between {@code foreground} and {@code background}.
     * {@code background} must be opaque.
     * <p>
     * Formula defined
     * <a href="http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef">here</a>.
     */
    public static double calculateContrast(@android.support.annotation.ColorInt
    int foreground, @android.support.annotation.ColorInt
    int background) {
        if (android.graphics.Color.alpha(background) != 255) {
            throw new java.lang.IllegalArgumentException("background can not be translucent: #" + java.lang.Integer.toHexString(background));
        }
        if (android.graphics.Color.alpha(foreground) < 255) {
            // If the foreground is translucent, composite the foreground over the background
            foreground = android.support.v4.graphics.ColorUtils.compositeColors(foreground, background);
        }
        final double luminance1 = android.support.v4.graphics.ColorUtils.calculateLuminance(foreground) + 0.05;
        final double luminance2 = android.support.v4.graphics.ColorUtils.calculateLuminance(background) + 0.05;
        // Now return the lighter luminance divided by the darker luminance
        return java.lang.Math.max(luminance1, luminance2) / java.lang.Math.min(luminance1, luminance2);
    }

    /**
     * Calculates the minimum alpha value which can be applied to {@code foreground} so that would
     * have a contrast value of at least {@code minContrastRatio} when compared to
     * {@code background}.
     *
     * @param foreground
     * 		the foreground color
     * @param background
     * 		the opaque background color
     * @param minContrastRatio
     * 		the minimum contrast ratio
     * @return the alpha value in the range 0-255, or -1 if no value could be calculated
     */
    public static int calculateMinimumAlpha(@android.support.annotation.ColorInt
    int foreground, @android.support.annotation.ColorInt
    int background, float minContrastRatio) {
        if (android.graphics.Color.alpha(background) != 255) {
            throw new java.lang.IllegalArgumentException("background can not be translucent: #" + java.lang.Integer.toHexString(background));
        }
        // First lets check that a fully opaque foreground has sufficient contrast
        int testForeground = android.support.v4.graphics.ColorUtils.setAlphaComponent(foreground, 255);
        double testRatio = android.support.v4.graphics.ColorUtils.calculateContrast(testForeground, background);
        if (testRatio < minContrastRatio) {
            // Fully opaque foreground does not have sufficient contrast, return error
            return -1;
        }
        // Binary search to find a value with the minimum value which provides sufficient contrast
        int numIterations = 0;
        int minAlpha = 0;
        int maxAlpha = 255;
        while ((numIterations <= android.support.v4.graphics.ColorUtils.MIN_ALPHA_SEARCH_MAX_ITERATIONS) && ((maxAlpha - minAlpha) > android.support.v4.graphics.ColorUtils.MIN_ALPHA_SEARCH_PRECISION)) {
            final int testAlpha = (minAlpha + maxAlpha) / 2;
            testForeground = android.support.v4.graphics.ColorUtils.setAlphaComponent(foreground, testAlpha);
            testRatio = android.support.v4.graphics.ColorUtils.calculateContrast(testForeground, background);
            if (testRatio < minContrastRatio) {
                minAlpha = testAlpha;
            } else {
                maxAlpha = testAlpha;
            }
            numIterations++;
        } 
        // Conservatively return the max of the range of possible alphas, which is known to pass.
        return maxAlpha;
    }

    /**
     * Convert RGB components to HSL (hue-saturation-lightness).
     * <ul>
     * <li>outHsl[0] is Hue [0 .. 360)</li>
     * <li>outHsl[1] is Saturation [0...1]</li>
     * <li>outHsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param r
     * 		red component value [0..255]
     * @param g
     * 		green component value [0..255]
     * @param b
     * 		blue component value [0..255]
     * @param outHsl
     * 		3-element array which holds the resulting HSL components
     */
    public static void RGBToHSL(@android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int r, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int g, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int b, @android.support.annotation.NonNull
    float[] outHsl) {
        final float rf = r / 255.0F;
        final float gf = g / 255.0F;
        final float bf = b / 255.0F;
        final float max = java.lang.Math.max(rf, java.lang.Math.max(gf, bf));
        final float min = java.lang.Math.min(rf, java.lang.Math.min(gf, bf));
        final float deltaMaxMin = max - min;
        float h;
        float s;
        float l = (max + min) / 2.0F;
        if (max == min) {
            // Monochromatic
            h = s = 0.0F;
        } else {
            if (max == rf) {
                h = ((gf - bf) / deltaMaxMin) % 6.0F;
            } else
                if (max == gf) {
                    h = ((bf - rf) / deltaMaxMin) + 2.0F;
                } else {
                    h = ((rf - gf) / deltaMaxMin) + 4.0F;
                }

            s = deltaMaxMin / (1.0F - java.lang.Math.abs((2.0F * l) - 1.0F));
        }
        h = (h * 60.0F) % 360.0F;
        if (h < 0) {
            h += 360.0F;
        }
        outHsl[0] = android.support.v4.graphics.ColorUtils.constrain(h, 0.0F, 360.0F);
        outHsl[1] = android.support.v4.graphics.ColorUtils.constrain(s, 0.0F, 1.0F);
        outHsl[2] = android.support.v4.graphics.ColorUtils.constrain(l, 0.0F, 1.0F);
    }

    /**
     * Convert the ARGB color to its HSL (hue-saturation-lightness) components.
     * <ul>
     * <li>outHsl[0] is Hue [0 .. 360)</li>
     * <li>outHsl[1] is Saturation [0...1]</li>
     * <li>outHsl[2] is Lightness [0...1]</li>
     * </ul>
     *
     * @param color
     * 		the ARGB color to convert. The alpha component is ignored
     * @param outHsl
     * 		3-element array which holds the resulting HSL components
     */
    public static void colorToHSL(@android.support.annotation.ColorInt
    int color, @android.support.annotation.NonNull
    float[] outHsl) {
        android.support.v4.graphics.ColorUtils.RGBToHSL(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), outHsl);
    }

    /**
     * Convert HSL (hue-saturation-lightness) components to a RGB color.
     * <ul>
     * <li>hsl[0] is Hue [0 .. 360)</li>
     * <li>hsl[1] is Saturation [0...1]</li>
     * <li>hsl[2] is Lightness [0...1]</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param hsl
     * 		3-element array which holds the input HSL components
     * @return the resulting RGB color
     */
    @android.support.annotation.ColorInt
    public static int HSLToColor(@android.support.annotation.NonNull
    float[] hsl) {
        final float h = hsl[0];
        final float s = hsl[1];
        final float l = hsl[2];
        final float c = (1.0F - java.lang.Math.abs((2 * l) - 1.0F)) * s;
        final float m = l - (0.5F * c);
        final float x = c * (1.0F - java.lang.Math.abs(((h / 60.0F) % 2.0F) - 1.0F));
        final int hueSegment = ((int) (h)) / 60;
        int r = 0;
        int g = 0;
        int b = 0;
        switch (hueSegment) {
            case 0 :
                r = java.lang.Math.round(255 * (c + m));
                g = java.lang.Math.round(255 * (x + m));
                b = java.lang.Math.round(255 * m);
                break;
            case 1 :
                r = java.lang.Math.round(255 * (x + m));
                g = java.lang.Math.round(255 * (c + m));
                b = java.lang.Math.round(255 * m);
                break;
            case 2 :
                r = java.lang.Math.round(255 * m);
                g = java.lang.Math.round(255 * (c + m));
                b = java.lang.Math.round(255 * (x + m));
                break;
            case 3 :
                r = java.lang.Math.round(255 * m);
                g = java.lang.Math.round(255 * (x + m));
                b = java.lang.Math.round(255 * (c + m));
                break;
            case 4 :
                r = java.lang.Math.round(255 * (x + m));
                g = java.lang.Math.round(255 * m);
                b = java.lang.Math.round(255 * (c + m));
                break;
            case 5 :
            case 6 :
                r = java.lang.Math.round(255 * (c + m));
                g = java.lang.Math.round(255 * m);
                b = java.lang.Math.round(255 * (x + m));
                break;
        }
        r = android.support.v4.graphics.ColorUtils.constrain(r, 0, 255);
        g = android.support.v4.graphics.ColorUtils.constrain(g, 0, 255);
        b = android.support.v4.graphics.ColorUtils.constrain(b, 0, 255);
        return android.graphics.Color.rgb(r, g, b);
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    @android.support.annotation.ColorInt
    public static int setAlphaComponent(@android.support.annotation.ColorInt
    int color, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int alpha) {
        if ((alpha < 0) || (alpha > 255)) {
            throw new java.lang.IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (color & 0xffffff) | (alpha << 24);
    }

    /**
     * Convert the ARGB color to its CIE Lab representative components.
     *
     * @param color
     * 		the ARGB color to convert. The alpha component is ignored
     * @param outLab
     * 		3-element array which holds the resulting LAB components
     */
    public static void colorToLAB(@android.support.annotation.ColorInt
    int color, @android.support.annotation.NonNull
    double[] outLab) {
        android.support.v4.graphics.ColorUtils.RGBToLAB(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), outLab);
    }

    /**
     * Convert RGB components to its CIE Lab representative components.
     *
     * <ul>
     * <li>outLab[0] is L [0 ...1)</li>
     * <li>outLab[1] is a [-128...127)</li>
     * <li>outLab[2] is b [-128...127)</li>
     * </ul>
     *
     * @param r
     * 		red component value [0..255]
     * @param g
     * 		green component value [0..255]
     * @param b
     * 		blue component value [0..255]
     * @param outLab
     * 		3-element array which holds the resulting LAB components
     */
    public static void RGBToLAB(@android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int r, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int g, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int b, @android.support.annotation.NonNull
    double[] outLab) {
        // First we convert RGB to XYZ
        android.support.v4.graphics.ColorUtils.RGBToXYZ(r, g, b, outLab);
        // outLab now contains XYZ
        android.support.v4.graphics.ColorUtils.XYZToLAB(outLab[0], outLab[1], outLab[2], outLab);
        // outLab now contains LAB representation
    }

    /**
     * Convert the ARGB color to it's CIE XYZ representative components.
     *
     * <p>The resulting XYZ representation will use the D65 illuminant and the CIE
     * 2° Standard Observer (1931).</p>
     *
     * <ul>
     * <li>outXyz[0] is X [0 ...95.047)</li>
     * <li>outXyz[1] is Y [0...100)</li>
     * <li>outXyz[2] is Z [0...108.883)</li>
     * </ul>
     *
     * @param color
     * 		the ARGB color to convert. The alpha component is ignored
     * @param outXyz
     * 		3-element array which holds the resulting LAB components
     */
    public static void colorToXYZ(@android.support.annotation.ColorInt
    int color, @android.support.annotation.NonNull
    double[] outXyz) {
        android.support.v4.graphics.ColorUtils.RGBToXYZ(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), outXyz);
    }

    /**
     * Convert RGB components to it's CIE XYZ representative components.
     *
     * <p>The resulting XYZ representation will use the D65 illuminant and the CIE
     * 2° Standard Observer (1931).</p>
     *
     * <ul>
     * <li>outXyz[0] is X [0 ...95.047)</li>
     * <li>outXyz[1] is Y [0...100)</li>
     * <li>outXyz[2] is Z [0...108.883)</li>
     * </ul>
     *
     * @param r
     * 		red component value [0..255]
     * @param g
     * 		green component value [0..255]
     * @param b
     * 		blue component value [0..255]
     * @param outXyz
     * 		3-element array which holds the resulting XYZ components
     */
    public static void RGBToXYZ(@android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int r, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int g, @android.support.annotation.IntRange(from = 0x0, to = 0xff)
    int b, @android.support.annotation.NonNull
    double[] outXyz) {
        if (outXyz.length != 3) {
            throw new java.lang.IllegalArgumentException("outXyz must have a length of 3.");
        }
        double sr = r / 255.0;
        sr = (sr < 0.04045) ? sr / 12.92 : java.lang.Math.pow((sr + 0.055) / 1.055, 2.4);
        double sg = g / 255.0;
        sg = (sg < 0.04045) ? sg / 12.92 : java.lang.Math.pow((sg + 0.055) / 1.055, 2.4);
        double sb = b / 255.0;
        sb = (sb < 0.04045) ? sb / 12.92 : java.lang.Math.pow((sb + 0.055) / 1.055, 2.4);
        outXyz[0] = 100 * (((sr * 0.4124) + (sg * 0.3576)) + (sb * 0.1805));
        outXyz[1] = 100 * (((sr * 0.2126) + (sg * 0.7152)) + (sb * 0.0722));
        outXyz[2] = 100 * (((sr * 0.0193) + (sg * 0.1192)) + (sb * 0.9505));
    }

    /**
     * Converts a color from CIE XYZ to CIE Lab representation.
     *
     * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE
     * 2° Standard Observer (1931).</p>
     *
     * <ul>
     * <li>outLab[0] is L [0 ...1)</li>
     * <li>outLab[1] is a [-128...127)</li>
     * <li>outLab[2] is b [-128...127)</li>
     * </ul>
     *
     * @param x
     * 		X component value [0...95.047)
     * @param y
     * 		Y component value [0...100)
     * @param z
     * 		Z component value [0...108.883)
     * @param outLab
     * 		3-element array which holds the resulting Lab components
     */
    public static void XYZToLAB(@android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_X)
    double x, @android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Y)
    double y, @android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Z)
    double z, @android.support.annotation.NonNull
    double[] outLab) {
        if (outLab.length != 3) {
            throw new java.lang.IllegalArgumentException("outLab must have a length of 3.");
        }
        x = android.support.v4.graphics.ColorUtils.pivotXyzComponent(x / android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_X);
        y = android.support.v4.graphics.ColorUtils.pivotXyzComponent(y / android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Y);
        z = android.support.v4.graphics.ColorUtils.pivotXyzComponent(z / android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Z);
        outLab[0] = java.lang.Math.max(0, (116 * y) - 16);
        outLab[1] = 500 * (x - y);
        outLab[2] = 200 * (y - z);
    }

    /**
     * Converts a color from CIE Lab to CIE XYZ representation.
     *
     * <p>The resulting XYZ representation will use the D65 illuminant and the CIE
     * 2° Standard Observer (1931).</p>
     *
     * <ul>
     * <li>outXyz[0] is X [0 ...95.047)</li>
     * <li>outXyz[1] is Y [0...100)</li>
     * <li>outXyz[2] is Z [0...108.883)</li>
     * </ul>
     *
     * @param l
     * 		L component value [0...100)
     * @param a
     * 		A component value [-128...127)
     * @param b
     * 		B component value [-128...127)
     * @param outXyz
     * 		3-element array which holds the resulting XYZ components
     */
    public static void LABToXYZ(@android.support.annotation.FloatRange(from = 0.0F, to = 100)
    final double l, @android.support.annotation.FloatRange(from = -128, to = 127)
    final double a, @android.support.annotation.FloatRange(from = -128, to = 127)
    final double b, @android.support.annotation.NonNull
    double[] outXyz) {
        final double fy = (l + 16) / 116;
        final double fx = (a / 500) + fy;
        final double fz = fy - (b / 200);
        double tmp = java.lang.Math.pow(fx, 3);
        final double xr = (tmp > android.support.v4.graphics.ColorUtils.XYZ_EPSILON) ? tmp : ((116 * fx) - 16) / android.support.v4.graphics.ColorUtils.XYZ_KAPPA;
        final double yr = (l > (android.support.v4.graphics.ColorUtils.XYZ_KAPPA * android.support.v4.graphics.ColorUtils.XYZ_EPSILON)) ? java.lang.Math.pow(fy, 3) : l / android.support.v4.graphics.ColorUtils.XYZ_KAPPA;
        tmp = java.lang.Math.pow(fz, 3);
        final double zr = (tmp > android.support.v4.graphics.ColorUtils.XYZ_EPSILON) ? tmp : ((116 * fz) - 16) / android.support.v4.graphics.ColorUtils.XYZ_KAPPA;
        outXyz[0] = xr * android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_X;
        outXyz[1] = yr * android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Y;
        outXyz[2] = zr * android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Z;
    }

    /**
     * Converts a color from CIE XYZ to its RGB representation.
     *
     * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE
     * 2° Standard Observer (1931).</p>
     *
     * @param x
     * 		X component value [0...95.047)
     * @param y
     * 		Y component value [0...100)
     * @param z
     * 		Z component value [0...108.883)
     * @return int containing the RGB representation
     */
    @android.support.annotation.ColorInt
    public static int XYZToColor(@android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_X)
    double x, @android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Y)
    double y, @android.support.annotation.FloatRange(from = 0.0F, to = android.support.v4.graphics.ColorUtils.XYZ_WHITE_REFERENCE_Z)
    double z) {
        double r = (((x * 3.2406) + (y * (-1.5372))) + (z * (-0.4986))) / 100;
        double g = (((x * (-0.9689)) + (y * 1.8758)) + (z * 0.0415)) / 100;
        double b = (((x * 0.0557) + (y * (-0.204))) + (z * 1.057)) / 100;
        r = (r > 0.0031308) ? (1.055 * java.lang.Math.pow(r, 1 / 2.4)) - 0.055 : 12.92 * r;
        g = (g > 0.0031308) ? (1.055 * java.lang.Math.pow(g, 1 / 2.4)) - 0.055 : 12.92 * g;
        b = (b > 0.0031308) ? (1.055 * java.lang.Math.pow(b, 1 / 2.4)) - 0.055 : 12.92 * b;
        return android.graphics.Color.rgb(android.support.v4.graphics.ColorUtils.constrain(((int) (java.lang.Math.round(r * 255))), 0, 255), android.support.v4.graphics.ColorUtils.constrain(((int) (java.lang.Math.round(g * 255))), 0, 255), android.support.v4.graphics.ColorUtils.constrain(((int) (java.lang.Math.round(b * 255))), 0, 255));
    }

    /**
     * Converts a color from CIE Lab to its RGB representation.
     *
     * @param l
     * 		L component value [0...100]
     * @param a
     * 		A component value [-128...127]
     * @param b
     * 		B component value [-128...127]
     * @return int containing the RGB representation
     */
    @android.support.annotation.ColorInt
    public static int LABToColor(@android.support.annotation.FloatRange(from = 0.0F, to = 100)
    final double l, @android.support.annotation.FloatRange(from = -128, to = 127)
    final double a, @android.support.annotation.FloatRange(from = -128, to = 127)
    final double b) {
        final double[] result = android.support.v4.graphics.ColorUtils.getTempDouble3Array();
        android.support.v4.graphics.ColorUtils.LABToXYZ(l, a, b, result);
        return android.support.v4.graphics.ColorUtils.XYZToColor(result[0], result[1], result[2]);
    }

    /**
     * Returns the euclidean distance between two LAB colors.
     */
    public static double distanceEuclidean(@android.support.annotation.NonNull
    double[] labX, @android.support.annotation.NonNull
    double[] labY) {
        return java.lang.Math.sqrt((java.lang.Math.pow(labX[0] - labY[0], 2) + java.lang.Math.pow(labX[1] - labY[1], 2)) + java.lang.Math.pow(labX[2] - labY[2], 2));
    }

    private static float constrain(float amount, float low, float high) {
        return amount < low ? low : amount > high ? high : amount;
    }

    private static int constrain(int amount, int low, int high) {
        return amount < low ? low : amount > high ? high : amount;
    }

    private static double pivotXyzComponent(double component) {
        return component > android.support.v4.graphics.ColorUtils.XYZ_EPSILON ? java.lang.Math.pow(component, 1 / 3.0) : ((android.support.v4.graphics.ColorUtils.XYZ_KAPPA * component) + 16) / 116;
    }

    /**
     * Blend between two ARGB colors using the given ratio.
     *
     * <p>A blend ratio of 0.0 will result in {@code color1}, 0.5 will give an even blend,
     * 1.0 will result in {@code color2}.</p>
     *
     * @param color1
     * 		the first ARGB color
     * @param color2
     * 		the second ARGB color
     * @param ratio
     * 		the blend ratio of {@code color1} to {@code color2}
     */
    @android.support.annotation.ColorInt
    public static int blendARGB(@android.support.annotation.ColorInt
    int color1, @android.support.annotation.ColorInt
    int color2, @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    float ratio) {
        final float inverseRatio = 1 - ratio;
        float a = (android.graphics.Color.alpha(color1) * inverseRatio) + (android.graphics.Color.alpha(color2) * ratio);
        float r = (android.graphics.Color.red(color1) * inverseRatio) + (android.graphics.Color.red(color2) * ratio);
        float g = (android.graphics.Color.green(color1) * inverseRatio) + (android.graphics.Color.green(color2) * ratio);
        float b = (android.graphics.Color.blue(color1) * inverseRatio) + (android.graphics.Color.blue(color2) * ratio);
        return android.graphics.Color.argb(((int) (a)), ((int) (r)), ((int) (g)), ((int) (b)));
    }

    /**
     * Blend between {@code hsl1} and {@code hsl2} using the given ratio. This will interpolate
     * the hue using the shortest angle.
     *
     * <p>A blend ratio of 0.0 will result in {@code hsl1}, 0.5 will give an even blend,
     * 1.0 will result in {@code hsl2}.</p>
     *
     * @param hsl1
     * 		3-element array which holds the first HSL color
     * @param hsl2
     * 		3-element array which holds the second HSL color
     * @param ratio
     * 		the blend ratio of {@code hsl1} to {@code hsl2}
     * @param outResult
     * 		3-element array which holds the resulting HSL components
     */
    public static void blendHSL(@android.support.annotation.NonNull
    float[] hsl1, @android.support.annotation.NonNull
    float[] hsl2, @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    float ratio, @android.support.annotation.NonNull
    float[] outResult) {
        if (outResult.length != 3) {
            throw new java.lang.IllegalArgumentException("result must have a length of 3.");
        }
        final float inverseRatio = 1 - ratio;
        // Since hue is circular we will need to interpolate carefully
        outResult[0] = android.support.v4.graphics.ColorUtils.circularInterpolate(hsl1[0], hsl2[0], ratio);
        outResult[1] = (hsl1[1] * inverseRatio) + (hsl2[1] * ratio);
        outResult[2] = (hsl1[2] * inverseRatio) + (hsl2[2] * ratio);
    }

    /**
     * Blend between two CIE-LAB colors using the given ratio.
     *
     * <p>A blend ratio of 0.0 will result in {@code lab1}, 0.5 will give an even blend,
     * 1.0 will result in {@code lab2}.</p>
     *
     * @param lab1
     * 		3-element array which holds the first LAB color
     * @param lab2
     * 		3-element array which holds the second LAB color
     * @param ratio
     * 		the blend ratio of {@code lab1} to {@code lab2}
     * @param outResult
     * 		3-element array which holds the resulting LAB components
     */
    public static void blendLAB(@android.support.annotation.NonNull
    double[] lab1, @android.support.annotation.NonNull
    double[] lab2, @android.support.annotation.FloatRange(from = 0.0, to = 1.0)
    double ratio, @android.support.annotation.NonNull
    double[] outResult) {
        if (outResult.length != 3) {
            throw new java.lang.IllegalArgumentException("outResult must have a length of 3.");
        }
        final double inverseRatio = 1 - ratio;
        outResult[0] = (lab1[0] * inverseRatio) + (lab2[0] * ratio);
        outResult[1] = (lab1[1] * inverseRatio) + (lab2[1] * ratio);
        outResult[2] = (lab1[2] * inverseRatio) + (lab2[2] * ratio);
    }

    @android.support.annotation.VisibleForTesting
    static float circularInterpolate(float a, float b, float f) {
        if (java.lang.Math.abs(b - a) > 180) {
            if (b > a) {
                a += 360;
            } else {
                b += 360;
            }
        }
        return (a + ((b - a) * f)) % 360;
    }

    private static double[] getTempDouble3Array() {
        double[] result = android.support.v4.graphics.ColorUtils.TEMP_ARRAY.get();
        if (result == null) {
            result = new double[3];
            android.support.v4.graphics.ColorUtils.TEMP_ARRAY.set(result);
        }
        return result;
    }
}

