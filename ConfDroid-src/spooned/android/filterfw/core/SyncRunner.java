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
public class SyncRunner extends android.filterfw.core.GraphRunner {
    private android.filterfw.core.Scheduler mScheduler = null;

    private android.filterfw.core.GraphRunner.OnRunnerDoneListener mDoneListener = null;

    private java.util.concurrent.ScheduledThreadPoolExecutor mWakeExecutor = new java.util.concurrent.ScheduledThreadPoolExecutor(1);

    private android.os.ConditionVariable mWakeCondition = new android.os.ConditionVariable();

    private android.filterfw.core.StopWatchMap mTimer = null;

    private final boolean mLogVerbose;

    private static final java.lang.String TAG = "SyncRunner";

    // TODO: Provide factory based constructor?
    public SyncRunner(android.filterfw.core.FilterContext context, android.filterfw.core.FilterGraph graph, java.lang.Class schedulerClass) {
        super(context);
        mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.SyncRunner.TAG, android.util.Log.VERBOSE);
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Initializing SyncRunner");

        // Create the scheduler
        if (android.filterfw.core.Scheduler.class.isAssignableFrom(schedulerClass)) {
            try {
                java.lang.reflect.Constructor schedulerConstructor = schedulerClass.getConstructor(android.filterfw.core.FilterGraph.class);
                mScheduler = ((android.filterfw.core.Scheduler) (schedulerConstructor.newInstance(graph)));
            } catch (java.lang.NoSuchMethodException e) {
                throw new java.lang.RuntimeException("Scheduler does not have constructor <init>(FilterGraph)!", e);
            } catch (java.lang.InstantiationException e) {
                throw new java.lang.RuntimeException("Could not instantiate the Scheduler instance!", e);
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.RuntimeException("Cannot access Scheduler constructor!", e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new java.lang.RuntimeException("Scheduler constructor threw an exception", e);
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException("Could not instantiate Scheduler", e);
            }
        } else {
            throw new java.lang.IllegalArgumentException("Class provided is not a Scheduler subclass!");
        }
        // Associate this runner and the graph with the context
        mFilterContext = context;
        mFilterContext.addGraph(graph);
        mTimer = new android.filterfw.core.StopWatchMap();
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Setting up filters");

        // Setup graph filters
        graph.setupFilters();
    }

    @java.lang.Override
    public android.filterfw.core.FilterGraph getGraph() {
        return mScheduler != null ? mScheduler.getGraph() : null;
    }

    public int step() {
        assertReadyToStep();
        if (!getGraph().isReady()) {
            throw new java.lang.RuntimeException("Trying to process graph that is not open!");
        }
        return performStep() ? android.filterfw.core.GraphRunner.RESULT_RUNNING : determinePostRunState();
    }

    public void beginProcessing() {
        mScheduler.reset();
        getGraph().beginProcessing();
    }

    public void close() {
        // Close filters
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Closing graph.");

        getGraph().closeFilters(mFilterContext);
        mScheduler.reset();
    }

    @java.lang.Override
    public void run() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Beginning run.");

        assertReadyToStep();
        // Preparation
        beginProcessing();
        boolean glActivated = activateGlContext();
        // Run
        boolean keepRunning = true;
        while (keepRunning) {
            keepRunning = performStep();
        } 
        // Cleanup
        if (glActivated) {
            deactivateGlContext();
        }
        // Call completion callback if set
        if (mDoneListener != null) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Calling completion listener.");

            mDoneListener.onRunnerDone(determinePostRunState());
        }
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Run complete");

    }

    @java.lang.Override
    public boolean isRunning() {
        return false;
    }

    @java.lang.Override
    public void setDoneCallback(android.filterfw.core.GraphRunner.OnRunnerDoneListener listener) {
        mDoneListener = listener;
    }

    @java.lang.Override
    public void stop() {
        throw new java.lang.RuntimeException("SyncRunner does not support stopping a graph!");
    }

    @java.lang.Override
    public synchronized java.lang.Exception getError() {
        return null;
    }

    protected void waitUntilWake() {
        mWakeCondition.block();
    }

    protected void processFilterNode(android.filterfw.core.Filter filter) {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Processing filter node");

        filter.performProcess(mFilterContext);
        if (filter.getStatus() == android.filterfw.core.Filter.STATUS_ERROR) {
            throw new java.lang.RuntimeException(("There was an error executing " + filter) + "!");
        } else
            if (filter.getStatus() == android.filterfw.core.Filter.STATUS_SLEEPING) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Scheduling filter wakeup");

                scheduleFilterWake(filter, filter.getSleepDelay());
            }

    }

    protected void scheduleFilterWake(android.filterfw.core.Filter filter, int delay) {
        // Close the wake condition
        mWakeCondition.close();
        // Schedule the wake-up
        final android.filterfw.core.Filter filterToSchedule = filter;
        final android.os.ConditionVariable conditionToWake = mWakeCondition;
        mWakeExecutor.schedule(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                filterToSchedule.unsetStatus(android.filterfw.core.Filter.STATUS_SLEEPING);
                conditionToWake.open();
            }
        }, delay, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    protected int determinePostRunState() {
        boolean isBlocked = false;
        for (android.filterfw.core.Filter filter : mScheduler.getGraph().getFilters()) {
            if (filter.isOpen()) {
                if (filter.getStatus() == android.filterfw.core.Filter.STATUS_SLEEPING) {
                    // If ANY node is sleeping, we return our state as sleeping
                    return android.filterfw.core.GraphRunner.RESULT_SLEEPING;
                } else {
                    // If a node is still open, it is blocked (by input or output)
                    return android.filterfw.core.GraphRunner.RESULT_BLOCKED;
                }
            }
        }
        return android.filterfw.core.GraphRunner.RESULT_FINISHED;
    }

    // Core internal methods ///////////////////////////////////////////////////////////////////////
    boolean performStep() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.SyncRunner.TAG, "Performing one step.");

        android.filterfw.core.Filter filter = mScheduler.scheduleNextNode();
        if (filter != null) {
            mTimer.start(filter.getName());
            processFilterNode(filter);
            mTimer.stop(filter.getName());
            return true;
        } else {
            return false;
        }
    }

    void assertReadyToStep() {
        if (mScheduler == null) {
            throw new java.lang.RuntimeException("Attempting to run schedule with no scheduler in place!");
        } else
            if (getGraph() == null) {
                throw new java.lang.RuntimeException("Calling step on scheduler with no graph in place!");
            }

    }
}

