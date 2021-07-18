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
package android.widget;


/**
 * Base class for a {@link FrameLayout} container that will perform animations
 * when switching between its views.
 *
 * @unknown ref android.R.styleable#ViewAnimator_inAnimation
 * @unknown ref android.R.styleable#ViewAnimator_outAnimation
 * @unknown ref android.R.styleable#ViewAnimator_animateFirstView
 */
public class ViewAnimator extends android.widget.FrameLayout {
    @android.annotation.UnsupportedAppUsage
    int mWhichChild = 0;

    @android.annotation.UnsupportedAppUsage
    boolean mFirstTime = true;

    boolean mAnimateFirstTime = true;

    android.view.animation.Animation mInAnimation;

    android.view.animation.Animation mOutAnimation;

    public ViewAnimator(android.content.Context context) {
        super(context);
        initViewAnimator(context, null);
    }

    public ViewAnimator(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.ViewAnimator);
        int resource = a.getResourceId(android.widget.com.android.internal.R.styleable.ViewAnimator_inAnimation, 0);
        if (resource > 0) {
            setInAnimation(context, resource);
        }
        resource = a.getResourceId(android.widget.com.android.internal.R.styleable.ViewAnimator_outAnimation, 0);
        if (resource > 0) {
            setOutAnimation(context, resource);
        }
        boolean flag = a.getBoolean(android.widget.com.android.internal.R.styleable.ViewAnimator_animateFirstView, true);
        setAnimateFirstView(flag);
        a.recycle();
        initViewAnimator(context, attrs);
    }

    /**
     * Initialize this {@link ViewAnimator}, possibly setting
     * {@link #setMeasureAllChildren(boolean)} based on {@link FrameLayout} flags.
     */
    private void initViewAnimator(android.content.Context context, android.util.AttributeSet attrs) {
        if (attrs == null) {
            // For compatibility, always measure children when undefined.
            mMeasureAllChildren = true;
            return;
        }
        // For compatibility, default to measure children, but allow XML
        // attribute to override.
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.FrameLayout);
        final boolean measureAllChildren = a.getBoolean(android.widget.com.android.internal.R.styleable.FrameLayout_measureAllChildren, true);
        setMeasureAllChildren(measureAllChildren);
        a.recycle();
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param whichChild
     * 		the index of the child view to display
     */
    @android.view.RemotableViewMethod
    public void setDisplayedChild(int whichChild) {
        mWhichChild = whichChild;
        if (whichChild >= getChildCount()) {
            mWhichChild = 0;
        } else
            if (whichChild < 0) {
                mWhichChild = getChildCount() - 1;
            }

        boolean hasFocus = getFocusedChild() != null;
        // This will clear old focus if we had it
        showOnly(mWhichChild);
        if (hasFocus) {
            // Try to retake focus if we had it
            requestFocus(android.view.View.FOCUS_FORWARD);
        }
    }

    /**
     * Returns the index of the currently displayed child view.
     */
    public int getDisplayedChild() {
        return mWhichChild;
    }

    /**
     * Manually shows the next child.
     */
    @android.view.RemotableViewMethod
    public void showNext() {
        setDisplayedChild(mWhichChild + 1);
    }

    /**
     * Manually shows the previous child.
     */
    @android.view.RemotableViewMethod
    public void showPrevious() {
        setDisplayedChild(mWhichChild - 1);
    }

    /**
     * Shows only the specified child. The other displays Views exit the screen,
     * optionally with the with the {@link #getOutAnimation() out animation} and
     * the specified child enters the screen, optionally with the
     * {@link #getInAnimation() in animation}.
     *
     * @param childIndex
     * 		The index of the child to be shown.
     * @param animate
     * 		Whether or not to use the in and out animations, defaults
     * 		to true.
     */
    @android.annotation.UnsupportedAppUsage
    void showOnly(int childIndex, boolean animate) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (i == childIndex) {
                if (animate && (mInAnimation != null)) {
                    child.startAnimation(mInAnimation);
                }
                child.setVisibility(android.view.View.VISIBLE);
                mFirstTime = false;
            } else {
                if ((animate && (mOutAnimation != null)) && (child.getVisibility() == android.view.View.VISIBLE)) {
                    child.startAnimation(mOutAnimation);
                } else
                    if (child.getAnimation() == mInAnimation)
                        child.clearAnimation();


                child.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     * Shows only the specified child. The other displays Views exit the screen
     * with the {@link #getOutAnimation() out animation} and the specified child
     * enters the screen with the {@link #getInAnimation() in animation}.
     *
     * @param childIndex
     * 		The index of the child to be shown.
     */
    void showOnly(int childIndex) {
        final boolean animate = (!mFirstTime) || mAnimateFirstTime;
        showOnly(childIndex, animate);
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (getChildCount() == 1) {
            child.setVisibility(android.view.View.VISIBLE);
        } else {
            child.setVisibility(android.view.View.GONE);
        }
        if ((index >= 0) && (mWhichChild >= index)) {
            // Added item above current one, increment the index of the displayed child
            setDisplayedChild(mWhichChild + 1);
        }
    }

    @java.lang.Override
    public void removeAllViews() {
        super.removeAllViews();
        mWhichChild = 0;
        mFirstTime = true;
    }

    @java.lang.Override
    public void removeView(android.view.View view) {
        final int index = indexOfChild(view);
        if (index >= 0) {
            removeViewAt(index);
        }
    }

    @java.lang.Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        final int childCount = getChildCount();
        if (childCount == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        } else
            if (mWhichChild >= childCount) {
                // Displayed is above child count, so float down to top of stack
                setDisplayedChild(childCount - 1);
            } else
                if (mWhichChild == index) {
                    // Displayed was removed, so show the new child living in its place
                    setDisplayedChild(mWhichChild);
                }


    }

    public void removeViewInLayout(android.view.View view) {
        removeView(view);
    }

    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        if (getChildCount() == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        } else
            if ((mWhichChild >= start) && (mWhichChild < (start + count))) {
                // Try showing new displayed child, wrapping if needed
                setDisplayedChild(mWhichChild);
            }

    }

    public void removeViewsInLayout(int start, int count) {
        removeViews(start, count);
    }

    /**
     * Returns the View corresponding to the currently displayed child.
     *
     * @return The View currently displayed.
     * @see #getDisplayedChild()
     */
    public android.view.View getCurrentView() {
        return getChildAt(mWhichChild);
    }

    /**
     * Returns the current animation used to animate a View that enters the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setInAnimation(android.view.animation.Animation)
     * @see #setInAnimation(android.content.Context, int)
     */
    @android.view.inspector.InspectableProperty
    public android.view.animation.Animation getInAnimation() {
        return mInAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param inAnimation
     * 		The animation started when a View enters the screen.
     * @see #getInAnimation()
     * @see #setInAnimation(android.content.Context, int)
     */
    public void setInAnimation(android.view.animation.Animation inAnimation) {
        mInAnimation = inAnimation;
    }

    /**
     * Returns the current animation used to animate a View that exits the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setOutAnimation(android.view.animation.Animation)
     * @see #setOutAnimation(android.content.Context, int)
     */
    @android.view.inspector.InspectableProperty
    public android.view.animation.Animation getOutAnimation() {
        return mOutAnimation;
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param outAnimation
     * 		The animation started when a View exit the screen.
     * @see #getOutAnimation()
     * @see #setOutAnimation(android.content.Context, int)
     */
    public void setOutAnimation(android.view.animation.Animation outAnimation) {
        mOutAnimation = outAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param context
     * 		The application's environment.
     * @param resourceID
     * 		The resource id of the animation.
     * @see #getInAnimation()
     * @see #setInAnimation(android.view.animation.Animation)
     */
    public void setInAnimation(android.content.Context context, @android.annotation.AnimRes
    int resourceID) {
        setInAnimation(android.view.animation.AnimationUtils.loadAnimation(context, resourceID));
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param context
     * 		The application's environment.
     * @param resourceID
     * 		The resource id of the animation.
     * @see #getOutAnimation()
     * @see #setOutAnimation(android.view.animation.Animation)
     */
    public void setOutAnimation(android.content.Context context, @android.annotation.AnimRes
    int resourceID) {
        setOutAnimation(android.view.animation.AnimationUtils.loadAnimation(context, resourceID));
    }

    /**
     * Returns whether the current View should be animated the first time the ViewAnimator
     * is displayed.
     *
     * @return true if the current View will be animated the first time it is displayed,
    false otherwise.
     * @see #setAnimateFirstView(boolean)
     */
    @android.view.inspector.InspectableProperty
    public boolean getAnimateFirstView() {
        return mAnimateFirstTime;
    }

    /**
     * Indicates whether the current View should be animated the first time
     * the ViewAnimator is displayed.
     *
     * @param animate
     * 		True to animate the current View the first time it is displayed,
     * 		false otherwise.
     */
    public void setAnimateFirstView(boolean animate) {
        mAnimateFirstTime = animate;
    }

    @java.lang.Override
    public int getBaseline() {
        return getCurrentView() != null ? getCurrentView().getBaseline() : super.getBaseline();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ViewAnimator.class.getName();
    }
}

