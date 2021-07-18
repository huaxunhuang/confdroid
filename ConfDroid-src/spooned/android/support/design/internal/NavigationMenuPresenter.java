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
 * limitations under the License.
 */
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NavigationMenuPresenter implements android.support.v7.view.menu.MenuPresenter {
    private static final java.lang.String STATE_HIERARCHY = "android:menu:list";

    private static final java.lang.String STATE_ADAPTER = "android:menu:adapter";

    private android.support.design.internal.NavigationMenuView mMenuView;

    android.widget.LinearLayout mHeaderLayout;

    private android.support.v7.view.menu.MenuPresenter.Callback mCallback;

    android.support.v7.view.menu.MenuBuilder mMenu;

    private int mId;

    android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter mAdapter;

    android.view.LayoutInflater mLayoutInflater;

    int mTextAppearance;

    boolean mTextAppearanceSet;

    android.content.res.ColorStateList mTextColor;

    android.content.res.ColorStateList mIconTintList;

    android.graphics.drawable.Drawable mItemBackground;

    /**
     * Padding to be inserted at the top of the list to avoid the first menu item
     * from being placed underneath the status bar.
     */
    private int mPaddingTopDefault;

    /**
     * Padding for separators between items
     */
    int mPaddingSeparator;

    @java.lang.Override
    public void initForMenu(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu) {
        mLayoutInflater = android.view.LayoutInflater.from(context);
        mMenu = menu;
        android.content.res.Resources res = context.getResources();
        mPaddingSeparator = res.getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        if (mMenuView == null) {
            mMenuView = ((android.support.design.internal.NavigationMenuView) (mLayoutInflater.inflate(R.layout.design_navigation_menu, root, false)));
            if (mAdapter == null) {
                mAdapter = new android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter();
            }
            mHeaderLayout = ((android.widget.LinearLayout) (mLayoutInflater.inflate(R.layout.design_navigation_item_header, mMenuView, false)));
            mMenuView.setAdapter(mAdapter);
        }
        return mMenuView;
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        if (mAdapter != null) {
            mAdapter.update();
        }
    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mCallback = cb;
    }

    @java.lang.Override
    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        return false;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        if (mCallback != null) {
            mCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    @java.lang.Override
    public boolean flagActionItems() {
        return false;
    }

    @java.lang.Override
    public boolean expandItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    @java.lang.Override
    public boolean collapseItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    @java.lang.Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // API 9-10 does not support ClassLoaderCreator, therefore things can crash if they're
            // loaded via different loaders. Rather than crash we just won't save state on those
            // platforms
            final android.os.Bundle state = new android.os.Bundle();
            if (mMenuView != null) {
                android.util.SparseArray<android.os.Parcelable> hierarchy = new android.util.SparseArray<>();
                mMenuView.saveHierarchyState(hierarchy);
                state.putSparseParcelableArray(android.support.design.internal.NavigationMenuPresenter.STATE_HIERARCHY, hierarchy);
            }
            if (mAdapter != null) {
                state.putBundle(android.support.design.internal.NavigationMenuPresenter.STATE_ADAPTER, mAdapter.createInstanceState());
            }
            return state;
        }
        return null;
    }

    @java.lang.Override
    public void onRestoreInstanceState(final android.os.Parcelable parcelable) {
        if (parcelable instanceof android.os.Bundle) {
            android.os.Bundle state = ((android.os.Bundle) (parcelable));
            android.util.SparseArray<android.os.Parcelable> hierarchy = state.getSparseParcelableArray(android.support.design.internal.NavigationMenuPresenter.STATE_HIERARCHY);
            if (hierarchy != null) {
                mMenuView.restoreHierarchyState(hierarchy);
            }
            android.os.Bundle adapterState = state.getBundle(android.support.design.internal.NavigationMenuPresenter.STATE_ADAPTER);
            if (adapterState != null) {
                mAdapter.restoreInstanceState(adapterState);
            }
        }
    }

    public void setCheckedItem(android.support.v7.view.menu.MenuItemImpl item) {
        mAdapter.setCheckedItem(item);
    }

    public android.view.View inflateHeaderView(@android.support.annotation.LayoutRes
    int res) {
        android.view.View view = mLayoutInflater.inflate(res, mHeaderLayout, false);
        addHeaderView(view);
        return view;
    }

    public void addHeaderView(@android.support.annotation.NonNull
    android.view.View view) {
        mHeaderLayout.addView(view);
        // The padding on top should be cleared.
        mMenuView.setPadding(0, 0, 0, mMenuView.getPaddingBottom());
    }

    public void removeHeaderView(@android.support.annotation.NonNull
    android.view.View view) {
        mHeaderLayout.removeView(view);
        if (mHeaderLayout.getChildCount() == 0) {
            mMenuView.setPadding(0, mPaddingTopDefault, 0, mMenuView.getPaddingBottom());
        }
    }

    public int getHeaderCount() {
        return mHeaderLayout.getChildCount();
    }

    public android.view.View getHeaderView(int index) {
        return mHeaderLayout.getChildAt(index);
    }

    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemTintList() {
        return mIconTintList;
    }

    public void setItemIconTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mIconTintList = tint;
        updateMenuView(false);
    }

    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemTextColor() {
        return mTextColor;
    }

    public void setItemTextColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList textColor) {
        mTextColor = textColor;
        updateMenuView(false);
    }

    public void setItemTextAppearance(@android.support.annotation.StyleRes
    int resId) {
        mTextAppearance = resId;
        mTextAppearanceSet = true;
        updateMenuView(false);
    }

    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getItemBackground() {
        return mItemBackground;
    }

    public void setItemBackground(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable itemBackground) {
        mItemBackground = itemBackground;
        updateMenuView(false);
    }

    public void setUpdateSuspended(boolean updateSuspended) {
        if (mAdapter != null) {
            mAdapter.setUpdateSuspended(updateSuspended);
        }
    }

    public void dispatchApplyWindowInsets(android.support.v4.view.WindowInsetsCompat insets) {
        int top = insets.getSystemWindowInsetTop();
        if (mPaddingTopDefault != top) {
            mPaddingTopDefault = top;
            if (mHeaderLayout.getChildCount() == 0) {
                mMenuView.setPadding(0, mPaddingTopDefault, 0, mMenuView.getPaddingBottom());
            }
        }
        android.support.v4.view.ViewCompat.dispatchApplyWindowInsets(mHeaderLayout, insets);
    }

    private static abstract class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(android.view.View itemView) {
            super(itemView);
        }
    }

    private static class NormalViewHolder extends android.support.design.internal.NavigationMenuPresenter.ViewHolder {
        public NormalViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent, android.view.View.OnClickListener listener) {
            super(inflater.inflate(R.layout.design_navigation_item, parent, false));
            itemView.setOnClickListener(listener);
        }
    }

    private static class SubheaderViewHolder extends android.support.design.internal.NavigationMenuPresenter.ViewHolder {
        public SubheaderViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent) {
            super(inflater.inflate(R.layout.design_navigation_item_subheader, parent, false));
        }
    }

    private static class SeparatorViewHolder extends android.support.design.internal.NavigationMenuPresenter.ViewHolder {
        public SeparatorViewHolder(android.view.LayoutInflater inflater, android.view.ViewGroup parent) {
            super(inflater.inflate(R.layout.design_navigation_item_separator, parent, false));
        }
    }

    private static class HeaderViewHolder extends android.support.design.internal.NavigationMenuPresenter.ViewHolder {
        public HeaderViewHolder(android.view.View itemView) {
            super(itemView);
        }
    }

    /**
     * Handles click events for the menu items. The items has to be {@link NavigationMenuItemView}.
     */
    final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            android.support.design.internal.NavigationMenuItemView itemView = ((android.support.design.internal.NavigationMenuItemView) (v));
            setUpdateSuspended(true);
            android.support.v7.view.menu.MenuItemImpl item = itemView.getItemData();
            boolean result = mMenu.performItemAction(item, android.support.design.internal.NavigationMenuPresenter.this, 0);
            if (((item != null) && item.isCheckable()) && result) {
                mAdapter.setCheckedItem(item);
            }
            setUpdateSuspended(false);
            updateMenuView(false);
        }
    };

    private class NavigationMenuAdapter extends android.support.v7.widget.RecyclerView.Adapter<android.support.design.internal.NavigationMenuPresenter.ViewHolder> {
        private static final java.lang.String STATE_CHECKED_ITEM = "android:menu:checked";

        private static final java.lang.String STATE_ACTION_VIEWS = "android:menu:action_views";

        private static final int VIEW_TYPE_NORMAL = 0;

        private static final int VIEW_TYPE_SUBHEADER = 1;

        private static final int VIEW_TYPE_SEPARATOR = 2;

        private static final int VIEW_TYPE_HEADER = 3;

        private final java.util.ArrayList<android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem> mItems = new java.util.ArrayList<>();

        private android.support.v7.view.menu.MenuItemImpl mCheckedItem;

        private boolean mUpdateSuspended;

        NavigationMenuAdapter() {
            prepareMenuItems();
        }

        @java.lang.Override
        public long getItemId(int position) {
            return position;
        }

        @java.lang.Override
        public int getItemCount() {
            return mItems.size();
        }

        @java.lang.Override
        public int getItemViewType(int position) {
            android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem item = mItems.get(position);
            if (item instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuSeparatorItem) {
                return android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SEPARATOR;
            } else
                if (item instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuHeaderItem) {
                    return android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_HEADER;
                } else
                    if (item instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) {
                        android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem textItem = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (item));
                        if (textItem.getMenuItem().hasSubMenu()) {
                            return android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SUBHEADER;
                        } else {
                            return android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_NORMAL;
                        }
                    }


            throw new java.lang.RuntimeException("Unknown item type.");
        }

        @java.lang.Override
        public android.support.design.internal.NavigationMenuPresenter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            switch (viewType) {
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_NORMAL :
                    return new android.support.design.internal.NavigationMenuPresenter.NormalViewHolder(mLayoutInflater, parent, mOnClickListener);
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SUBHEADER :
                    return new android.support.design.internal.NavigationMenuPresenter.SubheaderViewHolder(mLayoutInflater, parent);
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SEPARATOR :
                    return new android.support.design.internal.NavigationMenuPresenter.SeparatorViewHolder(mLayoutInflater, parent);
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_HEADER :
                    return new android.support.design.internal.NavigationMenuPresenter.HeaderViewHolder(mHeaderLayout);
            }
            return null;
        }

        @java.lang.Override
        public void onBindViewHolder(android.support.design.internal.NavigationMenuPresenter.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_NORMAL :
                    {
                        android.support.design.internal.NavigationMenuItemView itemView = ((android.support.design.internal.NavigationMenuItemView) (holder.itemView));
                        itemView.setIconTintList(mIconTintList);
                        if (mTextAppearanceSet) {
                            itemView.setTextAppearance(mTextAppearance);
                        }
                        if (mTextColor != null) {
                            itemView.setTextColor(mTextColor);
                        }
                        android.support.v4.view.ViewCompat.setBackground(itemView, mItemBackground != null ? mItemBackground.getConstantState().newDrawable() : null);
                        android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem item = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (mItems.get(position)));
                        itemView.setNeedsEmptyIcon(item.needsEmptyIcon);
                        itemView.initialize(item.getMenuItem(), 0);
                        break;
                    }
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SUBHEADER :
                    {
                        android.widget.TextView subHeader = ((android.widget.TextView) (holder.itemView));
                        android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem item = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (mItems.get(position)));
                        subHeader.setText(item.getMenuItem().getTitle());
                        break;
                    }
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_SEPARATOR :
                    {
                        android.support.design.internal.NavigationMenuPresenter.NavigationMenuSeparatorItem item = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuSeparatorItem) (mItems.get(position)));
                        holder.itemView.setPadding(0, item.getPaddingTop(), 0, item.getPaddingBottom());
                        break;
                    }
                case android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.VIEW_TYPE_HEADER :
                    {
                        break;
                    }
            }
        }

        @java.lang.Override
        public void onViewRecycled(android.support.design.internal.NavigationMenuPresenter.ViewHolder holder) {
            if (holder instanceof android.support.design.internal.NavigationMenuPresenter.NormalViewHolder) {
                ((android.support.design.internal.NavigationMenuItemView) (holder.itemView)).recycle();
            }
        }

        public void update() {
            prepareMenuItems();
            notifyDataSetChanged();
        }

        /**
         * Flattens the visible menu items of {@link #mMenu} into {@link #mItems},
         * while inserting separators between items when necessary.
         */
        private void prepareMenuItems() {
            if (mUpdateSuspended) {
                return;
            }
            mUpdateSuspended = true;
            mItems.clear();
            mItems.add(new android.support.design.internal.NavigationMenuPresenter.NavigationMenuHeaderItem());
            int currentGroupId = -1;
            int currentGroupStart = 0;
            boolean currentGroupHasIcon = false;
            for (int i = 0, totalSize = mMenu.getVisibleItems().size(); i < totalSize; i++) {
                android.support.v7.view.menu.MenuItemImpl item = mMenu.getVisibleItems().get(i);
                if (item.isChecked()) {
                    setCheckedItem(item);
                }
                if (item.isCheckable()) {
                    item.setExclusiveCheckable(false);
                }
                if (item.hasSubMenu()) {
                    android.view.SubMenu subMenu = item.getSubMenu();
                    if (subMenu.hasVisibleItems()) {
                        if (i != 0) {
                            mItems.add(new android.support.design.internal.NavigationMenuPresenter.NavigationMenuSeparatorItem(mPaddingSeparator, 0));
                        }
                        mItems.add(new android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem(item));
                        boolean subMenuHasIcon = false;
                        int subMenuStart = mItems.size();
                        for (int j = 0, size = subMenu.size(); j < size; j++) {
                            android.support.v7.view.menu.MenuItemImpl subMenuItem = ((android.support.v7.view.menu.MenuItemImpl) (subMenu.getItem(j)));
                            if (subMenuItem.isVisible()) {
                                if ((!subMenuHasIcon) && (subMenuItem.getIcon() != null)) {
                                    subMenuHasIcon = true;
                                }
                                if (subMenuItem.isCheckable()) {
                                    subMenuItem.setExclusiveCheckable(false);
                                }
                                if (item.isChecked()) {
                                    setCheckedItem(item);
                                }
                                mItems.add(new android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem(subMenuItem));
                            }
                        }
                        if (subMenuHasIcon) {
                            appendTransparentIconIfMissing(subMenuStart, mItems.size());
                        }
                    }
                } else {
                    int groupId = item.getGroupId();
                    if (groupId != currentGroupId) {
                        // first item in group
                        currentGroupStart = mItems.size();
                        currentGroupHasIcon = item.getIcon() != null;
                        if (i != 0) {
                            currentGroupStart++;
                            mItems.add(new android.support.design.internal.NavigationMenuPresenter.NavigationMenuSeparatorItem(mPaddingSeparator, mPaddingSeparator));
                        }
                    } else
                        if ((!currentGroupHasIcon) && (item.getIcon() != null)) {
                            currentGroupHasIcon = true;
                            appendTransparentIconIfMissing(currentGroupStart, mItems.size());
                        }

                    android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem textItem = new android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem(item);
                    textItem.needsEmptyIcon = currentGroupHasIcon;
                    mItems.add(textItem);
                    currentGroupId = groupId;
                }
            }
            mUpdateSuspended = false;
        }

        private void appendTransparentIconIfMissing(int startIndex, int endIndex) {
            for (int i = startIndex; i < endIndex; i++) {
                android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem textItem = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (mItems.get(i)));
                textItem.needsEmptyIcon = true;
            }
        }

        public void setCheckedItem(android.support.v7.view.menu.MenuItemImpl checkedItem) {
            if ((mCheckedItem == checkedItem) || (!checkedItem.isCheckable())) {
                return;
            }
            if (mCheckedItem != null) {
                mCheckedItem.setChecked(false);
            }
            mCheckedItem = checkedItem;
            checkedItem.setChecked(true);
        }

        public android.os.Bundle createInstanceState() {
            android.os.Bundle state = new android.os.Bundle();
            if (mCheckedItem != null) {
                state.putInt(android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.STATE_CHECKED_ITEM, mCheckedItem.getItemId());
            }
            // Store the states of the action views.
            android.util.SparseArray<android.support.design.internal.ParcelableSparseArray> actionViewStates = new android.util.SparseArray<>();
            for (android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem navigationMenuItem : mItems) {
                if (navigationMenuItem instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) {
                    android.support.v7.view.menu.MenuItemImpl item = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (navigationMenuItem)).getMenuItem();
                    android.view.View actionView = (item != null) ? item.getActionView() : null;
                    if (actionView != null) {
                        android.support.design.internal.ParcelableSparseArray container = new android.support.design.internal.ParcelableSparseArray();
                        actionView.saveHierarchyState(container);
                        actionViewStates.put(item.getItemId(), container);
                    }
                }
            }
            state.putSparseParcelableArray(android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.STATE_ACTION_VIEWS, actionViewStates);
            return state;
        }

        public void restoreInstanceState(android.os.Bundle state) {
            int checkedItem = state.getInt(android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.STATE_CHECKED_ITEM, 0);
            if (checkedItem != 0) {
                mUpdateSuspended = true;
                for (android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem item : mItems) {
                    if (item instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) {
                        android.support.v7.view.menu.MenuItemImpl menuItem = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (item)).getMenuItem();
                        if ((menuItem != null) && (menuItem.getItemId() == checkedItem)) {
                            setCheckedItem(menuItem);
                            break;
                        }
                    }
                }
                mUpdateSuspended = false;
                prepareMenuItems();
            }
            // Restore the states of the action views.
            android.util.SparseArray<android.support.design.internal.ParcelableSparseArray> actionViewStates = state.getSparseParcelableArray(android.support.design.internal.NavigationMenuPresenter.NavigationMenuAdapter.STATE_ACTION_VIEWS);
            for (android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem navigationMenuItem : mItems) {
                if (navigationMenuItem instanceof android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) {
                    android.support.v7.view.menu.MenuItemImpl item = ((android.support.design.internal.NavigationMenuPresenter.NavigationMenuTextItem) (navigationMenuItem)).getMenuItem();
                    android.view.View actionView = (item != null) ? item.getActionView() : null;
                    if (actionView != null) {
                        actionView.restoreHierarchyState(actionViewStates.get(item.getItemId()));
                    }
                }
            }
        }

        public void setUpdateSuspended(boolean updateSuspended) {
            mUpdateSuspended = updateSuspended;
        }
    }

    /**
     * Unified data model for all sorts of navigation menu items.
     */
    private interface NavigationMenuItem {}

    /**
     * Normal or subheader items.
     */
    private static class NavigationMenuTextItem implements android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem {
        private final android.support.v7.view.menu.MenuItemImpl mMenuItem;

        boolean needsEmptyIcon;

        NavigationMenuTextItem(android.support.v7.view.menu.MenuItemImpl item) {
            mMenuItem = item;
        }

        public android.support.v7.view.menu.MenuItemImpl getMenuItem() {
            return mMenuItem;
        }
    }

    /**
     * Separator items.
     */
    private static class NavigationMenuSeparatorItem implements android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem {
        private final int mPaddingTop;

        private final int mPaddingBottom;

        public NavigationMenuSeparatorItem(int paddingTop, int paddingBottom) {
            mPaddingTop = paddingTop;
            mPaddingBottom = paddingBottom;
        }

        public int getPaddingTop() {
            return mPaddingTop;
        }

        public int getPaddingBottom() {
            return mPaddingBottom;
        }
    }

    /**
     * Header (not subheader) items.
     */
    // The actual content is hold by NavigationMenuPresenter#mHeaderLayout.
    private static class NavigationMenuHeaderItem implements android.support.design.internal.NavigationMenuPresenter.NavigationMenuItem {
        NavigationMenuHeaderItem() {
        }
    }
}

