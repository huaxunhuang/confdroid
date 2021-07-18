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
package android.widget;


/**
 * A view that displays its children in a stack and allows users to discretely swipe
 * through the children.
 */
@android.widget.RemoteViews.RemoteView
public class StackView extends android.widget.AdapterViewAnimator {
    private final java.lang.String TAG = "StackView";

    /**
     * Default animation parameters
     */
    private static final int DEFAULT_ANIMATION_DURATION = 400;

    private static final int MINIMUM_ANIMATION_DURATION = 50;

    private static final int STACK_RELAYOUT_DURATION = 100;

    /**
     * Parameters effecting the perspective visuals
     */
    private static final float PERSPECTIVE_SHIFT_FACTOR_Y = 0.1F;

    private static final float PERSPECTIVE_SHIFT_FACTOR_X = 0.1F;

    private float mPerspectiveShiftX;

    private float mPerspectiveShiftY;

    private float mNewPerspectiveShiftX;

    private float mNewPerspectiveShiftY;

    @java.lang.SuppressWarnings({ "FieldCanBeLocal" })
    private static final float PERSPECTIVE_SCALE_FACTOR = 0.0F;

    /**
     * Represent the two possible stack modes, one where items slide up, and the other
     * where items slide down. The perspective is also inverted between these two modes.
     */
    private static final int ITEMS_SLIDE_UP = 0;

    private static final int ITEMS_SLIDE_DOWN = 1;

    /**
     * These specify the different gesture states
     */
    private static final int GESTURE_NONE = 0;

    private static final int GESTURE_SLIDE_UP = 1;

    private static final int GESTURE_SLIDE_DOWN = 2;

    /**
     * Specifies how far you need to swipe (up or down) before it
     * will be consider a completed gesture when you lift your finger
     */
    private static final float SWIPE_THRESHOLD_RATIO = 0.2F;

    /**
     * Specifies the total distance, relative to the size of the stack,
     * that views will be slid, either up or down
     */
    private static final float SLIDE_UP_RATIO = 0.7F;

    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    /**
     * Number of active views in the stack. One fewer view is actually visible, as one is hidden.
     */
    private static final int NUM_ACTIVE_VIEWS = 5;

    private static final int FRAME_PADDING = 4;

    private final android.graphics.Rect mTouchRect = new android.graphics.Rect();

    private static final int MIN_TIME_BETWEEN_INTERACTION_AND_AUTOADVANCE = 5000;

    private static final long MIN_TIME_BETWEEN_SCROLLS = 100;

    /**
     * These variables are all related to the current state of touch interaction
     * with the stack
     */
    private float mInitialY;

    private float mInitialX;

    private int mActivePointerId;

    private int mYVelocity = 0;

    private int mSwipeGestureType = android.widget.StackView.GESTURE_NONE;

    private int mSlideAmount;

    private int mSwipeThreshold;

    private int mTouchSlop;

    private int mMaximumVelocity;

    private android.view.VelocityTracker mVelocityTracker;

    private boolean mTransitionIsSetup = false;

    private int mResOutColor;

    private int mClickColor;

    private static android.widget.StackView.HolographicHelper sHolographicHelper;

    private android.widget.ImageView mHighlight;

    private android.widget.ImageView mClickFeedback;

    private boolean mClickFeedbackIsValid = false;

    private android.widget.StackView.StackSlider mStackSlider;

    private boolean mFirstLayoutHappened = false;

    private long mLastInteractionTime = 0;

    private long mLastScrollTime;

    private int mStackMode;

    private int mFramePadding;

    private final android.graphics.Rect stackInvalidateRect = new android.graphics.Rect();

    /**
     * {@inheritDoc }
     */
    public StackView(android.content.Context context) {
        this(context, null);
    }

