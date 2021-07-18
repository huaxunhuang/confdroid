/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.support.v7.view.menu;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class MenuItemImpl implements android.support.v4.internal.view.SupportMenuItem {
    private static final java.lang.String TAG = "MenuItemImpl";

    private static final int SHOW_AS_ACTION_MASK = (android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_NEVER | android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_IF_ROOM) | android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_ALWAYS;

    private final int mId;

    private final int mGroup;

    private final int mCategoryOrder;

    private final int mOrdering;

    private java.lang.CharSequence mTitle;

    private java.lang.CharSequence mTitleCondensed;

    private android.content.Intent mIntent;

    private char mShortcutNumericChar;

    private char mShortcutAlphabeticChar;

    /**
     * The icon's drawable which is only created as needed
     */
    private android.graphics.drawable.Drawable mIconDrawable;

    /**
     * The icon's resource ID which is used to get the Drawable when it is
     * needed (if the Drawable isn't already obtained--only one of the two is
     * needed).
     */
    private int mIconResId = android.support.v7.view.menu.MenuItemImpl.NO_ICON;

    /**
     * The menu to which this item belongs
     */
    android.support.v7.view.menu.MenuBuilder mMenu;

    /**
     * If this item should launch a sub menu, this is the sub menu to launch
     */
    private android.support.v7.view.menu.SubMenuBuilder mSubMenu;

    private java.lang.Runnable mItemCallback;

    private android.view.MenuItem.OnMenuItemClickListener mClickListener;

    private int mFlags = android.support.v7.view.menu.MenuItemImpl.ENABLED;

    private static final int CHECKABLE = 0x1;

    private static final int CHECKED = 0x2;

    private static final int EXCLUSIVE = 0x4;

    private static final int HIDDEN = 0x8;

    private static final int ENABLED = 0x10;

    private static final int IS_ACTION = 0x20;

    private int mShowAsAction = android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_NEVER;

    private android.view.View mActionView;

    private android.support.v4.view.ActionProvider mActionProvider;

    private android.support.v4.view.MenuItemCompat.OnActionExpandListener mOnActionExpandListener;

    private boolean mIsActionViewExpanded = false;

    /**
     * Used for the icon resource ID if this item does not have an icon
     */
    static final int NO_ICON = 0;

    /**
     * Current use case is for context menu: Extra information linked to the
     * View that added this item to the context menu.
     */
    private android.view.ContextMenu.ContextMenuInfo mMenuInfo;

    private static java.lang.String sPrependShortcutLabel;

    private static java.lang.String sEnterShortcutLabel;

    private static java.lang.String sDeleteShortcutLabel;

    private static java.lang.String sSpaceShortcutLabel;

    /**
     * Instantiates this menu item.
     *
     * @param menu
     * 		
     * @param group
     * 		Item ordering grouping control. The item will be added after
     * 		all other items whose order is <= this number, and before any
     * 		that are larger than it. This can also be used to define
     * 		groups of items for batch state changes. Normally use 0.
     * @param id
     * 		Unique item ID. Use 0 if you do not need a unique ID.
     * @param categoryOrder
     * 		The ordering for this item.
     * @param title
     * 		The text to display for the item.
     */
    MenuItemImpl(android.support.v7.view.menu.MenuBuilder menu, int group, int id, int categoryOrder, int ordering, java.lang.CharSequence title, int showAsAction) {
        /* if (sPrependShortcutLabel == null) {
        // This is instantiated from the UI thread, so no chance of sync issues
        sPrependShortcutLabel = menu.getContext().getResources().getString(
        com.android.internal.R.string.prepend_shortcut_label);
        sEnterShortcutLabel = menu.getContext().getResources().getString(
        com.android.internal.R.string.menu_enter_shortcut_label);
        sDeleteShortcutLabel = menu.getContext().getResources().getString(
        com.android.internal.R.string.menu_delete_shortcut_label);
        sSpaceShortcutLabel = menu.getContext().getResources().getString(
        com.android.internal.R.string.menu_space_shortcut_label);
        }
         */
        mMenu = menu;
        mId = id;
        mGroup = group;
        mCategoryOrder = categoryOrder;
        mOrdering = ordering;
        mTitle = title;
        mShowAsAction = showAsAction;
    }

    /**
     * Invokes the item by calling various listeners or callbacks.
     *
     * @return true if the invocation was handled, false otherwise
     */
    public boolean invoke() {
        if ((mClickListener != null) && mClickListener.onMenuItemClick(this)) {
            return true;
        }
        if (mMenu.dispatchMenuItemSelected(mMenu.getRootMenu(), this)) {
            return true;
        }
        if (mItemCallback != null) {
            mItemCallback.run();
            return true;
        }
        if (mIntent != null) {
            try {
                mMenu.getContext().startActivity(mIntent);
                return true;
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Log.e(android.support.v7.view.menu.MenuItemImpl.TAG, "Can't find activity to handle intent; ignoring", e);
            }
        }
        if ((mActionProvider != null) && mActionProvider.onPerformDefaultAction()) {
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.ENABLED) != 0;
    }

    @java.lang.Override
    public android.view.MenuItem setEnabled(boolean enabled) {
        if (enabled) {
            mFlags |= android.support.v7.view.menu.MenuItemImpl.ENABLED;
        } else {
            mFlags &= ~android.support.v7.view.menu.MenuItemImpl.ENABLED;
        }
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public int getGroupId() {
        return mGroup;
    }

    @java.lang.Override
    @android.view.ViewDebug.CapturedViewProperty
    public int getItemId() {
        return mId;
    }

    @java.lang.Override
    public int getOrder() {
        return mCategoryOrder;
    }

    public int getOrdering() {
        return mOrdering;
    }

    @java.lang.Override
    public android.content.Intent getIntent() {
        return mIntent;
    }

    @java.lang.Override
    public android.view.MenuItem setIntent(android.content.Intent intent) {
        mIntent = intent;
        return this;
    }

    java.lang.Runnable getCallback() {
        return mItemCallback;
    }

    public android.view.MenuItem setCallback(java.lang.Runnable callback) {
        mItemCallback = callback;
        return this;
    }

    @java.lang.Override
    public char getAlphabeticShortcut() {
        return mShortcutAlphabeticChar;
    }

    @java.lang.Override
    public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
        if (mShortcutAlphabeticChar == alphaChar) {
            return this;
        }
        mShortcutAlphabeticChar = java.lang.Character.toLowerCase(alphaChar);
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public char getNumericShortcut() {
        return mShortcutNumericChar;
    }

    @java.lang.Override
    public android.view.MenuItem setNumericShortcut(char numericChar) {
        if (mShortcutNumericChar == numericChar) {
            return this;
        }
        mShortcutNumericChar = numericChar;
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
        mShortcutNumericChar = numericChar;
        mShortcutAlphabeticChar = java.lang.Character.toLowerCase(alphaChar);
        mMenu.onItemsChanged(false);
        return this;
    }

    /**
     *
     *
     * @return The active shortcut (based on QWERTY-mode of the menu).
     */
    char getShortcut() {
        return mMenu.isQwertyMode() ? mShortcutAlphabeticChar : mShortcutNumericChar;
    }

    /**
     *
     *
     * @return The label to show for the shortcut. This includes the chording key (for example
    'Menu+a'). Also, any non-human readable characters should be human readable (for
    example 'Menu+enter').
     */
    java.lang.String getShortcutLabel() {
        char shortcut = getShortcut();
        if (shortcut == 0) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(android.support.v7.view.menu.MenuItemImpl.sPrependShortcutLabel);
        switch (shortcut) {
            case '\n' :
                sb.append(android.support.v7.view.menu.MenuItemImpl.sEnterShortcutLabel);
                break;
            case '\b' :
                sb.append(android.support.v7.view.menu.MenuItemImpl.sDeleteShortcutLabel);
                break;
            case ' ' :
                sb.append(android.support.v7.view.menu.MenuItemImpl.sSpaceShortcutLabel);
                break;
            default :
                sb.append(shortcut);
                break;
        }
        return sb.toString();
    }

    /**
     *
     *
     * @return Whether this menu item should be showing shortcuts (depends on
    whether the menu should show shortcuts and whether this item has
    a shortcut defined)
     */
    boolean shouldShowShortcut() {
        // Show shortcuts if the menu is supposed to show shortcuts AND this item has a shortcut
        return mMenu.isShortcutsVisible() && (getShortcut() != 0);
    }

    @java.lang.Override
    public android.view.SubMenu getSubMenu() {
        return mSubMenu;
    }

    @java.lang.Override
    public boolean hasSubMenu() {
        return mSubMenu != null;
    }

    public void setSubMenu(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        mSubMenu = subMenu;
        subMenu.setHeaderTitle(getTitle());
    }

    @java.lang.Override
    @android.view.ViewDebug.CapturedViewProperty
    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Gets the title for a particular {@link MenuView.ItemView}
     *
     * @param itemView
     * 		The ItemView that is receiving the title
     * @return Either the title or condensed title based on what the ItemView prefers
     */
    java.lang.CharSequence getTitleForItemView(android.support.v7.view.menu.MenuView.ItemView itemView) {
        return (itemView != null) && itemView.prefersCondensedTitle() ? getTitleCondensed() : getTitle();
    }

    @java.lang.Override
    public android.view.MenuItem setTitle(java.lang.CharSequence title) {
        mTitle = title;
        mMenu.onItemsChanged(false);
        if (mSubMenu != null) {
            mSubMenu.setHeaderTitle(title);
        }
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setTitle(int title) {
        return setTitle(mMenu.getContext().getString(title));
    }

    @java.lang.Override
    public java.lang.CharSequence getTitleCondensed() {
        final java.lang.CharSequence ctitle = (mTitleCondensed != null) ? mTitleCondensed : mTitle;
        if (((android.os.Build.VERSION.SDK_INT < 18) && (ctitle != null)) && (!(ctitle instanceof java.lang.String))) {
            // For devices pre-JB-MR2, where we have a non-String CharSequence, we need to
            // convert this to a String so that EventLog.writeEvent() does not throw an exception
            // in Activity.onMenuItemSelected()
            return ctitle.toString();
        } else {
            // Else, we just return the condensed title
            return ctitle;
        }
    }

    @java.lang.Override
    public android.view.MenuItem setTitleCondensed(java.lang.CharSequence title) {
        mTitleCondensed = title;
        // Could use getTitle() in the loop below, but just cache what it would do here
        if (title == null) {
            title = mTitle;
        }
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getIcon() {
        if (mIconDrawable != null) {
            return mIconDrawable;
        }
        if (mIconResId != android.support.v7.view.menu.MenuItemImpl.NO_ICON) {
            android.graphics.drawable.Drawable icon = android.support.v7.content.res.AppCompatResources.getDrawable(mMenu.getContext(), mIconResId);
            mIconResId = android.support.v7.view.menu.MenuItemImpl.NO_ICON;
            mIconDrawable = icon;
            return icon;
        }
        return null;
    }

    @java.lang.Override
    public android.view.MenuItem setIcon(android.graphics.drawable.Drawable icon) {
        mIconResId = android.support.v7.view.menu.MenuItemImpl.NO_ICON;
        mIconDrawable = icon;
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setIcon(int iconResId) {
        mIconDrawable = null;
        mIconResId = iconResId;
        // If we have a view, we need to push the Drawable to them
        mMenu.onItemsChanged(false);
        return this;
    }

    @java.lang.Override
    public boolean isCheckable() {
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.CHECKABLE) == android.support.v7.view.menu.MenuItemImpl.CHECKABLE;
    }

    @java.lang.Override
    public android.view.MenuItem setCheckable(boolean checkable) {
        final int oldFlags = mFlags;
        mFlags = (mFlags & (~android.support.v7.view.menu.MenuItemImpl.CHECKABLE)) | (checkable ? android.support.v7.view.menu.MenuItemImpl.CHECKABLE : 0);
        if (oldFlags != mFlags) {
            mMenu.onItemsChanged(false);
        }
        return this;
    }

    public void setExclusiveCheckable(boolean exclusive) {
        mFlags = (mFlags & (~android.support.v7.view.menu.MenuItemImpl.EXCLUSIVE)) | (exclusive ? android.support.v7.view.menu.MenuItemImpl.EXCLUSIVE : 0);
    }

    public boolean isExclusiveCheckable() {
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.EXCLUSIVE) != 0;
    }

    @java.lang.Override
    public boolean isChecked() {
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.CHECKED) == android.support.v7.view.menu.MenuItemImpl.CHECKED;
    }

    @java.lang.Override
    public android.view.MenuItem setChecked(boolean checked) {
        if ((mFlags & android.support.v7.view.menu.MenuItemImpl.EXCLUSIVE) != 0) {
            // Call the method on the Menu since it knows about the others in this
            // exclusive checkable group
            mMenu.setExclusiveItemChecked(this);
        } else {
            setCheckedInt(checked);
        }
        return this;
    }

    void setCheckedInt(boolean checked) {
        final int oldFlags = mFlags;
        mFlags = (mFlags & (~android.support.v7.view.menu.MenuItemImpl.CHECKED)) | (checked ? android.support.v7.view.menu.MenuItemImpl.CHECKED : 0);
        if (oldFlags != mFlags) {
            mMenu.onItemsChanged(false);
        }
    }

    @java.lang.Override
    public boolean isVisible() {
        if ((mActionProvider != null) && mActionProvider.overridesItemVisibility()) {
            return ((mFlags & android.support.v7.view.menu.MenuItemImpl.HIDDEN) == 0) && mActionProvider.isVisible();
        }
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.HIDDEN) == 0;
    }

    /**
     * Changes the visibility of the item. This method DOES NOT notify the parent menu of a change
     * in this item, so this should only be called from methods that will eventually trigger this
     * change.  If unsure, use {@link #setVisible(boolean)} instead.
     *
     * @param shown
     * 		Whether to show (true) or hide (false).
     * @return Whether the item's shown state was changed
     */
    boolean setVisibleInt(boolean shown) {
        final int oldFlags = mFlags;
        mFlags = (mFlags & (~android.support.v7.view.menu.MenuItemImpl.HIDDEN)) | (shown ? 0 : android.support.v7.view.menu.MenuItemImpl.HIDDEN);
        return oldFlags != mFlags;
    }

    @java.lang.Override
    public android.view.MenuItem setVisible(boolean shown) {
        // Try to set the shown state to the given state. If the shown state was changed
        // (i.e. the previous state isn't the same as given state), notify the parent menu that
        // the shown state has changed for this item
        if (setVisibleInt(shown))
            mMenu.onItemVisibleChanged(this);

        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener clickListener) {
        mClickListener = clickListener;
        return this;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mTitle != null ? mTitle.toString() : null;
    }

    void setMenuInfo(android.view.ContextMenu.ContextMenuInfo menuInfo) {
        mMenuInfo = menuInfo;
    }

    @java.lang.Override
    public android.view.ContextMenu.ContextMenuInfo getMenuInfo() {
        return mMenuInfo;
    }

    public void actionFormatChanged() {
        mMenu.onItemActionRequestChanged(this);
    }

    /**
     *
     *
     * @return Whether the menu should show icons for menu items.
     */
    public boolean shouldShowIcon() {
        return mMenu.getOptionalIconsVisible();
    }

    public boolean isActionButton() {
        return (mFlags & android.support.v7.view.menu.MenuItemImpl.IS_ACTION) == android.support.v7.view.menu.MenuItemImpl.IS_ACTION;
    }

    public boolean requestsActionButton() {
        return (mShowAsAction & android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_IF_ROOM) == android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_IF_ROOM;
    }

    public boolean requiresActionButton() {
        return (mShowAsAction & android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_ALWAYS) == android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_ALWAYS;
    }

    public void setIsActionButton(boolean isActionButton) {
        if (isActionButton) {
            mFlags |= android.support.v7.view.menu.MenuItemImpl.IS_ACTION;
        } else {
            mFlags &= ~android.support.v7.view.menu.MenuItemImpl.IS_ACTION;
        }
    }

    public boolean showsTextAsAction() {
        return (mShowAsAction & android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_WITH_TEXT) == android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_WITH_TEXT;
    }

    @java.lang.Override
    public void setShowAsAction(int actionEnum) {
        switch (actionEnum & android.support.v7.view.menu.MenuItemImpl.SHOW_AS_ACTION_MASK) {
            case android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_ALWAYS :
            case android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_IF_ROOM :
            case android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_NEVER :
                // Looks good!
                break;
            default :
                // Mutually exclusive options selected!
                throw new java.lang.IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM," + " and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        mShowAsAction = actionEnum;
        mMenu.onItemActionRequestChanged(this);
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setActionView(android.view.View view) {
        mActionView = view;
        mActionProvider = null;
        if (((view != null) && (view.getId() == android.view.View.NO_ID)) && (mId > 0)) {
            view.setId(mId);
        }
        mMenu.onItemActionRequestChanged(this);
        return this;
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setActionView(int resId) {
        final android.content.Context context = mMenu.getContext();
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        setActionView(inflater.inflate(resId, new android.widget.LinearLayout(context), false));
        return this;
    }

    @java.lang.Override
    public android.view.View getActionView() {
        if (mActionView != null) {
            return mActionView;
        } else
            if (mActionProvider != null) {
                mActionView = mActionProvider.onCreateActionView(this);
                return mActionView;
            } else {
                return null;
            }

    }

    @java.lang.Override
    public android.view.MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new java.lang.UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }

    @java.lang.Override
    public android.view.ActionProvider getActionProvider() {
        throw new java.lang.UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }

    @java.lang.Override
    public android.support.v4.view.ActionProvider getSupportActionProvider() {
        return mActionProvider;
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setSupportActionProvider(android.support.v4.view.ActionProvider actionProvider) {
        if (mActionProvider != null) {
            mActionProvider.reset();
        }
        mActionView = null;
        mActionProvider = actionProvider;
        mMenu.onItemsChanged(true);// Measurement can be changed

        if (mActionProvider != null) {
            mActionProvider.setVisibilityListener(new android.support.v4.view.ActionProvider.VisibilityListener() {
                @java.lang.Override
                public void onActionProviderVisibilityChanged(boolean isVisible) {
                    mMenu.onItemVisibleChanged(android.support.v7.view.menu.MenuItemImpl.this);
                }
            });
        }
        return this;
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setShowAsActionFlags(int actionEnum) {
        setShowAsAction(actionEnum);
        return this;
    }

    @java.lang.Override
    public boolean expandActionView() {
        if (!hasCollapsibleActionView()) {
            return false;
        }
        if ((mOnActionExpandListener == null) || mOnActionExpandListener.onMenuItemActionExpand(this)) {
            return mMenu.expandItemActionView(this);
        }
        return false;
    }

    @java.lang.Override
    public boolean collapseActionView() {
        if ((mShowAsAction & android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW) == 0) {
            return false;
        }
        if (mActionView == null) {
            // We're already collapsed if we have no action view.
            return true;
        }
        if ((mOnActionExpandListener == null) || mOnActionExpandListener.onMenuItemActionCollapse(this)) {
            return mMenu.collapseItemActionView(this);
        }
        return false;
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setSupportOnActionExpandListener(android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
        mOnActionExpandListener = listener;
        return this;
    }

    public boolean hasCollapsibleActionView() {
        if ((mShowAsAction & android.support.v4.internal.view.SupportMenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW) != 0) {
            if ((mActionView == null) && (mActionProvider != null)) {
                mActionView = mActionProvider.onCreateActionView(this);
            }
            return mActionView != null;
        }
        return false;
    }

    public void setActionViewExpanded(boolean isExpanded) {
        mIsActionViewExpanded = isExpanded;
        mMenu.onItemsChanged(false);
    }

    @java.lang.Override
    public boolean isActionViewExpanded() {
        return mIsActionViewExpanded;
    }

    @java.lang.Override
    public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem.OnActionExpandListener listener) {
        throw new java.lang.UnsupportedOperationException("This is not supported, use MenuItemCompat.setOnActionExpandListener()");
    }
}

