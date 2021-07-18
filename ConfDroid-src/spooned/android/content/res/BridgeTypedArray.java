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
 * Custom implementation of TypedArray to handle non compiled resources.
 */
public final class BridgeTypedArray extends android.content.res.TypedArray {
    private final android.content.res.Resources mBridgeResources;

    private final com.android.layoutlib.bridge.android.BridgeContext mContext;

    private final int[] mResourceId;

    private final com.android.ide.common.rendering.api.ResourceValue[] mResourceData;

    private final java.lang.String[] mNames;

    private final com.android.ide.common.rendering.api.ResourceNamespace[] mNamespaces;

    // Contains ids that are @empty. We still store null in mResourceData for that index, since we
    // want to save on the check against empty, each time a resource value is requested.
    @android.annotation.Nullable
    private int[] mEmptyIds;

    public BridgeTypedArray(android.content.res.Resources resources, com.android.layoutlib.bridge.android.BridgeContext context, int len) {
        super(resources);
        mBridgeResources = resources;
        mContext = context;
        mResourceId = new int[len];
        mResourceData = new com.android.ide.common.rendering.api.ResourceValue[len];
        mNames = new java.lang.String[len];
        mNamespaces = new com.android.ide.common.rendering.api.ResourceNamespace[len];
    }

    /**
     * A bridge-specific method that sets a value in the type array
     *
     * @param index
     * 		the index of the value in the TypedArray
     * @param name
     * 		the name of the attribute
     * @param namespace
     * 		namespace of the attribute
     * @param resourceId
     * 		the reference id of this resource
     * @param value
     * 		the value of the attribute
     */
    public void bridgeSetValue(int index, java.lang.String name, com.android.ide.common.rendering.api.ResourceNamespace namespace, int resourceId, com.android.ide.common.rendering.api.ResourceValue value) {
        mResourceId[index] = resourceId;
        mResourceData[index] = value;
        mNames[index] = name;
        mNamespaces[index] = namespace;
    }

    /**
     * Seals the array after all calls to
     * {@link #bridgeSetValue(int, String, ResourceNamespace, int, ResourceValue)} have been done.
     * <p/>This allows to compute the list of non default values, permitting
     * {@link #getIndexCount()} to return the proper value.
     */
    public void sealArray() {
        // fills TypedArray.mIndices which is used to implement getIndexCount/getIndexAt
        // first count the array size
        int count = 0;
        java.util.ArrayList<java.lang.Integer> emptyIds = null;
        for (int i = 0; i < mResourceData.length; i++) {
            com.android.ide.common.rendering.api.ResourceValue data = mResourceData[i];
            if (data != null) {
                java.lang.String dataValue = data.getValue();
                if (android.content.res.REFERENCE_NULL.equals(dataValue) || android.content.res.REFERENCE_UNDEFINED.equals(dataValue)) {
                    mResourceData[i] = null;
                } else
                    if (android.content.res.REFERENCE_EMPTY.equals(dataValue)) {
                        mResourceData[i] = null;
                        if (emptyIds == null) {
                            emptyIds = new java.util.ArrayList<>(4);
                        }
                        emptyIds.add(i);
                    } else {
                        count++;
                    }

            }
        }
        if (emptyIds != null) {
            mEmptyIds = new int[emptyIds.size()];
            for (int i = 0; i < emptyIds.size(); i++) {
                mEmptyIds[i] = emptyIds.get(i);
            }
        }
        // allocate the table with an extra to store the size
        mIndices = new int[count + 1];
        mIndices[0] = count;
        // fill the array with the indices.
        int index = 1;
        for (int i = 0; i < mResourceData.length; i++) {
            if (mResourceData[i] != null) {
                mIndices[index++] = i;
            }
        }
    }

    /**
     * Set the theme to be used for inflating drawables.
     */
    public void setTheme(android.content.res.Resources.Theme theme) {
        mTheme = theme;
    }

    /**
     * Return the number of values in this array.
     */
    @java.lang.Override
    public int length() {
        return mResourceData.length;
    }

    /**
     * Return the Resources object this array was loaded from.
     */
    @java.lang.Override
    public android.content.res.Resources getResources() {
        return mBridgeResources;
    }

