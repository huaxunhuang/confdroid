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


/**
 * KitKat-specific AccessibilityNodeProvider API implementation.
 */
class AccessibilityNodeProviderCompatKitKat {
    interface AccessibilityNodeInfoBridge {
        public java.lang.Object createAccessibilityNodeInfo(int virtualViewId);

        public boolean performAction(int virtualViewId, int action, android.os.Bundle arguments);

        public java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.String text, int virtualViewId);

        public java.lang.Object findFocus(int focus);
    }

    public static java.lang.Object newAccessibilityNodeProviderBridge(final android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge bridge) {
        return new android.view.accessibility.AccessibilityNodeProvider() {
            @java.lang.Override
            public android.view.accessibility.AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
                return ((android.view.accessibility.AccessibilityNodeInfo) (bridge.createAccessibilityNodeInfo(virtualViewId)));
            }

            @java.lang.Override
            @java.lang.SuppressWarnings("unchecked")
            public java.util.List<android.view.accessibility.AccessibilityNodeInfo> findAccessibilityNodeInfosByText(java.lang.String text, int virtualViewId) {
                // Use some voodoo to avoid creating intermediary instances.
                return ((java.util.List<android.view.accessibility.AccessibilityNodeInfo>) ((java.util.List<?>) (bridge.findAccessibilityNodeInfosByText(text, virtualViewId))));
            }

            @java.lang.Override
            public boolean performAction(int virtualViewId, int action, android.os.Bundle arguments) {
                return bridge.performAction(virtualViewId, action, arguments);
            }

            @java.lang.Override
            public android.view.accessibility.AccessibilityNodeInfo findFocus(int focus) {
                return ((android.view.accessibility.AccessibilityNodeInfo) (bridge.findFocus(focus)));
            }
        };
    }
}

