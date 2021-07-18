/**
 * Copyright (C) 2012 The Android Open Source Project
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


abstract class BaseMenuWrapper<T> extends android.support.v7.view.menu.BaseWrapper<T> {
    final android.content.Context mContext;

    private java.util.Map<android.support.v4.internal.view.SupportMenuItem, android.view.MenuItem> mMenuItems;

    private java.util.Map<android.support.v4.internal.view.SupportSubMenu, android.view.SubMenu> mSubMenus;

    BaseMenuWrapper(android.content.Context context, T object) {
        super(object);
        mContext = context;
    }

    final android.view.MenuItem getMenuItemWrapper(final android.view.MenuItem menuItem) {
        if (menuItem instanceof android.support.v4.internal.view.SupportMenuItem) {
            final android.support.v4.internal.view.SupportMenuItem supportMenuItem = ((android.support.v4.internal.view.SupportMenuItem) (menuItem));
            // Instantiate Map if null
            if (mMenuItems == null) {
                mMenuItems = new android.support.v4.util.ArrayMap<>();
            }
            // First check if we already have a wrapper for this item
            android.view.MenuItem wrappedItem = mMenuItems.get(menuItem);
            if (null == wrappedItem) {
                // ... if not, create one and add it to our map
                wrappedItem = android.support.v7.view.menu.MenuWrapperFactory.wrapSupportMenuItem(mContext, supportMenuItem);
                mMenuItems.put(supportMenuItem, wrappedItem);
            }
            return wrappedItem;
        }
        return menuItem;
    }

    final android.view.SubMenu getSubMenuWrapper(final android.view.SubMenu subMenu) {
        if (subMenu instanceof android.support.v4.internal.view.SupportSubMenu) {
            final android.support.v4.internal.view.SupportSubMenu supportSubMenu = ((android.support.v4.internal.view.SupportSubMenu) (subMenu));
            // Instantiate Map if null
            if (mSubMenus == null) {
                mSubMenus = new android.support.v4.util.ArrayMap<>();
            }
            android.view.SubMenu wrappedMenu = mSubMenus.get(supportSubMenu);
            if (null == wrappedMenu) {
                wrappedMenu = android.support.v7.view.menu.MenuWrapperFactory.wrapSupportSubMenu(mContext, supportSubMenu);
                mSubMenus.put(supportSubMenu, wrappedMenu);
            }
            return wrappedMenu;
        }
        return subMenu;
    }

    final void internalClear() {
        if (mMenuItems != null) {
            mMenuItems.clear();
        }
        if (mSubMenus != null) {
            mSubMenus.clear();
        }
    }

    final void internalRemoveGroup(final int groupId) {
        if (mMenuItems == null) {
            return;
        }
        java.util.Iterator<android.support.v4.internal.view.SupportMenuItem> iterator = mMenuItems.keySet().iterator();
        android.view.MenuItem menuItem;
        while (iterator.hasNext()) {
            menuItem = iterator.next();
            if (groupId == menuItem.getGroupId()) {
                iterator.remove();
            }
        } 
    }

    final void internalRemoveItem(final int id) {
        if (mMenuItems == null) {
            return;
        }
        java.util.Iterator<android.support.v4.internal.view.SupportMenuItem> iterator = mMenuItems.keySet().iterator();
        android.view.MenuItem menuItem;
        while (iterator.hasNext()) {
            menuItem = iterator.next();
            if (id == menuItem.getItemId()) {
                iterator.remove();
                break;
            }
        } 
    }
}

