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
 * Helper for menus that appear as Dialogs (context and submenus).
 */
class MenuDialogHelper implements android.content.DialogInterface.OnClickListener , android.content.DialogInterface.OnDismissListener , android.content.DialogInterface.OnKeyListener , android.support.v7.view.menu.MenuPresenter.Callback {
    private android.support.v7.view.menu.MenuBuilder mMenu;

    private android.support.v7.app.AlertDialog mDialog;

    android.support.v7.view.menu.ListMenuPresenter mPresenter;

    private android.support.v7.view.menu.MenuPresenter.Callback mPresenterCallback;

    public MenuDialogHelper(android.support.v7.view.menu.MenuBuilder menu) {
        mMenu = menu;
    }

    /**
     * Shows menu as a dialog.
     *
     * @param windowToken
     * 		Optional token to assign to the window.
     */
    public void show(android.os.IBinder windowToken) {
        // Many references to mMenu, create local reference
        final android.support.v7.view.menu.MenuBuilder menu = mMenu;
        // Get the builder for the dialog
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(menu.getContext());
        mPresenter = new android.support.v7.view.menu.ListMenuPresenter(builder.getContext(), R.layout.abc_list_menu_item_layout);
        mPresenter.setCallback(this);
        mMenu.addMenuPresenter(mPresenter);
        builder.setAdapter(mPresenter.getAdapter(), this);
        // Set the title
        final android.view.View headerView = menu.getHeaderView();
        if (headerView != null) {
            // Menu's client has given a custom header view, use it
            builder.setCustomTitle(headerView);
        } else {
            // Otherwise use the (text) title and icon
            builder.setIcon(menu.getHeaderIcon()).setTitle(menu.getHeaderTitle());
        }
        // Set the key listener
        builder.setOnKeyListener(this);
        // Show the menu
        mDialog = builder.create();
        mDialog.setOnDismissListener(this);
        android.view.WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        if (windowToken != null) {
            lp.token = windowToken;
        }
        lp.flags |= android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        mDialog.show();
    }

    public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_MENU) || (keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) {
                android.view.Window win = mDialog.getWindow();
                if (win != null) {
                    android.view.View decor = win.getDecorView();
                    if (decor != null) {
                        android.view.KeyEvent.DispatcherState ds = decor.getKeyDispatcherState();
                        if (ds != null) {
                            ds.startTracking(event, this);
                            return true;
                        }
                    }
                }
            } else
                if ((event.getAction() == android.view.KeyEvent.ACTION_UP) && (!event.isCanceled())) {
                    android.view.Window win = mDialog.getWindow();
                    if (win != null) {
                        android.view.View decor = win.getDecorView();
                        if (decor != null) {
                            android.view.KeyEvent.DispatcherState ds = decor.getKeyDispatcherState();
                            if ((ds != null) && ds.isTracking(event)) {
                                mMenu.close(true);
                                dialog.dismiss();
                                return true;
                            }
                        }
                    }
                }

        }
        // Menu shortcut matching
        return mMenu.performShortcut(keyCode, event, 0);
    }

    public void setPresenterCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        mPresenterCallback = cb;
    }

    /**
     * Dismisses the menu's dialog.
     *
     * @see Dialog#dismiss()
     */
    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @java.lang.Override
    public void onDismiss(android.content.DialogInterface dialog) {
        mPresenter.onCloseMenu(mMenu, true);
    }

    @java.lang.Override
    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        if (allMenusAreClosing || (menu == mMenu)) {
            dismiss();
        }
        if (mPresenterCallback != null) {
            mPresenterCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    @java.lang.Override
    public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
        if (mPresenterCallback != null) {
            return mPresenterCallback.onOpenSubMenu(subMenu);
        }
        return false;
    }

    public void onClick(android.content.DialogInterface dialog, int which) {
        mMenu.performItemAction(((android.support.v7.view.menu.MenuItemImpl) (mPresenter.getAdapter().getItem(which))), 0);
    }
}

