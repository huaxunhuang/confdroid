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
package android.support.v4.view.accessibility;


/**
 * JellyBean specific AccessibilityNodeInfo API implementation.
 */
class AccessibilityNodeInfoCompatJellyBean {
    public static void addChild(java.lang.Object info, android.view.View child, int virtualDescendantId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).addChild(child, virtualDescendantId);
    }

    public static void setSource(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setSource(root, virtualDescendantId);
    }

    public static boolean isVisibleToUser(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isVisibleToUser();
    }

    public static void setVisibleToUser(java.lang.Object info, boolean visibleToUser) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setVisibleToUser(visibleToUser);
    }

    public static boolean performAction(java.lang.Object info, int action, android.os.Bundle arguments) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).performAction(action, arguments);
    }

    public static void setMovementGranularities(java.lang.Object info, int granularities) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setMovementGranularities(granularities);
    }

    public static int getMovementGranularities(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getMovementGranularities();
    }

    public static java.lang.Object obtain(android.view.View root, int virtualDescendantId) {
        return android.view.accessibility.AccessibilityNodeInfo.obtain(root, virtualDescendantId);
    }

    public static java.lang.Object findFocus(java.lang.Object info, int focus) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).findFocus(focus);
    }

    public static java.lang.Object focusSearch(java.lang.Object info, int direction) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).focusSearch(direction);
    }

    public static void setParent(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setParent(root, virtualDescendantId);
    }

    public static boolean isAccessibilityFocused(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isAccessibilityFocused();
    }

    public static void setAccesibilityFocused(java.lang.Object info, boolean focused) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setAccessibilityFocused(focused);
    }
}

