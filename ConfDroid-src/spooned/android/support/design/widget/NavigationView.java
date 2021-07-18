/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Represents a standard navigation menu for application. The menu contents can be populated
 * by a menu resource file.
 * <p>NavigationView is typically placed inside a {@link android.support.v4.widget.DrawerLayout}.
 * </p>
 * <pre>
 * &lt;android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:app="http://schemas.android.com/apk/res-auto"
 *     android:id="@+id/drawer_layout"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:fitsSystemWindows="true"&gt;
 *
 *     &lt;!-- Your contents --&gt;
 *
 *     &lt;android.support.design.widget.NavigationView
 *         android:id="@+id/navigation"
 *         android:layout_width="wrap_content"
 *         android:layout_height="match_parent"
 *         android:layout_gravity="start"
 *         app:menu="@menu/my_navigation_items" /&gt;
 * &lt;/android.support.v4.widget.DrawerLayout&gt;
 * </pre>
 */
public class NavigationView extends android.support.design.internal.ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    private static final int[] DISABLED_STATE_SET = new int[]{ -android.R.attr.state_enabled };

    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;

    private final android.support.design.internal.NavigationMenu mMenu;

    private final android.support.design.internal.NavigationMenuPresenter mPresenter = new android.support.design.internal.NavigationMenuPresenter();

    android.support.design.widget.NavigationView.OnNavigationItemSelectedListener mListener;

    private int mMaxWidth;

    private android.view.MenuInflater mMenuInflater;

    public NavigationView(android.content.Context context) {
        this(context, null);
    }

    public NavigationView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        // Create the menu
        mMenu = new android.support.design.internal.NavigationMenu(context);
        // Custom attributes
        android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NavigationView, defStyleAttr, R.style.Widget_Design_NavigationView);
        android.support.v4.view.ViewCompat.setBackground(this, a.getDrawable(R.styleable.NavigationView_android_background));
        if (a.hasValue(R.styleable.NavigationView_elevation)) {
            android.support.v4.view.ViewCompat.setElevation(this, a.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        android.support.v4.view.ViewCompat.setFitsSystemWindows(this, a.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        mMaxWidth = a.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        final android.content.res.ColorStateList itemIconTint;
        if (a.hasValue(R.styleable.NavigationView_itemIconTint)) {
            itemIconTint = a.getColorStateList(R.styleable.NavigationView_itemIconTint);
        } else {
            itemIconTint = createDefaultColorStateList(android.R.attr.textColorSecondary);
        }
        boolean textAppearanceSet = false;
        int textAppearance = 0;
        if (a.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
            textAppearance = a.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
            textAppearanceSet = true;
        }
        android.content.res.ColorStateList itemTextColor = null;
        if (a.hasValue(R.styleable.NavigationView_itemTextColor)) {
            itemTextColor = a.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        if ((!textAppearanceSet) && (itemTextColor == null)) {
            // If there isn't a text appearance set, we'll use a default text color
            itemTextColor = createDefaultColorStateList(android.R.attr.textColorPrimary);
        }
        final android.graphics.drawable.Drawable itemBackground = a.getDrawable(R.styleable.NavigationView_itemBackground);
        mMenu.setCallback(new android.support.v7.view.menu.MenuBuilder.Callback() {
            @java.lang.Override
            public boolean onMenuItemSelected(android.support.v7.view.menu.MenuBuilder menu, android.view.MenuItem item) {
                return (mListener != null) && mListener.onNavigationItemSelected(item);
            }

            @java.lang.Override
            public void onMenuModeChange(android.support.v7.view.menu.MenuBuilder menu) {
            }
        });
        mPresenter.setId(android.support.design.widget.NavigationView.PRESENTER_NAVIGATION_VIEW_ID);
        mPresenter.initForMenu(context, mMenu);
        mPresenter.setItemIconTintList(itemIconTint);
        if (textAppearanceSet) {
            mPresenter.setItemTextAppearance(textAppearance);
        }
        mPresenter.setItemTextColor(itemTextColor);
        mPresenter.setItemBackground(itemBackground);
        mMenu.addMenuPresenter(mPresenter);
        addView(((android.view.View) (mPresenter.getMenuView(this))));
        if (a.hasValue(R.styleable.NavigationView_menu)) {
            inflateMenu(a.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        if (a.hasValue(R.styleable.NavigationView_headerLayout)) {
            inflateHeaderView(a.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        a.recycle();
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.design.widget.NavigationView.SavedState state = new android.support.design.widget.NavigationView.SavedState(superState);
        state.menuState = new android.os.Bundle();
        mMenu.savePresenterStates(state.menuState);
        return state;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable savedState) {
        if (!(savedState instanceof android.support.design.widget.NavigationView.SavedState)) {
            super.onRestoreInstanceState(savedState);
            return;
        }
        android.support.design.widget.NavigationView.SavedState state = ((android.support.design.widget.NavigationView.SavedState) (savedState));
        super.onRestoreInstanceState(state.getSuperState());
        mMenu.restorePresenterStates(state.menuState);
    }

    /**
     * Set a listener that will be notified when a menu item is selected.
     *
     * @param listener
     * 		The listener to notify
     */
    public void setNavigationItemSelectedListener(@android.support.annotation.Nullable
    android.support.design.widget.NavigationView.OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    @java.lang.Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        switch (android.view.View.MeasureSpec.getMode(widthSpec)) {
            case android.view.View.MeasureSpec.EXACTLY :
                // Nothing to do
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.min(android.view.View.MeasureSpec.getSize(widthSpec), mMaxWidth), android.view.View.MeasureSpec.EXACTLY);
                break;
            case android.view.View.MeasureSpec.UNSPECIFIED :
                widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(mMaxWidth, android.view.View.MeasureSpec.EXACTLY);
                break;
        }
        // Let super sort out the height
        super.onMeasure(widthSpec, heightSpec);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    protected void onInsetsChanged(android.support.v4.view.WindowInsetsCompat insets) {
        mPresenter.dispatchApplyWindowInsets(insets);
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
        mPresenter.updateMenuView(false);
    }

    /**
     * Returns the {@link Menu} instance associated with this navigation view.
     */
    public android.view.Menu getMenu() {
        return mMenu;
    }

    /**
     * Inflates a View and add it as a header of the navigation menu.
     *
     * @param res
     * 		The layout resource ID.
     * @return a newly inflated View.
     */
    public android.view.View inflateHeaderView(@android.support.annotation.LayoutRes
    int res) {
        return mPresenter.inflateHeaderView(res);
    }

    /**
     * Adds a View as a header of the navigation menu.
     *
     * @param view
     * 		The view to be added as a header of the navigation menu.
     */
    public void addHeaderView(@android.support.annotation.NonNull
    android.view.View view) {
        mPresenter.addHeaderView(view);
    }

    /**
     * Removes a previously-added header view.
     *
     * @param view
     * 		The view to remove
     */
    public void removeHeaderView(@android.support.annotation.NonNull
    android.view.View view) {
        mPresenter.removeHeaderView(view);
    }

    /**
     * Gets the number of headers in this NavigationView.
     *
     * @return A positive integer representing the number of headers.
     */
    public int getHeaderCount() {
        return mPresenter.getHeaderCount();
    }

    /**
     * Gets the header view at the specified position.
     *
     * @param index
     * 		The position at which to get the view from.
     * @return The header view the specified position or null if the position does not exist in this
    NavigationView.
     */
    public android.view.View getHeaderView(int index) {
        return mPresenter.getHeaderView(index);
    }

    /**
     * Returns the tint which is applied to our menu items' icons.
     *
     * @see #setItemIconTintList(ColorStateList)
     * @unknown ref R.styleable#NavigationView_itemIconTint
     */
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemIconTintList() {
        return mPresenter.getItemTintList();
    }

    /**
     * Set the tint which is applied to our menu items' icons.
     *
     * @param tint
     * 		the tint to apply.
     * @unknown ref R.styleable#NavigationView_itemIconTint
     */
    public void setItemIconTintList(@android.support.annotation.Nullable
    android.content.res.ColorStateList tint) {
        mPresenter.setItemIconTintList(tint);
    }

    /**
     * Returns the tint which is applied to our menu items' icons.
     *
     * @see #setItemTextColor(ColorStateList)
     * @unknown ref R.styleable#NavigationView_itemTextColor
     */
    @android.support.annotation.Nullable
    public android.content.res.ColorStateList getItemTextColor() {
        return mPresenter.getItemTextColor();
    }

    /**
     * Set the text color to be used on our menu items.
     *
     * @see #getItemTextColor()
     * @unknown ref R.styleable#NavigationView_itemTextColor
     */
    public void setItemTextColor(@android.support.annotation.Nullable
    android.content.res.ColorStateList textColor) {
        mPresenter.setItemTextColor(textColor);
    }

    /**
     * Returns the background drawable for our menu items.
     *
     * @see #setItemBackgroundResource(int)
     * @unknown ref R.styleable#NavigationView_itemBackground
     */
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getItemBackground() {
        return mPresenter.getItemBackground();
    }

    /**
     * Set the background of our menu items to the given resource.
     *
     * @param resId
     * 		The identifier of the resource.
     * @unknown ref R.styleable#NavigationView_itemBackground
     */
    public void setItemBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        setItemBackground(android.support.v4.content.ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * Set the background of our menu items to a given resource. The resource should refer to
     * a Drawable object or null to use the default background set on this navigation menu.
     *
     * @unknown ref R.styleable#NavigationView_itemBackground
     */
    public void setItemBackground(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable itemBackground) {
        mPresenter.setItemBackground(itemBackground);
    }

    /**
     * Sets the currently checked item in this navigation menu.
     *
     * @param id
     * 		The item ID of the currently checked item.
     */
    public void setCheckedItem(@android.support.annotation.IdRes
    int id) {
        android.view.MenuItem item = mMenu.findItem(id);
        if (item != null) {
            mPresenter.setCheckedItem(((android.support.v7.view.menu.MenuItemImpl) (item)));
        }
    }

    /**
     * Set the text appearance of the menu items to a given resource.
     *
     * @unknown ref R.styleable#NavigationView_itemTextAppearance
     */
    public void setItemTextAppearance(@android.support.annotation.StyleRes
    int resId) {
        mPresenter.setItemTextAppearance(resId);
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
        return new android.content.res.ColorStateList(new int[][]{ android.support.design.widget.NavigationView.DISABLED_STATE_SET, android.support.design.widget.NavigationView.CHECKED_STATE_SET, android.view.View.EMPTY_STATE_SET }, new int[]{ baseColor.getColorForState(android.support.design.widget.NavigationView.DISABLED_STATE_SET, defaultColor), colorPrimary, defaultColor });
    }

    /**
     * Listener for handling events on navigation items.
     */
    public interface OnNavigationItemSelectedListener {
        /**
         * Called when an item in the navigation menu is selected.
         *
         * @param item
         * 		The selected item
         * @return true to display the item as the selected item
         */
        public boolean onNavigationItemSelected(@android.support.annotation.NonNull
        android.view.MenuItem item);
    }

    /**
     * User interface state that is stored by NavigationView for implementing
     * onSaveInstanceState().
     */
    public static class SavedState extends android.support.v4.view.AbsSavedState {
        public android.os.Bundle menuState;

        public SavedState(android.os.Parcel in, java.lang.ClassLoader loader) {
            super(in, loader);
            menuState = in.readBundle(loader);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(@android.support.annotation.NonNull
        android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(menuState);
        }

        public static final android.os.Parcelable.Creator<android.support.design.widget.NavigationView.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.widget.NavigationView.SavedState>() {
            @java.lang.Override
            public android.support.design.widget.NavigationView.SavedState createFromParcel(android.os.Parcel parcel, java.lang.ClassLoader loader) {
                return new android.support.design.widget.NavigationView.SavedState(parcel, loader);
            }

            @java.lang.Override
            public android.support.design.widget.NavigationView.SavedState[] newArray(int size) {
                return new android.support.design.widget.NavigationView.SavedState[size];
            }
        });
    }
}

