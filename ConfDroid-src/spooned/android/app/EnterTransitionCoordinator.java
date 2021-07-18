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
 * This ActivityTransitionCoordinator is created by the Activity to manage
 * the enter scene and shared element transfer into the Scene, either during
 * launch of an Activity or returning from a launched Activity.
 */
class EnterTransitionCoordinator extends android.app.ActivityTransitionCoordinator {
    private static final java.lang.String TAG = "EnterTransitionCoordinator";

    private static final int MIN_ANIMATION_FRAMES = 2;

    private boolean mSharedElementTransitionStarted;

    private android.app.Activity mActivity;

    private boolean mHasStopped;

    private boolean mIsCanceled;

    private android.animation.ObjectAnimator mBackgroundAnimator;

    private boolean mIsExitTransitionComplete;

    private boolean mIsReadyForTransition;

    private android.os.Bundle mSharedElementsBundle;

    private boolean mWasOpaque;

    private boolean mAreViewsReady;

    private boolean mIsViewsTransitionStarted;

    private android.transition.Transition mEnterViewsTransition;

    private android.view.ViewTreeObserver.OnPreDrawListener mViewsReadyListener;

    private final boolean mIsCrossTask;

    public EnterTransitionCoordinator(android.app.Activity activity, android.os.ResultReceiver resultReceiver, java.util.ArrayList<java.lang.String> sharedElementNames, boolean isReturning, boolean isCrossTask) {
        super(activity.getWindow(), sharedElementNames, android.app.EnterTransitionCoordinator.getListener(activity, isReturning && (!isCrossTask)), isReturning);
        mActivity = activity;
        mIsCrossTask = isCrossTask;
        setResultReceiver(resultReceiver);
        prepareEnter();
        android.os.Bundle resultReceiverBundle = new android.os.Bundle();
        resultReceiverBundle.putParcelable(android.app.ActivityTransitionCoordinator.KEY_REMOTE_RECEIVER, this);
        mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_SET_REMOTE_RECEIVER, resultReceiverBundle);
        final android.view.View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    if (mIsReadyForTransition) {
                        decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return mIsReadyForTransition;
                }
            });
        }
    }

    boolean isCrossTask() {
        return mIsCrossTask;
    }

    public void viewInstancesReady(java.util.ArrayList<java.lang.String> accepted, java.util.ArrayList<java.lang.String> localNames, java.util.ArrayList<android.view.View> localViews) {
        boolean remap = false;
        for (int i = 0; i < localViews.size(); i++) {
            android.view.View view = localViews.get(i);
            if ((!android.text.TextUtils.equals(view.getTransitionName(), localNames.get(i))) || (!view.isAttachedToWindow())) {
                remap = true;
                break;
            }
        }
        if (remap) {
            triggerViewsReady(mapNamedElements(accepted, localNames));
        } else {
            triggerViewsReady(mapSharedElements(accepted, localViews));
        }
    }

    public void namedViewsReady(java.util.ArrayList<java.lang.String> accepted, java.util.ArrayList<java.lang.String> localNames) {
        triggerViewsReady(mapNamedElements(accepted, localNames));
    }

    public android.transition.Transition getEnterViewsTransition() {
        return mEnterViewsTransition;
    }

    @java.lang.Override
    protected void viewsReady(android.util.ArrayMap<java.lang.String, android.view.View> sharedElements) {
        super.viewsReady(sharedElements);
        mIsReadyForTransition = true;
        hideViews(mSharedElements);
        if ((getViewsTransition() != null) && (mTransitioningViews != null)) {
            hideViews(mTransitioningViews);
        }
        if (mIsReturning) {
            sendSharedElementDestination();
        } else {
            moveSharedElementsToOverlay();
        }
        if (mSharedElementsBundle != null) {
            onTakeSharedElements();
        }
    }

    private void triggerViewsReady(final android.util.ArrayMap<java.lang.String, android.view.View> sharedElements) {
        if (mAreViewsReady) {
            return;
        }
        mAreViewsReady = true;
        final android.view.ViewGroup decor = getDecor();
        // Ensure the views have been laid out before capturing the views -- we need the epicenter.
        if ((decor == null) || (decor.isAttachedToWindow() && (sharedElements.isEmpty() || (!sharedElements.valueAt(0).isLayoutRequested())))) {
            viewsReady(sharedElements);
        } else {
            mViewsReadyListener = new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    mViewsReadyListener = null;
                    decor.getViewTreeObserver().removeOnPreDrawListener(this);
                    viewsReady(sharedElements);
                    return true;
                }
            };
            decor.getViewTreeObserver().addOnPreDrawListener(mViewsReadyListener);
            decor.invalidate();
        }
    }

    private android.util.ArrayMap<java.lang.String, android.view.View> mapNamedElements(java.util.ArrayList<java.lang.String> accepted, java.util.ArrayList<java.lang.String> localNames) {
        android.util.ArrayMap<java.lang.String, android.view.View> sharedElements = new android.util.ArrayMap<java.lang.String, android.view.View>();
        android.view.ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.findNamedViews(sharedElements);
        }
        if (accepted != null) {
            for (int i = 0; i < localNames.size(); i++) {
                java.lang.String localName = localNames.get(i);
                java.lang.String acceptedName = accepted.get(i);
                if ((localName != null) && (!localName.equals(acceptedName))) {
                    android.view.View view = sharedElements.remove(localName);
                    if (view != null) {
                        sharedElements.put(acceptedName, view);
                    }
                }
            }
        }
        return sharedElements;
    }

    private void sendSharedElementDestination() {
        boolean allReady;
        final android.view.View decorView = getDecor();
        if (allowOverlappingTransitions() && (getEnterViewsTransition() != null)) {
            allReady = false;
        } else
            if (decorView == null) {
                allReady = true;
            } else {
                allReady = !decorView.isLayoutRequested();
                if (allReady) {
                    for (int i = 0; i < mSharedElements.size(); i++) {
                        if (mSharedElements.get(i).isLayoutRequested()) {
                            allReady = false;
                            break;
                        }
                    }
                }
            }

        if (allReady) {
            android.os.Bundle state = captureSharedElementState();
            moveSharedElementsToOverlay();
            mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_SHARED_ELEMENT_DESTINATION, state);
        } else
            if (decorView != null) {
                decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                    @java.lang.Override
                    public boolean onPreDraw() {
                        decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mResultReceiver != null) {
                            android.os.Bundle state = captureSharedElementState();
                            moveSharedElementsToOverlay();
                            mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_SHARED_ELEMENT_DESTINATION, state);
                        }
                        return true;
                    }
                });
            }

        if (allowOverlappingTransitions()) {
            startEnterTransitionOnly();
        }
    }

    private static android.app.SharedElementCallback getListener(android.app.Activity activity, boolean isReturning) {
        return isReturning ? activity.mExitTransitionListener : activity.mEnterTransitionListener;
    }

    @java.lang.Override
    protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
        switch (resultCode) {
            case android.app.ActivityTransitionCoordinator.MSG_TAKE_SHARED_ELEMENTS :
                if (!mIsCanceled) {
                    mSharedElementsBundle = resultData;
                    onTakeSharedElements();
                }
                break;
            case android.app.ActivityTransitionCoordinator.MSG_EXIT_TRANSITION_COMPLETE :
                if (!mIsCanceled) {
                    mIsExitTransitionComplete = true;
                    if (mSharedElementTransitionStarted) {
                        onRemoteExitTransitionComplete();
                    }
                }
                break;
            case android.app.ActivityTransitionCoordinator.MSG_CANCEL :
                cancel();
                break;
        }
    }

    public boolean isWaitingForRemoteExit() {
        return mIsReturning && (mResultReceiver != null);
    }

    /**
     * This is called onResume. If an Activity is resuming and the transitions
     * haven't started yet, force the views to appear. This is likely to be
     * caused by the top Activity finishing before the transitions started.
     * In that case, we can finish any transition that was started, but we
     * should cancel any pending transition and just bring those Views visible.
     */
    public void forceViewsToAppear() {
        if (!mIsReturning) {
            return;
        }
        if (!mIsReadyForTransition) {
            mIsReadyForTransition = true;
            final android.view.ViewGroup decor = getDecor();
            if ((decor != null) && (mViewsReadyListener != null)) {
                decor.getViewTreeObserver().removeOnPreDrawListener(mViewsReadyListener);
                mViewsReadyListener = null;
            }
            showViews(mTransitioningViews, true);
            setTransitioningViewsVisiblity(android.view.View.VISIBLE, true);
            mSharedElements.clear();
            mAllSharedElementNames.clear();
            mTransitioningViews.clear();
            mIsReadyForTransition = true;
            viewsTransitionComplete();
            sharedElementTransitionComplete();
        } else {
            if (!mSharedElementTransitionStarted) {
                moveSharedElementsFromOverlay();
                mSharedElementTransitionStarted = true;
                showViews(mSharedElements, true);
                mSharedElements.clear();
                sharedElementTransitionComplete();
            }
            if (!mIsViewsTransitionStarted) {
                mIsViewsTransitionStarted = true;
                showViews(mTransitioningViews, true);
                setTransitioningViewsVisiblity(android.view.View.VISIBLE, true);
                mTransitioningViews.clear();
                viewsTransitionComplete();
            }
            cancelPendingTransitions();
        }
        mAreViewsReady = true;
        if (mResultReceiver != null) {
            mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_CANCEL, null);
            mResultReceiver = null;
        }
    }

    private void cancel() {
        if (!mIsCanceled) {
            mIsCanceled = true;
            if ((getViewsTransition() == null) || mIsViewsTransitionStarted) {
                showViews(mSharedElements, true);
            } else
                if (mTransitioningViews != null) {
                    mTransitioningViews.addAll(mSharedElements);
                }

            moveSharedElementsFromOverlay();
            mSharedElementNames.clear();
            mSharedElements.clear();
            mAllSharedElementNames.clear();
            startSharedElementTransition(null);
            onRemoteExitTransitionComplete();
        }
    }

    public boolean isReturning() {
        return mIsReturning;
    }

    protected void prepareEnter() {
        android.view.ViewGroup decorView = getDecor();
        if ((mActivity == null) || (decorView == null)) {
            return;
        }
        if (!isCrossTask()) {
            mActivity.overridePendingTransition(0, 0);
        }
        if (!mIsReturning) {
            mWasOpaque = mActivity.convertToTranslucent(null, null);
            android.graphics.drawable.Drawable background = decorView.getBackground();
            if (background != null) {
                getWindow().setBackgroundDrawable(null);
                background = background.mutate();
                background.setAlpha(0);
                getWindow().setBackgroundDrawable(background);
            }
        } else {
            mActivity = null;// all done with it now.

        }
    }

    @java.lang.Override
    protected android.transition.Transition getViewsTransition() {
        android.view.Window window = getWindow();
        if (window == null) {
            return null;
        }
        if (mIsReturning) {
            return window.getReenterTransition();
        } else {
            return window.getEnterTransition();
        }
    }

    protected android.transition.Transition getSharedElementTransition() {
        android.view.Window window = getWindow();
        if (window == null) {
            return null;
        }
        if (mIsReturning) {
            return window.getSharedElementReenterTransition();
        } else {
            return window.getSharedElementEnterTransition();
        }
    }

    private void startSharedElementTransition(android.os.Bundle sharedElementState) {
        android.view.ViewGroup decorView = getDecor();
        if (decorView == null) {
            return;
        }
        // Remove rejected shared elements
        java.util.ArrayList<java.lang.String> rejectedNames = new java.util.ArrayList<java.lang.String>(mAllSharedElementNames);
        rejectedNames.removeAll(mSharedElementNames);
        java.util.ArrayList<android.view.View> rejectedSnapshots = createSnapshots(sharedElementState, rejectedNames);
        if (mListener != null) {
            mListener.onRejectSharedElements(rejectedSnapshots);
        }
        android.app.EnterTransitionCoordinator.removeNullViews(rejectedSnapshots);
        startRejectedAnimations(rejectedSnapshots);
        // Now start shared element transition
        java.util.ArrayList<android.view.View> sharedElementSnapshots = createSnapshots(sharedElementState, mSharedElementNames);
        showViews(mSharedElements, true);
        scheduleSetSharedElementEnd(sharedElementSnapshots);
        java.util.ArrayList<android.app.ActivityTransitionCoordinator.SharedElementOriginalState> originalImageViewState = setSharedElementState(sharedElementState, sharedElementSnapshots);
        requestLayoutForSharedElements();
        boolean startEnterTransition = allowOverlappingTransitions() && (!mIsReturning);
        boolean startSharedElementTransition = true;
        setGhostVisibility(android.view.View.INVISIBLE);
        scheduleGhostVisibilityChange(android.view.View.INVISIBLE);
        pauseInput();
        android.transition.Transition transition = beginTransition(decorView, startEnterTransition, startSharedElementTransition);
        scheduleGhostVisibilityChange(android.view.View.VISIBLE);
        setGhostVisibility(android.view.View.VISIBLE);
        if (startEnterTransition) {
            startEnterTransition(transition);
        }
        android.app.ActivityTransitionCoordinator.setOriginalSharedElementState(mSharedElements, originalImageViewState);
        if (mResultReceiver != null) {
            // We can't trust that the view will disappear on the same frame that the shared
            // element appears here. Assure that we get at least 2 frames for double-buffering.
            decorView.postOnAnimation(new java.lang.Runnable() {
                int mAnimations;

                @java.lang.Override
                public void run() {
                    if ((mAnimations++) < android.app.EnterTransitionCoordinator.MIN_ANIMATION_FRAMES) {
                        android.view.View decorView = getDecor();
                        if (decorView != null) {
                            decorView.postOnAnimation(this);
                        }
                    } else
                        if (mResultReceiver != null) {
                            mResultReceiver.send(android.app.ActivityTransitionCoordinator.MSG_HIDE_SHARED_ELEMENTS, null);
                            mResultReceiver = null;// all done sending messages.

                        }

                }
            });
        }
    }

    private static void removeNullViews(java.util.ArrayList<android.view.View> views) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                if (views.get(i) == null) {
                    views.remove(i);
                }
            }
        }
    }

    private void onTakeSharedElements() {
        if ((!mIsReadyForTransition) || (mSharedElementsBundle == null)) {
            return;
        }
        final android.os.Bundle sharedElementState = mSharedElementsBundle;
        mSharedElementsBundle = null;
        android.app.SharedElementCallback.OnSharedElementsReadyListener listener = new android.app.SharedElementCallback.OnSharedElementsReadyListener() {
            @java.lang.Override
            public void onSharedElementsReady() {
                final android.view.View decorView = getDecor();
                if (decorView != null) {
                    decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                        @java.lang.Override
                        public boolean onPreDraw() {
                            decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                            startTransition(new java.lang.Runnable() {
                                @java.lang.Override
                                public void run() {
                                    startSharedElementTransition(sharedElementState);
                                }
                            });
                            return false;
                        }
                    });
                    decorView.invalidate();
                }
            }
        };
        if (mListener == null) {
            listener.onSharedElementsReady();
        } else {
            mListener.onSharedElementsArrived(mSharedElementNames, mSharedElements, listener);
        }
    }

    private void requestLayoutForSharedElements() {
        int numSharedElements = mSharedElements.size();
        for (int i = 0; i < numSharedElements; i++) {
            mSharedElements.get(i).requestLayout();
        }
    }

    private android.transition.Transition beginTransition(android.view.ViewGroup decorView, boolean startEnterTransition, boolean startSharedElementTransition) {
        android.transition.Transition sharedElementTransition = null;
        if (startSharedElementTransition) {
            if (!mSharedElementNames.isEmpty()) {
                sharedElementTransition = configureTransition(getSharedElementTransition(), false);
            }
            if (sharedElementTransition == null) {
                sharedElementTransitionStarted();
                sharedElementTransitionComplete();
            } else {
                sharedElementTransition.addListener(new android.transition.Transition.TransitionListenerAdapter() {
                    @java.lang.Override
                    public void onTransitionStart(android.transition.Transition transition) {
                        sharedElementTransitionStarted();
                    }

                    @java.lang.Override
                    public void onTransitionEnd(android.transition.Transition transition) {
                        transition.removeListener(this);
                        sharedElementTransitionComplete();
                    }
                });
            }
        }
        android.transition.Transition viewsTransition = null;
        if (startEnterTransition) {
            mIsViewsTransitionStarted = true;
            if ((mTransitioningViews != null) && (!mTransitioningViews.isEmpty())) {
                viewsTransition = configureTransition(getViewsTransition(), true);
                if ((viewsTransition != null) && (!mIsReturning)) {
                    stripOffscreenViews();
                }
            }
            if (viewsTransition == null) {
                viewsTransitionComplete();
            } else {
                final java.util.ArrayList<android.view.View> transitioningViews = mTransitioningViews;
                viewsTransition.addListener(new android.app.ActivityTransitionCoordinator.ContinueTransitionListener() {
                    @java.lang.Override
                    public void onTransitionStart(android.transition.Transition transition) {
                        mEnterViewsTransition = transition;
                        if (transitioningViews != null) {
                            showViews(transitioningViews, false);
                        }
                        super.onTransitionStart(transition);
                    }

                    @java.lang.Override
                    public void onTransitionEnd(android.transition.Transition transition) {
                        mEnterViewsTransition = null;
                        transition.removeListener(this);
                        viewsTransitionComplete();
                        super.onTransitionEnd(transition);
                    }
                });
            }
        }
        android.transition.Transition transition = android.app.ActivityTransitionCoordinator.mergeTransitions(sharedElementTransition, viewsTransition);
        if (transition != null) {
            transition.addListener(new android.app.ActivityTransitionCoordinator.ContinueTransitionListener());
            if (startEnterTransition) {
                setTransitioningViewsVisiblity(android.view.View.INVISIBLE, false);
            }
            android.transition.TransitionManager.beginDelayedTransition(decorView, transition);
            if (startEnterTransition) {
                setTransitioningViewsVisiblity(android.view.View.VISIBLE, false);
            }
            decorView.invalidate();
        } else {
            transitionStarted();
        }
        return transition;
    }

    @java.lang.Override
    protected void onTransitionsComplete() {
        moveSharedElementsFromOverlay();
        final android.view.ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    private void sharedElementTransitionStarted() {
        mSharedElementTransitionStarted = true;
        if (mIsExitTransitionComplete) {
            send(android.app.ActivityTransitionCoordinator.MSG_EXIT_TRANSITION_COMPLETE, null);
        }
    }

    private void startEnterTransition(android.transition.Transition transition) {
        android.view.ViewGroup decorView = getDecor();
        if ((!mIsReturning) && (decorView != null)) {
            android.graphics.drawable.Drawable background = decorView.getBackground();
            if (background != null) {
                background = background.mutate();
                getWindow().setBackgroundDrawable(background);
                mBackgroundAnimator = android.animation.ObjectAnimator.ofInt(background, "alpha", 255);
                mBackgroundAnimator.setDuration(getFadeDuration());
                mBackgroundAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        makeOpaque();
                    }
                });
                mBackgroundAnimator.start();
            } else
                if (transition != null) {
                    transition.addListener(new android.transition.Transition.TransitionListenerAdapter() {
                        @java.lang.Override
                        public void onTransitionEnd(android.transition.Transition transition) {
                            transition.removeListener(this);
                            makeOpaque();
                        }
                    });
                } else {
                    makeOpaque();
                }

        }
    }

    public void stop() {
        // Restore the background to its previous state since the
        // Activity is stopping.
        if (mBackgroundAnimator != null) {
            mBackgroundAnimator.end();
            mBackgroundAnimator = null;
        } else
            if (mWasOpaque) {
                android.view.ViewGroup decorView = getDecor();
                if (decorView != null) {
                    android.graphics.drawable.Drawable drawable = decorView.getBackground();
                    if (drawable != null) {
                        drawable.setAlpha(1);
                    }
                }
            }

        makeOpaque();
        mIsCanceled = true;
        mResultReceiver = null;
        mActivity = null;
        moveSharedElementsFromOverlay();
        if (mTransitioningViews != null) {
            showViews(mTransitioningViews, true);
            setTransitioningViewsVisiblity(android.view.View.VISIBLE, true);
        }
        showViews(mSharedElements, true);
        clearState();
    }

    /**
     * Cancels the enter transition.
     *
     * @return True if the enter transition is still pending capturing the target state. If so,
    any transition started on the decor will do nothing.
     */
    public boolean cancelEnter() {
        setGhostVisibility(android.view.View.INVISIBLE);
        mHasStopped = true;
        mIsCanceled = true;
        clearState();
        return super.cancelPendingTransitions();
    }

    @java.lang.Override
    protected void clearState() {
        mSharedElementsBundle = null;
        mEnterViewsTransition = null;
        mResultReceiver = null;
        if (mBackgroundAnimator != null) {
            mBackgroundAnimator.cancel();
            mBackgroundAnimator = null;
        }
        super.clearState();
    }

    private void makeOpaque() {
        if ((!mHasStopped) && (mActivity != null)) {
            if (mWasOpaque) {
                mActivity.convertFromTranslucent();
            }
            mActivity = null;
        }
    }

    private boolean allowOverlappingTransitions() {
        return mIsReturning ? getWindow().getAllowReturnTransitionOverlap() : getWindow().getAllowEnterTransitionOverlap();
    }

    private void startRejectedAnimations(final java.util.ArrayList<android.view.View> rejectedSnapshots) {
        if ((rejectedSnapshots == null) || rejectedSnapshots.isEmpty()) {
            return;
        }
        final android.view.ViewGroup decorView = getDecor();
        if (decorView != null) {
            android.view.ViewGroupOverlay overlay = decorView.getOverlay();
            android.animation.ObjectAnimator animator = null;
            int numRejected = rejectedSnapshots.size();
            for (int i = 0; i < numRejected; i++) {
                android.view.View snapshot = rejectedSnapshots.get(i);
                overlay.add(snapshot);
                animator = android.animation.ObjectAnimator.ofFloat(snapshot, android.view.View.ALPHA, 1, 0);
                animator.start();
            }
            animator.addListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    android.view.ViewGroupOverlay overlay = decorView.getOverlay();
                    int numRejected = rejectedSnapshots.size();
                    for (int i = 0; i < numRejected; i++) {
                        overlay.remove(rejectedSnapshots.get(i));
                    }
                }
            });
        }
    }

    protected void onRemoteExitTransitionComplete() {
        if (!allowOverlappingTransitions()) {
            startEnterTransitionOnly();
        }
    }

    private void startEnterTransitionOnly() {
        startTransition(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                boolean startEnterTransition = true;
                boolean startSharedElementTransition = false;
                android.view.ViewGroup decorView = getDecor();
                if (decorView != null) {
                    android.transition.Transition transition = beginTransition(decorView, startEnterTransition, startSharedElementTransition);
                    startEnterTransition(transition);
                }
            }
        });
    }
}

