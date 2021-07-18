/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * Static library support version of the framework's {@link android.widget.PopupMenu}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation. See the framework SDK
 * documentation for a class overview.
 */
public class PopupMenu {
    private final android.content.Context mContext;

    private final android.support.v7.view.menu.MenuBuilder mMenu;

    private final android.view.View mAnchor;

    final android.support.v7.view.menu.MenuPopupHelper mPopup;

    android.support.v7.widget.PopupMenu.OnMenuItemClickListener mMenuItemClickListener;

    android.support.v7.widget.PopupMenu.OnDismissListener mOnDismissListener;

    private android.view.View.OnTouchListener mDragListener;

    /**
     * Constructor to create a new popup menu with an anchor view.
     *
     * @param context
     * 		Context the popup menu is running in, through which it
     * 		can access the current theme, resources, etc.
     * @param anchor
     * 		Anchor view for this popup. The popup will appear below
     * 		the anchor if there is room, or above it if there is not.
     */
    public PopupMenu(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.view.View anchor) {
        this(context, anchor, android.view.Gravity.NO_GRAVITY);
    }

    /**
     * Constructor to create a new popup menu with an anchor view and alignment
     * gravity.
     *
     * @param context
     * 		Context the popup menu is running in, through which it
     * 		can access the current theme, resources, etc.
     * @param anchor
     * 		Anchor view for this popup. The popup will appear below
     * 		the anchor if there is room, or above it if there is not.
     * @param gravity
     * 		The {@link Gravity} value for aligning the popup with its
     * 		anchor.
     */
    public PopupMenu(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.view.View anchor, int gravity) {
        this(context, anchor, gravity, R.attr.popupMenuStyle, 0);
    }

    /**
     * Constructor a create a new popup menu with a specific style.
     *
     * @param context
     * 		Context the popup menu is running in, through which it
     * 		can access the current theme, resources, etc.
     * @param anchor
     * 		Anchor view for this popup. The popup will appear below
     * 		the anchor if there is room, or above it if there is not.
     * @param gravity
     * 		The {@link Gravity} value for aligning the popup with its
     * 		anchor.
     * @param popupStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the popup window. Can be 0 to not look for defaults.
     * @param popupStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the popup window, used only if
     * 		popupStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public PopupMenu(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.view.View anchor, int gravity, @android.support.annotation.AttrRes
    int popupStyleAttr, @android.support.annotation.StyleRes
    int popupStyleRes) {
        mContext = context;
        mAnchor = anchor;
        mMenu = new android.support.v7.view.menu.MenuBuilder(context);
        mMenu.setCallback(new android.support.v7.view.menu.MenuBuilder.Callback() {
            @java.lang.Override
            public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
                if (mMenuItemClickListener != null) {
                    return mMenuItemClickListener.onMenuItemClick(item);
                }
                return false;
            }

            @java.lang.Override
            public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
            }
        });
        mPopup = new android.support.v7.view.menu.MenuPopupHelper(context, mMenu, anchor, false, popupStyleAttr, popupStyleRes);
        mPopup.setGravity(gravity);
        mPopup.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
            @java.lang.Override
            public void onDismiss() {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss(android.support.v7.widget.PopupMenu.this);
                }
            }
        });
    }

    /**
     * Sets the gravity used to align the popup window to its anchor view.
     * <p>
     * If the popup is showing, calling this method will take effect only
     * the next time the popup is shown.
     *
     * @param gravity
     * 		the gravity used to align the popup window
     * @see #getGravity()
     */
    public void setGravity(int gravity) {
        mPopup.setGravity(gravity);
    }

    /**
     *
     *
     * @return the gravity used to align the popup window to its anchor view
     * @see #setGravity(int)
     */
    public int getGravity() {
        return mPopup.getGravity();
    }

    /**
     * Returns an {@link View.OnTouchListener} that can be added to the anchor view
     * to implement drag-to-open behavior.
     * <p>
     * When the listener is set on a view, touching that view and dragging
     * outside of its bounds will open the popup window. Lifting will select
     * the currently touched list item.
     * <p>
     * Example usage:
     * <pre>
     * PopupMenu myPopup = new PopupMenu(context, myAnchor);
     * myAnchor.setOnTouchListener(myPopup.getDragToOpenListener());
     * </pre>
     *
     * @return a touch listener that controls drag-to-open behavior
     */
    @android.support.annotation.NonNull
    public android.view.View.OnTouchListener getDragToOpenListener() {
        if (mDragListener == null) {
            mDragListener = new android.support.v7.widget.ForwardingListener(mAnchor) {
                @java.lang.Override
                protected boolean onForwardingStarted() {
                    show();
                    return true;
                }

                @java.lang.Override
                protected boolean onForwardingStopped() {
                    dismiss();
                    return true;
                }

                @java.lang.Override
                public android.support.v7.view.menu.ShowableListMenu getPopup() {
                    // This will be null until show() is called.
                    return mPopup.getPopup();
                }
            };
        }
        return mDragListener;
    }

    /**
     * Returns the {@link Menu} associated with this popup. Populate the
     * returned Menu with items before calling {@link #show()}.
     *
     * @return the {@link Menu} associated with this popup
     * @see #show()
     * @see #getMenuInflater()
     */
    @android.support.annotation.NonNull
    public android.view.Menu getMenu() {
        return mMenu;
    }

    /**
     *
     *
     * @return a {@link MenuInflater} that can be used to inflate menu items
    from XML into the menu returned by {@link #getMenu()}
     * @see #getMenu()
     */
    @android.support.annotation.NonNull
    public android.view.MenuInflater getMenuInflater() {
        return new android.support.v7.view.SupportMenuInflater(mContext);
    }

    /**
     * Inflate a menu resource into this PopupMenu. This is equivalent to
     * calling {@code popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu())}.
     *
     * @param menuRes
     * 		Menu resource to inflate
     */
    public void inflate(@android.support.annotation.MenuRes
    int menuRes) {
        getMenuInflater().inflate(menuRes, mMenu);
    }

    /**
     * Show the menu popup anchored to the view specified during construction.
     *
     * @see #dismiss()
     */
    public void show() {
        mPopup.show();
    }

    /**
     * Dismiss the menu popup.
     *
     * @see #show()
     */
    public void dismiss() {
        mPopup.dismiss();
    }

    /**
     * Sets a listener that will be notified when the user selects an item from
     * the menu.
     *
     * @param listener
     * 		the listener to notify
     */
    public void setOnMenuItemClickListener(@android.support.annotation.Nullable
    android.support.v7.widget.PopupMenu.OnMenuItemClickListener listener) {
        mMenuItemClickListener = listener;
    }

    /**
     * Sets a listener that will be notified when this menu is dismissed.
     *
     * @param listener
     * 		the listener to notify
     */
    public void setOnDismissListener(@android.support.annotation.Nullable
    android.support.v7.widget.PopupMenu.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /**
     * Interface responsible for receiving menu item click events if the items
     * themselves do not have individual item click listeners.
     */
    public interface OnMenuItemClickListener {
        /**
         * This method will be invoked when a menu item is clicked if the item
         * itself did not already handle the event.
         *
         * @param item
         * 		the menu item that was clicked
         * @return {@code true} if the event was handled, {@code false}
        otherwise
         */
        boolean onMenuItemClick(android.view.MenuItem item);
    }

    /**
     * Callback interface used to notify the application that the menu has closed.
     */
    public interface OnDismissListener {
        /**
         * Called when the associated menu has been dismissed.
         *
         * @param menu
         * 		the popup menu that was dismissed
         */
        void onDismiss(android.support.v7.widget.PopupMenu menu);
    }
}

