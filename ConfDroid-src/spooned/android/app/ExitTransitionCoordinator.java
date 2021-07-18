/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.app;


/**
 * This ActivityTransitionCoordinator is created in ActivityOptions#makeSceneTransitionAnimation
 * to govern the exit of the Scene and the shared elements when calling an Activity as well as
 * the reentry of the Scene when coming back from the called Activity.
 */
class ExitTransitionCoordinator extends android.app.ActivityTransitionCoordinator {
    private static final java.lang.String TAG = "ExitTransitionCoordinator";

    private static final long MAX_WAIT_MS = 1000;

    private android.os.Bundle mSharedElementBundle;

    private boolean mExitNotified;

    private boolean mSharedElementNotified;

    private android.app.Activity mActivity;

    private boolean mIsBackgroundReady;

    private boolean mIsCanceled;

    private android.os.Handler mHandler;

    private android.animation.ObjectAnimator mBackgroundAnimator;

    private boolean mIsHidden;

    private android.os.Bundle mExitSharedElementBundle;

    private boolean mIsExitStarted;

    private boolean mSharedElementsHidden;

    private android.app.ExitTransitionCoordinator.HideSharedElementsCallback mHideSharedElementsCallback;

    public ExitTransitionCoordinator(android.app.Activity activity, android.view.Window window, android.app.SharedElementCallback listener, java.util.ArrayList<java.lang.String> names, java.util.ArrayList<java.lang.String> accepted, java.util.ArrayList<android.view.View> mapped, boolean isReturning) {
        super(window, names, listener, isReturning);
        viewsReady(mapSharedElements(accepted, mapped));
        stripOffscreenViews();
        mIsBackgroundReady = !isReturning;
        mActivity = activity;
    }

    void setHideSharedElementsCallback(android.app.ExitTransitionCoordinator.HideSharedElementsCallback callback) {
        mHideSharedElementsCallback = callback;
    }

