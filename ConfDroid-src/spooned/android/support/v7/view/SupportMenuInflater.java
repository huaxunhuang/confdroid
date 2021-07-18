/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.view;


/**
 * This class is used to instantiate menu XML files into Menu objects.
 * <p>
 * For performance reasons, menu inflation relies heavily on pre-processing of
 * XML files that is done at build time. Therefore, it is not currently possible
 * to use SupportMenuInflater with an XmlPullParser over a plain XML file at runtime;
 * it only works with an XmlPullParser returned from a compiled resource (R.
 * <em>something</em> file.)
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class SupportMenuInflater extends android.view.MenuInflater {
    static final java.lang.String LOG_TAG = "SupportMenuInflater";

    /**
     * Menu tag name in XML.
     */
    private static final java.lang.String XML_MENU = "menu";

    /**
     * Group tag name in XML.
     */
    private static final java.lang.String XML_GROUP = "group";

    /**
     * Item tag name in XML.
     */
    private static final java.lang.String XML_ITEM = "item";

    static final int NO_ID = 0;

    static final java.lang.Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new java.lang.Class[]{ android.content.Context.class };

    static final java.lang.Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = android.support.v7.view.SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE;

    final java.lang.Object[] mActionViewConstructorArguments;

    final java.lang.Object[] mActionProviderConstructorArguments;

    android.content.Context mContext;

    private java.lang.Object mRealOwner;

    /**
     * Constructs a menu inflater.
     *
     * @see Activity#getMenuInflater()
     */
    public SupportMenuInflater(android.content.Context context) {
        super(context);
        mContext = context;
        mActionViewConstructorArguments = new java.lang.Object[]{ context };
        mActionProviderConstructorArguments = mActionViewConstructorArguments;
    }

    /**
     * Inflate a menu hierarchy from the specified XML resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param menuRes
     * 		Resource ID for an XML layout resource to load (e.g.,
     * 		<code>R.menu.main_activity</code>)
     * @param menu
     * 		The Menu to inflate into. The items and submenus will be
     * 		added to this Menu.
     */
    @java.lang.Override
    public void inflate(int menuRes, android.view.Menu menu) {
        // If we're not dealing with a SupportMenu instance, let super handle
        if (!(menu instanceof android.support.v4.internal.view.SupportMenu)) {
            super.inflate(menuRes, menu);
            return;
        }
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = mContext.getResources().getLayout(menuRes);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            parseMenu(parser, attrs, menu);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new android.view.InflateException("Error inflating menu XML", e);
        } catch (java.io.IOException e) {
            throw new android.view.InflateException("Error inflating menu XML", e);
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    /**
     * Called internally to fill the given menu. If a sub menu is seen, it will
     * call this recursively.
     */
    private void parseMenu(org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.view.Menu menu) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.support.v7.view.SupportMenuInflater.MenuState menuState = new android.support.v7.view.SupportMenuInflater.MenuState(menu);
        int eventType = parser.getEventType();
        java.lang.String tagName;
        boolean lookingForEndOfUnknownTag = false;
        java.lang.String unknownTagName = null;
        // This loop will skip to the menu start tag
        do {
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                tagName = parser.getName();
                if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_MENU)) {
                    // Go to next tag
                    eventType = parser.next();
                    break;
                }
                throw new java.lang.RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = parser.next();
        } while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT );
        boolean reachedEndOfMenu = false;
        while (!reachedEndOfMenu) {
            switch (eventType) {
                case org.xmlpull.v1.XmlPullParser.START_TAG :
                    if (lookingForEndOfUnknownTag) {
                        break;
                    }
                    tagName = parser.getName();
                    if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_GROUP)) {
                        menuState.readGroup(attrs);
                    } else
                        if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_ITEM)) {
                            menuState.readItem(attrs);
                        } else
                            if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_MENU)) {
                                // A menu start tag denotes a submenu for an item
                                android.view.SubMenu subMenu = menuState.addSubMenuItem();
                                // Parse the submenu into returned SubMenu
                                parseMenu(parser, attrs, subMenu);
                            } else {
                                lookingForEndOfUnknownTag = true;
                                unknownTagName = tagName;
                            }


                    break;
                case org.xmlpull.v1.XmlPullParser.END_TAG :
                    tagName = parser.getName();
                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
                        lookingForEndOfUnknownTag = false;
                        unknownTagName = null;
                    } else
                        if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_GROUP)) {
                            menuState.resetGroup();
                        } else
                            if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_ITEM)) {
                                // Add the item if it hasn't been added (if the item was
                                // a submenu, it would have been added already)
                                if (!menuState.hasAddedItem()) {
                                    if ((menuState.itemActionProvider != null) && menuState.itemActionProvider.hasSubMenu()) {
                                        menuState.addSubMenuItem();
                                    } else {
                                        menuState.addItem();
                                    }
                                }
                            } else
                                if (tagName.equals(android.support.v7.view.SupportMenuInflater.XML_MENU)) {
                                    reachedEndOfMenu = true;
                                }



                    break;
                case org.xmlpull.v1.XmlPullParser.END_DOCUMENT :
                    throw new java.lang.RuntimeException("Unexpected end of document");
            }
            eventType = parser.next();
        } 
    }

    java.lang.Object getRealOwner() {
        if (mRealOwner == null) {
            mRealOwner = findRealOwner(mContext);
        }
        return mRealOwner;
    }

    private java.lang.Object findRealOwner(java.lang.Object owner) {
        if (owner instanceof android.app.Activity) {
            return owner;
        }
        if (owner instanceof android.content.ContextWrapper) {
            return findRealOwner(((android.content.ContextWrapper) (owner)).getBaseContext());
        }
        return owner;
    }

    private static class InflatedOnMenuItemClickListener implements android.view.MenuItem.OnMenuItemClickListener {
        private static final java.lang.Class<?>[] PARAM_TYPES = new java.lang.Class[]{ android.view.MenuItem.class };

        private java.lang.Object mRealOwner;

        private java.lang.reflect.Method mMethod;

        public InflatedOnMenuItemClickListener(java.lang.Object realOwner, java.lang.String methodName) {
            mRealOwner = realOwner;
            java.lang.Class<?> c = realOwner.getClass();
            try {
                mMethod = c.getMethod(methodName, android.support.v7.view.SupportMenuInflater.InflatedOnMenuItemClickListener.PARAM_TYPES);
            } catch (java.lang.Exception e) {
                android.view.InflateException ex = new android.view.InflateException((("Couldn't resolve menu item onClick handler " + methodName) + " in class ") + c.getName());
                ex.initCause(e);
                throw ex;
            }
        }

        public boolean onMenuItemClick(android.view.MenuItem item) {
            try {
                if (mMethod.getReturnType() == java.lang.Boolean.TYPE) {
                    return ((java.lang.Boolean) (mMethod.invoke(mRealOwner, item)));
                } else {
                    mMethod.invoke(mRealOwner, item);
                    return true;
                }
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }

    /**
     * State for the current menu.
     * <p>
     * Groups can not be nested unless there is another menu (which will have
     * its state class).
     */
    private class MenuState {
        private android.view.Menu menu;

        /* Group state is set on items as they are added, allowing an item to
        override its group state. (As opposed to set on items at the group end tag.)
         */
        private int groupId;

        private int groupCategory;

        private int groupOrder;

        private int groupCheckable;

        private boolean groupVisible;

        private boolean groupEnabled;

        private boolean itemAdded;

        private int itemId;

        private int itemCategoryOrder;

        private java.lang.CharSequence itemTitle;

        private java.lang.CharSequence itemTitleCondensed;

        private int itemIconResId;

        private char itemAlphabeticShortcut;

        private char itemNumericShortcut;

        /**
         * Sync to attrs.xml enum:
         * - 0: none
         * - 1: all
         * - 2: exclusive
         */
        private int itemCheckable;

        private boolean itemChecked;

        private boolean itemVisible;

        private boolean itemEnabled;

        /**
         * Sync to attrs.xml enum, values in MenuItem:
         * - 0: never
         * - 1: ifRoom
         * - 2: always
         * - -1: Safe sentinel for "no value".
         */
        private int itemShowAsAction;

        private int itemActionViewLayout;

        private java.lang.String itemActionViewClassName;

        private java.lang.String itemActionProviderClassName;

        private java.lang.String itemListenerMethodName;

        android.support.v4.view.ActionProvider itemActionProvider;

        private static final int defaultGroupId = android.support.v7.view.SupportMenuInflater.NO_ID;

        private static final int defaultItemId = android.support.v7.view.SupportMenuInflater.NO_ID;

        private static final int defaultItemCategory = 0;

        private static final int defaultItemOrder = 0;

        private static final int defaultItemCheckable = 0;

        private static final boolean defaultItemChecked = false;

        private static final boolean defaultItemVisible = true;

        private static final boolean defaultItemEnabled = true;

        public MenuState(final android.view.Menu menu) {
            this.menu = menu;
            resetGroup();
        }

        public void resetGroup() {
            groupId = android.support.v7.view.SupportMenuInflater.MenuState.defaultGroupId;
            groupCategory = android.support.v7.view.SupportMenuInflater.MenuState.defaultItemCategory;
            groupOrder = android.support.v7.view.SupportMenuInflater.MenuState.defaultItemOrder;
            groupCheckable = android.support.v7.view.SupportMenuInflater.MenuState.defaultItemCheckable;
            groupVisible = android.support.v7.view.SupportMenuInflater.MenuState.defaultItemVisible;
            groupEnabled = android.support.v7.view.SupportMenuInflater.MenuState.defaultItemEnabled;
        }

        /**
         * Called when the parser is pointing to a group tag.
         */
        public void readGroup(android.util.AttributeSet attrs) {
            android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuGroup);
            groupId = a.getResourceId(R.styleable.MenuGroup_android_id, android.support.v7.view.SupportMenuInflater.MenuState.defaultGroupId);
            groupCategory = a.getInt(R.styleable.MenuGroup_android_menuCategory, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemCategory);
            groupOrder = a.getInt(R.styleable.MenuGroup_android_orderInCategory, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemOrder);
            groupCheckable = a.getInt(R.styleable.MenuGroup_android_checkableBehavior, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemCheckable);
            groupVisible = a.getBoolean(R.styleable.MenuGroup_android_visible, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemVisible);
            groupEnabled = a.getBoolean(R.styleable.MenuGroup_android_enabled, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemEnabled);
            a.recycle();
        }

        /**
         * Called when the parser is pointing to an item tag.
         */
        public void readItem(android.util.AttributeSet attrs) {
            android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItem);
            // Inherit attributes from the group as default value
            itemId = a.getResourceId(R.styleable.MenuItem_android_id, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemId);
            final int category = a.getInt(R.styleable.MenuItem_android_menuCategory, groupCategory);
            final int order = a.getInt(R.styleable.MenuItem_android_orderInCategory, groupOrder);
            itemCategoryOrder = (category & android.support.v4.internal.view.SupportMenu.CATEGORY_MASK) | (order & android.support.v4.internal.view.SupportMenu.USER_MASK);
            itemTitle = a.getText(R.styleable.MenuItem_android_title);
            itemTitleCondensed = a.getText(R.styleable.MenuItem_android_titleCondensed);
            itemIconResId = a.getResourceId(R.styleable.MenuItem_android_icon, 0);
            itemAlphabeticShortcut = getShortcut(a.getString(R.styleable.MenuItem_android_alphabeticShortcut));
            itemNumericShortcut = getShortcut(a.getString(R.styleable.MenuItem_android_numericShortcut));
            if (a.hasValue(R.styleable.MenuItem_android_checkable)) {
                // Item has attribute checkable, use it
                itemCheckable = (a.getBoolean(R.styleable.MenuItem_android_checkable, false)) ? 1 : 0;
            } else {
                // Item does not have attribute, use the group's (group can have one more state
                // for checkable that represents the exclusive checkable)
                itemCheckable = groupCheckable;
            }
            itemChecked = a.getBoolean(R.styleable.MenuItem_android_checked, android.support.v7.view.SupportMenuInflater.MenuState.defaultItemChecked);
            itemVisible = a.getBoolean(R.styleable.MenuItem_android_visible, groupVisible);
            itemEnabled = a.getBoolean(R.styleable.MenuItem_android_enabled, groupEnabled);
            itemShowAsAction = a.getInt(R.styleable.MenuItem_showAsAction, -1);
            itemListenerMethodName = a.getString(R.styleable.MenuItem_android_onClick);
            itemActionViewLayout = a.getResourceId(R.styleable.MenuItem_actionLayout, 0);
            itemActionViewClassName = a.getString(R.styleable.MenuItem_actionViewClass);
            itemActionProviderClassName = a.getString(R.styleable.MenuItem_actionProviderClass);
            final boolean hasActionProvider = itemActionProviderClassName != null;
            if ((hasActionProvider && (itemActionViewLayout == 0)) && (itemActionViewClassName == null)) {
                itemActionProvider = newInstance(itemActionProviderClassName, android.support.v7.view.SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, mActionProviderConstructorArguments);
            } else {
                if (hasActionProvider) {
                    android.util.Log.w(android.support.v7.view.SupportMenuInflater.LOG_TAG, "Ignoring attribute 'actionProviderClass'." + " Action view already specified.");
                }
                itemActionProvider = null;
            }
            a.recycle();
            itemAdded = false;
        }

        private char getShortcut(java.lang.String shortcutString) {
            if (shortcutString == null) {
                return 0;
            } else {
                return shortcutString.charAt(0);
            }
        }

        private void setItem(android.view.MenuItem item) {
            item.setChecked(itemChecked).setVisible(itemVisible).setEnabled(itemEnabled).setCheckable(itemCheckable >= 1).setTitleCondensed(itemTitleCondensed).setIcon(itemIconResId).setAlphabeticShortcut(itemAlphabeticShortcut).setNumericShortcut(itemNumericShortcut);
            if (itemShowAsAction >= 0) {
                android.support.v4.view.MenuItemCompat.setShowAsAction(item, itemShowAsAction);
            }
            if (itemListenerMethodName != null) {
                if (mContext.isRestricted()) {
                    throw new java.lang.IllegalStateException("The android:onClick attribute cannot " + "be used within a restricted context");
                }
                item.setOnMenuItemClickListener(new android.support.v7.view.SupportMenuInflater.InflatedOnMenuItemClickListener(getRealOwner(), itemListenerMethodName));
            }
            final android.support.v7.view.menu.MenuItemImpl impl = (item instanceof android.support.v7.view.menu.MenuItemImpl) ? ((android.support.v7.view.menu.MenuItemImpl) (item)) : null;
            if (itemCheckable >= 2) {
                if (item instanceof android.support.v7.view.menu.MenuItemImpl) {
                    ((android.support.v7.view.menu.MenuItemImpl) (item)).setExclusiveCheckable(true);
                } else
                    if (item instanceof android.support.v7.view.menu.MenuItemWrapperICS) {
                        ((android.support.v7.view.menu.MenuItemWrapperICS) (item)).setExclusiveCheckable(true);
                    }

            }
            boolean actionViewSpecified = false;
            if (itemActionViewClassName != null) {
                android.view.View actionView = ((android.view.View) (newInstance(itemActionViewClassName, android.support.v7.view.SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, mActionViewConstructorArguments)));
                android.support.v4.view.MenuItemCompat.setActionView(item, actionView);
                actionViewSpecified = true;
            }
            if (itemActionViewLayout > 0) {
                if (!actionViewSpecified) {
                    android.support.v4.view.MenuItemCompat.setActionView(item, itemActionViewLayout);
                    actionViewSpecified = true;
                } else {
                    android.util.Log.w(android.support.v7.view.SupportMenuInflater.LOG_TAG, "Ignoring attribute 'itemActionViewLayout'." + " Action view already specified.");
                }
            }
            if (itemActionProvider != null) {
                android.support.v4.view.MenuItemCompat.setActionProvider(item, itemActionProvider);
            }
        }

        public void addItem() {
            itemAdded = true;
            setItem(menu.add(groupId, itemId, itemCategoryOrder, itemTitle));
        }

        public android.view.SubMenu addSubMenuItem() {
            itemAdded = true;
            android.view.SubMenu subMenu = menu.addSubMenu(groupId, itemId, itemCategoryOrder, itemTitle);
            setItem(subMenu.getItem());
            return subMenu;
        }

        public boolean hasAddedItem() {
            return itemAdded;
        }

        @java.lang.SuppressWarnings("unchecked")
        private <T> T newInstance(java.lang.String className, java.lang.Class<?>[] constructorSignature, java.lang.Object[] arguments) {
            try {
                java.lang.Class<?> clazz = mContext.getClassLoader().loadClass(className);
                java.lang.reflect.Constructor<?> constructor = clazz.getConstructor(constructorSignature);
                constructor.setAccessible(true);
                return ((T) (constructor.newInstance(arguments)));
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.support.v7.view.SupportMenuInflater.LOG_TAG, "Cannot instantiate class: " + className, e);
            }
            return null;
        }
    }
}

