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
 * Helper for accessing features in {@link AccessibilityManager}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class AccessibilityManagerCompat {
    interface AccessibilityManagerVersionImpl {
        android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener);

        boolean addAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener);

        boolean removeAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener);

        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager, int feedbackTypeFlags);

        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager);

        boolean isTouchExplorationEnabled(android.view.accessibility.AccessibilityManager manager);

        android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener);

        boolean addTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener);

        boolean removeTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener);
    }

    static class AccessibilityManagerStubImpl implements android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl {
        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return null;
        }

        @java.lang.Override
        public boolean addAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return false;
        }

        @java.lang.Override
        public boolean removeAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return false;
        }

        @java.lang.Override
        public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager, int feedbackTypeFlags) {
            return java.util.Collections.emptyList();
        }

        @java.lang.Override
        public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager) {
            return java.util.Collections.emptyList();
        }

        @java.lang.Override
        public boolean isTouchExplorationEnabled(android.view.accessibility.AccessibilityManager manager) {
            return false;
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return null;
        }

        @java.lang.Override
        public boolean addTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return false;
        }

        @java.lang.Override
        public boolean removeTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return false;
        }
    }

    static class AccessibilityManagerIcsImpl extends android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl {
        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(final android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return new android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper(listener, new android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge() {
                @java.lang.Override
                public void onAccessibilityStateChanged(boolean enabled) {
                    listener.onAccessibilityStateChanged(enabled);
                }
            });
        }

        @java.lang.Override
        public boolean addAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(manager, newAccessibilityStateChangeListener(listener));
        }

        @java.lang.Override
        public boolean removeAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(manager, newAccessibilityStateChangeListener(listener));
        }

        @java.lang.Override
        public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager, int feedbackTypeFlags) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
        }

        @java.lang.Override
        public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(manager);
        }

        @java.lang.Override
        public boolean isTouchExplorationEnabled(android.view.accessibility.AccessibilityManager manager) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.isTouchExplorationEnabled(manager);
        }
    }

    static class AccessibilityManagerKitKatImpl extends android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerIcsImpl {
        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(final android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return new android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper(listener, new android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge() {
                @java.lang.Override
                public void onTouchExplorationStateChanged(boolean enabled) {
                    listener.onTouchExplorationStateChanged(enabled);
                }
            });
        }

        @java.lang.Override
        public boolean addTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.addTouchExplorationStateChangeListener(manager, newTouchExplorationStateChangeListener(listener));
        }

        @java.lang.Override
        public boolean removeTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
            return android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.removeTouchExplorationStateChangeListener(manager, newTouchExplorationStateChangeListener(listener));
        }
    }

    static {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            // KitKat
            IMPL = new android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerKitKatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                // ICS
                IMPL = new android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerIcsImpl();
            } else {
                IMPL = new android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl();
            }

    }

    private static final android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl IMPL;

    /**
     * Registers an {@link AccessibilityManager.AccessibilityStateChangeListener} for changes in
     * the global accessibility state of the system.
     *
     * @param manager
     * 		The accessibility manager.
     * @param listener
     * 		The listener.
     * @return True if successfully registered.
     */
    public static boolean addAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.addAccessibilityStateChangeListener(manager, listener);
    }

    /**
     * Unregisters an {@link AccessibilityManager.AccessibilityStateChangeListener}.
     *
     * @param manager
     * 		The accessibility manager.
     * @param listener
     * 		The listener.
     * @return True if successfully unregistered.
     */
    public static boolean removeAccessibilityStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener listener) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.removeAccessibilityStateChangeListener(manager, listener);
    }

    /**
     * Returns the {@link AccessibilityServiceInfo}s of the installed accessibility services.
     *
     * @param manager
     * 		The accessibility manager.
     * @return An unmodifiable list with {@link AccessibilityServiceInfo}s.
     */
    public static java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.getInstalledAccessibilityServiceList(manager);
    }

    /**
     * Returns the {@link AccessibilityServiceInfo}s of the enabled accessibility services
     * for a given feedback type.
     *
     * @param manager
     * 		The accessibility manager.
     * @param feedbackTypeFlags
     * 		The feedback type flags.
     * @return An unmodifiable list with {@link AccessibilityServiceInfo}s.
     * @see AccessibilityServiceInfo#FEEDBACK_AUDIBLE
     * @see AccessibilityServiceInfo#FEEDBACK_GENERIC
     * @see AccessibilityServiceInfo#FEEDBACK_HAPTIC
     * @see AccessibilityServiceInfo#FEEDBACK_SPOKEN
     * @see AccessibilityServiceInfo#FEEDBACK_VISUAL
     */
    public static java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(android.view.accessibility.AccessibilityManager manager, int feedbackTypeFlags) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
    }

    /**
     * Returns if the touch exploration in the system is enabled.
     *
     * @param manager
     * 		The accessibility manager.
     * @return True if touch exploration is enabled, false otherwise.
     */
    public static boolean isTouchExplorationEnabled(android.view.accessibility.AccessibilityManager manager) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.isTouchExplorationEnabled(manager);
    }

    /**
     * Registers a {@link TouchExplorationStateChangeListener} for changes in
     * the global touch exploration state of the system.
     *
     * @param listener
     * 		The listener.
     * @return True if successfully registered.
     */
    public static boolean addTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.addTouchExplorationStateChangeListener(manager, listener);
    }

    /**
     * Unregisters a {@link TouchExplorationStateChangeListener}.
     *
     * @param listener
     * 		The listener.
     * @return True if successfully unregistered.
     */
    public static boolean removeTouchExplorationStateChangeListener(android.view.accessibility.AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener listener) {
        return android.support.v4.view.accessibility.AccessibilityManagerCompat.IMPL.removeTouchExplorationStateChangeListener(manager, listener);
    }

    /**
     * Listener for the accessibility state.
     *
     * @deprecated Use {@link AccessibilityStateChangeListener} instead.
     */
    @java.lang.Deprecated
    public static abstract class AccessibilityStateChangeListenerCompat implements android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListener {}

    /**
     * Listener for the accessibility state.
     */
    public interface AccessibilityStateChangeListener {
        /**
         * Called back on change in the accessibility state.
         *
         * @param enabled
         * 		Whether accessibility is enabled.
         */
        void onAccessibilityStateChanged(boolean enabled);
    }

    /**
     * Listener for the system touch exploration state. To listen for changes to
     * the touch exploration state on the device, implement this interface and
     * register it with the system by calling
     * {@link #addTouchExplorationStateChangeListener}.
     */
    public interface TouchExplorationStateChangeListener {
        /**
         * Called when the touch exploration enabled state changes.
         *
         * @param enabled
         * 		Whether touch exploration is enabled.
         */
        void onTouchExplorationStateChanged(boolean enabled);
    }

    private AccessibilityManagerCompat() {
    }
}

