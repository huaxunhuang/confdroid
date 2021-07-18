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


/**
 * Wraps a support {@link SupportSubMenu} as a framework {@link android.view.SubMenu}
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
class SubMenuWrapperICS extends android.support.v7.view.menu.MenuWrapperICS implements android.view.SubMenu {
    SubMenuWrapperICS(android.content.Context context, android.support.v4.internal.view.SupportSubMenu subMenu) {
        super(context, subMenu);
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportSubMenu getWrappedObject() {
        return ((android.support.v4.internal.view.SupportSubMenu) (mWrappedObject));
    }

    @java.lang.Override
    public android.view.SubMenu setHeaderTitle(int titleRes) {
        getWrappedObject().setHeaderTitle(titleRes);
        return this;
    }

    @java.lang.Override
    public android.view.SubMenu setHeaderTitle(java.lang.CharSequence title) {
        getWrappedObject().setHeaderTitle(title);
        return this;
    }

    @java.lang.Override
    public android.view.SubMenu setHeaderIcon(int iconRes) {
        getWrappedObject().setHeaderIcon(iconRes);
        return this;
    }

    @java.lang.Override
    public android.view.SubMenu setHeaderIcon(android.graphics.drawable.Drawable icon) {
        getWrappedObject().setHeaderIcon(icon);
        return this;
    }

    @java.lang.Override
    public android.view.SubMenu setHeaderView(android.view.View view) {
        getWrappedObject().setHeaderView(view);
        return this;
    }

    @java.lang.Override
    public void clearHeader() {
        getWrappedObject().clearHeader();
    }

    @java.lang.Override
    public android.view.SubMenu setIcon(int iconRes) {
        getWrappedObject().setIcon(iconRes);
        return this;
    }

    @java.lang.Override
    public android.view.SubMenu setIcon(android.graphics.drawable.Drawable icon) {
        getWrappedObject().setIcon(icon);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem getItem() {
        return getMenuItemWrapper(getWrappedObject().getItem());
    }
}

