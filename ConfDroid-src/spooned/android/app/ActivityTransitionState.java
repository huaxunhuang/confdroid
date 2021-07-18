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
 * This class contains all persistence-related functionality for Activity Transitions.
 * Activities start exit and enter Activity Transitions through this class.
 */
class ActivityTransitionState {
    private static final java.lang.String ENTERING_SHARED_ELEMENTS = "android:enteringSharedElements";

    private static final java.lang.String EXITING_MAPPED_FROM = "android:exitingMappedFrom";

    private static final java.lang.String EXITING_MAPPED_TO = "android:exitingMappedTo";

    /**
     * The shared elements that the calling Activity has said that they transferred to this
     * Activity.
     */
    private java.util.ArrayList<java.lang.String> mEnteringNames;

    /**
     * The names of shared elements that were shared to the called Activity.
     */
    private java.util.ArrayList<java.lang.String> mExitingFrom;

    /**
     * The names of local Views that were shared out, mapped to those elements in mExitingFrom.
     */
    private java.util.ArrayList<java.lang.String> mExitingTo;

    /**
     * The local Views that were shared out, mapped to those elements in mExitingFrom.
     */
    private java.util.ArrayList<android.view.View> mExitingToView;

    /**
     * The ExitTransitionCoordinator used to start an Activity. Used to make the elements restore
     * Visibility of exited Views.
     */
    private android.app.ExitTransitionCoordinator mCalledExitCoordinator;

    /**
     * The ExitTransitionCoordinator used to return to a previous Activity when called with
     * {@link android.app.Activity#finishAfterTransition()}.
     */
    private android.app.ExitTransitionCoordinator mReturnExitCoordinator;

    /**
     * We must be able to cancel entering transitions to stop changing the Window to
     * opaque when we exit before making the Window opaque.
     */
    private android.app.EnterTransitionCoordinator mEnterTransitionCoordinator;

    /**
     * ActivityOptions used on entering this Activity.
     */
    private android.app.ActivityOptions mEnterActivityOptions;

    /**
     * Has an exit transition been started? If so, we don't want to double-exit.
     */
    private boolean mHasExited;

    /**
     * Postpone painting and starting the enter transition until this is false.
     */
    private boolean mIsEnterPostponed;

    /**
     * Potential exit transition coordinators.
     */
    private android.util.SparseArray<java.lang.ref.WeakReference<android.app.ExitTransitionCoordinator>> mExitTransitionCoordinators;

    /**
     * Next key for mExitTransitionCoordinator.
     */
    private int mExitTransitionCoordinatorsKey = 1;

    private boolean mIsEnterTriggered;

    public ActivityTransitionState() {
    }

    public int addExitTransitionCoordinator(android.app.ExitTransitionCoordinator exitTransitionCoordinator) {
        if (mExitTransitionCoordinators == null) {
            mExitTransitionCoordinators = new android.util.SparseArray<java.lang.ref.WeakReference<android.app.ExitTransitionCoordinator>>();
        }
        java.lang.ref.WeakReference<android.app.ExitTransitionCoordinator> ref = new java.lang.ref.WeakReference(exitTransitionCoordinator);
        // clean up old references:
        for (int i = mExitTransitionCoordinators.size() - 1; i >= 0; i--) {
            java.lang.ref.WeakReference<android.app.ExitTransitionCoordinator> oldRef = mExitTransitionCoordinators.valueAt(i);
            if (oldRef.get() == null) {
                mExitTransitionCoordinators.removeAt(i);
            }
        }
        int newKey = mExitTransitionCoordinatorsKey++;
        mExitTransitionCoordinators.append(newKey, ref);
        return newKey;
    }

    public void readState(android.os.Bundle bundle) {
        if (bundle != null) {
            if ((mEnterTransitionCoordinator == null) || mEnterTransitionCoordinator.isReturning()) {
                mEnteringNames = bundle.getStringArrayList(android.app.ActivityTransitionState.ENTERING_SHARED_ELEMENTS);
            }
            if (mEnterTransitionCoordinator == null) {
                mExitingFrom = bundle.getStringArrayList(android.app.ActivityTransitionState.EXITING_MAPPED_FROM);
                mExitingTo = bundle.getStringArrayList(android.app.ActivityTransitionState.EXITING_MAPPED_TO);
            }
        }
    }

