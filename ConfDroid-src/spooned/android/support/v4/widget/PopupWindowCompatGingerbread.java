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
 * limitations under the License
 */
package android.support.v4.widget;


/**
 * Implementation of PopupWindow compatibility that can call Gingerbread APIs.
 */
class PopupWindowCompatGingerbread {
    private static java.lang.reflect.Method sSetWindowLayoutTypeMethod;

    private static boolean sSetWindowLayoutTypeMethodAttempted;

    private static java.lang.reflect.Method sGetWindowLayoutTypeMethod;

    private static boolean sGetWindowLayoutTypeMethodAttempted;

    static void setWindowLayoutType(android.widget.PopupWindow popupWindow, int layoutType) {
        if (!android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted) {
            try {
                android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod = android.widget.PopupWindow.class.getDeclaredMethod("setWindowLayoutType", int.class);
                android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod.setAccessible(true);
            } catch (java.lang.Exception e) {
                // Reflection method fetch failed. Oh well.
            }
            android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted = true;
        }
        if (android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod != null) {
            try {
                android.support.v4.widget.PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod.invoke(popupWindow, layoutType);
            } catch (java.lang.Exception e) {
                // Reflection call failed. Oh well.
            }
        }
    }

    static int getWindowLayoutType(android.widget.PopupWindow popupWindow) {
        if (!android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted) {
            try {
                android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod = android.widget.PopupWindow.class.getDeclaredMethod("getWindowLayoutType");
                android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod.setAccessible(true);
            } catch (java.lang.Exception e) {
                // Reflection method fetch failed. Oh well.
            }
            android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted = true;
        }
        if (android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod != null) {
            try {
                return ((java.lang.Integer) (android.support.v4.widget.PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod.invoke(popupWindow)));
            } catch (java.lang.Exception e) {
                // Reflection call failed. Oh well.
            }
        }
        return 0;
    }
}

