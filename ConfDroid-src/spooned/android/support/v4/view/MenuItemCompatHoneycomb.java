/**
 * Copyright (C) 2011 The Android Open Source Project
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


/**
 * Implementation of menu compatibility that can call Honeycomb APIs.
 */
class MenuItemCompatHoneycomb {
    public static void setShowAsAction(android.view.MenuItem item, int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    public static android.view.MenuItem setActionView(android.view.MenuItem item, android.view.View view) {
        return item.setActionView(view);
    }

    public static android.view.MenuItem setActionView(android.view.MenuItem item, int resId) {
        return item.setActionView(resId);
    }

    public static android.view.View getActionView(android.view.MenuItem item) {
        return item.getActionView();
    }
}

