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
 * The model for a sub menu, which is an extension of the menu.  Most methods are proxied to the
 * parent menu.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class SubMenuBuilder extends android.support.v7.view.menu.MenuBuilder implements android.view.SubMenu {
    private android.support.v7.view.menu.MenuBuilder mParentMenu;

    private android.support.v7.view.menu.MenuItemImpl mItem;

    public SubMenuBuilder(android.content.Context context, android.support.v7.view.menu.MenuBuilder parentMenu, android.support.v7.view.menu.MenuItemImpl item) {
        super(context);
        mParentMenu = parentMenu;
        mItem = item;
    }

    @java.lang.Override
    public void setQwertyMode(boolean isQwerty) {
        mParentMenu.setQwertyMode(isQwerty);
    }

    @java.lang.Override
    public boolean isQwertyMode() {
        return mParentMenu.isQwertyMode();
    }

    @java.lang.Override
    public void setShortcutsVisible(boolean shortcutsVisible) {
        mParentMenu.setShortcutsVisible(shortcutsVisible);
    }

    @java.lang.Override
    public boolean isShortcutsVisible() {
        return mParentMenu.isShortcutsVisible();
    }

    public android.view.Menu getParentMenu() {
        return mParentMenu;
    }

    public android.view.MenuItem getItem() {
        return mItem;
    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuBuilder.Callback callback) {
        mParentMenu.setCallback(callback);
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuBuilder getRootMenu() {
        return mParentMenu.getRootMenu();
    }

    @java.lang.Override
    boolean dispatchMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
        return super.dispatchMenuItemSelected(menu, item) || mParentMenu.dispatchMenuItemSelected(menu, item);
    }

    public android.view.SubMenu setIcon(android.graphics.drawable.Drawable icon) {
        mItem.setIcon(icon);
        return this;
    }

    public android.view.SubMenu setIcon(int iconRes) {
        mItem.setIcon(iconRes);
        return this;
    }

    public android.view.SubMenu setHeaderIcon(android.graphics.drawable.Drawable icon) {
        return ((android.view.SubMenu) (super.setHeaderIconInt(icon)));
    }

    public android.view.SubMenu setHeaderIcon(int iconRes) {
        return ((android.view.SubMenu) (super.setHeaderIconInt(iconRes)));
    }

    public android.view.SubMenu setHeaderTitle(java.lang.CharSequence title) {
        return ((android.view.SubMenu) (super.setHeaderTitleInt(title)));
    }

    public android.view.SubMenu setHeaderTitle(int titleRes) {
        return ((android.view.SubMenu) (super.setHeaderTitleInt(titleRes)));
    }

    public android.view.SubMenu setHeaderView(android.view.View view) {
        return ((android.view.SubMenu) (super.setHeaderViewInt(view)));
    }

    @java.lang.Override
    public boolean expandItemActionView(android.support.v7.view.menu.MenuItemImpl item) {
        return mParentMenu.expandItemActionView(item);
    }

    @java.lang.Override
    public boolean collapseItemActionView(android.support.v7.view.menu.MenuItemImpl item) {
        return mParentMenu.collapseItemActionView(item);
    }

    @java.lang.Override
    public java.lang.String getActionViewStatesKey() {
        final int itemId = (mItem != null) ? mItem.getItemId() : 0;
        if (itemId == 0) {
            return null;
        }
        return (super.getActionViewStatesKey() + ":") + itemId;
    }
}

