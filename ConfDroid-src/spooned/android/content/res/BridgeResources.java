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


import static value.string.toString;


/**
 *
 */
public final class BridgeResources extends android.content.res.Resources {
    private com.android.layoutlib.bridge.android.BridgeContext mContext;

    private com.android.ide.common.rendering.api.LayoutlibCallback mLayoutlibCallback;

    private boolean[] mPlatformResourceFlag = new boolean[1];

    private android.util.TypedValue mTmpValue = new android.util.TypedValue();

    /**
     * Simpler wrapper around FileInputStream. This is used when the input stream represent
     * not a normal bitmap but a nine patch.
     * This is useful when the InputStream is created in a method but used in another that needs
     * to know whether this is 9-patch or not, such as BitmapFactory.
     */
    public class NinePatchInputStream extends java.io.FileInputStream {
        private boolean mFakeMarkSupport = true;

        public NinePatchInputStream(java.io.File file) throws java.io.FileNotFoundException {
            super(file);
        }

        @java.lang.Override
        public boolean markSupported() {
            if (mFakeMarkSupport) {
                // this is needed so that BitmapFactory doesn't wrap this in a BufferedInputStream.
                return true;
            }
            return super.markSupported();
        }

        public void disableFakeMarkSupport() {
            // disable fake mark support so that in case codec actually try to use them
            // we don't lie to them.
            mFakeMarkSupport = false;
        }
    }

    /**
     * This initializes the static field {@link Resources#mSystem} which is used
     * by methods who get global resources using {@link Resources#getSystem()}.
     * <p/>
     * They will end up using our bridge resources.
     * <p/>
     * {@link Bridge} calls this method after setting up a new bridge.
     */
    public static android.content.res.Resources initSystem(com.android.layoutlib.bridge.android.BridgeContext context, android.content.res.AssetManager assets, android.util.DisplayMetrics metrics, android.content.res.Configuration config, com.android.ide.common.rendering.api.LayoutlibCallback layoutlibCallback) {
        return android.content.res.Resources.mSystem = new android.content.res.BridgeResources(context, assets, metrics, config, layoutlibCallback);
    }

    /**
     * Disposes the static {@link Resources#mSystem} to make sure we don't leave objects
     * around that would prevent us from unloading the library.
     */
    public static void disposeSystem() {
        if (android.content.res.Resources.mSystem instanceof android.content.res.BridgeResources) {
            ((android.content.res.BridgeResources) (android.content.res.Resources.mSystem)).mContext = null;
            ((android.content.res.BridgeResources) (android.content.res.Resources.mSystem)).mLayoutlibCallback = null;
        }
        android.content.res.Resources.mSystem = null;
    }

    private BridgeResources(com.android.layoutlib.bridge.android.BridgeContext context, android.content.res.AssetManager assets, android.util.DisplayMetrics metrics, android.content.res.Configuration config, com.android.ide.common.rendering.api.LayoutlibCallback layoutlibCallback) {
        super(assets, metrics, config);
        mContext = context;
        mLayoutlibCallback = layoutlibCallback;
    }

    public android.content.res.BridgeTypedArray newTypeArray(int numEntries, boolean platformFile) {
        return new android.content.res.BridgeTypedArray(this, mContext, numEntries, platformFile);
    }

