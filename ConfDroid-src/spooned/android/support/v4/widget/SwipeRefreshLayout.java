/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * The SwipeRefreshLayout should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The SwipeRefreshLayout
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and
 * progress animation, call setEnabled(false) on the view.
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The SwipeRefreshLayout does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.
 * </p>
 */
public class SwipeRefreshLayout extends android.view.ViewGroup implements android.support.v4.view.NestedScrollingChild , android.support.v4.view.NestedScrollingParent {
    // Maps to ProgressBar.Large style
    public static final int LARGE = android.support.v4.widget.MaterialProgressDrawable.LARGE;

    // Maps to ProgressBar default style
    public static final int DEFAULT = android.support.v4.widget.MaterialProgressDrawable.DEFAULT;

    @android.support.annotation.VisibleForTesting
    static final int CIRCLE_DIAMETER = 40;

    @android.support.annotation.VisibleForTesting
    static final int CIRCLE_DIAMETER_LARGE = 56;

    private static final java.lang.String LOG_TAG = android.support.v4.widget.SwipeRefreshLayout.class.getSimpleName();

    private static final int MAX_ALPHA = 255;

    private static final int STARTING_PROGRESS_ALPHA = ((int) (0.3F * android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA));

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;

    private static final int INVALID_POINTER = -1;

    private static final float DRAG_RATE = 0.5F;

    // Max amount of circle that can be filled by progress during swipe gesture,
    // where 1.0 is a full circle
    private static final float MAX_PROGRESS_ANGLE = 0.8F;

    private static final int SCALE_DOWN_DURATION = 150;

    private static final int ALPHA_ANIMATION_DURATION = 300;

    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;

    private static final int ANIMATE_TO_START_DURATION = 200;

    // Default background for the progress spinner
    private static final int CIRCLE_BG_LIGHT = 0xfffafafa;

    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_CIRCLE_TARGET = 64;

    private android.view.View mTarget;// the target of the gesture


    android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener mListener;

    boolean mRefreshing = false;

    private int mTouchSlop;

    private float mTotalDragDistance = -1;

    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    private float mTotalUnconsumed;

    private final android.support.v4.view.NestedScrollingParentHelper mNestedScrollingParentHelper;

    private final android.support.v4.view.NestedScrollingChildHelper mNestedScrollingChildHelper;

    private final int[] mParentScrollConsumed = new int[2];

    private final int[] mParentOffsetInWindow = new int[2];

    private boolean mNestedScrollInProgress;

    private int mMediumAnimationDuration;

    int mCurrentTargetOffsetTop;

    private float mInitialMotionY;

    private float mInitialDownY;

    private boolean mIsBeingDragged;

    private int mActivePointerId = android.support.v4.widget.SwipeRefreshLayout.INVALID_POINTER;

    // Whether this item is scaled up rather than clipped
    boolean mScale;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;

    private final android.view.animation.DecelerateInterpolator mDecelerateInterpolator;

    private static final int[] LAYOUT_ATTRS = new int[]{ android.R.attr.enabled };

    android.support.v4.widget.CircleImageView mCircleView;

    private int mCircleViewIndex = -1;

    protected int mFrom;

    float mStartingScale;

    protected int mOriginalOffsetTop;

    int mSpinnerOffsetEnd;

    android.support.v4.widget.MaterialProgressDrawable mProgress;

    private android.view.animation.Animation mScaleAnimation;

    private android.view.animation.Animation mScaleDownAnimation;

    private android.view.animation.Animation mAlphaStartAnimation;

    private android.view.animation.Animation mAlphaMaxAnimation;

    private android.view.animation.Animation mScaleDownToStartAnimation;

    boolean mNotify;

    private int mCircleDiameter;

    // Whether the client has set a custom starting position;
    boolean mUsingCustomStart;

    private android.support.v4.widget.SwipeRefreshLayout.OnChildScrollUpCallback mChildScrollUpCallback;