    /**
     * Retrieve the styled string value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return CharSequence holding string data.  May be styled.  Returns
    null if the attribute is not defined.
     */
    @java.lang.Override
    public java.lang.CharSequence getText(int index) {
        // FIXME: handle styled strings!
        return getString(index);
    }

    /**
     * Retrieve the string value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return String holding string data.  Any styling information is
    removed.  Returns null if the attribute is not defined.
     */
    @java.lang.Override
    public java.lang.String getString(int index) {
        if (!hasValue(index)) {
            return null;
        }
        // As unfortunate as it is, it's possible to use enums with all attribute formats,
        // not just integers/enums. So, we need to search the enums always. In case
        // enums are used, the returned value is an integer.
        java.lang.Integer v = resolveEnumAttribute(index);
        return v == null ? getValue() : java.lang.String.valueOf(((int) (v)));
    }

    /**
     * Retrieve the boolean value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined.
     * @return Attribute boolean value, or defValue if not defined.
     */
    @java.lang.Override
    public boolean getBoolean(int index, boolean defValue) {
        java.lang.String s = getString(index);
        return s == null ? defValue : com.android.internal.util.XmlUtils.convertValueToBoolean(s, defValue);
    }

    /**
     * Retrieve the integer value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined.
     * @return Attribute int value, or defValue if not defined.
     */
    @java.lang.Override
    public int getInt(int index, int defValue) {
        java.lang.String s = getString(index);
        try {
            return android.content.res.BridgeTypedArray.convertValueToInt(s, defValue);
        } catch (java.lang.NumberFormatException e) {
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_RESOURCES_FORMAT, java.lang.String.format("\"%1$s\" in attribute \"%2$s\" is not a valid integer", s, mNames[index]), null);
        }
        return defValue;
    }

    /**
     * Retrieve the float value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Attribute float value, or defValue if not defined..
     */
    @java.lang.Override
    public float getFloat(int index, float defValue) {
        java.lang.String s = getString(index);
        try {
            if (s != null) {
                return java.lang.Float.parseFloat(s);
            }
        } catch (java.lang.NumberFormatException e) {
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_RESOURCES_FORMAT, java.lang.String.format("\"%1$s\" in attribute \"%2$s\" cannot be converted to float.", s, mNames[index]), null);
        }
        return defValue;
    }

    /**
     * Retrieve the color value for the attribute at <var>index</var>.  If
     * the attribute references a color resource holding a complex
     * {@link android.content.res.ColorStateList}, then the default color from
     * the set is returned.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute color value, or defValue if not defined.
     */
    @java.lang.Override
    public int getColor(int index, int defValue) {
        if ((index < 0) || (index >= mResourceData.length)) {
            return defValue;
        }
        if (mResourceData[index] == null) {
            return defValue;
        }
        android.content.res.ColorStateList colorStateList = com.android.layoutlib.bridge.impl.ResourceHelper.getColorStateList(mResourceData[index], mContext, mTheme);
        if (colorStateList != null) {
            return colorStateList.getDefaultColor();
        }
        return defValue;
    }

    @java.lang.Override
    public android.content.res.ColorStateList getColorStateList(int index) {
        if (!hasValue(index)) {
            return null;
        }
        return com.android.layoutlib.bridge.impl.ResourceHelper.getColorStateList(mResourceData[index], mContext, mTheme);
    }

    @java.lang.Override
    public android.content.res.ComplexColor getComplexColor(int index) {
        if (!hasValue(index)) {
            return null;
        }
        return com.android.layoutlib.bridge.impl.ResourceHelper.getComplexColor(mResourceData[index], mContext, mTheme);
    }

    /**
     * Retrieve the integer value for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute integer value, or defValue if not defined.
     */
    @java.lang.Override
    public int getInteger(int index, int defValue) {
        return getInt(index, defValue);
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var>.  Unit
     * conversions are based on the current {@link DisplayMetrics}
     * associated with the resources this {@link TypedArray} object
     * came from.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric, or defValue if not defined.
     * @see #getDimensionPixelOffset
     * @see #getDimensionPixelSize
     */
    @java.lang.Override
    public float getDimension(int index, float defValue) {
        java.lang.String s = getString(index);
        if (s == null) {
            return defValue;
        }
        // Check if the value is a magic constant that doesn't require a unit.
        try {
            int i = java.lang.Integer.parseInt(s);
            if ((i == android.view.ViewGroup.LayoutParams.MATCH_PARENT) || (i == android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
                return i;
            }
        } catch (java.lang.NumberFormatException ignored) {
            // pass
        }
        if (com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(mNames[index], s, mValue, true)) {
            return mValue.getDimension(mBridgeResources.getDisplayMetrics());
        }
        return defValue;
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var> for use
     * as an offset in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for you.  An offset conversion involves simply
     * truncating the base value to an integer.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels, or defValue if not defined.
     * @see #getDimension
     * @see #getDimensionPixelSize
     */
    @java.lang.Override
    public int getDimensionPixelOffset(int index, int defValue) {
        return ((int) (getDimension(index, defValue)));
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var> for use
     * as a size in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for use as a size.  A size conversion involves
     * rounding the base value, and ensuring that a non-zero base value
     * is at least one pixel in size.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param defValue
     * 		Value to return if the attribute is not defined or
     * 		not a resource.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels, or defValue if not defined.
     * @see #getDimension
     * @see #getDimensionPixelOffset
     */
    @java.lang.Override
    public int getDimensionPixelSize(int index, int defValue) {
        try {
            return getDimension(index, null);
        } catch (java.lang.RuntimeException e) {
            java.lang.String s = getString(index);
            if (s != null) {
                // looks like we were unable to resolve the dimension value
                com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_RESOURCES_FORMAT, java.lang.String.format("\"%1$s\" in attribute \"%2$s\" is not a valid format.", s, mNames[index]), null);
            }
            return defValue;
        }
    }

    /**
     * Special version of {@link #getDimensionPixelSize} for retrieving
     * {@link android.view.ViewGroup}'s layout_width and layout_height
     * attributes.  This is only here for performance reasons; applications
     * should use {@link #getDimensionPixelSize}.
     *
     * @param index
     * 		Index of the attribute to retrieve.
     * @param name
     * 		Textual name of attribute for error reporting.
     * @return Attribute dimension value multiplied by the appropriate
    metric and truncated to integer pixels.
     */
    @java.lang.Override
    public int getLayoutDimension(int index, java.lang.String name) {
        try {
            // this will throw an exception if not found.
            return getDimension(index, name);
        } catch (java.lang.RuntimeException e) {
            if (android.view.LayoutInflater_Delegate.sIsInInclude) {
                throw new java.lang.RuntimeException(("Layout Dimension '" + name) + "' not found.");
            }
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_RESOURCES_FORMAT, ("You must supply a " + name) + " attribute.", null);
            return 0;
        }
    }

    @java.lang.Override
    public int getLayoutDimension(int index, int defValue) {
        return getDimensionPixelSize(index, defValue);
    }

    /**
     *
     *
     * @param name
     * 		attribute name, used for error reporting.
     */
    private int getDimension(int index, @android.annotation.Nullable
    java.lang.String name) {
        java.lang.String s = getString(index);
        if (s == null) {
            if (name != null) {
                throw new java.lang.RuntimeException(("Attribute '" + name) + "' not found");
            }
            throw new java.lang.RuntimeException();
        }
        // Check if the value is a magic constant that doesn't require a unit.
        try {
            int i = java.lang.Integer.parseInt(s);
            if ((i == android.view.ViewGroup.LayoutParams.MATCH_PARENT) || (i == android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
                return i;
            }
        } catch (java.lang.NumberFormatException ignored) {
            // pass
        }
        if (com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(mNames[index], s, mValue, true)) {
            float f = mValue.getDimension(mBridgeResources.getDisplayMetrics());
            final int res = ((int) (f + 0.5F));
            if (res != 0)
                return res;

            if (f == 0)
                return 0;

            if (f > 0)
                return 1;

        }
        throw new java.lang.RuntimeException();
    }

    /**
     * Retrieve a fractional unit attribute at <var>index</var>.
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
     */
    @java.lang.Override
    public float getFraction(int index, int base, int pbase, float defValue) {
        java.lang.String value = getString(index);
        if (value == null) {
            return defValue;
        }
        if (com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(mNames[index], value, mValue, false)) {
            return mValue.getFraction(base, pbase);
        }
        // looks like we were unable to resolve the fraction value
        com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_RESOURCES_FORMAT, java.lang.String.format("\"%1$s\" in attribute \"%2$s\" cannot be converted to a fraction.", value, mNames[index]), null);
        return defValue;
    }

    /**
     * Retrieve the resource identifier for the attribute at
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
     */
    @java.lang.Override
    public int getResourceId(int index, int defValue) {
        if ((index < 0) || (index >= mResourceData.length)) {
            return defValue;
        }
        // get the Resource for this index
        com.android.ide.common.rendering.api.ResourceValue resValue = mResourceData[index];
        // no data, return the default value.
        if (resValue == null) {
            return defValue;
        }
        // check if this is a style resource
        if (resValue instanceof com.android.ide.common.rendering.api.StyleResourceValue) {
            // get the id that will represent this style.
            return mContext.getDynamicIdByStyle(((com.android.ide.common.rendering.api.StyleResourceValue) (resValue)));
        }
        // If the attribute was a reference to a resource, and not a declaration of an id (@+id),
        // then the xml attribute value was "resolved" which leads us to a ResourceValue with a
        // valid type, name, namespace and a potentially null value.
        if (!(resValue instanceof com.android.layoutlib.bridge.android.UnresolvedResourceValue)) {
            return mContext.getResourceId(resValue.asReference(), defValue);
        }
        // else, try to get the value, and resolve it somehow.
        java.lang.String value = resValue.getValue();
        if (value == null) {
            return defValue;
        }
        value = value.trim();
        // `resValue` failed to be resolved. We extract the interesting bits and get rid of this
        // broken object. The namespace and resolver come from where the XML attribute was defined.
        com.android.ide.common.rendering.api.ResourceNamespace contextNamespace = resValue.getNamespace();
        com.android.ide.common.rendering.api.ResourceNamespace.Resolver namespaceResolver = resValue.getNamespaceResolver();
        if (value.startsWith("#")) {
            // this looks like a color, do not try to parse it
            return defValue;
        }
        if (android.graphics.Typeface_Accessor.isSystemFont(value)) {
            // A system font family value, do not try to parse
            return defValue;
        }
        // Handle the @id/<name>, @+id/<name> and @android:id/<name>
        // We need to return the exact value that was compiled (from the various R classes),
        // as these values can be reused internally with calls to findViewById().
        // There's a trick with platform layouts that not use "android:" but their IDs are in
        // fact in the android.R and com.android.internal.R classes.
        // The field mPlatformFile will indicate that all IDs are to be looked up in the android R
        // classes exclusively.
        // if this is a reference to an id, find it.
        com.android.resources.ResourceUrl resourceUrl = com.android.resources.ResourceUrl.parse(value);
        if (resourceUrl != null) {
            if (resourceUrl.type == com.android.resources.ResourceType.ID) {
                com.android.ide.common.rendering.api.ResourceReference referencedId = resourceUrl.resolve(contextNamespace, namespaceResolver);
                // Look for the idName in project or android R class depending on isPlatform.
                if (resourceUrl.isCreate()) {
                    int idValue;
                    if (referencedId.getNamespace() == com.android.ide.common.rendering.api.ResourceNamespace.ANDROID) {
                        idValue = com.android.layoutlib.bridge.Bridge.getResourceId(ResourceType.ID, resourceUrl.name);
                    } else {
                        idValue = mContext.getLayoutlibCallback().getOrGenerateResourceId(referencedId);
                    }
                    return idValue;
                }
                // This calls the same method as in if(create), but doesn't create a dynamic id, if
                // one is not found.
                return mContext.getResourceId(referencedId, defValue);
            } else
                if (resourceUrl.type == com.android.resources.ResourceType.AAPT) {
                    com.android.ide.common.rendering.api.ResourceReference referencedId = resourceUrl.resolve(contextNamespace, namespaceResolver);
                    return mContext.getLayoutlibCallback().getOrGenerateResourceId(referencedId);
                }

        }
        // not a direct id valid reference. First check if it's an enum (this is a corner case
        // for attributes that have a reference|enum type), then fallback to resolve
        // as an ID without prefix.
        java.lang.Integer enumValue = resolveEnumAttribute(index);
        if (enumValue != null) {
            return enumValue;
        }
        return defValue;
    }

    @java.lang.Override
    public int getThemeAttributeId(int index, int defValue) {
        // TODO: Get the right Theme Attribute ID to enable caching of the drawables.
        return defValue;
    }

    /**
     * Retrieve the Drawable for the attribute at <var>index</var>.  This
     * gets the resource ID of the selected attribute, and uses
     * {@link Resources#getDrawable Resources.getDrawable} of the owning
     * Resources object to retrieve its Drawable.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Drawable for the attribute, or null if not defined.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawable(int index) {
        if (!hasValue(index)) {
            return null;
        }
        com.android.ide.common.rendering.api.ResourceValue value = mResourceData[index];
        return com.android.layoutlib.bridge.impl.ResourceHelper.getDrawable(value, mContext, mTheme);
    }

    /**
     * Version of {@link #getDrawable(int)} that accepts an override density.
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawableForDensity(int index, int density) {
        return getDrawable(index);
    }

    /**
     * Retrieve the Typeface for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Typeface for the attribute, or null if not defined.
     */
    @java.lang.Override
    public android.graphics.Typeface getFont(int index) {
        if (!hasValue(index)) {
            return null;
        }
        com.android.ide.common.rendering.api.ResourceValue value = mResourceData[index];
        return com.android.layoutlib.bridge.impl.ResourceHelper.getFont(value, mContext, mTheme);
    }

    /**
     * Retrieve the CharSequence[] for the attribute at <var>index</var>.
     * This gets the resource ID of the selected attribute, and uses
     * {@link Resources#getTextArray Resources.getTextArray} of the owning
     * Resources object to retrieve its String[].
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return CharSequence[] for the attribute, or null if not defined.
     */
    @java.lang.Override
    public java.lang.CharSequence[] getTextArray(int index) {
        if (!hasValue(index)) {
            return null;
        }
        com.android.ide.common.rendering.api.ResourceValue resVal = mResourceData[index];
        if (resVal instanceof com.android.ide.common.rendering.api.ArrayResourceValue) {
            com.android.ide.common.rendering.api.ArrayResourceValue array = ((com.android.ide.common.rendering.api.ArrayResourceValue) (resVal));
            int count = array.getElementCount();
            return count >= 0 ? android.content.res.Resources_Delegate.resolveValues(mBridgeResources, array) : null;
        }
        int id = getResourceId(index, 0);
        java.lang.String resIdMessage = (id > 0) ? (" (resource id 0x" + java.lang.Integer.toHexString(id)) + ')' : "";
        assert false : java.lang.String.format("%1$s in %2$s%3$s is not a valid array resource.", resVal.getValue(), mNames[index], resIdMessage);
        return new java.lang.CharSequence[0];
    }

    @java.lang.Override
    public int[] extractThemeAttrs() {
        // The drawables are always inflated with a Theme and we don't care about caching. So,
        // just return.
        return null;
    }

    @java.lang.Override
    public int getChangingConfigurations() {
        // We don't care about caching. Any change in configuration is a fresh render. So,
        // just return.
        return 0;
    }

    /**
     * Retrieve the raw TypedValue for the attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @param outValue
     * 		TypedValue object in which to place the attribute's
     * 		data.
     * @return Returns true if the value was retrieved, else false.
     */
    @java.lang.Override
    public boolean getValue(int index, android.util.TypedValue outValue) {
        // TODO: more switch cases for other types.
        outValue.type = getType(index);
        switch (outValue.type) {
            case android.util.TypedValue.TYPE_NULL :
                return false;
            case android.util.TypedValue.TYPE_STRING :
                outValue.string = getString(index);
                return true;
            case android.util.TypedValue.TYPE_REFERENCE :
                outValue.resourceId = mResourceId[index];
                return true;
            case android.util.TypedValue.TYPE_INT_COLOR_ARGB4 :
            case android.util.TypedValue.TYPE_INT_COLOR_ARGB8 :
            case android.util.TypedValue.TYPE_INT_COLOR_RGB4 :
            case android.util.TypedValue.TYPE_INT_COLOR_RGB8 :
                android.content.res.ColorStateList colorStateList = getColorStateList(index);
                if (colorStateList == null) {
                    return false;
                }
                outValue.data = colorStateList.getDefaultColor();
                return true;
            default :
                // For back-compatibility, parse as float.
                java.lang.String s = getString(index);
                return (s != null) && com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(mNames[index], s, outValue, false);
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("ResultOfMethodCallIgnored")
    public int getType(int index) {
        java.lang.String value = getString(index);
        if (value == null) {
            return android.util.TypedValue.TYPE_NULL;
        }
        if (value.startsWith(com.android.SdkConstants.PREFIX_RESOURCE_REF)) {
            return android.util.TypedValue.TYPE_REFERENCE;
        }
        if (value.startsWith(com.android.SdkConstants.PREFIX_THEME_REF)) {
            return android.util.TypedValue.TYPE_ATTRIBUTE;
        }
        try {
            // Don't care about the value. Only called to check if an exception is thrown.
            android.content.res.BridgeTypedArray.convertValueToInt(value, 0);
            if (value.startsWith("0x") || value.startsWith("0X")) {
                return android.util.TypedValue.TYPE_INT_HEX;
            }
            // is it a color?
            if (value.startsWith("#")) {
                int length = value.length() - 1;
                if (length == 3) {
                    // rgb
                    return android.util.TypedValue.TYPE_INT_COLOR_RGB4;
                }
                if (length == 4) {
                    // argb
                    return android.util.TypedValue.TYPE_INT_COLOR_ARGB4;
                }
                if (length == 6) {
                    // rrggbb
                    return android.util.TypedValue.TYPE_INT_COLOR_RGB8;
                }
                if (length == 8) {
                    // aarrggbb
                    return android.util.TypedValue.TYPE_INT_COLOR_ARGB8;
                }
            }
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return android.util.TypedValue.TYPE_INT_BOOLEAN;
            }
            return android.util.TypedValue.TYPE_INT_DEC;
        } catch (java.lang.NumberFormatException ignored) {
            try {
                java.lang.Float.parseFloat(value);
                return android.util.TypedValue.TYPE_FLOAT;
            } catch (java.lang.NumberFormatException ignore) {
            }
            // Might be a dimension.
            if (com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(null, value, new android.util.TypedValue(), false)) {
                return android.util.TypedValue.TYPE_DIMENSION;
            }
        }
        // TODO: handle fractions.
        return android.util.TypedValue.TYPE_STRING;
    }

    /**
     * Determines whether there is an attribute at <var>index</var>.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return True if the attribute has a value, false otherwise.
     */
    @java.lang.Override
    public boolean hasValue(int index) {
        return ((index >= 0) && (index < mResourceData.length)) && (mResourceData[index] != null);
    }

    @java.lang.Override
    public boolean hasValueOrEmpty(int index) {
        return hasValue(index) || ((((index >= 0) && (index < mResourceData.length)) && (mEmptyIds != null)) && (java.util.Arrays.binarySearch(mEmptyIds, index) >= 0));
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
     */
    @java.lang.Override
    public android.util.TypedValue peekValue(int index) {
        if ((index < 0) || (index >= mResourceData.length)) {
            return null;
        }
        if (getValue(index, mValue)) {
            return mValue;
        }
        return null;
    }

    /**
     * Returns a message about the parser state suitable for printing error messages.
     */
    @java.lang.Override
    public java.lang.String getPositionDescription() {
        return "<internal -- stub if needed>";
    }

    /**
     * Give back a previously retrieved TypedArray, for later re-use.
     */
    @java.lang.Override
    public void recycle() {
        // pass
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.util.Arrays.toString(mResourceData);
    }

    /**
     * Searches for the string in the attributes (flag or enums) and returns the integer.
     * If found, it will return an integer matching the value.
     *
     * @param index
     * 		Index of attribute to retrieve.
     * @return Attribute int value, or null if not defined.
     */
    private java.lang.Integer resolveEnumAttribute(int index) {
        // Get the map of attribute-constant -> IntegerValue
        java.util.Map<java.lang.String, java.lang.Integer> map = null;
        if (mNamespaces[index] == com.android.ide.common.rendering.api.ResourceNamespace.ANDROID) {
            map = com.android.layoutlib.bridge.Bridge.getEnumValues(mNames[index]);
        } else {
            // get the styleable matching the resolved name
            com.android.ide.common.rendering.api.RenderResources res = mContext.getRenderResources();
            com.android.ide.common.rendering.api.ResourceValue attr = res.getResolvedResource(com.android.ide.common.rendering.api.ResourceReference.attr(mNamespaces[index], mNames[index]));
            if (attr instanceof com.android.ide.common.rendering.api.AttrResourceValue) {
                map = ((com.android.ide.common.rendering.api.AttrResourceValue) (attr)).getAttributeValues();
            }
        }
        if ((map != null) && (!map.isEmpty())) {
            // Accumulator to store the value of the 1+ constants.
            int result = 0;
            boolean found = false;
            java.lang.String value = getValue();
            if (!value.isEmpty()) {
                // Check if the value string is already representing an integer and return it if so.
                // Resources coming from res.apk in an AAR may have flags and enums in integer form.
                char c = value.charAt(0);
                if ((java.lang.Character.isDigit(c) || (c == '-')) || (c == '+')) {
                    try {
                        return android.content.res.BridgeTypedArray.convertValueToInt(value, 0);
                    } catch (java.lang.NumberFormatException e) {
                        // Ignore and continue.
                    }
                }
                // Split the value in case it is a mix of several flags.
                java.lang.String[] keywords = value.split("\\|");
                for (java.lang.String keyword : keywords) {
                    java.lang.Integer i = map.get(keyword.trim());
                    if (i != null) {
                        result |= i;
                        found = true;
                    }
                    // TODO: We should act smartly and log a warning for incorrect keywords. However,
                    // this method is currently called even if the resourceValue is not an enum.
                }
                if (found) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Copied from {@link XmlUtils#convertValueToInt(CharSequence, int)}, but adapted to account
     * for aapt, and the fact that host Java VM's Integer.parseInt("XXXXXXXX", 16) cannot handle
     * "XXXXXXXX" > 80000000.
     */
    private static int convertValueToInt(@android.annotation.Nullable
    java.lang.String charSeq, int defValue) {
        if ((null == charSeq) || charSeq.isEmpty())
            return defValue;

        int sign = 1;
        int index = 0;
        int len = charSeq.length();
        int base = 10;
        if ('-' == charSeq.charAt(0)) {
            sign = -1;
            index++;
        }
        if ('0' == charSeq.charAt(index)) {
            // Quick check for a zero by itself
            if (index == (len - 1))
                return 0;

            char c = charSeq.charAt(index + 1);
            if (('x' == c) || ('X' == c)) {
                index += 2;
                base = 16;
            } else {
                index++;
                // Leave the base as 10. aapt removes the preceding zero, and thus when framework
                // sees the value, it only gets the decimal value.
            }
        } else
            if ('#' == charSeq.charAt(index)) {
                return com.android.layoutlib.bridge.impl.ResourceHelper.getColor(charSeq) * sign;
            } else
                if ("true".equals(charSeq) || "TRUE".equals(charSeq)) {
                    return -1;
                } else
                    if ("false".equals(charSeq) || "FALSE".equals(charSeq)) {
                        return 0;
                    }



        // Use Long, since we want to handle hex ints > 80000000.
        return ((int) (java.lang.Long.parseLong(charSeq.substring(index), base))) * sign;
    }

    static android.content.res.TypedArray obtain(android.content.res.Resources res, int len) {
        return new android.content.res.BridgeTypedArray(res, null, len);
    }
}

