/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.view;


/**
 * Queries additional state from a list of {@link ScrollCaptureTarget targets} via asynchronous
 * callbacks, then aggregates and reduces the target list to a single target, or null if no target
 * is suitable.
 * <p>
 * The rules for selection are (in order):
 * <ul>
 * <li>prefer getScrollBounds(): non-empty
 * <li>prefer View.getScrollCaptureHint == SCROLL_CAPTURE_HINT_INCLUDE
 * <li>prefer descendants before parents
 * <li>prefer larger area for getScrollBounds() (clipped to view bounds)
 * </ul>
 *
 * <p>
 * All calls to {@link ScrollCaptureCallback#onScrollCaptureSearch} are made on the main thread,
 * with results are queued and consumed to the main thread as well.
 *
 * @see #start(Handler, long, Consumer)
 * @unknown 
 */
@android.annotation.UiThread
public class ScrollCaptureTargetResolver {
    private static final java.lang.String TAG = "ScrollCaptureTargetRes";

    private static final boolean DEBUG = true;

    private final java.lang.Object mLock = new java.lang.Object();

    private final java.util.Queue<android.view.ScrollCaptureTarget> mTargets;

    private android.os.Handler mHandler;

    private long mTimeLimitMillis;

    private java.util.function.Consumer<android.view.ScrollCaptureTarget> mWhenComplete;

    private int mPendingBoundsRequests;

    private long mDeadlineMillis;

    private android.view.ScrollCaptureTarget mResult;

    private boolean mFinished;

    private boolean mStarted;

    private static int area(android.graphics.Rect r) {
        return r.width() * r.height();
    }

    private static boolean nullOrEmpty(android.graphics.Rect r) {
        return (r == null) || r.isEmpty();
    }

