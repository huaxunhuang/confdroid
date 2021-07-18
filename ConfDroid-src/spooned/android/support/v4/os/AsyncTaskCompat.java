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
package android.support.v4.os;


/**
 * Helper for accessing features in {@link android.os.AsyncTask}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public final class AsyncTaskCompat {
    /**
     * Executes the task with the specified parameters, allowing multiple tasks to run in parallel
     * on a pool of threads managed by {@link android.os.AsyncTask}.
     *
     * @param task
     * 		The {@link android.os.AsyncTask} to execute.
     * @param params
     * 		The parameters of the task.
     * @return the instance of AsyncTask.
     */
    public static <Params, Progress, Result> android.os.AsyncTask<Params, Progress, Result> executeParallel(android.os.AsyncTask<Params, Progress, Result> task, Params... params) {
        if (task == null) {
            throw new java.lang.IllegalArgumentException("task can not be null");
        }
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // From API 11 onwards, we need to manually select the THREAD_POOL_EXECUTOR
            android.support.v4.os.AsyncTaskCompatHoneycomb.executeParallel(task, params);
        } else {
            // Before API 11, all tasks were run in parallel
            task.execute(params);
        }
        return task;
    }

    private AsyncTaskCompat() {
    }
}

