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
 * This class represents the id of a print job.
 */
public final class PrintJobId implements android.os.Parcelable {
    @android.annotation.NonNull
    private final java.lang.String mValue;

    /**
     * Creates a new instance.
     *
     * @unknown 
     */
    public PrintJobId() {
        this(java.util.UUID.randomUUID().toString());
    }

    /**
     * Creates a new instance.
     *
     * @param value
     * 		The internal value.
     * @unknown 
     */
    public PrintJobId(@android.annotation.NonNull
    java.lang.String value) {
        mValue = value;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mValue.hashCode();
        return result;
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
        android.print.PrintJobId other = ((android.print.PrintJobId) (obj));
        if (!mValue.equals(other.mValue)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mValue);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens this id to a string.
     *
     * @return The flattened id.
     * @unknown 
     */
    @android.annotation.NonNull
    public java.lang.String flattenToString() {
        return mValue;
    }

    /**
     * Unflattens a print job id from a string.
     *
     * @param string
     * 		The string.
     * @return The unflattened id, or null if the string is malformed.
     * @unknown 
     */
    @android.annotation.NonNull
    public static android.print.PrintJobId unflattenFromString(@android.annotation.NonNull
    java.lang.String string) {
        return new android.print.PrintJobId(string);
    }

    public static final android.os.Parcelable.Creator<android.print.PrintJobId> CREATOR = new android.os.Parcelable.Creator<android.print.PrintJobId>() {
        @java.lang.Override
        public android.print.PrintJobId createFromParcel(android.os.Parcel parcel) {
            return new android.print.PrintJobId(com.android.internal.util.Preconditions.checkNotNull(parcel.readString()));
        }

        @java.lang.Override
        public android.print.PrintJobId[] newArray(int size) {
            return new android.print.PrintJobId[size];
        }
    };
}

