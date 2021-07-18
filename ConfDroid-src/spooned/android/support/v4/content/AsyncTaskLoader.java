/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.content;


/**
 * Static library support version of the framework's {@link android.content.AsyncTaskLoader}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 */
public abstract class AsyncTaskLoader<D> extends android.support.v4.content.Loader<D> {
    static final java.lang.String TAG = "AsyncTaskLoader";

    static final boolean DEBUG = false;

    final class LoadTask extends android.support.v4.content.ModernAsyncTask<java.lang.Void, java.lang.Void, D> implements java.lang.Runnable {
        private final java.util.concurrent.CountDownLatch mDone = new java.util.concurrent.CountDownLatch(1);

        // Set to true to indicate that the task has been posted to a handler for
        // execution at a later time.  Used to throttle updates.
        boolean waiting;

        /* Runs on a worker thread */
        @java.lang.Override
        protected D doInBackground(java.lang.Void... params) {
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, this + " >>> doInBackground");

            try {
                D data = android.support.v4.content.AsyncTaskLoader.this.onLoadInBackground();
                if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                    android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, this + "  <<< doInBackground");

                return data;
            } catch (android.support.v4.os.OperationCanceledException ex) {
                if (!isCancelled()) {
                    // onLoadInBackground threw a canceled exception spuriously.
                    // This is problematic because it means that the LoaderManager did not
                    // cancel the Loader itself and still expects to receive a result.
                    // Additionally, the Loader's own state will not have been updated to
                    // reflect the fact that the task was being canceled.
                    // So we treat this case as an unhandled exception.
                    throw ex;
                }
                if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                    android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, this + "  <<< doInBackground (was canceled)", ex);

