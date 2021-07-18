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
 * limitations under the License
 */
package android.app.job;


/**
 * <p>Entry point for the callback from the {@link android.app.job.JobScheduler}.</p>
 * <p>This is the base class that handles asynchronous requests that were previously scheduled. You
 * are responsible for overriding {@link JobService#onStartJob(JobParameters)}, which is where
 * you will implement your job logic.</p>
 * <p>This service executes each incoming job on a {@link android.os.Handler} running on your
 * application's main thread. This means that you <b>must</b> offload your execution logic to
 * another thread/handler/{@link android.os.AsyncTask} of your choosing. Not doing so will result
 * in blocking any future callbacks from the JobManager - specifically
 * {@link #onStopJob(android.app.job.JobParameters)}, which is meant to inform you that the
 * scheduling requirements are no longer being met.</p>
 */
public abstract class JobService extends android.app.Service {
    private static final java.lang.String TAG = "JobService";

    /**
     * Job services must be protected with this permission:
     *
     * <pre class="prettyprint">
     *     &#60;service android:name="MyJobService"
     *              android:permission="android.permission.BIND_JOB_SERVICE" &#62;
     *         ...
     *     &#60;/service&#62;
     * </pre>
     *
     * <p>If a job service is declared in the manifest but not protected with this
     * permission, that service will be ignored by the OS.
     */
    public static final java.lang.String PERMISSION_BIND = "android.permission.BIND_JOB_SERVICE";

    /**
     * Identifier for a message that will result in a call to
     * {@link #onStartJob(android.app.job.JobParameters)}.
     */
    private static final int MSG_EXECUTE_JOB = 0;

    /**
     * Message that will result in a call to {@link #onStopJob(android.app.job.JobParameters)}.
     */
    private static final int MSG_STOP_JOB = 1;

    /**
     * Message that the client has completed execution of this job.
     */
    private static final int MSG_JOB_FINISHED = 2;

    /**
     * Lock object for {@link #mHandler}.
     */
    private final java.lang.Object mHandlerLock = new java.lang.Object();

    /**
     * Handler we post jobs to. Responsible for calling into the client logic, and handling the
     * callback to the system.
     */
    @com.android.internal.annotations.GuardedBy("mHandlerLock")
    android.app.job.JobService.JobHandler mHandler;

    static final class JobInterface extends android.app.job.IJobService.Stub {
        final java.lang.ref.WeakReference<android.app.job.JobService> mService;

        JobInterface(android.app.job.JobService service) {
            mService = new java.lang.ref.WeakReference<>(service);
        }

        @java.lang.Override
        public void startJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException {
            android.app.job.JobService service = mService.get();
            if (service != null) {
                service.ensureHandler();
                android.os.Message m = android.os.Message.obtain(service.mHandler, android.app.job.JobService.MSG_EXECUTE_JOB, jobParams);
                m.sendToTarget();
            }
        }

        @java.lang.Override
        public void stopJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException {
            android.app.job.JobService service = mService.get();
            if (service != null) {
                service.ensureHandler();
                android.os.Message m = android.os.Message.obtain(service.mHandler, android.app.job.JobService.MSG_STOP_JOB, jobParams);
                m.sendToTarget();
            }
        }
    }

    android.app.job.IJobService mBinder;

    /**
     *
     *
     * @unknown 
     */
    void ensureHandler() {
        synchronized(mHandlerLock) {
            if (mHandler == null) {
                mHandler = new android.app.job.JobService.JobHandler(getMainLooper());
            }
        }
    }

    /**
     * Runs on application's main thread - callbacks are meant to offboard work to some other
     * (app-specified) mechanism.
     *
     * @unknown 
     */
    class JobHandler extends android.os.Handler {
        JobHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            final android.app.job.JobParameters params = ((android.app.job.JobParameters) (msg.obj));
            switch (msg.what) {
                case android.app.job.JobService.MSG_EXECUTE_JOB :
                    try {
                        boolean workOngoing = android.app.job.JobService.this.onStartJob(params);
                        ackStartMessage(params, workOngoing);
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(android.app.job.JobService.TAG, "Error while executing job: " + params.getJobId());
                        throw new java.lang.RuntimeException(e);
                    }
                    break;
                case android.app.job.JobService.MSG_STOP_JOB :
                    try {
                        boolean ret = android.app.job.JobService.this.onStopJob(params);
                        ackStopMessage(params, ret);
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(android.app.job.JobService.TAG, "Application unable to handle onStopJob.", e);
                        throw new java.lang.RuntimeException(e);
                    }
                    break;
                case android.app.job.JobService.MSG_JOB_FINISHED :
                    final boolean needsReschedule = msg.arg2 == 1;
                    android.app.job.IJobCallback callback = params.getCallback();
                    if (callback != null) {
                        try {
                            callback.jobFinished(params.getJobId(), needsReschedule);
                        } catch (android.os.RemoteException e) {
                            android.util.Log.e(android.app.job.JobService.TAG, "Error reporting job finish to system: binder has gone" + "away.");
                        }
                    } else {
                        android.util.Log.e(android.app.job.JobService.TAG, "finishJob() called for a nonexistent job id.");
                    }
                    break;
                default :
                    android.util.Log.e(android.app.job.JobService.TAG, "Unrecognised message received.");
                    break;
            }
        }

