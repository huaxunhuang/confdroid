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
 * Sets up the highlighting behavior when an item gains focus.
 */
public class FocusHighlightHelper {
    static boolean isValidZoomIndex(int zoomIndex) {
        return (zoomIndex == android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_NONE) || (android.support.v17.leanback.widget.FocusHighlightHelper.getResId(zoomIndex) > 0);
    }

    static int getResId(int zoomIndex) {
        switch (zoomIndex) {
            case android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_SMALL :
                return R.fraction.lb_focus_zoom_factor_small;
            case android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_XSMALL :
                return R.fraction.lb_focus_zoom_factor_xsmall;
            case android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_MEDIUM :
                return R.fraction.lb_focus_zoom_factor_medium;
            case android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_LARGE :
                return R.fraction.lb_focus_zoom_factor_large;
            default :
                return 0;
        }
    }

    static class FocusAnimator implements android.animation.TimeAnimator.TimeListener {
        private final android.view.View mView;

        private final int mDuration;

        private final android.support.v17.leanback.widget.ShadowOverlayContainer mWrapper;

        private final float mScaleDiff;

        private float mFocusLevel = 0.0F;

        private float mFocusLevelStart;

        private float mFocusLevelDelta;

        private final android.animation.TimeAnimator mAnimator = new android.animation.TimeAnimator();

        private final android.view.animation.Interpolator mInterpolator = new android.view.animation.AccelerateDecelerateInterpolator();

        private final android.support.v17.leanback.graphics.ColorOverlayDimmer mDimmer;

        void animateFocus(boolean select, boolean immediate) {
            endAnimation();
            final float end = (select) ? 1 : 0;
            if (immediate) {
                setFocusLevel(end);
            } else
                if (mFocusLevel != end) {
                    mFocusLevelStart = mFocusLevel;
                    mFocusLevelDelta = end - mFocusLevelStart;
                    mAnimator.start();
                }

        }

        FocusAnimator(android.view.View view, float scale, boolean useDimmer, int duration) {
            mView = view;
            mDuration = duration;
            mScaleDiff = scale - 1.0F;
            if (view instanceof android.support.v17.leanback.widget.ShadowOverlayContainer) {
                mWrapper = ((android.support.v17.leanback.widget.ShadowOverlayContainer) (view));
            } else {
                mWrapper = null;
            }
            mAnimator.setTimeListener(this);
            if (useDimmer) {
                mDimmer = android.support.v17.leanback.graphics.ColorOverlayDimmer.createDefault(view.getContext());
            } else {
                mDimmer = null;
            }
        }

        void setFocusLevel(float level) {
            mFocusLevel = level;
            float scale = 1.0F + (mScaleDiff * level);
            mView.setScaleX(scale);
            mView.setScaleY(scale);
            if (mWrapper != null) {
                mWrapper.setShadowFocusLevel(level);
            } else {
                android.support.v17.leanback.widget.ShadowOverlayHelper.setNoneWrapperShadowFocusLevel(mView, level);
            }
            if (mDimmer != null) {
                mDimmer.setActiveLevel(level);
                int color = mDimmer.getPaint().getColor();
                if (mWrapper != null) {
                    mWrapper.setOverlayColor(color);
                } else {
                    android.support.v17.leanback.widget.ShadowOverlayHelper.setNoneWrapperOverlayColor(mView, color);
                }
            }
        }

        float getFocusLevel() {
            return mFocusLevel;
        }

        void endAnimation() {
            mAnimator.end();
        }

        @java.lang.Override
        public void onTimeUpdate(android.animation.TimeAnimator animation, long totalTime, long deltaTime) {
            float fraction;
            if (totalTime >= mDuration) {
                fraction = 1;
                mAnimator.end();
            } else {
                fraction = ((float) (totalTime / ((double) (mDuration))));
            }
            if (mInterpolator != null) {
                fraction = mInterpolator.getInterpolation(fraction);
            }
            setFocusLevel(mFocusLevelStart + (fraction * mFocusLevelDelta));
        }
    }

    static class BrowseItemFocusHighlight implements android.support.v17.leanback.widget.FocusHighlightHandler {
        private static final int DURATION_MS = 150;

        private int mScaleIndex;

        private final boolean mUseDimmer;

        BrowseItemFocusHighlight(int zoomIndex, boolean useDimmer) {
            if (!android.support.v17.leanback.widget.FocusHighlightHelper.isValidZoomIndex(zoomIndex)) {
                throw new java.lang.IllegalArgumentException("Unhandled zoom index");
            }
            mScaleIndex = zoomIndex;
            mUseDimmer = useDimmer;
        }

        private float getScale(android.content.res.Resources res) {
            return mScaleIndex == android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_NONE ? 1.0F : res.getFraction(android.support.v17.leanback.widget.FocusHighlightHelper.getResId(mScaleIndex), 1, 1);
        }

