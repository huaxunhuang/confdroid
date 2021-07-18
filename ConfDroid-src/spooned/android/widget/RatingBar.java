/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A RatingBar is an extension of SeekBar and ProgressBar that shows a rating in
 * stars. The user can touch/drag or use arrow keys to set the rating when using
 * the default size RatingBar. The smaller RatingBar style (
 * {@link android.R.attr#ratingBarStyleSmall}) and the larger indicator-only
 * style ({@link android.R.attr#ratingBarStyleIndicator}) do not support user
 * interaction and should only be used as indicators.
 * <p>
 * When using a RatingBar that supports user interaction, placing widgets to the
 * left or right of the RatingBar is discouraged.
 * <p>
 * The number of stars set (via {@link #setNumStars(int)} or in an XML layout)
 * will be shown when the layout width is set to wrap content (if another layout
 * width is set, the results may be unpredictable).
 * <p>
 * The secondary progress should not be modified by the client as it is used
 * internally as the background for a fractionally filled star.
 *
 * @unknown ref android.R.styleable#RatingBar_numStars
 * @unknown ref android.R.styleable#RatingBar_rating
 * @unknown ref android.R.styleable#RatingBar_stepSize
 * @unknown ref android.R.styleable#RatingBar_isIndicator
 */
public class RatingBar extends android.widget.AbsSeekBar {
    /**
     * A callback that notifies clients when the rating has been changed. This
     * includes changes that were initiated by the user through a touch gesture
     * or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnRatingBarChangeListener {
        /**
         * Notification that the rating has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         *
         * @param ratingBar
         * 		The RatingBar whose rating has changed.
         * @param rating
         * 		The current rating. This will be in the range
         * 		0..numStars.
         * @param fromUser
         * 		True if the rating change was initiated by a user's
         * 		touch gesture or arrow key/horizontal trackbell movement.
         */
        void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser);
    }

    private int mNumStars = 5;

    private int mProgressOnStartTracking;

    @android.annotation.UnsupportedAppUsage
    private android.widget.RatingBar.OnRatingBarChangeListener mOnRatingBarChangeListener;

    public RatingBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RatingBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.RatingBar, attrs, a, defStyleAttr, defStyleRes);
        final int numStars = a.getInt(R.styleable.RatingBar_numStars, mNumStars);
        setIsIndicator(a.getBoolean(R.styleable.RatingBar_isIndicator, !mIsUserSeekable));
        final float rating = a.getFloat(R.styleable.RatingBar_rating, -1);
        final float stepSize = a.getFloat(R.styleable.RatingBar_stepSize, -1);
        a.recycle();
        if ((numStars > 0) && (numStars != mNumStars)) {
            setNumStars(numStars);
        }
        if (stepSize >= 0) {
            setStepSize(stepSize);
        } else {
            setStepSize(0.5F);
        }
        if (rating >= 0) {
            setRating(rating);
        }
        // A touch inside a star fill up to that fractional area (slightly more
        // than 0.5 so boundaries round up).
        mTouchProgressOffset = 0.6F;
    }

    public RatingBar(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.ratingBarStyle);
    }

    public RatingBar(android.content.Context context) {
        this(context, null);
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener
     * 		The listener.
     */
    public void setOnRatingBarChangeListener(android.widget.RatingBar.OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
    }

    /**
     *
     *
     * @return The listener (may be null) that is listening for rating change
    events.
     */
    public android.widget.RatingBar.OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return mOnRatingBarChangeListener;
    }

    /**
     * Whether this rating bar should only be an indicator (thus non-changeable
     * by the user).
     *
     * @param isIndicator
     * 		Whether it should be an indicator.
     * @unknown ref android.R.styleable#RatingBar_isIndicator
     */
    public void setIsIndicator(boolean isIndicator) {
        mIsUserSeekable = !isIndicator;
        if (isIndicator) {
            setFocusable(android.view.View.FOCUSABLE_AUTO);
        } else {
            setFocusable(android.view.View.FOCUSABLE);
        }
    }

    /**
     *
     *
     * @return Whether this rating bar is only an indicator.
     * @unknown ref android.R.styleable#RatingBar_isIndicator
     */
    @android.view.inspector.InspectableProperty(name = "isIndicator")
    public boolean isIndicator() {
        return !mIsUserSeekable;
    }

    /**
     * Sets the number of stars to show. In order for these to be shown
     * properly, it is recommended the layout width of this widget be wrap
     * content.
     *
     * @param numStars
     * 		The number of stars.
     */
    public void setNumStars(final int numStars) {
        if (numStars <= 0) {
            return;
        }
        mNumStars = numStars;
        // This causes the width to change, so re-layout
        requestLayout();
    }

    /**
     * Returns the number of stars shown.
     *
     * @return The number of stars shown.
     */
    @android.view.inspector.InspectableProperty
    public int getNumStars() {
        return mNumStars;
    }

    /**
     * Sets the rating (the number of stars filled).
     *
     * @param rating
     * 		The rating to set.
     */
    public void setRating(float rating) {
        setProgress(java.lang.Math.round(rating * getProgressPerStar()));
    }

    /**
     * Gets the current rating (number of stars filled).
     *
     * @return The current rating.
     */
    @android.view.inspector.InspectableProperty
    public float getRating() {
        return getProgress() / getProgressPerStar();
    }

    /**
     * Sets the step size (granularity) of this rating bar.
     *
     * @param stepSize
     * 		The step size of this rating bar. For example, if
     * 		half-star granularity is wanted, this would be 0.5.
     */
    public void setStepSize(float stepSize) {
        if (stepSize <= 0) {
            return;
        }
        final float newMax = mNumStars / stepSize;
        final int newProgress = ((int) ((newMax / getMax()) * getProgress()));
        setMax(((int) (newMax)));
        setProgress(newProgress);
    }

    /**
     * Gets the step size of this rating bar.
     *
     * @return The step size.
     */
    @android.view.inspector.InspectableProperty
    public float getStepSize() {
        return ((float) (getNumStars())) / getMax();
    }

    /**
     *
     *
     * @return The amount of progress that fits into a star
     */
    private float getProgressPerStar() {
        if (mNumStars > 0) {
            return (1.0F * getMax()) / mNumStars;
        } else {
            return 1;
        }
    }

    @java.lang.Override
    android.graphics.drawable.shapes.Shape getDrawableShape() {
        // TODO: Once ProgressBar's TODOs are fixed, this won't be needed
        return new android.graphics.drawable.shapes.RectShape();
    }

    @java.lang.Override
    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        super.onProgressRefresh(scale, fromUser, progress);
        // Keep secondary progress in sync with primary
        updateSecondaryProgress(progress);
        if (!fromUser) {
            // Callback for non-user rating changes
            dispatchRatingChange(false);
        }
    }

    /**
     * The secondary progress is used to differentiate the background of a
     * partially filled star. This method keeps the secondary progress in sync
     * with the progress.
     *
     * @param progress
     * 		The primary progress level.
     */
    private void updateSecondaryProgress(int progress) {
        final float ratio = getProgressPerStar();
        if (ratio > 0) {
            final float progressInStars = progress / ratio;
            final int secondaryProgress = ((int) (java.lang.Math.ceil(progressInStars) * ratio));
            setSecondaryProgress(secondaryProgress);
        }
    }

    @java.lang.Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSampleWidth > 0) {
            final int width = mSampleWidth * mNumStars;
            setMeasuredDimension(android.view.View.resolveSizeAndState(width, widthMeasureSpec, 0), getMeasuredHeight());
        }
    }

    @java.lang.Override
    void onStartTrackingTouch() {
        mProgressOnStartTracking = getProgress();
        super.onStartTrackingTouch();
    }

    @java.lang.Override
    void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        if (getProgress() != mProgressOnStartTracking) {
            dispatchRatingChange(true);
        }
    }

    @java.lang.Override
    void onKeyChange() {
        super.onKeyChange();
        dispatchRatingChange(true);
    }

    void dispatchRatingChange(boolean fromUser) {
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, getRating(), fromUser);
        }
    }

    @java.lang.Override
    public synchronized void setMax(int max) {
        // Disallow max progress = 0
        if (max <= 0) {
            return;
        }
        super.setMax(max);
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.RatingBar.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (canUserSetProgress()) {
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
        }
    }

    @java.lang.Override
    boolean canUserSetProgress() {
        return super.canUserSetProgress() && (!isIndicator());
    }
}

