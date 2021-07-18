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
 * ICS specific AccessibilityManager API implementation.
 */
class AccessibilityManagerCompatIcs {
    public static class AccessibilityStateChangeListenerWrapper implements android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener {
        java.lang.Object mListener;

        android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge mListenerBridge;

        public AccessibilityStateChangeListenerWrapper(java.lang.Object listener, android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge listenerBridge) {
            mListener = listener;
            mListenerBridge = listenerBridge;
        }

        @java.lang.Override
        public int hashCode() {
            return mListener == null ? 0 : mListener.hashCode();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper other = ((android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper) (o));
            return mListener == null ? other.mListener == null : mListener.equals(other.mListener);
        }

        @java.lang.Override
        public void onAccessibilityStateChanged(boolean enabled) {
            mListenerBridge.onAccessibilityStateChanged(enabled);
        }
    }

    interface AccessibilityStateChangeListenerBridge {
        void onAccessibilityStateChanged(boolean enabled);
    }

    public static boolean addAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper listener) {
        return manager.addAccessibilityStateChangeListener(listener);
    }

    public static boolean removeAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper listener) {
        return manager.removeAccessibilityStateChangeListener(listener);
    }

    public static java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager, int feedbackTypeFlags) {
        return manager.getEnabledAccessibilityServiceList(feedbackTypeFlags);
    }

    public static java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager) {
        return manager.getInstalledAccessibilityServiceList();
    }

    public static boolean isTouchExplorationEnabled(android.view.accessibility.AccessibilityManager manager) {
        return manager.isTouchExplorationEnabled();
    }
}

