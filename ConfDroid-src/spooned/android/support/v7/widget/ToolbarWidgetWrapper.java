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
package android.support.v7.widget;


/**
 * Internal class used to interact with the Toolbar widget without
 * exposing interface methods to the public API.
 *
 * <p>ToolbarWidgetWrapper manages the differences between Toolbar and ActionBarView
 * so that either variant acting as a
 * {@link WindowDecorActionBar WindowDecorActionBar} can behave
 * in the same way.</p>
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ToolbarWidgetWrapper implements android.support.v7.widget.DecorToolbar {
    private static final java.lang.String TAG = "ToolbarWidgetWrapper";

    private static final int AFFECTS_LOGO_MASK = android.app.ActionBar.DISPLAY_SHOW_HOME | android.app.ActionBar.DISPLAY_USE_LOGO;

    // Default fade duration for fading in/out tool bar.
    private static final long DEFAULT_FADE_DURATION_MS = 200;

    android.support.v7.widget.Toolbar mToolbar;

    private int mDisplayOpts;

    private android.view.View mTabView;

    private android.widget.Spinner mSpinner;

    private android.view.View mCustomView;

    private android.graphics.drawable.Drawable mIcon;

    private android.graphics.drawable.Drawable mLogo;

    private android.graphics.drawable.Drawable mNavIcon;

    private boolean mTitleSet;

    java.lang.CharSequence mTitle;

    private java.lang.CharSequence mSubtitle;

    private java.lang.CharSequence mHomeDescription;

    android.view.Window.Callback mWindowCallback;

    boolean mMenuPrepared;

    private android.support.v7.widget.ActionMenuPresenter mActionMenuPresenter;

    private int mNavigationMode = android.app.ActionBar.NAVIGATION_MODE_STANDARD;

    private int mDefaultNavigationContentDescription = 0;

    private android.graphics.drawable.Drawable mDefaultNavigationIcon;

    public ToolbarWidgetWrapper(android.support.v7.widget.Toolbar toolbar, boolean style) {
        this(toolbar, style, R.string.abc_action_bar_up_description, R.drawable.abc_ic_ab_back_material);
    }

    public ToolbarWidgetWrapper(android.support.v7.widget.Toolbar toolbar, boolean style, int defaultNavigationContentDescription, int defaultNavigationIcon) {
        mToolbar = toolbar;
        mTitle = toolbar.getTitle();
        mSubtitle = toolbar.getSubtitle();
        mTitleSet = mTitle != null;
        mNavIcon = toolbar.getNavigationIcon();
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(toolbar.getContext(), null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        mDefaultNavigationIcon = a.getDrawable(R.styleable.ActionBar_homeAsUpIndicator);
        if (style) {
            final java.lang.CharSequence title = a.getText(R.styleable.ActionBar_title);
            if (!android.text.TextUtils.isEmpty(title)) {
                setTitle(title);
            }
            final java.lang.CharSequence subtitle = a.getText(R.styleable.ActionBar_subtitle);
            if (!android.text.TextUtils.isEmpty(subtitle)) {
                setSubtitle(subtitle);
            }
            final android.graphics.drawable.Drawable logo = a.getDrawable(R.styleable.ActionBar_logo);
            if (logo != null) {
                setLogo(logo);
            }
            final android.graphics.drawable.Drawable icon = a.getDrawable(R.styleable.ActionBar_icon);
            if (icon != null) {
                setIcon(icon);
            }
            if ((mNavIcon == null) && (mDefaultNavigationIcon != null)) {
                setNavigationIcon(mDefaultNavigationIcon);
            }
            setDisplayOptions(a.getInt(R.styleable.ActionBar_displayOptions, 0));
            final int customNavId = a.getResourceId(R.styleable.ActionBar_customNavigationLayout, 0);
            if (customNavId != 0) {
                setCustomView(android.view.LayoutInflater.from(mToolbar.getContext()).inflate(customNavId, mToolbar, false));
                setDisplayOptions(mDisplayOpts | android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            }
            final int height = a.getLayoutDimension(R.styleable.ActionBar_height, 0);
            if (height > 0) {
                final android.view.ViewGroup.LayoutParams lp = mToolbar.getLayoutParams();
                lp.height = height;
                mToolbar.setLayoutParams(lp);
            }
            final int contentInsetStart = a.getDimensionPixelOffset(R.styleable.ActionBar_contentInsetStart, -1);
            final int contentInsetEnd = a.getDimensionPixelOffset(R.styleable.ActionBar_contentInsetEnd, -1);
            if ((contentInsetStart >= 0) || (contentInsetEnd >= 0)) {
                mToolbar.setContentInsetsRelative(java.lang.Math.max(contentInsetStart, 0), java.lang.Math.max(contentInsetEnd, 0));
            }
            final int titleTextStyle = a.getResourceId(R.styleable.ActionBar_titleTextStyle, 0);
            if (titleTextStyle != 0) {
                mToolbar.setTitleTextAppearance(mToolbar.getContext(), titleTextStyle);
            }
            final int subtitleTextStyle = a.getResourceId(R.styleable.ActionBar_subtitleTextStyle, 0);
            if (subtitleTextStyle != 0) {
                mToolbar.setSubtitleTextAppearance(mToolbar.getContext(), subtitleTextStyle);
            }
            final int popupTheme = a.getResourceId(R.styleable.ActionBar_popupTheme, 0);
            if (popupTheme != 0) {
                mToolbar.setPopupTheme(popupTheme);
            }
        } else {
            mDisplayOpts = detectDisplayOptions();
        }
        a.recycle();
        setDefaultNavigationContentDescription(defaultNavigationContentDescription);
        mHomeDescription = mToolbar.getNavigationContentDescription();
        mToolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            final android.support.v7.view.menu.ActionMenuItem mNavItem = new android.support.v7.view.menu.ActionMenuItem(mToolbar.getContext(), 0, android.R.id.home, 0, 0, mTitle);

            @java.lang.Override
            public void onClick(android.view.View v) {
                if ((mWindowCallback != null) && mMenuPrepared) {
                    mWindowCallback.onMenuItemSelected(android.view.Window.FEATURE_OPTIONS_PANEL, mNavItem);
                }
            }
        });
    }

    @java.lang.Override
    public void setDefaultNavigationContentDescription(int defaultNavigationContentDescription) {
        if (defaultNavigationContentDescription == mDefaultNavigationContentDescription) {
            return;
        }
        mDefaultNavigationContentDescription = defaultNavigationContentDescription;
        if (android.text.TextUtils.isEmpty(mToolbar.getNavigationContentDescription())) {
            setNavigationContentDescription(mDefaultNavigationContentDescription);
        }
    }

    private int detectDisplayOptions() {
        int opts = (android.app.ActionBar.DISPLAY_SHOW_TITLE | android.app.ActionBar.DISPLAY_SHOW_HOME) | android.app.ActionBar.DISPLAY_USE_LOGO;
        if (mToolbar.getNavigationIcon() != null) {
            opts |= android.app.ActionBar.DISPLAY_HOME_AS_UP;
            mDefaultNavigationIcon = mToolbar.getNavigationIcon();
        }
        return opts;
    }

    @java.lang.Override
    public android.view.ViewGroup getViewGroup() {
        return mToolbar;
    }

    @java.lang.Override
    public android.content.Context getContext() {
        return mToolbar.getContext();
    }

    @java.lang.Override
    public boolean hasExpandedActionView() {
        return mToolbar.hasExpandedActionView();
    }

    @java.lang.Override
    public void collapseActionView() {
        mToolbar.collapseActionView();
    }

    @java.lang.Override
    public void setWindowCallback(android.view.Window.Callback cb) {
        mWindowCallback = cb;
    }

    @java.lang.Override
    public void setWindowTitle(java.lang.CharSequence title) {
        // "Real" title always trumps window title.
        if (!mTitleSet) {
            setTitleInt(title);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getTitle() {
        return mToolbar.getTitle();
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mTitleSet = true;
        setTitleInt(title);
    }

    private void setTitleInt(java.lang.CharSequence title) {
        mTitle = title;
        if ((mDisplayOpts & android.app.ActionBar.DISPLAY_SHOW_TITLE) != 0) {
            mToolbar.setTitle(title);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getSubtitle() {
        return mToolbar.getSubtitle();
    }

    @java.lang.Override
    public void setSubtitle(java.lang.CharSequence subtitle) {
        mSubtitle = subtitle;
        if ((mDisplayOpts & android.app.ActionBar.DISPLAY_SHOW_TITLE) != 0) {
            mToolbar.setSubtitle(subtitle);
        }
    }

    @java.lang.Override
    public void initProgress() {
        android.util.Log.i(android.support.v7.widget.ToolbarWidgetWrapper.TAG, "Progress display unsupported");
    }

    @java.lang.Override
    public void initIndeterminateProgress() {
        android.util.Log.i(android.support.v7.widget.ToolbarWidgetWrapper.TAG, "Progress display unsupported");
    }

    @java.lang.Override
    public boolean hasIcon() {
        return mIcon != null;
    }

    @java.lang.Override
    public boolean hasLogo() {
        return mLogo != null;
    }

    @java.lang.Override
    public void setIcon(int resId) {
        setIcon(resId != 0 ? android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable d) {
        mIcon = d;
        updateToolbarLogo();
    }

    @java.lang.Override
    public void setLogo(int resId) {
        setLogo(resId != 0 ? android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    @java.lang.Override
    public void setLogo(android.graphics.drawable.Drawable d) {
        mLogo = d;
        updateToolbarLogo();
    }

    private void updateToolbarLogo() {
        android.graphics.drawable.Drawable logo = null;
        if ((mDisplayOpts & android.app.ActionBar.DISPLAY_SHOW_HOME) != 0) {
            if ((mDisplayOpts & android.app.ActionBar.DISPLAY_USE_LOGO) != 0) {
                logo = (mLogo != null) ? mLogo : mIcon;
            } else {
                logo = mIcon;
            }
        }
        mToolbar.setLogo(logo);
    }

    @java.lang.Override
    public boolean canShowOverflowMenu() {
        return mToolbar.canShowOverflowMenu();
    }

    @java.lang.Override
    public boolean isOverflowMenuShowing() {
        return mToolbar.isOverflowMenuShowing();
    }

    @java.lang.Override
    public boolean isOverflowMenuShowPending() {
        return mToolbar.isOverflowMenuShowPending();
    }

    @java.lang.Override
    public boolean showOverflowMenu() {
        return mToolbar.showOverflowMenu();
    }

    @java.lang.Override
    public boolean hideOverflowMenu() {
        return mToolbar.hideOverflowMenu();
    }

    @java.lang.Override
    public void setMenuPrepared() {
        mMenuPrepared = true;
    }

    @java.lang.Override
    public void setMenu(android.view.Menu menu, android.support.v7.view.menu.MenuPresenter.Callback cb) {
        if (mActionMenuPresenter == null) {
            mActionMenuPresenter = new android.support.v7.widget.ActionMenuPresenter(mToolbar.getContext());
            mActionMenuPresenter.setId(R.id.action_menu_presenter);
        }
        mActionMenuPresenter.setCallback(cb);
        mToolbar.setMenu(((android.support.v7.view.menu.MenuBuilder) (menu)), mActionMenuPresenter);
    }

    @java.lang.Override
    public void dismissPopupMenus() {
        mToolbar.dismissPopupMenus();
    }

    @java.lang.Override
    public int getDisplayOptions() {
        return mDisplayOpts;
    }

    @java.lang.Override
    public void setDisplayOptions(int newOpts) {
        final int oldOpts = mDisplayOpts;
        final int changed = oldOpts ^ newOpts;
        mDisplayOpts = newOpts;
        if (changed != 0) {
            if ((changed & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
                if ((newOpts & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
                    updateHomeAccessibility();
                }
                updateNavigationIcon();
            }
            if ((changed & android.support.v7.widget.ToolbarWidgetWrapper.AFFECTS_LOGO_MASK) != 0) {
                updateToolbarLogo();
            }
            if ((changed & android.app.ActionBar.DISPLAY_SHOW_TITLE) != 0) {
                if ((newOpts & android.app.ActionBar.DISPLAY_SHOW_TITLE) != 0) {
                    mToolbar.setTitle(mTitle);
                    mToolbar.setSubtitle(mSubtitle);
                } else {
                    mToolbar.setTitle(null);
                    mToolbar.setSubtitle(null);
                }
            }
            if (((changed & android.app.ActionBar.DISPLAY_SHOW_CUSTOM) != 0) && (mCustomView != null)) {
                if ((newOpts & android.app.ActionBar.DISPLAY_SHOW_CUSTOM) != 0) {
                    mToolbar.addView(mCustomView);
                } else {
                    mToolbar.removeView(mCustomView);
                }
            }
        }
    }

    @java.lang.Override
    public void setEmbeddedTabView(android.support.v7.widget.ScrollingTabContainerView tabView) {
        if ((mTabView != null) && (mTabView.getParent() == mToolbar)) {
            mToolbar.removeView(mTabView);
        }
        mTabView = tabView;
        if ((tabView != null) && (mNavigationMode == android.app.ActionBar.NAVIGATION_MODE_TABS)) {
            mToolbar.addView(mTabView, 0);
            android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mTabView.getLayoutParams()));
            lp.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.gravity = android.view.Gravity.START | android.view.Gravity.BOTTOM;
            tabView.setAllowCollapse(true);
        }
    }

    @java.lang.Override
    public boolean hasEmbeddedTabs() {
        return mTabView != null;
    }

    @java.lang.Override
    public boolean isTitleTruncated() {
        return mToolbar.isTitleTruncated();
    }

    @java.lang.Override
    public void setCollapsible(boolean collapsible) {
        mToolbar.setCollapsible(collapsible);
    }

    @java.lang.Override
    public void setHomeButtonEnabled(boolean enable) {
        // Ignore
    }

    @java.lang.Override
    public int getNavigationMode() {
        return mNavigationMode;
    }

    @java.lang.Override
    public void setNavigationMode(int mode) {
        final int oldMode = mNavigationMode;
        if (mode != oldMode) {
            switch (oldMode) {
                case android.app.ActionBar.NAVIGATION_MODE_LIST :
                    if ((mSpinner != null) && (mSpinner.getParent() == mToolbar)) {
                        mToolbar.removeView(mSpinner);
                    }
                    break;
                case android.app.ActionBar.NAVIGATION_MODE_TABS :
                    if ((mTabView != null) && (mTabView.getParent() == mToolbar)) {
                        mToolbar.removeView(mTabView);
                    }
                    break;
            }
            mNavigationMode = mode;
            switch (mode) {
                case android.app.ActionBar.NAVIGATION_MODE_STANDARD :
                    break;
                case android.app.ActionBar.NAVIGATION_MODE_LIST :
                    ensureSpinner();
                    mToolbar.addView(mSpinner, 0);
                    break;
                case android.app.ActionBar.NAVIGATION_MODE_TABS :
                    if (mTabView != null) {
                        mToolbar.addView(mTabView, 0);
                        android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mTabView.getLayoutParams()));
                        lp.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                        lp.gravity = android.view.Gravity.START | android.view.Gravity.BOTTOM;
                    }
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Invalid navigation mode " + mode);
            }
        }
    }

    private void ensureSpinner() {
        if (mSpinner == null) {
            mSpinner = new android.support.v7.widget.AppCompatSpinner(getContext(), null, R.attr.actionDropDownStyle);
            android.support.v7.widget.Toolbar.LayoutParams lp = new android.support.v7.widget.Toolbar.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.Gravity.START | android.view.Gravity.CENTER_VERTICAL);
            mSpinner.setLayoutParams(lp);
        }
    }

    @java.lang.Override
    public void setDropdownParams(android.widget.SpinnerAdapter adapter, android.widget.AdapterView.OnItemSelectedListener listener) {
        ensureSpinner();
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(listener);
    }

    @java.lang.Override
    public void setDropdownSelectedPosition(int position) {
        if (mSpinner == null) {
            throw new java.lang.IllegalStateException("Can't set dropdown selected position without an adapter");
        }
        mSpinner.setSelection(position);
    }

    @java.lang.Override
    public int getDropdownSelectedPosition() {
        return mSpinner != null ? mSpinner.getSelectedItemPosition() : 0;
    }

    @java.lang.Override
    public int getDropdownItemCount() {
        return mSpinner != null ? mSpinner.getCount() : 0;
    }

    @java.lang.Override
    public void setCustomView(android.view.View view) {
        if ((mCustomView != null) && ((mDisplayOpts & android.app.ActionBar.DISPLAY_SHOW_CUSTOM) != 0)) {
            mToolbar.removeView(mCustomView);
        }
        mCustomView = view;
        if ((view != null) && ((mDisplayOpts & android.app.ActionBar.DISPLAY_SHOW_CUSTOM) != 0)) {
            mToolbar.addView(mCustomView);
        }
    }

    @java.lang.Override
    public android.view.View getCustomView() {
        return mCustomView;
    }

    @java.lang.Override
    public void animateToVisibility(int visibility) {
        android.support.v4.view.ViewPropertyAnimatorCompat anim = setupAnimatorToVisibility(visibility, android.support.v7.widget.ToolbarWidgetWrapper.DEFAULT_FADE_DURATION_MS);
        if (anim != null) {
            anim.start();
        }
    }

    @java.lang.Override
    public android.support.v4.view.ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int visibility, final long duration) {
        return android.support.v4.view.ViewCompat.animate(mToolbar).alpha(visibility == android.view.View.VISIBLE ? 1.0F : 0.0F).setDuration(duration).setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
            private boolean mCanceled = false;

            @java.lang.Override
            public void onAnimationStart(android.view.View view) {
                mToolbar.setVisibility(android.view.View.VISIBLE);
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.View view) {
                if (!mCanceled) {
                    mToolbar.setVisibility(visibility);
                }
            }

            @java.lang.Override
            public void onAnimationCancel(android.view.View view) {
                mCanceled = true;
            }
        });
    }

    @java.lang.Override
    public void setNavigationIcon(android.graphics.drawable.Drawable icon) {
        mNavIcon = icon;
        updateNavigationIcon();
    }

    @java.lang.Override
    public void setNavigationIcon(int resId) {
        setNavigationIcon(resId != 0 ? android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId) : null);
    }

    @java.lang.Override
    public void setDefaultNavigationIcon(android.graphics.drawable.Drawable defaultNavigationIcon) {
        if (mDefaultNavigationIcon != defaultNavigationIcon) {
            mDefaultNavigationIcon = defaultNavigationIcon;
            updateNavigationIcon();
        }
    }

    private void updateNavigationIcon() {
        if ((mDisplayOpts & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
            mToolbar.setNavigationIcon(mNavIcon != null ? mNavIcon : mDefaultNavigationIcon);
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    @java.lang.Override
    public void setNavigationContentDescription(java.lang.CharSequence description) {
        mHomeDescription = description;
        updateHomeAccessibility();
    }

    @java.lang.Override
    public void setNavigationContentDescription(int resId) {
        setNavigationContentDescription(resId == 0 ? null : getContext().getString(resId));
    }

    private void updateHomeAccessibility() {
        if ((mDisplayOpts & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
            if (android.text.TextUtils.isEmpty(mHomeDescription)) {
                mToolbar.setNavigationContentDescription(mDefaultNavigationContentDescription);
            } else {
                mToolbar.setNavigationContentDescription(mHomeDescription);
            }
        }
    }

    @java.lang.Override
    public void saveHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates) {
        mToolbar.saveHierarchyState(toolbarStates);
    }

    @java.lang.Override
    public void restoreHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates) {
        mToolbar.restoreHierarchyState(toolbarStates);
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable d) {
        android.support.v4.view.ViewCompat.setBackground(mToolbar, d);
    }

    @java.lang.Override
    public int getHeight() {
        return mToolbar.getHeight();
    }

    @java.lang.Override
    public void setVisibility(int visible) {
        mToolbar.setVisibility(visible);
    }

    @java.lang.Override
    public int getVisibility() {
        return mToolbar.getVisibility();
    }

    @java.lang.Override
    public void setMenuCallbacks(android.support.v7.view.menu.MenuPresenter.Callback actionMenuPresenterCallback, android.support.v7.view.menu.MenuBuilder.Callback menuBuilderCallback) {
        mToolbar.setMenuCallbacks(actionMenuPresenterCallback, menuBuilderCallback);
    }

    @java.lang.Override
    public android.view.Menu getMenu() {
        return mToolbar.getMenu();
    }
}

