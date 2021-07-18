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
package android.support.v7.view.menu;


/**
 * A popup for a menu which will allow multiple submenus to appear in a cascading fashion, side by
 * side.
 */
final class CascadingMenuPopup extends android.support.v7.view.menu.MenuPopup implements android.support.v7.view.menu.MenuPresenter , android.view.View.OnKeyListener , android.widget.PopupWindow.OnDismissListener {
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef({ android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_LEFT, android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT })
    public @interface HorizPosition {}

    static final int HORIZ_POSITION_LEFT = 0;

    static final int HORIZ_POSITION_RIGHT = 1;

    /**
     * Delay between hovering over a menu item with a mouse and receiving
     * side-effects (ex. opening a sub-menu or closing unrelated menus).
     */
    static final int SUBMENU_TIMEOUT_MS = 200;

    private final android.content.Context mContext;

    private final int mMenuMaxWidth;

    private final int mPopupStyleAttr;

    private final int mPopupStyleRes;

    private final boolean mOverflowOnly;

    final android.os.Handler mSubMenuHoverHandler;

    /**
     * List of menus that were added before this popup was shown.
     */
    private final java.util.List<android.support.v7.view.menu.MenuBuilder> mPendingMenus = new java.util.LinkedList<>();

    /**
     * List of open menus. The first item is the root menu and each
     * subsequent item is a direct submenu of the previous item.
     */
    final java.util.List<android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo> mShowingMenus = new java.util.ArrayList<>();

