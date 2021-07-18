/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.view;


/**
 * Delegate used to provide new implementation of a select few methods of {@link MenuInflater}
 * <p/>
 * Through the layoutlib_create tool, the original  methods of MenuInflater have been
 * replaced by calls to methods of the same name in this delegate class.
 * <p/>
 * The main purpose of the class is to get the view key from the menu xml parser and add it to
 * the menu item. The view key is used by the IDE to match the individual view elements to the
 * corresponding xml tag in the menu/layout file.
 * <p/>
 * For Menus, the views may be reused and the {@link MenuItem} is a better object to hold the
 * view key than the {@link MenuView.ItemView}. At the time of computation of the rest of {@link ViewInfo}, we check the corresponding view key in the menu item for the view and add it
 */
public class MenuInflater_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void registerMenu(android.view.MenuInflater thisInflater, android.view.MenuItem menuItem, android.util.AttributeSet attrs) {
        if (menuItem instanceof com.android.internal.view.menu.BridgeMenuItemImpl) {
            android.content.Context context = thisInflater.getContext();
            context = com.android.layoutlib.bridge.android.BridgeContext.getBaseContext(context);
            if (context instanceof com.android.layoutlib.bridge.android.BridgeContext) {
                java.lang.Object viewKey = android.view.BridgeInflater.getViewKeyFromParser(attrs, ((com.android.layoutlib.bridge.android.BridgeContext) (context)), null, false);
                ((com.android.internal.view.menu.BridgeMenuItemImpl) (menuItem)).setViewCookie(viewKey);
                return;
            }
        }
        java.lang.String menuItemName = (menuItem != null) ? menuItem.getClass().getName() : null;
        if (((menuItemName == null) || (!menuItemName.startsWith("android.support."))) || (!menuItemName.startsWith("androidx."))) {
            // This means that Bridge did not take over the instantiation of some object properly.
            // This is most likely a bug in the LayoutLib code.
            // We suppress this error for AppCompat menus since we do not support them in the menu
            // editor yet.
            com.android.layoutlib.bridge.Bridge.getLog().warning(LayoutLog.TAG_BROKEN, "Action Bar Menu rendering may be incorrect.", null);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void registerMenu(android.view.MenuInflater thisInflater, android.view.SubMenu subMenu, android.util.AttributeSet parser) {
        android.view.MenuInflater_Delegate.registerMenu(thisInflater, subMenu.getItem(), parser);
    }
}

