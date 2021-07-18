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
package android.view;


/**
 * Custom implementation of {@link LayoutInflater} to handle custom views.
 */
public final class BridgeInflater extends android.view.LayoutInflater {
    private static final java.lang.String INFLATER_CLASS_ATTR_NAME = "viewInflaterClass";

    private static final com.android.ide.common.rendering.api.ResourceReference RES_AUTO_INFLATER_CLASS_ATTR = com.android.ide.common.rendering.api.ResourceReference.attr(ResourceNamespace.RES_AUTO, android.view.BridgeInflater.INFLATER_CLASS_ATTR_NAME);

    private static final com.android.ide.common.rendering.api.ResourceReference LEGACY_APPCOMPAT_INFLATER_CLASS_ATTR = com.android.ide.common.rendering.api.ResourceReference.attr(ResourceNamespace.APPCOMPAT_LEGACY, android.view.BridgeInflater.INFLATER_CLASS_ATTR_NAME);

    private static final com.android.ide.common.rendering.api.ResourceReference ANDROIDX_APPCOMPAT_INFLATER_CLASS_ATTR = com.android.ide.common.rendering.api.ResourceReference.attr(ResourceNamespace.APPCOMPAT, android.view.BridgeInflater.INFLATER_CLASS_ATTR_NAME);

    private static final java.lang.String LEGACY_DEFAULT_APPCOMPAT_INFLATER_NAME = "android.support.v7.app.AppCompatViewInflater";

    private static final java.lang.String ANDROIDX_DEFAULT_APPCOMPAT_INFLATER_NAME = "androidx.appcompat.app.AppCompatViewInflater";

    private final com.android.ide.common.rendering.api.LayoutlibCallback mLayoutlibCallback;

    private boolean mIsInMerge = false;

    private com.android.ide.common.rendering.api.ResourceReference mResourceReference;

    private java.util.Map<android.view.View, java.lang.String> mOpenDrawerLayouts;

    // Keep in sync with the same value in LayoutInflater.
    private static final int[] ATTRS_THEME = new int[]{ com.android.internal.R.attr.theme };

    /**
     * List of class prefixes which are tried first by default.
     * <p/>
     * This should match the list in com.android.internal.policy.impl.PhoneLayoutInflater.
     */
    private static final java.lang.String[] sClassPrefixList = new java.lang.String[]{ "android.widget.", "android.webkit.", "android.app." };

    private java.util.function.BiFunction<java.lang.String, android.util.AttributeSet, android.view.View> mCustomInflater;

    public static java.lang.String[] getClassPrefixList() {
        return android.view.BridgeInflater.sClassPrefixList;
    }

    private BridgeInflater(android.view.LayoutInflater original, android.content.Context newContext) {
        super(original, newContext);
        newContext = getBaseContext(newContext);
        mLayoutlibCallback = (newContext instanceof com.android.layoutlib.bridge.android.BridgeContext) ? ((com.android.layoutlib.bridge.android.BridgeContext) (newContext)).getLayoutlibCallback() : null;
    }

    /**
     * Instantiate a new BridgeInflater with an {@link LayoutlibCallback} object.
     *
     * @param context
     * 		The Android application context.
     * @param layoutlibCallback
     * 		the {@link LayoutlibCallback} object.
     */
    public BridgeInflater(com.android.layoutlib.bridge.android.BridgeContext context, com.android.ide.common.rendering.api.LayoutlibCallback layoutlibCallback) {
        super(context);
        mLayoutlibCallback = layoutlibCallback;
        mConstructorArgs[0] = context;
    }

