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


/**
 * KitKat-specific AccessibilityManager API implementation.
 */
class AccessibilityManagerCompatKitKat {
    public static class TouchExplorationStateChangeListenerWrapper implements android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener {
        final java.lang.Object mListener;

        final android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge mListenerBridge;

        public TouchExplorationStateChangeListenerWrapper(java.lang.Object listener, android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge listenerBridge) {
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
            android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper other = ((android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper) (o));
            return mListener == null ? other.mListener == null : mListener.equals(other.mListener);
        }

        @java.lang.Override
        public void onTouchExplorationStateChanged(boolean enabled) {
            mListenerBridge.onTouchExplorationStateChanged(enabled);
        }
    }

    interface TouchExplorationStateChangeListenerBridge {
        void onTouchExplorationStateChanged(boolean enabled);
    }

    public static java.lang.Object newTouchExplorationStateChangeListener(final android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge bridge) {
        return new android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener() {
            @java.lang.Override
            public void onTouchExplorationStateChanged(boolean enabled) {
                bridge.onTouchExplorationStateChanged(enabled);
            }
        };
    }

    public static boolean addTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, java.lang.Object listener) {
        return manager.addTouchExplorationStateChangeListener(((android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener) (listener)));
    }

    public static boolean removeTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, java.lang.Object listener) {
        return manager.removeTouchExplorationStateChangeListener(((android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener) (listener)));
    }
}

