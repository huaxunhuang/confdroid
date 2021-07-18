/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.widget;


/**
 * <p>
 * Represents a standard bottom navigation bar for application. It is an implementation of
 * <a href="https://material.google.com/components/bottom-navigation.html">material design bottom
 * navigation</a>.
 * </p>
 *
 * <p>
 * Bottom navigation bars make it easy for users to explore and switch between top-level views in
 * a single tap. It should be used when application has three to five top-level destinations.
 * </p>
 *
 * <p>
 * The bar contents can be populated by specifying a menu resource file. Each menu item title, icon
 * and enabled state will be used for displaying bottom navigation bar items.
 * </p>
 *
 * <pre>
 * layout resource file:
 * &lt;android.support.design.widget.BottomNavigationView
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:design="http://schema.android.com/apk/res/android.support.design"
 *     android:id="@+id/navigation"
 *     android:layout_width="match_parent"
 *     android:layout_height="56dp"
 *     android:layout_gravity="start"
 *     design:menu="@menu/my_navigation_items" /&gt;
 *
 * res/menu/my_navigation_items.xml:
 * &lt;menu xmlns:android="http://schemas.android.com/apk/res/android"&gt;
 *     &lt;item android:id="@+id/action_search"
 *          android:title="@string/menu_search"
 *          android:icon="@drawable/ic_search" /&gt;
 *     &lt;item android:id="@+id/action_settings"
 *          android:title="@string/menu_settings"
 *          android:icon="@drawable/ic_add" /&gt;
 *     &lt;item android:id="@+id/action_navigation"
 *          android:title="@string/menu_navigation"
 *          android:icon="@drawable/ic_action_navigation_menu" /&gt;
 * &lt;/menu&gt;
 * </pre>
 */
