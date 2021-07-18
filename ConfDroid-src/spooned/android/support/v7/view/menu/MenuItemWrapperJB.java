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
package android.support.v7.view.menu;


/**
 * Wraps a support {@link SupportMenuItem} as a framework {@link android.view.MenuItem}
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
@android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
class MenuItemWrapperJB extends android.support.v7.view.menu.MenuItemWrapperICS {
    MenuItemWrapperJB(android.content.Context context, android.support.v4.internal.view.SupportMenuItem object) {
        super(context, object);
    }

    @java.lang.Override
    android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider provider) {
        return new android.support.v7.view.menu.MenuItemWrapperJB.ActionProviderWrapperJB(mContext, provider);
    }

    class ActionProviderWrapperJB extends android.support.v7.view.menu.MenuItemWrapperICS.ActionProviderWrapper implements android.view.ActionProvider.VisibilityListener {
        android.support.v4.view.ActionProvider.VisibilityListener mListener;

        public ActionProviderWrapperJB(android.content.Context context, android.view.ActionProvider inner) {
            super(context, inner);
        }

        @java.lang.Override
        public android.view.View onCreateActionView(android.view.MenuItem forItem) {
            return mInner.onCreateActionView(forItem);
        }

        @java.lang.Override
        public boolean overridesItemVisibility() {
            return mInner.overridesItemVisibility();
        }

        @java.lang.Override
        public boolean isVisible() {
            return mInner.isVisible();
        }

        @java.lang.Override
        public void refreshVisibility() {
            mInner.refreshVisibility();
        }

        @java.lang.Override
        public void setVisibilityListener(android.support.v4.view.ActionProvider.VisibilityListener listener) {
            mListener = listener;
            mInner.setVisibilityListener(listener != null ? this : null);
        }

        @java.lang.Override
        public void onActionProviderVisibilityChanged(boolean isVisible) {
            if (mListener != null) {
                mListener.onActionProviderVisibilityChanged(isVisible);
            }
        }
    }
}

