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
 * MenuPresenter for building action menus as seen in the action bar and action modes.
 */
class ActionMenuPresenter extends android.support.v7.view.menu.BaseMenuPresenter implements android.support.v4.view.ActionProvider.SubUiVisibilityListener {
    private static final java.lang.String TAG = "ActionMenuPresenter";

    android.support.v7.widget.ActionMenuPresenter.OverflowMenuButton mOverflowButton;

    private android.graphics.drawable.Drawable mPendingOverflowIcon;

    private boolean mPendingOverflowIconSet;

    private boolean mReserveOverflow;

    private boolean mReserveOverflowSet;

    private int mWidthLimit;

    private int mActionItemWidthLimit;

    private int mMaxItems;

    private boolean mMaxItemsSet;

    private boolean mStrictWidthLimit;

    private boolean mWidthLimitSet;

    private boolean mExpandedActionViewsExclusive;

    private int mMinCellSize;

    // Group IDs that have been added as actions - used temporarily, allocated here for reuse.
    private final android.util.SparseBooleanArray mActionButtonGroups = new android.util.SparseBooleanArray();

    private android.view.View mScrapActionButtonView;

    android.support.v7.widget.ActionMenuPresenter.OverflowPopup mOverflowPopup;

    android.support.v7.widget.ActionMenuPresenter.ActionButtonSubmenu mActionButtonPopup;

    android.support.v7.widget.ActionMenuPresenter.OpenOverflowRunnable mPostedOpenRunnable;

    private android.support.v7.widget.ActionMenuPresenter.ActionMenuPopupCallback mPopupCallback;

    final android.support.v7.widget.ActionMenuPresenter.PopupPresenterCallback mPopupPresenterCallback = new android.support.v7.widget.ActionMenuPresenter.PopupPresenterCallback();

    int mOpenSubMenuId;

    public ActionMenuPresenter(android.content.Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
    }

    @java.lang.Override
    public void initForMenu(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.support.v7.view.menu.MenuBuilder menu) {
        super.initForMenu(context, menu);
        final android.content.res.Resources res = context.getResources();
        final android.support.v7.view.ActionBarPolicy abp = android.support.v7.view.ActionBarPolicy.get(context);
        if (!mReserveOverflowSet) {
            mReserveOverflow = abp.showsOverflowMenuButton();
        }
        if (!mWidthLimitSet) {
            mWidthLimit = abp.getEmbeddedMenuWidthLimit();
        }
        // Measure for initial configuration
        if (!mMaxItemsSet) {
            mMaxItems = abp.getMaxActionButtons();
        }
        int width = mWidthLimit;
        if (mReserveOverflow) {
            if (mOverflowButton == null) {
                mOverflowButton = new android.support.v7.widget.ActionMenuPresenter.OverflowMenuButton(mSystemContext);
                if (mPendingOverflowIconSet) {
                    mOverflowButton.setImageDrawable(mPendingOverflowIcon);
                    mPendingOverflowIcon = null;
                    mPendingOverflowIconSet = false;
                }
                final int spec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
                mOverflowButton.measure(spec, spec);
            }
            width -= mOverflowButton.getMeasuredWidth();
        } else {
            mOverflowButton = null;
        }
        mActionItemWidthLimit = width;
        mMinCellSize = ((int) (android.support.v7.widget.ActionMenuView.MIN_CELL_SIZE * res.getDisplayMetrics().density));
        // Drop a scrap view as it may no longer reflect the proper context/config.
        mScrapActionButtonView = null;
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        if (!mMaxItemsSet) {
            mMaxItems = android.support.v7.view.ActionBarPolicy.get(mContext).getMaxActionButtons();
        }
        if (mMenu != null) {
            mMenu.onItemsChanged(true);
        }
    }

    public void setWidthLimit(int width, boolean strict) {
        mWidthLimit = width;
        mStrictWidthLimit = strict;
        mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean reserveOverflow) {
        mReserveOverflow = reserveOverflow;
        mReserveOverflowSet = true;
    }

    public void setItemLimit(int itemCount) {
        mMaxItems = itemCount;
        mMaxItemsSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean isExclusive) {
        mExpandedActionViewsExclusive = isExclusive;
    }

