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


final class DetailsOverviewSharedElementHelper extends android.support.v4.app.SharedElementCallback {
    static final java.lang.String TAG = "DetailsTransitionHelper";

    static final boolean DEBUG = false;

    static class TransitionTimeOutRunnable implements java.lang.Runnable {
        java.lang.ref.WeakReference<android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper> mHelperRef;

        TransitionTimeOutRunnable(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper helper) {
            mHelperRef = new java.lang.ref.WeakReference<android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper>(helper);
        }

        @java.lang.Override
        public void run() {
            android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper helper = mHelperRef.get();
            if (helper == null) {
                return;
            }
            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "timeout " + helper.mActivityToRunTransition);
            }
            helper.startPostponedEnterTransition();
        }
    }

    android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder mViewHolder;

    android.app.Activity mActivityToRunTransition;

    boolean mStartedPostpone;

    java.lang.String mSharedElementName;

    int mRightPanelWidth;

    int mRightPanelHeight;

    private android.widget.ImageView.ScaleType mSavedScaleType;

    private android.graphics.Matrix mSavedMatrix;

    private boolean hasImageViewScaleChange(android.view.View snapshotView) {
        return snapshotView instanceof android.widget.ImageView;
    }

    private void saveImageViewScale() {
        if (mSavedScaleType == null) {
            // only save first time after initialize/restoreImageViewScale()
            android.widget.ImageView imageView = mViewHolder.mImageView;
            mSavedScaleType = imageView.getScaleType();
            mSavedMatrix = (mSavedScaleType == android.widget.ImageView.ScaleType.MATRIX) ? imageView.getMatrix() : null;
            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "saveImageViewScale: " + mSavedScaleType);
            }
        }
    }

    private static void updateImageViewAfterScaleTypeChange(android.widget.ImageView imageView) {
        // enforcing imageView to update its internal bounds/matrix immediately
        imageView.measure(android.view.View.MeasureSpec.makeMeasureSpec(imageView.getMeasuredWidth(), android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(imageView.getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY));
        imageView.layout(imageView.getLeft(), imageView.getTop(), imageView.getRight(), imageView.getBottom());
    }

    private void changeImageViewScale(android.view.View snapshotView) {
        android.widget.ImageView snapshotImageView = ((android.widget.ImageView) (snapshotView));
        android.widget.ImageView imageView = mViewHolder.mImageView;
        if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "changeImageViewScale to " + snapshotImageView.getScaleType());
        }
        imageView.setScaleType(snapshotImageView.getScaleType());
        if (snapshotImageView.getScaleType() == android.widget.ImageView.ScaleType.MATRIX) {
            imageView.setImageMatrix(snapshotImageView.getImageMatrix());
        }
        android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.updateImageViewAfterScaleTypeChange(imageView);
    }

    private void restoreImageViewScale() {
        if (mSavedScaleType != null) {
            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "restoreImageViewScale to " + mSavedScaleType);
            }
            android.widget.ImageView imageView = mViewHolder.mImageView;
            imageView.setScaleType(mSavedScaleType);
            if (mSavedScaleType == android.widget.ImageView.ScaleType.MATRIX) {
                imageView.setImageMatrix(mSavedMatrix);
            }
            // only restore once unless another save happens
            mSavedScaleType = null;
            android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.updateImageViewAfterScaleTypeChange(imageView);
        }
    }

    @java.lang.Override
    public void onSharedElementStart(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
        if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "onSharedElementStart " + mActivityToRunTransition);
        }
        if (sharedElements.size() < 1) {
            return;
        }
        android.view.View overviewView = sharedElements.get(0);
        if ((mViewHolder == null) || (mViewHolder.mOverviewFrame != overviewView)) {
            return;
        }
        android.view.View snapshot = sharedElementSnapshots.get(0);
        if (hasImageViewScaleChange(snapshot)) {
            saveImageViewScale();
            changeImageViewScale(snapshot);
        }
        android.view.View imageView = mViewHolder.mImageView;
        final int width = overviewView.getWidth();
        final int height = overviewView.getHeight();
        imageView.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY));
        imageView.layout(0, 0, width, height);
        final android.view.View rightPanel = mViewHolder.mRightPanel;
        if ((mRightPanelWidth != 0) && (mRightPanelHeight != 0)) {
            rightPanel.measure(android.view.View.MeasureSpec.makeMeasureSpec(mRightPanelWidth, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(mRightPanelHeight, android.view.View.MeasureSpec.EXACTLY));
            rightPanel.layout(width, rightPanel.getTop(), width + mRightPanelWidth, rightPanel.getTop() + mRightPanelHeight);
        } else {
            rightPanel.offsetLeftAndRight(width - rightPanel.getLeft());
        }
        mViewHolder.mActionsRow.setVisibility(android.view.View.INVISIBLE);
        mViewHolder.mDetailsDescriptionFrame.setVisibility(android.view.View.INVISIBLE);
    }

    @java.lang.Override
    public void onSharedElementEnd(java.util.List<java.lang.String> sharedElementNames, java.util.List<android.view.View> sharedElements, java.util.List<android.view.View> sharedElementSnapshots) {
        if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "onSharedElementEnd " + mActivityToRunTransition);
        }
        if (sharedElements.size() < 1) {
            return;
        }
        android.view.View overviewView = sharedElements.get(0);
        if ((mViewHolder == null) || (mViewHolder.mOverviewFrame != overviewView)) {
            return;
        }
        restoreImageViewScale();
        // temporary let action row take focus so we defer button background animation
        mViewHolder.mActionsRow.setDescendantFocusability(android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mViewHolder.mActionsRow.setVisibility(android.view.View.VISIBLE);
        mViewHolder.mActionsRow.setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mViewHolder.mDetailsDescriptionFrame.setVisibility(android.view.View.VISIBLE);
    }

    void setSharedElementEnterTransition(android.app.Activity activity, java.lang.String sharedElementName, long timeoutMs) {
        if (((activity == null) && (!android.text.TextUtils.isEmpty(sharedElementName))) || ((activity != null) && android.text.TextUtils.isEmpty(sharedElementName))) {
            throw new java.lang.IllegalArgumentException();
        }
        if ((activity == mActivityToRunTransition) && android.text.TextUtils.equals(sharedElementName, mSharedElementName)) {
            return;
        }
        if (mActivityToRunTransition != null) {
            android.support.v4.app.ActivityCompat.setEnterSharedElementCallback(mActivityToRunTransition, null);
        }
        mActivityToRunTransition = activity;
        mSharedElementName = sharedElementName;
        if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "postponeEnterTransition " + mActivityToRunTransition);
        }
        android.support.v4.app.ActivityCompat.setEnterSharedElementCallback(mActivityToRunTransition, this);
        android.support.v4.app.ActivityCompat.postponeEnterTransition(mActivityToRunTransition);
        if (timeoutMs > 0) {
            new android.os.Handler().postDelayed(new android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TransitionTimeOutRunnable(this), timeoutMs);
        }
    }

    void onBindToDrawable(android.support.v17.leanback.widget.DetailsOverviewRowPresenter.ViewHolder vh) {
        if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
            android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "onBindToDrawable, could start transition of " + mActivityToRunTransition);
        }
        if (mViewHolder != null) {
            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "rebind? clear transitionName on current viewHolder " + mViewHolder.mOverviewFrame);
            }
            android.support.v4.view.ViewCompat.setTransitionName(mViewHolder.mOverviewFrame, null);
        }
        // After we got a image drawable,  we can determine size of right panel.
        // We want right panel to have fixed size so that the right panel don't change size
        // when the overview is layout as a small bounds in transition.
        mViewHolder = vh;
        mViewHolder.mRightPanel.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
            @java.lang.Override
            public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mViewHolder.mRightPanel.removeOnLayoutChangeListener(this);
                mRightPanelWidth = mViewHolder.mRightPanel.getWidth();
                mRightPanelHeight = mViewHolder.mRightPanel.getHeight();
                if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                    android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, (("onLayoutChange records size of right panel as " + mRightPanelWidth) + ", ") + mRightPanelHeight);
                }
            }
        });
        mViewHolder.mRightPanel.postOnAnimation(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                    android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "setTransitionName " + mViewHolder.mOverviewFrame);
                }
                android.support.v4.view.ViewCompat.setTransitionName(mViewHolder.mOverviewFrame, mSharedElementName);
                java.lang.Object transition = android.support.v17.leanback.transition.TransitionHelper.getSharedElementEnterTransition(mActivityToRunTransition.getWindow());
                if (transition != null) {
                    android.support.v17.leanback.transition.TransitionHelper.addTransitionListener(transition, new android.support.v17.leanback.transition.TransitionListener() {
                        @java.lang.Override
                        public void onTransitionEnd(java.lang.Object transition) {
                            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "onTransitionEnd " + mActivityToRunTransition);
                            }
                            // after transition if the action row still focused, transfer
                            // focus to its children
                            if (mViewHolder.mActionsRow.isFocused()) {
                                mViewHolder.mActionsRow.requestFocus();
                            }
                            android.support.v17.leanback.transition.TransitionHelper.removeTransitionListener(transition, this);
                        }
                    });
                }
                startPostponedEnterTransition();
            }
        });
    }

    void startPostponedEnterTransition() {
        if (!mStartedPostpone) {
            if (android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.DEBUG) {
                android.util.Log.d(android.support.v17.leanback.widget.DetailsOverviewSharedElementHelper.TAG, "startPostponedEnterTransition " + mActivityToRunTransition);
            }
            android.support.v4.app.ActivityCompat.startPostponedEnterTransition(mActivityToRunTransition);
            mStartedPostpone = true;
        }
    }
}

