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
 * limitations under the License
 */
package android.support.v17.leanback.widget;


/**
 * Helper for managing {@link android.support.v17.leanback.widget.TitleView}, including
 * transitions and focus movement.
 * Assumes the TitleView is overlayed on the topmost portion of the scene root view.
 */
public class TitleHelper {
    android.view.ViewGroup mSceneRoot;

    android.view.View mTitleView;

    private java.lang.Object mTitleUpTransition;

    private java.lang.Object mTitleDownTransition;

    private java.lang.Object mSceneWithTitle;

    private java.lang.Object mSceneWithoutTitle;

    // When moving focus off the TitleView, this focus search listener assumes that the view that
    // should take focus comes before the TitleView in a focus search starting at the scene root.
    private final android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener = new android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener() {
        @java.lang.Override
        public android.view.View onFocusSearch(android.view.View focused, int direction) {
            if ((focused != mTitleView) && (direction == android.view.View.FOCUS_UP)) {
                return mTitleView;
            }
            final boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(focused) == android.view.View.LAYOUT_DIRECTION_RTL;
            final int forward = (isRtl) ? android.view.View.FOCUS_LEFT : android.view.View.FOCUS_RIGHT;
            if (mTitleView.hasFocus() && ((direction == android.view.View.FOCUS_DOWN) || (direction == forward))) {
                return mSceneRoot;
            }
            return null;
        }
    };

    public TitleHelper(android.view.ViewGroup sceneRoot, android.view.View titleView) {
        if ((sceneRoot == null) || (titleView == null)) {
            throw new java.lang.IllegalArgumentException("Views may not be null");
        }
        mSceneRoot = sceneRoot;
        mTitleView = titleView;
        createTransitions();
    }

    private void createTransitions() {
        mTitleUpTransition = android.support.v17.leanback.transition.LeanbackTransitionHelper.loadTitleOutTransition(mSceneRoot.getContext());
        mTitleDownTransition = android.support.v17.leanback.transition.LeanbackTransitionHelper.loadTitleInTransition(mSceneRoot.getContext());
        mSceneWithTitle = android.support.v17.leanback.transition.TransitionHelper.createScene(mSceneRoot, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mTitleView.setVisibility(android.view.View.VISIBLE);
            }
        });
        mSceneWithoutTitle = android.support.v17.leanback.transition.TransitionHelper.createScene(mSceneRoot, new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mTitleView.setVisibility(android.view.View.INVISIBLE);
            }
        });
    }

    /**
     * Shows the title.
     */
    public void showTitle(boolean show) {
        if (show) {
            android.support.v17.leanback.transition.TransitionHelper.runTransition(mSceneWithTitle, mTitleDownTransition);
        } else {
            android.support.v17.leanback.transition.TransitionHelper.runTransition(mSceneWithoutTitle, mTitleUpTransition);
        }
    }

    /**
     * Returns the scene root ViewGroup.
     */
    public android.view.ViewGroup getSceneRoot() {
        return mSceneRoot;
    }

    /**
     * Returns the {@link TitleView}
     */
    public android.view.View getTitleView() {
        return mTitleView;
    }

    /**
     * Returns a
     * {@link android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener} which
     * may be used to manage focus switching between the title view and scene root.
     */
    public android.support.v17.leanback.widget.BrowseFrameLayout.OnFocusSearchListener getOnFocusSearchListener() {
        return mOnFocusSearchListener;
    }
}