        private void ackStartMessage(android.app.job.JobParameters params, boolean workOngoing) {
            final android.app.job.IJobCallback callback = params.getCallback();
            final int jobId = params.getJobId();
            if (callback != null) {
                try {
                    callback.acknowledgeStartMessage(jobId, workOngoing);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.app.job.JobService.TAG, "System unreachable for starting job.");
                }
            } else {
                if (android.util.Log.isLoggable(android.app.job.JobService.TAG, android.util.Log.DEBUG)) {
                    android.util.Log.d(android.app.job.JobService.TAG, "Attempting to ack a job that has already been processed.");
                }
            }
        }

        private void ackStopMessage(android.app.job.JobParameters params, boolean reschedule) {
            final android.app.job.IJobCallback callback = params.getCallback();
            final int jobId = params.getJobId();
            if (callback != null) {
                try {
                    callback.acknowledgeStopMessage(jobId, reschedule);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.app.job.JobService.TAG, "System unreachable for stopping job.");
                }
            } else {
                if (android.util.Log.isLoggable(android.app.job.JobService.TAG, android.util.Log.DEBUG)) {
                    android.util.Log.d(android.app.job.JobService.TAG, "Attempting to ack a job that has already been processed.");
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public final android.os.IBinder onBind(android.content.Intent intent) {
        if (mBinder == null) {
            mBinder = new android.app.job.JobService.JobInterface(this);
        }
        return mBinder.asBinder();
    }

    /**
     * Override this method with the callback logic for your job. Any such logic needs to be
     * performed on a separate thread, as this function is executed on your application's main
     * thread.
     *
     * @param params
     * 		Parameters specifying info about this job, including the extras bundle you
     * 		optionally provided at job-creation time.
     * @return True if your service needs to process the work (on a separate thread). False if
    there's no more work to be done for this job.
     */
    public abstract boolean onStartJob(android.app.job.JobParameters params);

    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call {@link #jobFinished(JobParameters, boolean)}.
     *
     * <p>This will happen if the requirements specified at schedule time are no longer met. For
     * example you may have requested WiFi with
     * {@link android.app.job.JobInfo.Builder#setRequiredNetworkType(int)}, yet while your
     * job was executing the user toggled WiFi. Another example is if you had specified
     * {@link android.app.job.JobInfo.Builder#setRequiresDeviceIdle(boolean)}, and the phone left its
     * idle maintenance window. You are solely responsible for the behaviour of your application
     * upon receipt of this message; your app will likely start to misbehave if you ignore it. One
     * immediate repercussion is that the system will cease holding a wakelock for you.</p>
     *
     * @param params
     * 		Parameters specifying info about this job.
     * @return True to indicate to the JobManager whether you'd like to reschedule this job based
    on the retry criteria provided at job creation-time. False to drop the job. Regardless of
    the value returned, your job must stop executing.
     */
    public abstract boolean onStopJob(android.app.job.JobParameters params);

    /**
     * Callback to inform the JobManager you've finished executing. This can be called from any
     * thread, as it will ultimately be run on your application's main thread. When the system
     * receives this message it will release the wakelock being held.
     * <p>
     *     You can specify post-execution behaviour to the scheduler here with
     *     <code>needsReschedule </code>. This will apply a back-off timer to your job based on
     *     the default, or what was set with
     *     {@link android.app.job.JobInfo.Builder#setBackoffCriteria(long, int)}. The original
     *     requirements are always honoured even for a backed-off job. Note that a job running in
     *     idle mode will not be backed-off. Instead what will happen is the job will be re-added
     *     to the queue and re-executed within a future idle maintenance window.
     * </p>
     *
     * @param params
     * 		Parameters specifying system-provided info about this job, this was given to
     * 		your application in {@link #onStartJob(JobParameters)}.
     * @param needsReschedule
     * 		True if this job should be rescheduled according to the back-off
     * 		criteria specified at schedule-time. False otherwise.
     */
    public final void jobFinished(android.app.job.JobParameters params, boolean needsReschedule) {
        ensureHandler();
        android.os.Message m = android.os.Message.obtain(mHandler, android.app.job.JobService.MSG_JOB_FINISHED, params);
        m.arg2 = (needsReschedule) ? 1 : 0;
        m.sendToTarget();
    }
}

