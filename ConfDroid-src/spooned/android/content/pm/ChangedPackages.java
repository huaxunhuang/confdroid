/**
 * Copyright (c) 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.content.pm;


/**
 * Packages that have been changed since the last time they
 * were requested.
 *
 * @see PackageManager#getChangedPackages(int)
 */
public final class ChangedPackages implements android.os.Parcelable {
    /**
     * The last known sequence number for these changes
     */
    private final int mSequenceNumber;

    /**
     * The names of the packages that have changed
     */
    private final java.util.List<java.lang.String> mPackageNames;

    public ChangedPackages(int sequenceNumber, @android.annotation.NonNull
    java.util.List<java.lang.String> packageNames) {
        this.mSequenceNumber = sequenceNumber;
        this.mPackageNames = packageNames;
    }

    /**
     *
     *
     * @unknown 
     */
    protected ChangedPackages(android.os.Parcel in) {
        mSequenceNumber = in.readInt();
        mPackageNames = in.createStringArrayList();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mSequenceNumber);
        dest.writeStringList(mPackageNames);
    }

    /**
     * Returns the last known sequence number for these changes.
     */
    public int getSequenceNumber() {
        return mSequenceNumber;
    }

    /**
     * Returns the names of the packages that have changed.
     */
    @android.annotation.NonNull
    public java.util.List<java.lang.String> getPackageNames() {
        return mPackageNames;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.ChangedPackages> CREATOR = new android.os.Parcelable.Creator<android.content.pm.ChangedPackages>() {
        public android.content.pm.ChangedPackages createFromParcel(android.os.Parcel in) {
            return new android.content.pm.ChangedPackages(in);
        }

        public android.content.pm.ChangedPackages[] newArray(int size) {
            return new android.content.pm.ChangedPackages[size];
        }
    };
}

