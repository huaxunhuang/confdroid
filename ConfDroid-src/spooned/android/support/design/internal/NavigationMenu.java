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
 * limitations under the License.
 */
package android.support.design.internal;


/**
 * This is a {@link MenuBuilder} that returns an instance of {@link NavigationSubMenu} instead of
 * {@link SubMenuBuilder} when a sub menu is created.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NavigationMenu extends android.support.v7.view.menu.MenuBuilder {
    public NavigationMenu(android.content.Context context) {
        super(context);
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(int group, int id, int categoryOrder, java.lang.CharSequence title) {
        final android.support.v7.view.menu.MenuItemImpl item = ((android.support.v7.view.menu.MenuItemImpl) (addInternal(group, id, categoryOrder, title)));
        final android.support.v7.view.menu.SubMenuBuilder subMenu = new android.support.design.internal.NavigationSubMenu(getContext(), this, item);
        item.setSubMenu(subMenu);
        return subMenu;
    }
}

