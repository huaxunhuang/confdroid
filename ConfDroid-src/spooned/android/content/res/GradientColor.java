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
package android.content.res;


/**
 * Lets you define a gradient color, which is used inside
 * {@link android.graphics.drawable.VectorDrawable}.
 *
 * {@link android.content.res.GradientColor}s are created from XML resource files defined in the
 * "color" subdirectory directory of an application's resource directory.  The XML file contains
 * a single "gradient" element with a number of attributes and elements inside.  For example:
 * <pre>
 * &lt;gradient xmlns:android="http://schemas.android.com/apk/res/android"&gt;
 *   &lt;android:startColor="?android:attr/colorPrimary"/&gt;
 *   &lt;android:endColor="?android:attr/colorControlActivated"/&gt;
 *   &lt;.../&gt;
 *   &lt;android:type="linear"/&gt;
 * &lt;/gradient&gt;
 * </pre>
 *
 * This can describe either a {@link android.graphics.LinearGradient},
 * {@link android.graphics.RadialGradient}, or {@link android.graphics.SweepGradient}.
 *
 * Note that different attributes are relevant for different types of gradient.
 * For example, android:gradientRadius is only applied to RadialGradient.
 * android:centerX and android:centerY are only applied to SweepGradient or RadialGradient.
 * android:startX, android:startY, android:endX and android:endY are only applied to LinearGradient.
 *
 * Also note if any color "item" element is defined, then startColor, centerColor and endColor will
 * be ignored.
 *
 * @unknown 
 */
public class GradientColor extends android.content.res.ComplexColor {
    private static final java.lang.String TAG = "GradientColor";

    private static final boolean DBG_GRADIENT = false;

    @android.annotation.IntDef(prefix = { "TILE_MODE_" }, value = { android.content.res.GradientColor.TILE_MODE_CLAMP, android.content.res.GradientColor.TILE_MODE_REPEAT, android.content.res.GradientColor.TILE_MODE_MIRROR })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface GradientTileMode {}

    private static final int TILE_MODE_CLAMP = 0;

    private static final int TILE_MODE_REPEAT = 1;

    private static final int TILE_MODE_MIRROR = 2;

    /**
     * Lazily-created factory for this GradientColor.
     */
    private android.content.res.GradientColor.GradientColorFactory mFactory;

    @android.content.pm.ActivityInfo.Config
    private int mChangingConfigurations;

    private int mDefaultColor;

    // After parsing all the attributes from XML, this shader is the ultimate result containing
    // all the XML information.
    private android.graphics.Shader mShader = null;

    // Below are the attributes at the root element <gradient>.
    // NOTE: they need to be copied in the copy constructor!
    private int mGradientType = android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT;

    private float mCenterX = 0.0F;

    private float mCenterY = 0.0F;

    private float mStartX = 0.0F;

    private float mStartY = 0.0F;

    private float mEndX = 0.0F;

    private float mEndY = 0.0F;

    private int mStartColor = 0;

    private int mCenterColor = 0;

    private int mEndColor = 0;

    private boolean mHasCenterColor = false;

    private int mTileMode = 0;// Clamp mode.


    private float mGradientRadius = 0.0F;

    // Below are the attributes for the <item> element.
    private int[] mItemColors;

    private float[] mItemOffsets;

    // Theme attributes for the root and item elements.
    private int[] mThemeAttrs;

    private int[][] mItemsThemeAttrs;

    private GradientColor() {
    }

