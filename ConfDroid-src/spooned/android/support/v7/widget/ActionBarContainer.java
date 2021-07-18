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
 * This class acts as a container for the action bar view and action mode context views.
 * It applies special styles as needed to help handle animated transitions between them.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ActionBarContainer extends android.widget.FrameLayout {
    private boolean mIsTransitioning;

    private android.view.View mTabContainer;

    private android.view.View mActionBarView;

    private android.view.View mContextView;

    android.graphics.drawable.Drawable mBackground;

    android.graphics.drawable.Drawable mStackedBackground;

    android.graphics.drawable.Drawable mSplitBackground;

    boolean mIsSplit;

    boolean mIsStacked;

    private int mHeight;

    public ActionBarContainer(android.content.Context context) {
        this(context, null);
    }

    public ActionBarContainer(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        // Set a transparent background so that we project appropriately.
        final android.graphics.drawable.Drawable bg = (android.os.Build.VERSION.SDK_INT >= 21) ? new android.support.v7.widget.ActionBarBackgroundDrawableV21(this) : new android.support.v7.widget.ActionBarBackgroundDrawable(this);
        android.support.v4.view.ViewCompat.setBackground(this, bg);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        mBackground = a.getDrawable(R.styleable.ActionBar_background);
        mStackedBackground = a.getDrawable(R.styleable.ActionBar_backgroundStacked);
        mHeight = a.getDimensionPixelSize(R.styleable.ActionBar_height, -1);
        if (getId() == R.id.split_action_bar) {
            mIsSplit = true;
            mSplitBackground = a.getDrawable(R.styleable.ActionBar_backgroundSplit);
        }
        a.recycle();
        setWillNotDraw(mIsSplit ? mSplitBackground == null : (mBackground == null) && (mStackedBackground == null));
    }

    @java.lang.Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mActionBarView = findViewById(R.id.action_bar);
        mContextView = findViewById(R.id.action_context_bar);
    }

    public void setPrimaryBackground(android.graphics.drawable.Drawable bg) {
        if (mBackground != null) {
            mBackground.setCallback(null);
            unscheduleDrawable(mBackground);
        }
        mBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (mActionBarView != null) {
                mBackground.setBounds(mActionBarView.getLeft(), mActionBarView.getTop(), mActionBarView.getRight(), mActionBarView.getBottom());
            }
        }
        setWillNotDraw(mIsSplit ? mSplitBackground == null : (mBackground == null) && (mStackedBackground == null));
        invalidate();
    }

    public void setStackedBackground(android.graphics.drawable.Drawable bg) {
        if (mStackedBackground != null) {
            mStackedBackground.setCallback(null);
            unscheduleDrawable(mStackedBackground);
        }
        mStackedBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (mIsStacked && (mStackedBackground != null)) {
                mStackedBackground.setBounds(mTabContainer.getLeft(), mTabContainer.getTop(), mTabContainer.getRight(), mTabContainer.getBottom());
            }
        }
        setWillNotDraw(mIsSplit ? mSplitBackground == null : (mBackground == null) && (mStackedBackground == null));
        invalidate();
    }

    public void setSplitBackground(android.graphics.drawable.Drawable bg) {
        if (mSplitBackground != null) {
            mSplitBackground.setCallback(null);
            unscheduleDrawable(mSplitBackground);
        }
        mSplitBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (mIsSplit && (mSplitBackground != null)) {
                mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        setWillNotDraw(mIsSplit ? mSplitBackground == null : (mBackground == null) && (mStackedBackground == null));
        invalidate();
    }

    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        final boolean isVisible = visibility == android.view.View.VISIBLE;
        if (mBackground != null)
            mBackground.setVisible(isVisible, false);

        if (mStackedBackground != null)
            mStackedBackground.setVisible(isVisible, false);

        if (mSplitBackground != null)
            mSplitBackground.setVisible(isVisible, false);

    }

    @java.lang.Override
    protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
        return ((((who == mBackground) && (!mIsSplit)) || ((who == mStackedBackground) && mIsStacked)) || ((who == mSplitBackground) && mIsSplit)) || super.verifyDrawable(who);
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((mBackground != null) && mBackground.isStateful()) {
            mBackground.setState(getDrawableState());
        }
        if ((mStackedBackground != null) && mStackedBackground.isStateful()) {
            mStackedBackground.setState(getDrawableState());
        }
        if ((mSplitBackground != null) && mSplitBackground.isStateful()) {
            mSplitBackground.setState(getDrawableState());
        }
    }

    public void jumpDrawablesToCurrentState() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            super.jumpDrawablesToCurrentState();
            if (mBackground != null) {
                mBackground.jumpToCurrentState();
            }
            if (mStackedBackground != null) {
                mStackedBackground.jumpToCurrentState();
            }
            if (mSplitBackground != null) {
                mSplitBackground.jumpToCurrentState();
            }
        }
    }

    /**
     * Set the action bar into a "transitioning" state. While transitioning the bar will block focus
     * and touch from all of its descendants. This prevents the user from interacting with the bar
     * while it is animating in or out.
     *
     * @param isTransitioning
     * 		true if the bar is currently transitioning, false otherwise.
     */
    public void setTransitioning(boolean isTransitioning) {
        mIsTransitioning = isTransitioning;
        setDescendantFocusability(isTransitioning ? android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS : android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        return mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        super.onTouchEvent(ev);
        // An action bar always eats touch events.
        return true;
    }

    public void setTabContainer(android.support.v7.widget.ScrollingTabContainerView tabView) {
        if (mTabContainer != null) {
            removeView(mTabContainer);
        }
        mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            final android.view.ViewGroup.LayoutParams lp = tabView.getLayoutParams();
            lp.width = android.widget.FrameLayout.LayoutParams.MATCH_PARENT;
            lp.height = android.widget.FrameLayout.LayoutParams.WRAP_CONTENT;
            tabView.setAllowCollapse(false);
        }
    }

    public android.view.View getTabContainer() {
        return mTabContainer;
    }

    public android.view.ActionMode startActionModeForChild(android.view.View child, android.view.ActionMode.Callback callback) {
        // No starting an action mode for an action bar child! (Where would it go?)
        return null;
    }

    public android.view.ActionMode startActionModeForChild(android.view.View child, android.view.ActionMode.Callback callback, int type) {
        if (type != android.view.ActionMode.TYPE_PRIMARY) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    private boolean isCollapsed(android.view.View view) {
        return ((view == null) || (view.getVisibility() == android.view.View.GONE)) || (view.getMeasuredHeight() == 0);
    }

    private int getMeasuredHeightWithMargins(android.view.View view) {
        final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (view.getLayoutParams()));
        return (view.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
    }

    @java.lang.Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (((mActionBarView == null) && (android.view.View.MeasureSpec.getMode(heightMeasureSpec) == android.view.View.MeasureSpec.AT_MOST)) && (mHeight >= 0)) {
            heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.min(mHeight, android.view.View.MeasureSpec.getSize(heightMeasureSpec)), android.view.View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mActionBarView == null)
            return;

        final int mode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if (((mTabContainer != null) && (mTabContainer.getVisibility() != android.view.View.GONE)) && (mode != android.view.View.MeasureSpec.EXACTLY)) {
            final int topMarginForTabs;
            if (!isCollapsed(mActionBarView)) {
                topMarginForTabs = getMeasuredHeightWithMargins(mActionBarView);
            } else
                if (!isCollapsed(mContextView)) {
                    topMarginForTabs = getMeasuredHeightWithMargins(mContextView);
                } else {
                    topMarginForTabs = 0;
                }

            final int maxHeight = (mode == android.view.View.MeasureSpec.AT_MOST) ? android.view.View.MeasureSpec.getSize(heightMeasureSpec) : java.lang.Integer.MAX_VALUE;
            setMeasuredDimension(getMeasuredWidth(), java.lang.Math.min(topMarginForTabs + getMeasuredHeightWithMargins(mTabContainer), maxHeight));
        }
    }

    @java.lang.Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final android.view.View tabContainer = mTabContainer;
        final boolean hasTabs = (tabContainer != null) && (tabContainer.getVisibility() != android.view.View.GONE);
        if ((tabContainer != null) && (tabContainer.getVisibility() != android.view.View.GONE)) {
            final int containerHeight = getMeasuredHeight();
            final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (tabContainer.getLayoutParams()));
            final int tabHeight = tabContainer.getMeasuredHeight();
            tabContainer.layout(l, (containerHeight - tabHeight) - lp.bottomMargin, r, containerHeight - lp.bottomMargin);
        }
        boolean needsInvalidate = false;
        if (mIsSplit) {
            if (mSplitBackground != null) {
                mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                needsInvalidate = true;
            }
        } else {
            if (mBackground != null) {
                if (mActionBarView.getVisibility() == android.view.View.VISIBLE) {
                    mBackground.setBounds(mActionBarView.getLeft(), mActionBarView.getTop(), mActionBarView.getRight(), mActionBarView.getBottom());
                } else
                    if ((mContextView != null) && (mContextView.getVisibility() == android.view.View.VISIBLE)) {
                        mBackground.setBounds(mContextView.getLeft(), mContextView.getTop(), mContextView.getRight(), mContextView.getBottom());
                    } else {
                        mBackground.setBounds(0, 0, 0, 0);
                    }

                needsInvalidate = true;
            }
            mIsStacked = hasTabs;
            if (hasTabs && (mStackedBackground != null)) {
                mStackedBackground.setBounds(tabContainer.getLeft(), tabContainer.getTop(), tabContainer.getRight(), tabContainer.getBottom());
                needsInvalidate = true;
            }
        }
        if (needsInvalidate) {
            invalidate();
        }
    }
}

