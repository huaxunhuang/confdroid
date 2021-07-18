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
package android.net;


/**
 * An inclusive range of UIDs.
 *
 * @unknown 
 */
public final class UidRange implements android.os.Parcelable {
    public final int start;

    public final int stop;

    public UidRange(int startUid, int stopUid) {
        if (startUid < 0)
            throw new java.lang.IllegalArgumentException("Invalid start UID.");

        if (stopUid < 0)
            throw new java.lang.IllegalArgumentException("Invalid stop UID.");

        if (startUid > stopUid)
            throw new java.lang.IllegalArgumentException("Invalid UID range.");

        start = startUid;
        stop = stopUid;
    }

    public static android.net.UidRange createForUser(int userId) {
        return new android.net.UidRange(userId * android.os.UserHandle.PER_USER_RANGE, ((userId + 1) * android.os.UserHandle.PER_USER_RANGE) - 1);
    }

    public int getStartUser() {
        return start / android.os.UserHandle.PER_USER_RANGE;
    }

    public boolean contains(int uid) {
        return (start <= uid) && (uid <= stop);
    }

    /**
     *
     *
     * @return {@code true} if this range contains every UID contained by the {@param other} range.
     */
    public boolean containsRange(android.net.UidRange other) {
        return (start <= other.start) && (other.stop <= stop);
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + start;
        result = (31 * result) + stop;
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof android.net.UidRange) {
            android.net.UidRange other = ((android.net.UidRange) (o));
            return (start == other.start) && (stop == other.stop);
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (start + "-") + stop;
    }

    // implement the Parcelable interface
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(start);
        dest.writeInt(stop);
    }

    public static final android.os.Parcelable.Creator<android.net.UidRange> CREATOR = new android.os.Parcelable.Creator<android.net.UidRange>() {
        @java.lang.Override
        public android.net.UidRange createFromParcel(android.os.Parcel in) {
            int start = in.readInt();
            int stop = in.readInt();
            return new android.net.UidRange(start, stop);
        }

        @java.lang.Override
        public android.net.UidRange[] newArray(int size) {
            return new android.net.UidRange[size];
        }
    };
}