    private GradientColor(android.content.res.GradientColor copy) {
        if (copy != null) {
            mChangingConfigurations = copy.mChangingConfigurations;
            mDefaultColor = copy.mDefaultColor;
            mShader = copy.mShader;
            mGradientType = copy.mGradientType;
            mCenterX = copy.mCenterX;
            mCenterY = copy.mCenterY;
            mStartX = copy.mStartX;
            mStartY = copy.mStartY;
            mEndX = copy.mEndX;
            mEndY = copy.mEndY;
            mStartColor = copy.mStartColor;
            mCenterColor = copy.mCenterColor;
            mEndColor = copy.mEndColor;
            mHasCenterColor = copy.mHasCenterColor;
            mGradientRadius = copy.mGradientRadius;
            mTileMode = copy.mTileMode;
            if (copy.mItemColors != null) {
                mItemColors = copy.mItemColors.clone();
            }
            if (copy.mItemOffsets != null) {
                mItemOffsets = copy.mItemOffsets.clone();
            }
            if (copy.mThemeAttrs != null) {
                mThemeAttrs = copy.mThemeAttrs.clone();
            }
            if (copy.mItemsThemeAttrs != null) {
                mItemsThemeAttrs = copy.mItemsThemeAttrs.clone();
            }
        }
    }

    // Set the default to clamp mode.
    private static android.graphics.Shader.TileMode parseTileMode(@android.content.res.GradientColor.GradientTileMode
    int tileMode) {
        switch (tileMode) {
            case android.content.res.GradientColor.TILE_MODE_CLAMP :
                return android.graphics.Shader.TileMode.CLAMP;
            case android.content.res.GradientColor.TILE_MODE_REPEAT :
                return android.graphics.Shader.TileMode.REPEAT;
            case android.content.res.GradientColor.TILE_MODE_MIRROR :
                return android.graphics.Shader.TileMode.MIRROR;
            default :
                return android.graphics.Shader.TileMode.CLAMP;
        }
    }

    /**
     * Update the root level's attributes, either for inflate or applyTheme.
     */
    private void updateRootElementState(android.content.res.TypedArray a) {
        // Extract the theme attributes, if any.
        mThemeAttrs = a.extractThemeAttrs();
        mStartX = a.getFloat(R.styleable.GradientColor_startX, mStartX);
        mStartY = a.getFloat(R.styleable.GradientColor_startY, mStartY);
        mEndX = a.getFloat(R.styleable.GradientColor_endX, mEndX);
        mEndY = a.getFloat(R.styleable.GradientColor_endY, mEndY);
        mCenterX = a.getFloat(R.styleable.GradientColor_centerX, mCenterX);
        mCenterY = a.getFloat(R.styleable.GradientColor_centerY, mCenterY);
        mGradientType = a.getInt(R.styleable.GradientColor_type, mGradientType);
        mStartColor = a.getColor(R.styleable.GradientColor_startColor, mStartColor);
        mHasCenterColor |= a.hasValue(R.styleable.GradientColor_centerColor);
        mCenterColor = a.getColor(R.styleable.GradientColor_centerColor, mCenterColor);
        mEndColor = a.getColor(R.styleable.GradientColor_endColor, mEndColor);
        mTileMode = a.getInt(R.styleable.GradientColor_tileMode, mTileMode);
        if (android.content.res.GradientColor.DBG_GRADIENT) {
            android.util.Log.v(android.content.res.GradientColor.TAG, "hasCenterColor is " + mHasCenterColor);
            if (mHasCenterColor) {
                android.util.Log.v(android.content.res.GradientColor.TAG, "centerColor:" + mCenterColor);
            }
            android.util.Log.v(android.content.res.GradientColor.TAG, "startColor: " + mStartColor);
            android.util.Log.v(android.content.res.GradientColor.TAG, "endColor: " + mEndColor);
            android.util.Log.v(android.content.res.GradientColor.TAG, "tileMode: " + mTileMode);
        }
        mGradientRadius = a.getFloat(R.styleable.GradientColor_gradientRadius, mGradientRadius);
    }

    /**
     * Check if the XML content is valid.
     *
     * @throws XmlPullParserException
     * 		if errors were found.
     */
    private void validateXmlContent() throws org.xmlpull.v1.XmlPullParserException {
        if ((mGradientRadius <= 0) && (mGradientType == android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT)) {
            throw new org.xmlpull.v1.XmlPullParserException("<gradient> tag requires 'gradientRadius' " + "attribute with radial type");
        }
    }

