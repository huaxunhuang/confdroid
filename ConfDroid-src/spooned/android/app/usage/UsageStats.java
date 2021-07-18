/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package android.app.usage;


/**
 * Contains usage statistics for an app package for a specific
 * time range.
 */
public final class UsageStats implements android.os.Parcelable {
    /**
     * {@hide }
     */
    public java.lang.String mPackageName;

    /**
     * {@hide }
     */
    public long mBeginTimeStamp;

    /**
     * {@hide }
     */
    public long mEndTimeStamp;

    /**
     * Last time used by the user with an explicit action (notification, activity launch).
     * {@hide }
     */
    public long mLastTimeUsed;

    /**
     * {@hide }
     */
    public long mTotalTimeInForeground;

    /**
     * {@hide }
     */
    public int mLaunchCount;

    /**
     * {@hide }
     */
    public int mLastEvent;

    /**
     * {@hide }
     */
    public UsageStats() {
    }

    public UsageStats(android.app.usage.UsageStats stats) {
        mPackageName = stats.mPackageName;
        mBeginTimeStamp = stats.mBeginTimeStamp;
        mEndTimeStamp = stats.mEndTimeStamp;
        mLastTimeUsed = stats.mLastTimeUsed;
        mTotalTimeInForeground = stats.mTotalTimeInForeground;
        mLaunchCount = stats.mLaunchCount;
        mLastEvent = stats.mLastEvent;
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     * Get the beginning of the time range this {@link android.app.usage.UsageStats} represents,
     * measured in milliseconds since the epoch.
     * <p/>
     * See {@link System#currentTimeMillis()}.
     */
    public long getFirstTimeStamp() {
        return mBeginTimeStamp;
    }

    /**
     * Get the end of the time range this {@link android.app.usage.UsageStats} represents,
     * measured in milliseconds since the epoch.
     * <p/>
     * See {@link System#currentTimeMillis()}.
     */
    public long getLastTimeStamp() {
        return mEndTimeStamp;
    }

    /**
     * Get the last time this package was used, measured in milliseconds since the epoch.
     * <p/>
     * See {@link System#currentTimeMillis()}.
     */
    public long getLastTimeUsed() {
        return mLastTimeUsed;
    }

    /**
     * Get the total time this package spent in the foreground, measured in milliseconds.
     */
    public long getTotalTimeInForeground() {
        return mTotalTimeInForeground;
    }

    /**
     * Add the statistics from the right {@link UsageStats} to the left. The package name for
     * both {@link UsageStats} objects must be the same.
     *
     * @param right
     * 		The {@link UsageStats} object to merge into this one.
     * @throws java.lang.IllegalArgumentException
     * 		if the package names of the two
     * 		{@link UsageStats} objects are different.
     */
    public void add(android.app.usage.UsageStats right) {
        if (!mPackageName.equals(right.mPackageName)) {
            throw new java.lang.IllegalArgumentException(((("Can't merge UsageStats for package '" + mPackageName) + "' with UsageStats for package '") + right.mPackageName) + "'.");
        }
        if (right.mBeginTimeStamp > mBeginTimeStamp) {
            // The incoming UsageStat begins after this one, so use its last time used fields
            // as the source of truth.
            // We use the mBeginTimeStamp due to a bug where UsageStats files can overlap with
            // regards to their mEndTimeStamp.
            mLastEvent = right.mLastEvent;
            mLastTimeUsed = right.mLastTimeUsed;
        }
        mBeginTimeStamp = java.lang.Math.min(mBeginTimeStamp, right.mBeginTimeStamp);
        mEndTimeStamp = java.lang.Math.max(mEndTimeStamp, right.mEndTimeStamp);
        mTotalTimeInForeground += right.mTotalTimeInForeground;
        mLaunchCount += right.mLaunchCount;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mPackageName);
        dest.writeLong(mBeginTimeStamp);
        dest.writeLong(mEndTimeStamp);
        dest.writeLong(mLastTimeUsed);
        dest.writeLong(mTotalTimeInForeground);
        dest.writeInt(mLaunchCount);
        dest.writeInt(mLastEvent);
    }

    public static final android.os.Parcelable.Creator<android.app.usage.UsageStats> CREATOR = new android.os.Parcelable.Creator<android.app.usage.UsageStats>() {
        @java.lang.Override
        public android.app.usage.UsageStats createFromParcel(android.os.Parcel in) {
            android.app.usage.UsageStats stats = new android.app.usage.UsageStats();
            stats.mPackageName = in.readString();
            stats.mBeginTimeStamp = in.readLong();
            stats.mEndTimeStamp = in.readLong();
            stats.mLastTimeUsed = in.readLong();
            stats.mTotalTimeInForeground = in.readLong();
            stats.mLaunchCount = in.readInt();
            stats.mLastEvent = in.readInt();
            return stats;
        }

        @java.lang.Override
        public android.app.usage.UsageStats[] newArray(int size) {
            return new android.app.usage.UsageStats[size];
        }
    };
}