    private com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> getResourceValue(int id, boolean[] platformResFlag_out) {
        // first get the String related to this id in the framework
        com.android.util.Pair<com.android.resources.ResourceType, java.lang.String> resourceInfo = com.android.layoutlib.bridge.Bridge.resolveResourceId(id);
        if (resourceInfo != null) {
            platformResFlag_out[0] = true;
            java.lang.String attributeName = resourceInfo.getSecond();
            return com.android.util.Pair.of(attributeName, mContext.getRenderResources().getFrameworkResource(resourceInfo.getFirst(), attributeName));
        }
        // didn't find a match in the framework? look in the project.
        if (mLayoutlibCallback != null) {
            resourceInfo = mLayoutlibCallback.resolveResourceId(id);
            if (resourceInfo != null) {
                platformResFlag_out[0] = false;
                java.lang.String attributeName = resourceInfo.getSecond();
                return com.android.util.Pair.of(attributeName, mContext.getRenderResources().getProjectResource(resourceInfo.getFirst(), attributeName));
            }
        }
        return null;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(int id, android.content.res.Resources.Theme theme) {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            return com.android.layoutlib.bridge.impl.ResourceHelper.getDrawable(value.getSecond(), mContext, theme);
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public int getColor(int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resourceValue = value.getSecond();
            try {
                return com.android.layoutlib.bridge.impl.ResourceHelper.getColor(resourceValue.getValue());
            } catch (java.lang.NumberFormatException e) {
                // Check if the value passed is a file. If it is, mostly likely, user is referencing
                // a color state list from a place where they should reference only a pure color.
                java.lang.String message;
                if (new java.io.File(resourceValue.getValue()).isFile()) {
                    java.lang.String resource = ((resourceValue.isFramework() ? "@android:" : "@") + "color/") + resourceValue.getName();
                    message = "Hexadecimal color expected, found Color State List for " + resource;
                } else {
                    message = e.getMessage();
                }
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, message, e, null);
                return 0;
            }
        }
        // Suppress possible NPE. getColorStateList will never return null, it will instead
        // throw an exception, but intelliJ can't figure that out
        // noinspection ConstantConditions
        return getColorStateList(id, theme).getDefaultColor();
    }