    public void setOverflowIcon(android.graphics.drawable.Drawable icon) {
        if (mOverflowButton != null) {
            mOverflowButton.setImageDrawable(icon);
        } else {
            mPendingOverflowIconSet = true;
            mPendingOverflowIcon = icon;
        }
    }

    public android.graphics.drawable.Drawable getOverflowIcon() {
        if (mOverflowButton != null) {
            return mOverflowButton.getDrawable();
        } else
            if (mPendingOverflowIconSet) {
                return mPendingOverflowIcon;
            }

        return null;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        android.support.v7.view.menu.MenuView oldMenuView = mMenuView;
        android.support.v7.view.menu.MenuView result = super.getMenuView(root);
        if (oldMenuView != result) {
            ((android.support.v7.widget.ActionMenuView) (result)).setPresenter(this);
        }
        return result;
    }

    @java.lang.Override
    public android.view.View getItemView(final android.support.v7.view.menu.MenuItemImpl item, android.view.View convertView, android.view.ViewGroup parent) {
        android.view.View actionView = item.getActionView();
        if ((actionView == null) || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
        final android.support.v7.widget.ActionMenuView menuParent = ((android.support.v7.widget.ActionMenuView) (parent));
        final android.view.ViewGroup.LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    @java.lang.Override
    public void bindItemView(android.support.v7.view.menu.MenuItemImpl item, android.support.v7.view.menu.MenuView.ItemView itemView) {
        itemView.initialize(item, 0);
        final android.support.v7.widget.ActionMenuView menuView = ((android.support.v7.widget.ActionMenuView) (mMenuView));
        final android.support.v7.view.menu.ActionMenuItemView actionItemView = ((android.support.v7.view.menu.ActionMenuItemView) (itemView));
        actionItemView.setItemInvoker(menuView);
        if (mPopupCallback == null) {
            mPopupCallback = new android.support.v7.widget.ActionMenuPresenter.ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(mPopupCallback);
    }

    @java.lang.Override
    public boolean shouldIncludeItem(int childIndex, android.support.v7.view.menu.MenuItemImpl item) {
        return item.isActionButton();
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        final android.view.ViewGroup menuViewParent = ((android.view.ViewGroup) (((android.view.View) (mMenuView)).getParent()));
        if (menuViewParent != null) {
            android.support.v7.transition.ActionBarTransition.beginDelayedTransition(menuViewParent);
        }
        super.updateMenuView(cleared);
        ((android.view.View) (mMenuView)).requestLayout();
        if (mMenu != null) {
            final java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> actionItems = mMenu.getActionItems();
            final int count = actionItems.size();
            for (int i = 0; i < count; i++) {
                final android.support.v4.view.ActionProvider provider = actionItems.get(i).getSupportActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        final java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> nonActionItems = (mMenu != null) ? mMenu.getNonActionItems() : null;
        boolean hasOverflow = false;
        if (mReserveOverflow && (nonActionItems != null)) {
            final int count = nonActionItems.size();
            if (count == 1) {
                hasOverflow = !nonActionItems.get(0).isActionViewExpanded();
            } else {
                hasOverflow = count > 0;
            }
        }
        if (hasOverflow) {
            if (mOverflowButton == null) {
                mOverflowButton = new android.support.v7.widget.ActionMenuPresenter.OverflowMenuButton(mSystemContext);
            }
            android.view.ViewGroup parent = ((android.view.ViewGroup) (mOverflowButton.getParent()));
            if (parent != mMenuView) {
                if (parent != null) {
                    parent.removeView(mOverflowButton);
                }
                android.support.v7.widget.ActionMenuView menuView = ((android.support.v7.widget.ActionMenuView) (mMenuView));
                menuView.addView(mOverflowButton, menuView.generateOverflowButtonLayoutParams());
            }
        } else
            if ((mOverflowButton != null) && (mOverflowButton.getParent() == mMenuView)) {
                ((android.view.ViewGroup) (mMenuView)).removeView(mOverflowButton);
            }

        ((android.support.v7.widget.ActionMenuView) (mMenuView)).setOverflowReserved(mReserveOverflow);
    }

    @java.lang.Override
    public boolean filterLeftoverView(android.view.ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == mOverflowButton)
            return false;

        return super.filterLeftoverView(parent, childIndex);
    }

    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems())
            return false;

        android.support.v7.view.menu.SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != mMenu) {
            topSubMenu = ((android.support.v7.view.menu.SubMenuBuilder) (topSubMenu.getParentMenu()));
        } 
        android.view.View anchor = findViewForItem(topSubMenu.getItem());
        if (anchor == null) {
            // This means the submenu was opened from an overflow menu item, indicating the
            // MenuPopupHelper will handle opening the submenu via its MenuPopup. Return false to
            // ensure that the MenuPopup acts as presenter for the submenu, and acts on its
            // responsibility to display the new submenu.
            return false;
        }
        mOpenSubMenuId = subMenu.getItem().getItemId();
        boolean preserveIconSpacing = false;
        final int count = subMenu.size();
        for (int i = 0; i < count; i++) {
            android.view.MenuItem childItem = subMenu.getItem(i);
            if (childItem.isVisible() && (childItem.getIcon() != null)) {
                preserveIconSpacing = true;
                break;
            }
        }
        mActionButtonPopup = new android.support.v7.widget.ActionMenuPresenter.ActionButtonSubmenu(mContext, subMenu, anchor);
        mActionButtonPopup.setForceShowIcon(preserveIconSpacing);
        mActionButtonPopup.show();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    private android.view.View findViewForItem(android.view.MenuItem item) {
        final android.view.ViewGroup parent = ((android.view.ViewGroup) (mMenuView));
        if (parent == null)
            return null;

        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View child = parent.getChildAt(i);
            if ((child instanceof android.support.v7.view.menu.MenuView.ItemView) && (((android.support.v7.view.menu.MenuView.ItemView) (child)).getItemData() == item)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Display the overflow menu if one is present.
     *
     * @return true if the overflow menu was shown, false otherwise.
     */
    public boolean showOverflowMenu() {
        if (((((mReserveOverflow && (!isOverflowMenuShowing())) && (mMenu != null)) && (mMenuView != null)) && (mPostedOpenRunnable == null)) && (!mMenu.getNonActionItems().isEmpty())) {
            android.support.v7.widget.ActionMenuPresenter.OverflowPopup popup = new android.support.v7.widget.ActionMenuPresenter.OverflowPopup(mContext, mMenu, mOverflowButton, true);
            mPostedOpenRunnable = new android.support.v7.widget.ActionMenuPresenter.OpenOverflowRunnable(popup);
            // Post this for later; we might still need a layout for the anchor to be right.
            ((android.view.View) (mMenuView)).post(mPostedOpenRunnable);
            // ActionMenuPresenter uses null as a callback argument here
            // to indicate overflow is opening.
            super.onSubMenuSelected(null);
            return true;
        }
        return false;
    }

    /**
     * Hide the overflow menu if it is currently showing.
     *
     * @return true if the overflow menu was hidden, false otherwise.
     */
    public boolean hideOverflowMenu() {
        if ((mPostedOpenRunnable != null) && (mMenuView != null)) {
            ((android.view.View) (mMenuView)).removeCallbacks(mPostedOpenRunnable);
            mPostedOpenRunnable = null;
            return true;
        }
        android.support.v7.view.menu.MenuPopupHelper popup = mOverflowPopup;
        if (popup != null) {
            popup.dismiss();
            return true;
        }
        return false;
    }

    /**
     * Dismiss all popup menus - overflow and submenus.
     *
     * @return true if popups were dismissed, false otherwise. (This can be because none were open.)
     */
    public boolean dismissPopupMenus() {
        boolean result = hideOverflowMenu();
        result |= hideSubMenus();
        return result;
    }

    /**
     * Dismiss all submenu popups.
     *
     * @return true if popups were dismissed, false otherwise. (This can be because none were open.)
     */
    public boolean hideSubMenus() {
        if (mActionButtonPopup != null) {
            mActionButtonPopup.dismiss();
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @return true if the overflow menu is currently showing
     */
    public boolean isOverflowMenuShowing() {
        return (mOverflowPopup != null) && mOverflowPopup.isShowing();
    }

    public boolean isOverflowMenuShowPending() {
        return (mPostedOpenRunnable != null) || isOverflowMenuShowing();
    }

    /**
     *
     *
     * @return true if space has been reserved in the action menu for an overflow item.
     */
    public boolean isOverflowReserved() {
        return mReserveOverflow;
    }

    public boolean flagActionItems() {
        final java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> visibleItems;
        final int itemsSize;
        if (mMenu != null) {
            visibleItems = mMenu.getVisibleItems();
            itemsSize = visibleItems.size();
        } else {
            visibleItems = null;
            itemsSize = 0;
        }
        int maxActions = mMaxItems;
        int widthLimit = mActionItemWidthLimit;
        final int querySpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final android.view.ViewGroup parent = ((android.view.ViewGroup) (mMenuView));
        int requiredItems = 0;
        int requestedItems = 0;
        int firstActionWidth = 0;
        boolean hasOverflow = false;
        for (int i = 0; i < itemsSize; i++) {
            android.support.v7.view.menu.MenuItemImpl item = visibleItems.get(i);
            if (item.requiresActionButton()) {
                requiredItems++;
            } else
                if (item.requestsActionButton()) {
                    requestedItems++;
                } else {
                    hasOverflow = true;
                }

            if (mExpandedActionViewsExclusive && item.isActionViewExpanded()) {
                // Overflow everything if we have an expanded action view and we're
                // space constrained.
                maxActions = 0;
            }
        }
        // Reserve a spot for the overflow item if needed.
        if (mReserveOverflow && (hasOverflow || ((requiredItems + requestedItems) > maxActions))) {
            maxActions--;
        }
        maxActions -= requiredItems;
        final android.util.SparseBooleanArray seenGroups = mActionButtonGroups;
        seenGroups.clear();
        int cellSize = 0;
        int cellsRemaining = 0;
        if (mStrictWidthLimit) {
            cellsRemaining = widthLimit / mMinCellSize;
            final int cellSizeRemaining = widthLimit % mMinCellSize;
            cellSize = mMinCellSize + (cellSizeRemaining / cellsRemaining);
        }
        // Flag as many more requested items as will fit.
        for (int i = 0; i < itemsSize; i++) {
            android.support.v7.view.menu.MenuItemImpl item = visibleItems.get(i);
            if (item.requiresActionButton()) {
                android.view.View v = getItemView(item, mScrapActionButtonView, parent);
                if (mScrapActionButtonView == null) {
                    mScrapActionButtonView = v;
                }
                if (mStrictWidthLimit) {
                    cellsRemaining -= android.support.v7.widget.ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                } else {
                    v.measure(querySpec, querySpec);
                }
                final int measuredWidth = v.getMeasuredWidth();
                widthLimit -= measuredWidth;
                if (firstActionWidth == 0) {
                    firstActionWidth = measuredWidth;
                }
                final int groupId = item.getGroupId();
                if (groupId != 0) {
                    seenGroups.put(groupId, true);
                }
                item.setIsActionButton(true);
            } else
                if (item.requestsActionButton()) {
                    // Items in a group with other items that already have an action slot
                    // can break the max actions rule, but not the width limit.
                    final int groupId = item.getGroupId();
                    final boolean inGroup = seenGroups.get(groupId);
                    boolean isAction = (((maxActions > 0) || inGroup) && (widthLimit > 0)) && ((!mStrictWidthLimit) || (cellsRemaining > 0));
                    if (isAction) {
                        android.view.View v = getItemView(item, mScrapActionButtonView, parent);
                        if (mScrapActionButtonView == null) {
                            mScrapActionButtonView = v;
                        }
                        if (mStrictWidthLimit) {
                            final int cells = android.support.v7.widget.ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                            cellsRemaining -= cells;
                            if (cells == 0) {
                                isAction = false;
                            }
                        } else {
                            v.measure(querySpec, querySpec);
                        }
                        final int measuredWidth = v.getMeasuredWidth();
                        widthLimit -= measuredWidth;
                        if (firstActionWidth == 0) {
                            firstActionWidth = measuredWidth;
                        }
                        if (mStrictWidthLimit) {
                            isAction &= widthLimit >= 0;
                        } else {
                            // Did this push the entire first item past the limit?
                            isAction &= (widthLimit + firstActionWidth) > 0;
                        }
                    }
                    if (isAction && (groupId != 0)) {
                        seenGroups.put(groupId, true);
                    } else
                        if (inGroup) {
                            // We broke the width limit. Demote the whole group, they all overflow now.
                            seenGroups.put(groupId, false);
                            for (int j = 0; j < i; j++) {
                                android.support.v7.view.menu.MenuItemImpl areYouMyGroupie = visibleItems.get(j);
                                if (areYouMyGroupie.getGroupId() == groupId) {
                                    // Give back the action slot
                                    if (areYouMyGroupie.isActionButton())
                                        maxActions++;

                                    areYouMyGroupie.setIsActionButton(false);
                                }
                            }
                        }

                    if (isAction)
                        maxActions--;

                    item.setIsActionButton(isAction);
                } else {
                    // Neither requires nor requests an action button.
                    item.setIsActionButton(false);
                }

        }
        return true;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.support.v7.widget.ActionMenuPresenter.SavedState state = new android.support.v7.widget.ActionMenuPresenter.SavedState();
        state.openSubMenuId = mOpenSubMenuId;
        return state;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v7.widget.ActionMenuPresenter.SavedState)) {
            return;
        }
        android.support.v7.widget.ActionMenuPresenter.SavedState saved = ((android.support.v7.widget.ActionMenuPresenter.SavedState) (state));
        if (saved.openSubMenuId > 0) {
            android.view.MenuItem item = mMenu.findItem(saved.openSubMenuId);
            if (item != null) {
                android.support.v7.view.menu.SubMenuBuilder subMenu = ((android.support.v7.view.menu.SubMenuBuilder) (item.getSubMenu()));
                onSubMenuSelected(subMenu);
            }
        }
    }

    @java.lang.Override
    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            // Not a submenu, but treat it like one.
            super.onSubMenuSelected(null);
        } else
            if (mMenu != null) {
                /* closeAllMenus */
                mMenu.close(false);
            }

    }

    public void setMenuView(android.support.v7.widget.ActionMenuView menuView) {
        mMenuView = menuView;
        menuView.initialize(mMenu);
    }

    private static class SavedState implements android.os.Parcelable {
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(android.os.Parcel in) {
            openSubMenuId = in.readInt();
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(openSubMenuId);
        }

        public static final android.os.Parcelable.Creator<android.support.v7.widget.ActionMenuPresenter.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v7.widget.ActionMenuPresenter.SavedState>() {
            public android.support.v7.widget.ActionMenuPresenter.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v7.widget.ActionMenuPresenter.SavedState(in);
            }

            public android.support.v7.widget.ActionMenuPresenter.SavedState[] newArray(int size) {
                return new android.support.v7.widget.ActionMenuPresenter.SavedState[size];
            }
        };
    }

    private class OverflowMenuButton extends android.support.v7.widget.AppCompatImageView implements android.support.v7.widget.ActionMenuView.ActionMenuChildView {
        private final float[] mTempPts = new float[2];

        public OverflowMenuButton(android.content.Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setVisibility(android.view.View.VISIBLE);
            setEnabled(true);
            setOnTouchListener(new android.support.v7.widget.ForwardingListener(this) {
                @java.lang.Override
                public android.support.v7.view.menu.ShowableListMenu getPopup() {
                    if (mOverflowPopup == null) {
                        return null;
                    }
                    return mOverflowPopup.getPopup();
                }

                @java.lang.Override
                public boolean onForwardingStarted() {
                    showOverflowMenu();
                    return true;
                }

                @java.lang.Override
                public boolean onForwardingStopped() {
                    // Displaying the popup occurs asynchronously, so wait for
                    // the runnable to finish before deciding whether to stop
                    // forwarding.
                    if (mPostedOpenRunnable != null) {
                        return false;
                    }
                    hideOverflowMenu();
                    return true;
                }
            });
        }

        @java.lang.Override
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(android.view.SoundEffectConstants.CLICK);
            showOverflowMenu();
            return true;
        }

        @java.lang.Override
        public boolean needsDividerBefore() {
            return false;
        }

        @java.lang.Override
        public boolean needsDividerAfter() {
            return false;
        }

        @java.lang.Override
        protected boolean setFrame(int l, int t, int r, int b) {
            final boolean changed = super.setFrame(l, t, r, b);
            // Set up the hotspot bounds to be centered on the image.
            final android.graphics.drawable.Drawable d = getDrawable();
            final android.graphics.drawable.Drawable bg = getBackground();
            if ((d != null) && (bg != null)) {
                final int width = getWidth();
                final int height = getHeight();
                final int halfEdge = java.lang.Math.max(width, height) / 2;
                final int offsetX = getPaddingLeft() - getPaddingRight();
                final int offsetY = getPaddingTop() - getPaddingBottom();
                final int centerX = (width + offsetX) / 2;
                final int centerY = (height + offsetY) / 2;
                android.support.v4.graphics.drawable.DrawableCompat.setHotspotBounds(bg, centerX - halfEdge, centerY - halfEdge, centerX + halfEdge, centerY + halfEdge);
            }
            return changed;
        }
    }

