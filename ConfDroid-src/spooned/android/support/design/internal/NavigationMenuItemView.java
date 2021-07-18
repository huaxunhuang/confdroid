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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class NavigationMenuItemView extends android.support.design.internal.ForegroundLinearLayout implements android.support.v7.view.menu.MenuView.ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    private final int mIconSize;

    private boolean mNeedsEmptyIcon;

    boolean mCheckable;

    private final android.widget.CheckedTextView mTextView;

    private android.widget.FrameLayout mActionArea;

    private android.support.v7.view.menu.MenuItemImpl mItemData;

    private android.content.res.ColorStateList mIconTintList;

    private boolean mHasIconTintList;

    private android.graphics.drawable.Drawable mEmptyDrawable;

    private final android.support.v4.view.AccessibilityDelegateCompat mAccessibilityDelegate = new android.support.v4.view.AccessibilityDelegateCompat() {
        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setCheckable(mCheckable);
        }
    };

    public NavigationMenuItemView(android.content.Context context) {
        this(context, null);
    }

    public NavigationMenuItemView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationMenuItemView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(android.support.v7.widget.LinearLayoutCompat.HORIZONTAL);
        android.view.LayoutInflater.from(context).inflate(R.layout.design_navigation_menu_item, this, true);
        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
        mTextView = ((android.widget.CheckedTextView) (findViewById(R.id.design_menu_item_text)));
        mTextView.setDuplicateParentStateEnabled(true);
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(mTextView, mAccessibilityDelegate);
    }

    @java.lang.Override
    public void initialize(android.support.v7.view.menu.MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        setVisibility(itemData.isVisible() ? android.view.View.VISIBLE : android.view.View.GONE);
        if (getBackground() == null) {
            android.support.v4.view.ViewCompat.setBackground(this, createDefaultBackground());
        }
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
        setTitle(itemData.getTitle());
        setIcon(itemData.getIcon());
        setActionView(itemData.getActionView());
        adjustAppearance();
    }

    private boolean shouldExpandActionArea() {
        return ((mItemData.getTitle() == null) && (mItemData.getIcon() == null)) && (mItemData.getActionView() != null);
    }

    private void adjustAppearance() {
        if (shouldExpandActionArea()) {
            // Expand the actionView area
            mTextView.setVisibility(android.view.View.GONE);
            if (mActionArea != null) {
                android.support.v7.widget.LinearLayoutCompat.LayoutParams params = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (mActionArea.getLayoutParams()));
                params.width = android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT;
                mActionArea.setLayoutParams(params);
            }
        } else {
            mTextView.setVisibility(android.view.View.VISIBLE);
            if (mActionArea != null) {
                android.support.v7.widget.LinearLayoutCompat.LayoutParams params = ((android.support.v7.widget.LinearLayoutCompat.LayoutParams) (mActionArea.getLayoutParams()));
                params.width = android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                mActionArea.setLayoutParams(params);
            }
        }
    }

    public void recycle() {
        if (mActionArea != null) {
            mActionArea.removeAllViews();
        }
        mTextView.setCompoundDrawables(null, null, null, null);
    }

    private void setActionView(android.view.View actionView) {
        if (actionView != null) {
            if (mActionArea == null) {
                mActionArea = ((android.widget.FrameLayout) (((android.view.ViewStub) (findViewById(R.id.design_menu_item_action_area_stub))).inflate()));
            }
            mActionArea.removeAllViews();
            mActionArea.addView(actionView);
        }
    }

    private android.graphics.drawable.StateListDrawable createDefaultBackground() {
        android.util.TypedValue value = new android.util.TypedValue();
        if (getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorControlHighlight, value, true)) {
            android.graphics.drawable.StateListDrawable drawable = new android.graphics.drawable.StateListDrawable();
            drawable.addState(android.support.design.internal.NavigationMenuItemView.CHECKED_STATE_SET, new android.graphics.drawable.ColorDrawable(value.data));
            drawable.addState(android.view.View.EMPTY_STATE_SET, new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            return drawable;
        }
        return null;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuItemImpl getItemData() {
        return mItemData;
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mTextView.setText(title);
    }

    @java.lang.Override
    public void setCheckable(boolean checkable) {
        refreshDrawableState();
        if (mCheckable != checkable) {
            mCheckable = checkable;
            mAccessibilityDelegate.sendAccessibilityEvent(mTextView, android.support.v4.view.accessibility.AccessibilityEventCompat.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    @java.lang.Override
    public void setChecked(boolean checked) {
        refreshDrawableState();
        mTextView.setChecked(checked);
    }

    @java.lang.Override
    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable icon) {
        if (icon != null) {
            if (mHasIconTintList) {
                android.graphics.drawable.Drawable.ConstantState state = icon.getConstantState();
                icon = android.support.v4.graphics.drawable.DrawableCompat.wrap(state == null ? icon : state.newDrawable()).mutate();
                android.support.v4.graphics.drawable.DrawableCompat.setTintList(icon, mIconTintList);
            }
            icon.setBounds(0, 0, mIconSize, mIconSize);
        } else
            if (mNeedsEmptyIcon) {
                if (mEmptyDrawable == null) {
                    mEmptyDrawable = android.support.v4.content.res.ResourcesCompat.getDrawable(getResources(), R.drawable.navigation_empty_icon, getContext().getTheme());
                    if (mEmptyDrawable != null) {
                        mEmptyDrawable.setBounds(0, 0, mIconSize, mIconSize);
                    }
                }
                icon = mEmptyDrawable;
            }

        android.support.v4.widget.TextViewCompat.setCompoundDrawablesRelative(mTextView, icon, null, null, null);
    }

    @java.lang.Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @java.lang.Override
    public boolean showsIcon() {
        return true;
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (((mItemData != null) && mItemData.isCheckable()) && mItemData.isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.support.design.internal.NavigationMenuItemView.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    void setIconTintList(android.content.res.ColorStateList tintList) {
        mIconTintList = tintList;
        mHasIconTintList = mIconTintList != null;
        if (mItemData != null) {
            // Update the icon so that the tint takes effect
            setIcon(mItemData.getIcon());
        }
    }

    public void setTextAppearance(int textAppearance) {
        android.support.v4.widget.TextViewCompat.setTextAppearance(mTextView, textAppearance);
    }

    public void setTextColor(android.content.res.ColorStateList colors) {
        mTextView.setTextColor(colors);
    }

    public void setNeedsEmptyIcon(boolean needsEmptyIcon) {
        mNeedsEmptyIcon = needsEmptyIcon;
    }
}

