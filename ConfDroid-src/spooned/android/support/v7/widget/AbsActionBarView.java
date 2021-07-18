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


abstract class AbsActionBarView extends android.view.ViewGroup {
    private static final int FADE_DURATION = 200;

    protected final android.support.v7.widget.AbsActionBarView.VisibilityAnimListener mVisAnimListener = new android.support.v7.widget.AbsActionBarView.VisibilityAnimListener();

    /**
     * Context against which to inflate popup menus.
     */
    protected final android.content.Context mPopupContext;

    protected android.support.v7.widget.ActionMenuView mMenuView;

    protected android.support.v7.widget.ActionMenuPresenter mActionMenuPresenter;

    protected int mContentHeight;

    protected android.support.v4.view.ViewPropertyAnimatorCompat mVisibilityAnim;

    private boolean mEatingTouch;

    private boolean mEatingHover;

    AbsActionBarView(android.content.Context context) {
        this(context, null);
    }

    AbsActionBarView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    AbsActionBarView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final android.util.TypedValue tv = new android.util.TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarPopupTheme, tv, true) && (tv.resourceId != 0)) {
            mPopupContext = new android.view.ContextThemeWrapper(context, tv.resourceId);
        } else {
            mPopupContext = context;
        }
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Action bar can change size on configuration changes.
        // Reread the desired height from the theme-specified style.
        android.content.res.TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        setContentHeight(a.getLayoutDimension(R.styleable.ActionBar_height, 0));
        a.recycle();
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.onConfigurationChanged(newConfig);
        }
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        // ActionBarViews always eat touch events, but should still respect the touch event dispatch
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
        if (action == android.support.v4.view.MotionEventCompat.ACTION_HOVER_ENTER) {
            mEatingHover = false;
        }
        if (!mEatingHover) {
            final boolean handled = super.onHoverEvent(ev);
            if ((action == android.support.v4.view.MotionEventCompat.ACTION_HOVER_ENTER) && (!handled)) {
                mEatingHover = true;
            }
        }
        if ((action == android.support.v4.view.MotionEventCompat.ACTION_HOVER_EXIT) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
            mEatingHover = false;
        }
        return true;
    }

    public void setContentHeight(int height) {
        mContentHeight = height;
        requestLayout();
    }

    public int getContentHeight() {
        return mContentHeight;
    }

    /**
     *
     *
     * @return Current visibility or if animating, the visibility being animated to.
     */
    public int getAnimatedVisibility() {
        if (mVisibilityAnim != null) {
            return mVisAnimListener.mFinalVisibility;
        }
        return getVisibility();
    }

    public android.support.v4.view.ViewPropertyAnimatorCompat setupAnimatorToVisibility(int visibility, long duration) {
        if (mVisibilityAnim != null) {
            mVisibilityAnim.cancel();
        }
        if (visibility == android.view.View.VISIBLE) {
            if (getVisibility() != android.view.View.VISIBLE) {
                android.support.v4.view.ViewCompat.setAlpha(this, 0.0F);
            }
            android.support.v4.view.ViewPropertyAnimatorCompat anim = android.support.v4.view.ViewCompat.animate(this).alpha(1.0F);
            anim.setDuration(duration);
            anim.setListener(mVisAnimListener.withFinalVisibility(anim, visibility));
            return anim;
        } else {
            android.support.v4.view.ViewPropertyAnimatorCompat anim = android.support.v4.view.ViewCompat.animate(this).alpha(0.0F);
            anim.setDuration(duration);
            anim.setListener(mVisAnimListener.withFinalVisibility(anim, visibility));
            return anim;
        }
    }

    public void animateToVisibility(int visibility) {
        android.support.v4.view.ViewPropertyAnimatorCompat anim = setupAnimatorToVisibility(visibility, android.support.v7.widget.AbsActionBarView.FADE_DURATION);
        anim.start();
    }

    @java.lang.Override
    public void setVisibility(int visibility) {
        if (visibility != getVisibility()) {
            if (mVisibilityAnim != null) {
                mVisibilityAnim.cancel();
            }
            super.setVisibility(visibility);
        }
    }

    public boolean showOverflowMenu() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

    public void postShowOverflowMenu() {
        post(new java.lang.Runnable() {
            public void run() {
                showOverflowMenu();
            }
        });
    }

    public boolean hideOverflowMenu() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    public boolean isOverflowMenuShowPending() {
        if (mActionMenuPresenter != null) {
            return mActionMenuPresenter.isOverflowMenuShowPending();
        }
        return false;
    }

    public boolean isOverflowReserved() {
        return (mActionMenuPresenter != null) && mActionMenuPresenter.isOverflowReserved();
    }

    public boolean canShowOverflowMenu() {
        return isOverflowReserved() && (getVisibility() == android.view.View.VISIBLE);
    }

    public void dismissPopupMenus() {
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.dismissPopupMenus();
        }
    }

    protected int measureChildView(android.view.View child, int availableWidth, int childSpecHeight, int spacing) {
        child.measure(android.view.View.MeasureSpec.makeMeasureSpec(availableWidth, android.view.View.MeasureSpec.AT_MOST), childSpecHeight);
        availableWidth -= child.getMeasuredWidth();
        availableWidth -= spacing;
        return java.lang.Math.max(0, availableWidth);
    }

    protected static int next(int x, int val, boolean isRtl) {
        return isRtl ? x - val : x + val;
    }

    protected int positionChild(android.view.View child, int x, int y, int contentHeight, boolean reverse) {
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        int childTop = y + ((contentHeight - childHeight) / 2);
        if (reverse) {
            child.layout(x - childWidth, childTop, x, childTop + childHeight);
        } else {
            child.layout(x, childTop, x + childWidth, childTop + childHeight);
        }
        return reverse ? -childWidth : childWidth;
    }

    protected class VisibilityAnimListener implements android.support.v4.view.ViewPropertyAnimatorListener {
        private boolean mCanceled = false;

        int mFinalVisibility;

        public android.support.v7.widget.AbsActionBarView.VisibilityAnimListener withFinalVisibility(android.support.v4.view.ViewPropertyAnimatorCompat animation, int visibility) {
            mVisibilityAnim = animation;
            mFinalVisibility = visibility;
            return this;
        }

        @java.lang.Override
        public void onAnimationStart(android.view.View view) {
            android.support.v7.widget.AbsActionBarView.super.setVisibility(android.view.View.VISIBLE);
            mCanceled = false;
        }

        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            if (mCanceled)
                return;

            mVisibilityAnim = null;
            android.support.v7.widget.AbsActionBarView.super.setVisibility(mFinalVisibility);
        }

        @java.lang.Override
        public void onAnimationCancel(android.view.View view) {
            mCanceled = true;
        }
    }
}

