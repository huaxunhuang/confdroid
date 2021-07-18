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
 * Api21-specific AccessibilityNodeInfo API implementation.
 */
class AccessibilityNodeInfoCompatApi21 {
    static java.util.List<java.lang.Object> getActionList(java.lang.Object info) {
        java.lang.Object result = ((android.view.accessibility.AccessibilityNodeInfo) (info)).getActionList();
        return ((java.util.List<java.lang.Object>) (result));
    }

    static void addAction(java.lang.Object info, java.lang.Object action) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).addAction(((android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction) (action)));
    }

    public static boolean removeAction(java.lang.Object info, java.lang.Object action) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).removeAction(((android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction) (action)));
    }

    public static java.lang.Object obtainCollectionInfo(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
        return android.view.accessibility.AccessibilityNodeInfo.CollectionInfo.obtain(rowCount, columnCount, hierarchical, selectionMode);
    }

    public static java.lang.Object obtainCollectionItemInfo(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
        return android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo.obtain(rowIndex, rowSpan, columnIndex, columnSpan, heading, selected);
    }

    public static java.lang.CharSequence getError(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getError();
    }

    public static void setError(java.lang.Object info, java.lang.CharSequence error) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setError(error);
    }

    public static void setMaxTextLength(java.lang.Object info, int max) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setMaxTextLength(max);
    }

    public static int getMaxTextLength(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getMaxTextLength();
    }

    public static java.lang.Object getWindow(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getWindow();
    }

    public static boolean removeChild(java.lang.Object info, android.view.View child) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).removeChild(child);
    }

    public static boolean removeChild(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).removeChild(root, virtualDescendantId);
    }

    static class CollectionInfo {
        public static int getSelectionMode(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionInfo) (info)).getSelectionMode();
        }
    }

    static class CollectionItemInfo {
        public static boolean isSelected(java.lang.Object info) {
            return ((android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo) (info)).isSelected();
        }
    }

    static java.lang.Object newAccessibilityAction(int actionId, java.lang.CharSequence label) {
        return new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(actionId, label);
    }

    static int getAccessibilityActionId(java.lang.Object action) {
        return ((android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction) (action)).getId();
    }

    static java.lang.CharSequence getAccessibilityActionLabel(java.lang.Object action) {
        return ((android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction) (action)).getLabel();
    }
}

