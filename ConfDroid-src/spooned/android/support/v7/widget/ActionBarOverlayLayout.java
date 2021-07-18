/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Special layout for the containing of an overlay action bar (and its content) to correctly handle
 * fitting system windows when the content has request that its layout ignore them.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ActionBarOverlayLayout extends android.view.ViewGroup implements android.support.v4.view.NestedScrollingParent , android.support.v7.widget.DecorContentParent {
    private static final java.lang.String TAG = "ActionBarOverlayLayout";

    private int mActionBarHeight;

    // private WindowDecorActionBar mActionBar;
    private int mWindowVisibility = android.view.View.VISIBLE;

    // The main UI elements that we handle the layout of.
    private android.support.v7.widget.ContentFrameLayout mContent;

    android.support.v7.widget.ActionBarContainer mActionBarTop;

    // Some interior UI elements.
    private android.support.v7.widget.DecorToolbar mDecorToolbar;

    // Content overlay drawable - generally the action bar's shadow
    private android.graphics.drawable.Drawable mWindowContentOverlay;

    private boolean mIgnoreWindowContentOverlay;

    private boolean mOverlayMode;

    private boolean mHasNonEmbeddedTabs;

    private boolean mHideOnContentScroll;

    boolean mAnimatingForFling;

    private int mHideOnContentScrollReference;

    private int mLastSystemUiVisibility;

    private final android.graphics.Rect mBaseContentInsets = new android.graphics.Rect();

    private final android.graphics.Rect mLastBaseContentInsets = new android.graphics.Rect();

    private final android.graphics.Rect mContentInsets = new android.graphics.Rect();

    private final android.graphics.Rect mBaseInnerInsets = new android.graphics.Rect();

    private final android.graphics.Rect mInnerInsets = new android.graphics.Rect();

    private final android.graphics.Rect mLastInnerInsets = new android.graphics.Rect();

    private android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback mActionBarVisibilityCallback;

    private final int ACTION_BAR_ANIMATE_DELAY = 600;// ms


    private android.support.v4.widget.ScrollerCompat mFlingEstimator;

    android.support.v4.view.ViewPropertyAnimatorCompat mCurrentActionBarTopAnimator;

    final android.support.v4.view.ViewPropertyAnimatorListener mTopAnimatorListener = new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
            mCurrentActionBarTopAnimator = null;
            mAnimatingForFling = false;
        }

        @java.lang.Override
        public void onAnimationCancel(android.view.View view) {
            mCurrentActionBarTopAnimator = null;
            mAnimatingForFling = false;
        }
    };

    private final java.lang.Runnable mRemoveActionBarHideOffset = new java.lang.Runnable() {
        public void run() {
            haltActionBarHideOffsetAnimations();
            mCurrentActionBarTopAnimator = android.support.v4.view.ViewCompat.animate(mActionBarTop).translationY(0).setListener(mTopAnimatorListener);
        }
    };

    private final java.lang.Runnable mAddActionBarHideOffset = new java.lang.Runnable() {
        public void run() {
            haltActionBarHideOffsetAnimations();
            mCurrentActionBarTopAnimator = android.support.v4.view.ViewCompat.animate(mActionBarTop).translationY(-mActionBarTop.getHeight()).setListener(mTopAnimatorListener);
        }
    };

    static final int[] ATTRS = new int[]{ R.attr.actionBarSize, android.R.attr.windowContentOverlay };

    private final android.support.v4.view.NestedScrollingParentHelper mParentHelper;

    public ActionBarOverlayLayout(android.content.Context context) {
        this(context, null);
    }

    public ActionBarOverlayLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mParentHelper = new android.support.v4.view.NestedScrollingParentHelper(this);
    }

    private void init(android.content.Context context) {
        android.content.res.TypedArray ta = getContext().getTheme().obtainStyledAttributes(android.support.v7.widget.ActionBarOverlayLayout.ATTRS);
        mActionBarHeight = ta.getDimensionPixelSize(0, 0);
        mWindowContentOverlay = ta.getDrawable(1);
        setWillNotDraw(mWindowContentOverlay == null);
        ta.recycle();
        mIgnoreWindowContentOverlay = context.getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.KITKAT;
        mFlingEstimator = android.support.v4.widget.ScrollerCompat.create(context);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        haltActionBarHideOffsetAnimations();
    }

    public void setActionBarVisibilityCallback(android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback cb) {
        mActionBarVisibilityCallback = cb;
        if (getWindowToken() != null) {
            // This is being initialized after being added to a window;
            // make sure to update all state now.
            mActionBarVisibilityCallback.onWindowVisibilityChanged(mWindowVisibility);
            if (mLastSystemUiVisibility != 0) {
                int newVis = mLastSystemUiVisibility;
                onWindowSystemUiVisibilityChanged(newVis);
                android.support.v4.view.ViewCompat.requestApplyInsets(this);
            }
        }
    }

    public void setOverlayMode(boolean overlayMode) {
        mOverlayMode = overlayMode;
        /* Drawing the window content overlay was broken before K so starting to draw it
        again unexpectedly will cause artifacts in some apps. They should fix it.
         */
        mIgnoreWindowContentOverlay = overlayMode && (getContext().getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.KITKAT);
    }

    public boolean isInOverlayMode() {
        return mOverlayMode;
    }

    public void setHasNonEmbeddedTabs(boolean hasNonEmbeddedTabs) {
        mHasNonEmbeddedTabs = hasNonEmbeddedTabs;
    }

    public void setShowingForActionMode(boolean showing) {
        // TODO: Add workaround for this
        // if (showing) {
        // // Here's a fun hack: if the status bar is currently being hidden,
        // // and the application has asked for stable content insets, then
        // // we will end up with the action mode action bar being shown
        // // without the status bar, but moved below where the status bar
        // // would be.  Not nice.  Trying to have this be positioned
        // // correctly is not easy (basically we need yet *another* content
        // // inset from the window manager to know where to put it), so
        // // instead we will just temporarily force the status bar to be shown.
        // if ((getWindowSystemUiVisibility() & (SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // | SYSTEM_UI_FLAG_LAYOUT_STABLE))
        // == (SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_STABLE)) {
        // setDisabledSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        // }
        // } else {
        // setDisabledSystemUiVisibility(0);
        // }
    }

    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        init(getContext());
        android.support.v4.view.ViewCompat.requestApplyInsets(this);
    }

    public void onWindowSystemUiVisibilityChanged(int visible) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            super.onWindowSystemUiVisibilityChanged(visible);
        }
        pullChildren();
        final int diff = mLastSystemUiVisibility ^ visible;
        mLastSystemUiVisibility = visible;
        final boolean barVisible = (visible & android.view.View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
        final boolean stable = (visible & android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE) != 0;
        if (mActionBarVisibilityCallback != null) {
            // We want the bar to be visible if it is not being hidden,
            // or the app has not turned on a stable UI mode (meaning they
            // are performing explicit layout around the action bar).
            mActionBarVisibilityCallback.enableContentAnimations(!stable);
            if (barVisible || (!stable))
                mActionBarVisibilityCallback.showForSystem();
            else
                mActionBarVisibilityCallback.hideForSystem();

        }
        if ((diff & android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE) != 0) {
            if (mActionBarVisibilityCallback != null) {
                android.support.v4.view.ViewCompat.requestApplyInsets(this);
            }
        }
    }

    @java.lang.Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mWindowVisibility = visibility;
        if (mActionBarVisibilityCallback != null) {
            mActionBarVisibilityCallback.onWindowVisibilityChanged(visibility);
        }
    }

    private boolean applyInsets(android.view.View view, android.graphics.Rect insets, boolean left, boolean top, boolean bottom, boolean right) {
        boolean changed = false;
        android.support.v7.widget.ActionBarOverlayLayout.LayoutParams lp = ((android.support.v7.widget.ActionBarOverlayLayout.LayoutParams) (view.getLayoutParams()));
        if (left && (lp.leftMargin != insets.left)) {
            changed = true;
            lp.leftMargin = insets.left;
        }
        if (top && (lp.topMargin != insets.top)) {
            changed = true;
            lp.topMargin = insets.top;
        }
        if (right && (lp.rightMargin != insets.right)) {
            changed = true;
            lp.rightMargin = insets.right;
        }
        if (bottom && (lp.bottomMargin != insets.bottom)) {
            changed = true;
            lp.bottomMargin = insets.bottom;
        }
        return changed;
    }

    @java.lang.Override
    protected boolean fitSystemWindows(android.graphics.Rect insets) {
        pullChildren();
        final int vis = android.support.v4.view.ViewCompat.getWindowSystemUiVisibility(this);
        final boolean stable = (vis & android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE) != 0;
        final android.graphics.Rect systemInsets = insets;
        // The top action bar is always within the content area.
        boolean changed = applyInsets(mActionBarTop, systemInsets, true, true, false, true);
        mBaseInnerInsets.set(systemInsets);
        android.support.v7.widget.ViewUtils.computeFitSystemWindows(this, mBaseInnerInsets, mBaseContentInsets);
        if (!mLastBaseContentInsets.equals(mBaseContentInsets)) {
            changed = true;
            mLastBaseContentInsets.set(mBaseContentInsets);
        }
        if (changed) {
            requestLayout();
        }
        // We don't do any more at this point.  To correctly compute the content/inner
        // insets in all cases, we need to know the measured size of the various action
        // bar elements. fitSystemWindows() happens before the measure pass, so we can't
        // do that here. Instead we will take this up in onMeasure().
        return true;
    }

    @java.lang.Override
    protected android.support.v7.widget.ActionBarOverlayLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v7.widget.ActionBarOverlayLayout.LayoutParams(android.support.v7.widget.ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, android.support.v7.widget.ActionBarOverlayLayout.LayoutParams.MATCH_PARENT);
    }

    @java.lang.Override
    public android.support.v7.widget.ActionBarOverlayLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v7.widget.ActionBarOverlayLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new android.support.v7.widget.ActionBarOverlayLayout.LayoutParams(p);
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.support.v7.widget.ActionBarOverlayLayout.LayoutParams;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        pullChildren();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int topInset = 0;
        int bottomInset = 0;
        measureChildWithMargins(mActionBarTop, widthMeasureSpec, 0, heightMeasureSpec, 0);
        android.support.v7.widget.ActionBarOverlayLayout.LayoutParams lp = ((android.support.v7.widget.ActionBarOverlayLayout.LayoutParams) (mActionBarTop.getLayoutParams()));
        maxWidth = java.lang.Math.max(maxWidth, (mActionBarTop.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
        maxHeight = java.lang.Math.max(maxHeight, (mActionBarTop.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
        childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mActionBarTop));
        final int vis = android.support.v4.view.ViewCompat.getWindowSystemUiVisibility(this);
        final boolean stable = (vis & android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE) != 0;
        if (stable) {
            // This is the standard space needed for the action bar.  For stable measurement,
            // we can't depend on the size currently reported by it -- this must remain constant.
            topInset = mActionBarHeight;
            if (mHasNonEmbeddedTabs) {
                final android.view.View tabs = mActionBarTop.getTabContainer();
                if (tabs != null) {
                    // If tabs are not embedded, increase space on top to account for them.
                    topInset += mActionBarHeight;
                }
            }
        } else
            if (mActionBarTop.getVisibility() != android.view.View.GONE) {
                // This is the space needed on top of the window for all of the action bar
                // and tabs.
                topInset = mActionBarTop.getMeasuredHeight();
            }

        // If the window has not requested system UI layout flags, we need to
        // make sure its content is not being covered by system UI...  though it
        // will still be covered by the action bar if they have requested it to
        // overlay.
        mContentInsets.set(mBaseContentInsets);
        mInnerInsets.set(mBaseInnerInsets);
        if ((!mOverlayMode) && (!stable)) {
            mContentInsets.top += topInset;
            mContentInsets.bottom += bottomInset;
        } else {
            mInnerInsets.top += topInset;
            mInnerInsets.bottom += bottomInset;
        }
        applyInsets(mContent, mContentInsets, true, true, true, true);
        if (!mLastInnerInsets.equals(mInnerInsets)) {
            // If the inner insets have changed, we need to dispatch this down to
            // the app's fitSystemWindows().  We do this before measuring the content
            // view to keep the same semantics as the normal fitSystemWindows() call.
            mLastInnerInsets.set(mInnerInsets);
            mContent.dispatchFitSystemWindows(mInnerInsets);
        }
        measureChildWithMargins(mContent, widthMeasureSpec, 0, heightMeasureSpec, 0);
        lp = ((android.support.v7.widget.ActionBarOverlayLayout.LayoutParams) (mContent.getLayoutParams()));
        maxWidth = java.lang.Math.max(maxWidth, (mContent.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
        maxHeight = java.lang.Math.max(maxHeight, (mContent.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
        childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(mContent));
        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        // Check against our minimum height and width
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(android.support.v4.view.ViewCompat.resolveSizeAndState(maxWidth, widthMeasureSpec, childState), android.support.v4.view.ViewCompat.resolveSizeAndState(maxHeight, heightMeasureSpec, childState << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int parentLeft = getPaddingLeft();
        final int parentRight = (right - left) - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = (bottom - top) - getPaddingBottom();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                final android.support.v7.widget.ActionBarOverlayLayout.LayoutParams lp = ((android.support.v7.widget.ActionBarOverlayLayout.LayoutParams) (child.getLayoutParams()));
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int childLeft = parentLeft + lp.leftMargin;
                int childTop = parentTop + lp.topMargin;
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas c) {
        super.draw(c);
        if ((mWindowContentOverlay != null) && (!mIgnoreWindowContentOverlay)) {
            final int top = (mActionBarTop.getVisibility() == android.view.View.VISIBLE) ? ((int) ((mActionBarTop.getBottom() + android.support.v4.view.ViewCompat.getTranslationY(mActionBarTop)) + 0.5F)) : 0;
            mWindowContentOverlay.setBounds(0, top, getWidth(), top + mWindowContentOverlay.getIntrinsicHeight());
            mWindowContentOverlay.draw(c);
        }
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int axes) {
        if (((axes & android.view.View.SCROLL_AXIS_VERTICAL) == 0) || (mActionBarTop.getVisibility() != android.view.View.VISIBLE)) {
            return false;
        }
        return mHideOnContentScroll;
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
        mHideOnContentScrollReference = getActionBarHideOffset();
        haltActionBarHideOffsetAnimations();
        if (mActionBarVisibilityCallback != null) {
            mActionBarVisibilityCallback.onContentScrollStarted();
        }
    }

    @java.lang.Override
    public void onNestedScroll(android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        mHideOnContentScrollReference += dyConsumed;
        setActionBarHideOffset(mHideOnContentScrollReference);
    }

    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
        if (mHideOnContentScroll && (!mAnimatingForFling)) {
            if (mHideOnContentScrollReference <= mActionBarTop.getHeight()) {
                postRemoveActionBarHideOffset();
            } else {
                postAddActionBarHideOffset();
            }
        }
        if (mActionBarVisibilityCallback != null) {
            mActionBarVisibilityCallback.onContentScrollStopped();
        }
    }

    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        if ((!mHideOnContentScroll) || (!consumed)) {
            return false;
        }
        if (shouldHideActionBarOnFling(velocityX, velocityY)) {
            addActionBarHideOffset();
        } else {
            removeActionBarHideOffset();
        }
        mAnimatingForFling = true;
        return true;
    }

    @java.lang.Override
    public void onNestedPreScroll(android.view.View target, int dx, int dy, int[] consumed) {
        // no-op
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.view.View target, float velocityX, float velocityY) {
        return false;
    }

    @java.lang.Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    void pullChildren() {
        if (mContent == null) {
            mContent = ((android.support.v7.widget.ContentFrameLayout) (findViewById(R.id.action_bar_activity_content)));
            mActionBarTop = ((android.support.v7.widget.ActionBarContainer) (findViewById(R.id.action_bar_container)));
            mDecorToolbar = getDecorToolbar(findViewById(R.id.action_bar));
        }
    }

    private android.support.v7.widget.DecorToolbar getDecorToolbar(android.view.View view) {
        if (view instanceof android.support.v7.widget.DecorToolbar) {
            return ((android.support.v7.widget.DecorToolbar) (view));
        } else
            if (view instanceof android.support.v7.widget.Toolbar) {
                return ((android.support.v7.widget.Toolbar) (view)).getWrapper();
            } else {
                throw new java.lang.IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
            }

    }

    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll != mHideOnContentScroll) {
            mHideOnContentScroll = hideOnContentScroll;
            if (!hideOnContentScroll) {
                haltActionBarHideOffsetAnimations();
                setActionBarHideOffset(0);
            }
        }
    }

    public boolean isHideOnContentScrollEnabled() {
        return mHideOnContentScroll;
    }

    public int getActionBarHideOffset() {
        return mActionBarTop != null ? -((int) (android.support.v4.view.ViewCompat.getTranslationY(mActionBarTop))) : 0;
    }

    public void setActionBarHideOffset(int offset) {
        haltActionBarHideOffsetAnimations();
        final int topHeight = mActionBarTop.getHeight();
        offset = java.lang.Math.max(0, java.lang.Math.min(offset, topHeight));
        android.support.v4.view.ViewCompat.setTranslationY(mActionBarTop, -offset);
    }

    void haltActionBarHideOffsetAnimations() {
        removeCallbacks(mRemoveActionBarHideOffset);
        removeCallbacks(mAddActionBarHideOffset);
        if (mCurrentActionBarTopAnimator != null) {
            mCurrentActionBarTopAnimator.cancel();
        }
    }

    private void postRemoveActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(mRemoveActionBarHideOffset, ACTION_BAR_ANIMATE_DELAY);
    }

    private void postAddActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(mAddActionBarHideOffset, ACTION_BAR_ANIMATE_DELAY);
    }

    private void removeActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        mRemoveActionBarHideOffset.run();
    }

    private void addActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        mAddActionBarHideOffset.run();
    }

    private boolean shouldHideActionBarOnFling(float velocityX, float velocityY) {
        mFlingEstimator.fling(0, 0, 0, ((int) (velocityY)), 0, 0, java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE);
        final int finalY = mFlingEstimator.getFinalY();
        return finalY > mActionBarTop.getHeight();
    }

    @java.lang.Override
    public void setWindowCallback(android.view.Window.Callback cb) {
        pullChildren();
        mDecorToolbar.setWindowCallback(cb);
    }

    @java.lang.Override
    public void setWindowTitle(java.lang.CharSequence title) {
        pullChildren();
        mDecorToolbar.setWindowTitle(title);
    }

    @java.lang.Override
    public java.lang.CharSequence getTitle() {
        pullChildren();
        return mDecorToolbar.getTitle();
    }

    @java.lang.Override
    public void initFeature(int windowFeature) {
        pullChildren();
        switch (windowFeature) {
            case android.view.Window.FEATURE_PROGRESS :
                mDecorToolbar.initProgress();
                break;
            case android.view.Window.FEATURE_INDETERMINATE_PROGRESS :
                mDecorToolbar.initIndeterminateProgress();
                break;
            case android.support.v7.app.AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY :
                setOverlayMode(true);
                break;
        }
    }

    @java.lang.Override
    public void setUiOptions(int uiOptions) {
        // Split Action Bar not included.
    }

    @java.lang.Override
    public boolean hasIcon() {
        pullChildren();
        return mDecorToolbar.hasIcon();
    }

    @java.lang.Override
    public boolean hasLogo() {
        pullChildren();
        return mDecorToolbar.hasLogo();
    }

    @java.lang.Override
    public void setIcon(int resId) {
        pullChildren();
        mDecorToolbar.setIcon(resId);
    }

    @java.lang.Override
    public void setIcon(android.graphics.drawable.Drawable d) {
        pullChildren();
        mDecorToolbar.setIcon(d);
    }

    @java.lang.Override
    public void setLogo(int resId) {
        pullChildren();
        mDecorToolbar.setLogo(resId);
    }

    @java.lang.Override
    public boolean canShowOverflowMenu() {
        pullChildren();
        return mDecorToolbar.canShowOverflowMenu();
    }

    @java.lang.Override
    public boolean isOverflowMenuShowing() {
        pullChildren();
        return mDecorToolbar.isOverflowMenuShowing();
    }

    @java.lang.Override
    public boolean isOverflowMenuShowPending() {
        pullChildren();
        return mDecorToolbar.isOverflowMenuShowPending();
    }

    @java.lang.Override
    public boolean showOverflowMenu() {
        pullChildren();
        return mDecorToolbar.showOverflowMenu();
    }

    @java.lang.Override
    public boolean hideOverflowMenu() {
        pullChildren();
        return mDecorToolbar.hideOverflowMenu();
    }

    @java.lang.Override
    public void setMenuPrepared() {
        pullChildren();
        mDecorToolbar.setMenuPrepared();
    }

    @java.lang.Override
    public void setMenu(android.view.Menu menu, android.support.v7.view.menu.MenuPresenter.Callback cb) {
        pullChildren();
        mDecorToolbar.setMenu(menu, cb);
    }

    @java.lang.Override
    public void saveToolbarHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates) {
        pullChildren();
        mDecorToolbar.saveHierarchyState(toolbarStates);
    }

    @java.lang.Override
    public void restoreToolbarHierarchyState(android.util.SparseArray<android.os.Parcelable> toolbarStates) {
        pullChildren();
        mDecorToolbar.restoreHierarchyState(toolbarStates);
    }

    @java.lang.Override
    public void dismissPopups() {
        pullChildren();
        mDecorToolbar.dismissPopupMenus();
    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }

    public interface ActionBarVisibilityCallback {
        void onWindowVisibilityChanged(int visibility);

        void showForSystem();

        void hideForSystem();

        void enableContentAnimations(boolean enable);

        void onContentScrollStarted();

        void onContentScrollStopped();
    }
}