        @java.lang.Override
        public void onItemFocused(android.view.View view, boolean hasFocus) {
            view.setSelected(hasFocus);
            getOrCreateAnimator(view).animateFocus(hasFocus, false);
        }

        @java.lang.Override
        public void onInitializeView(android.view.View view) {
            getOrCreateAnimator(view).animateFocus(false, true);
        }

        private android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator getOrCreateAnimator(android.view.View view) {
            android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator animator = ((android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator) (view.getTag(R.id.lb_focus_animator)));
            if (animator == null) {
                animator = new android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator(view, getScale(view.getResources()), mUseDimmer, android.support.v17.leanback.widget.FocusHighlightHelper.BrowseItemFocusHighlight.DURATION_MS);
                view.setTag(R.id.lb_focus_animator, animator);
            }
            return animator;
        }
    }

    /**
     * Sets up the focus highlight behavior of a focused item in browse list row.
     *
     * @param zoomIndex
     * 		One of {@link FocusHighlight#ZOOM_FACTOR_SMALL}
     * 		{@link FocusHighlight#ZOOM_FACTOR_XSMALL}
     * 		{@link FocusHighlight#ZOOM_FACTOR_MEDIUM}
     * 		{@link FocusHighlight#ZOOM_FACTOR_LARGE}
     * 		{@link FocusHighlight#ZOOM_FACTOR_NONE}.
     * @param useDimmer
     * 		Allow dimming browse item when unselected.
     * @param adapter
     * 		adapter of the list row.
     */
    public static void setupBrowseItemFocusHighlight(android.support.v17.leanback.widget.ItemBridgeAdapter adapter, int zoomIndex, boolean useDimmer) {
        adapter.setFocusHighlight(new android.support.v17.leanback.widget.FocusHighlightHelper.BrowseItemFocusHighlight(zoomIndex, useDimmer));
    }

    /**
     * Sets up the focus highlight behavior of a focused item in header list.
     *
     * @param gridView
     * 		the header list.
     */
    public static void setupHeaderItemFocusHighlight(android.support.v17.leanback.widget.VerticalGridView gridView) {
        if (gridView.getAdapter() instanceof android.support.v17.leanback.widget.ItemBridgeAdapter) {
            ((android.support.v17.leanback.widget.ItemBridgeAdapter) (gridView.getAdapter())).setFocusHighlight(new android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight(gridView));
        }
    }

    static class HeaderItemFocusHighlight implements android.support.v17.leanback.widget.FocusHighlightHandler {
        private static boolean sInitialized;

        private static float sSelectScale;

        private static int sDuration;

        android.support.v17.leanback.widget.BaseGridView mGridView;

        HeaderItemFocusHighlight(android.support.v17.leanback.widget.BaseGridView gridView) {
            mGridView = gridView;
            android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.lazyInit(gridView.getContext().getResources());
        }

        private static void lazyInit(android.content.res.Resources res) {
            if (!android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sInitialized) {
                android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sSelectScale = java.lang.Float.parseFloat(res.getString(R.dimen.lb_browse_header_select_scale));
                android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sDuration = java.lang.Integer.parseInt(res.getString(R.dimen.lb_browse_header_select_duration));
                android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sInitialized = true;
            }
        }

        class HeaderFocusAnimator extends android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator {
            android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder mViewHolder;

            HeaderFocusAnimator(android.view.View view, float scale, int duration) {
                super(view, scale, false, duration);
                mViewHolder = ((android.support.v17.leanback.widget.ItemBridgeAdapter.ViewHolder) (mGridView.getChildViewHolder(view)));
            }

            @java.lang.Override
            void setFocusLevel(float level) {
                android.support.v17.leanback.widget.Presenter presenter = mViewHolder.getPresenter();
                if (presenter instanceof android.support.v17.leanback.widget.RowHeaderPresenter) {
                    ((android.support.v17.leanback.widget.RowHeaderPresenter) (presenter)).setSelectLevel(((android.support.v17.leanback.widget.RowHeaderPresenter.ViewHolder) (mViewHolder.getViewHolder())), level);
                }
                super.setFocusLevel(level);
            }
        }

        private void viewFocused(android.view.View view, boolean hasFocus) {
            view.setSelected(hasFocus);
            android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator animator = ((android.support.v17.leanback.widget.FocusHighlightHelper.FocusAnimator) (view.getTag(R.id.lb_focus_animator)));
            if (animator == null) {
                animator = new android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.HeaderFocusAnimator(view, android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sSelectScale, android.support.v17.leanback.widget.FocusHighlightHelper.HeaderItemFocusHighlight.sDuration);
                view.setTag(R.id.lb_focus_animator, animator);
            }
            animator.animateFocus(hasFocus, false);
        }

        @java.lang.Override
        public void onItemFocused(android.view.View view, boolean hasFocus) {
            viewFocused(view, hasFocus);
        }

        @java.lang.Override
        public void onInitializeView(android.view.View view) {
        }
    }
}

