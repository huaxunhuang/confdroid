/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Container for an array of values that were retrieved with
 * {@link Resources.Theme#obtainStyledAttributes(AttributeSet, int[], int, int)}
 * or {@link Resources#obtainAttributes}.  Be
 * sure to call {@link #recycle} when done with them.
 *
 * The indices used to retrieve values from this structure correspond to
 * the positions of the attributes given to obtainStyledAttributes.
 */
public class TypedArray {
    static android.content.res.TypedArray obtain(android.content.res.Resources res, int len) {
        android.content.res.TypedArray attrs = res.mTypedArrayPool.acquire();
        if (attrs == null) {
            attrs = new android.content.res.TypedArray(res);
        }
        attrs.mRecycled = false;
        // Reset the assets, which may have changed due to configuration changes
        // or further resource loading.
        attrs.mAssets = res.getAssets();
        attrs.mMetrics = res.getDisplayMetrics();
        attrs.resize(len);
        return attrs;
    }

    // STYLE_ prefixed constants are offsets within the typed data array.
    // Keep this in sync with libs/androidfw/include/androidfw/AttributeResolution.h
    static final int STYLE_NUM_ENTRIES = 7;

    static final int STYLE_TYPE = 0;

    static final int STYLE_DATA = 1;

    static final int STYLE_ASSET_COOKIE = 2;

    static final int STYLE_RESOURCE_ID = 3;

    static final int STYLE_CHANGING_CONFIGURATIONS = 4;

    static final int STYLE_DENSITY = 5;

    static final int STYLE_SOURCE_RESOURCE_ID = 6;

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.Resources mResources;

    @android.annotation.UnsupportedAppUsage
    private android.util.DisplayMetrics mMetrics;

    @android.annotation.UnsupportedAppUsage
    private android.content.res.AssetManager mAssets;

    @android.annotation.UnsupportedAppUsage
    private boolean mRecycled;

    /* package */
    @android.annotation.UnsupportedAppUsage
    android.content.res.XmlBlock.Parser mXml;

    /* package */
    @android.annotation.UnsupportedAppUsage
    android.content.res.Resources.Theme mTheme;

    /**
     * mData is used to hold the value/id and other metadata about each attribute.
     *
     * [type, data, asset cookie, resource id, changing configuration, density]
     *
     * type - type of this attribute, see TypedValue#TYPE_*
     *
     * data - can be used in various ways:
     *     a) actual value of the attribute if type is between #TYPE_FIRST_INT and #TYPE_LAST_INT
     *        1) color represented by an integer (#TYPE_INT_COLOR_*)
     *        2) boolean represented by an integer (#TYPE_INT_BOOLEAN)
     *        3) integer number (#TYPE_TYPE_INT_DEC or #TYPE_INT_HEX)
     *        4) float number where integer gets interpreted as float (#TYPE_FLOAT, #TYPE_FRACTION
     *            and #TYPE_DIMENSION)
     *     b) index into string block inside AssetManager (#TYPE_STRING)
     *     c) attribute resource id in the current theme/style (#TYPE_ATTRIBUTE)
     *
     * asset cookie - used in two ways:
     *     a) for strings, drawables, and fonts it specifies the set of apk assets to look at
     *     (multi-apk case)
     *     b) cookie + asset as a unique identifier for drawable caches
     *
     * resource id - id that was finally used to resolve this attribute
     *
     * changing configuration - a mask of the configuration parameters for which the values in this
     * attribute may change
     *
     * density - density of drawable pointed to by this attribute
     */
    /* package */
    @android.annotation.UnsupportedAppUsage
    int[] mData;

    /**
     * Pointer to the start of the memory address of mData. It is passed via JNI and used to write
     * to mData array directly from native code (AttributeResolution.cpp).
     */
    /* package */
    long mDataAddress;

    /* package */
    @android.annotation.UnsupportedAppUsage
    int[] mIndices;

    /**
     * Similar to mDataAddress, but instead it is a pointer to mIndices address.
     */
    /* package */
    long mIndicesAddress;

    /* package */
    @android.annotation.UnsupportedAppUsage
    int mLength;

    /* package */
    @android.annotation.UnsupportedAppUsage
    android.util.TypedValue mValue = new android.util.TypedValue();

    private void resize(int len) {
        mLength = len;
        final int dataLen = len * android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int indicesLen = len + 1;
        final dalvik.system.VMRuntime runtime = dalvik.system.VMRuntime.getRuntime();
        if ((mDataAddress == 0) || (mData.length < dataLen)) {
            mData = ((int[]) (runtime.newNonMovableArray(int.class, dataLen)));
            mDataAddress = runtime.addressOf(mData);
            mIndices = ((int[]) (runtime.newNonMovableArray(int.class, indicesLen)));
            mIndicesAddress = runtime.addressOf(mIndices);
        }
    }

    /**
     * Returns the number of values in this array.
     *
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int length() {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return mLength;
    }

    /**
     * Returns the number of indices in the array that actually have data. Attributes with a value
     * of @empty are included, as this is an explicit indicator.
     *
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int getIndexCount() {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return mIndices[0];
    }

    /**
     * Returns an index in the array that has data. Attributes with a value of @empty are included,
     * as this is an explicit indicator.
     *
     * @param at
     * 		The index you would like to returned, ranging from 0 to
     * 		{@link #getIndexCount()}.
     * @return The index at the given offset, which can be used with
    {@link #getValue} and related APIs.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int getIndex(int at) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return mIndices[1 + at];
    }

    /**
     * Returns the Resources object this array was loaded from.
     *
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public android.content.res.Resources getResources() {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return mResources;
    }

    /**
     * Retrieves the styled string value for the attribute at <var>index</var>.
     * <p>
     * If the attribute is not a string, this method will attempt to coerce
     * it to a string.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return CharSequence holding string data. May be styled. Returns
    {@code null} if the attribute is not defined or could not be
    coerced to a string.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public java.lang.CharSequence getText(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return null;
        } else
            if (type == android.util.TypedValue.TYPE_STRING) {
                return loadStringValueAt(index);
            }

        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            return v.coerceToString();
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getText of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieves the string value for the attribute at <var>index</var>.
     * <p>
     * If the attribute is not a string, this method will attempt to coerce
     * it to a string.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return String holding string data. Any styling information is removed.
    Returns {@code null} if the attribute is not defined or could
    not be coerced to a string.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    @android.annotation.Nullable
    public java.lang.String getString(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return null;
        } else
            if (type == android.util.TypedValue.TYPE_STRING) {
                return loadStringValueAt(index).toString();
            }

        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            final java.lang.CharSequence cs = v.coerceToString();
            return cs != null ? cs.toString() : null;
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getString of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieves the string value for the attribute at <var>index</var>, but
     * only if that string comes from an immediate value in an XML file.  That
     * is, this does not allow references to string resources, string
     * attributes, or conversions from other types.  As such, this method
     * will only return strings for TypedArray objects that come from
     * attributes in an XML file.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return String holding string data. Any styling information is removed.
    Returns {@code null} if the attribute is not defined or is not
    an immediate string value.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public java.lang.String getNonResourceString(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_STRING) {
            final int cookie = data[index + android.content.res.TypedArray.STYLE_ASSET_COOKIE];
            if (cookie < 0) {
                return mXml.getPooledString(data[index + android.content.res.TypedArray.STYLE_DATA]).toString();
            }
        }
        return null;
    }

    /**
     * Retrieves the string value for the attribute at <var>index</var> that is
     * not allowed to change with the given configurations.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param allowedChangingConfigs
     * 		Bit mask of configurations from
     * 		{@link Configuration}.NATIVE_CONFIG_* that are allowed to change.
     * @return String holding string data. Any styling information is removed.
    Returns {@code null} if the attribute is not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public java.lang.String getNonConfigurationString(@android.annotation.StyleableRes
    int index, @android.content.pm.ActivityInfo.Config
    int allowedChangingConfigs) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        @android.content.pm.ActivityInfo.Config
        final int changingConfigs = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(data[index + android.content.res.TypedArray.STYLE_CHANGING_CONFIGURATIONS]);
        if ((changingConfigs & (~allowedChangingConfigs)) != 0) {
            return null;
        }
        if (type == android.util.TypedValue.TYPE_NULL) {
            return null;
        } else
            if (type == android.util.TypedValue.TYPE_STRING) {
                return loadStringValueAt(index).toString();
            }

        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            final java.lang.CharSequence cs = v.coerceToString();
            return cs != null ? cs.toString() : null;
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getNonConfigurationString of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve the boolean value for the attribute at <var>index</var>.
     * <p>
     * If the attribute is an integer value, this method will return whether
     * it is equal to zero. If the attribute is not a boolean or integer value,
     * this method will attempt to coerce it to an integer using
     * {@link Integer#decode(String)} and return whether it is equal to zero.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		cannot be coerced to an integer.
     * @return Boolean value of the attribute, or defValue if the attribute was
    not defined or could not be coerced to an integer.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public boolean getBoolean(@android.annotation.StyleableRes
    int index, boolean defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return data[index + android.content.res.TypedArray.STYLE_DATA] != 0;
            }

        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            android.os.StrictMode.noteResourceMismatch(v);
            return com.android.internal.util.XmlUtils.convertValueToBoolean(v.coerceToString(), defValue);
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getBoolean of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve the integer value for the attribute at <var>index</var>.
     * <p>
     * If the attribute is not an integer, this method will attempt to coerce
     * it to an integer using {@link Integer#decode(String)}.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		cannot be coerced to an integer.
     * @return Integer value of the attribute, or defValue if the attribute was
    not defined or could not be coerced to an integer.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int getInt(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return data[index + android.content.res.TypedArray.STYLE_DATA];
            }

        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            android.os.StrictMode.noteResourceMismatch(v);
            return com.android.internal.util.XmlUtils.convertValueToInt(v.coerceToString(), defValue);
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getInt of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve the float value for the attribute at <var>index</var>.
     * <p>
     * If the attribute is not a float or an integer, this method will attempt
     * to coerce it to a float using {@link Float#parseFloat(String)}.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Attribute float value, or defValue if the attribute was
    not defined or could not be coerced to a float.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public float getFloat(@android.annotation.StyleableRes
    int index, float defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if (type == android.util.TypedValue.TYPE_FLOAT) {
                return java.lang.Float.intBitsToFloat(data[index + android.content.res.TypedArray.STYLE_DATA]);
            } else
                if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
                    return data[index + android.content.res.TypedArray.STYLE_DATA];
                }


        final android.util.TypedValue v = mValue;
        if (getValueAt(index, v)) {
            final java.lang.CharSequence str = v.coerceToString();
            if (str != null) {
                android.os.StrictMode.noteResourceMismatch(v);
                return java.lang.Float.parseFloat(str.toString());
            }
        }
        // We already checked for TYPE_NULL. This should never happen.
        throw new java.lang.RuntimeException("getFloat of bad type: 0x" + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve the color value for the attribute at <var>index</var>.  If
     * the attribute references a color resource holding a complex
     * {@link android.content.res.ColorStateList}, then the default color from
     * the set is returned.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not an integer color or color state list.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute color value, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer color or color state list.
     */
    @android.annotation.ColorInt
    public int getColor(@android.annotation.StyleableRes
    int index, @android.annotation.ColorInt
    int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return data[index + android.content.res.TypedArray.STYLE_DATA];
            } else
                if (type == android.util.TypedValue.TYPE_STRING) {
                    final android.util.TypedValue value = mValue;
                    if (getValueAt(index, value)) {
                        final android.content.res.ColorStateList csl = mResources.loadColorStateList(value, value.resourceId, mTheme);
                        return csl.getDefaultColor();
                    }
                    return defValue;
                } else
                    if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                        final android.util.TypedValue value = mValue;
                        getValueAt(index, value);
                        throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                    }



        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to color: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve the ComplexColor for the attribute at <var>index</var>.
     * The value may be either a {@link android.content.res.ColorStateList} which can wrap a simple
     * color value or a {@link android.content.res.GradientColor}
     * <p>
     * This method will return {@code null} if the attribute is not defined or
     * is not an integer color, color state list or GradientColor.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return ComplexColor for the attribute, or {@code null} if not defined.
     * @throws RuntimeException
     * 		if the attribute if the TypedArray has already
     * 		been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer color, color state list or GradientColor.
     * @unknown 
     */
    @android.annotation.Nullable
    public android.content.res.ComplexColor getComplexColor(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            if (value.type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + index) + ": ") + value);
            }
            return mResources.loadComplexColor(value, value.resourceId, mTheme);
        }
        return null;
    }

    /**
     * Retrieve the ColorStateList for the attribute at <var>index</var>.
     * The value may be either a single solid color or a reference to
     * a color or complex {@link android.content.res.ColorStateList}
     * description.
     * <p>
     * This method will return {@code null} if the attribute is not defined or
     * is not an integer color or color state list.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return ColorStateList for the attribute, or {@code null} if not
    defined.
     * @throws RuntimeException
     * 		if the attribute if the TypedArray has already
     * 		been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer color or color state list.
     */
    @android.annotation.Nullable
    public android.content.res.ColorStateList getColorStateList(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            if (value.type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + index) + ": ") + value);
            }
            return mResources.loadColorStateList(value, value.resourceId, mTheme);
        }
        return null;
    }

    /**
     * Retrieve the integer value for the attribute at <var>index</var>.
     * <p>
     * Unlike {@link #getInt(int, int)}, this method will throw an exception if
     * the attribute is defined but is not an integer.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute integer value, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer.
     */
    public int getInteger(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return data[index + android.content.res.TypedArray.STYLE_DATA];
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to integer: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var>. Unit
     * conversions are based on the current {@link DisplayMetrics}
     * associated with the resources this {@link TypedArray} object
     * came from.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a dimension.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer.
     * @see #getDimensionPixelOffset
     * @see #getDimensionPixelSize
     */
    public float getDimension(@android.annotation.StyleableRes
    int index, float defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimension(data[index + android.content.res.TypedArray.STYLE_DATA], mMetrics);
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to dimension: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var> for use
     * as an offset in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for you.  An offset conversion involves simply
     * truncating the base value to an integer.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a dimension.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not an integer.
     * @see #getDimension
     * @see #getDimensionPixelSize
     */
    public int getDimensionPixelOffset(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelOffset(data[index + android.content.res.TypedArray.STYLE_DATA], mMetrics);
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to dimension: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var> for use
     * as a size in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for use as a size.  A size conversion involves
     * rounding the base value, and ensuring that a non-zero base value
     * is at least one pixel in size.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a dimension.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not a dimension.
     * @see #getDimension
     * @see #getDimensionPixelOffset
     */
    public int getDimensionPixelSize(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelSize(data[index + android.content.res.TypedArray.STYLE_DATA], mMetrics);
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to dimension: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Special version of {@link #getDimensionPixelSize} for retrieving
     * {@link android.view.ViewGroup}'s layout_width and layout_height
     * attributes.  This is only here for performance reasons; applications
     * should use {@link #getDimensionPixelSize}.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a dimension or integer (enum).
     *
     * @param index
     * 		Index of the attribute to retrieve.
     * @param name
     * 		Textual name of attribute for error reporting.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not a dimension or integer (enum).
     */
    public int getLayoutDimension(@android.annotation.StyleableRes
    int index, java.lang.String name) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
            return data[index + android.content.res.TypedArray.STYLE_DATA];
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelSize(data[index + android.content.res.TypedArray.STYLE_DATA], mMetrics);
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException(((getPositionDescription() + ": You must supply a ") + name) + " attribute.");
    }

    /**
     * Special version of {@link #getDimensionPixelSize} for retrieving
     * {@link android.view.ViewGroup}'s layout_width and layout_height
     * attributes.  This is only here for performance reasons; applications
     * should use {@link #getDimensionPixelSize}.
     *
     * @param index
     * 		Index of the attribute to retrieve.
     * @param defValue
     * 		The default value to return if this attribute is not
     * 		default or contains the wrong type of data.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int getLayoutDimension(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if ((type >= android.util.TypedValue.TYPE_FIRST_INT) && (type <= android.util.TypedValue.TYPE_LAST_INT)) {
            return data[index + android.content.res.TypedArray.STYLE_DATA];
        } else
            if (type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelSize(data[index + android.content.res.TypedArray.STYLE_DATA], mMetrics);
            }

        return defValue;
    }

    /**
     * Retrieves a fractional unit attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param base
     * 		The base value of this fraction.  In other words, a
     * 		standard fraction is multiplied by this value.
     * @param pbase
     * 		The parent base value of this fraction.  In other
     * 		words, a parent fraction (nn%p) is multiplied by this
     * 		value.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute fractional value multiplied by the appropriate
    base value, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not a fraction.
     */
    public float getFraction(@android.annotation.StyleableRes
    int index, int base, int pbase, float defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final int attrIndex = index;
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return defValue;
        } else
            if (type == android.util.TypedValue.TYPE_FRACTION) {
                return android.util.TypedValue.complexToFraction(data[index + android.content.res.TypedArray.STYLE_DATA], base, pbase);
            } else
                if (type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                    final android.util.TypedValue value = mValue;
                    getValueAt(index, value);
                    throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + attrIndex) + ": ") + value);
                }


        throw new java.lang.UnsupportedOperationException((("Can't convert value at index " + attrIndex) + " to fraction: type=0x") + java.lang.Integer.toHexString(type));
    }

    /**
     * Retrieves the resource identifier for the attribute at
     * <var>index</var>.  Note that attribute resource as resolved when
     * the overall {@link TypedArray} object is retrieved.  As a
     * result, this function will return the resource identifier of the
     * final resource value that was found, <em>not</em> necessarily the
     * original resource that was specified by the attribute.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute resource identifier, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    @android.annotation.AnyRes
    public int getResourceId(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        if (data[index + android.content.res.TypedArray.STYLE_TYPE] != android.util.TypedValue.TYPE_NULL) {
            final int resid = data[index + android.content.res.TypedArray.STYLE_RESOURCE_ID];
            if (resid != 0) {
                return resid;
            }
        }
        return defValue;
    }

    /**
     * Retrieves the theme attribute resource identifier for the attribute at
     * <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or not a
     * 		resource.
     * @return Theme attribute resource identifier, or defValue if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @unknown 
     */
    public int getThemeAttributeId(@android.annotation.StyleableRes
    int index, int defValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        if (data[index + android.content.res.TypedArray.STYLE_TYPE] == android.util.TypedValue.TYPE_ATTRIBUTE) {
            return data[index + android.content.res.TypedArray.STYLE_DATA];
        }
        return defValue;
    }

    /**
     * Retrieve the Drawable for the attribute at <var>index</var>.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a color or drawable resource.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Drawable for the attribute, or {@code null} if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not a color or drawable resource.
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawable(@android.annotation.StyleableRes
    int index) {
        return getDrawableForDensity(index, 0);
    }

    /**
     * Version of {@link #getDrawable(int)} that accepts an override density.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawableForDensity(@android.annotation.StyleableRes
    int index, int density) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            if (value.type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + index) + ": ") + value);
            }
            if (density > 0) {
                // If the density is overridden, the value in the TypedArray will not reflect this.
                // Do a separate lookup of the resourceId with the density override.
                mResources.getValueForDensity(value.resourceId, density, value, true);
            }
            return mResources.loadDrawable(value, value.resourceId, density, mTheme);
        }
        return null;
    }

    /**
     * Retrieve the Typeface for the attribute at <var>index</var>.
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a font.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Typeface for the attribute, or {@code null} if not defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @throws UnsupportedOperationException
     * 		if the attribute is defined but is
     * 		not a font resource.
     */
    @android.annotation.Nullable
    public android.graphics.Typeface getFont(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            if (value.type == android.util.TypedValue.TYPE_ATTRIBUTE) {
                throw new java.lang.UnsupportedOperationException((("Failed to resolve attribute at index " + index) + ": ") + value);
            }
            return mResources.getFont(value, value.resourceId);
        }
        return null;
    }

    /**
     * Retrieve the CharSequence[] for the attribute at <var>index</var>.
     * This gets the resource ID of the selected attribute, and uses
     * {@link Resources#getTextArray Resources.getTextArray} of the owning
     * Resources object to retrieve its String[].
     * <p>
     * This method will throw an exception if the attribute is defined but is
     * not a text array resource.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return CharSequence[] for the attribute, or {@code null} if not
    defined.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public java.lang.CharSequence[] getTextArray(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            return mResources.getTextArray(value.resourceId);
        }
        return null;
    }

    /**
     * Retrieve the raw TypedValue for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param outValue
     * 		TypedValue object in which to place the attribute's
     * 		data.
     * @return {@code true} if the value was retrieved and not @empty, {@code false} otherwise.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public boolean getValue(@android.annotation.StyleableRes
    int index, android.util.TypedValue outValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, outValue);
    }

    /**
     * Returns the type of attribute at the specified index.
     *
     * @param index
     * 		Index of attribute whose type to retrieve.
     * @return Attribute type.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public int getType(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        return mData[index + android.content.res.TypedArray.STYLE_TYPE];
    }

    /**
     * Returns the resource ID of the style or layout against which the specified attribute was
     * resolved, otherwise returns defValue.
     *
     * For example, if you we resolving two attributes {@code android:attribute1} and
     * {@code android:attribute2} and you were inflating a {@link android.view.View} from
     * {@code layout/my_layout.xml}:
     * <pre>
     *     &lt;View
     *         style="@style/viewStyle"
     *         android:layout_width="wrap_content"
     *         android:layout_height="wrap_content"
     *         android:attribute1="foo"/&gt;
     * </pre>
     *
     * and {@code @style/viewStyle} is:
     * <pre>
     *     &lt;style android:name="viewStyle"&gt;
     *         &lt;item name="android:attribute2"&gt;bar&lt;item/&gt;
     *     &lt;style/&gt;
     * </pre>
     *
     * then resolved {@link TypedArray} will have values that return source resource ID of
     * {@code R.layout.my_layout} for {@code android:attribute1} and {@code R.style.viewStyle} for
     * {@code android:attribute2}.
     *
     * @param index
     * 		Index of attribute whose source style to retrieve.
     * @param defaultValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Either a style resource ID, layout resource ID, or defaultValue if it was not
    resolved in a style or layout.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    @android.annotation.AnyRes
    public int getSourceResourceId(@android.annotation.StyleableRes
    int index, @android.annotation.AnyRes
    int defaultValue) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int resid = mData[index + android.content.res.TypedArray.STYLE_SOURCE_RESOURCE_ID];
        if (resid != 0) {
            return resid;
        }
        return defaultValue;
    }

    /**
     * Determines whether there is an attribute at <var>index</var>.
     * <p>
     * <strong>Note:</strong> If the attribute was set to {@code @empty} or
     * {@code @undefined}, this method returns {@code false}.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return True if the attribute has a value, false otherwise.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public boolean hasValue(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        return type != android.util.TypedValue.TYPE_NULL;
    }

    /**
     * Determines whether there is an attribute at <var>index</var>, returning
     * {@code true} if the attribute was explicitly set to {@code @empty} and
     * {@code false} only if the attribute was undefined.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return True if the attribute has a value or is empty, false otherwise.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public boolean hasValueOrEmpty(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        index *= android.content.res.TypedArray.STYLE_NUM_ENTRIES;
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        return (type != android.util.TypedValue.TYPE_NULL) || (data[index + android.content.res.TypedArray.STYLE_DATA] == android.util.TypedValue.DATA_NULL_EMPTY);
    }

    /**
     * Retrieve the raw TypedValue for the attribute at <var>index</var>
     * and return a temporary object holding its data.  This object is only
     * valid until the next call on to {@link TypedArray}.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Returns a TypedValue object if the attribute is defined,
    containing its data; otherwise returns null.  (You will not
    receive a TypedValue whose type is TYPE_NULL.)
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public android.util.TypedValue peekValue(@android.annotation.StyleableRes
    int index) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        final android.util.TypedValue value = mValue;
        if (getValueAt(index * android.content.res.TypedArray.STYLE_NUM_ENTRIES, value)) {
            return value;
        }
        return null;
    }

    /**
     * Returns a message about the parser state suitable for printing error messages.
     *
     * @return Human-readable description of current parser state.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public java.lang.String getPositionDescription() {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        return mXml != null ? mXml.getPositionDescription() : "<internal>";
    }

    /**
     * Recycles the TypedArray, to be re-used by a later caller. After calling
     * this function you must not ever touch the typed array again.
     *
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     */
    public void recycle() {
        if (mRecycled) {
            throw new java.lang.RuntimeException(toString() + " recycled twice!");
        }
        mRecycled = true;
        // These may have been set by the client.
        mXml = null;
        mTheme = null;
        mAssets = null;
        mResources.mTypedArrayPool.release(this);
    }

    /**
     * Extracts theme attributes from a typed array for later resolution using
     * {@link android.content.res.Resources.Theme#resolveAttributes(int[], int[])}.
     * Removes the entries from the typed array so that subsequent calls to typed
     * getters will return the default value without crashing.
     *
     * @return an array of length {@link #getIndexCount()} populated with theme
    attributes, or null if there are no theme attributes in the typed
    array
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @unknown 
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    public int[] extractThemeAttrs() {
        return extractThemeAttrs(null);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    public int[] extractThemeAttrs(@android.annotation.Nullable
    int[] scrap) {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        int[] attrs = null;
        final int[] data = mData;
        final int N = length();
        for (int i = 0; i < N; i++) {
            final int index = i * android.content.res.TypedArray.STYLE_NUM_ENTRIES;
            if (data[index + android.content.res.TypedArray.STYLE_TYPE] != android.util.TypedValue.TYPE_ATTRIBUTE) {
                // Not an attribute, ignore.
                continue;
            }
            // Null the entry so that we can safely call getZzz().
            data[index + android.content.res.TypedArray.STYLE_TYPE] = android.util.TypedValue.TYPE_NULL;
            final int attr = data[index + android.content.res.TypedArray.STYLE_DATA];
            if (attr == 0) {
                // Useless data, ignore.
                continue;
            }
            // Ensure we have a usable attribute array.
            if (attrs == null) {
                if ((scrap != null) && (scrap.length == N)) {
                    attrs = scrap;
                    java.util.Arrays.fill(attrs, 0);
                } else {
                    attrs = new int[N];
                }
            }
            attrs[i] = attr;
        }
        return attrs;
    }

    /**
     * Return a mask of the configuration parameters for which the values in
     * this typed array may change.
     *
     * @return Returns a mask of the changing configuration parameters, as
    defined by {@link android.content.pm.ActivityInfo}.
     * @throws RuntimeException
     * 		if the TypedArray has already been recycled.
     * @see android.content.pm.ActivityInfo
     */
    @android.content.pm.ActivityInfo.Config
    public int getChangingConfigurations() {
        if (mRecycled) {
            throw new java.lang.RuntimeException("Cannot make calls to a recycled instance!");
        }
        @android.content.pm.ActivityInfo.Config
        int changingConfig = 0;
        final int[] data = mData;
        final int N = length();
        for (int i = 0; i < N; i++) {
            final int index = i * android.content.res.TypedArray.STYLE_NUM_ENTRIES;
            final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
            if (type == android.util.TypedValue.TYPE_NULL) {
                continue;
            }
            changingConfig |= android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(data[index + android.content.res.TypedArray.STYLE_CHANGING_CONFIGURATIONS]);
        }
        return changingConfig;
    }

    @android.annotation.UnsupportedAppUsage
    private boolean getValueAt(int index, android.util.TypedValue outValue) {
        final int[] data = mData;
        final int type = data[index + android.content.res.TypedArray.STYLE_TYPE];
        if (type == android.util.TypedValue.TYPE_NULL) {
            return false;
        }
        outValue.type = type;
        outValue.data = data[index + android.content.res.TypedArray.STYLE_DATA];
        outValue.assetCookie = data[index + android.content.res.TypedArray.STYLE_ASSET_COOKIE];
        outValue.resourceId = data[index + android.content.res.TypedArray.STYLE_RESOURCE_ID];
        outValue.changingConfigurations = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(data[index + android.content.res.TypedArray.STYLE_CHANGING_CONFIGURATIONS]);
        outValue.density = data[index + android.content.res.TypedArray.STYLE_DENSITY];
        outValue.string = (type == android.util.TypedValue.TYPE_STRING) ? loadStringValueAt(index) : null;
        outValue.sourceResourceId = data[index + android.content.res.TypedArray.STYLE_SOURCE_RESOURCE_ID];
        return true;
    }

    private java.lang.CharSequence loadStringValueAt(int index) {
        final int[] data = mData;
        final int cookie = data[index + android.content.res.TypedArray.STYLE_ASSET_COOKIE];
        if (cookie < 0) {
            if (mXml != null) {
                return mXml.getPooledString(data[index + android.content.res.TypedArray.STYLE_DATA]);
            }
            return null;
        }
        return mAssets.getPooledStringForCookie(cookie, data[index + android.content.res.TypedArray.STYLE_DATA]);
    }

    /**
     *
     *
     * @unknown 
     */
    protected TypedArray(android.content.res.Resources resources) {
        mResources = resources;
        mMetrics = mResources.getDisplayMetrics();
        mAssets = mResources.getAssets();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.util.Arrays.toString(mData);
    }
}

