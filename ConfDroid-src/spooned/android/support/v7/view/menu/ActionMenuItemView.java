/**
 * Copyright (C) 2010 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ActionMenuItemView extends android.support.v7.widget.AppCompatTextView implements android.support.v7.view.menu.MenuView.ItemView , android.support.v7.widget.ActionMenuView.ActionMenuChildView , android.view.View.OnClickListener , android.view.View.OnLongClickListener {
    private static final java.lang.String TAG = "ActionMenuItemView";

    android.support.v7.view.menu.MenuItemImpl mItemData;

    private java.lang.CharSequence mTitle;

    private android.graphics.drawable.Drawable mIcon;

    android.support.v7.view.menu.MenuBuilder.ItemInvoker mItemInvoker;

    private android.support.v7.widget.ForwardingListener mForwardingListener;

    android.support.v7.view.menu.ActionMenuItemView.PopupCallback mPopupCallback;

    private boolean mAllowTextWithIcon;

    private boolean mExpandedFormat;

    private int mMinWidth;

    private int mSavedPaddingLeft;

    private static final int MAX_ICON_SIZE = 32;// dp


    private int mMaxIconSize;

    public ActionMenuItemView(android.content.Context context) {
        this(context, null);
    }

    public ActionMenuItemView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMenuItemView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final android.content.res.Resources res = context.getResources();
        mAllowTextWithIcon = shouldAllowTextWithIcon();
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionMenuItemView, defStyle, 0);
        mMinWidth = a.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
        a.recycle();
        final float density = res.getDisplayMetrics().density;
        mMaxIconSize = ((int) ((android.support.v7.view.menu.ActionMenuItemView.MAX_ICON_SIZE * density) + 0.5F));
        setOnClickListener(this);
        setOnLongClickListener(this);
        mSavedPaddingLeft = -1;
        setSaveEnabled(false);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mAllowTextWithIcon = shouldAllowTextWithIcon();
        updateTextButtonVisibility();
    }

    /**
     * Whether action menu items should obey the "withText" showAsAction flag. This may be set to
     * false for situations where space is extremely limited. -->
     */
    private boolean shouldAllowTextWithIcon() {
        final android.content.res.Configuration config = getContext().getResources().getConfiguration();
        final int widthDp = android.support.v4.content.res.ConfigurationHelper.getScreenWidthDp(getResources());
        final int heightDp = android.support.v4.content.res.ConfigurationHelper.getScreenHeightDp(getResources());
        return ((widthDp >= 480) || ((widthDp >= 640) && (heightDp >= 480))) || (config.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE);
    }

    @java.lang.Override
    public void setPadding(int l, int t, int r, int b) {
        mSavedPaddingLeft = l;
        super.setPadding(l, t, r, b);
    }

    public android.support.v7.view.menu.MenuItemImpl getItemData() {
        return mItemData;
    }

    public void initialize(android.support.v7.view.menu.MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitleForItemView(this));// Title only takes effect if there is no icon

        setId(itemData.getItemId());
        setVisibility(itemData.isVisible() ? android.view.View.VISIBLE : android.view.View.GONE);
        setEnabled(itemData.isEnabled());
        if (itemData.hasSubMenu()) {
            if (mForwardingListener == null) {
                mForwardingListener = new android.support.v7.view.menu.ActionMenuItemView.ActionMenuItemForwardingListener();
            }
        }
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent e) {
        if ((mItemData.hasSubMenu() && (mForwardingListener != null)) && mForwardingListener.onTouch(this, e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    @java.lang.Override
    public void onClick(android.view.View v) {
        if (mItemInvoker != null) {
            mItemInvoker.invokeItem(mItemData);
        }
    }

    public void setItemInvoker(android.support.v7.view.menu.MenuBuilder.ItemInvoker invoker) {
        mItemInvoker = invoker;
    }

    public void setPopupCallback(android.support.v7.view.menu.ActionMenuItemView.PopupCallback popupCallback) {
        mPopupCallback = popupCallback;
    }

    public boolean prefersCondensedTitle() {
        return true;
    }

    public void setCheckable(boolean checkable) {
        // TODO Support checkable action items
    }

    public void setChecked(boolean checked) {
        // TODO Support checkable action items
    }

    public void setExpandedFormat(boolean expandedFormat) {
        if (mExpandedFormat != expandedFormat) {
            mExpandedFormat = expandedFormat;
            if (mItemData != null) {
                mItemData.actionFormatChanged();
            }
        }
    }

    private void updateTextButtonVisibility() {
        boolean visible = !android.text.TextUtils.isEmpty(mTitle);
        visible &= (mIcon == null) || (mItemData.showsTextAsAction() && (mAllowTextWithIcon || mExpandedFormat));
        setText(visible ? mTitle : null);
    }

    public void setIcon(android.graphics.drawable.Drawable icon) {
        mIcon = icon;
        if (icon != null) {
            int width = icon.getIntrinsicWidth();
            int height = icon.getIntrinsicHeight();
            if (width > mMaxIconSize) {
                final float scale = ((float) (mMaxIconSize)) / width;
                width = mMaxIconSize;
                height *= scale;
            }
            if (height > mMaxIconSize) {
                final float scale = ((float) (mMaxIconSize)) / height;
                height = mMaxIconSize;
                width *= scale;
            }
            icon.setBounds(0, 0, width, height);
        }
        setCompoundDrawables(icon, null, null, null);
        updateTextButtonVisibility();
    }

    public boolean hasText() {
        return !android.text.TextUtils.isEmpty(getText());
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
        // Action buttons don't show text for shortcut keys.
    }

    public void setTitle(java.lang.CharSequence title) {
        mTitle = title;
        setContentDescription(mTitle);
        updateTextButtonVisibility();
    }

    public boolean showsIcon() {
        return true;
    }

    public boolean needsDividerBefore() {
        return hasText() && (mItemData.getIcon() == null);
    }

    public boolean needsDividerAfter() {
        return hasText();
    }

    @java.lang.Override
    public boolean onLongClick(android.view.View v) {
        if (hasText()) {
            // Don't show the cheat sheet for items that already show text.
            return false;
        }
        final int[] screenPos = new int[2];
        final android.graphics.Rect displayFrame = new android.graphics.Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);
        final android.content.Context context = getContext();
        final int width = getWidth();
        final int height = getHeight();
        final int midy = screenPos[1] + (height / 2);
        int referenceX = screenPos[0] + (width / 2);
        if (android.support.v4.view.ViewCompat.getLayoutDirection(v) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) {
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            referenceX = screenWidth - referenceX;// mirror

        }
        android.widget.Toast cheatSheet = android.widget.Toast.makeText(context, mItemData.getTitle(), android.widget.Toast.LENGTH_SHORT);
        if (midy < displayFrame.height()) {
            // Show along the top; follow action buttons
            cheatSheet.setGravity(android.view.Gravity.TOP | android.support.v4.view.GravityCompat.END, referenceX, (screenPos[1] + height) - displayFrame.top);
        } else {
            // Show along the bottom center
            cheatSheet.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, height);
        }
        cheatSheet.show();
        return true;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final boolean textVisible = hasText();
        if (textVisible && (mSavedPaddingLeft >= 0)) {
            super.setPadding(mSavedPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int oldMeasuredWidth = getMeasuredWidth();
        final int targetWidth = (widthMode == android.view.View.MeasureSpec.AT_MOST) ? java.lang.Math.min(widthSize, mMinWidth) : mMinWidth;
        if (((widthMode != android.view.View.MeasureSpec.EXACTLY) && (mMinWidth > 0)) && (oldMeasuredWidth < targetWidth)) {
            // Remeasure at exactly the minimum width.
            super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(targetWidth, android.view.View.MeasureSpec.EXACTLY), heightMeasureSpec);
        }
        if ((!textVisible) && (mIcon != null)) {
            // TextView won't center compound drawables in both dimensions without
            // a little coercion. Pad in to center the icon after we've measured.
            final int w = getMeasuredWidth();
            final int dw = mIcon.getBounds().width();
            super.setPadding((w - dw) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    }

    // Do not backport the framework impl here.
    // The framework's ListPopupWindow uses an animation before performing the item click
    // after selecting an item. As AppCompat doesn't use an animation, the popup is
    // dismissed and thus null'ed out before onForwardingStopped() has been called.
    // This messes up ActionMenuItemView's onForwardingStopped() impl since it will now
    // return false and make ListPopupWindow think it's still forwarding.
    private class ActionMenuItemForwardingListener extends android.support.v7.widget.ForwardingListener {
        public ActionMenuItemForwardingListener() {
            super(android.support.v7.view.menu.ActionMenuItemView.this);
        }

        @java.lang.Override
        public android.support.v7.view.menu.ShowableListMenu getPopup() {
            if (mPopupCallback != null) {
                return mPopupCallback.getPopup();
            }
            return null;
        }

        @java.lang.Override
        protected boolean onForwardingStarted() {
            // Call the invoker, then check if the expected popup is showing.
            if ((mItemInvoker != null) && mItemInvoker.invokeItem(mItemData)) {
                final android.support.v7.view.menu.ShowableListMenu popup = getPopup();
                return (popup != null) && popup.isShowing();
            }
            return false;
        }
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        // This might get called with the state of ActionView since it shares the same ID with
        // ActionMenuItemView. Do not restore this state as ActionMenuItemView never saved it.
        super.onRestoreInstanceState(null);
    }

    public static abstract class PopupCallback {
        public abstract android.support.v7.view.menu.ShowableListMenu getPopup();
    }
}

