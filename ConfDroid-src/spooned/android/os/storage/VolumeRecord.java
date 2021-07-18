/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.os.storage;


/**
 * Metadata for a storage volume which may not be currently present.
 *
 * @unknown 
 */
public class VolumeRecord implements android.os.Parcelable {
    public static final java.lang.String EXTRA_FS_UUID = "android.os.storage.extra.FS_UUID";

    public static final int USER_FLAG_INITED = 1 << 0;

    public static final int USER_FLAG_SNOOZED = 1 << 1;

    public final int type;

    public final java.lang.String fsUuid;

    public java.lang.String partGuid;

    public java.lang.String nickname;

    public int userFlags;

    public long createdMillis;

    public long lastTrimMillis;

    public long lastBenchMillis;

    public VolumeRecord(int type, java.lang.String fsUuid) {
        this.type = type;
        this.fsUuid = com.android.internal.util.Preconditions.checkNotNull(fsUuid);
    }

    public VolumeRecord(android.os.Parcel parcel) {
        type = parcel.readInt();
        fsUuid = parcel.readString();
        partGuid = parcel.readString();
        nickname = parcel.readString();
        userFlags = parcel.readInt();
        createdMillis = parcel.readLong();
        lastTrimMillis = parcel.readLong();
        lastBenchMillis = parcel.readLong();
    }

    public int getType() {
        return type;
    }

    public java.lang.String getFsUuid() {
        return fsUuid;
    }

    public java.lang.String getNickname() {
        return nickname;
    }

    public boolean isInited() {
        return (userFlags & android.os.storage.VolumeRecord.USER_FLAG_INITED) != 0;
    }

    public boolean isSnoozed() {
        return (userFlags & android.os.storage.VolumeRecord.USER_FLAG_SNOOZED) != 0;
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("VolumeRecord:");
        pw.increaseIndent();
        pw.printPair("type", android.util.DebugUtils.valueToString(android.os.storage.VolumeInfo.class, "TYPE_", type));
        pw.printPair("fsUuid", fsUuid);
        pw.printPair("partGuid", partGuid);
        pw.println();
        pw.printPair("nickname", nickname);
        pw.printPair("userFlags", android.util.DebugUtils.flagsToString(android.os.storage.VolumeRecord.class, "USER_FLAG_", userFlags));
        pw.println();
        pw.printPair("createdMillis", android.util.TimeUtils.formatForLogging(createdMillis));
        pw.printPair("lastTrimMillis", android.util.TimeUtils.formatForLogging(lastTrimMillis));
        pw.printPair("lastBenchMillis", android.util.TimeUtils.formatForLogging(lastBenchMillis));
        pw.decreaseIndent();
        pw.println();
    }

    @java.lang.Override
    public android.os.storage.VolumeRecord clone() {
        final android.os.Parcel temp = android.os.Parcel.obtain();
        try {
            writeToParcel(temp, 0);
            temp.setDataPosition(0);
            return android.os.storage.VolumeRecord.CREATOR.createFromParcel(temp);
        } finally {
            temp.recycle();
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.os.storage.VolumeRecord) {
            return java.util.Objects.equals(fsUuid, ((android.os.storage.VolumeRecord) (o)).fsUuid);
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return fsUuid.hashCode();
    }

    public static final android.os.Parcelable.Creator<android.os.storage.VolumeRecord> CREATOR = new android.os.Parcelable.Creator<android.os.storage.VolumeRecord>() {
        @java.lang.Override
        public android.os.storage.VolumeRecord createFromParcel(android.os.Parcel in) {
            return new android.os.storage.VolumeRecord(in);
        }

        @java.lang.Override
        public android.os.storage.VolumeRecord[] newArray(int size) {
            return new android.os.storage.VolumeRecord[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(type);
        parcel.writeString(fsUuid);
        parcel.writeString(partGuid);
        parcel.writeString(nickname);
        parcel.writeInt(userFlags);
        parcel.writeLong(createdMillis);
        parcel.writeLong(lastTrimMillis);
        parcel.writeLong(lastBenchMillis);
    }
}

