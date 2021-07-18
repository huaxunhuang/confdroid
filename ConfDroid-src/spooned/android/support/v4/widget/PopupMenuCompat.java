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
 * Helper for accessing features in PopupMenu introduced after API level 4 in a
 * backwards compatible fashion.
 */
public final class PopupMenuCompat {
    /**
     * Interface for the full API.
     */
    interface PopupMenuImpl {
        public android.view.View.OnTouchListener getDragToOpenListener(java.lang.Object popupMenu);
    }

    /**
     * Interface implementation that doesn't use anything above v4 APIs.
     */
    static class BasePopupMenuImpl implements android.support.v4.widget.PopupMenuCompat.PopupMenuImpl {
        @java.lang.Override
        public android.view.View.OnTouchListener getDragToOpenListener(java.lang.Object popupMenu) {
            return null;
        }
    }

    /**
     * Interface implementation for devices with at least KitKat APIs.
     */
    static class KitKatPopupMenuImpl extends android.support.v4.widget.PopupMenuCompat.BasePopupMenuImpl {
        @java.lang.Override
        public android.view.View.OnTouchListener getDragToOpenListener(java.lang.Object popupMenu) {
            return android.support.v4.widget.PopupMenuCompatKitKat.getDragToOpenListener(popupMenu);
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final android.support.v4.widget.PopupMenuCompat.PopupMenuImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 19) {
            IMPL = new android.support.v4.widget.PopupMenuCompat.KitKatPopupMenuImpl();
        } else {
            IMPL = new android.support.v4.widget.PopupMenuCompat.BasePopupMenuImpl();
        }
    }

    private PopupMenuCompat() {
        // This class is not publicly instantiable.
    }

    /**
     * On API {@link android.os.Build.VERSION_CODES#KITKAT} and higher, returns
     * an {@link OnTouchListener} that can be added to the anchor view to
     * implement drag-to-open behavior.
     * <p>
     * When the listener is set on a view, touching that view and dragging
     * outside of its bounds will open the popup window. Lifting will select the
     * currently touched list item.
     * <p>
     * Example usage:
     * <pre>
     * PopupMenu myPopup = new PopupMenu(context, myAnchor);
     * myAnchor.setOnTouchListener(PopupMenuCompat.getDragToOpenListener(myPopup));
     * </pre>
     *
     * @param popupMenu
     * 		the PopupMenu against which to invoke the method
     * @return a touch listener that controls drag-to-open behavior, or null on
    unsupported APIs
     */
    public static android.view.View.OnTouchListener getDragToOpenListener(java.lang.Object popupMenu) {
        return android.support.v4.widget.PopupMenuCompat.IMPL.getDragToOpenListener(popupMenu);
    }
}

