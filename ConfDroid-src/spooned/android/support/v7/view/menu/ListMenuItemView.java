/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * The item view for each item in the ListView-based MenuViews.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ListMenuItemView extends android.widget.LinearLayout implements android.support.v7.view.menu.MenuView.ItemView {
    private static final java.lang.String TAG = "ListMenuItemView";

    private android.support.v7.view.menu.MenuItemImpl mItemData;

    private android.widget.ImageView mIconView;

    private android.widget.RadioButton mRadioButton;

    private android.widget.TextView mTitleView;

    private android.widget.CheckBox mCheckBox;

    private android.widget.TextView mShortcutView;

    private android.widget.ImageView mSubMenuArrowView;

    private android.graphics.drawable.Drawable mBackground;

    private int mTextAppearance;

    private android.content.Context mTextAppearanceContext;

    private boolean mPreserveIconSpacing;

    private android.graphics.drawable.Drawable mSubMenuArrow;

    private int mMenuType;

    private android.view.LayoutInflater mInflater;

    private boolean mForceShowIcon;

    public ListMenuItemView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.listMenuViewStyle);
    }

    public ListMenuItemView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.MenuView, defStyleAttr, 0);
        mBackground = a.getDrawable(R.styleable.MenuView_android_itemBackground);
        mTextAppearance = a.getResourceId(R.styleable.MenuView_android_itemTextAppearance, -1);
        mPreserveIconSpacing = a.getBoolean(R.styleable.MenuView_preserveIconSpacing, false);
        mTextAppearanceContext = context;
        mSubMenuArrow = a.getDrawable(R.styleable.MenuView_subMenuArrow);
        a.recycle();
    }

    @java.lang.Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        android.support.v4.view.ViewCompat.setBackground(this, mBackground);
        mTitleView = ((android.widget.TextView) (findViewById(R.id.title)));
        if (mTextAppearance != (-1)) {
            mTitleView.setTextAppearance(mTextAppearanceContext, mTextAppearance);
        }
        mShortcutView = ((android.widget.TextView) (findViewById(R.id.shortcut)));
        mSubMenuArrowView = ((android.widget.ImageView) (findViewById(R.id.submenuarrow)));
        if (mSubMenuArrowView != null) {
            mSubMenuArrowView.setImageDrawable(mSubMenuArrow);
        }
    }

    public void initialize(android.support.v7.view.menu.MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        mMenuType = menuType;
        setVisibility(itemData.isVisible() ? android.view.View.VISIBLE : android.view.View.GONE);
        setTitle(itemData.getTitleForItemView(this));
        setCheckable(itemData.isCheckable());
        setShortcut(itemData.shouldShowShortcut(), itemData.getShortcut());
        setIcon(itemData.getIcon());
        setEnabled(itemData.isEnabled());
        setSubMenuArrowVisible(itemData.hasSubMenu());
    }

    public void setForceShowIcon(boolean forceShow) {
        mPreserveIconSpacing = mForceShowIcon = forceShow;
    }

    public void setTitle(java.lang.CharSequence title) {
        if (title != null) {
            mTitleView.setText(title);
            if (mTitleView.getVisibility() != android.view.View.VISIBLE)
                mTitleView.setVisibility(android.view.View.VISIBLE);

        } else {
            if (mTitleView.getVisibility() != android.view.View.GONE)
                mTitleView.setVisibility(android.view.View.GONE);

        }
    }

    public android.support.v7.view.menu.MenuItemImpl getItemData() {
        return mItemData;
    }

    public void setCheckable(boolean checkable) {
        if (((!checkable) && (mRadioButton == null)) && (mCheckBox == null)) {
            return;
        }
        // Depending on whether its exclusive check or not, the checkbox or
        // radio button will be the one in use (and the other will be otherCompoundButton)
        final android.widget.CompoundButton compoundButton;
        final android.widget.CompoundButton otherCompoundButton;
        if (mItemData.isExclusiveCheckable()) {
            if (mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = mRadioButton;
            otherCompoundButton = mCheckBox;
        } else {
            if (mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = mCheckBox;
            otherCompoundButton = mRadioButton;
        }
        if (checkable) {
            compoundButton.setChecked(mItemData.isChecked());
            final int newVisibility = (checkable) ? android.view.View.VISIBLE : android.view.View.GONE;
            if (compoundButton.getVisibility() != newVisibility) {
                compoundButton.setVisibility(newVisibility);
            }
            // Make sure the other compound button isn't visible
            if ((otherCompoundButton != null) && (otherCompoundButton.getVisibility() != android.view.View.GONE)) {
                otherCompoundButton.setVisibility(android.view.View.GONE);
            }
        } else {
            if (mCheckBox != null) {
                mCheckBox.setVisibility(android.view.View.GONE);
            }
            if (mRadioButton != null) {
                mRadioButton.setVisibility(android.view.View.GONE);
            }
        }
    }

    public void setChecked(boolean checked) {
        android.widget.CompoundButton compoundButton;
        if (mItemData.isExclusiveCheckable()) {
            if (mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = mRadioButton;
        } else {
            if (mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = mCheckBox;
        }
        compoundButton.setChecked(checked);
    }

    private void setSubMenuArrowVisible(boolean hasSubmenu) {
        if (mSubMenuArrowView != null) {
            mSubMenuArrowView.setVisibility(hasSubmenu ? android.view.View.VISIBLE : android.view.View.GONE);
        }
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
        final int newVisibility = (showShortcut && mItemData.shouldShowShortcut()) ? android.view.View.VISIBLE : android.view.View.GONE;
        if (newVisibility == android.view.View.VISIBLE) {
            mShortcutView.setText(mItemData.getShortcutLabel());
        }
        if (mShortcutView.getVisibility() != newVisibility) {
            mShortcutView.setVisibility(newVisibility);
        }
    }

    public void setIcon(android.graphics.drawable.Drawable icon) {
        final boolean showIcon = mItemData.shouldShowIcon() || mForceShowIcon;
        if ((!showIcon) && (!mPreserveIconSpacing)) {
            return;
        }
        if (((mIconView == null) && (icon == null)) && (!mPreserveIconSpacing)) {
            return;
        }
        if (mIconView == null) {
            insertIconView();
        }
        if ((icon != null) || mPreserveIconSpacing) {
            mIconView.setImageDrawable(showIcon ? icon : null);
            if (mIconView.getVisibility() != android.view.View.VISIBLE) {
                mIconView.setVisibility(android.view.View.VISIBLE);
            }
        } else {
            mIconView.setVisibility(android.view.View.GONE);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if ((mIconView != null) && mPreserveIconSpacing) {
            // Enforce minimum icon spacing
            android.view.ViewGroup.LayoutParams lp = getLayoutParams();
            android.widget.LinearLayout.LayoutParams iconLp = ((android.widget.LinearLayout.LayoutParams) (mIconView.getLayoutParams()));
            if ((lp.height > 0) && (iconLp.width <= 0)) {
                iconLp.width = lp.height;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void insertIconView() {
        android.view.LayoutInflater inflater = getInflater();
        mIconView = ((android.widget.ImageView) (inflater.inflate(R.layout.abc_list_menu_item_icon, this, false)));
        addView(mIconView, 0);
    }

    private void insertRadioButton() {
        android.view.LayoutInflater inflater = getInflater();
        mRadioButton = ((android.widget.RadioButton) (inflater.inflate(R.layout.abc_list_menu_item_radio, this, false)));
        addView(mRadioButton);
    }

    private void insertCheckBox() {
        android.view.LayoutInflater inflater = getInflater();
        mCheckBox = ((android.widget.CheckBox) (inflater.inflate(R.layout.abc_list_menu_item_checkbox, this, false)));
        addView(mCheckBox);
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public boolean showsIcon() {
        return mForceShowIcon;
    }

    private android.view.LayoutInflater getInflater() {
        if (mInflater == null) {
            mInflater = android.view.LayoutInflater.from(getContext());
        }
        return mInflater;
    }
}

