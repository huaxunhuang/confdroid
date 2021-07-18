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
package android.print;


/**
 * This class represents the description of a print job. The print job
 * state includes properties such as its id, print attributes used for
 * generating the content, and so on. Note that the print jobs state may
 * change over time and this class represents a snapshot of this state.
 */
public final class PrintJobInfo implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.print.PrintJobInfo.STATE_CREATED, android.print.PrintJobInfo.STATE_QUEUED, android.print.PrintJobInfo.STATE_STARTED, android.print.PrintJobInfo.STATE_BLOCKED, android.print.PrintJobInfo.STATE_COMPLETED, android.print.PrintJobInfo.STATE_FAILED, android.print.PrintJobInfo.STATE_CANCELED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * Constant for matching any print job state.
     *
     * @unknown 
     */
    public static final int STATE_ANY = -1;

    /**
     * Constant for matching any print job state.
     *
     * @unknown 
     */
    public static final int STATE_ANY_VISIBLE_TO_CLIENTS = -2;

    /**
     * Constant for matching any active print job state.
     *
     * @unknown 
     */
    public static final int STATE_ANY_ACTIVE = -3;

    /**
     * Constant for matching any scheduled, i.e. delivered to a print
     * service, print job state.
     *
     * @unknown 
     */
    public static final int STATE_ANY_SCHEDULED = -4;

    /**
     * Print job state: The print job is being created but not yet
     * ready to be printed.
     * <p>
     * Next valid states: {@link #STATE_QUEUED}
     * </p>
     */
    public static final int STATE_CREATED = 1;

    /**
     * Print job state: The print jobs is created, it is ready
     * to be printed and should be processed.
     * <p>
     * Next valid states: {@link #STATE_STARTED}, {@link #STATE_FAILED},
     * {@link #STATE_CANCELED}
     * </p>
     */
    public static final int STATE_QUEUED = 2;

    /**
     * Print job state: The print job is being printed.
     * <p>
     * Next valid states: {@link #STATE_COMPLETED}, {@link #STATE_FAILED},
     * {@link #STATE_CANCELED}, {@link #STATE_BLOCKED}
     * </p>
     */
    public static final int STATE_STARTED = 3;

    /**
     * Print job state: The print job is blocked.
     * <p>
     * Next valid states: {@link #STATE_FAILED}, {@link #STATE_CANCELED},
     * {@link #STATE_STARTED}
     * </p>
     */
    public static final int STATE_BLOCKED = 4;

    /**
     * Print job state: The print job is successfully printed.
     * This is a terminal state.
     * <p>
     * Next valid states: None
     * </p>
     */
    public static final int STATE_COMPLETED = 5;

    /**
     * Print job state: The print job was printing but printing failed.
     * <p>
     * Next valid states: {@link #STATE_CANCELED}, {@link #STATE_STARTED}
     * </p>
     */
    public static final int STATE_FAILED = 6;

    /**
     * Print job state: The print job is canceled.
     * This is a terminal state.
     * <p>
     * Next valid states: None
     * </p>
     */
    public static final int STATE_CANCELED = 7;

    /**
     * The unique print job id.
     */
    private android.print.PrintJobId mId;

    /**
     * The human readable print job label.
     */
    private java.lang.String mLabel;

    /**
     * The unique id of the printer.
     */
    private android.print.PrinterId mPrinterId;

    /**
     * The name of the printer - internally used
     */
    private java.lang.String mPrinterName;

    /**
     * The state of the print job.
     */
    private int mState;

    /**
     * The id of the app that created the job.
     */
    private int mAppId;

    /**
     * Optional tag assigned by a print service.
     */
    private java.lang.String mTag;

    /**
     * The wall time when the print job was created.
     */
    private long mCreationTime;

    /**
     * How many copies to print.
     */
    private int mCopies;

    /**
     * The pages to print
     */
    private android.print.PageRange[] mPageRanges;

    /**
     * The print job attributes size.
     */
    private android.print.PrintAttributes mAttributes;

    /**
     * Information about the printed document.
     */
    private android.print.PrintDocumentInfo mDocumentInfo;

    /**
     * The progress made on printing this job or -1 if not set.
     */
    private float mProgress;

    /**
     * A short string describing the status of this job.
     */
    @android.annotation.Nullable
    private java.lang.CharSequence mStatus;

    /**
     * A string resource describing the status of this job.
     */
    @android.annotation.StringRes
    private int mStatusRes;

    @android.annotation.Nullable
    private java.lang.CharSequence mStatusResAppPackageName;

    /**
     * Advanced printer specific options.
     */
    private android.os.Bundle mAdvancedOptions;

    /**
     * Whether we are trying to cancel this print job.
     */
    private boolean mCanceling;

    /**
     *
     *
     * @unknown 
     */
    public PrintJobInfo() {
        mProgress = -1;
    }

    /**
     *
     *
     * @unknown 
     */
    public PrintJobInfo(android.print.PrintJobInfo other) {
        mId = other.mId;
        mLabel = other.mLabel;
        mPrinterId = other.mPrinterId;
        mPrinterName = other.mPrinterName;
        mState = other.mState;
        mAppId = other.mAppId;
        mTag = other.mTag;
        mCreationTime = other.mCreationTime;
        mCopies = other.mCopies;
        mPageRanges = other.mPageRanges;
        mAttributes = other.mAttributes;
        mDocumentInfo = other.mDocumentInfo;
        mProgress = other.mProgress;
        mStatus = other.mStatus;
        mStatusRes = other.mStatusRes;
        mStatusResAppPackageName = other.mStatusResAppPackageName;
        mCanceling = other.mCanceling;
        mAdvancedOptions = other.mAdvancedOptions;
    }

    private PrintJobInfo(@android.annotation.NonNull
    android.os.Parcel parcel) {
        mId = parcel.readParcelable(null);
        mLabel = parcel.readString();
        mPrinterId = parcel.readParcelable(null);
        mPrinterName = parcel.readString();
        mState = parcel.readInt();
        mAppId = parcel.readInt();
        mTag = parcel.readString();
        mCreationTime = parcel.readLong();
        mCopies = parcel.readInt();
        android.os.Parcelable[] parcelables = parcel.readParcelableArray(null);
        if (parcelables != null) {
            mPageRanges = new android.print.PageRange[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                mPageRanges[i] = ((android.print.PageRange) (parcelables[i]));
            }
        }
        mAttributes = ((android.print.PrintAttributes) (parcel.readParcelable(null)));
        mDocumentInfo = ((android.print.PrintDocumentInfo) (parcel.readParcelable(null)));
        mProgress = parcel.readFloat();
        mStatus = parcel.readCharSequence();
        mStatusRes = parcel.readInt();
        mStatusResAppPackageName = parcel.readCharSequence();
        mCanceling = parcel.readInt() == 1;
        mAdvancedOptions = parcel.readBundle();
        if (mAdvancedOptions != null) {
            com.android.internal.util.Preconditions.checkArgument(!mAdvancedOptions.containsKey(null));
        }
    }

    /**
     * Gets the unique print job id.
     *
     * @return The id.
     */
    @android.annotation.Nullable
    public android.print.PrintJobId getId() {
        return mId;
    }

    /**
     * Sets the unique print job id.
     *
     * @param id
     * 		The job id.
     * @unknown 
     */
    public void setId(@android.annotation.NonNull
    android.print.PrintJobId id) {
        this.mId = id;
    }

    /**
     * Gets the human readable job label.
     *
     * @return The label.
     */
    @android.annotation.NonNull
    public java.lang.String getLabel() {
        return mLabel;
    }

    /**
     * Sets the human readable job label.
     *
     * @param label
     * 		The label.
     * @unknown 
     */
    public void setLabel(@android.annotation.NonNull
    java.lang.String label) {
        mLabel = label;
    }

    /**
     * Gets the unique target printer id.
     *
     * @return The target printer id.
     */
    @android.annotation.Nullable
    public android.print.PrinterId getPrinterId() {
        return mPrinterId;
    }

    /**
     * Sets the unique target printer id.
     *
     * @param printerId
     * 		The target printer id.
     * @unknown 
     */
    public void setPrinterId(@android.annotation.NonNull
    android.print.PrinterId printerId) {
        mPrinterId = printerId;
    }

    /**
     * Gets the name of the target printer.
     *
     * @return The printer name.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getPrinterName() {
        return mPrinterName;
    }

    /**
     * Sets the name of the target printer.
     *
     * @param printerName
     * 		The printer name.
     * @unknown 
     */
    public void setPrinterName(@android.annotation.NonNull
    java.lang.String printerName) {
        mPrinterName = printerName;
    }

    /**
     * Gets the current job state.
     *
     * @return The job state.
     * @see #STATE_CREATED
     * @see #STATE_QUEUED
     * @see #STATE_STARTED
     * @see #STATE_COMPLETED
     * @see #STATE_BLOCKED
     * @see #STATE_FAILED
     * @see #STATE_CANCELED
     */
    @android.print.PrintJobInfo.State
    public int getState() {
        return mState;
    }

    /**
     * Sets the current job state.
     *
     * @param state
     * 		The job state.
     * @unknown 
     */
    public void setState(int state) {
        mState = state;
    }

    /**
     * Sets the progress of the print job.
     *
     * @param progress
     * 		the progress of the job
     * @unknown 
     */
    public void setProgress(@android.annotation.FloatRange(from = 0.0, to = 1.0)
    float progress) {
        com.android.internal.util.Preconditions.checkArgumentInRange(progress, 0, 1, "progress");
        mProgress = progress;
    }

    /**
     * Sets the status of the print job.
     *
     * @param status
     * 		the status of the job, can be null
     * @unknown 
     */
    public void setStatus(@android.annotation.Nullable
    java.lang.CharSequence status) {
        mStatusRes = 0;
        mStatusResAppPackageName = null;
        mStatus = status;
    }

    /**
     * Sets the status of the print job.
     *
     * @param status
     * 		The new status as a string resource
     * @param appPackageName
     * 		App package name the resource belongs to
     * @unknown 
     */
    public void setStatus(@android.annotation.StringRes
    int status, @android.annotation.NonNull
    java.lang.CharSequence appPackageName) {
        mStatus = null;
        mStatusRes = status;
        mStatusResAppPackageName = appPackageName;
    }

    /**
     * Sets the owning application id.
     *
     * @return The owning app id.
     * @unknown 
     */
    public int getAppId() {
        return mAppId;
    }

    /**
     * Sets the owning application id.
     *
     * @param appId
     * 		The owning app id.
     * @unknown 
     */
    public void setAppId(int appId) {
        mAppId = appId;
    }

    /**
     * Gets the optional tag assigned by a print service.
     *
     * @return The tag.
     * @unknown 
     */
    public java.lang.String getTag() {
        return mTag;
    }

    /**
     * Sets the optional tag assigned by a print service.
     *
     * @param tag
     * 		The tag.
     * @unknown 
     */
    public void setTag(java.lang.String tag) {
        mTag = tag;
    }

    /**
     * Gets the wall time in millisecond when this print job was created.
     *
     * @return The creation time in milliseconds.
     */
    public long getCreationTime() {
        return mCreationTime;
    }

    /**
     * Sets the wall time in milliseconds when this print job was created.
     *
     * @param creationTime
     * 		The creation time in milliseconds.
     * @unknown 
     */
    public void setCreationTime(long creationTime) {
        if (creationTime < 0) {
            throw new java.lang.IllegalArgumentException("creationTime must be non-negative.");
        }
        mCreationTime = creationTime;
    }

    /**
     * Gets the number of copies.
     *
     * @return The number of copies or zero if not set.
     */
    @android.annotation.IntRange(from = 0)
    public int getCopies() {
        return mCopies;
    }

    /**
     * Sets the number of copies.
     *
     * @param copyCount
     * 		The number of copies.
     * @unknown 
     */
    public void setCopies(int copyCount) {
        if (copyCount < 1) {
            throw new java.lang.IllegalArgumentException("Copies must be more than one.");
        }
        mCopies = copyCount;
    }

    /**
     * Gets the included pages.
     *
     * @return The included pages or <code>null</code> if not set.
     */
    @android.annotation.Nullable
    public android.print.PageRange[] getPages() {
        return mPageRanges;
    }

    /**
     * Sets the included pages.
     *
     * @param pageRanges
     * 		The included pages.
     * @unknown 
     */
    public void setPages(android.print.PageRange[] pageRanges) {
        mPageRanges = pageRanges;
    }

    /**
     * Gets the print job attributes.
     *
     * @return The attributes.
     */
    @android.annotation.NonNull
    public android.print.PrintAttributes getAttributes() {
        return mAttributes;
    }

    /**
     * Sets the print job attributes.
     *
     * @param attributes
     * 		The attributes.
     * @unknown 
     */
    public void setAttributes(android.print.PrintAttributes attributes) {
        mAttributes = attributes;
    }

    /**
     * Gets the info describing the printed document.
     *
     * @return The document info.
     * @unknown 
     */
    public android.print.PrintDocumentInfo getDocumentInfo() {
        return mDocumentInfo;
    }

    /**
     * Sets the info describing the printed document.
     *
     * @param info
     * 		The document info.
     * @unknown 
     */
    public void setDocumentInfo(android.print.PrintDocumentInfo info) {
        mDocumentInfo = info;
    }

    /**
     * Gets whether this print is being cancelled.
     *
     * @return True if the print job is being cancelled.
     * @unknown 
     */
    public boolean isCancelling() {
        return mCanceling;
    }

    /**
     * Sets whether this print is being cancelled.
     *
     * @param cancelling
     * 		True if the print job is being cancelled.
     * @unknown 
     */
    public void setCancelling(boolean cancelling) {
        mCanceling = cancelling;
    }

    /**
     * Gets whether this job has a given advanced (printer specific) print
     * option.
     *
     * @param key
     * 		The option key.
     * @return Whether the option is present.
     * @unknown 
     */
    public boolean hasAdvancedOption(java.lang.String key) {
        return (mAdvancedOptions != null) && mAdvancedOptions.containsKey(key);
    }

    /**
     * Gets the value of an advanced (printer specific) print option.
     *
     * @param key
     * 		The option key.
     * @return The option value.
     * @unknown 
     */
    public java.lang.String getAdvancedStringOption(java.lang.String key) {
        if (mAdvancedOptions != null) {
            return mAdvancedOptions.getString(key);
        }
        return null;
    }

    /**
     * Gets the value of an advanced (printer specific) print option.
     *
     * @param key
     * 		The option key.
     * @return The option value.
     * @unknown 
     */
    public int getAdvancedIntOption(java.lang.String key) {
        if (mAdvancedOptions != null) {
            return mAdvancedOptions.getInt(key);
        }
        return 0;
    }

    /**
     * Gets the advanced options.
     *
     * @return The advanced options.
     * @unknown 
     */
    public android.os.Bundle getAdvancedOptions() {
        return mAdvancedOptions;
    }

    /**
     * Sets the advanced options.
     *
     * @param options
     * 		The advanced options.
     * @unknown 
     */
    public void setAdvancedOptions(android.os.Bundle options) {
        mAdvancedOptions = options;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mId, flags);
        parcel.writeString(mLabel);
        parcel.writeParcelable(mPrinterId, flags);
        parcel.writeString(mPrinterName);
        parcel.writeInt(mState);
        parcel.writeInt(mAppId);
        parcel.writeString(mTag);
        parcel.writeLong(mCreationTime);
        parcel.writeInt(mCopies);
        parcel.writeParcelableArray(mPageRanges, flags);
        parcel.writeParcelable(mAttributes, flags);
        parcel.writeParcelable(mDocumentInfo, 0);
        parcel.writeFloat(mProgress);
        parcel.writeCharSequence(mStatus);
        parcel.writeInt(mStatusRes);
        parcel.writeCharSequence(mStatusResAppPackageName);
        parcel.writeInt(mCanceling ? 1 : 0);
        parcel.writeBundle(mAdvancedOptions);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrintJobInfo{");
        builder.append("label: ").append(mLabel);
        builder.append(", id: ").append(mId);
        builder.append(", state: ").append(android.print.PrintJobInfo.stateToString(mState));
        builder.append(", printer: " + mPrinterId);
        builder.append(", tag: ").append(mTag);
        builder.append(", creationTime: " + mCreationTime);
        builder.append(", copies: ").append(mCopies);
        builder.append(", attributes: " + (mAttributes != null ? mAttributes.toString() : null));
        builder.append(", documentInfo: " + (mDocumentInfo != null ? mDocumentInfo.toString() : null));
        builder.append(", cancelling: " + mCanceling);
        builder.append(", pages: " + (mPageRanges != null ? java.util.Arrays.toString(mPageRanges) : null));
        builder.append(", hasAdvancedOptions: " + (mAdvancedOptions != null));
        builder.append(", progress: " + mProgress);
        builder.append(", status: " + (mStatus != null ? mStatus.toString() : null));
        builder.append(", statusRes: " + mStatusRes);
        builder.append(", statusResAppPackageName: " + (mStatusResAppPackageName != null ? mStatusResAppPackageName.toString() : null));
        builder.append("}");
        return builder.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String stateToString(int state) {
        switch (state) {
            case android.print.PrintJobInfo.STATE_CREATED :
                {
                    return "STATE_CREATED";
                }
            case android.print.PrintJobInfo.STATE_QUEUED :
                {
                    return "STATE_QUEUED";
                }
            case android.print.PrintJobInfo.STATE_STARTED :
                {
                    return "STATE_STARTED";
                }
            case android.print.PrintJobInfo.STATE_BLOCKED :
                {
                    return "STATE_BLOCKED";
                }
            case android.print.PrintJobInfo.STATE_FAILED :
                {
                    return "STATE_FAILED";
                }
            case android.print.PrintJobInfo.STATE_COMPLETED :
                {
                    return "STATE_COMPLETED";
                }
            case android.print.PrintJobInfo.STATE_CANCELED :
                {
                    return "STATE_CANCELED";
                }
            default :
                {
                    return "STATE_UNKNOWN";
                }
        }
    }

    /**
     * Get the progress that has been made printing this job.
     *
     * @return the print progress or -1 if not set
     * @unknown 
     */
    @android.annotation.TestApi
    public float getProgress() {
        return mProgress;
    }

    /**
     * Get the status of this job.
     *
     * @param pm
     * 		Package manager used to resolve the string
     * @return the status of this job or null if not set
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.Nullable
    public java.lang.CharSequence getStatus(@android.annotation.NonNull
    android.content.pm.PackageManager pm) {
        if (mStatusRes == 0) {
            return mStatus;
        } else {
            try {
                return pm.getResourcesForApplication(mStatusResAppPackageName.toString()).getString(mStatusRes);
            } catch (android.content.pm.PackageManager.NameNotFoundException | android.content.res.Resources.NotFoundException e) {
                return null;
            }
        }
    }

    /**
     * Builder for creating a {@link PrintJobInfo}.
     */
    public static final class Builder {
        private final android.print.PrintJobInfo mPrototype;

        /**
         * Constructor.
         *
         * @param prototype
         * 		Prototype to use as a starting point.
         * 		Can be <code>null</code>.
         */
        public Builder(@android.annotation.Nullable
        android.print.PrintJobInfo prototype) {
            mPrototype = (prototype != null) ? new android.print.PrintJobInfo(prototype) : new android.print.PrintJobInfo();
        }

        /**
         * Sets the number of copies.
         *
         * @param copies
         * 		The number of copies.
         */
        public void setCopies(@android.annotation.IntRange(from = 1)
        int copies) {
            mPrototype.mCopies = copies;
        }

        /**
         * Sets the print job attributes.
         *
         * @param attributes
         * 		The attributes.
         */
        public void setAttributes(@android.annotation.NonNull
        android.print.PrintAttributes attributes) {
            mPrototype.mAttributes = attributes;
        }

        /**
         * Sets the included pages.
         *
         * @param pages
         * 		The included pages.
         */
        public void setPages(@android.annotation.NonNull
        android.print.PageRange[] pages) {
            mPrototype.mPageRanges = pages;
        }

        /**
         * Sets the progress of the print job.
         *
         * @param progress
         * 		the progress of the job
         * @unknown 
         */
        public void setProgress(@android.annotation.FloatRange(from = 0.0, to = 1.0)
        float progress) {
            com.android.internal.util.Preconditions.checkArgumentInRange(progress, 0, 1, "progress");
            mPrototype.mProgress = progress;
        }

        /**
         * Sets the status of the print job.
         *
         * @param status
         * 		the status of the job, can be null
         * @unknown 
         */
        public void setStatus(@android.annotation.Nullable
        java.lang.CharSequence status) {
            mPrototype.mStatus = status;
        }

        /**
         * Puts an advanced (printer specific) option.
         *
         * @param key
         * 		The option key.
         * @param value
         * 		The option value.
         */
        public void putAdvancedOption(@android.annotation.NonNull
        java.lang.String key, @android.annotation.Nullable
        java.lang.String value) {
            com.android.internal.util.Preconditions.checkNotNull(key, "key cannot be null");
            if (mPrototype.mAdvancedOptions == null) {
                mPrototype.mAdvancedOptions = new android.os.Bundle();
            }
            mPrototype.mAdvancedOptions.putString(key, value);
        }

        /**
         * Puts an advanced (printer specific) option.
         *
         * @param key
         * 		The option key.
         * @param value
         * 		The option value.
         */
        public void putAdvancedOption(@android.annotation.NonNull
        java.lang.String key, int value) {
            if (mPrototype.mAdvancedOptions == null) {
                mPrototype.mAdvancedOptions = new android.os.Bundle();
            }
            mPrototype.mAdvancedOptions.putInt(key, value);
        }

        /**
         * Creates a new {@link PrintJobInfo} instance.
         *
         * @return The new instance.
         */
        @android.annotation.NonNull
        public android.print.PrintJobInfo build() {
            return mPrototype;
        }
    }

    public static final android.os.Parcelable.Creator<android.print.PrintJobInfo> CREATOR = new android.os.Parcelable.Creator<android.print.PrintJobInfo>() {
        @java.lang.Override
        public android.print.PrintJobInfo createFromParcel(android.os.Parcel parcel) {
            return new android.print.PrintJobInfo(parcel);
        }

        @java.lang.Override
        public android.print.PrintJobInfo[] newArray(int size) {
            return new android.print.PrintJobInfo[size];
        }
    };
}

