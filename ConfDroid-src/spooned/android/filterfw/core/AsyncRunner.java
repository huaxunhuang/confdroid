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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class AsyncRunner extends android.filterfw.core.GraphRunner {
    private java.lang.Class mSchedulerClass;

    private android.filterfw.core.SyncRunner mRunner;

    private android.filterfw.core.AsyncRunner.AsyncRunnerTask mRunTask;

    private android.filterfw.core.GraphRunner.OnRunnerDoneListener mDoneListener;

    private boolean isProcessing;

    private java.lang.Exception mException;

    private class RunnerResult {
        public int status = android.filterfw.core.GraphRunner.RESULT_UNKNOWN;

        public java.lang.Exception exception;
    }

    private class AsyncRunnerTask extends android.os.AsyncTask<android.filterfw.core.SyncRunner, java.lang.Void, android.filterfw.core.AsyncRunner.RunnerResult> {
        private static final java.lang.String TAG = "AsyncRunnerTask";

        @java.lang.Override
        protected android.filterfw.core.AsyncRunner.RunnerResult doInBackground(android.filterfw.core.SyncRunner... runner) {
            android.filterfw.core.AsyncRunner.RunnerResult result = new android.filterfw.core.AsyncRunner.RunnerResult();
            try {
                if (runner.length > 1) {
                    throw new java.lang.RuntimeException("More than one runner received!");
                }
                runner[0].assertReadyToStep();
                // Preparation
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Starting background graph processing.");

                activateGlContext();
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Preparing filter graph for processing.");

                runner[0].beginProcessing();
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Running graph.");

                // Run loop
                result.status = android.filterfw.core.GraphRunner.RESULT_RUNNING;
                while ((!isCancelled()) && (result.status == android.filterfw.core.GraphRunner.RESULT_RUNNING)) {
                    if (!runner[0].performStep()) {
                        result.status = runner[0].determinePostRunState();
                        if (result.status == android.filterfw.core.GraphRunner.RESULT_SLEEPING) {
                            runner[0].waitUntilWake();
                            result.status = android.filterfw.core.GraphRunner.RESULT_RUNNING;
                        }
                    }
                } 
                // Cleanup
                if (isCancelled()) {
                    result.status = android.filterfw.core.GraphRunner.RESULT_STOPPED;
                }
            } catch (java.lang.Exception exception) {
                result.exception = exception;
                result.status = android.filterfw.core.GraphRunner.RESULT_ERROR;
            }
            // Deactivate context.
            try {
                deactivateGlContext();
            } catch (java.lang.Exception exception) {
                result.exception = exception;
                result.status = android.filterfw.core.GraphRunner.RESULT_ERROR;
            }
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Done with background graph processing.");

            return result;
        }

        @java.lang.Override
        protected void onCancelled(android.filterfw.core.AsyncRunner.RunnerResult result) {
            onPostExecute(result);
        }

        @java.lang.Override
        protected void onPostExecute(android.filterfw.core.AsyncRunner.RunnerResult result) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Starting post-execute.");

            setRunning(false);
            if (result == null) {
                // Cancelled before got to doInBackground
                result = new android.filterfw.core.AsyncRunner.RunnerResult();
                result.status = android.filterfw.core.GraphRunner.RESULT_STOPPED;
            }
            setException(result.exception);
            if ((result.status == android.filterfw.core.GraphRunner.RESULT_STOPPED) || (result.status == android.filterfw.core.GraphRunner.RESULT_ERROR)) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Closing filters.");

                try {
                    mRunner.close();
                } catch (java.lang.Exception exception) {
                    result.status = android.filterfw.core.GraphRunner.RESULT_ERROR;
                    setException(exception);
                }
            }
            if (mDoneListener != null) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Calling graph done callback.");

                mDoneListener.onRunnerDone(result.status);
            }
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.AsyncRunner.AsyncRunnerTask.TAG, "Completed post-execute.");

        }
    }

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "AsyncRunner";

    /**
     * Create a new asynchronous graph runner with the given filter
     * context, and the given scheduler class.
     *
     * Must be created on the UI thread.
     */
    public AsyncRunner(android.filterfw.core.FilterContext context, java.lang.Class schedulerClass) {
        super(context);
        mSchedulerClass = schedulerClass;
        mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.AsyncRunner.TAG, android.util.Log.VERBOSE);
    }

    /**
     * Create a new asynchronous graph runner with the given filter
     * context. Uses a default scheduler.
     *
     * Must be created on the UI thread.
     */
    public AsyncRunner(android.filterfw.core.FilterContext context) {
        super(context);
        mSchedulerClass = android.filterfw.core.SimpleScheduler.class;
        mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.AsyncRunner.TAG, android.util.Log.VERBOSE);
    }

    /**
     * Set a callback to be called in the UI thread once the AsyncRunner
     * completes running a graph, whether the completion is due to a stop() call
     * or the filters running out of data to process.
     */
    @java.lang.Override
    public void setDoneCallback(android.filterfw.core.GraphRunner.OnRunnerDoneListener listener) {
        mDoneListener = listener;
    }

    /**
     * Sets the graph to be run. Will call prepare() on graph. Cannot be called
     * when a graph is already running.
     */
    public synchronized void setGraph(android.filterfw.core.FilterGraph graph) {
        if (isRunning()) {
            throw new java.lang.RuntimeException("Graph is already running!");
        }
        mRunner = new android.filterfw.core.SyncRunner(mFilterContext, graph, mSchedulerClass);
    }

    @java.lang.Override
    public android.filterfw.core.FilterGraph getGraph() {
        return mRunner != null ? mRunner.getGraph() : null;
    }

    /**
     * Execute the graph in a background thread.
     */
    @java.lang.Override
    public synchronized void run() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.AsyncRunner.TAG, "Running graph.");

        setException(null);
        if (isRunning()) {
            throw new java.lang.RuntimeException("Graph is already running!");
        }
        if (mRunner == null) {
            throw new java.lang.RuntimeException("Cannot run before a graph is set!");
        }
        mRunTask = this.new AsyncRunnerTask();
        setRunning(true);
        mRunTask.execute(mRunner);
    }

    /**
     * Stop graph execution. This is an asynchronous call; register a callback
     * with setDoneCallback to be notified of when the background processing has
     * been completed. Calling stop will close the filter graph.
     */
    @java.lang.Override
    public synchronized void stop() {
        if ((mRunTask != null) && (!mRunTask.isCancelled())) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.AsyncRunner.TAG, "Stopping graph.");

            mRunTask.cancel(false);
        }
    }

    @java.lang.Override
    public synchronized void close() {
        if (isRunning()) {
            throw new java.lang.RuntimeException("Cannot close graph while it is running!");
        }
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.AsyncRunner.TAG, "Closing filters.");

        mRunner.close();
    }

    /**
     * Check if background processing is happening
     */
    @java.lang.Override
    public synchronized boolean isRunning() {
        return isProcessing;
    }

    @java.lang.Override
    public synchronized java.lang.Exception getError() {
        return mException;
    }

    private synchronized void setRunning(boolean running) {
        isProcessing = running;
    }

    private synchronized void setException(java.lang.Exception exception) {
        mException = exception;
    }
}

