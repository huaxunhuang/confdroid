/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * This widget implements the dynamic action bar tab behavior that can change across different
 * configurations or circumstances.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ScrollingTabContainerView extends android.widget.HorizontalScrollView implements android.widget.AdapterView.OnItemSelectedListener {
    private static final java.lang.String TAG = "ScrollingTabContainerView";

    java.lang.Runnable mTabSelector;

    private android.support.v7.widget.ScrollingTabContainerView.TabClickListener mTabClickListener;

    android.support.v7.widget.LinearLayoutCompat mTabLayout;

    private android.widget.Spinner mTabSpinner;

    private boolean mAllowCollapse;

    int mMaxTabWidth;

    int mStackedTabMaxWidth;

    private int mContentHeight;

    private int mSelectedTabIndex;

    protected android.support.v4.view.ViewPropertyAnimatorCompat mVisibilityAnim;

    protected final android.support.v7.widget.ScrollingTabContainerView.VisibilityAnimListener mVisAnimListener = new android.support.v7.widget.ScrollingTabContainerView.VisibilityAnimListener();

    private static final android.view.animation.Interpolator sAlphaInterpolator = new android.view.animation.DecelerateInterpolator();

    private static final int FADE_DURATION = 200;

    public ScrollingTabContainerView(android.content.Context context) {
        super(context);
        setHorizontalScrollBarEnabled(false);
        android.support.v7.view.ActionBarPolicy abp = android.support.v7.view.ActionBarPolicy.get(context);
        setContentHeight(abp.getTabContainerHeight());
        mStackedTabMaxWidth = abp.getStackedTabMaxWidth();
        mTabLayout = createTabLayout();
        addView(mTabLayout, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @java.lang.Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == android.view.View.MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);
        final int childCount = mTabLayout.getChildCount();
        if ((childCount > 1) && ((widthMode == android.view.View.MeasureSpec.EXACTLY) || (widthMode == android.view.View.MeasureSpec.AT_MOST))) {
            if (childCount > 2) {
                mMaxTabWidth = ((int) (android.view.View.MeasureSpec.getSize(widthMeasureSpec) * 0.4F));
            } else {
                mMaxTabWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
            mMaxTabWidth = java.lang.Math.min(mMaxTabWidth, mStackedTabMaxWidth);
        } else {
            mMaxTabWidth = -1;
        }
        heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(mContentHeight, android.view.View.MeasureSpec.EXACTLY);
        final boolean canCollapse = (!lockedExpanded) && mAllowCollapse;
        if (canCollapse) {
            // See if we should expand
            mTabLayout.measure(android.view.View.MeasureSpec.UNSPECIFIED, heightMeasureSpec);
            if (mTabLayout.getMeasuredWidth() > android.view.View.MeasureSpec.getSize(widthMeasureSpec)) {
                performCollapse();
            } else {
                performExpand();
            }
        } else {
            performExpand();
        }
        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();
        if (lockedExpanded && (oldWidth != newWidth)) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setTabSelected(mSelectedTabIndex);
        }
    }

    /**
     * Indicates whether this view is collapsed into a dropdown menu instead
     * of traditional tabs.
     *
     * @return true if showing as a spinner
     */
    private boolean isCollapsed() {
        return (mTabSpinner != null) && (mTabSpinner.getParent() == this);
    }

    public void setAllowCollapse(boolean allowCollapse) {
        mAllowCollapse = allowCollapse;
    }

    private void performCollapse() {
        if (isCollapsed())
            return;

        if (mTabSpinner == null) {
            mTabSpinner = createSpinner();
        }
        removeView(mTabLayout);
        addView(mTabSpinner, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        if (mTabSpinner.getAdapter() == null) {
            mTabSpinner.setAdapter(new android.support.v7.widget.ScrollingTabContainerView.TabAdapter());
        }
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
            mTabSelector = null;
        }
        mTabSpinner.setSelection(mSelectedTabIndex);
    }

    private boolean performExpand() {
        if (!isCollapsed())
            return false;

        removeView(mTabSpinner);
        addView(mTabLayout, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        setTabSelected(mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void setTabSelected(int position) {
        mSelectedTabIndex = position;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final android.view.View child = mTabLayout.getChildAt(i);
            final boolean isSelected = i == position;
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(position);
            }
        }
        if ((mTabSpinner != null) && (position >= 0)) {
            mTabSpinner.setSelection(position);
        }
    }

    public void setContentHeight(int contentHeight) {
        mContentHeight = contentHeight;
        requestLayout();
    }

    private android.support.v7.widget.LinearLayoutCompat createTabLayout() {
        final android.support.v7.widget.LinearLayoutCompat tabLayout = new android.support.v7.widget.LinearLayoutCompat(getContext(), null, R.attr.actionBarTabBarStyle);
        tabLayout.setMeasureWithLargestChildEnabled(true);
        tabLayout.setGravity(android.view.Gravity.CENTER);
        tabLayout.setLayoutParams(new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT));
        return tabLayout;
    }

    private android.widget.Spinner createSpinner() {
        final android.widget.Spinner spinner = new android.support.v7.widget.AppCompatSpinner(getContext(), null, R.attr.actionDropDownStyle);
        spinner.setLayoutParams(new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT));
        spinner.setOnItemSelectedListener(this);
        return spinner;
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        android.support.v7.view.ActionBarPolicy abp = android.support.v7.view.ActionBarPolicy.get(getContext());
        // Action bar can change size on configuration changes.
        // Reread the desired height from the theme-specified style.
        setContentHeight(abp.getTabContainerHeight());
        mStackedTabMaxWidth = abp.getStackedTabMaxWidth();
    }

    public void animateToVisibility(int visibility) {
        if (mVisibilityAnim != null) {
            mVisibilityAnim.cancel();
        }
        if (visibility == android.view.View.VISIBLE) {
            if (getVisibility() != android.view.View.VISIBLE) {
                android.support.v4.view.ViewCompat.setAlpha(this, 0.0F);
            }
            android.support.v4.view.ViewPropertyAnimatorCompat anim = android.support.v4.view.ViewCompat.animate(this).alpha(1.0F);
            anim.setDuration(android.support.v7.widget.ScrollingTabContainerView.FADE_DURATION);
            anim.setInterpolator(android.support.v7.widget.ScrollingTabContainerView.sAlphaInterpolator);
            anim.setListener(mVisAnimListener.withFinalVisibility(anim, visibility));
            anim.start();
        } else {
            android.support.v4.view.ViewPropertyAnimatorCompat anim = android.support.v4.view.ViewCompat.animate(this).alpha(0.0F);
            anim.setDuration(android.support.v7.widget.ScrollingTabContainerView.FADE_DURATION);
            anim.setInterpolator(android.support.v7.widget.ScrollingTabContainerView.sAlphaInterpolator);
            anim.setListener(mVisAnimListener.withFinalVisibility(anim, visibility));
            anim.start();
        }
    }

    public void animateToTab(final int position) {
        final android.view.View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                final int scrollPos = tabView.getLeft() - ((getWidth() - tabView.getWidth()) / 2);
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    android.support.v7.widget.ScrollingTabContainerView.TabView createTabView(android.support.v7.app.ActionBar.Tab tab, boolean forAdapter) {
        final android.support.v7.widget.ScrollingTabContainerView.TabView tabView = new android.support.v7.widget.ScrollingTabContainerView.TabView(getContext(), tab, forAdapter);
        if (forAdapter) {
            tabView.setBackgroundDrawable(null);
            tabView.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.MATCH_PARENT, mContentHeight));
        } else {
            tabView.setFocusable(true);
            if (mTabClickListener == null) {
                mTabClickListener = new android.support.v7.widget.ScrollingTabContainerView.TabClickListener();
            }
            tabView.setOnClickListener(mTabClickListener);
        }
        return tabView;
    }

    public void addTab(android.support.v7.app.ActionBar.Tab tab, boolean setSelected) {
        android.support.v7.widget.ScrollingTabContainerView.TabView tabView = createTabView(tab, false);
        mTabLayout.addView(tabView, new android.support.v7.widget.LinearLayoutCompat.LayoutParams(0, android.widget.FrameLayout.LayoutParams.MATCH_PARENT, 1));
        if (mTabSpinner != null) {
            ((android.support.v7.widget.ScrollingTabContainerView.TabAdapter) (mTabSpinner.getAdapter())).notifyDataSetChanged();
        }
        if (setSelected) {
            tabView.setSelected(true);
        }
        if (mAllowCollapse) {
            requestLayout();
        }
    }

    public void addTab(android.support.v7.app.ActionBar.Tab tab, int position, boolean setSelected) {
        final android.support.v7.widget.ScrollingTabContainerView.TabView tabView = createTabView(tab, false);
        mTabLayout.addView(tabView, position, new android.support.v7.widget.LinearLayoutCompat.LayoutParams(0, android.widget.FrameLayout.LayoutParams.MATCH_PARENT, 1));
        if (mTabSpinner != null) {
            ((android.support.v7.widget.ScrollingTabContainerView.TabAdapter) (mTabSpinner.getAdapter())).notifyDataSetChanged();
        }
        if (setSelected) {
            tabView.setSelected(true);
        }
        if (mAllowCollapse) {
            requestLayout();
        }
    }

    public void updateTab(int position) {
        ((android.support.v7.widget.ScrollingTabContainerView.TabView) (mTabLayout.getChildAt(position))).update();
        if (mTabSpinner != null) {
            ((android.support.v7.widget.ScrollingTabContainerView.TabAdapter) (mTabSpinner.getAdapter())).notifyDataSetChanged();
        }
        if (mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeTabAt(int position) {
        mTabLayout.removeViewAt(position);
        if (mTabSpinner != null) {
            ((android.support.v7.widget.ScrollingTabContainerView.TabAdapter) (mTabSpinner.getAdapter())).notifyDataSetChanged();
        }
        if (mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeAllTabs() {
        mTabLayout.removeAllViews();
        if (mTabSpinner != null) {
            ((android.support.v7.widget.ScrollingTabContainerView.TabAdapter) (mTabSpinner.getAdapter())).notifyDataSetChanged();
        }
        if (mAllowCollapse) {
            requestLayout();
        }
    }

    @java.lang.Override
    public void onItemSelected(android.widget.AdapterView<?> adapterView, android.view.View view, int position, long id) {
        android.support.v7.widget.ScrollingTabContainerView.TabView tabView = ((android.support.v7.widget.ScrollingTabContainerView.TabView) (view));
        tabView.getTab().select();
    }

    @java.lang.Override
    public void onNothingSelected(android.widget.AdapterView<?> adapterView) {
        // no-op
    }

    private class TabView extends android.support.v7.widget.LinearLayoutCompat implements android.view.View.OnLongClickListener {
        private final int[] BG_ATTRS = new int[]{ android.R.attr.background };

        private android.support.v7.app.ActionBar.Tab mTab;

        private android.widget.TextView mTextView;

        private android.widget.ImageView mIconView;

        private android.view.View mCustomView;

        public TabView(android.content.Context context, android.support.v7.app.ActionBar.Tab tab, boolean forList) {
            super(context, null, R.attr.actionBarTabStyle);
            mTab = tab;
            android.support.v7.widget.TintTypedArray a = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(context, null, BG_ATTRS, R.attr.actionBarTabStyle, 0);
            if (a.hasValue(0)) {
                setBackgroundDrawable(a.getDrawable(0));
            }
            a.recycle();
            if (forList) {
                setGravity(android.support.v4.view.GravityCompat.START | android.view.Gravity.CENTER_VERTICAL);
            }
            update();
        }

        public void bindTab(android.support.v7.app.ActionBar.Tab tab) {
            mTab = tab;
            update();
        }

        @java.lang.Override
        public void setSelected(boolean selected) {
            final boolean changed = isSelected() != selected;
            super.setSelected(selected);
            if (changed && selected) {
                sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SELECTED);
            }
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            // This view masquerades as an action bar tab.
            event.setClassName(android.support.v7.app.ActionBar.Tab.class.getName());
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                // This view masquerades as an action bar tab.
                info.setClassName(android.support.v7.app.ActionBar.Tab.class.getName());
            }
        }

        @java.lang.Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Re-measure if we went beyond our maximum size.
            if ((mMaxTabWidth > 0) && (getMeasuredWidth() > mMaxTabWidth)) {
                super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(mMaxTabWidth, android.view.View.MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }

        public void update() {
            final android.support.v7.app.ActionBar.Tab tab = mTab;
            final android.view.View custom = tab.getCustomView();
            if (custom != null) {
                final android.view.ViewParent customParent = custom.getParent();
                if (customParent != this) {
                    if (customParent != null)
                        ((android.view.ViewGroup) (customParent)).removeView(custom);

                    addView(custom);
                }
                mCustomView = custom;
                if (mTextView != null)
                    mTextView.setVisibility(android.view.View.GONE);

                if (mIconView != null) {
                    mIconView.setVisibility(android.view.View.GONE);
                    mIconView.setImageDrawable(null);
                }
            } else {
                if (mCustomView != null) {
                    removeView(mCustomView);
                    mCustomView = null;
                }
                final android.graphics.drawable.Drawable icon = tab.getIcon();
                final java.lang.CharSequence text = tab.getText();
                if (icon != null) {
                    if (mIconView == null) {
                        android.widget.ImageView iconView = new android.support.v7.widget.AppCompatImageView(getContext());
                        android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        lp.gravity = android.view.Gravity.CENTER_VERTICAL;
                        iconView.setLayoutParams(lp);
                        addView(iconView, 0);
                        mIconView = iconView;
                    }
                    mIconView.setImageDrawable(icon);
                    mIconView.setVisibility(android.view.View.VISIBLE);
                } else
                    if (mIconView != null) {
                        mIconView.setVisibility(android.view.View.GONE);
                        mIconView.setImageDrawable(null);
                    }

                final boolean hasText = !android.text.TextUtils.isEmpty(text);
                if (hasText) {
                    if (mTextView == null) {
                        android.widget.TextView textView = new android.support.v7.widget.AppCompatTextView(getContext(), null, R.attr.actionBarTabTextStyle);
                        textView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                        android.support.v7.widget.LinearLayoutCompat.LayoutParams lp = new android.support.v7.widget.LinearLayoutCompat.LayoutParams(android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT, android.support.v7.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        lp.gravity = android.view.Gravity.CENTER_VERTICAL;
                        textView.setLayoutParams(lp);
                        addView(textView);
                        mTextView = textView;
                    }
                    mTextView.setText(text);
                    mTextView.setVisibility(android.view.View.VISIBLE);
                } else
                    if (mTextView != null) {
                        mTextView.setVisibility(android.view.View.GONE);
                        mTextView.setText(null);
                    }

                if (mIconView != null) {
                    mIconView.setContentDescription(tab.getContentDescription());
                }
                if ((!hasText) && (!android.text.TextUtils.isEmpty(tab.getContentDescription()))) {
                    setOnLongClickListener(this);
                } else {
                    setOnLongClickListener(null);
                    setLongClickable(false);
                }
            }
        }

        @java.lang.Override
        public boolean onLongClick(android.view.View v) {
            final int[] screenPos = new int[2];
            getLocationOnScreen(screenPos);
            final android.content.Context context = getContext();
            final int width = getWidth();
            final int height = getHeight();
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            android.widget.Toast cheatSheet = android.widget.Toast.makeText(context, mTab.getContentDescription(), android.widget.Toast.LENGTH_SHORT);
            // Show under the tab
            cheatSheet.setGravity(android.view.Gravity.TOP | android.view.Gravity.CENTER_HORIZONTAL, (screenPos[0] + (width / 2)) - (screenWidth / 2), height);
            cheatSheet.show();
            return true;
        }

        public android.support.v7.app.ActionBar.Tab getTab() {
            return mTab;
        }
    }

    private class TabAdapter extends android.widget.BaseAdapter {
        TabAdapter() {
        }

        @java.lang.Override
        public int getCount() {
            return mTabLayout.getChildCount();
        }

        @java.lang.Override
        public java.lang.Object getItem(int position) {
            return ((android.support.v7.widget.ScrollingTabContainerView.TabView) (mTabLayout.getChildAt(position))).getTab();
        }

        @java.lang.Override
        public long getItemId(int position) {
            return position;
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = createTabView(((android.support.v7.app.ActionBar.Tab) (getItem(position))), true);
            } else {
                ((android.support.v7.widget.ScrollingTabContainerView.TabView) (convertView)).bindTab(((android.support.v7.app.ActionBar.Tab) (getItem(position))));
            }
            return convertView;
        }
    }

    private class TabClickListener implements android.view.View.OnClickListener {
        TabClickListener() {
        }

        @java.lang.Override
        public void onClick(android.view.View view) {
            android.support.v7.widget.ScrollingTabContainerView.TabView tabView = ((android.support.v7.widget.ScrollingTabContainerView.TabView) (view));
            tabView.getTab().select();
            final int tabCount = mTabLayout.getChildCount();
            for (int i = 0; i < tabCount; i++) {
                final android.view.View child = mTabLayout.getChildAt(i);
                child.setSelected(child == view);
            }
        }
    }

    protected class VisibilityAnimListener implements android.support.v4.view.ViewPropertyAnimatorListener {
        private boolean mCanceled = false;

        private int mFinalVisibility;

        public android.support.v7.widget.ScrollingTabContainerView.VisibilityAnimListener withFinalVisibility(android.support.v4.view.ViewPropertyAnimatorCompat animation, int visibility) {
            mFinalVisibility = visibility;
            mVisibilityAnim = animation;
            return this;
        }

        @java.lang.Override
        public void onAnimationStart(android.view.View view) {
            setVisibility(android.view.View.VISIBLE);
            mCanceled = false;
        }

        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            if (mCanceled)
                return;

            mVisibilityAnim = null;
            setVisibility(mFinalVisibility);
        }

        @java.lang.Override
        public void onAnimationCancel(android.view.View view) {
            mCanceled = true;
        }
    }
}

