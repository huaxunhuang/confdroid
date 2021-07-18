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
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class MenuWrapperFactory {
    private MenuWrapperFactory() {
    }

    public static android.view.Menu wrapSupportMenu(android.content.Context context, android.support.v4.internal.view.SupportMenu supportMenu) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new android.support.v7.view.menu.MenuWrapperICS(context, supportMenu);
        }
        throw new java.lang.UnsupportedOperationException();
    }

    public static android.view.MenuItem wrapSupportMenuItem(android.content.Context context, android.support.v4.internal.view.SupportMenuItem supportMenuItem) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return new android.support.v7.view.menu.MenuItemWrapperJB(context, supportMenuItem);
        } else
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                return new android.support.v7.view.menu.MenuItemWrapperICS(context, supportMenuItem);
            }

        throw new java.lang.UnsupportedOperationException();
    }

    public static android.view.SubMenu wrapSupportSubMenu(android.content.Context context, android.support.v4.internal.view.SupportSubMenu supportSubMenu) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new android.support.v7.view.menu.SubMenuWrapperICS(context, supportSubMenu);
        }
        throw new java.lang.UnsupportedOperationException();
    }
}

