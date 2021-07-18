/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.content.rollback;


/**
 * Information about a set of packages that can be, or already have been
 * rolled back together.
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
public final class RollbackInfo implements android.os.Parcelable {
    /**
     * A unique identifier for the rollback.
     */
    private final int mRollbackId;

    private final java.util.List<android.content.rollback.PackageRollbackInfo> mPackages;

    private final java.util.List<android.content.pm.VersionedPackage> mCausePackages;

    private final boolean mIsStaged;

    private int mCommittedSessionId;

    /**
     *
     *
     * @unknown 
     */
    public RollbackInfo(int rollbackId, java.util.List<android.content.rollback.PackageRollbackInfo> packages, boolean isStaged, java.util.List<android.content.pm.VersionedPackage> causePackages, int committedSessionId) {
        this.mRollbackId = rollbackId;
        this.mPackages = packages;
        this.mIsStaged = isStaged;
        this.mCausePackages = causePackages;
        this.mCommittedSessionId = committedSessionId;
    }

    private RollbackInfo(android.os.Parcel in) {
        mRollbackId = in.readInt();
        mPackages = in.createTypedArrayList(this.CREATOR);
        mIsStaged = in.readBoolean();
        mCausePackages = in.createTypedArrayList(android.content.pm.VersionedPackage.CREATOR);
        mCommittedSessionId = in.readInt();
    }

    /**
     * Returns a unique identifier for this rollback.
     */
    public int getRollbackId() {
        return mRollbackId;
    }

    /**
     * Returns the list of package that are rolled back.
     */
    @android.annotation.NonNull
    public java.util.List<android.content.rollback.PackageRollbackInfo> getPackages() {
        return mPackages;
    }

    /**
     * Returns true if this rollback requires reboot to take effect after
     * being committed.
     */
    public boolean isStaged() {
        return mIsStaged;
    }

    /**
     * Returns the session ID for the committed rollback for staged rollbacks.
     * Only applicable for rollbacks that have been committed.
     */
    public int getCommittedSessionId() {
        return mCommittedSessionId;
    }

    /**
     * Sets the session ID for the committed rollback for staged rollbacks.
     *
     * @unknown 
     */
    public void setCommittedSessionId(int sessionId) {
        mCommittedSessionId = sessionId;
    }

    /**
     * Gets the list of package versions that motivated this rollback.
     * As provided to {@link #commitRollback} when the rollback was committed.
     * This is only applicable for rollbacks that have been committed.
     */
    @android.annotation.NonNull
    public java.util.List<android.content.pm.VersionedPackage> getCausePackages() {
        return mCausePackages;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mRollbackId);
        out.writeTypedList(mPackages);
        out.writeBoolean(mIsStaged);
        out.writeTypedList(mCausePackages);
        out.writeInt(mCommittedSessionId);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.rollback.RollbackInfo> CREATOR = new android.os.Parcelable.Creator<android.content.rollback.RollbackInfo>() {
        public android.content.rollback.RollbackInfo createFromParcel(android.os.Parcel in) {
            return new android.content.rollback.RollbackInfo(in);
        }

        public android.content.rollback.RollbackInfo[] newArray(int size) {
            return new android.content.rollback.RollbackInfo[size];
        }
    };
}

