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


import static value.string.toString;


@java.lang.SuppressWarnings("deprecation")
public class Resources_Delegate {
    private static java.util.WeakHashMap<android.content.res.Resources, com.android.ide.common.rendering.api.LayoutlibCallback> sLayoutlibCallbacks = new java.util.WeakHashMap<>();

    private static java.util.WeakHashMap<android.content.res.Resources, com.android.layoutlib.bridge.android.BridgeContext> sContexts = new java.util.WeakHashMap<>();

    // TODO: This cache is cleared every time a render session is disposed. Look into making this
    // more long lived.
    private static android.util.LruCache<java.lang.String, android.graphics.drawable.Drawable.ConstantState> sDrawableCache = new android.util.LruCache(50);

    public static android.content.res.Resources initSystem(@android.annotation.NonNull
    com.android.layoutlib.bridge.android.BridgeContext context, @android.annotation.NonNull
    android.content.res.AssetManager assets, @android.annotation.NonNull
    android.util.DisplayMetrics metrics, @android.annotation.NonNull
    android.content.res.Configuration config, @android.annotation.NonNull
    com.android.ide.common.rendering.api.LayoutlibCallback layoutlibCallback) {
        assert android.content.res.Resources.mSystem == null : "Resources_Delegate.initSystem called twice before disposeSystem was called";
        android.content.res.Resources resources = new android.content.res.Resources(android.content.res.Resources_Delegate.class.getClassLoader());
        resources.setImpl(new android.content.res.ResourcesImpl(assets, metrics, config, new android.view.DisplayAdjustments()));
        android.content.res.Resources_Delegate.sContexts.put(resources, java.util.Objects.requireNonNull(context));
        android.content.res.Resources_Delegate.sLayoutlibCallbacks.put(resources, java.util.Objects.requireNonNull(layoutlibCallback));
        return android.content.res.Resources.mSystem = resources;
    }

    /**
     * Returns the {@link BridgeContext} associated to the given {@link Resources}
     */
    @com.android.tools.layoutlib.annotations.VisibleForTesting
    @android.annotation.NonNull
    public static com.android.layoutlib.bridge.android.BridgeContext getContext(@android.annotation.NonNull
    android.content.res.Resources resources) {
        assert android.content.res.Resources_Delegate.sContexts.containsKey(resources) : "Resources_Delegate.getContext called before initSystem";
        return android.content.res.Resources_Delegate.sContexts.get(resources);
    }

    /**
     * Returns the {@link LayoutlibCallback} associated to the given {@link Resources}
     */
    @com.android.tools.layoutlib.annotations.VisibleForTesting
    @android.annotation.NonNull
    public static com.android.ide.common.rendering.api.LayoutlibCallback getLayoutlibCallback(@android.annotation.NonNull
    android.content.res.Resources resources) {
        assert android.content.res.Resources_Delegate.sLayoutlibCallbacks.containsKey(resources) : "Resources_Delegate.getLayoutlibCallback called before initSystem";
        return android.content.res.Resources_Delegate.sLayoutlibCallbacks.get(resources);
    }

    /**
     * Disposes the static {@link Resources#mSystem} to make sure we don't leave objects around that
     * would prevent us from unloading the library.
     */
    public static void disposeSystem() {
        android.content.res.Resources_Delegate.sDrawableCache.evictAll();
        android.content.res.Resources_Delegate.sContexts.clear();
        android.content.res.Resources_Delegate.sLayoutlibCallbacks.clear();
        android.content.res.Resources.mSystem = null;
    }

    public static android.content.res.BridgeTypedArray newTypeArray(android.content.res.Resources resources, int numEntries) {
        return new android.content.res.BridgeTypedArray(resources, android.content.res.Resources_Delegate.getContext(resources), numEntries);
    }