    private final android.view.ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
        @java.lang.Override
        public void onGlobalLayout() {
            // Only move the popup if it's showing and non-modal. We don't want
            // to be moving around the only interactive window, since there's a
            // good chance the user is interacting with it.
            if ((isShowing() && (mShowingMenus.size() > 0)) && (!mShowingMenus.get(0).window.isModal())) {
                final android.view.View anchor = mShownAnchorView;
                if ((anchor == null) || (!anchor.isShown())) {
                    dismiss();
                } else {
                    // Recompute window sizes and positions.
                    for (android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info : mShowingMenus) {
                        info.window.show();
                    }
                }
            }
        }
    };

    private final android.support.v7.widget.MenuItemHoverListener mMenuItemHoverListener = new android.support.v7.widget.MenuItemHoverListener() {
        @java.lang.Override
        public void onItemHoverExit(@android.support.annotation.NonNull
        android.support.v7.view.menu.MenuBuilder menu, @android.support.annotation.NonNull
        android.view.MenuItem item) {
            // If the mouse moves between two windows, hover enter/exit pairs
            // may be received out of order. So, instead of canceling all
            // pending runnables, only cancel runnables for the host menu.
            mSubMenuHoverHandler.removeCallbacksAndMessages(menu);
        }

        @java.lang.Override
        public void onItemHoverEnter(@android.support.annotation.NonNull
        final android.support.v7.view.menu.MenuBuilder menu, @android.support.annotation.NonNull
        final android.view.MenuItem item) {
            // Something new was hovered, cancel all scheduled runnables.
            mSubMenuHoverHandler.removeCallbacksAndMessages(null);
            // Find the position of the hovered menu within the added menus.
            int menuIndex = -1;
            for (int i = 0, count = mShowingMenus.size(); i < count; i++) {
                if (menu == mShowingMenus.get(i).menu) {
                    menuIndex = i;
                    break;
                }
            }
            if (menuIndex == (-1)) {
                return;
            }
            final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo nextInfo;
            final int nextIndex = menuIndex + 1;
            if (nextIndex < mShowingMenus.size()) {
                nextInfo = mShowingMenus.get(nextIndex);
            } else {
                nextInfo = null;
            }
            final java.lang.Runnable runnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    // Close any other submenus that might be open at the
                    // current or a deeper level.
                    if (nextInfo != null) {
                        // Disable exit animations to prevent overlapping
                        // fading out submenus.
                        mShouldCloseImmediately = true;
                        /* closeAllMenus */
                        nextInfo.menu.close(false);
                        mShouldCloseImmediately = false;
                    }
                    // Then open the selected submenu, if there is one.
                    if (item.isEnabled() && item.hasSubMenu()) {
                        menu.performItemAction(item, 0);
                    }
                }
            };
            final long uptimeMillis = android.os.SystemClock.uptimeMillis() + android.support.v7.view.menu.CascadingMenuPopup.SUBMENU_TIMEOUT_MS;
            mSubMenuHoverHandler.postAtTime(runnable, menu, uptimeMillis);
        }
    };

    private int mRawDropDownGravity = android.view.Gravity.NO_GRAVITY;

    private int mDropDownGravity = android.view.Gravity.NO_GRAVITY;

    private android.view.View mAnchorView;

    android.view.View mShownAnchorView;

    private int mLastPosition;

    private boolean mHasXOffset;

    private boolean mHasYOffset;

    private int mXOffset;

    private int mYOffset;

    private boolean mForceShowIcon;

    private boolean mShowTitle;

    private android.support.v7.view.menu.MenuPresenter.Callback mPresenterCallback;

    private android.view.ViewTreeObserver mTreeObserver;

    private android.widget.PopupWindow.OnDismissListener mOnDismissListener;

    /**
     * Whether popup menus should disable exit animations when closing.
     */
    boolean mShouldCloseImmediately;

    /**
     * Initializes a new cascading-capable menu popup.
     *
     * @param anchor
     * 		A parent view to get the {@link android.view.View#getWindowToken()} token from.
     */
    public CascadingMenuPopup(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.view.View anchor, @android.support.annotation.AttrRes
    int popupStyleAttr, @android.support.annotation.StyleRes
    int popupStyleRes, boolean overflowOnly) {
        mContext = context;
        mAnchorView = anchor;
        mPopupStyleAttr = popupStyleAttr;
        mPopupStyleRes = popupStyleRes;
        mOverflowOnly = overflowOnly;
        mForceShowIcon = false;
        mLastPosition = getInitialMenuPosition();
        final android.content.res.Resources res = context.getResources();
        mMenuMaxWidth = java.lang.Math.max(res.getDisplayMetrics().widthPixels / 2, res.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        mSubMenuHoverHandler = new android.os.Handler();
    }

    @java.lang.Override
    public void setForceShowIcon(boolean forceShow) {
        mForceShowIcon = forceShow;
    }

    private android.support.v7.widget.MenuPopupWindow createPopupWindow() {
        android.support.v7.widget.MenuPopupWindow popupWindow = new android.support.v7.widget.MenuPopupWindow(mContext, null, mPopupStyleAttr, mPopupStyleRes);
        popupWindow.setHoverListener(mMenuItemHoverListener);
        popupWindow.setOnItemClickListener(this);
        popupWindow.setOnDismissListener(this);
        popupWindow.setAnchorView(mAnchorView);
        popupWindow.setDropDownGravity(mDropDownGravity);
        popupWindow.setModal(true);
        return popupWindow;
    }

    @java.lang.Override
    public void show() {
        if (isShowing()) {
            return;
        }
        // Display all pending menus.
        for (android.support.v7.view.menu.MenuBuilder menu : mPendingMenus) {
            showMenu(menu);
        }
        mPendingMenus.clear();
        mShownAnchorView = mAnchorView;
        if (mShownAnchorView != null) {
            final boolean addGlobalListener = mTreeObserver == null;
            mTreeObserver = mShownAnchorView.getViewTreeObserver();// Refresh to latest

            if (addGlobalListener) {
                mTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
            }
        }
    }

    @java.lang.Override
    public void dismiss() {
        // Need to make another list to avoid a concurrent modification
        // exception, as #onDismiss may clear mPopupWindows while we are
        // iterating. Remove from the last added menu so that the callbacks
        // are received in order from foreground to background.
        final int length = mShowingMenus.size();
        if (length > 0) {
            final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo[] addedMenus = mShowingMenus.toArray(new android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo[length]);
            for (int i = length - 1; i >= 0; i--) {
                final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info = addedMenus[i];
                if (info.window.isShowing()) {
                    info.window.dismiss();
                }
            }
        }
    }

    @java.lang.Override
    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        if ((event.getAction() == android.view.KeyEvent.ACTION_UP) && (keyCode == android.view.KeyEvent.KEYCODE_MENU)) {
            dismiss();
            return true;
        }
        return false;
    }

    /**
     * Determines the proper initial menu position for the current LTR/RTL configuration.
     *
     * @return The initial position.
     */
    @android.support.v7.view.menu.CascadingMenuPopup.HorizPosition
    private int getInitialMenuPosition() {
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(mAnchorView);
        return layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL ? android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_LEFT : android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT;
    }

    /**
     * Determines whether the next submenu (of the given width) should display on the right or on
     * the left of the most recent menu.
     *
     * @param nextMenuWidth
     * 		Width of the next submenu to display.
     * @return The position to display it.
     */
    @android.support.v7.view.menu.CascadingMenuPopup.HorizPosition
    private int getNextMenuPosition(int nextMenuWidth) {
        android.widget.ListView lastListView = mShowingMenus.get(mShowingMenus.size() - 1).getListView();
        final int[] screenLocation = new int[2];
        lastListView.getLocationOnScreen(screenLocation);
        final android.graphics.Rect displayFrame = new android.graphics.Rect();
        mShownAnchorView.getWindowVisibleDisplayFrame(displayFrame);
        if (mLastPosition == android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT) {
            final int right = (screenLocation[0] + lastListView.getWidth()) + nextMenuWidth;
            if (right > displayFrame.right) {
                return android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_LEFT;
            }
            return android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT;
        } else {
            // LEFT
            final int left = screenLocation[0] - nextMenuWidth;
            if (left < 0) {
                return android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT;
            }
            return android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_LEFT;
        }
    }

    @java.lang.Override
    public void addMenu(android.support.v7.view.menu.MenuBuilder menu) {
        menu.addMenuPresenter(this, mContext);
        if (isShowing()) {
            showMenu(menu);
        } else {
            mPendingMenus.add(menu);
        }
    }

    /**
     * Prepares and shows the specified menu immediately.
     *
     * @param menu
     * 		the menu to show
     */
    private void showMenu(@android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder menu) {
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        final android.support.v7.view.menu.MenuAdapter adapter = new android.support.v7.view.menu.MenuAdapter(menu, inflater, mOverflowOnly);
        // Apply "force show icon" setting. There are 3 cases:
        // (1) This is the top level menu and icon spacing is forced. Add spacing.
        // (2) This is a submenu. Add spacing if any of the visible menu items has an icon.
        // (3) This is the top level menu and icon spacing isn't forced. Do not add spacing.
        if ((!isShowing()) && mForceShowIcon) {
            // Case 1
            adapter.setForceShowIcon(true);
        } else
            if (isShowing()) {
                // Case 2
                adapter.setForceShowIcon(android.support.v7.view.menu.MenuPopup.shouldPreserveIconSpacing(menu));
            }

        // Case 3: Else, don't allow spacing for icons (default behavior; do nothing).
        final int menuWidth = android.support.v7.view.menu.MenuPopup.measureIndividualMenuWidth(adapter, null, mContext, mMenuMaxWidth);
        final android.support.v7.widget.MenuPopupWindow popupWindow = createPopupWindow();
        popupWindow.setAdapter(adapter);
        popupWindow.setContentWidth(menuWidth);
        popupWindow.setDropDownGravity(mDropDownGravity);
        final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo parentInfo;
        final android.view.View parentView;
        if (mShowingMenus.size() > 0) {
            parentInfo = mShowingMenus.get(mShowingMenus.size() - 1);
            parentView = findParentViewForSubmenu(parentInfo, menu);
        } else {
            parentInfo = null;
            parentView = null;
        }
        if (parentView != null) {
            // This menu is a cascading submenu anchored to a parent view.
            popupWindow.setTouchModal(false);
            popupWindow.setEnterTransition(null);
            @android.support.v7.view.menu.CascadingMenuPopup.HorizPosition
            final int nextMenuPosition = getNextMenuPosition(menuWidth);
            final boolean showOnRight = nextMenuPosition == android.support.v7.view.menu.CascadingMenuPopup.HORIZ_POSITION_RIGHT;
            mLastPosition = nextMenuPosition;
            final int[] tempLocation = new int[2];
            // This popup menu will be positioned relative to the top-left edge
            // of the view representing its parent menu.
            parentView.getLocationInWindow(tempLocation);
            final int parentOffsetLeft = parentInfo.window.getHorizontalOffset() + tempLocation[0];
            final int parentOffsetTop = parentInfo.window.getVerticalOffset() + tempLocation[1];
            // By now, mDropDownGravity is the resolved absolute gravity, so
            // this should work in both LTR and RTL.
            final int x;
            if ((mDropDownGravity & android.view.Gravity.RIGHT) == android.view.Gravity.RIGHT) {
                if (showOnRight) {
                    x = parentOffsetLeft + menuWidth;
                } else {
                    x = parentOffsetLeft - parentView.getWidth();
                }
            } else {
                if (showOnRight) {
                    x = parentOffsetLeft + parentView.getWidth();
                } else {
                    x = parentOffsetLeft - menuWidth;
                }
            }
            popupWindow.setHorizontalOffset(x);
            final int y = parentOffsetTop;
            popupWindow.setVerticalOffset(y);
        } else {
            if (mHasXOffset) {
                popupWindow.setHorizontalOffset(mXOffset);
            }
            if (mHasYOffset) {
                popupWindow.setVerticalOffset(mYOffset);
            }
            final android.graphics.Rect epicenterBounds = getEpicenterBounds();
            popupWindow.setEpicenterBounds(epicenterBounds);
        }
        final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo menuInfo = new android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo(popupWindow, menu, mLastPosition);
        mShowingMenus.add(menuInfo);
        popupWindow.show();
        // If this is the root menu, show the title if requested.
        if (((parentInfo == null) && mShowTitle) && (menu.getHeaderTitle() != null)) {
            final android.widget.ListView listView = popupWindow.getListView();
            final android.widget.FrameLayout titleItemView = ((android.widget.FrameLayout) (inflater.inflate(R.layout.abc_popup_menu_header_item_layout, listView, false)));
            final android.widget.TextView titleView = ((android.widget.TextView) (titleItemView.findViewById(android.R.id.title)));
            titleItemView.setEnabled(false);
            titleView.setText(menu.getHeaderTitle());
            listView.addHeaderView(titleItemView, null, false);
            // Show again to update the title.
            popupWindow.show();
        }
    }

    /**
     * Returns the menu item within the specified parent menu that owns
     * specified submenu.
     *
     * @param parent
     * 		the parent menu
     * @param submenu
     * 		the submenu for which the index should be returned
     * @return the menu item that owns the submenu, or {@code null} if not
    present
     */
    private android.view.MenuItem findMenuItemForSubmenu(@android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder parent, @android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder submenu) {
        for (int i = 0, count = parent.size(); i < count; i++) {
            final android.view.MenuItem item = parent.getItem(i);
            if (item.hasSubMenu() && (submenu == item.getSubMenu())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Attempts to find the view for the menu item that owns the specified
     * submenu.
     *
     * @param parentInfo
     * 		info for the parent menu
     * @param submenu
     * 		the submenu whose parent view should be obtained
     * @return the parent view, or {@code null} if one could not be found
     */
    @android.support.annotation.Nullable
    private android.view.View findParentViewForSubmenu(@android.support.annotation.NonNull
    android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo parentInfo, @android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder submenu) {
        final android.view.MenuItem owner = findMenuItemForSubmenu(parentInfo.menu, submenu);
        if (owner == null) {
            // Couldn't find the submenu owner.
            return null;
        }
        // The adapter may be wrapped. Adjust the index if necessary.
        final int headersCount;
        final android.support.v7.view.menu.MenuAdapter menuAdapter;
        final android.widget.ListView listView = parentInfo.getListView();
        final android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter instanceof android.widget.HeaderViewListAdapter) {
            final android.widget.HeaderViewListAdapter headerAdapter = ((android.widget.HeaderViewListAdapter) (listAdapter));
            headersCount = headerAdapter.getHeadersCount();
            menuAdapter = ((android.support.v7.view.menu.MenuAdapter) (headerAdapter.getWrappedAdapter()));
        } else {
            headersCount = 0;
            menuAdapter = ((android.support.v7.view.menu.MenuAdapter) (listAdapter));
        }
        // Find the index within the menu adapter's data set of the menu item.
        int ownerPosition = android.widget.AbsListView.INVALID_POSITION;
        for (int i = 0, count = menuAdapter.getCount(); i < count; i++) {
            if (owner == menuAdapter.getItem(i)) {
                ownerPosition = i;
                break;
            }
        }
        if (ownerPosition == android.widget.AbsListView.INVALID_POSITION) {
            // Couldn't find the owner within the menu adapter.
            return null;
        }
        // Adjust the index for the adapter used to display views.
        ownerPosition += headersCount;
        // Adjust the index for the visible views.
        final int ownerViewPosition = ownerPosition - listView.getFirstVisiblePosition();
        if ((ownerViewPosition < 0) || (ownerViewPosition >= listView.getChildCount())) {
            // Not visible on screen.
            return null;
        }
        return listView.getChildAt(ownerViewPosition);
    }

    /**
     *
     *
     * @return {@code true} if the popup is currently showing, {@code false} otherwise.
     */
    @java.lang.Override
    public boolean isShowing() {
        return (mShowingMenus.size() > 0) && mShowingMenus.get(0).window.isShowing();
    }

    /**
     * Called when one or more of the popup windows was dismissed.
     */
    @java.lang.Override
    public void onDismiss() {
        // The dismiss listener doesn't pass the calling window, so walk
        // through the stack to figure out which one was just dismissed.
        android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo dismissedInfo = null;
        for (int i = 0, count = mShowingMenus.size(); i < count; i++) {
            final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info = mShowingMenus.get(i);
            if (!info.window.isShowing()) {
                dismissedInfo = info;
                break;
            }
        }
        // Close all menus starting from the dismissed menu, passing false
        // since we are manually closing only a subset of windows.
        if (dismissedInfo != null) {
            dismissedInfo.menu.close(false);
        }
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        for (android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info : mShowingMenus) {
            android.support.v7.view.menu.MenuPopup.toMenuAdapter(info.getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mPresenterCallback = cb;
    }

    @java.lang.Override
    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        // Don't allow double-opening of the same submenu.
        for (android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info : mShowingMenus) {
            if (subMenu == info.menu) {
                // Just re-focus that one.
                info.getListView().requestFocus();
                return true;
            }
        }
        if (subMenu.hasVisibleItems()) {
            addMenu(subMenu);
            if (mPresenterCallback != null) {
                mPresenterCallback.onOpenSubMenu(subMenu);
            }
            return true;
        }
        return false;
    }

    /**
     * Finds the index of the specified menu within the list of added menus.
     *
     * @param menu
     * 		the menu to find
     * @return the index of the menu, or {@code -1} if not present
     */
    private int findIndexOfAddedMenu(@android.support.annotation.NonNull
    android.support.v7.view.menu.MenuBuilder menu) {
        for (int i = 0, count = mShowingMenus.size(); i < count; i++) {
            final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info = mShowingMenus.get(i);
            if (menu == info.menu) {
                return i;
            }
        }
        return -1;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        final int menuIndex = findIndexOfAddedMenu(menu);
        if (menuIndex < 0) {
            return;
        }
        // Recursively close descendant menus.
        final int nextMenuIndex = menuIndex + 1;
        if (nextMenuIndex < mShowingMenus.size()) {
            final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo childInfo = mShowingMenus.get(nextMenuIndex);
            /* closeAllMenus */
            childInfo.menu.close(false);
        }
        // Close the target menu.
        final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo info = mShowingMenus.remove(menuIndex);
        info.menu.removeMenuPresenter(this);
        if (mShouldCloseImmediately) {
            // Disable all exit animations.
            info.window.setExitTransition(null);
            info.window.setAnimationStyle(0);
        }
        info.window.dismiss();
        final int count = mShowingMenus.size();
        if (count > 0) {
            mLastPosition = mShowingMenus.get(count - 1).position;
        } else {
            mLastPosition = getInitialMenuPosition();
        }
        if (count == 0) {
            // This was the last window. Clean up.
            dismiss();
            if (mPresenterCallback != null) {
                mPresenterCallback.onCloseMenu(menu, true);
            }
            if (mTreeObserver != null) {
                if (mTreeObserver.isAlive()) {
                    mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
                }
                mTreeObserver = null;
            }
            // If every [sub]menu was dismissed, that means the whole thing was
            // dismissed, so notify the owner.
            mOnDismissListener.onDismiss();
        } else
            if (allMenusAreClosing) {
                // Close all menus starting from the root. This will recursively
                // close any remaining menus, so we don't need to propagate the
                // "closeAllMenus" flag. The last window will clean up.
                final android.support.v7.view.menu.CascadingMenuPopup.CascadingMenuInfo rootInfo = mShowingMenus.get(0);
                /* closeAllMenus */
                rootInfo.menu.close(false);
            }

    }

    @java.lang.Override
    public boolean flagActionItems() {
        return false;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        return null;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
    }

    @java.lang.Override
    public void setGravity(int dropDownGravity) {
        if (mRawDropDownGravity != dropDownGravity) {
            mRawDropDownGravity = dropDownGravity;
            mDropDownGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(dropDownGravity, android.support.v4.view.ViewCompat.getLayoutDirection(mAnchorView));
        }
    }

    @java.lang.Override
    public void setAnchorView(@android.support.annotation.NonNull
    android.view.View anchor) {
        if (mAnchorView != anchor) {
            mAnchorView = anchor;
            // Gravity resolution may have changed, update from raw gravity.
            mDropDownGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(mRawDropDownGravity, android.support.v4.view.ViewCompat.getLayoutDirection(mAnchorView));
        }
    }

    @java.lang.Override
    public void setOnDismissListener(android.widget.PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    @java.lang.Override
    public android.widget.ListView getListView() {
        return mShowingMenus.isEmpty() ? null : mShowingMenus.get(mShowingMenus.size() - 1).getListView();
    }

    @java.lang.Override
    public void setHorizontalOffset(int x) {
        mHasXOffset = true;
        mXOffset = x;
    }

    @java.lang.Override
    public void setVerticalOffset(int y) {
        mHasYOffset = true;
        mYOffset = y;
    }

    @java.lang.Override
    public void setShowTitle(boolean showTitle) {
        mShowTitle = showTitle;
    }

    @java.lang.Override
    protected boolean closeMenuOnSubMenuOpened() {
        // Since we're cascading, we don't want the parent menu to be closed when a submenu
        // is opened
        return false;
    }

    private static class CascadingMenuInfo {
        public final android.support.v7.widget.MenuPopupWindow window;

        public final android.support.v7.view.menu.MenuBuilder menu;

        public final int position;

        public CascadingMenuInfo(@android.support.annotation.NonNull
        android.support.v7.widget.MenuPopupWindow window, @android.support.annotation.NonNull
        android.support.v7.view.menu.MenuBuilder menu, int position) {
            this.window = window;
            this.menu = menu;
            this.position = position;
        }

        public android.widget.ListView getListView() {
            return window.getListView();
        }
    }
}

