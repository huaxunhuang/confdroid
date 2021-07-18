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
 * Base class for a menu popup abstraction - i.e., some type of menu, housed in a popup window
 * environment.
 */
abstract class MenuPopup implements android.support.v7.view.menu.MenuPresenter , android.support.v7.view.menu.ShowableListMenu , android.widget.AdapterView.OnItemClickListener {
    private android.graphics.Rect mEpicenterBounds;

    public abstract void setForceShowIcon(boolean forceShow);

    /**
     * Adds the given menu to the popup, if it is capable of displaying submenus within itself.
     * If menu is the first menu shown, it won't be displayed until show() is called.
     * If the popup was already showing, adding a submenu via this method will cause that new
     * submenu to be shown immediately (that is, if this MenuPopup implementation is capable of
     * showing its own submenus).
     *
     * @param menu
     * 		
     */
    public abstract void addMenu(android.support.v7.view.menu.MenuBuilder menu);

    public abstract void setGravity(int dropDownGravity);

    public abstract void setAnchorView(android.view.View anchor);

    public abstract void setHorizontalOffset(int x);

    public abstract void setVerticalOffset(int y);

    /**
     * Specifies the anchor-relative bounds of the popup's transition
     * epicenter.
     *
     * @param bounds
     * 		anchor-relative bounds
     */
    public void setEpicenterBounds(android.graphics.Rect bounds) {
        mEpicenterBounds = bounds;
    }

    /**
     *
     *
     * @return anchor-relative bounds of the popup's transition epicenter
     */
    public android.graphics.Rect getEpicenterBounds() {
        return mEpicenterBounds;
    }

    /**
     * Set whether a title entry should be shown in the popup menu (if a title exists for the
     * menu).
     *
     * @param showTitle
     * 		
     */
    public abstract void setShowTitle(boolean showTitle);

    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param listener
     * 		Listener that will be notified when the popup is dismissed.
     */
    public abstract void setOnDismissListener(android.widget.PopupWindow.OnDismissListener listener);

    @java.lang.Override
    public void initForMenu(@android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.Nullable
    android.support.v7.view.menu.MenuBuilder menu) {
        // Don't need to do anything; we added as a presenter in the constructor.
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
        throw new java.lang.UnsupportedOperationException("MenuPopups manage their own views");
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
        return 0;
    }

    @java.lang.Override
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        android.widget.ListAdapter outerAdapter = ((android.widget.ListAdapter) (parent.getAdapter()));
        android.support.v7.view.menu.MenuAdapter wrappedAdapter = android.support.v7.view.menu.MenuPopup.toMenuAdapter(outerAdapter);
        // Use the position from the outer adapter so that if a header view was added, we don't get
        // an off-by-1 error in position.
        // always make sure that we show the sub-menu
        wrappedAdapter.mAdapterMenu.performItemAction(((android.view.MenuItem) (outerAdapter.getItem(position))), this, closeMenuOnSubMenuOpened() ? 0 : android.support.v4.internal.view.SupportMenu.FLAG_KEEP_OPEN_ON_SUBMENU_OPENED);
    }

    /**
     * Measures the width of the given menu view.
     *
     * @param view
     * 		The view to measure.
     * @return The width.
     */
    protected static int measureIndividualMenuWidth(android.widget.ListAdapter adapter, android.view.ViewGroup parent, android.content.Context context, int maxAllowedWidth) {
        // Menus don't tend to be long, so this is more sane than it looks.
        int maxWidth = 0;
        android.view.View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (parent == null) {
                parent = new android.widget.FrameLayout(context);
            }
            itemView = adapter.getView(i, itemView, parent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            final int itemWidth = itemView.getMeasuredWidth();
            if (itemWidth >= maxAllowedWidth) {
                return maxAllowedWidth;
            } else
                if (itemWidth > maxWidth) {
                    maxWidth = itemWidth;
                }

        }
        return maxWidth;
    }

    /**
     * Converts the given ListAdapter originating from a menu, to a MenuAdapter, accounting for
     * the possibility of the parameter adapter actually wrapping the MenuAdapter. (That could
     * happen if a header view was added on the menu.)
     *
     * @param adapter
     * 		
     * @return 
     */
    protected static android.support.v7.view.menu.MenuAdapter toMenuAdapter(android.widget.ListAdapter adapter) {
        if (adapter instanceof android.widget.HeaderViewListAdapter) {
            return ((android.support.v7.view.menu.MenuAdapter) (((android.widget.HeaderViewListAdapter) (adapter)).getWrappedAdapter()));
        }
        return ((android.support.v7.view.menu.MenuAdapter) (adapter));
    }

    /**
     * Returns whether icon spacing needs to be preserved for the given menu, based on whether any
     * of its items contains an icon.
     *
     * NOTE: This should only be used for non-overflow-only menus, because this method does not
     * take into account whether the menu items are being shown as part of the popup or or being
     * shown as actions in the action bar.
     *
     * @param menu
     * 		
     * @return Whether to preserve icon spacing.
     */
    protected static boolean shouldPreserveIconSpacing(android.support.v7.view.menu.MenuBuilder menu) {
        boolean preserveIconSpacing = false;
        final int count = menu.size();
        for (int i = 0; i < count; i++) {
            android.view.MenuItem childItem = menu.getItem(i);
            if (childItem.isVisible() && (childItem.getIcon() != null)) {
                preserveIconSpacing = true;
                break;
            }
        }
        return preserveIconSpacing;
    }

    protected boolean closeMenuOnSubMenuOpened() {
        return true;
    }
}