                return null;
            }
        }

        /* Runs on the UI thread */
        @java.lang.Override
        protected void onPostExecute(D data) {
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, this + " onPostExecute");

            try {
                android.support.v4.content.AsyncTaskLoader.this.dispatchOnLoadComplete(this, data);
            } finally {
                mDone.countDown();
            }
        }

        /* Runs on the UI thread */
        @java.lang.Override
        protected void onCancelled(D data) {
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, this + " onCancelled");

            try {
                android.support.v4.content.AsyncTaskLoader.this.dispatchOnCancelled(this, data);
            } finally {
                mDone.countDown();
            }
        }

        /* Runs on the UI thread, when the waiting task is posted to a handler.
        This method is only executed when task execution was deferred (waiting was true).
         */
        @java.lang.Override
        public void run() {
            waiting = false;
            android.support.v4.content.AsyncTaskLoader.this.executePendingTask();
        }

        /* Used for testing purposes to wait for the task to complete. */
        public void waitForLoader() {
            try {
                mDone.await();
            } catch (java.lang.InterruptedException e) {
                // Ignore
            }
        }
    }

    private final java.util.concurrent.Executor mExecutor;

    volatile android.support.v4.content.AsyncTaskLoader<D>.LoadTask mTask;

    volatile android.support.v4.content.AsyncTaskLoader<D>.LoadTask mCancellingTask;

    long mUpdateThrottle;

    long mLastLoadCompleteTime = -10000;

    android.os.Handler mHandler;

    public AsyncTaskLoader(android.content.Context context) {
        this(context, android.support.v4.content.ModernAsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTaskLoader(android.content.Context context, java.util.concurrent.Executor executor) {
        super(context);
        mExecutor = executor;
    }

    /**
     * Set amount to throttle updates by.  This is the minimum time from
     * when the last {@link #loadInBackground()} call has completed until
     * a new load is scheduled.
     *
     * @param delayMS
     * 		Amount of delay, in milliseconds.
     */
    public void setUpdateThrottle(long delayMS) {
        mUpdateThrottle = delayMS;
        if (delayMS != 0) {
            mHandler = new android.os.Handler();
        }
    }

    @java.lang.Override
    protected void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        mTask = new LoadTask();
        if (android.support.v4.content.AsyncTaskLoader.DEBUG)
            android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Preparing load: mTask=" + mTask);

        executePendingTask();
    }

    @java.lang.Override
    protected boolean onCancelLoad() {
        if (android.support.v4.content.AsyncTaskLoader.DEBUG)
            android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "onCancelLoad: mTask=" + mTask);

        if (mTask != null) {
            if (mCancellingTask != null) {
                // There was a pending task already waiting for a previous
                // one being canceled; just drop it.
                if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                    android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "cancelLoad: still waiting for cancelled task; dropping next");

                if (mTask.waiting) {
                    mTask.waiting = false;
                    mHandler.removeCallbacks(mTask);
                }
                mTask = null;
                return false;
            } else
                if (mTask.waiting) {
                    // There is a task, but it is waiting for the time it should
                    // execute.  We can just toss it.
                    if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                        android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "cancelLoad: task is waiting, dropping it");

                    mTask.waiting = false;
                    mHandler.removeCallbacks(mTask);
                    mTask = null;
                    return false;
                } else {
                    boolean cancelled = mTask.cancel(false);
                    if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                        android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "cancelLoad: cancelled=" + cancelled);

                    if (cancelled) {
                        mCancellingTask = mTask;
                        cancelLoadInBackground();
                    }
                    mTask = null;
                    return cancelled;
                }

        }
        return false;
    }

    /**
     * Called if the task was canceled before it was completed.  Gives the class a chance
     * to clean up post-cancellation and to properly dispose of the result.
     *
     * @param data
     * 		The value that was returned by {@link #loadInBackground}, or null
     * 		if the task threw {@link OperationCanceledException}.
     */
    public void onCanceled(D data) {
    }

    void executePendingTask() {
        if ((mCancellingTask == null) && (mTask != null)) {
            if (mTask.waiting) {
                mTask.waiting = false;
                mHandler.removeCallbacks(mTask);
            }
            if (mUpdateThrottle > 0) {
                long now = android.os.SystemClock.uptimeMillis();
                if (now < (mLastLoadCompleteTime + mUpdateThrottle)) {
                    // Not yet time to do another load.
                    if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                        android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, (("Waiting until " + (mLastLoadCompleteTime + mUpdateThrottle)) + " to execute: ") + mTask);

                    mTask.waiting = true;
                    mHandler.postAtTime(mTask, mLastLoadCompleteTime + mUpdateThrottle);
                    return;
                }
            }
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Executing: " + mTask);

            mTask.executeOnExecutor(mExecutor, ((java.lang.Void[]) (null)));
        }
    }

    void dispatchOnCancelled(android.support.v4.content.AsyncTaskLoader<D>.LoadTask task, D data) {
        onCanceled(data);
        if (mCancellingTask == task) {
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Cancelled task is now canceled!");

            rollbackContentChanged();
            mLastLoadCompleteTime = android.os.SystemClock.uptimeMillis();
            mCancellingTask = null;
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Delivering cancellation");

            deliverCancellation();
            executePendingTask();
        }
    }

    void dispatchOnLoadComplete(android.support.v4.content.AsyncTaskLoader<D>.LoadTask task, D data) {
        if (mTask != task) {
            if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Load complete of old task, trying to cancel");

            dispatchOnCancelled(task, data);
        } else {
            if (isAbandoned()) {
                // This cursor has been abandoned; just cancel the new data.
                onCanceled(data);
            } else {
                commitContentChanged();
                mLastLoadCompleteTime = android.os.SystemClock.uptimeMillis();
                mTask = null;
                if (android.support.v4.content.AsyncTaskLoader.DEBUG)
                    android.util.Log.v(android.support.v4.content.AsyncTaskLoader.TAG, "Delivering result");

                deliverResult(data);
            }
        }
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     *
     * Implementations should not deliver the result directly, but should return them
     * from this method, which will eventually end up calling {@link #deliverResult} on
     * the UI thread.  If implementations need to process the results on the UI thread
     * they may override {@link #deliverResult} and do so there.
     *
     * To support cancellation, this method should periodically check the value of
     * {@link #isLoadInBackgroundCanceled} and terminate when it returns true.
     * Subclasses may also override {@link #cancelLoadInBackground} to interrupt the load
     * directly instead of polling {@link #isLoadInBackgroundCanceled}.
     *
     * When the load is canceled, this method may either return normally or throw
     * {@link OperationCanceledException}.  In either case, the {@link Loader} will
     * call {@link #onCanceled} to perform post-cancellation cleanup and to dispose of the
     * result object, if any.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException
     * 		if the load is canceled during execution.
     * @see #isLoadInBackgroundCanceled
     * @see #cancelLoadInBackground
     * @see #onCanceled
     */
    public abstract D loadInBackground();

    /**
     * Calls {@link #loadInBackground()}.
     *
     * This method is reserved for use by the loader framework.
     * Subclasses should override {@link #loadInBackground} instead of this method.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException
     * 		if the load is canceled during execution.
     * @see #loadInBackground
     */
    protected D onLoadInBackground() {
        return loadInBackground();
    }

    /**
     * Called on the main thread to abort a load in progress.
     *
     * Override this method to abort the current invocation of {@link #loadInBackground}
     * that is running in the background on a worker thread.
     *
     * This method should do nothing if {@link #loadInBackground} has not started
     * running or if it has already finished.
     *
     * @see #loadInBackground
     */
    public void cancelLoadInBackground() {
    }

    /**
     * Returns true if the current invocation of {@link #loadInBackground} is being canceled.
     *
     * @return True if the current invocation of {@link #loadInBackground} is being canceled.
     * @see #loadInBackground
     */
    public boolean isLoadInBackgroundCanceled() {
        return mCancellingTask != null;
    }

    /**
     * Locks the current thread until the loader completes the current load
     * operation. Returns immediately if there is no load operation running.
     * Should not be called from the UI thread: calling it from the UI
     * thread would cause a deadlock.
     * <p>
     * Use for testing only.  <b>Never</b> call this from a UI thread.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void waitForLoader() {
        android.support.v4.content.AsyncTaskLoader<D>.LoadTask task = mTask;
        if (task != null) {
            task.waitForLoader();
        }
    }

    @java.lang.Override
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        super.dump(prefix, fd, writer, args);
        if (mTask != null) {
            writer.print(prefix);
            writer.print("mTask=");
            writer.print(mTask);
            writer.print(" waiting=");
            writer.println(mTask.waiting);
        }
        if (mCancellingTask != null) {
            writer.print(prefix);
            writer.print("mCancellingTask=");
            writer.print(mCancellingTask);
            writer.print(" waiting=");
            writer.println(mCancellingTask.waiting);
        }
        if (mUpdateThrottle != 0) {
            writer.print(prefix);
            writer.print("mUpdateThrottle=");
            android.support.v4.util.TimeUtils.formatDuration(mUpdateThrottle, writer);
            writer.print(" mLastLoadCompleteTime=");
            android.support.v4.util.TimeUtils.formatDuration(mLastLoadCompleteTime, android.os.SystemClock.uptimeMillis(), writer);
            writer.println();
        }
    }
}