    /**
     * The shader information will be applied to the native VectorDrawable's path.
     *
     * @unknown 
     */
    public android.graphics.Shader getShader() {
        return mShader;
    }

    /**
     * A public method to create GradientColor from a XML resource.
     */
    public static android.content.res.GradientColor createFromXml(android.content.res.Resources r, android.content.res.XmlResourceParser parser, android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            // Seek parser to start tag.
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
        }
        return android.content.res.GradientColor.createFromXmlInner(r, parser, attrs, theme);
    }

    /**
     * Create from inside an XML document. Called on a parser positioned at a
     * tag in an XML document, tries to create a GradientColor from that tag.
     *
     * @return A new GradientColor for the current tag.
     * @throws XmlPullParserException
     * 		if the current tag is not &lt;gradient>
     */
    @android.annotation.NonNull
    static android.content.res.GradientColor createFromXmlInner(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String name = parser.getName();
        if (!name.equals("gradient")) {
            throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": invalid gradient color tag ") + name);
        }
        final android.content.res.GradientColor gradientColor = new android.content.res.GradientColor();
        gradientColor.inflate(r, parser, attrs, theme);
        return gradientColor;
    }

    /**
     * Fill in this object based on the contents of an XML "gradient" element.
     */
    private void inflate(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.res.TypedArray a = android.content.res.Resources.obtainAttributes(r, theme, attrs, R.styleable.GradientColor);
        updateRootElementState(a);
        mChangingConfigurations |= a.getChangingConfigurations();
        a.recycle();
        // Check correctness and throw exception if errors found.
        validateXmlContent();
        inflateChildElements(r, parser, attrs, theme);
        onColorsChange();
    }

    /**
     * Inflates child elements "item"s for each color stop.
     *
     * Note that at root level, we need to save ThemeAttrs for theme applied later.
     * Here similarly, at each child item, we need to save the theme's attributes, and apply theme
     * later as applyItemsAttrsTheme().
     */
    private void inflateChildElements(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.NonNull
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int innerDepth = parser.getDepth() + 1;
        int type;
        int depth;
        // Pre-allocate the array with some size, for better performance.
        float[] offsetList = new float[20];
        int[] colorList = new int[offsetList.length];
        int[][] themeAttrsList = new int[offsetList.length][];
        int listSize = 0;
        boolean hasUnresolvedAttrs = false;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (((depth = parser.getDepth()) >= innerDepth) || (type != org.xmlpull.v1.XmlPullParser.END_TAG))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            if ((depth > innerDepth) || (!parser.getName().equals("item"))) {
                continue;
            }
            final android.content.res.TypedArray a = android.content.res.Resources.obtainAttributes(r, theme, attrs, R.styleable.GradientColorItem);
            boolean hasColor = a.hasValue(R.styleable.GradientColorItem_color);
            boolean hasOffset = a.hasValue(R.styleable.GradientColorItem_offset);
            if ((!hasColor) || (!hasOffset)) {
                throw new org.xmlpull.v1.XmlPullParserException((parser.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' ") + "attribute!");
            }
            final int[] themeAttrs = a.extractThemeAttrs();
            int color = a.getColor(R.styleable.GradientColorItem_color, 0);
            float offset = a.getFloat(R.styleable.GradientColorItem_offset, 0);
            if (android.content.res.GradientColor.DBG_GRADIENT) {
                android.util.Log.v(android.content.res.GradientColor.TAG, (("new item color " + color) + " ") + java.lang.Integer.toHexString(color));
                android.util.Log.v(android.content.res.GradientColor.TAG, "offset" + offset);
            }
            mChangingConfigurations |= a.getChangingConfigurations();
            a.recycle();
            if (themeAttrs != null) {
                hasUnresolvedAttrs = true;
            }
            colorList = com.android.internal.util.GrowingArrayUtils.append(colorList, listSize, color);
            offsetList = com.android.internal.util.GrowingArrayUtils.append(offsetList, listSize, offset);
            themeAttrsList = com.android.internal.util.GrowingArrayUtils.append(themeAttrsList, listSize, themeAttrs);
            listSize++;
        } 
        if (listSize > 0) {
            if (hasUnresolvedAttrs) {
                mItemsThemeAttrs = new int[listSize][];
                java.lang.System.arraycopy(themeAttrsList, 0, mItemsThemeAttrs, 0, listSize);
            } else {
                mItemsThemeAttrs = null;
            }
            mItemColors = new int[listSize];
            mItemOffsets = new float[listSize];
            java.lang.System.arraycopy(colorList, 0, mItemColors, 0, listSize);
            java.lang.System.arraycopy(offsetList, 0, mItemOffsets, 0, listSize);
        }
    }

    /**
     * Apply theme to all the items.
     */
    private void applyItemsAttrsTheme(android.content.res.Resources.Theme t) {
        if (mItemsThemeAttrs == null) {
            return;
        }
        boolean hasUnresolvedAttrs = false;
        final int[][] themeAttrsList = mItemsThemeAttrs;
        final int N = themeAttrsList.length;
        for (int i = 0; i < N; i++) {
            if (themeAttrsList[i] != null) {
                final android.content.res.TypedArray a = t.resolveAttributes(themeAttrsList[i], R.styleable.GradientColorItem);
                // Extract the theme attributes, if any, before attempting to
                // read from the typed array. This prevents a crash if we have
                // unresolved attrs.
                themeAttrsList[i] = a.extractThemeAttrs(themeAttrsList[i]);
                if (themeAttrsList[i] != null) {
                    hasUnresolvedAttrs = true;
                }
                mItemColors[i] = a.getColor(R.styleable.GradientColorItem_color, mItemColors[i]);
                mItemOffsets[i] = a.getFloat(R.styleable.GradientColorItem_offset, mItemOffsets[i]);
                if (android.content.res.GradientColor.DBG_GRADIENT) {
                    android.util.Log.v(android.content.res.GradientColor.TAG, (("applyItemsAttrsTheme Colors[i] " + i) + " ") + java.lang.Integer.toHexString(mItemColors[i]));
                    android.util.Log.v(android.content.res.GradientColor.TAG, (("Offsets[i] " + i) + " ") + mItemOffsets[i]);
                }
                // Account for any configuration changes.
                mChangingConfigurations |= a.getChangingConfigurations();
                a.recycle();
            }
        }
        if (!hasUnresolvedAttrs) {
            mItemsThemeAttrs = null;
        }
    }

    private void onColorsChange() {
        int[] tempColors = null;
        float[] tempOffsets = null;
        if (mItemColors != null) {
            int length = mItemColors.length;
            tempColors = new int[length];
            tempOffsets = new float[length];
            for (int i = 0; i < length; i++) {
                tempColors[i] = mItemColors[i];
                tempOffsets[i] = mItemOffsets[i];
            }
        } else {
            if (mHasCenterColor) {
                tempColors = new int[3];
                tempColors[0] = mStartColor;
                tempColors[1] = mCenterColor;
                tempColors[2] = mEndColor;
                tempOffsets = new float[3];
                tempOffsets[0] = 0.0F;
                // Since 0.5f is default value, try to take the one that isn't 0.5f
                tempOffsets[1] = 0.5F;
                tempOffsets[2] = 1.0F;
            } else {
                tempColors = new int[2];
                tempColors[0] = mStartColor;
                tempColors[1] = mEndColor;
            }
        }
        if (tempColors.length < 2) {
            android.util.Log.w(android.content.res.GradientColor.TAG, (("<gradient> tag requires 2 color values specified!" + tempColors.length) + " ") + tempColors);
        }
        if (mGradientType == android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT) {
            mShader = new android.graphics.LinearGradient(mStartX, mStartY, mEndX, mEndY, tempColors, tempOffsets, android.content.res.GradientColor.parseTileMode(mTileMode));
        } else {
            if (mGradientType == android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT) {
                mShader = new android.graphics.RadialGradient(mCenterX, mCenterY, mGradientRadius, tempColors, tempOffsets, android.content.res.GradientColor.parseTileMode(mTileMode));
            } else {
                mShader = new android.graphics.SweepGradient(mCenterX, mCenterY, tempColors, tempOffsets);
            }
        }
        mDefaultColor = tempColors[0];
    }

    /**
     * For Gradient color, the default color is not very useful, since the gradient will override
     * the color information anyway.
     */
    @java.lang.Override
    @android.annotation.ColorInt
    public int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * Similar to ColorStateList, setup constant state and its factory.
     *
     * @unknown only for resource preloading
     */
    @java.lang.Override
    public android.content.res.ConstantState<android.content.res.ComplexColor> getConstantState() {
        if (mFactory == null) {
            mFactory = new android.content.res.GradientColor.GradientColorFactory(this);
        }
        return mFactory;
    }

    private static class GradientColorFactory extends android.content.res.ConstantState<android.content.res.ComplexColor> {
        private final android.content.res.GradientColor mSrc;

        public GradientColorFactory(android.content.res.GradientColor src) {
            mSrc = src;
        }

        @java.lang.Override
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mSrc.mChangingConfigurations;
        }

        @java.lang.Override
        public android.content.res.GradientColor newInstance() {
            return mSrc;
        }

        @java.lang.Override
        public android.content.res.GradientColor newInstance(android.content.res.Resources res, android.content.res.Resources.Theme theme) {
            return mSrc.obtainForTheme(theme);
        }
    }

    /**
     * Returns an appropriately themed gradient color.
     *
     * @param t
     * 		the theme to apply
     * @return a copy of the gradient color the theme applied, or the
    gradient itself if there were no unresolved theme
    attributes
     * @unknown only for resource preloading
     */
    @java.lang.Override
    public android.content.res.GradientColor obtainForTheme(android.content.res.Resources.Theme t) {
        if ((t == null) || (!canApplyTheme())) {
            return this;
        }
        final android.content.res.GradientColor clone = new android.content.res.GradientColor(this);
        clone.applyTheme(t);
        return clone;
    }

    /**
     * Returns a mask of the configuration parameters for which this gradient
     * may change, requiring that it be re-created.
     *
     * @return a mask of the changing configuration parameters, as defined by
    {@link android.content.pm.ActivityInfo}
     * @see android.content.pm.ActivityInfo
     */
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mChangingConfigurations;
    }

    private void applyTheme(android.content.res.Resources.Theme t) {
        if (mThemeAttrs != null) {
            applyRootAttrsTheme(t);
        }
        if (mItemsThemeAttrs != null) {
            applyItemsAttrsTheme(t);
        }
        onColorsChange();
    }

    private void applyRootAttrsTheme(android.content.res.Resources.Theme t) {
        final android.content.res.TypedArray a = t.resolveAttributes(mThemeAttrs, R.styleable.GradientColor);
        // mThemeAttrs will be set to null if if there are no theme attributes in the
        // typed array.
        mThemeAttrs = a.extractThemeAttrs(mThemeAttrs);
        // merging the attributes update inside the updateRootElementState().
        updateRootElementState(a);
        // Account for any configuration changes.
        mChangingConfigurations |= a.getChangingConfigurations();
        a.recycle();
    }

    /**
     * Returns whether a theme can be applied to this gradient color, which
     * usually indicates that the gradient color has unresolved theme
     * attributes.
     *
     * @return whether a theme can be applied to this gradient color.
     * @unknown only for resource preloading
     */
    @java.lang.Override
    public boolean canApplyTheme() {
        return (mThemeAttrs != null) || (mItemsThemeAttrs != null);
    }
}

