/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * <p>The <code>Color</code> class provides methods for creating, converting and
 * manipulating colors. Colors have three different representations:</p>
 * <ul>
 *     <li>Color ints, the most common representation</li>
 *     <li>Color longs</li>
 *     <li><code>Color</code> instances</li>
 * </ul>
 * <p>The section below describe each representation in detail.</p>
 *
 * <h3>Color ints</h3>
 * <p>Color ints are the most common representation of colors on Android and
 * have been used since {@link android.os.Build.VERSION_CODES#BASE API level 1}.</p>
 *
 * <p>A color int always defines a color in the {@link ColorSpace.Named#SRGB sRGB}
 * color space using 4 components packed in a single 32 bit integer value:</p>
 *
 * <table summary="Color int definition">
 *     <tr>
 *         <th>Component</th><th>Name</th><th>Size</th><th>Range</th>
 *     </tr>
 *     <tr><td>A</td><td>Alpha</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>R</td><td>Red</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>G</td><td>Green</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>B</td><td>Blue</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 * </table>
 *
 * <p>The components in this table are listed in encoding order (see below),
 * which is why color ints are called ARGB colors.</p>
 *
 * <h4>Usage in code</h4>
 * <p>To avoid confusing color ints with arbitrary integer values, it is a
 * good practice to annotate them with the <code>@ColorInt</code> annotation
 * found in the Android Support Library.</p>
 *
 * <h4>Encoding</h4>
 * <p>The four components of a color int are encoded in the following way:</p>
 * <pre class="prettyprint">
 * int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
 * </pre>
 *
 * <p>Because of this encoding, color ints can easily be described as an integer
 * constant in source. For instance, opaque blue is <code>0xff0000ff</code>
 * and yellow is <code>0xffffff00</code>.</p>
 *
 * <p>To easily encode color ints, it is recommended to use the static methods
 * {@link #argb(int, int, int, int)} and {@link #rgb(int, int, int)}. The second
 * method omits the alpha component and assumes the color is opaque (alpha is 255).
 * As a convenience this class also offers methods to encode color ints from components
 * defined in the \([0..1]\) range: {@link #argb(float, float, float, float)} and
 * {@link #rgb(float, float, float)}.</p>
 *
 * <p>Color longs (defined below) can be easily converted to color ints by invoking
 * the {@link #toArgb(long)} method. This method performs a color space conversion
 * if needed.</p>
 *
 * <p>It is also possible to create a color int by invoking the method {@link #toArgb()}
 * on a color instance.</p>
 *
 * <h4>Decoding</h4>
 * <p>The four ARGB components can be individually extracted from a color int
 * using the following expressions:</p>
 * <pre class="prettyprint">
 * int A = (color >> 24) & 0xff; // or color >>> 24
 * int R = (color >> 16) & 0xff;
 * int G = (color >>  8) & 0xff;
 * int B = (color      ) & 0xff;
 * </pre>
 *
 * <p>This class offers convenience methods to easily extract these components:</p>
 * <ul>
 *     <li>{@link #alpha(int)} to extract the alpha component</li>
 *     <li>{@link #red(int)} to extract the red component</li>
 *     <li>{@link #green(int)} to extract the green component</li>
 *     <li>{@link #blue(int)} to extract the blue component</li>
 * </ul>
 *
 * <h3>Color longs</h3>
 * <p>Color longs are a representation introduced in
 * {@link android.os.Build.VERSION_CODES#O Android O} to store colors in different
 * {@link ColorSpace color spaces}, with more precision than color ints.</p>
 *
 * <p>A color long always defines a color using 4 components packed in a single
 * 64 bit long value. One of these components is always alpha while the other
 * three components depend on the color space's {@link ColorSpace.Model color model}.
 * The most common color model is the {@link ColorSpace.Model#RGB RGB} model in
 * which the components represent red, green and blue values.</p>
 *
 * <p class="note"><b>Component ranges:</b> the ranges defined in the tables
 * below indicate the ranges that can be encoded in a color long. They do not
 * represent the actual ranges as they may differ per color space. For instance,
 * the RGB components of a color in the {@link ColorSpace.Named#DISPLAY_P3 Display P3}
 * color space use the \([0..1]\) range. Please refer to the documentation of the
 * various {@link ColorSpace.Named color spaces} to find their respective ranges.</p>
 *
 * <p class="note"><b>Alpha range:</b> while alpha is encoded in a color long using
 * a 10 bit integer (thus using a range of \([0..1023]\)), it is converted to and
 * from \([0..1]\) float values when decoding and encoding color longs.</p>
 *
 * <p class="note"><b>sRGB color space:</b> for compatibility reasons and ease of
 * use, color longs encoding {@link ColorSpace.Named#SRGB sRGB} colors do not
 * use the same encoding as other color longs.</p>
 *
 * <table summary="Color long definition">
 *     <tr>
 *         <th>Component</th><th>Name</th><th>Size</th><th>Range</th>
 *     </tr>
 *     <tr><td colspan="4">{@link ColorSpace.Model#RGB RGB} color model</td></tr>
 *     <tr><td>R</td><td>Red</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>G</td><td>Green</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>B</td><td>Blue</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>A</td><td>Alpha</td><td>10 bits</td><td>\([0..1023]\)</td></tr>
 *     <tr><td></td><td>Color space</td><td>6 bits</td><td>\([0..63]\)</td></tr>
 *     <tr><td colspan="4">{@link ColorSpace.Named#SRGB sRGB} color space</td></tr>
 *     <tr><td>A</td><td>Alpha</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>R</td><td>Red</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>G</td><td>Green</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>B</td><td>Blue</td><td>8 bits</td><td>\([0..255]\)</td></tr>
 *     <tr><td>X</td><td>Unused</td><td>32 bits</td><td>\(0\)</td></tr>
 *     <tr><td colspan="4">{@link ColorSpace.Model#XYZ XYZ} color model</td></tr>
 *     <tr><td>X</td><td>X</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>Y</td><td>Y</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>Z</td><td>Z</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>A</td><td>Alpha</td><td>10 bits</td><td>\([0..1023]\)</td></tr>
 *     <tr><td></td><td>Color space</td><td>6 bits</td><td>\([0..63]\)</td></tr>
 *     <tr><td colspan="4">{@link ColorSpace.Model#XYZ Lab} color model</td></tr>
 *     <tr><td>L</td><td>L</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>a</td><td>a</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>b</td><td>b</td><td>16 bits</td><td>\([-65504.0, 65504.0]\)</td></tr>
 *     <tr><td>A</td><td>Alpha</td><td>10 bits</td><td>\([0..1023]\)</td></tr>
 *     <tr><td></td><td>Color space</td><td>6 bits</td><td>\([0..63]\)</td></tr>
 *     <tr><td colspan="4">{@link ColorSpace.Model#CMYK CMYK} color model</td></tr>
 *     <tr><td colspan="4">Unsupported</td></tr>
 * </table>
 *
 * <p>The components in this table are listed in encoding order (see below),
 * which is why color longs in the RGB model are called RGBA colors (even if
 * this doesn't quite hold for the special case of sRGB colors).</p>
 *
 * <p>The color long encoding relies on half-precision float values (fp16). If you
 * wish to know more about the limitations of half-precision float values, please
 * refer to the documentation of the {@link Half} class.</p>
 *
 * <h4>Usage in code</h4>
 * <p>To avoid confusing color longs with arbitrary long values, it is a
 * good practice to annotate them with the <code>@ColorLong</code> annotation
 * found in the Android Support Library.</p>
 *
 * <h4>Encoding</h4>
 *
 * <p>Given the complex nature of color longs, it is strongly encouraged to use
 * the various methods provided by this class to encode them.</p>
 *
 * <p>The most flexible way to encode a color long is to use the method
 * {@link #pack(float, float, float, float, ColorSpace)}. This method allows you
 * to specify three color components (typically RGB), an alpha component and a
 * color space. To encode sRGB colors, use {@link #pack(float, float, float)}
 * and {@link #pack(float, float, float, float)} which are the
 * equivalent of {@link #rgb(int, int, int)} and {@link #argb(int, int, int, int)}
 * for color ints. If you simply need to convert a color int into a color long,
 * use {@link #pack(int)}.</p>
 *
 * <p>It is also possible to create a color long value by invoking the method
 * {@link #pack()} on a color instance.</p>
 *
 * <h4>Decoding</h4>
 *
 * <p>This class offers convenience methods to easily extract the components
 * of a color long:</p>
 * <ul>
 *     <li>{@link #alpha(long)} to extract the alpha component</li>
 *     <li>{@link #red(long)} to extract the red/X/L component</li>
 *     <li>{@link #green(long)} to extract the green/Y/a component</li>
 *     <li>{@link #blue(long)} to extract the blue/Z/b component</li>
 * </ul>
 *
 * <p>The values returned by these methods depend on the color space encoded
 * in the color long. The values are however typically in the \([0..1]\) range
 * for RGB colors. Please refer to the documentation of the various
 * {@link ColorSpace.Named color spaces} for the exact ranges.</p>
 *
 * <h3>Color instances</h3>
 * <p>Color instances are a representation introduced in
 * {@link android.os.Build.VERSION_CODES#O Android O} to store colors in different
 * {@link ColorSpace color spaces}, with more precision than both color ints and
 * color longs. Color instances also offer the ability to store more than 4
 * components if necessary.</p>
 *
 * <p>Colors instances are immutable and can be created using one of the various
 * <code>valueOf</code> methods. For instance:</p>
 * <pre class="prettyprint">
 * // sRGB
 * Color opaqueRed = Color.valueOf(0xffff0000); // from a color int
 * Color translucentRed = Color.valueOf(1.0f, 0.0f, 0.0f, 0.5f);
 *
 * // Wide gamut color
 * {@literal @}ColorLong long p3 = pack(1.0f, 1.0f, 0.0f, 1.0f, colorSpaceP3);
 * Color opaqueYellow = Color.valueOf(p3); // from a color long
 *
 * // CIE L*a*b* color space
 * ColorSpace lab = ColorSpace.get(ColorSpace.Named.LAB);
 * Color green = Color.valueOf(100.0f, -128.0f, 128.0f, 1.0f, lab);
 * </pre>
 *
 * <p>Color instances can be converted to color ints ({@link #toArgb()}) or
 * color longs ({@link #pack()}). They also offer easy access to their various
 * components using the following methods:</p>
 * <ul>
 *     <li>{@link #alpha()}, returns the alpha component value</li>
 *     <li>{@link #red()}, returns the red component value (or first
 *     component value in non-RGB models)</li>
 *     <li>{@link #green()}, returns the green component value (or second
 *     component value in non-RGB models)</li>
 *     <li>{@link #blue()}, returns the blue component value (or third
 *     component value in non-RGB models)</li>
 *     <li>{@link #getComponent(int)}, returns a specific component value</li>
 *     <li>{@link #getComponents()}, returns all component values as an array</li>
 * </ul>
 *
 * <h3>Color space conversions</h3>
 * <p>You can convert colors from one color space to another using
 * {@link ColorSpace#connect(ColorSpace, ColorSpace)} and its variants. However,
 * the <code>Color</code> class provides a few convenience methods to simplify
 * the process. Here is a brief description of some of them:</p>
 * <ul>
 *     <li>{@link #convert(ColorSpace)} to convert a color instance in a color
 *     space to a new color instance in a different color space</li>
 *     <li>{@link #convert(float, float, float, float, ColorSpace, ColorSpace)} to
 *     convert a color from a source color space to a destination color space</li>
 *     <li>{@link #convert(long, ColorSpace)} to convert a color long from its
 *     built-in color space to a destination color space</li>
 *     <li>{@link #convert(int, ColorSpace)} to convert a color int from sRGB
 *     to a destination color space</li>
 * </ul>
 *
 * <p>Please refere to the {@link ColorSpace} documentation for more
 * information.</p>
 *
 * <h3>Alpha and transparency</h3>
 * <p>The alpha component of a color defines the level of transparency of a
 * color. When the alpha component is 0, the color is completely transparent.
 * When the alpha is component is 1 (in the \([0..1]\) range) or 255 (in the
 * \([0..255]\) range), the color is completely opaque.</p>
 *
 * <p>The color representations described above do not use pre-multiplied
 * color components (a pre-multiplied color component is a color component
 * that has been multiplied by the value of the alpha component).
 * For instance, the color int representation of opaque red is
 * <code>0xffff0000</code>. For semi-transparent (50%) red, the
 * representation becomes <code>0x80ff0000</code>. The equivalent color
 * instance representations would be <code>(1.0, 0.0, 0.0, 1.0)</code>
 * and <code>(1.0, 0.0, 0.0, 0.5)</code>.</p>
 */
@android.annotation.AnyThread
@android.annotation.SuppressAutoDoc
public class Color {
    @android.annotation.ColorInt
    public static final int BLACK = 0xff000000;

    @android.annotation.ColorInt
    public static final int DKGRAY = 0xff444444;

    @android.annotation.ColorInt
    public static final int GRAY = 0xff888888;

    @android.annotation.ColorInt
    public static final int LTGRAY = 0xffcccccc;

    @android.annotation.ColorInt
    public static final int WHITE = 0xffffffff;

    @android.annotation.ColorInt
    public static final int RED = 0xffff0000;

    @android.annotation.ColorInt
    public static final int GREEN = 0xff00ff00;

    @android.annotation.ColorInt
    public static final int BLUE = 0xff0000ff;

    @android.annotation.ColorInt
    public static final int YELLOW = 0xffffff00;

    @android.annotation.ColorInt
    public static final int CYAN = 0xff00ffff;

    @android.annotation.ColorInt
    public static final int MAGENTA = 0xffff00ff;

    @android.annotation.ColorInt
    public static final int TRANSPARENT = 0;

    @android.annotation.NonNull
    @android.annotation.Size(min = 4, max = 5)
    private final float[] mComponents;

    @android.annotation.NonNull
    private final android.graphics.ColorSpace mColorSpace;

    /**
     * Creates a new color instance set to opaque black in the
     * {@link ColorSpace.Named#SRGB sRGB} color space.
     *
     * @see #valueOf(float, float, float)
     * @see #valueOf(float, float, float, float)
     * @see #valueOf(float, float, float, float, ColorSpace)
     * @see #valueOf(float[], ColorSpace)
     * @see #valueOf(int)
     * @see #valueOf(long)
     */
    public Color() {
        // This constructor is required for compatibility with previous APIs
        mComponents = new float[]{ 0.0F, 0.0F, 0.0F, 1.0F };
        mColorSpace = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB);
    }

    /**
     * Creates a new color instance in the {@link ColorSpace.Named#SRGB sRGB}
     * color space.
     *
     * @param r
     * 		The value of the red channel, must be in [0..1] range
     * @param g
     * 		The value of the green channel, must be in [0..1] range
     * @param b
     * 		The value of the blue channel, must be in [0..1] range
     * @param a
     * 		The value of the alpha channel, must be in [0..1] range
     */
    private Color(float r, float g, float b, float a) {
        this(r, g, b, a, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * Creates a new color instance in the specified color space. The color space
     * must have a 3 components model.
     *
     * @param r
     * 		The value of the red channel, must be in the color space defined range
     * @param g
     * 		The value of the green channel, must be in the color space defined range
     * @param b
     * 		The value of the blue channel, must be in the color space defined range
     * @param a
     * 		The value of the alpha channel, must be in [0..1] range
     * @param colorSpace
     * 		This color's color space, cannot be null
     */
    private Color(float r, float g, float b, float a, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        mComponents = new float[]{ r, g, b, a };
        mColorSpace = colorSpace;
    }

    /**
     * Creates a new color instance in the specified color space.
     *
     * @param components
     * 		An array of color components, plus alpha
     * @param colorSpace
     * 		This color's color space, cannot be null
     */
    private Color(@android.annotation.Size(min = 4, max = 5)
    float[] components, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        mComponents = components;
        mColorSpace = colorSpace;
    }

    /**
     * Returns this color's color space.
     *
     * @return A non-null instance of {@link ColorSpace}
     */
    @android.annotation.NonNull
    public android.graphics.ColorSpace getColorSpace() {
        return mColorSpace;
    }

    /**
     * Returns the color model of this color.
     *
     * @return A non-null {@link ColorSpace.Model}
     */
    public android.graphics.ColorSpace.Model getModel() {
        return mColorSpace.getModel();
    }

    /**
     * Indicates whether this color color is in a wide-gamut color space.
     * See {@link ColorSpace#isWideGamut()} for a definition of a wide-gamut
     * color space.
     *
     * @return True if this color is in a wide-gamut color space, false otherwise
     * @see #isSrgb()
     * @see ColorSpace#isWideGamut()
     */
    public boolean isWideGamut() {
        return getColorSpace().isWideGamut();
    }

    /**
     * Indicates whether this color is in the {@link ColorSpace.Named#SRGB sRGB}
     * color space.
     *
     * @return True if this color is in the sRGB color space, false otherwise
     * @see #isWideGamut()
     */
    public boolean isSrgb() {
        return getColorSpace().isSrgb();
    }

    /**
     * Returns the number of components that form a color value according
     * to this color space's color model, plus one extra component for
     * alpha.
     *
     * @return The integer 4 or 5
     */
    @android.annotation.IntRange(from = 4, to = 5)
    public int getComponentCount() {
        return mColorSpace.getComponentCount() + 1;
    }

    /**
     * Packs this color into a color long. See the documentation of this class
     * for a description of the color long format.
     *
     * @return A color long
     * @throws IllegalArgumentException
     * 		If this color's color space has the id
     * 		{@link ColorSpace#MIN_ID} or if this color has more than 4 components
     */
    @android.annotation.ColorLong
    public long pack() {
        return android.graphics.Color.pack(mComponents[0], mComponents[1], mComponents[2], mComponents[3], mColorSpace);
    }

    /**
     * Converts this color from its color space to the specified color space.
     * The conversion is done using the default rendering intent as specified
     * by {@link ColorSpace#connect(ColorSpace, ColorSpace)}.
     *
     * @param colorSpace
     * 		The destination color space, cannot be null
     * @return A non-null color instance in the specified color space
     */
    @android.annotation.NonNull
    public android.graphics.Color convert(@android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        android.graphics.ColorSpace.Connector connector = android.graphics.ColorSpace.connect(mColorSpace, colorSpace);
        float[] color = new float[]{ mComponents[0], mComponents[1], mComponents[2], mComponents[3] };
        connector.transform(color);
        return new android.graphics.Color(color, colorSpace);
    }

    /**
     * Converts this color to an ARGB color int. A color int is always in
     * the {@link ColorSpace.Named#SRGB sRGB} color space. This implies
     * a color space conversion is applied if needed.
     *
     * @return An ARGB color in the sRGB color space
     */
    @android.annotation.ColorInt
    public int toArgb() {
        if (mColorSpace.isSrgb()) {
            return (((((int) ((mComponents[3] * 255.0F) + 0.5F)) << 24) | (((int) ((mComponents[0] * 255.0F) + 0.5F)) << 16)) | (((int) ((mComponents[1] * 255.0F) + 0.5F)) << 8)) | ((int) ((mComponents[2] * 255.0F) + 0.5F));
        }
        float[] color = new float[]{ mComponents[0], mComponents[1], mComponents[2], mComponents[3] };
        // The transformation saturates the output
        android.graphics.ColorSpace.connect(mColorSpace).transform(color);
        return (((((int) ((color[3] * 255.0F) + 0.5F)) << 24) | (((int) ((color[0] * 255.0F) + 0.5F)) << 16)) | (((int) ((color[1] * 255.0F) + 0.5F)) << 8)) | ((int) ((color[2] * 255.0F) + 0.5F));
    }

    /**
     * <p>Returns the value of the red component in the range defined by this
     * color's color space (see {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}).</p>
     *
     * <p>If this color's color model is not {@link ColorSpace.Model#RGB RGB},
     * calling this method is equivalent to <code>getComponent(0)</code>.</p>
     *
     * @see #alpha()
     * @see #red()
     * @see #green
     * @see #getComponents()
     */
    public float red() {
        return mComponents[0];
    }

    /**
     * <p>Returns the value of the green component in the range defined by this
     * color's color space (see {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}).</p>
     *
     * <p>If this color's color model is not {@link ColorSpace.Model#RGB RGB},
     * calling this method is equivalent to <code>getComponent(1)</code>.</p>
     *
     * @see #alpha()
     * @see #red()
     * @see #green
     * @see #getComponents()
     */
    public float green() {
        return mComponents[1];
    }

    /**
     * <p>Returns the value of the blue component in the range defined by this
     * color's color space (see {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}).</p>
     *
     * <p>If this color's color model is not {@link ColorSpace.Model#RGB RGB},
     * calling this method is equivalent to <code>getComponent(2)</code>.</p>
     *
     * @see #alpha()
     * @see #red()
     * @see #green
     * @see #getComponents()
     */
    public float blue() {
        return mComponents[2];
    }

    /**
     * Returns the value of the alpha component in the range \([0..1]\).
     * Calling this method is equivalent to
     * <code>getComponent(getComponentCount() - 1)</code>.
     *
     * @see #red()
     * @see #green()
     * @see #blue()
     * @see #getComponents()
     * @see #getComponent(int)
     */
    public float alpha() {
        return mComponents[mComponents.length - 1];
    }

    /**
     * Returns this color's components as a new array. The last element of the
     * array is always the alpha component.
     *
     * @return A new, non-null array whose size is equal to {@link #getComponentCount()}
     * @see #getComponent(int)
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 4, max = 5)
    public float[] getComponents() {
        return java.util.Arrays.copyOf(mComponents, mComponents.length);
    }

    /**
     * Copies this color's components in the supplied array. The last element of the
     * array is always the alpha component.
     *
     * @param components
     * 		An array of floats whose size must be at least
     * 		{@link #getComponentCount()}, can be null
     * @return The array passed as a parameter if not null, or a new array of length
    {@link #getComponentCount()}
     * @see #getComponent(int)
     * @throws IllegalArgumentException
     * 		If the specified array's length is less than
     * 		{@link #getComponentCount()}
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 4)
    public float[] getComponents(@android.annotation.Nullable
    @android.annotation.Size(min = 4)
    float[] components) {
        if (components == null) {
            return java.util.Arrays.copyOf(mComponents, mComponents.length);
        }
        if (components.length < mComponents.length) {
            throw new java.lang.IllegalArgumentException(("The specified array's length must be at " + "least ") + mComponents.length);
        }
        java.lang.System.arraycopy(mComponents, 0, components, 0, mComponents.length);
        return components;
    }

    /**
     * <p>Returns the value of the specified component in the range defined by
     * this color's color space (see {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}).</p>
     *
     * <p>If the requested component index is {@link #getComponentCount()},
     * this method returns the alpha component, always in the range
     * \([0..1]\).</p>
     *
     * @see #getComponents()
     * @throws ArrayIndexOutOfBoundsException
     * 		If the specified component index
     * 		is < 0 or >= {@link #getComponentCount()}
     */
    public float getComponent(@android.annotation.IntRange(from = 0, to = 4)
    int component) {
        return mComponents[component];
    }

    /**
     * <p>Returns the relative luminance of this color.</p>
     *
     * <p>Based on the formula for relative luminance defined in WCAG 2.0,
     * W3C Recommendation 11 December 2008.</p>
     *
     * @return A value between 0 (darkest black) and 1 (lightest white)
     * @throws IllegalArgumentException
     * 		If the this color's color space
     * 		does not use the {@link ColorSpace.Model#RGB RGB} color model
     */
    public float luminance() {
        if (mColorSpace.getModel() != android.graphics.ColorSpace.Model.RGB) {
            throw new java.lang.IllegalArgumentException(("The specified color must be encoded in an RGB " + "color space. The supplied color space is ") + mColorSpace.getModel());
        }
        java.util.function.DoubleUnaryOperator eotf = ((android.graphics.ColorSpace.Rgb) (mColorSpace)).getEotf();
        double r = eotf.applyAsDouble(mComponents[0]);
        double g = eotf.applyAsDouble(mComponents[1]);
        double b = eotf.applyAsDouble(mComponents[2]);
        return android.graphics.Color.saturate(((float) (((0.2126 * r) + (0.7152 * g)) + (0.0722 * b))));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.Color color = ((android.graphics.Color) (o));
        // noinspection SimplifiableIfStatement
        if (!java.util.Arrays.equals(mComponents, color.mComponents))
            return false;

        return mColorSpace.equals(color.mColorSpace);
    }

    @java.lang.Override
    public int hashCode() {
        int result = java.util.Arrays.hashCode(mComponents);
        result = (31 * result) + mColorSpace.hashCode();
        return result;
    }

    /**
     * <p>Returns a string representation of the object. This method returns
     * a string equal to the value of:</p>
     *
     * <pre class="prettyprint">
     * "Color(" + r + ", " + g + ", " + b + ", " + a +
     *         ", " + getColorSpace().getName + ')'
     * </pre>
     *
     * <p>For instance, the string representation of opaque black in the sRGB
     * color space is equal to the following value:</p>
     *
     * <pre>
     * Color(0.0, 0.0, 0.0, 1.0, sRGB IEC61966-2.1)
     * </pre>
     *
     * @return A non-null string representation of the object
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String toString() {
        java.lang.StringBuilder b = new java.lang.StringBuilder("Color(");
        for (float c : mComponents) {
            b.append(c).append(", ");
        }
        b.append(mColorSpace.getName());
        b.append(')');
        return b.toString();
    }

    /**
     * Returns the color space encoded in the specified color long.
     *
     * @param color
     * 		The color long whose color space to extract
     * @return A non-null color space instance
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     * @see #red(long)
     * @see #green(long)
     * @see #blue(long)
     * @see #alpha(long)
     */
    @android.annotation.NonNull
    public static android.graphics.ColorSpace colorSpace(@android.annotation.ColorLong
    long color) {
        return android.graphics.ColorSpace.get(((int) (color & 0x3fL)));
    }

    /**
     * Returns the red component encoded in the specified color long.
     * The range of the returned value depends on the color space
     * associated with the specified color. The color space can be
     * queried by calling {@link #colorSpace(long)}.
     *
     * @param color
     * 		The color long whose red channel to extract
     * @return A float value with a range defined by the specified color's
    color space
     * @see #colorSpace(long)
     * @see #green(long)
     * @see #blue(long)
     * @see #alpha(long)
     */
    public static float red(@android.annotation.ColorLong
    long color) {
        if ((color & 0x3fL) == 0L)
            return ((color >> 48) & 0xff) / 255.0F;

        return android.util.Half.toFloat(((short) ((color >> 48) & 0xffff)));
    }

    /**
     * Returns the green component encoded in the specified color long.
     * The range of the returned value depends on the color space
     * associated with the specified color. The color space can be
     * queried by calling {@link #colorSpace(long)}.
     *
     * @param color
     * 		The color long whose green channel to extract
     * @return A float value with a range defined by the specified color's
    color space
     * @see #colorSpace(long)
     * @see #red(long)
     * @see #blue(long)
     * @see #alpha(long)
     */
    public static float green(@android.annotation.ColorLong
    long color) {
        if ((color & 0x3fL) == 0L)
            return ((color >> 40) & 0xff) / 255.0F;

        return android.util.Half.toFloat(((short) ((color >> 32) & 0xffff)));
    }

    /**
     * Returns the blue component encoded in the specified color long.
     * The range of the returned value depends on the color space
     * associated with the specified color. The color space can be
     * queried by calling {@link #colorSpace(long)}.
     *
     * @param color
     * 		The color long whose blue channel to extract
     * @return A float value with a range defined by the specified color's
    color space
     * @see #colorSpace(long)
     * @see #red(long)
     * @see #green(long)
     * @see #alpha(long)
     */
    public static float blue(@android.annotation.ColorLong
    long color) {
        if ((color & 0x3fL) == 0L)
            return ((color >> 32) & 0xff) / 255.0F;

        return android.util.Half.toFloat(((short) ((color >> 16) & 0xffff)));
    }

    /**
     * Returns the alpha component encoded in the specified color long.
     * The returned value is always in the range \([0..1]\).
     *
     * @param color
     * 		The color long whose blue channel to extract
     * @return A float value in the range \([0..1]\)
     * @see #colorSpace(long)
     * @see #red(long)
     * @see #green(long)
     * @see #blue(long)
     */
    public static float alpha(@android.annotation.ColorLong
    long color) {
        if ((color & 0x3fL) == 0L)
            return ((color >> 56) & 0xff) / 255.0F;

        return ((color >> 6) & 0x3ff) / 1023.0F;
    }

    /**
     * Indicates whether the specified color is in the
     * {@link ColorSpace.Named#SRGB sRGB} color space.
     *
     * @param color
     * 		The color to test
     * @return True if the color is in the sRGB color space, false otherwise
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     * @see #isInColorSpace(long, ColorSpace)
     * @see #isWideGamut(long)
     */
    public static boolean isSrgb(@android.annotation.ColorLong
    long color) {
        return android.graphics.Color.colorSpace(color).isSrgb();
    }

    /**
     * Indicates whether the specified color is in a wide-gamut color space.
     * See {@link ColorSpace#isWideGamut()} for a definition of a wide-gamut
     * color space.
     *
     * @param color
     * 		The color to test
     * @return True if the color is in a wide-gamut color space, false otherwise
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     * @see #isInColorSpace(long, ColorSpace)
     * @see #isSrgb(long)
     * @see ColorSpace#isWideGamut()
     */
    public static boolean isWideGamut(@android.annotation.ColorLong
    long color) {
        return android.graphics.Color.colorSpace(color).isWideGamut();
    }

    /**
     * Indicates whether the specified color is in the specified color space.
     *
     * @param color
     * 		The color to test
     * @param colorSpace
     * 		The color space to test against
     * @return True if the color is in the specified color space, false otherwise
     * @see #isSrgb(long)
     * @see #isWideGamut(long)
     */
    public static boolean isInColorSpace(@android.annotation.ColorLong
    long color, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        return ((int) (color & 0x3fL)) == colorSpace.getId();
    }

    /**
     * Converts the specified color long to an ARGB color int. A color int is
     * always in the {@link ColorSpace.Named#SRGB sRGB} color space. This implies
     * a color space conversion is applied if needed.
     *
     * @return An ARGB color in the sRGB color space
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     */
    @android.annotation.ColorInt
    public static int toArgb(@android.annotation.ColorLong
    long color) {
        if ((color & 0x3fL) == 0L)
            return ((int) (color >> 32));

        float r = android.graphics.Color.red(color);
        float g = android.graphics.Color.green(color);
        float b = android.graphics.Color.blue(color);
        float a = android.graphics.Color.alpha(color);
        // The transformation saturates the output
        float[] c = android.graphics.ColorSpace.connect(android.graphics.Color.colorSpace(color)).transform(r, g, b);
        return (((((int) ((a * 255.0F) + 0.5F)) << 24) | (((int) ((c[0] * 255.0F) + 0.5F)) << 16)) | (((int) ((c[1] * 255.0F) + 0.5F)) << 8)) | ((int) ((c[2] * 255.0F) + 0.5F));
    }

    /**
     * Creates a new <code>Color</code> instance from an ARGB color int.
     * The resulting color is in the {@link ColorSpace.Named#SRGB sRGB}
     * color space.
     *
     * @param color
     * 		The ARGB color int to create a <code>Color</code> from
     * @return A non-null instance of {@link Color}
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(@android.annotation.ColorInt
    int color) {
        float r = ((color >> 16) & 0xff) / 255.0F;
        float g = ((color >> 8) & 0xff) / 255.0F;
        float b = (color & 0xff) / 255.0F;
        float a = ((color >> 24) & 0xff) / 255.0F;
        return new android.graphics.Color(r, g, b, a, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * Creates a new <code>Color</code> instance from a color long.
     * The resulting color is in the same color space as the specified color long.
     *
     * @param color
     * 		The color long to create a <code>Color</code> from
     * @return A non-null instance of {@link Color}
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(@android.annotation.ColorLong
    long color) {
        return new android.graphics.Color(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), android.graphics.Color.alpha(color), android.graphics.Color.colorSpace(color));
    }

    /**
     * Creates a new opaque <code>Color</code> in the {@link ColorSpace.Named#SRGB sRGB}
     * color space with the specified red, green and blue component values. The component
     * values must be in the range \([0..1]\).
     *
     * @param r
     * 		The red component of the opaque sRGB color to create, in \([0..1]\)
     * @param g
     * 		The green component of the opaque sRGB color to create, in \([0..1]\)
     * @param b
     * 		The blue component of the opaque sRGB color to create, in \([0..1]\)
     * @return A non-null instance of {@link Color}
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(float r, float g, float b) {
        return new android.graphics.Color(r, g, b, 1.0F);
    }

    /**
     * Creates a new <code>Color</code> in the {@link ColorSpace.Named#SRGB sRGB}
     * color space with the specified red, green, blue and alpha component values.
     * The component values must be in the range \([0..1]\).
     *
     * @param r
     * 		The red component of the sRGB color to create, in \([0..1]\)
     * @param g
     * 		The green component of the sRGB color to create, in \([0..1]\)
     * @param b
     * 		The blue component of the sRGB color to create, in \([0..1]\)
     * @param a
     * 		The alpha component of the sRGB color to create, in \([0..1]\)
     * @return A non-null instance of {@link Color}
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(float r, float g, float b, float a) {
        return new android.graphics.Color(android.graphics.Color.saturate(r), android.graphics.Color.saturate(g), android.graphics.Color.saturate(b), android.graphics.Color.saturate(a));
    }

    /**
     * Creates a new <code>Color</code> in the specified color space with the
     * specified red, green, blue and alpha component values. The range of the
     * components is defined by {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}. The values passed to this method
     * must be in the proper range.
     *
     * @param r
     * 		The red component of the color to create
     * @param g
     * 		The green component of the color to create
     * @param b
     * 		The blue component of the color to create
     * @param a
     * 		The alpha component of the color to create, in \([0..1]\)
     * @param colorSpace
     * 		The color space of the color to create
     * @return A non-null instance of {@link Color}
     * @throws IllegalArgumentException
     * 		If the specified color space uses a
     * 		color model with more than 3 components
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(float r, float g, float b, float a, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        if (colorSpace.getComponentCount() > 3) {
            throw new java.lang.IllegalArgumentException("The specified color space must use a color model " + "with at most 3 color components");
        }
        return new android.graphics.Color(r, g, b, a, colorSpace);
    }

    /**
     * <p>Creates a new <code>Color</code> in the specified color space with the
     * specified component values. The range of the components is defined by
     * {@link ColorSpace#getMinValue(int)} and {@link ColorSpace#getMaxValue(int)}.
     * The values passed to this method must be in the proper range. The alpha
     * component is always in the range \([0..1]\).</p>
     *
     * <p>The length of the array of components must be at least
     * <code>{@link ColorSpace#getComponentCount()} + 1</code>. The component at index
     * {@link ColorSpace#getComponentCount()} is always alpha.</p>
     *
     * @param components
     * 		The components of the color to create, with alpha as the last component
     * @param colorSpace
     * 		The color space of the color to create
     * @return A non-null instance of {@link Color}
     * @throws IllegalArgumentException
     * 		If the array of components is smaller than
     * 		required by the color space
     */
    @android.annotation.NonNull
    public static android.graphics.Color valueOf(@android.annotation.NonNull
    @android.annotation.Size(min = 4, max = 5)
    float[] components, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        if (components.length < (colorSpace.getComponentCount() + 1)) {
            throw new java.lang.IllegalArgumentException(((("Received a component array of length " + components.length) + " but the color model requires ") + (colorSpace.getComponentCount() + 1)) + " (including alpha)");
        }
        return new android.graphics.Color(java.util.Arrays.copyOf(components, colorSpace.getComponentCount() + 1), colorSpace);
    }

    /**
     * Converts the specified ARGB color int to an RGBA color long in the sRGB
     * color space. See the documentation of this class for a description of
     * the color long format.
     *
     * @param color
     * 		The ARGB color int to convert to an RGBA color long in sRGB
     * @return A color long
     */
    @android.annotation.ColorLong
    public static long pack(@android.annotation.ColorInt
    int color) {
        return (color & 0xffffffffL) << 32;
    }

    /**
     * Packs the sRGB color defined by the specified red, green and blue component
     * values into an RGBA color long in the sRGB color space. The alpha component
     * is set to 1.0. See the documentation of this class for a description of the
     * color long format.
     *
     * @param red
     * 		The red component of the sRGB color to create, in \([0..1]\)
     * @param green
     * 		The green component of the sRGB color to create, in \([0..1]\)
     * @param blue
     * 		The blue component of the sRGB color to create, in \([0..1]\)
     * @return A color long
     */
    @android.annotation.ColorLong
    public static long pack(float red, float green, float blue) {
        return android.graphics.Color.pack(red, green, blue, 1.0F, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * Packs the sRGB color defined by the specified red, green, blue and alpha
     * component values into an RGBA color long in the sRGB color space. See the
     * documentation of this class for a description of the color long format.
     *
     * @param red
     * 		The red component of the sRGB color to create, in \([0..1]\)
     * @param green
     * 		The green component of the sRGB color to create, in \([0..1]\)
     * @param blue
     * 		The blue component of the sRGB color to create, in \([0..1]\)
     * @param alpha
     * 		The alpha component of the sRGB color to create, in \([0..1]\)
     * @return A color long
     */
    @android.annotation.ColorLong
    public static long pack(float red, float green, float blue, float alpha) {
        return android.graphics.Color.pack(red, green, blue, alpha, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
    }

    /**
     * <p>Packs the 3 component color defined by the specified red, green, blue and
     * alpha component values into a color long in the specified color space. See the
     * documentation of this class for a description of the color long format.</p>
     *
     * <p>The red, green and blue components must be in the range defined by the
     * specified color space. See {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}.</p>
     *
     * @param red
     * 		The red component of the color to create
     * @param green
     * 		The green component of the color to create
     * @param blue
     * 		The blue component of the color to create
     * @param alpha
     * 		The alpha component of the color to create, in \([0..1]\)
     * @return A color long
     * @throws IllegalArgumentException
     * 		If the color space's id is {@link ColorSpace#MIN_ID}
     * 		or if the color space's color model has more than 3 components
     */
    @android.annotation.ColorLong
    public static long pack(float red, float green, float blue, float alpha, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        if (colorSpace.isSrgb()) {
            int argb = (((((int) ((alpha * 255.0F) + 0.5F)) << 24) | (((int) ((red * 255.0F) + 0.5F)) << 16)) | (((int) ((green * 255.0F) + 0.5F)) << 8)) | ((int) ((blue * 255.0F) + 0.5F));
            return (argb & 0xffffffffL) << 32;
        }
        int id = colorSpace.getId();
        if (id == android.graphics.ColorSpace.MIN_ID) {
            throw new java.lang.IllegalArgumentException("Unknown color space, please use a color space returned by ColorSpace.get()");
        }
        if (colorSpace.getComponentCount() > 3) {
            throw new java.lang.IllegalArgumentException("The color space must use a color model with at most 3 components");
        }
        @android.annotation.HalfFloat
        short r = android.util.Half.toHalf(red);
        @android.annotation.HalfFloat
        short g = android.util.Half.toHalf(green);
        @android.annotation.HalfFloat
        short b = android.util.Half.toHalf(blue);
        int a = ((int) ((java.lang.Math.max(0.0F, java.lang.Math.min(alpha, 1.0F)) * 1023.0F) + 0.5F));
        // Suppress sign extension
        return (((((r & 0xffffL) << 48) | ((g & 0xffffL) << 32)) | ((b & 0xffffL) << 16)) | ((a & 0x3ffL) << 6)) | (id & 0x3fL);
    }

    /**
     * Converts the specified ARGB color int from the {@link ColorSpace.Named#SRGB sRGB}
     * color space into the specified destination color space. The resulting color is
     * returned as a color long. See the documentation of this class for a description
     * of the color long format.
     *
     * @param color
     * 		The sRGB color int to convert
     * @param colorSpace
     * 		The destination color space
     * @return A color long in the destination color space
     */
    @android.annotation.ColorLong
    public static long convert(@android.annotation.ColorInt
    int color, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        float r = ((color >> 16) & 0xff) / 255.0F;
        float g = ((color >> 8) & 0xff) / 255.0F;
        float b = (color & 0xff) / 255.0F;
        float a = ((color >> 24) & 0xff) / 255.0F;
        android.graphics.ColorSpace source = android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB);
        return android.graphics.Color.convert(r, g, b, a, source, colorSpace);
    }

    /**
     * <p>Converts the specified color long from its color space into the specified
     * destination color space. The resulting color is returned as a color long. See
     * the documentation of this class for a description of the color long format.</p>
     *
     * <p>When converting several colors in a row, it is recommended to use
     * {@link #convert(long, ColorSpace.Connector)} instead to
     * avoid the creation of a {@link ColorSpace.Connector} on every invocation.</p>
     *
     * @param color
     * 		The color long to convert
     * @param colorSpace
     * 		The destination color space
     * @return A color long in the destination color space
     * @throws IllegalArgumentException
     * 		If the encoded color space is invalid or unknown
     */
    @android.annotation.ColorLong
    public static long convert(@android.annotation.ColorLong
    long color, @android.annotation.NonNull
    android.graphics.ColorSpace colorSpace) {
        float r = android.graphics.Color.red(color);
        float g = android.graphics.Color.green(color);
        float b = android.graphics.Color.blue(color);
        float a = android.graphics.Color.alpha(color);
        android.graphics.ColorSpace source = android.graphics.Color.colorSpace(color);
        return android.graphics.Color.convert(r, g, b, a, source, colorSpace);
    }

    /**
     * <p>Converts the specified 3 component color from the source color space to the
     * destination color space. The resulting color is returned as a color long. See
     * the documentation of this class for a description of the color long format.</p>
     *
     * <p>When converting multiple colors in a row, it is recommended to use
     * {@link #convert(float, float, float, float, ColorSpace.Connector)} instead to
     * avoid the creation of a {@link ColorSpace.Connector} on every invocation.</p>
     *
     * <p>The red, green and blue components must be in the range defined by the
     * specified color space. See {@link ColorSpace#getMinValue(int)} and
     * {@link ColorSpace#getMaxValue(int)}.</p>
     *
     * @param r
     * 		The red component of the color to convert
     * @param g
     * 		The green component of the color to convert
     * @param b
     * 		The blue component of the color to convert
     * @param a
     * 		The alpha component of the color to convert, in \([0..1]\)
     * @param source
     * 		The source color space, cannot be null
     * @param destination
     * 		The destination color space, cannot be null
     * @return A color long in the destination color space
     * @see #convert(float, float, float, float, ColorSpace.Connector)
     */
    @android.annotation.ColorLong
    public static long convert(float r, float g, float b, float a, @android.annotation.NonNull
    android.graphics.ColorSpace source, @android.annotation.NonNull
    android.graphics.ColorSpace destination) {
        float[] c = android.graphics.ColorSpace.connect(source, destination).transform(r, g, b);
        return android.graphics.Color.pack(c[0], c[1], c[2], a, destination);
    }

    /**
     * <p>Converts the specified color long from a color space to another using the
     * specified color space {@link ColorSpace.Connector connector}. The resulting
     * color is returned as a color long. See the documentation of this class for a
     * description of the color long format.</p>
     *
     * <p>When converting several colors in a row, this method is preferable to
     * {@link #convert(long, ColorSpace)} as it prevents a new connector from being
     * created on every invocation.</p>
     *
     * <p class="note">The connector's source color space should match the color long's
     * color space.</p>
     *
     * @param color
     * 		The color long to convert
     * @param connector
     * 		A color space connector, cannot be null
     * @return A color long in the destination color space of the connector
     */
    @android.annotation.ColorLong
    public static long convert(@android.annotation.ColorLong
    long color, @android.annotation.NonNull
    android.graphics.ColorSpace.Connector connector) {
        float r = android.graphics.Color.red(color);
        float g = android.graphics.Color.green(color);
        float b = android.graphics.Color.blue(color);
        float a = android.graphics.Color.alpha(color);
        return android.graphics.Color.convert(r, g, b, a, connector);
    }

    /**
     * <p>Converts the specified 3 component color from a color space to another using
     * the specified color space {@link ColorSpace.Connector connector}. The resulting
     * color is returned as a color long. See the documentation of this class for a
     * description of the color long format.</p>
     *
     * <p>When converting several colors in a row, this method is preferable to
     * {@link #convert(float, float, float, float, ColorSpace, ColorSpace)} as
     * it prevents a new connector from being created on every invocation.</p>
     *
     * <p>The red, green and blue components must be in the range defined by the
     * source color space of the connector. See {@link ColorSpace#getMinValue(int)}
     * and {@link ColorSpace#getMaxValue(int)}.</p>
     *
     * @param r
     * 		The red component of the color to convert
     * @param g
     * 		The green component of the color to convert
     * @param b
     * 		The blue component of the color to convert
     * @param a
     * 		The alpha component of the color to convert, in \([0..1]\)
     * @param connector
     * 		A color space connector, cannot be null
     * @return A color long in the destination color space of the connector
     * @see #convert(float, float, float, float, ColorSpace, ColorSpace)
     */
    @android.annotation.ColorLong
    public static long convert(float r, float g, float b, float a, @android.annotation.NonNull
    android.graphics.ColorSpace.Connector connector) {
        float[] c = connector.transform(r, g, b);
        return android.graphics.Color.pack(c[0], c[1], c[2], a, connector.getDestination());
    }

    /**
     * <p>Returns the relative luminance of a color.</p>
     *
     * <p>Based on the formula for relative luminance defined in WCAG 2.0,
     * W3C Recommendation 11 December 2008.</p>
     *
     * @return A value between 0 (darkest black) and 1 (lightest white)
     * @throws IllegalArgumentException
     * 		If the specified color's color space
     * 		is unknown or does not use the {@link ColorSpace.Model#RGB RGB} color model
     */
    public static float luminance(@android.annotation.ColorLong
    long color) {
        android.graphics.ColorSpace colorSpace = android.graphics.Color.colorSpace(color);
        if (colorSpace.getModel() != android.graphics.ColorSpace.Model.RGB) {
            throw new java.lang.IllegalArgumentException(("The specified color must be encoded in an RGB " + "color space. The supplied color space is ") + colorSpace.getModel());
        }
        java.util.function.DoubleUnaryOperator eotf = ((android.graphics.ColorSpace.Rgb) (colorSpace)).getEotf();
        double r = eotf.applyAsDouble(android.graphics.Color.red(color));
        double g = eotf.applyAsDouble(android.graphics.Color.green(color));
        double b = eotf.applyAsDouble(android.graphics.Color.blue(color));
        return android.graphics.Color.saturate(((float) (((0.2126 * r) + (0.7152 * g)) + (0.0722 * b))));
    }

    private static float saturate(float v) {
        return v <= 0.0F ? 0.0F : v >= 1.0F ? 1.0F : v;
    }

    /**
     * Return the alpha component of a color int. This is the same as saying
     * color >>> 24
     */
    @android.annotation.IntRange(from = 0, to = 255)
    public static int alpha(int color) {
        return color >>> 24;
    }

    /**
     * Return the red component of a color int. This is the same as saying
     * (color >> 16) & 0xFF
     */
    @android.annotation.IntRange(from = 0, to = 255)
    public static int red(int color) {
        return (color >> 16) & 0xff;
    }

    /**
     * Return the green component of a color int. This is the same as saying
     * (color >> 8) & 0xFF
     */
    @android.annotation.IntRange(from = 0, to = 255)
    public static int green(int color) {
        return (color >> 8) & 0xff;
    }

    /**
     * Return the blue component of a color int. This is the same as saying
     * color & 0xFF
     */
    @android.annotation.IntRange(from = 0, to = 255)
    public static int blue(int color) {
        return color & 0xff;
    }

    /**
     * Return a color-int from red, green, blue components.
     * The alpha component is implicitly 255 (fully opaque).
     * These component values should be \([0..255]\), but there is no
     * range check performed, so if they are out of range, the
     * returned color is undefined.
     *
     * @param red
     * 		Red component \([0..255]\) of the color
     * @param green
     * 		Green component \([0..255]\) of the color
     * @param blue
     * 		Blue component \([0..255]\) of the color
     */
    @android.annotation.ColorInt
    public static int rgb(@android.annotation.IntRange(from = 0, to = 255)
    int red, @android.annotation.IntRange(from = 0, to = 255)
    int green, @android.annotation.IntRange(from = 0, to = 255)
    int blue) {
        return ((0xff000000 | (red << 16)) | (green << 8)) | blue;
    }

    /**
     * Return a color-int from red, green, blue float components
     * in the range \([0..1]\). The alpha component is implicitly
     * 1.0 (fully opaque). If the components are out of range, the
     * returned color is undefined.
     *
     * @param red
     * 		Red component \([0..1]\) of the color
     * @param green
     * 		Green component \([0..1]\) of the color
     * @param blue
     * 		Blue component \([0..1]\) of the color
     */
    @android.annotation.ColorInt
    public static int rgb(float red, float green, float blue) {
        return ((0xff000000 | (((int) ((red * 255.0F) + 0.5F)) << 16)) | (((int) ((green * 255.0F) + 0.5F)) << 8)) | ((int) ((blue * 255.0F) + 0.5F));
    }

    /**
     * Return a color-int from alpha, red, green, blue components.
     * These component values should be \([0..255]\), but there is no
     * range check performed, so if they are out of range, the
     * returned color is undefined.
     *
     * @param alpha
     * 		Alpha component \([0..255]\) of the color
     * @param red
     * 		Red component \([0..255]\) of the color
     * @param green
     * 		Green component \([0..255]\) of the color
     * @param blue
     * 		Blue component \([0..255]\) of the color
     */
    @android.annotation.ColorInt
    public static int argb(@android.annotation.IntRange(from = 0, to = 255)
    int alpha, @android.annotation.IntRange(from = 0, to = 255)
    int red, @android.annotation.IntRange(from = 0, to = 255)
    int green, @android.annotation.IntRange(from = 0, to = 255)
    int blue) {
        return (((alpha << 24) | (red << 16)) | (green << 8)) | blue;
    }

    /**
     * Return a color-int from alpha, red, green, blue float components
     * in the range \([0..1]\). If the components are out of range, the
     * returned color is undefined.
     *
     * @param alpha
     * 		Alpha component \([0..1]\) of the color
     * @param red
     * 		Red component \([0..1]\) of the color
     * @param green
     * 		Green component \([0..1]\) of the color
     * @param blue
     * 		Blue component \([0..1]\) of the color
     */
    @android.annotation.ColorInt
    public static int argb(float alpha, float red, float green, float blue) {
        return (((((int) ((alpha * 255.0F) + 0.5F)) << 24) | (((int) ((red * 255.0F) + 0.5F)) << 16)) | (((int) ((green * 255.0F) + 0.5F)) << 8)) | ((int) ((blue * 255.0F) + 0.5F));
    }

    /**
     * Returns the relative luminance of a color.
     * <p>
     * Assumes sRGB encoding. Based on the formula for relative luminance
     * defined in WCAG 2.0, W3C Recommendation 11 December 2008.
     *
     * @return a value between 0 (darkest black) and 1 (lightest white)
     */
    public static float luminance(@android.annotation.ColorInt
    int color) {
        android.graphics.ColorSpace.Rgb cs = ((android.graphics.ColorSpace.Rgb) (android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB)));
        java.util.function.DoubleUnaryOperator eotf = cs.getEotf();
        double r = eotf.applyAsDouble(android.graphics.Color.red(color) / 255.0);
        double g = eotf.applyAsDouble(android.graphics.Color.green(color) / 255.0);
        double b = eotf.applyAsDouble(android.graphics.Color.blue(color) / 255.0);
        return ((float) (((0.2126 * r) + (0.7152 * g)) + (0.0722 * b)));
    }

    /**
     * </p>Parse the color string, and return the corresponding color-int.
     * If the string cannot be parsed, throws an IllegalArgumentException
     * exception. Supported formats are:</p>
     *
     * <ul>
     *   <li><code>#RRGGBB</code></li>
     *   <li><code>#AARRGGBB</code></li>
     * </ul>
     *
     * <p>The following names are also accepted: <code>red</code>, <code>blue</code>,
     * <code>green</code>, <code>black</code>, <code>white</code>, <code>gray</code>,
     * <code>cyan</code>, <code>magenta</code>, <code>yellow</code>, <code>lightgray</code>,
     * <code>darkgray</code>, <code>grey</code>, <code>lightgrey</code>, <code>darkgrey</code>,
     * <code>aqua</code>, <code>fuchsia</code>, <code>lime</code>, <code>maroon</code>,
     * <code>navy</code>, <code>olive</code>, <code>purple</code>, <code>silver</code>,
     * and <code>teal</code>.</p>
     */
    @android.annotation.ColorInt
    public static int parseColor(@android.annotation.Size(min = 1)
    java.lang.String colorString) {
        if (colorString.charAt(0) == '#') {
            // Use a long to avoid rollovers on #ffXXXXXX
            long color = java.lang.Long.parseLong(colorString.substring(1), 16);
            if (colorString.length() == 7) {
                // Set the alpha value
                color |= 0xff000000;
            } else
                if (colorString.length() != 9) {
                    throw new java.lang.IllegalArgumentException("Unknown color");
                }

            return ((int) (color));
        } else {
            java.lang.Integer color = android.graphics.Color.sColorNameMap.get(colorString.toLowerCase(java.util.Locale.ROOT));
            if (color != null) {
                return color;
            }
        }
        throw new java.lang.IllegalArgumentException("Unknown color");
    }

    /**
     * Convert RGB components to HSV.
     * <ul>
     *   <li><code>hsv[0]</code> is Hue \([0..360[\)</li>
     *   <li><code>hsv[1]</code> is Saturation \([0...1]\)</li>
     *   <li><code>hsv[2]</code> is Value \([0...1]\)</li>
     * </ul>
     *
     * @param red
     * 		red component value \([0..255]\)
     * @param green
     * 		green component value \([0..255]\)
     * @param blue
     * 		blue component value \([0..255]\)
     * @param hsv
     * 		3 element array which holds the resulting HSV components.
     */
    public static void RGBToHSV(@android.annotation.IntRange(from = 0, to = 255)
    int red, @android.annotation.IntRange(from = 0, to = 255)
    int green, @android.annotation.IntRange(from = 0, to = 255)
    int blue, @android.annotation.Size(3)
    float[] hsv) {
        if (hsv.length < 3) {
            throw new java.lang.RuntimeException("3 components required for hsv");
        }
        android.graphics.Color.nativeRGBToHSV(red, green, blue, hsv);
    }

    /**
     * Convert the ARGB color to its HSV components.
     * <ul>
     *   <li><code>hsv[0]</code> is Hue \([0..360[\)</li>
     *   <li><code>hsv[1]</code> is Saturation \([0...1]\)</li>
     *   <li><code>hsv[2]</code> is Value \([0...1]\)</li>
     * </ul>
     *
     * @param color
     * 		the argb color to convert. The alpha component is ignored.
     * @param hsv
     * 		3 element array which holds the resulting HSV components.
     */
    public static void colorToHSV(@android.annotation.ColorInt
    int color, @android.annotation.Size(3)
    float[] hsv) {
        android.graphics.Color.RGBToHSV((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, hsv);
    }

    /**
     * Convert HSV components to an ARGB color. Alpha set to 0xFF.
     * <ul>
     *   <li><code>hsv[0]</code> is Hue \([0..360[\)</li>
     *   <li><code>hsv[1]</code> is Saturation \([0...1]\)</li>
     *   <li><code>hsv[2]</code> is Value \([0...1]\)</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param hsv
     * 		3 element array which holds the input HSV components.
     * @return the resulting argb color
     */
    @android.annotation.ColorInt
    public static int HSVToColor(@android.annotation.Size(3)
    float[] hsv) {
        return android.graphics.Color.HSVToColor(0xff, hsv);
    }

    /**
     * Convert HSV components to an ARGB color. The alpha component is passed
     * through unchanged.
     * <ul>
     *   <li><code>hsv[0]</code> is Hue \([0..360[\)</li>
     *   <li><code>hsv[1]</code> is Saturation \([0...1]\)</li>
     *   <li><code>hsv[2]</code> is Value \([0...1]\)</li>
     * </ul>
     * If hsv values are out of range, they are pinned.
     *
     * @param alpha
     * 		the alpha component of the returned argb color.
     * @param hsv
     * 		3 element array which holds the input HSV components.
     * @return the resulting argb color
     */
    @android.annotation.ColorInt
    public static int HSVToColor(@android.annotation.IntRange(from = 0, to = 255)
    int alpha, @android.annotation.Size(3)
    float[] hsv) {
        if (hsv.length < 3) {
            throw new java.lang.RuntimeException("3 components required for hsv");
        }
        return android.graphics.Color.nativeHSVToColor(alpha, hsv);
    }

    private static native void nativeRGBToHSV(int red, int greed, int blue, float[] hsv);

    private static native int nativeHSVToColor(int alpha, float[] hsv);

    /**
     * Converts an HTML color (named or numeric) to an integer RGB value.
     *
     * @param color
     * 		Non-null color string.
     * @return A color value, or {@code -1} if the color string could not be interpreted.
     * @unknown 
     */
    @android.annotation.ColorInt
    public static int getHtmlColor(@android.annotation.NonNull
    java.lang.String color) {
        java.lang.Integer i = android.graphics.Color.sColorNameMap.get(color.toLowerCase(java.util.Locale.ROOT));
        if (i != null) {
            return i;
        } else {
            try {
                return com.android.internal.util.XmlUtils.convertValueToInt(color, -1);
            } catch (java.lang.NumberFormatException nfe) {
                return -1;
            }
        }
    }

    private static final java.util.HashMap<java.lang.String, java.lang.Integer> sColorNameMap;

    static {
        sColorNameMap = new java.util.HashMap<>();
        android.graphics.Color.sColorNameMap.put("black", android.graphics.Color.BLACK);
        android.graphics.Color.sColorNameMap.put("darkgray", android.graphics.Color.DKGRAY);
        android.graphics.Color.sColorNameMap.put("gray", android.graphics.Color.GRAY);
        android.graphics.Color.sColorNameMap.put("lightgray", android.graphics.Color.LTGRAY);
        android.graphics.Color.sColorNameMap.put("white", android.graphics.Color.WHITE);
        android.graphics.Color.sColorNameMap.put("red", android.graphics.Color.RED);
        android.graphics.Color.sColorNameMap.put("green", android.graphics.Color.GREEN);
        android.graphics.Color.sColorNameMap.put("blue", android.graphics.Color.BLUE);
        android.graphics.Color.sColorNameMap.put("yellow", android.graphics.Color.YELLOW);
        android.graphics.Color.sColorNameMap.put("cyan", android.graphics.Color.CYAN);
        android.graphics.Color.sColorNameMap.put("magenta", android.graphics.Color.MAGENTA);
        android.graphics.Color.sColorNameMap.put("aqua", 0xff00ffff);
        android.graphics.Color.sColorNameMap.put("fuchsia", 0xffff00ff);
        android.graphics.Color.sColorNameMap.put("darkgrey", android.graphics.Color.DKGRAY);
        android.graphics.Color.sColorNameMap.put("grey", android.graphics.Color.GRAY);
        android.graphics.Color.sColorNameMap.put("lightgrey", android.graphics.Color.LTGRAY);
        android.graphics.Color.sColorNameMap.put("lime", 0xff00ff00);
        android.graphics.Color.sColorNameMap.put("maroon", 0xff800000);
        android.graphics.Color.sColorNameMap.put("navy", 0xff000080);
        android.graphics.Color.sColorNameMap.put("olive", 0xff808000);
        android.graphics.Color.sColorNameMap.put("purple", 0xff800080);
        android.graphics.Color.sColorNameMap.put("silver", 0xffc0c0c0);
        android.graphics.Color.sColorNameMap.put("teal", 0xff008080);
    }
}

