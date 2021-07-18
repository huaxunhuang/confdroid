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
package android.support.v7.view;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class StandaloneActionMode extends android.support.v7.view.ActionMode implements android.support.v7.view.menu.MenuBuilder.Callback {
    private android.content.Context mContext;

    private android.support.v7.widget.ActionBarContextView mContextView;

    private android.support.v7.view.ActionMode.Callback mCallback;

    private java.lang.ref.WeakReference<android.view.View> mCustomView;

    private boolean mFinished;

    private boolean mFocusable;

    private android.support.v7.view.menu.MenuBuilder mMenu;

    public StandaloneActionMode(android.content.Context context, android.support.v7.widget.ActionBarContextView view, android.support.v7.view.ActionMode.Callback callback, boolean isFocusable) {
        mContext = context;
        mContextView = view;
        mCallback = callback;
        mMenu = new android.support.v7.view.menu.MenuBuilder(view.getContext()).setDefaultShowAsAction(android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        mMenu.setCallback(this);
        mFocusable = isFocusable;
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mContextView.setTitle(title);
    }

    @java.lang.Override
    public void setSubtitle(java.lang.CharSequence subtitle) {
        mContextView.setSubtitle(subtitle);
    }

    @java.lang.Override
    public void setTitle(int resId) {
        setTitle(mContext.getString(resId));
    }

    @java.lang.Override
    public void setSubtitle(int resId) {
        setSubtitle(mContext.getString(resId));
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
    public void setCustomView(android.view.View view) {
        mContextView.setCustomView(view);
        mCustomView = (view != null) ? new java.lang.ref.WeakReference<android.view.View>(view) : null;
    }

    @java.lang.Override
    public void invalidate() {
        mCallback.onPrepareActionMode(this, mMenu);
    }

    @java.lang.Override
    public void finish() {
        if (mFinished) {
            return;
        }
        mFinished = true;
        mContextView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
        mCallback.onDestroyActionMode(this);
    }

    @java.lang.Override
    public android.view.Menu getMenu() {
        return mMenu;
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
    public android.view.View getCustomView() {
        return mCustomView != null ? mCustomView.get() : null;
    }

    @java.lang.Override
    public android.view.MenuInflater getMenuInflater() {
        return new android.support.v7.view.SupportMenuInflater(mContextView.getContext());
    }

    public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
        return mCallback.onActionItemClicked(this, item);
    }

    public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
    }

    public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems()) {
            return true;
        }
        new android.support.v7.view.menu.MenuPopupHelper(mContextView.getContext(), subMenu).show();
        return true;
    }

    public void onCloseSubMenu(android.support.v7.view.menu.SubMenuBuilder menu) {
    }

    public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
        invalidate();
        mContextView.showOverflowMenu();
    }

    public boolean isUiFocusable() {
        return mFocusable;
    }
}