public class BottomNavigationView extends android.widget.FrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    private static final int[] DISABLED_STATE_SET = new int[]{ -android.R.attr.state_enabled };

    private final android.support.v7.view.menu.MenuBuilder mMenu;

    private final android.support.design.internal.BottomNavigationMenuView mMenuView;

    private final android.support.design.internal.BottomNavigationPresenter mPresenter = new android.support.design.internal.BottomNavigationPresenter();

    private android.view.MenuInflater mMenuInflater;

    private android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mListener;

    public BottomNavigationView(android.content.Context context) {
        this(context, null);
    }

    public BottomNavigationView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        // Create the menu
        mMenu = new android.support.design.internal.BottomNavigationMenu(context);
        mMenuView = new android.support.design.internal.BottomNavigationMenuView(context);
        android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = android.view.Gravity.CENTER;
        mMenuView.setLayoutParams(params);
        mPresenter.setBottomNavigationMenuView(mMenuView);
        mMenuView.setPresenter(mPresenter);
        mMenu.addMenuPresenter(mPresenter);
        mPresenter.initForMenu(getContext(), mMenu);
        // Custom attributes
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.BottomNavigationView, defStyleAttr, R.style.Widget_Design_BottomNavigationView);
        if (a.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
            mMenuView.setIconTintList(a.getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
        } else {
            mMenuView.setIconTintList(createDefaultColorStateList(android.R.attr.textColorSecondary));
        }
        if (a.hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
            mMenuView.setItemTextColor(a.getColorStateList(R.styleable.BottomNavigationView_itemTextColor));
        } else {
            mMenuView.setItemTextColor(createDefaultColorStateList(android.R.attr.textColorSecondary));
        }
        if (a.hasValue(R.styleable.BottomNavigationView_elevation)) {
            android.support.v4.view.ViewCompat.setElevation(this, a.getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0));
        }
        int itemBackground = a.getResourceId(R.styleable.BottomNavigationView_itemBackground, 0);
        mMenuView.setItemBackgroundRes(itemBackground);
        if (a.hasValue(R.styleable.BottomNavigationView_menu)) {
            inflateMenu(a.getResourceId(R.styleable.BottomNavigationView_menu, 0));
        }
        a.recycle();
        addView(mMenuView, params);
        if (android.os.Build.VERSION.SDK_INT < 21) {
            addCompatibilityTopDivider(context);
        }
        mMenu.setCallback(new android.support.v7.view.menu.MenuBuilder.Callback() {
            @java.lang.Override
            public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
                return (mListener != null) && (!mListener.onNavigationItemSelected(item));
            }

            @java.lang.Override
            public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
            }
        });
    }

    /**
     * Set a listener that will be notified when a bottom navigation item is selected.
     *
     * @param listener
     * 		The listener to notify
     */
    public void setOnNavigationItemSelectedListener(@android.support.annotation.Nullable
    android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Returns the {@link Menu} instance associated with this bottom navigation bar.
     */
    @android.support.annotation.NonNull
    public android.view.Menu getMenu() {
        return mMenu;
    }

    /**
     * Inflate a menu resource into this navigation view.
     *
     * <p>Existing items in the menu will not be modified or removed.</p>
     *
     * @param resId
     * 		ID of a menu resource to inflate
     */
    public void inflateMenu(int resId) {
        mPresenter.setUpdateSuspended(true);
        getMenuInflater().inflate(resId, mMenu);
        mPresenter.setUpdateSuspended(false);
        mPresenter.updateMenuView(true);
    }

    /**
     *
     *
     * @return The maximum number of items that can be shown in BottomNavigationView.
     */
    public int getMaxItemCount() {
        return android.support.design.internal.BottomNavigationMenu.MAX_ITEM_COUNT;
    }

    /**
     * Returns the tint which is applied to our menu items' icons.
     *
     * @see #setItemIconTintList(ColorStateList)
     * @unknown ref R.styleable#BottomNavigationView_itemIconTint
     */
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemIconTintList() {
        return mMenuView.getIconTintList();
    }

    /**
     * Set the tint which is applied to our menu items' icons.
     *
     * @param tint
     * 		the tint to apply.
     * @unknown ref R.styleable#BottomNavigationView_itemIconTint
     */
    public void setItemIconTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mMenuView.setIconTintList(tint);
    }

    /**
     * Returns colors used for the different states (normal, selected, focused, etc.) of the menu
     * item text.
     *
     * @see #setItemTextColor(ColorStateList)
     * @return the ColorStateList of colors used for the different states of the menu items text.
     * @unknown ref R.styleable#BottomNavigationView_itemTextColor
     */
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemTextColor() {
        return mMenuView.getItemTextColor();
    }

    /**
     * Set the colors to use for the different states (normal, selected, focused, etc.) of the menu
     * item text.
     *
     * @see #getItemTextColor()
     * @unknown ref R.styleable#BottomNavigationView_itemTextColor
     */
    public void setItemTextColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList textColor) {
        mMenuView.setItemTextColor(textColor);
    }

    /**
     * Returns the background resource of the menu items.
     *
     * @see #setItemBackgroundResource(int)
     * @unknown ref R.styleable#BottomNavigationView_itemBackground
     */
    @android.support.annotation.DrawableRes
    public int getItemBackgroundResource() {
        return mMenuView.getItemBackgroundRes();
    }

    /**
     * Set the background of our menu items to the given resource.
     *
     * @param resId
     * 		The identifier of the resource.
     * @unknown ref R.styleable#BottomNavigationView_itemBackground
     */
    public void setItemBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        mMenuView.setItemBackgroundRes(resId);
    }

    /**
     * Listener for handling events on bottom navigation items.
     */
    public interface OnNavigationItemSelectedListener {
        /**
         * Called when an item in the bottom navigation menu is selected.
         *
         * @param item
         * 		The selected item
         * @return true to display the item as the selected item and false if the item should not
        be selected. Consider setting non-selectable items as disabled preemptively to
        make them appear non-interactive.
         */
        boolean onNavigationItemSelected(@android.support.annotation.NonNull
        android.view.MenuItem item);
    }

    private void addCompatibilityTopDivider(android.content.Context context) {
        android.view.View divider = new android.view.View(context);
        divider.setBackgroundColor(android.support.v4.content.ContextCompat.getColor(context, R.color.design_bottom_navigation_shadow_color));
        android.widget.FrameLayout.LayoutParams dividerParams = new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height));
        divider.setLayoutParams(dividerParams);
        addView(divider);
    }

    private android.view.MenuInflater getMenuInflater() {
        if (mMenuInflater == null) {
            mMenuInflater = new android.support.v7.view.SupportMenuInflater(getContext());
        }
        return mMenuInflater;
    }

    private android.content.res.ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        final android.util.TypedValue value = new android.util.TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        android.content.res.ColorStateList baseColor = android.support.v7.content.res.AppCompatResources.getColorStateList(getContext(), value.resourceId);
        if (!getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        return new android.content.res.ColorStateList(new int[][]{ android.support.design.widget.BottomNavigationView.DISABLED_STATE_SET, android.support.design.widget.BottomNavigationView.CHECKED_STATE_SET, android.view.View.EMPTY_STATE_SET }, new int[]{ baseColor.getColorForState(android.support.design.widget.BottomNavigationView.DISABLED_STATE_SET, defaultColor), colorPrimary, defaultColor });
    }
}

