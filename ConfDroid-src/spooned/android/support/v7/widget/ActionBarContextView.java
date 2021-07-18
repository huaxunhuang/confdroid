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
package android.support.v7.widget;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ActionBarContextView extends android.support.v7.widget.AbsActionBarView {
    private static final java.lang.String TAG = "ActionBarContextView";

    private java.lang.CharSequence mTitle;

    private java.lang.CharSequence mSubtitle;

    private android.view.View mClose;

    private android.view.View mCustomView;

    private android.widget.LinearLayout mTitleLayout;

    private android.widget.TextView mTitleView;

    private android.widget.TextView mSubtitleView;

    private int mTitleStyleRes;

    private int mSubtitleStyleRes;

    private boolean mTitleOptional;

    private int mCloseItemLayout;

    public ActionBarContextView(android.content.Context context) {
        this(context, null);
    }

    public ActionBarContextView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.actionModeStyle);
    }

    public ActionBarContextView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.ActionMode, defStyle, 0);
        android.support.v4.view.ViewCompat.setBackground(this, a.getDrawable(R.styleable.ActionMode_background));
        mTitleStyleRes = a.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        mSubtitleStyleRes = a.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        mContentHeight = a.getLayoutDimension(R.styleable.ActionMode_height, 0);
        mCloseItemLayout = a.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        a.recycle();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.hideOverflowMenu();
            mActionMenuPresenter.hideSubMenus();
        }
    }

    public void setContentHeight(int height) {
        mContentHeight = height;
    }

    public void setCustomView(android.view.View view) {
        if (mCustomView != null) {
            removeView(mCustomView);
        }
        mCustomView = view;
        if ((view != null) && (mTitleLayout != null)) {
            removeView(mTitleLayout);
            mTitleLayout = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setTitle(java.lang.CharSequence title) {
        mTitle = title;
        initTitle();
    }

    public void setSubtitle(java.lang.CharSequence subtitle) {
        mSubtitle = subtitle;
        initTitle();
    }

    public java.lang.CharSequence getTitle() {
        return mTitle;
    }

    public java.lang.CharSequence getSubtitle() {
        return mSubtitle;
    }

    private void initTitle() {
        if (mTitleLayout == null) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
            inflater.inflate(R.layout.abc_action_bar_title_item, this);
            mTitleLayout = ((android.widget.LinearLayout) (getChildAt(getChildCount() - 1)));
            mTitleView = ((android.widget.TextView) (mTitleLayout.findViewById(R.id.action_bar_title)));
            mSubtitleView = ((android.widget.TextView) (mTitleLayout.findViewById(R.id.action_bar_subtitle)));
            if (mTitleStyleRes != 0) {
                mTitleView.setTextAppearance(getContext(), mTitleStyleRes);
            }
            if (mSubtitleStyleRes != 0) {
                mSubtitleView.setTextAppearance(getContext(), mSubtitleStyleRes);
            }
        }
        mTitleView.setText(mTitle);
        mSubtitleView.setText(mSubtitle);
        final boolean hasTitle = !android.text.TextUtils.isEmpty(mTitle);
        final boolean hasSubtitle = !android.text.TextUtils.isEmpty(mSubtitle);
        mSubtitleView.setVisibility(hasSubtitle ? android.view.View.VISIBLE : android.view.View.GONE);
        mTitleLayout.setVisibility(hasTitle || hasSubtitle ? android.view.View.VISIBLE : android.view.View.GONE);
        if (mTitleLayout.getParent() == null) {
            addView(mTitleLayout);
        }
    }

    public void initForMode(final android.support.v7.view.ActionMode mode) {
        if (mClose == null) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
            mClose = inflater.inflate(mCloseItemLayout, this, false);
            addView(mClose);
        } else
            if (mClose.getParent() == null) {
                addView(mClose);
            }

        android.view.View closeButton = mClose.findViewById(R.id.action_mode_close_button);
        closeButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mode.finish();
            }
        });
        final android.support.v7.view.menu.MenuBuilder menu = ((android.support.v7.view.menu.MenuBuilder) (mode.getMenu()));
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.dismissPopupMenus();
        }
        mActionMenuPresenter = new android.support.v7.widget.ActionMenuPresenter(getContext());
        mActionMenuPresenter.setReserveOverflow(true);
        final android.view.ViewGroup.LayoutParams layoutParams = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        menu.addMenuPresenter(mActionMenuPresenter, mPopupContext);
        mMenuView = ((android.support.v7.widget.ActionMenuView) (mActionMenuPresenter.getMenuView(this)));
        android.support.v4.view.ViewCompat.setBackground(mMenuView, null);
        addView(mMenuView, layoutParams);
    }

    public void closeMode() {
        if (mClose == null) {
            killMode();
            return;
        }
    }

    public void killMode() {
        removeAllViews();
        mCustomView = null;
        mMenuView = null;
    }

    @java.lang.Override
    public boolean showOverflowMenu() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

    @java.lang.Override
    public boolean hideOverflowMenu() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    @java.lang.Override
    public boolean isOverflowMenuShowing() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        // Used by custom views if they don't supply layout params. Everything else
        // added to an ActionBarContextView should have them already.
        return new android.view.ViewGroup.MarginLayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.view.ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != android.view.View.MeasureSpec.EXACTLY) {
            throw new java.lang.IllegalStateException((getClass().getSimpleName() + " can only be used ") + "with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == android.view.View.MeasureSpec.UNSPECIFIED) {
            throw new java.lang.IllegalStateException((getClass().getSimpleName() + " can only be used ") + "with android:layout_height=\"wrap_content\"");
        }
        final int contentWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = (mContentHeight > 0) ? mContentHeight : android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        int availableWidth = (contentWidth - getPaddingLeft()) - getPaddingRight();
        final int height = maxHeight - verticalPadding;
        final int childSpecHeight = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.AT_MOST);
        if (mClose != null) {
            availableWidth = measureChildView(mClose, availableWidth, childSpecHeight, 0);
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mClose.getLayoutParams()));
            availableWidth -= lp.leftMargin + lp.rightMargin;
        }
        if ((mMenuView != null) && (mMenuView.getParent() == this)) {
            availableWidth = measureChildView(mMenuView, availableWidth, childSpecHeight, 0);
        }
        if ((mTitleLayout != null) && (mCustomView == null)) {
            if (mTitleOptional) {
                final int titleWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
                mTitleLayout.measure(titleWidthSpec, childSpecHeight);
                final int titleWidth = mTitleLayout.getMeasuredWidth();
                final boolean titleFits = titleWidth <= availableWidth;
                if (titleFits) {
                    availableWidth -= titleWidth;
                }
                mTitleLayout.setVisibility(titleFits ? android.view.View.VISIBLE : android.view.View.GONE);
            } else {
                availableWidth = measureChildView(mTitleLayout, availableWidth, childSpecHeight, 0);
            }
        }
        if (mCustomView != null) {
            android.view.ViewGroup.LayoutParams lp = mCustomView.getLayoutParams();
            final int customWidthMode = (lp.width != android.view.ViewGroup.LayoutParams.WRAP_CONTENT) ? android.view.View.MeasureSpec.EXACTLY : android.view.View.MeasureSpec.AT_MOST;
            final int customWidth = (lp.width >= 0) ? java.lang.Math.min(lp.width, availableWidth) : availableWidth;
            final int customHeightMode = (lp.height != android.view.ViewGroup.LayoutParams.WRAP_CONTENT) ? android.view.View.MeasureSpec.EXACTLY : android.view.View.MeasureSpec.AT_MOST;
            final int customHeight = (lp.height >= 0) ? java.lang.Math.min(lp.height, height) : height;
            mCustomView.measure(android.view.View.MeasureSpec.makeMeasureSpec(customWidth, customWidthMode), android.view.View.MeasureSpec.makeMeasureSpec(customHeight, customHeightMode));
        }
        if (mContentHeight <= 0) {
            int measuredHeight = 0;
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.View v = getChildAt(i);
                int paddedViewHeight = v.getMeasuredHeight() + verticalPadding;
                if (paddedViewHeight > measuredHeight) {
                    measuredHeight = paddedViewHeight;
                }
            }
            setMeasuredDimension(contentWidth, measuredHeight);
        } else {
            setMeasuredDimension(contentWidth, maxHeight);
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final boolean isLayoutRtl = android.support.v7.widget.ViewUtils.isLayoutRtl(this);
        int x = (isLayoutRtl) ? (r - l) - getPaddingRight() : getPaddingLeft();
        final int y = getPaddingTop();
        final int contentHeight = ((b - t) - getPaddingTop()) - getPaddingBottom();
        if ((mClose != null) && (mClose.getVisibility() != android.view.View.GONE)) {
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mClose.getLayoutParams()));
            final int startMargin = (isLayoutRtl) ? lp.rightMargin : lp.leftMargin;
            final int endMargin = (isLayoutRtl) ? lp.leftMargin : lp.rightMargin;
            x = android.support.v7.widget.AbsActionBarView.next(x, startMargin, isLayoutRtl);
            x += positionChild(mClose, x, y, contentHeight, isLayoutRtl);
            x = android.support.v7.widget.AbsActionBarView.next(x, endMargin, isLayoutRtl);
        }
        if (((mTitleLayout != null) && (mCustomView == null)) && (mTitleLayout.getVisibility() != android.view.View.GONE)) {
            x += positionChild(mTitleLayout, x, y, contentHeight, isLayoutRtl);
        }
        if (mCustomView != null) {
            x += positionChild(mCustomView, x, y, contentHeight, isLayoutRtl);
        }
        x = (isLayoutRtl) ? getPaddingLeft() : (r - l) - getPaddingRight();
        if (mMenuView != null) {
            x += positionChild(mMenuView, x, y, contentHeight, !isLayoutRtl);
        }
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @java.lang.Override
    public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            if (event.getEventType() == android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // Action mode started
                event.setSource(this);
                event.setClassName(getClass().getName());
                event.setPackageName(getContext().getPackageName());
                event.setContentDescription(mTitle);
            } else {
                super.onInitializeAccessibilityEvent(event);
            }
        }
    }

    public void setTitleOptional(boolean titleOptional) {
        if (titleOptional != mTitleOptional) {
            requestLayout();
        }
        mTitleOptional = titleOptional;
    }

    public boolean isTitleOptional() {
        return mTitleOptional;
    }
}

