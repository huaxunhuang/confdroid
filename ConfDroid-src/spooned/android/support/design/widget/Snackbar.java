/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.design.widget;


/**
 * Snackbars provide lightweight feedback about an operation. They show a brief message at the
 * bottom of the screen on mobile and lower left on larger devices. Snackbars appear above all other
 * elements on screen and only one can be displayed at a time.
 * <p>
 * They automatically disappear after a timeout or after user interaction elsewhere on the screen,
 * particularly after interactions that summon a new surface or activity. Snackbars can be swiped
 * off screen.
 * <p>
 * Snackbars can contain an action which is set via
 * {@link #setAction(CharSequence, android.view.View.OnClickListener)}.
 * <p>
 * To be notified when a snackbar has been shown or dismissed, you can provide a {@link Callback}
 * via {@link #setCallback(Callback)}.</p>
 */
public final class Snackbar {
    /**
     * Callback class for {@link Snackbar} instances.
     *
     * @see Snackbar#setCallback(Callback)
     */
    public static abstract class Callback {
        /**
         * Indicates that the Snackbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = 0;

        /**
         * Indicates that the Snackbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = 1;

        /**
         * Indicates that the Snackbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        /**
         * Indicates that the Snackbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = 3;

        /**
         * Indicates that the Snackbar was dismissed from a new Snackbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @android.support.annotation.IntDef({ android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_SWIPE, android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_ACTION, android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_TIMEOUT, android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL, android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface DismissEvent {}

        /**
         * Called when the given {@link Snackbar} has been dismissed, either through a time-out,
         * having been manually dismissed, or an action being clicked.
         *
         * @param snackbar
         * 		The snackbar which has been dismissed.
         * @param event
         * 		The event which caused the dismissal. One of either:
         * 		{@link #DISMISS_EVENT_SWIPE}, {@link #DISMISS_EVENT_ACTION},
         * 		{@link #DISMISS_EVENT_TIMEOUT}, {@link #DISMISS_EVENT_MANUAL} or
         * 		{@link #DISMISS_EVENT_CONSECUTIVE}.
         * @see Snackbar#dismiss()
         */
        public void onDismissed(android.support.design.widget.Snackbar snackbar, @android.support.design.widget.Snackbar.Callback.DismissEvent
        int event) {
            // empty
        }

        /**
         * Called when the given {@link Snackbar} is visible.
         *
         * @param snackbar
         * 		The snackbar which is now visible.
         * @see Snackbar#show()
         */
        public void onShown(android.support.design.widget.Snackbar snackbar) {
            // empty
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.design.widget.Snackbar.LENGTH_INDEFINITE, android.support.design.widget.Snackbar.LENGTH_SHORT, android.support.design.widget.Snackbar.LENGTH_LONG })
    @android.support.annotation.IntRange(from = 1)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Duration {}

