/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Delegate used to provide new implementation of a select few methods of {@link LayoutInflater}
 *
 * Through the layoutlib_create tool, the original  methods of LayoutInflater have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class LayoutInflater_Delegate {
    private static final java.lang.String TAG_MERGE = "merge";

    private static final java.lang.String ATTR_LAYOUT = "layout";

    private static final int[] ATTRS_THEME = new int[]{ com.android.internal.R.attr.theme };

    public static boolean sIsInInclude = false;

    /**
     * Recursive method used to descend down the xml hierarchy and instantiate
     * views, instantiate their children, and then call onFinishInflate().
     *
     * This implementation just records the merge status before calling the default implementation.
     */
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void rInflate(android.view.LayoutInflater thisInflater, org.xmlpull.v1.XmlPullParser parser, android.view.View parent, android.content.Context context, android.util.AttributeSet attrs, boolean finishInflate) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (finishInflate == false) {
            // this is a merge rInflate!
            if (thisInflater instanceof android.view.BridgeInflater) {
                ((android.view.BridgeInflater) (thisInflater)).setIsInMerge(true);
            }
        }
        // ---- START DEFAULT IMPLEMENTATION.
        thisInflater.rInflate_Original(parser, parent, context, attrs, finishInflate);
        // ---- END DEFAULT IMPLEMENTATION.
        if (finishInflate == false) {
            // this is a merge rInflate!
            if (thisInflater instanceof android.view.BridgeInflater) {
                ((android.view.BridgeInflater) (thisInflater)).setIsInMerge(false);
            }
        }
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void parseInclude(android.view.LayoutInflater thisInflater, org.xmlpull.v1.XmlPullParser parser, android.content.Context context, android.view.View parent, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        if (parent instanceof android.view.ViewGroup) {
            // Apply a theme wrapper, if requested. This is sort of a weird
            // edge case, since developers think the <include> overwrites
            // values in the AttributeSet of the included View. So, if the
            // included View has a theme attribute, we'll need to ignore it.
            final android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, android.view.LayoutInflater_Delegate.ATTRS_THEME);
            final int themeResId = ta.getResourceId(0, 0);
            final boolean hasThemeOverride = themeResId != 0;
            if (hasThemeOverride) {
                context = new android.view.ContextThemeWrapper(context, themeResId);
            }
            ta.recycle();
            // If the layout is pointing to a theme attribute, we have to
            // massage the value to get a resource identifier out of it.
            int layout = attrs.getAttributeResourceValue(null, android.view.LayoutInflater_Delegate.ATTR_LAYOUT, 0);
            if (layout == 0) {
                final java.lang.String value = attrs.getAttributeValue(null, android.view.LayoutInflater_Delegate.ATTR_LAYOUT);
                if ((value == null) || (value.length() <= 0)) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "You must specify a layout in the" + " include tag: <include layout=\"@layout/layoutID\" />", null);
                    android.view.LayoutInflater.consumeChildElements(parser);
                    return;
                }
                // Attempt to resolve the "?attr/name" string to an identifier.
                layout = context.getResources().getIdentifier(value.substring(1), null, null);
            }
            // The layout might be referencing a theme attribute.
            // ---- START CHANGES
            if (layout != 0) {
                final android.util.TypedValue tempValue = new android.util.TypedValue();
                if (context.getTheme().resolveAttribute(layout, tempValue, true)) {
                    layout = tempValue.resourceId;
                }
            }
            // ---- END CHANGES
            if (layout == 0) {
                final java.lang.String value = attrs.getAttributeValue(null, android.view.LayoutInflater_Delegate.ATTR_LAYOUT);
                if (value == null) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "You must specify a layout in the" + " include tag: <include layout=\"@layout/layoutID\" />", null);
                } else {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, (("You must specify a valid layout " + "reference. The layout ID ") + value) + " is not valid.", null);
                }
            } else {
                final android.content.res.XmlResourceParser childParser = thisInflater.getContext().getResources().getLayout(layout);
                try {
                    final android.util.AttributeSet childAttrs = android.util.Xml.asAttributeSet(childParser);
                    while (((type = childParser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                        // Empty.
                    } 
                    if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, childParser.getPositionDescription() + ": No start tag found!", null);
                        android.view.LayoutInflater.consumeChildElements(parser);
                        return;
                    }
                    final java.lang.String childName = childParser.getName();
                    if (android.view.LayoutInflater_Delegate.TAG_MERGE.equals(childName)) {
                        // Inflate all children.
                        thisInflater.rInflate(childParser, parent, context, childAttrs, false);
                    } else {
                        final android.view.View view = thisInflater.createViewFromTag(parent, childName, context, childAttrs, hasThemeOverride);
                        final android.view.ViewGroup group = ((android.view.ViewGroup) (parent));
                        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.Include);
                        final int id = a.getResourceId(com.android.internal.R.styleable.Include_id, android.view.View.NO_ID);
                        final int visibility = a.getInt(com.android.internal.R.styleable.Include_visibility, -1);
                        a.recycle();
                        // We try to load the layout params set in the <include /> tag. If
                        // they don't exist, we will rely on the layout params set in the
                        // included XML file.
                        // During a layoutparams generation, a runtime exception is thrown
                        // if either layout_width or layout_height is missing. We catch
                        // this exception and set localParams accordingly: true means we
                        // successfully loaded layout params from the <include /> tag,
                        // false means we need to rely on the included layout params.
                        android.view.ViewGroup.LayoutParams params = null;
                        try {
                            // ---- START CHANGES
                            android.view.LayoutInflater_Delegate.sIsInInclude = true;
                            // ---- END CHANGES
                            params = group.generateLayoutParams(attrs);
                        } catch (java.lang.RuntimeException ignored) {
                            // Ignore, just fail over to child attrs.
                        } finally {
                            // ---- START CHANGES
                            android.view.LayoutInflater_Delegate.sIsInInclude = false;
                            // ---- END CHANGES
                        }
                        if (params == null) {
                            params = group.generateLayoutParams(childAttrs);
                        }
                        view.setLayoutParams(params);
                        // Inflate all children.
                        thisInflater.rInflateChildren(childParser, view, childAttrs, true);
                        if (id != android.view.View.NO_ID) {
                            view.setId(id);
                        }
                        switch (visibility) {
                            case 0 :
                                view.setVisibility(android.view.View.VISIBLE);
                                break;
                            case 1 :
                                view.setVisibility(android.view.View.INVISIBLE);
                                break;
                            case 2 :
                                view.setVisibility(android.view.View.GONE);
                                break;
                        }
                        group.addView(view);
                    }
                } finally {
                    childParser.close();
                }
            }
        } else {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "<include /> can only be used inside of a ViewGroup", null);
        }
        android.view.LayoutInflater.consumeChildElements(parser);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void initPrecompiledViews(android.view.LayoutInflater thisInflater) {
        android.view.LayoutInflater_Delegate.initPrecompiledViews(thisInflater, false);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void initPrecompiledViews(android.view.LayoutInflater thisInflater, boolean enablePrecompiledViews) {
        thisInflater.initPrecompiledViews_Original(enablePrecompiledViews);
    }
}

