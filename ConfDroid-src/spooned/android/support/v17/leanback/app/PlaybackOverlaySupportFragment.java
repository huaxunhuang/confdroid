/**
 * This file is auto-generated from PlaybackOverlayFragment.java.  DO NOT MODIFY.
 */
/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.app;


/**
 * A fragment for displaying playback controls and related content.
 * <p>
 * A PlaybackOverlaySupportFragment renders the elements of its {@link ObjectAdapter} as a set
 * of rows in a vertical list.  The Adapter's {@link PresenterSelector} must maintain subclasses
 * of {@link RowPresenter}.
 * </p>
 * <p>
 * An instance of {@link android.support.v17.leanback.widget.PlaybackControlsRow} is expected to be
 * at position 0 in the adapter.
 * </p>
 */
public class PlaybackOverlaySupportFragment extends android.support.v17.leanback.app.DetailsSupportFragment {
    /**
     * No background.
     */
    public static final int BG_NONE = 0;

    /**
     * A dark translucent background.
     */
    public static final int BG_DARK = 1;

    /**
     * A light translucent background.
     */
    public static final int BG_LIGHT = 2;

    /**
     * Listener allowing the application to receive notification of fade in and/or fade out
     * completion events.
     */
    public static class OnFadeCompleteListener {
        public void onFadeInComplete() {
        }

        public void onFadeOutComplete() {
        }
    }

    /**
     * Interface allowing the application to handle input events.
     */
    public interface InputEventHandler {
        /**
         * Called when an {@link InputEvent} is received.
         *
         * @return If the event should be consumed, return true. To allow the event to
        continue on to the next handler, return false.
         */
        public boolean handleInputEvent(android.view.InputEvent event);
    }

    static final java.lang.String TAG = "PlaybackOverlaySupportFragment";

    static final boolean DEBUG = false;

    private static final int ANIMATION_MULTIPLIER = 1;

    static int START_FADE_OUT = 1;

    // Fading status
    static final int IDLE = 0;

    private static final int IN = 1;

    static final int OUT = 2;

    private int mPaddingTop;

    private int mPaddingBottom;

    private android.view.View mRootView;

    private int mBackgroundType = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_DARK;

    private int mBgDarkColor;

    private int mBgLightColor;

    private int mShowTimeMs;

    private int mMajorFadeTranslateY;

    private int mMinorFadeTranslateY;

    int mAnimationTranslateY;

    android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OnFadeCompleteListener mFadeCompleteListener;

    private android.support.v17.leanback.app.PlaybackOverlaySupportFragment.InputEventHandler mInputEventHandler;

    boolean mFadingEnabled = true;

    int mFadingStatus = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE;

    int mBgAlpha;

    private android.animation.ValueAnimator mBgFadeInAnimator;

    private android.animation.ValueAnimator mBgFadeOutAnimator;

    private android.animation.ValueAnimator mControlRowFadeInAnimator;

    private android.animation.ValueAnimator mControlRowFadeOutAnimator;

    private android.animation.ValueAnimator mDescriptionFadeInAnimator;

    private android.animation.ValueAnimator mDescriptionFadeOutAnimator;

    private android.animation.ValueAnimator mOtherRowFadeInAnimator;

    private android.animation.ValueAnimator mOtherRowFadeOutAnimator;

    boolean mResetControlsToPrimaryActionsPending;

    private final android.animation.Animator.AnimatorListener mFadeListener = new android.animation.Animator.AnimatorListener() {
        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
            enableVerticalGridAnimations(false);
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "onAnimationEnd " + mBgAlpha);

