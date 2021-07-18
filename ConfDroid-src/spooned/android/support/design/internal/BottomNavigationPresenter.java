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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class BottomNavigationPresenter implements android.support.v7.view.menu.MenuPresenter {
    private android.support.v7.view.menu.MenuBuilder mMenu;

    private android.support.design.internal.BottomNavigationMenuView mMenuView;

    private boolean mUpdateSuspended = false;

    public void setBottomNavigationMenuView(android.support.design.internal.BottomNavigationMenuView menuView) {
        mMenuView = menuView;
    }

    @java.lang.Override
    public void initForMenu(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu) {
        mMenuView.initialize(mMenu);
        mMenu = menu;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        return mMenuView;
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        if (mUpdateSuspended)
            return;

        if (cleared) {
            mMenuView.buildMenuView();
        } else {
            mMenuView.updateMenuView();
        }
    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
    }

    @java.lang.Override
    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        return false;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
    }

    @java.lang.Override
    public boolean flagActionItems() {
        return false;
    }

    @java.lang.Override
    public boolean expandItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    @java.lang.Override
    public boolean collapseItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    @java.lang.Override
    public int getId() {
        return -1;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        return null;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
    }

    public void setUpdateSuspended(boolean updateSuspended) {
        mUpdateSuspended = updateSuspended;
    }
}