    private class OverflowPopup extends android.support.v7.view.menu.MenuPopupHelper {
        public OverflowPopup(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu, android.view.View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, R.attr.actionOverflowMenuStyle);
            setGravity(android.support.v4.view.GravityCompat.END);
            setPresenterCallback(mPopupPresenterCallback);
        }

        @java.lang.Override
        protected void onDismiss() {
            if (android.support.v7.widget.ActionMenuPresenter.this.mMenu != null) {
                android.support.v7.widget.ActionMenuPresenter.this.mMenu.close();
            }
            mOverflowPopup = null;
            super.onDismiss();
        }
    }

    private class ActionButtonSubmenu extends android.support.v7.view.menu.MenuPopupHelper {
        public ActionButtonSubmenu(android.content.Context context, android.support.v7.view.menu.SubMenuBuilder subMenu, android.view.View anchorView) {
            super(context, subMenu, anchorView, false, R.attr.actionOverflowMenuStyle);
            android.support.v7.view.menu.MenuItemImpl item = ((android.support.v7.view.menu.MenuItemImpl) (subMenu.getItem()));
            if (!item.isActionButton()) {
                // Give a reasonable anchor to nested submenus.
                setAnchorView(mOverflowButton == null ? ((android.view.View) (mMenuView)) : mOverflowButton);
            }
            setPresenterCallback(mPopupPresenterCallback);
        }

        @java.lang.Override
        protected void onDismiss() {
            mActionButtonPopup = null;
            mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    private class PopupPresenterCallback implements android.support.v7.view.menu.MenuPresenter.Callback {
        PopupPresenterCallback() {
        }

        @java.lang.Override
        public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
            if (subMenu == null)
                return false;

            mOpenSubMenuId = ((android.support.v7.view.menu.SubMenuBuilder) (subMenu)).getItem().getItemId();
            final android.support.v7.view.menu.MenuPresenter.Callback cb = getCallback();
            return cb != null ? cb.onOpenSubMenu(subMenu) : false;
        }

        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof android.support.v7.view.menu.SubMenuBuilder) {
                /* closeAllMenus */
                menu.getRootMenu().close(false);
            }
            final android.support.v7.view.menu.MenuPresenter.Callback cb = getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    private class OpenOverflowRunnable implements java.lang.Runnable {
        private android.support.v7.widget.ActionMenuPresenter.OverflowPopup mPopup;

        public OpenOverflowRunnable(android.support.v7.widget.ActionMenuPresenter.OverflowPopup popup) {
            mPopup = popup;
        }

        public void run() {
            if (mMenu != null) {
                mMenu.changeMenuMode();
            }
            final android.view.View menuView = ((android.view.View) (mMenuView));
            if (((menuView != null) && (menuView.getWindowToken() != null)) && mPopup.tryShow()) {
                mOverflowPopup = mPopup;
            }
            mPostedOpenRunnable = null;
        }
    }

    private class ActionMenuPopupCallback extends android.support.v7.view.menu.ActionMenuItemView.PopupCallback {
        ActionMenuPopupCallback() {
        }

        @java.lang.Override
        public android.support.v7.view.menu.ShowableListMenu getPopup() {
            return mActionButtonPopup != null ? mActionButtonPopup.getPopup() : null;
        }
    }
}

