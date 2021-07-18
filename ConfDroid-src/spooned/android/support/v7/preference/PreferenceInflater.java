/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * limitations under the License
 */
package android.support.v7.preference;


/**
 * The {@link PreferenceInflater} is used to inflate preference hierarchies from
 * XML files.
 */
class PreferenceInflater {
    private static final java.lang.String TAG = "PreferenceInflater";

    private static final java.lang.Class<?>[] CONSTRUCTOR_SIGNATURE = new java.lang.Class[]{ android.content.Context.class, android.util.AttributeSet.class };

    private static final java.util.HashMap<java.lang.String, java.lang.reflect.Constructor> CONSTRUCTOR_MAP = new java.util.HashMap<>();

    private final android.content.Context mContext;

    private final java.lang.Object[] mConstructorArgs = new java.lang.Object[2];

    private android.support.v7.preference.PreferenceManager mPreferenceManager;

    private java.lang.String[] mDefaultPackages;

    private static final java.lang.String INTENT_TAG_NAME = "intent";

    private static final java.lang.String EXTRA_TAG_NAME = "extra";

    public PreferenceInflater(android.content.Context context, android.support.v7.preference.PreferenceManager preferenceManager) {
        mContext = context;
        init(preferenceManager);
    }

    private void init(android.support.v7.preference.PreferenceManager preferenceManager) {
        mPreferenceManager = preferenceManager;
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            setDefaultPackages(new java.lang.String[]{ "android.support.v14.preference.", "android.support.v7.preference." });
        } else {
            setDefaultPackages(new java.lang.String[]{ "android.support.v7.preference." });
        }
    }

    /**
     * Sets the default package that will be searched for classes to construct
     * for tag names that have no explicit package.
     *
     * @param defaultPackage
     * 		The default package. This will be prepended to the
     * 		tag name, so it should end with a period.
     */
    public void setDefaultPackages(java.lang.String[] defaultPackage) {
        mDefaultPackages = defaultPackage;
    }

    /**
     * Returns the default package, or null if it is not set.
     *
     * @see #setDefaultPackages(String[])
     * @return The default package.
     */
    public java.lang.String[] getDefaultPackages() {
        return mDefaultPackages;
    }

    /**
     * Return the context we are running in, for access to resources, class
     * loader, etc.
     */
    public android.content.Context getContext() {
        return mContext;
    }

    /**
     * Inflate a new item hierarchy from the specified xml resource. Throws
     * InflaterException if there is an error.
     *
     * @param resource
     * 		ID for an XML resource to load (e.g.,
     * 		<code>R.layout.main_page</code>)
     * @param root
     * 		Optional parent of the generated hierarchy.
     * @return The root of the inflated hierarchy. If root was supplied,
    this is the root item; otherwise it is the root of the inflated
    XML file.
     */
    public android.support.v7.preference.Preference inflate(int resource, @android.support.annotation.Nullable
    android.support.v7.preference.PreferenceGroup root) {
        android.content.res.XmlResourceParser parser = getContext().getResources().getXml(resource);
        try {
            return inflate(parser, root);
        } finally {
            parser.close();
        }
    }

    /**
     * Inflate a new hierarchy from the specified XML node. Throws
     * InflaterException if there is an error.
     * <p>
     * <em><strong>Important</strong></em>&nbsp;&nbsp;&nbsp;For performance
     * reasons, inflation relies heavily on pre-processing of XML files
     * that is done at build time. Therefore, it is not currently possible to
     * use inflater with an XmlPullParser over a plain XML file at runtime.
     *
     * @param parser
     * 		XML dom node containing the description of the
     * 		hierarchy.
     * @param root
     * 		Optional to be the parent of the generated hierarchy (if
     * 		<em>attachToRoot</em> is true), or else simply an object that
     * 		provides a set of values for root of the returned
     * 		hierarchy (if <em>attachToRoot</em> is false.)
     * @return The root of the inflated hierarchy. If root was supplied,
    this is root; otherwise it is the root of
    the inflated XML file.
     */
    public android.support.v7.preference.Preference inflate(org.xmlpull.v1.XmlPullParser parser, @android.support.annotation.Nullable
    android.support.v7.preference.PreferenceGroup root) {
        synchronized(mConstructorArgs) {
            final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            mConstructorArgs[0] = mContext;
            final android.support.v7.preference.Preference result;
            try {
                // Look for the root node.
                int type;
                do {
                    type = parser.next();
                } while ((type != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) );
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new android.view.InflateException(parser.getPositionDescription() + ": No start tag found!");
                }
                // Temp is the root that was found in the xml
                android.support.v7.preference.Preference xmlRoot = createItemFromTag(parser.getName(), attrs);
                result = onMergeRoots(root, ((android.support.v7.preference.PreferenceGroup) (xmlRoot)));
                // Inflate all children under temp
                rInflate(parser, result, attrs);
            } catch (android.view.InflateException e) {
                throw e;
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                final android.view.InflateException ex = new android.view.InflateException(e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (java.io.IOException e) {
                final android.view.InflateException ex = new android.view.InflateException((parser.getPositionDescription() + ": ") + e.getMessage());
                ex.initCause(e);
                throw ex;
            }
            return result;
        }
    }

    @android.support.annotation.NonNull
    private android.support.v7.preference.PreferenceGroup onMergeRoots(android.support.v7.preference.PreferenceGroup givenRoot, @android.support.annotation.NonNull
    android.support.v7.preference.PreferenceGroup xmlRoot) {
        // If we were given a Preferences, use it as the root (ignoring the root
        // Preferences from the XML file).
        if (givenRoot == null) {
            xmlRoot.onAttachedToHierarchy(mPreferenceManager);
            return xmlRoot;
        } else {
            return givenRoot;
        }
    }

    /**
     * Low-level function for instantiating by name. This attempts to
     * instantiate class of the given <var>name</var> found in this
     * inflater's ClassLoader.
     *
     * <p>
     * There are two things that can happen in an error case: either the
     * exception describing the error will be thrown, or a null will be
     * returned. You must deal with both possibilities -- the former will happen
     * the first time createItem() is called for a class of a particular name,
     * the latter every time there-after for that class name.
     *
     * @param name
     * 		The full name of the class to be instantiated.
     * @param attrs
     * 		The XML attributes supplied for this instance.
     * @return The newly instantiated item, or null.
     */
    private android.support.v7.preference.Preference createItem(@android.support.annotation.NonNull
    java.lang.String name, @android.support.annotation.Nullable
    java.lang.String[] prefixes, android.util.AttributeSet attrs) throws android.view.InflateException, java.lang.ClassNotFoundException {
        java.lang.reflect.Constructor constructor = android.support.v7.preference.PreferenceInflater.CONSTRUCTOR_MAP.get(name);
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real,
                // and try to add it
                final java.lang.ClassLoader classLoader = mContext.getClassLoader();
                java.lang.Class<?> clazz = null;
                if ((prefixes == null) || (prefixes.length == 0)) {
                    clazz = classLoader.loadClass(name);
                } else {
                    java.lang.ClassNotFoundException notFoundException = null;
                    for (final java.lang.String prefix : prefixes) {
                        try {
                            clazz = classLoader.loadClass(prefix + name);
                            break;
                        } catch (final java.lang.ClassNotFoundException e) {
                            notFoundException = e;
                        }
                    }
                    if (clazz == null) {
                        if (notFoundException == null) {
                            throw new android.view.InflateException((attrs.getPositionDescription() + ": Error inflating class ") + name);
                        } else {
                            throw notFoundException;
                        }
                    }
                }
                constructor = clazz.getConstructor(android.support.v7.preference.PreferenceInflater.CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                android.support.v7.preference.PreferenceInflater.CONSTRUCTOR_MAP.put(name, constructor);
            }
            java.lang.Object[] args = mConstructorArgs;
            args[1] = attrs;
            return ((android.support.v7.preference.Preference) (constructor.newInstance(args)));
        } catch (java.lang.ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException((attrs.getPositionDescription() + ": Error inflating class ") + name);
            ie.initCause(e);
            throw ie;
        }
    }

    /**
     * This routine is responsible for creating the correct subclass of item
     * given the xml element name. Override it to handle custom item objects. If
     * you override this in your subclass be sure to call through to
     * super.onCreateItem(name) for names you do not recognize.
     *
     * @param name
     * 		The fully qualified class name of the item to be create.
     * @param attrs
     * 		An AttributeSet of attributes to apply to the item.
     * @return The item created.
     */
    protected android.support.v7.preference.Preference onCreateItem(java.lang.String name, android.util.AttributeSet attrs) throws java.lang.ClassNotFoundException {
        return createItem(name, mDefaultPackages, attrs);
    }

    private android.support.v7.preference.Preference createItemFromTag(java.lang.String name, android.util.AttributeSet attrs) {
        try {
            final android.support.v7.preference.Preference item;
            if ((-1) == name.indexOf('.')) {
                item = onCreateItem(name, attrs);
            } else {
                item = createItem(name, null, attrs);
            }
            return item;
        } catch (android.view.InflateException e) {
            throw e;
        } catch (java.lang.ClassNotFoundException e) {
            final android.view.InflateException ie = new android.view.InflateException((attrs.getPositionDescription() + ": Error inflating class (not found)") + name);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException((attrs.getPositionDescription() + ": Error inflating class ") + name);
            ie.initCause(e);
            throw ie;
        }
    }

    /**
     * Recursive method used to descend down the xml hierarchy and instantiate
     * items, instantiate their children, and then call onFinishInflate().
     */
    private void rInflate(org.xmlpull.v1.XmlPullParser parser, android.support.v7.preference.Preference parent, final android.util.AttributeSet attrs) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int depth = parser.getDepth();
        int type;
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final java.lang.String name = parser.getName();
            if (android.support.v7.preference.PreferenceInflater.INTENT_TAG_NAME.equals(name)) {
                final android.content.Intent intent;
                try {
                    intent = android.content.Intent.parseIntent(getContext().getResources(), parser, attrs);
                } catch (java.io.IOException e) {
                    org.xmlpull.v1.XmlPullParserException ex = new org.xmlpull.v1.XmlPullParserException("Error parsing preference");
                    ex.initCause(e);
                    throw ex;
                }
                parent.setIntent(intent);
            } else
                if (android.support.v7.preference.PreferenceInflater.EXTRA_TAG_NAME.equals(name)) {
                    getContext().getResources().parseBundleExtra(android.support.v7.preference.PreferenceInflater.EXTRA_TAG_NAME, attrs, parent.getExtras());
                    try {
                        android.support.v7.preference.PreferenceInflater.skipCurrentTag(parser);
                    } catch (java.io.IOException e) {
                        org.xmlpull.v1.XmlPullParserException ex = new org.xmlpull.v1.XmlPullParserException("Error parsing preference");
                        ex.initCause(e);
                        throw ex;
                    }
                } else {
                    final android.support.v7.preference.Preference item = createItemFromTag(name, attrs);
                    ((android.support.v7.preference.PreferenceGroup) (parent)).addItemFromInflater(item);
                    rInflate(parser, item, attrs);
                }

        } 
    }

    private static void skipCurrentTag(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        do {
            type = parser.next();
        } while ((type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth)) );
    }
}

