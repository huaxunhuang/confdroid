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
/**
 * in android.app so ContextImpl has package access
 */
package android.app;


/**
 * Concrete implementation of the JobScheduler interface
 *
 * @unknown 
 */
public class JobSchedulerImpl extends android.app.job.JobScheduler {
    android.app.job.IJobScheduler mBinder;

    /* package */
    JobSchedulerImpl(android.app.job.IJobScheduler binder) {
        mBinder = binder;
    }

    @java.lang.Override
    public int schedule(android.app.job.JobInfo job) {
        try {
            return mBinder.schedule(job);
        } catch (android.os.RemoteException e) {
            return android.app.job.JobScheduler.RESULT_FAILURE;
        }
    }

    @java.lang.Override
    public int scheduleAsPackage(android.app.job.JobInfo job, java.lang.String packageName, int userId, java.lang.String tag) {
        try {
            return mBinder.scheduleAsPackage(job, packageName, userId, tag);
        } catch (android.os.RemoteException e) {
            return android.app.job.JobScheduler.RESULT_FAILURE;
        }
    }

    @java.lang.Override
    public void cancel(int jobId) {
        try {
            mBinder.cancel(jobId);
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public void cancelAll() {
        try {
            mBinder.cancelAll();
        } catch (android.os.RemoteException e) {
        }
    }

    @java.lang.Override
    public java.util.List<android.app.job.JobInfo> getAllPendingJobs() {
        try {
            return mBinder.getAllPendingJobs();
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    @java.lang.Override
    public android.app.job.JobInfo getPendingJob(int jobId) {
        try {
            return mBinder.getPendingJob(jobId);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }
}

