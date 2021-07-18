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
 * ICS specific AccessibilityDelegate API implementation.
 */
class AccessibilityDelegateCompatIcs {
    public interface AccessibilityDelegateBridge {
        public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityNodeInfo(android.view.View host, java.lang.Object info);

        public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event);

        public void sendAccessibilityEvent(android.view.View host, int eventType);

        public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event);
    }

    public static java.lang.Object newAccessibilityDelegateDefaultImpl() {
        return new android.view.View.AccessibilityDelegate();
    }

    public static java.lang.Object newAccessibilityDelegateBridge(final android.support.v4.view.AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge bridge) {
        return new android.view.View.AccessibilityDelegate() {
            @java.lang.Override
            public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                return bridge.dispatchPopulateAccessibilityEvent(host, event);
            }

            @java.lang.Override
            public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                bridge.onInitializeAccessibilityEvent(host, event);
            }

            @java.lang.Override
            public void onInitializeAccessibilityNodeInfo(android.view.View host, android.view.accessibility.AccessibilityNodeInfo info) {
                bridge.onInitializeAccessibilityNodeInfo(host, info);
            }

            @java.lang.Override
            public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                bridge.onPopulateAccessibilityEvent(host, event);
            }

            @java.lang.Override
            public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
                return bridge.onRequestSendAccessibilityEvent(host, child, event);
            }

            @java.lang.Override
            public void sendAccessibilityEvent(android.view.View host, int eventType) {
                bridge.sendAccessibilityEvent(host, eventType);
            }

            @java.lang.Override
            public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                bridge.sendAccessibilityEventUnchecked(host, event);
            }
        };
    }

    public static boolean dispatchPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        return ((android.view.View.AccessibilityDelegate) (delegate)).dispatchPopulateAccessibilityEvent(host, event);
    }

    public static void onInitializeAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        ((android.view.View.AccessibilityDelegate) (delegate)).onInitializeAccessibilityEvent(host, event);
    }

    public static void onInitializeAccessibilityNodeInfo(java.lang.Object delegate, android.view.View host, java.lang.Object info) {
        ((android.view.View.AccessibilityDelegate) (delegate)).onInitializeAccessibilityNodeInfo(host, ((android.view.accessibility.AccessibilityNodeInfo) (info)));
    }

    public static void onPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        ((android.view.View.AccessibilityDelegate) (delegate)).onPopulateAccessibilityEvent(host, event);
    }

    public static boolean onRequestSendAccessibilityEvent(java.lang.Object delegate, android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
        return ((android.view.View.AccessibilityDelegate) (delegate)).onRequestSendAccessibilityEvent(host, child, event);
    }

    public static void sendAccessibilityEvent(java.lang.Object delegate, android.view.View host, int eventType) {
        ((android.view.View.AccessibilityDelegate) (delegate)).sendAccessibilityEvent(host, eventType);
    }

    public static void sendAccessibilityEventUnchecked(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        ((android.view.View.AccessibilityDelegate) (delegate)).sendAccessibilityEventUnchecked(host, event);
    }
}

