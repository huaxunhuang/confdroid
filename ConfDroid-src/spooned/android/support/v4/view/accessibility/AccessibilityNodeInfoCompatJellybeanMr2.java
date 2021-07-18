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
package android.support.v4.view.accessibility;


class AccessibilityNodeInfoCompatJellybeanMr2 {
    public static void setViewIdResourceName(java.lang.Object info, java.lang.String viewId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setViewIdResourceName(viewId);
    }

    public static java.lang.String getViewIdResourceName(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getViewIdResourceName();
    }

    @java.lang.SuppressWarnings("unchecked")
    public static java.util.List<java.lang.Object> findAccessibilityNodeInfosByViewId(java.lang.Object info, java.lang.String viewId) {
        java.lang.Object result = ((android.view.accessibility.AccessibilityNodeInfo) (info)).findAccessibilityNodeInfosByViewId(viewId);
        return ((java.util.List<java.lang.Object>) (result));
    }

    public static void setTextSelection(java.lang.Object info, int start, int end) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setTextSelection(start, end);
    }

    public static int getTextSelectionStart(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getTextSelectionStart();
    }

    public static int getTextSelectionEnd(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getTextSelectionEnd();
    }

    public static boolean isEditable(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).isEditable();
    }

    public static void setEditable(java.lang.Object info, boolean editable) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setEditable(editable);
    }

    public static boolean refresh(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).refresh();
    }
}

