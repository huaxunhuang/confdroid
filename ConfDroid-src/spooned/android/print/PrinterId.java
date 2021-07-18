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
 * This class represents the unique id of a printer.
 */
public final class PrinterId implements android.os.Parcelable {
    @android.annotation.NonNull
    private final android.content.ComponentName mServiceName;

    @android.annotation.NonNull
    private final java.lang.String mLocalId;

    /**
     * Creates a new instance.
     *
     * @param serviceName
     * 		The managing print service.
     * @param localId
     * 		The locally unique id within the managing service.
     * @unknown 
     */
    public PrinterId(@android.annotation.NonNull
    android.content.ComponentName serviceName, @android.annotation.NonNull
    java.lang.String localId) {
        mServiceName = serviceName;
        mLocalId = localId;
    }

    private PrinterId(@android.annotation.NonNull
    android.os.Parcel parcel) {
        mServiceName = com.android.internal.util.Preconditions.checkNotNull(((android.content.ComponentName) (parcel.readParcelable(null))));
        mLocalId = com.android.internal.util.Preconditions.checkNotNull(parcel.readString());
    }

    /**
     * The id of the print service this printer is managed by.
     *
     * @return The print service component name.
     * @unknown 
     */
    @android.annotation.NonNull
    public android.content.ComponentName getServiceName() {
        return mServiceName;
    }

    /**
     * Gets the id of this printer which is unique in the context
     * of the print service that manages it.
     *
     * @return The printer name.
     */
    @android.annotation.NonNull
    public java.lang.String getLocalId() {
        return mLocalId;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mServiceName, flags);
        parcel.writeString(mLocalId);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        android.print.PrinterId other = ((android.print.PrinterId) (object));
        if (!mServiceName.equals(other.mServiceName)) {
            return false;
        }
        if (!mLocalId.equals(other.mLocalId)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = (prime * hashCode) + mServiceName.hashCode();
        hashCode = (prime * hashCode) + mLocalId.hashCode();
        return hashCode;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrinterId{");
        builder.append("serviceName=").append(mServiceName.flattenToString());
        builder.append(", localId=").append(mLocalId);
        builder.append('}');
        return builder.toString();
    }

    public static final android.os.Parcelable.Creator<android.print.PrinterId> CREATOR = new android.os.Parcelable.Creator<android.print.PrinterId>() {
        @java.lang.Override
        public android.print.PrinterId createFromParcel(android.os.Parcel parcel) {
            return new android.print.PrinterId(parcel);
        }

        @java.lang.Override
        public android.print.PrinterId[] newArray(int size) {
            return new android.print.PrinterId[size];
        }
    };
}

