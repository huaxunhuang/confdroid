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
 * {@usesMathJax }
 *
 * <p>A {@link ColorSpace} is used to identify a specific organization of colors.
 * Each color space is characterized by a {@link Model color model} that defines
 * how a color value is represented (for instance the {@link Model#RGB RGB} color
 * model defines a color value as a triplet of numbers).</p>
 *
 * <p>Each component of a color must fall within a valid range, specific to each
 * color space, defined by {@link #getMinValue(int)} and {@link #getMaxValue(int)}
 * This range is commonly \([0..1]\). While it is recommended to use values in the
 * valid range, a color space always clamps input and output values when performing
 * operations such as converting to a different color space.</p>
 *
 * <h3>Using color spaces</h3>
 *
 * <p>This implementation provides a pre-defined set of common color spaces
 * described in the {@link Named} enum. To obtain an instance of one of the
 * pre-defined color spaces, simply invoke {@link #get(Named)}:</p>
 *
 * <pre class="prettyprint">
 * ColorSpace sRgb = ColorSpace.get(ColorSpace.Named.SRGB);
 * </pre>
 *
 * <p>The {@link #get(Named)} method always returns the same instance for a given
 * name. Color spaces with an {@link Model#RGB RGB} color model can be safely
 * cast to {@link Rgb}. Doing so gives you access to more APIs to query various
 * properties of RGB color models: color gamut primaries, transfer functions,
 * conversions to and from linear space, etc. Please refer to {@link Rgb} for
 * more information.</p>
 *
 * <p>The documentation of {@link Named} provides a detailed description of the
 * various characteristics of each available color space.</p>
 *
 * <h3>Color space conversions</h3>
 *
 * <p>To allow conversion between color spaces, this implementation uses the CIE
 * XYZ profile connection space (PCS). Color values can be converted to and from
 * this PCS using {@link #toXyz(float[])} and {@link #fromXyz(float[])}.</p>
 *
 * <p>For color space with a non-RGB color model, the white point of the PCS
 * <em>must be</em> the CIE standard illuminant D50. RGB color spaces use their
 * native white point (D65 for {@link Named#SRGB sRGB} for instance and must
 * undergo {@link Adaptation chromatic adaptation} as necessary.</p>
 *
 * <p>Since the white point of the PCS is not defined for RGB color space, it is
 * highly recommended to use the variants of the {@link #connect(ColorSpace, ColorSpace)}
 * method to perform conversions between color spaces. A color space can be
 * manually adapted to a specific white point using {@link #adapt(ColorSpace, float[])}.
 * Please refer to the documentation of {@link Rgb RGB color spaces} for more
 * information. Several common CIE standard illuminants are provided in this
 * class as reference (see {@link #ILLUMINANT_D65} or {@link #ILLUMINANT_D50}
 * for instance).</p>
 *
 * <p>Here is an example of how to convert from a color space to another:</p>
 *
 * <pre class="prettyprint">
 * // Convert from DCI-P3 to Rec.2020
 * ColorSpace.Connector connector = ColorSpace.connect(
 *         ColorSpace.get(ColorSpace.Named.DCI_P3),
 *         ColorSpace.get(ColorSpace.Named.BT2020));
 *
 * float[] bt2020 = connector.transform(p3r, p3g, p3b);
 * </pre>
 *
 * <p>You can easily convert to {@link Named#SRGB sRGB} by omitting the second
 * parameter:</p>
 *
 * <pre class="prettyprint">
 * // Convert from DCI-P3 to sRGB
 * ColorSpace.Connector connector = ColorSpace.connect(ColorSpace.get(ColorSpace.Named.DCI_P3));
 *
 * float[] sRGB = connector.transform(p3r, p3g, p3b);
 * </pre>
 *
 * <p>Conversions also work between color spaces with different color models:</p>
 *
 * <pre class="prettyprint">
 * // Convert from CIE L*a*b* (color model Lab) to Rec.709 (color model RGB)
 * ColorSpace.Connector connector = ColorSpace.connect(
 *         ColorSpace.get(ColorSpace.Named.CIE_LAB),
 *         ColorSpace.get(ColorSpace.Named.BT709));
 * </pre>
 *
 * <h3>Color spaces and multi-threading</h3>
 *
 * <p>Color spaces and other related classes ({@link Connector} for instance)
 * are immutable and stateless. They can be safely used from multiple concurrent
 * threads.</p>
 *
 * <p>Public static methods provided by this class, such as {@link #get(Named)}
 * and {@link #connect(ColorSpace, ColorSpace)}, are also guaranteed to be
 * thread-safe.</p>
 *
 * @see #get(Named)
 * @see Named
 * @see Model
 * @see Connector
 * @see Adaptation
 */
@android.annotation.AnyThread
@java.lang.SuppressWarnings("StaticInitializerReferencesSubClass")
@android.annotation.SuppressAutoDoc
public abstract class ColorSpace {
    /**
     * Standard CIE 1931 2° illuminant A, encoded in xyY.
     * This illuminant has a color temperature of 2856K.
     */
    public static final float[] ILLUMINANT_A = new float[]{ 0.44757F, 0.40745F };

    /**
     * Standard CIE 1931 2° illuminant B, encoded in xyY.
     * This illuminant has a color temperature of 4874K.
     */
    public static final float[] ILLUMINANT_B = new float[]{ 0.34842F, 0.35161F };

    /**
     * Standard CIE 1931 2° illuminant C, encoded in xyY.
     * This illuminant has a color temperature of 6774K.
     */
    public static final float[] ILLUMINANT_C = new float[]{ 0.31006F, 0.31616F };

    /**
     * Standard CIE 1931 2° illuminant D50, encoded in xyY.
     * This illuminant has a color temperature of 5003K. This illuminant
     * is used by the profile connection space in ICC profiles.
     */
    public static final float[] ILLUMINANT_D50 = new float[]{ 0.34567F, 0.3585F };

    /**
     * Standard CIE 1931 2° illuminant D55, encoded in xyY.
     * This illuminant has a color temperature of 5503K.
     */
    public static final float[] ILLUMINANT_D55 = new float[]{ 0.33242F, 0.34743F };

    /**
     * Standard CIE 1931 2° illuminant D60, encoded in xyY.
     * This illuminant has a color temperature of 6004K.
     */
    public static final float[] ILLUMINANT_D60 = new float[]{ 0.32168F, 0.33767F };

    /**
     * Standard CIE 1931 2° illuminant D65, encoded in xyY.
     * This illuminant has a color temperature of 6504K. This illuminant
     * is commonly used in RGB color spaces such as sRGB, BT.209, etc.
     */
    public static final float[] ILLUMINANT_D65 = new float[]{ 0.31271F, 0.32902F };

    /**
     * Standard CIE 1931 2° illuminant D75, encoded in xyY.
     * This illuminant has a color temperature of 7504K.
     */
    public static final float[] ILLUMINANT_D75 = new float[]{ 0.29902F, 0.31485F };

    /**
     * Standard CIE 1931 2° illuminant E, encoded in xyY.
     * This illuminant has a color temperature of 5454K.
     */
    public static final float[] ILLUMINANT_E = new float[]{ 0.33333F, 0.33333F };

    /**
     * The minimum ID value a color space can have.
     *
     * @see #getId()
     */
    public static final int MIN_ID = -1;// Do not change


    /**
     * The maximum ID value a color space can have.
     *
     * @see #getId()
     */
    public static final int MAX_ID = 63;// Do not change, used to encode in longs


    private static final float[] SRGB_PRIMARIES = new float[]{ 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F };

    private static final float[] NTSC_1953_PRIMARIES = new float[]{ 0.67F, 0.33F, 0.21F, 0.71F, 0.14F, 0.08F };

    private static final float[] ILLUMINANT_D50_XYZ = new float[]{ 0.964212F, 1.0F, 0.825188F };

    private static final android.graphics.ColorSpace.Rgb.TransferParameters SRGB_TRANSFER_PARAMETERS = new android.graphics.ColorSpace.Rgb.TransferParameters(1 / 1.055, 0.055 / 1.055, 1 / 12.92, 0.04045, 2.4);

    // See static initialization block next to #get(Named)
    private static final android.graphics.ColorSpace[] sNamedColorSpaces = new android.graphics.ColorSpace[android.graphics.ColorSpace.Named.values().length];

    @android.annotation.NonNull
    private final java.lang.String mName;

    @android.annotation.NonNull
    private final android.graphics.ColorSpace.Model mModel;

    @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
    private final int mId;

    /**
     * {@usesMathJax }
     *
     * <p>List of common, named color spaces. A corresponding instance of
     * {@link ColorSpace} can be obtained by calling {@link ColorSpace#get(Named)}:</p>
     *
     * <pre class="prettyprint">
     * ColorSpace cs = ColorSpace.get(ColorSpace.Named.DCI_P3);
     * </pre>
     *
     * <p>The properties of each color space are described below (see {@link #SRGB sRGB}
     * for instance). When applicable, the color gamut of each color space is compared
     * to the color gamut of sRGB using a CIE 1931 xy chromaticity diagram. This diagram
     * shows the location of the color space's primaries and white point.</p>
     *
     * @see ColorSpace#get(Named)
     */
    // Update the initialization block next to #get(Named) when adding new values
    public enum Named {

        // NOTE: Do NOT change the order of the enum
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space sRGB standardized as IEC 61966-2.1:1999.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.640</td><td>0.300</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.330</td><td>0.600</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">sRGB IEC61966-2.1</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{sRGB} = \begin{cases} 12.92 \times C_{linear} & C_{linear} \lt 0.0031308 \\\
         *             1.055 \times C_{linear}^{\frac{1}{2.4}} - 0.055 & C_{linear} \ge 0.0031308 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{sRGB}}{12.92} & C_{sRGB} \lt 0.04045 \\\
         *             \left( \frac{C_{sRGB} + 0.055}{1.055} \right) ^{2.4} & C_{sRGB} \ge 0.04045 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_srgb.png" />
         *     <figcaption style="text-align: center;">sRGB</figcaption>
         * </p>
         */
        SRGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space sRGB standardized as IEC 61966-2.1:1999.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.640</td><td>0.300</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.330</td><td>0.600</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">sRGB IEC61966-2.1 (Linear)</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{sRGB} = C_{linear}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{sRGB}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_srgb.png" />
         *     <figcaption style="text-align: center;">sRGB</figcaption>
         * </p>
         */
        LINEAR_SRGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space scRGB-nl standardized as IEC 61966-2-2:2003.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.640</td><td>0.300</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.330</td><td>0.600</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">scRGB-nl IEC 61966-2-2:2003</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{scRGB} = \begin{cases} sign(C_{linear}) 12.92 \times \left| C_{linear} \right| &
         *                      \left| C_{linear} \right| \lt 0.0031308 \\\
         *             sign(C_{linear}) 1.055 \times \left| C_{linear} \right| ^{\frac{1}{2.4}} - 0.055 &
         *                      \left| C_{linear} \right| \ge 0.0031308 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}sign(C_{scRGB}) \frac{\left| C_{scRGB} \right|}{12.92} &
         *                  \left| C_{scRGB} \right| \lt 0.04045 \\\
         *             sign(C_{scRGB}) \left( \frac{\left| C_{scRGB} \right| + 0.055}{1.055} \right) ^{2.4} &
         *                  \left| C_{scRGB} \right| \ge 0.04045 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([-0.799..2.399[\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_scrgb.png" />
         *     <figcaption style="text-align: center;">Extended sRGB (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        EXTENDED_SRGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space scRGB standardized as IEC 61966-2-2:2003.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.640</td><td>0.300</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.330</td><td>0.600</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">scRGB IEC 61966-2-2:2003</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{scRGB} = C_{linear}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{scRGB}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([-0.5..7.499[\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_scrgb.png" />
         *     <figcaption style="text-align: center;">Extended sRGB (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        LINEAR_EXTENDED_SRGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space BT.709 standardized as Rec. ITU-R BT.709-5.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.640</td><td>0.300</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.330</td><td>0.600</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Rec. ITU-R BT.709-5</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{BT709} = \begin{cases} 4.5 \times C_{linear} & C_{linear} \lt 0.018 \\\
         *             1.099 \times C_{linear}^{\frac{1}{2.2}} - 0.099 & C_{linear} \ge 0.018 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{BT709}}{4.5} & C_{BT709} \lt 0.081 \\\
         *             \left( \frac{C_{BT709} + 0.099}{1.099} \right) ^{2.2} & C_{BT709} \ge 0.081 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_bt709.png" />
         *     <figcaption style="text-align: center;">BT.709</figcaption>
         * </p>
         */
        BT709,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space BT.2020 standardized as Rec. ITU-R BT.2020-1.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.708</td><td>0.170</td><td>0.131</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.292</td><td>0.797</td><td>0.046</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Rec. ITU-R BT.2020-1</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{BT2020} = \begin{cases} 4.5 \times C_{linear} & C_{linear} \lt 0.0181 \\\
         *             1.0993 \times C_{linear}^{\frac{1}{2.2}} - 0.0993 & C_{linear} \ge 0.0181 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{BT2020}}{4.5} & C_{BT2020} \lt 0.08145 \\\
         *             \left( \frac{C_{BT2020} + 0.0993}{1.0993} \right) ^{2.2} & C_{BT2020} \ge 0.08145 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_bt2020.png" />
         *     <figcaption style="text-align: center;">BT.2020 (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        BT2020,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space DCI-P3 standardized as SMPTE RP 431-2-2007.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.680</td><td>0.265</td><td>0.150</td><td>0.314</td></tr>
         *     <tr><td>y</td><td>0.320</td><td>0.690</td><td>0.060</td><td>0.351</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">SMPTE RP 431-2-2007 DCI (P3)</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">N/A</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{P3} = C_{linear}^{\frac{1}{2.6}}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{P3}^{2.6}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_dci_p3.png" />
         *     <figcaption style="text-align: center;">DCI-P3 (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        DCI_P3,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space Display P3 based on SMPTE RP 431-2-2007 and IEC 61966-2.1:1999.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.680</td><td>0.265</td><td>0.150</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.320</td><td>0.690</td><td>0.060</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Display P3</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{DisplayP3} = \begin{cases} 12.92 \times C_{linear} & C_{linear} \lt 0.0030186 \\\
         *             1.055 \times C_{linear}^{\frac{1}{2.4}} - 0.055 & C_{linear} \ge 0.0030186 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{DisplayP3}}{12.92} & C_{sRGB} \lt 0.04045 \\\
         *             \left( \frac{C_{DisplayP3} + 0.055}{1.055} \right) ^{2.4} & C_{sRGB} \ge 0.04045 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_display_p3.png" />
         *     <figcaption style="text-align: center;">Display P3 (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        DISPLAY_P3,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space NTSC, 1953 standard.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.67</td><td>0.21</td><td>0.14</td><td>0.310</td></tr>
         *     <tr><td>y</td><td>0.33</td><td>0.71</td><td>0.08</td><td>0.316</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">NTSC (1953)</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">C</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{BT709} = \begin{cases} 4.5 \times C_{linear} & C_{linear} \lt 0.018 \\\
         *             1.099 \times C_{linear}^{\frac{1}{2.2}} - 0.099 & C_{linear} \ge 0.018 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{BT709}}{4.5} & C_{BT709} \lt 0.081 \\\
         *             \left( \frac{C_{BT709} + 0.099}{1.099} \right) ^{2.2} & C_{BT709} \ge 0.081 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_ntsc_1953.png" />
         *     <figcaption style="text-align: center;">NTSC 1953 (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        NTSC_1953,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space SMPTE C.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.630</td><td>0.310</td><td>0.155</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.340</td><td>0.595</td><td>0.070</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">SMPTE-C RGB</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{BT709} = \begin{cases} 4.5 \times C_{linear} & C_{linear} \lt 0.018 \\\
         *             1.099 \times C_{linear}^{\frac{1}{2.2}} - 0.099 & C_{linear} \ge 0.018 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{BT709}}{4.5} & C_{BT709} \lt 0.081 \\\
         *             \left( \frac{C_{BT709} + 0.099}{1.099} \right) ^{2.2} & C_{BT709} \ge 0.081 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_smpte_c.png" />
         *     <figcaption style="text-align: center;">SMPTE-C (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        SMPTE_C,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space Adobe RGB (1998).</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.64</td><td>0.21</td><td>0.15</td><td>0.3127</td></tr>
         *     <tr><td>y</td><td>0.33</td><td>0.71</td><td>0.06</td><td>0.3290</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Adobe RGB (1998)</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D65</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{RGB} = C_{linear}^{\frac{1}{2.2}}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{RGB}^{2.2}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_adobe_rgb.png" />
         *     <figcaption style="text-align: center;">Adobe RGB (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        ADOBE_RGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space ProPhoto RGB standardized as ROMM RGB ISO 22028-2:2013.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.7347</td><td>0.1596</td><td>0.0366</td><td>0.3457</td></tr>
         *     <tr><td>y</td><td>0.2653</td><td>0.8404</td><td>0.0001</td><td>0.3585</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">ROMM RGB ISO 22028-2:2013</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D50</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{ROMM} = \begin{cases} 16 \times C_{linear} & C_{linear} \lt 0.001953 \\\
         *             C_{linear}^{\frac{1}{1.8}} & C_{linear} \ge 0.001953 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(\begin{equation}
         *             C_{linear} = \begin{cases}\frac{C_{ROMM}}{16} & C_{ROMM} \lt 0.031248 \\\
         *             C_{ROMM}^{1.8} & C_{ROMM} \ge 0.031248 \end{cases}
         *             \end{equation}\)
         *         </td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([0..1]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_pro_photo_rgb.png" />
         *     <figcaption style="text-align: center;">ProPhoto RGB (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        PRO_PHOTO_RGB,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space ACES standardized as SMPTE ST 2065-1:2012.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.73470</td><td>0.00000</td><td>0.00010</td><td>0.32168</td></tr>
         *     <tr><td>y</td><td>0.26530</td><td>1.00000</td><td>-0.07700</td><td>0.33767</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">SMPTE ST 2065-1:2012 ACES</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D60</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{ACES} = C_{linear}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{ACES}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([-65504.0, 65504.0]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_aces.png" />
         *     <figcaption style="text-align: center;">ACES (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        ACES,
        /**
         * <p>{@link ColorSpace.Rgb RGB} color space ACEScg standardized as Academy S-2014-004.</p>
         * <table summary="Color space definition">
         *     <tr>
         *         <th>Chromaticity</th><th>Red</th><th>Green</th><th>Blue</th><th>White point</th>
         *     </tr>
         *     <tr><td>x</td><td>0.713</td><td>0.165</td><td>0.128</td><td>0.32168</td></tr>
         *     <tr><td>y</td><td>0.293</td><td>0.830</td><td>0.044</td><td>0.33767</td></tr>
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Academy S-2014-004 ACEScg</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D60</td></tr>
         *     <tr>
         *         <td>Opto-electronic transfer function (OETF)</td>
         *         <td colspan="4">\(C_{ACEScg} = C_{linear}\)</td>
         *     </tr>
         *     <tr>
         *         <td>Electro-optical transfer function (EOTF)</td>
         *         <td colspan="4">\(C_{linear} = C_{ACEScg}\)</td>
         *     </tr>
         *     <tr><td>Range</td><td colspan="4">\([-65504.0, 65504.0]\)</td></tr>
         * </table>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_acescg.png" />
         *     <figcaption style="text-align: center;">ACEScg (orange) vs sRGB (white)</figcaption>
         * </p>
         */
        ACESCG,
        /**
         * <p>{@link Model#XYZ XYZ} color space CIE XYZ. This color space assumes standard
         * illuminant D50 as its white point.</p>
         * <table summary="Color space definition">
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Generic XYZ</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D50</td></tr>
         *     <tr><td>Range</td><td colspan="4">\([-2.0, 2.0]\)</td></tr>
         * </table>
         */
        CIE_XYZ,
        /**
         * <p>{@link Model#LAB Lab} color space CIE L*a*b*. This color space uses CIE XYZ D50
         * as a profile conversion space.</p>
         * <table summary="Color space definition">
         *     <tr><th>Property</th><th colspan="4">Value</th></tr>
         *     <tr><td>Name</td><td colspan="4">Generic L*a*b*</td></tr>
         *     <tr><td>CIE standard illuminant</td><td colspan="4">D50</td></tr>
         *     <tr><td>Range</td><td colspan="4">\(L: [0.0, 100.0], a: [-128, 128], b: [-128, 128]\)</td></tr>
         * </table>
         */
        CIE_LAB;}

    /**
     * <p>A render intent determines how a {@link ColorSpace.Connector connector}
     * maps colors from one color space to another. The choice of mapping is
     * important when the source color space has a larger color gamut than the
     * destination color space.</p>
     *
     * @see ColorSpace#connect(ColorSpace, ColorSpace, RenderIntent)
     */
    public enum RenderIntent {

        /**
         * <p>Compresses the source gamut into the destination gamut.
         * This render intent affects all colors, inside and outside
         * of destination gamut. The goal of this render intent is
         * to preserve the visual relationship between colors.</p>
         *
         * <p class="note">This render intent is currently not
         * implemented and behaves like {@link #RELATIVE}.</p>
         */
        PERCEPTUAL,
        /**
         * Similar to the {@link #ABSOLUTE} render intent, this render
         * intent matches the closest color in the destination gamut
         * but makes adjustments for the destination white point.
         */
        RELATIVE,
        /**
         * <p>Attempts to maintain the relative saturation of colors
         * from the source gamut to the destination gamut, to keep
         * highly saturated colors as saturated as possible.</p>
         *
         * <p class="note">This render intent is currently not
         * implemented and behaves like {@link #RELATIVE}.</p>
         */
        SATURATION,
        /**
         * Colors that are in the destination gamut are left unchanged.
         * Colors that fall outside of the destination gamut are mapped
         * to the closest possible color within the gamut of the destination
         * color space (they are clipped).
         */
        ABSOLUTE;}

    /**
     * {@usesMathJax }
     *
     * <p>List of adaptation matrices that can be used for chromatic adaptation
     * using the von Kries transform. These matrices are used to convert values
     * in the CIE XYZ space to values in the LMS space (Long Medium Short).</p>
     *
     * <p>Given an adaptation matrix \(A\), the conversion from XYZ to
     * LMS is straightforward:</p>
     *
     * $$\left[ \begin{array}{c} L\\ M\\ S \end{array} \right] =
     * A \left[ \begin{array}{c} X\\ Y\\ Z \end{array} \right]$$
     *
     * <p>The complete von Kries transform \(T\) uses a diagonal matrix
     * noted \(D\) to perform the adaptation in LMS space. In addition
     * to \(A\) and \(D\), the source white point \(W1\) and the destination
     * white point \(W2\) must be specified:</p>
     *
     * $$\begin{align*}
     * \left[ \begin{array}{c} L_1\\ M_1\\ S_1 \end{array} \right] &=
     *      A \left[ \begin{array}{c} W1_X\\ W1_Y\\ W1_Z \end{array} \right] \\\
     * \left[ \begin{array}{c} L_2\\ M_2\\ S_2 \end{array} \right] &=
     *      A \left[ \begin{array}{c} W2_X\\ W2_Y\\ W2_Z \end{array} \right] \\\
     * D &= \left[ \begin{matrix} \frac{L_2}{L_1} & 0 & 0 \\\
     *      0 & \frac{M_2}{M_1} & 0 \\\
     *      0 & 0 & \frac{S_2}{S_1} \end{matrix} \right] \\\
     * T &= A^{-1}.D.A
     * \end{align*}$$
     *
     * <p>As an example, the resulting matrix \(T\) can then be used to
     * perform the chromatic adaptation of sRGB XYZ transform from D65
     * to D50:</p>
     *
     * $$sRGB_{D50} = T.sRGB_{D65}$$
     *
     * @see ColorSpace.Connector
     * @see ColorSpace#connect(ColorSpace, ColorSpace)
     */
    public enum Adaptation {

        /**
         * Bradford chromatic adaptation transform, as defined in the
         * CIECAM97s color appearance model.
         */
        BRADFORD(new float[]{ 0.8951F, -0.7502F, 0.0389F, 0.2664F, 1.7135F, -0.0685F, -0.1614F, 0.0367F, 1.0296F }),
        /**
         * von Kries chromatic adaptation transform.
         */
        VON_KRIES(new float[]{ 0.40024F, -0.2263F, 0.0F, 0.7076F, 1.16532F, 0.0F, -0.08081F, 0.0457F, 0.91822F }),
        /**
         * CIECAT02 chromatic adaption transform, as defined in the
         * CIECAM02 color appearance model.
         */
        CIECAT02(new float[]{ 0.7328F, -0.7036F, 0.003F, 0.4296F, 1.6975F, 0.0136F, -0.1624F, 0.0061F, 0.9834F });
        final float[] mTransform;

        Adaptation(@android.annotation.NonNull
        @android.annotation.Size(9)
        float[] transform) {
            mTransform = transform;
        }
    }

    /**
     * A color model is required by a {@link ColorSpace} to describe the
     * way colors can be represented as tuples of numbers. A common color
     * model is the {@link #RGB RGB} color model which defines a color
     * as represented by a tuple of 3 numbers (red, green and blue).
     */
    public enum Model {

        /**
         * The RGB model is a color model with 3 components that
         * refer to the three additive primiaries: red, green
         * andd blue.
         */
        RGB(3),
        /**
         * The XYZ model is a color model with 3 components that
         * are used to model human color vision on a basic sensory
         * level.
         */
        XYZ(3),
        /**
         * The Lab model is a color model with 3 components used
         * to describe a color space that is more perceptually
         * uniform than XYZ.
         */
        LAB(3),
        /**
         * The CMYK model is a color model with 4 components that
         * refer to four inks used in color printing: cyan, magenta,
         * yellow and black (or key). CMYK is a subtractive color
         * model.
         */
        CMYK(4);
        private final int mComponentCount;

        Model(@android.annotation.IntRange(from = 1, to = 4)
        int componentCount) {
            mComponentCount = componentCount;
        }

        /**
         * Returns the number of components for this color model.
         *
         * @return An integer between 1 and 4
         */
        @android.annotation.IntRange(from = 1, to = 4)
        public int getComponentCount() {
            return mComponentCount;
        }
    }

    private ColorSpace(@android.annotation.NonNull
    java.lang.String name, @android.annotation.NonNull
    android.graphics.ColorSpace.Model model, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
    int id) {
        if ((name == null) || (name.length() < 1)) {
            throw new java.lang.IllegalArgumentException("The name of a color space cannot be null and " + "must contain at least 1 character");
        }
        if (model == null) {
            throw new java.lang.IllegalArgumentException("A color space must have a model");
        }
        if ((id < android.graphics.ColorSpace.MIN_ID) || (id > android.graphics.ColorSpace.MAX_ID)) {
            throw new java.lang.IllegalArgumentException((("The id must be between " + android.graphics.ColorSpace.MIN_ID) + " and ") + android.graphics.ColorSpace.MAX_ID);
        }
        mName = name;
        mModel = model;
        mId = id;
    }

    /**
     * <p>Returns the name of this color space. The name is never null
     * and contains always at least 1 character.</p>
     *
     * <p>Color space names are recommended to be unique but are not
     * guaranteed to be. There is no defined format but the name usually
     * falls in one of the following categories:</p>
     * <ul>
     *     <li>Generic names used to identify color spaces in non-RGB
     *     color models. For instance: {@link Named#CIE_LAB Generic L*a*b*}.</li>
     *     <li>Names tied to a particular specification. For instance:
     *     {@link Named#SRGB sRGB IEC61966-2.1} or
     *     {@link Named#ACES SMPTE ST 2065-1:2012 ACES}.</li>
     *     <li>Ad-hoc names, often generated procedurally or by the user
     *     during a calibration workflow. These names often contain the
     *     make and model of the display.</li>
     * </ul>
     *
     * <p>Because the format of color space names is not defined, it is
     * not recommended to programmatically identify a color space by its
     * name alone. Names can be used as a first approximation.</p>
     *
     * <p>It is however perfectly acceptable to display color space names to
     * users in a UI, or in debuggers and logs. When displaying a color space
     * name to the user, it is recommended to add extra information to avoid
     * ambiguities: color model, a representation of the color space's gamut,
     * white point, etc.</p>
     *
     * @return A non-null String of length >= 1
     */
    @android.annotation.NonNull
    public java.lang.String getName() {
        return mName;
    }

    /**
     * Returns the ID of this color space. Positive IDs match the color
     * spaces enumerated in {@link Named}. A negative ID indicates a
     * color space created by calling one of the public constructors.
     *
     * @return An integer between {@link #MIN_ID} and {@link #MAX_ID}
     */
    @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
    public int getId() {
        return mId;
    }

    /**
     * Return the color model of this color space.
     *
     * @return A non-null {@link Model}
     * @see Model
     * @see #getComponentCount()
     */
    @android.annotation.NonNull
    public android.graphics.ColorSpace.Model getModel() {
        return mModel;
    }

    /**
     * Returns the number of components that form a color value according
     * to this color space's color model.
     *
     * @return An integer between 1 and 4
     * @see Model
     * @see #getModel()
     */
    @android.annotation.IntRange(from = 1, to = 4)
    public int getComponentCount() {
        return mModel.getComponentCount();
    }

    /**
     * Returns whether this color space is a wide-gamut color space.
     * An RGB color space is wide-gamut if its gamut entirely contains
     * the {@link Named#SRGB sRGB} gamut and if the area of its gamut is
     * 90% of greater than the area of the {@link Named#NTSC_1953 NTSC}
     * gamut.
     *
     * @return True if this color space is a wide-gamut color space,
    false otherwise
     */
    public abstract boolean isWideGamut();

    /**
     * <p>Indicates whether this color space is the sRGB color space or
     * equivalent to the sRGB color space.</p>
     * <p>A color space is considered sRGB if it meets all the following
     * conditions:</p>
     * <ul>
     *     <li>Its color model is {@link Model#RGB}.</li>
     *     <li>
     *         Its primaries are within 1e-3 of the true
     *         {@link Named#SRGB sRGB} primaries.
     *     </li>
     *     <li>
     *         Its white point is within 1e-3 of the CIE standard
     *         illuminant {@link #ILLUMINANT_D65 D65}.
     *     </li>
     *     <li>Its opto-electronic transfer function is not linear.</li>
     *     <li>Its electro-optical transfer function is not linear.</li>
     *     <li>Its transfer functions yield values within 1e-3 of {@link Named#SRGB}.</li>
     *     <li>Its range is \([0..1]\).</li>
     * </ul>
     * <p>This method always returns true for {@link Named#SRGB}.</p>
     *
     * @return True if this color space is the sRGB color space (or a
    close approximation), false otherwise
     */
    public boolean isSrgb() {
        return false;
    }

    /**
     * Returns the minimum valid value for the specified component of this
     * color space's color model.
     *
     * @param component
     * 		The index of the component
     * @return A floating point value less than {@link #getMaxValue(int)}
     * @see #getMaxValue(int)
     * @see Model#getComponentCount()
     */
    public abstract float getMinValue(@android.annotation.IntRange(from = 0, to = 3)
    int component);

    /**
     * Returns the maximum valid value for the specified component of this
     * color space's color model.
     *
     * @param component
     * 		The index of the component
     * @return A floating point value greater than {@link #getMinValue(int)}
     * @see #getMinValue(int)
     * @see Model#getComponentCount()
     */
    public abstract float getMaxValue(@android.annotation.IntRange(from = 0, to = 3)
    int component);

    /**
     * <p>Converts a color value from this color space's model to
     * tristimulus CIE XYZ values. If the color model of this color
     * space is not {@link Model#RGB RGB}, it is assumed that the
     * target CIE XYZ space uses a {@link #ILLUMINANT_D50 D50}
     * standard illuminant.</p>
     *
     * <p>This method is a convenience for color spaces with a model
     * of 3 components ({@link Model#RGB RGB} or {@link Model#LAB}
     * for instance). With color spaces using fewer or more components,
     * use {@link #toXyz(float[])} instead</p>.
     *
     * @param r
     * 		The first component of the value to convert from (typically R in RGB)
     * @param g
     * 		The second component of the value to convert from (typically G in RGB)
     * @param b
     * 		The third component of the value to convert from (typically B in RGB)
     * @return A new array of 3 floats, containing tristimulus XYZ values
     * @see #toXyz(float[])
     * @see #fromXyz(float, float, float)
     */
    @android.annotation.NonNull
    @android.annotation.Size(3)
    public float[] toXyz(float r, float g, float b) {
        return toXyz(new float[]{ r, g, b });
    }

    /**
     * <p>Converts a color value from this color space's model to
     * tristimulus CIE XYZ values. If the color model of this color
     * space is not {@link Model#RGB RGB}, it is assumed that the
     * target CIE XYZ space uses a {@link #ILLUMINANT_D50 D50}
     * standard illuminant.</p>
     *
     * <p class="note">The specified array's length  must be at least
     * equal to to the number of color components as returned by
     * {@link Model#getComponentCount()}.</p>
     *
     * @param v
     * 		An array of color components containing the color space's
     * 		color value to convert to XYZ, and large enough to hold
     * 		the resulting tristimulus XYZ values
     * @return The array passed in parameter
     * @see #toXyz(float, float, float)
     * @see #fromXyz(float[])
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 3)
    public abstract float[] toXyz(@android.annotation.NonNull
    @android.annotation.Size(min = 3)
    float[] v);

    /**
     * <p>Converts tristimulus values from the CIE XYZ space to this
     * color space's color model.</p>
     *
     * @param x
     * 		The X component of the color value
     * @param y
     * 		The Y component of the color value
     * @param z
     * 		The Z component of the color value
     * @return A new array whose size is equal to the number of color
    components as returned by {@link Model#getComponentCount()}
     * @see #fromXyz(float[])
     * @see #toXyz(float, float, float)
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 3)
    public float[] fromXyz(float x, float y, float z) {
        float[] xyz = new float[mModel.getComponentCount()];
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        return fromXyz(xyz);
    }

    /**
     * <p>Converts tristimulus values from the CIE XYZ space to this color
     * space's color model. The resulting value is passed back in the specified
     * array.</p>
     *
     * <p class="note">The specified array's length  must be at least equal to
     * to the number of color components as returned by
     * {@link Model#getComponentCount()}, and its first 3 values must
     * be the XYZ components to convert from.</p>
     *
     * @param v
     * 		An array of color components containing the XYZ values
     * 		to convert from, and large enough to hold the number
     * 		of components of this color space's model
     * @return The array passed in parameter
     * @see #fromXyz(float, float, float)
     * @see #toXyz(float[])
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 3)
    public abstract float[] fromXyz(@android.annotation.NonNull
    @android.annotation.Size(min = 3)
    float[] v);

    /**
     * <p>Returns a string representation of the object. This method returns
     * a string equal to the value of:</p>
     *
     * <pre class="prettyprint">
     * getName() + "(id=" + getId() + ", model=" + getModel() + ")"
     * </pre>
     *
     * <p>For instance, the string representation of the {@link Named#SRGB sRGB}
     * color space is equal to the following value:</p>
     *
     * <pre>
     * sRGB IEC61966-2.1 (id=0, model=RGB)
     * </pre>
     *
     * @return A string representation of the object
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String toString() {
        return ((((mName + " (id=") + mId) + ", model=") + mModel) + ")";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.ColorSpace that = ((android.graphics.ColorSpace) (o));
        if (mId != that.mId)
            return false;

        // noinspection SimplifiableIfStatement
        if (!mName.equals(that.mName))
            return false;

        return mModel == that.mModel;
    }

    @java.lang.Override
    public int hashCode() {
        int result = mName.hashCode();
        result = (31 * result) + mModel.hashCode();
        result = (31 * result) + mId;
        return result;
    }

    /**
     * <p>Connects two color spaces to allow conversion from the source color
     * space to the destination color space. If the source and destination
     * color spaces do not have the same profile connection space (CIE XYZ
     * with the same white point), they are chromatically adapted to use the
     * CIE standard illuminant {@link #ILLUMINANT_D50 D50} as needed.</p>
     *
     * <p>If the source and destination are the same, an optimized connector
     * is returned to avoid unnecessary computations and loss of precision.</p>
     *
     * <p>Colors are mapped from the source color space to the destination color
     * space using the {@link RenderIntent#PERCEPTUAL perceptual} render intent.</p>
     *
     * @param source
     * 		The color space to convert colors from
     * @param destination
     * 		The color space to convert colors to
     * @return A non-null connector between the two specified color spaces
     * @see #connect(ColorSpace)
     * @see #connect(ColorSpace, RenderIntent)
     * @see #connect(ColorSpace, ColorSpace, RenderIntent)
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace.Connector connect(@android.annotation.NonNull
    android.graphics.ColorSpace source, @android.annotation.NonNull
    android.graphics.ColorSpace destination) {
        return android.graphics.ColorSpace.connect(source, destination, android.graphics.ColorSpace.RenderIntent.PERCEPTUAL);
    }

    /**
     * <p>Connects two color spaces to allow conversion from the source color
     * space to the destination color space. If the source and destination
     * color spaces do not have the same profile connection space (CIE XYZ
     * with the same white point), they are chromatically adapted to use the
     * CIE standard illuminant {@link #ILLUMINANT_D50 D50} as needed.</p>
     *
     * <p>If the source and destination are the same, an optimized connector
     * is returned to avoid unnecessary computations and loss of precision.</p>
     *
     * @param source
     * 		The color space to convert colors from
     * @param destination
     * 		The color space to convert colors to
     * @param intent
     * 		The render intent to map colors from the source to the destination
     * @return A non-null connector between the two specified color spaces
     * @see #connect(ColorSpace)
     * @see #connect(ColorSpace, RenderIntent)
     * @see #connect(ColorSpace, ColorSpace)
     */
    @android.annotation.NonNull
    @java.lang.SuppressWarnings("ConstantConditions")
    public static android.graphics.ColorSpace.Connector connect(@android.annotation.NonNull
    android.graphics.ColorSpace source, @android.annotation.NonNull
    android.graphics.ColorSpace destination, @android.annotation.NonNull
    android.graphics.ColorSpace.RenderIntent intent) {
        if (source.equals(destination))
            return android.graphics.ColorSpace.Connector.identity(source);

        if ((source.getModel() == android.graphics.ColorSpace.Model.RGB) && (destination.getModel() == android.graphics.ColorSpace.Model.RGB)) {
            return new android.graphics.ColorSpace.Connector.Rgb(((android.graphics.ColorSpace.Rgb) (source)), ((android.graphics.ColorSpace.Rgb) (destination)), intent);
        }
        return new android.graphics.ColorSpace.Connector(source, destination, intent);
    }

    /**
     * <p>Connects the specified color spaces to sRGB.
     * If the source color space does not use CIE XYZ D65 as its profile
     * connection space, the two spaces are chromatically adapted to use the
     * CIE standard illuminant {@link #ILLUMINANT_D50 D50} as needed.</p>
     *
     * <p>If the source is the sRGB color space, an optimized connector
     * is returned to avoid unnecessary computations and loss of precision.</p>
     *
     * <p>Colors are mapped from the source color space to the destination color
     * space using the {@link RenderIntent#PERCEPTUAL perceptual} render intent.</p>
     *
     * @param source
     * 		The color space to convert colors from
     * @return A non-null connector between the specified color space and sRGB
     * @see #connect(ColorSpace, RenderIntent)
     * @see #connect(ColorSpace, ColorSpace)
     * @see #connect(ColorSpace, ColorSpace, RenderIntent)
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace.Connector connect(@android.annotation.NonNull
    android.graphics.ColorSpace source) {
        return android.graphics.ColorSpace.connect(source, android.graphics.ColorSpace.RenderIntent.PERCEPTUAL);
    }

    /**
     * <p>Connects the specified color spaces to sRGB.
     * If the source color space does not use CIE XYZ D65 as its profile
     * connection space, the two spaces are chromatically adapted to use the
     * CIE standard illuminant {@link #ILLUMINANT_D50 D50} as needed.</p>
     *
     * <p>If the source is the sRGB color space, an optimized connector
     * is returned to avoid unnecessary computations and loss of precision.</p>
     *
     * @param source
     * 		The color space to convert colors from
     * @param intent
     * 		The render intent to map colors from the source to the destination
     * @return A non-null connector between the specified color space and sRGB
     * @see #connect(ColorSpace)
     * @see #connect(ColorSpace, ColorSpace)
     * @see #connect(ColorSpace, ColorSpace, RenderIntent)
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace.Connector connect(@android.annotation.NonNull
    android.graphics.ColorSpace source, @android.annotation.NonNull
    android.graphics.ColorSpace.RenderIntent intent) {
        if (source.isSrgb())
            return android.graphics.ColorSpace.Connector.identity(source);

        if (source.getModel() == android.graphics.ColorSpace.Model.RGB) {
            return new android.graphics.ColorSpace.Connector.Rgb(((android.graphics.ColorSpace.Rgb) (source)), ((android.graphics.ColorSpace.Rgb) (android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB))), intent);
        }
        return new android.graphics.ColorSpace.Connector(source, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB), intent);
    }

    /**
     * <p>Performs the chromatic adaptation of a color space from its native
     * white point to the specified white point.</p>
     *
     * <p>The chromatic adaptation is performed using the
     * {@link Adaptation#BRADFORD} matrix.</p>
     *
     * <p class="note">The color space returned by this method always has
     * an ID of {@link #MIN_ID}.</p>
     *
     * @param colorSpace
     * 		The color space to chromatically adapt
     * @param whitePoint
     * 		The new white point
     * @return A {@link ColorSpace} instance with the same name, primaries,
    transfer functions and range as the specified color space
     * @see Adaptation
     * @see #adapt(ColorSpace, float[], Adaptation)
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace adapt(@android.annotation.NonNull
    android.graphics.ColorSpace colorSpace, @android.annotation.NonNull
    @android.annotation.Size(min = 2, max = 3)
    float[] whitePoint) {
        return android.graphics.ColorSpace.adapt(colorSpace, whitePoint, android.graphics.ColorSpace.Adaptation.BRADFORD);
    }

    /**
     * <p>Performs the chromatic adaptation of a color space from its native
     * white point to the specified white point. If the specified color space
     * does not have an {@link Model#RGB RGB} color model, or if the color
     * space already has the target white point, the color space is returned
     * unmodified.</p>
     *
     * <p>The chromatic adaptation is performed using the von Kries method
     * described in the documentation of {@link Adaptation}.</p>
     *
     * <p class="note">The color space returned by this method always has
     * an ID of {@link #MIN_ID}.</p>
     *
     * @param colorSpace
     * 		The color space to chromatically adapt
     * @param whitePoint
     * 		The new white point
     * @param adaptation
     * 		The adaptation matrix
     * @return A new color space if the specified color space has an RGB
    model and a white point different from the specified white
    point; the specified color space otherwise
     * @see Adaptation
     * @see #adapt(ColorSpace, float[])
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace adapt(@android.annotation.NonNull
    android.graphics.ColorSpace colorSpace, @android.annotation.NonNull
    @android.annotation.Size(min = 2, max = 3)
    float[] whitePoint, @android.annotation.NonNull
    android.graphics.ColorSpace.Adaptation adaptation) {
        if (colorSpace.getModel() == android.graphics.ColorSpace.Model.RGB) {
            android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (colorSpace));
            if (android.graphics.ColorSpace.compare(rgb.mWhitePoint, whitePoint))
                return colorSpace;

            float[] xyz = (whitePoint.length == 3) ? java.util.Arrays.copyOf(whitePoint, 3) : android.graphics.ColorSpace.xyYToXyz(whitePoint);
            float[] adaptationTransform = android.graphics.ColorSpace.chromaticAdaptation(adaptation.mTransform, android.graphics.ColorSpace.xyYToXyz(rgb.getWhitePoint()), xyz);
            float[] transform = android.graphics.ColorSpace.mul3x3(adaptationTransform, rgb.mTransform);
            return new android.graphics.ColorSpace.Rgb(rgb, transform, whitePoint);
        }
        return colorSpace;
    }

    /**
     * Helper method for creating native SkColorSpace.
     *
     * This essentially calls adapt on a ColorSpace that has not been fully
     * created. It also does not fully create the adapted ColorSpace, but
     * just returns the transform.
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    private static float[] adaptToIlluminantD50(@android.annotation.NonNull
    @android.annotation.Size(2)
    float[] origWhitePoint, @android.annotation.NonNull
    @android.annotation.Size(9)
    float[] origTransform) {
        float[] desired = android.graphics.ColorSpace.ILLUMINANT_D50;
        if (android.graphics.ColorSpace.compare(origWhitePoint, desired))
            return origTransform;

        float[] xyz = android.graphics.ColorSpace.xyYToXyz(desired);
        float[] adaptationTransform = android.graphics.ColorSpace.chromaticAdaptation(android.graphics.ColorSpace.Adaptation.BRADFORD.mTransform, android.graphics.ColorSpace.xyYToXyz(origWhitePoint), xyz);
        return android.graphics.ColorSpace.mul3x3(adaptationTransform, origTransform);
    }

    /**
     * <p>Returns an instance of {@link ColorSpace} whose ID matches the
     * specified ID.</p>
     *
     * <p>This method always returns the same instance for a given ID.</p>
     *
     * <p>This method is thread-safe.</p>
     *
     * @param index
     * 		An integer ID between {@link #MIN_ID} and {@link #MAX_ID}
     * @return A non-null {@link ColorSpace} instance
     * @throws IllegalArgumentException
     * 		If the ID does not match the ID of one of the
     * 		{@link Named named color spaces}
     */
    @android.annotation.NonNull
    static android.graphics.ColorSpace get(@android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
    int index) {
        if ((index < 0) || (index >= android.graphics.ColorSpace.Named.values().length)) {
            throw new java.lang.IllegalArgumentException(("Invalid ID, must be in the range [0.." + android.graphics.ColorSpace.Named.values().length) + ")");
        }
        return android.graphics.ColorSpace.sNamedColorSpaces[index];
    }

    /**
     * <p>Returns an instance of {@link ColorSpace} identified by the specified
     * name. The list of names provided in the {@link Named} enum gives access
     * to a variety of common RGB color spaces.</p>
     *
     * <p>This method always returns the same instance for a given name.</p>
     *
     * <p>This method is thread-safe.</p>
     *
     * @param name
     * 		The name of the color space to get an instance of
     * @return A non-null {@link ColorSpace} instance
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace get(@android.annotation.NonNull
    android.graphics.ColorSpace.Named name) {
        return android.graphics.ColorSpace.sNamedColorSpaces[name.ordinal()];
    }

    /**
     * <p>Returns a {@link Named} instance of {@link ColorSpace} that matches
     * the specified RGB to CIE XYZ transform and transfer functions. If no
     * instance can be found, this method returns null.</p>
     *
     * <p>The color transform matrix is assumed to target the CIE XYZ space
     * a {@link #ILLUMINANT_D50 D50} standard illuminant.</p>
     *
     * @param toXYZD50
     * 		3x3 column-major transform matrix from RGB to the profile
     * 		connection space CIE XYZ as an array of 9 floats, cannot be null
     * @param function
     * 		Parameters for the transfer functions
     * @return A non-null {@link ColorSpace} if a match is found, null otherwise
     */
    @android.annotation.Nullable
    public static android.graphics.ColorSpace match(@android.annotation.NonNull
    @android.annotation.Size(9)
    float[] toXYZD50, @android.annotation.NonNull
    android.graphics.ColorSpace.Rgb.TransferParameters function) {
        for (android.graphics.ColorSpace colorSpace : android.graphics.ColorSpace.sNamedColorSpaces) {
            if (colorSpace.getModel() == android.graphics.ColorSpace.Model.RGB) {
                android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (android.graphics.ColorSpace.adapt(colorSpace, android.graphics.ColorSpace.ILLUMINANT_D50_XYZ)));
                if (android.graphics.ColorSpace.compare(toXYZD50, rgb.mTransform) && android.graphics.ColorSpace.compare(function, rgb.mTransferParameters)) {
                    return colorSpace;
                }
            }
        }
        return null;
    }

    /**
     * <p>Creates a new {@link Renderer} that can be used to visualize and
     * debug color spaces. See the documentation of {@link Renderer} for
     * more information.</p>
     *
     * @return A new non-null {@link Renderer} instance
     * @see Renderer
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace.Renderer createRenderer() {
        return new android.graphics.ColorSpace.Renderer();
    }

    static {
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.SRGB.ordinal()] = new android.graphics.ColorSpace.Rgb("sRGB IEC61966-2.1", android.graphics.ColorSpace.SRGB_PRIMARIES, android.graphics.ColorSpace.ILLUMINANT_D65, android.graphics.ColorSpace.SRGB_TRANSFER_PARAMETERS, android.graphics.ColorSpace.Named.SRGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.LINEAR_SRGB.ordinal()] = new android.graphics.ColorSpace.Rgb("sRGB IEC61966-2.1 (Linear)", android.graphics.ColorSpace.SRGB_PRIMARIES, android.graphics.ColorSpace.ILLUMINANT_D65, 1.0, 0.0F, 1.0F, android.graphics.ColorSpace.Named.LINEAR_SRGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.EXTENDED_SRGB.ordinal()] = new android.graphics.ColorSpace.Rgb("scRGB-nl IEC 61966-2-2:2003", android.graphics.ColorSpace.SRGB_PRIMARIES, android.graphics.ColorSpace.ILLUMINANT_D65, null, ( x) -> android.graphics.ColorSpace.absRcpResponse(x, 1 / 1.055, 0.055 / 1.055, 1 / 12.92, 0.04045, 2.4), ( x) -> android.graphics.ColorSpace.absResponse(x, 1 / 1.055, 0.055 / 1.055, 1 / 12.92, 0.04045, 2.4), -0.799F, 2.399F, android.graphics.ColorSpace.SRGB_TRANSFER_PARAMETERS, android.graphics.ColorSpace.Named.EXTENDED_SRGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB.ordinal()] = new android.graphics.ColorSpace.Rgb("scRGB IEC 61966-2-2:2003", android.graphics.ColorSpace.SRGB_PRIMARIES, android.graphics.ColorSpace.ILLUMINANT_D65, 1.0, -0.5F, 7.499F, android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.BT709.ordinal()] = new android.graphics.ColorSpace.Rgb("Rec. ITU-R BT.709-5", new float[]{ 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F }, android.graphics.ColorSpace.ILLUMINANT_D65, new android.graphics.ColorSpace.Rgb.TransferParameters(1 / 1.099, 0.099 / 1.099, 1 / 4.5, 0.081, 1 / 0.45), android.graphics.ColorSpace.Named.BT709.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.BT2020.ordinal()] = new android.graphics.ColorSpace.Rgb("Rec. ITU-R BT.2020-1", new float[]{ 0.708F, 0.292F, 0.17F, 0.797F, 0.131F, 0.046F }, android.graphics.ColorSpace.ILLUMINANT_D65, new android.graphics.ColorSpace.Rgb.TransferParameters(1 / 1.0993, 0.0993 / 1.0993, 1 / 4.5, 0.08145, 1 / 0.45), android.graphics.ColorSpace.Named.BT2020.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.DCI_P3.ordinal()] = new android.graphics.ColorSpace.Rgb("SMPTE RP 431-2-2007 DCI (P3)", new float[]{ 0.68F, 0.32F, 0.265F, 0.69F, 0.15F, 0.06F }, new float[]{ 0.314F, 0.351F }, 2.6, 0.0F, 1.0F, android.graphics.ColorSpace.Named.DCI_P3.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.DISPLAY_P3.ordinal()] = new android.graphics.ColorSpace.Rgb("Display P3", new float[]{ 0.68F, 0.32F, 0.265F, 0.69F, 0.15F, 0.06F }, android.graphics.ColorSpace.ILLUMINANT_D65, android.graphics.ColorSpace.SRGB_TRANSFER_PARAMETERS, android.graphics.ColorSpace.Named.DISPLAY_P3.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.NTSC_1953.ordinal()] = new android.graphics.ColorSpace.Rgb("NTSC (1953)", android.graphics.ColorSpace.NTSC_1953_PRIMARIES, android.graphics.ColorSpace.ILLUMINANT_C, new android.graphics.ColorSpace.Rgb.TransferParameters(1 / 1.099, 0.099 / 1.099, 1 / 4.5, 0.081, 1 / 0.45), android.graphics.ColorSpace.Named.NTSC_1953.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.SMPTE_C.ordinal()] = new android.graphics.ColorSpace.Rgb("SMPTE-C RGB", new float[]{ 0.63F, 0.34F, 0.31F, 0.595F, 0.155F, 0.07F }, android.graphics.ColorSpace.ILLUMINANT_D65, new android.graphics.ColorSpace.Rgb.TransferParameters(1 / 1.099, 0.099 / 1.099, 1 / 4.5, 0.081, 1 / 0.45), android.graphics.ColorSpace.Named.SMPTE_C.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.ADOBE_RGB.ordinal()] = new android.graphics.ColorSpace.Rgb("Adobe RGB (1998)", new float[]{ 0.64F, 0.33F, 0.21F, 0.71F, 0.15F, 0.06F }, android.graphics.ColorSpace.ILLUMINANT_D65, 2.2, 0.0F, 1.0F, android.graphics.ColorSpace.Named.ADOBE_RGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.PRO_PHOTO_RGB.ordinal()] = new android.graphics.ColorSpace.Rgb("ROMM RGB ISO 22028-2:2013", new float[]{ 0.7347F, 0.2653F, 0.1596F, 0.8404F, 0.0366F, 1.0E-4F }, android.graphics.ColorSpace.ILLUMINANT_D50, new android.graphics.ColorSpace.Rgb.TransferParameters(1.0, 0.0, 1 / 16.0, 0.031248, 1.8), android.graphics.ColorSpace.Named.PRO_PHOTO_RGB.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.ACES.ordinal()] = new android.graphics.ColorSpace.Rgb("SMPTE ST 2065-1:2012 ACES", new float[]{ 0.7347F, 0.2653F, 0.0F, 1.0F, 1.0E-4F, -0.077F }, android.graphics.ColorSpace.ILLUMINANT_D60, 1.0, -65504.0F, 65504.0F, android.graphics.ColorSpace.Named.ACES.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.ACESCG.ordinal()] = new android.graphics.ColorSpace.Rgb("Academy S-2014-004 ACEScg", new float[]{ 0.713F, 0.293F, 0.165F, 0.83F, 0.128F, 0.044F }, android.graphics.ColorSpace.ILLUMINANT_D60, 1.0, -65504.0F, 65504.0F, android.graphics.ColorSpace.Named.ACESCG.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.CIE_XYZ.ordinal()] = new android.graphics.ColorSpace.Xyz("Generic XYZ", android.graphics.ColorSpace.Named.CIE_XYZ.ordinal());
        android.graphics.ColorSpace.sNamedColorSpaces[android.graphics.ColorSpace.Named.CIE_LAB.ordinal()] = new android.graphics.ColorSpace.Lab("Generic L*a*b*", android.graphics.ColorSpace.Named.CIE_LAB.ordinal());
    }

    // Reciprocal piecewise gamma response
    private static double rcpResponse(double x, double a, double b, double c, double d, double g) {
        return x >= (d * c) ? (java.lang.Math.pow(x, 1.0 / g) - b) / a : x / c;
    }

    // Piecewise gamma response
    private static double response(double x, double a, double b, double c, double d, double g) {
        return x >= d ? java.lang.Math.pow((a * x) + b, g) : c * x;
    }

    // Reciprocal piecewise gamma response
    private static double rcpResponse(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= (d * c) ? (java.lang.Math.pow(x - e, 1.0 / g) - b) / a : (x - f) / c;
    }

    // Piecewise gamma response
    private static double response(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= d ? java.lang.Math.pow((a * x) + b, g) + e : (c * x) + f;
    }

    // Reciprocal piecewise gamma response, encoded as sign(x).f(abs(x)) for color
    // spaces that allow negative values
    @java.lang.SuppressWarnings("SameParameterValue")
    private static double absRcpResponse(double x, double a, double b, double c, double d, double g) {
        return java.lang.Math.copySign(android.graphics.ColorSpace.rcpResponse(x < 0.0 ? -x : x, a, b, c, d, g), x);
    }

    // Piecewise gamma response, encoded as sign(x).f(abs(x)) for color spaces that
    // allow negative values
    @java.lang.SuppressWarnings("SameParameterValue")
    private static double absResponse(double x, double a, double b, double c, double d, double g) {
        return java.lang.Math.copySign(android.graphics.ColorSpace.response(x < 0.0 ? -x : x, a, b, c, d, g), x);
    }

    /**
     * Compares two sets of parametric transfer functions parameters with a precision of 1e-3.
     *
     * @param a
     * 		The first set of parameters to compare
     * @param b
     * 		The second set of parameters to compare
     * @return True if the two sets are equal, false otherwise
     */
    private static boolean compare(@android.annotation.Nullable
    android.graphics.ColorSpace.Rgb.TransferParameters a, @android.annotation.Nullable
    android.graphics.ColorSpace.Rgb.TransferParameters b) {
        // noinspection SimplifiableIfStatement
        if ((a == null) && (b == null))
            return true;

        return ((((((((a != null) && (b != null)) && (java.lang.Math.abs(a.a - b.a) < 0.001)) && (java.lang.Math.abs(a.b - b.b) < 0.001)) && (java.lang.Math.abs(a.c - b.c) < 0.001)) && (java.lang.Math.abs(a.d - b.d) < 0.002))// Special case for variations in sRGB OETF/EOTF
         && (java.lang.Math.abs(a.e - b.e) < 0.001)) && (java.lang.Math.abs(a.f - b.f) < 0.001)) && (java.lang.Math.abs(a.g - b.g) < 0.001);
    }

    /**
     * Compares two arrays of float with a precision of 1e-3.
     *
     * @param a
     * 		The first array to compare
     * @param b
     * 		The second array to compare
     * @return True if the two arrays are equal, false otherwise
     */
    private static boolean compare(@android.annotation.NonNull
    float[] a, @android.annotation.NonNull
    float[] b) {
        if (a == b)
            return true;

        for (int i = 0; i < a.length; i++) {
            if ((java.lang.Float.compare(a[i], b[i]) != 0) && (java.lang.Math.abs(a[i] - b[i]) > 0.001F))
                return false;

        }
        return true;
    }

    /**
     * Inverts a 3x3 matrix. This method assumes the matrix is invertible.
     *
     * @param m
     * 		A 3x3 matrix as a non-null array of 9 floats
     * @return A new array of 9 floats containing the inverse of the input matrix
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    private static float[] inverse3x3(@android.annotation.NonNull
    @android.annotation.Size(9)
    float[] m) {
        float a = m[0];
        float b = m[3];
        float c = m[6];
        float d = m[1];
        float e = m[4];
        float f = m[7];
        float g = m[2];
        float h = m[5];
        float i = m[8];
        float A = (e * i) - (f * h);
        float B = (f * g) - (d * i);
        float C = (d * h) - (e * g);
        float det = ((a * A) + (b * B)) + (c * C);
        float[] inverted = new float[m.length];
        inverted[0] = A / det;
        inverted[1] = B / det;
        inverted[2] = C / det;
        inverted[3] = ((c * h) - (b * i)) / det;
        inverted[4] = ((a * i) - (c * g)) / det;
        inverted[5] = ((b * g) - (a * h)) / det;
        inverted[6] = ((b * f) - (c * e)) / det;
        inverted[7] = ((c * d) - (a * f)) / det;
        inverted[8] = ((a * e) - (b * d)) / det;
        return inverted;
    }

    /**
     * Multiplies two 3x3 matrices, represented as non-null arrays of 9 floats.
     *
     * @param lhs
     * 		3x3 matrix, as a non-null array of 9 floats
     * @param rhs
     * 		3x3 matrix, as a non-null array of 9 floats
     * @return A new array of 9 floats containing the result of the multiplication
    of rhs by lhs
     * @unknown 
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    public static float[] mul3x3(@android.annotation.NonNull
    @android.annotation.Size(9)
    float[] lhs, @android.annotation.NonNull
    @android.annotation.Size(9)
    float[] rhs) {
        float[] r = new float[9];
        r[0] = ((lhs[0] * rhs[0]) + (lhs[3] * rhs[1])) + (lhs[6] * rhs[2]);
        r[1] = ((lhs[1] * rhs[0]) + (lhs[4] * rhs[1])) + (lhs[7] * rhs[2]);
        r[2] = ((lhs[2] * rhs[0]) + (lhs[5] * rhs[1])) + (lhs[8] * rhs[2]);
        r[3] = ((lhs[0] * rhs[3]) + (lhs[3] * rhs[4])) + (lhs[6] * rhs[5]);
        r[4] = ((lhs[1] * rhs[3]) + (lhs[4] * rhs[4])) + (lhs[7] * rhs[5]);
        r[5] = ((lhs[2] * rhs[3]) + (lhs[5] * rhs[4])) + (lhs[8] * rhs[5]);
        r[6] = ((lhs[0] * rhs[6]) + (lhs[3] * rhs[7])) + (lhs[6] * rhs[8]);
        r[7] = ((lhs[1] * rhs[6]) + (lhs[4] * rhs[7])) + (lhs[7] * rhs[8]);
        r[8] = ((lhs[2] * rhs[6]) + (lhs[5] * rhs[7])) + (lhs[8] * rhs[8]);
        return r;
    }

    /**
     * Multiplies a vector of 3 components by a 3x3 matrix and stores the
     * result in the input vector.
     *
     * @param lhs
     * 		3x3 matrix, as a non-null array of 9 floats
     * @param rhs
     * 		Vector of 3 components, as a non-null array of 3 floats
     * @return The array of 3 passed as the rhs parameter
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 3)
    private static float[] mul3x3Float3(@android.annotation.NonNull
    @android.annotation.Size(9)
    float[] lhs, @android.annotation.NonNull
    @android.annotation.Size(min = 3)
    float[] rhs) {
        float r0 = rhs[0];
        float r1 = rhs[1];
        float r2 = rhs[2];
        rhs[0] = ((lhs[0] * r0) + (lhs[3] * r1)) + (lhs[6] * r2);
        rhs[1] = ((lhs[1] * r0) + (lhs[4] * r1)) + (lhs[7] * r2);
        rhs[2] = ((lhs[2] * r0) + (lhs[5] * r1)) + (lhs[8] * r2);
        return rhs;
    }

    /**
     * Multiplies a diagonal 3x3 matrix lhs, represented as an array of 3 floats,
     * by a 3x3 matrix represented as an array of 9 floats.
     *
     * @param lhs
     * 		Diagonal 3x3 matrix, as a non-null array of 3 floats
     * @param rhs
     * 		3x3 matrix, as a non-null array of 9 floats
     * @return A new array of 9 floats containing the result of the multiplication
    of rhs by lhs
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    private static float[] mul3x3Diag(@android.annotation.NonNull
    @android.annotation.Size(3)
    float[] lhs, @android.annotation.NonNull
    @android.annotation.Size(9)
    float[] rhs) {
        return new float[]{ lhs[0] * rhs[0], lhs[1] * rhs[1], lhs[2] * rhs[2], lhs[0] * rhs[3], lhs[1] * rhs[4], lhs[2] * rhs[5], lhs[0] * rhs[6], lhs[1] * rhs[7], lhs[2] * rhs[8] };
    }

    /**
     * Converts a value from CIE xyY to CIE XYZ. Y is assumed to be 1 so the
     * input xyY array only contains the x and y components.
     *
     * @param xyY
     * 		The xyY value to convert to XYZ, cannot be null, length must be 2
     * @return A new float array of length 3 containing XYZ values
     */
    @android.annotation.NonNull
    @android.annotation.Size(3)
    private static float[] xyYToXyz(@android.annotation.NonNull
    @android.annotation.Size(2)
    float[] xyY) {
        return new float[]{ xyY[0] / xyY[1], 1.0F, ((1 - xyY[0]) - xyY[1]) / xyY[1] };
    }

    /**
     * Converts values from CIE xyY to CIE L*u*v*. Y is assumed to be 1 so the
     * input xyY array only contains the x and y components. After this method
     * returns, the xyY array contains the converted u and v components.
     *
     * @param xyY
     * 		The xyY value to convert to XYZ, cannot be null,
     * 		length must be a multiple of 2
     */
    private static void xyYToUv(@android.annotation.NonNull
    @android.annotation.Size(multiple = 2)
    float[] xyY) {
        for (int i = 0; i < xyY.length; i += 2) {
            float x = xyY[i];
            float y = xyY[i + 1];
            float d = (((-2.0F) * x) + (12.0F * y)) + 3;
            float u = (4.0F * x) / d;
            float v = (9.0F * y) / d;
            xyY[i] = u;
            xyY[i + 1] = v;
        }
    }

    /**
     * <p>Computes the chromatic adaptation transform from the specified
     * source white point to the specified destination white point.</p>
     *
     * <p>The transform is computed using the von Kries method, described
     * in more details in the documentation of {@link Adaptation}. The
     * {@link Adaptation} enum provides different matrices that can be
     * used to perform the adaptation.</p>
     *
     * @param matrix
     * 		The adaptation matrix
     * @param srcWhitePoint
     * 		The white point to adapt from, *will be modified*
     * @param dstWhitePoint
     * 		The white point to adapt to, *will be modified*
     * @return A 3x3 matrix as a non-null array of 9 floats
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    private static float[] chromaticAdaptation(@android.annotation.NonNull
    @android.annotation.Size(9)
    float[] matrix, @android.annotation.NonNull
    @android.annotation.Size(3)
    float[] srcWhitePoint, @android.annotation.NonNull
    @android.annotation.Size(3)
    float[] dstWhitePoint) {
        float[] srcLMS = android.graphics.ColorSpace.mul3x3Float3(matrix, srcWhitePoint);
        float[] dstLMS = android.graphics.ColorSpace.mul3x3Float3(matrix, dstWhitePoint);
        // LMS is a diagonal matrix stored as a float[3]
        float[] LMS = new float[]{ dstLMS[0] / srcLMS[0], dstLMS[1] / srcLMS[1], dstLMS[2] / srcLMS[2] };
        return android.graphics.ColorSpace.mul3x3(android.graphics.ColorSpace.inverse3x3(matrix), android.graphics.ColorSpace.mul3x3Diag(LMS, matrix));
    }

    /**
     * <p>Computes the chromaticity coordinates of a specified correlated color
     * temperature (CCT) on the Planckian locus. The specified CCT must be
     * greater than 0. A meaningful CCT range is [1667, 25000].</p>
     *
     * <p>The transform is computed using the methods in Kang et
     * al., <i>Design of Advanced Color - Temperature Control System for HDTV
     * Applications</i>, Journal of Korean Physical Society 41, 865-871
     * (2002).</p>
     *
     * @param cct
     * 		The correlated color temperature, in Kelvin
     * @return Corresponding XYZ values
     * @throws IllegalArgumentException
     * 		If cct is invalid
     * @unknown 
     */
    @android.annotation.NonNull
    @android.annotation.Size(3)
    public static float[] cctToXyz(@android.annotation.IntRange(from = 1)
    int cct) {
        if (cct < 1) {
            throw new java.lang.IllegalArgumentException("Temperature must be greater than 0");
        }
        final float icct = 1000.0F / cct;
        final float icct2 = icct * icct;
        final float x = (cct <= 4000.0F) ? ((0.17991F + (0.8776956F * icct)) - (0.2343589F * icct2)) - ((0.2661239F * icct2) * icct) : ((0.24039F + (0.2226347F * icct)) + (2.1070378F * icct2)) - ((3.025847F * icct2) * icct);
        final float x2 = x * x;
        final float y = (cct <= 2222.0F) ? (((-0.20219684F) + (2.1855583F * x)) - (1.3481102F * x2)) - ((1.1063814F * x2) * x) : cct <= 4000.0F ? (((-0.16748866F) + (2.09137F * x)) - (1.3741859F * x2)) - ((0.9549476F * x2) * x) : (((-0.37001482F) + (3.7511299F * x)) - (5.873387F * x2)) + ((3.081758F * x2) * x);
        return android.graphics.ColorSpace.xyYToXyz(new float[]{ x, y });
    }

    /**
     * <p>Computes the chromaticity coordinates of a CIE series D illuminant
     * from the specified correlated color temperature (CCT). The specified CCT
     * must be greater than 0. A meaningful CCT range is [4000, 25000].</p>
     *
     * <p>The transform is computed using the methods referred to in Kang et
     * al., <i>Design of Advanced Color - Temperature Control System for HDTV
     * Applications</i>, Journal of Korean Physical Society 41, 865-871
     * (2002).</p>
     *
     * @param cct
     * 		The correlated color temperature, in Kelvin
     * @return Corresponding XYZ values
     * @throws IllegalArgumentException
     * 		If cct is invalid
     * @unknown 
     */
    @android.annotation.NonNull
    @android.annotation.Size(3)
    public static float[] cctToIlluminantdXyz(@android.annotation.IntRange(from = 1)
    int cct) {
        if (cct < 1) {
            throw new java.lang.IllegalArgumentException("Temperature must be greater than 0");
        }
        final float icct = 1.0F / cct;
        final float icct2 = icct * icct;
        final float x = (cct <= 7000.0F) ? ((0.244063F + (99.11F * icct)) + (2967800.0F * icct2)) - ((4.6070001E9F * icct2) * icct) : ((0.23704F + (247.48F * icct)) + (1901800.0F * icct2)) - ((2.0064E9F * icct2) * icct);
        final float y = ((((-3.0F) * x) * x) + (2.87F * x)) - 0.275F;
        return android.graphics.ColorSpace.xyYToXyz(new float[]{ x, y });
    }

    /**
     * <p>Computes the chromatic adaptation transform from the specified
     * source white point to the specified destination white point.</p>
     *
     * <p>The transform is computed using the von Kries method, described
     * in more details in the documentation of {@link Adaptation}. The
     * {@link Adaptation} enum provides different matrices that can be
     * used to perform the adaptation.</p>
     *
     * @param adaptation
     * 		The adaptation method
     * @param srcWhitePoint
     * 		The white point to adapt from
     * @param dstWhitePoint
     * 		The white point to adapt to
     * @return A 3x3 matrix as a non-null array of 9 floats
     * @unknown 
     */
    @android.annotation.NonNull
    @android.annotation.Size(9)
    public static float[] chromaticAdaptation(@android.annotation.NonNull
    android.graphics.ColorSpace.Adaptation adaptation, @android.annotation.NonNull
    @android.annotation.Size(min = 2, max = 3)
    float[] srcWhitePoint, @android.annotation.NonNull
    @android.annotation.Size(min = 2, max = 3)
    float[] dstWhitePoint) {
        float[] srcXyz = (srcWhitePoint.length == 3) ? java.util.Arrays.copyOf(srcWhitePoint, 3) : android.graphics.ColorSpace.xyYToXyz(srcWhitePoint);
        float[] dstXyz = (dstWhitePoint.length == 3) ? java.util.Arrays.copyOf(dstWhitePoint, 3) : android.graphics.ColorSpace.xyYToXyz(dstWhitePoint);
        if (android.graphics.ColorSpace.compare(srcXyz, dstXyz)) {
            return new float[]{ 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
        }
        return android.graphics.ColorSpace.chromaticAdaptation(adaptation.mTransform, srcXyz, dstXyz);
    }

    /**
     * Implementation of the CIE XYZ color space. Assumes the white point is D50.
     */
    @android.annotation.AnyThread
    private static final class Xyz extends android.graphics.ColorSpace {
        private Xyz(@android.annotation.NonNull
        java.lang.String name, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            super(name, android.graphics.ColorSpace.Model.XYZ, id);
        }

        @java.lang.Override
        public boolean isWideGamut() {
            return true;
        }

        @java.lang.Override
        public float getMinValue(@android.annotation.IntRange(from = 0, to = 3)
        int component) {
            return -2.0F;
        }

        @java.lang.Override
        public float getMaxValue(@android.annotation.IntRange(from = 0, to = 3)
        int component) {
            return 2.0F;
        }

        @java.lang.Override
        public float[] toXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = android.graphics.ColorSpace.Xyz.clamp(v[0]);
            v[1] = android.graphics.ColorSpace.Xyz.clamp(v[1]);
            v[2] = android.graphics.ColorSpace.Xyz.clamp(v[2]);
            return v;
        }

        @java.lang.Override
        public float[] fromXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = android.graphics.ColorSpace.Xyz.clamp(v[0]);
            v[1] = android.graphics.ColorSpace.Xyz.clamp(v[1]);
            v[2] = android.graphics.ColorSpace.Xyz.clamp(v[2]);
            return v;
        }

        private static float clamp(float x) {
            return x < (-2.0F) ? -2.0F : x > 2.0F ? 2.0F : x;
        }
    }

    /**
     * Implementation of the CIE L*a*b* color space. Its PCS is CIE XYZ
     * with a white point of D50.
     */
    @android.annotation.AnyThread
    private static final class Lab extends android.graphics.ColorSpace {
        private static final float A = 216.0F / 24389.0F;

        private static final float B = 841.0F / 108.0F;

        private static final float C = 4.0F / 29.0F;

        private static final float D = 6.0F / 29.0F;

        private Lab(@android.annotation.NonNull
        java.lang.String name, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            super(name, android.graphics.ColorSpace.Model.LAB, id);
        }

        @java.lang.Override
        public boolean isWideGamut() {
            return true;
        }

        @java.lang.Override
        public float getMinValue(@android.annotation.IntRange(from = 0, to = 3)
        int component) {
            return component == 0 ? 0.0F : -128.0F;
        }

        @java.lang.Override
        public float getMaxValue(@android.annotation.IntRange(from = 0, to = 3)
        int component) {
            return component == 0 ? 100.0F : 128.0F;
        }

        @java.lang.Override
        public float[] toXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = android.graphics.ColorSpace.Lab.clamp(v[0], 0.0F, 100.0F);
            v[1] = android.graphics.ColorSpace.Lab.clamp(v[1], -128.0F, 128.0F);
            v[2] = android.graphics.ColorSpace.Lab.clamp(v[2], -128.0F, 128.0F);
            float fy = (v[0] + 16.0F) / 116.0F;
            float fx = fy + (v[1] * 0.002F);
            float fz = fy - (v[2] * 0.005F);
            float X = (fx > android.graphics.ColorSpace.Lab.D) ? (fx * fx) * fx : (1.0F / android.graphics.ColorSpace.Lab.B) * (fx - android.graphics.ColorSpace.Lab.C);
            float Y = (fy > android.graphics.ColorSpace.Lab.D) ? (fy * fy) * fy : (1.0F / android.graphics.ColorSpace.Lab.B) * (fy - android.graphics.ColorSpace.Lab.C);
            float Z = (fz > android.graphics.ColorSpace.Lab.D) ? (fz * fz) * fz : (1.0F / android.graphics.ColorSpace.Lab.B) * (fz - android.graphics.ColorSpace.Lab.C);
            v[0] = X * android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[0];
            v[1] = Y * android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[1];
            v[2] = Z * android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[2];
            return v;
        }

        @java.lang.Override
        public float[] fromXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            float X = v[0] / android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[0];
            float Y = v[1] / android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[1];
            float Z = v[2] / android.graphics.ColorSpace.ILLUMINANT_D50_XYZ[2];
            float fx = (X > android.graphics.ColorSpace.Lab.A) ? ((float) (java.lang.Math.pow(X, 1.0 / 3.0))) : (android.graphics.ColorSpace.Lab.B * X) + android.graphics.ColorSpace.Lab.C;
            float fy = (Y > android.graphics.ColorSpace.Lab.A) ? ((float) (java.lang.Math.pow(Y, 1.0 / 3.0))) : (android.graphics.ColorSpace.Lab.B * Y) + android.graphics.ColorSpace.Lab.C;
            float fz = (Z > android.graphics.ColorSpace.Lab.A) ? ((float) (java.lang.Math.pow(Z, 1.0 / 3.0))) : (android.graphics.ColorSpace.Lab.B * Z) + android.graphics.ColorSpace.Lab.C;
            float L = (116.0F * fy) - 16.0F;
            float a = 500.0F * (fx - fy);
            float b = 200.0F * (fy - fz);
            v[0] = android.graphics.ColorSpace.Lab.clamp(L, 0.0F, 100.0F);
            v[1] = android.graphics.ColorSpace.Lab.clamp(a, -128.0F, 128.0F);
            v[2] = android.graphics.ColorSpace.Lab.clamp(b, -128.0F, 128.0F);
            return v;
        }

        private static float clamp(float x, float min, float max) {
            return x < min ? min : x > max ? max : x;
        }
    }

    /**
     * Retrieve the native SkColorSpace object for passing to native.
     *
     * Only valid on ColorSpace.Rgb.
     */
    long getNativeInstance() {
        throw new java.lang.IllegalArgumentException("colorSpace must be an RGB color space");
    }

    /**
     * {@usesMathJax }
     *
     * <p>An RGB color space is an additive color space using the
     * {@link Model#RGB RGB} color model (a color is therefore represented
     * by a tuple of 3 numbers).</p>
     *
     * <p>A specific RGB color space is defined by the following properties:</p>
     * <ul>
     *     <li>Three chromaticities of the red, green and blue primaries, which
     *     define the gamut of the color space.</li>
     *     <li>A white point chromaticity that defines the stimulus to which
     *     color space values are normalized (also just called "white").</li>
     *     <li>An opto-electronic transfer function, also called opto-electronic
     *     conversion function or often, and approximately, gamma function.</li>
     *     <li>An electro-optical transfer function, also called electo-optical
     *     conversion function or often, and approximately, gamma function.</li>
     *     <li>A range of valid RGB values (most commonly \([0..1]\)).</li>
     * </ul>
     *
     * <p>The most commonly used RGB color space is {@link Named#SRGB sRGB}.</p>
     *
     * <h3>Primaries and white point chromaticities</h3>
     * <p>In this implementation, the chromaticity of the primaries and the white
     * point of an RGB color space is defined in the CIE xyY color space. This
     * color space separates the chromaticity of a color, the x and y components,
     * and its luminance, the Y component. Since the primaries and the white
     * point have full brightness, the Y component is assumed to be 1 and only
     * the x and y components are needed to encode them.</p>
     * <p>For convenience, this implementation also allows to define the
     * primaries and white point in the CIE XYZ space. The tristimulus XYZ values
     * are internally converted to xyY.</p>
     *
     * <p>
     *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_srgb.png" />
     *     <figcaption style="text-align: center;">sRGB primaries and white point</figcaption>
     * </p>
     *
     * <h3>Transfer functions</h3>
     * <p>A transfer function is a color component conversion function, defined as
     * a single variable, monotonic mathematical function. It is applied to each
     * individual component of a color. They are used to perform the mapping
     * between linear tristimulus values and non-linear electronic signal value.</p>
     * <p>The <em>opto-electronic transfer function</em> (OETF or OECF) encodes
     * tristimulus values in a scene to a non-linear electronic signal value.
     * An OETF is often expressed as a power function with an exponent between
     * 0.38 and 0.55 (the reciprocal of 1.8 to 2.6).</p>
     * <p>The <em>electro-optical transfer function</em> (EOTF or EOCF) decodes
     * a non-linear electronic signal value to a tristimulus value at the display.
     * An EOTF is often expressed as a power function with an exponent between
     * 1.8 and 2.6.</p>
     * <p>Transfer functions are used as a compression scheme. For instance,
     * linear sRGB values would normally require 11 to 12 bits of precision to
     * store all values that can be perceived by the human eye. When encoding
     * sRGB values using the appropriate OETF (see {@link Named#SRGB sRGB} for
     * an exact mathematical description of that OETF), the values can be
     * compressed to only 8 bits precision.</p>
     * <p>When manipulating RGB values, particularly sRGB values, it is safe
     * to assume that these values have been encoded with the appropriate
     * OETF (unless noted otherwise). Encoded values are often said to be in
     * "gamma space". They are therefore defined in a non-linear space. This
     * in turns means that any linear operation applied to these values is
     * going to yield mathematically incorrect results (any linear interpolation
     * such as gradient generation for instance, most image processing functions
     * such as blurs, etc.).</p>
     * <p>To properly process encoded RGB values you must first apply the
     * EOTF to decode the value into linear space. After processing, the RGB
     * value must be encoded back to non-linear ("gamma") space. Here is a
     * formal description of the process, where \(f\) is the processing
     * function to apply:</p>
     *
     * $$RGB_{out} = OETF(f(EOTF(RGB_{in})))$$
     *
     * <p>If the transfer functions of the color space can be expressed as an
     * ICC parametric curve as defined in ICC.1:2004-10, the numeric parameters
     * can be retrieved by calling {@link #getTransferParameters()}. This can
     * be useful to match color spaces for instance.</p>
     *
     * <p class="note">Some RGB color spaces, such as {@link Named#ACES} and
     * {@link Named#LINEAR_EXTENDED_SRGB scRGB}, are said to be linear because
     * their transfer functions are the identity function: \(f(x) = x\).
     * If the source and/or destination are known to be linear, it is not
     * necessary to invoke the transfer functions.</p>
     *
     * <h3>Range</h3>
     * <p>Most RGB color spaces allow RGB values in the range \([0..1]\). There
     * are however a few RGB color spaces that allow much larger ranges. For
     * instance, {@link Named#EXTENDED_SRGB scRGB} is used to manipulate the
     * range \([-0.5..7.5]\) while {@link Named#ACES ACES} can be used throughout
     * the range \([-65504, 65504]\).</p>
     *
     * <p>
     *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_scrgb.png" />
     *     <figcaption style="text-align: center;">Extended sRGB and its large range</figcaption>
     * </p>
     *
     * <h3>Converting between RGB color spaces</h3>
     * <p>Conversion between two color spaces is achieved by using an intermediate
     * color space called the profile connection space (PCS). The PCS used by
     * this implementation is CIE XYZ. The conversion operation is defined
     * as such:</p>
     *
     * $$RGB_{out} = OETF(T_{dst}^{-1} \cdot T_{src} \cdot EOTF(RGB_{in}))$$
     *
     * <p>Where \(T_{src}\) is the {@link #getTransform() RGB to XYZ transform}
     * of the source color space and \(T_{dst}^{-1}\) the {@link #getInverseTransform()
     * XYZ to RGB transform} of the destination color space.</p>
     * <p>Many RGB color spaces commonly used with electronic devices use the
     * standard illuminant {@link #ILLUMINANT_D65 D65}. Care must be take however
     * when converting between two RGB color spaces if their white points do not
     * match. This can be achieved by either calling
     * {@link #adapt(ColorSpace, float[])} to adapt one or both color spaces to
     * a single common white point. This can be achieved automatically by calling
     * {@link ColorSpace#connect(ColorSpace, ColorSpace)}, which also handles
     * non-RGB color spaces.</p>
     * <p>To learn more about the white point adaptation process, refer to the
     * documentation of {@link Adaptation}.</p>
     */
    @android.annotation.AnyThread
    public static class Rgb extends android.graphics.ColorSpace {
        /**
         * {@usesMathJax }
         *
         * <p>Defines the parameters for the ICC parametric curve type 4, as
         * defined in ICC.1:2004-10, section 10.15.</p>
         *
         * <p>The EOTF is of the form:</p>
         *
         * \(\begin{equation}
         * Y = \begin{cases}c X + f & X \lt d \\\
         * \left( a X + b \right) ^{g} + e & X \ge d \end{cases}
         * \end{equation}\)
         *
         * <p>The corresponding OETF is simply the inverse function.</p>
         *
         * <p>The parameters defined by this class form a valid transfer
         * function only if all the following conditions are met:</p>
         * <ul>
         *     <li>No parameter is a {@link Double#isNaN(double) Not-a-Number}</li>
         *     <li>\(d\) is in the range \([0..1]\)</li>
         *     <li>The function is not constant</li>
         *     <li>The function is positive and increasing</li>
         * </ul>
         */
        public static class TransferParameters {
            /**
             * Variable \(a\) in the equation of the EOTF described above.
             */
            public final double a;

            /**
             * Variable \(b\) in the equation of the EOTF described above.
             */
            public final double b;

            /**
             * Variable \(c\) in the equation of the EOTF described above.
             */
            public final double c;

            /**
             * Variable \(d\) in the equation of the EOTF described above.
             */
            public final double d;

            /**
             * Variable \(e\) in the equation of the EOTF described above.
             */
            public final double e;

            /**
             * Variable \(f\) in the equation of the EOTF described above.
             */
            public final double f;

            /**
             * Variable \(g\) in the equation of the EOTF described above.
             */
            public final double g;

            /**
             * <p>Defines the parameters for the ICC parametric curve type 3, as
             * defined in ICC.1:2004-10, section 10.15.</p>
             *
             * <p>The EOTF is of the form:</p>
             *
             * \(\begin{equation}
             * Y = \begin{cases}c X & X \lt d \\\
             * \left( a X + b \right) ^{g} & X \ge d \end{cases}
             * \end{equation}\)
             *
             * <p>This constructor is equivalent to setting  \(e\) and \(f\) to 0.</p>
             *
             * @param a
             * 		The value of \(a\) in the equation of the EOTF described above
             * @param b
             * 		The value of \(b\) in the equation of the EOTF described above
             * @param c
             * 		The value of \(c\) in the equation of the EOTF described above
             * @param d
             * 		The value of \(d\) in the equation of the EOTF described above
             * @param g
             * 		The value of \(g\) in the equation of the EOTF described above
             * @throws IllegalArgumentException
             * 		If the parameters form an invalid transfer function
             */
            public TransferParameters(double a, double b, double c, double d, double g) {
                this(a, b, c, d, 0.0, 0.0, g);
            }

            /**
             * <p>Defines the parameters for the ICC parametric curve type 4, as
             * defined in ICC.1:2004-10, section 10.15.</p>
             *
             * @param a
             * 		The value of \(a\) in the equation of the EOTF described above
             * @param b
             * 		The value of \(b\) in the equation of the EOTF described above
             * @param c
             * 		The value of \(c\) in the equation of the EOTF described above
             * @param d
             * 		The value of \(d\) in the equation of the EOTF described above
             * @param e
             * 		The value of \(e\) in the equation of the EOTF described above
             * @param f
             * 		The value of \(f\) in the equation of the EOTF described above
             * @param g
             * 		The value of \(g\) in the equation of the EOTF described above
             * @throws IllegalArgumentException
             * 		If the parameters form an invalid transfer function
             */
            public TransferParameters(double a, double b, double c, double d, double e, double f, double g) {
                if ((((((java.lang.Double.isNaN(a) || java.lang.Double.isNaN(b)) || java.lang.Double.isNaN(c)) || java.lang.Double.isNaN(d)) || java.lang.Double.isNaN(e)) || java.lang.Double.isNaN(f)) || java.lang.Double.isNaN(g)) {
                    throw new java.lang.IllegalArgumentException("Parameters cannot be NaN");
                }
                // Next representable float after 1.0
                // We use doubles here but the representation inside our native code is often floats
                if (!((d >= 0.0) && (d <= (1.0F + java.lang.Math.ulp(1.0F))))) {
                    throw new java.lang.IllegalArgumentException(("Parameter d must be in the range [0..1], " + "was ") + d);
                }
                if ((d == 0.0) && ((a == 0.0) || (g == 0.0))) {
                    throw new java.lang.IllegalArgumentException("Parameter a or g is zero, the transfer function is constant");
                }
                if ((d >= 1.0) && (c == 0.0)) {
                    throw new java.lang.IllegalArgumentException("Parameter c is zero, the transfer function is constant");
                }
                if (((a == 0.0) || (g == 0.0)) && (c == 0.0)) {
                    throw new java.lang.IllegalArgumentException("Parameter a or g is zero," + " and c is zero, the transfer function is constant");
                }
                if (c < 0.0) {
                    throw new java.lang.IllegalArgumentException("The transfer function must be increasing");
                }
                if ((a < 0.0) || (g < 0.0)) {
                    throw new java.lang.IllegalArgumentException("The transfer function must be " + "positive or increasing");
                }
                this.a = a;
                this.b = b;
                this.c = c;
                this.d = d;
                this.e = e;
                this.f = f;
                this.g = g;
            }

            @java.lang.SuppressWarnings("SimplifiableIfStatement")
            @java.lang.Override
            public boolean equals(java.lang.Object o) {
                if (this == o)
                    return true;

                if ((o == null) || (getClass() != o.getClass()))
                    return false;

                android.graphics.ColorSpace.Rgb.TransferParameters that = ((android.graphics.ColorSpace.Rgb.TransferParameters) (o));
                if (java.lang.Double.compare(that.a, a) != 0)
                    return false;

                if (java.lang.Double.compare(that.b, b) != 0)
                    return false;

                if (java.lang.Double.compare(that.c, c) != 0)
                    return false;

                if (java.lang.Double.compare(that.d, d) != 0)
                    return false;

                if (java.lang.Double.compare(that.e, e) != 0)
                    return false;

                if (java.lang.Double.compare(that.f, f) != 0)
                    return false;

                return java.lang.Double.compare(that.g, g) == 0;
            }

            @java.lang.Override
            public int hashCode() {
                int result;
                long temp;
                temp = java.lang.Double.doubleToLongBits(a);
                result = ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(b);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(c);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(d);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(e);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(f);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                temp = java.lang.Double.doubleToLongBits(g);
                result = (31 * result) + ((int) (temp ^ (temp >>> 32)));
                return result;
            }
        }

        @android.annotation.NonNull
        private final float[] mWhitePoint;

        @android.annotation.NonNull
        private final float[] mPrimaries;

        @android.annotation.NonNull
        private final float[] mTransform;

        @android.annotation.NonNull
        private final float[] mInverseTransform;

        @android.annotation.NonNull
        private final java.util.function.DoubleUnaryOperator mOetf;

        @android.annotation.NonNull
        private final java.util.function.DoubleUnaryOperator mEotf;

        @android.annotation.NonNull
        private final java.util.function.DoubleUnaryOperator mClampedOetf;

        @android.annotation.NonNull
        private final java.util.function.DoubleUnaryOperator mClampedEotf;

        private final float mMin;

        private final float mMax;

        private final boolean mIsWideGamut;

        private final boolean mIsSrgb;

        @android.annotation.Nullable
        private final android.graphics.ColorSpace.Rgb.TransferParameters mTransferParameters;

        private final long mNativePtr;

        @java.lang.Override
        long getNativeInstance() {
            if (mNativePtr == 0) {
                // If this object has TransferParameters, it must have a native object.
                throw new java.lang.IllegalArgumentException(("ColorSpace must use an ICC " + "parametric transfer function! used ") + this);
            }
            return mNativePtr;
        }

        private static native long nativeGetNativeFinalizer();

        private static native long nativeCreate(float a, float b, float c, float d, float e, float f, float g, float[] xyz);

        /**
         * <p>Creates a new RGB color space using a 3x3 column-major transform matrix.
         * The transform matrix must convert from the RGB space to the profile connection
         * space CIE XYZ.</p>
         *
         * <p class="note">The range of the color space is imposed to be \([0..1]\).</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param toXYZ
         * 		3x3 column-major transform matrix from RGB to the profile
         * 		connection space CIE XYZ as an array of 9 floats, cannot be null
         * @param oetf
         * 		Opto-electronic transfer function, cannot be null
         * @param eotf
         * 		Electro-optical transfer function, cannot be null
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The OETF is null or the EOTF is null.</li>
         * 		<li>The minimum valid value is >= the maximum valid value.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(9)
        float[] toXYZ, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator oetf, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator eotf) {
            this(name, android.graphics.ColorSpace.Rgb.computePrimaries(toXYZ), android.graphics.ColorSpace.Rgb.computeWhitePoint(toXYZ), null, oetf, eotf, 0.0F, 1.0F, null, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * <p class="note">The ID, areturned by {@link #getId()}, of an object created by
         * this constructor is always {@link #MIN_ID}.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param oetf
         * 		Opto-electronic transfer function, cannot be null
         * @param eotf
         * 		Electro-optical transfer function, cannot be null
         * @param min
         * 		The minimum valid value in this color space's RGB range
         * @param max
         * 		The maximum valid value in this color space's RGB range
         * @throws IllegalArgumentException
         * 		<p>If any of the following conditions is met:</p>
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>The OETF is null or the EOTF is null.</li>
         * 		<li>The minimum valid value is >= the maximum valid value.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator oetf, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator eotf, float min, float max) {
            this(name, primaries, whitePoint, null, oetf, eotf, min, max, null, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a 3x3 column-major transform matrix.
         * The transform matrix must convert from the RGB space to the profile connection
         * space CIE XYZ.</p>
         *
         * <p class="note">The range of the color space is imposed to be \([0..1]\).</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param toXYZ
         * 		3x3 column-major transform matrix from RGB to the profile
         * 		connection space CIE XYZ as an array of 9 floats, cannot be null
         * @param function
         * 		Parameters for the transfer functions
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>Gamma is negative.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(9)
        float[] toXYZ, @android.annotation.NonNull
        android.graphics.ColorSpace.Rgb.TransferParameters function) {
            this(name, android.graphics.ColorSpace.Rgb.computePrimaries(toXYZ), android.graphics.ColorSpace.Rgb.computeWhitePoint(toXYZ), function, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param function
         * 		Parameters for the transfer functions
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>The transfer parameters are invalid.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, @android.annotation.NonNull
        android.graphics.ColorSpace.Rgb.TransferParameters function) {
            this(name, primaries, whitePoint, function, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param function
         * 		Parameters for the transfer functions
         * @param id
         * 		ID of this color space as an integer between {@link #MIN_ID} and {@link #MAX_ID}
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>The ID is not between {@link #MIN_ID} and {@link #MAX_ID}.</li>
         * 		<li>The transfer parameters are invalid.</li>
         * 		</ul>
         * @see #get(Named)
         */
        private Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, @android.annotation.NonNull
        android.graphics.ColorSpace.Rgb.TransferParameters function, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            this(name, primaries, whitePoint, null, (function.e == 0.0) && (function.f == 0.0) ? ( x) -> android.graphics.ColorSpace.rcpResponse(x, function.a, function.b, function.c, function.d, function.g) : ( x) -> android.graphics.ColorSpace.rcpResponse(x, function.a, function.b, function.c, function.d, function.e, function.f, function.g), (function.e == 0.0) && (function.f == 0.0) ? ( x) -> android.graphics.ColorSpace.response(x, function.a, function.b, function.c, function.d, function.g) : ( x) -> android.graphics.ColorSpace.response(x, function.a, function.b, function.c, function.d, function.e, function.f, function.g), 0.0F, 1.0F, function, id);
        }

        /**
         * <p>Creates a new RGB color space using a 3x3 column-major transform matrix.
         * The transform matrix must convert from the RGB space to the profile connection
         * space CIE XYZ.</p>
         *
         * <p class="note">The range of the color space is imposed to be \([0..1]\).</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param toXYZ
         * 		3x3 column-major transform matrix from RGB to the profile
         * 		connection space CIE XYZ as an array of 9 floats, cannot be null
         * @param gamma
         * 		Gamma to use as the transfer function
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>Gamma is negative.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(9)
        float[] toXYZ, double gamma) {
            this(name, android.graphics.ColorSpace.Rgb.computePrimaries(toXYZ), android.graphics.ColorSpace.Rgb.computeWhitePoint(toXYZ), gamma, 0.0F, 1.0F, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param gamma
         * 		Gamma to use as the transfer function
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>Gamma is negative.</li>
         * 		</ul>
         * @see #get(Named)
         */
        public Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, double gamma) {
            this(name, primaries, whitePoint, gamma, 0.0F, 1.0F, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param gamma
         * 		Gamma to use as the transfer function
         * @param min
         * 		The minimum valid value in this color space's RGB range
         * @param max
         * 		The maximum valid value in this color space's RGB range
         * @param id
         * 		ID of this color space as an integer between {@link #MIN_ID} and {@link #MAX_ID}
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>The minimum valid value is >= the maximum valid value.</li>
         * 		<li>The ID is not between {@link #MIN_ID} and {@link #MAX_ID}.</li>
         * 		<li>Gamma is negative.</li>
         * 		</ul>
         * @see #get(Named)
         */
        private Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, double gamma, float min, float max, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            this(name, primaries, whitePoint, null, gamma == 1.0 ? java.util.function.DoubleUnaryOperator.identity() : ( x) -> java.lang.Math.pow(x < 0.0 ? 0.0 : x, 1 / gamma), gamma == 1.0 ? java.util.function.DoubleUnaryOperator.identity() : ( x) -> java.lang.Math.pow(x < 0.0 ? 0.0 : x, gamma), min, max, new android.graphics.ColorSpace.Rgb.TransferParameters(1.0, 0.0, 0.0, 0.0, gamma), id);
        }

        /**
         * <p>Creates a new RGB color space using a specified set of primaries
         * and a specified white point.</p>
         *
         * <p>The primaries and white point can be specified in the CIE xyY space
         * or in CIE XYZ. The length of the arrays depends on the chosen space:</p>
         *
         * <table summary="Parameters length">
         *     <tr><th>Space</th><th>Primaries length</th><th>White point length</th></tr>
         *     <tr><td>xyY</td><td>6</td><td>2</td></tr>
         *     <tr><td>XYZ</td><td>9</td><td>3</td></tr>
         * </table>
         *
         * <p>When the primaries and/or white point are specified in xyY, the Y component
         * does not need to be specified and is assumed to be 1.0. Only the xy components
         * are required.</p>
         *
         * @param name
         * 		Name of the color space, cannot be null, its length must be >= 1
         * @param primaries
         * 		RGB primaries as an array of 6 (xy) or 9 (XYZ) floats
         * @param whitePoint
         * 		Reference white as an array of 2 (xy) or 3 (XYZ) floats
         * @param transform
         * 		Computed transform matrix that converts from RGB to XYZ, or
         * 		{@code null} to compute it from {@code primaries} and {@code whitePoint}.
         * @param oetf
         * 		Opto-electronic transfer function, cannot be null
         * @param eotf
         * 		Electro-optical transfer function, cannot be null
         * @param min
         * 		The minimum valid value in this color space's RGB range
         * @param max
         * 		The maximum valid value in this color space's RGB range
         * @param transferParameters
         * 		Parameters for the transfer functions
         * @param id
         * 		ID of this color space as an integer between {@link #MIN_ID} and {@link #MAX_ID}
         * @throws IllegalArgumentException
         * 		If any of the following conditions is met:
         * 		<ul>
         * 		<li>The name is null or has a length of 0.</li>
         * 		<li>The primaries array is null or has a length that is neither 6 or 9.</li>
         * 		<li>The white point array is null or has a length that is neither 2 or 3.</li>
         * 		<li>The OETF is null or the EOTF is null.</li>
         * 		<li>The minimum valid value is >= the maximum valid value.</li>
         * 		<li>The ID is not between {@link #MIN_ID} and {@link #MAX_ID}.</li>
         * 		</ul>
         * @see #get(Named)
         */
        private Rgb(@android.annotation.NonNull
        @android.annotation.Size(min = 1)
        java.lang.String name, @android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint, @android.annotation.Nullable
        @android.annotation.Size(9)
        float[] transform, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator oetf, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator eotf, float min, float max, @android.annotation.Nullable
        android.graphics.ColorSpace.Rgb.TransferParameters transferParameters, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            super(name, android.graphics.ColorSpace.Model.RGB, id);
            if ((primaries == null) || ((primaries.length != 6) && (primaries.length != 9))) {
                throw new java.lang.IllegalArgumentException("The color space's primaries must be " + "defined as an array of 6 floats in xyY or 9 floats in XYZ");
            }
            if ((whitePoint == null) || ((whitePoint.length != 2) && (whitePoint.length != 3))) {
                throw new java.lang.IllegalArgumentException("The color space's white point must be " + "defined as an array of 2 floats in xyY or 3 float in XYZ");
            }
            if ((oetf == null) || (eotf == null)) {
                throw new java.lang.IllegalArgumentException("The transfer functions of a color space " + "cannot be null");
            }
            if (min >= max) {
                throw new java.lang.IllegalArgumentException(((("Invalid range: min=" + min) + ", max=") + max) + "; min must be strictly < max");
            }
            mWhitePoint = android.graphics.ColorSpace.Rgb.xyWhitePoint(whitePoint);
            mPrimaries = android.graphics.ColorSpace.Rgb.xyPrimaries(primaries);
            if (transform == null) {
                mTransform = android.graphics.ColorSpace.Rgb.computeXYZMatrix(mPrimaries, mWhitePoint);
            } else {
                if (transform.length != 9) {
                    throw new java.lang.IllegalArgumentException("Transform must have 9 entries! Has " + transform.length);
                }
                mTransform = transform;
            }
            mInverseTransform = android.graphics.ColorSpace.inverse3x3(mTransform);
            mOetf = oetf;
            mEotf = eotf;
            mMin = min;
            mMax = max;
            java.util.function.DoubleUnaryOperator clamp = this::clamp;
            mClampedOetf = oetf.andThen(clamp);
            mClampedEotf = clamp.andThen(eotf);
            mTransferParameters = transferParameters;
            // A color space is wide-gamut if its area is >90% of NTSC 1953 and
            // if it entirely contains the Color space definition in xyY
            mIsWideGamut = android.graphics.ColorSpace.Rgb.isWideGamut(mPrimaries, min, max);
            mIsSrgb = android.graphics.ColorSpace.Rgb.isSrgb(mPrimaries, mWhitePoint, oetf, eotf, min, max, id);
            if (mTransferParameters != null) {
                if ((mWhitePoint == null) || (mTransform == null)) {
                    throw new java.lang.IllegalStateException((((("ColorSpace (" + this) + ") cannot create native object! mWhitePoint: ") + mWhitePoint) + " mTransform: ") + mTransform);
                }
                // This mimics the old code that was in native.
                float[] nativeTransform = android.graphics.ColorSpace.adaptToIlluminantD50(mWhitePoint, mTransform);
                mNativePtr = android.graphics.ColorSpace.Rgb.nativeCreate(((float) (mTransferParameters.a)), ((float) (mTransferParameters.b)), ((float) (mTransferParameters.c)), ((float) (mTransferParameters.d)), ((float) (mTransferParameters.e)), ((float) (mTransferParameters.f)), ((float) (mTransferParameters.g)), nativeTransform);
                android.graphics.ColorSpace.Rgb.NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativePtr);
            } else {
                mNativePtr = 0;
            }
        }

        private static class NoImagePreloadHolder {
            public static final libcore.util.NativeAllocationRegistry sRegistry = new libcore.util.NativeAllocationRegistry(android.graphics.ColorSpace.Rgb.class.getClassLoader(), android.graphics.ColorSpace.Rgb.nativeGetNativeFinalizer(), 0);
        }

        /**
         * Creates a copy of the specified color space with a new transform.
         *
         * @param colorSpace
         * 		The color space to create a copy of
         */
        private Rgb(android.graphics.ColorSpace.Rgb colorSpace, @android.annotation.NonNull
        @android.annotation.Size(9)
        float[] transform, @android.annotation.NonNull
        @android.annotation.Size(min = 2, max = 3)
        float[] whitePoint) {
            this(colorSpace.getName(), colorSpace.mPrimaries, whitePoint, transform, colorSpace.mOetf, colorSpace.mEotf, colorSpace.mMin, colorSpace.mMax, colorSpace.mTransferParameters, android.graphics.ColorSpace.MIN_ID);
        }

        /**
         * Copies the non-adapted CIE xyY white point of this color space in
         * specified array. The Y component is assumed to be 1 and is therefore
         * not copied into the destination. The x and y components are written
         * in the array at positions 0 and 1 respectively.
         *
         * @param whitePoint
         * 		The destination array, cannot be null, its length
         * 		must be >= 2
         * @return The destination array passed as a parameter
         * @see #getWhitePoint(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 2)
        public float[] getWhitePoint(@android.annotation.NonNull
        @android.annotation.Size(min = 2)
        float[] whitePoint) {
            whitePoint[0] = mWhitePoint[0];
            whitePoint[1] = mWhitePoint[1];
            return whitePoint;
        }

        /**
         * Returns the non-adapted CIE xyY white point of this color space as
         * a new array of 2 floats. The Y component is assumed to be 1 and is
         * therefore not copied into the destination. The x and y components
         * are written in the array at positions 0 and 1 respectively.
         *
         * @return A new non-null array of 2 floats
         * @see #getWhitePoint()
         */
        @android.annotation.NonNull
        @android.annotation.Size(2)
        public float[] getWhitePoint() {
            return java.util.Arrays.copyOf(mWhitePoint, mWhitePoint.length);
        }

        /**
         * Copies the primaries of this color space in specified array. The Y
         * component is assumed to be 1 and is therefore not copied into the
         * destination. The x and y components of the first primary are written
         * in the array at positions 0 and 1 respectively.
         *
         * @param primaries
         * 		The destination array, cannot be null, its length
         * 		must be >= 6
         * @return The destination array passed as a parameter
         * @see #getPrimaries(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 6)
        public float[] getPrimaries(@android.annotation.NonNull
        @android.annotation.Size(min = 6)
        float[] primaries) {
            java.lang.System.arraycopy(mPrimaries, 0, primaries, 0, mPrimaries.length);
            return primaries;
        }

        /**
         * Returns the primaries of this color space as a new array of 6 floats.
         * The Y component is assumed to be 1 and is therefore not copied into
         * the destination. The x and y components of the first primary are
         * written in the array at positions 0 and 1 respectively.
         *
         * @return A new non-null array of 2 floats
         * @see #getWhitePoint()
         */
        @android.annotation.NonNull
        @android.annotation.Size(6)
        public float[] getPrimaries() {
            return java.util.Arrays.copyOf(mPrimaries, mPrimaries.length);
        }

        /**
         * <p>Copies the transform of this color space in specified array. The
         * transform is used to convert from RGB to XYZ (with the same white
         * point as this color space). To connect color spaces, you must first
         * {@link ColorSpace#adapt(ColorSpace, float[]) adapt} them to the
         * same white point.</p>
         * <p>It is recommended to use {@link ColorSpace#connect(ColorSpace, ColorSpace)}
         * to convert between color spaces.</p>
         *
         * @param transform
         * 		The destination array, cannot be null, its length
         * 		must be >= 9
         * @return The destination array passed as a parameter
         * @see #getInverseTransform()
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 9)
        public float[] getTransform(@android.annotation.NonNull
        @android.annotation.Size(min = 9)
        float[] transform) {
            java.lang.System.arraycopy(mTransform, 0, transform, 0, mTransform.length);
            return transform;
        }

        /**
         * <p>Returns the transform of this color space as a new array. The
         * transform is used to convert from RGB to XYZ (with the same white
         * point as this color space). To connect color spaces, you must first
         * {@link ColorSpace#adapt(ColorSpace, float[]) adapt} them to the
         * same white point.</p>
         * <p>It is recommended to use {@link ColorSpace#connect(ColorSpace, ColorSpace)}
         * to convert between color spaces.</p>
         *
         * @return A new array of 9 floats
         * @see #getInverseTransform(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(9)
        public float[] getTransform() {
            return java.util.Arrays.copyOf(mTransform, mTransform.length);
        }

        /**
         * <p>Copies the inverse transform of this color space in specified array.
         * The inverse transform is used to convert from XYZ to RGB (with the
         * same white point as this color space). To connect color spaces, you
         * must first {@link ColorSpace#adapt(ColorSpace, float[]) adapt} them
         * to the same white point.</p>
         * <p>It is recommended to use {@link ColorSpace#connect(ColorSpace, ColorSpace)}
         * to convert between color spaces.</p>
         *
         * @param inverseTransform
         * 		The destination array, cannot be null, its length
         * 		must be >= 9
         * @return The destination array passed as a parameter
         * @see #getTransform()
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 9)
        public float[] getInverseTransform(@android.annotation.NonNull
        @android.annotation.Size(min = 9)
        float[] inverseTransform) {
            java.lang.System.arraycopy(mInverseTransform, 0, inverseTransform, 0, mInverseTransform.length);
            return inverseTransform;
        }

        /**
         * <p>Returns the inverse transform of this color space as a new array.
         * The inverse transform is used to convert from XYZ to RGB (with the
         * same white point as this color space). To connect color spaces, you
         * must first {@link ColorSpace#adapt(ColorSpace, float[]) adapt} them
         * to the same white point.</p>
         * <p>It is recommended to use {@link ColorSpace#connect(ColorSpace, ColorSpace)}
         * to convert between color spaces.</p>
         *
         * @return A new array of 9 floats
         * @see #getTransform(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(9)
        public float[] getInverseTransform() {
            return java.util.Arrays.copyOf(mInverseTransform, mInverseTransform.length);
        }

        /**
         * <p>Returns the opto-electronic transfer function (OETF) of this color space.
         * The inverse function is the electro-optical transfer function (EOTF) returned
         * by {@link #getEotf()}. These functions are defined to satisfy the following
         * equality for \(x \in [0..1]\):</p>
         *
         * $$OETF(EOTF(x)) = EOTF(OETF(x)) = x$$
         *
         * <p>For RGB colors, this function can be used to convert from linear space
         * to "gamma space" (gamma encoded). The terms gamma space and gamma encoded
         * are frequently used because many OETFs can be closely approximated using
         * a simple power function of the form \(x^{\frac{1}{\gamma}}\) (the
         * approximation of the {@link Named#SRGB sRGB} OETF uses \(\gamma=2.2\)
         * for instance).</p>
         *
         * @return A transfer function that converts from linear space to "gamma space"
         * @see #getEotf()
         * @see #getTransferParameters()
         */
        @android.annotation.NonNull
        public java.util.function.DoubleUnaryOperator getOetf() {
            return mClampedOetf;
        }

        /**
         * <p>Returns the electro-optical transfer function (EOTF) of this color space.
         * The inverse function is the opto-electronic transfer function (OETF)
         * returned by {@link #getOetf()}. These functions are defined to satisfy the
         * following equality for \(x \in [0..1]\):</p>
         *
         * $$OETF(EOTF(x)) = EOTF(OETF(x)) = x$$
         *
         * <p>For RGB colors, this function can be used to convert from "gamma space"
         * (gamma encoded) to linear space. The terms gamma space and gamma encoded
         * are frequently used because many EOTFs can be closely approximated using
         * a simple power function of the form \(x^\gamma\) (the approximation of the
         * {@link Named#SRGB sRGB} EOTF uses \(\gamma=2.2\) for instance).</p>
         *
         * @return A transfer function that converts from "gamma space" to linear space
         * @see #getOetf()
         * @see #getTransferParameters()
         */
        @android.annotation.NonNull
        public java.util.function.DoubleUnaryOperator getEotf() {
            return mClampedEotf;
        }

        /**
         * <p>Returns the parameters used by the {@link #getEotf() electro-optical}
         * and {@link #getOetf() opto-electronic} transfer functions. If the transfer
         * functions do not match the ICC parametric curves defined in ICC.1:2004-10
         * (section 10.15), this method returns null.</p>
         *
         * <p>See {@link TransferParameters} for a full description of the transfer
         * functions.</p>
         *
         * @return An instance of {@link TransferParameters} or null if this color
        space's transfer functions do not match the equation defined in
        {@link TransferParameters}
         */
        @android.annotation.Nullable
        public android.graphics.ColorSpace.Rgb.TransferParameters getTransferParameters() {
            return mTransferParameters;
        }

        @java.lang.Override
        public boolean isSrgb() {
            return mIsSrgb;
        }

        @java.lang.Override
        public boolean isWideGamut() {
            return mIsWideGamut;
        }

        @java.lang.Override
        public float getMinValue(int component) {
            return mMin;
        }

        @java.lang.Override
        public float getMaxValue(int component) {
            return mMax;
        }

        /**
         * <p>Decodes an RGB value to linear space. This is achieved by
         * applying this color space's electro-optical transfer function
         * to the supplied values.</p>
         *
         * <p>Refer to the documentation of {@link ColorSpace.Rgb} for
         * more information about transfer functions and their use for
         * encoding and decoding RGB values.</p>
         *
         * @param r
         * 		The red component to decode to linear space
         * @param g
         * 		The green component to decode to linear space
         * @param b
         * 		The blue component to decode to linear space
         * @return A new array of 3 floats containing linear RGB values
         * @see #toLinear(float[])
         * @see #fromLinear(float, float, float)
         */
        @android.annotation.NonNull
        @android.annotation.Size(3)
        public float[] toLinear(float r, float g, float b) {
            return toLinear(new float[]{ r, g, b });
        }

        /**
         * <p>Decodes an RGB value to linear space. This is achieved by
         * applying this color space's electro-optical transfer function
         * to the first 3 values of the supplied array. The result is
         * stored back in the input array.</p>
         *
         * <p>Refer to the documentation of {@link ColorSpace.Rgb} for
         * more information about transfer functions and their use for
         * encoding and decoding RGB values.</p>
         *
         * @param v
         * 		A non-null array of non-linear RGB values, its length
         * 		must be at least 3
         * @return The specified array
         * @see #toLinear(float, float, float)
         * @see #fromLinear(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 3)
        public float[] toLinear(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = ((float) (mClampedEotf.applyAsDouble(v[0])));
            v[1] = ((float) (mClampedEotf.applyAsDouble(v[1])));
            v[2] = ((float) (mClampedEotf.applyAsDouble(v[2])));
            return v;
        }

        /**
         * <p>Encodes an RGB value from linear space to this color space's
         * "gamma space". This is achieved by applying this color space's
         * opto-electronic transfer function to the supplied values.</p>
         *
         * <p>Refer to the documentation of {@link ColorSpace.Rgb} for
         * more information about transfer functions and their use for
         * encoding and decoding RGB values.</p>
         *
         * @param r
         * 		The red component to encode from linear space
         * @param g
         * 		The green component to encode from linear space
         * @param b
         * 		The blue component to encode from linear space
         * @return A new array of 3 floats containing non-linear RGB values
         * @see #fromLinear(float[])
         * @see #toLinear(float, float, float)
         */
        @android.annotation.NonNull
        @android.annotation.Size(3)
        public float[] fromLinear(float r, float g, float b) {
            return fromLinear(new float[]{ r, g, b });
        }

        /**
         * <p>Encodes an RGB value from linear space to this color space's
         * "gamma space". This is achieved by applying this color space's
         * opto-electronic transfer function to the first 3 values of the
         * supplied array. The result is stored back in the input array.</p>
         *
         * <p>Refer to the documentation of {@link ColorSpace.Rgb} for
         * more information about transfer functions and their use for
         * encoding and decoding RGB values.</p>
         *
         * @param v
         * 		A non-null array of linear RGB values, its length
         * 		must be at least 3
         * @return A new array of 3 floats containing non-linear RGB values
         * @see #fromLinear(float[])
         * @see #toLinear(float, float, float)
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 3)
        public float[] fromLinear(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = ((float) (mClampedOetf.applyAsDouble(v[0])));
            v[1] = ((float) (mClampedOetf.applyAsDouble(v[1])));
            v[2] = ((float) (mClampedOetf.applyAsDouble(v[2])));
            return v;
        }

        @java.lang.Override
        @android.annotation.NonNull
        @android.annotation.Size(min = 3)
        public float[] toXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            v[0] = ((float) (mClampedEotf.applyAsDouble(v[0])));
            v[1] = ((float) (mClampedEotf.applyAsDouble(v[1])));
            v[2] = ((float) (mClampedEotf.applyAsDouble(v[2])));
            return android.graphics.ColorSpace.mul3x3Float3(mTransform, v);
        }

        @java.lang.Override
        @android.annotation.NonNull
        @android.annotation.Size(min = 3)
        public float[] fromXyz(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            android.graphics.ColorSpace.mul3x3Float3(mInverseTransform, v);
            v[0] = ((float) (mClampedOetf.applyAsDouble(v[0])));
            v[1] = ((float) (mClampedOetf.applyAsDouble(v[1])));
            v[2] = ((float) (mClampedOetf.applyAsDouble(v[2])));
            return v;
        }

        private double clamp(double x) {
            return x < mMin ? mMin : x > mMax ? mMax : x;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            if (!super.equals(o))
                return false;

            android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (o));
            if (java.lang.Float.compare(rgb.mMin, mMin) != 0)
                return false;

            if (java.lang.Float.compare(rgb.mMax, mMax) != 0)
                return false;

            if (!java.util.Arrays.equals(mWhitePoint, rgb.mWhitePoint))
                return false;

            if (!java.util.Arrays.equals(mPrimaries, rgb.mPrimaries))
                return false;

            if (mTransferParameters != null) {
                return mTransferParameters.equals(rgb.mTransferParameters);
            } else
                if (rgb.mTransferParameters == null) {
                    return true;
                }

            // noinspection SimplifiableIfStatement
            if (!mOetf.equals(rgb.mOetf))
                return false;

            return mEotf.equals(rgb.mEotf);
        }

        @java.lang.Override
        public int hashCode() {
            int result = super.hashCode();
            result = (31 * result) + java.util.Arrays.hashCode(mWhitePoint);
            result = (31 * result) + java.util.Arrays.hashCode(mPrimaries);
            result = (31 * result) + (mMin != (+0.0F) ? java.lang.Float.floatToIntBits(mMin) : 0);
            result = (31 * result) + (mMax != (+0.0F) ? java.lang.Float.floatToIntBits(mMax) : 0);
            result = (31 * result) + (mTransferParameters != null ? mTransferParameters.hashCode() : 0);
            if (mTransferParameters == null) {
                result = (31 * result) + mOetf.hashCode();
                result = (31 * result) + mEotf.hashCode();
            }
            return result;
        }

        /**
         * Computes whether a color space is the sRGB color space or at least
         * a close approximation.
         *
         * @param primaries
         * 		The set of RGB primaries in xyY as an array of 6 floats
         * @param whitePoint
         * 		The white point in xyY as an array of 2 floats
         * @param OETF
         * 		The opto-electronic transfer function
         * @param EOTF
         * 		The electro-optical transfer function
         * @param min
         * 		The minimum value of the color space's range
         * @param max
         * 		The minimum value of the color space's range
         * @param id
         * 		The ID of the color space
         * @return True if the color space can be considered as the sRGB color space
         * @see #isSrgb()
         */
        @java.lang.SuppressWarnings("RedundantIfStatement")
        private static boolean isSrgb(@android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(2)
        float[] whitePoint, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator OETF, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator EOTF, float min, float max, @android.annotation.IntRange(from = android.graphics.ColorSpace.MIN_ID, to = android.graphics.ColorSpace.MAX_ID)
        int id) {
            if (id == 0)
                return true;

            if (!android.graphics.ColorSpace.compare(primaries, android.graphics.ColorSpace.SRGB_PRIMARIES)) {
                return false;
            }
            if (!android.graphics.ColorSpace.compare(whitePoint, android.graphics.ColorSpace.ILLUMINANT_D65)) {
                return false;
            }
            if (min != 0.0F)
                return false;

            if (max != 1.0F)
                return false;

            // We would have already returned true if this was SRGB itself, so
            // it is safe to reference it here.
            android.graphics.ColorSpace.Rgb srgb = ((android.graphics.ColorSpace.Rgb) (android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB)));
            for (double x = 0.0; x <= 1.0; x += 1 / 255.0) {
                if (!android.graphics.ColorSpace.Rgb.compare(x, OETF, srgb.mOetf))
                    return false;

                if (!android.graphics.ColorSpace.Rgb.compare(x, EOTF, srgb.mEotf))
                    return false;

            }
            return true;
        }

        private static boolean compare(double point, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator a, @android.annotation.NonNull
        java.util.function.DoubleUnaryOperator b) {
            double rA = a.applyAsDouble(point);
            double rB = b.applyAsDouble(point);
            return java.lang.Math.abs(rA - rB) <= 0.001;
        }

        /**
         * Computes whether the specified CIE xyY or XYZ primaries (with Y set to 1) form
         * a wide color gamut. A color gamut is considered wide if its area is &gt; 90%
         * of the area of NTSC 1953 and if it contains the sRGB color gamut entirely.
         * If the conditions above are not met, the color space is considered as having
         * a wide color gamut if its range is larger than [0..1].
         *
         * @param primaries
         * 		RGB primaries in CIE xyY as an array of 6 floats
         * @param min
         * 		The minimum value of the color space's range
         * @param max
         * 		The minimum value of the color space's range
         * @return True if the color space has a wide gamut, false otherwise
         * @see #isWideGamut()
         * @see #area(float[])
         */
        private static boolean isWideGamut(@android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries, float min, float max) {
            return (((android.graphics.ColorSpace.Rgb.area(primaries) / android.graphics.ColorSpace.Rgb.area(android.graphics.ColorSpace.NTSC_1953_PRIMARIES)) > 0.9F) && android.graphics.ColorSpace.Rgb.contains(primaries, android.graphics.ColorSpace.SRGB_PRIMARIES)) || ((min < 0.0F) && (max > 1.0F));
        }

        /**
         * Computes the area of the triangle represented by a set of RGB primaries
         * in the CIE xyY space.
         *
         * @param primaries
         * 		The triangle's vertices, as RGB primaries in an array of 6 floats
         * @return The area of the triangle
         * @see #isWideGamut(float[], float, float)
         */
        private static float area(@android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float det = (((((Rx * Gy) + (Ry * Bx)) + (Gx * By)) - (Gy * Bx)) - (Ry * Gx)) - (Rx * By);
            float r = 0.5F * det;
            return r < 0.0F ? -r : r;
        }

        /**
         * Computes the cross product of two 2D vectors.
         *
         * @param ax
         * 		The x coordinate of the first vector
         * @param ay
         * 		The y coordinate of the first vector
         * @param bx
         * 		The x coordinate of the second vector
         * @param by
         * 		The y coordinate of the second vector
         * @return The result of a x b
         */
        private static float cross(float ax, float ay, float bx, float by) {
            return (ax * by) - (ay * bx);
        }

        /**
         * Decides whether a 2D triangle, identified by the 6 coordinates of its
         * 3 vertices, is contained within another 2D triangle, also identified
         * by the 6 coordinates of its 3 vertices.
         *
         * In the illustration below, we want to test whether the RGB triangle
         * is contained within the triangle XYZ formed by the 3 vertices at
         * the "+" locations.
         *
         *                                     Y     .
         *                                 .   +    .
         *                                  .     ..
         *                                   .   .
         *                                    . .
         *                                     .  G
         *                                     *
         *                                    * *
         *                                  **   *
         *                                 *      **
         *                                *         *
         *                              **           *
         *                             *              *
         *                            *                *
         *                          **                  *
         *                         *                     *
         *                        *                       **
         *                      **                          *   R    ...
         *                     *                             *  .....
         *                    *                         ***** ..
         *                  **              ************       .   +
         *              B  *    ************                    .   X
         *           ......*****                                 .
         *     ......    .                                        .
         *             ..
         *        +   .
         *      Z    .
         *
         * RGB is contained within XYZ if all the following conditions are true
         * (with "x" the cross product operator):
         *
         *   -->  -->
         *   GR x RX >= 0
         *   -->  -->
         *   RX x BR >= 0
         *   -->  -->
         *   RG x GY >= 0
         *   -->  -->
         *   GY x RG >= 0
         *   -->  -->
         *   RB x BZ >= 0
         *   -->  -->
         *   BZ x GB >= 0
         *
         * @param p1
         * 		The enclosing triangle
         * @param p2
         * 		The enclosed triangle
         * @return True if the triangle p1 contains the triangle p2
         * @see #isWideGamut(float[], float, float)
         */
        @java.lang.SuppressWarnings("RedundantIfStatement")
        private static boolean contains(@android.annotation.NonNull
        @android.annotation.Size(6)
        float[] p1, @android.annotation.NonNull
        @android.annotation.Size(6)
        float[] p2) {
            // Translate the vertices p1 in the coordinates system
            // with the vertices p2 as the origin
            float[] p0 = new float[]{ p1[0] - p2[0], p1[1] - p2[1], p1[2] - p2[2], p1[3] - p2[3], p1[4] - p2[4], p1[5] - p2[5] };
            // Check the first vertex of p1
            if ((android.graphics.ColorSpace.Rgb.cross(p0[0], p0[1], p2[0] - p2[4], p2[1] - p2[5]) < 0) || (android.graphics.ColorSpace.Rgb.cross(p2[0] - p2[2], p2[1] - p2[3], p0[0], p0[1]) < 0)) {
                return false;
            }
            // Check the second vertex of p1
            if ((android.graphics.ColorSpace.Rgb.cross(p0[2], p0[3], p2[2] - p2[0], p2[3] - p2[1]) < 0) || (android.graphics.ColorSpace.Rgb.cross(p2[2] - p2[4], p2[3] - p2[5], p0[2], p0[3]) < 0)) {
                return false;
            }
            // Check the third vertex of p1
            if ((android.graphics.ColorSpace.Rgb.cross(p0[4], p0[5], p2[4] - p2[2], p2[5] - p2[3]) < 0) || (android.graphics.ColorSpace.Rgb.cross(p2[4] - p2[0], p2[5] - p2[1], p0[4], p0[5]) < 0)) {
                return false;
            }
            return true;
        }

        /**
         * Computes the primaries  of a color space identified only by
         * its RGB->XYZ transform matrix. This method assumes that the
         * range of the color space is [0..1].
         *
         * @param toXYZ
         * 		The color space's 3x3 transform matrix to XYZ
         * @return A new array of 6 floats containing the color space's
        primaries in CIE xyY
         */
        @android.annotation.NonNull
        @android.annotation.Size(6)
        private static float[] computePrimaries(@android.annotation.NonNull
        @android.annotation.Size(9)
        float[] toXYZ) {
            float[] r = android.graphics.ColorSpace.mul3x3Float3(toXYZ, new float[]{ 1.0F, 0.0F, 0.0F });
            float[] g = android.graphics.ColorSpace.mul3x3Float3(toXYZ, new float[]{ 0.0F, 1.0F, 0.0F });
            float[] b = android.graphics.ColorSpace.mul3x3Float3(toXYZ, new float[]{ 0.0F, 0.0F, 1.0F });
            float rSum = (r[0] + r[1]) + r[2];
            float gSum = (g[0] + g[1]) + g[2];
            float bSum = (b[0] + b[1]) + b[2];
            return new float[]{ r[0] / rSum, r[1] / rSum, g[0] / gSum, g[1] / gSum, b[0] / bSum, b[1] / bSum };
        }

        /**
         * Computes the white point of a color space identified only by
         * its RGB->XYZ transform matrix. This method assumes that the
         * range of the color space is [0..1].
         *
         * @param toXYZ
         * 		The color space's 3x3 transform matrix to XYZ
         * @return A new array of 2 floats containing the color space's
        white point in CIE xyY
         */
        @android.annotation.NonNull
        @android.annotation.Size(2)
        private static float[] computeWhitePoint(@android.annotation.NonNull
        @android.annotation.Size(9)
        float[] toXYZ) {
            float[] w = android.graphics.ColorSpace.mul3x3Float3(toXYZ, new float[]{ 1.0F, 1.0F, 1.0F });
            float sum = (w[0] + w[1]) + w[2];
            return new float[]{ w[0] / sum, w[1] / sum };
        }

        /**
         * Converts the specified RGB primaries point to xyY if needed. The primaries
         * can be specified as an array of 6 floats (in CIE xyY) or 9 floats
         * (in CIE XYZ). If no conversion is needed, the input array is copied.
         *
         * @param primaries
         * 		The primaries in xyY or XYZ
         * @return A new array of 6 floats containing the primaries in xyY
         */
        @android.annotation.NonNull
        @android.annotation.Size(6)
        private static float[] xyPrimaries(@android.annotation.NonNull
        @android.annotation.Size(min = 6, max = 9)
        float[] primaries) {
            float[] xyPrimaries = new float[6];
            // XYZ to xyY
            if (primaries.length == 9) {
                float sum;
                sum = (primaries[0] + primaries[1]) + primaries[2];
                xyPrimaries[0] = primaries[0] / sum;
                xyPrimaries[1] = primaries[1] / sum;
                sum = (primaries[3] + primaries[4]) + primaries[5];
                xyPrimaries[2] = primaries[3] / sum;
                xyPrimaries[3] = primaries[4] / sum;
                sum = (primaries[6] + primaries[7]) + primaries[8];
                xyPrimaries[4] = primaries[6] / sum;
                xyPrimaries[5] = primaries[7] / sum;
            } else {
                java.lang.System.arraycopy(primaries, 0, xyPrimaries, 0, 6);
            }
            return xyPrimaries;
        }

        /**
         * Converts the specified white point to xyY if needed. The white point
         * can be specified as an array of 2 floats (in CIE xyY) or 3 floats
         * (in CIE XYZ). If no conversion is needed, the input array is copied.
         *
         * @param whitePoint
         * 		The white point in xyY or XYZ
         * @return A new array of 2 floats containing the white point in xyY
         */
        @android.annotation.NonNull
        @android.annotation.Size(2)
        private static float[] xyWhitePoint(@android.annotation.Size(min = 2, max = 3)
        float[] whitePoint) {
            float[] xyWhitePoint = new float[2];
            // XYZ to xyY
            if (whitePoint.length == 3) {
                float sum = (whitePoint[0] + whitePoint[1]) + whitePoint[2];
                xyWhitePoint[0] = whitePoint[0] / sum;
                xyWhitePoint[1] = whitePoint[1] / sum;
            } else {
                java.lang.System.arraycopy(whitePoint, 0, xyWhitePoint, 0, 2);
            }
            return xyWhitePoint;
        }

        /**
         * Computes the matrix that converts from RGB to XYZ based on RGB
         * primaries and a white point, both specified in the CIE xyY space.
         * The Y component of the primaries and white point is implied to be 1.
         *
         * @param primaries
         * 		The RGB primaries in xyY, as an array of 6 floats
         * @param whitePoint
         * 		The white point in xyY, as an array of 2 floats
         * @return A 3x3 matrix as a new array of 9 floats
         */
        @android.annotation.NonNull
        @android.annotation.Size(9)
        private static float[] computeXYZMatrix(@android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(2)
        float[] whitePoint) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float Wx = whitePoint[0];
            float Wy = whitePoint[1];
            float oneRxRy = (1 - Rx) / Ry;
            float oneGxGy = (1 - Gx) / Gy;
            float oneBxBy = (1 - Bx) / By;
            float oneWxWy = (1 - Wx) / Wy;
            float RxRy = Rx / Ry;
            float GxGy = Gx / Gy;
            float BxBy = Bx / By;
            float WxWy = Wx / Wy;
            float BY = (((oneWxWy - oneRxRy) * (GxGy - RxRy)) - ((WxWy - RxRy) * (oneGxGy - oneRxRy))) / (((oneBxBy - oneRxRy) * (GxGy - RxRy)) - ((BxBy - RxRy) * (oneGxGy - oneRxRy)));
            float GY = ((WxWy - RxRy) - (BY * (BxBy - RxRy))) / (GxGy - RxRy);
            float RY = (1 - GY) - BY;
            float RYRy = RY / Ry;
            float GYGy = GY / Gy;
            float BYBy = BY / By;
            return new float[]{ RYRy * Rx, RY, RYRy * ((1 - Rx) - Ry), GYGy * Gx, GY, GYGy * ((1 - Gx) - Gy), BYBy * Bx, BY, BYBy * ((1 - Bx) - By) };
        }
    }

    /**
     * {@usesMathJax }
     *
     * <p>A connector transforms colors from a source color space to a destination
     * color space.</p>
     *
     * <p>A source color space is connected to a destination color space using the
     * color transform \(C\) computed from their respective transforms noted
     * \(T_{src}\) and \(T_{dst}\) in the following equation:</p>
     *
     * $$C = T^{-1}_{dst} . T_{src}$$
     *
     * <p>The transform \(C\) shown above is only valid when the source and
     * destination color spaces have the same profile connection space (PCS).
     * We know that instances of {@link ColorSpace} always use CIE XYZ as their
     * PCS but their white points might differ. When they do, we must perform
     * a chromatic adaptation of the color spaces' transforms. To do so, we
     * use the von Kries method described in the documentation of {@link Adaptation},
     * using the CIE standard illuminant {@link ColorSpace#ILLUMINANT_D50 D50}
     * as the target white point.</p>
     *
     * <p>Example of conversion from {@link Named#SRGB sRGB} to
     * {@link Named#DCI_P3 DCI-P3}:</p>
     *
     * <pre class="prettyprint">
     * ColorSpace.Connector connector = ColorSpace.connect(
     *         ColorSpace.get(ColorSpace.Named.SRGB),
     *         ColorSpace.get(ColorSpace.Named.DCI_P3));
     * float[] p3 = connector.transform(1.0f, 0.0f, 0.0f);
     * // p3 contains { 0.9473, 0.2740, 0.2076 }
     * </pre>
     *
     * @see Adaptation
     * @see ColorSpace#adapt(ColorSpace, float[], Adaptation)
     * @see ColorSpace#adapt(ColorSpace, float[])
     * @see ColorSpace#connect(ColorSpace, ColorSpace, RenderIntent)
     * @see ColorSpace#connect(ColorSpace, ColorSpace)
     * @see ColorSpace#connect(ColorSpace, RenderIntent)
     * @see ColorSpace#connect(ColorSpace)
     */
    @android.annotation.AnyThread
    public static class Connector {
        @android.annotation.NonNull
        private final android.graphics.ColorSpace mSource;

        @android.annotation.NonNull
        private final android.graphics.ColorSpace mDestination;

        @android.annotation.NonNull
        private final android.graphics.ColorSpace mTransformSource;

        @android.annotation.NonNull
        private final android.graphics.ColorSpace mTransformDestination;

        @android.annotation.NonNull
        private final android.graphics.ColorSpace.RenderIntent mIntent;

        @android.annotation.NonNull
        @android.annotation.Size(3)
        private final float[] mTransform;

        /**
         * Creates a new connector between a source and a destination color space.
         *
         * @param source
         * 		The source color space, cannot be null
         * @param destination
         * 		The destination color space, cannot be null
         * @param intent
         * 		The render intent to use when compressing gamuts
         */
        Connector(@android.annotation.NonNull
        android.graphics.ColorSpace source, @android.annotation.NonNull
        android.graphics.ColorSpace destination, @android.annotation.NonNull
        android.graphics.ColorSpace.RenderIntent intent) {
            this(source, destination, source.getModel() == android.graphics.ColorSpace.Model.RGB ? android.graphics.ColorSpace.adapt(source, android.graphics.ColorSpace.ILLUMINANT_D50_XYZ) : source, destination.getModel() == android.graphics.ColorSpace.Model.RGB ? android.graphics.ColorSpace.adapt(destination, android.graphics.ColorSpace.ILLUMINANT_D50_XYZ) : destination, intent, android.graphics.ColorSpace.Connector.computeTransform(source, destination, intent));
        }

        /**
         * To connect between color spaces, we might need to use adapted transforms.
         * This should be transparent to the user so this constructor takes the
         * original source and destinations (returned by the getters), as well as
         * possibly adapted color spaces used by transform().
         */
        private Connector(@android.annotation.NonNull
        android.graphics.ColorSpace source, @android.annotation.NonNull
        android.graphics.ColorSpace destination, @android.annotation.NonNull
        android.graphics.ColorSpace transformSource, @android.annotation.NonNull
        android.graphics.ColorSpace transformDestination, @android.annotation.NonNull
        android.graphics.ColorSpace.RenderIntent intent, @android.annotation.Nullable
        @android.annotation.Size(3)
        float[] transform) {
            mSource = source;
            mDestination = destination;
            mTransformSource = transformSource;
            mTransformDestination = transformDestination;
            mIntent = intent;
            mTransform = transform;
        }

        /**
         * Computes an extra transform to apply in XYZ space depending on the
         * selected rendering intent.
         */
        @android.annotation.Nullable
        private static float[] computeTransform(@android.annotation.NonNull
        android.graphics.ColorSpace source, @android.annotation.NonNull
        android.graphics.ColorSpace destination, @android.annotation.NonNull
        android.graphics.ColorSpace.RenderIntent intent) {
            if (intent != android.graphics.ColorSpace.RenderIntent.ABSOLUTE)
                return null;

            boolean srcRGB = source.getModel() == android.graphics.ColorSpace.Model.RGB;
            boolean dstRGB = destination.getModel() == android.graphics.ColorSpace.Model.RGB;
            if (srcRGB && dstRGB)
                return null;

            if (srcRGB || dstRGB) {
                android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) ((srcRGB) ? source : destination));
                float[] srcXYZ = (srcRGB) ? android.graphics.ColorSpace.xyYToXyz(rgb.mWhitePoint) : android.graphics.ColorSpace.ILLUMINANT_D50_XYZ;
                float[] dstXYZ = (dstRGB) ? android.graphics.ColorSpace.xyYToXyz(rgb.mWhitePoint) : android.graphics.ColorSpace.ILLUMINANT_D50_XYZ;
                return new float[]{ srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2] };
            }
            return null;
        }

        /**
         * Returns the source color space this connector will convert from.
         *
         * @return A non-null instance of {@link ColorSpace}
         * @see #getDestination()
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace getSource() {
            return mSource;
        }

        /**
         * Returns the destination color space this connector will convert to.
         *
         * @return A non-null instance of {@link ColorSpace}
         * @see #getSource()
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace getDestination() {
            return mDestination;
        }

        /**
         * Returns the render intent this connector will use when mapping the
         * source color space to the destination color space.
         *
         * @return A non-null {@link RenderIntent}
         * @see RenderIntent
         */
        public android.graphics.ColorSpace.RenderIntent getRenderIntent() {
            return mIntent;
        }

        /**
         * <p>Transforms the specified color from the source color space
         * to a color in the destination color space. This convenience
         * method assumes a source color model with 3 components
         * (typically RGB). To transform from color models with more than
         * 3 components, such as {@link Model#CMYK CMYK}, use
         * {@link #transform(float[])} instead.</p>
         *
         * @param r
         * 		The red component of the color to transform
         * @param g
         * 		The green component of the color to transform
         * @param b
         * 		The blue component of the color to transform
         * @return A new array of 3 floats containing the specified color
        transformed from the source space to the destination space
         * @see #transform(float[])
         */
        @android.annotation.NonNull
        @android.annotation.Size(3)
        public float[] transform(float r, float g, float b) {
            return transform(new float[]{ r, g, b });
        }

        /**
         * <p>Transforms the specified color from the source color space
         * to a color in the destination color space.</p>
         *
         * @param v
         * 		A non-null array of 3 floats containing the value to transform
         * 		and that will hold the result of the transform
         * @return The v array passed as a parameter, containing the specified color
        transformed from the source space to the destination space
         * @see #transform(float, float, float)
         */
        @android.annotation.NonNull
        @android.annotation.Size(min = 3)
        public float[] transform(@android.annotation.NonNull
        @android.annotation.Size(min = 3)
        float[] v) {
            float[] xyz = mTransformSource.toXyz(v);
            if (mTransform != null) {
                xyz[0] *= mTransform[0];
                xyz[1] *= mTransform[1];
                xyz[2] *= mTransform[2];
            }
            return mTransformDestination.fromXyz(xyz);
        }

        /**
         * Optimized connector for RGB->RGB conversions.
         */
        private static class Rgb extends android.graphics.ColorSpace.Connector {
            @android.annotation.NonNull
            private final android.graphics.ColorSpace.Rgb mSource;

            @android.annotation.NonNull
            private final android.graphics.ColorSpace.Rgb mDestination;

            @android.annotation.NonNull
            private final float[] mTransform;

            Rgb(@android.annotation.NonNull
            android.graphics.ColorSpace.Rgb source, @android.annotation.NonNull
            android.graphics.ColorSpace.Rgb destination, @android.annotation.NonNull
            android.graphics.ColorSpace.RenderIntent intent) {
                super(source, destination, source, destination, intent, null);
                mSource = source;
                mDestination = destination;
                mTransform = android.graphics.ColorSpace.Connector.Rgb.computeTransform(source, destination, intent);
            }

            @java.lang.Override
            public float[] transform(@android.annotation.NonNull
            @android.annotation.Size(min = 3)
            float[] rgb) {
                rgb[0] = ((float) (mSource.mClampedEotf.applyAsDouble(rgb[0])));
                rgb[1] = ((float) (mSource.mClampedEotf.applyAsDouble(rgb[1])));
                rgb[2] = ((float) (mSource.mClampedEotf.applyAsDouble(rgb[2])));
                android.graphics.ColorSpace.mul3x3Float3(mTransform, rgb);
                rgb[0] = ((float) (mDestination.mClampedOetf.applyAsDouble(rgb[0])));
                rgb[1] = ((float) (mDestination.mClampedOetf.applyAsDouble(rgb[1])));
                rgb[2] = ((float) (mDestination.mClampedOetf.applyAsDouble(rgb[2])));
                return rgb;
            }

            /**
             * <p>Computes the color transform that connects two RGB color spaces.</p>
             *
             * <p>We can only connect color spaces if they use the same profile
             * connection space. We assume the connection space is always
             * CIE XYZ but we maye need to perform a chromatic adaptation to
             * match the white points. If an adaptation is needed, we use the
             * CIE standard illuminant D50. The unmatched color space is adapted
             * using the von Kries transform and the {@link Adaptation#BRADFORD}
             * matrix.</p>
             *
             * @param source
             * 		The source color space, cannot be null
             * @param destination
             * 		The destination color space, cannot be null
             * @param intent
             * 		The render intent to use when compressing gamuts
             * @return An array of 9 floats containing the 3x3 matrix transform
             */
            @android.annotation.NonNull
            @android.annotation.Size(9)
            private static float[] computeTransform(@android.annotation.NonNull
            android.graphics.ColorSpace.Rgb source, @android.annotation.NonNull
            android.graphics.ColorSpace.Rgb destination, @android.annotation.NonNull
            android.graphics.ColorSpace.RenderIntent intent) {
                if (android.graphics.ColorSpace.compare(source.mWhitePoint, destination.mWhitePoint)) {
                    // RGB->RGB using the PCS of both color spaces since they have the same
                    return android.graphics.ColorSpace.mul3x3(destination.mInverseTransform, source.mTransform);
                } else {
                    // RGB->RGB using CIE XYZ D50 as the PCS
                    float[] transform = source.mTransform;
                    float[] inverseTransform = destination.mInverseTransform;
                    float[] srcXYZ = android.graphics.ColorSpace.xyYToXyz(source.mWhitePoint);
                    float[] dstXYZ = android.graphics.ColorSpace.xyYToXyz(destination.mWhitePoint);
                    if (!android.graphics.ColorSpace.compare(source.mWhitePoint, android.graphics.ColorSpace.ILLUMINANT_D50)) {
                        float[] srcAdaptation = android.graphics.ColorSpace.chromaticAdaptation(android.graphics.ColorSpace.Adaptation.BRADFORD.mTransform, srcXYZ, java.util.Arrays.copyOf(android.graphics.ColorSpace.ILLUMINANT_D50_XYZ, 3));
                        transform = android.graphics.ColorSpace.mul3x3(srcAdaptation, source.mTransform);
                    }
                    if (!android.graphics.ColorSpace.compare(destination.mWhitePoint, android.graphics.ColorSpace.ILLUMINANT_D50)) {
                        float[] dstAdaptation = android.graphics.ColorSpace.chromaticAdaptation(android.graphics.ColorSpace.Adaptation.BRADFORD.mTransform, dstXYZ, java.util.Arrays.copyOf(android.graphics.ColorSpace.ILLUMINANT_D50_XYZ, 3));
                        inverseTransform = android.graphics.ColorSpace.inverse3x3(android.graphics.ColorSpace.mul3x3(dstAdaptation, destination.mTransform));
                    }
                    if (intent == android.graphics.ColorSpace.RenderIntent.ABSOLUTE) {
                        transform = android.graphics.ColorSpace.mul3x3Diag(new float[]{ srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2] }, transform);
                    }
                    return android.graphics.ColorSpace.mul3x3(inverseTransform, transform);
                }
            }
        }

        /**
         * Returns the identity connector for a given color space.
         *
         * @param source
         * 		The source and destination color space
         * @return A non-null connector that does not perform any transform
         * @see ColorSpace#connect(ColorSpace, ColorSpace)
         */
        static android.graphics.ColorSpace.Connector identity(android.graphics.ColorSpace source) {
            return new android.graphics.ColorSpace.Connector(source, source, android.graphics.ColorSpace.RenderIntent.RELATIVE) {
                @java.lang.Override
                public float[] transform(@android.annotation.NonNull
                @android.annotation.Size(min = 3)
                float[] v) {
                    return v;
                }
            };
        }
    }

    /**
     * <p>A color space renderer can be used to visualize and compare the gamut and
     * white point of one or more color spaces. The output is an sRGB {@link Bitmap}
     * showing a CIE 1931 xyY or a CIE 1976 UCS chromaticity diagram.</p>
     *
     * <p>The following code snippet shows how to compare the {@link Named#SRGB}
     * and {@link Named#DCI_P3} color spaces in a CIE 1931 diagram:</p>
     *
     * <pre class="prettyprint">
     * Bitmap bitmap = ColorSpace.createRenderer()
     *     .size(768)
     *     .clip(true)
     *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
     *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
     *     .render();
     * </pre>
     * <p>
     *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_clipped.png" />
     *     <figcaption style="text-align: center;">sRGB vs DCI-P3</figcaption>
     * </p>
     *
     * <p>A renderer can also be used to show the location of specific colors,
     * associated with a color space, in the CIE 1931 xyY chromaticity diagram.
     * See {@link #add(ColorSpace, float, float, float, int)} for more information.</p>
     *
     * @see ColorSpace#createRenderer()
     * @unknown 
     */
    public static class Renderer {
        private static final int NATIVE_SIZE = 1440;

        private static final float UCS_SCALE = 9.0F / 6.0F;

        // Number of subdivision of the inside of the spectral locus
        private static final int CHROMATICITY_RESOLUTION = 32;

        private static final double ONE_THIRD = 1.0 / 3.0;

        @android.annotation.IntRange(from = 128, to = java.lang.Integer.MAX_VALUE)
        private int mSize = 1024;

        private boolean mShowWhitePoint = true;

        private boolean mClip = false;

        private boolean mUcs = false;

        private final java.util.List<android.util.Pair<android.graphics.ColorSpace, java.lang.Integer>> mColorSpaces = new java.util.ArrayList<>(2);

        private final java.util.List<android.graphics.ColorSpace.Renderer.Point> mPoints = new java.util.ArrayList<>(0);

        private Renderer() {
        }

        /**
         * <p>Defines whether the chromaticity diagram should be clipped by the first
         * registered color space. The default value is false.</p>
         *
         * <p>The following code snippet and image show the default behavior:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_comparison.png" />
         *     <figcaption style="text-align: center;">Clipping disabled</figcaption>
         * </p>
         *
         * <p>Here is the same example with clipping enabled:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .clip(true)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_clipped.png" />
         *     <figcaption style="text-align: center;">Clipping enabled</figcaption>
         * </p>
         *
         * @param clip
         * 		True to clip the chromaticity diagram to the first registered color space,
         * 		false otherwise
         * @return This instance of {@link Renderer}
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer clip(boolean clip) {
            mClip = clip;
            return this;
        }

        /**
         * <p>Defines whether the chromaticity diagram should use the uniform
         * chromaticity scale (CIE 1976 UCS). When the uniform chromaticity scale
         * is used, the distance between two points on the diagram is approximately
         * proportional to the perceived color difference.</p>
         *
         * <p>The following code snippet shows how to enable the uniform chromaticity
         * scale. The image below shows the result:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .uniformChromaticityScale(true)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_ucs.png" />
         *     <figcaption style="text-align: center;">CIE 1976 UCS diagram</figcaption>
         * </p>
         *
         * @param ucs
         * 		True to render the chromaticity diagram as the CIE 1976 UCS diagram
         * @return This instance of {@link Renderer}
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer uniformChromaticityScale(boolean ucs) {
            mUcs = ucs;
            return this;
        }

        /**
         * Sets the dimensions (width and height) in pixels of the output bitmap.
         * The size must be at least 128px and defaults to 1024px.
         *
         * @param size
         * 		The size in pixels of the output bitmap
         * @return This instance of {@link Renderer}
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer size(@android.annotation.IntRange(from = 128, to = java.lang.Integer.MAX_VALUE)
        int size) {
            mSize = java.lang.Math.max(128, size);
            return this;
        }

        /**
         * Shows or hides the white point of each color space in the output bitmap.
         * The default is true.
         *
         * @param show
         * 		True to show the white point of each color space, false
         * 		otherwise
         * @return This instance of {@link Renderer}
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer showWhitePoint(boolean show) {
            mShowWhitePoint = show;
            return this;
        }

        /**
         * <p>Adds a color space to represent on the output CIE 1931 chromaticity
         * diagram. The color space is represented as a triangle showing the
         * footprint of its color gamut and, optionally, the location of its
         * white point.</p>
         *
         * <p class="note">Color spaces with a color model that is not RGB are
         * accepted but ignored.</p>
         *
         * <p>The following code snippet and image show an example of calling this
         * method to compare {@link Named#SRGB sRGB} and {@link Named#DCI_P3 DCI-P3}:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_comparison.png" />
         *     <figcaption style="text-align: center;">sRGB vs DCI-P3</figcaption>
         * </p>
         *
         * <p>Adding a color space extending beyond the boundaries of the
         * spectral locus will alter the size of the diagram within the output
         * bitmap as shown in this example:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.DCI_P3), 0xffffc845)
         *     .add(ColorSpace.get(ColorSpace.Named.ACES), 0xff097ae9)
         *     .add(ColorSpace.get(ColorSpace.Named.EXTENDED_SRGB), 0xff000000)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_comparison2.png" />
         *     <figcaption style="text-align: center;">sRGB, DCI-P3, ACES and scRGB</figcaption>
         * </p>
         *
         * @param colorSpace
         * 		The color space whose gamut to render on the diagram
         * @param color
         * 		The sRGB color to use to render the color space's gamut and white point
         * @return This instance of {@link Renderer}
         * @see #clip(boolean)
         * @see #showWhitePoint(boolean)
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer add(@android.annotation.NonNull
        android.graphics.ColorSpace colorSpace, @android.annotation.ColorInt
        int color) {
            mColorSpaces.add(new android.util.Pair(colorSpace, color));
            return this;
        }

        /**
         * <p>Adds a color to represent as a point on the chromaticity diagram.
         * The color is associated with a color space which will be used to
         * perform the conversion to CIE XYZ and compute the location of the point
         * on the diagram. The point is rendered as a colored circle.</p>
         *
         * <p>The following code snippet and image show an example of calling this
         * method to render the location of several sRGB colors as white circles:</p>
         * <pre class="prettyprint">
         * Bitmap bitmap = ColorSpace.createRenderer()
         *     .clip(true)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.0f, 0.1f, 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.1f, 0.1f, 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.2f, 0.1f, 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.3f, 0.1f, 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.4f, 0.1f, 0xffffffff)
         *     .add(ColorSpace.get(ColorSpace.Named.SRGB), 0.1f, 0.5f, 0.1f, 0xffffffff)
         *     .render();
         * </pre>
         * <p>
         *     <img style="display: block; margin: 0 auto;" src="{@docRoot }reference/android/images/graphics/colorspace_points.png" />
         *     <figcaption style="text-align: center;">
         *         Locating colors on the chromaticity diagram
         *     </figcaption>
         * </p>
         *
         * @param colorSpace
         * 		The color space of the color to locate on the diagram
         * @param r
         * 		The first component of the color to locate on the diagram
         * @param g
         * 		The second component of the color to locate on the diagram
         * @param b
         * 		The third component of the color to locate on the diagram
         * @param pointColor
         * 		The sRGB color to use to render the point on the diagram
         * @return This instance of {@link Renderer}
         */
        @android.annotation.NonNull
        public android.graphics.ColorSpace.Renderer add(@android.annotation.NonNull
        android.graphics.ColorSpace colorSpace, float r, float g, float b, @android.annotation.ColorInt
        int pointColor) {
            mPoints.add(new android.graphics.ColorSpace.Renderer.Point(colorSpace, new float[]{ r, g, b }, pointColor));
            return this;
        }

        /**
         * <p>Renders the {@link #add(ColorSpace, int) color spaces} and
         * {@link #add(ColorSpace, float, float, float, int) points} registered
         * with this renderer. The output bitmap is an sRGB image with the
         * dimensions specified by calling {@link #size(int)} (1204x1024px by
         * default).</p>
         *
         * @return A new non-null {@link Bitmap} with the dimensions specified
        by {@link #size(int)} (1024x1024 by default)
         */
        @android.annotation.NonNull
        public android.graphics.Bitmap render() {
            android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(mSize, mSize, android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
            float[] primaries = new float[6];
            float[] whitePoint = new float[2];
            int width = android.graphics.ColorSpace.Renderer.NATIVE_SIZE;
            int height = android.graphics.ColorSpace.Renderer.NATIVE_SIZE;
            android.graphics.Path path = new android.graphics.Path();
            setTransform(canvas, width, height, primaries);
            drawBox(canvas, width, height, paint, path);
            setUcsTransform(canvas, height);
            drawLocus(canvas, width, height, paint, path, primaries);
            drawGamuts(canvas, width, height, paint, path, primaries, whitePoint);
            drawPoints(canvas, width, height, paint);
            return bitmap;
        }

        /**
         * Draws registered points at their correct position in the xyY coordinates.
         * Each point is positioned according to its associated color space.
         *
         * @param canvas
         * 		The canvas to transform
         * @param width
         * 		Width in pixel of the final image
         * @param height
         * 		Height in pixel of the final image
         * @param paint
         * 		A pre-allocated paint used to avoid temporary allocations
         */
        private void drawPoints(@android.annotation.NonNull
        android.graphics.Canvas canvas, int width, int height, @android.annotation.NonNull
        android.graphics.Paint paint) {
            paint.setStyle(android.graphics.Paint.Style.FILL);
            float radius = 4.0F / (mUcs ? android.graphics.ColorSpace.Renderer.UCS_SCALE : 1.0F);
            float[] v = new float[3];
            float[] xy = new float[2];
            for (final android.graphics.ColorSpace.Renderer.Point point : mPoints) {
                v[0] = point.mRgb[0];
                v[1] = point.mRgb[1];
                v[2] = point.mRgb[2];
                point.mColorSpace.toXyz(v);
                paint.setColor(point.mColor);
                // XYZ to xyY, assuming Y=1.0, then to L*u*v* if needed
                float sum = (v[0] + v[1]) + v[2];
                xy[0] = v[0] / sum;
                xy[1] = v[1] / sum;
                if (mUcs)
                    android.graphics.ColorSpace.xyYToUv(xy);

                canvas.drawCircle(width * xy[0], height - (height * xy[1]), radius, paint);
            }
        }

        /**
         * Draws the color gamuts and white points of all the registered color
         * spaces. Only color spaces with an RGB color model are rendered, the
         * others are ignored.
         *
         * @param canvas
         * 		The canvas to transform
         * @param width
         * 		Width in pixel of the final image
         * @param height
         * 		Height in pixel of the final image
         * @param paint
         * 		A pre-allocated paint used to avoid temporary allocations
         * @param path
         * 		A pre-allocated path used to avoid temporary allocations
         * @param primaries
         * 		A pre-allocated array of 6 floats to avoid temporary allocations
         * @param whitePoint
         * 		A pre-allocated array of 2 floats to avoid temporary allocations
         */
        private void drawGamuts(@android.annotation.NonNull
        android.graphics.Canvas canvas, int width, int height, @android.annotation.NonNull
        android.graphics.Paint paint, @android.annotation.NonNull
        android.graphics.Path path, @android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries, @android.annotation.NonNull
        @android.annotation.Size(2)
        float[] whitePoint) {
            float radius = 4.0F / (mUcs ? android.graphics.ColorSpace.Renderer.UCS_SCALE : 1.0F);
            for (final android.util.Pair<android.graphics.ColorSpace, java.lang.Integer> item : mColorSpaces) {
                android.graphics.ColorSpace colorSpace = item.first;
                int color = item.second;
                if (colorSpace.getModel() != android.graphics.ColorSpace.Model.RGB)
                    continue;

                android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (colorSpace));
                android.graphics.ColorSpace.Renderer.getPrimaries(rgb, primaries, mUcs);
                path.rewind();
                path.moveTo(width * primaries[0], height - (height * primaries[1]));
                path.lineTo(width * primaries[2], height - (height * primaries[3]));
                path.lineTo(width * primaries[4], height - (height * primaries[5]));
                path.close();
                paint.setStyle(android.graphics.Paint.Style.STROKE);
                paint.setColor(color);
                canvas.drawPath(path, paint);
                // Draw the white point
                if (mShowWhitePoint) {
                    rgb.getWhitePoint(whitePoint);
                    if (mUcs)
                        android.graphics.ColorSpace.xyYToUv(whitePoint);

                    paint.setStyle(android.graphics.Paint.Style.FILL);
                    paint.setColor(color);
                    canvas.drawCircle(width * whitePoint[0], height - (height * whitePoint[1]), radius, paint);
                }
            }
        }

        /**
         * Returns the primaries of the specified RGB color space. This method handles
         * the special case of the {@link Named#EXTENDED_SRGB} family of color spaces.
         *
         * @param rgb
         * 		The color space whose primaries to extract
         * @param primaries
         * 		A pre-allocated array of 6 floats that will hold the result
         * @param asUcs
         * 		True if the primaries should be returned in Luv, false for xyY
         */
        @android.annotation.NonNull
        @android.annotation.Size(6)
        private static void getPrimaries(@android.annotation.NonNull
        android.graphics.ColorSpace.Rgb rgb, @android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries, boolean asUcs) {
            // TODO: We should find a better way to handle these cases
            if (rgb.equals(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.EXTENDED_SRGB)) || rgb.equals(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB))) {
                primaries[0] = 1.41F;
                primaries[1] = 0.33F;
                primaries[2] = 0.27F;
                primaries[3] = 1.24F;
                primaries[4] = -0.23F;
                primaries[5] = -0.57F;
            } else {
                rgb.getPrimaries(primaries);
            }
            if (asUcs)
                android.graphics.ColorSpace.xyYToUv(primaries);

        }

        /**
         * Draws the CIE 1931 chromaticity diagram: the spectral locus and its inside.
         * This method respect the clip parameter.
         *
         * @param canvas
         * 		The canvas to transform
         * @param width
         * 		Width in pixel of the final image
         * @param height
         * 		Height in pixel of the final image
         * @param paint
         * 		A pre-allocated paint used to avoid temporary allocations
         * @param path
         * 		A pre-allocated path used to avoid temporary allocations
         * @param primaries
         * 		A pre-allocated array of 6 floats to avoid temporary allocations
         */
        private void drawLocus(@android.annotation.NonNull
        android.graphics.Canvas canvas, int width, int height, @android.annotation.NonNull
        android.graphics.Paint paint, @android.annotation.NonNull
        android.graphics.Path path, @android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries) {
            int vertexCount = (android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X.length * android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION) * 6;
            float[] vertices = new float[vertexCount * 2];
            int[] colors = new int[vertices.length];
            android.graphics.ColorSpace.Renderer.computeChromaticityMesh(vertices, colors);
            if (mUcs)
                android.graphics.ColorSpace.xyYToUv(vertices);

            for (int i = 0; i < vertices.length; i += 2) {
                vertices[i] *= width;
                vertices[i + 1] = height - (vertices[i + 1] * height);
            }
            // Draw the spectral locus
            if (mClip && (mColorSpaces.size() > 0)) {
                for (final android.util.Pair<android.graphics.ColorSpace, java.lang.Integer> item : mColorSpaces) {
                    android.graphics.ColorSpace colorSpace = item.first;
                    if (colorSpace.getModel() != android.graphics.ColorSpace.Model.RGB)
                        continue;

                    android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (colorSpace));
                    android.graphics.ColorSpace.Renderer.getPrimaries(rgb, primaries, mUcs);
                    break;
                }
                path.rewind();
                path.moveTo(width * primaries[0], height - (height * primaries[1]));
                path.lineTo(width * primaries[2], height - (height * primaries[3]));
                path.lineTo(width * primaries[4], height - (height * primaries[5]));
                path.close();
                int[] solid = new int[colors.length];
                java.util.Arrays.fill(solid, 0xff6c6c6c);
                canvas.drawVertices(android.graphics.Canvas.VertexMode.TRIANGLES, vertices.length, vertices, 0, null, 0, solid, 0, null, 0, 0, paint);
                canvas.save();
                canvas.clipPath(path);
                canvas.drawVertices(android.graphics.Canvas.VertexMode.TRIANGLES, vertices.length, vertices, 0, null, 0, colors, 0, null, 0, 0, paint);
                canvas.restore();
            } else {
                canvas.drawVertices(android.graphics.Canvas.VertexMode.TRIANGLES, vertices.length, vertices, 0, null, 0, colors, 0, null, 0, 0, paint);
            }
            // Draw the non-spectral locus
            int index = (android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION - 1) * 12;
            path.reset();
            path.moveTo(vertices[index], vertices[index + 1]);
            for (int x = 2; x < android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X.length; x++) {
                index += android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION * 12;
                path.lineTo(vertices[index], vertices[index + 1]);
            }
            path.close();
            paint.setStrokeWidth(4.0F / (mUcs ? android.graphics.ColorSpace.Renderer.UCS_SCALE : 1.0F));
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            paint.setColor(0xff000000);
            canvas.drawPath(path, paint);
        }

        /**
         * Draws the diagram box, including borders, tick marks, grid lines
         * and axis labels.
         *
         * @param canvas
         * 		The canvas to transform
         * @param width
         * 		Width in pixel of the final image
         * @param height
         * 		Height in pixel of the final image
         * @param paint
         * 		A pre-allocated paint used to avoid temporary allocations
         * @param path
         * 		A pre-allocated path used to avoid temporary allocations
         */
        private void drawBox(@android.annotation.NonNull
        android.graphics.Canvas canvas, int width, int height, @android.annotation.NonNull
        android.graphics.Paint paint, @android.annotation.NonNull
        android.graphics.Path path) {
            int lineCount = 10;
            float scale = 1.0F;
            if (mUcs) {
                lineCount = 7;
                scale = android.graphics.ColorSpace.Renderer.UCS_SCALE;
            }
            // Draw the unit grid
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            paint.setStrokeWidth(2.0F);
            paint.setColor(0xffc0c0c0);
            for (int i = 1; i < (lineCount - 1); i++) {
                float v = i / 10.0F;
                float x = (width * v) * scale;
                float y = height - ((height * v) * scale);
                canvas.drawLine(0.0F, y, 0.9F * width, y, paint);
                canvas.drawLine(x, height, x, 0.1F * height, paint);
            }
            // Draw tick marks
            paint.setStrokeWidth(4.0F);
            paint.setColor(0xff000000);
            for (int i = 1; i < (lineCount - 1); i++) {
                float v = i / 10.0F;
                float x = (width * v) * scale;
                float y = height - ((height * v) * scale);
                canvas.drawLine(0.0F, y, width / 100.0F, y, paint);
                canvas.drawLine(x, height, x, height - (height / 100.0F), paint);
            }
            // Draw the axis labels
            paint.setStyle(android.graphics.Paint.Style.FILL);
            paint.setTextSize(36.0F);
            paint.setTypeface(android.graphics.Typeface.create("sans-serif-light", android.graphics.Typeface.NORMAL));
            android.graphics.Rect bounds = new android.graphics.Rect();
            for (int i = 1; i < (lineCount - 1); i++) {
                java.lang.String text = "0." + i;
                paint.getTextBounds(text, 0, text.length(), bounds);
                float v = i / 10.0F;
                float x = (width * v) * scale;
                float y = height - ((height * v) * scale);
                canvas.drawText(text, ((-0.05F) * width) + 10, y + (bounds.height() / 2.0F), paint);
                canvas.drawText(text, x - (bounds.width() / 2.0F), (height + bounds.height()) + 16, paint);
            }
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            // Draw the diagram box
            path.moveTo(0.0F, height);
            path.lineTo(0.9F * width, height);
            path.lineTo(0.9F * width, 0.1F * height);
            path.lineTo(0.0F, 0.1F * height);
            path.close();
            canvas.drawPath(path, paint);
        }

        /**
         * Computes and applies the Canvas transforms required to make the color
         * gamut of each color space visible in the final image.
         *
         * @param canvas
         * 		The canvas to transform
         * @param width
         * 		Width in pixel of the final image
         * @param height
         * 		Height in pixel of the final image
         * @param primaries
         * 		Array of 6 floats used to avoid temporary allocations
         */
        private void setTransform(@android.annotation.NonNull
        android.graphics.Canvas canvas, int width, int height, @android.annotation.NonNull
        @android.annotation.Size(6)
        float[] primaries) {
            android.graphics.RectF primariesBounds = new android.graphics.RectF();
            for (final android.util.Pair<android.graphics.ColorSpace, java.lang.Integer> item : mColorSpaces) {
                android.graphics.ColorSpace colorSpace = item.first;
                if (colorSpace.getModel() != android.graphics.ColorSpace.Model.RGB)
                    continue;

                android.graphics.ColorSpace.Rgb rgb = ((android.graphics.ColorSpace.Rgb) (colorSpace));
                android.graphics.ColorSpace.Renderer.getPrimaries(rgb, primaries, mUcs);
                primariesBounds.left = java.lang.Math.min(primariesBounds.left, primaries[4]);
                primariesBounds.top = java.lang.Math.min(primariesBounds.top, primaries[5]);
                primariesBounds.right = java.lang.Math.max(primariesBounds.right, primaries[0]);
                primariesBounds.bottom = java.lang.Math.max(primariesBounds.bottom, primaries[3]);
            }
            float max = (mUcs) ? 0.6F : 0.9F;
            primariesBounds.left = java.lang.Math.min(0.0F, primariesBounds.left);
            primariesBounds.top = java.lang.Math.min(0.0F, primariesBounds.top);
            primariesBounds.right = java.lang.Math.max(max, primariesBounds.right);
            primariesBounds.bottom = java.lang.Math.max(max, primariesBounds.bottom);
            float scaleX = max / primariesBounds.width();
            float scaleY = max / primariesBounds.height();
            float scale = java.lang.Math.min(scaleX, scaleY);
            canvas.scale(mSize / ((float) (android.graphics.ColorSpace.Renderer.NATIVE_SIZE)), mSize / ((float) (android.graphics.ColorSpace.Renderer.NATIVE_SIZE)));
            canvas.scale(scale, scale);
            canvas.translate(((primariesBounds.width() - max) * width) / 2.0F, ((primariesBounds.height() - max) * height) / 2.0F);
            // The spectrum extends ~0.85 vertically and ~0.65 horizontally
            // We shift the canvas a little bit to get nicer margins
            canvas.translate(0.05F * width, (-0.05F) * height);
        }

        /**
         * Computes and applies the Canvas transforms required to render the CIE
         * 197 UCS chromaticity diagram.
         *
         * @param canvas
         * 		The canvas to transform
         * @param height
         * 		Height in pixel of the final image
         */
        private void setUcsTransform(@android.annotation.NonNull
        android.graphics.Canvas canvas, int height) {
            if (mUcs) {
                canvas.translate(0.0F, height - (height * android.graphics.ColorSpace.Renderer.UCS_SCALE));
                canvas.scale(android.graphics.ColorSpace.Renderer.UCS_SCALE, android.graphics.ColorSpace.Renderer.UCS_SCALE);
            }
        }

        // X coordinates of the spectral locus in CIE 1931
        private static final float[] SPECTRUM_LOCUS_X = new float[]{ 0.175596F, 0.172787F, 0.170806F, 0.170085F, 0.160343F, 0.146958F, 0.139149F, 0.133536F, 0.126688F, 0.11583F, 0.109616F, 0.099146F, 0.09131F, 0.07813F, 0.068717F, 0.054675F, 0.040763F, 0.027497F, 0.01627F, 0.008169F, 0.004876F, 0.003983F, 0.003859F, 0.004646F, 0.007988F, 0.01387F, 0.022244F, 0.027273F, 0.03282F, 0.038851F, 0.045327F, 0.052175F, 0.059323F, 0.066713F, 0.074299F, 0.089937F, 0.114155F, 0.138695F, 0.154714F, 0.192865F, 0.229607F, 0.26576F, 0.301588F, 0.337346F, 0.373083F, 0.408717F, 0.444043F, 0.478755F, 0.512467F, 0.544767F, 0.575132F, 0.602914F, 0.627018F, 0.648215F, 0.665746F, 0.680061F, 0.691487F, 0.700589F, 0.707901F, 0.714015F, 0.719017F, 0.723016F, 0.734674F, 0.717203F, 0.699732F, 0.68226F, 0.664789F, 0.647318F, 0.629847F, 0.612376F, 0.594905F, 0.577433F, 0.559962F, 0.542491F, 0.52502F, 0.507549F, 0.490077F, 0.472606F, 0.455135F, 0.437664F, 0.420193F, 0.402721F, 0.38525F, 0.367779F, 0.350308F, 0.332837F, 0.315366F, 0.297894F, 0.280423F, 0.262952F, 0.245481F, 0.22801F, 0.210538F, 0.193067F, 0.175596F };

        // Y coordinates of the spectral locus in CIE 1931
        private static final float[] SPECTRUM_LOCUS_Y = new float[]{ 0.005295F, 0.0048F, 0.005472F, 0.005976F, 0.014496F, 0.026643F, 0.035211F, 0.042704F, 0.053441F, 0.073601F, 0.086866F, 0.112037F, 0.132737F, 0.170464F, 0.200773F, 0.254155F, 0.317049F, 0.387997F, 0.463035F, 0.538504F, 0.587196F, 0.610526F, 0.654897F, 0.67597F, 0.715407F, 0.750246F, 0.779682F, 0.792153F, 0.802971F, 0.812059F, 0.81943F, 0.8252F, 0.82946F, 0.832306F, 0.833833F, 0.833316F, 0.826231F, 0.814796F, 0.805884F, 0.781648F, 0.754347F, 0.724342F, 0.692326F, 0.658867F, 0.62447F, 0.589626F, 0.554734F, 0.520222F, 0.486611F, 0.454454F, 0.424252F, 0.396516F, 0.37251F, 0.351413F, 0.334028F, 0.319765F, 0.308359F, 0.299317F, 0.292044F, 0.285945F, 0.280951F, 0.276964F, 0.265326F, 0.2572F, 0.249074F, 0.240948F, 0.232822F, 0.224696F, 0.21657F, 0.208444F, 0.200318F, 0.192192F, 0.184066F, 0.17594F, 0.167814F, 0.159688F, 0.151562F, 0.143436F, 0.135311F, 0.127185F, 0.119059F, 0.110933F, 0.102807F, 0.094681F, 0.086555F, 0.078429F, 0.070303F, 0.062177F, 0.054051F, 0.045925F, 0.037799F, 0.029673F, 0.021547F, 0.013421F, 0.005295F };

        /**
         * Computes a 2D mesh representation of the CIE 1931 chromaticity
         * diagram.
         *
         * @param vertices
         * 		Array of floats that will hold the mesh vertices
         * @param colors
         * 		Array of floats that will hold the mesh colors
         */
        private static void computeChromaticityMesh(@android.annotation.NonNull
        float[] vertices, @android.annotation.NonNull
        int[] colors) {
            android.graphics.ColorSpace colorSpace = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB);
            float[] color = new float[3];
            int vertexIndex = 0;
            int colorIndex = 0;
            for (int x = 0; x < android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X.length; x++) {
                int nextX = (x % (android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X.length - 1)) + 1;
                float a1 = ((float) (java.lang.Math.atan2(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_Y[x] - android.graphics.ColorSpace.Renderer.ONE_THIRD, android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X[x] - android.graphics.ColorSpace.Renderer.ONE_THIRD)));
                float a2 = ((float) (java.lang.Math.atan2(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_Y[nextX] - android.graphics.ColorSpace.Renderer.ONE_THIRD, android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X[nextX] - android.graphics.ColorSpace.Renderer.ONE_THIRD)));
                float radius1 = ((float) (java.lang.Math.pow(android.graphics.ColorSpace.Renderer.sqr(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X[x] - android.graphics.ColorSpace.Renderer.ONE_THIRD) + android.graphics.ColorSpace.Renderer.sqr(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_Y[x] - android.graphics.ColorSpace.Renderer.ONE_THIRD), 0.5)));
                float radius2 = ((float) (java.lang.Math.pow(android.graphics.ColorSpace.Renderer.sqr(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_X[nextX] - android.graphics.ColorSpace.Renderer.ONE_THIRD) + android.graphics.ColorSpace.Renderer.sqr(android.graphics.ColorSpace.Renderer.SPECTRUM_LOCUS_Y[nextX] - android.graphics.ColorSpace.Renderer.ONE_THIRD), 0.5)));
                // Compute patches; each patch is a quad with a different
                // color associated with each vertex
                for (int c = 1; c <= android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION; c++) {
                    float f1 = c / ((float) (android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION));
                    float f2 = (c - 1) / ((float) (android.graphics.ColorSpace.Renderer.CHROMATICITY_RESOLUTION));
                    double cr1 = radius1 * java.lang.Math.cos(a1);
                    double sr1 = radius1 * java.lang.Math.sin(a1);
                    double cr2 = radius2 * java.lang.Math.cos(a2);
                    double sr2 = radius2 * java.lang.Math.sin(a2);
                    // Compute the XYZ coordinates of the 4 vertices of the patch
                    float v1x = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (cr1 * f1)));
                    float v1y = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (sr1 * f1)));
                    float v1z = (1 - v1x) - v1y;
                    float v2x = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (cr1 * f2)));
                    float v2y = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (sr1 * f2)));
                    float v2z = (1 - v2x) - v2y;
                    float v3x = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (cr2 * f2)));
                    float v3y = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (sr2 * f2)));
                    float v3z = (1 - v3x) - v3y;
                    float v4x = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (cr2 * f1)));
                    float v4y = ((float) (android.graphics.ColorSpace.Renderer.ONE_THIRD + (sr2 * f1)));
                    float v4z = (1 - v4x) - v4y;
                    // Compute the sRGB representation of each XYZ coordinate of the patch
                    colors[colorIndex] = android.graphics.ColorSpace.Renderer.computeColor(color, v1x, v1y, v1z, colorSpace);
                    colors[colorIndex + 1] = android.graphics.ColorSpace.Renderer.computeColor(color, v2x, v2y, v2z, colorSpace);
                    colors[colorIndex + 2] = android.graphics.ColorSpace.Renderer.computeColor(color, v3x, v3y, v3z, colorSpace);
                    colors[colorIndex + 3] = colors[colorIndex];
                    colors[colorIndex + 4] = colors[colorIndex + 2];
                    colors[colorIndex + 5] = android.graphics.ColorSpace.Renderer.computeColor(color, v4x, v4y, v4z, colorSpace);
                    colorIndex += 6;
                    // Flip the mesh upside down to match Canvas' coordinates system
                    vertices[vertexIndex++] = v1x;
                    vertices[vertexIndex++] = v1y;
                    vertices[vertexIndex++] = v2x;
                    vertices[vertexIndex++] = v2y;
                    vertices[vertexIndex++] = v3x;
                    vertices[vertexIndex++] = v3y;
                    vertices[vertexIndex++] = v1x;
                    vertices[vertexIndex++] = v1y;
                    vertices[vertexIndex++] = v3x;
                    vertices[vertexIndex++] = v3y;
                    vertices[vertexIndex++] = v4x;
                    vertices[vertexIndex++] = v4y;
                }
            }
        }

        @android.annotation.ColorInt
        private static int computeColor(@android.annotation.NonNull
        @android.annotation.Size(3)
        float[] color, float x, float y, float z, @android.annotation.NonNull
        android.graphics.ColorSpace cs) {
            color[0] = x;
            color[1] = y;
            color[2] = z;
            cs.fromXyz(color);
            return ((0xff000000 | ((((int) (color[0] * 255.0F)) & 0xff) << 16)) | ((((int) (color[1] * 255.0F)) & 0xff) << 8)) | (((int) (color[2] * 255.0F)) & 0xff);
        }

        private static double sqr(double v) {
            return v * v;
        }

        private static class Point {
            @android.annotation.NonNull
            final android.graphics.ColorSpace mColorSpace;

            @android.annotation.NonNull
            final float[] mRgb;

            final int mColor;

            Point(@android.annotation.NonNull
            android.graphics.ColorSpace colorSpace, @android.annotation.NonNull
            @android.annotation.Size(3)
            float[] rgb, @android.annotation.ColorInt
            int color) {
                mColorSpace = colorSpace;
                mRgb = rgb;
                mColor = color;
            }
        }
    }
}