    /**
     * Show the Snackbar indefinitely. This means that the Snackbar will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another Snackbar is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = -2;

    /**
     * Show the Snackbar for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = -1;

    /**
     * Show the Snackbar for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 0;

    static final int ANIMATION_DURATION = 250;

    static final int ANIMATION_FADE_DURATION = 180;

    static final android.os.Handler sHandler;

    static final int MSG_SHOW = 0;

    static final int MSG_DISMISS = 1;

    static {
        sHandler = new android.os.Handler(android.os.Looper.getMainLooper(), new android.os.Handler.Callback() {
            @java.lang.Override
            public boolean handleMessage(android.os.Message message) {
                switch (message.what) {
                    case android.support.design.widget.Snackbar.MSG_SHOW :
                        ((android.support.design.widget.Snackbar) (message.obj)).showView();
                        return true;
                    case android.support.design.widget.Snackbar.MSG_DISMISS :
                        ((android.support.design.widget.Snackbar) (message.obj)).hideView(message.arg1);
                        return true;
                }
                return false;
            }
        });
    }

    private final android.view.ViewGroup mTargetParent;

    private final android.content.Context mContext;

    final android.support.design.widget.Snackbar.SnackbarLayout mView;

    private int mDuration;

    private android.support.design.widget.Snackbar.Callback mCallback;

    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    private Snackbar(android.view.ViewGroup parent) {
        mTargetParent = parent;
        mContext = parent.getContext();
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(mContext);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        mView = ((android.support.design.widget.Snackbar.SnackbarLayout) (inflater.inflate(R.layout.design_layout_snackbar, mTargetParent, false)));
        mAccessibilityManager = ((android.view.accessibility.AccessibilityManager) (mContext.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE)));
    }

    /**
     * Make a Snackbar to display a message
     *
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     *
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view
     * 		The view to find a parent from.
     * @param text
     * 		The text to show.  Can be formatted text.
     * @param duration
     * 		How long to display the message.  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
     */
    @android.support.annotation.NonNull
    public static android.support.design.widget.Snackbar make(@android.support.annotation.NonNull
    android.view.View view, @android.support.annotation.NonNull
    java.lang.CharSequence text, @android.support.design.widget.Snackbar.Duration
    int duration) {
        android.support.design.widget.Snackbar snackbar = new android.support.design.widget.Snackbar(android.support.design.widget.Snackbar.findSuitableParent(view));
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    /**
     * Make a Snackbar to display a message.
     *
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     *
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view
     * 		The view to find a parent from.
     * @param resId
     * 		The resource id of the string resource to use. Can be formatted text.
     * @param duration
     * 		How long to display the message.  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
     */
    @android.support.annotation.NonNull
    public static android.support.design.widget.Snackbar make(@android.support.annotation.NonNull
    android.view.View view, @android.support.annotation.StringRes
    int resId, @android.support.design.widget.Snackbar.Duration
    int duration) {
        return android.support.design.widget.Snackbar.make(view, view.getResources().getText(resId), duration);
    }

    private static android.view.ViewGroup findSuitableParent(android.view.View view) {
        android.view.ViewGroup fallback = null;
        do {
            if (view instanceof android.support.design.widget.CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return ((android.view.ViewGroup) (view));
            } else
                if (view instanceof android.widget.FrameLayout) {
                    if (view.getId() == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return ((android.view.ViewGroup) (view));
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = ((android.view.ViewGroup) (view));
                    }
                }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final android.view.ViewParent parent = view.getParent();
                view = (parent instanceof android.view.View) ? ((android.view.View) (parent)) : null;
            }
        } while (view != null );
        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }

    /**
     * Set the action to be displayed in this {@link Snackbar}.
     *
     * @param resId
     * 		String resource to display
     * @param listener
     * 		callback to be invoked when the action is clicked
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setAction(@android.support.annotation.StringRes
    int resId, android.view.View.OnClickListener listener) {
        return setAction(mContext.getText(resId), listener);
    }

    /**
     * Set the action to be displayed in this {@link Snackbar}.
     *
     * @param text
     * 		Text to display
     * @param listener
     * 		callback to be invoked when the action is clicked
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setAction(java.lang.CharSequence text, final android.view.View.OnClickListener listener) {
        final android.widget.TextView tv = mView.getActionView();
        if (android.text.TextUtils.isEmpty(text) || (listener == null)) {
            tv.setVisibility(android.view.View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(android.view.View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new android.view.View.OnClickListener() {
                @java.lang.Override
                public void onClick(android.view.View view) {
                    listener.onClick(view);
                    // Now dismiss the Snackbar
                    dispatchDismiss(android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setActionTextColor(android.content.res.ColorStateList colors) {
        final android.widget.TextView tv = mView.getActionView();
        tv.setTextColor(colors);
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setActionTextColor(@android.support.annotation.ColorInt
    int color) {
        final android.widget.TextView tv = mView.getActionView();
        tv.setTextColor(color);
        return this;
    }

    /**
     * Update the text in this {@link Snackbar}.
     *
     * @param message
     * 		The new text for the Toast.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setText(@android.support.annotation.NonNull
    java.lang.CharSequence message) {
        final android.widget.TextView tv = mView.getMessageView();
        tv.setText(message);
        return this;
    }

    /**
     * Update the text in this {@link Snackbar}.
     *
     * @param resId
     * 		The new text for the Toast.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setText(@android.support.annotation.StringRes
    int resId) {
        return setText(mContext.getText(resId));
    }

    /**
     * Set how long to show the view for.
     *
     * @param duration
     * 		either be one of the predefined lengths:
     * 		{@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, or a custom duration
     * 		in milliseconds.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setDuration(@android.support.design.widget.Snackbar.Duration
    int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    @android.support.design.widget.Snackbar.Duration
    public int getDuration() {
        return mDuration;
    }

    /**
     * Returns the {@link Snackbar}'s view.
     */
    @android.support.annotation.NonNull
    public android.view.View getView() {
        return mView;
    }

