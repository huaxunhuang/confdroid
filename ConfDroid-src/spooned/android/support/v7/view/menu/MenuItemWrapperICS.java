/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Wraps a support {@link SupportMenuItem} as a framework {@link android.view.MenuItem}
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MenuItemWrapperICS extends android.support.v7.view.menu.BaseMenuWrapper<android.support.v4.internal.view.SupportMenuItem> implements android.view.MenuItem {
    static final java.lang.String LOG_TAG = "MenuItemWrapper";

    // Reflection Method to call setExclusiveCheckable
    private java.lang.reflect.Method mSetExclusiveCheckableMethod;

    MenuItemWrapperICS(android.content.Context context, android.support.v4.internal.view.SupportMenuItem object) {
        super(context, object);
    }

    @java.lang.Override
    public int getItemId() {
        return mWrappedObject.getItemId();
    }

    @java.lang.Override
    public int getGroupId() {
        return mWrappedObject.getGroupId();
    }

    @java.lang.Override
    public int getOrder() {
        return mWrappedObject.getOrder();
    }

    @java.lang.Override
    public android.view.MenuItem setTitle(java.lang.CharSequence title) {
        mWrappedObject.setTitle(title);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setTitle(int title) {
        mWrappedObject.setTitle(title);
        return this;
    }

    @java.lang.Override
    public java.lang.CharSequence getTitle() {
        return mWrappedObject.getTitle();
    }

    @java.lang.Override
    public android.view.MenuItem setTitleCondensed(java.lang.CharSequence title) {
        mWrappedObject.setTitleCondensed(title);
        return this;
    }

    @java.lang.Override
    public java.lang.CharSequence getTitleCondensed() {
        return mWrappedObject.getTitleCondensed();
    }

    @java.lang.Override
    public android.view.MenuItem setIcon(android.graphics.drawable.Drawable icon) {
        mWrappedObject.setIcon(icon);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setIcon(int iconRes) {
        mWrappedObject.setIcon(iconRes);
        return this;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getIcon() {
        return mWrappedObject.getIcon();
    }

    @java.lang.Override
    public android.view.MenuItem setIntent(android.content.Intent intent) {
        mWrappedObject.setIntent(intent);
        return this;
    }

    @java.lang.Override
    public android.content.Intent getIntent() {
        return mWrappedObject.getIntent();
    }

    @java.lang.Override
    public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
        mWrappedObject.setShortcut(numericChar, alphaChar);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setNumericShortcut(char numericChar) {
        mWrappedObject.setNumericShortcut(numericChar);
        return this;
    }

    @java.lang.Override
    public char getNumericShortcut() {
        return mWrappedObject.getNumericShortcut();
    }

    @java.lang.Override
    public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
        mWrappedObject.setAlphabeticShortcut(alphaChar);
        return this;
    }

    @java.lang.Override
    public char getAlphabeticShortcut() {
        return mWrappedObject.getAlphabeticShortcut();
    }

    @java.lang.Override
    public android.view.MenuItem setCheckable(boolean checkable) {
        mWrappedObject.setCheckable(checkable);
        return this;
    }

    @java.lang.Override
    public boolean isCheckable() {
        return mWrappedObject.isCheckable();
    }

    @java.lang.Override
    public android.view.MenuItem setChecked(boolean checked) {
        mWrappedObject.setChecked(checked);
        return this;
    }

    @java.lang.Override
    public boolean isChecked() {
        return mWrappedObject.isChecked();
    }

    @java.lang.Override
    public android.view.MenuItem setVisible(boolean visible) {
        return mWrappedObject.setVisible(visible);
    }

    @java.lang.Override
    public boolean isVisible() {
        return mWrappedObject.isVisible();
    }

    @java.lang.Override
    public android.view.MenuItem setEnabled(boolean enabled) {
        mWrappedObject.setEnabled(enabled);
        return this;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return mWrappedObject.isEnabled();
    }

    @java.lang.Override
    public boolean hasSubMenu() {
        return mWrappedObject.hasSubMenu();
    }

    @java.lang.Override
    public android.view.SubMenu getSubMenu() {
        return getSubMenuWrapper(mWrappedObject.getSubMenu());
    }

    @java.lang.Override
    public android.view.MenuItem setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener menuItemClickListener) {
        mWrappedObject.setOnMenuItemClickListener(menuItemClickListener != null ? new android.support.v7.view.menu.MenuItemWrapperICS.OnMenuItemClickListenerWrapper(menuItemClickListener) : null);
        return this;
    }

    @java.lang.Override
    public android.view.ContextMenu.ContextMenuInfo getMenuInfo() {
        return mWrappedObject.getMenuInfo();
    }

    @java.lang.Override
    public void setShowAsAction(int actionEnum) {
        mWrappedObject.setShowAsAction(actionEnum);
    }

    @java.lang.Override
    public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
        mWrappedObject.setShowAsActionFlags(actionEnum);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setActionView(android.view.View view) {
        if (view instanceof android.view.CollapsibleActionView) {
            view = new android.support.v7.view.menu.MenuItemWrapperICS.CollapsibleActionViewWrapper(view);
        }
        mWrappedObject.setActionView(view);
        return this;
    }

    @java.lang.Override
    public android.view.MenuItem setActionView(int resId) {
        // Make framework menu item inflate the view
        mWrappedObject.setActionView(resId);
        android.view.View actionView = mWrappedObject.getActionView();
        if (actionView instanceof android.view.CollapsibleActionView) {
            // If the inflated Action View is support-collapsible, wrap it
            mWrappedObject.setActionView(new android.support.v7.view.menu.MenuItemWrapperICS.CollapsibleActionViewWrapper(actionView));
        }
        return this;
    }

    @java.lang.Override
    public android.view.View getActionView() {
        android.view.View actionView = mWrappedObject.getActionView();
        if (actionView instanceof android.support.v7.view.menu.MenuItemWrapperICS.CollapsibleActionViewWrapper) {
            return ((android.support.v7.view.menu.MenuItemWrapperICS.CollapsibleActionViewWrapper) (actionView)).getWrappedView();
        }
        return actionView;
    }

    @java.lang.Override
    public android.view.MenuItem setActionProvider(android.view.ActionProvider provider) {
        mWrappedObject.setSupportActionProvider(provider != null ? createActionProviderWrapper(provider) : null);
        return this;
    }

    @java.lang.Override
    public android.view.ActionProvider getActionProvider() {
        android.support.v4.view.ActionProvider provider = mWrappedObject.getSupportActionProvider();
        if (provider instanceof android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper) {
            return ((android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper) (provider)).mInner;
        }
        return null;
    }

    @java.lang.Override
    public boolean expandActionView() {
        return mWrappedObject.expandActionView();
    }

    @java.lang.Override
    public boolean collapseActionView() {
        return mWrappedObject.collapseActionView();
    }

    @java.lang.Override
    public boolean isActionViewExpanded() {
        return mWrappedObject.isActionViewExpanded();
    }

    @java.lang.Override
    public android.view.MenuItem setOnActionExpandListener(android.view.MenuItem.OnActionExpandListener listener) {
        mWrappedObject.setSupportOnActionExpandListener(listener != null ? new android.support.v7.view.menu.MenuItemWrapperICS.OnActionExpandListenerWrapper(listener) : null);
        return this;
    }

    public void setExclusiveCheckable(boolean checkable) {
        try {
            if (mSetExclusiveCheckableMethod == null) {
                mSetExclusiveCheckableMethod = mWrappedObject.getClass().getDeclaredMethod("setExclusiveCheckable", java.lang.Boolean.TYPE);
            }
            mSetExclusiveCheckableMethod.invoke(mWrappedObject, checkable);
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.support.v7.view.menu.MenuItemWrapperICS.LOG_TAG, "Error while calling setExclusiveCheckable", e);
        }
    }

    android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider provider) {
        return new android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper(mContext, provider);
    }

    private class OnMenuItemClickListenerWrapper extends android.support.v7.view.menu.BaseWrapper<android.view.MenuItem.OnMenuItemClickListener> implements android.view.MenuItem.OnMenuItemClickListener {
        OnMenuItemClickListenerWrapper(android.view.MenuItem.OnMenuItemClickListener object) {
            super(object);
        }

        @java.lang.Override
        public boolean onMenuItemClick(android.view.MenuItem item) {
            return mWrappedObject.onMenuItemClick(getMenuItemWrapper(item));
        }
    }

    private class OnActionExpandListenerWrapper extends android.support.v7.view.menu.BaseWrapper<android.view.MenuItem.OnActionExpandListener> implements android.support.v4.view.MenuItemCompat.OnActionExpandListener {
        OnActionExpandListenerWrapper(android.view.MenuItem.OnActionExpandListener object) {
            super(object);
        }

        @java.lang.Override
        public boolean onMenuItemActionExpand(android.view.MenuItem item) {
            return mWrappedObject.onMenuItemActionExpand(getMenuItemWrapper(item));
        }

        @java.lang.Override
        public boolean onMenuItemActionCollapse(android.view.MenuItem item) {
            return mWrappedObject.onMenuItemActionCollapse(getMenuItemWrapper(item));
        }
    }

    class ActionProviderWrapper extends android.support.v4.view.ActionProvider {
        final android.view.ActionProvider mInner;

        public ActionProviderWrapper(android.content.Context context, android.view.ActionProvider inner) {
            super(context);
            mInner = inner;
        }

        @java.lang.Override
        public android.view.View onCreateActionView() {
            return mInner.onCreateActionView();
        }

        @java.lang.Override
        public boolean onPerformDefaultAction() {
            return mInner.onPerformDefaultAction();
        }

        @java.lang.Override
        public boolean hasSubMenu() {
            return mInner.hasSubMenu();
        }

        @java.lang.Override
        public void onPrepareSubMenu(android.view.SubMenu subMenu) {
            mInner.onPrepareSubMenu(getSubMenuWrapper(subMenu));
        }
    }

    /**
     * Wrap a support {@link android.support.v7.view.CollapsibleActionView} into a framework
     * {@link android.view.CollapsibleActionView}.
     */
    static class CollapsibleActionViewWrapper extends android.widget.FrameLayout implements android.support.v7.view.CollapsibleActionView {
        final android.view.CollapsibleActionView mWrappedView;

        CollapsibleActionViewWrapper(android.view.View actionView) {
            super(actionView.getContext());
            mWrappedView = ((android.view.CollapsibleActionView) (actionView));
            addView(actionView);
        }

        @java.lang.Override
        public void onActionViewExpanded() {
            mWrappedView.onActionViewExpanded();
        }

        @java.lang.Override
        public void onActionViewCollapsed() {
            mWrappedView.onActionViewCollapsed();
        }

        android.view.View getWrappedView() {
            return ((android.view.View) (mWrappedView));
        }
    }
}

