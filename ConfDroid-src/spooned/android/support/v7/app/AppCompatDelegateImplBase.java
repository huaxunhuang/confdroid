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


abstract class AppCompatDelegateImplBase extends android.support.v7.app.AppCompatDelegate {
    static final boolean DEBUG = false;

    private static boolean sInstalledExceptionHandler;

    private static final boolean SHOULD_INSTALL_EXCEPTION_HANDLER = android.os.Build.VERSION.SDK_INT < 21;

    static final java.lang.String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are" + (" trying to use is a vector resource, you may be referencing it in an unsupported" + " way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");

    static {
        if (android.support.v7.app.AppCompatDelegateImplBase.SHOULD_INSTALL_EXCEPTION_HANDLER && (!android.support.v7.app.AppCompatDelegateImplBase.sInstalledExceptionHandler)) {
            final java.lang.Thread.UncaughtExceptionHandler defHandler = java.lang.Thread.getDefaultUncaughtExceptionHandler();
            java.lang.Thread.setDefaultUncaughtExceptionHandler(new java.lang.Thread.UncaughtExceptionHandler() {
                @java.lang.Override
                public void uncaughtException(java.lang.Thread thread, final java.lang.Throwable thowable) {
                    if (shouldWrapException(thowable)) {
                        // Now wrap the throwable, but append some extra information to the message
                        final java.lang.Throwable wrapped = new android.content.res.Resources.NotFoundException(thowable.getMessage() + android.support.v7.app.AppCompatDelegateImplBase.EXCEPTION_HANDLER_MESSAGE_SUFFIX);
                        wrapped.initCause(thowable.getCause());
                        wrapped.setStackTrace(thowable.getStackTrace());
                        defHandler.uncaughtException(thread, wrapped);
                    } else {
                        defHandler.uncaughtException(thread, thowable);
                    }
                }

                private boolean shouldWrapException(java.lang.Throwable throwable) {
                    if (throwable instanceof android.content.res.Resources.NotFoundException) {
                        final java.lang.String message = throwable.getMessage();
                        return (message != null) && (message.contains("drawable") || message.contains("Drawable"));
                    }
                    return false;
                }
            });
            android.support.v7.app.AppCompatDelegateImplBase.sInstalledExceptionHandler = true;
        }
    }

    private static final int[] sWindowBackgroundStyleable = new int[]{ android.R.attr.windowBackground };

    final android.content.Context mContext;

    final android.view.Window mWindow;

    final android.view.Window.Callback mOriginalWindowCallback;

    final android.view.Window.Callback mAppCompatWindowCallback;

    final android.support.v7.app.AppCompatCallback mAppCompatCallback;

    android.support.v7.app.ActionBar mActionBar;

    android.view.MenuInflater mMenuInflater;

    // true if this activity has an action bar.
    boolean mHasActionBar;

    // true if this activity's action bar overlays other activity content.
    boolean mOverlayActionBar;

    // true if this any action modes should overlay the activity content
    boolean mOverlayActionMode;

    // true if this activity is floating (e.g. Dialog)
    boolean mIsFloating;

    // true if this activity has no title
    boolean mWindowNoTitle;

    private java.lang.CharSequence mTitle;

    private boolean mIsStarted;

    private boolean mIsDestroyed;

