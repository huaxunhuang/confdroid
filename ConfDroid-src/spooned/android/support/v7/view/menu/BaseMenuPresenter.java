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
package android.support.v7.view.menu;


/**
 * Base class for MenuPresenters that have a consistent container view and item views. Behaves
 * similarly to an AdapterView in that existing item views will be reused if possible when items
 * change.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public abstract class BaseMenuPresenter implements android.support.v7.view.menu.MenuPresenter {
    protected android.content.Context mSystemContext;

    protected android.content.Context mContext;

    protected android.support.v7.view.menu.MenuBuilder mMenu;

    protected android.view.LayoutInflater mSystemInflater;

    protected android.view.LayoutInflater mInflater;

    private android.support.v7.view.menu.MenuPresenter.Callback mCallback;

    private int mMenuLayoutRes;

    private int mItemLayoutRes;

    protected android.support.v7.view.menu.MenuView mMenuView;

    private int mId;

    /**
     * Construct a new BaseMenuPresenter.
     *
     * @param context
     * 		Context for generating system-supplied views
     * @param menuLayoutRes
     * 		Layout resource ID for the menu container view
     * @param itemLayoutRes
     * 		Layout resource ID for a single item view
     */
    public BaseMenuPresenter(android.content.Context context, int menuLayoutRes, int itemLayoutRes) {
        mSystemContext = context;
        mSystemInflater = android.view.LayoutInflater.from(context);
        mMenuLayoutRes = menuLayoutRes;
        mItemLayoutRes = itemLayoutRes;
    }

    @java.lang.Override
    public void initForMenu(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu) {
        mContext = context;
        mInflater = android.view.LayoutInflater.from(mContext);
        mMenu = menu;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        if (mMenuView == null) {
            mMenuView = ((android.support.v7.view.menu.MenuView) (mSystemInflater.inflate(mMenuLayoutRes, root, false)));
            mMenuView.initialize(mMenu);
            updateMenuView(true);
        }
        return mMenuView;
    }

    /**
     * Reuses item views when it can
     */
    public void updateMenuView(boolean cleared) {
        final android.view.ViewGroup parent = ((android.view.ViewGroup) (mMenuView));
        if (parent == null)
            return;

        int childIndex = 0;
        if (mMenu != null) {
            mMenu.flagActionItems();
            java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> visibleItems = mMenu.getVisibleItems();
            final int itemCount = visibleItems.size();
            for (int i = 0; i < itemCount; i++) {
                android.support.v7.view.menu.MenuItemImpl item = visibleItems.get(i);
                if (shouldIncludeItem(childIndex, item)) {
                    final android.view.View convertView = parent.getChildAt(childIndex);
                    final android.support.v7.view.menu.MenuItemImpl oldItem = (convertView instanceof android.support.v7.view.menu.MenuView.ItemView) ? ((android.support.v7.view.menu.MenuView.ItemView) (convertView)).getItemData() : null;
                    final android.view.View itemView = getItemView(item, convertView, parent);
                    if (item != oldItem) {
                        // Don't let old states linger with new data.
                        itemView.setPressed(false);
                        android.support.v4.view.ViewCompat.jumpDrawablesToCurrentState(itemView);
                    }
                    if (itemView != convertView) {
                        addItemView(itemView, childIndex);
                    }
                    childIndex++;
                }
            }
        }
        // Remove leftover views.
        while (childIndex < parent.getChildCount()) {
            if (!filterLeftoverView(parent, childIndex)) {
                childIndex++;
            }
        } 
    }

    /**
     * Add an item view at the given index.
     *
     * @param itemView
     * 		View to add
     * @param childIndex
     * 		Index within the parent to insert at
     */
    protected void addItemView(android.view.View itemView, int childIndex) {
        final android.view.ViewGroup currentParent = ((android.view.ViewGroup) (itemView.getParent()));
        if (currentParent != null) {
            currentParent.removeView(itemView);
        }
        ((android.view.ViewGroup) (mMenuView)).addView(itemView, childIndex);
    }

    /**
     * Filter the child view at index and remove it if appropriate.
     *
     * @param parent
     * 		Parent to filter from
     * @param childIndex
     * 		Index to filter
     * @return true if the child view at index was removed
     */
    protected boolean filterLeftoverView(android.view.ViewGroup parent, int childIndex) {
        parent.removeViewAt(childIndex);
        return true;
    }

    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mCallback = cb;
    }

    public android.support.v7.view.menu.MenuPresenter.Callback getCallback() {
        return mCallback;
    }

    /**
     * Create a new item view that can be re-bound to other item data later.
     *
     * @return The new item view
     */
    public android.support.v7.view.menu.MenuView.ItemView createItemView(android.view.ViewGroup parent) {
        return ((android.support.v7.view.menu.MenuView.ItemView) (mSystemInflater.inflate(mItemLayoutRes, parent, false)));
    }

    /**
     * Prepare an item view for use. See AdapterView for the basic idea at work here.
     * This may require creating a new item view, but well-behaved implementations will
     * re-use the view passed as convertView if present. The returned view will be populated
     * with data from the item parameter.
     *
     * @param item
     * 		Item to present
     * @param convertView
     * 		Existing view to reuse
     * @param parent
     * 		Intended parent view - use for inflation.
     * @return View that presents the requested menu item
     */
    public android.view.View getItemView(android.support.v7.view.menu.MenuItemImpl item, android.view.View convertView, android.view.ViewGroup parent) {
        android.support.v7.view.menu.MenuView.ItemView itemView;
        if (convertView instanceof android.support.v7.view.menu.MenuView.ItemView) {
            itemView = ((android.support.v7.view.menu.MenuView.ItemView) (convertView));
        } else {
            itemView = createItemView(parent);
        }
        bindItemView(item, itemView);
        return ((android.view.View) (itemView));
    }

    /**
     * Bind item data to an existing item view.
     *
     * @param item
     * 		Item to bind
     * @param itemView
     * 		View to populate with item data
     */
    public abstract void bindItemView(android.support.v7.view.menu.MenuItemImpl item, android.support.v7.view.menu.MenuView.ItemView itemView);

    /**
     * Filter item by child index and item data.
     *
     * @param childIndex
     * 		Indended presentation index of this item
     * @param item
     * 		Item to present
     * @return true if this item should be included in this menu presentation; false otherwise
     */
    public boolean shouldIncludeItem(int childIndex, android.support.v7.view.menu.MenuItemImpl item) {
        return true;
    }

    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        if (mCallback != null) {
            mCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder menu) {
        if (mCallback != null) {
            return mCallback.onOpenSubMenu(menu);
        }
        return false;
    }

    public boolean flagActionItems() {
        return false;
    }

    public boolean expandItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    public boolean collapseItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}