    /**
     * Binary operator which selects the best {@link ScrollCaptureTarget}.
     */
    private static android.view.ScrollCaptureTarget chooseTarget(android.view.ScrollCaptureTarget a, android.view.ScrollCaptureTarget b) {
        android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, (("chooseTarget: " + a) + " or ") + b);
        // Nothing plus nothing is still nothing.
        if ((a == null) && (b == null)) {
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (both null) return " + null);
            return null;
        }
        // Prefer non-null.
        if ((a == null) || (b == null)) {
            android.view.ScrollCaptureTarget c = (a == null) ? b : a;
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (other is null) return " + c);
            return c;
        }
        boolean emptyScrollBoundsA = android.view.ScrollCaptureTargetResolver.nullOrEmpty(a.getScrollBounds());
        boolean emptyScrollBoundsB = android.view.ScrollCaptureTargetResolver.nullOrEmpty(b.getScrollBounds());
        if (emptyScrollBoundsA || emptyScrollBoundsB) {
            if (emptyScrollBoundsA && emptyScrollBoundsB) {
                // Both have an empty or null scrollBounds
                android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (both have empty or null bounds) return " + null);
                return null;
            }
            // Prefer the one with a non-empty scroll bounds
            if (emptyScrollBoundsA) {
                android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (a has empty or null bounds) return " + b);
                return b;
            }
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (b has empty or null bounds) return " + a);
            return a;
        }
        final android.view.View viewA = a.getContainingView();
        final android.view.View viewB = b.getContainingView();
        // Prefer any view with scrollCaptureHint="INCLUDE", over one without
        // This is an escape hatch for the next rule (descendants first)
        boolean hintIncludeA = android.view.ScrollCaptureTargetResolver.hasIncludeHint(viewA);
        boolean hintIncludeB = android.view.ScrollCaptureTargetResolver.hasIncludeHint(viewB);
        if (hintIncludeA != hintIncludeB) {
            android.view.ScrollCaptureTarget c = (hintIncludeA) ? a : b;
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (has hint=INCLUDE) return " + c);
            return c;
        }
        // If the views are relatives, prefer the descendant. This allows implementations to
        // leverage nested scrolling APIs by interacting with the innermost scrollable view (as
        // would happen with touch input).
        if (android.view.ScrollCaptureTargetResolver.isDescendant(viewA, viewB)) {
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (b is descendant of a) return " + b);
            return b;
        }
        if (android.view.ScrollCaptureTargetResolver.isDescendant(viewB, viewA)) {
            android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: (a is descendant of b) return " + a);
            return a;
        }
        // finally, prefer one with larger scroll bounds
        int scrollAreaA = android.view.ScrollCaptureTargetResolver.area(a.getScrollBounds());
        int scrollAreaB = android.view.ScrollCaptureTargetResolver.area(b.getScrollBounds());
        android.view.ScrollCaptureTarget c = (scrollAreaA >= scrollAreaB) ? a : b;
        android.util.Log.d(android.view.ScrollCaptureTargetResolver.TAG, "chooseTarget: return " + c);
        return c;
    }

    /**
     * Creates an instance to query and filter {@code target}.
     *
     * @param targets
     * 		a list of {@link ScrollCaptureTarget} as collected by {@link View#dispatchScrollCaptureSearch}.
     * @param uiHandler
     * 		the UI thread handler for the view tree
     * @see #start(long, Consumer)
     */
    public ScrollCaptureTargetResolver(java.util.Queue<android.view.ScrollCaptureTarget> targets) {
        mTargets = targets;
    }

    void checkThread() {
        if (mHandler.getLooper() != android.os.Looper.myLooper()) {
            throw new java.lang.IllegalStateException(("Called from wrong thread! (" + java.lang.Thread.currentThread().getName()) + ")");
        }
    }

    /**
     * Blocks until a result is returned (after completion or timeout).
     * <p>
     * For testing only. Normal usage should receive a callback after calling {@link #start}.
     */
    @com.android.internal.annotations.VisibleForTesting
    public android.view.ScrollCaptureTarget waitForResult() throws java.lang.InterruptedException {
        synchronized(mLock) {
            while (!mFinished) {
                mLock.wait();
            } 
        }
        return mResult;
    }

    private void supplyResult(android.view.ScrollCaptureTarget target) {
        checkThread();
        if (mFinished) {
            return;
        }
        mResult = android.view.ScrollCaptureTargetResolver.chooseTarget(mResult, target);
        boolean finish = (mPendingBoundsRequests == 0) || (android.os.SystemClock.elapsedRealtime() >= mDeadlineMillis);
        if (finish) {
            java.lang.System.err.println("We think we're done, or timed out");
            mPendingBoundsRequests = 0;
            mWhenComplete.accept(mResult);
            synchronized(mLock) {
                mFinished = true;
                mLock.notify();
            }
            mWhenComplete = null;
        }
    }

    /**
     * Asks all targets for {@link ScrollCaptureCallback#onScrollCaptureSearch(Consumer)
     * scrollBounds}, and selects the primary target according to the {@link #chooseTarget} function.
     *
     * @param timeLimitMillis
     * 		the amount of time to wait for all responses before delivering the top
     * 		result
     * @param resultConsumer
     * 		the consumer to receive the primary target
     */
    @android.annotation.AnyThread
    public void start(android.os.Handler uiHandler, long timeLimitMillis, java.util.function.Consumer<android.view.ScrollCaptureTarget> resultConsumer) {
        synchronized(mLock) {
            if (mStarted) {
                throw new java.lang.IllegalStateException("already started!");
            }
            if (timeLimitMillis < 0) {
                throw new java.lang.IllegalArgumentException("Time limit must be positive");
            }
            mHandler = uiHandler;
            mTimeLimitMillis = timeLimitMillis;
            mWhenComplete = resultConsumer;
            if (mTargets.isEmpty()) {
                mHandler.post(() -> supplyResult(null));
                return;
            }
            mStarted = true;
            uiHandler.post(() -> run(timeLimitMillis, resultConsumer));
        }
    }

    private void run(long timeLimitMillis, java.util.function.Consumer<android.view.ScrollCaptureTarget> resultConsumer) {
        checkThread();
        mPendingBoundsRequests = mTargets.size();
        for (android.view.ScrollCaptureTarget target : mTargets) {
            queryTarget(target);
        }
        mDeadlineMillis = android.os.SystemClock.elapsedRealtime() + mTimeLimitMillis;
        mHandler.postAtTime(mTimeoutRunnable, mDeadlineMillis);
    }

    private final java.lang.Runnable mTimeoutRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            checkThread();
            supplyResult(null);
        }
    };

    /**
     * Adds a target to the list and requests {@link ScrollCaptureCallback#onScrollCaptureSearch}
     * scrollBounds} from it. Results are returned by a call to {@link #onScrollBoundsProvided}.
     *
     * @param target
     * 		the target to add
     */
    @android.annotation.UiThread
    private void queryTarget(@android.annotation.NonNull
    android.view.ScrollCaptureTarget target) {
        checkThread();
        final android.view.ScrollCaptureCallback callback = target.getCallback();
        // from the UI thread, request scroll bounds
        // allow only one callback to onReady.accept():
        callback.onScrollCaptureSearch(// Queue and consume on the UI thread
        new android.view.ScrollCaptureTargetResolver.SingletonConsumer<android.graphics.Rect>(( scrollBounds) -> mHandler.post(() -> onScrollBoundsProvided(target, scrollBounds))));
    }

    @android.annotation.UiThread
    private void onScrollBoundsProvided(android.view.ScrollCaptureTarget target, @android.annotation.Nullable
    android.graphics.Rect scrollBounds) {
        checkThread();
        if (mFinished) {
            return;
        }
        // Record progress.
        mPendingBoundsRequests--;
        // Remove the timeout.
        mHandler.removeCallbacks(mTimeoutRunnable);
        boolean doneOrTimedOut = (mPendingBoundsRequests == 0) || (android.os.SystemClock.elapsedRealtime() >= mDeadlineMillis);
        final android.view.View containingView = target.getContainingView();
        if ((!android.view.ScrollCaptureTargetResolver.nullOrEmpty(scrollBounds)) && containingView.isAggregatedVisible()) {
            target.updatePositionInWindow();
            target.setScrollBounds(scrollBounds);
            supplyResult(target);
        }
        java.lang.System.err.println("mPendingBoundsRequests: " + mPendingBoundsRequests);
        java.lang.System.err.println("mDeadlineMillis: " + mDeadlineMillis);
        java.lang.System.err.println("SystemClock.elapsedRealtime(): " + android.os.SystemClock.elapsedRealtime());
        if (!mFinished) {
            // Reschedule the timeout.
            java.lang.System.err.println("We think we're NOT done yet and will check back at " + mDeadlineMillis);
            mHandler.postAtTime(mTimeoutRunnable, mDeadlineMillis);
        }
    }

    private static boolean hasIncludeHint(android.view.View view) {
        return (view.getScrollCaptureHint() & android.view.View.SCROLL_CAPTURE_HINT_INCLUDE) != 0;
    }

    /**
     * Determines if {@code otherView} is a descendant of {@code view}.
     *
     * @param view
     * 		a view
     * @param otherView
     * 		another view
     * @return true if {@code view} is an ancestor of {@code otherView}
     */
    private static boolean isDescendant(@android.annotation.NonNull
    android.view.View view, @android.annotation.NonNull
    android.view.View otherView) {
        if (view == otherView) {
            return false;
        }
        android.view.ViewParent otherParent = otherView.getParent();
        while ((otherParent != view) && (otherParent != null)) {
            otherParent = otherParent.getParent();
        } 
        return otherParent == view;
    }

    private static int findRelation(@android.annotation.NonNull
    android.view.View a, @android.annotation.NonNull
    android.view.View b) {
        if (a == b) {
            return 0;
        }
        android.view.ViewParent parentA = a.getParent();
        android.view.ViewParent parentB = b.getParent();
        while ((parentA != null) || (parentB != null)) {
            if (parentA == parentB) {
                return 0;
            }
            if (parentA == b) {
                return 1;// A is descendant of B

            }
            if (parentB == a) {
                return -1;// B is descendant of A

            }
            if (parentA != null) {
                parentA = parentA.getParent();
            }
            if (parentB != null) {
                parentB = parentB.getParent();
            }
        } 
        return 0;
    }

    /**
     * A safe wrapper for a consumer callbacks intended to accept a single value. It ensures
     * that the receiver of the consumer does not retain a reference to {@code target} after use nor
     * cause race conditions by invoking {@link Consumer#accept accept} more than once.
     *
     * @param target
     * 		the target consumer
     */
    static class SingletonConsumer<T> implements java.util.function.Consumer<T> {
        final java.util.concurrent.atomic.AtomicReference<java.util.function.Consumer<T>> mAtomicRef;

        SingletonConsumer(java.util.function.Consumer<T> target) {
            mAtomicRef = new java.util.concurrent.atomic.AtomicReference<>(target);
        }

        @java.lang.Override
        public void accept(T t) {
            final java.util.function.Consumer<T> consumer = mAtomicRef.getAndSet(null);
            if (consumer != null) {
                consumer.accept(t);
            }
        }
    }
}

