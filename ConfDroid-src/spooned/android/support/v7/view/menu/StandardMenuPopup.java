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
package android.support.v7.view.menu;


/**
 * A standard menu popup in which when a submenu is opened, it replaces its parent menu in the
 * viewport.
 */
final class StandardMenuPopup extends android.support.v7.view.menu.MenuPopup implements android.support.v7.view.menu.MenuPresenter , android.view.View.OnKeyListener , android.widget.AdapterView.OnItemClickListener , android.widget.PopupWindow.OnDismissListener {
    private final android.content.Context mContext;

    private final android.support.v7.view.menu.MenuBuilder mMenu;

    private final android.support.v7.view.menu.MenuAdapter mAdapter;

    private final boolean mOverflowOnly;

    private final int mPopupMaxWidth;

    private final int mPopupStyleAttr;

    private final int mPopupStyleRes;

    // The popup window is final in order to couple its lifecycle to the lifecycle of the
    // StandardMenuPopup.
    final android.support.v7.widget.MenuPopupWindow mPopup;

    private final android.view.ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
        @java.lang.Override
        public void onGlobalLayout() {
            // Only move the popup if it's showing and non-modal. We don't want
            // to be moving around the only interactive window, since there's a
            // good chance the user is interacting with it.
            if (isShowing() && (!mPopup.isModal())) {
                final android.view.View anchor = mShownAnchorView;
                if ((anchor == null) || (!anchor.isShown())) {
                    dismiss();
                } else {
                    // Recompute window size and position
                    mPopup.show();
                }
            }
        }
    };

    private android.widget.PopupWindow.OnDismissListener mOnDismissListener;

    private android.view.View mAnchorView;

    android.view.View mShownAnchorView;

    private android.support.v7.view.menu.MenuPresenter.Callback mPresenterCallback;

    private android.view.ViewTreeObserver mTreeObserver;

    /**
     * Whether the popup has been dismissed. Once dismissed, it cannot be opened again.
     */
    private boolean mWasDismissed;

    /**
     * Whether the cached content width value is valid.
     */
    private boolean mHasContentWidth;

    /**
     * Cached content width.
     */
    private int mContentWidth;

    private int mDropDownGravity = android.view.Gravity.NO_GRAVITY;

    private boolean mShowTitle;

    public StandardMenuPopup(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu, android.view.View anchorView, int popupStyleAttr, int popupStyleRes, boolean overflowOnly) {
        mContext = context;
        mMenu = menu;
        mOverflowOnly = overflowOnly;
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context);
        mAdapter = new android.support.v7.view.menu.MenuAdapter(menu, inflater, mOverflowOnly);
        mPopupStyleAttr = popupStyleAttr;
        mPopupStyleRes = popupStyleRes;
        final android.content.res.Resources res = context.getResources();
        mPopupMaxWidth = java.lang.Math.max(res.getDisplayMetrics().widthPixels / 2, res.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        mAnchorView = anchorView;
        mPopup = new android.support.v7.widget.MenuPopupWindow(mContext, null, mPopupStyleAttr, mPopupStyleRes);
        // Present the menu using our context, not the menu builder's context.
        menu.addMenuPresenter(this, context);
    }

    @java.lang.Override
    public void setForceShowIcon(boolean forceShow) {
        mAdapter.setForceShowIcon(forceShow);
    }

    @java.lang.Override
    public void setGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    private boolean tryShow() {
        if (isShowing()) {
            return true;
        }
        if (mWasDismissed || (mAnchorView == null)) {
            return false;
        }
        mShownAnchorView = mAnchorView;
        mPopup.setOnDismissListener(this);
        mPopup.setOnItemClickListener(this);
        mPopup.setModal(true);
        final android.view.View anchor = mShownAnchorView;
        final boolean addGlobalListener = mTreeObserver == null;
        mTreeObserver = anchor.getViewTreeObserver();// Refresh to latest

        if (addGlobalListener) {
            mTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        mPopup.setAnchorView(anchor);
        mPopup.setDropDownGravity(mDropDownGravity);
        if (!mHasContentWidth) {
            mContentWidth = android.support.v7.view.menu.MenuPopup.measureIndividualMenuWidth(mAdapter, null, mContext, mPopupMaxWidth);
            mHasContentWidth = true;
        }
        mPopup.setContentWidth(mContentWidth);
        mPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopup.setEpicenterBounds(getEpicenterBounds());
        mPopup.show();
        final android.widget.ListView listView = mPopup.getListView();
        listView.setOnKeyListener(this);
        if (mShowTitle && (mMenu.getHeaderTitle() != null)) {
            android.widget.FrameLayout titleItemView = ((android.widget.FrameLayout) (android.view.LayoutInflater.from(mContext).inflate(R.layout.abc_popup_menu_header_item_layout, listView, false)));
            android.widget.TextView titleView = ((android.widget.TextView) (titleItemView.findViewById(android.R.id.title)));
            if (titleView != null) {
                titleView.setText(mMenu.getHeaderTitle());
            }
            titleItemView.setEnabled(false);
            listView.addHeaderView(titleItemView, null, false);
        }
        // Since addHeaderView() needs to be called before setAdapter() pre-v14, we have to set the
        // adapter as late as possible, and then call show again to update
        mPopup.setAdapter(mAdapter);
        mPopup.show();
        return true;
    }

    @java.lang.Override
    public void show() {
        if (!tryShow()) {
            throw new java.lang.IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @java.lang.Override
    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

    @java.lang.Override
    public void addMenu(android.support.v7.view.menu.MenuBuilder menu) {
        // No-op: standard implementation has only one menu which is set in the constructor.
    }

    @java.lang.Override
    public boolean isShowing() {
        return (!mWasDismissed) && mPopup.isShowing();
    }

    @java.lang.Override
    public void onDismiss() {
        mWasDismissed = true;
        mMenu.close();
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive())
                mTreeObserver = mShownAnchorView.getViewTreeObserver();

            mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
            mTreeObserver = null;
        }
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @java.lang.Override
    public void updateMenuView(boolean cleared) {
        mHasContentWidth = false;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @java.lang.Override
    public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mPresenterCallback = cb;
    }

    @java.lang.Override
    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        if (subMenu.hasVisibleItems()) {
            final android.support.v7.view.menu.MenuPopupHelper subPopup = new android.support.v7.view.menu.MenuPopupHelper(mContext, subMenu, mShownAnchorView, mOverflowOnly, mPopupStyleAttr, mPopupStyleRes);
            subPopup.setPresenterCallback(mPresenterCallback);
            subPopup.setForceShowIcon(android.support.v7.view.menu.MenuPopup.shouldPreserveIconSpacing(subMenu));
            // Pass responsibility for handling onDismiss to the submenu.
            subPopup.setOnDismissListener(mOnDismissListener);
            mOnDismissListener = null;
            // Close this menu popup to make room for the submenu popup.
            /* closeAllMenus */
            mMenu.close(false);
            // Show the new sub-menu popup at the same location as this popup.
            final int horizontalOffset = mPopup.getHorizontalOffset();
            final int verticalOffset = mPopup.getVerticalOffset();
            if (subPopup.tryShow(horizontalOffset, verticalOffset)) {
                if (mPresenterCallback != null) {
                    mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        // Only care about the (sub)menu we're presenting.
        if (menu != mMenu)
            return;

        dismiss();
        if (mPresenterCallback != null) {
            mPresenterCallback.onCloseMenu(menu, allMenusAreClosing);
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
    public void setAnchorView(android.view.View anchor) {
        mAnchorView = anchor;
    }

    @java.lang.Override
    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        if ((event.getAction() == android.view.KeyEvent.ACTION_UP) && (keyCode == android.view.KeyEvent.KEYCODE_MENU)) {
            dismiss();
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void setOnDismissListener(android.widget.PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    @java.lang.Override
    public android.widget.ListView getListView() {
        return mPopup.getListView();
    }

    @java.lang.Override
    public void setHorizontalOffset(int x) {
        mPopup.setHorizontalOffset(x);
    }

    @java.lang.Override
    public void setVerticalOffset(int y) {
        mPopup.setVerticalOffset(y);
    }

    @java.lang.Override
    public void setShowTitle(boolean showTitle) {
        mShowTitle = showTitle;
    }
}

