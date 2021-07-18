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
 * This is a {@link SubMenuBuilder} that it notifies the parent {@link NavigationMenu} of its menu
 * updates.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NavigationSubMenu extends android.support.v7.view.menu.SubMenuBuilder {
    public NavigationSubMenu(android.content.Context context, android.support.design.internal.NavigationMenu menu, android.support.v7.view.menu.MenuItemImpl item) {
        super(context, menu, item);
    }

    @java.lang.Override
    public void onItemsChanged(boolean structureChanged) {
        super.onItemsChanged(structureChanged);
        ((android.support.v7.view.menu.MenuBuilder) (getParentMenu())).onItemsChanged(structureChanged);
    }
}

