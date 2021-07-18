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
package android.support.v7.view;


/**
 * Wraps a support {@link android.support.v7.view.ActionMode} as a framework
 * {@link android.view.ActionMode}.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
public class SupportActionModeWrapper extends android.view.ActionMode {
    final android.content.Context mContext;

    final android.support.v7.view.ActionMode mWrappedObject;

    public SupportActionModeWrapper(android.content.Context context, android.support.v7.view.ActionMode supportActionMode) {
        mContext = context;
        mWrappedObject = supportActionMode;
    }

    @java.lang.Override
    public java.lang.Object getTag() {
        return mWrappedObject.getTag();
    }

    @java.lang.Override
    public void setTag(java.lang.Object tag) {
        mWrappedObject.setTag(tag);
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mWrappedObject.setTitle(title);
    }

    @java.lang.Override
    public void setSubtitle(java.lang.CharSequence subtitle) {
        mWrappedObject.setSubtitle(subtitle);
    }

    @java.lang.Override
    public void invalidate() {
        mWrappedObject.invalidate();
    }

    @java.lang.Override
    public void finish() {
        mWrappedObject.finish();
    }

    @java.lang.Override
    public android.view.Menu getMenu() {
        return android.support.v7.view.menu.MenuWrapperFactory.wrapSupportMenu(mContext, ((android.support.v4.internal.view.SupportMenu) (mWrappedObject.getMenu())));
    }

    @java.lang.Override
    public java.lang.CharSequence getTitle() {
        return mWrappedObject.getTitle();
    }

    @java.lang.Override
    public void setTitle(int resId) {
        mWrappedObject.setTitle(resId);
    }

    @java.lang.Override
    public java.lang.CharSequence getSubtitle() {
        return mWrappedObject.getSubtitle();
    }

    @java.lang.Override
    public void setSubtitle(int resId) {
        mWrappedObject.setSubtitle(resId);
    }

    @java.lang.Override
    public android.view.View getCustomView() {
        return mWrappedObject.getCustomView();
    }

    @java.lang.Override
    public void setCustomView(android.view.View view) {
        mWrappedObject.setCustomView(view);
    }

    @java.lang.Override
    public android.view.MenuInflater getMenuInflater() {
        return mWrappedObject.getMenuInflater();
    }

    @java.lang.Override
    public boolean getTitleOptionalHint() {
        return mWrappedObject.getTitleOptionalHint();
    }

    @java.lang.Override
    public void setTitleOptionalHint(boolean titleOptional) {
        mWrappedObject.setTitleOptionalHint(titleOptional);
    }

    @java.lang.Override
    public boolean isTitleOptional() {
        return mWrappedObject.isTitleOptional();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class CallbackWrapper implements android.support.v7.view.ActionMode.Callback {
        final android.view.ActionMode.Callback mWrappedCallback;

        final android.content.Context mContext;

        final java.util.ArrayList<android.support.v7.view.SupportActionModeWrapper> mActionModes;

        final android.support.v4.util.SimpleArrayMap<android.view.Menu, android.view.Menu> mMenus;

        public CallbackWrapper(android.content.Context context, android.view.ActionMode.Callback supportCallback) {
            mContext = context;
            mWrappedCallback = supportCallback;
            mActionModes = new java.util.ArrayList<>();
            mMenus = new android.support.v4.util.SimpleArrayMap<>();
        }

        @java.lang.Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, android.view.Menu menu) {
            return mWrappedCallback.onCreateActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }

        @java.lang.Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, android.view.Menu menu) {
            return mWrappedCallback.onPrepareActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }

        @java.lang.Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, android.view.MenuItem item) {
            return mWrappedCallback.onActionItemClicked(getActionModeWrapper(mode), android.support.v7.view.menu.MenuWrapperFactory.wrapSupportMenuItem(mContext, ((android.support.v4.internal.view.SupportMenuItem) (item))));
        }

        @java.lang.Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
            mWrappedCallback.onDestroyActionMode(getActionModeWrapper(mode));
        }

        private android.view.Menu getMenuWrapper(android.view.Menu menu) {
            android.view.Menu wrapper = mMenus.get(menu);
            if (wrapper == null) {
                wrapper = android.support.v7.view.menu.MenuWrapperFactory.wrapSupportMenu(mContext, ((android.support.v4.internal.view.SupportMenu) (menu)));
                mMenus.put(menu, wrapper);
            }
            return wrapper;
        }

        public android.view.ActionMode getActionModeWrapper(android.support.v7.view.ActionMode mode) {
            // First see if we already have a wrapper for this mode
            for (int i = 0, count = mActionModes.size(); i < count; i++) {
                android.support.v7.view.SupportActionModeWrapper wrapper = mActionModes.get(i);
                if ((wrapper != null) && (wrapper.mWrappedObject == mode)) {
                    // We've found a wrapper, return it
                    return wrapper;
                }
            }
            // If we reach here then we haven't seen this mode before. Create a new wrapper and
            // add it to our collection
            android.support.v7.view.SupportActionModeWrapper wrapper = new android.support.v7.view.SupportActionModeWrapper(mContext, mode);
            mActionModes.add(wrapper);
            return wrapper;
        }
    }
}

