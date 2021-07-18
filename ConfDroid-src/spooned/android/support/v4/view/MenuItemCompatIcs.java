/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.view;


class MenuItemCompatIcs {
    public static boolean expandActionView(android.view.MenuItem item) {
        return item.expandActionView();
    }

    public static boolean collapseActionView(android.view.MenuItem item) {
        return item.collapseActionView();
    }

    public static boolean isActionViewExpanded(android.view.MenuItem item) {
        return item.isActionViewExpanded();
    }

    public static android.view.MenuItem setOnActionExpandListener(android.view.MenuItem item, android.support.v4.view.MenuItemCompatIcs.SupportActionExpandProxy listener) {
        return item.setOnActionExpandListener(new android.support.v4.view.MenuItemCompatIcs.OnActionExpandListenerWrapper(listener));
    }

    /**
     * Work around the support lib's build dependency chain. The actual API-lib
     * depends on -ics, but -ics doesn't depend on the API-lib so it doesn't know
     * that MenuItemCompat.OnActionExpandListener exists.
     */
    interface SupportActionExpandProxy {
        boolean onMenuItemActionExpand(android.view.MenuItem item);

        boolean onMenuItemActionCollapse(android.view.MenuItem item);
    }

    // support => framework
    static class OnActionExpandListenerWrapper implements android.view.MenuItem.OnActionExpandListener {
        private android.support.v4.view.MenuItemCompatIcs.SupportActionExpandProxy mWrapped;

        public OnActionExpandListenerWrapper(android.support.v4.view.MenuItemCompatIcs.SupportActionExpandProxy wrapped) {
            mWrapped = wrapped;
        }

        @java.lang.Override
        public boolean onMenuItemActionExpand(android.view.MenuItem item) {
            return mWrapped.onMenuItemActionExpand(item);
        }

        @java.lang.Override
        public boolean onMenuItemActionCollapse(android.view.MenuItem item) {
            return mWrapped.onMenuItemActionCollapse(item);
        }
    }
}

