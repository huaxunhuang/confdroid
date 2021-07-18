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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class BottomNavigationItemView extends android.widget.FrameLayout implements android.support.v7.view.menu.MenuView.ItemView {
    public static final int INVALID_ITEM_POSITION = -1;

    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    private final int mDefaultMargin;

    private final int mShiftAmount;

    private final float mScaleUpFactor;

    private final float mScaleDownFactor;

    private boolean mShiftingMode;

    private android.widget.ImageView mIcon;

    private final android.widget.TextView mSmallLabel;

    private final android.widget.TextView mLargeLabel;

    private int mItemPosition = android.support.design.internal.BottomNavigationItemView.INVALID_ITEM_POSITION;

    private android.support.v7.view.menu.MenuItemImpl mItemData;

    private android.content.res.ColorStateList mIconTint;

    public BottomNavigationItemView(@android.support.annotation.NonNull
    android.content.Context context) {
        this(context, null);
    }

    public BottomNavigationItemView(@android.support.annotation.NonNull
    android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationItemView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final android.content.res.Resources res = getResources();
        int inactiveLabelSize = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_text_size);
        int activeLabelSize = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_text_size);
        mDefaultMargin = res.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin);
        mShiftAmount = inactiveLabelSize - activeLabelSize;
        mScaleUpFactor = (1.0F * activeLabelSize) / inactiveLabelSize;
        mScaleDownFactor = (1.0F * inactiveLabelSize) / activeLabelSize;
        android.view.LayoutInflater.from(context).inflate(R.layout.design_bottom_navigation_item, this, true);
        setBackgroundResource(R.drawable.design_bottom_navigation_item_background);
        mIcon = ((android.widget.ImageView) (findViewById(R.id.icon)));
        mSmallLabel = ((android.widget.TextView) (findViewById(R.id.smallLabel)));
        mLargeLabel = ((android.widget.TextView) (findViewById(R.id.largeLabel)));
    }

    @java.lang.Override
    public void initialize(android.support.v7.view.menu.MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitle());
        setId(itemData.getItemId());
    }

    public void setItemPosition(int position) {
        mItemPosition = position;
    }

    public int getItemPosition() {
        return mItemPosition;
    }

    public void setShiftingMode(boolean enabled) {
        mShiftingMode = enabled;
    }

    @java.lang.Override
    public android.support.v7.view.menu.MenuItemImpl getItemData() {
        return mItemData;
    }

    @java.lang.Override
    public void setTitle(java.lang.CharSequence title) {
        mSmallLabel.setText(title);
        mLargeLabel.setText(title);
    }

    @java.lang.Override
    public void setCheckable(boolean checkable) {
        refreshDrawableState();
    }

    @java.lang.Override
    public void setChecked(boolean checked) {
        android.support.v4.view.ViewCompat.setPivotX(mLargeLabel, mLargeLabel.getWidth() / 2);
        android.support.v4.view.ViewCompat.setPivotY(mLargeLabel, mLargeLabel.getBaseline());
        android.support.v4.view.ViewCompat.setPivotX(mSmallLabel, mSmallLabel.getWidth() / 2);
        android.support.v4.view.ViewCompat.setPivotY(mSmallLabel, mSmallLabel.getBaseline());
        if (mShiftingMode) {
            if (checked) {
                android.widget.FrameLayout.LayoutParams iconParams = ((android.widget.FrameLayout.LayoutParams) (mIcon.getLayoutParams()));
                iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.TOP;
                iconParams.topMargin = mDefaultMargin;
                mIcon.setLayoutParams(iconParams);
                mLargeLabel.setVisibility(android.view.View.VISIBLE);
                android.support.v4.view.ViewCompat.setScaleX(mLargeLabel, 1.0F);
                android.support.v4.view.ViewCompat.setScaleY(mLargeLabel, 1.0F);
            } else {
                android.widget.FrameLayout.LayoutParams iconParams = ((android.widget.FrameLayout.LayoutParams) (mIcon.getLayoutParams()));
                iconParams.gravity = android.view.Gravity.CENTER;
                iconParams.topMargin = mDefaultMargin;
                mIcon.setLayoutParams(iconParams);
                mLargeLabel.setVisibility(android.view.View.INVISIBLE);
                android.support.v4.view.ViewCompat.setScaleX(mLargeLabel, 0.5F);
                android.support.v4.view.ViewCompat.setScaleY(mLargeLabel, 0.5F);
            }
            mSmallLabel.setVisibility(android.view.View.INVISIBLE);
        } else {
            if (checked) {
                android.widget.FrameLayout.LayoutParams iconParams = ((android.widget.FrameLayout.LayoutParams) (mIcon.getLayoutParams()));
                iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.TOP;
                iconParams.topMargin = mDefaultMargin + mShiftAmount;
                mIcon.setLayoutParams(iconParams);
                mLargeLabel.setVisibility(android.view.View.VISIBLE);
                mSmallLabel.setVisibility(android.view.View.INVISIBLE);
                android.support.v4.view.ViewCompat.setScaleX(mLargeLabel, 1.0F);
                android.support.v4.view.ViewCompat.setScaleY(mLargeLabel, 1.0F);
                android.support.v4.view.ViewCompat.setScaleX(mSmallLabel, mScaleUpFactor);
                android.support.v4.view.ViewCompat.setScaleY(mSmallLabel, mScaleUpFactor);
            } else {
                android.widget.FrameLayout.LayoutParams iconParams = ((android.widget.FrameLayout.LayoutParams) (mIcon.getLayoutParams()));
                iconParams.gravity = android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.TOP;
                iconParams.topMargin = mDefaultMargin;
                mIcon.setLayoutParams(iconParams);
                mLargeLabel.setVisibility(android.view.View.INVISIBLE);
                mSmallLabel.setVisibility(android.view.View.VISIBLE);
                android.support.v4.view.ViewCompat.setScaleX(mLargeLabel, mScaleDownFactor);
                android.support.v4.view.ViewCompat.setScaleY(mLargeLabel, mScaleDownFactor);
                android.support.v4.view.ViewCompat.setScaleX(mSmallLabel, 1.0F);
                android.support.v4.view.ViewCompat.setScaleY(mSmallLabel, 1.0F);
            }
        }
        refreshDrawableState();
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mSmallLabel.setEnabled(enabled);
        mLargeLabel.setEnabled(enabled);
        mIcon.setEnabled(enabled);
    }

    @java.lang.Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (((mItemData != null) && mItemData.isCheckable()) && mItemData.isChecked()) {
            android.view.View.mergeDrawableStates(drawableState, android.support.design.internal.BottomNavigationItemView.CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @java.lang.Override
    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable icon) {
        if (icon != null) {
            android.graphics.drawable.Drawable.ConstantState state = icon.getConstantState();
            icon = android.support.v4.graphics.drawable.DrawableCompat.wrap(state == null ? icon : state.newDrawable()).mutate();
            android.support.v4.graphics.drawable.DrawableCompat.setTintList(icon, mIconTint);
        }
        mIcon.setImageDrawable(icon);
    }

    @java.lang.Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @java.lang.Override
    public boolean showsIcon() {
        return true;
    }

    public void setIconTintList(android.content.res.ColorStateList tint) {
        mIconTint = tint;
        if (mItemData != null) {
            // Update the icon so that the tint takes effect
            setIcon(mItemData.getIcon());
        }
    }

    public void setTextColor(android.content.res.ColorStateList color) {
        mSmallLabel.setTextColor(color);
        mLargeLabel.setTextColor(color);
    }

    public void setItemBackground(int background) {
        android.graphics.drawable.Drawable backgroundDrawable = (background == 0) ? null : android.support.v4.content.ContextCompat.getDrawable(getContext(), background);
        android.support.v4.view.ViewCompat.setBackground(this, backgroundDrawable);
    }
}

