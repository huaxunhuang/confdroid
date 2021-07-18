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
 * Common interface for a toolbar that sits as part of the window decor.
 * Layouts that control window decor use this as a point of interaction with different
 * bar implementations.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public interface DecorToolbar {
    android.view.ViewGroup getViewGroup();

    android.content.Context getContext();

    boolean hasExpandedActionView();

    void collapseActionView();

    void setWindowCallback(android.view.Window.Callback cb);

    void setWindowTitle(java.lang.CharSequence title);

    java.lang.CharSequence getTitle();

    void setTitle(java.lang.CharSequence title);

    java.lang.CharSequence getSubtitle();

    void setSubtitle(java.lang.CharSequence subtitle);

    void initProgress();

    void initIndeterminateProgress();

    boolean hasIcon();

    boolean hasLogo();

    void setIcon(int resId);

    void setIcon(android.graphics.drawable.Drawable d);

    void setLogo(int resId);

    void setLogo(android.graphics.drawable.Drawable d);

    boolean canShowOverflowMenu();

    boolean isOverflowMenuShowing();

    boolean isOverflowMenuShowPending();

    boolean showOverflowMenu();

    boolean hideOverflowMenu();

    void setMenuPrepared();

    void setMenu(android.view.Menu menu, android.support.v7.view.menu.MenuPresenter.Callback cb);

    void dismissPopupMenus();

    int getDisplayOptions();

    void setDisplayOptions(int opts);

    void setEmbeddedTabView(android.support.v7.widget.ScrollingTabContainerView tabView);

    boolean hasEmbeddedTabs();

    boolean isTitleTruncated();

    void setCollapsible(boolean collapsible);

    void setHomeButtonEnabled(boolean enable);

    int getNavigationMode();

    void setNavigationMode(int mode);

    void setDropdownParams(android.widget.SpinnerAdapter adapter, android.widget.AdapterView.OnItemSelectedListener listener);

    void setDropdownSelectedPosition(int position);

    int getDropdownSelectedPosition();

    int getDropdownItemCount();

    void setCustomView(android.view.View view);

    android.view.View getCustomView();

    void animateToVisibility(int visibility);

    android.support.v4.view.ViewPropertyAnimatorCompat setupAnimatorToVisibility(int visibility, long duration);

    void setNavigationIcon(android.graphics.drawable.Drawable icon);

    void setNavigationIcon(int resId);

    void setNavigationContentDescription(java.lang.CharSequence description);

    void setNavigationContentDescription(int resId);

    void setDefaultNavigationContentDescription(int defaultNavigationContentDescription);

    void setDefaultNavigationIcon(android.graphics.drawable.Drawable icon);

    void saveHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates);

    void restoreHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates);

    void setBackgroundDrawable(android.graphics.drawable.Drawable d);

    int getHeight();

    void setVisibility(int visible);

    int getVisibility();

    void setMenuCallbacks(android.support.v7.view.menu.MenuPresenter.Callback presenterCallback, android.support.v7.view.menu.MenuBuilder.Callback menuBuilderCallback);

    android.view.Menu getMenu();
}

