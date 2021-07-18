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
package android.support.v7.widget;


/**
 * This implementation of {@link RecyclerView.ItemAnimator} provides basic
 * animations on remove, add, and move events that happen to the items in
 * a RecyclerView. RecyclerView uses a DefaultItemAnimator by default.
 *
 * @see RecyclerView#setItemAnimator(RecyclerView.ItemAnimator)
 */
public class DefaultItemAnimator extends android.support.v7.widget.SimpleItemAnimator {
    private static final boolean DEBUG = false;

    private java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mPendingRemovals = new java.util.ArrayList<>();

    private java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mPendingAdditions = new java.util.ArrayList<>();

    private java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.MoveInfo> mPendingMoves = new java.util.ArrayList<>();

    private java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.ChangeInfo> mPendingChanges = new java.util.ArrayList<>();

    java.util.ArrayList<java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder>> mAdditionsList = new java.util.ArrayList<>();

    java.util.ArrayList<java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.MoveInfo>> mMovesList = new java.util.ArrayList<>();

    java.util.ArrayList<java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.ChangeInfo>> mChangesList = new java.util.ArrayList<>();

    java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mAddAnimations = new java.util.ArrayList<>();

    java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mMoveAnimations = new java.util.ArrayList<>();

    java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mRemoveAnimations = new java.util.ArrayList<>();

    java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> mChangeAnimations = new java.util.ArrayList<>();

    private static class MoveInfo {
        public android.support.v7.widget.RecyclerView.ViewHolder holder;

        public int fromX;

        public int fromY;

        public int toX;

        public int toY;

