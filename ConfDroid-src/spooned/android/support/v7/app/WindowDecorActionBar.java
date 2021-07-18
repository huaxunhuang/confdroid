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


/**
 * WindowDecorActionBar is the ActionBar implementation used
 * by devices of all screen sizes as part of the window decor layout.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class WindowDecorActionBar extends android.support.v7.app.ActionBar implements android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback {
    private static final java.lang.String TAG = "WindowDecorActionBar";

    private static final android.view.animation.Interpolator sHideInterpolator = new android.view.animation.AccelerateInterpolator();

    private static final android.view.animation.Interpolator sShowInterpolator = new android.view.animation.DecelerateInterpolator();

    /**
     * Only allow show/hide animations on ICS+, as that is what ViewPropertyAnimatorCompat supports
     */
    private static final boolean ALLOW_SHOW_HIDE_ANIMATIONS = android.os.Build.VERSION.SDK_INT >= 14;

    android.content.Context mContext;

    private android.content.Context mThemedContext;

    private android.app.Activity mActivity;

    private android.app.Dialog mDialog;

    android.support.v7.widget.ActionBarOverlayLayout mOverlayLayout;

    android.support.v7.widget.ActionBarContainer mContainerView;

    android.support.v7.widget.DecorToolbar mDecorToolbar;

    android.support.v7.widget.ActionBarContextView mContextView;

    android.view.View mContentView;

    android.support.v7.widget.ScrollingTabContainerView mTabScrollView;

    private java.util.ArrayList<android.support.v7.app.WindowDecorActionBar.TabImpl> mTabs = new java.util.ArrayList<android.support.v7.app.WindowDecorActionBar.TabImpl>();

    private android.support.v7.app.WindowDecorActionBar.TabImpl mSelectedTab;

    private int mSavedTabPosition = android.support.v7.app.WindowDecorActionBar.INVALID_POSITION;

    private boolean mDisplayHomeAsUpSet;

    android.support.v7.app.WindowDecorActionBar.ActionModeImpl mActionMode;

    android.support.v7.view.ActionMode mDeferredDestroyActionMode;

    android.support.v7.view.ActionMode.Callback mDeferredModeDestroyCallback;

    private boolean mLastMenuVisibility;

    private java.util.ArrayList<android.support.v7.app.ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new java.util.ArrayList<android.support.v7.app.ActionBar.OnMenuVisibilityListener>();

    private static final int INVALID_POSITION = -1;

    // The fade duration for toolbar and action bar when entering/exiting action mode.
    private static final long FADE_OUT_DURATION_MS = 100;

    private static final long FADE_IN_DURATION_MS = 200;

    private boolean mHasEmbeddedTabs;

    private int mCurWindowVisibility = android.view.View.VISIBLE;

    boolean mContentAnimations = true;

    boolean mHiddenByApp;

    boolean mHiddenBySystem;

    private boolean mShowingForMode;

    private boolean mNowShowing = true;

    android.support.v7.view.ViewPropertyAnimatorCompatSet mCurrentShowAnim;

    private boolean mShowHideAnimationEnabled;

    boolean mHideOnContentScroll;

    final android.support.v4.view.ViewPropertyAnimatorListener mHideListener = new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            if (mContentAnimations && (mContentView != null)) {
                android.support.v4.view.ViewCompat.setTranslationY(mContentView, 0.0F);
                android.support.v4.view.ViewCompat.setTranslationY(mContainerView, 0.0F);
            }
            mContainerView.setVisibility(android.view.View.GONE);
            mContainerView.setTransitioning(false);
            mCurrentShowAnim = null;
            completeDeferredDestroyActionMode();
            if (mOverlayLayout != null) {
                android.support.v4.view.ViewCompat.requestApplyInsets(mOverlayLayout);
            }
        }
    };

    final android.support.v4.view.ViewPropertyAnimatorListener mShowListener = new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            mCurrentShowAnim = null;
            mContainerView.requestLayout();
        }
    };

    final android.support.v4.view.ViewPropertyAnimatorUpdateListener mUpdateListener = new android.support.v4.view.ViewPropertyAnimatorUpdateListener() {
        @java.lang.Override
        public void onAnimationUpdate(android.view.View view) {
            final android.view.ViewParent parent = mContainerView.getParent();
            ((android.view.View) (parent)).invalidate();
        }
    };

    public WindowDecorActionBar(android.app.Activity activity, boolean overlayMode) {
        mActivity = activity;
        android.view.Window window = activity.getWindow();
        android.view.View decor = window.getDecorView();
        init(decor);
        if (!overlayMode) {
            mContentView = decor.findViewById(android.R.id.content);
        }
    }

    public WindowDecorActionBar(android.app.Dialog dialog) {
        mDialog = dialog;
        init(dialog.getWindow().getDecorView());
    }

    /**
     * Only for edit mode.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public WindowDecorActionBar(android.view.View layout) {
        assert layout.isInEditMode();
        init(layout);
    }

    private void init(android.view.View decor) {
        mOverlayLayout = ((android.support.v7.widget.ActionBarOverlayLayout) (decor.findViewById(R.id.decor_content_parent)));
        if (mOverlayLayout != null) {
            mOverlayLayout.setActionBarVisibilityCallback(this);
        }
        mDecorToolbar = getDecorToolbar(decor.findViewById(R.id.action_bar));
        mContextView = ((android.support.v7.widget.ActionBarContextView) (decor.findViewById(R.id.action_context_bar)));
        mContainerView = ((android.support.v7.widget.ActionBarContainer) (decor.findViewById(R.id.action_bar_container)));
        if (((mDecorToolbar == null) || (mContextView == null)) || (mContainerView == null)) {
            throw new java.lang.IllegalStateException((getClass().getSimpleName() + " can only be used ") + "with a compatible window decor layout");
        }
        mContext = mDecorToolbar.getContext();
        // This was initially read from the action bar style
        final int current = mDecorToolbar.getDisplayOptions();
        final boolean homeAsUp = (current & android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP) != 0;
        if (homeAsUp) {
            mDisplayHomeAsUpSet = true;
        }
        android.support.v7.view.ActionBarPolicy abp = android.support.v7.view.ActionBarPolicy.get(mContext);
        setHomeButtonEnabled(abp.enableHomeButtonByDefault() || homeAsUp);
        setHasEmbeddedTabs(abp.hasEmbeddedTabs());
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        if (a.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
            setHideOnContentScrollEnabled(true);
        }
        final int elevation = a.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0);
        if (elevation != 0) {
            setElevation(elevation);
        }
        a.recycle();
    }

    private android.support.v7.widget.DecorToolbar getDecorToolbar(android.view.View view) {
        if (view instanceof android.support.v7.widget.DecorToolbar) {
            return ((android.support.v7.widget.DecorToolbar) (view));
        } else
            if (view instanceof android.support.v7.widget.Toolbar) {
                return ((android.support.v7.widget.Toolbar) (view)).getWrapper();
            } else {
                throw new java.lang.IllegalStateException(("Can't make a decor toolbar out of " + view) != null ? view.getClass().getSimpleName() : "null");
            }

    }

    @java.lang.Override
    public void setElevation(float elevation) {
        android.support.v4.view.ViewCompat.setElevation(mContainerView, elevation);
    }

    @java.lang.Override
    public float getElevation() {
        return android.support.v4.view.ViewCompat.getElevation(mContainerView);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        setHasEmbeddedTabs(android.support.v7.view.ActionBarPolicy.get(mContext).hasEmbeddedTabs());
    }

    private void setHasEmbeddedTabs(boolean hasEmbeddedTabs) {
        mHasEmbeddedTabs = hasEmbeddedTabs;
        // Switch tab layout configuration if needed
        if (!mHasEmbeddedTabs) {
            mDecorToolbar.setEmbeddedTabView(null);
            mContainerView.setTabContainer(mTabScrollView);
        } else {
            mContainerView.setTabContainer(null);
            mDecorToolbar.setEmbeddedTabView(mTabScrollView);
        }
        final boolean isInTabMode = getNavigationMode() == android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS;
        if (mTabScrollView != null) {
            if (isInTabMode) {
                mTabScrollView.setVisibility(android.view.View.VISIBLE);
                if (mOverlayLayout != null) {
                    android.support.v4.view.ViewCompat.requestApplyInsets(mOverlayLayout);
                }
            } else {
                mTabScrollView.setVisibility(android.view.View.GONE);
            }
        }
        mDecorToolbar.setCollapsible((!mHasEmbeddedTabs) && isInTabMode);
        mOverlayLayout.setHasNonEmbeddedTabs((!mHasEmbeddedTabs) && isInTabMode);
    }

    private void ensureTabsExist() {
        if (mTabScrollView != null) {
            return;
        }
        android.support.v7.widget.ScrollingTabContainerView tabScroller = new android.support.v7.widget.ScrollingTabContainerView(mContext);
        if (mHasEmbeddedTabs) {
            tabScroller.setVisibility(android.view.View.VISIBLE);
            mDecorToolbar.setEmbeddedTabView(tabScroller);
        } else {
            if (getNavigationMode() == android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS) {
                tabScroller.setVisibility(android.view.View.VISIBLE);
                if (mOverlayLayout != null) {
                    android.support.v4.view.ViewCompat.requestApplyInsets(mOverlayLayout);
                }
            } else {
                tabScroller.setVisibility(android.view.View.GONE);
            }
            mContainerView.setTabContainer(tabScroller);
        }
        mTabScrollView = tabScroller;
    }

    void completeDeferredDestroyActionMode() {
        if (mDeferredModeDestroyCallback != null) {
            mDeferredModeDestroyCallback.onDestroyActionMode(mDeferredDestroyActionMode);
            mDeferredDestroyActionMode = null;
            mDeferredModeDestroyCallback = null;
        }
    }

    public void onWindowVisibilityChanged(int visibility) {
        mCurWindowVisibility = visibility;
    }

    /**
     * Enables or disables animation between show/hide states.
     * If animation is disabled using this method, animations in progress
     * will be finished.
     *
     * @param enabled
     * 		true to animate, false to not animate.
     */
    public void setShowHideAnimationEnabled(boolean enabled) {
        mShowHideAnimationEnabled = enabled;
        if ((!enabled) && (mCurrentShowAnim != null)) {
            mCurrentShowAnim.cancel();
        }
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

    @java.lang.Override
    public void setCustomView(int resId) {
        setCustomView(android.view.LayoutInflater.from(getThemedContext()).inflate(resId, mDecorToolbar.getViewGroup(), false));
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
    public void setHomeButtonEnabled(boolean enable) {
        mDecorToolbar.setHomeButtonEnabled(enable);
    }

    @java.lang.Override
    public void setTitle(int resId) {
        setTitle(mContext.getString(resId));
    }

    @java.lang.Override
    public void setSubtitle(int resId) {
        setSubtitle(mContext.getString(resId));
    }

    public void setSelectedNavigationItem(int position) {
        switch (mDecorToolbar.getNavigationMode()) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS :
                selectTab(mTabs.get(position));
                break;
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_LIST :
                mDecorToolbar.setDropdownSelectedPosition(position);
                break;
            default :
                throw new java.lang.IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
        }
    }

    public void removeAllTabs() {
        cleanupTabs();
    }

    private void cleanupTabs() {
        if (mSelectedTab != null) {
            selectTab(null);
        }
        mTabs.clear();
        if (mTabScrollView != null) {
            mTabScrollView.removeAllTabs();
        }
        mSavedTabPosition = android.support.v7.app.WindowDecorActionBar.INVALID_POSITION;
    }

    public void setTitle(java.lang.CharSequence title) {
        mDecorToolbar.setTitle(title);
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

    public void setSubtitle(java.lang.CharSequence subtitle) {
        mDecorToolbar.setSubtitle(subtitle);
    }

    public void setDisplayOptions(int options) {
        if ((options & android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
            mDisplayHomeAsUpSet = true;
        }
        mDecorToolbar.setDisplayOptions(options);
    }

    public void setDisplayOptions(int options, int mask) {
        final int current = mDecorToolbar.getDisplayOptions();
        if ((mask & android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP) != 0) {
            mDisplayHomeAsUpSet = true;
        }
        mDecorToolbar.setDisplayOptions((options & mask) | (current & (~mask)));
    }

    public void setBackgroundDrawable(android.graphics.drawable.Drawable d) {
        mContainerView.setPrimaryBackground(d);
    }

    public void setStackedBackgroundDrawable(android.graphics.drawable.Drawable d) {
        mContainerView.setStackedBackground(d);
    }

    public void setSplitBackgroundDrawable(android.graphics.drawable.Drawable d) {
        // no-op. We don't support split action bars
    }

    public android.view.View getCustomView() {
        return mDecorToolbar.getCustomView();
    }

    public java.lang.CharSequence getTitle() {
        return mDecorToolbar.getTitle();
    }

    public java.lang.CharSequence getSubtitle() {
        return mDecorToolbar.getSubtitle();
    }

    public int getNavigationMode() {
        return mDecorToolbar.getNavigationMode();
    }

    public int getDisplayOptions() {
        return mDecorToolbar.getDisplayOptions();
    }

    public android.support.v7.view.ActionMode startActionMode(android.support.v7.view.ActionMode.Callback callback) {
        if (mActionMode != null) {
            mActionMode.finish();
        }
        mOverlayLayout.setHideOnContentScrollEnabled(false);
        mContextView.killMode();
        android.support.v7.app.WindowDecorActionBar.ActionModeImpl mode = new android.support.v7.app.WindowDecorActionBar.ActionModeImpl(mContextView.getContext(), callback);
        if (mode.dispatchOnCreate()) {
            // This needs to be set before invalidate() so that it calls
            // onPrepareActionMode()
            mActionMode = mode;
            mode.invalidate();
            mContextView.initForMode(mode);
            animateToMode(true);
            mContextView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            return mode;
        }
        return null;
    }

    private void configureTab(android.support.v7.app.ActionBar.Tab tab, int position) {
        final android.support.v7.app.WindowDecorActionBar.TabImpl tabi = ((android.support.v7.app.WindowDecorActionBar.TabImpl) (tab));
        final android.support.v7.app.ActionBar.TabListener callback = tabi.getCallback();
        if (callback == null) {
            throw new java.lang.IllegalStateException("Action Bar Tab must have a Callback");
        }
        tabi.setPosition(position);
        mTabs.add(position, tabi);
        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, int position) {
        addTab(tab, position, mTabs.isEmpty());
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, boolean setSelected) {
        ensureTabsExist();
        mTabScrollView.addTab(tab, setSelected);
        configureTab(tab, mTabs.size());
        if (setSelected) {
            selectTab(tab);
        }
    }

    @java.lang.Override
    public void addTab(android.support.v7.app.ActionBar.Tab tab, int position, boolean setSelected) {
        ensureTabsExist();
        mTabScrollView.addTab(tab, position, setSelected);
        configureTab(tab, position);
        if (setSelected) {
            selectTab(tab);
        }
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab newTab() {
        return new android.support.v7.app.WindowDecorActionBar.TabImpl();
    }

    @java.lang.Override
    public void removeTab(android.support.v7.app.ActionBar.Tab tab) {
        removeTabAt(tab.getPosition());
    }

    @java.lang.Override
    public void removeTabAt(int position) {
        if (mTabScrollView == null) {
            // No tabs around to remove
            return;
        }
        int selectedTabPosition = (mSelectedTab != null) ? mSelectedTab.getPosition() : mSavedTabPosition;
        mTabScrollView.removeTabAt(position);
        android.support.v7.app.WindowDecorActionBar.TabImpl removedTab = mTabs.remove(position);
        if (removedTab != null) {
            removedTab.setPosition(-1);
        }
        final int newTabCount = mTabs.size();
        for (int i = position; i < newTabCount; i++) {
            mTabs.get(i).setPosition(i);
        }
        if (selectedTabPosition == position) {
            selectTab(mTabs.isEmpty() ? null : mTabs.get(java.lang.Math.max(0, position - 1)));
        }
    }

    @java.lang.Override
    public void selectTab(android.support.v7.app.ActionBar.Tab tab) {
        if (getNavigationMode() != android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS) {
            mSavedTabPosition = (tab != null) ? tab.getPosition() : android.support.v7.app.WindowDecorActionBar.INVALID_POSITION;
            return;
        }
        final android.support.v4.app.FragmentTransaction trans;
        if ((mActivity instanceof android.support.v4.app.FragmentActivity) && (!mDecorToolbar.getViewGroup().isInEditMode())) {
            // If we're not in edit mode and our Activity is a FragmentActivity, start a tx
            trans = ((android.support.v4.app.FragmentActivity) (mActivity)).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
        } else {
            trans = null;
        }
        if (mSelectedTab == tab) {
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabReselected(mSelectedTab, trans);
                mTabScrollView.animateToTab(tab.getPosition());
            }
        } else {
            mTabScrollView.setTabSelected(tab != null ? tab.getPosition() : android.support.v7.app.ActionBar.Tab.INVALID_POSITION);
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabUnselected(mSelectedTab, trans);
            }
            mSelectedTab = ((android.support.v7.app.WindowDecorActionBar.TabImpl) (tab));
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabSelected(mSelectedTab, trans);
            }
        }
        if ((trans != null) && (!trans.isEmpty())) {
            trans.commit();
        }
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab getSelectedTab() {
        return mSelectedTab;
    }

    @java.lang.Override
    public int getHeight() {
        return mContainerView.getHeight();
    }

    public void enableContentAnimations(boolean enabled) {
        mContentAnimations = enabled;
    }

    @java.lang.Override
    public void show() {
        if (mHiddenByApp) {
            mHiddenByApp = false;
            updateVisibility(false);
        }
    }

    private void showForActionMode() {
        if (!mShowingForMode) {
            mShowingForMode = true;
            if (mOverlayLayout != null) {
                mOverlayLayout.setShowingForActionMode(true);
            }
            updateVisibility(false);
        }
    }

    public void showForSystem() {
        if (mHiddenBySystem) {
            mHiddenBySystem = false;
            updateVisibility(true);
        }
    }

    @java.lang.Override
    public void hide() {
        if (!mHiddenByApp) {
            mHiddenByApp = true;
            updateVisibility(false);
        }
    }

    private void hideForActionMode() {
        if (mShowingForMode) {
            mShowingForMode = false;
            if (mOverlayLayout != null) {
                mOverlayLayout.setShowingForActionMode(false);
            }
            updateVisibility(false);
        }
    }

    public void hideForSystem() {
        if (!mHiddenBySystem) {
            mHiddenBySystem = true;
            updateVisibility(true);
        }
    }

    @java.lang.Override
    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll && (!mOverlayLayout.isInOverlayMode())) {
            throw new java.lang.IllegalStateException("Action bar must be in overlay mode " + "(Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        mHideOnContentScroll = hideOnContentScroll;
        mOverlayLayout.setHideOnContentScrollEnabled(hideOnContentScroll);
    }

    @java.lang.Override
    public boolean isHideOnContentScrollEnabled() {
        return mOverlayLayout.isHideOnContentScrollEnabled();
    }

    @java.lang.Override
    public int getHideOffset() {
        return mOverlayLayout.getActionBarHideOffset();
    }

    @java.lang.Override
    public void setHideOffset(int offset) {
        if ((offset != 0) && (!mOverlayLayout.isInOverlayMode())) {
            throw new java.lang.IllegalStateException("Action bar must be in overlay mode " + "(Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
        }
        mOverlayLayout.setActionBarHideOffset(offset);
    }

    static boolean checkShowingFlags(boolean hiddenByApp, boolean hiddenBySystem, boolean showingForMode) {
        if (showingForMode) {
            return true;
        } else
            if (hiddenByApp || hiddenBySystem) {
                return false;
            } else {
                return true;
            }

    }

    private void updateVisibility(boolean fromSystem) {
        // Based on the current state, should we be hidden or shown?
        final boolean shown = android.support.v7.app.WindowDecorActionBar.checkShowingFlags(mHiddenByApp, mHiddenBySystem, mShowingForMode);
        if (shown) {
            if (!mNowShowing) {
                mNowShowing = true;
                doShow(fromSystem);
            }
        } else {
            if (mNowShowing) {
                mNowShowing = false;
                doHide(fromSystem);
            }
        }
    }

    public void doShow(boolean fromSystem) {
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
        }
        mContainerView.setVisibility(android.view.View.VISIBLE);
        if (((mCurWindowVisibility == android.view.View.VISIBLE) && android.support.v7.app.WindowDecorActionBar.ALLOW_SHOW_HIDE_ANIMATIONS) && (mShowHideAnimationEnabled || fromSystem)) {
            // because we're about to ask its window loc
            android.support.v4.view.ViewCompat.setTranslationY(mContainerView, 0.0F);
            float startingY = -mContainerView.getHeight();
            if (fromSystem) {
                int[] topLeft = new int[]{ 0, 0 };
                mContainerView.getLocationInWindow(topLeft);
                startingY -= topLeft[1];
            }
            android.support.v4.view.ViewCompat.setTranslationY(mContainerView, startingY);
            android.support.v7.view.ViewPropertyAnimatorCompatSet anim = new android.support.v7.view.ViewPropertyAnimatorCompatSet();
            android.support.v4.view.ViewPropertyAnimatorCompat a = android.support.v4.view.ViewCompat.animate(mContainerView).translationY(0.0F);
            a.setUpdateListener(mUpdateListener);
            anim.play(a);
            if (mContentAnimations && (mContentView != null)) {
                android.support.v4.view.ViewCompat.setTranslationY(mContentView, startingY);
                anim.play(android.support.v4.view.ViewCompat.animate(mContentView).translationY(0.0F));
            }
            anim.setInterpolator(android.support.v7.app.WindowDecorActionBar.sShowInterpolator);
            anim.setDuration(250);
            // If this is being shown from the system, add a small delay.
            // This is because we will also be animating in the status bar,
            // and these two elements can't be done in lock-step.  So we give
            // a little time for the status bar to start its animation before
            // the action bar animates.  (This corresponds to the corresponding
            // case when hiding, where the status bar has a small delay before
            // starting.)
            anim.setListener(mShowListener);
            mCurrentShowAnim = anim;
            anim.start();
        } else {
            android.support.v4.view.ViewCompat.setAlpha(mContainerView, 1.0F);
            android.support.v4.view.ViewCompat.setTranslationY(mContainerView, 0);
            if (mContentAnimations && (mContentView != null)) {
                android.support.v4.view.ViewCompat.setTranslationY(mContentView, 0);
            }
            mShowListener.onAnimationEnd(null);
        }
        if (mOverlayLayout != null) {
            android.support.v4.view.ViewCompat.requestApplyInsets(mOverlayLayout);
        }
    }

    public void doHide(boolean fromSystem) {
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
        }
        if (((mCurWindowVisibility == android.view.View.VISIBLE) && android.support.v7.app.WindowDecorActionBar.ALLOW_SHOW_HIDE_ANIMATIONS) && (mShowHideAnimationEnabled || fromSystem)) {
            android.support.v4.view.ViewCompat.setAlpha(mContainerView, 1.0F);
            mContainerView.setTransitioning(true);
            android.support.v7.view.ViewPropertyAnimatorCompatSet anim = new android.support.v7.view.ViewPropertyAnimatorCompatSet();
            float endingY = -mContainerView.getHeight();
            if (fromSystem) {
                int[] topLeft = new int[]{ 0, 0 };
                mContainerView.getLocationInWindow(topLeft);
                endingY -= topLeft[1];
            }
            android.support.v4.view.ViewPropertyAnimatorCompat a = android.support.v4.view.ViewCompat.animate(mContainerView).translationY(endingY);
            a.setUpdateListener(mUpdateListener);
            anim.play(a);
            if (mContentAnimations && (mContentView != null)) {
                anim.play(android.support.v4.view.ViewCompat.animate(mContentView).translationY(endingY));
            }
            anim.setInterpolator(android.support.v7.app.WindowDecorActionBar.sHideInterpolator);
            anim.setDuration(250);
            anim.setListener(mHideListener);
            mCurrentShowAnim = anim;
            anim.start();
        } else {
            mHideListener.onAnimationEnd(null);
        }
    }

    public boolean isShowing() {
        final int height = getHeight();
        // Take into account the case where the bar has a 0 height due to not being measured yet.
        return mNowShowing && ((height == 0) || (getHideOffset() < height));
    }

    public void animateToMode(boolean toActionMode) {
        if (toActionMode) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        if (shouldAnimateContextView()) {
            android.support.v4.view.ViewPropertyAnimatorCompat fadeIn;
            android.support.v4.view.ViewPropertyAnimatorCompat fadeOut;
            if (toActionMode) {
                // We use INVISIBLE for the Toolbar to make sure that the container has a non-zero
                // height throughout. The context view is GONE initially, so will not have been laid
                // out when the animation starts. This can lead to the container collapsing to 0px
                // height for a short period.
                fadeOut = mDecorToolbar.setupAnimatorToVisibility(android.view.View.INVISIBLE, android.support.v7.app.WindowDecorActionBar.FADE_OUT_DURATION_MS);
                fadeIn = mContextView.setupAnimatorToVisibility(android.view.View.VISIBLE, android.support.v7.app.WindowDecorActionBar.FADE_IN_DURATION_MS);
            } else {
                fadeIn = mDecorToolbar.setupAnimatorToVisibility(android.view.View.VISIBLE, android.support.v7.app.WindowDecorActionBar.FADE_IN_DURATION_MS);
                fadeOut = mContextView.setupAnimatorToVisibility(android.view.View.GONE, android.support.v7.app.WindowDecorActionBar.FADE_OUT_DURATION_MS);
            }
            android.support.v7.view.ViewPropertyAnimatorCompatSet set = new android.support.v7.view.ViewPropertyAnimatorCompatSet();
            set.playSequentially(fadeOut, fadeIn);
            set.start();
        } else {
            if (toActionMode) {
                mDecorToolbar.setVisibility(android.view.View.INVISIBLE);
                mContextView.setVisibility(android.view.View.VISIBLE);
            } else {
                mDecorToolbar.setVisibility(android.view.View.VISIBLE);
                mContextView.setVisibility(android.view.View.GONE);
            }
        }
        // mTabScrollView's visibility is not affected by action mode.
    }

    private boolean shouldAnimateContextView() {
        // We only to animate the action mode in if the container view has already been laid out.
        // If it hasn't been laid out, it hasn't been drawn to screen yet.
        return android.support.v4.view.ViewCompat.isLaidOut(mContainerView);
    }

    public android.content.Context getThemedContext() {
        if (mThemedContext == null) {
            android.util.TypedValue outValue = new android.util.TypedValue();
            android.content.res.Resources.Theme currentTheme = mContext.getTheme();
            currentTheme.resolveAttribute(R.attr.actionBarWidgetTheme, outValue, true);
            final int targetThemeRes = outValue.resourceId;
            if (targetThemeRes != 0) {
                mThemedContext = new android.view.ContextThemeWrapper(mContext, targetThemeRes);
            } else {
                mThemedContext = mContext;
            }
        }
        return mThemedContext;
    }

    @java.lang.Override
    public boolean isTitleTruncated() {
        return (mDecorToolbar != null) && mDecorToolbar.isTitleTruncated();
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
    public void setHomeActionContentDescription(int resId) {
        mDecorToolbar.setNavigationContentDescription(resId);
    }

    @java.lang.Override
    public void onContentScrollStarted() {
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
            mCurrentShowAnim = null;
        }
    }

    @java.lang.Override
    public void onContentScrollStopped() {
    }

    @java.lang.Override
    public boolean collapseActionView() {
        if ((mDecorToolbar != null) && mDecorToolbar.hasExpandedActionView()) {
            mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public class ActionModeImpl extends android.support.v7.view.ActionMode implements android.support.v7.view.menu.MenuBuilder.Callback {
        private final android.content.Context mActionModeContext;

        private final android.support.v7.view.menu.MenuBuilder mMenu;

        private android.support.v7.view.ActionMode.Callback mCallback;

        private java.lang.ref.WeakReference<android.view.View> mCustomView;

        public ActionModeImpl(android.content.Context context, android.support.v7.view.ActionMode.Callback callback) {
            mActionModeContext = context;
            mCallback = callback;
            mMenu = new android.support.v7.view.menu.MenuBuilder(context).setDefaultShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            mMenu.setCallback(this);
        }

        @java.lang.Override
        public android.view.MenuInflater getMenuInflater() {
            return new android.support.v7.view.SupportMenuInflater(mActionModeContext);
        }

        @java.lang.Override
        public android.view.Menu getMenu() {
            return mMenu;
        }

        @java.lang.Override
        public void finish() {
            if (mActionMode != this) {
                // Not the active action mode - no-op
                return;
            }
            // If this change in state is going to cause the action bar
            // to be hidden, defer the onDestroy callback until the animation
            // is finished and associated relayout is about to happen. This lets
            // apps better anticipate visibility and layout behavior.
            if (!android.support.v7.app.WindowDecorActionBar.checkShowingFlags(mHiddenByApp, mHiddenBySystem, false)) {
                // With the current state but the action bar hidden, our
                // overall showing state is going to be false.
                mDeferredDestroyActionMode = this;
                mDeferredModeDestroyCallback = mCallback;
            } else {
                mCallback.onDestroyActionMode(this);
            }
            mCallback = null;
            animateToMode(false);
            // Clear out the context mode views after the animation finishes
            mContextView.closeMode();
            mDecorToolbar.getViewGroup().sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            mOverlayLayout.setHideOnContentScrollEnabled(mHideOnContentScroll);
            mActionMode = null;
        }

        @java.lang.Override
        public void invalidate() {
            if (mActionMode != this) {
                // Not the active action mode - no-op. It's possible we are
                // currently deferring onDestroy, so the app doesn't yet know we
                // are going away and is trying to use us. That's also a no-op.
                return;
            }
            mMenu.stopDispatchingItemsChanged();
            try {
                mCallback.onPrepareActionMode(this, mMenu);
            } finally {
                mMenu.startDispatchingItemsChanged();
            }
        }

        public boolean dispatchOnCreate() {
            mMenu.stopDispatchingItemsChanged();
            try {
                return mCallback.onCreateActionMode(this, mMenu);
            } finally {
                mMenu.startDispatchingItemsChanged();
            }
        }

        @java.lang.Override
        public void setCustomView(android.view.View view) {
            mContextView.setCustomView(view);
            mCustomView = new java.lang.ref.WeakReference<android.view.View>(view);
        }

        @java.lang.Override
        public void setSubtitle(java.lang.CharSequence subtitle) {
            mContextView.setSubtitle(subtitle);
        }

        @java.lang.Override
        public void setTitle(java.lang.CharSequence title) {
            mContextView.setTitle(title);
        }

        @java.lang.Override
        public void setTitle(int resId) {
            setTitle(mContext.getResources().getString(resId));
        }

        @java.lang.Override
        public void setSubtitle(int resId) {
            setSubtitle(mContext.getResources().getString(resId));
        }

        @java.lang.Override
        public java.lang.CharSequence getTitle() {
            return mContextView.getTitle();
        }

        @java.lang.Override
        public java.lang.CharSequence getSubtitle() {
            return mContextView.getSubtitle();
        }

        @java.lang.Override
        public void setTitleOptionalHint(boolean titleOptional) {
            super.setTitleOptionalHint(titleOptional);
            mContextView.setTitleOptional(titleOptional);
        }

        @java.lang.Override
        public boolean isTitleOptional() {
            return mContextView.isTitleOptional();
        }

        @java.lang.Override
        public android.view.View getCustomView() {
            return mCustomView != null ? mCustomView.get() : null;
        }

        public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
            if (mCallback != null) {
                return mCallback.onActionItemClicked(this, item);
            } else {
                return false;
            }
        }

        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
            if (mCallback == null) {
                return false;
            }
            if (!subMenu.hasVisibleItems()) {
                return true;
            }
            new android.support.v7.view.menu.MenuPopupHelper(getThemedContext(), subMenu).show();
            return true;
        }

        public void onCloseSubMenu(android.support.v7.view.menu.SubMenuBuilder menu) {
        }

        public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
            if (mCallback == null) {
                return;
            }
            invalidate();
            mContextView.showOverflowMenu();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public class TabImpl extends android.support.v7.app.ActionBar.Tab {
        private android.support.v7.app.ActionBar.TabListener mCallback;

        private java.lang.Object mTag;

        private android.graphics.drawable.Drawable mIcon;

        private java.lang.CharSequence mText;

        private java.lang.CharSequence mContentDesc;

        private int mPosition = -1;

        private android.view.View mCustomView;

        @java.lang.Override
        public java.lang.Object getTag() {
            return mTag;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setTag(java.lang.Object tag) {
            mTag = tag;
            return this;
        }

        public android.support.v7.app.ActionBar.TabListener getCallback() {
            return mCallback;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setTabListener(android.support.v7.app.ActionBar.TabListener callback) {
            mCallback = callback;
            return this;
        }

        @java.lang.Override
        public android.view.View getCustomView() {
            return mCustomView;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setCustomView(android.view.View view) {
            mCustomView = view;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setCustomView(int layoutResId) {
            return setCustomView(android.view.LayoutInflater.from(getThemedContext()).inflate(layoutResId, null));
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getIcon() {
            return mIcon;
        }

        @java.lang.Override
        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        @java.lang.Override
        public java.lang.CharSequence getText() {
            return mText;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setIcon(android.graphics.drawable.Drawable icon) {
            mIcon = icon;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setIcon(int resId) {
            return setIcon(android.support.v7.content.res.AppCompatResources.getDrawable(mContext, resId));
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setText(java.lang.CharSequence text) {
            mText = text;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setText(int resId) {
            return setText(mContext.getResources().getText(resId));
        }

        @java.lang.Override
        public void select() {
            selectTab(this);
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setContentDescription(int resId) {
            return setContentDescription(mContext.getResources().getText(resId));
        }

        @java.lang.Override
        public android.support.v7.app.ActionBar.Tab setContentDescription(java.lang.CharSequence contentDesc) {
            mContentDesc = contentDesc;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        @java.lang.Override
        public java.lang.CharSequence getContentDescription() {
            return mContentDesc;
        }
    }

    @java.lang.Override
    public void setCustomView(android.view.View view) {
        mDecorToolbar.setCustomView(view);
    }

    @java.lang.Override
    public void setCustomView(android.view.View view, android.support.v7.app.ActionBar.LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        mDecorToolbar.setCustomView(view);
    }

    @java.lang.Override
    public void setListNavigationCallbacks(android.widget.SpinnerAdapter adapter, android.support.v7.app.ActionBar.OnNavigationListener callback) {
        mDecorToolbar.setDropdownParams(adapter, new android.support.v7.app.NavItemSelectedListener(callback));
    }

    @java.lang.Override
    public int getSelectedNavigationIndex() {
        switch (mDecorToolbar.getNavigationMode()) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS :
                return mSelectedTab != null ? mSelectedTab.getPosition() : -1;
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_LIST :
                return mDecorToolbar.getDropdownSelectedPosition();
            default :
                return -1;
        }
    }

    @java.lang.Override
    public int getNavigationItemCount() {
        switch (mDecorToolbar.getNavigationMode()) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS :
                return mTabs.size();
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_LIST :
                return mDecorToolbar.getDropdownItemCount();
            default :
                return 0;
        }
    }

    @java.lang.Override
    public int getTabCount() {
        return mTabs.size();
    }

    @java.lang.Override
    public void setNavigationMode(int mode) {
        final int oldMode = mDecorToolbar.getNavigationMode();
        switch (oldMode) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS :
                mSavedTabPosition = getSelectedNavigationIndex();
                selectTab(null);
                mTabScrollView.setVisibility(android.view.View.GONE);
                break;
        }
        if ((oldMode != mode) && (!mHasEmbeddedTabs)) {
            if (mOverlayLayout != null) {
                android.support.v4.view.ViewCompat.requestApplyInsets(mOverlayLayout);
            }
        }
        mDecorToolbar.setNavigationMode(mode);
        switch (mode) {
            case android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS :
                ensureTabsExist();
                mTabScrollView.setVisibility(android.view.View.VISIBLE);
                if (mSavedTabPosition != android.support.v7.app.WindowDecorActionBar.INVALID_POSITION) {
                    setSelectedNavigationItem(mSavedTabPosition);
                    mSavedTabPosition = android.support.v7.app.WindowDecorActionBar.INVALID_POSITION;
                }
                break;
        }
        mDecorToolbar.setCollapsible((mode == android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS) && (!mHasEmbeddedTabs));
        mOverlayLayout.setHasNonEmbeddedTabs((mode == android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS) && (!mHasEmbeddedTabs));
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar.Tab getTabAt(int index) {
        return mTabs.get(index);
    }

    @java.lang.Override
    public void setIcon(int resId) {
        mDecorToolbar.setIcon(resId);
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable icon) {
        mDecorToolbar.setIcon(icon);
    }

    public boolean hasIcon() {
        return mDecorToolbar.hasIcon();
    }

    @java.lang.Override
    public void setLogo(int resId) {
        mDecorToolbar.setLogo(resId);
    }

    @java.lang.Override
    public void setLogo(android.graphics.drawable.Drawable logo) {
        mDecorToolbar.setLogo(logo);
    }

    public boolean hasLogo() {
        return mDecorToolbar.hasLogo();
    }

    public void setDefaultDisplayHomeAsUpEnabled(boolean enable) {
        if (!mDisplayHomeAsUpSet) {
            setDisplayHomeAsUpEnabled(enable);
        }
    }
}

