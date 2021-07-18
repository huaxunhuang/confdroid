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
package android.support.v7.app;


class AppCompatDelegateImplV7 extends android.support.v7.app.AppCompatDelegateImplBase implements android.support.v4.view.LayoutInflaterFactory , android.support.v7.view.menu.MenuBuilder.Callback {
    private android.support.v7.widget.DecorContentParent mDecorContentParent;

    private android.support.v7.app.AppCompatDelegateImplV7.ActionMenuPresenterCallback mActionMenuPresenterCallback;

    private android.support.v7.app.AppCompatDelegateImplV7.PanelMenuPresenterCallback mPanelMenuPresenterCallback;

    android.support.v7.view.ActionMode mActionMode;

    android.support.v7.widget.ActionBarContextView mActionModeView;

    android.widget.PopupWindow mActionModePopup;

    java.lang.Runnable mShowActionModePopup;

    android.support.v4.view.ViewPropertyAnimatorCompat mFadeAnim = null;

    // true if we have installed a window sub-decor layout.
    private boolean mSubDecorInstalled;

    private android.view.ViewGroup mSubDecor;

    private android.widget.TextView mTitleView;

    private android.view.View mStatusGuard;

    private boolean mFeatureProgress;

    // Used to keep track of Progress Bar Window features
    private boolean mFeatureIndeterminateProgress;

    // Used for emulating PanelFeatureState
    private boolean mClosingActionMenu;

    private android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState[] mPanels;

    private android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState mPreparedPanel;

    private boolean mLongPressBackDown;

    private boolean mInvalidatePanelMenuPosted;

    private int mInvalidatePanelMenuFeatures;

