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
public final class BottomNavigationMenu extends android.support.v7.view.menu.MenuBuilder {
    public static final int MAX_ITEM_COUNT = 5;

    public BottomNavigationMenu(android.content.Context context) {
        super(context);
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(int group, int id, int categoryOrder, java.lang.CharSequence title) {
        throw new java.lang.UnsupportedOperationException("BottomNavigationView does not support submenus");
    }

    @java.lang.Override
    protected android.view.MenuItem addInternal(int group, int id, int categoryOrder, java.lang.CharSequence title) {
        if ((size() + 1) > android.support.design.internal.BottomNavigationMenu.MAX_ITEM_COUNT) {
            throw new java.lang.IllegalArgumentException(("Maximum number of items supported by BottomNavigationView is " + android.support.design.internal.BottomNavigationMenu.MAX_ITEM_COUNT) + ". Limit can be checked with BottomNavigationView#getMaxItemCount()");
        }
        stopDispatchingItemsChanged();
        final android.view.MenuItem item = super.addInternal(group, id, categoryOrder, title);
        if (item instanceof android.support.v7.view.menu.MenuItemImpl) {
            ((android.support.v7.view.menu.MenuItemImpl) (item)).setExclusiveCheckable(true);
        }
        startDispatchingItemsChanged();
        return item;
    }
}

