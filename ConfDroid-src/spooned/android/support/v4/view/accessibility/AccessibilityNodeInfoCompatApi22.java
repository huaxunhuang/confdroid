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
 * Api22-specific AccessibilityNodeInfo API implementation.
 */
class AccessibilityNodeInfoCompatApi22 {
    public static java.lang.Object getTraversalBefore(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getTraversalBefore();
    }

    public static void setTraversalBefore(java.lang.Object info, android.view.View view) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setTraversalBefore(view);
    }

    public static void setTraversalBefore(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setTraversalBefore(root, virtualDescendantId);
    }

    public static java.lang.Object getTraversalAfter(java.lang.Object info) {
        return ((android.view.accessibility.AccessibilityNodeInfo) (info)).getTraversalAfter();
    }

    public static void setTraversalAfter(java.lang.Object info, android.view.View view) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setTraversalAfter(view);
    }

    public static void setTraversalAfter(java.lang.Object info, android.view.View root, int virtualDescendantId) {
        ((android.view.accessibility.AccessibilityNodeInfo) (info)).setTraversalAfter(root, virtualDescendantId);
    }
}