    @java.lang.Override
    public android.view.View onCreateView(java.lang.String name, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        android.view.View view = createViewFromCustomInflater(name, attrs);
        if (view == null) {
            try {
                // First try to find a class using the default Android prefixes
                for (java.lang.String prefix : android.view.BridgeInflater.sClassPrefixList) {
                    try {
                        view = createView(name, prefix, attrs);
                        if (view != null) {
                            break;
                        }
                    } catch (java.lang.ClassNotFoundException e) {
                        // Ignore. We'll try again using the base class below.
                    }
                }
                // Next try using the parent loader. This will most likely only work for
                // fully-qualified class names.
                try {
                    if (view == null) {
                        view = super.onCreateView(name, attrs);
                    }
                } catch (java.lang.ClassNotFoundException e) {
                    // Ignore. We'll try again using the custom view loader below.
                }
                // Finally try again using the custom view loader
                if (view == null) {
                    view = loadCustomView(name, attrs);
                }
            } catch (android.view.InflateException e) {
                // Don't catch the InflateException below as that results in hiding the real cause.
                throw e;
            } catch (java.lang.Exception e) {
                // Wrap the real exception in a ClassNotFoundException, so that the calling method
                // can deal with it.
                throw new java.lang.ClassNotFoundException("onCreateView", e);
            }
        }
        setupViewInContext(view, attrs);
        return view;
    }

    /**
     * Finds the createView method in the given customInflaterClass. Since createView is
     * currently package protected, it will show in the declared class so we iterate up the
     * hierarchy and return the first instance we find.
     * The returned method will be accessible.
     */
    @com.android.tools.layoutlib.annotations.NotNull
    private static java.lang.reflect.Method getCreateViewMethod(java.lang.Class<?> customInflaterClass) throws java.lang.NoSuchMethodException {
        java.lang.Class<?> current = customInflaterClass;
        do {
            try {
                java.lang.reflect.Method method = current.getDeclaredMethod("createView", android.view.View.class, java.lang.String.class, android.content.Context.class, android.util.AttributeSet.class, boolean.class, boolean.class, boolean.class, boolean.class);
                method.setAccessible(true);
                return method;
            } catch (java.lang.NoSuchMethodException ignore) {
            }
            current = current.getSuperclass();
        } while ((current != null) && (current != java.lang.Object.class) );
        throw new java.lang.NoSuchMethodException();
    }

    /**
     * Finds the custom inflater class. If it's defined in the theme, we'll use that one (if the
     * class does not exist, null is returned).
     * If {@code viewInflaterClass} is not defined in the theme, we'll try to instantiate
     * {@code android.support.v7.app.AppCompatViewInflater}
     */
    @com.android.tools.layoutlib.annotations.Nullable
    private static java.lang.Class<?> findCustomInflater(@com.android.tools.layoutlib.annotations.NotNull
    com.android.layoutlib.bridge.android.BridgeContext bc, @com.android.tools.layoutlib.annotations.NotNull
    com.android.ide.common.rendering.api.LayoutlibCallback layoutlibCallback) {
        com.android.ide.common.rendering.api.ResourceReference attrRef;
        if (layoutlibCallback.isResourceNamespacingRequired()) {
            if (layoutlibCallback.hasLegacyAppCompat()) {
                attrRef = android.view.BridgeInflater.LEGACY_APPCOMPAT_INFLATER_CLASS_ATTR;
            } else
                if (layoutlibCallback.hasAndroidXAppCompat()) {
                    attrRef = android.view.BridgeInflater.ANDROIDX_APPCOMPAT_INFLATER_CLASS_ATTR;
                } else {
                    return null;
                }

        } else {
            attrRef = android.view.BridgeInflater.RES_AUTO_INFLATER_CLASS_ATTR;
        }
        com.android.ide.common.rendering.api.ResourceValue value = bc.getRenderResources().findItemInTheme(attrRef);
        java.lang.String inflaterName = (value != null) ? value.getValue() : null;
        if (inflaterName != null) {
            try {
                return layoutlibCallback.findClass(inflaterName);
            } catch (java.lang.ClassNotFoundException ignore) {
            }
            // viewInflaterClass was defined but we couldn't find the class.
        } else
            if (bc.isAppCompatTheme()) {
                // Older versions of AppCompat do not define the viewInflaterClass so try to get it
                // manually.
                try {
                    if (layoutlibCallback.hasLegacyAppCompat()) {
                        return layoutlibCallback.findClass(android.view.BridgeInflater.LEGACY_DEFAULT_APPCOMPAT_INFLATER_NAME);
                    } else
                        if (layoutlibCallback.hasAndroidXAppCompat()) {
                            return layoutlibCallback.findClass(android.view.BridgeInflater.ANDROIDX_DEFAULT_APPCOMPAT_INFLATER_NAME);
                        }

                } catch (java.lang.ClassNotFoundException ignore) {
                }
            }

        return null;
    }

