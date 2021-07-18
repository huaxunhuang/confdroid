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
 * Information about a rollback available for a particular package.
 *
 * @unknown 
 */
@android.annotation.SystemApi
@android.annotation.TestApi
public final class PackageRollbackInfo implements android.os.Parcelable {
    private final android.content.pm.VersionedPackage mVersionRolledBackFrom;

    private final android.content.pm.VersionedPackage mVersionRolledBackTo;

    /**
     * Encapsulates information required to restore a snapshot of an app's userdata.
     *
     * @unknown 
     */
    public static class RestoreInfo {
        public final int userId;

        public final int appId;

        public final java.lang.String seInfo;

        public RestoreInfo(int userId, int appId, java.lang.String seInfo) {
            this.userId = userId;
            this.appId = appId;
            this.seInfo = seInfo;
        }
    }

    /* The list of users for which we need to backup userdata for this package. Backups of
    credential encrypted data are listed as pending if the user hasn't unlocked their device
    with credentials yet.
     */
    // NOTE: Not a part of the Parcelable representation of this object.
    private final android.util.IntArray mPendingBackups;

    /**
     * The list of users for which we need to restore userdata for this package. This field is
     * non-null only after a rollback for this package has been committed.
     */
    // NOTE: Not a part of the Parcelable representation of this object.
    private final java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> mPendingRestores;

    /**
     * Whether this instance represents the PackageRollbackInfo for an APEX module.
     */
    private final boolean mIsApex;

    /* The list of users the package is installed for. */
    // NOTE: Not a part of the Parcelable representation of this object.
    private final android.util.IntArray mInstalledUsers;

    /**
     * A mapping between user and an inode of theirs CE data snapshot.
     */
    // NOTE: Not a part of the Parcelable representation of this object.
    private final android.util.SparseLongArray mCeSnapshotInodes;

    /**
     * Returns the name of the package to roll back from.
     */
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mVersionRolledBackFrom.getPackageName();
    }

    /**
     * Returns the version of the package rolled back from.
     */
    @android.annotation.NonNull
    public android.content.pm.VersionedPackage getVersionRolledBackFrom() {
        return mVersionRolledBackFrom;
    }

    /**
     * Returns the version of the package rolled back to.
     */
    @android.annotation.NonNull
    public android.content.pm.VersionedPackage getVersionRolledBackTo() {
        return mVersionRolledBackTo;
    }

    /**
     *
     *
     * @unknown 
     */
    public void addPendingBackup(int userId) {
        mPendingBackups.add(userId);
    }

    /**
     *
     *
     * @unknown 
     */
    public android.util.IntArray getPendingBackups() {
        return mPendingBackups;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> getPendingRestores() {
        return mPendingRestores;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.rollback.PackageRollbackInfo.RestoreInfo getRestoreInfo(int userId) {
        for (android.content.rollback.PackageRollbackInfo.RestoreInfo ri : mPendingRestores) {
            if (ri.userId == userId) {
                return ri;
            }
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public void removeRestoreInfo(android.content.rollback.PackageRollbackInfo.RestoreInfo ri) {
        mPendingRestores.remove(ri);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isApex() {
        return mIsApex;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.util.IntArray getInstalledUsers() {
        return mInstalledUsers;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.util.SparseLongArray getCeSnapshotInodes() {
        return mCeSnapshotInodes;
    }

    /**
     *
     *
     * @unknown 
     */
    public void putCeSnapshotInode(int userId, long ceSnapshotInode) {
        mCeSnapshotInodes.put(userId, ceSnapshotInode);
    }

    /**
     *
     *
     * @unknown 
     */
    public void removePendingBackup(int userId) {
        int idx = mPendingBackups.indexOf(userId);
        if (idx != (-1)) {
            mPendingBackups.remove(idx);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void removePendingRestoreInfo(int userId) {
        removeRestoreInfo(getRestoreInfo(userId));
    }

    /**
     *
     *
     * @unknown 
     */
    public PackageRollbackInfo(android.content.pm.VersionedPackage packageRolledBackFrom, android.content.pm.VersionedPackage packageRolledBackTo, @android.annotation.NonNull
    android.util.IntArray pendingBackups, @android.annotation.NonNull
    java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> pendingRestores, boolean isApex, @android.annotation.NonNull
    android.util.IntArray installedUsers, @android.annotation.NonNull
    android.util.SparseLongArray ceSnapshotInodes) {
        this.mVersionRolledBackFrom = packageRolledBackFrom;
        this.mVersionRolledBackTo = packageRolledBackTo;
        this.mPendingBackups = pendingBackups;
        this.mPendingRestores = pendingRestores;
        this.mIsApex = isApex;
        this.mInstalledUsers = installedUsers;
        this.mCeSnapshotInodes = ceSnapshotInodes;
    }

    private PackageRollbackInfo(android.os.Parcel in) {
        this.mVersionRolledBackFrom = android.content.pm.VersionedPackage.CREATOR.createFromParcel(in);
        this.mVersionRolledBackTo = android.content.pm.VersionedPackage.CREATOR.createFromParcel(in);
        this.mIsApex = in.readBoolean();
        this.mPendingRestores = null;
        this.mPendingBackups = null;
        this.mInstalledUsers = null;
        this.mCeSnapshotInodes = null;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        mVersionRolledBackFrom.writeToParcel(out, flags);
        mVersionRolledBackTo.writeToParcel(out, flags);
        out.writeBoolean(mIsApex);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.rollback.PackageRollbackInfo> CREATOR = new android.os.Parcelable.Creator<android.content.rollback.PackageRollbackInfo>() {
        public android.content.rollback.PackageRollbackInfo createFromParcel(android.os.Parcel in) {
            return new android.content.rollback.PackageRollbackInfo(in);
        }

        public android.content.rollback.PackageRollbackInfo[] newArray(int size) {
            return new android.content.rollback.PackageRollbackInfo[size];
        }
    };
}

