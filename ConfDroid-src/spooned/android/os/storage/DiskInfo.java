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
 * Information about a physical disk which may contain one or more
 * {@link VolumeInfo}.
 *
 * @unknown 
 */
public class DiskInfo implements android.os.Parcelable {
    public static final java.lang.String ACTION_DISK_SCANNED = "android.os.storage.action.DISK_SCANNED";

    public static final java.lang.String EXTRA_DISK_ID = "android.os.storage.extra.DISK_ID";

    public static final java.lang.String EXTRA_VOLUME_COUNT = "android.os.storage.extra.VOLUME_COUNT";

    public static final int FLAG_ADOPTABLE = 1 << 0;

    public static final int FLAG_DEFAULT_PRIMARY = 1 << 1;

    public static final int FLAG_SD = 1 << 2;

    public static final int FLAG_USB = 1 << 3;

    public final java.lang.String id;

    public final int flags;

    public long size;

    public java.lang.String label;

    /**
     * Hacky; don't rely on this count
     */
    public int volumeCount;

    public java.lang.String sysPath;

    public DiskInfo(java.lang.String id, int flags) {
        this.id = com.android.internal.util.Preconditions.checkNotNull(id);
        this.flags = flags;
    }

    public DiskInfo(android.os.Parcel parcel) {
        id = parcel.readString();
        flags = parcel.readInt();
        size = parcel.readLong();
        label = parcel.readString();
        volumeCount = parcel.readInt();
        sysPath = parcel.readString();
    }

    @android.annotation.NonNull
    public java.lang.String getId() {
        return id;
    }

    private boolean isInteresting(java.lang.String label) {
        if (android.text.TextUtils.isEmpty(label)) {
            return false;
        }
        if (label.equalsIgnoreCase("ata")) {
            return false;
        }
        if (label.toLowerCase().contains("generic")) {
            return false;
        }
        if (label.toLowerCase().startsWith("usb")) {
            return false;
        }
        if (label.toLowerCase().startsWith("multiple")) {
            return false;
        }
        return true;
    }

    public java.lang.String getDescription() {
        final android.content.res.Resources res = android.content.res.Resources.getSystem();
        if ((flags & android.os.storage.DiskInfo.FLAG_SD) != 0) {
            if (isInteresting(label)) {
                return res.getString(com.android.internal.R.string.storage_sd_card_label, label);
            } else {
                return res.getString(com.android.internal.R.string.storage_sd_card);
            }
        } else
            if ((flags & android.os.storage.DiskInfo.FLAG_USB) != 0) {
                if (isInteresting(label)) {
                    return res.getString(com.android.internal.R.string.storage_usb_drive_label, label);
                } else {
                    return res.getString(com.android.internal.R.string.storage_usb_drive);
                }
            } else {
                return null;
            }

    }

    public boolean isAdoptable() {
        return (flags & android.os.storage.DiskInfo.FLAG_ADOPTABLE) != 0;
    }

    public boolean isDefaultPrimary() {
        return (flags & android.os.storage.DiskInfo.FLAG_DEFAULT_PRIMARY) != 0;
    }

    public boolean isSd() {
        return (flags & android.os.storage.DiskInfo.FLAG_SD) != 0;
    }

    public boolean isUsb() {
        return (flags & android.os.storage.DiskInfo.FLAG_USB) != 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
        dump(new com.android.internal.util.IndentingPrintWriter(writer, "    ", 80));
        return writer.toString();
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println(("DiskInfo{" + id) + "}:");
        pw.increaseIndent();
        pw.printPair("flags", android.util.DebugUtils.flagsToString(getClass(), "FLAG_", flags));
        pw.printPair("size", size);
        pw.printPair("label", label);
        pw.println();
        pw.printPair("sysPath", sysPath);
        pw.decreaseIndent();
        pw.println();
    }

    @java.lang.Override
    public android.os.storage.DiskInfo clone() {
        final android.os.Parcel temp = android.os.Parcel.obtain();
        try {
            writeToParcel(temp, 0);
            temp.setDataPosition(0);
            return android.os.storage.DiskInfo.CREATOR.createFromParcel(temp);
        } finally {
            temp.recycle();
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.os.storage.DiskInfo) {
            return java.util.Objects.equals(id, ((android.os.storage.DiskInfo) (o)).id);
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final android.os.Parcelable.Creator<android.os.storage.DiskInfo> CREATOR = new android.os.Parcelable.Creator<android.os.storage.DiskInfo>() {
        @java.lang.Override
        public android.os.storage.DiskInfo createFromParcel(android.os.Parcel in) {
            return new android.os.storage.DiskInfo(in);
        }

        @java.lang.Override
        public android.os.storage.DiskInfo[] newArray(int size) {
            return new android.os.storage.DiskInfo[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeInt(this.flags);
        parcel.writeLong(size);
        parcel.writeString(label);
        parcel.writeInt(volumeCount);
        parcel.writeString(sysPath);
    }
}

