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
package android.support.v4.widget;


/**
 * Helper for accessing features in PopupWindow introduced after API level 4
 * in a backwards compatible fashion.
 */
public final class PopupWindowCompat {
    /**
     * Interface for the full API.
     */
    interface PopupWindowImpl {
        void showAsDropDown(android.widget.PopupWindow popup, android.view.View anchor, int xoff, int yoff, int gravity);

        void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor);

        boolean getOverlapAnchor(android.widget.PopupWindow popupWindow);

        void setWindowLayoutType(android.widget.PopupWindow popupWindow, int layoutType);

        int getWindowLayoutType(android.widget.PopupWindow popupWindow);
    }

    /**
     * Interface implementation that doesn't use anything above v4 APIs.
     */
    static class BasePopupWindowImpl implements android.support.v4.widget.PopupWindowCompat.PopupWindowImpl {
        private static java.lang.reflect.Method sSetWindowLayoutTypeMethod;

        private static boolean sSetWindowLayoutTypeMethodAttempted;

        private static java.lang.reflect.Method sGetWindowLayoutTypeMethod;

        private static boolean sGetWindowLayoutTypeMethodAttempted;

        @java.lang.Override
        public void showAsDropDown(android.widget.PopupWindow popup, android.view.View anchor, int xoff, int yoff, int gravity) {
            final int hgrav = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, android.support.v4.view.ViewCompat.getLayoutDirection(anchor)) & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
            if (hgrav == android.view.Gravity.RIGHT) {
                // Flip the location to align the right sides of the popup and
                // anchor instead of left.
                xoff -= popup.getWidth() - anchor.getWidth();
            }
            popup.showAsDropDown(anchor, xoff, yoff);
        }

        @java.lang.Override
        public void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor) {
            // noop
        }

        @java.lang.Override
        public boolean getOverlapAnchor(android.widget.PopupWindow popupWindow) {
            return false;
        }

        @java.lang.Override
        public void setWindowLayoutType(android.widget.PopupWindow popupWindow, int layoutType) {
            if (!android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethodAttempted) {
                try {
                    android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethod = android.widget.PopupWindow.class.getDeclaredMethod("setWindowLayoutType", int.class);
                    android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethod.setAccessible(true);
                } catch (java.lang.Exception e) {
                    // Reflection method fetch failed. Oh well.
                }
                android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethodAttempted = true;
            }
            if (android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethod != null) {
                try {
                    android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sSetWindowLayoutTypeMethod.invoke(popupWindow, layoutType);
                } catch (java.lang.Exception e) {
                    // Reflection call failed. Oh well.
                }
            }
        }

        @java.lang.Override
        public int getWindowLayoutType(android.widget.PopupWindow popupWindow) {
            if (!android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethodAttempted) {
                try {
                    android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethod = android.widget.PopupWindow.class.getDeclaredMethod("getWindowLayoutType");
                    android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethod.setAccessible(true);
                } catch (java.lang.Exception e) {
                    // Reflection method fetch failed. Oh well.
                }
                android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethodAttempted = true;
            }
            if (android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethod != null) {
                try {
                    return ((java.lang.Integer) (android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl.sGetWindowLayoutTypeMethod.invoke(popupWindow)));
                } catch (java.lang.Exception e) {
                    // Reflection call failed. Oh well.
                }
            }
            return 0;
        }
    }

    /**
     * Interface implementation for devices with at least KitKat APIs.
     */
    static class KitKatPopupWindowImpl extends android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl {
        @java.lang.Override
        public void showAsDropDown(android.widget.PopupWindow popup, android.view.View anchor, int xoff, int yoff, int gravity) {
            android.support.v4.widget.PopupWindowCompatKitKat.showAsDropDown(popup, anchor, xoff, yoff, gravity);
        }
    }

    static class Api21PopupWindowImpl extends android.support.v4.widget.PopupWindowCompat.KitKatPopupWindowImpl {
        @java.lang.Override
        public void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor) {
            android.support.v4.widget.PopupWindowCompatApi21.setOverlapAnchor(popupWindow, overlapAnchor);
        }

        @java.lang.Override
        public boolean getOverlapAnchor(android.widget.PopupWindow popupWindow) {
            return android.support.v4.widget.PopupWindowCompatApi21.getOverlapAnchor(popupWindow);
        }
    }

    static class Api23PopupWindowImpl extends android.support.v4.widget.PopupWindowCompat.Api21PopupWindowImpl {
        @java.lang.Override
        public void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor) {
            android.support.v4.widget.PopupWindowCompatApi23.setOverlapAnchor(popupWindow, overlapAnchor);
        }

        @java.lang.Override
        public boolean getOverlapAnchor(android.widget.PopupWindow popupWindow) {
            return android.support.v4.widget.PopupWindowCompatApi23.getOverlapAnchor(popupWindow);
        }

        @java.lang.Override
        public void setWindowLayoutType(android.widget.PopupWindow popupWindow, int layoutType) {
            android.support.v4.widget.PopupWindowCompatApi23.setWindowLayoutType(popupWindow, layoutType);
        }

        @java.lang.Override
        public int getWindowLayoutType(android.widget.PopupWindow popupWindow) {
            return android.support.v4.widget.PopupWindowCompatApi23.getWindowLayoutType(popupWindow);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.widget.PopupWindowCompat.PopupWindowImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new android.support.v4.widget.PopupWindowCompat.Api23PopupWindowImpl();
        } else
            if (version >= 21) {
                IMPL = new android.support.v4.widget.PopupWindowCompat.Api21PopupWindowImpl();
            } else
                if (version >= 19) {
                    IMPL = new android.support.v4.widget.PopupWindowCompat.KitKatPopupWindowImpl();
                } else {
                    IMPL = new android.support.v4.widget.PopupWindowCompat.BasePopupWindowImpl();
                }


    }

    private PopupWindowCompat() {
        // This class is not publicly instantiable.
    }

    /**
     * <p>Display the content view in a popup window anchored to the bottom-left
     * corner of the anchor view offset by the specified x and y coordinates.
     * If there is not enough room on screen to show
     * the popup in its entirety, this method tries to find a parent scroll
     * view to scroll. If no parent scroll view can be scrolled, the bottom-left
     * corner of the popup is pinned at the top left corner of the anchor view.</p>
     * <p>If the view later scrolls to move <code>anchor</code> to a different
     * location, the popup will be moved correspondingly.</p>
     *
     * @param popup
     * 		the PopupWindow to show
     * @param anchor
     * 		the view on which to pin the popup window
     * @param xoff
     * 		A horizontal offset from the anchor in pixels
     * @param yoff
     * 		A vertical offset from the anchor in pixels
     * @param gravity
     * 		Alignment of the popup relative to the anchor
     */
    public static void showAsDropDown(android.widget.PopupWindow popup, android.view.View anchor, int xoff, int yoff, int gravity) {
        android.support.v4.widget.PopupWindowCompat.IMPL.showAsDropDown(popup, anchor, xoff, yoff, gravity);
    }

    /**
     * Sets whether the popup window should overlap its anchor view when
     * displayed as a drop-down.
     *
     * @param overlapAnchor
     * 		Whether the popup should overlap its anchor.
     */
    public static void setOverlapAnchor(android.widget.PopupWindow popupWindow, boolean overlapAnchor) {
        android.support.v4.widget.PopupWindowCompat.IMPL.setOverlapAnchor(popupWindow, overlapAnchor);
    }

    /**
     * Returns whether the popup window should overlap its anchor view when
     * displayed as a drop-down.
     *
     * @return Whether the popup should overlap its anchor.
     */
    public static boolean getOverlapAnchor(android.widget.PopupWindow popupWindow) {
        return android.support.v4.widget.PopupWindowCompat.IMPL.getOverlapAnchor(popupWindow);
    }

    /**
     * Set the layout type for this window. This value will be passed through to
     * {@link WindowManager.LayoutParams#type} therefore the value should match any value
     * {@link WindowManager.LayoutParams#type} accepts.
     *
     * @param layoutType
     * 		Layout type for this window.
     * @see WindowManager.LayoutParams#type
     */
    public static void setWindowLayoutType(android.widget.PopupWindow popupWindow, int layoutType) {
        android.support.v4.widget.PopupWindowCompat.IMPL.setWindowLayoutType(popupWindow, layoutType);
    }

    /**
     * Returns the layout type for this window.
     *
     * @see #setWindowLayoutType(PopupWindow popupWindow, int)
     */
    public static int getWindowLayoutType(android.widget.PopupWindow popupWindow) {
        return android.support.v4.widget.PopupWindowCompat.IMPL.getWindowLayoutType(popupWindow);
    }
}