    /**
     * Checks if there is a custom inflater and, when present, tries to instantiate the view
     * using it.
     */
    @com.android.tools.layoutlib.annotations.Nullable
    private android.view.View createViewFromCustomInflater(@com.android.tools.layoutlib.annotations.NotNull
    java.lang.String name, @com.android.tools.layoutlib.annotations.NotNull
    android.util.AttributeSet attrs) {
        if (mCustomInflater == null) {
            android.content.Context context = getContext();
            context = getBaseContext(context);
            if (context instanceof com.android.layoutlib.bridge.android.BridgeContext) {
                com.android.layoutlib.bridge.android.BridgeContext bc = ((com.android.layoutlib.bridge.android.BridgeContext) (context));
                java.lang.Class<?> inflaterClass = android.view.BridgeInflater.findCustomInflater(bc, mLayoutlibCallback);
                if (inflaterClass != null) {
                    try {
                        java.lang.reflect.Constructor<?> constructor = inflaterClass.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        java.lang.Object inflater = constructor.newInstance();
                        java.lang.reflect.Method method = android.view.BridgeInflater.getCreateViewMethod(inflaterClass);
                        android.content.Context finalContext = context;
                        mCustomInflater = ( viewName, attributeSet) -> {
                            try {
                                return /* readAndroidTheme */
                                // No need after L
                                /* readAppTheme */
                                /* wrapContext */
                                ((android.view.View) (method.invoke(inflater, null, viewName, finalContext, attributeSet, false, false, true, true)));
                            } catch (java.lang.IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                                assert false : "Call to createView failed";
                            }
                            return null;
                        };
                    } catch (java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException | java.lang.NoSuchMethodException | java.lang.InstantiationException ignore) {
                    }
                }
            }
            if (mCustomInflater == null) {
                // There is no custom inflater. We'll create a nop custom inflater to avoid the
                // penalty of trying to instantiate again
                mCustomInflater = ( s, attributeSet) -> null;
            }
        }
        return mCustomInflater.apply(name, attrs);
    }