    private android.view.animation.Animation.AnimationListener mRefreshListener = new android.view.animation.Animation.AnimationListener() {
        @java.lang.Override
        public void onAnimationStart(android.view.animation.Animation animation) {
        }

        @java.lang.Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {
        }

        @java.lang.Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            if (mRefreshing) {
                // Make sure the progress view is fully visible
                mProgress.setAlpha(android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA);
                mProgress.start();
                if (mNotify) {
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
                mCurrentTargetOffsetTop = mCircleView.getTop();
            } else {
                reset();
            }
        }
    };

    void reset() {
        mCircleView.clearAnimation();
        mProgress.stop();
        mCircleView.setVisibility(android.view.View.GONE);
        setColorViewAlpha(android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA);
        // Return the circle to its start position
        if (mScale) {
            /* animation complete and view is hidden */
            setAnimationProgress(0);
        } else {
            /* requires update */
            setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop, true);
        }
        mCurrentTargetOffsetTop = mCircleView.getTop();
    }

    @java.lang.Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    private void setColorViewAlpha(int targetAlpha) {
        mCircleView.getBackground().setAlpha(targetAlpha);
        mProgress.setAlpha(targetAlpha);
    }

    /**
     * The refresh indicator starting and resting position is always positioned
     * near the top of the refreshing content. This position is a consistent
     * location, but can be adjusted in either direction based on whether or not
     * there is a toolbar or actionbar present.
     * <p>
     * <strong>Note:</strong> Calling this will reset the position of the refresh indicator to
     * <code>start</code>.
     * </p>
     *
     * @param scale
     * 		Set to true if there is no view at a higher z-order than where the progress
     * 		spinner is set to appear. Setting it to true will cause indicator to be scaled
     * 		up rather than clipped.
     * @param start
     * 		The offset in pixels from the top of this view at which the
     * 		progress spinner should appear.
     * @param end
     * 		The offset in pixels from the top of this view at which the
     * 		progress spinner should come to rest after a successful swipe
     * 		gesture.
     */
    public void setProgressViewOffset(boolean scale, int start, int end) {
        mScale = scale;
        mOriginalOffsetTop = start;
        mSpinnerOffsetEnd = end;
        mUsingCustomStart = true;
        reset();
        mRefreshing = false;
    }

    /**
     *
     *
     * @return The offset in pixels from the top of this view at which the progress spinner should
    appear.
     */
    public int getProgressViewStartOffset() {
        return mOriginalOffsetTop;
    }

    /**
     *
     *
     * @return The offset in pixels from the top of this view at which the progress spinner should
    come to rest after a successful swipe gesture.
     */
    public int getProgressViewEndOffset() {
        return mSpinnerOffsetEnd;
    }

    /**
     * The refresh indicator resting position is always positioned near the top
     * of the refreshing content. This position is a consistent location, but
     * can be adjusted in either direction based on whether or not there is a
     * toolbar or actionbar present.
     *
     * @param scale
     * 		Set to true if there is no view at a higher z-order than where the progress
     * 		spinner is set to appear. Setting it to true will cause indicator to be scaled
     * 		up rather than clipped.
     * @param end
     * 		The offset in pixels from the top of this view at which the
     * 		progress spinner should come to rest after a successful swipe
     * 		gesture.
     */
    public void setProgressViewEndTarget(boolean scale, int end) {
        mSpinnerOffsetEnd = end;
        mScale = scale;
        mCircleView.invalidate();
    }

    /**
     * One of DEFAULT, or LARGE.
     */
    public void setSize(int size) {
        if ((size != android.support.v4.widget.MaterialProgressDrawable.LARGE) && (size != android.support.v4.widget.MaterialProgressDrawable.DEFAULT)) {
            return;
        }
        final android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (size == android.support.v4.widget.MaterialProgressDrawable.LARGE) {
            mCircleDiameter = ((int) (android.support.v4.widget.SwipeRefreshLayout.CIRCLE_DIAMETER_LARGE * metrics.density));
        } else {
            mCircleDiameter = ((int) (android.support.v4.widget.SwipeRefreshLayout.CIRCLE_DIAMETER * metrics.density));
        }
        // force the bounds of the progress circle inside the circle view to
        // update by setting it to null before updating its size and then
        // re-setting it
        mCircleView.setImageDrawable(null);
        mProgress.updateSizes(size);
        mCircleView.setImageDrawable(mProgress);
    }

    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     *
     * @param context
     * 		
     */
    public SwipeRefreshLayout(android.content.Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     *
     * @param context
     * 		
     * @param attrs
     * 		
     */
    public SwipeRefreshLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        setWillNotDraw(false);
        mDecelerateInterpolator = new android.view.animation.DecelerateInterpolator(android.support.v4.widget.SwipeRefreshLayout.DECELERATE_INTERPOLATION_FACTOR);
        final android.util.DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleDiameter = ((int) (android.support.v4.widget.SwipeRefreshLayout.CIRCLE_DIAMETER * metrics.density));
        createProgressView();
        android.support.v4.view.ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        // the absolute offset has to take into account that the circle starts at an offset
        mSpinnerOffsetEnd = ((int) (android.support.v4.widget.SwipeRefreshLayout.DEFAULT_CIRCLE_TARGET * metrics.density));
        mTotalDragDistance = mSpinnerOffsetEnd;
        mNestedScrollingParentHelper = new android.support.v4.view.NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new android.support.v4.view.NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        mOriginalOffsetTop = mCurrentTargetOffsetTop = -mCircleDiameter;
        moveToStart(1.0F);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v4.widget.SwipeRefreshLayout.LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @java.lang.Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mCircleViewIndex < 0) {
            return i;
        } else
            if (i == (childCount - 1)) {
                // Draw the selected child last
                return mCircleViewIndex;
            } else
                if (i >= mCircleViewIndex) {
                    // Move the children after the selected child earlier one
                    return i + 1;
                } else {
                    // Keep the children before the selected child the same
                    return i;
                }


    }

    private void createProgressView() {
        mCircleView = new android.support.v4.widget.CircleImageView(getContext(), android.support.v4.widget.SwipeRefreshLayout.CIRCLE_BG_LIGHT);
        mProgress = new android.support.v4.widget.MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(android.support.v4.widget.SwipeRefreshLayout.CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(android.view.View.GONE);
        addView(mCircleView);
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * Pre API 11, alpha is used to make the progress circle appear instead of scale.
     */
    private boolean isAlphaUsedForScale() {
        return android.os.Build.VERSION.SDK_INT < 11;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing
     * 		Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && (mRefreshing != refreshing)) {
            // scale and show
            mRefreshing = refreshing;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd + mOriginalOffsetTop;
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            /* requires update */
            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop, true);
            mNotify = false;
            startScaleUpAnimation(mRefreshListener);
        } else {
            /* notify */
            setRefreshing(refreshing, false);
        }
    }

    private void startScaleUpAnimation(android.view.animation.Animation.AnimationListener listener) {
        mCircleView.setVisibility(android.view.View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // Pre API 11, alpha is used in place of scale up to show the
            // progress circle appearing.
            // Don't adjust the alpha during appearance otherwise.
            mProgress.setAlpha(android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA);
        }
        mScaleAnimation = new android.view.animation.Animation() {
            @java.lang.Override
            public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                setAnimationProgress(interpolatedTime);
            }
        };
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleAnimation);
    }

    /**
     * Pre API 11, this does an alpha animation.
     *
     * @param progress
     * 		
     */
    void setAnimationProgress(float progress) {
        if (isAlphaUsedForScale()) {
            setColorViewAlpha(((int) (progress * android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA)));
        } else {
            android.support.v4.view.ViewCompat.setScaleX(mCircleView, progress);
            android.support.v4.view.ViewCompat.setScaleY(mCircleView, progress);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                startScaleDownAnimation(mRefreshListener);
            }
        }
    }

    void startScaleDownAnimation(android.view.animation.Animation.AnimationListener listener) {
        mScaleDownAnimation = new android.view.animation.Animation() {
            @java.lang.Override
            public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                setAnimationProgress(1 - interpolatedTime);
            }
        };
        mScaleDownAnimation.setDuration(android.support.v4.widget.SwipeRefreshLayout.SCALE_DOWN_DURATION);
        mCircleView.setAnimationListener(listener);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownAnimation);
    }

    private void startProgressAlphaStartAnimation() {
        mAlphaStartAnimation = startAlphaAnimation(mProgress.getAlpha(), android.support.v4.widget.SwipeRefreshLayout.STARTING_PROGRESS_ALPHA);
    }

    private void startProgressAlphaMaxAnimation() {
        mAlphaMaxAnimation = startAlphaAnimation(mProgress.getAlpha(), android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA);
    }

    private android.view.animation.Animation startAlphaAnimation(final int startingAlpha, final int endingAlpha) {
        // Pre API 11, alpha is used in place of scale. Don't also use it to
        // show the trigger point.
        if (mScale && isAlphaUsedForScale()) {
            return null;
        }
        android.view.animation.Animation alpha = new android.view.animation.Animation() {
            @java.lang.Override
            public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                mProgress.setAlpha(((int) (startingAlpha + ((endingAlpha - startingAlpha) * interpolatedTime))));
            }
        };
        alpha.setDuration(android.support.v4.widget.SwipeRefreshLayout.ALPHA_ANIMATION_DURATION);
        // Clear out the previous animation listeners.
        mCircleView.setAnimationListener(null);
        mCircleView.clearAnimation();
        mCircleView.startAnimation(alpha);
        return alpha;
    }

    /**
     *
     *
     * @deprecated Use {@link #setProgressBackgroundColorSchemeResource(int)}
     */
    @java.lang.Deprecated
    public void setProgressBackgroundColor(int colorRes) {
        setProgressBackgroundColorSchemeResource(colorRes);
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param colorRes
     * 		Resource id of the color.
     */
    public void setProgressBackgroundColorSchemeResource(@android.support.annotation.ColorRes
    int colorRes) {
        setProgressBackgroundColorSchemeColor(android.support.v4.content.ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param color
     * 		
     */
    public void setProgressBackgroundColorSchemeColor(@android.support.annotation.ColorInt
    int color) {
        mCircleView.setBackgroundColor(color);
        mProgress.setBackgroundColor(color);
    }

    /**
     *
     *
     * @deprecated Use {@link #setColorSchemeResources(int...)}
     */
    @java.lang.Deprecated
    public void setColorScheme(@android.support.annotation.ColorInt
    int... colors) {
        setColorSchemeResources(colors);
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds
     * 		
     */
    public void setColorSchemeResources(@android.support.annotation.ColorRes
    int... colorResIds) {
        final android.content.Context context = getContext();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = android.support.v4.content.ContextCompat.getColor(context, colorResIds[i]);
        }
        setColorSchemeColors(colorRes);
    }

    /**
     * Set the colors used in the progress animation. The first
     * color will also be the color of the bar that grows in response to a user
     * swipe gesture.
     *
     * @param colors
     * 		
     */
    public void setColorSchemeColors(@android.support.annotation.ColorInt
    int... colors) {
        ensureTarget();
        mProgress.setColorSchemeColors(colors);
    }

    /**
     *
     *
     * @return Whether the SwipeRefreshWidget is actively showing refresh
    progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                android.view.View child = getChildAt(i);
                if (!child.equals(mCircleView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    /**
     * Set the distance to trigger a sync in dips
     *
     * @param distance
     * 		
     */
    public void setDistanceToTriggerSync(int distance) {
        mTotalDragDistance = distance;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        final android.view.View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = (width - getPaddingLeft()) - getPaddingRight();
        final int childHeight = (height - getPaddingTop()) - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        mCircleView.layout((width / 2) - (circleWidth / 2), mCurrentTargetOffsetTop, (width / 2) + (circleWidth / 2), mCurrentTargetOffsetTop + circleHeight);
    }

    @java.lang.Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(android.view.View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), android.view.View.MeasureSpec.EXACTLY));
        mCircleView.measure(android.view.View.MeasureSpec.makeMeasureSpec(mCircleDiameter, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(mCircleDiameter, android.view.View.MeasureSpec.EXACTLY));
        mCircleViewIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mCircleView) {
                mCircleViewIndex = index;
                break;
            }
        }
    }

    /**
     * Get the diameter of the progress circle that is displayed as part of the
     * swipe to refresh layout.
     *
     * @return Diameter in pixels of the progress circle view.
     */
    public int getProgressCircleDiameter() {
        return mCircleDiameter;
    }

    /**
     *
     *
     * @return Whether it is possible for the child view of this layout to
    scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof android.widget.AbsListView) {
                final android.widget.AbsListView absListView = ((android.widget.AbsListView) (mTarget));
                return (absListView.getChildCount() > 0) && ((absListView.getFirstVisiblePosition() > 0) || (absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
            } else {
                return android.support.v4.view.ViewCompat.canScrollVertically(mTarget, -1) || (mTarget.getScrollY() > 0);
            }
        } else {
            return android.support.v4.view.ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * Set a callback to override {@link SwipeRefreshLayout#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     *
     * @param callback
     * 		Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@android.support.annotation.Nullable
    android.support.v4.widget.SwipeRefreshLayout.OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        ensureTarget();
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        int pointerIndex;
        if (mReturningToStart && (action == android.view.MotionEvent.ACTION_DOWN)) {
            mReturningToStart = false;
        }
        if (((((!isEnabled()) || mReturningToStart) || canChildScrollUp()) || mRefreshing) || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCircleView.getTop(), true);
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;
            case android.view.MotionEvent.ACTION_MOVE :
                if (mActivePointerId == android.support.v4.widget.SwipeRefreshLayout.INVALID_POINTER) {
                    android.util.Log.e(android.support.v4.widget.SwipeRefreshLayout.LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_UP :
                onSecondaryPointerUp(ev);
                break;
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                mIsBeingDragged = false;
                mActivePointerId = android.support.v4.widget.SwipeRefreshLayout.INVALID_POINTER;
                break;
        }
        return mIsBeingDragged;
    }

    @java.lang.Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if (((android.os.Build.VERSION.SDK_INT < 21) && (mTarget instanceof android.widget.AbsListView)) || ((mTarget != null) && (!android.support.v4.view.ViewCompat.isNestedScrollingEnabled(mTarget)))) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    // NestedScrollingParent
    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int nestedScrollAxes) {
        return ((isEnabled() && (!mReturningToStart)) && (!mRefreshing)) && ((nestedScrollAxes & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL) != 0);
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @java.lang.Override
    public void onNestedPreScroll(android.view.View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if ((dy > 0) && (mTotalUnconsumed > 0)) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - ((int) (mTotalUnconsumed));
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }
        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
        if (((mUsingCustomStart && (dy > 0)) && (mTotalUnconsumed == 0)) && (java.lang.Math.abs(dy - consumed[1]) > 0)) {
            mCircleView.setVisibility(android.view.View.GONE);
        }
        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @java.lang.Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @java.lang.Override
    public void onNestedScroll(final android.view.View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if ((dy < 0) && (!canChildScrollUp())) {
            mTotalUnconsumed += java.lang.Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }
    }

    // NestedScrollingChild
    @java.lang.Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @java.lang.Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @java.lang.Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @java.lang.Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @java.lang.Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @java.lang.Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @java.lang.Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.view.View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @java.lang.Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @java.lang.Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private boolean isAnimationRunning(android.view.animation.Animation animation) {
        return ((animation != null) && animation.hasStarted()) && (!animation.hasEnded());
    }

    private void moveSpinner(float overscrollTop) {
        mProgress.showArrow(true);
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        float dragPercent = java.lang.Math.min(1.0F, java.lang.Math.abs(originalDragPercent));
        float adjustedPercent = (((float) (java.lang.Math.max(dragPercent - 0.4, 0))) * 5) / 3;
        float extraOS = java.lang.Math.abs(overscrollTop) - mTotalDragDistance;
        float slingshotDist = (mUsingCustomStart) ? mSpinnerOffsetEnd - mOriginalOffsetTop : mSpinnerOffsetEnd;
        float tensionSlingshotPercent = java.lang.Math.max(0, java.lang.Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = ((float) ((tensionSlingshotPercent / 4) - java.lang.Math.pow(tensionSlingshotPercent / 4, 2))) * 2.0F;
        float extraMove = (slingshotDist * tensionPercent) * 2;
        int targetY = mOriginalOffsetTop + ((int) ((slingshotDist * dragPercent) + extraMove));
        // where 1.0f is a full circle
        if (mCircleView.getVisibility() != android.view.View.VISIBLE) {
            mCircleView.setVisibility(android.view.View.VISIBLE);
        }
        if (!mScale) {
            android.support.v4.view.ViewCompat.setScaleX(mCircleView, 1.0F);
            android.support.v4.view.ViewCompat.setScaleY(mCircleView, 1.0F);
        }
        if (mScale) {
            setAnimationProgress(java.lang.Math.min(1.0F, overscrollTop / mTotalDragDistance));
        }
        if (overscrollTop < mTotalDragDistance) {
            if ((mProgress.getAlpha() > android.support.v4.widget.SwipeRefreshLayout.STARTING_PROGRESS_ALPHA) && (!isAnimationRunning(mAlphaStartAnimation))) {
                // Animate the alpha
                startProgressAlphaStartAnimation();
            }
        } else {
            if ((mProgress.getAlpha() < android.support.v4.widget.SwipeRefreshLayout.MAX_ALPHA) && (!isAnimationRunning(mAlphaMaxAnimation))) {
                // Animate the alpha
                startProgressAlphaMaxAnimation();
            }
        }
        float strokeStart = adjustedPercent * 0.8F;
        mProgress.setStartEndTrim(0.0F, java.lang.Math.min(android.support.v4.widget.SwipeRefreshLayout.MAX_PROGRESS_ANGLE, strokeStart));
        mProgress.setArrowScale(java.lang.Math.min(1.0F, adjustedPercent));
        float rotation = (((-0.25F) + (0.4F * adjustedPercent)) + (tensionPercent * 2)) * 0.5F;
        mProgress.setProgressRotation(rotation);
        /* requires update */
        setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true);
    }

    private void finishSpinner(float overscrollTop) {
        if (overscrollTop > mTotalDragDistance) {
            /* notify */
            setRefreshing(true, true);
        } else {
            // cancel refresh
            mRefreshing = false;
            mProgress.setStartEndTrim(0.0F, 0.0F);
            android.view.animation.Animation.AnimationListener listener = null;
            if (!mScale) {
                listener = new android.view.animation.Animation.AnimationListener() {
                    @java.lang.Override
                    public void onAnimationStart(android.view.animation.Animation animation) {
                    }

                    @java.lang.Override
                    public void onAnimationEnd(android.view.animation.Animation animation) {
                        if (!mScale) {
                            startScaleDownAnimation(null);
                        }
                    }

                    @java.lang.Override
                    public void onAnimationRepeat(android.view.animation.Animation animation) {
                    }
                };
            }
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
            mProgress.showArrow(false);
        }
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        int pointerIndex = -1;
        if (mReturningToStart && (action == android.view.MotionEvent.ACTION_DOWN)) {
            mReturningToStart = false;
        }
        if (((((!isEnabled()) || mReturningToStart) || canChildScrollUp()) || mRefreshing) || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        android.util.Log.e(android.support.v4.widget.SwipeRefreshLayout.LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    final float y = ev.getY(pointerIndex);
                    startDragging(y);
                    if (mIsBeingDragged) {
                        final float overscrollTop = (y - mInitialMotionY) * android.support.v4.widget.SwipeRefreshLayout.DRAG_RATE;
                        if (overscrollTop > 0) {
                            moveSpinner(overscrollTop);
                        } else {
                            return false;
                        }
                    }
                    break;
                }
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_DOWN :
                {
                    pointerIndex = android.support.v4.view.MotionEventCompat.getActionIndex(ev);
                    if (pointerIndex < 0) {
                        android.util.Log.e(android.support.v4.widget.SwipeRefreshLayout.LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    mActivePointerId = ev.getPointerId(pointerIndex);
                    break;
                }
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_UP :
                onSecondaryPointerUp(ev);
                break;
            case android.view.MotionEvent.ACTION_UP :
                {
                    pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex < 0) {
                        android.util.Log.e(android.support.v4.widget.SwipeRefreshLayout.LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                        return false;
                    }
                    if (mIsBeingDragged) {
                        final float y = ev.getY(pointerIndex);
                        final float overscrollTop = (y - mInitialMotionY) * android.support.v4.widget.SwipeRefreshLayout.DRAG_RATE;
                        mIsBeingDragged = false;
                        finishSpinner(overscrollTop);
                    }
                    mActivePointerId = android.support.v4.widget.SwipeRefreshLayout.INVALID_POINTER;
                    return false;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
                return false;
        }
        return true;
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if ((yDiff > mTouchSlop) && (!mIsBeingDragged)) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
            mProgress.setAlpha(android.support.v4.widget.SwipeRefreshLayout.STARTING_PROGRESS_ALPHA);
        }
    }

    private void animateOffsetToCorrectPosition(int from, android.view.animation.Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(android.support.v4.widget.SwipeRefreshLayout.ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int from, android.view.animation.Animation.AnimationListener listener) {
        if (mScale) {
            // Scale the item back down
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            mFrom = from;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(android.support.v4.widget.SwipeRefreshLayout.ANIMATE_TO_START_DURATION);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            if (listener != null) {
                mCircleView.setAnimationListener(listener);
            }
            mCircleView.clearAnimation();
            mCircleView.startAnimation(mAnimateToStartPosition);
        }
    }

    private final android.view.animation.Animation mAnimateToCorrectPosition = new android.view.animation.Animation() {
        @java.lang.Override
        public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            int targetTop = 0;
            int endTarget = 0;
            if (!mUsingCustomStart) {
                endTarget = mSpinnerOffsetEnd - java.lang.Math.abs(mOriginalOffsetTop);
            } else {
                endTarget = mSpinnerOffsetEnd;
            }
            targetTop = mFrom + ((int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mCircleView.getTop();
            /* requires update */
            setTargetOffsetTopAndBottom(offset, false);
            mProgress.setArrowScale(1 - interpolatedTime);
        }
    };

    void moveToStart(float interpolatedTime) {
        int targetTop = 0;
        targetTop = mFrom + ((int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mCircleView.getTop();
        /* requires update */
        setTargetOffsetTopAndBottom(offset, false);
    }

    private final android.view.animation.Animation mAnimateToStartPosition = new android.view.animation.Animation() {
        @java.lang.Override
        public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private void startScaleDownReturnToStartAnimation(int from, android.view.animation.Animation.AnimationListener listener) {
        mFrom = from;
        if (isAlphaUsedForScale()) {
            mStartingScale = mProgress.getAlpha();
        } else {
            mStartingScale = android.support.v4.view.ViewCompat.getScaleX(mCircleView);
        }
        mScaleDownToStartAnimation = new android.view.animation.Animation() {
            @java.lang.Override
            public void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                float targetScale = mStartingScale + ((-mStartingScale) * interpolatedTime);
                setAnimationProgress(targetScale);
                moveToStart(interpolatedTime);
            }
        };
        mScaleDownToStartAnimation.setDuration(android.support.v4.widget.SwipeRefreshLayout.SCALE_DOWN_DURATION);
        if (listener != null) {
            mCircleView.setAnimationListener(listener);
        }
        mCircleView.clearAnimation();
        mCircleView.startAnimation(mScaleDownToStartAnimation);
    }

    void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        mCircleView.bringToFront();
        android.support.v4.view.ViewCompat.offsetTopAndBottom(mCircleView, offset);
        mCurrentTargetOffsetTop = mCircleView.getTop();
        if (requiresUpdate && (android.os.Build.VERSION.SDK_INT < 11)) {
            invalidate();
        }
    }

    private void onSecondaryPointerUp(android.view.MotionEvent ev) {
        final int pointerIndex = android.support.v4.view.MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    /**
     * Classes that wish to override {@link SwipeRefreshLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link SwipeRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent
         * 		SwipeRefreshLayout that this callback is overriding.
         * @param child
         * 		The child view of SwipeRefreshLayout.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(android.support.v4.widget.SwipeRefreshLayout parent, @android.support.annotation.Nullable
        android.view.View child);
    }
}

