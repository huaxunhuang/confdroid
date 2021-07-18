/**
 * Copyright (C) 2010 The Android Open Source Project
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
public class ActionMenuItem implements android.support.v4.internal.view.SupportMenuItem {
    private final int mId;

    private final int mGroup;

    private final int mCategoryOrder;

    private final int mOrdering;

    private java.lang.CharSequence mTitle;

    private java.lang.CharSequence mTitleCondensed;

    private android.content.Intent mIntent;

    private char mShortcutNumericChar;

    private char mShortcutAlphabeticChar;

    private android.graphics.drawable.Drawable mIconDrawable;

    private int mIconResId = android.support.v7.view.menu.ActionMenuItem.NO_ICON;

    private android.content.Context mContext;

    private android.view.MenuItem.OnMenuItemClickListener mClickListener;

    private static final int NO_ICON = 0;

    private int mFlags = android.support.v7.view.menu.ActionMenuItem.ENABLED;

    private static final int CHECKABLE = 0x1;

    private static final int CHECKED = 0x2;

    private static final int EXCLUSIVE = 0x4;

    private static final int HIDDEN = 0x8;

    private static final int ENABLED = 0x10;

    public ActionMenuItem(android.content.Context context, int group, int id, int categoryOrder, int ordering, java.lang.CharSequence title) {
        mContext = context;
        mId = id;
        mGroup = group;
        mCategoryOrder = categoryOrder;
        mOrdering = ordering;
        mTitle = title;
    }

    public char getAlphabeticShortcut() {
        return mShortcutAlphabeticChar;
    }

    public int getGroupId() {
        return mGroup;
    }

    public android.graphics.drawable.Drawable getIcon() {
        return mIconDrawable;
    }

    public android.content.Intent getIntent() {
        return mIntent;
    }

    public int getItemId() {
        return mId;
    }

    public android.view.ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    public char getNumericShortcut() {
        return mShortcutNumericChar;
    }

    public int getOrder() {
        return mOrdering;
    }

    public android.view.SubMenu getSubMenu() {
        return null;
    }

    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    public java.lang.CharSequence getTitleCondensed() {
        return mTitleCondensed != null ? mTitleCondensed : mTitle;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean isCheckable() {
        return (mFlags & android.support.v7.view.menu.ActionMenuItem.CHECKABLE) != 0;
    }

    public boolean isChecked() {
        return (mFlags & android.support.v7.view.menu.ActionMenuItem.CHECKED) != 0;
    }

    public boolean isEnabled() {
        return (mFlags & android.support.v7.view.menu.ActionMenuItem.ENABLED) != 0;
    }

    public boolean isVisible() {
        return (mFlags & android.support.v7.view.menu.ActionMenuItem.HIDDEN) == 0;
    }

    public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
        mShortcutAlphabeticChar = alphaChar;
        return this;
    }

    public android.view.MenuItem setCheckable(boolean checkable) {
        mFlags = (mFlags & (~android.support.v7.view.menu.ActionMenuItem.CHECKABLE)) | (checkable ? android.support.v7.view.menu.ActionMenuItem.CHECKABLE : 0);
        return this;
    }

    public android.support.v7.view.menu.ActionMenuItem setExclusiveCheckable(boolean exclusive) {
        mFlags = (mFlags & (~android.support.v7.view.menu.ActionMenuItem.EXCLUSIVE)) | (exclusive ? android.support.v7.view.menu.ActionMenuItem.EXCLUSIVE : 0);
        return this;
    }

    public android.view.MenuItem setChecked(boolean checked) {
        mFlags = (mFlags & (~android.support.v7.view.menu.ActionMenuItem.CHECKED)) | (checked ? android.support.v7.view.menu.ActionMenuItem.CHECKED : 0);
        return this;
    }

    public android.view.MenuItem setEnabled(boolean enabled) {
        mFlags = (mFlags & (~android.support.v7.view.menu.ActionMenuItem.ENABLED)) | (enabled ? android.support.v7.view.menu.ActionMenuItem.ENABLED : 0);
        return this;
    }

    public android.view.MenuItem setIcon(android.graphics.drawable.Drawable icon) {
        mIconDrawable = icon;
        mIconResId = android.support.v7.view.menu.ActionMenuItem.NO_ICON;
        return this;
    }

    public android.view.MenuItem setIcon(int iconRes) {
        mIconResId = iconRes;
        mIconDrawable = android.support.v4.content.ContextCompat.getDrawable(mContext, iconRes);
        return this;
    }

    public android.view.MenuItem setIntent(android.content.Intent intent) {
        mIntent = intent;
        return this;
    }

    public android.view.MenuItem setNumericShortcut(char numericChar) {
        mShortcutNumericChar = numericChar;
        return this;
    }

    public android.view.MenuItem setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener menuItemClickListener) {
        mClickListener = menuItemClickListener;
        return this;
    }

    public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
        mShortcutNumericChar = numericChar;
        mShortcutAlphabeticChar = alphaChar;
        return this;
    }

    public android.view.MenuItem setTitle(java.lang.CharSequence title) {
        mTitle = title;
        return this;
    }

    public android.view.MenuItem setTitle(int title) {
        mTitle = mContext.getResources().getString(title);
        return this;
    }

    public android.view.MenuItem setTitleCondensed(java.lang.CharSequence title) {
        mTitleCondensed = title;
        return this;
    }

    public android.view.MenuItem setVisible(boolean visible) {
        mFlags = (mFlags & android.support.v7.view.menu.ActionMenuItem.HIDDEN) | (visible ? 0 : android.support.v7.view.menu.ActionMenuItem.HIDDEN);
        return this;
    }

    public boolean invoke() {
        if ((mClickListener != null) && mClickListener.onMenuItemClick(this)) {
            return true;
        }
        if (mIntent != null) {
            mContext.startActivity(mIntent);
            return true;
        }
        return false;
    }

    public void setShowAsAction(int show) {
        // Do nothing. ActionMenuItems always show as action buttons.
    }

    public android.support.v4.internal.view.SupportMenuItem setActionView(android.view.View actionView) {
        throw new java.lang.UnsupportedOperationException();
    }

    public android.view.View getActionView() {
        return null;
    }

    @java.lang.Override
    public android.view.MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.view.ActionProvider getActionProvider() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setActionView(int resId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.support.v4.view.ActionProvider getSupportActionProvider() {
        return null;
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setSupportActionProvider(android.support.v4.view.ActionProvider actionProvider) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setShowAsActionFlags(int actionEnum) {
        setShowAsAction(actionEnum);
        return this;
    }

    @java.lang.Override
    public boolean expandActionView() {
        return false;
    }

    @java.lang.Override
    public boolean collapseActionView() {
        return false;
    }

    @java.lang.Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @java.lang.Override
    public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem.OnActionExpandListener listener) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.support.v4.internal.view.SupportMenuItem setSupportOnActionExpandListener(android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
        // No need to save the listener; ActionMenuItem does not support collapsing items.
        return this;
    }
}