    /**
     * {@inheritDoc }
     */
    public StackView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.stackViewStyle);
    }

    /**
     * {@inheritDoc }
     */
    public StackView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * {@inheritDoc }
     */
    public StackView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.StackView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, com.android.internal.R.styleable.StackView, attrs, a, defStyleAttr, defStyleRes);
        mResOutColor = a.getColor(com.android.internal.R.styleable.StackView_resOutColor, 0);
        mClickColor = a.getColor(com.android.internal.R.styleable.StackView_clickColor, 0);
        a.recycle();
        initStackView();
    }

    private void initStackView() {
        configureViewAnimator(android.widget.StackView.NUM_ACTIVE_VIEWS, 1);
        setStaticTransformationsEnabled(true);
        final android.view.ViewConfiguration configuration = android.view.ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mActivePointerId = android.widget.StackView.INVALID_POINTER;
        mHighlight = new android.widget.ImageView(getContext());
        mHighlight.setLayoutParams(new android.widget.StackView.LayoutParams(mHighlight));
        addViewInLayout(mHighlight, -1, new android.widget.StackView.LayoutParams(mHighlight));
        mClickFeedback = new android.widget.ImageView(getContext());
        mClickFeedback.setLayoutParams(new android.widget.StackView.LayoutParams(mClickFeedback));
        addViewInLayout(mClickFeedback, -1, new android.widget.StackView.LayoutParams(mClickFeedback));
        mClickFeedback.setVisibility(android.view.View.INVISIBLE);
        mStackSlider = new android.widget.StackView.StackSlider();
        if (android.widget.StackView.sHolographicHelper == null) {
            android.widget.StackView.sHolographicHelper = new android.widget.StackView.HolographicHelper(mContext);
        }
        setClipChildren(false);
        setClipToPadding(false);
        // This sets the form of the StackView, which is currently to have the perspective-shifted
        // views above the active view, and have items slide down when sliding out. The opposite is
        // available by using ITEMS_SLIDE_UP.
        mStackMode = android.widget.StackView.ITEMS_SLIDE_DOWN;
        // This is a flag to indicate the the stack is loading for the first time
        mWhichChild = -1;
        // Adjust the frame padding based on the density, since the highlight changes based
        // on the density
        final float density = mContext.getResources().getDisplayMetrics().density;
        mFramePadding = ((int) (java.lang.Math.ceil(density * android.widget.StackView.FRAME_PADDING)));
    }

    /**
     * Animate the views between different relative indexes within the {@link AdapterViewAnimator}
     */
    void transformViewForTransition(int fromIndex, int toIndex, final android.view.View view, boolean animate) {
        if (!animate) {
            ((android.widget.StackView.StackFrame) (view)).cancelSliderAnimator();
            view.setRotationX(0.0F);
            android.widget.StackView.LayoutParams lp = ((android.widget.StackView.LayoutParams) (view.getLayoutParams()));
            lp.setVerticalOffset(0);
            lp.setHorizontalOffset(0);
        }
        if ((fromIndex == (-1)) && (toIndex == (getNumActiveViews() - 1))) {
            transformViewAtIndex(toIndex, view, false);
            view.setVisibility(android.view.View.VISIBLE);
            view.setAlpha(1.0F);
        } else
            if ((fromIndex == 0) && (toIndex == 1)) {
                // Slide item in
                ((android.widget.StackView.StackFrame) (view)).cancelSliderAnimator();
                view.setVisibility(android.view.View.VISIBLE);
                int duration = java.lang.Math.round(mStackSlider.getDurationForNeutralPosition(mYVelocity));
                android.widget.StackView.StackSlider animationSlider = new android.widget.StackView.StackSlider(mStackSlider);
                animationSlider.setView(view);
                if (animate) {
                    android.animation.PropertyValuesHolder slideInY = android.animation.PropertyValuesHolder.ofFloat("YProgress", 0.0F);
                    android.animation.PropertyValuesHolder slideInX = android.animation.PropertyValuesHolder.ofFloat("XProgress", 0.0F);
                    android.animation.ObjectAnimator slideIn = android.animation.ObjectAnimator.ofPropertyValuesHolder(animationSlider, slideInX, slideInY);
                    slideIn.setDuration(duration);
                    slideIn.setInterpolator(new android.view.animation.LinearInterpolator());
                    ((android.widget.StackView.StackFrame) (view)).setSliderAnimator(slideIn);
                    slideIn.start();
                } else {
                    animationSlider.setYProgress(0.0F);
                    animationSlider.setXProgress(0.0F);
                }
            } else
                if ((fromIndex == 1) && (toIndex == 0)) {
                    // Slide item out
                    ((android.widget.StackView.StackFrame) (view)).cancelSliderAnimator();
                    int duration = java.lang.Math.round(mStackSlider.getDurationForOffscreenPosition(mYVelocity));
                    android.widget.StackView.StackSlider animationSlider = new android.widget.StackView.StackSlider(mStackSlider);
                    animationSlider.setView(view);
                    if (animate) {
                        android.animation.PropertyValuesHolder slideOutY = android.animation.PropertyValuesHolder.ofFloat("YProgress", 1.0F);
                        android.animation.PropertyValuesHolder slideOutX = android.animation.PropertyValuesHolder.ofFloat("XProgress", 0.0F);
                        android.animation.ObjectAnimator slideOut = android.animation.ObjectAnimator.ofPropertyValuesHolder(animationSlider, slideOutX, slideOutY);
                        slideOut.setDuration(duration);
                        slideOut.setInterpolator(new android.view.animation.LinearInterpolator());
                        ((android.widget.StackView.StackFrame) (view)).setSliderAnimator(slideOut);
                        slideOut.start();
                    } else {
                        animationSlider.setYProgress(1.0F);
                        animationSlider.setXProgress(0.0F);
                    }
                } else
                    if (toIndex == 0) {
                        // Make sure this view that is "waiting in the wings" is invisible
                        view.setAlpha(0.0F);
                        view.setVisibility(android.view.View.INVISIBLE);
                    } else
                        if (((fromIndex == 0) || (fromIndex == 1)) && (toIndex > 1)) {
                            view.setVisibility(android.view.View.VISIBLE);
                            view.setAlpha(1.0F);
                            view.setRotationX(0.0F);
                            android.widget.StackView.LayoutParams lp = ((android.widget.StackView.LayoutParams) (view.getLayoutParams()));
                            lp.setVerticalOffset(0);
                            lp.setHorizontalOffset(0);
                        } else
                            if (fromIndex == (-1)) {
                                view.setAlpha(1.0F);
                                view.setVisibility(android.view.View.VISIBLE);
                            } else
                                if (toIndex == (-1)) {
                                    if (animate) {
                                        postDelayed(new java.lang.Runnable() {
                                            public void run() {
                                                view.setAlpha(0);
                                            }
                                        }, android.widget.StackView.STACK_RELAYOUT_DURATION);
                                    } else {
                                        view.setAlpha(0.0F);
                                    }
                                }






        // Implement the faked perspective
        if (toIndex != (-1)) {
            transformViewAtIndex(toIndex, view, animate);
        }
    }

    private void transformViewAtIndex(int index, final android.view.View view, boolean animate) {
        final float maxPerspectiveShiftY = mPerspectiveShiftY;
        final float maxPerspectiveShiftX = mPerspectiveShiftX;
        if (mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) {
            index = (mMaxNumActiveViews - index) - 1;
            if (index == (mMaxNumActiveViews - 1))
                index--;

        } else {
            index--;
            if (index < 0)
                index++;

        }
        float r = (index * 1.0F) / (mMaxNumActiveViews - 2);
        final float scale = 1 - (android.widget.StackView.PERSPECTIVE_SCALE_FACTOR * (1 - r));
        float perspectiveTranslationY = r * maxPerspectiveShiftY;
        float scaleShiftCorrectionY = (scale - 1) * ((getMeasuredHeight() * (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_Y)) / 2.0F);
        final float transY = perspectiveTranslationY + scaleShiftCorrectionY;
        float perspectiveTranslationX = (1 - r) * maxPerspectiveShiftX;
        float scaleShiftCorrectionX = (1 - scale) * ((getMeasuredWidth() * (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_X)) / 2.0F);
        final float transX = perspectiveTranslationX + scaleShiftCorrectionX;
        // If this view is currently being animated for a certain position, we need to cancel
        // this animation so as not to interfere with the new transformation.
        if (view instanceof android.widget.StackView.StackFrame) {
            ((android.widget.StackView.StackFrame) (view)).cancelTransformAnimator();
        }
        if (animate) {
            android.animation.PropertyValuesHolder translationX = android.animation.PropertyValuesHolder.ofFloat("translationX", transX);
            android.animation.PropertyValuesHolder translationY = android.animation.PropertyValuesHolder.ofFloat("translationY", transY);
            android.animation.PropertyValuesHolder scalePropX = android.animation.PropertyValuesHolder.ofFloat("scaleX", scale);
            android.animation.PropertyValuesHolder scalePropY = android.animation.PropertyValuesHolder.ofFloat("scaleY", scale);
            android.animation.ObjectAnimator oa = android.animation.ObjectAnimator.ofPropertyValuesHolder(view, scalePropX, scalePropY, translationY, translationX);
            oa.setDuration(android.widget.StackView.STACK_RELAYOUT_DURATION);
            if (view instanceof android.widget.StackView.StackFrame) {
                ((android.widget.StackView.StackFrame) (view)).setTransformAnimator(oa);
            }
            oa.start();
        } else {
            view.setTranslationX(transX);
            view.setTranslationY(transY);
            view.setScaleX(scale);
            view.setScaleY(scale);
        }
    }

    private void setupStackSlider(android.view.View v, int mode) {
        mStackSlider.setMode(mode);
        if (v != null) {
            mHighlight.setImageBitmap(android.widget.StackView.sHolographicHelper.createResOutline(v, mResOutColor));
            mHighlight.setRotation(v.getRotation());
            mHighlight.setTranslationY(v.getTranslationY());
            mHighlight.setTranslationX(v.getTranslationX());
            mHighlight.bringToFront();
            v.bringToFront();
            mStackSlider.setView(v);
            v.setVisibility(android.view.View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    @android.view.RemotableViewMethod
    public void showNext() {
        if (mSwipeGestureType != android.widget.StackView.GESTURE_NONE)
            return;

        if (!mTransitionIsSetup) {
            android.view.View v = getViewAtRelativeIndex(1);
            if (v != null) {
                setupStackSlider(v, android.widget.StackView.StackSlider.NORMAL_MODE);
                mStackSlider.setYProgress(0);
                mStackSlider.setXProgress(0);
            }
        }
        super.showNext();
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    @android.view.RemotableViewMethod
    public void showPrevious() {
        if (mSwipeGestureType != android.widget.StackView.GESTURE_NONE)
            return;

        if (!mTransitionIsSetup) {
            android.view.View v = getViewAtRelativeIndex(0);
            if (v != null) {
                setupStackSlider(v, android.widget.StackView.StackSlider.NORMAL_MODE);
                mStackSlider.setYProgress(1);
                mStackSlider.setXProgress(0);
            }
        }
        super.showPrevious();
    }

    @java.lang.Override
    void showOnly(int childIndex, boolean animate) {
        super.showOnly(childIndex, animate);
        // Here we need to make sure that the z-order of the children is correct
        for (int i = mCurrentWindowEnd; i >= mCurrentWindowStart; i--) {
            int index = modulo(i, getWindowSize());
            android.widget.AdapterViewAnimator.ViewAndMetaData vm = mViewsMap.get(index);
            if (vm != null) {
                android.view.View v = mViewsMap.get(index).view;
                if (v != null)
                    v.bringToFront();

            }
        }
        if (mHighlight != null) {
            mHighlight.bringToFront();
        }
        mTransitionIsSetup = false;
        mClickFeedbackIsValid = false;
    }

    void updateClickFeedback() {
        if (!mClickFeedbackIsValid) {
            android.view.View v = getViewAtRelativeIndex(1);
            if (v != null) {
                mClickFeedback.setImageBitmap(android.widget.StackView.sHolographicHelper.createClickOutline(v, mClickColor));
                mClickFeedback.setTranslationX(v.getTranslationX());
                mClickFeedback.setTranslationY(v.getTranslationY());
            }
            mClickFeedbackIsValid = true;
        }
    }

    @java.lang.Override
    void showTapFeedback(android.view.View v) {
        updateClickFeedback();
        mClickFeedback.setVisibility(android.view.View.VISIBLE);
        mClickFeedback.bringToFront();
        invalidate();
    }

    @java.lang.Override
    void hideTapFeedback(android.view.View v) {
        mClickFeedback.setVisibility(android.view.View.INVISIBLE);
        invalidate();
    }

    private void updateChildTransforms() {
        for (int i = 0; i < getNumActiveViews(); i++) {
            android.view.View v = getViewAtRelativeIndex(i);
            if (v != null) {
                transformViewAtIndex(i, v, false);
            }
        }
    }

    private static class StackFrame extends android.widget.FrameLayout {
        java.lang.ref.WeakReference<android.animation.ObjectAnimator> transformAnimator;

        java.lang.ref.WeakReference<android.animation.ObjectAnimator> sliderAnimator;

        public StackFrame(android.content.Context context) {
            super(context);
        }

        void setTransformAnimator(android.animation.ObjectAnimator oa) {
            transformAnimator = new java.lang.ref.WeakReference<android.animation.ObjectAnimator>(oa);
        }

        void setSliderAnimator(android.animation.ObjectAnimator oa) {
            sliderAnimator = new java.lang.ref.WeakReference<android.animation.ObjectAnimator>(oa);
        }

        boolean cancelTransformAnimator() {
            if (transformAnimator != null) {
                android.animation.ObjectAnimator oa = transformAnimator.get();
                if (oa != null) {
                    oa.cancel();
                    return true;
                }
            }
            return false;
        }

        boolean cancelSliderAnimator() {
            if (sliderAnimator != null) {
                android.animation.ObjectAnimator oa = sliderAnimator.get();
                if (oa != null) {
                    oa.cancel();
                    return true;
                }
            }
            return false;
        }
    }

    @java.lang.Override
    android.widget.FrameLayout getFrameForChild() {
        android.widget.StackView.StackFrame fl = new android.widget.StackView.StackFrame(mContext);
        fl.setPadding(mFramePadding, mFramePadding, mFramePadding, mFramePadding);
        return fl;
    }

    /**
     * Apply any necessary tranforms for the child that is being added.
     */
    void applyTransformForChildAtIndex(android.view.View child, int relativeIndex) {
    }

    @java.lang.Override
    protected void dispatchDraw(android.graphics.Canvas canvas) {
        boolean expandClipRegion = false;
        canvas.getClipBounds(stackInvalidateRect);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            android.widget.StackView.LayoutParams lp = ((android.widget.StackView.LayoutParams) (child.getLayoutParams()));
            if ((((lp.horizontalOffset == 0) && (lp.verticalOffset == 0)) || (child.getAlpha() == 0.0F)) || (child.getVisibility() != android.view.View.VISIBLE)) {
                lp.resetInvalidateRect();
            }
            android.graphics.Rect childInvalidateRect = lp.getInvalidateRect();
            if (!childInvalidateRect.isEmpty()) {
                expandClipRegion = true;
                stackInvalidateRect.union(childInvalidateRect);
            }
        }
        // We only expand the clip bounds if necessary.
        if (expandClipRegion) {
            canvas.save();
            canvas.clipRectUnion(stackInvalidateRect);
            super.dispatchDraw(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    private void onLayout() {
        if (!mFirstLayoutHappened) {
            mFirstLayoutHappened = true;
            updateChildTransforms();
        }
        final int newSlideAmount = java.lang.Math.round(android.widget.StackView.SLIDE_UP_RATIO * getMeasuredHeight());
        if (mSlideAmount != newSlideAmount) {
            mSlideAmount = newSlideAmount;
            mSwipeThreshold = java.lang.Math.round(android.widget.StackView.SWIPE_THRESHOLD_RATIO * newSlideAmount);
        }
        if ((java.lang.Float.compare(mPerspectiveShiftY, mNewPerspectiveShiftY) != 0) || (java.lang.Float.compare(mPerspectiveShiftX, mNewPerspectiveShiftX) != 0)) {
            mPerspectiveShiftY = mNewPerspectiveShiftY;
            mPerspectiveShiftX = mNewPerspectiveShiftX;
            updateChildTransforms();
        }
    }

    @java.lang.Override
    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        if ((event.getSource() & android.view.InputDevice.SOURCE_CLASS_POINTER) != 0) {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_SCROLL :
                    {
                        final float vscroll = event.getAxisValue(android.view.MotionEvent.AXIS_VSCROLL);
                        if (vscroll < 0) {
                            pacedScroll(false);
                            return true;
                        } else
                            if (vscroll > 0) {
                                pacedScroll(true);
                                return true;
                            }

                    }
            }
        }
        return super.onGenericMotionEvent(event);
    }

    // This ensures that the frequency of stack flips caused by scrolls is capped
    private void pacedScroll(boolean up) {
        long timeSinceLastScroll = java.lang.System.currentTimeMillis() - mLastScrollTime;
        if (timeSinceLastScroll > android.widget.StackView.MIN_TIME_BETWEEN_SCROLLS) {
            if (up) {
                showPrevious();
            } else {
                showNext();
            }
            mLastScrollTime = java.lang.System.currentTimeMillis();
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        int action = ev.getAction();
        switch (action & android.view.MotionEvent.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    if (mActivePointerId == android.widget.StackView.INVALID_POINTER) {
                        mInitialX = ev.getX();
                        mInitialY = ev.getY();
                        mActivePointerId = ev.getPointerId(0);
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (pointerIndex == android.widget.StackView.INVALID_POINTER) {
                        // no data for our primary pointer, this shouldn't happen, log it
                        android.util.Log.d(TAG, "Error: No data for our primary pointer.");
                        return false;
                    }
                    float newY = ev.getY(pointerIndex);
                    float deltaY = newY - mInitialY;
                    beginGestureIfNeeded(deltaY);
                    break;
                }
            case android.view.MotionEvent.ACTION_POINTER_UP :
                {
                    onSecondaryPointerUp(ev);
                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                {
                    mActivePointerId = android.widget.StackView.INVALID_POINTER;
                    mSwipeGestureType = android.widget.StackView.GESTURE_NONE;
                }
        }
        return mSwipeGestureType != android.widget.StackView.GESTURE_NONE;
    }

    private void beginGestureIfNeeded(float deltaY) {
        if ((((int) (java.lang.Math.abs(deltaY))) > mTouchSlop) && (mSwipeGestureType == android.widget.StackView.GESTURE_NONE)) {
            final int swipeGestureType = (deltaY < 0) ? android.widget.StackView.GESTURE_SLIDE_UP : android.widget.StackView.GESTURE_SLIDE_DOWN;
            cancelLongPress();
            requestDisallowInterceptTouchEvent(true);
            if (mAdapter == null)
                return;

            final int adapterCount = getCount();
            int activeIndex;
            if (mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) {
                activeIndex = (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN) ? 0 : 1;
            } else {
                activeIndex = (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN) ? 1 : 0;
            }
            boolean endOfStack = (mLoopViews && (adapterCount == 1)) && (((mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) && (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_UP)) || ((mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) && (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN)));
            boolean beginningOfStack = (mLoopViews && (adapterCount == 1)) && (((mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) && (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_UP)) || ((mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) && (swipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN)));
            int stackMode;
            if ((mLoopViews && (!beginningOfStack)) && (!endOfStack)) {
                stackMode = android.widget.StackView.StackSlider.NORMAL_MODE;
            } else
                if (((mCurrentWindowStartUnbounded + activeIndex) == (-1)) || beginningOfStack) {
                    activeIndex++;
                    stackMode = android.widget.StackView.StackSlider.BEGINNING_OF_STACK_MODE;
                } else
                    if (((mCurrentWindowStartUnbounded + activeIndex) == (adapterCount - 1)) || endOfStack) {
                        stackMode = android.widget.StackView.StackSlider.END_OF_STACK_MODE;
                    } else {
                        stackMode = android.widget.StackView.StackSlider.NORMAL_MODE;
                    }


            mTransitionIsSetup = stackMode == android.widget.StackView.StackSlider.NORMAL_MODE;
            android.view.View v = getViewAtRelativeIndex(activeIndex);
            if (v == null)
                return;

            setupStackSlider(v, stackMode);
            // We only register this gesture if we've made it this far without a problem
            mSwipeGestureType = swipeGestureType;
            cancelHandleClick();
        }
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        super.onTouchEvent(ev);
        int action = ev.getAction();
        int pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex == android.widget.StackView.INVALID_POINTER) {
            // no data for our primary pointer, this shouldn't happen, log it
            android.util.Log.d(TAG, "Error: No data for our primary pointer.");
            return false;
        }
        float newY = ev.getY(pointerIndex);
        float newX = ev.getX(pointerIndex);
        float deltaY = newY - mInitialY;
        float deltaX = newX - mInitialX;
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (action & android.view.MotionEvent.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    beginGestureIfNeeded(deltaY);
                    float rx = deltaX / (mSlideAmount * 1.0F);
                    if (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN) {
                        float r = ((deltaY - (mTouchSlop * 1.0F)) / mSlideAmount) * 1.0F;
                        if (mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN)
                            r = 1 - r;

                        mStackSlider.setYProgress(1 - r);
                        mStackSlider.setXProgress(rx);
                        return true;
                    } else
                        if (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_UP) {
                            float r = ((-(deltaY + (mTouchSlop * 1.0F))) / mSlideAmount) * 1.0F;
                            if (mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN)
                                r = 1 - r;

                            mStackSlider.setYProgress(r);
                            mStackSlider.setXProgress(rx);
                            return true;
                        }

                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
                {
                    handlePointerUp(ev);
                    break;
                }
            case android.view.MotionEvent.ACTION_POINTER_UP :
                {
                    onSecondaryPointerUp(ev);
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
                {
                    mActivePointerId = android.widget.StackView.INVALID_POINTER;
                    mSwipeGestureType = android.widget.StackView.GESTURE_NONE;
                    break;
                }
        }
        return true;
    }

    private void onSecondaryPointerUp(android.view.MotionEvent ev) {
        final int activePointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(activePointerIndex);
        if (pointerId == mActivePointerId) {
            int activeViewIndex = (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN) ? 0 : 1;
            android.view.View v = getViewAtRelativeIndex(activeViewIndex);
            if (v == null)
                return;

            // Our primary pointer has gone up -- let's see if we can find
            // another pointer on the view. If so, then we should replace
            // our primary pointer with this new pointer and adjust things
            // so that the view doesn't jump
            for (int index = 0; index < ev.getPointerCount(); index++) {
                if (index != activePointerIndex) {
                    float x = ev.getX(index);
                    float y = ev.getY(index);
                    mTouchRect.set(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    if (mTouchRect.contains(java.lang.Math.round(x), java.lang.Math.round(y))) {
                        float oldX = ev.getX(activePointerIndex);
                        float oldY = ev.getY(activePointerIndex);
                        // adjust our frame of reference to avoid a jump
                        mInitialY += y - oldY;
                        mInitialX += x - oldX;
                        mActivePointerId = ev.getPointerId(index);
                        if (mVelocityTracker != null) {
                            mVelocityTracker.clear();
                        }
                        // ok, we're good, we found a new pointer which is touching the active view
                        return;
                    }
                }
            }
            // if we made it this far, it means we didn't find a satisfactory new pointer :(,
            // so end the gesture
            handlePointerUp(ev);
        }
    }

    private void handlePointerUp(android.view.MotionEvent ev) {
        int pointerIndex = ev.findPointerIndex(mActivePointerId);
        float newY = ev.getY(pointerIndex);
        int deltaY = ((int) (newY - mInitialY));
        mLastInteractionTime = java.lang.System.currentTimeMillis();
        if (mVelocityTracker != null) {
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
            mYVelocity = ((int) (mVelocityTracker.getYVelocity(mActivePointerId)));
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        if (((deltaY > mSwipeThreshold) && (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN)) && (mStackSlider.mMode == android.widget.StackView.StackSlider.NORMAL_MODE)) {
            // We reset the gesture variable, because otherwise we will ignore showPrevious() /
            // showNext();
            mSwipeGestureType = android.widget.StackView.GESTURE_NONE;
            // Swipe threshold exceeded, swipe down
            if (mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) {
                showPrevious();
            } else {
                showNext();
            }
            mHighlight.bringToFront();
        } else
            if (((deltaY < (-mSwipeThreshold)) && (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_UP)) && (mStackSlider.mMode == android.widget.StackView.StackSlider.NORMAL_MODE)) {
                // We reset the gesture variable, because otherwise we will ignore showPrevious() /
                // showNext();
                mSwipeGestureType = android.widget.StackView.GESTURE_NONE;
                // Swipe threshold exceeded, swipe up
                if (mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) {
                    showNext();
                } else {
                    showPrevious();
                }
                mHighlight.bringToFront();
            } else
                if (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_UP) {
                    // Didn't swipe up far enough, snap back down
                    int duration;
                    float finalYProgress = (mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) ? 1 : 0;
                    if ((mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) || (mStackSlider.mMode != android.widget.StackView.StackSlider.NORMAL_MODE)) {
                        duration = java.lang.Math.round(mStackSlider.getDurationForNeutralPosition());
                    } else {
                        duration = java.lang.Math.round(mStackSlider.getDurationForOffscreenPosition());
                    }
                    android.widget.StackView.StackSlider animationSlider = new android.widget.StackView.StackSlider(mStackSlider);
                    android.animation.PropertyValuesHolder snapBackY = android.animation.PropertyValuesHolder.ofFloat("YProgress", finalYProgress);
                    android.animation.PropertyValuesHolder snapBackX = android.animation.PropertyValuesHolder.ofFloat("XProgress", 0.0F);
                    android.animation.ObjectAnimator pa = android.animation.ObjectAnimator.ofPropertyValuesHolder(animationSlider, snapBackX, snapBackY);
                    pa.setDuration(duration);
                    pa.setInterpolator(new android.view.animation.LinearInterpolator());
                    pa.start();
                } else
                    if (mSwipeGestureType == android.widget.StackView.GESTURE_SLIDE_DOWN) {
                        // Didn't swipe down far enough, snap back up
                        float finalYProgress = (mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) ? 0 : 1;
                        int duration;
                        if ((mStackMode == android.widget.StackView.ITEMS_SLIDE_DOWN) || (mStackSlider.mMode != android.widget.StackView.StackSlider.NORMAL_MODE)) {
                            duration = java.lang.Math.round(mStackSlider.getDurationForNeutralPosition());
                        } else {
                            duration = java.lang.Math.round(mStackSlider.getDurationForOffscreenPosition());
                        }
                        android.widget.StackView.StackSlider animationSlider = new android.widget.StackView.StackSlider(mStackSlider);
                        android.animation.PropertyValuesHolder snapBackY = android.animation.PropertyValuesHolder.ofFloat("YProgress", finalYProgress);
                        android.animation.PropertyValuesHolder snapBackX = android.animation.PropertyValuesHolder.ofFloat("XProgress", 0.0F);
                        android.animation.ObjectAnimator pa = android.animation.ObjectAnimator.ofPropertyValuesHolder(animationSlider, snapBackX, snapBackY);
                        pa.setDuration(duration);
                        pa.start();
                    }



        mActivePointerId = android.widget.StackView.INVALID_POINTER;
        mSwipeGestureType = android.widget.StackView.GESTURE_NONE;
    }

    private class StackSlider {
        android.view.View mView;

        float mYProgress;

        float mXProgress;

        static final int NORMAL_MODE = 0;

        static final int BEGINNING_OF_STACK_MODE = 1;

        static final int END_OF_STACK_MODE = 2;

        int mMode = android.widget.StackView.StackSlider.NORMAL_MODE;

        public StackSlider() {
        }

        public StackSlider(android.widget.StackView.StackSlider copy) {
            mView = copy.mView;
            mYProgress = copy.mYProgress;
            mXProgress = copy.mXProgress;
            mMode = copy.mMode;
        }

        private float cubic(float r) {
            return ((float) (java.lang.Math.pow((2 * r) - 1, 3) + 1)) / 2.0F;
        }

        private float highlightAlphaInterpolator(float r) {
            float pivot = 0.4F;
            if (r < pivot) {
                return 0.85F * cubic(r / pivot);
            } else {
                return 0.85F * cubic(1 - ((r - pivot) / (1 - pivot)));
            }
        }

        private float viewAlphaInterpolator(float r) {
            float pivot = 0.3F;
            if (r > pivot) {
                return (r - pivot) / (1 - pivot);
            } else {
                return 0;
            }
        }

        private float rotationInterpolator(float r) {
            float pivot = 0.2F;
            if (r < pivot) {
                return 0;
            } else {
                return (r - pivot) / (1 - pivot);
            }
        }

        void setView(android.view.View v) {
            mView = v;
        }

        public void setYProgress(float r) {
            // enforce r between 0 and 1
            r = java.lang.Math.min(1.0F, r);
            r = java.lang.Math.max(0, r);
            mYProgress = r;
            if (mView == null)
                return;

            final android.widget.StackView.LayoutParams viewLp = ((android.widget.StackView.LayoutParams) (mView.getLayoutParams()));
            final android.widget.StackView.LayoutParams highlightLp = ((android.widget.StackView.LayoutParams) (mHighlight.getLayoutParams()));
            int stackDirection = (mStackMode == android.widget.StackView.ITEMS_SLIDE_UP) ? 1 : -1;
            // We need to prevent any clipping issues which may arise by setting a layer type.
            // This doesn't come for free however, so we only want to enable it when required.
            if ((java.lang.Float.compare(0.0F, mYProgress) != 0) && (java.lang.Float.compare(1.0F, mYProgress) != 0)) {
                if (mView.getLayerType() == android.view.View.LAYER_TYPE_NONE) {
                    mView.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
                }
            } else {
                if (mView.getLayerType() != android.view.View.LAYER_TYPE_NONE) {
                    mView.setLayerType(android.view.View.LAYER_TYPE_NONE, null);
                }
            }
            switch (mMode) {
                case android.widget.StackView.StackSlider.NORMAL_MODE :
                    viewLp.setVerticalOffset(java.lang.Math.round(((-r) * stackDirection) * mSlideAmount));
                    highlightLp.setVerticalOffset(java.lang.Math.round(((-r) * stackDirection) * mSlideAmount));
                    mHighlight.setAlpha(highlightAlphaInterpolator(r));
                    float alpha = viewAlphaInterpolator(1 - r);
                    // We make sure that views which can't be seen (have 0 alpha) are also invisible
                    // so that they don't interfere with click events.
                    if (((mView.getAlpha() == 0) && (alpha != 0)) && (mView.getVisibility() != android.view.View.VISIBLE)) {
                        mView.setVisibility(android.view.View.VISIBLE);
                    } else
                        if (((alpha == 0) && (mView.getAlpha() != 0)) && (mView.getVisibility() == android.view.View.VISIBLE)) {
                            mView.setVisibility(android.view.View.INVISIBLE);
                        }

                    mView.setAlpha(alpha);
                    mView.setRotationX((stackDirection * 90.0F) * rotationInterpolator(r));
                    mHighlight.setRotationX((stackDirection * 90.0F) * rotationInterpolator(r));
                    break;
                case android.widget.StackView.StackSlider.END_OF_STACK_MODE :
                    r = r * 0.2F;
                    viewLp.setVerticalOffset(java.lang.Math.round(((-stackDirection) * r) * mSlideAmount));
                    highlightLp.setVerticalOffset(java.lang.Math.round(((-stackDirection) * r) * mSlideAmount));
                    mHighlight.setAlpha(highlightAlphaInterpolator(r));
                    break;
                case android.widget.StackView.StackSlider.BEGINNING_OF_STACK_MODE :
                    r = (1 - r) * 0.2F;
                    viewLp.setVerticalOffset(java.lang.Math.round((stackDirection * r) * mSlideAmount));
                    highlightLp.setVerticalOffset(java.lang.Math.round((stackDirection * r) * mSlideAmount));
                    mHighlight.setAlpha(highlightAlphaInterpolator(r));
                    break;
            }
        }

        public void setXProgress(float r) {
            // enforce r between 0 and 1
            r = java.lang.Math.min(2.0F, r);
            r = java.lang.Math.max(-2.0F, r);
            mXProgress = r;
            if (mView == null)
                return;

            final android.widget.StackView.LayoutParams viewLp = ((android.widget.StackView.LayoutParams) (mView.getLayoutParams()));
            final android.widget.StackView.LayoutParams highlightLp = ((android.widget.StackView.LayoutParams) (mHighlight.getLayoutParams()));
            r *= 0.2F;
            viewLp.setHorizontalOffset(java.lang.Math.round(r * mSlideAmount));
            highlightLp.setHorizontalOffset(java.lang.Math.round(r * mSlideAmount));
        }

        void setMode(int mode) {
            mMode = mode;
        }

        float getDurationForNeutralPosition() {
            return getDuration(false, 0);
        }

        float getDurationForOffscreenPosition() {
            return getDuration(true, 0);
        }

        float getDurationForNeutralPosition(float velocity) {
            return getDuration(false, velocity);
        }

        float getDurationForOffscreenPosition(float velocity) {
            return getDuration(true, velocity);
        }

        private float getDuration(boolean invert, float velocity) {
            if (mView != null) {
                final android.widget.StackView.LayoutParams viewLp = ((android.widget.StackView.LayoutParams) (mView.getLayoutParams()));
                float d = ((float) (java.lang.Math.hypot(viewLp.horizontalOffset, viewLp.verticalOffset)));
                float maxd = ((float) (java.lang.Math.hypot(mSlideAmount, 0.4F * mSlideAmount)));
                if (d > maxd) {
                    // Because mSlideAmount is updated in onLayout(), it is possible that d > maxd
                    // if we get onLayout() right before this method is called.
                    d = maxd;
                }
                if (velocity == 0) {
                    return (invert ? 1 - (d / maxd) : d / maxd) * android.widget.StackView.DEFAULT_ANIMATION_DURATION;
                } else {
                    float duration = (invert) ? d / java.lang.Math.abs(velocity) : (maxd - d) / java.lang.Math.abs(velocity);
                    if ((duration < android.widget.StackView.MINIMUM_ANIMATION_DURATION) || (duration > android.widget.StackView.DEFAULT_ANIMATION_DURATION)) {
                        return getDuration(invert, 0);
                    } else {
                        return duration;
                    }
                }
            }
            return 0;
        }

        // Used for animations
        @java.lang.SuppressWarnings({ "UnusedDeclaration" })
        public float getYProgress() {
            return mYProgress;
        }

        // Used for animations
        @java.lang.SuppressWarnings({ "UnusedDeclaration" })
        public float getXProgress() {
            return mXProgress;
        }
    }

    android.widget.StackView.LayoutParams createOrReuseLayoutParams(android.view.View v) {
        final android.view.ViewGroup.LayoutParams currentLp = v.getLayoutParams();
        if (currentLp instanceof android.widget.StackView.LayoutParams) {
            android.widget.StackView.LayoutParams lp = ((android.widget.StackView.LayoutParams) (currentLp));
            lp.setHorizontalOffset(0);
            lp.setVerticalOffset(0);
            lp.width = 0;
            lp.width = 0;
            return lp;
        }
        return new android.widget.StackView.LayoutParams(v);
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        checkForAndHandleDataChanged();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            int childRight = mPaddingLeft + child.getMeasuredWidth();
            int childBottom = mPaddingTop + child.getMeasuredHeight();
            android.widget.StackView.LayoutParams lp = ((android.widget.StackView.LayoutParams) (child.getLayoutParams()));
            child.layout(mPaddingLeft + lp.horizontalOffset, mPaddingTop + lp.verticalOffset, childRight + lp.horizontalOffset, childBottom + lp.verticalOffset);
        }
        onLayout();
    }

    @java.lang.Override
    public void advance() {
        long timeSinceLastInteraction = java.lang.System.currentTimeMillis() - mLastInteractionTime;
        if (mAdapter == null)
            return;

        final int adapterCount = getCount();
        if ((adapterCount == 1) && mLoopViews)
            return;

        if ((mSwipeGestureType == android.widget.StackView.GESTURE_NONE) && (timeSinceLastInteraction > android.widget.StackView.MIN_TIME_BETWEEN_INTERACTION_AND_AUTOADVANCE)) {
            showNext();
        }
    }

    private void measureChildren() {
        final int count = getChildCount();
        final int measuredWidth = getMeasuredWidth();
        final int measuredHeight = getMeasuredHeight();
        final int childWidth = (java.lang.Math.round(measuredWidth * (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_X)) - mPaddingLeft) - mPaddingRight;
        final int childHeight = (java.lang.Math.round(measuredHeight * (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_Y)) - mPaddingTop) - mPaddingBottom;
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            child.measure(android.view.View.MeasureSpec.makeMeasureSpec(childWidth, android.view.View.MeasureSpec.AT_MOST), android.view.View.MeasureSpec.makeMeasureSpec(childHeight, android.view.View.MeasureSpec.AT_MOST));
            if ((child != mHighlight) && (child != mClickFeedback)) {
                final int childMeasuredWidth = child.getMeasuredWidth();
                final int childMeasuredHeight = child.getMeasuredHeight();
                if (childMeasuredWidth > maxWidth) {
                    maxWidth = childMeasuredWidth;
                }
                if (childMeasuredHeight > maxHeight) {
                    maxHeight = childMeasuredHeight;
                }
            }
        }
        mNewPerspectiveShiftX = android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_X * measuredWidth;
        mNewPerspectiveShiftY = android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_Y * measuredHeight;
        // If we have extra space, we try and spread the items out
        if (((maxWidth > 0) && (count > 0)) && (maxWidth < childWidth)) {
            mNewPerspectiveShiftX = measuredWidth - maxWidth;
        }
        if (((maxHeight > 0) && (count > 0)) && (maxHeight < childHeight)) {
            mNewPerspectiveShiftY = measuredHeight - maxHeight;
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        final int widthSpecMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        boolean haveChildRefSize = (mReferenceChildWidth != (-1)) && (mReferenceChildHeight != (-1));
        // We need to deal with the case where our parent hasn't told us how
        // big we should be. In this case we should
        float factorY = 1 / (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_Y);
        if (heightSpecMode == android.view.View.MeasureSpec.UNSPECIFIED) {
            heightSpecSize = (haveChildRefSize) ? (java.lang.Math.round(mReferenceChildHeight * (1 + factorY)) + mPaddingTop) + mPaddingBottom : 0;
        } else
            if (heightSpecMode == android.view.View.MeasureSpec.AT_MOST) {
                if (haveChildRefSize) {
                    int height = (java.lang.Math.round(mReferenceChildHeight * (1 + factorY)) + mPaddingTop) + mPaddingBottom;
                    if (height <= heightSpecSize) {
                        heightSpecSize = height;
                    } else {
                        heightSpecSize |= android.view.View.MEASURED_STATE_TOO_SMALL;
                    }
                } else {
                    heightSpecSize = 0;
                }
            }

        float factorX = 1 / (1 - android.widget.StackView.PERSPECTIVE_SHIFT_FACTOR_X);
        if (widthSpecMode == android.view.View.MeasureSpec.UNSPECIFIED) {
            widthSpecSize = (haveChildRefSize) ? (java.lang.Math.round(mReferenceChildWidth * (1 + factorX)) + mPaddingLeft) + mPaddingRight : 0;
        } else
            if (heightSpecMode == android.view.View.MeasureSpec.AT_MOST) {
                if (haveChildRefSize) {
                    int width = (mReferenceChildWidth + mPaddingLeft) + mPaddingRight;
                    if (width <= widthSpecSize) {
                        widthSpecSize = width;
                    } else {
                        widthSpecSize |= android.view.View.MEASURED_STATE_TOO_SMALL;
                    }
                } else {
                    widthSpecSize = 0;
                }
            }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
        measureChildren();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.StackView.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setScrollable(getChildCount() > 1);
        if (isEnabled()) {
            if (getDisplayedChild() < (getChildCount() - 1)) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            if (getDisplayedChild() > 0) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean performAccessibilityActionInternal(int action, android.os.Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        switch (action) {
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD :
                {
                    if (getDisplayedChild() < (getChildCount() - 1)) {
                        showNext();
                        return true;
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD :
                {
                    if (getDisplayedChild() > 0) {
                        showPrevious();
                        return true;
                    }
                }
                return false;
        }
        return false;
    }

    class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int horizontalOffset;

        int verticalOffset;

        android.view.View mView;

        private final android.graphics.Rect parentRect = new android.graphics.Rect();

        private final android.graphics.Rect invalidateRect = new android.graphics.Rect();

        private final android.graphics.RectF invalidateRectf = new android.graphics.RectF();

        private final android.graphics.Rect globalInvalidateRect = new android.graphics.Rect();

        LayoutParams(android.view.View view) {
            super(0, 0);
            width = 0;
            height = 0;
            horizontalOffset = 0;
            verticalOffset = 0;
            mView = view;
        }

        LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            horizontalOffset = 0;
            verticalOffset = 0;
            width = 0;
            height = 0;
        }

        void invalidateGlobalRegion(android.view.View v, android.graphics.Rect r) {
            // We need to make a new rect here, so as not to modify the one passed
            globalInvalidateRect.set(r);
            globalInvalidateRect.union(0, 0, getWidth(), getHeight());
            android.view.View p = v;
            if (!((v.getParent() != null) && (v.getParent() instanceof android.view.View)))
                return;

            boolean firstPass = true;
            parentRect.set(0, 0, 0, 0);
            while (((p.getParent() != null) && (p.getParent() instanceof android.view.View)) && (!parentRect.contains(globalInvalidateRect))) {
                if (!firstPass) {
                    globalInvalidateRect.offset(p.getLeft() - p.getScrollX(), p.getTop() - p.getScrollY());
                }
                firstPass = false;
                p = ((android.view.View) (p.getParent()));
                parentRect.set(p.getScrollX(), p.getScrollY(), p.getWidth() + p.getScrollX(), p.getHeight() + p.getScrollY());
                p.invalidate(globalInvalidateRect.left, globalInvalidateRect.top, globalInvalidateRect.right, globalInvalidateRect.bottom);
            } 
            p.invalidate(globalInvalidateRect.left, globalInvalidateRect.top, globalInvalidateRect.right, globalInvalidateRect.bottom);
        }

        android.graphics.Rect getInvalidateRect() {
            return invalidateRect;
        }

        void resetInvalidateRect() {
            invalidateRect.set(0, 0, 0, 0);
        }

        // This is public so that ObjectAnimator can access it
        public void setVerticalOffset(int newVerticalOffset) {
            setOffsets(horizontalOffset, newVerticalOffset);
        }

        public void setHorizontalOffset(int newHorizontalOffset) {
            setOffsets(newHorizontalOffset, verticalOffset);
        }

        public void setOffsets(int newHorizontalOffset, int newVerticalOffset) {
            int horizontalOffsetDelta = newHorizontalOffset - horizontalOffset;
            horizontalOffset = newHorizontalOffset;
            int verticalOffsetDelta = newVerticalOffset - verticalOffset;
            verticalOffset = newVerticalOffset;
            if (mView != null) {
                mView.requestLayout();
                int left = java.lang.Math.min(mView.getLeft() + horizontalOffsetDelta, mView.getLeft());
                int right = java.lang.Math.max(mView.getRight() + horizontalOffsetDelta, mView.getRight());
                int top = java.lang.Math.min(mView.getTop() + verticalOffsetDelta, mView.getTop());
                int bottom = java.lang.Math.max(mView.getBottom() + verticalOffsetDelta, mView.getBottom());
                invalidateRectf.set(left, top, right, bottom);
                float xoffset = -invalidateRectf.left;
                float yoffset = -invalidateRectf.top;
                invalidateRectf.offset(xoffset, yoffset);
                mView.getMatrix().mapRect(invalidateRectf);
                invalidateRectf.offset(-xoffset, -yoffset);
                invalidateRect.set(((int) (java.lang.Math.floor(invalidateRectf.left))), ((int) (java.lang.Math.floor(invalidateRectf.top))), ((int) (java.lang.Math.ceil(invalidateRectf.right))), ((int) (java.lang.Math.ceil(invalidateRectf.bottom))));
                invalidateGlobalRegion(mView, invalidateRect);
            }
        }
    }

    private static class HolographicHelper {
        private final android.graphics.Paint mHolographicPaint = new android.graphics.Paint();

        private final android.graphics.Paint mErasePaint = new android.graphics.Paint();

        private final android.graphics.Paint mBlurPaint = new android.graphics.Paint();

        private static final int RES_OUT = 0;

        private static final int CLICK_FEEDBACK = 1;

        private float mDensity;

        private android.graphics.BlurMaskFilter mSmallBlurMaskFilter;

        private android.graphics.BlurMaskFilter mLargeBlurMaskFilter;

        private final android.graphics.Canvas mCanvas = new android.graphics.Canvas();

        private final android.graphics.Canvas mMaskCanvas = new android.graphics.Canvas();

        private final int[] mTmpXY = new int[2];

        private final android.graphics.Matrix mIdentityMatrix = new android.graphics.Matrix();

        HolographicHelper(android.content.Context context) {
            mDensity = context.getResources().getDisplayMetrics().density;
            mHolographicPaint.setFilterBitmap(true);
            mHolographicPaint.setMaskFilter(android.graphics.TableMaskFilter.CreateClipTable(0, 30));
            mErasePaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OUT));
            mErasePaint.setFilterBitmap(true);
            mSmallBlurMaskFilter = new android.graphics.BlurMaskFilter(2 * mDensity, android.graphics.BlurMaskFilter.Blur.NORMAL);
            mLargeBlurMaskFilter = new android.graphics.BlurMaskFilter(4 * mDensity, android.graphics.BlurMaskFilter.Blur.NORMAL);
        }

        android.graphics.Bitmap createClickOutline(android.view.View v, int color) {
            return createOutline(v, android.widget.StackView.HolographicHelper.CLICK_FEEDBACK, color);
        }

        android.graphics.Bitmap createResOutline(android.view.View v, int color) {
            return createOutline(v, android.widget.StackView.HolographicHelper.RES_OUT, color);
        }

        android.graphics.Bitmap createOutline(android.view.View v, int type, int color) {
            mHolographicPaint.setColor(color);
            if (type == android.widget.StackView.HolographicHelper.RES_OUT) {
                mBlurPaint.setMaskFilter(mSmallBlurMaskFilter);
            } else
                if (type == android.widget.StackView.HolographicHelper.CLICK_FEEDBACK) {
                    mBlurPaint.setMaskFilter(mLargeBlurMaskFilter);
                }

            if ((v.getMeasuredWidth() == 0) || (v.getMeasuredHeight() == 0)) {
                return null;
            }
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(v.getResources().getDisplayMetrics(), v.getMeasuredWidth(), v.getMeasuredHeight(), android.graphics.Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(bitmap);
            float rotationX = v.getRotationX();
            float rotation = v.getRotation();
            float translationY = v.getTranslationY();
            float translationX = v.getTranslationX();
            v.setRotationX(0);
            v.setRotation(0);
            v.setTranslationY(0);
            v.setTranslationX(0);
            v.draw(mCanvas);
            v.setRotationX(rotationX);
            v.setRotation(rotation);
            v.setTranslationY(translationY);
            v.setTranslationX(translationX);
            drawOutline(mCanvas, bitmap);
            mCanvas.setBitmap(null);
            return bitmap;
        }

        void drawOutline(android.graphics.Canvas dest, android.graphics.Bitmap src) {
            final int[] xy = mTmpXY;
            android.graphics.Bitmap mask = src.extractAlpha(mBlurPaint, xy);
            mMaskCanvas.setBitmap(mask);
            mMaskCanvas.drawBitmap(src, -xy[0], -xy[1], mErasePaint);
            dest.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            dest.setMatrix(mIdentityMatrix);
            dest.drawBitmap(mask, xy[0], xy[1], mHolographicPaint);
            mMaskCanvas.setBitmap(null);
            mask.recycle();
        }
    }
}