    private final java.lang.Runnable mInvalidatePanelMenuRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if ((mInvalidatePanelMenuFeatures & (1 << android.view.Window.FEATURE_OPTIONS_PANEL)) != 0) {
                doInvalidatePanelMenu(android.view.Window.FEATURE_OPTIONS_PANEL);
            }
            if ((mInvalidatePanelMenuFeatures & (1 << android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR)) != 0) {
                doInvalidatePanelMenu(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR);
            }
            mInvalidatePanelMenuPosted = false;
            mInvalidatePanelMenuFeatures = 0;
        }
    };

    private boolean mEnableDefaultActionBarUp;

    private android.graphics.Rect mTempRect1;

    private android.graphics.Rect mTempRect2;

    private android.support.v7.app.AppCompatViewInflater mAppCompatViewInflater;

    AppCompatDelegateImplV7(android.content.Context context, android.view.Window window, android.support.v7.app.AppCompatCallback callback) {
        super(context, window, callback);
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        if (mOriginalWindowCallback instanceof android.app.Activity) {
            if (android.support.v4.app.NavUtils.getParentActivityName(((android.app.Activity) (mOriginalWindowCallback))) != null) {
                // Peek at the Action Bar and update it if it already exists
                android.support.v7.app.ActionBar ab = peekSupportActionBar();
                if (ab == null) {
                    mEnableDefaultActionBarUp = true;
                } else {
                    ab.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    @java.lang.Override
    public void onPostCreate(android.os.Bundle savedInstanceState) {
        // Make sure that the sub decor is installed
        ensureSubDecor();
    }

    @java.lang.Override
    public void initWindowDecorActionBar() {
        ensureSubDecor();
        if ((!mHasActionBar) || (mActionBar != null)) {
            return;
        }
        if (mOriginalWindowCallback instanceof android.app.Activity) {
            mActionBar = new android.support.v7.app.WindowDecorActionBar(((android.app.Activity) (mOriginalWindowCallback)), mOverlayActionBar);
        } else
            if (mOriginalWindowCallback instanceof android.app.Dialog) {
                mActionBar = new android.support.v7.app.WindowDecorActionBar(((android.app.Dialog) (mOriginalWindowCallback)));
            }

        if (mActionBar != null) {
            mActionBar.setDefaultDisplayHomeAsUpEnabled(mEnableDefaultActionBarUp);
        }
    }

    @java.lang.Override
    public void setSupportActionBar(android.support.v7.widget.Toolbar toolbar) {
        if (!(mOriginalWindowCallback instanceof android.app.Activity)) {
            // Only Activities support custom Action Bars
            return;
        }
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab instanceof android.support.v7.app.WindowDecorActionBar) {
            throw new java.lang.IllegalStateException("This Activity already has an action bar supplied " + ("by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set " + "windowActionBar to false in your theme to use a Toolbar instead."));
        }
        // If we reach here then we're setting a new action bar
        // First clear out the MenuInflater to make sure that it is valid for the new Action Bar
        mMenuInflater = null;
        // If we have an action bar currently, destroy it
        if (ab != null) {
            ab.onDestroy();
        }
        if (toolbar != null) {
            final android.support.v7.app.ToolbarActionBar tbab = new android.support.v7.app.ToolbarActionBar(toolbar, ((android.app.Activity) (mContext)).getTitle(), mAppCompatWindowCallback);
            mActionBar = tbab;
            mWindow.setCallback(tbab.getWrappedWindowCallback());
        } else {
            mActionBar = null;
            // Re-set the original window callback since we may have already set a Toolbar wrapper
            mWindow.setCallback(mAppCompatWindowCallback);
        }
        invalidateOptionsMenu();
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View findViewById(@android.support.annotation.IdRes
    int id) {
        ensureSubDecor();
        return mWindow.findViewById(id);
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        // If this is called before sub-decor is installed, ActionBar will not
        // be properly initialized.
        if (mHasActionBar && mSubDecorInstalled) {
            // Note: The action bar will need to access
            // view changes from superclass.
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.onConfigurationChanged(newConfig);
            }
        }
        // Re-apply Day/Night to the new configuration
        applyDayNight();
    }

    @java.lang.Override
    public void onStop() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
        }
    }

    @java.lang.Override
    public void onPostResume() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(true);
        }
    }

    @java.lang.Override
    public void setContentView(android.view.View v) {
        ensureSubDecor();
        android.view.ViewGroup contentParent = ((android.view.ViewGroup) (mSubDecor.findViewById(android.R.id.content)));
        contentParent.removeAllViews();
        contentParent.addView(v);
        mOriginalWindowCallback.onContentChanged();
    }

    @java.lang.Override
    public void setContentView(int resId) {
        ensureSubDecor();
        android.view.ViewGroup contentParent = ((android.view.ViewGroup) (mSubDecor.findViewById(android.R.id.content)));
        contentParent.removeAllViews();
        android.view.LayoutInflater.from(mContext).inflate(resId, contentParent);
        mOriginalWindowCallback.onContentChanged();
    }

    @java.lang.Override
    public void setContentView(android.view.View v, android.view.ViewGroup.LayoutParams lp) {
        ensureSubDecor();
        android.view.ViewGroup contentParent = ((android.view.ViewGroup) (mSubDecor.findViewById(android.R.id.content)));
        contentParent.removeAllViews();
        contentParent.addView(v, lp);
        mOriginalWindowCallback.onContentChanged();
    }

    @java.lang.Override
    public void addContentView(android.view.View v, android.view.ViewGroup.LayoutParams lp) {
        ensureSubDecor();
        android.view.ViewGroup contentParent = ((android.view.ViewGroup) (mSubDecor.findViewById(android.R.id.content)));
        contentParent.addView(v, lp);
        mOriginalWindowCallback.onContentChanged();
    }

    @java.lang.Override
    public void onDestroy() {
        super.onDestroy();
        if (mActionBar != null) {
            mActionBar.onDestroy();
        }
    }

    private void ensureSubDecor() {
        if (!mSubDecorInstalled) {
            mSubDecor = createSubDecor();
            // If a title was set before we installed the decor, propogate it now
            java.lang.CharSequence title = getTitle();
            if (!android.text.TextUtils.isEmpty(title)) {
                onTitleChanged(title);
            }
            applyFixedSizeWindow();
            onSubDecorInstalled(mSubDecor);
            mSubDecorInstalled = true;
            // Invalidate if the panel menu hasn't been created before this.
            // Panel menu invalidation is deferred avoiding application onCreateOptionsMenu
            // being called in the middle of onCreate or similar.
            // A pending invalidation will typically be resolved before the posted message
            // would run normally in order to satisfy instance state restoration.
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, false);
            if ((!isDestroyed()) && ((st == null) || (st.menu == null))) {
                invalidatePanelMenu(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR);
            }
        }
    }

    private android.view.ViewGroup createSubDecor() {
        android.content.res.TypedArray a = mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!a.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            a.recycle();
            throw new java.lang.IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (a.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
            requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        } else
            if (a.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
                // Don't allow an action bar if there is no title.
                requestWindowFeature(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR);
            }

        if (a.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            requestWindowFeature(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        }
        if (a.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            requestWindowFeature(android.support.v7.app.AppCompatDelegate.FEATURE_ACTION_MODE_OVERLAY);
        }
        mIsFloating = a.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
        a.recycle();
        // Now let's make sure that the Window has installed its decor by retrieving it
        mWindow.getDecorView();
        final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        android.view.ViewGroup subDecor = null;
        if (!mWindowNoTitle) {
            if (mIsFloating) {
                // If we're floating, inflate the dialog title decor
                subDecor = ((android.view.ViewGroup) (inflater.inflate(R.layout.abc_dialog_title_material, null)));
                // Floating windows can never have an action bar, reset the flags
                mHasActionBar = mOverlayActionBar = false;
            } else
                if (mHasActionBar) {
                    /**
                     * This needs some explanation. As we can not use the android:theme attribute
                     * pre-L, we emulate it by manually creating a LayoutInflater using a
                     * ContextThemeWrapper pointing to actionBarTheme.
                     */
                    android.util.TypedValue outValue = new android.util.TypedValue();
                    mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, outValue, true);
                    android.content.Context themedContext;
                    if (outValue.resourceId != 0) {
                        themedContext = new android.support.v7.view.ContextThemeWrapper(mContext, outValue.resourceId);
                    } else {
                        themedContext = mContext;
                    }
                    // Now inflate the view using the themed context and set it as the content view
                    subDecor = ((android.view.ViewGroup) (android.view.LayoutInflater.from(themedContext).inflate(R.layout.abc_screen_toolbar, null)));
                    mDecorContentParent = ((android.support.v7.widget.DecorContentParent) (subDecor.findViewById(R.id.decor_content_parent)));
                    mDecorContentParent.setWindowCallback(getWindowCallback());
                    /**
                     * Propagate features to DecorContentParent
                     */
                    if (mOverlayActionBar) {
                        mDecorContentParent.initFeature(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
                    }
                    if (mFeatureProgress) {
                        mDecorContentParent.initFeature(android.view.Window.FEATURE_PROGRESS);
                    }
                    if (mFeatureIndeterminateProgress) {
                        mDecorContentParent.initFeature(android.view.Window.FEATURE_INDETERMINATE_PROGRESS);
                    }
                }

        } else {
            if (mOverlayActionMode) {
                subDecor = ((android.view.ViewGroup) (inflater.inflate(R.layout.abc_screen_simple_overlay_action_mode, null)));
            } else {
                subDecor = ((android.view.ViewGroup) (inflater.inflate(R.layout.abc_screen_simple, null)));
            }
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                // If we're running on L or above, we can rely on ViewCompat's
                // setOnApplyWindowInsetsListener
                android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(subDecor, new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @java.lang.Override
                    public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
                        final int top = insets.getSystemWindowInsetTop();
                        final int newTop = updateStatusGuard(top);
                        if (top != newTop) {
                            insets = insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), newTop, insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
                        }
                        // Now apply the insets on our view
                        return android.support.v4.view.ViewCompat.onApplyWindowInsets(v, insets);
                    }
                });
            } else {
                // Else, we need to use our own FitWindowsViewGroup handling
                ((android.support.v7.widget.FitWindowsViewGroup) (subDecor)).setOnFitSystemWindowsListener(new android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener() {
                    @java.lang.Override
                    public void onFitSystemWindows(android.graphics.Rect insets) {
                        insets.top = updateStatusGuard(insets.top);
                    }
                });
            }
        }
        if (subDecor == null) {
            throw new java.lang.IllegalArgumentException((((((((((("AppCompat does not support the current theme features: { " + "windowActionBar: ") + mHasActionBar) + ", windowActionBarOverlay: ") + mOverlayActionBar) + ", android:windowIsFloating: ") + mIsFloating) + ", windowActionModeOverlay: ") + mOverlayActionMode) + ", windowNoTitle: ") + mWindowNoTitle) + " }");
        }
        if (mDecorContentParent == null) {
            mTitleView = ((android.widget.TextView) (subDecor.findViewById(R.id.title)));
        }
        // Make the decor optionally fit system windows, like the window's decor
        android.support.v7.widget.ViewUtils.makeOptionalFitsSystemWindows(subDecor);
        final android.support.v7.widget.ContentFrameLayout contentView = ((android.support.v7.widget.ContentFrameLayout) (subDecor.findViewById(R.id.action_bar_activity_content)));
        final android.view.ViewGroup windowContentView = ((android.view.ViewGroup) (mWindow.findViewById(android.R.id.content)));
        if (windowContentView != null) {
            // There might be Views already added to the Window's content view so we need to
            // migrate them to our content view
            while (windowContentView.getChildCount() > 0) {
                final android.view.View child = windowContentView.getChildAt(0);
                windowContentView.removeViewAt(0);
                contentView.addView(child);
            } 
            // Change our content FrameLayout to use the android.R.id.content id.
            // Useful for fragments.
            windowContentView.setId(android.view.View.NO_ID);
            contentView.setId(android.R.id.content);
            // The decorContent may have a foreground drawable set (windowContentOverlay).
            // Remove this as we handle it ourselves
            if (windowContentView instanceof android.widget.FrameLayout) {
                ((android.widget.FrameLayout) (windowContentView)).setForeground(null);
            }
        }
        // Now set the Window's content view with the decor
        mWindow.setContentView(subDecor);
        contentView.setAttachListener(new android.support.v7.widget.ContentFrameLayout.OnAttachListener() {
            @java.lang.Override
            public void onAttachedFromWindow() {
            }

            @java.lang.Override
            public void onDetachedFromWindow() {
                dismissPopups();
            }
        });
        return subDecor;
    }

    void onSubDecorInstalled(android.view.ViewGroup subDecor) {
    }

    private void applyFixedSizeWindow() {
        android.support.v7.widget.ContentFrameLayout cfl = ((android.support.v7.widget.ContentFrameLayout) (mSubDecor.findViewById(android.R.id.content)));
        // This is a bit weird. In the framework, the window sizing attributes control
        // the decor view's size, meaning that any padding is inset for the min/max widths below.
        // We don't control measurement at that level, so we need to workaround it by making sure
        // that the decor view's padding is taken into account.
        final android.view.View windowDecor = mWindow.getDecorView();
        cfl.setDecorPadding(windowDecor.getPaddingLeft(), windowDecor.getPaddingTop(), windowDecor.getPaddingRight(), windowDecor.getPaddingBottom());
        android.content.res.TypedArray a = mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        a.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, cfl.getMinWidthMajor());
        a.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, cfl.getMinWidthMinor());
        if (a.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            a.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, cfl.getFixedWidthMajor());
        }
        if (a.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            a.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, cfl.getFixedWidthMinor());
        }
        if (a.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            a.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, cfl.getFixedHeightMajor());
        }
        if (a.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            a.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, cfl.getFixedHeightMinor());
        }
        a.recycle();
        cfl.requestLayout();
    }

    @java.lang.Override
    public boolean requestWindowFeature(int featureId) {
        featureId = sanitizeWindowFeatureId(featureId);
        if (mWindowNoTitle && (featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR)) {
            return false;// Ignore. No title dominates.

        }
        if (mHasActionBar && (featureId == android.view.Window.FEATURE_NO_TITLE)) {
            // Remove the action bar feature if we have no title. No title dominates.
            mHasActionBar = false;
        }
        switch (featureId) {
            case android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR :
                throwFeatureRequestIfSubDecorInstalled();
                mHasActionBar = true;
                return true;
            case android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY :
                throwFeatureRequestIfSubDecorInstalled();
                mOverlayActionBar = true;
                return true;
            case android.support.v7.app.AppCompatDelegate.FEATURE_ACTION_MODE_OVERLAY :
                throwFeatureRequestIfSubDecorInstalled();
                mOverlayActionMode = true;
                return true;
            case android.view.Window.FEATURE_PROGRESS :
                throwFeatureRequestIfSubDecorInstalled();
                mFeatureProgress = true;
                return true;
            case android.view.Window.FEATURE_INDETERMINATE_PROGRESS :
                throwFeatureRequestIfSubDecorInstalled();
                mFeatureIndeterminateProgress = true;
                return true;
            case android.view.Window.FEATURE_NO_TITLE :
                throwFeatureRequestIfSubDecorInstalled();
                mWindowNoTitle = true;
                return true;
        }
        return mWindow.requestFeature(featureId);
    }

    @java.lang.Override
    public boolean hasWindowFeature(int featureId) {
        featureId = sanitizeWindowFeatureId(featureId);
        switch (featureId) {
            case android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR :
                return mHasActionBar;
            case android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY :
                return mOverlayActionBar;
            case android.support.v7.app.AppCompatDelegate.FEATURE_ACTION_MODE_OVERLAY :
                return mOverlayActionMode;
            case android.view.Window.FEATURE_PROGRESS :
                return mFeatureProgress;
            case android.view.Window.FEATURE_INDETERMINATE_PROGRESS :
                return mFeatureIndeterminateProgress;
            case android.view.Window.FEATURE_NO_TITLE :
                return mWindowNoTitle;
        }
        return mWindow.hasFeature(featureId);
    }

    @java.lang.Override
    void onTitleChanged(java.lang.CharSequence title) {
        if (mDecorContentParent != null) {
            mDecorContentParent.setWindowTitle(title);
        } else
            if (peekSupportActionBar() != null) {
                peekSupportActionBar().setWindowTitle(title);
            } else
                if (mTitleView != null) {
                    mTitleView.setText(title);
                }


    }

    @java.lang.Override
    void onPanelClosed(final int featureId, android.view.Menu menu) {
        if (featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.dispatchMenuVisibilityChanged(false);
            }
        } else
            if (featureId == android.view.Window.FEATURE_OPTIONS_PANEL) {
                // Make sure that the options panel is closed. This is mainly used when we're using a
                // ToolbarActionBar
                android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(featureId, true);
                if (st.isOpen) {
                    closePanel(st, false);
                }
            }

    }

    @java.lang.Override
    boolean onMenuOpened(final int featureId, android.view.Menu menu) {
        if (featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.dispatchMenuVisibilityChanged(true);
            }
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
        final android.view.Window.Callback cb = getWindowCallback();
        if ((cb != null) && (!isDestroyed())) {
            final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState panel = findMenuPanel(menu.getRootMenu());
            if (panel != null) {
                return cb.onMenuItemSelected(panel.featureId, item);
            }
        }
        return false;
    }

    @java.lang.Override
    public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
        reopenMenu(menu, true);
    }

    @java.lang.Override
    public android.support.v7.view.ActionMode startSupportActionMode(@android.support.annotation.NonNull
    final android.support.v7.view.ActionMode.Callback callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (mActionMode != null) {
            mActionMode.finish();
        }
        final android.support.v7.view.ActionMode.Callback wrappedCallback = new android.support.v7.app.AppCompatDelegateImplV7.ActionModeCallbackWrapperV7(callback);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            mActionMode = ab.startActionMode(wrappedCallback);
            if ((mActionMode != null) && (mAppCompatCallback != null)) {
                mAppCompatCallback.onSupportActionModeStarted(mActionMode);
            }
        }
        if (mActionMode == null) {
            // If the action bar didn't provide an action mode, start the emulated window one
            mActionMode = startSupportActionModeFromWindow(wrappedCallback);
        }
        return mActionMode;
    }

    @java.lang.Override
    public void invalidateOptionsMenu() {
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        if ((ab != null) && ab.invalidateOptionsMenu())
            return;

        invalidatePanelMenu(android.view.Window.FEATURE_OPTIONS_PANEL);
    }

    @java.lang.Override
    android.support.v7.view.ActionMode startSupportActionModeFromWindow(@android.support.annotation.NonNull
    android.support.v7.view.ActionMode.Callback callback) {
        endOnGoingFadeAnimation();
        if (mActionMode != null) {
            mActionMode.finish();
        }
        if (!(callback instanceof android.support.v7.app.AppCompatDelegateImplV7.ActionModeCallbackWrapperV7)) {
            // If the callback hasn't been wrapped yet, wrap it
            callback = new android.support.v7.app.AppCompatDelegateImplV7.ActionModeCallbackWrapperV7(callback);
        }
        android.support.v7.view.ActionMode mode = null;
        if ((mAppCompatCallback != null) && (!isDestroyed())) {
            try {
                mode = mAppCompatCallback.onWindowStartingSupportActionMode(callback);
            } catch (java.lang.AbstractMethodError ame) {
                // Older apps might not implement this callback method.
            }
        }
        if (mode != null) {
            mActionMode = mode;
        } else {
            if (mActionModeView == null) {
                if (mIsFloating) {
                    // Use the action bar theme.
                    final android.util.TypedValue outValue = new android.util.TypedValue();
                    final android.content.res.Resources.Theme baseTheme = mContext.getTheme();
                    baseTheme.resolveAttribute(R.attr.actionBarTheme, outValue, true);
                    final android.content.Context actionBarContext;
                    if (outValue.resourceId != 0) {
                        final android.content.res.Resources.Theme actionBarTheme = mContext.getResources().newTheme();
                        actionBarTheme.setTo(baseTheme);
                        actionBarTheme.applyStyle(outValue.resourceId, true);
                        actionBarContext = new android.support.v7.view.ContextThemeWrapper(mContext, 0);
                        actionBarContext.getTheme().setTo(actionBarTheme);
                    } else {
                        actionBarContext = mContext;
                    }
                    mActionModeView = new android.support.v7.widget.ActionBarContextView(actionBarContext);
                    mActionModePopup = new android.widget.PopupWindow(actionBarContext, null, R.attr.actionModePopupWindowStyle);
                    android.support.v4.widget.PopupWindowCompat.setWindowLayoutType(mActionModePopup, android.view.WindowManager.LayoutParams.TYPE_APPLICATION);
                    mActionModePopup.setContentView(mActionModeView);
                    mActionModePopup.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                    actionBarContext.getTheme().resolveAttribute(R.attr.actionBarSize, outValue, true);
                    final int height = android.util.TypedValue.complexToDimensionPixelSize(outValue.data, actionBarContext.getResources().getDisplayMetrics());
                    mActionModeView.setContentHeight(height);
                    mActionModePopup.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                    mShowActionModePopup = new java.lang.Runnable() {
                        public void run() {
                            mActionModePopup.showAtLocation(mActionModeView, android.view.Gravity.TOP | android.view.Gravity.FILL_HORIZONTAL, 0, 0);
                            endOnGoingFadeAnimation();
                            if (shouldAnimateActionModeView()) {
                                android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 0.0F);
                                mFadeAnim = android.support.v4.view.ViewCompat.animate(mActionModeView).alpha(1.0F);
                                mFadeAnim.setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                                    @java.lang.Override
                                    public void onAnimationStart(android.view.View view) {
                                        mActionModeView.setVisibility(android.view.View.VISIBLE);
                                    }

                                    @java.lang.Override
                                    public void onAnimationEnd(android.view.View view) {
                                        android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 1.0F);
                                        mFadeAnim.setListener(null);
                                        mFadeAnim = null;
                                    }
                                });
                            } else {
                                android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 1.0F);
                                mActionModeView.setVisibility(android.view.View.VISIBLE);
                            }
                        }
                    };
                } else {
                    android.support.v7.widget.ViewStubCompat stub = ((android.support.v7.widget.ViewStubCompat) (mSubDecor.findViewById(R.id.action_mode_bar_stub)));
                    if (stub != null) {
                        // Set the layout inflater so that it is inflated with the action bar's context
                        stub.setLayoutInflater(android.view.LayoutInflater.from(getActionBarThemedContext()));
                        mActionModeView = ((android.support.v7.widget.ActionBarContextView) (stub.inflate()));
                    }
                }
            }
            if (mActionModeView != null) {
                endOnGoingFadeAnimation();
                mActionModeView.killMode();
                mode = new android.support.v7.view.StandaloneActionMode(mActionModeView.getContext(), mActionModeView, callback, mActionModePopup == null);
                if (callback.onCreateActionMode(mode, mode.getMenu())) {
                    mode.invalidate();
                    mActionModeView.initForMode(mode);
                    mActionMode = mode;
                    if (shouldAnimateActionModeView()) {
                        android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 0.0F);
                        mFadeAnim = android.support.v4.view.ViewCompat.animate(mActionModeView).alpha(1.0F);
                        mFadeAnim.setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                            @java.lang.Override
                            public void onAnimationStart(android.view.View view) {
                                mActionModeView.setVisibility(android.view.View.VISIBLE);
                                mActionModeView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                                if (mActionModeView.getParent() != null) {
                                    android.support.v4.view.ViewCompat.requestApplyInsets(((android.view.View) (mActionModeView.getParent())));
                                }
                            }

                            @java.lang.Override
                            public void onAnimationEnd(android.view.View view) {
                                android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 1.0F);
                                mFadeAnim.setListener(null);
                                mFadeAnim = null;
                            }
                        });
                    } else {
                        android.support.v4.view.ViewCompat.setAlpha(mActionModeView, 1.0F);
                        mActionModeView.setVisibility(android.view.View.VISIBLE);
                        mActionModeView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                        if (mActionModeView.getParent() != null) {
                            android.support.v4.view.ViewCompat.requestApplyInsets(((android.view.View) (mActionModeView.getParent())));
                        }
                    }
                    if (mActionModePopup != null) {
                        mWindow.getDecorView().post(mShowActionModePopup);
                    }
                } else {
                    mActionMode = null;
                }
            }
        }
        if ((mActionMode != null) && (mAppCompatCallback != null)) {
            mAppCompatCallback.onSupportActionModeStarted(mActionMode);
        }
        return mActionMode;
    }

    final boolean shouldAnimateActionModeView() {
        // We only to animate the action mode in if the sub decor has already been laid out.
        // If it hasn't been laid out, it hasn't been drawn to screen yet.
        return (mSubDecorInstalled && (mSubDecor != null)) && android.support.v4.view.ViewCompat.isLaidOut(mSubDecor);
    }

    private void endOnGoingFadeAnimation() {
        if (mFadeAnim != null) {
            mFadeAnim.cancel();
        }
    }

    boolean onBackPressed() {
        // Back cancels action modes first.
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        // Next collapse any expanded action views.
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if ((ab != null) && ab.collapseActionView()) {
            return true;
        }
        // Let the call through...
        return false;
    }

    @java.lang.Override
    boolean onKeyShortcut(int keyCode, android.view.KeyEvent ev) {
        // Let the Action Bar have a chance at handling the shortcut
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if ((ab != null) && ab.onKeyShortcut(keyCode, ev)) {
            return true;
        }
        // If the panel is already prepared, then perform the shortcut using it.
        boolean handled;
        if (mPreparedPanel != null) {
            handled = performPanelShortcut(mPreparedPanel, ev.getKeyCode(), ev, android.view.Menu.FLAG_PERFORM_NO_CLOSE);
            if (handled) {
                if (mPreparedPanel != null) {
                    mPreparedPanel.isHandled = true;
                }
                return true;
            }
        }
        // If the panel is not prepared, then we may be trying to handle a shortcut key
        // combination such as Control+C.  Temporarily prepare the panel then mark it
        // unprepared again when finished to ensure that the panel will again be prepared
        // the next time it is shown for real.
        if (mPreparedPanel == null) {
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, true);
            preparePanel(st, ev);
            handled = performPanelShortcut(st, ev.getKeyCode(), ev, android.view.Menu.FLAG_PERFORM_NO_CLOSE);
            st.isPrepared = false;
            if (handled) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_MENU) {
            // If this is a MENU event, let the Activity have a go.
            if (mOriginalWindowCallback.dispatchKeyEvent(event)) {
                return true;
            }
        }
        final int keyCode = event.getKeyCode();
        final int action = event.getAction();
        final boolean isDown = action == android.view.KeyEvent.ACTION_DOWN;
        return isDown ? onKeyDown(keyCode, event) : onKeyUp(keyCode, event);
    }

    boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_MENU :
                onKeyUpPanel(android.view.Window.FEATURE_OPTIONS_PANEL, event);
                return true;
            case android.view.KeyEvent.KEYCODE_BACK :
                final boolean wasLongPressBackDown = mLongPressBackDown;
                mLongPressBackDown = false;
                android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, false);
                if ((st != null) && st.isOpen) {
                    if (!wasLongPressBackDown) {
                        // Certain devices allow opening the options menu via a long press of the
                        // back button. We should only close the open options menu if it wasn't
                        // opened via a long press gesture.
                        closePanel(st, true);
                    }
                    return true;
                }
                if (onBackPressed()) {
                    return true;
                }
                break;
        }
        return false;
    }

    boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_MENU :
                onKeyDownPanel(android.view.Window.FEATURE_OPTIONS_PANEL, event);
                // We need to return true here and not let it bubble up to the Window.
                // For empty menus, PhoneWindow's KEYCODE_BACK handling will steals all events,
                // not allowing the Activity to call onBackPressed().
                return true;
            case android.view.KeyEvent.KEYCODE_BACK :
                // Certain devices allow opening the options menu via a long press of the back
                // button. We keep a record of whether the last event is from a long press.
                mLongPressBackDown = (event.getFlags() & android.view.KeyEvent.FLAG_LONG_PRESS) != 0;
                break;
        }
        // On API v7-10 we need to manually call onKeyShortcut() as this is not called
        // from the Activity
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            // We do not return true here otherwise dispatchKeyEvent will not reach the Activity
            // (which results in the back button not working)
            onKeyShortcut(keyCode, event);
        }
        return false;
    }

    @java.lang.Override
    public android.view.View createView(android.view.View parent, final java.lang.String name, @android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.util.AttributeSet attrs) {
        final boolean isPre21 = android.os.Build.VERSION.SDK_INT < 21;
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new android.support.v7.app.AppCompatViewInflater();
        }
        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext(((android.view.ViewParent) (parent)));
        return /* Only read android:theme pre-L (L+ handles this anyway) */
        /* Read read app:theme as a fallback at all times for legacy reasons */
        /* Only tint wrap the context if enabled */
        mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext, isPre21, true, android.support.v7.widget.VectorEnabledTintResources.shouldBeUsed());
    }

    private boolean shouldInheritContext(android.view.ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final android.view.View windowDecor = mWindow.getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else
                if (((parent == windowDecor) || (!(parent instanceof android.view.View))) || android.support.v4.view.ViewCompat.isAttachedToWindow(((android.view.View) (parent)))) {
                    // We have either hit the window's decor view, a parent which isn't a View
                    // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                    // is currently added to the view hierarchy. This means that it has not be
                    // inflated in the current inflate() call and we should not inherit the context.
                    return false;
                }

            parent = parent.getParent();
        } 
    }

    @java.lang.Override
    public void installViewFactory() {
        android.view.LayoutInflater layoutInflater = android.view.LayoutInflater.from(mContext);
        if (layoutInflater.getFactory() == null) {
            android.support.v4.view.LayoutInflaterCompat.setFactory(layoutInflater, this);
        } else {
            if (!(android.support.v4.view.LayoutInflaterCompat.getFactory(layoutInflater) instanceof android.support.v7.app.AppCompatDelegateImplV7)) {
                android.util.Log.i(android.support.v7.app.AppCompatDelegate.TAG, "The Activity's LayoutInflater already has a Factory installed" + " so we can not install AppCompat's");
            }
        }
    }

    /**
     * From {@link android.support.v4.view.LayoutInflaterFactory}
     */
    @java.lang.Override
    public final android.view.View onCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        // First let the Activity's Factory try and inflate the view
        final android.view.View view = callActivityOnCreateView(parent, name, context, attrs);
        if (view != null) {
            return view;
        }
        // If the Factory didn't handle it, let our createView() method try
        return createView(parent, name, context, attrs);
    }

    android.view.View callActivityOnCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        // Let the Activity's LayoutInflater.Factory try and handle it
        if (mOriginalWindowCallback instanceof android.view.LayoutInflater.Factory) {
            final android.view.View result = ((android.view.LayoutInflater.Factory) (mOriginalWindowCallback)).onCreateView(name, context, attrs);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private void openPanel(final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st, android.view.KeyEvent event) {
        // Already open, return
        if (st.isOpen || isDestroyed()) {
            return;
        }
        // Don't open an options panel for honeycomb apps on xlarge devices.
        // (The app should be using an action bar for menu items.)
        if (st.featureId == android.view.Window.FEATURE_OPTIONS_PANEL) {
            android.content.Context context = mContext;
            android.content.res.Configuration config = context.getResources().getConfiguration();
            boolean isXLarge = (config.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) == android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
            boolean isHoneycombApp = context.getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.HONEYCOMB;
            if (isXLarge && isHoneycombApp) {
                return;
            }
        }
        android.view.Window.Callback cb = getWindowCallback();
        if ((cb != null) && (!cb.onMenuOpened(st.featureId, st.menu))) {
            // Callback doesn't want the menu to open, reset any state
            closePanel(st, true);
            return;
        }
        final android.view.WindowManager wm = ((android.view.WindowManager) (mContext.getSystemService(android.content.Context.WINDOW_SERVICE)));
        if (wm == null) {
            return;
        }
        // Prepare panel (should have been done before, but just in case)
        if (!preparePanel(st, event)) {
            return;
        }
        int width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        if ((st.decorView == null) || st.refreshDecorView) {
            if (st.decorView == null) {
                // Initialize the panel decor, this will populate st.decorView
                if ((!initializePanelDecor(st)) || (st.decorView == null))
                    return;

            } else
                if (st.refreshDecorView && (st.decorView.getChildCount() > 0)) {
                    // Decor needs refreshing, so remove its views
                    st.decorView.removeAllViews();
                }

            // This will populate st.shownPanelView
            if ((!initializePanelContent(st)) || (!st.hasPanelItems())) {
                return;
            }
            android.view.ViewGroup.LayoutParams lp = st.shownPanelView.getLayoutParams();
            if (lp == null) {
                lp = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            int backgroundResId = st.background;
            st.decorView.setBackgroundResource(backgroundResId);
            android.view.ViewParent shownPanelParent = st.shownPanelView.getParent();
            if ((shownPanelParent != null) && (shownPanelParent instanceof android.view.ViewGroup)) {
                ((android.view.ViewGroup) (shownPanelParent)).removeView(st.shownPanelView);
            }
            st.decorView.addView(st.shownPanelView, lp);
            /* Give focus to the view, if it or one of its children does not
            already have it.
             */
            if (!st.shownPanelView.hasFocus()) {
                st.shownPanelView.requestFocus();
            }
        } else
            if (st.createdPanelView != null) {
                // If we already had a panel view, carry width=MATCH_PARENT through
                // as we did above when it was created.
                android.view.ViewGroup.LayoutParams lp = st.createdPanelView.getLayoutParams();
                if ((lp != null) && (lp.width == android.view.ViewGroup.LayoutParams.MATCH_PARENT)) {
                    width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }

        st.isHandled = false;
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, st.x, st.y, android.view.WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL, android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | android.view.WindowManager.LayoutParams.FLAG_SPLIT_TOUCH, android.graphics.PixelFormat.TRANSLUCENT);
        lp.gravity = st.gravity;
        lp.windowAnimations = st.windowAnimations;
        wm.addView(st.decorView, lp);
        st.isOpen = true;
    }

    private boolean initializePanelDecor(android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st) {
        st.setStyle(getActionBarThemedContext());
        st.decorView = new android.support.v7.app.AppCompatDelegateImplV7.ListMenuDecorView(st.listPresenterContext);
        st.gravity = android.view.Gravity.CENTER | android.view.Gravity.BOTTOM;
        return true;
    }

    private void reopenMenu(android.support.v7.view.menu.MenuBuilder menu, boolean toggleMenuMode) {
        if (((mDecorContentParent != null) && mDecorContentParent.canShowOverflowMenu()) && ((!android.support.v4.view.ViewConfigurationCompat.hasPermanentMenuKey(android.view.ViewConfiguration.get(mContext))) || mDecorContentParent.isOverflowMenuShowPending())) {
            final android.view.Window.Callback cb = getWindowCallback();
            if ((!mDecorContentParent.isOverflowMenuShowing()) || (!toggleMenuMode)) {
                if ((cb != null) && (!isDestroyed())) {
                    // If we have a menu invalidation pending, do it now.
                    if (mInvalidatePanelMenuPosted && ((mInvalidatePanelMenuFeatures & (1 << android.view.Window.FEATURE_OPTIONS_PANEL)) != 0)) {
                        mWindow.getDecorView().removeCallbacks(mInvalidatePanelMenuRunnable);
                        mInvalidatePanelMenuRunnable.run();
                    }
                    final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, true);
                    // If we don't have a menu or we're waiting for a full content refresh,
                    // forget it. This is a lingering event that no longer matters.
                    if (((st.menu != null) && (!st.refreshMenuContent)) && cb.onPreparePanel(android.view.Window.FEATURE_OPTIONS_PANEL, st.createdPanelView, st.menu)) {
                        cb.onMenuOpened(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, st.menu);
                        mDecorContentParent.showOverflowMenu();
                    }
                }
            } else {
                mDecorContentParent.hideOverflowMenu();
                if (!isDestroyed()) {
                    final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, true);
                    cb.onPanelClosed(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, st.menu);
                }
            }
            return;
        }
        android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, true);
        st.refreshDecorView = true;
        closePanel(st, false);
        openPanel(st, null);
    }

    private boolean initializePanelMenu(final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st) {
        android.content.Context context = mContext;
        // If we have an action bar, initialize the menu with the right theme.
        if (((st.featureId == android.view.Window.FEATURE_OPTIONS_PANEL) || (st.featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR)) && (mDecorContentParent != null)) {
            final android.util.TypedValue outValue = new android.util.TypedValue();
            final android.content.res.Resources.Theme baseTheme = context.getTheme();
            baseTheme.resolveAttribute(R.attr.actionBarTheme, outValue, true);
            android.content.res.Resources.Theme widgetTheme = null;
            if (outValue.resourceId != 0) {
                widgetTheme = context.getResources().newTheme();
                widgetTheme.setTo(baseTheme);
                widgetTheme.applyStyle(outValue.resourceId, true);
                widgetTheme.resolveAttribute(R.attr.actionBarWidgetTheme, outValue, true);
            } else {
                baseTheme.resolveAttribute(R.attr.actionBarWidgetTheme, outValue, true);
            }
            if (outValue.resourceId != 0) {
                if (widgetTheme == null) {
                    widgetTheme = context.getResources().newTheme();
                    widgetTheme.setTo(baseTheme);
                }
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            if (widgetTheme != null) {
                context = new android.support.v7.view.ContextThemeWrapper(context, 0);
                context.getTheme().setTo(widgetTheme);
            }
        }
        final android.support.v7.view.menu.MenuBuilder menu = new android.support.v7.view.menu.MenuBuilder(context);
        menu.setCallback(this);
        st.setMenu(menu);
        return true;
    }

    private boolean initializePanelContent(android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st) {
        if (st.createdPanelView != null) {
            st.shownPanelView = st.createdPanelView;
            return true;
        }
        if (st.menu == null) {
            return false;
        }
        if (mPanelMenuPresenterCallback == null) {
            mPanelMenuPresenterCallback = new android.support.v7.app.AppCompatDelegateImplV7.PanelMenuPresenterCallback();
        }
        android.support.v7.view.menu.MenuView menuView = st.getListMenuView(mPanelMenuPresenterCallback);
        st.shownPanelView = ((android.view.View) (menuView));
        return st.shownPanelView != null;
    }

    private boolean preparePanel(android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st, android.view.KeyEvent event) {
        if (isDestroyed()) {
            return false;
        }
        // Already prepared (isPrepared will be reset to false later)
        if (st.isPrepared) {
            return true;
        }
        if ((mPreparedPanel != null) && (mPreparedPanel != st)) {
            // Another Panel is prepared and possibly open, so close it
            closePanel(mPreparedPanel, false);
        }
        final android.view.Window.Callback cb = getWindowCallback();
        if (cb != null) {
            st.createdPanelView = cb.onCreatePanelView(st.featureId);
        }
        final boolean isActionBarMenu = (st.featureId == android.view.Window.FEATURE_OPTIONS_PANEL) || (st.featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR);
        if (isActionBarMenu && (mDecorContentParent != null)) {
            // Enforce ordering guarantees around events so that the action bar never
            // dispatches menu-related events before the panel is prepared.
            mDecorContentParent.setMenuPrepared();
        }
        if ((st.createdPanelView == null) && ((!isActionBarMenu) || (!(peekSupportActionBar() instanceof android.support.v7.app.ToolbarActionBar)))) {
            // Since ToolbarActionBar handles the list options menu itself, we only want to
            // init this menu panel if we're not using a TAB.
            if ((st.menu == null) || st.refreshMenuContent) {
                if (st.menu == null) {
                    if ((!initializePanelMenu(st)) || (st.menu == null)) {
                        return false;
                    }
                }
                if (isActionBarMenu && (mDecorContentParent != null)) {
                    if (mActionMenuPresenterCallback == null) {
                        mActionMenuPresenterCallback = new android.support.v7.app.AppCompatDelegateImplV7.ActionMenuPresenterCallback();
                    }
                    mDecorContentParent.setMenu(st.menu, mActionMenuPresenterCallback);
                }
                // Creating the panel menu will involve a lot of manipulation;
                // don't dispatch change events to presenters until we're done.
                st.menu.stopDispatchingItemsChanged();
                if (!cb.onCreatePanelMenu(st.featureId, st.menu)) {
                    // Ditch the menu created above
                    st.setMenu(null);
                    if (isActionBarMenu && (mDecorContentParent != null)) {
                        // Don't show it in the action bar either
                        mDecorContentParent.setMenu(null, mActionMenuPresenterCallback);
                    }
                    return false;
                }
                st.refreshMenuContent = false;
            }
            // Preparing the panel menu can involve a lot of manipulation;
            // don't dispatch change events to presenters until we're done.
            st.menu.stopDispatchingItemsChanged();
            // Restore action view state before we prepare. This gives apps
            // an opportunity to override frozen/restored state in onPrepare.
            if (st.frozenActionViewState != null) {
                st.menu.restoreActionViewStates(st.frozenActionViewState);
                st.frozenActionViewState = null;
            }
            // Callback and return if the callback does not want to show the menu
            if (!cb.onPreparePanel(android.view.Window.FEATURE_OPTIONS_PANEL, st.createdPanelView, st.menu)) {
                if (isActionBarMenu && (mDecorContentParent != null)) {
                    // The app didn't want to show the menu for now but it still exists.
                    // Clear it out of the action bar.
                    mDecorContentParent.setMenu(null, mActionMenuPresenterCallback);
                }
                st.menu.startDispatchingItemsChanged();
                return false;
            }
            // Set the proper keymap
            android.view.KeyCharacterMap kmap = android.view.KeyCharacterMap.load(event != null ? event.getDeviceId() : android.view.KeyCharacterMap.VIRTUAL_KEYBOARD);
            st.qwertyMode = kmap.getKeyboardType() != android.view.KeyCharacterMap.NUMERIC;
            st.menu.setQwertyMode(st.qwertyMode);
            st.menu.startDispatchingItemsChanged();
        }
        // Set other state
        st.isPrepared = true;
        st.isHandled = false;
        mPreparedPanel = st;
        return true;
    }

    private void checkCloseActionMenu(android.support.v7.view.menu.MenuBuilder menu) {
        if (mClosingActionMenu) {
            return;
        }
        mClosingActionMenu = true;
        mDecorContentParent.dismissPopups();
        android.view.Window.Callback cb = getWindowCallback();
        if ((cb != null) && (!isDestroyed())) {
            cb.onPanelClosed(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, menu);
        }
        mClosingActionMenu = false;
    }

    private void closePanel(int featureId) {
        closePanel(getPanelState(featureId, true), true);
    }

    private void closePanel(android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st, boolean doCallback) {
        if (((doCallback && (st.featureId == android.view.Window.FEATURE_OPTIONS_PANEL)) && (mDecorContentParent != null)) && mDecorContentParent.isOverflowMenuShowing()) {
            checkCloseActionMenu(st.menu);
            return;
        }
        final android.view.WindowManager wm = ((android.view.WindowManager) (mContext.getSystemService(android.content.Context.WINDOW_SERVICE)));
        if (((wm != null) && st.isOpen) && (st.decorView != null)) {
            wm.removeView(st.decorView);
            if (doCallback) {
                callOnPanelClosed(st.featureId, st, null);
            }
        }
        st.isPrepared = false;
        st.isHandled = false;
        st.isOpen = false;
        // This view is no longer shown, so null it out
        st.shownPanelView = null;
        // Next time the menu opens, it should not be in expanded mode, so
        // force a refresh of the decor
        st.refreshDecorView = true;
        if (mPreparedPanel == st) {
            mPreparedPanel = null;
        }
    }

    private boolean onKeyDownPanel(int featureId, android.view.KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(featureId, true);
            if (!st.isOpen) {
                return preparePanel(st, event);
            }
        }
        return false;
    }

    private boolean onKeyUpPanel(int featureId, android.view.KeyEvent event) {
        if (mActionMode != null) {
            return false;
        }
        boolean handled = false;
        final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(featureId, true);
        if ((((featureId == android.view.Window.FEATURE_OPTIONS_PANEL) && (mDecorContentParent != null)) && mDecorContentParent.canShowOverflowMenu()) && (!android.support.v4.view.ViewConfigurationCompat.hasPermanentMenuKey(android.view.ViewConfiguration.get(mContext)))) {
            if (!mDecorContentParent.isOverflowMenuShowing()) {
                if ((!isDestroyed()) && preparePanel(st, event)) {
                    handled = mDecorContentParent.showOverflowMenu();
                }
            } else {
                handled = mDecorContentParent.hideOverflowMenu();
            }
        } else {
            if (st.isOpen || st.isHandled) {
                // Play the sound effect if the user closed an open menu (and not if
                // they just released a menu shortcut)
                handled = st.isOpen;
                // Close menu
                closePanel(st, true);
            } else
                if (st.isPrepared) {
                    boolean show = true;
                    if (st.refreshMenuContent) {
                        // Something may have invalidated the menu since we prepared it.
                        // Re-prepare it to refresh.
                        st.isPrepared = false;
                        show = preparePanel(st, event);
                    }
                    if (show) {
                        // Show menu
                        openPanel(st, event);
                        handled = true;
                    }
                }

        }
        if (handled) {
            android.media.AudioManager audioManager = ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE)));
            if (audioManager != null) {
                audioManager.playSoundEffect(android.media.AudioManager.FX_KEY_CLICK);
            } else {
                android.util.Log.w(android.support.v7.app.AppCompatDelegate.TAG, "Couldn't get audio manager");
            }
        }
        return handled;
    }

    private void callOnPanelClosed(int featureId, android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState panel, android.view.Menu menu) {
        // Try to get a menu
        if (menu == null) {
            // Need a panel to grab the menu, so try to get that
            if (panel == null) {
                if ((featureId >= 0) && (featureId < mPanels.length)) {
                    panel = mPanels[featureId];
                }
            }
            if (panel != null) {
                // menu still may be null, which is okay--we tried our best
                menu = panel.menu;
            }
        }
        // If the panel is not open, do not callback
        if ((panel != null) && (!panel.isOpen))
            return;

        if (!isDestroyed()) {
            // We need to be careful which callback we dispatch the call to. We can not dispatch
            // this to the Window's callback since that will call back into this method and cause a
            // crash. Instead we need to dispatch down to the original Activity/Dialog/etc.
            mOriginalWindowCallback.onPanelClosed(featureId, menu);
        }
    }

    private android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState findMenuPanel(android.view.Menu menu) {
        final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState[] panels = mPanels;
        final int N = (panels != null) ? panels.length : 0;
        for (int i = 0; i < N; i++) {
            final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState panel = panels[i];
            if ((panel != null) && (panel.menu == menu)) {
                return panel;
            }
        }
        return null;
    }

    protected android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState getPanelState(int featureId, boolean required) {
        android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState[] ar;
        if (((ar = mPanels) == null) || (ar.length <= featureId)) {
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState[] nar = new android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState[featureId + 1];
            if (ar != null) {
                java.lang.System.arraycopy(ar, 0, nar, 0, ar.length);
            }
            mPanels = ar = nar;
        }
        android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = ar[featureId];
        if (st == null) {
            ar[featureId] = st = new android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState(featureId);
        }
        return st;
    }

    private boolean performPanelShortcut(android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st, int keyCode, android.view.KeyEvent event, int flags) {
        if (event.isSystem()) {
            return false;
        }
        boolean handled = false;
        // Only try to perform menu shortcuts if preparePanel returned true (possible false
        // return value from application not wanting to show the menu).
        if ((st.isPrepared || preparePanel(st, event)) && (st.menu != null)) {
            // The menu is prepared now, perform the shortcut on it
            handled = st.menu.performShortcut(keyCode, event, flags);
        }
        if (handled) {
            // Only close down the menu if we don't have an action bar keeping it open.
            if (((flags & android.view.Menu.FLAG_PERFORM_NO_CLOSE) == 0) && (mDecorContentParent == null)) {
                closePanel(st, true);
            }
        }
        return handled;
    }

    private void invalidatePanelMenu(int featureId) {
        mInvalidatePanelMenuFeatures |= 1 << featureId;
        if (!mInvalidatePanelMenuPosted) {
            android.support.v4.view.ViewCompat.postOnAnimation(mWindow.getDecorView(), mInvalidatePanelMenuRunnable);
            mInvalidatePanelMenuPosted = true;
        }
    }

    private void doInvalidatePanelMenu(int featureId) {
        android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(featureId, true);
        android.os.Bundle savedActionViewStates = null;
        if (st.menu != null) {
            savedActionViewStates = new android.os.Bundle();
            st.menu.saveActionViewStates(savedActionViewStates);
            if (savedActionViewStates.size() > 0) {
                st.frozenActionViewState = savedActionViewStates;
            }
            // This will be started again when the panel is prepared.
            st.menu.stopDispatchingItemsChanged();
            st.menu.clear();
        }
        st.refreshMenuContent = true;
        st.refreshDecorView = true;
        // Prepare the options panel if we have an action bar
        if (((featureId == android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR) || (featureId == android.view.Window.FEATURE_OPTIONS_PANEL)) && (mDecorContentParent != null)) {
            st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, false);
            if (st != null) {
                st.isPrepared = false;
                preparePanel(st, null);
            }
        }
    }

    /**
     * Updates the status bar guard
     *
     * @param insetTop
     * 		the current top system window inset
     * @return the new top system window inset
     */
    private int updateStatusGuard(int insetTop) {
        boolean showStatusGuard = false;
        // Show the status guard when the non-overlay contextual action bar is showing
        if (mActionModeView != null) {
            if (mActionModeView.getLayoutParams() instanceof android.view.ViewGroup.MarginLayoutParams) {
                android.view.ViewGroup.MarginLayoutParams mlp = ((android.view.ViewGroup.MarginLayoutParams) (mActionModeView.getLayoutParams()));
                boolean mlpChanged = false;
                if (mActionModeView.isShown()) {
                    if (mTempRect1 == null) {
                        mTempRect1 = new android.graphics.Rect();
                        mTempRect2 = new android.graphics.Rect();
                    }
                    final android.graphics.Rect insets = mTempRect1;
                    final android.graphics.Rect localInsets = mTempRect2;
                    insets.set(0, insetTop, 0, 0);
                    android.support.v7.widget.ViewUtils.computeFitSystemWindows(mSubDecor, insets, localInsets);
                    final int newMargin = (localInsets.top == 0) ? insetTop : 0;
                    if (mlp.topMargin != newMargin) {
                        mlpChanged = true;
                        mlp.topMargin = insetTop;
                        if (mStatusGuard == null) {
                            mStatusGuard = new android.view.View(mContext);
                            mStatusGuard.setBackgroundColor(mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                            mSubDecor.addView(mStatusGuard, -1, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, insetTop));
                        } else {
                            android.view.ViewGroup.LayoutParams lp = mStatusGuard.getLayoutParams();
                            if (lp.height != insetTop) {
                                lp.height = insetTop;
                                mStatusGuard.setLayoutParams(lp);
                            }
                        }
                    }
                    // The action mode's theme may differ from the app, so
                    // always show the status guard above it.
                    showStatusGuard = mStatusGuard != null;
                    // We only need to consume the insets if the action
                    // mode is overlaid on the app content (e.g. it's
                    // sitting in a FrameLayout, see
                    // screen_simple_overlay_action_mode.xml).
                    if ((!mOverlayActionMode) && showStatusGuard) {
                        insetTop = 0;
                    }
                } else {
                    // reset top margin
                    if (mlp.topMargin != 0) {
                        mlpChanged = true;
                        mlp.topMargin = 0;
                    }
                }
                if (mlpChanged) {
                    mActionModeView.setLayoutParams(mlp);
                }
            }
        }
        if (mStatusGuard != null) {
            mStatusGuard.setVisibility(showStatusGuard ? android.view.View.VISIBLE : android.view.View.GONE);
        }
        return insetTop;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (mSubDecorInstalled) {
            throw new android.util.AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private int sanitizeWindowFeatureId(int featureId) {
        if (featureId == android.support.v4.view.WindowCompat.FEATURE_ACTION_BAR) {
            android.util.Log.i(android.support.v7.app.AppCompatDelegate.TAG, "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR" + " id when requesting this feature.");
            return android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR;
        } else
            if (featureId == android.support.v4.view.WindowCompat.FEATURE_ACTION_BAR_OVERLAY) {
                android.util.Log.i(android.support.v7.app.AppCompatDelegate.TAG, "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY" + " id when requesting this feature.");
                return android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY;
            }

        // Else we'll just return the original id
        return featureId;
    }

    android.view.ViewGroup getSubDecor() {
        return mSubDecor;
    }

    private void dismissPopups() {
        if (mDecorContentParent != null) {
            mDecorContentParent.dismissPopups();
        }
        if (mActionModePopup != null) {
            mWindow.getDecorView().removeCallbacks(mShowActionModePopup);
            if (mActionModePopup.isShowing()) {
                try {
                    mActionModePopup.dismiss();
                } catch (java.lang.IllegalArgumentException e) {
                    // Pre-v18, there are times when the Window will remove the popup before us.
                    // In these cases we need to swallow the resulting exception.
                }
            }
            mActionModePopup = null;
        }
        endOnGoingFadeAnimation();
        android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState st = getPanelState(android.view.Window.FEATURE_OPTIONS_PANEL, false);
        if ((st != null) && (st.menu != null)) {
            st.menu.close();
        }
    }

    /**
     * Clears out internal reference when the action mode is destroyed.
     */
    class ActionModeCallbackWrapperV7 implements android.support.v7.view.ActionMode.Callback {
        private android.support.v7.view.ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapperV7(android.support.v7.view.ActionMode.Callback wrapped) {
            mWrapped = wrapped;
        }

        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, android.view.Menu menu) {
            return mWrapped.onCreateActionMode(mode, menu);
        }

        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, android.view.Menu menu) {
            return mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, android.view.MenuItem item) {
            return mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
            mWrapped.onDestroyActionMode(mode);
            if (mActionModePopup != null) {
                mWindow.getDecorView().removeCallbacks(mShowActionModePopup);
            }
            if (mActionModeView != null) {
                endOnGoingFadeAnimation();
                mFadeAnim = android.support.v4.view.ViewCompat.animate(mActionModeView).alpha(0.0F);
                mFadeAnim.setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.view.View view) {
                        mActionModeView.setVisibility(android.view.View.GONE);
                        if (mActionModePopup != null) {
                            mActionModePopup.dismiss();
                        } else
                            if (mActionModeView.getParent() instanceof android.view.View) {
                                android.support.v4.view.ViewCompat.requestApplyInsets(((android.view.View) (mActionModeView.getParent())));
                            }

                        mActionModeView.removeAllViews();
                        mFadeAnim.setListener(null);
                        mFadeAnim = null;
                    }
                });
            }
            if (mAppCompatCallback != null) {
                mAppCompatCallback.onSupportActionModeFinished(mActionMode);
            }
            mActionMode = null;
        }
    }

    private final class PanelMenuPresenterCallback implements android.support.v7.view.menu.MenuPresenter.Callback {
        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            final android.view.Menu parentMenu = menu.getRootMenu();
            final boolean isSubMenu = parentMenu != menu;
            final android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState panel = findMenuPanel(isSubMenu ? parentMenu : menu);
            if (panel != null) {
                if (isSubMenu) {
                    callOnPanelClosed(panel.featureId, panel, parentMenu);
                    closePanel(panel, true);
                } else {
                    // Close the panel and only do the callback if the menu is being
                    // closed completely, not if opening a sub menu
                    closePanel(panel, allMenusAreClosing);
                }
            }
        }

        @java.lang.Override
        public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
            if ((subMenu == null) && mHasActionBar) {
                android.view.Window.Callback cb = getWindowCallback();
                if ((cb != null) && (!isDestroyed())) {
                    cb.onMenuOpened(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, subMenu);
                }
            }
            return true;
        }
    }

    private final class ActionMenuPresenterCallback implements android.support.v7.view.menu.MenuPresenter.Callback {
        @java.lang.Override
        public boolean onOpenSubMenu(android.support.v7.view.menu.MenuBuilder subMenu) {
            android.view.Window.Callback cb = getWindowCallback();
            if (cb != null) {
                cb.onMenuOpened(android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR, subMenu);
            }
            return true;
        }

        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
            checkCloseActionMenu(menu);
        }
    }

    protected static final class PanelFeatureState {
        /**
         * Feature ID for this panel.
         */
        int featureId;

        int background;

        int gravity;

        int x;

        int y;

        int windowAnimations;

        /**
         * Dynamic state of the panel.
         */
        android.view.ViewGroup decorView;

        /**
         * The panel that we are actually showing.
         */
        android.view.View shownPanelView;

        /**
         * The panel that was returned by onCreatePanelView().
         */
        android.view.View createdPanelView;

        /**
         * Use {@link #setMenu} to set this.
         */
        android.support.v7.view.menu.MenuBuilder menu;

        android.support.v7.view.menu.ListMenuPresenter listMenuPresenter;

        android.content.Context listPresenterContext;

        /**
         * Whether the panel has been prepared (see
         * {@link #preparePanel}).
         */
        boolean isPrepared;

        /**
         * Whether an item's action has been performed. This happens in obvious
         * scenarios (user clicks on menu item), but can also happen with
         * chording menu+(shortcut key).
         */
        boolean isHandled;

        boolean isOpen;

        public boolean qwertyMode;

        boolean refreshDecorView;

        boolean refreshMenuContent;

        boolean wasLastOpen;

        /**
         * Contains the state of the menu when told to freeze.
         */
        android.os.Bundle frozenMenuState;

        /**
         * Contains the state of associated action views when told to freeze.
         * These are saved across invalidations.
         */
        android.os.Bundle frozenActionViewState;

        PanelFeatureState(int featureId) {
            this.featureId = featureId;
            refreshDecorView = false;
        }

        public boolean hasPanelItems() {
            if (shownPanelView == null)
                return false;

            if (createdPanelView != null)
                return true;

            return listMenuPresenter.getAdapter().getCount() > 0;
        }

        /**
         * Unregister and free attached MenuPresenters. They will be recreated as needed.
         */
        public void clearMenuPresenters() {
            if (menu != null) {
                menu.removeMenuPresenter(listMenuPresenter);
            }
            listMenuPresenter = null;
        }

        void setStyle(android.content.Context context) {
            final android.util.TypedValue outValue = new android.util.TypedValue();
            final android.content.res.Resources.Theme widgetTheme = context.getResources().newTheme();
            widgetTheme.setTo(context.getTheme());
            // First apply the actionBarPopupTheme
            widgetTheme.resolveAttribute(R.attr.actionBarPopupTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            // Now apply the panelMenuListTheme
            widgetTheme.resolveAttribute(R.attr.panelMenuListTheme, outValue, true);
            if (outValue.resourceId != 0) {
                widgetTheme.applyStyle(outValue.resourceId, true);
            } else {
                widgetTheme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            context = new android.support.v7.view.ContextThemeWrapper(context, 0);
            context.getTheme().setTo(widgetTheme);
            listPresenterContext = context;
            android.content.res.TypedArray a = context.obtainStyledAttributes(R.styleable.AppCompatTheme);
            background = a.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
            windowAnimations = a.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            a.recycle();
        }

        void setMenu(android.support.v7.view.menu.MenuBuilder menu) {
            if (menu == this.menu)
                return;

            if (this.menu != null) {
                this.menu.removeMenuPresenter(listMenuPresenter);
            }
            this.menu = menu;
            if (menu != null) {
                if (listMenuPresenter != null)
                    menu.addMenuPresenter(listMenuPresenter);

            }
        }

        android.support.v7.view.menu.MenuView getListMenuView(android.support.v7.view.menu.MenuPresenter.Callback cb) {
            if (menu == null)
                return null;

            if (listMenuPresenter == null) {
                listMenuPresenter = new android.support.v7.view.menu.ListMenuPresenter(listPresenterContext, R.layout.abc_list_menu_item_layout);
                listMenuPresenter.setCallback(cb);
                menu.addMenuPresenter(listMenuPresenter);
            }
            android.support.v7.view.menu.MenuView result = listMenuPresenter.getMenuView(decorView);
            return result;
        }

        android.os.Parcelable onSaveInstanceState() {
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState savedState = new android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState();
            savedState.featureId = featureId;
            savedState.isOpen = isOpen;
            if (menu != null) {
                savedState.menuState = new android.os.Bundle();
                menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void onRestoreInstanceState(android.os.Parcelable state) {
            android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState savedState = ((android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState) (state));
            featureId = savedState.featureId;
            wasLastOpen = savedState.isOpen;
            frozenMenuState = savedState.menuState;
            shownPanelView = null;
            decorView = null;
        }

        void applyFrozenState() {
            if ((menu != null) && (frozenMenuState != null)) {
                menu.restorePresenterStates(frozenMenuState);
                frozenMenuState = null;
            }
        }

        private static class SavedState implements android.os.Parcelable {
            int featureId;

            boolean isOpen;

            android.os.Bundle menuState;

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(android.os.Parcel dest, int flags) {
                dest.writeInt(featureId);
                dest.writeInt(isOpen ? 1 : 0);
                if (isOpen) {
                    dest.writeBundle(menuState);
                }
            }

            private static android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState readFromParcel(android.os.Parcel source, java.lang.ClassLoader loader) {
                android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState savedState = new android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState();
                savedState.featureId = source.readInt();
                savedState.isOpen = source.readInt() == 1;
                if (savedState.isOpen) {
                    savedState.menuState = source.readBundle(loader);
                }
                return savedState;
            }

            public static final android.os.Parcelable.Creator<android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState>() {
                @java.lang.Override
                public android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                    return android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState.readFromParcel(in, loader);
                }

                @java.lang.Override
                public android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState[] newArray(int size) {
                    return new android.support.v7.app.AppCompatDelegateImplV7.PanelFeatureState.SavedState[size];
                }
            });
        }
    }

    private class ListMenuDecorView extends android.support.v7.widget.ContentFrameLayout {
        public ListMenuDecorView(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        public boolean dispatchKeyEvent(android.view.KeyEvent event) {
            return android.support.v7.app.AppCompatDelegateImplV7.this.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
        }

        @java.lang.Override
        public boolean onInterceptTouchEvent(android.view.MotionEvent event) {
            int action = event.getAction();
            if (action == android.view.MotionEvent.ACTION_DOWN) {
                int x = ((int) (event.getX()));
                int y = ((int) (event.getY()));
                if (isOutOfBounds(x, y)) {
                    closePanel(android.view.Window.FEATURE_OPTIONS_PANEL);
                    return true;
                }
            }
            return super.onInterceptTouchEvent(event);
        }

        @java.lang.Override
        public void setBackgroundResource(int resid) {
            setBackgroundDrawable(android.support.v7.widget.AppCompatDrawableManager.get().getDrawable(getContext(), resid));
        }

        private boolean isOutOfBounds(int x, int y) {
            return (((x < (-5)) || (y < (-5))) || (x > (getWidth() + 5))) || (y > (getHeight() + 5));
        }
    }
}

