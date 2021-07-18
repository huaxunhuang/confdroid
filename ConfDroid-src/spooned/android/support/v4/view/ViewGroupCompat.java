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
 * Helper for accessing features in {@link ViewGroup}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class ViewGroupCompat {
    /**
     * This constant is a {@link #setLayoutMode(ViewGroup, int) layoutMode}.
     * Clip bounds are the raw values of {@link android.view.View#getLeft() left},
     * {@link android.view.View#getTop() top},
     * {@link android.view.View#getRight() right} and {@link android.view.View#getBottom() bottom}.
     */
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;

    /**
     * This constant is a {@link #setLayoutMode(ViewGroup, int) layoutMode}.
     * Optical bounds describe where a widget appears to be. They sit inside the clip
     * bounds which need to cover a larger area to allow other effects,
     * such as shadows and glows, to be drawn.
     */
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

    interface ViewGroupCompatImpl {
        boolean onRequestSendAccessibilityEvent(android.view.ViewGroup group, android.view.View child, android.view.accessibility.AccessibilityEvent event);

        void setMotionEventSplittingEnabled(android.view.ViewGroup group, boolean split);

        int getLayoutMode(android.view.ViewGroup group);

        void setLayoutMode(android.view.ViewGroup group, int mode);

        void setTransitionGroup(android.view.ViewGroup group, boolean isTransitionGroup);

        boolean isTransitionGroup(android.view.ViewGroup group);

        int getNestedScrollAxes(android.view.ViewGroup group);
    }

    static class ViewGroupCompatStubImpl implements android.support.v4.view.ViewGroupCompat.ViewGroupCompatImpl {
        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup group, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            return true;
        }

        @java.lang.Override
        public void setMotionEventSplittingEnabled(android.view.ViewGroup group, boolean split) {
            // no-op, didn't exist.
        }

        @java.lang.Override
        public int getLayoutMode(android.view.ViewGroup group) {
            return android.support.v4.view.ViewGroupCompat.LAYOUT_MODE_CLIP_BOUNDS;
        }

        @java.lang.Override
        public void setLayoutMode(android.view.ViewGroup group, int mode) {
            // no-op, didn't exist. Views only support clip bounds.
        }

        @java.lang.Override
        public void setTransitionGroup(android.view.ViewGroup group, boolean isTransitionGroup) {
        }

        @java.lang.Override
        public boolean isTransitionGroup(android.view.ViewGroup group) {
            return false;
        }

        @java.lang.Override
        public int getNestedScrollAxes(android.view.ViewGroup group) {
            if (group instanceof android.support.v4.view.NestedScrollingParent) {
                return ((android.support.v4.view.NestedScrollingParent) (group)).getNestedScrollAxes();
            }
            return android.support.v4.view.ViewCompat.SCROLL_AXIS_NONE;
        }
    }

    static class ViewGroupCompatHCImpl extends android.support.v4.view.ViewGroupCompat.ViewGroupCompatStubImpl {
        @java.lang.Override
        public void setMotionEventSplittingEnabled(android.view.ViewGroup group, boolean split) {
            android.support.v4.view.ViewGroupCompatHC.setMotionEventSplittingEnabled(group, split);
        }
    }

    static class ViewGroupCompatIcsImpl extends android.support.v4.view.ViewGroupCompat.ViewGroupCompatHCImpl {
        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup group, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.ViewGroupCompatIcs.onRequestSendAccessibilityEvent(group, child, event);
        }
    }

    static class ViewGroupCompatJellybeanMR2Impl extends android.support.v4.view.ViewGroupCompat.ViewGroupCompatIcsImpl {
        @java.lang.Override
        public int getLayoutMode(android.view.ViewGroup group) {
            return android.support.v4.view.ViewGroupCompatJellybeanMR2.getLayoutMode(group);
        }

        @java.lang.Override
        public void setLayoutMode(android.view.ViewGroup group, int mode) {
            android.support.v4.view.ViewGroupCompatJellybeanMR2.setLayoutMode(group, mode);
        }
    }

    static class ViewGroupCompatLollipopImpl extends android.support.v4.view.ViewGroupCompat.ViewGroupCompatJellybeanMR2Impl {
        @java.lang.Override
        public void setTransitionGroup(android.view.ViewGroup group, boolean isTransitionGroup) {
            android.support.v4.view.ViewGroupCompatLollipop.setTransitionGroup(group, isTransitionGroup);
        }

        @java.lang.Override
        public boolean isTransitionGroup(android.view.ViewGroup group) {
            return android.support.v4.view.ViewGroupCompatLollipop.isTransitionGroup(group);
        }

        @java.lang.Override
        public int getNestedScrollAxes(android.view.ViewGroup group) {
            return android.support.v4.view.ViewGroupCompatLollipop.getNestedScrollAxes(group);
        }
    }

    static final android.support.v4.view.ViewGroupCompat.ViewGroupCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new android.support.v4.view.ViewGroupCompat.ViewGroupCompatLollipopImpl();
        } else
            if (version >= 18) {
                IMPL = new android.support.v4.view.ViewGroupCompat.ViewGroupCompatJellybeanMR2Impl();
            } else
                if (version >= 14) {
                    IMPL = new android.support.v4.view.ViewGroupCompat.ViewGroupCompatIcsImpl();
                } else
                    if (version >= 11) {
                        IMPL = new android.support.v4.view.ViewGroupCompat.ViewGroupCompatHCImpl();
                    } else {
                        IMPL = new android.support.v4.view.ViewGroupCompat.ViewGroupCompatStubImpl();
                    }



    }

    /* Hide the constructor. */
    private ViewGroupCompat() {
    }

    /**
     * Called when a child has requested sending an {@link AccessibilityEvent} and
     * gives an opportunity to its parent to augment the event.
     * <p>
     * If an {@link AccessibilityDelegateCompat} has been specified via calling
     * {@link ViewCompat#setAccessibilityDelegate(View, AccessibilityDelegateCompat)} its
     * {@link AccessibilityDelegateCompat#onRequestSendAccessibilityEvent(ViewGroup, View,
     * AccessibilityEvent)} is responsible for handling this call.
     * </p>
     *
     * @param group
     * 		The group whose method to invoke.
     * @param child
     * 		The child which requests sending the event.
     * @param event
     * 		The event to be sent.
     * @return True if the event should be sent.
     */
    public static boolean onRequestSendAccessibilityEvent(android.view.ViewGroup group, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.ViewGroupCompat.IMPL.onRequestSendAccessibilityEvent(group, child, event);
    }

    /**
     * Enable or disable the splitting of MotionEvents to multiple children during touch event
     * dispatch. This behavior is enabled by default for applications that target an
     * SDK version of 11 (Honeycomb) or newer. On earlier platform versions this feature
     * was not supported and this method is a no-op.
     *
     * <p>When this option is enabled MotionEvents may be split and dispatched to different child
     * views depending on where each pointer initially went down. This allows for user interactions
     * such as scrolling two panes of content independently, chording of buttons, and performing
     * independent gestures on different pieces of content.
     *
     * @param group
     * 		ViewGroup to modify
     * @param split
     * 		<code>true</code> to allow MotionEvents to be split and dispatched to multiple
     * 		child views. <code>false</code> to only allow one child view to be the target of
     * 		any MotionEvent received by this ViewGroup.
     */
    public static void setMotionEventSplittingEnabled(android.view.ViewGroup group, boolean split) {
        android.support.v4.view.ViewGroupCompat.IMPL.setMotionEventSplittingEnabled(group, split);
    }

    /**
     * Returns the basis of alignment during layout operations on this ViewGroup:
     * either {@link #LAYOUT_MODE_CLIP_BOUNDS} or {@link #LAYOUT_MODE_OPTICAL_BOUNDS}.
     * <p>
     * If no layoutMode was explicitly set, either programmatically or in an XML resource,
     * the method returns the layoutMode of the view's parent ViewGroup if such a parent exists,
     * otherwise the method returns a default value of {@link #LAYOUT_MODE_CLIP_BOUNDS}.
     *
     * @return the layout mode to use during layout operations
     * @see #setLayoutMode(ViewGroup, int)
     */
    public static int getLayoutMode(android.view.ViewGroup group) {
        return android.support.v4.view.ViewGroupCompat.IMPL.getLayoutMode(group);
    }

    /**
     * Sets the basis of alignment during the layout of this ViewGroup.
     * Valid values are either {@link #LAYOUT_MODE_CLIP_BOUNDS} or
     * {@link #LAYOUT_MODE_OPTICAL_BOUNDS}.
     *
     * @param mode
     * 		the layout mode to use during layout operations
     * @see #getLayoutMode(ViewGroup)
     */
    public static void setLayoutMode(android.view.ViewGroup group, int mode) {
        android.support.v4.view.ViewGroupCompat.IMPL.setLayoutMode(group, mode);
    }

    /**
     * Changes whether or not this ViewGroup should be treated as a single entity during
     * Activity Transitions.
     *
     * @param isTransitionGroup
     * 		Whether or not the ViewGroup should be treated as a unit
     * 		in Activity transitions. If false, the ViewGroup won't transition,
     * 		only its children. If true, the entire ViewGroup will transition
     * 		together.
     */
    public static void setTransitionGroup(android.view.ViewGroup group, boolean isTransitionGroup) {
        android.support.v4.view.ViewGroupCompat.IMPL.setTransitionGroup(group, isTransitionGroup);
    }

    /**
     * Returns true if this ViewGroup should be considered as a single entity for removal
     * when executing an Activity transition. If this is false, child elements will move
     * individually during the transition.
     */
    public static boolean isTransitionGroup(android.view.ViewGroup group) {
        return android.support.v4.view.ViewGroupCompat.IMPL.isTransitionGroup(group);
    }

    /**
     * Return the current axes of nested scrolling for this ViewGroup.
     *
     * <p>A ViewGroup returning something other than {@link ViewCompat#SCROLL_AXIS_NONE} is
     * currently acting as a nested scrolling parent for one or more descendant views in
     * the hierarchy.</p>
     *
     * @return Flags indicating the current axes of nested scrolling
     * @see ViewCompat#SCROLL_AXIS_HORIZONTAL
     * @see ViewCompat#SCROLL_AXIS_VERTICAL
     * @see ViewCompat#SCROLL_AXIS_NONE
     */
    public static int getNestedScrollAxes(android.view.ViewGroup group) {
        return android.support.v4.view.ViewGroupCompat.IMPL.getNestedScrollAxes(group);
    }
}

