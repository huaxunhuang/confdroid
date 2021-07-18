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
package android.widget;


/**
 * MenuPresenter for building action menus as seen in the action bar and action modes.
 *
 * @unknown 
 */
public class ActionMenuPresenter extends com.android.internal.view.menu.BaseMenuPresenter implements android.view.ActionProvider.SubUiVisibilityListener {
    private static final int ITEM_ANIMATION_DURATION = 150;

    private static final boolean ACTIONBAR_ANIMATIONS_ENABLED = false;

    private android.widget.ActionMenuPresenter.OverflowMenuButton mOverflowButton;

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

    private android.widget.ActionMenuPresenter.OverflowPopup mOverflowPopup;

    private android.widget.ActionMenuPresenter.ActionButtonSubmenu mActionButtonPopup;

    private android.widget.ActionMenuPresenter.OpenOverflowRunnable mPostedOpenRunnable;

    private android.widget.ActionMenuPresenter.ActionMenuPopupCallback mPopupCallback;

    final android.widget.ActionMenuPresenter.PopupPresenterCallback mPopupPresenterCallback = new android.widget.ActionMenuPresenter.PopupPresenterCallback();

    int mOpenSubMenuId;

    // These collections are used to store pre- and post-layout information for menu items,
    // which is used to determine appropriate animations to run for changed items.
    private android.util.SparseArray<android.widget.ActionMenuPresenter.MenuItemLayoutInfo> mPreLayoutItems = new android.util.SparseArray();

    private android.util.SparseArray<android.widget.ActionMenuPresenter.MenuItemLayoutInfo> mPostLayoutItems = new android.util.SparseArray();

    // The list of currently running animations on menu items.
    private java.util.List<android.widget.ActionMenuPresenter.ItemAnimationInfo> mRunningItemAnimations = new java.util.ArrayList<>();

    private android.view.ViewTreeObserver.OnPreDrawListener mItemAnimationPreDrawListener = new android.view.ViewTreeObserver.OnPreDrawListener() {
        @java.lang.Override
        public boolean onPreDraw() {
            computeMenuItemAnimationInfo(false);
            ((android.view.View) (mMenuView)).getViewTreeObserver().removeOnPreDrawListener(this);
            runItemAnimations();
            return true;
        }
    };

    private android.view.View.OnAttachStateChangeListener mAttachStateChangeListener = new android.view.View.OnAttachStateChangeListener() {
        @java.lang.Override
        public void onViewAttachedToWindow(android.view.View v) {
        }

        @java.lang.Override
        public void onViewDetachedFromWindow(android.view.View v) {
            ((android.view.View) (mMenuView)).getViewTreeObserver().removeOnPreDrawListener(mItemAnimationPreDrawListener);
            mPreLayoutItems.clear();
            mPostLayoutItems.clear();
        }
    };

    public ActionMenuPresenter(android.content.Context context) {
        super(context, com.android.internal.R.layout.action_menu_layout, com.android.internal.R.layout.action_menu_item_layout);
    }