    /**
     * Show the {@link Snackbar}.
     */
    public void show() {
        android.support.design.widget.SnackbarManager.getInstance().show(mDuration, mManagerCallback);
    }

    /**
     * Dismiss the {@link Snackbar}.
     */
    public void dismiss() {
        dispatchDismiss(android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL);
    }

    void dispatchDismiss(@android.support.design.widget.Snackbar.Callback.DismissEvent
    int event) {
        android.support.design.widget.SnackbarManager.getInstance().dismiss(mManagerCallback, event);
    }

    /**
     * Set a callback to be called when this the visibility of this {@link Snackbar} changes.
     */
    @android.support.annotation.NonNull
    public android.support.design.widget.Snackbar setCallback(android.support.design.widget.Snackbar.Callback callback) {
        mCallback = callback;
        return this;
    }

    /**
     * Return whether this {@link Snackbar} is currently being shown.
     */
    public boolean isShown() {
        return android.support.design.widget.SnackbarManager.getInstance().isCurrent(mManagerCallback);
    }

    /**
     * Returns whether this {@link Snackbar} is currently being shown, or is queued to be
     * shown next.
     */
    public boolean isShownOrQueued() {
        return android.support.design.widget.SnackbarManager.getInstance().isCurrentOrNext(mManagerCallback);
    }

    final android.support.design.widget.SnackbarManager.Callback mManagerCallback = new android.support.design.widget.SnackbarManager.Callback() {
        @java.lang.Override
        public void show() {
            android.support.design.widget.Snackbar.sHandler.sendMessage(android.support.design.widget.Snackbar.sHandler.obtainMessage(android.support.design.widget.Snackbar.MSG_SHOW, android.support.design.widget.Snackbar.this));
        }

        @java.lang.Override
        public void dismiss(int event) {
            android.support.design.widget.Snackbar.sHandler.sendMessage(android.support.design.widget.Snackbar.sHandler.obtainMessage(android.support.design.widget.Snackbar.MSG_DISMISS, event, 0, android.support.design.widget.Snackbar.this));
        }
    };

