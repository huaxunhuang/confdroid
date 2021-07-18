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
 * Wraps a support {@link SupportMenu} as a framework {@link android.view.Menu}
 */
class MenuWrapperICS extends android.support.v7.view.menu.BaseMenuWrapper<android.support.v4.internal.view.SupportMenu> implements android.view.Menu {
    MenuWrapperICS(android.content.Context context, android.support.v4.internal.view.SupportMenu object) {
        super(context, object);
    }

    @java.lang.Override
    public android.view.MenuItem add(java.lang.CharSequence title) {
        return getMenuItemWrapper(mWrappedObject.add(title));
    }

    @java.lang.Override
    public android.view.MenuItem add(int titleRes) {
        return getMenuItemWrapper(mWrappedObject.add(titleRes));
    }

    @java.lang.Override
    public android.view.MenuItem add(int groupId, int itemId, int order, java.lang.CharSequence title) {
        return getMenuItemWrapper(mWrappedObject.add(groupId, itemId, order, title));
    }

    @java.lang.Override
    public android.view.MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return getMenuItemWrapper(mWrappedObject.add(groupId, itemId, order, titleRes));
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(java.lang.CharSequence title) {
        return getSubMenuWrapper(mWrappedObject.addSubMenu(title));
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(int titleRes) {
        return getSubMenuWrapper(mWrappedObject.addSubMenu(titleRes));
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(int groupId, int itemId, int order, java.lang.CharSequence title) {
        return getSubMenuWrapper(mWrappedObject.addSubMenu(groupId, itemId, order, title));
    }

    @java.lang.Override
    public android.view.SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return getSubMenuWrapper(mWrappedObject.addSubMenu(groupId, itemId, order, titleRes));
    }

    @java.lang.Override
    public int addIntentOptions(int groupId, int itemId, int order, android.content.ComponentName caller, android.content.Intent[] specifics, android.content.Intent intent, int flags, android.view.MenuItem[] outSpecificItems) {
        android.view.MenuItem[] items = null;
        if (outSpecificItems != null) {
            items = new android.view.MenuItem[outSpecificItems.length];
        }
        int result = mWrappedObject.addIntentOptions(groupId, itemId, order, caller, specifics, intent, flags, items);
        if (items != null) {
            for (int i = 0, z = items.length; i < z; i++) {
                outSpecificItems[i] = getMenuItemWrapper(items[i]);
            }
        }
        return result;
    }

    @java.lang.Override
    public void removeItem(int id) {
        internalRemoveItem(id);
        mWrappedObject.removeItem(id);
    }

    @java.lang.Override
    public void removeGroup(int groupId) {
        internalRemoveGroup(groupId);
        mWrappedObject.removeGroup(groupId);
    }

    @java.lang.Override
    public void clear() {
        internalClear();
        mWrappedObject.clear();
    }

    @java.lang.Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        mWrappedObject.setGroupCheckable(group, checkable, exclusive);
    }

    @java.lang.Override
    public void setGroupVisible(int group, boolean visible) {
        mWrappedObject.setGroupVisible(group, visible);
    }

    @java.lang.Override
    public void setGroupEnabled(int group, boolean enabled) {
        mWrappedObject.setGroupEnabled(group, enabled);
    }

    @java.lang.Override
    public boolean hasVisibleItems() {
        return mWrappedObject.hasVisibleItems();
    }

    @java.lang.Override
    public android.view.MenuItem findItem(int id) {
        return getMenuItemWrapper(mWrappedObject.findItem(id));
    }

    @java.lang.Override
    public int size() {
        return mWrappedObject.size();
    }

    @java.lang.Override
    public android.view.MenuItem getItem(int index) {
        return getMenuItemWrapper(mWrappedObject.getItem(index));
    }

    @java.lang.Override
    public void close() {
        mWrappedObject.close();
    }

    @java.lang.Override
    public boolean performShortcut(int keyCode, android.view.KeyEvent event, int flags) {
        return mWrappedObject.performShortcut(keyCode, event, flags);
    }

    @java.lang.Override
    public boolean isShortcutKey(int keyCode, android.view.KeyEvent event) {
        return mWrappedObject.isShortcutKey(keyCode, event);
    }

    @java.lang.Override
    public boolean performIdentifierAction(int id, int flags) {
        return mWrappedObject.performIdentifierAction(id, flags);
    }

    @java.lang.Override
    public void setQwertyMode(boolean isQwerty) {
        mWrappedObject.setQwertyMode(isQwerty);
    }
}

