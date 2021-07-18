/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * Class intended to support snapping for a {@link RecyclerView}.
 * <p>
 * SnapHelper tries to handle fling as well but for this to work properly, the
 * {@link RecyclerView.LayoutManager} must implement the {@link ScrollVectorProvider} interface or
 * you should override {@link #onFling(int, int)} and handle fling manually.
 */
public abstract class SnapHelper extends android.support.v7.widget.RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100.0F;

    android.support.v7.widget.RecyclerView mRecyclerView;

    private android.widget.Scroller mGravityScroller;

    // Handles the snap on scroll case.
    private final android.support.v7.widget.RecyclerView.OnScrollListener mScrollListener = new android.support.v7.widget.RecyclerView.OnScrollListener() {
        boolean mScrolled = false;

        @java.lang.Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if ((newState == android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE) && mScrolled) {
                mScrolled = false;
                snapToTargetExistingView();
            }
        }

        @java.lang.Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            if ((dx != 0) || (dy != 0)) {
                mScrolled = true;
            }
        }
    };

    @java.lang.Override
    public boolean onFling(int velocityX, int velocityY) {
        android.support.v7.widget.RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        android.support.v7.widget.RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return false;
        }
        int minFlingVelocity = mRecyclerView.getMinFlingVelocity();
        return ((java.lang.Math.abs(velocityY) > minFlingVelocity) || (java.lang.Math.abs(velocityX) > minFlingVelocity)) && snapFromFling(layoutManager, velocityX, velocityY);
    }

    /**
     * Attaches the {@link SnapHelper} to the provided RecyclerView, by calling
     * {@link RecyclerView#setOnFlingListener(RecyclerView.OnFlingListener)}.
     * You can call this method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView
     * 		The RecyclerView instance to which you want to add this helper or
     * 		{@code null} if you want to remove SnapHelper from the current
     * 		RecyclerView.
     * @throws IllegalArgumentException
     * 		if there is already a {@link RecyclerView.OnFlingListener}
     * 		attached to the provided {@link RecyclerView}.
     */
    public void attachToRecyclerView(@android.support.annotation.Nullable
    android.support.v7.widget.RecyclerView recyclerView) throws java.lang.IllegalStateException {
        if (mRecyclerView == recyclerView) {
            return;// nothing to do

        }
        if (mRecyclerView != null) {
            destroyCallbacks();
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setupCallbacks();
            mGravityScroller = new android.widget.Scroller(mRecyclerView.getContext(), new android.view.animation.DecelerateInterpolator());
            snapToTargetExistingView();
        }
    }

    /**
     * Called when an instance of a {@link RecyclerView} is attached.
     */
    private void setupCallbacks() throws java.lang.IllegalStateException {
        if (mRecyclerView.getOnFlingListener() != null) {
            throw new java.lang.IllegalStateException("An instance of OnFlingListener already set.");
        }
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setOnFlingListener(this);
    }

    /**
     * Called when the instance of a {@link RecyclerView} is detached.
     */
    private void destroyCallbacks() {
        mRecyclerView.removeOnScrollListener(mScrollListener);
        mRecyclerView.setOnFlingListener(null);
    }

    /**
     * Calculated the estimated scroll distance in each direction given velocities on both axes.
     *
     * @param velocityX
     * 		Fling velocity on the horizontal axis.
     * @param velocityY
     * 		Fling velocity on the vertical axis.
     * @return array holding the calculated distances in x and y directions
    respectively.
     */
    public int[] calculateScrollDistance(int velocityX, int velocityY) {
        int[] outDist = new int[2];
        mGravityScroller.fling(0, 0, velocityX, velocityY, java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE, java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE);
        outDist[0] = mGravityScroller.getFinalX();
        outDist[1] = mGravityScroller.getFinalY();
        return outDist;
    }

    /**
     * Helper method to facilitate for snapping triggered by a fling.
     *
     * @param layoutManager
     * 		The {@link LayoutManager} associated with the attached
     * 		{@link RecyclerView}.
     * @param velocityX
     * 		Fling velocity on the horizontal axis.
     * @param velocityY
     * 		Fling velocity on the vertical axis.
     * @return true if it is handled, false otherwise.
     */
    private boolean snapFromFling(@android.support.annotation.NonNull
    android.support.v7.widget.RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (!(layoutManager instanceof android.support.v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        }
        android.support.v7.widget.RecyclerView.SmoothScroller smoothScroller = createSnapScroller(layoutManager);
        if (smoothScroller == null) {
            return false;
        }
        int targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (targetPosition == android.support.v7.widget.RecyclerView.NO_POSITION) {
            return false;
        }
        smoothScroller.setTargetPosition(targetPosition);
        layoutManager.startSmoothScroll(smoothScroller);
        return true;
    }

    /**
     * Snaps to a target view which currently exists in the attached {@link RecyclerView}. This
     * method is used to snap the view when the {@link RecyclerView} is first attached; when
     * snapping was triggered by a scroll and when the fling is at its final stages.
     */
    void snapToTargetExistingView() {
        if (mRecyclerView == null) {
            return;
        }
        android.support.v7.widget.RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        android.view.View snapView = findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        int[] snapDistance = calculateDistanceToFinalSnap(layoutManager, snapView);
        if ((snapDistance[0] != 0) || (snapDistance[1] != 0)) {
            mRecyclerView.smoothScrollBy(snapDistance[0], snapDistance[1]);
        }
    }

    /**
     * Creates a scroller to be used in the snapping implementation.
     *
     * @param layoutManager
     * 		The {@link RecyclerView.LayoutManager} associated with the attached
     * 		{@link RecyclerView}.
     * @return a {@link LinearSmoothScroller} which will handle the scrolling.
     */
    @android.support.annotation.Nullable
    private android.support.v7.widget.LinearSmoothScroller createSnapScroller(android.support.v7.widget.RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof android.support.v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new android.support.v7.widget.LinearSmoothScroller(mRecyclerView.getContext()) {
            @java.lang.Override
            protected void onTargetFound(android.view.View targetView, android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.RecyclerView.SmoothScroller.Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(android.support.v7.widget.SnapHelper.this.mRecyclerView.getLayoutManager(), targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(java.lang.Math.max(java.lang.Math.abs(dx), java.lang.Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @java.lang.Override
            protected float calculateSpeedPerPixel(android.util.DisplayMetrics displayMetrics) {
                return android.support.v7.widget.SnapHelper.MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
    }

    /**
     * Override this method to snap to a particular point within the target view or the container
     * view on any axis.
     * <p>
     * This method is called when the {@link SnapHelper} has intercepted a fling and it needs
     * to know the exact distance required to scroll by in order to snap to the target view.
     *
     * @param layoutManager
     * 		the {@link RecyclerView.LayoutManager} associated with the attached
     * 		{@link RecyclerView}
     * @param targetView
     * 		the target view that is chosen as the view to snap
     * @return the output coordinates the put the result into. out[0] is the distance
    on horizontal axis and out[1] is the distance on vertical axis.
     */
    @java.lang.SuppressWarnings("WeakerAccess")
    @android.support.annotation.Nullable
    public abstract int[] calculateDistanceToFinalSnap(@android.support.annotation.NonNull
    android.support.v7.widget.RecyclerView.LayoutManager layoutManager, @android.support.annotation.NonNull
    android.view.View targetView);

    /**
     * Override this method to provide a particular target view for snapping.
     * <p>
     * This method is called when the {@link SnapHelper} is ready to start snapping and requires
     * a target view to snap to. It will be explicitly called when the scroll state becomes idle
     * after a scroll. It will also be called when the {@link SnapHelper} is preparing to snap
     * after a fling and requires a reference view from the current set of child views.
     * <p>
     * If this method returns {@code null}, SnapHelper will not snap to any view.
     *
     * @param layoutManager
     * 		the {@link RecyclerView.LayoutManager} associated with the attached
     * 		{@link RecyclerView}
     * @return the target view to which to snap on fling or end of scroll
     */
    @java.lang.SuppressWarnings("WeakerAccess")
    @android.support.annotation.Nullable
    public abstract android.view.View findSnapView(android.support.v7.widget.RecyclerView.LayoutManager layoutManager);

    /**
     * Override to provide a particular adapter target position for snapping.
     *
     * @param layoutManager
     * 		the {@link RecyclerView.LayoutManager} associated with the attached
     * 		{@link RecyclerView}
     * @param velocityX
     * 		fling velocity on the horizontal axis
     * @param velocityY
     * 		fling velocity on the vertical axis
     * @return the target adapter position to you want to snap or {@link RecyclerView#NO_POSITION}
    if no snapping should happen
     */
    public abstract int findTargetSnapPosition(android.support.v7.widget.RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY);
}

