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
package android.hardware.camera2.utils;


/**
 * Keep track of a single concurrent task starting and finishing;
 * allow draining the existing task and figuring out when the task has finished
 * (and won't restart).
 *
 * <p>The initial state is to allow all tasks to be started and finished. A task may only be started
 * once, after which it must be finished before starting again. Likewise, finishing a task
 * that hasn't been started is also not allowed.</p>
 *
 * <p>When draining begins, the task cannot be started again. This guarantees that at some
 * point the task will be finished forever, at which point the {@link DrainListener#onDrained}
 * callback will be invoked.</p>
 */
public class TaskSingleDrainer {
    private final android.hardware.camera2.utils.TaskDrainer<java.lang.Object> mTaskDrainer;

    private final java.lang.Object mSingleTask = new java.lang.Object();

    /**
     * Create a new task drainer; {@code onDrained} callbacks will be posted to the listener
     * via the {@code handler}.
     *
     * @param handler
     * 		a non-{@code null} handler to use to post runnables to
     * @param listener
     * 		a non-{@code null} listener where {@code onDrained} will be called
     */
    public TaskSingleDrainer(android.os.Handler handler, android.hardware.camera2.utils.TaskDrainer.DrainListener listener) {
        mTaskDrainer = new android.hardware.camera2.utils.TaskDrainer<>(handler, listener);
    }

    /**
     * Create a new task drainer; {@code onDrained} callbacks will be posted to the listener
     * via the {@code handler}.
     *
     * @param handler
     * 		a non-{@code null} handler to use to post runnables to
     * @param listener
     * 		a non-{@code null} listener where {@code onDrained} will be called
     * @param name
     * 		an optional name used for debug logging
     */
    public TaskSingleDrainer(android.os.Handler handler, android.hardware.camera2.utils.TaskDrainer.DrainListener listener, java.lang.String name) {
        mTaskDrainer = new android.hardware.camera2.utils.TaskDrainer<>(handler, listener, name);
    }

    /**
     * Mark this asynchronous task as having started.
     *
     * <p>The task cannot be started more than once without first having finished. Once
     * draining begins with {@link #beginDrain}, no new tasks can be started.</p>
     *
     * @see #taskFinished
     * @see #beginDrain
     * @throws IllegalStateException
     * 		If attempting to start a task which is already started (and not finished),
     * 		or if attempting to start a task after draining has begun.
     */
    public void taskStarted() {
        mTaskDrainer.taskStarted(mSingleTask);
    }

    /**
     * Do not allow any more task re-starts; once the existing task is finished,
     * fire the {@link DrainListener#onDrained} callback asynchronously.
     *
     * <p>This operation is idempotent; calling it more than once has no effect.</p>
     */
    public void beginDrain() {
        mTaskDrainer.beginDrain();
    }

    /**
     * Mark this asynchronous task as having finished.
     *
     * <p>The task cannot be finished if it hasn't started. Once finished, a task
     * cannot be finished again (unless it's started again).</p>
     *
     * @see #taskStarted
     * @see #beginDrain
     * @throws IllegalStateException
     * 		If attempting to start a task which is already finished (and not re-started),
     */
    public void taskFinished() {
        mTaskDrainer.taskFinished(mSingleTask);
    }
}

