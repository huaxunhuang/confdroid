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
package android.support.v4.view.accessibility;


/**
 * ICS specific AccessibilityNodeInfo API implementation.
 */
class AccessibilityNodeInfoCompatIcs {
    public static java.lang.Object obtain() {
        return android.view.accessibility.AccessibilityNodeInfo.obtain();
    }

    public static java.lang.Object obtain(android.view.View source) {
        return android.view.accessibility.AccessibilityNodeInfo.obtain(source);
    }

    public static java.lang.Object obtain(java.lang.Object info) {
        return android.view.accessibility.AccessibilityNodeInfo.obtain(((android.view.accessibility.AccessibilityNodeInfo) (info)));
    }

    public static void addAction(java.lang.Object info, int action) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).addAction(action);
    }

    public static void addChild(java.lang.Object info, android.view.View child) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).addChild(child);
    }

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.Object info, java.lang.String text) {
        java.lang.Object result = ((android.view.accessibility.AccessibilityNodeInfo) (info)).findAccessibilityNodeInfosByText(text);
        return ((java.util.List<java.lang.Object>) (result));
    }

    public static int getActions(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getActions();
    }

    public static void getBoundsInParent(java.lang.Object info, android.graphics.Rect outBounds) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).getBoundsInParent(outBounds);
    }

    public static void getBoundsInScreen(java.lang.Object info, android.graphics.Rect outBounds) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).getBoundsInScreen(outBounds);
    }

    public static java.lang.Object getChild(java.lang.Object info, int index) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getChild(index);
    }

    public static int getChildCount(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getChildCount();
    }

    public static java.lang.CharSequence getClassName(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getClassName();
    }

    public static java.lang.CharSequence getContentDescription(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getContentDescription();
    }

    public static java.lang.CharSequence getPackageName(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getPackageName();
    }

    public static java.lang.Object getParent(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getParent();
    }

    public static java.lang.CharSequence getText(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getText();
    }

    public static int getWindowId(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getWindowId();
    }

    public static boolean isCheckable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isCheckable();
    }

    public static boolean isChecked(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isChecked();
    }

    public static boolean isClickable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isClickable();
    }

    public static boolean isEnabled(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isEnabled();
    }

    public static boolean isFocusable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isFocusable();
    }

    public static boolean isFocused(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isFocused();
    }

    public static boolean isLongClickable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isLongClickable();
    }

    public static boolean isPassword(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isPassword();
    }

    public static boolean isScrollable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isScrollable();
    }

    public static boolean isSelected(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isSelected();
    }

    public static boolean performAction(java.lang.Object info, int action) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).performAction(action);
    }

    public static void setBoundsInParent(java.lang.Object info, android.graphics.Rect bounds) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setBoundsInParent(bounds);
    }

    public static void setBoundsInScreen(java.lang.Object info, android.graphics.Rect bounds) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setBoundsInScreen(bounds);
    }

    public static void setCheckable(java.lang.Object info, boolean checkable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setCheckable(checkable);
    }

    public static void setChecked(java.lang.Object info, boolean checked) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setChecked(checked);
    }

    public static void setClassName(java.lang.Object info, java.lang.CharSequence className) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setClassName(className);
    }

    public static void setClickable(java.lang.Object info, boolean clickable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setClickable(clickable);
    }

    public static void setContentDescription(java.lang.Object info, java.lang.CharSequence contentDescription) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setContentDescription(contentDescription);
    }

    public static void setEnabled(java.lang.Object info, boolean enabled) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setEnabled(enabled);
    }

    public static void setFocusable(java.lang.Object info, boolean focusable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setFocusable(focusable);
    }

    public static void setFocused(java.lang.Object info, boolean focused) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setFocused(focused);
    }

    public static void setLongClickable(java.lang.Object info, boolean longClickable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setLongClickable(longClickable);
    }

    public static void setPackageName(java.lang.Object info, java.lang.CharSequence packageName) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setPackageName(packageName);
    }

    public static void setParent(java.lang.Object info, android.view.View parent) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setParent(parent);
    }

    public static void setPassword(java.lang.Object info, boolean password) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setPassword(password);
    }

    public static void setScrollable(java.lang.Object info, boolean scrollable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setScrollable(scrollable);
    }

    public static void setSelected(java.lang.Object info, boolean selected) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setSelected(selected);
    }

    public static void setSource(java.lang.Object info, android.view.View source) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setSource(source);
    }

    public static void setText(java.lang.Object info, java.lang.CharSequence text) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setText(text);
    }

    public static void recycle(java.lang.Object info) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).recycle();
    }
}

