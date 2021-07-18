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
 * Helper for accessing features in {@link KeyEvent} introduced after
 * API level 4 in a backwards compatible fashion.
 */
public final class KeyEventCompat {
    /**
     * Interface for the full API.
     */
    interface KeyEventVersionImpl {
        int normalizeMetaState(int metaState);

        boolean metaStateHasModifiers(int metaState, int modifiers);

        boolean metaStateHasNoModifiers(int metaState);

        boolean isCtrlPressed(android.view.KeyEvent event);
    }

    /**
     * Interface implementation that doesn't use anything about v4 APIs.
     */
    static class BaseKeyEventVersionImpl implements android.support.v4.view.KeyEventCompat.KeyEventVersionImpl {
        private static final int META_MODIFIER_MASK = (((((android.view.KeyEvent.META_SHIFT_ON | android.view.KeyEvent.META_SHIFT_LEFT_ON) | android.view.KeyEvent.META_SHIFT_RIGHT_ON) | android.view.KeyEvent.META_ALT_ON) | android.view.KeyEvent.META_ALT_LEFT_ON) | android.view.KeyEvent.META_ALT_RIGHT_ON) | android.view.KeyEvent.META_SYM_ON;

        // Mask of all lock key meta states.
        private static final int META_ALL_MASK = android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.META_MODIFIER_MASK;

        private static int metaStateFilterDirectionalModifiers(int metaState, int modifiers, int basic, int left, int right) {
            final boolean wantBasic = (modifiers & basic) != 0;
            final int directional = left | right;
            final boolean wantLeftOrRight = (modifiers & directional) != 0;
            if (wantBasic) {
                if (wantLeftOrRight) {
                    throw new java.lang.IllegalArgumentException("bad arguments");
                }
                return metaState & (~directional);
            } else
                if (wantLeftOrRight) {
                    return metaState & (~basic);
                } else {
                    return metaState;
                }

        }

        @java.lang.Override
        public int normalizeMetaState(int metaState) {
            if ((metaState & (android.view.KeyEvent.META_SHIFT_LEFT_ON | android.view.KeyEvent.META_SHIFT_RIGHT_ON)) != 0) {
                metaState |= android.view.KeyEvent.META_SHIFT_ON;
            }
            if ((metaState & (android.view.KeyEvent.META_ALT_LEFT_ON | android.view.KeyEvent.META_ALT_RIGHT_ON)) != 0) {
                metaState |= android.view.KeyEvent.META_ALT_ON;
            }
            return metaState & android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.META_ALL_MASK;
        }

        @java.lang.Override
        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            metaState = normalizeMetaState(metaState) & android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.META_MODIFIER_MASK;
            metaState = android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.metaStateFilterDirectionalModifiers(metaState, modifiers, android.view.KeyEvent.META_SHIFT_ON, android.view.KeyEvent.META_SHIFT_LEFT_ON, android.view.KeyEvent.META_SHIFT_RIGHT_ON);
            metaState = android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.metaStateFilterDirectionalModifiers(metaState, modifiers, android.view.KeyEvent.META_ALT_ON, android.view.KeyEvent.META_ALT_LEFT_ON, android.view.KeyEvent.META_ALT_RIGHT_ON);
            return metaState == modifiers;
        }

        @java.lang.Override
        public boolean metaStateHasNoModifiers(int metaState) {
            return (normalizeMetaState(metaState) & android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl.META_MODIFIER_MASK) == 0;
        }

        @java.lang.Override
        public boolean isCtrlPressed(android.view.KeyEvent event) {
            return false;
        }
    }

    /**
     * Interface implementation for devices with at least v11 APIs.
     */
    static class HoneycombKeyEventVersionImpl extends android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl {
        @java.lang.Override
        public int normalizeMetaState(int metaState) {
            return android.support.v4.view.KeyEventCompatHoneycomb.normalizeMetaState(metaState);
        }

        @java.lang.Override
        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return android.support.v4.view.KeyEventCompatHoneycomb.metaStateHasModifiers(metaState, modifiers);
        }

        @java.lang.Override
        public boolean metaStateHasNoModifiers(int metaState) {
            return android.support.v4.view.KeyEventCompatHoneycomb.metaStateHasNoModifiers(metaState);
        }

        @java.lang.Override
        public boolean isCtrlPressed(android.view.KeyEvent event) {
            return android.support.v4.view.KeyEventCompatHoneycomb.isCtrlPressed(event);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.view.KeyEventCompat.KeyEventVersionImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            IMPL = new android.support.v4.view.KeyEventCompat.HoneycombKeyEventVersionImpl();
        } else {
            IMPL = new android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl();
        }
    }

    // -------------------------------------------------------------------
    public static int normalizeMetaState(int metaState) {
        return android.support.v4.view.KeyEventCompat.IMPL.normalizeMetaState(metaState);
    }

    public static boolean metaStateHasModifiers(int metaState, int modifiers) {
        return android.support.v4.view.KeyEventCompat.IMPL.metaStateHasModifiers(metaState, modifiers);
    }

    public static boolean metaStateHasNoModifiers(int metaState) {
        return android.support.v4.view.KeyEventCompat.IMPL.metaStateHasNoModifiers(metaState);
    }

    public static boolean hasModifiers(android.view.KeyEvent event, int modifiers) {
        return android.support.v4.view.KeyEventCompat.IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
    }

    public static boolean hasNoModifiers(android.view.KeyEvent event) {
        return android.support.v4.view.KeyEventCompat.IMPL.metaStateHasNoModifiers(event.getMetaState());
    }

    /**
     *
     *
     * @deprecated Call {@link KeyEvent#startTracking()} directly. This method will be removed in a
    future release.
     */
    @java.lang.Deprecated
    public static void startTracking(android.view.KeyEvent event) {
        event.startTracking();
    }

    /**
     *
     *
     * @deprecated Call {@link KeyEvent#isTracking()} directly. This method will be removed in a
    future release.
     */
    @java.lang.Deprecated
    public static boolean isTracking(android.view.KeyEvent event) {
        return event.isTracking();
    }

    /**
     *
     *
     * @deprecated Call {@link View#getKeyDispatcherState()} directly. This method will be removed
    in a future release.
     */
    @java.lang.Deprecated
    public static java.lang.Object getKeyDispatcherState(android.view.View view) {
        return view.getKeyDispatcherState();
    }

    /**
     *
     *
     * @deprecated Call
    {@link KeyEvent#dispatch(KeyEvent.Callback, KeyEvent.DispatcherState, Object)} directly.
    This method will be removed in a future release.
     */
    @java.lang.Deprecated
    public static boolean dispatch(android.view.KeyEvent event, android.view.KeyEvent.Callback receiver, java.lang.Object state, java.lang.Object target) {
        return event.dispatch(receiver, ((android.view.KeyEvent.DispatcherState) (state)), target);
    }

    public static boolean isCtrlPressed(android.view.KeyEvent event) {
        return android.support.v4.view.KeyEventCompat.IMPL.isCtrlPressed(event);
    }

    private KeyEventCompat() {
    }
}

