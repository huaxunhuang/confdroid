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
package android.support.v4.view.accessibility;


class AccessibilityNodeInfoCompatApi23 {
    public static java.lang.Object getActionScrollToPosition() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION;
    }

    public static boolean isContextClickable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isContextClickable();
    }

    public static void setContextClickable(java.lang.Object info, boolean contextClickable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setContextClickable(contextClickable);
    }

    public static java.lang.Object getActionShowOnScreen() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN;
    }

    public static java.lang.Object getActionScrollUp() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP;
    }

    public static java.lang.Object getActionScrollDown() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN;
    }

    public static java.lang.Object getActionScrollLeft() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT;
    }

    public static java.lang.Object getActionScrollRight() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT;
    }

    public static java.lang.Object getActionContextClick() {
        return android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK;
    }
}

