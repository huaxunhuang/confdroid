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
package android.support.v4.view;


/**
 * JellyBean specific AccessibilityDelegate API implementation.
 */
class AccessibilityDelegateCompatJellyBean {
    public interface AccessibilityDelegateBridgeJellyBean {
        public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityNodeInfo(android.view.View host, java.lang.Object info);

        public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event);

        public void sendAccessibilityEvent(android.view.View host, int eventType);

        public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public java.lang.Object getAccessibilityNodeProvider(android.view.View host);

        public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args);
    }

    public static java.lang.Object newAccessibilityDelegateBridge(final android.support.v4.view.AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean bridge) {
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

            @java.lang.Override
            public android.view.accessibility.AccessibilityNodeProvider getAccessibilityNodeProvider(android.view.View host) {
                return ((android.view.accessibility.AccessibilityNodeProvider) (bridge.getAccessibilityNodeProvider(host)));
            }

            @java.lang.Override
            public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
                return bridge.performAccessibilityAction(host, action, args);
            }
        };
    }

    public static java.lang.Object getAccessibilityNodeProvider(java.lang.Object delegate, android.view.View host) {
        return ((android.view.View.AccessibilityDelegate) (delegate)).getAccessibilityNodeProvider(host);
    }

    public static boolean performAccessibilityAction(java.lang.Object delegate, android.view.View host, int action, android.os.Bundle args) {
        return ((android.view.View.AccessibilityDelegate) (delegate)).performAccessibilityAction(host, action, args);
    }
}