    public void saveState(android.os.Bundle bundle) {
        if (mEnteringNames != null) {
            bundle.putStringArrayList(android.app.ActivityTransitionState.ENTERING_SHARED_ELEMENTS, mEnteringNames);
        }
        if (mExitingFrom != null) {
            bundle.putStringArrayList(android.app.ActivityTransitionState.EXITING_MAPPED_FROM, mExitingFrom);
            bundle.putStringArrayList(android.app.ActivityTransitionState.EXITING_MAPPED_TO, mExitingTo);
        }
    }

    public void setEnterActivityOptions(android.app.Activity activity, android.app.ActivityOptions options) {
        final android.view.Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        // ensure Decor View has been created so that the window features are activated
        window.getDecorView();
        if ((((window.hasFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS) && (options != null)) && (mEnterActivityOptions == null)) && (mEnterTransitionCoordinator == null)) && (options.getAnimationType() == android.app.ActivityOptions.ANIM_SCENE_TRANSITION)) {
            mEnterActivityOptions = options;
            mIsEnterTriggered = false;
            if (mEnterActivityOptions.isReturning()) {
                restoreExitedViews();
                int result = mEnterActivityOptions.getResultCode();
                if (result != 0) {
                    activity.onActivityReenter(result, mEnterActivityOptions.getResultData());
                }
            }
        }
    }

    public void enterReady(android.app.Activity activity) {
        if ((mEnterActivityOptions == null) || mIsEnterTriggered) {
            return;
        }
        mIsEnterTriggered = true;
        mHasExited = false;
        java.util.ArrayList<java.lang.String> sharedElementNames = mEnterActivityOptions.getSharedElementNames();
        android.os.ResultReceiver resultReceiver = mEnterActivityOptions.getResultReceiver();
        if (mEnterActivityOptions.isReturning()) {
            restoreExitedViews();
            activity.getWindow().getDecorView().setVisibility(android.view.View.VISIBLE);
        }
        mEnterTransitionCoordinator = new android.app.EnterTransitionCoordinator(activity, resultReceiver, sharedElementNames, mEnterActivityOptions.isReturning(), mEnterActivityOptions.isCrossTask());
        if (mEnterActivityOptions.isCrossTask()) {
            mExitingFrom = new java.util.ArrayList<>(mEnterActivityOptions.getSharedElementNames());
            mExitingTo = new java.util.ArrayList<>(mEnterActivityOptions.getSharedElementNames());
        }
        if (!mIsEnterPostponed) {
            startEnter();
        }
    }

    public void postponeEnterTransition() {
        mIsEnterPostponed = true;
    }

    public void startPostponedEnterTransition() {
        if (mIsEnterPostponed) {
            mIsEnterPostponed = false;
            if (mEnterTransitionCoordinator != null) {
                startEnter();
            }
        }
    }

    private void startEnter() {
        if (mEnterTransitionCoordinator.isReturning()) {
            if (mExitingToView != null) {
                mEnterTransitionCoordinator.viewInstancesReady(mExitingFrom, mExitingTo, mExitingToView);
            } else {
                mEnterTransitionCoordinator.namedViewsReady(mExitingFrom, mExitingTo);
            }
        } else {
            mEnterTransitionCoordinator.namedViewsReady(null, null);
            mEnteringNames = mEnterTransitionCoordinator.getAllSharedElementNames();
        }
        mExitingFrom = null;
        mExitingTo = null;
        mExitingToView = null;
        mEnterActivityOptions = null;
    }

    public void onStop() {
        restoreExitedViews();
        if (mEnterTransitionCoordinator != null) {
            mEnterTransitionCoordinator.stop();
            mEnterTransitionCoordinator = null;
        }
        if (mReturnExitCoordinator != null) {
            mReturnExitCoordinator.stop();
            mReturnExitCoordinator = null;
        }
    }

    public void onResume(android.app.Activity activity, boolean isTopOfTask) {
        // After orientation change, the onResume can come in before the top Activity has
        // left, so if the Activity is not top, wait a second for the top Activity to exit.
        if (isTopOfTask || (mEnterTransitionCoordinator == null)) {
            restoreExitedViews();
            restoreReenteringViews();
        } else {
            activity.mHandler.postDelayed(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if ((mEnterTransitionCoordinator == null) || mEnterTransitionCoordinator.isWaitingForRemoteExit()) {
                        restoreExitedViews();
                        restoreReenteringViews();
                    }
                }
            }, 1000);
        }
    }

    public void clear() {
        mEnteringNames = null;
        mExitingFrom = null;
        mExitingTo = null;
        mExitingToView = null;
        mCalledExitCoordinator = null;
        mEnterTransitionCoordinator = null;
        mEnterActivityOptions = null;
        mExitTransitionCoordinators = null;
    }

    private void restoreExitedViews() {
        if (mCalledExitCoordinator != null) {
            mCalledExitCoordinator.resetViews();
            mCalledExitCoordinator = null;
        }
    }

    private void restoreReenteringViews() {
        if (((mEnterTransitionCoordinator != null) && mEnterTransitionCoordinator.isReturning()) && (!mEnterTransitionCoordinator.isCrossTask())) {
            mEnterTransitionCoordinator.forceViewsToAppear();
            mExitingFrom = null;
            mExitingTo = null;
            mExitingToView = null;
        }
    }

    public boolean startExitBackTransition(final android.app.Activity activity) {
        if ((mEnteringNames == null) || (mCalledExitCoordinator != null)) {
            return false;
        } else {
            if (!mHasExited) {
                mHasExited = true;
                android.transition.Transition enterViewsTransition = null;
                android.view.ViewGroup decor = null;
                boolean delayExitBack = false;
                if (mEnterTransitionCoordinator != null) {
                    enterViewsTransition = mEnterTransitionCoordinator.getEnterViewsTransition();
                    decor = mEnterTransitionCoordinator.getDecor();
                    delayExitBack = mEnterTransitionCoordinator.cancelEnter();
                    mEnterTransitionCoordinator = null;
                    if ((enterViewsTransition != null) && (decor != null)) {
                        enterViewsTransition.pause(decor);
                    }
                }
                mReturnExitCoordinator = new android.app.ExitTransitionCoordinator(activity, activity.getWindow(), activity.mEnterTransitionListener, mEnteringNames, null, null, true);
                if ((enterViewsTransition != null) && (decor != null)) {
                    enterViewsTransition.resume(decor);
                }
                if (delayExitBack && (decor != null)) {
                    final android.view.ViewGroup finalDecor = decor;
                    decor.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                        @java.lang.Override
                        public boolean onPreDraw() {
                            finalDecor.getViewTreeObserver().removeOnPreDrawListener(this);
                            if (mReturnExitCoordinator != null) {
                                mReturnExitCoordinator.startExit(activity.mResultCode, activity.mResultData);
                            }
                            return true;
                        }
                    });
                } else {
                    mReturnExitCoordinator.startExit(activity.mResultCode, activity.mResultData);
                }
            }
            return true;
        }
    }

    public void startExitOutTransition(android.app.Activity activity, android.os.Bundle options) {
        mEnterTransitionCoordinator = null;
        if ((!activity.getWindow().hasFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS)) || (mExitTransitionCoordinators == null)) {
            return;
        }
        android.app.ActivityOptions activityOptions = new android.app.ActivityOptions(options);
        if (activityOptions.getAnimationType() == android.app.ActivityOptions.ANIM_SCENE_TRANSITION) {
            int key = activityOptions.getExitCoordinatorKey();
            int index = mExitTransitionCoordinators.indexOfKey(key);
            if (index >= 0) {
                mCalledExitCoordinator = mExitTransitionCoordinators.valueAt(index).get();
                mExitTransitionCoordinators.removeAt(index);
                if (mCalledExitCoordinator != null) {
                    mExitingFrom = mCalledExitCoordinator.getAcceptedNames();
                    mExitingTo = mCalledExitCoordinator.getMappedNames();
                    mExitingToView = mCalledExitCoordinator.copyMappedViews();
                    mCalledExitCoordinator.startExit();
                }
            }
        }
    }
}

