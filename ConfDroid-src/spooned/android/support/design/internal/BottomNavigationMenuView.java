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
package android.support.design.internal;


/**
 *
 *
 * @unknown For internal use only.
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class BottomNavigationMenuView extends android.view.ViewGroup implements android.support.v7.view.menu.MenuView {
    private final int mInactiveItemMaxWidth;

    private final int mInactiveItemMinWidth;

    private final int mActiveItemMaxWidth;

    private final int mItemHeight;

    private final android.view.View.OnClickListener mOnClickListener;

    private final android.support.design.internal.BottomNavigationAnimationHelperBase mAnimationHelper;

    private static final android.support.v4.util.Pools.Pool<android.support.design.internal.BottomNavigationItemView> sItemPool = new android.support.v4.util.Pools.SynchronizedPool<>(5);

    private boolean mShiftingMode = true;

    private android.support.design.internal.BottomNavigationItemView[] mButtons;

    private int mActiveButton = 0;

    private android.content.res.ColorStateList mItemIconTint;

    private android.content.res.ColorStateList mItemTextColor;

    private int mItemBackgroundRes;

    private int[] mTempChildWidths;

    private android.support.design.internal.BottomNavigationPresenter mPresenter;

    private android.support.v7.view.menu.MenuBuilder mMenu;

    public BottomNavigationMenuView(android.content.Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        final android.content.res.Resources res = getResources();
        mInactiveItemMaxWidth = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
        mInactiveItemMinWidth = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
        mActiveItemMaxWidth = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
        mItemHeight = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mAnimationHelper = new android.support.design.internal.BottomNavigationAnimationHelperIcs();
        } else {
            mAnimationHelper = new android.support.design.internal.BottomNavigationAnimationHelperBase();
        }
        mOnClickListener = new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                final android.support.design.internal.BottomNavigationItemView itemView = ((android.support.design.internal.BottomNavigationItemView) (v));
                final int itemPosition = itemView.getItemPosition();
                if (!mMenu.performItemAction(itemView.getItemData(), mPresenter, 0)) {
                    activateNewButton(itemPosition);
                }
            }
        };
        mTempChildWidths = new int[android.support.design.internal.BottomNavigationMenu.MAX_ITEM_COUNT];
    }

    @java.lang.Override
    public void initialize(android.support.v7.view.menu.MenuBuilder menu) {
        mMenu = menu;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int count = getChildCount();
        final int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(mItemHeight, android.view.View.MeasureSpec.EXACTLY);
        if (mShiftingMode) {
            final int inactiveCount = count - 1;
            final int activeMaxAvailable = width - (inactiveCount * mInactiveItemMinWidth);
            final int activeWidth = java.lang.Math.min(activeMaxAvailable, mActiveItemMaxWidth);
            final int inactiveMaxAvailable = (width - activeWidth) / inactiveCount;
            final int inactiveWidth = java.lang.Math.min(inactiveMaxAvailable, mInactiveItemMaxWidth);
            int extra = (width - activeWidth) - (inactiveWidth * inactiveCount);
            for (int i = 0; i < count; i++) {
                mTempChildWidths[i] = (i == mActiveButton) ? activeWidth : inactiveWidth;
                if (extra > 0) {
                    mTempChildWidths[i]++;
                    extra--;
                }
            }
        } else {
            final int maxAvailable = width / (count == 0 ? 1 : count);
            final int childWidth = java.lang.Math.min(maxAvailable, mActiveItemMaxWidth);
            int extra = width - (childWidth * count);
            for (int i = 0; i < count; i++) {
                mTempChildWidths[i] = childWidth;
                if (extra > 0) {
                    mTempChildWidths[i]++;
                    extra--;
                }
            }
        }
        int totalWidth = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            child.measure(android.view.View.MeasureSpec.makeMeasureSpec(mTempChildWidths[i], android.view.View.MeasureSpec.EXACTLY), heightSpec);
            android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
            params.width = child.getMeasuredWidth();
            totalWidth += child.getMeasuredWidth();
        }
        setMeasuredDimension(android.support.v4.view.ViewCompat.resolveSizeAndState(totalWidth, android.view.View.MeasureSpec.makeMeasureSpec(totalWidth, android.view.View.MeasureSpec.EXACTLY), 0), android.support.v4.view.ViewCompat.resolveSizeAndState(mItemHeight, heightSpec, 0));
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int width = right - left;
        final int height = bottom - top;
        int used = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            if (android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL) {
                child.layout((width - used) - child.getMeasuredWidth(), 0, width - used, height);
            } else {
                child.layout(used, 0, child.getMeasuredWidth() + used, height);
            }
            used += child.getMeasuredWidth();
        }
    }

    @java.lang.Override
    public int getWindowAnimations() {
        return 0;
    }

    /**
     * Sets the tint which is applied to the menu items' icons.
     *
     * @param tint
     * 		the tint to apply
     */
    public void setIconTintList(android.content.res.ColorStateList tint) {
        mItemIconTint = tint;
        if (mButtons == null)
            return;

        for (android.support.design.internal.BottomNavigationItemView item : mButtons) {
            item.setIconTintList(tint);
        }
    }

    /**
     * Returns the tint which is applied to menu items' icons.
     *
     * @return the ColorStateList that is used to tint menu items' icons
     */
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getIconTintList() {
        return mItemIconTint;
    }

    /**
     * Sets the text color to be used on menu items.
     *
     * @param color
     * 		the ColorStateList used for menu items' text.
     */
    public void setItemTextColor(android.content.res.ColorStateList color) {
        mItemTextColor = color;
        if (mButtons == null)
            return;

        for (android.support.design.internal.BottomNavigationItemView item : mButtons) {
            item.setTextColor(color);
        }
    }

    /**
     * Returns the text color used on menu items.
     *
     * @return the ColorStateList used for menu items' text
     */
    public android.content.res.ColorStateList getItemTextColor() {
        return mItemTextColor;
    }

    /**
     * Sets the resource ID to be used for item background.
     *
     * @param background
     * 		the resource ID of the background
     */
    public void setItemBackgroundRes(int background) {
        mItemBackgroundRes = background;
        if (mButtons == null)
            return;

        for (android.support.design.internal.BottomNavigationItemView item : mButtons) {
            item.setItemBackground(background);
        }
    }

    /**
     * Returns the resource ID for the background of the menu items.
     *
     * @return the resource ID for the background
     */
    public int getItemBackgroundRes() {
        return mItemBackgroundRes;
    }

    public void setPresenter(android.support.design.internal.BottomNavigationPresenter presenter) {
        mPresenter = presenter;
    }

    public void buildMenuView() {
        if (mButtons != null) {
            for (android.support.design.internal.BottomNavigationItemView item : mButtons) {
                android.support.design.internal.BottomNavigationMenuView.sItemPool.release(item);
            }
        }
        removeAllViews();
        if (mMenu.size() == 0) {
            return;
        }
        mButtons = new android.support.design.internal.BottomNavigationItemView[mMenu.size()];
        mShiftingMode = mMenu.size() > 3;
        for (int i = 0; i < mMenu.size(); i++) {
            mPresenter.setUpdateSuspended(true);
            mMenu.getItem(i).setCheckable(true);
            mPresenter.setUpdateSuspended(false);
            android.support.design.internal.BottomNavigationItemView child = getNewItem();
            mButtons[i] = child;
            child.setIconTintList(mItemIconTint);
            child.setTextColor(mItemTextColor);
            child.setItemBackground(mItemBackgroundRes);
            child.setShiftingMode(mShiftingMode);
            child.initialize(((android.support.v7.view.menu.MenuItemImpl) (mMenu.getItem(i))), 0);
            child.setItemPosition(i);
            child.setOnClickListener(mOnClickListener);
            addView(child);
        }
        mActiveButton = java.lang.Math.min(mMenu.size() - 1, mActiveButton);
        mMenu.getItem(mActiveButton).setChecked(true);
    }

    public void updateMenuView() {
        final int menuSize = mMenu.size();
        if (menuSize != mButtons.length) {
            // The size has changed. Rebuild menu view from scratch.
            buildMenuView();
            return;
        }
        for (int i = 0; i < menuSize; i++) {
            mPresenter.setUpdateSuspended(true);
            if (mMenu.getItem(i).isChecked()) {
                mActiveButton = i;
            }
            mButtons[i].initialize(((android.support.v7.view.menu.MenuItemImpl) (mMenu.getItem(i))), 0);
            mPresenter.setUpdateSuspended(false);
        }
    }

    private void activateNewButton(int newButton) {
        if (mActiveButton == newButton)
            return;

        mAnimationHelper.beginDelayedTransition(this);
        mMenu.getItem(newButton).setChecked(true);
        mActiveButton = newButton;
    }

    private android.support.design.internal.BottomNavigationItemView getNewItem() {
        android.support.design.internal.BottomNavigationItemView item = android.support.design.internal.BottomNavigationMenuView.sItemPool.acquire();
        if (item == null) {
            item = new android.support.design.internal.BottomNavigationItemView(getContext());
        }
        return item;
    }
}