    final void showView() {
        if (mView.getParent() == null) {
            final android.view.ViewGroup.LayoutParams lp = mView.getLayoutParams();
            if (lp instanceof android.support.design.widget.CoordinatorLayout.LayoutParams) {
                // If our LayoutParams are from a CoordinatorLayout, we'll setup our Behavior
                final android.support.design.widget.CoordinatorLayout.LayoutParams clp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (lp));
                final android.support.design.widget.Snackbar.Behavior behavior = new android.support.design.widget.Snackbar.Behavior();
                behavior.setStartAlphaSwipeDistance(0.1F);
                behavior.setEndAlphaSwipeDistance(0.6F);
                behavior.setSwipeDirection(android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new android.support.design.widget.SwipeDismissBehavior.OnDismissListener() {
                    @java.lang.Override
                    public void onDismiss(android.view.View view) {
                        view.setVisibility(android.view.View.GONE);
                        dispatchDismiss(android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_SWIPE);
                    }

                    @java.lang.Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case android.support.design.widget.SwipeDismissBehavior.STATE_DRAGGING :
                            case android.support.design.widget.SwipeDismissBehavior.STATE_SETTLING :
                                // If the view is being dragged or settling, cancel the timeout
                                android.support.design.widget.SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                                break;
                            case android.support.design.widget.SwipeDismissBehavior.STATE_IDLE :
                                // If the view has been released and is idle, restore the timeout
                                android.support.design.widget.SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                                break;
                        }
                    }
                });
                clp.setBehavior(behavior);
                // Also set the inset edge so that views can dodge the snackbar correctly
                clp.insetEdge = android.view.Gravity.BOTTOM;
            }
            mTargetParent.addView(mView);
        }
        mView.setOnAttachStateChangeListener(new android.support.design.widget.Snackbar.SnackbarLayout.OnAttachStateChangeListener() {
            @java.lang.Override
            public void onViewAttachedToWindow(android.view.View v) {
            }

            @java.lang.Override
            public void onViewDetachedFromWindow(android.view.View v) {
                if (isShownOrQueued()) {
                    // If we haven't already been dismissed then this event is coming from a
                    // non-user initiated action. Hence we need to make sure that we callback
                    // and keep our state up to date. We need to post the call since removeView()
                    // will call through to onDetachedFromWindow and thus overflow.
                    android.support.design.widget.Snackbar.sHandler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            onViewHidden(android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });
        if (android.support.v4.view.ViewCompat.isLaidOut(mView)) {
            if (shouldAnimate()) {
                // If animations are enabled, animate it in
                animateViewIn();
            } else {
                // Else if anims are disabled just call back now
                onViewShown();
            }
        } else {
            // Otherwise, add one of our layout change listeners and show it in when laid out
            mView.setOnLayoutChangeListener(new android.support.design.widget.Snackbar.SnackbarLayout.OnLayoutChangeListener() {
                @java.lang.Override
                public void onLayoutChange(android.view.View view, int left, int top, int right, int bottom) {
                    mView.setOnLayoutChangeListener(null);
                    if (shouldAnimate()) {
                        // If animations are enabled, animate it in
                        animateViewIn();
                    } else {
                        // Else if anims are disabled just call back now
                        onViewShown();
                    }
                }
            });
        }
    }

    void animateViewIn() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            android.support.v4.view.ViewCompat.setTranslationY(mView, mView.getHeight());
            android.support.v4.view.ViewCompat.animate(mView).translationY(0.0F).setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(android.support.design.widget.Snackbar.ANIMATION_DURATION).setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.view.View view) {
                    mView.animateChildrenIn(android.support.design.widget.Snackbar.ANIMATION_DURATION - android.support.design.widget.Snackbar.ANIMATION_FADE_DURATION, android.support.design.widget.Snackbar.ANIMATION_FADE_DURATION);
                }

                @java.lang.Override
                public void onAnimationEnd(android.view.View view) {
                    onViewShown();
                }
            }).start();
        } else {
            android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_in);
            anim.setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(android.support.design.widget.Snackbar.ANIMATION_DURATION);
            anim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @java.lang.Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    onViewShown();
                }

                @java.lang.Override
                public void onAnimationStart(android.view.animation.Animation animation) {
                }

                @java.lang.Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewOut(final int event) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            android.support.v4.view.ViewCompat.animate(mView).translationY(mView.getHeight()).setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(android.support.design.widget.Snackbar.ANIMATION_DURATION).setListener(new android.support.v4.view.ViewPropertyAnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.view.View view) {
                    mView.animateChildrenOut(0, android.support.design.widget.Snackbar.ANIMATION_FADE_DURATION);
                }

                @java.lang.Override
                public void onAnimationEnd(android.view.View view) {
                    onViewHidden(event);
                }
            }).start();
        } else {
            android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_out);
            anim.setInterpolator(android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(android.support.design.widget.Snackbar.ANIMATION_DURATION);
            anim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @java.lang.Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    onViewHidden(event);
                }

                @java.lang.Override
                public void onAnimationStart(android.view.animation.Animation animation) {
                }

                @java.lang.Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    final void hideView(@android.support.design.widget.Snackbar.Callback.DismissEvent
    final int event) {
        if (shouldAnimate() && (mView.getVisibility() == android.view.View.VISIBLE)) {
            animateViewOut(event);
        } else {
            // If anims are disabled or the view isn't visible, just call back now
            onViewHidden(event);
        }
    }

    void onViewShown() {
        android.support.design.widget.SnackbarManager.getInstance().onShown(mManagerCallback);
        if (mCallback != null) {
            mCallback.onShown(this);
        }
    }

    void onViewHidden(int event) {
        // First tell the SnackbarManager that it has been dismissed
        android.support.design.widget.SnackbarManager.getInstance().onDismissed(mManagerCallback);
        // Now call the dismiss listener (if available)
        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }
        if (android.os.Build.VERSION.SDK_INT < 11) {
            // We need to hide the Snackbar on pre-v11 since it uses an old style Animation.
            // ViewGroup has special handling in removeView() when getAnimation() != null in
            // that it waits. This then means that the calculated insets are wrong and the
            // any dodging views do not return. We workaround it by setting the view to gone while
            // ViewGroup actually gets around to removing it.
            mView.setVisibility(android.view.View.GONE);
        }
        // Lastly, hide and remove the view from the parent (if attached)
        final android.view.ViewParent parent = mView.getParent();
        if (parent instanceof android.view.ViewGroup) {
            ((android.view.ViewGroup) (parent)).removeView(mView);
        }
    }

    /**
     * Returns true if we should animate the Snackbar view in/out.
     */
    boolean shouldAnimate() {
        return !mAccessibilityManager.isEnabled();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class SnackbarLayout extends android.widget.LinearLayout {
        private android.widget.TextView mMessageView;

        private android.widget.Button mActionView;

        private int mMaxWidth;

        private int mMaxInlineActionWidth;

        interface OnLayoutChangeListener {
            void onLayoutChange(android.view.View view, int left, int top, int right, int bottom);
        }

        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(android.view.View v);

            void onViewDetachedFromWindow(android.view.View v);
        }

        private android.support.design.widget.Snackbar.SnackbarLayout.OnLayoutChangeListener mOnLayoutChangeListener;

        private android.support.design.widget.Snackbar.SnackbarLayout.OnAttachStateChangeListener mOnAttachStateChangeListener;

        public SnackbarLayout(android.content.Context context) {
            this(context, null);
        }

        public SnackbarLayout(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
            mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            mMaxInlineActionWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
                android.support.v4.view.ViewCompat.setElevation(this, a.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            a.recycle();
            setClickable(true);
            // Now inflate our content. We need to do this manually rather than using an <include>
            // in the layout since older versions of the Android do not inflate includes with
            // the correct Context.
            android.view.LayoutInflater.from(context).inflate(R.layout.design_layout_snackbar_include, this);
            android.support.v4.view.ViewCompat.setAccessibilityLiveRegion(this, android.support.v4.view.ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
            android.support.v4.view.ViewCompat.setImportantForAccessibility(this, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
            // Make sure that we fit system windows and have a listener to apply any insets
            android.support.v4.view.ViewCompat.setFitsSystemWindows(this, true);
            android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
                @java.lang.Override
                public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
                    // Copy over the bottom inset as padding so that we're displayed above the
                    // navigation bar
                    v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
                    return insets;
                }
            });
        }

        @java.lang.Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            mMessageView = ((android.widget.TextView) (findViewById(R.id.snackbar_text)));
            mActionView = ((android.widget.Button) (findViewById(R.id.snackbar_action)));
        }

        android.widget.TextView getMessageView() {
            return mMessageView;
        }

        android.widget.Button getActionView() {
            return mActionView;
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if ((mMaxWidth > 0) && (getMeasuredWidth() > mMaxWidth)) {
                widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(mMaxWidth, android.view.View.MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            final int multiLineVPadding = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
            final int singleLineVPadding = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
            final boolean isMultiLine = mMessageView.getLayout().getLineCount() > 1;
            boolean remeasure = false;
            if ((isMultiLine && (mMaxInlineActionWidth > 0)) && (mActionView.getMeasuredWidth() > mMaxInlineActionWidth)) {
                if (updateViewsWithinLayout(android.widget.LinearLayout.VERTICAL, multiLineVPadding, multiLineVPadding - singleLineVPadding)) {
                    remeasure = true;
                }
            } else {
                final int messagePadding = (isMultiLine) ? multiLineVPadding : singleLineVPadding;
                if (updateViewsWithinLayout(android.widget.LinearLayout.HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true;
                }
            }
            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        void animateChildrenIn(int delay, int duration) {
            android.support.v4.view.ViewCompat.setAlpha(mMessageView, 0.0F);
            android.support.v4.view.ViewCompat.animate(mMessageView).alpha(1.0F).setDuration(duration).setStartDelay(delay).start();
            if (mActionView.getVisibility() == android.view.View.VISIBLE) {
                android.support.v4.view.ViewCompat.setAlpha(mActionView, 0.0F);
                android.support.v4.view.ViewCompat.animate(mActionView).alpha(1.0F).setDuration(duration).setStartDelay(delay).start();
            }
        }

        void animateChildrenOut(int delay, int duration) {
            android.support.v4.view.ViewCompat.setAlpha(mMessageView, 1.0F);
            android.support.v4.view.ViewCompat.animate(mMessageView).alpha(0.0F).setDuration(duration).setStartDelay(delay).start();
            if (mActionView.getVisibility() == android.view.View.VISIBLE) {
                android.support.v4.view.ViewCompat.setAlpha(mActionView, 1.0F);
                android.support.v4.view.ViewCompat.animate(mActionView).alpha(0.0F).setDuration(duration).setStartDelay(delay).start();
            }
        }

        @java.lang.Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (mOnLayoutChangeListener != null) {
                mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        @java.lang.Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
            android.support.v4.view.ViewCompat.requestApplyInsets(this);
        }

        @java.lang.Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(android.support.design.widget.Snackbar.SnackbarLayout.OnLayoutChangeListener onLayoutChangeListener) {
            mOnLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(android.support.design.widget.Snackbar.SnackbarLayout.OnAttachStateChangeListener listener) {
            mOnAttachStateChangeListener = listener;
        }

        private boolean updateViewsWithinLayout(final int orientation, final int messagePadTop, final int messagePadBottom) {
            boolean changed = false;
            if (orientation != getOrientation()) {
                setOrientation(orientation);
                changed = true;
            }
            if ((mMessageView.getPaddingTop() != messagePadTop) || (mMessageView.getPaddingBottom() != messagePadBottom)) {
                android.support.design.widget.Snackbar.SnackbarLayout.updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
                changed = true;
            }
            return changed;
        }

        private static void updateTopBottomPadding(android.view.View view, int topPadding, int bottomPadding) {
            if (android.support.v4.view.ViewCompat.isPaddingRelative(view)) {
                android.support.v4.view.ViewCompat.setPaddingRelative(view, android.support.v4.view.ViewCompat.getPaddingStart(view), topPadding, android.support.v4.view.ViewCompat.getPaddingEnd(view), bottomPadding);
            } else {
                view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(), bottomPadding);
            }
        }
    }

    final class Behavior extends android.support.design.widget.SwipeDismissBehavior<android.support.design.widget.Snackbar.SnackbarLayout> {
        @java.lang.Override
        public boolean canSwipeDismissView(android.view.View child) {
            return child instanceof android.support.design.widget.Snackbar.SnackbarLayout;
        }

        @java.lang.Override
        public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.Snackbar.SnackbarLayout child, android.view.MotionEvent event) {
            // We want to make sure that we disable any Snackbar timeouts if the user is
            // currently touching the Snackbar. We restore the timeout when complete
            if (parent.isPointInChildBounds(child, ((int) (event.getX())), ((int) (event.getY())))) {
                switch (event.getActionMasked()) {
                    case android.view.MotionEvent.ACTION_DOWN :
                        android.support.design.widget.SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                        break;
                    case android.view.MotionEvent.ACTION_UP :
                    case android.view.MotionEvent.ACTION_CANCEL :
                        android.support.design.widget.SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                        break;
                }
            }
            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}

