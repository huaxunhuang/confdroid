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
 * limitations under the License
 */
package android.support.v7.widget;


/**
 * A MenuPopupWindow represents the popup window for menu.
 *
 * MenuPopupWindow is mostly same as ListPopupWindow, but it has customized
 * behaviors specific to menus,
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class MenuPopupWindow extends android.support.v7.widget.ListPopupWindow implements android.support.v7.widget.MenuItemHoverListener {
    private static final java.lang.String TAG = "MenuPopupWindow";

    private static java.lang.reflect.Method sSetTouchModalMethod;

    static {
        try {
            android.support.v7.widget.MenuPopupWindow.sSetTouchModalMethod = android.widget.PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
        } catch (java.lang.NoSuchMethodException e) {
            android.util.Log.i(android.support.v7.widget.MenuPopupWindow.TAG, "Could not find method setTouchModal() on PopupWindow. Oh well.");
        }
    }

    private android.support.v7.widget.MenuItemHoverListener mHoverListener;

    public MenuPopupWindow(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @java.lang.Override
    android.support.v7.widget.DropDownListView createDropDownListView(android.content.Context context, boolean hijackFocus) {
        android.support.v7.widget.MenuPopupWindow.MenuDropDownListView view = new android.support.v7.widget.MenuPopupWindow.MenuDropDownListView(context, hijackFocus);
        view.setHoverListener(this);
        return view;
    }

    public void setEnterTransition(java.lang.Object enterTransition) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            mPopup.setEnterTransition(((android.transition.Transition) (enterTransition)));
        }
    }

    public void setExitTransition(java.lang.Object exitTransition) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            mPopup.setExitTransition(((android.transition.Transition) (exitTransition)));
        }
    }

    public void setHoverListener(android.support.v7.widget.MenuItemHoverListener hoverListener) {
        mHoverListener = hoverListener;
    }

    /**
     * Set whether this window is touch modal or if outside touches will be sent to
     * other windows behind it.
     */
    public void setTouchModal(final boolean touchModal) {
        if (android.support.v7.widget.MenuPopupWindow.sSetTouchModalMethod != null) {
            try {
                android.support.v7.widget.MenuPopupWindow.sSetTouchModalMethod.invoke(mPopup, touchModal);
            } catch (java.lang.Exception e) {
                android.util.Log.i(android.support.v7.widget.MenuPopupWindow.TAG, "Could not invoke setTouchModal() on PopupWindow. Oh well.");
            }
        }
    }

    @java.lang.Override
    public void onItemHoverEnter(@android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder menu, @android.support.annotation.NonNull
    android.view.MenuItem item) {
        // Forward up the chain
        if (mHoverListener != null) {
            mHoverListener.onItemHoverEnter(menu, item);
        }
    }

    @java.lang.Override
    public void onItemHoverExit(@android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder menu, @android.support.annotation.NonNull
    android.view.MenuItem item) {
        // Forward up the chain
        if (mHoverListener != null) {
            mHoverListener.onItemHoverExit(menu, item);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class MenuDropDownListView extends android.support.v7.widget.DropDownListView {
        final int mAdvanceKey;

        final int mRetreatKey;

        private android.support.v7.widget.MenuItemHoverListener mHoverListener;

        private android.view.MenuItem mHoveredMenuItem;

        public MenuDropDownListView(android.content.Context context, boolean hijackFocus) {
            super(context, hijackFocus);
            final android.content.res.Resources res = context.getResources();
            final android.content.res.Configuration config = res.getConfiguration();
            if ((android.os.Build.VERSION.SDK_INT >= 17) && (android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL == config.getLayoutDirection())) {
                mAdvanceKey = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
                mRetreatKey = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
            } else {
                mAdvanceKey = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
                mRetreatKey = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
            }
        }

        public void setHoverListener(android.support.v7.widget.MenuItemHoverListener hoverListener) {
            mHoverListener = hoverListener;
        }

        public void clearSelection() {
            setSelection(android.support.v7.widget.ListViewCompat.INVALID_POSITION);
        }

        @java.lang.Override
        public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
            android.support.v7.view.menu.ListMenuItemView selectedItem = ((android.support.v7.view.menu.ListMenuItemView) (getSelectedView()));
            if ((selectedItem != null) && (keyCode == mAdvanceKey)) {
                if (selectedItem.isEnabled() && selectedItem.getItemData().hasSubMenu()) {
                    performItemClick(selectedItem, getSelectedItemPosition(), getSelectedItemId());
                }
                return true;
            } else
                if ((selectedItem != null) && (keyCode == mRetreatKey)) {
                    setSelection(android.support.v7.widget.ListViewCompat.INVALID_POSITION);
                    // Close only the top-level menu.
                    /* closeAllMenus */
                    ((android.support.v7.view.menu.MenuAdapter) (getAdapter())).getAdapterMenu().close(false);
                    return true;
                }

            return super.onKeyDown(keyCode, event);
        }

        @java.lang.Override
        public boolean onHoverEvent(android.view.MotionEvent ev) {
            // Dispatch any changes in hovered item index to the listener.
            if (mHoverListener != null) {
                // The adapter may be wrapped. Adjust the index if necessary.
                final int headersCount;
                final android.support.v7.view.menu.MenuAdapter menuAdapter;
                final android.widget.ListAdapter adapter = getAdapter();
                if (adapter instanceof android.widget.HeaderViewListAdapter) {
                    final android.widget.HeaderViewListAdapter headerAdapter = ((android.widget.HeaderViewListAdapter) (adapter));
                    headersCount = headerAdapter.getHeadersCount();
                    menuAdapter = ((android.support.v7.view.menu.MenuAdapter) (headerAdapter.getWrappedAdapter()));
                } else {
                    headersCount = 0;
                    menuAdapter = ((android.support.v7.view.menu.MenuAdapter) (adapter));
                }
                // Find the menu item for the view at the event coordinates.
                android.view.MenuItem menuItem = null;
                if (ev.getAction() != android.view.MotionEvent.ACTION_HOVER_EXIT) {
                    final int position = pointToPosition(((int) (ev.getX())), ((int) (ev.getY())));
                    if (position != android.support.v7.widget.ListViewCompat.INVALID_POSITION) {
                        final int itemPosition = position - headersCount;
                        if ((itemPosition >= 0) && (itemPosition < menuAdapter.getCount())) {
                            menuItem = menuAdapter.getItem(itemPosition);
                        }
                    }
                }
                final android.view.MenuItem oldMenuItem = mHoveredMenuItem;
                if (oldMenuItem != menuItem) {
                    final android.support.v7.view.menu.MenuBuilder menu = menuAdapter.getAdapterMenu();
                    if (oldMenuItem != null) {
                        mHoverListener.onItemHoverExit(menu, oldMenuItem);
                    }
                    mHoveredMenuItem = menuItem;
                    if (menuItem != null) {
                        mHoverListener.onItemHoverEnter(menu, menuItem);
                    }
                }
            }
            return super.onHoverEvent(ev);
        }
    }
}