    AppCompatDelegateImplBase(android.content.Context context, android.view.Window window, android.support.v7.app.AppCompatCallback callback) {
        mContext = context;
        mWindow = window;
        mAppCompatCallback = callback;
        mOriginalWindowCallback = mWindow.getCallback();
        if (mOriginalWindowCallback instanceof android.support.v7.app.AppCompatDelegateImplBase.AppCompatWindowCallbackBase) {
            throw new java.lang.IllegalStateException("AppCompat has already installed itself into the Window");
        }
        mAppCompatWindowCallback = wrapWindowCallback(mOriginalWindowCallback);
        // Now install the new callback
        mWindow.setCallback(mAppCompatWindowCallback);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, null, android.support.v7.app.AppCompatDelegateImplBase.sWindowBackgroundStyleable);
        final android.graphics.drawable.Drawable winBg = a.getDrawableIfKnown(0);
        if (winBg != null) {
            mWindow.setBackgroundDrawable(winBg);
        }
        a.recycle();
    }

    abstract void initWindowDecorActionBar();

    android.view.Window.Callback wrapWindowCallback(android.view.Window.Callback callback) {
        return new android.support.v7.app.AppCompatDelegateImplBase.AppCompatWindowCallbackBase(callback);
    }

    @java.lang.Override
    public android.support.v7.app.ActionBar getSupportActionBar() {
        // The Action Bar should be lazily created as hasActionBar
        // could change after onCreate
        initWindowDecorActionBar();
        return mActionBar;
    }

    final android.support.v7.app.ActionBar peekSupportActionBar() {
        return mActionBar;
    }

    @java.lang.Override
    public android.view.MenuInflater getMenuInflater() {
        // Make sure that action views can get an appropriate theme.
        if (mMenuInflater == null) {
            initWindowDecorActionBar();
            mMenuInflater = new android.support.v7.view.SupportMenuInflater(mActionBar != null ? mActionBar.getThemedContext() : mContext);
        }
        return mMenuInflater;
    }

    // Methods used to create and respond to options menu
    abstract void onPanelClosed(int featureId, android.view.Menu menu);

    abstract boolean onMenuOpened(int featureId, android.view.Menu menu);

    abstract boolean dispatchKeyEvent(android.view.KeyEvent event);

    abstract boolean onKeyShortcut(int keyCode, android.view.KeyEvent event);

    @java.lang.Override
    public void setLocalNightMode(@android.support.v7.app.AppCompatDelegate.NightMode
    int mode) {
        // no-op
    }

    @java.lang.Override
    public final android.support.v7.app.ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new android.support.v7.app.AppCompatDelegateImplBase.ActionBarDrawableToggleImpl();
    }

    final android.content.Context getActionBarThemedContext() {
        android.content.Context context = null;
        // If we have an action bar, let it return a themed context
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            context = ab.getThemedContext();
        }
        if (context == null) {
            context = mContext;
        }
        return context;
    }

    private class ActionBarDrawableToggleImpl implements android.support.v7.app.ActionBarDrawerToggle.Delegate {
        ActionBarDrawableToggleImpl() {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getThemeUpIndicator() {
            final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), null, new int[]{ R.attr.homeAsUpIndicator });
            final android.graphics.drawable.Drawable result = a.getDrawable(0);
            a.recycle();
            return result;
        }

        @java.lang.Override
        public android.content.Context getActionBarThemedContext() {
            return android.support.v7.app.AppCompatDelegateImplBase.this.getActionBarThemedContext();
        }

        @java.lang.Override
        public boolean isNavigationVisible() {
            final android.support.v7.app.ActionBar ab = getSupportActionBar();
            return (ab != null) && ((ab.getDisplayOptions() & android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP) != 0);
        }

        @java.lang.Override
        public void setActionBarUpIndicator(android.graphics.drawable.Drawable upDrawable, int contentDescRes) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(upDrawable);
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }

        @java.lang.Override
        public void setActionBarDescription(int contentDescRes) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setHomeActionContentDescription(contentDescRes);
            }
        }
    }

    abstract android.support.v7.view.ActionMode startSupportActionModeFromWindow(android.support.v7.view.ActionMode.Callback callback);

    @java.lang.Override
    public void onStart() {
        mIsStarted = true;
    }

    @java.lang.Override
    public void onStop() {
        mIsStarted = false;
    }

    @java.lang.Override
    public void onDestroy() {
        mIsDestroyed = true;
    }

    @java.lang.Override
    public void setHandleNativeActionModesEnabled(boolean enabled) {
        // no-op pre-v14
    }

    @java.lang.Override
    public boolean isHandleNativeActionModesEnabled() {
        // Always false pre-v14
        return false;
    }

    @java.lang.Override
    public boolean applyDayNight() {
        // no-op on v7
        return false;
    }

    final boolean isDestroyed() {
        return mIsDestroyed;
    }

    final boolean isStarted() {
        return mIsStarted;
    }

    final android.view.Window.Callback getWindowCallback() {
        return mWindow.getCallback();
    }

    @java.lang.Override
    public final void setTitle(java.lang.CharSequence title) {
        mTitle = title;
        onTitleChanged(title);
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        // no-op
    }

    abstract void onTitleChanged(java.lang.CharSequence title);

    final java.lang.CharSequence getTitle() {
        // If the original window callback is an Activity, we'll use it's title
        if (mOriginalWindowCallback instanceof android.app.Activity) {
            return ((android.app.Activity) (mOriginalWindowCallback)).getTitle();
        }
        // Else, we'll return the title we have recorded ourselves
        return mTitle;
    }

    class AppCompatWindowCallbackBase extends android.support.v7.view.WindowCallbackWrapper {
        AppCompatWindowCallbackBase(android.view.Window.Callback callback) {
            super(callback);
        }

        @java.lang.Override
        public boolean dispatchKeyEvent(android.view.KeyEvent event) {
            return android.support.v7.app.AppCompatDelegateImplBase.this.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
        }

        @java.lang.Override
        public boolean dispatchKeyShortcutEvent(android.view.KeyEvent event) {
            return super.dispatchKeyShortcutEvent(event) || android.support.v7.app.AppCompatDelegateImplBase.this.onKeyShortcut(event.getKeyCode(), event);
        }

        @java.lang.Override
        public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
            if ((featureId == android.view.Window.FEATURE_OPTIONS_PANEL) && (!(menu instanceof android.support.v7.view.menu.MenuBuilder))) {
                // If this is an options menu but it's not an AppCompat menu, we eat the event
                // and return false
                return false;
            }
            return super.onCreatePanelMenu(featureId, menu);
        }

        @java.lang.Override
        public void onContentChanged() {
            // We purposely do not propagate this call as this is called when we install
            // our sub-decor rather than the user's content
        }

        @java.lang.Override
        public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
            final android.support.v7.view.menu.MenuBuilder mb = (menu instanceof android.support.v7.view.menu.MenuBuilder) ? ((android.support.v7.view.menu.MenuBuilder) (menu)) : null;
            if ((featureId == android.view.Window.FEATURE_OPTIONS_PANEL) && (mb == null)) {
                // If this is an options menu but it's not an AppCompat menu, we eat the event
                // and return false
                return false;
            }
            // On ICS and below devices, onPreparePanel calls menu.hasVisibleItems() to determine
            // if a panel is prepared. This interferes with any initially invisible items, which
            // are later made visible. We workaround it by making hasVisibleItems() always
            // return true during the onPreparePanel call.
            if (mb != null) {
                mb.setOverrideVisibleItems(true);
            }
            final boolean handled = super.onPreparePanel(featureId, view, menu);
            if (mb != null) {
                mb.setOverrideVisibleItems(false);
            }
            return handled;
        }

        @java.lang.Override
        public boolean onMenuOpened(int featureId, android.view.Menu menu) {
            super.onMenuOpened(featureId, menu);
            android.support.v7.app.AppCompatDelegateImplBase.this.onMenuOpened(featureId, menu);
            return true;
        }

        @java.lang.Override
        public void onPanelClosed(int featureId, android.view.Menu menu) {
            super.onPanelClosed(featureId, menu);
            android.support.v7.app.AppCompatDelegateImplBase.this.onPanelClosed(featureId, menu);
        }
    }
}