        MoveInfo(android.support.v7.widget.RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    private static class ChangeInfo {
        public android.support.v7.widget.RecyclerView.ViewHolder oldHolder;

        public android.support.v7.widget.RecyclerView.ViewHolder newHolder;

        public int fromX;

        public int fromY;

        public int toX;

        public int toY;

        private ChangeInfo(android.support.v7.widget.RecyclerView.ViewHolder oldHolder, android.support.v7.widget.RecyclerView.ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }

        ChangeInfo(android.support.v7.widget.RecyclerView.ViewHolder oldHolder, android.support.v7.widget.RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
            this(oldHolder, newHolder);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((("ChangeInfo{" + "oldHolder=") + oldHolder) + ", newHolder=") + newHolder) + ", fromX=") + fromX) + ", fromY=") + fromY) + ", toX=") + toX) + ", toY=") + toY) + '}';
        }
    }

    @java.lang.Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean movesPending = !mPendingMoves.isEmpty();
        boolean changesPending = !mPendingChanges.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        if ((((!removalsPending) && (!movesPending)) && (!additionsPending)) && (!changesPending)) {
            // nothing to animate
            return;
        }
        // First, remove stuff
        for (android.support.v7.widget.RecyclerView.ViewHolder holder : mPendingRemovals) {
            animateRemoveImpl(holder);
        }
        mPendingRemovals.clear();
        // Next, move stuff
        if (movesPending) {
            final java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.MoveInfo> moves = new java.util.ArrayList<>();
            moves.addAll(mPendingMoves);
            mMovesList.add(moves);
            mPendingMoves.clear();
            java.lang.Runnable mover = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    for (android.support.v7.widget.DefaultItemAnimator.MoveInfo moveInfo : moves) {
                        animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                    }
                    moves.clear();
                    mMovesList.remove(moves);
                }
            };
            if (removalsPending) {
                android.view.View view = moves.get(0).holder.itemView;
                android.support.v4.view.ViewCompat.postOnAnimationDelayed(view, mover, getRemoveDuration());
            } else {
                mover.run();
            }
        }
        // Next, change stuff, to run in parallel with move animations
        if (changesPending) {
            final java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.ChangeInfo> changes = new java.util.ArrayList<>();
            changes.addAll(mPendingChanges);
            mChangesList.add(changes);
            mPendingChanges.clear();
            java.lang.Runnable changer = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    for (android.support.v7.widget.DefaultItemAnimator.ChangeInfo change : changes) {
                        animateChangeImpl(change);
                    }
                    changes.clear();
                    mChangesList.remove(changes);
                }
            };
            if (removalsPending) {
                android.support.v7.widget.RecyclerView.ViewHolder holder = changes.get(0).oldHolder;
                android.support.v4.view.ViewCompat.postOnAnimationDelayed(holder.itemView, changer, getRemoveDuration());
            } else {
                changer.run();
            }
        }
        // Next, add stuff
        if (additionsPending) {
            final java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> additions = new java.util.ArrayList<>();
            additions.addAll(mPendingAdditions);
            mAdditionsList.add(additions);
            mPendingAdditions.clear();
            java.lang.Runnable adder = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    for (android.support.v7.widget.RecyclerView.ViewHolder holder : additions) {
                        animateAddImpl(holder);
                    }
                    additions.clear();
                    mAdditionsList.remove(additions);
                }
            };
            if ((removalsPending || movesPending) || changesPending) {
                long removeDuration = (removalsPending) ? getRemoveDuration() : 0;
                long moveDuration = (movesPending) ? getMoveDuration() : 0;
                long changeDuration = (changesPending) ? getChangeDuration() : 0;
                long totalDelay = removeDuration + java.lang.Math.max(moveDuration, changeDuration);
                android.view.View view = additions.get(0).itemView;
                android.support.v4.view.ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
            } else {
                adder.run();
            }
        }
    }

    @java.lang.Override
    public boolean animateRemove(final android.support.v7.widget.RecyclerView.ViewHolder holder) {
        resetAnimation(holder);
        mPendingRemovals.add(holder);
        return true;
    }

    private void animateRemoveImpl(final android.support.v7.widget.RecyclerView.ViewHolder holder) {
        final android.view.View view = holder.itemView;
        final android.support.v4.view.ViewPropertyAnimatorCompat animation = android.support.v4.view.ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
        animation.setDuration(getRemoveDuration()).alpha(0).setListener(new android.support.v7.widget.DefaultItemAnimator.VpaListenerAdapter() {
            @java.lang.Override
            public void onAnimationStart(android.view.View view) {
                dispatchRemoveStarting(holder);
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.View view) {
                animation.setListener(null);
                android.support.v4.view.ViewCompat.setAlpha(view, 1);
                dispatchRemoveFinished(holder);
                mRemoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    @java.lang.Override
    public boolean animateAdd(final android.support.v7.widget.RecyclerView.ViewHolder holder) {
        resetAnimation(holder);
        android.support.v4.view.ViewCompat.setAlpha(holder.itemView, 0);
        mPendingAdditions.add(holder);
        return true;
    }

    void animateAddImpl(final android.support.v7.widget.RecyclerView.ViewHolder holder) {
        final android.view.View view = holder.itemView;
        final android.support.v4.view.ViewPropertyAnimatorCompat animation = android.support.v4.view.ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animation.alpha(1).setDuration(getAddDuration()).setListener(new android.support.v7.widget.DefaultItemAnimator.VpaListenerAdapter() {
            @java.lang.Override
            public void onAnimationStart(android.view.View view) {
                dispatchAddStarting(holder);
            }

            @java.lang.Override
            public void onAnimationCancel(android.view.View view) {
                android.support.v4.view.ViewCompat.setAlpha(view, 1);
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.View view) {
                animation.setListener(null);
                dispatchAddFinished(holder);
                mAddAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    @java.lang.Override
    public boolean animateMove(final android.support.v7.widget.RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        final android.view.View view = holder.itemView;
        fromX += android.support.v4.view.ViewCompat.getTranslationX(holder.itemView);
        fromY += android.support.v4.view.ViewCompat.getTranslationY(holder.itemView);
        resetAnimation(holder);
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        if ((deltaX == 0) && (deltaY == 0)) {
            dispatchMoveFinished(holder);
            return false;
        }
        if (deltaX != 0) {
            android.support.v4.view.ViewCompat.setTranslationX(view, -deltaX);
        }
        if (deltaY != 0) {
            android.support.v4.view.ViewCompat.setTranslationY(view, -deltaY);
        }
        mPendingMoves.add(new android.support.v7.widget.DefaultItemAnimator.MoveInfo(holder, fromX, fromY, toX, toY));
        return true;
    }

    void animateMoveImpl(final android.support.v7.widget.RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        final android.view.View view = holder.itemView;
        final int deltaX = toX - fromX;
        final int deltaY = toY - fromY;
        if (deltaX != 0) {
            android.support.v4.view.ViewCompat.animate(view).translationX(0);
        }
        if (deltaY != 0) {
            android.support.v4.view.ViewCompat.animate(view).translationY(0);
        }
        // TODO: make EndActions end listeners instead, since end actions aren't called when
        // vpas are canceled (and can't end them. why?)
        // need listener functionality in VPACompat for this. Ick.
        final android.support.v4.view.ViewPropertyAnimatorCompat animation = android.support.v4.view.ViewCompat.animate(view);
        mMoveAnimations.add(holder);
        animation.setDuration(getMoveDuration()).setListener(new android.support.v7.widget.DefaultItemAnimator.VpaListenerAdapter() {
            @java.lang.Override
            public void onAnimationStart(android.view.View view) {
                dispatchMoveStarting(holder);
            }

            @java.lang.Override
            public void onAnimationCancel(android.view.View view) {
                if (deltaX != 0) {
                    android.support.v4.view.ViewCompat.setTranslationX(view, 0);
                }
                if (deltaY != 0) {
                    android.support.v4.view.ViewCompat.setTranslationY(view, 0);
                }
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.View view) {
                animation.setListener(null);
                dispatchMoveFinished(holder);
                mMoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    @java.lang.Override
    public boolean animateChange(android.support.v7.widget.RecyclerView.ViewHolder oldHolder, android.support.v7.widget.RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        if (oldHolder == newHolder) {
            // Don't know how to run change animations when the same view holder is re-used.
            // run a move animation to handle position changes.
            return animateMove(oldHolder, fromX, fromY, toX, toY);
        }
        final float prevTranslationX = android.support.v4.view.ViewCompat.getTranslationX(oldHolder.itemView);
        final float prevTranslationY = android.support.v4.view.ViewCompat.getTranslationY(oldHolder.itemView);
        final float prevAlpha = android.support.v4.view.ViewCompat.getAlpha(oldHolder.itemView);
        resetAnimation(oldHolder);
        int deltaX = ((int) ((toX - fromX) - prevTranslationX));
        int deltaY = ((int) ((toY - fromY) - prevTranslationY));
        // recover prev translation state after ending animation
        android.support.v4.view.ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
        android.support.v4.view.ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);
        android.support.v4.view.ViewCompat.setAlpha(oldHolder.itemView, prevAlpha);
        if (newHolder != null) {
            // carry over translation values
            resetAnimation(newHolder);
            android.support.v4.view.ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
            android.support.v4.view.ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
            android.support.v4.view.ViewCompat.setAlpha(newHolder.itemView, 0);
        }
        mPendingChanges.add(new android.support.v7.widget.DefaultItemAnimator.ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
        return true;
    }

    void animateChangeImpl(final android.support.v7.widget.DefaultItemAnimator.ChangeInfo changeInfo) {
        final android.support.v7.widget.RecyclerView.ViewHolder holder = changeInfo.oldHolder;
        final android.view.View view = (holder == null) ? null : holder.itemView;
        final android.support.v7.widget.RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
        final android.view.View newView = (newHolder != null) ? newHolder.itemView : null;
        if (view != null) {
            final android.support.v4.view.ViewPropertyAnimatorCompat oldViewAnim = android.support.v4.view.ViewCompat.animate(view).setDuration(getChangeDuration());
            mChangeAnimations.add(changeInfo.oldHolder);
            oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
            oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
            oldViewAnim.alpha(0).setListener(new android.support.v7.widget.DefaultItemAnimator.VpaListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.view.View view) {
                    dispatchChangeStarting(changeInfo.oldHolder, true);
                }

                @java.lang.Override
                public void onAnimationEnd(android.view.View view) {
                    oldViewAnim.setListener(null);
                    android.support.v4.view.ViewCompat.setAlpha(view, 1);
                    android.support.v4.view.ViewCompat.setTranslationX(view, 0);
                    android.support.v4.view.ViewCompat.setTranslationY(view, 0);
                    dispatchChangeFinished(changeInfo.oldHolder, true);
                    mChangeAnimations.remove(changeInfo.oldHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
        if (newView != null) {
            final android.support.v4.view.ViewPropertyAnimatorCompat newViewAnimation = android.support.v4.view.ViewCompat.animate(newView);
            mChangeAnimations.add(changeInfo.newHolder);
            newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration()).alpha(1).setListener(new android.support.v7.widget.DefaultItemAnimator.VpaListenerAdapter() {
                @java.lang.Override
                public void onAnimationStart(android.view.View view) {
                    dispatchChangeStarting(changeInfo.newHolder, false);
                }

                @java.lang.Override
                public void onAnimationEnd(android.view.View view) {
                    newViewAnimation.setListener(null);
                    android.support.v4.view.ViewCompat.setAlpha(newView, 1);
                    android.support.v4.view.ViewCompat.setTranslationX(newView, 0);
                    android.support.v4.view.ViewCompat.setTranslationY(newView, 0);
                    dispatchChangeFinished(changeInfo.newHolder, false);
                    mChangeAnimations.remove(changeInfo.newHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }

    private void endChangeAnimation(java.util.List<android.support.v7.widget.DefaultItemAnimator.ChangeInfo> infoList, android.support.v7.widget.RecyclerView.ViewHolder item) {
        for (int i = infoList.size() - 1; i >= 0; i--) {
            android.support.v7.widget.DefaultItemAnimator.ChangeInfo changeInfo = infoList.get(i);
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if ((changeInfo.oldHolder == null) && (changeInfo.newHolder == null)) {
                    infoList.remove(changeInfo);
                }
            }
        }
    }

    private void endChangeAnimationIfNecessary(android.support.v7.widget.DefaultItemAnimator.ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }

    private boolean endChangeAnimationIfNecessary(android.support.v7.widget.DefaultItemAnimator.ChangeInfo changeInfo, android.support.v7.widget.RecyclerView.ViewHolder item) {
        boolean oldItem = false;
        if (changeInfo.newHolder == item) {
            changeInfo.newHolder = null;
        } else
            if (changeInfo.oldHolder == item) {
                changeInfo.oldHolder = null;
                oldItem = true;
            } else {
                return false;
            }

        android.support.v4.view.ViewCompat.setAlpha(item.itemView, 1);
        android.support.v4.view.ViewCompat.setTranslationX(item.itemView, 0);
        android.support.v4.view.ViewCompat.setTranslationY(item.itemView, 0);
        dispatchChangeFinished(item, oldItem);
        return true;
    }

    @java.lang.Override
    public void endAnimation(android.support.v7.widget.RecyclerView.ViewHolder item) {
        final android.view.View view = item.itemView;
        // this will trigger end callback which should set properties to their target values.
        android.support.v4.view.ViewCompat.animate(view).cancel();
        // TODO if some other animations are chained to end, how do we cancel them as well?
        for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
            android.support.v7.widget.DefaultItemAnimator.MoveInfo moveInfo = mPendingMoves.get(i);
            if (moveInfo.holder == item) {
                android.support.v4.view.ViewCompat.setTranslationY(view, 0);
                android.support.v4.view.ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(item);
                mPendingMoves.remove(i);
            }
        }
        endChangeAnimation(mPendingChanges, item);
        if (mPendingRemovals.remove(item)) {
            android.support.v4.view.ViewCompat.setAlpha(view, 1);
            dispatchRemoveFinished(item);
        }
        if (mPendingAdditions.remove(item)) {
            android.support.v4.view.ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
        }
        for (int i = mChangesList.size() - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.ChangeInfo> changes = mChangesList.get(i);
            endChangeAnimation(changes, item);
            if (changes.isEmpty()) {
                mChangesList.remove(i);
            }
        }
        for (int i = mMovesList.size() - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.MoveInfo> moves = mMovesList.get(i);
            for (int j = moves.size() - 1; j >= 0; j--) {
                android.support.v7.widget.DefaultItemAnimator.MoveInfo moveInfo = moves.get(j);
                if (moveInfo.holder == item) {
                    android.support.v4.view.ViewCompat.setTranslationY(view, 0);
                    android.support.v4.view.ViewCompat.setTranslationX(view, 0);
                    dispatchMoveFinished(item);
                    moves.remove(j);
                    if (moves.isEmpty()) {
                        mMovesList.remove(i);
                    }
                    break;
                }
            }
        }
        for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
            if (additions.remove(item)) {
                android.support.v4.view.ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(i);
                }
            }
        }
        // animations should be ended by the cancel above.
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (mRemoveAnimations.remove(item) && android.support.v7.widget.DefaultItemAnimator.DEBUG) {
            throw new java.lang.IllegalStateException("after animation is cancelled, item should not be in " + "mRemoveAnimations list");
        }
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (mAddAnimations.remove(item) && android.support.v7.widget.DefaultItemAnimator.DEBUG) {
            throw new java.lang.IllegalStateException("after animation is cancelled, item should not be in " + "mAddAnimations list");
        }
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (mChangeAnimations.remove(item) && android.support.v7.widget.DefaultItemAnimator.DEBUG) {
            throw new java.lang.IllegalStateException("after animation is cancelled, item should not be in " + "mChangeAnimations list");
        }
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (mMoveAnimations.remove(item) && android.support.v7.widget.DefaultItemAnimator.DEBUG) {
            throw new java.lang.IllegalStateException("after animation is cancelled, item should not be in " + "mMoveAnimations list");
        }
        dispatchFinishedWhenDone();
    }

    private void resetAnimation(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        android.support.v4.animation.AnimatorCompatHelper.clearInterpolator(holder.itemView);
        endAnimation(holder);
    }

    @java.lang.Override
    public boolean isRunning() {
        return ((((((((((!mPendingAdditions.isEmpty()) || (!mPendingChanges.isEmpty())) || (!mPendingMoves.isEmpty())) || (!mPendingRemovals.isEmpty())) || (!mMoveAnimations.isEmpty())) || (!mRemoveAnimations.isEmpty())) || (!mAddAnimations.isEmpty())) || (!mChangeAnimations.isEmpty())) || (!mMovesList.isEmpty())) || (!mAdditionsList.isEmpty())) || (!mChangesList.isEmpty());
    }

    /**
     * Check the state of currently pending and running animations. If there are none
     * pending/running, call {@link #dispatchAnimationsFinished()} to notify any
     * listeners.
     */
    void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    @java.lang.Override
    public void endAnimations() {
        int count = mPendingMoves.size();
        for (int i = count - 1; i >= 0; i--) {
            android.support.v7.widget.DefaultItemAnimator.MoveInfo item = mPendingMoves.get(i);
            android.view.View view = item.holder.itemView;
            android.support.v4.view.ViewCompat.setTranslationY(view, 0);
            android.support.v4.view.ViewCompat.setTranslationX(view, 0);
            dispatchMoveFinished(item.holder);
            mPendingMoves.remove(i);
        }
        count = mPendingRemovals.size();
        for (int i = count - 1; i >= 0; i--) {
            android.support.v7.widget.RecyclerView.ViewHolder item = mPendingRemovals.get(i);
            dispatchRemoveFinished(item);
            mPendingRemovals.remove(i);
        }
        count = mPendingAdditions.size();
        for (int i = count - 1; i >= 0; i--) {
            android.support.v7.widget.RecyclerView.ViewHolder item = mPendingAdditions.get(i);
            android.view.View view = item.itemView;
            android.support.v4.view.ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
            mPendingAdditions.remove(i);
        }
        count = mPendingChanges.size();
        for (int i = count - 1; i >= 0; i--) {
            endChangeAnimationIfNecessary(mPendingChanges.get(i));
        }
        mPendingChanges.clear();
        if (!isRunning()) {
            return;
        }
        int listCount = mMovesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.MoveInfo> moves = mMovesList.get(i);
            count = moves.size();
            for (int j = count - 1; j >= 0; j--) {
                android.support.v7.widget.DefaultItemAnimator.MoveInfo moveInfo = moves.get(j);
                android.support.v7.widget.RecyclerView.ViewHolder item = moveInfo.holder;
                android.view.View view = item.itemView;
                android.support.v4.view.ViewCompat.setTranslationY(view, 0);
                android.support.v4.view.ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(moveInfo.holder);
                moves.remove(j);
                if (moves.isEmpty()) {
                    mMovesList.remove(moves);
                }
            }
        }
        listCount = mAdditionsList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
            count = additions.size();
            for (int j = count - 1; j >= 0; j--) {
                android.support.v7.widget.RecyclerView.ViewHolder item = additions.get(j);
                android.view.View view = item.itemView;
                android.support.v4.view.ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                additions.remove(j);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions);
                }
            }
        }
        listCount = mChangesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            java.util.ArrayList<android.support.v7.widget.DefaultItemAnimator.ChangeInfo> changes = mChangesList.get(i);
            count = changes.size();
            for (int j = count - 1; j >= 0; j--) {
                endChangeAnimationIfNecessary(changes.get(j));
                if (changes.isEmpty()) {
                    mChangesList.remove(changes);
                }
            }
        }
        cancelAll(mRemoveAnimations);
        cancelAll(mMoveAnimations);
        cancelAll(mAddAnimations);
        cancelAll(mChangeAnimations);
        dispatchAnimationsFinished();
    }

    void cancelAll(java.util.List<android.support.v7.widget.RecyclerView.ViewHolder> viewHolders) {
        for (int i = viewHolders.size() - 1; i >= 0; i--) {
            android.support.v4.view.ViewCompat.animate(viewHolders.get(i).itemView).cancel();
        }
    }

    /**
     * {@inheritDoc }
     * <p>
     * If the payload list is not empty, DefaultItemAnimator returns <code>true</code>.
     * When this is the case:
     * <ul>
     * <li>If you override {@link #animateChange(ViewHolder, ViewHolder, int, int, int, int)}, both
     * ViewHolder arguments will be the same instance.
     * </li>
     * <li>
     * If you are not overriding {@link #animateChange(ViewHolder, ViewHolder, int, int, int, int)},
     * then DefaultItemAnimator will call {@link #animateMove(ViewHolder, int, int, int, int)} and
     * run a move animation instead.
     * </li>
     * </ul>
     */
    @java.lang.Override
    public boolean canReuseUpdatedViewHolder(@android.support.annotation.NonNull
    android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.NonNull
    java.util.List<java.lang.Object> payloads) {
        return (!payloads.isEmpty()) || super.canReuseUpdatedViewHolder(viewHolder, payloads);
    }

    private static class VpaListenerAdapter implements android.support.v4.view.ViewPropertyAnimatorListener {
        VpaListenerAdapter() {
        }

        @java.lang.Override
        public void onAnimationStart(android.view.View view) {
        }

        @java.lang.Override
        public void onAnimationEnd(android.view.View view) {
        }

        @java.lang.Override
        public void onAnimationCancel(android.view.View view) {
        }
    }
}