    @java.lang.Override
    public android.view.View createViewFromTag(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs, boolean ignoreThemeAttr) {
        android.view.View view = null;
        if (name.equals("view")) {
            // This is usually done by the superclass but this allows us catching the error and
            // reporting something useful.
            name = attrs.getAttributeValue(null, "class");
            if (name == null) {
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Unable to inflate view tag without " + "class attribute", null);
                // We weren't able to resolve the view so we just pass a mock View to be able to
                // continue rendering.
                view = new com.android.layoutlib.bridge.MockView(context, attrs);
                ((com.android.layoutlib.bridge.MockView) (view)).setText("view");
            }
        }
        try {
            if (view == null) {
                view = super.createViewFromTag(parent, name, context, attrs, ignoreThemeAttr);
            }
        } catch (android.view.InflateException e) {
            // Creation of ContextThemeWrapper code is same as in the super method.
            // Apply a theme wrapper, if allowed and one is specified.
            if (!ignoreThemeAttr) {
                final android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, android.view.BridgeInflater.ATTRS_THEME);
                final int themeResId = ta.getResourceId(0, 0);
                if (themeResId != 0) {
                    context = new android.view.ContextThemeWrapper(context, themeResId);
                }
                ta.recycle();
            }
            if (!(e.getCause() instanceof java.lang.ClassNotFoundException)) {
                // There is some unknown inflation exception in inflating a View that was found.
                view = new com.android.layoutlib.bridge.MockView(context, attrs);
                ((com.android.layoutlib.bridge.MockView) (view)).setText(name);
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, e.getMessage(), e, null);
            } else {
                final java.lang.Object lastContext = mConstructorArgs[0];
                mConstructorArgs[0] = context;
                // try to load the class from using the custom view loader
                try {
                    view = loadCustomView(name, attrs);
                } catch (java.lang.Exception e2) {
                    // Wrap the real exception in an InflateException so that the calling
                    // method can deal with it.
                    android.view.InflateException exception = new android.view.InflateException();
                    if (!e2.getClass().equals(java.lang.ClassNotFoundException.class)) {
                        exception.initCause(e2);
                    } else {
                        exception.initCause(e);
                    }
                    throw exception;
                } finally {
                    mConstructorArgs[0] = lastContext;
                }
            }
        }
        setupViewInContext(view, attrs);
        return view;
    }

    @java.lang.Override
    public android.view.View inflate(int resource, android.view.ViewGroup root) {
        android.content.Context context = getContext();
        context = getBaseContext(context);
        if (context instanceof com.android.layoutlib.bridge.android.BridgeContext) {
            com.android.layoutlib.bridge.android.BridgeContext bridgeContext = ((com.android.layoutlib.bridge.android.BridgeContext) (context));
            com.android.ide.common.rendering.api.ResourceValue value = null;
            com.android.ide.common.rendering.api.ResourceReference layoutInfo = com.android.layoutlib.bridge.Bridge.resolveResourceId(resource);
            if (layoutInfo == null) {
                layoutInfo = mLayoutlibCallback.resolveResourceId(resource);
            }
            if (layoutInfo != null) {
                value = bridgeContext.getRenderResources().getResolvedResource(layoutInfo);
            }
            if (value != null) {
                java.lang.String path = value.getValue();
                try {
                    org.xmlpull.v1.XmlPullParser parser = com.android.layoutlib.bridge.impl.ParserFactory.create(path, true);
                    if (parser == null) {
                        return null;
                    }
                    com.android.layoutlib.bridge.android.BridgeXmlBlockParser bridgeParser = new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, bridgeContext, value.getNamespace());
                    return inflate(bridgeParser, root);
                } catch (java.lang.Exception e) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_RESOURCES_READ, "Failed to parse file " + path, e, null);
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Instantiates the given view name and returns the instance. If the view doesn't exist, a
     * MockView or null might be returned.
     *
     * @param name
     * 		the custom view name
     * @param attrs
     * 		the {@link AttributeSet} to be passed to the view constructor
     * @param silent
     * 		if true, errors while loading the view won't be reported and, if the view
     * 		doesn't exist, null will be returned.
     */
    private android.view.View loadCustomView(java.lang.String name, android.util.AttributeSet attrs, boolean silent) throws java.lang.Exception {
        if (mLayoutlibCallback != null) {
            // first get the classname in case it's not the node name
            if (name.equals("view")) {
                name = attrs.getAttributeValue(null, "class");
                if (name == null) {
                    return null;
                }
            }
            mConstructorArgs[1] = attrs;
            java.lang.Object customView = (silent) ? mLayoutlibCallback.loadClass(name, android.view.LayoutInflater.mConstructorSignature, mConstructorArgs) : mLayoutlibCallback.loadView(name, android.view.LayoutInflater.mConstructorSignature, mConstructorArgs);
            if (customView instanceof android.view.View) {
                return ((android.view.View) (customView));
            }
        }
        return null;
    }

    private android.view.View loadCustomView(java.lang.String name, android.util.AttributeSet attrs) throws java.lang.Exception {
        return loadCustomView(name, attrs, false);
    }

    private void setupViewInContext(android.view.View view, android.util.AttributeSet attrs) {
        android.content.Context context = getContext();
        context = getBaseContext(context);
        if (!(context instanceof com.android.layoutlib.bridge.android.BridgeContext)) {
            return;
        }
        com.android.layoutlib.bridge.android.BridgeContext bc = ((com.android.layoutlib.bridge.android.BridgeContext) (context));
        // get the view key
        java.lang.Object viewKey = android.view.BridgeInflater.getViewKeyFromParser(attrs, bc, mResourceReference, mIsInMerge);
        if (viewKey != null) {
            bc.addViewKey(view, viewKey);
        }
        java.lang.String scrollPosX = attrs.getAttributeValue(BridgeConstants.NS_RESOURCES, "scrollX");
        if ((scrollPosX != null) && scrollPosX.endsWith("px")) {
            int value = java.lang.Integer.parseInt(scrollPosX.substring(0, scrollPosX.length() - 2));
            bc.setScrollXPos(view, value);
        }
        java.lang.String scrollPosY = attrs.getAttributeValue(BridgeConstants.NS_RESOURCES, "scrollY");
        if ((scrollPosY != null) && scrollPosY.endsWith("px")) {
            int value = java.lang.Integer.parseInt(scrollPosY.substring(0, scrollPosY.length() - 2));
            bc.setScrollYPos(view, value);
        }
        if (com.android.layoutlib.bridge.util.ReflectionUtils.isInstanceOf(view, RecyclerViewUtil.CN_RECYCLER_VIEW)) {
            int resourceId = 0;
            int attrItemCountValue = attrs.getAttributeIntValue(BridgeConstants.NS_TOOLS_URI, BridgeConstants.ATTR_ITEM_COUNT, -1);
            if (attrs instanceof android.util.ResolvingAttributeSet) {
                com.android.ide.common.rendering.api.ResourceValue attrListItemValue = ((android.util.ResolvingAttributeSet) (attrs)).getResolvedAttributeValue(BridgeConstants.NS_TOOLS_URI, BridgeConstants.ATTR_LIST_ITEM);
                if (attrListItemValue != null) {
                    resourceId = bc.getResourceId(attrListItemValue.asReference(), 0);
                }
            }
            com.android.layoutlib.bridge.android.support.RecyclerViewUtil.setAdapter(view, bc, mLayoutlibCallback, resourceId, attrItemCountValue);
        } else
            if (com.android.layoutlib.bridge.util.ReflectionUtils.isInstanceOf(view, DrawerLayoutUtil.CN_DRAWER_LAYOUT)) {
                java.lang.String attrVal = attrs.getAttributeValue(BridgeConstants.NS_TOOLS_URI, BridgeConstants.ATTR_OPEN_DRAWER);
                if (attrVal != null) {
                    getDrawerLayoutMap().put(view, attrVal);
                }
            } else
                if (view instanceof android.widget.NumberPicker) {
                    android.widget.NumberPicker numberPicker = ((android.widget.NumberPicker) (view));
                    java.lang.String minValue = attrs.getAttributeValue(BridgeConstants.NS_TOOLS_URI, "minValue");
                    if (minValue != null) {
                        numberPicker.setMinValue(java.lang.Integer.parseInt(minValue));
                    }
                    java.lang.String maxValue = attrs.getAttributeValue(BridgeConstants.NS_TOOLS_URI, "maxValue");
                    if (maxValue != null) {
                        numberPicker.setMaxValue(java.lang.Integer.parseInt(maxValue));
                    }
                } else
                    if (view instanceof android.widget.ImageView) {
                        android.widget.ImageView img = ((android.widget.ImageView) (view));
                        android.graphics.drawable.Drawable drawable = img.getDrawable();
                        if (drawable instanceof android.graphics.drawable.Animatable) {
                            if (!((android.graphics.drawable.Animatable) (drawable)).isRunning()) {
                                ((android.graphics.drawable.Animatable) (drawable)).start();
                            }
                        }
                    } else
                        if (view instanceof android.view.ViewStub) {
                            // By default, ViewStub will be set to GONE and won't be inflate. If the XML has the
                            // tools:visibility attribute we'll workaround that behavior.
                            java.lang.String visibility = attrs.getAttributeValue(BridgeConstants.NS_TOOLS_URI, SdkConstants.ATTR_VISIBILITY);
                            boolean isVisible = "visible".equals(visibility);
                            if (isVisible || "invisible".equals(visibility)) {
                                // We can not inflate the view until is attached to its parent so we need to delay
                                // the setVisible call until after that happens.
                                final int visibilityValue = (isVisible) ? android.view.View.VISIBLE : android.view.View.INVISIBLE;
                                view.addOnAttachStateChangeListener(new android.view.View.OnAttachStateChangeListener() {
                                    @java.lang.Override
                                    public void onViewAttachedToWindow(android.view.View v) {
                                        v.removeOnAttachStateChangeListener(this);
                                        view.setVisibility(visibilityValue);
                                    }

                                    @java.lang.Override
                                    public void onViewDetachedFromWindow(android.view.View v) {
                                    }
                                });
                            }
                        }




    }

    public void setIsInMerge(boolean isInMerge) {
        mIsInMerge = isInMerge;
    }

    public void setResourceReference(com.android.ide.common.rendering.api.ResourceReference reference) {
        mResourceReference = reference;
    }

    @java.lang.Override
    public android.view.LayoutInflater cloneInContext(android.content.Context newContext) {
        return new android.view.BridgeInflater(this, newContext);
    }

    /* package */
    static java.lang.Object getViewKeyFromParser(android.util.AttributeSet attrs, com.android.layoutlib.bridge.android.BridgeContext bc, com.android.ide.common.rendering.api.ResourceReference resourceReference, boolean isInMerge) {
        if (!(attrs instanceof com.android.layoutlib.bridge.android.BridgeXmlBlockParser)) {
            return null;
        }
        com.android.layoutlib.bridge.android.BridgeXmlBlockParser parser = ((com.android.layoutlib.bridge.android.BridgeXmlBlockParser) (attrs));
        // get the view key
        java.lang.Object viewKey = parser.getViewCookie();
        if (viewKey == null) {
            int currentDepth = parser.getDepth();
            // test whether we are in an included file or in a adapter binding view.
            com.android.layoutlib.bridge.android.BridgeXmlBlockParser previousParser = bc.getPreviousParser();
            if (previousParser != null) {
                // looks like we are inside an embedded layout.
                // only apply the cookie of the calling node (<include>) if we are at the
                // top level of the embedded layout. If there is a merge tag, then
                // skip it and look for the 2nd level
                int testDepth = (isInMerge) ? 2 : 1;
                if (currentDepth == testDepth) {
                    viewKey = previousParser.getViewCookie();
                    // if we are in a merge, wrap the cookie in a MergeCookie.
                    if ((viewKey != null) && isInMerge) {
                        viewKey = new com.android.ide.common.rendering.api.MergeCookie(viewKey);
                    }
                }
            } else
                if ((resourceReference != null) && (currentDepth == 1)) {
                    // else if there's a resource reference, this means we are in an adapter
                    // binding case. Set the resource ref as the view cookie only for the top
                    // level view.
                    viewKey = resourceReference;
                }

        }
        return viewKey;
    }

    public void postInflateProcess(android.view.View view) {
        if (mOpenDrawerLayouts != null) {
            java.lang.String gravity = mOpenDrawerLayouts.get(view);
            if (gravity != null) {
                com.android.layoutlib.bridge.android.support.DrawerLayoutUtil.openDrawer(view, gravity);
            }
            mOpenDrawerLayouts.remove(view);
        }
    }

    @android.annotation.NonNull
    private java.util.Map<android.view.View, java.lang.String> getDrawerLayoutMap() {
        if (mOpenDrawerLayouts == null) {
            mOpenDrawerLayouts = new java.util.HashMap<>(4);
        }
        return mOpenDrawerLayouts;
    }

    public void onDoneInflation() {
        if (mOpenDrawerLayouts != null) {
            mOpenDrawerLayouts.clear();
        }
    }
}