    @java.lang.Override
    protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
        switch (resultCode) {
            case android.app.ActivityTransitionCoordinator.MSG_SET_REMOTE_RECEIVER :
                stopCancel();
                mResultReceiver = resultData.getParcelable(android.app.ActivityTransitionCoordinator.KEY_REMOTE_RECEIVER);
                if (mIsCanceled) {
                    mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_CANCEL, null);
                    mResultReceiver = null;
                } else {
                    notifyComplete();
                }
                break;
            case android.app.ActivityTransitionCoordinator.MSG_HIDE_SHARED_ELEMENTS :
                stopCancel();
                if (!mIsCanceled) {
                    hideSharedElements();
                }
                break;
            case android.app.ActivityTransitionCoordinator.MSG_START_EXIT_TRANSITION :
                mHandler.removeMessages(android.app.ActivityTransitionCoordinator.MSG_CANCEL);
                startExit();
                break;
            case android.app.ActivityTransitionCoordinator.MSG_SHARED_ELEMENT_DESTINATION :
                mExitSharedElementBundle = resultData;
                sharedElementExitBack();
                break;
            case android.app.ActivityTransitionCoordinator.MSG_CANCEL :
                mIsCanceled = true;
                finish();
                break;
        }
    }

    private void stopCancel() {
        if (mHandler != null) {
            mHandler.removeMessages(android.app.ActivityTransitionCoordinator.MSG_CANCEL);
        }
    }

    private void delayCancel() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(android.app.ActivityTransitionCoordinator.MSG_CANCEL, android.app.ExitTransitionCoordinator.MAX_WAIT_MS);
        }
    }

    public void resetViews() {
        if (mTransitioningViews != null) {
            showViews(mTransitioningViews, true);
            setTransitioningViewsVisiblity(android.view.View.VISIBLE, true);
        }
        showViews(mSharedElements, true);
        mIsHidden = true;
        android.view.ViewGroup decorView = getDecor();
        if ((!mIsReturning) && (decorView != null)) {
            decorView.suppressLayout(false);
        }
        moveSharedElementsFromOverlay();
        clearState();
    }

    private void sharedElementExitBack() {
        final android.view.ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.suppressLayout(true);
        }
        if (((((decorView != null) && (mExitSharedElementBundle != null)) && (!mExitSharedElementBundle.isEmpty())) && (!mSharedElements.isEmpty())) && (getSharedElementTransition() != null)) {
            startTransition(new java.lang.Runnable() {
                public void run() {
                    startSharedElementExit(decorView);
                }
            });
        } else {
            sharedElementTransitionComplete();
        }
    }

    private void startSharedElementExit(final android.view.ViewGroup decorView) {
        android.transition.Transition transition = getSharedElementExitTransition();
        transition.addListener(new android.transition.Transition.TransitionListenerAdapter() {
            @java.lang.Override
            public void onTransitionEnd(android.transition.Transition transition) {
                transition.removeListener(this);
                if (isViewsTransitionComplete()) {
                    delayCancel();
                }
            }
        });
        final java.util.ArrayList<android.view.View> sharedElementSnapshots = createSnapshots(mExitSharedElementBundle, mSharedElementNames);
        decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
            @java.lang.Override
            public boolean onPreDraw() {
                decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                setSharedElementState(mExitSharedElementBundle, sharedElementSnapshots);
                return true;
            }
        });
        setGhostVisibility(android.view.View.INVISIBLE);
        scheduleGhostVisibilityChange(android.view.View.INVISIBLE);
        if (mListener != null) {
            mListener.onSharedElementEnd(mSharedElementNames, mSharedElements, sharedElementSnapshots);
        }
        android.transition.TransitionManager.beginDelayedTransition(decorView, transition);
        scheduleGhostVisibilityChange(android.view.View.VISIBLE);
        setGhostVisibility(android.view.View.VISIBLE);
        decorView.invalidate();
    }

    private void hideSharedElements() {
        moveSharedElementsFromOverlay();
        if (mHideSharedElementsCallback != null) {
            mHideSharedElementsCallback.hideSharedElements();
        }
        if (!mIsHidden) {
            hideViews(mSharedElements);
        }
        mSharedElementsHidden = true;
        finishIfNecessary();
    }

    public void startExit() {
        if (!mIsExitStarted) {
            mIsExitStarted = true;
            pauseInput();
            android.view.ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            moveSharedElementsToOverlay();
            startTransition(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (mActivity != null) {
                        beginTransitions();
                    } else {
                        startExitTransition();
                    }
                }
            });
        }
    }

    public void startExit(int resultCode, android.content.Intent data) {
        if (!mIsExitStarted) {
            mIsExitStarted = true;
            pauseInput();
            android.view.ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            mHandler = new android.os.Handler() {
                @java.lang.Override
                public void handleMessage(android.os.Message msg) {
                    mIsCanceled = true;
                    finish();
                }
            };
            delayCancel();
            moveSharedElementsToOverlay();
            if ((decorView != null) && (decorView.getBackground() == null)) {
                getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.BLACK));
            }
            final boolean targetsM = (decorView == null) || (decorView.getContext().getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.M);
            java.util.ArrayList<java.lang.String> sharedElementNames = (targetsM) ? mSharedElementNames : mAllSharedElementNames;
            android.app.ActivityOptions options = android.app.ActivityOptions.makeSceneTransitionAnimation(mActivity, this, sharedElementNames, resultCode, data);
            mActivity.convertToTranslucent(new android.app.Activity.TranslucentConversionListener() {
                @java.lang.Override
                public void onTranslucentConversionComplete(boolean drawComplete) {
                    if (!mIsCanceled) {
                        fadeOutBackground();
                    }
                }
            }, options);
            startTransition(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    startExitTransition();
                }
            });
        }
    }

    public void stop() {
        if (mIsReturning && (mActivity != null)) {
            // Override the previous ActivityOptions. We don't want the
            // activity to have options since we're essentially canceling the
            // transition and finishing right now.
            mActivity.convertToTranslucent(null, null);
            finish();
        }
    }

    private void startExitTransition() {
        android.transition.Transition transition = getExitTransition();
        android.view.ViewGroup decorView = getDecor();
        if (((transition != null) && (decorView != null)) && (mTransitioningViews != null)) {
            setTransitioningViewsVisiblity(android.view.View.VISIBLE, false);
            android.transition.TransitionManager.beginDelayedTransition(decorView, transition);
            setTransitioningViewsVisiblity(android.view.View.INVISIBLE, false);
            decorView.invalidate();
        } else {
            transitionStarted();
        }
    }

    private void fadeOutBackground() {
        if (mBackgroundAnimator == null) {
            android.view.ViewGroup decor = getDecor();
            android.graphics.drawable.Drawable background;
            if ((decor != null) && ((background = decor.getBackground()) != null)) {
                background = background.mutate();
                getWindow().setBackgroundDrawable(background);
                mBackgroundAnimator = android.animation.ObjectAnimator.ofInt(background, "alpha", 0);
                mBackgroundAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        mBackgroundAnimator = null;
                        if (!mIsCanceled) {
                            mIsBackgroundReady = true;
                            notifyComplete();
                        }
                    }
                });
                mBackgroundAnimator.setDuration(getFadeDuration());
                mBackgroundAnimator.start();
            } else {
                mIsBackgroundReady = true;
            }
        }
    }

    private android.transition.Transition getExitTransition() {
        android.transition.Transition viewsTransition = null;
        if ((mTransitioningViews != null) && (!mTransitioningViews.isEmpty())) {
            viewsTransition = configureTransition(getViewsTransition(), true);
        }
        if (viewsTransition == null) {
            viewsTransitionComplete();
        } else {
            final java.util.ArrayList<android.view.View> transitioningViews = mTransitioningViews;
            viewsTransition.addListener(new android.app.ActivityTransitionCoordinator.ContinueTransitionListener() {
                @java.lang.Override
                public void onTransitionEnd(android.transition.Transition transition) {
                    transition.removeListener(this);
                    viewsTransitionComplete();
                    if (mIsHidden && (transitioningViews != null)) {
                        showViews(transitioningViews, true);
                        setTransitioningViewsVisiblity(android.view.View.VISIBLE, true);
                    }
                    if (mSharedElementBundle != null) {
                        delayCancel();
                    }
                    super.onTransitionEnd(transition);
                }
            });
        }
        return viewsTransition;
    }

    private android.transition.Transition getSharedElementExitTransition() {
        android.transition.Transition sharedElementTransition = null;
        if (!mSharedElements.isEmpty()) {
            sharedElementTransition = configureTransition(getSharedElementTransition(), false);
        }
        if (sharedElementTransition == null) {
            sharedElementTransitionComplete();
        } else {
            sharedElementTransition.addListener(new android.app.ActivityTransitionCoordinator.ContinueTransitionListener() {
                @java.lang.Override
                public void onTransitionEnd(android.transition.Transition transition) {
                    transition.removeListener(this);
                    sharedElementTransitionComplete();
                    if (mIsHidden) {
                        showViews(mSharedElements, true);
                    }
                }
            });
            mSharedElements.get(0).invalidate();
        }
        return sharedElementTransition;
    }

    private void beginTransitions() {
        android.transition.Transition sharedElementTransition = getSharedElementExitTransition();
        android.transition.Transition viewsTransition = getExitTransition();
        android.transition.Transition transition = android.app.ActivityTransitionCoordinator.mergeTransitions(sharedElementTransition, viewsTransition);
        android.view.ViewGroup decorView = getDecor();
        if ((transition != null) && (decorView != null)) {
            setGhostVisibility(android.view.View.INVISIBLE);
            scheduleGhostVisibilityChange(android.view.View.INVISIBLE);
            if (viewsTransition != null) {
                setTransitioningViewsVisiblity(android.view.View.VISIBLE, false);
            }
            android.transition.TransitionManager.beginDelayedTransition(decorView, transition);
            scheduleGhostVisibilityChange(android.view.View.VISIBLE);
            setGhostVisibility(android.view.View.VISIBLE);
            if (viewsTransition != null) {
                setTransitioningViewsVisiblity(android.view.View.INVISIBLE, false);
            }
            decorView.invalidate();
        } else {
            transitionStarted();
        }
    }

    protected boolean isReadyToNotify() {
        return ((mSharedElementBundle != null) && (mResultReceiver != null)) && mIsBackgroundReady;
    }

    @java.lang.Override
    protected void sharedElementTransitionComplete() {
        mSharedElementBundle = (mExitSharedElementBundle == null) ? captureSharedElementState() : captureExitSharedElementsState();
        super.sharedElementTransitionComplete();
    }

    private android.os.Bundle captureExitSharedElementsState() {
        android.os.Bundle bundle = new android.os.Bundle();
        android.graphics.RectF bounds = new android.graphics.RectF();
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        for (int i = 0; i < mSharedElements.size(); i++) {
            java.lang.String name = mSharedElementNames.get(i);
            android.os.Bundle sharedElementState = mExitSharedElementBundle.getBundle(name);
            if (sharedElementState != null) {
                bundle.putBundle(name, sharedElementState);
            } else {
                android.view.View view = mSharedElements.get(i);
                captureSharedElementState(view, name, bundle, matrix, bounds);
            }
        }
        return bundle;
    }

    @java.lang.Override
    protected void onTransitionsComplete() {
        notifyComplete();
    }

    protected void notifyComplete() {
        if (isReadyToNotify()) {
            if (!mSharedElementNotified) {
                mSharedElementNotified = true;
                delayCancel();
                if (mListener == null) {
                    mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_TAKE_SHARED_ELEMENTS, mSharedElementBundle);
                    notifyExitComplete();
                } else {
                    final android.os.ResultReceiver resultReceiver = mResultReceiver;
                    final android.os.Bundle sharedElementBundle = mSharedElementBundle;
                    mListener.onSharedElementsArrived(mSharedElementNames, mSharedElements, new android.app.SharedElementCallback.OnSharedElementsReadyListener() {
                        @java.lang.Override
                        public void onSharedElementsReady() {
                            resultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_TAKE_SHARED_ELEMENTS, sharedElementBundle);
                            notifyExitComplete();
                        }
                    });
                }
            } else {
                notifyExitComplete();
            }
        }
    }

    private void notifyExitComplete() {
        if ((!mExitNotified) && isViewsTransitionComplete()) {
            mExitNotified = true;
            mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_EXIT_TRANSITION_COMPLETE, null);
            mResultReceiver = null;// done talking

            android.view.ViewGroup decorView = getDecor();
            if ((!mIsReturning) && (decorView != null)) {
                decorView.suppressLayout(false);
            }
            finishIfNecessary();
        }
    }

    private void finishIfNecessary() {
        if (((mIsReturning && mExitNotified) && (mActivity != null)) && (mSharedElements.isEmpty() || mSharedElementsHidden)) {
            finish();
        }
        if ((!mIsReturning) && mExitNotified) {
            mActivity = null;// don't need it anymore

        }
    }

    private void finish() {
        stopCancel();
        if (mActivity != null) {
            mActivity.mActivityTransitionState.clear();
            mActivity.finish();
            mActivity.overridePendingTransition(0, 0);
            mActivity = null;
        }
        // Clear the state so that we can't hold any references accidentally and leak memory.
        clearState();
    }

    @java.lang.Override
    protected void clearState() {
        mHandler = null;
        mSharedElementBundle = null;
        if (mBackgroundAnimator != null) {
            mBackgroundAnimator.cancel();
            mBackgroundAnimator = null;
        }
        mExitSharedElementBundle = null;
        super.clearState();
    }

    @java.lang.Override
    protected boolean moveSharedElementWithParent() {
        return !mIsReturning;
    }

    @java.lang.Override
    protected android.transition.Transition getViewsTransition() {
        if (mIsReturning) {
            return getWindow().getReturnTransition();
        } else {
            return getWindow().getExitTransition();
        }
    }

    protected android.transition.Transition getSharedElementTransition() {
        if (mIsReturning) {
            return getWindow().getSharedElementReturnTransition();
        } else {
            return getWindow().getSharedElementExitTransition();
        }
    }

    interface HideSharedElementsCallback {
        void hideSharedElements();
    }
}

