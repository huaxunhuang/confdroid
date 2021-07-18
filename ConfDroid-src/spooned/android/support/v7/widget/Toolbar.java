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
 * A standard toolbar for use within application content.
 *
 * <p>A Toolbar is a generalization of {@link ActionBar action bars} for use
 * within application layouts. While an action bar is traditionally part of an
 * {@link android.app.Activity Activity's} opaque window decor controlled by the framework,
 * a Toolbar may be placed at any arbitrary level of nesting within a view hierarchy.
 * An application may choose to designate a Toolbar as the action bar for an Activity
 * using the {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)
 * setSupportActionBar()} method.</p>
 *
 * <p>Toolbar supports a more focused feature set than ActionBar. From start to end, a toolbar
 * may contain a combination of the following optional elements:
 *
 * <ul>
 *     <li><em>A navigation button.</em> This may be an Up arrow, navigation menu toggle, close,
 *     collapse, done or another glyph of the app's choosing. This button should always be used
 *     to access other navigational destinations within the container of the Toolbar and
 *     its signified content or otherwise leave the current context signified by the Toolbar.
 *     The navigation button is vertically aligned within the Toolbar's minimum height,
 *     if set.</li>
 *     <li><em>A branded logo image.</em> This may extend to the height of the bar and can be
 *     arbitrarily wide.</li>
 *     <li><em>A title and subtitle.</em> The title should be a signpost for the Toolbar's current
 *     position in the navigation hierarchy and the content contained there. The subtitle,
 *     if present should indicate any extended information about the current content.
 *     If an app uses a logo image it should strongly consider omitting a title and subtitle.</li>
 *     <li><em>One or more custom views.</em> The application may add arbitrary child views
 *     to the Toolbar. They will appear at this position within the layout. If a child view's
 *     {@link LayoutParams} indicates a {@link Gravity} value of
 *     {@link Gravity#CENTER_HORIZONTAL CENTER_HORIZONTAL} the view will attempt to center
 *     within the available space remaining in the Toolbar after all other elements have been
 *     measured.</li>
 *     <li><em>An {@link ActionMenuView action menu}.</em> The menu of actions will pin to the
 *     end of the Toolbar offering a few
 *     <a href="http://developer.android.com/design/patterns/actionbar.html#ActionButtons">
 *     frequent, important or typical</a> actions along with an optional overflow menu for
 *     additional actions. Action buttons are vertically aligned within the Toolbar's
 *     minimum height, if set.</li>
 * </ul>
 * </p>
 *
 * <p>In modern Android UIs developers should lean more on a visually distinct color scheme for
 * toolbars than on their application icon. The use of application icon plus title as a standard
 * layout is discouraged on API 21 devices and newer.</p>
 *
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_buttonGravity
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_collapseContentDescription
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_collapseIcon
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEnd
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetLeft
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetRight
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStart
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStartWithNavigation
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEndWithActions
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_android_gravity
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_logo
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_logoDescription
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_maxButtonHeight
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationContentDescription
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationIcon
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_popupTheme
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_subtitle
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_subtitleTextAppearance
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_subtitleTextColor
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_title
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMargin
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginBottom
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginEnd
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginStart
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginTop
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleTextAppearance
 * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleTextColor
 */
public class Toolbar extends android.view.ViewGroup {
    private static final java.lang.String TAG = "Toolbar";

    private android.support.v7.widget.ActionMenuView mMenuView;

    private android.widget.TextView mTitleTextView;

    private android.widget.TextView mSubtitleTextView;

    private android.widget.ImageButton mNavButtonView;

    private android.widget.ImageView mLogoView;

    private android.graphics.drawable.Drawable mCollapseIcon;

    private java.lang.CharSequence mCollapseDescription;

    android.widget.ImageButton mCollapseButtonView;

    android.view.View mExpandedActionView;

    /**
     * Context against which to inflate popup menus.
     */
    private android.content.Context mPopupContext;

    /**
     * Theme resource against which to inflate popup menus.
     */
    private int mPopupTheme;

    private int mTitleTextAppearance;

    private int mSubtitleTextAppearance;

    int mButtonGravity;

    private int mMaxButtonHeight;

    private int mTitleMarginStart;

    private int mTitleMarginEnd;

    private int mTitleMarginTop;

    private int mTitleMarginBottom;

    private android.support.v7.widget.RtlSpacingHelper mContentInsets;

    private int mContentInsetStartWithNavigation;

    private int mContentInsetEndWithActions;

    private int mGravity = android.support.v4.view.GravityCompat.START | android.view.Gravity.CENTER_VERTICAL;

    private java.lang.CharSequence mTitleText;

    private java.lang.CharSequence mSubtitleText;

    private int mTitleTextColor;

    private int mSubtitleTextColor;

    private boolean mEatingTouch;

    private boolean mEatingHover;

    // Clear me after use.
    private final java.util.ArrayList<android.view.View> mTempViews = new java.util.ArrayList<android.view.View>();

    // Used to hold views that will be removed while we have an expanded action view.
    private final java.util.ArrayList<android.view.View> mHiddenViews = new java.util.ArrayList<>();

    private final int[] mTempMargins = new int[2];

    android.support.v7.widget.Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;

    private final android.support.v7.widget.ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new android.support.v7.widget.ActionMenuView.OnMenuItemClickListener() {
        @java.lang.Override
        public boolean onMenuItemClick(android.view.MenuItem item) {
            if (mOnMenuItemClickListener != null) {
                return mOnMenuItemClickListener.onMenuItemClick(item);
            }
            return false;
        }
    };

    private android.support.v7.widget.ToolbarWidgetWrapper mWrapper;

    private android.support.v7.widget.ActionMenuPresenter mOuterActionMenuPresenter;

    private android.support.v7.widget.Toolbar.ExpandedActionViewMenuPresenter mExpandedMenuPresenter;

    private android.support.v7.view.menu.MenuPresenter.Callback mActionMenuPresenterCallback;

    private android.support.v7.view.menu.MenuBuilder.Callback mMenuBuilderCallback;

    private boolean mCollapsible;

    private final java.lang.Runnable mShowOverflowMenuRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            showOverflowMenu();
        }
    };

    public Toolbar(android.content.Context context) {
        this(context, null);
    }

    public Toolbar(android.content.Context context, @android.support.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public Toolbar(android.content.Context context, @android.support.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Need to use getContext() here so that we use the themed context
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.Toolbar, defStyleAttr, 0);
        mTitleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        mSubtitleTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        mGravity = a.getInteger(R.styleable.Toolbar_android_gravity, mGravity);
        mButtonGravity = a.getInteger(R.styleable.Toolbar_buttonGravity, android.view.Gravity.TOP);
        // First read the correct attribute
        int titleMargin = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
        if (a.hasValue(R.styleable.Toolbar_titleMargins)) {
            // Now read the deprecated attribute, if it has a value
            titleMargin = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, titleMargin);
        }
        mTitleMarginStart = mTitleMarginEnd = mTitleMarginTop = mTitleMarginBottom = titleMargin;
        final int marginStart = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (marginStart >= 0) {
            mTitleMarginStart = marginStart;
        }
        final int marginEnd = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
        if (marginEnd >= 0) {
            mTitleMarginEnd = marginEnd;
        }
        final int marginTop = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
        if (marginTop >= 0) {
            mTitleMarginTop = marginTop;
        }
        final int marginBottom = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
        if (marginBottom >= 0) {
            mTitleMarginBottom = marginBottom;
        }
        mMaxButtonHeight = a.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        final int contentInsetStart = a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, android.support.v7.widget.RtlSpacingHelper.UNDEFINED);
        final int contentInsetEnd = a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, android.support.v7.widget.RtlSpacingHelper.UNDEFINED);
        final int contentInsetLeft = a.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
        final int contentInsetRight = a.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
        if ((contentInsetStart != android.support.v7.widget.RtlSpacingHelper.UNDEFINED) || (contentInsetEnd != android.support.v7.widget.RtlSpacingHelper.UNDEFINED)) {
            mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
        }
        mContentInsetStartWithNavigation = a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, android.support.v7.widget.RtlSpacingHelper.UNDEFINED);
        mContentInsetEndWithActions = a.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, android.support.v7.widget.RtlSpacingHelper.UNDEFINED);
        mCollapseIcon = a.getDrawable(R.styleable.Toolbar_collapseIcon);
        mCollapseDescription = a.getText(R.styleable.Toolbar_collapseContentDescription);
        final java.lang.CharSequence title = a.getText(R.styleable.Toolbar_title);
        if (!android.text.TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        final java.lang.CharSequence subtitle = a.getText(R.styleable.Toolbar_subtitle);
        if (!android.text.TextUtils.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }
        // Set the default context, since setPopupTheme() may be a no-op.
        mPopupContext = getContext();
        setPopupTheme(a.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        final android.graphics.drawable.Drawable navIcon = a.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (navIcon != null) {
            setNavigationIcon(navIcon);
        }
        final java.lang.CharSequence navDesc = a.getText(R.styleable.Toolbar_navigationContentDescription);
        if (!android.text.TextUtils.isEmpty(navDesc)) {
            setNavigationContentDescription(navDesc);
        }
        final android.graphics.drawable.Drawable logo = a.getDrawable(R.styleable.Toolbar_logo);
        if (logo != null) {
            setLogo(logo);
        }
        final java.lang.CharSequence logoDesc = a.getText(R.styleable.Toolbar_logoDescription);
        if (!android.text.TextUtils.isEmpty(logoDesc)) {
            setLogoDescription(logoDesc);
        }
        if (a.hasValue(R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(a.getColor(R.styleable.Toolbar_titleTextColor, 0xffffffff));
        }
        if (a.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(a.getColor(R.styleable.Toolbar_subtitleTextColor, 0xffffffff));
        }
        a.recycle();
    }

    /**
     * Specifies the theme to use when inflating popup menus. By default, uses
     * the same theme as the toolbar itself.
     *
     * @param resId
     * 		theme used to inflate popup menus
     * @see #getPopupTheme()
     */
    public void setPopupTheme(@android.support.annotation.StyleRes
    int resId) {
        if (mPopupTheme != resId) {
            mPopupTheme = resId;
            if (resId == 0) {
                mPopupContext = getContext();
            } else {
                mPopupContext = new android.view.ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    /**
     *
     *
     * @return resource identifier of the theme used to inflate popup menus, or
    0 if menus are inflated against the toolbar theme
     * @see #setPopupTheme(int)
     */
    public int getPopupTheme() {
        return mPopupTheme;
    }

    /**
     * Sets the title margin.
     *
     * @param start
     * 		the starting title margin in pixels
     * @param top
     * 		the top title margin in pixels
     * @param end
     * 		the ending title margin in pixels
     * @param bottom
     * 		the bottom title margin in pixels
     * @see #getTitleMarginStart()
     * @see #getTitleMarginTop()
     * @see #getTitleMarginEnd()
     * @see #getTitleMarginBottom()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMargin
     */
    public void setTitleMargin(int start, int top, int end, int bottom) {
        mTitleMarginStart = start;
        mTitleMarginTop = top;
        mTitleMarginEnd = end;
        mTitleMarginBottom = bottom;
        requestLayout();
    }

    /**
     *
     *
     * @return the starting title margin in pixels
     * @see #setTitleMarginStart(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginStart
     */
    public int getTitleMarginStart() {
        return mTitleMarginStart;
    }

    /**
     * Sets the starting title margin in pixels.
     *
     * @param margin
     * 		the starting title margin in pixels
     * @see #getTitleMarginStart()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginStart
     */
    public void setTitleMarginStart(int margin) {
        mTitleMarginStart = margin;
        requestLayout();
    }

    /**
     *
     *
     * @return the top title margin in pixels
     * @see #setTitleMarginTop(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginTop
     */
    public int getTitleMarginTop() {
        return mTitleMarginTop;
    }

    /**
     * Sets the top title margin in pixels.
     *
     * @param margin
     * 		the top title margin in pixels
     * @see #getTitleMarginTop()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginTop
     */
    public void setTitleMarginTop(int margin) {
        mTitleMarginTop = margin;
        requestLayout();
    }

    /**
     *
     *
     * @return the ending title margin in pixels
     * @see #setTitleMarginEnd(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginEnd
     */
    public int getTitleMarginEnd() {
        return mTitleMarginEnd;
    }

    /**
     * Sets the ending title margin in pixels.
     *
     * @param margin
     * 		the ending title margin in pixels
     * @see #getTitleMarginEnd()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginEnd
     */
    public void setTitleMarginEnd(int margin) {
        mTitleMarginEnd = margin;
        requestLayout();
    }

    /**
     *
     *
     * @return the bottom title margin in pixels
     * @see #setTitleMarginBottom(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginBottom
     */
    public int getTitleMarginBottom() {
        return mTitleMarginBottom;
    }

    /**
     * Sets the bottom title margin in pixels.
     *
     * @param margin
     * 		the bottom title margin in pixels
     * @see #getTitleMarginBottom()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_titleMarginBottom
     */
    public void setTitleMarginBottom(int margin) {
        mTitleMarginBottom = margin;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(layoutDirection);
        }
        ensureContentInsets();
        mContentInsets.setDirection(layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL);
    }

    /**
     * Set a logo drawable from a resource id.
     *
     * <p>This drawable should generally take the place of title text. The logo cannot be
     * clicked. Apps using a logo should also supply a description using
     * {@link #setLogoDescription(int)}.</p>
     *
     * @param resId
     * 		ID of a drawable resource
     */
    public void setLogo(@android.support.annotation.DrawableRes
    int resId) {
        setLogo(android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId));
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public boolean canShowOverflowMenu() {
        return ((getVisibility() == android.view.View.VISIBLE) && (mMenuView != null)) && mMenuView.isOverflowReserved();
    }

    /**
     * Check whether the overflow menu is currently showing. This may not reflect
     * a pending show operation in progress.
     *
     * @return true if the overflow menu is currently showing
     */
    public boolean isOverflowMenuShowing() {
        return (mMenuView != null) && mMenuView.isOverflowMenuShowing();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public boolean isOverflowMenuShowPending() {
        return (mMenuView != null) && mMenuView.isOverflowMenuShowPending();
    }

    /**
     * Show the overflow items from the associated menu.
     *
     * @return true if the menu was able to be shown, false otherwise
     */
    public boolean showOverflowMenu() {
        return (mMenuView != null) && mMenuView.showOverflowMenu();
    }

    /**
     * Hide the overflow items from the associated menu.
     *
     * @return true if the menu was able to be hidden, false otherwise
     */
    public boolean hideOverflowMenu() {
        return (mMenuView != null) && mMenuView.hideOverflowMenu();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setMenu(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.widget.ActionMenuPresenter outerPresenter) {
        if ((menu == null) && (mMenuView == null)) {
            return;
        }
        ensureMenuView();
        final android.support.v7.view.menu.MenuBuilder oldMenu = mMenuView.peekMenu();
        if (oldMenu == menu) {
            return;
        }
        if (oldMenu != null) {
            oldMenu.removeMenuPresenter(mOuterActionMenuPresenter);
            oldMenu.removeMenuPresenter(mExpandedMenuPresenter);
        }
        if (mExpandedMenuPresenter == null) {
            mExpandedMenuPresenter = new android.support.v7.widget.Toolbar.ExpandedActionViewMenuPresenter();
        }
        outerPresenter.setExpandedActionViewsExclusive(true);
        if (menu != null) {
            menu.addMenuPresenter(outerPresenter, mPopupContext);
            menu.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
        } else {
            outerPresenter.initForMenu(mPopupContext, null);
            mExpandedMenuPresenter.initForMenu(mPopupContext, null);
            outerPresenter.updateMenuView(true);
            mExpandedMenuPresenter.updateMenuView(true);
        }
        mMenuView.setPopupTheme(mPopupTheme);
        mMenuView.setPresenter(outerPresenter);
        mOuterActionMenuPresenter = outerPresenter;
    }

    /**
     * Dismiss all currently showing popup menus, including overflow or submenus.
     */
    public void dismissPopupMenus() {
        if (mMenuView != null) {
            mMenuView.dismissPopupMenus();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public boolean isTitleTruncated() {
        if (mTitleTextView == null) {
            return false;
        }
        final android.text.Layout titleLayout = mTitleTextView.getLayout();
        if (titleLayout == null) {
            return false;
        }
        final int lineCount = titleLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (titleLayout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set a logo drawable.
     *
     * <p>This drawable should generally take the place of title text. The logo cannot be
     * clicked. Apps using a logo should also supply a description using
     * {@link #setLogoDescription(int)}.</p>
     *
     * @param drawable
     * 		Drawable to use as a logo
     */
    public void setLogo(android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(mLogoView)) {
                addSystemView(mLogoView, true);
            }
        } else
            if ((mLogoView != null) && isChildOrHidden(mLogoView)) {
                removeView(mLogoView);
                mHiddenViews.remove(mLogoView);
            }

        if (mLogoView != null) {
            mLogoView.setImageDrawable(drawable);
        }
    }

    /**
     * Return the current logo drawable.
     *
     * @return The current logo drawable
     * @see #setLogo(int)
     * @see #setLogo(android.graphics.drawable.Drawable)
     */
    public android.graphics.drawable.Drawable getLogo() {
        return mLogoView != null ? mLogoView.getDrawable() : null;
    }

    /**
     * Set a description of the toolbar's logo.
     *
     * <p>This description will be used for accessibility or other similar descriptions
     * of the UI.</p>
     *
     * @param resId
     * 		String resource id
     */
    public void setLogoDescription(@android.support.annotation.StringRes
    int resId) {
        setLogoDescription(getContext().getText(resId));
    }

    /**
     * Set a description of the toolbar's logo.
     *
     * <p>This description will be used for accessibility or other similar descriptions
     * of the UI.</p>
     *
     * @param description
     * 		Description to set
     */
    public void setLogoDescription(java.lang.CharSequence description) {
        if (!android.text.TextUtils.isEmpty(description)) {
            ensureLogoView();
        }
        if (mLogoView != null) {
            mLogoView.setContentDescription(description);
        }
    }

    /**
     * Return the description of the toolbar's logo.
     *
     * @return A description of the logo
     */
    public java.lang.CharSequence getLogoDescription() {
        return mLogoView != null ? mLogoView.getContentDescription() : null;
    }

    private void ensureLogoView() {
        if (mLogoView == null) {
            mLogoView = new android.support.v7.widget.AppCompatImageView(getContext());
        }
    }

    /**
     * Check whether this Toolbar is currently hosting an expanded action view.
     *
     * <p>An action view may be expanded either directly from the
     * {@link android.view.MenuItem MenuItem} it belongs to or by user action. If the Toolbar
     * has an expanded action view it can be collapsed using the {@link #collapseActionView()}
     * method.</p>
     *
     * @return true if the Toolbar has an expanded action view
     */
    public boolean hasExpandedActionView() {
        return (mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null);
    }

    /**
     * Collapse a currently expanded action view. If this Toolbar does not have an
     * expanded action view this method has no effect.
     *
     * <p>An action view may be expanded either directly from the
     * {@link android.view.MenuItem MenuItem} it belongs to or by user action.</p>
     *
     * @see #hasExpandedActionView()
     */
    public void collapseActionView() {
        final android.support.v7.view.menu.MenuItemImpl item = (mExpandedMenuPresenter == null) ? null : mExpandedMenuPresenter.mCurrentExpandedItem;
        if (item != null) {
            item.collapseActionView();
        }
    }

    /**
     * Returns the title of this toolbar.
     *
     * @return The current title.
     */
    public java.lang.CharSequence getTitle() {
        return mTitleText;
    }

    /**
     * Set the title of this toolbar.
     *
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param resId
     * 		Resource ID of a string to set as the title
     */
    public void setTitle(@android.support.annotation.StringRes
    int resId) {
        setTitle(getContext().getText(resId));
    }

    /**
     * Set the title of this toolbar.
     *
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param title
     * 		Title to set
     */
    public void setTitle(java.lang.CharSequence title) {
        if (!android.text.TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                final android.content.Context context = getContext();
                mTitleTextView = new android.support.v7.widget.AppCompatTextView(context);
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                if (mTitleTextAppearance != 0) {
                    mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                }
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
            }
            if (!isChildOrHidden(mTitleTextView)) {
                addSystemView(mTitleTextView, true);
            }
        } else
            if ((mTitleTextView != null) && isChildOrHidden(mTitleTextView)) {
                removeView(mTitleTextView);
                mHiddenViews.remove(mTitleTextView);
            }

        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    /**
     * Return the subtitle of this toolbar.
     *
     * @return The current subtitle
     */
    public java.lang.CharSequence getSubtitle() {
        return mSubtitleText;
    }

    /**
     * Set the subtitle of this toolbar.
     *
     * <p>Subtitles should express extended information about the current content.</p>
     *
     * @param resId
     * 		String resource ID
     */
    public void setSubtitle(@android.support.annotation.StringRes
    int resId) {
        setSubtitle(getContext().getText(resId));
    }

    /**
     * Set the subtitle of this toolbar.
     *
     * <p>Subtitles should express extended information about the current content.</p>
     *
     * @param subtitle
     * 		Subtitle to set
     */
    public void setSubtitle(java.lang.CharSequence subtitle) {
        if (!android.text.TextUtils.isEmpty(subtitle)) {
            if (mSubtitleTextView == null) {
                final android.content.Context context = getContext();
                mSubtitleTextView = new android.support.v7.widget.AppCompatTextView(context);
                mSubtitleTextView.setSingleLine();
                mSubtitleTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                if (mSubtitleTextAppearance != 0) {
                    mSubtitleTextView.setTextAppearance(context, mSubtitleTextAppearance);
                }
                if (mSubtitleTextColor != 0) {
                    mSubtitleTextView.setTextColor(mSubtitleTextColor);
                }
            }
            if (!isChildOrHidden(mSubtitleTextView)) {
                addSystemView(mSubtitleTextView, true);
            }
        } else
            if ((mSubtitleTextView != null) && isChildOrHidden(mSubtitleTextView)) {
                removeView(mSubtitleTextView);
                mHiddenViews.remove(mSubtitleTextView);
            }

        if (mSubtitleTextView != null) {
            mSubtitleTextView.setText(subtitle);
        }
        mSubtitleText = subtitle;
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     */
    public void setTitleTextAppearance(android.content.Context context, @android.support.annotation.StyleRes
    int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     */
    public void setSubtitleTextAppearance(android.content.Context context, @android.support.annotation.StyleRes
    int resId) {
        mSubtitleTextAppearance = resId;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextAppearance(context, resId);
        }
    }

    /**
     * Sets the text color of the title, if present.
     *
     * @param color
     * 		The new text color in 0xAARRGGBB format
     */
    public void setTitleTextColor(@android.support.annotation.ColorInt
    int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    /**
     * Sets the text color of the subtitle, if present.
     *
     * @param color
     * 		The new text color in 0xAARRGGBB format
     */
    public void setSubtitleTextColor(@android.support.annotation.ColorInt
    int color) {
        mSubtitleTextColor = color;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(color);
        }
    }

    /**
     * Retrieve the currently configured content description for the navigation button view.
     * This will be used to describe the navigation action to users through mechanisms such
     * as screen readers or tooltips.
     *
     * @return The navigation button's content description
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationContentDescription
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getNavigationContentDescription() {
        return mNavButtonView != null ? mNavButtonView.getContentDescription() : null;
    }

    /**
     * Set a content description for the navigation button if one is present. The content
     * description will be read via screen readers or other accessibility systems to explain
     * the action of the navigation button.
     *
     * @param resId
     * 		Resource ID of a content description string to set, or 0 to
     * 		clear the description
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationContentDescription
     */
    public void setNavigationContentDescription(@android.support.annotation.StringRes
    int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    /**
     * Set a content description for the navigation button if one is present. The content
     * description will be read via screen readers or other accessibility systems to explain
     * the action of the navigation button.
     *
     * @param description
     * 		Content description to set, or <code>null</code> to
     * 		clear the content description
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationContentDescription
     */
    public void setNavigationContentDescription(@android.support.annotation.Nullable
    java.lang.CharSequence description) {
        if (!android.text.TextUtils.isEmpty(description)) {
            ensureNavButtonView();
        }
        if (mNavButtonView != null) {
            mNavButtonView.setContentDescription(description);
        }
    }

    /**
     * Set the icon to use for the toolbar's navigation button.
     *
     * <p>The navigation button appears at the start of the toolbar if present. Setting an icon
     * will make the navigation button visible.</p>
     *
     * <p>If you use a navigation icon you should also set a description for its action using
     * {@link #setNavigationContentDescription(int)}. This is used for accessibility and
     * tooltips.</p>
     *
     * @param resId
     * 		Resource ID of a drawable to set
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationIcon
     */
    public void setNavigationIcon(@android.support.annotation.DrawableRes
    int resId) {
        setNavigationIcon(android.support.v7.content.res.AppCompatResources.getDrawable(getContext(), resId));
    }

    /**
     * Set the icon to use for the toolbar's navigation button.
     *
     * <p>The navigation button appears at the start of the toolbar if present. Setting an icon
     * will make the navigation button visible.</p>
     *
     * <p>If you use a navigation icon you should also set a description for its action using
     * {@link #setNavigationContentDescription(int)}. This is used for accessibility and
     * tooltips.</p>
     *
     * @param icon
     * 		Drawable to set, may be null to clear the icon
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationIcon
     */
    public void setNavigationIcon(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable icon) {
        if (icon != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(mNavButtonView)) {
                addSystemView(mNavButtonView, true);
            }
        } else
            if ((mNavButtonView != null) && isChildOrHidden(mNavButtonView)) {
                removeView(mNavButtonView);
                mHiddenViews.remove(mNavButtonView);
            }

        if (mNavButtonView != null) {
            mNavButtonView.setImageDrawable(icon);
        }
    }

    /**
     * Return the current drawable used as the navigation icon.
     *
     * @return The navigation icon drawable
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_navigationIcon
     */
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getNavigationIcon() {
        return mNavButtonView != null ? mNavButtonView.getDrawable() : null;
    }

    /**
     * Set a listener to respond to navigation events.
     *
     * <p>This listener will be called whenever the user clicks the navigation button
     * at the start of the toolbar. An icon must be set for the navigation button to appear.</p>
     *
     * @param listener
     * 		Listener to set
     * @see #setNavigationIcon(android.graphics.drawable.Drawable)
     */
    public void setNavigationOnClickListener(android.view.View.OnClickListener listener) {
        ensureNavButtonView();
        mNavButtonView.setOnClickListener(listener);
    }

    /**
     * Return the Menu shown in the toolbar.
     *
     * <p>Applications that wish to populate the toolbar's menu can do so from here. To use
     * an XML menu resource, use {@link #inflateMenu(int)}.</p>
     *
     * @return The toolbar's Menu
     */
    public android.view.Menu getMenu() {
        ensureMenu();
        return mMenuView.getMenu();
    }

    /**
     * Set the icon to use for the overflow button.
     *
     * @param icon
     * 		Drawable to set, may be null to clear the icon
     */
    public void setOverflowIcon(@android.support.annotation.Nullable
    android.graphics.drawable.Drawable icon) {
        ensureMenu();
        mMenuView.setOverflowIcon(icon);
    }

    /**
     * Return the current drawable used as the overflow icon.
     *
     * @return The overflow icon drawable
     */
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getOverflowIcon() {
        ensureMenu();
        return mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (mMenuView.peekMenu() == null) {
            // Initialize a new menu for the first time.
            final android.support.v7.view.menu.MenuBuilder menu = ((android.support.v7.view.menu.MenuBuilder) (mMenuView.getMenu()));
            if (mExpandedMenuPresenter == null) {
                mExpandedMenuPresenter = new android.support.v7.widget.Toolbar.ExpandedActionViewMenuPresenter();
            }
            mMenuView.setExpandedActionViewsExclusive(true);
            menu.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (mMenuView == null) {
            mMenuView = new android.support.v7.widget.ActionMenuView(getContext());
            mMenuView.setPopupTheme(mPopupTheme);
            mMenuView.setOnMenuItemClickListener(mMenuViewItemClickListener);
            mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
            final android.support.v7.widget.Toolbar.LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = android.support.v4.view.GravityCompat.END | (mButtonGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK);
            mMenuView.setLayoutParams(lp);
            addSystemView(mMenuView, false);
        }
    }

    private android.view.MenuInflater getMenuInflater() {
        return new android.support.v7.view.SupportMenuInflater(getContext());
    }

    /**
     * Inflate a menu resource into this toolbar.
     *
     * <p>Inflate an XML menu resource into this toolbar. Existing items in the menu will not
     * be modified or removed.</p>
     *
     * @param resId
     * 		ID of a menu resource to inflate
     */
    public void inflateMenu(@android.support.annotation.MenuRes
    int resId) {
        getMenuInflater().inflate(resId, getMenu());
    }

    /**
     * Set a listener to respond to menu item click events.
     *
     * <p>This listener will be invoked whenever a user selects a menu item from
     * the action buttons presented at the end of the toolbar or the associated overflow.</p>
     *
     * @param listener
     * 		Listener to set
     */
    public void setOnMenuItemClickListener(android.support.v7.widget.Toolbar.OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    /**
     * Sets the content insets for this toolbar relative to layout direction.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @param contentInsetStart
     * 		Content inset for the toolbar starting edge
     * @param contentInsetEnd
     * 		Content inset for the toolbar ending edge
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetStart()
     * @see #getContentInsetEnd()
     * @see #getContentInsetLeft()
     * @see #getContentInsetRight()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEnd
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStart
     */
    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        ensureContentInsets();
        mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
    }

    /**
     * Gets the starting content inset for this toolbar.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @return The starting content inset for this toolbar
     * @see #setContentInsetsRelative(int, int)
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetEnd()
     * @see #getContentInsetLeft()
     * @see #getContentInsetRight()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStart
     */
    public int getContentInsetStart() {
        return mContentInsets != null ? mContentInsets.getStart() : 0;
    }

    /**
     * Gets the ending content inset for this toolbar.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @return The ending content inset for this toolbar
     * @see #setContentInsetsRelative(int, int)
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetStart()
     * @see #getContentInsetLeft()
     * @see #getContentInsetRight()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEnd
     */
    public int getContentInsetEnd() {
        return mContentInsets != null ? mContentInsets.getEnd() : 0;
    }

    /**
     * Sets the content insets for this toolbar.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @param contentInsetLeft
     * 		Content inset for the toolbar's left edge
     * @param contentInsetRight
     * 		Content inset for the toolbar's right edge
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetStart()
     * @see #getContentInsetEnd()
     * @see #getContentInsetLeft()
     * @see #getContentInsetRight()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetLeft
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetRight
     */
    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        ensureContentInsets();
        mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
    }

    /**
     * Gets the left content inset for this toolbar.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @return The left content inset for this toolbar
     * @see #setContentInsetsRelative(int, int)
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetStart()
     * @see #getContentInsetEnd()
     * @see #getContentInsetRight()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetLeft
     */
    public int getContentInsetLeft() {
        return mContentInsets != null ? mContentInsets.getLeft() : 0;
    }

    /**
     * Gets the right content inset for this toolbar.
     *
     * <p>The content inset affects the valid area for Toolbar content other than
     * the navigation button and menu. Insets define the minimum margin for these components
     * and can be used to effectively align Toolbar content along well-known gridlines.</p>
     *
     * @return The right content inset for this toolbar
     * @see #setContentInsetsRelative(int, int)
     * @see #setContentInsetsAbsolute(int, int)
     * @see #getContentInsetStart()
     * @see #getContentInsetEnd()
     * @see #getContentInsetLeft()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetRight
     */
    public int getContentInsetRight() {
        return mContentInsets != null ? mContentInsets.getRight() : 0;
    }

    /**
     * Gets the start content inset to use when a navigation button is present.
     *
     * <p>Different content insets are often called for when additional buttons are present
     * in the toolbar, as well as at different toolbar sizes. The larger value of
     * {@link #getContentInsetStart()} and this value will be used during layout.</p>
     *
     * @return the start content inset used when a navigation icon has been set in pixels
     * @see #setContentInsetStartWithNavigation(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStartWithNavigation
     */
    public int getContentInsetStartWithNavigation() {
        return mContentInsetStartWithNavigation != android.support.v7.widget.RtlSpacingHelper.UNDEFINED ? mContentInsetStartWithNavigation : getContentInsetStart();
    }

    /**
     * Sets the start content inset to use when a navigation button is present.
     *
     * <p>Different content insets are often called for when additional buttons are present
     * in the toolbar, as well as at different toolbar sizes. The larger value of
     * {@link #getContentInsetStart()} and this value will be used during layout.</p>
     *
     * @param insetStartWithNavigation
     * 		the inset to use when a navigation icon has been set
     * 		in pixels
     * @see #getContentInsetStartWithNavigation()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetStartWithNavigation
     */
    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        if (insetStartWithNavigation < 0) {
            insetStartWithNavigation = android.support.v7.widget.RtlSpacingHelper.UNDEFINED;
        }
        if (insetStartWithNavigation != mContentInsetStartWithNavigation) {
            mContentInsetStartWithNavigation = insetStartWithNavigation;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    /**
     * Gets the end content inset to use when action buttons are present.
     *
     * <p>Different content insets are often called for when additional buttons are present
     * in the toolbar, as well as at different toolbar sizes. The larger value of
     * {@link #getContentInsetEnd()} and this value will be used during layout.</p>
     *
     * @return the end content inset used when a menu has been set in pixels
     * @see #setContentInsetEndWithActions(int)
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEndWithActions
     */
    public int getContentInsetEndWithActions() {
        return mContentInsetEndWithActions != android.support.v7.widget.RtlSpacingHelper.UNDEFINED ? mContentInsetEndWithActions : getContentInsetEnd();
    }

    /**
     * Sets the start content inset to use when action buttons are present.
     *
     * <p>Different content insets are often called for when additional buttons are present
     * in the toolbar, as well as at different toolbar sizes. The larger value of
     * {@link #getContentInsetEnd()} and this value will be used during layout.</p>
     *
     * @param insetEndWithActions
     * 		the inset to use when a menu has been set in pixels
     * @see #getContentInsetEndWithActions()
     * @unknown ref android.support.v7.appcompat.R.styleable#Toolbar_contentInsetEndWithActions
     */
    public void setContentInsetEndWithActions(int insetEndWithActions) {
        if (insetEndWithActions < 0) {
            insetEndWithActions = android.support.v7.widget.RtlSpacingHelper.UNDEFINED;
        }
        if (insetEndWithActions != mContentInsetEndWithActions) {
            mContentInsetEndWithActions = insetEndWithActions;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    /**
     * Gets the content inset that will be used on the starting side of the bar in the current
     * toolbar configuration.
     *
     * @return the current content inset start in pixels
     * @see #getContentInsetStartWithNavigation()
     */
    public int getCurrentContentInsetStart() {
        return getNavigationIcon() != null ? java.lang.Math.max(getContentInsetStart(), java.lang.Math.max(mContentInsetStartWithNavigation, 0)) : getContentInsetStart();
    }

    /**
     * Gets the content inset that will be used on the ending side of the bar in the current
     * toolbar configuration.
     *
     * @return the current content inset end in pixels
     * @see #getContentInsetEndWithActions()
     */
    public int getCurrentContentInsetEnd() {
        boolean hasActions = false;
        if (mMenuView != null) {
            final android.support.v7.view.menu.MenuBuilder mb = mMenuView.peekMenu();
            hasActions = (mb != null) && mb.hasVisibleItems();
        }
        return hasActions ? java.lang.Math.max(getContentInsetEnd(), java.lang.Math.max(mContentInsetEndWithActions, 0)) : getContentInsetEnd();
    }

    /**
     * Gets the content inset that will be used on the left side of the bar in the current
     * toolbar configuration.
     *
     * @return the current content inset left in pixels
     * @see #getContentInsetStartWithNavigation()
     * @see #getContentInsetEndWithActions()
     */
    public int getCurrentContentInsetLeft() {
        return android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
    }

    /**
     * Gets the content inset that will be used on the right side of the bar in the current
     * toolbar configuration.
     *
     * @return the current content inset right in pixels
     * @see #getContentInsetStartWithNavigation()
     * @see #getContentInsetEndWithActions()
     */
    public int getCurrentContentInsetRight() {
        return android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (mNavButtonView == null) {
            mNavButtonView = new android.support.v7.widget.AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
            final android.support.v7.widget.Toolbar.LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = android.support.v4.view.GravityCompat.START | (mButtonGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK);
            mNavButtonView.setLayoutParams(lp);
        }
    }

    void ensureCollapseButtonView() {
        if (mCollapseButtonView == null) {
            mCollapseButtonView = new android.support.v7.widget.AppCompatImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
            mCollapseButtonView.setImageDrawable(mCollapseIcon);
            mCollapseButtonView.setContentDescription(mCollapseDescription);
            final android.support.v7.widget.Toolbar.LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = android.support.v4.view.GravityCompat.START | (mButtonGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK);
            lp.mViewType = android.support.v7.widget.Toolbar.LayoutParams.EXPANDED;
            mCollapseButtonView.setLayoutParams(lp);
            mCollapseButtonView.setOnClickListener(new android.view.View.OnClickListener() {
                @java.lang.Override
                public void onClick(android.view.View v) {
                    collapseActionView();
                }
            });
        }
    }

    private void addSystemView(android.view.View v, boolean allowHide) {
        final android.view.ViewGroup.LayoutParams vlp = v.getLayoutParams();
        final android.support.v7.widget.Toolbar.LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else
            if (!checkLayoutParams(vlp)) {
                lp = generateLayoutParams(vlp);
            } else {
                lp = ((android.support.v7.widget.Toolbar.LayoutParams) (vlp));
            }

        lp.mViewType = android.support.v7.widget.Toolbar.LayoutParams.SYSTEM;
        if (allowHide && (mExpandedActionView != null)) {
            v.setLayoutParams(lp);
            mHiddenViews.add(v);
        } else {
            addView(v, lp);
        }
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.support.v7.widget.Toolbar.SavedState state = new android.support.v7.widget.Toolbar.SavedState(super.onSaveInstanceState());
        if ((mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
            state.expandedMenuItemId = mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v7.widget.Toolbar.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final android.support.v7.widget.Toolbar.SavedState ss = ((android.support.v7.widget.Toolbar.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        final android.view.Menu menu = (mMenuView != null) ? mMenuView.peekMenu() : null;
        if (((ss.expandedMenuItemId != 0) && (mExpandedMenuPresenter != null)) && (menu != null)) {
            final android.view.MenuItem item = menu.findItem(ss.expandedMenuItemId);
            if (item != null) {
                android.support.v4.view.MenuItemCompat.expandActionView(item);
            }
        }
        if (ss.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    private void postShowOverflowMenu() {
        removeCallbacks(mShowOverflowMenuRunnable);
        post(mShowOverflowMenuRunnable);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mShowOverflowMenuRunnable);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        // Toolbars always eat touch events, but should still respect the touch event dispatch
        // contract. If the normal View implementation doesn't want the events, we'll just silently
        // eat the rest of the gesture without reporting the events to the default implementation
        // since that's what it expects.
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            mEatingTouch = false;
        }
        if (!mEatingTouch) {
            final boolean handled = super.onTouchEvent(ev);
            if ((action == android.view.MotionEvent.ACTION_DOWN) && (!handled)) {
                mEatingTouch = true;
            }
        }
        if ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
            mEatingTouch = false;
        }
        return true;
    }

    @java.lang.Override
    public boolean onHoverEvent(android.view.MotionEvent ev) {
        // Same deal as onTouchEvent() above. Eat all hover events, but still
        // respect the touch event dispatch contract.
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        if (action == android.view.MotionEvent.ACTION_HOVER_ENTER) {
            mEatingHover = false;
        }
        if (!mEatingHover) {
            final boolean handled = super.onHoverEvent(ev);
            if ((action == android.view.MotionEvent.ACTION_HOVER_ENTER) && (!handled)) {
                mEatingHover = true;
            }
        }
        if ((action == android.view.MotionEvent.ACTION_HOVER_EXIT) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
            mEatingHover = false;
        }
        return true;
    }

    private void measureChildConstrained(android.view.View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed, int heightConstraint) {
        final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
        int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthSpec, (((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(parentHeightSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height);
        final int childHeightMode = android.view.View.MeasureSpec.getMode(childHeightSpec);
        if ((childHeightMode != android.view.View.MeasureSpec.EXACTLY) && (heightConstraint >= 0)) {
            final int size = (childHeightMode != android.view.View.MeasureSpec.UNSPECIFIED) ? java.lang.Math.min(android.view.View.MeasureSpec.getSize(childHeightSpec), heightConstraint) : heightConstraint;
            childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(size, android.view.View.MeasureSpec.EXACTLY);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * Returns the width + uncollapsed margins
     */
    private int measureChildCollapseMargins(android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
        final int leftDiff = lp.leftMargin - collapsingMargins[0];
        final int rightDiff = lp.rightMargin - collapsingMargins[1];
        final int leftMargin = java.lang.Math.max(0, leftDiff);
        final int rightMargin = java.lang.Math.max(0, rightDiff);
        final int hMargins = leftMargin + rightMargin;
        collapsingMargins[0] = java.lang.Math.max(0, -leftDiff);
        collapsingMargins[1] = java.lang.Math.max(0, -rightDiff);
        final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, ((getPaddingLeft() + getPaddingRight()) + hMargins) + widthUsed, lp.width);
        final int childHeightMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        return child.getMeasuredWidth() + hMargins;
    }

    /**
     * Returns true if the Toolbar is collapsible and has no child views with a measured size > 0.
     */
    private boolean shouldCollapse() {
        if (!mCollapsible)
            return false;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if ((shouldLayout(child) && (child.getMeasuredWidth() > 0)) && (child.getMeasuredHeight() > 0)) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        int childState = 0;
        final int[] collapsingMargins = mTempMargins;
        final int marginStartIndex;
        final int marginEndIndex;
        if (android.support.v7.widget.ViewUtils.isLayoutRtl(this)) {
            marginStartIndex = 1;
            marginEndIndex = 0;
        } else {
            marginStartIndex = 0;
            marginEndIndex = 1;
        }
        // System views measure first.
        int navWidth = 0;
        if (shouldLayout(mNavButtonView)) {
            measureChildConstrained(mNavButtonView, widthMeasureSpec, width, heightMeasureSpec, 0, mMaxButtonHeight);
            navWidth = mNavButtonView.getMeasuredWidth() + getHorizontalMargins(mNavButtonView);
            height = java.lang.Math.max(height, mNavButtonView.getMeasuredHeight() + getVerticalMargins(mNavButtonView));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mNavButtonView));
        }
        if (shouldLayout(mCollapseButtonView)) {
            measureChildConstrained(mCollapseButtonView, widthMeasureSpec, width, heightMeasureSpec, 0, mMaxButtonHeight);
            navWidth = mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(mCollapseButtonView);
            height = java.lang.Math.max(height, mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(mCollapseButtonView));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mCollapseButtonView));
        }
        final int contentInsetStart = getCurrentContentInsetStart();
        width += java.lang.Math.max(contentInsetStart, navWidth);
        collapsingMargins[marginStartIndex] = java.lang.Math.max(0, contentInsetStart - navWidth);
        int menuWidth = 0;
        if (shouldLayout(mMenuView)) {
            measureChildConstrained(mMenuView, widthMeasureSpec, width, heightMeasureSpec, 0, mMaxButtonHeight);
            menuWidth = mMenuView.getMeasuredWidth() + getHorizontalMargins(mMenuView);
            height = java.lang.Math.max(height, mMenuView.getMeasuredHeight() + getVerticalMargins(mMenuView));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mMenuView));
        }
        final int contentInsetEnd = getCurrentContentInsetEnd();
        width += java.lang.Math.max(contentInsetEnd, menuWidth);
        collapsingMargins[marginEndIndex] = java.lang.Math.max(0, contentInsetEnd - menuWidth);
        if (shouldLayout(mExpandedActionView)) {
            width += measureChildCollapseMargins(mExpandedActionView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            height = java.lang.Math.max(height, mExpandedActionView.getMeasuredHeight() + getVerticalMargins(mExpandedActionView));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mExpandedActionView));
        }
        if (shouldLayout(mLogoView)) {
            width += measureChildCollapseMargins(mLogoView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            height = java.lang.Math.max(height, mLogoView.getMeasuredHeight() + getVerticalMargins(mLogoView));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mLogoView));
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
            if ((lp.mViewType != android.support.v7.widget.Toolbar.LayoutParams.CUSTOM) || (!shouldLayout(child))) {
                // We already got all system views above. Skip them and GONE views.
                continue;
            }
            width += measureChildCollapseMargins(child, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            height = java.lang.Math.max(height, child.getMeasuredHeight() + getVerticalMargins(child));
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child));
        }
        int titleWidth = 0;
        int titleHeight = 0;
        final int titleVertMargins = mTitleMarginTop + mTitleMarginBottom;
        final int titleHorizMargins = mTitleMarginStart + mTitleMarginEnd;
        if (shouldLayout(mTitleTextView)) {
            titleWidth = measureChildCollapseMargins(mTitleTextView, widthMeasureSpec, width + titleHorizMargins, heightMeasureSpec, titleVertMargins, collapsingMargins);
            titleWidth = mTitleTextView.getMeasuredWidth() + getHorizontalMargins(mTitleTextView);
            titleHeight = mTitleTextView.getMeasuredHeight() + getVerticalMargins(mTitleTextView);
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mTitleTextView));
        }
        if (shouldLayout(mSubtitleTextView)) {
            titleWidth = java.lang.Math.max(titleWidth, measureChildCollapseMargins(mSubtitleTextView, widthMeasureSpec, width + titleHorizMargins, heightMeasureSpec, titleHeight + titleVertMargins, collapsingMargins));
            titleHeight += mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(mSubtitleTextView);
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mSubtitleTextView));
        }
        width += titleWidth;
        height = java.lang.Math.max(height, titleHeight);
        // Measurement already took padding into account for available space for the children,
        // add it in for the final size.
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        final int measuredWidth = android.support.v4.view.ViewCompat.resolveSizeAndState(java.lang.Math.max(width, getSuggestedMinimumWidth()), widthMeasureSpec, childState & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK);
        final int measuredHeight = android.support.v4.view.ViewCompat.resolveSizeAndState(java.lang.Math.max(height, getSuggestedMinimumHeight()), heightMeasureSpec, childState << android.support.v4.view.ViewCompat.MEASURED_HEIGHT_STATE_SHIFT);
        setMeasuredDimension(measuredWidth, shouldCollapse() ? 0 : measuredHeight);
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
        final int width = getWidth();
        final int height = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int left = paddingLeft;
        int right = width - paddingRight;
        final int[] collapsingMargins = mTempMargins;
        collapsingMargins[0] = collapsingMargins[1] = 0;
        // Align views within the minimum toolbar height, if set.
        final int minHeight = android.support.v4.view.ViewCompat.getMinimumHeight(this);
        final int alignmentHeight = (minHeight >= 0) ? java.lang.Math.min(minHeight, b - t) : 0;
        if (shouldLayout(mNavButtonView)) {
            if (isRtl) {
                right = layoutChildRight(mNavButtonView, right, collapsingMargins, alignmentHeight);
            } else {
                left = layoutChildLeft(mNavButtonView, left, collapsingMargins, alignmentHeight);
            }
        }
        if (shouldLayout(mCollapseButtonView)) {
            if (isRtl) {
                right = layoutChildRight(mCollapseButtonView, right, collapsingMargins, alignmentHeight);
            } else {
                left = layoutChildLeft(mCollapseButtonView, left, collapsingMargins, alignmentHeight);
            }
        }
        if (shouldLayout(mMenuView)) {
            if (isRtl) {
                left = layoutChildLeft(mMenuView, left, collapsingMargins, alignmentHeight);
            } else {
                right = layoutChildRight(mMenuView, right, collapsingMargins, alignmentHeight);
            }
        }
        final int contentInsetLeft = getCurrentContentInsetLeft();
        final int contentInsetRight = getCurrentContentInsetRight();
        collapsingMargins[0] = java.lang.Math.max(0, contentInsetLeft - left);
        collapsingMargins[1] = java.lang.Math.max(0, contentInsetRight - ((width - paddingRight) - right));
        left = java.lang.Math.max(left, contentInsetLeft);
        right = java.lang.Math.min(right, (width - paddingRight) - contentInsetRight);
        if (shouldLayout(mExpandedActionView)) {
            if (isRtl) {
                right = layoutChildRight(mExpandedActionView, right, collapsingMargins, alignmentHeight);
            } else {
                left = layoutChildLeft(mExpandedActionView, left, collapsingMargins, alignmentHeight);
            }
        }
        if (shouldLayout(mLogoView)) {
            if (isRtl) {
                right = layoutChildRight(mLogoView, right, collapsingMargins, alignmentHeight);
            } else {
                left = layoutChildLeft(mLogoView, left, collapsingMargins, alignmentHeight);
            }
        }
        final boolean layoutTitle = shouldLayout(mTitleTextView);
        final boolean layoutSubtitle = shouldLayout(mSubtitleTextView);
        int titleHeight = 0;
        if (layoutTitle) {
            final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mTitleTextView.getLayoutParams()));
            titleHeight += (lp.topMargin + mTitleTextView.getMeasuredHeight()) + lp.bottomMargin;
        }
        if (layoutSubtitle) {
            final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mSubtitleTextView.getLayoutParams()));
            titleHeight += (lp.topMargin + mSubtitleTextView.getMeasuredHeight()) + lp.bottomMargin;
        }
        if (layoutTitle || layoutSubtitle) {
            int titleTop;
            final android.view.View topChild = (layoutTitle) ? mTitleTextView : mSubtitleTextView;
            final android.view.View bottomChild = (layoutSubtitle) ? mSubtitleTextView : mTitleTextView;
            final android.support.v7.widget.Toolbar.LayoutParams toplp = ((android.support.v7.widget.Toolbar.LayoutParams) (topChild.getLayoutParams()));
            final android.support.v7.widget.Toolbar.LayoutParams bottomlp = ((android.support.v7.widget.Toolbar.LayoutParams) (bottomChild.getLayoutParams()));
            final boolean titleHasWidth = (layoutTitle && (mTitleTextView.getMeasuredWidth() > 0)) || (layoutSubtitle && (mSubtitleTextView.getMeasuredWidth() > 0));
            switch (mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
                case android.view.Gravity.TOP :
                    titleTop = (getPaddingTop() + toplp.topMargin) + mTitleMarginTop;
                    break;
                default :
                case android.view.Gravity.CENTER_VERTICAL :
                    final int space = (height - paddingTop) - paddingBottom;
                    int spaceAbove = (space - titleHeight) / 2;
                    if (spaceAbove < (toplp.topMargin + mTitleMarginTop)) {
                        spaceAbove = toplp.topMargin + mTitleMarginTop;
                    } else {
                        final int spaceBelow = (((height - paddingBottom) - titleHeight) - spaceAbove) - paddingTop;
                        if (spaceBelow < (toplp.bottomMargin + mTitleMarginBottom)) {
                            spaceAbove = java.lang.Math.max(0, spaceAbove - ((bottomlp.bottomMargin + mTitleMarginBottom) - spaceBelow));
                        }
                    }
                    titleTop = paddingTop + spaceAbove;
                    break;
                case android.view.Gravity.BOTTOM :
                    titleTop = (((height - paddingBottom) - bottomlp.bottomMargin) - mTitleMarginBottom) - titleHeight;
                    break;
            }
            if (isRtl) {
                final int rd = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[1];
                right -= java.lang.Math.max(0, rd);
                collapsingMargins[1] = java.lang.Math.max(0, -rd);
                int titleRight = right;
                int subtitleRight = right;
                if (layoutTitle) {
                    final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mTitleTextView.getLayoutParams()));
                    final int titleLeft = titleRight - mTitleTextView.getMeasuredWidth();
                    final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
                    mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
                    titleRight = titleLeft - mTitleMarginEnd;
                    titleTop = titleBottom + lp.bottomMargin;
                }
                if (layoutSubtitle) {
                    final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mSubtitleTextView.getLayoutParams()));
                    titleTop += lp.topMargin;
                    final int subtitleLeft = subtitleRight - mSubtitleTextView.getMeasuredWidth();
                    final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
                    mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
                    subtitleRight = subtitleRight - mTitleMarginEnd;
                    titleTop = subtitleBottom + lp.bottomMargin;
                }
                if (titleHasWidth) {
                    right = java.lang.Math.min(titleRight, subtitleRight);
                }
            } else {
                final int ld = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[0];
                left += java.lang.Math.max(0, ld);
                collapsingMargins[0] = java.lang.Math.max(0, -ld);
                int titleLeft = left;
                int subtitleLeft = left;
                if (layoutTitle) {
                    final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mTitleTextView.getLayoutParams()));
                    final int titleRight = titleLeft + mTitleTextView.getMeasuredWidth();
                    final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
                    mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
                    titleLeft = titleRight + mTitleMarginEnd;
                    titleTop = titleBottom + lp.bottomMargin;
                }
                if (layoutSubtitle) {
                    final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (mSubtitleTextView.getLayoutParams()));
                    titleTop += lp.topMargin;
                    final int subtitleRight = subtitleLeft + mSubtitleTextView.getMeasuredWidth();
                    final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
                    mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
                    subtitleLeft = subtitleRight + mTitleMarginEnd;
                    titleTop = subtitleBottom + lp.bottomMargin;
                }
                if (titleHasWidth) {
                    left = java.lang.Math.max(titleLeft, subtitleLeft);
                }
            }
        }
        // Get all remaining children sorted for layout. This is all prepared
        // such that absolute layout direction can be used below.
        addCustomViewsWithGravity(mTempViews, android.view.Gravity.LEFT);
        final int leftViewsCount = mTempViews.size();
        for (int i = 0; i < leftViewsCount; i++) {
            left = layoutChildLeft(mTempViews.get(i), left, collapsingMargins, alignmentHeight);
        }
        addCustomViewsWithGravity(mTempViews, android.view.Gravity.RIGHT);
        final int rightViewsCount = mTempViews.size();
        for (int i = 0; i < rightViewsCount; i++) {
            right = layoutChildRight(mTempViews.get(i), right, collapsingMargins, alignmentHeight);
        }
        // Centered views try to center with respect to the whole bar, but views pinned
        // to the left or right can push the mass of centered views to one side or the other.
        addCustomViewsWithGravity(mTempViews, android.view.Gravity.CENTER_HORIZONTAL);
        final int centerViewsWidth = getViewListMeasuredWidth(mTempViews, collapsingMargins);
        final int parentCenter = paddingLeft + (((width - paddingLeft) - paddingRight) / 2);
        final int halfCenterViewsWidth = centerViewsWidth / 2;
        int centerLeft = parentCenter - halfCenterViewsWidth;
        final int centerRight = centerLeft + centerViewsWidth;
        if (centerLeft < left) {
            centerLeft = left;
        } else
            if (centerRight > right) {
                centerLeft -= centerRight - right;
            }

        final int centerViewsCount = mTempViews.size();
        for (int i = 0; i < centerViewsCount; i++) {
            centerLeft = layoutChildLeft(mTempViews.get(i), centerLeft, collapsingMargins, alignmentHeight);
        }
        mTempViews.clear();
    }

    private int getViewListMeasuredWidth(java.util.List<android.view.View> views, int[] collapsingMargins) {
        int collapseLeft = collapsingMargins[0];
        int collapseRight = collapsingMargins[1];
        int width = 0;
        final int count = views.size();
        for (int i = 0; i < count; i++) {
            final android.view.View v = views.get(i);
            final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (v.getLayoutParams()));
            final int l = lp.leftMargin - collapseLeft;
            final int r = lp.rightMargin - collapseRight;
            final int leftMargin = java.lang.Math.max(0, l);
            final int rightMargin = java.lang.Math.max(0, r);
            collapseLeft = java.lang.Math.max(0, -l);
            collapseRight = java.lang.Math.max(0, -r);
            width += (leftMargin + v.getMeasuredWidth()) + rightMargin;
        }
        return width;
    }

    private int layoutChildLeft(android.view.View child, int left, int[] collapsingMargins, int alignmentHeight) {
        final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
        final int l = lp.leftMargin - collapsingMargins[0];
        left += java.lang.Math.max(0, l);
        collapsingMargins[0] = java.lang.Math.max(0, -l);
        final int top = getChildTop(child, alignmentHeight);
        final int childWidth = child.getMeasuredWidth();
        child.layout(left, top, left + childWidth, top + child.getMeasuredHeight());
        left += childWidth + lp.rightMargin;
        return left;
    }

    private int layoutChildRight(android.view.View child, int right, int[] collapsingMargins, int alignmentHeight) {
        final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
        final int r = lp.rightMargin - collapsingMargins[1];
        right -= java.lang.Math.max(0, r);
        collapsingMargins[1] = java.lang.Math.max(0, -r);
        final int top = getChildTop(child, alignmentHeight);
        final int childWidth = child.getMeasuredWidth();
        child.layout(right - childWidth, top, right, top + child.getMeasuredHeight());
        right -= childWidth + lp.leftMargin;
        return right;
    }

    private int getChildTop(android.view.View child, int alignmentHeight) {
        final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
        final int childHeight = child.getMeasuredHeight();
        final int alignmentOffset = (alignmentHeight > 0) ? (childHeight - alignmentHeight) / 2 : 0;
        switch (getChildVerticalGravity(lp.gravity)) {
            case android.view.Gravity.TOP :
                return getPaddingTop() - alignmentOffset;
            case android.view.Gravity.BOTTOM :
                return (((getHeight() - getPaddingBottom()) - childHeight) - lp.bottomMargin) - alignmentOffset;
            default :
            case android.view.Gravity.CENTER_VERTICAL :
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                final int height = getHeight();
                final int space = (height - paddingTop) - paddingBottom;
                int spaceAbove = (space - childHeight) / 2;
                if (spaceAbove < lp.topMargin) {
                    spaceAbove = lp.topMargin;
                } else {
                    final int spaceBelow = (((height - paddingBottom) - childHeight) - spaceAbove) - paddingTop;
                    if (spaceBelow < lp.bottomMargin) {
                        spaceAbove = java.lang.Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
                    }
                }
                return paddingTop + spaceAbove;
        }
    }

    private int getChildVerticalGravity(int gravity) {
        final int vgrav = gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        switch (vgrav) {
            case android.view.Gravity.TOP :
            case android.view.Gravity.BOTTOM :
            case android.view.Gravity.CENTER_VERTICAL :
                return vgrav;
            default :
                return mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        }
    }

    /**
     * Prepare a list of non-SYSTEM child views. If the layout direction is RTL
     * this will be in reverse child order.
     *
     * @param views
     * 		List to populate. It will be cleared before use.
     * @param gravity
     * 		Horizontal gravity to match against
     */
    private void addCustomViewsWithGravity(java.util.List<android.view.View> views, int gravity) {
        final boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
        final int childCount = getChildCount();
        final int absGrav = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, android.support.v4.view.ViewCompat.getLayoutDirection(this));
        views.clear();
        if (isRtl) {
            for (int i = childCount - 1; i >= 0; i--) {
                final android.view.View child = getChildAt(i);
                final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
                if (((lp.mViewType == android.support.v7.widget.Toolbar.LayoutParams.CUSTOM) && shouldLayout(child)) && (getChildHorizontalGravity(lp.gravity) == absGrav)) {
                    views.add(child);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                final android.view.View child = getChildAt(i);
                final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
                if (((lp.mViewType == android.support.v7.widget.Toolbar.LayoutParams.CUSTOM) && shouldLayout(child)) && (getChildHorizontalGravity(lp.gravity) == absGrav)) {
                    views.add(child);
                }
            }
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        final int ld = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        final int absGrav = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, ld);
        final int hGrav = absGrav & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (hGrav) {
            case android.view.Gravity.LEFT :
            case android.view.Gravity.RIGHT :
            case android.view.Gravity.CENTER_HORIZONTAL :
                return hGrav;
            default :
                return ld == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL ? android.view.Gravity.RIGHT : android.view.Gravity.LEFT;
        }
    }

    private boolean shouldLayout(android.view.View view) {
        return ((view != null) && (view.getParent() == this)) && (view.getVisibility() != android.view.View.GONE);
    }

    private int getHorizontalMargins(android.view.View v) {
        final android.view.ViewGroup.MarginLayoutParams mlp = ((android.view.ViewGroup.MarginLayoutParams) (v.getLayoutParams()));
        return android.support.v4.view.MarginLayoutParamsCompat.getMarginStart(mlp) + android.support.v4.view.MarginLayoutParamsCompat.getMarginEnd(mlp);
    }

    private int getVerticalMargins(android.view.View v) {
        final android.view.ViewGroup.MarginLayoutParams mlp = ((android.view.ViewGroup.MarginLayoutParams) (v.getLayoutParams()));
        return mlp.topMargin + mlp.bottomMargin;
    }

    @java.lang.Override
    public android.support.v7.widget.Toolbar.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v7.widget.Toolbar.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.support.v7.widget.Toolbar.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof android.support.v7.widget.Toolbar.LayoutParams) {
            return new android.support.v7.widget.Toolbar.LayoutParams(((android.support.v7.widget.Toolbar.LayoutParams) (p)));
        } else
            if (p instanceof android.support.v7.app.ActionBar.LayoutParams) {
                return new android.support.v7.widget.Toolbar.LayoutParams(((android.support.v7.app.ActionBar.LayoutParams) (p)));
            } else
                if (p instanceof android.view.ViewGroup.MarginLayoutParams) {
                    return new android.support.v7.widget.Toolbar.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (p)));
                } else {
                    return new android.support.v7.widget.Toolbar.LayoutParams(p);
                }


    }

    @java.lang.Override
    protected android.support.v7.widget.Toolbar.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v7.widget.Toolbar.LayoutParams(android.support.v7.widget.Toolbar.LayoutParams.WRAP_CONTENT, android.support.v7.widget.Toolbar.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof android.support.v7.widget.Toolbar.LayoutParams);
    }

    private static boolean isCustomView(android.view.View child) {
        return ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams())).mViewType == android.support.v7.widget.Toolbar.LayoutParams.CUSTOM;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public android.support.v7.widget.DecorToolbar getWrapper() {
        if (mWrapper == null) {
            mWrapper = new android.support.v7.widget.ToolbarWidgetWrapper(this, true);
        }
        return mWrapper;
    }

    void removeChildrenForExpandedActionView() {
        final int childCount = getChildCount();
        // Go backwards since we're removing from the list
        for (int i = childCount - 1; i >= 0; i--) {
            final android.view.View child = getChildAt(i);
            final android.support.v7.widget.Toolbar.LayoutParams lp = ((android.support.v7.widget.Toolbar.LayoutParams) (child.getLayoutParams()));
            if ((lp.mViewType != android.support.v7.widget.Toolbar.LayoutParams.EXPANDED) && (child != mMenuView)) {
                removeViewAt(i);
                mHiddenViews.add(child);
            }
        }
    }

    void addChildrenForExpandedActionView() {
        final int count = mHiddenViews.size();
        // Re-add in reverse order since we removed in reverse order
        for (int i = count - 1; i >= 0; i--) {
            addView(mHiddenViews.get(i));
        }
        mHiddenViews.clear();
    }

    private boolean isChildOrHidden(android.view.View child) {
        return (child.getParent() == this) || mHiddenViews.contains(child);
    }

    /**
     * Force the toolbar to collapse to zero-height during measurement if
     * it could be considered "empty" (no visible elements with nonzero measured size)
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setCollapsible(boolean collapsible) {
        mCollapsible = collapsible;
        requestLayout();
    }

    /**
     * Must be called before the menu is accessed
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setMenuCallbacks(android.support.v7.view.menu.MenuPresenter.Callback pcb, android.support.v7.view.menu.MenuBuilder.Callback mcb) {
        mActionMenuPresenterCallback = pcb;
        mMenuBuilderCallback = mcb;
        if (mMenuView != null) {
            mMenuView.setMenuCallbacks(pcb, mcb);
        }
    }

    private void ensureContentInsets() {
        if (mContentInsets == null) {
            mContentInsets = new android.support.v7.widget.RtlSpacingHelper();
        }
    }

    /**
     * Interface responsible for receiving menu item click events if the items themselves
     * do not have individual item click listeners.
     */
    public interface OnMenuItemClickListener {
        /**
         * This method will be invoked when a menu item is clicked if the item itself did
         * not already handle the event.
         *
         * @param item
         * 		{@link MenuItem} that was clicked
         * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
         */
        public boolean onMenuItemClick(android.view.MenuItem item);
    }

    /**
     * Layout information for child views of Toolbars.
     *
     * <p>Toolbar.LayoutParams extends ActionBar.LayoutParams for compatibility with existing
     * ActionBar API. See
     * {@link android.support.v7.app.AppCompatActivity#setSupportActionBar(Toolbar)
     * ActionBarActivity.setActionBar}
     * for more info on how to use a Toolbar as your Activity's ActionBar.</p>
     */
    public static class LayoutParams extends android.support.v7.app.ActionBar.LayoutParams {
        static final int CUSTOM = 0;

        static final int SYSTEM = 1;

        static final int EXPANDED = 2;

        int mViewType = android.support.v7.widget.Toolbar.LayoutParams.CUSTOM;

        public LayoutParams(@android.support.annotation.NonNull
        android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = android.view.Gravity.CENTER_VERTICAL | android.support.v4.view.GravityCompat.START;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, gravity);
        }

        public LayoutParams(android.support.v7.widget.Toolbar.LayoutParams source) {
            super(source);
            mViewType = source.mViewType;
        }

        public LayoutParams(android.support.v7.app.ActionBar.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
            // ActionBar.LayoutParams doesn't have a MarginLayoutParams constructor.
            // Fake it here and copy over the relevant data.
            copyMarginsFromCompat(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        void copyMarginsFromCompat(android.view.ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    public static class SavedState extends android.support.v4.view.AbsSavedState {
        int expandedMenuItemId;

        boolean isOverflowOpen;

        public SavedState(android.os.Parcel source) {
            this(source, null);
        }

        public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
            super(source, loader);
            expandedMenuItemId = source.readInt();
            isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(expandedMenuItemId);
            out.writeInt(isOverflowOpen ? 1 : 0);
        }

        public static final android.os.Parcelable.Creator<android.support.v7.widget.Toolbar.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v7.widget.Toolbar.SavedState>() {
            @java.lang.Override
            public android.support.v7.widget.Toolbar.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.v7.widget.Toolbar.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.v7.widget.Toolbar.SavedState[] newArray(int size) {
                return new android.support.v7.widget.Toolbar.SavedState[size];
            }
        });
    }

    private class ExpandedActionViewMenuPresenter implements android.support.v7.view.menu.MenuPresenter {
        android.support.v7.view.menu.MenuBuilder mMenu;

        android.support.v7.view.menu.MenuItemImpl mCurrentExpandedItem;

        ExpandedActionViewMenuPresenter() {
        }

        @java.lang.Override
        public void initForMenu(android.content.Context context, android.support.v7.view.menu.MenuBuilder menu) {
            // Clear the expanded action view when menus change.
            if ((mMenu != null) && (mCurrentExpandedItem != null)) {
                mMenu.collapseItemActionView(mCurrentExpandedItem);
            }
            mMenu = menu;
        }

        @java.lang.Override
        public android.support.v7.view.menu.MenuView getMenuView(android.view.ViewGroup root) {
            return null;
        }

        @java.lang.Override
        public void updateMenuView(boolean cleared) {
            // Make sure the expanded item we have is still there.
            if (mCurrentExpandedItem != null) {
                boolean found = false;
                if (mMenu != null) {
                    final int count = mMenu.size();
                    for (int i = 0; i < count; i++) {
                        final android.view.MenuItem item = mMenu.getItem(i);
                        if (item == mCurrentExpandedItem) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    // The item we had expanded disappeared. Collapse.
                    collapseItemActionView(mMenu, mCurrentExpandedItem);
                }
            }
        }

        @java.lang.Override
        public void setCallback(android.support.v7.view.menu.MenuPresenter.Callback cb) {
        }

        @java.lang.Override
        public boolean onSubMenuSelected(android.support.v7.view.menu.SubMenuBuilder subMenu) {
            return false;
        }

        @java.lang.Override
        public void onCloseMenu(android.support.v7.view.menu.MenuBuilder menu, boolean allMenusAreClosing) {
        }

        @java.lang.Override
        public boolean flagActionItems() {
            return false;
        }

        @java.lang.Override
        public boolean expandItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
            ensureCollapseButtonView();
            if (mCollapseButtonView.getParent() != android.support.v7.widget.Toolbar.this) {
                addView(mCollapseButtonView);
            }
            mExpandedActionView = item.getActionView();
            mCurrentExpandedItem = item;
            if (mExpandedActionView.getParent() != android.support.v7.widget.Toolbar.this) {
                final android.support.v7.widget.Toolbar.LayoutParams lp = generateDefaultLayoutParams();
                lp.gravity = android.support.v4.view.GravityCompat.START | (mButtonGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK);
                lp.mViewType = android.support.v7.widget.Toolbar.LayoutParams.EXPANDED;
                mExpandedActionView.setLayoutParams(lp);
                addView(mExpandedActionView);
            }
            removeChildrenForExpandedActionView();
            requestLayout();
            item.setActionViewExpanded(true);
            if (mExpandedActionView instanceof android.support.v7.view.CollapsibleActionView) {
                ((android.support.v7.view.CollapsibleActionView) (mExpandedActionView)).onActionViewExpanded();
            }
            return true;
        }

        @java.lang.Override
        public boolean collapseItemActionView(android.support.v7.view.menu.MenuBuilder menu, android.support.v7.view.menu.MenuItemImpl item) {
            // Do this before detaching the actionview from the hierarchy, in case
            // it needs to dismiss the soft keyboard, etc.
            if (mExpandedActionView instanceof android.support.v7.view.CollapsibleActionView) {
                ((android.support.v7.view.CollapsibleActionView) (mExpandedActionView)).onActionViewCollapsed();
            }
            removeView(mExpandedActionView);
            removeView(mCollapseButtonView);
            mExpandedActionView = null;
            addChildrenForExpandedActionView();
            mCurrentExpandedItem = null;
            requestLayout();
            item.setActionViewExpanded(false);
            return true;
        }

        @java.lang.Override
        public int getId() {
            return 0;
        }

        @java.lang.Override
        public android.os.Parcelable onSaveInstanceState() {
            return null;
        }

        @java.lang.Override
        public void onRestoreInstanceState(android.os.Parcelable state) {
        }
    }
}