    @java.lang.Override
    public android.content.res.ColorStateList getColorStateList(int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> resValue = getResourceValue(id, mPlatformResourceFlag);
        if (resValue != null) {
            android.content.res.ColorStateList stateList = com.android.layoutlib.bridge.impl.ResourceHelper.getColorStateList(resValue.getSecond(), mContext);
            if (stateList != null) {
                return stateList.obtainForTheme(theme);
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public java.lang.CharSequence getText(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    return v;
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public java.lang.CharSequence[] getTextArray(int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue resValue = getArrayResourceValue(id);
        if (resValue == null) {
            // Error already logged by getArrayResourceValue.
            return new java.lang.CharSequence[0];
        } else
            if (!(resValue instanceof com.android.ide.common.rendering.api.ArrayResourceValue)) {
                return new java.lang.CharSequence[]{ resolveReference(resValue.getValue(), resValue.isFramework()) };
            }

        com.android.ide.common.rendering.api.ArrayResourceValue arv = ((com.android.ide.common.rendering.api.ArrayResourceValue) (resValue));
        return fillValues(arv, new java.lang.CharSequence[arv.getElementCount()]);
    }

    @java.lang.Override
    public java.lang.String[] getStringArray(int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue resValue = getArrayResourceValue(id);
        if (resValue == null) {
            // Error already logged by getArrayResourceValue.
            return new java.lang.String[0];
        } else
            if (!(resValue instanceof com.android.ide.common.rendering.api.ArrayResourceValue)) {
                return new java.lang.String[]{ resolveReference(resValue.getValue(), resValue.isFramework()) };
            }

        com.android.ide.common.rendering.api.ArrayResourceValue arv = ((com.android.ide.common.rendering.api.ArrayResourceValue) (resValue));
        return fillValues(arv, new java.lang.String[arv.getElementCount()]);
    }

    /**
     * Resolve each element in resValue and copy them to {@code values}. The values copied are
     * always Strings. The ideal signature for the method should be &lt;T super String&gt;, but java
     * generics don't support it.
     */
    private <T extends java.lang.CharSequence> T[] fillValues(com.android.ide.common.rendering.api.ArrayResourceValue resValue, T[] values) {
        int i = 0;
        for (java.util.Iterator<java.lang.String> iterator = resValue.iterator(); iterator.hasNext(); i++) {
            @java.lang.SuppressWarnings("unchecked")
            T s = ((T) (resolveReference(iterator.next(), resValue.isFramework())));
            values[i] = s;
        }
        return values;
    }

    @java.lang.Override
    public int[] getIntArray(int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue rv = getArrayResourceValue(id);
        if (rv == null) {
            // Error already logged by getArrayResourceValue.
            return new int[0];
        } else
            if (!(rv instanceof com.android.ide.common.rendering.api.ArrayResourceValue)) {
                // This is an older IDE that can only give us the first element of the array.
                java.lang.String firstValue = resolveReference(rv.getValue(), rv.isFramework());
                try {
                    return new int[]{ getInt(firstValue) };
                } catch (java.lang.NumberFormatException e) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, "Integer resource array contains non-integer value: " + firstValue, null);
                    return new int[1];
                }
            }

        com.android.ide.common.rendering.api.ArrayResourceValue resValue = ((com.android.ide.common.rendering.api.ArrayResourceValue) (rv));
        int[] values = new int[resValue.getElementCount()];
        int i = 0;
        for (java.util.Iterator<java.lang.String> iterator = resValue.iterator(); iterator.hasNext(); i++) {
            java.lang.String element = resolveReference(iterator.next(), resValue.isFramework());
            try {
                values[i] = getInt(element);
            } catch (java.lang.NumberFormatException e) {
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, "Integer resource array contains non-integer value: " + element, null);
            }
        }
        return values;
    }

    /**
     * Try to find the ArrayResourceValue for the given id.
     * <p/>
     * If the ResourceValue found is not of type {@link ResourceType#ARRAY}, the method logs an
     * error and return null. However, if the ResourceValue found has type {@code ResourceType.ARRAY}, but the value is not an instance of {@link ArrayResourceValue}, the
     * method returns the ResourceValue. This happens on older versions of the IDE, which did not
     * parse the array resources properly.
     * <p/>
     *
     * @throws NotFoundException
     * 		if no resource if found
     */
    @android.annotation.Nullable
    private com.android.ide.common.rendering.api.ResourceValue getArrayResourceValue(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = getResourceValue(id, mPlatformResourceFlag);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = v.getSecond();
            assert resValue != null;
            if (resValue != null) {
                final com.android.resources.ResourceType type = resValue.getResourceType();
                if (type != com.android.resources.ResourceType.ARRAY) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_RESOLVE, java.lang.String.format("Resource with id 0x%1$X is not an array resource, but %2$s", id, type == null ? "null" : type.getDisplayName()), null);
                    return null;
                }
                if (!(resValue instanceof com.android.ide.common.rendering.api.ArrayResourceValue)) {
                    com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_UNSUPPORTED, "Obtaining resource arrays via getTextArray, getStringArray or getIntArray is not fully supported in this version of the IDE.", null);
                }
                return resValue;
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @android.annotation.NonNull
    private java.lang.String resolveReference(@android.annotation.NonNull
    java.lang.String ref, boolean forceFrameworkOnly) {
        if (ref.startsWith(SdkConstants.PREFIX_RESOURCE_REF) || ref.startsWith(SdkConstants.PREFIX_THEME_REF)) {
            com.android.ide.common.rendering.api.ResourceValue rv = mContext.getRenderResources().findResValue(ref, forceFrameworkOnly);
            rv = mContext.getRenderResources().resolveResValue(rv);
            if (rv != null) {
                return rv.getValue();
            } else {
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_RESOLVE, "Unable to resolve resource " + ref, null);
            }
        }
        // Not a reference.
        return ref;
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getLayout(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = getResourceValue(id, mPlatformResourceFlag);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue value = v.getSecond();
            org.xmlpull.v1.XmlPullParser parser = null;
            try {
                // check if the current parser can provide us with a custom parser.
                if (mPlatformResourceFlag[0] == false) {
                    parser = mLayoutlibCallback.getParser(value);
                }
                // create a new one manually if needed.
                if (parser == null) {
                    java.io.File xml = new java.io.File(value.getValue());
                    if (xml.isFile()) {
                        // we need to create a pull parser around the layout XML file, and then
                        // give that to our XmlBlockParser
                        parser = com.android.layoutlib.bridge.impl.ParserFactory.create(xml);
                    }
                }
                if (parser != null) {
                    return new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, mContext, mPlatformResourceFlag[0]);
                }
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to configure parser for " + value.getValue(), e, null);
                // we'll return null below.
            } catch (java.io.FileNotFoundException e) {
                // this shouldn't happen since we check above.
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getAnimation(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = getResourceValue(id, mPlatformResourceFlag);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue value = v.getSecond();
            org.xmlpull.v1.XmlPullParser parser = null;
            try {
                java.io.File xml = new java.io.File(value.getValue());
                if (xml.isFile()) {
                    // we need to create a pull parser around the layout XML file, and then
                    // give that to our XmlBlockParser
                    parser = com.android.layoutlib.bridge.impl.ParserFactory.create(xml);
                    return new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, mContext, mPlatformResourceFlag[0]);
                }
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to configure parser for " + value.getValue(), e, null);
                // we'll return null below.
            } catch (java.io.FileNotFoundException e) {
                // this shouldn't happen since we check above.
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainAttributes(android.util.AttributeSet set, int[] attrs) {
        return mContext.obtainStyledAttributes(set, attrs);
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainTypedArray(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public float getDimension(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    if (v.equals(BridgeConstants.MATCH_PARENT) || v.equals(BridgeConstants.FILL_PARENT)) {
                        return android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                    } else
                        if (v.equals(BridgeConstants.WRAP_CONTENT)) {
                            return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                        }

                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, mTmpValue, true) && (mTmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return mTmpValue.getDimension(getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return 0;
    }

    @java.lang.Override
    public int getDimensionPixelOffset(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, mTmpValue, true) && (mTmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return android.util.TypedValue.complexToDimensionPixelOffset(mTmpValue.data, getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return 0;
    }

    @java.lang.Override
    public int getDimensionPixelSize(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, mTmpValue, true) && (mTmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return android.util.TypedValue.complexToDimensionPixelSize(mTmpValue.data, getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return 0;
    }

    @java.lang.Override
    public int getInteger(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    try {
                        return getInt(v);
                    } catch (java.lang.NumberFormatException e) {
                        // return exception below
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return 0;
    }

    @java.lang.Override
    public boolean getBoolean(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    return java.lang.Boolean.parseBoolean(v);
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return false;
    }

    @java.lang.Override
    public java.lang.String getResourceEntryName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getResourceName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getResourceTypeName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getString(int id, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        java.lang.String s = getString(id);
        if (s != null) {
            return java.lang.String.format(s, formatArgs);
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public java.lang.String getString(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if ((value != null) && (value.getSecond().getValue() != null)) {
            return getValue();
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public void getValue(int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            java.lang.String v = getValue();
            if (v != null) {
                if (/* requireUnit */
                com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, outValue, false)) {
                    return;
                }
                // else it's a string
                outValue.type = android.util.TypedValue.TYPE_STRING;
                outValue.string = v;
                return;
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
    }

    @java.lang.Override
    public void getValue(java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getXml(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            java.lang.String v = getValue();
            if (v != null) {
                // check this is a file
                java.io.File f = new java.io.File(v);
                if (f.isFile()) {
                    try {
                        org.xmlpull.v1.XmlPullParser parser = com.android.layoutlib.bridge.impl.ParserFactory.create(f);
                        return new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, mContext, mPlatformResourceFlag[0]);
                    } catch (org.xmlpull.v1.XmlPullParserException e) {
                        android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
                        newE.initCause(e);
                        throw newE;
                    } catch (java.io.FileNotFoundException e) {
                        android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
                        newE.initCause(e);
                        throw newE;
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser loadXmlResourceParser(java.lang.String file, int id, int assetCookie, java.lang.String type) throws android.content.res.Resources.NotFoundException {
        // even though we know the XML file to load directly, we still need to resolve the
        // id so that we can know if it's a platform or project resource.
        // (mPlatformResouceFlag will get the result and will be used later).
        getResourceValue(id, mPlatformResourceFlag);
        java.io.File f = new java.io.File(file);
        try {
            org.xmlpull.v1.XmlPullParser parser = com.android.layoutlib.bridge.impl.ParserFactory.create(f);
            return new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, mContext, mPlatformResourceFlag[0]);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
            newE.initCause(e);
            throw newE;
        } catch (java.io.FileNotFoundException e) {
            android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
            newE.initCause(e);
            throw newE;
        }
    }

    @java.lang.Override
    public java.io.InputStream openRawResource(int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = getResourceValue(id, mPlatformResourceFlag);
        if (value != null) {
            java.lang.String path = getValue();
            if (path != null) {
                // check this is a file
                java.io.File f = new java.io.File(path);
                if (f.isFile()) {
                    try {
                        // if it's a nine-patch return a custom input stream so that
                        // other methods (mainly bitmap factory) can detect it's a 9-patch
                        // and actually load it as a 9-patch instead of a normal bitmap
                        if (path.toLowerCase().endsWith(NinePatch.EXTENSION_9PATCH)) {
                            return new android.content.res.BridgeResources.NinePatchInputStream(f);
                        }
                        return new java.io.FileInputStream(f);
                    } catch (java.io.FileNotFoundException e) {
                        android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
                        newE.initCause(e);
                        throw newE;
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        throwException(id);
        // this is not used since the method above always throws
        return null;
    }

    @java.lang.Override
    public java.io.InputStream openRawResource(int id, android.util.TypedValue value) throws android.content.res.Resources.NotFoundException {
        getValue(id, value, true);
        java.lang.String path = toString();
        java.io.File f = new java.io.File(path);
        if (f.isFile()) {
            try {
                // if it's a nine-patch return a custom input stream so that
                // other methods (mainly bitmap factory) can detect it's a 9-patch
                // and actually load it as a 9-patch instead of a normal bitmap
                if (path.toLowerCase().endsWith(NinePatch.EXTENSION_9PATCH)) {
                    return new android.content.res.BridgeResources.NinePatchInputStream(f);
                }
                return new java.io.FileInputStream(f);
            } catch (java.io.FileNotFoundException e) {
                android.content.res.Resources.NotFoundException exception = new android.content.res.Resources.NotFoundException();
                exception.initCause(e);
                throw exception;
            }
        }
        throw new android.content.res.Resources.NotFoundException();
    }

    @java.lang.Override
    public android.content.res.AssetFileDescriptor openRawResourceFd(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Builds and throws a {@link Resources.NotFoundException} based on a resource id and a resource type.
     *
     * @param id
     * 		the id of the resource
     * @throws NotFoundException
     * 		
     */
    private void throwException(int id) throws android.content.res.Resources.NotFoundException {
        // first get the String related to this id in the framework
        com.android.util.Pair<com.android.resources.ResourceType, java.lang.String> resourceInfo = com.android.layoutlib.bridge.Bridge.resolveResourceId(id);
        // if the name is unknown in the framework, get it from the custom view loader.
        if ((resourceInfo == null) && (mLayoutlibCallback != null)) {
            resourceInfo = mLayoutlibCallback.resolveResourceId(id);
        }
        java.lang.String message;
        if (resourceInfo != null) {
            message = java.lang.String.format("Could not find %1$s resource matching value 0x%2$X (resolved name: %3$s) in current configuration.", resourceInfo.getFirst(), id, resourceInfo.getSecond());
        } else {
            message = java.lang.String.format("Could not resolve resource value: 0x%1$X.", id);
        }
        throw new android.content.res.Resources.NotFoundException(message);
    }

    private int getInt(java.lang.String v) throws java.lang.NumberFormatException {
        int radix = 10;
        if (v.startsWith("0x")) {
            v = v.substring(2);
            radix = 16;
        } else
            if (v.startsWith("0")) {
                radix = 8;
            }

        return java.lang.Integer.parseInt(v, radix);
    }
}

