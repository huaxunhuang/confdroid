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
package android.support.v7.app;


class ToolbarActionBar extends android.support.v7.app.ActionBar {
    android.support.v7.widget.DecorToolbar mDecorToolbar;

    boolean mToolbarMenuPrepared;

    android.view.Window.Callback mWindowCallback;

    private boolean mMenuCallbackSet;

    private boolean mLastMenuVisibility;

    private java.util.ArrayList<android.support.v7.app.ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new java.util.ArrayList<>();

    private android.support.v7.view.menu.ListMenuPresenter mListMenuPresenter;

    private final java.lang.Runnable mMenuInvalidator = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            populateOptionsMenu();
        }
    };

    private final android.support.v7.widget.Toolbar.OnMenuItemClickListener mMenuClicker = new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
        @java.lang.Override
        public boolean onMenuItemClick(android.view.MenuItem item) {
            return mWindowCallback.onMenuItemSelected(android.view.Window.FEATURE_OPTIONS_PANEL, item);
        }
    };

    public ToolbarActionBar(android.support.v7.widget.Toolbar toolbar, java.lang.CharSequence title, android.view.Window.Callback callback) {
        mDecorToolbar = new android.support.v7.widget.ToolbarWidgetWrapper(toolbar, false);
        mWindowCallback = new android.support.v7.app.ToolbarActionBar.ToolbarCallbackWrapper(callback);
        mDecorToolbar.setWindowCallback(mWindowCallback);
        toolbar.setOnMenuItemClickListener(mMenuClicker);
        mDecorToolbar.setWindowTitle(title);
    }

    public android.view.Window.Callback getWrappedWindowCallback() {
        return mWindowCallback;
    }

    @java.lang.Override
    public void setCustomView(android.view.View view) {
        setCustomView(view, new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT));
    }

    @java.lang.Override
    public void setCustomView(android.view.View view, android.support.v7.app.ActionBar.LayoutParams layoutParams) {
        if (view != null) {
            view.setLayoutParams(layoutParams);
        }
        mDecorToolbar.setCustomView(view);
    }

    @java.lang.Override
    public void setCustomView(int resId) {
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mDecorToolbar.getContext());
        setCustomView(inflater.inflate(resId, mDecorToolbar.getViewGroup(), false));
    }

    @java.lang.Override
    public void setIcon(int resId) {
        mDecorToolbar.setIcon(resId);
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable icon) {
        mDecorToolbar.setIcon(icon);
    }

    @java.lang.Override
    public void setLogo(int resId) {
        mDecorToolbar.setLogo(resId);
    }

    @java.lang.Override
    public void setLogo(android.graphics.drawable.Drawable logo) {
        mDecorToolbar.setLogo(logo);
    }

    @java.lang.Override
    public void setStackedBackgroundDrawable(android.graphics.drawable.Drawable d) {
        // This space for rent (do nothing)
    }

    @java.lang.Override
    public void setSplitBackgroundDrawable(android.graphics.drawable.Drawable d) {
        // This space for rent (do nothing)
    }

    @java.lang.Override
    public void setHomeButtonEnabled(boolean enabled) {
        // If the nav button on a Toolbar is present, it's enabled. No-op.
    }

    @java.lang.Override
    public void setElevation(float elevation) {
        android.support.v4.view.ViewCompat.setElevation(mDecorToolbar.getViewGroup(), elevation);
    }

    @java.lang.Override
    public float getElevation() {
        return android.support.v4.view.ViewCompat.getElevation(mDecorToolbar.getViewGroup());
    }

    @java.lang.Override
    public android.content.Context getThemedContext() {
        return mDecorToolbar.getContext();
    }

    @java.lang.Override
    public boolean isTitleTruncated() {
        return super.isTitleTruncated();
    }

    @java.lang.Override
    public void setHomeAsUpIndicator(android.graphics.drawable.Drawable indicator) {
        mDecorToolbar.setNavigationIcon(indicator);
    }

    @java.lang.Override
    public void setHomeAsUpIndicator(int resId) {
        mDecorToolbar.setNavigationIcon(resId);
    }

    @java.lang.Override
    public void setHomeActionContentDescription(java.lang.CharSequence description) {
        mDecorToolbar.setNavigationContentDescription(description);
    }

    @java.lang.Override
    public void setDefaultDisplayHomeAsUpEnabled(boolean enabled) {
        // Do nothing
    }

    @java.lang.Override
    public void setHomeActionContentDescription(int resId) {
        mDecorToolbar.setNavigationContentDescription(resId);
    }

    @java.lang.Override
    public void setShowHideAnimationEnabled(boolean enabled) {
        // This space for rent; no-op.
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration config) {
        super.onConfigurationChanged(config);
    }

    @java.lang.Override
    public void setListNavigationCallbacks(android.widget.SpinnerAdapter adapter, android.support.v7.app.ActionBar.OnNavigationListener callback) {
        mDecorToolbar.setDropdownParams(adapter, new android.support.v7.app.NavItemSelectedListener(callback));
    }

    @java.lang.Override
    public void setSelectedNavigationItem(int position) {
        switch (mDecorToolbar.getNavigationMode()) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_LIST :
                mDecorToolbar.setDropdownSelectedPosition(position);
                break;
            default :
                throw new java.lang.IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
        }
    }

    @java.lang.Override
    public int getSelectedNavigationIndex() {
        return -1;
    }

    @java.lang.Override
    public int getNavigationItemCount() {
        return 0;
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mDecorToolbar.setTitle(title);
    }

    @java.lang.Override
    public void setTitle(int resId) {
        mDecorToolbar.setTitle(resId != 0 ? mDecorToolbar.getContext().getText(resId) : null);
    }

    @java.lang.Override
    public void setWindowTitle(java.lang.CharSequence title) {
        mDecorToolbar.setWindowTitle(title);
    }

    @java.lang.Override
    public boolean requestFocus() {
        final android.view.ViewGroup viewGroup = mDecorToolbar.getViewGroup();
        if ((viewGroup != null) && (!viewGroup.hasFocus())) {
            viewGroup.requestFocus();
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void setSubtitle(java.lang.CharSequence subtitle) {
        mDecorToolbar.setSubtitle(subtitle);
    }

    @java.lang.Override
    public void setSubtitle(int resId) {
        mDecorToolbar.setSubtitle(resId != 0 ? mDecorToolbar.getContext().getText(resId) : null);
    }

    @java.lang.Override
    public void setDisplayOptions(@android.support.v7.app.ActionBar.DisplayOptions
    int options) {
        setDisplayOptions(options, 0xffffffff);
    }

    @java.lang.Override
    public void setDisplayOptions(@android.support.v7.app.ActionBar.DisplayOptions
    int options, @android.support.v7.app.ActionBar.DisplayOptions
    int mask) {
        final int currentOptions = mDecorToolbar.getDisplayOptions();
        mDecorToolbar.setDisplayOptions((options & mask) | (currentOptions & (~mask)));
    }

    @java.lang.Override
    public void setDisplayUseLogoEnabled(boolean useLogo) {
        setDisplayOptions(useLogo ? android.support.v7.app.ActionBar.DISPLAY_USE_LOGO : 0, android.support.v7.app.ActionBar.DISPLAY_USE_LOGO);
    }

    @java.lang.Override
    public void setDisplayShowHomeEnabled(boolean showHome) {
        setDisplayOptions(showHome ? android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME : 0, android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME);
    }

    @java.lang.Override
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        setDisplayOptions(showHomeAsUp ? android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP : 0, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
    }

    @java.lang.Override
    public void setDisplayShowTitleEnabled(boolean showTitle) {
        setDisplayOptions(showTitle ? android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE : 0, android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE);
    }

    @java.lang.Override
    public void setDisplayShowCustomEnabled(boolean showCustom) {
        setDisplayOptions(showCustom ? android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM : 0, android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    @java.lang.Override
    public void setBackgroundDrawable(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable d) {
        mDecorToolbar.setBackgroundDrawable(d);
    }

    @java.lang.Override
    public android.view.View getCustomView() {
        return mDecorToolbar.getCustomView();
    }

    @java.lang.Override
    public java.lang.CharSequence getTitle() {
        return mDecorToolbar.getTitle();
    }

    @java.lang.Override
    public java.lang.CharSequence getSubtitle() {
        return mDecorToolbar.getSubtitle();
    }

    @java.lang.Override
    public int getNavigationMode() {
        return android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD;
    }

    @java.lang.Override
    public void setNavigationMode(@android.support.v7.app.ActionBar.NavigationMode
    int mode) {
        if (mode == android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS) {
            throw new java.lang.IllegalArgumentException("Tabs not supported in this configuration");
        }
        mDecorToolbar.setNavigationMode(mode);
    }

    @java.lang.Override
    public int getDisplayOptions() {
        return mDecorToolbar.getDisplayOptions();
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab newTab() {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, boolean setSelected) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, int position) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, int position, boolean setSelected) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void removeTab(android.support.v7.app.ActionBar.Tab tab) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void removeTabAt(int position) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void removeAllTabs() {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public void selectTab(android.support.v7.app.ActionBar.Tab tab) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab getSelectedTab() {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab getTabAt(int index) {
        throw new java.lang.UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @java.lang.Override
    public int getTabCount() {
        return 0;
    }

    @java.lang.Override
    public int getHeight() {
        return mDecorToolbar.getHeight();
    }

    @java.lang.Override
    public void show() {
        // TODO: Consider a better transition for this.
        // Right now use no automatic transition so that the app can supply one if desired.
        mDecorToolbar.setVisibility(android.view.View.VISIBLE);
    }

    @java.lang.Override
    public void hide() {
        // TODO: Consider a better transition for this.
        // Right now use no automatic transition so that the app can supply one if desired.
        mDecorToolbar.setVisibility(android.view.View.GONE);
    }

    @java.lang.Override
    public boolean isShowing() {
        return mDecorToolbar.getVisibility() == android.view.View.VISIBLE;
    }

    @java.lang.Override
    public boolean openOptionsMenu() {
        return mDecorToolbar.showOverflowMenu();
    }

    @java.lang.Override
    public boolean invalidateOptionsMenu() {
        mDecorToolbar.getViewGroup().removeCallbacks(mMenuInvalidator);
        android.support.v4.view.ViewCompat.postOnAnimation(mDecorToolbar.getViewGroup(), mMenuInvalidator);
        return true;
    }

    @java.lang.Override
    public boolean collapseActionView() {
        if (mDecorToolbar.hasExpandedActionView()) {
            mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }

    void populateOptionsMenu() {
        final android.view.Menu menu = getMenu();
        final android.support.v7.view.menu.MenuBuilder mb = (menu instanceof android.support.v7.view.menu.MenuBuilder) ? ((android.support.v7.view.menu.MenuBuilder) (menu)) : null;
        if (mb != null) {
            mb.stopDispatchingItemsChanged();
        }
        try {
            menu.clear();
            if ((!mWindowCallback.onCreatePanelMenu(android.view.Window.FEATURE_OPTIONS_PANEL, menu)) || (!mWindowCallback.onPreparePanel(android.view.Window.FEATURE_OPTIONS_PANEL, null, menu))) {
                menu.clear();
            }
        } finally {
            if (mb != null) {
                mb.startDispatchingItemsChanged();
            }
        }
    }

    @java.lang.Override
    public boolean onMenuKeyEvent(android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
            openOptionsMenu();
        }
        return true;
    }

    @java.lang.Override
    public boolean onKeyShortcut(int keyCode, android.view.KeyEvent ev) {
        android.view.Menu menu = getMenu();
        if (menu != null) {
            final android.view.KeyCharacterMap kmap = android.view.KeyCharacterMap.load(ev != null ? ev.getDeviceId() : android.view.KeyCharacterMap.VIRTUAL_KEYBOARD);
            menu.setQwertyMode(kmap.getKeyboardType() != android.view.KeyCharacterMap.NUMERIC);
            menu.performShortcut(keyCode, ev, 0);
        }
        // This action bar always returns true for handling keyboard shortcuts.
        // This will block the window from preparing a temporary panel to handle
        // keyboard shortcuts.
        return true;
    }

    @java.lang.Override
    void onDestroy() {
        // Remove any invalidation callbacks
        mDecorToolbar.getViewGroup().removeCallbacks(mMenuInvalidator);
    }

    public void addOnMenuVisibilityListener(android.support.v7.app.ActionBar.OnMenuVisibilityListener listener) {
        mMenuVisibilityListeners.add(listener);
    }

    public void removeOnMenuVisibilityListener(android.support.v7.app.ActionBar.OnMenuVisibilityListener listener) {
        mMenuVisibilityListeners.remove(listener);
    }

    public void dispatchMenuVisibilityChanged(boolean isVisible) {
        if (isVisible == mLastMenuVisibility) {
            return;
        }
        mLastMenuVisibility = isVisible;
        final int count = mMenuVisibilityListeners.size();
        for (int i = 0; i < count; i++) {
            mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(isVisible);
        }
    }

    android.view.View getListMenuView(android.view.Menu menu) {
        ensureListMenuPresenter(menu);
        if ((menu == null) || (mListMenuPresenter == null)) {
            return null;
        }
        if (mListMenuPresenter.getAdapter().getCount() > 0) {
            return ((android.view.View) (mListMenuPresenter.getMenuView(mDecorToolbar.getViewGroup())));
        }
        return null;
    }

    private void ensureListMenuPresenter(android.view.Menu menu) {
        if ((mListMenuPresenter == null) && (menu instanceof android.support.v7.view.menu.MenuBuilder)) {
            android.support.v7.view.menu.MenuBuilder mb = ((android.support.v7.view.menu.MenuBuilder) (menu));
            android.content.Context context = mDecorToolbar.getContext();
            final android.util.TypedValue outValue = new android.util.TypedValue();
            final android.content.res.Resources.Theme widgetTheme = context.getResources().newTheme();
            widgetTheme.setTo(context.getTheme());
            // First apply the actionBarPopupTheme
            widgetTheme.resolveAttribute(R.attr.actionBarPopupTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            // Apply the panelMenuListTheme
            widgetTheme.resolveAttribute(R.attr.panelMenuListTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            } else {
                widgetTheme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            context = new android.view.ContextThemeWrapper(context, 0);
            context.getTheme().setTo(widgetTheme);
            // Finally create the list menu presenter
            mListMenuPresenter = new android.support.v7.view.menu.ListMenuPresenter(context, R.layout.abc_list_menu_item_layout);
            mListMenuPresenter.setCallback(new android.support.v7.app.ToolbarActionBar.PanelMenuPresenterCallback());
            mb.addMenuPresenter(mListMenuPresenter);
        }
    }

    private class ToolbarCallbackWrapper extends android.support.v7.view.WindowCallbackWrapper {
        public ToolbarCallbackWrapper(android.view.Window.Callback wrapped) {
            super(wrapped);
        }

        @java.lang.Override
        public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
            final boolean result = super.onPreparePanel(featureId, view, menu);
            if (result && (!mToolbarMenuPrepared)) {
                mDecorToolbar.setMenuPrepared();
                mToolbarMenuPrepared = true;
            }
            return result;
        }

        @java.lang.Override
        public android.view.View onCreatePanelView(int featureId) {
            switch (featureId) {
                case android.view.Window.FEATURE_OPTIONS_PANEL :
                    final android.view.Menu menu = mDecorToolbar.getMenu();
                    if (onPreparePanel(featureId, null, menu) && onMenuOpened(featureId, menu)) {
                        return getListMenuView(menu);
                    }
                    break;
            }
            return super.onCreatePanelView(featureId);
        }
    }

    private android.view.Menu getMenu() {
        if (!mMenuCallbackSet) {
            mDecorToolbar.setMenuCallbacks(new android.support.v7.app.ToolbarActionBar.ActionMenuPresenterCallback(), new android.support.v7.app.ToolbarActionBar.MenuBuilderCallback());
            mMenuCallbackSet = true;
        }
        return mDecorToolbar.getMenu();
    }

    private final class ActionMenuPresenterCallback implements android.support.v7.view.menu.MenuPresenter.Callback {
        private boolean mClosingActionMenu;

        ActionMenuPresenterCallback() {
        }

        @java.lang.Override
        public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
            if (mWindowCallback != null) {
                mWindowCallback.onMenuOpened(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, subMenu);
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            if (mClosingActionMenu) {
                return;
            }
            mClosingActionMenu = true;
            mDecorToolbar.dismissPopupMenus();
            if (mWindowCallback != null) {
                mWindowCallback.onPanelClosed(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, menu);
            }
            mClosingActionMenu = false;
        }
    }

    private final class PanelMenuPresenterCallback implements android.support.v7.view.menu.MenuPresenter.Callback {
        PanelMenuPresenterCallback() {
        }

        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            if (mWindowCallback != null) {
                mWindowCallback.onPanelClosed(android.view.Window.FEATURE_OPTIONS_PANEL, menu);
            }
        }

        @java.lang.Override
        public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
            if ((subMenu == null) && (mWindowCallback != null)) {
                mWindowCallback.onMenuOpened(android.view.Window.FEATURE_OPTIONS_PANEL, subMenu);
            }
            return true;
        }
    }

    private final class MenuBuilderCallback implements android.support.v7.view.menu.MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        @java.lang.Override
        public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
            return false;
        }

        @java.lang.Override
        public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
            if (mWindowCallback != null) {
                if (mDecorToolbar.isOverflowMenuShowing()) {
                    mWindowCallback.onPanelClosed(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, menu);
                } else
                    if (mWindowCallback.onPreparePanel(android.view.Window.FEATURE_OPTIONS_PANEL, null, menu)) {
                        mWindowCallback.onMenuOpened(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, menu);
                    }

            }
        }
    }
}

