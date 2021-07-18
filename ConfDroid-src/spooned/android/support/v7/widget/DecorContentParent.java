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
 * Implemented by the top-level decor layout for a window. DecorContentParent offers
 * entry points for a number of title/window decor features.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public interface DecorContentParent {
    void setWindowCallback(android.view.Window.Callback cb);

    void setWindowTitle(java.lang.CharSequence title);

    java.lang.CharSequence getTitle();

    void initFeature(int windowFeature);

    void setUiOptions(int uiOptions);

    boolean hasIcon();

    boolean hasLogo();

    void setIcon(int resId);

    void setIcon(android.graphics.drawable.Drawable d);

    void setLogo(int resId);

    boolean canShowOverflowMenu();

    boolean isOverflowMenuShowing();

    boolean isOverflowMenuShowPending();

    boolean showOverflowMenu();

    boolean hideOverflowMenu();

    void setMenuPrepared();

    void setMenu(android.view.Menu menu, android.support.v7.view.menu.MenuPresenter.Callback cb);

    void saveToolbarHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates);

    void restoreToolbarHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates);

    void dismissPopups();
}

