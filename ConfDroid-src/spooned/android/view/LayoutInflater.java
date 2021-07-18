/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Instantiates a layout XML file into its corresponding {@link android.view.View}
 * objects. It is never used directly. Instead, use
 * {@link android.app.Activity#getLayoutInflater()} or
 * {@link Context#getSystemService} to retrieve a standard LayoutInflater instance
 * that is already hooked up to the current context and correctly configured
 * for the device you are running on.
 *
 * <p>
 * To create a new LayoutInflater with an additional {@link Factory} for your
 * own views, you can use {@link #cloneInContext} to clone an existing
 * ViewFactory, and then call {@link #setFactory} on it to include your
 * Factory.
 *
 * <p>
 * For performance reasons, view inflation relies heavily on pre-processing of
 * XML files that is done at build time. Therefore, it is not currently possible
 * to use LayoutInflater with an XmlPullParser over a plain XML file at runtime;
 * it only works with an XmlPullParser returned from a compiled resource
 * (R.<em>something</em> file.)
 */
@android.annotation.SystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)
public abstract class LayoutInflater {
    private static final java.lang.String TAG = android.view.LayoutInflater.class.getSimpleName();

    private static final boolean DEBUG = false;

    private static final java.lang.String COMPILED_VIEW_DEX_FILE_NAME = "/compiled_view.dex";

    /**
     * Whether or not we use the precompiled layout.
     */
    private static final java.lang.String USE_PRECOMPILED_LAYOUT = "view.precompiled_layout_enabled";

    /**
     * Empty stack trace used to avoid log spam in re-throw exceptions.
     */
    private static final java.lang.StackTraceElement[] EMPTY_STACK_TRACE = new java.lang.StackTraceElement[0];

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    protected final android.content.Context mContext;

    // these are optional, set by the caller
    /**
     * If any developer has desire to change this value, they should instead use
     * {@link #cloneInContext(Context)} and set the new factory in thew newly-created
     * LayoutInflater.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private boolean mFactorySet;

    @android.annotation.UnsupportedAppUsage
    private android.view.LayoutInflater.Factory mFactory;

    @android.annotation.UnsupportedAppUsage
    private android.view.LayoutInflater.Factory2 mFactory2;

    @android.annotation.UnsupportedAppUsage
    private android.view.LayoutInflater.Factory2 mPrivateFactory;

    private android.view.LayoutInflater.Filter mFilter;

    // Indicates whether we should try to inflate layouts using a precompiled layout instead of
    // inflating from the XML resource.
    private boolean mUseCompiledView;

    // This variable holds the classloader that will be used to look for precompiled layouts. The
    // The classloader includes the generated compiled_view.dex file.
    private java.lang.ClassLoader mPrecompiledClassLoader;

    /**
     * This is not a public API. Two APIs are now available to alleviate the need to access
     * this directly: {@link #createView(Context, String, String, AttributeSet)} and
     * {@link #onCreateView(Context, View, String, AttributeSet)}.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    final java.lang.Object[] mConstructorArgs = new java.lang.Object[2];

    @android.annotation.UnsupportedAppUsage
    static final java.lang.Class<?>[] mConstructorSignature = new java.lang.Class[]{ android.content.Context.class, android.util.AttributeSet.class };

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 123769490)
    private static final java.util.HashMap<java.lang.String, java.lang.reflect.Constructor<? extends android.view.View>> sConstructorMap = new java.util.HashMap<java.lang.String, java.lang.reflect.Constructor<? extends android.view.View>>();

    private java.util.HashMap<java.lang.String, java.lang.Boolean> mFilterMap;

    private android.util.TypedValue mTempValue;

    private static final java.lang.String TAG_MERGE = "merge";

    private static final java.lang.String TAG_INCLUDE = "include";

    private static final java.lang.String TAG_1995 = "blink";

    private static final java.lang.String TAG_REQUEST_FOCUS = "requestFocus";

    private static final java.lang.String TAG_TAG = "tag";

    private static final java.lang.String ATTR_LAYOUT = "layout";

    @android.annotation.UnsupportedAppUsage
    private static final int[] ATTRS_THEME = new int[]{ com.android.internal.R.attr.theme };

    /**
     * Hook to allow clients of the LayoutInflater to restrict the set of Views that are allowed
     * to be inflated.
     */
    public interface Filter {
        /**
         * Hook to allow clients of the LayoutInflater to restrict the set of Views
         * that are allowed to be inflated.
         *
         * @param clazz
         * 		The class object for the View that is about to be inflated
         * @return True if this class is allowed to be inflated, or false otherwise
         */
        @java.lang.SuppressWarnings("unchecked")
        boolean onLoadClass(java.lang.Class clazz);
    }

    public interface Factory {
        /**
         * Hook you can supply that is called when inflating from a LayoutInflater.
         * You can use this to customize the tag names available in your XML
         * layout files.
         *
         * <p>
         * Note that it is good practice to prefix these custom names with your
         * package (i.e., com.coolcompany.apps) to avoid conflicts with system
         * names.
         *
         * @param name
         * 		Tag name to be inflated.
         * @param context
         * 		The context the view is being created in.
         * @param attrs
         * 		Inflation attributes as specified in XML file.
         * @return View Newly created view. Return null for the default
        behavior.
         */
        @android.annotation.Nullable
        android.view.View onCreateView(@android.annotation.NonNull
        java.lang.String name, @android.annotation.NonNull
        android.content.Context context, @android.annotation.NonNull
        android.util.AttributeSet attrs);
    }

    public interface Factory2 extends android.view.LayoutInflater.Factory {
        /**
         * Version of {@link #onCreateView(String, Context, AttributeSet)}
         * that also supplies the parent that the view created view will be
         * placed in.
         *
         * @param parent
         * 		The parent that the created view will be placed
         * 		in; <em>note that this may be null</em>.
         * @param name
         * 		Tag name to be inflated.
         * @param context
         * 		The context the view is being created in.
         * @param attrs
         * 		Inflation attributes as specified in XML file.
         * @return View Newly created view. Return null for the default
        behavior.
         */
        @android.annotation.Nullable
        android.view.View onCreateView(@android.annotation.Nullable
        android.view.View parent, @android.annotation.NonNull
        java.lang.String name, @android.annotation.NonNull
        android.content.Context context, @android.annotation.NonNull
        android.util.AttributeSet attrs);
    }

    private static class FactoryMerger implements android.view.LayoutInflater.Factory2 {
        private final android.view.LayoutInflater.Factory mF1;

        private final android.view.LayoutInflater.Factory mF2;

        private final android.view.LayoutInflater.Factory2 mF12;

        private final android.view.LayoutInflater.Factory2 mF22;

        FactoryMerger(android.view.LayoutInflater.Factory f1, android.view.LayoutInflater.Factory2 f12, android.view.LayoutInflater.Factory f2, android.view.LayoutInflater.Factory2 f22) {
            mF1 = f1;
            mF2 = f2;
            mF12 = f12;
            mF22 = f22;
        }

        @android.annotation.Nullable
        public android.view.View onCreateView(@android.annotation.NonNull
        java.lang.String name, @android.annotation.NonNull
        android.content.Context context, @android.annotation.NonNull
        android.util.AttributeSet attrs) {
            android.view.View v = mF1.onCreateView(name, context, attrs);
            if (v != null)
                return v;

            return mF2.onCreateView(name, context, attrs);
        }

        @android.annotation.Nullable
        public android.view.View onCreateView(@android.annotation.Nullable
        android.view.View parent, @android.annotation.NonNull
        java.lang.String name, @android.annotation.NonNull
        android.content.Context context, @android.annotation.NonNull
        android.util.AttributeSet attrs) {
            android.view.View v = (mF12 != null) ? mF12.onCreateView(parent, name, context, attrs) : mF1.onCreateView(name, context, attrs);
            if (v != null)
                return v;

            return mF22 != null ? mF22.onCreateView(parent, name, context, attrs) : mF2.onCreateView(name, context, attrs);
        }
    }

    /**
     * Create a new LayoutInflater instance associated with a particular Context.
     * Applications will almost always want to use
     * {@link Context#getSystemService Context.getSystemService()} to retrieve
     * the standard {@link Context#LAYOUT_INFLATER_SERVICE Context.INFLATER_SERVICE}.
     *
     * @param context
     * 		The Context in which this LayoutInflater will create its
     * 		Views; most importantly, this supplies the theme from which the default
     * 		values for their attributes are retrieved.
     */
    protected LayoutInflater(android.content.Context context) {
        mContext = context;
        initPrecompiledViews();
    }

    /**
     * Create a new LayoutInflater instance that is a copy of an existing
     * LayoutInflater, optionally with its Context changed.  For use in
     * implementing {@link #cloneInContext}.
     *
     * @param original
     * 		The original LayoutInflater to copy.
     * @param newContext
     * 		The new Context to use.
     */
    protected LayoutInflater(android.view.LayoutInflater original, android.content.Context newContext) {
        mContext = newContext;
        mFactory = original.mFactory;
        mFactory2 = original.mFactory2;
        mPrivateFactory = original.mPrivateFactory;
        setFilter(original.mFilter);
        initPrecompiledViews();
    }

    /**
     * Obtains the LayoutInflater from the given context.
     */
    public static android.view.LayoutInflater from(android.content.Context context) {
        android.view.LayoutInflater LayoutInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        if (LayoutInflater == null) {
            throw new java.lang.AssertionError("LayoutInflater not found.");
        }
        return LayoutInflater;
    }

    /**
     * Create a copy of the existing LayoutInflater object, with the copy
     * pointing to a different Context than the original.  This is used by
     * {@link ContextThemeWrapper} to create a new LayoutInflater to go along
     * with the new Context theme.
     *
     * @param newContext
     * 		The new Context to associate with the new LayoutInflater.
     * 		May be the same as the original Context if desired.
     * @return Returns a brand spanking new LayoutInflater object associated with
    the given Context.
     */
    public abstract android.view.LayoutInflater cloneInContext(android.content.Context newContext);

    /**
     * Return the context we are running in, for access to resources, class
     * loader, etc.
     */
    public android.content.Context getContext() {
        return mContext;
    }

    /**
     * Return the current {@link Factory} (or null). This is called on each element
     * name. If the factory returns a View, add that to the hierarchy. If it
     * returns null, proceed to call onCreateView(name).
     */
    public final android.view.LayoutInflater.Factory getFactory() {
        return mFactory;
    }

    /**
     * Return the current {@link Factory2}.  Returns null if no factory is set
     * or the set factory does not implement the {@link Factory2} interface.
     * This is called on each element
     * name. If the factory returns a View, add that to the hierarchy. If it
     * returns null, proceed to call onCreateView(name).
     */
    public final android.view.LayoutInflater.Factory2 getFactory2() {
        return mFactory2;
    }

    /**
     * Attach a custom Factory interface for creating views while using
     * this LayoutInflater.  This must not be null, and can only be set once;
     * after setting, you can not change the factory.  This is
     * called on each element name as the xml is parsed. If the factory returns
     * a View, that is added to the hierarchy. If it returns null, the next
     * factory default {@link #onCreateView} method is called.
     *
     * <p>If you have an existing
     * LayoutInflater and want to add your own factory to it, use
     * {@link #cloneInContext} to clone the existing instance and then you
     * can use this function (once) on the returned new instance.  This will
     * merge your own factory with whatever factory the original instance is
     * using.
     */
    public void setFactory(android.view.LayoutInflater.Factory factory) {
        if (mFactorySet) {
            throw new java.lang.IllegalStateException("A factory has already been set on this LayoutInflater");
        }
        if (factory == null) {
            throw new java.lang.NullPointerException("Given factory can not be null");
        }
        mFactorySet = true;
        if (mFactory == null) {
            mFactory = factory;
        } else {
            mFactory = new android.view.LayoutInflater.FactoryMerger(factory, null, mFactory, mFactory2);
        }
    }

    /**
     * Like {@link #setFactory}, but allows you to set a {@link Factory2}
     * interface.
     */
    public void setFactory2(android.view.LayoutInflater.Factory2 factory) {
        if (mFactorySet) {
            throw new java.lang.IllegalStateException("A factory has already been set on this LayoutInflater");
        }
        if (factory == null) {
            throw new java.lang.NullPointerException("Given factory can not be null");
        }
        mFactorySet = true;
        if (mFactory == null) {
            mFactory = mFactory2 = factory;
        } else {
            mFactory = mFactory2 = new android.view.LayoutInflater.FactoryMerger(factory, factory, mFactory, mFactory2);
        }
    }

    /**
     *
     *
     * @unknown for use by framework
     */
    @android.annotation.UnsupportedAppUsage
    public void setPrivateFactory(android.view.LayoutInflater.Factory2 factory) {
        if (mPrivateFactory == null) {
            mPrivateFactory = factory;
        } else {
            mPrivateFactory = new android.view.LayoutInflater.FactoryMerger(factory, factory, mPrivateFactory, mPrivateFactory);
        }
    }

    /**
     *
     *
     * @return The {@link Filter} currently used by this LayoutInflater to restrict the set of Views
    that are allowed to be inflated.
     */
    public android.view.LayoutInflater.Filter getFilter() {
        return mFilter;
    }

    /**
     * Sets the {@link Filter} to by this LayoutInflater. If a view is attempted to be inflated
     * which is not allowed by the {@link Filter}, the {@link #inflate(int, ViewGroup)} call will
     * throw an {@link InflateException}. This filter will replace any previous filter set on this
     * LayoutInflater.
     *
     * @param filter
     * 		The Filter which restricts the set of Views that are allowed to be inflated.
     * 		This filter will replace any previous filter set on this LayoutInflater.
     */
    public void setFilter(android.view.LayoutInflater.Filter filter) {
        mFilter = filter;
        if (filter != null) {
            mFilterMap = new java.util.HashMap<java.lang.String, java.lang.Boolean>();
        }
    }

    private void initPrecompiledViews() {
        // Precompiled layouts are not supported in this release.
        boolean enabled = false;
        initPrecompiledViews(enabled);
    }

    private void initPrecompiledViews(boolean enablePrecompiledViews) {
        mUseCompiledView = enablePrecompiledViews;
        if (!mUseCompiledView) {
            mPrecompiledClassLoader = null;
            return;
        }
        // Make sure the application allows code generation
        android.content.pm.ApplicationInfo appInfo = mContext.getApplicationInfo();
        if (appInfo.isEmbeddedDexUsed() || appInfo.isPrivilegedApp()) {
            mUseCompiledView = false;
            return;
        }
        // Try to load the precompiled layout file.
        try {
            mPrecompiledClassLoader = mContext.getClassLoader();
            java.lang.String dexFile = mContext.getCodeCacheDir() + android.view.LayoutInflater.COMPILED_VIEW_DEX_FILE_NAME;
            if (new java.io.File(dexFile).exists()) {
                mPrecompiledClassLoader = new dalvik.system.PathClassLoader(dexFile, mPrecompiledClassLoader);
            } else {
                // If the precompiled layout file doesn't exist, then disable precompiled
                // layouts.
                mUseCompiledView = false;
            }
        } catch (java.lang.Throwable e) {
            if (android.view.LayoutInflater.DEBUG) {
                android.util.Log.e(android.view.LayoutInflater.TAG, "Failed to initialized precompiled views layouts", e);
            }
            mUseCompiledView = false;
        }
        if (!mUseCompiledView) {
            mPrecompiledClassLoader = null;
        }
    }

    /**
     *
     *
     * @unknown for use by CTS tests
     */
    @android.annotation.TestApi
    public void setPrecompiledLayoutsEnabledForTesting(boolean enablePrecompiledLayouts) {
        initPrecompiledViews(enablePrecompiledLayouts);
    }

    /**
     * Inflate a new view hierarchy from the specified xml resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param resource
     * 		ID for an XML layout resource to load (e.g.,
     * 		<code>R.layout.main_page</code>)
     * @param root
     * 		Optional view to be the parent of the generated hierarchy.
     * @return The root View of the inflated hierarchy. If root was supplied,
    this is the root View; otherwise it is the root of the inflated
    XML file.
     */
    public android.view.View inflate(@android.annotation.LayoutRes
    int resource, @android.annotation.Nullable
    android.view.ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    /**
     * Inflate a new view hierarchy from the specified xml node. Throws
     * {@link InflateException} if there is an error. *
     * <p>
     * <em><strong>Important</strong></em>&nbsp;&nbsp;&nbsp;For performance
     * reasons, view inflation relies heavily on pre-processing of XML files
     * that is done at build time. Therefore, it is not currently possible to
     * use LayoutInflater with an XmlPullParser over a plain XML file at runtime.
     *
     * @param parser
     * 		XML dom node containing the description of the view
     * 		hierarchy.
     * @param root
     * 		Optional view to be the parent of the generated hierarchy.
     * @return The root View of the inflated hierarchy. If root was supplied,
    this is the root View; otherwise it is the root of the inflated
    XML file.
     */
    public android.view.View inflate(org.xmlpull.v1.XmlPullParser parser, @android.annotation.Nullable
    android.view.ViewGroup root) {
        return inflate(parser, root, root != null);
    }

    /**
     * Inflate a new view hierarchy from the specified xml resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param resource
     * 		ID for an XML layout resource to load (e.g.,
     * 		<code>R.layout.main_page</code>)
     * @param root
     * 		Optional view to be the parent of the generated hierarchy (if
     * 		<em>attachToRoot</em> is true), or else simply an object that
     * 		provides a set of LayoutParams values for root of the returned
     * 		hierarchy (if <em>attachToRoot</em> is false.)
     * @param attachToRoot
     * 		Whether the inflated hierarchy should be attached to
     * 		the root parameter? If false, root is only used to create the
     * 		correct subclass of LayoutParams for the root view in the XML.
     * @return The root View of the inflated hierarchy. If root was supplied and
    attachToRoot is true, this is root; otherwise it is the root of
    the inflated XML file.
     */
    public android.view.View inflate(@android.annotation.LayoutRes
    int resource, @android.annotation.Nullable
    android.view.ViewGroup root, boolean attachToRoot) {
        final android.content.res.Resources res = getContext().getResources();
        if (android.view.LayoutInflater.DEBUG) {
            android.util.Log.d(android.view.LayoutInflater.TAG, ((("INFLATING from resource: \"" + res.getResourceName(resource)) + "\" (") + java.lang.Integer.toHexString(resource)) + ")");
        }
        android.view.View view = tryInflatePrecompiled(resource, res, root, attachToRoot);
        if (view != null) {
            return view;
        }
        android.content.res.XmlResourceParser parser = res.getLayout(resource);
        try {
            return inflate(parser, root, attachToRoot);
        } finally {
            parser.close();
        }
    }

    @android.annotation.Nullable
    private android.view.View tryInflatePrecompiled(@android.annotation.LayoutRes
    int resource, android.content.res.Resources res, @android.annotation.Nullable
    android.view.ViewGroup root, boolean attachToRoot) {
        if (!mUseCompiledView) {
            return null;
        }
        android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "inflate (precompiled)");
        // Try to inflate using a precompiled layout.
        java.lang.String pkg = res.getResourcePackageName(resource);
        java.lang.String layout = res.getResourceEntryName(resource);
        try {
            java.lang.Class clazz = java.lang.Class.forName(("" + pkg) + ".CompiledView", false, mPrecompiledClassLoader);
            java.lang.reflect.Method inflater = clazz.getMethod(layout, android.content.Context.class, int.class);
            android.view.View view = ((android.view.View) (inflater.invoke(null, mContext, resource)));
            if ((view != null) && (root != null)) {
                // We were able to use the precompiled inflater, but now we need to do some work to
                // attach the view to the root correctly.
                android.content.res.XmlResourceParser parser = res.getLayout(resource);
                try {
                    android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
                    advanceToRootNode(parser);
                    android.view.ViewGroup.LayoutParams params = root.generateLayoutParams(attrs);
                    if (attachToRoot) {
                        root.addView(view, params);
                    } else {
                        view.setLayoutParams(params);
                    }
                } finally {
                    parser.close();
                }
            }
            return view;
        } catch (java.lang.Throwable e) {
            if (android.view.LayoutInflater.DEBUG) {
                android.util.Log.e(android.view.LayoutInflater.TAG, "Failed to use precompiled view", e);
            }
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
        return null;
    }

    /**
     * Advances the given parser to the first START_TAG. Throws InflateException if no start tag is
     * found.
     */
    private void advanceToRootNode(org.xmlpull.v1.XmlPullParser parser) throws android.view.InflateException, java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Look for the root node.
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            // Empty
        } 
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
            throw new android.view.InflateException(parser.getPositionDescription() + ": No start tag found!");
        }
    }

    /**
     * Inflate a new view hierarchy from the specified XML node. Throws
     * {@link InflateException} if there is an error.
     * <p>
     * <em><strong>Important</strong></em>&nbsp;&nbsp;&nbsp;For performance
     * reasons, view inflation relies heavily on pre-processing of XML files
     * that is done at build time. Therefore, it is not currently possible to
     * use LayoutInflater with an XmlPullParser over a plain XML file at runtime.
     *
     * @param parser
     * 		XML dom node containing the description of the view
     * 		hierarchy.
     * @param root
     * 		Optional view to be the parent of the generated hierarchy (if
     * 		<em>attachToRoot</em> is true), or else simply an object that
     * 		provides a set of LayoutParams values for root of the returned
     * 		hierarchy (if <em>attachToRoot</em> is false.)
     * @param attachToRoot
     * 		Whether the inflated hierarchy should be attached to
     * 		the root parameter? If false, root is only used to create the
     * 		correct subclass of LayoutParams for the root view in the XML.
     * @return The root View of the inflated hierarchy. If root was supplied and
    attachToRoot is true, this is root; otherwise it is the root of
    the inflated XML file.
     */
    public android.view.View inflate(org.xmlpull.v1.XmlPullParser parser, @android.annotation.Nullable
    android.view.ViewGroup root, boolean attachToRoot) {
        synchronized(mConstructorArgs) {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, "inflate");
            final android.content.Context inflaterContext = mContext;
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            android.content.Context lastContext = ((android.content.Context) (mConstructorArgs[0]));
            mConstructorArgs[0] = inflaterContext;
            android.view.View result = root;
            try {
                advanceToRootNode(parser);
                final java.lang.String name = parser.getName();
                if (android.view.LayoutInflater.DEBUG) {
                    java.lang.System.out.println("**************************");
                    java.lang.System.out.println("Creating root view: " + name);
                    java.lang.System.out.println("**************************");
                }
                if (android.view.LayoutInflater.TAG_MERGE.equals(name)) {
                    if ((root == null) || (!attachToRoot)) {
                        throw new android.view.InflateException("<merge /> can be used only with a valid " + "ViewGroup root and attachToRoot=true");
                    }
                    rInflate(parser, root, inflaterContext, attrs, false);
                } else {
                    // Temp is the root view that was found in the xml
                    final android.view.View temp = createViewFromTag(root, name, inflaterContext, attrs);
                    android.view.ViewGroup.LayoutParams params = null;
                    if (root != null) {
                        if (android.view.LayoutInflater.DEBUG) {
                            java.lang.System.out.println("Creating params from root: " + root);
                        }
                        // Create layout params that match root, if supplied
                        params = root.generateLayoutParams(attrs);
                        if (!attachToRoot) {
                            // Set the layout params for temp if we are not
                            // attaching. (If we are, we use addView, below)
                            temp.setLayoutParams(params);
                        }
                    }
                    if (android.view.LayoutInflater.DEBUG) {
                        java.lang.System.out.println("-----> start inflating children");
                    }
                    // Inflate all children under temp against its context.
                    rInflateChildren(parser, temp, attrs, true);
                    if (android.view.LayoutInflater.DEBUG) {
                        java.lang.System.out.println("-----> done inflating children");
                    }
                    // We are supposed to attach all the views we found (int temp)
                    // to root. Do that now.
                    if ((root != null) && attachToRoot) {
                        root.addView(temp, params);
                    }
                    // Decide whether to return the root that was passed in or the
                    // top view found in xml.
                    if ((root == null) || (!attachToRoot)) {
                        result = temp;
                    }
                }
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                final android.view.InflateException ie = new android.view.InflateException(e.getMessage(), e);
                ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
                throw ie;
            } catch (java.lang.Exception e) {
                final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(inflaterContext, attrs) + ": ") + e.getMessage(), e);
                ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
                throw ie;
            } finally {
                // Don't retain static reference on context.
                mConstructorArgs[0] = lastContext;
                mConstructorArgs[1] = null;
                android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
            return result;
        }
    }

    private static java.lang.String getParserStateDescription(android.content.Context context, android.util.AttributeSet attrs) {
        int sourceResId = android.content.res.Resources.getAttributeSetSourceResId(attrs);
        if (sourceResId == android.content.res.Resources.ID_NULL) {
            return attrs.getPositionDescription();
        } else {
            return (attrs.getPositionDescription() + " in ") + context.getResources().getResourceName(sourceResId);
        }
    }

    private static final java.lang.ClassLoader BOOT_CLASS_LOADER = android.view.LayoutInflater.class.getClassLoader();

    private final boolean verifyClassLoader(java.lang.reflect.Constructor<? extends android.view.View> constructor) {
        final java.lang.ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        if (constructorLoader == android.view.LayoutInflater.BOOT_CLASS_LOADER) {
            // fast path for boot class loader (most common case?) - always ok
            return true;
        }
        // in all normal cases (no dynamic code loading), we will exit the following loop on the
        // first iteration (i.e. when the declaring classloader is the contexts class loader).
        java.lang.ClassLoader cl = mContext.getClassLoader();
        do {
            if (constructorLoader == cl) {
                return true;
            }
            cl = cl.getParent();
        } while (cl != null );
        return false;
    }

    /**
     * Low-level function for instantiating a view by name. This attempts to
     * instantiate a view class of the given <var>name</var> found in this
     * LayoutInflater's ClassLoader. To use an explicit Context in the View
     * constructor, use {@link #createView(Context, String, String, AttributeSet)} instead.
     *
     * <p>
     * There are two things that can happen in an error case: either the
     * exception describing the error will be thrown, or a null will be
     * returned. You must deal with both possibilities -- the former will happen
     * the first time createView() is called for a class of a particular name,
     * the latter every time there-after for that class name.
     *
     * @param name
     * 		The full name of the class to be instantiated.
     * @param attrs
     * 		The XML attributes supplied for this instance.
     * @return View The newly instantiated view, or null.
     */
    public final android.view.View createView(java.lang.String name, java.lang.String prefix, android.util.AttributeSet attrs) throws android.view.InflateException, java.lang.ClassNotFoundException {
        android.content.Context context = ((android.content.Context) (mConstructorArgs[0]));
        if (context == null) {
            context = mContext;
        }
        return createView(context, name, prefix, attrs);
    }

    /**
     * Low-level function for instantiating a view by name. This attempts to
     * instantiate a view class of the given <var>name</var> found in this
     * LayoutInflater's ClassLoader.
     *
     * <p>
     * There are two things that can happen in an error case: either the
     * exception describing the error will be thrown, or a null will be
     * returned. You must deal with both possibilities -- the former will happen
     * the first time createView() is called for a class of a particular name,
     * the latter every time there-after for that class name.
     *
     * @param viewContext
     * 		The context used as the context parameter of the View constructor
     * @param name
     * 		The full name of the class to be instantiated.
     * @param attrs
     * 		The XML attributes supplied for this instance.
     * @return View The newly instantiated view, or null.
     */
    @android.annotation.Nullable
    public final android.view.View createView(@android.annotation.NonNull
    android.content.Context viewContext, @android.annotation.NonNull
    java.lang.String name, @android.annotation.Nullable
    java.lang.String prefix, @android.annotation.Nullable
    android.util.AttributeSet attrs) throws android.view.InflateException, java.lang.ClassNotFoundException {
        java.util.Objects.requireNonNull(viewContext);
        java.util.Objects.requireNonNull(name);
        java.lang.reflect.Constructor<? extends android.view.View> constructor = android.view.LayoutInflater.sConstructorMap.get(name);
        if ((constructor != null) && (!verifyClassLoader(constructor))) {
            constructor = null;
            android.view.LayoutInflater.sConstructorMap.remove(name);
        }
        java.lang.Class<? extends android.view.View> clazz = null;
        try {
            android.os.Trace.traceBegin(Trace.TRACE_TAG_VIEW, name);
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = java.lang.Class.forName(prefix != null ? prefix + name : name, false, mContext.getClassLoader()).asSubclass(android.view.View.class);
                if ((mFilter != null) && (clazz != null)) {
                    boolean allowed = mFilter.onLoadClass(clazz);
                    if (!allowed) {
                        failNotAllowed(name, prefix, viewContext, attrs);
                    }
                }
                constructor = clazz.getConstructor(android.view.LayoutInflater.mConstructorSignature);
                constructor.setAccessible(true);
                android.view.LayoutInflater.sConstructorMap.put(name, constructor);
            } else {
                // If we have a filter, apply it to cached constructor
                if (mFilter != null) {
                    // Have we seen this name before?
                    java.lang.Boolean allowedState = mFilterMap.get(name);
                    if (allowedState == null) {
                        // New class -- remember whether it is allowed
                        clazz = java.lang.Class.forName(prefix != null ? prefix + name : name, false, mContext.getClassLoader()).asSubclass(android.view.View.class);
                        boolean allowed = (clazz != null) && mFilter.onLoadClass(clazz);
                        mFilterMap.put(name, allowed);
                        if (!allowed) {
                            failNotAllowed(name, prefix, viewContext, attrs);
                        }
                    } else
                        if (allowedState.equals(java.lang.Boolean.FALSE)) {
                            failNotAllowed(name, prefix, viewContext, attrs);
                        }

                }
            }
            java.lang.Object lastContext = mConstructorArgs[0];
            mConstructorArgs[0] = viewContext;
            java.lang.Object[] args = mConstructorArgs;
            args[1] = attrs;
            try {
                final android.view.View view = constructor.newInstance(args);
                if (view instanceof android.view.ViewStub) {
                    // Use the same context when inflating ViewStub later.
                    final android.view.ViewStub viewStub = ((android.view.ViewStub) (view));
                    viewStub.setLayoutInflater(cloneInContext(((android.content.Context) (args[0]))));
                }
                return view;
            } finally {
                mConstructorArgs[0] = lastContext;
            }
        } catch (java.lang.NoSuchMethodException e) {
            final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(viewContext, attrs) + ": Error inflating class ") + (prefix != null ? prefix + name : name), e);
            ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
            throw ie;
        } catch (java.lang.ClassCastException e) {
            // If loaded class is not a View subclass
            final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(viewContext, attrs) + ": Class is not a View ") + (prefix != null ? prefix + name : name), e);
            ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
            throw ie;
        } catch (java.lang.ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(viewContext, attrs) + ": Error inflating class ") + (clazz == null ? "<unknown>" : clazz.getName()), e);
            ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
            throw ie;
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
    }

    /**
     * Throw an exception because the specified class is not allowed to be inflated.
     */
    private void failNotAllowed(java.lang.String name, java.lang.String prefix, android.content.Context context, android.util.AttributeSet attrs) {
        throw new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(context, attrs) + ": Class not allowed to be inflated ") + (prefix != null ? prefix + name : name));
    }

    /**
     * This routine is responsible for creating the correct subclass of View
     * given the xml element name. Override it to handle custom view objects. If
     * you override this in your subclass be sure to call through to
     * super.onCreateView(name) for names you do not recognize.
     *
     * @param name
     * 		The fully qualified class name of the View to be create.
     * @param attrs
     * 		An AttributeSet of attributes to apply to the View.
     * @return View The View created.
     */
    protected android.view.View onCreateView(java.lang.String name, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        return createView(name, "android.view.", attrs);
    }

    /**
     * Version of {@link #onCreateView(String, AttributeSet)} that also
     * takes the future parent of the view being constructed.  The default
     * implementation simply calls {@link #onCreateView(String, AttributeSet)}.
     *
     * @param parent
     * 		The future parent of the returned view.  <em>Note that
     * 		this may be null.</em>
     * @param name
     * 		The fully qualified class name of the View to be create.
     * @param attrs
     * 		An AttributeSet of attributes to apply to the View.
     * @return View The View created.
     */
    protected android.view.View onCreateView(android.view.View parent, java.lang.String name, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        return onCreateView(name, attrs);
    }

    /**
     * Version of {@link #onCreateView(View, String, AttributeSet)} that also
     * takes the inflation context.  The default
     * implementation simply calls {@link #onCreateView(View, String, AttributeSet)}.
     *
     * @param viewContext
     * 		The Context to be used as a constructor parameter for the View
     * @param parent
     * 		The future parent of the returned view.  <em>Note that
     * 		this may be null.</em>
     * @param name
     * 		The fully qualified class name of the View to be create.
     * @param attrs
     * 		An AttributeSet of attributes to apply to the View.
     * @return View The View created.
     */
    @android.annotation.Nullable
    public android.view.View onCreateView(@android.annotation.NonNull
    android.content.Context viewContext, @android.annotation.Nullable
    android.view.View parent, @android.annotation.NonNull
    java.lang.String name, @android.annotation.Nullable
    android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        return onCreateView(parent, name, attrs);
    }

    /**
     * Convenience method for calling through to the five-arg createViewFromTag
     * method. This method passes {@code false} for the {@code ignoreThemeAttr}
     * argument and should be used for everything except {@code &gt;include>}
     * tag parsing.
     */
    @android.annotation.UnsupportedAppUsage
    private android.view.View createViewFromTag(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        return createViewFromTag(parent, name, context, attrs, false);
    }

    /**
     * Creates a view from a tag name using the supplied attribute set.
     * <p>
     * <strong>Note:</strong> Default visibility so the BridgeInflater can
     * override it.
     *
     * @param parent
     * 		the parent view, used to inflate layout params
     * @param name
     * 		the name of the XML tag used to define the view
     * @param context
     * 		the inflation context for the view, typically the
     * 		{@code parent} or base layout inflater context
     * @param attrs
     * 		the attribute set for the XML tag used to define the view
     * @param ignoreThemeAttr
     * 		{@code true} to ignore the {@code android:theme}
     * 		attribute (if set) for the view being inflated,
     * 		{@code false} otherwise
     */
    @android.annotation.UnsupportedAppUsage
    android.view.View createViewFromTag(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs, boolean ignoreThemeAttr) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        // Apply a theme wrapper, if allowed and one is specified.
        if (!ignoreThemeAttr) {
            final android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, android.view.LayoutInflater.ATTRS_THEME);
            final int themeResId = ta.getResourceId(0, 0);
            if (themeResId != 0) {
                context = new android.view.ContextThemeWrapper(context, themeResId);
            }
            ta.recycle();
        }
        try {
            android.view.View view = tryCreateView(parent, name, context, attrs);
            if (view == null) {
                final java.lang.Object lastContext = mConstructorArgs[0];
                mConstructorArgs[0] = context;
                try {
                    if ((-1) == name.indexOf('.')) {
                        view = onCreateView(context, parent, name, attrs);
                    } else {
                        view = createView(context, name, null, attrs);
                    }
                } finally {
                    mConstructorArgs[0] = lastContext;
                }
            }
            return view;
        } catch (android.view.InflateException e) {
            throw e;
        } catch (java.lang.ClassNotFoundException e) {
            final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(context, attrs) + ": Error inflating class ") + name, e);
            ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
            throw ie;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException((android.view.LayoutInflater.getParserStateDescription(context, attrs) + ": Error inflating class ") + name, e);
            ie.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
            throw ie;
        }
    }

    /**
     * Tries to create a view from a tag name using the supplied attribute set.
     *
     * This method gives the factory provided by {@link LayoutInflater#setFactory} and
     * {@link LayoutInflater#setFactory2} a chance to create a view. However, it does not apply all
     * of the general view creation logic, and thus may return {@code null} for some tags. This
     * method is used by {@link LayoutInflater#inflate} in creating {@code View} objects.
     *
     * @unknown for use by precompiled layouts.
     * @param parent
     * 		the parent view, used to inflate layout params
     * @param name
     * 		the name of the XML tag used to define the view
     * @param context
     * 		the inflation context for the view, typically the
     * 		{@code parent} or base layout inflater context
     * @param attrs
     * 		the attribute set for the XML tag used to define the view
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 122360734)
    @android.annotation.Nullable
    public final android.view.View tryCreateView(@android.annotation.Nullable
    android.view.View parent, @android.annotation.NonNull
    java.lang.String name, @android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.util.AttributeSet attrs) {
        if (name.equals(android.view.LayoutInflater.TAG_1995)) {
            // Let's party like it's 1995!
            return new android.view.LayoutInflater.BlinkLayout(context, attrs);
        }
        android.view.View view;
        if (mFactory2 != null) {
            view = mFactory2.onCreateView(parent, name, context, attrs);
        } else
            if (mFactory != null) {
                view = mFactory.onCreateView(name, context, attrs);
            } else {
                view = null;
            }

        if ((view == null) && (mPrivateFactory != null)) {
            view = mPrivateFactory.onCreateView(parent, name, context, attrs);
        }
        return view;
    }

    /**
     * Recursive method used to inflate internal (non-root) children. This
     * method calls through to {@link #rInflate} using the parent context as
     * the inflation context.
     * <strong>Note:</strong> Default visibility so the BridgeInflater can
     * call it.
     */
    final void rInflateChildren(org.xmlpull.v1.XmlPullParser parser, android.view.View parent, android.util.AttributeSet attrs, boolean finishInflate) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
    }

    /**
     * Recursive method used to descend down the xml hierarchy and instantiate
     * views, instantiate their children, and then call onFinishInflate().
     * <p>
     * <strong>Note:</strong> Default visibility so the BridgeInflater can
     * override it.
     */
    void rInflate(org.xmlpull.v1.XmlPullParser parser, android.view.View parent, android.content.Context context, android.util.AttributeSet attrs, boolean finishInflate) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int depth = parser.getDepth();
        int type;
        boolean pendingRequestFocus = false;
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final java.lang.String name = parser.getName();
            if (android.view.LayoutInflater.TAG_REQUEST_FOCUS.equals(name)) {
                pendingRequestFocus = true;
                android.view.LayoutInflater.consumeChildElements(parser);
            } else
                if (android.view.LayoutInflater.TAG_TAG.equals(name)) {
                    parseViewTag(parser, parent, attrs);
                } else
                    if (android.view.LayoutInflater.TAG_INCLUDE.equals(name)) {
                        if (parser.getDepth() == 0) {
                            throw new android.view.InflateException("<include /> cannot be the root element");
                        }
                        parseInclude(parser, context, parent, attrs);
                    } else
                        if (android.view.LayoutInflater.TAG_MERGE.equals(name)) {
                            throw new android.view.InflateException("<merge /> must be the root element");
                        } else {
                            final android.view.View view = createViewFromTag(parent, name, context, attrs);
                            final android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (parent));
                            final android.view.ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
                            rInflateChildren(parser, view, attrs, true);
                            viewGroup.addView(view, params);
                        }



        } 
        if (pendingRequestFocus) {
            parent.restoreDefaultFocus();
        }
        if (finishInflate) {
            parent.onFinishInflate();
        }
    }

    /**
     * Parses a <code>&lt;tag&gt;</code> element and sets a keyed tag on the
     * containing View.
     */
    private void parseViewTag(org.xmlpull.v1.XmlPullParser parser, android.view.View view, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final android.content.Context context = view.getContext();
        final android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewTag);
        final int key = ta.getResourceId(R.styleable.ViewTag_id, 0);
        final java.lang.CharSequence value = ta.getText(R.styleable.ViewTag_value);
        view.setTag(key, value);
        ta.recycle();
        android.view.LayoutInflater.consumeChildElements(parser);
    }

    @android.annotation.UnsupportedAppUsage
    private void parseInclude(org.xmlpull.v1.XmlPullParser parser, android.content.Context context, android.view.View parent, android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        if (!(parent instanceof android.view.ViewGroup)) {
            throw new android.view.InflateException("<include /> can only be used inside of a ViewGroup");
        }
        // Apply a theme wrapper, if requested. This is sort of a weird
        // edge case, since developers think the <include> overwrites
        // values in the AttributeSet of the included View. So, if the
        // included View has a theme attribute, we'll need to ignore it.
        final android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, android.view.LayoutInflater.ATTRS_THEME);
        final int themeResId = ta.getResourceId(0, 0);
        final boolean hasThemeOverride = themeResId != 0;
        if (hasThemeOverride) {
            context = new android.view.ContextThemeWrapper(context, themeResId);
        }
        ta.recycle();
        // If the layout is pointing to a theme attribute, we have to
        // massage the value to get a resource identifier out of it.
        int layout = attrs.getAttributeResourceValue(null, android.view.LayoutInflater.ATTR_LAYOUT, 0);
        if (layout == 0) {
            final java.lang.String value = attrs.getAttributeValue(null, android.view.LayoutInflater.ATTR_LAYOUT);
            if ((value == null) || (value.length() <= 0)) {
                throw new android.view.InflateException("You must specify a layout in the" + " include tag: <include layout=\"@layout/layoutID\" />");
            }
            // Attempt to resolve the "?attr/name" string to an attribute
            // within the default (e.g. application) package.
            layout = context.getResources().getIdentifier(value.substring(1), "attr", context.getPackageName());
        }
        // The layout might be referencing a theme attribute.
        if (mTempValue == null) {
            mTempValue = new android.util.TypedValue();
        }
        if ((layout != 0) && context.getTheme().resolveAttribute(layout, mTempValue, true)) {
            layout = mTempValue.resourceId;
        }
        if (layout == 0) {
            final java.lang.String value = attrs.getAttributeValue(null, android.view.LayoutInflater.ATTR_LAYOUT);
            throw new android.view.InflateException((("You must specify a valid layout " + "reference. The layout ID ") + value) + " is not valid.");
        }
        final android.view.View precompiled = /* attachToRoot= */
        tryInflatePrecompiled(layout, context.getResources(), ((android.view.ViewGroup) (parent)), true);
        if (precompiled == null) {
            final android.content.res.XmlResourceParser childParser = context.getResources().getLayout(layout);
            try {
                final android.util.AttributeSet childAttrs = android.util.Xml.asAttributeSet(childParser);
                while (((type = childParser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                    // Empty.
                } 
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new android.view.InflateException(android.view.LayoutInflater.getParserStateDescription(context, childAttrs) + ": No start tag found!");
                }
                final java.lang.String childName = childParser.getName();
                if (android.view.LayoutInflater.TAG_MERGE.equals(childName)) {
                    // The <merge> tag doesn't support android:theme, so
                    // nothing special to do here.
                    rInflate(childParser, parent, context, childAttrs, false);
                } else {
                    final android.view.View view = createViewFromTag(parent, childName, context, childAttrs, hasThemeOverride);
                    final android.view.ViewGroup group = ((android.view.ViewGroup) (parent));
                    final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Include);
                    final int id = a.getResourceId(R.styleable.Include_id, android.view.View.NO_ID);
                    final int visibility = a.getInt(R.styleable.Include_visibility, -1);
                    a.recycle();
                    // We try to load the layout params set in the <include /> tag.
                    // If the parent can't generate layout params (ex. missing width
                    // or height for the framework ViewGroups, though this is not
                    // necessarily true of all ViewGroups) then we expect it to throw
                    // a runtime exception.
                    // We catch this exception and set localParams accordingly: true
                    // means we successfully loaded layout params from the <include>
                    // tag, false means we need to rely on the included layout params.
                    android.view.ViewGroup.LayoutParams params = null;
                    try {
                        params = group.generateLayoutParams(attrs);
                    } catch (java.lang.RuntimeException e) {
                        // Ignore, just fail over to child attrs.
                    }
                    if (params == null) {
                        params = group.generateLayoutParams(childAttrs);
                    }
                    view.setLayoutParams(params);
                    // Inflate all children.
                    rInflateChildren(childParser, view, childAttrs, true);
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
        android.view.LayoutInflater.consumeChildElements(parser);
    }

    /**
     * <strong>Note:</strong> default visibility so that
     * LayoutInflater_Delegate can call it.
     */
    static final void consumeChildElements(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type;
        final int currentDepth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > currentDepth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            // Empty
        } 
    }

    private static class BlinkLayout extends android.widget.FrameLayout {
        private static final int MESSAGE_BLINK = 0x42;

        private static final int BLINK_DELAY = 500;

        private boolean mBlink;

        private boolean mBlinkState;

        private final android.os.Handler mHandler;

        public BlinkLayout(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            mHandler = new android.os.Handler(new android.os.Handler.Callback() {
                @java.lang.Override
                public boolean handleMessage(android.os.Message msg) {
                    if (msg.what == android.view.LayoutInflater.BlinkLayout.MESSAGE_BLINK) {
                        if (mBlink) {
                            mBlinkState = !mBlinkState;
                            makeBlink();
                        }
                        invalidate();
                        return true;
                    }
                    return false;
                }
            });
        }

        private void makeBlink() {
            android.os.Message message = mHandler.obtainMessage(android.view.LayoutInflater.BlinkLayout.MESSAGE_BLINK);
            mHandler.sendMessageDelayed(message, android.view.LayoutInflater.BlinkLayout.BLINK_DELAY);
        }

        @java.lang.Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            mBlink = true;
            mBlinkState = true;
            makeBlink();
        }

        @java.lang.Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            mBlink = false;
            mBlinkState = true;
            mHandler.removeMessages(android.view.LayoutInflater.BlinkLayout.MESSAGE_BLINK);
        }

        @java.lang.Override
        protected void dispatchDraw(android.graphics.Canvas canvas) {
            if (mBlinkState) {
                super.dispatchDraw(canvas);
            }
        }
    }
}

