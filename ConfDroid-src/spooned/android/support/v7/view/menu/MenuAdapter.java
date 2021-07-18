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
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class MenuAdapter extends android.widget.BaseAdapter {
    static final int ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;

    android.support.v7.view.menu.MenuBuilder mAdapterMenu;

    private int mExpandedIndex = -1;

    private boolean mForceShowIcon;

    private final boolean mOverflowOnly;

    private final android.view.LayoutInflater mInflater;

    public MenuAdapter(android.support.v7.view.menu.MenuBuilder menu, android.view.LayoutInflater inflater, boolean overflowOnly) {
        mOverflowOnly = overflowOnly;
        mInflater = inflater;
        mAdapterMenu = menu;
        findExpandedIndex();
    }

    public boolean getForceShowIcon() {
        return mForceShowIcon;
    }

    public void setForceShowIcon(boolean forceShow) {
        mForceShowIcon = forceShow;
    }

    @java.lang.Override
    public int getCount() {
        java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = (mOverflowOnly) ? mAdapterMenu.getNonActionItems() : mAdapterMenu.getVisibleItems();
        if (mExpandedIndex < 0) {
            return items.size();
        }
        return items.size() - 1;
    }

    public android.support.v7.view.menu.MenuBuilder getAdapterMenu() {
        return mAdapterMenu;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuItemImpl getItem(int position) {
        java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = (mOverflowOnly) ? mAdapterMenu.getNonActionItems() : mAdapterMenu.getVisibleItems();
        if ((mExpandedIndex >= 0) && (position >= mExpandedIndex)) {
            position++;
        }
        return items.get(position);
    }

    @java.lang.Override
    public long getItemId(int position) {
        // Since a menu item's ID is optional, we'll use the position as an
        // ID for the item in the AdapterView
        return position;
    }

    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.support.v7.view.menu.MenuAdapter.ITEM_LAYOUT, parent, false);
        }
        android.support.v7.view.menu.MenuView.ItemView itemView = ((android.support.v7.view.menu.MenuView.ItemView) (convertView));
        if (mForceShowIcon) {
            ((android.support.v7.view.menu.ListMenuItemView) (convertView)).setForceShowIcon(true);
        }
        itemView.initialize(getItem(position), 0);
        return convertView;
    }

    void findExpandedIndex() {
        final android.support.v7.view.menu.MenuItemImpl expandedItem = mAdapterMenu.getExpandedItem();
        if (expandedItem != null) {
            final java.util.ArrayList<android.support.v7.view.menu.MenuItemImpl> items = mAdapterMenu.getNonActionItems();
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