    private static com.android.ide.common.rendering.api.ResourceReference getResourceInfo(android.content.res.Resources resources, int id) {
        // first get the String related to this id in the framework
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = com.android.layoutlib.bridge.Bridge.resolveResourceId(id);
        assert android.content.res.Resources.mSystem != null : "Resources_Delegate.initSystem wasn't called";
        // Set the layoutlib callback and context for resources
        if ((resources != android.content.res.Resources.mSystem) && ((!android.content.res.Resources_Delegate.sContexts.containsKey(resources)) || (!android.content.res.Resources_Delegate.sLayoutlibCallbacks.containsKey(resources)))) {
            android.content.res.Resources_Delegate.sLayoutlibCallbacks.put(resources, android.content.res.Resources_Delegate.getLayoutlibCallback(android.content.res.Resources.mSystem));
            android.content.res.Resources_Delegate.sContexts.put(resources, android.content.res.Resources_Delegate.getContext(android.content.res.Resources.mSystem));
        }
        if (resourceInfo == null) {
            // Didn't find a match in the framework? Look in the project.
            resourceInfo = android.content.res.Resources_Delegate.getLayoutlibCallback(resources).resolveResourceId(id);
        }
        return resourceInfo;
    }

    private static com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> getResourceValue(android.content.res.Resources resources, int id) {
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = android.content.res.Resources_Delegate.getResourceInfo(resources, id);
        if (resourceInfo != null) {
            java.lang.String attributeName = resourceInfo.getName();
            com.android.ide.common.rendering.api.RenderResources renderResources = android.content.res.Resources_Delegate.getContext(resources).getRenderResources();
            com.android.ide.common.rendering.api.ResourceValue value = renderResources.getResolvedResource(resourceInfo);
            if (value == null) {
                // Unable to resolve the attribute, just leave the unresolved value.
                value = new com.android.ide.common.rendering.api.ResourceValueImpl(resourceInfo.getNamespace(), resourceInfo.getResourceType(), attributeName, attributeName);
            }
            return com.android.util.Pair.of(attributeName, value);
        }
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.drawable.Drawable getDrawable(android.content.res.Resources resources, int id) {
        return android.content.res.Resources_Delegate.getDrawable(resources, id, null);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.drawable.Drawable getDrawable(android.content.res.Resources resources, int id, android.content.res.Resources.Theme theme) {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            java.lang.String key = getValue();
            android.graphics.drawable.Drawable.ConstantState constantState = (key != null) ? android.content.res.Resources_Delegate.sDrawableCache.get(key) : null;
            android.graphics.drawable.Drawable drawable;
            if (constantState != null) {
                drawable = constantState.newDrawable(resources, theme);
            } else {
                drawable = com.android.layoutlib.bridge.impl.ResourceHelper.getDrawable(value.getSecond(), android.content.res.Resources_Delegate.getContext(resources), theme);
                if (key != null) {
                    android.content.res.Resources_Delegate.sDrawableCache.put(key, drawable.getConstantState());
                }
            }
            return drawable;
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getColor(android.content.res.Resources resources, int id) {
        return android.content.res.Resources_Delegate.getColor(resources, id, null);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getColor(android.content.res.Resources resources, int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resourceValue = value.getSecond();
            try {
                return com.android.layoutlib.bridge.impl.ResourceHelper.getColor(resourceValue.getValue());
            } catch (java.lang.NumberFormatException e) {
                // Check if the value passed is a file. If it is, mostly likely, user is referencing
                // a color state list from a place where they should reference only a pure color.
                com.android.ide.common.rendering.api.AssetRepository repository = android.content.res.Resources_Delegate.getAssetRepository(resources);
                java.lang.String message;
                if (repository.isFileResource(resourceValue.getValue())) {
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
        return android.content.res.Resources_Delegate.getColorStateList(resources, id, theme).getDefaultColor();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.ColorStateList getColorStateList(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        return android.content.res.Resources_Delegate.getColorStateList(resources, id, null);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.ColorStateList getColorStateList(android.content.res.Resources resources, int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> resValue = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (resValue != null) {
            android.content.res.ColorStateList stateList = com.android.layoutlib.bridge.impl.ResourceHelper.getColorStateList(resValue.getSecond(), android.content.res.Resources_Delegate.getContext(resources), theme);
            if (stateList != null) {
                return stateList;
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.CharSequence getText(android.content.res.Resources resources, int id, java.lang.CharSequence def) {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
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
        return def;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.CharSequence getText(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
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
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.CharSequence[] getTextArray(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue resValue = android.content.res.Resources_Delegate.getArrayResourceValue(resources, id);
        if (resValue == null) {
            // Error already logged by getArrayResourceValue.
            return new java.lang.CharSequence[0];
        }
        if (resValue instanceof com.android.ide.common.rendering.api.ArrayResourceValue) {
            com.android.ide.common.rendering.api.ArrayResourceValue arrayValue = ((com.android.ide.common.rendering.api.ArrayResourceValue) (resValue));
            return android.content.res.Resources_Delegate.resolveValues(resources, arrayValue);
        }
        com.android.ide.common.rendering.api.RenderResources renderResources = android.content.res.Resources_Delegate.getContext(resources).getRenderResources();
        return new java.lang.CharSequence[]{ renderResources.resolveResValue(resValue).getValue() };
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String[] getStringArray(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue resValue = android.content.res.Resources_Delegate.getArrayResourceValue(resources, id);
        if (resValue == null) {
            // Error already logged by getArrayResourceValue.
            return new java.lang.String[0];
        }
        if (resValue instanceof com.android.ide.common.rendering.api.ArrayResourceValue) {
            com.android.ide.common.rendering.api.ArrayResourceValue arv = ((com.android.ide.common.rendering.api.ArrayResourceValue) (resValue));
            return android.content.res.Resources_Delegate.resolveValues(resources, arv);
        }
        return new java.lang.String[]{ android.content.res.Resources_Delegate.resolveReference(resources, resValue) };
    }

    /**
     * Resolves each element in resValue and returns an array of resolved values. The returned array
     * may contain nulls.
     */
    @android.annotation.NonNull
    static java.lang.String[] resolveValues(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.NonNull
    com.android.ide.common.rendering.api.ArrayResourceValue resValue) {
        java.lang.String[] result = new java.lang.String[resValue.getElementCount()];
        for (int i = 0; i < resValue.getElementCount(); i++) {
            java.lang.String value = resValue.getElement(i);
            result[i] = android.content.res.Resources_Delegate.resolveReference(resources, value, resValue.getNamespace(), resValue.getNamespaceResolver());
        }
        return result;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int[] getIntArray(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue rv = android.content.res.Resources_Delegate.getArrayResourceValue(resources, id);
        if (rv == null) {
            // Error already logged by getArrayResourceValue.
            return new int[0];
        }
        if (rv instanceof com.android.ide.common.rendering.api.ArrayResourceValue) {
            com.android.ide.common.rendering.api.ArrayResourceValue resValue = ((com.android.ide.common.rendering.api.ArrayResourceValue) (rv));
            int n = resValue.getElementCount();
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                java.lang.String element = android.content.res.Resources_Delegate.resolveReference(resources, resValue.getElement(i), resValue.getNamespace(), resValue.getNamespaceResolver());
                if (element != null) {
                    try {
                        if (element.startsWith("#")) {
                            // This integer represents a color (starts with #).
                            values[i] = android.graphics.Color.parseColor(element);
                        } else {
                            values[i] = android.content.res.Resources_Delegate.getInt(element);
                        }
                    } catch (java.lang.NumberFormatException e) {
                        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, ("Integer resource array contains non-integer value: \"" + element) + "\"", null);
                    } catch (java.lang.IllegalArgumentException e) {
                        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, ("Integer resource array contains wrong color format: \"" + element) + "\"", null);
                    }
                } else {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, ("Integer resource array contains non-integer value: \"" + resValue.getElement(i)) + "\"", null);
                }
            }
            return values;
        }
        // This is an older IDE that can only give us the first element of the array.
        java.lang.String firstValue = android.content.res.Resources_Delegate.resolveReference(resources, rv);
        if (firstValue != null) {
            try {
                return new int[]{ android.content.res.Resources_Delegate.getInt(firstValue) };
            } catch (java.lang.NumberFormatException e) {
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, ("Integer resource array contains non-integer value: \"" + firstValue) + "\"", null);
                return new int[1];
            }
        } else {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_FORMAT, ("Integer resource array contains non-integer value: \"" + rv.getValue()) + "\"", null);
            return new int[1];
        }
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
    private static com.android.ide.common.rendering.api.ResourceValue getArrayResourceValue(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = android.content.res.Resources_Delegate.getResourceValue(resources, id);
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
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @android.annotation.Nullable
    private static java.lang.String resolveReference(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.Nullable
    java.lang.String value, @android.annotation.NonNull
    com.android.ide.common.rendering.api.ResourceNamespace contextNamespace, @android.annotation.NonNull
    com.android.ide.common.rendering.api.ResourceNamespace.Resolver resolver) {
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = new com.android.layoutlib.bridge.android.UnresolvedResourceValue(value, contextNamespace, resolver);
            return android.content.res.Resources_Delegate.resolveReference(resources, resValue);
        }
        return null;
    }

    @android.annotation.Nullable
    private static java.lang.String resolveReference(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.NonNull
    com.android.ide.common.rendering.api.ResourceValue value) {
        com.android.ide.common.rendering.api.RenderResources renderResources = android.content.res.Resources_Delegate.getContext(resources).getRenderResources();
        com.android.ide.common.rendering.api.ResourceValue resolvedValue = renderResources.resolveResValue(value);
        return resolvedValue == null ? null : resolvedValue.getValue();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.XmlResourceParser getLayout(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue value = v.getSecond();
            try {
                com.android.layoutlib.bridge.android.BridgeXmlBlockParser parser = com.android.layoutlib.bridge.impl.ResourceHelper.getXmlBlockParser(android.content.res.Resources_Delegate.getContext(resources), value);
                if (parser != null) {
                    return parser;
                }
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to parse " + value.getValue(), e, null);
                // we'll return null below.
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id, "layout");
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.XmlResourceParser getAnimation(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue value = v.getSecond();
            try {
                return com.android.layoutlib.bridge.impl.ResourceHelper.getXmlBlockParser(android.content.res.Resources_Delegate.getContext(resources), value);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to parse " + value.getValue(), e, null);
                // we'll return null below.
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainAttributes(android.content.res.Resources resources, android.util.AttributeSet set, int[] attrs) {
        return android.content.res.Resources_Delegate.getContext(resources).obtainStyledAttributes(set, attrs);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainAttributes(android.content.res.Resources resources, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        return android.content.res.Resources.obtainAttributes_Original(resources, theme, set, attrs);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.TypedArray obtainTypedArray(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.layoutlib.bridge.android.BridgeContext context = android.content.res.Resources_Delegate.getContext(resources);
        com.android.ide.common.rendering.api.ResourceReference reference = context.resolveId(id);
        com.android.ide.common.rendering.api.RenderResources renderResources = context.getRenderResources();
        com.android.ide.common.rendering.api.ResourceValue value = renderResources.getResolvedResource(reference);
        if (!(value instanceof com.android.ide.common.rendering.api.ArrayResourceValue)) {
            throw new android.content.res.Resources.NotFoundException("Array resource ID #0x" + java.lang.Integer.toHexString(id));
        }
        com.android.ide.common.rendering.api.ArrayResourceValue arrayValue = ((com.android.ide.common.rendering.api.ArrayResourceValue) (value));
        int length = arrayValue.getElementCount();
        com.android.ide.common.rendering.api.ResourceNamespace namespace = arrayValue.getNamespace();
        android.content.res.BridgeTypedArray typedArray = android.content.res.Resources_Delegate.newTypeArray(resources, length);
        for (int i = 0; i < length; i++) {
            com.android.ide.common.rendering.api.ResourceValue elementValue;
            com.android.resources.ResourceUrl resourceUrl = com.android.resources.ResourceUrl.parse(arrayValue.getElement(i));
            if (resourceUrl != null) {
                com.android.ide.common.rendering.api.ResourceReference elementRef = resourceUrl.resolve(namespace, arrayValue.getNamespaceResolver());
                elementValue = renderResources.getResolvedResource(elementRef);
            } else {
                elementValue = new com.android.ide.common.rendering.api.ResourceValueImpl(namespace, com.android.resources.ResourceType.STRING, "element" + i, arrayValue.getElement(i));
            }
            typedArray.bridgeSetValue(i, elementValue.getName(), namespace, i, elementValue);
        }
        typedArray.sealArray();
        return typedArray;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float getDimension(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
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

                    android.util.TypedValue tmpValue = new android.util.TypedValue();
                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, tmpValue, true) && (tmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return tmpValue.getDimension(resources.getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return 0;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getDimensionPixelOffset(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    android.util.TypedValue tmpValue = new android.util.TypedValue();
                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, tmpValue, true) && (tmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return android.util.TypedValue.complexToDimensionPixelOffset(tmpValue.data, resources.getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return 0;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getDimensionPixelSize(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    android.util.TypedValue tmpValue = new android.util.TypedValue();
                    if (/* requireUnit */
                    com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, tmpValue, true) && (tmpValue.type == android.util.TypedValue.TYPE_DIMENSION)) {
                        return android.util.TypedValue.complexToDimensionPixelSize(tmpValue.data, resources.getDisplayMetrics());
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return 0;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getInteger(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            assert resValue != null;
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    try {
                        return android.content.res.Resources_Delegate.getInt(v);
                    } catch (java.lang.NumberFormatException e) {
                        // return exception below
                    }
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return 0;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float getFloat(android.content.res.Resources resources, int id) {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    try {
                        return java.lang.Float.parseFloat(v);
                    } catch (java.lang.NumberFormatException ignore) {
                    }
                }
            }
        }
        return 0;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean getBoolean(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resValue = value.getSecond();
            if (resValue != null) {
                java.lang.String v = resValue.getValue();
                if (v != null) {
                    return java.lang.Boolean.parseBoolean(v);
                }
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return false;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getResourceEntryName(android.content.res.Resources resources, int resid) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = android.content.res.Resources_Delegate.getResourceInfo(resources, resid);
        if (resourceInfo != null) {
            return resourceInfo.getName();
        }
        android.content.res.Resources_Delegate.throwException(resid, null);
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getResourceName(android.content.res.Resources resources, int resid) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = android.content.res.Resources_Delegate.getResourceInfo(resources, resid);
        if (resourceInfo != null) {
            java.lang.String packageName = android.content.res.Resources_Delegate.getPackageName(resourceInfo, resources);
            return (((packageName + ':') + resourceInfo.getResourceType().getName()) + '/') + resourceInfo.getName();
        }
        android.content.res.Resources_Delegate.throwException(resid, null);
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getResourcePackageName(android.content.res.Resources resources, int resid) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = android.content.res.Resources_Delegate.getResourceInfo(resources, resid);
        if (resourceInfo != null) {
            return android.content.res.Resources_Delegate.getPackageName(resourceInfo, resources);
        }
        android.content.res.Resources_Delegate.throwException(resid, null);
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getResourceTypeName(android.content.res.Resources resources, int resid) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceReference resourceInfo = android.content.res.Resources_Delegate.getResourceInfo(resources, resid);
        if (resourceInfo != null) {
            return resourceInfo.getResourceType().getName();
        }
        android.content.res.Resources_Delegate.throwException(resid, null);
        return null;
    }

    private static java.lang.String getPackageName(com.android.ide.common.rendering.api.ResourceReference resourceInfo, android.content.res.Resources resources) {
        java.lang.String packageName = getPackageName();
        if (packageName == null) {
            packageName = android.content.res.Resources_Delegate.getContext(resources).getPackageName();
            if (packageName == null) {
                packageName = com.android.SdkConstants.APP_PREFIX;
            }
        }
        return packageName;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getString(android.content.res.Resources resources, int id, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        java.lang.String s = android.content.res.Resources_Delegate.getString(resources, id);
        if (s != null) {
            return java.lang.String.format(s, formatArgs);
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getString(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if ((value != null) && (value.getSecond().getValue() != null)) {
            return getValue();
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getQuantityString(android.content.res.Resources resources, int id, int quantity) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            if (value.getSecond() instanceof com.android.ide.common.rendering.api.PluralsResourceValue) {
                com.android.ide.common.rendering.api.PluralsResourceValue pluralsResourceValue = ((com.android.ide.common.rendering.api.PluralsResourceValue) (value.getSecond()));
                android.icu.text.PluralRules pluralRules = android.icu.text.PluralRules.forLocale(resources.getConfiguration().getLocales().get(0));
                java.lang.String strValue = pluralsResourceValue.getValue(pluralRules.select(quantity));
                if (strValue == null) {
                    strValue = pluralsResourceValue.getValue(PluralRules.KEYWORD_OTHER);
                }
                return strValue;
            } else {
                return getValue();
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.String getQuantityString(android.content.res.Resources resources, int id, int quantity, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        java.lang.String raw = android.content.res.Resources_Delegate.getQuantityString(resources, id, quantity);
        return java.lang.String.format(resources.getConfiguration().getLocales().get(0), raw, formatArgs);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.lang.CharSequence getQuantityText(android.content.res.Resources resources, int id, int quantity) throws android.content.res.Resources.NotFoundException {
        return android.content.res.Resources_Delegate.getQuantityString(resources, id, quantity);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Typeface getFont(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            return com.android.layoutlib.bridge.impl.ResourceHelper.getFont(value.getSecond(), android.content.res.Resources_Delegate.getContext(resources), null);
        }
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Typeface getFont(android.content.res.Resources resources, android.util.TypedValue outValue, int id) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.ResourceValue resVal = android.content.res.Resources_Delegate.getResourceValue(resources, id, outValue);
        if (resVal != null) {
            return com.android.layoutlib.bridge.impl.ResourceHelper.getFont(resVal, android.content.res.Resources_Delegate.getContext(resources), null);
        }
        android.content.res.Resources_Delegate.throwException(resources, id);
        return null;// This is not used since the method above always throws.

    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void getValue(android.content.res.Resources resources, int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        android.content.res.Resources_Delegate.getResourceValue(resources, id, outValue);
    }

    private static com.android.ide.common.rendering.api.ResourceValue getResourceValue(android.content.res.Resources resources, int id, android.util.TypedValue outValue) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            com.android.ide.common.rendering.api.ResourceValue resVal = value.getSecond();
            java.lang.String v = (resVal != null) ? resVal.getValue() : null;
            if (v != null) {
                if (/* requireUnit */
                com.android.layoutlib.bridge.impl.ResourceHelper.parseFloatAttribute(value.getFirst(), v, outValue, false)) {
                    return resVal;
                }
                if (resVal instanceof com.android.ide.common.rendering.api.DensityBasedResourceValue) {
                    outValue.density = ((com.android.ide.common.rendering.api.DensityBasedResourceValue) (resVal)).getResourceDensity().getDpiValue();
                }
                // else it's a string
                outValue.type = android.util.TypedValue.TYPE_STRING;
                outValue.string = v;
                return resVal;
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        return null;// This is not used since the method above always throws.

    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void getValue(android.content.res.Resources resources, java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void getValueForDensity(android.content.res.Resources resources, int id, int density, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        android.content.res.Resources_Delegate.getValue(resources, id, outValue, resolveRefs);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getAttributeSetSourceResId(@android.annotation.Nullable
    android.util.AttributeSet set) {
        // Not supported in layoutlib
        return android.content.res.Resources.ID_NULL;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.XmlResourceParser getXml(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> v = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (v != null) {
            com.android.ide.common.rendering.api.ResourceValue value = v.getSecond();
            try {
                return com.android.layoutlib.bridge.impl.ResourceHelper.getXmlBlockParser(android.content.res.Resources_Delegate.getContext(resources), value);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Failed to parse " + value.getValue(), e, null);
                // we'll return null below.
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.XmlResourceParser loadXmlResourceParser(android.content.res.Resources resources, int id, java.lang.String type) throws android.content.res.Resources.NotFoundException {
        return resources.loadXmlResourceParser_Original(id, type);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.XmlResourceParser loadXmlResourceParser(android.content.res.Resources resources, java.lang.String file, int id, int assetCookie, java.lang.String type) throws android.content.res.Resources.NotFoundException {
        // even though we know the XML file to load directly, we still need to resolve the
        // id so that we can know if it's a platform or project resource.
        // (mPlatformResouceFlag will get the result and will be used later).
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> result = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        com.android.ide.common.rendering.api.ResourceNamespace layoutNamespace;
        if ((result != null) && (result.getSecond() != null)) {
            layoutNamespace = result.getSecond().getNamespace();
        } else {
            // We need to pick something, even though the resource system never heard about a layout
            // with this numeric id.
            layoutNamespace = com.android.ide.common.rendering.api.ResourceNamespace.RES_AUTO;
        }
        try {
            org.xmlpull.v1.XmlPullParser parser = com.android.layoutlib.bridge.impl.ParserFactory.create(file);
            return new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, android.content.res.Resources_Delegate.getContext(resources), layoutNamespace);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.content.res.Resources.NotFoundException newE = new android.content.res.Resources.NotFoundException();
            newE.initCause(e);
            throw newE;
        }
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.io.InputStream openRawResource(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        com.android.util.Pair<java.lang.String, com.android.ide.common.rendering.api.ResourceValue> value = android.content.res.Resources_Delegate.getResourceValue(resources, id);
        if (value != null) {
            java.lang.String path = getValue();
            if (path != null) {
                return android.content.res.Resources_Delegate.openRawResource(resources, path);
            }
        }
        // id was not found or not resolved. Throw a NotFoundException.
        android.content.res.Resources_Delegate.throwException(resources, id);
        // this is not used since the method above always throws
        return null;
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static java.io.InputStream openRawResource(android.content.res.Resources resources, int id, android.util.TypedValue value) throws android.content.res.Resources.NotFoundException {
        android.content.res.Resources_Delegate.getValue(resources, id, value, true);
        java.lang.String path = toString();
        return android.content.res.Resources_Delegate.openRawResource(resources, path);
    }

    private static java.io.InputStream openRawResource(android.content.res.Resources resources, java.lang.String path) throws android.content.res.Resources.NotFoundException {
        com.android.ide.common.rendering.api.AssetRepository repository = android.content.res.Resources_Delegate.getAssetRepository(resources);
        try {
            java.io.InputStream stream = repository.openNonAsset(0, path, android.content.res.AssetManager.ACCESS_STREAMING);
            if (stream == null) {
                throw new android.content.res.Resources.NotFoundException(path);
            }
            // If it's a nine-patch return a custom input stream so that
            // other methods (mainly bitmap factory) can detect it's a 9-patch
            // and actually load it as a 9-patch instead of a normal bitmap.
            if (path.toLowerCase().endsWith(NinePatch.EXTENSION_9PATCH)) {
                return new com.android.layoutlib.bridge.util.NinePatchInputStream(stream);
            }
            return stream;
        } catch (java.io.IOException e) {
            android.content.res.Resources.NotFoundException exception = new android.content.res.Resources.NotFoundException();
            exception.initCause(e);
            throw exception;
        }
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.content.res.AssetFileDescriptor openRawResourceFd(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @com.android.tools.layoutlib.annotations.VisibleForTesting
    @android.annotation.Nullable
    static com.android.resources.ResourceUrl resourceUrlFromName(@android.annotation.NonNull
    java.lang.String name, @android.annotation.Nullable
    java.lang.String defType, @android.annotation.Nullable
    java.lang.String defPackage) {
        int colonIdx = name.indexOf(':');
        int slashIdx = name.indexOf('/');
        if ((colonIdx != (-1)) && (slashIdx != (-1))) {
            // Easy case
            return com.android.resources.ResourceUrl.parse(com.android.SdkConstants.PREFIX_RESOURCE_REF + name);
        }
        if ((colonIdx == (-1)) && (slashIdx == (-1))) {
            if (defType == null) {
                throw new java.lang.IllegalArgumentException("name does not define a type an no defType was" + " passed");
            }
            // It does not define package or type
            return com.android.resources.ResourceUrl.parse((((com.android.SdkConstants.PREFIX_RESOURCE_REF + (defPackage != null ? defPackage + ":" : "")) + defType) + "/") + name);
        }
        if (colonIdx != (-1)) {
            if (defType == null) {
                throw new java.lang.IllegalArgumentException("name does not define a type an no defType was" + " passed");
            }
            // We have package but no type
            java.lang.String pkg = name.substring(0, colonIdx);
            com.android.resources.ResourceType type = com.android.resources.ResourceType.getEnum(defType);
            return type != null ? com.android.resources.ResourceUrl.create(pkg, type, name.substring(colonIdx + 1)) : null;
        }
        com.android.resources.ResourceType type = com.android.resources.ResourceType.getEnum(name.substring(0, slashIdx));
        if (type == null) {
            return null;
        }
        // We have type but no package
        return com.android.resources.ResourceUrl.create(defPackage, type, name.substring(slashIdx + 1));
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int getIdentifier(android.content.res.Resources resources, java.lang.String name, java.lang.String defType, java.lang.String defPackage) {
        if (name == null) {
            return 0;
        }
        com.android.resources.ResourceUrl url = android.content.res.Resources_Delegate.resourceUrlFromName(name, defType, defPackage);
        if (url != null) {
            if (com.android.SdkConstants.ANDROID_PKG.equals(url.namespace)) {
                return com.android.layoutlib.bridge.Bridge.getResourceId(url.type, url.name);
            }
            if (android.content.res.Resources_Delegate.getContext(resources).getPackageName().equals(url.namespace)) {
                return android.content.res.Resources_Delegate.getLayoutlibCallback(resources).getOrGenerateResourceId(new com.android.ide.common.rendering.api.ResourceReference(com.android.ide.common.rendering.api.ResourceNamespace.RES_AUTO, url.type, url.name));
            }
        }
        return 0;
    }

    /**
     * Builds and throws a {@link Resources.NotFoundException} based on a resource id and a resource
     * type.
     *
     * @param id
     * 		the id of the resource
     * @param expectedType
     * 		the type of resource that was expected
     * @throws NotFoundException
     * 		
     */
    private static void throwException(android.content.res.Resources resources, int id, @android.annotation.Nullable
    java.lang.String expectedType) throws android.content.res.Resources.NotFoundException {
        android.content.res.Resources_Delegate.throwException(id, android.content.res.Resources_Delegate.getResourceInfo(resources, id), expectedType);
    }

    private static void throwException(android.content.res.Resources resources, int id) throws android.content.res.Resources.NotFoundException {
        android.content.res.Resources_Delegate.throwException(resources, id, null);
    }

    private static void throwException(int id, @android.annotation.Nullable
    com.android.ide.common.rendering.api.ResourceReference resourceInfo) {
        android.content.res.Resources_Delegate.throwException(id, resourceInfo, null);
    }

    private static void throwException(int id, @android.annotation.Nullable
    com.android.ide.common.rendering.api.ResourceReference resourceInfo, @android.annotation.Nullable
    java.lang.String expectedType) {
        java.lang.String message;
        if (resourceInfo != null) {
            message = java.lang.String.format("Could not find %1$s resource matching value 0x%2$X (resolved name: %3$s) in current configuration.", resourceInfo.getResourceType(), id, resourceInfo.getName());
        } else {
            message = java.lang.String.format("Could not resolve resource value: 0x%1$X.", id);
        }
        if (expectedType != null) {
            message += (" Or the resolved value was not of type " + expectedType) + " as expected.";
        }
        throw new android.content.res.Resources.NotFoundException(message);
    }

    private static int getInt(java.lang.String v) throws java.lang.NumberFormatException {
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

    private static com.android.ide.common.rendering.api.AssetRepository getAssetRepository(android.content.res.Resources resources) {
        com.android.layoutlib.bridge.android.BridgeContext context = android.content.res.Resources_Delegate.getContext(resources);
        android.content.res.BridgeAssetManager assetManager = context.getAssets();
        return assetManager.getAssetRepository();
    }
}

