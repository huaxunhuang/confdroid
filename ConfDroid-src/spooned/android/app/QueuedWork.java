/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.app;


/**
 * Internal utility class to keep track of process-global work that's
 * outstanding and hasn't been finished yet.
 *
 * This was created for writing SharedPreference edits out
 * asynchronously so we'd have a mechanism to wait for the writes in
 * Activity.onPause and similar places, but we may use this mechanism
 * for other things in the future.
 *
 * @unknown 
 */
public class QueuedWork {
    // The set of Runnables that will finish or wait on any async
    // activities started by the application.
    private static final java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> sPendingWorkFinishers = new java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable>();

    private static java.util.concurrent.ExecutorService sSingleThreadExecutor = null;// lazy, guarded by class


    /**
     * Returns a single-thread Executor shared by the entire process,
     * creating it if necessary.
     */
    public static java.util.concurrent.ExecutorService singleThreadExecutor() {
        synchronized(android.app.QueuedWork.class) {
            if (android.app.QueuedWork.sSingleThreadExecutor == null) {
                // TODO: can we give this single thread a thread name?
                android.app.QueuedWork.sSingleThreadExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
            }
            return android.app.QueuedWork.sSingleThreadExecutor;
        }
    }

    /**
     * Add a runnable to finish (or wait for) a deferred operation
     * started in this context earlier.  Typically finished by e.g.
     * an Activity#onPause.  Used by SharedPreferences$Editor#startCommit().
     *
     * Note that this doesn't actually start it running.  This is just
     * a scratch set for callers doing async work to keep updated with
     * what's in-flight.  In the common case, caller code
     * (e.g. SharedPreferences) will pretty quickly call remove()
     * after an add().  The only time these Runnables are run is from
     * waitToFinish(), below.
     */
    public static void add(java.lang.Runnable finisher) {
        android.app.QueuedWork.sPendingWorkFinishers.add(finisher);
    }

    public static void remove(java.lang.Runnable finisher) {
        android.app.QueuedWork.sPendingWorkFinishers.remove(finisher);
    }

    /**
     * Finishes or waits for async operations to complete.
     * (e.g. SharedPreferences$Editor#startCommit writes)
     *
     * Is called from the Activity base class's onPause(), after
     * BroadcastReceiver's onReceive, after Service command handling,
     * etc.  (so async work is never lost)
     */
    public static void waitToFinish() {
        java.lang.Runnable toFinish;
        while ((toFinish = android.app.QueuedWork.sPendingWorkFinishers.poll()) != null) {
            toFinish.run();
        } 
    }

    /**
     * Returns true if there is pending work to be done.  Note that the
     * result is out of data as soon as you receive it, so be careful how you
     * use it.
     */
    public static boolean hasPendingWork() {
        return !android.app.QueuedWork.sPendingWorkFinishers.isEmpty();
    }
}