    @java.lang.Override
    public void initForMenu(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    com.android.internal.view.menu.MenuBuilder menu) {
        super.initForMenu(context, menu);
        final android.content.res.Resources res = context.getResources();
        final com.android.internal.view.ActionBarPolicy abp = com.android.internal.view.ActionBarPolicy.get(context);
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
                mOverflowButton = new android.widget.ActionMenuPresenter.OverflowMenuButton(mSystemContext);
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
        mMinCellSize = ((int) (android.widget.ActionMenuView.MIN_CELL_SIZE * res.getDisplayMetrics().density));
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        if (!mMaxItemsSet) {
            mMaxItems = com.android.internal.view.ActionBarPolicy.get(mContext).getMaxActionButtons();
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
    public com.android.internal.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        com.android.internal.view.menu.MenuView oldMenuView = mMenuView;
        com.android.internal.view.menu.MenuView result = getMenuView(root);
        if (oldMenuView != result) {
            ((android.widget.ActionMenuView) (result)).setPresenter(this);
            if (oldMenuView != null) {
                ((android.view.View) (oldMenuView)).removeOnAttachStateChangeListener(mAttachStateChangeListener);
            }
            ((android.view.View) (result)).addOnAttachStateChangeListener(mAttachStateChangeListener);
        }
        return result;
    }

    @java.lang.Override
    public android.view.View getItemView(final com.android.internal.view.menu.MenuItemImpl item, android.view.View convertView, android.view.ViewGroup parent) {
        android.view.View actionView = item.getActionView();
        if ((actionView == null) || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
        final android.widget.ActionMenuView menuParent = ((android.widget.ActionMenuView) (parent));
        final android.view.ViewGroup.LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    @java.lang.Override
    public void bindItemView(com.android.internal.view.menu.MenuItemImpl item, com.android.internal.view.menu.MenuView.ItemView itemView) {
        itemView.initialize(item, 0);
        final android.widget.ActionMenuView menuView = ((android.widget.ActionMenuView) (mMenuView));
        final com.android.internal.view.menu.ActionMenuItemView actionItemView = ((com.android.internal.view.menu.ActionMenuItemView) (itemView));
        actionItemView.setItemInvoker(menuView);
        if (mPopupCallback == null) {
            mPopupCallback = new android.widget.ActionMenuPresenter.ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(mPopupCallback);
    }

    @java.lang.Override
    public boolean shouldIncludeItem(int childIndex, com.android.internal.view.menu.MenuItemImpl item) {
        return item.isActionButton();
    }

    /**
     * Store layout information about current items in the menu. This is stored for
     * both pre- and post-layout phases and compared in runItemAnimations() to determine
     * the animations that need to be run on any item changes.
     *
     * @param preLayout
     * 		Whether this is being called in the pre-layout phase. This is passed
     * 		into the MenuItemLayoutInfo structure to store the appropriate position values.
     */
    private void computeMenuItemAnimationInfo(boolean preLayout) {
        final android.view.ViewGroup menuView = ((android.view.ViewGroup) (mMenuView));
        final int count = menuView.getChildCount();
        android.util.SparseArray items = (preLayout) ? mPreLayoutItems : mPostLayoutItems;
        for (int i = 0; i < count; ++i) {
            android.view.View child = menuView.getChildAt(i);
            final int id = child.getId();
            if (((id > 0) && (child.getWidth() != 0)) && (child.getHeight() != 0)) {
                android.widget.ActionMenuPresenter.MenuItemLayoutInfo info = new android.widget.ActionMenuPresenter.MenuItemLayoutInfo(child, preLayout);
                items.put(id, info);
            }
        }
    }

    /**
     * This method is called once both the pre-layout and post-layout steps have
     * happened. It figures out which views are new (didn't exist prior to layout),
     * gone (existed pre-layout, but are now gone), or changed (exist in both,
     * but in a different location) and runs appropriate animations on those views.
     * Items are tracked by ids, since the underlying views that represent items
     * pre- and post-layout may be different.
     */
    private void runItemAnimations() {
        for (int i = 0; i < mPreLayoutItems.size(); ++i) {
            int id = mPreLayoutItems.keyAt(i);
            final android.widget.ActionMenuPresenter.MenuItemLayoutInfo menuItemLayoutInfoPre = mPreLayoutItems.get(id);
            final int postLayoutIndex = mPostLayoutItems.indexOfKey(id);
            if (postLayoutIndex >= 0) {
                // item exists pre and post: see if it's changed
                final android.widget.ActionMenuPresenter.MenuItemLayoutInfo menuItemLayoutInfoPost = mPostLayoutItems.valueAt(postLayoutIndex);
                android.animation.PropertyValuesHolder pvhX = null;
                android.animation.PropertyValuesHolder pvhY = null;
                if (menuItemLayoutInfoPre.left != menuItemLayoutInfoPost.left) {
                    pvhX = android.animation.PropertyValuesHolder.ofFloat(android.view.View.TRANSLATION_X, menuItemLayoutInfoPre.left - menuItemLayoutInfoPost.left, 0);
                }
                if (menuItemLayoutInfoPre.top != menuItemLayoutInfoPost.top) {
                    pvhY = android.animation.PropertyValuesHolder.ofFloat(android.view.View.TRANSLATION_Y, menuItemLayoutInfoPre.top - menuItemLayoutInfoPost.top, 0);
                }
                if ((pvhX != null) || (pvhY != null)) {
                    for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                        android.widget.ActionMenuPresenter.ItemAnimationInfo oldInfo = mRunningItemAnimations.get(j);
                        if ((oldInfo.id == id) && (oldInfo.animType == android.widget.ActionMenuPresenter.ItemAnimationInfo.MOVE)) {
                            oldInfo.animator.cancel();
                        }
                    }
                    android.animation.ObjectAnimator anim;
                    if (pvhX != null) {
                        if (pvhY != null) {
                            anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, pvhX, pvhY);
                        } else {
                            anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, pvhX);
                        }
                    } else {
                        anim = android.animation.ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, pvhY);
                    }
                    anim.setDuration(android.widget.ActionMenuPresenter.ITEM_ANIMATION_DURATION);
                    anim.start();
                    android.widget.ActionMenuPresenter.ItemAnimationInfo info = new android.widget.ActionMenuPresenter.ItemAnimationInfo(id, menuItemLayoutInfoPost, anim, android.widget.ActionMenuPresenter.ItemAnimationInfo.MOVE);
                    mRunningItemAnimations.add(info);
                    anim.addListener(new android.animation.AnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationEnd(android.animation.Animator animation) {
                            for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                                if (mRunningItemAnimations.get(j).animator == animation) {
                                    mRunningItemAnimations.remove(j);
                                    break;
                                }
                            }
                        }
                    });
                }
                mPostLayoutItems.remove(id);
            } else {
                // item used to be there, is now gone
                float oldAlpha = 1;
                for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                    android.widget.ActionMenuPresenter.ItemAnimationInfo oldInfo = mRunningItemAnimations.get(j);
                    if ((oldInfo.id == id) && (oldInfo.animType == android.widget.ActionMenuPresenter.ItemAnimationInfo.FADE_IN)) {
                        oldAlpha = oldInfo.menuItemLayoutInfo.view.getAlpha();
                        oldInfo.animator.cancel();
                    }
                }
                android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(menuItemLayoutInfoPre.view, android.view.View.ALPHA, oldAlpha, 0);
                // Re-using the view from pre-layout assumes no view recycling
                ((android.view.ViewGroup) (mMenuView)).getOverlay().add(menuItemLayoutInfoPre.view);
                anim.setDuration(android.widget.ActionMenuPresenter.ITEM_ANIMATION_DURATION);
                anim.start();
                android.widget.ActionMenuPresenter.ItemAnimationInfo info = new android.widget.ActionMenuPresenter.ItemAnimationInfo(id, menuItemLayoutInfoPre, anim, android.widget.ActionMenuPresenter.ItemAnimationInfo.FADE_OUT);
                mRunningItemAnimations.add(info);
                anim.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                            if (mRunningItemAnimations.get(j).animator == animation) {
                                mRunningItemAnimations.remove(j);
                                break;
                            }
                        }
                        ((android.view.ViewGroup) (mMenuView)).getOverlay().remove(menuItemLayoutInfoPre.view);
                    }
                });
            }
        }
        for (int i = 0; i < mPostLayoutItems.size(); ++i) {
            int id = mPostLayoutItems.keyAt(i);
            final int postLayoutIndex = mPostLayoutItems.indexOfKey(id);
            if (postLayoutIndex >= 0) {
                // item is new
                final android.widget.ActionMenuPresenter.MenuItemLayoutInfo menuItemLayoutInfo = mPostLayoutItems.valueAt(postLayoutIndex);
                float oldAlpha = 0;
                for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                    android.widget.ActionMenuPresenter.ItemAnimationInfo oldInfo = mRunningItemAnimations.get(j);
                    if ((oldInfo.id == id) && (oldInfo.animType == android.widget.ActionMenuPresenter.ItemAnimationInfo.FADE_OUT)) {
                        oldAlpha = oldInfo.menuItemLayoutInfo.view.getAlpha();
                        oldInfo.animator.cancel();
                    }
                }
                android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(menuItemLayoutInfo.view, android.view.View.ALPHA, oldAlpha, 1);
                anim.start();
                anim.setDuration(android.widget.ActionMenuPresenter.ITEM_ANIMATION_DURATION);
                android.widget.ActionMenuPresenter.ItemAnimationInfo info = new android.widget.ActionMenuPresenter.ItemAnimationInfo(id, menuItemLayoutInfo, anim, android.widget.ActionMenuPresenter.ItemAnimationInfo.FADE_IN);
                mRunningItemAnimations.add(info);
                anim.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        for (int j = 0; j < mRunningItemAnimations.size(); ++j) {
                            if (mRunningItemAnimations.get(j).animator == animation) {
                                mRunningItemAnimations.remove(j);
                                break;
                            }
                        }
                    }
                });
            }
        }
        mPreLayoutItems.clear();
        mPostLayoutItems.clear();
    }

    /**
     * Gets position/existence information on menu items before and after layout,
     * which is then fed into runItemAnimations()
     */
    private void setupItemAnimations() {
        computeMenuItemAnimationInfo(true);
        ((android.view.View) (mMenuView)).getViewTreeObserver().addOnPreDrawListener(mItemAnimationPreDrawListener);
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        final android.view.ViewGroup menuViewParent = ((android.view.ViewGroup) (((android.view.View) (mMenuView)).getParent()));
        if ((menuViewParent != null) && android.widget.ActionMenuPresenter.ACTIONBAR_ANIMATIONS_ENABLED) {
            setupItemAnimations();
        }
        updateMenuView(cleared);
        ((android.view.View) (mMenuView)).requestLayout();
        if (mMenu != null) {
            final java.util.ArrayList<com.android.internal.view.menu.MenuItemImpl> actionItems = mMenu.getActionItems();
            final int count = actionItems.size();
            for (int i = 0; i < count; i++) {
                final android.view.ActionProvider provider = actionItems.get(i).getActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        final java.util.ArrayList<com.android.internal.view.menu.MenuItemImpl> nonActionItems = (mMenu != null) ? mMenu.getNonActionItems() : null;
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
                mOverflowButton = new android.widget.ActionMenuPresenter.OverflowMenuButton(mSystemContext);
            }
            android.view.ViewGroup parent = ((android.view.ViewGroup) (mOverflowButton.getParent()));
            if (parent != mMenuView) {
                if (parent != null) {
                    parent.removeView(mOverflowButton);
                }
                android.widget.ActionMenuView menuView = ((android.widget.ActionMenuView) (mMenuView));
                menuView.addView(mOverflowButton, menuView.generateOverflowButtonLayoutParams());
            }
        } else
            if ((mOverflowButton != null) && (mOverflowButton.getParent() == mMenuView)) {
                ((android.view.ViewGroup) (mMenuView)).removeView(mOverflowButton);
            }

        ((android.widget.ActionMenuView) (mMenuView)).setOverflowReserved(mReserveOverflow);
    }

    @java.lang.Override
    public boolean filterLeftoverView(android.view.ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == mOverflowButton)
            return false;

        return filterLeftoverView(parent, childIndex);
    }

    public boolean onSubMenuSelected(com.android.internal.view.menu.SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems())
            return false;

        com.android.internal.view.menu.SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != mMenu) {
            topSubMenu = ((com.android.internal.view.menu.SubMenuBuilder) (topSubMenu.getParentMenu()));
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
        mActionButtonPopup = new android.widget.ActionMenuPresenter.ActionButtonSubmenu(mContext, subMenu, anchor);
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
            if ((child instanceof com.android.internal.view.menu.MenuView.ItemView) && (((com.android.internal.view.menu.MenuView.ItemView) (child)).getItemData() == item)) {
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
            android.widget.ActionMenuPresenter.OverflowPopup popup = new android.widget.ActionMenuPresenter.OverflowPopup(mContext, mMenu, mOverflowButton, true);
            mPostedOpenRunnable = new android.widget.ActionMenuPresenter.OpenOverflowRunnable(popup);
            // Post this for later; we might still need a layout for the anchor to be right.
            ((android.view.View) (mMenuView)).post(mPostedOpenRunnable);
            // ActionMenuPresenter uses null as a callback argument here
            // to indicate overflow is opening.
            onSubMenuSelected(null);
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
        com.android.internal.view.menu.MenuPopupHelper popup = mOverflowPopup;
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
    @android.annotation.UnsupportedAppUsage
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
    @android.annotation.UnsupportedAppUsage
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
        final java.util.ArrayList<com.android.internal.view.menu.MenuItemImpl> visibleItems;
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
            com.android.internal.view.menu.MenuItemImpl item = visibleItems.get(i);
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
            com.android.internal.view.menu.MenuItemImpl item = visibleItems.get(i);
            if (item.requiresActionButton()) {
                android.view.View v = getItemView(item, null, parent);
                if (mStrictWidthLimit) {
                    cellsRemaining -= android.widget.ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
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
                        android.view.View v = getItemView(item, null, parent);
                        if (mStrictWidthLimit) {
                            final int cells = android.widget.ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
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
                                com.android.internal.view.menu.MenuItemImpl areYouMyGroupie = visibleItems.get(j);
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
    public void onCloseMenu(com.android.internal.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public android.os.Parcelable onSaveInstanceState() {
        android.widget.ActionMenuPresenter.SavedState state = new android.widget.ActionMenuPresenter.SavedState();
        state.openSubMenuId = mOpenSubMenuId;
        return state;
    }

    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public void onRestoreInstanceState(android.os.Parcelable state) {
        android.widget.ActionMenuPresenter.SavedState saved = ((android.widget.ActionMenuPresenter.SavedState) (state));
        if (saved.openSubMenuId > 0) {
            android.view.MenuItem item = mMenu.findItem(saved.openSubMenuId);
            if (item != null) {
                com.android.internal.view.menu.SubMenuBuilder subMenu = ((com.android.internal.view.menu.SubMenuBuilder) (item.getSubMenu()));
                onSubMenuSelected(subMenu);
            }
        }
    }

    @java.lang.Override
    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            // Not a submenu, but treat it like one.
            onSubMenuSelected(null);
        } else
            if (mMenu != null) {
                /* closeAllMenus */
                mMenu.close(false);
            }

    }

    public void setMenuView(android.widget.ActionMenuView menuView) {
        if (menuView != mMenuView) {
            if (mMenuView != null) {
                ((android.view.View) (mMenuView)).removeOnAttachStateChangeListener(mAttachStateChangeListener);
            }
            mMenuView = menuView;
            menuView.initialize(mMenu);
            menuView.addOnAttachStateChangeListener(mAttachStateChangeListener);
        }
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

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.ActionMenuPresenter.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.ActionMenuPresenter.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }

    private class OverflowMenuButton extends android.widget.ImageButton implements android.widget.ActionMenuView.ActionMenuChildView {
        public OverflowMenuButton(android.content.Context context) {
            super(context, null, com.android.internal.R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setVisibility(android.view.View.VISIBLE);
            setEnabled(true);
            setOnTouchListener(new android.widget.ForwardingListener(this) {
                @java.lang.Override
                public com.android.internal.view.menu.ShowableListMenu getPopup() {
                    if (mOverflowPopup == null) {
                        return null;
                    }
                    return getPopup();
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

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfoInternal(info);
            info.setCanOpenPopup(true);
        }

        @java.lang.Override
        protected boolean setFrame(int l, int t, int r, int b) {
            final boolean changed = super.setFrame(l, t, r, b);
            // Set up the hotspot bounds to square and centered on the image.
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
                bg.setHotspotBounds(centerX - halfEdge, centerY - halfEdge, centerX + halfEdge, centerY + halfEdge);
            }
            return changed;
        }
    }

    private class OverflowPopup extends com.android.internal.view.menu.MenuPopupHelper {
        public OverflowPopup(android.content.Context context, com.android.internal.view.menu.MenuBuilder menu, android.view.View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, com.android.internal.R.attr.actionOverflowMenuStyle);
            setGravity(android.view.Gravity.END);
            setPresenterCallback(mPopupPresenterCallback);
        }

        @java.lang.Override
        protected void onDismiss() {
            if (mMenu != null) {
                mMenu.close();
            }
            mOverflowPopup = null;
            onDismiss();
        }
    }

    private class ActionButtonSubmenu extends com.android.internal.view.menu.MenuPopupHelper {
        public ActionButtonSubmenu(android.content.Context context, com.android.internal.view.menu.SubMenuBuilder subMenu, android.view.View anchorView) {
            super(context, subMenu, anchorView, false, com.android.internal.R.attr.actionOverflowMenuStyle);
            com.android.internal.view.menu.MenuItemImpl item = ((com.android.internal.view.menu.MenuItemImpl) (subMenu.getItem()));
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
            onDismiss();
        }
    }

    private class PopupPresenterCallback implements android.widget.Callback {
        @java.lang.Override
        public boolean onOpenSubMenu(com.android.internal.view.menu.MenuBuilder subMenu) {
            if (subMenu == null)
                return false;

            mOpenSubMenuId = ((com.android.internal.view.menu.SubMenuBuilder) (subMenu)).getItem().getItemId();
            final android.widget.Callback cb = getCallback();
            return cb != null ? cb.onOpenSubMenu(subMenu) : false;
        }

        @java.lang.Override
        public void onCloseMenu(com.android.internal.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof com.android.internal.view.menu.SubMenuBuilder) {
                /* closeAllMenus */
                menu.getRootMenu().close(false);
            }
            final android.widget.Callback cb = getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    private class OpenOverflowRunnable implements java.lang.Runnable {
        private android.widget.ActionMenuPresenter.OverflowPopup mPopup;

        public OpenOverflowRunnable(android.widget.ActionMenuPresenter.OverflowPopup popup) {
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

    private class ActionMenuPopupCallback extends com.android.internal.view.menu.ActionMenuItemView.PopupCallback {
        @java.lang.Override
        public com.android.internal.view.menu.ShowableListMenu getPopup() {
            return mActionButtonPopup != null ? getPopup() : null;
        }
    }

    /**
     * This class holds layout information for a menu item. This is used to determine
     * pre- and post-layout information about menu items, which will then be used to
     * determine appropriate item animations.
     */
    private static class MenuItemLayoutInfo {
        android.view.View view;

        int left;

        int top;

        MenuItemLayoutInfo(android.view.View view, boolean preLayout) {
            left = view.getLeft();
            top = view.getTop();
            if (preLayout) {
                // We track translation for pre-layout because a view might be mid-animation
                // and we need this information to know where to animate from
                left += view.getTranslationX();
                top += view.getTranslationY();
            }
            this.view = view;
        }
    }

    /**
     * This class is used to store information about currently-running item animations.
     * This is used when new animations are scheduled to determine whether any existing
     * animations need to be canceled, based on whether the running animations overlap
     * with any new animations. For example, if an item is currently animating from
     * location A to B and another change dictates that it be animated to C, then the current
     * A-B animation will be canceled and a new animation to C will be started.
     */
    private static class ItemAnimationInfo {
        int id;

        android.widget.ActionMenuPresenter.MenuItemLayoutInfo menuItemLayoutInfo;

        android.animation.Animator animator;

        int animType;

        static final int MOVE = 0;

        static final int FADE_IN = 1;

        static final int FADE_OUT = 2;

        ItemAnimationInfo(int id, android.widget.ActionMenuPresenter.MenuItemLayoutInfo info, android.animation.Animator anim, int animType) {
            this.id = id;
            menuItemLayoutInfo = info;
            animator = anim;
            this.animType = animType;
        }
    }
}

