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
package android.support.v17.leanback.widget;


/**
 * Helper class to assist delayed shared element activity transition for view created by
 * {@link FullWidthDetailsOverviewRowPresenter}. User must call
 * {@link #setSharedElementEnterTransition(Activity, String, long)} during activity onCreate() and
 * call {@link FullWidthDetailsOverviewRowPresenter#setListener(FullWidthDetailsOverviewRowPresenter.Listener)}.
 * The helper implements {@link FullWidthDetailsOverviewRowPresenter.Listener} and starts delayed
 * activity transition once {@link FullWidthDetailsOverviewRowPresenter.Listener#onBindLogo(ViewHolder)}
 * is called.
 */
public class FullWidthDetailsOverviewSharedElementHelper extends android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.Listener {
    static final java.lang.String TAG = "DetailsTransitionHelper";

    static final boolean DEBUG = false;

    private static final long DEFAULT_TIMEOUT = 5000;

    static class TransitionTimeOutRunnable implements java.lang.Runnable {
        java.lang.ref.WeakReference<android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper> mHelperRef;

        TransitionTimeOutRunnable(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper helper) {
            mHelperRef = new java.lang.ref.WeakReference<android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper>(helper);
        }

        @java.lang.Override
        public void run() {
            android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper helper = mHelperRef.get();
            if (helper == null) {
                return;
            }
            if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "timeout " + helper.mActivityToRunTransition);
            }
            helper.startPostponedEnterTransition();
        }
    }

    android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder mViewHolder;

    android.app.Activity mActivityToRunTransition;

    private boolean mStartedPostpone;

    java.lang.String mSharedElementName;

    private boolean mAutoStartSharedElementTransition = true;

    public void setSharedElementEnterTransition(android.app.Activity activity, java.lang.String sharedElementName) {
        setSharedElementEnterTransition(activity, sharedElementName, android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEFAULT_TIMEOUT);
    }

    public void setSharedElementEnterTransition(android.app.Activity activity, java.lang.String sharedElementName, long timeoutMs) {
        if (((activity == null) && (!android.text.TextUtils.isEmpty(sharedElementName))) || ((activity != null) && android.text.TextUtils.isEmpty(sharedElementName))) {
            throw new java.lang.IllegalArgumentException();
        }
        if ((activity == mActivityToRunTransition) && android.text.TextUtils.equals(sharedElementName, mSharedElementName)) {
            return;
        }
        mActivityToRunTransition = activity;
        mSharedElementName = sharedElementName;
        if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "postponeEnterTransition " + mActivityToRunTransition);
        }
        java.lang.Object transition = android.support.v17.leanback.transition.TransitionHelper.getSharedElementEnterTransition(activity.getWindow());
        setAutoStartSharedElementTransition(transition != null);
        android.support.v4.app.ActivityCompat.postponeEnterTransition(mActivityToRunTransition);
        if (timeoutMs > 0) {
            new android.os.Handler().postDelayed(new android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TransitionTimeOutRunnable(this), timeoutMs);
        }
    }

    /**
     * Enable or disable auto startPostponedEnterTransition() when bound to logo. When it's
     * disabled, app must call {@link #startPostponedEnterTransition()} to kick off
     * windowEnterTransition. By default, it is disabled when there is no
     * windowEnterSharedElementTransition set on the activity.
     */
    public void setAutoStartSharedElementTransition(boolean enabled) {
        mAutoStartSharedElementTransition = enabled;
    }

    /**
     * Returns true if auto startPostponedEnterTransition() when bound to logo. When it's
     * disabled, app must call {@link #startPostponedEnterTransition()} to kick off
     * windowEnterTransition. By default, it is disabled when there is no
     * windowEnterSharedElementTransition set on the activity.
     */
    public boolean getAutoStartSharedElementTransition() {
        return mAutoStartSharedElementTransition;
    }

    @java.lang.Override
    public void onBindLogo(android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter.ViewHolder vh) {
        if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "onBindLogo, could start transition of " + mActivityToRunTransition);
        }
        mViewHolder = vh;
        if (!mAutoStartSharedElementTransition) {
            return;
        }
        if (mViewHolder != null) {
            if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "rebind? clear transitionName on current viewHolder " + mViewHolder.getOverviewView());
            }
            android.support.v4.view.ViewCompat.setTransitionName(mViewHolder.getLogoViewHolder().view, null);
        }
        // After we got a image drawable,  we can determine size of right panel.
        // We want right panel to have fixed size so that the right panel don't change size
        // when the overview is layout as a small bounds in transition.
        mViewHolder.getDetailsDescriptionFrame().postOnAnimation(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
                    android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "setTransitionName " + mViewHolder.getOverviewView());
                }
                android.support.v4.view.ViewCompat.setTransitionName(mViewHolder.getLogoViewHolder().view, mSharedElementName);
                java.lang.Object transition = android.support.v17.leanback.transition.TransitionHelper.getSharedElementEnterTransition(mActivityToRunTransition.getWindow());
                if (transition != null) {
                    android.support.v17.leanback.transition.TransitionHelper.addTransitionListener(transition, new android.support.v17.leanback.transition.TransitionListener() {
                        @java.lang.Override
                        public void onTransitionEnd(java.lang.Object transition) {
                            if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
                                android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "onTransitionEnd " + mActivityToRunTransition);
                            }
                            // after transition if the action row still focused, transfer
                            // focus to its children
                            if (mViewHolder.getActionsRow().isFocused()) {
                                mViewHolder.getActionsRow().requestFocus();
                            }
                            android.support.v17.leanback.transition.TransitionHelper.removeTransitionListener(transition, this);
                        }
                    });
                }
                startPostponedEnterTransitionInternal();
            }
        });
    }

    /**
     * Manually start postponed enter transition.
     */
    public void startPostponedEnterTransition() {
        new android.os.Handler().post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                startPostponedEnterTransitionInternal();
            }
        });
    }

    void startPostponedEnterTransitionInternal() {
        if ((!mStartedPostpone) && (mViewHolder != null)) {
            if (android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper.TAG, "startPostponedEnterTransition " + mActivityToRunTransition);
            }
            android.support.v4.app.ActivityCompat.startPostponedEnterTransition(mActivityToRunTransition);
            mStartedPostpone = true;
        }
    }
}

