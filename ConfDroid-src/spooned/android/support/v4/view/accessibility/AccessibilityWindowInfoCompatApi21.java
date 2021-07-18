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
package android.support.v4.view.accessibility;


/**
 * Api21-specific AccessibilityWindowInfo API implementation.
 */
class AccessibilityWindowInfoCompatApi21 {
    public static java.lang.Object obtain() {
        return android.view.accessibility.AccessibilityWindowInfo.obtain();
    }

    public static java.lang.Object obtain(java.lang.Object info) {
        return android.view.accessibility.AccessibilityWindowInfo.obtain(((android.view.accessibility.AccessibilityWindowInfo) (info)));
    }

    public static int getType(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getType();
    }

    public static int getLayer(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getLayer();
    }

    public static java.lang.Object getRoot(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getRoot();
    }

    public static java.lang.Object getParent(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getParent();
    }

    public static int getId(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getId();
    }

    public static void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
        ((android.view.accessibility.AccessibilityWindowInfo) (info)).getBoundsInScreen(outBounds);
    }

    public static boolean isActive(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).isActive();
    }

    public static boolean isFocused(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).isFocused();
    }

    public static boolean isAccessibilityFocused(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).isAccessibilityFocused();
    }

    public static int getChildCount(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getChildCount();
    }

    public static java.lang.Object getChild(java.lang.Object info, int index) {
        return ((android.view.accessibility.AccessibilityWindowInfo) (info)).getChild(index);
    }

    public static void recycle(java.lang.Object info) {
        ((android.view.accessibility.AccessibilityWindowInfo) (info)).recycle();
    }
}