            if (mBgAlpha > 0) {
                enableVerticalGridAnimations(true);
                startFadeTimer();
                if (mFadeCompleteListener != null) {
                    mFadeCompleteListener.onFadeInComplete();
                }
            } else {
                android.support.v17.leanback.widget.VerticalGridView verticalView = getVerticalGridView();
                // reset focus to the primary actions only if the selected row was the controls row
                if ((verticalView != null) && (verticalView.getSelectedPosition() == 0)) {
                    resetControlsToPrimaryActions(null);
                }
                if (mFadeCompleteListener != null) {
                    mFadeCompleteListener.onFadeOutComplete();
                }
            }
            mFadingStatus = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE;
        }
    };

    static class FadeHandler extends android.os.Handler {
        @java.lang.Override
        public void handleMessage(android.os.Message message) {
            android.support.v17.leanback.app.PlaybackOverlaySupportFragment fragment;
            if (message.what == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT) {
                fragment = ((java.lang.ref.WeakReference<android.support.v17.leanback.app.PlaybackOverlaySupportFragment>) (message.obj)).get();
                if ((fragment != null) && fragment.mFadingEnabled) {
                    fragment.fade(false);
                }
            }
        }
    }

    static final android.os.Handler sHandler = new android.support.v17.leanback.app.PlaybackOverlaySupportFragment.FadeHandler();

    final java.lang.ref.WeakReference<android.support.v17.leanback.app.PlaybackOverlaySupportFragment> mFragmentReference = new java.lang.ref.WeakReference(this);

    private final android.support.v17.leanback.widget.VerticalGridView.OnTouchInterceptListener mOnTouchInterceptListener = new android.support.v17.leanback.widget.VerticalGridView.OnTouchInterceptListener() {
        @java.lang.Override
        public boolean onInterceptTouchEvent(android.view.MotionEvent event) {
            return onInterceptInputEvent(event);
        }
    };

    private final android.support.v17.leanback.widget.VerticalGridView.OnKeyInterceptListener mOnKeyInterceptListener = new android.support.v17.leanback.widget.VerticalGridView.OnKeyInterceptListener() {
        @java.lang.Override
        public boolean onInterceptKeyEvent(android.view.KeyEvent event) {
            return onInterceptInputEvent(event);
        }
    };

    void setBgAlpha(int alpha) {
        mBgAlpha = alpha;
        if (mRootView != null) {
            mRootView.getBackground().setAlpha(alpha);
        }
    }

    void enableVerticalGridAnimations(boolean enable) {
        if (getVerticalGridView() != null) {
            getVerticalGridView().setAnimateChildLayout(enable);
        }
    }

    void resetControlsToPrimaryActions(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
        if ((vh == null) && (getVerticalGridView() != null)) {
            vh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (getVerticalGridView().findViewHolderForPosition(0)));
        }
        if (vh == null) {
            mResetControlsToPrimaryActionsPending = true;
        } else
            if (vh.getPresenter() instanceof android.support.v17.leanback.widget.PlaybackControlsRowPresenter) {
                mResetControlsToPrimaryActionsPending = false;
                ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter) (vh.getPresenter())).showPrimaryActions(((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh.getViewHolder())));
            }

    }

    /**
     * Enables or disables view fading.  If enabled,
     * the view will be faded in when the fragment starts,
     * and will fade out after a time period.  The timeout
     * period is reset each time {@link #tickle} is called.
     */
    public void setFadingEnabled(boolean enabled) {
        if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "setFadingEnabled " + enabled);

        if (enabled != mFadingEnabled) {
            mFadingEnabled = enabled;
            if (mFadingEnabled) {
                if ((isResumed() && (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE)) && (!android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.hasMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference))) {
                    startFadeTimer();
                }
            } else {
                // Ensure fully opaque
                android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference);
                fade(true);
            }
        }
    }

    /**
     * Returns true if view fading is enabled.
     */
    public boolean isFadingEnabled() {
        return mFadingEnabled;
    }

    /**
     * Sets the listener to be called when fade in or out has completed.
     */
    public void setFadeCompleteListener(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OnFadeCompleteListener listener) {
        mFadeCompleteListener = listener;
    }

    /**
     * Returns the listener to be called when fade in or out has completed.
     */
    public android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OnFadeCompleteListener getFadeCompleteListener() {
        return mFadeCompleteListener;
    }

    /**
     * Sets the input event handler.
     */
    public final void setInputEventHandler(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.InputEventHandler handler) {
        mInputEventHandler = handler;
    }

    /**
     * Returns the input event handler.
     */
    public final android.support.v17.leanback.app.PlaybackOverlaySupportFragment.InputEventHandler getInputEventHandler() {
        return mInputEventHandler;
    }

    /**
     * Tickles the playback controls.  Fades in the view if it was faded out,
     * otherwise resets the fade out timer.  Tickling on input events is handled
     * by the fragment.
     */
    public void tickle() {
        if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, (("tickle enabled " + mFadingEnabled) + " isResumed ") + isResumed());

        if ((!mFadingEnabled) || (!isResumed())) {
            return;
        }
        if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.hasMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference)) {
            // Restart the timer
            startFadeTimer();
        } else {
            fade(true);
        }
    }

    /**
     * Fades out the playback overlay immediately.
     */
    public void fadeOut() {
        android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference);
        fade(false);
    }

    private boolean areControlsHidden() {
        return (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE) && (mBgAlpha == 0);
    }

    boolean onInterceptInputEvent(android.view.InputEvent event) {
        final boolean controlsHidden = areControlsHidden();
        if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, (("onInterceptInputEvent hidden " + controlsHidden) + " ") + event);

        boolean consumeEvent = false;
        int keyCode = android.view.KeyEvent.KEYCODE_UNKNOWN;
        if (mInputEventHandler != null) {
            consumeEvent = mInputEventHandler.handleInputEvent(event);
        }
        if (event instanceof android.view.KeyEvent) {
            keyCode = ((android.view.KeyEvent) (event)).getKeyCode();
        }
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                // Event may be consumed; regardless, if controls are hidden then these keys will
                // bring up the controls.
                if (controlsHidden) {
                    consumeEvent = true;
                }
                tickle();
                break;
            case android.view.KeyEvent.KEYCODE_BACK :
            case android.view.KeyEvent.KEYCODE_ESCAPE :
                // If fading enabled and controls are not hidden, back will be consumed to fade
                // them out (even if the key was consumed by the handler).
                if (mFadingEnabled && (!controlsHidden)) {
                    consumeEvent = true;
                    android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference);
                    fade(false);
                } else
                    if (consumeEvent) {
                        tickle();
                    }

                break;
            default :
                if (consumeEvent) {
                    tickle();
                }
        }
        return consumeEvent;
    }

    @java.lang.Override
    public void onResume() {
        super.onResume();
        if (mFadingEnabled) {
            setBgAlpha(0);
            fade(true);
        }
        getVerticalGridView().setOnTouchInterceptListener(mOnTouchInterceptListener);
        getVerticalGridView().setOnKeyInterceptListener(mOnKeyInterceptListener);
    }

    void startFadeTimer() {
        android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.removeMessages(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference);
        android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.sendMessageDelayed(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.sHandler.obtainMessage(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.START_FADE_OUT, mFragmentReference), mShowTimeMs);
    }

    private static android.animation.ValueAnimator loadAnimator(android.content.Context context, int resId) {
        android.animation.ValueAnimator animator = ((android.animation.ValueAnimator) (android.animation.AnimatorInflater.loadAnimator(context, resId)));
        animator.setDuration(animator.getDuration() * android.support.v17.leanback.app.PlaybackOverlaySupportFragment.ANIMATION_MULTIPLIER);
        return animator;
    }

    private void loadBgAnimator() {
        android.animation.ValueAnimator.AnimatorUpdateListener listener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator arg0) {
                setBgAlpha(((java.lang.Integer) (arg0.getAnimatedValue())));
            }
        };
        mBgFadeInAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_bg_fade_in);
        mBgFadeInAnimator.addUpdateListener(listener);
        mBgFadeInAnimator.addListener(mFadeListener);
        mBgFadeOutAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_bg_fade_out);
        mBgFadeOutAnimator.addUpdateListener(listener);
        mBgFadeOutAnimator.addListener(mFadeListener);
    }

    private android.animation.TimeInterpolator mLogDecelerateInterpolator = new android.support.v17.leanback.animation.LogDecelerateInterpolator(100, 0);

    private android.animation.TimeInterpolator mLogAccelerateInterpolator = new android.support.v17.leanback.animation.LogAccelerateInterpolator(100, 0);

    android.view.View getControlRowView() {
        if (getVerticalGridView() == null) {
            return null;
        }
        android.support.v7.widget.RecyclerView.ViewHolder vh = getVerticalGridView().findViewHolderForPosition(0);
        if (vh == null) {
            return null;
        }
        return vh.itemView;
    }

    private void loadControlRowAnimator() {
        final android.support.v17.leanback.app.PlaybackOverlaySupportFragment.AnimatorListener listener = new android.support.v17.leanback.app.PlaybackOverlaySupportFragment.AnimatorListener() {
            @java.lang.Override
            void getViews(java.util.ArrayList<android.view.View> views) {
                android.view.View view = getControlRowView();
                if (view != null) {
                    views.add(view);
                }
            }
        };
        final android.animation.ValueAnimator.AnimatorUpdateListener updateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator arg0) {
                android.view.View view = getControlRowView();
                if (view != null) {
                    final float fraction = ((java.lang.Float) (arg0.getAnimatedValue()));
                    if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "fraction " + fraction);

                    view.setAlpha(fraction);
                    view.setTranslationY(((float) (mAnimationTranslateY)) * (1.0F - fraction));
                }
            }
        };
        mControlRowFadeInAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_controls_fade_in);
        mControlRowFadeInAnimator.addUpdateListener(updateListener);
        mControlRowFadeInAnimator.addListener(listener);
        mControlRowFadeInAnimator.setInterpolator(mLogDecelerateInterpolator);
        mControlRowFadeOutAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_controls_fade_out);
        mControlRowFadeOutAnimator.addUpdateListener(updateListener);
        mControlRowFadeOutAnimator.addListener(listener);
        mControlRowFadeOutAnimator.setInterpolator(mLogAccelerateInterpolator);
    }

    private void loadOtherRowAnimator() {
        final android.support.v17.leanback.app.PlaybackOverlaySupportFragment.AnimatorListener listener = new android.support.v17.leanback.app.PlaybackOverlaySupportFragment.AnimatorListener() {
            @java.lang.Override
            void getViews(java.util.ArrayList<android.view.View> views) {
                if (getVerticalGridView() == null) {
                    return;
                }
                final int count = getVerticalGridView().getChildCount();
                for (int i = 0; i < count; i++) {
                    android.view.View view = getVerticalGridView().getChildAt(i);
                    if (view != null) {
                        views.add(view);
                    }
                }
            }
        };
        final android.animation.ValueAnimator.AnimatorUpdateListener updateListener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator arg0) {
                if (getVerticalGridView() == null) {
                    return;
                }
                final float fraction = ((java.lang.Float) (arg0.getAnimatedValue()));
                for (android.view.View view : listener.mViews) {
                    if (getVerticalGridView().getChildPosition(view) > 0) {
                        view.setAlpha(fraction);
                        view.setTranslationY(((float) (mAnimationTranslateY)) * (1.0F - fraction));
                    }
                }
            }
        };
        mOtherRowFadeInAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_controls_fade_in);
        mOtherRowFadeInAnimator.addListener(listener);
        mOtherRowFadeInAnimator.addUpdateListener(updateListener);
        mOtherRowFadeInAnimator.setInterpolator(mLogDecelerateInterpolator);
        mOtherRowFadeOutAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_controls_fade_out);
        mOtherRowFadeOutAnimator.addListener(listener);
        mOtherRowFadeOutAnimator.addUpdateListener(updateListener);
        mOtherRowFadeOutAnimator.setInterpolator(new android.view.animation.AccelerateInterpolator());
    }

    private void loadDescriptionAnimator() {
        android.animation.ValueAnimator.AnimatorUpdateListener listener = new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @java.lang.Override
            public void onAnimationUpdate(android.animation.ValueAnimator arg0) {
                if (getVerticalGridView() == null) {
                    return;
                }
                android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder adapterVh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (getVerticalGridView().findViewHolderForPosition(0)));
                if ((adapterVh != null) && (adapterVh.getViewHolder() instanceof android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder)) {
                    final android.support.v17.leanback.widget.Presenter.ViewHolder vh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (adapterVh.getViewHolder())).mDescriptionViewHolder;
                    if (vh != null) {
                        vh.view.setAlpha(((java.lang.Float) (arg0.getAnimatedValue())));
                    }
                }
            }
        };
        mDescriptionFadeInAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_description_fade_in);
        mDescriptionFadeInAnimator.addUpdateListener(listener);
        mDescriptionFadeInAnimator.setInterpolator(mLogDecelerateInterpolator);
        mDescriptionFadeOutAnimator = android.support.v17.leanback.app.PlaybackOverlaySupportFragment.loadAnimator(getActivity(), R.animator.lb_playback_description_fade_out);
        mDescriptionFadeOutAnimator.addUpdateListener(listener);
    }

    void fade(boolean fadeIn) {
        if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
            android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "fade " + fadeIn);

        if (getView() == null) {
            return;
        }
        if ((fadeIn && (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IN)) || ((!fadeIn) && (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OUT))) {
            if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "requested fade in progress");

            return;
        }
        if ((fadeIn && (mBgAlpha == 255)) || ((!fadeIn) && (mBgAlpha == 0))) {
            if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "fade is no-op");

            return;
        }
        mAnimationTranslateY = (getVerticalGridView().getSelectedPosition() == 0) ? mMajorFadeTranslateY : mMinorFadeTranslateY;
        if (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE) {
            if (fadeIn) {
                mBgFadeInAnimator.start();
                mControlRowFadeInAnimator.start();
                mOtherRowFadeInAnimator.start();
                mDescriptionFadeInAnimator.start();
            } else {
                mBgFadeOutAnimator.start();
                mControlRowFadeOutAnimator.start();
                mOtherRowFadeOutAnimator.start();
                mDescriptionFadeOutAnimator.start();
            }
        } else {
            if (fadeIn) {
                mBgFadeOutAnimator.reverse();
                mControlRowFadeOutAnimator.reverse();
                mOtherRowFadeOutAnimator.reverse();
                mDescriptionFadeOutAnimator.reverse();
            } else {
                mBgFadeInAnimator.reverse();
                mControlRowFadeInAnimator.reverse();
                mOtherRowFadeInAnimator.reverse();
                mDescriptionFadeInAnimator.reverse();
            }
        }
        // If fading in while control row is focused, set initial translationY so
        // views slide in from below.
        if (fadeIn && (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE)) {
            final int count = getVerticalGridView().getChildCount();
            for (int i = 0; i < count; i++) {
                getVerticalGridView().getChildAt(i).setTranslationY(mAnimationTranslateY);
            }
        }
        mFadingStatus = (fadeIn) ? android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IN : android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OUT;
    }

    /**
     * Sets the list of rows for the fragment.
     */
    @java.lang.Override
    public void setAdapter(android.support.v17.leanback.widget.ObjectAdapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerObserver(mObserver);
        }
    }

    @java.lang.Override
    void setVerticalGridViewLayout(android.support.v17.leanback.widget.VerticalGridView listview) {
        if (listview == null) {
            return;
        }
        // Padding affects alignment when last row is focused
        // (last is first when there's only one row).
        android.support.v17.leanback.app.PlaybackOverlaySupportFragment.setPadding(listview, mPaddingTop, mPaddingBottom);
        // Item alignment affects focused row that isn't the last.
        listview.setItemAlignmentOffset(0);
        listview.setItemAlignmentOffsetPercent(50);
        // Push rows to the bottom.
        listview.setWindowAlignmentOffset(0);
        listview.setWindowAlignmentOffsetPercent(50);
        listview.setWindowAlignment(android.support.v17.leanback.widget.VerticalGridView.WINDOW_ALIGN_BOTH_EDGE);
    }

    private static void setPadding(android.view.View view, int paddingTop, int paddingBottom) {
        view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), paddingBottom);
    }

    @java.lang.Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaddingTop = getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_padding_top);
        mPaddingBottom = getResources().getDimensionPixelSize(R.dimen.lb_playback_controls_padding_bottom);
        mBgDarkColor = getResources().getColor(R.color.lb_playback_controls_background_dark);
        mBgLightColor = getResources().getColor(R.color.lb_playback_controls_background_light);
        mShowTimeMs = getResources().getInteger(R.integer.lb_playback_controls_show_time_ms);
        mMajorFadeTranslateY = getResources().getDimensionPixelSize(R.dimen.lb_playback_major_fade_translate_y);
        mMinorFadeTranslateY = getResources().getDimensionPixelSize(R.dimen.lb_playback_minor_fade_translate_y);
        loadBgAnimator();
        loadControlRowAnimator();
        loadOtherRowAnimator();
        loadDescriptionAnimator();
    }

    /**
     * Sets the background type.
     *
     * @param type
     * 		One of BG_LIGHT, BG_DARK, or BG_NONE.
     */
    public void setBackgroundType(int type) {
        switch (type) {
            case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_LIGHT :
            case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_DARK :
            case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_NONE :
                if (type != mBackgroundType) {
                    mBackgroundType = type;
                    updateBackground();
                }
                break;
            default :
                throw new java.lang.IllegalArgumentException("Invalid background type");
        }
    }

    /**
     * Returns the background type.
     */
    public int getBackgroundType() {
        return mBackgroundType;
    }

    private void updateBackground() {
        if (mRootView != null) {
            int color = mBgDarkColor;
            switch (mBackgroundType) {
                case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_DARK :
                    break;
                case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_LIGHT :
                    color = mBgLightColor;
                    break;
                case android.support.v17.leanback.app.PlaybackOverlaySupportFragment.BG_NONE :
                    color = android.graphics.Color.TRANSPARENT;
                    break;
            }
            mRootView.setBackground(new android.graphics.drawable.ColorDrawable(color));
        }
    }

    void updateControlsBottomSpace(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
        // Add extra space between rows 0 and 1
        if ((vh == null) && (getVerticalGridView() != null)) {
            vh = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (getVerticalGridView().findViewHolderForPosition(0)));
        }
        if ((vh != null) && (vh.getPresenter() instanceof android.support.v17.leanback.widget.PlaybackControlsRowPresenter)) {
            final int adapterSize = (getAdapter() == null) ? 0 : getAdapter().size();
            ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter) (vh.getPresenter())).showBottomSpace(((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh.getViewHolder())), adapterSize > 1);
        }
    }

    private final android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener mAdapterListener = new android.support.v17.leanback.widget.ItemBridgeAdapter.AdapterListener() {
        @java.lang.Override
        public void onAttachedToWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "onAttachedToWindow " + vh.getViewHolder().view);

            if (((mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.IDLE) && (mBgAlpha == 0)) || (mFadingStatus == android.support.v17.leanback.app.PlaybackOverlaySupportFragment.OUT)) {
                if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                    android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "setting alpha to 0");

                vh.getViewHolder().view.setAlpha(0);
            }
            if ((vh.getPosition() == 0) && mResetControlsToPrimaryActionsPending) {
                resetControlsToPrimaryActions(vh);
            }
        }

        @java.lang.Override
        public void onDetachedFromWindow(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (android.support.v17.leanback.app.PlaybackOverlaySupportFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.PlaybackOverlaySupportFragment.TAG, "onDetachedFromWindow " + vh.getViewHolder().view);

            // Reset animation state
            vh.getViewHolder().view.setAlpha(1.0F);
            vh.getViewHolder().view.setTranslationY(0);
            if (vh.getViewHolder() instanceof android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) {
                android.support.v17.leanback.widget.Presenter.ViewHolder descriptionVh = ((android.support.v17.leanback.widget.PlaybackControlsRowPresenter.ViewHolder) (vh.getViewHolder())).mDescriptionViewHolder;
                if (descriptionVh != null) {
                    descriptionVh.view.setAlpha(1.0F);
                }
            }
        }

        @java.lang.Override
        public void onBind(android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder vh) {
            if (vh.getPosition() == 0) {
                updateControlsBottomSpace(vh);
            }
        }
    };

    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);
        mBgAlpha = 255;
        updateBackground();
        getRowsSupportFragment().setExternalAdapterListener(mAdapterListener);
        return mRootView;
    }

    @java.lang.Override
    public void onDestroyView() {
        mRootView = null;
        super.onDestroyView();
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        // Workaround problem VideoView forcing itself to focused, let controls take focus.
        getRowsSupportFragment().getView().requestFocus();
    }

    private final android.support.v17.leanback.widget.ObjectAdapter.DataObserver mObserver = new android.support.v17.leanback.widget.ObjectAdapter.DataObserver() {
        @java.lang.Override
        public void onChanged() {
            updateControlsBottomSpace(null);
        }
    };

    static abstract class AnimatorListener implements android.animation.Animator.AnimatorListener {
        java.util.ArrayList<android.view.View> mViews = new java.util.ArrayList<android.view.View>();

        java.util.ArrayList<java.lang.Integer> mLayerType = new java.util.ArrayList<java.lang.Integer>();

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationRepeat(android.animation.Animator animation) {
        }

        @java.lang.Override
        public void onAnimationStart(android.animation.Animator animation) {
            getViews(mViews);
            for (android.view.View view : mViews) {
                mLayerType.add(view.getLayerType());
                view.setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animation) {
            for (int i = 0; i < mViews.size(); i++) {
                mViews.get(i).setLayerType(mLayerType.get(i), null);
            }
            mLayerType.clear();
            mViews.clear();
        }

        abstract void getViews(java.util.ArrayList<android.view.View> views);
    }
}

