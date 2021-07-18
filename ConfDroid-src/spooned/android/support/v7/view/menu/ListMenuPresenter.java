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
package android.support.v7.view.menu;


/**
 * MenuPresenter for list-style menus.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ListMenuPresenter implements android.support.v7.view.menu.MenuPresenter , android.widget.AdapterView.OnItemClickListener {
    private static final java.lang.String TAG = "ListMenuPresenter";

    android.content.Context mContext;

    android.view.LayoutInflater mInflater;

    android.support.v7.view.menu.MenuBuilder mMenu;

    android.support.v7.view.menu.ExpandedMenuView mMenuView;

    int mItemIndexOffset;

    int mThemeRes;

    int mItemLayoutRes;

    private android.support.v7.view.menu.MenuPresenter.Callback mCallback;

    android.support.v7.view.menu.ListMenuPresenter.MenuAdapter mAdapter;

    private int mId;

    public static final java.lang.String VIEWS_TAG = "android:menu:list";

    /**
     * Construct a new ListMenuPresenter.
     *
     * @param context
     * 		Context to use for theming. This will supersede the context provided
     * 		to initForMenu when this presenter is added.
     * @param itemLayoutRes
     * 		Layout resource for individual item views.
     */
    public ListMenuPresenter(android.content.Context context, int itemLayoutRes) {
        this(itemLayoutRes, 0);
        mContext = context;
        mInflater = android.view.LayoutInflater.from(mContext);
    }

    /**
     * Construct a new ListMenuPresenter.
     *
     * @param itemLayoutRes
     * 		Layout resource for individual item views.
     * @param themeRes
     * 		Resource ID of a theme to use for views.
     */
    public ListMenuPresenter(int itemLayoutRes, int themeRes) {
        mItemLayoutRes = itemLayoutRes;
        mThemeRes = themeRes;
    }

    @java.lang.Override
    public void initForMenu(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu) {
        if (mThemeRes != 0) {
            mContext = new android.view.ContextThemeWrapper(context, mThemeRes);
            mInflater = android.view.LayoutInflater.from(mContext);
        } else
            if (mContext != null) {
                mContext = context;
                if (mInflater == null) {
                    mInflater = android.view.LayoutInflater.from(mContext);
                }
            }

        mMenu = menu;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        if (mMenuView == null) {
            mMenuView = ((android.support.v7.view.menu.ExpandedMenuView) (mInflater.inflate(R.layout.abc_expanded_menu_layout, root, false)));
            if (mAdapter == null) {
                mAdapter = new android.support.v7.view.menu.ListMenuPresenter.MenuAdapter();
            }
            mMenuView.setAdapter(mAdapter);
            mMenuView.setOnItemClickListener(this);
        }
        return mMenuView;
    }

    /**
     * Call this instead of getMenuView if you want to manage your own ListView.
     * For proper operation, the ListView hosting this adapter should add
     * this presenter as an OnItemClickListener.
     *
     * @return A ListAdapter containing the items in the menu.
     */
    public android.widget.ListAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new android.support.v7.view.menu.ListMenuPresenter.MenuAdapter();
        }
        return mAdapter;
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();

    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mCallback = cb;
    }

    @java.lang.Override
    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems())
            return false;

        // The window manager will give us a token.
        new android.support.v7.view.menu.MenuDialogHelper(subMenu).show(null);
        if (mCallback != null) {
            mCallback.onOpenSubMenu(subMenu);
        }
        return true;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        if (mCallback != null) {
            mCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    int getItemIndexOffset() {
        return mItemIndexOffset;
    }

    public void setItemIndexOffset(int offset) {
        mItemIndexOffset = offset;
        if (mMenuView != null) {
            updateMenuView(false);
        }
    }

    @java.lang.Override
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        mMenu.performItemAction(mAdapter.getItem(position), this, 0);
    }

    @java.lang.Override
    public boolean flagActionItems() {
        return false;
    }

    public boolean expandItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    public boolean collapseItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
        return false;
    }

    public void saveHierarchyState(android.os.Bundle outState) {
        android.util.SparseArray<android.os.Parcelable> viewStates = new android.util.SparseArray<android.os.Parcelable>();
        if (mMenuView != null) {
            ((android.view.View) (mMenuView)).saveHierarchyState(viewStates);
        }
        outState.putSparseParcelableArray(android.support.v7.view.menu.ListMenuPresenter.VIEWS_TAG, viewStates);
    }

    public void restoreHierarchyState(android.os.Bundle inState) {
        android.util.SparseArray<android.os.Parcelable> viewStates = inState.getSparseParcelableArray(android.support.v7.view.menu.ListMenuPresenter.VIEWS_TAG);
        if (viewStates != null) {
            ((android.view.View) (mMenuView)).restoreHierarchyState(viewStates);
        }
    }

    public void setId(int id) {
        mId = id;
    }

    @java.lang.Override
    public int getId() {
        return mId;
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        if (mMenuView == null) {
            return null;
        }
        android.os.Bundle state = new android.os.Bundle();
        saveHierarchyState(state);
        return state;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        restoreHierarchyState(((android.os.Bundle) (state)));
    }

    private class MenuAdapter extends android.widget.BaseAdapter {
        private int mExpandedIndex = -1;

        public MenuAdapter() {
            findExpandedIndex();
        }

        public int getCount() {
            java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = mMenu.getNonActionItems();
            int count = items.size() - mItemIndexOffset;
            if (mExpandedIndex < 0) {
                return count;
            }
            return count - 1;
        }

        public android.support.v7.view.menu.MenuItemImpl getItem(int position) {
            java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = mMenu.getNonActionItems();
            position += mItemIndexOffset;
            if ((mExpandedIndex >= 0) && (position >= mExpandedIndex)) {
                position++;
            }
            return items.get(position);
        }

        public long getItemId(int position) {
            // Since a menu item's ID is optional, we'll use the position as an
            // ID for the item in the AdapterView
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(mItemLayoutRes, parent, false);
            }
            android.support.v7.view.menu.MenuView.ItemView itemView = ((android.support.v7.view.menu.MenuView.ItemView) (convertView));
            itemView.initialize(getItem(position), 0);
            return convertView;
        }

        void findExpandedIndex() {
            final android.support.v7.view.menu.MenuItemImpl expandedItem = mMenu.getExpandedItem();
            if (expandedItem != null) {
                final java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = mMenu.getNonActionItems();
                final int count = items.size();
                for (int i = 0; i < count; i++) {
                    final android.support.v7.view.menu.MenuItemImpl item = items.get(i);
                    if (item == expandedItem) {
                        mExpandedIndex = i;
                        return;
                    }
                }
            }
            mExpandedIndex = -1;
        }

        @java.lang.Override
        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }
}

