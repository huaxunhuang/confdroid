/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.printservice;


/**
 * This class represents a print job from the perspective of a print
 * service. It provides APIs for observing the print job state and
 * performing operations on the print job.
 * <p>
 * <strong>Note: </strong> All methods of this class must be invoked on
 * the main application thread.
 * </p>
 */
public final class PrintJob {
    private static final java.lang.String LOG_TAG = "PrintJob";

    private final android.printservice.IPrintServiceClient mPrintServiceClient;

    private final android.printservice.PrintDocument mDocument;

    private android.print.PrintJobInfo mCachedInfo;

    /**
     * Context that created the object
     */
    private final android.content.Context mContext;

    PrintJob(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.print.PrintJobInfo jobInfo, @android.annotation.NonNull
    android.printservice.IPrintServiceClient client) {
        mContext = context;
        mCachedInfo = jobInfo;
        mPrintServiceClient = client;
        mDocument = new android.printservice.PrintDocument(mCachedInfo.getId(), client, jobInfo.getDocumentInfo());
    }

    /**
     * Gets the unique print job id.
     *
     * @return The id.
     */
    @android.annotation.MainThread
    public android.print.PrintJobId getId() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return mCachedInfo.getId();
    }

    /**
     * Gets the {@link PrintJobInfo} that describes this job.
     * <p>
     * <strong>Node:</strong>The returned info object is a snapshot of the
     * current print job state. Every call to this method returns a fresh
     * info object that reflects the current print job state.
     * </p>
     *
     * @return The print job info.
     */
    @android.annotation.MainThread
    @android.annotation.NonNull
    public android.print.PrintJobInfo getInfo() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        if (isInImmutableState()) {
            return mCachedInfo;
        }
        android.print.PrintJobInfo info = null;
        try {
            info = mPrintServiceClient.getPrintJobInfo(mCachedInfo.getId());
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Couldn't get info for job: " + mCachedInfo.getId(), re);
        }
        if (info != null) {
            mCachedInfo = info;
        }
        return mCachedInfo;
    }

    /**
     * Gets the printed document.
     *
     * @return The document.
     */
    @android.annotation.MainThread
    @android.annotation.NonNull
    public android.printservice.PrintDocument getDocument() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return mDocument;
    }

    /**
     * Gets whether this print job is queued. Such a print job is
     * ready to be printed and can be started or cancelled.
     *
     * @return Whether the print job is queued.
     * @see #start()
     * @see #cancel()
     */
    @android.annotation.MainThread
    public boolean isQueued() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_QUEUED;
    }

    /**
     * Gets whether this print job is started. Such a print job is
     * being printed and can be completed or canceled or failed.
     *
     * @return Whether the print job is started.
     * @see #complete()
     * @see #cancel()
     * @see #fail(String)
     */
    @android.annotation.MainThread
    public boolean isStarted() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_STARTED;
    }

    /**
     * Gets whether this print job is blocked. Such a print job is halted
     * due to an abnormal condition and can be started or canceled or failed.
     *
     * @return Whether the print job is blocked.
     * @see #start()
     * @see #cancel()
     * @see #fail(String)
     */
    @android.annotation.MainThread
    public boolean isBlocked() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_BLOCKED;
    }

    /**
     * Gets whether this print job is completed. Such a print job
     * is successfully printed. This is a final state.
     *
     * @return Whether the print job is completed.
     * @see #complete()
     */
    @android.annotation.MainThread
    public boolean isCompleted() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_COMPLETED;
    }

    /**
     * Gets whether this print job is failed. Such a print job is
     * not successfully printed due to an error. This is a final state.
     *
     * @return Whether the print job is failed.
     * @see #fail(String)
     */
    @android.annotation.MainThread
    public boolean isFailed() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_FAILED;
    }

    /**
     * Gets whether this print job is cancelled. Such a print job was
     * cancelled as a result of a user request. This is a final state.
     *
     * @return Whether the print job is cancelled.
     * @see #cancel()
     */
    @android.annotation.MainThread
    public boolean isCancelled() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getState() == android.print.PrintJobInfo.STATE_CANCELED;
    }

    /**
     * Starts the print job. You should call this method if {@link #isQueued()} or {@link #isBlocked()} returns true and you started
     * resumed printing.
     * <p>
     * This resets the print status to null. Set the new status by using {@link #setStatus}.
     * </p>
     *
     * @return Whether the job was started.
     * @see #isQueued()
     * @see #isBlocked()
     */
    @android.annotation.MainThread
    public boolean start() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        final int state = getInfo().getState();
        if ((state == android.print.PrintJobInfo.STATE_QUEUED) || (state == android.print.PrintJobInfo.STATE_BLOCKED)) {
            return setState(android.print.PrintJobInfo.STATE_STARTED, null);
        }
        return false;
    }

    /**
     * Blocks the print job. You should call this method if {@link #isStarted()} returns true and
     * you need to block the print job. For example, the user has to add some paper to continue
     * printing. To resume the print job call {@link #start()}. To change the reason call
     * {@link #setStatus(CharSequence)}.
     *
     * @param reason
     * 		The human readable, short, and translated reason why the print job is blocked.
     * @return Whether the job was blocked.
     * @see #isStarted()
     * @see #isBlocked()
     */
    @android.annotation.MainThread
    public boolean block(@android.annotation.Nullable
    java.lang.String reason) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        android.print.PrintJobInfo info = getInfo();
        final int state = info.getState();
        if ((state == android.print.PrintJobInfo.STATE_STARTED) || (state == android.print.PrintJobInfo.STATE_BLOCKED)) {
            return setState(android.print.PrintJobInfo.STATE_BLOCKED, reason);
        }
        return false;
    }

    /**
     * Completes the print job. You should call this method if {@link #isStarted()} returns true and you are done printing.
     *
     * @return Whether the job as completed.
     * @see #isStarted()
     */
    @android.annotation.MainThread
    public boolean complete() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        if (isStarted()) {
            return setState(android.print.PrintJobInfo.STATE_COMPLETED, null);
        }
        return false;
    }

    /**
     * Fails the print job. You should call this method if {@link #isQueued()} or {@link #isStarted()} or {@link #isBlocked()}
     * returns true you failed while printing.
     *
     * @param error
     * 		The human readable, short, and translated reason
     * 		for the failure.
     * @return Whether the job was failed.
     * @see #isQueued()
     * @see #isStarted()
     * @see #isBlocked()
     */
    @android.annotation.MainThread
    public boolean fail(@android.annotation.Nullable
    java.lang.String error) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        if (!isInImmutableState()) {
            return setState(android.print.PrintJobInfo.STATE_FAILED, error);
        }
        return false;
    }

    /**
     * Cancels the print job. You should call this method if {@link #isQueued()} or {@link #isStarted() or #isBlocked()} returns
     * true and you canceled the print job as a response to a call to
     * {@link PrintService#onRequestCancelPrintJob(PrintJob)}.
     *
     * @return Whether the job is canceled.
     * @see #isStarted()
     * @see #isQueued()
     * @see #isBlocked()
     */
    @android.annotation.MainThread
    public boolean cancel() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        if (!isInImmutableState()) {
            return setState(android.print.PrintJobInfo.STATE_CANCELED, null);
        }
        return false;
    }

    /**
     * Sets the progress of this print job as a fraction of 1.
     *
     * @param progress
     * 		The new progress
     */
    @android.annotation.MainThread
    public void setProgress(@android.annotation.FloatRange(from = 0.0, to = 1.0)
    float progress) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        try {
            mPrintServiceClient.setProgress(mCachedInfo.getId(), progress);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Error setting progress for job: " + mCachedInfo.getId(), re);
        }
    }

    /**
     * Sets the status of this print job. This should be a human readable, short, and translated
     * description of the current state of the print job.
     * <p />
     * This overrides any previously set status set via {@link #setStatus(CharSequence)},
     * {@link #setStatus(int)}, {@link #block(String)}, or {@link #fail(String)},
     *
     * @param status
     * 		The new status. If null the status will be empty.
     */
    @android.annotation.MainThread
    public void setStatus(@android.annotation.Nullable
    java.lang.CharSequence status) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        try {
            mPrintServiceClient.setStatus(mCachedInfo.getId(), status);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Error setting status for job: " + mCachedInfo.getId(), re);
        }
    }

    /**
     * Sets the status of this print job as a string resource.
     * <p />
     * This overrides any previously set status set via {@link #setStatus(CharSequence)},
     * {@link #setStatus(int)}, {@link #block(String)}, or {@link #fail(String)},
     *
     * @param statusResId
     * 		The new status as a String resource. If 0 the status will be empty.
     */
    @android.annotation.MainThread
    public void setStatus(@android.annotation.StringRes
    int statusResId) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        try {
            mPrintServiceClient.setStatusRes(mCachedInfo.getId(), statusResId, mContext.getPackageName());
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Error setting status for job: " + mCachedInfo.getId(), re);
        }
    }

    /**
     * Sets a tag that is valid in the context of a {@link PrintService}
     * and is not interpreted by the system. For example, a print service
     * may set as a tag the key of the print job returned by a remote
     * print server, if the printing is off handed to a cloud based service.
     *
     * @param tag
     * 		The tag.
     * @return True if the tag was set, false otherwise.
     */
    @android.annotation.MainThread
    public boolean setTag(@android.annotation.NonNull
    java.lang.String tag) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        if (isInImmutableState()) {
            return false;
        }
        try {
            return mPrintServiceClient.setPrintJobTag(mCachedInfo.getId(), tag);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Error setting tag for job: " + mCachedInfo.getId(), re);
        }
        return false;
    }

    /**
     * Gets the print job tag.
     *
     * @return The tag or null.
     * @see #setTag(String)
     */
    @android.annotation.MainThread
    @android.annotation.Nullable
    public java.lang.String getTag() {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getTag();
    }

    /**
     * Gets the value of an advanced (printer specific) print option.
     *
     * @param key
     * 		The option key.
     * @return The option value.
     */
    @android.annotation.MainThread
    public java.lang.String getAdvancedStringOption(java.lang.String key) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getAdvancedStringOption(key);
    }

    /**
     * Gets whether this job has a given advanced (printer specific) print
     * option.
     *
     * @param key
     * 		The option key.
     * @return Whether the option is present.
     */
    @android.annotation.MainThread
    public boolean hasAdvancedOption(java.lang.String key) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().hasAdvancedOption(key);
    }

    /**
     * Gets the value of an advanced (printer specific) print option.
     *
     * @param key
     * 		The option key.
     * @return The option value.
     */
    @android.annotation.MainThread
    public int getAdvancedIntOption(java.lang.String key) {
        android.printservice.PrintService.throwIfNotCalledOnMainThread();
        return getInfo().getAdvancedIntOption(key);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.printservice.PrintJob other = ((android.printservice.PrintJob) (obj));
        return mCachedInfo.getId().equals(other.mCachedInfo.getId());
    }

    @java.lang.Override
    public int hashCode() {
        return mCachedInfo.getId().hashCode();
    }

    private boolean isInImmutableState() {
        final int state = mCachedInfo.getState();
        return ((state == android.print.PrintJobInfo.STATE_COMPLETED) || (state == android.print.PrintJobInfo.STATE_CANCELED)) || (state == android.print.PrintJobInfo.STATE_FAILED);
    }

    private boolean setState(int state, @android.annotation.Nullable
    java.lang.String error) {
        try {
            if (mPrintServiceClient.setPrintJobState(mCachedInfo.getId(), state, error)) {
                // Best effort - update the state of the cached info since
                // we may not be able to re-fetch it later if the job gets
                // removed from the spooler as a result of the state change.
                mCachedInfo.setState(state);
                mCachedInfo.setStatus(error);
                return true;
            }
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.printservice.PrintJob.LOG_TAG, "Error setting the state of job: " + mCachedInfo.getId(), re);
        }
        return false;
    }
}

