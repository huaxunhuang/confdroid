/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.widget;


/**
 * A MenuPopupWindow represents the popup window for menu.
 *
 * MenuPopupWindow is mostly same as ListPopupWindow, but it has customized
 * behaviors specific to menus,
 *
 * @unknown 
 */
public class MenuPopupWindow extends android.widget.ListPopupWindow implements android.widget.MenuItemHoverListener {
    private android.widget.MenuItemHoverListener mHoverListener;

    public MenuPopupWindow(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @java.lang.Override
    android.widget.DropDownListView createDropDownListView(android.content.Context context, boolean hijackFocus) {
        android.widget.MenuPopupWindow.MenuDropDownListView view = new android.widget.MenuPopupWindow.MenuDropDownListView(context, hijackFocus);
        view.setHoverListener(this);
        return view;
    }

    public void setEnterTransition(android.transition.Transition enterTransition) {
        mPopup.setEnterTransition(enterTransition);
    }

    public void setExitTransition(android.transition.Transition exitTransition) {
        mPopup.setExitTransition(exitTransition);
    }

    public void setHoverListener(android.widget.MenuItemHoverListener hoverListener) {
        mHoverListener = hoverListener;
    }

    /**
     * Set whether this window is touch modal or if outside touches will be sent to
     * other windows behind it.
     */
    public void setTouchModal(boolean touchModal) {
        mPopup.setTouchModal(touchModal);
    }

    @java.lang.Override
    public void onItemHoverEnter(@android.annotation.NonNull
    com.android.internal.view.menu.MenuBuilder menu, @android.annotation.NonNull
    android.view.MenuItem item) {
        // Forward up the chain
        if (mHoverListener != null) {
            mHoverListener.onItemHoverEnter(menu, item);
        }
    }

    @java.lang.Override
    public void onItemHoverExit(@android.annotation.NonNull
    com.android.internal.view.menu.MenuBuilder menu, @android.annotation.NonNull
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
    public static class MenuDropDownListView extends android.widget.DropDownListView {
        final int mAdvanceKey;

        final int mRetreatKey;

        private android.widget.MenuItemHoverListener mHoverListener;

        private android.view.MenuItem mHoveredMenuItem;

        public MenuDropDownListView(android.content.Context context, boolean hijackFocus) {
            super(context, hijackFocus);
            final android.content.res.Resources res = context.getResources();
            final android.content.res.Configuration config = res.getConfiguration();
            if (config.getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) {
                mAdvanceKey = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
                mRetreatKey = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
            } else {
                mAdvanceKey = android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
                mRetreatKey = android.view.KeyEvent.KEYCODE_DPAD_LEFT;
            }
        }

        public void setHoverListener(android.widget.MenuItemHoverListener hoverListener) {
            mHoverListener = hoverListener;
        }

        public void clearSelection() {
            setSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
            setNextSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
        }

        @java.lang.Override
        public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
            com.android.internal.view.menu.ListMenuItemView selectedItem = ((com.android.internal.view.menu.ListMenuItemView) (getSelectedView()));
            if ((selectedItem != null) && (keyCode == mAdvanceKey)) {
                if (selectedItem.isEnabled() && selectedItem.getItemData().hasSubMenu()) {
                    performItemClick(selectedItem, getSelectedItemPosition(), getSelectedItemId());
                }
                return true;
            } else
                if ((selectedItem != null) && (keyCode == mRetreatKey)) {
                    setSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
                    setNextSelectedPositionInt(android.widget.AdapterView.INVALID_POSITION);
                    // Close only the top-level menu.
                    /* closeAllMenus */
                    ((com.android.internal.view.menu.MenuAdapter) (getAdapter())).getAdapterMenu().close(false);
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
                final com.android.internal.view.menu.MenuAdapter menuAdapter;
                final android.widget.ListAdapter adapter = getAdapter();
                if (adapter instanceof android.widget.HeaderViewListAdapter) {
                    final android.widget.HeaderViewListAdapter headerAdapter = ((android.widget.HeaderViewListAdapter) (adapter));
                    headersCount = headerAdapter.getHeadersCount();
                    menuAdapter = ((com.android.internal.view.menu.MenuAdapter) (headerAdapter.getWrappedAdapter()));
                } else {
                    headersCount = 0;
                    menuAdapter = ((com.android.internal.view.menu.MenuAdapter) (adapter));
                }
                // Find the menu item for the view at the event coordinates.
                android.view.MenuItem menuItem = null;
                if (ev.getAction() != android.view.MotionEvent.ACTION_HOVER_EXIT) {
                    final int position = pointToPosition(((int) (ev.getX())), ((int) (ev.getY())));
                    if (position != android.widget.AdapterView.INVALID_POSITION) {
                        final int itemPosition = position - headersCount;
                        if ((itemPosition >= 0) && (itemPosition < menuAdapter.getCount())) {
                            menuItem = menuAdapter.getItem(itemPosition);
                        }
                    }
                }
                final android.view.MenuItem oldMenuItem = mHoveredMenuItem;
                if (oldMenuItem != menuItem) {
                    final com.android.internal.view.menu.MenuBuilder menu = menuAdapter.getAdapterMenu();
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

